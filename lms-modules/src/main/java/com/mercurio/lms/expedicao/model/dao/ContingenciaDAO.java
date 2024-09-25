package com.mercurio.lms.expedicao.model.dao;



import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Contingencia;
import com.mercurio.lms.util.IntegerUtils;

public class ContingenciaDAO extends BaseCrudDao<Contingencia, Long>{

	@Override
	protected Class getPersistentClass() {
		return Contingencia.class;
	}
	
	
    public Integer getRowCountContingencia(TypedFlatMap criteria) {
    	SqlTemplate sql = this.getSqlTemplateFindPaginatedContingencia(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
    }
	
	
	public ResultSetPage<Contingencia> findPaginatedContingencia(TypedFlatMap criteria, FindDefinition findDef){
		SqlTemplate sqlTemplate = this.getSqlTemplateFindPaginatedContingencia(criteria);
		
		sqlTemplate.addProjection("new map (" +
				" contingencia.id as idContingencia," +
				" contingencia.dsContingencia as dsContingencia," +
				" contingencia.tpContingencia as tpContingencia," +
				" contingencia.tpSituacao as tpSituacao," +
				" contingencia.dhSolicitacao as dhSolicitacao," +
				" contingencia.dhAprovacao as dhAprovacao," +
				" contingencia.dhFinalizacao as dhFinalizacao," +
				" filial.id as idFilial, " +
				" filial.sgFilial as sgFilial ) " );
		
		sqlTemplate.addOrderBy("filial.sgFilial", "asc");
		sqlTemplate.addOrderBy("contingencia.dhSolicitacao", "desc");
		
		return getAdsmHibernateTemplate().findPaginated(sqlTemplate.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sqlTemplate.getCriteria());
	}
	
	private SqlTemplate getSqlTemplateFindPaginatedContingencia(TypedFlatMap criteria){
		StringBuffer from = new StringBuffer();
		
		/**
		 * from
		 */
		from.append(Contingencia.class.getName() + " as contingencia ")
			.append("inner join contingencia.filial as filial ");
		
		SqlTemplate sqlTemplate = new SqlTemplate();
		sqlTemplate.addFrom( from.toString() );
		
		
		/**
		 * where
		 */
		sqlTemplate.addCriteria("filial.id", "=", criteria.getLong("idFilial"));
		
		if( StringUtils.isNotBlank(criteria.getString("tpSituacao")) ){
			sqlTemplate.addCriteria("contingencia.tpSituacao", "=", criteria.getString("tpSituacao"));
		}
		
		if( StringUtils.isNotBlank(criteria.getString("tpContingencia")) ){
			sqlTemplate.addCriteria("contingencia.tpContingencia", "=", criteria.getString("tpContingencia"));
		}
		
		return sqlTemplate;
	}
	
	
	/**
	 * retornar ContingenciaCte não finalizada (tpSituacao = "S" ou "A")
	 * @param idFilial
	 * @return ContingenciaCte
	 */
	public Contingencia findNaoFinalizadaByIdFilialAndTpContingencia(Long idFilial, String tpContingencia){
		SqlTemplate hql = new SqlTemplate();	
		
   		hql.addFrom(getPersistentClass().getName() + " contingencia ");
   		hql.addCriteria("contingencia.filial.id", "=", idFilial);
   		hql.addCriteria("contingencia.tpContingencia", "=", tpContingencia);
   		hql.addCustomCriteria("contingencia.tpSituacao in ('S', 'A') ");
   		
   		return  (Contingencia)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
	

	
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("usuarioSolicitante", FetchMode.JOIN);	
		lazyFindById.put("usuarioSolicitante.usuarioADSM", FetchMode.JOIN);
		
		lazyFindById.put("usuarioAprovador", FetchMode.JOIN);	
		lazyFindById.put("usuarioAprovador.usuarioADSM", FetchMode.JOIN);
		
		lazyFindById.put("usuarioFinalizador", FetchMode.JOIN);	
		lazyFindById.put("usuarioFinalizador.usuarioADSM", FetchMode.JOIN);

	}

	
	public Contingencia findByFilial(Long idFilial, String tpSituacao) {
		return findByFilial(idFilial, tpSituacao, null);
	}

	public Contingencia findByFilial(Long idFilial, String tpSituacao, String tpContingencia) {
		SqlTemplate hql = new SqlTemplate();

   		hql.addFrom(getPersistentClass().getName() + " contingencia ");
   		hql.addCriteria("contingencia.filial.id", "=", idFilial);
   		hql.addCriteria("contingencia.tpSituacao", "=", tpSituacao);

   		if (tpContingencia != null) {
   			hql.addCriteria("contingencia.tpContingencia", "=", tpContingencia);
   		}

		return (Contingencia)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	public Boolean hasContingenciaValidaByFilial(Long idFilial) {
		if(idFilial == null){
			return null;
		}

		StringBuffer sql = new StringBuffer()
		.append("select cont from " +  Contingencia.class.getName() + " as cont")
		.append(" WHERE cont.filial.id = ? and")
		.append(" cont.tpSituacao = 'A' and")
		.append(" cont.tpContingencia = 'P'");

		Integer num =  getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[] { idFilial });

		return (num > 0);
	}

	public Boolean hasContingenciaMdfeValida(Long idFilial) {
		if(idFilial == null){
			return null;
		}
		
		
		StringBuffer sql = new StringBuffer()
		.append("select cont from " +  Contingencia.class.getName() + " as cont")
		.append(" WHERE (cont.filial.id = ? ) ")
		.append(" and cont.tpSituacao = 'A' ")
		.append(" and cont.tpContingencia = 'M'");
		
		Integer num =  getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[] { idFilial });
		
		return (num > 0);
	}

	public Boolean hasContingenciaMdfeValida(Long idFilial, Long idEmpresaMercurio) {
		if(idFilial == null){
			return null;
		}
		
		
		StringBuffer sql = new StringBuffer()
		.append("select cont from " +  Contingencia.class.getName() + " as cont")
		.append(" WHERE (cont.filial.id = ? or cont.filial.id = ? ) ")
		.append(" and cont.tpSituacao = 'A' ")
		.append(" and cont.tpContingencia = 'M'");
		
		Integer num =  getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[] { idFilial, idEmpresaMercurio });
		
		return (num > 0);
	}
	
	/**
	 * Incrementa um no número de emissões em contingência
	 * 
	 * @param Contingencia
	 */
	public void updateQtUtilizacoes(Contingencia contingencia) {
		
		final Long idContingencia = contingencia.getIdContingencia();
		final Integer qtEmissoes = contingencia.getQtEmissoes();
		
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String hql = "UPDATE " + getPersistentClass().getName() + " co \n" +
							 	" SET co.qtEmissoes = :qtEmissoes \n" +
								" WHERE co.idContingencia = :idContingencia "; 
				query = session.createQuery(hql);
				query.setInteger("qtEmissoes", IntegerUtils.incrementValue(qtEmissoes));
				query.setLong("idContingencia", idContingencia);
				query.executeUpdate();
				return null;
			}
		});
	}

}
