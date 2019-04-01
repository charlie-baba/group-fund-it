/**
 * 
 */
package com.gfi.config.security.util;

import static com.gfi.web.util.AppConstants.FIRST_NAME;
import static com.gfi.web.util.AppConstants.FULL_NAME;
import static com.gfi.web.util.AppConstants.IS_ADMIN;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.bis.gfi.entities.UserProfile;
import com.bis.gfi.enums.Roles;
import com.gfi.web.service.UserService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
public class LoginUtil {

	public static String getTargetUrlAfterSuccessfulLogin(Authentication authentication) {
		String url = "";		 
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); 
 
        for (GrantedAuthority a : authorities) {
            log.info("authority - "+ a.getAuthority());
            if (Roles.ADMIN.name().equals(a.getAuthority())) {
            	url = "/admin/users/index";
            } else if (Roles.USER.name().equals(a.getAuthority())) {
            	url = "/user/dashboard";
            }
        }
        
		return url;
    }
	
	public static void setSessionParams(HttpServletRequest request, UserProfile profile) {
		 request.getSession().setAttribute(FULL_NAME, profile.getFirstAndLastName());
	     request.getSession().setAttribute(FIRST_NAME, profile.getFirstName());
	     request.getSession().setAttribute(IS_ADMIN, UserService.isAdmin(profile.getUserRoles()));
	}
	
}
