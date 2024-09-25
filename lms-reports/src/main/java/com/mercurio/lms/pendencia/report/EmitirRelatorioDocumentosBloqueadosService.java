package com.mercurio.lms.pendencia.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de Documentos Bloqueados
 * Especificação técnica 17.01.01.06
 * @author Cesar Gabbardo
 * 
 * @spring.bean id="lms.pendencia.emitirRelatorioDocumentosBloqueadosService"
 * @spring.property name="reportName" value="com/mercurio/lms/pendencia/report/emitirRelatorioDocumentosBloqueados.jasper"
 */
public class EmitirRelatorioDocumentosBloqueadosService extends ReportServiceSupport {
	private DomainValueService domainValueService;
	private ConversaoMoedaService conversaoMoedaService;
	private ConfiguracoesFacade configuracoesFacade;

    /**
     * Método responsável por gerar o relatório. 
     */
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap param = (TypedFlatMap)parameters;
		
        SqlTemplate sql = mountSql(param);
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap(); 
                
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("mostrarOcorrencias", param.getBoolean("mostrarOcorrencias"));
        parametersReport.put("parametrosPesquisa", getFilterSummary(param));
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
    	sql.setDistinct();
    	sql.addProjection("LOCALIZACAO_MERCADORIA.ID_LOCALIZACAO_MERCADORIA", "ID_LOCALIZACAO_MERCADORIA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("LOCALIZACAO_MERCADORIA.DS_LOCALIZACAO_MERCADORIA_I"), "DS_LOCALIZACAO_MERCADORIA");
		sql.addProjection("FILIAL_DESTINO.ID_FILIAL", "ID_FILIAL_DESTINO");
		sql.addProjection("FILIAL_DESTINO.SG_FILIAL", "SG_FILIAL_DESTINO");
		sql.addProjection("DOCTO_SERVICO.ID_DOCTO_SERVICO", "ID_DOCTO_SERVICO");		
    	sql.addProjection("DOCTO_SERVICO.TP_DOCUMENTO_SERVICO", "TP_DOCUMENTO_SERVICO");
    	sql.addProjection("FILIAL_ORIGEM.SG_FILIAL", "SG_FILIAL_ORIGEM");
    	sql.addProjection("DOCTO_SERVICO.NR_DOCTO_SERVICO", "NR_DOCTO_SERVICO");
    	sql.addProjection("DOCTO_SERVICO.DH_EMISSAO", "DH_EMISSAO");
    	sql.addProjection("CLIENTE_REMETENTE.NM_PESSOA", "CLIENTE_REMETENTE");
    	sql.addProjection("CLIENTE_DESTINATARIO.NM_PESSOA", "CLIENTE_DESTINATARIO");    	
    	sql.addProjection("DOCTO_SERVICO.QT_VOLUMES", "QT_VOLUMES");
    	sql.addProjection("DOCTO_SERVICO.PS_REAL", "PS_REAL");    	
    	sql.addProjection("MOEDA.ID_MOEDA", "ID_MOEDA");
    	sql.addProjection("MOEDA.SG_MOEDA", "SG_MOEDA");
        sql.addProjection("MOEDA.DS_SIMBOLO", "DS_SIMBOLO");
    	sql.addProjection("DOCTO_SERVICO.VL_MERCADORIA", "VL_MERCADORIA");
    	sql.addProjection("DOCTO_SERVICO.DT_PREV_ENTREGA", "DT_PREV_ENTREGA");
    	sql.addProjection("FLOOR(CURRENT_DATE - TRUNC(CAST(OCORRENCIA_DOCTO_SERVICO.DH_BLOQUEIO AS DATE)))", "DIAS_BLOQUEIO");

    	sql.addProjection(
                "CASE\n" +
                "\t\tWHEN DOCTO_SERVICO.TP_DOCUMENTO_SERVICO = 'CTR' THEN CONHECIMENTO.TP_FRETE \n" +
                "\t\tWHEN DOCTO_SERVICO.TP_DOCUMENTO_SERVICO <> 'CTR' THEN  NULL\n" +
                "\tEND as TP_FRETE");
        sql.addProjection(
                "CASE\n" +
                "\t\tWHEN DOCTO_SERVICO.TP_DOCUMENTO_SERVICO = 'CTR' THEN " + PropertyVarcharI18nProjection.createProjection("NATUREZA_PRODUTO.DS_NATUREZA_PRODUTO_I") + "\n" +
                "\t\tWHEN DOCTO_SERVICO.TP_DOCUMENTO_SERVICO <> 'CTR' THEN  NULL\n" +
                "\tEND as DS_NATUREZA_PRODUTO");        
        
        
    	/** FROM */
    	sql.addFrom("DOCTO_SERVICO", "DOCTO_SERVICO");
    	sql.addFrom("LOCALIZACAO_MERCADORIA", "LOCALIZACAO_MERCADORIA");
    	sql.addFrom("FILIAL", "FILIAL_ORIGEM");
    	sql.addFrom("FILIAL", "FILIAL_DESTINO");
    	sql.addFrom("OCORRENCIA_DOCTO_SERVICO", "OCORRENCIA_DOCTO_SERVICO");
      	sql.addFrom("CONHECIMENTO", "CONHECIMENTO");
      	sql.addFrom("NATUREZA_PRODUTO", "NATUREZA_PRODUTO");
      	sql.addFrom("PESSOA", "CLIENTE_REMETENTE");
      	sql.addFrom("PESSOA", "CLIENTE_DESTINATARIO");
      	sql.addFrom("MOEDA", "MOEDA");
      	

    	/** JOIN */
        sql.addJoin("DOCTO_SERVICO.ID_LOCALIZACAO_MERCADORIA", "LOCALIZACAO_MERCADORIA.ID_LOCALIZACAO_MERCADORIA");
    	sql.addJoin("DOCTO_SERVICO.ID_FILIAL_ORIGEM", "FILIAL_ORIGEM.ID_FILIAL");
    	sql.addJoin("DOCTO_SERVICO.ID_FILIAL_DESTINO", "FILIAL_DESTINO.ID_FILIAL(+)");
    	sql.addJoin("DOCTO_SERVICO.ID_DOCTO_SERVICO", "OCORRENCIA_DOCTO_SERVICO.ID_DOCTO_SERVICO");
    	sql.addJoin("DOCTO_SERVICO.ID_DOCTO_SERVICO", "CONHECIMENTO.ID_CONHECIMENTO(+)");
    	sql.addJoin("CONHECIMENTO.ID_NATUREZA_PRODUTO", "NATUREZA_PRODUTO.ID_NATUREZA_PRODUTO(+)");
    	sql.addJoin("DOCTO_SERVICO.ID_CLIENTE_REMETENTE", "CLIENTE_REMETENTE.ID_PESSOA(+)");
    	sql.addJoin("DOCTO_SERVICO.ID_CLIENTE_DESTINATARIO", "CLIENTE_DESTINATARIO.ID_PESSOA(+)"); 
    	sql.addJoin("DOCTO_SERVICO.ID_MOEDA", "MOEDA.ID_MOEDA");
    	
        
    	/** CRITERIA */
    	sql.addCriteria("DOCTO_SERVICO.BL_BLOQUEADO", "=", "S");
    	sql.addCustomCriteria("OCORRENCIA_DOCTO_SERVICO.DH_LIBERACAO IS NULL");
    	sql.addCriteria("TRUNC(CAST(OCORRENCIA_DOCTO_SERVICO.DH_BLOQUEIO AS DATE))", ">=", parameters.getYearMonthDay("periodoInicial"));
    	sql.addCriteria("TRUNC(CAST(OCORRENCIA_DOCTO_SERVICO.DH_BLOQUEIO AS DATE))", "<=", parameters.getYearMonthDay("periodoFinal"));
    	if (parameters.getLong("filialDestino.idFilial") != null) {
			sql.addCriteria("DOCTO_SERVICO.ID_FILIAL_DESTINO", "=", parameters.getLong("filialDestino.idFilial"));
		}
    	if (parameters.getLong("filialBloqueio.idFilial") != null) {
			sql.addCriteria("OCORRENCIA_DOCTO_SERVICO.ID_FILIAL_BLOQUEIO", "=", parameters.getLong("filialBloqueio.idFilial"));
		}    	
    	if (parameters.getLong("filialOrigem.idFilial") != null) {
    		sql.addCriteria("DOCTO_SERVICO.ID_FILIAL_ORIGEM", "=", parameters.getLong("filialOrigem.idFilial"));
		}    	    	
    	if (parameters.getLong("localizacaoMercadoria.idLocalizacaoMercadoria") != null) {
			sql.addCriteria("DOCTO_SERVICO.ID_LOCALIZACAO_MERCADORIA", "=",	parameters.getLong("localizacaoMercadoria.idLocalizacaoMercadoria"));
		}
    	if (parameters.getLong("ocorrenciaBloqueio.idOcorrenciaPendencia") != null) {
			sql.addCriteria("OCORRENCIA_DOCTO_SERVICO.ID_OCOR_BLOQUEIO", "=", parameters.getLong("ocorrenciaBloqueio.idOcorrenciaPendencia"));
		}
    	if (parameters.getLong("idDoctoServico") != null){
    		sql.addCriteria("DOCTO_SERVICO.ID_DOCTO_SERVICO", "=", parameters.getLong("idDoctoServico"));
    	} else {
    		if (!parameters.getString("doctoServico.tpDocumentoServico").equals("")){
    			sql.addCriteria("DOCTO_SERVICO.TP_DOCUMENTO_SERVICO", "=", parameters.getString("doctoServico.tpDocumentoServico"));
    		}
        	if (parameters.getLong("doctoServico.filialByIdFilialOrigem.idFilial") != null) {
        		sql.addCriteria("DOCTO_SERVICO.ID_FILIAL_ORIGEM", "=", parameters.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));			
    		}
    		
    	}
    	if (parameters.getYearMonthDay("emissaoDocumentoInicial") != null) {
    		sql.addCriteria("TRUNC(CAST(DOCTO_SERVICO.DH_EMISSAO AS DATE))", ">=", parameters.getYearMonthDay("emissaoDocumentoInicial"));
		}
    	if (parameters.getYearMonthDay("emissaoDocumentoFinal") != null) {
    		sql.addCriteria("TRUNC(CAST(DOCTO_SERVICO.DH_EMISSAO AS DATE))", "<=", parameters.getYearMonthDay("emissaoDocumentoFinal"));
		}
    	if (parameters.getLong("clienteRemetente.idCliente") != null) {
			sql.addCriteria("DOCTO_SERVICO.ID_CLIENTE_REMETENTE", "=", parameters.getLong("clienteRemetente.idCliente"));
		}
    	if (parameters.getLong("municipioRemetente.idMunicipio") != null) {        	
          	sql.addFrom("ENDERECO_PESSOA", "ENDERECO_REMETENTE");        	
        	sql.addJoin("CLIENTE_REMETENTE.ID_PESSOA", "ENDERECO_REMETENTE.ID_PESSOA");       		
    		sql.addCriteria("ENDERECO_REMETENTE.ID_MUNICIPIO", "=", parameters.getLong("municipioRemetente.idMunicipio"));
		}
    	if (parameters.getLong("clienteDestinatario.idCliente") != null) {
    		sql.addCriteria("DOCTO_SERVICO.ID_CLIENTE_DESTINATARIO", "=", parameters.getLong("clienteDestinatario.idCliente"));
		}
    	if (parameters.getLong("municipioDestinatario.idMunicipio") != null) {        	
          	sql.addFrom("ENDERECO_PESSOA", "ENDERECO_DESTINATARIO");        	
        	sql.addJoin("CLIENTE_DESTINATARIO.ID_PESSOA", "ENDERECO_DESTINATARIO.ID_PESSOA");    		
    		sql.addCriteria("ENDERECO_DESTINATARIO.ID_MUNICIPIO", "=", parameters.getLong("municipioDestinatario.idMunicipio"));
		}
    	if (!parameters.getString("tpFrete").equals("")) {
    		sql.addCriteria("CONHECIMENTO.TP_FRETE", "=", parameters.getString("tpFrete"));
		}
    	if (parameters.getInteger("nrNotaFiscal") != null) {
        	sql.addFrom("NOTA_FISCAL_CONHECIMENTO", "NOTA_FISCAL_CONHECIMENTO");
        	sql.addJoin("DOCTO_SERVICO.ID_DOCTO_SERVICO", "NOTA_FISCAL_CONHECIMENTO.ID_CONHECIMENTO");    		
    		sql.addCriteria("NOTA_FISCAL_CONHECIMENTO.NR_NOTA_FISCAL", "=", parameters.getInteger("nrNotaFiscal"));
		}	      
    	if (parameters.getInteger("diasBloqueioInicial") != null && parameters.getInteger("diasBloqueioFinal") != null) {
    		sql.addCustomCriteria("FLOOR(CURRENT_DATE - TRUNC(CAST(OCORRENCIA_DOCTO_SERVICO.DH_BLOQUEIO AS DATE))) " +
    		"BETWEEN " + parameters.getInteger("diasBloqueioInicial") + " AND " + parameters.getInteger("diasBloqueioFinal"));    		
		}
    	
        return sql;
    }
    
    
	/**
	 * Método que gera o subrelatorio de Ocorrencias. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirOcorrencias(Long idDoctoServico) throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection(PropertyVarcharI18nProjection.createProjection("OCORRENCIA_PENDENCIA.DS_OCORRENCIA_I"), "DS_OCORRENCIA");
        sql.addProjection("OCORRENCIA_DOCTO_SERVICO.DH_BLOQUEIO", "DH_BLOQUEIO");        
        
        sql.addFrom("OCORRENCIA_DOCTO_SERVICO", "OCORRENCIA_DOCTO_SERVICO");
        sql.addFrom("OCORRENCIA_PENDENCIA", "OCORRENCIA_PENDENCIA");

        sql.addJoin("OCORRENCIA_DOCTO_SERVICO.ID_OCOR_BLOQUEIO", "OCORRENCIA_PENDENCIA.ID_OCORRENCIA_PENDENCIA");
        sql.addCriteria("OCORRENCIA_DOCTO_SERVICO.ID_DOCTO_SERVICO", "=", idDoctoServico);
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }       
    
	/**
	 * Método que recebe o total do valor de mercadoria e converte para a moeda do usuário logado.
	 * @param totalVlMercadoria
	 * @return
	 * @throws Exception
	 */
    public String executeConversaoMoeda(Long idMoeda, BigDecimal totalVlMercadoria) throws Exception {
		BigDecimal valorConvertido = this.getConversaoMoedaService().findConversaoMoeda(
									 SessionUtils.getPaisSessao().getIdPais(),
									 idMoeda,
									 SessionUtils.getPaisSessao().getIdPais(),
									 SessionUtils.getMoedaSessao().getIdMoeda(),
									 JTDateTimeUtils.getDataAtual(),
									 totalVlMercadoria );	    	
    	
    	return FormatUtils.formatDecimal("#,###,###,###,##0.00", valorConvertido);
    }
    
    

    /**
     * Configura Dominios
     */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
		config.configDomainField("TP_FRETE", "DM_TIPO_FRETE");		
	}
	
	/**
	 * Obtem os parametros comuns a ambas as consultas de coleta e entrega
	 * @param param
	 * @return
	 */
	private String getFilterSummary(TypedFlatMap param) {
		SqlTemplate sqlTemp = createSqlTemplate();
		
		sqlTemp.addFilterSummary("periodoInicial", JTFormatUtils.format(param.getYearMonthDay("periodoInicial")));
		sqlTemp.addFilterSummary("periodoFinal", JTFormatUtils.format(param.getYearMonthDay("periodoFinal")));
         
    	if (param.getLong("filialOrigem.idFilial") != null) {
    		sqlTemp.addFilterSummary("filialOrigem", param.getString("filialOrigem.sgFilial"));
		}
    	if (param.getLong("filialByIdFilialOrigem.idFilial") != null) {
			sqlTemp.addFilterSummary("filialOrigem", param.getString("filialByIdFilialOrigem.sgFilial"));
		}     	
    	if (param.getLong("filialDestino.idFilial") != null) {
    		sqlTemp.addFilterSummary("filialDestino", param.getString("filialDestino.sgFilial"));	
    	}
    	if (param.getLong("filialBloqueio.idFilial") != null) {
    		sqlTemp.addFilterSummary("filialBloqueio", param.getString("filialBloqueio.sgFilial"));
		}   	
    	if (param.getLong("localizacaoMercadoria.idLocalizacaoMercadoria") != null) {
    		sqlTemp.addFilterSummary("localizacaoMercadoria", param.getString("localizacaoMercadoria.dsLocalizacaoMercadoria"));
		}
    	if (param.getLong("ocorrenciaBloqueio.idOcorrenciaPendencia") != null) {
    		sqlTemp.addFilterSummary("ocorrenciaBloqueio", param.getString("ocorrenciaBloqueio.dsOcorrencia"));
		}
    	
    	String strDoctoServico = new String();
    	if (!param.getString("doctoServico.tpDocumentoServico").equals("")){
    		strDoctoServico = this.getDomainValueService().findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO", param.getString("doctoServico.tpDocumentoServico"));
    	}
    	if (!param.getString("doctoServico.filialByIdFilialOrigem.sgFilial").equals("")){
    		strDoctoServico += " " + param.getString("doctoServico.filialByIdFilialOrigem.sgFilial");
    	}
    	if (param.getLong("idDoctoServico") != null && param.getLong("doctoServico.nrDoctoServico") != null) {
    		strDoctoServico += " " + FormatUtils.fillNumberWithZero(param.getLong("doctoServico.nrDoctoServico").toString(), 8);
		} 
    	sqlTemp.addFilterSummary("documentoServico", strDoctoServico);
    	
    	if (param.getYearMonthDay("emissaoDocumentoInicial") != null) {
    		String emissao = JTFormatUtils.format(param.getYearMonthDay("emissaoDocumentoInicial"));
    		if (param.getYearMonthDay("emissaoDocumentoFinal") != null) {
    			emissao += " " + configuracoesFacade.getMensagem("ate") + " " + JTFormatUtils.format(param.getYearMonthDay("emissaoDocumentoFinal"));
    		}
    		sqlTemp.addFilterSummary("emissaoDocumento", emissao);
		} else if (param.getYearMonthDay("emissaoDocumentoFinal") != null) {
			
			String emissao = configuracoesFacade.getMensagem("ateTitulo") + " " + JTFormatUtils.format(param.getYearMonthDay("emissaoDocumentoFinal"));
			sqlTemp.addFilterSummary("emissaoDocumento", emissao);
		}
    	if (!param.getString("tpFrete").equals("")) {
    		sqlTemp.addFilterSummary("tipoFrete", this.getDomainValueService().
														findDomainValueDescription("DM_TIPO_FRETE", param.getString("tpFrete")));
		}
    	if (param.getInteger("nrNotaFiscal") != null) {
    		sqlTemp.addFilterSummary("notaFiscal", param.getInteger("nrNotaFiscal"));
		}
    	if (param.getLong("clienteRemetente.idCliente") != null) {
    		sqlTemp.addFilterSummary("clienteRemetente", param.getString("clienteRemetente.pessoa.nmPessoa"));
		}
    	if (param.getLong("clienteDestinatario.idCliente") != null) {
    		sqlTemp.addFilterSummary("clienteDestinatario", param.getString("clienteDestinatario.pessoa.nmPessoa"));
		}
    	if (param.getLong("municipioRemetente.idMunicipio") != null) {        	
    		sqlTemp.addFilterSummary("municipioRemetente", param.getString("municipioRemetente.nmMunicipio"));
    	}
    	if (param.getLong("municipioDestinatario.idMunicipio") != null) {
    		sqlTemp.addFilterSummary("municipioDestinatario", param.getString("municipioDestinatario.nmMunicipio"));
		}	      
    	if (param.getInteger("diasBloqueioInicial") != null && param.getInteger("diasBloqueioFinal") != null) {
    		sqlTemp.addFilterSummary("diasEmBloqueio", param.getInteger("diasBloqueioInicial") + " " + configuracoesFacade.getMensagem("ate") + " " + 
    												   param.getInteger("diasBloqueioFinal"));
		}
		
		return sqlTemp.getFilterSummary();
	}


	public DomainValueService getDomainValueService() {
		return domainValueService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	

}