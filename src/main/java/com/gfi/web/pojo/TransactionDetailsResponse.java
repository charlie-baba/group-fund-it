/**
 * 
 */
package com.gfi.web.pojo;

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
public class TransactionDetailsResponse extends GenericResponse {
	
	private static final long serialVersionUID = 5182983642276294681L;

	private String email;
	
	private String transRef;
	
	private long amount;
	
}
