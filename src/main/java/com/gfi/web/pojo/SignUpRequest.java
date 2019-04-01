/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

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
public class SignUpRequest implements Serializable {

	private static final long serialVersionUID = 2008813827657252151L;

	@NotBlank(message = "First name is required")
	private String firstname;
	
	private String middlename;
		
	@NotBlank(message = "Last name is required")
	private String lastname;
	
	@NotBlank(message = "Please enter a valid email")
	@Email
	private String email;
	
	private String phone;
	
	@NotEmpty
	@Size(min = 6, message = "Your password must not be less than 6 characters")
	private String password;
	
	@NotEmpty
	@Size(min = 6, message = "Your password must not be less than 6 characters")
	private String confirmPassword;
	
}