package com.mercurio.lms.seguros.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author rwendt@voiza.com.br
 *
 * @spring.bean id="lms.seguros.relatorioManterAverbacoesService"
 * @spring.property name="reportName" value="com/mercurio/lms/seguros/report/relatorioManterAverbacoesExcel.jasper"
 */
public class RelatorioManterAverbacoesService extends ReportServiceSupport {
	
	private static final String QUERY_KEY = "QUERY";
	private static final String CRITERIOS_KEY = "CRITERIOS";

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
        parametersReport.put("ID_CLIENTE", criterios.containsKey("ID_CLIENTE") ? (String)criterios.get("ID_CLIENTE") : "");
    	parametersReport.put("NM_CLIENTE", criterios.containsKey("NM_CLIENTE") ? (String)criterios.get("NM_CLIENTE") : "");
    	parametersReport.put("TP_MODAL", criterios.containsKey("TP_MODAL") ? (String)criterios.get("TP_MODAL") : "");
    	parametersReport.put("ID_TIPO_FRETE", criterios.containsKey("ID_TIPO_FRETE") ? (String)criterios.get("ID_TIPO_FRETE") : "");
    	parametersReport.put("ID_TIPO_SEGURO", criterios.containsKey("ID_TIPO_SEGURO") ? (String)criterios.get("ID_TIPO_SEGURO") : "");
    	parametersReport.put("ID_REGULADORA", criterios.containsKey("ID_REGULADORA") ? (String)criterios.get("ID_REGULADORA") : "");
    	parametersReport.put("ID_SEGURADORA", criterios.containsKey("ID_SEGURADORA") ? (String)criterios.get("ID_SEGURADORA") : "");
    	parametersReport.put("DT_PERIODO_VIAGEM_INICIAL", criterios.containsKey("DT_PERIODO_VIAGEM_INICIAL") ? (String)criterios.get("DT_PERIODO_VIAGEM_INICIAL")  : "");
    	parametersReport.put("DT_PERIODO_VIAGEM_FINAL", criterios.containsKey("DT_PERIODO_VIAGEM_FINAL") ? (String)criterios.get("DT_PERIODO_VIAGEM_FINAL") : "");
    	parametersReport.put("FROTA", criterios.containsKey("FROTA") ? (String)criterios.get("FROTA") : "");
    	parametersReport.put("PLACA", criterios.containsKey("PLACA") ? (String)criterios.get("PLACA") : "");
    	parametersReport.put("SG_FILIAL_ORIGEM", criterios.containsKey("SG_FILIAL_ORIGEM") ? (String)criterios.get("SG_FILIAL_ORIGEM") : "");
    	parametersReport.put("FILIAL_ORIGEM", criterios.containsKey("FILIAL_ORIGEM") ? (String)criterios.get("FILIAL_ORIGEM") : "");
    	parametersReport.put("SG_FILIAL_DESTINO", criterios.containsKey("SG_FILIAL_DESTINO") ? (String)criterios.get("SG_FILIAL_DESTINO") : "");
    	parametersReport.put("FILIAL_DESTINO", criterios.containsKey("FILIAL_DESTINO") ? (String)criterios.get("FILIAL_DESTINO") : "");
    	
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        
        jRReportDataObject.setParameters(parametersReport);
        
        return jRReportDataObject;
	}
	
	private Map<String, Object> mountSql(TypedFlatMap criteria) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// Indica quais filtros foram utilizados na pesquisa
		Map<String, String> criterios = new HashMap<String, String>();
				
		Long idCliente = criteria.getLong("cliente.idCliente");
		String tpModal = criteria.getString("tpModal");
		String tpFrete = criteria.getString("tpFrete");
		Long idTipoSeguro = criteria.getLong("tipoSeguro.idTipoSeguro");
		Long idReguladora = criteria.getLong("reguladoraSeguro.idReguladora");
		Long idSeguradora = criteria.getLong("seguradora.idSeguradora");
		String dtInicioViagem = criteria.getYearMonthDay("dtInicioViagem") == null ? "" : criteria.getYearMonthDay("dtInicioViagem").toString("dd/MM/yyyy");
    	String dtFimViagem = criteria.getYearMonthDay("dtFimViagem") == null ? "" : criteria.getYearMonthDay("dtFimViagem").toString("dd/MM/yyyy");
    	Long idMeioTransporte = criteria.getLong("meioTransporteRodoviario.idMeioTransporte");
    	Long idFilialOrigem = criteria.getLong("filialOrigem.idFilial");
    	Long idFilialDestino = criteria.getLong("filialDestino.idFilial");
		
		StringBuilder sql = new StringBuilder()
			.append("SELECT ")
			.append("pes_1.nr_identificacao              AS nr_identificacao, ")
			.append("pes_1.nm_pessoa                     AS nm_cliente, ")
			.append("(SELECT VI18n(vd_mod.ds_valor_dominio_i) ")
			.append("FROM dominio do_mod, ")
			.append("valor_dominio vd_mod ")
			.append("WHERE do_mod.nm_dominio = 'DM_MODAL' ")
			.append("AND do_mod.id_dominio = vd_mod.id_dominio ")
			.append("AND av.tp_modal         = vd_mod.vl_valor_dominio")
			.append(") AS modal, ")
			.append("(SELECT VI18n(vd_tpf.ds_valor_dominio_i) ")
			.append("FROM dominio do_tf, ")
			.append("valor_dominio vd_tpf ")
			.append("WHERE do_tf.nm_dominio = 'DM_TIPO_FRETE' ")
			.append("AND do_tf.id_dominio = vd_tpf.id_dominio ")
			.append("AND av.tp_frete         = vd_tpf.vl_valor_dominio")
			.append(") AS tipo_frete, ")
			.append("ts.sg_tipo                          AS tipo_seguro, ")
			.append("pes_0.nm_pessoa                     AS nm_corretora, ")
			.append("pes_2.nm_pessoa                     AS nm_seguradora, ")
			.append("TO_CHAR(av.dt_viagem, 'DD/MM/YYYY') AS dt_viagem, ")
			.append("av.vl_estimado                      AS vl_estimado, ")
			.append("av.ps_total                         AS peso_total, ")
			.append("av.ds_nf                            AS nfs, ")
			.append("flo.sg_filial                       AS filial_origem, ")
			.append("fld.sg_filial                       AS filial_destino, ")
			.append("muo.nm_municipio                    AS nm_municipio_origem, ")
			.append("ufo.sg_unidade_federativa           AS sg_unidade_federativa_origem, ")
			.append("mud.nm_municipio                    AS nm_municipio_destino, ")
			.append("ufd.sg_unidade_federativa           AS sg_unidade_federativa_destino, ")
			.append("mt.nr_frota                         AS frota, ")
			.append("mt.nr_identificador                 AS placa, ")
			.append("av.ds_contingencia                  AS contingencia, ")
			.append("av.ob_averbacao                     AS observacoes ")
			.append("FROM ")
			.append("averbacao av, ")
			.append("tipo_seguro ts, ")
			.append("reguladora_seguro rs, ")
			.append("seguradora se, ")
			.append("filial flo, ")
			.append("filial fld, ")
			.append("municipio muo, ")
			.append("unidade_federativa ufo, ")
			.append("municipio mud, ")
			.append("unidade_federativa ufd, ")
			.append("pessoa pes_0, ")
			.append("pessoa pes_1, ")
			.append("pessoa pes_2, ")
			.append("cliente cli, ")
			.append("meio_transporte mt ")
			.append("WHERE av.id_corretora 		   = rs.id_reguladora(+) ")
			.append("AND rs.id_reguladora          = pes_0.id_pessoa(+) ")
			.append("AND av.id_cliente             = cli.id_cliente ")
			.append("AND cli.id_cliente            = pes_1.id_pessoa ")
			.append("AND av.id_seguradora          = se.id_seguradora(+) ")
			.append("AND se.id_seguradora          = pes_2.id_pessoa(+) ")
			.append("AND av.id_municipio_origem    = muo.id_municipio ")
			.append("AND muo.id_unidade_federativa = ufo.id_unidade_federativa(+) ")
			.append("AND av.id_municipio_destino   = mud.id_municipio ")
			.append("AND mud.id_unidade_federativa = ufd.id_unidade_federativa(+) ")
			.append("AND av.id_tipo_seguro         = ts.id_tipo_seguro ")
			.append("AND av.id_filial_origem       = flo.id_filial ")
			.append("AND av.id_filial_destino      = fld.id_filial ")
			.append("AND av.id_meio_transporte     = mt.id_meio_transporte(+) ");
			
			if(idCliente != null) {
				sql.append("AND av.id_cliente = ").append(idCliente).append(" ");
				criterios.put("ID_CLIENTE", criteria.getString("cliente.identificacao"));
				criterios.put("NM_CLIENTE", criteria.getString("cliente.pessoa.nmPessoa"));
			}
		
			if(!tpModal.isEmpty()) {
				sql.append("AND av.tp_modal = '").append(tpModal).append("' ");
				criterios.put("TP_MODAL", criteria.getString("dsModal"));
			}
			
			if(!tpFrete.isEmpty()) {
				sql.append("AND av.tp_frete = '").append(tpFrete).append("' ");
				criterios.put("ID_TIPO_FRETE", criteria.getString("dsFrete"));
			}
			
			if(idTipoSeguro != null) {
				sql.append("AND av.id_tipo_seguro = ").append(idTipoSeguro).append(" ");
				criterios.put("ID_TIPO_SEGURO", criteria.getString("tipoSeguro.sgTipo"));
			}			
			
			if(idSeguradora != null) {
				sql.append("AND se.id_seguradora = ").append(idSeguradora).append(" ");
				criterios.put("ID_SEGURADORA", criteria.getString("seguradora.pessoa.nmPessoa"));
			}
			
			if(idReguladora != null) {
				sql.append("AND rs.id_reguladora = ").append(idReguladora).append(" ");
				criterios.put("ID_REGULADORA", criteria.getString("reguladoraSeguro.pessoa.nmPessoa"));
			}
			
			if(!("").equals(dtInicioViagem) && !("").equals(dtFimViagem)) {				
				sql.append("AND av.dt_viagem BETWEEN '").append(dtInicioViagem).append("' AND '").append(dtFimViagem).append("' ");
				criterios.put("DT_PERIODO_VIAGEM_INICIAL", dtInicioViagem);
				criterios.put("DT_PERIODO_VIAGEM_FINAL", dtFimViagem);
			}
			
			if(!("").equals(dtInicioViagem) && ("").equals(dtFimViagem)) {
				sql.append("AND av.dt_viagem >= '").append(dtInicioViagem).append("' ");
				criterios.put("DT_PERIODO_VIAGEM_INICIAL", dtInicioViagem);
			}
			
			if(("").equals(dtInicioViagem) && !("").equals(dtFimViagem)) {
				sql.append("AND av.dt_viagem <= '").append(dtFimViagem).append("' ");
				criterios.put("DT_PERIODO_VIAGEM_FINAL", dtFimViagem);
			}
			
			if(idMeioTransporte != null) {
				sql.append("AND mt.id_meio_transporte = ").append(idMeioTransporte).append(" ");
				criterios.put("FROTA", criteria.getString("meioTransporteRodoviario2.nrFrota"));
				criterios.put("PLACA", criteria.getString("meioTransporteRodoviario.nrIdentificador"));
			}
			
			if(idFilialOrigem != null) {
				sql.append("AND flo.id_filial = ").append(idFilialOrigem).append(" ");
				criterios.put("SG_FILIAL_ORIGEM", criteria.getString("sgFilialOrigem"));
				criterios.put("FILIAL_ORIGEM", criteria.getString("filialOrigem.pessoa.nmFantasia"));
			}
			
			if(idFilialDestino != null) {
				sql.append("AND fld.id_filial = ").append(idFilialDestino).append(" ");
				criterios.put("SG_FILIAL_DESTINO", criteria.getString("sgFilialDestino"));
				criterios.put("FILIAL_DESTINO", criteria.getString("filialDestino.pessoa.nmFantasia"));
			}
			
			sql.append("ORDER BY ")
			.append("pes_1.nm_pessoa, ")
			.append("av.dt_viagem");

			// Adiciona a query no retorno
			result.put(QUERY_KEY, sql.toString());
			
			// Adiciona os critérios utilizados na pesquisa
			result.put(CRITERIOS_KEY, criterios);
			
		return result;
	}
}
