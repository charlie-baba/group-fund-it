/**
 * 
 */
package com.gfi.config.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bis.gfi.entities.UserProfile;
import com.bis.gfi.entities.UserRole;
import com.gfi.web.dao.UserDao;


/**
 * @author Obi
 *
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	UserDao userDao;
	
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserProfile user = userDao.findByEmail(email);
		if(user == null) {
			logger.info("User not found");
			throw new UsernameNotFoundException("Email not found");
		}
		List<GrantedAuthority> authorities = getUserAuthorities(user.getUserRoles());
		return new User(user.getEmail(), user.getPassword(), user.isActive(), true, true, true, authorities);
	}

	/**
	 * @param userRoles
	 * @return
	 */
	private List<GrantedAuthority> getUserAuthorities(Set<UserRole> userRoles) {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		
		if(!CollectionUtils.isEmpty(userRoles)) {
			for (UserRole userRole : userRoles) {
				auths.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
			}
		}
		return auths;
	}

}
