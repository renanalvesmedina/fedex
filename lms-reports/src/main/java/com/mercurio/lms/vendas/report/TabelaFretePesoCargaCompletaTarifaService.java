package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * @author Alexandre Poletto 
 * 
 * 30.03.02.22 Emitir tabela de frete minimo progressivo carga completa por tarifa
 *
 * @spring.bean id="lms.vendas.report.tabelaFretePesoCargaCompletaTarifaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFretePesoCargaCompletaTarifa.vm"
 * 
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="35"
 * @spring.property name="crossTabBandWidths" value="439" 
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class TabelaFretePesoCargaCompletaTarifaService extends ReportServiceSupport {

	
	private TabelaFreteMinimoProgressivoTarifaService tabelaFreteTarifaService;
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
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

	public List<Map<String, String>> findDados(TypedFlatMap parameters)  throws Exception{
		List<Map> data = new ArrayList<Map>();
		Boolean blCargaCompleta = parameters.getBoolean("cargaCompleta");
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		data = getEmitirTabelasClienteDAO().findRelatorioMinimoProgs(parameters, "<>", "Y");
			
		if(data == null || data.isEmpty()){
			return null;
		}
		TypedFlatMap parametros = new TypedFlatMap();
		for (Map map : data) {
			parametros = getCommonParameter(map);
			parametros.put("pagaFreteTonelada", MapUtilsPlus.getBoolean(map,"pagaFreteTonelada"));
			parametros.put("parametroCliente", MapUtils.getLong(map,"listaParametros"));
			parametros.put("vlFretePeso", MapUtilsPlus.getBigDecimal(map,"vlFretePeso",null));
			parametros.put("vlPercMinimoProgr", MapUtilsPlus.getBigDecimal(map,"vlPercMinimoProgr",null));
			parametros.put("vlAdvalorem", MapUtilsPlus.getBigDecimal(map,"vlAdvalorem",null));
			parametros.put("tpIndicadorMinFretePeso", MapUtils.getString(map,"tpIndicadorMinFretePeso"));
			parametros.put("tpIndicadorPercMinimoProgr", MapUtils.getString(map,"tpIndicadorPercMinimoProgr"));
			parametros.put("tpIndicadorAdvalorem", MapUtils.getString(map,"tpIndicadorAdvalorem"));
			parametros.put("tpIndicadorFretePeso", MapUtils.getString(map,"tpIndicadorFretePeso"));
			parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
			parameters.put("idCliente", MapUtils.getLong(map,"idCliente"));
			parameters.put("idDivisao", MapUtils.getLong(map,"idDivisao"));
			parameters.put("idServico", MapUtils.getLong(map,"idServico"));
			parameters.put("idTabelaPreco", MapUtils.getLong(map,"idTabelaPreco"));
		

		Long idTabelaPreco		= MapUtils.getLong(parameters, "idTabelaPreco");
		Simulacao simulacao         = (Simulacao)MapUtils.getObject(parameters, "simulacao");
		
		tabelaFreteTarifaService.setRelatorio_et("30.03.02.22");
		
		String where = " AND F.VL_FAIXA_PROGRESSIVA >= 1000";
		tabelaFreteTarifaService.setWhere(where);
		
		tabelaFreteTarifaService.findDados(parameters);
		Map parametersReport = tabelaFreteTarifaService.getParametersReport();
		if(parametersReport == null){
			return null;
		}
		parametersReport.put("SERVICE", this);
		parametersReport.put("idCliente", MapUtilsPlus.getLong(parameters,"idCliente",null));
        parametersReport.put("idContato", MapUtilsPlus.getLong(parameters,"idContato",null));
		getTabelasClienteService().montaLogoMercurio(parametersReport, getJdbcTemplate());
		
		String servico = null;
		if(simulacao != null){
			Servico serv = simulacao.getServico();
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(serv.getIdServico(), getJdbcTemplate());
			servico = MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO");
		}else{
			servico = getTabelasClienteService().getTipoServicoTabela(idTabelaPreco, getJdbcTemplate());
		}
		parametersReport.put("SERVICO", servico);
		
		tabelaFreteTarifaService.setRelatorio_et("30.03.02.15");
		
		parameters.put(idTabelaPreco, parametersReport);
	}
		return result;
	}
		
		
	/**
	 * Metodo chamado dentro do Jasper, para formatar os numeros com 2 ou 5 casas decimais,
	 * dependendo do seu valor e do seu valor minimo
	 * @param parametro
	 * @param psMinimo
	 * @param field
	 * @return
	 */
    public String formataCasasDecimais(String parametro,BigDecimal psMinimo,BigDecimal field){
    	
    	return getTabelasClienteService().formataCasasDecimais(parametro,psMinimo,field);
    }
		
	/**
	 * Metodo chamado dentro do Jasper, para formatar o texto das colunas dinamicas do relatorio
	 * 
	 * @param parametro
	 * @param dsSimbolo
	 * @param psMinimo
	 * @return
	 */
    public String formataColumnParameter(String parametro, String dsSimbolo, BigDecimal psMinimo)
    {
    	return getTabelasClienteService().formataColumnParameter(parametro, dsSimbolo, psMinimo);
    }
    
	public TabelaFreteMinimoProgressivoTarifaService getTabelaFreteTarifaService() {
		return tabelaFreteTarifaService;
	}


	public void setTabelaFreteTarifaService(
		TabelaFreteMinimoProgressivoTarifaService tabelaFreteTarifaService) {
		this.tabelaFreteTarifaService = tabelaFreteTarifaService;
	}

	
	private TypedFlatMap getCommonParameter(Map param) {
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("idCliente", MapUtils.getLong(param, "idCliente"));
		parameters.put("idDivisao", MapUtils.getLong(param, "idDivisao"));
		parameters.put("idTabelaPreco", MapUtils.getLong(param, "idTabelaPreco"));
		parameters.put("idContato", MapUtils.getLong(param, "idContato"));
		return parameters;
}

	public EmitirTabelasClienteDAO getEmitirTabelasClienteDAO() {
		return emitirTabelasClienteDAO;
	}


	public void setEmitirTabelasClienteDAO(
			EmitirTabelasClienteDAO emitirTabelasClienteDAO) {
		this.emitirTabelasClienteDAO = emitirTabelasClienteDAO;
	}
}
