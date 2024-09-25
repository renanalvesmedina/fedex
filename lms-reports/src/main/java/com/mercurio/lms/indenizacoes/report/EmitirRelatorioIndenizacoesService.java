package com.mercurio.lms.indenizacoes.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.indenizacoes.model.FilialDebitada;
import com.mercurio.lms.indenizacoes.model.service.FilialDebitadaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração da df Relatório de RIMs emitidos e não pagos
 * Especificação técnica 21.04.01.03
 * @author Rodrigo Antunes
 * 
 * @spring.bean id="lms.indenizacoes.emitirRelatorioIndenizacoesService"
 * @spring.property name="reportName" value="com/mercurio/lms/indenizacoes/report/emitirRelatorioIndenizacoes.jasper"
 */
public class EmitirRelatorioIndenizacoesService extends ReportServiceSupport {

	private ConversaoMoedaService conversaoMoedaService;
	private EnderecoPessoaService enderecoPessoaService;
	private FilialDebitadaService filialDebitadaService;
	
	private EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	private ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	/**
	 * Método que chama o relatorio principal  
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap tfm = (TypedFlatMap)parameters; 
        SqlTemplate sql = createMainSql(tfm);  
        
        List<Map> lista = getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
        
        List subListQuery;
        String subQuery = " select to_char(er.dh_evento_rim,'dd/mm/yyyy') || ' - ' || u.nm_usuario evento" +
						  " from evento_rim er, " +
						  "     usuario u " +    
						  " where er.tp_evento_indenizacao = ?  " +
						  " and rownum = 1 " +
						  " and er.id_usuario = u.id_usuario " +
						  " and er.id_recibo_indenizacao = ? " +
						  " order by er.dh_evento_rim desc ";
        
        for (Map map : lista) {
        	String idReciboIdenizacao = ((BigDecimal) map.get("ID_RECIBO_INDENIZACAO")).toString();
        	
        	if (map.get("ID_FILIAL_RIM")!=null){
        		map.put("ID_FILIAL_RIM",Long.valueOf(((BigDecimal)map.get("ID_FILIAL_RIM")).longValue()));
        	}
        	
        	if(map.get("ID_MOEDA_RIM")!=null){
        		map.put("ID_MOEDA_RIM",Long.valueOf(((BigDecimal)map.get("ID_MOEDA_RIM")).longValue()));
        	}
        	
        	if (map.get("ID_NAO_CONFORMIDADE")!=null) {
        		map.put("ID_NAO_CONFORMIDADE",Long.valueOf(((BigDecimal)map.get("ID_NAO_CONFORMIDADE")).longValue()));
        	}
        	
        	if (map.get("ID_RECIBO_INDENIZACAO")!=null){
        		map.put("ID_RECIBO_INDENIZACAO",Long.valueOf(((BigDecimal)map.get("ID_RECIBO_INDENIZACAO")).longValue()));
        	}
        	
        	if(map.get("ID_DOCTO_SERVICO_INDENIZACAO")!=null){
        		map.put("ID_DOCTO_SERVICO_INDENIZACAO",Long.valueOf(((BigDecimal)map.get("ID_DOCTO_SERVICO_INDENIZACAO")).longValue()));
        	}
        	
        	if(map.get("NR_RECIBO_INDENIZACAO")!=null){
        		map.put("NR_RECIBO_INDENIZACAO",Long.valueOf(((BigDecimal)map.get("NR_RECIBO_INDENIZACAO")).longValue()));
        	}
        	
        	
        	if(map.get("NR_DOCTO_SERVICO")!=null){
        		map.put("NR_DOCTO_SERVICO",Long.valueOf(((BigDecimal)map.get("NR_DOCTO_SERVICO")).longValue()));
        	}
        	
        	
        	if(map.get("NR_NAO_CONFORMIDADE")!=null){
        		map.put("NR_NAO_CONFORMIDADE",Long.valueOf(((BigDecimal)map.get("NR_NAO_CONFORMIDADE")).longValue()));
        	}
        	
        	
        	if(map.get("NR_NOTA_FISCAL")!=null){
        		map.put("NR_NOTA_FISCAL",((BigDecimal)map.get("NR_NOTA_FISCAL")).toString());
        	}
        	
        	if(idReciboIdenizacao != null){
        		
        		subListQuery = getJdbcTemplate().queryForList(subQuery, new Object[]{"RM",idReciboIdenizacao});
        		if (subListQuery!=null && subListQuery.size()!=0){
        			map.put("RECEBIMENTO_PROCESSO", (String)((Map)subListQuery.get(0)).get("evento"));
        		} else {
        			map.put("RECEBIMENTO_PROCESSO", "");
        		}
        		
        		subListQuery = getJdbcTemplate().queryForList(subQuery, new Object[]{"RE",idReciboIdenizacao});
        		if (subListQuery!=null && subListQuery.size()!=0){
        			map.put("DEVOLUCAO", (String)((Map)subListQuery.get(0)).get("evento"));
        		} else {
        			map.put("DEVOLUCAO", "");
        		}
        		
        		subListQuery = getJdbcTemplate().queryForList(subQuery, new Object[]{"CR",idReciboIdenizacao});
        		if (subListQuery!=null && subListQuery.size()!=0){
        			map.put("CANCELAMENTO", (String)((Map)subListQuery.get(0)).get("evento"));
        		} else {
        			map.put("CANCELAMENTO", "");
        		}
        		
        		subListQuery = getJdbcTemplate().queryForList(subQuery, new Object[]{"LI",idReciboIdenizacao});
        		if (subListQuery!=null && subListQuery.size()!=0){
        			map.put("LIBERACAO_PAGAMENTO", (String)((Map)subListQuery.get(0)).get("evento"));
        		} else {
        			map.put("LIBERACAO_PAGAMENTO", "");
        		}
        	}
		}
        
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        parametersReport.put("SERVICE",this);
        return createReportDataObject(new JRMapCollectionDataSource(lista),parametersReport);
	}
	
	/**
	 * consulta principal 
	 * @param tfm
	 * @return
	 */
	public SqlTemplate createMainSql(TypedFlatMap tfm) {
		SqlTemplate sql = createSqlTemplate();
		
		// 1 - Regional
				
		sql.addProjection("regional.SG_REGIONAL","SG_REGIONAL");

		// 2 - Filial
				
		sql.addProjection("f_rim.ID_FILIAL","ID_FILIAL_RIM");
		sql.addProjection("f_rim.SG_FILIAL","SG_FILIAL_RIM");
				
		// 3 - RIM
				
		sql.addProjection("rim.ID_RECIBO_INDENIZACAO","ID_RECIBO_INDENIZACAO");
		sql.addProjection("rim.NR_RECIBO_INDENIZACAO","NR_RECIBO_INDENIZACAO");
		sql.addProjection("rim.DT_EMISSAO","DT_EMISSAO");
				
				//alteração 666
				sql.addProjection("rim.DT_PAGAMENTO_EFETUADO","DT_PAGAMENTO_EFETUADO, " +
						"" + PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I","TP_INDENIZACAO")+
						"," + PropertyVarcharI18nProjection.createProjection("VD3.DS_VALOR_DOMINIO_I","TP_STATUS_INDENIZACAO")+
						"," + PropertyVarcharI18nProjection.createProjection("VD4.DS_VALOR_DOMINIO_I","MODAL")+
						"," + PropertyVarcharI18nProjection.createProjection("VD4.DS_VALOR_DOMINIO_I","ABRANGENCIA"));
		sql.addProjection("ts.SG_TIPO","SG_TIPO_SEGURO");
		sql.addProjection("ps.NR_PROCESSO_SINISTRO","NR_PROCESSO_SINISTRO");
				sql.addProjection("beneficiario.NR_IDENTIFICACAO || ' - ' || beneficiario.NM_PESSOA","NM_BENEFICIARIO");
				sql.addProjection("favorecido.NR_IDENTIFICACAO || ' - ' || favorecido.NM_PESSOA","NM_FAVORECIDO");
		sql.addProjection("mrim.DS_SIMBOLO","DS_SIMBOLO_RIM");
		sql.addProjection("mrim.SG_MOEDA","SG_MOEDA_RIM");
		sql.addProjection("mrim.ID_MOEDA","ID_MOEDA_RIM");
		sql.addProjection("rim.VL_INDENIZACAO","VL_INDENIZACAO");
				sql.addProjection("FLOOR(current_date - RIM.dt_emissao)","DIAS_EM_ABERTO");
		sql.addProjection("mrim.DS_SIMBOLO","DS_SIMBOLO");
		sql.addProjection("mrim.SG_MOEDA","SG_MOEDA");
				
				sql.addProjection("jde.id_lote_jde_rim","NR_LOTE");

		sql.addProjection("pessoa_devedor.nm_pessoa","NM_DEVEDOR");		
		// 4 - Docto Servico

        sql.addProjection("moeda_dsi.SG_MOEDA","SG_MOEDA_DSI");
        sql.addProjection("moeda_dsi.DS_SIMBOLO","DS_SIMBOLO_DSI");		
		sql.addProjection("dsi.VL_INDENIZADO","VL_INDENIZADO");
		sql.addProjection("ds.VL_MERCADORIA","VL_MERCADORIA");
        sql.addProjection("ds.ID_DOCTO_SERVICO","ID_DOCTO_SERVICO");
        sql.addProjection("fo_ds.SG_FILIAL","SG_FILIAL_DOCTO");
        sql.addProjection("ds.TP_DOCUMENTO_SERVICO", "TP_DOCUMENTO_SERVICO");
        sql.addProjection("ds.NR_DOCTO_SERVICO","NR_DOCTO_SERVICO");
        sql.addProjection("pr.NM_PESSOA","REMETENTE");
        sql.addProjection("pd.NM_PESSOA","DESTINATARIO");
        sql.addProjection("ds.VL_TOTAL_DOC_SERVICO","VL_TOTAL_DOC_SERVICO");
		        
		// 5 - Notas Fiscais
		        sql.addProjection("nfc.NR_NOTA_FISCAL","NR_NOTA_FISCAL");
		        
		// 6 - Ocorrencias RNC
        sql.addProjection("fnc.SG_FILIAL","FILIAL_RNC");
        sql.addProjection("nc.NR_NAO_CONFORMIDADE","NR_NAO_CONFORMIDADE");
        sql.addProjection("nc.ID_NAO_CONFORMIDADE","ID_NAO_CONFORMIDADE"); 
        sql.addProjection("dsi.ID_DOCTO_SERVICO_INDENIZACAO","ID_DOCTO_SERVICO_INDENIZACAO");
		sql.addProjection("mds.DS_SIMBOLO","DS_SIMBOLO_DS");
		sql.addProjection("mds.SG_MOEDA","SG_MOEDA_DS");
		sql.addProjection("mds.ID_MOEDA","ID_MOEDA_DS");        

        
				sql.addProjection("to_char(nvl(rim.Id_Lote_Jde_Rim,0))","LOTE_JDE");
		// - - Fim Grupos
				
		sql.addFrom("moeda", "moeda_dsi");
        sql.addFrom("filial","fnc");
        sql.addFrom("filial","fo_ds");
        sql.addFrom("docto_servico_indenizacao","dsi");
        sql.addFrom("docto_servico","ds");
        sql.addFrom("moeda","mds");
        sql.addFrom("servico");
        sql.addFrom("cliente","cr");
        sql.addFrom("cliente","cd");
        sql.addFrom("pessoa","pr");
        sql.addFrom("pessoa","pd");
        sql.addFrom("nao_conformidade","nc");
		        sql.addFrom("RECIBO_INDENIZACAO_NF","rin");
		        sql.addFrom("NOTA_FISCAL_CONHECIMENTO","nfc");
		
		        sql.addFrom("lote_jde_rim","jde");
		
		        
		sql.addFrom("devedor_doc_serv_fat", "ddsf");                
		// fim docto servico -----------------------------------------------------------		
				
		sql.addFrom("moeda","mrim");
		sql.addFrom("recibo_indenizacao","rim");
		sql.addFrom("filial","f_rim");
		sql.addFrom("regional_filial","rf");
		sql.addFrom("regional");
		sql.addFrom("processo_sinistro","ps");
		sql.addFrom("tipo_seguro","ts");
		sql.addFrom("pessoa","beneficiario");
		sql.addFrom("pessoa","favorecido");
		sql.addFrom("pessoa","pessoa_devedor");
		sql.addFrom("cliente","cliente_devedor");
		
				//alteração 666
				sql.addFrom("VALOR_DOMINIO","VD2");
				sql.addFrom("DOMINIO","D2");
				
				//alteração 666
				sql.addFrom("VALOR_DOMINIO","VD3");
				sql.addFrom("DOMINIO","D3");
				
				//alteração 666
				sql.addFrom("VALOR_DOMINIO","VD4");
				sql.addFrom("DOMINIO","D4");
				
				//alteração 666
				sql.addFrom("VALOR_DOMINIO","VD5");
				sql.addFrom("DOMINIO","D5");
				
		sql.addJoin("rim.ID_FILIAL", "f_rim.ID_FILIAL");
		sql.addJoin("f_rim.ID_FILIAL", "rf.ID_FILIAL(+)");
		sql.addJoin("rf.ID_REGIONAL", "regional. ID_REGIONAL(+)");
		sql.addJoin("rim.ID_PROCESSO_SINISTRO", "ps.ID_PROCESSO_SINISTRO(+)");
		sql.addJoin("ps.ID_TIPO_SEGURO", "ts.ID_TIPO_SEGURO(+)");
				sql.addJoin("rim.ID_BENEFICIARIO", "beneficiario.ID_PESSOA(+)");
				sql.addJoin("rim.ID_FAVORECIDO", "favorecido.ID_PESSOA(+)");
		sql.addJoin("rim.ID_MOEDA", "mrim.ID_MOEDA(+)");
				sql.addJoin("rim.ID_MOEDA", "mrim.ID_MOEDA(+)");
				
				//alteração 666
				sql.addJoin("VD2.ID_DOMINIO", "D2.ID_DOMINIO");
				sql.addJoin("VD3.ID_DOMINIO", "D3.ID_DOMINIO");
				sql.addJoin("VD4.ID_DOMINIO", "D4.ID_DOMINIO");
				sql.addJoin("VD5.ID_DOMINIO", "D5.ID_DOMINIO");
				
				//alteração 666
				sql.addJoin("VD2.VL_VALOR_DOMINIO","rim.TP_INDENIZACAO");
				sql.addJoin("VD3.VL_VALOR_DOMINIO","rim.TP_STATUS_INDENIZACAO");
				sql.addJoin("VD4.VL_VALOR_DOMINIO","servico.TP_MODAL");
				sql.addJoin("VD5.VL_VALOR_DOMINIO","servico.TP_ABRANGENCIA");
				
				
		/* Docto Servico***************************************************************/
		sql.addJoin("dsi.ID_MOEDA", "moeda_dsi.ID_MOEDA(+)");
        sql.addJoin("ds.ID_MOEDA", "mds.ID_MOEDA(+)");
        sql.addJoin("dsi.ID_RECIBO_INDENIZACAO", "rim.ID_RECIBO_INDENIZACAO");
        sql.addJoin("dsi.ID_DOCTO_SERVICO", "ds.ID_DOCTO_SERVICO");
        sql.addJoin("ds.ID_FILIAL_ORIGEM", "fo_ds.ID_FILIAL");
        sql.addJoin("ds.ID_SERVICO", "servico.ID_SERVICO(+)");
        sql.addJoin("ds.ID_CLIENTE_REMETENTE", "cr.ID_CLIENTE(+)");
        sql.addJoin("cr.ID_CLIENTE", "pr.ID_PESSOA(+)");
        sql.addJoin("ds.ID_CLIENTE_DESTINATARIO", "cd.ID_CLIENTE(+)");
        sql.addJoin("cd.ID_CLIENTE", "pd.ID_PESSOA(+)");
        sql.addJoin("ds.ID_DOCTO_SERVICO", "nc.ID_DOCTO_SERVICO(+)");
        sql.addJoin("nc.ID_FILIAL", "fnc.ID_FILIAL(+)");
		        sql.addJoin("dsi.ID_DOCTO_SERVICO_INDENIZACAO", "rin.ID_DOCTO_SERVICO_INDENIZACAO(+)");
		        sql.addJoin("rin.ID_NOTA_FISCAL_CONHECIMENTO", "nfc.ID_NOTA_FISCAL_CONHECIMENTO(+)");
		        sql.addJoin("rim.ID_LOTE_JDE_RIM", "jde.ID_LOTE_JDE_RIM(+)");
		
		sql.addJoin("ds.ID_DOCTO_SERVICO", "ddsf.ID_DOCTO_SERVICO(+)");
		sql.addJoin("ddsf.id_cliente", "cliente_devedor.id_cliente");
		sql.addJoin("cliente_devedor.id_cliente", "pessoa_devedor.id_pessoa");
		       
		/* *************************************************************************** */
				
		sql.addOrderBy("f_rim.SG_FILIAL");
		sql.addOrderBy("rim.NR_RECIBO_INDENIZACAO");

		if(null == tfm.getLong("nrLoteJdeRim")){
					filterReport(tfm, sql);   
		}else{
			sql.addCriteria("rim.ID_LOTE_JDE_RIM","=",tfm.getLong("nrLoteJdeRim"));
		}
		
				sql.addCriteria("D2.NM_DOMINIO", "=", "DM_TIPO_INDENIZACAO");
				sql.addCriteria("D3.NM_DOMINIO", "=", "DM_STATUS_INDENIZACAO");
				sql.addCriteria("D4.NM_DOMINIO", "=", "DM_MODAL");
				sql.addCriteria("D5.NM_DOMINIO", "=", "DM_ABRANGENCIA");
				
				String dataFormatada = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				
				sql.addCustomCriteria("rf.dt_vigencia_inicial <=   to_date('" + dataFormatada + "', 'dd/mm/yyyy') ");
				sql.addCustomCriteria("rf.dt_vigencia_final >= to_date('" + dataFormatada + "', 'dd/mm/yyyy') ");
				
		return sql;
	}

	private void filterReport(TypedFlatMap tfm, SqlTemplate sql) {
		
		//adicionando as criticas da vigencia da filial
		
		   
		YearMonthDay dtInicial = tfm.getYearMonthDay("dtInicial");
		sql.addCustomCriteria("to_date(rim.DT_EMISSAO)   >= to_date('"+dtInicial+"', 'rrrr-mm-dd')");
		sql.addFilterSummary("periodoInicial", dtInicial);
		
		YearMonthDay dtFinal =  tfm.getYearMonthDay("dtFinal");
		sql.addCustomCriteria("to_date(rim.DT_EMISSAO)  <= to_date('"+dtFinal+"', 'rrrr-mm-dd')");
		sql.addFilterSummary("periodoFinal", dtFinal);
		
		// tpIndenizacao
		DomainValue tpIndenizacao = tfm.getDomainValue("tpIndenizacao");
		if(!"".equals( tpIndenizacao.getValue() )) {
			sql.addCriteria("rim.TP_INDENIZACAO","=", tpIndenizacao.getValue());
			sql.addFilterSummary("tipoIndenizacao", tfm.getString("tpIndenizacaoHidden"));
		}
		
		// Motivo
		Long idMotivo = tfm.getLong("motivoAberturaNc.idMotivoAberturaNc");
		if(idMotivo!=null) {
			String str = tfm.getString("dsMotivoAberturaHidden");
			sql.addFilterSummary("motivoNaoConformidade", str);
			sql.addFrom("ocorrencia_nao_conformidade", "onc");	
	        sql.addJoin("nc.ID_NAO_CONFORMIDADE","onc.ID_NAO_CONFORMIDADE");
	        sql.addCriteria("onc.ID_MOTIVO_ABERTURA_NC","=", idMotivo);
		}		
		
		// TipoSeguro 
		Long idTipoSeguro = tfm.getLong("tipoSeguro.idTipoSeguro");
		if(idTipoSeguro!=null) {
			sql.addCriteria("ts.ID_TIPO_SEGURO","=", idTipoSeguro);
			String str = tfm.getString("sgTipoSeguroHidden");
			sql.addFilterSummary("tipoSeguro", str);
		}		
		
		// TipoSinistro 
		Long idTipoSinistro = tfm.getLong("tipoSinistro.idTipo");
		if(idTipoSinistro!=null) {
			sql.addFrom("tipo_sinistro");
			sql.addJoin("ps.id_tipo_sinistro","tipo_sinistro.id_tipo_sinistro");
			sql.addCriteria("tipo_sinistro.id_tipo_sinistro","=", idTipoSinistro);
			String str = tfm.getString("dsTipoSinistroHidden");
			sql.addFilterSummary("tipoSinistro", str);
		}		

		// moeda
		Long idMoeda = tfm.getLong("moeda.idMoeda");
		if(idMoeda!=null) {
			sql.addCriteria("rim.id_moeda","=", idMoeda);
			String str = tfm.getString("moedaHidden");
			sql.addFilterSummary("moeda", str);
		}

		// vlInicial
		BigDecimal  vlInicial = tfm.getBigDecimal("vlInicial");
		if(vlInicial!=null) {
			sql.addCriteria("rim.VL_INDENIZACAO",">=", vlInicial);
			sql.addFilterSummary("valorInicial", FormatUtils.formatDecimal("###,###,##0.00", vlInicial));
		}
		// vlFinal
		BigDecimal vlFinal = tfm.getBigDecimal("vlFinal");
		if(vlInicial!=null) {
			sql.addCriteria("rim.VL_INDENIZACAO","<=", vlFinal);
			sql.addFilterSummary("valorFinal", FormatUtils.formatDecimal("###,###,##0.00", vlFinal));
		}		
		
		// Filial
		Long idFilial = tfm.getLong("filial.idFilial");
		if (idFilial!=null) {
			sql.addCriteria("rim.ID_FILIAL","=", idFilial);
			String str = tfm.getString("filial.sgFilial");
			sql.addFilterSummary("filial", str);
		}

		// Filial debitada
		Long idFDebitada = tfm.getLong("filialDebitada.idFilial");
		if (idFDebitada!=null) {
	    	sql.addFrom("filial_debitada", "fd");
	    	sql.addJoin("fd.ID_DOCTO_SERVICO_INDENIZACAO", "dsi.ID_DOCTO_SERVICO_INDENIZACAO");
	    	sql.addJoin("fd.ID_RECIBO_INDENIZACAO", "rim.ID_RECIBO_INDENIZACAO");
			sql.addCriteria("fd.ID_FILIAL","=", idFDebitada);
			
			String str = tfm.getString("filialDebitada.sgFilial");
			sql.addFilterSummary("filialDebitada", str);
		}		
		
		// Regional
		Long idRegionalFilial = tfm.getLong("regional.idRegionalFilial");
		if(idRegionalFilial!=null) {
			sql.addCriteria("regional.ID_REGIONAL","=", idRegionalFilial);
			String str = tfm.getString("sgAndDsRegionalHidden");
			sql.addFilterSummary("regional", str);
		}
		
		
		
		

		// Modal
		DomainValue tpModal = tfm.getDomainValue("tpModal");
		if(!"".equals( tpModal.getValue() ) ) {
			sql.addCriteria("servico.TP_MODAL","=", tpModal.getValue());
			sql.addFilterSummary("modal", tfm.getString("tpModalHidden"));
		}
		
		// Abrangencia
		DomainValue tpAbrangencia = tfm.getDomainValue("tpAbrangencia");
		if(!"".equals( tpAbrangencia.getValue() )) {
			sql.addCriteria("servico.TP_ABRANGENCIA","=", tpAbrangencia.getValue());
			sql.addFilterSummary("abrangencia", tfm.getString("tpAbrangenciaHidden"));
		}

		// Status
		DomainValue tpStatusIndenizacao = tfm.getDomainValue("tpStatusIndenizacao");
		if(!"".equals( tpStatusIndenizacao.getValue() ) ) {
			sql.addCriteria("rim.TP_STATUS_INDENIZACAO","=", tpStatusIndenizacao.getValue());
			sql.addFilterSummary("status", tfm.getString("tpStatusIndenizacaoHidden"));
		}

		// remetente
		Long idRemetente = tfm.getLong("remetente.idCliente");
		if(idRemetente!=null) {
			sql.addCriteria("cr.id_cliente","=", idRemetente);
			String str = tfm.getString("remetente.pessoa.nmPessoa");
			sql.addFilterSummary("remetente", str);
		}
		
		// destinatario
		Long idDestinatario = tfm.getLong("destinatario.idCliente");
		if(idDestinatario!=null) {
			sql.addCriteria("cd.id_cliente","=", idDestinatario);
			String str = tfm.getString("destinatario.pessoa.nmPessoa");
			sql.addFilterSummary("destinatario", str);
		}

		// beneficiario
		Long idBeneficiario = tfm.getLong("beneficiario.idPessoa");
		if(idBeneficiario!=null) {
			sql.addCriteria("beneficiario.id_pessoa","=", idBeneficiario);
			String str = tfm.getString("beneficiario.nmPessoa");
			sql.addFilterSummary("beneficiario", str);
		}
		
		// favorecido
		Long idFavorecido = tfm.getLong("favorecido.idPessoa");
		if(idFavorecido!=null) {
			sql.addCriteria("favorecido.id_pessoa","=", idFavorecido);
			String str = tfm.getString("favorecido.nmPessoa");
			sql.addFilterSummary("favorecido", str);
		}
		
		
		if(tfm.getLong("devedor.idPessoa") != null){
			sql.addCustomCriteria("EXISTS (SELECT 1 FROM DOCTO_SERVICO_INDENIZACAO DSI, DEVEDOR_DOC_SERV_FAT DEV WHERE DSI.ID_RECIBO_INDENIZACAO = rim.ID_RECIBO_INDENIZACAO " +
					"AND DSI.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO AND DEV.ID_CLIENTE ="+ tfm.getLong("devedor.idPessoa") +" )");
			sql.addFilterSummary("devedor", tfm.getString("devedor.nmPessoa"));
	}
	
	}
	
    public JRDataSource executeMotivoAbertura(Long idDoctoRNC) {
        SqlTemplate sql = createSqlTemplate();
        
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("manc.DS_MOTIVO_ABERTURA_I"), 
        				  "DS_MOTIVO_ABERTURA");
        sql.addFrom("nao_conformidade", "nc");
        sql.addFrom("ocorrencia_nao_conformidade", "onc");
        sql.addFrom("motivo_abertura_nc", "manc");
        sql.addFrom("docto_servico_indenizacao", "dsi");
        
        sql.addCriteria("dsi.id_docto_servico_indenizacao","=",idDoctoRNC);
        sql.addJoin("nc.ID_NAO_CONFORMIDADE","onc.ID_NAO_CONFORMIDADE(+)");
        sql.addJoin("onc.ID_MOTIVO_ABERTURA_NC","manc.ID_MOTIVO_ABERTURA_NC(+)");
        sql.addJoin("dsi.id_ocorrencia_nao_conformidade","onc.id_ocorrencia_nao_conformidade");
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }

    public JRDataSource executeFiliaisDebitadas(Long idRim, Long idDoctoServicoIndenizacao ) {
        SqlTemplate sql = createSqlTemplate();
        
        sql.addProjection("f.SG_FILIAL","SG_FILIAL_DEBITADA");
        
        sql.addProjection("fd.PC_DEBITADO","PC_DEBITADO");
    	sql.addFrom("filial_debitada", "fd");
    	sql.addFrom("filial", "f");

    	sql.addJoin("fd.ID_FILIAL", "f.ID_FILIAL(+)");
    	sql.addCriteria("fd.ID_RECIBO_INDENIZACAO","=",idRim);
    	sql.addOrderBy("f.SG_FILIAL");
    	
     return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    @SuppressWarnings("unchecked")
	public String executeFiliaisDebitadasExcel(Long idReciboIndenizacao){
    	StringBuilder filiaisDebitadas = new StringBuilder();
    	
    	List<FilialDebitada> listaFiliais = filialDebitadaService.findByIdReciboIndenizacao(idReciboIndenizacao);
    	for (Iterator<FilialDebitada> iterator = listaFiliais.iterator(); iterator.hasNext();) {
			FilialDebitada filialDebitada = iterator.next();
			
			filiaisDebitadas.append(filialDebitada.getFilial().getSgFilial());
			filiaisDebitadas.append(" ");
			filiaisDebitadas.append(FormatUtils.formatDecimal("#,##0.00", filialDebitada.getPcDebitado(), true));
			
			if(iterator.hasNext()){
				filiaisDebitadas.append(", ");
			}
		}
    	
    	return filiaisDebitadas.toString();
    }
    
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_INDENIZACAO","DM_TIPO_INDENIZACAO");
		config.configDomainField("MODAL","DM_MODAL");
		config.configDomainField("ABRANGENCIA","DM_ABRANGENCIA");
		config.configDomainField("TP_STATUS_INDENIZACAO","DM_STATUS_INDENIZACAO");
		config.configDomainField("TP_DOCUMENTO_SERVICO","DM_TIPO_DOCUMENTO_SERVICO");
	}
	
	public BigDecimal executeRetornaValorConvertido(Long idFilial, Long idMoedaOrigem, BigDecimal valor) {
		BigDecimal retorno = null;
		
		Long idPaisDestino = SessionUtils.getPaisSessao().getIdPais();
		Long idMoedaDestino = SessionUtils.getMoedaSessao().getIdMoeda();
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		EnderecoPessoa enderecoPessoaPadrao = getEnderecoPessoaService().findEnderecoPessoaPadrao(idFilial);
		Long idPaisOrigem = enderecoPessoaPadrao.getMunicipio().getUnidadeFederativa().getPais().getIdPais();
		
		retorno = getConversaoMoedaService().findConversaoMoeda(idPaisOrigem, idMoedaOrigem.longValue(), idPaisDestino, idMoedaDestino, dataAtual, valor);

		return retorno;
	}
	
    public String calculaPercentualIndenizado(BigDecimal a, BigDecimal b) {
    	String retorno = "";
    	
    	if (a!=null && b!=null) {
    		BigDecimal resultado = BigDecimalUtils.divide(a.multiply(new BigDecimal(100)), b);
    		retorno = FormatUtils.formatDecimal("#,##0.00", resultado, true);
    	}
    	
    	return retorno;
    }

	public void setFilialDebitadaService(FilialDebitadaService filialDebitadaService) {
		this.filialDebitadaService = filialDebitadaService;
	}


}