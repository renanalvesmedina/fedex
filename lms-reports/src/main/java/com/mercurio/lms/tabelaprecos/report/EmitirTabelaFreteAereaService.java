package com.mercurio.lms.tabelaprecos.report;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * Generated by: ADSM ActionGenerator   
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.report.emitirTabelaFreteAereaService"
 * 																		           
 * @spring.property name="reportName" value="/com/mercurio/lms/tabelaprecos/report/EmitirTabelaFreteAerea.vm"
 *
 * @spring.property name="numberOfCrosstabs" value="2"
 * @spring.property name="crossTabLefts" value="298,542"
 * @spring.property name="crossTabBandWidths" value="244,244"
 * @spring.property name="numbersOfCrossTabColumns" value="6,10"
 * 
 * ET: 30.03.02.11N - Tabela de Frete A�rea
 * 
 */	 
public class EmitirTabelaFreteAereaService extends ReportServiceSupport {
	
	
    private ConfiguracoesFacade configuracoesFacade;
	private TabelasClienteService tabelasClienteService;
	
	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}
	
	private static final String SQL_SUBTIPO;
	
	static
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" Select STP.TP_SUBTIPO_TABELA_PRECO from tabela_preco tp, subtipo_tabela_preco stp");
		sql.append(" where tp.id_tabela_preco = ?");
		sql.append("       and stp.id_subtipo_tabela_preco = tp.id_subtipo_tabela_preco");
		
		SQL_SUBTIPO = sql.toString();
	}
	

	private EmitirTabelaFreteCiaAereaService emitirTabelaFreteCiaAereaService;
	
	public void setEmitirTabelaFreteCiaAereaService(
			EmitirTabelaFreteCiaAereaService emitirTabelaFreteCiaAereaService) {
		this.emitirTabelaFreteCiaAereaService = emitirTabelaFreteCiaAereaService;
	}

	private void adjustParameters(Map parameters){
		transformKey(parameters, "aeroportoOrigem.idAeroporto", "idAeroportoOrigem");
		transformKey(parameters, "aeroportoDestino.idAeroporto", "idAeroportoDestino");
	}
	
	private void transformKey(Map map, String oldKey, String newKey){
		Object value = map.get(oldKey);
		if(value != null){
			map.put(newKey, value);
			map.remove(oldKey);
		}
	}
	
	public JRReportDataObject execute(Map parameters) throws Exception {				
		return null;
	}
		
	public List<Map<String, String>> findDados(Map parameters) throws Exception {	
		
		adjustParameters(parameters);
		
		if (parameters.get("idAeroportoDestino")!=null){
			parameters.put("AGRUPAMENTO_DADOS", "D");
		}else{
			parameters.put("AGRUPAMENTO_DADOS", "O");
		}
		
		List<Map<String, String>> precos  = emitirTabelaFreteCiaAereaService.findDados(parameters);
		
		JdbcTemplate jdbcTemplate = getJdbcTemplate();

		Long idDivisao = MapUtils.getLong(parameters,"idTabelaDivisao");
		Long idTabelaPreco = MapUtils.getLong(parameters,"tipoTabelaPreco.idTabelaPreco", MapUtils.getLongValue(parameters,"tabelaPreco.idTabelaPreco"));
		String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco, jdbcTemplate);

		/* COLETA */
		List coleta = getTabelasClienteService().getSubRelColeta(idTabelaPreco,getJdbcTemplate());
		parameters.put(TabelasClienteService.KEY_PARAMETER_COLETA,  coleta);
		 
		/* ENTREGA */
		List entrega = getTabelasClienteService().getSubRelEntrega(idTabelaPreco,getJdbcTemplate());
		parameters.put(TabelasClienteService.KEY_PARAMETER_ENTREGA, entrega);
		
		String subTipo = (String) jdbcTemplate.queryForObject(SQL_SUBTIPO,new Long[]{idTabelaPreco},String.class);
		parameters.put("SUBTIPO",subTipo);
		   
		/* GENERALIDADES. */
		List generalidades = getTabelasClienteService().getSubRelGeneralidadesTabelaPreco(getTabelasClienteService().montaSqlSubGeneralidadesTab(), idTabelaPreco,dsSimbolo,"",0,0,getConfiguracoesFacade(),getJdbcTemplate(),null);
		parameters.put(TabelasClienteService.KEY_PARAMETER_GENERALIDADES,  generalidades);

		/* LEGENDAS DAS GENERALIDADES. */
		List legendaGeneralidades = getTabelasClienteService().montaPageFooter(generalidades);
		parameters.put(TabelasClienteService.KEY_PARAMETER_LEGENDA_GENERALIDADE, legendaGeneralidades);

		/* FORMALIDADES AEREO */
		List formalidades =  getTabelasClienteService().getFormalidadesAereoNacional(idTabelaPreco, configuracoesFacade, jdbcTemplate);
		parameters.put(TabelasClienteService.KEY_PARAMETER_FORMALIDADES, formalidades);

		/* SERVICOS ADICIONAIS. */
		
		List<Map> generalidadesDificuldadeEntrega = getTabelasClienteService().getGeneralidadesTabelaPrecoDificuldadeEntrega(idTabelaPreco, jdbcTemplate);
		
		parameters.put(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA, generalidadesDificuldadeEntrega);
		
		List servAdd = getTabelasClienteService().getSubServicosAdicionais(idDivisao, null, null, dsSimbolo, getConfiguracoesFacade(),getJdbcTemplate(),null);
		parameters.put(TabelasClienteService.KEY_PARAMETER_SERVICOSAD, servAdd);
		
		return precos;		
	}	

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}