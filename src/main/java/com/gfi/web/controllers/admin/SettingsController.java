/**
 * 
 */
package com.gfi.web.controllers.admin;

import static com.gfi.web.util.AppConstants.IS_ADMIN;
import static com.gfi.web.util.AppConstants.REDIRECT;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bis.gfi.entities.TransactionCharge;
import com.gfi.web.dao.SettingsDao;
import com.gfi.web.dao.TransactionChargeDao;
import com.gfi.web.enums.ResponseCode;
import com.gfi.web.enums.SettingsEnum;
import com.gfi.web.pojo.BooleanRequest;
import com.gfi.web.pojo.GenericResponse;
import com.gfi.web.pojo.OTPPojo;
import com.gfi.web.pojo.SettingsRequest;
import com.gfi.web.pojo.paystack.BaseResponse;
import com.gfi.web.service.PaystackService;
import com.gfi.web.service.SettingsService;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Controller
@RequestMapping("/admin/settings")
public class SettingsController {
	
	@Autowired
	SettingsService settingsService;
	
	@Autowired
	TransactionChargeDao transChargeDao;
	
	@Autowired
	SettingsDao settingsDao;
	
	@Autowired
	PaystackService paystackService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String viewSettings(HttpServletRequest req, Model model) {
		Boolean isAdmin = (Boolean)req.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			return REDIRECT + "/accessdenied";
		}
		log.info("loading settings");

		TransactionCharge transCharge = transChargeDao.getTransactionCharge();
		boolean otpEnabled = settingsDao.getSettingBoolean(SettingsEnum.PAYSTACK_OTP_ENABLED);
		model.addAttribute("settingsForm", SettingsRequest.fromTransactionCharge(transCharge, otpEnabled));
		return "modules/admin/settings/index";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveSettings(@ModelAttribute("settingsForm") @Valid SettingsRequest req, BindingResult result,
			Model model, Principal principal, HttpServletRequest httpReq) {
		log.info("saving application settings");
		Boolean isAdmin = (Boolean)httpReq.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			return REDIRECT + "/accessdenied";
		}

		if (result.hasErrors()) {
			return "modules/admin/users/index";
		}

		try {
			settingsService.updateTransaction(req, principal.getName());
			model.addAttribute("successMessage", "Settings was updated successfully.");
		} catch (Exception e) {
			log.error("", e);
			model.addAttribute("errorMessage", "An error occured while updating settings. Please try again.");
		}
		return "modules/admin/settings/index";
	}
	
	@ResponseBody 
	@RequestMapping(value = "/toggleOTP", method = RequestMethod.POST)
	public GenericResponse toggleOTP(@RequestBody BooleanRequest req, HttpServletRequest httpReq) {
		log.info("toggle Otp");
		GenericResponse resp = new GenericResponse();
		resp.assignResponseCode(ResponseCode.SYSTEM_ERROR);
		
		Boolean isAdmin = (Boolean)httpReq.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			resp.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return resp;
		}

		try {
			BaseResponse otpResp = paystackService.toggleOTP(req.isEnable());
			if (otpResp != null && otpResp.isStatus()) {
				if (otpResp.getMessage().equals("OTP already disabled for transfers")) {
					settingsService.updateSetting(SettingsEnum.PAYSTACK_OTP_ENABLED, "false");
					resp.assignResponseCode(ResponseCode.OTP_ALREADY_DISABLED);
				} else {
					if (req.isEnable()) {
						settingsService.updateSetting(SettingsEnum.PAYSTACK_OTP_ENABLED, "true");
						resp.assignResponseCode(ResponseCode.OTP_ENABLED);
					} else {
						resp.assignResponseCode(ResponseCode.SUCCESS);
					}
				}
			}
		} catch(Exception e) {
			log.error("", e);
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value = "/finalizeDisableOTP", method = RequestMethod.POST)
	public GenericResponse finalizeDisableOTP(@RequestBody OTPPojo otp, HttpServletRequest httpReq) {
		log.info("Finalize disable Otp");
		GenericResponse resp = new GenericResponse();
		resp.assignResponseCode(ResponseCode.SYSTEM_ERROR);
		
		Boolean isAdmin = (Boolean)httpReq.getSession().getAttribute(IS_ADMIN);
		if (!isAdmin) {
			resp.assignResponseCode(ResponseCode.UNAUTHORIZED_OPERATION);
			return resp;
		}

		try {
			BaseResponse otpResp = paystackService.finalizeDisbleOTP(otp);
			if (otpResp != null && otpResp.isStatus()) {
				settingsService.updateSetting(SettingsEnum.PAYSTACK_OTP_ENABLED, "false");
				resp.assignResponseCode(ResponseCode.SUCCESS);
			}
		} catch(Exception e) {
			log.error("", e);
		}
		return resp;
	}
	
}
