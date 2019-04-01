/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

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
public class PictureRequest implements Serializable {

	private static final long serialVersionUID = -7283312406879519625L;

	private MultipartFile profilePicture;
	
}
