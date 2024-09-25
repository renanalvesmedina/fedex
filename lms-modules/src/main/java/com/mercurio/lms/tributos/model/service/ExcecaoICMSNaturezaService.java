package com.mercurio.lms.tributos.model.service;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.tributos.model.ExcecaoICMSNatureza;
import com.mercurio.lms.tributos.model.dao.ExcecaoICMSNaturezaDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.excecaoICMSNaturezaService"
 */
public class ExcecaoICMSNaturezaService extends CrudService<ExcecaoICMSNatureza, Long> {


	private NaturezaProdutoService naturezaProdutoService;
	
	/**
	 * Recupera uma inst�ncia de <code>Object</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ExcecaoICMSNatureza findById(java.lang.Long id) {
        return (ExcecaoICMSNatureza)super.findById(id);
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
	 * Remove todas as ExcecaoICMSNatureza atrav�s do IdExcecaoICMSCliente
	 * 
	 */
	public void removeByIdExcecaoICMSCliente(Long idExcecaoICMSCliente){
		getExcecaoICMSNaturezaDAO().removeByIdExcecaoICMSCliente(idExcecaoICMSCliente);
	}	
	
	
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ExcecaoICMSNatureza bean) {
        return super.store(bean);
    }
    
    /**
     * Retorna uma lista de mapas com todas as entidades ExcecaoICMSNAtureza 
     * relacionada com ExcecaoICMSCliente
     * 
     * @return List<Map<String, Object>>
     */
    public List findICMSNaturezaByIdICMSCliente(TypedFlatMap criteria){
    	
    	Long idExcecaoICMSCliente = criteria.getLong("idExcecaoICMSCliente");
    	
    	return getExcecaoICMSNaturezaDAO().findICMSNaturezaByIdICMSCliente(idExcecaoICMSCliente);
    }
    
    public List findICMSNaturezaByIdICMSCliente(Long id){    	
    	return getExcecaoICMSNaturezaDAO().findICMSNaturezaByIdICMSCliente(id);
    }

    public List  findNaturezaProduto(){
    	return naturezaProdutoService.findAllAtivo();
    }
    
    public List findListICMSNaturezaByIdICMSCliente(Long id){
    	return getExcecaoICMSNaturezaDAO().findICMSNaturezaByIdICMSCliente(id);
    }
    
	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setExcecaoICMSNaturezaDAO(ExcecaoICMSNaturezaDAO dao) {
        setDao( dao );
    }
    
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ExcecaoICMSNaturezaDAO getExcecaoICMSNaturezaDAO() {
        return (ExcecaoICMSNaturezaDAO) getDao();
    }

	public void setNaturezaProdutoService(
			NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}
    
}