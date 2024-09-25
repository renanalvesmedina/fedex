package com.mercurio.lms.coleta.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração do Relatório de Manifestos Coleta Sem Baixa.
 * Especificação técnica 02.03.02.11
 * @author Christian Brunkow
 * 
 * @spring.bean id="lms.coleta.emitirRelatoriosColetasLiberadasDestinoBloqueadoService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirRelatoriosColetasLiberadasDestinoBloqueado.jasper"
 */

public class EmitirRelatoriosColetasLiberadasDestinoBloqueadoService extends ReportServiceSupport{

    /**
     * método responsável por gerar o relatório. 
     */
	
    public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap criteria = (TypedFlatMap) parameters;
    	
    	YearMonthDay dhInicial = criteria.getYearMonthDay("dhInicial"); 
    	YearMonthDay dhFinal = criteria.getYearMonthDay("dhFinal");
    	Long idManifesto = criteria.getLong("municipio.idMunicipio");
    	String nmMunicipio = criteria.getString("municipio.nmMunicipio");
    	
        SqlTemplate sql = this.mountSql(dhInicial, dhFinal, idManifesto, nmMunicipio);
        
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
        jr.setParameters(parametersReport);
        return jr;
    }
    
    private SqlTemplate mountSql(YearMonthDay dhInicial, YearMonthDay dhFinal, Long idMunicipio, String nmMunicipio) { 
    	
    	SqlTemplate sql = createSqlTemplate();
    	
    	//Projecao...
    	sql.addProjection("pedidoColeta.ID_PEDIDO_COLETA", "idPedidoColeta");
    	sql.addProjection("pessoa.ID_PESSOA", "idPessoa");
    	sql.addProjection("municipio.ID_MUNICIPIO", "idMunicipio");
    	sql.addProjection("municipio.NM_MUNICIPIO", "nmMunicipio");
    	sql.addProjection("pessoa.NR_IDENTIFICACAO", "nrIdentificacao");
    	sql.addProjection("pessoa.NM_PESSOA", "nmPessoa");
    	sql.addProjection("filial.SG_FILIAL", "sgFilial");
    	sql.addProjection("pedidoColeta.NR_COLETA", "nrColeta");
    	sql.addProjection("eventoColeta.DH_EVENTO", "dhEvento");
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("ocorrenciaColeta.DS_DESCRICAO_RESUMIDA_I"), "dsDescricacaoResumida");
    	sql.addProjection("eventoColeta.DS_DESCRICAO", "dsDescricao");
    	sql.addProjection("pessoaDestinatario.NM_PESSOA", "nmPessoaDestinatario");
    	sql.addProjection("usuarioLiberacao.NM_USUARIO", "nmPessoaLiberacao");
    	sql.addProjection("usuarioRegistro.NM_USUARIO", "nmPessoaRegistro");
    	     	
    	//From...
    	sql.addFrom("PEDIDO_COLETA", "pedidoColeta");
    	sql.addFrom("EVENTO_COLETA", "eventoColeta");
    	sql.addFrom("DETALHE_COLETA", "detalheColeta");
    	sql.addFrom("MUNICIPIO", "municipio");
    	sql.addFrom("OCORRENCIA_COLETA", "ocorrenciaColeta");
    	sql.addFrom("FILIAL", "filial");
    	sql.addFrom("PESSOA", "pessoa");
    	sql.addFrom("PESSOA", "pessoaDestinatario");
    	sql.addFrom("USUARIO", "usuarioRegistro");
    	sql.addFrom("USUARIO", "usuarioLiberacao");
    	
    	//Joins
    	sql.addJoin("pedidoColeta.ID_CLIENTE", "pessoa.ID_PESSOA(+)");
    	sql.addJoin("pedidoColeta.ID_FILIAL_RESPONSAVEL", "filial.ID_FILIAL");
    	sql.addJoin("pedidoColeta.ID_PEDIDO_COLETA", "eventoColeta.ID_PEDIDO_COLETA");
    	sql.addJoin("eventoColeta.ID_OCORRENCIA_COLETA", "ocorrenciaColeta.ID_OCORRENCIA_COLETA");
    	sql.addJoin("eventoColeta.ID_DETALHE_COLETA", "detalheColeta.ID_DETALHE_COLETA");
    	sql.addJoin("detalheColeta.ID_MUNICIPIO", "municipio.ID_MUNICIPIO");
    	sql.addJoin("detalheColeta.ID_CLIENTE", "pessoaDestinatario.ID_PESSOA(+)");
    	sql.addJoin("eventoColeta.ID_USUARIO", "usuarioLiberacao.ID_USUARIO(+)");
    	sql.addJoin("pedidoColeta.ID_USUARIO", "usuarioRegistro.ID_USUARIO(+)");
    	    	
    	//Criteria...
    	sql.addCriteria("TRUNC (CAST(eventoColeta.dh_Evento AS DATE))", ">=", dhInicial);
    	if (dhInicial!=null)sql.addFilterSummary("periodoInicial",  JTFormatUtils.format(dhInicial));

		sql.addCriteria("TRUNC (CAST(eventoColeta.dh_Evento AS DATE))", "<=", dhFinal);
		if (dhFinal!=null)sql.addFilterSummary("periodoFinal",  JTFormatUtils.format(dhFinal));
		
		sql.addCriteria("eventoColeta.TP_EVENTO_COLETA", "=", "LI");
		
    	sql.addCriteria("municipio.ID_MUNICIPIO", "=", idMunicipio);    
    	if (nmMunicipio!=null)sql.addFilterSummary("municipio",  nmMunicipio);
    	
    	sql.addOrderBy("nmMunicipio");
    	sql.addOrderBy("sgFilial"); 
    	sql.addOrderBy("nrColeta"); 
    	
        
        return sql;
    }
}
