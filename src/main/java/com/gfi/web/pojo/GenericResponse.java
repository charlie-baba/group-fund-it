/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import com.gfi.web.enums.ResponseCode;

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
public class GenericResponse implements Serializable {

	private static final long serialVersionUID = 7631698547742104401L;

	private String code;
	
	private String description;
	
	public void assignResponseCode(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.description = responseCode.getDescription();
    }
	
}
