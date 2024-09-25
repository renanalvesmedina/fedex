package com.mercurio.lms.expedicao.model.dao;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.LocalizacaoAwbCiaAerea;
import com.mercurio.lms.expedicao.model.TrackingAwb;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class TrackingAwbDAO extends BaseCrudDao<TrackingAwb, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return TrackingAwb.class;
	}
	
	public Long store(TrackingAwb trackingAwb) {
		super.store(trackingAwb);
		return trackingAwb.getIdTrackingAwb();
	}

	public ResultSetPage<Map<String, Object>> findPaginated(Long idAwb) {
		SqlTemplate sql = generateHql(idAwb);
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(), 1, 1000, sql.getCriteria());
		List result = rsp.getList();
		result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
		rsp.setList(result);
		
		return rsp;
	}

	private SqlTemplate generateHql(Long idAwb) {
		StringBuilder hql = new StringBuilder()
		.append(" new map( ")
		.append(" lca.tpLocalizacaoCiaAerea as tpLocalizacao, ")
		.append(" lca.dsTracking as dsTracking, ")
		.append(" t.dhEvento as dhEvento ")
		.append(" ) ");
		
		StringBuilder hqlfrom = new StringBuilder()
		.append(getPersistentClass().getName()).append(" as t ")
		.append(" join t.localizacaoAwbCiaAerea lca ")
		.append(" join t.awb a ");

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(hqlfrom.toString());
	
		sql.addCriteria("a.idAwb", "=", idAwb);
		
		sql.addProjection(hql.toString());

		sql.addOrderBy("t.dhEvento", "desc");
		return sql;
	}

	public List<Map<String, Object>> findTrackingAwbByCiaAereaAndAwb(Long idAwb) {
		SqlTemplate sql = generateHql(idAwb);
    	
    	return (List<Map<String, Object>>) getAdsmHibernateTemplate().find(sql.toString(), idAwb);
	}
}
