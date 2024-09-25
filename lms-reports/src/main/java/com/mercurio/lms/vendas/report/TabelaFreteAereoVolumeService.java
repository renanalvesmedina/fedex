package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

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
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * ET: 30.03.02.35 Emitir Tabela de Frete Aéreo Volume
 *  
 * @author Baltazar Schirmer  
 *
 * @spring.bean id="lms.vendas.tabelaFreteAereoVolumeService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteAereoVolume.vm"
 *
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="271"
 * @spring.property name="crossTabBandWidths" value="448" 
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class TabelaFreteAereoVolumeService extends ReportServiceSupport {

	private static final String NOME_TABELA = "TMP_FRETE_AEREO_VOLUME";

	private ConfiguracoesFacade configuracoesFacade;
	private EmitirTabelaTaxaCombustivelLandscapeService emitirTabelaTaxaCombustivel;
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
		List<Map> data = getEmitirTabelasClienteDAO().findRelatorioFreteRodoviarioVolume(parameters);
		TypedFlatMap parametros = new TypedFlatMap();
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		for (Map map : data) {
			parametros = getCommonParameter(map);
			parametros.put("idParametroCliente", MapUtils.getLong(map,"listaParametros"));
			parametros.put("idTabelaDivisaoCliente", MapUtils.getLong(map,"idTabelaDivisao"));
			parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
		
	
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		jdbcTemplate.execute("DELETE FROM "+ NOME_TABELA);

		//Parametros recebidos da Action
		Long idTabelaPreco = MapUtils.getLong(parametros,"idTabelaPreco");
		Boolean isTabelaNova = MapUtils.getBoolean(parametros, "isTabelaNova", false);
		Long idTabelaDivisao = MapUtils.getLong(parametros,"idTabelaDivisao");
		Long idParametro = MapUtils.getLong(parametros,"idParametroCliente");
		
		//Por causa do processamento dentro de TabelasClienteUtil.getAllDefaultChoiceCrosstabReportParameters
		parameters.put("idParametro", idParametro);
		parameters.put("idTabelaPreco", idTabelaPreco);
		
		Simulacao simulacao = (Simulacao)MapUtils.getObject(parameters, "simulacao");

		if(idTabelaPreco == null){
			JRReportDataObject jr = executeQuery("SELECT * FROM "+ NOME_TABELA +" ",new HashMap());
			parameters.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{Integer.valueOf(1)});
			jr.setParameters(parameters);
			return null;
		}

		/* Obtém os dados do relatório principal */
		List list = jdbcTemplate.queryForList(createQuery(), new Long[]{idTabelaPreco, idTabelaPreco, idTabelaPreco});

		Map parametersColumn = new HashMap();
		Set crosstab = new LinkedHashSet();
       	populateTable(
       		list,
       		idParametro,
       		crosstab,
       		parametersColumn);

        int[] subreports = {
        	TabelasClienteService.SUBREPORT_COLETA_CLIENTE,
        	TabelasClienteService.SUBREPORT_ENTREGA_CLIENTE,
        	TabelasClienteService.SUBREPORT_FORMALIDADES_AEREO_VOLUME,
        	TabelasClienteService.SUBREPORT_GENERALIDADES,
        	TabelasClienteService.SUBREPORT_SERVICOSCONTRATADOS,
        	TabelasClienteService.SUBREPORT_SERVICOSNAOCONTRATADOS,
        	TabelasClienteService.SUBREPORT_TAXA_TERRESTRE
        };

        Map parametersReport = new HashMap();
        //não passa crosstab porque já montou os parametros das cols dinamicas em populateTable, devido a colunas vazias no meio de tabelas do tipo 'D'
       	parametersReport = getTabelasClienteService().getAllDefaultChoiceCrosstabReportParameters(parametros,NOME_TABELA,isTabelaNova,crosstab.size(),null,subreports,configuracoesFacade,jdbcTemplate);
      	parametersReport.putAll(parametersColumn);

      	String dsTipoServico = null;
        if(simulacao==null){
        	dsTipoServico = getTabelasClienteService().getTipoServico(idTabelaDivisao,jdbcTemplate);	
        } else {
			Servico serv = simulacao.getServico();
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(serv.getIdServico(), jdbcTemplate);
			dsTipoServico =  MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO");       	
        }

       	parametersReport.put("SERVICO",dsTipoServico);
       	parametersReport.put("DS_MOEDA",getTabelasClienteService().getMoeda(idTabelaPreco, jdbcTemplate));
       	//getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);

		//TAXA COMBUSTIVEL
       	Long idParametroCliente = MapUtils.getLong(parameters ,"idParametroCliente");
		Map mapTaxCombustivel = getTabelasClienteService().getSubRelTaxaCombustivel(idTabelaPreco, idParametroCliente, jdbcTemplate);
		List listTaxCombustivel = (List)mapTaxCombustivel.get("RESULT");
		Set subCrossTab = (Set)mapTaxCombustivel.get("SUBCROSSTAB");
		if(listTaxCombustivel !=null && !listTaxCombustivel.isEmpty()) {
			List listColumn = new ArrayList();
			listColumn.addAll(subCrossTab);
			String nomeSubRel = TabelasClienteService.PATH_TABELAPRECOS + "report/subReportTaxaCombustivel_Landscape_ct_" + subCrossTab.size() + ".jasper";

			parametersReport.put("SUBREPORTTAXACOMBUSTIVEL_PATH", nomeSubRel);
			parametersReport.put(TabelasClienteService.KEY_PARAMETER_TAXA_COMBUSTIVEL, new JRMapCollectionDataSource(listTaxCombustivel));
			for(int i=0; i<subCrossTab.size();i++) {
				mapTaxCombustivel.put("COLUMN"+(i+1), listColumn.get(i));
			}
			emitirTabelaTaxaCombustivel.setMapParameters(mapTaxCombustivel);
			parametersReport.put("SERVICE_TX_COMB", emitirTabelaTaxaCombustivel);
		}       	

		int dataCount = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM " + NOME_TABELA);
		parametersReport.put("TOTAL", Integer.valueOf(dataCount));       	
		getTabelasClienteService().setEspacoQuebra(15,12,dataCount,8,0,parametersReport);

        JRReportDataObject jr = executeQuery("SELECT * FROM "+ NOME_TABELA +" ",new HashMap());
        parametersReport.put(ReportServiceSupport.CT_NUMBER_OF_COLS,new Integer[]{Integer.valueOf(crosstab.size())});
        jr.setParameters(parametersReport);
        parameters.put(idTabelaPreco, parametersReport); 
	}
		return result;
	}

	private String createQuery(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT")
		   .append("   PESQ_A.vl_faixa_progressiva, \n" )
		   .append("   PESQ_A.tarifa, \n" )
		   .append("   PESQ_A.NR_COLUNA_DINAMICA, \n")
		   .append("   PESQ_A.vl_fixo, \n" )
		   .append("   PESQ_B.preco_frete, \n" )
		   .append("   PESQ_A.uf_origem, \n" )
		   .append("   PESQ_A.uf_destino, \n" )
		   .append("   PESQ_A.nf_origem, \n" )
		   .append("   PESQ_A.nf_destino, \n" )
		   .append("   PESQ_A.m_origem, \n" )
		   .append("   PESQ_A.m_destino, \n" )
		   .append("   PESQ_A.f_origem, \n" )
		   .append("   PESQ_A.f_destino, \n" )
		   .append("   PESQ_A.tlm_origem, \n" )
		   .append("   PESQ_A.tlm_destino, \n" )
		   .append("   PESQ_A.id_rota_preco \n" )
		   .append(" FROM \n" )

		   .append("   (SELECT \n" )
		   .append("      FX_PROG.vl_faixa_progressiva, \n" )
		   .append("      VAL_FX_PROG.vl_fixo, \n" )
		   .append("      RT_PRECO.id_rota_preco, \n" )
		   .append("	  tap.cd_tarifa_preco as tarifa, \n" )
		   .append("      COLUNA_DINAMICA.NR_COLUNA_DINAMICA, \n")
		   .append("      UF_ORIGEM.sg_unidade_federativa as uf_origem, \n" )
		   .append("      UF_DESTINO.sg_unidade_federativa as uf_destino, \n" )
		   .append("      PESSOA_ORIGEM.nm_fantasia as nf_origem, \n" )
		   .append("      PESSOA_DESTINO.nm_fantasia as nf_destino, \n" )
		   .append("      M_ORIGEM.NM_MUNICIPIO as m_origem, \n" )
		   .append("      M_DESTINO.NM_MUNICIPIO as m_destino, \n" )
		   .append("      A_ORIGEM.sg_aeroporto as f_origem, \n" )
		   .append("      A_DESTINO.sg_aeroporto as f_destino, \n" )
		   .append(PropertyVarcharI18nProjection.createProjection("  TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I", "tlm_origem")).append(" , \n")
		   .append(PropertyVarcharI18nProjection.createProjection("  TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I", "tlm_destino \n"))
		   //.append("      TLM_ORIGEM.ds_tipo_localizacao_municipio as tlm_origem," )
		   //.append("      TLM_DESTINO.ds_tipo_localizacao_municipio as tlm_destino" )
		   .append("	FROM tabela_preco TAB_PRECO, \n" )
		   .append("      tabela_preco_parcela TAB_PRECO_PARC, \n" )
		   .append("      parcela_preco PARC_PRECO, \n" )
		   .append("      faixa_progressiva FX_PROG, \n" )
		   .append("      valor_faixa_progressiva VAL_FX_PROG, \n" )
		   .append("      rota_preco RT_PRECO, \n" )
		   .append("      unidade_federativa UF_ORIGEM, \n" )
		   .append("      unidade_federativa UF_DESTINO, \n" )
		   .append("      aeroporto A_ORIGEM, \n" )
		   .append("      aeroporto A_DESTINO, \n" )
		   
		   .append("      pessoa P_ORIGEM, \n")
		   .append("      pessoa P_DESTINO, \n")
		   .append("      endereco_pessoa EP_ORIGEM, \n")
		   .append("      endereco_pessoa EP_DESTINO, \n")
		   .append("      municipio M_ORIGEM, \n")
		   .append("      municipio M_DESTINO, \n")
		   
		   .append("      pessoa PESSOA_ORIGEM, \n")
		   .append("      pessoa PESSOA_DESTINO, \n" )
		   .append("      filial F_ORIGEM, \n" )
		   .append("      filial F_DESTINO, \n" )
		   .append("      filial F1, \n" )
		   .append("      filial F2, \n" )
		   .append("      tipo_localizacao_municipio TLM_ORIGEM, \n" )
		   .append("      tipo_localizacao_municipio TLM_DESTINO, \n" )
		   .append("	  tarifa_preco tap,  \n" )
           .append("      tarifa_preco_rota tpr,  \n" )	

           .append("(SELECT TABCOLDIN.*, rownum as nr_coluna_dinamica \n")
           .append(" FROM(SELECT tp.id_tabela_preco, fp.id_faixa_progressiva, fp.vl_faixa_progressiva FROM faixa_progressiva fp,  \n")
           .append(" 	tabela_preco_parcela tpp,  tabela_preco tp  \n")
           .append(" WHERE fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela  \n")
           .append("   AND tpp.id_tabela_preco = tp.id_tabela_preco \n")
           .append("   AND fp.cd_minimo_progressivo = 'VO' \n")
           .append("   AND tp.id_tabela_preco = ?  \n")
           .append(" ORDER BY fp.vl_faixa_progressiva) TABCOLDIN) COLUNA_DINAMICA  \n")	                    

		   .append("	WHERE TAB_PRECO.id_tabela_preco = ? \n" )
		   
		   .append("      AND COLUNA_DINAMICA.id_tabela_preco = TAB_PRECO.id_tabela_preco \n" )
		   .append("      AND COLUNA_DINAMICA.id_faixa_progressiva  = FX_PROG.id_faixa_progressiva \n" )
		   .append("      AND TAB_PRECO_PARC.id_tabela_preco = TAB_PRECO.id_tabela_preco \n" )
		   .append("      AND PARC_PRECO.id_parcela_preco = TAB_PRECO_PARC.id_parcela_preco \n" )
		   .append("      AND PARC_PRECO.cd_parcela_preco = 'IDFretePeso' \n" )
		   .append("      AND TAB_PRECO_PARC.id_tabela_preco_parcela = FX_PROG.id_tabela_preco_parcela \n" )
		   .append("      AND FX_PROG.cd_minimo_progressivo = 'VO' \n" )
		   .append("      AND VAL_FX_PROG.id_faixa_progressiva = FX_PROG.id_faixa_progressiva \n" )
		   .append("      AND VAL_FX_PROG.id_tarifa_preco = tap.id_tarifa_preco \n" )
           .append("      AND tap.id_tarifa_preco = tpr.id_tarifa_preco \n" )
           .append("      AND tpr.id_tabela_preco = TAB_PRECO.id_tabela_preco \n" )
           .append("      AND tpr.id_rota_preco =  RT_PRECO.id_rota_preco \n" )
		   .append("      AND RT_PRECO.id_uf_origem = UF_ORIGEM.id_unidade_federativa \n" )
		   .append("      AND RT_PRECO.id_uf_destino = UF_DESTINO.id_unidade_federativa \n" )
		   .append("      AND RT_PRECO.id_aeroporto_origem = A_ORIGEM.id_aeroporto (+) \n" )
		   .append("      AND RT_PRECO.id_aeroporto_destino = A_DESTINO.id_aeroporto (+) \n")

		   .append("      AND A_ORIGEM.ID_AEROPORTO = P_ORIGEM.ID_PESSOA (+) \n" )
		   .append("      AND A_DESTINO.ID_AEROPORTO = P_DESTINO.ID_PESSOA (+) \n" )
		   
		   .append("      AND P_ORIGEM.ID_ENDERECO_PESSOA = EP_ORIGEM.ID_ENDERECO_PESSOA (+) \n" )
		   .append("      AND P_DESTINO.ID_ENDERECO_PESSOA = EP_DESTINO.ID_ENDERECO_PESSOA (+) \n" )

		   .append("      AND EP_ORIGEM.ID_MUNICIPIO = M_ORIGEM.ID_MUNICIPIO (+) \n" )
		   .append("      AND EP_DESTINO.ID_MUNICIPIO = M_DESTINO.ID_MUNICIPIO (+) \n" )
		   
		   
		   .append("      AND A_ORIGEM.id_filial_responsavel = F1.id_filial (+) \n")
		   .append("      AND A_DESTINO.id_filial_responsavel = F2.id_filial (+) \n")
		   .append("      AND F1.id_filial = PESSOA_ORIGEM.id_pessoa (+) \n")
		   .append("      AND F2.id_filial = PESSOA_DESTINO.id_pessoa (+) \n")
		   .append("      AND RT_PRECO.id_filial_origem = F_ORIGEM.id_filial (+) \n")
		   .append("      AND RT_PRECO.id_filial_destino = F_DESTINO.id_filial (+) \n")
		   .append("      AND RT_PRECO.id_tipo_localizacao_origem = TLM_ORIGEM.id_tipo_localizacao_municipio (+) \n")
		   .append("      AND RT_PRECO.id_tipo_localizacao_destino = TLM_DESTINO.id_tipo_localizacao_municipio (+) ) PESQ_A, \n")

		   .append("	(SELECT \n")
		   .append("      PRECO_FT.vl_preco_frete as preco_frete, \n")
		   .append("      RT_PRECO.id_rota_preco, \n")
		   .append("	  tap.cd_tarifa_preco as tarifa \n")
		   .append("	FROM tabela_preco TAB_PRECO, \n")
		   .append("      tabela_preco_parcela TAB_PRECO_PARC, \n")
		   .append("      parcela_preco PARC_PRECO, \n")
		   .append("      preco_frete PRECO_FT, \n")
		   .append("      rota_preco RT_PRECO, \n")
		   .append("	  tarifa_preco tap,  \n")
           .append("      tarifa_preco_rota tpr  \n")
		   .append("	WHERE TAB_PRECO.id_tabela_preco = ? \n")
		   .append("      AND TAB_PRECO.id_tabela_preco = TAB_PRECO_PARC.id_tabela_preco \n")
		   .append("      AND TAB_PRECO_PARC.id_parcela_preco = PARC_PRECO.id_parcela_preco \n")
		   .append("      AND PARC_PRECO.cd_parcela_preco = 'IDAdvalorem' \n")
		   .append("      AND TAB_PRECO_PARC.id_tabela_preco_parcela = PRECO_FT.id_tabela_preco_parcela \n")
		   .append("      AND PRECO_FT.id_tarifa_preco = tap.id_tarifa_preco \n")
           .append("      AND tap.id_tarifa_preco = tpr.id_tarifa_preco \n")
           .append("      AND tpr.id_tabela_preco = TAB_PRECO.id_tabela_preco \n")
           .append("      AND tpr.id_rota_preco =  RT_PRECO.id_rota_preco \n")
           .append("      ) PESQ_B \n")

		   .append(" WHERE PESQ_A.id_rota_preco = PESQ_B.id_rota_preco \n")
		   .append("        ORDER BY tarifa, vl_faixa_progressiva,  \n")
		   .append("        uf_origem, nf_origem, m_origem, f_origem, \n" )
		   .append("        uf_destino, nf_destino, m_destino, f_destino \n");
		return sql.toString();
	}

	private void populateTable(List<Map> data, Long idParametro, Set crosstab, Map parametersReport){
		Map tabelas = new HashMap();
		List<String> ufOrigemList = new ArrayList<String>();
		List<String> ufDestinoList = new ArrayList<String>();
		List<String> insertQuery = new ArrayList<String>();

		StringBuffer columns = null;
		StringBuffer values = null;
		String query = "";

	    String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
		String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");
		Map parametros = getTabelasClienteService().buscaDadosParametroCliente(idParametro,getJdbcTemplate());

	    for (Map map : data) {
	    	String keyData = MapUtils.getString(map,"id_rota_preco");

			/** Header para nova Rota */
	   		if (!tabelas.containsKey(keyData)){
	   			String tarifa = MapUtils.getString(map,"tarifa");
	   			String ufOrigem = MapUtils.getString(map,"uf_origem");
				String ufDestino = MapUtils.getString(map,"uf_destino");
				String ufRota = ufOrigem+"-"+ufDestino;

				/** Endereços */
				String enderecoOrigem = trataLogradouro(
					MapUtils.getString(map,"nf_origem"), 
				    MapUtils.getString(map,"m_origem"), 
					MapUtils.getString(map,"f_origem"), 
					MapUtils.getString(map,"tlm_origem"));
				String enderecoDestino = trataLogradouro(MapUtils.getString(map,"nf_destino"),
					MapUtils.getString(map,"m_destino"), 
					MapUtils.getString(map,"f_destino"), 
					MapUtils.getString(map,"tlm_destino"));

				/** Complementos */
				String complementoOrigem = getTabelasClienteService().verificaComplemento(
					ufOrigemList,
					msgTodoEstado,
					msgDemaisLocalidades,
					ufRota,
					enderecoOrigem);
				String complementoDestino = getTabelasClienteService().verificaComplemento(
					ufDestinoList,
					msgTodoEstado,
					msgDemaisLocalidades,
					ufRota,
					enderecoDestino);

				/** Rotas */
				String rotaOrigem = ufOrigem+" - "+complementoOrigem;
				String rotaDestino = ufDestino+" - "+complementoDestino;

	   			if(columns != null){
	   				columns.append(")");
	   			}
	   			if(values != null){
	   				values.append(")");
	   				query = columns.toString() + values.toString();
	   				insertQuery.add(query);
	   			}

	   			BigDecimal adValorem = MapUtilsPlus.getBigDecimal(map,"preco_frete");
		   		adValorem = getTabelasClienteService().aplicaRegraAdvalorem(parametros,adValorem);
	   			columns = new StringBuffer("INSERT INTO " + NOME_TABELA + " (TARIFA, ORIGEM, DESTINO, ADVALOREM");
		   		values = new StringBuffer(" VALUES('"+ tarifa +"','"+ rotaOrigem +"','"+ rotaDestino +"', "+ adValorem);

		   		tabelas.put(keyData, new String());
		   	}

	   		/** Colunas dinamicas */
	   		Object nrColunaDin = map.get("NR_COLUNA_DINAMICA");
			crosstab.add(MapUtils.getObject(map, "vl_faixa_progressiva"));
			parametersReport.put("PCOLUMN"+nrColunaDin, MapUtils.getString(map, "vl_faixa_progressiva"));

			columns.append(", COLUMN"+nrColunaDin);
			values.append(", "+MapUtils.getObject(map, "vl_fixo"));
		}

		/** Fecha SQL */
		if(columns != null){
			columns.append(")");
		}
		if(values != null){
			values.append(")");
			query = columns.toString() + values.toString();
			insertQuery.add(query);
		}

		/** Executa Inserts */
		for (String strInsert : insertQuery) {
			getJdbcTemplate().execute(strInsert);		
		}

		getTabelasClienteService().verificaUfs(ufOrigemList,"ORIGEM",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,getJdbcTemplate());
	    getTabelasClienteService().verificaUfs(ufDestinoList,"DESTINO",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,getJdbcTemplate());
	}

	private String trataLogradouro(String val1, String val2, String val3, String val4){
		if (val2 != null && val3 != null) return val3+" - "+val2;
		else if (val1 != null) return val1;
		else if (val2 != null) return val2;
		else if (val3 != null) return val3;
		else if (val4 != null) return val4;
		else return null;
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
	public void setEmitirTabelaTaxaCombustivel(EmitirTabelaTaxaCombustivelLandscapeService emitirTabelaTaxaCombustivel) {
		this.emitirTabelaTaxaCombustivel = emitirTabelaTaxaCombustivel;
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
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;


}