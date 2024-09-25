package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * @author Diego Pacheco - LMS - GT5
 *
 * @spring.bean id="lms.vendas.tabelaFreteMinimoRotaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteMinimoRota.jasper"
 * 
 * ET: 30.03.02.24 - Tabela Frete Minimo Por Rota
 * 
 */
public class TabelaFreteMinimoRotaService extends ReportServiceSupport {

	private TabelaFreteMinimoPesoExcedenteService tabelaFreteMinimoPesoExcedenteService;
	private EmitirTabelasClienteDAO tabelasClienteDAO;
	private TabelasClienteService tabelasClienteService;
	
	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}
	
	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}
	
	public JRReportDataObject execute(Map parameters) throws Exception {	
		return null;
	}
	
	public List<Map<String, String>> findDados(TypedFlatMap parameters) throws Exception{
		
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		
		Simulacao simulacao  = (Simulacao)MapUtils.getObject(parameters, "simulacao");
		
		TypedFlatMap parametros = new TypedFlatMap();
		List listaParametros = new ArrayList();
		boolean primeiro = true;
		Long idTabelaDivisao = null;
		
		if(simulacao == null){
			List<Map> data = getEmitirTabelasClienteDAO().findRelatorioFreteMinimoRota(parameters);
			if(data == null || data.isEmpty()){
				return null;
			}
			parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
			for (Map map : data) {
				parametros.put("idCliente", MapUtils.getLong(map, "idCliente"));
				parametros.put("idDivisao", MapUtils.getLong(map, "idDivisao"));
				parametros.put("idTabelaPreco", MapUtils.getLong(map, "idTabelaPreco"));
				parametros.put("idContato", MapUtils.getLong(map, "idContato"));
				parametros.put("idTabelaDivisao", idTabelaDivisao);
				if(primeiro) {
					idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
					parametros.put("pagaFreteTonelada", MapUtilsPlus.getBoolean(map,"pagaFreteTonelada"));
					listaParametros.add(MapUtilsPlus.getLong(map,"listaParametros",null));
					parametros.put("listaParametros", listaParametros );
					primeiro = false;
				} else {
					if(!idTabelaDivisao.equals(MapUtils.getLong(map,"idTabelaDivisao"))) {
						parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
						idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
						parametros.put("pagaFreteTonelada", MapUtilsPlus.getBoolean(map,"pagaFreteTonelada"));
						listaParametros.add(MapUtilsPlus.getLong(map,"listaParametros",null));
						parametros.put("listaParametros", listaParametros );
					} else {
						idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
						listaParametros.add(MapUtilsPlus.getLong(map,"listaParametros",null));
						parametros.put("listaParametros", listaParametros );
					}
				}
			}
			
			String divisaoCliente  = (String) MapUtils.getObject(parameters, "divisaoCliente.idDivisaoCliente");
			if (divisaoCliente != null && divisaoCliente != "") {
				parametros.put("idDivisao", divisaoCliente);
			}else {
				if (data.size() > 1) {
					parametros.put("idDivisao", null);
				}
			}
			
		}else{			
			parametros.putAll(parameters);
		}

		jdbcTemplate.execute("DELETE FROM " + TabelaFreteMinimoPesoExcedenteService.NOME_TABELA);

		Map parametersReport = new HashMap();
			
		parametersReport = tabelaFreteMinimoPesoExcedenteService.AplicaParametrosEPopulaTabela(parametros,"N");
		parameters.put("grupos", parametros.get("grupos"));
		
		int totRegistros = getJdbcTemplate().queryForInt("Select COUNT(*) from " + TabelaFreteMinimoPesoExcedenteService.NOME_TABELA);
		parametersReport.put("TOTAL", Integer.valueOf(totRegistros));
		  
		List parametrosCliente = jdbcTemplate.queryForList("SELECT * FROM "+ TabelaFreteMinimoPesoExcedenteService.NOME_TABELA + " Order by GRUPO, VLMINFRETEPESO");
		
		if (parametrosCliente == null || parametrosCliente.isEmpty()) {
			return null;
		}
		
	    getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);
		
	    tabelasClienteService.ordenaParametroClienteRota(parametros, parametrosCliente, "vlMinFretePeso", "vlFretePeso", "vlAdvalorem");
	    parameters.put("mapCifFob", parametros.get("mapCifFob"));

	    for (Object object : parametrosCliente) {
			Map map = (Map)object;
			map.put("VLMINIMOFRETEQUILO", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VLMINIMOFRETEQUILO")));				 
			map.put("VLFRETEPESO", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map,"VLFRETEPESO")));
			map.put("VLADVALOREM", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VLADVALOREM")));
	}


		result.addAll(parametrosCliente);
		result.add(parametersReport);
		result.add(parameters);
		
		
		
		return result;
	}

	public void setTabelaFreteMinimoPesoExcedenteService(
			TabelaFreteMinimoPesoExcedenteService tabelaFreteMinimoPesoExcedenteService) {
		this.tabelaFreteMinimoPesoExcedenteService = tabelaFreteMinimoPesoExcedenteService;
	}	

	public void setTabelasClienteDAO(EmitirTabelasClienteDAO tabelasClienteDAO) {
		this.tabelasClienteDAO = tabelasClienteDAO;
}

	public EmitirTabelasClienteDAO getEmitirTabelasClienteDAO() {
		return tabelasClienteDAO;
	}	
}
