package com.mercurio.lms.util.ftp;

import com.mercurio.lms.util.ftp.Ftp.FtpFlavor;

public final class FTPFactory {

  private static FTPFactory instance = null;

  public static FTPFactory getInstance() {
    if (instance == null) {
      instance = new FTPFactory();
    }
    return instance;
  }

  public Ftp getFtpImplementation(final FtpFlavor flavor) throws FtpException {
    Ftp ftpImplementation;
    if (FtpFlavor.FTP.equals(flavor)) {
      ftpImplementation = new FtpImpl();
    } else if (FtpFlavor.SFTP.equals(flavor)) {
      ftpImplementation = new SFtpImpl();
    } else if (FtpFlavor.FTPS.equals(flavor)) {
      ftpImplementation = new FtpSImpl(false, "SSL");
    } else if (FtpFlavor.FTPES_TLS.equals(flavor)) {
        ftpImplementation = new FtpSImpl(false, FtpSImpl.TSL);
    } else if (FtpFlavor.FTPES.equals(flavor)) {
      ftpImplementation = new FtpesImpl();
    } else if (FtpFlavor.SFTP_RSA .equals(flavor)){
      ftpImplementation = new SFftpRSAKeyImpl();  
    } else {
      // should never happen
      throw new FtpException("Ftp flavor " + flavor.name() + " unknown.");
    }
    return ftpImplementation;
  }

  private FTPFactory() {}

}
