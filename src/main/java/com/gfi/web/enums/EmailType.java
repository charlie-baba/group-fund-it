/**
 * 
 */
package com.gfi.web.enums;

import lombok.Getter;

/**
 * @author Obi
 *
 */
public enum EmailType {
	
	GENERAL_TEMPLATE("General.ftl"),
	PASSWORD_RESET("Password Reset.ftl"),
	GROUP_INVITE("Group Invite.ftl"),
	PUBLISH_PROJECT("Publish Project.ftl"),
	DONATION("Donation.ftl"),
	PAYOUT("Payout.ftl"),
	;
	
	@Getter
	String templateName;
	
	private EmailType(String templateName) {
        this.templateName = templateName;
    }

}
