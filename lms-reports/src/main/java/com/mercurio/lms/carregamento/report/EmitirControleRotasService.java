package com.mercurio.lms.carregamento.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Recibo de Posto de Passagem - RPP
 * Especificação técnica 05.01.01.05
 * @author Christian Brunkow
 * 
 * @spring.bean id="lms.carregamento.emitirControleRotasService"
 * @spring.property name="reportName" value="com/mercurio/lms/carregamento/report/emitirControleRotas.jasper"
 */
public class EmitirControleRotasService extends ReportServiceSupport {
	
	private DomainValueService domainValueService;
	private ConfiguracoesFacade configuracoesFacade;

	private static String PDF_REPORT_NAME = "com/mercurio/lms/carregamento/report/emitirControleRotas.jasper";
    private static String EXCEL_REPORT_NAME = "com/mercurio/lms/carregamento/report/emitirControleRotasExcel.jasper";

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	/**
     * método responsável por gerar o relatório. 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	
    	TypedFlatMap criteria = (TypedFlatMap) parameters;
    	
    	Long idRotaIdaVolta = criteria.getLong("rotaIdaVolta.idRotaIdaVolta");
    	String dsRota = criteria.getString("dsRota");
    	Long idFilialOrigem = criteria.getLong("filialOrigem.idFilial");
    	String sgFilialOrigem = criteria.getString("filialOrigem.sgFilial");
    	Long idFilialDestino = criteria.getLong("filialDestino.idFilial");
    	String sgFilialDestino = criteria.getString("filialDestino.sgFilial");
    	
    	YearMonthDay dtInicial = criteria.getYearMonthDay("dataInicial");
    	YearMonthDay dtFinal = criteria.getYearMonthDay("dataFinal");
    	
    	Boolean blSomenteComAtraso = criteria.getBoolean("somenteComAtraso");
    	boolean isPdf = parameters.get("tpFormatoRelatorio") != null && parameters.get("tpFormatoRelatorio").equals("pdf");
    	SqlTemplate sql = this.getSql(idFilialOrigem, sgFilialOrigem, idFilialDestino, sgFilialDestino,
    			idRotaIdaVolta, dsRota, blSomenteComAtraso.booleanValue(), dtInicial, dtFinal, isPdf);
        
        JRReportDataObject jrReportDataObject = executeQuery(sql.getSql(), sql.getCriteria());
        
        if(isPdf){
			this.setReportName(PDF_REPORT_NAME);				
		} else {
			this.setReportName(EXCEL_REPORT_NAME);
		}

        Map parametersReport = new HashMap();
        
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio").toString());
        
        jrReportDataObject.setParameters(parametersReport);
        
        return jrReportDataObject;
    }
    
    /**
     * Método que monta o select para a consulta.
     * 
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param idRota
     * @param somenteComAtraso
     * @param dtInicial
     * @param dtFinal
     * @param isPdf a ordenação é diferente quando o muda o tpFormatoRelatorio
     * @return SqlTemplate
     */
    private SqlTemplate getSql(Long idFilialOrigem, String sgFilialOrigem, Long idFilialDestino, String sgFilialDestino, 
    						   Long idRota, String dsRota, boolean somenteComAtraso, YearMonthDay dtInicial, YearMonthDay dtFinal, boolean isPdf) { 
    	
    	SqlTemplate sql = createSqlTemplate();    	
    	
    	sql.addProjection("principal.*");
    	sql.addProjection("carregamentoDescarga.*");
    	
    	sql.addFrom("(" + this.getSubPrincipal() + ")", "principal");
    	sql.addFrom("(" + this.getSubCarregamentoDescarga() + ")", "carregamentoDescarga");
    	
    	sql.addJoin("principal.idFilialDestinoCT", "carregamentoDescarga.idFilialCD(+)");
    	sql.addJoin("principal.idcontrolecarga", "carregamentoDescarga.idControleCargaCD(+)");
    	
    	//Criteria...
    	
    	sql.addCriteria("TRUNC (CAST(principal.dhprevisaosaida AS DATE))", ">=", dtInicial);
    	if (dtInicial!=null)sql.addFilterSummary("periodoInicial",  JTFormatUtils.format(dtInicial));
    	
		sql.addCriteria("TRUNC (CAST(principal.dhprevisaosaida AS DATE))", "<=", dtFinal);
		if (dtFinal!=null)sql.addFilterSummary("periodoFinal",  JTFormatUtils.format(dtFinal));

    	sql.addCriteria("principal.idRotaIdaVolta" , "=", idRota);
    	if (dsRota!=null)sql.addFilterSummary("rotaViagem", dsRota);
		
    	sql.addCriteria("principal.idFilialOrigemCT" , "=", idFilialOrigem);
    	if (sgFilialOrigem!=null)sql.addFilterSummary("filialOrigem", sgFilialOrigem);
    	
    	sql.addCriteria("principal.idFilialDestinoCT" , "=", idFilialDestino);
    	if (sgFilialDestino!=null)sql.addFilterSummary("filialDestino", sgFilialDestino);
    	
    	if (somenteComAtraso) {
    		sql.addCustomCriteria("((principal.dhsaida > principal.dhprevisaosaida) or (principal.dhchegada > principal.dhprevisaochegada))");
    		
    		sql.addFilterSummary("somenteAtraso", configuracoesFacade.getMensagem("sim"));
    	}
    	if(isPdf){
    	sql.addOrderBy("principal.idRota");
    	sql.addOrderBy("principal.dsRota");
    	sql.addOrderBy("principal.idControleCarga");
    	sql.addOrderBy("principal.idControleTrecho");
    	} else {
    		sql.addOrderBy("principal.dsRota");
    		sql.addOrderBy("principal.sgFilial");
    		sql.addOrderBy("principal.nrControleCarga");
    		sql.addOrderBy("principal.dhprevisaosaida");
    		sql.addOrderBy("principal.dhprevisaochegada");
    	
    	}
    	
        return sql;         
    }
    
	/**
	 * Retorna um subSelect para a consulta carregamento descarga
	 * 
	 * @return
	 */
	private String getSubPrincipal() {
	
		SqlTemplate sql = createSqlTemplate();
		
		//Projection...
		sql.addProjection("rota.id_Rota", "idRota");
		sql.addProjection("rotaIdaVolta.id_rota_ida_volta", "idRotaIdaVolta");
		sql.addProjection("controleCarga.id_Controle_carga", "idControleCarga");
		sql.addProjection("controleTrecho.id_Controle_trecho", "idControleTrecho");
		sql.addProjection("filialOrigemCT.id_filial", "idFilialOrigemCT");
		sql.addProjection("filialDestinoCT.id_filial", "idFilialDestinoCT");
		sql.addProjection("rota.ds_rota", "dsRota");
		sql.addProjection("filialcc.sg_filial", "sgFilial");
		sql.addProjection("controlecarga.nr_controle_carga", "nrControlecarga");
		sql.addProjection("meiotransporte.nr_frota", "nrFrota");
		sql.addProjection("meiotransporte.nr_identificador", "nrIdentificador");
		sql.addProjection("filialorigemct.sg_filial", "sgfilialorigemct");
		sql.addProjection("filialdestinoct.sg_filial", "sgfilialdestinoct");
		sql.addProjection("controletrecho.dh_previsao_saida", "dhprevisaosaida");
		sql.addProjection("controletrecho.dh_saida", "dhsaida");
		sql.addProjection("controletrecho.nr_tempo_viagem", "nrtempoviagem");
		sql.addProjection("controletrecho.dh_previsao_chegada", "dhprevisaochegada");
		sql.addProjection("controletrecho.dh_chegada", "dhchegada");
		sql.addProjection("(" + this.getSubSelectEventoControleCarga() + ")", "dhEventoControleCarga");
		
		//From...
		sql.addFrom("controle_carga", "controlecarga");
		sql.addFrom("rota");
		sql.addFrom("rota_ida_volta", "rotaidavolta");
		sql.addFrom("filial", "filialcc");
		sql.addFrom("controle_trecho", "controletrecho");
		sql.addFrom("filial", "filialorigemct");
		sql.addFrom("filial", "filialdestinoct");
		sql.addFrom("meio_transporte", "meiotransporte");
		
		sql.addJoin("controlecarga.id_rota_ida_volta", "rotaidavolta.id_rota_ida_volta");
		sql.addJoin("rotaidavolta.id_rota", "rota.id_rota");
		sql.addJoin("controlecarga.id_filial_origem", "filialcc.id_filial");
		sql.addJoin("controlecarga.id_transportado", "meiotransporte.id_meio_transporte(+)");
		sql.addJoin("controlecarga.id_controle_carga", "controletrecho.id_controle_carga");
		sql.addJoin("controletrecho.id_filial_origem", "filialorigemct.id_filial");
		sql.addJoin("controletrecho.id_filial_destino", "filialdestinoct.id_filial");
		
		//Criteria...
		sql.addCustomCriteria("controletrecho.bl_trecho_direto ='S'");
		
		return sql.getSql();
	}
	
	/**
     * Retorna um subSelect para a consulta controle de rotas...
     * 
     * @return
     */
    private String getSubSelectEventoControleCarga() {
    
    	SqlTemplate sql = createSqlTemplate();
    	sql.addProjection("dh_evento", "dhEvento");
    	sql.addFrom("evento_controle_carga", "eventoControleCarga");
    	sql.addCustomCriteria("eventoControleCarga.id_controle_carga = controleCarga.id_controle_carga");
    	sql.addCustomCriteria("eventoControleCarga.id_filial = controleTrecho.id_filial_origem");
    	sql.addCustomCriteria("eventoControleCarga.TP_EVENTO_CONTROLE_CARGA = 'EM'");
    	sql.addCustomCriteria("ROWNUM = 1");
    	
    	return sql.getSql();
    }


    /**
     * Retorna um subSelect para a consulta carregamento descarga
     * 
     * @return
     */
    private String getSubCarregamentoDescarga() {

    	SqlTemplate sql = createSqlTemplate();
    	sql.addProjection("dh_inicio_operacao", "dhiniciooperacao");
    	sql.addProjection("dh_fim_operacao", "dhfimoperacao");
    	sql.addProjection("id_filial", "idFilialCD");
    	sql.addProjection("id_controle_carga", "idControleCargaCD");
    	sql.addFrom("carregamento_descarga");
    	sql.addCustomCriteria("id_posto_avancado IS NULL");
    	sql.addCustomCriteria("tp_operacao = 'D'");
    	
    	return sql.getSql();
    }
    
    /*
     * ###########################################
     * Funcoes para o relatorio...
     * ###########################################
     */
    
    /**
     * Método para calcular a diferença em dias entre dois DateTime.
     * 
     * @param smallerDate
     * @param biggerDate
     * @return Uma string formatada hh:mmm
     */
    public static String getDiferencaTempo(String dataInicial, String dataFinal) {

    	String retorno = "";
    	
    	if ((dataInicial!=null) && (dataFinal!=null)) {
    		DateTime dtInicial = JTFormatUtils.stringToDateTime(dataInicial);
    		DateTime dtFinal = JTFormatUtils.stringToDateTime(dataFinal);
    		
    		Duration duration = new Duration(dtFinal.getMillis(), dtInicial.getMillis());
    		long seconds = duration.getMillis()/1000;
			// pois neste caso, são apenas horas positivas
			if (seconds > 0) {
				retorno = JTFormatUtils.formatTime(seconds, JTFormatUtils.MINUTES, JTFormatUtils.MINUTES);	
			}
    	}	
    	return retorno;
    }
    
    /**
     * Busca a data de chegada estimada. Caso uma das datas não for informada
     * retorna uma String vazia.
     * 
     * @param dhSaida
     * @param nrTempoViagem
     * @return
     */
    public static String getChegadaEstimada(String dhSaida, Long nrTempoViagem) {
    	if (dhSaida!=null) {
    		
    		DateTime dhChegadaEstimada = JTFormatUtils.stringToDateTime(dhSaida);    	
    		int milis = Integer.parseInt(String.valueOf(nrTempoViagem * 60 * 1000));
    		
    		return JTFormatUtils.format(dhChegadaEstimada.plusMillis(milis));
    	} 
    	return "";
    }
    
    /**
     * Retorna uma hora apartir de determinado minuto.
     * 
     * @param min minutos a serem convertidos
     * @return
     */
    public static String getMinutsToHour(Long min) {
    	
    	if (min!=null) {
    		BigDecimal minute = new BigDecimal(String.valueOf(min));
    		long seconds = Long.valueOf(String.valueOf(minute.multiply(new BigDecimal(60)))).longValue();
    		
    		return JTFormatUtils.formatTime(seconds, JTFormatUtils.HOURS, JTFormatUtils.MINUTES);
    	}
    	return "";
    }
}
