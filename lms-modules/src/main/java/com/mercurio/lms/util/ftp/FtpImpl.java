package com.mercurio.lms.util.ftp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @deprecated Favor contatar a arquitetura para avaliar outra solução.
 * 
 */
@Deprecated
public class FtpImpl implements Ftp {
	
  protected static final Logger LOGGER = LogManager.getLogger(FtpImpl.class);
  
  FTPClient ftpClient = null;

  public FtpImpl() {
    ftpClient = new FTPClient();
  }

  @Override
  public void connect(final FtpConnectionData connectionData) throws FtpException {
    if (ftpClient == null) {
    	throw FtpExceptionFactory.createNullFtpSClientException(); 
    }

    try {
      if (connectionData.getTimeout() != null && connectionData.getTimeout() > 0) {
        ftpClient.setConnectTimeout(connectionData.getTimeout().intValue());
      }
      ftpClient.connect(connectionData.getHostname(), connectionData.getPort());

      if (connectionData.getPassive()) {
        ftpClient.enterLocalPassiveMode();
      }

      final int replyCode = ftpClient.getReplyCode();
      if (!FTPReply.isPositiveCompletion(replyCode)) {
    	  throw FtpExceptionFactory.createServerRefusedException(replyCode);
      }

      if (!ftpClient.login(connectionData.getUsername(), connectionData.getPassword())) {
    	  throw FtpExceptionFactory.createLogonException(); 
      }
    } catch (final IOException e) {
      disconnect();
      throw FtpExceptionFactory.createConnectionException(connectionData.getHostname(),
          connectionData.getPort(), connectionData.getUsername(), connectionData.getPassword(), e);
    }
  }

  @Override
  public void changeWorkingDirectory(final String remoteDir) throws FtpException {
    testConnection();
    if (remoteDir != null) {
      try {
        ftpClient.changeWorkingDirectory(remoteDir);
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) { 
        	throw new FtpException(ftpClient.getReplyString()); 
        }
      } catch (final IOException e) {
        throw FtpExceptionFactory.createChangeWorkingDirectoryExcetption();
      }
    }
  }

  @Override
  public boolean downloadFile(final String remoteFileName, final String localFileName,
      final int fileType) throws FtpException {
    boolean downloaded = false;
    testConnection();
    OutputStream output = null;
    try {
      output = new FileOutputStream(localFileName);
      ftpClient.setFileType(fileType);
      downloaded = ftpClient.retrieveFile(remoteFileName, output);
      output.close();
    } catch (final Exception e) {
      throw FtpExceptionFactory.createDownloadException(remoteFileName, e);
    } finally {
      IOUtils.closeQuietly(output);
    }
    return downloaded;
  }

  @Override
  public byte[] downloadFileContent(final String remoteFileName, final int fileType)
      throws FtpException {
    testConnection();

    InputStream inputStream = null;
    ByteArrayOutputStream bos = null;
    byte[] result = null;

    try {
      ftpClient.setFileType(fileType);
      ftpClient.enterLocalPassiveMode();

      inputStream = ftpClient.retrieveFileStream(remoteFileName);
      bos = new ByteArrayOutputStream();
      int next = inputStream.read();
      while (next > -1) {
        bos.write(next);
        next = inputStream.read();
      }

      bos.flush();
      result = bos.toByteArray();

      bos.close();
      inputStream.close();

      if (!ftpClient.completePendingCommand()) {
    	  throw FtpExceptionFactory.createDownloadException(remoteFileName); 
      }
    } catch (final Exception e) {
      throw FtpExceptionFactory.createDownloadException(remoteFileName, e);
    } finally {
      IOUtils.closeQuietly(bos);
      IOUtils.closeQuietly(inputStream);
    }

    return result;
  }

  @Override
  public boolean sendFile(final File file, final String remoteFileName, final int fileType)
      throws FtpException {
    boolean send = false;
    testConnection();
    InputStream inputStream = null;
    try {
      if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
        inputStream = new FileInputStream(file);
        ftpClient.setFileType(fileType);
        send = ftpClient.storeFile(remoteFileName, inputStream);
        inputStream.close();
      }
    } catch (final IOException e) {
      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
    return send;
  }

  @Override
  public boolean sendFile(final File file, final int fileType) throws FtpException {
    return sendFile(file, file.getName(), fileType);
  }

  @Override
  public boolean sendFile(final File file, final String remoteFileName, final int fileType,
      final boolean passiveMode) throws FtpException {
    boolean send = false;
    testConnection();
    InputStream inputStream = null;
    try {
      if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
        inputStream = new FileInputStream(file);
        ftpClient.setFileType(fileType);
        if (passiveMode) {
          ftpClient.enterLocalPassiveMode();
        }
        send = ftpClient.storeFile(remoteFileName, inputStream);
        inputStream.close();
      } else {
        throw new FtpException(ftpClient.getReplyString());
      }
    } catch (final IOException e) {
      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
    return send;
  }

  @Override
  public void deleteFile(final String remoteFileName) throws FtpException {
    testConnection();

    try {
      if (!ftpClient.deleteFile(remoteFileName)) {
    	  throw FtpExceptionFactory.createDeleteException(remoteFileName); 
      }
    } catch (final IOException e) {
      throw FtpExceptionFactory.createDeleteException(remoteFileName, e);
    }
  }

  @Override
  public boolean rename(final String remoteSrcFileName, final String remoteDstFileName)
      throws FtpException {
    testConnection();
    boolean renamed = false;
    try {
      if (!ftpClient.rename(remoteSrcFileName, remoteDstFileName)) {
    	  throw FtpExceptionFactory.createRenameException(remoteSrcFileName, remoteDstFileName); 
      }
      renamed = true;
    } catch (final IOException e) {
      throw FtpExceptionFactory.createRenameException(remoteSrcFileName, remoteDstFileName, e);
    }
    return renamed;
  }

  @Override
  public List<File> listFiles() throws FtpException {
    LOGGER.warn("Preparando listar arquivos");
    final List<File> listFTP = new ArrayList<File>();
    final List<FtpFile> listFTPFile = listFTPFile();
    for (final FtpFile ftpFile : listFTPFile) {
      if (ftpFile.isFile()) {
        final File file = new File(ftpFile.getName());
        listFTP.add(file);
      }
    }
    LOGGER.warn("Finalizado listar arquivos");
    return listFTP;
  }

  @Override
  public List<FtpFile> listFTPFile() throws FtpException {
    LOGGER.warn("Testando conexao");
    testConnection();
    LOGGER.warn("Testado conexao");
    final List<FtpFile> ftpFiles = new ArrayList<FtpFile>();
    try {
        LOGGER.warn("Listando arquivos");
      final FTPFile[] files = ftpClient.listFiles();
      LOGGER.warn(String.format("Listados %d arquivos",files.length));
      for (final FTPFile file : files) {
        final FtpFile ftpFile = new FtpFile();
        ftpFile.setName(file.getName());
        ftpFile.setSize(file.getSize());
        ftpFile.setTimestamp(file.getTimestamp());
        ftpFile.setType(file.getType());
        ftpFile.setFile(file.isFile());
        ftpFiles.add(ftpFile);
      }
      
    } catch (final IOException e) {
      throw FtpExceptionFactory.createListFilesException(e);
    }
    return ftpFiles;
  }

  private String setExtension(String extension) {
    if (extension == null) {
      return "";
    } else {
      return extension.trim().toLowerCase();
    }
  }

  @Override
  public List<File> listFiles(final String extensionFilter) throws FtpException {
    final List<File> files = new ArrayList<File>();
    final String ext = setExtension(extensionFilter);

    try {
        ftpClient.setConnectTimeout(15000);
      LOGGER.error(String.format("!!!!!!!!!!!!!!!Reading FTP files from '.'. timeout 15000 status %d",ftpClient.getConnectTimeout()));
      
      final FTPFile[] ftpFiles = ftpClient.listFiles(".");
      LOGGER.error(String.format("!!!!!!!Reading FTP files from '.', #%d.", ftpFiles.length));
      if (LOGGER.isDebugEnabled()) {
        for (final FTPFile l : ftpFiles) {
          LOGGER.debug(l.toFormattedString() + '=' + l.toFormattedString());
        }
      }
      for (final FTPFile ftpFile : ftpFiles) {
        if (ftpFile.isFile() && ftpFile.getName().trim().toLowerCase().endsWith(ext)) {
          final File file = new File(ftpFile.getName());
          files.add(file);
        }
      }
    } catch (final IOException e) {
      throw FtpExceptionFactory.createListFilesException(e);
    }
    return files;
  }

  public List<FtpFile> listFiles(final String remoteDir, final FTPFileFilter filter)
      throws FtpException {
    testConnection();
    final List<FtpFile> ftpFiles = new ArrayList<FtpFile>();
    try {
      final FTPFile[] files = ftpClient.listFiles(remoteDir, filter);
      for (final FTPFile file : files) {
        final FtpFile ftpFile = new FtpFile();
        ftpFile.setName(file.getName());
        ftpFile.setSize(file.getSize());
        ftpFile.setTimestamp(file.getTimestamp());
        ftpFile.setType(file.getType());
        ftpFile.setFile(file.isFile());
        ftpFiles.add(ftpFile);
      }
    } catch (final IOException e) {
      throw FtpExceptionFactory.createListFilesException(e);
    }
    return ftpFiles;
  }

  @Override
  public void disconnect() throws FtpException {
    if (ftpClient != null && ftpClient.isConnected()) {
      logout();

      try {
        ftpClient.disconnect();
      } catch (final IOException e) {
        throw FtpExceptionFactory.createDisconnectException(e);
      }
    }
  }

  private void logout() {
    try {
      ftpClient.logout();
    } catch (final IOException e1) {
    	LOGGER.error(e1);
    }
  }

  private void testConnection() throws FtpException {
    if (ftpClient == null || !ftpClient.isConnected()) {
    	throw FtpExceptionFactory.createNotConnectedExcetption(); 
  }
  }

  @Override
  public boolean checkDirectoryExists(final String remoteDir) throws FtpException {
    boolean directoryExist = true;
    try {
      ftpClient.changeWorkingDirectory(remoteDir);
    } catch (final IOException e) {
      throw FtpExceptionFactory.createDisconnectException(e);
    }
    final int returnCode = ftpClient.getReplyCode();
    if (returnCode == 550) {
      directoryExist = false;
    }
    return directoryExist;
  }

  @Override
  public void enterLocalPassiveMode() {
    this.ftpClient.enterLocalPassiveMode();
  }

}
