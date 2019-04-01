/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;
import java.util.Date;

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
public class TransactionDto implements Serializable {

	private static final long serialVersionUID = -5794637700868771798L;

	private String email;
	
	private Double amount;
	
	private Date date;
	
}
