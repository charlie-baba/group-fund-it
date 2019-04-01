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
public class ProjectDto implements Serializable {

	private static final long serialVersionUID = -9204493737979567262L;

	private Long id;
	
	private String name;
	
	private String createdBy;
	
	private Date createDate;
	
	private boolean active;
	
	private String description;
	
	private String category;
	
	private Date startDate;
	
	private Date endDate;
	
	private String targetAmount;
	
	private boolean privateProject;
	
	private boolean paidOut;

	private String imageUrls;
	
	private String base64Image;
	
}
