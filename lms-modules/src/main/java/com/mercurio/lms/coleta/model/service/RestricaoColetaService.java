package com.mercurio.lms.coleta.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.RestricaoColeta;
import com.mercurio.lms.coleta.model.dao.ProdutoProibidoDAO;
import com.mercurio.lms.coleta.model.dao.RestricaoColetaDAO;
import com.mercurio.lms.configuracoes.model.dao.ServicoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.restricaoColetaService"
 */
public class RestricaoColetaService extends CrudService<RestricaoColeta, Long> {

	private ServicoDAO servicoDAO;
	private ProdutoProibidoDAO produtoProibidoDAO;

	/**
	 * Recupera uma instância de <code>RestricaoColeta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public RestricaoColeta findById(java.lang.Long id) {
        return (RestricaoColeta)super.findById(id);
    }

    
    
	public ResultSetPage findPaginatedRestricoesColeta(Map criteria) {
		return super.findPaginated(criteria);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		FindDefinition fd = FindDefinition.createFindDefinition(criteria);
		return getRestricaoColetaDAO().findPaginated(criteria, fd);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getRestricaoColetaDAO().getRowCount(criteria);
	}



	/**
     * Realiza uma pesquisa de Servico  
     * @param 
     * @return Servico
     */
    public List findListProdutoProibido(Map criteria){
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsProduto:asc");
       return getProdutoProibidoDAO().findListByCriteria(criteria, campoOrdenacao);        
    }

	/**
     * Realiza uma pesquisa de Servico  
     * @param 
     * @return Servico
     */
    public List findListServico(Map criteria){
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsServico:asc");
       return getServicoDAO().findListByCriteria(criteria, campoOrdenacao);        
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
    public java.io.Serializable store(RestricaoColeta bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRestricaoColetaDAO(RestricaoColetaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RestricaoColetaDAO getRestricaoColetaDAO() {
        return (RestricaoColetaDAO) getDao();
    }

	public ServicoDAO getServicoDAO() {
		return servicoDAO;
	}

	public void setServicoDAO(ServicoDAO servicoDAO) {
		this.servicoDAO = servicoDAO;
	}

	public ProdutoProibidoDAO getProdutoProibidoDAO() {
		return produtoProibidoDAO;
	}

	public void setProdutoProibidoDAO(ProdutoProibidoDAO produtoProibidoDAO) {
		this.produtoProibidoDAO = produtoProibidoDAO;
	}	
   }