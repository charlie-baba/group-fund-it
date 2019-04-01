/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
public class DonationRequest implements Serializable {

	private static final long serialVersionUID = 3181135777208236645L;
	
	private Long projectId;
	
	@NotNull
	private double amount;
	
	private String email;
		
	private boolean anonymous;
	
}
