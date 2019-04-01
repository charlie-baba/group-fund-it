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
public class TransferDetails implements Serializable {

	private static final long serialVersionUID = -8754667858417535051L;

	private long integration;
	
	private String domain;
	
	private long amount;
	
	private String currency;
	
	private String source;
	
	private String reason;
	
	private int recipient;
	
	private String status;
	
	private String transfer_code;
	
	private int id;
	
	private String createdAt;
	
	private String updatedAt;
	
}
