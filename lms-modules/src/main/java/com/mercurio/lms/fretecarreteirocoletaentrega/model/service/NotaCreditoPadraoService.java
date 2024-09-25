package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoNotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.NotaCreditoPadraoDAO;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.helper.NotaCreditoPadraoReportHelper;
import com.mercurio.lms.fretecarreteirocoletaentrega.utils.ConstantesEventosNotaCredito;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Service responsável por operações de nota de crédito padrão.
 * 
 */
public class NotaCreditoPadraoService extends CrudService<NotaCredito, Long> {

	private TabelaFreteCarreteiroCeService tabelaFreteCarreteiroCeService;	
	private ExecuteWorkflowNotaCreditoPadraoService executeWorkflowNotaCreditoPadraoService;
	private ExecuteWorkflowCalculoNotaCreditoPadraoService executeWorkflowCalculoNotaCreditoPadraoService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;	
	private EventoNotaCreditoService eventoNotaCreditoService;
	
	private NotaCreditoPadraoDAO getNotaCreditoPadraoDAO() {
		return (NotaCreditoPadraoDAO) getDao();
	}

	public void setNotaCreditoPadraoDAO(NotaCreditoPadraoDAO dao) {
		setDao(dao);
	}

	public TabelaFreteCarreteiroCeService getTabelaFreteCarreteiroCeService() {
		return tabelaFreteCarreteiroCeService;
	}

	public void setTabelaFreteCarreteiroCeService(
			TabelaFreteCarreteiroCeService tabelaFreteCarreteiroCeService) {
		this.tabelaFreteCarreteiroCeService = tabelaFreteCarreteiroCeService;
	}

	public ExecuteWorkflowNotaCreditoPadraoService getExecuteWorkflowNotaCreditoPadraoService() {
		return executeWorkflowNotaCreditoPadraoService;
	}

	public void setExecuteWorkflowNotaCreditoPadraoService(
			ExecuteWorkflowNotaCreditoPadraoService executeWorkflowNotaCreditoPadraoService) {
		this.executeWorkflowNotaCreditoPadraoService = executeWorkflowNotaCreditoPadraoService;
	}

	public ManifestoEntregaDocumentoService getManifestoEntregaDocumentoService() {
		return manifestoEntregaDocumentoService;
	}

	public void setManifestoEntregaDocumentoService(
			ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}
	
	public NotaCredito findById(java.lang.Long id) {
		return (NotaCredito) super.findById(id);
	}

	/**
	 * Não é possível excluir uma nota de crédito.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@SuppressWarnings({ "rawtypes" })
	public void removeByIds(List ids) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String, Object>> findPaginatedCustom(TypedFlatMap criteria) {
		return getNotaCreditoPadraoDAO().findPaginatedCustom(criteria, FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getNotaCreditoPadraoDAO().getRowCountCustom(criteria);
	}

	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String, Object>> findReportData(TypedFlatMap criteria) {
		return getNotaCreditoPadraoDAO().findPaginatedCustom(criteria, FindDefinition.createFindDefinition(criteria));
	}

	/**
	 * Grava uma nota de crédito.
	 * 
	 * @param notaCredito
	 * @param listAnexoNotaCredito
	 * 
	 * @return NotaCredito
	 */
	public NotaCredito storeNotaCredito(NotaCredito notaCredito, List<AnexoNotaCredito> listAnexoNotaCredito) {
		NotaCredito notaCreditoDB = findById(notaCredito.getIdNotaCredito());
		
		prepare(notaCredito, notaCreditoDB);
		
		/*
		 * Grava nota de crédito.
		 */
		getNotaCreditoPadraoDAO().store(notaCreditoDB);

		/*
		 * Se foram inseridos novos anexos, salva todos eles.
		 */
		storeAnexoNotaCredito(notaCreditoDB, listAnexoNotaCredito);
		
		/*
		 * Gera workflow se necessário. 
		 */
		executeWorkflowNotaCreditoPadraoService.generatePendencia(notaCreditoDB);

		return notaCreditoDB;
	}

	/**
	 * Grava a nota de crédito exclusivamente gerada pelo cálculo.
	 * 
	 * @param notaCredito
	 */
	public void storeCalculoNotaCredito(NotaCredito notaCredito) {
		this.store(notaCredito);
		
		if(notaCredito.getNotaCreditoCalculoPadraoItens() != null){
			getNotaCreditoPadraoDAO().storeAllParcela(notaCredito.getNotaCreditoCalculoPadraoItens());		
		}
		
		if(notaCredito.getNotaCreditoDoctoItens() != null){
			getNotaCreditoPadraoDAO().storeAllDoct(notaCredito.getNotaCreditoDoctoItens());			
		}	
		
		if(notaCredito.getNotaCreditoParcelas() != null){
			getNotaCreditoPadraoDAO().storeAllParcelas(notaCredito.getNotaCreditoParcelas());			
		}	
	}
	
	/**
	 * Verifica se é necessário alterar a nota de crédito com alguma informação.
	 * 
	 * @param notaCredito
	 * @param notaCreditoDB
	 */
	private void prepare(NotaCredito notaCredito, NotaCredito notaCreditoDB) {
		
		BigDecimal vlTotal = notaCreditoDB.getVlTotal();		
		BigDecimal vlDesconto = notaCredito.getVlDescontoSugerido();
		
		
		if (vlTotal == null || (vlDesconto != null && vlDesconto.compareTo(vlTotal) > 0)) {
			throw new BusinessException("LMS-25120");
		}
		
		notaCreditoDB.setVlDescontoSugerido(notaCredito.getVlDescontoSugerido());
		notaCreditoDB.setVlAcrescimoSugerido(notaCredito.getVlAcrescimoSugerido());
		notaCreditoDB.setObNotaCredito(notaCredito.getObNotaCredito());			
	}
	
	@Override
	protected Serializable store(NotaCredito bean) {
		return super.store(bean);
	}
	
	/**
	 * Grava os anexos da nota de crédito, se houverem.
	 * 
	 * @param notaCredito
	 * @param listAnexoNotaCredito
	 */
	private void storeAnexoNotaCredito(NotaCredito notaCredito, List<AnexoNotaCredito> listAnexoNotaCredito) {
		if(listAnexoNotaCredito == null || listAnexoNotaCredito.isEmpty()){
			return;
		}
		
		/*
		 * Define a entidade do desconto atualizada para cada anexo.
		 */
		for (AnexoNotaCredito anexoNotaCredito : listAnexoNotaCredito) {
			anexoNotaCredito.setNotaCredito(notaCredito);
		}
		
		getNotaCreditoPadraoDAO().store(listAnexoNotaCredito);
	}
	
	
	public AnexoNotaCredito storeAnexoNotaCredito(TypedFlatMap map){
		AnexoNotaCredito anexoNotaCredito = new AnexoNotaCredito();
		anexoNotaCredito.setIdAnexoNotaCredito(map.getLong("idAnexoNotaCredito"));
		
		NotaCredito notaCredito = new NotaCredito();
    	notaCredito.setIdNotaCredito(map.getLong("idNotaCredito"));
    	anexoNotaCredito.setNotaCredito(notaCredito);
    	
    	UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
    	anexoNotaCredito.setUsuario(usuarioLMS);
    	
    	try{
    		anexoNotaCredito.setDcArquivo(Base64Util.decode(map.getString("dcArquivo")));
    	} catch (IOException e) {
			throw new InfrastructureException(e);
		}
    	
    	anexoNotaCredito.setDsAnexo(map.getString("dsAnexo"));    	
    	anexoNotaCredito.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
    	
    	getNotaCreditoPadraoDAO().store(anexoNotaCredito);
    	
    	return anexoNotaCredito;
	}
	
	public AnexoNotaCredito findAnexoNotaCreditoById(Long idAnexoNotaCredito) {
		AnexoNotaCredito anexoNotaCredito = getNotaCreditoPadraoDAO().findAnexoNotaCreditoById(idAnexoNotaCredito);
		if(anexoNotaCredito != null){
			Hibernate.initialize(anexoNotaCredito);
		}
		return anexoNotaCredito;
	}
	
	@SuppressWarnings("rawtypes")
	public void removeByIdsAnexoNotaCredito(List ids) {		
		getNotaCreditoPadraoDAO().removeByIdsAnexoNotaCredito(ids);
	}

	public List<Map<String, Object>> findAnexoNotaCreditoByIdNotaCredito(Long idNotaCredito) {
		return getNotaCreditoPadraoDAO().findAnexoNotaCreditoByIdNotaCredito(idNotaCredito);
    }
	
	public Integer getRowCountAnexoNotaCreditoByIdNotaCredito(Long idDescontoRfc) {
		return getNotaCreditoPadraoDAO().getRowCountAnexoNotaCreditoByIdNotaCredito(idDescontoRfc);
	}
	
	/**
	 * Constrói dados para relatório em PDF da nota de crédito.
	 * 
	 * @param idNotaCredito
	 * @param preview
	 * 
	 * @return Map<String, Object>
	 */
	public Map<String, Object> findDataPDFReport(Long idNotaCredito, Boolean preview){		
		NotaCredito notaCredito = findById(idNotaCredito);
		
		if(preview || !executeWorkflowCalculoNotaCreditoPadraoService.isGeneratePendencia(notaCredito)){
			if(!preview){
				eventoNotaCreditoService.storeEventoNotaCredito(notaCredito, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_EMITIDO);			
			}
			return populatePDFReport(idNotaCredito, preview, notaCredito);
		} 
		
		return new HashMap<String, Object>();
	}

	/**
	 * Popula os dados para o report.
	 * 
	 * @param idNotaCredito
	 * @param preview
	 * @param notaCredito
	 * 
	 * @return Map<String, Object>
	 */
	private Map<String, Object> populatePDFReport(Long idNotaCredito, Boolean preview,
			NotaCredito notaCredito) {
		updateDhEmissao(preview, notaCredito);		
		
		NotaCreditoPadraoReportHelper reportHelper = new NotaCreditoPadraoReportHelper(getNotaCreditoPadraoDAO());
		
		return reportHelper.getSummaryPDF(idNotaCredito, notaCredito.getTpNotaCredito().getValue());
	}
	
	/**
	 * Constrói dados para relatório resumido em tela da nota de crédito.
	 * 
	 * @param idNotaCredito
	 * 
	 * @return <Map<String, Object>
	 */
	public Map<String, Object> findDataScreenReport(Long idNotaCredito, String tpNotaCredito){
		return new NotaCreditoPadraoReportHelper(getNotaCreditoPadraoDAO()).getSummaryScreen(idNotaCredito, tpNotaCredito);
	}

	/**
	 * Atualiza data de emissão, quando não for visualização.
	 * 
	 * @param preview
	 * @param notaCredito
	 */
	private void updateDhEmissao(Boolean preview, NotaCredito notaCredito) {	
		if(preview || notaCredito.getDhEmissao() != null){
			return;
		}
		
		notaCredito.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
		
		this.store(notaCredito);	
		
		getNotaCreditoPadraoDAO().getAdsmHibernateTemplate().flush();
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findGerarNotaCredito(TypedFlatMap criteria) {
		return getNotaCreditoPadraoDAO().findGerarNotaCredito(criteria, FindDefinition.createFindDefinition(criteria));
	}
	
	public Integer getRowCountGerarNotaCredito(TypedFlatMap criteria) {		
		return getNotaCreditoPadraoDAO().getRowCountGerarNotaCredito(criteria);
	}

	public void setExecuteWorkflowCalculoNotaCreditoPadraoService(
			ExecuteWorkflowCalculoNotaCreditoPadraoService executeWorkflowCalculoNotaCreditoPadraoService) {
		this.executeWorkflowCalculoNotaCreditoPadraoService = executeWorkflowCalculoNotaCreditoPadraoService;
	}
	
	public void removeListaNotaCredito(NotaCredito notaCredito) {
		getNotaCreditoPadraoDAO().removeParcelasItens(notaCredito.getNotaCreditoCalculoPadraoItens());
		getNotaCreditoPadraoDAO().removeDocs(notaCredito.getNotaCreditoDoctoItens());
		getNotaCreditoPadraoDAO().removeParcelas(notaCredito.getNotaCreditoParcelas());
	}
	
	public List<NotaCredito> findByIdControleCarga(Long idControleCarga) {
		List<NotaCredito> notasCredito = getNotaCreditoPadraoDAO().findByIdControleCarga(idControleCarga);

		if (notasCredito == null) {
			return new ArrayList<NotaCredito>();
		}

		return notasCredito;
	}

	public void setEventoNotaCreditoService(EventoNotaCreditoService eventoNotaCreditoService) {
		this.eventoNotaCreditoService = eventoNotaCreditoService;
	}

	public List<Map<String, Object>> findParcelasByIdNotaCredito(Long idNotaCredito) {		
		return getNotaCreditoPadraoDAO().findParcelasByIdNotaCredito(idNotaCredito);
				
	}
}