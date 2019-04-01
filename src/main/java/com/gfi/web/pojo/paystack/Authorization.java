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
public class Authorization implements Serializable {

	private static final long serialVersionUID = -8368595438209705664L;

    private String authorization_code;

    private String country_code;

    private String card_type;

    private String last4;

    private String exp_month;

    private String exp_year;

    private String bank;

    private String channel;

    private boolean reusable;
}
