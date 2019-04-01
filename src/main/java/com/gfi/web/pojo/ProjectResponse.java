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
public class ProjectResponse extends GenericResponse {

	private static final long serialVersionUID = -3806270651721131277L;

	private ProjectDto projectDto;
	
}
