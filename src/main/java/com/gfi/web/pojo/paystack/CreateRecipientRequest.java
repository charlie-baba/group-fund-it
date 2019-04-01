/**
 * 
 */
package com.gfi.web.pojo.paystack;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateRecipientRequest implements Serializable {

	private static final long serialVersionUID = -7660143761288048933L;

	private String type = "nuban";
	
	private String name;
	
	private String description;
	
	private String account_number;
	
	private String bank_code;
	
	private String currency = "NGN";
	
	private MetaData metadata;
	
}
