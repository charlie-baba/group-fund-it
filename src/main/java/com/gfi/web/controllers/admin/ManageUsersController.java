/**
 * 
 */
package com.gfi.web.controllers.admin;

import static com.gfi.web.util.AppConstants.IS_ADMIN;
import static com.gfi.web.util.AppConstants.REDIRECT;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.bis.gfi.entities.UserProfile;
import com.gfi.web.dao.GroupMembersDao;
import com.gfi.web.dao.UserDao;
import com.gfi.web.enums.ResponseCode;
import com.gfi.web.pojo.EmailPojo;
import com.gfi.web.pojo.RemoveFromGroupRequest;
import com.gfi.web.pojo.UserDetailsResponse;
import com.gfi.web.pojo.UserDto;
import com.gfi.web.pojo.UserRequest;
import com.gfi.web.service.GroupService;
import com.gfi.web.service.UserService;
import com.gfi.web.util.ImageUtil;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
@RequestMapping("/admin/users")
public class ManageUsersController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	GroupMembersDao groupMemberDao;
	
	@Autowired
	ImageUtil imageUtil;
	
	@Autowired
	GroupService groupService;
	

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String viewUsers(HttpServletRequest req, Model model) {
		Boolean isAdmin = (Boolean)req.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			return REDIRECT + "/accessdenied";
		}
		log.info("fetching users");

		model.addAttribute("users", userDao.findAllUsers());
		model.addAttribute("removeFromGroupForm", new RemoveFromGroupRequest());
		return "modules/admin/users/index";
	}
	
	@RequestMapping(value = "/activate/{id}", method = RequestMethod.GET)
	public String toggleStatus(@PathVariable("id") String userId, HttpServletRequest req,  Model model, Principal principal) {
		Boolean isAdmin = (Boolean)req.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			return REDIRECT + "/accessdenied";
		}

		UserProfile user = userDao.findById(Long.valueOf(userId));
		if (user == null) {
			return REDIRECT + "/error-404";
		}
		
		userService.setActivationStatus(user, !user.isActive(), principal.getName());
		model.addAttribute("users", userDao.findAllUsers());
		model.addAttribute("removeFromGroupForm", new RemoveFromGroupRequest());
		return "modules/admin/users/index";
	}

	@ResponseBody
	@RequestMapping(value = "/getUserDetails", method = RequestMethod.POST)
	public UserDetailsResponse getUserDetails(@RequestBody UserRequest req, HttpServletRequest httpReq) {
		log.info("fetching user details");
		UserDetailsResponse resp = new UserDetailsResponse();
		Boolean isAdmin = (Boolean)httpReq.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			resp.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return resp;
		}

		UserDto userDto = userDao.findUserDetails(req.getUserId());
		if (userDto == null) {
			resp.assignResponseCode(ResponseCode.NOT_FOUND);
			return resp;
		}
		
		userDto.setBase64Image(imageUtil.getImageStringFromUrl(userDto.getPictureUrl()));
		resp.setUserDto(userDto);
		resp.setGroups(groupMemberDao.findGroupsByMemberEmail(userDto.getEmail()));
		resp.assignResponseCode(ResponseCode.SUCCESS);
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getGroups", method = RequestMethod.POST)
	public UserDetailsResponse getGroups(@RequestBody EmailPojo req, HttpServletRequest httpReq) {
		log.info("fetching user groups");
		UserDetailsResponse resp = new UserDetailsResponse();
		Boolean isAdmin = (Boolean)httpReq.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			resp.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return resp;
		}
				
		resp.setGroups(groupMemberDao.findGroupsByMemberEmail(req.getEmail()));
		resp.assignResponseCode(ResponseCode.SUCCESS);
		return resp;
	}
	
	@RequestMapping(value = "/removeFromGroup", method = RequestMethod.POST)
	public String removeFromGroup(@ModelAttribute("removeFromGroupForm") @Valid RemoveFromGroupRequest req, BindingResult result,
			Model model, HttpServletRequest httpReq) {
		log.info("removing user from group");
		Boolean isAdmin = (Boolean)httpReq.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			return REDIRECT + "/accessdenied";
		}
		
		model.addAttribute("users", userDao.findAllUsers());

		if (result.hasErrors()) {
			return "modules/admin/users/index";
		}
		
		if (CollectionUtils.isEmpty(req.getGroupId())) {
			model.addAttribute("infoMessage", "No group was selected.");
			return "modules/admin/users/index";
		}
		
		int count = 0;
		for (Long groupId : req.getGroupId()) {
			if (groupService.removeGroupMember(groupId, req.getEmail())) {
				count++;
			}
		}
		model.addAttribute("successMessage", "You have successfully removed the user from "+ count + (count > 1 ? " groups" : " group"));
		return "modules/admin/users/index";
	}

}
