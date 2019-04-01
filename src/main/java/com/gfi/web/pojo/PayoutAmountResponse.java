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
public class PayoutAmountResponse extends GenericResponse {

	private static final long serialVersionUID = 3524733231135026990L;

	private String amount;
	
}
