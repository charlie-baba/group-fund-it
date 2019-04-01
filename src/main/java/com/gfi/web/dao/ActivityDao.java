/**
 * 
 */
package com.gfi.web.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.Activity;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;

/**
 * @author Obi
 *
 */
@Repository
public class ActivityDao extends StandardJpaEntityBaseDao<Activity> {

	@PostConstruct
    public void init() {
        super.setClazz(Activity.class);
    }
		
	@Transactional
	public List<Activity> findByEmail(String email, int start, int size) {
		String hql = "select a from Activity a where a.performedBy = :email order by a.createDate desc";
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        return executeQuery(hql, paramsMap, start, size);
	}
	
	@Transactional
	public long countAll(String email) {
		String hql = "select count(a) from Activity a where a.performedBy = :email";
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
	@Transactional
	public long countByDate(String email, Date startDate, Date endDate) {
		String hql = "select count(a) from Activity a where a.performedBy = :email and DATE(a.datePerformed) >= :startDate and DATE(a.datePerformed) <= :endDate";
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        paramsMap.put("startDate", startDate);
        paramsMap.put("endDate", endDate);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
	@Transactional
	public List<Object[]>  getByDateRange(String email, Date startDate, Date endDate) {
		String hql = "select MONTH(a.datePerformed) AS month, count(a) from Activity a where a.performedBy = :email and DATE(a.datePerformed) >= :startDate and DATE(a.datePerformed) <= :endDate "
				+ "group by MONTH(a.datePerformed)";
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        paramsMap.put("startDate", startDate);
        paramsMap.put("endDate", endDate);
        return executeQuery(hql, paramsMap);
	}
	
}
