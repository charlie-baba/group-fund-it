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
public class InitiateTransferResponse extends BaseResponse {
	
	private static final long serialVersionUID = -52559322198984112L;
	
	private TransferDetails data;

}
