package com.mercurio.lms.coleta.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRLoopDataSource;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;

/**
 * Classe responsável pela geração do Relatório de Manifestos. Especificação
 * técnica 02.01.02.06
 * 
 * @author Rodrigo Antunes
 * 
 * @spring.bean id="lms.coleta.emitirManifestosService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirManifestos.jasper"
 */
public class EmitirManifestosService extends ReportServiceSupport {

    private Integer nrFolhasComplementares;
    private VersaoDescritivoPceService versaoDescritivoPceService;
    private EnderecoPessoaService enderecoPessoaService;
    private ConversaoMoedaService conversaoMoedaService;
    
    /**
     * Indica quantas tabelas cabem por página
     */
    private static final int NR_ITENS_COMPLEMENTARES_POR_PAGINA = 4;
    
    public JRReportDataObject execute(Map parameters) throws Exception {
        TypedFlatMap tfm = (TypedFlatMap)parameters;
        setNrFolhasComplementares( tfm.getInteger("nrFolhasComplementares") );

        SqlTemplate sql = createMainQuery(tfm);
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        
        // Seta os parametros de pesquisa que irão no cabeçalho da página
        Map parametersReport = new HashMap();
        parametersReport.put("nrFolhasComplementares", getNrFolhasComplementares());
        parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosRelatorio", tfm);
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        jr.setParameters(parametersReport);
        return jr;
    }

    /**
     * Adiciona os campos que serão utilizados mo relatorio 
     * @param parameters
     * @return
     */
    private SqlTemplate createMainQuery(TypedFlatMap parameters) {
    	String subsql = new StringBuffer()
		.append("(SELECT count(1)")
		.append(" FROM produto")
		.append(" INNER JOIN pedido_coleta_produto")
		.append(" ON produto.id_produto = pedido_coleta_produto.id_produto")
		.append(" INNER JOIN pedido_coleta")
		.append(" ON pedido_coleta_produto.id_pedido_coleta = pedido_coleta.id_pedido_coleta")
		.append(" INNER JOIN manifesto_coleta")
		.append(" ON pedido_coleta.id_rota_coleta_entrega = manifesto_coleta.id_rota_coleta_entrega")
		.append(" WHERE produto.tp_categoria                in ('PE','PR')")
		.append(" AND manifesto_coleta.id_manifesto_coleta  = mc.id_manifesto_coleta)").toString();
    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("mc.ID_MANIFESTO_COLETA");
        sql.addProjection("f_mc.SG_FILIAL", "SG_FILIAL_MC");
        sql.addProjection("mc.NR_MANIFESTO", "NR_MANIFESTO");
        sql.addProjection("nvl(decode("+subsql+",0,' ','P'), ' ')", "IDENT_PERIGOSO");
        sql.addFrom("manifesto_coleta", "mc");
        sql.addFrom("filial", "f_mc");
        sql.addJoin("mc.TP_STATUS_MANIFESTO_COLETA", "'GE'");
        sql.addJoin("mc.ID_FILIAL_ORIGEM", "f_mc.ID_FILIAL");
        sql.addCriteria("mc.ID_FILIAL_ORIGEM","=",SessionUtils.getFilialSessao().getIdFilial());

        Long idRotaColetaEntrega = parameters.getLong("rotaColetaEntrega.idRotaColetaEntrega");
        sql.addCriteria("mc.ID_ROTA_COLETA_ENTREGA","=",idRotaColetaEntrega);
        sql.addCriteria("mc.ID_ROTA_COLETA_ENTREGA","=",idRotaColetaEntrega);
        
        sql.addCriteria("mc.ID_MANIFESTO_COLETA","=", parameters.getLong("manifestoColeta.idManifestoColeta"));

        sql.addOrderBy("SG_FILIAL_MC");
        sql.addOrderBy("NR_MANIFESTO");

        return sql;
    }

    /**
     * sub relatorio de coleta 
     * @param parameters
     * @return
     */
    public JRDataSource executeSubReportColeta(TypedFlatMap parameters, Long idManifestoColeta) {
        SqlTemplate sql = mountDefaultFromAndWhere(parameters);
        sql.addProjection("f_mc.SG_FILIAL", "SG_FILIAL_MC");
        sql.addProjection("pc.ID_PEDIDO_COLETA");
        sql.addProjection("f_pc.SG_FILIAL", "SG_FILIAL_pc");
        sql.addProjection("pc.NR_COLETA", "NR_COLETA");
        sql.addProjection("pc.TP_PEDIDO_COLETA", "TP_PEDIDO_COLETA");
        sql.addProjection("pc.id_cliente", "ID_CLIENTE_COLETA");
        sql.addProjection("p_cliente.NM_PESSOA", "CLIENTE");
        sql.addProjection("p_cliente.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
	    sql.addProjection("p_cliente.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
        sql.addProjection("pc.DH_COLETA_DISPONIVEL", "DH_COLETA_DISPONIVEL");
        sql.addProjection("pc.HR_LIMITE_COLETA", "HR_LIMITE_COLETA");
        sql.addProjection("pc.NM_CONTATO_CLIENTE", "CONTATO");
        sql.addProjection("pc.NR_DDD_CLIENTE", "NR_DDD_CLIENTE");
        sql.addProjection("pc.NR_TELEFONE_CLIENTE", "NR_TELEFONE_CLIENTE");
        sql.addProjection("PC.ED_COLETA", "ENDERECO");
        sql.addProjection("case when NOT (PC.NR_ENDERECO IS NULL) then ', ' || PC.NR_ENDERECO ELSE ' ' end as NR_ENDERECO");
        sql.addProjection("case when NOT (PC.DS_COMPLEMENTO_ENDERECO IS NULL) then ', ' || PC.DS_COMPLEMENTO_ENDERECO ELSE ' ' end as COMPLEMENTO");
        sql.addProjection("case when NOT (PC.DS_BAIRRO IS NULL) then ', ' || PC.DS_BAIRRO ELSE ' ' end as DS_BAIRRO");
        sql.addProjection("m.NM_MUNICIPIO", "NM_MUNICIPIO_PC");
        sql.addProjection("pc.OB_PEDIDO_COLETA", "OB_PEDIDO_COLETA");
        sql.addCriteria("mc.ID_MANIFESTO_COLETA","=",idManifestoColeta);
        sql.addOrderBy("f_pc.SG_FILIAL");
        sql.addOrderBy("pc.NR_COLETA");

        Integer nrPedidosColeta = findNrTotalDados(parameters);
        if (nrPedidosColeta.intValue()==0)
        	return new JRLoopDataSource(1);
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Monta o from e o where default utilizado nas consultas 
     * deste relatorio. 
     * @param parameters
     * @return
     */
    private SqlTemplate mountDefaultFromAndWhere(TypedFlatMap parameters) {
        SqlTemplate sql = createSqlTemplate();

        StringBuffer strFrom = new StringBuffer()
	        .append("manifesto_coleta mc ")
	        .append("inner join filial f_mc on (mc.ID_FILIAL_ORIGEM = f_mc.ID_FILIAL) ")
	        .append("left outer join pedido_coleta pc on (mc.ID_MANIFESTO_COLETA = pc.ID_MANIFESTO_COLETA AND pc.TP_PEDIDO_COLETA <> 'AE' ) ")
	        .append("left outer join filial f_pc on (pc.ID_FILIAL_RESPONSAVEL = f_pc.ID_FILIAL) ")
	        .append("left outer join cliente c on (pc.ID_CLIENTE = c.ID_CLIENTE) ")
	        .append("left outer join pessoa p_cliente on (c.ID_CLIENTE = p_cliente.ID_PESSOA) ")
	        .append("left outer join municipio m on (pc.ID_MUNICIPIO = m.ID_MUNICIPIO) ");
        
        sql.addFrom(strFrom.toString());
        
        Long idRotaColetaEntrega = parameters.getLong("rotaColetaEntrega.idRotaColetaEntrega");
        sql.addCriteria("mc.TP_STATUS_MANIFESTO_COLETA", "=", "GE");
        sql.addCriteria("mc.ID_ROTA_COLETA_ENTREGA","=",idRotaColetaEntrega);
        sql.addCriteria("mc.ID_MANIFESTO_COLETA","=", parameters.getLong("manifestoColeta.idManifestoColeta"));
        sql.addCriteria("mc.ID_FILIAL_ORIGEM","=",SessionUtils.getFilialSessao().getIdFilial());
        return sql;
    }
    
    /**
     * executa um find para pegar o total de coletas e
     * setar esse valor no atributo nrTotalColetas. 
     * @param parameters
     */
    public Integer findNrTotalDados(TypedFlatMap parameters) {
        SqlTemplate sql = mountDefaultFromAndWhere(parameters);
        sql.addProjection("COUNT(*)");
        int nrTotalColetas = this.getJdbcTemplate().queryForInt(sql.getSql(),sql.getCriteria());
        return Integer.valueOf(nrTotalColetas);
    }
    
    /**
     * Método que busca os manifestos que serão registrados para 
     * a emissão do relatório. 
     * @param parameters
     * @return
     */
    public List findManifestosParaRegistrar(TypedFlatMap parameters) {
        SqlTemplate sql = mountDefaultFromAndWhere(parameters);
        sql.setDistinct();
        sql.addProjection("mc.ID_MANIFESTO_COLETA","ID_MANIFESTO_COLETA");
        List list = this.getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria(),Long.class);
        return list;
    }
    
    /**
     * Sub relatório para gerar as folhas compementares e
     * calcular para ver quantos itens cabe na primeira folha. 
     * 
     */
    public JRDataSource executeFolhasComplementares(Integer totalColetas, Integer totalDetalhesColeta) {
        int total = 0;

        int nrColetas = totalColetas.intValue();
        int totalDetalhes = totalDetalhesColeta.intValue();

        // Neste if, é para verificar se a chamada é para gerar páginas
        if (nrColetas ==-1 && totalDetalhes==-1) {
            if (getNrFolhasComplementares()!=null && getNrFolhasComplementares().intValue() > 0) {
                total = NR_ITENS_COMPLEMENTARES_POR_PAGINA * getNrFolhasComplementares().intValue();
            }
            // Neste caso é para completar a folha que está sendo impressa com os itens.
        } else {
            int itensComplementares = NR_ITENS_COMPLEMENTARES_POR_PAGINA;
            int contaDetalhe = totalDetalhes-nrColetas;
            // verifica se há detalhes a mais e diminui 1 item conforme a regra abaixo
            if (contaDetalhe>2 && contaDetalhe<=3) {
                itensComplementares--;
            }else if (contaDetalhe>3 && contaDetalhe<=5) {
                itensComplementares = itensComplementares-2;
            }else if (contaDetalhe>5 && contaDetalhe<=7) {
                itensComplementares = itensComplementares-3;
            }  
            
            if (totalColetas.intValue() < itensComplementares) {
                total = total + (itensComplementares - nrColetas);
            } else if (totalColetas.intValue() > itensComplementares) {
            	// caso seja maior o total de coletas é porque passou de uma folha 
            	// e deve iniciar um novo calculo
                int resto = nrColetas/itensComplementares;
                int nrColetasUltimaPagina = Math.abs((resto * itensComplementares)-nrColetas);
                
                if (nrColetasUltimaPagina > 0 && nrColetasUltimaPagina < itensComplementares) {
                    total = total + (itensComplementares - nrColetasUltimaPagina);
                }
            }
        }
        
        JRLoopDataSource loopDataSource = new JRLoopDataSource(total);
        return loopDataSource;
    }

    /**
     * Método para configurar os dominios utilizados no relatorio. 
     */
    public void configReportDomains(ReportDomainConfig config) {
        config.configDomainField("TP_PEDIDO_COLETA","DM_TIPO_PEDIDO_COLETA");
        config.configDomainField("TP_FRETE","DM_TIPO_FRETE");
    }
    
    /**
     * 
     * @param idPedidoColeta
     * @return
     */
    public JRDataSource executeSubReportDetalheColeta(Long idPedidoColeta) {

    	if (idPedidoColeta==null)
    		return new JREmptyDataSource();
    	
    	SqlTemplate sql = createSqlTemplate();
        sql.addProjection("dc.ID_DETALHE_COLETA");
        sql.addProjection("case when not("+PropertyVarcharI18nProjection.createProjection("LE_DC.DS_LOCALIDADE_I")+" is null) then " + 
        		PropertyVarcharI18nProjection.createProjection("LE_DC.DS_LOCALIDADE_I") + " ELSE M_DC.NM_MUNICIPIO end as DESTINO");
        sql.addProjection("case when not(UF_LE.SG_UNIDADE_FEDERATIVA is null) then UF_LE.SG_UNIDADE_FEDERATIVA ELSE UF_M_DC.SG_UNIDADE_FEDERATIVA end as UF");
        sql.addProjection("p_c_dc.NM_PESSOA", "DESTINATARIO");
        sql.addProjection("S.SG_SERVICO", "SERVICO");
        sql.addProjection("dc.TP_FRETE", "TP_FRETE");
        sql.addProjection("DC.PS_MERCADORIA", "PESO");
        sql.addProjection("dc.PS_AFORADO", "PESO_AFORADO");
        sql.addProjection("dc.QT_VOLUMES", "VOLUMES");
        sql.addProjection("dc.VL_MERCADORIA", "VL_MERCADORIA");
        sql.addProjection("pc.ID_FILIAL_RESPONSAVEL", "ID_FILIAL_RESPONSAVEL");
        sql.addProjection("moeda.ID_MOEDA", "ID_MOEDA_DC");
        sql.addProjection("moeda.SG_MOEDA", "SG_MOEDA");
        sql.addProjection("moeda.DS_SIMBOLO", "DS_SIMBOLO");
        sql.addProjection("f_cotacao.SG_FILIAL", "SG_FILIAL_COTACAO");
        sql.addProjection("cotacao.NR_COTACAO", "NR_COTACAO");
        sql.addProjection("ctoi.SG_PAIS", "SG_PAIS_CRT");
        sql.addProjection("ctoi.NR_CRT", "NR_CRT");
        
        sql.addFrom("pedido_coleta", "pc");
        sql.addFrom("detalhe_coleta", "dc");
        sql.addFrom("servico", "s");
        sql.addFrom("municipio", "m_dc");
        sql.addFrom("localidade_especial", "le_dc");
        sql.addFrom("cliente", "c_dc");
        sql.addFrom("pessoa", "p_c_dc");
        sql.addFrom("unidade_federativa", "UF_M_DC");
        sql.addFrom("unidade_federativa", "UF_LE");
        sql.addFrom("moeda");
        sql.addFrom("cotacao");
        sql.addFrom("filial", "f_cotacao");
        sql.addFrom("cto_internacional", "ctoi");
        
        sql.addJoin("dc.ID_MOEDA", "moeda.ID_MOEDA(+)");
        sql.addJoin("dc.ID_COTACAO", "cotacao.ID_COTACAO(+)");
        sql.addJoin("cotacao.ID_FILIAL", "f_cotacao.ID_FILIAL(+)");
        sql.addJoin("dc.ID_CTO_INTERNACIONAL", "ctoi.ID_CTO_INTERNACIONAL(+)");
        sql.addJoin("pc.ID_PEDIDO_COLETA", "dc.ID_PEDIDO_COLETA(+)");
        sql.addJoin("dc.ID_SERVICO", "s.ID_SERVICO(+)");
        sql.addJoin("dc.ID_MUNICIPIO", "m_dc.ID_MUNICIPIO(+)");
        sql.addJoin("LE_DC.ID_UNIDADE_FEDERATIVA", "UF_LE.ID_UNIDADE_FEDERATIVA(+)");
        sql.addJoin("dc.ID_LOCALIDADE_ESPECIAL", "le_dc.ID_LOCALIDADE_ESPECIAL(+)");
        sql.addJoin("M_DC.ID_UNIDADE_FEDERATIVA", "UF_M_DC.ID_UNIDADE_FEDERATIVA(+)");
        sql.addJoin("dc.ID_CLIENTE", "c_dc.ID_CLIENTE(+)");
        sql.addJoin("c_dc.ID_CLIENTE", "p_c_dc.ID_PESSOA(+)");        
        sql.addCriteria("pc.ID_PEDIDO_COLETA","=",idPedidoColeta);

        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Sub relatório de awb, baseado no detalhe coleta 
     * @param idDetalheColeta
     * @return
     */
    public JRDataSource executeSubReportAwb(Long idDetalheColeta) {
    	if (idDetalheColeta==null)
    		return new JREmptyDataSource();

        SqlTemplate sql = createSqlTemplate();
        sql.addProjection("awb.NR_AWB", "NR_AWB");
        sql.addFrom("detalhe_coleta", "dc");
        sql.addFrom("awb_coleta");
        sql.addFrom("awb");
        sql.addJoin("dc.ID_DETALHE_COLETA", "awb_coleta.ID_DETALHE_COLETA(+)");
        sql.addJoin("awb_coleta.ID_AWB", "awb.ID_AWB(+)");
        sql.addOrderBy("awb.NR_AWB","ASC");
        sql.addCriteria("dc.ID_DETALHE_COLETA","=",idDetalheColeta);

        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Sub relatório de nf, baseado no detalhe coleta 
     * @param idDetalheColeta
     * @return
     */
    public JRDataSource executeSubReportNF(Long idDetalheColeta) {
    	if (idDetalheColeta==null)
    		return new JREmptyDataSource();
    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("nfc.NR_NOTA_FISCAL", "NR_NOTA_FISCAL");
        sql.addFrom("detalhe_coleta", "dc");
        sql.addFrom("nota_fiscal_coleta","nfc");
        sql.addJoin("dc.ID_DETALHE_COLETA", "nfc.ID_DETALHE_COLETA(+)");
        sql.addCriteria("dc.ID_DETALHE_COLETA","=",idDetalheColeta);
        sql.addOrderBy("nfc.NR_NOTA_FISCAL","ASC");
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Sub relatório de documento do awb/preawb, baseado no detalhe coleta 
     * @param idDetalheColeta
     * @return
     */
    public JRDataSource executeSubReportDoctoByAwb(Long idDetalheColeta) {
    	if (idDetalheColeta==null)
    		return new JREmptyDataSource();   	

    	SqlTemplate sql = createSqlTemplate();
    	
    	sql.addProjection("NVL(FO_DS.SG_FILIAL,FO_DSAWB.SG_FILIAL)", "DS_FILIAL_ORIGEM");
    	sql.addProjection("NVL(DS.NR_DOCTO_SERVICO, DSAWB.NR_DOCTO_SERVICO)", "DS_NUMERO");
    	sql.addProjection("(SELECT P.NM_FANTASIA FROM   CIA_FILIAL_MERCURIO CFM, PESSOA P WHERE  CFM.ID_EMPRESA = P.ID_PESSOA AND CFM.ID_CIA_FILIAL_MERCURIO IN (AW.ID_CIA_FILIAL_MERCURIO, AWB_DS.ID_CIA_FILIAL_MERCURIO))", "NM_CIA_AEREA");
    	sql.addProjection("DECODE(AW.TP_STATUS_AWB,'P',AW.ID_AWB, DECODE(AWB_DS.TP_STATUS_AWB, 'P', AWB_DS.ID_AWB, NVL(AW.NR_AWB, AWB_DS.NR_AWB)))", "NR_AWB");
    	sql.addProjection("NVL(AW.dv_awb , AWB_DS.dv_awb)", "DV_AWB");
    	sql.addProjection("NVL(CO.tp_frete , COAWB.tp_frete)", "TP_FRETE");
    	sql.addProjection("NVL(ds.id_docto_servico, DSAWB.id_docto_servico)", "ID_DOCTO_SERVICO");
    	sql.addProjection("NVL(ds.ps_real, DSAWB.ps_real)", "PESO");
    	sql.addProjection("NVL(ds.ps_aforado, DSAWB.ps_aforado)", "PESO_AFORADO");
    	sql.addProjection("NVL(ds.qt_volumes, DSAWB.qt_volumes)", "QT_VOLUME");
    	sql.addProjection("NVL(DS.VL_MERCADORIA, DSAWB.VL_MERCADORIA)", "VL_MERCADORIA");
    	sql.addProjection("NVL(aw.tp_status_awb, AWB_DS.tp_status_awb)", "TP_STATUS_AWB");
    	sql.addProjection("M.SG_MOEDA", "SG_MOEDA");
    	sql.addProjection("M.DS_SIMBOLO", "DS_SIMBOLO");
    	
    	sql.addInnerJoin("DETALHE_COLETA DC");
    	
    	sql.addLeftOuterJoin("MOEDA M ON M.id_moeda = DC.ID_MOEDA");
    	sql.addLeftOuterJoin("AWB_COLETA AC ON DC.ID_DETALHE_COLETA = AC.ID_DETALHE_COLETA");
    	sql.addLeftOuterJoin("AWB AW ON AC.ID_AWB = AW.ID_AWB");
    	sql.addLeftOuterJoin("CTO_AWB CA ON AW.ID_AWB = CA.ID_AWB");
    	sql.addLeftOuterJoin("DOCTO_SERVICO DSAWB ON CA.ID_CONHECIMENTO = DSAWB.ID_DOCTO_SERVICO");
    	sql.addLeftOuterJoin("CONHECIMENTO COAWB ON COAWB.ID_CONHECIMENTO = DSAWB.ID_DOCTO_SERVICO");
    	sql.addLeftOuterJoin("FILIAL FO_DSAWB ON DSAWB.ID_FILIAL_ORIGEM = FO_DSAWB.ID_FILIAL");
    	sql.addLeftOuterJoin("DOCTO_SERVICO DS ON DC.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO");
    	sql.addLeftOuterJoin("CONHECIMENTO CO ON CO.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO");
    	sql.addLeftOuterJoin("FILIAL FO_DS ON DS.ID_FILIAL_ORIGEM = FO_DS.ID_FILIAL");
    	sql.addLeftOuterJoin("CTO_AWB CA_DS ON DS.ID_DOCTO_SERVICO = CA_DS.ID_CONHECIMENTO");
    	sql.addLeftOuterJoin("AWB AWB_DS ON CA_DS.ID_AWB = AWB_DS.ID_AWB");
    	
    	sql.addCriteria("DC.ID_DETALHE_COLETA", "=", idDetalheColeta);    	
    	sql.addCustomCriteria("(AWB_DS.ID_AWB = (select max(a.id_awb) from   cto_awb a join awb on awb.id_awb = a.id_awb where  a.id_conhecimento = DC.id_docto_servico and awb.tp_status_awb <> 'C')  or DC.id_docto_servico is null)");
    	
    	sql.addOrderBy("NM_CIA_AEREA, NR_AWB, DS_FILIAL_ORIGEM, DS_NUMERO");
    	
    	return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Sub relatório de Servicos Adicionais da coleta 
     * @param idPedidoColeta
     * @return
     */
    public JRDataSource executeSubReportServicoAdicional(Long idPedidoColeta) {

    	if (idPedidoColeta==null) 
            return new JREmptyDataSource();
    	
    	SqlTemplate sql = createSqlTemplate();
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("sa.DS_SERVICO_ADICIONAL_I"), "DS_SERVICO_ADICIONAL");
        sql.addFrom("servico_adicional_coleta", "sac");
        sql.addFrom("servico_adicional", "sa");
        sql.addFrom("pedido_coleta", "pc");
        sql.addJoin("pc.ID_PEDIDO_COLETA", "sac.id_pedido_coleta(+)");
        sql.addJoin("sac.id_servico_adicional", "sa.ID_SERVICO_ADICIONAL(+)");
        sql.addCriteria("pc.ID_PEDIDO_COLETA","=",idPedidoColeta);
        sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("sa.DS_SERVICO_ADICIONAL_I"),"ASC");
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();        
    }
    
	public BigDecimal executeRetornaValorConvertido(Long idFilial,
			Long idMoedaOrigem, BigDecimal valor) {
		BigDecimal retorno = null;

		if (valor != null) {
			Long idPaisDestino = SessionUtils.getPaisSessao().getIdPais();
			Long idMoedaDestino = SessionUtils.getMoedaSessao().getIdMoeda();
			YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
			EnderecoPessoa enderecoPessoaPadrao = getEnderecoPessoaService().findEnderecoPessoaPadrao(idFilial);
			Long idPaisOrigem = enderecoPessoaPadrao.getMunicipio().getUnidadeFederativa().getPais().getIdPais();

			retorno = getConversaoMoedaService().findConversaoMoeda(idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino,dataAtual, valor);

		} else {
			retorno = new BigDecimal(0);
		}

		return retorno;
	}

    private Integer getNrFolhasComplementares() {
        return nrFolhasComplementares;
    }

    private void setNrFolhasComplementares(Integer nrFolhasComplementares) {
        this.nrFolhasComplementares = nrFolhasComplementares;
    }

    public VersaoDescritivoPceService getVersaoDescritivoPceService() {
        return versaoDescritivoPceService;
    }

    public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
        this.versaoDescritivoPceService = versaoDescritivoPceService;
    }

	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
   
	
    public String findObservacao(String obPedidoColeta, Long idClienteColeta) {
    	String retorno = obPedidoColeta!=null?obPedidoColeta:"";

    	String mensagemPce = getVersaoDescritivoPceService().
    				executeFindMensagemPce(	idClienteColeta, 
    										ProcessoPce.ID_PROCESSO_PCE_COLETA, 
    										EventoPce.ID_EVENTO_PCE_EMISS_MANIF_COLETA, 
    										OcorrenciaPce.ID_OCORR_PCE_COL_MANIF_EMISS_MANIF_COLETA);
    	if (!StringUtils.isBlank(mensagemPce)) {
    		retorno += "\n                       " + mensagemPce;
    	}
    	return retorno;
    }
}