package com.mercurio.lms.municipios.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.McdService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.municipios.emitirFluxoCargaEntreFiliaisService"
 * @spring.property name="reportName" value="com/mercurio/lms/municipios/report/emitirFluxoCargaEntreFiliais.jasper"
 */

public class EmitirFluxoCargaEntreFiliaisService extends ReportServiceSupport {

	private McdService mcdService;
	private DomainService domainService;
	
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		DomainValue dvTipoEmissao = null;
		TypedFlatMap criteria = (TypedFlatMap)parameters;
		for (Iterator ie = criteria.keySet().iterator(); ie.hasNext();) {
			String key = (String)ie.next();
			if (!StringUtils.isBlank(criteria.getString(key)) && !key.startsWith("_"))
				break;
			else if (!ie.hasNext())
				throw new BusinessException("LMS-29112");
		}
		
		if (!criteria.getString("tpEmissao").equals("")) {
			Domain dmTipoEmissao = this.domainService.findByName("DM_TIPO_EMISSAO_MCD");
			dvTipoEmissao = dmTipoEmissao.findDomainValueByValue(criteria.getString("tpEmissao"));
		}	
		
		SqlTemplate sql = createSqlTemplate(criteria, dvTipoEmissao);
		
		Map parametersReport = new HashMap();
			parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
			parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
			parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
			parametersReport.put("tpEmissao", criteria.getString("tpEmissao"));
		
		JRReportDataObject jr = executeQuery(sql.getSql(),sql.getCriteria());
						   jr.setParameters(parametersReport);
		return jr;
	}
	private SqlTemplate createSqlTemplate(TypedFlatMap criteria, DomainValue dvTipoEmissao) {
		SqlTemplate sql = createSqlTemplate();

		//Projections
		sql.addProjection("DISTINCT FO.SG_FILIAL || ' - ' || PFO.NM_FANTASIA","NM_FILIAL_O");
		sql.addProjection("FD.SG_FILIAL || ' - ' || PFD.NM_FANTASIA","NM_FILIAL_D");
		sql.addProjection("FR.SG_FILIAL","SG_FILIAL_R");
		sql.addProjection("PFR.NM_FANTASIA","NM_FILIAL_R");
		sql.addProjection("RO.SG_REGIONAL || ' - ' || RO.DS_REGIONAL","NM_REGIONAL");
		sql.addProjection("FO.ID_FILIAL","ID_FILIAL_O");
		sql.addProjection("FD.ID_FILIAL","ID_FILIAL_D");
		sql.addProjection("UFO.NM_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PO.NM_PAIS_I", "NM_PAIS"));
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("S.DS_SERVICO_I", "DS_SERVICO"));
		sql.addProjection("S.ID_SERVICO", "ID_SERVICO");
		sql.addProjection("FF.BL_DOMINGO");
		sql.addProjection("FF.BL_SEGUNDA");
		sql.addProjection("FF.BL_TERCA");
		sql.addProjection("FF.BL_QUARTA");
		sql.addProjection("FF.BL_QUINTA");
		sql.addProjection("FF.BL_SEXTA");
		sql.addProjection("FF.BL_SABADO");
		sql.addProjection("FF.NR_DISTANCIA");
		sql.addProjection("FF.NR_GRAU_DIFICULDADE"); 
		
		if (!criteria.getString("tpEmissao").equals("")) {
			sql.addFilterSummary("tipoEmissao",dvTipoEmissao.getDescription().getValue(LocaleContextHolder.getLocale()));
			if (criteria.getString("tpEmissao").equals("D"))  
				sql.addProjection("(ceil((FF.NR_PRAZO/60) / 24))","NR_PRAZO");
			else 
				sql.addProjection("(FF.NR_PRAZO/60)","NR_PRAZO"); 
		}
		
		 //From's 
		StringBuffer sb = new StringBuffer("FLUXO_FILIAL FF \n")
		 				.append("INNER JOIN FILIAL FO ON FO.ID_FILIAL = FF.ID_FILIAL_ORIGEM \n")
		 				.append("INNER JOIN PESSOA PFO ON PFO.ID_PESSOA = FO.ID_FILIAL \n")
		 				.append("INNER JOIN FILIAL FD ON FD.ID_FILIAL = FF.ID_FILIAL_DESTINO \n")
		 				.append("INNER JOIN PESSOA PFD ON PFD.ID_PESSOA = FD.ID_FILIAL \n")
		 				.append("LEFT JOIN FILIAL FR ON FR.ID_FILIAL = FF.ID_FILIAL_REEMBARCADORA \n")
		 				.append("LEFT JOIN PESSOA PFR ON PFR.ID_PESSOA = FR.ID_FILIAL \n")
		 				.append("INNER JOIN REGIONAL_FILIAL RFO ON RFO.ID_FILIAL = FO.ID_FILIAL AND RFO.DT_VIGENCIA_INICIAL <= ? AND RFO.DT_VIGENCIA_FINAL >= ? \n")
		 				.append("INNER JOIN REGIONAL RO ON RO.ID_REGIONAL = RFO.ID_REGIONAL \n")
		 				.append("INNER JOIN REGIONAL_FILIAL RFD ON RFD.ID_FILIAL = FD.ID_FILIAL AND RFD.DT_VIGENCIA_INICIAL <= ? AND RFD.DT_VIGENCIA_FINAL >= ? \n")
		 				.append("INNER JOIN REGIONAL RD ON RD.ID_REGIONAL = RFD.ID_REGIONAL \n")
		 				.append("LEFT JOIN V_SERVICO_I S ON S.ID_SERVICO = FF.ID_SERVICO \n")
		 				.append("INNER JOIN ENDERECO_PESSOA EPO ON EPO.ID_ENDERECO_PESSOA = PFO.ID_ENDERECO_PESSOA AND EPO.DT_VIGENCIA_INICIAL <= ? AND EPO.DT_VIGENCIA_FINAL >= ? \n")
		 				.append("INNER JOIN MUNICIPIO MO ON MO.ID_MUNICIPIO = EPO.ID_MUNICIPIO \n")
		 				.append("INNER JOIN UNIDADE_FEDERATIVA UFO ON UFO.ID_UNIDADE_FEDERATIVA = MO.ID_UNIDADE_FEDERATIVA \n")
		 				.append("INNER JOIN V_PAIS_I PO ON PO.ID_PAIS = UFO.ID_PAIS \n")
		 				.append("INNER JOIN ENDERECO_PESSOA EPD ON EPD.ID_ENDERECO_PESSOA = PFD.ID_ENDERECO_PESSOA AND EPD.DT_VIGENCIA_INICIAL <= ? AND EPD.DT_VIGENCIA_FINAL >= ? \n")
		 				.append("INNER JOIN MUNICIPIO MD ON MD.ID_MUNICIPIO = EPD.ID_MUNICIPIO \n")
		 				.append("INNER JOIN UNIDADE_FEDERATIVA UFD ON UFD.ID_UNIDADE_FEDERATIVA = MD.ID_UNIDADE_FEDERATIVA ");
		sql.addFrom(sb.toString());
		
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		
		//Wheres
		sql.addCustomCriteria("FF.DT_VIGENCIA_INICIAL <= ? AND FF.DT_VIGENCIA_FINAL >= ?");
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());

		sql.addFilterSummary("servico",criteria.getString("servico.dsServico"));
		sql.addCriteria("S.ID_SERVICO","=",criteria.getLong("servico.idServico"));

		if (criteria.getLong("regionalO.idRegional") != null) {
			sql.addFilterSummary("regionalOrigem2",criteria.getString("regionalO.nmRegional"));
			sql.addCriteria("RO.ID_REGIONAL","=",criteria.getLong("regionalO.idRegional"));
		}

		if (criteria.getLong("filialO.idFilial") != null) {
			sql.addFilterSummary("filialOrigem",criteria.getString("filialO.sgFilial") + " - " + criteria.getString("filialO.pessoa.nmFantasia"));
			sql.addCriteria("FO.ID_FILIAL","=",criteria.getLong("filialO.idFilial"));
		}

		if (criteria.getLong("unidadeFederativaO.idUnidadeFederativa") != null) {
			sql.addFilterSummary("ufOrigem2",criteria.getString("unidadeFederativaO.sgUnidadeFederativa") + " - " + criteria.getString("unidadeFederativaO.nmUnidadeFederativa"));
			sql.addCriteria("UFO.ID_UNIDADE_FEDERATIVA","=",criteria.getLong("unidadeFederativaO.idUnidadeFederativa"));
		}
		
		if (criteria.getLong("regionalD.idRegional") != null) {
			sql.addFilterSummary("regionalDestino2",criteria.getString("regionalD.nmRegional"));
			sql.addCriteria("RD.ID_REGIONAL","=",criteria.getLong("regionalD.idRegional"));
		} 
		
		if (criteria.getLong("filialD.idFilial") != null) {
			sql.addFilterSummary("filialDestino",criteria.getString("filialD.sgFilial") + " - " + criteria.getString("filialD.pessoa.nmFantasia"));
			sql.addCriteria("FD.ID_FILIAL","=",criteria.getLong("filialD.idFilial"));
		} 

		if (criteria.getLong("unidadeFederativaD.idUnidadeFederativa") != null) {
			sql.addFilterSummary("ufDestino2",criteria.getString("unidadeFederativaD.sgUnidadeFederativa") + " - " + criteria.getString("unidadeFederativaD.nmUnidadeFederativa"));
			sql.addCriteria("UFD.ID_UNIDADE_FEDERATIVA","=",criteria.getLong("unidadeFederativaD.idUnidadeFederativa"));
		}
		
		if (criteria.getLong("filialR.idFilial") != null) {
			sql.addFilterSummary("filialReembarcadora",criteria.getString("filialR.sgFilial") + " - " + criteria.getString("filialR.pessoa.nmFantasia"));
			sql.addCriteria("FR.ID_FILIAL","=",criteria.getLong("filialR.idFilial"));
		}
 
		
		//Order
		sql.addOrderBy("NM_REGIONAL");
		sql.addOrderBy("NM_FILIAL_O");
		sql.addOrderBy("DS_SERVICO");
		sql.addOrderBy("NM_FILIAL_D");
		
		return sql;
	}
	
	/**
	 * LMS-2537
	 * Retorna o valor do calculo de pontos de passagens entre um fluxo de carga
	 *  
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idFFServiço
	 * @return
	 */
	public Integer countPostosPassagens(Long idFilialOrigem, Long idFilialDestino, Long idFFServiço) {
		return mcdService.countPostosPassagens(null, null, idFilialOrigem, idFilialDestino, null, idFFServiço);
	}
	public McdService getMcdService() {
		return mcdService;
	}
	public void setMcdService(McdService mcdService) {
		this.mcdService = mcdService;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}	
}
