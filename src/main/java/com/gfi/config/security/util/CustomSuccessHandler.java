/**
 * 
 */
package com.gfi.config.security.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.bis.gfi.entities.UserProfile;
import com.gfi.web.dao.UserDao;

/**
 * @author Obi
 *
 */
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	UserDao userDao;
	
    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String targetUrl = LoginUtil.getTargetUrlAfterSuccessfulLogin(authentication);
        
        try {
	        User user = (User)authentication.getPrincipal();
	        UserProfile profile = userDao.findByEmail(user.getUsername());
	        LoginUtil.setSessionParams(request, profile);
        } catch (Exception e) {
        	System.out.println("Unable to add full name to session " + e.getMessage());
        }
        
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
    
}
