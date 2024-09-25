package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 *
 * @spring.bean id="lms.vendas.clienteNaoPagaPedagioService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/clientesNaoPagamPedagio.jasper"
 */
public class ClienteNaoPagaPedagioService extends ReportServiceSupport {

	private ParametroGeralService parametroGeralService;
	private List filiaisUsuario;
	
	/** 
	 * Método invocado pela classe clienteNaoPagaPedagioAction, é o método default do Struts
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		filiaisUsuario = buscaIdsFiliais(SessionUtils.getFiliaisUsuarioLogado());
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o formato do relatório selecionado na tela */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}
	
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception{
		
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("P.NM_PESSOA", "CLIENTE");
		sql.addProjection("P.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sql.addProjection("P.NR_IDENTIFICACAO", "CNPJ");
		sql.addProjection("U.NM_USUARIO", "PROMOTOR");
		sql.addProjection("DC.DS_DIVISAO_CLIENTE", "DIVISAO");
		sql.addProjection("F.SG_FILIAL","SG_FILIAL");
		sql.addProjection("PFIL.NM_FANTASIA", "NM_FILIAL");
		sql.addProjection("R.SG_REGIONAL", "SG_REGIONAL");
		sql.addProjection("R.DS_REGIONAL", "NM_REGIONAL");
		
		sql.addFrom("CLIENTE C " +
	                "   inner join PESSOA P                     on C.ID_CLIENTE = P.ID_PESSOA " +
	                "   inner join DIVISAO_CLIENTE DC           on C.ID_CLIENTE = DC.ID_CLIENTE " +
	                "   inner join TABELA_DIVISAO_CLIENTE TDC   on DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE " +
	                "   inner join SERVICO SER                  on SER.ID_SERVICO = TDC.ID_SERVICO " +	                
	                "   left outer join PROMOTOR_CLIENTE PRO    on C.ID_CLIENTE = PRO.ID_CLIENTE " +
	                "   left outer join USUARIO U               on PRO.ID_USUARIO = U.ID_USUARIO " +
	                "   inner join FILIAL F                     on C.ID_FILIAL_ATENDE_COMERCIAL = F.ID_FILIAL " +
	                "   inner join PESSOA PFIL                  on F.ID_FILIAL = PFIL.ID_PESSOA " +
	                "   left outer join REGIONAL R              on C.ID_REGIONAL_COMERCIAL = R.ID_REGIONAL " +
	                "   left outer join V_FUNCIONARIO VP        on U.ID_USUARIO = VP.ID_USUARIO");
		
		/*
		 * Não há pagamento de pedágio no caso do indicador ser por Valor e valor do pedágio ser 0 (zero)
		 * Não há pagamento de pedágio no caso do indicador ser por Desconto e valor do pedágio ser de 100 (cem porcento)
		 */
		sql.addCustomCriteria("TDC.ID_TABELA_DIVISAO_CLIENTE IN (" +
				              "SELECT PC.ID_TABELA_DIVISAO_CLIENTE FROM PARAMETRO_CLIENTE PC WHERE " +
				              "(  (PC.TP_INDICADOR_PEDAGIO = 'V' AND PC.VL_PEDAGIO = 0) " +
				              "OR (PC.TP_INDICADOR_PEDAGIO = 'D' AND PC.VL_PEDAGIO = 100)))");
		
		sql.addCustomCriteria("SER.tp_modal       = nvl(PRO.tp_modal,SER.tp_modal)");
		sql.addCustomCriteria("SER.tp_abrangencia = nvl(PRO.tp_abrangencia,SER.tp_abrangencia)");        		

		// c.tp_cliente <> F
		sql.addCriteria("C.TP_CLIENTE","<>",ConstantesVendas.CLIENTE_FILIAL);
		
		SQLUtils.joinExpressionsWithComma(filiaisUsuario,sql,"f.id_filial");
		
		/** Resgata o parametro  do request */
		String idRegional = tfm.getString("regional.idRegional");
		
		/** Verifica se o parametro não é nulo, caso não seja, adiciona o parametro como critério na consulta,
		 *  e seta o mesmo como um campo utilizado no filtro 
		 */
		if(StringUtils.isNotBlank(idRegional)) {
			sql.addCriteria("C.ID_REGIONAL_COMERCIAL", "=", idRegional, Long.class);
			sql.addFilterSummary("regional", tfm.getString("regional.siglaDescricao"));
		}
		
		String idFilial = tfm.getString("filial.idFilial");
		String sgFilial = tfm.getString("sgFilial");
		
		if(StringUtils.isNotBlank(idFilial)) {
			sql.addCriteria("C.ID_FILIAL_ATENDE_COMERCIAL", "=", idFilial);
			sql.addFilterSummary("filial", sgFilial);
		}
		
		String nmPromotor = tfm.getString("nmPromotor");
		String idUsuario = tfm.getString("usuario.idUsuario");
		
		if(StringUtils.isNotBlank(nmPromotor)){
			sql.addCriteria("PRO.ID_USUARIO", "=", idUsuario);
			sql.addFilterSummary("promotor", nmPromotor);
		}
		
		/** Variável que contém os códigos da função promotor separados por virgula para serem incluídos no critéria */ 
		String codigos = getParametroGeralService().findByNomeParametro("CD_PROMOTOR", false).getDsConteudo().replaceAll(";","','");
		String codigos2 = getParametroGeralService().findByNomeParametro("CD_PROMOTOR_2", false).getDsConteudo().replaceAll(";","','");
		if(!StringUtils.isBlank(codigos2)){
			codigos = StringUtils.isBlank(codigos) ? codigos2 : (codigos + "','" + codigos2);
		}

		sql.addCustomCriteria("( VP.CD_FUNCAO IS NULL OR VP.CD_FUNCAO IN ('" + codigos + "'))");
		
		sql.addOrderBy("R.SG_REGIONAL, R.DS_REGIONAL, " +
					   "F.SG_FILIAL,   PFIL.NM_FANTASIA, " +				       
				       "P.NM_PESSOA,   P.NR_IDENTIFICACAO, " +
				       "DC.DS_DIVISAO_CLIENTE, U.NM_USUARIO");
		
		return sql;
	}


	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}


	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
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