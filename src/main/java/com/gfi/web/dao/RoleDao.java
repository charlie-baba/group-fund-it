/**
 * 
 */
package com.gfi.web.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.bis.gfi.entities.Role;
import com.bis.gfi.enums.Roles;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;

/**
 * @author Obi
 *
 */
@Repository
public class RoleDao extends StandardJpaEntityBaseDao<Role> {

	@PostConstruct
    public void init() {
        super.setClazz(Role.class);
	}
	
	public Role getRole(Roles roleEnum) {
		String query = "select r from Role r where upper(r.name) = :name";
        Map<String, Object> map = new HashMap<>();
        map.put("name", roleEnum.name().toUpperCase());
        Role role = executeQueryUniqueResult(query, map);
        
        if (role != null) {
        	return role;
        }
        
        role = new Role();
        role.setName(roleEnum.name());
        role.setDescription(roleEnum.getDescription());
        create(role);
        return role;
	}
	
}
