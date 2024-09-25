package com.mercurio.lms.ppd.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.ppd.model.enums.PpdTipoDataRecibo;
import com.mercurio.lms.ppd.model.service.PpdGrupoAtendimentoService;
import com.mercurio.lms.ppd.model.service.PpdNaturezaProdutoService;
import com.mercurio.lms.util.session.SessionUtils;

import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;

public class PpdExcelRecibosService extends ReportServiceSupport {	
	private FilialService filialService;
	private PessoaService pessoaService;
	private PpdGrupoAtendimentoService grupoAtendimentoService;
	private PpdNaturezaProdutoService naturezaProdutoService;
	private DomainValueService domainValueService;

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		SqlTemplate sql = this.createSqlTemplate();	
		//Tipo de data
		if(parameters.get("dtReciboInicial") != null && parameters.get("dtReciboFinal") != null) 
			sql.addFilterSummary("tipoData", PpdTipoDataRecibo.valueOf((String)parameters.get("sgTipoDataRecibo")).getDescription());
		//Intervalo de datas
		sql.addFilterSummary("dataInicio", parameters.get("dtReciboInicial"));
		sql.addFilterSummary("dataFim", parameters.get("dtReciboFinal"));		
		//Natureza do Produto
		if(parameters.get("idNaturezaProduto") != null)
			sql.addFilterSummary("naturezaProduto", 
					naturezaProdutoService.findById((Long)parameters.get("idNaturezaProduto")).getDsNaturezaProduto());		
		//Filial Recibo
		if(parameters.get("idFilial") != null) {
			sql.addFilterSummary("filial", 
					filialService.findById((Long)parameters.get("idFilial")).getSgFilial());
		}	
		//Número do recibo
		sql.addFilterSummary("recibo", parameters.get("nrRecibo"));
		//Status do recibo
		if(parameters.get("tpStatus") != null)
			sql.addFilterSummary("status", domainValueService.findDomainValueByValue(
					"DM_PPD_STATUS_INDENIZACAO",(String)parameters.get("tpStatus")).getDescriptionAsString());
		//Tipo de Indenização
		if(parameters.get("tpIndenizacao") != null)
			sql.addFilterSummary("status", domainValueService.findDomainValueByValue(
					"DM_PPD_TIPO_INDENIZACAO",(String)parameters.get("tpIndenizacao")).getDescriptionAsString());
		//Intervalo de valor
		if(parameters.get("vlIndenizacaoInicial") != null)
			sql.addFilterSummary("valorInicial", ((BigDecimal)parameters.get("vlIndenizacaoInicial")).setScale(2));
		if(parameters.get("vlIndenizacaoInicial") != null)
			sql.addFilterSummary("valorFinal", ((BigDecimal)parameters.get("vlIndenizacaoFinal")).setScale(2));	
		//Conhecimento
		sql.addFilterSummary("filialOrigem", parameters.get("sgFilialOrigem") == null ? parameters.get("sgFilialOrigem") : parameters.get("sgFilialOrigem").toString().toUpperCase());
		sql.addFilterSummary("ctrc", parameters.get("nrCtrc"));

		//RNC
		sql.addFilterSummary("filialEmissoraRNC", parameters.get("sgFilialRnc") == null ? parameters.get("sgFilialRnc") : parameters.get("sgFilialRnc").toString().toUpperCase());
		sql.addFilterSummary("rnc", parameters.get("nrRnc"));		

		//Local sinistro		
		if(parameters.get("tpLocalidade") != null)
			sql.addFilterSummary("localOcorrencia", domainValueService.findDomainValueByValue(
					"DM_PPD_LOCAL_SINISTRO",(String)parameters.get("tpLocalidade")).getDescriptionAsString());
		//Filial / Rota
		sql.addFilterSummary("filialRota", parameters.get("sgFilialLocal1") == null ? parameters.get("sgFilialLocal1") : parameters.get("sgFilialLocal1").toString().toUpperCase());
		sql.addFilterSummary("filialRota", parameters.get("sgFilialLocal2") == null ? parameters.get("sgFilialLocal2") : parameters.get("sgFilialLocal2").toString().toUpperCase());
		
		//Favorecido
		if(parameters.get("idPessoa") != null) {
			sql.addFilterSummary("cliente", 
					pessoaService.findById((Long) parameters.get("idPessoa")).getNmFantasia());
		}		
		//Grupo de Atendimento		
		if(parameters.get("idGrupoAtendimento") != null) {
			sql.addFilterSummary("grupoAtendimento", 
					grupoAtendimentoService.findById((Long)parameters.get("idGrupoAtendimento")).getDsGrupoAtendimento());
		}
				
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append("		 t.*,");
		query.append("       nvl((SELECT upper(p.nm_pessoa)");
		query.append("             FROM pessoa p");
		query.append("            WHERE p.nr_identificacao =");
		query.append("                  lpad(t.clib_pess_id_remetente, 14, '0')");
		query.append("              AND p.tp_identificacao = 'CNPJ'");
		query.append("              AND rownum = 1),");
		query.append("           (SELECT upper(p.nm_pessoa)");
		query.append("              FROM pessoa p");
		query.append("             WHERE p.nr_identificacao =");
		query.append("                   lpad(t.clib_pess_id_remetente, 11, '0')");
		query.append("               AND p.tp_identificacao = 'CPF'");
		query.append("               AND rownum = 1)) nm_pessoa_emit,");
		query.append("       nvl((SELECT upper(p.nm_pessoa)");
		query.append("             FROM pessoa p");
		query.append("            WHERE p.nr_identificacao =");
		query.append("                  lpad(t.clib_pess_id_destinatario, 14, '0')");
		query.append("              AND p.tp_identificacao = 'CNPJ'");
		query.append("              AND rownum = 1),");
		query.append("           (SELECT upper(p.nm_pessoa)");
		query.append("              FROM pessoa p");
		query.append("             WHERE p.nr_identificacao =");
		query.append("                   lpad(t.clib_pess_id_destinatario, 11, '0')");
		query.append("               AND p.tp_identificacao = 'CPF'");
		query.append("               AND rownum = 1)) nm_pessoa_dest,");
		query.append("               rec.NR_RECIBO_INDENIZACAO nrRecibo,");
		query.append("               to_char(rec.DT_RECIBO, 'DD/MM/YYYY') dtRecibo,");
		query.append("               fil.SG_FILIAL sgFilial,");
		query.append("               rec.SG_FILIAL_ORIGEM sgFilialOrigem,");
		query.append("               rec.NR_CTRC nrCtrc,");
		query.append("               rec.SG_FILIAL_RNC sgFilialRnc,");
		query.append("               rec.NR_RNC nrRnc,");
		query.append("               to_char(rec.DT_EMISSAO_CTRC, 'DD/MM/YYYY') dtEmissaoCtrc,");
		query.append("               to_char(rec.DT_EMISSAO_RNC, 'DD/MM/YYYY') dtEmissaoRnc,");
		query.append("               to_char(rec.DT_PROGRAMADA_PAGAMENTO, 'DD/MM/YYYY') dtPagto,");
		query.append("               rec.VL_INDENIZACAO vlIndenizacao,");
		query.append("               nat.DS_NATUREZA_PRODUTO dsNaturezaProduto,");              
		query.append("               pben.NM_PESSOA nmPessoa,");
		query.append("               (select vI18n(ds_valor_dominio_i) from valor_dominio where id_dominio in (select id_dominio from dominio where nm_dominio = 'DM_PPD_STATUS_INDENIZACAO') and vl_valor_dominio = rec.TP_STATUS) dsTpStatus,");
		query.append("               (select vI18n(ds_valor_dominio_i) from valor_dominio where id_dominio in (select id_dominio from dominio where nm_dominio = 'DM_PPD_TIPO_INDENIZACAO') and vl_valor_dominio = rec.TP_INDENIZACAO) dsTpIndenizacao,");
		query.append("               (select vI18n(ds_valor_dominio_i) from valor_dominio where id_dominio in (select id_dominio from dominio where nm_dominio = 'DM_PPD_LOCAL_SINISTRO') and vl_valor_dominio = rec.TP_LOCALIDADE) dsTpLocalidade,");
		query.append("               rec.SG_FILIAL_LOCAL1 || ' ' || rec.SG_FILIAL_LOCAL2 dsFilialRota,");
		query.append("               u_rec.NM_USUARIO nmURec,");
		query.append("               s_rec.OB_STATUS_RECIBO obRec,");
		query.append("               u_dev.NM_USUARIO nmUDev,");
		query.append("               u_can.NM_USUARIO nmUCan,");
		query.append("               u_lib.NM_USUARIO nmULib,");
		query.append("               u_env.NM_USUARIO nmUEnv,");
		query.append("               s_dev.OB_STATUS_RECIBO obDev,");
		query.append("               s_can.OB_STATUS_RECIBO obCan,");
		query.append("               s_lib.OB_STATUS_RECIBO obLib,");
		query.append("               s_env.OB_STATUS_RECIBO obEnv,");
		query.append("               to_char(s_rec.DH_STATUS_RECIBO, 'DD/MM/YYYY') dhRec,");
		query.append("               to_char(s_dev.DH_STATUS_RECIBO, 'DD/MM/YYYY') dhDev,");
		query.append("               to_char(s_can.DH_STATUS_RECIBO, 'DD/MM/YYYY') dhCan,");
		query.append("               to_char(s_lib.DH_STATUS_RECIBO, 'DD/MM/YYYY') dhLib,");
		query.append("               to_char(s_env.DH_STATUS_RECIBO, 'DD/MM/YYYY') dhEnv,");
		query.append("				 (SELECT wm_concat(DISTINCT nfcon.numero) ");
		query.append("				 	FROM notas_fiscais_ctos nfcon ");
		query.append("				 WHERE t.unid_sigla_origem = nfcon.ctos_unid_sigla ");
		query.append("					AND t.numero = nfcon.ctos_numero ");
		query.append("					AND rownum <= 10) nrnf ");
		query.append("  FROM ");
		query.append("  	(SELECT rec.id_recibo,");
		query.append("				 con.unid_sigla_origem,");
		query.append("				 con.numero,");
		query.append("               MAX(s_rec.id_status_recibo) id_status_recibo_rec,");
		query.append("               MAX(s_dev.id_status_recibo) id_status_recibo_dev,");
		query.append("               MAX(s_can.id_status_recibo) id_status_recibo_can,");
		query.append("               MAX(s_lib.id_status_recibo) id_status_recibo_lib,");
		query.append("               MAX(s_env.id_status_recibo) id_status_recibo_env,");
		query.append("               nvl(con.unid_sigla_destino, con_res.unid_sigla_destino) unid_sigla_destino,");
		query.append("               nvl(con.clib_pess_id_remetente, con_res.rcli_id_remetente) clib_pess_id_remetente,");
		query.append("               nvl(con.clib_pess_id_destinatario,");
		query.append("				 con_res.rcli_id_destinatario) clib_pess_id_destinatario");
		query.append("          FROM ");
		query.append("          (SELECT r.*,");
		query.append("                       substr(lpad(r.nr_ctrc, 10, '0'), 5, 6) nr_ctrc_join");
		query.append("                  FROM ppd_recibos r) rec,");
		query.append("               (SELECT c.*,");
		query.append("                       substr(lpad(c.numero, 10, '0'), 5, 6) nr_ctrc,");
		query.append("                       trunc(c.dthr_emissao) dt_emissao_ctrc");
		query.append("                  FROM conhecimentos c) con,");
		query.append("               (SELECT c.*,");
		query.append("                       substr(lpad(c.numero, 10, '0'), 5, 6) nr_ctrc,");
		query.append("                       trunc(c.dthr_emissao) dt_emissao_ctrc");
		query.append("                  FROM res_conhecimentos c) con_res,");
		query.append("               ppd_status_recibos s_rec,");
		query.append("               ppd_status_recibos s_dev,");
		query.append("               ppd_status_recibos s_can,");
		query.append("               ppd_status_recibos s_lib,");
		query.append("               ppd_status_recibos s_env");
		query.append("         WHERE rec.sg_filial_origem = con.unid_sigla_origem(+)");
		query.append("           AND rec.nr_ctrc_join = con.nr_ctrc(+)");
		query.append("           AND rec.dt_emissao_ctrc = con.dt_emissao_ctrc(+)");
		query.append("           AND rec.sg_filial_origem = con_res.unid_sigla_origem(+)");
		query.append("           AND rec.nr_ctrc_join = con_res.nr_ctrc(+)");
		query.append("           AND rec.dt_emissao_ctrc = con_res.dt_emissao_ctrc(+)");
		query.append("           AND rec.id_recibo = s_rec.id_recibo(+)");
		query.append("           AND s_rec.tp_status_recibo(+) = 'R'");
		query.append("           AND rec.id_recibo = s_dev.id_recibo(+)");
		query.append("           AND s_dev.tp_status_recibo(+) = 'F'");
		query.append("           AND rec.id_recibo = s_can.id_recibo(+)");
		query.append("           AND s_can.tp_status_recibo(+) = 'C'");
		query.append("           AND rec.id_recibo = s_lib.id_recibo(+)");
		query.append("           AND s_lib.tp_status_recibo(+) = 'L'");
		query.append("           AND rec.id_recibo = s_env.id_recibo(+)");
		query.append("           AND s_env.tp_status_recibo(+) = 'E'");

		if(MapUtils.getObject(parameters, "idRecibo") != null) {
			query.append(" and rec.id_recibo = :idRecibo");
	}

		StringBuilder sbRecibos = null;
		if (MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.LOTE.toString()) ||
				 MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.ENVIO.toString()) ||					 
				 MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECEBIMENTO.toString())) {
			sbRecibos = new StringBuilder();

			sbRecibos.append(" and exists (");
			sbRecibos.append("select status.id_recibo from ppd_status_recibos status ");				
			sbRecibos.append("where status.id_recibo = rec.id_recibo ");
			//Adição ao lote
			if (MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.LOTE.toString()))
				sbRecibos.append("and status.tp_status_recibo = 'L' ");
			//Envio JDE
			if (MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.ENVIO.toString()))
				sbRecibos.append("and status.tp_status_recibo = 'E' ");			
			//Recebimento documentação
			if (MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECEBIMENTO.toString()))
				sbRecibos.append("and status.tp_status_recibo = 'R' ");
		}	
		
		
		if(MapUtils.getObject(parameters, "dtReciboInicial") != null) {
			//Data de emissão do CTRC
			if(MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.CTRC.toString()))
				query.append(" and rec.dt_emissao_ctrc >= :dtReciboInicial ");
			//Data de emissão do Recibo
			else if(MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECIBO.toString()))
				query.append(" and rec.dt_recibo >= :dtReciboInicial ");
			//Data de emissão do RNC
			else if(MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RNC.toString()))
				query.append(" and rec.dt_emissao_rnc >= :dtReciboInicial ");
			//Data de pagamento
			else if(MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.PAGAMENTO.toString()))
				query.append("and rec.dt_pagamento_efetuado >= :dtReciboInicial ");
			//Filtro por data de status
			else if (MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.LOTE.toString()) ||
					 MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.ENVIO.toString()) ||					 
					 MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECEBIMENTO.toString())) {
				sbRecibos.append("and trunc(status.dh_status_recibo) >= :dtReciboInicial ");
			}			
		}
		if(MapUtils.getObject(parameters, "dtReciboFinal") != null) {
			//Data de emissão do CTRC
			if(MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.CTRC.toString()))
				query.append(" and rec.dt_emissao_ctrc <= :dtReciboFinal ");
			//Data de emissão do Recibo
			else if(MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECIBO.toString()))
				query.append(" and rec.dt_recibo <= :dtReciboFinal ");
			//Data de emissão do RNC
			else if(MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RNC.toString()))
				query.append(" and rec.dt_emissao_rnc <= :dtReciboFinal ");
			//Data de pagamento
			else if (MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.PAGAMENTO.toString()))
				query.append("and rec.dt_pagamento_efetuado <= :dtReciboFinal ");
			//Data de adição do Recibo no lote 
			else if (MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.LOTE.toString()) ||
					 MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.ENVIO.toString()) ||					 
					 MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECEBIMENTO.toString())) {
				sbRecibos.append("and trunc(status.dh_status_recibo) <= :dtReciboFinal ");
			}					
		}
		
		if (MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.LOTE.toString()) ||
				 MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.ENVIO.toString()) ||					 
				 MapUtils.getString(parameters, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECEBIMENTO.toString())) {
			sbRecibos.append(")");
			query.append(sbRecibos);
		}
		
		if(MapUtils.getObject(parameters, "vlIndenizacaoInicial") != null) { 
			query.append(" and rec.vl_indenizacao >= :vlIndenizacaoInicial");
		}
		if(MapUtils.getObject(parameters, "vlIndenizacaoFinal") != null) {
			query.append(" and rec.vl_indenizacao <= :vlIndenizacaoFinal");		
		}
		if(MapUtils.getObject(parameters, "idFilial") != null) {
			query.append(" and rec.id_filial = :idFilial");		
		}
		if(MapUtils.getObject(parameters, "sgFilialOrigem") != null) {			
			query.append(" and rec.sg_filial_origem like :sgFilialOrigem");
		}
		if(MapUtils.getObject(parameters, "nrCtrc") != null) {
			query.append(" and rec.nr_ctrc = :nrCtrc");
		}
		if(MapUtils.getObject(parameters, "sgFilialRnc") != null) {			
			query.append(" and rec.sg_filial_rnc like :sgFilialRnc");
		}
		if(MapUtils.getObject(parameters, "nrRnc") != null) {
			query.append(" and rec.nr_rnc = :nrRnc");
		}
		if(MapUtils.getObject(parameters, "tpStatus") != null) {
			query.append(" and rec.tp_status = :tpStatus");
		}		
		if(MapUtils.getObject(parameters, "idPessoa") != null) {
			query.append(" and rec.id_pessoa = :idPessoa");
		}
		if(MapUtils.getObject(parameters, "tpIndenizacao") != null) {
			query.append(" and rec.tp_indenizacao = :tpIndenizacao");
		}
		if(MapUtils.getObject(parameters, "nrRecibo") != null) {
			query.append(" and rec.nr_recibo_indenizacao = :nrRecibo");
		}
		if(MapUtils.getObject(parameters, "idNaturezaProduto") != null) {
			query.append(" and rec.id_natureza_produto = :idNaturezaProduto");
		}
		if(MapUtils.getObject(parameters, "tpLocalidade") != null) {
			query.append(" and rec.tp_localidade = :tpLocalidade");
		}
		if(MapUtils.getObject(parameters, "sgFilialLocal1") != null) {
			query.append(" and rec.sg_filial_local1 like :sgFilialLocal1");
		}
		if(MapUtils.getObject(parameters, "sgFilialLocal2") != null) {
			query.append(" and rec.sg_filial_local2 like :sgFilialLocal2");
		}		
		if(MapUtils.getObject(parameters, "idsFiliais") != null) {			
			List<Long> filiais = (List<Long>)parameters.get("idsFiliais"); 			
			query.append(" and rec.id_filial in (");
			for(int i=0; i<filiais.size(); i++) {			
				query.append(filiais.get(i));				
				if(i != filiais.size() - 1)
					query.append(",");
				else
					query.append(")");
			}
		}		
		query.append("         GROUP BY rec.id_recibo,");
		query.append("		 con.unid_sigla_origem,");
		query.append("		 con.numero,");
		query.append("       nvl(con.unid_sigla_destino, con_res.unid_sigla_destino),");
		query.append("       nvl(con.clib_pess_id_remetente, con_res.rcli_id_remetente),");
		query.append("       nvl(con.clib_pess_id_destinatario,");
		query.append("       con_res.rcli_id_destinatario)) t,");
		query.append("       ppd_recibos rec,");
		query.append("       ppd_status_recibos s_rec,");
		query.append("       ppd_status_recibos s_dev,");
		query.append("       ppd_status_recibos s_can,");
		query.append("       ppd_status_recibos s_lib,");
		query.append("       ppd_status_recibos s_env,");
		query.append("       usuario u_rec,");
		query.append("       usuario u_dev,");
		query.append("       usuario u_can,");
		query.append("       usuario u_lib,");
		query.append("       usuario u_env,");
		query.append("       ppd_natureza_produtos nat,");
		query.append("       ppd_formas_pgto fp,");
		query.append("       pessoa pben,");
		query.append("       filial fil,");
		query.append("       pessoa pfil");
		query.append(" WHERE rec.id_recibo = t.id_recibo");
		query.append("   AND t.id_status_recibo_rec = s_rec.id_status_recibo(+)");
		query.append("   AND t.id_status_recibo_dev = s_dev.id_status_recibo(+)");
		query.append("   AND t.id_status_recibo_can = s_can.id_status_recibo(+)");
		query.append("   AND t.id_status_recibo_lib = s_lib.id_status_recibo(+)");
		query.append("   AND t.id_status_recibo_env = s_env.id_status_recibo(+)");
		query.append("   AND s_rec.id_usuario = u_rec.id_usuario(+)");
		query.append("   AND s_dev.id_usuario = u_dev.id_usuario(+)");
		query.append("   AND s_can.id_usuario = u_can.id_usuario(+)");
		query.append("   AND s_lib.id_usuario = u_lib.id_usuario(+)");
		query.append("   AND s_env.id_usuario = u_env.id_usuario(+)");
		query.append("   AND rec.id_natureza_produto = nat.id_natureza_produto(+)");
		query.append("   AND rec.id_forma_pgto = fp.id_forma_pgto(+)");
		query.append("   AND rec.id_pessoa = pben.id_pessoa(+)");
		query.append("   AND rec.id_filial = fil.id_filial(+)");
		query.append("   AND fil.id_filial = pfil.id_pessoa(+)");
		
		Map queryParam = new HashMap();
		
		if(MapUtils.getObject(parameters, "idRecibo") != null) {
			queryParam.put("idRecibo", parameters.get("idRecibo"));
		}
		if(MapUtils.getObject(parameters, "dtReciboInicial") != null) {
			queryParam.put("dtReciboInicial", (YearMonthDay)parameters.get("dtReciboInicial"));	
		}
		if(MapUtils.getObject(parameters, "dtReciboFinal") != null) {
			queryParam.put("dtReciboFinal", (YearMonthDay)parameters.get("dtReciboFinal"));
		}
		if(MapUtils.getObject(parameters, "vlIndenizacaoInicial") != null) { 
			queryParam.put("vlIndenizacaoInicial", parameters.get("vlIndenizacaoInicial"	));
		}
		if(MapUtils.getObject(parameters, "vlIndenizacaoFinal") != null) {
			queryParam.put("vlIndenizacaoFinal", parameters.get("vlIndenizacaoFinal"));
		}
		if(MapUtils.getObject(parameters, "idFilial") != null) {
			queryParam.put("idFilial", parameters.get("idFilial"));
		}
		if(MapUtils.getObject(parameters, "sgFilialOrigem") != null) {			
			queryParam.put("sgFilialOrigem", parameters.get("sgFilialOrigem").toString().toUpperCase());
		}
		if(MapUtils.getObject(parameters, "nrCtrc") != null) {
			queryParam.put("nrCtrc", parameters.get("nrCtrc"));
		}
		if(MapUtils.getObject(parameters, "sgFilialRnc") != null) {			
			queryParam.put("sgFilialRnc", parameters.get("sgFilialRnc").toString().toUpperCase());
		}
		if(MapUtils.getObject(parameters, "nrRnc") != null) {
			queryParam.put("nrRnc", parameters.get("nrRnc"));
		}
		if(MapUtils.getObject(parameters, "tpStatus") != null) {
			queryParam.put("tpStatus", parameters.get("tpStatus"));
		}		
		if(MapUtils.getObject(parameters, "idPessoa") != null) {
			queryParam.put("idPessoa", parameters.get("idPessoa"));
		}
		if(MapUtils.getObject(parameters, "tpIndenizacao") != null) {
			queryParam.put("tpIndenizacao", parameters.get("tpIndenizacao"));
		}
		if(MapUtils.getObject(parameters, "nrRecibo") != null) {
			queryParam.put("nrRecibo", parameters.get("nrRecibo"));
		}
		if(MapUtils.getObject(parameters, "idNaturezaProduto") != null) {
			queryParam.put("idNaturezaProduto", parameters.get("idNaturezaProduto"));
		}
		if(MapUtils.getObject(parameters, "tpLocalidade") != null) {
			queryParam.put("tpLocalidade", parameters.get("tpLocalidade"));
		}
		if(MapUtils.getObject(parameters, "sgFilialLocal1") != null) {
			queryParam.put("sgFilialLocal1", parameters.get("sgFilialLocal1").toString().toUpperCase());
		}
		if(MapUtils.getObject(parameters, "sgFilialLocal2") != null) {
			queryParam.put("sgFilialLocal2", parameters.get("sgFilialLocal2").toString().toUpperCase());
		}
		
		JRReportDataObject jrReportDataObject = executeQuery(query.toString(), queryParam);
		
		Map parametrosReport = new HashMap(); 	
		
		parametrosReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
		parametrosReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametrosReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametrosReport.put(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		
		jrReportDataObject.setParameters(parametrosReport);
		return jrReportDataObject;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setGrupoAtendimentoService(
			PpdGrupoAtendimentoService grupoAtendimentoService) {
		this.grupoAtendimentoService = grupoAtendimentoService;
	}

	public void setNaturezaProdutoService(
			PpdNaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
}
