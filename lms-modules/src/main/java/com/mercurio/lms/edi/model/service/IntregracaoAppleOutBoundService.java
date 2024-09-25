package com.mercurio.lms.edi.model.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.edi.model.AppleLogEdi;
import com.mercurio.lms.edi.model.EDI945File;
import com.mercurio.lms.edi.model.Edi945dnn;
import com.mercurio.lms.edi.model.Edi945dnnLabes;
import com.mercurio.lms.edi.util.ConstantesPdf2HashMap;
import com.mercurio.lms.edi.util.FTPFileFilter;
import com.mercurio.lms.edi.util.Pdf2HashMap;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.ftp.FTPFactory;
import com.mercurio.lms.util.ftp.Ftp;
import com.mercurio.lms.util.ftp.Ftp.FtpFlavor;
import com.mercurio.lms.util.ftp.FtpConnectionData;
import com.mercurio.lms.util.ftp.FtpException;
import com.mercurio.lms.util.ftp.SFtpImpl;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

@Assynchronous(name = "intregracaoAppleOutBoundService")
public class IntregracaoAppleOutBoundService {
	private static final int SFTP_PORT = 22;
	private static final String TXT_FILE = ".txt";
	private static final String PDF_FILE = ".pdf";
	private static final String TEXT_HTML = "text/html; charset='utf-8'";

	private IntegracaoJmsService integracaoJmsService;
	private StringBuilder report;
	private Map<String, Map<String, String>> danfesMap;
	private Map<String, String> dn945;
	private ParametroGeralService parametroGeralService;
	private EDI945File edi945file = null;
	private Edi945dnn edi945dnn = null;
	private String ftpFolder;
	private String ftpFolder945Origin;
	private String ftpFolder945processed;
	private String ftpFolder945done;
	private String ftpFolder997;
	private String ftpFolder997done;
	private String ftpFolderDanfes;
	private String ftpFolderDanfesDone;
	private String ftpFolderNotfis;
	private String ftpFolderNotfisDone;
	private String administratorEmail;
	private String remetenteEmail;
	private String ftpFolderNotfisEsales;
	private String cabecalhoEmail;
	private AppleLogEdiService appleLogEdiService;
	private List<Edi945dnn> listaDnnGeracao;
	private List<EDI945File> lista945Geracao;
	private String ftpHost;
	private String ftpUser;
	private String ftpPassword;


	@AssynchronousMethod (
			name="intregracaoAppleOutBoundService.gerenciarArquivos",
			type=BatchType.BATCH_SERVICE,
			feedback=BatchFeedbackType.ON_ERROR
	)
	public void gerenciarArquivos() throws Exception {
		//Diretório de FTP para operações Outbound
		ftpFolder = parametroGeralService.findConteudoByNomeParametro("APPLE_OUTBOUND_FTP_DIR", false).toString();
		administratorEmail = parametroGeralService.findConteudoByNomeParametro("APPLE_ADM_EMAIL", false).toString();
		remetenteEmail = parametroGeralService.findConteudoByNomeParametro("REMETENTE_EMAIL_LMS", false).toString();
		ftpFolderNotfisEsales = parametroGeralService.findConteudoByNomeParametro("APPLE_ESALES_NOTFIS", false).toString();

		ftpHost = parametroGeralService.findConteudoByNomeParametro("APPLE_FTP_HOST", false).toString();
		ftpUser = parametroGeralService.findConteudoByNomeParametro("APPLE_FTP_USER", false).toString();
		ftpPassword = parametroGeralService.findConteudoByNomeParametro("APPLE_FTP_PASSWORD", false).toString();

		ftpFolder945Origin = ftpFolder + "/EDI945/";
		ftpFolder945processed = ftpFolder945Origin + "processado/";
		ftpFolder945done = ftpFolder945processed + "done/";
		ftpFolder997 = ftpFolder + "/EDI997/";
		ftpFolder997done = ftpFolder997 + "done/";
		ftpFolderDanfes = ftpFolder + "/Danfes/";
		ftpFolderDanfesDone = ftpFolderDanfes + "done/";
		ftpFolderNotfis = ftpFolderDanfes + "Notfis/";
		ftpFolderNotfisDone = ftpFolderNotfis + "done/";
		report = new StringBuilder();
		cabecalhoEmail = "EDI Outbound Report:\n";
		report.append(cabecalhoEmail);

		listaDnnGeracao = new ArrayList<Edi945dnn>();
		lista945Geracao = new ArrayList<EDI945File>();

		renomearArquivos945();
		LeituraArquivos945(ftpFolder945Origin);
		gerarArquivos997();
		LeituraArquivos945(ftpFolder945processed);
		verificarDanfeseNotfisparaDnn();
		lerArquivosDanfes();
		gerarArquivosNotfis();
		mover945completos();
		EnviarEmailReport();
	}

	private void mover945completos() throws Exception {
		boolean cabecalho = false;

		Ftp conn = FTPFactory.getInstance().getFtpImplementation(FtpFlavor.SFTP);
		conn.connect(new FtpConnectionData(FtpFlavor.SFTP, ftpHost, SFTP_PORT, ftpUser, ftpPassword));
		conn.changeWorkingDirectory(ftpFolder945processed);

		for (EDI945File file945: lista945Geracao) {
			boolean notfisGerado = true;
			for (Edi945dnn dnn: file945.getDnns()) {
				if (!dnn.isNotfisGerado()) {
					notfisGerado = false;
					break;
				}
			}

			if (notfisGerado) {
				if (!cabecalho) {
					report.append("\n-----------------------------------------------------------------------------------------------");
					report.append("\nEDI 945 with a complete workflow:\n");
				}
				cabecalho = true;
				if (!conn.rename(ftpFolder945processed + file945.getPath(), ftpFolder945done + file945.getPath())){
					report.append("It couldn't move file: " + file945.getPath() + " to: " + ftpFolder945done);
				}
				report.append("   " + file945.getPath() + "\n");

				for (Edi945dnn dnn: file945.getDnns()) {
					if (!conn.rename(ftpFolderNotfis + dnn.getNomeArquivoNotfis(), ftpFolderNotfisDone + dnn.getNomeArquivoNotfis())){
						report.append("It couldn't move file: " + dnn.getNomeArquivoNotfis() + " to: " + ftpFolderNotfisDone);
					}
				}
			}
		}
		conn.disconnect();
	}

	private void EnviarEmailReport() {
		if (report.length() > cabecalhoEmail.length()) {
			String destinatarios[] = {administratorEmail};
			sendEmail(destinatarios, "EDI Outbound Report", remetenteEmail, report.toString());
		}
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

	private void verificarDanfeseNotfisparaDnn() throws Exception {
		Ftp conn = FTPFactory.getInstance().getFtpImplementation(FtpFlavor.SFTP);
		conn.connect(new FtpConnectionData(FtpFlavor.SFTP, ftpHost, SFTP_PORT, ftpUser, ftpPassword));

		conn.changeWorkingDirectory(ftpFolderDanfes);
		List<File> arquivos = conn.listFiles(PDF_FILE);
		if (arquivos != null) {
			for (File pdfFile : arquivos) {
				String dnn = pdfFile.getName().substring(3, 13);
				for (Edi945dnn edi945dnn: listaDnnGeracao) {
					if (dnn.equals(edi945dnn.getReferenceID())) {
						edi945dnn.setTemDanfe(true);
						edi945dnn.setNomeDanfeRecebido(pdfFile.getName());
						break;
					}
				}
			}
		}

		conn.changeWorkingDirectory(ftpFolderNotfis);
		arquivos = conn.listFiles(TXT_FILE);
		if (arquivos != null) {
			for (File txtFile : arquivos) {
				String dnn = txtFile.getName().substring(9, 19);
				for (Edi945dnn edi945dnn: listaDnnGeracao) {
					if (dnn.equals(edi945dnn.getReferenceID())) {
						edi945dnn.setNotfisGerado(true);
						edi945dnn.setNomeArquivoNotfis(txtFile.getName());
						break;
					}
				}
			}
		}
		conn.disconnect();
	}

	private void gerarArquivosNotfis() throws Exception {
		boolean cabecalho = false;

		if (listaDnnGeracao == null) {
			return;
		}
		for (int i = 0; i < listaDnnGeracao.size(); i++) {
			Edi945dnn edi945dnn = (Edi945dnn) listaDnnGeracao.get(i);
			Map<String, String> danfeMap = danfesMap.get(edi945dnn.getReferenceID());
			if ((danfeMap != null) && (edi945dnn.isTemDanfe()) && (!edi945dnn.isNotfisGerado())) {
				if (!cabecalho) {
					report.append("\n-----------------------------------------------------------------------------------------------");
					report.append("\nNew generated Notfis/invoices files:\n");
				}
				cabecalho = true;
				String nomeArquivo = null;
				String nota = null;
				int indNota = 0;

				try {
					nomeArquivo = GeracaoAppleNotFis.geracaoNotFisLMS(danfeMap, edi945dnn, ftpFolderNotfis, ftpFolderNotfisEsales);
					indNota = danfeMap.get(ConstantesPdf2HashMap.FATURA_DUPLICATA).toString().indexOf("Num.:");
					nota = danfeMap.get(ConstantesPdf2HashMap.FATURA_DUPLICATA).toString();
				} catch (Exception e) {
					nomeArquivo = null;
					report.append("   Invoice format has an invalid format: " + edi945dnn.getNomeDanfeRecebido());
				}

				if (nomeArquivo != null) {
					report.append("   " + nomeArquivo + " / Invoice Number: " + (nota.substring(indNota+6, indNota+14)).substring(0,8) + "\n");
					edi945dnn.setNotfisGerado(true);
					edi945dnn.setNomeArquivoNotfis(nomeArquivo);
				}
			}
		}
	}

	private void lerArquivosDanfes() throws Exception {
		Ftp conn = FTPFactory.getInstance().getFtpImplementation(FtpFlavor.SFTP);
		conn.connect(new FtpConnectionData(FtpFlavor.SFTP, ftpHost, SFTP_PORT, ftpUser, ftpPassword));
		conn.changeWorkingDirectory(ftpFolderDanfes);

		danfesMap = new HashMap<String, Map<String, String>>();

		Pdf2HashMap pdf = new Pdf2HashMap();

		List<File> arquivos = conn.listFiles(".pdf");
		boolean cabecalho = false;

		if (arquivos != null) {
			for (File pdfFile : arquivos) {
				if (pdfFile.getName().indexOf(PDF_FILE) > -1) {
					String dnn = pdfFile.getName().substring(3, 13);

					if (dn945.get(dnn) != null) {

						pdf.setInputStream(((SFtpImpl)conn).getFileInputStream(pdfFile.getName()));
						Map<String, String> mapa = pdf.generateHashMap();

						mapa.put("NFREG", dnn);
						mapa.put("NOMEARQUIVO", pdfFile.getName());
						danfesMap.put(dnn, mapa);

						if (!conn.rename(ftpFolderDanfes + pdfFile.getName(), ftpFolderDanfesDone + pdfFile.getName())){
							report.append("It couldn't move file: " + pdfFile.getName() + " to: " + ftpFolderDanfesDone);
						}
					} else {
						if (!cabecalho) {
							report.append("\n-----------------------------------------------------------------------------------------------");
							report.append("\nDanfes waiting for EDI 945's arrival:\n");
						}
						cabecalho = true;
						report.append("   " + pdfFile.getName() + "\n");
					}
				}
			}
		}

		conn.disconnect();
	}

	private void renomearArquivos945() throws FtpException {
		Ftp conn = FTPFactory.getInstance().getFtpImplementation(FtpFlavor.SFTP);
		conn.connect(new FtpConnectionData(FtpFlavor.SFTP, ftpHost, SFTP_PORT, ftpUser, ftpPassword));
		conn.changeWorkingDirectory(ftpFolder945Origin);

		List<File> arquivos = conn.listFiles(".edi");

		if (arquivos != null) {
			for (File arquivo : arquivos) {
				if (!conn.rename(ftpFolder945Origin + arquivo.getName(), ftpFolder945Origin + arquivo.getName()+".save")){
					report.append("It couldn't move file: " + arquivo.getName() + " to: " + ftpFolder945Origin);
				}
			}
		}
		conn.disconnect();
	}


	private void LeituraArquivos945(String origem) throws IOException, FtpException {
		Ftp conn = FTPFactory.getInstance().getFtpImplementation(FtpFlavor.SFTP);
		conn.connect(new FtpConnectionData(FtpFlavor.SFTP, ftpHost, SFTP_PORT, ftpUser, ftpPassword));
		conn.changeWorkingDirectory(origem);

		int id = 0;
		int iddnn = 0;
		int totaldnn = 0;
		edi945file = null;
		edi945dnn = null;
		dn945 = new HashMap<String, String>();
		boolean blOrigem = origem.equals(ftpFolder945Origin);

		List<File> arquivos = conn.listFiles(".edi.save");

		BufferedReader buffReader = null;
		if (arquivos != null) {
			if (arquivos.size() > 0) {
				report.append("\n-----------------------------------------------------------------------------------------------");
				if (blOrigem) {
					report.append("\nNew EDI 945 Files received:");
				} else {
					report.append("\nEDI 945 Files waiting for complete receiving of danfes:");
				}
				report.append(" " + arquivos.size() + " files. \n");
			}
			for (File arquivo : arquivos) {
				report.append("\n   " + arquivo.getName() + "\n");
				report.append("   DNs:\n");
				int contdnn = 0;

				InputStream is = ((SFtpImpl)conn).getFileInputStream(arquivo.getName());
				buffReader = new BufferedReader(new InputStreamReader(is));

				String line = null;
				while ((line = buffReader.readLine()) != null) {

					// Novo Arquivo EDI945
					if (comando(line, "ISA*")) {
						edi945dnn = null;
						edi945file = new EDI945File();
						edi945file.setId(Long.valueOf(id++));
						edi945file.setArquivo(arquivo.getName());
						edi945file.setInterchange(line);
						edi945file.setDnns(new ArrayList<Edi945dnn>());
						edi945file.setPath(arquivo.getName());
						edi945file.setGenerate997(!blOrigem);
						edi945file.setNrTransacao(Long.valueOf(arquivo.getName().substring(26, 35)));
						lista945Geracao.add(edi945file);
					} else if (comando(line, "GS*")) {
						edi945file.setFunctional(line);
					}

					// DNN Novo Header
					if (comando(line, "ST*")) {
						iddnn = 0;
						edi945dnn = new Edi945dnn();
						edi945dnn.setEdi945Id(Long.valueOf(iddnn++));
						edi945dnn.setEdi945(edi945file);
						edi945dnn.setTransaction(line);
						edi945dnn.setDnnLabels(new HashMap<String, Edi945dnnLabes>());
						edi945file.getDnns().add(edi945dnn);
					} else if (comando(line, "W06*")) {
						edi945dnn.setWarehouse(line);
					} else if (comando(line, "N1*")) {
						edi945dnn.setName(line);
					} else if (comando(line, "N9*SV*")) {
						edi945dnn.setDivisao(line);
					} else if (comando(line, "N9*XE*")) {
						edi945dnn.setTransportWay(line.substring(6, line.length()));
					} else if (comando(line, "N9*AW*")) {
						edi945dnn.setReferenceID(line.substring(6, 16));
						dn945.put(edi945dnn.getReferenceID(), edi945dnn.getReferenceID());
						report.append((contdnn == 0 ? "      " : " ") + edi945dnn.getReferenceID() + ",");
						contdnn++;
						if (contdnn > 10) {
							contdnn = 0;
							report.append("\n");
						}
						listaDnnGeracao.add(edi945dnn);
						totaldnn++;
					} else if (comando(line, "G62*86*")) {
						edi945dnn.setDateTransact(line.substring(7, 15));
					} else if (comando(line, "G69*")) {
						edi945dnn.setProduct(line.substring(4,line.length()));
					}

					if ((comando(line, "MAN*SM*")) && (edi945dnn != null)) {
						String codeLabel = line.substring(line.indexOf("AA*")+3, line.length());
						codeLabel = codeLabel.substring(0, codeLabel.indexOf("*"));

						if (edi945dnn.getDnnLabels().get(codeLabel) == null) {
							Edi945dnnLabes label = new Edi945dnnLabes();
							label.setLabel(codeLabel);
							label.setEdi945dnn(edi945dnn);
							edi945dnn.getDnnLabels().put(codeLabel, label);
						}
					}
				}
				buffReader.close();
				is.close();
			}
		}
		conn.disconnect();
	}

	private void gerarArquivos997() throws Exception {
		SFtpImpl conn = (SFtpImpl)FTPFactory.getInstance().getFtpImplementation(FtpFlavor.SFTP);
		conn.connect(new FtpConnectionData(FtpFlavor.SFTP, ftpHost, SFTP_PORT, ftpUser, ftpPassword));

		Long transacao997 = Long.valueOf(parametroGeralService.findConteudoByNomeParametro("APPLE_SQ_997_FILES", false).toString());

		BufferedOutputStream buffWriter = null;
		Calendar today = Calendar.getInstance();
		SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd_HHmmss_");
		SimpleDateFormat formato2 = new SimpleDateFormat("yyyyMMdd*HHmm*");
		SimpleDateFormat formato3 = new SimpleDateFormat("yyMMdd*HHmm*");
		boolean cabecalho = false;

		for (EDI945File arq: lista945Geracao) {
			if (!arq.isGenerated997()) {
				transacao997++;

				if (!cabecalho) {
					report.append("\n-----------------------------------------------------------------------------------------------");
					report.append("\nEDI 997 generated:\n");
				}
				cabecalho = true;
				String[] interchanges = arq.getInterchange().split("\\*");

				String nomeArquivo = interchanges[6].trim() + "_997_FA_"
						+ formato.format(today.getTime())
						+ String.format("%010d", transacao997) + ".x12.save";
				report.append("   " + nomeArquivo.substring(0, nomeArquivo.length()-5) + "\n");

				conn.changeWorkingDirectory(ftpFolder997done);
				OutputStream ou = conn.getSftpChannel().put(nomeArquivo);

				buffWriter = new BufferedOutputStream(ou);

				String interchange = interchanges[0]+"*"+interchanges[1]+"*"+interchanges[2]+"*"+interchanges[3]
						+"*"+interchanges[4]+"*"+interchanges[7]+"*"+interchanges[8]+"*"+interchanges[5]
						+"*"+interchanges[6]+"*";
				escreve(buffWriter,
						interchange
								+ formato3.format(today.getTime()) + "U*" + interchanges[12]
								+ "*" + String.format("%09d", transacao997)
								+ "*0*T*>");

				String linhaGSFuncional = arq.getFunctional();

				String[] functional = linhaGSFuncional.split("\\*");
				String grupo = functional[1];
				String rementente = functional[3].trim();
				String destinatario = functional[2].trim();
				String controle = functional[6];
				String versao = functional[8];


				String transacao997_4 = "1"; //= transacao997 +

				transacao997_4 = "00000".substring(0, 4-transacao997_4.length()) + transacao997_4;
				escreve(buffWriter, "GS*FA*"+rementente+"*" + destinatario + "*"
						+ formato2.format(today.getTime()) + transacao997 + "*X*"
						+ versao);
				escreve(buffWriter, "ST*997*" + transacao997_4);
				escreve(buffWriter, "AK1*" + grupo + "*" + controle);
				int totst = 4;
				int totqtd945 = 0;
				for (int j = 0; j < arq.getDnns().size(); j++) {
					escreve(buffWriter, "AK2*945*"
							+ arq.getDnns().get(j).getTransaction()
							.substring(7, 11));
					totqtd945++;
					escreve(buffWriter, "AK5*A");
					totst += 2;
				}
				escreve(buffWriter, "AK9*A*" + totqtd945 + "*" + totqtd945 + "*"
						+ totqtd945);
				escreve(buffWriter, "SE*" + totst + "*" + transacao997_4);
				escreve(buffWriter, "GE*1*" + transacao997);
				escreve(buffWriter, "IEA*1*" + String.format("%09d", transacao997));
				buffWriter.close();
				ou.close();

				//Busca arquivo salvo
				conn.changeWorkingDirectory(ftpFolder997done);
				InputStream is = conn.getSftpChannel().get(ftpFolder997done + nomeArquivo);

				//Copia arquivo criado na pasta done para a pasta EDI997
				conn.changeWorkingDirectory(ftpFolder997);
				String nomeArquivo2 =  interchanges[6].trim() + "_997_FA_" + formato.format(today.getTime()) + String.format("%010d", transacao997) + ".x12";
				ou = conn.getSftpChannel().put(nomeArquivo2);
				IOUtils.copy(is, ou);
				ou.close();
				is.close();

				//Move arquivo EDI945 para pasta de processados
				conn.changeWorkingDirectory(ftpFolder945Origin);
				if (!conn.rename(ftpFolder945Origin + arq.getPath(), ftpFolder945processed + arq.getPath())){
					report.append("It couldn't move file: " + arq.getPath() + " to: " + ftpFolder945processed);
				}

				AppleLogEdi appleLogEdi = new AppleLogEdi();
				appleLogEdi.setTpAppleLogEdi("997");
				appleLogEdi.setNrTransacao(arq.getNrTransacao());
				appleLogEdi.setNrTransacaoResp(transacao997);
				appleLogEdi.setDhAppleLogEdi(JTDateTimeUtils.getDataHoraAtual());
				appleLogEdi.setDhEventoEdi(JTDateTimeUtils.getDataHoraAtual());
				appleLogEdiService.store(appleLogEdi);
			}
		}
		conn.disconnect();
		if (lista945Geracao.size() > 0) {
			parametroGeralService.storeValorParametro("APPLE_SQ_997_FILES", new BigDecimal(transacao997));
		}

	}

	public static void escreve(BufferedOutputStream buffWriter, String valor)
			throws IOException {
		char LF = 10;
		for (int i = 0; i < valor.length(); i++) {
			buffWriter.write(valor.charAt(i));
		}
		buffWriter.write(LF);
	}

	private static boolean comando(String line, String command) {
		if (line.length() > command.length()) {
			return line.substring(0, command.length()).equals(command);
		}
		return false;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setAppleLogEdiService(AppleLogEdiService appleLogEdiService) {
		this.appleLogEdiService = appleLogEdiService;
	}
}

class AppleDanfesPDFFileFilter implements FTPFileFilter {
	public boolean accept(String file) {
		String f = file.toLowerCase();
		return (f.indexOf(".pdf") > 0);
	}
}

class AppleDanfesTXTFileFilter implements FTPFileFilter {
	public boolean accept(String file) {
		String f = file.toLowerCase();
		return (f.indexOf(".txt") > 0);
	}
}

class Apple945FileFilter implements FTPFileFilter {
	public boolean accept(String file) {
		String f = file.toLowerCase();
		return (f.indexOf(".edi") > 0) && (f.indexOf(".save") == -1);
	}
}

class Apple945SaveFileFilter implements FTPFileFilter {
	public boolean accept(String file) {
		String f = file.toLowerCase();
		return f.indexOf(".edi.save") > 0;
	}
}
