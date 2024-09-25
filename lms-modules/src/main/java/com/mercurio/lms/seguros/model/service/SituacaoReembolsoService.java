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
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.situacaoReembolsoService"
 */
public class SituacaoReembolsoService extends CrudService<SituacaoReembolso, Long> {
	
	/**
	 * Recupera uma instância de <code>SituacaoReembolso</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public SituacaoReembolso findById(java.lang.Long id) {
        return (SituacaoReembolso)super.findById(id);
    }
    
    protected SituacaoReembolso beforeStore(SituacaoReembolso bean) {
		return super.beforeStore(bean);
	}
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.	
	 */
    public java.io.Serializable store(SituacaoReembolso bean) {    	    	
        return super.store(bean);
    }
    
    /* Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
   @Override
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
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
   public void removeByIds(List ids) {
       super.removeByIds(ids);
   }
	    
   /**
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSituacaoReembolsoDAO(SituacaoReembolsoDAO dao) {
        setDao(dao);
    }
    
    public List findOrderByDsReembolso(Map parameters) {
    	Map criteria = parameters;
    	//Este if é para garantir que o map não venha null do jsp
    	if( criteria == null ) {
    		criteria = new HashMap();
    	}
        List orderBy = new ArrayList();
        orderBy.add("dsSituacaoReembolso:asc");
        
        return getDao().findListByCriteria(criteria, orderBy);
    }

}
