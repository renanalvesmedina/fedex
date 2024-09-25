package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.TrtCliente;
import com.mercurio.lms.vendas.model.dao.TrtClienteDAO;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.service.AcaoService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.ocorrenciaClienteService"
 */
public class TrtClienteService extends CrudService<TrtCliente, Long> {
	
	private ConfiguracoesFacade configuracoesFacade;
	private PessoaService pessoaService;
	private AcaoService acaoService;
	private WorkflowPendenciaService workflowPendenciaService;
	
	/**
	 * Recupera uma instância de <code>OcorrenciaCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public TrtCliente findById(java.lang.Long id) {
		return (TrtCliente)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(TrtCliente bean) {
		super.store(bean);
		return bean;
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setTrtClienteDAO(TrtClienteDAO dao) {
		setDao(dao);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria){
		return getTrtClienteDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
	}
	
	public Integer getRowCount(TypedFlatMap criteria){
		return getTrtClienteDAO().getRowCount(criteria);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private TrtClienteDAO getTrtClienteDAO() {
		return (TrtClienteDAO) getDao();
	}
	
	public Boolean validateRegistroFilhoEmAprovacao(Long idTrtCliente){
		return getTrtClienteDAO().validateRegistroFilhoEmAprovacao(idTrtCliente);
	}
	
	public void executeWorkflow(List<Long> idsTrtCliente, List<String> situacoes) {
		
		for (int i = 0; i < idsTrtCliente.size(); i++) {
			TrtCliente trtCliente = this.findById(idsTrtCliente.get(i));
			trtCliente.setTpSituacaoAprovacao(new DomainValue(situacoes.get(i)));
		
			TrtCliente trtClienteOriginal = null;

			if(ConstantesWorkflow.APROVADO.equals(situacoes.get(i))){
				trtClienteOriginal = trtCliente.getTrtClienteOriginal();
				updateDatasAprovacaoWK(trtCliente, trtClienteOriginal);

				trtCliente.setDtVigenciaInicialSolicitada(null);
				trtCliente.setDtVigenciaFinalSolicitada(null);
			}
			
			if(trtClienteOriginal != null){
				List<TrtCliente> trtsCliente = new ArrayList<TrtCliente>();
				trtsCliente.add(trtCliente);
				trtsCliente.add(trtClienteOriginal);
				storeAll(trtsCliente);
			} else {
				store(trtCliente);
			}
		}
	}

	private void updateDatasAprovacaoWK(TrtCliente trtCliente, TrtCliente trtClienteOriginal) {
		YearMonthDay dataAcao = this.findDataAcao(trtCliente.getPendencia().getIdPendencia());
		
		if (dataAcao.isBefore(trtCliente.getDtVigenciaInicialSolicitada())
				|| dataAcao.isEqual(trtCliente.getDtVigenciaInicialSolicitada())) {
			if(isAtualizarTrtOriginal(trtClienteOriginal)){
				trtClienteOriginal.setDtVigenciaFinal(trtCliente.getDtVigenciaInicialSolicitada().minusDays(1));
			}
			
			trtCliente.setDtVigenciaInicial(trtCliente.getDtVigenciaInicialSolicitada());
			trtCliente.setDtVigenciaFinal(trtCliente.getDtVigenciaFinalSolicitada());
			
		} else {
			if(isAtualizarTrtOriginal(trtClienteOriginal)){
				trtClienteOriginal.setDtVigenciaFinal(JTDateTimeUtils.getDataAtual());
			}
			
			trtCliente.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual().plusDays(1));
			trtCliente.setDtVigenciaFinal(trtCliente.getDtVigenciaFinalSolicitada());
		}
	}

	private Boolean isAtualizarTrtOriginal(TrtCliente trtClienteOriginal) {
		return trtClienteOriginal != null && trtClienteOriginal.getPendencia() != null && ConstantesWorkflow.APROVADO.equals(trtClienteOriginal.getPendencia().getTpSituacaoPendencia().getValue())
				&& JTDateTimeUtils.MAX_YEARMONTHDAY.equals(trtClienteOriginal.getDtVigenciaFinal());
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	private YearMonthDay findDataAcao(Long idTrtCliente){
		YearMonthDay dataAcao = null;
		List<Acao> acoes = acaoService.findByIdPendenciaTpSituacaoAcao(idTrtCliente, ConstantesWorkflow.APROVADO);
		
		if (acoes != null && !acoes.isEmpty()){
			Acao acao = acoes.get(0);
			dataAcao = acao.getDhAcao().toYearMonthDay();
		}
		
		return dataAcao;
	}
	
	/**
	 * 
	 * @param criteria
	 */
	public void validateTrtCliente(TypedFlatMap criteria){
    	List<Long> idClientes = new ArrayList<Long>();
    	idClientes.add(criteria.getLong("cliente.idCliente"));
    	List<TrtCliente> trtClientes = this.findTrtClienteByClientes(idClientes);

    	Long idTrtClienteAlteracao = criteria.getLong("idTrtCliente");
    	YearMonthDay dtVigenciaInicialSolicitada = criteria.getYearMonthDay("dtVigenciaInicialSolicitada");
    	YearMonthDay dtVigenciaFinalSolicitada = criteria.getYearMonthDay("dtVigenciaFinalSolicitada");
    	
    	for (TrtCliente trtCliente : trtClientes){
    		if (this.validateTrtVigenteByIdCliente(trtCliente.getCliente().getIdCliente(), idTrtClienteAlteracao, dtVigenciaInicialSolicitada, dtVigenciaFinalSolicitada)){    					
    			throw new BusinessException("LMS-01222", new String[]{FormatUtils.formatIdentificacao(pessoaService.findById(trtCliente.getCliente().getIdCliente()))});    	
    		}
    		
    		if (this.validateTrtSolicitadaByIdCliente(trtCliente.getCliente().getIdCliente(), idTrtClienteAlteracao, dtVigenciaInicialSolicitada, dtVigenciaFinalSolicitada)){    					
    			throw new BusinessException("LMS-01257", new String[]{FormatUtils.formatIdentificacao(pessoaService.findById(trtCliente.getCliente().getIdCliente()))});    	
    		}
    	}
    }
	
	public void validateTrtIdTabela(TypedFlatMap criteria){
		List<TrtCliente> trtClientes   = this.findTrtClienteByIdTabela(criteria.getLong("idTabelaPreco"));
		Long idTrtClienteAlteracao     = criteria.getLong("idTrtCliente");
    	YearMonthDay dtVigenciaInicial = criteria.getYearMonthDay("dtVigenciaInicial");
    	YearMonthDay dtVigenciaFinal   = criteria.getYearMonthDay("dtVigenciaFinal") == null ? JTDateTimeUtils.MAX_YEARMONTHDAY : criteria.getYearMonthDay("dtVigenciaFinal");
    	
    	if(dtVigenciaInicial == null || dtVigenciaInicial.isBefore(JTDateTimeUtils.getDataAtual()) || dtVigenciaInicial.isEqual(JTDateTimeUtils.getDataAtual())){
    		throw new BusinessException("LMS-30040");
    	}
    	
    	if (dtVigenciaFinal != null && dtVigenciaFinal.isBefore(dtVigenciaInicial)){
       		throw new BusinessException("LMS-00008");    		
       	}
    	
    	for (TrtCliente trtCliente : trtClientes){
    		if (!validateTrtVigenteByIdTabela(trtCliente.getIdTabelaPreco(), idTrtClienteAlteracao, dtVigenciaInicial, dtVigenciaFinal)){    					
    			throw new BusinessException("LMS-01115");   
    		}
    	}
	}
	

	//LMS-5434 - HF
	public List<TrtCliente> findTrtVigenteByIdCliente(Long id) {
		return findTrtVigenteByIdCliente(id, null);
	}
	
	public List<TrtCliente> findTrtVigenteByIdCliente(Long id, YearMonthDay dataInicial){
		return getTrtClienteDAO().findTrtVigenteByIdCliente(id, dataInicial);
	}
	
	public Boolean validateTrtVigenteByIdCliente(Long id, Long idTrtClienteAlteracao, YearMonthDay dataInicial, YearMonthDay dataFinal){
		return getTrtClienteDAO().validateTrtVigenteByIdCliente(id, idTrtClienteAlteracao, dataInicial, dataFinal);
	}
	
	public Boolean validateTrtVigenteByIdTabela(Long id, Long idTrtClienteAlteracao, YearMonthDay dataInicial, YearMonthDay dataFinal){
		return getTrtClienteDAO().validateTrtVigenteByIdTabela(id, idTrtClienteAlteracao, dataInicial, dataFinal);
	}

	public Boolean validateTrtSolicitadaByIdCliente(Long id, Long idTrtClienteAlteracao, YearMonthDay dataInicial, YearMonthDay dataFinal){
		return getTrtClienteDAO().validateTrtSolicitadaByIdCliente(id, idTrtClienteAlteracao, dataInicial, dataFinal);
	}
	
	public List<TrtCliente> findTrtClienteByClientes(List<Long> idClientes){
		return getTrtClienteDAO().findTrtClienteByClientes(idClientes);
	}

	public List<TrtCliente> findTrtClienteByIdTabela(Long idTabela){
		return getTrtClienteDAO().findTrtClienteByIdTabela(idTabela);
	}
	
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}
}