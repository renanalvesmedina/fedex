package com.mercurio.lms.contasreceber.model.service;

import java.util.List;


import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.contasreceber.model.ItemPreFatura;
import com.mercurio.lms.contasreceber.model.dao.ItemPreFaturaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.itemPreFaturaService"
 */
public class ItemPreFaturaService extends CrudService<ItemPreFatura, Long> {


	


	/**Recupera estancias  de itemPreFatura de acordo com o idFatura
	 * 
	 * @author Diego Umpierre
	 * @param idFatura
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedComplemento(Long idFatura, FindDefinition findDef) {

		return getItemPreFaturaDAO().findPaginatedComplemento(idFatura,findDef);
		
	}

	
	
	/**Recupera  o n�mero de linhas da consulta findPaginatedComplemento de acordo com o idFatura passado.
	 * 
	 * @author Diego Umpierre
	 * @param idFatura
	 * @return
	 */
	public Integer getRowCountComplemento(Long idFatura) {
		return getItemPreFaturaDAO().getRowCountComplemento(idFatura);
	}

	
	
	/**
	 * Recupera uma inst�ncia de <code>RepositorioItemRedeco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ItemPreFatura findById(java.lang.Long id) {
        return (ItemPreFatura)super.findById(id);
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
    public java.io.Serializable store(ItemPreFatura bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setItemPreFaturaDAO(ItemPreFaturaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ItemPreFaturaDAO getItemPreFaturaDAO() {
        return (ItemPreFaturaDAO) getDao();
    }
   }