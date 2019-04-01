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
public class Customer implements Serializable {

	private static final long serialVersionUID = 2510273849827935418L;

	private long id;

    private String customer_code;

    private String first_name;

    private String last_name;

    private String email;
    
}
