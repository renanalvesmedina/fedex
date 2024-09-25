package com.mercurio.lms.util.ftp;

import java.util.Calendar;

public class FtpFile {
  private String name;
  private long size;
  private Calendar timestamp;
  private int type;
  private Boolean isFile;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public long getSize() {
    return size;
  }

  public void setSize(final long size) {
    this.size = size;
  }

  public Calendar getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(final Calendar timestamp) {
    this.timestamp = timestamp;
  }

  public int getType() {
    return type;
  }

  public void setType(final int type) {
    this.type = type;
  }

  public Boolean isFile() {
    return isFile;
  }

  public void setFile(final Boolean isFile) {
    this.isFile = isFile;
  }

}
