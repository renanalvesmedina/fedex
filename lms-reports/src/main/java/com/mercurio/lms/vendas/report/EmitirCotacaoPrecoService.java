package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.ServicoAdicionalPrecificado;
import com.mercurio.lms.expedicao.model.service.TabelaServicoAdicionalService;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.service.CotacaoService;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * @author Mauricio Moraes
 *
 * @spring.bean id="lms.vendas.emitirCotacaoPrecoService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirCotacaoPrecos.jasper"
 */
public class EmitirCotacaoPrecoService extends ReportServiceSupport {
	private CotacaoService cotacaoService;
	private TabelaServicoAdicionalService tabelaServicoAdicionalService;

	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate(parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		parametersReport.put("id_coleta", parameters.get("idCotacao"));

		jr.setParameters(parametersReport);
		return jr;
	}

	public JRDataSource executeDimensoes(Object[] parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		sql.addProjection("dim.nr_altura", "nr_altura");
		sql.addProjection("dim.nr_largura", "nr_largura");
		sql.addProjection("dim.nr_comprimento", "nr_comprimento");
		sql.addProjection("dim.nr_quantidade", "nr_quantidade");
		sql.addFrom("dimensao", "dim");
		sql.addCustomCriteria("dim.id_cotacao="+parameters[0]);
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}

	public JRDataSource executeServicosAdicionaisCotados(Object[] parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("sa.ds_servico_adicional_i"), "ds_servico_adicional");
		sql.addProjection("sads.qt_dias", "qt_dias");
		sql.addProjection("sads.qt_coletas", "qt_coletas");
		sql.addProjection("sads.nr_km_rodado", "nr_km_rodado");
		sql.addProjection("sads.qt_paletes", "qt_paletes");
		sql.addProjection("sads.qt_segurancas_adicionais", "qt_segurancas_adicionais");
		sql.addProjection("sads.vl_mercadoria", "vl_mercadoria");
		sql.addFrom("cotacao", "cot");
		sql.addFrom("serv_adicional_doc_serv", "sads");
		sql.addFrom("servico_adicional", "sa");
		sql.addCustomCriteria("sads.id_cotacao=cot.id_cotacao");
		sql.addCustomCriteria("sads.id_servico_adicional=sa.id_servico_adicional");
		sql.addCustomCriteria("cot.id_cotacao="+parameters[0]);
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}

	public JRDataSource executeServicosAdicionaisTabelaPrecoVigente(Object[] parameters) throws Exception {	
		Cotacao cotacao = cotacaoService.findById(((Double)parameters[0]).longValue());
		Long idServico = null;
		Long idDivisaoCliente = null;
		
		if(cotacao.getServico() != null ) {
			idServico = cotacao.getServico().getIdServico();
		}
		if(cotacao.getDivisaoCliente()!= null) {
			idDivisaoCliente = cotacao.getDivisaoCliente().getIdDivisaoCliente();
		}
		
		List<ServicoAdicionalPrecificado> servicosAdicionaisPrecificados = 
				tabelaServicoAdicionalService.findByTabelaCliente(idServico, idDivisaoCliente);
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			
		for (ServicoAdicionalPrecificado servico : servicosAdicionaisPrecificados) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("nmServicoAdicional", servico.getDsParcela());
			map.put("dsServicoAdicional", tabelaServicoAdicionalService.findDsFormatada(servico, Boolean.TRUE));
			result.add(map);
				}
			
		JRMapCollectionDataSource jrMap1 = new JRMapCollectionDataSource(result);
		
		return jrMap1;
	}

	public JRDataSource executeGeneralidades(Object[] parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("pp.nm_parcela_preco_i"), "nm_parcela_preco");
		sql.addProjection("tp_ind_dom.vl", "tp_indicador_vl");
		sql.addProjection("tp_ind_dom.ds", "tp_indicador");
		sql.addProjection("gc.vl_generalidade ", "vl_generalidade");
		
		sql.addFrom("parametro_cliente", "pc");
		sql.addFrom("cotacao", "cot");
		sql.addFrom("generalidade_cliente", "gc");
		sql.addFrom("parcela_preco", "pp");
		sql.addFrom(getSelectDominio("DM_INDICADOR_PARAMETRO_CLIENTE"), "tp_ind_dom");
		
		sql.addCustomCriteria("cot.id_cotacao=pc.id_cotacao(+)");
		sql.addCustomCriteria("pc.id_parametro_cliente=gc.id_parametro_cliente(+)");
		sql.addCustomCriteria("pp.id_parcela_preco=gc.id_parcela_preco");
		sql.addCustomCriteria("pp.cd_parcela_preco<>'IDPedagio'");
		sql.addCustomCriteria("gc.tp_indicador=tp_ind_dom.vl");
		sql.addCustomCriteria("cot.id_cotacao="+parameters[0]);
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}

	public JRDataSource executeTaxas(Object[] parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("pp.nm_parcela_preco_i"), "nm_parcela_preco");
		sql.addProjection("tp_tx_ind_dom.vl", "tp_taxa_indicador_vl");
		sql.addProjection("tp_tx_ind_dom.ds", "tp_taxa_indicador");
		sql.addProjection("txc.vl_taxa", "vl_taxa");
		sql.addProjection("txc.ps_minimo", "ps_minimo");
		sql.addProjection("txc.vl_excedente", "vl_excedente");
		
		sql.addFrom("parametro_cliente", "pc");
		sql.addFrom("cotacao", "cot");
		sql.addFrom("taxa_cliente", "txc");
		sql.addFrom("parcela_preco", "pp");
		sql.addFrom(getSelectDominio("DM_INDICADOR_PARAMETRO_CLIENTE"), "tp_tx_ind_dom");
		
		sql.addCustomCriteria("cot.id_cotacao=pc.id_cotacao(+)");
		sql.addCustomCriteria("pc.id_parametro_cliente=txc.id_parametro_cliente(+)");
		sql.addCustomCriteria("pp.id_parcela_preco=txc.id_parcela_preco");
		sql.addCustomCriteria("txc.tp_taxa_indicador=tp_tx_ind_dom.vl");
		sql.addCustomCriteria("cot.id_cotacao="+parameters[0]);
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}

	public JRDataSource executeServicosAdicionais(Object[] parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("pp.nm_parcela_preco_i"), "nm_parcela_preco");
		sql.addProjection("tp_ind_dom.vl", "tp_indicador_vl");
		sql.addProjection("tp_ind_dom.ds", "tp_indicador");
		sql.addProjection("sac.vl_valor", "vl_valor");
		sql.addProjection("sac.nr_quantidade_dias", "nr_quantidade_dias");
		sql.addProjection("sac.vl_minimo", "vl_minimo");
		
		sql.addFrom("servico_adicional_cliente", "sac");
		sql.addFrom("cotacao", "cot");
		sql.addFrom("parcela_preco", "pp");
		sql.addFrom(getSelectDominio("DM_INDICADOR_PARAMETRO_CLIENTE"), "tp_ind_dom");
		
		sql.addCustomCriteria("cot.id_cotacao=sac.id_cotacao(+)");
		sql.addCustomCriteria("pp.id_parcela_preco=sac.id_parcela_preco");
		sql.addCustomCriteria("sac.tp_indicador=tp_ind_dom.vl");
		sql.addCustomCriteria("cot.id_cotacao="+parameters[0]);
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}

	public JRDataSource executeCalculoFrete(Object[] parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("pp.nm_parcela_preco_i"), "nm_parcela_preco");
		sql.addProjection("pc.vl_parcela_cotacao", "vl_parcela_cotacao");
		
		sql.addFrom("cotacao", "cot");
		sql.addFrom("parcela_cotacao", "pc");
		sql.addFrom("parcela_preco", "pp");
		
		sql.addCustomCriteria("cot.id_cotacao=pc.id_cotacao");
		sql.addCustomCriteria("pp.id_parcela_preco=pc.id_parcela_preco");
		sql.addCustomCriteria("pp.tp_parcela_preco<>'S'");
		sql.addCustomCriteria("cot.id_cotacao="+parameters[0]);
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}

	public JRDataSource executeCalculoServicos(Object[] parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection(PropertyVarcharI18nProjection.createProjection("pp.nm_parcela_preco_i"), "nm_parcela_preco");
		sql.addProjection("pc.vl_parcela_cotacao", "vl_parcela_cotacao");

		sql.addFrom("cotacao", "cot");
		sql.addFrom("parcela_cotacao", "pc");
		sql.addFrom("parcela_preco", "pp");

		sql.addCustomCriteria("cot.id_cotacao=pc.id_cotacao");
		sql.addCustomCriteria("pp.id_parcela_preco=pc.id_parcela_preco");
		sql.addCustomCriteria("pp.tp_parcela_preco='S'");
		sql.addCustomCriteria("cot.id_cotacao="+parameters[0]);

		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}

	private SqlTemplate getSqlTemplate(Map parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("cot.id_cotacao", "id_cotacao");
		sql.addProjection("cot.tp_docto_cotacao", "tp_documento_cotacao");
		sql.addProjection("fil.sg_filial ", "sg_filial");
		sql.addProjection("cot.nr_cotacao", "nr_cotacao");
		sql.addProjection("cot.dt_geracao_cotacao", "dt_geracao_cotacao");
		sql.addProjection("cot.dt_validade", "dt_validade");
		sql.addProjection("frete_dom.vl", "tp_frete_vl");
		sql.addProjection("frete_dom.ds", "tp_frete");
		sql.addProjection("cot.nr_nota_fiscal", "nr_nota_fiscal");
		sql.addProjection("cot.vl_mercadoria", "vl_mercadoria");
		sql.addProjection("cot.ps_real", "ps_real");
		sql.addProjection("cot.ps_cubado", "ps_cubado");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("nat_prod.ds_natureza_produto_i"), "ds_natureza_produto");
		sql.addProjection("usu_rea.nm_usuario", "nm_usuario_realizou");
		sql.addProjection("usu_apr.nm_usuario", "nm_usuario_aprovou");
		sql.addProjection("cot.nm_cliente_remetente", "nm_cliente_remetente");
		sql.addProjection("cot.nm_cliente_destino","nm_cliente_destino");
		sql.addProjection("cli_sol_dom.vl","tp_cliente_solicitou_vl");
		sql.addProjection("cli_sol_dom.ds","tp_cliente_solicitou");
		sql.addProjection("pes_rem.nr_identificacao","nr_identificacao_remetente");
		sql.addProjection("pes_rem.tp_identificacao","tp_identificacao_remetente");
		sql.addProjection("sit_trib_rem_dom.vl","tp_sit_tributaria_remetente_vl");
		sql.addProjection("sit_trib_rem_dom.ds","tp_sit_tributaria_remetente");
		sql.addProjection("mun_ori.nm_municipio","nm_municipio_origem");
		sql.addProjection("fil_ori.sg_filial","sg_filial_origem");
		sql.addProjection("cli_des_dom.vl","tp_cliente_destino_vl");
		sql.addProjection("cli_des_dom.ds","tp_cliente_destino");
		sql.addProjection("pes_des.nr_identificacao","nr_identificacao_destinatario");
		sql.addProjection("pes_des.tp_identificacao","tp_identificacao_destinatario");
		sql.addProjection("sit_trib_des_dom.vl","tp_sit_tributaria_dest_vl");
		sql.addProjection("sit_trib_des_dom.ds","tp_sit_tributaria_destinatario");
		sql.addProjection("mun_des.nm_municipio","nm_municipio_destino");
		sql.addProjection("fil_des.sg_filial","sg_filial_destino");
		sql.addProjection("cot.nm_responsavel_frete","nm_responsavel_frete");
		sql.addProjection("cli_dom.vl","tp_cliente_vl");
		sql.addProjection("cli_dom.ds","tp_cliente");
		sql.addProjection("pes_res.nr_identificacao","nr_identificacao_responsavel");
		sql.addProjection("pes_res.tp_identificacao","tp_identificacao_responsavel");
		sql.addProjection("sit_trib_res_dom.vl","tp_sit_tributaria_resp_vl");
		sql.addProjection("sit_trib_res_dom.ds","tp_sit_tributaria_responsavel");
		sql.addProjection("cot.ds_contato","ds_contato");
		sql.addProjection("cot.nr_telefone","nr_telefone");
		sql.addProjection("cot.ds_email","ds_email");
		sql.addProjection("cot.nr_fax","nr_fax");
		sql.addProjection("cot.nr_ppe","nr_ppe");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("ser.ds_servico_i"),"ds_servico");
		sql.addProjection("div_cli.ds_divisao_cliente","ds_divisao_cliente");
		sql.addProjection("ttp.tp_tipo_tabela_preco","tp_tipo_tabela_preco");
		sql.addProjection("ttp.nr_versao","nr_versao");
		sql.addProjection("sttp.tp_subtipo_tabela_preco","tp_subtipo_tabela_preco");
		sql.addProjection("ttp.tp_tipo_tabela_preco || ttp.nr_versao || '-' || sttp.tp_subtipo_tabela_preco","tabela_preco");
		sql.addProjection("sit_cot_dom.vl","tp_situacao_vl");
		sql.addProjection("sit_cot_dom.ds","tp_situacao");
		sql.addProjection("ds.dh_emissao","dh_emissao");
		sql.addProjection("cot.nr_documento_cotacao","nr_documento_cotacao");
		sql.addProjection("aer_ori.sg_aeroporto","sg_aeroporto_origem");
		sql.addProjection("aer_des.sg_aeroporto","sg_aeroporto_destino");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("pe.ds_produto_especifico_i"),"ds_produto_especifico");
		sql.addProjection("bl_col_emer_dom.vl","bl_coleta_emerg_vl");
		sql.addProjection("bl_col_emer_dom.ds","bl_coleta_emergencia");
		sql.addProjection("bl_ent_emer_dom.vl","bl_entrega_emerg_vl");
		sql.addProjection("bl_ent_emer_dom.ds","bl_entrega_emergencia");
		sql.addProjection("tp_ind_per_min_prog_dom.vl","tp_indic_perc_min_progr_vl");
		sql.addProjection("tp_ind_per_min_prog_dom.ds","tp_indicador_perc_minimo_progr");
		sql.addProjection("pc.vl_perc_minimo_progr","vl_perc_minimo_progr");
		sql.addProjection("tp_ind_min_fre_pes_dom.vl","tp_ind_min_frete_peso_vl");
		sql.addProjection("tp_ind_min_fre_pes_dom.ds","tp_indicador_min_frete_peso");
		sql.addProjection("pc.vl_min_frete_peso","vl_min_frete_peso");
		sql.addProjection("tp_ind_fre_pes_dom.vl","tp_ind_frete_peso_vl");
		sql.addProjection("tp_ind_fre_pes_dom.ds","tp_indicador_frete_peso");
		sql.addProjection("pc.vl_frete_peso","vl_frete_peso");
		sql.addProjection("pc.vl_frete_volume","vl_frete_volume");
		sql.addProjection("pc.vl_minimo_frete_quilo","vl_minimo_frete_quilo");
		sql.addProjection("bl_paga_peso_exc_dom.vl","bl_paga_peso_exc_vl");
		sql.addProjection("bl_paga_peso_exc_dom.ds","bl_paga_peso_excedente");
		sql.addProjection("tp_tar_min_dom.vl","tp_tarifa_minima_vl");
		sql.addProjection("tp_tar_min_dom.ds","tp_tarifa_minima");
		sql.addProjection("pc.vl_tarifa_minima","vl_tarifa_minima");
		sql.addProjection("tp_ind_vlr_tbl_esp.vl","tp_indic_vlr_tbl_esp_vl");
		sql.addProjection("tp_ind_vlr_tbl_esp.ds","tp_indic_vlr_tbl_especifica");
		sql.addProjection("pc.vl_tbl_especifica","vl_tbl_especifica");
		sql.addProjection("bl_paga_cub_dom.vl","bl_paga_cubagem_vl");
		sql.addProjection("bl_paga_cub_dom.ds","bl_paga_cubagem");
		sql.addProjection("pc.pc_paga_cubagem","pc_paga_cubagem");
		sql.addProjection("tp_ind_adv_dom.vl","tp_indicador_adv_vl");
		sql.addProjection("tp_ind_adv_dom.ds","tp_indicador_advalorem");
		sql.addProjection("pc.vl_advalorem","vl_advalorem");
		sql.addProjection("tp_ind_adv2_dom.vl","tp_indicador_adv_2_vl");
		sql.addProjection("tp_ind_adv2_dom.ds","tp_indicador_advalorem_2");
		sql.addProjection("pc.vl_advalorem_2","vl_advalorem_2");
		sql.addProjection("tp_ind_val_ref_dom.vl","tp_indicador_valor_ref_vl");
		sql.addProjection("tp_ind_val_ref_dom.ds","tp_indicador_valor_referencia");
		sql.addProjection("pc.vl_valor_referencia","vl_valor_referencia");
		sql.addProjection("pc.pc_frete_percentual","pc_frete_percentual");
		sql.addProjection("pc.vl_minimo_frete_percentual","vl_minimo_frete_percentual");
		sql.addProjection("pc.vl_tonelada_frete_percentual","vl_tonelada_frete_percentual");
		sql.addProjection("pc.ps_frete_percentual","ps_frete_percentual");
		sql.addProjection("pc.vl_percentual_gris","vl_percentual_gris");
		sql.addProjection("tp_ind_min_gris_dom.vl","tp_ind_min_gris_vl");
		sql.addProjection("tp_ind_min_gris_dom.ds","tp_indicador_minimo_gris");
		sql.addProjection("tp_ind_per_gris_dom.vl","tp_ind_perc_gris_vl");
		sql.addProjection("tp_ind_per_gris_dom.ds","tp_indicador_percentual_gris");
		sql.addProjection("pc.vl_minimo_gris","vl_minimo_gris");
		
		sql.addProjection("pc.vl_percentual_tde","vl_percentual_tde");
		sql.addProjection("pc.vl_minimo_tde","vl_minimo_tde");
		sql.addProjection("tp_ind_min_tde_dom.vl","tp_ind_min_tde_vl");
		sql.addProjection("tp_ind_min_tde_dom.ds","tp_indicador_minimo_tde");
		sql.addProjection("tp_ind_per_tde_dom.vl","tp_ind_perc_tde_vl");
		sql.addProjection("tp_ind_per_tde_dom.ds","tp_indicador_percentual_tde");

		sql.addProjection("pc.pc_cobranca_reentrega","pc_cobranca_reentrega");
		sql.addProjection("pc.pc_cobranca_devolucoes","pc_cobranca_devolucoes");
		sql.addProjection("pc.pc_desconto_frete_total","pc_desconto_frete_total");
		sql.addProjection("tp_ind_ped_dom.vl","tp_indicador_pedagio_vl");
		sql.addProjection("tp_ind_ped_dom.ds","tp_indicador_pedagio");
		sql.addProjection("pc.vl_pedagio","vl_pedagio");
		sql.addProjection("cot.ob_cotacao","ob_cotacao");
		sql.addProjection("cot.vl_total_parcelas","vl_total_parcelas");
		sql.addProjection("cot.vl_total_servicos","vl_total_servicos");
		sql.addProjection("cot.vl_total_cotacao","vl_total_cotacao");
		sql.addProjection("cot.vl_icms_st","vl_icms_st");
		sql.addProjection("cot.vl_frete_liquido","vl_frete_liquido");
		sql.addProjection("cot.vl_imposto","vl_imposto");
		sql.addProjection("moeda_cotacao.sg_moeda || ' ' || moeda_cotacao.ds_simbolo","moeda_cotacao");
		sql.addProjection("moeda_tab_preco.sg_moeda || ' ' || moeda_tab_preco.ds_simbolo","moeda");
	 
		sql.addFrom("cotacao", "cot");
		sql.addFrom("filial", "fil");
		sql.addFrom(getSelectDominio("DM_TIPO_FRETE"), "frete_dom");
		sql.addFrom("v_natureza_produto_i", "nat_prod");
		sql.addFrom("usuario", "usu_rea");
		sql.addFrom("usuario", "usu_apr");
		sql.addFrom(getSelectDominio("DM_TIPO_CLIENTE"), "cli_sol_dom");
		sql.addFrom("cliente", "cli_sol");
		sql.addFrom("pessoa", "pes_rem");
		sql.addFrom(getSelectDominio("DM_SIT_TRIBUTARIA_CLIENTE"), "sit_trib_rem_dom");
		sql.addFrom("municipio", "mun_ori");
		sql.addFrom("filial", "fil_ori");
		sql.addFrom(getSelectDominio("DM_TIPO_CLIENTE"), "cli_des_dom");
		sql.addFrom("cliente", "cli_des");
		sql.addFrom("pessoa", "pes_des");
		sql.addFrom(getSelectDominio("DM_SIT_TRIBUTARIA_CLIENTE"), "sit_trib_des_dom");
		sql.addFrom("municipio", "mun_des");
		sql.addFrom("filial", "fil_des");
		sql.addFrom(getSelectDominio("DM_TIPO_CLIENTE"), "cli_dom");
		sql.addFrom("cliente", "cli");
		sql.addFrom("pessoa", "pes_res");
		sql.addFrom(getSelectDominio("DM_SIT_TRIBUTARIA_CLIENTE"), "sit_trib_res_dom");
		sql.addFrom("v_servico_i", "ser");
		sql.addFrom("divisao_cliente", "div_cli");
		sql.addFrom("v_tabela_preco_i", "tp");
		sql.addFrom("tipo_tabela_preco", "ttp");
		sql.addFrom("subtipo_tabela_preco", "sttp");
		sql.addFrom(getSelectDominio("DM_STATUS_COTACAO"), "sit_cot_dom");
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("aeroporto", "aer_ori");
		sql.addFrom("aeroporto", "aer_des");
		sql.addFrom("produto_especifico", "pe");
		sql.addFrom(getSelectDominio("DM_SIM_NAO"), "bl_col_emer_dom");
		sql.addFrom(getSelectDominio("DM_SIM_NAO"), "bl_ent_emer_dom");
		sql.addFrom("parametro_cliente", "pc");
		sql.addFrom(getSelectDominio("DM_ACRESCIMO_DESCONTO"), "tp_ind_per_min_prog_dom");
		sql.addFrom(getSelectDominio("DM_INDICADOR_FRETE_MINIMO"), "tp_ind_min_fre_pes_dom");
		sql.addFrom(getSelectDominio("DM_INDICADOR_PARAMETRO_CLIENTE"), "tp_ind_fre_pes_dom");
		sql.addFrom(getSelectDominio("DM_SIM_NAO"), "bl_paga_peso_exc_dom");
		sql.addFrom(getSelectDominio("DM_INDICADOR_PARAMETRO_CLIENTE"), "tp_tar_min_dom");
		sql.addFrom(getSelectDominio("DM_INDICADOR_PARAMETRO_CLIENTE"), "tp_ind_vlr_tbl_esp");
		sql.addFrom(getSelectDominio("DM_SIM_NAO"), "bl_paga_cub_dom");
		sql.addFrom(getSelectDominio("DM_INDICADOR_ADVALOREM"), "tp_ind_adv_dom");
		sql.addFrom(getSelectDominio("DM_INDICADOR_ADVALOREM"), "tp_ind_adv2_dom");
		sql.addFrom(getSelectDominio("DM_INDICADOR_PARAMETRO_CLIENTE"), "tp_ind_val_ref_dom");

		sql.addFrom(getSelectDominio("DM_INDICADOR_ADVALOREM"), "tp_ind_per_gris_dom");
		sql.addFrom(getSelectDominio("DM_INDICADOR_PARAMETRO_CLIENTE"), "tp_ind_min_gris_dom");

		sql.addFrom(getSelectDominio("DM_INDICADOR_ADVALOREM"), "tp_ind_per_tde_dom");
		sql.addFrom(getSelectDominio("DM_INDICADOR_PARAMETRO_CLIENTE"), "tp_ind_min_tde_dom");

		sql.addFrom(getSelectDominio("DM_INDICADOR_PEDAGIO"), "tp_ind_ped_dom");
		sql.addFrom("moeda", "moeda_cotacao");
		sql.addFrom("moeda", "moeda_tab_preco");
	
		sql.addCustomCriteria("cot.id_filial = fil.id_filial(+)");
		sql.addCustomCriteria("cot.tp_frete = frete_dom.vl(+)");
		sql.addCustomCriteria("cot.id_natureza_produto = nat_prod.id_natureza_produto(+)");
		sql.addCustomCriteria("cot.id_usuario_realizou = usu_rea.id_usuario(+)");
		sql.addCustomCriteria("cot.id_usuario_aprovou = usu_apr.id_usuario(+)");
		sql.addCustomCriteria("cot.id_cliente_solicitou = cli_sol.id_cliente(+)");
		sql.addCustomCriteria("cot.id_cliente_solicitou = pes_rem.id_pessoa(+)");
		sql.addCustomCriteria("cli_sol.tp_cliente = cli_sol_dom.vl(+)");
		sql.addCustomCriteria("cot.tp_sit_tributaria_remetente = sit_trib_rem_dom.vl(+)");
		sql.addCustomCriteria("cot.id_municipio_origem = mun_ori.id_municipio(+)");
		sql.addCustomCriteria("cot.id_filial_origem = fil_ori.id_filial(+)");
		sql.addCustomCriteria("cot.id_cliente_destino = cli_des.id_cliente(+)");
		sql.addCustomCriteria("cot.id_cliente_destino = pes_des.id_pessoa(+)");
		sql.addCustomCriteria("cli_des.tp_cliente = cli_des_dom.vl(+)");
		sql.addCustomCriteria("cot.tp_sit_tributaria_destinatario = sit_trib_des_dom.vl(+)");
		sql.addCustomCriteria("cot.id_municipio_destino = mun_des.id_municipio(+)");
		sql.addCustomCriteria("cot.id_filial_destino = fil_des.id_filial(+)");
		sql.addCustomCriteria("cot.id_cliente = cli.id_cliente(+)");
		sql.addCustomCriteria("cot.id_cliente = pes_res.id_pessoa(+)");
		sql.addCustomCriteria("cli.tp_cliente = cli_dom.vl(+)");
		sql.addCustomCriteria("cot.tp_sit_tributaria_responsavel = sit_trib_res_dom.vl(+)");
		sql.addCustomCriteria("cot.id_servico = ser.id_servico(+)");
		sql.addCustomCriteria("cot.id_divisao_cliente = div_cli.id_divisao_cliente(+)");
		sql.addCustomCriteria("cot.id_tabela_preco = tp.id_tabela_preco(+)");
		sql.addCustomCriteria("tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco(+)");
		sql.addCustomCriteria("tp.id_subtipo_tabela_preco = sttp.id_subtipo_tabela_preco(+)");
		sql.addCustomCriteria("cot.tp_situacao = sit_cot_dom.vl(+)");
		sql.addCustomCriteria("cot.id_docto_servico = ds.id_docto_servico(+)");
		sql.addCustomCriteria("cot.id_aeroporto_origem = aer_ori.id_aeroporto(+)");
		sql.addCustomCriteria("cot.id_aeroporto_destino = aer_des.id_aeroporto(+)");
		sql.addCustomCriteria("cot.id_produto_especifico = pe.id_produto_especifico(+)");
		sql.addCustomCriteria("cot.bl_coleta_emergencia = bl_col_emer_dom.vl(+)");
		sql.addCustomCriteria("cot.bl_entrega_emergencia = bl_ent_emer_dom.vl(+)");
		sql.addCustomCriteria("cot.id_cotacao = pc.id_cotacao(+)");
		sql.addCustomCriteria("pc.tp_indicador_perc_minimo_progr=tp_ind_per_min_prog_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_indicador_min_frete_peso=tp_ind_min_fre_pes_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_indicador_frete_peso=tp_ind_fre_pes_dom.vl(+)");
		sql.addCustomCriteria("pc.bl_paga_peso_excedente=bl_paga_peso_exc_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_tarifa_minima=tp_tar_min_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_indic_vlr_tbl_especifica=tp_ind_vlr_tbl_esp.vl(+)");
		sql.addCustomCriteria("pc.bl_paga_cubagem=bl_paga_cub_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_indicador_advalorem=tp_ind_adv_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_indicador_advalorem_2=tp_ind_adv2_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_indicador_valor_referencia=tp_ind_val_ref_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_indicador_percentual_gris=tp_ind_per_gris_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_indicador_minimo_gris=tp_ind_min_gris_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_indicador_minimo_tde=tp_ind_min_tde_dom.vl(+)");
		sql.addCustomCriteria("pc.tp_indicador_percentual_tde=tp_ind_per_tde_dom.vl(+)");
		sql.addCustomCriteria("tp_indicador_pedagio=tp_ind_ped_dom.vl(+)");
		sql.addCustomCriteria("cot.id_moeda=moeda_cotacao.id_moeda(+)");
		sql.addCustomCriteria("tp.id_moeda=moeda_tab_preco.id_moeda(+)");
		sql.addCustomCriteria("cot.id_cotacao="+parameters.get("idCotacao"));

		return sql;
	}

	private String getSelectDominio(String nomeDominio)	{
		StringBuilder sb = new StringBuilder()
			.append("(SELECT vdom.vl_valor_dominio as vl,")
			.append(PropertyVarcharI18nProjection.createProjection("vdom.ds_valor_dominio_i")).append(" as ds ")
			.append("FROM dominio dom, valor_dominio vdom ")
			.append("WHERE dom.id_dominio = vdom.id_dominio ")
			.append("  AND dom.nm_dominio='"+nomeDominio+"') ");
		return sb.toString();
	}

	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}

	public void setTabelaServicoAdicionalService(TabelaServicoAdicionalService tabelaServicoAdicionalService) {
		this.tabelaServicoAdicionalService = tabelaServicoAdicionalService;
	}
}