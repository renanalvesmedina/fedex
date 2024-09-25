package com.mercurio.lms.mww.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.mww.model.service.UpdaterUtilService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.mww.updaterUtilAction"
 */
public class UpdaterUtilAction {

	private UpdaterUtilService updaterUtilService;
	private ParametroGeralService parametroGeralService;

	private static final String MWW = "mww.xml";
	private static final String MWW_UPDATER = "mww-updater.xml";
	
	public Map getNewVersions(Map map){

		String mwwMobileVersion = map.get("mwwversion").toString();
		
		String mwwCurrentVersion = getParametroGeral("MWW_VERSAO_COLETOR");		
		String mwwCurrentUpdaterVersion = "1.1.0";
			
		if(mwwCurrentVersion.equalsIgnoreCase(mwwMobileVersion)){
			mwwCurrentVersion = null;
			mwwCurrentUpdaterVersion = null;
		}
				
		Filial filial = SessionUtils.getFilialSessao();

		String path = getUpdaterUtilService().findPathToNewVersions(
				filial.getIdFilial());
		
		Map mapReturn = new HashMap();
		mapReturn.put("path", path);
		mapReturn.put("mww", mwwCurrentVersion);
		mapReturn.put("mwwUpdater", mwwCurrentUpdaterVersion);
		if(map.containsKey("mwwconfig")){
			String mwwConfigDate = map.get("mwwconfig").toString();
			String mwwConfig = getParametroGeral("MWW_CONFIG_DEFAULT_DATE");
			mapReturn.put("mwwConfig", Long.parseLong(mwwConfigDate) < Long.parseLong(mwwConfig) ? mwwConfig : null);
		}
		
		return mapReturn;
		
	}
	

	public Map getPathToNewVersions(){
	
		Filial filial = SessionUtils.getFilialSessao();

		String path = getUpdaterUtilService().findPathToNewVersions(
				filial.getIdFilial());
		Map map = new HashMap();
		map.put("path", path);
		map.put("mww", MWW);
		map.put("mwwUpdater", MWW_UPDATER);
		return map;
		
	}

	private String getParametroGeral(String nmParam){
		ParametroGeral param = getParametroGeralService().findByNomeParametro(nmParam, Boolean.FALSE);
		if (param!=null) {
			return param.getDsConteudo();
		}else{
			return null;
		}
	}

	public void sendEmailWithPrint(Map map) throws IOException {
		String attachment = String.valueOf(map.get("attachment"));
		String obs = String.valueOf(map.get("obs"));

		String nmUsuario = SessionUtils.getUsuarioLogado().getNmUsuario();
		String filialLogado = SessionUtils.getFilialSessao().getSgFilial();
		
		String login = SessionUtils.getUsuarioLogado().getLogin();
		String dhLogin = String.valueOf(map.get("dhLogin"));
		String dhAtual = String.valueOf(map.get("dhAtual"));
		
		String version = String.valueOf(map.get("version"));
		String ip = String.valueOf(map.get("ip"));
		String mac = String.valueOf(map.get("mac"));
		
		String log = String.valueOf(map.get("log"));
		
		StringBuilder text = new StringBuilder()
			.append("Observação: ").append(obs)
			.append("\n\nUsuário: ").append(nmUsuario)
			.append("\nFilial logado: ").append(filialLogado)
			.append("\n\nLogin: ").append(login)
			.append("\nData/Hora login: ").append(dhLogin)
			.append("\nData/Hora envio: ").append(dhAtual)
			.append("\n\nVersão MWW: ").append(version)
			.append("\nEndereço IP: ").append(ip)
			.append("\nEndereço MAC: ").append(mac)
			.append("\n\nLog MWW: ");
		
		String[] sa = log.split(";");
		for (String row : sa) {
			text.append("\n\n");
			text.append(row);
		}
				
				
		byte[] bytes = Base64Util.decode(attachment);

		Properties p = new Properties();
		p.put("mail.host", "nt-exmtz02");

		Session session = Session.getInstance(p, null);
		MimeMessage email = new MimeMessage(session);
		try {
			email.setFrom(new InternetAddress("mww@tntbrasil.com.br","MWW"));
			email.setRecipients(Message.RecipientType.TO, 
					new InternetAddress[]
					                    { 
											new InternetAddress("roberto.azambuja@tntbrasil.com.br")
										});

			email.setSentDate(new Date());

			email.setSubject("MWW: Print enviado por " + SessionUtils.getUsuarioLogado().getNmUsuario());
			
			BodyPart message = new MimeBodyPart();       
			message.setText(text.toString());
			
			MimeBodyPart attachPart = new MimeBodyPart();  
			attachPart.setDataHandler(new DataHandler(new ByteArrayDataSource(bytes, "application/zip")));  
			attachPart.setFileName("mww.zip"); 
			
			Multipart corpo = new MimeMultipart();
			corpo.addBodyPart(message);
			corpo.addBodyPart(attachPart);
			
			email.setContent(corpo); 

			// evniando mensagem (tentando)
			Transport.send(email);
		} catch (AddressException e) {
			// nunca deixe catches vazios!
		} catch (MessagingException e) {
			// nunca deixe catches vazios!
		}
	}

	/**
	 * @param updaterUtilService
	 *            the updaterUtilService to set
	 */
	public void setUpdaterUtilService(UpdaterUtilService updaterUtilService) {
		this.updaterUtilService = updaterUtilService;
	}

	/**
	 * @return the updaterUtilService
	 */
	public UpdaterUtilService getUpdaterUtilService() {
		return updaterUtilService;
	}


	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
}


	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
}
