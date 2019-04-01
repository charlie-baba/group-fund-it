/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import com.bis.gfi.enums.MemberStatus;

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
public class GroupMemberDto implements Serializable { 
	
	private static final long serialVersionUID = 5403680640389608827L;

	private MemberStatus status;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String picture;
	
	private String aboutMe;
}
