/**
 * 
 */
package com.gfi.web.pojo.paystack;

import java.io.Serializable;

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
public class FinalizeTransferRequest implements Serializable {

	private static final long serialVersionUID = 4694356429460937396L;

	private String transfer_code;
	
	private String otp;
	
}
