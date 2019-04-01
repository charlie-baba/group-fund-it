/**
 * 
 */
package com.gfi.web.pojo.paystack;

import java.io.Serializable;

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
public class BaseResponse implements Serializable {

	private static final long serialVersionUID = -2271242270661677570L;

	private boolean status;
	
	private String message;
	
}
