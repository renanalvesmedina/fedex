package com.mercurio.lms.contasreceber.model.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateMidnight;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ItemRedeco;
import com.mercurio.lms.contasreceber.model.Redeco;
import com.mercurio.lms.contasreceber.model.param.RedecoSomatorioParam;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicaï¿½ï¿½o
 * atravï¿½s do suporte ao Hibernate em conjunto com o Spring.
 * Nï¿½o inserir documentaï¿½ï¿½o apï¿½s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RedecoDAO extends BaseCrudDao<Redeco, Long> {
	
	/**
	 * Nome da classe que o DAO ï¿½ responsï¿½vel por persistir.
	 */
    @Override
	protected final Class getPersistentClass() {
        return Redeco.class;
    }
    
    public void evict(Redeco redeco){
    	this.getSession().evict(redeco);
    }
    
    @Override
	public void flush(){
    	this.getSession().flush();
    }
    
	/**
	 * Retorna o redeco com todas as pendencias 'fetchado' se existe.
	 * 
	 * @author Mickaï¿½l Jalbert
	 * @since 13/07/2006
	 * 
	 * @param Long idRedeco
	 * 
	 * @return Redeco
	 */
	public Redeco findByIdWithPendencia(Long idRedeco){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("red");
    	
    	hql.addInnerJoin(Redeco.class.getName(), "red");
    	hql.addLeftOuterJoin("fetch red.pendenciaDesconto", "pend");
    	hql.addLeftOuterJoin("fetch red.pendenciaLucrosPerdas", "penl");
    	hql.addLeftOuterJoin("fetch red.pendenciaRecebimento", "penr");    	
    	
    	hql.addCriteria("red.id", "=" , idRedeco);
    	
    	List lstRedeco = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (lstRedeco != null && lstRedeco.size() == 1){
    		return (Redeco)lstRedeco.get(0);
    	} else {
    		return null;
    	}		
	}
	
	/**
	 * Retorna o redeco disconectado do hibernate
	 * 
	 * @author Mickaï¿½l Jalbert
	 * @since 16/01/2007
	 * 
	 * @param idRedeco
	 * 
	 * @return redeco
	 */
	public Redeco findByIdDisconnected(Long idRedeco){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("red");
    	
    	hql.addInnerJoin(Redeco.class.getName(), "red");
    	
    	hql.addCriteria("red.id", "=" , idRedeco);
    	
    	Redeco redeco = (Redeco)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    	
    	getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().evict(redeco);
    	
    	return redeco;
	}	
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("moeda", FetchMode.JOIN);
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
		
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("filial", FetchMode.JOIN);
		lazyFindLookup.put("filial.pessoa", FetchMode.JOIN);
		
		super.initFindLookupLazyProperties(lazyFindLookup);
	}

	/**
     * Find by id da tela Redeco
     * 
     * @author Mickaï¿½l Jalbert
     * @since 04/07/2006
     * 
     * @param Long idRedeco
     * @return Redeco
     */
    public Redeco findByIdTela(Long idRedeco){
    	SqlTemplate hql = mountHql(idRedeco);
    	
    	hql.addProjection("red");
    	
    	List lstRedeco = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (lstRedeco != null && lstRedeco.size() == 1){
    		return (Redeco)lstRedeco.get(0);
    	} else {
    		return null;
    	}
    }    
    
    @Override
	public void removeById(Long id) {
    	super.removeById(id, true);
    }
    
    @Override
	public int removeByIds(List ids) {
    	return super.removeByIds(ids, true);
    }
    
    public void storeItemRedeco(List lstItemRedecoNew, List lstItemRedecoDeleted) {
    	removeItemRedeco(lstItemRedecoDeleted);
    	storeItemRedeco(lstItemRedecoNew);
		getAdsmHibernateTemplate().flush();    
    }     
    
	private void storeItemRedeco(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}
	
	private void removeItemRedeco(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}	
    
    /**
     * Soma os valores de desconto, juros etc e retorna um objeto RedecoSomatorioParam populado 
     * dos valores do redeco informado.
     * 
     * @author Mickaï¿½l Jalbert
     * @since 04/07/2006
     * 
     * @param Long idRedeco
     * @return RedecoSomatorioParam
     */
    public RedecoSomatorioParam findSomatorio(Long idRedeco){
    	RedecoSomatorioParam somatorio = new RedecoSomatorioParam();
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("count(nvl(fa.id_fatura, re.id_recibo))", "qtTotalDocumentos");
    	sql.addProjection("sum(nvl(fa.vl_desconto, re.vl_total_desconto))", "vlTotalDesconto");
    	sql.addProjection("sum(ir.vl_juros)", "vlTotalJuros");
    	sql.addProjection("sum(nvl(fa.vl_total, nvl(re.vl_total_recibo, 0)))", "vlTotalPago");
    	sql.addProjection("sum(ir.vl_tarifa)", "vlTotalTarifas");
    	sql.addProjection("sum(ir.vl_diferenca_cambial_cotacao)", "vlDiferencaCambialCotacao");

    	sql.addFrom("item_redeco", "ir");
    	sql.addFrom("fatura", "fa");
    	sql.addFrom("recibo", "re");
    	
    	sql.addJoin("ir.id_fatura", "fa.id_fatura(+)");
    	sql.addJoin("ir.id_recibo", "re.id_recibo(+)");
    	
    	sql.addCriteria("ir.id_redeco", "=", idRedeco);

    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("qtTotalDocumentos", Hibernate.LONG);
                sqlQuery.addScalar("vlTotalDesconto", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("vlTotalJuros", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("vlTotalPago", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("vlTotalTarifas", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("vlDiferencaCambialCotacao", Hibernate.BIG_DECIMAL);
			}
		};
    	
    	Object[] arraySomatorio = (Object[])getAdsmHibernateTemplate().findByIdBySql(sql.getSql(), sql.getCriteria(), csq);
    	
    	somatorio.setQtTotalDocumentos((Long)arraySomatorio[0]);
    	somatorio.setVlTotalDesconto((BigDecimal)arraySomatorio[1]);
    	somatorio.setVlTotalJuros((BigDecimal)arraySomatorio[2]);
    	somatorio.setVlTotalPago((BigDecimal)arraySomatorio[3]);
    	somatorio.setVlTotalTarifas((BigDecimal)arraySomatorio[4]);
    	somatorio.setVlDiferencaCambialCotacao((BigDecimal)arraySomatorio[5]);
    	
    	return somatorio;
    }

    private SqlTemplate mountHql(Long idRedeco){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addInnerJoin(Redeco.class.getName(), "red");
    	hql.addInnerJoin("fetch red.moeda", "moe");
    	hql.addInnerJoin("fetch red.filial", "fil");
    	hql.addInnerJoin("fetch fil.pessoa", "pes");
    	
    	hql.addLeftOuterJoin("fetch red.pendenciaDesconto", "pendenciaDesconto");
    	hql.addLeftOuterJoin("fetch red.pendenciaLucrosPerdas", "pendenciaLucrosPerdas");
    	hql.addLeftOuterJoin("fetch red.pendenciaRecebimento", "pendenciaRecebimento");
    	
    	hql.addLeftOuterJoin("fetch red.empresaCobranca", "emp");
    	
    	hql.addCriteria("red.id", "=" , idRedeco);
    	
    	return hql;
    }
    
	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("filial", FetchMode.JOIN);
		lazyFindPaginated.put("moeda", FetchMode.JOIN);
		lazyFindPaginated.put("filial.pessoa", FetchMode.JOIN);
		
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	} 

	/**
	 * Pesquisa de redeco pelo id 
	 * @param TypedFlatMap criteria
	 * @return Redeco 
	 */
	public List findRedeco(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();
    	
		sql.addProjection("r" );
		
		sql.addInnerJoin(this.getPersistentClass().getName(), "r");
		sql.addInnerJoin("fetch r.filial", "fil");
		sql.addInnerJoin("fetch fil.pessoa", "pes");
		
		sql.addCriteria("fil.idFilial", "=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("r.nrRedeco", "=", criteria.getLong("nrRedeco"));
		
    	return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}	
    
	/**
	 * Pesquisa de redeco pelo id 
	 * @param Long idRedeco
	 * @return ResultSetPage selecionado 
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria){
		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);
		
		SqlTemplate sql = new SqlTemplate();
    	
		sql.addProjection("r" );
		
		sql.addInnerJoin(this.getPersistentClass().getName(), "r" );
		sql.addInnerJoin("fetch r.filial", "fil");
				
		sql.addCriteria("nrRedeco", "=", criteria.getLong("nrRedeco"));
		
		sql.addOrderBy("fil.sgFilial");
		sql.addOrderBy("r.nrRedeco");
		
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), 
									    			findDefinition.getCurrentPage(),
													findDefinition.getPageSize(), 
    												sql.getCriteria());
    	
	}	

	public Integer getRowCount(TypedFlatMap criteria){
		DetachedCriteria dc = createCriteria(criteria);
		dc.setProjection(Projections.rowCount());

		return (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public List<Map<String, Object>> findForCsv(TypedFlatMap map) {
		String query = createQueryForCsv(map);

		Map<String, Object> namedParams = new HashMap<String, Object>();

		return getAdsmHibernateTemplate().findBySqlToMappedResult(query, namedParams, getConfigureSql());
	}
	
	
	private String createQueryForCsv(TypedFlatMap map) {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT t.id_redeco as id_redeco, "
				+ "f.sg_filial as \"Filial\", "
				+ "t.nr_redeco as \"Numero\", "
				+ "t.dt_emissao as \"Emissao\", "
				+ "t.dt_recebimento as \"Recebimento\", "
				+ "t.dt_liquidacao as \"Liquidacao\", "
				+ "t.tp_situacao_redeco as \"Situacao\", "
				+ "t.tp_finalidade as \"Finalidade\", "
				+ "t.nm_responsavel_cobranca as \"Cobrador\", "
				+ "t.bl_digitacao_concluida as \"Digitacao concluida\", "
				+ "t.num_itens as \"Qtd de documentos\", "
				+ "t.vl_total_fat as \"Valor documentos\", "
				+ "vl_total_fat - vl_total_desc + vl_total_juros - vl_total_tarifa - vl_total_receb_parcial as \"Total recebido\", "
				+ "t.vl_total_comp_pagto as \"Composicao pagamentos\" "
				+ "FROM ("
				+ "	 SELECT re.*,"
				+ "    (SELECT COUNT(*) FROM item_redeco ire WHERE ire.id_redeco = re.id_redeco) num_itens, "
				+ "    (SELECT nvl(SUM(fat.vl_total), 0) FROM item_redeco ire, fatura fat WHERE ire.id_redeco = re.id_redeco AND ire.id_fatura = fat.id_fatura) vl_total_fat, "
				+ "    (SELECT nvl(SUM(fat.vl_desconto), 0) FROM item_redeco ire, fatura fat WHERE ire.id_redeco = re.id_redeco AND ire.id_fatura = fat.id_fatura) vl_total_desc, "
				+ "    (SELECT nvl(SUM(ire.vl_juros), 0) FROM item_redeco ire WHERE ire.id_redeco = re.id_redeco) vl_total_juros, "
				+ "    (SELECT nvl(SUM(ire.vl_tarifa), 0) FROM item_redeco ire WHERE ire.id_redeco = re.id_redeco) vl_total_tarifa, "
				+ "    (SELECT nvl(SUM(rpp.vl_pagamento), 0) FROM item_redeco ire, relacao_pagto_parcial rpp WHERE ire.id_fatura = rpp.id_fatura AND ire.id_redeco = re.id_redeco) vl_total_receb_parcial, "
				+ "    (SELECT nvl(SUM(cpr.vl_pagamento), 0) FROM composicao_pagamento_redeco cpr WHERE cpr.id_redeco = re.id_redeco AND cpr.tp_composicao_pagamento_redeco <> 'F') vl_total_comp_pagto "
				+ "    FROM redeco re " + "    WHERE ");

		addFilters(sb, map);

		sb.append(" ) t JOIN filial f on t.id_filial = f.id_filial");

		String idFatura = map.get("fatura.idFatura").toString();
		if (idFatura != null && !idFatura.isEmpty()) {
			sb.append(" join item_redeco ir on ir.id_redeco=t.id_redeco ");
			sb.append(" join fatura fat on fat.id_fatura = ir.id_fatura ");
			sb.append(" where fat.id_fatura = " + idFatura);
		}

		return sb.toString();
	}

	
	private void addFilters(StringBuilder sb, TypedFlatMap map) {
		Long idFilial = Long.valueOf(map.get("filial.idFilial").toString());
		sb.append(" id_filial = " + idFilial);

		String tpSituacaoRedeco = (String) map.get("tpSituacaoRedeco");
		if (tpSituacaoRedeco != null && !tpSituacaoRedeco.isEmpty()) {
			sb.append(" and tp_situacao_redeco = " + tpSituacaoRedeco);
		}

		String tpFinalidade = (String) map.get("tpFinalidade");
		if (tpFinalidade != null && !tpFinalidade.isEmpty()) {
			sb.append(" and tp_finalidade = " + tpFinalidade);
		}

		String blDigicataoConcluida = (String) map.get("blDigitacaoConcluida");
		if (blDigicataoConcluida != null && !blDigicataoConcluida.isEmpty()) {
			sb.append(" and bl_digitacao_concluida = " + blDigicataoConcluida);
		}

		String nrRedeco = (String) map.get("nrRedeco");
		if (nrRedeco != null && !nrRedeco.isEmpty()) {
			sb.append(" and nr_redeco = " + nrRedeco);
		}

		String dtEmissaoInicial = map.get("dtEmissaoInicial").toString();
		if (dtEmissaoInicial != null && !dtEmissaoInicial.isEmpty()) {
			sb.append(" and dt_emissao >= to_date('" + dtEmissaoInicial + "', 'yyyy-MM-dd') ");
		}

		String dtEmissaoFinal = map.get("dtEmissaoFinal").toString();
		if (dtEmissaoFinal != null && !dtEmissaoFinal.isEmpty()) {
			sb.append(" and dt_emissao <= to_date('" + dtEmissaoFinal + "', 'yyyy-MM-dd') ");
		}

		String dtLiquidacaoInicial = map.get("dtLiquidacaoInicial").toString();
		if (dtLiquidacaoInicial != null && !dtLiquidacaoInicial.isEmpty()) {
			sb.append(" and dt_liquidacao >= to_date('" + dtLiquidacaoInicial + "', 'yyyy-MM-dd') ");
		}

		String dtLiquidacaoFinal = map.get("dtLiquidacaoFinal").toString();
		if (dtLiquidacaoFinal != null && !dtLiquidacaoFinal.isEmpty()) {
			sb.append(" and dt_liquidacao <= to_date('" + dtLiquidacaoFinal + "', 'yyyy-MM-dd') ");
		}

	}
	
	private ConfigureSqlQuery getConfigureSql() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Filial", Hibernate.STRING);
				sqlQuery.addScalar("Numero", Hibernate.LONG);
				sqlQuery.addScalar("Emissao", Hibernate.DATE);
				sqlQuery.addScalar("Recebimento", Hibernate.DATE);
				sqlQuery.addScalar("Liquidacao", Hibernate.DATE);
				sqlQuery.addScalar("Situacao", Hibernate.custom(DomainCompositeUserType.class, new String[] { "domainName" }, new String[] { "DM_STATUS_REDECO" }));
				sqlQuery.addScalar("Finalidade", Hibernate.custom(DomainCompositeUserType.class, new String[] { "domainName" }, new String[] { "DM_FINALIDADE_REDECO" }));
				sqlQuery.addScalar("Cobrador", Hibernate.STRING);
				sqlQuery.addScalar("Digitacao concluida", Hibernate.custom(DomainCompositeUserType.class, new String[] { "domainName" }, new String[] { "DM_SIM_NAO" }));
				sqlQuery.addScalar("Qtd de documentos", Hibernate.LONG);
				sqlQuery.addScalar("Valor documentos", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Total recebido", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Composicao pagamentos", Hibernate.BIG_DECIMAL);
			}
		};
		return csq;

	}

	public ResultSetPage findPaginatedRedeco(TypedFlatMap map, Integer currentPage, Integer pageSize) {
		DetachedCriteria criteria = createCriteria(map);

		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(criteria, currentPage, pageSize);
	}
	
	private DetachedCriteria createCriteria(Map map) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());

		criteria.createAlias("filial", "filial");
		criteria.createAlias("filial.pessoa", "pessoa");
		criteria.createAlias("moeda", "moeda");

		String tpSituacaoRedeco = (String) map.get("tpSituacaoRedeco");
		if (tpSituacaoRedeco != null && !tpSituacaoRedeco.isEmpty()) {
			criteria.add(Restrictions.eq("tpSituacaoRedeco", new DomainValue(tpSituacaoRedeco)));
		}

		String tpFinalidade = (String) map.get("tpFinalidade");
		if (tpFinalidade != null && !tpFinalidade.isEmpty()) {
			criteria.add(Restrictions.eq("tpFinalidade", new DomainValue(tpFinalidade)));
		}

		String blDigicataoConcluida = (String) map.get("blDigitacaoConcluida");
		if (blDigicataoConcluida != null && !blDigicataoConcluida.isEmpty()) {
			criteria.add(Restrictions.eq("blDigitacaoConcluida", blDigicataoConcluida.toString()));
		}

		Long idFilial = Long.valueOf(map.get("filial.idFilial").toString());
		criteria.add(Restrictions.eq("filial.idFilial", idFilial));

		String idFatura = map.get("fatura.idFatura").toString();
		if (idFatura != null && !idFatura.isEmpty()) {
			List<Long> idRedecos = extractIdsFromRedecos(findRedecoFromFatura(Long.parseLong(idFatura)));
			criteria.add(Restrictions.in("idRedeco", idRedecos));
		}

		String nrRedeco = (String) map.get("nrRedeco");
		if (nrRedeco != null && !nrRedeco.isEmpty()) {
			criteria.add(Restrictions.eq("nrRedeco", Long.valueOf(nrRedeco)));
		}

		YearMonthDay dtEmissaoInicial = stringToDateConverter(map.get("dtEmissaoInicial").toString());
		if (dtEmissaoInicial != null) {
			criteria.add(Restrictions.ge("dtEmissao", dtEmissaoInicial));
		}

		YearMonthDay dtEmissaoFinal = stringToDateConverter(map.get("dtEmissaoFinal").toString());
		if (dtEmissaoFinal != null) {
			criteria.add(Restrictions.le("dtEmissao", dtEmissaoFinal));
		}

		YearMonthDay dtLiquidacaoInicial = stringToDateConverter(map.get("dtLiquidacaoInicial").toString());
		if (dtLiquidacaoInicial != null) {
			criteria.add(Restrictions.ge("dtLiquidacao", dtLiquidacaoInicial));
		}

		YearMonthDay dtLiquidacaoFinal = stringToDateConverter(map.get("dtLiquidacaoFinal").toString());
		if (dtLiquidacaoFinal != null) {
			criteria.add(Restrictions.le("dtLiquidacao", dtLiquidacaoFinal));
		}

		return criteria;
	}

	private List<Long> extractIdsFromRedecos(List<Redeco> findRedecoFromFatura) {
		List<Long> ids = new ArrayList<Long>();

		for (Redeco redeco : findRedecoFromFatura) {
			ids.add(redeco.getIdRedeco());
		}

		return ids;
	}

	private List<Redeco> findRedecoFromFatura(long idFatura) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("r");

		sql.addInnerJoin(this.getPersistentClass().getName(), "r");
		sql.addInnerJoin("fetch r.itemRedecos", "itemRedecos");
		sql.addInnerJoin("fetch itemRedecos.fatura", "fatura");

		sql.addCriteria("fatura.idFatura", "=", idFatura);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	private YearMonthDay stringToDateConverter(String s) {
		if (s == null || s.isEmpty()) {
			return null;
		}
		String[] split = s.toString().split("-");
		return new YearMonthDay(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
	}
	
	/**
     * Retorna o redeco pela Fatura      
     * @author Josï¿½ Rodrigo Moraes
     * @since 11/04/2006
     * 
     * @param Long idFatura
     * @return Redeco
     */
	public List findByFatura(Long idFatura) {
		
		SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("red");
    	sql.addInnerJoin(ItemRedeco.class.getName(), "item ");
    	sql.addInnerJoin("item.redeco","red");
    	sql.addInnerJoin("fetch red.filial","fil");
    	
    	sql.addCriteria("red.tpSituacaoRedeco","!=","CA");    	
   		sql.addCriteria("item.fatura.id","=",idFatura);
    	
    	return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		
	}
	

    public List findItemRedeco(Long masterId){
    	SqlTemplate hql = mountSqlItem(masterId);
    	
    	hql.addProjection("ired");
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    public Integer getRowCountItemRedeco(Long masterId){
    	
    	SqlTemplate hql = mountSqlItem(masterId);

    	return this.getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
    }    
    
    private SqlTemplate mountSqlItem(Long idRedeco){
    	SqlTemplate hql = new SqlTemplate();

    	hql.addInnerJoin(ItemRedeco.class.getName(), "ired");
    	hql.addLeftOuterJoin("fetch ired.fatura", "fat");
    	hql.addLeftOuterJoin("fetch fat.moeda", "moe");
    	hql.addLeftOuterJoin("fetch fat.filialByIdFilial", "filfat");
    	hql.addLeftOuterJoin("fetch fat.filialByIdFilialCobradora", "filfatcob");
    	hql.addLeftOuterJoin("fetch fat.cotacaoMoeda", "cotmoefat");
    	hql.addLeftOuterJoin("fetch cotmoefat.moedaPais", "moepasfat");
    	hql.addLeftOuterJoin("fetch filfat.pessoa", "pesfat");
    	hql.addLeftOuterJoin("fetch ired.recibo", "rec");
    	hql.addLeftOuterJoin("fetch rec.filialByIdFilialEmissora", "filrec");
    	hql.addLeftOuterJoin("fetch rec.cotacaoMoeda", "cotMoe");
    	hql.addLeftOuterJoin("fetch cotMoe.moedaPais", "moePas");
    	hql.addLeftOuterJoin("fetch moePas.moeda", "moe");
    	hql.addLeftOuterJoin("fetch filrec.pessoa", "pesrec");

    	hql.addCriteria("ired.redeco.id","=",idRedeco);
    	return hql;
    }  
    
	/**
     * Retorna um map de dados por fatura que serve no relatï¿½rio 'EmitirDocumentosServicoPendenteCliente'.
     * 
     * @author Mickaï¿½l Jalbert
     * @since 28/08/2006
     * 
     * @param Long idFatura
     *  
     * @return Map
     * */
    public Map findDadosEmitirDocumentosServicoPendenteCliente(Long idFatura){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("new Map(red.nrRedeco as nrRedeco, fil.sgFilial as sgFilial)");
    	hql.addInnerJoin(ItemRedeco.class.getName(), "item ");
    	hql.addInnerJoin("item.redeco","red");
    	hql.addInnerJoin("red.filial","fil");
    	
    	hql.addCriteria("red.tpSituacaoRedeco","!=","CA");    	
    	hql.addCriteria("item.fatura.id","=",idFatura);

    	List lstTransferencia = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (!lstTransferencia.isEmpty()){
    		return (Map)lstTransferencia.get(0);
    	} else {
    		return null;
    	}
    }      
    
    /**
     * Carrega o redeco de acordo com o nrRedeco e o idFilial
     *
     * @author Hector Julian Esnaola Junior
     * @since 05/10/2007
     *
     * @param idFilial
     * @param nrRedeco
     * @return
     *
     */
	public Redeco findRedecoByIdFilialAndNrRedeco(Long idFilial, Long nrRedeco){
		SqlTemplate sql = new SqlTemplate();
    	
		sql.addProjection("r");
		
		sql.addInnerJoin(this.getPersistentClass().getName(), "r");
		sql.addInnerJoin("fetch r.filial", "fil");
		
		sql.addCriteria("fil.idFilial", "=", idFilial);
		sql.addCriteria("r.nrRedeco", "=", nrRedeco);
		
    	return (Redeco)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}
	
	public Map<String, Object> findSomatoriosRedeco(Long idRedeco) {
		String query = createSqlForSomatorio();

		Map<String, Object> namedParams = new HashMap<String, Object>();
		namedParams.put("idRedeco", idRedeco);

		return getAdsmHibernateTemplate()
				.findBySqlToMappedResult(query, namedParams, getSqlForSomatorio())
				.iterator().next();
	}

	private ConfigureSqlQuery getSqlForSomatorio() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("vl_total_fat", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("num_itens", Hibernate.LONG);
				sqlQuery.addScalar("vl_total_desc", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_total_juros", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_total_tarifa", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_total_receb_parcial", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_total_comp_pagto", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_total_recebido", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_total_recebido_sem_juros", Hibernate.BIG_DECIMAL);
			}
		};
		return csq;

	}

	private ConfigureSqlQuery getSqlForDescontosFatura() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("sumPagamento", Hibernate.BIG_DECIMAL);
			}
		};
		return csq;

	}

	private String createSqlForSomatorio() {
		StringBuilder sb = new StringBuilder();

		sb.append(" SELECT ");
		sb.append(" t.id_redeco as id_redeco, ");
		sb.append(" t.vl_total_fat as vl_total_fat, ");
		sb.append(" t.num_itens as num_itens, ");
		sb.append(" t.vl_total_desc as vl_total_desc, ");
		sb.append(" t.vl_total_juros as vl_total_juros, ");
		sb.append(" t.vl_total_tarifa as vl_total_tarifa, ");
		sb.append(" t.vl_total_receb_parcial as vl_total_receb_parcial, ");
		sb.append(" t.vl_total_comp_pagto as vl_total_comp_pagto, ");
		sb.append("        vl_total_fat - vl_total_desc + vl_total_juros - vl_total_tarifa - ");
		sb.append("        vl_total_receb_parcial vl_total_recebido,  ");

		sb.append("        vl_total_fat - vl_total_desc - vl_total_tarifa - ");
		sb.append("        vl_total_receb_parcial vl_total_recebido_sem_juros ");
		
		sb.append("   FROM (SELECT re.id_redeco, ");
		sb.append("                (SELECT COUNT(*) ");
		sb.append("                   FROM item_redeco ire ");
		sb.append("                  WHERE ire.id_redeco = re.id_redeco) num_itens, ");
		sb.append("                (SELECT nvl(SUM(fat.vl_total), 0) ");
		sb.append("                   FROM item_redeco ire, ");
		sb.append("                        fatura      fat ");
		sb.append("                  WHERE ire.id_redeco = re.id_redeco ");
		sb.append("                    AND ire.id_fatura = fat.id_fatura) vl_total_fat, ");
		sb.append("                (SELECT nvl(SUM(fat.vl_desconto), 0) ");
		sb.append("                   FROM item_redeco ire, ");
		sb.append("                        fatura      fat ");
		sb.append("                  WHERE ire.id_redeco = re.id_redeco ");
		sb.append("                    AND ire.id_fatura = fat.id_fatura) vl_total_desc, ");
		sb.append("                (SELECT nvl(SUM(ire.vl_juros), 0) ");
		sb.append("                   FROM item_redeco ire ");
		sb.append("                  WHERE ire.id_redeco = re.id_redeco) vl_total_juros, ");
		sb.append("                (SELECT nvl(SUM(ire.vl_tarifa), 0) ");
		sb.append("                   FROM item_redeco ire ");
		sb.append("                  WHERE ire.id_redeco = re.id_redeco) vl_total_tarifa, ");
		sb.append("                (SELECT nvl(SUM(rpp.vl_pagamento), 0) ");
		sb.append("                   FROM item_redeco           ire, ");
		sb.append("                        relacao_pagto_parcial rpp ");
		sb.append("                  WHERE ire.id_fatura = rpp.id_fatura ");
		sb.append("                    AND ire.id_redeco = re.id_redeco) vl_total_receb_parcial, ");
		sb.append("                (SELECT nvl(SUM(cpr.vl_pagamento), 0) ");
		sb.append("                   FROM composicao_pagamento_redeco cpr ");
		sb.append("                  WHERE cpr.id_redeco = re.id_redeco ");
		sb.append("                    AND cpr.tp_composicao_pagamento_redeco <> 'F') vl_total_comp_pagto ");
		sb.append("           FROM redeco re ");
		sb.append("          WHERE re.id_redeco = :idRedeco) t ");
		return sb.toString();
	}

	public BigDecimal findVlPagtoFatura(Long idFatura, Long idRedeco) {
		Map<String, Object> namedParams = new HashMap<String, Object>();
		namedParams.put("idFatura", idFatura);
		
		StringBuilder query = new StringBuilder();
		
		query.append(" select nvl(sum(rela.vl_pagamento), 0) as sumPagamento ");
		query.append(" from relacao_pagto_parcial rela  ");
		query.append(" where rela.id_fatura = :idFatura ");
		if (idRedeco != null)
		{
			query.append(" and (rela.id_redeco is null or rela.id_redeco <> :idRedeco) ");
			namedParams.put("idRedeco", idRedeco);
		}

		Map<String, Object> map = getAdsmHibernateTemplate()
				.findBySqlToMappedResult(query.toString(), namedParams, getSqlForDescontosFatura())
				.iterator().next();

		return (BigDecimal) map.get("sumPagamento");
	}		

	public List findRecebimentoRedeco(Long idFatura) {
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT re.id_redeco, ");
		sql.append("        fre.sg_filial, ");
		sql.append("        re.nr_redeco, ");
		sql.append("        re.tp_situacao_redeco, ");
		sql.append("        re.dt_recebimento, ");
		sql.append("        re.dt_liquidacao, ");
		sql.append("        re.tp_finalidade, ");
		sql.append("        nvl((SELECT SUM(cpr.vl_pagamento) ");
		sql.append("              FROM composicao_pagamento_redeco cpr ");
		sql.append("             WHERE cpr.id_redeco = re.id_redeco), ");
		sql.append("            0) vl_redeco, ");
		sql.append("        CASE ");
		sql.append("          WHEN fa.vl_total - fa.vl_desconto > 0 THEN ");
		sql.append("           nvl((SELECT SUM(rpp.vl_pagamento) ");
		sql.append("                 FROM relacao_pagto_parcial rpp ");
		sql.append("                WHERE rpp.id_redeco = re.id_redeco ");
		sql.append("                  AND rpp.id_fatura = ire.id_fatura), ");
		sql.append("               nvl((SELECT SUM(cpr.vl_pagamento) ");
		sql.append("                     FROM composicao_pagamento_redeco cpr ");
		sql.append("                    WHERE cpr.id_redeco = re.id_redeco), ");
		sql.append("                   0) * ((fa.vl_total - fa.vl_desconto) / ");
		sql.append("                         (SELECT SUM(fa2.vl_total - fa2.vl_desconto) ");
		sql.append("                            FROM item_redeco ire2, ");
		sql.append("                                 fatura      fa2 ");
		sql.append("                           WHERE ire2.id_redeco = re.id_redeco ");
		sql.append("                             AND ire2.id_fatura = fa2.id_fatura))) ");
		sql.append("          ELSE ");
		sql.append("           0 ");
		sql.append("        END vl_fatura ");
		sql.append("   FROM redeco      re, ");
		sql.append("        item_redeco ire, ");
		sql.append("        filial      fre, ");
		sql.append("        fatura      fa ");
		sql.append("  WHERE re.id_redeco = ire.id_redeco ");
		sql.append("    AND re.id_filial = fre.id_filial ");
		sql.append("    AND ire.id_fatura = fa.id_fatura ");
		sql.append("    AND re.tp_situacao_redeco <> 'CA' ");
		sql.append("    AND ire.id_fatura = " + idFatura);
		sql.append("  ORDER BY fre.sg_filial, ");
		sql.append("           re.nr_redeco ");

		List<Object[]> lista = getAdsmHibernateTemplate().findBySql(sql.toString(), null, getSqlQueryToString());
		List eventos = new ArrayList();
		for (int i = 0; i < lista.size(); i++) {
			Object[] tratativa = lista.get(i);
			TypedFlatMap retorno = new TypedFlatMap();
			retorno.put("idRedeco", tratativa[0]);
			retorno.put("sgFilial", tratativa[1]);
			retorno.put("nrRedeco", tratativa[2]);
			retorno.put("tpSituacaoRedeco", tratativa[3]);
			retorno.put("dtRecebimento", tratativa[4]);
			retorno.put("dtLiquidacao", tratativa[5]);
			retorno.put("tpFinalidade", tratativa[6]);
			retorno.put("vlRedeco", tratativa[7]);
			retorno.put("vlFatura", tratativa[8]);
			eventos.add(retorno);
		}

		return eventos;
	}

	private ConfigureSqlQuery getSqlQueryToString() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_redeco", Hibernate.LONG);
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
				sqlQuery.addScalar("nr_redeco", Hibernate.LONG);
				sqlQuery.addScalar("tp_situacao_redeco", Hibernate.STRING);
				sqlQuery.addScalar("dt_recebimento", Hibernate.DATE);
				sqlQuery.addScalar("dt_liquidacao", Hibernate.DATE);
				sqlQuery.addScalar("tp_finalidade", Hibernate.STRING);
				sqlQuery.addScalar("vl_redeco", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_fatura", Hibernate.BIG_DECIMAL);
			}
		};
		return csq;
	}

	public void updateLancamentoFranquiaBaixaRedeco(Long idRedeco, Long idFilial, Long nrRedeco, YearMonthDay dtLiquidacaoYMD){
		DateMidnight data = dtLiquidacaoYMD.toDateMidnight();
		Date dtLiquidacao = new Date(data.getMillis());
		try {
	    	Connection connection = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().connection();
			CallableStatement statement = connection.prepareCall("{call P_LANCTO_FRANQUIA_BAIXA_REDECO(?, ?, ?, ?) }");
			statement.setLong(1, idRedeco);
			statement.setLong(2, idFilial);
			statement.setLong(3, nrRedeco);
			statement.setDate(4, dtLiquidacao);
			statement.executeUpdate();
			
		} catch (Exception e) {
			throw new InfrastructureException(e);
			
		}
	}
	
	public void updateLancamentoBaixaParcialRedeco(Long idRedeco, Long idFilial, Long nrRedeco, Date dtRecebimento){
		try {
	    	Connection connection = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().connection();
			CallableStatement statement = connection.prepareCall("{call P_LANCTO_BAIXA_PARCIAL_REDECO(?, ?, ?, ?) }");
			statement.setLong(1, idRedeco);
			statement.setLong(2, idFilial);
			statement.setLong(3, nrRedeco);
			statement.setDate(4, dtRecebimento);
			statement.executeUpdate();
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
	}

	public List<Map<String, Object>> findTpDoctoServicoFaturaRedeco(Long idRedeco) {
		String query = createSqlForTpDoctoServicoFaturaRedeco();

		Map<String, Object> namedParams = new HashMap<String, Object>();
		namedParams.put("idRedeco", idRedeco);

		return getAdsmHibernateTemplate()
				.findBySqlToMappedResult(query, namedParams, getSqlForTpDoctoServicoFaturaRedeco());
	}

	private String createSqlForTpDoctoServicoFaturaRedeco() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select q1.id_fatura as id_fatura, docto.tp_documento_servico from ");
		sb.append(" ( ");
		sb.append("   select "); 
		sb.append("   red.id_redeco, ir.id_item_redeco, fat.id_fatura, min(itfat.id_item_fatura) as iditemfatura ");
		sb.append("   from redeco red ");
		sb.append("   join item_redeco ir on red.id_redeco = ir.id_redeco ");
		sb.append("   join fatura fat on fat.id_fatura = ir.id_fatura ");
		sb.append("   join item_fatura itfat on itfat.id_fatura = fat.id_fatura ");
		sb.append("   where red.id_redeco = :idRedeco ");
		sb.append("   group by red.id_redeco, ir.id_item_redeco, fat.id_fatura ");
		sb.append(" ) q1  ");
		sb.append(" join item_fatura itemfatura on iditemfatura = itemfatura.id_item_fatura ");
		sb.append(" join devedor_doc_serv_fat devedor on devedor.ID_DEVEDOR_DOC_SERV_FAT = itemfatura.ID_DEVEDOR_DOC_SERV_FAT ");
		sb.append(" join docto_servico docto on docto.ID_DOCTO_SERVICO = devedor.id_docto_servico ");
		
		return sb.toString();
	}
	
	
	private ConfigureSqlQuery getSqlForTpDoctoServicoFaturaRedeco() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_fatura", Hibernate.LONG);
				sqlQuery.addScalar("tp_documento_servico", Hibernate.STRING);
			}
		};
		return csq;

	}


	/**
	 * LMSA-2772
	 * Retorna o número de linhas encontradas
	 * 
	 * @param idRedeco
	 * @return
	 */
	public Integer findNroDoctosServicoFaturaRedeco(Long idRedeco) {
		String query = createSqlForCountDoctoServicoFaturaRedeco();

		Map<String, Object> namedParams = new HashMap<String, Object>();
		namedParams.put("idRedeco", idRedeco);

		List documentos = getAdsmHibernateTemplate().findBySqlToMappedResult(query, namedParams, getSqlForCountDoctoServicoFaturaRedeco());
		
		return documentos.size();
	}
	
	private String createSqlForCountDoctoServicoFaturaRedeco() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select distinct docto.tp_documento_servico, id_filial_fat from ");
		sb.append(" ( ");
		sb.append("   select "); 
		sb.append("   red.id_redeco, ir.id_item_redeco, fat.id_fatura,  ");
		sb.append("		fat.id_filial as id_filial_fat, min(itfat.id_item_fatura) as iditemfatura ");
		sb.append("   from redeco red ");
		sb.append("   join item_redeco ir on red.id_redeco = ir.id_redeco ");
		sb.append("   join fatura fat on fat.id_fatura = ir.id_fatura ");
		sb.append("   join item_fatura itfat on itfat.id_fatura = fat.id_fatura ");
		sb.append("   where red.id_redeco = :idRedeco ");
		sb.append("   group by red.id_redeco, ir.id_item_redeco, fat.id_filial, fat.id_fatura ");
		sb.append(" ) q1  ");
		sb.append(" join item_fatura itemfatura on iditemfatura = itemfatura.id_item_fatura ");
		sb.append(" join devedor_doc_serv_fat devedor on devedor.ID_DEVEDOR_DOC_SERV_FAT = itemfatura.ID_DEVEDOR_DOC_SERV_FAT ");
		sb.append(" join docto_servico docto on docto.ID_DOCTO_SERVICO = devedor.id_docto_servico ");
		
		return sb.toString();
	}
	
	private ConfigureSqlQuery getSqlForCountDoctoServicoFaturaRedeco() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("tp_documento_servico", Hibernate.STRING);
				sqlQuery.addScalar("id_filial_fat", Hibernate.LONG);
			}
		};
		return csq;

	}

}