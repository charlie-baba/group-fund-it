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
public class GroupRequest implements Serializable {

	private static final long serialVersionUID = -4260960996291011124L;

	private Long groupId;
	
}
