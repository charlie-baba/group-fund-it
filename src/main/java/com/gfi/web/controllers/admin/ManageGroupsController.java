/**
 * 
 */
package com.gfi.web.controllers.admin;

import static com.gfi.web.util.AppConstants.IS_ADMIN;
import static com.gfi.web.util.AppConstants.REDIRECT;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bis.gfi.entities.Group;
import com.gfi.web.dao.GroupDao;
import com.gfi.web.enums.ResponseCode;
import com.gfi.web.pojo.GroupDto;
import com.gfi.web.pojo.GroupRequest;
import com.gfi.web.pojo.GroupResponse;
import com.gfi.web.service.GroupService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
@RequestMapping("/admin/groups")
public class ManageGroupsController {
	
	@Autowired
	GroupDao groupDao;
	
	@Autowired
	GroupService groupService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String viewGroups(HttpServletRequest req, Model model) {
		Boolean isAdmin = (Boolean)req.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			return REDIRECT + "/accessdenied";
		}
		log.info("fetching groups");
		model.addAttribute("groups", groupDao.findAllGroups());
		return "modules/admin/groups/index";
	}
	
	@RequestMapping(value = "/activate/{id}", method = RequestMethod.GET)
	public String toggleStatus(@PathVariable("id") String groupId, HttpServletRequest req,  Model model, Principal principal) {
		Boolean isAdmin = (Boolean)req.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			return REDIRECT + "/accessdenied";
		}

		Group group = groupDao.findById(Long.valueOf(groupId));
		if (group == null) {
			return REDIRECT + "/error-404";
		}
		
		groupService.setActivationStatus(group, !group.isActive(), principal.getName());
		model.addAttribute("groups", groupDao.findAllGroups());
		return "modules/admin/groups/index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getInfo", method = RequestMethod.POST)
	public GroupResponse getGroupInfo(@RequestBody GroupRequest req, HttpServletRequest httpReq) {
		log.info("fetching group details");
		GroupResponse resp = new GroupResponse();
		Boolean isAdmin = (Boolean)httpReq.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			resp.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return resp;
		}

		GroupDto groupDto = groupDao.findGroupDetails(req.getGroupId());
		if (groupDto == null) {
			resp.assignResponseCode(ResponseCode.NOT_FOUND);
			return resp;
		}
		
		resp.setGroupDto(groupDto);
		resp.assignResponseCode(ResponseCode.SUCCESS);
		return resp;
	}
	
}
