/**
 * 
 */
package com.gfi.web.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
public class SearchController {

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String viewGroups(@ModelAttribute("q")String search, Model model, Principal principal) {
		log.info("searching... "+ search);
		model.addAttribute("searchQ", search);
		model.addAttribute("sizeOfResult", 2);
		model.addAttribute("resultTime", 0.213);
		return "modules/search";
	}
	
}
