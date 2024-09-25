package com.mercurio.lms.carregamento.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de Geração e Manutenção de Pré-Manifesto
 * Especificação técnica 05.01.01.07
 * @author Cesar Gabbardo
 * 
 * @spring.bean id="lms.carregamento.manterGerarPreManifestoService"
 * @spring.property name="reportName" value="com/mercurio/lms/carregamento/report/manterGerarPreManifesto.jasper"
 */
public class ManterGerarPreManifestoService extends ReportServiceSupport {
	private ConversaoMoedaService conversaoMoedaService;
	private String tpManifesto;

    /**
     * Método responsável por gerar o relatório. 
     */
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;
		
		this.setTpManifesto((String) parameters.get("tpManifesto"));
		
        SqlTemplate sql = mountSql(tfm);
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
              
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("siglaSimbolo", SessionUtils.getMoedaSessao().getSiglaSimbolo());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        
        jr.setParameters(parametersReport);
        return jr;
	}
	
	/**
	 * Método que monta o select para a consulta.
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
    private SqlTemplate mountSql(TypedFlatMap parameters) throws Exception {    	
    	SqlTemplate sql = createSqlTemplate();

    	/** SELECT */
    	sql.addProjection("MANIFESTO.ID_MANIFESTO", "ID_MANIFESTO");
    	sql.addProjection("F_ORIGEM_MANIF.SG_FILIAL", "SG_FILIAL_MANIF");
    	sql.addProjection("MANIFESTO.NR_PRE_MANIFESTO", "NR_PRE_MANIFESTO");
    	sql.addProjection("MANIFESTO.DH_GERACAO_PRE_MANIFESTO", "DH_GERACAO_PRE_MANIFESTO");
    	sql.addProjection("F_ORIGEM_CC.SG_FILIAL", "SG_FILIAL_CC");
    	sql.addProjection("CONTROLE_CARGA.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");    	
    	sql.addProjection("MANIFESTO.TP_MANIFESTO", "TP_MANIFESTO");
    	sql.addProjection("MANIFESTO.TP_STATUS_MANIFESTO", "TP_STATUS_MANIFESTO");

    	if(parameters.getString("tpManifesto").equals("V")) {
    		sql.addProjection("ROTA.DS_ROTA", "DS_ROTA");
    		sql.addProjection("MANIFESTO.TP_MANIFESTO_VIAGEM", "TP_MANIF_VIAG_ENTR");
    	}   
    	if(parameters.getString("tpManifesto").equals("E")) {
    		sql.addProjection("ROTA_COLETA_ENTREGA.DS_ROTA", "DS_ROTA");
    		sql.addProjection("MANIFESTO.TP_MANIFESTO_ENTREGA", "TP_MANIF_VIAG_ENTR");
    	}
   
    	sql.addProjection("P_F_DESTINO_MANIF.NM_FANTASIA", "NM_FILIAL_MANIF");
    	sql.addProjection("MANIFESTO.OB_MANIFESTO", "OB_MANIFESTO");
    	    	
    	/** FROMS */
    	sql.addFrom("MANIFESTO", "MANIFESTO");
    	sql.addFrom("CONTROLE_CARGA", "CONTROLE_CARGA");
    	sql.addFrom("FILIAL", "F_ORIGEM_MANIF");
    	sql.addFrom("FILIAL", "F_DESTINO_MANIF");
    	sql.addFrom("PESSOA", "P_F_DESTINO_MANIF");
    	sql.addFrom("FILIAL", "F_ORIGEM_CC");
        
    	if(parameters.getString("tpManifesto").equals("V")) {
    		sql.addFrom("ROTA", "ROTA");
    	}        
    	if(parameters.getString("tpManifesto").equals("E")) {
    		sql.addFrom("ROTA_COLETA_ENTREGA", "ROTA_COLETA_ENTREGA");
    	}
           	
    	/** JOINS */    	
    	sql.addJoin("MANIFESTO.ID_FILIAL_ORIGEM", "F_ORIGEM_MANIF.ID_FILIAL");
    	sql.addJoin("MANIFESTO.ID_CONTROLE_CARGA", "CONTROLE_CARGA.ID_CONTROLE_CARGA");
    	sql.addJoin("CONTROLE_CARGA.ID_FILIAL_ORIGEM", "F_ORIGEM_CC.ID_FILIAL");
        
    	if(parameters.getString("tpManifesto").equals("V")) {
    		sql.addJoin("CONTROLE_CARGA.ID_ROTA", "ROTA.ID_ROTA");
    	}
    	if(parameters.getString("tpManifesto").equals("E")) {
    		sql.addJoin("CONTROLE_CARGA.ID_ROTA_COLETA_ENTREGA", "ROTA_COLETA_ENTREGA.ID_ROTA_COLETA_ENTREGA");
    	}
        
    	sql.addJoin("MANIFESTO.ID_FILIAL_DESTINO", "F_DESTINO_MANIF.ID_FILIAL");    	
    	sql.addJoin("F_DESTINO_MANIF.ID_FILIAL", "P_F_DESTINO_MANIF.ID_PESSOA");
    	
    	/** CRITERIA */
    	sql.addCriteria("MANIFESTO.ID_MANIFESTO", "=", parameters.getLong("idManifesto"));
    	
        return sql;
    }	
    
    /**
     * Método que gera o subrelatorio do Manter Gerar Pré-Manifesto - Documentos. 
     * @return
     * @throws Exception
     */
    public JRDataSource executeManterGerarPreManifestoDocumentos(Long idManifesto) throws Exception {
        SqlTemplate sql = createSqlTemplate();

        /** SELECT */
    	sql.addProjection("SERVICO.SG_SERVICO", "SG_SERVICO");
    	sql.addProjection("DOCTO_SERVICO.TP_DOCUMENTO_SERVICO", "TP_DOCUMENTO_SERVICO");
    	sql.addProjection("F_ORIGEM_DOC_SER.SG_FILIAL", "SG_FILIAL_DOC_SER");
    	sql.addProjection("DOCTO_SERVICO.NR_DOCTO_SERVICO", "NR_DOCTO_SERVICO");
    	sql.addProjection("F_DESTINO_DOC_SER.SG_FILIAL", "SG_FILIAL_DESTINO_DOC_SER");
    	sql.addProjection("DOCTO_SERVICO.DH_EMISSAO", "DH_EMISSAO");
    	sql.addProjection("REMETENTE_DOC_SER.NM_PESSOA", "NM_REMETENTE");
    	sql.addProjection("DESTINATARIO_DOC_SER.NM_PESSOA", "NM_DESTINATARIO");
    	sql.addProjection("DOCTO_SERVICO.QT_VOLUMES", "QT_VOLUMES");
    	sql.addProjection("DOCTO_SERVICO.PS_REAL", "PS_REAL");    	
    	sql.addProjection("DOCTO_SERVICO.PS_AFORADO", "PS_AFORADO");
    	sql.addProjection("MOEDA.ID_MOEDA", "ID_MOEDA");
    	sql.addProjection("MOEDA.SG_MOEDA", "SG_MOEDA");
    	sql.addProjection("MOEDA.DS_SIMBOLO", "DS_SIMBOLO");     	
    	sql.addProjection("DOCTO_SERVICO.VL_MERCADORIA", "VL_MERCADORIA");
    	sql.addProjection("DOCTO_SERVICO.VL_TOTAL_DOC_SERVICO", "VL_TOTAL_DOC_SER");
    	sql.addProjection("DOCTO_SERVICO.DT_PREV_ENTREGA", "DT_PREV_ENTREGA");
    	    	
    	/** FROMS */
    	sql.addFrom("PRE_MANIFESTO_DOCUMENTO", "PRE_MANIFESTO_DOCUMENTO");
    	sql.addFrom("MANIFESTO", "MANIFESTO");
    	sql.addFrom("SERVICO", "SERVICO");
    	sql.addFrom("DOCTO_SERVICO", "DOCTO_SERVICO");
    	sql.addFrom("MOEDA", "MOEDA");
    	sql.addFrom("FILIAL", "F_ORIGEM_DOC_SER");
    	sql.addFrom("FILIAL", "F_DESTINO_DOC_SER");    	
    	sql.addFrom("PESSOA", "REMETENTE_DOC_SER");
    	sql.addFrom("PESSOA", "DESTINATARIO_DOC_SER");
    	    	
    	/** JOINS */
    	sql.addJoin("MANIFESTO.ID_MANIFESTO", "PRE_MANIFESTO_DOCUMENTO.ID_MANIFESTO");
    	sql.addJoin("DOCTO_SERVICO.ID_DOCTO_SERVICO", "PRE_MANIFESTO_DOCUMENTO.ID_DOCTO_SERVICO");
     	sql.addJoin("DOCTO_SERVICO.ID_SERVICO", "SERVICO.ID_SERVICO");
    	sql.addJoin("DOCTO_SERVICO.ID_MOEDA", "MOEDA.ID_MOEDA");
    	sql.addJoin("DOCTO_SERVICO.ID_FILIAL_ORIGEM", "F_ORIGEM_DOC_SER.ID_FILIAL");
    	sql.addJoin("DOCTO_SERVICO.ID_FILIAL_DESTINO", "F_DESTINO_DOC_SER.ID_FILIAL");    	
    	sql.addJoin("DOCTO_SERVICO.ID_CLIENTE_REMETENTE", "REMETENTE_DOC_SER.ID_PESSOA");
    	sql.addJoin("DOCTO_SERVICO.ID_CLIENTE_DESTINATARIO", "DESTINATARIO_DOC_SER.ID_PESSOA");
    	
    	/** CRITERIA */
    	sql.addCriteria("MANIFESTO.ID_MANIFESTO", "=", idManifesto);

        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Método que converte os valores do documento de serviço.
     * @param vlMercadoria
     * @param idMoeda
     * @return
     */
    public BigDecimal executeConverteValor(BigDecimal valor, Long idMoeda) {    	
    	BigDecimal valorConvertido = this.getConversaoMoedaService()
									.findConversaoMoeda( SessionUtils.getPaisSessao().getIdPais(),
														 idMoeda,
													   	 SessionUtils.getPaisSessao().getIdPais(),
													   	 SessionUtils.getMoedaSessao().getIdMoeda(),
													   	 JTDateTimeUtils.getDataAtual(),
													   	 valor );
    	return valorConvertido;
    }    
    
    /**
     * Configura Dominios
     */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_MANIFESTO", "DM_TIPO_MANIFESTO");
		config.configDomainField("TP_STATUS_MANIFESTO", "DM_STATUS_MANIFESTO");
		
		if (this.getTpManifesto().equals("V")) {
			config.configDomainField("TP_MANIF_VIAG_ENTR", "DM_TIPO_MANIFESTO_VIAGEM");
		} else if (this.getTpManifesto().equals("E")) {
			config.configDomainField("TP_MANIF_VIAG_ENTR", "DM_TIPO_MANIFESTO_ENTREGA");
		}
		
		config.configDomainField("TP_MODAL", "DM_MODAL");		
		config.configDomainField("TP_ABRANGENCIA", "DM_ABRANGENCIA");
		config.configDomainField("TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
	}


	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public String getTpManifesto() {
		return tpManifesto;
	}
	public void setTpManifesto(String tpManifesto) {
		this.tpManifesto = tpManifesto;
	}          

}