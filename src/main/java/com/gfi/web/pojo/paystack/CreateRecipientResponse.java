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
public class CreateRecipientResponse extends BaseResponse {

	private static final long serialVersionUID = -6467155889311020887L;

	private InitiateTransferData data;
	
}
