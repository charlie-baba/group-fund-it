/**
 * 
 */
package com.gfi.web.pojo;

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
public class OTPPojo implements Serializable {

	private static final long serialVersionUID = 1389038088867230177L;

	private String otp;
	
}
