package com.mercurio.lms.fretecarreteirocoletaentrega.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaReajuste;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ParcelaReajusteService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TipoTabelaColetaEntregaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author 
 *
 * @spring.bean id="lms.fretecarreteirocoletaentrega.emitirTabelasFretesSimuladasService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteirocoletaentrega/report/emitirTabelasFreteSimuladas.jasper"
 */
public class EmitirTabelasFretesSimuladasService extends ReportServiceSupport {
	
	
	private ParcelaReajusteService parcelaReajusteService;
	private TipoMeioTransporteService tipoMeioTransporteService;
	private TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService;
	
	
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap parameters = (TypedFlatMap)criteria;
		
        SqlTemplate sql = montaSqlTemplate(parameters);                        
        
        List list = (List)getJdbcTemplate().query(sql.getSql(),JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()),new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				ParcelaReajuste parcelaDH = findParcelaByTpParcela("DH");
				ParcelaReajuste parcelaQU = findParcelaByTpParcela("QU");
				ParcelaReajuste parcelaEV = findParcelaByTpParcela("EV");
				ParcelaReajuste parcelaFP = findParcelaByTpParcela("FP");
				
				List resultList = new ArrayList();
				while (rs.next()) {
					Map result = new TypedFlatMap();
					result.put("NM_FILIAL",rs.getString("NM_FILIAL"));
					result.put("DS_TIPO_MEIO_TRANSPORTE",rs.getString("DS_TIPO_MEIO_TRANSPORTE"));
					result.put("VL_DEFINIDO_DH",calculateNewValue(parcelaDH,rs.getBigDecimal("VL_DEFINIDO_DH")));
					result.put("VL_DEFINIDO_QU",calculateNewValue(parcelaQU,rs.getBigDecimal("VL_DEFINIDO_QU")));
					result.put("VL_DEFINIDO_EV",calculateNewValue(parcelaEV,rs.getBigDecimal("VL_DEFINIDO_EV")));
					result.put("VL_DEFINIDO_FP",calculateNewValue(parcelaFP,rs.getBigDecimal("VL_DEFINIDO_FP")));
					result.put("DT_VIGENCIA_INICIAL",rs.getDate("DT_VIGENCIA_INICIAL"));
					result.put("DT_VIGENCIA_FINAL",rs.getDate("DT_VIGENCIA_FINAL"));
					result.put("DS_SIMBOLO",rs.getString("DS_SIMBOLO"));
					
					resultList.add(result);
				}
				return resultList;
			}});
        
        Map parametersReport = new HashMap();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        if (list.size() > 0)
        	parametersReport.put("DS_SIMBOLO",((TypedFlatMap)list.get(0)).getString("DS_SIMBOLO"));
        JRMapCollectionDataSource jrDataSource = new JRMapCollectionDataSource(list);
        return createReportDataObject(jrDataSource,parametersReport);
	}
	
	private String calculateNewValue(ParcelaReajuste parcela, BigDecimal nowValue) {
		BigDecimal result;
		if (parcela == null && nowValue == null)
			return null;
		if (parcela == null)
			result = nowValue;
		else{
			if (parcela.getVlBruto() != null) {
				result = parcela.getVlBruto().add(nowValue);
			}else{
				if (parcela.getVlReajustado() != null)
					result = nowValue.multiply(parcela.getVlReajustado()).divide(new BigDecimal(100),2,BigDecimal.ROUND_UP).add(nowValue);
				else
					result = nowValue;
			}
		}
		return FormatUtils.formatDecimal("###,###,##0.00",result);
	}
	
	private ParcelaReajuste findParcelaByTpParcela(String tpParcela) {
		Map criteria = new HashMap();
		criteria.put("tpParcela",tpParcela);
		List rs = parcelaReajusteService.find(criteria);
		if (rs.isEmpty())
			return null;
		return (ParcelaReajuste)rs.get(0);
	}

	private SqlTemplate montaSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("(SELECT PTC.VL_DEFINIDO FROM PARCELA_TABELA_CE PTC WHERE PTC.ID_TABELA_COLETA_ENTREGA = TCE.ID_TABELA_COLETA_ENTREGA AND PTC.TP_PARCELA = 'DH')","VL_DEFINIDO_DH"); 
		sql.addProjection("(SELECT PTC.VL_DEFINIDO FROM PARCELA_TABELA_CE PTC WHERE PTC.ID_TABELA_COLETA_ENTREGA = TCE.ID_TABELA_COLETA_ENTREGA AND PTC.TP_PARCELA = 'EV')","VL_DEFINIDO_EV");
		sql.addProjection("(SELECT PTC.VL_DEFINIDO FROM PARCELA_TABELA_CE PTC WHERE PTC.ID_TABELA_COLETA_ENTREGA = TCE.ID_TABELA_COLETA_ENTREGA AND PTC.TP_PARCELA = 'FP')","VL_DEFINIDO_FP");
		sql.addProjection("(SELECT PTC.VL_DEFINIDO FROM PARCELA_TABELA_CE PTC WHERE PTC.ID_TABELA_COLETA_ENTREGA = TCE.ID_TABELA_COLETA_ENTREGA AND PTC.TP_PARCELA = 'QU')","VL_DEFINIDO_QU");
		sql.addProjection("F.SG_FILIAL || ' - ' || P.NM_FANTASIA","NM_FILIAL");
		sql.addProjection("M.DS_SIMBOLO");
		sql.addProjection("TCE.DT_VIGENCIA_INICIAL");
		sql.addProjection("TCE.DT_VIGENCIA_FINAL");
		sql.addProjection("TMT.DS_TIPO_MEIO_TRANSPORTE","DS_TIPO_MEIO_TRANSPORTE");
		
		sql.addFrom(new StringBuffer("TABELA_COLETA_ENTREGA TCE ")
		   		.append("INNER JOIN FILIAL F ON F.ID_FILIAL = TCE.ID_FILIAL ")
				.append("INNER JOIN PESSOA P ON P.ID_PESSOA = TCE.ID_FILIAL ")
				.append("INNER JOIN MOEDA_PAIS MP ON MP.ID_MOEDA_PAIS = TCE.ID_MOEDA_PAIS ")
				.append("INNER JOIN MOEDA M ON M.ID_MOEDA = MP.ID_MOEDA ")
				.append("INNER JOIN TIPO_TABELA_COLETA_ENTREGA TTCE ON TTCE.ID_TIPO_TABELA_COLETA_ENTREGA = TCE.ID_TIPO_TABELA_COLETA_ENTREGA ")
				.append("INNER JOIN TIPO_MEIO_TRANSPORTE TMT ON TMT.ID_TIPO_MEIO_TRANSPORTE = TCE.ID_TIPO_MEIO_TRANSPORTE ").toString());

		sql.addOrderBy("F.SG_FILIAL");
		sql.addOrderBy("TMT.DS_TIPO_MEIO_TRANSPORTE");
		
		
		sql.addCriteria("TTCE.ID_TIPO_TABELA_COLETA_ENTREGA","=",parameters.getLong("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"));
		sql.addCriteria("TMT.ID_TIPO_MEIO_TRANSPORTE","=",parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
		sql.addCriteria("TCE.ID_MOEDA_PAIS","=",parameters.getLong("moedaPais.idMoedaPais"));
		
		sql.addCriteria("TCE.TP_REGISTRO","=","A"); 
		
		TipoTabelaColetaEntrega tipoTabelaColetaEntrega = tipoTabelaColetaEntregaService.findById(parameters.getLong("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"));
		TipoMeioTransporte tipoMeioTransporte = tipoMeioTransporteService.findById(parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
		
		sql.addFilterSummary("tipoTabela",tipoTabelaColetaEntrega.getDsTipoTabelaColetaEntrega());
		sql.addFilterSummary("tipoMeioTransporte",tipoMeioTransporte.getDsTipoMeioTransporte());
		sql.addFilterSummary("pais",parameters.getString("moedaPais.pais.nmPais"));
		sql.addFilterSummary("moeda",parameters.getString("descricaoMoeda"));

		YearMonthDay dtVigenciaInicial = parameters.getYearMonthDay("dtEmissaoInicial");
		YearMonthDay dtVigenciaFinal   = parameters.getYearMonthDay("dtEmissaoFinal");
		
		sql.addFilterSummary("vigenciaInicial",dtVigenciaInicial);
		sql.addFilterSummary("vigenciaFinal",dtVigenciaFinal);
		
		sql.addCustomCriteria(new StringBuffer("TCE.DT_VIGENCIA_INICIAL = (").append("SELECT MAX(TCE2.DT_VIGENCIA_INICIAL) FROM TABELA_COLETA_ENTREGA TCE2 WHERE ")
				.append("TCE2.ID_TIPO_TABELA_COLETA_ENTREGA = ? AND ")
				.append("TCE2.ID_TIPO_MEIO_TRANSPORTE = ? AND ")
				.append("TCE2.ID_MOEDA_PAIS = ? AND ")
				.append("TCE2.TP_REGISTRO = ? AND ")
				.append("TCE2.DT_VIGENCIA_INICIAL < ? AND ")
				.append("TCE2.ID_FILIAL = F.ID_FILIAL)").toString());
		sql.addCriteriaValue(parameters.getLong("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"));
		sql.addCriteriaValue(parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
		sql.addCriteriaValue(parameters.getLong("moedaPais.idMoedaPais"));
		sql.addCriteriaValue("A");
		sql.addCriteriaValue(dtVigenciaInicial);
			
			
		List filiaisT = parameters.getList("filiaisT");
		List filiaisF = parameters.getList("filiaisF");
		
		if (filiaisT != null && filiaisT.size() > 0) {
			
			TypedFlatMap filial = (TypedFlatMap)filiaisT.get(0);
			StringBuffer criterias = new StringBuffer("?");
			sql.addCriteriaValue(filial.getLong("filial.idFilial"));

			for(int i = 1; i < filiaisT.size(); i++) {
				filial = (TypedFlatMap)filiaisT.get(i);
				criterias.append(",?");
				sql.addCriteriaValue(filial.getLong("filial.idFilial"));
			}
			sql.addCustomCriteria(new StringBuffer("F.ID_FILIAL IN (").append(criterias).append(")").toString());
		}
		
		if (filiaisF != null && filiaisF.size() > 0) {
			TypedFlatMap filial = (TypedFlatMap)filiaisF.get(0);
			StringBuffer criterias = new StringBuffer("?");
			sql.addCriteriaValue(filial.getLong("filial.idFilial"));

			for(int i = 1; i < filiaisF.size(); i++) {
				filial = (TypedFlatMap)filiaisF.get(i);
				criterias.append(",?");
				sql.addCriteriaValue(filial.getLong("filial.idFilial"));
			}
			sql.addCustomCriteria(new StringBuffer("F.ID_FILIAL NOT IN (").append(criterias).append(")").toString());
		}
		
		return sql;
	}

	public void setParcelaReajusteService(
			ParcelaReajusteService parcelaReajusteService) {
		this.parcelaReajusteService = parcelaReajusteService;
	}

	public void setTipoMeioTransporteService(
			TipoMeioTransporteService tipoMeioTransporteService) {
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}

	public void setTipoTabelaColetaEntregaService(
			TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService) {
		this.tipoTabelaColetaEntregaService = tipoTabelaColetaEntregaService;
	}
}
