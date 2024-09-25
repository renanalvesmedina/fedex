package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.dao.TabelaDivisaoClienteDAO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.HistoricoWorkflowService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.tabelaDivisaoClienteService"
 */
public class TabelaDivisaoClienteService extends CrudService<TabelaDivisaoCliente, Long> {
	
	private static final String FORMAT = "#,##0.00";
	private static final String CAMPO_NULL = "vazio";
	
	private TabelaPrecoService tabelaPrecoService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	private ParametroClienteService parametroclienteService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private HistoricoWorkflowService historicoWorkflowService;

	/**
	 * Recupera uma instância de <code>TabelaDivisaoCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public TabelaDivisaoCliente findById(Long id) {
		TabelaDivisaoCliente tabelaDivisaoCliente = (TabelaDivisaoCliente)super.findById(id);
		normalizaCamposWK(tabelaDivisaoCliente);
		
		return tabelaDivisaoCliente;
	}
	
	public int getRowCountByTabelaPreco(Long idTabelaPreco, List<String> cdParcelasPrecoList){
		return getTabelaDivisaoClienteDAO().getRowCountByTabelaPreco(idTabelaPreco, cdParcelasPrecoList);
	}
	
	public int getRowCountByDivisaoCliente(Long idDivisaoCliente, List<String> cdParcelasPrecoList){
		return getTabelaDivisaoClienteDAO().getRowCountByDivisaoCliente(idDivisaoCliente, cdParcelasPrecoList);
	}

	private void normalizaCamposWK(TabelaDivisaoCliente tabelaDivisaoCliente) {
		Long idTabelaDivisaoCliente = tabelaDivisaoCliente.getIdTabelaDivisaoCliente();
		
		Map <String, Boolean> pendenciasCamposWK = validateWorkflowPendenciaAprovacaoTabDivCliente(idTabelaDivisaoCliente);
		tabelaDivisaoCliente.setTemPendenciaTpPesoCalculo(pendenciasCamposWK.get(CampoHistoricoWorkflow.PBCL.name()));
		tabelaDivisaoCliente.setTemPendenciaNrFatorCubagem(pendenciasCamposWK.get(CampoHistoricoWorkflow.FTCB.name()));
		tabelaDivisaoCliente.setTemPendenciaNrFatorDensidade(pendenciasCamposWK.get(CampoHistoricoWorkflow.FTDN.name()));
		tabelaDivisaoCliente.setTemPendenciaBlObrigaDimensoes(pendenciasCamposWK.get(CampoHistoricoWorkflow.OBDM.name()));
		
		if (tabelaDivisaoCliente.getTpPesoCalculoSolicitado() == null) {
			tabelaDivisaoCliente.setTpPesoCalculoSolicitado(tabelaDivisaoCliente.getTpPesoCalculo());
		}
		
		if (tabelaDivisaoCliente.getBlObrigaDimensoesSolicitado() == null) {
			tabelaDivisaoCliente.setBlObrigaDimensoesSolicitado(tabelaDivisaoCliente.getBlObrigaDimensoes());
		}
	}

	/**
	 * Apaga uma entidade através do Id.
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	private void validateHistoricoWorkflow(List<Long> ids) {
		for (Long id : ids) {
			if (historicoWorkflowService.validateHistoricoWorkflow(id, TabelaHistoricoWorkflow.TABELA_DIVISAO_CLIENTE)) {
				TabelaDivisaoCliente tabelaDivisaoCliente = this.findById(id);
				throw new BusinessException("LMS-01239", new String[] { tabelaDivisaoCliente.getTabelaPreco().getTabelaPrecoString(),
						tabelaDivisaoCliente.getDivisaoCliente().getDsDivisaoCliente() });
			}
		}
	}
	
	@Override
	protected void beforeRemoveById(Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		validateHistoricoWorkflow(ids);
		super.beforeRemoveById(id);
	}
	
	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		validateHistoricoWorkflow(ids);
		super.beforeRemoveByIds(ids);
	}
	
	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	@Override
	protected TabelaDivisaoCliente beforeStore(TabelaDivisaoCliente bean) {
		// regras devem ser executadas somente se integração NÃO estiver rodando
		// - CQPRO00011718
		if (SessionUtils.isIntegrationRunning() == false) {
			if (bean.getTabelaPreco() != null) {
				tabelaPrecoService.validateTrocaTabelaPreco(bean);
			}
		}
		return super.beforeStore(bean);
	}


	@Override
	protected TabelaDivisaoCliente beforeUpdate(TabelaDivisaoCliente bean) {
		TabelaPreco tabelaPreco = (TabelaPreco)tabelaPrecoService.findByIdTabelaPreco(bean.getTabelaPreco().getIdTabelaPreco());
		
		if ("P".equals(tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco())) {
			parametroclienteService.updateVigenciaFinalByIdTabelaDivisaoCliente(JTDateTimeUtils.getDataAtual(), bean.getIdTabelaDivisaoCliente());
			List<Long> idsTabelaDivisaoCliente = new ArrayList<Long>(1);
			idsTabelaDivisaoCliente.add(bean.getIdTabelaDivisaoCliente());
			servicoAdicionalClienteService.removeByTabelasDivisaoCliente(idsTabelaDivisaoCliente);
		}
		return super.beforeUpdate(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(TabelaDivisaoCliente bean) {
		super.store(bean);
		/* 
    	 * Caso o campo dsMotivoSolicitacao estiver preenchido, gerar workflow
    	 *  
    	 */
		if (!StringUtils.isEmpty(bean.getDsMotivoSolicitacao())) {
			gerarPendenciasWKNecessarias(bean);
			bean.setDsMotivoSolicitacao("");
		}
		
		refreshBean(bean);
		normalizaCamposWK(bean);
		
		return bean;
	}

	/** 
	 * Método criado para gerar pendências WK para campos que já não tem pendências em aberto.
	 * 
	 * @param bean = TabelaDivisaoCliente
	 */
	private void gerarPendenciasWKNecessarias(TabelaDivisaoCliente bean) {
		Map <String, Boolean> pendenciasCamposWK = validateWorkflowPendenciaAprovacaoTabDivCliente(bean.getIdTabelaDivisaoCliente());
		refreshBean(bean);
		
		if (isGerarWKTpPesoCalculo(bean, pendenciasCamposWK)) {
			String dsVlAntigo = bean.getTpPesoCalculo().getDescriptionAsString(); 
			String dsVlNovo = bean.getTpPesoCalculoSolicitado().getDescriptionAsString();
			gerarPendenciaWorkflow(bean, ConstantesWorkflow.NR136_APROVACAO_TP_PESO_CALCULO, dsVlAntigo, dsVlNovo, CampoHistoricoWorkflow.PBCL);
		}
		if (isGerarWKBlObrigaDimensoes(bean, pendenciasCamposWK)) {
			String dsVlAntigo = bean.getBlObrigaDimensoes() ? configuracoesFacade.getMensagem("sim") : configuracoesFacade.getMensagem("nao");
			String dsVlNovo = bean.getBlObrigaDimensoesSolicitado() ? configuracoesFacade.getMensagem("sim") : configuracoesFacade.getMensagem("nao");
			gerarPendenciaWorkflow(bean, ConstantesWorkflow.NR144_APROVACAO_OBRIGA_DIMENSOES, dsVlAntigo, dsVlNovo, CampoHistoricoWorkflow.OBDM);
		}
		if (isGerarWKNrFatorDensidade(bean, pendenciasCamposWK)) {
			String dsVlAntigo = null;
			String dsVlNovo = null;
			dsVlAntigo = formatValor(bean.getNrFatorDensidade());
			dsVlNovo = formatValor(bean.getNrFatorDensidadeSolicitado());
			gerarPendenciaWorkflow(bean, ConstantesWorkflow.NR145_APROVACAO_NR_FATOR_DENSIDADE, dsVlAntigo, dsVlNovo, CampoHistoricoWorkflow.FTDN);
		}
		if (isGerarWKNrFatorCubagem(bean, pendenciasCamposWK)) {
			String dsVlAntigo = null;
			String dsVlNovo = null;
			dsVlAntigo = formatValor(bean.getNrFatorCubagem());
			dsVlNovo = formatValor(bean.getNrFatorCubagemSolicitado());
			gerarPendenciaWorkflow(bean, ConstantesWorkflow.NR146_APROVACAO_NR_FATOR_CUBAGEM, dsVlAntigo, dsVlNovo, CampoHistoricoWorkflow.FTCB);
		}
	}

	private String formatValor(BigDecimal valor) {
		String valorRetornado;
		if (valor != null) {
			valorRetornado = FormatUtils.formatDecimal(FORMAT, valor, true);
		} else {
			valorRetornado = CAMPO_NULL;
		}
		return valorRetornado;
	}

	private boolean isGerarWKNrFatorCubagem(TabelaDivisaoCliente bean, Map<String, Boolean> pendenciasCamposWK) {
		//null é uma comparação válida
		Boolean isCamposDiferentes = bean.getNrFatorCubagem() == null ? bean.getNrFatorCubagemSolicitado() != null : !bean.getNrFatorCubagem().equals(bean.getNrFatorCubagemSolicitado());
		return isCamposDiferentes && BooleanUtils.isFalse(pendenciasCamposWK.get(CampoHistoricoWorkflow.FTCB.name()));
	}

	private boolean isGerarWKNrFatorDensidade(TabelaDivisaoCliente bean, Map<String, Boolean> pendenciasCamposWK) {
		//null é uma comparação válida
		Boolean isCamposDiferentes = bean.getNrFatorDensidade() == null ? bean.getNrFatorDensidadeSolicitado() != null : !bean.getNrFatorDensidade().equals(bean.getNrFatorDensidadeSolicitado()); 
		return  isCamposDiferentes && BooleanUtils.isFalse(pendenciasCamposWK.get(CampoHistoricoWorkflow.FTDN.name()));
	}

	private boolean isGerarWKBlObrigaDimensoes(TabelaDivisaoCliente bean, Map<String, Boolean> pendenciasCamposWK) {
		return !bean.getBlObrigaDimensoes().equals(bean.getBlObrigaDimensoesSolicitado()) 
				&& BooleanUtils.isFalse(pendenciasCamposWK.get(CampoHistoricoWorkflow.OBDM.name()));
	}

	private boolean isGerarWKTpPesoCalculo(TabelaDivisaoCliente bean, Map<String, Boolean> pendenciasCamposWK) {
		return !bean.getTpPesoCalculo().equals(bean.getTpPesoCalculoSolicitado()) 
				&& BooleanUtils.isFalse(pendenciasCamposWK.get(CampoHistoricoWorkflow.PBCL.name()));
	}

	private void refreshBean(TabelaDivisaoCliente bean) {
		getTabelaDivisaoClienteDAO().getAdsmHibernateTemplate().flush();
		getTabelaDivisaoClienteDAO().getAdsmHibernateTemplate().refresh(bean);
	}
	
	private void gerarPendenciaWorkflow(TabelaDivisaoCliente bean, Short nrTipoEvento, String dsVlAntigo,
			String dsVlNovo, CampoHistoricoWorkflow campo) {
		String chave = this.getChaveDsProcesso(campo);
		String dsProcesso = this.generateDsProcessoWorkflow(bean, chave, dsVlAntigo, dsVlNovo);

		PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
		pendenciaHistoricoDTO.setIdFilial(SessionUtils.getFilialSessao().getIdFilial());
		pendenciaHistoricoDTO.setNrTipoEvento(nrTipoEvento);
		pendenciaHistoricoDTO.setIdProcesso(bean.getIdTabelaDivisaoCliente());
		pendenciaHistoricoDTO.setDsProcesso(dsProcesso);
		pendenciaHistoricoDTO.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());
		pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.TABELA_DIVISAO_CLIENTE);
		pendenciaHistoricoDTO.setCampoHistoricoWorkflow(campo);
		pendenciaHistoricoDTO.setDsVlAntigo(dsVlAntigo);
		pendenciaHistoricoDTO.setDsVlNovo(dsVlNovo);
		pendenciaHistoricoDTO.setDsObservacao(bean.getDsMotivoSolicitacao());

		workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
	}

	private String generateDsProcessoWorkflow(TabelaDivisaoCliente bean, String chave, String dsVlAntigo, String dsVlNovo) {
		String nrIdentificacao = this.getPessoaNrIdentificacao(bean.getDivisaoCliente().getCliente());
		String nmPessoa = this.getNmPessoa(bean.getDivisaoCliente().getCliente());
		String dsDivisaoCliente = bean.getDivisaoCliente().getDsDivisaoCliente();
		String dsTabelaPreco = bean.getTabelaPreco().getTabelaPrecoString();
		String dsMotivoSolicitacao = bean.getDsMotivoSolicitacao();
		
		return configuracoesFacade.getMensagem(chave, new Object[] { 
				dsVlAntigo,
				dsVlNovo, 
				nrIdentificacao, 
				nmPessoa,
				dsDivisaoCliente,
				dsTabelaPreco,
				dsMotivoSolicitacao});
	}
	
	private String getChaveDsProcesso(CampoHistoricoWorkflow campo) {
		String chave = null;
		if (campo.equals(CampoHistoricoWorkflow.PBCL)) {
			chave = "LMS-01237"; 
		}
		if (campo.equals(CampoHistoricoWorkflow.OBDM)) {
			chave = "LMS-01246"; 
		}
		if (campo.equals(CampoHistoricoWorkflow.FTDN)) {
			chave = "LMS-01247"; 
		}
		if (campo.equals(CampoHistoricoWorkflow.FTCB)) {
			chave = "LMS-01248"; 
		}
		return chave;
	}
	
	public Map <String, Boolean> validateWorkflowPendenciaAprovacaoTabDivCliente(Long idTabelaDivisaoCliente) {
		List<String> camposTabelaDivisaoCliente = new ArrayList<String>();
		camposTabelaDivisaoCliente.add(CampoHistoricoWorkflow.PBCL.name());
		camposTabelaDivisaoCliente.add(CampoHistoricoWorkflow.OBDM.name());
		camposTabelaDivisaoCliente.add(CampoHistoricoWorkflow.FTDN.name());
		camposTabelaDivisaoCliente.add(CampoHistoricoWorkflow.FTCB.name());

		return historicoWorkflowService.findPendenciaAprovacaoByTabelaCampos(idTabelaDivisaoCliente,
				TabelaHistoricoWorkflow.TABELA_DIVISAO_CLIENTE, camposTabelaDivisaoCliente);
	}

	private String getNmPessoa(Cliente cliente) {
		String nmPessoa = null;
		if (cliente != null && cliente.getPessoa() != null) {
			nmPessoa = cliente.getPessoa().getNmPessoa();
		}
		return nmPessoa;
	}

	private String getPessoaNrIdentificacao(Cliente cliente) {
		String nrIdent = null;
		if (cliente != null && cliente.getPessoa() != null) {
			nrIdent = FormatUtils.formatIdentificacao(cliente.getPessoa());
		}
		return nrIdent;
	}
	
	public List<TabelaDivisaoCliente> findTabelaDivisaoClienteComboByDivisaoWithServico(Long idDivisao) {
		return getTabelaDivisaoClienteDAO().findTabelaDivisaoClienteComboByDivisaoWithServico(idDivisao);	
	}

	public List<TabelaDivisaoCliente> findHistoricoReajusteClienteByIdDivisaoCliente(Long idDivisaoCliente) {
		return getTabelaDivisaoClienteDAO().findHistoricoReajusteClienteByIdDivisaoCliente(idDivisaoCliente);
	}

	public List<TabelaDivisaoCliente> findTabelaDivisaoClienteByIdDivisaoClienteComboDetail(Long idDivisaoCliente) {
		return getTabelaDivisaoClienteDAO().findTabelaDivisaoClienteByIdDivisaoClienteComboDetail(idDivisaoCliente);
	}
	
	public Integer getRowCountByDivisaoClienteServicoBlObrigaDimensoes(Long idDivisaoCliente, Long idServico, Boolean blObrigaDimensoes) {
		return getTabelaDivisaoClienteDAO().getRowCountByDivisaoClienteServicoBlObrigaDimensoes(idDivisaoCliente, idServico, blObrigaDimensoes);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idDivisaoCliente
	 * @param tpTipoTabelaPreco
	 * @return <List TabelaDivisaoCliente>
	 */
	public List<TabelaDivisaoCliente> findTabelaDivisaoCliente(Long idDivisaoCliente, String tpTipoTabelaPreco) {
		return getTabelaDivisaoClienteDAO().findTabelaDivisaoCliente(idDivisaoCliente, tpTipoTabelaPreco);
	}

	public List<TabelaDivisaoCliente> findByDivisaoCliente(Long idDivisaoCliente){
		return getTabelaDivisaoClienteDAO().findByDivisaoCliente(idDivisaoCliente);
	}
	
	/**
	 * Solicitação CQPRO00005948 da Integração.
	 * @param tpSubtipoTabelaPreco
	 * @param nrVersao
	 * @param tpTipoTabelaPreco
	 * @return
	 */
	public List<TabelaDivisaoCliente> findTabelaDivisaoCliente(String tpSubtipoTabelaPreco, Integer nrVersao, String tpTipoTabelaPreco){
		return getTabelaDivisaoClienteDAO().findTabelaDivisaoCliente(tpSubtipoTabelaPreco, nrVersao, tpTipoTabelaPreco);
	}

	/**
	 * Método usado pela integração no intuito de buscar a TabelaDivisaoCliente sem o id da divisão do cliente.
	 * 
	 * @param idDivisaoCliente
	 * @param idServico
	 * @return TabelaDivisaoCliente
	 */
	public TabelaDivisaoCliente findTabelaDivisaoCliente(Long idDivisaoCliente, Long idServico) {
		return getTabelaDivisaoClienteDAO().findTabelaDivisaoCliente(idDivisaoCliente, idServico);
	}

	public List<TabelaDivisaoCliente> findTabelaDivisaoClienteByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		return getTabelaDivisaoClienteDAO().findByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idCliente, idDivisaoCliente, idTabelaDivisaoCliente);
	}

	/**
	 * Busca Tabelas da Divisao de Cliente.
	 * @param idTabelaPreco
	 * @param blAtualizacaoAutomatica
	 * @return
	 */
	public List<TabelaDivisaoCliente> findByIdTabelaPreco(Long idTabelaPreco, Boolean blAtualizacaoAutomatica) {
		return getTabelaDivisaoClienteDAO().findByIdTabelaPreco(idTabelaPreco, blAtualizacaoAutomatica);
	}

	/**
	 * Obtem a tabela divisao cliente através do cliente e tpModal
	 * @param idCliente
	 * @param tpServico
	 * @return
	 */
	public List<TabelaDivisaoCliente> findByServicoCliente(Long idCliente, String tpModal){
		return getTabelaDivisaoClienteDAO().findByServicoCliente(idCliente,tpModal);
	}
	
	/**
	 * Obtem a tabela divisao cliente, identificando se a mesma possue 
	 * tabelaFOB
	 * 
	 * @param idDivisaoCliente
	 * @param tpModal
	 * @return
	 */
	public List<TabelaDivisaoCliente> findTabelaPrecoFob(Long idDivisaoCliente, String tpModal){
		return getTabelaDivisaoClienteDAO().findTabelaPrecoFob(idDivisaoCliente,tpModal);
	}

	public Boolean findObrigatoriedadeDadosDimensaoECubagemByNrVolume(Long idVolumeNotaFiscal) {
		return getTabelaDivisaoClienteDAO().findObrigatoriedadeDadosDimensaoECubagemByNrVolume(idVolumeNotaFiscal);
	}
	
	public Boolean validateDimensaoOpcionalByFatorDensidade(Long idVolumeNotaFiscal){
		return getTabelaDivisaoClienteDAO().validateDimensaoOpcionalByFatorDensidade(idVolumeNotaFiscal);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setTabelaDivisaoClienteDAO(TabelaDivisaoClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private TabelaDivisaoClienteDAO getTabelaDivisaoClienteDAO() {
		return (TabelaDivisaoClienteDAO) getDao();
	}
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	public void setParametroclienteService(ParametroClienteService parametroclienteService) {
		this.parametroclienteService = parametroclienteService;
	}
	public void setServicoAdicionalClienteService(ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}
	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public TabelaPrecoService getTabelaPrecoService() {
		return tabelaPrecoService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public HistoricoWorkflowService getHistoricoWorkflowService() {
		return historicoWorkflowService;
	}

	public void setHistoricoWorkflowService(HistoricoWorkflowService historicoWorkflowService) {
		this.historicoWorkflowService = historicoWorkflowService;
	}
}