package com.mercurio.lms.veiculoonline.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioGreenPODDAO extends AdsmDao {

	public List<Map<String, Object>> findDadosReport(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(
				mountSQL(parameters), parameters, configureSQL());
	}
	
	private String mountSQL(Map<String, Object> parameters) {
		StringBuilder sql = new StringBuilder(" SELECT /*+ index(OE OCEN_PK) */ ");
		sql = appendSelect(sql);
		sql = appendFrom(sql);
		sql = appendRelationships(sql);
		sql = appendParameters(sql,parameters);
		
		return sql.toString();
	}
	

	private StringBuilder appendParameters(StringBuilder sql, Map<String, Object> parameters) {
		sql.append(" and trunc(cast(ds.dh_emissao as date)) >= to_date(:dtPeriodoInicial, 'dd/mm/yyyy')")
		   .append(" and trunc(cast(ds.dh_emissao as date)) <=  to_date(:dtPeriodoFinal, 'dd/mm/yyyy')");
				
		if (parameters.get("idFilialOrigem") != null){
			sql.append(" and ma.id_filial_origem = :idFilialOrigem ");
		}
		
		if (parameters.get("idFilialDestino") != null){
			sql.append(" and ma.id_filial_destino = :idFilialDestino ");
		}
						
		if (parameters.get("idsNatura") != null){
			sql.append(" and ds.id_cliente_remetente in(").append(parameters.get("idsNatura")).append(")");		
		}
		
		return sql;
	}

	private StringBuilder appendSelect(StringBuilder sql) {
		return sql
			   .append(" NVL( (SELECT DECODE( NVL(dbms_lob.getlength(ce.assinatura),0) ,0,'Não','Sim' ) FROM comprovante_entrega ce WHERE ce.id_docto_servico = ds.id_docto_servico and rownum = 1) ,'Não') existe_imagem, ")
			   .append(" TRUNC(ds.dh_emissao) data_emissao, ")
			   .append(" ( SELECT fl.sg_filial FROM lms_pd.filial fl WHERE fl.id_filial = ma.id_filial_destino) filial_de_entrega, ")
			   .append(" ( SELECT ps.nr_identificacao FROM pessoa ps WHERE ps.id_pessoa = ds.id_cliente_remetente ) cnpj_remetente, ")
			   .append(" ( SELECT ps.nr_identificacao FROM pessoa ps WHERE ps.id_pessoa = ds.id_cliente_destinatario ) cnpj_destinatario, ")
			   .append(" cc.nr_controle_carga nr_controle_carga, ")
			   .append(" ( select me.nr_manifesto_entrega from manifesto_entrega me where ma.id_manifesto = me.id_manifesto_entrega ) nr_manifesto_entrega, ")
			   .append(" ( select reg.ds_regional ")
			   .append("     from regional_filial regfil, regional reg  ")
			   .append("    where regfil.id_filial                    = ma.id_filial_origem ")
			   .append("      AND reg.id_regional                     = regfil.id_regional ")
			   .append("      and trunc(sysdate) between regfil.dt_vigencia_inicial and regfil.dt_vigencia_final ")
			   .append("   ) regional, ")
			   .append(" ( SELECT mu.nm_municipio FROM municipio mu WHERE mu.id_municipio = co.id_municipio_entrega ) municipio_entrega, ")
			   .append(" ( SELECT mtr.nr_rastreador FROM meio_transporte_rodoviario mtr WHERE mtr.id_meio_transporte = cc.id_transportado ) veiculo_rastreado, ")
			   .append(" ( SELECT mt.nr_identificador FROM meio_transporte mt WHERE mt.id_meio_transporte = cc.id_transportado ) placa_veiculo, ")
			   .append(" ( SELECT fl.nr_telefone_agenda FROM filial fl WHERE fl.id_filial = ma.id_filial_destino ) nr_celular, ")
			   .append(" ( SELECT fl.sg_filial FROM filial fl WHERE fl.id_filial = ds.id_filial_origem ) filial_origem, ")
			   .append(" ds.nr_docto_servico ct_e, ")
			   .append(" ( SELECT dc.ds_valor_campo  ")
			   .append("     FROM informacao_docto_cliente ic , dados_complemento dc ")
			   .append("    WHERE dc.id_informacao_docto_cliente = ic.id_informacao_docto_cliente ")
			   .append("      AND upper(ds_campo)              = 'PEDIDO' ")
			   .append("      AND ic.id_cliente                = ds.id_cliente_remetente ")
			   .append("      AND dc.id_conhecimento           = co.id_conhecimento ) nr_pedido_natura, ")
			   .append(" CASE WHEN ( OE.CD_OCORRENCIA_ENTREGA = 1 ) THEN MD.DH_OCORRENCIA END data_entrega, ")
			   .append(" CASE WHEN ( OE.CD_OCORRENCIA_ENTREGA = 1 ) THEN MD.DH_OCORRENCIA END hora_entrega, ")
			   .append(" CASE WHEN (OE.CD_OCORRENCIA_ENTREGA = 1) THEN ( case when (MD.TP_FORMA_BAIXA = 'C') then ('Sim') ELSE 'Não' end)  END baixa_celular, ")
			   .append(" ( select P.NM_PESSOA from PESSOA p where P.ID_PESSOA = CC.ID_MOTORISTA ) nome_motorista, ")
			   .append(" (select EQ.DS_NUMERO from EQUIPAMENTO eq where EQ.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ) celular_contato, ")
			   .append(" CASE WHEN ( OE.CD_OCORRENCIA_ENTREGA  = 1 ) THEN EDS.DH_INCLUSAO END dh_inclusao_eds, ")
			   .append(" CASE WHEN ( MA.TP_MANIFESTO_ENTREGA = 'EP' ) THEN (select P_PROPRIETARIO.NM_PESSOA from PESSOA p_proprietario where P_PROPRIETARIO.ID_PESSOA = CC.ID_PROPRIETARIO ) END redespacho, ")
			   .append(" CASE WHEN ( OE.CD_OCORRENCIA_ENTREGA = 1 ) THEN (case when (MD.TP_GRAU_RECEB IS NULL ) THEN 'Sim' ELSE 'Não' end) END entregou_destinatario ");
	}

	private StringBuilder appendFrom(StringBuilder sql) {
		return sql
			   .append(" FROM docto_servico ds , ")
			   .append("      conhecimento co , ")
			   .append("      manifesto_entrega_documento md , ")
			   .append("      manifesto ma , ")
			   .append("      controle_carga cc , ")
			   .append("      EVENTO_DOCUMENTO_SERVICO eds, ")
			   .append("      OCORRENCIA_ENTREGA oe  ");
				
	}
	
	private StringBuilder appendRelationships(StringBuilder sql) {
		return sql
				.append(" where ds.id_docto_servico                 = co.id_conhecimento ")
				.append(" AND md.id_docto_servico                 = ds.id_docto_servico ")
				.append(" AND md.id_manifesto_entrega             = ma.id_manifesto ")
				.append(" and cc.id_controle_carga                = ma.id_controle_carga ")
				.append(" AND OE.ID_OCORRENCIA_ENTREGA            = MD.ID_OCORRENCIA_ENTREGA(+) ")
				.append(" AND DS.ID_DOCTO_SERVICO                 = EDS.ID_DOCTO_SERVICO ")
				.append(" AND OE.ID_OCORRENCIA_ENTREGA            = EDS.ID_OCORRENCIA_ENTREGA(+) ")
				.append(" AND eds.BL_EVENTO_CANCELADO 			  = 'N' ")
				.append(" AND eds.dh_evento 					  = md.dh_ocorrencia ");
	}
	
	private ConfigureSqlQuery configureSQL() {
		return new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("existe_imagem", Hibernate.STRING);
				sqlQuery.addScalar("data_emissao", Hibernate.DATE);
				sqlQuery.addScalar("filial_de_entrega", Hibernate.STRING);
				sqlQuery.addScalar("cnpj_remetente", Hibernate.LONG);
				sqlQuery.addScalar("cnpj_destinatario", Hibernate.LONG);
				sqlQuery.addScalar("nr_controle_carga", Hibernate.STRING);
				sqlQuery.addScalar("nr_manifesto_entrega", Hibernate.INTEGER);
				sqlQuery.addScalar("regional", Hibernate.STRING);
				sqlQuery.addScalar("municipio_entrega", Hibernate.STRING);
				sqlQuery.addScalar("veiculo_rastreado", Hibernate.STRING);
				sqlQuery.addScalar("placa_veiculo", Hibernate.STRING);
				sqlQuery.addScalar("nr_celular", Hibernate.STRING);
				sqlQuery.addScalar("filial_origem", Hibernate.STRING);
				sqlQuery.addScalar("ct_e", Hibernate.STRING);
				sqlQuery.addScalar("nr_pedido_natura", Hibernate.STRING);		
				sqlQuery.addScalar("data_entrega", Hibernate.DATE);		
				sqlQuery.addScalar("hora_entrega", Hibernate.TIMESTAMP);		
				sqlQuery.addScalar("baixa_celular", Hibernate.STRING);		
				sqlQuery.addScalar("nome_motorista", Hibernate.STRING);		
				sqlQuery.addScalar("celular_contato", Hibernate.STRING);		
				sqlQuery.addScalar("dh_inclusao_eds", Hibernate.TIMESTAMP);		
				sqlQuery.addScalar("redespacho", Hibernate.STRING);		
				sqlQuery.addScalar("entregou_destinatario", Hibernate.STRING);		
			}
		};
	}
}
