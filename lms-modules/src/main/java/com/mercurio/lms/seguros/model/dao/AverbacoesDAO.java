package com.mercurio.lms.seguros.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.Averbacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean com.mercurio.lms.seguros.model.dao.AverbacoesDAO
 */
public class AverbacoesDAO extends BaseCrudDao<Averbacao, Long>{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected Class getPersistentClass() {
		return Averbacao.class;
	}
	
	protected void initFindByIdLazyProperties(Map map) {
		map.put("cliente",FetchMode.JOIN);
		map.put("cliente.pessoa",FetchMode.JOIN);
		map.put("seguradora",FetchMode.JOIN);
		map.put("seguradora.pessoa",FetchMode.JOIN);
		map.put("tipoSeguro",FetchMode.JOIN);				
		map.put("filialOrigem",FetchMode.JOIN);
		map.put("filialOrigem.pessoa",FetchMode.JOIN);
		map.put("filialDestino",FetchMode.JOIN);
		map.put("filialDestino.pessoa",FetchMode.JOIN);		
		map.put("municipioOrigem",FetchMode.JOIN);
		map.put("municipioOrigem.unidadeFederativa",FetchMode.JOIN);
		map.put("municipioDestino",FetchMode.JOIN);
		map.put("municipioDestino.unidadeFederativa",FetchMode.JOIN);
		map.put("corretora",FetchMode.JOIN);
		map.put("corretora.pessoa",FetchMode.JOIN);
		map.put("meioTransporte",FetchMode.JOIN);
	}
	
	public ResultSetPage findPaginatedAverbacoes(FindDefinition findDef, TypedFlatMap tfm){
		SqlTemplate sql = getFindPaginatedQuery(tfm);
		
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idAverbacao", Hibernate.LONG);
    			sqlQuery.addScalar("nrIdentificacao", Hibernate.STRING);
    			sqlQuery.addScalar("nmPessoa", Hibernate.STRING);
    			sqlQuery.addScalar("tpModal", Hibernate.STRING);
    			sqlQuery.addScalar("tpFrete", Hibernate.STRING);
    			sqlQuery.addScalar("tpSeguro", Hibernate.STRING);
    			sqlQuery.addScalar("dtViagem", Hibernate.DATE);
    			sqlQuery.addScalar("vlEstimado", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("psTotal", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("filialOrigem", Hibernate.STRING);
    			sqlQuery.addScalar("filialDestino", Hibernate.STRING);
    			sqlQuery.addScalar("nmCorretora", Hibernate.STRING);
    			sqlQuery.addScalar("nmSeguradora", Hibernate.STRING);
    		}
    	};
    	
    	return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria(), csq);
	}

	public Integer getRowCountAverbacoes(TypedFlatMap tfm){
		SqlTemplate sql = getFindPaginatedQuery(tfm);
		
		return getAdsmHibernateTemplate().getRowCountBySql("FROM (SELECT DISTINCT AV.ID_AVERBACAO " + sql.getSql(false) + "\n)", sql.getCriteria());
	}
	
	private SqlTemplate getFindPaginatedQuery(TypedFlatMap tfm) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("AV.ID_AVERBACAO", "idAverbacao");
		sql.addProjection("P.NR_IDENTIFICACAO", "nrIdentificacao");
		sql.addProjection("P.NM_PESSOA", "nmPessoa");
		sql.addProjection("(Select vi18n(vd.DS_VALOR_DOMINIO_I) from valor_dominio vd, dominio d where vd.id_dominio = d.id_dominio and d.nm_dominio = 'DM_MODAL' and vd.vl_valor_dominio = av.tp_modal)", "tpModal");
		sql.addProjection(" (Select vi18n(vd.DS_VALOR_DOMINIO_I) from valor_dominio vd, dominio d where vd.id_dominio = d.id_dominio and d.nm_dominio = 'DM_TIPO_FRETE' and vd.vl_valor_dominio = av.tp_frete)", "tpFrete");  
		sql.addProjection("TS.SG_TIPO", "tpSeguro");
		sql.addProjection("AV.DT_VIAGEM", "dtViagem");
		sql.addProjection("AV.VL_ESTIMADO", "vlEstimado");
		sql.addProjection("AV.PS_TOTAL", "psTotal");
		sql.addProjection("FO.SG_FILIAL", "filialOrigem");
		sql.addProjection("FD.SG_FILIAL", "filialDestino");
		sql.addProjection("RG.NM_PESSOA", "nmCorretora");
		sql.addProjection("SG.NM_PESSOA", "nmSeguradora");
		
		sql.addFrom("AVERBACAO", new StringBuffer("AV")
			.append(" \n INNER JOIN PESSOA  P						ON AV.ID_CLIENTE 					= P.ID_PESSOA  " )
			.append(" \n INNER JOIN TIPO_SEGURO TS					ON AV.ID_TIPO_SEGURO                = TS.ID_TIPO_SEGURO " )
			.append(" \n INNER JOIN FILIAL FO						ON FO.ID_FILIAL						= AV.ID_FILIAL_ORIGEM " )
			.append(" \n INNER JOIN FILIAL FD						ON FD.ID_FILIAL						= AV.ID_FILIAL_DESTINO " )
			.append(" \n LEFT  JOIN PESSOA  RG						ON AV.ID_CORRETORA					= RG.ID_PESSOA  " )
			.append(" \n INNER JOIN PESSOA  SG						ON AV.ID_SEGURADORA					= SG.ID_PESSOA  " )
			.toString()
			);
		
		sql.addCriteria("AV.ID_CLIENTE", "=", tfm.getLong("cliente.idCliente"));
		sql.addCriteria("AV.TP_MODAL", "=", tfm.getString("tpModal"));
		sql.addCriteria("AV.TP_FRETE", "=", tfm.getString("tpFrete"));
		sql.addCriteria("AV.ID_TIPO_SEGURO", "=", tfm.getLong("tipoSeguro.idTipoSeguro"));
		sql.addCriteria("AV.ID_CORRETORA", "=", tfm.getLong("reguladoraSeguro.idReguladora"));
		sql.addCriteria("AV.ID_SEGURADORA", "=", tfm.getLong("seguradora.idSeguradora"));
		sql.addCriteria("AV.ID_MEIO_TRANSPORTE", "=", tfm.getLong("meioTransporteRodoviario.idMeioTransporte"));
		sql.addCriteria("AV.ID_FILIAL_ORIGEM", "=", tfm.getLong("filialOrigem.idFilial"));
		sql.addCriteria("AV.ID_FILIAL_DESTINO", "=", tfm.getLong("filialDestino.idFilial"));    	
		sql.addCriteria("AV.DT_VIAGEM", ">=", tfm.getYearMonthDay("dtInicioViagem"));
    	sql.addCriteria("AV.DT_VIAGEM", "<=", tfm.getYearMonthDay("dtFimViagem"));
		
    	sql.addOrderBy("P.NM_PESSOA");
    	sql.addOrderBy("AV.DT_VIAGEM");
    	
		return sql;
	}
	
}
