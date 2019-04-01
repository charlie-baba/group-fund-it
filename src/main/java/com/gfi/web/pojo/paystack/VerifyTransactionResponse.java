/**
 * 
 */
package com.gfi.web.pojo.paystack;

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
public class VerifyTransactionResponse extends BaseResponse {

	private static final long serialVersionUID = 5386093907604351001L;

	private TransactionDetail data;
	
}
