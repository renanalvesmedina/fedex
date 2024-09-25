package com.mercurio.lms.seguros.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.seguros.model.SituacaoReembolso;
import com.mercurio.lms.seguros.model.dao.SituacaoReembolsoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.seguros.situacaoReembolsoService"
 */
public class SituacaoReembolsoService extends CrudService<SituacaoReembolso, Long> {
	
	/**
	 * Recupera uma inst�ncia de <code>SituacaoReembolso</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public SituacaoReembolso findById(java.lang.Long id) {
        return (SituacaoReembolso)super.findById(id);
    }
    
    protected SituacaoReembolso beforeStore(SituacaoReembolso bean) {
		return super.beforeStore(bean);
	}
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.	
	 */
    public java.io.Serializable store(SituacaoReembolso bean) {    	    	
        return super.store(bean);
    }
    
    /* Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
   @Override
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
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
   public void removeByIds(List ids) {
       super.removeByIds(ids);
   }
	    
   /**
    
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setSituacaoReembolsoDAO(SituacaoReembolsoDAO dao) {
        setDao(dao);
    }
    
    public List findOrderByDsReembolso(Map parameters) {
    	Map criteria = parameters;
    	//Este if � para garantir que o map n�o venha null do jsp
    	if( criteria == null ) {
    		criteria = new HashMap();
    	}
        List orderBy = new ArrayList();
        orderBy.add("dsSituacaoReembolso:asc");
        
        return getDao().findListByCriteria(criteria, orderBy);
    }

}
