/**
 * 
 */
package com.gfi.web.util;

import java.util.Random;

/**
 * @author Obi
 *
 */
public class PasswordGenerator {

    private static final String validChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&*()=+-/?";
	
	public static String generate(int n) {
	    StringBuilder sb = new StringBuilder();
	    int length = validChar.length();
	    Random rand = new Random(System.nanoTime());

	    for (int i= 0; i < n; i++) {
	        int k = rand.nextInt(length);
	        sb.append(validChar.charAt(k));
	    }
	    return sb.toString();
	}
	
}
