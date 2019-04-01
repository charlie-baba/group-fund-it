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
public class ProjectDashboard implements Serializable {

	private static final long serialVersionUID = -2009256032671777926L;

	private Double targetAmt;
	
	private Double donatedAmt;
	
	private Double remainingAmt;
	
	private long donatedPercentage;
	
	private long remainingPercentage;
	
	List<TransactionDto> donations;
	
}
