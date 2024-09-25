package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.EtiquetaAfericao;

public class EtiquetaAfericaoDAO extends BaseCrudDao<EtiquetaAfericao, Long>{

	@Override
	protected Class<EtiquetaAfericao> getPersistentClass() {
		return EtiquetaAfericao.class;
	}

	public ResultSetPage findPaginatedEtiquetaAfericao(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sqlTemplate = this.getSqlTemplateFindPaginatedEtiquetaAfericao(criteria);
		
		sqlTemplate.addProjection("new map (" +
				" etiquetaAfericao.id as idEtiquetaAfericao," +
				" filialOrigem.sgFilial as sgFilialOrigem," +
				" filialDestino.sgFilial as sgFilialDestino," +
				" rota.dsRota as rota," +
				" (etiquetaAfericao.nrComprimento||' X '||etiquetaAfericao.nrLargura||' X '||etiquetaAfericao.nrAltura) as dimensoes," +
				" etiquetaAfericao.psInformado as pesoInformado," +
				" etiquetaAfericao.nrCodigoBarras as nrCodBarras) " );
		
		sqlTemplate.addOrderBy("filialOrigem.sgFilial", "asc");
		sqlTemplate.addOrderBy("filialDestino.sgFilial", "asc");
		sqlTemplate.addOrderBy("rota.dsRota", "asc");
		
		return getAdsmHibernateTemplate().findPaginated(sqlTemplate.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sqlTemplate.getCriteria());
	}
	
	private SqlTemplate getSqlTemplateFindPaginatedEtiquetaAfericao(TypedFlatMap criteria){
		StringBuffer from = new StringBuffer();
		
		/**
		 * from
		 */
		from.append(EtiquetaAfericao.class.getName() + " as etiquetaAfericao ");
		from.append(" join etiquetaAfericao.filialOrigem as filialOrigem ");
		from.append(" join etiquetaAfericao.filialDestino as filialDestino ");
		from.append(" join etiquetaAfericao.rotaColetaEntrega as rota ");
		
		SqlTemplate sqlTemplate = new SqlTemplate();
		sqlTemplate.addFrom( from.toString() );
		
		
		/**
		 * where
		 */
		if(criteria.getLong("idFilialOrigem") != null ){
			sqlTemplate.addCriteria("etiquetaAfericao.filialOrigem.id", "=", criteria.getLong("idFilialOrigem"));			
		}
		
		if(criteria.getLong("idFilialDestino") != null ){
			sqlTemplate.addCriteria("etiquetaAfericao.filialDestino.id", "=", criteria.getLong("idFilialDestino"));
		}
		
		if( StringUtils.isNotBlank(criteria.getString("nrCodigoBarras")) ){
			sqlTemplate.addCriteria("etiquetaAfericao.nrCodigoBarras", "like", criteria.getString("nrCodigoBarras"));
		}
		
		return sqlTemplate;
	}

	public Integer getRowCountEtiquetaAfericao(TypedFlatMap criteria) {
		SqlTemplate sql = this.getSqlTemplateFindPaginatedEtiquetaAfericao(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}
	
	protected void initFindByIdLazyProperties(Map arg0) {
		arg0.put("filialOrigem", FetchMode.JOIN);
		arg0.put("filialOrigem.pessoa", FetchMode.JOIN);
		arg0.put("filialDestino", FetchMode.JOIN);
		arg0.put("filialDestino.pessoa", FetchMode.JOIN);
		arg0.put("rotaColetaEntrega", FetchMode.JOIN);
	}

	public boolean validateCodigoBarras(String barcode) {
		Criteria c = getSession().createCriteria(persistentClass);
		c.add(Restrictions.eq("nrCodigoBarras", barcode));
		return c.uniqueResult() != null;
	}

	public EtiquetaAfericao findByNrCodBarra(String nrCodBarra) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ea");
		hql.append("   FROM ").append(EtiquetaAfericao.class.getName()).append(" ea ");
		hql.append("  WHERE nrCodigoBarras = ?");
		
		List<Object> params = new ArrayList<Object>();
		params.add(nrCodBarra);
		
		return (EtiquetaAfericao) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
	}
	
}
