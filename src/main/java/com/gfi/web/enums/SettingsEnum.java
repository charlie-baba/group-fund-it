/**
 * 
 */
package com.gfi.web.enums;

import lombok.Getter;

/**
 * @author Obi
 *
 */
public enum SettingsEnum {

	PROFILE_PIC_DIR("/files/picture/user/", "Holds all users profile pictures"),
	PROJECT_PIC_DIR("/files/picture/project/", "Holds all project pictures"),
	PAYSTACK_TEST_SECRET_KEY("sk_test_869079d2f76766d4257f74c0c3c7dd9d189acae1", "In seconds"),
    PAYSTACK_TEST_PUBLIC_KEY("pk_test_c5972e8f8ffb87a6775a6bb2550297ae74ee5e65", "In seconds"),
    PAYSTACK_LIVE_SECRET_KEY("sk_test_869079d2f76766d4257f74c0c3c7dd9d189acae1", "In seconds"),
    PAYSTACK_LIVE_PUBLIC_KEY("pk_test_c5972e8f8ffb87a6775a6bb2550297ae74ee5e65", "In seconds"),
    PAYSTACK_OTP_ENABLED("true", "Request for OTP when initiating paystack tranfer"),
    DEPLOYMENT_MODE("TEST", "Determines whether to use test or live api keys"),
    ;

	SettingsEnum(String defaultValue, String description) {
        this.defaultValue = defaultValue;
        this.description = description;
    }

	@Getter
    private String defaultValue;
    @Getter
    private String description;
    
}
