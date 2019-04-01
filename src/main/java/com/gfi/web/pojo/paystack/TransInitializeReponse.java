/**
 * 
 */
package com.gfi.web.pojo.paystack;

import com.gfi.web.pojo.GenericResponse;

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
public class TransInitializeReponse extends GenericResponse {

	private static final long serialVersionUID = 848136325410260013L;

	private String authUrl;
	
}
