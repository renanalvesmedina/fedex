package com.mercurio.lms.util;

public class ArrayUtils {

	/**
	 * Método que concatena dois arrays, retornando um array de length = (sizeOne + sizeTwo) 
	 * @param arrayOne Array um.
	 * @param sizeOne Seu length.
	 * @param arrayTwo Array dois.
	 * @param sizeTwo Seu length.
	 * @return Um array de [sizeOne + sizeTwo] concatenado.
	 * @author luisfco
	 */
	public static Object[] joinArrays (Object[] arrayOne, int sizeOne, Object[] arrayTwo, int sizeTwo) {
		
		Object[] result = new Object[sizeOne + sizeTwo];
		
    	System.arraycopy(arrayOne, 0, result, 0, sizeOne);
    	System.arraycopy(arrayTwo, 0, result, sizeOne, sizeTwo);
    	
    	return result;
	}
	
}
