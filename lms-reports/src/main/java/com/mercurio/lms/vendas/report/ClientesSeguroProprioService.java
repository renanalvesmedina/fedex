package com.mercurio.lms.vendas.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.core.util.VarcharI18nUtil;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.hibernate.OrderI18n;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.clientesSeguroProprioService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/clientesSeguroProprio.jasper"
 */
public class ClientesSeguroProprioService extends ReportServiceSupport {
	private DomainService domainService;
	private List filiaisUsuario;

	public JRReportDataObject execute(Map parameters) throws Exception {
		filiaisUsuario = buscaIdsFiliais(SessionUtils.getFiliaisUsuarioLogado());

		SqlTemplate sql = getSqlTemplate(parameters);
		List list = getJdbcTemplate().query(
			sql.getSql(),
			JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()),
			new RowMapper() {

				private String nrIdentificacaoAnterior = "";
				/**
				 * Se o número de identificação do cliente for diferente do cliente anterior, deve mostrar no relatório. 
				 * @param nrIdentificacao
				 * @return
				 */
				private boolean validatePrintCliente(String nrIdentificacao){
					boolean isValid = !(nrIdentificacaoAnterior.equalsIgnoreCase(nrIdentificacao));
					nrIdentificacaoAnterior = nrIdentificacao;
					return isValid;
				}

				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					Map map = new HashMap();
					String nmCliente = rs.getString(1);
					String nrIdentificacao = rs.getString(2);

					/*
					 * Verifica se deve mostrar o nome do cliente, na linha corrente.
					 */
					if (validatePrintCliente(nrIdentificacao)){
						map.put("CLIENTE",nmCliente);	
					}else{
						map.put("CLIENTE","");
					}

					map.put("NRIDENTIFICACAO",nrIdentificacao);
					map.put("MODAL", VarcharI18nUtil.createVarcharI18n(rs.getObject(3)).getValue());
					map.put("ABRANGENCIA", VarcharI18nUtil.createVarcharI18n(rs.getObject(4)).getValue() );
					map.put("TIPOSEGURO",rs.getString(5));
					map.put("TPIDENTIFICACAO",rs.getString(6));
					map.put("REGIONAL",rs.getString(7));
					map.put("FILIAL",rs.getString(8));
					map.put("CARTAANEXADA",rs.getString(9));
					return map;
				}
			}
		);
		JRReportDataObject jr = createReportDataObject( new JRMapCollectionDataSource(list) , new HashMap() );
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		SqlTemplate sql = this.createSqlTemplate();

		sql.addFrom(
				new StringBuffer()
				.append("cliente c ")
				.append("inner join pessoa pc on pc.id_pessoa = c.id_cliente ")
				.append("inner join seguro_cliente sc on sc.id_cliente = c.id_cliente ")
				.append("inner join tipo_seguro ts on ts.id_tipo_seguro = sc.id_tipo_seguro ")
				.append("inner join filial f on f.id_filial = c.id_filial_atende_comercial ")
				.append("inner join pessoa pf on pf.id_pessoa = f.id_filial ")
				.append("left outer join regional r on r.id_regional = c.id_regional_comercial")
				.toString()
				);

		//Para dominio Abrangencia
		sql.addFrom("dominio domA inner join valor_dominio vdA on vdA.id_dominio = domA.id_dominio");

		//Para dominio Modal
		sql.addFrom("dominio domM inner join valor_dominio vdM on vdM.id_dominio = domM.id_dominio");

		sql.addProjection(
				new StringBuffer()
				.append("pc.nm_pessoa CLIENTE,")
				.append("pc.nr_identificacao NRIDENTIFICACAO,")
				.append(PropertyVarcharI18nProjection.createProjection("vdM.ds_valor_dominio_i")).append("as MODAL,")
				.append(PropertyVarcharI18nProjection.createProjection("vdA.ds_valor_dominio_i")).append("as ABRANGENCIA,")
				.append("ts.sg_tipo TIPOSEGURO, ")
				.append("pc.tp_identificacao TPIDENTIFICACAO, ")
				.append("case when r.sg_regional is null or r.sg_regional = '' then '' else r.sg_regional || ' - ' || r.ds_regional end REGIONAL, ")
				.append("(f.sg_filial || ' - ' || pf.nm_fantasia) FILIAL, case when dc_carta_isencao is null then 'n' else 's' end CARTAANEXADA ")
				.toString()
		);

		SQLUtils.joinExpressionsWithComma(filiaisUsuario,sql,"f.id_filial");

		Long idRegional = tfm.getLong("regional.idRegional");
		if (idRegional != null){
			sql.addCriteria("c.id_regional_comercial", "=", idRegional);
			sql.addFilterSummary("regional",tfm.getString("regional.siglaDescricao"));
		}

		Long idFilial = tfm.getLong("filial.idFilial");
		if (idFilial != null){
			sql.addCriteria("c.id_filial_atende_comercial", "=", idFilial);
			sql.addFilterSummary("filial", tfm.getString("sgFilial"));
		}

		/* Alterados filtros current_date() */ 
		YearMonthDay dataAtual = null;

		if(tfm.getYearMonthDay("dtReferencia") != null) {
			dataAtual = tfm.getYearMonthDay("dtReferencia");
		} else {
			dataAtual = JTDateTimeUtils.getDataAtual();
		}
		sql.addFilterSummary("dataReferencia", dataAtual);

		sql.addCriteria("sc.dt_vigencia_inicial", "<=", dataAtual);
		sql.addCriteria("sc.dt_vigencia_final", ">=", dataAtual);

		sql.addCriteria("domA.nm_dominio","=","DM_ABRANGENCIA");
		sql.addCriteria("domM.nm_dominio","=","DM_MODAL");
		sql.addJoin("vdA.vl_valor_dominio","sc.tp_abrangencia");
		sql.addJoin("vdM.vl_valor_dominio","sc.tp_modal");

		sql.addOrderBy("f.sg_filial");
		sql.addOrderBy("pc.nm_pessoa");
		sql.addOrderBy("pc.nr_identificacao");

		sql.addOrderBy( OrderI18n.hqlOrder("vdA.ds_valor_dominio", LocaleContextHolder.getLocale()) );
		sql.addOrderBy( OrderI18n.hqlOrder("vdM.ds_valor_dominio",LocaleContextHolder.getLocale()) );

		sql.addOrderBy("ts.sg_tipo");

		return sql;
	}

	public DomainService getDomainService() {
		return domainService;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
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
