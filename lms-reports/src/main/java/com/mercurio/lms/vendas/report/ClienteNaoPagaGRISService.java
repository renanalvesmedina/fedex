package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 *
 * @spring.bean id="lms.vendas.clienteNaoPagaGRISService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/clientesNaoPagamGRIS.jasper"
 */
public class ClienteNaoPagaGRISService extends ReportServiceSupport {
	
	private List filiaisUsuario;

	public JRReportDataObject execute(Map parameters) throws Exception {		

		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		filiaisUsuario = buscaIdsFiliais(SessionUtils.getFiliaisUsuarioLogado());
		
		SqlTemplate sql = mountSql(tfm);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		
		jr.setParameters(parametersReport);
		
		return jr; 		
	
	}
	
	
	/**
	 * Monta o sql principal
	 * @param tfm Critérios da pesquisa
	 * @return Sql montado
	 * @throws Exception
	 */		
	private SqlTemplate mountSql(TypedFlatMap tfm) throws Exception{
		
		SqlTemplate sql = this.createSqlTemplate();
        
        sql.addProjection("C.ID_CLIENTE ID_CLIENTE, " +
        		          "P.NM_PESSOA CLIENTE, " +
		                  "P.TP_IDENTIFICACAO TP_IDENTIFICACAO, " +
			              "P.NR_IDENTIFICACAO IDENTIFICACAO, " +
                          "DC.DS_DIVISAO_CLIENTE DIVISAO, " +
                          "U.NM_USUARIO PROMOTOR, " +
                          "F.ID_FILIAL, " +
                          "F.SG_FILIAL, " +
                          "PF.NM_FANTASIA NM_FILIAL, " +
                          "R.SG_REGIONAL, " +
                          "R.DS_REGIONAL NM_REGIONAL");
        
        sql.addFrom("CLIENTE C " +
                    "   inner join PESSOA P                     on C.ID_CLIENTE = P.ID_PESSOA " +
                    "   inner join DIVISAO_CLIENTE DC           on C.ID_CLIENTE = DC.ID_CLIENTE " +
                    "   inner join TABELA_DIVISAO_CLIENTE TDC   on DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE " +
                    "   inner join SERVICO S                    on S.id_servico = TDC.id_servico " +                    
                    "   left outer join PROMOTOR_CLIENTE PRO    on C.ID_CLIENTE = PRO.ID_CLIENTE " +
                    "   left outer join USUARIO U               on PRO.ID_USUARIO = U.ID_USUARIO " +
                    "   inner join FILIAL F                     on C.ID_FILIAL_ATENDE_COMERCIAL = F.ID_FILIAL " +
                    "   inner join PESSOA PF                    on F.ID_FILIAL = PF.ID_PESSOA " +
                    "   left outer join REGIONAL R              on C.ID_REGIONAL_COMERCIAL = R.ID_REGIONAL");
        
		sql.addOrderBy("R.SG_REGIONAL, R.DS_REGIONAL, " +
				       "F.SG_FILIAL, PF.NM_FANTASIA, " +				       
				       "P.NM_PESSOA, P.NR_IDENTIFICACAO, " +
				       "DC.DS_DIVISAO_CLIENTE, U.NM_USUARIO");
        
		sql.addCustomCriteria("TDC.ID_TABELA_DIVISAO_CLIENTE IN (" +
				              "SELECT PC.ID_TABELA_DIVISAO_CLIENTE FROM PARAMETRO_CLIENTE PC WHERE " +
				              "((PC.TP_INDICADOR_MINIMO_GRIS     = 'V' AND PC.VL_MINIMO_GRIS     = 0) " +
				              " OR  (PC.TP_INDICADOR_MINIMO_GRIS     = 'D' AND PC.VL_MINIMO_GRIS     = 100)) " +
				              "AND ((PC.TP_INDICADOR_PERCENTUAL_GRIS = 'V' AND PC.VL_PERCENTUAL_GRIS = 0) " +
				              " OR  (PC.TP_INDICADOR_PERCENTUAL_GRIS = 'D' AND PC.VL_PERCENTUAL_GRIS = 100)))");
		
		sql.addCustomCriteria("S.tp_modal       = nvl(PRO.tp_modal,S.tp_modal)");
		sql.addCustomCriteria("S.tp_abrangencia = nvl(PRO.tp_abrangencia,S.tp_abrangencia)");

		// c.tp_cliente <> F
		sql.addCriteria("C.TP_CLIENTE","<>",ConstantesVendas.CLIENTE_FILIAL);
		
		SQLUtils.joinExpressionsWithComma(filiaisUsuario,sql,"f.id_filial");
		
		Long idRegional = tfm.getLong("regional.idRegional");
		if(idRegional != null && idRegional.longValue() > 0){
			sql.addCriteria("c.id_regional_comercial","=",tfm.getLong("regional.idRegional"));
			sql.addFilterSummary("regional", tfm.getString("regional.siglaDescricao"));
		}
		
		Long idFilial = tfm.getLong("filial.idFilial");
		if(idFilial != null && idFilial.longValue() > 0){
			sql.addCriteria("c.id_filial_atende_comercial","=",tfm.getLong("filial.idFilial"));
			sql.addFilterSummary("filial", tfm.getString("sgFilial"));
		}
		
		Long idUsuario = tfm.getLong("usuario.idUsuario");
		if(idUsuario != null && idUsuario.longValue() > 0){
			sql.addCriteria("pro.id_usuario","=",tfm.getLong("usuario.idUsuario"));
			sql.addFilterSummary("promotor", tfm.getString("usuario.nmUsuario"));
		}		
		
		return sql;
	}
	
	private List buscaIdsFiliais(List filiaisUsuarioLogado) {
		List retorno = new ArrayList();
		
		for (Iterator iter = filiaisUsuarioLogado.iterator(); iter.hasNext();) {
			Filial f = (Filial) iter.next();
			retorno.add(f.getIdFilial());
		}
		
		return retorno;
	}

}