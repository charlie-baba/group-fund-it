/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Obi
 *
 */
@Setter
@Getter
@ToString
public class ChangePasswordRequest implements Serializable {

	private static final long serialVersionUID = 4953204039021543669L;

	@NotBlank(message="Current Password is required")
	private String currentPassword;

	@NotBlank(message="New Password is required")
	private String newPassword;

	@NotBlank(message="Confirm Password is required")
	private String confirmPassword;
	
}
