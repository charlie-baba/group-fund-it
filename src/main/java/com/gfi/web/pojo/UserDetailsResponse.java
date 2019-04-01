/**
 * 
 */
package com.gfi.web.pojo;

import java.util.List;

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
public class UserDetailsResponse extends GenericResponse {

	private static final long serialVersionUID = 6903990747115423691L;

	private UserDto userDto;
		
	List<GroupDto> groups;
	
}
