package com.mercurio.lms.util.ftp;

import java.io.File;
import java.util.List;

public interface Ftp {
public enum FtpFlavor {
    FTP, SFTP, FTPS,FTPES_TLS, FTPES,
    /**
     * SFTP utilizando arquivos encriptados de chave publica e privada para autenticação
     */
    SFTP_RSA;

    public static boolean haveFlavorWithName(final String flavorName) {
      try {
        valueOf(flavorName);
        return true;
      } catch (final IllegalArgumentException eill) {
        return false;
      }
    }
  }

  /**
   * Connect and login
   *
   * @param connectionData
   * @throws FtpException
   */
  public void connect(final FtpConnectionData connectionData) throws FtpException;

  public boolean checkDirectoryExists(final String remoteDir) throws FtpException;

  public void changeWorkingDirectory(final String remoteDir) throws FtpException;

  /**
   * Sempre retorna true. TODO mudar a assinatura para retorno void.
   *
   * @param remoteFileName
   * @param localFileName
   * @param fileType
   * @return
   * @throws FtpException
   */
  public boolean downloadFile(final String remoteFileName, final String localFileName,
      final int fileType) throws FtpException;

  public byte[] downloadFileContent(final String remoteFileName, final int fileType)
      throws FtpException;

  public boolean sendFile(final File file, final String remoteFileName, final int fileType)
      throws FtpException;

  public boolean sendFile(final File file, final String remoteFileName, final int fileType,
      final boolean passiveMode) throws FtpException;

  public boolean sendFile(final File file, final int fileType) throws FtpException;

  public void deleteFile(final String remoteFileName) throws FtpException;

  /**
   * As implementações nunca retornam false, jogam FtpException, que é o certo. TODO mudar a
   * assinatura para void.
   *
   * @param remoteSrcFileName
   * @param remoteDstFileName
   * @return true;
   * @throws FtpException
   */
  public boolean rename(final String remoteSrcFileName, final String remoteDstFileName)
      throws FtpException;

  public List<FtpFile> listFTPFile() throws FtpException;

  public List<File> listFiles() throws FtpException;

  public List<File> listFiles(final String filter) throws FtpException;

  public void disconnect() throws FtpException;

  public void enterLocalPassiveMode();
  


}
