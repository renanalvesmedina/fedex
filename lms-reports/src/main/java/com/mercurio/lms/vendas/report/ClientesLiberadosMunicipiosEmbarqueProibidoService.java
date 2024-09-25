package com.mercurio.lms.vendas.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 *
 * @spring.bean id="lms.vendas.clientesLiberadosMunicipiosEmbarqueProibido"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/clientesLiberadosMunicipiosEmbarqueProibido.jasper"
 */
public class ClientesLiberadosMunicipiosEmbarqueProibidoService extends ReportServiceSupport {
	private List filiaisUsuario;
	private DomainService domainService;

	/** 
	 * Método invocado pela classe ClientesLiberadosMunicipiosEmbarqueProibidoAction, é o método default do Struts
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;

		filiaisUsuario = buscaIdsFiliais(SessionUtils.getFiliaisUsuarioLogado());
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);

		List list = getJdbcTemplate().query(sql.getSql(), sql.getCriteria(), new RowMapper() {
			private Domain modal = getDomainService().findByName("DM_MODAL");
			private String nrIdentificacaoAnterior = "";

			/**
			 * Se o número de identificação do cliente for diferente do cliente anterior, deve mostrar no relatório.
			 * 
			 * @param nrIdentificacao
			 * @return
			 */
			private boolean validatePrintCliente(String nrIdentificacao) {
				boolean isValid = !(nrIdentificacaoAnterior.equalsIgnoreCase(nrIdentificacao));
				nrIdentificacaoAnterior = nrIdentificacao;
				return isValid;
			}

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map map = new HashMap();

				String nmCliente = rs.getString(1);
				String tpIdentificacao = rs.getString(2);
				String nrIdentificacao = rs.getString(3);

				if (validatePrintCliente(nrIdentificacao)) {
					map.put("CLIENTE", nmCliente);
				} else {
					map.put("CLIENTE", "");
				}

				String strModal = rs.getString(4);
				if (strModal != null){
					strModal = modal.findDomainValueByValue(strModal).getDescription().getValue();
				}

				map.put("TIPO", tpIdentificacao);
				map.put("CNPJ", nrIdentificacao);
				map.put("MODAL", strModal);
				map.put("MUNICIPIOLIB", rs.getString(5));
				map.put("FILIAL", rs.getString(6));
				map.put("REGIONAL", rs.getString(7));
				return map;
			}
		});

		JRReportDataObject jr = createReportDataObject(new JRMapCollectionDataSource(list), new HashMap());
		Map parametersReport = new HashMap();

		/** Adiciona o formato do relatório selecionado na tela */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());

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
		sql.addProjection("P.TP_IDENTIFICACAO", "TIPO");
		sql.addProjection("P.NR_IDENTIFICACAO", "CNPJ");
		sql.addProjection("LE.TP_MODAL", "MODAL");
		sql.addProjection("M.NM_MUNICIPIO", "MUNICIPIOLIB");
		sql.addProjection("F.SG_FILIAL || ' - ' || P1.NM_FANTASIA", "FILIAL");  
		sql.addProjection("R.SG_REGIONAL || ' - ' || R.DS_REGIONAL", "REGIONAL");

		sql.addFrom("CLIENTE","C");
		sql.addFrom("PESSOA","P");
		sql.addFrom("PESSOA","P1");
		sql.addFrom("LIBERACAO_EMBARQUE","LE");
		sql.addFrom("MUNICIPIO","M");
		sql.addFrom("FILIAL","F");
		sql.addFrom("REGIONAL","R");

		sql.addJoin("C.ID_CLIENTE","P.ID_PESSOA");
		sql.addJoin("C.ID_CLIENTE","LE.ID_CLIENTE");
		sql.addJoin("LE.ID_MUNICIPIO","M.ID_MUNICIPIO");
		sql.addJoin("C.ID_FILIAL_ATENDE_COMERCIAL","F.ID_FILIAL");
		sql.addJoin("F.ID_FILIAL", "P1.ID_PESSOA");
		sql.addJoin("C.ID_REGIONAL_COMERCIAL","R.ID_REGIONAL");

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

		SQLUtils.joinExpressionsWithComma(filiaisUsuario,sql,"f.id_filial");

		if(StringUtils.isNotBlank(idFilial)) {
			sql.addCriteria("C.ID_FILIAL_ATENDE_COMERCIAL", "=", idFilial);
			sql.addFilterSummary("filial", sgFilial);
		}

		sql.addOrderBy("R.SG_REGIONAL, F.SG_FILIAL, P.NM_PESSOA, P.NR_IDENTIFICACAO, LE.TP_MODAL, M.NM_MUNICIPIO");

		return sql;
	}

	private List buscaIdsFiliais(List<Filial> filiaisUsuarioLogado) {
		List<Long> retorno = new ArrayList<Long>();
		for (Filial filial : filiaisUsuarioLogado) {
			retorno.add(filial.getIdFilial());
		}
		return retorno;
	}

	public DomainService getDomainService() {
		return domainService;
	}
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}
}