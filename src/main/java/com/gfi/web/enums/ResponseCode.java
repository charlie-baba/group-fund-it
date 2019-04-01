/**
 * 
 */
package com.gfi.web.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Obi
 *
 */
public enum ResponseCode {

	SUCCESS("00", "Successful"),
	NOT_FOUND("01", "Record can not be found"),
    UNAUTHORIZED_OPERATION("02", "Unauthorized Operation"),
    INCORRECT_PASSWORD("03", "The password you entered is not correct."),
    SERVICE_NOT_AVAILABLE("04", "Service currently unavailable, please try again later"),
    TRANSACTION_FAILED("05", "Transaction failed."),
    INSUFFICIENT_FUND("06", "You do not have sufficient fund to initiate this transaction."),
    PAID_OUT("07", "This project has already been paid out."),
    OTP_ALREADY_DISABLED("08", "OTP already disabled for transfers."),
    OTP_ENABLED("09", "OTP requirement for payout has been enabled successfully."),
    SYSTEM_ERROR("ZZ", "We cannot process your request at this moment. Please try again later"),
    ;
	
	@Getter
	@Setter
	private String code;
	
	@Getter
	@Setter
	private String description;
	
	private ResponseCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
