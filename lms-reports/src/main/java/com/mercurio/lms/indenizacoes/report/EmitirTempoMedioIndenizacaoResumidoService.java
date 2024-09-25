package com.mercurio.lms.indenizacoes.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração da especificação técnica 21.04.01.04
 * @author Rodrigo Antunes
 * 
 * @spring.bean id="lms.indenizacoes.emitirTempoMedioIndenizacaoResumidoService"
 * @spring.property name="reportName" value="com/mercurio/lms/indenizacoes/report/emitirRelatorioTempoMedioIndenizacaoResumido.jasper"
 */
public class EmitirTempoMedioIndenizacaoResumidoService extends EmitirTempoMedioIndenizacao {

	public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap tfm = (TypedFlatMap)parameters; 
        SqlTemplate sql = createMainSql(tfm);
        
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
        
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        jr.setParameters(parametersReport);
        return jr; 
	}

	private SqlTemplate createMainSql(TypedFlatMap tfm) {
		SqlTemplate sql = createSqlTemplate();
		
		addProjection(sql);
        addFrom(sql);
        addWhere(sql);
        addCriteria(sql, tfm);
        addOrderBy(sql);        

		return sql;
	}

	@Override
	protected void addProjection(SqlTemplate sql) {
		sql.addProjection("regional.SG_REGIONAL","SG_REGIONAL");
		sql.addProjection("f_rim.ID_FILIAL","ID_FILIAL_RIM");
		sql.addProjection("f_rim.SG_FILIAL","SG_FILIAL_RIM");
		sql.addProjection("rim.TP_INDENIZACAO","TP_INDENIZACAO");
		sql.addProjection("rim.TP_INDENIZACAO","TP_INDENIZACAO_VALUE");
		
		sql.addProjection(
                "CASE\n" +
                "\t\tWHEN TP_INDENIZACAO = 'NC' THEN "+PropertyVarcharI18nProjection.createProjection("manc.DS_MOTIVO_ABERTURA_I")+" \n" +
                "\t\tWHEN TP_INDENIZACAO = 'PS' THEN ts.SG_TIPO\n" +
                "\tEND as DESC_TIPO_INDENIZACAO");		
		sql.addProjection("rim.ID_RECIBO_INDENIZACAO","ID_RECIBO_INDENIZACAO");
		
		sql.addProjection("rim.NR_RECIBO_INDENIZACAO","NR_RECIBO_INDENIZACAO");
		sql.addProjection("beneficiario.NM_PESSOA","NM_BENEFICIARIO");
		sql.addProjection("favorecido.NM_PESSOA","NM_FAVORECIDO");
		sql.addProjection("ps.NR_PROCESSO_SINISTRO","NR_PROCESSO_SINISTRO");
		sql.addProjection("rim.DT_GERACAO","DT_GERACAO");
		sql.addProjection("rim.DT_PAGAMENTO_EFETUADO","DT_PAGAMENTO_EFETUADO");
		sql.addProjection("mrim.DS_SIMBOLO","DS_SIMBOLO_RIM");
		sql.addProjection("mrim.SG_MOEDA","SG_MOEDA_RIM");
		sql.addProjection("mrim.ID_MOEDA","ID_MOEDA_RIM");
		sql.addProjection("rim.VL_INDENIZACAO","VL_INDENIZACAO");
		//-- docto servico
		sql.addProjection("ds.VL_MERCADORIA","VL_MERCADORIA");
		sql.addProjection("fo_ds.SG_FILIAL","SG_FILIAL_DOCTO");
		sql.addProjection("ds.ID_DOCTO_SERVICO","ID_DOCTO_SERVICO");
		sql.addProjection("ds.NR_DOCTO_SERVICO","NR_DOCTO_SERVICO");
		sql.addProjection("ds.TP_DOCUMENTO_SERVICO","TP_DOCUMENTO_SERVICO");
		sql.addProjection("servico.TP_MODAL","MODAL");
		sql.addProjection("servico.TP_ABRANGENCIA","ABRANGENCIA");
		sql.addProjection("pr.NM_PESSOA","REMETENTE");
		sql.addProjection("pd.NM_PESSOA","DESTINATARIO");
		sql.addProjection("mds.DS_SIMBOLO","DS_SIMBOLO_DS");
		sql.addProjection("mds.SG_MOEDA","SG_MOEDA_DS");
		sql.addProjection("mds.ID_MOEDA","ID_MOEDA_DS");
		sql.addProjection("ds.VL_MERCADORIA","VL_MERCADORIA"); 
		sql.addProjection("fnc.SG_FILIAL","FILIAL_RNC");
		sql.addProjection("nc.NR_NAO_CONFORMIDADE","NR_NAO_CONFORMIDADE");
		sql.addProjection("nc.ID_NAO_CONFORMIDADE","ID_NAO_CONFORMIDADE");
		sql.addProjection("dsi.ID_DOCTO_SERVICO_INDENIZACAO","ID_DOCTO_SERVICO_INDENIZACAO");
		sql.addProjection("rim.DT_PAGAMENTO_EFETUADO - TRUNC(CAST(ds.DH_EMISSAO AS DATE)) as DIAS_DESDE_EMISSAO_DOC");
		
        sql.addProjection(
                "CASE\n" +
                "\t\tWHEN TP_INDENIZACAO = 'NC' THEN rim.DT_PAGAMENTO_EFETUADO - TRUNC(CAST(nc.DH_INCLUSAO AS DATE))\n" +
                "\t\tWHEN TP_INDENIZACAO = 'PS' THEN rim.DT_PAGAMENTO_EFETUADO - TRUNC(CAST(ps.DH_ABERTURA AS DATE))\n" +
                "\tEND as DIAS_DESDE_GER_RNC");
		
	}
}