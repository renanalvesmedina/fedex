package com.mercurio.lms.coleta.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração do Relatório de Eventos Coleta.
 * Especificação técnica 12.02.01.01
 * 
 * @spring.bean id="lms.coleta.relatorioEventosColetaService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/consultarEventosColeta.jasper"
 */

public class RelatorioEventosColetaService extends ReportServiceSupport {

	private PedidoColetaService pedidoColetaService;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
        SqlTemplate sql = mountSql(parameters);
        
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
     * Monta o sql do relatório
     * @param parameters
     * @return
     * @throws Exception
     */     
    private SqlTemplate mountSql(Map parameters) throws Exception{
  
        // Select que irá conter a consulta real
        SqlTemplate sqlInterno = createSqlTemplate();
        
        // Adiciona os campos de retorno do select
        sqlInterno.addProjection(PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I"),"Evento");
        sqlInterno.addProjection("EC.DH_EVENTO","DATAHORA");
        sqlInterno.addProjection(PropertyVarcharI18nProjection.createProjection("OC.DS_DESCRICAO_RESUMIDA_I"),"OCORRENCIA");
        sqlInterno.addProjection(PropertyVarcharI18nProjection.createProjection("OC.DS_DESCRICAO_COMPLETA_I"),"DESCRICAO");
        sqlInterno.addProjection("US.NM_USUARIO","USUARIO");
        sqlInterno.addProjection("MT.NR_FROTA","FROTA");
        
        // Adiciona as tabelas necessárias
        sqlInterno.addFrom("EVENTO_COLETA","EC");
        sqlInterno.addFrom("PEDIDO_COLETA","PC");
        sqlInterno.addFrom("DOMINIO","DM");
        sqlInterno.addFrom("VALOR_DOMINIO","VD");
        sqlInterno.addFrom("MEIO_TRANSPORTE","MT");
        sqlInterno.addFrom("OCORRENCIA_COLETA","OC");
        sqlInterno.addFrom("USUARIO","US");
        
        sqlInterno.addJoin("PC.ID_PEDIDO_COLETA","EC.ID_PEDIDO_COLETA");
        sqlInterno.addJoin("EC.ID_USUARIO","US.ID_USUARIO");
        sqlInterno.addJoin("EC.ID_OCORRENCIA_COLETA","OC.ID_OCORRENCIA_COLETA");
        sqlInterno.addJoin("EC.ID_MEIO_TRANSPORTE","MT.ID_MEIO_TRANSPORTE(+)");
        sqlInterno.addJoin("DM.NM_DOMINIO","'DM_TIPO_EVENTO_COLETA'");
        sqlInterno.addJoin("VD.ID_DOMINIO","DM.ID_DOMINIO");
        sqlInterno.addJoin("VD.VL_VALOR_DOMINIO","EC.TP_EVENTO_COLETA");
        
        Map pedidoColetaMap = (Map)parameters.get("pedidoColeta");
        String idPedidoColeta = (String)pedidoColetaMap.get("idPedidoColeta");
        Map filialByIdFilialResponsavel = (Map)pedidoColetaMap.get("filialByIdFilialResponsavel");
        String idFilial = (String)filialByIdFilialResponsavel.get("idFilial");
        	
        if(idPedidoColeta != null) {
            sqlInterno.addCriteria("EC.ID_PEDIDO_COLETA", "=", idPedidoColeta.toString(), Long.class);
        }

        if(idFilial != null) {
            sqlInterno.addCriteria("PC.ID_FILIAL_RESPONSAVEL", "=", idFilial.toString(), Long.class);
        }
        
        PedidoColeta pedidoColeta = getPedidoColetaService().findById(Long.valueOf(idPedidoColeta));
        
    	sqlInterno.addFilterSummary("coleta", FormatUtils.formatSgFilialWithLong(pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial(), pedidoColeta.getNrColeta()));
        
        sqlInterno.addOrderBy("EC.DH_EVENTO");
        return sqlInterno;        
    }
    
	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
    
}