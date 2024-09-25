package com.mercurio.lms.util.ftp;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

public class FtpesImpl implements Ftp {
	
  private static final Logger LOGGER = LogManager.getLogger(FtpesImpl.class);

  FTPClient ftpClient = null;

  protected FTPClient getFtpClient() {
    if (ftpClient == null) {
      ftpClient = new FTPClient();
    }
    return ftpClient;
  }

  @Override
  public void connect(final FtpConnectionData connectionData) throws FtpException {

    if (connectionData.getTimeout() != null && connectionData.getTimeout() > 0) {
      System.setProperty("ftp4j.activeDataTransfer.acceptTimeout", connectionData.getTimeout()
          .toString());
    }

    System.setProperty("java.net.preferIPv4Stack", "true");
    
    final SSLSocketFactory sslSocketFactory = getSSLContext().getSocketFactory();
    getFtpClient().setSSLSocketFactory(sslSocketFactory);

    if (connectionData.getType() != null) {
      if (connectionData.getType().equals(FtpFlavor.FTPES)) {
        getFtpClient().setSecurity(FTPClient.SECURITY_FTPES);
      } else if (connectionData.getType().equals(FtpFlavor.FTPS)) {
        getFtpClient().setSecurity(FTPClient.SECURITY_FTPS);
      }
    }

    try {
      getFtpClient().connect(connectionData.getHostname(), connectionData.getPort());

      if (!getFtpClient().isConnected()) { throw FtpExceptionFactory.createLogonException(); }

      getFtpClient().login(connectionData.getUsername(), connectionData.getPassword());

    } catch (final Exception e) {
      disconnect();
      throw FtpExceptionFactory.createConnectionException(connectionData.getHostname(),
          connectionData.getPort(), connectionData.getUsername(), connectionData.getPassword(), e);
    }
  }

  private final TrustManager[] trustManager = new TrustManager[] {new X509TrustManager() {
    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    @Override
    public void checkClientTrusted(final X509Certificate[] certs, final String authType) {}

    @Override
    public void checkServerTrusted(final X509Certificate[] certs, final String authType) {}
  }};

  private SSLContext getSSLContext() throws FtpException {
    SSLContext sslContext = null;
    try {
      sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustManager, new SecureRandom());
    } catch (final Exception e) {
      throw FtpExceptionFactory.createSSLSocketException(e);
    }
    return sslContext;
  }

  @Override
  public boolean sendFile(final File file, final String remoteFileName, final int fileType,
      final boolean passiveMode) throws FtpException {
    boolean send = false;
    testConnection();
    try {
      getFtpClient().upload(file);
      send = true;
    } catch (final IOException e) {
      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
    } catch (final Exception e) {
      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
    }
    return send;
  }

  @Override
  public boolean sendFile(final File file, final String remoteFileName, final int fileType)
      throws FtpException {
    boolean send = false;
    testConnection();
    try {
      getFtpClient().upload(file);
      send = true;
    } catch (final IOException e) {
      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
    } catch (final Exception e) {
      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
    }
    return send;
  }

  @Override
  public boolean sendFile(final File file, final int fileType) throws FtpException {
    return sendFile(file, fileType);
  }

  @Override
  public void changeWorkingDirectory(final String remoteDir) throws FtpException {
    testConnection();
    if (remoteDir != null) {
      try {
        getFtpClient().changeDirectory(remoteDir);
      } catch (final Exception e) {
        throw FtpExceptionFactory.createChangeWorkingDirectoryExcetption();
      }
    }
  }

  @Override
  public boolean downloadFile(final String remoteFileName, final String localFileName,
      final int fileType) throws FtpException {
    try {
      getFtpClient().download(remoteFileName, new File(localFileName));
    } catch (final Exception e) {
      throw new FtpException(e.getMessage(), e);
    }
    return false;
  }

  @Override
  public byte[] downloadFileContent(final String remoteFileName, final int fileType)
      throws FtpException {
    throw new FtpException("Not Implemented.");
  }

  @Override
  public void deleteFile(final String remoteFileName) throws FtpException {
    try {
      getFtpClient().deleteFile(remoteFileName);
    } catch (final Exception e) {
      throw new FtpException(e);
    }
  }

  @Override
  public boolean rename(final String remoteSrcFileName, final String remoteDstFileName)
      throws FtpException {
    boolean renamed = false;
    try {
      getFtpClient().rename(remoteSrcFileName, remoteDstFileName);
      renamed = true;
    } catch (final Exception e) {
      throw new FtpException(e);
    }
    return renamed;
  }

  @Override
  public void disconnect() throws FtpException {
    if (getFtpClient() != null && getFtpClient().isConnected()) {
      try {
        getFtpClient().disconnect(true);
      } catch (final Exception e) {
        throw new FtpException(e);
      }
    }
  }

  private void testConnection() throws FtpException {
    if (getFtpClient() == null || !getFtpClient().isConnected()
        || !getFtpClient().isAuthenticated()) { throw FtpExceptionFactory
        .createNotConnectedExcetption(); }
  }

  @Override
  public boolean checkDirectoryExists(final String remoteDir) throws FtpException {
    // boolean directoryExist = true;
    // return directoryExist;
    return false;
  }

  @Override
  public List<File> listFiles() throws FtpException {
    final List<File> listFiles = new ArrayList<File>();
    try {
      final FTPFile[] ftpFiles = getFtpClient().list();
      for (final FTPFile ftpFile : ftpFiles) {
        final File file = new File(ftpFile.getName());
        listFiles.add(file);
      }
    } catch (final Exception e) {
      throw FtpExceptionFactory.createListFilesException(e);
    }
    return listFiles;
  }

  @Override
  public List<File> listFiles(final String extension) throws FtpException {
    final String filterEnds = setExtension(extension);

    final List<File> listFiles = new ArrayList<File>();
    try {
      final FTPFile[] ftpFiles = getFtpClient().list();
      for (final FTPFile ftpFile : ftpFiles) {
        final String fn = ftpFile.getName().toLowerCase();
        if (ftpFile.getType() == FTPFile.TYPE_FILE && fn.endsWith(filterEnds)) {
          final File file = new File(ftpFile.getName());
          listFiles.add(file);
        }
      }
    } catch (final Exception e) {
      throw FtpExceptionFactory.createListFilesException(e);
    }
    return listFiles;
  }

  private String setExtension(String extension) {
    if (extension == null) {
      return "";
    } else {
      return extension.trim().toLowerCase();
    }
  }

  @Override
  public List<FtpFile> listFTPFile() throws FtpException {
    return null;
  }

  @Override
  public void enterLocalPassiveMode() {
    LOGGER.debug("Cliente FTPES não suporta passive mode.");
  }

}
