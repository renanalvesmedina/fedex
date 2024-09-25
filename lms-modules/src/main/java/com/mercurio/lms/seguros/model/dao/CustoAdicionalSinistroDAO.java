package com.mercurio.lms.seguros.model.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Query;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.CustoAdicionalSinistro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CustoAdicionalSinistroDAO extends BaseCrudDao<CustoAdicionalSinistro, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CustoAdicionalSinistro.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("moeda", FetchMode.JOIN);
    }
    
    private SqlTemplate getFindPaginatedQuery(TypedFlatMap tfm) {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("new Map(cas.dsCustoAdicional", "dsCustoAdicional");
    	sql.addProjection("cas.idCustoAdicionalSinistro", "idCustoAdicionalSinistro");
    	sql.addProjection("cas.dtCustoAdicional",         "dtCustoAdicional");
    	sql.addProjection("cas.vlReembolsado",            "vlReembolsado");
    	sql.addProjection("cas.vlCustoAdicional",         "vlCustoAdicional");
    	sql.addProjection("cas.dtReembolsado",            "dtReembolsado");
    	sql.addProjection("cas.moeda.sgMoeda",            "sgMoeda01");
    	sql.addProjection("cas.moeda.dsSimbolo",          "dsSimbolo01");
    	sql.addProjection("cas.moeda.sgMoeda",            "sgMoeda02");
    	sql.addProjection("cas.moeda.dsSimbolo",          "dsSimbolo02)");
    	sql.addFrom(CustoAdicionalSinistro.class.getName(), "cas");
    	sql.addCriteria("cas.processoSinistro.id", "=", tfm.getLong("idProcessoSinistro"));
    	sql.addOrderBy("cas.dsCustoAdicional");
    	return sql;
    }
    
    public ResultSetPage findPaginatedCustom(FindDefinition fd, TypedFlatMap tfm) {
    	SqlTemplate sql = getFindPaginatedQuery(tfm);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), fd.getCurrentPage(), fd.getPageSize(), sql.getCriteria());
    }
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	SqlTemplate sql = getFindPaginatedQuery(tfm);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
    }
    
    public BigDecimal findSumVlCustoAdicionalByIdProcessoSinistro(Long idProcessoSinistro) {
    	Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("select sum(cas.vlCustoAdicional) from "+CustoAdicionalSinistro.class.getName()+" cas where cas.processoSinistro.id = ?");
 	    q.setParameter(0, idProcessoSinistro);
 	    return (BigDecimal)q.uniqueResult();
    }

    public BigDecimal findSumVlReembolsadoByIdProcessoSinistro(Long idProcessoSinistro) {    
    	Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("select sum(cas.vlReembolsado) from "+CustoAdicionalSinistro.class.getName()+" cas where cas.processoSinistro.id = ? and cas.vlReembolsado is not null");
    	q.setParameter(0, idProcessoSinistro);
    	return (BigDecimal) q.uniqueResult();
    }
   
    public List findByIdProcessoSinistro(Long idProcessoSinistro) {
    	return getAdsmHibernateTemplate().find("from "+CustoAdicionalSinistro.class.getName()+" cas where cas.processoSinistro.id = ?", idProcessoSinistro);
    }

    //LMS-6178
    public List findSomaValoresReembolso(Long idProcessoSinistro) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("SUM(CAS.vlReembolsado)");
		sql.addFrom(CustoAdicionalSinistro.class.getName(), "CAS");
		
		sql.addCriteria("CAS.processoSinistro.idProcessoSinistro", "=", idProcessoSinistro);
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}

	//LMS-6178
    public List findSomaValoresPrejuizo(Long idProcessoSinistro) {
    	SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("SUM(CAS.vlCustoAdicional)");
		sql.addFrom(CustoAdicionalSinistro.class.getName(), "CAS");
		
		sql.addCriteria("CAS.processoSinistro.idProcessoSinistro", "=", idProcessoSinistro);
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}

}