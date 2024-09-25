/**
 * 
 */
package com.mercurio.lms.seguros.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;

/**
 * @author lalmeida
 *
 * @spring.bean id="lms.seguros.relatorioManterSegurosClienteService"
 * @spring.property name="reportName" value="com/mercurio/lms/seguros/report/relatorioManterSegurosClienteExcel.jasper"
 */
public class RelatorioManterSegurosClienteService extends ReportServiceSupport {

	private static final String QUERY_KEY = "QUERY";
	private static final String CRITERIOS_KEY = "CRITERIOS";
	
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.report.ReportServiceSupport#execute(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap criteria = (TypedFlatMap) parameters;
				
        Map<String, Object> sql = mountSql(criteria);
        // Recupera a query
        final String query = (String)sql.get(QUERY_KEY);
        // Recupera os critérios de pesquisa
        final Map<String, String> criterios = (Map<String, String>)sql.get(CRITERIOS_KEY);
        
        JRReportDataObject jRReportDataObject = executeQuery(query, new Object[] { });

        Map parametersReport = new HashMap();
        parametersReport.put("TP_MODAL", criterios.containsKey("TP_MODAL") ? (String)criterios.get("TP_MODAL") : "");
    	parametersReport.put("TP_ABRANGENCIA", criterios.containsKey("TP_ABRANGENCIA") ? (String)criterios.get("TP_ABRANGENCIA") : "");
    	parametersReport.put("ID_TIPOSEGURO", criterios.containsKey("ID_TIPOSEGURO") ? (String)criterios.get("ID_TIPOSEGURO") : "");
    	parametersReport.put("ID_CLIENTE", criterios.containsKey("ID_CLIENTE") ? (String)criterios.get("ID_CLIENTE") : "");
    	parametersReport.put("NM_CLIENTE", criterios.containsKey("NM_CLIENTE") ? (String)criterios.get("NM_CLIENTE") : "");
    	parametersReport.put("ID_SEGURADORA", criterios.containsKey("ID_SEGURADORA") ? (String)criterios.get("ID_SEGURADORA") : "");
    	parametersReport.put("ID_REGULADORA", criterios.containsKey("ID_REGULADORA") ? (String)criterios.get("ID_REGULADORA") : "");
        
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        
        jRReportDataObject.setParameters(parametersReport);
        
        return jRReportDataObject;
	}
	
	private Map<String, Object> mountSql(TypedFlatMap criteria) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// Indica quais filtros foram utilizados na pesquisa
		Map<String, String> criterios = new HashMap<String, String>();
		
		String tpModal = criteria.getString("tpModal");
    	String tpAbrangencia = criteria.getString("tpAbrangencia");
    	Long idTipoSeguro = criteria.getLong("tipoSeguro.idTipoSeguro");
    	Long idCliente = criteria.getLong("cliente.idCliente");
    	Long idSeguradora = criteria.getLong("seguradora.idSeguradora");
    	Long idReguladora = criteria.getLong("reguladoraSeguro.idReguladora");
		
		StringBuilder sql = new StringBuilder()
			.append("SELECT ")
			.append("pes_1.nr_identificacao      		as nr_identificacao, ")
			.append("pes_1.nm_pessoa             		as nm_cliente, ")
			.append("VI18n(vd_mod.ds_valor_dominio_i)   as modal, ")
			.append("VI18n(vd_abr.ds_valor_dominio_i)	as abrangencia, ")
			.append("ts.sg_tipo                  		as sg_tipo, ")
			.append("pes_2.nm_pessoa             		as nm_seguradora, ")
			.append("pes_0.nm_pessoa             		as nm_corretora, ")
			.append("sg.ds_apolice               		as ds_apolice, ")
			.append("VI18n(vd_emis.ds_valor_dominio_i)  	as bl_em_emissao, ")
			.append("mo.sg_moeda || ' ' || mo.ds_simbolo 	as ds_simbolo, ")
			.append("sg.vl_limite                					as vl_limite, ")
			.append("TO_CHAR(sg.dt_vigencia_inicial, 'DD/MM/YYYY') 	as dt_vigencia_inicial, ")
			.append("TO_CHAR(sg.dt_vigencia_final, 'DD/MM/YYYY') 	as dt_vigencia_final, ")
			.append("sg.pc_taxa                  as pc_taxa, ")
			.append("muo.nm_municipio            as nm_municipio_origem, ")
			.append("ufo.sg_unidade_federativa   as sg_unidade_federativa_origem, ")
			.append("mud.nm_municipio            as nm_municipio_destino, ")
			.append("ufd.sg_unidade_federativa   as sg_unidade_federativa_destino, ")
			.append("(SELECT ")
			.append("  usu_1.nr_matricula ")
			.append(" FROM ")
			.append("  seguro_cliente sc_1, ")
			.append("  promotor_cliente pc_1, ")
			.append("  usuario usu_1 ")
			.append(" WHERE ")
			.append("  sc_1.id_cliente           = pc_1.id_cliente ")
			.append(" AND pc_1.id_usuario        = usu_1.id_usuario")
			.append(" AND (pc_1.dt_fim_promotor  IS NULL) ")
			.append(" AND pc_1.id_usuario        <> 5000 ")
			.append(" AND (pc_1.tp_modal         = sc_1.tp_modal ")
			.append("  OR pc_1.tp_modal          IS NULL) ")
			.append(" AND (pc_1.tp_abrangencia   = sc_1.tp_abrangencia ")
			.append("  OR pc_1.tp_abrangencia    IS NULL) ")
			.append(" AND sc_1.id_seguro_cliente = sg.id_seguro_cliente ")
			.append(" AND rownum                 <= 1) AS nr_matricula_resp_comercial, ")
			.append("(SELECT ")
			.append(" usu_1.nm_usuario ")
			.append(" FROM ")
			.append("  seguro_cliente sc_1, ")
			.append("  promotor_cliente pc_1, ")
			.append("  usuario usu_1 ")
			.append(" WHERE ")
			.append("  sc_1.id_cliente           = pc_1.id_cliente ")
			.append(" AND pc_1.id_usuario        = usu_1.id_usuario")
			.append(" AND (pc_1.dt_fim_promotor  IS NULL) ")
			.append(" AND pc_1.id_usuario        <> 5000 ")
			.append(" AND (pc_1.tp_modal         = sc_1.tp_modal ")
			.append("  OR pc_1.tp_modal          IS NULL) ")
			.append(" AND (pc_1.tp_abrangencia   = sc_1.tp_abrangencia ")
			.append("  OR pc_1.tp_abrangencia    IS NULL) ")
			.append(" AND sc_1.id_seguro_cliente = sg.id_seguro_cliente ")
			.append(" AND rownum                 <= 1) AS nm_usuario_resp_comercial, ")
			.append("TO_CHAR(sg.dh_envio_aviso, 'DD/MM/YYYY HH24:MI') as dh_envio_aviso, ")
			.append("usu.nr_matricula      as nr_matricula_usuario_avisado, ")
			.append("usu.nm_usuario        as nm_usuario_avisado, ")
			.append("sg.ds_cobertura       as ds_cobertura, ")
			.append("sg.ds_mercadoria      as ds_mercadoria, ")
			.append("sg.ds_embalagem       as ds_embalagem ")
			.append("FROM ")
			.append("valor_dominio vd_mod, ")
			.append("valor_dominio vd_abr, ")
			.append("valor_dominio vd_emis, ")
			.append("seguro_cliente sg, ")
			.append("reguladora_seguro rs, ")
			.append("pessoa pes_0, ")
			.append("cliente cli, ")
			.append("pessoa pes_1, ")
			.append("seguradora se, ")
			.append("pessoa pes_2, ")
			.append("moeda mo, ")
			.append("municipio muo, ")
			.append("unidade_federativa ufo, ")
			.append("municipio mud, ")
			.append("unidade_federativa ufd, ")
			.append("usuario usu, ")
			.append("tipo_seguro ts, ")
			.append("dominio do_mod, ")
			.append("dominio do_abr, ")
			.append("dominio do_emis ")
			.append("WHERE ")
			.append("sg.id_pessoa_reguladora           = rs.id_reguladora(+) ")
			.append("AND rs.id_reguladora              = pes_0.id_pessoa(+) ")
			.append("AND sg.id_cliente                 = cli.id_cliente ")
			.append("AND cli.id_cliente                = pes_1.id_pessoa ")
			.append("AND sg.id_seguradora              = se.id_seguradora(+) ")
			.append("AND se.id_seguradora              = pes_2.id_pessoa(+) ")
			.append("AND sg.id_moeda                   = mo.id_moeda ")
			.append("AND sg.id_municipio_origem        = muo.id_municipio(+) ")
			.append("AND muo.id_unidade_federativa     = ufo.id_unidade_federativa(+) ")
			.append("AND sg.id_municipio_destino       = mud.id_municipio(+) ")
			.append("AND mud.id_unidade_federativa     = ufd.id_unidade_federativa(+) ")
			.append("AND sg.id_usuario_aviso           = usu.id_usuario(+) ")
			.append("AND sg.id_tipo_seguro             = ts.id_tipo_seguro ")
			.append("AND vd_mod.id_dominio             = do_mod.id_dominio ")
			.append("AND vd_abr.id_dominio             = do_abr.id_dominio ")
			.append("AND vd_emis.id_dominio            = do_emis.id_dominio ")
			.append("AND vd_mod.vl_valor_dominio       = sg.tp_modal ")
			.append("AND vd_abr.vl_valor_dominio       = sg.tp_abrangencia ")
			.append("AND vd_emis.vl_valor_dominio      = sg.bl_em_emissao ")
			.append("AND do_mod.nm_dominio             = 'DM_MODAL' ") 
			.append("AND do_abr.nm_dominio             = 'DM_ABRANGENCIA' ")
			.append("AND do_emis.nm_dominio            = 'DM_SIM_NAO' ");
		
			if(!tpModal.isEmpty()) {
				sql.append("AND sg.tp_modal = '").append(tpModal).append("' ");
				criterios.put("TP_MODAL", criteria.getString("dsModal"));
			}
			
			if(!tpAbrangencia.isEmpty()) {
				sql.append("AND sg.tp_abrangencia = '").append(tpAbrangencia).append("' ");
				criterios.put("TP_ABRANGENCIA", criteria.getString("dsAbrangencia"));
			}
			
			if(idTipoSeguro != null) {
				sql.append("AND ts.id_tipo_seguro = ").append(idTipoSeguro).append(" ");
				criterios.put("ID_TIPOSEGURO", criteria.getString("tipoSeguro.sgTipo"));
			}
			
			if(idCliente != null) {
				sql.append("AND sg.id_cliente = ").append(idCliente).append(" ");
				criterios.put("ID_CLIENTE", FormatUtils.formatCNPJ(criteria.getString("cliente.identificacao")));
				criterios.put("NM_CLIENTE", criteria.getString("cliente.pessoa.nmPessoa"));
			}
			
			if(idSeguradora != null){
				sql.append("AND se.id_seguradora = ").append(idSeguradora).append(" ");
				criterios.put("ID_SEGURADORA", criteria.getString("seguradora.pessoa.nmPessoa"));
			}
			
			if (idReguladora != null) {
				sql.append("AND rs.id_reguladora = ").append(idReguladora).append(" ");
				criterios.put("ID_REGULADORA", criteria.getString("reguladoraSeguro.pessoa.nmPessoa"));
			}
			
			sql.append("ORDER BY ")
			.append("vd_mod.ds_valor_dominio_i, ")
			.append("vd_abr.ds_valor_dominio_i, ")
			.append("ts.sg_tipo, ")
			.append("pes_0.nm_pessoa, ")
			.append("sg.dt_vigencia_inicial");

			// Adiciona a query no retorno
			result.put(QUERY_KEY, sql.toString());
			
			// Adiciona os critérios utilizados na pesquisa
			result.put(CRITERIOS_KEY, criterios);
			
		return result;
	}
}
