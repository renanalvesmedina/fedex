package com.mercurio.lms.tributos.model.service;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.tributos.model.RemetenteExcecaoICMSCli;
import com.mercurio.lms.tributos.model.dao.RemetenteExcecaoICMSCliDAO;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.remetenteExcecaoICMSCliService"
 */
public class RemetenteExcecaoICMSCliService extends CrudService<RemetenteExcecaoICMSCli, Long> {

	private PessoaService pessoaService;
	private ClienteService clienteService;
	
	public void setClienteService(ClienteService clienteService){
		this.clienteService = clienteService;
	}
	
	public void setPessoaService(PessoaService pessoaService){
		this.pessoaService = pessoaService;
	}

	/**
	 * Recupera uma inst�ncia de <code>Object</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public RemetenteExcecaoICMSCli findById(java.lang.Long id) {
        return (RemetenteExcecaoICMSCli)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    /**
     * M�todo invocado antes do store 
     */ 
	public RemetenteExcecaoICMSCli beforeStore(RemetenteExcecaoICMSCli bean) {
		RemetenteExcecaoICMSCli reic = (RemetenteExcecaoICMSCli) bean;
		
		/** Busca registros na base com intervalos de vig�ncia iguais */
		List lst = getRemetenteExcecaoICMSCliDAO().findRemetenteExcecaoICMSCliByVigenciaEquals(reic.getDtVigenciaInicial()
						, reic.getDtVigenciaFinal()
						, reic.getExcecaoICMSCliente().getIdExcecaoICMSCliente()
						, reic.getNrCnpjParcialRem()
						, reic.getIdRemetenteExcecaoICMSCli());
		
		/** Verifica se n�o j� n�o existe nenhum registro na base com o mesmo intervalo de vig�ncia */
		if(!lst.isEmpty()){
			throw new BusinessException("LMS-00047");
		}
		 
		return super.beforeStore(bean);
	}
	
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(RemetenteExcecaoICMSCli bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setRemetenteExcecaoICMSCliDAO(RemetenteExcecaoICMSCliDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private RemetenteExcecaoICMSCliDAO getRemetenteExcecaoICMSCliDAO() {
        return (RemetenteExcecaoICMSCliDAO) getDao();
    }
    
    /**
	 * M�todo respons�vel por buscar nrCNPJParcialRem igual as primeiras 8 posi��es do nrIdentificacao da Pessoa 
	 * 
	 * @author HectorJ
	 * @since 31/05/2006
	 * 
	 * @param nrCNPJParcialRem
	 */
	public List findPessoaByNrIdentificacao(String nrCnpj){
		List lst = pessoaService.findPessoasByNrIdentificacao(nrCnpj);
		if(lst == null || lst.isEmpty()){
			throw new BusinessException("LMS-23030");
		}
		return lst;
	}
	
	public Cliente findClienteByCNPJParcial(String cnpjParcial){
		Cliente cliente = clienteService.findByCNPJParcial(cnpjParcial);
		if (cliente == null){
			throw new BusinessException("LMS-23030");
		}
		return cliente;
		
	}
	
	/**
	 * M�todo respons�vel por buscar os dados da grid
	 * 
	 * @author Diego Umpierre
	 * @since 20/07/2006
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */	
	public ResultSetPage findPaginatedTela(TypedFlatMap criteria) {
		ResultSetPage rsp = getRemetenteExcecaoICMSCliDAO().findPaginatedTela(criteria, 
				FindDefinition.createFindDefinition(criteria));
    	return rsp;
	}

		/**
		 * M�todo respons�vel por buscar o numero de linhas da grid 
		 * 
		 * @author Diego Umpierre
		 * @since 20/07/2006
		 * 
		 * @param criteria
		 * @return Integer
		 */ 
		public Integer getRowCountTela(TypedFlatMap criteria) {
			return getRemetenteExcecaoICMSCliDAO().getRowCountTela(criteria);
	}
	
	public List findRemetenteExcecaoICMSByDadosCliente(String nrIdentificacao, Long idTipoTributacaoDevido, Long idFilialOrigemDocto){
		return getRemetenteExcecaoICMSCliDAO().findRemetenteExcecaoICMSByDadosCliente(nrIdentificacao,idTipoTributacaoDevido,idFilialOrigemDocto);
	}	
	
}