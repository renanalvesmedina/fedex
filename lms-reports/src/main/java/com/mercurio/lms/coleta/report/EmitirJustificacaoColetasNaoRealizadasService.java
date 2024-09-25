package com.mercurio.lms.coleta.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de Justificação de Coletas Não Realizadas.
 * Especificação técnica 02.03.02.10
 * @author Cesar Gabbardo
 * 
 * @spring.bean id="lms.coleta.emitirJustificacaoColetasNaoRealizadasService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirJustificacaoColetasNaoRealizadas.jasper"
 */
public class EmitirJustificacaoColetasNaoRealizadasService extends ReportServiceSupport {
	private ConversaoMoedaService conversaoMoedaService;
		
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
        parametersReport.put("siglaSimbolo", SessionUtils.getMoedaSessao().getSiglaSimbolo());
        parametersReport.put("parametrosPesquisa", getFilterSummary(param));
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
    	// PEDIDO COLETA
    	sql.addProjection("PEDIDO_COLETA.ID_PEDIDO_COLETA", "ID_PEDIDO_COLETA");
    	sql.addProjection("FILIAL_RESPONSAVEL.SG_FILIAL", "SG_FILIAL_RESPONSAVEL");
    	sql.addProjection("PEDIDO_COLETA.NR_COLETA", "NR_COLETA");
    	sql.addProjection("PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA", "TP_MODO_PEDIDO_COLETA");
    	sql.addProjection("PEDIDO_COLETA.TP_PEDIDO_COLETA", "TP_PEDIDO_COLETA");
    	sql.addProjection("PESSOA.NM_PESSOA", "NM_PESSOA");
    	sql.addProjection("PEDIDO_COLETA.ED_COLETA", "ED_COLETA");
    	sql.addProjection("PEDIDO_COLETA.NR_ENDERECO", "NR_ENDERECO");
    	sql.addProjection("PEDIDO_COLETA.DS_COMPLEMENTO_ENDERECO", "DS_COMPLEMENTO_ENDERECO");
    	sql.addProjection("PEDIDO_COLETA.DS_BAIRRO", "DS_BAIRRO");
    	sql.addProjection("REGIAO_COLETA_ENTREGA_FIL.DS_REGIAO_COLETA_ENTREGA_FIL", "DS_REGIAO_COLETA_ENTREGA_FIL");
    	sql.addProjection("MUNICIPIO.NM_MUNICIPIO", "NM_MUNICIPIO");
    	sql.addProjection("PEDIDO_COLETA.NR_CEP", "NR_CEP");
    	sql.addProjection("ROTA_COLETA_ENTREGA.DS_ROTA", "DS_ROTA");
    	sql.addProjection("PEDIDO_COLETA.NM_CONTATO_CLIENTE", "NM_CONTATO_CLIENTE");
    	sql.addProjection("PEDIDO_COLETA.NR_DDD_CLIENTE", "NR_DDD_CLIENTE");
    	sql.addProjection("PEDIDO_COLETA.NR_TELEFONE_CLIENTE", "NR_TELEFONE_CLIENTE");
    	sql.addProjection("FILIAL_SOLICITANTE.SG_FILIAL", "SG_FILIAL_SOLICITANTE");
    	sql.addProjection("USUARIO.NM_USUARIO", "NM_USUARIO");
    	sql.addProjection("PEDIDO_COLETA.DH_COLETA_DISPONIVEL", "DH_COLETA_DISPONIVEL");
    	sql.addProjection("PEDIDO_COLETA.DT_PREVISAO_COLETA", "DT_PREVISAO_COLETA");
    	sql.addProjection("PEDIDO_COLETA.HR_LIMITE_COLETA", "HR_LIMITE_COLETA");
    	sql.addProjection("PEDIDO_COLETA.TP_STATUS_COLETA", "TP_STATUS_COLETA");
    	sql.addProjection("PEDIDO_COLETA.OB_PEDIDO_COLETA", "OB_PEDIDO_COLETA");
    	
    	// DETALHE COLETA
    	sql.addProjection("DETALHE_COLETA.ID_DETALHE_COLETA", "ID_DETALHE_COLETA");    	
    	sql.addProjection("CASE WHEN NOT("+PropertyVarcharI18nProjection.createProjection("LOCALIDADE_ESPECIAL.DS_LOCALIDADE_I")+" IS NULL) THEN " +
    			PropertyVarcharI18nProjection.createProjection("LOCALIDADE_ESPECIAL.DS_LOCALIDADE_I")+" ELSE MUNICIPIO_DETALHE.NM_MUNICIPIO END AS DESTINO_DETALHE");
    	sql.addProjection("CASE WHEN NOT(UF_LOCALIDADE.SG_UNIDADE_FEDERATIVA IS NULL) THEN" +
    					  "		UF_LOCALIDADE.SG_UNIDADE_FEDERATIVA ELSE UF_MUNICIPIO_DETALHE.SG_UNIDADE_FEDERATIVA END AS UF_DETALHE");
    	sql.addProjection("FILIAL_DETALHE.SG_FILIAL", "SG_FILIAL_DETALHE");
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("SERVICO.DS_SERVICO_I"), "DS_SERVICO");
    	sql.addProjection("PESSOA_DETALHE.NM_PESSOA", "NM_PESSOA_DETALHE");
    	sql.addProjection("DETALHE_COLETA.TP_FRETE", "TP_FRETE");
    	sql.addProjection("DETALHE_COLETA.PS_MERCADORIA", "PS_MERCADORIA");
    	sql.addProjection("DETALHE_COLETA.PS_AFORADO", "PS_AFORADO");
    	sql.addProjection("DETALHE_COLETA.QT_VOLUMES", "QT_VOLUMES");
    	sql.addProjection("MOEDA.ID_MOEDA", "ID_MOEDA");
    	sql.addProjection("MOEDA.SG_MOEDA", "SG_MOEDA");
    	sql.addProjection("MOEDA.DS_SIMBOLO", "DS_SIMBOLO");        
    	sql.addProjection("DETALHE_COLETA.VL_MERCADORIA", "VL_MERCADORIA");    	
    	sql.addProjection("FILIAL_COTACAO.SG_FILIAL", "SG_FILIAL_COTACAO");
    	sql.addProjection("COTACAO.NR_COTACAO", "NR_COTACAO");    	
    	sql.addProjection("CTO_INTERNACIONAL.SG_PAIS", "SG_PAIS_CRT");
    	sql.addProjection("CTO_INTERNACIONAL.NR_CRT", "NR_CRT");
    	
  	
    	/** FROM */
		sql.addFrom("PEDIDO_COLETA", "PEDIDO_COLETA");
		sql.addFrom("FILIAL", "FILIAL_RESPONSAVEL");
		sql.addFrom("FILIAL", "FILIAL_SOLICITANTE");
		sql.addFrom("PESSOA", "PESSOA");
		sql.addFrom("MUNICIPIO", "MUNICIPIO");
		sql.addFrom("ROTA_COLETA_ENTREGA", "ROTA_COLETA_ENTREGA");
        sql.addFrom("REGIAO_FILIAL_ROTA_COL_ENT", "REGIAO_FILIAL_ROTA_COL_ENT");
        sql.addFrom("REGIAO_COLETA_ENTREGA_FIL", "REGIAO_COLETA_ENTREGA_FIL");
        sql.addFrom("DETALHE_COLETA", "DETALHE_COLETA");
        sql.addFrom("LOCALIDADE_ESPECIAL", "LOCALIDADE_ESPECIAL");
        sql.addFrom("MUNICIPIO", "MUNICIPIO_DETALHE");
        sql.addFrom("UNIDADE_FEDERATIVA", "UF_LOCALIDADE");
        sql.addFrom("UNIDADE_FEDERATIVA", "UF_MUNICIPIO_DETALHE");
        sql.addFrom("FILIAL", "FILIAL_DETALHE");
        sql.addFrom("SERVICO", "SERVICO");
        sql.addFrom("PESSOA", "PESSOA_DETALHE");
        sql.addFrom("MOEDA", "MOEDA");
        sql.addFrom("USUARIO", "USUARIO");        
        sql.addFrom("COTACAO", "COTACAO");
        sql.addFrom("FILIAL", "FILIAL_COTACAO");
        sql.addFrom("CTO_INTERNACIONAL", "CTO_INTERNACIONAL");
                
        
		/** JOIN */
		sql.addJoin("PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL", "FILIAL_RESPONSAVEL.ID_FILIAL");
		sql.addJoin("PEDIDO_COLETA.ID_FILIAL_SOLICITANTE", "FILIAL_SOLICITANTE.ID_FILIAL");
		sql.addJoin("PEDIDO_COLETA.ID_CLIENTE", "PESSOA.ID_PESSOA");
		sql.addJoin("PEDIDO_COLETA.ID_MUNICIPIO", "MUNICIPIO.ID_MUNICIPIO");
		sql.addJoin("PEDIDO_COLETA.ID_ROTA_COLETA_ENTREGA", "ROTA_COLETA_ENTREGA.ID_ROTA_COLETA_ENTREGA");
		sql.addJoin("ROTA_COLETA_ENTREGA.ID_ROTA_COLETA_ENTREGA", "REGIAO_FILIAL_ROTA_COL_ENT.ID_ROTA_COLETA_ENTREGA(+)");
		sql.addJoin("REGIAO_FILIAL_ROTA_COL_ENT.ID_REGIAO_COLETA_ENTREGA_FIL", "REGIAO_COLETA_ENTREGA_FIL.ID_REGIAO_COLETA_ENTREGA_FIL(+)");
		sql.addJoin("PEDIDO_COLETA.ID_PEDIDO_COLETA", "DETALHE_COLETA.ID_PEDIDO_COLETA");
		sql.addJoin("DETALHE_COLETA.ID_LOCALIDADE_ESPECIAL", "LOCALIDADE_ESPECIAL.ID_LOCALIDADE_ESPECIAL(+)");
		sql.addJoin("DETALHE_COLETA.ID_MUNICIPIO", "MUNICIPIO_DETALHE.ID_MUNICIPIO(+)");
		sql.addJoin("LOCALIDADE_ESPECIAL.ID_UNIDADE_FEDERATIVA", "UF_LOCALIDADE.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("MUNICIPIO_DETALHE.ID_UNIDADE_FEDERATIVA", "UF_MUNICIPIO_DETALHE.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("DETALHE_COLETA.ID_FILIAL", "FILIAL_DETALHE.ID_FILIAL(+)");
		sql.addJoin("DETALHE_COLETA.ID_SERVICO", "SERVICO.ID_SERVICO");
		sql.addJoin("DETALHE_COLETA.ID_CLIENTE", "PESSOA_DETALHE.ID_PESSOA(+)");
		sql.addJoin("PEDIDO_COLETA.ID_MOEDA", "MOEDA.ID_MOEDA");
		sql.addJoin("PEDIDO_COLETA.ID_USUARIO", "USUARIO.ID_USUARIO");
		sql.addJoin("DETALHE_COLETA.ID_COTACAO", "COTACAO.ID_COTACAO(+)");
		sql.addJoin("COTACAO.ID_FILIAL_ORIGEM", "FILIAL_COTACAO.ID_FILIAL(+)");
		sql.addJoin("DETALHE_COLETA.ID_CTO_INTERNACIONAL", "CTO_INTERNACIONAL.ID_CTO_INTERNACIONAL(+)");
		
				
		/** CRITERIA */
		sql.addCriteria("PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL", "=", SessionUtils.getFilialSessao().getIdFilial());
    	sql.addCriteria("TRUNC(CAST(PEDIDO_COLETA.DH_PEDIDO_COLETA AS DATE))", ">=", parameters.getYearMonthDay("dataInicial"));
    	sql.addCriteria("TRUNC(CAST(PEDIDO_COLETA.DH_PEDIDO_COLETA AS DATE))", "<=", parameters.getYearMonthDay("dataFinal"));
    	
    	if(parameters.getLong("servico.idServico") != null){
    		sql.addCriteria("DETALHE_COLETA.ID_SERVICO", "=", parameters.getLong("servico.idServico"));
    	}
    	
    	if(StringUtils.isNotBlank(parameters.getString("tpPedidoColeta"))){
    		sql.addCriteria("PEDIDO_COLETA.TP_PEDIDO_COLETA", "=", parameters.getString("tpPedidoColeta"));
    	}

    	sql.addCustomCriteria("( (REGIAO_FILIAL_ROTA_COL_ENT.ID_ROTA_COLETA_ENTREGA IS NULL) OR " +
    						  "	 (REGIAO_FILIAL_ROTA_COL_ENT.DT_VIGENCIA_INICIAL <= CURRENT_DATE" +
    						  "	  AND REGIAO_FILIAL_ROTA_COL_ENT.DT_VIGENCIA_FINAL >= CURRENT_DATE) )");
    	
    	if (parameters.getBoolean("naoProgramadas").booleanValue()) {
        	sql.addCriteria("PEDIDO_COLETA.TP_STATUS_COLETA", "=", "AB"); 
		} else {
	    	sql.addCustomCriteria("PEDIDO_COLETA.TP_STATUS_COLETA IN ('AB', 'MA', 'TR')"); 			
		}
   	
    	sql.addCustomCriteria("EXISTS ( SELECT * " +
				  " 		FROM   EVENTO_COLETA EVENTO_COLETA" +
				  "         WHERE  PEDIDO_COLETA.ID_PEDIDO_COLETA = EVENTO_COLETA.ID_PEDIDO_COLETA" +
				  "                AND EVENTO_COLETA.TP_EVENTO_COLETA = 'RC' ) ");

        return sql;
    }
    
	/**
	 * Método que gera o subrelatorio de AWB. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirJustificacaoColetasNaoRealizadasSubAWB(Long idDetalheColeta) {
        SqlTemplate sql = createSqlTemplate();
        
        sql.addProjection("AWB.ID_AWB", "ID_AWB");
        sql.addProjection("AWB.DS_SERIE", "DS_SERIE");
        sql.addProjection("AWB.NR_AWB", "NR_AWB");
        sql.addProjection("AWB.DV_AWB", "DV_AWB");
        sql.addProjection("AWB.TP_STATUS_AWB", "TP_STATUS_AWB");
        sql.addProjection("EMP.SG_EMPRESA", "SG_EMPRESA");
        
        sql.addFrom("DETALHE_COLETA", "DC");
        sql.addFrom("DOCTO_SERVICO", "DS");
        sql.addFrom("CTO_AWB", "CTO");
        sql.addFrom("AWB", "AWB");
        sql.addFrom("CIA_FILIAL_MERCURIO", "CFM");
        sql.addFrom("EMPRESA", "EMP");

    	sql.addJoin("DC.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO");
    	sql.addJoin("DS.ID_DOCTO_SERVICO", "CTO.ID_CONHECIMENTO");
    	sql.addJoin("CTO.ID_AWB", "AWB.ID_AWB");
    	sql.addJoin("AWB.ID_CIA_FILIAL_MERCURIO", "CFM.ID_CIA_FILIAL_MERCURIO");
    	sql.addJoin("CFM.ID_EMPRESA", "EMP.ID_EMPRESA");
    	sql.addCriteria("DC.ID_DETALHE_COLETA", "=", idDetalheColeta);
    	sql.addCustomCriteria("AWB.ID_AWB = (select max(a.id_awb) from   cto_awb a join awb on awb.id_awb = a.id_awb where  a.id_conhecimento = DC.id_docto_servico and awb.tp_status_awb <> 'C')");
 
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();    	
    }
    
	/**
	 * Método que gera o subrelatorio de Nota Fiscal. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirJustificacaoColetasNaoRealizadasSubNF(Long idDetalheColeta) {
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("NOTA_FISCAL_COLETA.NR_NOTA_FISCAL", "NR_NOTA_FISCAL");
        
        sql.addFrom("NOTA_FISCAL_COLETA", "NOTA_FISCAL_COLETA");

        sql.addCriteria("NOTA_FISCAL_COLETA.ID_DETALHE_COLETA", "=", idDetalheColeta);
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource(); 
    }    
    
    
	/**
	 * Método que gera o subrelatorio de Ocorrencias. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirJustificacaoColetasNaoRealizadasSub(Long idPedidoColeta) throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("EVENTO_COLETA.TP_EVENTO_COLETA", "TP_EVENTO_COLETA");
        sql.addProjection("EVENTO_COLETA.DH_EVENTO", "DH_EVENTO");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("OCORRENCIA_COLETA.DS_DESCRICAO_RESUMIDA_I"), "DS_DESCRICAO_RESUMIDA");
        
        sql.addFrom("EVENTO_COLETA", "EVENTO_COLETA");
        sql.addFrom("OCORRENCIA_COLETA", "OCORRENCIA_COLETA");
        
        sql.addJoin("EVENTO_COLETA.ID_OCORRENCIA_COLETA", "OCORRENCIA_COLETA.ID_OCORRENCIA_COLETA");
        sql.addCriteria("EVENTO_COLETA.ID_PEDIDO_COLETA", "=", idPedidoColeta);         
        
        sql.addOrderBy("EVENTO_COLETA.DH_EVENTO");
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }       
    
    
    /**
     * Método que converte o valor de mercadoria do detalhe coleta.
     * @param vlMercadoria
     * @param idMoeda
     * @return
     */
    public BigDecimal executeConverteValorMercadoria(BigDecimal vlMercadoria, Long idMoeda) {    	
    	BigDecimal valorConvertido = this.getConversaoMoedaService()
									.findConversaoMoeda( SessionUtils.getPaisSessao().getIdPais(),
														 idMoeda,
													   	 SessionUtils.getPaisSessao().getIdPais(),
													   	 SessionUtils.getMoedaSessao().getIdMoeda(),
													   	 JTDateTimeUtils.getDataAtual(),
													   	 vlMercadoria );
    	return valorConvertido;
    }
    

    /**
     * Configura Dominios
     */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_PEDIDO_COLETA", "DM_TIPO_PEDIDO_COLETA");
		config.configDomainField("TP_FRETE", "DM_TIPO_FRETE");
		config.configDomainField("TP_EVENTO_COLETA", "DM_TIPO_EVENTO_COLETA");
		config.configDomainField("TP_MODO_PEDIDO_COLETA", "DM_MODO_PEDIDO_COLETA");
		config.configDomainField("TP_STATUS_COLETA", "DM_STATUS_COLETA");
	}    
    
    
    /**
	 * Obtem os parametros comuns da consulta.
	 * @param param
	 * @return
	 */
	private String getFilterSummary(TypedFlatMap param) {
		SqlTemplate sqlTemp = createSqlTemplate();
		
		sqlTemp.addFilterSummary("periodoInicial", JTFormatUtils.format(param.getYearMonthDay("dataInicial")));
		sqlTemp.addFilterSummary("periodoFinal", JTFormatUtils.format(param.getYearMonthDay("dataFinal")));
		sqlTemp.addFilterSummary("servico", param.getString("servico.dsServico"));
		sqlTemp.addFilterSummary("tipoColeta", param.getString("dsTpPedidoColeta"));
         
    	if (param.getBoolean("naoProgramadas").booleanValue()) {
    		sqlTemp.addFilterSummary("naoProgramadas", "Sim");
		}
		
		return sqlTemp.getFilterSummary();
	}

	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}	

}
