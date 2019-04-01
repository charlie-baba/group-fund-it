/**
 * 
 */
package com.gfi.web.controllers;

import static com.gfi.web.util.AppConstants.IS_ADMIN;
import static com.gfi.web.util.AppConstants.REDIRECT;
import static com.gfi.web.util.AppConstants.df;
import static com.gfi.web.util.AppConstants.FIRST_NAME;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bis.gfi.entities.Group;
import com.bis.gfi.entities.Project;
import com.gfi.web.dao.BankDao;
import com.gfi.web.dao.GroupDao;
import com.gfi.web.dao.ProjectCategoryDao;
import com.gfi.web.dao.ProjectDao;
import com.gfi.web.dao.SettingsDao;
import com.gfi.web.dao.TransactionDao;
import com.gfi.web.enums.ResponseCode;
import com.gfi.web.pojo.GenericResponse;
import com.gfi.web.pojo.NewProjectRequest;
import com.gfi.web.pojo.PayoutAmountResponse;
import com.gfi.web.pojo.ProjectRequest;
import com.gfi.web.pojo.TransactionsResponse;
import com.gfi.web.service.GroupService;
import com.gfi.web.service.ProjectService;
import com.gfi.web.service.UserService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	GroupDao groupDao;

	@Autowired
	BankDao bankDao;

	@Autowired
	ProjectDao projectDao;

	@Autowired
	SettingsDao settingsDao;

	@Autowired
	ProjectCategoryDao projectCategoryDao;

	@Autowired
	GroupService groupService;

	@Autowired
	ProjectService projectService;

	@Autowired
	UserService userService;
	
	@Autowired
	TransactionDao transactionDao;
	

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String viewMyProjects(HttpServletRequest req, Model model, Principal principal) {
		model.addAttribute("rootURL", req.getRequestURL().toString().replace("/project/index", ""));
		model.addAttribute("projectDetails", projectService.getAllByAdmin(principal.getName()));
		model.addAttribute("publishForm", new ProjectRequest());
		return "modules/project/index";
	}

	@RequestMapping(value = "/index/{id}", method = RequestMethod.GET)
	public String viewProjects(@PathVariable("id") String groupId, HttpServletRequest req, Model model,
			Principal principal) {
		Group group = groupDao.findById(Long.valueOf(groupId));
		if (group == null) {
			return REDIRECT + "/error-404";
		}

		if (!groupService.isAdmin(group, principal.getName())) {
			return REDIRECT + "/accessdenied";
		}

		String trail = "/project/index/" + groupId;
		model.addAttribute("rootURL", req.getRequestURL().toString().replace(trail, ""));
		model.addAttribute("projectDetails", projectService.getProjectDetailsByGroup(group.getId()));
		model.addAttribute("groupName", group.getName());
		model.addAttribute("groupId", group.getId());
		model.addAttribute("publishForm", new ProjectRequest());
		return "modules/project/index";
	}
	
	@RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
	public String createProjectPage(@PathVariable("id") String groupId, Model model, Principal principal) {
		Group group = groupDao.findWithBanksById(Long.valueOf(groupId));
		if (group == null) {
			return REDIRECT + "/error-404";
		}

		if (!groupService.isAdmin(group, principal.getName())) {
			return REDIRECT + "/accessdenied";
		}

		populateCreatePage(model, group.getId(), "create", group, null);
		return "modules/project/create";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createGroup(@ModelAttribute("projectForm") @Valid NewProjectRequest req, BindingResult result,
			Model model, Principal principal, RedirectAttributes attributes) {
		log.info("creating project");

		Group group = groupDao.findWithBanksById(Long.valueOf(req.getGroupId()));
		if (group == null) {
			return REDIRECT + "/error-404";
		}

		if (result.hasErrors()) {
			populateCreatePage(model, group.getId(), "create", group, null);
			return "modules/project/create";
		}
		
		if (!CollectionUtils.isEmpty(req.getPics()) && req.getPics().size() > 5) {
			populateCreatePage(model, group.getId(), "create", group, null);
			model.addAttribute("errorMessage", "You cannot upload more than 5 images.");
			return "modules/project/create";
		}

		try {
			projectService.createProject(req, group, principal.getName());
			attributes.addFlashAttribute("successMessage", "New project was created successfully");
			attributes.addFlashAttribute("firstProject", projectService.isFirstProject(principal.getName()));
		} catch (Exception e) {
			log.error("Unable to create project: ", e);
			populateCreatePage(model, group.getId(), "create", group, null);
			model.addAttribute("errorMessage", "Something went wrong while saving the project. Please, try again or contact support.");
			return "modules/project/create";
		}
		return REDIRECT + "/project/index/" + req.getGroupId();
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editProjectPage(@PathVariable("id") String projectId, Model model, Principal principal) {
		Project project = projectDao.findProjectEagerlyById(Long.valueOf(projectId));
		if (project == null) {
			return REDIRECT + "/error-404";
		}

		Group group = project.getGroup();
		if (!principal.getName().equals(project.getCreatedBy())) {
			return REDIRECT + "/accessdenied";
		}

		populateCreatePage(model, group.getId(), "edit", null, project);
		return "modules/project/create";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editGroup(@ModelAttribute("projectForm") @Valid NewProjectRequest req, BindingResult result,
			Model model, Principal principal, RedirectAttributes attributes) {
		log.info("editing project");

		Project project = projectDao.findProjectEagerlyById(Long.valueOf(req.getProjectId()));
		if (project == null) {
			return REDIRECT + "/error-404";
		}

		Group group = project.getGroup();
		if (!principal.getName().equals(project.getCreatedBy())) {
			return REDIRECT + "/accessdenied";
		}

		if (result.hasErrors()) {
			populateCreatePage(model, group.getId(), "edit", null, project);
			return "modules/project/create";
		}

		if (!CollectionUtils.isEmpty(req.getPics()) && req.getPics().size() > 5) {
			populateCreatePage(model, group.getId(), "edit", null, project);
			model.addAttribute("errorMessage", "You cannot upload more than 5 images.");
			return "modules/project/create";
		}
		
		try {
			projectService.editProject(req, project, group, principal.getName());
			attributes.addFlashAttribute("successMessage", project.getName() + " was edited successfully");
		} catch (Exception e) {
			log.error("Unable to edit project: ", e);
			populateCreatePage(model, group.getId(), "edit", null, project);
			model.addAttribute("errorMessage", "Something went wrong while saving the project. Please, try again or contact support.");
			return "modules/project/create";
		}
		return REDIRECT + "/project/index/" + req.getGroupId();
	}

	@RequestMapping(value = "/dashboard/{id}", method = RequestMethod.GET)
	public String dashboardPage(@PathVariable("id") String projectId, Model model, Principal principal) {
		Project project = projectDao.findProjectEagerlyById(Long.valueOf(projectId));
		if (project == null) {
			return REDIRECT + "/error-404";
		}
		
		Group group = project.getGroup();
		if (!groupService.isAdmin(group, principal.getName())) {
			return REDIRECT + "/accessdenied";
		}
		
		model.addAttribute("project", project);
		model.addAttribute("dashboard", projectService.getProjectDashboard(project));
		return "modules/project/dashboard";
	}
	
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	public String publishProject(@ModelAttribute("publishForm") @Valid ProjectRequest req, Model model,
			Principal principal, RedirectAttributes attributes) {
		log.info("pubhlishing project");

		Project project = projectDao.findProjectEagerlyById(Long.valueOf(req.getProjectId()));
		if (project == null) {
			return REDIRECT + "/error-404";
		}

		Group group = project.getGroup();
		if (!principal.getName().equals(project.getCreatedBy())) {
			return REDIRECT + "/accessdenied";
		}

		try {
			projectService.publishProject(project, principal.getName());
			attributes.addFlashAttribute("successMessage", project.getName() + " was published successfully.");
		} catch (Exception e) {
			log.error("Unable to publish project: ", e);
			attributes.addFlashAttribute("errorMessage", "Something went wrong while publishing the project. Please, try again or contact support.");
		}
		return REDIRECT + "/project/index/" + group.getId();
	}
	
	@RequestMapping(value = "/deactivate", method = RequestMethod.POST)
	public String deactivateProject(@ModelAttribute("publishForm") @Valid ProjectRequest req, Model model,
			Principal principal, RedirectAttributes attributes) {
		log.info("deactivating project");

		Project project = projectDao.findProjectEagerlyById(Long.valueOf(req.getProjectId()));
		if (project == null) {
			return REDIRECT + "/error-404";
		}

		Group group = project.getGroup();
		if (!principal.getName().equals(project.getCreatedBy())) {
			return REDIRECT + "/accessdenied";
		}

		try {
			projectService.deactivateProject(project, principal.getName());
			attributes.addFlashAttribute("successMessage", project.getName() + " was deactivated successfully.");
		} catch (Exception e) {
			log.error("Unable to publish project: ", e);
			attributes.addFlashAttribute("errorMessage", "Something went wrong while deactivating the project. Please, try again or contact support.");
		}
		return REDIRECT + "/project/index/" + group.getId();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPayoutAmount", method = RequestMethod.POST)
	public GenericResponse getPayoutAmount(@RequestBody ProjectRequest req, Principal principal) {
		log.info("getting projet transactions");
		PayoutAmountResponse resp = new PayoutAmountResponse();

		Project project = projectDao.findProjectEagerlyById(req.getProjectId());
		if (project == null) {
			resp.assignResponseCode(ResponseCode.NOT_FOUND);
			return resp;
		}

		if (!principal.getName().equals(project.getCreatedBy())) {
			resp.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return resp;
		}

		try {
			Double amount = transactionDao.totalAmtContributedToProject(project.getId());
			if (amount == null || amount.equals(0D)) {
				resp.assignResponseCode(ResponseCode.INSUFFICIENT_FUND);
			} else {
				resp.setAmount(df.format(amount == null ? 0 : amount));
				resp.assignResponseCode(ResponseCode.SUCCESS);
			}
		} catch (Exception ex) {
			log.error("", ex);
			resp.assignResponseCode(ResponseCode.SYSTEM_ERROR);
		}
		return resp;
	}

	@ResponseBody
	@RequestMapping(value = "/payout", method = RequestMethod.POST)
	public GenericResponse payoutProject(@RequestBody ProjectRequest req, Principal principal, HttpServletRequest httpReq) {
		log.info("payout project " + req.getProjectId());
		GenericResponse resp = new GenericResponse();
		
		Project project = projectDao.findById(req.getProjectId());
		if (project == null) {
			resp.assignResponseCode(ResponseCode.NOT_FOUND);
			return resp;
		}
		
		if (!principal.getName().equals(project.getCreatedBy())) {
			resp.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return resp;
		}
		
		if (project.isCompleted()) {
			resp.assignResponseCode(ResponseCode.PAID_OUT);
			return resp;
		}
		
		try {
			Double amount = transactionDao.totalAmtContributedToProject(project.getId());
			if (amount == null || amount.equals(0D)) {
				resp.assignResponseCode(ResponseCode.INSUFFICIENT_FUND);
				return resp;
			}
			
			String firstName = (String) httpReq.getSession().getAttribute(FIRST_NAME);
			boolean success = projectService.payoutProject(project, firstName, amount, principal.getName());
			resp.assignResponseCode(success ? ResponseCode.SUCCESS : ResponseCode.TRANSACTION_FAILED);
		} catch (Exception ex) {
			log.error("", ex);
			resp.assignResponseCode(ResponseCode.SYSTEM_ERROR);
		}
		return resp;
	}

	@ResponseBody
	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public GenericResponse projectTransactions(@RequestBody ProjectRequest req, Principal principal, HttpServletRequest httpReq) {
		log.info("getting projet transactions");
		TransactionsResponse resp = new TransactionsResponse();

		Project project = projectDao.findProjectEagerlyById(req.getProjectId());
		if (project == null) {
			resp.assignResponseCode(ResponseCode.NOT_FOUND);
			return resp;
		}

		Boolean isAdmin = (Boolean)httpReq.getSession().getAttribute(IS_ADMIN);
		if (!groupService.isAdmin(project.getGroup(), principal.getName()) && !isAdmin) {
			resp.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return resp;
		}

		try {
			resp.setDonations(transactionDao.findDonationsByProject(project.getId()));
			resp.assignResponseCode(ResponseCode.SUCCESS);
		} catch (Exception ex) {
			log.error("", ex);
			resp.assignResponseCode(ResponseCode.SYSTEM_ERROR);
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value = "/logo/{id}", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getProjectLogo(@PathVariable("id") String projectId, Principal principal) throws IOException {
		Project project = projectDao.findById(Long.valueOf(projectId));
		if (project == null) {
			return null;
		}
		
		return projectService.getFirstImage(project);
	}

	private void populateCreatePage(Model model, Long groupId, String action, Group group, Project project) {
		model.addAttribute("banks", bankDao.findAll());
		model.addAttribute("categories", projectCategoryDao.findAll());
		model.addAttribute("action", action);
		model.addAttribute("groupId", groupId);
		if (group != null) {
			model.addAttribute("projectForm", NewProjectRequest.fromGroup(group));
		} else if (project != null) {
			model.addAttribute("projectForm", NewProjectRequest.fromProject(project));
		}
	}

}
