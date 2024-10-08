package com.mercurio.lms.franqueados.report;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.franqueados.model.service.FranquiaService;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.franqueado.gerarCartaCessaoCreditoServiceAnexo"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/gerarCartaCessaoCreditoAnexo.jasper"
 */
public class GerarCartaCessaoCreditoServiceAnexo extends ReportServiceSupport{

	FranquiaService franquiaService;
	
	/**
     * M�todo respons�vel por gerar o relat�rio. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		String sql = getQuery(parameters);
		JRReportDataObject jr = executeQuery(sql, new Object[]{});
		
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", getFilterSummary(parameters));
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			Long idFranquia = (Long) parameters.get("idFilial");
			parametersReport.put("idFranquia", idFranquia);
			Map<String, Object> franquiaDados = franquiaService.findDadosFranquia((Long) idFranquia);
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
			
			if (franquiaDados.containsKey("sgFilial") && franquiaDados.get("sgFilial") != null) {
				parametersReport.put("dsComunicado", "Comunico que a "+franquiaDados.get("nmPessoa")+", CNPJ "+FormatUtils.formatCNPJ((String)franquiaDados.get("nrIdentificacao"))+", "
						+ "recebeu a carta de cess�o de cr�dito "+franquiaDados.get("sgFilial")+dtCompetencia.getYear()+String.format("%02d", dtCompetencia.getMonthOfYear())+
						" na data mencionada abaixo com os t�tulos relacionados a seguir:");
				parametersReport.put("nmPessoa", franquiaDados.get("nmPessoa"));
			}
			if (franquiaDados.containsKey("sgUnidadeFederativa") && franquiaDados.get("sgUnidadeFederativa") != null) {
				parametersReport.put("sgUnidadeFederativa", franquiaDados.get("sgUnidadeFederativa"));
			}
			
			parametersReport.put("franquiaAnoMes", getFranquiaAnoMes(parameters));
			
			if (franquiaDados.containsKey("nmMunicipio") && franquiaDados.get("nmMunicipio") != null) {
				parametersReport.put("nmMunicipio", franquiaDados.get("nmMunicipio"));
			}
			
		}
		YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
		parametersReport.put("competencia", dtCompetencia);
		Date dateNow = new Date();
		parametersReport.put("dateNow", dateNow);
		parametersReport.put("yearNow", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
		
		
		jr.setParameters(parametersReport);
		return jr;
	}
    
	@SuppressWarnings("rawtypes")
	private String getQuery(Map parameters){
	    String query = " SELECT FDS.SG_FILIAL AS SG_FILIAL_FDS, "
                + " DS.NR_DOCTO_SERVICO AS NR_DOCTO_SERVICO, "
                + " TO_CHAR(DS.DH_EMISSAO, 'dd/MM/yyyy') AS DH_EMISSAO, "
                + " P.NM_PESSOA AS NM_PESSOA, "
                + " DS.VL_TOTAL_DOC_SERVICO AS VL_TOTAL_DOC_SERVICO, "
                + " FF.SG_FILIAL AS SG_FILIAL_FF, "
                + " F.NR_FATURA AS NR_FATURA, "
                + " F.DT_VENCIMENTO AS DT_VENCIMENTO "
                + " FROM DOCTO_SERVICO DS, "
                + " FILIAL FDS, "
                + " DEVEDOR_DOC_SERV_FAT DEV, "
                + " PESSOA P, "
                + " ITEM_FATURA IF, "
                + " FATURA F, "
                + " FILIAL FF, "
                + " ITEM_REDECO IR, "
                + " REDECO R, "
                + " RELACAO_COBRANCA RC "
                
                + " WHERE R.ID_REDECO = IR.ID_REDECO "              
                + " AND R.ID_REDECO = RC.ID_REDECO "
                + " AND RC.ID_FILIAL = FF.ID_FILIAL "
                + " AND IR.ID_FATURA = F.ID_FATURA "                
                + " AND DEV.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO "
                
                + " AND DS.ID_FILIAL_ORIGEM = FDS.ID_FILIAL "
                + " AND IF.ID_DEVEDOR_DOC_SERV_FAT = DEV.ID_DEVEDOR_DOC_SERV_FAT "
                + " AND DEV.ID_CLIENTE = P.ID_PESSOA "
                + " AND IF.ID_FATURA = F.ID_FATURA "
                + " AND F.ID_FILIAL = FF.ID_FILIAL "
                + " AND IR.ID_FATURA = F.ID_FATURA "

                + " AND R.TP_FINALIDADE IN ('CJ','DF') ";
	    
	    
	    if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
            Long idFranquia = (Long) parameters.get("idFilial");
            query = query.concat(" AND RC.ID_FILIAL = :franquia ");
            query = query.replaceAll(":franquia", idFranquia.toString());
        }
        
        if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
            YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
            YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
            YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
            query = query.concat(" AND R.DT_LIQUIDACAO BETWEEN TO_DATE(':dtInicioCompetencia','dd/MM/yyyy') AND TO_DATE(':dtFimCompetencia','dd/MM/yyyy') ");
            String competenciaInicial = dtCompetenciaInicial.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
            String competenciaFinal = dtCompetenciaFinal.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
            query = query.replaceAll(":dtInicioCompetencia", competenciaInicial);
            query = query.replaceAll(":dtFimCompetencia", competenciaFinal);
        }

		return query;
	}
	
	@SuppressWarnings("rawtypes")
	protected String getFilterSummary(Map parameters) {
		SqlTemplate sql = createSqlTemplate();

		if (parameters.containsKey("dsFranquia") && parameters.get("dsFranquia") != null) {
			String dsFranquia = (String) parameters.get("dsFranquia");
			sql.addFilterSummary("franquia", dsFranquia);
		}

		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");

			String competencia = dtCompetencia.toString(DateTimeFormat.forPattern("MM/yyyy"));

			sql.addFilterSummary("competencia", competencia);
		}

		return sql.getFilterSummary();
	}
	
	@SuppressWarnings("rawtypes")
	protected String getFranquiaAnoMes(Map parameters) {
		String franquiaAnoMes = "";

		if (parameters.containsKey("dsFranquia") && parameters.get("dsFranquia") != null) {
			franquiaAnoMes = (String) parameters.get("dsFranquia");
		}

		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
			franquiaAnoMes = franquiaAnoMes + dtCompetencia.toString(DateTimeFormat.forPattern("yyyyMM"));
		}

		return franquiaAnoMes;
	}

	public void setFranquiaService(FranquiaService franquiaService) {
		this.franquiaService = franquiaService;
	}
    
}
