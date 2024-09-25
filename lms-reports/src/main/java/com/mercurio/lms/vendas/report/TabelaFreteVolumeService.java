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

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * ET: 30.03.02.34 - Tabela de Frete Volume
 * 
 * @author Rodrigo F. Dias
 *
 * @spring.bean id="lms.vendas.tabelaFreteVolumeService"
 * @spring.property name="reportName" value="/com/mercurio/lms/vendas/report/emitirTabelaFreteVolume.vm"
 *  
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="296"
 * @spring.property name="crossTabBandWidths" value="440" 
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class TabelaFreteVolumeService extends ReportServiceSupport {

	private static final String NOME_TABELA = "TMP_FRETE_VOLUME";

	private ConfiguracoesFacade configuracoesFacade = null;
	private TabelasClienteService tabelasClienteService;

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

	public JRReportDataObject execute(Map parameters) throws Exception {
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		jdbcTemplate.execute("DELETE FROM "+ NOME_TABELA);

		//Parametros recebidos da Action
		Long idCliente = MapUtils.getLong(parameters,"idCliente");
		Long idDivisao = MapUtils.getLong(parameters,"idDivisao");
		Long idTabelaDivisaoCliente = MapUtils.getLong(parameters,"idTabelaDivisaoCliente");
		Long idParametro = MapUtils.getLong(parameters,"idParametroCliente");
		Long idTabelaPreco = MapUtils.getLong(parameters,"idTabelaPreco");
		Boolean isTabelaNova = MapUtils.getBoolean(parameters, "isTabelaNova", false);
		Simulacao simulacao = (Simulacao)MapUtils.getObject(parameters, "simulacao");

		//Se não tem dados para o relatório
		if (idTabelaPreco == null) {
			return retornaVazio();
		}

		/* Obtém os dados do relatório principal */
        List result = jdbcTemplate.queryForList(montaSql(), new Long[]{idTabelaPreco,idTabelaPreco,idTabelaPreco,idTabelaPreco});
        Map parametersReport = new HashMap();
        String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco,jdbcTemplate);
		populateTable(
			 result
			,idParametro
			,jdbcTemplate
			,parametersReport
			,dsSimbolo);

		parametersReport.put("HEADER",(JRMapCollectionDataSource)getTabelasClienteService().montaHeader(parameters, jdbcTemplate,TabelasClienteService.RETORNO_DATASOURCE));
		int dataCount = getJdbcTemplate().queryForInt(" SELECT COUNT(*) AS TOTAL FROM " + NOME_TABELA);
		parametersReport.put("TOTAL", Integer.valueOf(dataCount/*/reportGroups.size()*/));
		parametersReport.put("SERVICE", this);

	    //Monta subrelatorios - Generalidades sem as parametrizacoes do cliente (3º atributo = null)
		int[] subreports = {
			TabelasClienteService.SUBREPORT_FORMALIDADES,
			TabelasClienteService.SUBREPORT_GENERALIDADES,
			TabelasClienteService.SUBREPORT_SERVICOSCONTRATADOS,
			TabelasClienteService.SUBREPORT_SERVICOSNAOCONTRATADOS
		};

		parametersReport = getTabelasClienteService().montaSubRelatoriosOfChoice(idCliente,idDivisao,idParametro,idTabelaPreco,isTabelaNova,parametersReport,subreports,configuracoesFacade,jdbcTemplate);

        String dsTipoServico = null;
        if(simulacao==null) {
        	dsTipoServico = getTabelasClienteService().getTipoServico(idTabelaDivisaoCliente, jdbcTemplate);	
        } else {
			Servico serv = simulacao.getServico();
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(serv.getIdServico(), jdbcTemplate);
			dsTipoServico =  MapUtils.getString(mapTipoServico,"DS_TIPO_SERVICO");       	
        }

		parametersReport.put("SERVICO", dsTipoServico);        
        parametersReport.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{Integer.valueOf(this.getCrosstab().size())});	
        parametersReport.put("DS_SIMBOLO",dsSimbolo);
        getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);

        JRReportDataObject jr = executeQuery("SELECT * FROM "+ NOME_TABELA +" ORDER BY TARIFA",new HashMap());
     	jr.setParameters(parametersReport);
		return jr;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
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
	
	/**
	 * Metodo chamado dentro do Jasper, para formatar os numeros com 2 ou 5
	 * casas decimais, dependendo do seu valor e do seu valor minimo
	 * 
	 * @param parametro
	 * @param psMinimo
	 * @param field
	 * @return
	 */
	public String formataCasasDecimais(String parametro,BigDecimal field) {
			String valorFormatado = "";
	       
	       if(field == null) return valorFormatado; 
	  	   
		   if(!StringUtils.isNumeric(parametro.substring(0,2))) //parametro = "Acima 'psMinimo'"
		   {
			   valorFormatado = FormatUtils.formatDecimal("##,###,###,###,##0.00000", field);
		   }
		   else
		   {
			   if(field != null)
			   {
				   valorFormatado = FormatUtils.formatDecimal("##,###,###,###,##0.00", field);
			   }
		   }
		return valorFormatado;
	}

	/**
	 * Monta sql para retornar dados da consulta do relat&oacute;rio
	 * @return
	 */
	private String montaSql() {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT Faixa.VL_FIXO, ")
			.append("	Faixa.faixa_rota_preco, ")
			.append("	Faixa.UF_Origem, ")
			.append("	Faixa.UF_Destino, ")
			.append("	Faixa.Origem, ")
			.append("	Faixa.tarifa, ")
			.append("	Faixa.Destino, ")
			.append("	Faixa.NR_COLUNA_DINAMICA,  ")		
			.append("	Faixa.vl_faixa_progressiva as Valor_faixa, ")
			.append("	Advalorem.valor_Advalorem,  ")
			.append("	FreteQuilo.vl_frete_quilo as vl_acima ")
			.append(" FROM (SELECT vfp.vl_fixo,  ")
			.append("			Fp.vl_faixa_progressiva,  ")
			.append("			tap.cd_tarifa_preco as tarifa,  ")
			.append("			uf_origem.sg_unidade_federativa as UF_Origem,  ")
			.append("			uf_destino.sg_unidade_federativa as UF_Destino,  ")
			.append("			COLUNA_DINAMICA.NR_COLUNA_DINAMICA,  ")
			.append("			rp.id_rota_preco as faixa_rota_preco, ")
			.append(" 			decode(rp.ID_MUNICIPIO_ORIGEM,null, ")
			.append("			decode(F_ORIGEM.SG_FILIAL,null, ")
			.append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append(" , ")
			.append("			decode(po_filial.nm_fantasia,null,F_ORIGEM.SG_FILIAL,F_ORIGEM.SG_FILIAL || ' - ' || po_filial.nm_fantasia)), ")
			.append("			M_ORIGEM.NM_MUNICIPIO) as ORIGEM,  ")
			.append(" 			decode(rp.ID_MUNICIPIO_DESTINO,null, ")
			.append("			decode(F_DESTINO.SG_FILIAL,null, ")
			.append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append(" , ")		
			.append("			decode(pd_filial.nm_fantasia,null,F_DESTINO.SG_FILIAL,F_DESTINO.SG_FILIAL || ' - ' || pd_filial.nm_fantasia)), ")
			.append("			M_ORIGEM.NM_MUNICIPIO) as DESTINO  ")
			.append(" 		FROM tabela_preco tp, ")
			.append("			 tipo_tabela_preco ttp, ")
			.append("			 subtipo_tabela_preco stp, ")
			.append("			 tabela_preco_parcela tpp, ")
			.append("			 parcela_preco pp, ")
			.append("			 faixa_progressiva fp, ")
			.append("			 valor_faixa_progressiva vfp, ")
			.append("			 rota_preco rp, ")
			.append("			 unidade_federativa uf_origem, ")
			.append("			 unidade_federativa uf_destino, ")
			.append("			 MUNICIPIO M_ORIGEM,  ")
			.append("			 MUNICIPIO M_DESTINO, ")
			.append("			 TIPO_LOCALIZACAO_MUNICIPIO TLM_ORIGEM,  ")
			.append("			 TIPO_LOCALIZACAO_MUNICIPIO TLM_DESTINO,  ")
			.append("			 FILIAL F_ORIGEM, ")
			.append("			 FILIAL F_DESTINO, ")
			.append("			 tarifa_preco tap, ")
			.append("			 PESSOA po_filial,  ")
			.append("			 PESSOA pd_filial,  ")
			.append("			 tarifa_preco_rota tpr, ")
			.append("			(SELECT TABCOLDIN.*, rownum as nr_coluna_dinamica ")
			.append(" 			 FROM(SELECT tp.id_tabela_preco, fp.id_faixa_progressiva, fp.vl_faixa_progressiva FROM faixa_progressiva fp,  ")
			.append(" 				tabela_preco_parcela tpp,  tabela_preco tp  ")
			.append(" 			 WHERE fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela  ")
			.append(" 				and tpp.id_tabela_preco = tp.id_tabela_preco  and tp.id_tabela_preco = ? ")
			.append(" 			 ORDER BY fp.vl_faixa_progressiva) TABCOLDIN) COLUNA_DINAMICA,  ")		
			.append("		servico s ")
			.append(" WHERE tp.id_tabela_preco = ?  ")
			.append("	AND COLUNA_DINAMICA.id_faixa_progressiva = fp.id_faixa_progressiva ")		
			.append("	AND tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco ")
			.append("	AND tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco ")
			.append("	AND tp.id_tabela_preco = tpp.id_tabela_preco ")
			.append("	AND tpp.id_parcela_preco = pp.id_parcela_preco ")
			.append("	AND ttp.id_servico = s.id_servico ")
			.append("	AND s.tp_modal = 'R' ")
			.append("	AND s.tp_abrangencia = 'N' ")
			.append("	AND pp.cd_parcela_preco = 'IDFretePeso' ")
			.append("	AND fp.cd_minimo_progressivo = 'VO' ")
			.append("	AND tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela ")
			.append("	AND fp.id_faixa_progressiva = vfp.id_faixa_progressiva ")
			.append("	AND vfp.id_tarifa_preco = tap.id_tarifa_preco ")
			.append("	AND tap.id_tarifa_preco = tpr.id_tarifa_preco ")
			.append("	AND tpr.id_tabela_preco = tp.id_tabela_preco ")
			.append("	AND tpr.id_rota_preco =  rp.id_rota_preco ")
			.append("	AND RP.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA ")
			.append("	AND RP.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
			.append("	AND RP.ID_MUNICIPIO_ORIGEM = M_ORIGEM.ID_MUNICIPIO (+) ")
			.append("	AND RP.ID_MUNICIPIO_DESTINO = M_DESTINO.ID_MUNICIPIO (+) ")
			.append("	AND RP.ID_FILIAL_ORIGEM = F_ORIGEM.ID_FILIAL (+) ")
			.append("	AND RP.ID_FILIAL_DESTINO = F_DESTINO.ID_FILIAL (+) ")
			.append(" 	AND RP.ID_FILIAL_ORIGEM = po_FILIAL.ID_Pessoa (+) ")
			.append(" 	AND RP.ID_FILIAL_DESTINO = pd_FILIAL.ID_Pessoa (+) ")
			.append("	AND RP.ID_TIPO_LOCALIZACAO_ORIGEM = TLM_ORIGEM.ID_TIPO_LOCALIZACAO_MUNICIPIO (+) ")
			.append(" 	AND RP.ID_TIPO_LOCALIZACAO_DESTINO = TLM_DESTINO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+) ")
			.append(" ) Faixa,  ")

			.append(" (SELECT pf.vl_preco_frete as vl_frete_quilo, ")
			.append("		rp.id_rota_preco as frete_quilo_rota_preco, ")
			.append(" 		tap.cd_tarifa_preco as tarifa ")
			.append(" FROM tabela_preco tp, ")
			.append("  	tipo_tabela_preco ttp, ")
			.append("  	subtipo_tabela_preco stp, ")
			.append("  	tabela_preco_parcela tpp, ")
			.append(" 	parcela_preco pp, ")
			.append("  	tarifa_preco tap, ")
			.append("  	tarifa_preco_rota tpr, ")
			.append(" 	preco_frete pf, ")
			.append(" 	rota_preco rp ")
			.append(" WHERE tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco ") 
			.append("   AND tp.id_tabela_preco = ? ")
			.append("   AND tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco ")
			.append("   AND tp.id_tabela_preco = tpp.id_tabela_preco ")
			.append("   AND tpp.id_parcela_preco = pp.id_parcela_preco ")
			.append("   AND pp.cd_parcela_preco = 'IDFreteQuilo' ")
			.append("   AND tpp.id_tabela_preco_parcela = pf.id_tabela_preco_parcela ")
			.append("   AND pf.id_tarifa_preco = tap.id_tarifa_preco ")
			.append("   AND tap.id_tarifa_preco = tpr.id_tarifa_preco ")
			.append("   AND tpr.id_tabela_preco = tp.id_tabela_preco ")
			.append("   AND tpr.id_rota_preco =  rp.id_rota_preco ")
			.append(" ) FreteQuilo, ")

			.append(" (SELECT  ")
			.append(" 	pf.vl_preco_frete as VALOR_Advalorem, rp.id_rota_preco as advalorem_rota_preco,  ")
			.append(" 	tap.cd_tarifa_preco as tarifa ")
			.append(" FROM tabela_preco tp, ")
			.append(" 	tipo_tabela_preco ttp, ")
			.append(" 	subtipo_tabela_preco stp, ")
			.append(" 	tabela_preco_parcela tpp, ")
			.append(" 	parcela_preco pp, ")
			.append(" 	tarifa_preco tap, ")
			.append(" 	tarifa_preco_rota tpr, ")
			.append(" 	preco_frete pf, ")
			.append(" 	rota_preco rp ")
			.append(" WHERE tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco  ")
			.append(" 	AND tp.id_tabela_preco = ? ")
			.append(" 	AND tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco ")
			.append(" 	AND tp.id_tabela_preco = tpp.id_tabela_preco ")
			.append(" 	AND tpp.id_parcela_preco = pp.id_parcela_preco ")
			.append(" 	AND pp.cd_parcela_preco = 'IDAdvalorem' ")
			.append(" 	AND tpp.id_tabela_preco_parcela = pf.id_tabela_preco_parcela ")
			.append(" 	AND pf.id_tarifa_preco = tap.id_tarifa_preco ")
			.append(" 	AND tap.id_tarifa_preco = tpr.id_tarifa_preco ")
			.append(" 	AND tpr.id_tabela_preco = tp.id_tabela_preco ")
			.append(" 	AND tpr.id_rota_preco =  rp.id_rota_preco ")
			.append(" ) Advalorem ")

			.append(" WHERE Faixa.faixa_rota_preco = Advalorem.advalorem_rota_preco   ")
			.append("   AND Faixa.faixa_rota_preco = FreteQuilo.frete_quilo_rota_preco   ")
			.append("   AND Faixa.tarifa = Advalorem.tarifa   ")
			.append("   AND Faixa.tarifa = FreteQuilo.tarifa   ")

			.append(" ORDER BY faixa_rota_preco, valor_faixa, UF_Origem,Origem, Uf_Destino,Destino ");
		return sql.toString();
	}

	/**
	 * M&eacute;todo para popular tabela tempor&aacute;ria do relat&oacute;rio
	 * @param list resultset com os dados da consulta
	 */
	private void populateTable(List list,Long idParametro, JdbcTemplate jdbcTemplate, Map parameters, String dsSimbolo) {
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
		Map mapParametro = getTabelasClienteService().buscaDadosParametroCliente(idParametro,getJdbcTemplate());

		String rotaPreco = null;
		String mOrigem = null;
		String mDestino = null;
		String ufOrigem = null;
		String ufDestino = null;
		String tarifa = null;
		Object column = null;
		Object dado = null;
		BigDecimal vlAcima	= null;
		BigDecimal adv = null;
		Object nrColunaDin = null, nrColunaDinOld = null;
		String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
		String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");

		for (Iterator it = list.iterator(); it.hasNext();) { 
			Map map = (Map)it.next();
			rotaPreco = MapUtils.getString(map,"FAIXA_ROTA_PRECO");
			mOrigem = MapUtils.getString(map,"ORIGEM");
			mDestino = MapUtils.getString(map,"DESTINO");
			ufOrigem = MapUtils.getString(map,"UF_ORIGEM");
			ufDestino = MapUtils.getString(map,"UF_DESTINO");
			tarifa = MapUtils.getString(map,"TARIFA");
			vlAcima = MapUtilsPlus.getBigDecimal(map,"VL_ACIMA");
			column = map.get("VALOR_FAIXA");
			dado = map.get("VL_FIXO"); 
			adv = MapUtilsPlus.getBigDecimal(map,"VALOR_ADVALOREM");
			nrColunaDin = map.get("NR_COLUNA_DINAMICA");

			if (!tabelas.containsKey(rotaPreco)) {
				if ( StringUtils.isNotBlank(sql) ) {
					sqlRegistros.add(sql);
					sqlValues.add(values.toArray());
				}

				if(colunas != null){
					int columnAcima = Integer.parseInt(""+nrColunaDinOld) +1;
					colunas.append(", COLUMN" + columnAcima);
					colunas.append(")");
				}
				if(valuesStr != null){
					valuesStr.append(", ?");
					values.add(vlAcima);
					valuesStr.append(")");
					sql = colunas.toString() + valuesStr.toString();
				}

				//Concatena o uf da origem e do destino com "-" para ser usado no metodo verificaComplemento
				String ufOrigemDestino = ufOrigem+"-"+ufDestino;
				mOrigem = getTabelasClienteService().verificaComplemento(listaUFOrigem, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, mOrigem);
				mDestino = getTabelasClienteService().verificaComplemento(listaUFDestino, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, mDestino);

				String origem = ufOrigem + " - " + mOrigem;
				String destino = ufDestino + " - " + mDestino;

	   			//aplica as regras do parametro do cliente no advalorem
				adv = getTabelasClienteService().aplicaRegraAdvalorem(mapParametro,adv);

	   			colunas = new StringBuffer("INSERT INTO " + NOME_TABELA + " (ORIGEM, DESTINO, ADVALOREM, TARIFA");
	   			valuesStr = new StringBuffer(" VALUES(?, ?, ?, ?");
	   			values = new LinkedList();
	   			values.add(origem);
	   			values.add(destino);
	   			values.add(adv);
	   			values.add(tarifa);

	   			tabelas.put(rotaPreco, new ArrayList());
	   		}

			crosstab.add(column);
			parameters.put(("PCOLUMN"+nrColunaDin.toString()), (column.toString()+ " volumes\r(" + dsSimbolo + "/CRTC)"));

			colunas.append(", COLUMN"+nrColunaDin);
			valuesStr.append(", "+ dado);

			nrColunaDinOld = nrColunaDin;
		}

		//finaliza o ultimo registro
		if(colunas != null) {
			int iColumnAcima = Integer.parseInt(""+nrColunaDinOld) + 1;

			crosstab.add("Acima");
			parameters.put(("PCOLUMN"+iColumnAcima), ("Acima de\r" + column + " volumes\r(" + dsSimbolo + "/volume)"));
	   		colunas.append(", COLUMN" + iColumnAcima);
			colunas.append(")");
		}
		if(valuesStr != null) {
			valuesStr.append(", ?");
			values.add(vlAcima);
			valuesStr.append(")");
			sql = colunas.toString() + valuesStr.toString();
			sqlRegistros.add(sql);
			sqlValues.add(values.toArray());
		}

		Iterator iterValues = values.iterator();
		for (Iterator iter = sqlRegistros.iterator(); iter.hasNext();) {
			String sqlInsert = (String) iter.next();
			jdbcTemplate.update(sqlInsert, (Object[])iterValues.next());
		}

		//ajuste dados da tabela temporária
		getTabelasClienteService().verificaUfs(listaUFOrigem,"ORIGEM",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,jdbcTemplate);
		getTabelasClienteService().verificaUfs(listaUFDestino,"DESTINO",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,jdbcTemplate);

		this.setCrosstab(crosstab);
	}

	public Set getCrosstab() {
		return (Set) this.getLocalVariableValue("crosstab");
	}
	public void setCrosstab(Set crosstab) {
		this.setLocalVariableValue("crosstab", crosstab);
	}

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}
}