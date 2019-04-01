/**
 * 
 */
package com.gfi.web.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gfi.web.service.DashboardService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
@RequestMapping("/user")
public class DashboardController {
		
	@Autowired
	DashboardService dashboardService;	

	@Value("${no_of_months_activity_log}")
	private int noOfMonthsInChart;
	

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String loadDashboard(Model model, Principal principal) {
		log.info("loading dashboard");
		
		model.addAttribute("data", dashboardService.getDashboardData(principal.getName()));
		model.addAttribute("noOfMonths", noOfMonthsInChart);		
		return "modules/dashboard/dashboard";
	}
	
}
