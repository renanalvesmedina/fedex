package com.mercurio.lms.edi.util;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FtpConnection {
	private Logger log = LogManager.getLogger(this.getClass());
	private FTPClient ftp;
	private Boolean isConnected = false;
	
	public FtpConnection() {				
	}			
		
	public FTPClient getFTPClient() {
		return ftp;
	}
	
	public boolean moveFile(String fileName, String newFolder, String newFileName) throws IOException {					
		if(isConnected) {			
			ftp.makeDirectory(newFolder);			
			ftp.deleteFile(newFolder + newFileName);
			return ftp.rename(fileName, newFolder + newFileName);
		} 
		
		return false;		
	}
	
	public void storeFile(File arquivo) throws FileNotFoundException, IOException {		
		ftp.storeFile(arquivo.getName(), new FileInputStream(arquivo));				
	}
	
	public void storeFile(FTPFile arquivo) throws FileNotFoundException, IOException {		
		ftp.storeFile(arquivo.getName(), new FileInputStream(arquivo.getName()));				
	}

	public void storeFile(FTPFile arquivo, InputStream source) throws FileNotFoundException, IOException {		
		ftp.storeFile(arquivo.getName(), source);				
	}
	
	public void storeFile(String fileName, InputStream arquivo) throws FileNotFoundException, IOException {		
		ftp.storeFile(fileName, arquivo);				
	}

	public void deleteFile(String fileName) throws IOException {
		ftp.deleteFile(fileName);
	}
	
	@Deprecated
	public File[] listFiles(FileFilter filter) throws IOException {		
		if(isConnected) {
			FTPListParseEngine engine = ftp.initiateListParsing();
			FTPFile[] fileList = engine.getFiles();
			List<File> files = new ArrayList<File>();		
			for(FTPFile arquivo : fileList) {				
				if(arquivo.isFile()) {
					File novoArquivo =  this.getFile(arquivo.getName());				
					if ((filter == null) || filter.accept(novoArquivo)) {
						files.add(novoArquivo);						
					}
				}
			}			
			return files.toArray(new File[files.size()]);
		} else {
			return null;
		}			
	}
	
	public FTPFile[] listFTPFiles(FTPFileFilter filter) throws IOException {		
		if(isConnected) {
			FTPListParseEngine engine = ftp.initiateListParsing();
			FTPFile[] fileList = engine.getFiles();
			List<FTPFile> files = new ArrayList<FTPFile>();		
			for(FTPFile arquivo : fileList) {				
				if(arquivo.isFile()) {
					if ((filter == null) || filter.accept(arquivo.getName())) {
						files.add(arquivo);						
					}
				}
			}			
			return files.toArray(new FTPFile[files.size()]);
		} else {
			return null;
		}		
	}
	
	public File getFile(String fileName) throws IOException  {				
		if(isConnected) {
			File f = new File(fileName);
			OutputStream os = new FileOutputStream(f);
			ftp.retrieveFile(fileName, os);
			os.flush();
			os.close();
			return f;							
		} 
		
		return null;
	}
	
	public boolean connect(String host, String folder, String user, String password, String clientOS) {			
		ftp = new FTPClient();				
		ftp.configure(new FTPClientConfig(clientOS));			
		try {
			ftp.connect(host);
		} catch (Exception e) {	
			log.error(e);			
			return false;
		}
				
		if( FTPReply.isPositiveCompletion(ftp.getReplyCode())) {  
			boolean isLogged = false;
			try {
				isLogged = ftp.login( user, password );
			} catch (IOException e) {				
				this.disconnect();
				log.error(e);
				return false;
			}				
			if(isLogged) {													
				try {						
					ftp.changeWorkingDirectory(folder);
				} catch (IOException e) {
					this.disconnect();
					log.error(e);
					return false;
				}									
				ftp.enterLocalPassiveMode();
				isConnected = true;
				return true;						
			} 
		}
		
		this.disconnect();
		return false;
	}
	
	public void disconnect() {		
		if (ftp != null) {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (Exception e) {
				log.error(e);
			}			
			ftp = null;
		}
		isConnected = false;
	}
}
