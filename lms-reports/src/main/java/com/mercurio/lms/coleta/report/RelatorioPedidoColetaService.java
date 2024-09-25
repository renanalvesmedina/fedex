package com.mercurio.lms.coleta.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de Pedido de Coleta (ImprimirColeta). 
 * Especificação técnica 02.06.01.05
 * 
 * @spring.bean id="lms.coleta.relatorioPedidoColetaService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirPedidoColeta.jasper"
 */
 
public class RelatorioPedidoColetaService extends ReportServiceSupport {

    private PedidoColetaService pedidoColetaService;
    private Long idPedidoColeta;

    public JRReportDataObject execute(Map parameters) throws Exception {

    	Long id = Long.valueOf((String)parameters.get("idPedidoColeta"));
    	
    	PedidoColeta pc = pedidoColetaService.findById(id);
    	
    	if(pc.getSituacaoAprovacao() != null && !"A".equals(pc.getSituacaoAprovacao().getValue())){
    		throw new BusinessException("LMS-02094");
    	}
    	
        SqlTemplate sql = mountSql(parameters);

        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

        // Seta os parametros que irão no cabeçalho da página,
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        
        parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());

        jr.setParameters(parametersReport);
        return jr;
    }

    /**
     * Monta o sql do relatório
     * 
     * @param parameters
     * @return
     * @throws Exception
     */
    private SqlTemplate mountSql(Map parameters) throws Exception {

        // Select que irá conter a consulta real
        SqlTemplate sql = createSqlTemplate();

        // Adiciona os campos de retorno do select

        sql.addProjection("PC.NR_DDD_CLIENTE", "NR_DDD_CLIENTE");
        
        sql.addProjection("MOEDA.DS_SIMBOLO", "DS_SIMBOLO_MOEDA");
        sql.addProjection("DC.ID_DETALHE_COLETA", "ID_DETALHE_COLETA");
        sql.addProjection("PS.NM_PESSOA", "CLIENTE");
        sql.addProjection("PC.NR_COLETA", "NUMEROCOLETA");
        sql.addProjection("PC.ED_COLETA", "ENDERECO");
        sql.addProjection("case when NOT (PC.NR_ENDERECO IS NULL) then ', ' || PC.NR_ENDERECO ELSE ' ' end as NUMEROENDERECO");
        sql.addProjection("case when NOT (PC.DS_COMPLEMENTO_ENDERECO IS NULL) then ', ' || PC.DS_COMPLEMENTO_ENDERECO ELSE ' ' end as COMPLEMENTOENDERECO");
        sql.addProjection("PC.DS_BAIRRO", "BAIRRO");
        sql.addProjection("RCEF.DS_REGIAO_COLETA_ENTREGA_FIL", "REGIAO");
        sql.addProjection("MN.NM_MUNICIPIO", "MUNICIPIO");
        sql.addProjection("PC.NR_TELEFONE_CLIENTE", "TELEFONE");
        sql.addProjection("PC.OB_PEDIDO_COLETA", "OBSERVACAO");
        sql.addProjection("PC.TP_MODO_PEDIDO_COLETA", "MODOPEDIDO");
        sql.addProjection("PC.TP_PEDIDO_COLETA", "TIPOPEDIDO");
        sql.addProjection("PC.TP_STATUS_COLETA", "STATUS");
        sql.addProjection("FL.SG_FILIAL", "FILIALANUNCIANTE");
        sql.addProjection("PC.NM_SOLICITANTE", "NM_SOLICITANTE");
        sql.addProjection("PC.DT_PREVISAO_COLETA", "DATAPREVISAOCOLETA");
        sql.addProjection("PC.DH_PEDIDO_COLETA", "DATAHORAPRONTACOLETA");
        sql.addProjection("PC.DH_COLETA_DISPONIVEL", "DH_COLETA_DISPONIVEL");
        sql.addProjection("PC.HR_LIMITE_COLETA", "HORALIMITES");
        sql.addProjection("FL3.SG_FILIAL", "FILIAL_RESPONSAVEL");
        sql.addProjection("case when not(" +PropertyVarcharI18nProjection.createProjection("LE.DS_LOCALIDADE_I")+  " is null) then " +PropertyVarcharI18nProjection.createProjection("LE.DS_LOCALIDADE_I")+  " ELSE M_DC.NM_MUNICIPIO end as DESTINO");
        sql.addProjection("case when not(UF_LE.SG_UNIDADE_FEDERATIVA is null) then UF_LE.SG_UNIDADE_FEDERATIVA ELSE UF_M_DC.SG_UNIDADE_FEDERATIVA end as UF");
        sql.addProjection("F_DC.SG_FILIAL", "FILIAL");
        sql.addProjection("S.SG_SERVICO", "SERVICO");
        sql.addProjection("P_C_DC.NM_PESSOA", "DESTINATARIO");
        sql.addProjection("DC.TP_FRETE", "TIPOFRETE");

        sql.addProjection("DC.PS_MERCADORIA", "PESO");
        sql.addProjection("DC.PS_AFORADO", "PESOAFORADO");
        sql.addProjection("DC.QT_VOLUMES", "VOLUMES");
        sql.addProjection("DC.VL_MERCADORIA", "VALORMERCADORIA");

        sql.addProjection("PC.NR_CEP", "CEP");
        sql.addProjection("RC.DS_ROTA", "ROTA");
        sql.addProjection("PC.NM_CONTATO_CLIENTE", "CONTATO");
        sql.addProjection("FL.SG_FILIAL", "FILIALSOLICITANTE");

        sql.addFrom("PEDIDO_COLETA", "PC");
        sql.addFrom("DETALHE_COLETA", "DC");
        sql.addFrom("CLIENTE", "CL");
        sql.addFrom("PESSOA", "PS");
        sql.addFrom("MUNICIPIO", "MN");
        sql.addFrom("MUNICIPIO", "M_DC");
        sql.addFrom("FILIAL", "FL");
        sql.addFrom("FILIAL", "F_DC");
        sql.addFrom("FILIAL", "FL3");
        sql.addFrom("SERVICO", "S");
        sql.addFrom("CLIENTE", "C_DC");
        sql.addFrom("PESSOA", "P_C_DC");

        sql.addFrom("LOCALIDADE_ESPECIAL", "LE");
        sql.addFrom("UNIDADE_FEDERATIVA", "UF_LE");
        sql.addFrom("UNIDADE_FEDERATIVA", "UF_M_DC");
        sql.addFrom("ROTA_COLETA_ENTREGA", "RC");
        sql.addFrom("REGIAO_FILIAL_ROTA_COL_ENT", "RFRCE");
        sql.addFrom("REGIAO_COLETA_ENTREGA_FIL", "RCEF");
        sql.addFrom("MOEDA");

        sql.addJoin("PC.ID_PEDIDO_COLETA", "DC.ID_PEDIDO_COLETA(+)");
        sql.addJoin("DC.ID_LOCALIDADE_ESPECIAL", "LE.ID_LOCALIDADE_ESPECIAL(+)");
        sql.addJoin("LE.ID_UNIDADE_FEDERATIVA", "UF_LE.ID_UNIDADE_FEDERATIVA(+)");
        sql.addJoin("DC.ID_MUNICIPIO", "M_DC.ID_MUNICIPIO(+)");
        sql.addJoin("M_DC.ID_UNIDADE_FEDERATIVA", "UF_M_DC.ID_UNIDADE_FEDERATIVA(+)");
        sql.addJoin("DC.ID_FILIAL", "F_DC.ID_FILIAL(+)");
        sql.addJoin("DC.ID_CLIENTE", "C_DC.ID_CLIENTE(+)");
        sql.addJoin("C_DC.ID_CLIENTE", "P_C_DC.ID_PESSOA(+)");
        sql.addJoin("DC.ID_SERVICO", "S.ID_SERVICO(+)");
        
        sql.addJoin("PC.ID_FILIAL_RESPONSAVEL", "FL3.ID_FILIAL");
        sql.addJoin("PC.ID_CLIENTE", "CL.ID_CLIENTE");
        sql.addJoin("PS.ID_PESSOA", "CL.ID_CLIENTE");

        sql.addJoin("PC.ID_ROTA_COLETA_ENTREGA", "RC.ID_ROTA_COLETA_ENTREGA(+)");
        sql.addJoin("RC.ID_ROTA_COLETA_ENTREGA", "RFRCE.ID_ROTA_COLETA_ENTREGA(+)");

        sql.addCustomCriteria("((RFRCE.ID_ROTA_COLETA_ENTREGA IS NULL) OR (CURRENT_DATE>=RFRCE.DT_VIGENCIA_INICIAL AND CURRENT_DATE<=RFRCE.DT_VIGENCIA_FINAL))");

        sql.addJoin("RFRCE.ID_REGIAO_COLETA_ENTREGA_FIL", "RCEF.ID_REGIAO_COLETA_ENTREGA_FIL(+)");

        sql.addJoin("PC.ID_MOEDA", "MOEDA.ID_MOEDA");
 
        sql.addJoin("PC.ID_MUNICIPIO","MN.ID_MUNICIPIO");
        sql.addJoin("PC.ID_FILIAL_SOLICITANTE","FL.ID_FILIAL");

        setIdPedidoColeta(null);
        if (parameters.get("idPedidoColeta") != null) {
            String idPedidoColeta = parameters.get("idPedidoColeta").toString();
            sql.addCriteria("PC.ID_PEDIDO_COLETA", "=", idPedidoColeta, Long.class);

            Long idColeta = Long.valueOf(idPedidoColeta);
            setIdPedidoColeta(idColeta);
            Long nrColeta = findNumeroColeta(idColeta);

            sql.addFilterSummary("numeroColeta", FormatUtils.formatLongWithZeros(nrColeta));
        } else {
            // lança uma exception caso não venha o idPedidoColeta
            throw new BusinessException("LMS-02043");
        }
 
        return sql;
    }

    
    /**
     * Sub relatorio de AWB 
     * @param obj
     * @return
     */
    public JRDataSource executeEmitirPedidoColetaAWB(Object obj) {
        JRDataSource dataSource = null;
        
        if(obj==null) {
            dataSource = new JREmptyDataSource();
        } else {

            SqlTemplate sql = createSqlTemplate();
    
            sql.addProjection("emp.SG_EMPRESA","SG_EMPRESA");
            sql.addProjection("awb.DS_SERIE","DS_SERIE");
            sql.addProjection("awb.ID_AWB","ID_AWB");
            sql.addProjection("awb.NR_AWB","NR_AWB");
            sql.addProjection("awb.DV_AWB","DV_AWB");
            sql.addProjection("awb.TP_STATUS_AWB","TP_STATUS_AWB");
            
            sql.addFrom("pedido_coleta","pc");
            sql.addFrom("detalhe_coleta","dc");
            sql.addFrom("awb");
            sql.addFrom("cia_filial_mercurio", "cfm");
            sql.addFrom("empresa", "emp");
            
            sql.addCriteria("dc.ID_DETALHE_COLETA", "=", obj, Long.class);
            sql.addJoin("pc.ID_PEDIDO_COLETA","dc.ID_PEDIDO_COLETA");
    		sql.addJoin("awb.ID_AWB","(SELECT MAX(CAW.ID_AWB) FROM CTO_AWB CAW WHERE CAW.ID_CONHECIMENTO = dc.ID_DOCTO_SERVICO)");
            
            sql.addJoin("awb.ID_CIA_FILIAL_MERCURIO","cfm.ID_CIA_FILIAL_MERCURIO");
            sql.addJoin("cfm.ID_EMPRESA","emp.ID_EMPRESA");
            
            dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource(); 
        }
        return dataSource;
    }
    
    /**
     * Sub relatorio de AWB 
     * @param obj
     * @return
     */
    public JRDataSource executeEmitirPedidoColetaNF(Object obj) {
        JRDataSource dataSource = null;
        
        if(obj==null) {
            dataSource = new JREmptyDataSource();
        } else {
            SqlTemplate sql = new SqlTemplate();
            sql.addProjection("nfc.NR_NOTA_FISCAL","NR_NOTA_FISCAL");
            
            sql.addFrom("pedido_coleta pc");
            sql.addFrom("detalhe_coleta dc");
            sql.addFrom("nota_fiscal_coleta nfc");
            
            sql.addCriteria("PC.ID_PEDIDO_COLETA", "=", getIdPedidoColeta(), Long.class);
            sql.addCriteria("dc.ID_DETALHE_COLETA", "=", obj, Long.class);
            sql.addJoin("pc.ID_PEDIDO_COLETA","dc.ID_PEDIDO_COLETA");
            sql.addJoin("dc.ID_DETALHE_COLETA","nfc.ID_DETALHE_COLETA");
            dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource(); 
        }
        
        return dataSource;
    }
    
    /**
     * Método que retorna o numero da coleta que está sendo feito o relatório.
     * 
     * @param idPedidoColeta
     * @return nrColeta
     */
    private Long findNumeroColeta(Long idPedidoColeta) {
        PedidoColeta pc = getPedidoColetaService().findById(idPedidoColeta);
        return pc.getNrColeta();
    }

    public void configReportDomains(ReportDomainConfig config) {

        config.configDomainField("MODOPEDIDO", "DM_MODO_PEDIDO_COLETA");
        config.configDomainField("TIPOPEDIDO", "DM_TIPO_PEDIDO_COLETA");
        config.configDomainField("STATUS", "DM_STATUS_COLETA");
        config.configDomainField("TIPOFRETE", "DM_TIPO_FRETE");
    }

    public PedidoColetaService getPedidoColetaService() {
        return pedidoColetaService;
    }

    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }

    public Long getIdPedidoColeta() {
        return idPedidoColeta;
    }

    public void setIdPedidoColeta(Long idPedidoColeta) {
        this.idPedidoColeta = idPedidoColeta;
    }

}
