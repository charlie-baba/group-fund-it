/**
 * 
 */
package com.gfi.web.controllers;

import static com.gfi.web.util.AppConstants.REDIRECT;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bis.gfi.entities.SocialNetworkDetail;
import com.bis.gfi.entities.UserProfile;
import com.gfi.config.security.util.LoginUtil;
import com.gfi.web.dao.SocialNetworkDetailDao;
import com.gfi.web.pojo.EmailPojo;
import com.gfi.web.pojo.OAuthRequest;
import com.gfi.web.service.UserService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
public class AuthController {
	
	@Autowired
	UserService userService;

	@Autowired
	SocialNetworkDetailDao socialNtwkDao;
	
	
	@RequestMapping(value = {"", "/index", "/", "/login"}, method = RequestMethod.GET)
	public String showLogin(Model model, @RequestParam(value = "loginError", defaultValue = "false", required = false) 
		boolean showLoginError, Principal principal) {
        if (principal != null) {
            Authentication authentication = (Authentication) principal;
            if (authentication.isAuthenticated()) {
                return REDIRECT + LoginUtil.getTargetUrlAfterSuccessfulLogin(authentication);
            }
        }
        if (showLoginError) {
            model.addAttribute("errorMessage", "Invalid username/password");
        }
		model.addAttribute("oauthForm", new OAuthRequest());
		return "auth/login";
	}
	
	@RequestMapping(value = {"/login/oauth"}, method = RequestMethod.POST)
	public String openAuthLogin(@ModelAttribute("oauthForm") @Valid OAuthRequest req, BindingResult result, Model model, 
			HttpServletRequest request, Principal principal) {
        if (principal != null) {
            Authentication authentication = (Authentication) principal;
            if (authentication.isAuthenticated()) {
                return REDIRECT + LoginUtil.getTargetUrlAfterSuccessfulLogin(authentication);
            }
        }
        
        if (result.hasErrors()) {
			model.addAttribute("errorMessage", "An error occured. Please try again or contact support.");
            return "auth/login";
        }
        
        UserProfile user;
        SocialNetworkDetail socialNtwkdetail = socialNtwkDao.findByUserIdandSocialNtwkType(req.getUid(), req.getSocialNetworkType());
		if (socialNtwkdetail != null) {
			user = socialNtwkdetail.getUser();
			String url = userService.authenticateUserAndSetSession(user, user.getPassword(), request, null);
			return REDIRECT + url;
		}

        user = userService.findUser(req.getEmail());
		if (user != null) {
			model.addAttribute("errorMessage", "Email already exist.");
            return "auth/login";
		}
        
		model.addAttribute("errorMessage", "Record not found! You have not signed up yet.");
        return "auth/login";
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.POST)
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return REDIRECT + "/login?logout";
	}
	
	@RequestMapping(value = "/accessdenied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		return "auth/accessDenied";
	}
	
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
	public String forgotPasswordPage(ModelMap model) {
		model.addAttribute("pswdResetForm", new EmailPojo());
		return "auth/forgotPassword";
	}
	
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public String forgotPassword(@ModelAttribute("pswdResetForm") @Valid EmailPojo emailReq, BindingResult result, ModelMap model) {
		log.info("reseting password");
		if (result.hasErrors()) {
			return "auth/forgotPassword";
		}
		
		UserProfile user = userService.findUser(emailReq.getEmail());
		if (user == null) {
			model.addAttribute("infoMessage", "If your email exists in our records, you will recieve a new password in a few minutes.");
			return "auth/forgotPassword";
		}
		
		if (!user.isActive()) {
			model.addAttribute("errorMessage", "Your account has been deactivate. Please contact support for reactivation.");
			return "auth/forgotPassword";
		}
			
		boolean success = userService.resetPassword(user);
		if (success) {
			model.addAttribute("infoMessage", "If your email exists in our records, you will recieve a new password in a few minutes.");
		} else {
			model.addAttribute("errorMessage", "We could not reset your password. Please try again or contact support.");
		}
		return "auth/forgotPassword";
	}
	
}
