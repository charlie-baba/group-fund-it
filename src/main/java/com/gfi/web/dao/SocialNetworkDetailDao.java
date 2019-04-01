/**
 * 
 */
package com.gfi.web.dao;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.SocialNetworkDetail;
import com.bis.gfi.enums.SocialNetworkType;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;

/**
 * @author Obi
 *
 */
@Repository
public class SocialNetworkDetailDao extends StandardJpaEntityBaseDao<SocialNetworkDetail> {

	@PostConstruct
    public void init() {
        super.setClazz(SocialNetworkDetail.class);
    }

	@Transactional
	public SocialNetworkDetail findByUserIdandSocialNtwkType(String uid, SocialNetworkType socialNtwkType) {
		String hql = "Select s from SocialNetworkDetail s where s.userId = :uid and s.socialNetworkType =:socialNtwkType";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("uid", uid);
        paramsMap.put("socialNtwkType", socialNtwkType);
        return executeQueryUniqueResult(hql, paramsMap);
	}
}
