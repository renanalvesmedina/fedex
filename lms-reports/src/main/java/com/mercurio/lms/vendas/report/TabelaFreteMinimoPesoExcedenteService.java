package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * @spring.bean id="lms.vendas.tabelaFreteMinimoPesoExcedenteService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteMinimoPesoExcedente.jasper"
 * 
 * ET: 30.03.02.19 - Tabela Frete Minimo Por Peso Excedente 
 */
public class TabelaFreteMinimoPesoExcedenteService extends ReportServiceSupport {

	public static final String NOME_TABELA = "TMP_FRETE_MIN_VALOR";	

    private ConfiguracoesFacade configuracoesFacade;
    private DomainValueService domainValueService; 
    private TabelaPrecoService tabelaPrecoService;
    private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
    private EmitirTabelasClientesService emitirTabelasClientesService;
	private TabelasClienteService tabelasClienteService;

	public JRReportDataObject execute(Map parameters) throws Exception {	
		return null;
	}
	
	public List<Map<String, String>> findDados(TypedFlatMap parameters) {
		Simulacao simulacao  = (Simulacao)MapUtils.getObject(parameters, "simulacao");
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		TypedFlatMap parametros = new TypedFlatMap();
		
		if(simulacao == null){
			List<Map> data = getEmitirTabelasClienteDAO().findRelatorioFreteMinimoPesoExcedente(parameters);
			List listaParametros = new ArrayList();
			
			if(data == null || data.isEmpty()){
				return null;
			}
			if (MapUtils.getLong(data.get(0), "idTabelaPreco") == null) {
				return null;
			}
			for (Map map : data) {
				if(listaParametros.contains(MapUtils.getLong(map, "LISTAPARAMETROS"))){
					continue;
				}
				listaParametros.add(MapUtils.getLong(map, "LISTAPARAMETROS"));
			}
			parametros.put("listaParametros", listaParametros);
			
			parametros.put("idTabelaPreco",MapUtils.getLong(data.get(0), "idTabelaPreco"));// 9
			parametros.put("isTabelaNova",MapUtils.getBoolean(data.get(0), "isTabelaNova", false));
			parametros.put("idDivisao",MapUtils.getLong(data.get(0), "idDivisao"));// 107
			parametros.put("idSimulacao",MapUtils.getLong(data.get(0), "idSimulacao"));
			parametros.put("idCliente",MapUtils.getLong(data.get(0), "idCliente"));
			parametros.put("simulacao",(Simulacao) MapUtils.getObject(data.get(0),"simulacao"));
			parametros.put("idTabelaDivisao", MapUtils.getLong(data.get(0),"idTabelaDivisao"));
			parametros.put("idServico", MapUtils.getLong(data.get(0),"idServico"));
		}else{
			parametros.put("idTabelaPreco",MapUtils.getLong(parameters, "idTabelaPreco"));// 9
			parametros.put("isTabelaNova",MapUtils.getBoolean(parameters, "isTabelaNova", false));
			parametros.put("idDivisao",MapUtils.getLong(parameters, "idDivisao"));// 107
			parametros.put("idSimulacao",MapUtils.getLong(parameters, "idSimulacao"));
			parametros.put("idCliente",MapUtils.getLong(parameters, "idCliente"));
			parametros.put("simulacao",(Simulacao) MapUtils.getObject(parameters,"simulacao"));
			parametros.put("listaParametros", MapUtilsPlus.getList(parameters,"listaParametros",null));
			parametros.put("idServico", MapUtils.getLong(parameters,"idServico"));
		}
		
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		jdbcTemplate.execute("DELETE FROM " + NOME_TABELA);
		
		Map parametersReport = new HashMap();
		
		
		parametersReport = AplicaParametrosEPopulaTabela(parametros,"S");
		parameters.put("grupos", parametros.get("grupos"));
		getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);

		List dados = jdbcTemplate.queryForList("SELECT * FROM "+ NOME_TABELA + " Order by GRUPO, VLMINFRETEPESO");		
		
		// LMS-4526
		tabelasClienteService.ordenaParametroClienteRota(parametros, dados, "vlMinFretePeso", "vlFretePeso", "vlAdvalorem");
		parameters.put("mapCifFob", parametros.get("mapCifFob"));
		
		for (Object object : dados) {
			Map map = (Map) object;
			map.put("VLADVALOREM", FormatUtils.formatDecimal(getTabelasClienteService().FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VLADVALOREM")));
			map.put("VLMINIMOFRETEQUILO", FormatUtils.formatDecimal(getTabelasClienteService().FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VLMINIMOFRETEQUILO")));
			map.put("VLFRETEPESO", FormatUtils.formatDecimal(getTabelasClienteService().FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map,"VLFRETEPESO")));
	}
	
		result.addAll(dados);
		result.add(parametersReport);
		result.add(parameters);
		return result;
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

	public Map AplicaParametrosEPopulaTabela(Map parameters,String blPagaPesoExcedente)	{
		Long idCliente = MapUtilsPlus.getLong(parameters,"idCliente",null);
		Long idDivisao = MapUtilsPlus.getLong(parameters,"idDivisao",null);
		List parametros = MapUtilsPlus.getList(parameters,"listaParametros",null);
		Long idTabelaPreco = MapUtilsPlus.getLong(parameters,"idTabelaPreco",null);
		Boolean isTabelaNova = MapUtils.getBoolean(parameters, "isTabelaNova", false);
		Long idTabelaDivisao = MapUtilsPlus.getLong(parameters,"idTabelaDivisao",null);
		Long idContato = MapUtilsPlus.getLong(parameters,"idContato",null);
		Long idSimulacao = MapUtilsPlus.getLong(parameters,"idSimulacao",null);
		Long idServico = MapUtilsPlus.getLong(parameters,"idServico",null);
		Simulacao simulacao = (Simulacao)MapUtils.getObject(parameters, "simulacao");

		Map parametersReport = new HashMap();		
        parametersReport.put("idCliente",idCliente);
        parametersReport.put("idServico",idServico);
        parametersReport.put("idContato", idContato);

		//Agrupa os dados
		Map agrup = new HashMap();
        agrup = montaAgrupamento(
        	idCliente,
        	idDivisao,
        	idTabelaDivisao,
        	parametros,
        	agrup,
        	idSimulacao
        );

        //insere os dados na tabela temporaria
        String dsSimbolo = "";
        if (parameters != null) {
        	dsSimbolo = populateTable(
        		parametros,
        		idDivisao,
        		idCliente,
        		idTabelaPreco,
        		isTabelaNova,
        		agrup,
        		blPagaPesoExcedente,
        		idSimulacao
        	);

        	/** MODAL/ABRANGENCIA/SERVICO */
            if(idSimulacao==null){
        		Map servico = getTabelasClienteService().getServicoTabela(idTabelaPreco, getJdbcTemplate());
                parametersReport.put("MODAL", domainValueService.findDomainValueDescription("DM_MODAL", MapUtils.getString(servico, "TP_MODAL")));
                parametersReport.put("ABRANGENCIA", domainValueService.findDomainValueDescription("DM_ABRANGENCIA", MapUtils.getString(servico, "TP_ABRANGENCIA")));
                parametersReport.put("SERVICO", getTabelasClienteService().getTipoServico(idTabelaDivisao, getJdbcTemplate()));
            }else{
    			Servico servico = simulacao.getServico();
    			parametersReport.put("MODAL", servico.getTpModal().getDescription().getValue());
                parametersReport.put("ABRANGENCIA", servico.getTpAbrangencia().getDescription().getValue());
                Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(servico.getIdServico(), getJdbcTemplate());
                parametersReport.put("SERVICO", MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO"));
            }
        }

        if(StringUtils.isBlank(dsSimbolo)) return new HashMap();

        Long idParametro = null;
        if(parameters.containsKey("listaParametros") && 
        		(parameters.get("listaParametros") instanceof List && CollectionUtils.isNotEmpty((List) parameters.get("listaParametros")))){
        	List<Long> params = MapUtilsPlus.getList(parameters, "listaParametros", ListUtils.EMPTY_LIST);
        	idParametro = params.get(0);
        }
        
        // monta os parameters.
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
        
        parameters.put("grupos", agrup.size());
        
        return parametersReport;
	}

		
	/**
	 * Popula a tabela temporaria com dados
	 * @param parametros
	 * @param idDivisao
	 * @param idCliente
	 * @param idTabelaPreco
	 * @param isTabelaNova
	 * @param agrup
	 * @param blPagaPesoExcedente
	 * @param idSimulacao
	 * @return
	 */
	private String populateTable(List parametros,Long idDivisao, Long idCliente, Long idTabelaPreco, Boolean isTabelaNova, Map agrup, String blPagaPesoExcedente, Long idSimulacao) {
		String dsSimbolo = "";

		/** Se existe parametros */
		if (parametros != null && parametros.size() > 0) {
			int tamanho = agrup.size();

			List listaUFOrigem = new ArrayList();
			List listaUFDestino = new ArrayList();

			String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
			String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");

			/** Ajusta Parametros de Filtro para Query */
			Object[] queryParams = null;
			Boolean consideraDivisaoCliente = Boolean.TRUE.equals(idDivisao != null);
			
		    if(idSimulacao==null) {
		    	if(Boolean.TRUE.equals(isTabelaNova)) {
		    		if (consideraDivisaoCliente) {
		    			queryParams = new Object[]{idCliente, idDivisao, blPagaPesoExcedente};
		    		}else {
		    			queryParams = new Object[]{idCliente, blPagaPesoExcedente};
		    		}
		    	} 
		    	else {
		    		if (consideraDivisaoCliente) {
		    			queryParams = new Object[]{idCliente, idTabelaPreco, idDivisao, blPagaPesoExcedente};
		    		}else {
		    			queryParams = new Object[]{idCliente, idTabelaPreco, blPagaPesoExcedente};
		    		}
		    	}
		    } else {
		    	if(Boolean.TRUE.equals(isTabelaNova)) {
		    		queryParams = new Object[]{blPagaPesoExcedente};
		    	} else queryParams = new Object[]{idTabelaPreco, blPagaPesoExcedente};
		    }

		    /** Busca registros */
		    List<Map> parametrosCliente = getJdbcTemplate().queryForList(createQuery(parametros, idSimulacao, isTabelaNova, consideraDivisaoCliente), queryParams);

		    /** Verifica se Parametros devem ser reajustados pela Tabela Nova */
		    BigDecimal pcReajuste = BigDecimal.ZERO;
		    if(Boolean.TRUE.equals(isTabelaNova) && !parametrosCliente.isEmpty()) {
		    	TabelaPreco tabelaPreco = tabelaPrecoService.findByIdTabelaPreco(idTabelaPreco);
		    	pcReajuste = tabelaPreco.getPcReajuste();
		    }

		    /** Inclui parametros na tabela temporaria do Relatório */
		    for (Map parametroMapped : parametrosCliente) {
		    	/** Reajusta Parametros pela tabela nova caso informada */
		    	getTabelasClienteService().reajustarParametros(parametroMapped, pcReajuste);

				Long idParametro = MapUtils.getLong(parametroMapped,"ID_PARAMETRO_CLIENTE");				
				String columnGroup = getTabelasClienteService().getGrupo(idParametro,agrup,tamanho);
				String dsMoeda = MapUtils.getString(parametroMapped,"DSMOEDA");
				dsSimbolo = MapUtils.getString(parametroMapped,"DS_SIMBOLO");

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

				BigDecimal vlAdValorem = MapUtilsPlus.getBigDecimal(parametroMapped,"VL_ADVALOREM");
				BigDecimal vlFretePeso = MapUtilsPlus.getBigDecimal(parametroMapped,"VL_FRETE_PESO");
				BigDecimal vlMinimoFreteQuilo = MapUtilsPlus.getBigDecimal(parametroMapped,"VL_MINIMO_FRETE_QUILO");
				BigDecimal vlMinFretePeso = MapUtilsPlus.getBigDecimal(parametroMapped,"VL_MIN_FRETE_PESO");

				/** Insert */
				StringBuilder query = new StringBuilder();
				query.append("INSERT INTO ").append(NOME_TABELA).append(" (BLPAGAPESOEXCEDENTE, ID_PARAMETRO_CLIENTE, VLADVALOREM, VLFRETEPESO, VLMINIMOFRETEQUILO, VLMINFRETEPESO,");
				query.append(" ORIGEM, DESTINO, DSMOEDA, GRUPO) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				getJdbcTemplate().update(query.toString(), new Object[]{blPagaPesoExcedente, idParametro, 
						vlAdValorem, vlFretePeso, vlMinimoFreteQuilo, vlMinFretePeso, origem, destino, dsMoeda, columnGroup});
			}

		    getTabelasClienteService().verificaUfs(listaUFOrigem,"ORIGEM",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,getJdbcTemplate());
		    getTabelasClienteService().verificaUfs(listaUFDestino,"DESTINO",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,getJdbcTemplate());
		}
		return dsSimbolo;
	}

	/**
	 * 
	 * @param idCliente
	 * @param agrup
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "null" })
	private Map montaAgrupamento(Long idCliente,Long idDivisao,Long idTabelaDivisao,List parametros,Map agrup, Long idSimulacao) {
		//monta um map com as generalidades de cada parametro
		String sqlGrupo = null;
		List grupos = new ArrayList();
		Long[] queryParams = null;
		
		Boolean consideraDivisaoCliente = Boolean.TRUE.equals(idDivisao != null);
		
		if (idSimulacao == null) {
			sqlGrupo = montaSqlGrupos(parametros, false, consideraDivisaoCliente);
			
			if (consideraDivisaoCliente) {
				queryParams = new Long[] { idCliente, idDivisao, idTabelaDivisao };
			} else {
				queryParams = new Long[] { idCliente, idTabelaDivisao };
			}
			
			grupos = getJdbcTemplate().queryForList(sqlGrupo, queryParams);
		} else {
			sqlGrupo = montaSqlGrupos(parametros, true, consideraDivisaoCliente);
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
	
	private String createQuery(List parametros, Long idSimulacao, Boolean isTabelaNova, Boolean consideraDivisaoCliente){
		StringBuilder sql = new StringBuilder()
		 	.append("SELECT DISTINCT ")
		 	.append("	PC.ID_PARAMETRO_CLIENTE,")
         	.append("	TP.ID_TABELA_PRECO,")
         	.append("	M.DS_SIMBOLO,")
         	.append("	M.DS_SIMBOLO as DSMOEDA,")
			.append("   UF_ORIGEM.SG_UNIDADE_FEDERATIVA as UF_ORIGEM,")
			.append("   UF_DESTINO.SG_UNIDADE_FEDERATIVA as UF_DESTINO,")
			.append("	DECODE(PC.VL_MINIMO_FRETE_QUILO,0,(pc.VL_MIN_FRETE_PESO * pc.VL_FRETE_PESO),PC.VL_MINIMO_FRETE_QUILO)as VL_MINIMO_FRETE_QUILO,")
			.append("   PC.TP_INDICADOR_FRETE_PESO,")
			.append("   PC.TP_INDICADOR_MIN_FRETE_PESO,")
			.append("   PC.VL_FRETE_PESO,")
			.append("   PC.VL_MIN_FRETE_PESO,") 
			.append("   PC.VL_ADVALOREM,") 
			
	 		.append(" decode(PC.ID_MUNICIPIO_ORIGEM,null, ")
	 		.append(" decode(F_ORIGEM.SG_FILIAL,null, ")
	 		.append(" decode(").append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",null, GRUPO_ORIGEM.DS_GRUPO_REGIAO, ").append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append( " ) , ")
	 		.append(" decode(po_filial.nm_fantasia,null,F_ORIGEM.SG_FILIAL,F_ORIGEM.SG_FILIAL || ' - ' || po_filial.nm_fantasia)), ")
	 		.append(" M_ORIGEM.NM_MUNICIPIO) as ORIGEM, ")
			
	 		.append(" decode(PC.ID_MUNICIPIO_DESTINO,null, ")
	 		.append(" decode(F_DESTINO.SG_FILIAL,null, ")
	 		.append(" decode(").append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",null, GRUPO_DESTINO.DS_GRUPO_REGIAO, ").append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append( " ) , ")
	 		.append(" decode(pd_filial.nm_fantasia,null,F_DESTINO.SG_FILIAL,F_DESTINO.SG_FILIAL || ' - ' || pd_filial.nm_fantasia)), ")
	 		.append(" M_DESTINO.NM_MUNICIPIO) as DESTINO ");
		
         sql.append("FROM ");
         if(idSimulacao == null) {
			sql.append("	CLIENTE C, ")
			   .append("	DIVISAO_CLIENTE DC, ")
			   .append("	TABELA_DIVISAO_CLIENTE TDC, ");
         }
         sql.append("	TABELA_PRECO TP, ")
			.append("	MOEDA M, ")
			.append("	PARAMETRO_CLIENTE PC, ")
			.append("	MUNICIPIO M_Origem, ")
			.append("	MUNICIPIO M_Destino, ")
			.append("	UNIDADE_FEDERATIVA UF_ORIGEM, ")
			.append("	UNIDADE_FEDERATIVA UF_DESTINO, ")
			.append("	FILIAL F_ORIGEM, ")
			.append("	FILIAL F_DESTINO, ")
			.append("	PESSOA po_filial, ")
			.append("	PESSOA pd_filial, ")			 
			.append("	TIPO_LOCALIZACAO_MUNICIPIO TLM_ORIGEM, ")
			.append("	TIPO_LOCALIZACAO_MUNICIPIO TLM_DESTINO, ")
			.append("	GRUPO_REGIAO GRUPO_ORIGEM, ")
			.append("	GRUPO_REGIAO GRUPO_DESTINO ")
			
			.append("WHERE");
         if(idSimulacao == null) {
        	 sql.append("	C.ID_CLIENTE = ? ");
        	 
        	 if(!Boolean.TRUE.equals(isTabelaNova)) {
				sql.append("	AND TP.ID_TABELA_PRECO = ?");
        	 }
        	 
        	 if(Boolean.TRUE.equals(consideraDivisaoCliente)) {
        		 sql.append("	AND DC.ID_DIVISAO_CLIENTE = ? ");	 
        	 }
			
			sql.append("	AND C.ID_CLIENTE = DC.ID_CLIENTE")
				.append("	AND DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE")
				.append("	AND TDC.ID_TABELA_DIVISAO_CLIENTE = PC.ID_TABELA_DIVISAO_CLIENTE")
				.append("	AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO");
		} else {
			sql.append("	PC.ID_SIMULACAO = ").append(idSimulacao.longValue())
       	 		.append("	AND TP.ID_TABELA_PRECO = PC.ID_TABELA_PRECO");
       		if(!Boolean.TRUE.equals(isTabelaNova)) {
				sql.append("	AND PC.ID_TABELA_PRECO = ?");
       	 	}
		}
		sql.append("	AND TP.ID_MOEDA = M.ID_MOEDA")
			.append("	AND PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA")
			.append("	AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
			.append("	AND PC.ID_MUNICIPIO_ORIGEM = M_ORIGEM.ID_MUNICIPIO (+)")
			.append("	AND PC.ID_MUNICIPIO_DESTINO = M_DESTINO.ID_MUNICIPIO (+)")
			.append("	AND PC.ID_FILIAL_ORIGEM = F_ORIGEM.ID_FILIAL (+)")
		    .append("	AND PC.ID_FILIAL_DESTINO = F_DESTINO.ID_FILIAL (+)")
			.append("	AND PC.ID_FILIAL_ORIGEM = po_FILIAL.ID_PESSOA (+)")
			.append("	AND PC.ID_FILIAL_DESTINO = pd_FILIAL.ID_PESSOA (+)")
			.append("	AND PC.ID_TIPO_LOC_MUNICIPIO_ORIGEM = TLM_ORIGEM.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)")
			.append("	AND PC.ID_TIPO_LOC_MUNICIPIO_DESTINO = TLM_DESTINO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)")
			.append("	AND PC.BL_PAGA_PESO_EXCEDENTE = ? ")
		 	.append("   AND PC.ID_GRUPO_REGIAO_ORIGEM  = GRUPO_ORIGEM.ID_GRUPO_REGIAO  (+)")
		 	.append("   AND PC.ID_GRUPO_REGIAO_DESTINO = GRUPO_DESTINO.ID_GRUPO_REGIAO (+)");

		/** Adiciona IDs Parametros do Cliente à consulta */
		StringBuilder queryParametros = new StringBuilder();
		for (Iterator iter = parametros.iterator(); iter.hasNext();) {
			Long idParametroCliente = (Long) iter.next();
			if(StringUtils.isBlank(queryParametros.toString())) {
				queryParametros.append(" AND (PC.ID_PARAMETRO_CLIENTE = " + idParametroCliente);
			} else {
				queryParametros.append(" OR PC.ID_PARAMETRO_CLIENTE = " + idParametroCliente);	
			}
		}
		sql.append(queryParametros.toString());
		sql.append(") ");
		sql.append(" ORDER BY PC.VL_MIN_FRETE_PESO, UF_ORIGEM.SG_UNIDADE_FEDERATIVA, UF_DESTINO.SG_UNIDADE_FEDERATIVA ");

	    return sql.toString();
	 }

	 private String montaSqlGrupos(List parametros, boolean isSimulacao, Boolean consideraDivisaoCliente){
		 StringBuffer sql = new StringBuffer();
		 
		 if(isSimulacao){
				sql.append("SELECT distinct PC.ID_PARAMETRO_CLIENTE,")
				.append("(G.VL_GENERALIDADE ||")
				.append(" G.TP_INDICADOR || PC.TP_INDICADOR_PERCENTUAL_GRIS || PC.VL_PERCENTUAL_GRIS ")
				.append(" || PC.TP_INDICADOR_MINIMO_GRIS || PC.VL_MINIMO_GRIS || PC.PC_COBRANCA_DEVOLUCOES ")
				.append(" || PC.PC_COBRANCA_REENTREGA || PC.VL_MIN_FRETE_PESO || PC.TP_INDICADOR_PEDAGIO || PC.VL_PEDAGIO") 
				.append(" || PC.TP_INDICADOR_PERCENTUAL_TRT || PC.VL_PERCENTUAL_TRT || PC.TP_INDICADOR_MINIMO_TRT || PC.VL_MINIMO_TRT) as BRAKEGROUP ") 
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
			.append(" || PC.PC_COBRANCA_REENTREGA || PC.VL_MIN_FRETE_PESO || PC.TP_INDICADOR_PEDAGIO || PC.VL_PEDAGIO") 
			.append(" || PC.TP_INDICADOR_PERCENTUAL_TRT || PC.VL_PERCENTUAL_TRT || PC.TP_INDICADOR_MINIMO_TRT || PC.VL_MINIMO_TRT) as BRAKEGROUP ") 
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
			.append("	AND DC.ID_CLIENTE = ?");
			
			if (consideraDivisaoCliente) {
				sql.append("	AND DC.ID_DIVISAO_CLIENTE = ?");	
			}
			
			sql.append("	AND TDC.ID_TABELA_DIVISAO_CLIENTE = ?")
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
		sql.append(" ORDER BY PC.ID_PARAMETRO_CLIENTE, BRAKEGROUP ");
		return sql.toString();
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
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

	public TabelaPrecoService getTabelaPrecoService() {
		return tabelaPrecoService;
	}

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
}

}
