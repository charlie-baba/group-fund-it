/**
 * 
 */
package com.gfi.web.service;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.bis.gfi.entities.Activity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfi.web.dao.ActivityDao;
import com.gfi.web.dao.GroupDao;
import com.gfi.web.dao.GroupMembersDao;
import com.gfi.web.dao.ProjectDao;
import com.gfi.web.dao.TransactionDao;
import com.gfi.web.pojo.ChartPojo;
import com.gfi.web.pojo.DashboardData;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Service
public class DashboardService {

	@Autowired
	GroupMembersDao memberDao;
	
	@Autowired
	ProjectDao projectDao;
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	GroupDao groupDao;

	@Autowired
	ActivityDao activityDao;

	@Autowired
	ProjectService projectService;
	
	@Value("${no_of_months_activity_log}")
	private int noOfMonthsInChart;
	private int monthsInBtw;
	private ObjectMapper mapper = new ObjectMapper();
	
	public DashboardData getDashboardData(String email) {
		DashboardData data = new DashboardData();
		
		try {
			monthsInBtw = (noOfMonthsInChart > 0) ? noOfMonthsInChart - 1 : 0;
			long myPrivateProjs = projectDao.countMyPrivateProjectsByEmail(email);
			long publicProjs = projectDao.countAllPublicProjects();
			Double amtDonated = transactionDao.totalAmountDonatedByEmail(email);
			
			data.setNoOfGroups(memberDao.countActiveGroupsByEmail(email));
			data.setNoOfProjects(myPrivateProjs + publicProjs);
			data.setAmountDonated(amtDonated == null ? 0d : amtDonated);
			data.setNoOfCampaigns(projectDao.countProjects(email));
			data.setTotalActivities(activityDao.countAll(email));
			data.setLastMonthActivities(activityDao.countByDate(email, java.sql.Date.valueOf(LocalDate.now().minusMonths(1)), new Date()));
			data.setActivitiesInChart(activityDao.countByDate(email, java.sql.Date.valueOf(LocalDate.now().withDayOfMonth(1).minusMonths(monthsInBtw)), new Date()));
			data.setActivityLog(getActivitiesLog(email));
			data.setChartData(getChartData(email));
			data.setPieCharts(getPieChartData(email));
			data.setPieChartData(mapper.writeValueAsString(data.getPieCharts()));
			//data.setMygroups(groupDao.findMemberGroupsByEmail(email, 0, 5));
			//data.setActiveProjects(projectDao.findAllByEmail(email, 0, 5));
		} catch (Exception e) {
			log.error("Error", e);
		}
		return data;
	}
	
	private Map<Date, List<Activity>> getActivitiesLog(String email) {
		List<Activity> list = activityDao.findByEmail(email, 0, 5);
		Map<Date, List<Activity>> activities = list.stream().collect(Collectors.groupingBy(Activity::getDatePerformed));
		Map<Date, List<Activity>> finalMap = new LinkedHashMap<>();
		activities.entrySet().stream().sorted(Map.Entry.<Date, List<Activity>>comparingByKey().reversed()).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
		return finalMap;
	}
	
	private ChartPojo getChartData(String email) {
		ChartPojo pojo = new ChartPojo();
		
		try {
			List<Object[]> list = activityDao.getByDateRange(email, java.sql.Date.valueOf(LocalDate.now().withDayOfMonth(1).minusMonths(monthsInBtw)), new Date());
			int s = YearMonth.now().minusMonths(monthsInBtw).getMonthValue();
			
			String[] labels = new String[noOfMonthsInChart];
			long[] data = new long[noOfMonthsInChart];
			
			for (int i = 0; i < noOfMonthsInChart; i++) {
				labels[i] = new DateFormatSymbols().getMonths()[s-1];
				data[i] = 0;
				for (Object[] arr : list) {
					if (arr[0] != null && (int)arr[0] == s) {
						data[i] = (long)arr[1];
					}
				}
				
				s = (s >= 12) ? 0 : s; 
				s++;
			}
			pojo.setData(mapper.writeValueAsString(data));
			pojo.setLabels(mapper.writeValueAsString(labels));
		} catch (Exception e) {
			log.error("", e);
		}
		return pojo;
	}
	
	private List<ChartPojo> getPieChartData(String email) {
		List<ChartPojo> pieCharts = new ArrayList<ChartPojo>();
		
		List<Object[]> projectObjs = projectDao.findRecentProjectsByEmail(email, 0, 2);
		if (!CollectionUtils.isEmpty(projectObjs) && projectObjs.get(0) != null) {
			for (Object[] obj : projectObjs) {
				try {
					ChartPojo pojo = new ChartPojo();
					pojo.setLabels((String) obj[1]);
					Double amount = checkNull(transactionDao.totalAmtContributedToProject((Long) obj[0]));
					Double targetAmount = checkNull((Double) obj[2]);
					long percentage = projectService.getAsPercentage(amount, targetAmount);
					
					long[] value = {percentage, 100-percentage};
					pojo.setData(mapper.writeValueAsString(value));
					pieCharts.add(pojo);
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
		
		return pieCharts;
	}
	
	public Double checkNull(Double amount) {
		return (amount == null) ? 0D : amount;
	}
	
}
