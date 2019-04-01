/**
 * 
 */
package com.gfi.web.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * @author Obi
 *
 */
public class AppConstants {

	public static final String REDIRECT = "redirect:";
	public static final String HOME_DIR = "user.dir"; //"jboss.home.dir";
	public static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
	public static final DecimalFormat df = new DecimalFormat("###,###,###,###.##");
	public static final String PROFILE_PICTURE = "profile_picture";
	public static final String FULL_NAME = "full_name";
	public static final String FIRST_NAME = "first_name";
	public static final String IS_ADMIN = "is_admin";
	public static final String PAYSTACK_BASE_URL = "https://api.paystack.co";
    public final static String PAYSTACK_INITIALIZE_PATH = "/transaction/initialize";
    public final static String PAYSTACK_VERIFY_PATH = "/transaction/verify/";
    public final static String PAYSTACK_TRANSFER_RECIPIENT_PATH = "/transferrecipient";
    public final static String PAYSTACK_INITIATE_TRANSFER_PATH = "/transfer";
    public final static String PAYSTACK_FINALIZE_TRANSFER_PATH = "/transfer/finalize_transfer";
    public final static String PAYSTACK_DISABLE_OTP_PATH = "/transfer/disable_otp";
    public final static String PAYSTACK_FINALIZE_DISABLE_OTP_PATH = "/transfer/disable_otp_finalize";
    public final static String PAYSTACK_ENABLE_OTP_PATH = "/transfer/enable_otp";
	
}
