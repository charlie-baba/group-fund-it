/**
 * 
 */
package com.gfi.web.util;

/**
 * @author Obi
 *
 */
public class Utility {

	public static synchronized String getRequestId() {
		return String.valueOf(System.currentTimeMillis());
	}
	
}
