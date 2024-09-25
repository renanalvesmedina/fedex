package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * ET: 30.03.02.21 - Emitir Tabela Frete Minimo por Tarifa
 * 
 * @author Alexandre Poletto
 *
 * @spring.bean id="lms.vendas.tabelaFreteMinimoTarifaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteMinimoTarifa.jasper"
 */
public class TabelaFreteMinimoTarifaService extends ReportServiceSupport {
    private ConfiguracoesFacade configuracoesFacade;
    private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
    private TabelaFreteMinimoPesoExcedenteService tabelaFreteMinimoPesoExcedenteService;
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
	public List<Map<String, String>> findDados(TypedFlatMap parameters) {
		
		Simulacao simulacao = (Simulacao) MapUtils.getObject(parameters,"simulacao");
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();

		Long idTabelaPreco = null;
		Boolean isTabelaNova = null;
		Long idParametro = null;
		Long idDivisao = null;
		Long idSimulacao = null;
		Long idCliente = null;
		Map parametros = null;
		if(simulacao == null){
		
			List<Map> data = getEmitirTabelasClienteDAO().findRelatorioMinimoTarifa(parameters);
			if(data == null || data.isEmpty()){
				return null;
			}
	
			 idTabelaPreco = MapUtils.getLong(data.get(0), "idTabelaPreco");// 9
			 isTabelaNova = MapUtils.getBoolean(data.get(0), "isTabelaNova", false);
			 idParametro = MapUtils.getLong(data.get(0), "LISTAPARAMETROS");// 107
			 idDivisao = MapUtils.getLong(data.get(0), "idDivisao");// 107
			 idSimulacao = MapUtils.getLong(data.get(0), "idSimulacao");
			 idCliente = MapUtils.getLong(data.get(0), "idCliente");
			 parametros = data.get(0);
		}else{
			 idTabelaPreco = MapUtils.getLong(parameters, "idTabelaPreco");// 9
			 isTabelaNova = MapUtils.getBoolean(parameters, "isTabelaNova", false);
			 idParametro = MapUtils.getLong(parameters, "LISTAPARAMETROS");// 107
			 idDivisao = MapUtils.getLong(parameters, "idDivisao");// 107
			 idSimulacao = MapUtils.getLong(parameters, "idSimulacao");
			 idCliente = MapUtils.getLong(parameters, "idCliente");
			 parametros = parameters;
		}
		JdbcTemplate jdbcTemplate = getJdbcTemplate();

		//relatório em branco
		if(idTabelaPreco == null){
			return null;
		}

		Long[] listParams = null;
		if(idSimulacao==null){
			if(Boolean.TRUE.equals(isTabelaNova)) {
				listParams = new Long[]{idTabelaPreco, idDivisao, idParametro, idTabelaPreco};
			} else listParams = new Long[]{idDivisao, idTabelaPreco, idParametro};
		}else{
			if(Boolean.TRUE.equals(isTabelaNova)) {
				listParams = new Long[]{idTabelaPreco, idParametro, idTabelaPreco};
			} else listParams = new Long[]{idTabelaPreco,idParametro};
		}

		/** Busca Parametros */
		String query = createQuery(idSimulacao, isTabelaNova);
		List parametrosCliente = jdbcTemplate.queryForList(query, listParams);
		for (Object m : parametrosCliente) {
			((Map)m).put("grupo", "grupo0");
		}

		for (Object m : parametrosCliente) {
			Map map = (Map)m;
		
			BigDecimal vlPrecoFrete = MapUtilsPlus.getBigDecimal(map,"VL_PRECO_FRETE",BigDecimal.ZERO);
			BigDecimal vlMinFretePeso = MapUtilsPlus.getBigDecimal(map,"VL_MIN_FRETE_PESO",BigDecimal.ZERO);
			BigDecimal vlPercMinimo = MapUtilsPlus.getBigDecimal(map,"VL_PERC_MINIMO_PROGR",BigDecimal.ZERO);
			String indicadorPercMinimo = MapUtils.getString(map, "TP_INDICADOR_PERC_MINIMO_PROGR");
		
		
			BigDecimal pesoMinimo= calculaVlPesoMinimo(vlPrecoFrete,vlMinFretePeso,vlPercMinimo,indicadorPercMinimo);
			map.put("VL_PESO_MINIMO", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, pesoMinimo));

			map.put("VL_FRETE_PESO", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map,"VL_FRETE_PESO")));
		
	    	map.put("VL_ADVALOREM", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VL_ADVALOREM")));
		}

		//Busca o subtipo da tabela de preco
		StringBuffer querySubtipo = new StringBuffer()
    		.append(" SELECT TP_SUBTIPO_TABELA_PRECO ")
    		.append(" FROM SUBTIPO_TABELA_PRECO STP, TABELA_PRECO TP ")
    		.append(" WHERE STP.ID_SUBTIPO_TABELA_PRECO = TP.ID_SUBTIPO_TABELA_PRECO AND ID_TABELA_PRECO = ?");
    	List<Map> subtipos = jdbcTemplate.queryForList(querySubtipo.toString(), new Long[]{idTabelaPreco});
    	Map subtipoMapped = subtipos.get(0);
    	String tpSubtipo = MapUtils.getString(subtipoMapped, "TP_SUBTIPO_TABELA_PRECO");

    	BigDecimal valorEmbutido = new BigDecimal(0);
    	String observacao = "";
    	//List generalidades = tabelasClienteService.getSubRelGeneralidades(idParametro, idTabelaPreco, isTabelaNova, "", configuracoesFacade, getJdbcTemplate());
		//Se o subtipo da tabela for Y imbute os valores das generalidades
		if ("Y".equalsIgnoreCase(tpSubtipo)){
			//Observação
			observacao = "******* N/B ATÉ 200 kg DESPACHO POR CONHECIMENTO INCLUSO"; 
						}

		int[] subreports = {
        		TabelasClienteService.SUBREPORT_FORMALIDADES,
        		TabelasClienteService.SUBREPORT_GENERALIDADES,
        		TabelasClienteService.SUBREPORT_SERVICOSCONTRATADOS,
        		TabelasClienteService.SUBREPORT_SERVICOSNAOCONTRATADOS};

		Map parametersReport = getTabelasClienteService().getAllChoiceReportParameters(parametros,isTabelaNova,subreports,configuracoesFacade,jdbcTemplate);
		parametersReport.put("SERVICE",this);
		parametersReport.put("OBSERVACAO", observacao);
		parametersReport.put("VALOR_EMBUTIDO", valorEmbutido);
		String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco,getJdbcTemplate());


		if(simulacao!=null){
			Servico servico = simulacao.getServico();
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(servico.getIdServico(), getJdbcTemplate());
			parametersReport.put("SERVICO", MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO"));
		}

		int totRegistros = 1;
		if(!parametrosCliente.isEmpty()){
			totRegistros = parametrosCliente.size();
		}
		result.addAll(parametrosCliente);
		result.add(parametersReport);

		return result;
	}

	private BigDecimal calculaVlPesoMinimo(BigDecimal vlPreco, BigDecimal vlMinFretePeso, BigDecimal vlPercMinimo, String indicador){
		BigDecimal vlPrecoFrete = vlPreco;
		vlPrecoFrete = vlPrecoFrete.multiply(vlMinFretePeso);

		if("A".equals(indicador) && BigDecimalUtils.hasValue(vlPercMinimo)){
			vlPrecoFrete = BigDecimalUtils.acrescimo(vlPrecoFrete, vlPercMinimo);
		} else if("D".equals(indicador) && BigDecimalUtils.hasValue(vlPercMinimo)){
			vlPrecoFrete = BigDecimalUtils.desconto(vlPrecoFrete, vlPercMinimo);
		}
		vlPrecoFrete = vlPrecoFrete.setScale(2, RoundingMode.HALF_UP);
		
		return vlPrecoFrete;
	}
	

	/** Rotina chamada pelo Relatório */
	public BigDecimal calculaCampo(String tpIndicador, BigDecimal vlKg, BigDecimal vlMinFretePeso, BigDecimal vl, BigDecimal valorEmbutido){
		BigDecimal multiplicacao = null;
		BigDecimal total = null;
		if(vlMinFretePeso != null){
			multiplicacao = vlMinFretePeso.multiply(vlKg);
			if (valorEmbutido != null){
				multiplicacao = multiplicacao.add(valorEmbutido); 
			}
			total = getTabelasClienteService().calculaPercentagem(tpIndicador,vl,multiplicacao);
		}else{
			if (valorEmbutido != null){
				vlKg = vlKg.add(valorEmbutido); 
			}
			total = getTabelasClienteService().calculaPercentagem(tpIndicador,vl,vlKg);
		}
		return total;
	}
	
	/**
	 * Rotina chamada pelo Relatorio
	 */
	public BigDecimal calculaCampo(String tpIndicador, BigDecimal vlKg, BigDecimal vlMinFretePeso, BigDecimal vl){
		return calculaCampo(tpIndicador, vlKg, vlMinFretePeso, vl,null);
	}
	
	private String createQuery(Long idSimulacao, Boolean isTabelaNova){
		StringBuffer sql = new StringBuffer()
			.append(" SELECT DISTINCT ")
			.append("	tar.cd_tarifa_preco as TARIFA,")
			.append("	pc.VL_MIN_FRETE_PESO,")
			.append("	pf.vl_preco_frete as VL_PRECO_FRETE,")
			.append("	pc.TP_INDICADOR_MIN_FRETE_PESO,")
			.append("	pc.TP_INDICADOR_FRETE_PESO,")
			.append("	pc.TP_INDICADOR_PERC_MINIMO_PROGR,")
			.append("	pc.TP_INDICADOR_ADVALOREM,")
			.append("	pc.VL_FRETE_PESO,")
			.append("	pc.VL_PERC_MINIMO_PROGR,")
			.append("	pc.VL_ADVALOREM  as PC_VL_ADVALOREM ,")
			.append("	(")
			.append("      SELECT ")
			.append("			pf.vl_preco_frete as vlAdvalorem")
			.append("      FROM ")
			.append("           tabela_preco_parcela tpp,")
			.append("	        parcela_preco pp,")
			.append("	        preco_frete pf")
			.append("      WHERE ");
		if(!Boolean.TRUE.equals(isTabelaNova)) {
			sql.append(" tpp.id_tabela_preco = tp.id_tabela_preco ");
		} else sql.append(" tpp.id_tabela_preco = ? "); 
		sql.append("	        and tpp.id_parcela_preco = pp.id_parcela_preco")
			.append("	        and pp.cd_parcela_preco = 'IDAdvalorem'")
			.append("	        and tpp.id_tabela_preco_parcela = pf.id_tabela_preco_parcela")
			.append("	        and pf.id_tarifa_preco = tar.id_tarifa_preco")
			.append("            ) as VL_ADVALOREM");

		/** From */
		sql.append(" FROM");
		if(idSimulacao == null){
			sql.append("  cliente c,")
			.append(" 	  divisao_cliente d,")
			.append("	  tabela_divisao_cliente tdc,");
		}
		sql.append("  parametro_cliente pc,")
			.append("	  tabela_preco tp,")
			.append("	  tipo_tabela_preco ttp,")
			.append("	  subtipo_tabela_preco stp,")
			.append("	  tabela_preco_parcela tpp,")
			.append(" 	  preco_frete pf,")
			.append(" 	  parcela_preco pp,")
			.append("	  tarifa_preco tar");
		
		/** Where */
		sql.append(" WHERE ");
		if(idSimulacao == null){
			sql.append("       c.id_cliente = d.id_cliente");
			sql.append("       and d.id_divisao_cliente = tdc.id_divisao_cliente");
			sql.append("       and tdc.id_divisao_cliente = ?");
			sql.append("       and tdc.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente");
			sql.append("       and tdc.id_tabela_preco = tp.id_tabela_preco");
		} else sql.append(" pc.id_simulacao = ").append(idSimulacao);

		if(!Boolean.TRUE.equals(isTabelaNova)) {
			sql.append("	and tp.id_tabela_preco = ? ");
		}
	    sql.append("	    and pc.id_parametro_cliente = ?")	
		    .append("       and tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco")
		    .append("       and tp.id_subtipo_tabela_preco =  stp.id_subtipo_tabela_preco");

	    if(!Boolean.TRUE.equals(isTabelaNova)) {
	    	sql.append(" and tp.id_tabela_preco = tpp.id_tabela_preco ");
		} else sql.append(" and tpp.id_tabela_preco = ? ");
	    sql.append("       and tpp.id_parcela_preco = pp.id_parcela_preco")
		    .append("       and pp.cd_parcela_preco = 'IDFreteQuilo'")
		    .append("       and tpp.id_tabela_preco_parcela = pf.id_tabela_preco_parcela")
		    .append("       and pf.id_tarifa_preco = tar.id_tarifa_preco")
		    .append("		ORDER BY tar.cd_tarifa_preco");
		return sql.toString();
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	private TypedFlatMap getCommonParameter(Map param) {
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("idCliente", MapUtils.getLong(param, "idCliente"));
		parameters.put("idDivisao", MapUtils.getLong(param, "idDivisao"));
		parameters.put("idTabelaPreco", MapUtils.getLong(param, "idTabelaPreco"));
		parameters.put("idContato", MapUtils.getLong(param, "idContato"));
		return parameters;
}

	public void setEmitirTabelasClienteDAO(EmitirTabelasClienteDAO emitirTabelasClienteDAO) {
		this.emitirTabelasClienteDAO = emitirTabelasClienteDAO;
	}

	public EmitirTabelasClienteDAO getEmitirTabelasClienteDAO() {
		return emitirTabelasClienteDAO;
	}
	
	public TabelaFreteMinimoPesoExcedenteService getTabelaFreteMinimoPesoExcedenteService() {
		return tabelaFreteMinimoPesoExcedenteService;
	}
	public void setTabelaFreteMinimoPesoExcedenteService(
			TabelaFreteMinimoPesoExcedenteService tabelaFreteMinimoPesoExcedenteService) {
		this.tabelaFreteMinimoPesoExcedenteService = tabelaFreteMinimoPesoExcedenteService;
	}
}
