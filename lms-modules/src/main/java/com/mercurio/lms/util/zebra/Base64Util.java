package com.mercurio.lms.util.zebra;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {
	private static BASE64Decoder decoder = new BASE64Decoder();
	private static BASE64Encoder encoder = new BASE64Encoder();
	
	public static String encode(byte[] data) {
		return encoder.encode(data);
	}
	
	public static byte[] decode(String data) throws IOException {
		return decoder.decodeBuffer(data);		
	}
}
