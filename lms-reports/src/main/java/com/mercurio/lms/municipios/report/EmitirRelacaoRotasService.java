package com.mercurio.lms.municipios.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.service.FilialRotaService;
import com.mercurio.lms.municipios.model.service.PostoPassagemTrechoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 *
 * @spring.bean id="lms.municipios.emitirRelacaoRotasService"
 * @spring.property name="reportName" value="com/mercurio/lms/municipios/report/emitirRelacaoRotas.jasper"
 */
public class EmitirRelacaoRotasService extends ReportServiceSupport {
	private PostoPassagemTrechoService postoPassagemTrechoService;
	private FilialRotaService filialRotaService;

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;

		SqlTemplate sql = getSqlTemplate(tfm);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
 
		setSqlFilterSummary(sql,tfm);

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());

		parametersReport.put("ID_MOEDA",tfm.getLong("moedaPais.moeda.idMoeda"));
		parametersReport.put("ID_PAIS",tfm.getLong("moedaPais.pais.idPais"));
		parametersReport.put("ID_MOEDA_PAIS",tfm.getLong("moedaPais.idMoedaPais"));
		parametersReport.put("SIMBOLO_MOEDA",tfm.getString("moedaPais.moeda.dsSimbolo"));

		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));

		jr.setParameters(parametersReport);
		return jr;
	}

	private void setSqlFilterSummary(SqlTemplate sql, TypedFlatMap tfm) {
		sql.addFilterSummary("tipoRota",tfm.getString("tpRotaDescription"));

		Long idFilial = tfm.getLong("filialOrigem.idFilial");
		if (idFilial != null) {
			sql.addFilterSummary("filialOrigem",
					tfm.getString("filialOrigem.sgFilial") + " - " + tfm.getString("filialOrigem.nmFantasia"));
		}
		idFilial = tfm.getLong("filialDestino.idFilial");
		if (idFilial != null) {
			sql.addFilterSummary("filialDestino",
					tfm.getString("filialDestino.sgFilial") + " - " + tfm.getString("filialDestino.nmFantasia"));
		}
		idFilial = null;
		idFilial = tfm.getLong("filialIntegrante.idFilial");
		if (idFilial != null) {
			sql.addFilterSummary("filialIntegranteRota",
					tfm.getString("filialIntegrante.sgFilial") + " - " + tfm.getString("filialIntegrante.pessoa.nmFantasia"));
		}

		if(!tfm.getString("vigentes").equals(""))
			sql.addFilterSummary("vigentes", getDomainValueService().findDomainValueDescription("DM_SIM_NAO",tfm.getString("vigentes")));

		Long idRota = tfm.getLong("rotaIdaVolta.idRotaIdaVolta");
		if (idRota != null) {
			sql.addFilterSummary("rotaViagem",FormatUtils.fillNumberWithZero(tfm.getString("rotaIdaVolta.nrRota"),4)
					.concat(" - ")
					.concat(tfm.getString("rotaViagem.dsRota")));
		}

		sql.addFilterSummary("converterParaMoeda",tfm.getString("moedaPais.moeda.siglaSimbolo"));
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap map) {
		SqlTemplate sql = createSqlTemplate();

		sql.addProjection(new StringBuilder()
				.append("F_CONV_MOEDA(")
				.append("PA.ID_PAIS,M.ID_MOEDA,?,?,?")
				.append(",RIV.VL_FRETE_KM)")
				.toString(),"VALOR_KM");

		sql.addCriteriaValue(map.getLong("moedaPais.pais.idPais"));
		sql.addCriteriaValue(map.getLong("moedaPais.moeda.idMoeda"));
		sql.addCriteriaValue(JTDateTimeUtils.getDataHoraAtual());

		sql.addProjection(new StringBuilder()
		.append("F_CONV_MOEDA(")
		.append("PA.ID_PAIS,M.ID_MOEDA,?,?,?")
		.append(",(RIV.NR_DISTANCIA * RIV.VL_FRETE_KM))")
		.toString(),"VALOR_FRETE");

		sql.addCriteriaValue(map.getLong("moedaPais.pais.idPais"));
		sql.addCriteriaValue(map.getLong("moedaPais.moeda.idMoeda"));
		sql.addCriteriaValue(JTDateTimeUtils.getDataHoraAtual());

		sql.addProjection("RIV.NR_ROTA");
		sql.addProjection("R.DS_ROTA");
		sql.addProjection("RV.TP_ROTA");
		sql.addProjection("RIV.TP_ROTA_IDA_VOLTA");
		sql.addProjection("RIV.ID_ROTA");

		StringBuffer projHrSAIDA = new StringBuffer()
		.append("SELECT Max(TRIV.HR_SAIDA) FROM ")
		.append("TRECHO_ROTA_IDA_VOLTA TRIV, ")
		.append("FILIAL_ROTA FRO ")
		.append("WHERE TRIV.ID_ROTA_IDA_VOLTA = RIV.ID_ROTA_IDA_VOLTA ")
		.append("  AND FRO.ID_FILIAL_ROTA = TRIV.ID_FILIAL_ROTA_ORIGEM ")
		.append("  AND FRO.BL_ORIGEM_ROTA = ?");

		sql.addProjection("(" + projHrSAIDA.toString() + ")","HR_SAIDA");
		sql.addCriteriaValue("S");

		StringBuffer projNrTempoViagem = new StringBuffer()
		.append("SELECT Max(TRIV.NR_TEMPO_VIAGEM) FROM ")
		.append("TRECHO_ROTA_IDA_VOLTA TRIV, ")
		.append("FILIAL_ROTA FRO, ")
		.append("FILIAL_ROTA FRD ")
		.append("WHERE TRIV.ID_ROTA_IDA_VOLTA = RIV.ID_ROTA_IDA_VOLTA ")
		.append("  AND FRO.ID_FILIAL_ROTA = TRIV.ID_FILIAL_ROTA_ORIGEM ")
		.append("  AND FRO.BL_ORIGEM_ROTA = ? ")
		.append("  AND FRD.ID_FILIAL_ROTA = TRIV.ID_FILIAL_ROTA_DESTINO ")
		.append("  AND FRD.BL_DESTINO_ROTA = ? ");

		sql.addProjection("(" + projNrTempoViagem.toString() + ")","NR_TEMPO_VIAGEM");
		sql.addCriteriaValue("S");
		sql.addCriteriaValue("S");

		sql.addProjection("RIV.NR_DISTANCIA");
		sql.addProjection("RIV.VL_FRETE_KM");
		sql.addProjection("(RIV.NR_DISTANCIA * RIV.VL_FRETE_KM)","VL_FRETE");

		sql.addProjection("TMT.ID_TIPO_MEIO_TRANSPORTE");
		sql.addProjection("TMT.DS_TIPO_MEIO_TRANSPORTE");
		sql.addProjection("TMT.ID_TIPO_MEIO_TRANSPORTE_COMPOS");

		StringBuffer projQtEixos = new StringBuffer()
		.append("SELECT Max(EIXOS.QT_EIXOS) FROM EIXOS_TIPO_MEIO_TRANSPORTE EIXOS ")
		.append("WHERE EIXOS.ID_TIPO_MEIO_TRANSPORTE = TMT.ID_TIPO_MEIO_TRANSPORTE");

		sql.addProjection("(" + projQtEixos.toString() + ")","QT_EIXOS");

		StringBuffer projQtEixosCompos = new StringBuffer()
		.append("SELECT Max(EIXOS.QT_EIXOS) FROM EIXOS_TIPO_MEIO_TRANSPORTE EIXOS ")
		.append("WHERE EIXOS.ID_TIPO_MEIO_TRANSPORTE = TMT.ID_TIPO_MEIO_TRANSPORTE_COMPOS");

		sql.addProjection("(" + projQtEixosCompos.toString() + ")","QT_EIXOS_COMPOS");

		sql.addProjection("RV.DT_VIGENCIA_INICIAL");
		sql.addProjection("RV.DT_VIGENCIA_FINAL");
		sql.addProjection("P.NM_PESSOA","NM_CLIENTE");
		sql.addProjection("M.ID_MOEDA");
		sql.addProjection("PA.ID_PAIS");

		sql.addFrom("ROTA_IDA_VOLTA","RIV");
		sql.addFrom("ROTA","R");
		sql.addFrom("ROTA_VIAGEM","RV");
		sql.addFrom("TIPO_MEIO_TRANSPORTE","TMT");
		sql.addFrom("CLIENTE","C");
		sql.addFrom("PESSOA","P");
		sql.addFrom("MOEDA_PAIS","MP");
		sql.addFrom("MOEDA","M");
		sql.addFrom("PAIS","PA");

		sql.addJoin("R.ID_ROTA","RIV.ID_ROTA");
		sql.addJoin("RV.ID_ROTA_VIAGEM","RIV.ID_ROTA_VIAGEM");
		sql.addJoin("TMT.ID_TIPO_MEIO_TRANSPORTE (+)","RV.ID_TIPO_MEIO_TRANSPORTE");
		sql.addJoin("C.ID_CLIENTE (+)","RV.ID_CLIENTE");
		sql.addJoin("P.ID_PESSOA (+)","C.ID_CLIENTE");
		sql.addJoin("MP.ID_MOEDA_PAIS","RIV.ID_MOEDA_PAIS");
		sql.addJoin("M.ID_MOEDA","MP.ID_MOEDA");
		sql.addJoin("PA.ID_PAIS","MP.ID_PAIS");

		sql.addCriteria("RIV.ID_ROTA_IDA_VOLTA","=",map.getLong("rotaIdaVolta.idRotaIdaVolta"));
		sql.addCriteria("RV.TP_ROTA","=",map.getString("rotaViagem.tpRota"));

		Long idFilial = null;
		StringBuffer sqlExists = null;
		idFilial = map.getLong("filialIntegrante.idFilial");
		if (idFilial != null) {
			sqlExists = new StringBuffer()
			.append("EXISTS (SELECT * FROM FILIAL_ROTA FR ")
			.append(" WHERE FR.ID_FILIAL = ? ")
			.append("	AND FR.ID_ROTA = R.ID_ROTA) ");
			sql.addCustomCriteria(sqlExists.toString());
			sql.addCriteriaValue(idFilial);
		}

		idFilial = map.getLong("filialOrigem.idFilial");
		if (idFilial != null) {
			sqlExists = new StringBuffer()
					.append("EXISTS (SELECT * FROM FILIAL_ROTA FR ")
					.append(" WHERE FR.ID_FILIAL = ? ")
					.append("	AND FR.ID_ROTA = R.ID_ROTA ")
					.append("   AND FR.BL_ORIGEM_ROTA = ?) ");
			sql.addCustomCriteria(sqlExists.toString());
			sql.addCriteriaValue(idFilial);
			sql.addCriteriaValue("S");
		}

		idFilial = map.getLong("filialDestino.idFilial");
		if (idFilial != null) {
			sqlExists = new StringBuffer()
			.append("EXISTS (SELECT * FROM FILIAL_ROTA FR ")
			.append(" WHERE FR.ID_FILIAL = ? ")
			.append("	AND FR.ID_ROTA = R.ID_ROTA ")
			.append("   AND FR.BL_DESTINO_ROTA = ?) ");
			sql.addCustomCriteria(sqlExists.toString());
			sql.addCriteriaValue(idFilial);
			sql.addCriteriaValue("S");
		}

		String isVigentes = map.getString("vigentes");
		if (isVigentes.equals("S")){
			sql.addCriteria("RV.DT_VIGENCIA_INICIAL","<=",JTDateTimeUtils.getDataAtual());
			sql.addCriteria("RV.DT_VIGENCIA_FINAL",">=",JTDateTimeUtils.getDataAtual());
		} else if (isVigentes.equals("N")){
			sql.addCustomCriteria("(RV.DT_VIGENCIA_INICIAL > ? OR RV.DT_VIGENCIA_FINAL < ?)",
					new Object[]{JTDateTimeUtils.getDataAtual(),JTDateTimeUtils.getDataAtual()});
		}

		sql.addOrderBy("R.DS_ROTA");
		sql.addOrderBy("RIV.NR_ROTA");
		sql.addOrderBy("RV.DT_VIGENCIA_INICIAL");
		
		return sql;
	}

	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_ROTA", "DM_TIPO_ROTA_VIAGEM"); 
		config.configDomainField("TP_ROTA_IDA_VOLTA", "DM_TIPO_ROTA_IDA_VOLTA");
	} 

	public String findValorPedagio(Long idRota, Long idTipoMeioTransporte, Integer qtEixos,
			Long idTipoMeioTransporteComposto, Integer qtEixosComposto, Long idMoedaPais) {
		List filiaisRota = filialRotaService.findFiliaisRotaByRotaOrRotaIdaVolta(idRota,null);
		List result = new ArrayList();
		for (Iterator iter = filiaisRota.iterator(); iter.hasNext();) {
			FilialRota fr = (FilialRota)iter.next();
			Filial filial = new Filial();
			filial.setIdFilial(fr.getFilial().getIdFilial());
			result.add(filial);
		}

		BigDecimal valor = postoPassagemTrechoService.findValorPostosPassagemRota(result,
					idTipoMeioTransporte, qtEixos,
					idTipoMeioTransporteComposto, qtEixosComposto,
					JTDateTimeUtils.getDataAtual(), idMoedaPais);

		if (valor != null)
			return FormatUtils.formatDecimal("#,##0.00",valor);
		else
			return "";
	}

	public void setPostoPassagemTrechoService(PostoPassagemTrechoService postoPassagemTrechoService) {
		this.postoPassagemTrechoService = postoPassagemTrechoService;
	}
	public void setFilialRotaService(FilialRotaService filialRotaService) {
		this.filialRotaService = filialRotaService;
	}

}
