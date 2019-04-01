/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;
import java.util.List;

import com.bis.gfi.entities.Project;

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
public class ProjectDetails implements Serializable {

	private static final long serialVersionUID = -7259423207454711158L;
	
	private Project project;
	
	private List<String> images;
	
	private long projectStatus;

}
