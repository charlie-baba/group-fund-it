/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;
import java.util.Date;

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
public class UserDto implements Serializable {

	private static final long serialVersionUID = -3993615484369307582L;

	private Long id;
	
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private String gender;
	
	private boolean active;
	
	private String phoneNumber;
	
	private Date dateOfBirth;
	
	private Integer age;
	
	private String pictureUrl;
	
	private String base64Image;
	
}
