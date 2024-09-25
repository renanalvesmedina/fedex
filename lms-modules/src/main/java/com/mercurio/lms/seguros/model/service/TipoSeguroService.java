package com.mercurio.lms.seguros.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.seguros.model.TipoSeguro;
import com.mercurio.lms.seguros.model.dao.TipoSeguroDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.seguros.tipoSeguroService"
 */
public class TipoSeguroService extends CrudService<TipoSeguro, Long> {


	/**
	 * Recupera uma inst�ncia de <code>TipoSeguro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public TipoSeguro findById(java.lang.Long id) {
        return (TipoSeguro)super.findById(id);
    }

	protected TipoSeguro beforeStore(TipoSeguro bean) {
		return (TipoSeguro)super.beforeStore(bean);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        getTipoSeguroDAO().removeById(id, true);
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
        getTipoSeguroDAO().removeByIds(ids, true);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(TipoSeguro bean) {
    	
    	// LMS-6145
    	if("true".equals(bean.getBlEnvolveCarga())) {
    		if(bean.getTpModal() == null || bean.getTpAbrangencia() == null) {
    			throw new BusinessException("LMS-22024");
    		}
    		bean.setBlEnvolveCarga("S");
    	} else {
    		bean.setBlEnvolveCarga("N");
    	}
    	    	
        return super.store(bean);
    }
    
	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTipoSeguroDAO(TipoSeguroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TipoSeguroDAO getTipoSeguroDAO() {
        return (TipoSeguroDAO) getDao();
    }
    
    /**
     * M�todo para retornar uma list ordenada.
     * Utilizado em combobox.
     * @param criteria
     * @return
     */
    public List findOrderByDsTipo(Map criteria){
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsTipo:asc");
        return getDao().findListByCriteria(criteria, campoOrdenacao);
    }
    
    public List findOrderBySgTipo(Map criteria){
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("sgTipo:asc");
        return getDao().findListByCriteria(criteria, campoOrdenacao);
    }
    
    /**
	 * M�todo que valida se as combos de modal e abrang�ncia est�o preenchidas, para preencher a combo de tipo de seguro 
	 * @param criteria
	 * @return List com resultado da consulta
	 */
    public List findComboByTipoSeguro(Map criteria){
    	String sgModal = (String) criteria.get("sgModal");
    	String sgAbrangencia = (String) criteria.get("sgAbrangencia");
        
        String tpSituacao = null;
        
        if( criteria.containsKey("tpSituacao")){
            tpSituacao = (String) criteria.get("tpSituacao");
        }
        
    	return getTipoSeguroDAO().findComboByTipoSeguro(sgModal, sgAbrangencia, tpSituacao);
    }
    
}