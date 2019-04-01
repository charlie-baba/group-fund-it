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
public class InitiateTransferRequest implements Serializable {
	
	private static final long serialVersionUID = 5217143530437636828L;

	private String source = "balance";
	
	private String reason;
	
	private long amount;
	
	private String recipient;
	
}
