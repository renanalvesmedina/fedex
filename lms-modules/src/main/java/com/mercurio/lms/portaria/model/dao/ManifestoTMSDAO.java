package com.mercurio.lms.portaria.model.dao;


import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.portaria.model.ManifestoTMS;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ManifestoTMSDAO extends BaseCrudDao<ManifestoTMS, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ManifestoTMS.class;
	}

	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = getSqlFindPaginated(criteria);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria()); 
		return rsp;
	}

	public Integer getRowCount(Map criteria) {
		SqlTemplate sql = getSqlFindPaginated(criteria);
		List list = getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
		if( list != null ){
			return list.size();
		}
		return 0;
	}

	private SqlTemplate getSqlFindPaginated(Map criteria) {
		Map filial = MapUtils.getMap(criteria, "filial");
		
		StringBuffer projection = new StringBuffer();
		projection.append("new map(");
		projection.append("max(m.idManifestoTMS) as idManifestoTMS,");
		projection.append("m.dsVeiculo as dsVeiculo,");
		projection.append("m.nrManifesto as nrManifesto,");
		projection.append("max(m.nrQtdCtos) as nrQtdCtos,");
		projection.append("fm.sgFilial as sgFilial");
		projection.append(")");

		StringBuffer from = new StringBuffer();
		from.append(getPersistentClass().getName()+ " as m ");
		from.append(" join m.filialManifesto as fm ");
		

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projection.toString());		
		sql.addFrom(from.toString());		

		sql.addCriteria("m.filial.id","=",MapUtils.getLong(filial, "idFilial"));
		sql.addCustomCriteria("m.dhChegada is null");

		sql.addGroupBy("fm.sgFilial");
		sql.addGroupBy("m.nrManifesto");
		sql.addGroupBy("m.dsVeiculo");

		return sql;
	} 
	
	public Integer findMaxQtdCtosManifestoTMS(Long idFilial, Long nrManifesto) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"m")
		.add(Restrictions.eq("m.filialManifesto.id", idFilial))
		.add(Restrictions.eq("nrManifesto", nrManifesto))
		.setProjection(Projections.max("m.nrQtdCtos"));
		
		List list = getAdsmHibernateTemplate().findByCriteria(dc);
		if( list != null && list.size() > 0 ){
			if( list.get(0) != null ){
				return ((Integer) list.get(0)).intValue();
			}
		}
		return 0;
	}
	
	public Integer findQtdRegistrosManifestoTMS(Long idFilial, Long nrManifesto) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"m")
		.add(Restrictions.eq("m.filialManifesto.id", idFilial))
		.add(Restrictions.eq("nrManifesto", nrManifesto))
		.setProjection(Projections.count("m.idManifestoTMS"));
		
		List list = getAdsmHibernateTemplate().findByCriteria(dc);
		if( list.size() > 0 ){
			return ((Integer) list.get(0)).intValue();
		}
		return 0;
	}

	public List<ManifestoTMS> findManifestosTMS(Long idFilial, Long nrManifesto) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"m")
		.add(Restrictions.eq("m.filialManifesto.id", idFilial))
		.add(Restrictions.eq("nrManifesto", nrManifesto));
		
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}

	public ManifestoTMS findManifestoTMSByDoctoServico(Long idDoctoServico){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"m")
		.add(Restrictions.eq("m.doctoServico.id", idDoctoServico))
		.add(Restrictions.isNotNull("m.dhChegada"))
		.addOrder(Order.asc("nrQtdCtos"));
		List list = getAdsmHibernateTemplate().findByCriteria(dc);
		if( list.size() > 0 ){
			return (ManifestoTMS)list.get(0);
		}
		return null;
	}
	
}