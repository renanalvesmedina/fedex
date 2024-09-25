package com.mercurio.lms.municipios.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Felipe Ferreira
 * 
 * @spring.bean id="lms.municipios.EmitirMapaCodigosDistanciaService"
 * @spring.property name="reportName" value=
 *                  "com/mercurio/lms/municipios/report/emitirMapaCodigosDistancia.jasper"
 */

public class EmitirMapaCodigosDistanciaService extends ReportServiceSupport {

	private DomainService domainService;

	@SuppressWarnings("unchecked")
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;

        SqlTemplate sql = getSqlTemplate(tfm);

		configureFilterSummary(sql, tfm);

	    JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

        Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
        jr.setParameters(parametersReport);

        return jr;
	}

	private void configureFilterSummary(SqlTemplate sql, TypedFlatMap tfm) {
		String strAux = "";

		if (!tfm.getString("tpEmissao").equals("")) {
			Domain dmTipoEmissao = this.domainService.findByName("DM_TIPO_EMISSAO_MCD");
			DomainValue dvTipoEmissao = dmTipoEmissao.findDomainValueByValue(tfm.getString("tpEmissao"));
			sql.addFilterSummary("tipoEmissao", dvTipoEmissao.getDescription().getValue(LocaleContextHolder.getLocale()));
		}

		if (tfm.getLong("servico.idServico") != null)
			sql.addFilterSummary("servico", tfm.getString("servico.dsServico"));

		if (tfm.getLong("municipioFilialByIdMunicipioFilialOrigem.filial.idFilial") != null) {
			strAux = String.valueOf(tfm.getString("municipioFilialByIdMunicipioFilialOrigem.filial.sgFilial").concat(" - ").concat(tfm.getString("municipioFilialByIdMunicipioFilialOrigem.filial.pessoa.nmFantasia")));
			sql.addFilterSummary("filialResponsavelOrigem", strAux);
		}

		if (tfm.getLong("municipioFilialByIdMunicipioFilialOrigem.municipio.idMunicipio") != null)
			sql.addFilterSummary("municipioOrigem", tfm.getString("municipioFilialByIdMunicipioFilialOrigem.municipio.nmMunicipio"));

		if (tfm.getLong("municipioFilialByIdMunicipioFilialOrigem.municipio.idMunicipio") != null)
			sql.addFilterSummary("cdIbge", tfm.getString("municipioFilialByIdMunicipioFilialOrigem.municipio.cdIbge"));

		if (tfm.getLong("municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.idUnidadeFederativa") != null) {
			strAux = String.valueOf(tfm.getString("municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.sgUnidadeFederativa").concat(" - ").concat(tfm.getString("nmUnidadeFederativaOrigem")));
			sql.addFilterSummary("ufOrigem", strAux);
			sql.addFilterSummary("paisOrigem", tfm.getString("nmPaisOrigem"));
		}

		if (tfm.getLong("municipioFilialByIdMunicipioFilialDestino.filial.idFilial") != null) {
			strAux = String.valueOf(tfm.getString("municipioFilialByIdMunicipioFilialDestino.filial.sgFilial").concat(" - ").concat(tfm.getString("municipioFilialByIdMunicipioFilialDestino.filial.pessoa.nmFantasia")));
			sql.addFilterSummary("filialResponsavelDestino", strAux);
		}

		if (tfm.getLong("municipioFilialByIdMunicipioFilialDestino.municipio.idMunicipio") != null)
			sql.addFilterSummary("municipioDestino", tfm.getString("municipioFilialByIdMunicipioFilialDestino.municipio.nmMunicipio"));

		if (tfm.getLong("municipioFilialByIdMunicipioFilialDestino.municipio.idMunicipio") != null)
			sql.addFilterSummary("cdIbge", tfm.getString("municipioFilialByIdMunicipioFilialDestino.municipio.cdIbge"));

		if (tfm.getLong("municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.idUnidadeFederativa") != null) {
			strAux = String.valueOf(tfm.getString("municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.sgUnidadeFederativa").concat(" - ").concat(tfm.getString("nmUnidadeFederativaDestino")));
			sql.addFilterSummary("ufOrigem", strAux);
			sql.addFilterSummary("paisDestino", tfm.getString("nmPaisDestino"));
		}

		if (!tfm.getString("dtVigencia").equals(""))
			sql.addFilterSummary("mcdVigenteEm", tfm.getYearMonthDay("dtVigencia"));
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap values) {
		SqlTemplate sql = createSqlTemplate();

		sql.addProjection("MCD.ID_MCD", "ID_MCD");
		sql.addProjection("MCD.DT_VIGENCIA_INICIAL", "DT_VIGENCIA_INICIAL");
		sql.addProjection("S.ID_SERVICO", "ID_SERVICO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("S.DS_SERVICO_I"), "DS_SERVICO");
		sql.addProjection("MCD_MF.ID_MCD_MUNICIPIO_FILIAL", "ID_MCD_MUNICIPIO_FILIAL");
		sql.addProjection("MFO.ID_MUNICIPIO_FILIAL", "ID_MUNICIPIO_FILIAL_ORIGEM");
		sql.addProjection("MO.NM_MUNICIPIO", "NM_MUNICIPIO_ORIGEM");
		sql.addProjection("MO.CD_IBGE", "CD_IBGE_ORIGEM");
		sql.addProjection("UFO.NM_UNIDADE_FEDERATIVA", "NM_UNIDADE_FEDERATIVA_ORIGEM");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PO.NM_PAIS_I"), "NM_PAIS_ORIGEM");
		sql.addProjection("(FO.SG_FILIAL || ' - ' || PO.NM_FANTASIA)", "NM_FILIAL_ORIGEM");
		sql.addProjection("MCD_MF.BL_DOMINGO_ORIGEM", "BL_DOMINGO_ORIGEM");
		sql.addProjection("MCD_MF.BL_SEGUNDA_ORIGEM", "BL_SEGUNDA_ORIGEM");
		sql.addProjection("MCD_MF.BL_TERCA_ORIGEM", "BL_TERCA_ORIGEM");
		sql.addProjection("MCD_MF.BL_QUARTA_ORIGEM", "BL_QUARTA_ORIGEM");
		sql.addProjection("MCD_MF.BL_QUINTA_ORIGEM", "BL_QUINTA_ORIGEM");
		sql.addProjection("MCD_MF.BL_SEXTA_ORIGEM", "BL_SEXTA_ORIGEM");
		sql.addProjection("MCD_MF.BL_SABADO_ORIGEM", "BL_SABADO_ORIGEM");
		sql.addProjection("MCD_MF.ID_MCD_MUNICIPIO_FILIAL", "ID_MCD_MUNICIPIO_FILIAL");
		sql.addProjection("MFD.ID_MUNICIPIO_FILIAL", "ID_MUNICIPIO_FILIAL_DESTINO");
		sql.addProjection("MD.NM_MUNICIPIO", "NM_MUNICIPIO_DESTINO");
		sql.addProjection("MD.CD_IBGE", "CD_IBGE_DESTINO");
		sql.addProjection("UFD.NM_UNIDADE_FEDERATIVA", "NM_UNIDADE_FEDERATIVA_DESTINO");
		sql.addProjection("PD.NM_PAIS", "NM_PAIS_DESTINO");
		sql.addProjection("(FD.SG_FILIAL || ' - ' || PD.NM_FANTASIA)", "NM_FILIAL_DESTINO");
		sql.addProjection("MCD_MF.BL_DOMINGO_DESTINO", "BL_DOMINGO_DESTINO");
		sql.addProjection("MCD_MF.BL_SEGUNDA_DESTINO", "BL_SEGUNDA_DESTINO");
		sql.addProjection("MCD_MF.BL_TERCA_DESTINO", "BL_TERCA_DESTINO");
		sql.addProjection("MCD_MF.BL_QUARTA_DESTINO", "BL_QUARTA_DESTINO");
		sql.addProjection("MCD_MF.BL_QUINTA_DESTINO", "BL_QUINTA_DESTINO");
		sql.addProjection("MCD_MF.BL_SEXTA_DESTINO", "BL_SEXTA_DESTINO");
		sql.addProjection("MCD_MF.BL_SABADO_DESTINO", "BL_SABADO_DESTINO");
		sql.addProjection("TP.CD_TARIFA_PRECO", "CD_TARIFA_PRECO");
		sql.addProjection("MCD_MF.QT_PEDAGIO", "QT_PEDAGIO");
		if (values.getString("tpEmissao").equals("D"))
			sql.addProjection("(ceil(MCD_MF.NR_PPE / 24))", "NR_PPE");
		else
			sql.addProjection("MCD_MF.NR_PPE", "NR_PPE");
		sql.addProjection("(FFF.SG_FILIAL || CASE WHEN FFF.SG_FILIAL IS NOT NULL THEN  ' - ' END || FFP.NM_FANTASIA)", "NM_FILIAL_REEMBARCADORA");

		StringBuffer sqlFrom = new StringBuffer().append("MCD_MUNICIPIO_FILIAL MCD_MF \n").append("INNER JOIN MCD ON MCD_MF.ID_MCD = MCD.ID_MCD \n").append("INNER JOIN V_SERVICO_I S ON MCD_MF.ID_SERVICO = S.ID_SERVICO \n").append("INNER JOIN MUNICIPIO_FILIAL MFO ON MCD_MF.ID_MUNICIPIO_FILIAL_ORIGEM = MFO.ID_MUNICIPIO_FILIAL \n").append("INNER JOIN MUNICIPIO MO ON MFO.ID_MUNICIPIO = MO.ID_MUNICIPIO \n").append("INNER JOIN UNIDADE_FEDERATIVA UFO ON MO.ID_UNIDADE_FEDERATIVA = UFO.ID_UNIDADE_FEDERATIVA \n").append("INNER JOIN V_PAIS_I PO ON UFO.ID_PAIS = PO.ID_PAIS \n").append("INNER JOIN FILIAL FO ON MFO.ID_FILIAL = FO.ID_FILIAL \n").append("INNER JOIN PESSOA PO ON FO.ID_FILIAL = PO.ID_PESSOA \n").append("INNER JOIN MUNICIPIO_FILIAL MFD ON MCD_MF.ID_MUNICIPIO_FILIAL_DESTINO = MFD.ID_MUNICIPIO_FILIAL \n").append("INNER JOIN MUNICIPIO MD ON MFD.ID_MUNICIPIO = MD.ID_MUNICIPIO \n").append("INNER JOIN UNIDADE_FEDERATIVA UFD ON MD.ID_UNIDADE_FEDERATIVA = UFD.ID_UNIDADE_FEDERATIVA \n").append(
				"INNER JOIN V_PAIS_I PD ON UFD.ID_PAIS = PD.ID_PAIS \n").append("INNER JOIN FILIAL FD ON MFD.ID_FILIAL = FD.ID_FILIAL \n").append("INNER JOIN PESSOA PD ON FD.ID_FILIAL = PD.ID_PESSOA \n").append("INNER JOIN TARIFA_PRECO TP ON MCD_MF.ID_TARIFA_PRECO = TP.ID_TARIFA_PRECO \n").append("LEFT JOIN FLUXO_FILIAL FF ON MCD_MF.ID_FLUXO_FILIAL_REEMBARCADORA = FF.ID_FLUXO_FILIAL \n").append("LEFT JOIN FILIAL FFF ON FF.ID_FILIAL_REEMBARCADORA = FFF.ID_FILIAL \n").append("LEFT JOIN PESSOA FFP ON FFF.ID_FILIAL = FFP.ID_PESSOA \n");

		sql.addFrom(sqlFrom.toString());

		sql.addOrderBy("MCD.ID_MCD");
		sql.addOrderBy("S.ID_SERVICO");
		sql.addOrderBy("MO.NM_MUNICIPIO");
		sql.addOrderBy("MFO.ID_MUNICIPIO_FILIAL");
		sql.addOrderBy("MD.NM_MUNICIPIO");

		sql.addCriteria("MCD.DT_VIGENCIA_INICIAL", "<=", values.getString("dtVigencia"), YearMonthDay.class);
		sql.addCustomCriteria("MCD.DT_VIGENCIA_FINAL >= ?", JTDateTimeUtils.maxYmd(values.getString("dtVigencia")), YearMonthDay.class, true);

		sql.addCriteria("S.ID_SERVICO", "=", values.getLong("servico.idServico"));

		sql.addCriteria("FO.ID_FILIAL", "=", values.getLong("municipioFilialByIdMunicipioFilialOrigem.filial.idFilial"));
		sql.addCriteria("MO.ID_MUNICIPIO", "=", values.getLong("municipioFilialByIdMunicipioFilialOrigem.municipio.idMunicipio"));
		sql.addCriteria("UFO.ID_UNIDADE_FEDERATIVA", "=", values.getLong("municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.idUnidadeFederativa"));

		sql.addCriteria("FD.ID_FILIAL", "=", values.getLong("municipioFilialByIdMunicipioFilialDestino.filial.idFilial"));
		sql.addCriteria("MD.ID_MUNICIPIO", "=", values.getLong("municipioFilialByIdMunicipioFilialDestino.municipio.idMunicipio"));
		sql.addCriteria("UFD.ID_UNIDADE_FEDERATIVA", "=", values.getLong("municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.idUnidadeFederativa"));

		return sql;
	}

	public DomainService getDomainService() {
		return domainService;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

}