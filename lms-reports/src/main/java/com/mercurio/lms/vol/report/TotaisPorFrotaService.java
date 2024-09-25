package com.mercurio.lms.vol.report;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.session.SessionUtils;
 

/**
 * 
 * @spring.bean id="lms.vol.totaisPorFrotaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/totaisPorFrota.jasper"
 */
public class TotaisPorFrotaService extends ReportServiceSupport {
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		String query = this.montaSql(parameters);
		Map parametros = new HashMap();
		
    	YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)parameters.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)parameters.get("dataFinal"), YearMonthDay.class);
    	
    	Map filial = (Map)parameters.get("filial");
    	Long idFilial = Long.valueOf(filial.get("idFilial").toString());
    	
    	Map grupo = (Map)parameters.get("grupo");	
    	String dsGrupo = null;
    	if ((grupo.get("idGrupoFrota") != null) && (StringUtils.isNotBlank(grupo.get("idGrupoFrota").toString()))){
    		Long idGrupoFrota = Long.valueOf(grupo.get("idGrupoFrota").toString());
    		parametros.put("idGrupoFrota",idGrupoFrota);
    		dsGrupo = parameters.get("dsNome").toString(); 
    	}
    	
    	Map meioTransporte = (Map)parameters.get("meioTransporte");
    	String frotaPlaca = null;
    	if ( (meioTransporte.get("idMeioTransporte") != null) && (StringUtils.isNotBlank(meioTransporte.get("idMeioTransporte").toString())) ) {
    		Long idMeioTransporte = Long.valueOf(meioTransporte.get("idMeioTransporte").toString());
    		parametros.put("idMeioTransporte", idMeioTransporte);
    		frotaPlaca = parameters.get("frotaPlaca").toString();
    	}
    	    	
    	parametros.put("idFilial",idFilial);
    	parametros.put("dataInicial",dataInicial);
    	parametros.put("dataFinal",dataFinal);
    			
		JRReportDataObject jr = executeQuery(query, parametros);
		 
		Map parametersReport = new HashMap();
		
		SqlTemplate sql = createSqlTemplate();
		sql.addFilterSummary("filial", parameters.get("sgFilial"));
		
		if (dsGrupo != null)
			sql.addFilterSummary("grupo",dsGrupo);
		
		if ( frotaPlaca != null )
			sql.addFilterSummary("meioTransporte", frotaPlaca);  
	
		sql.addFilterSummary("dataIni", dataInicial);
		sql.addFilterSummary("dataFinal", dataFinal);
				
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,JRReportDataObject.EXPORT_XLS);
		jr.setParameters(parametersReport);
           
        return jr;
	}
	 
	
	
	public String montaSql(Map criteria){
		
		StringBuilder query = new StringBuilder()
			.append(" select ")	
			.append(" 	metr.id_meio_transporte idFrota, ")
			.append(" 	CASE WHEN equip.id_equipamento is NULL THEN metr.nr_frota ELSE metr.nr_frota || '*' END frota, ")
			.append(" 	nvl(entr.TOTAL_ENT,0) totais_e,	")
			.append(" 	nvl(cole.TOTAL_COL,0) totais_c,	")
			.append(" 	nvl(entr.TOTAL_ENT,0) + nvl(cole.TOTAL_COL,0) totais_ec, ")
			.append(" 	nvl(volm_ent.VOLUMES_ENT,0) volumes_e, ")
			.append(" 	nvl(volm_col.VOLUMES_COL,0) volumes_c, ")
			.append(" 	nvl(volm_ent.VOLUMES_ENT,0) + nvl(volm_col.VOLUMES_COL,0) volumes_ec, ")
			.append(" 	nvl(nao_real_ent.NAO_REALIZADAS_ENT,0) nao_realizadas_e, ")
			.append(" 	nvl(nao_real_col.NAO_REALIZADAS_COL,0) nao_realizadas_c, ")
			.append(" 	nvl(nao_real_ent.NAO_REALIZADAS_ENT,0) + nvl(nao_real_col.NAO_REALIZADAS_COL,0) nao_realizadas_ec, ")
			.append(" 	nvl(nao_baix_ent.NAO_BAIXADOS_ENT,0) nao_baixados_e, ")
			.append(" 	nvl(nao_baix_col.NAO_BAIXADOS_COL,0) nao_baixados_c, ")
			.append(" 	nvl(nao_baix_ent.NAO_BAIXADOS_ENT,0) + nvl(nao_baix_col.NAO_BAIXADOS_COL,0) nao_baixados_ec, ")	
			.append(" 	nvl(cham_ent.CHAMADAS_ENT,0) cham_ent, ")
			.append(" 	nvl(cham_col.CHAMADAS_COL,0) cham_col,	")
			.append(" 	nvl(cham_ent.CHAMADAS_ENT,0) + nvl(cham_col.CHAMADAS_COL,0) cham_tot,	")
			.append(" 	nvl(recusas_ent.RECUSAS_ENT,0) recusas_e, ")
			.append(" 	nvl(rec_tr_ree.REC_TR_REE,0) recusa_reentregas, ")
			.append(" 	nvl(rec_tr_dev.REC_TR_DEV,0) recusa_devolucoes, ")
			.append(" 	nvl(tot_auto.TOTAL_AUTO,0) col_automaticas,	")
			.append(" 	nvl(troca_frota.TROCA_FROTA,0) col_troc_frota, ")
			.append(" 	nvl(reent.REENTREGA,0) reentregas ")
			.append(" from meio_transporte metr ")
			.append(" left join equipamento equip on equip.id_meio_transporte = metr.id_meio_transporte ")
			.append(" left join GRUPO_FROTA_VEICULO vgf on vgf.ID_MEIO_TRANSPORTE = metr.id_meio_transporte ")
			.append(" 	left join (	")
			.append(" 		SELECT cg.id_transportado id_meio_transporte, ")
			.append(" 			   Count(med.id_manifesto_entrega_documento) TOTAL_ENT ")
			.append(" 		FROM manifesto_entrega me ")
			.append(" 			inner join filial f ON f.id_filial = me.id_filial ")
			.append(" 			inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
			.append(" 			inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
			.append(" 			inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append(" 		where F.ID_FILIAL =:idFilial ")
			.append(" 			AND me.dh_emissao between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY cg.id_transportado ")
			.append(" 	) entr on entr.iD_meio_transporte = metr.id_meio_transporte	")
			.append(" 	left join (	")
			.append(" 		SELECT ec.id_meio_transporte, ")
			.append(" 			Sum(CASE WHEN ec.tp_evento_coleta = 'EX' THEN 1 ELSE 0 END) TOTAL_COL ")
			.append(" 		FROM pedido_coleta pc ")
			.append(" 			inner join filial f ON f.id_filial = pc.id_filial_responsavel ")
			.append(" 			inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ")
			.append(" 		WHERE F.ID_FILIAL =:idFilial ")
			.append(" 			AND PC.DH_PEDIDO_COLETA between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY ec.id_meio_transporte ")
			.append(" 	) cole on cole.iD_meio_transporte = metr.id_meio_transporte	")
			.append(" 	left join ( ")
			.append(" 		SELECT cg.id_transportado, ")
			.append(" 			Sum (CASE WHEN nfc.qt_volumes IS NULL THEN 0 ELSE nfc.qt_volumes END) VOLUMES_ENT ")
			.append(" 		FROM manifesto_entrega me ")
			.append(" 			inner join filial f ON f.id_filial = me.id_filial ")
			.append(" 			inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
			.append(" 			inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
			.append(" 			inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append(" 			inner join nota_fiscal_conhecimento nfc ON nfc.id_nota_fiscal_conhecimento = med.id_nota_fiscal_conhecimento ")
			.append(" 		WHERE f.id_filial =:idFilial ")
			.append(" 			and me.dh_emissao between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY cg.id_transportado	")
			.append(" 	) volm_ent on volm_ent.id_transportado = metr.id_meio_transporte ")
			.append(" 	left join (	")
			.append(" 		SELECT ec.id_meio_transporte, ")
			.append(" 			Sum(CASE WHEN dc.qt_volumes IS NULL THEN 0 ELSE dc.qt_volumes END) VOLUMES_COL ")
			.append(" 		FROM pedido_coleta pc ")
			.append(" 			inner join filial f ON f.id_filial = pc.id_filial_responsavel ")
			.append(" 			inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ")
			.append(" 			inner join detalhe_coleta dc ON dc.id_pedido_coleta = pc.id_pedido_coleta ")
			.append(" 		WHERE f.id_filial =:idFilial ")
			.append(" 			and PC.DH_PEDIDO_COLETA between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY ec.id_meio_transporte ")
			.append(" 	) volm_col on volm_col.id_meio_transporte = metr.id_meio_transporte	")
			.append(" 	left join (	")
			.append(" 		SELECT cg.id_transportado, ")
			.append(" 			Sum(CASE WHEN oe.tp_ocorrencia = 'N' THEN 1 ELSE 0 END) NAO_REALIZADAS_ENT ")
			.append(" 		FROM manifesto_entrega me ")
			.append(" 			inner join filial f ON f.id_filial = me.id_filial ")
			.append(" 			inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
			.append(" 			inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
			.append(" 			inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append(" 			inner join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega ")
			.append(" 		WHERE f.id_filial =:idFilial ")
			.append(" 			and me.dh_emissao between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY cg.id_transportado				")
			.append(" 	) nao_real_ent on nao_real_ent.id_transportado = metr.id_meio_transporte ")
			.append(" 	left join (	")
			.append(" 		SELECT ec.id_meio_transporte, ")
			.append(" 				Sum(CASE WHEN pc.tp_status_coleta <> 'EX' THEN 1 ELSE 0 END) NAO_REALIZADAS_COL ")
			.append(" 		FROM pedido_coleta pc ")
			.append(" 			inner join filial f ON f.id_filial = pc.id_filial_responsavel ")
			.append(" 			inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ")
			.append(" 		WHERE f.id_filial =:idFilial ")
			.append(" 			and PC.DH_PEDIDO_COLETA between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY ec.id_meio_transporte ")
			.append(" 	) nao_real_col on nao_real_col.id_meio_transporte = metr.id_meio_transporte	")
			.append(" 	left join (	")
			.append(" 		SELECT cg.id_transportado, ")
			.append(" 			Sum(CASE WHEN oe.id_ocorrencia_entrega IS NULL THEN 1 ELSE 0 END) NAO_BAIXADOS_ENT ")
			.append(" 		FROM manifesto_entrega me ")
			.append(" 			inner join filial f ON f.id_filial = me.id_filial ")
			.append(" 			inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
			.append(" 			inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
			.append(" 			inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append(" 			left join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega	")
			.append(" 		WHERE f.id_filial =:idFilial ")
			.append(" 			and me.dh_emissao between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY cg.id_transportado	")
			.append(" 	) nao_baix_ent on nao_baix_ent.id_transportado = metr.id_meio_transporte ")
			.append(" 	left join (	")
			.append(" 		SELECT ec.id_meio_transporte, ")
			.append(" 			Sum(CASE WHEN pc.tp_status_coleta = 'TR' THEN 1 ELSE 0 END) NAO_BAIXADOS_COL ")
			.append(" 		FROM pedido_coleta pc ")
			.append(" 			inner join filial f ON f.id_filial = pc.id_filial_responsavel ")
			.append(" 			inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ")
			.append(" 		WHERE f.id_filial =:idFilial ")
			.append(" 			and PC.DH_PEDIDO_COLETA between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY ec.id_meio_transporte ")
			.append(" 	) nao_baix_col on nao_baix_col.id_meio_transporte = metr.id_meio_transporte	")
			.append(" 	left join (	")
			.append(" 		SELECT mt.id_meio_transporte, ")
			.append(" 			Sum(CASE WHEN ec.tp_origem = 'C' AND te.tp_tipo_evento = 'E' THEN 1 ELSE 0 END) CHAMADAS_ENT ")
			.append(" 		FROM meio_transporte mt	")
			.append(" 			left join eventos_celular ec ON mt.id_meio_transporte = ec.id_meio_transporte ")
			.append(" 			left join tipo_evento_celular te ON te.id_tipo_evento = ec.id_tipo_evento	")
			.append(" 		WHERE mt.id_filial =:idFilial ")
			.append(" 			and ec.dh_solicitacao between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY mt.id_meio_transporte ")
			.append(" 	) cham_ent on cham_ent.id_meio_transporte = metr.id_meio_transporte	")
			.append(" 	left join (	")
			.append(" 		SELECT mt.id_meio_transporte, ")
			.append(" 			Sum(CASE te.tp_tipo_evento WHEN  'C' THEN 1 ELSE 0 END) CHAMADAS_COL ")
			.append(" 		FROM meio_transporte mt	")
			.append(" 			left join eventos_celular ec ON ec.id_meio_transporte = mt.id_meio_transporte ")
			.append(" 			left join tipo_evento_celular te ON te.id_tipo_evento = ec.id_tipo_evento	")
			.append(" 		WHERE mt.id_filial =:idFilial ")
			.append(" 			AND ec.dh_solicitacao between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY mt.id_meio_transporte ")
			.append(" 	) cham_col on cham_col.id_meio_transporte = metr.id_meio_transporte	")
			.append(" 	left join (	")
			.append(" 		SELECT cg.id_transportado, ")
			.append(" 			Count(r.id_recusa) RECUSAS_ENT ")
			.append(" 		FROM controle_carga cg ")
			.append(" 			left join manifesto m ON m.id_controle_carga = cg.id_controle_carga	")
			.append(" 			left join manifesto_entrega me ON me.id_manifesto_entrega = m.id_manifesto ")
			.append(" 			left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega	")
			.append(" 			left join recusa r ON r.id_manifesto_entrega_documento = med.id_manifesto_entrega_documento ")
			.append(" 		WHERE r.id_filial =:idFilial ")
			.append(" 			AND r.dh_recusa between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY cg.id_transportado	")
			.append(" 	) recusas_ent on recusas_ent.id_transportado = metr.id_meio_transporte ")
			.append(" 	left join (	")
			.append(" 		SELECT cg.id_transportado, ")
			.append(" 			Sum(CASE WHEN r.tp_recusa = 'R' THEN 1 ELSE 0 END) REC_TR_REE ")
			.append(" 		FROM controle_carga cg ")
			.append(" 			left join manifesto m ON m.id_controle_carga = cg.id_controle_carga	")
			.append(" 			left join manifesto_entrega me ON me.id_manifesto_entrega = m.id_manifesto ")
			.append(" 			left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append(" 			left join recusa r ON r.id_manifesto_entrega_documento = med.id_manifesto_entrega_documento ")
			.append(" 		WHERE r.id_filial =:idFilial ")
			.append(" 			AND r.dh_recusa between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY cg.id_transportado				")
			.append(" 	) rec_tr_ree on rec_tr_ree.id_transportado = metr.id_meio_transporte ")
			.append(" 	left join (	")
			.append(" 		SELECT cg.id_transportado, ")
			.append(" 			Sum(CASE WHEN r.tp_recusa = 'D' THEN 1 ELSE 0 END) REC_TR_DEV ")
			.append(" 		FROM controle_carga cg ")
			.append(" 			left join manifesto m ON m.id_controle_carga = cg.id_controle_carga	")
			.append(" 			left join manifesto_entrega me ON me.id_manifesto_entrega = m.id_manifesto ")
			.append(" 			left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append(" 			left join recusa r ON r.id_manifesto_entrega_documento = med.id_manifesto_entrega_documento ")
			.append(" 		WHERE r.id_filial =:idFilial ")
			.append(" 			AND r.dh_recusa between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY cg.id_transportado	")
			.append(" 	) rec_tr_dev on rec_tr_dev.id_transportado = metr.id_meio_transporte ")
			.append(" 	left join (	")
			.append(" 		SELECT ec.id_meio_transporte, ")
			.append(" 			Sum(CASE WHEN pc.tp_modo_pedido_coleta = 'AU' THEN 1 ELSE 0 END) TOTAL_AUTO	")
			.append(" 		FROM pedido_coleta pc ")
			.append(" 			left join filial f ON f.id_filial = pc.id_filial_responsavel ")
			.append(" 			left join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta	")
			.append(" 		WHERE f.id_filial =:idFilial ")
			.append(" 			and PC.DH_PEDIDO_COLETA between :dataInicial and :dataFinal ")
			.append(" 		GROUP BY ec.id_meio_transporte ")
			.append(" 	) tot_auto on tot_auto.id_meio_transporte = metr.id_meio_transporte	")
			.append(" 	left join (	")
			.append(" 		SELECT id_meio_transporte, ")
			.append(" 			Sum(CASE WHEN tr > 1 AND ex >=1 THEN 1 ELSE 0 END) TROCA_FROTA FROM ( ")
			.append(" 				SELECT mt.id_meio_transporte, pc.id_pedido_coleta, ")
			.append(" 					Count(DISTINCT ec2.id_evento_coleta) TR, Count(DISTINCT ec1.id_evento_coleta) EX ")
			.append(" 				FROM meio_transporte mt ")
			.append(" 					left join evento_coleta ec1 ON ec1.id_meio_transporte = mt.id_meio_transporte AND ec1.tp_evento_coleta = 'EX' ")
			.append(" 					left join pedido_coleta pc  ON pc.id_pedido_coleta    = ec1.id_pedido_coleta ")
			.append(" 					left join evento_coleta ec2 ON ec2.id_pedido_coleta = pc.id_pedido_coleta AND ec2.tp_evento_coleta = 'TR' ")
			.append(" 					left join filial f ON f.id_filial = pc.id_filial_responsavel ")
			.append(" 				where f.id_filial =:idFilial ")
			.append(" 					and PC.DH_PEDIDO_COLETA between :dataInicial and :dataFinal ")
			.append(" 				GROUP BY mt.id_meio_transporte, pc.id_pedido_coleta	")
			.append(" 		) GROUP BY id_meio_transporte ")
			.append(" 	) troca_frota on troca_frota.id_meio_transporte = metr.id_meio_transporte ")
			.append(" 	left join (	")
			.append(" 		SELECT id_transportado,	")
			.append(" 			Sum(CASE WHEN TOT_ENT_REENT > 1 THEN 1 ELSE 0 END) REENTREGA FROM ( ")
			.append(" 				SELECT cg.id_transportado, med.id_docto_servico, ")
			.append(" 					Count(id_manifesto_entrega_documento) TOT_ENT_REENT	")
			.append(" 				FROM manifesto_entrega me ")
			.append(" 					inner join filial f ON f.id_filial = me.id_filial	")
			.append(" 					inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto	")
			.append(" 					inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga	")
			.append(" 					inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega	")
			.append(" 				WHERE f.id_filial =:idFilial ")
			.append(" 					and me.dh_emissao between :dataInicial and :dataFinal ")
			.append(" 				GROUP BY cg.id_transportado,med.id_docto_servico ")
			.append(" 		) GROUP BY id_transportado ")
			.append(" 	) reent on reent.id_transportado = metr.id_meio_transporte ")
			.append(" 	where nvl(entr.total_ent,0) + nvl(cole.total_col,0) > 0	")
			.append(" 	AND METR.ID_FILIAL =:idFilial ");
	
		
		if (criteria.get("tpPossuiCelular") != null){
			if ("S".equals(((String)criteria.get("tpPossuiCelular")))){
				query.append(" and equip.id_equipamento is not null ");
			} else if ("N".equals(((String)criteria.get("tpPossuiCelular")))){
				query.append(" and equip.id_equipamento is null ");
			}  
		}
		
		Map grupo = (Map)criteria.get("grupo");		
		if ( ( grupo.get("idGrupoFrota")!= null ) && ( StringUtils.isNotBlank(grupo.get("idGrupoFrota").toString()) ) ){ 
			 query.append(" and vgf.id_grupo_frota =:idGrupoFrota "); 
		}
		
		Map meioTransporte = (Map)criteria.get("meioTransporte");
    	if ( (meioTransporte.get("idMeioTransporte") != null) && (StringUtils.isNotBlank(meioTransporte.get("idMeioTransporte").toString())) ) {
    		query.append(" and METR.ID_MEIO_TRANSPORTE =:idMeioTransporte "); 
    	}
	
		query.append(" 	order by metr.nr_frota ");
		return query.toString();		
	}
	
}