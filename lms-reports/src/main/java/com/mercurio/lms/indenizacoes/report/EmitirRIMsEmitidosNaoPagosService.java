package com.mercurio.lms.indenizacoes.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

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
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração da df Relatório de RIMs emitidos e não pagos
 * Especificação técnica 21.04.01.05
 * @author Rodrigo Antunes
 * 
 * @spring.bean id="lms.indenizacoes.emitirRIMsEmitidosNaoPagosService"
 * @spring.property name="reportName" value="com/mercurio/lms/indenizacoes/report/emitirRIMsEmitidosNaoPagos.jasper"
 */
public class EmitirRIMsEmitidosNaoPagosService extends ReportServiceSupport {

	private ConversaoMoedaService conversaoMoedaService;
	private EnderecoPessoaService enderecoPessoaService;
	
	private Long idMotivoAberturaNc;
	
	
	private Long getIdMotivoAberturaNc() {
		return idMotivoAberturaNc;
	}

	private void setIdMotivoAberturaNc(Long idMotivoAberturaNc) {
		this.idMotivoAberturaNc = idMotivoAberturaNc;
	}

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
        
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        jr.setParameters(parametersReport);
        return jr; 
	}
	
	/**
	 * consulta principal 
	 * @param tfm
	 * @return
	 */
	public SqlTemplate createMainSql(TypedFlatMap tfm) {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("regional.SG_REGIONAL","SG_REGIONAL");
		sql.addProjection("f_rim.ID_FILIAL","ID_FILIAL_RIM");
		sql.addProjection("f_rim.SG_FILIAL","SG_FILIAL_RIM");
		sql.addProjection("rim.ID_RECIBO_INDENIZACAO","ID_RECIBO_INDENIZACAO");
		sql.addProjection("rim.NR_RECIBO_INDENIZACAO","NR_RECIBO_INDENIZACAO");
		sql.addProjection("rim.DT_EMISSAO","DT_EMISSAO");
		sql.addProjection("rim.TP_INDENIZACAO","TP_INDENIZACAO");
		sql.addProjection("ts.SG_TIPO","SG_TIPO_SEGURO");
		sql.addProjection("ps.NR_PROCESSO_SINISTRO","NR_PROCESSO_SINISTRO");
		sql.addProjection("beneficiario.NM_PESSOA","NM_BENEFICIARIO");
		sql.addProjection("favorecido.NM_PESSOA","NM_FAVORECIDO");
		sql.addProjection("mrim.DS_SIMBOLO","DS_SIMBOLO_RIM");
		sql.addProjection("mrim.SG_MOEDA","SG_MOEDA_RIM");
		sql.addProjection("mrim.ID_MOEDA","ID_MOEDA_RIM");
		sql.addProjection("rim.VL_INDENIZACAO","VL_INDENIZACAO");
		sql.addProjection("FLOOR(current_date - rim.dt_geracao)","DIAS_EM_ABERTO");
		sql.addProjection("mrim.DS_SIMBOLO","DS_SIMBOLO");
		sql.addProjection("mrim.SG_MOEDA","SG_MOEDA");
// docto servico
        sql.addProjection("ds.ID_DOCTO_SERVICO","ID_DOCTO_SERVICO");
        sql.addProjection("fo_ds.SG_FILIAL","SG_FILIAL_DOCTO");
        sql.addProjection("ds.TP_DOCUMENTO_SERVICO","TP_DOCTO_SERVICO");
        sql.addProjection("ds.NR_DOCTO_SERVICO","NR_DOCTO_SERVICO");
        sql.addProjection("servico.TP_MODAL","MODAL");
        sql.addProjection("servico.TP_ABRANGENCIA","ABRANGENCIA");
        sql.addProjection("pr.NM_PESSOA","REMETENTE");
        sql.addProjection("pd.NM_PESSOA","DESTINATARIO");
        sql.addProjection("dsi.VL_INDENIZADO","VL_INDENIZADO_DOCUMENTO");
        sql.addProjection("fnc.SG_FILIAL","FILIAL_RNC");
        sql.addProjection("nc.NR_NAO_CONFORMIDADE","NR_NAO_CONFORMIDADE");
        sql.addProjection("nc.ID_NAO_CONFORMIDADE","ID_NAO_CONFORMIDADE"); 
        sql.addProjection("dsi.ID_DOCTO_SERVICO_INDENIZACAO","ID_DOCTO_SERVICO_INDENIZACAO");

		sql.addProjection("mds.DS_SIMBOLO","DS_SIMBOLO_DS");
		sql.addProjection("mds.SG_MOEDA","SG_MOEDA_DS");
		sql.addProjection("mds.ID_MOEDA","ID_MOEDA_DS");        
        
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
		
		sql.addJoin("rim.ID_FILIAL", "f_rim.ID_FILIAL");
		sql.addJoin("f_rim.ID_FILIAL", "rf.ID_FILIAL(+)");
		sql.addJoin("rf.ID_REGIONAL", "regional. ID_REGIONAL(+)");
		sql.addJoin("rim.ID_PROCESSO_SINISTRO", "ps.ID_PROCESSO_SINISTRO(+)");
		sql.addJoin("ps.ID_TIPO_SEGURO", "ts.ID_TIPO_SEGURO(+)");
		sql.addJoin("rim.ID_BENEFICIARIO", "beneficiario.ID_PESSOA");
		sql.addJoin("rim.ID_FAVORECIDO", "favorecido.ID_PESSOA");
		sql.addJoin("rim.ID_MOEDA", "mrim.ID_MOEDA(+)");

/* Docto Servico***************************************************************/		
        sql.addJoin("ds.ID_MOEDA", "mds.ID_MOEDA(+)");
        sql.addJoin("dsi.ID_RECIBO_INDENIZACAO(+)", "rim.ID_RECIBO_INDENIZACAO");
        sql.addJoin("dsi.ID_DOCTO_SERVICO", "ds.ID_DOCTO_SERVICO(+)");
        sql.addJoin("ds.ID_FILIAL_ORIGEM", "fo_ds.ID_FILIAL(+)");
        sql.addJoin("ds.ID_SERVICO", "servico.ID_SERVICO(+)");
        sql.addJoin("ds.ID_CLIENTE_REMETENTE", "cr.ID_CLIENTE(+)");
        sql.addJoin("cr.ID_CLIENTE", "pr.ID_PESSOA(+)");
        sql.addJoin("ds.ID_CLIENTE_DESTINATARIO", "cd.ID_CLIENTE(+)");
        sql.addJoin("cd.ID_CLIENTE", "pd.ID_PESSOA(+)");
        sql.addJoin("ds.ID_DOCTO_SERVICO", "nc.ID_DOCTO_SERVICO(+)");
        sql.addJoin("nc.ID_FILIAL", "fnc.ID_FILIAL(+)");
/* ****************************************************************************/		
		
		sql.addOrderBy("f_rim.SG_FILIAL");
		sql.addOrderBy("rim.NR_RECIBO_INDENIZACAO");
		sql.addCriteria("rim.TP_STATUS_INDENIZACAO", "!=", "P");
		sql.addCriteria("rim.TP_STATUS_INDENIZACAO", "!=", "C");

		YearMonthDay dtInicial = tfm.getYearMonthDay("dtInicial");
		sql.addCriteria("rim.DT_GERACAO",">=",dtInicial);
		sql.addFilterSummary("periodoInicial", dtInicial);
		
		YearMonthDay dtFinal =  tfm.getYearMonthDay("dtFinal");
		sql.addCriteria("rim.DT_GERACAO","<=",dtFinal);
		sql.addFilterSummary("periodoFinal", dtFinal);
		

		//adicionando as criticas da vigencia da filial
		sql.addCriteria("rf.DT_VIGENCIA_INICIAL(+)", "<=", JTDateTimeUtils.getDataAtual());
		sql.addCriteria("rf.DT_VIGENCIA_FINAL(+)", ">=", JTDateTimeUtils.getDataAtual());

		
		
		// Filial
		Long idFilial = tfm.getLong("filial.idFilial");
		if (idFilial!=null) {
			sql.addCriteria("rim.ID_FILIAL","=", idFilial);
			String str = tfm.getString("filial.sgFilial");
			sql.addFilterSummary("filial", str);
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
		
		// tpIndenizacao
		DomainValue tpIndenizacao = tfm.getDomainValue("tpIndenizacao");
		if(!"".equals( tpIndenizacao.getValue() )) {
			sql.addCriteria("rim.TP_INDENIZACAO","=", tpIndenizacao.getValue());
			sql.addFilterSummary("tipoIndenizacao", tfm.getString("tpIndenizacaoHidden"));
		}
		// Motivo
		Long idMotivo = tfm.getLong("motivoAberturaNc.idMotivoAberturaNc");
		setIdMotivoAberturaNc(idMotivo);
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

		String strProcessoSinistro = "";
		String numeroProcessoSinistro1 = tfm.getString("numeroProcessoSinistro1");
		if(numeroProcessoSinistro1!=null) {
			sql.addCriteria("ps.NR_PROCESSO_SINISTRO","=", numeroProcessoSinistro1);
			strProcessoSinistro = tfm.getString("numeroProcessoSinistro1");
				}		
		
		return sql;
	}
	
    public JRDataSource executeMotivoAbertura(Long idRNC) {
        SqlTemplate sql = createSqlTemplate();
        
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("manc.DS_MOTIVO_ABERTURA_I"), "DS_MOTIVO_ABERTURA");
        sql.addFrom("nao_conformidade", "nc");
        sql.addFrom("ocorrencia_nao_conformidade", "onc");
        sql.addFrom("motivo_abertura_nc", "manc");
        
        sql.addCriteria("nc.ID_NAO_CONFORMIDADE","=",idRNC);
        sql.addJoin("nc.ID_NAO_CONFORMIDADE","onc.ID_NAO_CONFORMIDADE(+)");
        sql.addJoin("onc.ID_MOTIVO_ABERTURA_NC","manc.ID_MOTIVO_ABERTURA_NC(+)");
        
        if( getIdMotivoAberturaNc()!=null ) {
        	sql.addCriteria("manc.ID_MOTIVO_ABERTURA_NC","=", getIdMotivoAberturaNc());
        }
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }

    public JRDataSource executeFiliaisDebitadas(Long idRim, Long idDoctoServicoIndenizacao ) {
        SqlTemplate sql = createSqlTemplate();
        
        sql.addProjection("f.SG_FILIAL","SG_FILIAL_DEBITADA");
        sql.addProjection("fd.PC_DEBITADO","PC_DEBITADO");
    	sql.addFrom("filial_debitada", "fd");
    	sql.addFrom("filial", "f");
    	sql.addFrom("docto_servico_indenizacao", "dsi");
    	sql.addFrom("recibo_indenizacao", "rim");

    	sql.addJoin("fd.ID_DOCTO_SERVICO_INDENIZACAO", "dsi.ID_DOCTO_SERVICO_INDENIZACAO(+)");
    	sql.addJoin("fd.ID_RECIBO_INDENIZACAO", "rim.ID_RECIBO_INDENIZACAO(+)");
    	sql.addJoin("fd.ID_FILIAL", "f.ID_FILIAL(+)");
    	sql.addCriteria("rim.ID_RECIBO_INDENIZACAO","=",idRim);
    	sql.addCriteria("dsi.ID_DOCTO_SERVICO_INDENIZACAO","=",idDoctoServicoIndenizacao);
    	sql.addOrderBy("f.SG_FILIAL");
     return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_INDENIZACAO","DM_TIPO_INDENIZACAO");
		config.configDomainField("MODAL","DM_MODAL");
		config.configDomainField("ABRANGENCIA","DM_ABRANGENCIA");
		config.configDomainField("TP_DOCTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
	}
	
	public BigDecimal executeRetornaValorConvertido(Long idFilial, Long idMoedaOrigem, BigDecimal valor) {
		BigDecimal retorno = null;
		
		Long idPaisDestino = SessionUtils.getPaisSessao().getIdPais();
		Long idMoedaDestino = SessionUtils.getMoedaSessao().getIdMoeda();
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		EnderecoPessoa enderecoPessoaPadrao = getEnderecoPessoaService().findEnderecoPessoaPadrao(idFilial);
		Long idPaisOrigem = enderecoPessoaPadrao.getMunicipio().getUnidadeFederativa().getPais().getIdPais();
		
		retorno = getConversaoMoedaService().findConversaoMoeda(idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino, dataAtual, valor);

		return retorno;
	}
}