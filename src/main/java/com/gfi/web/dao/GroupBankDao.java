/**
 * 
 */
package com.gfi.web.dao;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.GroupBank;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;

/**
 * @author Obi
 *
 */
@Repository
public class GroupBankDao extends StandardJpaEntityBaseDao<GroupBank> {
	
	@PostConstruct
    public void init() {
        super.setClazz(GroupBank.class);
	}

	@Transactional
	public List<GroupBank> findByGroup(Long groupId) {
		String hql = "Select g from GroupBank g where g.group.id = :groupId";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("groupId", groupId);
        return executeQuery(hql, paramsMap);
	}
	
	@Transactional
	public GroupBank findPrimaryAcctByGroup(Long groupId) {
		String hql = "Select g from GroupBank g where g.group.id = :groupId and g.primaryAccount is true";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("groupId", groupId);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
}
