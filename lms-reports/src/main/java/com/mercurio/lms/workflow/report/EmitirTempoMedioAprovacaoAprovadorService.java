package com.mercurio.lms.workflow.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author JoseMR
 *
 * @spring.bean id="lms.workflow.emitirTempoMedioAprovacaoAprovadorService"
 * @spring.property name="reportName" value="com/mercurio/lms/workflow/report/emitirTempoMedioAprovacaoAprovador.jasper"
 */
public class EmitirTempoMedioAprovacaoAprovadorService extends ReportServiceSupport {
    
    /**
     * Executa a pesquisa para os dados do relatório de acordo com os critérios passados como parâmetro
     * @param parameters Critérios de pesquisa
     * @return JRReportDataObject Classe que encapsula os dados retornados da pesquisa utilizados no relatório
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	
    	TypedFlatMap tfm = (TypedFlatMap)parameters;

        SqlTemplate sqlPerfil = mountSqlPerfil(tfm);
        SqlTemplate sqlUsuario = mountSqlUsuario(tfm);
        
        Object[] array = new Object[sqlPerfil.getCriteria().length + sqlUsuario.getCriteria().length];
        
        System.arraycopy(sqlPerfil.getCriteria(),0,array, 0, sqlPerfil.getCriteria().length);
        System.arraycopy(sqlUsuario.getCriteria(),0,array, sqlPerfil.getCriteria().length, sqlUsuario.getCriteria().length);
        
        List resultado = (List) getJdbcTemplate().query(sqlPerfil.getSql() + "\nUNION\n" + sqlUsuario.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), array), new ResultSetExtractor(){
            
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                
                DateTime dtLiberacao = null;
                DateTime dtAcao      = null;
                
                List retorno = new ArrayList();
                
                Map map = null;
                
                while (rs.next()) {
                    
                    map = new HashMap();

                    map.put("TEMPO_MEDIO", null);
                    
                    try {
                        dtLiberacao = new DateTime( rs.getTimestamp("DH_LIBERACAO").getTime() );
                        dtAcao      = new DateTime( rs.getTimestamp("DH_ACAO").getTime() );

                        if( dtLiberacao != null && dtAcao != null ){
	                        long segundos = Math.abs((dtLiberacao.getMillis()/1000) - (dtAcao.getMillis()/1000));
	                        double minutos = calcHMS(segundos);
	                        
	                    	map.put("TEMPO_MEDIO", new Double(minutos));
                        }
                    } catch (Exception e) {
                    }
                    
                    map.put("ID_FILIAL",    Long.valueOf(rs.getString("ID_FILIAL")));
                    map.put("SG_NM_FILIAL", rs.getString("SG_NM_FILIAL"));
                    map.put("ID_USUARIO",   Long.valueOf(rs.getString("ID_USUARIO")));
                    map.put("NM_APROVADOR", rs.getString("NM_APROVADOR"));
                    map.put("DS_EVENTO",    rs.getString("DS_EVENTO"));
                    map.put("DH_LIBERACAO", rs.getObject("DH_LIBERACAO"));
                    map.put("DH_ACAO",      rs.getObject("DH_ACAO"));
                    
                    retorno.add(map);
                }
                
                return retorno;
            }
        });
        
        List resultadoAlterado = trataDadosCollection(resultado);

        String idFilial      = tfm.getString("filial.idFilial");
        String sgFilial      = tfm.getString("sgFilial");
        String nmFilial      = tfm.getString("nmFilial");
        String idUsuario     = tfm.getString("usuario.idUsuario");
        String modal         = tfm.getString("tpModal");
        String dsModal       = tfm.getString("dsModal");
        String abrangencia   = tfm.getString("tpAbrangencia");
        String dsAbrangencia = tfm.getString("dsAbrangencia");
        String nmUsuario     = tfm.getString("usuario.nmUsuario");
        YearMonthDay dtInicial   = tfm.getYearMonthDay("dtInicial");
        YearMonthDay dtFinal  	 = tfm.getYearMonthDay("dtFinal");
        
        SqlTemplate filtros = createSqlTemplate();
        
        if ( StringUtils.isNotBlank(idFilial) ) {         
            filtros.addFilterSummary("filial", sgFilial + " - " + nmFilial);
        }
        
        if ( StringUtils.isNotBlank(modal) ) {
            filtros.addFilterSummary("modal",dsModal);
        }
        
        if ( StringUtils.isNotBlank(abrangencia) ) {
            filtros.addFilterSummary("abrangencia", dsAbrangencia);
        }
        
        if ( StringUtils.isNotBlank(idUsuario) ) {               
            filtros.addFilterSummary("aprovador",nmUsuario);
        }
        
        if ( dtInicial != null ) {
            filtros.addFilterSummary("periodoInicial", JTFormatUtils.format(dtInicial, JTFormatUtils.YEARMONTHDAY));
        }
        
        if ( dtFinal != null) {
            filtros.addFilterSummary("periodoFinal", JTFormatUtils.format(dtFinal, JTFormatUtils.YEARMONTHDAY));
        }
        
        Map parametersReport = new HashMap();
        
        /** Seta o parâmetro de tipo de arquivo a ser gerado */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));

        parametersReport.put("parametrosPesquisa",filtros.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        
        JRMapCollectionDataSource mapCollection =  new JRMapCollectionDataSource(resultadoAlterado);
        
        return createReportDataObject(mapCollection,parametersReport);
    }
    
    /**
     * Separa e faz o controle do tempo médio para cada evento, aprovador, filial
     * @param resultado Lista retorno da pesquisa de acordo com os critérios 
     * @return Lista contendo o resumo e tempos médios separados por filial, aprovador e evento
     */
    private List trataDadosCollection(List resultado) {
        
        List retorno = new ArrayList();
        String dsEventoAnterior = null;
        String dsEvento = null;
        String aprovador = null;
        String aprovadorAnterior = null;
        BigDecimal tempoMedio = null;
        BigDecimal somatorio  = new BigDecimal(0);
        somatorio.setScale(2,BigDecimal.ROUND_DOWN);
        
        Map map = new HashMap();
        HashMap mapAnterior = null;
        
        int contador = 0;        
        
        if( resultado.size() == 1 ){
            map = (HashMap) resultado.iterator().next();
            somatorio = somatorio.divide(new BigDecimal(1), 2, BigDecimal.ROUND_DOWN);            
            map.put("TEMPO_MEDIO", new Double(somatorio.doubleValue()));
            retorno.add(map);
        } else {        
            for (Iterator iter = resultado.iterator(); iter.hasNext();) {
                
                HashMap element = (HashMap) iter.next();
                
                dsEvento   = (String) element.get("DS_EVENTO");
                
                if (element.get("TEMPO_MEDIO") != null) {
					tempoMedio = new BigDecimal(((Double) element.get("TEMPO_MEDIO")).doubleValue());
					tempoMedio.setScale(2, BigDecimal.ROUND_DOWN);
				}
                
                aprovador = (String) element.get("NM_APROVADOR");                
                
                if( (dsEventoAnterior != null && !dsEvento.equals(dsEventoAnterior)) || 
                    (aprovadorAnterior != null && !aprovador.equals(aprovadorAnterior)) ){
                    
                    map = mapAnterior;
                    
                    somatorio = somatorio.divide(new BigDecimal(contador), 2, BigDecimal.ROUND_DOWN);
                    
                    map.put("TEMPO_MEDIO", new Double(somatorio.doubleValue()));
                    retorno.add(map);
                    
                    somatorio  = new BigDecimal(0);
                    dsEventoAnterior = null;
                    contador = 0;
                    map = null; 
                    
                }
                
                contador++;
                mapAnterior = element; 
                dsEventoAnterior = dsEvento;
                aprovadorAnterior = aprovador;
                
                if (element.get("TEMPO_MEDIO") != null) {
                	somatorio = somatorio.add(tempoMedio);
                }
                
                if( !iter.hasNext() ){
                    map = element;
                    somatorio = somatorio.divide(new BigDecimal(contador), 2, BigDecimal.ROUND_DOWN);            
                    map.put("TEMPO_MEDIO", new Double(somatorio.doubleValue()));
                    retorno.add(map); 
                }
                
            }
        }
        
        return retorno;
    }

    /**
     * Monta a query de pesquisa para o relatório
     * @param parameters Critérios de pesquisa
     * @return Query montada
     * @throws Exception
     */
    private SqlTemplate mountSqlPerfil(TypedFlatMap map) throws Exception{
        
    	SqlTemplate sql = this.createSqlTemplate();
        
        String idFilial     = map.getString("filial.idFilial");
        Long idUsuario      = map.getLong("usuario.idUsuario");
        String modal        = map.getString("tpModal");
        String abrangencia  = map.getString("tpAbrangencia");

        
        //Query para Perfil
        sql.addProjection("F.ID_FILIAL","ID_FILIAL");
        sql.addProjection("F.SG_FILIAL","SG_FILIAL");
        sql.addProjection("F.SG_FILIAL || ' - ' || P.NM_FANTASIA","SG_NM_FILIAL");
        sql.addProjection("PER.ID_PERFIL","ID_USUARIO");
        sql.addProjection("PER.DS_PERFIL","NM_APROVADOR");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("TE.DS_TIPO_EVENTO_I"),"DS_EVENTO");
        sql.addProjection("A.DH_LIBERACAO","DH_LIBERACAO");
        sql.addProjection("A.DH_ACAO","DH_ACAO");
        sql.addProjection("A.ID_ACAO","ID_ACAO");
        
        sql.addFrom("ACAO","A");
        sql.addFrom("INTEGRANTE","I");
        sql.addFrom("PERFIL", "PER");
        sql.addFrom("PENDENCIA","PE");
        sql.addFrom("OCORRENCIA","O");
        sql.addFrom("EVENTO_WORKFLOW", "EW");
        sql.addFrom("TIPO_EVENTO", "TE");
        sql.addFrom("COMITE", "C");
        sql.addFrom("FILIAL", "F");
        sql.addFrom("PESSOA", "P");
        
        sql.addJoin("A.ID_INTEGRANTE"       ,"I.ID_INTEGRANTE");
        sql.addJoin("I.ID_COMITE"           ,"C.ID_COMITE");
        sql.addJoin("I.ID_PERFIL"           ,"PER.ID_PERFIL");
        sql.addJoin("A.ID_PENDENCIA"        ,"PE.ID_PENDENCIA");
        sql.addJoin("PE.ID_OCORRENCIA"      ,"O.ID_OCORRENCIA");
        sql.addJoin("EW.ID_EVENTO_WORKFLOW" ,"O.ID_EVENTO_WORKFLOW");
        sql.addJoin("C.ID_COMITE"           ,"EW.ID_COMITE");        
        sql.addJoin("EW.ID_EVENTO_WORKFLOW" ,"TE.ID_TIPO_EVENTO");
        sql.addJoin("O.ID_FILIAL"           ,"F.ID_FILIAL");        
        sql.addJoin("F.ID_FILIAL"           ,"P.ID_PESSOA");
        
        if( StringUtils.isNotBlank(idFilial) ){
            sql.addCriteria("F.ID_FILIAL","=",idFilial);
        }
        
        if( idUsuario != null ){
            sql.addCriteria("I.ID_USUARIO","=",idUsuario);
        }
        
        if( idUsuario != null ){
           sql.addCriteria("I.ID_PERFIL","=",idUsuario);
        }
        
        if( StringUtils.isNotBlank(modal) ){
            sql.addCriteria("C.TP_MODAL","=",modal);
        }
        
        if( StringUtils.isNotBlank(abrangencia) ){
            sql.addCriteria("C.TP_ABRANGENCIA","=",abrangencia);
        }
        
        if(StringUtils.isNotBlank(map.getString("dtInicial"))){
            sql.addCriteria("trunc( cast(A.DH_ACAO as Date))",">=",map.getYearMonthDay("dtInicial"));
        }
        
        if(StringUtils.isNotBlank(map.getString("dtFinal"))){
            sql.addCriteria("trunc( cast(A.DH_ACAO as Date))","<=",map.getYearMonthDay("dtFinal"));
        }
        
        return sql;
    }
    
    /**
     * Monta a query de usuário para a pesquisa do relatório
     * @param parameters Critérios de pesquisa
     * @return Query montada
     * @throws Exception
     */
    private SqlTemplate mountSqlUsuario(TypedFlatMap map) throws Exception{
        
    	SqlTemplate sql = this.createSqlTemplate();
        
        String idFilial     = map.getString("filial.idFilial");
        Long idUsuario    = map.getLong("usuario.idUsuario");
        String modal        = map.getString("tpModal");
        String abrangencia  = map.getString("tpAbrangencia");
        
        //Query para usuário
        sql.addProjection("F.ID_FILIAL","ID_FILIAL");
        sql.addProjection("F.SG_FILIAL","SG_FILIAL");
        sql.addProjection("F.SG_FILIAL || ' - ' || P.NM_FANTASIA","SG_NM_FILIAL");
        sql.addProjection("U.ID_USUARIO","ID_USUARIO");
        sql.addProjection("U.NM_USUARIO","NM_APROVADOR");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("TE.DS_TIPO_EVENTO_I"),"DS_EVENTO");              
        sql.addProjection("A.DH_LIBERACAO","DH_LIBERACAO");
        sql.addProjection("A.DH_ACAO","DH_ACAO");
        sql.addProjection("A.ID_ACAO","ID_ACAO");
        
        sql.addFrom("ACAO","A");
        sql.addFrom("INTEGRANTE","I");
        sql.addFrom("USUARIO", "U");
        sql.addFrom("PENDENCIA","PE");
        sql.addFrom("OCORRENCIA","O");
        sql.addFrom("EVENTO_WORKFLOW", "EW");
        sql.addFrom("TIPO_EVENTO", "TE");
        sql.addFrom("COMITE", "C");
        sql.addFrom("FILIAL", "F");
        sql.addFrom("PESSOA", "P");
        
        sql.addJoin("A.ID_INTEGRANTE"       ,"I.ID_INTEGRANTE");        
        sql.addJoin("I.ID_COMITE"           ,"C.ID_COMITE");
        sql.addJoin("I.ID_USUARIO"          ,"U.ID_USUARIO");
        sql.addJoin("A.ID_PENDENCIA"        ,"PE.ID_PENDENCIA");
        sql.addJoin("PE.ID_OCORRENCIA"      ,"O.ID_OCORRENCIA");
        sql.addJoin("EW.ID_EVENTO_WORKFLOW" ,"O.ID_EVENTO_WORKFLOW");
        sql.addJoin("C.ID_COMITE"           ,"EW.ID_COMITE");
        sql.addJoin("EW.ID_EVENTO_WORKFLOW" ,"TE.ID_TIPO_EVENTO");        
        sql.addJoin("O.ID_FILIAL"           ,"F.ID_FILIAL");        
        sql.addJoin("F.ID_FILIAL"           ,"P.ID_PESSOA");
        
        if( StringUtils.isNotBlank(idFilial) ){
            sql.addCriteria("F.ID_FILIAL","=",idFilial);        
        }
        
        if( idUsuario != null ){
           sql.addCriteria("I.ID_USUARIO","=",idUsuario);
        }
        
        if( StringUtils.isNotBlank(modal) ){
            sql.addCriteria("C.TP_MODAL","=",modal);
        }
        
        if( StringUtils.isNotBlank(abrangencia) ){
            sql.addCriteria("C.TP_ABRANGENCIA","=",abrangencia);
        }
        
        if(StringUtils.isNotBlank(map.getString("dtInicial"))){
            sql.addCriteria("trunc( cast(A.DH_ACAO as Date))",">=",map.getYearMonthDay("dtInicial"));
        }
        
        if(StringUtils.isNotBlank(map.getString("dtFinal"))){
            sql.addCriteria("trunc( cast(A.DH_ACAO as Date))","<=",map.getYearMonthDay("dtFinal"));
        }     
        
        sql.addOrderBy("SG_FILIAL");
        sql.addOrderBy("NM_APROVADOR");
        sql.addOrderBy("DS_EVENTO");
        
        return sql;
    }
    
    /**
     * Calcula o tempo decorrido entre duas datas. Recebe o tempo em milisegundos 
     * e retorna o tempo em minutos.
     * @param timeInSeconds Tempo decorrido entre duas datas (em milisegundos)
     * @return double representando o tempo decorrido entre as duas datas (em minutos)
     */
    private double calcHMS(double timeInSeconds) {
        
        double hours   = 0;
        double minutes = 0;
        double seconds = 0;        
        double hoursToMinutes = 0; 
        double segundos = 0.0;
        
        hours = timeInSeconds / 3600d;
        
        hoursToMinutes = hours * 60d;
        
        timeInSeconds = timeInSeconds - (hours * 3600d);

        minutes = timeInSeconds / 60d;
        
        timeInSeconds = timeInSeconds - (minutes * 60d);
        
        seconds = timeInSeconds;
        
        segundos = seconds / 60d;
        
        return minutes + hoursToMinutes + segundos;
    }
    
}
