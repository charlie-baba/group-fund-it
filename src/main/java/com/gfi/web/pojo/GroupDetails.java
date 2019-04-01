/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import com.bis.gfi.entities.Group;

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
public class GroupDetails implements Serializable {

	private static final long serialVersionUID = 3734834013238438703L;

	private Group group;
	
	private long noOfMembers;
	
	private long noOfProjects;

}
