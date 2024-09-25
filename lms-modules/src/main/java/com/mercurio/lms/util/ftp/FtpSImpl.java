package com.mercurio.lms.util.ftp;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

/**
 * 
 * @deprecated Favor contatar a arquitetura para avaliar outra solução.
 * 
 */
@Deprecated
public class FtpSImpl extends FtpImpl {
  public static final String SSL = "SSL";
  public static final String TSL = "TLS";

  public FtpSImpl(final boolean isImplicit, final String secProtocol) {
    ftpClient = new FTPSClient(secProtocol, isImplicit);
  }

  public FtpSImpl(final String secProtocol) {
    ftpClient = new FTPSClient(secProtocol);
  }

  @Override
  public void connect(final FtpConnectionData connectionData) throws FtpException {
    if (LOGGER.isDebugEnabled()) {
      ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.err),
          true));
    }
    try {
      final String server = connectionData.getHostname();
      final Integer port = connectionData.getPort();
      if (connectionData.getTimeout() != null && connectionData.getTimeout() > 0) {
        ftpClient.setConnectTimeout(connectionData.getTimeout().intValue());
      }
      ftpClient.connect(connectionData.getHostname(), connectionData.getPort());
      // After connection attempt, you should check the reply code to verify
      // success.
      final int reply = ftpClient.getReplyCode();

      if (!FTPReply.isPositiveCompletion(reply)) {
        ftpClient.disconnect();
        LOGGER.error("FTP server refused connection.");
        throw FtpExceptionFactory.createConnectionException(server, port,
            connectionData.getUsername(), connectionData.getPassword(), null);
      }
      LOGGER.info("Connected to " + server + " on " + (port > 0 ? port : ftpClient.getDefaultPort()));

      if (!ftpClient.login(connectionData.getUsername(), connectionData.getPassword())) {
        ftpClient.logout();
        throw FtpExceptionFactory.createLogonException();
      }

      ftpClient.enterLocalPassiveMode();

      LOGGER.info("Remote system is " + ftpClient.getSystemType());
      LOGGER.debug("FTP is connected " + ftpClient.isConnected());

    } catch (final IOException e) {
      LOGGER.error(e.getMessage());
      throw new FtpException(e);
    }
  }
}
