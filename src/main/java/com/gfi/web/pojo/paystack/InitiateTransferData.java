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
public class InitiateTransferData implements Serializable {

	private static final long serialVersionUID = -1828467380167549092L;

	private String type;
	
	private String name;
	
	private String description;
	
	private MetaData metadata;
	
	private String domain;
	
	private RecipientDetails details;
	
	private String currency;
	
	private String recipient_code;
	
	private boolean active;
	
	private Integer id;
	
	private String createdAt;
	
	private String updatedAt;
	
}
