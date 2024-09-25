package com.mercurio.lms.rest.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

public class UploadResponseBuilder {
	
	public static String getFileName(HttpServletRequest request) {
		return request.getHeader("fileName");
	}
	
	/**
	 * Gera um status 200
	 * 
	 * @param entity
	 * @return
	 */
	public Response build(Object entity) {
		return Response.status(200).entity(entity).build();
	}
	
	/**
	 * Escreve o input stream do request em um arquivo
	 * 
	 * @param request
	 * @param folder
	 * @return
	 * @throws IOException
	 */
	public UploadResponseBuilder writeToFile(HttpServletRequest request, String folder) throws IOException {
		String fileName = getFileName(request);
		
		if (!folder.endsWith("/")) {
			folder += "/";
		}
		
		String uploadedFileLocation = folder+fileName;
		
		// save it
		OutputStream outputStream = new FileOutputStream(new File(uploadedFileLocation));
		try {
			
			writeToStream(request, outputStream);
			
		} finally {
			outputStream.close();
		}
		
		return this;
	}
	
	/**
	 * Escreve o input stream do request direto em um outputstream
	 * 
	 * @param request
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	public UploadResponseBuilder writeToStream(HttpServletRequest request, OutputStream outputStream) throws IOException {
		int read = 0;
		byte[] bytes = new byte[1024];
		
		while ((read = request.getInputStream().read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
		outputStream.flush();
		
		return this;
	}
	
}
