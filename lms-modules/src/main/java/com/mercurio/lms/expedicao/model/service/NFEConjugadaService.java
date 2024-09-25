package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.jfree.util.Log;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.NFEConjugada;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.NFEPacket;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.canc.NFECanc;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.envio.NFEEnvio;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.inut.NFEInut;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.transfer.NFEConjugadaTransfer;
import br.com.tntbrasil.integracao.domains.jms.Queues;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.security.model.Usuario;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.entrega.model.service.RegistrarBaixaEntregaPorNotaFiscalService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.dao.NFEConjugadaDAO;
import com.mercurio.lms.expedicao.reports.NFEConjugadaReportService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.MensagemService;

/**
 * Service responsável por gerenciar as transações com notas fiscais eletrônicas
 * conjugadas.
 * 
 * @see NFEConjugadaServiceHelper
 * @see NFEConjugadaReportService
 * @see NFEConjugadaDAO
 * 
 * @see NFEConjugada
 * @see NFEEnvio
 * @see NFECanc
 * @see NFEInut
 * @see NFEConjugadaTransfer
 * @see NFEPacket
 */
public class NFEConjugadaService {

	private static final String UTF_8 = "UTF-8";
	public static final String PARAM_NR_NFE_CONJUGADA = "NR_NFE_CONJUGADA";	
	public static final String PARAM_TEMPO_RETORNO_NFE_CONJUGADA = "TEMPO_RETORNO_NFE_CONJUGADA";
	private static final String PARAM_ATIVA_NFE_CONJUGADA = "ATIVA_NFE_CONJUGADA";
	private static final String PARAM_PRAZO_INUT_NFE_CONJUGADA = "PRAZO_INUT_NFE_CONJUGADA";
	private static final String PARAM_LIMITE_MINIMO_CNF_NFE_CONJUGADA = "LIMITE_MINIMO_CNF_NF-E_CONJUGADA";
	private static final String PARAM_LIMITE_MAXIMO_CNF_NFE_CONJUGADA = "LIMITE_MAXIMO_CNF_NF-E_CONJUGADA";
	private static final String NR_INVALIDOS_PARA_GERACAO_CNF_NFE_CONJUGADA = "NR_INVALIDOS_PARA_GERACAO_CNF_NF-E_CONJUGADA";
	
	private static final String MSG_OBSERVACAO_DANFE = "numeracaoDoctoServico";
	
	private static final UniformRandomProvider rng = RandomSource.create(RandomSource.MT);
	
	private NFEConjugadaDAO nfeConjugadaDAO;
	private IntegracaoJmsService integracaoJmsService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private ParametroGeralService parametroGeralService;
	private DevedorDocServFatService devedorDocServFatService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private ReciboReembolsoService reciboReembolsoService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private MensagemService mensagemService;
	private NotaFiscalOperadaService notaFiscalOperadaService;
	private RegistrarBaixaEntregaPorNotaFiscalService registrarBaixaEntregaPorNotaFiscalService;	
	
	/**
	 * Executa a coleta de informações para o <b>ENVIO</b> da nota fiscal
	 * eletronica conjugada, assim como envia para uma fila.
	 * 
	 * @param doctoServico
	 * @param idFilial
	 * 
	 * @return String
	 */
	public String storeEnvio(DoctoServico doctoServico, Long nrFiscalRps, Short nrCfop) {		
		nfeConjugadaDAO.getAdsmHibernateTemplate().flush();
		
		List<Map<String, Object>> listDados = getNfeConjugadaDAO().findEnvioNotaFiscalConjugada(doctoServico.getIdDoctoServico(), doctoServico.getFilialByIdFilialOrigem().getIdFilial());
		
		if(listDados == null || listDados.isEmpty()){					
			return null;
		}
		
		String nrCnf = this.generateCnfNotaFiscalConjugada(doctoServico.getIdDoctoServico());
		listDados.get(0).put("idChaveAcessoNf", nrCnf);
		
		MonitoramentoDocEletronico monitoramentoDocEletronico = new MonitoramentoDocEletronico();
		monitoramentoDocEletronico.setDoctoServico(doctoServico);	
		monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_ENVIADO));
		monitoramentoDocEletronico.setNrFiscalRps(nrFiscalRps);
		monitoramentoDocEletronico.setDhEnvio(JTDateTimeUtils.getDataHoraAtual(SessionUtils.getFilialSessao()));
		monitoramentoDocEletronico.setNrCnf(Long.parseLong(nrCnf));
		
		Long idMonitoramento = (Long) monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
		
		createEnvio(doctoServico, nrFiscalRps, nrCfop, listDados, idMonitoramento);
				
		return idMonitoramento.toString();
	}
	
	/**
	 * Executa a coleta de informações para o <b>REENVIO</b> da nota fiscal
	 * eletronica conjugada, assim como envia para uma fila.
	 *  
	 * @param doctoServico
	 * @param idFilial
	 * @param monitoramento
	 */
	public void storeReenvio(DoctoServico doctoServico, MonitoramentoDocEletronico monitoramento, Long nrFiscalRps) {
		List<Map<String, Object>> listDados = getNfeConjugadaDAO().findEnvioNotaFiscalConjugada(doctoServico.getIdDoctoServico(), doctoServico.getFilialByIdFilialOrigem().getIdFilial());
		
		if(listDados == null || listDados.isEmpty()){					
			return;
		}
		
		String nrCnf = null;
		if (monitoramento.getNrCnf() != null) {
			nrCnf = StringUtils.leftPad(monitoramento.getNrCnf().toString(), 8, '0');
		} else {
			nrCnf = this.generateCnfNotaFiscalConjugada(doctoServico.getIdDoctoServico());
			monitoramento.setNrCnf(Long.parseLong(nrCnf));
		}
		
		listDados.get(0).put("idChaveAcessoNf", nrCnf);
		
		monitoramento.setTpSituacaoDocumento(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_ENVIADO));
		monitoramento.setDhEnvio(JTDateTimeUtils.getDataHoraAtual(SessionUtils.getFilialSessao()));
		monitoramento.setNrFiscalRps(nrFiscalRps);
		monitoramentoDocEletronicoService.store(monitoramento);
		
		createEnvio(doctoServico, nrFiscalRps, doctoServico.getNrCfop(), listDados, monitoramento.getIdMonitoramentoDocEletronic());
	}
	
	public String generateCnfNotaFiscalConjugada(Long idDoctoServico) {
		Long nrDocumentoFiscal = getNfeConjugadaDAO().findNumeroDocumentoFiscal(idDoctoServico);
		Long valorMinCNF = Long.parseLong(parametroGeralService.findSimpleConteudoByNomeParametro(PARAM_LIMITE_MINIMO_CNF_NFE_CONJUGADA));
		Long valorMaxCNF = Long.parseLong(parametroGeralService.findSimpleConteudoByNomeParametro(PARAM_LIMITE_MAXIMO_CNF_NFE_CONJUGADA));
		List<String> valoresInvalidos = Arrays.asList(
			parametroGeralService.findSimpleConteudoByNomeParametro(NR_INVALIDOS_PARA_GERACAO_CNF_NFE_CONJUGADA).split(";")
		);
		
		return this.generateCnfRecursive(nrDocumentoFiscal, valorMinCNF, valorMaxCNF, valoresInvalidos);
	}
	
	private String generateCnfRecursive(Long nrDocumentoFiscal, Long min, long max, List<String> invalidos) {
		Long randomNumber = rng.nextLong((max - min) + 1l) + min;
		String cnf = StringUtils.leftPad(randomNumber.toString(), 8, '0');
		String documentoFiscal = "";
		
		if (nrDocumentoFiscal != null) {
			documentoFiscal = StringUtils.leftPad(nrDocumentoFiscal.toString(), 8, '0');
		}
		
		if (invalidos.contains(cnf) || cnf.equals(documentoFiscal) || this.getNfeConjugadaDAO().existsNfeByNrCnf(cnf)) {
			Log.info("Nova chamada do método recursivo #generateCnfRecursive");
			return this.generateCnfRecursive(nrDocumentoFiscal, min, max, invalidos);
		}
		return cnf;
	}
	
	/**
	 * Executa a coleta de informações para <b>CANCELAR</b> uma nota fiscal
	 * eletronica conjugada, assim como envia para uma fila.
	 * 
	 * @param id
	 * 
	 */
	public void storeCancelar(Long id) {
		List<Map<String, Object>> listDados = getNfeConjugadaDAO().findCancNotaFiscalConjugada(id);
		
		if(listDados == null || listDados.isEmpty()){					
			return;
		}
		
		sendNFEToQueue(new NFEPacket(NFEConjugadaServiceHelper.getCancelar(listDados)));		
	}
		
	/**
	 * Executa a coleta de informações para <b>INUTILIZAR</b> uma nota fiscal
	 * eletronica conjugada, assim como envia para uma fila.
	 * 
	 * @param doctoServico
	 * @param idFilial
	 * @param idMonitoramento
	 */
	private void storeInutilizar(DoctoServico doctoServico, Long idFilial, Long idMonitoramento) {
		List<Map<String, Object>> listDados = getNfeConjugadaDAO().findInutNotaFiscalConjugada(doctoServico.getIdDoctoServico(), idFilial);
		
		if(listDados == null || listDados.isEmpty()){					
			return;
		}
		
		NFEInut inutilizar = NFEConjugadaServiceHelper.getInutilizar(listDados);
		
		MonitoramentoDocEletronico monitoramento = monitoramentoDocEletronicoService.findById(idMonitoramento);		
		monitoramento.setTpSituacaoDocumento(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_INUT_ENVIADA));
		monitoramento.setNrChave(inutilizar.getIdChave());
		monitoramento.setDhEnvio(JTDateTimeUtils.getDataHoraAtual(SessionUtils.getFilialSessao()));
		monitoramentoDocEletronicoService.store(monitoramento);
		
		sendNFEToQueue(new NFEPacket(inutilizar));		
	}
	
	/**
	 * Atualiza o monitoramento do serviço de envio.
	 * 
	 * @param retorno
	 */
	public void storeEnvioMonitoramento(NFEConjugadaTransfer retorno){
		if(retorno.getIdMonitoramento() == null){
			return;
		}
		
		getNfeConjugadaDAO().storeEnvioMonitoramento(retorno.getIdMonitoramento(), retorno.getDocumentData(), retorno.getNrChave());
	}
	
	/**
	 * Busca o xml de uma nota fiscal conjugada.
	 * 
	 * @param retorno
	 * @return NFEConjugadaTransfer
	 */
	@SuppressWarnings("rawtypes")
	public NFEConjugadaTransfer findXMLData(String nrChave){
		List result = getNfeConjugadaDAO().findDocumentData(nrChave);
		
		if(result.isEmpty()){
			return new NFEConjugadaTransfer();
		}
		
		NFEConjugadaTransfer transfer = new NFEConjugadaTransfer();
		transfer.setDocumentData(new String((byte[]) result.get(0), Charset.forName(UTF_8)));
		
		return transfer;
	}
	
	/**
	 * Atualiza o monitoramento do serviço de retorno.
	 * 
	 * @param retorno
	 */
	public void storeRetornoMonitoramento(NFEConjugadaTransfer retorno){
		MonitoramentoDocEletronico monitoramento = monitoramentoDocEletronicoService.findByNrChave(retorno.getNrChave());
		monitoramento.setDsObservacao(retorno.getObservacao());
		monitoramento.setTpSituacaoDocumento(new DomainValue(retorno.getTpSituacaoDocumento()));
		
		if(retorno.getProtocolo() != null){
			monitoramento.setNrProtocolo(NumberUtils.toLong(retorno.getProtocolo()));
		}
		
		if(retorno.getDocumentData() != null){
			monitoramento.setDsDadosDocumento(retorno.getDocumentData().getBytes(Charset.forName(UTF_8)));
		}
		
		monitoramentoDocEletronicoService.store(monitoramento);
		
		if(monitoramento.getDoctoServico().getDoctoServicoOriginal()!= null){
		    finalizacaoCteOriginal(monitoramento.getDoctoServico().getIdDoctoServico(), monitoramento.getDoctoServico().getDoctoServicoOriginal().getIdDoctoServico());
		}
	}
	
	private void finalizacaoCteOriginal(Long idDoctoServico, Long idDoctoServicoOriginal) {
		if (CollectionUtils.isNotEmpty(notaFiscalOperadaService.findByIdDoctoServico(idDoctoServico))) {
			registrarBaixaEntregaPorNotaFiscalService.executeFinalizacaoCteOriginal(idDoctoServicoOriginal, SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());
		}
	}

	public Short findNrCFOP(Long idDoctoServico){
		return getNfeConjugadaDAO().findNrCFOP(idDoctoServico);
	}
	
	private void createEnvio(DoctoServico doctoServico, Long nrFiscalRps, Short nrCfop, List<Map<String, Object>> listDados, Long idMonitoramento) {
		String txtObsAutomatica = mensagemService.getMessage(MSG_OBSERVACAO_DANFE, new Object[]{ getObservacao(doctoServico) });
		String dsBairroPadrao = parametroGeralService.findSimpleConteudoByNomeParametro("Bairro_padrao");
		
		Map<String, Object> map = listDados.get(0);
		map.put("nrFiscalRps", nrFiscalRps);
		map.put("nrCfop", nrCfop);
		map.put("idMonitoramento", idMonitoramento);
		map.put("txtObsAutomatica", txtObsAutomatica);
		
		setValueDefault(map,"bairroEmitente",dsBairroPadrao);
		setValueDefault(map,"bairroDestinatario",dsBairroPadrao);
		
		sendNFEToQueue(new NFEPacket(NFEConjugadaServiceHelper.getEnvio(listDados)));
	}
	
	private void setValueDefault(Map<String, Object> map, String key, String defaultValue){
		String value = (String)map.get(key);
		if(value == null || value.length() < 2){
			map.put(key, defaultValue);
		}
	}

	private String getObservacao(DoctoServico doctoServico) {
		return doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + FormatUtils.formatLongWithZeros(doctoServico.getNrDoctoServico(), "0000000000");
	}

	public void generateInutilizarNFE(DoctoServico doctoServico, Long idMonitoramento) {
		atualizaConhecimentoInutilizacao(doctoServico);
		
		notaFiscalConhecimentoService.removeByIdConhecimento(doctoServico.getIdDoctoServico());
		
		reciboReembolsoService.executeCancelaReciboByDoctoServico(doctoServico.getIdDoctoServico());
		
		storeInutilizar(doctoServico, SessionUtils.getFilialSessao().getIdFilial(), idMonitoramento);
	}
		
	private void atualizaConhecimentoInutilizacao(DoctoServico doctoServico) {
		ConhecimentoCancelarService.validateDataEmissao(doctoServico.getDhEmissao());

		validateInutilizar(doctoServico, PARAM_PRAZO_INUT_NFE_CONJUGADA);
		
		doctoServico.setTpSituacaoConhecimento(new DomainValue(ConstantesExpedicao.DOCUMENTO_SERVICO_CANCELADO));
		doctoServico.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		doctoServico.setUsuarioByIdUsuarioAlteracao(SessionUtils.getUsuarioLogado());
		
		final Long idLocalizacaoMercadoria = NumberUtils.toLong(String.valueOf(parametroGeralService.findConteudoByNomeParametro("ID_LOCA_DOC_CANCELADO", false)));
		final LocalizacaoMercadoria localizacaoMercadoria = new LocalizacaoMercadoria();
		localizacaoMercadoria.setIdLocalizacaoMercadoria(idLocalizacaoMercadoria);
		
		cancelarDevedorDocServFat(doctoServico);		
	}	

	private void cancelarDevedorDocServFat(DoctoServico doctoServico) {
		DevedorDocServFat devedorDocServFat = devedorDocServFatService.findDevedorDocServFatByIdDoctoServico(doctoServico.getIdDoctoServico());
		if(devedorDocServFat != null) {
			devedorDocServFat.setTpSituacaoCobranca(new DomainValue("L"));
			devedorDocServFat.setDtLiquidacao(YearMonthDay.fromDateFields(doctoServico.getDhEmissao().toDate()));
			devedorDocServFatService.store(devedorDocServFat);
		}
	}

	private void validateInutilizar(DoctoServico doctoServico,String parametro){
		BigDecimal prazoLegalInutCTE = (BigDecimal) parametroGeralService.findConteudoByNomeParametro(parametro, false);

		if(doctoServico.getDhEmissao().isBefore(JTDateTimeUtils.getDataHoraAtual().minusHours(prazoLegalInutCTE.intValue()))){
			throw new BusinessException("LMS-04377");
		}
		
		//Verifica se a filial do usuario logado eh a mesma filial de origem do documento de servico 
		Long idFilialOrigem = doctoServico.getFilialByIdFilialOrigem().getIdFilial();
		if(!idFilialOrigem.equals(SessionUtils.getFilialSessao().getIdFilial())) {
			throw new BusinessException("LMS-04084");
		}
	}

	/**
	 * Envia um objeto da família NFEConjugada para a fila.
	 * 
	 * @param nfe
	 */
	private void sendNFEToQueue(NFEPacket nfe) {
		if(nfe == null){
			return;
		}
		
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.NFE_CONJUGADA_SERVICE, nfe);
		integracaoJmsService.storeMessage(msg);
	}
	
	/**
	 * Processa o pedido de impressão de nota fiscal conjugada.
	 * 
	 * @param listNfeConjugada
	 * @return Map<String, JasperPrint>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findXMLData(Object listNfeConjugada) {		
		if(listNfeConjugada == null){
			return null;
		}
		
		Map<String, JasperPrint> reports = null;
			
		List<Long> listIdMonitoramento = getListAsLong((List<String>) listNfeConjugada);
		
		List<Object[]> listDocs = findDocsNFEConjugada(listIdMonitoramento);
				
		Map<String, Object> retorno = new HashMap<String, Object>();
		if(listDocs != null && !listDocs.isEmpty()){
			if(isRejected(listDocs, listIdMonitoramento.size())){
				throw new BusinessException("LMS-04540");
			}
			
			reports = createReport(reports, listDocs);
			retorno.put("itensProcessados", listDocs.size());
			retorno.put("reports", reports);
		}
		return retorno;
	}

	/**
	 * Cria jasper com base no xml da nfe autorizada, podendo ter várias
	 * páginas, uma para cada nota.
	 * 
	 * @param reports
	 * @param listDocs
	 * 
	 * @returnMap<String, JasperPrint>
	 */
	private Map<String, JasperPrint> createReport(Map<String, JasperPrint> reports, List<Object[]> listDocs) {
		Locale currentUserLocale = getCurrentReportLocale();
		
		try {	
			JasperPrint jasperPrint = new NFEConjugadaReportService(currentUserLocale, null).execute(listDocs, currentUserLocale);
			
			reports = new HashMap<String, JasperPrint>();
			reports.put("jasperPrint", jasperPrint);								
		} catch (Exception e) {
			throw new BusinessException("reportNotFound");
		}
		
		return reports;
	}
	
	/**
	 * Consulta os resultados dos monitoramentos da nota conjugada.
	 * 
	 * @param idMonitoramentoEletronico
	 * 
	 * @return List<Object[]> 
	 */
	private List<Object[]> findDocsNFEConjugada(List<Long> idMonitoramentoEletronico) {
		return nfeConjugadaDAO.findDocsNFEConjugada(idMonitoramentoEletronico);
	}
	
	/**
	 * Verifica se alguma nota foi rejeitada de acordo com a quantidade de
	 * monitoramentos em processamento.
	 * 
	 * @param listDocs
	 * @param numMonitoramento
	 * 
	 * @return boolean
	 */
	private boolean isRejected(List<Object[]> listDocs, int numMonitoramento){
		boolean reject = false;
		
		for (Object[] doc : listDocs) {			
			DomainValue tpSituacaoDocumento = (DomainValue) doc[1];
			
			if(ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_REJEITADO.equals(tpSituacaoDocumento.getValue())){
				reject = true;
			}
		}
		
		return reject && listDocs.size() == numMonitoramento;
	}
	
	/**
	 * Converte a lista de Strings para lista de representação numerica.
	 * 
	 * @param listNfeConjugada
	 * @return List<Long>
	 */
	private List<Long> getListAsLong(List<String> listNfeConjugada) {		
		List<Long> ids = new ArrayList<Long>();
		
		for (String idMonitoramento : listNfeConjugada) {
			ids.add(NumberUtils.toLong(idMonitoramento));
		}
		
		return ids;
	}
	
	private Locale getCurrentReportLocale() {
		Usuario currentUser = SessionUtils.getUsuarioLogado();
		return (currentUser != null && currentUser.getLocale() != null) ? currentUser.getLocale() : new Locale("pt", "BR");
	}
	
	/**
	 * Verifica se a filial logada está com a nota fiscal conjugada habilitada.
	 * 
	 * @return boolean
	 */
	public boolean isAtivaNfeConjugada(Long idFilial) {		
		if(idFilial == null){
			return false;
		}
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(idFilial, PARAM_ATIVA_NFE_CONJUGADA, false, true);
		
		if (conteudoParametroFilial != null && "S".equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			return true;
		}
		
		return false;
	}
	
	public Long getTempoRetorno() {
		ParametroGeral tempoRetorno = parametroGeralService.findByNomeParametro(PARAM_TEMPO_RETORNO_NFE_CONJUGADA, false);
		
		if (tempoRetorno != null) {
			return NumberUtils.toLong(tempoRetorno.getDsConteudo());
		}
		
		return null;
	}	
	
	public NFEConjugadaDAO getNfeConjugadaDAO() {
		return nfeConjugadaDAO;
	}

	public void setNfeConjugadaDAO(NFEConjugadaDAO nfeConjugadaDAO) {
		this.nfeConjugadaDAO = nfeConjugadaDAO;
	}

	public IntegracaoJmsService getIntegracaoJmsService() {
		return integracaoJmsService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public MonitoramentoDocEletronicoService getMonitoramentoDocEletronicoService() {
		return monitoramentoDocEletronicoService;
	}

	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public DevedorDocServFatService getDevedorDocServFatService() {
		return devedorDocServFatService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
		return notaFiscalConhecimentoService;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public ReciboReembolsoService getReciboReembolsoService() {
		return reciboReembolsoService;
	}

	public void setReciboReembolsoService(
			ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}

	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public NotaFiscalEletronicaService getNotaFiscalEletronicaService() {
		return notaFiscalEletronicaService;
	}

	public void setNotaFiscalEletronicaService(
			NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

	public MensagemService getMensagemService() {
		return mensagemService;
	}

	public void setMensagemService(MensagemService mensagemService) {
		this.mensagemService = mensagemService;
	}

	public void setNotaFiscalOperadaService(
			NotaFiscalOperadaService notaFiscalOperadaService) {
		this.notaFiscalOperadaService = notaFiscalOperadaService;
	}

	public void setRegistrarBaixaEntregaPorNotaFiscalService(
			RegistrarBaixaEntregaPorNotaFiscalService registrarBaixaEntregaPorNotaFiscalService) {
		this.registrarBaixaEntregaPorNotaFiscalService = registrarBaixaEntregaPorNotaFiscalService;
	}
}
