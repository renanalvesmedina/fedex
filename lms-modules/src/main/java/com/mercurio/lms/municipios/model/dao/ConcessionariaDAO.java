package com.mercurio.lms.municipios.model.dao;


import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.Concessionaria;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.    
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ConcessionariaDAO extends BaseCrudDao<Concessionaria, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Concessionaria.class;
    }

    /* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindPaginatedLazyProperties(java.util.Map)
	 */
	protected void initFindPaginatedLazyProperties(Map FetchModes) {
		FetchModes.put("pessoa",FetchMode.JOIN);
	}
	
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	protected void initFindByIdLazyProperties(Map FetchModes) {
		FetchModes.put("pessoa",FetchMode.JOIN);
		FetchModes.put("inscricaoEstadual",FetchMode.JOIN);
    	FetchModes.put("inscricaoEstadual.unidadeFederativa",FetchMode.JOIN);
		
	}

	/**
     * Verifica a existencia da especialização com mesmo Numero e Tipo de Identificacao, exceto a mesma.
     * @param map
     * @return a existência de uma especialização
     */
    public boolean verificaExistenciaEspecializacao(Concessionaria concessionaria){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.createAlias("pessoa", "pessoa_");
    	dc.add(Restrictions.eq("pessoa_.nrIdentificacao", concessionaria.getPessoa().getNrIdentificacao()));
    	dc.add(Restrictions.eq("pessoa_.tpIdentificacao", concessionaria.getPessoa().getTpIdentificacao().getValue()));
    	dc.setProjection(Projections.rowCount());
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(dc);
    	return (result.intValue() > 0);	
    }
    
    public List findLookupConcessionaria(String nrIdentificacao) {
    	ProjectionList pl = Projections.projectionList()
		    .add(Projections.alias(Projections.property("P.nmPessoa"),"pessoa_nmPessoa"))
		  	.add(Projections.alias(Projections.property("P.nrIdentificacao"),"pessoa_nrIdentificacao"))
		  	.add(Projections.alias(Projections.property("P.tpIdentificacao"),"pessoa_tpIdentificacao"))
		  	.add(Projections.alias(Projections.property("idConcessionaria"),"idConcessionaria"));
		DetachedCriteria dc = createDetachedCriteria()
				   .setProjection(pl)
				   .createAlias("pessoa","P")
				   .add(Restrictions.ilike("P.nrIdentificacao",nrIdentificacao))
				   .setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
	/**
	 * Retorna 'true' se a pessoa informada é uma concessionaria ativa senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isConcessionaria(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("count(con.id)");
		
		hql.addInnerJoin(Concessionaria.class.getName(), "con");
		
		hql.addCriteria("con.id", "=", idPessoa);
		hql.addCriteria("con.tpSituacao", "=", "A");
		
		List lstConcessionaria = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (((Long)lstConcessionaria.get(0)) > 0){
			return true;
		} else {
			return false;
		}
	}	    
	
}