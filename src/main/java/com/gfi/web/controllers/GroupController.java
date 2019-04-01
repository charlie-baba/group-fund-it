/**
 * 
 */
package com.gfi.web.controllers;

import static com.gfi.web.util.AppConstants.REDIRECT;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bis.gfi.entities.Group;
import com.bis.gfi.enums.GroupCategory;
import com.gfi.web.dao.BankDao;
import com.gfi.web.dao.GroupDao;
import com.gfi.web.dao.GroupMembersDao;
import com.gfi.web.enums.ResponseCode;
import com.gfi.web.pojo.AddMemberRequest;
import com.gfi.web.pojo.GenericResponse;
import com.gfi.web.pojo.GroupRequest;
import com.gfi.web.pojo.NewGroupRequest;
import com.gfi.web.service.GroupService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
@RequestMapping("/group")
public class GroupController {
	
	@Autowired
	BankDao bankDao;
	
	@Autowired
	GroupDao groupDao;
	
	@Autowired
	GroupMembersDao groupMembersDao;
	
	@Autowired
	GroupService groupService;
	

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String viewGroups(Model model, Principal principal) {
		model.addAttribute("groupDetails", groupService.getAllByAdmin(principal.getName()));
		return "modules/group/index";
	}
	
	@RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
	public String viewGroupDetails(@PathVariable("id") String groupId, Model model, Principal principal) {
		Group group = groupDao.findById(Long.valueOf(groupId));
		if (group == null) {
			return REDIRECT + "/error-404";
		}

		if (!groupService.isGroupMember(group.getId(), principal.getName())) {
			return REDIRECT + "/accessdenied";
		}

		model.addAttribute("group", group);
		model.addAttribute("members", groupMembersDao.findMembersByGroup(group.getId()));
		return "modules/group/details";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createGroupPage(Model model) {
		populateCreatePage(model, "create");
		model.addAttribute("action", "create");
		model.addAttribute("groupForm", new NewGroupRequest());
		return "modules/group/create";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createGroup(@ModelAttribute("groupForm") @Valid NewGroupRequest groupReq, BindingResult result, 
			Model model, Principal principal) {
		log.info("creating group");

		model.addAttribute("action", "create");
		if (result.hasErrors()) {
			populateCreatePage(model, "create");
			return "modules/group/create";
		}
		
		if (groupService.groupNameExists(principal.getName(), groupReq.getName())) {
			populateCreatePage(model, "create");
			model.addAttribute("errorMessage", "You have already created a group with this name.");
			return "modules/group/create";
		}
		
		try {
			groupService.createGroup(groupReq, principal.getName());
		} catch (Exception e) {
			log.error("Unable to create group: ", e);
			populateCreatePage(model, "create");
			model.addAttribute("errorMessage", "Something went wrong while saving the group. Please, try again or contact support.");
			return "modules/group/create";
		}
		return REDIRECT + "/group/index";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editGroupPage(@PathVariable("id") String groupId, Model model, Principal principal) {
		Group group = groupDao.findWithBanksById(Long.valueOf(groupId));
		if (group == null) {
			return REDIRECT + "/error-404";
		}
		
		if (!groupService.isAdmin(group, principal.getName())) {
			return REDIRECT + "/accessdenied";
		}
		
		populateCreatePage(model, "edit");
		model.addAttribute("action", "edit");
		model.addAttribute("groupForm", NewGroupRequest.fromGroup(group));
		return "modules/group/create";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editGroup(@ModelAttribute("groupForm") @Valid NewGroupRequest groupReq, BindingResult result, Model model, Principal principal) {
		log.info("editing group");

		if (result.hasErrors()) {
			populateCreatePage(model, "edit");
			return "modules/group/create";
		}
		
		Group group = groupDao.findWithBanksById(groupReq.getGroupId());
		if (group == null) {
			return REDIRECT + "/error-404";
		}
		
		if (!groupService.isAdmin(group, principal.getName())) {
			return REDIRECT + "/accessdenied";
		}
		
		if (groupService.groupNameExists(principal.getName(), groupReq.getName(), group.getId())) {
			populateCreatePage(model, "edit");
			model.addAttribute("errorMessage", "You have already created a group with this name.");
			return "modules/group/create";
		}
		
		populateCreatePage(model, "edit");
		try {
			groupService.editGroup(groupReq, group, principal.getName());
		} catch (Exception e) {
			log.error("Unable to edit group: ", e);
			model.addAttribute("errorMessage", "Something went wrong while saving the group. Please, try again or contact support.");
			return "modules/group/create";
		}
		model.addAttribute("groupForm", NewGroupRequest.fromGroup(group));
		model.addAttribute("successMessage", "The group was updated successfully!");
		return "modules/group/create";
	}
	
	@RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
	public String groupMembersPage(@PathVariable("id") String groupId, Model model, Principal principal) {
		Group group = groupDao.findById(Long.valueOf(groupId));
		if (group == null) {
			return REDIRECT + "/error-404";
		}
		
		if (!groupService.isAdmin(group, principal.getName())) {
			return REDIRECT + "/accessdenied";
		}
		
		model.addAttribute("groupId", group.getId());
		model.addAttribute("groupName", group.getName());
		model.addAttribute("members", groupMembersDao.findMemberDetailsByGroup(group.getId()));
		return "modules/group/members";
	}
	
	@RequestMapping(value = "/addmember", method = RequestMethod.POST)
	public String addMember(@ModelAttribute("memberForm") @Valid AddMemberRequest req, BindingResult result, 
			RedirectAttributes attributes, Principal principal) {
		log.info("adding members...");
		
		if (result.hasErrors()) {
			attributes.addFlashAttribute("errorMessage", "Please enter a valid email and try again.");
			return REDIRECT + "/group/members" + req.getGroupId();
		}
		
		Group group = groupDao.findById(req.getGroupId());
		if (group == null) {
			return REDIRECT + "/error-404";
		}
		
		if (!groupService.isAdmin(group, principal.getName())) {
			return REDIRECT + "/accessdenied";
		}
		
		try {
			groupService.addMemberToGroup(group, req.getEmail());
			attributes.addFlashAttribute("successMessage", "An email request was sent successfully!");
		} catch(Exception ex) {
			log.error("Unable to add member to group", ex);
			attributes.addFlashAttribute("errorMessage", "We were unable to send the membership request. Please try again or contact support.");
		}
		return REDIRECT + "/group/members/" + req.getGroupId();
	}
	
	@RequestMapping(value = "/removemember", method = RequestMethod.POST)
	public String removeMember(@ModelAttribute("memberForm") @Valid AddMemberRequest req, BindingResult result, 
			RedirectAttributes attributes, Principal principal) {
		log.info("removing group member...");
		
		if (result.hasErrors()) {
			attributes.addFlashAttribute("errorMessage", "Please enter a valid email and try again.");
			return REDIRECT + "/group/members/" + req.getGroupId();
		}
		
		Group group = groupDao.findById(req.getGroupId());
		if (group == null) {
			return REDIRECT + "/error-404";
		}
		
		if (!groupService.isAdmin(group, principal.getName())) {
			return REDIRECT + "/accessdenied";
		}
		
		if (req.getEmail().equals(principal.getName())) {
			attributes.addFlashAttribute("errorMessage", "Sorry, you cannot remove yourself from the group.");
			return REDIRECT + "/group/members/" + req.getGroupId();
		}
		
		try {
			groupService.removeGroupMember(group.getId(), req.getEmail());
			attributes.addFlashAttribute("successMessage", "The user was removed successfully!");
		} catch(Exception ex) {
			log.error("Unable to remove member from group", ex);
			attributes.addFlashAttribute("errorMessage", "We were unable to remove the group member. Please try again or contact support.");
		}
		return REDIRECT + "/group/members/" + req.getGroupId();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deactivate", method = RequestMethod.POST)
	public GenericResponse deactivateGroup(@RequestBody GroupRequest req, Principal principal) {
		log.info("deactivating group");
		GenericResponse response = new GenericResponse();
		
		Group group = groupDao.findById(req.getGroupId());
		if (group == null) {
			response.assignResponseCode(ResponseCode.NOT_FOUND);
			return response;
		}
		
		if (!groupService.isAdmin(group, principal.getName())) {
			response.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return response;
		}
		
		try {
			groupService.setActivationStatus(group, false, principal.getName());
			response.assignResponseCode(ResponseCode.SUCCESS);
		} catch (Exception ex) {
			response.assignResponseCode(ResponseCode.SYSTEM_ERROR);
		}
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/activate", method = RequestMethod.POST)
	public GenericResponse activateGroup(@RequestBody GroupRequest req, Principal principal) {
		log.info("activating group");
		GenericResponse response = new GenericResponse();
		
		Group group = groupDao.findById(req.getGroupId());
		if (group == null) {
			response.assignResponseCode(ResponseCode.NOT_FOUND);
			return response;
		}
		
		if (!groupService.isAdmin(group, principal.getName())) {
			response.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return response;
		}
		
		try {
			groupService.setActivationStatus(group, true, principal.getName());
			response.assignResponseCode(ResponseCode.SUCCESS);
		} catch (Exception ex) {
			response.assignResponseCode(ResponseCode.SYSTEM_ERROR);
		}
		return response;
	}
	
	private void populateCreatePage(Model model, String action) {
		model.addAttribute("banks", bankDao.findAll());
		model.addAttribute("category", GroupCategory.values());
		model.addAttribute("action", action);
	}
	
}
