/**
 * 
 */
package com.gfi.web.pojo.paystack;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class History implements Serializable {

	private static final long serialVersionUID = 4230830435479867393L;

	private String message;

	private int time;

	private String type;
	
}
