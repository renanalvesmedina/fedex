package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
 * ET: 30.03.02.16 - Tabela Frete Minimo Progressivo Taxa Embutida
 * 
 * @spring.bean id="lms.vendas.report.TabelaFreteMinimoProgressivoTaxaEmbutidaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabFreteMinProgTaxasEmbutidas.vm"
 * 
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="35"
 * @spring.property name="crossTabBandWidths" value="439" 
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class TabelaFreteMinimoProgressivoTaxaEmbutidaService extends ReportServiceSupport {
	
	/* Nome da Tabela temporaria */
	private static final String NOME_TABELA = "TMP_MN_PROGS";	
	
	/* Para utilização de RecursosMensagens */
	private ConfiguracoesFacade configuracoesFacade;
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	private EmitirTabelasMinimoProgressivoService tabelaMinimoProgressivo;
	private TabelasClienteService tabelasClienteService;
		
	/**
	 * Para guardar o idTabelaPreco e o dsSimbolo e compartilhar entre metodos
	 */
	private static final ThreadLocal dadosClasseThread = new ThreadLocal();
	
	
   public JRReportDataObject execute(Map parameters) throws Exception {
		return null;
	}
		
	public List<Map<String, String>> findDados(TypedFlatMap parameters) throws Exception {
		List<Map> data = getEmitirTabelasClienteDAO().findRelatorioMinimoProgs(parameters, "=", "Y");

		Simulacao simulacao  = (Simulacao)MapUtils.getObject(parameters, "simulacao");
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		
		if(simulacao == null){
			if(data == null || data.isEmpty() ){
				return null;
			}
			for (Map map1 : data) {
				parameters = getCommonParameter(map1);
				parameters.put("pagaFreteTonelada", MapUtilsPlus.getBoolean(map1,"pagaFreteTonelada"));
				parameters.put("idTabelaDivisao", MapUtils.getLong(map1,"idTabelaDivisao"));
				parameters.put("parametroCliente", MapUtils.getLong(map1,"listaparametros"));
				parameters.put("vlFretePeso", MapUtilsPlus.getBigDecimal(map1,"vlFretePeso",null));
				parameters.put("vlPercMinimoProgr", MapUtilsPlus.getBigDecimal(map1,"vlPercMinimoProgr",null));
				parameters.put("vlAdvalorem", MapUtilsPlus.getBigDecimal(map1,"vlAdvalorem",null));
				parameters.put("tpIndicadorMinFretePeso", MapUtils.getString(map1,"tpIndicadorMinFretePeso"));
				parameters.put("tpIndicadorPercMinimoProgr", MapUtils.getString(map1,"tpIndicadorPercMinimoProgr"));
				parameters.put("tpIndicadorAdvalorem", MapUtils.getString(map1,"tpIndicadorAdvalorem"));
				parameters.put("tpIndicadorFretePeso", MapUtils.getString(map1,"tpIndicadorFretePeso"));
				parameters.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
				parameters.put("idCliente", MapUtils.getLong(map1,"idCliente"));
				parameters.put("idDivisao", MapUtils.getLong(map1,"idDivisao"));
				parameters.put("idServico", MapUtils.getLong(map1,"idServico"));
				parameters.put("idTabelaPreco", MapUtils.getLong(map1,"idTabelaPreco"));
			}
		}
				// limpa a tabela temporaria
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		jdbcTemplate.execute("DELETE FROM " + NOME_TABELA);
		
		// Parametros recebidos da Action
		Long idCliente 				= MapUtils.getLong(parameters, "idCliente");
		Long idDivisao 				= MapUtils.getLong(parameters, "idDivisao");
		Long idParametroCliente 	= MapUtils.getLong(parameters, "parametroCliente");
		Long idTabelaPreco 			= MapUtils.getLong(parameters, "idTabelaPreco");
		boolean isTabelaNova 		= MapUtils.getBoolean(parameters, "isTabelaNova", false);
		Long idTabelaDivisao		= MapUtils.getLong(parameters, "idTabelaDivisao");
		Long idServico 				= MapUtils.getLong(parameters, "idServico");
		boolean pagaFreteTonelada 	= MapUtils.getBoolean(parameters, "pagaFreteTonelada", false);
		
		Map parametersReport = new HashMap();
		
		parametersReport.put("idServico", idServico);
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
		
		String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco,jdbcTemplate);
		
		
		//Busca a generalidade fora do metodo montaSubRelatoriosOfChoice para poder usar a sua lista e não seu dataSource
		List generalidades = getTabelasClienteService().getSubRelGeneralidades(idDivisao,idServico,isTabelaNova,dsSimbolo,configuracoesFacade,getJdbcTemplate(),idDivisao);
		parametersReport.put(TabelasClienteService.KEY_PARAMETER_GENERALIDADES, generalidades);
		BigDecimal despacho = BigDecimal.ZERO;
		if(generalidades != null)
		{
			for (Iterator iter = generalidades.iterator(); iter.hasNext();) {
				Map dados = (Map)iter.next();
				Map map = (Map) dados.get("EMBUTIDO");
				if(map != null){
					despacho = (BigDecimal) map.get("despacho_cliente");
				}
			}
		}
		despacho = despacho == null ? BigDecimal.ZERO : despacho;
		List faixaList = jdbcTemplate.queryForList(montaSql(localStr, BigDecimal.ZERO), new Long[]{idTabelaPreco,idTabelaPreco, idTabelaPreco});
		
	
		int cont = 1;
		Map<String, String> header = new HashMap<String, String>();
		for (Object object : faixaList) {
			Map faixa = (Map)object;
			BigDecimal valor = (BigDecimal)faixa.get("VALOR_FAIXA");
			valor = valor.add(despacho);
			faixa.put("VALOR_FAIXA",valor);
			String coluna = "COLUMN" + cont;
			header.put(coluna, String.valueOf(valor));
			cont++;
		}
		for (Object object : faixaList) {
			Map map = (Map) object;
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
		
		parametersReport.put("DADOS", getTabelasClienteService().montaHeader(parameters, jdbcTemplate, TabelasClienteService.RETORNO_DATASOURCE));
		
		int[] subReports = null;
		String servico = null;
		
		if(simulacao != null){
			List formalidades = getTabelasClienteService().getFormalidadesPropostaRodoviarioNacional(idTabelaPreco,idCliente,idDivisao,idParametroCliente,simulacao.getIdSimulacao(),getJdbcTemplate());
			parametersReport.put(TabelasClienteService.KEY_PARAMETER_FORMALIDADES,  formalidades);
			subReports = new int[]{ 
					TabelasClienteService.SUBREPORT_SERVICOSNAOCONTRATADOS,
					TabelasClienteService.SUBREPORT_SERVICOSCONTRATADOS };
			
			Servico serv = simulacao.getServico();
			idServico = serv.getIdServico();
			parametersReport.put("idServico", idServico);
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(serv.getIdServico(), jdbcTemplate);
			servico = MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO");
		}else{
			subReports = new int[]{ 
				TabelasClienteService.SUBREPORT_FORMALIDADES,
				TabelasClienteService.SUBREPORT_SERVICOSNAOCONTRATADOS,
				TabelasClienteService.SUBREPORT_SERVICOSCONTRATADOS };
			servico = getTabelasClienteService().getTipoServico(idTabelaDivisao, jdbcTemplate);
		}

		parametersReport = getTabelasClienteService().montaSubRelatoriosOfChoice(idCliente, idDivisao,
						idParametroCliente, idTabelaPreco, isTabelaNova, parametersReport,
						subReports, configuracoesFacade, getJdbcTemplate());
		
		parametersReport.put(TabelasClienteService.KEY_PARAMETER_PAGEFOOTER,getTabelasClienteService().montaPageFooter(generalidades));
		
		parametersReport.put("SERVICO", servico);

		parametersReport.put("tabelaPreco.idTabelaPreco", idTabelaPreco);
		
		result.addAll(faixaList);
		
		
		result.add(header);

		parametersReport.put("OBSERVACAO", "******* N/B ATÉ 200 KGs DESPACHO POR CONHECIMENTO INCLUSO.");
		parametersReport.put("SERVICE", this);
		getTabelasClienteService().montaLogoMercurio(parametersReport, getJdbcTemplate());
		
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
	 * Metodo chamado dentro do Jasper, para formatar os numeros com 2 ou 5 casas decimais,
	 * dependendo do seu valor e do seu valor minimo
	 * @param parametro
	 * @param psMinimo
	 * @param field
	 * @return
	 */
    public String formataCasasDecimais(String parametro, BigDecimal psMinimo, BigDecimal field){
    	return getTabelasClienteService().formataCasasDecimais(parametro, psMinimo, field);
     }
    
    
	/**
	 * 
	 * @param list 
	 * @param faixaProgColunmName
	 */
    private Map calcReport(List list, Long idTabelaPreco,boolean emitirTonelada, JdbcTemplate jdbcTemplate) {
    	LinkedHashSet crosstab = new LinkedHashSet();
    	
        Map tabelas = new HashMap();
        
        BigDecimal faixaProgress = null, psMinimo = null;
        String tarifaAux = "";
        long maxFaixaProgress = getMaxFaixaProgressiva(idTabelaPreco, emitirTonelada, jdbcTemplate);
        int contTarifa = 0;
        
        for (Iterator it = list.iterator(); it.hasNext(); ) { 
           Map map = (Map)it.next();
           String tarifa = map.get("COD_TARIFA").toString();
           Object column = map.get("FAIXA_PROGRESSIVA");
           Object dado   = map.get("VALOR_FAIXA");
           Object acima  = map.get("VALOR_FAIXA_ADICIONAL");           
           Object adv    = map.get("VL_ADVALOREM");
           
           psMinimo = new BigDecimal(MapUtils.getString(map,"PS_MINIMO","0"));
           
           faixaProgress = new BigDecimal(MapUtils.getString(map,"FAIXA_PROGRESSIVA","0"));
           
           int resultCompareFaixaProgress = faixaProgress.compareTo(psMinimo);
           if(resultCompareFaixaProgress < 1) { //Se faixaProgress menor(-1) ou igual(0) a psMinimo
        	   if(!tarifa.equals(tarifaAux)) {
        		   tarifaAux = tarifa; 
        		   contTarifa = 1;
        	   } else {
        		   contTarifa++;
        	   }
           }
                                               
       	   //Não mostrar toneladas se emitir Tonelada estiver desmarcado
       	   if(!emitirTonelada && resultCompareFaixaProgress == 1) {
       		   //não adiciona faixa da tonelada
       	   } else {
               crosstab.add(column != null ? column.toString() : column);
               if(maxFaixaProgress<=psMinimo.longValue() && faixaProgress.longValue() == maxFaixaProgress)
            	   crosstab.add("Acima " + faixaProgress.longValue());
               
       		   if (!tabelas.containsKey(tarifa)) {
                   tabelas.put(tarifa, new ArrayList());
                   ((ArrayList)tabelas.get(tarifa)).add(adv);                                      
               }
               ((ArrayList)tabelas.get(tarifa)).add(dado);
               if(maxFaixaProgress<=psMinimo.longValue() && faixaProgress.longValue() == maxFaixaProgress) {
            	   ((ArrayList)tabelas.get(tarifa)).add(acima);
               }
           }
        }//for iterator da list
               
        String names = ""; 
        String columnName = null;
        int i = 0;

        //definicao do desconto das colunas
        for (Iterator j = crosstab.iterator(); j.hasNext();) {
        	j.next();
           	columnName = "COLUMN" + ++i;
        	names += ", " + columnName;
        }
        //insercao na tabela temporaria
        for (Iterator it = tabelas.keySet().iterator(); it.hasNext(); ) {
           String tarifa = (String)it.next();
           int cont = 0;
           List values = new LinkedList();
           List lista    = (List)tabelas.get(tarifa);
           StringBuffer sb = new StringBuffer("INSERT INTO " + NOME_TABELA + " (TARIFA, ADVALOREM" + names + ") VALUES(? ");
           values.add(tarifa);
           for (Iterator it2 = lista.iterator(); it2.hasNext(); ) {
        	   if (cont <= crosstab.size()){
        	       sb.append(", ?");
        	       values.add(it2.next());
        	   } else {
        		   break;
        	   }
        	   cont++;
           }
           sb.append(")\n");
           getJdbcTemplate().update(sb.toString(), values.toArray());
        }
        
        Map map = new HashMap();
        map.put("crosstab",crosstab);
        map.put("psMinimo",psMinimo);
		return map;

      }
    
    
    /**
	 * 
	 */
	private String montaSql(String[] localVariables, BigDecimal vlEmbute)
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
		
		String valor_faixa 		 = "((TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) + " + vlEmbute +")";
		String valor_Adicional 		 = "decode(sign(FAIXA_PROGRESSIVA - FAIXA.PS_MINIMO),0,TARIFA.VALOR_FRETE)";
		String vl_faixaSemEmbute = "(TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO)";
		String pcDesconto 		 = valor_faixa + " - (((TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) + " + vlEmbute +") * (FAIXA.PC_DESCONTO/100))";
		String pcDescSemEmbute 	 = vl_faixaSemEmbute + " - ((TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) * (FAIXA.PC_DESCONTO/100))";
		
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
					decode.append("((TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) + " + vlEmbute +") * "+ desc +", (" + pcDesconto + ") *  " + desc + ")");
				}
			} else if ("A".equals(localVariables[6])) {
				acres = localVariables[1];
				BigDecimal acresimo = new BigDecimal(acres);
				acresimo = acresimo.divide(new BigDecimal("100"));
				acres = acresimo.add(new BigDecimal(1)).toString();
				//Aplica o desconto contido em PC_DESCONTO caso exista e o acrescimo em vlPercMinimoProgr 
				decode.append("((TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) + " + vlEmbute +") * "+ acres +", (" + pcDesconto + ") *  " + acres + ")");
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
					valor_Adicional  = valor_Adicional + " * " + desc;
					decode.append("(TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) * "+ desc +", (" + pcDescSemEmbute + ") *  " + desc + "))");
				}
			} else if ("A".equals(localVariables[4])) {
				acres = localVariables[0];
				BigDecimal acresimo = new BigDecimal(acres);
				acresimo = acresimo.divide(new BigDecimal("100"));
				acres = acresimo.add(new BigDecimal(1)).toString();
				//Aplica o desconto contido em PC_DESCONTO caso exista e o acrescimo em vlFretePeso 
				decode.append("(TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO) * "+ acres +", (" + pcDescSemEmbute + ") *  " + acres + "))");
				valor_Adicional  = valor_Adicional + " * " + acres;
				} else {
					decode.append(vl_faixaSemEmbute + " ,  "+ pcDescSemEmbute + "))");
			}
		} else {
			decode.append(vl_faixaSemEmbute + " ,  "+ pcDescSemEmbute + "))");
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
		.append(" 		FAIXA.FATOR_MULTIPLICACAO,  ")
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
		.append(" 				AND  T.ID_TABELA_PRECO_PARCELA = F.ID_TABELA_PRECO_PARCELA");
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
	
	  private Long getMaxFaixaProgressiva(Long idTabelaPreco, boolean emitirTonelada, JdbcTemplate jdbcTemplate)
	    {
	    	String sql = "select max(FAIXA_PROGRESSIVA) from " + getSubQueryFaixa(emitirTonelada);
	    	return jdbcTemplate.queryForLong(sql, new Long[]{idTabelaPreco});
	    }
	  
	  /**
	     * Retorna sql da subquery de faixas do relatorio
	     * @return
	     */
	    private String getSubQueryFaixa(boolean emitirTonelada)
	    {
	    	StringBuffer sql = new StringBuffer();
			sql.append("(select  F.VL_FAIXA_PROGRESSIVA AS FAIXA_PROGRESSIVA, ") 
			.append(" vfp.NR_FATOR_MULTIPLICACAO AS FATOR_MULTIPLICACAO, ")
			.append(" vfp.PC_DESCONTO, ")
			.append(" TAB.PS_MINIMO AS PS_MINIMO,  ")
			.append(" MO.DS_SIMBOLO AS DS_SIMBOLO ")
			.append(" from    TABELA_PRECO TAB, "  )
			.append(" TABELA_PRECO_PARCELA T, " )
			.append(" PARCELA_PRECO P, ")
			.append(" FAIXA_PROGRESSIVA F,  ")
			.append(" VALOR_FAIXA_PROGRESSIVA VFP, ")
			.append(" MOEDA MO ")
			.append(" where T.ID_TABELA_PRECO = ? ")
			.append(" AND  TAB.ID_TABELA_PRECO = T.ID_TABELA_PRECO  ")
			.append(" AND  T.ID_PARCELA_PRECO = P.ID_PARCELA_PRECO ")
			.append(" AND  P.TP_PARCELA_PRECO = 'P'  ")
			.append(" AND  P.TP_PRECIFICACAO = 'M' ")
			.append(" AND  VFP.ID_FAIXA_PROGRESSIVA = F.ID_FAIXA_PROGRESSIVA ")
			.append(" AND TAB.ID_MOEDA = MO.ID_MOEDA ");
			
			if(!emitirTonelada)
			{
				sql.append(" AND F.VL_FAIXA_PROGRESSIVA <= TAB.PS_MINIMO");
			}
				
			sql.append(" AND  T.ID_TABELA_PRECO_PARCELA = F.ID_TABELA_PRECO_PARCELA) FAIXA ");
			
			return sql.toString();
	    }
    
    
    
    /**
     * Metodo chamado dentro do Jasper, para formatar as colunas dinamicas do relatorio
     * @param parametro
     * @param dsSimbolo
     * @param psMinimo
     * @return
     */
    public String formataColumnParameter(String parametro, String dsSimbolo, BigDecimal psMinimo)
    {
    	return getTabelasClienteService().formataColumnParameter(parametro, dsSimbolo, psMinimo);
    }
    
	
	public String getRelatorio_et() {
		Map dadosClasseMap = (Map)dadosClasseThread.get();
		return (String)dadosClasseMap.get("relatorio_et");
	}

	public void setRelatorio_et(String relatorio_et) {
		Map dadosClasseMap = (Map)dadosClasseThread.get();
		dadosClasseMap.put("relatorio_et",relatorio_et);
		dadosClasseThread.set(dadosClasseMap);
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setEmitirTabelasClienteDAO(EmitirTabelasClienteDAO emitirTabelasClienteDAO) {
		this.emitirTabelasClienteDAO = emitirTabelasClienteDAO;
	}
	
	public EmitirTabelasClienteDAO getEmitirTabelasClienteDAO() {
		return emitirTabelasClienteDAO;
}
	
	public EmitirTabelasMinimoProgressivoService getTabelaMinimoProgressivo() {
		return tabelaMinimoProgressivo;
	}

	public void setTabelaMinimoProgressivo(EmitirTabelasMinimoProgressivoService tabelaMinimoProgressivo) {
		this.tabelaMinimoProgressivo = tabelaMinimoProgressivo;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
}
	
	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}
}
