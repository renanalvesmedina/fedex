package com.mercurio.lms.tabelaprecos.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;
import com.mercurio.lms.vendas.report.EmitirTabelaTaxaCombustivelLandscapeService;



/**
 * Rel 10
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.report.emitirTabelaFreteCiaAereaService"
 * @spring.property name="reportName" value="/com/mercurio/lms/tabelaprecos/report/EmitirTabelaFreteCiaAerea.vm"
 *
 * @spring.property name="numberOfCrosstabs" value="2"
 * @spring.property name="crossTabLefts" value="245,486"
 * @spring.property name="crossTabBandWidths" value="241,300"
 * @spring.property name="numbersOfCrossTabColumns" value="11,10"
 */	 
public class EmitirTabelaFreteCiaAereaService extends ReportServiceSupport {

	/* Nome da Tebela temporaria */
	public static final String NOME_TABELA = "TMP_CIAAEREAS";

	/**
	 * Para guardar o idTabelaPreco e o dsSimbolo e compartilhar entre metodos
	 */
	private static final ThreadLocal dadosClasseThread = new ThreadLocal();

	/**
	 * Configuracoes de mensagens
	 */
	private ConfiguracoesFacade configuracoesFacade;
	
	/**
	 * Para layout do relatorio Cia Aerea
	 */
	private EmitirTabelaTaxaCombustivelLandscapeService emitirTabelaTaxaCombustivel;
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	private TabelasClienteService tabelasClienteService;

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}	
	
    /**
     * Seta o atributo local identificado pela chave com o valor
     * 
     * @param key chave do atributo local
     * @param value valor do atributo local
     */
    private void setLocalVariableValue(Object key, Object value) {
    	Map map = (Map)dadosClasseThread.get();
    	if(map == null) map = new HashMap();

    	map.put(key, value);
    	dadosClasseThread.set(map);
    }

    /**
     * Retorna valor do atributo local identificado pela chave
     * 
     * @param key chave do atributo local
     * @return valor do atributo local
     */
    private Object getLocalVariableValue(Object key) {
    	Map map = (Map)dadosClasseThread.get();
    	if(map == null) return null;
    	return map.get(key);
    }

	public void setTipoLocalizacaoMunicipioService(
			TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
	}

	/* Inner Class utilizada para a definição de uma coluna */
	private class ColumnsDefinition	{
		private int totColumns;
		private Set columns = new LinkedHashSet();
		private String sqlNameColumns;
		private HashMap tabela;
		private int totVl;
		private int totTe;

		public ColumnsDefinition(){}

		public int getTotTe() {
			return totTe;
		}
		public void setTotTe(int totTe) {
			this.totTe = totTe;
		}
		public int getTotVl() {
			return totVl;
		}
		public void setTotVl(int totVl) {
			this.totVl = totVl;
		}
		public Set getColumns() {
			return columns;
		}
		public void setColumns(Set columns) {
			this.columns = columns;
		}
		public String getSqlNameColumns() {
			return sqlNameColumns;
		}
		public void setSqlNameColumns(String sqlNameColumns) {
			this.sqlNameColumns = sqlNameColumns;
		}
		public int getTotColumns() {
			return totColumns;
		}
		public void setTotColumns(int totColumns) {
			this.totColumns = totColumns;
		}
		public HashMap getTabela() {
			return tabela;
		}
		public void setTabela(HashMap tabela) {
			this.tabela = tabela;
		}		
	}

	/**
	 * 
	 * @return
	 */
	private SqlTemplate createDataQuery(Map parameters) {
		
		Long idTabelaPreco = MapUtils.getLong(parameters, "tipoTabelaPreco.idTabelaPreco");
		if (idTabelaPreco==null){
			idTabelaPreco = MapUtils.getLong(parameters, "idTabelaPreco");
		}if (idTabelaPreco == null){
			idTabelaPreco = 0l;
		}
		
		//Query de Frete Quilo
		SqlTemplate sqlFreteQuilo = createSqlTemplate();
		sqlFreteQuilo.addProjection("PF.VL_PRECO_FRETE" ,"TAXA_MINIMA");
		sqlFreteQuilo.addProjection("ID_ROTA_PRECO","PRECO_FRETE_RT");
		sqlFreteQuilo.addFrom("PARCELA_PRECO","PP");
		sqlFreteQuilo.addFrom("PRECO_FRETE","PF");
		sqlFreteQuilo.addFrom("TABELA_PRECO_PARCELA","TPP");
   		sqlFreteQuilo.addCustomCriteria("TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO");
		sqlFreteQuilo.addCustomCriteria("TPP.ID_TABELA_PRECO_PARCELA = PF.ID_TABELA_PRECO_PARCELA");
		sqlFreteQuilo.addCustomCriteria("PP.TP_PARCELA_PRECO = 'P'");
		sqlFreteQuilo.addCustomCriteria("PP.TP_PRECIFICACAO = 'P'");
		sqlFreteQuilo.addCustomCriteria("PP.CD_PARCELA_PRECO = 'IDTarifaMinima'");
		sqlFreteQuilo.addCriteria("TPP.ID_TABELA_PRECO","=",idTabelaPreco);
		
		//Query de Frete Peso
		SqlTemplate sqlFretePeso = createSqlTemplate();
		sqlFretePeso.addProjection("AO.SG_AEROPORTO","ORD_CD_CIDADE_AERO_O");
		sqlFretePeso.addProjection("PO.NM_FANTASIA","ORD_NM_PESS_O");
		sqlFretePeso.addProjection("AD.SG_AEROPORTO","ORD_CD_CIDADE_AERO_D");
		sqlFretePeso.addProjection("PD.NM_FANTASIA","ORD_NM_PESS_D");
		sqlFretePeso.addProjection("VF.VL_FIXO","DEST_X_FAIXA");
		sqlFretePeso.addProjection("PE.NR_TARIFA_ESPECIFICA","PRODUTO_ESPECIFICO");
		sqlFretePeso.addProjection("FP.VL_FAIXA_PROGRESSIVA","C_HEADER");
		sqlFretePeso.addProjection("RP.ID_ROTA_PRECO","ID_ROTA_PRECO");
		sqlFretePeso.addProjection("decode(PO.NM_FANTASIA,null,AO.CD_CIDADE,AO.CD_CIDADE || ' - ' || PO.NM_FANTASIA)","ORIGEM");
		sqlFretePeso.addProjection("decode(PD.NM_FANTASIA,null,AD.CD_CIDADE,decode(AD.BL_TAXA_TERRESTRE_OBRIGATORIA,'S','*'||AD.CD_CIDADE || ' - ' || PD.NM_FANTASIA,AD.CD_CIDADE || ' - ' || PD.NM_FANTASIA))","DESTINO_COM_SIGLA");
		sqlFretePeso.addProjection("decode(AD.BL_TAXA_TERRESTRE_OBRIGATORIA,'S','*'||PD.NM_FANTASIA,PD.NM_FANTASIA)","DESTINO");
		if (MapUtils.getLong(parameters, "idAeroportoDestino")!=null){
			sqlFretePeso.addProjection("AO.CD_CIDADE","SIGLA");
		}else{
			sqlFretePeso.addProjection("AD.CD_CIDADE","SIGLA");
		}
		
		sqlFretePeso.addProjection("STP.TP_SUBTIPO_TABELA_PRECO","SUBTIPO");
		sqlFretePeso.addProjection("PC.NM_FANTASIA","CIA_AEREA");
		
		sqlFretePeso.addFrom("PARCELA_PRECO","PP");
		sqlFretePeso.addFrom("ROTA_PRECO RP");
		sqlFretePeso.addFrom("VALOR_FAIXA_PROGRESSIVA","VF");
		sqlFretePeso.addFrom("PRODUTO_ESPECIFICO","PE");
		sqlFretePeso.addFrom("TABELA_PRECO_PARCELA","TPP");
		sqlFretePeso.addFrom("FAIXA_PROGRESSIVA","FP");
		sqlFretePeso.addFrom("TABELA_PRECO","TP");
		sqlFretePeso.addFrom("TIPO_TABELA_PRECO","TTP");
		sqlFretePeso.addFrom("SUBTIPO_TABELA_PRECO","STP");
		sqlFretePeso.addFrom("AEROPORTO","AO");
		sqlFretePeso.addFrom("AEROPORTO","AD");
		sqlFretePeso.addFrom("FILIAL","FO");
		sqlFretePeso.addFrom("FILIAL","FD");
		sqlFretePeso.addFrom("PESSOA","PO");
		sqlFretePeso.addFrom("PESSOA","PD");
		sqlFretePeso.addFrom("PESSOA","PC");
		
		sqlFretePeso.addCustomCriteria("TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO");
		sqlFretePeso.addCustomCriteria("TP.ID_TABELA_PRECO = TPP.ID_TABELA_PRECO");
		sqlFretePeso.addCustomCriteria("TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO");
		sqlFretePeso.addCustomCriteria("TTP.ID_EMPRESA_CADASTRADA = PC.ID_PESSOA");
		sqlFretePeso.addCustomCriteria("TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO");
		sqlFretePeso.addCustomCriteria("TPP.ID_TABELA_PRECO_PARCELA = FP.ID_TABELA_PRECO_PARCELA");
		sqlFretePeso.addCustomCriteria("FP.ID_PRODUTO_ESPECIFICO = PE.ID_PRODUTO_ESPECIFICO (+)");
		sqlFretePeso.addCustomCriteria("FP.ID_FAIXA_PROGRESSIVA = VF.ID_FAIXA_PROGRESSIVA");
		sqlFretePeso.addCustomCriteria("VF.ID_ROTA_PRECO = RP.ID_ROTA_PRECO");
		sqlFretePeso.addCustomCriteria("RP.ID_AEROPORTO_ORIGEM = AO.ID_AEROPORTO");
		sqlFretePeso.addCustomCriteria("RP.ID_AEROPORTO_DESTINO = AD.ID_AEROPORTO");
		sqlFretePeso.addCustomCriteria("AO.ID_FILIAL_RESPONSAVEL = FO.ID_FILIAL (+)");
		sqlFretePeso.addCustomCriteria("FO.ID_FILIAL = PO.ID_PESSOA (+)");
		sqlFretePeso.addCustomCriteria("AD.ID_FILIAL_RESPONSAVEL = FD.ID_FILIAL");
		sqlFretePeso.addCustomCriteria("FD.ID_FILIAL = PD.ID_PESSOA");
		sqlFretePeso.addCustomCriteria("PP.TP_PARCELA_PRECO = 'P'");
		sqlFretePeso.addCustomCriteria("PP.TP_PRECIFICACAO = 'M'");
		sqlFretePeso.addCustomCriteria("PP.CD_PARCELA_PRECO = 'IDFretePeso'");
		sqlFretePeso.addCriteria("TPP.ID_TABELA_PRECO","=",idTabelaPreco);
		
		Long idAeroportoOrigem = MapUtils.getLong(parameters, "idAeroportoOrigem");
		Long idAeroportoDestino = MapUtils.getLong(parameters, "idAeroportoDestino");
		
		sqlFretePeso.addCriteria("RP.ID_AEROPORTO_ORIGEM","=",idAeroportoOrigem);
		sqlFretePeso.addCriteria("RP.ID_AEROPORTO_DESTINO","=",idAeroportoDestino);

		/*inclui o filtro para emitir apenas aeroportos de capitais, definido 
		 * pelo checkBox "Emitir Somente Capitais"
		 */
		Boolean emiteSomenteCapitais = MapUtils.getBoolean(parameters, "blEmitirSomenteCapitais");
		if (emiteSomenteCapitais != null && emiteSomenteCapitais.booleanValue()){
			TipoLocalizacaoMunicipio capital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Capital", "O");
			if (capital != null){
				this.addicionaFiltroAeroportosCapitais(sqlFretePeso, "AO", capital.getIdTipoLocalizacaoMunicipio());
				this.addicionaFiltroAeroportosCapitais(sqlFretePeso, "AD", capital.getIdTipoLocalizacaoMunicipio());
			}
		}
		
		//Montagem da query principal
		SqlTemplate sqlMainQuery = createSqlTemplate();
		sqlMainQuery.addProjection("*");
		sqlMainQuery.addFrom(sqlFreteQuilo, "FRETE_QUILO");
		sqlMainQuery.addFrom(sqlFretePeso,"FRETE_PESO");
		sqlMainQuery.addCustomCriteria("FRETE_QUILO.PRECO_FRETE_RT = FRETE_PESO.ID_ROTA_PRECO");
		
		if (idAeroportoDestino != null){
			sqlMainQuery.addOrderBy("FRETE_PESO.ORD_NM_PESS_D");
			sqlMainQuery.addOrderBy("FRETE_PESO.ORD_NM_PESS_O");
			sqlMainQuery.addOrderBy("FRETE_PESO.ORD_CD_CIDADE_AERO_O");
		}else{
			sqlMainQuery.addOrderBy("FRETE_PESO.ORD_CD_CIDADE_AERO_O");
			sqlMainQuery.addOrderBy("FRETE_PESO.ORD_NM_PESS_D");
			sqlMainQuery.addOrderBy("FRETE_PESO.ORD_CD_CIDADE_AERO_D");
		}
		sqlMainQuery.addOrderBy("FRETE_PESO.PRODUTO_ESPECIFICO");
		
		return sqlMainQuery;
	}
	
	private void addicionaFiltroAeroportosCapitais(SqlTemplate sql, String tipoAeroporto, Long idTipoLocalizacao){
		if (!tipoAeroporto.equals("AO") && !tipoAeroporto.equals("AD")) return;
		String endereco = "end_"+tipoAeroporto;
		String municipio = "m_"+tipoAeroporto;
		String municipioFilial = "mf_"+tipoAeroporto;
		String operacao = "osl_"+tipoAeroporto;

		sql.addFrom("endereco_pessoa",endereco);
		sql.addFrom("municipio",municipio);
		sql.addFrom("municipio_filial", municipioFilial);
		sql.addFrom("operacao_servico_localiza", operacao);
		
		sql.addCustomCriteria(tipoAeroporto+".id_filial_responsavel = "+endereco+".id_pessoa");
		sql.addCustomCriteria(endereco+".id_municipio = "+municipio+".id_municipio");
		sql.addCustomCriteria(endereco+".dt_vigencia_final > sysdate");
		sql.addCustomCriteria(endereco+".dt_vigencia_inicial < sysdate");
		sql.addCustomCriteria(municipioFilial+".id_municipio = "+municipio+".id_municipio");
		sql.addCustomCriteria(municipioFilial+".dt_vigencia_final > sysdate");
		sql.addCustomCriteria(municipioFilial+".dt_vigencia_inicial < sysdate");
		sql.addCustomCriteria(operacao+".id_municipio_filial = "+municipioFilial+".id_municipio_filial");
		sql.addCustomCriteria(operacao+".id_tipo_localizacao_municipio = "+idTipoLocalizacao.toString());
		sql.addCustomCriteria(operacao+".dt_vigencia_final > sysdate");
		sql.addCustomCriteria(operacao+".dt_vigencia_inicial < sysdate");
		sql.addCustomCriteria("("+operacao+".ID_SERVICO = 1 OR "+operacao+".ID_SERVICO is null)");
	}

	/**
	 * 
	 * @return
	 */
	private String createRowCountQuery() {
		StringBuilder query = new StringBuilder()
			.append(" SELECT")
			.append("  	COUNT(DISTINCT PE.ID_PRODUTO_ESPECIFICO) AS TOTAL_B,")
			.append("  	COUNT(DISTINCT FP.VL_FAIXA_PROGRESSIVA) AS TOTAL_A")
			.append(" FROM PARCELA_PRECO PP")
			.append("  	,ROTA_PRECO RP")
			.append("  	,VALOR_FAIXA_PROGRESSIVA VF")
			.append("  	,PRODUTO_ESPECIFICO PE")
			.append("  	,TABELA_PRECO_PARCELA TPP")
			.append("  	,FAIXA_PROGRESSIVA FP")
			.append("  	,TABELA_PRECO TP")
			.append("  	,TIPO_TABELA_PRECO TTP")
			.append("  	,SUBTIPO_TABELA_PRECO STP")
			.append("  	,AEROPORTO AO")
			.append("  	,AEROPORTO AD")
			.append("  	,FILIAL FO")
			.append("  	,FILIAL FD")
			.append("  	,PESSOA PO")
			.append("  	,PESSOA PD")
			.append("  	,PESSOA PC")
			.append(" WHERE TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO")
			.append("  	AND TP.ID_TABELA_PRECO = TPP.ID_TABELA_PRECO")
			.append("  	AND TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO")
			.append("  	AND TTP.ID_EMPRESA_CADASTRADA = PC.ID_PESSOA")
			.append("  	AND TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO")
			.append("  	AND TPP.ID_TABELA_PRECO_PARCELA = FP.ID_TABELA_PRECO_PARCELA")
			.append("  	AND FP.ID_PRODUTO_ESPECIFICO = PE.ID_PRODUTO_ESPECIFICO(+)")
			.append("  	AND FP.ID_FAIXA_PROGRESSIVA = VF.ID_FAIXA_PROGRESSIVA")
			.append("  	AND VF.ID_ROTA_PRECO = RP.ID_ROTA_PRECO")
			.append("  	AND RP.ID_AEROPORTO_ORIGEM = AO.ID_AEROPORTO")
			.append("  	AND RP.ID_AEROPORTO_DESTINO = AD.ID_AEROPORTO")
			.append("  	AND AO.ID_FILIAL_RESPONSAVEL = FO.ID_FILIAL(+)")
			.append("  	AND FO.ID_FILIAL = PO.ID_PESSOA(+)")
			.append("  	AND AD.ID_FILIAL_RESPONSAVEL = FD.ID_FILIAL")
			.append("  	AND FD.ID_FILIAL = PD.ID_PESSOA")
			.append("  	AND PP.TP_PARCELA_PRECO = 'P'")
			.append("  	AND PP.TP_PRECIFICACAO = 'M'")
			.append("  	AND PP.CD_PARCELA_PRECO = 'IDFretePeso'")
			.append("  	AND TPP.ID_TABELA_PRECO = ?");
		return query.toString();
	}

	/**
	 * 
	 * @return
	 */
	private String createRowCountGroupsQuery(Map parameters) {
		String groupBy = "ORIGEM";
		if (parameters.get("idAeroportoDestino")!=null){
			groupBy = "DESTINO"; 
		}
		StringBuilder query = new StringBuilder()
			.append(" SELECT COUNT(*) as TOT FROM ( ")
			.append(" 	SELECT COUNT("+groupBy+") AS TOT FROM " + NOME_TABELA)
			.append(" 	GROUP BY "+groupBy+" ")
			.append(" ORDER BY "+groupBy+" ASC) ");
		return query.toString();
	}
	
	private String createReportQuery(Map parameters){
		StringBuilder query = new StringBuilder("SELECT * FROM "+NOME_TABELA+ " ORDER BY ");
		if (parameters.get("idAeroportoDestino")!=null)
			query.append("DESTINO, ORIGEM,  SIGLA");
		else query.append("ORIGEM, DESTINO, SIGLA");
		return query.toString();
	}

	/**
	 * Metodo de execução do relatório
	 */
	public JRReportDataObject execute(Map parameters) {
		return null;
	}
	public List<Map<String, String>> findDados(Map parameters) throws Exception {
	
    	JdbcTemplate jdbcTemplate = getJdbcTemplate();

		/* Pega o id da tabela de preços. */
    	Long idTabelaPreco = MapUtils.getLong(parameters,"tipoTabelaPreco.idTabelaPreco", MapUtils.getLongValue(parameters,"tabelaPreco.idTabelaPreco"));

    	SqlTemplate sqlData = createDataQuery(parameters);
    	
		/* Obtem os dados do relatório */
		List result = getJdbcTemplate().queryForList(sqlData.getSql(), sqlData.getCriteria());


		/* Insere os dados na tebela temporaria do banco */
		getColumns(result,true,parameters);

		for (Object object : result) {
			Map map = (Map)object;
			map.put("TAXA_MINIMA", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"TAXA_MINIMA")));
			map.put("PRECO_FRETE_RT", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"PRECO_FRETE_RT")));
			map.put("DEST_X_FAIXA", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"DEST_X_FAIXA")));
		}
		/* Pega o sql count das queries */
		Integer[] counts = getCountForQuery(idTabelaPreco);


		parameters.put("MOEDA", getTabelasClienteService().getMoeda(idTabelaPreco, jdbcTemplate));

		/* Monta os parrameters desse relatorio , para serem mandados ao jasper. */
		int rowCountGroups = 0;
		montaParametersReport(parameters,this.getColumnsDefinition(), idTabelaPreco, rowCountGroups, jdbcTemplate);
		
		return result;
	}

	/**
	 * 
	 * @param parameters
	 * @param jdbcTemplate
	 * @param destinoSemSigla
	 * @return
	 */
	public int[] populateTable(Map parameters,JdbcTemplate jdbcTemplate, boolean destinoSemSigla) {
		jdbcTemplate.execute("DELETE FROM " + NOME_TABELA);	

		SqlTemplate sqlData = createDataQuery(parameters);
		
		/* Obtem os dados do relatório */
		List result = getJdbcTemplate().queryForList(sqlData.getSql(), sqlData.getCriteria());

		/* Insere os dados na tebela temporaria do banco */
		getColumns(result,true,parameters);

		this.setLocalVariableValue("crosstabColumns", this.getColumnsDefinition().getColumns());
		return new int[]{this.getColumnsDefinition().getTotVl(),this.getColumnsDefinition().getTotTe()}; 
	}

		
	/* Metodo que obtem as definições das colunas */
	private void getColumns(List<Map> data, boolean isReport11, Map parameters) {
		HashMap tabelas = new HashMap();
		List headersKG = new ArrayList();
		Set<Double> headersPE = new LinkedHashSet();
		BigDecimal anterior = null;

		/* Descobre as Colunas(Headers) do relatorio. */
		for (Map element : data) {
			/* captura todos os headers */
 			if (element.get("C_HEADER") == null) {
				headersPE.add(MapUtils.getDouble(element,"PRODUTO_ESPECIFICO"));
			} else {
				BigDecimal atual = (BigDecimal) element.get("C_HEADER");
				element.put("C_HEADER", element.get("C_HEADER")+"P");
				if(anterior != null && anterior.equals(atual) || headersKG.contains(atual)){					
					continue;
			}
				headersKG.add(atual);			
				anterior = atual;
			}

			/* Adiciona o valor dinamico */
		}

		Comparator comparator = new Comparator() {
			public int compare(Object arg0, Object arg1) {
				BigDecimal obj1 =  (BigDecimal) arg0;
				BigDecimal obj2 =  (BigDecimal) arg1;
				return obj1.compareTo(obj2);
			}
		};
		
		Collections.sort(headersKG, comparator);

		/* pega os header de KG - somente os que não se repetem */
		String lastElement = null;
		List<Map<String,String>> mapHeader = new ArrayList<Map<String,String>>();
		
		for (Object currentElement : headersKG) {
			Map<String, String> header = new HashMap<String, String>();
			String valor = "";
			if (lastElement == null){
				valor = currentElement+" (kg)";
			} else {
				valor = new BigDecimal(lastElement).add(BigDecimal.ONE) + " a " + currentElement;
			}
			lastElement = String.valueOf(currentElement);
			header.put("valor", String.valueOf(currentElement)+"P");
			header.put("label", valor);
			mapHeader.add(header);
			
		}

		/* Pega os headers de PE - somente os que não se repetem */
		for (Double element : headersPE) {
			Map<String, String> header = new HashMap<String, String>();
			if (element != null){
				String formatedValue = FormatUtils.formatDecimal("000", element);				
				header.put("valor",FormatUtils.formatDecimal("0", element));
				header.put("label","TE "+formatedValue);
				mapHeader.add(header);
			}
		}
		
		parameters.put("HEADER", mapHeader);
	}

	/**
	 * Metodo que monta os parametros do relatório
	 * 
	 * @param parameters
	 * @param cd
	 * @param idTabelaPreco
	 * @param qtdGroups
	 * @param jdbcTemplate
	 */
	private void montaParametersReport(Map parameters, ColumnsDefinition cd, Long idTabelaPreco, int qtdGroups, JdbcTemplate jdbcTemplate){
		Long idDivisao = MapUtilsPlus.getLong(parameters, "idDivisao", null);
		parameters.put("usuarioEmissor",SessionUtils.getUsuarioLogado().getNmUsuario());

		/* busca e adiciona o subrelatório de: Coleta */
		List coleta = getTabelasClienteService().getSubRelColeta(idTabelaPreco,getJdbcTemplate());
		parameters.put(TabelasClienteService.KEY_PARAMETER_COLETA, coleta);
		 
		/* busca e adiciona o subrelatório de: Entrega */
		List entrega = getTabelasClienteService().getSubRelEntrega(idTabelaPreco,getJdbcTemplate());
		parameters.put(TabelasClienteService.KEY_PARAMETER_ENTREGA, entrega);

		/* busca e adiciona o subrelatório: aereo */
		List aereo = getTabelasClienteService().getSubRelAereo(jdbcTemplate);
		parameters.put(TabelasClienteService.KEY_PARAMETER_AEREO,aereo);

		/* busca de adiciona o subrelatório de: legendas */
		List legendas = getTabelasClienteService().getSubRelLegendas(jdbcTemplate);
		parameters.put(TabelasClienteService.KEY_PARAMETER_LEGENDAS,legendas);

		/* busca e adiciona o subrelatorio de servicos adicionais. */
		List<Map> generalidadesDificuldadeEntrega = getTabelasClienteService().getGeneralidadesTabelaPrecoDificuldadeEntrega(idTabelaPreco, jdbcTemplate);
		parameters.put(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA, generalidadesDificuldadeEntrega);
		
		List servAdd = getTabelasClienteService().getSubServicosAdicionais(idDivisao, null, null, null, getConfiguracoesFacade(),getJdbcTemplate(),null);
		parameters.put(TabelasClienteService.KEY_PARAMETER_SERVICOSAD, servAdd);

		//TAXA TERRESTRE
		List listTaxTerrestre = getTabelasClienteService().getSubRelTaxaTerrestre(idTabelaPreco, getJdbcTemplate());
		if(listTaxTerrestre !=null && !listTaxTerrestre.isEmpty()) {
			parameters.put(TabelasClienteService.KEY_PARAMETER_TAXA_TERRESTRE, listTaxTerrestre);
		}

       	//TAXA COMBUSTIVEL
		Map mapTaxCombustivel = getTabelasClienteService().getSubRelTaxaCombustivel(idTabelaPreco, jdbcTemplate);
		List listTaxCombustivel = (List)mapTaxCombustivel.get("RESULT");
		parameters.put(TabelasClienteService.KEY_PARAMETER_TAXA_COMBUSTIVEL, listTaxCombustivel); 
		Set subCrossTab = (Set)mapTaxCombustivel.get("SUBCROSSTAB");
		parameters.put(TabelasClienteService.KEY_PARAMETER_TAXA_COMBUSTIVEL+"CABECALHO", subCrossTab); 
		if(listTaxCombustivel !=null && !listTaxCombustivel.isEmpty()) {
			List listColumn = new ArrayList();
			listColumn.addAll(subCrossTab);
			String nomeSubRel = TabelasClienteService.PATH_TABELAPRECOS + "report/subReportTaxaCombustivel_Landscape_ct_" + subCrossTab.size() + ".jasper";

			parameters.put("SUBREPORTTAXACOMBUSTIVEL_PATH", nomeSubRel);
			parameters.put(TabelasClienteService.KEY_PARAMETER_TAXA_COMBUSTIVEL, listTaxCombustivel); 
			for(int j=0; j<subCrossTab.size();j++) {
				mapTaxCombustivel.put("COLUMN"+(j+1), listColumn.get(j));
			}
			emitirTabelaTaxaCombustivel.setMapParameters(mapTaxCombustivel);
			parameters.put("SERVICE_TX_COMB", emitirTabelaTaxaCombustivel);
		}
	}

	/* Meotodo que devolve os totais de tuplas das queries */
	private Integer[] getCountForQuery(Long idTabelaPreco)	{
	    List result = getJdbcTemplate().queryForList(createRowCountQuery(),new Object[]{idTabelaPreco});
	    if (result==null || result.isEmpty()) {
	    	return new Integer[]{IntegerUtils.ZERO, IntegerUtils.ZERO};
	    }
	    Map data = (Map)result.get(0);
	    return new Integer[]{
	    	MapUtils.getInteger(data, "TOTAL_A"),
	    	MapUtils.getInteger(data, "TOTAL_B")};
	}

	public ColumnsDefinition getColumnsDefinition() {
		return (ColumnsDefinition) this.getLocalVariableValue("columnsDefinition");
	}

	public void setColumnsDefinition(ColumnsDefinition columnsDefinition) {
		this.setLocalVariableValue("columnsDefinition", columnsDefinition);
	}

	public Set getColunasCrosstab(){
		return (Set) this.getLocalVariableValue("crosstabColumns");
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public EmitirTabelaTaxaCombustivelLandscapeService getEmitirTabelaTaxaCombustivel() {
		return emitirTabelaTaxaCombustivel;
	}

	public void setEmitirTabelaTaxaCombustivel(
			EmitirTabelaTaxaCombustivelLandscapeService emitirTabelaTaxaCombustivel) {
		this.emitirTabelaTaxaCombustivel = emitirTabelaTaxaCombustivel;
	}
}