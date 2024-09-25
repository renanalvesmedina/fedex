package com.mercurio.lms.util.ftp;

public class FtpExceptionFactory {
  public static FtpException createNotConnectedExcetption() {
    return new FtpException(
        "Unable to execute operation because FTP / FTPS server is disconnected.");
  }

  public static FtpException createChangeWorkingDirectoryExcetption() {
    return new FtpException("Unable to change working directory.");
  }

  public static FtpException createServerRefusedException(final int replyCode) {
    return new FtpException("Connection refused by FTP / FTPS server with reply code " + replyCode
        + ".");
  }

  public static FtpException createLogonException() {
    return new FtpException("Não conseguiu logar no servidor FTP / FTPS.");
  }

  public static FtpException createSendFileException(final String remoteScr, final Throwable cause) {
    return new FtpException(
        "Não conseguiu enviar o arquivo para o servidor FTP / FTPS. Cliente. Arquivo: " + remoteScr,
        cause);
  }

  public static FtpException createListFilesException(final Throwable cause) {
    return new FtpException("Unable to list files on FTP / FTPS server.", cause);
  }

  public static FtpException createConnectionException(final String host, final Integer port,
      final String user, final String password, final Throwable cause) {
    return new FtpException("Unable to connect on FTP / FTPS server " + host + ":" + port
        + " using credentials " + user + "@" + password + ".", cause);
  }

  public static FtpException createDisconnectException(final Throwable cause) {
    return new FtpException("Unable to disconnect from FTP / FTPS server.", cause);
  }

  public static FtpException createNullFtpSClientException() {
    return new FtpException("Unale to connect on FTP / FTPS server because client is null.");
  }

  public static FtpException createFtpSClientException(final Throwable cause) {
    return new FtpException("Unable to create client for FTP / FTPS connection.", cause);
  }

  public static FtpException createDownloadException(final String fileName) {
    return new FtpException("Unable to download file " + fileName + ".");
  }

  public static FtpException createDownloadException(final String fileName, final Throwable cause) {
    return new FtpException("Unable to download file " + fileName + ".", cause);
  }

  public static FtpException createDeleteException(final String fileName, final Throwable cause) {
    return new FtpException("Unable to delete file " + fileName + ".", cause);
  }

  public static FtpException createDeleteException(final String fileName) {
    return new FtpException("Unable to delete file " + fileName + ".");
  }

  public static FtpException createRenameException(final String srcFileName,
      final String dstFileName, final Throwable cause) {
    return new FtpException("Unable to rename file " + srcFileName + " to " + dstFileName + ".",
        cause);
  }

  public static FtpException createRenameException(final String srcFileName,
      final String dstFileName) {
    return new FtpException("Unable to rename file " + srcFileName + " to " + dstFileName + ".");
  }

  public static FtpException createSSLSocketException(final Throwable cause) {
    return new FtpException("Cannot create SSL socket: " + cause);
  }
}
