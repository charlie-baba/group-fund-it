/**
 * 
 */
package com.gfi.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bis.gfi.entities.TransactionCharge;
import com.bis.gfi.enums.DonationChargeType;
import com.gfi.web.dao.TransactionChargeDao;

/**
 * @author Obi
 *
 */
@Component
public class PayCalculator {

	@Autowired
	TransactionChargeDao transChargeDao;
	
	public Double getTotalDonationAmt(Double amountWithoutTransCharge) {
		return amountWithoutTransCharge + getChargeAmount(amountWithoutTransCharge);
	}
	
	public Double getChargeAmount(Double amount) {
		Double chargeAmount = 0D;
		TransactionCharge charge = transChargeDao.getTransactionCharge();
		if (charge == null || amount == null || amount == 0D ) {
			return chargeAmount;
		}
		
		if (DonationChargeType.Flat_Fee == charge.getChargeType()) {
			chargeAmount = charge.getFlatAmount() == null ? 0D : charge.getFlatAmount();
		} else {
			Double percentage = charge.getPercentageAmount() == null ? 0D : charge.getPercentageAmount();
			if (percentage != 0D) {
				chargeAmount = (amount * percentage / 100);
			}
		}
		return chargeAmount;
	}
	
}
