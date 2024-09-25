package com.mercurio.lms.franqueados.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.LancamentoFranqueado;
import com.mercurio.lms.franqueados.model.ReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.service.calculo.OutputFranqueadoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioContabilService"
 * @spring.property name="reportName" value=
 *                  "com/mercurio/lms/franqueados/report/emitirRelatorioFinanceiro.jasper"
 */
public class RelatorioFinanceiroService2 extends ReportServiceSupport {

	private FilialService filialService;
	
	/**
	 * Método responsável por gerar o relatório.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		YearMonthDay dtCompetencia = null;
		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			dtCompetencia = (YearMonthDay) parameters.get("competencia");
		}

		List dados = convertDataToDadosReport((Map) parameters.get("data"), dtCompetencia);
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", getFilterSummary(parameters));
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		parametersReport.put("competencia", dtCompetencia);
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null)
			parametersReport.put("idFranquia", parameters.get("idFilial"));

		JRReportDataObject jr = createReportDataObject(dados, parametersReport);
		return jr;
	}

	public List<Map<String,Object>> convertDataToDadosReport(Map<Long, Queue> listFranquias, YearMonthDay dtCompetencia) {
		List<Map<String,Object>> _result = new ArrayList<Map<String,Object>>();
		for (Long idFranquia : listFranquias.keySet()) {
			Queue list = listFranquias.get(idFranquia);

			Queue<DoctoServicoFranqueado> doctos = new LinkedList<DoctoServicoFranqueado>(); 
			Queue<DoctoServicoFranqueado> servicoAdicional = new LinkedList<DoctoServicoFranqueado>();
			Queue<ReembarqueDoctoServicoFranqueado> reembarque = new LinkedList<ReembarqueDoctoServicoFranqueado>();
			Queue<DoctoServicoFranqueado> recalculo = new LinkedList<DoctoServicoFranqueado>(); 
			Queue<LancamentoFranqueado> BDM = new LinkedList<LancamentoFranqueado>();
			
			buildDados(list, doctos, servicoAdicional, reembarque, recalculo, BDM);
			
			String sgFilial = filialService.findById(idFranquia).getSgFilial();
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map = getVlTotalDoc(doctos);
			map.put("SG_FILIAL", sgFilial);
			map.put("VL_TOTAL_DOC", map.get("VL_TOTAL_DOC_BASE"));
			map.put("VL_SERVICO_ADICIONAL", getVlServicoAdicional(servicoAdicional));
			map.put("VL_SERV_REEMB", getVlReembarque(reembarque));
			map.put("VL_RECALCULO", getVlRecalculo(recalculo));
			map.put("VL_BDM", getVlBDM(BDM));
			map.put("VL_IRE", getVlIre(idFranquia, dtCompetencia));
			
			map.put("VL_CREDITO_DIVERSO", getVlCreditoDiverso(idFranquia, dtCompetencia));
			map.put("VL_DEBITO_DIVERSO", getVlDebitoDiverso(idFranquia, dtCompetencia));
			map.put("VL_OVER_60", getVlOver60(idFranquia, dtCompetencia));
			map.put("VL_INDENIZACAO", getVlIndeninacao(idFranquia, dtCompetencia));

			map.put("VL_PARTICIPACAO", getVlParticipacao(map));
			map.put("VL_TOTAL_DEBITO_DIVERSO", getVlTotalDebitoDiverso(map));
			map.put("VL_A_PAGAR", getVlTotalAPagar(map));
			map.put("VL_DIA15", getVlDia15(map));
			map.put("VL_DIA30", getVlDia30(map));
			_result.add(map);
		}

		return _result;
	}

	private void buildDados(Queue list, Queue<DoctoServicoFranqueado> doctos, Queue<DoctoServicoFranqueado> servicoAdicional,
			Queue<ReembarqueDoctoServicoFranqueado> reembarque, Queue<DoctoServicoFranqueado> recalculo, Queue<LancamentoFranqueado> BDM) {
		while ( !list.isEmpty() ) {
			Object o = list.poll();
			if (o instanceof DoctoServicoFranqueado) {
				DoctoServicoFranqueado doc = (DoctoServicoFranqueado) o;
				if( doc.getDoctoServicoFranqueadoOriginal() == null  ){
					if ( "SE".equals(doc.getTpFrete().getValue())) {
						servicoAdicional.add(doc);
					}else{
						doctos.add(doc);
					}
				}else{
					recalculo.add(doc);
				}
			}else if (o instanceof ReembarqueDoctoServicoFranqueado) {
				reembarque.add((ReembarqueDoctoServicoFranqueado)o);
			}else if (o instanceof LancamentoFranqueado) {
				BDM.add((LancamentoFranqueado)o);
			}
		}
	}

	private Map<String, Object> getVlTotalDoc(Queue<DoctoServicoFranqueado> list) {
		Map<String, Object> _result = new HashMap<String, Object>();
		_result.put("VL_TOTAL_DOC_BASE", BigDecimal.ZERO);
		_result.put("VL_PARTICIPACAO_BASE", BigDecimal.ZERO);
		while (!list.isEmpty()) {
			DoctoServicoFranqueado doc = list.poll();
			_result.put("VL_TOTAL_DOC_BASE", MapUtilsPlus.getBigDecimal(_result,"VL_TOTAL_DOC_BASE").add(doc.getVlDoctoServico()));
			_result.put("VL_PARTICIPACAO_BASE",
					MapUtilsPlus.getBigDecimal(_result,"VL_PARTICIPACAO_BASE").add(doc.getVlParticipacao()));
		}
		return _result;
	}

	private BigDecimal getVlReembarque(Queue list) {
		BigDecimal _result = BigDecimal.ZERO;
		while ( !list.isEmpty() ) {
			Object o = list.poll();
			if (o instanceof ReembarqueDoctoServicoFranqueado) {
				ReembarqueDoctoServicoFranqueado doc = (ReembarqueDoctoServicoFranqueado) o;
				_result = _result.add(doc.getVlCte().add(doc.getVlTonelada()));
			}
		}
		return _result;
	}

	private BigDecimal getVlServicoAdicional(Queue<DoctoServicoFranqueado> list) {
		BigDecimal _result = BigDecimal.ZERO;
		while ( !list.isEmpty() ) {
			DoctoServicoFranqueado doc = list.poll();
			_result = _result.add(doc.getVlParticipacao());
		}
		return _result;
	}

	private BigDecimal getVlRecalculo(Queue<DoctoServicoFranqueado> list) {
		BigDecimal _result = BigDecimal.ZERO;
		while ( !list.isEmpty() ) {
			DoctoServicoFranqueado doc = list.poll();
			_result = _result.add(doc.getVlDiferencaParticipacao());
		}
		_result = _result.negate();
		return _result;
	}

	private BigDecimal getVlBDM(Queue<LancamentoFranqueado> list) {
		BigDecimal _result = BigDecimal.ZERO;
		while ( !list.isEmpty() ) {
			LancamentoFranqueado doc = list.poll();
			BigDecimal valor = BigDecimal.ZERO;
			if( "C".equals(doc.getContaContabilFranqueado().getTpLancamento().getValue())) {
				valor = doc.getVlLancamento();
			}else{
				valor = doc.getVlLancamento().negate();
			}
			_result = _result.add(valor);
		}
		return _result;
	}

	private BigDecimal getVlIre(Long idFranquia, YearMonthDay competencia) {
		StringBuilder sql = new StringBuilder()
				.append(" SELECT SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1) ) ")
				.append(" FROM LANCAMENTO_FRQ LF, ").append("  CONTA_CONTABIL_FRQ CCF ")
				.append(" WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
				.append(" AND CCF.TP_CONTA_CONTABIL      = 'IR' ").append(" AND LF.ID_FRANQUIA             = ? ")
				.append(" AND LF.DT_COMPETENCIA          = TO_DATE(?,'dd/MM/yyyy') ")
				.append(" AND LF.TP_SITUACAO_PENDENCIA   = 'A' ");
		Object[] params = new Object[2];
		params[0] = idFranquia;
		params[1] = JTDateTimeUtils.formatDateYearMonthDayToString(competencia);

		BigDecimal _result = (BigDecimal) getJdbcTemplate().queryForObject(sql.toString(), params, BigDecimal.class);
		if (_result == null) {
			_result = BigDecimal.ZERO;
		}
		return _result;
	}

	private BigDecimal getVlCreditoDiverso(Long idFranquia, YearMonthDay competencia) {
		StringBuilder sql = new StringBuilder()
				.append(" SELECT SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1) ) ")
				.append(" FROM LANCAMENTO_FRQ LF, ").append("  CONTA_CONTABIL_FRQ CCF ")
				.append(" WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
				.append(" AND CCF.TP_CONTA_CONTABIL      IN ('CD','CA') ")
				.append(" AND LF.ID_FRANQUIA             = ? ")
				.append(" AND LF.DT_COMPETENCIA          = TO_DATE(?,'dd/MM/yyyy') ")
				.append(" AND LF.TP_SITUACAO_PENDENCIA   = 'A' ");
		Object[] params = new Object[2];
		params[0] = idFranquia;
		params[1] = JTDateTimeUtils.formatDateYearMonthDayToString(competencia);

		BigDecimal _result = (BigDecimal) getJdbcTemplate().queryForObject(sql.toString(), params, BigDecimal.class);
		if (_result == null) {
			_result = BigDecimal.ZERO;
		}
		return _result;
	}

	private BigDecimal getVlDebitoDiverso(Long idFranquia, YearMonthDay competencia) {
		StringBuilder sql = new StringBuilder()
				.append(" SELECT SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1) ) ")
				.append(" FROM LANCAMENTO_FRQ LF, ").append("  CONTA_CONTABIL_FRQ CCF ")
				.append(" WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
				.append(" AND CCF.TP_CONTA_CONTABIL      IN ('DD','DA') ")
				.append(" AND LF.ID_FRANQUIA             = ? ")
				.append(" AND LF.DT_COMPETENCIA          = TO_DATE(?,'dd/MM/yyyy') ")
				.append(" AND LF.TP_SITUACAO_PENDENCIA   = 'A' ");
		Object[] params = new Object[2];
		params[0] = idFranquia;
		params[1] = JTDateTimeUtils.formatDateYearMonthDayToString(competencia);

		BigDecimal _result = (BigDecimal) getJdbcTemplate().queryForObject(sql.toString(), params, BigDecimal.class);
		if (_result == null) {
			_result = BigDecimal.ZERO;
		}
		return _result;
	}

	private BigDecimal getVlOver60(Long idFranquia, YearMonthDay competencia) {
		StringBuilder sql = new StringBuilder()
				.append(" SELECT SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1) ) ")
				.append(" FROM LANCAMENTO_FRQ LF, ").append("  CONTA_CONTABIL_FRQ CCF ")
				.append(" WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
				.append(" AND CCF.TP_CONTA_CONTABIL      = 'OV' ").append(" AND LF.ID_FRANQUIA             = ? ")
				.append(" AND LF.DT_COMPETENCIA          = TO_DATE(?,'dd/MM/yyyy') ")
				.append(" AND LF.TP_SITUACAO_PENDENCIA   = 'A' ");
		Object[] params = new Object[2];
		params[0] = idFranquia;
		params[1] = JTDateTimeUtils.formatDateYearMonthDayToString(competencia);

		BigDecimal _result = (BigDecimal) getJdbcTemplate().queryForObject(sql.toString(), params, BigDecimal.class);
		if (_result == null) {
			_result = BigDecimal.ZERO;
		}
		return _result;
	}

	private BigDecimal getVlIndeninacao(Long idFranquia, YearMonthDay competencia) {
		StringBuilder sql = new StringBuilder()
				.append(" SELECT SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1) ) ")
				.append(" FROM LANCAMENTO_FRQ LF, ").append("  CONTA_CONTABIL_FRQ CCF ")
				.append(" WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
				.append(" AND CCF.TP_CONTA_CONTABIL      IN ('IA','IF','IO') ")
				.append(" AND LF.ID_FRANQUIA             = ? ")
				.append(" AND LF.DT_COMPETENCIA          = TO_DATE(?,'dd/MM/yyyy') ")
				.append(" AND LF.TP_SITUACAO_PENDENCIA   = 'A' ");
		Object[] params = new Object[2];
		params[0] = idFranquia;
		params[1] = JTDateTimeUtils.formatDateYearMonthDayToString(competencia);

		BigDecimal _result = (BigDecimal) getJdbcTemplate().queryForObject(sql.toString(), params, BigDecimal.class);
		if (_result == null) {
			_result = BigDecimal.ZERO;
		}
		return _result;
	}

	private Object getVlParticipacao(Map<String, Object> map) {
		BigDecimal _result = MapUtilsPlus.getBigDecimal(map,"VL_PARTICIPACAO_BASE").add(MapUtilsPlus.getBigDecimal(map,"VL_SERVICO_ADICIONAL")).add(MapUtilsPlus.getBigDecimal(map,"VL_IRE"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_SERV_REEMB"));
		return _result;
	}

	private BigDecimal getVlTotalDebitoDiverso(Map<String, Object> map) {
		BigDecimal _result = MapUtilsPlus.getBigDecimal(map,"VL_RECALCULO").add(MapUtilsPlus.getBigDecimal(map,"VL_BDM")).add(MapUtilsPlus.getBigDecimal(map,"VL_DEBITO_DIVERSO"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_OVER_60")).add(MapUtilsPlus.getBigDecimal(map,"VL_INDENIZACAO"));
		return _result;
	}

	private BigDecimal getVlTotalAPagar(Map<String, Object> map) {
		BigDecimal _result = MapUtilsPlus.getBigDecimal(map,"VL_PARTICIPACAO_BASE").add(MapUtilsPlus.getBigDecimal(map,"VL_CREDITO_DIVERSO"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_SERVICO_ADICIONAL")).add(MapUtilsPlus.getBigDecimal(map,"VL_IRE")).add(MapUtilsPlus.getBigDecimal(map,"VL_SERV_REEMB"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_RECALCULO")).add(MapUtilsPlus.getBigDecimal(map,"VL_BDM")).add(MapUtilsPlus.getBigDecimal(map,"VL_DEBITO_DIVERSO"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_OVER_60")).add(MapUtilsPlus.getBigDecimal(map,"VL_INDENIZACAO"));
		return _result;
	}

	private BigDecimal getVlDia15(Map<String, Object> map) {
		BigDecimal _result = MapUtilsPlus.getBigDecimal(map,"VL_PARTICIPACAO_BASE").add(MapUtilsPlus.getBigDecimal(map,"VL_CREDITO_DIVERSO"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_SERVICO_ADICIONAL")).add(MapUtilsPlus.getBigDecimal(map,"VL_IRE")).add(MapUtilsPlus.getBigDecimal(map,"VL_SERV_REEMB"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_RECALCULO")).add(MapUtilsPlus.getBigDecimal(map,"VL_BDM")).add(MapUtilsPlus.getBigDecimal(map,"VL_DEBITO_DIVERSO"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_OVER_60")).add(MapUtilsPlus.getBigDecimal(map,"VL_INDENIZACAO"));
		_result = _result.divide(new BigDecimal(2), 2);
		return _result;
	}

	private BigDecimal getVlDia30(Map<String, Object> map) {
		BigDecimal _resultTotal = MapUtilsPlus.getBigDecimal(map,"VL_PARTICIPACAO_BASE").add(MapUtilsPlus.getBigDecimal(map,"VL_CREDITO_DIVERSO"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_SERVICO_ADICIONAL")).add(MapUtilsPlus.getBigDecimal(map,"VL_IRE")).add(MapUtilsPlus.getBigDecimal(map,"VL_SERV_REEMB"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_RECALCULO")).add(MapUtilsPlus.getBigDecimal(map,"VL_BDM")).add(MapUtilsPlus.getBigDecimal(map,"VL_DEBITO_DIVERSO"))
				.add(MapUtilsPlus.getBigDecimal(map,"VL_OVER_60")).add(MapUtilsPlus.getBigDecimal(map,"VL_INDENIZACAO"));
		BigDecimal _resultTotalHalf = _resultTotal.divide(new BigDecimal(2), 2);
		return _resultTotal.min(_resultTotalHalf);
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

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public String toInsert(Map<Long,Queue> dados){
		OutputFranqueadoService out = new OutputFranqueadoService();
		StringBuilder sb = new StringBuilder();
		for(Queue list : dados.values() ){
			out.setData(list);
			for(String linha : out.toSQL()){
				sb.append(linha);
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
