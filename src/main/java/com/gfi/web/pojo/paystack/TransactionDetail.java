/**
 * 
 */
package com.gfi.web.pojo.paystack;

import java.io.Serializable;

import javax.xml.datatype.XMLGregorianCalendar;

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
public class TransactionDetail implements Serializable {

	private static final long serialVersionUID = -6334575660248150815L;

    private String plan;

    private String currency;

    private long amount;

    private XMLGregorianCalendar transactionDate;

    private String status;

    private String reference;

    private String domain;

    private Customer customer;

    private Authorization authorization;

    private String gateway_response;

    private String message;

    private String channel;

    private String ip_address;

    private Log log;

    private String fees;
}
