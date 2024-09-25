package com.mercurio.lms.sim.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.DocumentoServicoRetirada;
import com.mercurio.lms.sim.model.SolicitacaoRetirada;
import com.mercurio.lms.sim.model.dao.SolicitacaoRetiradaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sim.solicitacaoRetiradaService"
 */
public class SolicitacaoRetiradaService extends CrudService<SolicitacaoRetirada, Long> {
 
	private WorkflowPendenciaService pendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private DoctoServicoService doctoServicoService;
	
	/**
	 * Recupera uma instância de <code>SolicitacaoRetirada</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public SolicitacaoRetirada findById(java.lang.Long id) {
        return (SolicitacaoRetirada)super.findById(id);
    }

    
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	ResultSetPage rsp = getSolicitacaoRetiradaDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));		
		return rsp;
    }
    
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		Integer rowCountCustom = this.getSolicitacaoRetiradaDAO().getRowCount(criteria);
		return rowCountCustom;
	}
	
	public List findGridDoctoServicoPaginated(Map criteria){
		List result = getSolicitacaoRetiradaDAO().findGridDoctoServicoPaginated(criteria);
		
		return result;
	}
	   
	public Integer getRowCountGridDoctoServico(TypedFlatMap criteria) {
		Integer rowCountCustom = this.getSolicitacaoRetiradaDAO().getRowCountGridDoctoServico(criteria);
		return rowCountCustom;
	 }
	
	public boolean validateLocalizacaoMercadoria(Long idDoctoServico){
		return getSolicitacaoRetiradaDAO().verificaLocalizacaoMercadoria(idDoctoServico);
	}
	
	public boolean validateFilialLocalizacaoMercadoria(Long idDoctoServico, Long idFilial){
		 return getSolicitacaoRetiradaDAO().verificaFilialLocalizacaoMercadoria(idDoctoServico, idFilial);
	}
	 
	public boolean validateDoctoServicoInclusao(Long idSolicitacaoRetirada, Long idDoctoServico){
		return getSolicitacaoRetiradaDAO().validateDoctoServicoInclusao(idSolicitacaoRetirada, idDoctoServico);
	}
	 
	public String findFluxoDoctoServico(Long idDoctoServico){
		return getSolicitacaoRetiradaDAO().findFluxoDoctoServico(idDoctoServico);
	}
	
	public List findDocsBySolicitacaoRetirada(Long idSolicitacaoRetirada){
		return getSolicitacaoRetiradaDAO().findDocsBySolicitacaoRetirada(idSolicitacaoRetirada);
	}
	
	public Integer getRowCountDocsBySolicitacaoRetirada(Long idSolicitacaoRetirada){
		return getSolicitacaoRetiradaDAO().getRowCountDocsBySolicitacaoRetirada(idSolicitacaoRetirada);
	}
	
	public Map findDadosDoctoServico(Long idDoctoServico){
		 return getSolicitacaoRetiradaDAO().findDadosDoctoServico(idDoctoServico);
	}
	
	public DoctoServico findDocumentoServicoRetiradaById(Long idDoctoServico){
		DoctoServico documentoServico = (DoctoServico)getSolicitacaoRetiradaDAO().getAdsmHibernateTemplate().get(DoctoServico.class,idDoctoServico);
		
		DoctoServico novoDoctoServico = new DoctoServico(); 
		novoDoctoServico.setIdDoctoServico(documentoServico.getIdDoctoServico());
		novoDoctoServico.setBlPrioridadeCarregamento(documentoServico.getBlPrioridadeCarregamento());
		novoDoctoServico.setNrDoctoServico(documentoServico.getNrDoctoServico());
		novoDoctoServico.setTpDocumentoServico(documentoServico.getTpDocumentoServico());
				
		Cliente cliente = documentoServico.getClienteByIdClienteRemetente();
		Cliente novoCliente = new Cliente();
		novoCliente.setIdCliente(cliente.getIdCliente());		
		Pessoa pessoa = cliente.getPessoa();
		Pessoa novoPessoa  = new Pessoa();
		novoPessoa.setIdPessoa(novoPessoa.getIdPessoa());
		novoPessoa.setNmPessoa(pessoa.getNmPessoa());
		novoPessoa.setTpIdentificacao(pessoa.getTpIdentificacao());
		novoPessoa.setNrIdentificacao(pessoa.getNrIdentificacao());
		novoCliente.setPessoa(novoPessoa);
		novoDoctoServico.setClienteByIdClienteRemetente(novoCliente);
		
		Filial filial = documentoServico.getFilialByIdFilialOrigem();
		Filial novoFilial = new Filial();
		novoFilial.setIdFilial(filial.getIdFilial());
		novoFilial.setSgFilial(filial.getSgFilial());		
		novoDoctoServico.setFilialByIdFilialOrigem(novoFilial);
		
		Filial filialDestino = documentoServico.getFilialByIdFilialDestino();
		Filial novoFilialDestino = new Filial();
		novoFilialDestino.setIdFilial(filialDestino.getIdFilial());
		novoFilialDestino.setSgFilial(filialDestino.getSgFilial());	
		Pessoa pessoaFilial = filialDestino.getPessoa();
		Pessoa novaPessoaFilial = new Pessoa();
		novaPessoaFilial.setNmFantasia(pessoaFilial.getNmFantasia());
		novoFilialDestino.setPessoa(novaPessoaFilial);
		novoDoctoServico.setFilialByIdFilialDestino(filialDestino);
		
		return novoDoctoServico;
		
	}
	 
	protected SolicitacaoRetirada beforeStore(SolicitacaoRetirada bean) {
		SolicitacaoRetirada sr = (SolicitacaoRetirada) bean;
		
		if (sr.getDestinatario() == null && sr.getConsignatario() == null) {
			throw new BusinessException("LMS-10022");
		}
		
		if (sr.getTpRegistroLiberacao().getValue().equals("C") && 
		    ("".equals(sr.getNmResponsavelAutorizacao()) || StringUtils.isBlank(sr.getDsFuncaoResponsavelAutoriza()) || StringUtils.isBlank(sr.getNrTelefoneAutorizador()))){
				throw new BusinessException("LMS-10024");
		}
		
		DateTime dtAtual = JTDateTimeUtils.getDataHoraAtual(); 
		dtAtual = dtAtual.minusMinutes(1);
		if (sr.getDhPrevistaRetirada().isBefore(dtAtual)){
			throw new BusinessException("LMS-10025");
		}
		
		if (sr.getTpIdentificacao() == null && sr.getNrCnpj() == null && StringUtils.isBlank(sr.getNrRg()) ){
			throw new BusinessException("LMS-10026");
		}
		
		return super.beforeStore(bean);
	}
	
	public Conhecimento findConhecimento(Long idConhecimento){
		Conhecimento c = (Conhecimento) getSolicitacaoRetiradaDAO().getAdsmHibernateTemplate().get(Conhecimento.class, idConhecimento);
		
		if (Hibernate.isInitialized(c)) {
			if (c.getLocalizacaoMercadoria() != null)
				Hibernate.initialize(c.getLocalizacaoMercadoria());
			return c;
		}
		else return null;
		
	}
	
	protected SolicitacaoRetirada beforeInsert(SolicitacaoRetirada bean) {
		SolicitacaoRetirada sr = (SolicitacaoRetirada) bean;
		sr.setTpSituacao(new DomainValue("A"));
		sr.setDhSolicitacao(JTDateTimeUtils.getDataHoraAtual());
		sr.setUsuarioCriacao(SessionUtils.getUsuarioLogado());
		
		sr.setNrSolicitacaoRetirada(configuracoesFacade.incrementaParametroSequencial(sr.getFilial().getIdFilial(), "NR_SOLICITACAO_RETIR", true));
		
		return super.beforeInsert(bean);
	}
	
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeByIdComplete(java.lang.Long id) {
        getSolicitacaoRetiradaDAO().removeByIdComplete(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsComplete(List ids) {
    	getSolicitacaoRetiradaDAO().removeByIdsComplete(ids);
    }
	
	public void executeCancelarSolicitacaoRetirada(Long idSolicitacaoRetirada){
		getSolicitacaoRetiradaDAO().alteraSituacaoSolicitacaoRetirada(idSolicitacaoRetirada, "C");
	}
		
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
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
    public java.io.Serializable store(SolicitacaoRetirada bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSolicitacaoRetiradaDAO(SolicitacaoRetiradaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SolicitacaoRetiradaDAO getSolicitacaoRetiradaDAO() {
        return (SolicitacaoRetiradaDAO) getDao();
    }


	public SolicitacaoRetirada store(SolicitacaoRetirada sr, ItemList items) {
		boolean masterIdIsNull = sr.getIdSolicitacaoRetirada() == null;
    	
    	try {
    		this.beforeStore(sr);    	
    		
    		getSolicitacaoRetiradaDAO().store(sr);
    		
    		cancelaDocumentosWorkflow(items.getRemovedItems());
    		createPendenciaWorkflow(sr, items.getNewOrModifiedItems());
    		    		
			getSolicitacaoRetiradaDAO().storeItems(items);						
			
		} catch (HibernateOptimisticLockingFailureException e) {
			throw new BusinessException("LMS-00012");
			
		} catch (RuntimeException e) {
			this.rollbackMasterState(sr, masterIdIsNull, e);
			items.rollbackItemsState();
            
            throw e;
		}
		
        return sr;
		
	}

	
	public void createPendenciaWorkflow(SolicitacaoRetirada sr, List itens){
		
		String labelDocumento = configuracoesFacade.getMensagem("documento");
		String labelRetiradoFilial = configuracoesFacade.getMensagem("paraSerRetiradoNaFilial");
		
		for (Iterator it = itens.iterator(); it.hasNext();){
			
			DocumentoServicoRetirada dsr = (DocumentoServicoRetirada) it.next();
			
			if (dsr.getPendencia() == null) {
				Filial filial = doctoServicoService.findFilialDestinoOperacionalById(dsr.getDoctoServico().getIdDoctoServico());
				
				if (filial != null && !filial.getIdFilial().equals(sr.getFilialRetirada().getIdFilial())) {
					StringBuffer obs = new StringBuffer()
													.append(labelDocumento)
													.append(" ")
													.append(FormatUtils.formatSgFilialWithLong(dsr.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial(), 
																							   dsr.getDoctoServico().getNrDoctoServico()))												
													.append(" ")
													.append(labelRetiradoFilial)
													.append(" ")													
													.append(sr.getFilialRetirada().getSgFilial())
													.append(" - ")
													.append(sr.getFilialRetirada().getPessoa().getNmFantasia());
								
					Pendencia pendencia = pendenciaService.generatePendencia(filial.getIdFilial(),  
																			   ConstantesWorkflow.NR1001_DOCTO_RETFIL, 
																			   sr.getIdSolicitacaoRetirada(), 
																			   obs.toString(), 
																			   JTDateTimeUtils.getDataHoraAtual());
					dsr.setPendencia(pendencia);
				}				
			}
		}
	}

	
	private void cancelaDocumentosWorkflow(List docs){
		
		for (Iterator it = docs.iterator(); it.hasNext();){
			DocumentoServicoRetirada dsr = (DocumentoServicoRetirada) it.next();
			if (dsr.getPendencia() != null)
				pendenciaService.cancelPendencia(dsr.getPendencia().getIdPendencia());
		}
	}
	
	
	/**
	 * Método que retorna um List de solicitação de retirada pelo ID do DoctoServico.
	 * @param idDoctoServico
	 * @return
	 */
	public List findSolicitacaoRetiradaByIdDoctoServico(Long idDoctoServico) {
		return this.getSolicitacaoRetiradaDAO().findSolicitacaoRetiradaByIdDoctoServico(idDoctoServico);
	}
	
	public Boolean findExistenciaSolicitacaoRetirada(Long idDoctoServico, String tpSituacao) {
		Number result = (Number) this.getSolicitacaoRetiradaDAO().findExistenciaSolicitacaoRetirada(idDoctoServico, tpSituacao);
		return result != null && result.longValue() > 0;
	}

	/**
	 * @param pendenciaService The pendenciaService to set.
	 */
	public void setPendenciaService(WorkflowPendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}


	/**
	 * @param configuracoesFacade The configuracoesFacade to set.
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}


	/**
	 * @param doctoServicoService The doctoServicoService to set.
	 */
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}


}