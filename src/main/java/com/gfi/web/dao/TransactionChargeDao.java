/**
 * 
 */
package com.gfi.web.dao;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.bis.gfi.entities.TransactionCharge;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;

/**
 * @author Obi
 *
 */
@Repository
public class TransactionChargeDao extends StandardJpaEntityBaseDao<TransactionCharge> {

	@PostConstruct
    public void init() {
        super.setClazz(TransactionCharge.class);
	}
	
	@Transactional
	public TransactionCharge getTransactionCharge() {
		String hql = "Select t from TransactionCharge t";
		HashMap<String, Object> paramsMap = new HashMap<>();
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
}
