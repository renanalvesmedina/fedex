package com.mercurio.lms.vendas.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Diego Pacheco, Rodrigo Dias - GT5 - LMS
 *
 * @spring.bean id="lms.vendas.emitirGestaoVisitasService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirGestaoVisitas.jasper"
 * 
 * ET: 01.08.01.02 Relatorio de Gestão de Visitas
 */
public class EmitirGestaoVisitasService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {

		SqlTemplate sql = montaSQL(parameters);		

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		sql.addFilterSummary("regional",MapUtilsPlus.getString(parameters,"regional.siglaDescricao",null));
		sql.addFilterSummary("filial", getDsFilial(MapUtilsPlus.getLong(parameters,"filial.idFilial",null)));
		sql.addFilterSummary("funcionario", getNomeUsuario(MapUtilsPlus.getLong(parameters,"funcionario.idUsuario",null)));
		sql.addFilterSummary("cliente",MapUtilsPlus.getString(parameters,"nmRemetente",null));
		sql.addFilterSummary("tipoVisita", getTipoVisita(MapUtilsPlus.getLong(parameters,"tipoVisita.idTipoVisita",null)));
		sql.addFilterSummary("tipoServico", getServico(MapUtilsPlus.getLong(parameters,"servico",null)));
		sql.addFilterSummary("indicadorMarketing", getIndicadorMarketing(MapUtilsPlus.getLong(parameters,"campanhaMarketing.idCampanhaMarketing",null)));
		sql.addFilterSummary("periodoInicial", formatDate(MapUtilsPlus.getString(parameters,"dtInicial",null)));
		sql.addFilterSummary("periodoFinal"  , formatDate(MapUtilsPlus.getString(parameters,"dtFinal",null)));
		sql.addFilterSummary("perspectivaFaturamento", getPerspectivaFaturamento(MapUtilsPlus.getString(parameters,"tpPerspectivaFaturamento",null)));

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());		

		jr.setParameters(parametersReport);
		return jr;
	}

	/**
	 * @param date
	 * @return
	 * @throws Exception
	 */
	private String formatDate(Object date) throws Exception {
		if(date == null) return null;

		java.text.SimpleDateFormat dtShort = new java.text.SimpleDateFormat("yyyy-mm-dd");	   
		java.text.SimpleDateFormat dtBR = new SimpleDateFormat("dd/mm/yyyy");		   
		return dtBR.format(dtShort.parse((String)date));		   
	}
	
	/**
	 * Retorna valor do domínio de perspectiva de faturamento identificado pelo seu tipo
	 * @param tpPerspectivaFat
	 * @return
	 */
	private String getPerspectivaFaturamento(String tpPerspectivaFat) {
		if(tpPerspectivaFat == null) return "";
		List ret = getJdbcTemplate().queryForList(
				" SELECT "+PropertyVarcharI18nProjection.createProjection("vd.DS_VALOR_DOMINIO_I", "DS_VALOR_DOMINIO")+
				" FROM dominio d, valor_dominio vd " +
				" WHERE d.id_dominio = vd.id_dominio " +
				"	AND d.NM_DOMINIO = 'DM_PERSPECTIVA_FATUR'" +
				"   AND vd.VL_VALOR_DOMINIO = '" + tpPerspectivaFat + "'");
		if (ret==null || ret.size()==0) return "";
		return (String)((Map)ret.get(0)).get("DS_VALOR_DOMINIO");
	}

	/**
	 * Retorna sigla - nome fantasia do filial do identificador especifico
	 */
	private String getDsFilial(Long idFilial) {
		if(idFilial == null) return "";
		List ret = getJdbcTemplate().queryForList(" select SG_FILIAL || ' - ' ||  NM_FANTASIA as header from filial, pessoa  where pessoa.id_pessoa = filial.id_filial and id_filial = " + idFilial);
		if (ret==null || ret.size()==0) return "";
		return (String)((Map)ret.get(0)).get("header");
	}
	
	/**
	 * Retorna nome do usuario especificado pelo seu id
	 * @param idUsuario
	 * @return
	 */
	private String getNomeUsuario(Long idUsuario) {
		if(idUsuario == null) return null;

		List ret = getJdbcTemplate().queryForList("select NM_USUARIO from usuario where id_usuario = " + idUsuario);
		if (ret==null || ret.size()==0) return "";
		return (String)((Map)ret.get(0)).get("NM_USUARIO");		

	}
	
	/**
	 * Retorna descricao do servico especificado pelo seu id
	 * @param idServico
	 * @return
	 */
	private String getServico(Long idServico) {
		if(idServico != null) {
			StringBuilder sql = new StringBuilder();
			sql.append(" select ").append(PropertyVarcharI18nProjection.createProjection("DS_SERVICO_I", "DS_SERVICO"));
			sql.append("   from SERVICO");
			sql.append("  where ID_SERVICO = ?");

			List result = getJdbcTemplate().queryForList(sql.toString(), new Long[]{idServico});
			if (!result.isEmpty()) {
				return (String)((Map)result.get(0)).get("DS_SERVICO");
			}
		}
		return null;
	}

	/**
	 * Retorna descricao do tipo de visita especificado pelo seu id
	 * @param idTipoVisita
	 * @return
	 */
	private String getTipoVisita(Long idTipoVisita) {
		if(idTipoVisita != null) {
			StringBuilder sql = new StringBuilder();
			sql.append(" select ").append(PropertyVarcharI18nProjection.createProjection("DS_TIPO_VISITA_I", "DS_TIPO_VISITA"));
			sql.append("   from TIPO_VISITA");
			sql.append("  where ID_TIPO_VISITA = ?");

			List result = getJdbcTemplate().queryForList(sql.toString(), new Long[]{idTipoVisita});
			if(!result.isEmpty()) {
				return (String)((Map)result.get(0)).get("DS_TIPO_VISITA");
			}
		}
		return null;
	}

	/**
	 * Retorna descricao da campanha de marketing especificada pelo seu id
	 * @param idCampanhaMarketing
	 * @return
	 */
	private String getIndicadorMarketing(Long idCampanhaMarketing) {
		if(idCampanhaMarketing != null) {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT ").append(PropertyVarcharI18nProjection.createProjection("DS_CAMPANHA_MARKETING_I", "DS_CAMPANHA_MARKETING"));
			sql.append("   FROM CAMPANHA_MARKETING");
			sql.append("  WHERE ID_CAMPANHA_MARKETING = ?");

			List result = getJdbcTemplate().queryForList(sql.toString(), new Long[]{idCampanhaMarketing});
			if (!result.isEmpty()) {
				return (String)((Map)result.get(0)).get("DS_CAMPANHA_MARKETING");
			}
		}
		return null;
	}

	private SqlTemplate montaSQL(Map parameters) {
		StringBuilder sqlProjection = new StringBuilder();
		sqlProjection.append(" v.dt_visita as datavisita,");
		sqlProjection.append(" v.id_visita,");
		sqlProjection.append(" pc.nm_pessoa as cliente,");
		sqlProjection.append(PropertyVarcharI18nProjection.createProjection("tv.ds_tipo_visita_i","tpvisita")).append(",");

		sqlProjection.append(" pc.nr_identificacao,");
		sqlProjection.append(" vfunc.nm_funcionario as funcionario,");
		sqlProjection.append(" f.sg_filial || ' - ' || pf.nm_fantasia as filial,");
		sqlProjection.append(" r.sg_regional || ' - ' || r.ds_regional as regional,");
		sqlProjection.append(" vfunc.ds_cargo as cargo,");
		sqlProjection.append(" u.id_usuario,");
		sqlProjection.append(PropertyVarcharI18nProjection.createProjection("cm.ds_campanha_marketing_i","indmarketing")).append(",");
		sqlProjection.append(PropertyVarcharI18nProjection.createProjection("s.ds_servico_i","solucoferecidas")).append(",");
		sqlProjection.append(" f.id_filial,");
		sqlProjection.append(" r.id_regional,");
		sqlProjection.append(" ( select ").append(PropertyVarcharI18nProjection.createProjection("ds_valor_dominio_i"));
		sqlProjection.append("     from valor_dominio vd,");
		sqlProjection.append("          dominio do");
		sqlProjection.append("     where vd.id_dominio = do.id_dominio");
		sqlProjection.append("       and c.tp_cliente = vd.vl_valor_dominio");
		sqlProjection.append("       and do.nm_dominio = 'DM_TIPO_CLIENTE'");
		sqlProjection.append(" ) as tpcliente,");
		sqlProjection.append(" ( select ").append(PropertyVarcharI18nProjection.createProjection("ds_valor_dominio_i"));
		sqlProjection.append("     from valor_dominio vd,");
		sqlProjection.append("          dominio do");
		sqlProjection.append("    where vd.id_dominio = do.id_dominio");
		sqlProjection.append("      and s.tp_abrangencia = vd.vl_valor_dominio");
		sqlProjection.append("      and do.nm_dominio = 'DM_ABRANGENCIA'");
		sqlProjection.append(" ) as abrangencia,");
		sqlProjection.append(" ( select ").append(PropertyVarcharI18nProjection.createProjection("ds_valor_dominio_i"));
		sqlProjection.append("     from valor_dominio vd,");
		sqlProjection.append("          dominio do");
		sqlProjection.append("    where vd.id_dominio = do.id_dominio");
		sqlProjection.append("      and s.tp_modal = vd.vl_valor_dominio");
		sqlProjection.append("      and do.nm_dominio = 'DM_MODAL'");
		sqlProjection.append(" ) as modal,");
		sqlProjection.append(" ( select ").append(PropertyVarcharI18nProjection.createProjection("ds_valor_dominio_i"));
		sqlProjection.append("     from valor_dominio vd,");
		sqlProjection.append("          dominio do");
		sqlProjection.append("    where vd.id_dominio = do.id_dominio");
		sqlProjection.append("      and ev.tp_perspectiva_faturamento = vd.vl_valor_dominio");
		sqlProjection.append("      and do.nm_dominio = 'DM_PERSPECTIVA_FATUR'");
		sqlProjection.append(" ) as perspecfat ");

		SqlTemplate sql = createSqlTemplate();		

		sql.addProjection(sqlProjection.toString());

		sql.addFrom(" visita v, cliente c, pessoa pc, tipo_visita tv, etapa_visita ev, " +
					" servico_oferecido so, servico s, campanha_marketing cm, " +
					" usuario u, V_FUNCIONARIO vfunc, filial f, regional r, regional_filial rf,pessoa pf ");

		sql.addCustomCriteria(" v.id_cliente  = c.id_cliente ");
		sql.addCustomCriteria(" c.id_cliente  = pc.id_pessoa ");
		sql.addCustomCriteria(" v.id_visita  = ev.id_visita(+) ");
		sql.addCustomCriteria(" ev.id_tipo_visita = tv.id_tipo_visita (+) ");
		sql.addCustomCriteria(" ev.id_etapa_visita = so.id_etapa_visita (+) ");
		sql.addCustomCriteria(" so.id_servico = s.id_servico (+) ");
		sql.addCustomCriteria(" c.id_cliente = pc.id_pessoa (+) ");
		sql.addCustomCriteria(" v.id_usuario = u.id_usuario (+) ");
		sql.addCustomCriteria(" u.nr_matricula = vfunc.nr_matricula (+) ");
		sql.addCustomCriteria(" ev.id_campanha_marketing = cm.id_campanha_marketing (+) ");
		sql.addCustomCriteria(" f.id_filial = v.id_filial_usuario (+) ");
		sql.addCustomCriteria(" f.id_filial = rf.id_filial (+) ");
		sql.addCustomCriteria(" rf.id_regional = r.id_regional (+) ");
		sql.addCustomCriteria(" pf.id_pessoa = f.id_filial (+) ");

		Long idFilial = MapUtilsPlus.getLong(parameters, "filial.idFilial", null);
		if(idFilial != null) {
			sql.addCriteria(" f.id_filial", "=", idFilial);
		} else {
			sql.addCustomCriteria(" f.id_filial in " +  SQLUtils.joinExpressionsWithComma(getIdsFiliais()) + " ");
		}

		Long idUsuario = MapUtilsPlus.getLong(parameters, "funcionario.idUsuario", null);
		if(idUsuario != null) {
			sql.addCriteria("u.id_usuario","=",idUsuario);	
		}

		Long idCliente = MapUtilsPlus.getLong(parameters, "cliente.idCliente", null);
		if(idCliente != null) {
			sql.addCriteria("c.id_cliente", "=", idCliente);
		}

		sql.addCriteria("s.id_servico","=",MapUtilsPlus.getLong(parameters,"servico",null));
		sql.addCriteria("tv.id_tipo_visita","=",MapUtilsPlus.getLong(parameters,"tipoVisita.idTipoVisita",null));
		sql.addCriteria("cm.id_campanha_marketing","=",MapUtilsPlus.getLong(parameters,"campanhaMarketing.idCampanhaMarketing",null));
		
		String tpPerspectivaFaturamento = MapUtilsPlus.getString(parameters,"tpPerspectivaFaturamento",null);
		if(tpPerspectivaFaturamento != null) {
			sql.addCriteria("ev.tp_perspectiva_faturamento","=",tpPerspectivaFaturamento);
		}

		sql.addCriteria("r.id_regional", "=", MapUtilsPlus.getLong(parameters,"regional.idRegional",null));

		if ( (MapUtilsPlus.getObject(parameters,"dtInicial",null)!=null) && (!((String)MapUtilsPlus.getObject(parameters,"dtInicial",null)).equals("")) )
			sql.addCustomCriteria("to_char(v.dt_visita ,'yyyy-mm-dd')  >= '" + MapUtilsPlus.getObject(parameters,"dtInicial",null) + "'");

		if ( (MapUtilsPlus.getObject(parameters,"dtFinal",null)!=null) && (!((String)MapUtilsPlus.getObject(parameters,"dtFinal",null)).equals("")) )
			sql.addCustomCriteria("to_char(v.dt_visita ,'yyyy-mm-dd') <= '" + MapUtilsPlus.getObject(parameters,"dtFinal",null) + "'");		

		sql.addOrderBy(" r.sg_regional, f.sg_filial, vfunc.nm_funcionario, u.nm_usuario, v.dt_visita, v.id_visita, pc.nm_pessoa ");

		return sql;		
	}

	private List<Long> getIdsFiliais() {
		List<Filial> filiais = SessionUtils.getFiliaisUsuarioLogado();
		List<Long> result = new ArrayList<Long>(filiais.size());
		for (Filial filial : filiais) {
			result.add(filial.getIdFilial());
		}
		return result;
	}
}
