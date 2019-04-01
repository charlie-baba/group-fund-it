/**
 * 
 */
package com.gfi.web.controllers;

import static com.gfi.web.util.AppConstants.REDIRECT;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bis.gfi.entities.SocialNetworkDetail;
import com.bis.gfi.entities.UserProfile;
import com.bis.gfi.enums.Gender;
import com.bis.gfi.enums.Roles;
import com.gfi.config.security.util.LoginUtil;
import com.gfi.web.dao.SocialNetworkDetailDao;
import com.gfi.web.pojo.OAuthRequest;
import com.gfi.web.pojo.SignUpRequest;
import com.gfi.web.service.UserService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
public class SignUpController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	SocialNetworkDetailDao socialNtwkDao;
	
	@Autowired
    protected DaoAuthenticationProvider authenticationManager;
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupPage(Model model, Principal principal) {
		if (principal != null) {
            Authentication authentication = (Authentication) principal;
            if (authentication.isAuthenticated()) {
                return REDIRECT + LoginUtil.getTargetUrlAfterSuccessfulLogin(authentication);
            }
        }
		model.addAttribute("signupForm", new SignUpRequest());
		model.addAttribute("oauthForm", new OAuthRequest());
		return "auth/signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String performSignup(@ModelAttribute("signupForm") @Valid SignUpRequest signupReq, BindingResult result, Model model, 
			HttpServletRequest request) {
		log.info("signup request");	
		model.addAttribute("oauthForm", new OAuthRequest());
		
		if (result.hasErrors()) {
            return "auth/signup";
        }
		
		if (userService.emailExists(signupReq.getEmail())) {
			model.addAttribute("errorMessage", "A user with this email already exist.");
            return "auth/signup";
		}
		
		if (!signupReq.getPassword().equals(signupReq.getConfirmPassword())) {
			model.addAttribute("errorMessage", "Your password does not match your confirm password");
            return "auth/signup";
		}
		
		try {
			UserProfile profile = userService.registerUser(signupReq);
			authenticateUserAndSetSession(signupReq, profile, request);
		} catch(Exception e) {
			log.error("Unable to signup user ", e);
			model.addAttribute("gender", Gender.values());
			model.addAttribute("errorMessage", "We encountered an error while creating your account. Please contact support.");
            return "auth/signup";
		}
		
		return REDIRECT + "/user/dashboard";
	}
	
	@RequestMapping(value = "/signup/oauth", method = RequestMethod.POST)
	public String performSocialNetworkSignup(@ModelAttribute("oauthForm") @Valid OAuthRequest req, BindingResult result, Model model, 
			HttpServletRequest request) {
		model.addAttribute("signupForm", new SignUpRequest());

		if (result.hasErrors()) {
			model.addAttribute("gender", Gender.values());
			model.addAttribute("errorMessage", "An error occured. Please try again or contact support.");
            return "auth/signup";
        }
		
		UserProfile user = userService.findUser(req.getEmail());
		if (user != null) {
			model.addAttribute("gender", Gender.values());
			model.addAttribute("errorMessage", "Email already exist.");
            return "auth/signup";
		}
		
		SocialNetworkDetail socialNtwkdetail = socialNtwkDao.findByUserIdandSocialNtwkType(req.getUid(), req.getSocialNetworkType());
		if (socialNtwkdetail != null) {
			user = socialNtwkdetail.getUser();
			userService.authenticateUserAndSetSession(user, user.getPassword(), request, null);
			return REDIRECT + "/user/dashboard";
		}
		
		try {
			user = userService.registerUser(req);
			userService.authenticateUserAndSetSession(user, user.getPassword(), request, Roles.USER);
		} catch(Exception e) {
			log.error("Unable to signup user ", e);
			model.addAttribute("gender", Gender.values());
			model.addAttribute("errorMessage", "We encountered an error while creating your account. Please contact support.");
            return "auth/signup";
		}
		
		return REDIRECT + "/user/dashboard";
	}
	
	private void authenticateUserAndSetSession(SignUpRequest signupReq, UserProfile profile, HttpServletRequest request) {
        String username = signupReq.getEmail();
        String password = signupReq.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        LoginUtil.setSessionParams(request, profile);
    }
	
}
