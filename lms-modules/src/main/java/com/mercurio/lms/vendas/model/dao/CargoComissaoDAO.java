package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.CargoComissao;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;

public class CargoComissaoDAO extends BaseCrudDao<CargoComissao, Long> {
	
	private static final Long FILIAL_MTZ = 361L;

	@Override
	protected final Class getPersistentClass() {
		return CargoComissao.class;
	}

	@Override
	public CargoComissao findById(Long id) {
		return findByIdByCriteria("idCargoComissao", id);
	}

	public CargoComissao findAtivoByIdUsuario(Long idUsuario) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Restrictions.eq("usuario.idUsuario", idUsuario));
		criterionList.add(Restrictions.eq("tpSituacao", DmStatusEnum.ATIVO.getValue()).ignoreCase());
		
		return findUniqueResultByCriteria(criterionList); 
	}

	@Override
	protected Criteria createCriteria() {
		Criteria criteria = super.createCriteria();
		criteria.createAlias("usuario", "usuario");
		criteria.createAlias("usuario.usuarioADSM", "usuarioADSM");
		
		criteria.addOrder(Order.asc("usuarioADSM.nmUsuario"));
		return criteria;
	}


	private Query createCriterions(Map<String, Object> criteria) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		Long idRegional = (Long) criteria.get("idRegional");
		if (idRegional  != null) {
			sql.append(" and re.id_regional = :idRegional ");
			params.put("idRegional", idRegional);
		}

		Long idFilial = (Long) criteria.get("idFilial");
		if (idFilial  != null) {
			sql.append(" and  fi.id_filial  = :idFilial ");
			params.put("idFilial", idFilial);
		}
		
		Long idUsuario = (Long) criteria.get("idUsuario");
		if (idUsuario  != null) {
			sql.append(" and cc.id_usuario = :idUsuario ");
			params.put("idUsuario", idUsuario);
		}
		
		DomainValue tpCargo = (DomainValue) criteria.get("tpCargo");
		if (tpCargo != null) {
			sql.append(" and  cc.tp_cargo = :tpCargo ");
			params.put("tpCargo", tpCargo.getValue());
		}

		String situacao = (String) criteria.get("situacao");
		if( DmStatusEnum.ATIVO.getValue().equalsIgnoreCase(situacao)){
			sql.append(" and  cc.dt_desligamento is NULL ");
		}else if( DmStatusEnum.INATIVO.getValue().equalsIgnoreCase(situacao)){
			sql.append(" and  cc.dt_desligamento <= TRUNC(sysdate) ");
		}
		return new Query(sql.toString(),params);
	}
	

	public List<Map<String, Object>> findToMappedResult(Map<String, Object> criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append(getProjectionSql());
		sql.append(getFromSql());
		
		Query criterions = createCriterions(criteria);
		sql.append(criterions.getSql());
		sql.append(" order by re.sg_regional, fi.sg_filial, u.nm_usuario, dtAdmissao ");
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id", Hibernate.STRING);
				sqlQuery.addScalar("nrMatricula", Hibernate.STRING);
				sqlQuery.addScalar("nmFuncionario", Hibernate.STRING);
				sqlQuery.addScalar("nrCpf", Hibernate.STRING);
				sqlQuery.addScalar("tpCargo", Hibernate.STRING);
				sqlQuery.addScalar("regional", Hibernate.STRING);
				sqlQuery.addScalar("filial", Hibernate.STRING);
				sqlQuery.addScalar("dtInclusaoComissao", Hibernate.STRING);
				sqlQuery.addScalar("dtAdmissao", Hibernate.STRING);
				sqlQuery.addScalar("dtDemissao", Hibernate.STRING);
				sqlQuery.addScalar("dtDesligamento", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
			}
		};
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), criterions.getParams(), csq);
	}
	

	public Integer findCount(Map<String, Object> criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) nrCount ");
		sql.append(getFromSqlCount());
		
		Query criterions = createCriterions(criteria);
		sql.append(criterions.getSql());
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("nrCount", Hibernate.INTEGER);
			}
		};
		
		List<Map<String, Object>> mappedResult = getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), criterions.getParams(), csq);
		return getCount(mappedResult);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findComissionadoSuggest(final String nrIdentificacao, final String nmPessoa,
			final String tpPessoa, List<String> notINrIdentificacao) {

		StringBuffer sb = new StringBuffer(" select cc.ID_USUARIO as idUsuario, vco.NR_MATRICULA as nrMatricula, '' as login, vco.NM_FUNCIONARIO as nmUsuario")
		.append(" from Cargo_Comissao cc, V_FUNCIONARIO vco ")
		.append(" where ")
		.append("	cc.ID_USUARIO = vco.ID_USUARIO ");
		
		if (nrIdentificacao != null && nrIdentificacao.length() > 0) {
			sb.append("	and lower(VCO.NR_MATRICULA) like '").append(nrIdentificacao.toLowerCase()).append("'");
		}

		if (nmPessoa != null && nmPessoa.length() > 0) {
			sb.append("	and lower(VCO.NM_FUNCIONARIO) like '%").append(nmPessoa.toLowerCase()).append("%'");
		}

		if (notINrIdentificacao != null && notINrIdentificacao.size() > 0) {
			sb.append("	and VCO.NR_MATRICULA not in (");
			StringBuilder strNotINrIdentificacao = new StringBuilder("");
			for (String nrIdentificacaoIn : notINrIdentificacao) {
				strNotINrIdentificacao.append(",'").append(nrIdentificacaoIn).append("'");
			}

			sb.append(strNotINrIdentificacao.toString().replaceFirst(",", ""));
			sb.append(" ) ");
		}

		Map<String, Object> criterions = new HashMap<String, Object>();
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idUsuario", Hibernate.LONG);
				sqlQuery.addScalar("nrMatricula", Hibernate.STRING);
				sqlQuery.addScalar("login", Hibernate.STRING);
				sqlQuery.addScalar("nmUsuario", Hibernate.STRING);
			}
		};

		return getAdsmHibernateTemplate().findBySqlToMappedResult(sb.toString(), criterions, csq);
	}
	
	private Integer getCount(List<Map<String, Object>> mappedResult) {
		if(mappedResult != null && !mappedResult.isEmpty() && mappedResult.get(0) != null && 
				mappedResult.get(0).get("nrCount") != null){
			return (Integer) mappedResult.get(0).get("nrCount");
		}
		return 0;
	}

	private String getFromSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("  from cargo_comissao cc, v_comissionamento vc, filial fi, regional re, usuario u, regional_filial rf ");
		sql.append(" where vc.id_usuario = cc.ID_USUARIO ");
		sql.append("   and fi.SG_FILIAL = vc.sg_filial ");
		sql.append("   and fi.id_empresa = " + FILIAL_MTZ);
		sql.append("   and rf.ID_FILIAL = fi.id_filial ");
		sql.append("   and rf.DT_VIGENCIA_INICIAL <= trunc(sysdate) ");
		sql.append("   and rf.DT_VIGENCIA_FINAL >= trunc(sysdate) ");
		sql.append("   and re.ID_REGIONAL = rf.ID_REGIONAL ");
		sql.append("   and u.ID_USUARIO = cc.ID_USUARIO ");
		return sql.toString();
	}
	
	private String getFromSqlCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" from cargo_comissao cc ");
		sql.append(" left join executivo_territorio et on (cc.id_usuario = et.id_usuario) ");
		sql.append(" left join territorio t on (et.id_territorio = t.id_territorio) ");
		sql.append(" left join usuario u on (cc.id_usuario = u.id_usuario) ");
		sql.append(" left join regional re on (re.id_regional = t.id_regional) ");
		sql.append(" left join filial fi on (fi.id_filial = t.id_filial) ");
		sql.append("  where 1=1 ");
		return sql.toString();
	}

	private String getProjectionSql() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append("  cc.id_cargo_comissao id, ");
		sql.append("   u.nr_matricula nrMatricula, ");
		sql.append("   u.nm_usuario nmFuncionario, ");
		sql.append("   cc.nr_cpf nrCpf, ");
		sql.append("   cc.tp_cargo tpCargo, ");
		sql.append("   re.ds_regional regional, ");
		sql.append("   fi.sg_filial filial, ");
		sql.append("    to_char(cc.dh_inclusao, 'DD/MM/YYYY') dtInclusaoComissao, ");
		sql.append("    to_char(vc.dt_admissao, 'DD/MM/YYYY') dtAdmissao, ");
		sql.append("    to_char(vc.dt_demis, 'DD/MM/YYYY') dtDemissao, ");
		sql.append("    dt_desligamento dtDesligamento ");
		
		return sql.toString();
	}
	
	public Boolean findCargoComissaoByCpf(String nrCpf) {
		SqlTemplate template = new SqlTemplate();
		template.addFrom("CargoComissao");
		template.addCriteria("nrCpf", "=", nrCpf);
		template.addOrderBy("idCargoComissao");

		List<CargoComissao> result = getAdsmHibernateTemplate().find(template.getSql(), template.getCriteria());
		if (result.size() > 0) {
			return true;
		}

		return false;
	}
	
	static class Query{
		private Map<String, Object> params;
		private String sql;
		public Query(String sql, Map<String, Object> params) {
			super();
			this.params = params;
			this.sql = sql;
		}
		public Map<String, Object> getParams() {
			return params;
		}
		public String getSql() {
			return sql;
		}
	}
}
