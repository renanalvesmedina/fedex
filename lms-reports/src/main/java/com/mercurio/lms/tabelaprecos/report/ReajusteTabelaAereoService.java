package com.mercurio.lms.tabelaprecos.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andre Valadas
 *
 * @spring.bean id="lms.tabelaprecos.reajustesTabelasAereo"
 * @spring.property name="reportName" value="com/mercurio/lms/tabelaprecos/report/relatorioReajustesTabelasAereo.jasper"
 */
public class ReajusteTabelaAereoService extends ReportServiceSupport {

	/**
	 * Método execute chamado pela Action
	 */ 
	public JRReportDataObject execute(Map parameters) throws Exception {

		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, ((TypedFlatMap) parameters).getString("tpFormatoRelatorio"));
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		jr.setParameters(parametersReport);
		return jr;
	}

	/** 
	 * Consulta SQL
	 */
	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {

		SqlTemplate sql = super.createSqlTemplate();
		/** Fields **/
		sql.addProjection(
			"	 pes.id_pessoa" +
			"	,pes.nm_pessoa" +
			"   ,tabPre.id_tabela_preco" +
			"   ,tabPre.pc_reajuste" +
			"   ,tabPre.dt_vigencia_inicial" +
			"   ,tabPre.dt_vigencia_final" +
			"   ,tipTabPre.id_tipo_tabela_preco" +
			"   ,tipTabPre.nr_versao" +
			"   ,tipTabPre.tp_tipo_tabela_preco" +
			"   ,subTipTabPre.id_subtipo_tabela_preco" +
			"   ,subTipTabPre.tp_subtipo_tabela_preco" +
			"   ,tabPre.ds_descricao");

		/** Joins **/
		sql.addFrom(
			"	pessoa pes" +
			"      inner join empresa emp" +
			"          inner join tipo_tabela_preco tipTabPre" +
			"              inner join v_tabela_preco_i tabPre" +
			"                  inner join subtipo_tabela_preco subTipTabPre" +
			"                  	   on tabPre.id_subtipo_tabela_preco = subTipTabPre.id_subtipo_tabela_preco" +
			"              on tipTabPre.id_tipo_tabela_preco = tabPre.id_tipo_tabela_preco" +
			"          on emp.id_empresa = tipTabPre.id_empresa_cadastrada" +
			"      on pes.id_pessoa = emp.id_empresa");

		/** Order **/
		sql.addOrderBy(
			"	 pes.nm_pessoa" +
			"	,tipTabPre.tp_tipo_tabela_preco" +
			"   ,tipTabPre.nr_versao" +
			"   ,subTipTabPre.tp_subtipo_tabela_preco" +
			"   ,tabPre.dt_vigencia_inicial" +
			"   ,tabPre.dt_vigencia_final");

		/** Where **/
		sql.addCustomCriteria("tabPre.bl_efetivada = 'S'");
		sql.addCustomCriteria("tipTabPre.tp_tipo_tabela_preco = 'C'");
		/** Empresa do Usuario da Sessão **/
		Long idEmpresaUsuario = SessionUtils.getEmpresaSessao().getIdEmpresa();
		if (idEmpresaUsuario != null && idEmpresaUsuario.longValue() > 0)
			sql.addCriteria("tipTabPre.id_empresa_logada","=",idEmpresaUsuario);
		/** Empresa da Cia Aerea **/
		Long idEmpresaCiaAerea = parameters.getLong("empresa.idEmpresa");		
		if (idEmpresaCiaAerea != null && idEmpresaCiaAerea.longValue() > 0)
		{
			sql.addCriteria("tipTabPre.id_empresa_cadastrada","=",idEmpresaCiaAerea);
			sql.addFilterSummary("ciaAerea", parameters.getString("ciaAerea.empresa.nmPessoa"));
		}
		
		/** Filtro Periodo (Datas) **/
		YearMonthDay dtInicialEmissaoAWB = parameters.getYearMonthDay("dtInicialEmissaoAWB");
		YearMonthDay dtFinalEmissaoAWB = parameters.getYearMonthDay("dtFinalEmissaoAWB");
		/** Between **/
		if (dtInicialEmissaoAWB != null && dtFinalEmissaoAWB != null) {
			sql.addCriteria("tabPre.dt_vigencia_inicial", "<=", dtFinalEmissaoAWB);
			sql.addCriteria("tabPre.dt_vigencia_final", ">=", dtInicialEmissaoAWB);
			sql.addFilterSummary("periodoInicial", dtInicialEmissaoAWB);
			sql.addFilterSummary("periodoFinal", dtFinalEmissaoAWB);
		/** Inicial **/
		} else if (dtInicialEmissaoAWB != null) {
			sql.addCriteria("tabPre.dt_vigencia_inicial", ">=", dtInicialEmissaoAWB);
			sql.addFilterSummary("periodoInicial", dtInicialEmissaoAWB);
		/** Final **/
		} else if (dtFinalEmissaoAWB != null) {
			sql.addCustomCriteria("(tabPre.dt_vigencia_final <= ? or tabPre.dt_vigencia_final = ?)");
			sql.addCriteriaValue(dtFinalEmissaoAWB);
			sql.addCriteriaValue(JTDateTimeUtils.MAX_YEARMONTHDAY);
			sql.addFilterSummary("periodoFinal", dtFinalEmissaoAWB);
		}
		return sql;
	}
}