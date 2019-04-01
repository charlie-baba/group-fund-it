/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.bis.gfi.enums.SocialNetworkType;

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
public class OAuthRequest implements Serializable {

	private static final long serialVersionUID = -673030169390257812L;

	@NotBlank
	private String uid;
	
	private String email;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String gender;
	
	private String accessToken;
	
	private SocialNetworkType socialNetworkType;
}
