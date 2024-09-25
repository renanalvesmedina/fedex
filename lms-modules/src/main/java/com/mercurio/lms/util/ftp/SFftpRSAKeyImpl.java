package com.mercurio.lms.util.ftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Implementação do acesso à servidores SFTP utilizando arquivos de chave privada e publica para 
 * autenticação criptografada. Extende {@link SFtpImpl} apenas modificando a forma de conectar ao 
 * servidor.
 * 
 * É necessário apenas o local do arquivo de chave privada e o resto continua igual.
 * 
 * @author vagnerh
 *
 * @deprecated Favor contatar a arquitetura para avaliar outra solu��o.
 * 
 */
@Deprecated
public class SFftpRSAKeyImpl extends SFtpImpl{
    
    public SFftpRSAKeyImpl() {
     
    }
    
    @Override
    public void connect(FtpConnectionData connectionData) throws FtpException {
        if (connectionData == null || connectionData.getPrivateKeyLocation() == null){
            throw new IllegalArgumentException("PrivateKeyLocation must be informed for RSA authentication.");
        }
        JSch sch = new JSch();
        try {
            sch.addIdentity(connectionData.getPrivateKeyLocation(),connectionData.getPassword());
            
            sch.setConfig("StrictHostKeyChecking", "no");
            
            Session session = sch.getSession(connectionData.getUsername(),connectionData.getHostname(),connectionData.getPort());
            this.setSession(session);
            session.connect();
            Channel channel = session.openChannel("sftp");
            this.setSftpChannel((ChannelSftp) channel);
            channel.connect();
        } catch (JSchException e) {
            disconnect();
            throw FtpExceptionFactory.createConnectionException(connectionData.getHostname(),
                connectionData.getPort(), connectionData.getUsername(), connectionData.getPrivateKeyLocation(), e);
        }
    }
}
