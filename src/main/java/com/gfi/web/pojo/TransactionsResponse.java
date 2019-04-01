/**
 * 
 */
package com.gfi.web.pojo;

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
public class TransactionsResponse extends GenericResponse {

	private static final long serialVersionUID = -8578353083448510882L;

	private List<TransactionDto> donations;
	
	private List<TransactionDto> payouts;
}
