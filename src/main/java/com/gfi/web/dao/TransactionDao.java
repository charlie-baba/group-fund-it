/**
 * 
 */
package com.gfi.web.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.Transaction;
import com.bis.gfi.enums.TransactionStatus;
import com.bis.gfi.enums.TransactionType;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;
import com.gfi.web.pojo.TransactionDto;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Repository
public class TransactionDao extends StandardJpaEntityBaseDao<Transaction> {

	@PostConstruct
    public void init() {
        super.setClazz(Transaction.class);
	}
	
	@Transactional
	public Transaction findByRef(String transRef) {
		String hql = "Select t from Transaction t left join fetch t.project where t.transactionRef = :ref";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ref", transRef);
    	return executeQueryUniqueResult(hql, paramsMap);
	}
	
	@Transactional
	public Double totalAmtContributedToProject(Long projectId) {
		String hql = "Select sum(t.amount) from Transaction t where t.project.id = :projectId and t.status = :successful and t.transactionType = :donation";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("projectId", projectId);
        paramsMap.put("donation", TransactionType.DONATION);
        paramsMap.put("successful", TransactionStatus.SUCCESSFUL);
        Double result = executeQueryUniqueResult(hql, paramsMap);
        return result == null ? 0D : result;
	}
	
	@Transactional
	public Double totalAmountDonatedByEmail(String email) {
		String hql = "Select sum(t.amount) from Transaction t where t.email = :email and t.status = :successful and t.transactionType = :donation";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        paramsMap.put("donation", TransactionType.DONATION);
        paramsMap.put("successful", TransactionStatus.SUCCESSFUL);
        Double result = executeQueryUniqueResult(hql, paramsMap);
        return result == null ? 0D : result;
	}
	
	@Transactional
	public List<TransactionDto> findDonationsByProject(Long projectId) {
		String hql = "Select t.email, t.amount, t.date from Transaction t where t.project.id = :projectId and t.status = :successful and t.transactionType = :donation";
        List<TransactionDto> entityList = new ArrayList<>();
        Session session = null;
        try {
            session = getSession();
            Query<TransactionDto> q = session.createQuery(hql);
            q.setParameter("projectId", projectId);
            q.setParameter("donation", TransactionType.DONATION);
            q.setParameter("successful", TransactionStatus.SUCCESSFUL);
            entityList = q.list();
        } catch (Exception ex) {
        	log.error("", ex);
        }
        return entityList;
	}
	
	@Transactional
	public List<TransactionDto> findHighestDonationsByProject(Long projectId, int start, int size) {
		String hql = "Select t.email, t.date, t.amount from Transaction t where t.project.id = :projectId and t.status = :successful and t.transactionType = :donation "
				+ "order by t.amount desc";
        List<TransactionDto> entityList = new ArrayList<>();
        Session session = null;
        try {
            session = getSession();
            Query<TransactionDto> q = session.createQuery(hql);
            q.setParameter("projectId", projectId);
            q.setParameter("donation", TransactionType.DONATION);
            q.setParameter("successful", TransactionStatus.SUCCESSFUL);
            q.setFirstResult(start);
            q.setMaxResults(size);
            entityList = q.list();
        } catch (Exception ex) {
        	log.error("", ex);
        }
        return entityList;
	}
	
}
