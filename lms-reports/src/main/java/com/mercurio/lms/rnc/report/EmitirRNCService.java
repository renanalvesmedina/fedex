package com.mercurio.lms.rnc.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.rnc.model.ItemOcorrenciaNc;
import com.mercurio.lms.rnc.model.service.ItemOcorrenciaNcService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Classe responsável pela geração do relatório Emitir RNC.
 * Especificação técnica 12.01.01.03
 * @author Rodrigo Antunes
 * 
 * @spring.bean id="lms.rnc.emitirRNCService"
 * @spring.property name="reportName" value="com/mercurio/lms/rnc/report/emitirRNC.jasper"
 */
public class EmitirRNCService extends ReportServiceSupport {
    
	private ItemOcorrenciaNcService itemOcorrenciaNcService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
    private Long idNaoConformidade;
    

    public void setItemOcorrenciaNcService(ItemOcorrenciaNcService itemOcorrenciaNcService) {
		this.itemOcorrenciaNcService = itemOcorrenciaNcService;
	}
	
	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}
    public Long getIdNaoConformidade() {
        return idNaoConformidade;
    }
    public void setIdNaoConformidade(Long idNaoConformidade) {
        this.idNaoConformidade = idNaoConformidade;
    }

    
	/**
     * Método que é chamado para geração do relatório. 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap tfm = (TypedFlatMap)parameters;
    	
    	Long idNaoConformidade = tfm.getLong("naoConformidade.idNaoConformidade");
        setIdNaoConformidade(idNaoConformidade);
    	List param = new ArrayList();
        String sql = mountSql(idNaoConformidade, param);

        JRReportDataObject jr = executeQuery(sql, param.toArray());

        SqlTemplate sqlTemplate = createSqlTemplate();
        sqlTemplate.addFilterSummary("naoConformidade", FormatUtils.formatSgFilialWithLong(tfm.getString("naoConformidade.filial.sgFilial"), tfm.getLong("naoConformidade.nrNaoConformidade")));

        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa", sqlTemplate.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
        jr.setParameters(parametersReport);
        return jr; 
    }
    
    /**
     * Monta o sql do relatório
     * @param idNaoConformidade
     * @return 
     */     
    private String mountSql(Long idNaoConformidade, List param) throws Exception {
    	StringBuffer sql = new StringBuffer()
    	.append("select ")
    	.append("FILIAL_NC.SG_FILIAL as SG_FILIAL_NC, ")
    	.append("NC.NR_NAO_CONFORMIDADE as NR_NAO_CONFORMIDADE, ")
    	.append("DS.TP_DOCUMENTO_SERVICO as TP_DOCUMENTO_SERVICO, ")
    	.append("DS.DH_EMISSAO as DH_EMISSAO, ")
    	.append("PESSOA_REM.NM_PESSOA as REMETENTE, ")
    	.append("PESSOA_DEST.NM_PESSOA as DESTINATARIO, ")
    	.append("FILIAL_DEST.SG_FILIAL as DESTINO, ")
    	.append("FILIAL_ORIG.SG_FILIAL as FILIAL_DOCTO_SERV, ")
    	.append("DS.NR_DOCTO_SERVICO as NR_DOCUMENTO_SERVICO, ")
    	.append("case when (")
    		.append("DS.TP_DOCUMENTO_SERVICO = 'CTR' or ") // CTRC
    		.append("DS.TP_DOCUMENTO_SERVICO = 'NFT' or ") 
    		.append("DS.TP_DOCUMENTO_SERVICO = 'CTE' or ") 
    		.append("DS.TP_DOCUMENTO_SERVICO = 'NTE') then DS.QT_VOLUMES ")
    	.append("when (DS.TP_DOCUMENTO_SERVICO = 'CRT') then null ")
    	.append("when (DS.TP_DOCUMENTO_SERVICO = 'MDA') then ")
    		 .append("(select sum(QT_VOLUMES) from ITEM_MDA where ITEM_MDA.ID_MDA = DS.ID_DOCTO_SERVICO) ")
    	.append("end as QT_VOLUMES ")
    	.append("from ")
    	.append("NAO_CONFORMIDADE NC ")
    	.append("inner join FILIAL FILIAL_NC on (NC.ID_FILIAL = FILIAL_NC.ID_FILIAL) ")
    	.append("left join DOCTO_SERVICO DS on (NC.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO) ")
    	.append("left join FILIAL FILIAL_DEST on (DS.ID_FILIAL_DESTINO = FILIAL_DEST.ID_FILIAL) ")
    	.append("left join FILIAL FILIAL_ORIG on (DS.ID_FILIAL_ORIGEM = FILIAL_ORIG.ID_FILIAL) ")
    	.append("left join CLIENTE CLIENTE_DEST on (NC.ID_CLIENTE_DESTINATARIO = CLIENTE_DEST.ID_CLIENTE) ")
    	.append("left join PESSOA PESSOA_DEST on (CLIENTE_DEST.ID_CLIENTE = PESSOA_DEST.ID_PESSOA) ")
    	.append("left join CLIENTE CLIENTE_REM on (NC.ID_CLIENTE_REMETENTE = CLIENTE_REM.ID_CLIENTE) ")
    	.append("left join PESSOA PESSOA_REM on (CLIENTE_REM.ID_CLIENTE = PESSOA_REM.ID_PESSOA) ")
    	.append("where ")
    	.append("NC.ID_NAO_CONFORMIDADE = ? ");
    	
    	param.add(idNaoConformidade);
        return sql.toString();
    }
    
    /**
     * Método que gera o subrelatorio de RNC Ocorrencias. 
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirRNCOcorrencias(Object[] parameters) throws Exception {
        SqlTemplate sql = createSqlTemplate();
        // SELECT
        sql.addProjection("onc.id_ocorrencia_nao_conformidade","ID_OCORRENCIA_NAO_CONFORMIDADE");
        sql.addProjection("onc.nr_ocorrencia_nc","NR_OCORRENCIA_NC");
        sql.addProjection("onc.nr_rnc_legado","NR_RNC_LEGADO");
        sql.addProjection("filial_legado.sg_filial","SG_FILIAL_LEGADO");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I","STATUS"));
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("MA_NC.DS_MOTIVO_ABERTURA_I","DS_MOTIVO_ABERTURA"));
        sql.addProjection("MA_NC.ID_MOTIVO_ABERTURA_NC","ID_MOTIVO_ABERTURA");
        
        sql.addProjection("onc.dh_inclusao","DH_ABERTURA");
        sql.addProjection("filial_cc.SG_FILIAL","FILIAL_CC");
        sql.addProjection("cc.NR_CONTROLE_CARGA","NR_CONTROLE_CARGA");

        sql.addProjection("" +
                "case\n" +
                "  \twhen manifesto.TP_MANIFESTO = 'E' THEN me.nr_manifesto_entrega\n" +
                "  \twhen manifesto.TP_MANIFESTO = 'V'\n" +
                "  \t\tTHEN\n" + 
                "  \t\t\t case\n" +
                "  \t\t\t\twhen manifesto.tp_abrangencia = 'N' then mvn.nr_manifesto_origem\n" +
                "  \t\t\t\twhen manifesto.tp_abrangencia = 'I' then mi.nr_manifesto_int\n" +
                "  \t\t\tend\n " +
                "end as NR_MANIFESTO\n");
        sql.addProjection("mt_t.NR_FROTA || ' ' || mt_t.NR_IDENTIFICADOR","VEICULO");
        sql.addProjection("mt_r.NR_FROTA || ' ' || mt_r.NR_IDENTIFICADOR","SEMI_REBOQUE");
        sql.addProjection("usuario.nm_usuario","USUARIO");
        sql.addProjection("filial_ab.SG_FILIAL","FILIAL_ABERTURA");
        sql.addProjection("onc.ds_ocorrencia_nc" ,"DESCRICAO");
        sql.addProjection("pessoa_emp.nm_pessoa","CIA_AEREA");

        sql.addProjection("onc.DS_CAIXA_REAPROVEITADA","CAIXA_REAPROVEITADA");
        sql.addProjection("moeda.ds_simbolo","SIMBOLO_MOEDA");
        sql.addProjection("onc.vl_ocorrencia_nc","VALOR");
        sql.addProjection("filial_resp.SG_FILIAL","FILIAL_RESPONSAVEL");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("cnc.ds_causa_nao_conformidade_i","CAUSA"));
        sql.addProjection("onc.ds_causa_nc","CAUSA2");
        sql.addProjection("onc.qt_volumes","QT_VOLUMES");
        sql.addProjection("f_manifesto.sg_filial","FILIAL_MANIFESTO");

        // FROM
        sql.addFrom("nao_conformidade","nc");
        sql.addFrom("ocorrencia_nao_conformidade","onc");
        sql.addFrom("MOTIVO_ABERTURA_NC","ma_nc");
        sql.addFrom("filial","filial_cc");
        sql.addFrom("filial","filial_ab");
        sql.addFrom("filial","filial_resp");
        sql.addFrom("filial","filial_legado");
        sql.addFrom("controle_carga","cc");
        sql.addFrom("meio_transporte","mt_t");
        sql.addFrom("meio_transporte","mt_r");
        sql.addFrom("usuario");
        sql.addFrom("empresa");
        sql.addFrom("pessoa","pessoa_emp");

        sql.addFrom("CAUSA_NAO_CONFORMIDADE","cnc");
        sql.addFrom("manifesto");
        sql.addFrom("filial","f_manifesto");
        sql.addFrom("moeda");
        sql.addFrom("manifesto_entrega"," me");
        sql.addFrom("manifesto_internacional","mi");
        sql.addFrom("manifesto_viagem_nacional","mvn");
        sql.addFrom("dominio","DM");
        sql.addFrom("valor_dominio","VD");
        
        // WHERE
        sql.addJoin("nc.id_nao_conformidade","onc.id_nao_conformidade");
        sql.addJoin("onc.id_motivo_abertura_nc","ma_nc.id_motivo_abertura_nc");
        sql.addJoin("onc.id_controle_carga","cc.id_controle_carga (+)");
        sql.addJoin("cc.id_transportado","mt_t.id_meio_transporte (+)");
        sql.addJoin("cc.id_semi_rebocado","mt_r.id_meio_transporte (+)");
        sql.addJoin("cc.id_filial_origem","filial_cc.id_filial (+)");
        sql.addJoin("onc.id_usuario","usuario.id_usuario");
        sql.addJoin("onc.id_filial_abertura","filial_ab.id_filial");
        sql.addJoin("onc.id_filial_legado","filial_legado.id_filial");
        sql.addJoin("onc.id_empresa","empresa.id_empresa (+)");
        sql.addJoin("empresa.id_empresa","pessoa_emp.ID_PESSOA (+)");

        sql.addJoin("onc.id_moeda","moeda.id_moeda (+)");
        sql.addJoin("onc.id_filial_responsavel","filial_resp.id_filial");
        sql.addJoin("onc.id_causa_nao_conformidade","cnc.id_causa_nao_conformidade (+)");
        sql.addJoin("onc.id_manifesto","manifesto.id_manifesto (+)");
        sql.addJoin("manifesto.id_filial_origem","f_manifesto.id_filial (+)");
        
        sql.addJoin("manifesto.ID_MANIFESTO ","me.id_manifesto_entrega (+)");
        sql.addJoin("manifesto.ID_MANIFESTO","mvn.ID_MANIFESTO_VIAGEM_NACIONAL(+)");
        sql.addJoin("manifesto.ID_MANIFESTO","mi.ID_MANIFESTO_INTERNACIONAL (+)");

        sql.addJoin("DM.NM_DOMINIO","'DM_STATUS_OCORRENCIA_NC'");
        sql.addJoin("VD.ID_DOMINIO","DM.ID_DOMINIO");
        sql.addJoin("VD.VL_VALOR_DOMINIO","onc.tp_status_ocorrencia_nc");
        sql.addCriteria("nc.id_nao_conformidade","=",getIdNaoConformidade());
        
        sql.addOrderBy("onc.nr_ocorrencia_nc");

        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }

    /**
     * Método que gera o subrelatorio de RNC Notas Fiscais. 
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirRNCNotasFiscais(Object[] parameters) throws Exception {
        SqlTemplate sql = createSqlTemplate();

        // select
        sql.addProjection("nf_cto.nr_nota_fiscal","NR_NOTA_FISCAL");
        //from
        sql.addFrom("nota_ocorrencia_nc", "no_nc");
        sql.addFrom("nota_fiscal_conhecimento", "nf_cto");
        // where
        sql.addCriteria("no_nc.id_ocorrencia_nao_conformidade","=", parameters[0]);
        sql.addJoin("no_nc.id_nota_fiscal_conhecimento", "nf_cto.id_nota_fiscal_conhecimento");
        // order by
        sql.addOrderBy("nf_cto.nr_nota_fiscal","ASC");
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }

    /**
     * Método que gera o subrelatorio de RNC Ações Corretivas.
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirRNCAcoesCorretivas(Object[] parameters) throws Exception {
        SqlTemplate sql = createSqlTemplate();
        // select
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("ac.ds_acao_corretiva_i","DS_ACAO_CORRETIVA"));
        // from
        sql.addFrom("ocorrencia_nao_conformidade","onc");
        sql.addFrom("CAUSA_NAO_CONFORMIDADE","cnc");
        sql.addFrom("causa_acao_corretiva","cac");
        sql.addFrom("ACAO_CORRETIVA","ac");
        // where
        sql.addJoin("onc.id_causa_nao_conformidade","cnc.id_causa_nao_conformidade");
        sql.addJoin("cnc.id_causa_nao_conformidade","cac.id_causa_nao_conformidade");
        sql.addJoin("cac.id_acao_corretiva","ac.id_acao_corretiva");
        sql.addCriteria("onc.id_ocorrencia_nao_conformidade","=", parameters[0]);
        
        sql.addOrderBy("DS_ACAO_CORRETIVA");

        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Método que gera o subrelatorio de RNC Caracteristicas.
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirRNCCaracteristicas(Object[] parameters) throws Exception {
        SqlTemplate sql = createSqlTemplate();
        //select
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("cp.ds_caracteristica_produto_i","CARACTERISTICA"));
        sql.addProjection("cpo.ds_caract_produto_ocorrencia","DESCRICAO");
        // from
        sql.addFrom("caract_produto_ocorrencia","cpo");
        sql.addFrom("CARACTERISTICA_PRODUTO","cp");
        //where
        sql.addCriteria("cpo.id_ocorrencia_nao_conformidade","=", parameters[0]);
        sql.addJoin("cpo.id_caracteristica_produto","cp.id_caracteristica_produto");
        sql.addOrderBy("CARACTERISTICA");
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Método que gera o subrelatorio de RNC Negociações 
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirRNCNegociacoes(Object[] parameters) throws Exception {
        SqlTemplate sql = createSqlTemplate();
        
        // Monta o select
        sql.addProjection("n.dh_negociacao","DH_NEGOCIACAO");
        sql.addProjection("u.nm_usuario","USUARIO");
        sql.addProjection("f.sg_filial","FILIAL");
        sql.addProjection("n.ds_negociacao","DESCRICAO");
        sql.addProjection("n.id_negociacao","ID_NEGOCIACAO");
        //Monta o from
        sql.addFrom("negociacao","n");
        sql.addFrom("filial","f");
        sql.addFrom("usuario","u");
        // Monta o where
        sql.addJoin("n.id_filial","f.id_filial");
        sql.addJoin("n.id_usuario","u.id_usuario");
        sql.addCriteria("n.id_ocorrencia_nao_conformidade","=", parameters[0]);
        // Order by
        sql.addOrderBy("n.dh_negociacao","DESC");
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Método que gera o subrelatorio de RNC Disposição
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirRNCDisposicao(Object[] parameters) throws Exception {
        SqlTemplate sql = createSqlTemplate();
        
        //select
        sql.addProjection("d.id_disposicao","ID_DISPOSICAO");
        sql.addProjection("d.dh_disposicao","DH_DISPOSICAO");
        sql.addProjection("u.nm_usuario","USUARIO");
        sql.addProjection("f.sg_filial","FILIAL");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("md.ds_motivo_i","MOTIVO"));
        sql.addProjection("d.ds_disposicao","DESCRICAO");
        
        // from
        sql.addFrom("disposicao","d");
        sql.addFrom("MOTIVO_DISPOSICAO","md");
        sql.addFrom("filial","f");
        sql.addFrom("usuario","u");
        // where
        sql.addCriteria("d.id_ocorrencia_nao_conformidade","=", parameters[0]);
        sql.addJoin("d.id_filial","f.id_filial");
        sql.addJoin("d.id_usuario","u.id_usuario");
        sql.addJoin("d.id_motivo_disposicao","md.id_motivo_disposicao");
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }

    /**
     * Executa a consulta para o relatorio de detalhamento de itensNFe
     * 
     * @param parameters
     * @return
     * @throws Exception
     * @author WagnerFC
     */
    public JRDataSource executeItensNFe(Object[] parameters) throws Exception {
		
	    	
	
	    	
			Map criteria = new HashMap<String, Object>();
			criteria.put("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", parameters[0]);
			List<ItemOcorrenciaNc> itens = (List<ItemOcorrenciaNc>) itemOcorrenciaNcService.find(criteria);
	        

			List<Map> mappedItens = new ArrayList<Map>();
	        
			Map<String,List<NotaFiscalEdiItem>> notaItensInMemory = new HashMap<String,List<NotaFiscalEdiItem>>(); 
			for (ItemOcorrenciaNc item : itens) {
	        
				List<NotaFiscalEdiItem> notaItens = notaItensInMemory.get(item.getNrChave());
				if(notaItens == null){
					notaItens = notaFiscalEletronicaService.findNfeItensByNrChave(item.getNrChave());
					notaItensInMemory.put(item.getNrChave(), notaItens);
		}
					
				Map nfeItem;
				for (NotaFiscalEdiItem notaItem : notaItens) {
					if(notaItem.getNumeroItem().equals(item.getNrItem().intValue())){
						nfeItem = new HashMap<String,Object>();
						nfeItem.put("NR_CHAVE", item.getNrChave()); //Precisa ser String
						nfeItem.put("NR_ITEM", item.getNrItem().toString()); //Precisa ser String
						nfeItem.put("DESCRICAO",notaItem.getDescricaoItem()); //Precisa ser String
						nfeItem.put("VALOR_TOTAL_COM",notaItem.getVlTotalItem().doubleValue()); //Precisa ser Double
						nfeItem.put("QTDE_COM", notaItem.getQtdeItem().toString());//Precisa ser String
						nfeItem.put("QT_NAO_CONFORMIDADE",item.getQtNaoConformidade().toString());//Precisa ser String
						nfeItem.put("VL_NAO_CONFORMIDADE",item.getVlNaoConformidade().doubleValue()); //Precisa ser Double
							
						mappedItens.add(nfeItem);
						break;
					}
				}
			}
			
			JRDataSource jrds = new JRBeanCollectionDataSource(mappedItens);   

			return jrds;
		
		
		}
    
    public void configReportDomains(ReportDomainConfig config) {
        config.configDomainField("TP_DOCUMENTO_SERVICO","DM_TIPO_DOCUMENTO_SERVICO");
    }

}