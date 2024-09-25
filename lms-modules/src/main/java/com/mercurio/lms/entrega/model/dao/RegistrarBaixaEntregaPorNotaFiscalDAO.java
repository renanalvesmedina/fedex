package com.mercurio.lms.entrega.model.dao;

import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegistrarBaixaEntregaPorNotaFiscalDAO extends AdsmDao {
	
	public Integer getRowCountNotasFiscais(TypedFlatMap criteria) {
		
		ResultSetPage<Map<String, Object>> rsp = findNotasFiscais(criteria);
		if(CollectionUtils.isEmpty(rsp.getList())){
			return 0;
		}
		return rsp.getList().size();
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String, Object>> findNotasFiscais(TypedFlatMap criteria) {
		String sql = getSqlFindNotasFiscais(criteria, Boolean.FALSE);
		return getAdsmHibernateTemplate().findPaginatedBySqlToMappedResult(sql, criteria, getConfigureSqlQueryNotasFiscais());
	}
	
	private String getSqlFindNotasFiscais(TypedFlatMap criteria, Boolean isRowCount) {
		
		if (criteria.getString("alteracao") != null && "T".equals(criteria.getString("alteracao"))) {
			return buildSqlFindNotasFiscaisAlteracao(criteria, isRowCount);
		} else {
			return buildSqlFindNotasFiscais(criteria, isRowCount);
		}

	}

	private String buildSqlFindNotasFiscaisAlteracao(TypedFlatMap criteria, Boolean isRowCount) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select nfc.id_nota_fiscal_conhecimento  ");
		if(!isRowCount){
			sql.append(" , nfc.nr_nota_fiscal ");
			sql.append(" , nfc.qt_volumes ");
			sql.append(" , nfc.ps_mercadoria ");
			sql.append(" , nfc.vl_total ");
			sql.append(" , enf.id_entrega_nota_fiscal ");
			sql.append(" , enf.qt_volumes_entregues ");
			sql.append(" , enf.dh_ocorrencia ");
			sql.append(" , oe.id_ocorrencia_entrega ");
			sql.append(" , oe.cd_ocorrencia_entrega ");
			sql.append(" , ").append(PropertyVarcharI18nProjection.createProjection(" oe.ds_ocorrencia_entrega_i ")).append(" as ds_ocorrencia_entrega ");
			sql.append(" ,  ua.nm_usuario as nmUsuario "); 
		}

		sql.append(" from nota_fiscal_conhecimento nfc  ");
		sql.append(" 	, entrega_nota_fiscal enf  ");
		sql.append(" 	, ocorrencia_entrega oe  ");
		sql.append(" 	, usuario_lms ul  ");
		sql.append(" 	, usuario_adsm ua  ");
		sql.append(" where 1 = 1 ");
		sql.append(" and nfc.ID_NOTA_FISCAL_CONHECIMENTO = enf.ID_NOTA_FISCAL_CONHECIMENTO ");
		sql.append(" and enf.ID_OCORRENCIA_ENTREGA = oe.ID_OCORRENCIA_ENTREGA ");
		sql.append(" and enf.ID_USUARIO = ul.ID_USUARIO ");
		sql.append(" and ul.ID_USUARIO = ua.ID_USUARIO ");
		sql.append(" and enf.ID_MANIFESTO(+) = :idManifesto ");
		sql.append(" and nfc.id_conhecimento = :idDoctoServico ");
		sql.append(" and NOT EXISTS (SELECT 1 FROM nota_fiscal_operada nfo ");
		sql.append(" 				 where nfo.id_nota_fiscal_cto_original = nfc.id_nota_fiscal_conhecimento ");
		sql.append(" 				 and nfo.tp_situacao in ('DV','RF') )");
		
		return sql.toString();
	}
	
	private String buildSqlFindNotasFiscais(TypedFlatMap criteria, Boolean isRowCount) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select nfc.id_nota_fiscal_conhecimento  ");
		if(!isRowCount){
			sql.append(" , nfc.nr_nota_fiscal ");
			sql.append(" , nfc.qt_volumes ");
			sql.append(" , nfc.ps_mercadoria ");
			sql.append(" , nfc.vl_total ");
			sql.append(" , null as id_entrega_nota_fiscal ");
			sql.append(" , null as qt_volumes_entregues ");
			sql.append(" , null as dh_ocorrencia ");
			sql.append(" , null as id_ocorrencia_entrega ");
			sql.append(" , null as cd_ocorrencia_entrega ");
			sql.append(" , null as ds_ocorrencia_entrega ");
			sql.append(" , null as nmUsuario "); 
		}

		sql.append(" from nota_fiscal_conhecimento nfc  ");
		sql.append(" where 1 = 1 ");
		sql.append(" and NOT EXISTS (SELECT 1 FROM nota_fiscal_operada nfo ");
		sql.append(" 				 where nfo.id_nota_fiscal_cto_original = nfc.id_nota_fiscal_conhecimento ");
		sql.append(" 				 and nfo.tp_situacao in ('DV','RF','EN') )");
		sql.append(" and NOT EXISTS (SELECT 1 FROM entrega_nota_fiscal enf ");
		sql.append(" 				 where enf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento and enf.ID_MANIFESTO(+) = :idManifesto ) ");
		sql.append(" and nfc.id_conhecimento = :idDoctoServico ");
		
		sql.append(" union ");
		
		if(!isRowCount){
			sql.append(" select nfc.id_nota_fiscal_conhecimento ");
			sql.append(" , nfc.nr_nota_fiscal ");
			sql.append(" , nfc.qt_volumes ");
			sql.append(" , nfc.ps_mercadoria ");
			sql.append(" , nfc.vl_total  ");
			sql.append(" , null as id_entrega_nota_fiscal ");
			sql.append(" , null as qt_volumes_entregues ");
			sql.append(" , null as dh_ocorrencia ");
			sql.append(" , null as id_ocorrencia_entrega ");
			sql.append(" , null as cd_ocorrencia_entrega ");
			sql.append(" , null as ds_ocorrencia_entrega ");
			sql.append(" , null as nmUsuario ");
		} else {
			sql.append(" select count(*) as rowCount ");
		}
		
		sql.append(" from manifesto_entrega_volume mev, volume_nota_fiscal vnf, nota_fiscal_conhecimento nfc ");
		sql.append(" where mev.ID_VOLUME_NOTA_FISCAL = vnf.ID_VOLUME_NOTA_FISCAL ");
		sql.append(" and vnf.ID_NOTA_FISCAL_CONHECIMENTO = nfc.ID_NOTA_FISCAL_CONHECIMENTO ");
		sql.append(" and mev.ID_OCORRENCIA_ENTREGA is null ");
		sql.append(" and mev.ID_MANIFESTO_ENTREGA = :idManifesto ");
		sql.append(" and nfc.id_conhecimento = :idDoctoServico  ");
		
		return sql.toString();
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryNotasFiscais(){
		return  new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_nota_fiscal_conhecimento", Hibernate.LONG);
				sqlQuery.addScalar("nr_nota_fiscal", Hibernate.LONG);
				sqlQuery.addScalar("qt_volumes", Hibernate.INTEGER);
				sqlQuery.addScalar("ps_mercadoria", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_total", Hibernate.BIG_DECIMAL);
				
				sqlQuery.addScalar("id_entrega_nota_fiscal", Hibernate.LONG);
				sqlQuery.addScalar("qt_volumes_entregues", Hibernate.INTEGER);
				sqlQuery.addScalar("dh_ocorrencia", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("id_ocorrencia_entrega", Hibernate.LONG);
				sqlQuery.addScalar("cd_ocorrencia_entrega", Hibernate.SHORT);
				sqlQuery.addScalar("ds_ocorrencia_entrega", Hibernate.STRING);
				sqlQuery.addScalar("nmUsuario", Hibernate.STRING);
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String, Object>> findPaginated(TypedFlatMap criteria) {
		String sql = getSqlPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginatedBySqlToMappedResult(sql, criteria, getConfigureSqlQueryPaginated());
	}
	
	private String getSqlPaginated(TypedFlatMap criteria) {
		StringBuilder sql = new StringBuilder();
		
		boolean comManifestoEntrega = criteria.getLong("idControleCarga") != null || criteria.getLong("idManifestoEntrega") != null || criteria.getLong("idDoctoServico") != null;
		boolean comManifestoViagem =  criteria.getLong("idControleCarga") != null ||  criteria.getLong("idManifestoViagem") != null || criteria.getLong("idDoctoServico") != null;
		
		if(comManifestoEntrega){
		
			sql.append(" select ds.id_docto_servico  ");
			sql.append(" , fo.sg_filial ");
			sql.append(" , ds.tp_documento_servico ");
			sql.append(" , ds.nr_docto_servico ");
			sql.append(" , fm.sg_filial as sg_filial_manifesto ");
			sql.append(" , me.id_manifesto_entrega as id_manifesto_entrega ");
			sql.append(" , me.nr_manifesto_entrega as nr_manifesto_entrega ");
			sql.append(" , null as id_manifesto_viagem ");
			sql.append(" , null as nr_manifesto_viagem ");
			sql.append(" , oe.cd_ocorrencia_entrega ");
			sql.append(" , ").append(PropertyVarcharI18nProjection.createProjection(" oe.ds_ocorrencia_entrega_i ")).append(" as ds_ocorrencia_entrega ");
			sql.append(" , med.dh_ocorrencia ");

			sql.append(" from  ");
			sql.append(" controle_carga cc,  ");
			sql.append(" manifesto m,  ");
			sql.append(" manifesto_entrega me,  ");
			sql.append(" manifesto_entrega_documento med,  ");
			sql.append(" ocorrencia_entrega oe,  ");
			sql.append(" docto_servico ds,  ");
			sql.append(" filial fo, ");
			sql.append(" filial fm ");

			sql.append(" where cc.id_controle_carga = m.id_controle_carga ");
			sql.append(" and m.id_manifesto = me.id_manifesto_entrega ");
			sql.append(" and me.id_manifesto_entrega = med.id_manifesto_entrega ");
			sql.append(" and me.id_filial = fm.id_filial ");
			sql.append(" and med.id_docto_servico  = ds.id_docto_servico ");
			sql.append(" and ds.id_filial_origem = fo.id_filial ");
			sql.append(" and med.id_ocorrencia_entrega = oe.id_ocorrencia_entrega ");
			sql.append(" and oe.cd_ocorrencia_entrega = 102 ");
			sql.append(" and m.tp_manifesto_entrega in('EN','ED','EP') ");
			sql.append(" and m.tp_status_manifesto not in('FE','CA','DC') ");
			
			if(criteria.getLong("idFilial") != null){
				sql.append(" and me.id_filial = :idFilial ");
			}
			if(criteria.getLong("idControleCarga") != null){
				sql.append(" and cc.id_controle_carga = :idControleCarga ");
			}
			if(criteria.getLong("idManifestoEntrega") != null){
				sql.append(" and me.id_manifesto_entrega = :idManifestoEntrega ");
			}
			if(criteria.getLong("idDoctoServico") != null){
				sql.append(" and ds.id_docto_servico = :idDoctoServico ");
			}
			
		}

		if(comManifestoEntrega && comManifestoViagem){
			sql.append(" union ");
		}
		
		if(comManifestoViagem){
			
			sql.append(" select ds.id_docto_servico  ");
			sql.append(" , fo.sg_filial ");
			sql.append(" , ds.tp_documento_servico ");
			sql.append(" , ds.nr_docto_servico ");
			sql.append(" , fm.sg_filial as sg_filial_manifesto ");
			sql.append(" , null as id_manifesto_entrega ");
			sql.append(" , null as nr_manifesto_entrega ");
			sql.append(" , mvn.id_manifesto_viagem_nacional as id_manifesto_viagem ");
			sql.append(" , mvn.nr_manifesto_origem as nr_manifesto_viagem ");
			sql.append(" , oe.cd_ocorrencia_entrega ");
			sql.append(" , ").append(PropertyVarcharI18nProjection.createProjection(" oe.ds_ocorrencia_entrega_i ")).append(" as ds_ocorrencia_entrega ");
			sql.append(" , eds.dh_evento as dh_ocorrencia ");

			sql.append(" from  ");
			sql.append(" controle_carga cc,  ");
			sql.append(" manifesto m,  ");
			sql.append(" manifesto_viagem_nacional mvn,  ");
			sql.append(" manifesto_nacional_cto mnc,  ");
			sql.append(" docto_servico ds,  ");
			sql.append(" filial fo, ");
			sql.append(" evento_documento_servico eds,  ");
			sql.append(" ocorrencia_entrega oe,  ");
			sql.append(" filial fm ");

			sql.append(" where cc.id_controle_carga = m.id_controle_carga ");
			sql.append(" and m.id_manifesto = mvn.id_manifesto_viagem_nacional ");
			sql.append(" and mvn.id_filial = fm.id_filial ");
			sql.append(" and mnc.id_manifesto_viagem_nacional = mvn.id_manifesto_viagem_nacional ");
			sql.append(" and mnc.id_conhecimento  = ds.id_docto_servico ");
			sql.append(" and ds.id_filial_origem = fo.id_filial ");
			sql.append(" and eds.id_docto_servico = ds.id_docto_servico ");
			sql.append(" and eds.id_ocorrencia_entrega = oe.id_ocorrencia_entrega ");
			sql.append(" and oe.cd_ocorrencia_entrega = 102 ");
			sql.append(" and m.tp_status_manifesto not in('FE','CA') ");
			sql.append(" and m.tp_manifesto_viagem = 'ED' ");
			
			if(criteria.getLong("idFilial") != null){
				sql.append(" and (m.id_filial_origem = :idFilial or m.id_filial_destino = :idFilial) ");
			}

			if(criteria.getLong("idControleCarga") != null){
				sql.append(" and cc.id_controle_carga = :idControleCarga ");
			}
			
			if(criteria.getLong("idManifestoViagem") != null){
				sql.append(" and mnc.id_manifesto_viagem_nacional = :idManifestoViagem ");
			}

			if(criteria.getLong("idDoctoServico") != null){
				sql.append(" and ds.id_docto_servico = :idDoctoServico ");
			}
			
		}
		
		return sql.toString();
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryPaginated(){
		return  new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_docto_servico", Hibernate.LONG);
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
				sqlQuery.addScalar("tp_documento_servico", Hibernate.STRING);
				sqlQuery.addScalar("nr_docto_servico", Hibernate.LONG);
				sqlQuery.addScalar("sg_filial_manifesto", Hibernate.STRING);
				sqlQuery.addScalar("id_manifesto_entrega", Hibernate.LONG);
				sqlQuery.addScalar("nr_manifesto_entrega", Hibernate.STRING);
				sqlQuery.addScalar("id_manifesto_viagem", Hibernate.LONG);
				sqlQuery.addScalar("nr_manifesto_viagem", Hibernate.STRING);
				sqlQuery.addScalar("cd_ocorrencia_entrega", Hibernate.STRING);
				sqlQuery.addScalar("ds_ocorrencia_entrega", Hibernate.STRING);
				sqlQuery.addScalar("dh_ocorrencia", Hibernate.TIMESTAMP);
				//sqlQuery.addScalar("nm_usuario", Hibernate.STRING);
			}
		};
	}
}