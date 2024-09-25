package com.mercurio.lms.edi.model.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.edi.model.ItemNotaFiscalApple;
import com.mercurio.lms.edi.model.NotaFiscalApple;
import com.mercurio.lms.edi.util.FTPFileFilter;
import com.mercurio.lms.edi.util.FtpConnection;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;

@Assynchronous(name = "importacaoAppleService")
public class ImportacaoAppleService {
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private Logger log = LogManager.getLogger(this.getClass());

	private IntegracaoJmsService integracaoJmsService;
	private NotaFiscalAppleService notaFiscalAppleService;
	private ItemNotaFiscalAppleService itemNotaFiscalAppleService;
	private ConfiguracoesFacade configuracoesFacade;

	private String ftpFolderNotfis; // "/@NOTFIS/CisaCPQ/BKP"
	private String ftpFolderEdi214; // "/@NOTFIS/CisaCPQ/EDI214"
	private StringBuilder report;
	private String administratorEmail;
	private String remetenteEmail;

	private String FTP_HOST;
	private String FTP_USER;
	private String FTP_PASSWORD;
	private String FTP_OS;

	private String FTP_HOST_CISA;
	private String FTP_USER_CISA;
	private String FTP_PASSWORD_CISA;
	private String FTP_OS_CISA;
	private String FTP_FOLDER_CISA;

	private String cabecalhoEmail;

	@AssynchronousMethod (
			name="importacaoAppleService.importarNotfis",
			type=BatchType.BATCH_SERVICE,
			feedback=BatchFeedbackType.ON_ERROR
	)
	public void importarNotfis() throws Exception {
		inicializaParametros();

		report = new StringBuilder();
		cabecalhoEmail = "EDI Notfis Report:\n";

		FtpConnection connESALES = new FtpConnection();
		connESALES.connect(FTP_HOST, ftpFolderNotfis, FTP_USER, FTP_PASSWORD, FTP_OS);
		FTPFile arquivos[] = null;
		arquivos = connESALES.listFTPFiles(new AppleNotfisFileFilter());

		if (arquivos != null) {
			for (FTPFile arquivo : arquivos) {
				report.append("\nFile Noftis received: " + arquivo.getName() + "\n");
				if (this.importaArquivoNotfis(connESALES, arquivo)) {
					String destino = ftpFolderNotfis + "/done/";
					if (!connESALES.moveFile(arquivo.getName(), destino, arquivo.getName())) {
						report.append("\nError on moving File Noftis: " + destino + arquivo.getName() + " to DONE directory\n");
					} else {
						connESALES.getFTPClient().deleteFile(arquivo.getName());
					}
				}
			}
		}

		if (report.length() > cabecalhoEmail.length()) {
			String destinatarios[] = {administratorEmail};
			sendEmail(destinatarios, "EDI Notfis Report", remetenteEmail, report.toString());
		}

		connESALES.disconnect();
	}

	@AssynchronousMethod (
			name="importacaoAppleService.importarEdi214",
			type=BatchType.BATCH_SERVICE,
			feedback=BatchFeedbackType.ON_ERROR
	)


	public void importarEdi214() throws Exception {
		inicializaParametros();

		Long idInformacaoDn = Long.valueOf(configuracoesFacade.getValorParametro("ID_INFORMACAO_DN_APPLE").toString());
		Long idInformacaoGs = Long.valueOf(configuracoesFacade.getValorParametro("ID_INFORMACAO_GS_APPLE").toString());
		Long idInformacaoIsa = Long.valueOf(configuracoesFacade.getValorParametro("ID_INFORMACAO_ISA_APPLE").toString());
		Long idInformacaoIsa2 = Long.valueOf(configuracoesFacade.getValorParametro("ID_INFORMACAO_ISA2_APPLE").toString());

		report = new StringBuilder();
		cabecalhoEmail = "EDI InBound Report:\n";
		report.append(cabecalhoEmail);

		FtpConnection connCISA = new FtpConnection();
		connCISA.connect(FTP_HOST_CISA, FTP_FOLDER_CISA, FTP_USER_CISA, FTP_PASSWORD_CISA, FTP_OS_CISA);

		FTPFile arquivos[] = null;
		arquivos = connCISA.listFTPFiles(new AppleCisaFileFilter());

		FtpConnection connESALES = new FtpConnection();
		connESALES.connect(FTP_HOST, ftpFolderEdi214, FTP_USER, FTP_PASSWORD, FTP_OS);

		List<FTPFile> importedFiles = new ArrayList<FTPFile>();
		if(arquivos != null) {
			/* Percorre arquivos */
			for(FTPFile arquivo : arquivos) {
				/* Caso importe o arquivo, insere na lista de importados */
				if(this.importaArquivoEdi214(connCISA, arquivo, idInformacaoDn, idInformacaoGs, idInformacaoIsa, idInformacaoIsa2)) {
					importedFiles.add(arquivo);
				}
			}

			/* Percorre arquivos importados */
			for(FTPFile imported : importedFiles) {
				/* Grava arquivo no FTP da TNT */
				InputStream source = connCISA.getFTPClient().retrieveFileStream(imported.getName());
				connESALES.storeFile(imported, source);
				/* Deleta arquivo do FTP da apple */
				connCISA.getFTPClient().completePendingCommand();
				connCISA.deleteFile(imported.getName());
			}
		}

		connCISA.disconnect();
		connESALES.disconnect();

		if (report.length() > cabecalhoEmail.length()) {
			String destinatarios[] = {administratorEmail};
			sendEmail(destinatarios, "EDI InBound Report", remetenteEmail, report.toString());
		}

	}

	private void inicializaParametros() {
		FTP_HOST = configuracoesFacade.getValorParametro("APPLE_FTP_HOST").toString();
		FTP_USER = configuracoesFacade.getValorParametro("APPLE_FTP_USER").toString();
		FTP_PASSWORD = configuracoesFacade.getValorParametro("APPLE_FTP_PASSWORD").toString();
		FTP_OS = configuracoesFacade.getValorParametro("APPLE_FTP_OS").toString();

		FTP_HOST_CISA = configuracoesFacade.getValorParametro("APPLE_FTP_CISA_HOST").toString();
		FTP_USER_CISA = configuracoesFacade.getValorParametro("APPLE_FTP_CISA_USER").toString();
		FTP_PASSWORD_CISA = configuracoesFacade.getValorParametro("APPLE_FTP_CISA_PASSWORD").toString();
		FTP_OS_CISA = configuracoesFacade.getValorParametro("APPLE_FTP_CISA_OS").toString();
		FTP_FOLDER_CISA = configuracoesFacade.getValorParametro("APPLE_FTP_CISA_FOLDER").toString();

		administratorEmail = configuracoesFacade.getValorParametro("APPLE_ADM_EMAIL").toString();
		remetenteEmail = configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS").toString();

		ftpFolderNotfis = configuracoesFacade.getValorParametro("APPLE_INBOUND_NOTFIS_BKP").toString();
		ftpFolderEdi214 = configuracoesFacade.getValorParametro("APPLE_INBOUND_NOTFIS_214").toString();
	}

	private void sendEmail(String[] strEmails, String strSubject, String strFrom, String strText){
		Mail mail = createMail(StringUtils.join(strEmails, ";"), strFrom, strSubject, strText);

		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		return mail;
	}

	private boolean importaArquivoNotfis(FtpConnection conn, FTPFile arquivo) throws Exception {
		BufferedReader buffReader = null;
		boolean retorno = true;
		InputStream is = null;
		try {
			is = conn.getFTPClient().retrieveFileStream(arquivo.getName());
			if (is != null) {
				buffReader = new BufferedReader(new InputStreamReader(is));

				String line = null;
				while ((line = buffReader.readLine()) != null) {
					if (comando(line, "313")) {
						Long nrNotaFiscal = Long.parseLong(line.substring(32, 40));
						YearMonthDay dtEmissao = this.toDate(line.substring(40, 48), "ddMMyyyy");
						String nrSerie = line.substring(29, 32).trim();

						NotaFiscalApple nota = notaFiscalAppleService.find(nrNotaFiscal, nrSerie, dtEmissao);

						if (nota == null) {
							nota = new NotaFiscalApple();
							nota.setDtEmissao(dtEmissao);
							nota.setNrNotaFiscal(nrNotaFiscal);
							nota.setNrSerie(nrSerie);
							notaFiscalAppleService.store(nota);
						}
					}
				}
			} else {
				report.append("\nError on File: " + arquivo.getName() + " returned null on reading\n");
				retorno = false;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (buffReader != null) {
				try {
					buffReader.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
			if (is != null) {
				is.close();
			}
			conn.getFTPClient().completePendingCommand();
		}
		return retorno;
	}

	private Boolean importaArquivoEdi214(FtpConnection conn, FTPFile arquivo, Long idInformacaoDn, Long idInformacaoGs, Long idInformacaoIsa, Long idInformacaoIsa2) throws Exception {
		BufferedReader buffReader = null;
		InputStream is = null;
		try {
			is = conn.getFTPClient().retrieveFileStream(arquivo.getName());
			buffReader = new BufferedReader(new InputStreamReader(is));

			String line = null;
			String dnn = "";
			String gsInfo = "";
			String isaInfo = "";

			boolean vernota = false;
			while ((line = buffReader.readLine()) != null) {
				if (comando(line, "L11"))
				{
					if (line.indexOf(":", 4) != -1)
						dnn = line.substring(4, line.indexOf(":", 4));
					else
						dnn = line.substring(4, line.length());
				}
				if (comando(line, "GS"))
					gsInfo = line;
				if (comando(line, "ISA"))
					isaInfo = line;

				if (vernota) {
					Long nrNotaFiscal = Long.parseLong(line.trim());
					NotaFiscalApple notaFiscal = notaFiscalAppleService.find(nrNotaFiscal, null, null);

					if(notaFiscal != null) {
						report.append("\nFile: " + arquivo.getName() + " with complements for invoice number: " + nrNotaFiscal + "\n");
						boolean inserted = false;
						if(dnn != null && !dnn.trim().equals("")) {
							if (itemNotaFiscalAppleService.find(nrNotaFiscal, idInformacaoDn, dnn) == null)  {
								ItemNotaFiscalApple item = this.generateNewItemNotaFiscalApple(notaFiscal, idInformacaoDn, dnn, dnn);
								itemNotaFiscalAppleService.store(item);
								report.append("   Complement: [" + idInformacaoDn + "] " + dnn + "\n");
								inserted = true;
							}

							if (itemNotaFiscalAppleService.find(nrNotaFiscal, idInformacaoIsa, dnn) == null) {
								ItemNotaFiscalApple item = this.generateNewItemNotaFiscalApple(notaFiscal, idInformacaoIsa, dnn, isaInfo.substring(0,59).replace(":", "*"));
								itemNotaFiscalAppleService.store(item);
								report.append("   Complement: [" + idInformacaoIsa + "] " + isaInfo.substring(0,59).replace(":", "*") + "\n");
								inserted = true;
							}

							if (itemNotaFiscalAppleService.find(nrNotaFiscal, idInformacaoIsa2, dnn) == null) {
								ItemNotaFiscalApple item = this.generateNewItemNotaFiscalApple(notaFiscal, idInformacaoIsa2, dnn, isaInfo.substring(59).replace(":", "*"));
								itemNotaFiscalAppleService.store(item);
								report.append("   Complement: [" + idInformacaoIsa2 + "] " + isaInfo.substring(59).replace(":", "*") + "\n");
								inserted = true;
							}

							if (itemNotaFiscalAppleService.find(nrNotaFiscal, idInformacaoGs, dnn) == null) {
								ItemNotaFiscalApple item = this.generateNewItemNotaFiscalApple(notaFiscal, idInformacaoGs, dnn, gsInfo.replace(":", "*"));
								itemNotaFiscalAppleService.store(item);
								report.append("   Complement: [" + idInformacaoGs + "] " + gsInfo.replace(":", "*") + "\n");
								inserted = true;
							}

							if (!inserted) {
								report.append("   No updates.\n");
							}
						}
					} else {
						report.append("\nFile: " + arquivo.getName() + " waiting for invoice number: " + nrNotaFiscal + "\n");
						return false;
					}
				}

				if (comando(line, "IEA")) {
					vernota = true;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (buffReader != null) {
				try {
					buffReader.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
			if (is != null) {
				is.close();
			}
			conn.getFTPClient().completePendingCommand();
		}

		return true;
	}

	private ItemNotaFiscalApple generateNewItemNotaFiscalApple(NotaFiscalApple nf, Long idInformacaoDoctoCliente, String dn, String dsValorCampo) {
		ItemNotaFiscalApple item = new ItemNotaFiscalApple();
		item.setNotaFiscalApple(nf);

		InformacaoDoctoCliente info = new InformacaoDoctoCliente();
		info.setIdInformacaoDoctoCliente(idInformacaoDoctoCliente);
		item.setInformacaoDoctoCliente(info);

		item.setDsValorCampo(dsValorCampo);
		item.setDsDnn(dn);

		return item;
	}


	private YearMonthDay toDate(String data, String format) {
		Date date = null;
		try {
			DateFormat formatter = new SimpleDateFormat(format);
			date = (Date)formatter.parse(data);
		} catch (ParseException e) {
			log.error(e);
		}

		return YearMonthDay.fromDateFields(date);
	}

	private boolean comando(String line, String command) {
		if (line.length() > command.length()) {
			return line.substring(0, command.length()).equals(command);
		}
		return false;
	}

	public void setNotaFiscalAppleService(
			NotaFiscalAppleService notaFiscalAppleService) {
		this.notaFiscalAppleService = notaFiscalAppleService;
	}

	public void setItemNotaFiscalAppleService(
			ItemNotaFiscalAppleService itemNotaFiscalAppleService) {
		this.itemNotaFiscalAppleService = itemNotaFiscalAppleService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}

class AppleNotfisFileFilter implements FTPFileFilter {
	public boolean accept(String file) {
		String f = file.toLowerCase();
		if (f.indexOf("0_1.txt") > 0 || f.indexOf("0_2.txt") > 0)
			return true;
		else
			return false;
	}
}

class AppleCisaFileFilter implements FTPFileFilter {
	public boolean accept(String file) {
		String f = file.toLowerCase();
		return f.indexOf(".edi") > 0;
	}
}
