/**
 * 
 */
package com.gfi.web.service;

import static com.gfi.web.util.AppConstants.HOME_DIR;
import static com.gfi.web.util.AppConstants.sdf;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bis.gfi.entities.Bank;
import com.bis.gfi.entities.Group;
import com.bis.gfi.entities.GroupMembers;
import com.bis.gfi.entities.PaystackAccount;
import com.bis.gfi.entities.Project;
import com.bis.gfi.entities.ProjectBank;
import com.bis.gfi.entities.Transaction;
import com.bis.gfi.enums.ActivityType;
import com.bis.gfi.enums.ChargeBearer;
import com.bis.gfi.enums.TransactionStatus;
import com.bis.gfi.enums.TransactionType;
import com.gfi.web.dao.BankDao;
import com.gfi.web.dao.GroupMembersDao;
import com.gfi.web.dao.PaystackAccountDao;
import com.gfi.web.dao.ProjectBankDao;
import com.gfi.web.dao.ProjectCategoryDao;
import com.gfi.web.dao.ProjectDao;
import com.gfi.web.dao.SettingsDao;
import com.gfi.web.dao.TransactionDao;
import com.gfi.web.enums.SettingsEnum;
import com.gfi.web.pojo.NewProjectRequest;
import com.gfi.web.pojo.ProjectDashboard;
import com.gfi.web.pojo.ProjectDetails;
import com.gfi.web.pojo.paystack.CreateRecipientResponse;
import com.gfi.web.pojo.paystack.InitiateTransferResponse;
import com.gfi.web.util.EmailUtil;
import com.gfi.web.util.ImageUtil;
import com.sun.jersey.api.client.ClientResponse;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Service
public class ProjectService {
	
	@Autowired
	ProjectCategoryDao projectCategoryDao;

	@Autowired
	ProjectDao projectDao;
	
	@Autowired
	BankDao bankDao;
	
	@Autowired
	ProjectBankDao projectBankDao;

	@Autowired
	ImageUtil imageUtil;
	
	@Autowired
	SettingsDao settingsDao;
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	GroupMembersDao groupMemberDao;
	
	@Autowired
	EmailUtil emailUtil;
	
	@Autowired
	PaystackAccountDao paystackAccountDao;
	
	@Autowired
	PaystackService paystackService;
	

	public boolean isFirstProject(String email) {
		return projectDao.countProjects(email) == 1;
	}
	
	public List<ProjectDetails> getAllByAdmin(String email) {
		return getProjectDetails(projectDao.findAllByAdmin(email));
	}
	
	public List<ProjectDetails> getProjectDetailsByGroup(Long groupId) {
		return getProjectDetails(projectDao.findByGroup(groupId));
	} 
	
	public List<ProjectDetails> getProjectDetails(List<Project> projects) {
		if (CollectionUtils.isEmpty(projects)) {
			return null;
		}
		
		List<ProjectDetails> details = new ArrayList<>();
		for (Project project : projects) {
			ProjectDetails detail = new ProjectDetails();
			detail.setProject(project);
			if (StringUtils.isNotEmpty(project.getProjectImages())) {
				detail.setImages(getProjectImages(project));
			}
			detail.setProjectStatus(getProjectDonatedPercentage(project));
			details.add(detail);
		}
		return details;
	}
	
	public ProjectDashboard getProjectDashboard(Project project) {
		ProjectDashboard dashboard = new ProjectDashboard();
		dashboard.setTargetAmt(project.getTargetAmount());
		Double donatedAmt = transactionDao.totalAmtContributedToProject(project.getId());
		dashboard.setDonatedAmt(donatedAmt == null ? 0 : donatedAmt);
		Double remaining = dashboard.getTargetAmt() - dashboard.getDonatedAmt();
		dashboard.setRemainingAmt(remaining < 0 ? 0 : remaining);
		dashboard.setDonations(transactionDao.findDonationsByProject(project.getId()));
		long percentageDonated = getProjectDonatedPercentage(project);
		dashboard.setDonatedPercentage(percentageDonated);
		dashboard.setRemainingPercentage(100l - percentageDonated);
		return dashboard;
	}
	
	@Transactional
	public boolean createProject(NewProjectRequest req, Group group, String email) {
		Project project = new Project();
		project.setActive(false);
		project.setName(req.getName());
		project.setDescription(req.getDescription());
		project.setPrivateProject(req.getProjectPrivacy().equals("private"));
		project.setTargetAmount(Double.valueOf(req.getTargetAmount()));
		project.setChargeBearer(ChargeBearer.valueOf(req.getChargeBearer()));
		project.setGroup(group);
		project.setCreatedBy(email);
		if (StringUtils.isNotBlank(req.getCategory())) {
			project.setCategory(projectCategoryDao.findById(Long.valueOf(req.getCategory())));
		}
		if (StringUtils.isNotBlank(req.getStartDate()) && StringUtils.isNotBlank(req.getEndDate())) {
			try {
				project.setStartDate(sdf.parse(req.getStartDate()));
				project.setEndDate(sdf.parse(req.getEndDate()));
			} catch (ParseException e) {
				log.error("Unable to format project date", e);
			}
		}
		String uuid = UUID.randomUUID().toString();
		project.setLink(uuid);
		project.setProjectImages(setProjectImages(uuid, req.getPics()));
		projectDao.create(project);
		createBankDetails(req.getBankCode(), req.getAcctNo(), req.getAcctName(), project, true);
		activityService.createActivity(email, ActivityType.CREATE_PROJECT);
		return true;
	}
	
	@Transactional
	public void createBankDetails(String code, String acctNo, String acctName, Project project, boolean primaryAcct) {
		if (!NumberUtils.isDigits(code) || acctNo == null || project == null) {
			return;
		}
		
		Bank bank = bankDao.findBankByCode(code);
		if (bank == null) {
			return;
		}
		createProjectBank(acctNo, acctName, bank, project, primaryAcct);
	}
	
	@Transactional
	public boolean editProject(NewProjectRequest req, Project project, Group group, String email) {
		project.setName(req.getName());
		project.setDescription(req.getDescription());
		project.setPrivateProject(req.getProjectPrivacy().equals("private"));
		project.setTargetAmount(Double.valueOf(req.getTargetAmount()));
		project.setChargeBearer(ChargeBearer.valueOf(req.getChargeBearer()));
		project.setGroup(group);
		if (StringUtils.isNotBlank(req.getCategory())) {
			project.setCategory(projectCategoryDao.findById(Long.valueOf(req.getCategory())));
		}
		if (StringUtils.isNotBlank(req.getStartDate()) && StringUtils.isNotBlank(req.getEndDate())) {
			try {
				project.setStartDate(sdf.parse(req.getStartDate()));
				project.setEndDate(sdf.parse(req.getEndDate()));
			} catch (ParseException e) {
				log.error("Unable to format project date", e);
			}
		}
		String uuid = UUID.randomUUID().toString();
		if (StringUtils.isBlank(project.getLink())) {
			project.setLink(uuid);
		}
		if (!CollectionUtils.isEmpty(req.getPics()) && StringUtils.isNotBlank(req.getPics().get(0).getOriginalFilename())) {
			String imageUrls = setProjectImages(project.getLink(), req.getPics());
			if (StringUtils.isNotBlank(imageUrls)) {
				project.setProjectImages(imageUrls);
			}
		}
		projectDao.update(project);
		editBankDetails(req.getBankCode(), req.getAcctNo(), req.getAcctName(), project);
		activityService.createActivity(email, ActivityType.EDIT_PROJECT, project.getName());
		return true;
	}
	
	@Transactional
	public void editBankDetails(String code, String acctNo, String acctName, Project project) {
		if (!NumberUtils.isDigits(code) || acctNo == null || project == null) {
			return;
		}
		
		Bank bank = bankDao.findBankByCode(code);
		if (bank == null) {
			return;
		}
		
		ProjectBank projectBank = projectBankDao.findPrimaryAcctByProject(project.getId());
		if (projectBank != null) {
			updateProjectBank(projectBank, acctNo, acctName, bank);
		} else {
			createProjectBank(acctNo, acctName, bank, project, true);
		}
	}
	
	@Transactional
	public void createProjectBank(String acctNo, String acctName, Bank bank, Project project, boolean primaryAcct) {
		ProjectBank projectBank = new ProjectBank();
		projectBank.setAccountNumber(acctNo);
		projectBank.setAccountName(acctName);
		projectBank.setPrimaryAccount(primaryAcct);
		projectBank.setBank(bank);
		projectBank.setProject(project);
		projectBankDao.create(projectBank);
	}
	
	@Transactional
	public void updateProjectBank(ProjectBank projectBank, String acctNo, String acctName, Bank bank) {
		projectBank.setAccountNumber(acctNo);
		projectBank.setAccountName(acctName);
		projectBank.setBank(bank);
		projectBankDao.update(projectBank);
	}
	
	@Transactional
	public boolean publishProject(Project project, String email) {
		project.setActive(true);
		projectDao.update(project);
		activityService.createActivity(email, ActivityType.PUBLISH_PROJECT, project.getName());
		List<GroupMembers> members = groupMemberDao.findMembersByGroup(project.getGroup().getId());
		emailUtil.sendPublishProject(project.getName(), project.getLink(), members);
		return true;
	}
	
	@Transactional
	public boolean deactivateProject(Project project, String email) {
		project.setActive(false);
		projectDao.update(project);
		activityService.createActivity(email, ActivityType.DEACTIVATE_PROJECT, project.getName());
		return true;
	}
	
	@Transactional
	public boolean payoutProject(Project project, String firstName, double amount, String email) {
		ProjectBank acct = project.getProjectBanks().iterator().next();
		//create recipient
		PaystackAccount paystackAcct = paystackAccountDao.findByBankAndAcctNo(acct.getBank().getCode(), acct.getAccountNumber());
		if (paystackAcct == null) {
			Bank bank = acct.getBank();
			CreateRecipientResponse resp = paystackService.createTransferRecipient(acct.getAccountName(), acct.getAccountNumber(), bank.getCode());
			if(resp == null || resp.getData() == null) {
				return false;
			}
			paystackAcct = paystackService.createPaystackAccount(resp);
		}
		
		//initiate transfer 
		InitiateTransferResponse trnsResp = paystackService.initiateTransfer(paystackAcct.getRecipientCode(), amount);
		if (trnsResp == null || !trnsResp.isStatus() || trnsResp.getData() == null) {
			return false;
		}
		
		boolean otpEnabled = settingsDao.getSettingBoolean(SettingsEnum.PAYSTACK_OTP_ENABLED);
		if (otpEnabled) {
			paystackService.createTransaction(amount, 0D, email, trnsResp.getData().getTransfer_code(), null, project, 
					TransactionType.PAYOUT, TransactionStatus.PENDING);
		} else {
			paystackService.createTransaction(amount, 0D, email, trnsResp.getData().getTransfer_code(), null, project, 
					TransactionType.PAYOUT, TransactionStatus.SUCCESSFUL);
			completeProjectPayout(project, email, firstName, amount);
		}
		return true;
	}
	
	@Deprecated
	@Transactional
	public boolean finalizePayout(Project project, String transRef, String otp, String email, 
			String firstName, double amount) {
		ClientResponse resp = paystackService.finalizeTransfer(transRef, otp);
		
		Transaction trans = transactionDao.findByRef(transRef);
		if (trans == null) {
			return false;
		}
		paystackService.updateTransaction(trans, TransactionStatus.SUCCESSFUL);
		completeProjectPayout(project, email, firstName, amount);
		return true;
	}
	
	@Transactional
	private void completeProjectPayout(Project project, String email, String firstName, double amount) {
		project.setCompleted(true);
		projectDao.update(project);
		activityService.createActivity(email, ActivityType.PAYOUT_PROJECT, project.getName());
		emailUtil.sendProjectPayout(email, firstName, amount, project.getName());
	}
	
	public List<String> getProjectImages(Project project) {
		if (project == null || StringUtils.isBlank(project.getProjectImages())) {
			return null;
		}
		
		List<String> images = new ArrayList<String>();
		String[] urls = project.getProjectImages().split(";");
		for (String url : urls) {
			File image = new File(url);
			try {
				byte[] bytes = FileUtils.readFileToByteArray(image);
				images.add(new String(Base64.getEncoder().encode(bytes), "UTF-8"));
			} catch (IOException e) {
				log.error("Unable to retrieve file: ", e);
			}
		}
		return images;
	}
	
	public byte[] getFirstImage(Project project) {
		if (project == null || StringUtils.isBlank(project.getProjectImages())) {
			return null;
		}
		
		String[] urls = project.getProjectImages().split(";");
		return imageUtil.getImageFromUrl(urls[0]);
	}
	
	public String getFirstImage(String imageUrls) {
		if (StringUtils.isBlank(imageUrls)) {
			return null;
		}
		
		String[] urls = imageUrls.split(";");		
		return imageUtil.getImageStringFromUrl(urls[0]);
	}

	private String setProjectImages(String uuid, List<MultipartFile> pics) {
		if(pics == null || pics.isEmpty() || StringUtils.isBlank(pics.get(0).getOriginalFilename())) {
			return null;
		}
		
		String projDir = System.getProperty(HOME_DIR) + settingsDao.getSetting(SettingsEnum.PROJECT_PIC_DIR) + uuid;
		//to avoid duplicates, delete the project folder before creating new images;
		deleteProjFolder(projDir);

		File rootFoler = ImageUtil.getDirectory(projDir);
		String fullPath = "";
		int size = pics.size();
		for (int i = 0; i < size; i++) {
			MultipartFile pic = pics.get(i);
			String imagePath = imageUtil.saveImage(pic, String.valueOf(i), rootFoler);
			fullPath += imagePath;
			if (i != size-1) {
				fullPath += ";";
			}
		}
		return fullPath;
	}

	private void deleteProjFolder(String projDir) {
		File folder = new File(projDir);
		if (folder.exists()) {
			try {
				FileUtils.deleteDirectory(folder);
			} catch (IOException e) {
				log.error("Unable to delete directory");
			}
		}
	}
	
	public long getProjectDonatedPercentage(Project project) {
		if (project == null) {
			return 0;
		}
		
		Double amount = transactionDao.totalAmtContributedToProject(project.getId());
		Double totalAmount = project.getTargetAmount();
		return getAsPercentage(amount, totalAmount);
	}
	
	public long getAsPercentage(Double amount, Double totalAmount) {
		long percentage = 0;
		try {
			if (totalAmount == null || totalAmount == 0) {
				percentage = 100;
			} else {
				percentage = Math.round((amount/totalAmount) * 100);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return percentage;
	}

}
