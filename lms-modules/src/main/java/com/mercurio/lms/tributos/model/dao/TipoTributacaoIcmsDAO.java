package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tributos.model.TipoTributacaoIcms;
import com.mercurio.lms.util.SQLUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoTributacaoIcmsDAO extends BaseCrudDao<TipoTributacaoIcms, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoTributacaoIcms.class;
    }

    /**
     * Lista de resultados a partir dos critérios de busca
     * ordenados por dsTipoTributacaoIcms 
     * 
     * @author Alexandre Menezes
     * @param  criterions Critérios de busca.
     * @return Lista de resultados sem paginação. 
     */
    public List findListByCriteria(Map criterions) {
   	
    	if (criterions == null) criterions = new HashMap(1);
    	List order = new ArrayList(1);
    	order.add("dsTipoTributacaoIcms");
    	
        return super.findListByCriteria(criterions, order);
    }
    
    /**
     * Método responsável por popular a combo de TipoTributacaoICMS
     * 
     * @author HectorJ
     * @since 31/05/2006
     * 
     * @param notInIdsParametrosGerais
     * @return List de TipoTributacaoICMS
     */
    public List findComboTipoTributacaoIcms(List notInIdsParametrosGerais, String tpSituacao, Long idTipoTributacaoIcms){
		
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("tti");
    	hql.addFrom(getPersistentClass().getName() + " tti ");
    	
    	mountCriteriaComboTipoTributacaoIcms(notInIdsParametrosGerais, tpSituacao, idTipoTributacaoIcms, hql);
    	
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 06/12/2006
	 *
	 * @param tpSituacao
	 * @param idTipoTributacaoIcms
	 * @param hql
	 *
	 */
	private void mountCriteriaComboTipoTributacaoIcms(List notInIdsParametrosGerais, String tpSituacao, Long idTipoTributacaoIcms, SqlTemplate hql) {
		
		SQLUtils.joinExpressionsWithComma(notInIdsParametrosGerais, hql, "tti.idTipoTributacaoIcms not");
		
		/** Caso a situação não seja nula e seja = 'A' */
    	if(tpSituacao != null && tpSituacao.equals("A")){
    		
    		/** Caso seja informado o idTipoTributacaoIcms, o mesmo é incluso no filtro */
    		if(idTipoTributacaoIcms != null){
    			
    			hql.addCustomCriteria(" (tti.tpSituacao = 'A' OR tti.idTipoTributacaoIcms = ?) ");
    			hql.addCriteriaValue(idTipoTributacaoIcms);
    			
    		}else{
    			hql.addCriteria("tti.tpSituacao", "=", "A");
    		}
    		
    	}
    	
	}
	
	/**
     * Busca uma entidade TipoTributacaoIcms de acordo com o campo dsTipoTributacaoIcms
     *
     * @author Hector Julian Esnaola Junior
     * @since 18/01/2007
     *
     * @param dsTipoTributacaoIcms
     * @return
     *
     */
    public TipoTributacaoIcms findTipoTributacaoIcmsByDsTipoTributacaoIcms( String dsTipoTributacaoIcms ){
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection(" tti ");
    	
    	hql.addInnerJoin(getPersistentClass().getName() + " tti ");
    	
    	hql.addCriteria("tti.dsTipoTributacaoIcms", "=", dsTipoTributacaoIcms);    	
    	
    	return (TipoTributacaoIcms) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }


}