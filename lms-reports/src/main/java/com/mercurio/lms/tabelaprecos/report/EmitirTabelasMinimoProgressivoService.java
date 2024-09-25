package com.mercurio.lms.tabelaprecos.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JREmptyDataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;
import com.mercurio.lms.vendas.util.TabelasClienteUtil;

/**
 * 
 * ET: 30.03.02.03 - Tabela Minimo Progressivo
 * 
 * @spring.bean id="lms.tabelaprecos.report.emitirTabelasMinimoProgressivoService" 
 * @spring.property name="reportName" value="/com/mercurio/lms/tabelaprecos/report/emitirTabelasMinimoProgressivo.vm"
 * @spring.property name="numberOfCrosstabs" value="1" 
 * @spring.property name="crossTabLefts" value="35"
 * @spring.property name="crossTabBandWidths" value="439" 
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class EmitirTabelasMinimoProgressivoService extends ReportServiceSupport { 
	
	/* Nome da Tebela temporaria */
	private static final String NOME_TABELA = "TMP_MN_PROGS";	

	/**
	 * Para guardar o idTabelaPreco e o dsSimbolo e compartilhar entre metodos
	 */
	private static final ThreadLocal dadosClasseThread = new ThreadLocal();
	
    /* Para utilização de RecursosMensagens*/
    private ConfiguracoesFacade configuracoesFacade;
	private TabelasClienteService tabelasClienteService;


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
     * @param key have do atributo local
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

	public List<Map<String, String>> findDados(Map parameters) throws Exception {
    	Boolean emitirTonelada = MapUtils.getBoolean(parameters, "emitirTonelada", false);
    	Long idSimulacao = MapUtils.getLong(parameters, "idSimulacao");
    	String relatorio_et = (getRelatorio_et()==null) ? "30.03.02.03" : (String)getLocalVariableValue("relatorio_et");
    	String sql = (getSql()==null) ? (String)montaSql(emitirTonelada) : (String)getLocalVariableValue("sql");
    	Map tabelas = (getLocalVariableValue("tabelas")==null) ? new HashMap() : (HashMap)getLocalVariableValue("tabelas");
    	Set crosstab = (getCrosstab()==null) ? null : (Set)getLocalVariableValue("crosstab");
    	BigDecimal psMinimo = (getPsMinimo()==null) ? null : (BigDecimal)getLocalVariableValue("psMinimo");
    	String dsSimbolo = (getDsSimbolo()==null) ? null : (String)getLocalVariableValue("dsSimbolo");
    	Integer posPsminInFaixa = (getPosPsminInFaixa()==null) ? null : (Integer)getLocalVariableValue("posPsminInFaixa");
    	Set faixaProgColunmName = new LinkedHashSet();
    	
        /* Obtém os dados do relatório principal */
    	JdbcTemplate jdbcTemplate = getJdbcTemplate(); 
    	Long idTabelaPreco = Long.valueOf(parameters.get("tabelaPreco.idTabelaPreco").toString());
    	
    	if(dsSimbolo == null)
    		dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco, jdbcTemplate);

    	this.setRelatorio_et(relatorio_et);
    	this.setSql(sql);
    	this.setCrosstab(crosstab);
    	setLocalVariableValue("tabelas", tabelas);
    	this.setPsMinimo(psMinimo);
    	this.setDsSimbolo(dsSimbolo);
    	this.setPosPsminInFaixa(posPsminInFaixa);
    	
    	List<Map<String, String>> result = jdbcTemplate.queryForList(getSql(), new Long[]{idTabelaPreco,idTabelaPreco, idTabelaPreco});    	
    	
        calcReport(result, idTabelaPreco, faixaProgColunmName, emitirTonelada.booleanValue(), idSimulacao, jdbcTemplate); 
        Map anterior = new HashMap();
        for (Map<String, String> map : result) {
        	map.put("VALOR_FAIXA", FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VALOR_FAIXA")));
        	if(MapUtilsPlus.getBigDecimal(map,"FAIXA_PROGRESSIVA").longValue() > 200){
        		anterior.put("VALOR_FRETE", map.get("VALOR_FAIXA"));		
        	}

	    	map.put("VL_ADVALOREM", FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VL_ADVALOREM")));
	    	if(map.get("VALOR_FAIXA_ADICIONAL") != null){	    		
	    		map.put("VALOR_FAIXA_ADICIONAL", FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map,"VALOR_FAIXA_ADICIONAL")));
        }
	    	anterior = map;
		}

    	String subtipo = getSubtipoTabelaPreco(idTabelaPreco, jdbcTemplate);
    	if(subtipo.equalsIgnoreCase("Y"))
    	{
    		parameters.put("OBSERVACAO_1", "******* N/B ATÉ 200 KGs DESPACHO POR CONHECIMENTO INCLUSO.");
    	}
    	else
    	{
    		parameters.put("OBSERVACAO_1", "");
    	}
    	
    	parameters.put("DS_SIMBOLO",dsSimbolo);
    	parameters.put("PS_MINIMO",this.getPsMinimo());
    	parameters.put(ReportServiceSupport.CT_NUMBER_OF_COLS,new Integer[]{Integer.valueOf(this.getCrosstab().size())});
        
    	parameters.put("usuarioEmissor",SessionContext.getUser().getNmUsuario());
        int totRegistros = getTabelasClienteService().getCountTable(NOME_TABELA, jdbcTemplate);
        parameters.put("TOTAL", Integer.valueOf(totRegistros));
		
       setSubReportsParameters(parameters, idTabelaPreco, jdbcTemplate, totRegistros, configuracoesFacade);	
        
        if(this.getRelatorio_et().equalsIgnoreCase("30.03.02.03")) {
        	getTabelasClienteService().applyDiscount(faixaProgColunmName, NOME_TABELA, jdbcTemplate);
        	this.setFaixaProgColunmName(null);
        }
        
        return result;
    }

    /**
     * Retorna o subtipo da tabela com o idendificador especifico
     * 
     * @param idTabelaPreco identificador da tabela de preço
     * @param jdbcTemplate conexao
     * @return subtipo da tabela
     */
    private static String getSubtipoTabelaPreco(Long idTabelaPreco, JdbcTemplate jdbcTemplate)
    {
    	/* Obtém o subtipo da tabela */
    	StringBuffer sqlSubtipoTP = new StringBuffer("");
    	sqlSubtipoTP.append("select TP_SUBTIPO_TABELA_PRECO from SUBTIPO_TABELA_PRECO stp, TABELA_PRECO tp ")
    	.append(" where stp.ID_SUBTIPO_TABELA_PRECO = tp.ID_SUBTIPO_TABELA_PRECO and ID_TABELA_PRECO = ?");
    	
    	List resSubtipo = jdbcTemplate.queryForList(sqlSubtipoTP.toString(), new Long[]{idTabelaPreco});
    	Map mapsubtipo = new HashMap();
    	mapsubtipo = (Map)resSubtipo.get(0);
    	String subtipo = MapUtilsPlus.getString(mapsubtipo, "TP_SUBTIPO_TABELA_PRECO", "");
    	
    	return subtipo;
    }
    
    /**
     * Seta subreports nos parametros do relatorio
     * 
     * @param parameters
     * @param idTabelaPreco
     * @param jdbcTemplate
     * @param configuracoesFacade
     */
	private void setSubReportsParameters(Map parameters, Long idTabelaPreco, JdbcTemplate jdbcTemplate, int totRegsRelatorio, ConfiguracoesFacade configuracoesFacade)
	{
		Set crosstab = getCrosstab();
		
		if(getPosPsminInFaixa()==null)
			setPosPsminInFaixa(Integer.valueOf(crosstab.size()));

		getTabelasClienteService().montaSubReportsTabelaPreco(idTabelaPreco, getSubReportsToCall(), NOME_TABELA, crosstab.size(), getPosPsminInFaixa().intValue(), configuracoesFacade,
				jdbcTemplate, parameters);
        
        }

	/**
	 * 
	 * @return
	 */
	private int[] getSubReportsToCall()
	{
		return new int[]{
				TabelasClienteUtil.SUBREPORT_GENERALIDADES_TABELA_PRECO,
				TabelasClienteUtil.SUBREPORT_FORMALIDADES,
				TabelasClienteUtil.SUBREPORT_SERVICOSAD,
				TabelasClienteUtil.SUBREPORT_GENERALIDADE_DIFICULDADE_ENTREGA};		
	}
	
	/**
	 * 
	 * @param list 
	 * @param faixaProgColunmName
	 */
    private void calcReport(List list, Long idTabelaPreco, Set faixaProgColunmName, boolean emitirTonelada, Long idSimulacao, JdbcTemplate jdbcTemplate)
    {
    	LinkedHashSet crosstab = new LinkedHashSet();
    	
        Map faixaProgWithDiscount = new HashMap();
        Map tabelas = new HashMap();
        setLocalVariableValue("tabelas", tabelas);
        String acimaDe = configuracoesFacade.getMensagem("acimaDe");
        
        BigDecimal faixaProgress = null, psMinimo = null;
        String tarifaAux = "", dsSimbolo = null;
        long maxFaixaProgress = getMaxFaixaProgressiva(idTabelaPreco, emitirTonelada, jdbcTemplate);
        int contTarifa = 0;
        
        for (Iterator it = list.iterator(); it.hasNext(); )
        { 
           Map map = (Map)it.next();
           String tarifa = map.get("COD_TARIFA").toString();
           Object column = map.get("FAIXA_PROGRESSIVA");
           Object dado   = map.get("VALOR_FAIXA");
           Object acima  = map.get("VALOR_FAIXA_ADICIONAL");           
           Object adv    = map.get("VL_ADVALOREM");
           Object desconto = map.get("PC_DESCONTO");
           
           psMinimo = new BigDecimal(MapUtils.getString(map,"PS_MINIMO","0"));
           this.setPsMinimo(psMinimo);
           
           dsSimbolo = MapUtils.getString(map,"DS_SIMBOLO");
           this.setDsSimbolo(dsSimbolo);
           
           faixaProgress = new BigDecimal(MapUtils.getString(map,"FAIXA_PROGRESSIVA","0"));
           
           int resultCompareFaixaProgress = faixaProgress.compareTo(psMinimo);
           if(resultCompareFaixaProgress < 1) //Se faixaProgress menor(-1) ou igual(0) a psMinimo
           {
        	   if(!tarifa.equals(tarifaAux))
        	   {
        		   tarifaAux = tarifa; 
        		   contTarifa = 1;
        	   } 
        	   else
        	   {
        		   contTarifa++;
        	   }
        	   this.setPosPsminInFaixa(Integer.valueOf(contTarifa));
           }
                                               
       	   //Não mostrar toneladas se emitir Tonelada estiver desmarcado
           if(idSimulacao == null){
	       	   if(!emitirTonelada && resultCompareFaixaProgress == 1)
	       	   {
	       		   //não adiciona faixa da tonelada
	       	   }
	       	   else
	       	   {
	               crosstab.add(column != null ? column.toString() : column);
	               if(maxFaixaProgress<=psMinimo.longValue() && faixaProgress.longValue() == maxFaixaProgress)
	            	   crosstab.add(acimaDe + " " + faixaProgress.longValue());
	               
	       		   if (!tabelas.containsKey(tarifa))
	               {
	                   tabelas.put(tarifa, new ArrayList());
	                   ((ArrayList)tabelas.get(tarifa)).add(adv);                                      
	               }
	               ((ArrayList)tabelas.get(tarifa)).add(dado);
	               if(maxFaixaProgress<=psMinimo.longValue() && faixaProgress.longValue() == maxFaixaProgress)
	            	   ((ArrayList)tabelas.get(tarifa)).add(acima);
	               
	               if(desconto != null)
	               {
	            	   faixaProgWithDiscount.put((column != null ? column.toString() : column), desconto);
	               }
	       	   }
           }else{
        	   if(this.getRelatorio_et().equalsIgnoreCase("30.03.02.15")){
        		   if(!emitirTonelada && resultCompareFaixaProgress == 1)
        			   continue;
        	   }
        	   
               crosstab.add(column != null ? column.toString() : column);
               if(maxFaixaProgress<=psMinimo.longValue() && faixaProgress.longValue() == maxFaixaProgress)
            	   crosstab.add(acimaDe + " " + faixaProgress.longValue());
               
       		   if (!tabelas.containsKey(tarifa))
               {
                   tabelas.put(tarifa, new ArrayList());
                   ((ArrayList)tabelas.get(tarifa)).add(adv);                                      
               }
               ((ArrayList)tabelas.get(tarifa)).add(dado);
               if(maxFaixaProgress<=psMinimo.longValue() && faixaProgress.longValue() == maxFaixaProgress)
            	   ((ArrayList)tabelas.get(tarifa)).add(acima);
               
               if(desconto != null)
               {
            	   faixaProgWithDiscount.put((column != null ? column.toString() : column), desconto);
               }
               
           }
        }//for iterator da list
               
        String names = "", columnName = null;
        int i = 0;

        //definicao do desconto das colunas
        for (Iterator j = crosstab.iterator(); j.hasNext();)
    	{
        	Object col = j.next();
        	columnName = "COLUMN" + ++i;
        	names += ", " + columnName;
        	Object discount = null;
        	
        	if(faixaProgWithDiscount.containsKey(col))
        	{
        		Map map = new HashMap();
        		//nome da coluna, desconto da coluna da faixa progressiva
        		discount = faixaProgWithDiscount.get(col);
        		map.put(columnName, discount);
        		if(!faixaProgColunmName.contains(map))
        		{
        			faixaProgColunmName.add(map);
        		}
        	}
    	}
        
        this.setFaixaProgColunmName(faixaProgColunmName);
        
        //insercao na tabela temporaria
        for (Iterator it = tabelas.keySet().iterator(); it.hasNext(); ) {
           String tarifa = (String)it.next();
           int cont = 0;
           List lista    = (List)tabelas.get(tarifa);
           StringBuffer sb = new StringBuffer("INSERT INTO " + NOME_TABELA + " (TARIFA, ADVALOREM" + names + ") VALUES('"+ tarifa +"'  ");
           for (Iterator it2 = lista.iterator(); it2.hasNext(); ) {
        	   if(cont <= crosstab.size()){
        	           sb.append(", "+it2.next());
        	   }else{
        		   break;
        	   }
        	   cont++;
           }
           sb.append(")\n");
           getJdbcTemplate().execute(sb.toString());
        }

        setCrosstab(crosstab);
    }

    /**
     * 
     * @param parametro
     * @param dsSimbolo
     * @param psMinimo
     * @return
     */
    public String formataColumnParameter(String parametro, String dsSimbolo, BigDecimal psMinimo)
    {
    	/*
    	 * Double.parseDouble($P{COLUMN2}) <= $P{PS_MINIMO}.doubleValue() ?
    	 $P{COLUMN2}+$R{kg}+new String("\n")+new String("(")+$P{DS_SIMBOLO}+$R{ctrc}+new String(")") :
    	 $P{COLUMN2}+$R{kg}+new String("\n")+new String("(")+$P{DS_SIMBOLO}+$R{bkg}+new String(")")
    	 */
    	String formatedValue = "";
    	
    	if(StringUtils.isNumeric(parametro))
    	{
    	
    		if(Long.parseLong(parametro) <= psMinimo.longValue())
    		{
    			formatedValue = parametro + " Kg\n(" + dsSimbolo + "/CTRC)";
    		}
    		else
    		{
    			formatedValue = parametro + " Kg\n(" + dsSimbolo + "/Kg)";
    		}
    	}
    	else
    	{
    		formatedValue = parametro + " Kg";
    	}

    	return formatedValue;
    }
    
    /**
	 * Metodo chamado dentro do Jasper, para formatar os numeros com 2 ou 5 casas decimais,
	 * dependendo do seu valor e do seu valor minimo
	 * @param parametro
	 * @param psMinimo
	 * @param field
	 * @return
	 */
    public String formataCasasDecimais(String parametro, BigDecimal psMinimo, BigDecimal field){
    	return getTabelasClienteService().formataCasasDecimais(parametro,psMinimo,field);
    }

    private Long getMaxFaixaProgressiva(Long idTabelaPreco, Boolean emitirTonelada, JdbcTemplate jdbcTemplate)
    {
    	String sql = "select max(FAIXA_PROGRESSIVA) from " + getSubQueryFaixa(emitirTonelada);
    	return jdbcTemplate.queryForLong(sql, new Long[]{idTabelaPreco});
    }
    
    /**
     * Retorna sql da subquery de faixas do relatorio
     * @return
     */
    private String getSubQueryFaixa(Boolean emitirTonelada)
    {
    	StringBuffer sql = new StringBuffer();
		sql.append("(select  F.VL_FAIXA_PROGRESSIVA AS FAIXA_PROGRESSIVA, ") 
		.append(" vfp.NR_FATOR_MULTIPLICACAO AS FATOR_MULTIPLICACAO, ")
		.append(" vfp.PC_DESCONTO, ")
		.append(" TAB.PS_MINIMO AS PS_MINIMO,  ")
		.append(" MO.DS_SIMBOLO AS DS_SIMBOLO ")
		.append(" from    TABELA_PRECO TAB, "  )
		.append(" TABELA_PRECO_PARCELA T, " )
		.append(" PARCELA_PRECO P, ")
		.append(" FAIXA_PROGRESSIVA F,  ")
		.append(" VALOR_FAIXA_PROGRESSIVA VFP, ")
		.append(" MOEDA MO ")
		.append(" where T.ID_TABELA_PRECO = ? ")
		.append(" AND  TAB.ID_TABELA_PRECO = T.ID_TABELA_PRECO  ")
		.append(" AND  T.ID_PARCELA_PRECO = P.ID_PARCELA_PRECO ")
		.append(" AND  P.TP_PARCELA_PRECO = 'P'  ")
		.append(" AND  P.TP_PRECIFICACAO = 'M' ")
		.append(" AND  VFP.ID_FAIXA_PROGRESSIVA = F.ID_FAIXA_PROGRESSIVA ")
		.append(" AND TAB.ID_MOEDA = MO.ID_MOEDA ");
		
		if(!emitirTonelada.booleanValue())
		{
			sql.append(" AND F.VL_FAIXA_PROGRESSIVA <= TAB.PS_MINIMO");
		}
			
		sql.append(" AND  T.ID_TABELA_PRECO_PARCELA = F.ID_TABELA_PRECO_PARCELA) FAIXA ");
		
		return sql.toString();
    }
    
    /**
     * 
     * @return
     */
    private String montaSql(Boolean emitirTonelada)
    {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select  TARIFA.COD_TARIFA, ")
		.append(" FAIXA.FAIXA_PROGRESSIVA,  ")
		.append(" TARIFA.VALOR_FRETE,  ")
		.append(" FAIXA.FATOR_MULTIPLICACAO,  ")
		.append(" TARIFA.VALOR_FRETE * FAIXA.FATOR_MULTIPLICACAO AS VALOR_FAIXA, ")
		.append(" decode(sign(FAIXA_PROGRESSIVA - FAIXA.PS_MINIMO),0,TARIFA.VALOR_FRETE) as VALOR_FAIXA_ADICIONAL, ")
		.append(" FAIXA.PC_DESCONTO, ")
		.append(" ADVALOREM.VL_ADVALOREM, ")
		.append(" FAIXA.PS_MINIMO, ")
		.append(" FAIXA.DS_SIMBOLO ")
		.append(" from ")

		.append(this.getSubQueryFaixa(emitirTonelada)).append(", ")
		
		.append(" (SELECT  TAR.ID_TARIFA_PRECO AS ID_TARIFA, ")
		.append("  FP.VL_PRECO_FRETE AS VALOR_FRETE, ")
		.append("  TAR.CD_TARIFA_PRECO AS COD_TARIFA ")
        .append("  FROM    TABELA_PRECO_PARCELA T, ")
        .append("  PARCELA_PRECO P, ")
		.append("  PRECO_FRETE FP, ")
		.append(" TARIFA_PRECO TAR ")
	    .append(" where T.ID_TABELA_PRECO = ? ")
		.append(" AND  T.ID_PARCELA_PRECO = P.ID_PARCELA_PRECO ")
		.append(" AND  P.TP_PARCELA_PRECO = 'P'  ")
		.append(" AND  P.TP_PRECIFICACAO = 'P' ")
		.append(" AND  P.CD_PARCELA_PRECO = 'IDFreteQuilo' ")
		.append(" AND  T.ID_TABELA_PRECO_PARCELA = FP.ID_TABELA_PRECO_PARCELA ")
		.append(" AND  FP.ID_TARIFA_PRECO = TAR.ID_TARIFA_PRECO) TARIFA, ")
		
		.append(" (SELECT TAR.ID_TARIFA_PRECO  AS ID_ADVALOREM,  ")
		.append(" FP.VL_PRECO_FRETE AS VL_ADVALOREM ")
		.append(" FROM   TABELA_PRECO_PARCELA T,  ")
		.append(" PARCELA_PRECO P,  ")
		.append(" PRECO_FRETE FP,  ")
		.append(" TARIFA_PRECO TAR ")
		.append(" where T.ID_TABELA_PRECO = ? ")
		.append(" AND  T.ID_PARCELA_PRECO = P.ID_PARCELA_PRECO ")
		.append(" AND  P.TP_PARCELA_PRECO = 'P'  ")
		.append(" AND  P.TP_PRECIFICACAO = 'P' ")
		.append(" AND  P.CD_PARCELA_PRECO = 'IDAdvalorem' ")
		.append(" AND  T.ID_TABELA_PRECO_PARCELA = FP.ID_TABELA_PRECO_PARCELA ")
		.append(" AND  FP.ID_TARIFA_PRECO = TAR.ID_TARIFA_PRECO) ADVALOREM ")

		.append(" WHERE ADVALOREM.ID_ADVALOREM = TARIFA.ID_TARIFA ")
		.append(" ORDER BY  TARIFA.COD_TARIFA,  FAIXA.FAIXA_PROGRESSIVA ");
		return sql.toString();
    }

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public int getNumColumnsCrosstab()
	{
		return (getCrosstab()==null)? 0 : getCrosstab().size();
	}
	
	public Set getCrosstab() {
		return (Set)getLocalVariableValue("crosstab");
	}

	public void setCrosstab(Set crosstab) {
		setLocalVariableValue("crosstab", crosstab);
	}

	public String getDsSimbolo() {
		return (String)getLocalVariableValue("dsSimbolo");
	}

	public void setDsSimbolo(String dsSimbolo) {
		setLocalVariableValue("dsSimbolo", dsSimbolo);
	}

	public BigDecimal getPsMinimo() {
		return (BigDecimal)getLocalVariableValue("psMinimo");
	}

	public void setPsMinimo(BigDecimal psMinimo) {
		setLocalVariableValue("psMinimo", psMinimo);
	}

	public String getSql() {
		return (String)getLocalVariableValue("sql");
	}

	public void setSql(String sql) {
		setLocalVariableValue("sql", sql);
	}

	public String getRelatorio_et() {
		return (String)getLocalVariableValue("relatorio_et");
	}

	public void setRelatorio_et(String relatorio_et) {
		setLocalVariableValue("relatorio_et", relatorio_et);
	}

	public Integer getPosPsminInFaixa() {
		return (Integer)getLocalVariableValue("posPsminInFaixa");
	}

	public void setPosPsminInFaixa(Integer posPsminInFaixa) {
		setLocalVariableValue("posPsminInFaixa", posPsminInFaixa);
	}

	public Set getFaixaProgColunmName() {
		return (Set)getLocalVariableValue("faixaProgColunmName");
	}

	public void setFaixaProgColunmName(Set faixaProgColunmName) {
		setLocalVariableValue("faixaProgColunmName", faixaProgColunmName);
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
}

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}
}
