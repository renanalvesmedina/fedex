package com.mercurio.lms.rnc.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapArrayDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração do Relatório de Não Conformidade.
 * Especificação técnica 12.02.01.01
 * @author Rodrigo Antunes
 * 
 * @spring.bean id="lms.rnc.relatorioNaoConformidadeService"
 * @spring.property name="reportName" value="com/mercurio/lms/rnc/report/emitirRelatoriosRNC.jasper"
 */

public class RelatorioNaoConformidadeService extends ReportServiceSupport {
	
	 private NaoConformidadeService naoConformidadeService;
	
	private static final String DATA_INICIAL = "dataInicial";
	private static final String DATA_FINAL = "dataFinal";
	private static final int PERIODO_DIAS = 31;
	

    /**
     * Método responsável pela geração do relatório 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
        TypedFlatMap tfm = (TypedFlatMap)parameters; 
        validateForm(tfm);
        SqlTemplate filterSummary = mountFilterSummary(tfm);
        List list = naoConformidadeService.executeRelatorioNaoConformidade(tfm);
        
        JRReportDataObject jr = this.createReportDataObject(new JRMapArrayDataSource(list.toArray()), parameters);
        
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa",filterSummary.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
        String dsSimboloMoeda = tfm.getString("dsSimboloMoedaHidden");
        parametersReport.put("moedaSelecionada",dsSimboloMoeda);
        
        jr.setParameters(parametersReport);
        return jr; 
    }
    
    /**
     * Verifica se a filial emitente ou a filial responsavel foi informada.
     * Valida se o usuario tem acesso a essas filiais.
     */
    private void validateForm(TypedFlatMap tfm) {
        
    	Long idFilialEmitente = tfm.getLong("filialEmitente.idFilial"); 
    	Long idFilialResponsavel = tfm.getLong("filialResponsavel.idFilial");
    	
    	if (idFilialEmitente==null && idFilialResponsavel==null) {
    		throw new BusinessException("LMS-12015");
    	}
    	
        YearMonthDay dataInicial = tfm.getYearMonthDay(DATA_INICIAL);
        YearMonthDay dataFinal = tfm.getYearMonthDay(DATA_FINAL);
        if (dataInicial==null || dataFinal==null || (JTDateTimeUtils.getIntervalInDays( dataInicial, dataFinal) > PERIODO_DIAS)) {
    		throw new BusinessException("LMS-10045");
    	}
       
    }    
    
    /**
     * Monta o sql do relatório
     * @param parameters
     * @return
     * @throws Exception
     */     
    private SqlTemplate mountFilterSummary(TypedFlatMap tfm){
        SqlTemplate sqlExterno = createSqlTemplate();

        String statusNaoConformidade = tfm.getString("statusNaoConformidade");
        if(StringUtils.isNotEmpty(statusNaoConformidade)) {
            sqlExterno.addFilterSummary("statusNaoConformidade", tfm.getString("statusNaoConformidadeHidden"));
        }

        Long idFilial = tfm.getLong("filialEmitente.idFilial");
        if (idFilial!=null) {
            sqlExterno.addFilterSummary("filialEmitente", tfm.getString("filialEmitente.sgFilial"));
        }

        idFilial = tfm.getLong("filialResponsavel.idFilial");
        if(idFilial!=null) {
            sqlExterno.addFilterSummary("filialResponsavel", tfm.getString("filialResponsavel.sgFilial"));
        }
        
        Long idFilialDestino = tfm.getLong("filialDestino.idFilial");
        if(idFilialDestino != null) {
            sqlExterno.addFilterSummary("filialDeDestino", tfm.getString("filialDestino.sgFilial"));
        }
        
        YearMonthDay dataInicial = tfm.getYearMonthDay("dataInicial");
        if(dataInicial!=null) {
            sqlExterno.addFilterSummary("periodoInicial", JTFormatUtils.format(dataInicial));
        }
        
        YearMonthDay dataFinal = tfm.getYearMonthDay("dataFinal");
        if (dataFinal!=null){
            sqlExterno.addFilterSummary("periodoFinal", JTFormatUtils.format(dataFinal));
        }
        
        String tipoDocumento = tfm.getString("tipoDocumento");
        if(StringUtils.isNotEmpty(tipoDocumento)) {
            sqlExterno.addFilterSummary("tipoDocumento", tfm.getString("tipoDocumentoHidden"));
        }
        
        String motivoNaoConformidade = tfm.getString("motivoNaoConformidade");
        if(StringUtils.isNotEmpty(motivoNaoConformidade)) {
            sqlExterno.addFilterSummary("motivoNaoConformidade", tfm.getString("motivoNaoConformidadeHidden"));
        }

        String causaNaoConformidade = tfm.getString("causaNaoConformidade");
        if(StringUtils.isNotEmpty(causaNaoConformidade)) {
            sqlExterno.addFilterSummary("causaNaoConformidade", tfm.getString("causaNaoConformidadeHidden"));
        }
        
        String statusOcorrencia = tfm.getString("statusOcorrencia");
        if(StringUtils.isNotEmpty(statusOcorrencia)) {
            sqlExterno.addFilterSummary("status",tfm.getString("statusOcorrenciaHidden"));
        }
        
        Long idRemetente= tfm.getLong("remetente.idCliente");
        if(idRemetente!=null) {
            sqlExterno.addFilterSummary("remetente", tfm.getString("remetente.pessoa.nmPessoa"));
        }        
        
        Long idDestinatario = tfm.getLong("destinatario.idCliente");
        if(idDestinatario!=null) {
            sqlExterno.addFilterSummary("destinatario", tfm.getString("destinatario.pessoa.nmPessoa"));
        }
        
        String diaInicial = tfm.getString("diaInicial");
        if(StringUtils.isNotEmpty(diaInicial)) {
            sqlExterno.addFilterSummary("diaPendenteInicial",tfm.getString("diaInicial"));
        }

        String diaFinal = tfm.getString("diaFinal");
        if(StringUtils.isNotEmpty(diaFinal)) {
            sqlExterno.addFilterSummary("diaPendenteFinal",diaFinal);
        }
        
        return sqlExterno;        
    }
    
    public List<Map<String, Object>> findDadosRalatorioRNCByItem(TypedFlatMap criteria) {
    	List<Map<String, Object>> lista = getJdbcTemplate().queryForList(getQueryRelatorioRNCPorItem(criteria));
		return lista;
	} 
    
    public String getQueryRelatorioRNCPorItem(TypedFlatMap criteria){
	  	
		 StringBuilder sql = new StringBuilder();
		 sql.append(" SELECT * FROM ( ")
			 .append("SELECT ")
			 .append(" FNC.SG_FILIAL           											       						AS filialNaoConformidade, ")
			 .append(" NC.NR_NAO_CONFORMIDADE																		AS nrNC, ")
			 .append(" DS.TP_DOCUMENTO_SERVICO																		AS tpDocumento, ")
			 .append(" FO.SG_FILIAL      																			AS filialOrigem, ")
			 .append(" DS.NR_DOCTO_SERVICO 																			AS nrDoctoServico,")
			 .append(" NC.TP_STATUS_NAO_CONFORMIDADE																AS statusNC, ")
			 .append(PropertyVarcharI18nProjection.createProjection(" MANC.DS_MOTIVO_ABERTURA_I")).append("			AS motivoAbertura, ")
			 .append(PropertyVarcharI18nProjection.createProjection(" MD.DS_MOTIVO_I  ")).append("					AS motivoDisposicao, ")
			 .append(" FE.SG_FILIAL 																				AS filialEmitente, ")
			 .append(" FR.SG_FILIAL 																				AS filialResponsavel, ") 
			 .append(" FD.SG_FILIAL 																				AS filialDestino, ") 
			 .append(" TO_CHAR(ONC.DH_INCLUSAO,'DD/MM/YYYY HH24:MI')												AS dhNC, ")
			 .append(" ONC.QT_VOLUMES 																				AS qtdVolumes, ")
			 .append(PropertyVarcharI18nProjection.createProjection(" VD.DS_VALOR_DOMINIO_I ")).append(" 			AS statusOcorrenciaNC, ")
			 .append(" MCCT.NR_CHAVE 																				AS nrChave, ")
			 .append(" PR.NM_PESSOA															    					AS nmRemetente, ")
			 .append(" PD.NM_PESSOA															   		 				AS nmDestinatario, ")
			 .append(" (SELECT AWB.DS_SERIE || '/' || AWB.NR_AWB || '-' || AWB.DV_AWB  FROM CTO_AWB, AWB WHERE DS.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO(+) AND CTO_AWB.ID_AWB = AWB.ID_AWB(+)AND rownum = 1) AS nrAwb, ")
			 .append(" PA.NM_PESSOA															    					AS ciaAerea, ")
			 .append(PropertyVarcharI18nProjection.createProjection(" CNC.DS_CAUSA_NAO_CONFORMIDADE_I ")).append(" 	AS causaNCI, ")
			 .append(" ONC.DS_CAUSA_NC              										    					AS causaNC, ")
			 .append(PropertyVarcharI18nProjection.createProjection(" DPNC.DS_PADRAO_NC_I ")).append("  			AS descricaoNCI, ")
			 .append(" ONC.DS_OCORRENCIA_NC 																		AS descricaoOcorrenciaNC, ")
			 .append(" IONC.NR_ITEM         																		AS itemOcorrenciaNC, ")
			 .append(" IONC.QT_NAO_CONFORMIDADE 																	AS qtdOcorrenciaNC, ")
			 .append(" IONC.VL_NAO_CONFORMIDADE  																	AS vlOcorrenciaNC, ")
			 .append(" CASE WHEN TP_STATUS_OCORRENCIA_NC = 'A' THEN  extract(day from (systimestamp - ONC.dh_inclusao)) WHEN TP_STATUS_OCORRENCIA_NC = 'F' THEN  0 END AS DIAS_PENDENTES ")
			 
			 .append(" FROM NAO_CONFORMIDADE NC ")
			 .append(" INNER JOIN OCORRENCIA_NAO_CONFORMIDADE ONC on NC.ID_NAO_CONFORMIDADE = ONC.ID_NAO_CONFORMIDADE ")
			 .append(" LEFT JOIN DOCTO_SERVICO DS ON NC.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ")
			 .append(" LEFT JOIN FILIAL FO on DS.ID_FILIAL_ORIGEM = FO.ID_FILIAL ")
			 .append(" INNER JOIN FILIAL FNC on NC.ID_FILIAL = FNC.ID_FILIAL ")
			 .append(" INNER JOIN FILIAL FE on ONC.ID_FILIAL_ABERTURA = FE.ID_FILIAL ")
			 .append(" INNER JOIN FILIAL FR on ONC.ID_FILIAL_RESPONSAVEL = FR.ID_FILIAL ")
			 .append(" LEFT JOIN MOTIVO_ABERTURA_NC MANC on ONC.ID_MOTIVO_ABERTURA_NC = MANC.ID_MOTIVO_ABERTURA_NC ")
			 .append(" LEFT JOIN DISPOSICAO D on ONC.ID_OCORRENCIA_NAO_CONFORMIDADE = D.ID_OCORRENCIA_NAO_CONFORMIDADE ")
			 .append(" LEFT JOIN MOTIVO_DISPOSICAO MD on D.ID_MOTIVO_DISPOSICAO = MD.ID_MOTIVO_DISPOSICAO ")
			 
			 
			 .append(" INNER JOIN FILIAL FD on DS.ID_FILIAL_DESTINO = FD.ID_FILIAL ")
			 
			 .append(" JOIN VALOR_DOMINIO VD ON ONC.TP_STATUS_OCORRENCIA_NC  = VD.VL_VALOR_DOMINIO ")
			 .append(" INNER JOIN DOMINIO D ON D.ID_DOMINIO  = VD.ID_DOMINIO ")
			 .append(" AND D.NM_DOMINIO = 'DM_STATUS_OCORRENCIA_NC' ")
			
			 .append(" LEFT JOIN MONITORAMENTO_CCT MCCT on NC.ID_DOCTO_SERVICO = MCCT.ID_DOCTO_SERVICO ")
			 .append(" LEFT JOIN PESSOA PR on MCCT.ID_CLIENTE_REM = PR.ID_PESSOA ")
			 .append(" LEFT JOIN PESSOA PD on MCCT.ID_CLIENTE_DEST = PD.ID_PESSOA ")
			 .append(" LEFT JOIN CTO_AWB CTA on NC.ID_DOCTO_SERVICO = CTA.ID_CONHECIMENTO ") 
			 .append(" LEFT JOIN AWB ON CTA.ID_AWB = AWB.ID_AWB ")
			 .append(" LEFT JOIN CIA_FILIAL_MERCURIO CFM on AWB.ID_CIA_FILIAL_MERCURIO = CFM.ID_CIA_FILIAL_MERCURIO ")
			 .append(" LEFT JOIN PESSOA PA on CFM.ID_EMPRESA = PA.ID_PESSOA ")
			 .append(" LEFT JOIN CAUSA_NAO_CONFORMIDADE CNC on ONC.ID_CAUSA_NAO_CONFORMIDADE = CNC.ID_CAUSA_NAO_CONFORMIDADE ")
			 .append(" LEFT JOIN DESCRICAO_PADRAO_NC DPNC on ONC.ID_MOTIVO_ABERTURA_NC = DPNC.ID_MOTIVO_ABERTURA_NC ")
			 .append(" LEFT JOIN ITEM_OCORRENCIA_NC IONC on ONC.ID_OCORRENCIA_NAO_CONFORMIDADE = IONC.ID_OCORRENCIA_NAO_CONFORMIDADE ")
			 .append(" WHERE 1 = 1 ")
		 	 .append(" AND MCCT.nr_chave = IONC.nr_chave ");
		 
			String statusNaoConformidade = criteria.getString("statusNaoConformidade");
	        if(StringUtils.isNotEmpty(statusNaoConformidade)) {
	        	sql.append(" AND NC.TP_STATUS_NAO_CONFORMIDADE = '").append(statusNaoConformidade).append("' ");
	        }
	
	        Long idFilial = criteria.getLong("filialEmitente.idFilial");
	        if (idFilial!=null) {
	            sql.append(" AND ONC.ID_FILIAL_ABERTURA = ").append(idFilial);
	        }
	
		    Long idFilialResp = criteria.getLong("filialResponsavel.idFilial");
		    if(idFilialResp!=null) {
		    	sql.append(" AND ONC.ID_FILIAL_RESPONSAVEL = ").append(idFilialResp);
		    }
		        
		    Long idFilialDestino = criteria.getLong("filialDestino.idFilial");
		    if(idFilialDestino != null) {
		        sql.append(" AND DS.ID_FILIAL_DESTINO = ").append(idFilialDestino);
		    }
		        
		    YearMonthDay dataInicial = criteria.getYearMonthDay("dataInicial");
		    if(dataInicial!=null) {
		        sql.append(" AND TRUNC(CAST(ONC.DH_INCLUSAO AS DATE)) >= to_date('").append(JTFormatUtils.format(dataInicial)).append("', 'dd/mm/yyyy')");
		    }
		        
		    YearMonthDay dataFinal = criteria.getYearMonthDay("dataFinal");
		    if (dataFinal!=null){
		    	sql.append(" AND TRUNC(CAST(ONC.DH_INCLUSAO AS DATE)) <= to_date('").append(JTFormatUtils.format(dataFinal)).append("', 'dd/mm/yyyy')");
		    }
		        
		    String tipoDocumento = criteria.getString("tipoDocumento");
		    if(StringUtils.isNotEmpty(tipoDocumento)) {
		    	sql.append(" AND DS.TP_DOCUMENTO_SERVICO = '").append(tipoDocumento).append("' ");
		    }
		        
		    String motivoNaoConformidade = criteria.getString("motivoNaoConformidade");
		    if(StringUtils.isNotEmpty(motivoNaoConformidade)) {
		    	sql.append(" AND MANC.ID_MOTIVO_ABERTURA_NC = ").append(motivoNaoConformidade);
		    }
	
	        String causaNaoConformidade = criteria.getString("causaNaoConformidade");
	        if(StringUtils.isNotEmpty(causaNaoConformidade)) {
	        	sql.append(" AND ONC.ID_CAUSA_NAO_CONFORMIDADE = ").append(causaNaoConformidade);
	        }
		        
	        String statusOcorrencia = criteria.getString("statusOcorrencia");
	        if(StringUtils.isNotEmpty(statusOcorrencia)) {
	        	sql.append(" AND ONC.TP_STATUS_OCORRENCIA_NC = '").append(statusOcorrencia).append("' ");
	        }
		        
	        Long idRemetente= criteria.getLong("remetente.idCliente");
	        if(idRemetente!=null) {
	        	sql.append(" AND NC.ID_CLIENTE_REMETENTE = ").append(idRemetente);
	        }        
		        
	        Long idDestinatario = criteria.getLong("destinatario.idCliente");
	        if(idDestinatario!=null) {
	        	sql.append(" AND NC.ID_CLIENTE_DESTINATARIO = ").append(idDestinatario);
	        }
	    sql.append(" ) ") 
        .append(" WHERE 1 = 1 ");
	    
	    String diaInicial = criteria.getString("diaInicial");
        if(StringUtils.isNotEmpty(diaInicial)) {
            sql.append(" AND DIAS_PENDENTES >= " + diaInicial);
        }

        String diaFinal = criteria.getString("diaFinal");
        if(StringUtils.isNotEmpty(diaFinal)) {
        	sql.append(" AND DIAS_PENDENTES >= " + diaFinal);
        }
	    
        sql.append(" ORDER BY filialNaoConformidade, nrNC");
	    
		return sql.toString();
	 }

    /**
     * Método para setar os campos que são dominios. A partir disso,
     * o framework carregará as descrições desse dominio  
     */
    public void configReportDomains(ReportDomainConfig config) {
        config.configDomainField("STATUS_OCORRENCIA_NC", "DM_STATUS_OCORRENCIA_NC");
        config.configDomainField("DS_TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
    }

	public NaoConformidadeService getNaoConformidadeService() {
		return naoConformidadeService;
	}
	public void setNaoConformidadeService(NaoConformidadeService naoConformidadeService) {
		this.naoConformidadeService = naoConformidadeService;
	}
}