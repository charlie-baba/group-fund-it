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
public class MetaData implements Serializable {

	private static final long serialVersionUID = 3506123092539101291L;

	private String job = "Project Admin";
	
}
