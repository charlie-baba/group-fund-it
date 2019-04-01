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
public class UserRequest implements Serializable {

	private static final long serialVersionUID = -7900607034957058504L;

	private Long userId;
	
}
