/**
 * 
 */
package com.gfi.web.controllers;

import static com.gfi.web.util.AppConstants.REDIRECT;
import static com.gfi.web.util.AppConstants.df;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bis.gfi.entities.Project;
import com.bis.gfi.entities.Transaction;
import com.bis.gfi.entities.UserProfile;
import com.bis.gfi.enums.ActivityType;
import com.bis.gfi.enums.TransactionStatus;
import com.bis.gfi.enums.TransactionType;
import com.gfi.web.dao.ProjectDao;
import com.gfi.web.dao.TransactionDao;
import com.gfi.web.dao.UserDao;
import com.gfi.web.enums.ResponseCode;
import com.gfi.web.pojo.AmountResponse;
import com.gfi.web.pojo.DonationRequest;
import com.gfi.web.pojo.GenericResponse;
import com.gfi.web.pojo.TransactionDetailsResponse;
import com.gfi.web.pojo.paystack.TransInitializeReponse;
import com.gfi.web.service.ActivityService;
import com.gfi.web.service.GroupService;
import com.gfi.web.service.PaystackService;
import com.gfi.web.service.ProjectService;
import com.gfi.web.util.EmailUtil;
import com.gfi.web.util.PayCalculator;
import com.gfi.web.util.Utility;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
@RequestMapping("/na")
public class NonAuthContoller {

	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	ProjectDao projectDao;

	@Autowired
	UserDao userDao;
	
	@Autowired
	PaystackService paystackService;

	@Autowired
	ActivityService activityService;

	@Autowired
	ProjectService projectService;
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	PayCalculator payCalculator;

	@Autowired
	EmailUtil emailUtil;
	
	@Value("${anonymousEmail}")
    private String anonymousEmail;
		

	@RequestMapping(value = "/publicprojects", method = RequestMethod.GET)
	public String publicprojects(Model model, Principal principal) {
		log.info("fetching public projects");

		model.addAttribute("isAthenticated", principal != null);
		model.addAttribute("publicProjects", projectDao.findAllPublicProjects());
		return "modules/nonAuth/publicProjects";
	}
	
	@RequestMapping(value = "/lnk/{link}", method = RequestMethod.GET)
	public String getByLink(@PathVariable("link") String link, Model model, Principal principal) {
		Project project = projectDao.findByLink(link);
		if (project == null) {
			return REDIRECT + "/error-404";
		}

		model.addAttribute("project", project);
		model.addAttribute("projImages", projectService.getProjectImages(project));
		model.addAttribute("percentage", projectService.getProjectDonatedPercentage(project));
		model.addAttribute("creator", userDao.findFirstAndLastName(project.getCreatedBy()));
		model.addAttribute("allowAnonymous", project.getGroup().isAllowAnonymousDonations());
		
		if (principal != null) {
			if (project.isPrivateProject() && !groupService.isGroupMember(project.getGroup().getId(), principal.getName())) {
				return REDIRECT + "/accessdenied";
			}
			model.addAttribute("returnUrl", "/donate/projects");
			return "modules/donate/donate";
		} 
		
		if (project.isPrivateProject()) {
			return REDIRECT + "/accessdenied";
		}
		model.addAttribute("returnUrl", "/na/publicprojects");
		return "modules/nonAuth/donate";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getTransactionCharge", method = RequestMethod.POST)
	public AmountResponse getTransactionCharge(@RequestBody DonationRequest req, HttpServletRequest httpReq) {
		log.info("getting transaction fee");
		AmountResponse resp = new AmountResponse();
		
		try {
			resp.setAmount(payCalculator.getChargeAmount(req.getAmount()));
			resp.assignResponseCode(ResponseCode.SUCCESS);
		} catch(Exception e) {
			log.error("", e);
			resp.assignResponseCode(ResponseCode.SYSTEM_ERROR);
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value = "/donation/initiate", method = RequestMethod.POST)
	public TransactionDetailsResponse createTransaction(@RequestBody DonationRequest req, HttpServletRequest httpReq, Principal principal) {
		log.info("initiating donation");
		
		TransactionDetailsResponse resp = new TransactionDetailsResponse();
		Project project = projectDao.findById(Long.valueOf(req.getProjectId()));
		if (project == null) {
			resp.assignResponseCode(ResponseCode.NOT_FOUND);
			return resp;
		}
		
		String email = anonymousEmail;
		if (!req.isAnonymous()) {
			if (principal != null) {
				email = principal.getName();
			} else if (StringUtils.isNotBlank(req.getEmail())) {
				email = req.getEmail();
			}
		}
		
		try {
			Double transCharge = payCalculator.getChargeAmount(req.getAmount());
			Double totalAMount = req.getAmount() + transCharge;
			long chargeAmount = Math.round(100 * totalAMount);
			String ref = Utility.getRequestId();
			
			paystackService.createTransaction(totalAMount, transCharge, email, ref, null, project, TransactionType.DONATION, TransactionStatus.PENDING);
			resp.setAmount(chargeAmount);
			resp.setEmail(email);
			resp.setTransRef(ref);
			resp.assignResponseCode(ResponseCode.SUCCESS);
		} catch(Exception e) {
			resp.assignResponseCode(ResponseCode.SYSTEM_ERROR);
		}
		return resp;
	}
	
	//used for paystack standard
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "/donation/", method = RequestMethod.POST)
	public TransInitializeReponse initiateTransaction(@RequestBody DonationRequest req, HttpServletRequest httpReq, Principal principal) {
		log.info("initiating donation");
		
		TransInitializeReponse resp = new TransInitializeReponse();
		Project project = projectDao.findProjectEagerlyById(Long.valueOf(req.getProjectId()));
		if (project == null) {
			resp.assignResponseCode(ResponseCode.NOT_FOUND);
			return resp;
		}
		
		String email = anonymousEmail;
		if (!req.isAnonymous()) {
			if (principal != null) {
				email = principal.getName();
			} else if (StringUtils.isNotBlank(req.getEmail())) {
				email = req.getEmail();
			}
		}
		
		String rootUrl = httpReq.getRequestURL().toString().replace("initiate", "verify/");
		try {
			resp = paystackService.initializePayment(req.getAmount(), email, rootUrl, project);
		} catch(Exception e) {
			resp.assignResponseCode(ResponseCode.SYSTEM_ERROR);
		}
		return resp;
	}
	
	@RequestMapping(value = "/donation/verify/{ref}", method = RequestMethod.GET)
	public String verifyTransaction(@PathVariable("ref") String transRef, Model model, 
			RedirectAttributes attributes, Principal principal) {
		log.info("verifying transaction for : "+ transRef);
		
		Transaction trans = transactionDao.findByRef(transRef);
		if (trans == null) {
			log.info("transaction ref does not exist");
			return null;
		}
		
		try {
			GenericResponse resp = paystackService.verifyPayment(trans);
			if (resp.getCode().equals(ResponseCode.SUCCESS.getCode())) {
				String projectName = trans.getProject().getName();
				double amount = trans.getAmount();
				attributes.addFlashAttribute("successMessage", "Congratulations! You have successfully contributed NGN "+ df.format(amount) + " to " + projectName);
				activityService.createActivity(principal == null ? "anonymous" : principal.getName(), ActivityType.DONATE_TO_PROJECT, projectName);
				emailUtil.sendDonationNotification(trans.getEmail(), getFirstName(trans.getEmail()), amount, projectName);
			} else {
				attributes.addFlashAttribute("errorMessage", resp.getDescription());
			}
		} catch(Exception e) {
			log.error("Unable to verify transaction", e);
		}
		
		if(principal != null) {
			return REDIRECT + "/donate/projectDetails/"+ trans.getProject().getId();
		} else {
			return REDIRECT + "/na/lnk/" + trans.getProject().getLink();
		}
	}
	
	private String getFirstName(String email) {
		UserProfile user = userDao.findByEmail(email);
		if (user == null) {
			return "";
		}
		
		return user.getFirstName();
	}
	
}
