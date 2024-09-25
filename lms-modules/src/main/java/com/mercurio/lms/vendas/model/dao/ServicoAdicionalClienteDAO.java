package com.mercurio.lms.vendas.model.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.util.AliasToNestedBeanResultTransformer;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class ServicoAdicionalClienteDAO extends BaseCrudDao<ServicoAdicionalCliente, Long> {

	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return ServicoAdicionalCliente.class;
	}

	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindById) {
		lazyFindById.put("parcelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaDivisaoCliente", FetchMode.JOIN);
		lazyFindById.put("tabelaDivisaoCliente.tabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaDivisaoCliente.tabelaPreco.moeda", FetchMode.JOIN);
		lazyFindById.put("simulacao", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	@Override
	protected void initFindByIdLazyProperties(Map arg0) {
		arg0.put("parcelaPreco", FetchMode.JOIN);
		super.initFindByIdLazyProperties(arg0);
	}

	public ResultSetPage findPaginatedByProposta(TypedFlatMap criteria) {
		ProjectionList projectionList = Projections.projectionList()
			.add(Projections.property("d.idServicoAdicionalCliente"), "idServicoAdicionalCliente")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco.nmParcelaPreco")
			.add(Projections.property("d.tpIndicador"), "tpIndicador")
			.add(Projections.property("d.vlValor"), "vlValor")
			.add(Projections.property("m.sgMoeda"), "tabelaDivisaoCliente.tabelaPreco.moeda.sgMoeda")
			.add(Projections.property("m.dsSimbolo"), "tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo");

		DetachedCriteria dc = createQueryByProposta(criteria);
		dc.setProjection(projectionList);
		dc.addOrder(OrderVarcharI18n.asc("pp.nmParcelaPreco", LocaleContextHolder.getLocale()));
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(ServicoAdicionalCliente.class));

		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		return findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}

	public Integer getRowCountByProposta(TypedFlatMap criteria) {
		DetachedCriteria dc = createQueryByProposta(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public DetachedCriteria createQueryByProposta(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d");
		dc.createAlias("d.parcelaPreco", "pp");
		dc.createAlias("d.simulacao", "s");
		dc.createAlias("s.tabelaPreco", "tp");
		dc.createAlias("tp.moeda", "m");
		/** Filtro */
		Long idSimulacao = criteria.getLong("simulacao.idSimulacao");
		if(idSimulacao != null) {
			dc.add(Restrictions.eq("s.id", idSimulacao));
		}
		Long idParcelaPreco = criteria.getLong("parcelaPreco.idParcelaPreco");
		if(idParcelaPreco != null) {
			dc.add(Restrictions.eq("pp.id", idParcelaPreco));
		}
		String tpIndicador = criteria.getString("tpIndicador");
		if(StringUtils.isNotBlank(tpIndicador)) {
			dc.add(Restrictions.eq("d.tpIndicador", tpIndicador));
		}
		return dc;
	}

	public Integer getRowCountByIdCotacao(Long idCotacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq("d.cotacao.id", idCotacao));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public List<Map<String, Object>> findByIdCotacao(Long idCotacao) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("d.idServicoAdicionalCliente"), "idServicoAdicionalCliente")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco_nmParcelaPreco")
			.add(Projections.property("d.tpIndicador"), "tpIndicador")
			.add(Projections.property("d.vlValor"), "vlValor");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
			.createAlias("d.parcelaPreco", "pp")
			.setProjection(pl)
			.add(Restrictions.eq("d.cotacao.id", idCotacao))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	public List<Long> findIdsByTabelasPrecoIdSimulacao(Long idSimulacao, List<Long> idParcelaPrecos) {
		StringBuilder hql = new StringBuilder()
			.append(" select gc.idServicoAdicionalCliente \n")
			.append("   from ").append(getPersistentClass().getName()).append(" gc \n")
			.append("  where gc.simulacao.id = :idSimulacao \n");

		List<String> paramNames = new ArrayList<String>();
		List<Object> paramValues = new ArrayList<Object>();
		paramNames.add("idSimulacao");
		paramValues.add(idSimulacao);
		if (idParcelaPrecos != null && !idParcelaPrecos.isEmpty()) {
			hql.append("    and gc.parcelaPreco.id in (:idParcelaPrecos) \n");
			paramNames.add("idParcelaPrecos");
			paramValues.add(idParcelaPrecos);
		}

		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), paramNames.toArray(new String[] {}), paramValues.toArray());
	}

	/**
	 * Método utilizado pela Integração
	 * @author Claiton Grings
	 * 
	 * @param idTabelaDivisaoCliente
	 * @param idParcelaPreco
	 * @return
	 */
	public ServicoAdicionalCliente findServicoAdicionalCliente(Long idTabelaDivisaoCliente, Long idParcelaPreco) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "sac")
		.add(Restrictions.eq("sac.tabelaDivisaoCliente.id", idTabelaDivisaoCliente))
		.add(Restrictions.eq("sac.parcelaPreco.id", idParcelaPreco));

		return (ServicoAdicionalCliente)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Método utilizado pela Integração - CQPRO00007501
	 * @author Claiton Grings
	 * 
	 * @param idsTabelaDivisaoCliente
	 */
	public void removeByTabelaDivisaoCliente(List<Long> idsTabelaDivisaoCliente) {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from ").append(getPersistentClass().getName()).append(" as sac ");
		sql.append(" where sac.tabelaDivisaoCliente.id in (:id)");
		getAdsmHibernateTemplate().removeByIds(sql.toString(), idsTabelaDivisaoCliente);
	}

	@SuppressWarnings("unchecked")
	public List<ServicoAdicionalCliente> findByTabelaDivisaoCliente(Long idTabelaPreco, Long idDivisaoCliente, String cdParcelaPreco) {
    	StringBuilder query = new StringBuilder();
    	
		query.append("from " + ServicoAdicionalCliente.class.getName() + " as sac ");
		query.append("inner join fetch sac.parcelaPreco pp ");
		query.append("inner join fetch sac.tabelaDivisaoCliente tdc ");
		query.append("inner join fetch tdc.divisaoCliente dc ");
		query.append("inner join fetch tdc.tabelaPreco tp ");
		query.append("where ");
		query.append(" 		dc.idDivisaoCliente = :idDivisaoCliente ");
		query.append(" and	tp.idTabelaPreco = :idTabelaPreco ");
		
		TypedFlatMap bindValues = new TypedFlatMap();
		bindValues.put("idTabelaPreco", idTabelaPreco);
		bindValues.put("idDivisaoCliente", idDivisaoCliente);		
		
		if(cdParcelaPreco != null) {
			query.append(" and pp.cdParcelaPreco = :cdParcelaPreco");
			bindValues.put("cdParcelaPreco", cdParcelaPreco);
		}
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), bindValues);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ServicoAdicionalCliente> findByTabelaSimulacaoCliente(Long idSimulacao) {
    	StringBuilder query = new StringBuilder();
    	
		query.append("from " + ServicoAdicionalCliente.class.getName() + " as sac ");
		query.append("inner join fetch sac.parcelaPreco pp ");
		query.append("inner join fetch sac.simulacao s ");
		query.append("inner join fetch s.tabelaPreco tp ");
		query.append("where ");
		query.append(" 		s.idSimulacao = :idSimulacao ");
		
		TypedFlatMap bindValues = new TypedFlatMap();
		bindValues.put("idSimulacao", idSimulacao);		
	
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), bindValues);
	}
	
	public int updateValorAndValorMinById(Long idServAdicionalCliente, BigDecimal valorReajuste, BigDecimal valorMinReajuste){
		String sql = new StringBuilder()
							.append(" UPDATE servico_adicional_cliente  ")
							.append("    SET vl_valor = :valorReajuste , vl_minimo = ").append(valorMinReajuste == null ? "NULL" : ":valorMinReajuste")
							.append(" WHERE id_servico_adicional_cliente = :idServAdicionalCliente  ") 
							.toString();
		
		Map<String, Object> namedParams = new HashMap<String, Object>();
    	namedParams.put("valorReajuste", valorReajuste);
    	namedParams.put("valorMinReajuste", valorMinReajuste );
    	namedParams.put("idServAdicionalCliente", idServAdicionalCliente);
		
		return getAdsmHibernateTemplate().executeUpdateBySql(sql, namedParams);
	}
	public void insertServicoAdicionalReajusteCliente(Long idReajusteCliente, Long idServicoAdicionalCliente, BigDecimal pcReajusteValMinParcela, BigDecimal pcReajusteValorParcela, BigDecimal valorMinimo, BigDecimal valor) {
		String sql = new StringBuilder()
			.append(" INSERT INTO SERVICO_ADIC_REAJ_CLIENTE (ID_SERVICO_ADIC_REAJ_CLIENTE, ID_REAJUSTE_CLIENTE, ID_SERVICO_ADICIONAL_CLIENTE, ")
			.append("     PC_REAJUSTE_VAL_MIN_PARCELA, PC_REAJUSTE_VALOR_PARCELA, VL_MINIMO, VL_VALOR )  ") 
			.append(" VALUES  ")
			.append(" (servico_adic_reaj_cliente_seq.nextval,?,?,?,?,?,?)  ")
			.toString();
		
    	jdbcTemplate.update(sql, new Object[]{idReajusteCliente, idServicoAdicionalCliente, pcReajusteValMinParcela, pcReajusteValorParcela, valorMinimo, valor}); 
	}	
	
	public List<ServicoAdicionalCliente> findByIdTabelaDivisaoCliente(Long idTabelaDivisaoCliente) {
    	String query = new StringBuilder()    	
							.append("from " + ServicoAdicionalCliente.class.getName() + " as sac ")
							.append("where sac.tabelaDivisaoCliente.idTabelaDivisaoCliente = :idTabelaDivisaoCliente ").toString();
		
		TypedFlatMap params = new TypedFlatMap();
		params.put("idTabelaDivisaoCliente", idTabelaDivisaoCliente);		
	
		return getAdsmHibernateTemplate().findByNamedParam(query, params);
	}
	
	public List<Map<String,Object>> findServicoAdicReajusteCliente(Long idReajusteCliente){
		String query = new StringBuilder()
						.append(" select id_servico_adicional_cliente id, vl_minimo valorMinimo, vl_valor valor ")
						.append(" from SERVICO_ADIC_REAJ_CLIENTE ")
						.append(" where id_reajuste_cliente = :idReajusteCliente ")
						.toString();
		
		Map<String, Object> namedParams = new HashMap<String, Object>();
		namedParams.put("idReajusteCliente", idReajusteCliente);
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id", Hibernate.LONG);
				sqlQuery.addScalar("valorMinimo", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("valor", Hibernate.BIG_DECIMAL);
			}
		};
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(query, namedParams, csq);
	}
	
	public void removeServicoAdicionalReajusteCliente(Long idReajustecliente){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idReajustecliente",idReajustecliente);
		
		String sqlTaxaCliente = new StringBuilder("DELETE FROM SERVICO_ADIC_REAJ_CLIENTE WHERE ID_REAJUSTE_CLIENTE = :idReajustecliente").toString();
		getAdsmHibernateTemplate().executeUpdateBySql(sqlTaxaCliente, params);
	}
		
	public List<Map<String,Object>> listServicoAdicionalCliente(Long idTabDivisaoCliente, YearMonthDay vigenciaInicial, Long idReajuste){
		String query = new StringBuilder()
				.append(" select     ") 
				.append(" 	sarc.VL_MINIMO as CALCULADO_MINIMO,  ")
				.append("   sarc.VL_VALOR  as CALCULADO,  ")
				.append("   sarc.PC_REAJUSTE_VAL_MIN_PARCELA as PERCENTUAL_MINIMO, ")
				.append("   sarc.PC_REAJUSTE_VALOR_PARCELA as PERCENTUAL, ")
				.append(" 	round(sarc.VL_MINIMO - (sarc.VL_MINIMO * sarc.PC_REAJUSTE_VAL_MIN_PARCELA / (100 + sarc.PC_REAJUSTE_VAL_MIN_PARCELA)),2 ) as VALOR_MINIMO, ")
				.append("   round(sarc.VL_VALOR - (sarc.VL_VALOR * sarc.PC_REAJUSTE_VALOR_PARCELA / (100 + sarc.PC_REAJUSTE_VALOR_PARCELA)),2 ) as VALOR, ")
				.append(" 	SUBSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), 'pt_BR»')+LENGTH('pt_BR»')) as SERVADICIONAL, ")
				.append(" 	sarc.ID_SERVICO_ADIC_REAJ_CLIENTE as ID_SERV_ADIC_CLIENTE ")
				.append(" from servico_adicional_cliente sac,	 ")
				.append(" 	   servico_adic_reaj_cliente sarc, ")
				.append("      parcela_preco pp	 ")
				.append(" where sac.id_servico_adicional_cliente = sarc.id_servico_adicional_cliente(+)	 ")
				.append(" 	and sac.id_parcela_preco = pp.id_parcela_preco ")
				.append("   and sac.ID_TABELA_DIVISAO_CLIENTE = ? ")
				.append("   and sarc.id_reajuste_cliente = ? ")
				.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("CALCULADO_MINIMO", 	resultSet.getBigDecimal(1));
				map.put("CALCULADO",			resultSet.getBigDecimal(2));
				map.put("PERCENTUAL_MINIMO", 	resultSet.getBigDecimal(3));
				map.put("PERCENTUAL", 			resultSet.getBigDecimal(4));
				map.put("VALOR_MINIMO", 		resultSet.getBigDecimal(5));
				map.put("VALOR", 				resultSet.getBigDecimal(6));
				map.put("SERVADICIONAL", 		resultSet.getString(7));
				map.put("ID_SERV_ADIC_CLIENTE", resultSet.getLong(8));
				return map;
			}
		};
		
		Object[] params = new Object[]{idTabDivisaoCliente, idReajuste};
		return jdbcTemplate.query(query, JodaTimeUtils.jdbcPureParamConverter(jdbcTemplate, params) , rowMapper);
	}
	
	public void updatePercValorAndPercValorMinimo(Long id, BigDecimal valor, BigDecimal valorMinimo, BigDecimal percentual, BigDecimal percentualMinimo){
		StringBuilder sql = new StringBuilder()
									.append(" UPDATE SERVICO_ADIC_REAJ_CLIENTE SET ")
									.append("   VL_MINIMO = ").append(valorMinimo)
									.append(" , VL_VALOR = ").append(valor) 
									.append(" , PC_REAJUSTE_VAL_MIN_PARCELA = ").append(percentualMinimo)
									.append(" , PC_REAJUSTE_VALOR_PARCELA = ").append(percentual)
									.append(" WHERE ID_SERVICO_ADIC_REAJ_CLIENTE = ").append(id);
		
		jdbcTemplate.update(sql.toString());
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public ResultSetPage findCustomPaginated(Map criteria, FindDefinition def) {
		
		List<Object> parameters = new ArrayList<Object>();
			
		StringBuilder hql = new StringBuilder()
			.append(" select new Map (sim_fil.sgFilial || '' || sim.nrSimulacao as idProposta, ")
			.append("sac as servicoAdicionalCliente, pp.nmParcelaPreco as nmParcelaPreco) ")
			.append("from ").append(ServicoAdicionalCliente.class.getName()).append(" as sac ")
			.append("inner join sac.tabelaDivisaoCliente as tdc ")
			.append("inner join sac.parcelaPreco as pp ")
			.append("left join sac.simulacao as sim ")
			.append("left join sim.filial as sim_fil ")
			.append("where  tdc.idTabelaDivisaoCliente = ? ");
			
		parameters.add(Long.valueOf((String)MapUtils.getMap(criteria, "tabelaDivisaoCliente").get("idTabelaDivisaoCliente")));
		
		if(StringUtils.isNotEmpty((String)criteria.get("tpIndicador"))) {
			hql.append("and sac.tpIndicador = ? ");
			parameters.add(criteria.get("tpIndicador"));
		}
		
		if(StringUtils.isNotEmpty((String)MapUtils.getMap(criteria, "parcelaPreco").get("idParcelaPreco"))) {
			hql.append("and sac.parcelaPreco.idParcelaPreco = ? ");
			parameters.add(Long.valueOf((String)MapUtils.getMap(criteria, "parcelaPreco").get("idParcelaPreco")));
		}
		
		if(StringUtils.isNotEmpty((String)criteria.get("dsSimbolo"))) {
			hql.append("and tdc.tabelaPreco.moeda.dsSimbolo = ? ");
			parameters.add(criteria.get("dsSimbolo"));
		}
		
		if(StringUtils.isNotEmpty((String)criteria.get("sgMoeda"))) {
			hql.append("and tdc.tabelaPreco.moeda.sgMoeda = ? ");
			parameters.add(criteria.get("sgMoeda"));
		}
		
		hql.append("order by pp.nmParcelaPreco ");
		
		return getAdsmHibernateTemplate().findPaginated(hql.toString(), def.getCurrentPage(), def.getPageSize(), parameters.toArray());
	}
	
	public Boolean hasNegociacaoServicoAdicionalCliente(String cdParcela, DivisaoCliente divisaoCliente){
	    StringBuilder query = new StringBuilder();
	    
	    query.append("SELECT COUNT(PP.ID_PARCELA_PRECO) \n");
	    query.append("FROM DIVISAO_CLIENTE DC \n");
	    query.append("INNER JOIN TABELA_DIVISAO_CLIENTE TDC on TDC.ID_DIVISAO_CLIENTE = DC.ID_DIVISAO_CLIENTE \n");
	    query.append("INNER JOIN TABELA_PRECO TP on TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO \n");
	    query.append("INNER JOIN TIPO_TABELA_PRECO TTP on TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO \n");
	    query.append("LEFT JOIN SERVICO_ADICIONAL_CLIENTE SAC on TDC.ID_TABELA_DIVISAO_CLIENTE = SAC.ID_TABELA_DIVISAO_CLIENTE \n");
	    query.append("LEFT JOIN TABELA_PRECO_PARCELA TPP ON TPP.ID_TABELA_PRECO = TP.ID_TABELA_PRECO \n");
	    query.append("LEFT JOIN VALOR_SERVICO_ADICIONAL VSA ON TPP.ID_TABELA_PRECO_PARCELA = VSA.ID_VALOR_SERVICO_ADICIONAL \n");
	    query.append("INNER JOIN PARCELA_PRECO PP on (SAC.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO or TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO) \n");
	    query.append("INNER JOIN CLIENTE C on DC.ID_CLIENTE = C.ID_CLIENTE \n");
	    query.append("INNER JOIN PESSOA P on P.ID_PESSOA = C.ID_CLIENTE \n");
	    query.append("WHERE C.TP_CLIENTE                 IN ('S', 'F') \n");
	    query.append("AND ((Ttp.Tp_Tipo_Tabela_Preco   In ('A', 'T', 'M', 'D') \n");
	    query.append("AND ((Sac.Tp_Indicador = 'V' And Sac.Vl_Valor = 0) Or (Sac.Tp_Indicador = 'D' And Sac.Vl_Valor = 100) Or (VSA.VL_SERVICO = 0 )))) \n");
	    query.append("AND PP.CD_PARCELA_PRECO          IN ('"+cdParcela+"') \n");
	    query.append("AND C.ID_CLIENTE                  = "+divisaoCliente.getCliente().getIdCliente()+"\n");
	    query.append("AND DC.ID_DIVISAO_CLIENTE         = "+divisaoCliente.getIdDivisaoCliente());

	    int count = jdbcTemplate.queryForInt(query.toString());
	    
	    if(count > 0 ){
	    	return false;
	    }
	    
	    return true;
	}
}