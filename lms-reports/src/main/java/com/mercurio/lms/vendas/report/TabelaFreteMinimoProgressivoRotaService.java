package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.dto.ParametroRotaDTO;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * ET 30.03.02.20
 * 
 * @author Rodrigo F. Dias
 *
 * @spring.bean id="lms.vendas.report.tabelaFreteMinimoProgressivoRotaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteMinimoProgressivoRota.vm"
 
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="250"
 * @spring.property name="crossTabBandWidths" value="411" 
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class TabelaFreteMinimoProgressivoRotaService extends ReportServiceSupport {

	private static final int FAIXA_PROGRESSIVA_200 = 200;

	private static final String NOME_TABELA = "TMP_MN_PROGS_ROTA";

	Set crosstab;
	private BigDecimal psMinimo;
	private ConfiguracoesFacade configuracoesFacade;
	private TabelaPrecoService tabelaPrecoService;
	private Map mapDespacho;
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	private EmitirTabelasClientesService emitirTabelasClientesService;
	private TabelasClienteService tabelasClienteService;

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}
   
	
	public EmitirTabelasClientesService getEmitirTabelasClientesService() {
		return emitirTabelasClientesService;
	}

	public List<Map<String, String>> findDados(TypedFlatMap parameters) {
		
		Simulacao simulacao  = (Simulacao)MapUtils.getObject(parameters, "simulacao");
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		TypedFlatMap parametros = new TypedFlatMap();
		
		List listaParametros = new ArrayList();
		boolean primeiro = true;
		Long idTabelaDivisao = null;
		
		if(simulacao == null){
			List<Map> data = getEmitirTabelasClienteDAO().findFreteMinimoProgressivoRota(parameters);
	
			if(data == null || data.isEmpty()){
				return null;
			}
			
			for (Map map : data) {
				if(primeiro) {
					idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
					parametros = getCommonParameter(map);
					parametros.put("idTabelaDivisao", idTabelaDivisao);
					parametros.put("pagaFreteTonelada", MapUtilsPlus.getBoolean(map,"pagaFreteTonelada"));
					listaParametros.add(MapUtilsPlus.getLong(map,"listaParametros",null));
					parametros.put("listaParametros", listaParametros );
					parameters.put("idCliente", MapUtils.getLong(map,"idCliente"));
					parameters.put("idDivisao", MapUtils.getLong(map,"idDivisao"));
					parameters.put("idServico", MapUtils.getLong(map,"idServico"));
					parameters.put("idTabelaPreco", MapUtils.getLong(map,"idTabelaPreco"));
					primeiro = false;
				} else {
					if(!idTabelaDivisao.equals(MapUtils.getLong(map,"idTabelaDivisao"))) {
						parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
	//					mrc.addCommand("lms.vendas.report.tabelaFreteMinimoProgressivoRotaService", parametros);//adiciona anterior
	
						idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
						parametros = getCommonParameter(map);
						parametros.put("idTabelaDivisao", idTabelaDivisao);
						parametros.put("pagaFreteTonelada", MapUtilsPlus.getBoolean(map,"pagaFreteTonelada"));
						listaParametros = new ArrayList();
						listaParametros.add(MapUtilsPlus.getLong(map,"listaParametros",null));
						parametros.put("listaParametros", listaParametros );
					} else {
						idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
						listaParametros.add(MapUtilsPlus.getLong(map,"listaParametros",null));
						parametros.put("listaParametros", listaParametros );
					}
				}
			}
		}else{
			parametros = getCommonParameter(parameters);
			parametros.putAll(parameters);
			idTabelaDivisao = MapUtils.getLong(parameters,"idTabelaDivisao");
			parametros.put("idTabelaDivisao", idTabelaDivisao);
			parametros.put("pagaFreteTonelada", MapUtilsPlus.getBoolean(parameters,"pagaFreteTonelada"));
		
			primeiro = false;
		}
		
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		jdbcTemplate.execute("DELETE FROM "+ NOME_TABELA);

		//Parametros recebidos da Action
		Long idCliente = MapUtils.getLong(parametros,"idCliente");
		Long idDivisao = MapUtils.getLong(parametros,"idDivisao");
		Long idTabelaDivisaoCliente = MapUtils.getLong(parametros,"idTabelaDivisao");
		Long idTabelaPreco = MapUtils.getLong(parametros,"idTabelaPreco");
		Boolean isTabelaNova = MapUtils.getBoolean(parameters, "isTabelaNova", false);
		Long idSimulacao = MapUtils.getLong(parametros,"idSimulacao");
		List parametrosList = MapUtilsPlus.getList(parametros,"listaParametros",null);
		Boolean pagaFreteTonelada = MapUtils.getBoolean(parametros, "pagaFreteTonelada", Boolean.FALSE);
		mapDespacho = new HashMap();
		
		if(idTabelaPreco == null){
			return null ;
		}
		//Agrupa os dados 
        Map agrup = new HashMap();
        agrup = montaAgrupamento(
        	idCliente,
        	idDivisao,
        	idTabelaDivisaoCliente,
        	idSimulacao,
        	parametrosList,
        	idTabelaPreco);

		 /* Obtém os dados do relatório principal */
        Long[] listParamLong = null;
        boolean isSimulacao = true;
        if(idSimulacao==null){
        	isSimulacao = false;
        	listParamLong = new Long[]{idCliente,idTabelaDivisaoCliente};
        }
        //Acha os dados do cabeçalho
        Map parametersReport = new HashMap();
        parametersReport.put("idCliente",idCliente);
        parametersReport.put("idContato", MapUtils.getLong(parameters,"idContato"));
        parametersReport.put("idServico", MapUtils.getLong(parameters,"idServico"));
        
        
        Long idParametro = null;
        if(parameters.containsKey("listaParametros") && 
        		(parameters.get("listaParametros") instanceof List && CollectionUtils.isNotEmpty((List) parameters.get("listaParametros")))){
        	List<Long> params = MapUtilsPlus.getList(parameters, "listaParametros", ListUtils.EMPTY_LIST);
        	idParametro = params.get(0);
        }
        
        
        emitirTabelasClientesService.montaParametrosRodoviario(
        		idTabelaPreco,
        		isTabelaNova,
        		idCliente,
        		idDivisao,
        		idParametro,
        		idSimulacao,
        		parametersReport,
        		agrup,
            	getJdbcTemplate(),
            	NOME_TABELA,
            	configuracoesFacade
            );
        List parametrosCliente = jdbcTemplate.queryForList(
        	createQuery(
        		parametrosList,
        		Boolean.TRUE.equals(pagaFreteTonelada),
        		isSimulacao,
        		idTabelaPreco,
        		isTabelaNova,
        		idDivisao
        	),listParamLong);
        
		populateTable(
				parametrosCliente,
				idTabelaPreco,
				isTabelaNova,
				jdbcTemplate,
				agrup);

		if(simulacao==null){
			parametersReport.put("SERVICO", getTabelasClienteService().getTipoServico(idTabelaDivisaoCliente,jdbcTemplate));
		}else{
			Servico servico = simulacao.getServico();
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(servico.getIdServico(), getJdbcTemplate());
			parametersReport.put("SERVICO", MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO"));
		}

		for (Object object : parametrosCliente) {
			Map map = (Map)object;
            if (MapUtilsPlus.getBigDecimal(map, "FAIXA_PROGRESSIVA").intValue() < FAIXA_PROGRESSIVA_200) {
                map.put("VL_FRETE_PESO", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map, "VL_FRETE_PESO")));
            } else {
                map.put("VL_FRETE_PESO", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map, "VL_FRETE_PESO")));
            }
			map.put("VL_FAIXA_PROGRESSIVA", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VL_FAIXA_PROGRESSIVA")));
	    	map.put("VL_ADVALOREM", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VL_ADVALOREM")));
		}
		

		getTabelasClienteService().montaLogoMercurio(parametersReport,jdbcTemplate);
		int totRegistros = jdbcTemplate.queryForInt("Select COUNT(*) from " + NOME_TABELA +" ");
		int size = agrup.size();
		if(size == 0) size = 1;
		parametersReport.put("TOTAL", Integer.valueOf(totRegistros/size));

        //Parameters from database
        String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco,jdbcTemplate);
		parametersReport.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{Integer.valueOf(crosstab.size())});	
        parametersReport.put("DS_SIMBOLO",dsSimbolo);
		parametersReport.put("PSMINIMO", psMinimo);
		parametersReport.put("SERVICE", this);
		parameters.put("grupos", size);
		getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);

     	parameters.put(idTabelaPreco, parametersReport);
     	
     	ordenaParametroRotaFaixaProgressiva(parameters, parametrosCliente);
     	
     	result.addAll(parametrosCliente);
     	result.add(parametersReport);
     	result.add(parameters);
     	
     	
     	return result;
	}


	public void ordenaParametroRotaFaixaProgressiva(Map parametros, List<ListOrderedMap> dados) {
		final Map<OrdenaFaixasDTO, ListOrderedMap> mapDados = new HashMap<OrdenaFaixasDTO, ListOrderedMap>();
		Map<Long,List<OrdenaFaixasDTO>> mapParametros = new HashMap<Long,List<OrdenaFaixasDTO>>();
		
		for (ListOrderedMap map : dados) {
			OrdenaFaixasDTO faixa = new OrdenaFaixasDTO();
			faixa.setFaixaProgressiva((BigDecimal) map.get("FAIXA_PROGRESSIVA"));
			faixa.setIdParametrosCliente((BigDecimal) map.get("ID_PARAMETRO_CLIENTE"));
			
			long key = faixa.getIdParametrosCliente().longValue();
			
			List<OrdenaFaixasDTO> faixas = null;
			if(!mapParametros.containsKey(key)){
				faixas = new ArrayList<OrdenaFaixasDTO>();
			} else {
				faixas = (List<OrdenaFaixasDTO>) mapParametros.get(key);
			}
			
			faixas.add(faixa);

			mapParametros.put(key,faixas);
			mapDados.put(faixa, map);
		}
	
		// Busca blocos de dados para ordenamento de parâmetros do cliente
		Long idCliente = MapUtils.getLong(parametros, "idCliente");
		List<Long> ids = new ArrayList<Long>();
		ids.addAll(mapParametros.keySet());
		
		List<ParametroRotaDTO> listParametros = emitirTabelasClienteDAO.findParametroClienteRota(idCliente, ids,      	
		     	"tpIndicadorAdvalorem",        
		     	"tpIndicadorFretePeso",       
		     	"tpIndicadorMinFretePeso",
		     	"tpIndicadorPercMinimoProgr",
		     	"vlAdvalorem",                  
		     	"vlFretePeso",                 
		     	"vlMinFretePeso",             
		     	"vlMinimoFretePercentual",
		     	"vlMinimoFreteQuilo",         
		     	"vlPercMinimoProgr" );
		
		// Antes do ordenamento transfere identificação de grupos
		for (ParametroRotaDTO parametroClienteRotaDTO : listParametros) {
			List<OrdenaFaixasDTO> faixas = mapParametros.get(parametroClienteRotaDTO.getIdParametroReferencia());
			ListOrderedMap listOrderedMap = mapDados.get(faixas.get(0));
			String grupo = (String) listOrderedMap.get("GRUPO");
			parametroClienteRotaDTO.setGrupo(grupo);
		}
		
		Collections.sort(listParametros);

		listParametros = tabelasClienteService.removeRotasDuplicadas(parametros, listParametros);
		
		dados.clear();
		for (ParametroRotaDTO parametroClienteRotaDTO : listParametros) {
			List<OrdenaFaixasDTO> faixas = mapParametros.get(parametroClienteRotaDTO.getIdParametroReferencia());
			Collections.sort(faixas);
			
			for (OrdenaFaixasDTO ordenaFaixasDTO : faixas) {
				ListOrderedMap listOrderedMap = mapDados.get(ordenaFaixasDTO);
				dados.add(listOrderedMap);
			}
		}
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
	 * Monta tabela temporaria
	 * @param parametrosCliente
	 * @param idTabelaPreco
	 * @param isTabelaNova
	 * @param jdbcTemplate
	 * @param agrup
	 */
	private void populateTable(List<Map> parametrosCliente, Long idTabelaPreco, Boolean isTabelaNova, JdbcTemplate jdbcTemplate, Map agrup) {
		List listaUFOrigem = new LinkedList();
		List listaUFDestino = new LinkedList();
		List sqlRegistros = new LinkedList();
		List sqlValues = new LinkedList();
		int tamanho = agrup.size();
		StringBuffer colunas = null;
		StringBuffer valuesStr = null;
		List values = new LinkedList();
		crosstab = new LinkedHashSet();
		Map parametrosControl = new HashMap();

		String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
		String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");

		/** Verifica se Parametros devem ser reajustados pela Tabela Nova */
	    BigDecimal pcReajuste = BigDecimal.ZERO;
	    if(Boolean.TRUE.equals(isTabelaNova) && !parametrosCliente.isEmpty()) {
	    	TabelaPreco tabelaPreco = tabelaPrecoService.findByIdTabelaPreco(idTabelaPreco);
	    	pcReajuste = tabelaPreco.getPcReajuste();
	    }

		int columnIndex = 1;
		for (Iterator it = parametrosCliente.iterator(); it.hasNext(); columnIndex++) {
			Map parametroMapped = (Map) it.next();
			/** Reajusta Parametros pela tabela nova caso informada */
	    	getTabelasClienteService().reajustarParametros(parametroMapped, pcReajuste);

			Long idParametro = MapUtils.getLong(parametroMapped, "ID_PARAMETRO_CLIENTE");
			BigDecimal vlFaixaProgressiva = MapUtilsPlus.getBigDecimal(parametroMapped, "VL_FAIXA_PROGRESSIVA");
			BigDecimal vlAdVAlorem = MapUtilsPlus.getBigDecimal(parametroMapped, "VL_ADVALOREM");
			BigDecimal vlFretePeso = MapUtilsPlus.getBigDecimal(parametroMapped, "VL_FRETE_PESO");
			BigDecimal nrFatorMultiplicacao = MapUtilsPlus.getBigDecimal(parametroMapped, "NR_FATOR_MULTIPLICACAO");
			BigDecimal vlFaixaCustom = vlFretePeso.multiply(nrFatorMultiplicacao);
			BigDecimal pcDesconto = MapUtilsPlus.getBigDecimal(parametroMapped, "PC_DESCONTO");
			psMinimo = MapUtilsPlus.getBigDecimal(parametroMapped, "PS_MINIMO");
			
			if(CompareUtils.gt(vlFaixaProgressiva, BigDecimalUtils.getBigDecimal(1000))) {
				if(BigDecimalUtils.hasValue(pcDesconto)) {
					vlFaixaCustom = BigDecimalUtils.desconto(vlFaixaCustom, pcDesconto);
				}
			}
			if(!mapDespacho.isEmpty()) {
				BigDecimal valorDespacho = (BigDecimal) mapDespacho.get(idParametro);
				if(BigDecimalUtils.hasValue(valorDespacho)){
					vlFaixaCustom = BigDecimalUtils.add(vlFaixaCustom, valorDespacho);
				}
			}

			

			
			
			

			/** Novo Parametro */
				columnIndex = 1;
				String columnGroup = getTabelasClienteService().getGrupo(idParametro, agrup, tamanho);

				/**
				 * Concatena o uf da origem e do destino com "-" para ser usado
				 * no metodo verificaComplemento
				 */
				String ufOrigem = MapUtils.getString(parametroMapped, "UF_ORIGEM");
				String ufDestino = MapUtils.getString(parametroMapped, "UF_DESTINO");
				String ufOrigemDestino = ufOrigem + "-" + ufDestino;
				String complementoOrigem = getTabelasClienteService().verificaComplemento(listaUFOrigem, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, MapUtils.getString(parametroMapped, "ORIGEM"));
				String complementoDestino = getTabelasClienteService().verificaComplemento(listaUFDestino, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, MapUtils.getString(parametroMapped, "DESTINO"));
				String origem = complementoOrigem != null ? ufOrigem + " - " + complementoOrigem : ufOrigem;
				String destino = complementoDestino != null ? ufDestino + " - " + complementoDestino : ufDestino;

				parametroMapped.put("origem", origem);
				parametroMapped.put("destino", destino);
				parametroMapped.put("grupo", columnGroup);
				parametroMapped.put("VL_FAIXA_PROGRESSIVA", vlFaixaCustom);
				parametroMapped.put("FAIXA_PROGRESSIVA", vlFaixaProgressiva);
				
				if (psMinimo.longValue() < vlFaixaProgressiva.longValue()) {
					continue;
				}
				if (colunas != null) {
					colunas.append(")");
				}
				if (valuesStr != null) {
					valuesStr.append(")");
					sqlRegistros.add(colunas.toString() + valuesStr.toString());
					sqlValues.add(values.toArray());
				}

				colunas = new StringBuffer("INSERT INTO " + NOME_TABELA + " (ORIGEM, DESTINO, ADVALOREM, GRUPO, VL_ACIMA");
				valuesStr = new StringBuffer(" VALUES(?, ?, ?, ?, ?");
				values = new LinkedList();
				values.add(origem);
				values.add(destino);
				values.add(vlAdVAlorem);
				values.add(columnGroup);
				if (vlFretePeso != null) {
					values.add(vlFretePeso);
				}
				parametrosControl.put(idParametro, new ArrayList());

			crosstab.add(vlFaixaProgressiva);
			colunas.append(", COLUMN" + columnIndex);
			valuesStr.append(", ?");
			values.add(vlFaixaCustom);//TODO AQUI ADICIONA O VALOR
			
		}

		getTabelasClienteService().verificaUfs(listaUFOrigem, "ORIGEM", NOME_TABELA, msgTodoEstado, msgDemaisLocalidades, jdbcTemplate);
		getTabelasClienteService().verificaUfs(listaUFDestino, "DESTINO", NOME_TABELA, msgTodoEstado, msgDemaisLocalidades, jdbcTemplate);
	}

	/**
	 * 
	 * @param idCliente
	 * @param idTabelaPreco
	 * @return
	 */
	private Map montaAgrupamento(Long idCliente,Long idDivisao,Long idTabelaDivisao,Long idSimulacao,List parametros, Long idTabelaPreco) {
		Map agrup = new HashMap<String, String>();
		
		//monta um map com as generalidades de cada parametro
		String sqlGrupo = null;
		List grupos = null;
		
		if(idSimulacao==null){
			sqlGrupo = montaSqlGrupos(parametros, false);
			grupos = getJdbcTemplate().queryForList(sqlGrupo, new Long[]{idCliente,idDivisao,idTabelaDivisao});
		}else{
			sqlGrupo = montaSqlGrupos(parametros, true);
			grupos = getJdbcTemplate().queryForList(sqlGrupo);
		}
		
		
/*		if(grupos==null){
			sqlGrupo = montaSqlGrupos();
			grupos = getJdbcTemplate().queryForList(sqlGrupo,new Object[]{idCliente,idTabelaDivisao,idTabelaPreco});
		}*/
		
		
        for (Iterator iter = grupos.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			Long idParametro = Long.valueOf(map.get("ID_PARAMETRO_CLIENTE").toString());
			String valor = map.get("BRAKEGROUP").toString();
			if(!agrup.containsKey(idParametro)){
				agrup.put(idParametro,valor);
				
			}else{
				String valores = valor + agrup.get(idParametro).toString();
			    agrup.put(idParametro,valores);
			}
        }
        int i = 0;
        Map mapa = new HashMap();
        List todasChaves = new ArrayList();
        // le o map de generalidades de parametros e agrupa os que tiverem os mesmos valores
        for (Iterator iter = agrup.keySet().iterator(); iter.hasNext();) {
			Long chave = (Long) iter.next();
			String value = agrup.get(chave).toString();
			// apenas faz a verificação em registros que ainda nao tenham sido verificados
			if(todasChaves.indexOf(chave) == -1){
				if(agrup.containsValue(value)){
					List listaChaves = new ArrayList();
					for (Iterator it = agrup.keySet().iterator(); it.hasNext();) {
						Long key = (Long) it.next();
			        	String v = agrup.get(key).toString();
			        	if(value.equals(v)){
			        		listaChaves.add(key);
			        		todasChaves.add(key);
			        	}
					}
					iter.remove();
					mapa.put("grupo"+i,listaChaves);
					i++;
				}
			}
		}
        return mapa;
    }
	
	private String montaSqlGrupos() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct PC.ID_PARAMETRO_CLIENTE,")
			.append("(G.VL_GENERALIDADE ||")
			.append(" G.TP_INDICADOR || PC.TP_INDICADOR_PERCENTUAL_GRIS || PC.VL_PERCENTUAL_GRIS ")
			.append(" || PC.TP_INDICADOR_MINIMO_GRIS || PC.VL_MINIMO_GRIS || PC.PC_COBRANCA_DEVOLUCOES ")
			.append(" || PC.PC_COBRANCA_REENTREGA || PC.VL_MIN_FRETE_PESO || PC.TP_INDICADOR_PEDAGIO || PC.VL_PEDAGIO) as BRAKEGROUP ") 
			.append(" FROM 	PARAMETRO_CLIENTE PC, ")
			.append("		DIVISAO_CLIENTE DC, ")
			.append("		GENERALIDADE_CLIENTE G,")
			.append("		TABELA_DIVISAO_CLIENTE TDC, ")
			.append("		UNIDADE_FEDERATIVA UF_ORIGEM, ")
			.append("		UNIDADE_FEDERATIVA UF_DESTINO ")
			.append("WHERE")
			.append("	DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE")
			.append("	AND TDC.ID_TABELA_DIVISAO_CLIENTE = PC.ID_TABELA_DIVISAO_CLIENTE")
			.append("	AND PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA")
			.append("	AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
			.append("	AND PC.ID_PARAMETRO_CLIENTE = G.ID_PARAMETRO_CLIENTE (+)")
			.append("	AND DC.ID_CLIENTE = ?")
			.append("	AND DC.ID_DIVISAO_CLIENTE = ?")
			.append("	AND TDC.ID_TABELA_DIVISAO_CLIENTE = ?")
			.append("ORDER BY PC.ID_PARAMETRO_CLIENTE, BRAKEGROUP ");
		return sql.toString();
	}

	/**
	 * Monta sql para retornar dados da consulta do relat&oacute;rio
	 * @return
	 */
	private String createQuery(List parametros, boolean pagaTonelada, boolean isSimulacao, Long idTabelaPreco, Boolean isTabelaNova, Long idDivisao){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT ")
			.append(" 		VFP.VL_FIXO, ")
			.append("       PC.ID_PARAMETRO_CLIENTE, ")
			.append("		PC.TP_INDICADOR_FRETE_PESO, ")
			.append("		PC.VL_FRETE_PESO, ")
			.append(" 		TP.PS_MINIMO, ")
			.append(" 		FP.VL_FAIXA_PROGRESSIVA, ")
			.append(" 		VFP.PC_DESCONTO, ")
			.append(" 		VFP.NR_FATOR_MULTIPLICACAO, ")
			.append(" 		PC.VL_ADVALOREM, ")
			.append(" 		UF_ORIGEM.SG_UNIDADE_FEDERATIVA as UF_ORIGEM, ")
			.append(" 		UF_DESTINO.SG_UNIDADE_FEDERATIVA as UF_DESTINO, ")
			
	 		.append(" decode(PC.ID_MUNICIPIO_ORIGEM,null, ")
	 		.append(" decode(F_ORIGEM.SG_FILIAL,null, ")
	 		.append(" decode(").append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",null, GRUPO_ORIGEM.DS_GRUPO_REGIAO, ").append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append( " ) , ")
	 		.append(" decode(po_filial.nm_fantasia,null,F_ORIGEM.SG_FILIAL,F_ORIGEM.SG_FILIAL || ' - ' || po_filial.nm_fantasia)), ")
	 		.append(" M_ORIGEM.NM_MUNICIPIO) as ORIGEM, ")
			 
	 		.append(" decode(PC.ID_MUNICIPIO_DESTINO,null, ")
	 		.append(" decode(F_DESTINO.SG_FILIAL,null, ")
	 		.append(" decode(").append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",null, GRUPO_DESTINO.DS_GRUPO_REGIAO, ").append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append( " ) , ")
	 		.append(" decode(pd_filial.nm_fantasia,null,F_DESTINO.SG_FILIAL,F_DESTINO.SG_FILIAL || ' - ' || pd_filial.nm_fantasia)), ")
	 		.append(" M_DESTINO.NM_MUNICIPIO) as DESTINO ")
			
			.append(" FROM ");
		if(!isSimulacao) {
			sql.append("	CLIENTE C, ")
			   .append("	DIVISAO_CLIENTE DC, ")
			   .append("	TABELA_DIVISAO_CLIENTE TDC, ");
		}
		sql.append(" 		TABELA_PRECO TP, ")
			.append(" 		TIPO_TABELA_PRECO TTP, ")
			.append(" 		SUBTIPO_TABELA_PRECO STP, ")
			.append(" 		TABELA_PRECO_PARCELA TPP, ")
			.append(" 		PARCELA_PRECO PP, ")
			.append(" 		FAIXA_PROGRESSIVA FP, ")
			.append(" 		VALOR_FAIXA_PROGRESSIVA VFP, ")
			.append(" 		PARAMETRO_CLIENTE PC, ")
			.append(" 		UNIDADE_FEDERATIVA UF_ORIGEM, ")
			.append(" 		UNIDADE_FEDERATIVA UF_DESTINO, ")
			.append(" 		MUNICIPIO M_ORIGEM, ")
			.append(" 		MUNICIPIO M_DESTINO, ")
			.append(" 		TIPO_LOCALIZACAO_MUNICIPIO TLM_ORIGEM, ")
			.append(" 		TIPO_LOCALIZACAO_MUNICIPIO TLM_DESTINO, ")
			.append(" 		FILIAL F_ORIGEM, ")
			.append(" 		FILIAL F_DESTINO, ")
			.append(" 		PESSOA PO_FILIAL, ")
			.append(" 		PESSOA PD_FILIAL, ")
			.append(" 		GRUPO_REGIAO GRUPO_ORIGEM, ")
			.append(" 		GRUPO_REGIAO GRUPO_DESTINO, ")
			.append(" 		SERVICO S ")
			.append("WHERE");
        if(!isSimulacao) {
       	 	sql.append("	C.ID_CLIENTE = ? ");
       	 	/** Divisão do Cliente */
       	 	if(idDivisao==null){
       	 		sql.append("	AND DC.ID_DIVISAO_CLIENTE IS NOT NULL ");
       	 	}else{
       	 		sql.append("	AND DC.ID_DIVISAO_CLIENTE = ").append(idDivisao).append("");
       	 	}
       	 	sql.append("	AND TDC.ID_TABELA_DIVISAO_CLIENTE = ? ")
       	 		.append("	AND TP.ID_TABELA_PRECO = TDC.ID_TABELA_PRECO ")
				.append("	AND C.ID_CLIENTE = DC.ID_CLIENTE ")
				.append("	AND DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE ")
				.append("	AND TDC.ID_TABELA_DIVISAO_CLIENTE = PC.ID_TABELA_DIVISAO_CLIENTE ")
				.append("	AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO ");
		} else {
			 sql.append("	PC.ID_SIMULACAO IS NOT NULL ")
				.append("	AND TP.ID_TABELA_PRECO = PC.ID_TABELA_PRECO ");
		}
        if(!pagaTonelada) {
			sql.append("	AND FP.VL_FAIXA_PROGRESSIVA <= TP.PS_MINIMO ");
		}
        if(!Boolean.TRUE.equals(isTabelaNova)) {
        	sql.append("	AND TP.ID_TABELA_PRECO = ").append(idTabelaPreco);
        }
		sql.append("	AND TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO ")
		    .append("	AND TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO ")
			.append("	AND TP.ID_TABELA_PRECO = TPP.ID_TABELA_PRECO ")
			.append("	AND TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
			.append("	AND TTP.ID_SERVICO = S.ID_SERVICO ")
			.append("	AND S.TP_MODAL = 'R' AND S.TP_ABRANGENCIA = 'N' ")
			.append("	AND PP.CD_PARCELA_PRECO = '").append(ConstantesExpedicao.CD_FRETE_PESO).append("' ")
			.append("	AND TPP.ID_TABELA_PRECO_PARCELA = FP.ID_TABELA_PRECO_PARCELA ")
			.append("	AND FP.ID_FAIXA_PROGRESSIVA = VFP.ID_FAIXA_PROGRESSIVA ")
			.append("	AND PC.TP_INDICADOR_ADVALOREM = 'V'  ")
			.append("	AND PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA ")
			.append("	AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA ")
			.append("	AND PC.ID_MUNICIPIO_ORIGEM = M_ORIGEM.ID_MUNICIPIO (+) ")
			.append("	AND PC.ID_MUNICIPIO_DESTINO = M_DESTINO.ID_MUNICIPIO (+) ")
			.append("	AND PC.ID_FILIAL_ORIGEM = F_ORIGEM.ID_FILIAL (+) ")
			.append("	AND PC.ID_FILIAL_DESTINO = F_DESTINO.ID_FILIAL (+) ")
			.append("	AND PC.ID_FILIAL_ORIGEM = PO_FILIAL.ID_PESSOA (+) ")
			.append("	AND PC.ID_FILIAL_DESTINO = PD_FILIAL.ID_PESSOA (+) ")
			.append("	AND PC.ID_TIPO_LOC_MUNICIPIO_ORIGEM = TLM_ORIGEM.ID_TIPO_LOCALIZACAO_MUNICIPIO (+) ")
			.append("	AND PC.ID_TIPO_LOC_MUNICIPIO_DESTINO = TLM_DESTINO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+) ")
			.append("	AND PC.ID_GRUPO_REGIAO_ORIGEM  = GRUPO_ORIGEM.ID_GRUPO_REGIAO  (+) ")
			.append("	AND PC.ID_GRUPO_REGIAO_DESTINO = GRUPO_DESTINO.ID_GRUPO_REGIAO (+) ")
			.append("	AND VFP.NR_FATOR_MULTIPLICACAO IS NOT NULL ")
			.append("	AND PC.ID_PARAMETRO_CLIENTE IN ( ");

		/** Adiciona IDs Parametros do Cliente à consulta */
		sql.append(" SELECT * FROM (");
		
		boolean primeiro = true;
		for (Iterator iter = parametros.iterator(); iter.hasNext();) {
			Long pc = (Long) iter.next();
			if(primeiro) {
				sql.append(" SELECT ");
				sql.append(pc);
				sql.append(" FROM DUAL ");
				primeiro = false;
			} else {
				sql.append("UNION ALL SELECT ");
				sql.append(pc);
				sql.append(" FROM DUAL ");
			}
		}
		
		sql.append(") ) ");
		sql.append("ORDER BY PC.ID_PARAMETRO_CLIENTE,  UF_ORIGEM.SG_UNIDADE_FEDERATIVA, UF_DESTINO.SG_UNIDADE_FEDERATIVA, FP.VL_FAIXA_PROGRESSIVA");
	 	return sql.toString();
	}
	
	private String montaSqlGrupos(List parametros, boolean isSimulacao){
		StringBuffer sql = new StringBuffer();

		if(isSimulacao){
			sql.append("SELECT distinct PC.ID_PARAMETRO_CLIENTE,")
				.append("(G.VL_GENERALIDADE ||")
				.append(" G.TP_INDICADOR || PC.TP_INDICADOR_PERCENTUAL_GRIS || PC.VL_PERCENTUAL_GRIS ")
				.append(" || PC.TP_INDICADOR_MINIMO_GRIS || PC.VL_MINIMO_GRIS || PC.PC_COBRANCA_DEVOLUCOES ")
				.append(" || PC.PC_COBRANCA_REENTREGA || PC.VL_MIN_FRETE_PESO || PC.TP_INDICADOR_PEDAGIO || PC.VL_PEDAGIO) as BRAKEGROUP ") 
				.append(" FROM 	PARAMETRO_CLIENTE PC, ")
				.append("		GENERALIDADE_CLIENTE G,")
				.append("		UNIDADE_FEDERATIVA UF_ORIGEM, ")
				.append("		UNIDADE_FEDERATIVA UF_DESTINO ")
				.append("WHERE")
				.append("	PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA")
				.append("	AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
				.append("	AND PC.ID_PARAMETRO_CLIENTE = G.ID_PARAMETRO_CLIENTE (+)")
			    .append("   AND PC.ID_PARAMETRO_CLIENTE IN ( ");			 
		 }else{
			sql.append("SELECT distinct PC.ID_PARAMETRO_CLIENTE,")
				.append("(G.VL_GENERALIDADE ||")
				.append(" G.TP_INDICADOR || PC.TP_INDICADOR_PERCENTUAL_GRIS || PC.VL_PERCENTUAL_GRIS ")
				.append(" || PC.TP_INDICADOR_MINIMO_GRIS || PC.VL_MINIMO_GRIS || PC.PC_COBRANCA_DEVOLUCOES ")
				.append(" || PC.PC_COBRANCA_REENTREGA || PC.VL_MIN_FRETE_PESO || PC.TP_INDICADOR_PEDAGIO || PC.VL_PEDAGIO) as BRAKEGROUP ") 
				.append(" FROM 	PARAMETRO_CLIENTE PC, ")
				.append("		DIVISAO_CLIENTE DC, ")
				.append("		GENERALIDADE_CLIENTE G,")
				.append("		TABELA_DIVISAO_CLIENTE TDC, ")
				.append("		UNIDADE_FEDERATIVA UF_ORIGEM, ")
				.append("		UNIDADE_FEDERATIVA UF_DESTINO ")
				.append("WHERE")
				.append("	DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE")
				.append("	AND TDC.ID_TABELA_DIVISAO_CLIENTE = PC.ID_TABELA_DIVISAO_CLIENTE")
				.append("	AND PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA")
				.append("	AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
				.append("	AND PC.ID_PARAMETRO_CLIENTE = G.ID_PARAMETRO_CLIENTE (+)")
				.append("	AND DC.ID_CLIENTE = ?")
				.append("	AND DC.ID_DIVISAO_CLIENTE = ?")
				.append("	AND TDC.ID_TABELA_DIVISAO_CLIENTE = ?")
			    .append("   AND PC.ID_PARAMETRO_CLIENTE IN ( ");
		 }
		/** Adiciona IDs Parametros do Cliente à consulta */
		sql.append(" SELECT * FROM (");
		
		 boolean primeiro = true;
		for (Iterator iter = parametros.iterator(); iter.hasNext();) {
			Long pc = (Long) iter.next();
			if(primeiro) {
				sql.append(" SELECT ");
				sql.append(pc);
				sql.append(" FROM DUAL ");
				primeiro = false;
			} else {
				sql.append("UNION ALL SELECT ");
				sql.append(pc);
				sql.append(" FROM DUAL ");
			}
		}
		
		sql.append(") ) ");
		sql.append("ORDER BY PC.ID_PARAMETRO_CLIENTE, BRAKEGROUP ");
		return sql.toString();
	}

	private class OrdenaFaixasDTO implements Comparable<OrdenaFaixasDTO> {
		BigDecimal idParametrosCliente;
		BigDecimal faixaProgressiva;
		
		public BigDecimal getIdParametrosCliente() {
			return idParametrosCliente;
		}
		public void setIdParametrosCliente(BigDecimal idParametrosCliente) {
			this.idParametrosCliente = idParametrosCliente;
		}
		public BigDecimal getFaixaProgressiva() {
			return faixaProgressiva;
		}
		public void setFaixaProgressiva(BigDecimal faixaProgressiva) {
			this.faixaProgressiva = faixaProgressiva;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((faixaProgressiva == null) ? 0 : faixaProgressiva.hashCode());
			result = prime * result + ((idParametrosCliente == null) ? 0 : idParametrosCliente.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			OrdenaFaixasDTO other = (OrdenaFaixasDTO) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (faixaProgressiva == null) {
				if (other.faixaProgressiva != null)
					return false;
			} else if (!faixaProgressiva.equals(other.faixaProgressiva))
				return false;
			if (idParametrosCliente == null) {
				if (other.idParametrosCliente != null)
					return false;
			} else if (!idParametrosCliente.equals(other.idParametrosCliente))
				return false;
			return true;
		}
		
		private TabelaFreteMinimoProgressivoRotaService getOuterType() {
			return TabelaFreteMinimoProgressivoRotaService.this;
		}
		
		@Override
		public int compareTo(OrdenaFaixasDTO o) {
			return this.faixaProgressiva.compareTo(o.faixaProgressiva);
		}
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public void setEmitirTabelasClienteDAO(EmitirTabelasClienteDAO emitirTabelasClienteDAO) {
		this.emitirTabelasClienteDAO = emitirTabelasClienteDAO;
	}

	public EmitirTabelasClienteDAO getEmitirTabelasClienteDAO() {
		return emitirTabelasClienteDAO;
	}
	
	public void setEmitirTabelasClientesService(
			EmitirTabelasClientesService emitirTabelasClientesService) {
		this.emitirTabelasClientesService = emitirTabelasClientesService;
	}

	public JRReportDataObject execute(Map parameters) throws Exception {
		return null;
	}
}