/**
 * 
 */
package com.gfi.web.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bis.gfi.entities.GroupMembers;
import com.bis.gfi.enums.MemberStatus;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;
import com.gfi.web.pojo.GroupDto;
import com.gfi.web.pojo.GroupMemberDto;
import com.gfi.web.util.ImageUtil;

/**
 * @author Obi
 *
 */
@Repository
public class GroupMembersDao extends StandardJpaEntityBaseDao<GroupMembers> {
	
	@Autowired
	ImageUtil imageUtil;

	@PostConstruct
    public void init() {
        super.setClazz(GroupMembers.class);
	}
	
	@Transactional
	public List<GroupMembers> findMembersByGroup(Long groupId) {
		String query = "select m from GroupMembers m where m.group.id = :groupId";
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", groupId);
        return executeQuery(query, map);
	}
	
	@Transactional
	public List<GroupMemberDto> findMemberDetailsByGroup(Long groupId) {
		String query = "select m.status, u.firstName, u.lastName, m.email, u.pictureUrl, u.aboutMe "
				+ "from GroupMembers m left join UserProfile u on m.email = u.email where m.group.id = :groupId";
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", groupId);
        return transformToGroupMember(executeQuery(query, map));
	}
	
	@Transactional
	public GroupMembers findByGroupAndEmail(Long groupId, String email) {
		String query = "select m from GroupMembers m where m.group.id = :groupId and m.email = :email";
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", groupId);
        map.put("email", email);
        return executeQueryUniqueResult(query, map);
	}
	
	@Transactional
	public List<GroupMembers> findByEmail(String email) {
		String query = "select m from GroupMembers m where m.email = :email";
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        return executeQuery(query, map);
	}	
	
	@Transactional
	public long countActiveGroupsByEmail(String email) {
		String query = "select count(m) from GroupMembers m where m.email = :email and m.active = true";
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        return executeQueryUniqueResult(query, map);
	}

	@Transactional
	public long countGroupMembers(Long groupId) {
		String hql = "select count(m) from GroupMembers m where m.group.id = :groupId";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("groupId", groupId);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
	@Transactional
	public List<GroupDto> findGroupsByMemberEmail(String email) {
		String query = "select g.id, g.name, g.createdBy from GroupMembers m join m.group g where m.email = :email";
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        return transformToGroup(executeQuery(query, map), email);
	}
	
	private List<GroupDto> transformToGroup(List<Object[]> objs, String adminEmail) {
		List<GroupDto> groups = new ArrayList<GroupDto>();
		if (!CollectionUtils.isEmpty(objs)) {
			for (Object[] obj : objs) {
				GroupDto group = new GroupDto();
				group.setId((Long) obj[0]);
				group.setName((String) obj[1]);			
				String email = (String) obj[2];
				group.setAdmin(adminEmail.equals(email));
				groups.add(group);
			}
		}			
		return groups;
	}
	
	private List<GroupMemberDto> transformToGroupMember(List<Object[]> objs) {
		List<GroupMemberDto> members = new ArrayList<GroupMemberDto>();
		if (!CollectionUtils.isEmpty(objs)) {
			for (Object[] obj : objs) {
				GroupMemberDto member = new GroupMemberDto();
				member.setStatus((MemberStatus) obj[0]);
				member.setFirstName((String) obj[1]);
				member.setLastName((String) obj[2]);
				member.setEmail((String) obj[3]);
				member.setPicture(imageUtil.getImageStringFromUrl((String) obj[4]));
				member.setAboutMe((String) obj[5]);
				members.add(member);
			}
		}
		return members;
	}
	
}
