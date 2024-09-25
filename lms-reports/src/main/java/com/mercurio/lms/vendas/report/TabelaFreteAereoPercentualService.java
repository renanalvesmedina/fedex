package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
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
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;
import com.mercurio.lms.vendas.util.LMSScriptlet;

/**
 * @author Alexandre Poletto
 *
 * 30.03.02.33 - Emitir tabela de frete aereo percentual
 *
 * @spring.bean id="lms.vendas.report.tabelaFreteAereoPercentualService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteAereoPercentual.jasper"
 */
public class TabelaFreteAereoPercentualService extends ReportServiceSupport {

	private static final String NOME_TABELA = "TMP_TABELA_FRETE_PERCENTUAL";

	/** Para guardar o idTabelaPreco e o dsSimbolo e compartilhar entre metodos */
	private static final ThreadLocal dadosClasse = new ThreadLocal();
    private ConfiguracoesFacade configuracoesFacade;
    private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	private EmitirTabelasClientesService emitirTabelasClientesService;
	private TabelasClienteService tabelasClienteService;

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}
	
	public JRReportDataObject execute(Map criteria) throws Exception {
		return null;
	}
		public List<Map<String, String>> findDados(TypedFlatMap parameters) {
			List<Map> data = getEmitirTabelasClienteDAO().findRelatorioFretePercentual(parameters, ConstantesExpedicao.MODAL_AEREO);
			TypedFlatMap parametros = new TypedFlatMap();
			parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
			List<Map<String, String>> result = new ArrayList<Map<String,String>>();
			if(data == null || data.isEmpty()){
				return null;
			}
			parameters.put("idCliente", MapUtils.getLong(data.get(0), "idCliente"));
			parameters.put("idDivisao", MapUtils.getLong(data.get(0), "idDivisao"));
			parameters.put("idTabelaPreco", MapUtils.getLong(data.get(0), "idTabelaPreco"));
			parameters.put("idContato", MapUtils.getLong(data.get(0), "idContato"));

		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		jdbcTemplate.execute("DELETE FROM "+ NOME_TABELA);

		Long idCliente = MapUtils.getLong(parameters,"idCliente");
		Long idContato = MapUtils.getLong(parameters,"idContato");
		Long idDivisaoCliente = MapUtils.getLong(parameters,"idDivisao");
		List parametrosL = MapUtilsPlus.getList(parameters,"listaParametros",null);
		Long idTabelaDivisaoCliente = MapUtils.getLong(parameters,"idTabelaDivisao");
		Long idSimulacao = MapUtils.getLong(parameters,"idSimulacao");
		Boolean isTabelaNova = MapUtils.getBoolean(parameters, "isTabelaNova", false);
		Simulacao simulacao = (Simulacao)MapUtils.getObject(parameters, "simulacao");

		//faz o relatorio retornar em branco caso ele nao contenha dados
		if(idSimulacao == null && idTabelaDivisaoCliente == null){
			return null;
		}

		//Agrupa os dados 
        Map reportGroups = new HashMap();
        reportGroups = getTabelasClienteService().montaAgrupamento(
        	 idCliente
        	,idDivisaoCliente
        	,idSimulacao
        	,parametrosL
        	,reportGroups
        	,jdbcTemplate);
        //insere os dados na tabela temporaria
        
        populateTable(parametrosL 	,parameters   	,reportGroups);

	    //Acha os dados do cabeçalho
		Map parametersReport = new HashMap();
		parametersReport.put("idCliente",idCliente);
		parametersReport.put("idContato",idContato);
		Object[] obj = (Object[])dadosClasse.get();
		Long idTabelaPreco = (Long)obj[0];
		emitirTabelasClientesService.montaParametrosAereo(
			idTabelaPreco
			,idCliente
			,idDivisaoCliente
			,isTabelaNova
			,idSimulacao
			,parametersReport
			,reportGroups
			,jdbcTemplate
			,configuracoesFacade);

		if(simulacao == null) {
			parametersReport.put("SERVICO", getTabelasClienteService().getTipoServico(idTabelaDivisaoCliente,jdbcTemplate));	
		} else {
			Servico servico = simulacao.getServico();
			Map tipoServico = getTabelasClienteService().getTipoServicoByServico(servico.getIdServico(), jdbcTemplate);
			parametersReport.put("SERVICO", MapUtils.getString(tipoServico,"DS_TIPO_SERVICO"));			
		}

		getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);
		parametersReport.put("REPORT_SCRIPTLET", new LMSScriptlet());
		int dataCount = jdbcTemplate.queryForInt("Select COUNT(*) from " + NOME_TABELA);
		parametersReport.put("TOTAL", Integer.valueOf(dataCount/reportGroups.size()));

	    JRReportDataObject jr = executeQuery("SELECT * FROM "+ NOME_TABELA + " Order by GRUPO", parameters);
		jr.setParameters(parametersReport);
		parameters.put(idTabelaDivisaoCliente, parametersReport);
		return result;
	}

	/**
	 * Popula Tabela temporaria de dados 
	 * @param parametros
	 * @param idDivisao
	 * @param idSimulacao
	 * @param idCliente
	 * @param idTabelaDivisaoCliente
	 * @param reportGroups
	 */
	private void populateTable(List parametros, Map criteria, Map reportGroups) {
	    String query = createQuery(parametros, criteria);
	    List data = getJdbcTemplate().queryForList(query);

		if (!data.isEmpty()) {
			Long idTabelaPreco = Long.valueOf( ((Map)data.get(0)).get("ID_TABELA_PRECO").toString());
			String dsSimbolo = ((Map)data.get(0)).get("ID_TABELA_PRECO").toString();
			dadosClasse.set(new Object[]{idTabelaPreco, dsSimbolo});
		}

		// Querys
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		//obtencao da origem e destino
		StringBuffer sqlAeroporto = new StringBuffer()
			.append(" SELECT P.NM_FANTASIA")
			.append(" FROM PESSOA P, AEROPORTO A, FILIAL F ")
			.append(" WHERE A.ID_AEROPORTO = ?") // aeroporto origem do param do cliente
			.append("  AND A.ID_FILIAL_RESPONSAVEL = F.ID_FILIAL")
			.append("  AND F.ID_FILIAL = P.ID_PESSOA");

		StringBuffer sqlMunicipio = new StringBuffer()
			.append("select M.NM_MUNICIPIO from MUNICIPIO M where M.ID_MUNICIPIO = ?");

		StringBuffer sqlFilial = new StringBuffer()
			.append("select F.SG_FILIAL from FILIAL F where F.ID_FILIAL = ?");

		StringBuffer sqlTipoLocMunicipio = new StringBuffer()
			.append("SELECT ")
			.append(PropertyVarcharI18nProjection.createProjection("T.DS_TIPO_LOCAL_MUNICIPIO_I", "DS_TIPO_LOCALIZACAO_MUNICIPIO"))
			.append(" FROM TIPO_LOCALIZACAO_MUNICIPIO T")
			.append(" WHERE T.ID_TIPO_LOCALIZACAO_MUNICIPIO = ?");

		StringBuffer sqlGrupo = new StringBuffer("");
		sqlFilial.append("select GR.DS_GRUPO_REGIAO from GRUPO_REGIAO GR where GR.ID_GRUPO_REGIAO = ?");		

		List listaUFOrigem = new ArrayList();
		List listaUFDestino = new ArrayList();
		String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
		String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");
	    for (Iterator iter = data.iterator(); iter.hasNext();) {
			Map currentData = (Map) iter.next();
			Long idParametro = MapUtils.getLong(currentData, "ID_PARAMETRO_CLIENTE");
			String grupo = getTabelasClienteService().getGrupo(idParametro, reportGroups, reportGroups.size());
			String ufOrigem = MapUtils.getString(currentData,"UF_ORIGEM");
			String ufDestino = MapUtils.getString(currentData,"UF_DESTINO");
			String complementoOrigem = null;
			String complementoDestino = null;

			Long idAeroportoOrigem = MapUtils.getLong(currentData,"ID_AEROPORTO_ORIGEM");
			Long idMunicipioOrigem = MapUtils.getLong(currentData,"ID_MUNICIPIO_ORIGEM");
			Long idFilialOrigem	= MapUtils.getLong(currentData,"ID_FILIAL_ORIGEM");
			Long idTipoLocMunicipioOrigem = MapUtils.getLong(currentData,"ID_TIPO_LOC_MUNICIPIO_ORIGEM");
			Long idGrupoOrigem = MapUtils.getLong(currentData,"ID_GRUPO_REGIAO_ORIGEM");

			Long idAeroportoDestino	= MapUtils.getLong(currentData,"ID_AEROPORTO_DESTINO");
			Long idMunicipioDestino = MapUtils.getLong(currentData,"ID_MUNICIPIO_DESTINO");
			Long idFilialDestino = MapUtils.getLong(currentData,"ID_FILIAL_DESTINO");
			Long idTipoLocMunicipioDestino = MapUtils.getLong(currentData,"ID_TIPO_LOC_MUNICIPIO_DESTINO");
			Long idGrupoDestino = MapUtils.getLong(currentData,"ID_GRUPO_REGIAO_DESTINO");

			//Origem
			complementoOrigem = descobreComplemento(
				jdbcTemplate
				,complementoOrigem
				,idAeroportoOrigem
				,idMunicipioOrigem
				,idFilialOrigem
				,idTipoLocMunicipioOrigem
				,idGrupoOrigem
				,sqlAeroporto
				,sqlMunicipio
				,sqlFilial
				,sqlTipoLocMunicipio
				,sqlGrupo);

			//Destino
			complementoDestino = descobreComplemento(
				jdbcTemplate
				,complementoDestino
				,idAeroportoDestino
				,idMunicipioDestino
				,idFilialDestino
				,idTipoLocMunicipioDestino
				,idGrupoDestino
				,sqlAeroporto
				,sqlMunicipio
				,sqlFilial
				,sqlTipoLocMunicipio
				,sqlGrupo);			

			//Concatena o uf da origem e do destino com "-" para ser usado no metodo verificaComplemento
			String ufOrigemDestino = ufOrigem+"-"+ufDestino;
			complementoOrigem = getTabelasClienteService().verificaComplemento(listaUFOrigem, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, complementoOrigem);
			complementoDestino = getTabelasClienteService().verificaComplemento(listaUFDestino, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, complementoDestino);

			String origem  = complementoOrigem  != null ? ufOrigem + " - " + complementoOrigem : ufOrigem;
			String destino = complementoDestino != null ? ufDestino + " - " + complementoDestino : ufDestino;

			BigDecimal pcValorNF = MapUtilsPlus.getBigDecimal(currentData, "PERCVALORNF");
			BigDecimal vlFrete = MapUtilsPlus.getBigDecimal(currentData, "FRETECTRC");
			BigDecimal psMinimo = MapUtilsPlus.getBigDecimal(currentData, "PESOMIN");
			BigDecimal vlFreteTonelada = MapUtilsPlus.getBigDecimal(currentData, "FRETETONEL");

			//Insere os dados
			StringBuffer sqlInsert = new StringBuffer()
				.append(" INSERT INTO ").append(NOME_TABELA).append(" (")
				.append(" 	 ID_PARAMETRO_CLIENTE")
				.append("	,PERCVALORNF")
				.append("	,FRETECTRC")
				.append("	,PESOMIN")
				.append("	,FRETETONEL")
				.append("	,ORIGEM")
				.append("	,DESTINO")
				.append("	,GRUPO)")
				.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

			jdbcTemplate.update(sqlInsert.toString(), new Object[]{idParametro, pcValorNF, vlFrete, psMinimo, vlFreteTonelada, origem, destino, grupo});
		}

	    getTabelasClienteService().verificaUfs(listaUFOrigem, "ORIGEM", NOME_TABELA, msgTodoEstado, msgDemaisLocalidades, jdbcTemplate);
	    getTabelasClienteService().verificaUfs(listaUFDestino, "DESTINO", NOME_TABELA, msgTodoEstado, msgDemaisLocalidades, jdbcTemplate);
	}

	private String descobreComplemento(JdbcTemplate jdbcTemplate, String complemento, Long idAeroporto, Long idMunicipio, Long idFilial, Long idTipoLocMunicipio, Long idGrupo, StringBuffer sqlAeroporto, StringBuffer sqlMunicipio, StringBuffer sqlFilial, StringBuffer sqlTipoLocMunicipio, StringBuffer sqlGrupo) {
		if(idAeroporto != null) {
			List result = jdbcTemplate.queryForList(sqlAeroporto.toString(), new Long[]{idAeroporto});
			Map map = result.isEmpty() ? new HashMap() : (Map)result.get(0);
			complemento = MapUtils.getString(map,"NM_FANTASIA");
		} else if (idMunicipio != null) {
			List result = jdbcTemplate.queryForList(sqlMunicipio.toString(), new Long[]{idMunicipio});
			Map map = result.isEmpty() ? new HashMap() : (Map)result.get(0);
			complemento = MapUtils.getString(map,"NM_MUNICIPIO");
		} else if (idFilial != null) {
			List result = jdbcTemplate.queryForList(sqlFilial.toString(), new Long[]{idFilial});
			Map map = result.isEmpty() ? new HashMap() : (Map)result.get(0);
			complemento = MapUtils.getString(map,"SG_FILIAL");
		} else if (idTipoLocMunicipio != null) {
			List result = jdbcTemplate.queryForList(sqlTipoLocMunicipio.toString(), new Long[]{idTipoLocMunicipio});
			Map map = result.isEmpty() ? new HashMap() : (Map)result.get(0);
			complemento = MapUtils.getString(map,"DS_TIPO_LOCALIZACAO_MUNICIPIO");
		}else{
			List result = jdbcTemplate.queryForList(sqlGrupo.toString(), new Long[]{idGrupo});
			Map map = result.isEmpty() ? new HashMap() : (Map)result.get(0);
			complemento = MapUtils.getString(map,"DS_GRUPO_REGIAO");			
		}
		return complemento;
	}

	private String createQuery(List parametros, Map criteria){
		Long idCliente = MapUtils.getLong(criteria,"idCliente");
		Long idDivisaoCliente = MapUtils.getLong(criteria,"idDivisao");
		Long idTabelaDivisaoCliente = MapUtils.getLong(criteria,"idTabelaDivisao");
		Long idSimulacao = MapUtils.getLong(criteria,"idSimulacao");
		
		StringBuffer sql = new StringBuffer()
			.append(" SELECT DISTINCT ")
		 	.append("	  PC.ID_PARAMETRO_CLIENTE,");
		if(idSimulacao==null){
			sql.append("     TP.ID_TABELA_PRECO,");
		} else sql.append("     PC.ID_TABELA_PRECO,");
		sql.append("	  M.DS_SIMBOLO,")
			.append("     UF_ORIGEM.SG_UNIDADE_FEDERATIVA as UF_ORIGEM,")
			.append("     UF_DESTINO.SG_UNIDADE_FEDERATIVA as UF_DESTINO,")
			.append("     PC.PC_FRETE_PERCENTUAL as PERCVALORNF,")
			.append("     PC.VL_MINIMO_FRETE_PERCENTUAL as FRETECTRC,")
			.append("     PC.PS_FRETE_PERCENTUAL as PESOMIN,")
			.append("     PC.VL_TONELADA_FRETE_PERCENTUAL as FRETETONEL,")
			.append("	  PC.ID_AEROPORTO_ORIGEM,")
		 	.append("	  PC.ID_MUNICIPIO_ORIGEM,")
		 	.append("	  PC.ID_FILIAL_ORIGEM,")
		 	.append("	  PC.ID_TIPO_LOC_MUNICIPIO_ORIGEM,")
		 	.append("	  PC.ID_AEROPORTO_DESTINO,")
		 	.append("	  PC.ID_MUNICIPIO_DESTINO,")
		 	.append("	  PC.ID_FILIAL_DESTINO,")
		 	.append("	  PC.ID_TIPO_LOC_MUNICIPIO_DESTINO, ")
		 	.append("	  PC.ID_GRUPO_REGIAO_ORIGEM, ")
		 	.append("	  PC.ID_GRUPO_REGIAO_DESTINO ")
		 	
		 	.append(" FROM CLIENTE C, ")
			.append("     DIVISAO_CLIENTE DC, ")
			.append("     TABELA_DIVISAO_CLIENTE TDC, ")
			.append("     TABELA_PRECO TP, ")
			.append("     MOEDA M, ")
			.append("     PARAMETRO_CLIENTE PC, ")
			.append("     MUNICIPIO M_Origem, ")
			.append("     MUNICIPIO M_Destino, ")
			.append("     UNIDADE_FEDERATIVA UF_ORIGEM, ")
			.append("     UNIDADE_FEDERATIVA UF_DESTINO, ")
			.append("     FILIAL F_ORIGEM, ")
			.append("     FILIAL F_DESTINO, ")
			.append("     TIPO_LOCALIZACAO_MUNICIPIO TLM_ORIGEM, ")
			.append("     TIPO_LOCALIZACAO_MUNICIPIO TLM_DESTINO ")
			.append(" WHERE C.ID_CLIENTE = DC.ID_CLIENTE")
			.append("     AND DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE");
		 if(idSimulacao==null) {
			 sql.append("     AND TDC.ID_TABELA_DIVISAO_CLIENTE = PC.ID_TABELA_DIVISAO_CLIENTE");
		 }
		 sql.append("     AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO")
		 	.append("     AND TP.ID_MOEDA = M.ID_MOEDA")
		 	.append("     AND PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA")
		 	.append("     AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
		 	.append("     AND PC.ID_MUNICIPIO_ORIGEM = M_ORIGEM.ID_MUNICIPIO (+)")
		 	.append("     AND PC.ID_MUNICIPIO_DESTINO = M_DESTINO.ID_MUNICIPIO (+)")
		 	.append("     AND PC.ID_FILIAL_ORIGEM = F_ORIGEM.ID_FILIAL (+)")
		 	.append("     AND PC.ID_FILIAL_DESTINO = F_DESTINO.ID_FILIAL (+)")
		 	.append("     AND PC.ID_TIPO_LOC_MUNICIPIO_ORIGEM = TLM_ORIGEM.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)")
		 	.append("     AND PC.ID_TIPO_LOC_MUNICIPIO_DESTINO = TLM_DESTINO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)")
		 	.append("     AND C.ID_CLIENTE = ").append(idCliente.longValue());
		 if(idDivisaoCliente != null) {
		 	sql.append("     AND DC.ID_DIVISAO_CLIENTE = ").append(idDivisaoCliente.longValue());
		 }
		 if(idSimulacao == null){
			 sql.append("     AND TDC.ID_TABELA_DIVISAO_CLIENTE = ").append(idTabelaDivisaoCliente.longValue());
		 } else {
			 sql.append("     AND PC.ID_SIMULACAO = ").append(idSimulacao.longValue());
		 }

		 //Parametros de consulta
		 StringBuilder paramValues = new StringBuilder();
		 for (Iterator iter = parametros.iterator(); iter.hasNext();) {
			if(StringUtils.isNotBlank(paramValues.toString())){
				paramValues.append(", ");
			}
			paramValues.append((Long) iter.next());
		 }
		 sql.append("     AND PC.ID_PARAMETRO_CLIENTE IN (").append(paramValues).append(")")
		 	.append(" ORDER BY UF_ORIGEM.SG_UNIDADE_FEDERATIVA")
		 	.append("  		  ,UF_DESTINO.SG_UNIDADE_FEDERATIVA");
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
	
	public EmitirTabelasClienteDAO getEmitirTabelasClienteDAO() {
		return emitirTabelasClienteDAO;
	}

	public void setEmitirTabelasClienteDAO(
			EmitirTabelasClienteDAO emitirTabelasClienteDAO) {
		this.emitirTabelasClienteDAO = emitirTabelasClienteDAO;
	}
	

	public EmitirTabelasClientesService getEmitirTabelasClientesService() {
		return emitirTabelasClientesService;
	}
	public void setEmitirTabelasClientesService(
			EmitirTabelasClientesService emitirTabelasClientesService) {
		this.emitirTabelasClientesService = emitirTabelasClientesService;
	}

}