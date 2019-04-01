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
public class BooleanRequest implements Serializable {

	private static final long serialVersionUID = 4228167214628646403L;

	private boolean enable;
	
}
