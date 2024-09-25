package com.mercurio.lms.util.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

/**
 * 
 * @deprecated Favor contatar a arquitetura para avaliar outra soluÁ„o.
 * 
 */
@Deprecated
public class SFtpImpl implements Ftp {
	

  private static Logger LOGGER = LogManager.getLogger(SFtpImpl.class);
	
  private ChannelSftp sftpChannel;
  private Session session;

  public ChannelSftp getSftpChannel() {
	  return sftpChannel;
  }
  @Override
  public void connect(final FtpConnectionData connectionData) throws FtpException {
    try {
      final JSch jsch = new JSch();
      session =
          jsch.getSession(connectionData.getUsername(), connectionData.getHostname(),
              connectionData.getPort());
      if (connectionData.getTimeout() != null && connectionData.getTimeout() > 0) {
        session.setTimeout(connectionData.getTimeout().intValue());
      }
      session.setPassword(connectionData.getPassword());
      session.setUserInfo(createUserInfo());
      session.setConfig("StrictHostKeyChecking", "no");
      session.setConfig("PreferredAuthentications", 
              "publickey,keyboard-interactive,password");
      session.connect();

      final Channel channel = session.openChannel("sftp");
      channel.connect();

      sftpChannel = (ChannelSftp) channel;
    } catch (final JSchException e) {
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
        sftpChannel.cd(remoteDir);
      } catch (final SftpException e) {
        throw FtpExceptionFactory.createChangeWorkingDirectoryExcetption();
      }
    }
  }

  @Override
  public boolean downloadFile(final String remoteFileName, final String localFileName,
      final int fileType) throws FtpException {
    boolean send = false;
    BufferedInputStream bufferedInput = null;
    BufferedOutputStream bufferedOutput = null;
    final byte[] buffer = new byte[1024];
    try {
      bufferedInput = new BufferedInputStream(sftpChannel.get(remoteFileName));
      final File newFile = new File(localFileName);
      bufferedOutput = new BufferedOutputStream(new FileOutputStream(newFile));
      int readCount;
      while ((readCount = bufferedInput.read(buffer)) > 0) {
        bufferedOutput.write(buffer, 0, readCount);
      }
      bufferedOutput.flush();
      bufferedOutput.close();
      send = true;
    } catch (final FileNotFoundException e) {
      throw FtpExceptionFactory.createDownloadException(remoteFileName, e);
    } catch (final IOException e) {
      throw FtpExceptionFactory.createDownloadException(remoteFileName, e);
    } catch (final SftpException e) {
      throw FtpExceptionFactory.createDownloadException(remoteFileName, e);
    } finally {
      closeQuietly(bufferedInput);
      closeQuietly(bufferedOutput);
    }
    return send;
  }

  private void closeQuietly(Closeable c) {
    try {
      c.close();
    } catch (Exception e) {
    	LOGGER.error(e);
    }
  }

  @Override
  public byte[] downloadFileContent(final String remoteFileName, final int fileType)
      throws FtpException {
    return null;
  }
  
  public InputStream getFileInputStream(final String remoteFileName) throws FtpException {
    try {
		return sftpChannel.get(remoteFileName);
	} catch (SftpException e) {
		throw FtpExceptionFactory.createDownloadException(remoteFileName, e);
	}
  }

	@Override
	public boolean sendFile(final File file, final String remoteFileName, final int fileType,
			final boolean passiveMode) throws FtpException  {
	  FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (final FileNotFoundException e) {
		      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
		}
	  return this.sendFile(inputStream, remoteFileName, fileType, passiveMode);
	}
  
  public boolean sendFile(InputStream is, final String remoteFileName, final int fileType,
	      final boolean passiveMode) throws FtpException {
	    boolean send = false;
	    try {	      
	      sftpChannel.put(is, remoteFileName, ChannelSftp.OVERWRITE);
	      send = true;
	      is.close();
	    } catch (final SftpException e) {
	      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
	    } catch (final IOException e) {
	      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
	    } finally {
	      IOUtils.closeQuietly(is);
	    }
	    return send;
	  }

  @Override
  public boolean sendFile(final File file, final String remoteFileName, final int fileType)
      throws FtpException {
    boolean send = false;
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      sftpChannel.put(inputStream, remoteFileName, ChannelSftp.OVERWRITE);
      inputStream.close();
      send = true;
    } catch (final SftpException e) {
      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
    } catch (final FileNotFoundException e) {
      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
    } catch (final IOException e) {
      throw FtpExceptionFactory.createSendFileException(remoteFileName, e);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
    return send;
  }

  @Override
  public boolean sendFile(final File file, final int fileType) throws FtpException {
    return sendFile(file, fileType);
  }

  @Override
  public void deleteFile(final String remoteFileName) throws FtpException {
    try {
      sftpChannel.rm(remoteFileName);
    } catch (final SftpException e) {
      throw FtpExceptionFactory.createDeleteException(remoteFileName, e);
    }
  }

  @Override
  public boolean rename(final String remoteSrcFileName, final String remoteDstFileName)
      throws FtpException {
    boolean renamed = false;
    try {
      sftpChannel.rename(remoteSrcFileName, remoteDstFileName);
      renamed = true;
    } catch (final SftpException e) {
      throw FtpExceptionFactory.createRenameException(remoteSrcFileName, remoteDstFileName, e);
    }
    return renamed;
  }

  @Override
  public List<File> listFiles() throws FtpException {
    return listFiles("");
  }

  private String setExtension(String extension) {
    if (extension == null) {
      return "";
    } else {
      return extension.trim().toLowerCase();
    }
  }


  @Override
  public List<File> listFiles(final String extension) throws FtpException {
    LOGGER.error("1");
    final List<File> listFiles = new ArrayList<File>();
    final String ext = setExtension(extension);

    try {
      LOGGER.error("2");
        
      @SuppressWarnings("unchecked")
      final Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*" + ext);
      LOGGER.error("3");
      if (list.size() > 0) {
        for (final ChannelSftp.LsEntry entry : list) {
          if (entry.getFilename() != null && !entry.getAttrs().isDir()) {
            final File file = new File(entry.getFilename());
            listFiles.add(file);
          }
        }
      }
    } catch (final SftpException e) {
      throw FtpExceptionFactory.createListFilesException(e.getCause());
    }
    return listFiles;
  }

  @Override
  public void disconnect() {
    if (sftpChannel != null && sftpChannel.isConnected()) {
      sftpChannel.disconnect();
    }
    if (sftpChannel != null && session.isConnected()) {
      session.disconnect();
    }
  }

  private void testConnection() throws FtpException {
    if (sftpChannel == null || !sftpChannel.isConnected()) {
    	throw FtpExceptionFactory.createNotConnectedExcetption();
  }
  }

  private UserInfo createUserInfo() {
    return new UserInfo() {
      @Override
      public void showMessage(final String arg0) {
    	  //n„o necessario
      }

      @Override
      public boolean promptYesNo(final String arg0) {
        return false;
      }

      @Override
      public boolean promptPassword(final String arg0) {
        return false;
      }

      @Override
      public boolean promptPassphrase(final String arg0) {
        return false;
      }

      @Override
      public String getPassword() {
        return null;
      }

      @Override
      public String getPassphrase() {
        return null;
      }
    };
  }

  @Override
  public boolean checkDirectoryExists(final String remoteDir) throws FtpException {
    boolean directoryExist = false;
    SftpATTRS attr = null;
    try {
      attr = sftpChannel.stat(remoteDir);
      directoryExist = attr.isDir();
    } catch (final SftpException e) {
      throw FtpExceptionFactory.createDisconnectException(e);
    }
    return directoryExist;
  }

  @Override
  public List<FtpFile> listFTPFile() throws FtpException {
    return null;
  }

  @Override
  public void enterLocalPassiveMode() {
    LOGGER.debug("Cliente FTPES n√£o suporta passive mode.");
  }

protected void setSftpChannel(ChannelSftp sftpChannel) {
    this.sftpChannel = sftpChannel;
}

protected void setSession(Session session) {
    this.session = session;
}

}
