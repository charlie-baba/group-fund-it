/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import com.bis.gfi.entities.TransactionCharge;
import com.bis.gfi.enums.DonationChargeType;

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
public class SettingsRequest implements Serializable {

	private static final long serialVersionUID = 2073173852259153155L;

	private DonationChargeType donationChargeType;
	
	private Double flatAmount;
	
	private Double percentageAmount;
	
	private boolean otpEnabled;
	
	public static SettingsRequest fromTransactionCharge(TransactionCharge charge, boolean otpEnabled) {
		SettingsRequest req = new SettingsRequest();
		if (charge != null) {
			req.setDonationChargeType(charge.getChargeType());
			req.setFlatAmount(charge.getFlatAmount());
			req.setPercentageAmount(charge.getPercentageAmount());
		}
		req.setOtpEnabled(otpEnabled);
		return req;
	}
	
}
