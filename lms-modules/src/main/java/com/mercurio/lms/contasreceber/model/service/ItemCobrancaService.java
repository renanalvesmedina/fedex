package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.ItemCobranca;
import com.mercurio.lms.contasreceber.model.dao.ItemCobrancaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.itemCobrancaService"
 */
public class ItemCobrancaService extends CrudService<ItemCobranca, Long> {


	/**
	 * Recupera uma inst�ncia de <code>ItemCobranca</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ItemCobranca findById(java.lang.Long id) {
        return (ItemCobranca)super.findById(id);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ItemCobranca bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setItemCobrancaDAO(ItemCobrancaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ItemCobrancaDAO getItemCobrancaDAO() {
        return (ItemCobrancaDAO) getDao();
    }
    
    /**
	 * M�todo respons�vel por buscar itemCobranca que associados a fatura em quest�o
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