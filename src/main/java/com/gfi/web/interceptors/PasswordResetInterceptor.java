/**
 * 
 */
package com.gfi.web.interceptors;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bis.gfi.entities.UserProfile;
import com.gfi.web.service.UserService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
public class PasswordResetInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	UserService userService;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler, Principal principal) throws Exception {
		log.info("password reset interceptor");
		
		UserProfile user = userService.findUser(principal.getName());		
		if(user.isPasswordReset()) {
	         response.sendRedirect(request.getContextPath() +"/changepassword");
	         return false;
	     }
	     
	    return true;
	}
	
}
