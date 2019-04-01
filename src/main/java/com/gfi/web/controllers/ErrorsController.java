/**
 * 
 */
package com.gfi.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**1
 * @author Obi
 *
 */
@Controller
public class ErrorsController {

	@RequestMapping(value = "/error-500", method = RequestMethod.GET)
	public String error500(Model model) {
		return "error/500";
	}
	
	@RequestMapping(value = "/error-404", method = RequestMethod.GET)
	public String error400(Model model) {
		return "error/404";
	}

}
