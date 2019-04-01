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
public class ChartPojo implements Serializable {
	
	private static final long serialVersionUID = 632495067306979096L;

	private String labels;
	
	private String data;

}
