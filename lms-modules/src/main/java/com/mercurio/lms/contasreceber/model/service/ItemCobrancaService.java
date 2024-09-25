package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.ItemCobranca;
import com.mercurio.lms.contasreceber.model.dao.ItemCobrancaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.itemCobrancaService"
 */
public class ItemCobrancaService extends CrudService<ItemCobranca, Long> {


	/**
	 * Recupera uma instância de <code>ItemCobranca</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ItemCobranca findById(java.lang.Long id) {
        return (ItemCobranca)super.findById(id);
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
    public java.io.Serializable store(ItemCobranca bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setItemCobrancaDAO(ItemCobrancaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ItemCobrancaDAO getItemCobrancaDAO() {
        return (ItemCobrancaDAO) getDao();
    }
    
    /**
	 * Método responsável por buscar itemCobranca que associados a fatura em questão
	 * 
	 * @param idFatura
	 * @return List de itemCobranca
	 */
    public List findFaturaInCobrancaInadimplencia(java.lang.Long idFatura) {
    	return getItemCobrancaDAO().findFaturaInCobrancaInadimplencia(idFatura);
    }
    
    /**
     * 
     * 
     * Hector Julian Esnaola Junior
     * 29/02/2008
     *
     * @param idFatura
     * @return
     *
     * List
     *
     */
    public Long findItemCobrancaByIdFatura(Long idFatura, DomainValue... domainValues) {
    	return getItemCobrancaDAO().findItemCobrancaByIdFatura(idFatura, domainValues);
    }
}