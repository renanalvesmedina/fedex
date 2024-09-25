package com.mercurio.lms.integracao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsmmanager.integracao.model.PontoLayoutBinder;
import com.mercurio.lms.integracao.model.dao.PontoLayoutBinderDAO;

/** 
 * @spring.bean id="lms.integracao.pontoLayoutBinderService"                  
 */
public class PontoLayoutBinderService extends CrudService<PontoLayoutBinder, Long>{

	
	public List findAllPontoIntegracao(){
		return getPontoLayoutBinderDAO().findComboPontoIntegracao();
	}
	
	public List findAllGruposLayout(){
		return getPontoLayoutBinderDAO().findAllGruposLayout();
	}
	
	public List findAllLayout(){
		return getPontoLayoutBinderDAO().findAllLayout();
	}
	
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPontoIntegracaoLayoutDAO(PontoLayoutBinderDAO dao) {
        setDao(dao);
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos 
     * dados deste serviço.
     *
     * @return Instância do DAO.
     */
    public PontoLayoutBinderDAO getPontoLayoutBinderDAO() {
        return (PontoLayoutBinderDAO)getDao();
    }    
    
    public ResultSetPage findPaginated(TypedFlatMap criteria) {    
    	return getPontoLayoutBinderDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
    }
    
     public Integer getRowCount(TypedFlatMap criteria) {    
    	return getPontoLayoutBinderDAO().getRowCount(criteria);
    }

	

	/**
	 * 
	 * @param idPontoBinder
	 * @param idLayoutBinder
	 * @return
	 */
	public PontoLayoutBinder findPontoLayoutBinder(Long idPontoBinder, Long idLayoutBinder) {
		return getPontoLayoutBinderDAO().findPontoLayoutBinder(idPontoBinder, idLayoutBinder);
	}
	
	
	@Override
	public PontoLayoutBinder findById(Long id) {
		return (PontoLayoutBinder) super.findById(id);
	}
	
	
}