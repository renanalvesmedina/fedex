package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.tabelaprecos.report.EmitirTabelaEcommerceDiferenciadaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;
import com.mercurio.lms.vendas.util.TabelasClienteUtil;

/**
 * 
 * ET: 30.03.02.18 - Tabela Frete Minimo Progressivo com Peso Excedente 
 * 
 * @author Alexandre Poletto	
 *
 * @spring.bean id="lms.vendas.report.tabelaFreteMinimoProgressivoPesoExcedenteService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabFreteMinProgPesoExcedente.vm"
 * 
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="326"
 * @spring.property name="crossTabBandWidths" value="366" 
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class TabelaFreteMinimoProgressivoPesoExcedenteService extends ReportServiceSupport {

	private static final String NOME_TABELA = "TMP_MN_PROGS_PESO_EXCEDENTES";
	
	private ConfiguracoesFacade configuracoesFacade;
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	private EmitirTabelaEcommerceDiferenciadaService emitirTabelaEcommerceDiferenciadaService;
	private TabelasClienteService tabelasClienteService;

	Set crosstab;
	
	ThreadLocal thrLoc = new ThreadLocal();
	
	public Map getTabelas() {
		return (Map) TabelasClienteUtil.getLocalVariableValue(thrLoc,"tabelas");
	}

	public void setTabelas(Map tabelas) {
		TabelasClienteUtil.setLocalVariableValue(thrLoc,"tabelas", tabelas);
	}
	
	public Set getCrosstab() {
		return (Set) TabelasClienteUtil.getLocalVariableValue(thrLoc,"crosstab");
	}

	public void setCrosstab(Set crosstab) {
		TabelasClienteUtil.setLocalVariableValue(thrLoc,"crosstab", crosstab);
	}

	
	public JRReportDataObject execute(Map parameters) throws Exception {
		return null;
	}
		
	public List<Map<String, String>> findDados(TypedFlatMap parameters) throws Exception {
		
			List<Map> data = getEmitirTabelasClienteDAO().findRelatorioMinimoProgPorPesoExcedente(parameters);
			TypedFlatMap parametros = new TypedFlatMap();

			if(data == null || data.isEmpty()){
				return null;
			}
			List<Map<String, String>> result = new ArrayList<Map<String,String>>();
			
			for (Map map : data) {
				parametros = getCommonParameter(map);
				parametros.put("idParametroCliente", MapUtils.getLong(map,"listaParametros"));
				parametros.put("idTabelaDivisao", MapUtils.getLong(map,"idTabelaDivisao"));
				parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
				parameters.put("idCliente", MapUtils.getLong(map,"idCliente"));
				parameters.put("idDivisao", MapUtils.getLong(map,"idDivisao"));
				parameters.put("idTabelaPreco", MapUtils.getLong(map,"idTabelaPreco"));
			}	
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		jdbcTemplate.execute("DELETE FROM "+ NOME_TABELA);
		
		//Parametros recebidos da Action
			Long idTabelaPreco = MapUtils.getLong(parametros,"idTabelaPreco");
			Boolean isTabelaNova = MapUtils.getBoolean(parametros, "isTabelaNova", false);
			Long idTabelaDivisao = MapUtils.getLong(parametros,"idTabelaDivisao");
			Long idParametro = MapUtils.getLong(parametros,"idParametroCliente");
			Simulacao simulacao = (Simulacao)MapUtils.getObject(parametros, "simulacao");
		
			Long[] ids = new Long[]{idTabelaPreco,idTabelaPreco,idTabelaPreco,idTabelaPreco};

		/* Obtém os dados do relatório principal */
			List list = jdbcTemplate.queryForList(emitirTabelaEcommerceDiferenciadaService.createQuery(parametros),ids);
        
			Map parametersColumn = new HashMap(); 
			list = emitirTabelaEcommerceDiferenciadaService.populateTable(crosstab, list, parametersColumn);
       
        int[] subreports = {
        		TabelasClienteService.SUBREPORT_FORMALIDADES,
        		TabelasClienteService.SUBREPORT_GENERALIDADES,
        		TabelasClienteService.SUBREPORT_SERVICOSCONTRATADOS,
	        	TabelasClienteService.SUBREPORT_SERVICOSNAOCONTRATADOS
	        };
	        parameters.putAll(parametros);
        
	        String dsTipoServico = null;
	        if(simulacao==null){
	        	dsTipoServico = getTabelasClienteService().getTipoServico(idTabelaDivisao,jdbcTemplate);	
	        }else{
				Servico serv = simulacao.getServico();
				Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(serv.getIdServico(), jdbcTemplate);
				dsTipoServico =  MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO");       	
	        }
        
       	
	        Map parametersReport = new HashMap();
	      
	       	parametersReport = getTabelasClienteService().getAllDefaultChoiceCrosstabReportParameters(parameters,NOME_TABELA,isTabelaNova,0,crosstab,subreports,configuracoesFacade,jdbcTemplate);
	       	parametersReport.putAll(parametersColumn);

	       	parametersReport.put("SERVICO",dsTipoServico);
	       	parametersReport.put("DS_MOEDA",getTabelasClienteService().getMoeda(idTabelaPreco, jdbcTemplate));
	       	parametersReport.put("TOTAL",Integer.valueOf(getJdbcTemplate().queryForInt(" SELECT COUNT(*) AS TTT FROM " + NOME_TABELA)));
	        


	        getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);

	        
	        for (Object object : list) {
	        	Map map = (Map)object;
	        	map.put("grupo","grupo0");
	        	if(MapUtilsPlus.getBigDecimal(map,"VL_FAIXA_PROGRESSIVA").intValue() < 201){
					 map.put("VL_FIXO", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VALOR_FIXO")));				 
				 }else{
					 map.put("VL_FIXO", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map,"VALOR_FIXO")));
		}
			    	map.put("VL_ADVALOREM", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VL_ADVALOREM")));
			    	map.put("FRETE_QUILO", FormatUtils.formatDecimal(TabelasClienteService.FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map,"FRETEPORKG")));
			}
		
	        parameters.put("grupos", parametersReport.get("grupos"));
	        result.add(parametersReport);
	        result.addAll(list);
	        result.add(parameters);
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
	
	private void populateTable(List list,Long idParametro) {
		List listaUFOrigem = new LinkedList();
		List listaUFDestino = new LinkedList();
		List sqlRegistros = new LinkedList();
		List sqlValues = new LinkedList();
		StringBuffer colunas = null;
		StringBuffer valuesStr = null;
		List values = new LinkedList();
		String sql = "";
	    setCrosstab(new LinkedHashSet());
	    setTabelas(new HashMap());
	    Map mapParametro = getTabelasClienteService().buscaDadosParametroCliente(idParametro,getJdbcTemplate());
	    String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
	    String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");
	    
	    int count = 1;
	    for (Iterator it = list.iterator(); it.hasNext();count++ ) { 
	    	Map map = (Map)it.next();
	        String rotaPreco = MapUtils.getString(map,"FAIXA_ROTA");
	        String mOrigem   = MapUtils.getString(map,"ORIGEM");
		    String mDestino  = MapUtils.getString(map,"DESTINO");
		    String ufOrigem  = MapUtils.getString(map,"UF_ORIGEM");
			String ufDestino = MapUtils.getString(map,"UF_DESTINO");
			String tarifa 	= MapUtils.getString(map,"TARIFA");
			Object column    = map.get("VALOR_FAIXA");
			Object excedente = map.get("VALOR_EXCEDENTE");
			Object dado      = map.get("VL_FIXO"); 
			BigDecimal adv   = MapUtilsPlus.getBigDecimal(map,"VALOR_ADVALOREM");
			String chave = rotaPreco+tarifa;
			//Concatena o uf da origem e do destino com "-" para ser usado no metodo verificaComplemento
			String ufOrigemDestino = ufOrigem+"-"+ufDestino;
			mOrigem 	= getTabelasClienteService().verificaComplemento(listaUFOrigem, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, mOrigem);
			mDestino 	= getTabelasClienteService().verificaComplemento(listaUFDestino, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, mDestino);
			String origem = ufOrigem + " - " + mOrigem;
			String destino = ufDestino + " - " + mDestino;
		        
	   		if (!getTabelas().containsKey(chave)){
	   			count = 1;
				//aplica as regras do parametro do cliente no advalorem
				adv = getTabelasClienteService().aplicaRegraAdvalorem(mapParametro,adv);
	   			if(colunas != null){
	   				colunas.append(")\n");
	   			}
	   			if(valuesStr != null){
	   				valuesStr.append(")\n");
	   				sql = colunas.toString() + valuesStr.toString();
	   				sqlRegistros.add(sql);
	   				sqlValues.add(values.toArray());
	   			}
	   			colunas = new StringBuffer("INSERT INTO " + NOME_TABELA + " (ORIGEM, DESTINO, ADVALOREM, VL_EXCEDENTE, TARIFA");
	   			valuesStr = new StringBuffer(" VALUES(?, ?, ?, ?, ?");
	   			values = new LinkedList();
	   			values.add(origem);
	   			values.add(destino);
	   			values.add(adv);
	   			values.add(excedente);
	   			values.add(tarifa);
	   			
	   			getTabelas().put(chave, new ArrayList());
	   		}
			getCrosstab().add(column);
			colunas.append(", COLUMN"+count);
			valuesStr.append(", ?");
			values.add(dado);
		}
			
		//finaliza o ultimo registro
		if(colunas != null){
			colunas.append(")\n");
		}
		if(valuesStr != null){
			valuesStr.append(")\n");
			sql = colunas.toString() + valuesStr.toString();
			sqlRegistros.add(sql);
			sqlValues.add(values.toArray());
		}
		Iterator iterValues = sqlValues.iterator();
		for (Iterator iter = sqlRegistros.iterator(); iter.hasNext();) {
			String sqlInsert = (String) iter.next();
			getJdbcTemplate().update(sqlInsert, (Object[])iterValues.next());
			
		}
		
		getTabelasClienteService().verificaUfs(listaUFOrigem,"ORIGEM",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,getJdbcTemplate());
		getTabelasClienteService().verificaUfs(listaUFDestino,"DESTINO",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,getJdbcTemplate());
    }
	
	private String montaSql(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select ")
		.append("	Faixa.VL_FIXO,")
		.append("	Faixa.faixa_rota,")
		.append("	Faixa.UF_Origem,")
		.append("	Faixa.UF_Destino,")
		.append("	Faixa.Origem,")
		.append("	Faixa.Destino,")
		.append("	Faixa.Tarifa,")
		.append("	Faixa.vl_faixa_progressiva as Valor_faixa,")
		.append("	Excedente.valor_Excedente,")
		.append("	Advalorem.valor_Advalorem")
		.append(" from")
		.append("	(select ")
		.append("		vfp.vl_fixo,")
		.append("		Fp.vl_faixa_progressiva,")
		.append("		tap.cd_tarifa_preco as tarifa,")
		.append("		uf_origem.sg_unidade_federativa as UF_Origem,")
		.append("		uf_destino.sg_unidade_federativa as UF_Destino,")
		.append("		rp.id_rota_preco as faixa_rota,")
		.append("		decode(RP.ID_MUNICIPIO_ORIGEM,null,decode(F_ORIGEM.SG_FILIAL,null,").append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",F_ORIGEM.SG_FILIAL||' - '||P_ORIGEM.NM_FANTASIA),M_ORIGEM.NM_MUNICIPIO) as ORIGEM,")
		.append("		decode(RP.ID_MUNICIPIO_DESTINO,null,decode(F_DESTINO.SG_FILIAL,null,").append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",F_DESTINO.SG_FILIAL||' - '||P_DESTINO.NM_FANTASIA),M_DESTINO.NM_MUNICIPIO) as DESTINO")
		.append("	from")
		.append("		tabela_preco tp,")
		.append("		tipo_tabela_preco ttp,")
		.append("		subtipo_tabela_preco stp,")
		.append("		tabela_preco_parcela tpp,")
		.append("		parcela_preco pp,")
		.append("		faixa_progressiva fp,")
		.append("		valor_faixa_progressiva vfp,")
		.append("		rota_preco rp,")
		.append("		unidade_federativa uf_origem,")
		.append("		unidade_federativa uf_destino,")
		.append("		MUNICIPIO M_ORIGEM, ")
		.append("		MUNICIPIO M_DESTINO,")
		.append("		TIPO_LOCALIZACAO_MUNICIPIO TLM_ORIGEM, ")
		.append("		TIPO_LOCALIZACAO_MUNICIPIO TLM_DESTINO, ")
		.append("		FILIAL F_ORIGEM, ")
		.append("		FILIAL F_DESTINO, ")
		.append("		PESSOA P_ORIGEM, ")
		.append("		PESSOA P_DESTINO, ")
		.append("		TARIFA_PRECO tap,")
		.append("		TARIFA_PRECO_ROTA tpr")
		.append("	where")
		.append("		tp.id_tabela_preco = ? ")
		.append("		and tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco")
		.append("		and tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco")
		.append("		and tp.id_tabela_preco = tpp.id_tabela_preco")
		.append("		and tpp.id_parcela_preco = pp.id_parcela_preco")
		.append("		and pp.cd_parcela_preco = 'IDFretePeso'")
		.append("		and tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela")
		.append("		and fp.id_faixa_progressiva = vfp.id_faixa_progressiva")
		.append("		and fp.TP_INDICADOR_CALCULO = 'VL'")
		.append("		and fp.cd_minimo_progressivo = 'PE'")
		.append("		and vfp.id_tarifa_preco = tap.id_tarifa_preco")
        .append("		and tpr.id_tarifa_preco = tap.id_tarifa_preco")
        .append("		and tpr.id_tabela_preco = tp.id_tabela_preco")
		.append("		and tpr.id_rota_preco = rp.id_rota_preco")
		.append("		AND  RP.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA") 	
		.append("		AND  RP.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA") 	
		.append("		AND  RP.ID_MUNICIPIO_ORIGEM = M_ORIGEM.ID_MUNICIPIO (+)")
		.append("		AND  RP.ID_MUNICIPIO_DESTINO = M_DESTINO.ID_MUNICIPIO (+)")
		.append("		AND  RP.ID_FILIAL_ORIGEM = F_ORIGEM.ID_FILIAL (+)")
		.append("		AND  RP.ID_FILIAL_DESTINO = F_DESTINO.ID_FILIAL (+)")
		.append("		AND  F_ORIGEM.ID_FILIAL = P_ORIGEM.ID_PESSOA (+)")
		.append("		AND  F_DESTINO.ID_FILIAL = P_DESTINO.ID_PESSOA (+)")
		.append("		AND  RP.ID_TIPO_LOCALIZACAO_ORIGEM = TLM_ORIGEM.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)")
		.append("		AND  RP.ID_TIPO_LOCALIZACAO_DESTINO = TLM_DESTINO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)) Faixa, ")
		.append("	(select ")
		.append("		pf.vl_preco_frete as valor_Excedente, rp.id_rota_preco as frete_rota,")
		.append("		tap.cd_tarifa_preco as tarifa")
		.append("	from  tabela_preco tp,")
		.append("		tipo_tabela_preco ttp,")
		.append("		subtipo_tabela_preco stp,")
		.append("		tabela_preco_parcela tpp,")
		.append("		parcela_preco pp,")
		.append("		preco_frete pf,")
		.append("		rota_preco rp,")
		.append("		TARIFA_PRECO tap,")
		.append("		TARIFA_PRECO_ROTA tpr")
		.append("	where tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco")
		.append("		and tp.id_tabela_preco = ? ")
		.append("		and tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco")
		.append("		and tp.id_tabela_preco = tpp.id_tabela_preco")
		.append("		and tpp.id_parcela_preco = pp.id_parcela_preco")
		.append("		and pp.cd_parcela_preco = 'IDFreteQuilo'")
		.append("		and tpp.id_tabela_preco_parcela = pf.id_tabela_preco_parcela")
		.append("		and pf.id_tarifa_preco = tap.id_tarifa_preco")
        .append("		and tpr.id_tarifa_preco = tap.id_tarifa_preco")
        .append("		and tpr.id_tabela_preco = tp.id_tabela_preco")
		.append("		and tpr.id_rota_preco = rp.id_rota_preco")
		.append("		) Excedente,")
	    .append("	(select") 
		.append("		pf.vl_preco_frete as VALOR_Advalorem, rp.id_rota_preco as advalorem_rota,")
		.append("		tap.cd_tarifa_preco as tarifa")
		.append("	from  tabela_preco tp,")
		.append("		tipo_tabela_preco ttp,")
		.append("		subtipo_tabela_preco stp,")
		.append("		tabela_preco_parcela tpp,")
		.append("		parcela_preco pp,")
		.append("		preco_frete pf,")
		.append("		rota_preco rp,")
		.append("		TARIFA_PRECO tap,")
		.append("		TARIFA_PRECO_ROTA tpr")
		.append("	where tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco")
		.append("		and tp.id_tabela_preco = ? ")
		.append("		and tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco")
		.append("		and tp.id_tabela_preco = tpp.id_tabela_preco")
		.append("		and tpp.id_parcela_preco = pp.id_parcela_preco")
		.append("		and pp.cd_parcela_preco = 'IDAdvalorem'")
		.append("		and tpp.id_tabela_preco_parcela = pf.id_tabela_preco_parcela")
		.append("		and pf.id_tarifa_preco = tap.id_tarifa_preco")
        .append("		and tpr.id_tarifa_preco = tap.id_tarifa_preco")
        .append("		and tpr.id_tabela_preco = tp.id_tabela_preco")
		.append("		and tpr.id_rota_preco = rp.id_rota_preco")
		.append("		) Advalorem")
		.append(" where Faixa.faixa_rota = Excedente.frete_rota") 	  
		.append("	   and Faixa.faixa_rota = Advalorem.advalorem_rota") 
		.append("	   and FAIXA.tarifa = Excedente.TARIFA")
		.append("	   and FAIXA.tarifa = Advalorem.TARIFA")
		.append("	   Order by Faixa.TARIFA, Faixa.faixa_rota, valor_faixa,UF_Origem,Origem,Uf_Destino,Destino");
		return sql.toString();
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

	public EmitirTabelaEcommerceDiferenciadaService getEmitirTabelaEcommerceDiferenciadaService() {
		return emitirTabelaEcommerceDiferenciadaService;
	}

	public void setEmitirTabelaEcommerceDiferenciadaService(
			EmitirTabelaEcommerceDiferenciadaService emitirTabelaEcommerceDiferenciadaService) {
		this.emitirTabelaEcommerceDiferenciadaService = emitirTabelaEcommerceDiferenciadaService;
	}

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}
}