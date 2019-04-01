/**
 * 
 */
package com.gfi.web.pojo.paystack;

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
public class PaystackInitializeResponse extends BaseResponse {

	private static final long serialVersionUID = -8606035570761421059L;

	private InitializationData data;
	
}
