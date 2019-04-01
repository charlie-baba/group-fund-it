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
public class EmailPojo implements Serializable {
	
	private static final long serialVersionUID = -5656960517079056040L;
	
	@NotBlank(message = "Please enter a valid email")
	@Email
	private String email;
	
}
