package com.mercurio.lms.portaria.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel;
import com.mercurio.lms.fretecarreteiroviagem.model.service.TipoCombustivelService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.property name="reportName" value="com/mercurio/lms/portaria/report/relatorioEmissaoCO2.jasper" 
 * @spring.bean id="lms.portaria.relatorioEmissaoCO2Service"
 */
public class RelatorioEmissaoCO2Service extends ReportServiceSupport{

	private FilialService filialService;
	private TipoCombustivelService tipoCombustivelService;
	private TipoMeioTransporteService tipoMeioTransporteService;
	private DomainValueService domainValueService;
	private ConfiguracoesFacade configuracoesFacade;
	
	/**
	 * Método responsável pela execução do relatório
	 */
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap map = (TypedFlatMap) parameters;
		
		this.validarPeriodo(map);
		
		SqlTemplate sql = getSqlRelatorioEmissaoCO2(map);
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

	    Map parametersReport = new HashMap();
		parametersReport.put("PARAMETROS_PESQUISA",  getFiltros(map));
        parametersReport.put("USUARIO_EMISSOR", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		jr.setParameters(parametersReport);
		return jr;
	}
	
	private void validarPeriodo(TypedFlatMap parameters) {
		Integer intervalo = JTDateTimeUtils.getIntervalInDays(parameters.getYearMonthDay("dtEmissaoInicial"), parameters.getYearMonthDay("dtEmissaoFinal"));
		
		Integer periodo = Integer.valueOf(configuracoesFacade.getValorParametro("PERIODO_RELATORIO_CO2").toString());
		if (intervalo.compareTo(periodo) >= 0) {
			throw new BusinessException("LMS-10061", new Object[]{periodo.toString()});
		}
	}

	/**
	 * Método responsável pela descrição dos filtros do relatório
	 */
	public String getFiltros(TypedFlatMap parameters){
		
		Long idFilial = parameters.getLong("idFilial");
		Long idTpCombustivel = parameters.getLong("idTipoCombustivel");
		Long idTpMeioTransporte = parameters.getLong("idTipoMeioTransporte");

		String tpVinculo = parameters.getString("tpVinculo");
		String tpModal = parameters.getString("tpModal");
		
		String dtInicio = parameters.getYearMonthDay("dtEmissaoInicial").toString("dd/MM/yyyy");
		String dtFim = parameters.getYearMonthDay("dtEmissaoFinal").toString("dd/MM/yyyy");
		
		StringBuffer sb =new StringBuffer();
		
		if(idFilial != null){
			Filial filial = filialService.findById(idFilial);		
			sb.append("Filial: " + filial.getSgFilial() + " - ");			
		}
		
		if(idTpCombustivel != null){
			TipoCombustivel tipoCombustivel = tipoCombustivelService.findById(idTpCombustivel);
			sb.append("Tipo de Combustível: " + tipoCombustivel.getDsTipoCombustivel() + " - ");
		}
		
		if(idTpMeioTransporte != null){
			TipoMeioTransporte tipoMeioTransporte = tipoMeioTransporteService.findById(idTpMeioTransporte);
			sb.append("Tipo Meio de Transporte: " + tipoMeioTransporte.getDsTipoMeioTransporte() + " - ");
		}
		
		if(tpVinculo != null){
			String vinculo = domainValueService.findDomainValueDescription("DM_TIPO_VINCULO_VEICULO", tpVinculo);			
			sb.append("Vínculo: " + vinculo + " - ");
		}
		
		if(tpModal != null){
			String modal = domainValueService.findDomainValueDescription("DM_MODAL", tpModal);
			sb.append("Modalidade: " + modal + " - ");
		}
		
		sb.append("Período: " + dtInicio + " até " + dtFim);
		
		return sb.toString();
	}
	
	/**
	 * Método responsável pelo SQL do relatório
	 */
	public SqlTemplate getSqlRelatorioEmissaoCO2(TypedFlatMap parameters){
		List<Object> values = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		Long idFilial = parameters.getLong("idFilial");
		Long tpCombustivel = parameters.getLong("idTipoCombustivel");
		Long tpMeioTransporte = parameters.getLong("idTipoMeioTransporte");

		String tpVinculo = parameters.getString("tpVinculo");
		String tpModal = parameters.getString("tpModal");
		
		String dtInicio = parameters.getYearMonthDay("dtEmissaoInicial").toString("dd/MM/yyyy");
		String dtFim = parameters.getYearMonthDay("dtEmissaoFinal").toString("dd/MM/yyyy");
		
		
		sb.append(" SELECT  filial AS FILIAL, ");
		sb.append("			nr_frota AS NR_FROTA, ");
		sb.append("			nr_identificador AS NR_IDENTIFICADOR, ");
		sb.append("			combustivel AS COMBUSTIVEL, ");
		sb.append("			SUM(quilometragem)		AS QUILOMETRAGEM, ");
		sb.append("			vinculo AS VINCULO, ");
		sb.append("			tp_meio_transporte AS TP_MEIO_TRANSPORTE, ");
		sb.append("			modalidade AS MODALIDADE ");
		
		sb.append(" FROM ");
		
		sb.append(" 	( SELECT parameter.km_min            			AS p_km_min, ");
		sb.append(" 	 		 parameter.km_max            			AS p_km_max, ");
		sb.append(" 	 		 f.sg_filial                 			AS 	filial, ");
		sb.append(" 	 		 mt.nr_frota                 			AS nr_frota, ");
		sb.append(" 	 		 mt.nr_identificador         			AS nr_identificador, ");
		
		sb.append(" 	 		 " + PropertyVarcharI18nProjection.createProjection("tc.ds_tipo_combustivel_i", "combustivel") + ", ");
		
		sb.append(" 	 		 (cdq.km_final - cdq.km_inicial) 									AS quilometragem, ");
		
		sb.append(" 	 		 ( SELECT REGEXP_REPLACE( vd.ds_valor_dominio_i , ");
		sb.append(" 	 		 					'^.*pt_BR»(([[:alnum:]]|[[:space:]])+).*$', '\\1' )");
		sb.append("					FROM valor_dominio vd ");
		sb.append("					JOIN   dominio       d  ON d.id_dominio = vd.id_dominio ");
		sb.append("					WHERE  d.nm_dominio = 'DM_TIPO_VINCULO_VEICULO' ");
		sb.append("					AND    vd.vl_valor_dominio = mt.tp_vinculo ) 					AS vinculo, ");
		
		sb.append("					tmt.ds_tipo_meio_transporte 		AS tp_meio_transporte, ");
		
		sb.append(" 	 		 ( SELECT REGEXP_REPLACE( vd.ds_valor_dominio_i , ");
		sb.append(" 	 		 					'^.*pt_BR»(([[:alnum:]]|[[:space:]])+).*$', '\\1' )");
		sb.append("					FROM valor_dominio vd ");
		sb.append("					JOIN   dominio       d  ON d.id_dominio = vd.id_dominio ");
		sb.append("					WHERE  d.nm_dominio = 'DM_TIPO_MEIO_TRANSPORTE' ");
		sb.append("					AND    vd.vl_valor_dominio = tmt.tp_meio_transporte ) 			 AS modalidade ");
		
		sb.append("		FROM ");
		sb.append("			( SELECT f.id_filial                   		AS id_f, ");
		
		sb.append(expressao("        ?           AS tp_vinculo, ", tpVinculo, values));
		sb.append(expressao("        ?           AS tp_modal, ", tpModal, values));
		sb.append(expressao("        ?           AS id_tmt, ", tpMeioTransporte, values));
		sb.append(expressao("        ?           AS id_comb, ", tpCombustivel, values));

		sb.append("			 		 ( SELECT CAST( pg.ds_conteudo AS INTEGER ) ");
		sb.append("			 		   FROM   parametro_geral pg ");
		sb.append("			 		   WHERE  pg.nm_parametro_geral = 'KM_MIN_RELATORIO_CO2' ) 		  AS km_min, ");
		
		sb.append("			 		 ( SELECT CAST( pg.ds_conteudo AS INTEGER ) ");
		sb.append("			 		   FROM   parametro_geral pg ");
		sb.append("			 		   WHERE  pg.nm_parametro_geral = 'KM_MAX_RELATORIO_CO2' ) 		  AS km_max ");
		
		sb.append("			FROM filial f ");
		sb.append("			JOIN   historico_filial hf ON f.id_filial = hf.id_filial ");
		
		if(idFilial != null){
			sb.append("			WHERE  f.id_filial = ? ");
			sb.append("			AND    SYSDATE BETWEEN hf.dt_real_operacao_inicial AND hf.dt_real_operacao_final ) parameter, ");
			values.add(idFilial);
		} else {
			sb.append("			WHERE    SYSDATE BETWEEN hf.dt_real_operacao_inicial AND hf.dt_real_operacao_final ) parameter, ");
		}
		
		sb.append("				   meio_transporte        mt ");
		sb.append("		JOIN   filial                 f   ON mt.id_filial = f.id_filial ");
		sb.append("		JOIN   modelo_meio_transporte mmt ON mmt.id_modelo_meio_transporte = mt.id_modelo_meio_transporte ");
		sb.append("		JOIN   tipo_meio_transporte   tmt ON mmt.id_tipo_meio_transporte = tmt.id_tipo_meio_transporte ");
		sb.append("		JOIN   tipo_combustivel       tc  ON tc.id_tipo_combustivel = mt.id_tipo_combustivel ");
		sb.append("		JOIN   ( SELECT cc.id_controle_carga         AS id_controle_carga, ");
		sb.append("						cc.id_transportado           AS id_meio_transporte, ");
		sb.append("						cc.dh_saida_coleta_entrega   AS dh_saida, ");
		sb.append("						cc.dh_chegada_coleta_entrega AS dh_chegada, ");
		sb.append("						cc.dh_geracao                AS dh_geracao, ");
		sb.append("						cqs.nr_quilometragem         AS km_inicial, ");
		sb.append("						cqc.nr_quilometragem + DECODE( cqc.bl_virou_hodometro, 'S', POWER( 10, 6 ), 0 ) AS km_final ");
		
		sb.append("				FROM controle_carga cc");
		sb.append("				JOIN   controle_quilometragem cqs ON cc.id_controle_carga = cqs.id_controle_carga ");
		sb.append("				JOIN   controle_quilometragem cqc ON cc.id_controle_carga = cqc.id_controle_carga ");
		sb.append("				WHERE  cc.tp_status_controle_carga = 'FE' ");
		sb.append("				AND    NOT EXISTS ( SELECT 1  ");
		sb.append("									FROM   manifesto m ");
		sb.append("									WHERE  m.id_controle_carga = cc.id_controle_carga ");
		sb.append("									AND    m.tp_manifesto_entrega = 'EP' ");
		sb.append("									AND    cc.tp_controle_carga = 'C' ");
		sb.append("									and    rownum = 1 ) ");
		sb.append("				AND    cqc.id_controle_quilometragem = ( SELECT MAX( id_controle_quilometragem ) ");
		sb.append("														 FROM   controle_quilometragem cq ");
		sb.append("														 WHERE  cq.bl_saida = 'N' ");
		sb.append("														 AND    cq.id_controle_carga = cc.id_controle_carga ) ");
		sb.append("				AND    cqs.id_controle_quilometragem = ( SELECT MIN( id_controle_quilometragem ) ");
		sb.append("	                                                     FROM   controle_quilometragem cq ");
		sb.append("	                                                     WHERE  cq.bl_saida = 'S' ");
		sb.append("	                                                     AND    cq.id_controle_carga = cc.id_controle_carga ) ");
		sb.append("				AND    cc.dh_geracao BETWEEN to_date( ? , 'dd/MM/yyyy') AND to_date(?, 'dd/MM/yyyy') + 1");
		sb.append("				) cdq ON cdq.id_meio_transporte = mt.id_meio_transporte ");
		
		values.add(dtInicio );
		values.add(dtFim );
		
		sb.append("		WHERE NVL( parameter.id_f, -1 )           IN ( -1, f.id_filial ) ");
		sb.append("		AND   NVL( parameter.tp_vinculo, 'null' ) IN ( 'null', mt.tp_vinculo ) ");
		sb.append("		AND   NVL( parameter.tp_modal, 'null' )   IN ( 'null', mt.tp_modal ) ");
		sb.append("		AND   NVL( parameter.id_tmt, -1 )         IN ( -1, mmt.id_tipo_meio_transporte ) ");
		sb.append("		AND   NVL( parameter.id_comb, -1 )        IN ( -1, tc.id_tipo_combustivel ) ");
		sb.append("		) tb ");

		sb.append(" having SUM(quilometragem) >= tb.p_km_min ");
		sb.append(" AND   (   SUM(quilometragem) <= tb.p_km_max OR tb.p_km_max = 0 ) ");
		
		sb.append(" GROUP BY filial, ");
		sb.append(" 		 nr_frota, ");
		sb.append(" 		 nr_identificador, ");
		sb.append(" 		 combustivel, ");
		sb.append(" 		 vinculo, ");
		sb.append(" 		 tp_meio_transporte, ");
		sb.append(" 		 modalidade, ");
		sb.append(" 		 p_km_min, ");
		sb.append(" 		 p_km_max ");
		
		sb.append(" ORDER BY filial, ");
		sb.append(" 		 nr_frota, ");
		sb.append(" 		 combustivel ");
		
		SqlTemplate sqlTemplate = new SqlTemplate(sb.toString());
		sqlTemplate.addCriteriaValue(values.toArray());
		return sqlTemplate;
	}
	
	public static String expressao(String string, Object e, List<Object> values) {
		if(e!=null){
			values.add(e);
			return string;
		}else{
			return string.replaceAll("[?]", "null");
		}
	}
	
	public void setFilialService(FilialService filialService){
		this.filialService = filialService;
	}

	public void setTipoCombustivelService(TipoCombustivelService tipoCombustivelService){
		this.tipoCombustivelService = tipoCombustivelService;
	}
	
	public void setTipoMeioTransporteService(TipoMeioTransporteService tipoMeioTransporteService){
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}
	
	public void setDomainValueService(DomainValueService domainValueService){
		this.domainValueService = domainValueService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
