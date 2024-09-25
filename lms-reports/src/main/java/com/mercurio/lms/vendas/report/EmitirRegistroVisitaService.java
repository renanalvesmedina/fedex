package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.collections.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.FormatUtils;

/**
 * @author Rodrigo F. Dias - GT5
 *
 * ET: 01.08.01.05 - Emitir Registro de Visitas
 *
 * @spring.bean id="lms.vendas.EmitirRegistroVisitaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirRegistroVisita.jasper"
 */
public class EmitirRegistroVisitaService extends ReportServiceSupport {
	private ConfiguracoesFacade configuracoesFacade;

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		Long idVisita = MapUtils.getLong(parameters,"masterId");

		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		parametersReport.put("SUBREPORTETAPAVISITA", new JRMapCollectionDataSource(montaEtapasVisita(idVisita, jdbcTemplate)));
		parametersReport.put("SUBREPORTACOMPANHANTEVISITA", new JRMapCollectionDataSource(montaAcompanhantesVisita(idVisita, jdbcTemplate)));

		List<Map<String, Object>> listaDadosCliente = jdbcTemplate.queryForList(montaSQLVisita(idVisita));
		if(listaDadosCliente.size() > 0) {
			Map<String, Object> map = listaDadosCliente.get(0);
			String cnpj = (String) map.get("CNPJ");
			String formattedCNPJ = FormatUtils.formatCNPJ(cnpj);
			map.put("CNPJ", formattedCNPJ);
		}

		JRMapCollectionDataSource dataCliente = new JRMapCollectionDataSource(listaDadosCliente);
		JRReportDataObject jr = createReportDataObject(dataCliente, parameters);
		jr.setParameters(parametersReport);
		return jr;
	}

	/**
	 * Metodo que retorna lista de acompanhantes da visita
	 * 
	 * @param idVisita
	 * @param jdbcTemplate
	 * @return
	 */
	public List<Map<String, Object>> montaAcompanhantesVisita(Long idVisita, JdbcTemplate jdbcTemplate) {
		StringBuffer sqlAcompanhantesVisita = new StringBuffer();
		sqlAcompanhantesVisita.append("SELECT DISTINCT")
			.append(" usu.NM_USUARIO as NOMEACOMPAN, ")
			.append(" vfu.DS_FUNCAO as CARGOACOMPAN")
			.append(" FROM ")
			.append(" V_FUNCIONARIO vfu, ")
			.append(" USUARIO usu, ")
			.append(" FUNCIONARIO_VISITA fvi ")
			.append(" WHERE ")
			.append(" fvi.ID_USUARIO = usu.ID_USUARIO ")
			.append(" and usu.NR_MATRICULA = vfu.NR_MATRICULA ")
			.append(" and fvi.ID_VISITA = ?");

		return jdbcTemplate.queryForList(sqlAcompanhantesVisita.toString(), new Long[]{idVisita});
	}

	/**
	 * Metodo que retorna etapas da visita
	 * 
	 * @param idVisita
	 * @param jdbcTemplate
	 * @return
	 */
	public List<Map<String, Object>> montaEtapasVisita(Long idVisita, JdbcTemplate jdbcTemplate) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT ep.ID_ETAPA_VISITA as IDETAPAVISITA, ")
			.append(PropertyVarcharI18nProjection.createProjection(" tv.DS_TIPO_VISITA_I", "TPVISITA")).append(", ")
			.append(PropertyVarcharI18nProjection.createProjection(" cm.DS_CAMPANHA_MARKETING_I", "INDMARKETING")).append(", ")
			.append(" ep.DS_CONTATO as CONTATO, ")
			.append(" ep.DS_AREA_ATUACAO as AREA, ")
			.append(" ep.DS_EMAIL as EMAIL, ")
			.append(" ep.NR_TELEFONE as TELEFONE, ")
			.append(PropertyVarcharI18nProjection.createProjection("MODAL.DS_VALOR_DOMINIO_I", "MODAL")).append(",")
			.append(PropertyVarcharI18nProjection.createProjection("ABRANGENCIA.DS_VALOR_DOMINIO_I", "ABRANGENCIA")).append(",")
			.append(PropertyVarcharI18nProjection.createProjection("PERSPECTIVA_FATURAMENTO.DS_VALOR_DOMINIO_I", "PERSPECTIVAFAT")).append(",")
			.append(PropertyVarcharI18nProjection.createProjection("se.DS_SERVICO_I", "TPSERVICO")).append(",")
			.append(" so.OB_SERVICO_OFERECIDO as DESCSERVICO ");

		query.append(" FROM ")
			.append(" ETAPA_VISITA ep, ")
			.append(" TIPO_VISITA tv, ")
			.append(" CAMPANHA_MARKETING cm, ")
			.append(" SERVICO_OFERECIDO so, ")
			.append(" SERVICO se, ")
			.append(" (select ETAPA_VISITA.ID_ETAPA_VISITA, ETAPA_VISITA.TP_MODAL, VALOR_DOMINIO.DS_VALOR_DOMINIO_I ")
			.append(" from ")
			.append(" DOMINIO, VALOR_DOMINIO, ETAPA_VISITA ")
			.append(" where ")
			.append(" DOMINIO.ID_DOMINIO = VALOR_DOMINIO.ID_DOMINIO ")
			.append(" AND DOMINIO.NM_DOMINIO = 'DM_MODAL' ")
			.append(" AND VALOR_DOMINIO.VL_VALOR_DOMINIO = ETAPA_VISITA.TP_MODAL ")
			.append(" ) MODAL, ")
			.append(" (select ETAPA_VISITA.ID_ETAPA_VISITA, ETAPA_VISITA.TP_MODAL, VALOR_DOMINIO.DS_VALOR_DOMINIO_I ")
			.append(" from ")
			.append(" DOMINIO, VALOR_DOMINIO, ETAPA_VISITA ")
			.append(" where ")
			.append(" DOMINIO.ID_DOMINIO = VALOR_DOMINIO.ID_DOMINIO ")
			.append(" AND DOMINIO.NM_DOMINIO = 'DM_ABRANGENCIA' ")
			.append(" AND VALOR_DOMINIO.VL_VALOR_DOMINIO = ETAPA_VISITA.TP_ABRANGENCIA ")
			.append(" ) ABRANGENCIA, ")
			.append(" (select ETAPA_VISITA.ID_ETAPA_VISITA, ETAPA_VISITA.TP_MODAL, VALOR_DOMINIO.DS_VALOR_DOMINIO_I ")
			.append(" from ")
			.append(" DOMINIO, VALOR_DOMINIO, ETAPA_VISITA ")
			.append(" where ")
			.append(" DOMINIO.ID_DOMINIO = VALOR_DOMINIO.ID_DOMINIO ")
			.append(" AND DOMINIO.NM_DOMINIO = 'DM_PERSPECTIVA_FATUR' ")
			.append(" AND VALOR_DOMINIO.VL_VALOR_DOMINIO = ETAPA_VISITA.TP_PERSPECTIVA_FATURAMENTO ")
			.append(" ) PERSPECTIVA_FATURAMENTO ");

		query.append(" WHERE ")
			.append(" ep.ID_TIPO_VISITA = tv.ID_TIPO_VISITA ")
			.append(" and ep.ID_CAMPANHA_MARKETING = cm.ID_CAMPANHA_MARKETING (+)")
			.append(" and ep.ID_ETAPA_VISITA = MODAL.ID_ETAPA_VISITA (+)")
			.append(" and ep.ID_ETAPA_VISITA = ABRANGENCIA.ID_ETAPA_VISITA (+)")
			.append(" and ep.ID_ETAPA_VISITA = PERSPECTIVA_FATURAMENTO.ID_ETAPA_VISITA (+)")
			.append(" and ep.ID_ETAPA_VISITA = so.ID_ETAPA_VISITA (+)")
			.append(" and so.ID_SERVICO = se.ID_SERVICO (+)")
			.append(" and id_visita = ? ")
			.append(" order by ep.ID_ETAPA_VISITA");

		return jdbcTemplate.queryForList(query.toString(), new Long[]{idVisita});
	}

	/**
	 * 
	 * @param idVistia
	 * @return
	 */
	public String montaSQLVisita(Long idVisita) {
		StringBuffer query = new StringBuffer();
		query.append("select distinct ")
			.append("FIL.SG_FILIAL||' - '||PESFIL.NM_FANTASIA as FILIAL, ")

			.append(" (select reg.sg_regional || ' - ' || reg.ds_regional from regional reg, regional_filial regfil ")
			.append("where regfil.id_filial = fil.id_filial ")
			.append("and regfil.id_regional = reg.id_regional ")
			.append("and regfil.dt_vigencia_inicial <= current_date ")
			.append("and regfil.dt_vigencia_final >= current_date ) as REGIONAL, ")

			.append("V_FUNCIONARIO.NM_FUNCIONARIO as NOMEFUNCIO, ")
			.append("V_FUNCIONARIO.DS_FUNCAO as CARGOFUNCIO, ")
			.append("vis.DT_REGISTRO as DTGERACAO, ")
			.append("pescli.NM_PESSOA as CLIENTE, ")
			.append("cli.id_cliente, ")
			.append("pescli.NR_IDENTIFICACAO as CNPJ, ")
			.append(PropertyVarcharI18nProjection.createProjection("tipo_cliente.DS_VALOR_DOMINIO_I", "TPCLIENTE")).append(",")

			.append(PropertyVarcharI18nProjection.createProjection("endereco.DS_TIPO_LOGRADOURO_I"))
			.append("||' '||endereco.DS_ENDERECO")
			.append("||' '||endereco.nr_endereco")
			.append("||' '||endereco.ds_complemento AS endereco, ")

			.append("endereco.NM_MUNICIPIO as MUNICIPIO, ")
			.append("endereco.SG_UNIDADE_FEDERATIVA as UF, ")
			.append("vis.DT_VISITA as DTVISITA, ")
			.append("vis.HR_INICIAL as HORAINICIAL, ")
			.append("vis.HR_FINAL as HORAFINAL, ")
			.append("vis.DS_VISITA as DESCRVISITA, ")
			.append("vis.DT_VISTO as DATAAPROVACAO, ")
			.append("(select usuvisto.nm_usuario from usuario usuvisto where usuvisto.id_usuario = vis.ID_USUARIO_VISTO ) as GERENTEAPROVADOR ")

			.append(" FROM ")
			.append("visita vis, ")
			.append("filial fil, ")
			.append("pessoa pesfil, ")
			.append("usuario usu, ")
			.append("v_funcionario, ")
			.append("cliente cli, ")
			.append("pessoa pescli, ")
			.append("(select CLIENTE.ID_CLIENTE, DS_VALOR_DOMINIO_I ")
			.append("from DOMINIO, VALOR_DOMINIO, CLIENTE ")
			.append("where DOMINIO.ID_DOMINIO = VALOR_DOMINIO.ID_DOMINIO ")
			.append("AND DOMINIO.NM_DOMINIO = 'DM_TIPO_CLIENTE' ")
			.append("AND VALOR_DOMINIO.VL_VALOR_DOMINIO = CLIENTE.TP_CLIENTE ")
			.append(") tipo_cliente, ")

			.append("(select ep.*, tl.DS_TIPO_LOGRADOURO_I, mun.NM_MUNICIPIO, uf.SG_UNIDADE_FEDERATIVA ")
			.append("FROM endereco_pessoa ep, tipo_logradouro tl, tipo_endereco_pessoa tep,")
			.append("municipio mun, unidade_federativa uf ")
			.append("WHERE ep.id_tipo_logradouro = tl.ID_TIPO_LOGRADOURO ")
			.append(" and ep.id_endereco_pessoa = tep.id_endereco_pessoa ")
			.append(" and ep.id_municipio = mun.id_municipio ")
			.append(" and mun.id_unidade_federativa = uf.id_unidade_federativa ")
			.append(" and tep.tp_endereco = 'COM' ")
			.append(" and ep.dt_vigencia_inicial <= sysdate and ep.dt_vigencia_final >= sysdate ")
			.append(") endereco ")

			.append(" WHERE ")
			.append("vis.id_usuario = usu.id_usuario ")
			.append(" and usu.nr_matricula = v_funcionario.nr_matricula (+)")
			.append("and vis.id_filial_usuario = fil.id_filial (+)")
			.append("and fil.id_filial = pesfil.id_pessoa (+)")
			.append("and vis.id_cliente = cli.id_cliente (+)")
			.append("and cli.id_cliente = pescli.id_pessoa (+)")
			.append("and cli.id_cliente = tipo_cliente.id_cliente (+)")
			.append("and cli.id_cliente = endereco.id_pessoa (+)")
			.append("and vis.id_visita = ").append(idVisita);

		return query.toString();
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}