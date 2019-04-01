/**
 * 
 */
package com.gfi.web.service;

import static com.gfi.web.util.AppConstants.sdf;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bis.gfi.entities.GroupMembers;
import com.bis.gfi.entities.SocialNetworkDetail;
import com.bis.gfi.entities.UserProfile;
import com.bis.gfi.entities.UserRole;
import com.bis.gfi.enums.ActivityType;
import com.bis.gfi.enums.Gender;
import com.bis.gfi.enums.MemberStatus;
import com.bis.gfi.enums.Roles;
import com.gfi.config.security.util.LoginUtil;
import com.gfi.web.dao.GroupMembersDao;
import com.gfi.web.dao.RoleDao;
import com.gfi.web.dao.SocialNetworkDetailDao;
import com.gfi.web.dao.UserDao;
import com.gfi.web.dao.UserRoleDao;
import com.gfi.web.enums.ResponseCode;
import com.gfi.web.enums.SettingsEnum;
import com.gfi.web.pojo.GenericResponse;
import com.gfi.web.pojo.OAuthRequest;
import com.gfi.web.pojo.ProfileRequest;
import com.gfi.web.pojo.SignUpRequest;
import com.gfi.web.util.EmailUtil;
import com.gfi.web.util.ImageUtil;
import com.gfi.web.util.PasswordGenerator;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	SocialNetworkDetailDao socialNtwkDao;
	
	@Autowired
	UserRoleDao userRoleDao;
	
	@Autowired
	EmailUtil emailUtil;
	
	@Autowired
	ImageUtil imageUtil;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	GroupMembersDao groupMembersDao;
	
	public UserProfile findUser(String email) {
		return userDao.findByEmail(email);
	}
	
	public boolean emailExists(String email) {
		return findUser(email) != null;
	}
		
	public static boolean isAdmin(Set<UserRole> userRoles) {
		if (CollectionUtils.isEmpty(userRoles)) {
			return false;
		}
		
		for (UserRole userRole : userRoles) {
			if (Roles.ADMIN.name().equals(userRole.getRole().getName()))
				return true;			
		}
		return false;
	}
	
	@Transactional
	public UserProfile registerUser(SignUpRequest req) {
		UserProfile user = new UserProfile();
		user.setFirstName(req.getFirstname());
		user.setLastName(req.getLastname());
		user.setPhoneNumber(req.getPhone());
		user.setEmail(req.getEmail());
		user.setPassword(passwordEncoder.encode(req.getPassword()));
		userDao.create(user);
		createUserRole(user, Roles.USER);
		updateGroupMemberships(req.getEmail());
		log.info("new user created successfully.");
		return user;
	}
	
	@Transactional
	public UserProfile registerUser(OAuthRequest req) {
		UserProfile user = new UserProfile();
		user.setFirstName(req.getFirstName());
		user.setMiddleName(req.getMiddleName());
		user.setLastName(req.getLastName());
		user.setEmail(req.getEmail());
		user.setPassword(passwordEncoder.encode(PasswordGenerator.generate(8)));		
		if (StringUtils.isNotBlank(req.getGender())) {
			user.setGender(Gender.valueOf(req.getGender().trim().toUpperCase()));
		}
		userDao.create(user);
		createUserRole(user, Roles.USER);
		createSocialNetworkDetail(req, user);
		updateGroupMemberships(req.getEmail());
		log.info("new user created successfully.");
		return user;
	}
	
	@Transactional
	public void createSocialNetworkDetail(OAuthRequest req, UserProfile user) {
		SocialNetworkDetail socNtwkDetail = new SocialNetworkDetail();
		socNtwkDetail.setUserId(req.getUid());
		socNtwkDetail.setEmail(req.getEmail());
		socNtwkDetail.setSocialNetworkType(req.getSocialNetworkType());
		socNtwkDetail.setUser(user);
		socialNtwkDao.create(socNtwkDetail);
	}

	@Transactional
	public void createUserRole(UserProfile user, Roles role) {
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(roleDao.getRole(role));
		userRoleDao.create(userRole);
	}

	@Transactional
	public boolean resetPassword(UserProfile user) {
		if (user == null) {
			return false;
		}
		
		try {
			String password = PasswordGenerator.generate(8);
			user.setPassword(passwordEncoder.encode(password));
			user.setPasswordReset(true);
			userDao.update(user);
			emailUtil.sendPasswordReset(password, user);
		} catch(Exception ex) {
			log.error("Could not reset password: ", ex);
			return false;
		}
		return true; 
	}
	
	@Transactional
	public boolean changePassword(String password, String email) {
		UserProfile user = userDao.findByEmail(email);
		if(user == null) {
			return false;
		}
		
		try {
			user.setPassword(passwordEncoder.encode(password));
			user.setPasswordReset(false);
			userDao.update(user);
		} catch (HibernateException ex) {
			log.error("Could not change password", ex);
			return false;
		}
		return true;
	}
	
	@Transactional
	public UserProfile editProfile(UserProfile profile, ProfileRequest req, String email) {
		profile.setFirstName(req.getFirstName());
		profile.setMiddleName(req.getMiddleName());
		profile.setLastName(req.getLastName());
		profile.setPhoneNumber(req.getPhoneNumber());
		if (req.getAge() != null) {
			profile.setAge(req.getAge());
		}
		if (StringUtils.isNotBlank(req.getDateOfBirth())) {
			try {
				profile.setDateOfBirth(sdf.parse(req.getDateOfBirth()));
			} catch (ParseException e) {
				log.error("Unable to format date ", e);
			}
		}
		profile.setGender(req.getGender());
		profile.setAboutMe(req.getAboutMe());
		userDao.update(profile);
		activityService.createActivity(email, ActivityType.UPDATE_PROFILE);
		return profile;
	}
	
	@Transactional
	public GenericResponse changePassword(UserProfile profile, String currentPassword, String newPassword) {
		GenericResponse resp = new GenericResponse();
		if (profile == null) {
			resp.assignResponseCode(ResponseCode.SYSTEM_ERROR);
			return resp;
		}
		
		if (!passwordEncoder.matches(currentPassword, profile.getPassword())) {
			resp.assignResponseCode(ResponseCode.INCORRECT_PASSWORD);
			return resp;
		}
		
		profile.setPassword(passwordEncoder.encode(newPassword));
		profile.setPasswordReset(false);
		userDao.update(profile);
		resp.assignResponseCode(ResponseCode.SUCCESS);
		return resp;
	}
	
	@Transactional
	public void changeProfilePicture(UserProfile profile, MultipartFile picture) {
		if(picture == null || StringUtils.isBlank(picture.getOriginalFilename())) {
			return;
		}
		
		String imagePath = imageUtil.saveImage(picture, profile.getId().toString(), SettingsEnum.PROFILE_PIC_DIR);
		if (StringUtils.isNotBlank(imagePath)) {
			profile.setPictureUrl(imagePath);
			userDao.update(profile);
		}
	}	

	@Transactional
	public boolean setActivationStatus(UserProfile user, boolean status, String email) {
		if(user == null) {
			return false;
		}
		
		user.setActive(status);
		if (!status) {
			String password = PasswordGenerator.generate(8);
			user.setPassword(passwordEncoder.encode(password));
			user.setPasswordReset(true);
		}
		userDao.update(user);
		activityService.createActivity(email, status ? ActivityType.ACTIVATE_USER : ActivityType.DEACTIVATE_USER, user.getEmail());
		return true;
	}
	
	@Transactional
	private void updateGroupMemberships(String email) {
		List<GroupMembers> members = groupMembersDao.findByEmail(email);
		if (CollectionUtils.isEmpty(members)) {
			return;
		}
		
		for (GroupMembers member : members) {
			member.setStatus(MemberStatus.SIGNED_UP);
			groupMembersDao.update(member);
		}
	}
	
	public String authenticateUserAndSetSession(UserProfile profile, String password, HttpServletRequest request, Roles roles) {
		String role = roles == null ? profile.getUserRoles().iterator().next().getRole().getName() : roles.name();
		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);
		User user = new User(profile.getEmail(), password, authorities);		
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, password, authorities);

        // generate session if one doesn't exist
        request.getSession();
        LoginUtil.setSessionParams(request, profile);

        token.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
        return LoginUtil.getTargetUrlAfterSuccessfulLogin(token);
    }
	
}
