package com.mercurio.lms.expedicao.swt.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.xml.sax.SAXException;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.PreManifestoVolumeService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.entrega.model.service.RegistrarBaixaEntregaPorNotaFiscalService;
import com.mercurio.lms.expedicao.model.CartaCorrecaoEletronica;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.TBIntegration;
import com.mercurio.lms.expedicao.model.service.CancelarConhecimentoEletronicoService;
import com.mercurio.lms.expedicao.model.service.CartaCorrecaoEletronicaService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.model.service.NFEConjugadaService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaRetornoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.expedicao.report.EmitirCartaCorrecaoEletronicaService;
import com.mercurio.lms.expedicao.report.InutilizarConhecimentoEletronicoService;
import com.mercurio.lms.expedicao.report.ReenviarConhecimentoEletronicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

@Assynchronous(name = "monitoramentoDocEletronicoAction")
public class MonitoramentoDocEletronicoAction extends CrudAction {

	private static final String PRAZO_CCE_CTE = "PRAZO_CCE_CTE";
	private static final String NUMERO_LIMITE_CCE_CTE = "NUMERO_LIMITE_CCE_CTE";
	
	private FilialService filialService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private DevedorDocServService devedorDocServService;
	private CancelarConhecimentoEletronicoService cancelarConhecimentoEletronicoService;
	private ReenviarConhecimentoEletronicoService reenviarConhecimentoEletronicoService;
	private InutilizarConhecimentoEletronicoService inutilizarConhecimentoEletronicoService;
	private InscricaoEstadualService inscricaoEstadualService;
	private PessoaService pessoaService;
    private FaturaService faturaService;
	private ConfiguracoesFacade configuracoesFacade; 
	private PreManifestoVolumeService preManifestoVolumeService;
	private NotaFiscalEletronicaRetornoService notaFiscalEletronicaRetornoService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private ClienteService clienteService;
	private DoctoServicoService doctoServicoService;
	private ConhecimentoService conhecimentoService;
	private CartaCorrecaoEletronicaService cartaCorrecaoEletronicaService;
	private ParametroGeralService parametroGeralService;
	private ReportExecutionManager reportExecutionManager;
	private EmitirCartaCorrecaoEletronicaService emitirCartaCorrecaoEletronicaService;
	private NFEConjugadaService nfeConjugadaService;
	private NotaFiscalOperadaService notaFiscalOperadaService;
	private RegistrarBaixaEntregaPorNotaFiscalService registrarBaixaEntregaPorNotaFiscalService;
	
	@SuppressWarnings("rawtypes")
    public List findByNrFaturaIdFilialOrigemTpSituacaoFatura(TypedFlatMap criteria) {
	Long nrFatura = LongUtils.getLong(criteria.getInteger("nrFatura"));
	Long idFilial = criteria.getLong("idFilial");
	List result = this.faturaService.findByNrFaturaIdFilialOrigemTpSituacaoFatura(nrFatura, idFilial, "CA");

	if (result.isEmpty()) {
	    throw new BusinessException("LMS-04401");
	}

	return result;
    }

    public TypedFlatMap enviarInformacoesFatura(TypedFlatMap param) throws UnsupportedEncodingException, Exception {
    	reenviarConhecimentoEletronicoService.executeEnviarCTEFatura((Long)param.get("idFatura"));
    	return param;
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupFilial(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}
	
	public void cancelar(Map<String, Object> criteria) throws UnsupportedEncodingException, Exception {
		MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findById((Long) criteria.get("idMonitoramentoDocEletronic"));
		cancelarConhecimentoEletronicoService.cancelar(monitoramentoDocEletronico);
	}

	// LMSA-6159 LMSA-6249
    public void generateNotFisFedEXAgainByMonitoramentoDocEletronico(Map<String, Object> criteria) throws UnsupportedEncodingException, Exception {
        Long idMonitoramentoDocEletronico = (Long) criteria.get("idMonitoramentoDocEletronico");
        monitoramentoDocEletronicoService.generateNotFisFedEXAgainByMonitoramentoDocEletronico(idMonitoramentoDocEletronico);
    }
    
	@SuppressWarnings("rawtypes")
	public Map<String, Object> verificaSeConhecimentoEstaPreManifestado(Map<String, Object> criteria) {
		Map<String, Object> result = new HashMap<String, Object>();
		Long idMonitoramentoDocEletronic = (Long) criteria.get("idMonitoramentoDocEletronic");
		MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findById(idMonitoramentoDocEletronic);
		List list = getPreManifestoVolumeService().findbyIdDoctoServico(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
		boolean conhecimentoEstaPreManifestado = (list!=null && !list.isEmpty());
		result.put("conhecimentoEstaPreManifestado", conhecimentoEstaPreManifestado);
		return result;
	}	
		
	public void excluirPreManifestoVolumeDoDocumentoServico(Map<String, Object> criteria){
		Long idMonitoramentoDocEletronic = (Long) criteria.get("idMonitoramentoDocEletronic");
		MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findById(idMonitoramentoDocEletronic);
		preManifestoVolumeService.removePreManifestoVolumeDoDocumentoServico(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
	}
	
	public Map<String, Object> findById(java.lang.Long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findById(id);
		
		result.put("idMonitoramentoDocEletronic", monitoramentoDocEletronico.getIdMonitoramentoDocEletronic());
		
		DoctoServico doctoServico = monitoramentoDocEletronico.getDoctoServico();
		result.put("idFilial", doctoServico.getFilialByIdFilialOrigem().getIdFilial());
		result.put("sgFilial", doctoServico.getFilialByIdFilialOrigem().getSgFilial()); 
		result.put("nmFantasia", doctoServico.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
		
		Long idDoctoServico = doctoServico.getIdDoctoServico();
		result.put("idDoctoServico", idDoctoServico);		
		result.put("tpDocumentoServico", doctoServico.getTpDocumentoServico().getValue());
		result.put("nrDoctoServico", doctoServico.getNrDoctoServico());
		result.put("dhEmissao", doctoServico.getDhEmissao());
		result.put("tpSituacaoDocEletronico", monitoramentoDocEletronico.getTpSituacaoDocumento().getValue());
		
		if (doctoServico instanceof Conhecimento) {
			Conhecimento conhecimento = (Conhecimento) doctoServico;
			result.put("tpSituacaoConhecimento", conhecimento.getTpSituacaoConhecimento().getValue());
		}
		
		List<TypedFlatMap> cceAprovadas = cartaCorrecaoEletronicaService.findAprovadasByIdDoctoServico(idDoctoServico);
		if (!cceAprovadas.isEmpty()) {
			TypedFlatMap map = cceAprovadas.get(cceAprovadas.size() - 1);
			Long nrProtocolo = map.getLong("nrProtocolo");
			result.put("nrProtocolo", nrProtocolo);
		}
		
		DevedorDocServ devedorDocServ = devedorDocServService.findDevedorByDoctoServico(idDoctoServico);
		result.put("devedorDoctoServico", devedorDocServ.getCliente().getPessoa().getNmPessoa()); 
				
		result.put("vlTotalDoctoServico", doctoServico.getVlTotalDocServico());
		result.put("dsObservacao", monitoramentoDocEletronico.getDsObservacao());
	
		result.put("dhEnvio", monitoramentoDocEletronico.getDhEnvio());
		result.put("blEnviadoCliente", monitoramentoDocEletronico.getDsEnvioEmail() != null);
		result.put("dsEnvioEmail", monitoramentoDocEletronico.getDsEnvioEmail()); 
		result.put("nrDocumentoEletronico", monitoramentoDocEletronico.getNrDocumentoEletronico());
	
		result.put("hasXmlData", Boolean.valueOf(monitoramentoDocEletronico.getDsDadosDocumento() != null));
	
		if(!ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equalsIgnoreCase(doctoServico.getTpDocumentoServico().getValue())){
			Conhecimento conhecimento =  conhecimentoService.findById(devedorDocServ.getDoctoServico().getIdDoctoServico());
			result.put("tpConhecimento", conhecimento.getTpConhecimento().getValue());
		}
	
		if( doctoServico.getClienteByIdClienteRemetente() != null ){
	    putDadosClientes(result, "Remetente", doctoServico
		    .getClienteByIdClienteRemetente());
		}

		if( doctoServico.getClienteByIdClienteRemetente() != null ){
	    putDadosClientes(result, "Destinatario", doctoServico
		    .getClienteByIdClienteDestinatario());
		}
		
		if( devedorDocServ.getCliente() != null ){
			putDadosClientes(result,"Tomador",devedorDocServ.getCliente());
		}

		if( doctoServico.getClienteByIdClienteRedespacho() != null ){
	    putDadosClientes(result, "Expedidor", doctoServico
		    .getClienteByIdClienteRedespacho());
		}

		if( doctoServico.getClienteByIdClienteConsignatario() != null ){
	    putDadosClientes(result, "Recebedor", doctoServico
		    .getClienteByIdClienteConsignatario());
		}
		
		result.put("ativaNfeConjugada", nfeConjugadaService.isAtivaNfeConjugada(doctoServico.getFilialByIdFilialOrigem().getIdFilial()));
		result.put("tempoRetorno", nfeConjugadaService.getTempoRetorno());
		
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void putDadosClientes(Map map, String tipo, Cliente cliente){
		Pessoa pessoa = pessoaService.findById(cliente.getIdCliente());
		
		map.put("cnpj"+tipo, pessoa.getNrIdentificacao());
		map.put("nm"+tipo, pessoa.getNmPessoa());
		
		map.put("listIE"+tipo,new ArrayList<Map>());
	List<InscricaoEstadual> list = inscricaoEstadualService.findInscricaoEstadualAtivaByPessoa(cliente
		.getIdCliente());
		for(InscricaoEstadual ie : list ){
			Map<String, Object> m = new HashMap<String, Object>();
			if( BooleanUtils.isTrue(ie.getBlIndicadorPadrao()) ){
				map.put("idIE"+tipo,ie.getIdInscricaoEstadual());
			}
			m.put("idIE"+tipo,ie.getIdInscricaoEstadual());
			m.put("nrIE"+tipo,ie.getNrInscricaoEstadual());
			((List)map.get("listIE"+tipo)).add(m);
		}
	}
	
	public List<TypedFlatMap> findXmlByIdDoctoServico(TypedFlatMap criteria) {
		List<TypedFlatMap> l = monitoramentoDocEletronicoService.findXmlByIdDoctoServico(criteria.getLong("idDoctoServico"));
		
		return l;
	}
	
	public List<TypedFlatMap> findHistoricoCCEByIdDoctoServico(TypedFlatMap criteria) {
		Long idDoctoServico = criteria.getLong("idDoctoServico");
		List<TypedFlatMap> list = cartaCorrecaoEletronicaService.findHistoricoByIdDoctoServico(idDoctoServico);
		return list;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) { 
		return monitoramentoDocEletronicoService.getRowCountMonitoramentoDocEletronico(criteria);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public ResultSetPage findPaginated(TypedFlatMap criteria){
		ResultSetPage rsp = monitoramentoDocEletronicoService.findPaginatedMonitoramentoDocEletronico(criteria);
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}
	
	@SuppressWarnings({ "unchecked" })
    public Map<String, Object> regerar(TypedFlatMap criteria) {
//		Map<String, Object> map = new HashMap<String, Object>();
		monitoramentoDocEletronicoService.executeRegerarCTE(criteria);
		reenviarConhecimentoEletronicoService.generateReenvioConhecimentoEletronico(criteria.getLong("idMonitoramentoDocEletronic"));
		return findById(criteria.getLong("idMonitoramentoDocEletronic"));
	}

	public Map<String, Object> reemitir(TypedFlatMap criteria) {
		return monitoramentoDocEletronicoService.executeReemitir(criteria);
	}
	
	public Map<String, Object> reenviar(TypedFlatMap criteria) {
		reenviarConhecimentoEletronicoService.generateReenvioConhecimentoEletronico(criteria.getLong("idMonitoramentoDocEletronic"));
		return findById(criteria.getLong("idMonitoramentoDocEletronic"));
	}
	
	public Map<String, Object> reenviarDataAtual(TypedFlatMap criteria) {
		reenviarConhecimentoEletronicoService.generateReenvioConhecimentoEletronicoDataAtual(criteria.getLong("idMonitoramentoDocEletronic"),true);
		return findById(criteria.getLong("idMonitoramentoDocEletronic"));
	}
	
	public Map<String, Object> regerarRpsMotivoFiscal(TypedFlatMap criteria) {
		Long idMonitoramentoDocEletronic = criteria.getLong("idMonitoramentoDocEletronic");
		
		//Rotina "AO CLICAR NO BOTÃO REGERAR RPS POR MOTIVO FISCAL" (item 3.38). Foi optado ser desenvolvido na action por que precisa dois contextos transacionais diferentes atualizando a mesma tabela.
		
		//Atualiza os dados do cálculo e reinclui os impostos do documento de serviço, comitando a transação
		Long proximoNumeroRps = doctoServicoService.executeAtualizarDoctoServicoRegerarRps(idMonitoramentoDocEletronic);

		notaFiscalEletronicaService.executeRegerarNotaFiscalEletronica(idMonitoramentoDocEletronic, proximoNumeroRps);
		
		return findById(idMonitoramentoDocEletronic);
	}
	
	public Map<String, Object> reenviarCliente(TypedFlatMap criteria) throws UnsupportedEncodingException, Exception {
	reenviarConhecimentoEletronicoService.generateReenvioClienteConhecimentoEletronico(criteria
		.getLong("idMonitoramentoDocEletronic"));
		
		return findById(criteria.getLong("idMonitoramentoDocEletronic"));
	}
	
	public Map<String, Object> inutilizar(TypedFlatMap criteria) {
		inutilizarConhecimentoEletronicoService.generateInutilizacaoConhecimentoRejeitado(criteria.getLong("idMonitoramentoDocEletronic"));
		return findById(criteria.getLong("idMonitoramentoDocEletronic"));
	}
	
	@SuppressWarnings("rawtypes")
    private void executeInutilizacaoAutomarica(String dtInicio, String dtFim){
		BigDecimal idLocalizacaoMercadoria = (BigDecimal)configuracoesFacade.getValorParametro("ID_LOCA_DOC_CANCELADO");
		
		List<Map> rejeitados = monitoramentoDocEletronicoService.findDocumentosRejeitados(dtInicio, dtFim);
		for(Map m : rejeitados ){
			monitoramentoDocEletronicoService.removeDocumentoRejeitado((Long)m.get("idDoctoServico"),(Long)m.get("idFilial"),
					(String)m.get("sgFilial"),(Long)m.get("nrConhecimento"),((DomainValue)m.get("tpDocumentoServico")).getValue(),idLocalizacaoMercadoria.longValue());
		}
	}
	
	@AssynchronousMethod( name = "expedicao.monitoramentoDocEletronicoService.executeInutilizacaoAutomaticaDiaria",
			  type = BatchType.BATCH_SERVICE,
			  feedback = BatchFeedbackType.ON_ERROR)
	public void executeInutilizacaoAutomaricaDiaria(){

		BigDecimal qtdHoras = (BigDecimal)configuracoesFacade.getValorParametro("HR_AUTO_INUTILIZACAO");
		
		DateTime dt = JTDateTimeUtils.getDataHoraAtual();
		String dtInicio = "01" + dt.toString("MMyyyy");
		dt = dt.minusHours(qtdHoras.intValue());
		String dtFim = dt.toString("ddMMyyyy");
		executeInutilizacaoAutomarica(dtInicio,dtFim);
	}

	@AssynchronousMethod( name = "expedicao.monitoramentoDocEletronicoService.executeInutilizacaoAutomaticaMensal",
			  type = BatchType.BATCH_SERVICE,
			  feedback = BatchFeedbackType.ON_ERROR)
	public void executeInutilizacaoAutomaricaMensal(){
		DateTime dt = JTDateTimeUtils.getDataHoraAtual();

		String dtFim = "01" + dt.toString("MMyyyy");
		dt = dt.minusMonths(1);
		String dtInicio = "01" + dt.toString("MMyyyy");

		executeInutilizacaoAutomarica(dtInicio,dtFim);
	}
	
	/**
	 * 04.01.01.20 - Monitoramento de Documentos Eletrônicos
	 * Rotina Atualiza_Retorno_NFSe
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 * 
	 */
	@AssynchronousMethod( name="expedicao.Atualiza_Retorno_NFSe",
			type = BatchType.BATCH_SERVICE,
			feedback = BatchFeedbackType.ON_ERROR)
	public void executeAtualizaRetornoNFSe() throws IOException, ParserConfigurationException, XPathExpressionException, SAXException{
		
		//1 - Selecionar, na tabela TBINTEGRATION,  todos os registros que estejam com DOCSTATUS = 0 (zero).
		List<TBIntegration> listNotaFiscalEletronicaRetorno = notaFiscalEletronicaRetornoService.findNotaFiscalEletronicaRetornoByDocStatus(0);//não processados
		
		//2
		for(TBIntegration notaFiscalEletronicaRetorno : listNotaFiscalEletronicaRetorno) {
			
			//Executa em uma nova transação. Se der erro lá, aborta apenas do registro atual em diante.
			notaFiscalEletronicaService.executeAtualizaRetornoNFSe(notaFiscalEletronicaRetorno);

		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> findLookupCliente(Map criteria) {
		List<Map<String, Object>> clientes = clienteService.findClienteByNrIdentificacao((String) criteria.get("nrIdentificacao"));
		if (clientes != null) {
			for(Map cliente : clientes) {
				cliente.remove("tpCliente");
				cliente.put("idCliente", cliente.get("idCliente"));
				Map pessoa = (Map) cliente.remove("pessoa");
				if (pessoa != null) {
					cliente.put("nmPessoa", pessoa.get("nmPessoa"));
					cliente.put("nrIdentificacao", pessoa.remove("nrIdentificacaoFormatado"));
					
				}
			}
		}
		return clientes;
	}
	
	public Map<String, Object> executeUpdateNroNfse(TypedFlatMap map) {

		Long idMonitoramentoDocEletronic = map.getLong("idMonitoramentoDocEletronic");
		Long nrDocumentoEletronico = map.getLong("nrDocumentoEletronico");
		DateTime txtDtAutorizacao = JTDateTimeUtils.yearMonthDayToDateTime(map.getYearMonthDay("txtDtAutorizacao"));
		
		monitoramentoDocEletronicoService.executeUpdateNroNfse(idMonitoramentoDocEletronic, nrDocumentoEletronico , txtDtAutorizacao);
		
		Map<String, Object> retorno = findById(map.getLong("idMonitoramentoDocEletronic"));
		
		Long idDoctoServico = (Long) retorno.get("idDoctoServico");

		DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico);
		if(doctoServico.getDoctoServicoOriginal()!=null){
		    finalizacaoCteOriginal(idDoctoServico, doctoServico.getDoctoServicoOriginal().getIdDoctoServico());
		}
		
		return retorno;
	}

	private void finalizacaoCteOriginal(Long idDoctoServico, Long idDoctoServicoOriginal) {
		if (CollectionUtils.isNotEmpty(notaFiscalOperadaService.findByIdDoctoServico(idDoctoServico))) {
			registrarBaixaEntregaPorNotaFiscalService.executeFinalizacaoCteOriginal(idDoctoServicoOriginal, SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());
		}
	}
	
	public void validateNrDocumentoEletronico(TypedFlatMap criteria) {
    	Long idFilialOrigemDocumento = (Long)criteria.get("idFilialOrigemDocumento");
    	Long nrDocumentoEletronico = (Long)criteria.get("nrDocumentoEletronico");
    	this.monitoramentoDocEletronicoService.validateNrDocumentoEletronico(idFilialOrigemDocumento, nrDocumentoEletronico);
    }
	
	public void validateDtAutorizacao(TypedFlatMap criteria) {
		Long idFilialOrigemDocumento = (Long)criteria.get("idFilialOrigemDocumento");
		YearMonthDay dtAutorizacao = (YearMonthDay)criteria.get("dtAutorizacao");
		this.monitoramentoDocEletronicoService.validateDtAutorizacao(idFilialOrigemDocumento, dtAutorizacao);
	}
	
	public TypedFlatMap storeCartaCorrecao(TypedFlatMap map) {
		validateEmissao(map);
		Long  idDoctoServico = map.getLong("idDoctoServico");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> listCorrecao = map.getList("listCamposCorrigido");
		List<CartaCorrecaoEletronica> listCceToSave = new ArrayList<CartaCorrecaoEletronica>();
		Long nrCartaCce = cartaCorrecaoEletronicaService.validateLimiteCCEByIdDoctoServico(idDoctoServico);
		
		for (Map<String, Object> mCorrecao : listCorrecao) {
			CartaCorrecaoEletronica cce = new CartaCorrecaoEletronica();
			TypedFlatMap tfmCorrecao = new TypedFlatMap(mCorrecao);
			cce.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
			DoctoServico doctoServico = new DoctoServico();
			doctoServico.setIdDoctoServico(idDoctoServico);
			cce.setDoctoServico(doctoServico);
			cce.setNrCartaCorrecaoEletronica(nrCartaCce + 1);
			cce.setIdTagCampo(tfmCorrecao.getString("tag"));
			cce.setIdTagGrupo(tfmCorrecao.getString("grupo"));
			cce.setTpSituacaoCartaCorrecao("E");
			cce.setDsConteudoDocto(tfmCorrecao.getString("sgValorCTE"));
			cce.setDsConteudoCarta(tfmCorrecao.getString("sgValorCorrigido"));
			
			listCceToSave.add(cce);
		}
		
		cartaCorrecaoEletronicaService.storeAll(listCceToSave);
		gerarXMLCartaCorrecao(map);
		
		map.put("tempoRespostaSefaz", configuracoesFacade.getValorParametro("TEMPO_RESPOSTA_SEFAZ"));
		return map;
	}
	
	private void validateEmissao(TypedFlatMap map) {
		/*
		 * Verificar se o limite de cartas para o documento não foi atingido.
		 * Verificar se existe registro na tabela CARTA_CORRECAO_ELETRONICA,
		 * onde ID_DOCTO_SERVICO igual ao ID do documento selecionado e
		 * NR_CARTA_CORRECAO_ELETRONICA igual ou maior que o conteúdo do
		 * parâmetro geral PARAMETRO_GERAL.NUMERO_LIMITE_CCE_CTE. Caso positivo,
		 * apresentar a mensagem LMS-04492, alterando o coringa {0} pela
		 * informação do parâmetro geral PARAMETRO_GERAL.NUMERO_LIMITE_CCE_CTE.
		 */
		ParametroGeral p = parametroGeralService.findByNomeParametro(PRAZO_CCE_CTE);
		TypedFlatMap flatMap = new TypedFlatMap(map);
		DateTime dhEmissao = flatMap.getDateTime("dhEmissao");
		
		int dif = JTDateTimeUtils.getIntervalInDays(new YearMonthDay(dhEmissao.getMillis()), JTDateTimeUtils.getDataAtual());
		
		if(dif > Integer.valueOf(p.getDsConteudo()).intValue()) {
			throw new BusinessException("LMS-04489");
		}
		
		String tpSituacaoConhecimento = flatMap.getString("tpSituacaoConhecimento");
		if (!"E".equals(tpSituacaoConhecimento)) {
			throw new BusinessException("LMS-04490");
		}
		
		Long idDoctoServico = flatMap.getLong("idDoctoServico");
		Conhecimento c = conhecimentoService.loadConhecimentosSubstituto(idDoctoServico);
		if(c != null){
			String tipoDoc = c.getTpDocumentoServico().getDescriptionAsString();
			String sgFilialOrig = c.getFilialOrigem().getSgFilial();
			Long nrDoc = c.getNrDoctoServico();
			String label = tipoDoc + " " + sgFilialOrig + " " + nrDoc; 
			throw new BusinessException("LMS-04491", new Object[]{label});
		}
	
		ParametroGeral pNrLimiteCce = parametroGeralService.findByNomeParametro(NUMERO_LIMITE_CCE_CTE);
		Long nrLimiteCce = LongUtils.getLong(pNrLimiteCce.getDsConteudo());
		Long nrCartaCce = cartaCorrecaoEletronicaService.validateLimiteCCEByIdDoctoServico(idDoctoServico);
		
		if(nrCartaCce != null && nrCartaCce.compareTo(nrLimiteCce) >= 0) {
			throw new BusinessException("LMS-04492", new Object[]{nrLimiteCce});
		}
		
	}

	private void gerarXMLCartaCorrecao(TypedFlatMap map) {
		cartaCorrecaoEletronicaService.executeGerarXMLCartaCorrecao(map);
	}
	
	public TypedFlatMap executeProcessarIntegracaoCCE(TypedFlatMap map) {
		Long idDoctoServico = (Long) map.get("idDoctoServico");
		String dsObservacao = cartaCorrecaoEletronicaService.executeProcessarIntegracaoCCE(idDoctoServico);
		if (dsObservacao != null) {
			map.put("dsObservacao", dsObservacao);
			return map;
		}
		return null;
	}
	
	public String executeCartaCorrecaoEletronica(TypedFlatMap parameters) throws Exception {
		File report = createFileCCeReport(parameters);
		
		return executeReportCCe(report);
	}
	
	public void executeEnviarEmailCCE(TypedFlatMap parameters) {
		File pdf = null;
		try {
			pdf = createFileCCeReport(parameters);
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
		cartaCorrecaoEletronicaService.executeEnviarEmailCCE(parameters, pdf);
	}
	
	private File createFileCCeReport(TypedFlatMap parameters) throws Exception {
		return reportExecutionManager.executeReport(emitirCartaCorrecaoEletronicaService, parameters);
	}
	
	private String executeReportCCe(File report) throws Exception {
		return reportExecutionManager.generateReportLocator(report);
	}
	
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

    public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setCancelarConhecimentoEletronicoService(
			CancelarConhecimentoEletronicoService cancelarConhecimentoEletronicoService) {
		this.cancelarConhecimentoEletronicoService = cancelarConhecimentoEletronicoService;
	}

	public void setReenviarConhecimentoEletronicoService(
			ReenviarConhecimentoEletronicoService reenviarConhecimentoEletronicoService) {
		this.reenviarConhecimentoEletronicoService = reenviarConhecimentoEletronicoService;
	}

	public void setInutilizarConhecimentoEletronicoService(
			InutilizarConhecimentoEletronicoService inutilizarConhecimentoEletronicoService) {
		this.inutilizarConhecimentoEletronicoService = inutilizarConhecimentoEletronicoService;
	}

    public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
    }

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

    public void setFaturaService(FaturaService faturaService) {
    	this.faturaService = faturaService;
    }
    
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setPreManifestoVolumeService(PreManifestoVolumeService preManifestoVolumeService) {
		this.preManifestoVolumeService = preManifestoVolumeService;
	}

	public PreManifestoVolumeService getPreManifestoVolumeService() {
		return preManifestoVolumeService;
	}
	
	public void setNotaFiscalEletronicaRetornoService(NotaFiscalEletronicaRetornoService notaFiscalEletronicaRetornoService) {
		this.notaFiscalEletronicaRetornoService = notaFiscalEletronicaRetornoService;
	}
	
	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}
	
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	
	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	
	public void setCartaCorrecaoEletronicaService(
			CartaCorrecaoEletronicaService cartaCorrecaoEletronicaService) {
		this.cartaCorrecaoEletronicaService = cartaCorrecaoEletronicaService;
	}


	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public EmitirCartaCorrecaoEletronicaService getEmitirCartaCorrecaoEletronicaService() {
		return emitirCartaCorrecaoEletronicaService;
	}

	public void setEmitirCartaCorrecaoEletronicaService(
			EmitirCartaCorrecaoEletronicaService emitirCartaCorrecaoEletronicaService) {
		this.emitirCartaCorrecaoEletronicaService = emitirCartaCorrecaoEletronicaService;
	}

	public NFEConjugadaService getNfeConjugadaService() {
		return nfeConjugadaService;
	}

	public void setNfeConjugadaService(NFEConjugadaService nfeConjugadaService) {
		this.nfeConjugadaService = nfeConjugadaService;
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
