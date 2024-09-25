package com.mercurio.lms.util;

import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtils {
    
    private static final String CRIPTOGRAFIA_SERVICE_ADDRESS = "http://criptografiaservice.tntbrasil.com.br";
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtils.class);

	public static String getEncryptString(String input){
        String result = null;
        StringBuilder inputURL = new StringBuilder();
        inputURL.append(CRIPTOGRAFIA_SERVICE_ADDRESS + "/criptografiaservice/encrypt?obj=");
        inputURL.append(input);
            
        result = getStringByCriptografiaService(inputURL);
        return result;
    }
	
	public static String getDecryptString(String input){
        String result = null;
        StringBuilder inputURL = new StringBuilder();
        inputURL.append(CRIPTOGRAFIA_SERVICE_ADDRESS + "/criptografiaservice/decrypt?obj=");
        inputURL.append(input);
            
        result = getStringByCriptografiaService(inputURL);
        return result;
    }

    private static String getStringByCriptografiaService(StringBuilder inputURL) {
        String result = null;
        try{
            URL url = new URL(inputURL.toString());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        
            StringWriter writer = new StringWriter();
            IOUtils.copy(connection.getInputStream(), writer, "UTF-8");
            result = writer.toString();
            connection.disconnect();
        } catch (Exception e) {
            LOGGER.error("Error while encrypting: ", e);
        }
        return result;
    }
}
