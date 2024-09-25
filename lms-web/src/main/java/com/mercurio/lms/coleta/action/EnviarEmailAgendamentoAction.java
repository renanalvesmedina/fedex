package com.mercurio.lms.coleta.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.xml.sax.SAXException;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.entrega.model.AgendamentoAnexo;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.service.AgendamentoEntregaService;
import com.mercurio.lms.entrega.model.service.RotinasEnviarEmailRelatorioAgendamentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.expedicao.util.NFEUtils;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.sim.model.service.TemplateRelatorioService;
import com.mercurio.lms.sim.reports.GerarDanfeService;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

public class EnviarEmailAgendamentoAction extends CrudAction {
	
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private static final String EXTENSAO_PDF = ".pdf";
	private Logger log = LogManager.getLogger(this.getClass());
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private AgendamentoEntregaService agendamentoEntregaService;
	private ConfiguracoesFacade configuracoesFacade;
	private ClienteService clienteService;
	private TemplateRelatorioService templateRelatorioService;
	private RotinasEnviarEmailRelatorioAgendamentoService rotinasEnviarEmailRelatorioAgendamentoService;
	private IntegracaoJmsService integracaoJmsService; 

	public TypedFlatMap loadPage(TypedFlatMap map) {
		HashMap<String, Object> mapEmail = rotinasEnviarEmailRelatorioAgendamentoService.generateEmail(
				"A", 
				agendamentoEntregaService.findClienteRemetByIdAgendamentoEntrega(map.getLong("idAgendamentoEntrega")), 
				agendamentoEntregaService.findClienteDestByIdAgendamentoEntrega(map.getLong("idAgendamentoEntrega")), 
				map.getLong("idAgendamentoEntrega"), map.getYearMonthDay("dtAgendamento"));
		map.put("infoEmail", mapEmail);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public TypedFlatMap enviarEmail(final TypedFlatMap map) {
		final String chavesNFe = map.getString("enviarEmail.chavesNFe");
		final String from = map.getString("enviarEmail.de");
		final String[] to = map.getString("enviarEmail.para").split(";");
		final String subject = map.getString("enviarEmail.assunto");
		final String text = map.getString("enviarEmail.texto");
		final Long idAgendamentoEntrega = map.getLong("idAgendamentoEntrega");
		final List<HashMap> anexos = new ArrayList<HashMap>();

		final boolean anexarDanfe = map.getBoolean("enviarEmail.anexarDanfe");
		final boolean anexarXML = map.getBoolean("enviarEmail.anexarXml");
		final Long idTemplateRelatorio = map.getLong("enviarEmail.idTemplateRelatorio");

		final com.mercurio.adsm.framework.security.model.Usuario currentUser = SessionContext.getUser();
		final Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? currentUser.getLocale() : new Locale("pt", "BR");

		try {
			if (anexarXML) {
				anexos.addAll(getAnexosForChaveNFe(chavesNFe));
			}
		} catch (Exception e) {
			map.put("error", "true");
			log.error(e);
			return map;
		}

		List<MailAttachment> mailAttachments = new ArrayList<MailAttachment>();

		if (idTemplateRelatorio != null) {
			Map<String, Object> mapReportFileAgendamento = rotinasEnviarEmailRelatorioAgendamentoService.executeReplaceAgendamento(idAgendamentoEntrega, idTemplateRelatorio);
			if (mapReportFileAgendamento != null && !mapReportFileAgendamento.isEmpty()) {
				MailAttachment mailAttachment = new MailAttachment();
				mailAttachment.setName((String) mapReportFileAgendamento.get("nomeArquivo"));
				mailAttachment.setData(FileUtils.readFile((File) mapReportFileAgendamento.get("reportFileAgendamento")));
				mailAttachments.add(mailAttachment);
			}
		}

		anexos.addAll(getAnexosFromAgendamentoAnexo(idAgendamentoEntrega));

		if (anexarDanfe) {
			String[] chvs = chavesNFe.split(",");
			String chave = null;

			for (int i = 0; i < chvs.length; i++) {
				chave = chvs[i].trim();
				File danfe;
				try {
					String xmlString = notaFiscalEletronicaService.findNfeXMLFromEDIToString(chave);
					GerarDanfeService gerarDanfeService = new GerarDanfeService(currentUserLocale, null);
					danfe = gerarDanfeService.execute(xmlString, currentUserLocale);
					MailAttachment mailAttachment = new MailAttachment();
					mailAttachment.setName(NFEUtils.getNumeroNotaFiscalByChave(chave) + EXTENSAO_PDF);
					mailAttachment.setData(FileUtils.readFile(danfe));
					mailAttachments.add(mailAttachment);
				} catch (Exception e) {
					log.error(e);
				}
			}
		}

		for (HashMap anexo : anexos) {
			boolean anexoEntrega = false;
			InputStream in = null;
			if (anexo.get("stream") != null) {
				anexoEntrega = true;
				byte[] arquivo = (byte[]) anexo.get("stream");
				in = new ByteArrayInputStream(arquivo);
			} else {
				in = (InputStream) anexo.get("xml");
			}

			FileOutputStream fileOutputStream = null;

			try {
				File file = null;
				if (anexoEntrega) {
					String[] nome = ((String) anexo.get("name")).split("\\.");
					file = File.createTempFile(nome[0], "." + nome[1]);
				} else {
					file = File.createTempFile((String) anexo.get("name"), ".xml");
				}
				file.deleteOnExit();

				fileOutputStream = new FileOutputStream(file);
				OutputStreamWriter outputStreamWriter = null;
				if (anexoEntrega) {
					org.jfree.io.IOUtils.getInstance().copyStreams(in, fileOutputStream);
					fileOutputStream.close();
					in.close();
				} else {
					InputStreamReader inReader = null;
					inReader = new InputStreamReader(in, Charset.forName("ISO-8859-1"));
					outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charset.forName("UTF-8"));
					int c;
					while ((c = inReader.read()) != -1) {
						outputStreamWriter.write(c);
					}
					outputStreamWriter.close();
					fileOutputStream.close();
				}

				String nomeArquivo = "";
				if (anexoEntrega) {
					nomeArquivo = (String) anexo.get("name");
				} else {
					nomeArquivo = file.getName();
				}
				MailAttachment mailAttachment = new MailAttachment();
				mailAttachment.setName(nomeArquivo);
				mailAttachment.setData(FileUtils.readFile(file));
				mailAttachments.add(mailAttachment);

			} catch (Exception e) {
				map.put("error", "true");
				log.error(e);
			}
		}

		Mail mail = createMail(StringUtils.join(to, ";"), from, subject, text, mailAttachments);

		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);

		map.put("error", "false");

		return map;
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body, List<MailAttachment> mailAttachmentList) {

		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		mail.setAttachements(mailAttachmentList.toArray(new MailAttachment[mailAttachmentList.size()]));

		return mail;
	}
	
	private List<HashMap> getAnexosFromAgendamentoAnexo(Long idAgendamentoEntrega){
		List<HashMap> anexos = new ArrayList<HashMap>();
		List<AgendamentoAnexo> allAnexos = agendamentoEntregaService.findAllAgendamentoAnexoByEntrega(idAgendamentoEntrega);
		for (AgendamentoAnexo agendamentoAnexo : allAnexos) {
			
			HashMap anexo = new HashMap();
			anexo.put("stream", agendamentoAnexo.getDcArquivo());
			anexo.put("name", agendamentoAnexo.getDsAnexo());
			
			anexos.add(anexo);
		}
		return anexos;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<HashMap> getAnexosForChaveNFe(String chavesNFe) throws IOException, SQLException, SAXException, ParserConfigurationException {
		String[] chvs = chavesNFe.split(",");
		String chave = null;
		List<HashMap> anexos = new ArrayList<HashMap>();
		
		for (int i = 0; i < chvs.length; i++) {
			chave = chvs[i].trim();
			
			InputStream xml = notaFiscalEletronicaService.findNfeXMLFromEDI(chave);
						
			HashMap anexo = new HashMap();
			anexo.put("xml", xml);
			anexo.put("name", chave);
			
			anexos.add(anexo);
		}
		
		return anexos;
	}
	
	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

	public ResultSetPage findPaginatedAgendamentoAnexo(TypedFlatMap criteria) {
		return agendamentoEntregaService.findPaginatedAgendamentoAnexo(criteria);
	}

	public Integer findAgendamentoAnexoRowCount(TypedFlatMap criteria) {
		return agendamentoEntregaService.getRowCountAgendamentoAnexo(criteria.getLong("idAgendamentoEntrega"));
	}
	
	@SuppressWarnings("rawtypes")
	public void storeAnexo(TypedFlatMap criteria){
		AgendamentoAnexo agendamentoAnexo = new AgendamentoAnexo();
		AgendamentoEntrega agendamentoEntrega = agendamentoEntregaService.findById(criteria.getLong("idAgendamentoEntrega"));
		agendamentoAnexo.setAgendamentoEntrega(agendamentoEntrega);
		agendamentoAnexo.setDhInclusao(new DateTime());
		agendamentoAnexo.setUsuario(SessionUtils.getUsuarioLogado());
		
		
		
		String[] nomeArquivo = criteria.getString("dirArquivo").split("\\\\");
		agendamentoAnexo.setDsAnexo(nomeArquivo[nomeArquivo.length-1]);
		try {
			String arquivo = criteria.getString("dcArquivo");
			byte[] decodeArquivo = Base64Util.decode(arquivo);
			agendamentoAnexo.setDcArquivo(ArrayUtils.subarray(decodeArquivo, 1024, decodeArquivo.length));
			agendamentoEntregaService.storeBasicAgendamentoAnexo(agendamentoAnexo);
		} catch (Exception e) {
			log.error(e);
		}
		
	}
	
	public void excluiAnexos(TypedFlatMap criteria){
		Long idAgendamentoEntrega = criteria.getLong("idAgendamentoEntrega");
		if(idAgendamentoEntrega != null){
			agendamentoEntregaService.excluiAnexos(idAgendamentoEntrega);
		}
	}

	public void setAgendamentoEntregaService(
			AgendamentoEntregaService agendamentoEntregaService) {
		this.agendamentoEntregaService = agendamentoEntregaService;
	}
	
	public void removeByIdsAgendamentoAnexo(TypedFlatMap criteria){
		List<Long> ids = listAsLong(criteria.getList("ids"));
		
		agendamentoEntregaService.removeByIdAgendamentoAnexo(ids);
		
	}
	
	private List listAsLong(List ids) {
		List retorno = new ArrayList();
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			String id = (String) iter.next();
			retorno.add(Long.valueOf(id));
		}
		return retorno;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	public TemplateRelatorioService getTemplateRelatorioService() {
		return templateRelatorioService;
	}

	public void setTemplateRelatorioService(
			TemplateRelatorioService templateRelatorioService) {
		this.templateRelatorioService = templateRelatorioService;
	}

	public RotinasEnviarEmailRelatorioAgendamentoService getRotinasEnviarEmailRelatorioAgendamentoService() {
		return rotinasEnviarEmailRelatorioAgendamentoService;
	}

	public void setRotinasEnviarEmailRelatorioAgendamentoService(
			RotinasEnviarEmailRelatorioAgendamentoService rotinasEnviarEmailRelatorioAgendamentoService) {
		this.rotinasEnviarEmailRelatorioAgendamentoService = rotinasEnviarEmailRelatorioAgendamentoService;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	
}
