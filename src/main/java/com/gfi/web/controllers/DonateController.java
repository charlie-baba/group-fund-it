/**
 * 
 */
package com.gfi.web.controllers;

import static com.gfi.web.util.AppConstants.REDIRECT;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bis.gfi.entities.Project;
import com.gfi.web.dao.ProjectDao;
import com.gfi.web.dao.UserDao;
import com.gfi.web.service.GroupService;
import com.gfi.web.service.ProjectService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
@RequestMapping("/donate")
public class DonateController {
	
	@Autowired
	ProjectDao projectDao;
	
	@Autowired
	ProjectService projectService;

	@Autowired
	GroupService groupService;

	@Autowired
	UserDao userDao;
	
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public String projectsPage(Model model, Principal principal) {
		log.info("getting projects");
		
		model.addAttribute("isAthenticated", principal != null);
		model.addAttribute("myprojects", projectDao.findAllByEmail(principal.getName()));
		model.addAttribute("publicProjects", projectDao.findAllPublicProjects());
		return "modules/donate/projects";
	}
	
	@RequestMapping(value = "/projectDetails/{id}", method = RequestMethod.GET)
	public String projectsDetailsPage(@PathVariable("id") String projectId, Model model, Principal principal) {
		log.info("getting project details");
		Project project = projectDao.findProjectEagerlyById(Long.valueOf(projectId));
		if (project == null) {
			return REDIRECT + "/error-404";
		}
		
		if (project.isPrivateProject() && !groupService.isGroupMember(project.getGroup().getId(), principal.getName())) {
			return REDIRECT + "/accessdenied";
		}
		
		model.addAttribute("project", project);
		model.addAttribute("projImages", projectService.getProjectImages(project));
		model.addAttribute("percentage", projectService.getProjectDonatedPercentage(project));
		model.addAttribute("creator", userDao.findFirstAndLastName(project.getCreatedBy()));
		model.addAttribute("returnUrl", "/donate/projects");
		return "modules/donate/donate";
	}
	
}