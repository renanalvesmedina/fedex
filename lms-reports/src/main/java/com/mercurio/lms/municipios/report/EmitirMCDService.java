package com.mercurio.lms.municipios.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Mcd;
import com.mercurio.lms.municipios.model.dao.ConsultarMCDDAO;
import com.mercurio.lms.municipios.model.param.MCDParam;
import com.mercurio.lms.municipios.model.service.McdService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andresa Vargas
 * 
 * @spring.bean id="lms.municipios.EmitirMCDService"
 * @spring.property name="reportName" value=
 *                  "com/mercurio/lms/municipios/report/emitirMapaCodigosDistancia.jasper"
 */

public class EmitirMCDService extends ReportServiceSupport {

	private DomainService domainService;
	private McdService mcdService;
	private RegionalService regionalService;

	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}

	@SuppressWarnings("unchecked")
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;

		Mcd mcd = mcdService.findMcdByVigente(tfm.getYearMonthDay("dtVigencia"));
		if (mcd == null) {
			throw new BusinessException("LMS-29170");
		} else {
			SqlTemplate sql = getSqlTemplate(tfm, tfm.getYearMonthDay("dtVigencia"));

			configureFilterSummary(sql, tfm);

			JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

			Map parametersReport = new HashMap();
			parametersReport.put("DT_MCD", JTFormatUtils.format(tfm.getYearMonthDay("dtVigencia")));
			parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
			parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
			parametersReport.put("titulo", "Relatório de MCD - Mapa de Códigos de Distância");
			parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
			jr.setParameters(parametersReport);

			return jr;
		}
	}

	private void configureFilterSummary(SqlTemplate sql, TypedFlatMap tfm) {
		String strAux = "";
		if (!tfm.getString("tpEmissao").equals("")) {
			Domain dmTipoEmissao = this.domainService.findByName("DM_TIPO_EMISSAO_MCD");
			DomainValue dvTipoEmissao = dmTipoEmissao.findDomainValueByValue(tfm.getString("tpEmissao"));
			sql.addFilterSummary("tipoEmissao", dvTipoEmissao.getDescription().getValue(LocaleContextHolder.getLocale()));
		}
		if (tfm.getLong("servico.idServico") != null) {
			sql.addFilterSummary("servico", tfm.getString("servico.dsServico"));
		}
		if (tfm.getLong("municipioFilialByIdMunicipioFilialOrigem.filial.idFilial") != null) {
			strAux = String.valueOf(tfm.getString("municipioFilialByIdMunicipioFilialOrigem.filial.sgFilial").concat(" - ").concat(tfm.getString("municipioFilialByIdMunicipioFilialOrigem.filial.pessoa.nmFantasia")));
			sql.addFilterSummary("filialResponsavelOrigem", strAux);
		}
		if (tfm.getLong("municipioFilialByIdMunicipioFilialOrigem.municipio.idMunicipio") != null) {
			sql.addFilterSummary("municipioOrigem", tfm.getString("municipioFilialByIdMunicipioFilialOrigem.municipio.nmMunicipio"));
		}
		if (tfm.getLong("municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.idUnidadeFederativa") != null) {
			strAux = String.valueOf(tfm.getString("municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.sgUnidadeFederativa").concat(" - ").concat(tfm.getString("nmUnidadeFederativaOrigem")));
			sql.addFilterSummary("ufOrigem", strAux);
			sql.addFilterSummary("paisOrigem", tfm.getString("nmPaisOrigem"));
		}
		if (tfm.getLong("municipioFilialByIdMunicipioFilialDestino.filial.idFilial") != null) {
			strAux = String.valueOf(tfm.getString("municipioFilialByIdMunicipioFilialDestino.filial.sgFilial").concat(" - ").concat(tfm.getString("municipioFilialByIdMunicipioFilialDestino.filial.pessoa.nmFantasia")));
			sql.addFilterSummary("filialResponsavelDestino", strAux);
		}
		if (tfm.getLong("municipioFilialByIdMunicipioFilialDestino.municipio.idMunicipio") != null) {
			sql.addFilterSummary("municipioDestino", tfm.getString("municipioFilialByIdMunicipioFilialDestino.municipio.nmMunicipio"));
		}
		if (tfm.getLong("municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.idUnidadeFederativa") != null) {
			strAux = String.valueOf(tfm.getString("municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.sgUnidadeFederativa").concat(" - ").concat(tfm.getString("nmUnidadeFederativaDestino")));
			sql.addFilterSummary("ufDestino", strAux);
			sql.addFilterSummary("paisDestino", tfm.getString("nmPaisDestino"));
		}
		if (!tfm.getString("dtVigencia").equals("")) {
			sql.addFilterSummary("mcdVigenteEm", tfm.getYearMonthDay("dtVigencia"));
		}
		if (!tfm.getString("idRegionalOrigem").equals("")) {
			sql.addFilterSummary("regionalOrigem", regionalService.findById(tfm.getLong("idRegionalOrigem")).getSiglaDescricao());
	}
		if (!tfm.getString("idRegionalDestino").equals("")) {
			sql.addFilterSummary("regionalDestino", regionalService.findById(tfm.getLong("idRegionalDestino")).getSiglaDescricao());
		}
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap values, YearMonthDay dtVigenciaInicialMcd) {
		SqlTemplate sql = createSqlTemplate();

		MCDParam dados = new MCDParam();
		dados.setIdMunicipioOrigem(values.getLong("municipioFilialByIdMunicipioFilialOrigem.municipio.idMunicipio"));
		dados.setIdFilialOrigem(values.getLong("municipioFilialByIdMunicipioFilialOrigem.filial.idFilial"));
		dados.setIdUnidadeFederativaOrigem(values.getLong("municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.idUnidadeFederativa"));

		dados.setIdFilialDestino(values.getLong("municipioFilialByIdMunicipioFilialDestino.filial.idFilial"));
		dados.setIdMunicipioDestino(values.getLong("municipioFilialByIdMunicipioFilialDestino.municipio.idMunicipio"));
		dados.setIdUnidadeFederativaDestino(values.getLong("municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.idUnidadeFederativa"));

		dados.setIdServico(values.getLong("servico.idServico"));
		dados.setTpEmissao(values.getString("tpEmissao"));
		dados.setDtVigencia(dtVigenciaInicialMcd);

		dados.setIdRegionalDestino(values.getLong("idRegionalDestino"));
		dados.setIdRegionalOrigem(values.getLong("idRegionalOrigem"));

		return ConsultarMCDDAO.getSqlTemplate(sql, dados);
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public void setMcd(McdService mcd) {
		this.mcdService = mcd;
	}

}
