package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.mercurio.lms.tabelaprecos.report.EmitirTabelaEcommerceDiferenciadaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * @author Baltazar Schirmer
 *	
 *	ET: 30.03.02.37 Emitir Tabela de Frete Diferenciada
 *
 * @spring.bean id="lms.vendas.tabelaFreteDiferenciadaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteDiferenciada.vm"
 *
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="302"
 * @spring.property name="crossTabBandWidths" value="373" 
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 * 
 */
public class TabelaFreteDiferenciadaService extends ReportServiceSupport {

	private static final String NOME_TABELA = "TMP_FRETE_DIFERENCIADA";
	Set crosstab;
	private ConfiguracoesFacade configuracoesFacade;
	private EmitirTabelaEcommerceDiferenciadaService emitirTabelaEcommerceDiferenciadaService;
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
		List<Map> data = getEmitirTabelasClienteDAO().findRelatorioFreteDiferenciada(parameters);
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
        result.addAll(list);
        result.add(parametersReport);
        result.add(parameters);
		return result;
	}

	private String montaSql(){
		StringBuilder sql = new StringBuilder();
		sql.append(" select \n")
		.append("   PESQ_A.vl_faixa_progressiva, \n")
		.append("   PESQ_A.vl_fixo, \n")
		.append("   PESQ_A.tarifa, \n")
		.append("   PESQ_A.NR_COLUNA_DINAMICA, \n")
		.append("   PESQ_B.frete_advalorem, \n")
		.append("   PESQ_C.frete_quilo, \n")
		.append("   PESQ_A.uf_origem, \n" )
		.append("   PESQ_A.uf_destino, \n")
		.append("   PESQ_A.m_origem, \n")
		.append("   PESQ_A.m_destino, \n")
		.append("   PESQ_A.f_origem, \n")
		.append("   PESQ_A.f_destino, \n")
		.append("   PESQ_A.tlm_origem, \n")
		.append("   PESQ_A.tlm_destino, \n")
		.append("   PESQ_A.id_rota_preco \n")
		.append(" from \n")
		.append("   (select \n")
		.append("     FX_PROG.vl_faixa_progressiva, \n")
		.append("     VAL_FX_PROG.vl_fixo, \n")
		.append("     RT_PRECO.id_rota_preco, \n")
		.append("     TAR_PRECO.cd_tarifa_preco as tarifa, \n")
		.append("     COLUNA_DINAMICA.NR_COLUNA_DINAMICA, \n")
		.append("     VAL_FX_PROG.id_tarifa_preco, \n")
		.append("     UF_ORIGEM.sg_unidade_federativa as uf_origem, \n")
		.append("     UF_DESTINO.sg_unidade_federativa as uf_destino, \n")
		.append("     M_ORIGEM.nm_municipio as m_origem, \n")
		.append("     M_DESTINO.nm_municipio as m_destino, \n")				   
		.append("     F_ORIGEM.sg_filial ||  ' - ' || pfo.nm_fantasia as f_origem,  \n")				   
		.append("     F_DESTINO.sg_filial ||  ' - ' || pfd.nm_fantasia as f_destino,  \n")
		.append(PropertyVarcharI18nProjection.createProjection("  TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I", "tlm_origem")).append(" , \n")
		.append(PropertyVarcharI18nProjection.createProjection("  TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I", "tlm_destino \n"))
		.append("   from  \n")
		.append("     tabela_preco TAB_PRECO, \n")
		.append("     tabela_preco_parcela TAB_PRECO_PARC, \n")
		.append("	  parcela_preco PARC_PRECO, \n")
		.append("     faixa_progressiva FX_PROG, \n")
		.append("	  valor_faixa_progressiva VAL_FX_PROG, \n")
		.append("	  rota_preco RT_PRECO, \n")
		.append("     tarifa_preco TAR_PRECO,")
		.append("	  tarifa_preco_rota TAR_P_ROTA, \n")
		.append("     unidade_federativa UF_ORIGEM, \n")
		.append("     unidade_federativa UF_DESTINO, \n")
		.append("     municipio M_ORIGEM, \n")
		.append("     municipio M_DESTINO, \n")
		.append("     filial F_ORIGEM, \n")
		.append("     filial F_DESTINO, \n")
		.append("     tipo_localizacao_municipio TLM_ORIGEM, \n")
		.append("     tipo_localizacao_municipio TLM_DESTINO, \n")
		.append("     pessoa pfo,  pessoa pfd,  \n")
		
		.append("(select TABCOLDIN.*, rownum as nr_coluna_dinamica \n")
		.append(" from(select tp.id_tabela_preco, fp.id_faixa_progressiva, fp.vl_faixa_progressiva from faixa_progressiva fp,  \n")
		.append(" tabela_preco_parcela tpp,  tabela_preco tp  \n")
		.append(" where  fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela  \n")
		.append(" and tpp.id_tabela_preco = tp.id_tabela_preco  and tp.id_tabela_preco = ?  \n")
		.append(" order by fp.vl_faixa_progressiva) TABCOLDIN) COLUNA_DINAMICA  \n")		
		
		.append("   where  \n")
		.append("	 TAB_PRECO.id_tabela_preco = ? \n")
		.append("	 and TAB_PRECO.id_tabela_preco = TAB_PRECO_PARC.id_tabela_preco \n")
		.append("	 and TAB_PRECO_PARC.id_parcela_preco = PARC_PRECO.id_parcela_preco \n")
		.append("	 and PARC_PRECO.cd_parcela_preco = 'IDFretePeso' \n")
		.append("	 and TAB_PRECO_PARC.id_tabela_preco_parcela = FX_PROG.id_tabela_preco_parcela \n")
		.append("    and COLUNA_DINAMICA.id_faixa_progressiva = FX_PROG.id_faixa_progressiva \n")
		.append("	 and FX_PROG.id_faixa_progressiva = VAL_FX_PROG.id_faixa_progressiva \n")
		.append(" 	 and FX_PROG.cd_minimo_progressivo = 'PE' \n") //ajuste
		.append("	 and VAL_FX_PROG.id_tarifa_preco = TAR_PRECO.id_tarifa_preco \n")
		.append("    and TAR_P_ROTA.id_tarifa_preco = TAR_PRECO.id_tarifa_preco \n")
		.append("    and TAR_P_ROTA.id_tabela_preco = TAB_PRECO.id_tabela_preco \n")
		.append("    and TAR_P_ROTA.id_rota_preco = RT_PRECO.id_rota_preco \n")
		.append("    and RT_PRECO.id_uf_origem = UF_ORIGEM.id_unidade_federativa   (+)  \n")
		.append("    and RT_PRECO.id_uf_destino = UF_DESTINO.id_unidade_federativa (+)  \n")
		.append("    and RT_PRECO.id_municipio_origem = M_ORIGEM.id_municipio (+) \n")
		.append("    and RT_PRECO.id_municipio_destino = M_DESTINO.id_municipio (+) \n")
		.append("    and RT_PRECO.id_filial_origem = F_ORIGEM.id_filial (+) \n")
		.append("    and RT_PRECO.id_filial_destino = F_DESTINO.id_filial (+) \n")
		.append("    and RT_PRECO.id_tipo_localizacao_origem = TLM_ORIGEM.id_tipo_localizacao_municipio (+) \n")
		.append("    and F_ORIGEM.id_filial = pfo.id_pessoa  (+)  \n")
		.append("    and F_DESTINO.id_filial = pfd.id_pessoa (+)  \n")
		.append("    and RT_PRECO.id_tipo_localizacao_destino = TLM_DESTINO.id_tipo_localizacao_municipio (+) ) PESQ_A, \n")				    
		.append("   (select  \n")
		.append("	 PRECO_FT.vl_preco_frete as frete_advalorem, \n")
		.append("     PRECO_FT.id_tarifa_preco  \n")
		.append("   from  \n")
		.append("	 tabela_preco TAB_PRECO, \n")
		.append("     tabela_preco_parcela TAB_PRECO_PARC, \n")
		.append("     parcela_preco PARC_PRECO, \n")
		.append("	 preco_frete PRECO_FT  \n")
		.append("   where \n")
		.append("	 TAB_PRECO.id_tabela_preco = ? \n")
		.append("	 and TAB_PRECO.id_tabela_preco = TAB_PRECO_PARC.id_tabela_preco \n")
		.append("	 and TAB_PRECO_PARC.id_parcela_preco = PARC_PRECO.id_parcela_preco \n")
		.append("	 and PARC_PRECO.cd_parcela_preco = 'IDAdvalorem' \n")
		.append("	 and TAB_PRECO_PARC.id_tabela_preco_parcela = PRECO_FT.id_tabela_preco_parcela) PESQ_B, \n")
		.append("   (select  \n")
		.append("	 PRECO_FT.vl_preco_frete as frete_quilo, \n")
		.append("	 PRECO_FT.id_tarifa_preco  \n")
		.append("   from \n")
		.append("	 tabela_preco TAB_PRECO, \n")
		.append("	 tabela_preco_parcela TAB_PRECO_PARC, \n")
		.append("	 parcela_preco PARC_PRECO, \n")
		.append("     preco_frete PRECO_FT  \n")
		.append("   where \n")
		.append("	 TAB_PRECO.id_tabela_preco = ? \n")
		.append("	 and TAB_PRECO.id_tabela_preco = TAB_PRECO_PARC.id_tabela_preco \n")
		.append("	 and TAB_PRECO_PARC.id_parcela_preco = PARC_PRECO.id_parcela_preco \n")
		.append("	 and PARC_PRECO.cd_parcela_preco = 'IDFreteQuilo' \n")
		.append("     and TAB_PRECO_PARC.id_tabela_preco_parcela = PRECO_FT.id_tabela_preco_parcela) PESQ_C \n")
		.append(" where \n")
		.append("   PESQ_A.id_tarifa_preco = PESQ_B.id_tarifa_preco \n")
		.append("   and PESQ_A.id_tarifa_preco = PESQ_C.id_tarifa_preco \n")
		.append(" order by id_rota_preco, vl_faixa_progressiva, tarifa, \n")
		.append("   uf_origem, m_origem, uf_destino, m_destino");
		return sql.toString();
	}

	private void populateTable(List list, Long idParametro, Map parameters){
		List listaUFOrigem = new ArrayList();
		List listaUFDestino = new ArrayList();
		List sqlRegistros = new ArrayList();

		Map mapParametro = getTabelasClienteService().buscaDadosParametroCliente(idParametro,getJdbcTemplate());

		StringBuffer colunas = null;
		StringBuffer values = null;
		String sql = "";

	    crosstab = new LinkedHashSet();

	    String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
		String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");

	    int count = 1;
	    for (Iterator it = list.iterator(); it.hasNext();count++ ) { 
	    	Map map = (Map)it.next();

	    	String rota = MapUtils.getString(map,"id_rota_preco");
	    	BigDecimal advalorem = MapUtilsPlus.getBigDecimal(map,"frete_advalorem");
	    	Object freteQuilo = map.get("frete_quilo");
			Object tarifa = map.get("tarifa");
	    	Object nomeColuna = map.get("vl_faixa_progressiva");
			Object valorColuna = map.get("vl_fixo");
			Object nrColunaDin  = map.get("NR_COLUNA_DINAMICA");

			String ufOrigem = MapUtils.getString(map,"uf_origem");
			String ufDestino = MapUtils.getString(map,"uf_destino");

			String origem = trataLogradouro( MapUtils.getString(map,"m_origem"), 
					   						 MapUtils.getString(map,"f_origem"), 
					   						 MapUtils.getString(map,"tlm_origem"));

			String destino = trataLogradouro(MapUtils.getString(map,"m_destino"), 
					   						 MapUtils.getString(map,"f_destino"), 
					   						 MapUtils.getString(map,"tlm_destino"));

//			Concatena o uf da origem e do destino com "-" para ser usado no metodo verificaComplemento
			String ufOrigemDestino = ufOrigem+"-"+ufDestino;
			origem 	= getTabelasClienteService().verificaComplemento(listaUFOrigem, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, origem);
			destino = getTabelasClienteService().verificaComplemento(listaUFDestino, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, destino);

			origem = ufOrigem + " - " + origem;
			destino = ufDestino + " - " + destino;

			map.put("origem", origem);
			map.put("destino", destino);


			crosstab.add(nomeColuna);

		}

		 getTabelasClienteService().verificaUfs(listaUFOrigem,"ORIGEM",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,getJdbcTemplate());
		 getTabelasClienteService().verificaUfs(listaUFDestino,"DESTINO",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,getJdbcTemplate());
	}

	private String trataLogradouro(String municipio, String filial, String tlm){		
		if (municipio != null) return municipio;
		else if (StringUtils.isNotBlank(filial) && !" - ".equals(filial)) return filial;
		else if (tlm != null) return tlm;
		else return null;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
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
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	
	public EmitirTabelaEcommerceDiferenciadaService getEmitirTabelaEcommerceDiferenciadaService() {
		return emitirTabelaEcommerceDiferenciadaService;
	}
	public void setEmitirTabelaEcommerceDiferenciadaService(
			EmitirTabelaEcommerceDiferenciadaService emitirTabelaEcommerceDiferenciadaService) {
		this.emitirTabelaEcommerceDiferenciadaService = emitirTabelaEcommerceDiferenciadaService;
	}

}