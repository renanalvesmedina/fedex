package com.mercurio.lms.recepcaodescarga.report;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * @author luisfco 
 *
 * @spring.bean id="lms.recepcaodescarga.relatorioVeiculosChegadosService"
 * @spring.property name="reportName" value="com/mercurio/lms/recepcaodescarga/report/emitirRelatoriosVeiculosChegados.jasper"
 */
public class RelatorioVeiculosChegadosService extends ReportServiceSupport{
	
	private FilialService filialService;
	private RecursoMensagemService recursoMensagemService;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		JRReportDataObject jr = null;
        
		SqlTemplate temp = makeReportQuery(tfm); 
		
		String query = temp.getSql();
        Object[] criteria = temp.getCriteria();
        
        Map reportParameters = new HashMap();
        reportParameters.put("parametrosPesquisa",temp.getFilterSummary());
        reportParameters.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        reportParameters.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
        List linhas = (List) getJdbcTemplate().query(query, 
        											JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), criteria),
        											new ResultSetExtractor() {
        	
        	public Object extractData(ResultSet rs) throws java.sql.SQLException ,org.springframework.dao.DataAccessException {
        		List linhasRs = new LinkedList();
        		
        		while (rs.next()) {
        			Map dados = new HashMap();
        			
        			String strDhInicioDescarga = rs.getString("DH_INICIO_DESCARGA");
        			String strDhInicioDescargaFormatado = strDhInicioDescarga!=null?JTFormatUtils.format(strDhInicioDescarga, JTFormatUtils.SHORT):null;
        			String strDhFimDescarga = rs.getString("DH_FINAL_DESCARGA");
        			String strDhFimDescargaFormatado = strDhFimDescarga!=null?JTFormatUtils.format(strDhFimDescarga, JTFormatUtils.SHORT):null;
        			String strDhChegada = rs.getString("DH_CHEGADA");
        			String strDhChegadaFormatado = strDhChegada!=null?JTFormatUtils.format(strDhChegada, JTFormatUtils.SHORT):null;
        			String dhEmissaoManifesto = rs.getString("DT_EMISSAO_MANIFESTO");
        			String dhEmissaoManifestoFormatado = dhEmissaoManifesto!=null?JTFormatUtils.format(dhEmissaoManifesto, JTFormatUtils.SHORT):null;
        			String tpAbrangencia = rs.getString("TP_ABRANGENCIA");
        			String tpModal = rs.getString("TP_MODAL");
        			
        			dados.put("TP_ABRANGENCIA", tpAbrangencia == null ? "" : getDomainValueService().findDomainValueDescription("DM_ABRANGENCIA", tpAbrangencia));
        			dados.put("TP_ABRANGENCIA_VALOR", rs.getString("TP_ABRANGENCIA_VALOR"));
        			dados.put("TP_MODAL",  tpModal == null ? "" : getDomainValueService().findDomainValueDescription("DM_MODAL", tpModal));        			
        			dados.put("ID_FILIAL_DESTINO_MANIFESTO", Long.valueOf(rs.getLong("ID_FILIAL_DESTINO_MANIFESTO")));
        			dados.put("ID_CONTROLE_CARGA", Long.valueOf(rs.getLong("ID_CONTROLE_CARGA")));
        			dados.put("FILIAL_CONTROLE", rs.getString("FILIAL_CONTROLE"));
        			dados.put("NR_CONTROLE_CARGA", Long.valueOf(rs.getLong("NR_CONTROLE_CARGA")));
        			dados.put("FILIAL_MANIFESTO", rs.getString("FILIAL_MANIFESTO"));
        			dados.put("NR_MANIFESTO", Long.valueOf(rs.getLong("NR_MANIFESTO")));
        			dados.put("DT_EMISSAO_MANIFESTO", dhEmissaoManifestoFormatado);
        			dados.put("NR_FROTA", rs.getString("NR_FROTA"));
        			dados.put("NR_IDENTIFICADOR", rs.getString("NR_IDENTIFICADOR"));
        			dados.put("DH_CHEGADA", strDhChegadaFormatado);
        			dados.put("DH_INICIO_DESCARGA", strDhInicioDescargaFormatado);
        			dados.put("DH_FINAL_DESCARGA", strDhFimDescargaFormatado);
        			
        			// calculando os tempos de descarga e espera
        			if (rs.getString("DH_INICIO_DESCARGA") != null) {
        				// tempo de espera
        				if (strDhChegada != null) {
        					DateTime dhInicioDescarga = JTFormatUtils.stringToDateTime(strDhInicioDescarga);
        					DateTime dhChegada = JTFormatUtils.stringToDateTime(strDhChegada);

        					long diferencaSegundos = (dhInicioDescarga.getMillis() - dhChegada.getMillis()) / 1000;
        					String strTempoEspera = JTFormatUtils.formatTime(diferencaSegundos, JTFormatUtils.HOURS);
        					dados.put("TEMPO_ESPERA", strTempoEspera);
        				}
       				
        				// tempo de descarga
        				if (strDhFimDescarga != null) {
        					DateTime dhFimDescarga = JTFormatUtils.stringToDateTime(strDhFimDescarga);
        					DateTime dhInicioDescarga = JTFormatUtils.stringToDateTime(strDhInicioDescarga);

        					long diferencaSegundos = (dhFimDescarga.getMillis() - dhInicioDescarga.getMillis()) / 1000;
        					String strTempoDescarga = JTFormatUtils.formatTime(diferencaSegundos, JTFormatUtils.HOURS);
        					dados.put("TEMPO_DESCARGA", strTempoDescarga);
        				}
        			}
        			linhasRs.add(dados);
        		}
        		return linhasRs;
        	};
        });
        jr = createReportDataObject(new JRMapCollectionDataSource(linhas), reportParameters);
        return jr; 
	}
	
    /**
     * Subrelatorio de equipes 
     * @param obj
     * @return
     */
    public JRDataSource executeEquipes(Object idFilialDestinoManifesto, Object idControleCarga) {
        JRDataSource dataSource = null;
        
        if(idFilialDestinoManifesto == null || idControleCarga == null) {
            dataSource = new JREmptyDataSource();
        } else {
        	StringBuffer sql = new StringBuffer()
        	.append("SELECT DISTINCT ")
        	.append("E.DS_EQUIPE AS DS_EQUIPE, ")
        	.append("EO.DH_INICIO_OPERACAO AS DH_INICIO_OPERACAO, ")
        	.append("EO.DH_FIM_OPERACAO AS DH_FIM_OPERACAO ")
        	.append("FROM ")
        	.append("DESCARGA_MANIFESTO DM ")
        	.append("inner join MANIFESTO M on (DM.ID_MANIFESTO = M.ID_MANIFESTO) ")
        	.append("inner join EQUIPE_OPERACAO EO on (DM.ID_CARREGAMENTO_DESCARGA = EO.ID_CARREGAMENTO_DESCARGA) ")
        	.append("inner join EQUIPE E on (E.ID_EQUIPE = EO.ID_EQUIPE) ")
        	.append("WHERE ")  
        	.append("M.ID_CONTROLE_CARGA = ? ")
        	.append("AND M.ID_FILIAL_DESTINO = ? ")
        	.append("AND DM.DH_CANCELAMENTO_DESCARGA IS NULL ")
        	.append("ORDER BY DH_INICIO_OPERACAO, DH_FIM_OPERACAO ");

        	Object[] criterias = new Object[2];
    		criterias[0] = idControleCarga;
    		criterias[1] = idFilialDestinoManifesto;

            dataSource = executeQuery(sql.toString(), criterias).getDataSource(); 
        }
        return dataSource;
    }

    public void configReportDomains(ReportDomainConfig config) {
    }
    
    /**
     * 
     * @param tfm
     * @return
     */
    private SqlTemplate makeReportQuery(TypedFlatMap tfm) {
    	Long idFilial = tfm.getLong("filial.idFilial");
        DateTime dtConsultaInicial = tfm.getDateTime("dtConsultaInicial");
        DateTime dtConsultaFinal   = tfm.getDateTime("dtConsultaFinal");
        boolean aguardandoDescarga = tfm.getBoolean("aguardandoDescarga").booleanValue();
        boolean emDescarga = tfm.getBoolean("emDescarga").booleanValue();
        boolean descarregados = tfm.getBoolean("descarregados").booleanValue();        
    	
        SqlTemplate s = createSqlTemplate();
		s.addProjection("M.TP_ABRANGENCIA", "TP_ABRANGENCIA_VALOR");
		s.addProjection("M.TP_ABRANGENCIA", "TP_ABRANGENCIA");		
		s.addProjection("M.TP_MODAL", "TP_MODAL");
        s.addProjection("M.DH_EMISSAO_MANIFESTO", "DT_EMISSAO_MANIFESTO");

        s.addProjection("M.ID_FILIAL_DESTINO", "ID_FILIAL_DESTINO_MANIFESTO");
        s.addProjection("CC.ID_CONTROLE_CARGA", "ID_CONTROLE_CARGA");
        s.addProjection("FILIAL_CC.SG_FILIAL", "FILIAL_CONTROLE");
        s.addProjection("CC.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
        s.addProjection("FILIAL_MANIFESTO.SG_FILIAL", "FILIAL_MANIFESTO");
        s.addProjection("MT.NR_FROTA", "NR_FROTA");
        s.addProjection("MT.NR_IDENTIFICADOR", "NR_IDENTIFICADOR");
        
        String caseWhen = " CASE WHEN (M.TP_MANIFESTO = 'V' AND M.TP_ABRANGENCIA = 'N') THEN (SELECT MVN.NR_MANIFESTO_ORIGEM from MANIFESTO_VIAGEM_NACIONAL MVN WHERE M.ID_MANIFESTO = MVN.ID_MANIFESTO_VIAGEM_NACIONAL) "
	  	  +"WHEN (M.TP_MANIFESTO = 'V' AND M.TP_ABRANGENCIA = 'I') THEN (SELECT MVI.NR_MANIFESTO_INT FROM MANIFESTO_INTERNACIONAL MVI WHERE M.ID_MANIFESTO = MVI.ID_MANIFESTO_INTERNACIONAL) "
	  	  +"WHEN (M.TP_MANIFESTO = 'E') THEN (SELECT ME.NR_MANIFESTO_ENTREGA FROM MANIFESTO_ENTREGA ME WHERE M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA) "
	  	  +"END AS NR_MANIFESTO ";
        
        s.addProjection(caseWhen);
        
        String dhChegada = new StringBuilder()
    	.append("(SELECT MAX(ECC.DH_EVENTO)")
    	.append("   FROM EVENTO_CONTROLE_CARGA ECC")
    	.append("  WHERE ECC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA")
    	.append("    AND ECC.ID_FILIAL = M.ID_FILIAL_DESTINO")
    	.append("    AND ECC.TP_EVENTO_CONTROLE_CARGA = 'CP'")
    	.append("    AND ECC.DH_EVENTO BETWEEN ? and ? )").toString();
    	s.addCriteriaValue(dtConsultaInicial);
    	s.addCriteriaValue(dtConsultaFinal);
        s.addProjection(dhChegada, "DH_CHEGADA");

        String dhInicioDescarga = new StringBuilder()
    	.append("(SELECT MAX(ECC.DH_EVENTO)")
    	.append("   FROM EVENTO_CONTROLE_CARGA ECC")
    	.append("  WHERE ECC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA")
    	.append("    AND ECC.ID_FILIAL = M.ID_FILIAL_DESTINO")
    	.append("    AND ECC.TP_EVENTO_CONTROLE_CARGA = 'ID'")
    	.append("	 AND NOT EXISTS ")
    	.append("		(SELECT 1 FROM EVENTO_CONTROLE_CARGA ECC1")
    	.append("		WHERE ECC1.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA")
    	.append("		AND ECC1.ID_FILIAL = M.ID_FILIAL_DESTINO ")
    	.append("		AND ECC1.TP_EVENTO_CONTROLE_CARGA = 'CD'")
    	.append("		AND ECC1.DH_EVENTO > ECC.DH_EVENTO")
    	.append("		AND ECC1.DH_EVENTO BETWEEN ? and ?)" )
    	.append("    AND ECC.DH_EVENTO BETWEEN ? and ? )").toString();
    	s.addCriteriaValue(dtConsultaInicial);
    	s.addCriteriaValue(dtConsultaFinal);
        s.addCriteriaValue(dtConsultaInicial);
    	s.addCriteriaValue(dtConsultaFinal);
        s.addProjection(dhInicioDescarga, "DH_INICIO_DESCARGA");
        
        String dhFinalDescarga = new StringBuilder()
    	.append("(SELECT MAX(ECC.DH_EVENTO)")
    	.append("   FROM EVENTO_CONTROLE_CARGA ECC")
    	.append("  WHERE ECC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA")
    	.append("    AND ECC.ID_FILIAL = M.ID_FILIAL_DESTINO")
    	.append("    AND ECC.TP_EVENTO_CONTROLE_CARGA = 'FD'")
    	.append("    AND ECC.DH_EVENTO BETWEEN ? and ? )").toString();
    	s.addCriteriaValue(dtConsultaInicial);
    	s.addCriteriaValue(dtConsultaFinal);
	    s.addProjection(dhFinalDescarga, "DH_FINAL_DESCARGA");

        s.addFrom("MANIFESTO", "M");
        s.addFrom("FILIAL", "FILIAL_MANIFESTO");
        s.addFrom("CONTROLE_CARGA", "CC");
        s.addFrom("FILIAL", "FILIAL_CC");
        s.addFrom("MEIO_TRANSPORTE", "MT");

        s.addJoin("M.ID_FILIAL_ORIGEM", "FILIAL_MANIFESTO.ID_FILIAL");
        s.addJoin("M.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA");
        s.addJoin("CC.ID_FILIAL_ORIGEM", "FILIAL_CC.ID_FILIAL");
        s.addJoin("CC.ID_TRANSPORTADO", "MT.ID_MEIO_TRANSPORTE(+)");
        
        s.addCriteria("M.ID_FILIAL_DESTINO", "=", idFilial);
                
        StringBuilder existsQuery = new StringBuilder();
        existsQuery.append("exists (SELECT 1 FROM evento_controle_carga ecc WHERE dh_evento BETWEEN ? AND ? AND id_filial = ? ");
        existsQuery.append("and ecc.id_controle_carga = cc.id_controle_carga ");
        existsQuery.append("and ecc.tp_evento_controle_carga in ");
        s.addCriteriaValue(dtConsultaInicial);
		s.addCriteriaValue(dtConsultaFinal);
		s.addCriteriaValue(idFilial);
        
        int criterias = 0;
        if (aguardandoDescarga){
        	criterias = criterias + 2;
        	s.addCriteriaValue("CP");
        	s.addCriteriaValue("CD");
        }
        if (emDescarga){
        	criterias++;
        	s.addCriteriaValue("ID");
        }
		if (descarregados){
			criterias++;
			s.addCriteriaValue("FD");
		}
		String in = new String();
		for (int i = 0; i < criterias; i++) {
			in = in + "?";
			if (i<(criterias-1)){
				in = in + ",";
			}
		}
		in = "("+in+") ";
		existsQuery.append(in);
        
        existsQuery.append(") and exists ");
        existsQuery.append("(select 1 from manifesto manif "); 
        existsQuery.append("where ");
        existsQuery.append("manif.id_controle_carga = cc.id_controle_carga ");
        existsQuery.append("and manif.id_filial_destino = ?) ");

        s.addCustomCriteria(existsQuery.toString());
		s.addCriteriaValue(idFilial);

		s.addOrderBy("FILIAL_CONTROLE");
		s.addOrderBy("NR_CONTROLE_CARGA");
		s.addOrderBy("FILIAL_MANIFESTO");
		s.addOrderBy("NR_MANIFESTO");

		setFilterSummary(s, tfm, aguardandoDescarga, emDescarga, descarregados);

		return s;
    }

    /**
     * 
     * @param sql
     * @param tfm
     * @param aguardandoDescarga
     * @param emDescarga
     * @param descarregados
     */
    private void setFilterSummary(SqlTemplate sql, TypedFlatMap tfm, boolean aguardandoDescarga, boolean emDescarga, boolean descarregados) {
    	Filial filial = filialService.findById(tfm.getLong("filial.idFilial")); 
    	sql.addFilterSummary("filial", filial.getSgFilial() + " - " + filial.getPessoa().getNmFantasia());
    	sql.addFilterSummary("dataHoraInicial", JTFormatUtils.format(tfm.getDateTime("dtConsultaInicial"), JTFormatUtils.SHORT));
    	sql.addFilterSummary("dataHoraFinal",   JTFormatUtils.format(tfm.getDateTime("dtConsultaFinal"), JTFormatUtils.SHORT));
    	
    	String sim = recursoMensagemService.findByChave("sim", null);
    	String nao = recursoMensagemService.findByChave("nao", null);
    	
    	if (aguardandoDescarga)     		
    		sql.addFilterSummary("aguardandoDescarga",sim); 
    	else sql.addFilterSummary("aguardandoDescarga",nao);

    	if (emDescarga)     		
    		sql.addFilterSummary("emDescarga",sim); 
    	else sql.addFilterSummary("emDescarga",nao);

    	if (descarregados)     		
    		sql.addFilterSummary("descarregados",sim); 
    	else sql.addFilterSummary("descarregados",nao);
		
    	
    }

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setRecursoMensagemService(
			RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}

	
}