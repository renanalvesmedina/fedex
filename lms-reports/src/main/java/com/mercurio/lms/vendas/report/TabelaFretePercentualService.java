package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * @author Alexandre Poletto 
 * ET: 30.03.02.17 - Tabela Frete Percentual
 *  
 * @spring.bean id="lms.vendas.report.tabelaFretePercentualService"
 * 
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFretePercentual.jasper"
 */
public class TabelaFretePercentualService extends ReportServiceSupport {

	private static final String NOME_TABELA = "TMP_TABELA_FRETE_PERCENTUAL";

    private ConfiguracoesFacade configuracoesFacade;
	private TabelaPrecoService tabelaPrecoService;
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	private EmitirTabelasClientesService emitirTabelasClientesService;
	private TabelasClienteService tabelasClienteService;

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}
   
	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}


   	@Override
   	public JRReportDataObject execute(Map parameters) throws Exception {
   		return null;
	}
		
	public List<Map<String, String>> findDados(TypedFlatMap parameters) throws Exception {

		Simulacao simulacao  = (Simulacao)MapUtils.getObject(parameters, "simulacao");
		List result = new ArrayList();
		List parametros = new ArrayList();
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		if(simulacao == null){
			List<Map> data = getEmitirTabelasClienteDAO().findRelatorioFretePercentual(parameters, "R");
			parameters.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
				
			if(data == null || data.isEmpty() ){
				return null;
			}
			
		//limpa a tabela temporararia
		jdbcTemplate.execute("DELETE FROM "+ NOME_TABELA);

			for (Map map : data) {
				parameters.put("pagaFreteTonelada", MapUtilsPlus.getBoolean(map,"pagaFreteTonelada"));
				parameters.put("idTabelaDivisao", MapUtils.getLong(map,"idTabelaDivisao"));
				parameters.put("parametroCliente", MapUtils.getLong(map,"listaparametros"));
				parameters.put("vlFretePeso", MapUtilsPlus.getBigDecimal(map,"vlFretePeso",null));
				parameters.put("vlPercMinimoProgr", MapUtilsPlus.getBigDecimal(map,"vlPercMinimoProgr",null));
				parameters.put("vlAdvalorem", MapUtilsPlus.getBigDecimal(map,"vlAdvalorem",null));
				parameters.put("tpIndicadorMinFretePeso", MapUtils.getString(map,"tpIndicadorMinFretePeso"));
				parameters.put("tpIndicadorPercMinimoProgr", MapUtils.getString(map,"tpIndicadorPercMinimoProgr"));
				parameters.put("tpIndicadorAdvalorem", MapUtils.getString(map,"tpIndicadorAdvalorem"));
				parameters.put("tpIndicadorFretePeso", MapUtils.getString(map,"tpIndicadorFretePeso"));
				parameters.put("idCliente", MapUtils.getLong(map,"idCliente"));
				parameters.put("idDivisao", MapUtils.getLong(map,"idDivisao"));
				parameters.put("idTabelaPreco", MapUtils.getLong(map,"idTabelaPreco"));
				parameters.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
				
				Long parametro = MapUtilsPlus.getLong(map,"listaParametros",null);
				if(parametros.contains(parametro)){
					continue;
				}
				if(parametros.size() == 999){
					break;
				}
				parametros.add(parametro);
				parameters.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
			}
		}else{
			List<ParametroCliente> parametrosCliente =  (List<ParametroCliente>) parameters.get("parametroCliente");
			for(ParametroCliente pc : parametrosCliente) {
				parametros.add(pc.getIdParametroCliente());
			}
		}
		//Parametros recebidos da Action
		Long idCliente = MapUtils.getLong(parameters,"idCliente");
		Long idDivisao = MapUtils.getLong(parameters,"idDivisao");
		Long idTabelaDivisao = MapUtils.getLong(parameters,"idTabelaDivisao");
		Long idSimulacao = MapUtils.getLong(parameters, "idSimulacao");
		Long idTabelaPreco = MapUtils.getLong(parameters,"idTabelaPreco");
		Boolean isTabelaNova = MapUtils.getBoolean(parameters, "isTabelaNova",false);

		// Faz o relatorio retornar em branco caso ele nao contenha dados
		if(idTabelaDivisao == null && idSimulacao == null){
				return null;
		}

		// Agrupa os dados 
        Map agrup = new HashMap();
		agrup = montaAgrupamento(idCliente, idDivisao, idTabelaDivisao, idSimulacao, parametros, agrup);

        // Insere os dados na tabela temporaria
		populateTable(parametros, idDivisao, idCliente, idTabelaDivisao, idTabelaPreco, isTabelaNova, idSimulacao, agrup);

	    // Acha os dados do cabeçalho
		Map parametersReport = new HashMap();
		parametersReport.put("idCliente",idCliente);
		parametersReport.put("idContato", MapUtils.getLong(parameters,"idContato"));

		// Parametros
		Long idParametro = parametros.get(0) != null? (Long)parametros.get(0): null;
		emitirTabelasClientesService.montaParametrosRodoviario(idTabelaPreco, isTabelaNova, idCliente, idDivisao, idParametro, idSimulacao, parametersReport, agrup, jdbcTemplate, NOME_TABELA,
				configuracoesFacade);

		if(idSimulacao == null){
			parametersReport.put("SERVICO", getTabelasClienteService().getTipoServico(idTabelaDivisao,jdbcTemplate));
		}else{
			Servico servico = simulacao.getServico();
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(servico.getIdServico(), jdbcTemplate);
			parametersReport.put("SERVICO", MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO"));
		}

		getTabelasClienteService().montaLogoMercurio(parametersReport,jdbcTemplate);
		int totRegistros = jdbcTemplate.queryForInt("Select COUNT(*) from " + NOME_TABELA);
		int size = agrup.size();
		if (size == 0)
			size = 1;
		parametersReport.put("TOTAL", Integer.valueOf(totRegistros/size));
        parametersReport.put("idCliente",idCliente);
        parametersReport.put("idContato", MapUtilsPlus.getLong(parameters,"idContato",null));

		List<ListOrderedMap>  dados = jdbcTemplate.queryForList("SELECT DISTINCT * FROM " + NOME_TABELA + " Order by GRUPO , ORIGEM, DESTINO");

		    parameters.put("grupos", agrup.size());

		tabelasClienteService.ordenaParametroClienteRota(parameters,dados,"pcFretePercentual","vlMinimoFretePercentual","psFretePercentual","vlToneladaFretePercentual");
		    
		    for (Map map : dados) {
		    	map.put("PERCVALORNF", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"PERCVALORNF")));
		    	map.put("FRETECTRC", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"FRETECTRC")));
		    	map.put("FRETETONEL", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"FRETETONEL")));
		    	map.put("PESOMIN", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"PESOMIN")));
		}

		result.add(parametersReport);
		    result.addAll(dados);
		
		return result;
		}

		
	/**
	 * Popula a tabela temporaria com dados
	 * @param parametros
	 * @param idDivisao
	 * @param idCliente
	 * @param idTabelaDivisaoCliente
	 * @param isTabelaNova
	 * @param idSimulacao
	 * @param agrup
	 */
	private void populateTable(List parametros, Long idDivisao, Long idCliente, Long idTabelaDivisaoCliente, Long idTabelaPreco, Boolean isTabelaNova, Long idSimulacao, Map agrup) {
		String sql = createQuery(parametros, idSimulacao, idDivisao);
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		int tamanho = agrup.size();
		List listaUFOrigem = new ArrayList();
		List listaUFDestino = new ArrayList();
		List<Map> parametrosCliente = null;

		// Se não é proposta
		if(idSimulacao==null){
			Long[] params = new Long[LongUtils.hasValue(idDivisao) ? 3 : 2];
			params[0] = idCliente;
			params[1] = idTabelaDivisaoCliente;
			if(LongUtils.hasValue(idDivisao)){
				params[2] = idDivisao;
			}
			parametrosCliente = getJdbcTemplate().queryForList(sql, params);
		}else{
			parametrosCliente = getJdbcTemplate().queryForList(sql);
		}

		/** Busca registros */
	    String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
		String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");

		/** Verifica se Parametros devem ser reajustados pela Tabela Nova */
	    BigDecimal pcReajuste = BigDecimal.ZERO;
	    if(Boolean.TRUE.equals(isTabelaNova) && !parametrosCliente.isEmpty()) {
	    	TabelaPreco tabelaPreco = tabelaPrecoService.findByIdTabelaPreco(idTabelaPreco);
	    	pcReajuste = tabelaPreco.getPcReajuste();
	    }

	    String anteriorOrigem = "";
	    String anteriorDestino = "";
	    /** Inclui parametros na tabela temporaria do Relatório */
	    for (Map parametroMapped : parametrosCliente) {
	    	/** Reajusta Parametros pela tabela nova caso informada */
	    	getTabelasClienteService().reajustarParametros(parametroMapped, pcReajuste);

			Long idParametro = MapUtils.getLong(parametroMapped,"ID_PARAMETRO_CLIENTE");
			String columnGroup = getTabelasClienteService().getGrupo(idParametro,agrup,tamanho);

			/**
			 * Concatena o uf da origem e do destino com "-" para ser usado
			 * no metodo verificaComplemento
			 */
			String ufOrigem = MapUtils.getString(parametroMapped,"UF_ORIGEM");
			String ufDestino = MapUtils.getString(parametroMapped,"UF_DESTINO");
			String ufOrigemDestino = ufOrigem+"-"+ufDestino;
	        String complementoOrigem = getTabelasClienteService().verificaComplemento(listaUFOrigem, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, MapUtils.getString(parametroMapped,"ORIGEM"));
			String complementoDestino = getTabelasClienteService().verificaComplemento(listaUFDestino, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, MapUtils.getString(parametroMapped,"DESTINO"));
			String origem = complementoOrigem != null ? ufOrigem + " - " + complementoOrigem : ufOrigem;
			String destino = complementoDestino != null ? ufDestino + " - " + complementoDestino : ufDestino;

			if(anteriorOrigem.equals(origem) && anteriorDestino.equals(destino)){
				continue;
			}
			
			
			BigDecimal pcFretePercentual = MapUtilsPlus.getBigDecimal(parametroMapped,"PC_FRETE_PERCENTUAL");
			BigDecimal vlMinimoFretePercentual = MapUtilsPlus.getBigDecimal(parametroMapped,"VL_MINIMO_FRETE_PERCENTUAL");
			BigDecimal psFretePercentual = MapUtilsPlus.getBigDecimal(parametroMapped,"PS_FRETE_PERCENTUAL");
			BigDecimal vlToneladaFretePercentual = MapUtilsPlus.getBigDecimal(parametroMapped,"VL_TONELADA_FRETE_PERCENTUAL");

			/** Insert */
			StringBuilder query = new StringBuilder();
			query.append("INSERT INTO ").append(NOME_TABELA).append(" (ID_PARAMETRO_CLIENTE, PERCVALORNF, FRETECTRC, PESOMIN, FRETETONEL,");
			query.append(" ORIGEM, DESTINO, GRUPO) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
			jdbcTemplate.update(query.toString(), new Object[]{idParametro, pcFretePercentual, vlMinimoFretePercentual, psFretePercentual, vlToneladaFretePercentual, origem, destino, columnGroup});
			anteriorOrigem = origem;
			anteriorDestino = destino;
		}

	    getTabelasClienteService().verificaUfs(listaUFOrigem,"ORIGEM",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,jdbcTemplate);
	    getTabelasClienteService().verificaUfs(listaUFDestino,"DESTINO",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,jdbcTemplate);
	}

	private Map montaAgrupamento(Long idCliente, Long idDivisao, Long idTabelaDivisao, Long idSimulacao, List parametros, Map agrup) {
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

	private String createQuery(List parametros, Long idSimulacao, Long idDivisao){
		 StringBuilder sql = new StringBuilder();
		 sql.append("SELECT DISTINCT ")
		 	.append("	  PC.ID_PARAMETRO_CLIENTE,");
		 if(idSimulacao==null) {
			 sql.append("		TP.ID_TABELA_PRECO,");
		 }

		 sql.append("	  	UF_ORIGEM.SG_UNIDADE_FEDERATIVA as UF_ORIGEM,")
			 .append("     	UF_DESTINO.SG_UNIDADE_FEDERATIVA as UF_DESTINO,")
			 .append("     	PC.PC_FRETE_PERCENTUAL,")
			 .append("     	PC.VL_MINIMO_FRETE_PERCENTUAL,")
			 .append("     	PC.PS_FRETE_PERCENTUAL,")
			 .append("     	PC.VL_TONELADA_FRETE_PERCENTUAL,")
			 
	 		.append(" decode(PC.ID_MUNICIPIO_ORIGEM,null, ")
	 		.append(" decode(F_ORIGEM.SG_FILIAL,null, ")
	 		.append(" decode(").append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",null, GRUPO_ORIGEM.DS_GRUPO_REGIAO, ").append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append( " ) , ")
	 		.append(" decode(po_filial.nm_fantasia,null,F_ORIGEM.SG_FILIAL,F_ORIGEM.SG_FILIAL || ' - ' || po_filial.nm_fantasia)), ")
	 		.append(" M_ORIGEM.NM_MUNICIPIO) as ORIGEM, ")
			 
	 		.append(" decode(PC.ID_MUNICIPIO_DESTINO,null, ")
	 		.append(" decode(F_DESTINO.SG_FILIAL,null, ")
	 		.append(" decode(").append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",null, GRUPO_DESTINO.DS_GRUPO_REGIAO, ").append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append( " ) , ")
	 		.append(" decode(pd_filial.nm_fantasia,null,F_DESTINO.SG_FILIAL,F_DESTINO.SG_FILIAL || ' - ' || pd_filial.nm_fantasia)), ")
	 		.append(" M_DESTINO.NM_MUNICIPIO) as DESTINO, ")	 		

			 .append("		GRUPO_ORIGEM.DS_GRUPO_REGIAO  as GRUPO_REGIAO_ORIGEM, ")
			 .append("		GRUPO_DESTINO.DS_GRUPO_REGIAO as GRUPO_REGIAO_DESTINO ")

			 .append(" FROM ");
		 if(idSimulacao==null) {
			 sql.append(" 	  CLIENTE C, ")
			 	.append("     DIVISAO_CLIENTE DC, ")
			 	.append("     TABELA_DIVISAO_CLIENTE TDC, ");
		 }

		 sql.append("	TABELA_PRECO TP, ")
			 .append("     PARAMETRO_CLIENTE PC, ")
			 .append("     MUNICIPIO M_Origem, ")
			 .append("     MUNICIPIO M_Destino, ")
			 .append("     UNIDADE_FEDERATIVA UF_ORIGEM, ")
			 .append("     UNIDADE_FEDERATIVA UF_DESTINO, ")
			 .append("     FILIAL F_ORIGEM, ")
			 .append("     FILIAL F_DESTINO, ")
			 .append(" 	   PESSOA po_filial, ")
			 .append(" 	   PESSOA pd_filial, ")		 
			 .append("     TIPO_LOCALIZACAO_MUNICIPIO TLM_ORIGEM, ")
			 .append("     TIPO_LOCALIZACAO_MUNICIPIO TLM_DESTINO, ")
			 .append("     GRUPO_REGIAO GRUPO_ORIGEM, ")
			 .append("     GRUPO_REGIAO GRUPO_DESTINO ");

		sql.append(" WHERE PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA")
			 .append("     AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
			 .append("     AND PC.ID_MUNICIPIO_ORIGEM = M_ORIGEM.ID_MUNICIPIO (+)")
			 .append("     AND PC.ID_MUNICIPIO_DESTINO = M_DESTINO.ID_MUNICIPIO (+)")
			 .append("     AND PC.ID_FILIAL_ORIGEM = F_ORIGEM.ID_FILIAL (+)")
		     .append("     AND PC.ID_FILIAL_DESTINO = F_DESTINO.ID_FILIAL (+)")
			 .append(" 	   AND PC.ID_FILIAL_ORIGEM = po_FILIAL.ID_Pessoa (+)")
			 .append(" 	   AND PC.ID_FILIAL_DESTINO = pd_FILIAL.ID_Pessoa (+)")	     
			 .append("     AND PC.ID_TIPO_LOC_MUNICIPIO_ORIGEM = TLM_ORIGEM.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)")
			 .append("     AND PC.ID_TIPO_LOC_MUNICIPIO_DESTINO = TLM_DESTINO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)")
			 .append("     AND PC.ID_GRUPO_REGIAO_ORIGEM  = GRUPO_ORIGEM.ID_GRUPO_REGIAO  (+)")
			 .append("     AND PC.ID_GRUPO_REGIAO_DESTINO = GRUPO_DESTINO.ID_GRUPO_REGIAO (+)");
		 if(idSimulacao==null){
			 sql.append("     AND C.ID_CLIENTE = DC.ID_CLIENTE\n")
			 	.append("     AND DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE")
				.append("     AND TDC.ID_TABELA_DIVISAO_CLIENTE = PC.ID_TABELA_DIVISAO_CLIENTE")
				.append("     AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO")
				.append("     AND C.ID_CLIENTE = ? ")
			 	.append("     AND TDC.ID_TABELA_DIVISAO_CLIENTE = ?");
			 if(idDivisao!=null) {
				 sql.append("     AND DC.ID_DIVISAO_CLIENTE = ? ");
			 }
		 } else {
			 sql.append("     AND PC.ID_SIMULACAO = ").append(idSimulacao.longValue());
		 }

		 sql.append("     AND PC.ID_PARAMETRO_CLIENTE IN ( ");
		 boolean primeiro = true;
		 for (Iterator iter = parametros.iterator(); iter.hasNext();) {
			Long pc = (Long) iter.next();
			if(primeiro){
			   sql.append(pc);
			   primeiro = false;
			}else{
				sql.append(", " + pc);	
			}
		}
		sql.append(") ")
			.append(" ORDER BY UF_ORIGEM.SG_UNIDADE_FEDERATIVA, UF_DESTINO.SG_UNIDADE_FEDERATIVA , PC.VL_MINIMO_FRETE_PERCENTUAL");

	    return sql.toString();
	 }

	 private String montaSqlGrupos(List parametros, boolean isSimulacao){
		 StringBuffer sql = new StringBuffer();
		 
		 if(isSimulacao){
				sql.append("SELECT PC.ID_PARAMETRO_CLIENTE,")
				.append("(G.VL_GENERALIDADE ||")
				.append(" G.TP_INDICADOR || PC.TP_INDICADOR_PERCENTUAL_GRIS || PC.VL_PERCENTUAL_GRIS ")
				.append(" || PC.TP_INDICADOR_MINIMO_GRIS || PC.VL_MINIMO_GRIS || PC.PC_COBRANCA_DEVOLUCOES ")
				.append(" || PC.PC_COBRANCA_REENTREGA || PC.VL_MIN_FRETE_PESO || PC.TP_INDICADOR_PEDAGIO || PC.VL_PEDAGIO) ")
				.append(" || PC.TP_INDICADOR_PERCENTUAL_TRT || PC.VL_PERCENTUAL_TRT || PC.TP_INDICADOR_MINIMO_TRT || PC.VL_MINIMO_TRT || count(G.VL_GENERALIDADE) as BRAKEGROUP ") 				
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
			sql.append("SELECT PC.ID_PARAMETRO_CLIENTE,")
			.append("(G.VL_GENERALIDADE ||")
			.append(" G.TP_INDICADOR || PC.TP_INDICADOR_PERCENTUAL_GRIS || PC.VL_PERCENTUAL_GRIS ")
			.append(" || PC.TP_INDICADOR_MINIMO_GRIS || PC.VL_MINIMO_GRIS || PC.PC_COBRANCA_DEVOLUCOES ")
			.append(" || PC.PC_COBRANCA_REENTREGA || PC.VL_MIN_FRETE_PESO || PC.TP_INDICADOR_PEDAGIO || PC.VL_PEDAGIO) ") 
			.append(" || PC.TP_INDICADOR_PERCENTUAL_TRT || PC.VL_PERCENTUAL_TRT || PC.TP_INDICADOR_MINIMO_TRT || PC.VL_MINIMO_TRT || count(G.VL_GENERALIDADE) as BRAKEGROUP ") 
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

		sql.append(" group by PC.ID_PARAMETRO_CLIENTE, (G.VL_GENERALIDADE ")
		.append(" || G.TP_INDICADOR ")
		.append(" || PC.TP_INDICADOR_PERCENTUAL_GRIS ")
		.append(" || PC.VL_PERCENTUAL_GRIS ")
		.append(" || PC.TP_INDICADOR_MINIMO_GRIS ")
		.append(" || PC.VL_MINIMO_GRIS ")
		.append(" || PC.PC_COBRANCA_DEVOLUCOES ")
		.append(" || PC.PC_COBRANCA_REENTREGA ")
		.append(" || PC.VL_MIN_FRETE_PESO ")
		.append(" || PC.TP_INDICADOR_PEDAGIO ")
		.append(" || PC.VL_PEDAGIO) ")
		.append(" || PC.TP_INDICADOR_PERCENTUAL_TRT ")
		.append(" || PC.VL_PERCENTUAL_TRT ")
		.append(" || PC.TP_INDICADOR_MINIMO_TRT ")
		.append(" || PC.VL_MINIMO_TRT ")

		.append("ORDER BY PC.ID_PARAMETRO_CLIENTE, BRAKEGROUP ");
		 return sql.toString();
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
	
	public EmitirTabelasClientesService getEmitirTabelasClientesService() {
		return emitirTabelasClientesService;
	}

	public void setEmitirTabelasClientesService(
			EmitirTabelasClientesService emitirTabelasClientesService) {
		this.emitirTabelasClientesService = emitirTabelasClientesService;
	}
}