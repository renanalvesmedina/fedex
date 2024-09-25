package com.mercurio.lms.contasreceber.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.MotivoDesconto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoDescontoDAO extends BaseCrudDao<MotivoDesconto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoDesconto.class;
    }

    public List findListByCriteria(Map criterions, List order) {
    	order = new ArrayList();
    	order.add("dsMotivoDesconto");
    	return super.findListByCriteria(criterions, order);
    }

    /**
     * Carrega o motivo de desconto de acordo com o tpMotivoDesconto
     *
     * @author Hector Julian Esnaola Junior
     * @since 25/09/2007
     *
     * @param tpDesconto
     * @return
     *
     */
    public MotivoDesconto findByTpMotivoDesconto(String tpMotivoDesconto){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection(" md ");
    	hql.addFrom(getPersistentClass().getName() + " md ");
    	hql.addCriteria("md.tpMotivoDesconto", "=", tpMotivoDesconto);
    	
    	return (MotivoDesconto) getAdsmHibernateTemplate().findUniqueResult(
    			hql.getSql(), 
    			hql.getCriteria());
    }
    
    /**
     * Carrega o motivo de desconto de acordo com o tpMotivoDesconto
     *
     * @author Hector Julian Esnaola Junior
     * @since 25/09/2007
     *
     * @param tpDesconto
     * @param motivo descontos que nao vao ser validados quanto a situacao
     * @return
     *
     */
    public List<MotivoDesconto> findMotivoDescontoByTpMotivoDesconto(String tpMotivoDesconto, Long ... idsMotivoDesconto ){
    	List criteria = new ArrayList();
    	
    	StringBuilder hql = new StringBuilder()
	    	.append("SELECT md ")
	    	.append("FROM " + getPersistentClass().getName() + " md ") 
	    	.append("WHERE (md.tpSituacao = 'A' ");
    	if(idsMotivoDesconto != null){
    		for (Long motivoDesconto : idsMotivoDesconto) {
    			if(motivoDesconto != null){
    				hql.append(" or md.id = ? ");
    				criteria.add(motivoDesconto);
    			}
			}
    	}
    	hql.append(" ) ");
    	
    	if (tpMotivoDesconto != null) {
    		hql.append("AND md.tpMotivoDesconto = ? ");
    		criteria.add(tpMotivoDesconto);
    	}    		   
    	
	    hql.append("ORDER BY ").append(PropertyVarcharI18nProjection.createProjection("md.dsMotivoDesconto"));
	    
    	return (List<MotivoDesconto>) getAdsmHibernateTemplate().find(hql.toString(), criteria.toArray());
    }
    
    /**
     * Carrega uma lista de motivoDesconto de acordo 
     * com a situacao e o idDesconto
     * 
     * Hector Julian Esnaola Junior
     * 18/01/2008
     *
     * @param tpSituacao
     * @param idDesconto
     * @return
     *
     * List<MotivoDesconto>  
     *
     */
    public List<MotivoDesconto> findMotivoDescontoByTpSituacaoAndIdDesconto(String tpSituacao, Long idDesconto){
    	List criteria = new ArrayList();
    	
    	StringBuilder hql = new StringBuilder()
	    	.append("SELECT md ")
	    	.append("FROM " + getPersistentClass().getName() + " md ") 
	    	.append("WHERE 1 = 1 ");
    	
    	if (tpSituacao != null) {
    		hql.append("AND md.tpSituacao = ? ");
    		criteria.add(tpSituacao);
    	}
    	
	    if (idDesconto != null) {
    		hql.append("OR md.id = ")
	    		.append("(SELECT d.motivoDesconto.id ")
		    	.append("FROM " + Desconto.class.getName() + " d ")
		    	.append("WHERE d.id = ?) " );
	    	criteria.add(idDesconto);
	    }
    	
	    hql.append("ORDER BY "+PropertyVarcharI18nProjection.createProjection("md.dsMotivoDesconto")+"");
    	
    	return getAdsmHibernateTemplate().find(hql.toString(), criteria.toArray());
    }
    
    
    public MotivoDesconto findByIntegracao(String cdMotivoDesconto) {
		StringBuffer sb = new StringBuffer()
		.append(" from MotivoDesconto as md ")
		.append("where md.cdMotivoDesconto = ? ");

		List<MotivoDesconto> result = getAdsmHibernateTemplate().find(sb.toString(), new Object[]{cdMotivoDesconto});
		
		return result.isEmpty() ? null : result.get(0);
	}
}