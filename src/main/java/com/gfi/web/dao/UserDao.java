/**
 * 
 */
package com.gfi.web.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.bis.gfi.entities.UserProfile;
import com.bis.gfi.enums.Gender;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;
import com.gfi.web.pojo.UserDto;

/**
 * @author Obi
 *
 */
@Repository
public class UserDao extends StandardJpaEntityBaseDao<UserProfile> {

	@PostConstruct
    public void init() {
        super.setClazz(UserProfile.class);
    }
	
	@Transactional
    public UserProfile findByEmail(String email) {
		String hql = "Select u from UserProfile u where lower(u.email) = :email";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email.toLowerCase());
    	return executeQueryUniqueResult(hql, paramsMap);
    }
	
	@Transactional
    public List<UserDto> findAllUsers() {
		String hql = "Select u.id, u.email, u.firstName, u.lastName, u.gender, u.active from UserProfile u";
    	return executeQuery(hql, new HashMap<>());
    }
	
	@Transactional
    public Object[] findFirstAndLastName(String email) {
		String hql = "Select u.id, u.firstName, u.lastName from UserProfile u where lower(u.email) = :email";
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("email", email.toLowerCase());
    	return executeQueryUniqueResult(hql, paramsMap);
    }
	
	@Transactional
    public UserDto findUserDetails(Long userId) {
		String hql = "Select u.id, u.email, u.firstName, u.lastName, u.gender, u.active, u.phoneNumber, "
				+ "u.dateOfBirth, u.age, u.pictureUrl from UserProfile u where u.id = :userId";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
    	return transformToUser(executeQueryUniqueResult(hql, paramsMap));
    }
	

	public UserDto transformToUser(Object[] obj) {
		if (obj == null) {
			return null;
		}
		
		UserDto user = new UserDto();
		user.setId((Long) obj[0]);
		user.setEmail((String) obj[1]);
		user.setFirstName((String) obj[2]);
		user.setLastName((String) obj[3]);
		user.setGender(((Gender) obj[4]).name());
		user.setActive((boolean) obj[5]);
		user.setPhoneNumber((String) obj[6]);
		user.setDateOfBirth((Date) obj[7]);
		user.setAge((Integer) obj[8]);
		user.setPictureUrl((String) obj[9]);
		return user;
	}
	
}