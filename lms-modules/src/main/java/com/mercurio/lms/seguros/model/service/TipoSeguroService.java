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
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.tipoSeguroService"
 */
public class TipoSeguroService extends CrudService<TipoSeguro, Long> {


	/**
	 * Recupera uma instância de <code>TipoSeguro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public TipoSeguro findById(java.lang.Long id) {
        return (TipoSeguro)super.findById(id);
    }

	protected TipoSeguro beforeStore(TipoSeguro bean) {
		return (TipoSeguro)super.beforeStore(bean);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        getTipoSeguroDAO().removeById(id, true);
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
        getTipoSeguroDAO().removeByIds(ids, true);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
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
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTipoSeguroDAO(TipoSeguroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TipoSeguroDAO getTipoSeguroDAO() {
        return (TipoSeguroDAO) getDao();
    }
    
    /**
     * Método para retornar uma list ordenada.
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
	 * Método que valida se as combos de modal e abrangência estão preenchidas, para preencher a combo de tipo de seguro 
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