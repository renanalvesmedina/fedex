package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Robson Edemar Gehl
 *
 * @spring.bean id="lms.vendas.clientesPromotorService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/clientesPromotor.jasper"
 */
public class ClientesPromotorService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {		
		TypedFlatMap tfm = (TypedFlatMap) parameters;

		validatePeriodoDatas(tfm.getYearMonthDay("dtPeriodoInicial"),tfm.getYearMonthDay("dtPeriodoFinal"));

		String[] tpModal = {tfm.getString("tpModal")};
		String[] tpAbrangencia = {tfm.getString("tpAbrangencia")};

		/*
		 * Para cada par de modal e abrangencia é realizada uma consulta.
		 * Quando não informado filtro, o total de consultas é 4.
		 * Para mais de uma consulta é realizado um UNION. 
		 * Por isto que é criado um array de Object, contendo o SqlTemplate das consultas, para concatenar as consultas com UNION.
		 */

		if (StringUtils.isBlank(tfm.getString("tpModal"))){
			tpModal = new String[]{"R", "A"};
		}

		if (StringUtils.isBlank(tfm.getString("tpAbrangencia"))){
			tpAbrangencia = new String[]{"I", "N"};
		}

		SqlTemplate sql = mountSql(tfm, tpModal, tpAbrangencia);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));

		SqlTemplate sqlTemplate = createFilterSummary(tfm);
		parametersReport.put("parametrosPesquisa",sqlTemplate.getFilterSummary());

		jr.setParameters(parametersReport);

		return jr; 		
	}

	/**
	 * Configura Dominios do relatório
	 */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_MODAL","DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA","DM_ABRANGENCIA");
	}

	/**
	 * Cria os filtros para o relatório 
	 * @param map
	 * @return
	 */
	private SqlTemplate createFilterSummary(Map map){
		SqlTemplate sqlTemplate = this.createSqlTemplate();

		TypedFlatMap tfm = (TypedFlatMap) map;
		sqlTemplate.addFilterSummary("regional", tfm.getString("regional.siglaDescricao"));

		if(StringUtils.isNotBlank(tfm.getString("sgFilial"))) {
			sqlTemplate.addFilterSummary("filial", tfm.getString("sgFilial") );	
		}

		sqlTemplate.addFilterSummary("modal", tfm.getString("modal.descricao"));
		sqlTemplate.addFilterSummary("abrangencia", tfm.getString("abrangencia.descricao"));
		sqlTemplate.addFilterSummary("servico", tfm.getString("dsServico"));
		sqlTemplate.addFilterSummary("promotor", tfm.getString("usuario.nmUsuario"));

		if (StringUtils.isNotBlank(tfm.getString("dtPeriodoInicial"))){
			sqlTemplate.addFilterSummary("periodoInicial", 
					JTDateTimeUtils.convertFrameworkDateToFormat( tfm.getString("dtPeriodoInicial"), "dd/MM/yyyy") );
		}
		if (StringUtils.isNotBlank(tfm.getString("dtPeriodoFinal"))){
			sqlTemplate.addFilterSummary("periodoFinal", 
				JTDateTimeUtils.convertFrameworkDateToFormat( tfm.getString("dtPeriodoFinal"), "dd/MM/yyyy") );
		}
		return sqlTemplate;
	}

	/**
	 * SQL para cada consulta, filtrando sempre por Modal e Abrangencia.<BR> 
	 * @param tfm
	 * @param modal
	 * @param abrangencia
	 * @return
	 */
	private SqlTemplate mountSql(TypedFlatMap tfm, String[] modal, String[] abrangencia){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(new StringBuilder()
				.append("c.id_regional_comercial, ")
				.append("c.id_filial_atende_comercial, ")
				.append("pc.id_usuario, ")
				.append("usuarioPromotor.nm_usuario PROMOTOR, ")
				.append("filialPromotor.sg_filial SG_FILIAL, ")
				.append("pessoaPromotor.nm_fantasia NM_FILIAL, ")
				.append("regionalPromotor.sg_regional SG_REGIONAL, ")
				.append("regionalPromotor.ds_regional NM_REGIONAL,")
				.append("pessoaCliente.nm_pessoa CLIENTE, ")
				.append("pessoaCliente.tp_identificacao TP_IDENTIFICACAO, ")
				.append("pessoaCliente.nr_identificacao CNPJ, ")
				.append("pc.dt_inicio_promotor DATAINICIOPROMOTOR, ")
				.append("filialCliente.sg_filial FILIALCLIENTE, ")
				.append("pc.ID_PROMOTOR_CLIENTE, pc.tp_modal TP_MODAL, pc.tp_abrangencia TP_ABRANGENCIA").toString()
		);

		sql.addFrom(new StringBuilder()
				.append("promotor_cliente pc ")
				.append("inner join usuario usuarioPromotor on usuarioPromotor.id_usuario = pc.id_usuario ")
				.append("inner join v_funcionario fun on fun.id_usuario = usuarioPromotor.id_usuario ")
				.append("left outer join filial filialPromotor on filialPromotor.id_filial = fun.id_filial ")
				.append("left outer join regional_filial regionalFilialPromotor on regionalFilialPromotor.id_filial = filialPromotor.id_filial ")
				.append("left outer join regional regionalPromotor on regionalPromotor.id_regional = regionalFilialPromotor.id_regional ")
				//Clientes
				.append("inner join cliente c on c.id_cliente = pc.id_cliente ")
				.append("left outer join pessoa pessoaPromotor on pessoaPromotor.id_pessoa = filialPromotor.id_filial ")
				.append("inner join pessoa pessoaCliente on pessoaCliente.id_pessoa = c.id_cliente ")
				.append("inner join filial filialCliente on c.id_filial_atende_comercial = filialCliente.id_filial ").toString()
		);

		sql.addCriteria("regionalPromotor.id_regional","=", tfm.getLong("regional.idRegional"));
		sql.addCriteria("fun.id_filial","=", tfm.getLong("filial.idFilial"));

		sql.addCriteriaIn("(pc.tp_modal", modal, "or pc.tp_modal is null)");
		sql.addCriteriaIn("(pc.tp_abrangencia", abrangencia, "or pc.tp_abrangencia is null)");

		sql.addCriteria("pc.dt_inicio_promotor",">=", tfm.getYearMonthDay("dtPeriodoInicial"));
		sql.addCriteria("pc.dt_inicio_promotor","<=", tfm.getYearMonthDay("dtPeriodoFinal"));

		sql.addCriteria("usuarioPromotor.id_usuario","=", tfm.getLong("usuario.idUsuario"));

		sql.addOrderBy("TP_MODAL, TP_ABRANGENCIA, PROMOTOR, SG_REGIONAL, NM_REGIONAL, SG_FILIAL, NM_FILIAL, CLIENTE, CNPJ, DATAINICIOPROMOTOR");

		return sql;
	}

	 /**
	 * Verifica o período de datas informado. A diferença não pode ultrapassar 31 dias
	 *
	 * @author José Rodrigo Moraes
	 * @since 13/12/2006
	 *
	 * @param tfm
	 * @return
	 */
	private void validatePeriodoDatas(YearMonthDay dtPeriodoInicial, YearMonthDay dtPeriodoFinal) {
		if( dtPeriodoFinal != null && dtPeriodoInicial != null ){
			if( JTDateTimeUtils.getIntervalInDays(dtPeriodoInicial,dtPeriodoFinal) > 31 ){
				throw new BusinessException("LMS-10045");
			}
		}
	}
}
