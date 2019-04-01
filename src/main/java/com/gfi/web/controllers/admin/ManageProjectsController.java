/**
 * 
 */
package com.gfi.web.controllers.admin;

import static com.gfi.web.util.AppConstants.IS_ADMIN;
import static com.gfi.web.util.AppConstants.REDIRECT;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gfi.web.dao.ProjectDao;
import com.gfi.web.enums.ResponseCode;
import com.gfi.web.pojo.ProjectDto;
import com.gfi.web.pojo.ProjectRequest;
import com.gfi.web.pojo.ProjectResponse;
import com.gfi.web.service.ProjectService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
@RequestMapping("/admin/projects")
public class ManageProjectsController {
	
	@Autowired
	ProjectDao projectDao;
	
	@Autowired
	ProjectService projectService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String viewGroups(HttpServletRequest req, Model model) {
		Boolean isAdmin = (Boolean)req.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			return REDIRECT + "/accessdenied";
		}
		log.info("fetching projects");
		
		model.addAttribute("projects", projectDao.findAllProjects());
		return "modules/admin/projects/index";
	}
	

	@ResponseBody
	@RequestMapping(value = "/getInfo", method = RequestMethod.POST)
	public ProjectResponse getGroupInfo(@RequestBody ProjectRequest req, HttpServletRequest httpReq) {
		log.info("fetching project details");
		ProjectResponse resp = new ProjectResponse();
		Boolean isAdmin = (Boolean)httpReq.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			resp.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return resp;
		}

		ProjectDto projectDto = projectDao.findProjectDetails(req.getProjectId());
		if (projectDto == null) {
			resp.assignResponseCode(ResponseCode.NOT_FOUND);
			return resp;
		}
		
		projectDto.setBase64Image(projectService.getFirstImage(projectDto.getImageUrls()));
		resp.setProjectDto(projectDto);
		resp.assignResponseCode(ResponseCode.SUCCESS);
		return resp;
	}
	
}
