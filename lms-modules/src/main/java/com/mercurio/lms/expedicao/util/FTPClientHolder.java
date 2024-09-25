package com.mercurio.lms.expedicao.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPClientHolder {
	
	private final FTPClient ftp;
	
	private final String host;
	
	private final String userName;
	
	private final String password;
	
	private final String initialPath;

	public FTPClientHolder(String host, String userName, String password, String initialPath, String systemKey) {
		super();
		this.host = host;
		this.userName = userName;
		this.password = password;
		ftp = new FTPClient();
		FTPClientConfig conf = new FTPClientConfig(systemKey);
		ftp.configure(conf);
		this.initialPath = initialPath;
		
	}
	
	public void connect() throws SocketException, IOException {
		
		ftp.connect(host);
		checkReply("FTP server refused connection.");
		
		ftp.login(userName, password);
		
		checkReply("FTP server refused login.");
		
		changeWorkingDirectory(initialPath);

	}

	public void checkReply(String message) throws IOException {
		String replyString = ftp.getReplyString();
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			throw new IOException("Erro na conexao FTP (" + message + ")  "
					+ reply + " - " + replyString);
		}
	}

	public boolean changeWorkingDirectory(String path) throws IOException {
		try {
			boolean result = false;
			result = ftp.changeWorkingDirectory(path);
			
			int reply = ftp.getReplyCode();
			if (FTPReply.isPositiveCompletion(reply)) {
				result = true;
			}
			
			return result;
		} catch (Throwable e) {
			reconnect();
			return changeWorkingDirectory(path);
		}
	}
	

	public boolean changeToParentDirectory() throws IOException {
		try {
			return ftp.changeToParentDirectory();
		} catch (Throwable e) {
			reconnect();
			return changeToParentDirectory();
		}
	}

	public FTPFile[] listFiles(String pathName) throws IOException {
		FTPFile[] listFiles = ftp.listFiles(pathName);
		checkReply("FTP server refused listing names.");
		return listFiles;
	}

	public void renameFile(String from, String to) throws IOException {
		try {
			ftp.rename(from, to);
		} catch (Throwable e) {
			reconnect();
			renameFile(from, to);
		}
		checkReply("FTP could not rename file");
	}

	private void reconnect() throws IOException, SocketException {
		disconnect();
		connect();
	}

	public InputStream retrieveFileStream(String name) throws IOException {
		return ftp.retrieveFileStream(name);
	}
	
	public OutputStream storeFileStream(String name) throws IOException {
		return ftp.storeFileStream(name);
	}

	public void completePendingCommand() throws IOException {
		ftp.completePendingCommand();
	}

	public String printWorkingDirectory() throws IOException {
		try {
			return ftp.printWorkingDirectory();
		} catch (Throwable e) {
			reconnect();
			return printWorkingDirectory();
		}
	}

	public void makeDirectory(String dirName) throws IOException {
		try {
			ftp.makeDirectory(dirName);
		} catch (Throwable e) {
			reconnect();
			makeDirectory(dirName);
		}
	}
	
	public void disconnect() throws IOException {
		if (ftp.isConnected()) {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException ioe) {
				// do nothing
			}
		}
	}

}
