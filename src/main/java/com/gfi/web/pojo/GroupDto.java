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
public class GroupDto implements Serializable {

	private static final long serialVersionUID = 2526027464600663164L;

	private Long id;
	
	private String name;
	
	private String createdBy;
	
	private Date createDate;
	
	private boolean active;
	
	private String description;
	
	private String base64Image;
	
	private boolean privateGroup;
	
	private boolean allowAnonymous;
	
	private boolean isAdmin;
	
}
