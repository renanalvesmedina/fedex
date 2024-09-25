package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.param.DivisaoClienteParam;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.PrazoVencimento;
import com.mercurio.lms.vendas.model.dao.DivisaoClienteDAO;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.HistoricoWorkflowService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.divisaoClienteService"
 */
public class DivisaoClienteService extends CrudService<DivisaoCliente, Long> {
	private ConfiguracoesFacade configuracoesFacade;
	private WorkflowPendenciaService workflowPendenciaService;
	private HistoricoWorkflowService historicoWorkflowService;
	
	/**
	 * Recupera uma instância de <code>DivisaoCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public DivisaoCliente findById(java.lang.Long id) {
		DivisaoCliente divisaoCliente = (DivisaoCliente)super.findById(id);
		divisaoCliente.setTemPendenciaSituacao(historicoWorkflowService.validateWorkflowPendenciaAprovacao(
				divisaoCliente.getIdDivisaoCliente(), TabelaHistoricoWorkflow.DIVISAO_CLIENTE,
				CampoHistoricoWorkflow.STDV));
		return divisaoCliente;
	}
	
	public void beforeRemoveById(Long id) {
		validateDivisaoClienteAtiva((Long) id);
		
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		validateHistoricoWorkflow(ids);
		
		super.beforeRemoveById(id);
	}
	
	private void validateHistoricoWorkflow(List<Long> ids){
		for (Long id : ids) {
			if(historicoWorkflowService.validateHistoricoWorkflow(id, TabelaHistoricoWorkflow.DIVISAO_CLIENTE)){
				DivisaoCliente divisaoCliente = this.findById(id);
				throw new BusinessException("LMS-01238", new String[]{divisaoCliente.getDsDivisaoCliente()});
			}
		}
	}
	
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	public void beforeRemoveByIds(List ids) {
		List divisoesClientes = getDivisaoClienteDAO().findByTpSituacao(new DomainValue(ConstantesVendas.SITUACAO_ATIVO), ids);
		if(divisoesClientes.size() == 0) {
			throw new BusinessException("LMS-01143");
		}
		
		validateHistoricoWorkflow(ids);
		super.beforeRemoveByIds(ids);
	}
	
	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}
	
	public DivisaoCliente beforeUpdate(DivisaoCliente bean) {
		Long id = ((DivisaoCliente) bean).getIdDivisaoCliente();
		DomainValue tpSituacaoSolicitada = ((DivisaoCliente) bean).getTpSituacaoSolicitada();
		if (tpSituacaoSolicitada != null) {
			String tpSituacaoSolicitadaValue = tpSituacaoSolicitada.getValue();

			if (ConstantesVendas.SITUACAO_INATIVO.equals(tpSituacaoSolicitadaValue)) {
				this.validateDivisaoClienteAtiva(id);
			}
		}

		return super.beforeUpdate(bean);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(DivisaoCliente bean) {
		super.store(bean);
    	
    	/* 
    	 * Caso o campo dsMotivoSolicitacao estiver preenchido, gerar workflow
    	 *  
    	 */
		if (!StringUtils.isEmpty(bean.getDsMotivoSolicitacao())) {
			gerarPendenciaWorkflow(bean);
			bean.setDsMotivoSolicitacao("");
		}
		
		bean.setTemPendenciaSituacao(historicoWorkflowService.validateWorkflowPendenciaAprovacao(
				bean.getIdDivisaoCliente(), TabelaHistoricoWorkflow.DIVISAO_CLIENTE,
				CampoHistoricoWorkflow.STDV));
		return bean;
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idCliente
	 * @param dsDivisaoCliente
	 * @return <DivisaoCliente>
	 */
	public DivisaoCliente findDivisaoCliente(Long idCliente, String dsDivisaoCliente) {
		return getDivisaoClienteDAO().findDivisaoCliente(idCliente, dsDivisaoCliente);
	}

	/**
	  * Busca todas as divisões relacionadas ao cliente informado.
	  * 
	  * @param idCliente 
	  * @return
	  */
	public List findDivisoesByIdCliente(Long idCliente) {
		return getDivisaoClienteDAO().findByIdCliente(idCliente);
	}

	public List<DivisaoCliente> findDivisaoClienteByIdServico(Long idCliente, Long idServico) {
		return getDivisaoClienteDAO().findDivisaoClienteByIdServico(idCliente, idServico);
	}
	
	public DivisaoCliente findDivisaoClienteByClienteAndSituacao(Long idCliente, String divisaoCliente, String tpSituacao) {
		return getDivisaoClienteDAO().findDivisaoClienteByClienteAndSituacao(idCliente, divisaoCliente, tpSituacao);
	}
	public List findDivisaoClienteByClienteAndSituacao(Long idCliente, String tpSituacao) {
		return getDivisaoClienteDAO().findDivisaoClienteByClienteAndSituacao(idCliente, tpSituacao);
	}
	
	/**
	  * Busca as divisoes do cliente que sejam do servico fornecido e que
	  * estejam na mesma situacao fornecida, normalmente o tpSituacao devera
	  * receber o valor "<code>A</code>".
	  * 
	  * @param idCliente
	  * @param idServico
	  * @param tpSituacao
	  * @return
	  */
	 public List findDivisaoClienteByIdServico(Long idCliente, Long idServico, String tpSituacao) {
		 return getDivisaoClienteDAO().findDivisaoClienteByIdServico(idCliente, idServico, tpSituacao);
	 }

	public List<DivisaoCliente> findDivisaoCliente(Long idCliente, DomainValue tpModal, DomainValue tpAbrangencia) {
		return getDivisaoClienteDAO().findDivisaoCliente(idCliente, tpModal, tpAbrangencia);
	}

	/**
	 * Informa se o cliente tem divisão cliente com, dia de faturamento, dia de vencimento e tabela de divisao de cliente
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/04/2006
	 * 
	 * @param Long idCliente
	 * @return Boolean
	 * */
	public Boolean verificaExistenciaDivisaoCliente(Long idCliente){
		//Busca o número de divisão que tem
		Long nrDivisao = getDivisaoClienteDAO().findCountByIdCliente(idCliente);

		if (nrDivisao <= 0) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	/**
	 * Verificar se existem outras divisões ativas para o mesmo cliente, antes da exclusão e da alteração.
	 * @param idDivisaoCliente
	 */
	public void validateDivisaoClienteAtiva(Long idDivisaoCliente){
		DivisaoCliente divisaoCliente = this.findById(idDivisaoCliente);

		/** Se situação estava Ativa */
		if(ConstantesVendas.SITUACAO_ATIVO.equals(divisaoCliente.getTpSituacao().getValue())) {
			/** Verifica se existe para esse cliente outras divisões ativas e diferentes da passada */
			Integer rowCount = getDivisaoClienteDAO().getRowCountNotIn(
				divisaoCliente.getIdDivisaoCliente(), 
				divisaoCliente.getCliente().getIdCliente(), 
				ConstantesVendas.SITUACAO_ATIVO);
			/** Caso não exista lança a exceção: "Deve existir pelo menos uma divisão ativa para um cliente especial." */
			if(CompareUtils.lt(rowCount, IntegerUtils.ONE)) {
				throw new BusinessException("LMS-01143");
			}
		}
		getDivisaoClienteDAO().getSessionFactory().getCurrentSession().evict(divisaoCliente);
	}

	public List findByIdCliente(Long idCliente, String tpSituacao) {
		return getDivisaoClienteDAO().findByIdCliente(idCliente, tpSituacao);
	}

	public List findByIdCliente(Long idCliente) {
		return findByIdCliente(idCliente, ConstantesVendas.SITUACAO_ATIVO);
	}

	public DivisaoCliente findPrimeiraDivisaoCadastradaByIdCliente(Long idCliente) {
		return getDivisaoClienteDAO().findPrimeiraDivisaoCadastradaByIdCliente(idCliente);
	}

	public List findDivisaoClienteByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		return getDivisaoClienteDAO().findByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idCliente, idDivisaoCliente, idTabelaDivisaoCliente);
	}

	public DivisaoCliente findByIdClienteCdDivisao(Long idCliente, Long cdDivisaoCliente){
		return getDivisaoClienteDAO().findByIdClienteCdDivisao(idCliente, cdDivisaoCliente);
	}

	public Long findNewCdDivisaoCliente(Long idCliente){
		return getDivisaoClienteDAO().findNewCdDivisaoCliente(idCliente);
	}

	/**
	 * Retorna a lista de divisões ativas ou que está dentro do devedor informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/12/2006
	 * 
	 * @param Long idCliente
	 * @param Long idDevedorDocServFat
	 * @return List
	 */
	public List findByIdClienteIdDevedorDocServFat(Long idCliente, Long idDevedorDocServFat) {
		return getDivisaoClienteDAO().findByIdClienteIdDevedorDocServFat(idCliente, idDevedorDocServFat);
	}
	
	/**
	 * Retorna a lista de divisões ativas ou que está dentro da fatura informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/12/2006
	 * 
	 * @param Long idCliente
	 * @param Long idFatura
	 * @return List
	 */
	public List findByIdClienteIdFatura(Long idCliente, Long idFatura) {
		return getDivisaoClienteDAO().findByIdClienteIdFatura(idCliente, idFatura);
	}
	
	/**
	 * Retorna a lista de divisões do cliente informado na situaçãi informada. Se o cliente tem 
	 * um cliente matriz, retorna a lista de divisões do cliente matriz.
	 * 
	 * @author Mickaël Jalbert
	 * @since 12/12/2006
	 * 
	 * @param DivisaoClienteParam param
	 * @return List
	 */
	public List findByIdClienteMatriz(DivisaoClienteParam param) {
		return getDivisaoClienteDAO().findByIdClienteMatriz(param);
	}

	/**
	  * Busca as divisoes do cliente que estejam na mesma situacao fornecida, 
	  * normalmente o tpSituacao devera receber o valor "<code>A</code>".
	  * 
	  * @param idCliente
	  * @param tpSituacao
	  * @return
	  */
	 public List findLookupDivisoesCliente(Long idCliente, String tpSituacao) {
		 return getDivisaoClienteDAO().findLookupDivisoesCliente(idCliente, tpSituacao);
	 }

	/**
	 * Retorna a lista de divisões do cliente informado na situaçãi informada. Se o cliente tem 
	 * um cliente matriz, retorna a lista de divisões do cliente matriz.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/12/2006
	 * 
	 * @param Long idCliente
	 * @param String tpSituacao
	 * @param Long idDivisaoCliente
	 * @return List
	 */
	public List findMapByIdClienteMatriz(DivisaoClienteParam param) {
		return getDivisaoClienteDAO().findMapByIdClienteMatriz(param);
	}

	/**
	 * Retorna a divisao cliente com o menor cdDivisaoCliente
	 * 
	 * @param idCliente
	 * @param tpSituacao
	 * @return
	 */
	public DivisaoCliente findMenorCdDivisaoCliente(Long idCliente, String tpSituacao) {
		return getDivisaoClienteDAO().findMenorCdDivisaoCliente(idCliente, tpSituacao);
	}
	
	public PrazoVencimento findPrazoVencimentoByIdDivisao(Long idDivisaoCliente) {
		return getDivisaoClienteDAO().findPrazoVencimentoByIdDivisao(idDivisaoCliente);
	}
	
	
	public void setDivisaoClienteDAO(DivisaoClienteDAO dao) {
		setDao( dao );
	}
	private DivisaoClienteDAO getDivisaoClienteDAO() {
		return (DivisaoClienteDAO) getDao();
	}

	private void gerarPendenciaWorkflow(DivisaoCliente bean) {
		if (bean.getTpSituacao() != null && bean.getTpSituacaoSolicitada() != null) {
			String tpSituacaoAtual = bean.getTpSituacao().getValue();
			String tpSituacaoSolicitada = bean.getTpSituacaoSolicitada().getValue();
			if (!tpSituacaoAtual.equals(tpSituacaoSolicitada)) {
				getDivisaoClienteDAO().getAdsmHibernateTemplate().flush();
				getDivisaoClienteDAO().getAdsmHibernateTemplate().refresh(bean);
				Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
				Short nrTipoEvento = ConstantesWorkflow.NR135_APROVACAO_SITUACAO_DIVISAO_CLIENTE;
				Long idProcesso = bean.getIdDivisaoCliente();
				DateTime dhLiberacao = JTDateTimeUtils.getDataHoraAtual();
				String dsProcesso = this.generateDsProcessoWorkflow(bean);
				String dsVlAntigo = null;
				String dsVlNovo = null;
				String dsObservacao = bean.getDsMotivoSolicitacao();
				
				if (bean.getTpSituacao() != null) {
					dsVlAntigo = bean.getTpSituacao().getDescriptionAsString(); 
				}
				if (bean.getTpSituacaoSolicitada() != null) {
					dsVlNovo = bean.getTpSituacaoSolicitada().getDescriptionAsString(); 
				}
				PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
				pendenciaHistoricoDTO.setIdFilial(idFilial);
				pendenciaHistoricoDTO.setNrTipoEvento(nrTipoEvento);
				pendenciaHistoricoDTO.setIdProcesso(idProcesso);
				pendenciaHistoricoDTO.setDsProcesso(dsProcesso);
				pendenciaHistoricoDTO.setDhLiberacao(dhLiberacao);
				pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.DIVISAO_CLIENTE);
				pendenciaHistoricoDTO.setCampoHistoricoWorkflow(CampoHistoricoWorkflow.STDV);
				pendenciaHistoricoDTO.setDsVlAntigo(dsVlAntigo);
				pendenciaHistoricoDTO.setDsVlNovo(dsVlNovo);
				pendenciaHistoricoDTO.setDsObservacao(dsObservacao);
				
				workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
			}
		}
	}

	private String generateDsProcessoWorkflow(DivisaoCliente bean) {
		/*
		 * {0} = DIVISAO_CLINTE.DS_DIVISAO 
		 * {1} = conteúdo do domínio DM_STATUS para DIVISAO_CLIENTE.TP_SITUACAO_SOLICITADA 
		 * {2} = PESSOA.NR_IDENTIFICACAO para PESSOA.ID_ PESSOA = DIVISAO_CLIENTE.ID_CLIENTE 
		 * {3} = PESSOA.NM_PESSOA para PESSOA.ID_PESSOA = DIVISAO_CLIENTE.ID_CLIENTE 
		 * {4} = conteúdo do campo motivo informado na popup Motivo da alteração da situação da divisão>
		 */

		String dsDivisao = bean.getDsDivisaoCliente();
		String dsSituacaoSolicitada = null;
		if (bean.getTpSituacaoSolicitada() != null) {
			dsSituacaoSolicitada = bean.getTpSituacaoSolicitada().getDescriptionAsString(); 
		}
		String nrIdentificacao = this.getPessoaNrIdentificacao(bean.getCliente());
		String nmPessoa = this.getNmPessoa(bean.getCliente());
		String dsMotivo = bean.getDsMotivoSolicitacao();

		return configuracoesFacade.getMensagem("LMS-01236", new Object[] { 
				dsDivisao, 
				dsSituacaoSolicitada,
				nrIdentificacao, 
				nmPessoa,
				dsMotivo });
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
			nrIdent = cliente.getPessoa().getNrIdentificacao();
		}
		return nrIdent;
	}
	
	public List<DivisaoCliente> findByIdClienteAndTpModal(Long idCliente, String tpModal) {
		return getDivisaoClienteDAO().findByIdClienteAndTpModal(idCliente, tpModal);
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public HistoricoWorkflowService getHistoricoWorkflowService() {
		return historicoWorkflowService;
	}

	public void setHistoricoWorkflowService(HistoricoWorkflowService historicoWorkflowService) {
		this.historicoWorkflowService = historicoWorkflowService;
	}
	
	public List<Map<String,Long>> getIdTabelaDivisaoClienteByIdDivisaoCliente(Long idDivisaoCliente){
		return getDivisaoClienteDAO().findIdTabelaDivisaoClienteByIdDivisaoCliente(idDivisaoCliente);
	}
	
}