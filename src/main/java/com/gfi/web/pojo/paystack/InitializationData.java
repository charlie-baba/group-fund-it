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
public class InitializationData implements Serializable {

	private static final long serialVersionUID = 2819314118314746248L;

	private String authorization_url;

	private String access_code;

	private String reference;
	
}
