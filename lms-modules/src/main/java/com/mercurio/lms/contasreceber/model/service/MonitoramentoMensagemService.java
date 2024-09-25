package com.mercurio.lms.contasreceber.model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.FaturaEmailPojo;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Email;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.LoteCobrancaTerceira;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemDetalhe;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemEvento;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemFatura;
import com.mercurio.lms.contasreceber.model.dao.MonitoramentoMensagemDAO;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.ArquivoUtils;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

public class MonitoramentoMensagemService extends CrudService<MonitoramentoMensagem, Long> {
	private static final String TEXT_HTML = "text/html; charset='utf-8'";
	
	MonitoramentoMensagemDAO monitoramentoMensagemDAO;
	MonitoramentoMensagemFaturaService monitoramentoMensagemFaturaService;
	MonitoramentoMensagemEventoService monitoramentoMensagemEventoService;
	MonitoramentoMensagemDetalheService monitoramentoMensagemDetalheService;
	ReportExecutionManager reportExecutionManager;
	ParametroGeralService parametroGeralService;
	MensagemComunicacaoService mensagemComunicacaoService;
	ConfiguracoesFacade configuracoesFacade;
	LoteCobrancaTerceiraService loteCobrancaTerceiraService;
	private BoletoService boletoService;
	private HistoricoBoletoService historicoBoletoService;
	private FaturaService faturaService;
	private IntegracaoJmsService integracaoJmsService;
	
	public static final String ENVIO = "E";
	public static final String TENTATIVA_FALHA = "T";
	public static final String RECEBIMENTO = "R";
	public static final String VISUALIZACAO = "I";
	public static final String ABERTURA_LINK_RADAR = "L";
	public static final String CONFIRMACAO_LEITURA = "C";
	public static final String RESPONDIDA_PELO_USUARIO = "O";
	public static final String DEVOLUCAO = "D";
	public static final String EXCESSO_TENTATIVAS = "N";
	public static final String ERRO = "X";
	public static final String TP_SITUACAO_BOLETO_DIGITADO = "DI";
	public static final String TP_SITUACAO_BOLETO_EMITIDO = "EM";
	public static final String TP_OCORRENCIA_BANCO_REMESSA = "REM";
	public static final String TP_EVENTO_MENSAGEM_SOLICITACAO = "S";
	
	@Override
	public Serializable store(MonitoramentoMensagem entity) {
		return super.store(entity);
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
		
	public MonitoramentoMensagem findByIdMonitoramentoMensagem(Long idMonitoramentoMensagem){
		return monitoramentoMensagemDAO.findByIdMonitoramentoMensagem(idMonitoramentoMensagem);
	}
	
	public void setMonitoramentoMensagemDAO(MonitoramentoMensagemDAO monitoramentoMensagemDAO) {
		setDao(monitoramentoMensagemDAO);
		this.monitoramentoMensagemDAO = monitoramentoMensagemDAO;
	}
	
	public Mail executeCreateEmailMensagenFaturamentoAutomacao(MonitoramentoMensagem monitoramentoMsg){
		Email email = mensagemComunicacaoService.executeMontaMensagemGenerica(monitoramentoMsg);

		Mail mail = new Mail();
		mail.setSubject(email.getpAssunto());
		mail.setBody(email.getpCorpo());
		
		return mail;
		
	}
	
	public Email executeCreateEmailMensagenFaturamento(MonitoramentoMensagem monitoramentoMsg){
		Email email = mensagemComunicacaoService.executeMontaMensagemGenerica(monitoramentoMsg);
		email.setpAnexos(executeMontarMensagenFaturamento(email, monitoramentoMsg));
		return email;
	}
	
	public Email executeCreateEmailMensagenCobrancaTerceira(MonitoramentoMensagem monitoramentoMsg){
		Email email = mensagemComunicacaoService.executeMontaMensagemGenerica(monitoramentoMsg);
		email.setpAnexos(executeMontarMensagenCobrancaTerceira(email, monitoramentoMsg));
		return email;
	}
	
	public Email executeCreateEmailMensagenCobranca(MonitoramentoMensagem monitoramentoMsg){
		Email email = mensagemComunicacaoService.executeMontaMensagemGenerica(monitoramentoMsg);
		email.setpAnexos(executeMontarMensagenCobranca(email, monitoramentoMsg));
		return email;
	}
	
	public void enviaMensagemComunicacao(@QueryParam("monitMsg") final MonitoramentoMensagem monitMsg, @QueryParam("email") final Email email) {
		if("E".equals(monitMsg.getTpEnvioMensagem().getValue())) {
			int nrTentativas = Integer.parseInt(parametroGeralService.findByNomeParametro("NR_TENTATIVAS_MENSAGEM").getDsConteudo());
			
			boolean mensagemEnviada = false;
			
			for(int i=0; i<nrTentativas; i++) {
				try {
					mensagemEnviada = enviaMensagem(monitMsg, email);
					if (mensagemEnviada) {
						saveEventoMensagem(monitMsg.getIdMonitoramentoMensagem(), ENVIO, "LMS-36303");
						break;
					}
				}catch(Exception e) {
					saveEventoMensagem(monitMsg.getIdMonitoramentoMensagem(), TENTATIVA_FALHA, e.getMessage());
				}
			}
			
			if (mensagemEnviada == false) {
				saveEventoMensagem(monitMsg.getIdMonitoramentoMensagem(), EXCESSO_TENTATIVAS, "LMS-36302");
			}
		}
	}
	
	private boolean enviaMensagem(final MonitoramentoMensagem monitMsg, final Email email) throws Exception {
		try{
			String emailsJson = monitMsg.getDsDestinatario();
			Map<String, String> emails = unmarshalJsonToMap(emailsJson);
			List<MailAttachment> mailAttachments = new ArrayList<MailAttachment>();
			
			if(email.getpAnexos() != null) {
				Iterator it = email.getpAnexos().entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String, File> data = (Map.Entry)it.next();
					MailAttachment mailAttachment = new MailAttachment();
					mailAttachment.setName(data.getKey());
					mailAttachment.setData(FileUtils.readFile(data.getValue()));
					mailAttachments.add(mailAttachment);
				}
			}

			String emailsCc = emails.get("cc");
			if (emailsCc != null) {
				emailsCc = StringUtils.join(emailsCc.split(","), ";");
			}
			
			Mail mail = createMail(
				StringUtils.join(emails.get("para").split(","), ";"),
				emailsCc,
				emails.get("de"),
				email.getpAssunto(),
				email.getpCorpo(),
				mailAttachments
			);

			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
			integracaoJmsService.storeMessage(msg);
			
			return true;
		} catch(Exception e) {
			throw e;
		}
	}

	private Mail createMail(String strTo, String strCc, String strFrom, String 
	strSubject, String body, List<MailAttachment> mailAttachmentList) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setCc(strCc);
		mail.setSubject(strSubject);
		mail.setBody(body);
		mail.setAttachements(mailAttachmentList.toArray(new MailAttachment[mailAttachmentList.size()]));

		return mail;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> unmarshalJsonToMap(String emailsJson) {
		Map<String, String> parametro = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			parametro = mapper.readValue(emailsJson, Map.class);
		} catch (JsonParseException e) {
			throw new InfrastructureException(e);
		} catch (JsonMappingException e) {
			throw new InfrastructureException(e);
		} catch (IOException e) {
			throw new InfrastructureException(e);
		}
		return parametro;
	}
	
	//Teste geração de boleto
	public File testaGeracaoBoleto(boolean recalcular) throws Exception {
		List<Long> idBoletos = new ArrayList<Long>();
		idBoletos.add(2218666L);
		TypedFlatMap parametros = new TypedFlatMap();
		parametros.put("idFaturas", idBoletos.toString().replace("[", "").replace("]", ""));
		parametros.put("BL_ATUALIZA_VCTO_CARTA_COB", recalcular);
		return this.reportExecutionManager.executeReport("lms.contasreceber.emitirBoletoService", parametros);
	}
	
	private Map<String, File> executeMontarMensagenCobrancaTerceira(Email email, MonitoramentoMensagem monitoramentoMsg) {
		Map<String, File> anexos = new HashMap<String, File>();
		try {
			if (email.getpCorpo() != null) {
				String idMoni = monitoramentoMsg.getDsParametro();
				Map<String, String> ids = unmarshalJsonToMap(idMoni);
				LoteCobrancaTerceira lote = loteCobrancaTerceiraService.findLoteCobrancaById(Long.parseLong(ids.get(":1")));
				if (lote == null || lote.getDcArquivo() == null) {
					return anexos;
				}
				String name = "LOTE_COBRANCA_" + StringUtils.leftPad(lote.getNrLote(), 10, '0') + ".txt";
				byte[] arquivo = mountFile(Long.parseLong(ids.get(":1")), name);

				File targetFile = new File(VMProperties.JAVA_IO_TMPDIR.getValue(), name.trim());
				OutputStream outStream = new FileOutputStream(targetFile);
				outStream.write(arquivo);

				anexos.put(name.trim(), targetFile);
			}
		} catch (Exception e) {
			saveEventoMensagem(monitoramentoMsg.getIdMonitoramentoMensagem(), ERRO, e.getMessage());
		}

		return anexos;
	}

	private byte[] mountFile(Long idLoteCobranca, String nomeArquivo) throws IOException {
		StringWriter stringWriter = new StringWriter();
		List<Object[]> arquivos = loteCobrancaTerceiraService.geraArquivoLoteCobrancaTerceira(idLoteCobranca);
		for (Object[] row : arquivos) {
			stringWriter.append(geraLinha(row) + "\n");
		}
		stringWriter.close();

		return FormatUtils.mountFileInArrayByteASCII(nomeArquivo, FormatUtils.removeAccents(stringWriter.toString()));
	}

	private String geraLinha(Object[] row) {
		StringBuilder linha = new StringBuilder();
		for (int i = 1; i < row.length; i++) {
			linha.append(row[i]);
		}
		return limpaLinha(linha.toString());
	}

	private String limpaLinha(String linha) {
		return linha.replace("\t", "").replace("\n", "");
	}
	
	private Map<String,File> executeMontarMensagenCobranca(Email email, MonitoramentoMensagem monitoramentoMsg) {
		Map<String, File> anexos = new HashMap<String, File>();
		try {
			if (email.getpCorpo() != null) {
				generateBoletos(monitoramentoMsg, anexos, true);
			}
		} catch (Exception e) {
			saveEventoMensagem(monitoramentoMsg.getIdMonitoramentoMensagem(), ERRO, e.getMessage());
		}

		return anexos;
	}
	
	private Map<String,File> executeMontarMensagenFaturamento(Email email, MonitoramentoMensagem monitoramentoMsg) {
		Map<String, File> anexos = new HashMap<String, File>();
		try {
			if (email.getpCorpo() != null) {
				List<Long> idFaturas = monitoramentoMensagemFaturaService.findFaturas(monitoramentoMsg);

				if(idFaturas != null && !idFaturas.isEmpty() && idFaturas.size() > 0){
					TypedFlatMap parametros = new TypedFlatMap();
					parametros.put("importacaoPreFaturas", idFaturas.toString().replace("[", "").replace("]", ""));
					File faturaReport = this.reportExecutionManager.executeReport("lms.contasreceber.emitirFaturasNacionaisService", parametros);
					String nomeArquivoFatura = parametroGeralService.findByNomeParametro("NM_ARQ_FATURA").getDsConteudo();
					anexos.put(nomeArquivoFatura, faturaReport);
					
					generateBoletos(monitoramentoMsg, anexos, false);
				}
			}
		} catch (Exception e) {
			saveEventoMensagem(monitoramentoMsg.getIdMonitoramentoMensagem(), ERRO, e.getMessage());
		}

		return anexos;
	}
	
	private void generateBoletos(MonitoramentoMensagem monitoramentoMsg, Map<String, File> anexos, boolean recalcular) throws Exception {
		List<Long> idBoletos = monitoramentoMensagemFaturaService.getBoletos(monitoramentoMsg);
		
		if(idBoletos != null && !idBoletos.isEmpty() && idBoletos.size() > 0){
			TypedFlatMap parametros = new TypedFlatMap();
			parametros.put("idFaturas", idBoletos.toString().replace("[", "").replace("]", ""));
			if (recalcular) {
				parametros.put("BL_ATUALIZA_VCTO_CARTA_COB", recalcular);
			}
			File boletoReport = this.reportExecutionManager.executeReport("lms.contasreceber.emitirBoletoService", parametros);
			String nomeArquivoBoleto = parametroGeralService.findByNomeParametro("NM_ARQ_BOLETO").getDsConteudo();
			anexos.put(nomeArquivoBoleto, boletoReport);
		}
	}
	
	
	public void saveEventoMensagem(Long idMonitMsg, String tpEvento, String dsEvento){
		MonitoramentoMensagemEvento msgEvento = MonitoramentoMensagemEvento
				.newInstance(idMonitMsg, tpEvento, dsEvento);
		
		if(msgEvento.getMonitoramentoMensagem() != null) {
			monitoramentoMensagemEventoService.store(msgEvento);
			
			MonitoramentoMensagemDetalhe msgDetalhe = monitoramentoMensagemDetalheService
					.findByIdMonitoramentoMensagem(msgEvento.getMonitoramentoMensagem().getIdMonitoramentoMensagem());

			msgDetalhe.setMonitoramentoMensagem(msgEvento.getMonitoramentoMensagem());
			
			if(ENVIO.equals(tpEvento)) {
				msgDetalhe.setDhEnvio(new DateTime());
			} else if(RECEBIMENTO.equals(tpEvento) || VISUALIZACAO.equals(tpEvento) || ABERTURA_LINK_RADAR.equals(tpEvento) || RESPONDIDA_PELO_USUARIO.equals(tpEvento)) {
				msgDetalhe.setDhRecebimento(new DateTime());
			} else if(DEVOLUCAO.equals(tpEvento)) {
				msgDetalhe.setDhDevolucao(new DateTime());
			} else if(EXCESSO_TENTATIVAS.equals(tpEvento) || (ERRO.equals(tpEvento))) {
				msgDetalhe.setDhErro(new DateTime());
			}
			
			monitoramentoMensagemDetalheService.store(msgDetalhe);
		}
	}
	
	public List<MonitoramentoMensagemEvento> findMonitoramentoEventosMensagem(Long idMonitoramentoMensagem){
		return monitoramentoMensagemDAO.findMonitoramentoEventos(idMonitoramentoMensagem);
	}
	
	public List findEventosMensagem(Long idMonitoramentoMensagem){
		List callback = findMonitoramentoEventosMensagem(idMonitoramentoMensagem);
		List eventos = new ArrayList();
		for(int i=0; i < callback.size();i++){
			MonitoramentoMensagemEvento monitoramentoEvento = (MonitoramentoMensagemEvento) callback.get(i);
			TypedFlatMap relacao = new TypedFlatMap();
			relacao.put("tpEvento", monitoramentoEvento.getTpEvento().getDescriptionAsString());
			String descricao = monitoramentoEvento.getDsEvento();
			String message =  configuracoesFacade.getMensagem(descricao.trim());
			if ( message != null ){
				descricao = message;
			}
			relacao.put("descricao",descricao);
			relacao.put("dhEvento",JTDateTimeUtils.formatDateTimeToString(monitoramentoEvento.getDhEvento()));
			eventos.add(relacao);
		}
		return eventos;
	}
	
	public MonitoramentoMensagemDetalhe findMonitoramentoConteudoMensagem(Long idMonitoramentoMensagem){
		return monitoramentoMensagemDAO.findMonitoramentoDetalhe(idMonitoramentoMensagem);
	}
	
	public List findConteudoMensagem(Long idMonitoramentoMensagem){
		List callback = new ArrayList();
		MonitoramentoMensagemDetalhe detalhe = findMonitoramentoConteudoMensagem(idMonitoramentoMensagem);
		if ( detalhe == null || detalhe.getDcMensagem() == null ){
			return callback;
		}
		detalhe.setDcMensagem( detalhe.getDcMensagem().replace("<img", "p").replace("</img>", "</p>") );
		callback.add(detalhe);
		return callback; 
	}
	
	public List findHistoricoMensagensByLoteCobrancaId(Long loteCobrancaTerceira) {
		if ( loteCobrancaTerceira == null  ) {
			return new ArrayList(); 
		}
		return monitoramentoMensagemDAO.findHistoricoMensagensByLoteCobrancaId(loteCobrancaTerceira);
	}
	
	public void updateDhProcessamentoMonitoramentoMensagem(MonitoramentoMensagem mm) {
		monitoramentoMensagemDAO.updateDhProcessamentoMonitoramentoMensagem(mm);
	}
	
	public List<MonitoramentoMensagem> findByDhProcessamentoNull() {
        return monitoramentoMensagemDAO.findByDhProcessamentoNull();
    }

    public List<MonitoramentoMensagem> findMonitoramentosFaturar() {
		return monitoramentoMensagemDAO.findMonitoramentosFaturar();
		
	}
	
	public List<MonitoramentoMensagem> findMonitoramentoMensagemByIdFatura(Long idFatura) {
		return monitoramentoMensagemDAO.findMonitoramentoMensagemByIdFatura(idFatura);
	}
	
	public MonitoramentoMensagemDetalhe findMonitoramentoDetalheByMonitoramentoId(Long idMonitoramentoMensagem) {
		return monitoramentoMensagemDAO.findMonitoramentoDetalhe(idMonitoramentoMensagem);
	}
	
	public List findHistoricoMensagensByFaturaId(Long faturaId){
		if ( faturaId == null  ) {
			return new ArrayList(); 
		}
		List callback = monitoramentoMensagemDAO.findHistoricoMensagensByFaturaId(faturaId);
		List messagesPopup = new ArrayList();
		for(int i=0; i < callback.size();i++){
			TypedFlatMap relacao = new TypedFlatMap();
			MonitoramentoMensagem monitoramentoMensagem = (MonitoramentoMensagem) callback.get(i);
			MonitoramentoMensagemDetalhe monitoramentoMensagemDetalhe = monitoramentoMensagemDAO.findMonitoramentoDetalhe(monitoramentoMensagem.getIdMonitoramentoMensagem());
			
			Map<String, String> emails = unmarshalJsonToMap(monitoramentoMensagem.getDsDestinatario());
			relacao.put("de",emails.get("de"));
			relacao.put("para",emails.get("para"));
			relacao.put("monitoramentoMensagem", monitoramentoMensagem);
			relacao.put("idMonitoramentoMensagemEvento",monitoramentoMensagem.getIdMonitoramentoMensagem());
			relacao.put("idMonitoramentoMensagemConteudo",monitoramentoMensagem.getIdMonitoramentoMensagem());
			relacao.put("idMonitoramentoMensagem", monitoramentoMensagem.getIdMonitoramentoMensagem());
			relacao.put("tpEnvioMensagem", monitoramentoMensagem.getTpEnvioMensagem().getDescriptionAsString());
			relacao.put("tpModeloMensagem", monitoramentoMensagem.getTpModeloMensagem().getDescriptionAsString());
			relacao.put("dhProcessamento", JTDateTimeUtils.formatDateTimeToString(monitoramentoMensagem.getDhProcessamento()));
			relacao.put("dhInclusao", JTDateTimeUtils.formatDateTimeToString(monitoramentoMensagem.getDhInclusao()));
			relacao.put("dhEnvio", "");
			relacao.put("dhRecebimento", "");
			relacao.put("dhDevolucao", "");
			relacao.put("dhErro", "");
			if ( monitoramentoMensagemDetalhe == null ){
				messagesPopup.add(relacao);
				continue;
			}
			if ( monitoramentoMensagemDetalhe.getDhEnvio() != null ){
				relacao.put("dhEnvio", JTDateTimeUtils.formatDateTimeToString(monitoramentoMensagemDetalhe.getDhEnvio()));
			}
			if ( monitoramentoMensagemDetalhe.getDhRecebimento() != null ){
				relacao.put("dhRecebimento", JTDateTimeUtils.formatDateTimeToString(monitoramentoMensagemDetalhe.getDhRecebimento()));
			}
			if ( monitoramentoMensagemDetalhe.getDhDevolucao() != null ){
				relacao.put("dhDevolucao", JTDateTimeUtils.formatDateTimeToString(monitoramentoMensagemDetalhe.getDhDevolucao()));
			}
			if ( monitoramentoMensagemDetalhe.getDhErro() != null ){
				relacao.put("dhErro", JTDateTimeUtils.formatDateTimeToString(monitoramentoMensagemDetalhe.getDhErro()));
			}
			messagesPopup.add(relacao);
		}
		return messagesPopup;
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void storeGeraEmailComunicacao(List<FaturaEmailPojo> faturas, String tpModeloMensagem) {
		if (faturas == null || faturas.isEmpty()) {
			return;
		}
		
		MonitoramentoMensagem modelo = new MonitoramentoMensagem();
		modelo.setTpModeloMensagem(new DomainValue(tpModeloMensagem));
		modelo.setTpEnvioMensagem(new DomainValue("E"));
		modelo.setDhInclusao(new DateTime());
		modelo.setDsParametro("{\":1\":\"" + faturas.get(0).getIdRegional() + "\"}");

		String para = "";

		if ("FA".equalsIgnoreCase(tpModeloMensagem)) {
			para = faturas.get(0).getDsEmail();
		} else {
			para = faturas.get(0).getDsEmailFaturamento();
		}

		modelo.setDsDestinatario("{" + configuracoesFacade.getMensagem("dsDestinatario", new Object[] { faturas.get(0).getDsEmailFaturamento(), para }) + "}");
		monitoramentoMensagemDAO.store(modelo);

		for (FaturaEmailPojo faturaEmail : faturas) {
			MonitoramentoMensagemFatura monitoramentoMensagemFatura = new MonitoramentoMensagemFatura();
			monitoramentoMensagemFatura.setMonitoramentoMensagem(modelo);

			if (!"SE".equalsIgnoreCase(tpModeloMensagem)) {
				updateFatura(faturaEmail);
				updateSituacaoBoleto(faturaEmail);
			}

			popularFatura(faturaEmail, monitoramentoMensagemFatura);

			monitoramentoMensagemFaturaService.store(monitoramentoMensagemFatura);
		}

		updateMonitoramentoMensagemEvento(modelo);
	}

	public boolean findMonitoramentoMensagem(Long idFatura){
		SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("LIMITE_HORAS_ARQS_FATURAMENTO");
		
		if(parametroGeral == null) {
			return false;
		}
		
		
		try {
			Calendar dataInicialCalendar = Calendar.getInstance();
			Calendar dataFinalCalendar = Calendar.getInstance();
			calcularDiasHoras(parametroGeral.getDsConteudo(), dataInicialCalendar);
			
			String dataInicial = formatarData.format(dataInicialCalendar.getTime());
			String dataFinal = formatarData.format(dataFinalCalendar.getTime());
			
			List<Object[]> monitoramentoMensagem = monitoramentoMensagemDAO
													.findMonitoramentoMensagem(idFatura, dataInicial, dataFinal);
			
			return !monitoramentoMensagem.isEmpty();
		}catch (Exception e) {
			return false;
		}
				
	}

	/**
	 * Para otimizar o processo não é realizada a busca da fatura, somente criado um objeto setando o id e a versão. 
	 * Nesse caso a versão não importa, pois a fatura não será alterada, ela é necessária somente para
	 * "comprovar" que o objeto já foi persistido.
	 * 
	 * @param faturaEmail
	 * @param monitoramentoMensagemFatura
	 */
	private void popularFatura(FaturaEmailPojo faturaEmail, MonitoramentoMensagemFatura monitoramentoMensagemFatura) {
		Fatura fatura = new Fatura();
		fatura.setIdFatura(faturaEmail.getIdFatura());
		fatura.setVersao(1);
		monitoramentoMensagemFatura.setFatura(fatura);
	}

	private void updateSituacaoBoleto(FaturaEmailPojo faturaEmail) {
		Boleto boleto = boletoService.findFirstByFaturaAndSituacaoBoleto(faturaEmail.getIdFatura(), TP_SITUACAO_BOLETO_DIGITADO);
		if (boleto == null) {
			return;
		}

		boleto.setTpSituacaoBoleto(new DomainValue(TP_SITUACAO_BOLETO_EMITIDO));
		historicoBoletoService.generateHistoricoBoleto(boleto, ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_ENVIO, TP_OCORRENCIA_BANCO_REMESSA);
		boletoService.storeBasic(boleto);
	}

	private void updateFatura(FaturaEmailPojo faturaEmail) {
		faturaService.updateIndicadorImpressao(faturaEmail.getIdFatura(), Boolean.TRUE);
	}
	
	private void updateMonitoramentoMensagemEvento(MonitoramentoMensagem modelo) {		
		MonitoramentoMensagemEvento monitoramentoMensagem = MonitoramentoMensagemEvento
				.newInstance(modelo.getIdMonitoramentoMensagem(), TP_EVENTO_MENSAGEM_SOLICITACAO, "LMS-36310");
				
		monitoramentoMensagemEventoService.store(monitoramentoMensagem);
	}
	
	private void calcularDiasHoras(String limiteHoras, Calendar dataCalculada) {
		int horas = Integer.parseInt(limiteHoras);
		
		if(horas > 0) {
			horas *= -1;
		}
		dataCalculada.add(Calendar.HOUR, horas);
	}
		
	public void setMonitoramentoMensagemEventoService(
			MonitoramentoMensagemEventoService monitoramentoMensagemEventoService) {
		this.monitoramentoMensagemEventoService = monitoramentoMensagemEventoService;
	}

	public void setMonitoramentoMensagemDetalheService(
			MonitoramentoMensagemDetalheService monitoramentoMensagemDetalheService) {
		this.monitoramentoMensagemDetalheService = monitoramentoMensagemDetalheService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setMensagemComunicacaoService(MensagemComunicacaoService mensagemComunicacaoService) {
		this.mensagemComunicacaoService = mensagemComunicacaoService;
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public void setMonitoramentoMensagemFaturaService(MonitoramentoMensagemFaturaService monitoramentoMensagemFaturaService) {
		this.monitoramentoMensagemFaturaService = monitoramentoMensagemFaturaService;
	}
	
	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setLoteCobrancaTerceiraService(LoteCobrancaTerceiraService loteCobrancaTerceiraService) {
		this.loteCobrancaTerceiraService = loteCobrancaTerceiraService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

}
