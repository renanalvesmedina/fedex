package com.mercurio.lms.contasreceber.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.ComposicaoPagamentoRedeco;
import com.mercurio.lms.contasreceber.model.CreditoBancarioEntity;
import com.mercurio.lms.contasreceber.model.Redeco;
import com.mercurio.lms.util.session.SessionUtils;

public class CreditoBancarioDAO extends BaseCrudDao<CreditoBancarioEntity, Long> {

	@Override
	public CreditoBancarioEntity findById(Long id) {
		Criteria criteria = createCriteria();
		
		criteria.createAlias("usuario", "usuario");
		criteria.createAlias("usuario.usuarioADSM", "usuarioADSM");
		
		return findByIdByCriteria(criteria, "idCreditoBancario", id);
	}

	public ResultSetPage findPaginated(Map map, Integer pageNumber, Integer pageSize) {
		SqlTemplate sql = getCreditosFilterHqlQuery(map);
		
		return getCreditosSomaFilterSqlQuery(map, pageNumber, pageSize);

	}

	public ResultSetPage findPaginatedAba(Map map, Integer pageNumber, Integer pageSize) {
		return getCreditosSomaFilterSqlQuery(map, pageNumber, pageSize);

	}

	private ResultSetPage convertResultComSaldoPraEntity(ResultSetPage result) {
		List<CreditoBancarioEntity> list = new ArrayList<CreditoBancarioEntity>();
		for(Object item : result.getList()){
			Object[] array = (Object[])item;
			CreditoBancarioEntity creditoEntity = transformToEntityUnique(array);
			list.add(creditoEntity);
		}
		result.setList(list);
		return result;
	}

	public List<CreditoBancarioEntity> findComTotal(Map map) {
		return filterSaldoValido(getCreditosSomaFilterSqlQuery(map), map);
		
	}

	private SqlTemplate getCreditosFilterHqlQueryCount(Map map) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" count(*) ");

		StringBuffer froms = new StringBuffer();
		froms.append(" ").append( CreditoBancarioEntity.class.getName()).append(" as cb ");
		hql.addFrom(froms.toString());

		hql = adicionaCriteriaFiltros(hql, map);
		return hql;
	}
	
	private SqlTemplate getCreditosFilterHqlQuery(Map map) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("cb");

		StringBuffer select = getSelectTotal();
		hql.addProjection(select.toString());

		StringBuffer froms = new StringBuffer();
		froms.append(" ").append(CreditoBancarioEntity.class.getName()).append(" as cb ");
		hql.addFrom(froms.toString());

		hql = adicionaCriteriaFiltros(hql, map);

		return hql;
	}

	public List<CreditoBancarioEntity> getCreditosSomaFilterSqlQuery(Map map) {
		String sqlQuery = getSQL(map);
		ConfigureSqlQuery csq = projecao();
		int maxRowsReturned = 20000;
		ResultSetPage rsp = convertResultComSaldoPraEntity(getAdsmHibernateTemplate().findPaginatedBySql(sqlQuery, 1, maxRowsReturned, new Object[] {}, csq));

		return rsp.getList();
		
	}
	
	private ResultSetPage getCreditosSomaFilterSqlQuery(final Map map, Integer pageNumber, Integer pageSize) {
		
		return convertResultComSaldoPraEntity(getAdsmHibernateTemplate()
				.findPaginatedBySql(getSQL(map), pageNumber, pageSize, new Object[] {}, projecao()));
				
	}
	
	private String getSQL(Map map) { 
		String sqlQuery = 
				  " SELECT sum(vl_credito - vlUtilizado) OVER () AS soma,	" 
				+ "		creditos.*														"
				+ " FROM (																"
				+ " SELECT 														 		"
				+ "		(credb.vl_credito - vlUtilizado) as sld,						"
				+ "		credb.* 														"
				+ "		FROM (															"
				+ "			SELECT cb.*, nvl(											"
				+ "				(SELECT sum(comp.VL_PAGAMENTO)							"
				+ "				FROM COMPOSICAO_PAGAMENTO_REDECO comp, REDECO rd		"
				+ "				WHERE comp.ID_CREDITO_BANCARIO = cb.ID_CREDITO_BANCARIO	"
				+ "					AND comp.ID_REDECO = rd.ID_REDECO					"
				+ "					AND rd.TP_SITUACAO_REDECO <> 'CA'					"
				+ "				), 0) AS vlUtilizado									"
				+ "		FROM CREDITO_BANCARIO cb										"
				+ "		WHERE 															"
				+ 			adicionaCriteriaSQLFiltros(map)
				+ "		) credb															"
				+ "		ORDER BY sld													"
				+ "	) creditos															"
				+ 		adicionaCriteriaSqlSaldoFiltro(map);

		return sqlQuery;
		
	}
	
	private ConfigureSqlQuery projecao() {
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery query) {
				query.addEntity(CreditoBancarioEntity.class)
					.addScalar("SOMA", Hibernate.BIG_DECIMAL)
					.addScalar("VLUTILIZADO", Hibernate.BIG_DECIMAL);
			}
		};

		return csq;
		
	}

	private StringBuffer getSelectTotal() {
		StringBuffer select = new StringBuffer();
		select.append("(select COALESCE(sum(cpr.vlPagamento), 0)  from " + ComposicaoPagamentoRedeco.class.getName() + " as cpr, ").
		append(Redeco.class.getName()).append(" as re ").
		append(" where	cpr.creditoBancario.idCreditoBancario = cb.idCreditoBancario ").
		append(" AND cpr.redeco.idRedeco = re.idRedeco ").
		append(" AND re.tpSituacaoRedeco != 'CA' ").append(" )");
		return select;
	}
	
	private List<CreditoBancarioEntity> filterSaldoValido(
			List<CreditoBancarioEntity> transformToEntityList, Map criteriaMap) {

		List<CreditoBancarioEntity> filterList = new ArrayList<CreditoBancarioEntity>();
		
		for(CreditoBancarioEntity entity : transformToEntityList){
			if(isValidSaldo(entity, criteriaMap)){
				filterList.add(entity);
			}
		}

		return filterList;
	}

	private boolean isValidSaldo(CreditoBancarioEntity entity, Map criteriaMap) {

		BigDecimal vlSaldoInicial = (BigDecimal) criteriaMap.get("vlSaldoInicial");
		if (null != vlSaldoInicial && entity.getSaldo().compareTo(vlSaldoInicial) < 0) {
			return false;
		}
		
		BigDecimal vlSaldoFinal = (BigDecimal) criteriaMap.get("vlSaldoFinal");
		if (vlSaldoFinal != null && entity.getSaldo().compareTo(vlSaldoFinal) > 0) {
			return false;
		}

		return true;
	}

	private List<CreditoBancarioEntity> transformToEntity(
			List<Object[]> creditobancarioList) {
		List<CreditoBancarioEntity> creditoEntities = new ArrayList<CreditoBancarioEntity>();
		
		for(Object[] credito : creditobancarioList){
			CreditoBancarioEntity creditoEntity = transformToEntityUnique(credito);
			
			creditoEntities.add(creditoEntity);
		}
		return creditoEntities;
	}

	private SqlTemplate adicionaCriteriaFiltros(SqlTemplate hql, Map criteriaMap) {

		Long idFilial = (Long) criteriaMap.get("idFilial");
		if (idFilial != null) {
			hql.addCriteria("cb.filial.idFilial","=",idFilial);
		}
		
		Long idBanco = (Long) criteriaMap.get("idBanco");
		if (idBanco != null) {
			hql.addCriteria("cb.banco.idBanco","=",idBanco);
		}
		
		DomainValue tpModalidade = (DomainValue) criteriaMap.get("tpModalidade");
		if (tpModalidade != null) {
			hql.addCriteria("tpModalidade","=",tpModalidade);
		}
		
		DomainValue tpOrigem = (DomainValue) criteriaMap.get("tpOrigem");
		if (tpOrigem != null) {
			hql.addCriteria("tpOrigem","=",tpOrigem);
		}
		
		String dsCpfCnpj = (String) criteriaMap.get("dsCpfCnpj");
		if (dsCpfCnpj != null && !dsCpfCnpj.isEmpty()) {
			hql.addCriteria("dsCpfCnpj","like",createLikeCondition(dsCpfCnpj));
		}
		
		String dsNomeRazaoSocial = (String) criteriaMap.get("dsNomeRazaoSocial");
		if (dsNomeRazaoSocial != null && !dsNomeRazaoSocial.isEmpty()) {
			hql.addCriteria("upper(dsNomeRazaoSocial)","like",createLikeCondition(dsNomeRazaoSocial.toUpperCase()));
		}
		
		List situacoesValidas = (List) criteriaMap.get("tpSituacao");
		if (situacoesValidas != null) {
			hql.addCriteriaIn("tpSituacao", situacoesValidas);
		}
		
		String dsBoleto = (String) criteriaMap.get("dsBoleto");
		if (dsBoleto != null && !dsBoleto.isEmpty()) {
			hql.addCriteria("dsBoleto","like",createLikeCondition(dsBoleto));
		}
		
		String obCreditoBancario = (String) criteriaMap.get("obCreditoBancario");
		if (obCreditoBancario != null && !obCreditoBancario.isEmpty()) {
			hql.addCriteria("upper(obCreditoBancario)","like",createLikeCondition(obCreditoBancario.toUpperCase()));
		}
		
		BigDecimal vlCreditoInicial = (BigDecimal) criteriaMap.get("vlCreditoInicial");
		if (vlCreditoInicial != null) {
			hql.addCriteria("vlCredito",">=",vlCreditoInicial);
		}
		
		BigDecimal vlCreditoFinal = (BigDecimal) criteriaMap.get("vlCreditoFinal");
		if (vlCreditoFinal != null) {
			hql.addCriteria("vlCredito","<=",vlCreditoFinal);
		}

		YearMonthDay dataCreditoInicial = (YearMonthDay) criteriaMap.get("dataCreditoInicial");
		if (dataCreditoInicial != null) {
			hql.addCriteria("dtCredito",">=",dataCreditoInicial);
		}
		
		YearMonthDay dataCreditoFinal = (YearMonthDay) criteriaMap.get("dataCreditoFinal");
		if (dataCreditoFinal != null) {
			hql.addCriteria("dtCredito","<=",dataCreditoFinal);
		}
		
		YearMonthDay dataAlteracaoInicial = (YearMonthDay) criteriaMap.get("dataAlteracaoInicial");
		if (dataAlteracaoInicial != null) {
			hql.addCriteria("dhAlteracao.value",">=", dataAlteracaoInicial.toDateTimeAtMidnight(SessionUtils.getFilialSessao().getDateTimeZone()));
			
		}
		
		YearMonthDay dataAlteracaoFinal = (YearMonthDay) criteriaMap.get("dataAlteracaoFinal");
		if (dataAlteracaoFinal != null) {
			hql.addCriteria("dhAlteracao.value","<=", dataAlteracaoFinal.plusDays(1).toDateTimeAtMidnight(SessionUtils.getFilialSessao().getDateTimeZone()));
		}

		DomainValue tpSituacaoFilter = (DomainValue) criteriaMap.get("tpSituacaoFilter");
		if (tpSituacaoFilter != null) {
			hql.addCriteria("tpSituacao","=", tpSituacaoFilter);
		}

		DomainValue tpClassificacaoFilter = (DomainValue) criteriaMap.get("tpClassificacao");
		if (tpClassificacaoFilter != null) {
			hql.addCriteria("tpClassificacao","=", tpClassificacaoFilter);
		}

		return hql;
	}

	private String adicionaCriteriaSQLFiltros(Map criteriaMap) {

		StringBuffer sql = new StringBuffer();

		Long idFilial = (Long) criteriaMap.get("idFilial");
		if (idFilial != null) {
			sql.append("cb.id_Filial =" + idFilial);
		} else {
			sql.append(" 1 = 1 ");
		}
 
		Long idBanco = (Long) criteriaMap.get("idBanco");
		if (idBanco != null) {
			sql.append(" and cb.id_Banco = " + idBanco);
		}

		DomainValue tpModalidade = (DomainValue) criteriaMap.get("tpModalidade");
		if (tpModalidade != null) {
			sql.append(" and cb.tp_Modalidade = '" + tpModalidade.getValue() + "' ");
		}

		DomainValue tpOrigem = (DomainValue) criteriaMap.get("tpOrigem");
		if (tpOrigem != null) {
			sql.append(" and cb.tp_Origem = '" + tpOrigem.getValue() + "' ");
		}

		String dsCpfCnpj = (String) criteriaMap.get("dsCpfCnpj");
		if (dsCpfCnpj != null && !dsCpfCnpj.isEmpty()) {
			sql.append(" and cb.ds_Cpf_Cnpj like '%" + dsCpfCnpj + "%'");
		}

		String dsNomeRazaoSocial = (String) criteriaMap.get("dsNomeRazaoSocial");
		if (dsNomeRazaoSocial != null && !dsNomeRazaoSocial.isEmpty()) {
			sql.append(" and upper(cb.ds_Nome_Razao_Social) like '%" + dsNomeRazaoSocial.toUpperCase() + "%'");
		}

		List<DomainValue> situacoesValidas = (List) criteriaMap.get("tpSituacao");
		if (situacoesValidas != null) {
			String value = "";
			for (DomainValue domainValue : situacoesValidas) {
				value += "'" + domainValue.getValue() + "', ";
			}
			sql.append(" and cb.tp_Situacao in (" + value.substring(0, value.lastIndexOf(",")) + ") ");
		}

		String dsBoleto = (String) criteriaMap.get("dsBoleto");
		if (dsBoleto != null && !dsBoleto.isEmpty()) {
			sql.append(" and cb.ds_Boleto like '%" + dsBoleto + "%'");
		}

		String obCreditoBancario = (String) criteriaMap.get("obCreditoBancario");
		if (obCreditoBancario != null && !obCreditoBancario.isEmpty()) {
			sql.append(" and upper(cb.ob_Credito_Bancario) like '%" + obCreditoBancario.toUpperCase() + "%'");
		}

		BigDecimal vlCreditoInicial = (BigDecimal) criteriaMap.get("vlCreditoInicial");
		if (vlCreditoInicial != null) {
			sql.append(" and cb.vl_Credito >= " + vlCreditoInicial);
		}

		BigDecimal vlCreditoFinal = (BigDecimal) criteriaMap.get("vlCreditoFinal");
		if (vlCreditoFinal != null) {
			sql.append(" and cb.vl_Credito <=" + vlCreditoFinal);
		}

		YearMonthDay dataCreditoInicial = (YearMonthDay) criteriaMap.get("dataCreditoInicial");
		if (dataCreditoInicial != null) {
			sql.append(" and cb.dt_Credito >= to_Date('" + dataCreditoInicial + "', 'YYYY-MM-DD')");
		}

		YearMonthDay dataCreditoFinal = (YearMonthDay) criteriaMap.get("dataCreditoFinal");
		if (dataCreditoFinal != null) {
			sql.append(" and cb.dt_Credito <= to_Date('" + dataCreditoFinal + "', 'YYYY-MM-DD')");
		}

		YearMonthDay dataAlteracaoInicial = (YearMonthDay) criteriaMap.get("dataAlteracaoInicial");
		if (dataAlteracaoInicial != null) {
			sql.append(" and cb.dh_Alteracao >= TO_TIMESTAMP_TZ('" + dataAlteracaoInicial.toDateTimeAtMidnight(SessionUtils.getFilialSessao().getDateTimeZone()) + "', 'YYYY-MM-DD\"T\"HH24:MI:SS.FFTZH:TZM')");

		}

		YearMonthDay dataAlteracaoFinal = (YearMonthDay) criteriaMap.get("dataAlteracaoFinal");
		if (dataAlteracaoFinal != null) {
			sql.append("and cb.dh_Alteracao <= TO_TIMESTAMP_TZ('" + dataAlteracaoFinal.plusDays(1).toDateTimeAtMidnight(SessionUtils.getFilialSessao().getDateTimeZone()) + "', 'YYYY-MM-DD\"T\"HH24:MI:SS.FFTZH:TZM')");
		}

		DomainValue tpSituacaoFilter = (DomainValue) criteriaMap.get("tpSituacaoFilter");
		if (tpSituacaoFilter != null) {
			sql.append("and cb.tp_Situacao = '" + tpSituacaoFilter.getValue() + "' ");
		}
		
		DomainValue tpClassificacaoFilter = (DomainValue) criteriaMap.get("tpClassificacao");
		if (tpClassificacaoFilter != null) {
			sql.append("and tp_Classificacao = '"+ tpClassificacaoFilter.getValue() + "' ");
		}

		return sql.toString();
		
	}

	private String adicionaCriteriaSqlSaldoFiltro(Map criteriaMap) {
		BigDecimal vlSaldoInicial = (BigDecimal) criteriaMap.get("vlSaldoInicial");
		BigDecimal vlSaldoFinal = (BigDecimal) criteriaMap.get("vlSaldoFinal");
		String sql = "";

		if (null != vlSaldoInicial && null != vlSaldoFinal) {
			sql = " WHERE ";
			sql += "  sld >= " + vlSaldoInicial;
			sql += "  and sld <= " + vlSaldoFinal;
		}
		return sql;

	}

	private SqlTemplate adicionaCriteriaHqlSaldoFiltro(SqlTemplate hql, Map criteriaMap) {
		BigDecimal vlSaldoInicial = (BigDecimal) criteriaMap.get("vlSaldoInicial");
		BigDecimal vlSaldoFinal = (BigDecimal) criteriaMap.get("vlSaldoFinal");

		hql.addCriteria("vlSaldo", ">=", vlSaldoInicial);
		hql.addCriteria("vlSaldo", "<=", vlSaldoFinal);

		return hql;

	}

	public CreditoBancarioEntity findByIdComTotal(Long id){
		
		SqlTemplate hql = new SqlTemplate();
    	
		hql.addProjection(" cb ");
		
		StringBuffer select = getSelectTotal();
		hql.addProjection(select.toString());

		StringBuffer froms = new StringBuffer();
		froms.append(" ").append( CreditoBancarioEntity.class.getName()).append(" as cb ");
		hql.addFrom(froms.toString());
    	
		hql.addCriteria("cb.idCreditoBancario","=",id);

    	List<Object[]> creditobancarioList = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	List<CreditoBancarioEntity> creditosList = transformToEntity(creditobancarioList);
    	CreditoBancarioEntity naoEncontrou = null;
    	if(creditosList.size() < 1){
    		return naoEncontrou;
    	}
		return creditosList.get(0);
	}

	private CreditoBancarioEntity transformToEntityUnique(Object[] creditoResult) {
		CreditoBancarioEntity naoEncontrou = null;
		if (creditoResult != null && creditoResult.length == 3) {
			CreditoBancarioEntity creditobancario = (CreditoBancarioEntity) creditoResult[0];
			creditobancario.setTotal((BigDecimal) creditoResult[2]);
			creditobancario.getUsuario().getUsuarioADSM().getNmUsuario();
			creditobancario.getFilial().getPessoa();
			creditobancario.setVlSomaSaldo(((BigDecimal) creditoResult[1]));
			return creditobancario;

		} else if (creditoResult != null && creditoResult.length == 2) {
			CreditoBancarioEntity creditobancario = (CreditoBancarioEntity) creditoResult[0];
			creditobancario.setTotal(((BigDecimal) creditoResult[1]));
			creditobancario.getUsuario().getUsuarioADSM().getNmUsuario();
			creditobancario.getFilial().getPessoa();
			return creditobancario;

		}
		return naoEncontrou;

	}
	
	private String createLikeCondition(String param) {
		return "%" + param + "%";
	}

	@Override
	protected Class getPersistentClass() {
		return CreditoBancarioEntity.class;
	}

	public Integer findCount(Map<String, Object> map) {
		SqlTemplate hql = getCreditosFilterHqlQueryCount(map);
		
		return (Integer) ((Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria())).intValue();
	}
	
	public Integer findCountLote(Map<String, Object> map) {
		return findCountCreditosSomaFilterSqlQuery(map);

	}
	
	private Integer findCountCreditosSomaFilterSqlQuery(final Map map) { 
		String sqlQuery = 
				  " SELECT COUNT(*) linhas FROM (														"
				+ " 	SELECT sum(vl_credito - vlUtilizado) OVER () AS soma,		"
				+ "			creditos.*															"
				+ " 	FROM (																	"
				+ " 		SELECT 														 		"
				+ "				(credb.vl_credito - vlUtilizado) as sld,						"
				+ "					credb.* 													"
				+ "			FROM (																"
				+ "				SELECT cb.*, nvl(												"
				+ "					(SELECT sum(comp.VL_PAGAMENTO)								"
				+ "						FROM COMPOSICAO_PAGAMENTO_REDECO comp, REDECO rd		"
				+ "						WHERE comp.ID_CREDITO_BANCARIO = cb.ID_CREDITO_BANCARIO	"
				+ "						AND comp.ID_REDECO = rd.ID_REDECO						"
				+ "						AND rd.TP_SITUACAO_REDECO <> 'CA'						"
				+ "					), 0) AS vlUtilizado										"
				+ "				FROM CREDITO_BANCARIO cb										"
				+ "			WHERE 																"
				+ 				adicionaCriteriaSQLFiltros(map)
				+ "		) credb																	"
				+ "		ORDER BY sld															"
				+ "	) creditos																	"
				+ 		adicionaCriteriaSqlSaldoFiltro(map)
				+ " ) ";

		Map<String, Object> param = new HashMap<String, Object>();
		return getAdsmHibernateTemplate().getRowCountBySql(sqlQuery, param);

	}

	public CreditoBancarioEntity findByIdComposicaoPagamento(Long idComposicaoPagamento){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idComposicao", idComposicaoPagamento);
    	String hql = new StringBuilder().
    			append(" select cb ").
    			append(" from ").append(ComposicaoPagamentoRedeco.class.getName()).append(" cpr, ").
    			append(CreditoBancarioEntity.class.getName()).append(" cb ").
    			append(" where ").
    			append(" cb.idCreditoBancario = cpr.creditoBancario.idCreditoBancario and ").
    			append(" cpr.idComposicaoPagamentoRedeco = :idComposicao ").
    			toString();
		List<CreditoBancarioEntity> data = getAdsmHibernateTemplate().findByNamedParam(hql, params);
		if(data == null){
			return null;
		}
		return data.get(0);
	}

	public List<CreditoBancarioEntity> findCreditosBancariosALiberar(
			YearMonthDay dataDeCorte) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataDeCorte", dataDeCorte);
    	String hql = new StringBuilder().
    			append(" from ").
    			append(CreditoBancarioEntity.class.getName()).
    			append(" where ").
    			append(" dtCredito  <= :dataDeCorte and ").
    			append(" tpSituacao = 'D' ").
    			toString();
    	return getAdsmHibernateTemplate().findByNamedParam(hql, params);
	}
	
	
	/**
	 * Jira LMSA-2155
	 * 
	 * @param dtCorte
	 * @return List<Object[]>
	 */
	public List<Object[]> findCreditosBancariosByDataCorte(String dtCorte) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id_credito_bancario AS Id_Credito_Bancario,							"
				+ "	f.sg_filial as Filial,														"
				+ "	b.nr_banco AS Banco,														"
				+ "	b.nm_banco AS Nome_Banco,													"
				+ "	dt_credito AS Data_credito,													"
				+ "	decode(cb.tp_modalidade, 'BO', 'BOLETO', 'DE', 'DEPÓSITO', 'DO',			"
				+ " 	'DOC', 'TE', 'TED', 'TR', 'TRANSFERÊNCIA') AS Modalidade,				"
				+ "	decode(cb.tp_origem, 'R', 'COB BANCÁRIA', 'CARTEIRA') AS Origem,			"
				+ "	decode(cb.tp_situacao, 'L', 'Liberado', 'Digitado') AS Situacao_Credito,	"
				+ "	ds_cpf_cnpj AS CPF_CNPJ_Cliente,											"
				+ "	ds_nome_razao_social AS Razao_Social,										"
				+ "	ds_boleto AS Boleto,														"
				+ "	ob_credito_bancario AS Observacao,											"
				+ "	vl_credito AS Valor_Credito,												"
				+ "	COALESCE((																	"
				+ "		SELECT sum(vl_pagamento)												"
				+ "		FROM composicao_pagamento_redeco cpr,									"
				+ "			redeco r															"
				+ "		WHERE cpr.id_credito_bancario = cb.id_credito_bancario					"
				+ "			AND cpr.id_redeco = r.id_redeco										"
				+ "			AND r.dt_liquidacao <= to_date(:dtCorte, 'YYYY-MM-DD')				"
				+ "			AND r.tp_situacao_redeco = 'LI'										"
				+ "		), 0) AS Valor_Utilizado,												"
				+ "	vl_credito - COALESCE((														"
				+ "		SELECT sum(vl_pagamento)												"
				+ "		FROM composicao_pagamento_redeco cpr,								"
				+ "			redeco r													"
				+ "		WHERE cpr.id_credito_bancario = cb.id_credito_bancario		"
				+ "			AND cpr.id_redeco = r.id_redeco							"
				+ "			AND r.dt_liquidacao <= to_date(:dtCorte, 'YYYY-MM-DD')	"
				+ "			AND r.tp_situacao_redeco = 'LI'							"
				+ "		), 0) AS Saldo,												"
				+ "	decode(cb.tp_classificacao, 									"
				+ "		'IN', 'INTERCOMPANY',										"
				+ "		'TI', 'TRANSPORTE INTERNACIONAL', 							"
				+ "		'DR', 'DESPESAS RECUPERADAS', 'CR', 'DESCONTOS RECUPERADOS',"
				+ "		'JR', 'JUROS RECUPERADOS', 'OU', 'OUTROS') 					"
				+ "												AS CLASSIFICACAO	"
				+ "FROM Credito_bancario cb,										"
				+ "	banco b,														"
				+ "	filial f														"
				+ "WHERE cb.dt_credito <= to_date(:dtCorte, 'YYYY-MM-DD')			"
				+ "	AND cb.id_filial = f.id_filial									"
				+ "	AND cb.id_banco = b.id_banco									"
				+ "	AND cb.vl_credito > COALESCE((									"
				+ "		SELECT sum(vl_pagamento)									"
				+ "		FROM composicao_pagamento_redeco cpr,						"
				+ "			redeco r												"
				+ "		WHERE cpr.id_credito_bancario = cb.id_credito_bancario		"
				+ "			AND cpr.id_redeco = r.id_redeco							"
				+ "			AND r.dt_liquidacao <= to_date(:dtCorte, 'YYYY-MM-DD')	"
				+ "			AND r.tp_situacao_redeco = 'LI'							"
				+ "		), 0)														"
				+ "ORDER BY cb.dt_credito											");
		
        ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {                
                sqlQuery.addScalar("Id_Credito_Bancario", Hibernate.LONG);
                sqlQuery.addScalar("Filial", Hibernate.STRING);
                sqlQuery.addScalar("Banco", Hibernate.STRING);
                sqlQuery.addScalar("Nome_Banco", Hibernate.STRING);
                sqlQuery.addScalar("Data_Credito", Hibernate.STRING);
                sqlQuery.addScalar("Modalidade", Hibernate.STRING);
                sqlQuery.addScalar("Origem", Hibernate.STRING);
                sqlQuery.addScalar("Situacao_Credito", Hibernate.STRING);
                sqlQuery.addScalar("CPF_CNPJ_Cliente", Hibernate.STRING);
                sqlQuery.addScalar("Razao_Social", Hibernate.STRING);
                sqlQuery.addScalar("Boleto", Hibernate.STRING);
                sqlQuery.addScalar("Observacao", Hibernate.STRING);
                sqlQuery.addScalar("Valor_Credito", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("Valor_Utilizado", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("Saldo", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("Classificacao", Hibernate.STRING);
}
        };

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dtCorte", dtCorte);

		return getAdsmHibernateTemplate().findBySql(sql.toString(), param, configSql);
	}
	
}
