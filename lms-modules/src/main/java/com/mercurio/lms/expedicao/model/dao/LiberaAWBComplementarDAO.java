package com.mercurio.lms.expedicao.model.dao;

import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureRestrictionsBuilder;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.LiberaAWBComplementar;
import com.mercurio.lms.expedicao.model.VolumeSobraFilial;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;

public class LiberaAWBComplementarDAO extends BaseCrudDao<LiberaAWBComplementar, Long>{

	@Override
	@SuppressWarnings("rawtypes") 
	protected Class getPersistentClass() {
		return LiberaAWBComplementar.class;
	}

	@Override
	@SuppressWarnings("rawtypes") 
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = montaSqlPaginated( criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}
	
	@Override
	public Integer getRowCount(Map<String, Object> criteria,
			FindDefinition def, RestrictionsBuilder rb,
			ConfigureRestrictionsBuilder crb) {
		SqlTemplate sql = montaSqlPaginated(criteria);
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;
	}
	
	@SuppressWarnings("rawtypes") 
	private SqlTemplate montaSqlPaginated(Map criteria) {
		SqlTemplate sqlTemplate = new SqlTemplate();
		StringBuffer sql = new StringBuffer()
		.append("new Map( ") 
		.append("		lac.filialSolicitante.sgFilial as sgFilial, ")
		.append("		lac.empresa.sgEmpresa as sgEmpresa, ")
		.append("		lac.usuarioSolicitante.usuarioADSM.nmUsuario as funcionarioSolicitante, ")
		.append("		lac.dhLiberacao as dhLiberacao, ")
		.append("		lac.dsSenha as cdLiberacao, ")
		.append("		lac.idLiberaAWBComplementar as idLiberaAWBComplementar, ")
		.append("		concat(concat(concat(concat(lac.empresa.sgEmpresa, concat(' ', concat(lac.awbOriginal.dsSerie, '.'))), lac.awbOriginal.nrAwb), '-'), lac.awbOriginal.dvAwb) as nrAwbFormatado")
		.append("	) ");
		
		sqlTemplate.addProjection(sql.toString());
		sqlTemplate.addFrom(LiberaAWBComplementar.class.getName(), "lac");
		
		sqlTemplate.addCriteria("lac.filialSolicitante.idFilial", "=", criteria.get("idFilial"));
		if (criteria.get("idEmpresa") != null) {
			sqlTemplate.addCriteria("lac.empresa.idEmpresa", "=", criteria.get("idEmpresa"));
		}
		
		if (criteria.get("dtInicial") != null) {
			sqlTemplate.addCriteria("TRUNC(lac.dhLiberacao.value)", ">=", ((YearMonthDay) criteria.get("dtInicial")));
		}
		
		if (criteria.get("dtFinal") != null) {
			sqlTemplate.addCriteria("TRUNC(lac.dhLiberacao.value)", "<=", ((YearMonthDay) criteria.get("dtFinal")));
		}
		
		if (criteria.get("cdLiberacao") != null) {
			sqlTemplate.addCriteria("lac.dsSenha", "=", criteria.get("cdLiberacao").toString());
		}
		
		sqlTemplate.addOrderBy("lac.filialSolicitante.sgFilial");
		sqlTemplate.addOrderBy("lac.empresa.pessoa.nmPessoa");
		sqlTemplate.addOrderBy("lac.usuarioSolicitante.usuarioADSM.nmUsuario");
		sqlTemplate.addOrderBy("lac.dhLiberacao");
		
		return sqlTemplate;
	}

	public LiberaAWBComplementar findByIdAwbComplementado(Long idAwb) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT lac ")
		   .append(" FROM " + LiberaAWBComplementar.class.getSimpleName() + " lac ")
		   .append(" JOIN lac.awbOriginal ao ")
		   .append(" WHERE ")
		   .append(" ao.idAwb = ? ");
		
		return (LiberaAWBComplementar) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idAwb});
	}
	
	public LiberaAWBComplementar findByIdAwbComplementar(Long idAwb) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT lac ")
		   .append(" FROM " + LiberaAWBComplementar.class.getSimpleName() + " lac ")
		   .append(" JOIN lac.awbComplementar ac ")
		   .append(" WHERE ")
		   .append(" ac.idAwb = ? ");
		
		return (LiberaAWBComplementar) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idAwb});
	}
}
