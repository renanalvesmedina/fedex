package com.mercurio.lms.indenizacoes.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.beanutils.BeanComparator;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.indenizacoes.model.FilialDebitada;
import com.mercurio.lms.indenizacoes.model.service.FilialDebitadaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.MoedaUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * Classe responsável pela geração da df Emitir RECIBO RIM.
 * Especificação técnica 21.01.01.02
 * @author Rodrigo Antunes
 * 
 * @spring.bean id="lms.indenizacoes.emitirReciboRIMService"
 * @spring.property name="reportName" value="com/mercurio/lms/indenizacoes/report/emitirReciboRIM.jasper"
 */
public class EmitirReciboRIMService extends ReportServiceSupport {
	
	private MoedaService moedaService; 
	private FilialDebitadaService filialDebitadaService;

	public MoedaService getMoedaService() {
		return moedaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	@SuppressWarnings("unchecked")
	public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap tfm = (TypedFlatMap)parameters; 
        SqlTemplate sql = getSqlTemplate(tfm);
        
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("UNIDADE", SessionUtils.getFilialSessao().getPessoa().getNmFantasia());
        parametersReport.put("CNPJ", FormatUtils.formatIdentificacao(SessionUtils.getFilialSessao().getPessoa()));
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        jr.setParameters(parametersReport);
        return jr; 
	}
	
    private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception {
        
        SqlTemplate sqlTemplate = createSqlTemplate();
        // Monta o SELECT
        sqlTemplate.addProjection("rim.ID_RECIBO_INDENIZACAO","ID_RECIBO_INDENIZACAO");
        sqlTemplate.addProjection("rim.NR_NOTA_FISCAL_DEBITO_CLIENTE","NR_NOTA_FISCAL_DEBITO_CLIENTE");
        sqlTemplate.addProjection("f_rim.SG_FILIAL","SG_FILIAL_RIM");
        sqlTemplate.addProjection("rim.NR_RECIBO_INDENIZACAO","NR_RECIBO_INDENIZACAO");
        sqlTemplate.addProjection("rim.DT_EMISSAO","DT_EMISSAO");
        sqlTemplate.addProjection("rim.TP_INDENIZACAO","TP_INDENIZACAO_VALUE");
        sqlTemplate.addProjection("rim.TP_INDENIZACAO","TP_INDENIZACAO");
        sqlTemplate.addProjection("rim.TP_BENEFICIARIO_INDENIZACAO","TP_BENEFICIARIO_INDENIZACAO");
        sqlTemplate.addProjection("p_beneficiario.NM_PESSOA","NOME_BENEFICIARIO");
        sqlTemplate.addProjection("p_beneficiario.TP_IDENTIFICACAO","TP_IDENTIFICACAO");
        sqlTemplate.addProjection("p_beneficiario.NR_IDENTIFICACAO","NR_IDENTIFICACAO");
        sqlTemplate.addProjection(PropertyVarcharI18nProjection.createProjection("tl.ds_tipo_logradouro_i"),"DS_TIPO_LOGRADOURO");
        sqlTemplate.addProjection("ep.DS_ENDERECO","DS_ENDERECO");
        sqlTemplate.addProjection("ep.NR_ENDERECO","NR_ENDERECO");
        sqlTemplate.addProjection("ep.DS_BAIRRO","DS_BAIRRO");
        sqlTemplate.addProjection("ep.NR_CEP","NR_CEP");
        sqlTemplate.addProjection("m.NM_MUNICIPIO","NM_MUNICIPIO");
        sqlTemplate.addProjection("uf.SG_UNIDADE_FEDERATIVA","SG_UNIDADE_FEDERATIVA");
        sqlTemplate.addProjection("banco.NR_BANCO","NR_BANCO");
        sqlTemplate.addProjection("ab.NR_AGENCIA_BANCARIA","NR_AGENCIA_BANCARIA");
        sqlTemplate.addProjection("ab.NR_DIGITO","NR_DIGITO_AGENCIA_BANCARIA");
        sqlTemplate.addProjection("rim.NR_CONTA_CORRENTE","NR_CONTA_CORRENTE");
        sqlTemplate.addProjection("rim.NR_DIGITO_CONTA_CORRENTE","NR_DIGITO_CONTA_CORRENTE");
        sqlTemplate.addProjection("moeda.ID_MOEDA","ID_MOEDA");
        sqlTemplate.addProjection(PropertyVarcharI18nProjection.createProjection("moeda.DS_MOEDA_I"),"DS_MOEDA");
        sqlTemplate.addProjection("moeda.DS_SIMBOLO","DS_SIMBOLO");
        sqlTemplate.addProjection("moeda.SG_MOEDA","SG_MOEDA");
        sqlTemplate.addProjection("rim.VL_INDENIZACAO","VL_INDENIZACAO");
        sqlTemplate.addProjection("rim.TP_FORMA_PAGAMENTO","TP_FORMA_PAGAMENTO");
        sqlTemplate.addProjection("rim.OB_RECIBO_INDENIZACAO","OB_RECIBO_INDENIZACAO");
        sqlTemplate.addProjection("ps.NR_PROCESSO_SINISTRO","NR_PROCESSO_SINISTRO");
        sqlTemplate.addProjection("rim.NR_NOTA_FISCAL_DEBITO_CLIENTE","NR_NOTA_FISCAL_DEBITO_CLIENTE");

        // Monta o FROM
        sqlTemplate.addFrom("recibo_indenizacao","rim");
        sqlTemplate.addFrom("pessoa","p_beneficiario");
        sqlTemplate.addFrom("filial","f_rim");
        sqlTemplate.addFrom("endereco_pessoa","ep");
        sqlTemplate.addFrom("tipo_logradouro","tl");
        sqlTemplate.addFrom("banco");
        sqlTemplate.addFrom("agencia_bancaria","ab");
        sqlTemplate.addFrom("processo_sinistro","ps");
        sqlTemplate.addFrom("municipio","m");
        sqlTemplate.addFrom("unidade_federativa","uf");
        sqlTemplate.addFrom("moeda");
        
        // Monta o WHERE (joins)
        sqlTemplate.addJoin("rim.ID_FILIAL","f_rim.ID_FILIAL");
        sqlTemplate.addJoin("rim.ID_BANCO","banco.ID_BANCO (+)");
        sqlTemplate.addJoin("rim.ID_AGENCIA_BANCARIA","ab.ID_AGENCIA_BANCARIA (+)");
        sqlTemplate.addJoin("rim.ID_PROCESSO_SINISTRO","ps.ID_PROCESSO_SINISTRO (+)");
        sqlTemplate.addJoin("rim.ID_BENEFICIARIO","p_beneficiario.ID_PESSOA");
        sqlTemplate.addJoin("p_beneficiario.id_endereco_pessoa","ep.ID_ENDERECO_PESSOA (+)");
        sqlTemplate.addJoin("ep.ID_TIPO_LOGRADOURO","tl.ID_TIPO_LOGRADOURO (+)");
        sqlTemplate.addJoin("ep.ID_MUNICIPIO","m.ID_MUNICIPIO(+)");
        sqlTemplate.addJoin("m.ID_UNIDADE_FEDERATIVA"," uf.ID_UNIDADE_FEDERATIVA(+)");
        sqlTemplate.addJoin("rim.ID_MOEDA","moeda.ID_MOEDA");

        // adiciona o id como criterio de pesquisa
        Long idReciboIndenizacao = tfm.getLong("reciboIndenizacao.idReciboIndenizacao");
        
        if(idReciboIndenizacao!=null) {
        	sqlTemplate.addCriteria("rim.ID_RECIBO_INDENIZACAO","=",idReciboIndenizacao);
        } else {
        	//lançar exceção dizendo que tem que haver o rim
        	throw new Exception("É necessario informar o idReciboindenizacao");
        }
        
        return sqlTemplate;
    }
    
    /**
     * Sub relatorio para buscas os mda's da indenizacao 
     * @param idReciboIndenizacao
     * @return
     * @throws Exception
     */
    public JRDataSource executeSalvados(Long idReciboIndenizacao) throws Exception {
        SqlTemplate sql = createSqlTemplate();

        // select
        sql.addProjection("filial.SG_FILIAL","SG_FILIAL");
        sql.addProjection("ds.NR_DOCTO_SERVICO","NR_DOCTO_SERVICO");

        //from
        sql.addFrom("filial");
        sql.addFrom("mda");
        sql.addFrom("docto_servico", "ds");
        sql.addFrom("mda_salvado_indenizacao", "msi");
        sql.addFrom("recibo_indenizacao", "rim");
      
        // where
        sql.addJoin("rim.ID_RECIBO_INDENIZACAO", "msi.ID_RECIBO_INDENIZACAO");
        sql.addJoin("msi.ID_MDA", "mda.ID_MDA");
        sql.addJoin("mda.ID_MDA", "ds.ID_DOCTO_SERVICO");
        sql.addJoin("ds.ID_FILIAL_ORIGEM", "filial.ID_FILIAL");
        
        sql.addCriteria("rim.ID_RECIBO_INDENIZACAO","=", idReciboIndenizacao);
        sql.addCustomCriteria("rownum <=1");
        sql.addOrderBy("mda.id_mda");
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    
    public JRDataSource executeDocumentos(Long idReciboIndenizacao) {
    	
    	SqlTemplate sql = createSqlTemplate();

        // select
    	sql.addProjection("ds.ID_DOCTO_SERVICO", "ID_DOCTO_SERVICO");
        sql.addProjection("dsi.ID_DOCTO_SERVICO_INDENIZACAO", "ID_DOCTO_SERVICO_INDENIZACAO");
        sql.addProjection("f_origem.SG_FILIAL","SG_FILIAL_ORIGEM");
        sql.addProjection("ds.NR_DOCTO_SERVICO","NR_DOCTO_SERVICO");
        sql.addProjection("ds.TP_DOCUMENTO_SERVICO","TP_DOCUMENTO_SERVICO");
        sql.addProjection("ds.DH_EMISSAO","DH_EMISSAO");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("produto.DS_PRODUTO_I"),"DS_PRODUTO");
        sql.addProjection("dsi.QT_VOLUMES","QT_VOLUMES");
        sql.addProjection("ds.VL_MERCADORIA","VL_MERCADORIA");
        sql.addProjection("dsi.VL_INDENIZADO","VL_INDENIZADO");

        // Remetente
        sql.addProjection("p_remetente.NM_PESSOA","NM_REMETENTE");
        sql.addProjection("p_remetente.NR_IDENTIFICACAO","NR_IDENTIFICACAO_REM");
        sql.addProjection("p_remetente.TP_IDENTIFICACAO","TP_IDENTIFICACAO_REM");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("tl_rem.ds_tipo_logradouro_i"),"DS_TIPO_LOGRADOURO_REM");
        sql.addProjection("ep_rem.DS_ENDERECO","DS_ENDERECO_REM");
        sql.addProjection("ep_rem.NR_ENDERECO","NR_ENDERECO_REM");
        sql.addProjection("ep_rem.DS_COMPLEMENTO","DS_COMPLEMENTO_REM");
        sql.addProjection("ep_rem.NR_CEP","NR_CEP_REM");
        sql.addProjection("m_rem.NM_MUNICIPIO","NM_MUNICIPIO_REM");
        sql.addProjection("uf_rem.SG_UNIDADE_FEDERATIVA","SG_UNIDADE_FEDERATIVA_REM");
        sql.addProjection("f_rem.SG_FILIAL", "SG_FILIAL_REM");
        
        //Destinatário
        sql.addProjection("p_destinatario.NM_PESSOA","NM_DESTINATARIO");
        sql.addProjection("p_destinatario.NR_IDENTIFICACAO","NR_IDENTIFICACAO_DEST");
        sql.addProjection("p_destinatario.TP_IDENTIFICACAO","TP_IDENTIFICACAO_DEST");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("tl_dest.ds_tipo_logradouro_i"),"DS_TIPO_LOGRADOURO_DEST");
        sql.addProjection("ep_dest.DS_ENDERECO","DS_ENDERECO_DEST");
        sql.addProjection("ep_dest.NR_ENDERECO","NR_ENDERECO_DEST");
        sql.addProjection("ep_dest.DS_COMPLEMENTO","DS_COMPLEMENTO_DEST");
        sql.addProjection("ep_dest.NR_CEP","NR_CEP_DEST");
        sql.addProjection("m_dest.NM_MUNICIPIO","NM_MUNICIPIO_DEST");
        sql.addProjection("uf_dest.SG_UNIDADE_FEDERATIVA","SG_UNIDADE_FEDERATIVA_DEST");
        sql.addProjection("f_dest.SG_FILIAL", "SG_FILIAL_DEST");
        
        //Consignatário
        sql.addProjection("p_consig.NM_PESSOA","NM_CONSIG");
        sql.addProjection("p_consig.NR_IDENTIFICACAO","NR_IDENTIFICACAO_CONSIG");
        sql.addProjection("p_consig.TP_IDENTIFICACAO","TP_IDENTIFICACAO_CONSIG");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("tl_consig.ds_tipo_logradouro_i"),"DS_TIPO_LOGRADOURO_CONSIG");
        sql.addProjection("ep_consig.DS_ENDERECO","DS_ENDERECO_CONSIG");
        sql.addProjection("ep_consig.NR_ENDERECO","NR_ENDERECO_CONSIG");
        sql.addProjection("ep_consig.DS_COMPLEMENTO","DS_COMPLEMENTO_CONSIG");
        sql.addProjection("ep_consig.NR_CEP","NR_CEP__CONSIG");
        sql.addProjection("m_consig.NM_MUNICIPIO","NM_MUNICIPIO_CONSIG");
        sql.addProjection("uf_consig.SG_UNIDADE_FEDERATIVA","SG_UNIDADE_FEDERATIVA_CONSIG");
        sql.addProjection("f_consig.SG_FILIAL", "SG_FILIAL_CONSIG");
        
        sql.addProjection("f_nc.SG_FILIAL","SG_FILIAL_NC");
        sql.addProjection("nc.NR_NAO_CONFORMIDADE","NR_NAO_CONFORMIDADE");
        
        sql.addProjection("moeda_ds.SG_MOEDA","SG_MOEDA_DS");
        sql.addProjection("moeda_ds.DS_SIMBOLO","DS_SIMBOLO_DS");
        
        sql.addProjection("moeda_dsi.SG_MOEDA","SG_MOEDA_DSI");
        sql.addProjection("moeda_dsi.DS_SIMBOLO","DS_SIMBOLO_DSI");

	    sql.addProjection("case\n" +
	    		"when rim.TP_INDENIZACAO='NC' THEN (select MIN(onc.ID_MANIFESTO) from ocorrencia_nao_conformidade onc" +
	    		"									where onc.ID_OCORRENCIA_NAO_CONFORMIDADE = dsi.ID_OCORRENCIA_NAO_CONFORMIDADE)\n" +
	    		"end as ID_MANIFESTO\n");

        //from
        
        sql.addFrom("docto_servico_indenizacao","dsi");
        sql.addFrom("docto_servico", "ds");
        sql.addFrom("recibo_indenizacao", "rim");
        sql.addFrom("filial", "f_origem");
        sql.addFrom("cliente", "remetente");
        sql.addFrom("cliente", "destinatario");
        sql.addFrom("cliente", "consig");
        sql.addFrom("pessoa", "p_remetente");
        sql.addFrom("pessoa", "p_destinatario");
        sql.addFrom("pessoa", "p_consig");
        sql.addFrom("produto");
        
        sql.addFrom("nao_conformidade", "nc");
        sql.addFrom("filial", "f_nc");
        sql.addFrom("moeda", "moeda_ds");
        sql.addFrom("moeda", "moeda_dsi");
        sql.addFrom("filial", "f_rem");
        sql.addFrom("filial", "f_dest");
        sql.addFrom("filial", "f_consig");
        sql.addFrom("devedor_doc_serv", "dds");
 
        sql.addFrom("endereco_pessoa","ep_rem");
        sql.addFrom("tipo_logradouro","tl_rem");
        sql.addFrom("municipio","m_rem");
        sql.addFrom("unidade_federativa","uf_rem");
        
        sql.addFrom("endereco_pessoa","ep_dest");
        sql.addFrom("tipo_logradouro","tl_dest");
        sql.addFrom("municipio","m_dest");
        sql.addFrom("unidade_federativa","uf_dest");
        
        sql.addFrom("endereco_pessoa","ep_consig");
        sql.addFrom("tipo_logradouro","tl_consig");
        sql.addFrom("municipio","m_consig");
        sql.addFrom("unidade_federativa","uf_consig");

        // where
        sql.addJoin("dsi.ID_RECIBO_INDENIZACAO", "rim.ID_RECIBO_INDENIZACAO");
        sql.addJoin("dsi.ID_MOEDA", "moeda_dsi.ID_MOEDA");
        sql.addJoin("dsi.ID_DOCTO_SERVICO", "ds.ID_DOCTO_SERVICO");
        sql.addJoin("ds.ID_FILIAL_ORIGEM", "f_origem.ID_FILIAL");
        sql.addJoin("ds.ID_MOEDA", "moeda_ds.ID_MOEDA");
        sql.addJoin("ds.ID_CLIENTE_REMETENTE", "remetente.ID_CLIENTE(+)");
        sql.addJoin("remetente.ID_CLIENTE", "p_remetente.ID_PESSOA(+)");
        sql.addJoin("ds.ID_CLIENTE_DESTINATARIO", "destinatario.ID_CLIENTE(+)");
        sql.addJoin("destinatario.ID_CLIENTE", "p_destinatario.ID_PESSOA(+)");
        sql.addJoin("dsi.ID_PRODUTO", "produto.ID_PRODUTO(+)");
        sql.addJoin("ds.ID_DOCTO_SERVICO", "nc.ID_DOCTO_SERVICO(+)");
        sql.addJoin("nc.ID_FILIAL", "f_nc.ID_FILIAL(+)");

        sql.addJoin("ds.ID_DOCTO_SERVICO", "dds.ID_DOCTO_SERVICO(+)");
        sql.addJoin("dds.ID_CLIENTE", "consig.ID_CLIENTE");
        sql.addJoin("consig.ID_CLIENTE", "p_consig.ID_PESSOA(+)");
        
        sql.addJoin("p_remetente.id_endereco_pessoa","ep_rem.ID_ENDERECO_PESSOA (+)");
        sql.addJoin("ep_rem.ID_TIPO_LOGRADOURO","tl_rem.ID_TIPO_LOGRADOURO (+)");
        sql.addJoin("ep_rem.ID_MUNICIPIO","m_rem.ID_MUNICIPIO(+)");
        sql.addJoin("m_rem.ID_UNIDADE_FEDERATIVA"," uf_rem.ID_UNIDADE_FEDERATIVA(+)");
        sql.addJoin("remetente.ID_FILIAL_ATENDE_OPERACIONAL", "f_rem.id_filial(+)");
        
        sql.addJoin("p_destinatario.id_endereco_pessoa","ep_dest.ID_ENDERECO_PESSOA (+)");
        sql.addJoin("ep_dest.ID_TIPO_LOGRADOURO","tl_dest.ID_TIPO_LOGRADOURO (+)");
        sql.addJoin("ep_dest.ID_MUNICIPIO","m_dest.ID_MUNICIPIO(+)");
        sql.addJoin("m_dest.ID_UNIDADE_FEDERATIVA"," uf_dest.ID_UNIDADE_FEDERATIVA(+)");
        sql.addJoin("destinatario.ID_FILIAL_ATENDE_OPERACIONAL", "f_dest.id_filial(+)");
        
        sql.addJoin("p_consig.id_endereco_pessoa","ep_consig.ID_ENDERECO_PESSOA (+)");
        sql.addJoin("ep_consig.ID_TIPO_LOGRADOURO","tl_consig.ID_TIPO_LOGRADOURO (+)");
        sql.addJoin("ep_consig.ID_MUNICIPIO","m_consig.ID_MUNICIPIO(+)");
        sql.addJoin("m_consig.ID_UNIDADE_FEDERATIVA"," uf_consig.ID_UNIDADE_FEDERATIVA(+)");
        sql.addJoin("consig.ID_FILIAL_ATENDE_OPERACIONAL", "f_consig.id_filial(+)");
        
    	sql.addCriteria("rim.ID_RECIBO_INDENIZACAO","=", idReciboIndenizacao);
    	
    	/* Para o Recibo em questão (RECIBO_INDENIZACAO), acessar o primeiro registro da tabela DOCTO_SERVICO_INDENIZACAO */
    	sql.addCustomCriteria("rownum <=1");
    	sql.addOrderBy("dsi.id_docto_servico_indenizacao");
    	
    	return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
	public JRDataSource executeManifesto(Long idManifesto) throws Exception {
    
		if (idManifesto != null) {
			SqlTemplate sql = createSqlTemplate();

			// select
			sql.addProjection("f_orig.SG_FILIAL", "SG_FILIAL_ORIG");

			// Número do Manifesto
			sql.addProjection("\ncase\n"
					+ "  when m.TP_MANIFESTO = 'V'\n"
					+ "  	then\n"
					+ "  	case\n"
					+ "  		when m.tp_abrangencia = 'N' then (select mvn.nr_manifesto_origem from manifesto_viagem_nacional mvn where mvn.ID_MANIFESTO_VIAGEM_NACIONAL = m.id_manifesto) \n"
					+ "  		when m.tp_abrangencia = 'I' then (select mi.nr_manifesto_int from manifesto_internacional mi where mi.ID_MANIFESTO_INTERNACIONAL = m.id_manifesto) \n"
					+ "  	end\n "
					+ "  when m.TP_MANIFESTO = 'E' then (select me.nr_manifesto_entrega from manifesto_entrega me where me.ID_MANIFESTO_ENTREGA = m.id_manifesto) \n"
					+ "end as NR_MANIFESTO\n");

			// Qtd Volumes
			sql.addProjection("\ncase\n"
					+ "  when m.TP_MANIFESTO = 'V'\n"
					+ "  	then\n"
					+ "  	case\n"
					+ "  		when m.tp_abrangencia = 'N' then (select sum(ds.qt_volumes) from  manifesto_nacional_cto mnc, docto_servico ds where mnc.ID_MANIFESTO_VIAGEM_NACIONAL = m.id_manifesto AND mnc.ID_CONHECIMENTO = ds.ID_DOCTO_SERVICO)\n"
					+ "  		when m.tp_abrangencia = 'I' then (select sum(ds.qt_volumes) from  manifesto_internac_cto mic, docto_servico ds where mic.ID_MANIFESTO_INTERNACIONAL = m.id_manifesto AND mic.ID_MANIFESTO_INTERNAC_CTO = ds.ID_DOCTO_SERVICO)\n"
					+ "  	end\n "
					+ "  when m.TP_MANIFESTO = 'E' then (select sum(ds.qt_volumes) from manifesto_entrega_documento med, docto_servico ds where med.ID_MANIFESTO_ENTREGA = m.id_manifesto AND med.ID_DOCTO_SERVICO = ds.ID_DOCTO_SERVICO)\n"
					+ "end as QTD_VOLUMES\n");

			// Qtd Documentos
			sql.addProjection("\ncase\n"
					+ "  when m.TP_MANIFESTO = 'V'\n"
					+ "  	then\n"
					+ "  	case\n"
					+ "  		when m.tp_abrangencia = 'N' then (select count(mnc.ID_MANIFESTO_NACIONAL_CTO) from manifesto_nacional_cto mnc where mnc.ID_MANIFESTO_VIAGEM_NACIONAL = m.id_manifesto)\n"
					+ "  		when m.tp_abrangencia = 'I' then (select count(mic.ID_MANIFESTO_INTERNAC_CTO) from manifesto_internac_cto mic where mic.ID_MANIFESTO_INTERNACIONAL = m.id_manifesto)\n"
					+ "  	end\n "
					+ "  when m.TP_MANIFESTO = 'E' then (select count(med.ID_MANIFESTO_ENTREGA_DOCUMENTO) from manifesto_entrega_documento med where med.ID_MANIFESTO_ENTREGA = m.id_manifesto)\n"
					+ "end as QTD_DOCS\n");

			// from
			sql.addFrom("manifesto", "m");
			sql.addFrom("filial", "f_orig");

			// where
			sql.addJoin("m.id_filial_origem", "f_orig.id_filial");
			sql.addCriteria("m.id_manifesto", "=", idManifesto);

			return executeQuery(sql.getSql(), sql.getCriteria())
					.getDataSource();
		} else {
			return new JRBeanCollectionDataSource(new ArrayList());
		}
	}
    
    
    /**
	 * Motivo: DOCTO_SERVICO_INDENIZACAO.ID_OCORRENCIA_NAO_CONFORMIDADE -> OCORRENCIA_NAO_CONFORMIDADE.ID_MOTIVO_ABERTURA_NC ->
	 * MOTIVO_ABERTURA_NC.DS_MOTIVO_ABERTURA
	 * 
     * @param idDoctoServico
     * @return
     * @throws Exception
     */
    public JRDataSource executeMotivosRNC(Long idDoctoServico) throws Exception {
        SqlTemplate sql = createSqlTemplate();

        // select
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("manc.DS_MOTIVO_ABERTURA_I"),"MOTIVOS_RNC");
        //from
        sql.addFrom("docto_servico_indenizacao", "dsi");
        sql.addFrom("ocorrencia_nao_conformidade", "onc");
        sql.addFrom("motivo_abertura_nc", "manc");
        
        sql.addJoin("dsi.id_ocorrencia_nao_conformidade","onc.id_ocorrencia_nao_conformidade");
        sql.addJoin("onc.ID_MOTIVO_ABERTURA_NC","manc.ID_MOTIVO_ABERTURA_NC");
        
        sql.addCriteria("dsi.id_docto_servico_indenizacao","=",idDoctoServico);
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
	 * Notas fiscais: Concatenação das primeiras 10 ocorrências de DOCTO_SERVICO_INDENIZACAO.ID_ DOCTO_SERVICO_INDENIZACAO ->
	 * RECIBO_INDENIZACAO_NF.ID_NOTA_FISCAL_CONHECIMENTO -> NOTA_FISCAL_CONHECIMENTO.NR_NOTA_FISCAL
	 * 
     * @param idDoctoServicoIndenizacao
     * @return
     * @throws Exception
     */
    public JRDataSource executeNFs(Long idDoctoServicoIndenizacao) throws Exception {
        SqlTemplate sql = createSqlTemplate();

        // select
        sql.addProjection("nfc.NR_NOTA_FISCAL","NR_NOTA_FISCAIS");
        //from
        sql.addFrom("recibo_indenizacao_nf", "rinf");
        sql.addFrom("docto_servico_indenizacao", "dsi");
        sql.addFrom("nota_fiscal_conhecimento", "nfc");
        
        // where
        sql.addJoin("dsi.ID_DOCTO_SERVICO_INDENIZACAO", "rinf.ID_DOCTO_SERVICO_INDENIZACAO");
        sql.addJoin("rinf.ID_NOTA_FISCAL_CONHECIMENTO", "nfc.ID_NOTA_FISCAL_CONHECIMENTO");
        sql.addCriteria("dsi.ID_DOCTO_SERVICO_INDENIZACAO","=", idDoctoServicoIndenizacao);
        sql.addCustomCriteria("rownum <= 10");
        sql.addOrderBy("nfc.id_nota_fiscal_conhecimento");
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
	 * Unidade debitada: RECIBO_INDENIZACAO.ID_RECIBO_INDENIZACAO -> Primeiro registro de FILIAL_DEBITADA.ID_FILIAL -> FILIAL.SG_FILIAL.
	 * 
     * @param idDoctoServicoIndenizacao
     * @return
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public String executeUnidadeDebitada(Long idReciboIndenizacao) throws Exception {
		String siglaPrimeiraFilial = null;
		List<FilialDebitada> listaFiliais = filialDebitadaService.findByIdReciboIndenizacao(idReciboIndenizacao);

		if (!listaFiliais.isEmpty()) {
			Collections.sort(listaFiliais, new BeanComparator("idFilialDebitada"));
			siglaPrimeiraFilial = listaFiliais.get(0).getFilial().getSgFilial();
		}

		return siglaPrimeiraFilial;
    }
    
    public void configReportDomains(ReportDomainConfig config) {
    	config.configDomainField("TP_INDENIZACAO", "DM_TIPO_INDENIZACAO");
    	config.configDomainField("TP_BENEFICIARIO_INDENIZACAO", "DM_BENEFICIARIO_INDENIZACAO");
    	config.configDomainField("TP_FORMA_PAGAMENTO", "DM_FORMA_PAGAMENTO_INDENIZACAO");
    	config.configDomainField("TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
    	config.configDomainField("TP_IDENTIFICACAO", "DM_TIPO_IDENTIFICACAO_PESSOA");
    }
    
    public String calculaPercentualIndenizado(BigDecimal a, BigDecimal b) {
    	String retorno = "";
    	if (a!=null && b!=null) {
    		BigDecimal resultado = BigDecimalUtils.divide(a.multiply(new BigDecimal(100)), b);
    		resultado = BigDecimalUtils.round(resultado);
    		retorno = FormatUtils.formatDecimal("#,##0.00", resultado, true) + " %";
    	}
    	return retorno;
    }
    
    public String formataValorPorExtenso(BigDecimal valor, Long idMoeda) {
    	String retorno = "";
    	
    	if (idMoeda!=null && valor != null && BigDecimal.ZERO.compareTo(valor) != 0) {
    		Moeda moeda = getMoedaService().findById(idMoeda);
    		retorno = MoedaUtils.formataPorExtenso(valor, moeda);
    		retorno = retorno.toUpperCase();
    	}
    	return retorno;
    }
    
	public void setFilialDebitadaService(FilialDebitadaService filialDebitadaService) {
		this.filialDebitadaService = filialDebitadaService;
	}
        
        
}
