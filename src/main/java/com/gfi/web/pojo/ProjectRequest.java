/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

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
public class ProjectRequest implements Serializable {

	private static final long serialVersionUID = -4565627222695458263L;

	private Long projectId;
	
}
