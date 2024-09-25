package com.mercurio.lms.pendencia.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.pendencia.model.ItemMda;
import com.mercurio.lms.pendencia.model.dao.ItemMdaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.pendencia.itemMdaService"
 */
public class ItemMdaService extends CrudService<ItemMda, Long> {


	/**
	 * Recupera uma instância de <code>ItemMda</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public ItemMda findById(java.lang.Long id) {
        return (ItemMda)super.findById(id);
    }
    
	/**
	 * Recupera uma instância de <code>ItemMda</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public ItemMda findItemMdaById(java.lang.Long id) {
        return this.getItemMdaDAO().findItemMdaById(id);
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
    public java.io.Serializable store(ItemMda bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setItemMdaDAO(ItemMdaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ItemMdaDAO getItemMdaDAO() {
        return (ItemMdaDAO) getDao();
    }
 
    
    /**
     * Retorna um ResultSetPage com os objetos de ItemMDA a serem mostrados na grid.
     * 
     * @param TypedFlatMap criteria
     * @return
     */
    public ResultSetPage findPaginatedItemMda(TypedFlatMap criteria) {
    	return this.getItemMdaDAO().findPaginatedItemMda(criteria, FindDefinition.createFindDefinition(criteria));
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados 
     * para determinados parametros.
     * 
     * @param TypedFlatMap criteria
     * @return
     */
    public Integer getRowCountItemMda(TypedFlatMap criteria) {
    	return this.getItemMdaDAO().getRowCountItemMda(criteria, FindDefinition.createFindDefinition(criteria));
    }
    
	/**
	 * Método que retorna um ItemMda a partir do ID do DoctoServico referente ao MDA. 
	 * 
	 * @param idDoctoServico
	 * @return List
	 */
    public List findItemMdaByMda(Long idDoctoServico) {
    	return this.getItemMdaDAO().findItemMdaByMda(idDoctoServico);
    }
        
}