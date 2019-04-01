/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;
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
public class RemoveFromGroupRequest implements Serializable {

	private static final long serialVersionUID = -5677248262580615408L;

	private String email;
	
	private List<Long> groupId;
	
}
