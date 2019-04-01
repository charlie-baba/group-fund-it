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
public class AmountResponse extends GenericResponse {

	private static final long serialVersionUID = -4697445032684157289L;

	private Double amount;
	
}
