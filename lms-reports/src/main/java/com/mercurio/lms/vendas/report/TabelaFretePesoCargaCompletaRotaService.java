package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.collections.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * ET: 30.03.02.23 - Tabela Frete Peso Carga Completa por Rota
 * 
 * @author Rodrigo F. Dias
 *
 * @spring.bean id="lms.vendas.report.tabelaFretePesoCargaCompletaRotaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFretePesoCargaCompletaRota.vm"
 
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="268"
 * @spring.property name="crossTabBandWidths" value="451" 
 * @spring.property name="numbersOfCrossTabColumns" value="20"

 */
public class TabelaFretePesoCargaCompletaRotaService extends ReportServiceSupport {

	/**
	 * Tabela tempor&aacute;ria para comportar dos dados din&acirc;micos do relat&oacute;rio
	 */
	private static final String NOME_TABELA = "TMP_CARGA_COMPL_ROTA";
	
	private ConfiguracoesFacade configuracoesFacade;
	private TabelasClienteService tabelasClienteService;	
		
	/**
	 * Valor m&iacute;nimo da faixa especificado na regra do relat&oacute;rio
	 */
	private static final Integer psminimo= Integer.valueOf(1000);

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

		getJdbcTemplate().execute("DELETE FROM "+ NOME_TABELA);
		
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		
		//Parametros recebidos da Action
		Long idCliente = MapUtils.getLong(parameters, "idCliente");
		Long idDivisao = MapUtils.getLong(parameters, "idDivisao");
		Long idTabelaDivisaoCliente = MapUtils.getLong(parameters,"idTabelaDivisaoCliente");
		Long idTabelaPreco = MapUtils.getLong(parameters, "idTabelaPreco");
		Boolean isTabelaNova = MapUtils.getBoolean(parameters, "isTabelaNova", false);
			    
		 /* Obtém os dados do relatório principal */
        List result = jdbcTemplate.queryForList(montaSql(), new Long[]{idTabelaPreco,idTabelaPreco});
		populateTable(result, jdbcTemplate);
		
		//Acha os dados do cabeçalho
		Map parametersReport = new HashMap();
		
		
	    //Monta subrelatorios - Generalidades sem as parametrizacoes do cliente (3º atributo = null)
		int[] subreports = {
				TabelasClienteService.SUBREPORT_FORMALIDADES,
				TabelasClienteService.SUBREPORT_GENERALIDADES,
				TabelasClienteService.SUBREPORT_SERVICOSCONTRATADOS,
				TabelasClienteService.SUBREPORT_SERVICOSNAOCONTRATADOS
		};
		parametersReport = getTabelasClienteService().montaSubRelatoriosOfChoice(idCliente,idDivisao,NOME_TABELA, this.getCrosstab().size(),idTabelaPreco,isTabelaNova,parametersReport,subreports,configuracoesFacade,jdbcTemplate);
	    
	    JRReportDataObject jr = executeQuery("SELECT * FROM "+ NOME_TABELA +" ",new HashMap());
		
	    int cont = 1;
        for(Iterator i = this.getCrosstab().iterator();i.hasNext();){
        	String coluna = "PCOLUMN"+cont;
        	Object v =  i.next();
        	String valor = v != null ? v.toString() : null;
        	parametersReport.put(coluna,valor);
        	cont++;
        }
        
        //Parameters from database
        String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco,jdbcTemplate);
        String dsTipoServico = getTabelasClienteService().getTipoServico(idTabelaDivisaoCliente, jdbcTemplate);

		parametersReport.put("SERVICO", dsTipoServico);
        parametersReport.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{Integer.valueOf(this.getCrosstab().size())});	
        parametersReport.put("DS_SIMBOLO",dsSimbolo);
		parametersReport.put("idCliente", MapUtilsPlus.getLong(parameters,"idCliente",null));
        parametersReport.put("idContato", MapUtilsPlus.getLong(parameters,"idContato",null));
        parametersReport.put("HEADER",(JRMapCollectionDataSource)getTabelasClienteService().montaHeader(parametersReport, jdbcTemplate,TabelasClienteService.RETORNO_DATASOURCE));
        getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);
        
     	jr.setParameters(parametersReport);
		return jr;
	}

	/**
	 * Monta sql para retornar dados da consulta do relat&oacute;rio
	 * @return
	 */
	private String montaSql(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append("	Faixa.ps_minimo,");
		sql.append("	Faixa.VL_FIXO,");
		sql.append("	Faixa.faixa_rota,");
		sql.append("	Faixa.UF_Origem,");
		sql.append("	Faixa.UF_Destino,");
		sql.append("	Faixa.Origem,");
		sql.append("	Faixa.Destino,");
		sql.append("	Faixa.vl_faixa_progressiva as Valor_faixa,");
		sql.append("	Advalorem.valor_Advalorem");
		sql.append(" from");
		
		//quando pp.cd_parcela_preco = 'IDFretePeso' busca faixa progressiva, rotas, uf's de origem e destino e municipios de origem e destino
		sql.append("	(select ");
		sql.append("		vfp.vl_fixo,"); 
		sql.append("		tp.ps_minimo,");
		sql.append("		Fp.vl_faixa_progressiva,");
		sql.append("		uf_origem.sg_unidade_federativa as UF_Origem,");
		sql.append("		uf_destino.sg_unidade_federativa as UF_Destino,");
		sql.append("		rp.id_rota_preco as faixa_rota,");
		sql.append("		decode(RP.ID_MUNICIPIO_ORIGEM,null,decode(F_ORIGEM.SG_FILIAL,null,"+PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")+",F_ORIGEM.SG_FILIAL),M_ORIGEM.NM_MUNICIPIO) as ORIGEM,");
		sql.append("		decode(RP.ID_MUNICIPIO_DESTINO,null,decode(F_DESTINO.SG_FILIAL,null,"+PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")+",F_DESTINO.SG_FILIAL),M_DESTINO.NM_MUNICIPIO) as DESTINO");
		sql.append("	from");
		sql.append("		tabela_preco tp,");
		sql.append("		tipo_tabela_preco ttp,");
		sql.append("		subtipo_tabela_preco stp,");
		sql.append("		tabela_preco_parcela tpp,");
		sql.append("		parcela_preco pp,");
		sql.append("		faixa_progressiva fp,");
		sql.append("		valor_faixa_progressiva vfp,");
		sql.append("		rota_preco rp,");
		sql.append("		unidade_federativa uf_origem,");
		sql.append("		unidade_federativa uf_destino,");
		sql.append("		MUNICIPIO M_ORIGEM, ");
		sql.append("		MUNICIPIO M_DESTINO,");
		sql.append("		TIPO_LOCALIZACAO_MUNICIPIO TLM_ORIGEM, ");
		sql.append("		TIPO_LOCALIZACAO_MUNICIPIO TLM_DESTINO, ");
		sql.append("		FILIAL F_ORIGEM,");
		sql.append("		FILIAL F_DESTINO,");
		sql.append("		servico s");
		sql.append("	where");
		sql.append("		tp.id_tabela_preco = ? ");
		sql.append("		and vl_faixa_progressiva >= ").append(psminimo);
		sql.append("		and tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco");
		sql.append("		and tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco");
		sql.append("		and tp.id_tabela_preco = tpp.id_tabela_preco");
		sql.append("		and tpp.id_parcela_preco = pp.id_parcela_preco");
		sql.append("		and ttp.id_servico = s.id_servico");
		sql.append("		and s.tp_modal = 'R' and s.tp_abrangencia = 'N'");	//rodoviário e nacional			
		sql.append("		and pp.cd_parcela_preco = 'IDFretePeso'");
		sql.append("		and tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela");
		sql.append("		and fp.id_faixa_progressiva = vfp.id_faixa_progressiva");
		sql.append("		and vfp.id_rota_preco = rp.id_rota_preco");
		sql.append("		AND  RP.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA"); 	
		sql.append("		AND  RP.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA"); 	
		sql.append("		AND  RP.ID_MUNICIPIO_ORIGEM = M_ORIGEM.ID_MUNICIPIO (+)");
		sql.append("		AND  RP.ID_MUNICIPIO_DESTINO = M_DESTINO.ID_MUNICIPIO (+)");
		sql.append("		AND  RP.ID_FILIAL_ORIGEM = F_ORIGEM.ID_FILIAL (+)");
		sql.append("		AND  RP.ID_FILIAL_DESTINO = F_DESTINO.ID_FILIAL (+)");
		sql.append("		AND  RP.ID_TIPO_LOCALIZACAO_ORIGEM = TLM_ORIGEM.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)");
		sql.append("		AND  RP.ID_TIPO_LOCALIZACAO_DESTINO = TLM_DESTINO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)");
		sql.append("		) Faixa, ");

		//busca PRECO_FRETE quando cd_parcela_preco = IDAdvalorem
	    sql.append("	(select"); 
		sql.append("		pf.vl_preco_frete as VALOR_Advalorem, rp.id_rota_preco as advalorem_rota_preco");
		sql.append("	from  tabela_preco tp,");
		sql.append("		tipo_tabela_preco ttp,");
		sql.append("		subtipo_tabela_preco stp,");
		sql.append("		tabela_preco_parcela tpp,");
		sql.append("		parcela_preco pp,");
		sql.append("		preco_frete pf,");
		sql.append("		rota_preco rp");
		sql.append("	where tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco");
		sql.append("		and tp.id_tabela_preco = ? ");
		sql.append("		and tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco");
		sql.append("		and tp.id_tabela_preco = tpp.id_tabela_preco");
		sql.append("		and tpp.id_parcela_preco = pp.id_parcela_preco");
		sql.append("		and pp.cd_parcela_preco = 'IDAdvalorem'");
		sql.append("		and tpp.id_tabela_preco_parcela = pf.id_tabela_preco_parcela");
		sql.append("		and pf.id_rota_preco = rp.id_rota_preco) Advalorem");
		
		sql.append(" where Faixa.faixa_rota = Advalorem.advalorem_rota_preco (+)");
		sql.append(" Order by faixa_rota,valor_faixa,UF_Origem,Origem,Uf_Destino,Destino");
		return sql.toString();
	}

	/**
	 * M&eacute;todo para popular tabela tempor&aacute;ria do relat&oacute;rio
	 * e parametrizar o label da &uacute;ltima coluna dinamica
	 * 
	 * @param list resultset com os dados da consulta
	 */
	private void populateTable(List list, JdbcTemplate jdbcTemplate) {
		
		List listaUFOrigem = new LinkedList();
		List listaUFDestino = new LinkedList();
		List sqlRegistros = new LinkedList();
		List sqlValues = new LinkedList();
		StringBuffer colunas = null;
		StringBuffer valuesStr = null;
		List values = new LinkedList();
		String sql = "";
		Set crosstab = new LinkedHashSet();
		Map tabelas = new HashMap();
		 
		String rotaPreco = null;
		String mOrigem   = null;
		String mDestino  = null;
		String ufOrigem  = null;
		String ufDestino = null;
		Object column    = null;
		Object dado      = null;
		Object adv       = null;
		String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
		String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");
		
		int i = 1;
		for (Iterator it = list.iterator(); it.hasNext(); i++) { 
			
			Map map = (Map)it.next();
			rotaPreco = MapUtils.getString(map,"FAIXA_ROTA");
			mOrigem   = MapUtils.getString(map,"ORIGEM");
			mDestino  = MapUtils.getString(map,"DESTINO");
			ufOrigem  = MapUtils.getString(map,"UF_ORIGEM");
			ufDestino = MapUtils.getString(map,"UF_DESTINO");
			column    = map.get("VALOR_FAIXA");
			dado      = map.get("VL_FIXO"); 
			adv       = map.get("VALOR_ADVALOREM");
	      
			if (!tabelas.containsKey(rotaPreco)){
				i = 1;
				
				//Concatena o uf da origem e do destino com "-" para ser usado no metodo verificaComplemento
				String ufOrigemDestino = ufOrigem+"-"+ufDestino;
				mOrigem 	= getTabelasClienteService().verificaComplemento(listaUFOrigem, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, mOrigem);
				mDestino 	= getTabelasClienteService().verificaComplemento(listaUFDestino, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, mDestino);
				
				String origem = ufOrigem + " - " + mOrigem;
				String destino = ufDestino + " - " + mDestino;
	   			
	   			if(colunas != null) {
	   				colunas.append(")\n");
	   			}

	   			if(valuesStr != null){
	   				valuesStr.append(")\n");
	   				sql = colunas.toString() + valuesStr.toString();
	   				sqlRegistros.add(sql);
	   				sqlValues.add(values.toArray());
	   			}
	   			
	   			colunas = new StringBuffer("INSERT INTO " + NOME_TABELA + " (ORIGEM, DESTINO, ADVALOREM");
	   			valuesStr = new StringBuffer(" VALUES(?, ?, ?");
	   			values = new LinkedList();
	   			values.add(origem);
	   			values.add(destino);
	   			values.add(adv);
	   			
	   			tabelas.put(rotaPreco, new ArrayList());
	   		}
	   	
			crosstab.add(column);
			colunas.append(", COLUMN" + i);
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
			getJdbcTemplate().update(sqlInsert, (Object[]) iterValues.next());
		}
		
		//ajuste dados da tabela temporária
		getTabelasClienteService().verificaUfs(listaUFOrigem,"ORIGEM",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,jdbcTemplate);
		getTabelasClienteService().verificaUfs(listaUFDestino,"DESTINO",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,jdbcTemplate);
		
		this.setCrosstab(crosstab);
	}

	
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public Set getCrosstab() {
		return (Set) this.getLocalVariableValue("crosstab");
	}

	public void setCrosstab(Set crosstab) {
		this.setLocalVariableValue("crosstab", crosstab);
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
}
	
	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
}

	}
