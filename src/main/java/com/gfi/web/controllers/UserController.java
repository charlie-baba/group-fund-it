/**
 * 
 */
package com.gfi.web.controllers;

import static com.gfi.web.util.AppConstants.FULL_NAME;
import static com.gfi.web.util.AppConstants.PROFILE_PICTURE;
import static com.gfi.web.util.AppConstants.REDIRECT;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.bis.gfi.entities.UserProfile;
import com.bis.gfi.enums.Gender;
import com.gfi.web.dao.UserDao;
import com.gfi.web.enums.ResponseCode;
import com.gfi.web.pojo.ChangePasswordRequest;
import com.gfi.web.pojo.GenericResponse;
import com.gfi.web.pojo.PictureRequest;
import com.gfi.web.pojo.ProfileRequest;
import com.gfi.web.service.UserService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserService userService;

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profilePage(Model model, Principal principal) {
		UserProfile profile = userDao.findByEmail(principal.getName());
		if (profile == null) {
			return REDIRECT + "/error-404";
		}

		populateProfilePage(model, profile);
		return "modules/user/profile";
	}
	
	@RequestMapping(value = "/editProfile", method = RequestMethod.POST)
	public String editProfile(@ModelAttribute("profile") @Valid ProfileRequest req, BindingResult result, HttpServletRequest request, 
			Model model, Principal principal) {		
		UserProfile profile = userDao.findByEmail(principal.getName());
		if (profile == null) {
			return REDIRECT + "/error-404";
		}
		
		if (result.hasErrors()) {
			populateProfilePage(model, profile);
			return "modules/user/profile";
		}
				
		try {
			profile = userService.editProfile(profile, req, principal.getName());
			request.getSession().setAttribute(FULL_NAME, profile.getFirstAndLastName());
			model.addAttribute("successMessage", "Your profile was updated successfully");
		} catch(Exception e) {
			log.error("Unable to edit profile", e);
			model.addAttribute("errorMessage", "Something went wrong while udpating your profile. Please contact support.");
		}

		populateProfilePage(model, profile);
		return "modules/user/profile";
	}
	
	@RequestMapping(value = "/changePicture", method = RequestMethod.POST)
	public String changePicture(@ModelAttribute("pictureForm") @Valid PictureRequest req, HttpServletRequest request, 
			Model model, Principal principal) {
		log.info("changing profile picture");
		
		UserProfile profile = userDao.findByEmail(principal.getName());
		if (profile == null) {
			return REDIRECT + "/error-404";
		}
		
		userService.changeProfilePicture(profile, req.getProfilePicture());
		try {
			request.getSession().setAttribute(PROFILE_PICTURE, req.getProfilePicture().getBytes());
		} catch (IOException e) {
			log.error("Unable to save image to session", e);
		}
		populateProfilePage(model, profile);
		return "modules/user/profile";
	}
	
	@ResponseBody
	@RequestMapping(value = "/profilepic", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getProfilePicture(HttpServletRequest request, Principal principal) throws IOException {
		byte[] pic = (byte[]) request.getSession().getAttribute(PROFILE_PICTURE);
		if(pic != null){
			return pic;
		}
		
		UserProfile profile = userDao.findByEmail(principal.getName());		
		if(profile == null || StringUtils.isBlank(profile.getPictureUrl())) {
			return null;	
		}
		
		File image = new File(profile.getPictureUrl());
		pic = FileUtils.readFileToByteArray(image);
		request.getSession().setAttribute(PROFILE_PICTURE, pic);
		
		return pic;
	}
	
	@RequestMapping(value = "/changepassword", method = RequestMethod.GET)
	public String changePasswordPage(Model model) {
		model.addAttribute("changePswdForm", new ChangePasswordRequest());
		return "modules/user/changePassword";
	}
	
	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public String changePassword(@ModelAttribute("changePswdForm") @Valid ChangePasswordRequest req, BindingResult result, 
			Model model, Principal principal) {
		log.info("changing password");
		UserProfile profile = userDao.findByEmail(principal.getName());		
		if(profile == null) {
			return REDIRECT + "/error-500";	
		}
		
		if (result.hasErrors()) {
			return "modules/user/changePassword";
		}
		
		if (!req.getNewPassword().equals(req.getConfirmPassword())) {
			model.addAttribute("errorMessage", "Your new password does not match with the confirm password.");
			return "modules/user/changePassword";
		}
		
		GenericResponse resp = userService.changePassword(profile, req.getCurrentPassword(), req.getNewPassword());
		if (resp.getCode().equals(ResponseCode.SUCCESS.getCode())) {
			model.addAttribute("successMessage", "Your password was changed successfully.");
		} else {
			model.addAttribute("errorMessage", resp.getDescription());			
		}
		return "modules/user/changePassword";
	}
	
	
	private void populateProfilePage(Model model, UserProfile profile) {
		model.addAttribute("gender", Gender.values());
		model.addAttribute("profile", ProfileRequest.fromUserProfile(profile));
		model.addAttribute("pictureForm", new PictureRequest());
	}
	
}
