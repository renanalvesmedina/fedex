package com.mercurio.lms.entrega.model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.joda.time.YearMonthDay;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.dao.ParametroGeralDAO;
import com.mercurio.lms.configuracoes.model.dao.PessoaDAO;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.AgendamentoMonitCCT;
import com.mercurio.lms.entrega.model.Turno;
import com.mercurio.lms.entrega.reports.AgendamentoReportRunnerCCT;
import com.mercurio.lms.entrega.reports.DevolucaoReportRunnerCCT;
import com.mercurio.lms.entrega.reports.FUPReportRunnerCCT;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.expedicao.util.NFEUtils;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.MonitoramentoCCT;
import com.mercurio.lms.sim.model.service.ContatosCCTService;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.sim.model.service.TemplateRelatorioService;
import com.mercurio.lms.sim.reports.GerarDanfeService;
import com.mercurio.lms.util.ArquivoUtils;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.SpringBeanFactory;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.EnvioCteCliente;
import com.mercurio.lms.vendas.model.dao.EnvioCteClienteDAO;
import com.mercurio.lms.vendas.model.service.ClienteService;


/** 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.rotinasEnviarEmailRelatorioAgendamentoService"
 */
@Assynchronous(name="RotinasEnviarEmailRelatorioAgendamentoService")
public class RotinasEnviarEmailRelatorioAgendamentoService {
	private static final String OBSERVACAO_EVENTO = "Batch automático";
	private static final String EXTENSAO_PDF = ".pdf";
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	
	private SpringBeanFactory springBeanFactory;
	private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private TemplateRelatorioService templateRelatorioService;
	private ParametroGeralDAO parametroGeralDAO;
	private EnvioCteClienteDAO envioCteClienteDAO;
	private ContatosCCTService contatosCCTService;
	private ConfiguracoesFacade configuracoesFacade;
	private PessoaDAO pessoaDAO;
	private ClienteService clienteService;
	private TurnoService turnoService;
	private static final Log LOG = LogFactory.getLog(RotinasEnviarEmailRelatorioAgendamentoService.class);	
	private AgendamentoEntregaService agendamentoEntregaService; 
	private IntegracaoJmsService integracaoJmsService;
	private final Long MEGABYTE_TO_BYTE = 1048576L;

	private Long getLimiteTamanhoEmail(){
		Long tamanhoAnexos =  Long.parseLong(parametroGeralDAO.findByNomeParametro("LIMITE_TAMANHO_EMAIL", false).getDsConteudo());
		tamanhoAnexos = tamanhoAnexos * MEGABYTE_TO_BYTE;
		return tamanhoAnexos;
	}
	
	private Long getLimiteNroAnexoEmail(){
		return Long.parseLong(parametroGeralDAO.findByNomeParametro("LIMITE_NRO_ANEXO_EMAIL", false).getDsConteudo());
	}

	private String getEnderecoEmailRemetente(){
		return (String) parametroGeralDAO.findByNomeParametro("DS_REMETENTE_EMAIL", false).getDsConteudo();
	}
	
	@AssynchronousMethod(name="sim.executeReplaceDevolucao", type=BatchType.BATCH_SERVICE, feedback=BatchFeedbackType.ON_ERROR)
	public void executeReplaceDevolucao(){
		Long tamanhoAnexos = getLimiteTamanhoEmail();
		List<Long> clientesCCT = clienteService.findIdsClienteCCT();
		
		for (Long idClienteCCT : clientesCCT) {
			generateRelatorioDevolucao(idClienteCCT, tamanhoAnexos);
		}
	}
	
	private void generateRelatorioDevolucao(Long idClienteCCT, Long tamanhoAnexos){
		Cliente clienteDestinatario = populateClienteById(idClienteCCT);
		Map<String, Object> dadosEmail = generateEmail("D", null, clienteDestinatario, null, null);
		Long idEnvioCteCliente = (Long) dadosEmail.get("idEnvioCteCliente");
		Long idTemplateRelatorio = (Long) dadosEmail.get("idTemplateRelatorio");
		
		if (idEnvioCteCliente != null && idTemplateRelatorio != null && dadosEmail.get("para") != null && !"".equals(dadosEmail.get("para"))) {
			List<Map<String, Object>> listaMapDadosRelatorioDevolucao = monitoramentoNotasFiscaisCCTService.findDadosRelatorioDevolucao(idEnvioCteCliente, idClienteCCT);
			List<Map<String, Object>> dadosRelatorio = findaDadosItensRelatorioFUPDevolucao(listaMapDadosRelatorioDevolucao);
			
			Map<String, Object> mapReportFUPFile = executeReplaceDevolucao(idTemplateRelatorio, dadosRelatorio);
			String nomeArquivo = (String) mapReportFUPFile.get("nomeArquivo");
			File arquivoTemplateDevolucao = (File) mapReportFUPFile.get("reportDevolucaoFile"); 
			
			if(arquivoTemplateDevolucao != null && dadosRelatorio != null && !dadosRelatorio.isEmpty()){
				if(arquivoTemplateDevolucao.length() > tamanhoAnexos.longValue()){
					executeEnviarEmailProblemaRelatorioFUP(idClienteCCT, "LMS-10086");
				} else {
					executeEnviarEmailRelatorioFUPDevolucao(dadosEmail, arquivoTemplateDevolucao, nomeArquivo);
				}
			}
		}
	}
	
	private Map<String, Object> executeReplaceDevolucao(Long idTemplateRelatorio, List<Map<String, Object>> listaMapDadosItensRelatorioDevolucao) {
		try {
			Map mapArquivo = templateRelatorioService.findArquivo(idTemplateRelatorio);
			String nomeArquivo = (String) mapArquivo.get("nomeArquivo");
			DevolucaoReportRunnerCCT devolucaoReportRunnerCCT = new DevolucaoReportRunnerCCT((byte[]) mapArquivo.get("arquivo"), nomeArquivo, new ReportExecutionManager().generateOutputDir());
			devolucaoReportRunnerCCT.setDadosItens(listaMapDadosItensRelatorioDevolucao);
			File originalReportDevolucaoFile = devolucaoReportRunnerCCT.generateReportFile();
			
			Map<String, Object> retorno = new HashMap<String, Object>();
			retorno.put("nomeArquivo", ArquivoUtils.getNomeArquivoZip(nomeArquivo));
			retorno.put("reportDevolucaoFile", ArquivoUtils.compactarArquivo(originalReportDevolucaoFile));
			return retorno;
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
	}
	
	@AssynchronousMethod(name="sim.executeRelatorioFUP", type=BatchType.BATCH_SERVICE, feedback=BatchFeedbackType.ON_ERROR)
	public void executeRelatorioFUP(){
		Long tamanhoAnexos = getLimiteTamanhoEmail();
		
		List<Long> clientesCCT = clienteService.findIdsClienteCCT();
		for (Long idClienteCCT : clientesCCT) {
			generateRelatorioFUP(idClienteCCT, tamanhoAnexos);
		}
	}
	
	private void generateRelatorioFUP(Long idClienteCCT, Long tamanhoAnexos){
		Cliente clienteRemetente = populateClienteById(idClienteCCT);
		Map<String, Object> dadosEmail = generateEmail("F", clienteRemetente, null, null, null);
		Long idEnvioCteCliente = (Long) dadosEmail.get("idEnvioCteCliente");
		Long idTemplateRelatorio = (Long) dadosEmail.get("idTemplateRelatorio");

		if (idEnvioCteCliente != null && idTemplateRelatorio != null && dadosEmail.get("para") != null && !"".equals(dadosEmail.get("para"))) {
			List<Map<String, Object>> listaMapDadosRelatorioFUP = monitoramentoNotasFiscaisCCTService.findDadosRelatorioFUP(idEnvioCteCliente, idClienteCCT);
			findDadosComplementaresRelatorioFUP(listaMapDadosRelatorioFUP);
			List<Map<String, Object>> dadosRelatorio = findaDadosItensRelatorioFUPDevolucao(listaMapDadosRelatorioFUP);
			
			Map<String, Object> mapReportFUPFile = executeReplaceFUP(idTemplateRelatorio, dadosRelatorio);
			String nomeArquivo = (String) mapReportFUPFile.get("nomeArquivo");
			File arquivoTemplateFUP = (File) mapReportFUPFile.get("reportFUPFile"); 
			
			if(arquivoTemplateFUP != null && dadosRelatorio != null && !dadosRelatorio.isEmpty()){
				if(arquivoTemplateFUP.length() > tamanhoAnexos.longValue()){
					executeEnviarEmailProblemaRelatorioFUP(idClienteCCT, "LMS-10084");
				} else {
					executeEnviarEmailRelatorioFUPDevolucao(dadosEmail, arquivoTemplateFUP, nomeArquivo);
				}
			}
		}
	}
	
	private List<Map<String, Object>> findaDadosItensRelatorioFUPDevolucao(List<Map<String, Object>> listaMapDadosRelatorioFUPDevolucao){
		List<Map<String, Object>> retorno = new ArrayList<Map<String, Object>>();
		
		if(listaMapDadosRelatorioFUPDevolucao != null && !listaMapDadosRelatorioFUPDevolucao.isEmpty()){
			for (Map<String, Object> mapDadosRelatorioFUPDevolucao : listaMapDadosRelatorioFUPDevolucao) {
				adicionarDadosItens(mapDadosRelatorioFUPDevolucao, retorno);
			}
		}
		
		return retorno;
	}
	
	private void adicionarDadosItens(Map<String, Object> mapDadosRelatorioFUPDevolucao, List<Map<String, Object>> retorno){
		List<String> chavesNFE = new ArrayList<String>();
		chavesNFE.add((String) mapDadosRelatorioFUPDevolucao.get("nr_chave"));
		List<Map<String, Object>> mapItens = agendamentoEntregaService.findDadosItensRelatoriosCCT(chavesNFE);
		
		if(mapItens != null && !mapItens.isEmpty()){
			for (Map<String, Object> mapItem : mapItens) {
				mapItem.putAll(mapDadosRelatorioFUPDevolucao);
				retorno.add(mapItem);
			}
		} else {
			retorno.add(mapDadosRelatorioFUPDevolucao);
		}
	}
	
	private void findDadosComplementaresRelatorioFUP(List<Map<String, Object>> listaMapDadosRelatorioFUP){
		if(listaMapDadosRelatorioFUP != null && !listaMapDadosRelatorioFUP.isEmpty()){
			for (Map<String, Object> mapaDadosRelatorioFUP : listaMapDadosRelatorioFUP) {
				Long idMonitoramentoCCT = (Long) mapaDadosRelatorioFUP.get("idMonitoramentoCCT");
				
				List<Map<String, Object>> datasSolicitacao = monitoramentoNotasFiscaisCCTService.findDatasEventoMonitoramentoRelatorioFUP(idMonitoramentoCCT, "AA");
				addChavesDataRelatorioFUP(mapaDadosRelatorioFUP, datasSolicitacao, "dt_solic_agend_");
				
				List<Map<String, Object>> datasConfirmacao = monitoramentoNotasFiscaisCCTService.findDatasEventoMonitoramentoRelatorioFUP(idMonitoramentoCCT, "AG");
				addChavesDataRelatorioFUP(mapaDadosRelatorioFUP, datasConfirmacao, "dt_conf_agend_");
				
				List<Map<String, Object>> datasAgendamento = monitoramentoNotasFiscaisCCTService.findDatasAgendamentoRelatorioFUP(idMonitoramentoCCT);
				addChavesDataRelatorioFUP(mapaDadosRelatorioFUP, datasAgendamento, "dt_agend_");
			}
		}
	}
	
	private void addChavesDataRelatorioFUP(Map<String, Object> mapaDadosRelatorioFUP, List<Map<String, Object>> mapaDatas, String nmChave) {
		if (mapaDatas != null && !mapaDatas.isEmpty()) {
			if (mapaDatas.size() == 1) {
				mapaDadosRelatorioFUP.put(nmChave + 1, mapaDatas.get(0));
			} else if (mapaDatas.size() == 2) {
				mapaDadosRelatorioFUP.put(nmChave + 2, mapaDatas.get(0));
				mapaDadosRelatorioFUP.put(nmChave + 1, mapaDatas.get(1));
			} else if (mapaDatas.size() >= 3) {
				mapaDadosRelatorioFUP.put(nmChave + 3, mapaDatas.get(0));
				mapaDadosRelatorioFUP.put(nmChave + 2, mapaDatas.get(1));
				mapaDadosRelatorioFUP.put(nmChave + 1, mapaDatas.get(2));
			}
		}
	}
	
	private Map<String, Object> executeReplaceFUP(Long idTemplateRelatorio, List<Map<String, Object>> listaMapDadosItensRelatorioFUP) {
		try {
			Map mapArquivo = templateRelatorioService.findArquivo(idTemplateRelatorio);
			String nomeArquivo = (String) mapArquivo.get("nomeArquivo");
			FUPReportRunnerCCT fupReportRunnerCCT = new FUPReportRunnerCCT((byte[]) mapArquivo.get("arquivo"), nomeArquivo, new ReportExecutionManager().generateOutputDir());
			fupReportRunnerCCT.setDadosItens(listaMapDadosItensRelatorioFUP);
			File originalReportFUPFile = fupReportRunnerCCT.generateReportFile();
			
			Map<String, Object> retorno = new HashMap<String, Object>();
			retorno.put("nomeArquivo", ArquivoUtils.getNomeArquivoZip(nomeArquivo));
			retorno.put("reportFUPFile", ArquivoUtils.compactarArquivo(originalReportFUPFile));
			return retorno;
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
	}

	private void executeEnviarEmailProblemaRelatorioFUP(Long idClienteCCT, String chaveMensagem){
		Pessoa pessoaClienteCCt = pessoaDAO.findById(idClienteCCT);
		String assunto = configuracoesFacade.getMensagem(chaveMensagem, new Object[]{pessoaClienteCCt.getNmPessoa()});
		String texto = configuracoesFacade.getMensagem("LMS-10085");
		
		Map<String, Object> dadosProblemaEmail = new HashMap<String, Object>();
		dadosProblemaEmail.put("de", getEnderecoEmailRemetente());
		dadosProblemaEmail.put("para", (String) parametroGeralDAO.findByNomeParametro("DS_DESTINATARIO_EMAIL", false).getDsConteudo());
		dadosProblemaEmail.put("assunto", assunto);
		dadosProblemaEmail.put("texto", texto);
		
		executeEnviarEmailRelatorioFUPDevolucao(dadosProblemaEmail, null, null);
	}
	
	private void executeEnviarEmailRelatorioFUPDevolucao(final Map<String, Object> dadosEmail, final File arquivoTemplateFUPDevolucao, final String nomeArquivo) {
		List<MailAttachment> mailAttachments = new ArrayList<MailAttachment>();

		if(arquivoTemplateFUPDevolucao != null){
			MailAttachment mailAttachment = new MailAttachment();
			mailAttachment.setName(nomeArquivo);
			mailAttachment.setData(FileUtils.readFile(arquivoTemplateFUPDevolucao));
			mailAttachments.add(mailAttachment);
		}
		
		this.sendMail(
			(String) dadosEmail.get("para"),
			(String) dadosEmail.get("de"),
			(String) dadosEmail.get("assunto"),
			(String) dadosEmail.get("texto"),
			mailAttachments
		);
	}
	
	private void sendMail(String strTo, String strFrom, String strSubject, 
	String body, List<MailAttachment> mailAttachmentList) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		mail.setAttachements(mailAttachmentList.toArray(new MailAttachment[mailAttachmentList.size()]));

		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}
	
	private Cliente populateClienteById(Long idCliente){
		Cliente cliente = new Cliente();
		cliente.setIdCliente(idCliente);
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(idCliente);
		cliente.setPessoa(pessoa);
		return cliente;
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@AssynchronousMethod(name="sim.executeEnvioAgendamentoAutomatico", type=BatchType.BATCH_SERVICE, feedback=BatchFeedbackType.ON_ERROR)
	public void executeEnvioAgendamentoAutomatico() throws Exception {
		Locale locale = new Locale("pt","BR");
		List<Map> listaMapDadosMonitoramento = monitoramentoNotasFiscaisCCTService.findMonitoramentosEnvioAgendamentoAutomatico();
		Map<ClienteKey, AgendamentoAuxiliar> mapRemententeDestinatarioAgendamento = agruparAgendamentoPorRemetenteDestinatario(listaMapDadosMonitoramento);
		
		if(mapRemententeDestinatarioAgendamento != null && !mapRemententeDestinatarioAgendamento.isEmpty()){
			Long numeroAnexos = getLimiteNroAnexoEmail();
			Long tamanhoAnexos = getLimiteTamanhoEmail();
			
			for (Map.Entry<ClienteKey, AgendamentoAuxiliar> entryClientes : mapRemententeDestinatarioAgendamento.entrySet()) {
			
				ClienteKey clienteKey = entryClientes.getKey();
				AgendamentoAuxiliar agendamentoAuxiliar =  entryClientes.getValue();
				
				Cliente clienteRemetente = populateClienteById(clienteKey.getIdClienteRemetente());
				Cliente clienteDestinatario = populateClienteById(clienteKey.getIdClienteDestinatario());
				
				getServiceParaNovaTransacao().executeAgendamentoComNovaTransacao(numeroAnexos, tamanhoAnexos, clienteRemetente, clienteDestinatario, agendamentoAuxiliar, locale);
			}
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	protected void executeAgendamentoComNovaTransacao(Long numeroAnexos, Long tamanhoAnexos, Cliente clienteRemetente, Cliente clienteDestinatario,
			AgendamentoAuxiliar agendamentoAuxiliar, Locale locale) throws Exception {
		Session session = envioCteClienteDAO.getAdsmHibernateTemplate().getSessionFactory().openSession();
		session.beginTransaction();
		
		List<AnexoEmail> anexos = new ArrayList<AnexoEmail>();
		AgendamentoEntrega agendamentoEntrega = generateAgendamentoEntrega(agendamentoAuxiliar);
		generateAgendamentoMonitoramentoCCT(agendamentoEntrega, agendamentoAuxiliar);

		Map<String, Object> dadosEmail = generateEmail("A", clienteRemetente, clienteDestinatario, agendamentoEntrega.getIdAgendamentoEntrega(), agendamentoAuxiliar.getDtPrevEntrega());
		anexos.addAll(findAnexoTemplate(agendamentoEntrega.getIdAgendamentoEntrega(), (Long) dadosEmail.get("idTemplateRelatorio")));
		anexos.addAll(findAnexosDanfeXml(agendamentoAuxiliar.getNrChaves(), (Boolean) dadosEmail.get("anexarDanfe"), (Boolean) dadosEmail.get("anexarXml"), locale));
		
		Set<String> chavesPorEmail = new HashSet<String>();
		List<AnexoEmail> anexosPorEmail = new ArrayList<AnexoEmail>();
		int contadorPartesEmail = 0;
		int totalPartesEmail = 0;
		
		if (!anexos.isEmpty() && !(anexos.size() == 1 && anexos.get(0).getNrChave() == null)) {
			totalPartesEmail = getTotalPartesEmail(anexos,  numeroAnexos, tamanhoAnexos);
			
			for(AnexoEmail anexoEmail : anexos){

				if (validarAnexosPorEmail(anexosPorEmail, anexoEmail, numeroAnexos, tamanhoAnexos)) {
					chavesPorEmail.add(anexoEmail.getNrChave());
					anexosPorEmail.add(anexoEmail);

				} else {
					if(!anexosPorEmail.isEmpty()){
						generateEventoRelatorioAgendamento(agendamentoEntrega.getTpAgendamento().getValue(), chavesPorEmail);
						contadorPartesEmail++;
						executeEnviarEmailRelatorioAgendamento(dadosEmail, anexosPorEmail, chavesPorEmail, contadorPartesEmail, totalPartesEmail);
					}
					chavesPorEmail = new HashSet<String>();
					anexosPorEmail = new ArrayList<AnexoEmail>();

					// Um único anexo não deve ultrapassar os limites, logo já é inserido aqui sem a validação.
					chavesPorEmail.add(anexoEmail.getNrChave());
					anexosPorEmail.add(anexoEmail);
				}
			}

			executeAgendamento(anexosPorEmail, agendamentoEntrega, chavesPorEmail, contadorPartesEmail, totalPartesEmail, dadosEmail);
			
		} else {
			//Caso tenha somente o anexo com o template, este anexo não possui chave, sendo assim, todas as chaves devem ser atualizadas.
			if(anexos.size() == 1 && anexos.get(0).getNrChave() == null){
				anexosPorEmail.addAll(anexos);
			}
			
			chavesPorEmail.addAll(agendamentoAuxiliar.getNrChaves());
			generateEventoRelatorioAgendamento(agendamentoEntrega.getTpAgendamento().getValue(), chavesPorEmail);
			executeEnviarEmailRelatorioAgendamento(dadosEmail, anexosPorEmail, chavesPorEmail, contadorPartesEmail, totalPartesEmail);
		}
		
		session.flush();
		session.getTransaction().commit();
		session.close();
	}

	private void executeAgendamento(List<AnexoEmail> anexosPorEmail, AgendamentoEntrega agendamentoEntrega, Set<String> chavesPorEmail,
			int contadorPartesEmail, int totalPartesEmail, Map<String, Object> dadosEmail) {
		if (!anexosPorEmail.isEmpty()) {
			generateEventoRelatorioAgendamento(agendamentoEntrega.getTpAgendamento().getValue(), chavesPorEmail);
			if (contadorPartesEmail != 0) {
				contadorPartesEmail++;
			}
			executeEnviarEmailRelatorioAgendamento(dadosEmail, anexosPorEmail, chavesPorEmail, contadorPartesEmail, totalPartesEmail);
		}
	}
	
	private void generateEventoRelatorioAgendamento(String tpAgendamento, Set<String> chaves) {
		String tpOrigem = null;
		if ("TA".equals(tpAgendamento)) {
			tpOrigem = "TA";
		} else if ("AT".equals(tpAgendamento)) {
			tpOrigem = "AG";
		}

		for (String nrChave : chaves) {
			if (nrChave != null) {
				monitoramentoNotasFiscaisCCTService.storeEvento(tpOrigem, nrChave, null, null, null, OBSERVACAO_EVENTO, null, SessionUtils.getUsuarioLogado());
			}
		}
	}
	
	private int getTotalPartesEmail(List<AnexoEmail> anexos, Long numeroAnexos, Long tamanhoAnexos) {
		int totalPartesEmail = 0;
		List<AnexoEmail> anexosPorEmail = new ArrayList<AnexoEmail>();
		
		for (AnexoEmail anexoEmail : anexos) {
			if (validarAnexosPorEmail(anexosPorEmail, anexoEmail, numeroAnexos, tamanhoAnexos)) {
				anexosPorEmail.add(anexoEmail);
			} else {
				if (!anexosPorEmail.isEmpty()) {
					totalPartesEmail++;
				}
				anexosPorEmail.clear();
				anexosPorEmail.add(anexoEmail);
			}
		}

		if (!anexosPorEmail.isEmpty() && totalPartesEmail != 0) {
			totalPartesEmail++;
		}

		return totalPartesEmail;
	}
	
	/**
	 * 
	 * @param dadosEmail
	 * @param anexosPorEmail
	 * @param notasFiscais
	 * @param chaves
	 */
	private void executeEnviarEmailRelatorioAgendamento(final Map<String, Object> dadosEmail, final List<AnexoEmail> anexosPorEmail,
	final Set<String> chaves, final int contadorPartesEmail, final int totalPartesEmail) {
		try {
			String assunto = (String) dadosEmail.get("assunto");
			if (contadorPartesEmail > 0) {
				assunto += " - Parte " + contadorPartesEmail + "/" + totalPartesEmail;
			}

			List<MailAttachment> mailAttachments = new ArrayList<MailAttachment>();
			for (AnexoEmail anexoEmail : anexosPorEmail) {
				if (anexoEmail.getArquivoDanfe() != null) {
					MailAttachment mailAttachment = new MailAttachment();
					mailAttachment.setName(anexoEmail.getNomeArquivoDanfe());
					mailAttachment.setData(FileUtils.readFile(anexoEmail.getArquivoDanfe()));
					mailAttachments.add(mailAttachment);
				}

				if (anexoEmail.getArquivoTemplate() != null) {
					MailAttachment mailAttachment = new MailAttachment();
					mailAttachment.setName(anexoEmail.getNomeArquivoTemplate());
					mailAttachment.setData(FileUtils.readFile(anexoEmail.getArquivoTemplate()));
					mailAttachments.add(mailAttachment);
				}

				if (anexoEmail.getArquivoXml() != null) {
					MailAttachment mailAttachment = new MailAttachment();
					mailAttachment.setName(anexoEmail.getNomeArquivoXml());
					mailAttachment.setData(FileUtils.readFile(anexoEmail.getArquivoXml()));
					mailAttachments.add(mailAttachment);
				}
			}
				
			this.sendMail(
				(String) dadosEmail.get("para"),
				(String) dadosEmail.get("de"),
				(String) assunto,
				(String) dadosEmail.get("texto"),
				mailAttachments
			);
			
		} catch (Exception e) {
			LOG.error(e);
			for (String nrChave : chaves) {
				if (nrChave != null) {
					monitoramentoNotasFiscaisCCTService.storeEvento("FA", nrChave, null, null, null, null, null, SessionUtils.getUsuarioLogado());
				}
			}
		}
	}
	
	/**
	 * 
	 * @param agendamentoEntrega
	 * @param agendamentoAuxiliar
	 */
	private void generateAgendamentoMonitoramentoCCT(AgendamentoEntrega agendamentoEntrega, AgendamentoAuxiliar agendamentoAuxiliar){
		for(Long idMonitoramentoCCT : agendamentoAuxiliar.getIdsMonitoramentoCCT()){
			AgendamentoMonitCCT agendamentoMonitCCT = new AgendamentoMonitCCT();
			agendamentoMonitCCT.setAgendamentoEntrega(agendamentoEntrega);
			MonitoramentoCCT monitoramentoCCT = new MonitoramentoCCT();
			monitoramentoCCT.setIdMonitoramentoCCT(idMonitoramentoCCT);
			agendamentoMonitCCT.setMonitoramentoCCT(monitoramentoCCT);
			parametroGeralDAO.getAdsmHibernateTemplate().save(agendamentoMonitCCT);
		}
	}
	
	/**
	 * 
	 * @param agendamentoAuxiliar
	 * @return
	 */
	private AgendamentoEntrega generateAgendamentoEntrega(AgendamentoAuxiliar agendamentoAuxiliar){
		AgendamentoEntrega agendamentoEntrega = new AgendamentoEntrega();
		
		Filial filialAtendeOperacional = new Filial();
		filialAtendeOperacional.setIdFilial(agendamentoAuxiliar.getIdFilialAtendeOperacional());
		agendamentoEntrega.setFilial(filialAtendeOperacional);
		agendamentoEntrega.setDhContato(JTDateTimeUtils.getDataHoraAtual());
		agendamentoEntrega.setNmContato("Batch");
		agendamentoEntrega.setNrTelefone("11111111");
		agendamentoEntrega.setUsuarioByIdUsuarioCriacao(SessionUtils.getUsuarioLogado());
		agendamentoEntrega.setBlCartao(false);
		
		if(agendamentoAuxiliar.getBlConfirmaAgendamento() != null && agendamentoAuxiliar.getBlConfirmaAgendamento()){
			agendamentoEntrega.setTpAgendamento(new DomainValue("TA"));
			agendamentoEntrega.setTpSituacaoAgendamento(new DomainValue("F"));
			
		} else {
			agendamentoEntrega.setTpAgendamento(new DomainValue("AT"));
			agendamentoEntrega.setTpSituacaoAgendamento(new DomainValue("A"));
			agendamentoEntrega.setTurno(findTurnoAgendamento(agendamentoAuxiliar.getIdFilialAtendeOperacional()));
			agendamentoEntrega.setDtAgendamento(agendamentoAuxiliar.getDtPrevEntrega());
			
		}

		parametroGeralDAO.getAdsmHibernateTemplate().save(agendamentoEntrega);
		return agendamentoEntrega;
	}
	
	/**
	 * 
	 * @param idFilialAtendeComercial
	 * @return
	 */
	private Turno findTurnoAgendamento(Long idFilialAtendeComercial){
		return turnoService.findTurnoByIdFilialDsTurno(idFilialAtendeComercial.toString(), "Comercial", JTDateTimeUtils.getDataAtual());
	}
	
	private Map<ClienteKey, AgendamentoAuxiliar> agruparAgendamentoPorRemetenteDestinatario(List<Map> listaMapDadosMonitoramento) {
		Map<ClienteKey, AgendamentoAuxiliar> retorno = new HashMap<ClienteKey, AgendamentoAuxiliar>();

		for (Map map : listaMapDadosMonitoramento) {
			ClienteKey clienteKey = new ClienteKey((Long) map.get("idClienteRemetente"), (Long) map.get("idClienteDestinatario"));

			if (retorno.containsKey(clienteKey)) {
				retorno.get(clienteKey).setMaxDtPrevEntrega((YearMonthDay) map.get("dtPrevEntrega"));
				retorno.get(clienteKey).getIdsMonitoramentoCCT().add((Long) map.get("idMonitoramentoCCT"));
				retorno.get(clienteKey).getNrChaves().add((String) map.get("nrChave"));

			} else {
				AgendamentoAuxiliar agendamentoAuxiliar = new AgendamentoAuxiliar((Long) map.get("idClienteRemetente"),
						(Long) map.get("idClienteDestinatario"), (Long) map.get("idFilialAtendeOperacional"), (YearMonthDay) map.get("dtPrevEntrega"), (Boolean) map.get("blConfirmaAgendamento") );
				agendamentoAuxiliar.getIdsMonitoramentoCCT().add((Long) map.get("idMonitoramentoCCT"));
				agendamentoAuxiliar.getNrChaves().add((String) map.get("nrChave"));
				retorno.put(clienteKey, agendamentoAuxiliar);
			}
		}

		return retorno;
	}
	
	private class AnexoEmail {
		private String nrChave;
		
		private String nomeArquivoXml;
		private File arquivoXml;
		
		private String nomeArquivoDanfe;
		private File arquivoDanfe;
		
		private String nomeArquivoTemplate;
		private File arquivoTemplate;

		
		AnexoEmail(String nrChave) {
			this.nrChave = nrChave;
		}
		
		public long getTotalArquivos(){
			long totalArquivos = 0;
			
			if(arquivoXml != null){
				totalArquivos++;
			}
			
			if(arquivoDanfe != null){
				totalArquivos++; 
			}
			
			if(arquivoTemplate != null){
				totalArquivos++; 
			}
			
			return totalArquivos;
		}
		
		
		public long getTotalLength(){
			long totalLength = 0;
			
			if(arquivoXml != null){
				totalLength += arquivoXml.length(); 
			}
			
			if(arquivoDanfe != null){
				totalLength += arquivoDanfe.length(); 
			}
			
			if(arquivoTemplate != null){
				totalLength += arquivoTemplate.length(); 
			}
			
			return totalLength;
		}

		public String getNrChave() {
			return nrChave;
		}

		public void setNrChave(String nrChave) {
			this.nrChave = nrChave;
		}

		public String getNomeArquivoXml() {
			return nomeArquivoXml;
		}

		public void setNomeArquivoXml(String nomeArquivoXml) {
			this.nomeArquivoXml = nomeArquivoXml;
		}

		public File getArquivoXml() {
			return arquivoXml;
		}

		public void setArquivoXml(File arquivoXml) {
			this.arquivoXml = arquivoXml;
		}

		public String getNomeArquivoDanfe() {
			return nomeArquivoDanfe;
		}

		public void setNomeArquivoDanfe(String nomeArquivoDanfe) {
			this.nomeArquivoDanfe = nomeArquivoDanfe;
		}

		public File getArquivoDanfe() {
			return arquivoDanfe;
		}

		public void setArquivoDanfe(File arquivoDanfe) {
			this.arquivoDanfe = arquivoDanfe;
		}

		public String getNomeArquivoTemplate() {
			return nomeArquivoTemplate;
		}

		public void setNomeArquivoTemplate(String nomeArquivoTemplate) {
			this.nomeArquivoTemplate = nomeArquivoTemplate;
		}

		public File getArquivoTemplate() {
			return arquivoTemplate;
		}

		public void setArquivoTemplate(File arquivoTemplate) {
			this.arquivoTemplate = arquivoTemplate;
		}

	}
	
	/**
	 * 
	 * @author RafaelKF
	 *
	 */
	private static class AgendamentoAuxiliar{
		private Long idClienteRemetente;
		private Long idClienteDestinatario;
		private Long idFilialAtendeOperacional;
		private Boolean blConfirmaAgendamento;
		private YearMonthDay dtPrevEntrega;
		private List<Long> idsMonitoramentoCCT = new ArrayList<Long>();
		private List<String> nrChaves = new ArrayList<String>();
		
		AgendamentoAuxiliar(Long idClienteRemetente, Long idClienteDestinatario, Long idFilialAtendeOperacional, YearMonthDay dtPrevEntrega, Boolean blConfirmaAgendamento){
			this.idClienteRemetente = idClienteRemetente;
			this.idClienteDestinatario = idClienteDestinatario;
			this.idFilialAtendeOperacional = idFilialAtendeOperacional;
			this.dtPrevEntrega = dtPrevEntrega;
			this.blConfirmaAgendamento = blConfirmaAgendamento;
		}
		
		/**
		 * Caso a dtPrevEntrega passada seja maior que a dtPrevEntrega atual, seta a atual com a nova.
		 *  
		 * @param dtPrevEntrega
		 */
		public void setMaxDtPrevEntrega(YearMonthDay dtPrevEntrega) {
			if(dtPrevEntrega != null){
				if(this.dtPrevEntrega == null){
					this.dtPrevEntrega = dtPrevEntrega;
				} else if(dtPrevEntrega.isAfter(this.getDtPrevEntrega())){
					this.dtPrevEntrega = dtPrevEntrega;
				}
			}
		}

		public Long getIdClienteRemetente() {
			return idClienteRemetente;
		}
		public void setIdClienteRemetente(Long idClienteRemetente) {
			this.idClienteRemetente = idClienteRemetente;
		}
		public Long getIdClienteDestinatario() {
			return idClienteDestinatario;
		}
		public void setIdClienteDestinatario(Long idClienteDestinatario) {
			this.idClienteDestinatario = idClienteDestinatario;
		}
		public YearMonthDay getDtPrevEntrega() {
			return dtPrevEntrega;
		}
		public void setDtPrevEntrega(YearMonthDay dtPrevEntrega) {
			this.dtPrevEntrega = dtPrevEntrega;
		}
		public List<Long> getIdsMonitoramentoCCT() {
			return idsMonitoramentoCCT;
		}
		public void setIdsMonitoramentoCCT(List<Long> idsMonitoramentoCCT) {
			this.idsMonitoramentoCCT = idsMonitoramentoCCT;
		}
		public List<String> getNrChaves() {
			return nrChaves;
		}
		public void setNrChaves(List<String> nrChaves) {
			this.nrChaves = nrChaves;
		}

		public Long getIdFilialAtendeOperacional() {
			return idFilialAtendeOperacional;
		}

		public void setIdFilialAtendeOperacional(Long idFilialAtendeOperacional) {
			this.idFilialAtendeOperacional = idFilialAtendeOperacional;
		}

		public Boolean getBlConfirmaAgendamento() {
			return blConfirmaAgendamento;
		}

		public void setBlConfirmaAgendamento(Boolean blConfirmaAgendamento) {
			this.blConfirmaAgendamento = blConfirmaAgendamento;
		}
	}
	
	/**
	 * Itera a lista de chaves da NFe informadas, busca arquivos da Danfe e Xml conforme necessário.
	 * 
	 * @param nrChaves
	 * @param anexarDanfe
	 * @param anexarXml
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private List<AnexoEmail> findAnexosDanfeXml(List<String> nrChaves, Boolean anexarDanfe, Boolean anexarXml, Locale locale) throws Exception{
		List<AnexoEmail> anexos = new ArrayList<AnexoEmail>();
		for (String nrChave : nrChaves) {
			AnexoEmail anexoEmail = new AnexoEmail(nrChave);
			
			if (anexarDanfe) {
				String xmlString = notaFiscalEletronicaService.findNfeXMLFromEDIToString(nrChave);
				GerarDanfeService gerarDanfeService = new GerarDanfeService(locale, null);
				File arquivoDanfe = gerarDanfeService.execute(xmlString, locale);
				anexoEmail.setArquivoDanfe(arquivoDanfe);
				anexoEmail.setNomeArquivoDanfe(NFEUtils.getNumeroNotaFiscalByChave(nrChave) + EXTENSAO_PDF);
			}
			if (anexarXml) {
				File arquivoXml = findXml(nrChave);
				anexoEmail.setArquivoXml(arquivoXml);
				anexoEmail.setNomeArquivoXml(arquivoXml.getName());
			}
			if(anexarDanfe || anexarXml){
				anexos.add(anexoEmail);
			}
		}
		return anexos;
	}
	
	/**
	 * Anexa o relatório de agendamento com template, um por Agendamento.
	 * 
	 * @param idAgendamentoEntrega
	 * @param idTemplateRelatorio
	 * @param anexos
	 */
	private List<AnexoEmail> findAnexoTemplate(Long idAgendamentoEntrega, Long idTemplateRelatorio) {
		List<AnexoEmail> anexoTemplate = new ArrayList<AnexoEmail>();
		if (idTemplateRelatorio != null) {
			Map<String, Object> mapReportFileAgendamento = executeReplaceAgendamento(idAgendamentoEntrega, idTemplateRelatorio);
			AnexoEmail anexoEmail = new AnexoEmail(null);
			anexoEmail.setNomeArquivoTemplate((String) mapReportFileAgendamento.get("nomeArquivo"));
			anexoEmail.setArquivoTemplate((File) mapReportFileAgendamento.get("reportFileAgendamento"));
			anexoTemplate.add(anexoEmail);
		}
		return anexoTemplate;
	}
	
	/**
	 * Realiza o envio de emails para clientes solicitando o pagamento de ICMS.
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@AssynchronousMethod(name="sim.executeEnvioSolicitacaoPagtoICMS", type=BatchType.BATCH_SERVICE, feedback=BatchFeedbackType.ON_ERROR)
	public void executeEnvioSolicitacaoPagtoICMS() throws Exception {
		Locale locale = new Locale("pt","BR");
		List<Map> listaMapDadosMonitoramento = findMonitoramentosEnvioSolicitacaoPagtoICMS();
		Map<ClienteKey, List<String>> mapRemetenteDestinatario = agruparPorRemetenteDestinatario(listaMapDadosMonitoramento);

		Long numeroAnexos = getLimiteNroAnexoEmail();
		Long tamanhoAnexos = getLimiteTamanhoEmail();

		for (Map.Entry<ClienteKey, List<String>> entryClientes : mapRemetenteDestinatario.entrySet()) {
			ClienteKey clienteKey = entryClientes.getKey();
			
			Cliente clienteRemetente = populateClienteById(clienteKey.getIdClienteRemetente());
			Cliente clienteDestinatario = populateClienteById(clienteKey.getIdClienteDestinatario());
						
			HashMap<String, Object> dadosEmail = generateEmail("I", clienteRemetente, clienteDestinatario, null, null);
			Boolean anexarDanfe = (Boolean) dadosEmail.get("anexarDanfe"); 
			Boolean anexarXml = (Boolean) dadosEmail.get("anexarXml");
			
			List<AnexoEmail> anexos = findAnexosDanfeXml(entryClientes.getValue(), anexarDanfe, anexarXml, locale);
			
			String notasFiscais = "";
			List<String> chaves = new ArrayList<String>();
			List<AnexoEmail> anexosPorEmail = new ArrayList<AnexoEmail>();
			if(!anexos.isEmpty()){
				for (AnexoEmail anexoEmail : anexos) {
					
					if(validarAnexosPorEmail(anexosPorEmail, anexoEmail, numeroAnexos, tamanhoAnexos)){
						notasFiscais = montarNumeroNotaFiscal(anexoEmail.getNrChave(), notasFiscais);
						chaves.add(anexoEmail.getNrChave());
						anexosPorEmail.add(anexoEmail);
						
					} else {
						executeEnviarEmailRelatorioICMS(dadosEmail, anexosPorEmail, notasFiscais, chaves);
						notasFiscais = "";
						chaves = new ArrayList<String>();
						anexosPorEmail = new ArrayList<AnexoEmail>();
						
						//Um único anexo não deve ultrapassar os limites, logo já é inserido aqui sem a validação.
						notasFiscais = montarNumeroNotaFiscal(anexoEmail.getNrChave(), notasFiscais);
						chaves.add(anexoEmail.getNrChave());
						anexosPorEmail.add(anexoEmail);
					}
				}
				
				if(!anexosPorEmail.isEmpty()){
					executeEnviarEmailRelatorioICMS(dadosEmail, anexosPorEmail, notasFiscais, chaves);
				}
				
			} else {
				// Caso não tenham sido marcados os checkboxs, não terá nenhum anexo, mas deve ser enviando o email contendo as notas.
				//Itera todas as chaves que estão nos monitoramentos
				for(String nrChave : entryClientes.getValue()){
					notasFiscais = montarNumeroNotaFiscal(nrChave, notasFiscais);
					chaves.add(nrChave);
				}
				
				executeEnviarEmailRelatorioICMS(dadosEmail, anexosPorEmail, notasFiscais, chaves);
			}
		}
	}
	
	/**
	 * Envia o email com os anexos referentes ao relatório de ICMS.
	 * Grava evento de sucesso ou erro, abrindo nova transação.
	 * 
	 * @param dadosEmail
	 * @param anexosPorEmail
	 * @param notasFiscais
	 */
	private void executeEnviarEmailRelatorioICMS(final HashMap<String, Object> dadosEmail, final List<AnexoEmail> anexosPorEmail, final String notasFiscais, List<String> chaves) {
		if(dadosEmail.get("para") == null || "".equals(dadosEmail.get("para"))){
			getServiceParaNovaTransacao().executeGravarEventoComNovaTransacao("PA", chaves, OBSERVACAO_EVENTO);
			return;
		}
		
		try{
			List<MailAttachment> mailAttachmentList = new ArrayList<MailAttachment>();
			for(AnexoEmail anexoEmail : anexosPorEmail){
				if (anexoEmail.getArquivoDanfe() != null) {
					MailAttachment mailAttachment = new MailAttachment();
					mailAttachment.setName(anexoEmail.getNomeArquivoDanfe());
					mailAttachment.setData(FileUtils.readFile(anexoEmail.getArquivoDanfe()));
					mailAttachmentList.add(mailAttachment);
				}

				if (anexoEmail.getArquivoXml() != null) {
					MailAttachment mailAttachment = new MailAttachment();
					mailAttachment.setName(anexoEmail.getNomeArquivoXml());
					mailAttachment.setData(FileUtils.readFile(anexoEmail.getArquivoXml()));
					mailAttachmentList.add(mailAttachment);
				}
			}

			this.sendMail(
				(String) dadosEmail.get("para"),
				(String) dadosEmail.get("de"),
				(String) dadosEmail.get("assunto"),
				MessageFormat.format(
					(String) dadosEmail.get("texto"), 
					(Object[]) new Object[]{null, null, null, notasFiscais}
				),
				mailAttachmentList
			);

			getServiceParaNovaTransacao().executeGravarEventoComNovaTransacao("SI", chaves, null);
		} catch(Exception e){
			LOG.error(e);
			getServiceParaNovaTransacao().executeGravarEventoComNovaTransacao("FE", chaves, null);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	protected void executeGravarEventoComNovaTransacao(String tpOrigem, List<String> chaves, String observacao){
		Session session = envioCteClienteDAO.getAdsmHibernateTemplate().getSessionFactory().openSession();
		session.beginTransaction();
		for (String nrChave : chaves) {
			monitoramentoNotasFiscaisCCTService.storeEvento(tpOrigem, nrChave, null, null, null, observacao, null, SessionUtils.getUsuarioLogado());
		}
		
		if("SI".equals(tpOrigem)){
			monitoramentoNotasFiscaisCCTService.updateDataSolicitacaoICMS(chaves);
		}
		
		session.flush();
		session.getTransaction().commit();
		session.close();
	}
	
	private RotinasEnviarEmailRelatorioAgendamentoService getServiceParaNovaTransacao(){
		return (RotinasEnviarEmailRelatorioAgendamentoService) springBeanFactory.getBean("lms.entrega.rotinasEnviarEmailRelatorioAgendamentoService");
	}
	
	/**
	 * Extrai o número da nota fiscal da chave e concatena em notasFiscais.
	 * 
	 * @param key
	 * @param notasFiscais
	 */
	private String montarNumeroNotaFiscal(String key, String notasFiscais) {
		if("".equals(notasFiscais)){
			notasFiscais = NFEUtils.getNumeroNotaFiscalByChave(key);
		}else{
			notasFiscais = notasFiscais + ", " + NFEUtils.getNumeroNotaFiscalByChave(key); 
		}
		return notasFiscais;
	}

	/**
	 * Retorna um File com o xml da Nfe.
	 * 
	 * @param nrChave
	 * @return
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private File findXml(String nrChave) throws SQLException, IOException {
		File xmlFile = new File(nrChave + ".xml");
		InputStream xmlInputStream = notaFiscalEletronicaService.findNfeXMLFromEDI(nrChave);
		FileOutputStream fileOutputStream = new FileOutputStream(xmlFile);

		OutputStreamWriter outputStreamWriter = null;
		InputStreamReader inReader = new InputStreamReader(xmlInputStream, Charset.forName("ISO-8859-1"));
		outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charset.forName("UTF-8"));

		int c;
		while ((c = inReader.read()) != -1) {
			outputStreamWriter.write(c);
		}
		outputStreamWriter.close();
		fileOutputStream.close();
		return xmlFile;
	}
	
	private boolean validarAnexosPorEmail(List<AnexoEmail> anexos, AnexoEmail anexoEmail, Long limiteNumeroAnexos, Long limiteTamanhoAnexos) {
		long numeroJaAnexados = 0;
		Long tamanhoJaAnexados = 0L;

		for (AnexoEmail anexo : anexos) {
			numeroJaAnexados += anexo.getTotalArquivos();
			tamanhoJaAnexados += anexo.getTotalLength();
		}

		if (((numeroJaAnexados + anexoEmail.getTotalArquivos()) > limiteNumeroAnexos.longValue())
				|| ((tamanhoJaAnexados + anexoEmail.getTotalLength()) > limiteTamanhoAnexos.longValue())) {
			return false;
		} else {
			return true;
		}
	}

	private Map agruparPorRemetenteDestinatario(List<Map> listaMapDadosMonitoramento) {
		Map<ClienteKey, List<String>> retorno = new HashMap<ClienteKey, List<String>>();
		
		for(Map map : listaMapDadosMonitoramento){
			ClienteKey clienteKey = new ClienteKey((Long) map.get("idClienteRemetente"), (Long) map.get("idClienteDestinatario"));
			
			if(retorno.containsKey(clienteKey)){
				retorno.get(clienteKey).add((String) map.get("nrChave"));
			} else {
				List<String> chaves = new ArrayList<String>();
				chaves.add((String) map.get("nrChave"));
				retorno.put(clienteKey, chaves);
			}
		}

		return retorno;
	}

	private static class ClienteKey {
		private Long idClienteRemetente;
		private Long idClienteDestinatario;

		public ClienteKey(Long idClienteRemetente, Long idClienteDestinatario) {
			this.idClienteRemetente = idClienteRemetente;
			this.idClienteDestinatario = idClienteDestinatario;
		}

		public Long getIdClienteRemetente() {
			return idClienteRemetente;
		}

		public void setIdClienteRemetente(Long idClienteRemetente) {
			this.idClienteRemetente = idClienteRemetente;
		}

		public Long getIdClienteDestinatario() {
			return idClienteDestinatario;
		}

		public void setIdClienteDestinatario(Long idClienteDestinatario) {
			this.idClienteDestinatario = idClienteDestinatario;
		}
		
		public boolean equals(Object other) {
			if ((this == other))
				return true;
			if (!(other instanceof ClienteKey))
				return false;
			ClienteKey castOther = (ClienteKey) other;
			return idClienteRemetente.equals(castOther.getIdClienteRemetente()) && idClienteDestinatario.equals(castOther.getIdClienteDestinatario()); 
		}
		
		@Override
	    public int hashCode() {
	        int result = idClienteRemetente.intValue();
	        result = 31 * result + idClienteDestinatario.intValue();
	        return result;
	    }
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Map> findMonitoramentosEnvioSolicitacaoPagtoICMS() {
		return monitoramentoNotasFiscaisCCTService.findMonitoramentosEnvioSolicitacaoPagtoICMS();
	}

	/**
	 * 
	 * @param idAgendamentoEntrega
	 * @param idTemplateRelatorio
	 * @return
	 */
	public Map<String, Object> executeReplaceAgendamento(Long idAgendamentoEntrega, Long idTemplateRelatorio) {

		try {
			Map dadosGerais = findDadosGeraisRelatorioAgendamento(idAgendamentoEntrega);
			List<Map<String, Object>> dadosItens = agendamentoEntregaService.findDadosItensRelatorioAgendamento(idAgendamentoEntrega);
			Map mapArquivo = templateRelatorioService.findArquivo(idTemplateRelatorio);
			String nomeArquivo = (String) mapArquivo.get("nomeArquivo");
			AgendamentoReportRunnerCCT agendamentoReportRunnerCCT = new AgendamentoReportRunnerCCT((byte[]) mapArquivo.get("arquivo"), (String) mapArquivo.get("nomeArquivo"), new ReportExecutionManager().generateOutputDir()); 
			agendamentoReportRunnerCCT.setDadosGerais(dadosGerais);
			agendamentoReportRunnerCCT.setDadosItens(dadosItens);
			File reportFileAgendamento =  agendamentoReportRunnerCCT.generateReportFile();
			
			Map<String, Object> retorno = new HashMap<String, Object>();
			retorno.put("nomeArquivo", nomeArquivo);
			retorno.put("reportFileAgendamento", reportFileAgendamento);
			return retorno;
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
	}

	/**
	 * Retorna dados comuns, gerais, para o relatório de agendamento.
	 *  
	 * @return
	 */
	public Map findDadosGeraisRelatorioAgendamento(Long idAgendamentoEntrega) {
		return agendamentoEntregaService.findDadosGeraisRelatorioAgendamento(idAgendamentoEntrega);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> generateEmail(String tipoRelatorio, Cliente remetente, Cliente destinatario, Long idAgendamentoEntrega, YearMonthDay dtDPE){
		HashMap<String, Object> infoEmail = new HashMap<String, Object>();
		String assunto = "";
		String mensagem = "";
		boolean anexarDanfe = false;
		boolean anexarXml = false;
		Long idTemplateRelatorio = null;
		Long idEnvioCteCliente = null;
		
		Map<String, Object> map =  findEnvioCteClienteByTpRelatorio(tipoRelatorio, remetente, destinatario);
		EnvioCteCliente envioCteCliente = (EnvioCteCliente) map.get("envioCteCliente");
		List<String> listaEmailCCT = (List<String>) map.get("listaEmailCCT");
		
		if (envioCteCliente != null) {
			Pessoa remetentePessoa = null;
			if(remetente != null){
				remetentePessoa = pessoaDAO.findById(remetente.getPessoa().getIdPessoa());
			}
			Pessoa destinatarioPessoa = null;
			if(destinatario != null){
				destinatarioPessoa = pessoaDAO.findById(destinatario.getPessoa().getIdPessoa());
			}
			
			idEnvioCteCliente = envioCteCliente.getIdEnvioCteCliente();
			assunto = matchAssuntoEmail(envioCteCliente.getDsAssunto(), remetentePessoa, destinatarioPessoa);
			if (idAgendamentoEntrega != null) {
				String dtDPEString = "";
				if (dtDPE != null) {
					dtDPEString = dtDPE.toString("dd/MM/yyyy");
				}

				mensagem = matchMensagemEmail(envioCteCliente.getDsTextoEmail(), remetentePessoa, destinatarioPessoa, dtDPEString,
						agendamentoEntregaService.findNotasMonitoramentoCCTSendEmailByIdAgendamentoEntrega(idAgendamentoEntrega));

			} else {
				mensagem = matchMensagemEmail(envioCteCliente.getDsTextoEmail(), remetentePessoa, destinatarioPessoa);
			}

			anexarDanfe = envioCteCliente.getBlEnviaNfe();
			anexarXml = envioCteCliente.getBlEnviaXml();
			if (envioCteCliente.getTemplateRelatorio() != null) {
				idTemplateRelatorio = envioCteCliente.getTemplateRelatorio().getIdTemplate();
			}
		}
		
		infoEmail.put("de", getEnderecoEmailRemetente());
		infoEmail.put("para", formatEmailContatos(listaEmailCCT));
		infoEmail.put("assunto", assunto);
		infoEmail.put("texto", mensagem);
		infoEmail.put("anexarDanfe", anexarDanfe);
		infoEmail.put("anexarXml", anexarXml);
		infoEmail.put("idTemplateRelatorio", idTemplateRelatorio);
		infoEmail.put("idEnvioCteCliente", idEnvioCteCliente);
		return infoEmail;
	}

	private Map<String, Object> findEnvioCteClienteByTpRelatorio(String tipoRelatorio, Cliente remetente, Cliente destinatario){
		Map<String, Object> retorno = new HashMap<String, Object>();
		EnvioCteCliente envioCteCliente = null;
		List<String> listaEmailCCT = new ArrayList<String>();
		
		if("A".equalsIgnoreCase(tipoRelatorio)){
			listaEmailCCT = contatosCCTService.findEmailsByClientesTpParametrizacao(remetente.getIdCliente(), destinatario.getIdCliente(), tipoRelatorio);
			envioCteCliente = envioCteClienteDAO.findEnvioCteClienteByIdClienteETipoRelatorio(destinatario.getIdCliente(), tipoRelatorio);
		}else if("F".equalsIgnoreCase(tipoRelatorio)){
			listaEmailCCT = contatosCCTService.findEmailsByClientesTpParametrizacao(remetente.getIdCliente(), null, tipoRelatorio);
			envioCteCliente = envioCteClienteDAO.findEnvioCteClienteByIdClienteETipoRelatorio(remetente.getIdCliente(), tipoRelatorio);
		}else if("I".equalsIgnoreCase(tipoRelatorio)){
			listaEmailCCT = contatosCCTService.findEmailsByClientesTpParametrizacao(remetente.getIdCliente(), destinatario.getIdCliente(), tipoRelatorio);
			envioCteCliente = envioCteClienteDAO.findEnvioCteClienteByIdClienteETipoRelatorio(destinatario.getIdCliente(), tipoRelatorio);
		} else if("D".equalsIgnoreCase(tipoRelatorio)){
			listaEmailCCT = contatosCCTService.findEmailsByClientesTpParametrizacao(null, destinatario.getIdCliente(), tipoRelatorio);
			envioCteCliente = envioCteClienteDAO.findEnvioCteClienteByIdClienteETipoRelatorio(destinatario.getIdCliente(), tipoRelatorio);
		}
		
		if(envioCteCliente == null){
			envioCteCliente = envioCteClienteDAO.findEnvioCteClienteByIdClienteETipoRelatorio(
					Long.valueOf(parametroGeralDAO.findByNomeParametro("ID_EMPRESA_MERCURIO", false).getDsConteudo()), tipoRelatorio);
		}
		
		retorno.put("envioCteCliente", envioCteCliente);
		retorno.put("listaEmailCCT", listaEmailCCT);
		return retorno;
	}
	
	private String matchMensagemEmail(String mensagem, Pessoa remetentePessoa, Pessoa destinatarioPessoa) {
		if (remetentePessoa != null && destinatarioPessoa != null) {
			return MessageFormat.format(mensagem, new Object[] { remetentePessoa.getNmPessoa(), destinatarioPessoa.getNmPessoa() });
		} else if (remetentePessoa != null){
			return MessageFormat.format(mensagem, new Object[] { remetentePessoa.getNmPessoa(), ""});
		} else if (destinatarioPessoa != null){
			return MessageFormat.format(mensagem, new Object[] { destinatarioPessoa.getNmPessoa(), ""});
		}
		return "";
	}
	
	private String matchMensagemEmail(String mensagem,
		Pessoa remetentePessoa, Pessoa destinatarioPessoa,
		String dataAgendamento, String notas) {
		if(remetentePessoa != null && destinatarioPessoa != null){
			return MessageFormat.format(mensagem, new Object[]{remetentePessoa.getNmPessoa(), 
					destinatarioPessoa.getNmPessoa(),
					dataAgendamento,
					notas});
		}
		return "";
	}

	public String matchAssuntoEmail(String assunto, Pessoa remetente, Pessoa destinatario) {
		if(remetente != null && destinatario != null){
			return MessageFormat.format(assunto, new Object[]{remetente.getNmPessoa(), destinatario.getNmPessoa()});
		} else if(remetente != null){
			return MessageFormat.format(assunto, new Object[]{remetente.getNmPessoa(), ""});
		} else if(destinatario != null){
			return MessageFormat.format(assunto, new Object[]{destinatario.getNmPessoa(), ""});
		}
		return "";
	}
	
	private String formatEmailContatos(List<String> listaEmailCCT){
		String listaEmails = "";
		for (String dsEmail : listaEmailCCT) {
			listaEmails += dsEmail + ";";
		}		
		return listaEmails;
	}

	public void setParametroGeralDAO(ParametroGeralDAO parametroGeralDAO) {
		this.parametroGeralDAO = parametroGeralDAO;
	}

	public void setEnvioCteClienteDAO(EnvioCteClienteDAO envioCteClienteDAO) {
		this.envioCteClienteDAO = envioCteClienteDAO;
	}

	public void setAgendamentoEntregaService(
			AgendamentoEntregaService agendamentoEntregaService) {
		this.agendamentoEntregaService = agendamentoEntregaService;
	}

	public PessoaDAO getPessoaDAO() {
		return pessoaDAO;
	}

	public void setPessoaDAO(PessoaDAO pessoaDAO) {
		this.pessoaDAO = pessoaDAO;
	}

	public void setTemplateRelatorioService(TemplateRelatorioService templateRelatorioService) {
		this.templateRelatorioService = templateRelatorioService;
	}

	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

	public void setMonitoramentoNotasFiscaisCCTService(MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setSpringBeanFactory(SpringBeanFactory springBeanFactory) {
		this.springBeanFactory = springBeanFactory;
	}

	public void setTurnoService(TurnoService turnoService) {
		this.turnoService = turnoService;
	}

	public void setContatosCCTService(ContatosCCTService contatosCCTService) {
		this.contatosCCTService = contatosCCTService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
