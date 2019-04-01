/**
 * 
 */
package com.gfi.web.pojo;

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
public class GroupResponse extends GenericResponse {

	private static final long serialVersionUID = 3932724821074000449L;

	private GroupDto groupDto;
	
}
