/**
 * 
 */
package com.gfi.web.dao;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.bis.gfi.entities.UserRole;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;

/**
 * @author Obi
 *
 */
@Repository
public class UserRoleDao extends StandardJpaEntityBaseDao<UserRole> {

	@PostConstruct
    public void init() {
        super.setClazz(UserRole.class);
	}
	
	
}
