package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * ET 30.03.02.28
 * 
 * @author Rodrigo F. Dias
 * 
 * @spring.bean id="lms.vendas.tabelaFreteAereoTaxaMinimaPesoExcedenteService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteAereoTaxaMinimaPesoExcedente.jasper"
 */
public class TabelaFreteAereoTaxaMinimaPesoExcedenteService extends	ReportServiceSupport {

	private static final String NOME_TABELA = "TMP_FRETE_AEREO_TAXA_MIN_PEXCE";

	private EmitirTabelaTaxaCombustivelPortraitService emitirTabelaTaxaCombustivel;
	
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	private EmitirTabelasClientesService emitirTabelasClientesService;
	private TabelasClienteService tabelasClienteService;
	
	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}
	
	/**
	 * Para utiliza&ccedil;&aatilde;o de RecursosMensagens
	 */
	private ConfiguracoesFacade configuracoesFacade = null;

	public JRReportDataObject execute(Map parameters) throws Exception {
		return null;
	}

	public List<Map<String, String>> findDados(TypedFlatMap parameters) {
		
		TypedFlatMap parametros = new TypedFlatMap();
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		List listaParametros = new ArrayList();
		boolean isFirstRecord = true;
		Long idTabelaDivisao = null;
		
		Simulacao simulacao  = (Simulacao)MapUtils.getObject(parameters, "simulacao");
		
		if(simulacao == null){
			List<Map> data = getEmitirTabelasClienteDAO().findRelatorioFreteAereoTaxaMinimaPesoExcedente(parameters);
		
			if(data == null || data.isEmpty()){
				return null;
			}
	
			for (Map map : data) {
				parameters.put("idCliente", MapUtils.getLong(map,"idCliente"));
				parameters.put("idDivisao", MapUtils.getLong(map,"idDivisao"));
				parameters.put("idTabelaPreco", MapUtils.getLong(map,"idTabelaPreco"));
				
				if(isFirstRecord) {
					idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
					parametros = getCommonParameter(map);
					parametros.put("idTabelaDivisaoCliente", idTabelaDivisao);
					listaParametros.add(MapUtilsPlus.getLong(map,"listaParametros",null));
					parametros.put("listaParametros", listaParametros );
					parametros.put("relatorio", "28");
					isFirstRecord = false;
				} else {
					if(!idTabelaDivisao.equals(MapUtils.getLong(map,"idTabelaDivisao"))) {
						parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
						//mrc.addCommand("lms.vendas.tabelaFreteAereoTaxaMinimaPesoExcedenteService", parametros);//adiciona anterior
	
						idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
						parametros = getCommonParameter(map);
						parametros.put("idTabelaDivisaoCliente", idTabelaDivisao);
						listaParametros = new ArrayList();
						listaParametros.add(MapUtilsPlus.getLong(map,"listaParametros",null));
						parametros.put("listaParametros", listaParametros );
						parametros.put("relatorio", "28");
					} else {
						idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
						listaParametros.add(MapUtilsPlus.getLong(map,"listaParametros",null));
						parametros.put("listaParametros", listaParametros );
					}
				}
			}
			// Verifica se foram adicionados parametros
			if(!listaParametros.isEmpty()) {
				parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
				//mrc.addCommand("lms.vendas.tabelaFreteAereoTaxaMinimaPesoExcedenteService", parametros);//adiciona ultimo
			}
	
		}

		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		Map parametersReport = new HashMap();
		
		// limpa a tabela temporararia
		jdbcTemplate.execute("DELETE FROM " + NOME_TABELA);

		// Parametros recebidos da Action
		Long idCliente 				= MapUtils.getLong(parametros, "idCliente");
		Long idDivisao 				= MapUtils.getLong(parametros, "idDivisao");
		Long idContato 				= MapUtils.getLong(parametros,"idContato");
		Long idTabelaDivisaoCliente	= MapUtils.getLong(parametros, "idTabelaDivisaoCliente");
		List parametrosCli			= MapUtilsPlus.getList(parametros, "listaParametros", null);
		Long idTabelaPreco			= MapUtils.getLong(parametros, "idTabelaPreco");
		Boolean isTabelaNova = MapUtils.getBoolean(parametros, "isTabelaNova", false);
		Long idSimulacao 			= MapUtils.getLong(parametros, "idSimulacao");
		
		//Se não tem dados para o relatório
		if (idTabelaPreco == null) {
			return null;
		}
		
		// Agrupa os dados
		Map agrup = new HashMap();
		agrup = getTabelasClienteService().montaAgrupamento(idCliente, idDivisao, idSimulacao, parametrosCli, agrup, jdbcTemplate);
		boolean processoOk = false;
		// insere os dados na tabela temporaria
		if (parameters != null) 
		{
			processoOk = populateTable(parametrosCli, idDivisao, idCliente, agrup, jdbcTemplate,idTabelaDivisaoCliente, idSimulacao);
		}
		int size = agrup.size();
		if(!processoOk) return null;
			
		int totRegistros = jdbcTemplate.queryForInt("Select COUNT(*) from " + NOME_TABELA);
		if(totRegistros>0){
			if(size==0) size = 1;
			parametersReport.put("TOTAL", Integer.valueOf(totRegistros/size));
		}else{
			return null ;
		}
		List dados = jdbcTemplate.queryForList("SELECT * FROM " + NOME_TABELA);

		// seta os parametros no relatorio
		getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);
		
		String dsTipoServico = null;
		if(simulacao==null){
			dsTipoServico = getTabelasClienteService().getTipoServico(idTabelaDivisaoCliente, jdbcTemplate);
		}else{
			Servico serv = simulacao.getServico();
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(serv.getIdServico(), jdbcTemplate);
			dsTipoServico = MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO");
		}
		parametersReport.put("SERVICO", dsTipoServico);
		
		
		String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco, jdbcTemplate);
		parametersReport.put("dsSimbolo", dsSimbolo);
		
        parametersReport.put("idCliente",idCliente);
        parametersReport.put("idContato",idContato);
        emitirTabelasClientesService.montaParametrosAereo(idTabelaPreco,idCliente, idDivisao, isTabelaNova,idSimulacao, parametersReport, agrup, jdbcTemplate,configuracoesFacade);
		getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);
		Map grupos = new HashMap();
		grupos.put("grupo", agrup.size());
		parameters.putAll(grupos);
		parameters.putAll(parametersReport);
		result.add(grupos);
		result.add(parametersReport);
		result.addAll(dados);
		
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
		JRReportDataObject jr = executeQuery("SELECT * FROM " + NOME_TABELA + " order by GRUPO", new HashMap());
		Map map = new HashMap();
		map.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[] {Integer.valueOf(0)});
		jr.setParameters(map);
		return jr;
	}

	

	/**
	 * Popula a tabela tempor&aacute;ria com dados
	 * 
	 * @param parametros
	 * @param divisao
	 * @param idCliente
	 * @param agrup
	 */
	private boolean populateTable(List parametros, Long idDivisao, Long idCliente, Map agrup, JdbcTemplate jdbcTemplate, Long idTabelaDivisao, Long idSimulacao) {
		if (parametros != null) {
			String sql = montaSql(parametros, idSimulacao);
			int tamanho = agrup.size();
			
			List listaUFOrigem = new ArrayList();
			List listaUFDestino = new ArrayList();
			
		    List registros =  new ArrayList();
		    if(idSimulacao==null){
		    	registros = jdbcTemplate.queryForList(sql, new Long[]{idCliente,idDivisao,idTabelaDivisao});
		    }else{
		    	registros = jdbcTemplate.queryForList(sql, new Long[]{idCliente,idDivisao});
		    }
		   
		    if(registros.isEmpty()) return false;
		    
		    Map dados = null;
			String origem = null, destino = null, grupo = null, ufOrigem = null, ufDestino = null, mOrigem = null, mDestino = null;
			Long idParametro = null;
			BigDecimal idAeroportoOrigem = null, idMunicipioOrigem = null, idFilialOrigem = null, idTipoLocMunicipioOrigem = null, idGrupoOrigem = null, idGrupoDestino = null;
			BigDecimal idAeroportoDestino = null, idMunicipioDestino = null, idFilialDestino = null, idTipoLocMunicipioDestino = null;
			BigDecimal taxaMinima = null, pesoMinimo = null, excedenteKg = null;
		    
			
			String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
			String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");
			    
		    for (Iterator iter = registros.iterator(); iter.hasNext();)
		    {
				dados = (Map) iter.next();
				StringBuffer sqlInsert = new StringBuffer();
				idParametro = Long.valueOf(dados.get("ID_PARAMETRO_CLIENTE").toString());
				
				grupo = getTabelasClienteService().getGrupo(idParametro, agrup, tamanho);
				
				if (dados.get("UF_ORIGEM")== null || dados.get("UF_DESTINO") == null) continue;
				ufOrigem  = dados.get("UF_ORIGEM").toString();
				ufDestino = dados.get("UF_DESTINO").toString();

				taxaMinima  = (BigDecimal) dados.get("TAXA_MINIMA");
				pesoMinimo  = (BigDecimal) dados.get("PESO_MINIMO");
				excedenteKg = (BigDecimal) dados.get("EXCEDENTEKG");

				idAeroportoOrigem 		  = (BigDecimal) dados.get("ID_AEROPORTO_ORIGEM");
				idMunicipioOrigem 		  = (BigDecimal) dados.get("ID_MUNICIPIO_ORIGEM");
				idFilialOrigem			  = (BigDecimal) dados.get("ID_FILIAL_ORIGEM");
				idTipoLocMunicipioOrigem  = (BigDecimal) dados.get("ID_TIPO_LOC_MUNICIPIO_ORIGEM");
				idGrupoOrigem  			  = (BigDecimal) dados.get("ID_GRUPO_REGIAO_ORIGEM");
				
				idAeroportoDestino		  = (BigDecimal) dados.get("ID_AEROPORTO_DESTINO");
				idMunicipioDestino 		  = (BigDecimal) dados.get("ID_MUNICIPIO_DESTINO");
				idFilialDestino			  = (BigDecimal) dados.get("ID_FILIAL_DESTINO");
				idTipoLocMunicipioDestino = (BigDecimal) dados.get("ID_TIPO_LOC_MUNICIPIO_DESTINO");
				idGrupoDestino 			  = (BigDecimal) dados.get("ID_GRUPO_REGIAO_DESTINO");
				
				//obtencao da origem e destino
				StringBuffer sqlAeroporto = new StringBuffer("");
				sqlAeroporto.append("select A.SG_AEROPORTO AS SG_AEROPORTO from PESSOA P, AEROPORTO A, FILIAL F ");
				sqlAeroporto.append("where A.ID_AEROPORTO = ?"); // aeroporto origem do param do cliente
				sqlAeroporto.append(" and A.ID_FILIAL_RESPONSAVEL = F.ID_FILIAL");
				sqlAeroporto.append(" and F.ID_FILIAL = P.ID_PESSOA");
				
				StringBuffer sqlMunicipio = new StringBuffer("");
				sqlMunicipio.append("select M.NM_MUNICIPIO from MUNICIPIO M where M.ID_MUNICIPIO = ?");
				
				StringBuffer sqlFilial = new StringBuffer("");
				sqlFilial.append("select F.SG_FILIAL from FILIAL F where F.ID_FILIAL = ?");
				
				StringBuffer sqlTipoLocMunicipio = new StringBuffer("");
				sqlTipoLocMunicipio.append("select ").append(PropertyVarcharI18nProjection.createProjection("T.DS_TIPO_LOCAL_MUNICIPIO_I", "DS_TIPO_LOCALIZACAO_MUNICIPIO"))
				.append(" from TIPO_LOCALIZACAO_MUNICIPIO T").append(" where T.ID_TIPO_LOCALIZACAO_MUNICIPIO = ?");				
				 
				StringBuffer sqlGrupo = new StringBuffer("");
				sqlFilial.append("select GR.DS_GRUPO_REGIAO from GRUPO_REGIAO GR where GR.ID_GRUPO_REGIAO = ?");
				 
				//origem
				mOrigem = null;
				if(idAeroportoOrigem != null)
				{
					List listaOrigem = jdbcTemplate.queryForList(sqlAeroporto.toString(), new BigDecimal[]{idAeroportoOrigem});
					Map  mapOrigem   = listaOrigem.isEmpty() ? new HashMap() : (Map)listaOrigem.get(0);
					mOrigem = MapUtils.getString(mapOrigem,"SG_AEROPORTO");
				}
				else if(idAeroportoOrigem == null && idMunicipioOrigem != null){
					List listaOrigem = jdbcTemplate.queryForList(sqlMunicipio.toString(), new BigDecimal[]{idMunicipioOrigem});
					Map  mapOrigem   = listaOrigem.isEmpty() ? new HashMap() : (Map)listaOrigem.get(0);
					mOrigem = MapUtils.getString(mapOrigem,"NM_MUNICIPIO");
				}
				else if(idAeroportoOrigem == null && idMunicipioOrigem == null && idFilialOrigem != null){
					List listaOrigem = jdbcTemplate.queryForList(sqlFilial.toString(), new BigDecimal[]{idFilialOrigem});
					Map  mapOrigem   = listaOrigem.isEmpty() ? new HashMap() : (Map)listaOrigem.get(0);
					mOrigem = MapUtils.getString(mapOrigem,"SG_FILIAL");
				}
				else if (idAeroportoOrigem == null && idMunicipioOrigem == null && idFilialOrigem == null
						&& idTipoLocMunicipioOrigem != null){
					List listaOrigem = jdbcTemplate.queryForList(sqlTipoLocMunicipio.toString(), new BigDecimal[]{idTipoLocMunicipioOrigem});
					Map  mapOrigem   = listaOrigem.isEmpty() ? new HashMap() : (Map)listaOrigem.get(0);
					mOrigem = MapUtils.getString(mapOrigem,"DS_TIPO_LOCALIZACAO_MUNICIPIO");
				}			
				else if (idMunicipioOrigem == null && idFilialOrigem == null && idTipoLocMunicipioOrigem == null && idGrupoOrigem != null){
					List listaOrigem = jdbcTemplate.queryForList(sqlGrupo.toString(), new BigDecimal[]{idGrupoOrigem});
					Map  mapOrigem   = listaOrigem.isEmpty() ? new HashMap() : (Map)listaOrigem.get(0);
					mOrigem = MapUtils.getString(mapOrigem,"DS_GRUPO_REGIAO");
				}			

				//destino
				mDestino = null;
				if(idAeroportoDestino != null){
					
					List listaDestino = jdbcTemplate.queryForList(sqlAeroporto.toString(), new BigDecimal[]{idAeroportoDestino});
					Map  mapDestino   = listaDestino.isEmpty() ? new HashMap() : (Map)listaDestino.get(0);
					mDestino = MapUtils.getString(mapDestino,"SG_AEROPORTO");
				}
				else if (idAeroportoDestino == null && idMunicipioDestino != null){
					
					List listaDestino = jdbcTemplate.queryForList(sqlMunicipio.toString(), new BigDecimal[]{idMunicipioDestino});
					Map  mapDestino   = listaDestino.isEmpty() ? new HashMap() : (Map)listaDestino.get(0);
					mDestino = MapUtils.getString(mapDestino,"NM_MUNICIPIO");
				}
				else if(idAeroportoDestino  == null && idMunicipioDestino == null && idFilialDestino != null){
					
					List listaDestino = jdbcTemplate.queryForList(sqlFilial.toString(), new BigDecimal[]{idFilialDestino});
					Map  mapDestino   = listaDestino.isEmpty() ? new HashMap() : (Map)listaDestino.get(0);
					mDestino = MapUtils.getString(mapDestino,"SG_FILIAL");
				}
				else if(idAeroportoDestino  == null && idMunicipioDestino == null && idFilialDestino == null 
						&& idTipoLocMunicipioDestino != null){				
					
					List listaDestino = jdbcTemplate.queryForList(sqlTipoLocMunicipio.toString(), new BigDecimal[]{idTipoLocMunicipioDestino});
					Map  mapDestino   = listaDestino.isEmpty() ? new HashMap() : (Map)listaDestino.get(0);
					mDestino = MapUtils.getString(mapDestino,"DS_TIPO_LOCALIZACAO_MUNICIPIO");
					
				}
				else if (idMunicipioDestino == null && idFilialDestino == null && idTipoLocMunicipioDestino == null && idGrupoDestino != null){
					List listaOrigem = jdbcTemplate.queryForList(sqlGrupo.toString(), new BigDecimal[]{idGrupoDestino});
					Map  mapOrigem   = listaOrigem.isEmpty() ? new HashMap() : (Map)listaOrigem.get(0);
					mOrigem = MapUtils.getString(mapOrigem,"DS_GRUPO_REGIAO");
				}					
				
				
				//Concatena o uf da origem e do destino com "-" para ser usado no metodo verificaComplemento
				String ufOrigemDestino = ufOrigem+"-"+ufDestino;
				mOrigem 	= getTabelasClienteService().verificaComplemento(listaUFOrigem, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, mOrigem);
				mDestino 	= getTabelasClienteService().verificaComplemento(listaUFDestino, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, mDestino);
				
				
				origem  = mOrigem  != null ? ufOrigem + " - " + mOrigem : ufOrigem;
				destino = mDestino != null ? ufDestino + " - " + mDestino : ufDestino;
				
				
				//salva registro na tabela temporaria
				sqlInsert.append("INSERT INTO " + NOME_TABELA + " (ID_PARAMETRO_CLIENTE, TAXAMINIMA, PESOMINIMO, EXCEDENTE,");
				sqlInsert.append(" ORIGEM, DESTINO, GRUPO) VALUES(?,?,?,?,?,?,?)");
	
				jdbcTemplate.update(sqlInsert.toString(), new Object[]{idParametro, taxaMinima, pesoMinimo, excedenteKg, origem, destino, grupo});
			}
		    
		    getTabelasClienteService().verificaUfs(listaUFOrigem,"ORIGEM",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,jdbcTemplate);
		    getTabelasClienteService().verificaUfs(listaUFDestino,"DESTINO",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,jdbcTemplate);

		}
		return true;
	}
	

	

	/**
	 * Monta sql para consulta principal
	 * 
	 * @param parametros
	 * @return
	 */
	private String montaSql(List parametros, Long idSimulacao) {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT DISTINCT ")
	 	  .append("PC.ID_PARAMETRO_CLIENTE,")
	 	  .append("TP.ID_TABELA_PRECO,")
          .append("UF_ORIGEM.SG_UNIDADE_FEDERATIVA as UF_ORIGEM,")
          .append("UF_DESTINO.SG_UNIDADE_FEDERATIVA as UF_DESTINO,")
          
	 	  .append("PC.VL_MINIMO_FRETE_QUILO as TAXA_MINIMA,")
	 	  .append("PC.VL_MIN_FRETE_PESO as PESO_MINIMO,")
	 	  .append("PC.VL_FRETE_PESO as EXCEDENTEKG,")
	 	  
	 	  .append("PC.ID_AEROPORTO_ORIGEM,")
	 	  .append("PC.ID_MUNICIPIO_ORIGEM,")
	 	  .append("PC.ID_FILIAL_ORIGEM,")
	 	  .append("PC.ID_TIPO_LOC_MUNICIPIO_ORIGEM,")
	 	  .append("PC.ID_AEROPORTO_DESTINO,")
	 	  .append("PC.ID_MUNICIPIO_DESTINO,")
	 	  .append("PC.ID_FILIAL_DESTINO,")
	 	  .append("PC.ID_TIPO_LOC_MUNICIPIO_DESTINO, ")
		  .append("PC.ID_GRUPO_REGIAO_ORIGEM, ")
		  .append("PC.ID_GRUPO_REGIAO_DESTINO ")
	 	  
	 	  .append(" FROM CLIENTE C,")
	 	  .append("DIVISAO_CLIENTE DC,")
	 	  .append("TABELA_DIVISAO_CLIENTE TDC,")
	 	  .append("TABELA_PRECO TP,")
	 	  .append("PARAMETRO_CLIENTE PC,")
	 	  .append("UNIDADE_FEDERATIVA UF_ORIGEM,")
	 	  .append("UNIDADE_FEDERATIVA UF_DESTINO")

	 	  .append(" WHERE C.ID_CLIENTE = DC.ID_CLIENTE")
	      .append(" AND DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE");
		  if(idSimulacao == null){
			  sql.append(" AND TDC.ID_TABELA_DIVISAO_CLIENTE = PC.ID_TABELA_DIVISAO_CLIENTE");
		  }
	      
	      sql.append(" AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO")
	      .append(" AND PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA")
	      .append(" AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
		  
	      .append(" AND C.ID_CLIENTE = ? AND DC.ID_DIVISAO_CLIENTE = ?");
	      if(idSimulacao == null){
	    	  sql.append(" AND TDC.ID_TABELA_DIVISAO_CLIENTE = ?");
	      }else{
	    	  sql.append(" AND PC.ID_SIMULACAO = ").append(idSimulacao.longValue());
	      }

		StringBuffer idsParamCliente = new StringBuffer("");
		boolean primeiro = true;
		for (Iterator iter = parametros.iterator(); iter.hasNext();) {
			Long pc = (Long) iter.next();
			if(primeiro) {
				idsParamCliente.append(" SELECT * FROM (");
				idsParamCliente.append(" SELECT ");
				idsParamCliente.append(pc);
				idsParamCliente.append(" FROM DUAL ");
				primeiro = false;
			} else {
				idsParamCliente.append("UNION ALL SELECT ");
				idsParamCliente.append(pc);
				idsParamCliente.append(" FROM DUAL ");
			}
		}
		
		if (!idsParamCliente.toString().equalsIgnoreCase("")) {
			idsParamCliente.append(") ");
			sql.append(" AND PC.ID_PARAMETRO_CLIENTE in (").append(idsParamCliente).append(")");
		}

		sql.append("order by UF_ORIGEM.SG_UNIDADE_FEDERATIVA, UF_DESTINO.SG_UNIDADE_FEDERATIVA ");

		return sql.toString();
	}

		

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
		}
		
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
		}
		
		
	public EmitirTabelaTaxaCombustivelPortraitService getEmitirTabelaTaxaCombustivel() {
		return emitirTabelaTaxaCombustivel;
				}
			
		
	public void setEmitirTabelaTaxaCombustivel(
			EmitirTabelaTaxaCombustivelPortraitService emitirTabelaTaxaCombustivel) {
		this.emitirTabelaTaxaCombustivel = emitirTabelaTaxaCombustivel;
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


