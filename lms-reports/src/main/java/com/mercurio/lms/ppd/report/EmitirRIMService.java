package com.mercurio.lms.ppd.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.util.MoedaUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class EmitirRIMService extends ReportServiceSupport {	
	private MoedaService moedaService;
	
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT fil.sg_filial SG_FILIAL_RIM, ");
		query.append("   rec.nr_recibo_indenizacao NR_RECIBO_INDENIZACAO, ");
		query.append("   rec.dt_recibo DT_EMISSAO, ");
		query.append("   (SELECT vi18n(ds_valor_dominio_i) ");
		query.append("      FROM valor_dominio ");
		query.append("     WHERE id_dominio IN ");
		query.append("           (SELECT id_dominio ");
		query.append("              FROM dominio ");
		query.append("             WHERE nm_dominio = 'DM_PPD_TIPO_INDENIZACAO' ");
		query.append("       AND vl_valor_dominio = rec.tp_indenizacao)) TP_INDENIZACAO, ");
		query.append("   pben.nm_pessoa NOME_BENEFICIARIO, ");
		query.append("   pben.tp_identificacao TP_IDENTIFICACAO, ");
		query.append("   pben.nr_identificacao NR_IDENTIFICACAO, ");
		query.append("   vi18n(log.ds_tipo_logradouro_i) DS_TIPO_LOGRADOURO, ");
		query.append("   ep.ds_endereco DS_ENDERECO, ");
		query.append("   ep.nr_endereco NR_ENDERECO, ");
		query.append("   ep.ds_bairro DS_BAIRRO, ");
		query.append("   ep.nr_cep, ");
		query.append("   mun.nm_municipio NM_MUNICIPIO, ");
		query.append("   uf.sg_unidade_federativa SG_UNIDADE_FEDERATIVA, ");
		query.append("   rec.nr_banco NR_BANCO, ");
		query.append("   rec.nr_agencia NR_AGENCIA_BANCARIA, ");
		query.append("   rec.nr_digito_agencia NR_DIGITO_AGENCIA, ");
		query.append("   rec.nr_conta_corrente NR_CONTA_CORRENTE, ");
		query.append("   rec.nr_digito_conta_corrente NR_DIGITO_CONTA_CORRENTE, ");
		query.append("   rec.sg_filial_origem SG_FILIAL_ORIGEM, ");
		query.append("   t.numero_ctrc NR_DOCTO_SERVICO, ");
		query.append("   rec.dt_emissao_ctrc DH_EMISSAO, ");
		query.append("   nat.ds_natureza_produto DS_PRODUTO, ");
		query.append("   t.qt_volumes QT_VOLUMES, ");
		query.append("   t.vlr_mercadoria VL_MERCADORIA, ");
		query.append("   round((rec.vl_indenizacao / t.vlr_mercadoria) * 100, 2) perc_inden, ");
		query.append("   rec.vl_indenizacao VL_INDENIZADO, ");
		query.append("   nvl((SELECT upper(p.nm_pessoa) ");
		query.append("         FROM pessoa p ");
		query.append("        WHERE p.nr_identificacao = ");
		query.append("              lpad(t.clib_pess_id_remetente, 14, '0') ");
		query.append("          AND p.tp_identificacao = 'CNPJ' ");
		query.append("          AND rownum = 1), ");
		query.append("       (SELECT upper(p.nm_pessoa) ");
		query.append("          FROM pessoa p ");
		query.append("         WHERE p.nr_identificacao = ");
		query.append("               lpad(t.clib_pess_id_remetente, 11, '0') ");
		query.append("           AND p.tp_identificacao = 'CPF' ");
		query.append("           AND rownum = 1)) NM_REMETENTE, ");
		query.append("   nvl((SELECT upper(p.nm_pessoa) ");
		query.append("         FROM pessoa p ");
		query.append("        WHERE p.nr_identificacao = ");
		query.append("              lpad(t.clib_pess_id_destinatario, 14, '0') ");
		query.append("          AND p.tp_identificacao = 'CNPJ' ");
		query.append("          AND rownum = 1), ");
		query.append("       (SELECT upper(p.nm_pessoa) ");
		query.append("          FROM pessoa p ");
		query.append("         WHERE p.nr_identificacao = ");
		query.append("               lpad(t.clib_pess_id_destinatario, 11, '0') ");
		query.append("           AND p.tp_identificacao = 'CPF' ");
		query.append("           AND rownum = 1)) NM_DESTINATARIO, ");
		query.append("   rnc.unid_sigla SG_FILIAL_NC, ");
		query.append("   rnc.numero NR_NAO_CONFORMIDADE, ");
		query.append("   rnc.tipo_nao_conf, ");
		query.append("   t.nrnf, ");
		query.append("   fp.ds_forma_pgto TP_FORMA_PAGAMENTO, ");
		query.append("   rec.sg_filial_comp1, ");
		query.append("   rec.pc_filial_comp1, ");
		query.append("   rec.sg_filial_comp2, ");
		query.append("   rec.pc_filial_comp2, ");
		query.append("   rec.sg_filial_comp3, ");
		query.append("   rec.pc_filial_comp3, ");
		query.append("   rec.ob_recibo OB_RECIBO_INDENIZACAO, ");
		query.append("   rec.nr_seguro NR_PROCESSO_SINISTRO ");
		query.append("FROM (SELECT rec.id_recibo, ");
		query.append("           rec.sg_filial_origem, ");
		query.append("           rec.dt_emissao_ctrc, ");
		query.append("           nvl(con.unid_sigla_destino, con_res.unid_sigla_destino) unid_sigla_destino, ");
		query.append("           nvl(con.clib_pess_id_remetente, con_res.rcli_id_remetente) clib_pess_id_remetente, ");
		query.append("           nvl(con.clib_pess_id_destinatario, ");
		query.append("               con_res.rcli_id_destinatario) clib_pess_id_destinatario, ");
		query.append("           wm_concat(nvl(nfcon.numero, nfcon_res.numero)) nrnf, ");
		query.append("           SUM(nvl(nfcon.qtd_volumes, nfcon_res.qtde_vols)) qt_volumes, ");
		query.append("           SUM(nvl(nfcon.vlr_total, nfcon_res.vlr_total)) vlr_mercadoria, ");
		query.append("           nvl(con.numero, con_res.numero) numero_ctrc ");
		query.append("      FROM (SELECT r.*, ");
		query.append("                   substr(lpad(r.nr_ctrc, 10, '0'), 5, 6) nr_ctrc_join ");
		query.append("              FROM ppd_recibos r) rec, ");
		query.append("           (SELECT c.*, ");
		query.append("                   substr(lpad(c.numero, 10, '0'), 5, 6) nr_ctrc, ");
		query.append("                   trunc(c.dthr_emissao) dt_emissao_ctrc ");
		query.append("              FROM conhecimentos c) con, ");
		query.append("           (SELECT c.*, ");
		query.append("                   substr(lpad(c.numero, 10, '0'), 5, 6) nr_ctrc, ");
		query.append("                   trunc(c.dthr_emissao) dt_emissao_ctrc ");
		query.append("              FROM res_conhecimentos c) con_res, ");
		query.append("           notas_fiscais_ctos nfcon, ");
		query.append("           res_notas_fiscais nfcon_res ");
		query.append("     WHERE rec.sg_filial_origem = con.unid_sigla_origem(+) ");
		query.append("       AND rec.nr_ctrc_join = con.nr_ctrc(+) ");
		query.append("       AND rec.dt_emissao_ctrc = con.dt_emissao_ctrc(+) ");
		query.append("       AND rec.sg_filial_origem = con_res.unid_sigla_origem(+) ");
		query.append("       AND rec.nr_ctrc_join = con_res.nr_ctrc(+) ");
		query.append("       AND rec.dt_emissao_ctrc = con_res.dt_emissao_ctrc(+) ");
		query.append("       AND con.unid_sigla_origem = nfcon.ctos_unid_sigla(+) ");
		query.append("       AND con.numero = nfcon.ctos_numero(+) ");
		query.append("       AND con_res.unid_sigla_origem = nfcon_res.rcto_unid_sigla(+) ");
		query.append("       AND con_res.numero = nfcon_res.rcto_numero(+) ");
		query.append("       AND con_res.dt_emissao_ctrc = nfcon_res.rcto_dthr_emissao(+) ");		
		
		query.append("  AND rec.id_recibo = :idRecibo ");

		query.append("     GROUP BY rec.id_recibo, ");
		query.append("              rec.sg_filial_origem, ");
		query.append("              rec.dt_emissao_ctrc, ");
		query.append("              nvl(con.unid_sigla_destino, con_res.unid_sigla_destino), ");
		query.append("              nvl(con.clib_pess_id_remetente, con_res.rcli_id_remetente), ");
		query.append("              nvl(con.clib_pess_id_destinatario, ");
		query.append("              con_res.rcli_id_destinatario), ");
		query.append("              nvl(con.numero, con_res.numero)) t, ");
		query.append("   			ppd_recibos rec, ");
		query.append("   			ppd_natureza_produtos nat, ");
		query.append("   			ppd_formas_pgto fp, ");
		query.append("   			pessoa pben, ");
		query.append("   			endereco_pessoa ep, ");
		query.append("   			tipo_logradouro log, ");
		query.append("  			municipio mun, ");
		query.append("   			unidade_federativa uf, ");
		query.append("   			filial fil, ");
		query.append("   			pessoa pfil, ");
		query.append("   			relatorios_nao_conformidades rnc ");
		query.append("WHERE rec.id_recibo = t.id_recibo ");
		query.append("AND rec.id_natureza_produto = nat.id_natureza_produto(+) ");
		query.append("AND rec.id_forma_pgto = fp.id_forma_pgto(+) ");
		query.append("AND rec.id_pessoa = pben.id_pessoa(+) ");
		query.append("AND pben.id_endereco_pessoa = ep.id_endereco_pessoa(+) ");
		query.append("AND ep.id_tipo_logradouro = log.id_tipo_logradouro(+) ");
		query.append("AND ep.id_municipio = mun.id_municipio(+) ");
		query.append("AND mun.id_unidade_federativa = uf.id_unidade_federativa(+) ");
		query.append("AND rec.id_filial = fil.id_filial(+) ");
		query.append("AND fil.id_filial = pfil.id_pessoa(+) ");
		query.append("AND t.sg_filial_origem = rnc.unid_sigla_orig_doc(+) ");
		query.append("AND t.numero_ctrc = rnc.nro_documento(+) ");
		query.append("AND t.dt_emissao_ctrc = rnc.data_emissao_documento(+) ");
		query.append("AND 1 = rnc.tipo_documento(+) ");

		Map queryParam = new HashMap();
		
		if(MapUtils.getObject(parameters, "idRecibo") != null) {
			queryParam.put("idRecibo", parameters.get("idRecibo"));
		}

		JRReportDataObject jrReportDataObject = executeQuery(query.toString(), queryParam);
		
		Map parametrosReport = new HashMap(); 	
		
		parametrosReport.put("UNIDADE", SessionUtils.getFilialSessao().getSgFilial());
		parametrosReport.put("CNPJ", SessionUtils.getFilialSessao().getPessoa().getNrIdentificacaoFormatado());
		parametrosReport.put("SERVICE", this);
		
		jrReportDataObject.setParameters(parametrosReport);
		return jrReportDataObject;
	}
	
    public String formataValorPorExtenso(BigDecimal valor, Long idMoeda) {
    	String retorno = "";
    	
    	if (idMoeda!=null) {
    		Moeda moeda = getMoedaService().findById(idMoeda);
    		retorno = MoedaUtils.formataPorExtenso(valor, moeda);
    		retorno = retorno.toUpperCase();
    	}
    	return retorno;
    }

	public MoedaService getMoedaService() {
		return moedaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	
}
