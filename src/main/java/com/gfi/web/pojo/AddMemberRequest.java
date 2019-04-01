/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

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
public class AddMemberRequest implements Serializable {
	
	private static final long serialVersionUID = -2545020098495799593L;

	private Long groupId;
	
	@Email
	@NotBlank(message = "Please enter a valid email")
	private String email;
	
}
