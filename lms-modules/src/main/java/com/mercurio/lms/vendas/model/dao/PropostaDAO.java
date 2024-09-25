package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.vendas.model.Proposta;

/**
 * @spring.bean 
 */
public class PropostaDAO extends BaseCrudDao<Proposta, Long> {

	protected final Class getPersistentClass() {
		return Proposta.class;
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("unidadeFederativaByIdUfOrigem", FetchMode.JOIN);
		lazyFindById.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem", FetchMode.JOIN);
		lazyFindById.put("simulacao", FetchMode.JOIN);
		lazyFindById.put("parametroCliente", FetchMode.JOIN);
	}

	public Proposta findByIdSimulacao(Long idSimulacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setFetchMode("simulacao", FetchMode.JOIN);
		dc.setFetchMode("unidadeFederativaByIdUfOrigem", FetchMode.JOIN);
		dc.setFetchMode("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem", FetchMode.JOIN);
		dc.add(Restrictions.eq("simulacao.id", idSimulacao));

		return (Proposta)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public void removeByIdSimulacao(Long idSimulacao) {
		StringBuilder hql = new StringBuilder();
    	hql.append(" DELETE FROM ").append(getPersistentClass().getName());
    	hql.append(" WHERE simulacao.id = :id");
    	getAdsmHibernateTemplate().removeById(hql.toString(), idSimulacao);
	}
	
	public List<Map<String, Object>> findRelatorioPropostaPromocional16a30(Long idTabelaPreco, Long idClienteProposta, String orderBy){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idTabelaPreco", idTabelaPreco);
		parametros.put("idClienteProposta", idClienteProposta);
		return getAdsmHibernateTemplate().findBySqlToMappedResult(getQuery16a30(orderBy), parametros, ConfigureSql);
	}
	
	private String getQuery16a30(String orderBy){
		StringBuilder sql = new StringBuilder(query16a30);
		addOrderBy(sql, orderBy);
		return sql.toString();
	}
	
	public List<Map<String, Object>> findRelatorioPropostaPromocional2a15(Long idTabelaPreco, Long idClienteProposta, String orderBy){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idTabelaPreco", idTabelaPreco);
		parametros.put("idClienteProposta", idClienteProposta);
		return getAdsmHibernateTemplate().findBySqlToMappedResult(getQuery2a15(orderBy), parametros, ConfigureSql);
	}
	
	private String getQuery2a15(String orderBy){
		StringBuilder sql = new StringBuilder(query2a15);
		addOrderBy(sql, orderBy);
		return sql.toString();
	}
	
	private void addOrderBy(StringBuilder sql, String orderBy){
		if (orderBy != null) {
			sql.append(" ORDER BY ");

			if ("R".equals(orderBy)) {
				sql.append("col1, col2 ");
			} else if ("A".equals(orderBy)) {
				sql.append("col2, col1 ");
			}
		}
	}
	
	private static final String query2a15 = " SELECT origem "
  			+ " 			,col1 "
  			+ " 			,col2 "
  			+ " 			,SUM(col3) AS COL3 "
  			+ " 			,SUM(col4) AS COL4 "
  			+ " 			,SUM(col5) AS COL5 "
  			+ " 			,SUM(col6) AS COL6 "
  			+ " 			,SUM(col7) AS COL7 "
  			+ " 			,SUM(col8) AS COL8 "
  			+ " 			,SUM(col9) AS COL9 "
  			+ " 			,SUM(col10) AS COL10 "
  			+ " 			,SUM(col11) AS COL11 "
  			+ " 			,SUM(col12) AS COL12 "
  			+ " 			,SUM(col13) AS COL13 "
  			+ " 			,SUM(col14) AS COL14 "
  			+ " 			,SUM(col15) AS COL15 "
  			+ " 			,SUM(col16) AS COL16 "
  			+ " 			,SUM(col17) AS COL17 "
  			+ " 			,SUM(col18) AS COL18 "
			+ " 			FROM (SELECT aer_or.sg_aeroporto AS ORIGEM "
  			+ " 			,SUBSTR(REGEXP_SUBSTR(mun.regiao, 'pt_BR»[^¦]+'),INSTR(REGEXP_SUBSTR(mun.regiao, 'pt_BR»[^¦]+'),'pt_BR»') + LENGTH('pt_BR»')) COL1 "
  			+ " 			,(aer_de.sg_aeroporto || ' - ' || mun.nm_municipio || ' - ' || uf.sg_unidade_federativa) COL2 "
  			+ " 			,(DECODE(tpp.id_parcela_preco, 5, pf.vl_preco_frete, 0)) COL3 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0))))) COL4 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0)) COL5 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 2)) COL6 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 3)) COL7 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 4)) COL8 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 5)) COL9 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 6)) COL10 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 7)) COL11 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 8)) COL12 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 9)) COL13 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 10)) COL14 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 11)) COL15 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 12)) COL16 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 13)) COL17 "
  			+ " 			,DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) COL18 "
  			+ " 			FROM tabela_preco_parcela tpp "
  			+ " 			,preco_frete pf "
  			+ " 			,rota_preco rp "
  			+ " 			,aeroporto aer_or "
  			+ " 			,aeroporto aer_de "
  			+ " 			,unidade_federativa uf "
  			+ " 			,(SELECT DISTINCT (mun1.nm_municipio) "
   			+ " 			,aero1.id_aeroporto "
   			+ " 			,geo.ds_regiao_geografica_i regiao "
  			+ " 			FROM aeroporto          aero1 "
  			+ " 			,pessoa             pes1 "
  			+ " 			,endereco_pessoa    endp1 "
  			+ " 			,municipio          mun1 "
  			+ " 			,unidade_federativa uf "
  			+ " 			,regiao_geografica  geo "
 			+ " 			WHERE aero1.id_aeroporto = pes1.id_pessoa "
   			+ " 			AND pes1.id_pessoa = endp1.id_pessoa "
   			+ " 			AND mun1.id_unidade_federativa = uf.id_unidade_federativa "
   			+ " 			AND uf.id_regiao_geografica = geo.id_regiao_geografica "
   			+ " 			AND endp1.dt_vigencia_final > SYSDATE "
   			+ " 			AND endp1.id_municipio = mun1.id_municipio) mun "
  			+ " 			,(SELECT a.id_aeroporto "
  			+ " 			FROM filial f, aeroporto a, cliente c, pessoa p "
 			+ " 			WHERE f.id_aeroporto = a.id_aeroporto "
   			+ " 			AND c.id_filial_atende_operacional = f.id_filial "
   			+ " 			AND p.id_pessoa = c.id_cliente "
   			+ " 			AND p.id_pessoa = :idClienteProposta) aero_atnd_cli "
 			+ " 			WHERE tpp.id_tabela_preco = :idTabelaPreco "
   			+ " 			AND tpp.id_parcela_preco IN (5, 17) "
   			+ " 			AND tpp.id_tabela_preco_parcela = pf.id_tabela_preco_parcela "
   			+ " 			AND rp.id_rota_preco = pf.id_rota_preco "
   			+ " 			AND aer_or.id_aeroporto = rp.id_aeroporto_origem "
   			+ " 			AND aer_de.id_aeroporto = rp.id_aeroporto_destino "
   			+ " 			AND aer_or.id_aeroporto = aero_atnd_cli.id_aeroporto "
   			+ " 			AND uf.id_unidade_federativa = rp.id_uf_destino "
   			+ " 			AND rp.id_uf_origem IS NOT NULL "
   			+ " 			AND mun.id_aeroporto(+) = aer_de.id_aeroporto) "
			+ " 			GROUP BY ORIGEM, COL1, COL2 ";
	
	private static final String query16a30 = " SELECT origem "
  			+ " 			,col1 "
  			+ " 			,col2 "
  			+ " 			,SUM(col3) AS COL3 "
  			+ " 			,SUM(col4) AS COL4 "
  			+ " 			,SUM(col5) AS COL5 "
  			+ " 			,SUM(col6) AS COL6 "
  			+ " 			,SUM(col7) AS COL7 "
  			+ " 			,SUM(col8) AS COL8 "
  			+ " 			,SUM(col9) AS COL9 "
  			+ " 			,SUM(col10) AS COL10 "
  			+ " 			,SUM(col11) AS COL11 "
  			+ " 			,SUM(col12) AS COL12 "
  			+ " 			,SUM(col13) AS COL13 "
  			+ " 			,SUM(col14) AS COL14 "
  			+ " 			,SUM(col15) AS COL15 "
  			+ " 			,SUM(col16) AS COL16 "
  			+ " 			,SUM(col17) AS COL17 "
  			+ " 			,SUM(col18) AS COL18 "
			+ " 			FROM (SELECT aer_or.sg_aeroporto AS ORIGEM "
  			+ " 			,SUBSTR(REGEXP_SUBSTR(mun.regiao, 'pt_BR»[^¦]+'),INSTR(REGEXP_SUBSTR(mun.regiao, 'pt_BR»[^¦]+'),'pt_BR»') + LENGTH('pt_BR»')) COL1 "
  			+ " 			,(aer_de.sg_aeroporto || ' - ' || mun.nm_municipio || ' - ' || uf.sg_unidade_federativa) COL2 "
			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 14)) COL3 "
			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 15)) COL4 "
			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 16)) COL5 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 17)) COL6 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 18)) COL7 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 19)) COL8 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 20)) COL9 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 21)) COL10 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 22)) COL11 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 23)) COL12 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 24)) COL13 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 25)) COL14 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 26)) COL15 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 27)) COL16 "
  			+ " 			,((DECODE(tpp.id_parcela_preco,5,pf.vl_preco_frete,(DECODE(tpp.id_parcela_preco,17,pf.vl_preco_frete,0)))) + (DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) * 28)) COL17 "
  			+ " 			,DECODE(tpp.id_parcela_preco, 17, pf.vl_preco_frete, 0) COL18 "
  			+ " 			FROM tabela_preco_parcela tpp "
  			+ " 			,preco_frete pf "
  			+ " 			,rota_preco rp "
  			+ " 			,aeroporto aer_or "
  			+ " 			,aeroporto aer_de "
  			+ " 			,unidade_federativa uf "
  			+ " 			,(SELECT DISTINCT (mun1.nm_municipio) "
   			+ " 			,aero1.id_aeroporto "
   			+ " 			,geo.ds_regiao_geografica_i regiao "
  			+ " 			FROM aeroporto          aero1 "
  			+ " 			,pessoa             pes1 "
  			+ " 			,endereco_pessoa    endp1 "
  			+ " 			,municipio          mun1 "
  			+ " 			,unidade_federativa uf "
  			+ " 			,regiao_geografica  geo "
 			+ " 			WHERE aero1.id_aeroporto = pes1.id_pessoa "
   			+ " 			AND pes1.id_pessoa = endp1.id_pessoa "
   			+ " 			AND mun1.id_unidade_federativa = uf.id_unidade_federativa "
   			+ " 			AND uf.id_regiao_geografica = geo.id_regiao_geografica "
   			+ " 			AND endp1.dt_vigencia_final > SYSDATE "
   			+ " 			AND endp1.id_municipio = mun1.id_municipio) mun "
  			+ " 			,(SELECT a.id_aeroporto "
  			+ " 			FROM filial f, aeroporto a, cliente c, pessoa p "
 			+ " 			WHERE f.id_aeroporto = a.id_aeroporto "
   			+ " 			AND c.id_filial_atende_operacional = f.id_filial "
   			+ " 			AND p.id_pessoa = c.id_cliente "
   			+ " 			AND p.id_pessoa = :idClienteProposta) aero_atnd_cli "
 			+ " 			WHERE tpp.id_tabela_preco = :idTabelaPreco "
   			+ " 			AND tpp.id_parcela_preco IN (5, 17) "
   			+ " 			AND tpp.id_tabela_preco_parcela = pf.id_tabela_preco_parcela "
   			+ " 			AND rp.id_rota_preco = pf.id_rota_preco "
   			+ " 			AND aer_or.id_aeroporto = rp.id_aeroporto_origem "
   			+ " 			AND aer_de.id_aeroporto = rp.id_aeroporto_destino "
   			+ " 			AND aer_or.id_aeroporto = aero_atnd_cli.id_aeroporto "
   			+ " 			AND uf.id_unidade_federativa = rp.id_uf_destino "
   			+ " 			AND rp.id_uf_origem IS NOT NULL "
   			+ " 			AND mun.id_aeroporto(+) = aer_de.id_aeroporto) "
			+ " 			GROUP BY ORIGEM, COL1, COL2 ";
	
	
	final ConfigureSqlQuery ConfigureSql = new ConfigureSqlQuery() {
		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
			sqlQuery.addScalar("ORIGEM", Hibernate.STRING);
			sqlQuery.addScalar("COL1", Hibernate.STRING);
			sqlQuery.addScalar("COL2", Hibernate.STRING);
			sqlQuery.addScalar("COL3", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL4", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL5", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL6", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL7", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL8", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL9", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL10", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL11", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL12", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL13", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL14", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL15", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL16", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL17", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL18", Hibernate.BIG_DECIMAL);
		}
	};
	
	final ConfigureSqlQuery ConfigureSqlProdutoEspecifico = new ConfigureSqlQuery() {
		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
			sqlQuery.addScalar("COL1", Hibernate.STRING);
			sqlQuery.addScalar("COL2", Hibernate.STRING);
			sqlQuery.addScalar("COL3", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("COL4", Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("DSPRODUTOESPECIFICO", Hibernate.STRING);
			sqlQuery.addScalar("NRTARIFAESPECIFICA", Hibernate.STRING);
		}
	};
	
	public List<Map<String, Object>> findRelatorioPropostaPromocionalProdutoEspecifico(Long idSimulacao, Long idCliente, String orderBy){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idSimulacao", idSimulacao);
		parametros.put("idCliente", idCliente);
		return getAdsmHibernateTemplate().findBySqlToMappedResult(getQueryProdutoEspecifico(orderBy), parametros, ConfigureSqlProdutoEspecifico);
	}
	
	private String getQueryProdutoEspecifico(String orderBy){
		StringBuilder sql = new StringBuilder(queryProdutoEspecifico);
		addOrderByProdutoEspecifico(sql, orderBy);
		return sql.toString();
	}
	
	private void addOrderByProdutoEspecifico(StringBuilder sql, String orderBy){
		if (orderBy != null) {
			sql.append(" ORDER BY ");

			if ("R".equals(orderBy)) {
				sql.append("mun.regiao, (aer_de.sg_aeroporto ||' - '||mun.nm_municipio|| ' - ' || uf.sg_unidade_federativa) ");
			} else if ("A".equals(orderBy)) {
				sql.append("(aer_de.sg_aeroporto ||' - '||mun.nm_municipio|| ' - ' || uf.sg_unidade_federativa), mun.regiao ");
			}
		}
	}
	
	private static final String queryProdutoEspecifico = " SELECT  "
			+ "			SUBSTR(REGEXP_SUBSTR(mun.regiao, 'pt_BR»[^¦]+'), "
			+ " 		INSTR(REGEXP_SUBSTR(mun.regiao, 'pt_BR»[^¦]+'), "
			+ " 		'pt_BR»') +LENGTH('pt_BR»')) COL1, "
			+ "     	(aer_de.sg_aeroporto||' - '||mun.nm_municipio||' - ' ||uf.sg_unidade_federativa) AS COL2,  "
			+ "     	pf.vl_preco_frete AS COL3, "
			+ "     	vfp.vl_fixo AS COL4,  "
			+ "			SUBSTR(REGEXP_SUBSTR(pe.ds_produto_especifico_i, 'pt_BR»[^¦]+'), "
			+ " 		INSTR(REGEXP_SUBSTR(pe.ds_produto_especifico_i, 'pt_BR»[^¦]+'), "
			+ " 		'pt_BR»') +LENGTH('pt_BR»')) AS DSPRODUTOESPECIFICO, "
			+ "     	pe.nr_tarifa_especifica AS NRTARIFAESPECIFICA "
			+ "     FROM  "
			+ "     	(SELECT DISTINCT(mun1.nm_municipio) "
			+ "                , aero1.id_aeroporto "
			+ "                , geo.ds_regiao_geografica_i regiao "
			+ "             FROM aeroporto       aero1 "
			+ "                , pessoa          pes1 "
			+ "                , endereco_pessoa endp1 "
			+ "                , municipio       mun1 "
			+ "                , unidade_federativa uf "
			+ "                , regiao_geografica geo "
			+ "            WHERE aero1.id_aeroporto      = pes1.id_pessoa "
			+ "              AND pes1.id_pessoa          = endp1.id_pessoa "
			+ "              AND mun1.id_unidade_federativa = uf.id_unidade_federativa "
			+ "              AND uf.id_regiao_geografica = geo.id_regiao_geografica "
			+ "              AND endp1.dt_vigencia_final > SYSDATE "
			+ "              AND endp1.id_municipio      = mun1.id_municipio) mun, "
			+ "     	(SELECT a.id_aeroporto "
			+ "             FROM filial f "
			+ "                , aeroporto a "
			+ "                , cliente c "
			+ "                , pessoa p "
			+ "            WHERE f.id_aeroporto = a.id_aeroporto "
			+ "              AND c.id_filial_atende_operacional = f.id_filial "
			+ "              AND p.id_pessoa = c.id_cliente "
			+ "              AND p.id_pessoa = :idCliente) aero_atnd_cli, "
			+ "     	simulacao sim, "
			+ "     	filial f, "
			+ "     	tabela_preco tp, "
			+ "     	tabela_preco_parcela tpp, "
			+ "     	faixa_progressiva fp, "
			+ "     	valor_faixa_progressiva vfp, "
			+ "     	produto_especifico pe, "
			+ "     	tabela_preco_parcela tpp_min, "
			+ "     	preco_frete pf, "
			+ "     	rota_preco rp, "
			+ "     	aeroporto aer_or, "
			+ "     	aeroporto aer_de, "
			+ "     	unidade_federativa uf "
			+ "     WHERE f.id_filial = sim.id_filial "
			+ "     AND tp.id_tabela_preco = sim.id_tabela_preco "
			+ "     AND tpp.id_tabela_preco = tp.id_tabela_preco "
			+ "     AND (fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela AND fp.id_produto_especifico = sim.id_produto_especifico) "
			+ "     AND vfp.id_faixa_progressiva = fp.id_faixa_progressiva "
			+ "     AND pe.id_produto_especifico = sim.id_produto_especifico "
			+ "     AND tpp_min.id_tabela_preco = tp.id_tabela_preco "
			+ "     AND pf.id_tabela_preco_parcela = tpp_min.id_tabela_preco_parcela "
			+ "     AND (rp.id_rota_preco = vfp.id_rota_preco AND rp.id_rota_preco = pf.id_rota_preco) "
			+ "     AND aer_or.id_aeroporto = rp.id_aeroporto_origem "
			+ "     AND aer_de.id_aeroporto = rp.id_aeroporto_destino "
			+ "     AND uf.id_unidade_federativa = rp.id_uf_destino "
			+ "     AND aer_or.id_aeroporto = aero_atnd_cli.id_aeroporto "
			+ "     AND mun.id_aeroporto = aer_de.id_aeroporto "
			+ "     AND tpp_min.id_parcela_preco = 5  "
			+ "     AND sim.id_simulacao = :idSimulacao ";
	
}