/**
 * 
 */
package com.gfi.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bis.gfi.entities.Bank;
import com.bis.gfi.entities.Group;
import com.bis.gfi.entities.GroupBank;
import com.bis.gfi.entities.GroupMembers;
import com.bis.gfi.enums.ActivityType;
import com.bis.gfi.enums.ChargeBearer;
import com.bis.gfi.enums.GroupCategory;
import com.bis.gfi.enums.MemberStatus;
import com.gfi.web.dao.BankDao;
import com.gfi.web.dao.GroupBankDao;
import com.gfi.web.dao.GroupDao;
import com.gfi.web.dao.GroupMembersDao;
import com.gfi.web.dao.ProjectDao;
import com.gfi.web.dao.SettingsDao;
import com.gfi.web.pojo.GroupDetails;
import com.gfi.web.pojo.NewGroupRequest;
import com.gfi.web.util.EmailUtil;

/**
 * @author Obi
 *
 */
@Service
public class GroupService {
	
	@Autowired
	SettingsDao settingsDao;
	
	@Autowired
	GroupDao groupDao;
	
	@Autowired
	BankDao bankDao;

	@Autowired
	ProjectDao projectDao;
	
	@Autowired
	GroupBankDao groupBankDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	GroupMembersDao groupMembersDao;
	
	@Autowired
	EmailUtil emailUtil;
	
	@Autowired
	ActivityService activityService;
	
	
	public boolean groupNameExists(String email, String groupName) {
		Group group = groupDao.findByNameAndAdmin(groupName, email);
		return group != null;
	}
	
	public boolean groupNameExists(String email, String groupName, Long groupId) {
		Group group = groupDao.findByNameAndAdmin(groupName, email);
		return (group != null && group.getId() != groupId);
	}
	
	public List<GroupDetails> getAllByAdmin(String email) {
		List<Group> groups = groupDao.findAllByAdmin(email);
		if (CollectionUtils.isEmpty(groups)) {
			return null;
		}
		
		List<GroupDetails> details = new ArrayList<>();
		for (Group group : groups) {
			GroupDetails detail = new GroupDetails();
			detail.setGroup(group);
			detail.setNoOfMembers(groupMembersDao.countGroupMembers(group.getId()));
			detail.setNoOfProjects(projectDao.countProjectsInGroup(group.getId()));
			details.add(detail);
		}
		return details;
	}
	
	public boolean isAdmin(Group group, String email) {
		return email.equals(group.getCreatedBy());
	}
	
	public boolean isGroupMember(Long groupId, String email) {
		return groupMembersDao.findByGroupAndEmail(groupId, email) != null;
	}

	@Transactional
	public boolean createGroup(NewGroupRequest groupReq, String adminEmail) throws Exception {
		Group group = new Group();
		group.setName(groupReq.getName());
		group.setCreatedBy(adminEmail);
		group.setDescription(groupReq.getDescription());
		group.setCategory(StringUtils.isNotBlank(groupReq.getCategory()) ? GroupCategory.valueOf(groupReq.getCategory()) : null);
		group.setFundAvailabilityDuration(groupReq.getFundDuration());
		group.setPrivateGroup(groupReq.getGroupPrivacy().equals("private"));
		group.setTargetAmount(Double.valueOf(groupReq.getTargetAmount()));
		group.setAllowAnonymousDonations(groupReq.isAllowAnonymous());
		group.setChargeBearer(ChargeBearer.valueOf(groupReq.getChargeBearer()));
		if (StringUtils.isNotBlank(groupReq.getLogo().getOriginalFilename())) {
			byte[] bytes = groupReq.getLogo().getBytes();
			group.setLogo(bytes);
			group.setBase64Image(new String(Base64.getEncoder().encode(bytes), "UTF-8"));
		}
		groupDao.create(group);
		createBankDetails(groupReq.getBankCode(), groupReq.getAcctNo(), group, true);
		addMemberToGroup(group, adminEmail);
		activityService.createActivity(adminEmail, ActivityType.CREATE_GROUP);
		return true;
	}
		
	@Transactional
	public void createBankDetails(String code, String acctNo, Group group, boolean primaryAcct) {
		if (!NumberUtils.isDigits(code) || StringUtils.isBlank(acctNo) || group == null) {
			return;
		}
		
		Bank bank = bankDao.findBankByCode(code);
		if (bank == null) {
			return;
		}
		createGroupBank(acctNo, null, bank, group, primaryAcct);
	}

	@Transactional
	public boolean editGroup(NewGroupRequest groupReq, Group group, String email) throws IOException {
		group.setName(groupReq.getName());
		group.setDescription(groupReq.getDescription());
		group.setCategory(StringUtils.isNotBlank(groupReq.getCategory()) ? GroupCategory.valueOf(groupReq.getCategory()) : null);
		group.setFundAvailabilityDuration(groupReq.getFundDuration());
		group.setPrivateGroup(groupReq.getGroupPrivacy().equals("private"));
		group.setTargetAmount(Double.valueOf(groupReq.getTargetAmount()));
		group.setAllowAnonymousDonations(groupReq.isAllowAnonymous());
		group.setChargeBearer(StringUtils.isNotBlank(groupReq.getChargeBearer()) ? ChargeBearer.valueOf(groupReq.getChargeBearer()) : null);
		if (StringUtils.isNotBlank(groupReq.getLogo().getOriginalFilename())) {
			byte[] bytes = groupReq.getLogo().getBytes();
			group.setLogo(bytes);
			group.setBase64Image(new String(Base64.getEncoder().encode(bytes), "UTF-8"));
		}
		groupDao.update(group);
		editBankDetails(groupReq.getBankCode(), groupReq.getAcctNo(), group);
		activityService.createActivity(email, ActivityType.EDIT_GROUP, group.getName());
		return true;
	}
	
	@Transactional
	public void editBankDetails(String code, String acctNo, Group group) {
		if (!NumberUtils.isDigits(code) || StringUtils.isBlank(acctNo) || group == null) {
			return;
		}
		
		Bank bank = bankDao.findBankByCode(code);
		if (bank == null) {
			return;
		}
		
		GroupBank groupBank = groupBankDao.findPrimaryAcctByGroup(group.getId());
		if (groupBank != null) {
			updateGroupBank(groupBank, acctNo, null, bank);
		} else {
			createGroupBank(acctNo, null, bank, group, true);
		}
	}
	
	@Transactional
	public void createGroupBank(String acctNo, String acctName, Bank bank, Group group, boolean primaryAcct) {
		GroupBank groupBank = new GroupBank();
		groupBank.setAccountNumber(acctNo);
		groupBank.setAccountName(acctName);
		groupBank.setPrimaryAccount(primaryAcct);
		groupBank.setBank(bank);
		groupBank.setGroup(group);
		groupBankDao.create(groupBank);
	}
	
	@Transactional
	public void updateGroupBank(GroupBank groupBank, String acctNo, String acctName, Bank bank) {
		groupBank.setAccountNumber(acctNo);
		groupBank.setAccountName(acctName);
		groupBank.setBank(bank);
		groupBankDao.update(groupBank);
	}
	
	@Transactional
	public boolean setActivationStatus(Group group, boolean status, String email) {
		if(group == null) {
			return false;
		}
		
		group.setActive(status);
		groupDao.update(group);
		activityService.createActivity(email, status ? ActivityType.ACTIVATE_GROUP : ActivityType.DEACTIVATE_GROUP, group.getName());
		return true;
	}
	
	@Transactional
	public boolean addMemberToGroup(Group group, String email) throws Exception {
		if(group == null || StringUtils.isBlank(email)) {
			return false;
		}
		
		GroupMembers member = groupMembersDao.findByGroupAndEmail(group.getId(), email);
		if (member != null) {
			member.setDeleted(false);
			member.setStatus(userService.emailExists(email) ? MemberStatus.SIGNED_UP : MemberStatus.PENDING);
			groupMembersDao.update(member);
		} else {
			member = new GroupMembers();
			member.setGroup(group);
			member.setEmail(email);
			member.setStatus(userService.emailExists(email) ? MemberStatus.SIGNED_UP : MemberStatus.PENDING);
			groupMembersDao.create(member);
		}
		emailUtil.sendGroupInvitation(group.getName(), email);
		return true;
	}
	
	@Transactional
	public boolean removeGroupMember(Long groupId, String email) {
		if(StringUtils.isBlank(email)) {
			return false;
		}
		
		GroupMembers member = groupMembersDao.findByGroupAndEmail(groupId, email);
		if (member != null) {
			groupMembersDao.delete(member);
		}
		return true;
	}
	
}
