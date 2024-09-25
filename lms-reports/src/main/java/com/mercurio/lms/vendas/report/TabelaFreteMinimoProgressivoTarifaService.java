package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.mercurio.lms.tabelaprecos.report.EmitirTabelasMinimoProgressivoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * @author Alexandre Poletto
 * 
 * ET: 30.03.02.15 - Tabela Frete Minimo Progressivo por Tarifa
 * 
 * @spring.bean id="lms.vendas.report.tabelaFreteMinimoProgressivoTarifaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteMinimoProgressivoTarifa.vm"
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="35"
 * @spring.property name="crossTabBandWidths" value="439"
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 * 
 */
public class TabelaFreteMinimoProgressivoTarifaService extends ReportServiceSupport {

	/* Para utilização de RecursosMensagens */
	private ConfiguracoesFacade configuracoesFacade;
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	private TabelasClienteService tabelasClienteService;

	private static final String NOME_TABELA = "TMP_MN_PROGS";
	
	/**
	 * Para guardar o idTabelaPreco e o dsSimbolo e compartilhar entre metodos
	 */
	private static final ThreadLocal dadosClasseThread = new ThreadLocal();
	

	private EmitirTabelasMinimoProgressivoService tabelaMinimoProgressivo;

    /**
     * Seta o atributo local identificado pela chave com o valor
     * 
     * @param key chave do atributo local
     * @param value valor do atributo local
     */
    private void setLocalVariableValue(Object key, Object value)
    {
    	Map map = (Map)dadosClasseThread.get();
    	if(map == null) map = new HashMap();
    	
    	map.put(key, value);
    	dadosClasseThread.set(map);
    }
    
    /**
     * Retorna valor do atributo local identificado pela chave
     * 
     * @param key have do atributo local
     * @return valor do atributo local
     */
    private Object getLocalVariableValue(Object key)
    {
    	Map map = (Map)dadosClasseThread.get();
    	if(map == null) return null;
    	
    	return map.get(key);
    }
    
	public JRReportDataObject execute(Map parameters) throws Exception {
		return null;
	}
		
	public List<Map<String, String>> findDados(TypedFlatMap parameters) throws Exception {
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		
		Simulacao simulacao  = (Simulacao)MapUtils.getObject(parameters, "simulacao");
		
		if(simulacao == null){
			List<Map> data = getEmitirTabelasClienteDAO().findRelatorioMinimoProgs(parameters, "=", "X");
			
			if(data == null || data.isEmpty() ){
				return null;
			}
			for (Map map : data) {
				parameters.putAll(getCommonParameter(map));
				parameters.put("pagaFreteTonelada", MapUtilsPlus.getBoolean(map, "pagaFreteTonelada"));
				parameters.put("idTabelaDivisao", MapUtils.getLong(map, "idTabelaDivisao"));
				parameters.put("parametroCliente", MapUtils.getLong(map, "listaparametros"));
				parameters.put("blPagaPesoExcedente", "S".equals(MapUtils.getString(map, "blPagaPesoExcedente")));
				parameters.put("vlFretePeso", MapUtilsPlus.getBigDecimal(map, "vlFretePeso",null));
				parameters.put("vlPercMinimoProgr", MapUtilsPlus.getBigDecimal(map, "vlPercMinimoProgr",null));
				parameters.put("vlAdvalorem", MapUtilsPlus.getBigDecimal(map, "vlAdvalorem",null));
				parameters.put("tpIndicadorMinFretePeso", MapUtils.getString(map, "tpIndicadorMinFretePeso"));
				parameters.put("tpIndicadorPercMinimoProgr", MapUtils.getString(map, "tpIndicadorPercMinimoProgr"));
				parameters.put("tpIndicadorAdvalorem", MapUtils.getString(map, "tpIndicadorAdvalorem"));
				parameters.put("tpIndicadorFretePeso", MapUtils.getString(map, "tpIndicadorFretePeso"));
				parameters.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
				parameters.put("idCliente", MapUtils.getLong(map,"idCliente"));
				parameters.put("idDivisao", MapUtils.getLong(map,"idDivisao"));
				parameters.put("idServico", MapUtils.getLong(map,"idServico"));
				parameters.put("idTabelaPreco", MapUtils.getLong(map,"idTabelaPreco"));
			}		
    	String relatorio_et = (getRelatorio_et()==null) ? "30.03.02.15" : (String)getLocalVariableValue("relatorio_et");
    	BigDecimal psMinimo = (getPsMinimo()==null) ? null : (BigDecimal)getLocalVariableValue("psMinimo");
    	String dsSimbolo = (getDsSimbolo()==null) ? null : (String)getLocalVariableValue("dsSimbolo");
    	this.setRelatorio_et(relatorio_et);
    	this.setPsMinimo(psMinimo);
    	this.setDsSimbolo(dsSimbolo);

    	
		jdbcTemplate.execute("DELETE FROM " + NOME_TABELA);
		}
		
		
		
		Long idCliente 				= MapUtils.getLong(parameters, "idCliente");
		Long idDivisao 				= MapUtils.getLong(parameters, "idDivisao");
		Long idParametroCliente 	= MapUtils.getLong(parameters, "parametroCliente");
		Long idTabelaPreco 			= MapUtils.getLong(parameters, "idTabelaPreco");
		Boolean isTabelaNova 		= MapUtils.getBoolean(parameters, "isTabelaNova", false);
		Long idTabelaDivisao		= MapUtils.getLong(parameters, "idTabelaDivisao");
		
		Boolean pagaFreteTonelada	= MapUtils.getBoolean(parameters, "pagaFreteTonelada", Boolean.FALSE);
		
		Map parametersReport = new HashMap();
		// se a tabelaPreco for nula o relatorio devera ser vazio
		if (idTabelaPreco == null) {
			return null;
		}
		
		// valores
		BigDecimal vlFretePrecoBD = MapUtilsPlus.getBigDecimal(parameters,"vlFretePeso", null);
		String vlFretePeso = vlFretePrecoBD == null ? "" : vlFretePrecoBD.toString();
		
		BigDecimal vlPercMinimoProgrBD = MapUtilsPlus.getBigDecimal(parameters,"vlPercMinimoProgr", null);
		String vlPercMinimoProgr = vlPercMinimoProgrBD == null? "": vlPercMinimoProgrBD.toString();
		
		BigDecimal vlAdvaloremBD = MapUtilsPlus.getBigDecimal(parameters,"vlAdvalorem", null);
		String vlAdvalorem = vlAdvaloremBD == null? "" : vlAdvaloremBD.toString();

		if (vlFretePeso.length() == 1) {
			vlFretePeso = "0" + vlFretePeso;
		}
		if (vlPercMinimoProgr.length() == 1) {
			vlPercMinimoProgr = "0" + vlPercMinimoProgr;
		}
		if (vlAdvalorem.length() == 1) {
			vlAdvalorem = "0" + vlAdvalorem;
		}

		// indicadores
		String tpIndicadorFretePeso = MapUtils.getString(parameters, "tpIndicadorFretePeso");
		String tpIndicadorMinFretePeso = MapUtils.getString(parameters, "tpIndicadorMinFretePeso");
		String tpIndicadorPercMinimoFretePeso = MapUtils.getString(parameters, "tpIndicadorPercMinimoProgr");
		String tpIndicadorAdvalorem = MapUtils.getString(parameters, "tpIndicadorAdvalorem");

		String[] localStr = new String[]{vlFretePeso, vlPercMinimoProgr, vlAdvalorem, 
			tpIndicadorAdvalorem, tpIndicadorFretePeso, tpIndicadorMinFretePeso, tpIndicadorPercMinimoFretePeso};
		
		// seta o sql alterado no minimo progressivo
		tabelaMinimoProgressivo.setSql(montaSql(localStr));
		
		//seta parametros oriundos
		parametersReport.putAll(parameters);
		
		// Acha os dados do cabeçalho
		parametersReport.put("DADOS", getTabelasClienteService().montaHeader(parameters, jdbcTemplate, TabelasClienteService.RETORNO_DATASOURCE));
		
		this.setDsSimbolo(getTabelasClienteService().getMoeda(idTabelaPreco,jdbcTemplate));
		
		
		List<Map<String, String>> faixaList = jdbcTemplate.queryForList(tabelaMinimoProgressivo.getSql(),(new Long[]{idTabelaPreco,idTabelaPreco, idTabelaPreco})); 
		int cont = 1;
		Map<String, String> header = new HashMap<String, String>();
		for (Object object : faixaList) {
			Map faixa = (Map)object;
			BigDecimal valor = (BigDecimal)faixa.get("VALOR_FAIXA");
			String coluna = "COLUMN" + cont;
			header.put(coluna, String.valueOf(valor));
			cont++;
		}
		
		 for (Map map : faixaList) {
			 if(MapUtilsPlus.getBigDecimal(map,"FAIXA_PROGRESSIVA").intValue() < 201){
				 map.put("VALOR_FAIXA", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VALOR_FAIXA")));				 
			 }else{
				 map.put("VALOR_FAIXA", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map,"VALOR_FAIXA")));
			 }
		    	map.put("VL_ADVALOREM", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VL_ADVALOREM")));
		    	
		    	if(map.get("VALOR_FAIXA_ADICIONAL") != null){
		    		map.put("VALOR_FAIXA_ADICIONAL", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map,"VALOR_FAIXA_ADICIONAL")));
		    	}
		    	
			}
		
		
		int[] subReports = null;
		
		String servico = null;
		if(simulacao != null){
			List formalidades = getTabelasClienteService().getFormalidadesPropostaRodoviarioNacional(idTabelaPreco,idCliente,idDivisao,null,simulacao.getIdSimulacao(),getJdbcTemplate());
			parametersReport.put(TabelasClienteService.KEY_PARAMETER_FORMALIDADES,  formalidades);
			subReports = new int[]{ 
					TabelasClienteService.SUBREPORT_GENERALIDADES,
					TabelasClienteService.SUBREPORT_SERVICOSNAOCONTRATADOS,
					TabelasClienteService.SUBREPORT_SERVICOSCONTRATADOS };

			Servico serv = simulacao.getServico();
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(serv.getIdServico(), jdbcTemplate);
			servico = MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO");
		}else{
			subReports = new int[]{ 
				TabelasClienteService.SUBREPORT_FORMALIDADES,
				TabelasClienteService.SUBREPORT_GENERALIDADES,
				TabelasClienteService.SUBREPORT_SERVICOSNAOCONTRATADOS,
				TabelasClienteService.SUBREPORT_SERVICOSCONTRATADOS };
			servico = getTabelasClienteService().getTipoServico(idTabelaDivisao, jdbcTemplate);
		}

		parametersReport.putAll(getTabelasClienteService().montaSubRelatoriosOfChoice(idCliente, idDivisao,
						idParametroCliente, idTabelaPreco, isTabelaNova, parametersReport,
						subReports, configuracoesFacade, getJdbcTemplate()));
		
		
		parametersReport.put("SERVICO", servico);

		parametersReport.put("tabelaPreco.idTabelaPreco", idTabelaPreco);
		
		
			if(simulacao != null){
				parametersReport.put("emitirTonelada", simulacao.getBlPagaFreteTonelada());
			}else{
				parametersReport.put("emitirTonelada", Boolean.TRUE.toString());
			}

		if(faixaList == null || faixaList.isEmpty()){
			return null;
		}
		int totRegistros = jdbcTemplate.queryForInt("Select COUNT(*) from " + NOME_TABELA);
		result.addAll(faixaList);
		result.add(header);
		parametersReport.put("TOTAL", Integer.valueOf(totRegistros));
		getTabelasClienteService().setEspacoQuebra(35,8,totRegistros,12,0,parametersReport);
		
		parametersReport.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[] { Integer.valueOf(this.getNumColumns()) });
		parametersReport.put("DS_SIMBOLO", tabelaMinimoProgressivo.getDsSimbolo());
		parametersReport.put("PS_MINIMO", tabelaMinimoProgressivo.getPsMinimo());
		parametersReport.put("SERVICE", this);
		getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);
		
		result.add(parametersReport);

		return result;
		
	}
	
	private TypedFlatMap getCommonParameter(Map param) {
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("idCliente", MapUtils.getLong(param, "idCliente"));
		parameters.put("idDivisao", MapUtils.getLong(param, "idDivisao"));
		parameters.put("idTabelaPreco", MapUtils.getLong(param, "idTabelaPreco"));
		parameters.put("idContato", MapUtils.getLong(param, "idContato"));
		return parameters;
	}
	
	
	/**
	 * Retorna um JRReportDataObject vazio pois nao tera dados na tabela temporaria
	 * @param parametersReport
	 * @return
	 */
	private JRReportDataObject retornaVazio(){
		JRReportDataObject jr = executeQuery("SELECT * FROM " + NOME_TABELA, new HashMap());
		Map map = new HashMap();
		map.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[] {Integer.valueOf(0)});
		jr.setParameters(map);
		return jr;
	}
	
	
	/**
	 * Metodo chamado dentro do Jasper, para formatar os numeros com 2 ou 5
	 * casas decimais, dependendo do seu valor e do seu valor minimo
	 * 
	 * @param parametro
	 * @param psMinimo
	 * @param field
	 * @return
	 */
	public String formataCasasDecimais(String parametro, BigDecimal psMinimo,BigDecimal field) {
		return getTabelasClienteService().formataCasasDecimais(parametro, psMinimo,field);
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
    
    
	/**
	 * 
	 */
	private String montaSql(String[] localVariables)
	{
		String valorFaixa = null;
		String valorAdvalorem = null;
		String desc = null;
		String acres = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer decode = new StringBuffer();
		
		/*
		 * localVariables[0] = vlFretePeso
		 * localVariables[1] = vlPercMinimoProgr
		 * localVariables[2] = vlAdvalorem
		 * localVariables[3] = tpIndicadorAdvalorem
		 * localVariables[4] = tpIndicadorFretePeso
		 * localVariables[5] = tpIndicadorMinFretePeso
		 * localVariables[6] = tpIndicadorPercMinimoFretePeso
		 */
		
		String valor_faixa 		= "(TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO)";
		String pcDesconto 		= valor_faixa + " - ((TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) * (FAIXA.PC_DESCONTO/100))";
		String valor_Adicional 	= "decode(sign(FAIXA_PROGRESSIVA - FAIXA.PS_MINIMO),0,TARIFA.VALOR_FRETE)";
		
		sql.append(" select TARIFA.COD_TARIFA, ");
		sql.append(" 		FAIXA.FAIXA_PROGRESSIVA,  ");
		
		//O primeiro decode verifica se a faixa é <= ao PS_MINIMO 
		decode.append(" decode(greatest(FAIXA.PS_MINIMO,FAIXA.FAIXA_PROGRESSIVA), FAIXA.PS_MINIMO,");
		
		//Se a faixa é <= ao PS_MINIMO, um outro decode verifica se o PC_DESCONTO é nulo
		decode.append(" 	decode(FAIXA.PC_DESCONTO, null,");
		
		//De acordo com o valor do tpIndicadorPercMinimoFretePeso fara o desconto, acrescimo, substituição ou nada com os valores <= ao PS_MINIMO
		// caso o PC_DESCONTO seja diferente de nulo, aplica ele ao valor antes do desconto ou acrescimo do mesmo.
		if (localVariables[6] != null) {
			if ("D".equals(localVariables[6])) {
				desc = localVariables[1];
				BigDecimal vlAux = new BigDecimal(desc);
				if(vlAux.longValue() >= 100){
					//Se o desconto for maior que 100% seta os valores do decode para zero
					decode.append(" 0, 0)");
				}else{
					BigDecimal desconto = new BigDecimal("100").subtract(vlAux);
					desc = desconto.toString().replace(".", "");
					desc = "0".equals(desc) || "100".equals(desc) ? "1" : "0." + desc;
					//Aplica o desconto contido em PC_DESCONTO caso exista e em vlPercMinimoProgr 
					decode.append("(TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) * "+ desc +", (" + pcDesconto + ") *  " + desc + ")");
				}
			} else if ("A".equals(localVariables[6])) {
				acres = localVariables[1];
				BigDecimal acresimo = new BigDecimal(acres);
				acresimo = acresimo.divide(new BigDecimal("100"));
				acres = acresimo.add(new BigDecimal(1)).toString();
				//Aplica o desconto contido em PC_DESCONTO caso exista e o acrescimo em vlPercMinimoProgr 
				decode.append("(TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) * "+ acres +", (" + pcDesconto + ") *  " + acres + ")");
			}else{
				decode.append(valor_faixa + " ,  "+ pcDesconto + ")");
			}
		}else{
			decode.append(valor_faixa + " ,  "+ pcDesconto + ")");
		}
		
		//Se a faixa é > que o PS_MINIMO, um outro decode verifica se o PC_DESCONTO é nulo
		decode.append(" ,decode(FAIXA.PC_DESCONTO, null,");

		
		//De acordo com o valor do tpIndicadorFretePeso fara o desconto, acrescimo ou nada com os valores > ao PS_MINIMO
		// caso o PC_DESCONTO seja diferente de nulo, aplica ele ao valor antes do desconto ou acrescimo do mesmo.
		if (localVariables[0] != null && localVariables[4] != null) {
			if ("V".equals(localVariables[4])) {
				decode.append(localVariables[0] + " , ("+localVariables[0] + " * FAIXA.FATOR_MULTIPLICACAO) - (("+localVariables[0] + " * FAIXA.FATOR_MULTIPLICACAO) * (FAIXA.PC_DESCONTO/100)))) ");
				
			} else if ("D".equals(localVariables[4])) {
				desc = localVariables[0];
				BigDecimal vlAux = new BigDecimal(desc);
				if(vlAux.longValue() >= 100){
					//se o desconto for maior que 100% seta os valores do decode para zero
					decode.append(" 0, 0))");
				}else{
					BigDecimal desconto = new BigDecimal("100").subtract(vlAux);
					desc = desconto.toString().replace(".", "");
					desc = "0".equals(desc) || "100".equals(desc) ? "1" : "0." + desc;
					//Aplica o desconto contido em PC_DESCONTO caso exista e em vlFretePeso 
					decode.append("(TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) * "+ desc +", (" + pcDesconto + ") *  " + desc + "))");
					valor_Adicional  = valor_Adicional + " * " + desc;
				}
			} else if ("A".equals(localVariables[4])) {
				acres = localVariables[0];
				BigDecimal acresimo = new BigDecimal(acres);
				acresimo = acresimo.divide(new BigDecimal("100"));
				acres = acresimo.add(new BigDecimal(1)).toString();
				//Aplica o desconto contido em PC_DESCONTO caso exista e o acrescimo em vlFretePeso 
				decode.append("(TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) * "+ acres +", (" + pcDesconto + ") *  " + acres + "))");
				valor_Adicional  = valor_Adicional + " * " + acres;
				} else {
					decode.append(valor_faixa + " ,  "+ pcDesconto + "))");
			}
		} else {
			decode.append(valor_faixa + " ,  "+ pcDesconto + "))");
		}
		
		if (localVariables[2] != null && localVariables[3] != null) {
			if ("V".equals(localVariables[3])) {
				valorAdvalorem = localVariables[2];
			} else if ("D".equals(localVariables[3])) {
				desc = localVariables[2];
				BigDecimal vlAux = new BigDecimal(desc);
				if(vlAux.longValue() >= 100){
					valorAdvalorem = "0";
				}else{
				BigDecimal desconto = new BigDecimal("100").subtract(vlAux);
				desc = desconto.toString().replace(".", "");
				desc = "0".equals(desc) || "100".equals(desc) ? "1" : "0." + desc;
				valorAdvalorem = desc + " * ADVALOREM.VL_ADVALOREM ";
				}
			} else if ("A".equals(localVariables[3])) {
				acres = localVariables[2];
				BigDecimal acresimo = new BigDecimal(acres);
				acresimo = acresimo.divide(new BigDecimal("100"));
				acres = acresimo.add(new BigDecimal(1)).toString();
				valorAdvalorem = acres + " * ADVALOREM.VL_ADVALOREM ";
			} else if ("P".equals(localVariables[3])) {
				valorAdvalorem = localVariables[2]
						+ " + ADVALOREM.VL_ADVALOREM ";
			} else {
				valorAdvalorem = " ADVALOREM.VL_ADVALOREM ";
			}
		} else {
			valorAdvalorem = " ADVALOREM.VL_ADVALOREM ";
		}
		
		valorFaixa = decode.toString();
		
		if (localVariables[5] != null) {
			if ("V".equals(localVariables[5])) {
				valorFaixa = "greatest(" + decode.toString() + "," + localVariables[0] + ") ";
			}
		}
		
		sql.append(valorFaixa +" as VALOR_FAIXA, ")
		.append(valor_Adicional +" as VALOR_FAIXA_ADICIONAL, ")
		.append("		FAIXA.PC_DESCONTO, ")
		.append(valorAdvalorem + " as VL_ADVALOREM, ")
		.append(" 		FAIXA.PS_MINIMO, ")
		.append(" 		FAIXA.DS_SIMBOLO ")
		.append(" from ")
		.append("		(select F.VL_FAIXA_PROGRESSIVA AS FAIXA_PROGRESSIVA, ")
		.append(" 				vfp.NR_FATOR_MULTIPLICACAO AS FATOR_MULTIPLICACAO, ")
		.append(" 				vfp.PC_DESCONTO, ")
		.append(" 				TAB.PS_MINIMO AS PS_MINIMO,  ")
		.append(" 				MO.DS_SIMBOLO AS DS_SIMBOLO ")
		.append(" 		 from   TABELA_PRECO TAB, ")
		.append(" 				TABELA_PRECO_PARCELA T, ")
		.append(" 				PARCELA_PRECO P, ")
		.append(" 				FAIXA_PROGRESSIVA F,  ")
		.append(" 				VALOR_FAIXA_PROGRESSIVA VFP, ")
		.append(" 				MOEDA MO ")
		.append(" 		 where  T.ID_TABELA_PRECO = ? ")
		.append(" 				AND  TAB.ID_TABELA_PRECO = T.ID_TABELA_PRECO  ")
		.append(" 				AND  T.ID_PARCELA_PRECO = P.ID_PARCELA_PRECO ")
		.append(" 				AND  P.TP_PARCELA_PRECO = 'P'  ")
		.append(" 				AND  P.TP_PRECIFICACAO = 'M' ")
		.append(" 				AND  VFP.ID_FAIXA_PROGRESSIVA = F.ID_FAIXA_PROGRESSIVA ")
		.append(" 				AND TAB.ID_MOEDA = MO.ID_MOEDA ")
		.append(" 				AND  T.ID_TABELA_PRECO_PARCELA = F.ID_TABELA_PRECO_PARCELA")
		.append((getSqlWhere()==null)? "":getSqlWhere());
		setWhere("");
		
		sql.append(" 				) FAIXA, ")
		.append(" 		(SELECT TAR.ID_TARIFA_PRECO AS ID_TARIFA, ")
		.append("  				FP.VL_PRECO_FRETE AS VALOR_FRETE, ")
		.append("  				TAR.CD_TARIFA_PRECO AS COD_TARIFA ")
		.append("  		 FROM   TABELA_PRECO_PARCELA T, ")
		.append("  				PARCELA_PRECO P, ")
		.append("  				PRECO_FRETE FP, ")
		.append(" 				TARIFA_PRECO TAR ")
		.append(" 		 where  T.ID_TABELA_PRECO = ? ")
		.append(" 				AND  T.ID_PARCELA_PRECO = P.ID_PARCELA_PRECO ")
		.append(" 				AND  P.TP_PARCELA_PRECO = 'P'  ")
		.append(" 				AND  P.TP_PRECIFICACAO = 'P' ")
		.append("				AND  P.CD_PARCELA_PRECO = 'IDFreteQuilo' ")
		.append(" 				AND  T.ID_TABELA_PRECO_PARCELA = FP.ID_TABELA_PRECO_PARCELA ")
		.append(" 				AND  FP.ID_TARIFA_PRECO = TAR.ID_TARIFA_PRECO) TARIFA, ")
		.append(" 		(SELECT TAR.ID_TARIFA_PRECO  AS ID_ADVALOREM,  ")
		.append(" 				FP.VL_PRECO_FRETE AS VL_ADVALOREM ")
		.append(" 		 FROM   TABELA_PRECO_PARCELA T,  ")
		.append(" 				PARCELA_PRECO P,  ")
		.append(" 				PRECO_FRETE FP,  ")
		.append(" 				TARIFA_PRECO TAR ")
		.append(" 		 where  T.ID_TABELA_PRECO = ? ")
		.append(" 				AND  T.ID_PARCELA_PRECO = P.ID_PARCELA_PRECO ")
		.append(" 				AND  P.TP_PARCELA_PRECO = 'P'  ")
		.append(" 				AND  P.TP_PRECIFICACAO = 'P' ")
		.append(" 				AND  P.CD_PARCELA_PRECO = 'IDAdvalorem' ")
		.append(" 				AND  T.ID_TABELA_PRECO_PARCELA = FP.ID_TABELA_PRECO_PARCELA ")
		.append(" 				AND  FP.ID_TARIFA_PRECO = TAR.ID_TARIFA_PRECO) ADVALOREM ")
		.append(" WHERE 	ADVALOREM.ID_ADVALOREM = TARIFA.ID_TARIFA ")
		.append(" ORDER BY  TARIFA.COD_TARIFA,  FAIXA.FAIXA_PROGRESSIVA ");
		
		return sql.toString();
	}

	public void setWhere(String where) {
		this.setLocalVariableValue("where", where);
	}

	private String getSqlWhere() {
		return (String)this.getLocalVariableValue("where");
	}
	
	public Integer getPosPsminInFaixa()	{
		return tabelaMinimoProgressivo.getPosPsminInFaixa();
	}
	
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public EmitirTabelasMinimoProgressivoService getTabelaMinimoProgressivo() {
		return tabelaMinimoProgressivo;
	}

	public void setTabelaMinimoProgressivo(
			EmitirTabelasMinimoProgressivoService tabelaMinimoProgressivo) {
		this.tabelaMinimoProgressivo = tabelaMinimoProgressivo;
	}

	public String getDsSimbolo() {
		return (String)this.getLocalVariableValue("dsSimbolo");
	}

	public void setDsSimbolo(String dsSimbolo) {
		this.setLocalVariableValue("dsSimbolo", dsSimbolo);
	}

	public BigDecimal getPsMinimo() {
		return (BigDecimal)this.getLocalVariableValue("psMinimo");
	}

	public void setPsMinimo(BigDecimal psMinimo) {
		this.setLocalVariableValue("psMinimo", psMinimo);
	}

	public int getNumColumns() {
		Integer numColumns = (Integer)this.getLocalVariableValue("numColumns");
		return (numColumns == null)? 0: numColumns;
	}

	public void setNumColumns(int numColumns) {
		this.setLocalVariableValue("numColumns", numColumns);
	}

	public Map getParametersReport() {
		return (Map)this.getLocalVariableValue("parametersReport");
	}

	public void setParametersReport(Map parametersReport) {
		this.setLocalVariableValue("parametersReport", parametersReport);
	}

	public String getRelatorio_et()
	{
		return (String)getLocalVariableValue("relatorio_et");
	}


	public void setRelatorio_et(String relatorio_et) {
		this.setLocalVariableValue("relatorio_et", relatorio_et);
	}

	public void setEmitirTabelasClienteDAO(EmitirTabelasClienteDAO emitirTabelasClienteDAO) {
		this.emitirTabelasClienteDAO = emitirTabelasClienteDAO;
	}

	public EmitirTabelasClienteDAO getEmitirTabelasClienteDAO() {
		return emitirTabelasClienteDAO;
}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
}

}
