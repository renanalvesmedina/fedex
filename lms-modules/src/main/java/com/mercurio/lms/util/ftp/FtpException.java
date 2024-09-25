package com.mercurio.lms.util.ftp;

public class FtpException extends Exception {
  private static final long serialVersionUID = 1L;

  public FtpException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public FtpException(final Throwable cause) {
    super(cause);
  }

  public FtpException(final String message) {
    super(message);
  }
}
