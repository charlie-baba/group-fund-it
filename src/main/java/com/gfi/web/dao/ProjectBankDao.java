/**
 * 
 */
package com.gfi.web.dao;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.ProjectBank;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;

/**
 * @author Obi
 *
 */
@Repository
public class ProjectBankDao extends StandardJpaEntityBaseDao<ProjectBank>{

	@PostConstruct
    public void init() {
        super.setClazz(ProjectBank.class);
	}

	@Transactional
	public List<ProjectBank> findByProject(Long projectId) {
		String hql = "Select p from ProjectBank p where p.project.id = :projectId";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("projectId", projectId);
        return executeQuery(hql, paramsMap);
	}

	@Transactional
	public ProjectBank findPrimaryAcctByProject(Long projectId) {
		String hql = "Select p from ProjectBank p where p.project.id = :projectId and p.primaryAccount is true";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("projectId", projectId);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
}
