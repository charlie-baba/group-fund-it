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
public class RecipientDetails implements Serializable {
	
	private static final long serialVersionUID = -6063107864590850074L;

	private String account_number;
	
	private String account_name;
	
	private String bank_code;
	
	private String bank_name;
	
}
