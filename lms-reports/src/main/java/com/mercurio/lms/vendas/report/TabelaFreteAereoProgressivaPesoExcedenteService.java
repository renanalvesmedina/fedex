package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
 * ET: 30.03.02.29 Emitir tabela de frete aéreo progressivo com peso excedente
 * 
 * @author Baltazar Schirmer
 *
 * @spring.bean id="lms.vendas.tabelaFreteAereoProgressivaPesoExcedenteService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteAereoProgressivaPesoExcedente.vm"
 * 
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="285"
 * @spring.property name="crossTabBandWidths" value="362" 
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class TabelaFreteAereoProgressivaPesoExcedenteService extends ReportServiceSupport {
	
	private static final String NOME_TABELA = "TMP_FRETE_AEREO_PROG_PEXC";
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	private TabelasClienteService tabelasClienteService;
	
	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}
	
	private String getSqlUpdate(Long idSimulacao){
		StringBuffer sql = new StringBuffer(); 

		if(idSimulacao==null){
			sql.append(" select PC.TP_INDICADOR_FRETE_PESO, PC.VL_FRETE_PESO, ")
			.append(" PC.TP_INDIC_VLR_TBL_ESPECIFICA, VL_TBL_ESPECIFICA, TP_TARIFA_MINIMA, VL_TARIFA_MINIMA ")
			.append(" from ")
			.append(" CLIENTE CC, DIVISAO_CLIENTE  DC, TABELA_DIVISAO_CLIENTE TDC, TABELA_PRECO TP, ")
			.append(" PARAMETRO_CLIENTE PC ")
			.append(" WHERE ")
			.append("   DC.ID_CLIENTE = CC.ID_CLIENTE AND ")
			.append("   TDC.ID_DIVISAO_CLIENTE = DC.ID_DIVISAO_CLIENTE AND ")
			.append("   TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO AND ")
			.append("   PC.ID_TABELA_DIVISAO_CLIENTE = TDC.ID_TABELA_DIVISAO_CLIENTE ") 
			.append("   AND DC.ID_DIVISAO_CLIENTE = ? ")
			.append("   AND PC.ID_PARAMETRO_CLIENTE = ? ");
		}else{
			sql.append(" select PC.TP_INDICADOR_FRETE_PESO, PC.VL_FRETE_PESO, ")
			.append(" PC.TP_INDIC_VLR_TBL_ESPECIFICA, VL_TBL_ESPECIFICA, TP_TARIFA_MINIMA, VL_TARIFA_MINIMA ")
			.append(" from ")
			.append(" PARAMETRO_CLIENTE PC ")
			.append(" WHERE ")
			.append(" PC.ID_PARAMETRO_CLIENTE = ?");			
		}
		return sql.toString();
	}
	
	private ConfiguracoesFacade configuracoesFacade;
	
	/**
	 * Para pegar configuracoes especificas do subReport
	 */
	private EmitirTabelaTaxaCombustivelLandscapeService emitirTabelaTaxaCombustivel;
	
	/**
	 * Para guardar o idTabelaPreco e o dsSimbolo e compartilhar entre metodos
	 */
	private static final ThreadLocal dadosClasseThread = new ThreadLocal();
	
    /**
     * Seta o atributo local identificado pela chave com o valor
     * 
     * @param key chave do atributo local
     * @param value valor do atributo local
     */
    private void setLocalVariableValue(Object key, Object value)
    {
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
    private Object getLocalVariableValue(Object key)
    {
    	Map map = (Map)dadosClasseThread.get();
    	if(map == null) return null;
    	
    	return map.get(key);
    }
    
	public JRReportDataObject execute(Map parameters) throws Exception {
		return null;
	}
		
	public List<Map<String, String>> findDados(TypedFlatMap parameters) {
		List<Map> data = getEmitirTabelasClienteDAO().findRelatorioFreteAereoProgressivaPesoExcedente(parameters);
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		TypedFlatMap parametros = new TypedFlatMap();
		
		if(data == null || data.isEmpty()){
			return null;
		}

		for (Map map : data) {
			parametros = getCommonParameter(map);
			parametros.put("idParametroCliente", MapUtils.getLong(map,"listaParametros"));
			parametros.put("idTabelaDivisao", MapUtils.getLong(map,"idTabelaDivisao"));
			parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));

			
		
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		
		jdbcTemplate.execute("DELETE FROM "+ NOME_TABELA);
		
		//Parametros recebidos da Action
		Long idTabelaPreco 	= MapUtils.getLong(map,"idTabelaPreco");
		Boolean isTabelaNova = MapUtils.getBoolean(map, "isTabelaNova", false);
		Simulacao simulacao = (Simulacao)MapUtils.getObject(map, "simulacao");
		
		if(idTabelaPreco == null){
			return null;
		}
		
		/* Obtém os dados do relatório principal */
		List list = jdbcTemplate.queryForList(montaSql(), new Long[]{idTabelaPreco, idTabelaPreco, idTabelaPreco, idTabelaPreco, idTabelaPreco});
        if(list == null || list.isEmpty()){
        	return null;
        }
		
		Map parametersColumn = new HashMap();
		Set crosstab = new LinkedHashSet();
       	populateTable(list, crosstab, parametersColumn); 
       	updateTableTemp(parameters, crosstab);
       	 
        int[] subreports = {
        		TabelasClienteService.SUBREPORT_COLETA_CLIENTE,
        		TabelasClienteService.SUBREPORT_ENTREGA_CLIENTE,
        		TabelasClienteService.SUBREPORT_FORMALIDADES_AEREO,
        		TabelasClienteService.SUBREPORT_GENERALIDADES,
        		TabelasClienteService.SUBREPORT_SERVICOSCONTRATADOS,
        		TabelasClienteService.SUBREPORT_SERVICOSNAOCONTRATADOS,
        		TabelasClienteService.SUBREPORT_TAXA_TERRESTRE};
        
        Map parametersReport = new HashMap();
        //não passa crosstab porque já montou os parametros das cols dinamicas em populateTable, devido a colunas vazias no meio de tabelas do tipo 'D'
       	parametersReport = getTabelasClienteService().getAllDefaultChoiceCrosstabReportParameters(parameters, NOME_TABELA, isTabelaNova,this.getCrosstab().size(), null, subreports,configuracoesFacade,jdbcTemplate);
       	parametersReport.putAll(parametersColumn);
       	
		if(simulacao!=null){
			Servico serv = simulacao.getServico();
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(serv.getIdServico(), jdbcTemplate);
			parametersReport.put("SERVICO", MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO"));
       	}
       	
       	//TAXA COMBUSTIVEL
		Map mapTaxCombustivel = getTabelasClienteService().getSubRelTaxaCombustivel(idTabelaPreco, jdbcTemplate);
		List listTaxCombustivel = (List)mapTaxCombustivel.get("RESULT");
		Set subCrossTab         = (Set)mapTaxCombustivel.get("SUBCROSSTAB");
		if(listTaxCombustivel !=null && !listTaxCombustivel.isEmpty())
		{
			List listColumn = new ArrayList();
			listColumn.addAll(subCrossTab);
			String nomeSubRel = TabelasClienteService.PATH_TABELAPRECOS + "report/subReportTaxaCombustivel_Landscape_ct_" + subCrossTab.size() + ".jasper";
			
			parametersReport.put("SUBREPORTTAXACOMBUSTIVEL_PATH", nomeSubRel);
			parametersReport.put(TabelasClienteService.KEY_PARAMETER_TAXA_COMBUSTIVEL, new JRMapCollectionDataSource(listTaxCombustivel));
			for(int i=0; i<subCrossTab.size();i++)
			{
				mapTaxCombustivel.put("COLUMN"+(i+1), listColumn.get(i));
			}
			emitirTabelaTaxaCombustivel.setMapParameters(mapTaxCombustivel);
			parametersReport.put("SERVICE_TX_COMB", emitirTabelaTaxaCombustivel);
		}
       	
       	int totRegs = getJdbcTemplate().queryForInt("SELECT COUNT(*) AS TTT from " + NOME_TABELA);
       	parametersReport.put("TOTAL",totRegs);
       	
       	getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);
       	
       	String moeda = getTabelasClienteService().getMoeda(idTabelaPreco, jdbcTemplate);
       	parametersReport.put("MOEDA", moeda);
       	
        JRReportDataObject jr = executeQuery("SELECT * FROM "+ NOME_TABELA +" ORDER BY TARIFA",new HashMap());
        parametersReport.put(ReportServiceSupport.CT_NUMBER_OF_COLS,new Integer[]{Integer.valueOf(this.getCrosstab().size())});
        jr.setParameters(parametersReport);
		parameters.put(idTabelaPreco, parametersReport);
	}
		return result;
	}
	
	/**
	 * @param parameters
	 * @return
	 */
	private JRReportDataObject getEmptyReport(Map parameters){
		
		JRReportDataObject jr = executeQuery("SELECT * FROM "+ NOME_TABELA +" order by Tarifa",new HashMap());
		parameters.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{Integer.valueOf(1)});
		jr.setParameters(parameters);
		return jr;
	}
	
	/**
	 * Metodo que atualiza a tabela temporaria com os parametros do cliente
	 * @param parameters
	 */
	private void updateTableTemp(Map parameters, Set crosstab)
	{
		Map dados = getValorAplicado(
				MapUtilsPlus.getLong(parameters,"idParametroCliente",null),
				MapUtilsPlus.getLong(parameters,"idDivisao",null),
				MapUtilsPlus.getLong(parameters,"idSimulacao",null)
									 );
		
		if (dados!=null && dados.size()>=1)
		{
			StringBuffer updateVl = new StringBuffer();
			StringBuffer updateTarMin = new StringBuffer();

			//FRETE PESO
			if (MapUtilsPlus.getString(dados,"VL_PESO_TIPO","").equalsIgnoreCase("A")){
				Long valorParam = MapUtilsPlus.getLong(dados, "VL_PESO");
				if(valorParam != null && valorParam.longValue()!=0)
				{
					makeUpdateFretePeso(updateVl, crosstab, "+", valorParam);
				}
			}
			else if (MapUtilsPlus.getString(dados,"VL_PESO_TIPO","").equalsIgnoreCase("D")){
				Long valorParam = MapUtilsPlus.getLong(dados, "VL_PESO");
				if(valorParam != null && valorParam.longValue()!=0)
				{
					makeUpdateFretePeso(updateVl, crosstab, "-", valorParam);
				}
			}
			//TARIFA MINIMA
			if (MapUtilsPlus.getString(dados,"VL_TARMIN_TIPO","").equalsIgnoreCase("A")){
				Long valorParam = MapUtilsPlus.getLong(dados, "VL_TARMIN");
				if(valorParam != null && valorParam.longValue()!=0)
				{
					updateTarMin.append("VL_EXCEDENTE = VL_EXCEDENTE + (VL_EXCEDENTE * ").append(valorParam)
					.append("/100)");
				}
			}
			else if (MapUtilsPlus.getString(dados,"VL_TARMIN_TIPO","").equalsIgnoreCase("D")){
				Long valorParam = MapUtilsPlus.getLong(dados, "VL_TARMIN");
				if(valorParam != null && valorParam.longValue()!=0)
				{
					updateTarMin.append("VL_EXCEDENTE = VL_EXCEDENTE - (VL_EXCEDENTE * ").append(valorParam)
					.append("/100)");
				}
			}
			
			StringBuffer sqlUpdate = new StringBuffer("update " + NOME_TABELA + " set   ");
			if(!updateVl.toString().equalsIgnoreCase(""))
			{
				sqlUpdate.append(updateVl);
			}
			if(!updateTarMin.toString().equalsIgnoreCase(""))
			{
				if(!updateVl.toString().equalsIgnoreCase("")) sqlUpdate.append(" ,");
				sqlUpdate.append(updateTarMin);
			}
			
			getJdbcTemplate().execute(sqlUpdate.toString());			
		}
	}
	
	/**
	 * Constroi update das colunas dinâmicas
	 */
	private void makeUpdateFretePeso(StringBuffer sbFretePeso, Set crosstab, String operacao, Long valorParam)
	{
		for(int i=0; i<=crosstab.size(); i++)
		{
			sbFretePeso.append("COLUMN").append(i+1).append(" = ").append("COLUMN").append(i+1)
				.append(" ").append(operacao).append(" ")
				.append(" ( COLUMN").append(i+1).append(" * ").append(valorParam).append("/100)");
			
			if(i<crosstab.size())sbFretePeso.append(", ");
		}
	}
	
	/**
	 * Metodo que retorna os parametros do cliente
	 * @param idParametroCliente
	 * @param idDivisaoCliente
	 * @return
	 */
	private Map getValorAplicado(Long idParametroCliente,Long idDivisaoCliente, Long idSimulacao)
	{
		
		List result = null;
		
		if(idSimulacao==null){
			result = getJdbcTemplate().queryForList(getSqlUpdate(idSimulacao),new Object[]{idDivisaoCliente,idParametroCliente});
		}else{
			result = getJdbcTemplate().queryForList(getSqlUpdate(idSimulacao),new Object[]{idParametroCliente});
		}
		
		if (result!=null && result.size()>=1)
		{
			Map retorno = new HashMap();
			
			Map dados = (Map)result.iterator().next();
			if (dados.get("TP_INDICADOR_FRETE_PESO").toString().equals("V")||dados.get("TP_INDICADOR_FRETE_PESO").toString().equals("A"))
			{
				retorno.put("VL_PESO",MapUtilsPlus.getLong(dados,"VL_FRETE_PESO",null));
				retorno.put("VL_PESO_TIPO","A");
			}
			if (dados.get("TP_INDICADOR_FRETE_PESO").toString().equals("D"))
			{
				retorno.put("VL_PESO",MapUtilsPlus.getLong(dados,"VL_FRETE_PESO",null));
				retorno.put("VL_PESO_TIPO","D");
			}
			
			if (dados.get("TP_INDIC_VLR_TBL_ESPECIFICA").toString().equals("A"))
			{
				retorno.put("VL_TBL",MapUtilsPlus.getLong(dados,"VL_TBL_ESPECIFICA",null));
				retorno.put("VL_TBL_TIPO","A");
			}
			if (dados.get("TP_INDIC_VLR_TBL_ESPECIFICA").toString().equals("D"))
			{
				retorno.put("VL_TBL",MapUtilsPlus.getLong(dados,"VL_TBL_ESPECIFICA",null));
				retorno.put("VL_TBL_TIPO","D");
			}

			if (dados.get("TP_TARIFA_MINIMA").toString().equals("A"))
			{
				retorno.put("VL_TARMIN",MapUtilsPlus.getLong(dados,"VL_TARIFA_MINIMA",null));
				retorno.put("VL_TARMIN_TIPO","A");
			}
			if (dados.get("TP_TARIFA_MINIMA").toString().equals("D"))
			{
				retorno.put("VL_TARMIN",MapUtilsPlus.getLong(dados,"VL_TARIFA_MINIMA",null));
				retorno.put("VL_TARMIN_TIPO","D");
			}			
			return retorno;
			
		}
		return null;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private String montaSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("  select \n")
					.append("   PESQ_A.vl_faixa_progressiva, \n")
					.append("   PESQ_A.vl_fixo, \n")
					.append("   PESQ_A.tarifa, \n")
					.append("   PESQ_A.NR_COLUNA_DINAMICA, \n")
					.append("	PESQ_A.id_rota_preco, \n")
					.append("   PESQ_A.uf_origem, \n")
					.append("   PESQ_A.uf_destino, \n")
					.append("   PESQ_A.a_origem, \n")
					.append("   PESQ_A.a_destino, \n")
					.append("   PESQ_A.m_origem, \n")
					.append("   PESQ_A.m_destino, \n")
					.append("   PESQ_A.f_origem, \n")
					.append("   PESQ_A.f_destino, \n")
					.append("   PESQ_A.tlm_origem, \n")
					.append("   PESQ_A.tlm_destino, \n")
					.append("   PESQ_B.preco_frete, \n")
					.append("   PESQ_C.preco_frete as advalorem \n")
					.append(" from  \n")
					
					.append("   (select \n")
					.append("      FX_PROG.vl_faixa_progressiva, \n")
					.append("      VAL_FX_PROG.vl_fixo, \n")
					.append("	   tap.cd_tarifa_preco as tarifa, \n")	
					.append("      RT_PRECO.id_rota_preco, \n")
					.append("      COLUNA_DINAMICA.NR_COLUNA_DINAMICA, \n")
					.append("      UF_ORIGEM.sg_unidade_federativa as uf_origem, \n")
					.append("      UF_DESTINO.sg_unidade_federativa as uf_destino, \n")
					.append("      A_ORIGEM.sg_aeroporto as a_origem, \n")
					.append("      A_DESTINO.sg_aeroporto as a_destino, \n")
					.append("      M_ORIGEM.nm_municipio as m_origem, \n")
					.append("      M_DESTINO.nm_municipio as m_destino, \n")
					.append("      F_ORIGEM.sg_filial as f_origem, \n")
					.append("      F_DESTINO.sg_filial as f_destino, \n")
					.append(PropertyVarcharI18nProjection.createProjection("  TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I", "tlm_origem")).append(" , \n")
					.append(PropertyVarcharI18nProjection.createProjection("  TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I", "tlm_destino \n"))
					//.append("      TLM_ORIGEM.ds_tipo_localizacao_municipio as tlm_origem,")
					//.append("      TLM_DESTINO.ds_tipo_localizacao_municipio as tlm_destino")
					.append("    from \n")
					.append("      tabela_preco TAB_PRECO, \n")
					.append("      tabela_preco_parcela TAB_PRECO_PARC, \n")
					.append("      parcela_preco PARC_PRECO, \n")
					.append("      faixa_progressiva FX_PROG, \n")
					.append("      valor_faixa_progressiva VAL_FX_PROG, \n")
					.append("      rota_preco RT_PRECO, \n")
					.append("      unidade_federativa UF_ORIGEM, \n")
					.append("      unidade_federativa UF_DESTINO, \n")
					.append("      municipio M_ORIGEM, \n")
					.append("      municipio M_DESTINO, \n")
					.append("      aeroporto A_ORIGEM, \n")
					.append("      aeroporto A_DESTINO, \n")
					.append("      filial F_ORIGEM, \n")
					.append("      filial F_DESTINO, \n")
					.append("      tipo_localizacao_municipio TLM_ORIGEM, \n")
					.append("      tipo_localizacao_municipio TLM_DESTINO, \n")
					.append("	   tarifa_preco tap,  \n")
                    .append("      tarifa_preco_rota tpr,  \n")	
                    
					.append("(select TABCOLDIN.*, rownum as nr_coluna_dinamica \n")
					.append(" from(select tp.id_tabela_preco, fp.id_faixa_progressiva, fp.vl_faixa_progressiva from faixa_progressiva fp,  \n")
					.append(" tabela_preco_parcela tpp,  tabela_preco tp, parcela_preco pp  \n")
					.append(" where  fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela  \n")
					.append(" and tpp.id_parcela_preco = pp.id_parcela_preco")//new
					.append(" and pp.cd_parcela_preco = 'IDFretePeso'")//new
					
					.append(" and tpp.id_tabela_preco = tp.id_tabela_preco  and tp.id_tabela_preco = ?  \n")
					.append(" order by fp.vl_faixa_progressiva) TABCOLDIN) COLUNA_DINAMICA  \n")	                    
                    
					.append("    where \n")
					.append("      TAB_PRECO.id_tabela_preco = ? \n")
					.append("      and TAB_PRECO_PARC.id_tabela_preco = TAB_PRECO.id_tabela_preco \n")
					.append("      and PARC_PRECO.id_parcela_preco = TAB_PRECO_PARC.id_parcela_preco \n")
					.append("      and PARC_PRECO.cd_parcela_preco = 'IDFretePeso' \n")//frete peso
					
					.append("and ") //relatório não pode conter parcelas de tarifa mínima
					.append(" (select count(pp.cd_parcela_preco) as qtd_tarifa_min \n")
					.append("  from tabela_preco tp, tabela_preco_parcela tpp, parcela_preco pp \n")
					.append("  where tp.ID_TABELA_PRECO = tpp.ID_TABELA_PRECO \n")
					.append("  and tpp.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO \n")
					.append("  and pp.cd_parcela_preco = 'IDTarifaMinima' \n")
					.append("  and tp.ID_TABELA_PRECO = ?) = 0 \n")

					.append("      and TAB_PRECO_PARC.id_tabela_preco_parcela = FX_PROG.id_tabela_preco_parcela \n")
					.append("      and VAL_FX_PROG.id_faixa_progressiva = FX_PROG.id_faixa_progressiva \n")
					.append("      and VAL_FX_PROG.id_tarifa_preco = tap.id_tarifa_preco \n")
					.append("      and COLUNA_DINAMICA.id_faixa_progressiva = FX_PROG.id_faixa_progressiva \n")
                    .append("      and tap.id_tarifa_preco = tpr.id_tarifa_preco \n")
                    .append("      and tpr.id_tabela_preco = TAB_PRECO.id_tabela_preco \n")
                    .append("      and tpr.id_rota_preco =  RT_PRECO.id_rota_preco \n")
					.append("      and RT_PRECO.id_uf_origem = UF_ORIGEM.id_unidade_federativa \n")
					.append("      and RT_PRECO.id_uf_destino = UF_DESTINO.id_unidade_federativa \n")
					.append("      and RT_PRECO.id_aeroporto_origem = A_ORIGEM.id_aeroporto (+) \n")
					.append("      and RT_PRECO.id_aeroporto_destino = A_DESTINO.id_aeroporto (+) \n")
					.append("      and RT_PRECO.id_municipio_origem = M_ORIGEM.id_municipio (+) \n")
					.append("      and RT_PRECO.id_municipio_destino = M_DESTINO.id_municipio (+) \n")
					.append("      and RT_PRECO.id_filial_origem = F_ORIGEM.id_filial (+) \n")
					.append("      and RT_PRECO.id_filial_destino = F_DESTINO.id_filial (+) \n")
					.append("      and RT_PRECO.id_tipo_localizacao_origem = TLM_ORIGEM.id_tipo_localizacao_municipio (+) \n")
					.append("      and RT_PRECO.id_tipo_localizacao_destino = TLM_DESTINO.id_tipo_localizacao_municipio (+) ) PESQ_A, \n")

					.append("   (select \n")
					.append("      PRECO_FT.vl_preco_frete as preco_frete, \n")
					.append("      RT_PRECO.id_rota_preco, \n")
					.append("	   tap.cd_tarifa_preco as tarifa \n")
					.append("    from \n")
					.append("      tabela_preco TAB_PRECO, \n")
					.append("      tabela_preco_parcela TAB_PRECO_PARC, \n")
					.append("      parcela_preco PARC_PRECO, \n")
					.append("      preco_frete PRECO_FT, \n")
					.append("      rota_preco RT_PRECO, \n")
					.append("	   tarifa_preco tap,  \n")
                    .append("      tarifa_preco_rota tpr  \n")	
					.append("    where \n")
					.append("      TAB_PRECO.id_tabela_preco = ? \n")
					.append("      and TAB_PRECO.id_tabela_preco = TAB_PRECO_PARC.id_tabela_preco \n")
					.append("      and TAB_PRECO_PARC.id_parcela_preco = PARC_PRECO.id_parcela_preco \n")
					
					.append("      and PARC_PRECO.cd_parcela_preco = 'IDFreteQuilo' \n")//frete quilo
					
					.append("      and TAB_PRECO_PARC.id_tabela_preco_parcela = PRECO_FT.id_tabela_preco_parcela \n")
					.append("      and PRECO_FT.id_tarifa_preco = tap.id_tarifa_preco \n")
                    .append("      and tap.id_tarifa_preco = tpr.id_tarifa_preco \n")
                    .append("      and tpr.id_tabela_preco = TAB_PRECO.id_tabela_preco \n")
                    .append("      and tpr.id_rota_preco =  RT_PRECO.id_rota_preco \n")
					.append("      ) PESQ_B, \n")
					
					.append("   (select \n")
					.append("      PRECO_FT.vl_preco_frete as preco_frete, \n")
					.append("      RT_PRECO.id_rota_preco, \n")
					.append("	   tap.cd_tarifa_preco as tarifa \n")
					.append("    from \n")
					.append("      tabela_preco TAB_PRECO, \n")
					.append("      tabela_preco_parcela TAB_PRECO_PARC, \n")
					.append("      parcela_preco PARC_PRECO, \n")
					.append("      preco_frete PRECO_FT, \n")
					.append("      rota_preco RT_PRECO, \n")
					.append("	   tarifa_preco tap,  \n")
                    .append("      tarifa_preco_rota tpr  \n")	
					.append("    where \n")
					.append("      TAB_PRECO.id_tabela_preco = ? \n")
					.append("      and TAB_PRECO.id_tabela_preco = TAB_PRECO_PARC.id_tabela_preco \n")
					.append("      and TAB_PRECO_PARC.id_parcela_preco = PARC_PRECO.id_parcela_preco \n")
					.append("      and PARC_PRECO.cd_parcela_preco = 'IDAdvalorem' \n")
					.append("      and TAB_PRECO_PARC.id_tabela_preco_parcela = PRECO_FT.id_tabela_preco_parcela \n")
					.append("      and PRECO_FT.id_tarifa_preco = tap.id_tarifa_preco \n")
                    .append("      and tap.id_tarifa_preco = tpr.id_tarifa_preco \n")
                    .append("      and tpr.id_tabela_preco = TAB_PRECO.id_tabela_preco \n")
                    .append("      and tpr.id_rota_preco =  RT_PRECO.id_rota_preco \n")
					.append("      ) PESQ_C \n")					

					.append(" where \n")
					
					.append("    PESQ_A.id_rota_preco = PESQ_B.id_rota_preco \n")
					.append("    and PESQ_A.tarifa = PESQ_B.tarifa \n")
					
					.append("    and PESQ_A.id_rota_preco = PESQ_C.id_rota_preco \n")
					.append("    and PESQ_A.tarifa = PESQ_C.tarifa \n")

					.append("    ORDER BY id_rota_preco, vl_faixa_progressiva, uf_origem, uf_destino");
		
		return sql.toString();
	}
	 	
	
	private void populateTable(List list, Set crosstab, Map parametersReport){

		List listaUFOrigem = new ArrayList();
		List listaUFDestino = new ArrayList();
		List sqlRegistros = new ArrayList();
		 
		StringBuffer colunas = null;
		StringBuffer values = null;
		String sql = "";
		 
	    this.setCrosstab(crosstab);
	    
	    Map tabelas = new HashMap();
	    this.setTabelas(tabelas);
	    
	    
	    String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
		String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");
	    
	    int count = 1;
	    for (Iterator it = list.iterator(); it.hasNext();count++ ) { 
	    	Map map = (Map)it.next();
	           
	    	String rota = MapUtils.getString(map,"id_rota_preco");
	    	Object tarifa      = map.get("tarifa");
	    	Object excedente   = map.get("preco_frete");
	    	Object advalorem   = map.get("advalorem");
			Object nomeColuna  = map.get("vl_faixa_progressiva");
			Object valorColuna = map.get("vl_fixo");
			Object nrColunaDin  = map.get("NR_COLUNA_DINAMICA");
			   
			String ufOrigem = MapUtils.getString(map,"uf_origem");
			String ufDestino = MapUtils.getString(map,"uf_destino");
			  
			String origem = trataLogradouro( MapUtils.getString(map,"a_origem"), 
			          						 MapUtils.getString(map,"m_origem"), 
					   						 MapUtils.getString(map,"f_origem"), 
					   						 MapUtils.getString(map,"tlm_origem"));
			   
			String destino = trataLogradouro(MapUtils.getString(map,"a_destino"),
					   						 MapUtils.getString(map,"m_destino"), 
					   						 MapUtils.getString(map,"f_destino"), 
					   						 MapUtils.getString(map,"tlm_destino"));
				   
			   
//			Concatena o uf da origem e do destino com "-" para ser usado no metodo verificaComplemento
			String ufOrigemDestino = ufOrigem+"-"+ufDestino;
			origem 	= getTabelasClienteService().verificaComplemento(listaUFOrigem, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, origem);
			destino 	= getTabelasClienteService().verificaComplemento(listaUFDestino, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, destino);
			
		        
			origem = ufOrigem + " - " + origem;
			destino = ufDestino + " - " + destino;
		    		      
	   		if (!tabelas.containsKey(rota)){
	   			count = 1;
	   			
	   			
	   			if(colunas != null){
	   				colunas.append(")\n");
	   			}
	   			if(values != null){
	   				values.append(")\n");
	   				sql = colunas.toString() + values.toString();
	   				sqlRegistros.add(sql);
	   			}
	   			
	   			
	   			colunas = new StringBuffer("INSERT INTO " + NOME_TABELA + " (TARIFA, ORIGEM, DESTINO, VL_EXCEDENTE, ADVALOREM");
		   		values = new StringBuffer(" VALUES('"+ tarifa +"','"+ origem +"','"+ destino +"', "+ excedente +", "+ advalorem);
		   			
		   		tabelas.put(rota, new String());
		   	}
		   	
			crosstab.add(nomeColuna);
			parametersReport.put(("PCOLUMN"+nrColunaDin.toString()), nomeColuna.toString());
			
			colunas.append(", COLUMN"+nrColunaDin);
			values.append(", "+ valorColuna);
		}
			
		//finaliza o ultimo registro
		if(colunas != null){
			colunas.append(")\n");
		}
		
		if(values != null){
			values.append(")\n");
			sql = colunas.toString() + values.toString();
			sqlRegistros.add(sql);
		}
			
		for (Iterator iter = sqlRegistros.iterator(); iter.hasNext();) {
			String sqlInsert = (String) iter.next();
			getJdbcTemplate().execute(sqlInsert);		
		}
		
		getTabelasClienteService().verificaUfs(listaUFOrigem,"ORIGEM",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,getJdbcTemplate());
	    getTabelasClienteService().verificaUfs(listaUFDestino,"DESTINO",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,getJdbcTemplate());
	
		
	    this.setCrosstab(crosstab);
	    this.setTabelas(tabelas);
	}
	
	private String trataLogradouro(String aerop, String munic, String filial, String tlm){
		
		if (aerop != null) return aerop;
		else if (munic != null) return munic;
		else if (filial != null) return filial;
		else if (tlm != null) return tlm;
		else return null;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public Set getCrosstab() {
		return (Set)this.getLocalVariableValue("crosstab");
	}

	public void setCrosstab(Set crosstab) {
		this.setLocalVariableValue("crosstab", crosstab);
	}

	public Map getTabelas() {
		return (Map)this.getLocalVariableValue("tabelas");
	}

	public void setTabelas(Map tabelas) {
		this.setLocalVariableValue("tabelas", tabelas);
	}

	public EmitirTabelaTaxaCombustivelLandscapeService getEmitirTabelaTaxaCombustivel() {
		return emitirTabelaTaxaCombustivel;
	}

	public void setEmitirTabelaTaxaCombustivel(
			EmitirTabelaTaxaCombustivelLandscapeService emitirTabelaTaxaCombustivel) {
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
		
}
