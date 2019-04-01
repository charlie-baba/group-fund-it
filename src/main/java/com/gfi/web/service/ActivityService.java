/**
 * 
 */
package com.gfi.web.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.Activity;
import com.bis.gfi.enums.ActivityType;
import com.gfi.web.dao.ActivityDao;

/**
 * @author Obi
 *
 */
@Service
public class ActivityService {

	@Autowired
	ActivityDao activityDao;
	
	@Transactional
	public void createActivity(String email, ActivityType type, String param) {
		String title = type.getDescription();
		if (StringUtils.isNotBlank(param)) {
			title += param;
		}
		
		Activity activity = new Activity();
		activity.setPerformedBy(email);
		activity.setType(type);
		activity.setTitle(title);
		activity.setDatePerformed(new Date());
		activityDao.create(activity);
	}
	
	@Transactional
	public void createActivity(String email, ActivityType type) {
		createActivity(email, type, null);
	}
	
}
