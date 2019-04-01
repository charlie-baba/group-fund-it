/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bis.gfi.entities.Activity;
import com.bis.gfi.entities.Group;
import com.bis.gfi.entities.Project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Obi
 *
 */
@Getter
@Setter
@ToString
public class DashboardData implements Serializable {

	private static final long serialVersionUID = 6266664535379180599L;
	
	private long noOfGroups;
	
	private long noOfProjects;
	
	private double amountDonated;
	
	private long noOfCampaigns;
	
	private long totalActivities;
	
	private long lastMonthActivities;
	
	private long activitiesInChart;
	
	private ChartPojo chartData;
	
	private List<ChartPojo> pieCharts;
	
	private String pieChartData;
	
	private Map<Date, List<Activity>> activityLog;
	

	private List<Group> mygroups;
	
	private List<Project> activeProjects;

}
