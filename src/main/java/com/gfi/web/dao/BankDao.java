/**
 * 
 */
package com.gfi.web.dao;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.Bank;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;

/**
 * @author Obi
 *
 */
@Repository
public class BankDao extends StandardJpaEntityBaseDao<Bank> {

	@PostConstruct
    public void init() {
        super.setClazz(Bank.class);
    }
		
	@Transactional
    public Bank findBankByCode(String code) {
        String query = "select c from Bank c where c.code = :param";
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("param", code);
        return executeQueryUniqueResult(query, paramsMap);
    }
}
