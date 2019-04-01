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
public class PaystackInitializeRequest implements Serializable {

	private static final long serialVersionUID = -5308758489510164418L;
	
	private String email;
	
	private long amount;
	
	private String reference;
	
	private String callback_url;

}
