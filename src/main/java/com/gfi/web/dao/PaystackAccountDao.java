/**
 * 
 */
package com.gfi.web.dao;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.PaystackAccount;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;

/**
 * @author Obi
 *
 */
@Repository
public class PaystackAccountDao extends StandardJpaEntityBaseDao<PaystackAccount>{

	@PostConstruct
    public void init() {
        super.setClazz(PaystackAccount.class);
	}
	
	@Transactional
	public PaystackAccount findByBankAndAcctNo(String bankCode, String acctNo) {
		String hql = "Select p from PaystackAccount p where p.bankCode = :bankCode and p.accountNumber = :acctNo";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("bankCode", bankCode);
        paramsMap.put("acctNo", acctNo);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
}
