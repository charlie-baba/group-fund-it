/**
 * 
 */
package com.gfi.web.pojo;

import static com.gfi.web.util.AppConstants.sdf;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.bis.gfi.entities.UserProfile;
import com.bis.gfi.enums.Gender;

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
public class ProfileRequest implements Serializable {

	private static final long serialVersionUID = -7524224703713059306L;

	@NotBlank(message = "First name is required")
	private String firstName;

	private String middleName;

	@NotBlank(message = "Last name is required")
	private String lastName;
	
	private String phoneNumber;
	
	private String email;
	
	private Integer age;
	
	private String dateOfBirth;

	@NotNull(message = "Gender is required")
	private Gender gender;
	
	private String pictureUrl;
	
	private String aboutMe;
	
	
	public static ProfileRequest fromUserProfile(UserProfile profile) {
		ProfileRequest req = new ProfileRequest();
		req.firstName = profile.getFirstName();
		req.middleName = profile.getMiddleName();
		req.lastName = profile.getLastName();
		req.phoneNumber = profile.getPhoneNumber();
		req.email = profile.getEmail();
		req.age = profile.getAge();
		req.dateOfBirth = profile.getDateOfBirth() != null ? sdf.format(profile.getDateOfBirth()) : null;
		req.gender = profile.getGender();
		req.pictureUrl = profile.getPictureUrl();
		req.aboutMe = profile.getAboutMe();
		return req;
	}
	
}
