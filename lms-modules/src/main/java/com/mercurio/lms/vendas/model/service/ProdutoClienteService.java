package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.Produto;
import com.mercurio.lms.expedicao.model.service.ProdutoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.ProdutoCliente;
import com.mercurio.lms.vendas.model.dao.ProdutoClienteDAO;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.produtoClienteService"
 */
public class ProdutoClienteService extends CrudService<ProdutoCliente, Long> {

	private UsuarioService usuarioService;
	private ProdutoService produtoService;	
	private WorkflowPendenciaService workflowPendenciaService;
	private DomainValueService domainValueService;
	
	/**
	 * Recupera uma instância de <code>ProdutoCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ProdutoCliente findById(java.lang.Long id) {
		return (ProdutoCliente)super.findById(id);
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
	public java.io.Serializable store(ProdutoCliente bean) {
		
		Serializable prodId = super.store(bean);		
		getProdutoClienteDAO().getAdsmHibernateTemplate().flush();
		
		ProdutoCliente produtoCliente = findById((Long)prodId);		
		trataProdutosRiscoEPerigosos(produtoCliente);
		
		return produtoCliente;
	}

	public List<ProdutoCliente> findByIdCliente(Long idCliente){
		return getProdutoClienteDAO().findByIdCliente(idCliente);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public Map storeMap(Map bean) {
		ProdutoCliente produtoCliente = new ProdutoCliente();
		ReflectionUtils.copyNestedBean(produtoCliente, bean);

		this.store(produtoCliente);
		
		if (produtoCliente.getSituacaoAprovacao() == null)
			bean.put("situacaoAprovacao", "");
		else
			bean.put("situacaoAprovacao", produtoCliente.getSituacaoAprovacao().getValue());
		
		return bean;
	}	
	
	/**
	 * Metodos responsavel por tratar das regras de negócio de Produtos Perigosos
	 * ou produtos de Risco 
	 */
	private void trataProdutosRiscoEPerigosos(ProdutoCliente bean){

		Produto prod = produtoService.findById(bean.getProduto().getIdProduto());
		
		// Verifica se produto que está sendo cadastrado NÂO pertence a categoria
		// de ‘Produto Perigoso’ e NÂO ‘Produto de Risco’		
//		if (prod != null &&
//			!prod.getCategoria().getValue().equals("PR") &&
//			!prod.getCategoria().getValue().equals("PE")) {
//			return;
//		}
		
		Pendencia pendencia = null;		
		
//		if (prod.getCategoria().getValue().equals("PE")) {			
//			pendencia = generateWorkflow(SessionUtils.getFilialSessao().getIdFilial(), bean,
//						ConstantesWorkflow.NR105_APROVACAO_PRODUTO_PERIGOSO, 
//						"Solicitação de aprovação para Produto Perigoso.");
//		} else if (prod.getCategoria().getValue().equals("PR")) {
//			pendencia = generateWorkflow(SessionUtils.getFilialSessao().getIdFilial(), bean,
//						ConstantesWorkflow.NR106_APROVACAO_PRODUTO_RISCO,
//						"Solicitação de aprovação para Produto de Risco.");
//		}

		if (pendencia != null){
			//seta a situação de Aprovação para “Pendente”.
			bean.setSituacaoAprovacao(domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW", "E"));
			
			//Setando o ID_PENDENCIA conforme o retorno da geração da pendência 
			bean.setPendencia(pendencia);
		}
	}
	

    /**
     * Gera um workflow padrão a partir do produtoCliente informado. 
     * @return Pendencia
     */
    private Pendencia generateWorkflow(Long filialId, ProdutoCliente bean, Short cdWorkflow, String observacao){
    	return workflowPendenciaService.generatePendencia(filialId, 
    			                                          cdWorkflow, 
    			                                          bean.getIdProdutoCliente(), 
    			                                          observacao, 
    			                                          JTDateTimeUtils.getDataHoraAtual());
    }
	
	public String executeWorkflow(List<Long> listIds,List<String> listTpSituacao){
		Iterator<String> situacaoIt = listTpSituacao.iterator();
		for(Long id:listIds){
			ProdutoCliente produtoCliente = findById(id);
			if (produtoCliente != null){
				produtoCliente.setSituacaoAprovacao(new DomainValue(situacaoIt.next()));
				super.store(produtoCliente);
			}
		}
		return null;
	}
	
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setProdutoClienteDAO(ProdutoClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ProdutoClienteDAO getProdutoClienteDAO() {
		return (ProdutoClienteDAO) getDao();
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	/**
	 * Chama a verificação de permissões do usuário sobre uma filial / regional
	 */
	public Boolean validatePermissao(Long idFilial) {
		return getUsuarioService().verificaAcessoFilialRegionalUsuarioLogado(idFilial);
	}

	public void setProdutoService(ProdutoService produtoService) {
		this.produtoService = produtoService;
}
	
	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
}
