/**
 * 
 */
package com.gfi.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.Settings;
import com.bis.gfi.entities.TransactionCharge;
import com.bis.gfi.enums.ActivityType;
import com.bis.gfi.enums.DonationChargeType;
import com.gfi.web.dao.SettingsDao;
import com.gfi.web.dao.TransactionChargeDao;
import com.gfi.web.enums.SettingsEnum;
import com.gfi.web.pojo.SettingsRequest;

/**
 * @author Obi
 *
 */
@Service
public class SettingsService {

	@Autowired
	TransactionChargeDao transChargeDao;
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	SettingsDao settingsDao;
	
	@Transactional
	public void updateTransaction(SettingsRequest req, String email) {
		if (req == null) {
			return;
		}
		
		TransactionCharge transCharge = transChargeDao.getTransactionCharge();
		if (transCharge == null) {
			transCharge = new TransactionCharge();
		}
		transCharge.setChargeType(req.getDonationChargeType());
		if (DonationChargeType.Flat_Fee == req.getDonationChargeType()) {
			transCharge.setFlatAmount(req.getFlatAmount());
			transCharge.setPercentageAmount(null);
		} else {
			transCharge.setFlatAmount(null);
			transCharge.setPercentageAmount(req.getPercentageAmount());
		}
		transChargeDao.update(transCharge);
		activityService.createActivity(email, ActivityType.UPDATE_SETTING);
	}
	
	@Transactional
	public void updateSetting(SettingsEnum settingEnum, String value) {
		Settings setting = settingsDao.getSettingObj(settingEnum);
		setting.setValue(value);
		settingsDao.update(setting);
	}
	
}
