package com.mercurio.lms.util;

/**
 * 
 * @author Julio Cesar Fernandes Corrêa
 * 10/01/2006
 */
public final class EqualsHelper {

	public static boolean equals(Object x, Object y) {
		return x==y || ( x!=null && y!=null && x.equals(y) );
	}
	
	private EqualsHelper() {}

}

