/**
 * 
 */
package com.gfi.web.pojo.paystack;

import java.io.Serializable;
import java.util.List;

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
public class Log implements Serializable {

	private static final long serialVersionUID = -5128089732439766124L;

    private int time_spent;

    private int attempts;

    private int errors;
    
    private boolean success;
    
    private boolean mobile;

    private List<History> history;
    
}
