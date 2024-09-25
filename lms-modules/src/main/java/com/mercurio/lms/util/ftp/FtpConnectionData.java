package com.mercurio.lms.util.ftp;

import com.mercurio.lms.util.ftp.Ftp.FtpFlavor;

/**
 * 
 * @deprecated Favor contatar a arquitetura para avaliar outra solução.
 * 
 */
@Deprecated
public class FtpConnectionData {
  private String connectionName;
  private FtpFlavor type;
  private String hostname;
  private Integer port;
  private String username;
  private String password;
  private String remoteDir;
  
  private String privateKeyLocation;
  
  private Boolean passive = Boolean.FALSE;
  private Long timeout;


  public FtpConnectionData(final FtpFlavor type, final String hostname, final Integer port,
      final String username, final String password) {
    this.type = type;
    this.hostname = hostname;
    this.port = port;
    this.username = username;
    this.password = password;
  }


  public String getConnectionName() {
    return connectionName;
  }

  public void setConnectionName(final String connectionName) {
    this.connectionName = connectionName;
  }

  public FtpFlavor getType() {
    return type;
  }

  public void setType(final FtpFlavor type) {
    this.type = type;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(final String hostname) {
    this.hostname = hostname;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(final Integer port) {
    this.port = port;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getRemoteDir() {
    return remoteDir;
  }

  public void setRemoteDir(final String remoteDir) {
    this.remoteDir = remoteDir;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(connectionName).append(':');
    sb.append(getType().name()).append("://");
    sb.append(username).append(':').append(password).append('@');
    sb.append(hostname).append(':').append(port).append('/');
    sb.append(remoteDir);

    return sb.toString();
  }

    public String getPrivateKeyLocation() {
        return privateKeyLocation;
    }
    
    public void setPrivateKeyLocation(String privateKeyLocation) {
        this.privateKeyLocation = privateKeyLocation;
    }

	public Boolean getPassive() {
		return passive;
	}

	public void setPassive(Boolean passive) {
		this.passive = passive;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}   
}
