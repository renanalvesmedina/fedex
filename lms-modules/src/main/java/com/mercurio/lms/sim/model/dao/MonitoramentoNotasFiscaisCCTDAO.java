package com.mercurio.lms.sim.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.AgendamentoMonitCCT;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.NFEUtils;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.MonitoramentoCCT;
import com.mercurio.lms.util.JTDateTimeUtils;

public class MonitoramentoNotasFiscaisCCTDAO extends BaseCrudDao<MonitoramentoCCT, Long> {

	@Override
	protected final Class<MonitoramentoCCT> getPersistentClass() {
		return MonitoramentoCCT.class;
	}

	public MonitoramentoCCT findById(Long id) {
		return (MonitoramentoCCT) getAdsmHibernateTemplate().get(
				MonitoramentoCCT.class, id);
	}

	public MonitoramentoCCT findById(Long id, String[] fetches) {
		Criteria c = getSession().createCriteria(getPersistentClass());

		if (fetches != null && fetches.length != 0) {
			for (int i = 0; i < fetches.length; i++) {
				c.setFetchMode(fetches[i], FetchMode.JOIN);
			}
		}
		
		c.add(Restrictions.idEq(id));

		return (MonitoramentoCCT) c.uniqueResult();
	}

	public MonitoramentoCCT findByNrChave(String chave) {
		Criteria c = getSession().createCriteria(getPersistentClass());
		c.add(Restrictions.eq("nrChave", chave));
		return (MonitoramentoCCT) c.uniqueResult();
	}

	public List findDatasAgendamentoRelatorioFUP(Long idMonitoramentoCCT) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idMonitoramentoCCT", idMonitoramentoCCT);
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("dt_agendamento",  Hibernate.custom(JodaTimeYearMonthDayUserType.class));
			}
		};
		
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append("ae.dt_agendamento ");
		sql.append(" FROM ");
		sql.append("agendamento_monit_cct amc ");
		sql.append(",agendamento_entrega ae ");
		sql.append(" WHERE ");
		sql.append("amc.id_agendamento_entrega = ae.id_agendamento_entrega ");
		sql.append("AND amc.id_monitoramento_cct = :idMonitoramentoCCT ");
		sql.append("AND rownum <= 3 ");
		sql.append("ORDER BY ae.dt_agendamento DESC ");
		
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parametersValues, csq);
	}
	
	
	public List findDatasEventoMonitoramentoRelatorioFUP(Long idMonitoramentoCCT, String tpSituacaoNFCCT) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idMonitoramentoCCT", idMonitoramentoCCT);
		parametersValues.put("tpSituacaoNFCCT", tpSituacaoNFCCT);
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("dh_inclusao",  Hibernate.custom(JodaTimeDateTimeUserType.class));
			}
		};
		
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append("emc.dh_inclusao");
		sql.append(" FROM ");
		sql.append("evento_monitoramento_cct emc ");
		sql.append(" WHERE ");
		sql.append("emc.id_monitoramento_cct = :idMonitoramentoCCT ");
		sql.append("AND emc.tp_situacao_nf_cct = :tpSituacaoNFCCT ");
		sql.append("ORDER BY emc.dh_inclusao DESC ");
		
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parametersValues, csq);
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryDadosRelatorioFUP(){
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idMonitoramentoCCT", Hibernate.LONG);
				sqlQuery.addScalar("nr_chave", Hibernate.STRING);				
				sqlQuery.addScalar("ds_remetente", Hibernate.STRING);
				sqlQuery.addScalar("ds_modal", Hibernate.STRING);
				sqlQuery.addScalar("tp_docto_servico", Hibernate.STRING);
				sqlQuery.addScalar("ds_filial_orig", Hibernate.STRING);
				sqlQuery.addScalar("ds_filial_dest", Hibernate.STRING);
				sqlQuery.addScalar("nr_docto_servico", Hibernate.LONG);
				sqlQuery.addScalar("nr_cnpj_dest", Hibernate.STRING);
				sqlQuery.addScalar("ds_destinatario", Hibernate.STRING);
				sqlQuery.addScalar("ds_cidade_dest", Hibernate.STRING);
				sqlQuery.addScalar("ds_uf_dest", Hibernate.STRING);
				sqlQuery.addScalar("vl_frete", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_peso_cobrado", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dt_emis_docto_servico", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("dh_chegada_filial_dest", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("dt_previsao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dt_solicit_icms", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dt_pgto_icms", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dt_entrega", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("ds_recebedor", Hibernate.STRING);
				sqlQuery.addScalar("ds_situacao", Hibernate.STRING);
				sqlQuery.addScalar("ds_localiz_mercad", Hibernate.STRING);
				sqlQuery.addScalar("nr_awb", Hibernate.STRING);
				sqlQuery.addScalar("ds_cia_aerea", Hibernate.STRING);
				sqlQuery.addScalar("nr_dias_armazen", Hibernate.LONG);
				sqlQuery.addScalar("nr_dias_prazo_entrega", Hibernate.LONG);
				sqlQuery.addScalar("ds_regional", Hibernate.STRING);
				sqlQuery.addScalar("ds_comentarios", Hibernate.STRING);
				sqlQuery.addScalar("ds_obs_agend", Hibernate.STRING);
				sqlQuery.addScalar("ds_ocorrencia", Hibernate.STRING);
				sqlQuery.addScalar("ds_possui_agend", Hibernate.STRING);
				sqlQuery.addScalar("vl_peso_cubado", Hibernate.BIG_DECIMAL);
			}
		};
		
		return csq;
	}
	
	private String getSqlDadosRelatorioFUP(){
		StringBuilder sql = new StringBuilder("SELECT DISTINCT ");
		sql.append("mc.id_monitoramento_cct as idMonitoramentoCCT");
		sql.append(",mc.nr_chave as nr_chave");
		sql.append(",pr.nm_pessoa as ds_remetente");
		sql.append(",VI18N(s.ds_servico_i) as ds_modal");
		sql.append(",ds.tp_documento_servico as tp_docto_servico");
		sql.append(",fo.sg_filial as ds_filial_orig");
		sql.append(",fd.sg_filial as ds_filial_dest");
		sql.append(",ds.nr_docto_servico as nr_docto_servico");
		sql.append(",pd.nr_identificacao as nr_cnpj_dest");
		sql.append(",pd.nm_pessoa as ds_destinatario");
		sql.append(",m.nm_municipio as ds_cidade_dest");
		sql.append(",uf.sg_unidade_federativa as ds_uf_dest");
		sql.append(",ds.vl_frete_liquido as vl_frete");
		sql.append(",ds.ps_referencia_calculo as vl_peso_cobrado");
		sql.append(",ds.ps_cubado_real as vl_peso_cubado");
		sql.append(",ds.dh_emissao as dt_emis_docto_servico");
		sql.append(",ds.dt_prev_entrega as dt_previsao");
		sql.append(",mc.dt_icms_solic as dt_solicit_icms");
		sql.append(",mc.dt_icms_pago as dt_pgto_icms");
		sql.append(",(SELECT eds.dh_evento FROM evento_documento_servico eds, evento e WHERE eds.id_evento = e.id_evento AND mc.id_docto_servico = eds.id_docto_servico AND e.cd_evento = :cdEventoEntregaRealizada AND eds.bl_evento_cancelado = :blEventoCancelado) as dt_entrega");
		sql.append(",(SELECT nm_recebedor FROM manifesto_entrega_documento med, ocorrencia_entrega oe WHERE mc.id_docto_servico = med.id_docto_servico AND med.id_ocorrencia_entrega = oe.id_ocorrencia_entrega  AND oe.cd_ocorrencia_entrega = :cdOcorrenciaEntregaRealizada) as ds_recebedor");
		sql.append(",VI18N(vd.ds_valor_dominio_i) as ds_situacao");
		sql.append(",VI18N(lm.ds_localizacao_mercadoria_i) as ds_localiz_mercad");
		sql.append(",(SELECT a.ds_serie || '/' || a.nr_awb ||'-'|| a.dv_awb FROM cto_awb ca, awb a WHERE ds.id_docto_servico = ca.id_conhecimento AND ca.id_awb = a.id_awb AND rownum = 1) as nr_awb");
		sql.append(",(SELECT pc.nm_pessoa FROM cto_awb ca, awb a, cia_filial_mercurio cfm, pessoa pc WHERE ds.id_docto_servico = ca.id_conhecimento AND ca.id_awb = a.id_awb AND a.id_cia_filial_mercurio = cfm.id_cia_filial_mercurio AND cfm.id_empresa = pc.id_pessoa AND rownum = 1) as ds_cia_aerea");
		sql.append(",(trunc(sysdate) - trunc(ds.dt_prev_entrega)) as nr_dias_armazen");
		sql.append(",ds.nr_dias_prev_entrega as nr_dias_prazo_entrega");
		sql.append(",r.ds_regional as ds_regional");
		sql.append(",(SELECT LISTAGG(TO_CHAR(evm.dh_inclusao,'DD/MM/YYYY') ||' - '|| evm.ds_comentario, ';') WITHIN GROUP (ORDER BY evm.dh_inclusao DESC) FROM evento_monitoramento_cct evm WHERE evm.id_monitoramento_cct = mc.id_monitoramento_cct AND ds_comentario IS NOT NULL) as ds_comentarios");
		sql.append(",(SELECT LISTAGG(CASE WHEN (ae.ob_agendamento_entrega IS NOT NULL AND ae.ob_tentativa IS NOT NULL) THEN TO_CHAR(ae.dh_contato,'DD/MM/YYYY') ||' - '|| ae.ob_agendamento_entrega ||', '|| ae.ob_tentativa WHEN (ae.ob_agendamento_entrega IS NOT NULL) THEN TO_CHAR(ae.dh_contato,'DD/MM/YYYY') ||' - '|| ae.ob_agendamento_entrega WHEN (ae.ob_tentativa IS NOT NULL) THEN  TO_CHAR(ae.dh_contato,'DD/MM/YYYY') ||' - '|| ae.ob_tentativa END, ';') WITHIN GROUP (ORDER BY ae.dh_contato DESC) FROM agendamento_monit_cct amc, agendamento_entrega ae WHERE mc.id_monitoramento_cct = amc.id_monitoramento_cct AND amc.id_agendamento_entrega = ae.id_agendamento_entrega AND (ae.ob_agendamento_entrega IS NOT NULL OR ae.ob_tentativa IS NOT NULL)) as ds_obs_agend");
		sql.append(",(SELECT LISTAGG(TO_CHAR(ae.dh_cancelamento,'DD/MM/YYYY') ||' - '|| VI18N(ma.ds_motivo_agendamento_i) ||' - '|| ae.ob_cancelamento ) WITHIN GROUP (ORDER BY ae.dh_contato DESC) FROM agendamento_monit_cct amc, agendamento_entrega ae, motivo_agendamento ma  WHERE mc.id_monitoramento_cct = amc.id_monitoramento_cct AND amc.id_agendamento_entrega = ae.id_agendamento_entrega  AND ae.id_motivo_cancelamento = ma.id_motivo_agendamento AND ae.dh_cancelamento IS NOT NULL) as ds_ocorrencia");
		sql.append(",(CASE WHEN (EXISTS (SELECT 1 FROM agendamento_monit_cct amc, agendamento_entrega ae WHERE mc.id_monitoramento_cct = amc.id_monitoramento_cct AND amc.id_agendamento_entrega = ae.id_agendamento_entrega AND ae.tp_situacao_agendamento = :tpSituacaoAgendamento AND rownum <= 1)) THEN 'Sim' ELSE 'Não' END ) as ds_possui_agend");
		sql.append(",(SELECT ct.dh_chegada FROM manifesto_nacional_cto mn, manifesto_viagem_nacional mvn, manifesto man, controle_carga cc, controle_trecho ct WHERE ds.id_docto_servico = mn.id_conhecimento AND mn.id_manifesto_viagem_nacional = mvn.id_manifesto_viagem_nacional AND mvn.id_manifesto_viagem_nacional = man.id_manifesto AND man.id_controle_carga = cc.id_controle_carga(+) AND ct.id_controle_carga = cc.id_controle_carga AND ct.id_filial_origem = man.id_filial_origem AND ct.id_filial_destino = man.id_filial_destino AND ds.id_filial_destino = man.id_filial_destino and rownum =1 ) as dh_chegada_filial_dest");
		
		sql.append(" FROM ");
		sql.append("monitoramento_cct mc ");
		sql.append(",cliente mc_cr ");
		sql.append(",pessoa pr ");
		sql.append(",envio_cte_cliente ecc ");
		sql.append(",cliente ecc_c ");
		sql.append(",agrupador_cliente ac ");
		sql.append(",docto_servico ds ");
		sql.append(",servico s ");
		sql.append(",filial fo ");
		sql.append(",filial fd ");
		sql.append(",pessoa pd ");
		sql.append(",cliente c ");
		sql.append(",endereco_pessoa ep ");
		sql.append(",municipio m ");
		sql.append(",unidade_federativa uf ");
		sql.append(",dominio d ");
		sql.append(",valor_dominio vd ");
		sql.append(",localizacao_mercadoria lm ");
		sql.append(",regional r ");
		
		sql.append(" WHERE ");
		sql.append("mc.id_cliente_rem = mc_cr.id_cliente ");
		sql.append("AND mc_cr.id_cliente = pr.id_pessoa ");
		sql.append("AND ecc.id_cliente = ecc_c.id_cliente " );
		sql.append("AND ecc.id_envio_cte_cliente = ac.id_envio_cte_cliente(+) ");
		sql.append("AND mc.id_docto_servico = ds.id_docto_servico(+) ");
		sql.append("AND ds.id_servico = s.id_servico(+) ");
		sql.append("AND ds.id_filial_origem = fo.id_filial(+) ");
		sql.append("AND ds.id_filial_destino = fd.id_filial(+) ");
		sql.append("AND mc.id_cliente_dest = pd.id_pessoa(+) ");
		sql.append("AND pd.id_endereco_pessoa = ep.id_endereco_pessoa(+) ");
		sql.append("AND ep.id_municipio = m.id_municipio(+) ");
		sql.append("AND m.id_unidade_federativa = uf.id_unidade_federativa(+) ");
		sql.append("AND d.id_dominio = vd.id_dominio ");
		sql.append("AND d.nm_dominio = :situacaoNFCCT ");
		sql.append("AND mc.tp_situacao_nf_cct = vd.vl_valor_dominio ");
		sql.append("AND ds.id_localizacao_mercadoria = lm.id_localizacao_mercadoria(+) ");
		sql.append("AND pd.id_pessoa = c.id_cliente(+) ");
		sql.append("AND c.id_regional_operacional = r.id_regional(+) ");
		
		//Campos do cadastro de parametrização de cliente
		sql.append("AND ecc.tp_parametrizacao = :tpParametrizacao ");
		sql.append("AND ( ");
		sql.append("	(mc_cr.id_cliente = :idClienteCCT AND ecc_c.id_cliente = mc_cr.id_cliente AND mc_cr.bl_cliente_cct = :blClienteCCT) ");
		sql.append("	OR ");
		sql.append("	(mc_cr.id_cliente = ac.id_cliente AND ecc.id_envio_cte_cliente = :idEnvioCteCliente) ");
		sql.append(") ");
		
		sql.append("AND ( ");
		sql.append("	(mc.tp_situacao_nf_cct NOT IN (:tpSituacoesMonitoramento) ");
		sql.append("		OR ");
		sql.append(" 		EXISTS (SELECT 1 FROM evento_documento_servico eds, evento e  ");
		sql.append("				WHERE eds.id_evento = e.id_evento AND mc.id_docto_servico = eds.id_docto_servico AND e.cd_evento = :cdEventoEntregaRealizada ");
		sql.append("				AND eds.bl_evento_cancelado = :blEventoCancelado AND eds.dh_evento >= ecc.dt_periodo_inicial AND eds.dh_evento <= ecc.dt_periodo_final) ");
		sql.append("	)");
		sql.append("	OR ");
		sql.append(" 	( EXISTS (SELECT 1 FROM evento_documento_servico eds, evento e  ");
		sql.append("				WHERE eds.id_evento = e.id_evento AND mc.id_docto_servico = eds.id_docto_servico AND e.cd_evento = :cdEventoClienteIndenizado ");
		sql.append("				AND eds.bl_evento_cancelado = :blEventoCancelado AND eds.dh_evento >= ecc.dt_periodo_inicial AND eds.dh_evento <= ecc.dt_periodo_final) ");
		sql.append("	)");
		sql.append(" ) ");
		
		sql.append("ORDER BY pr.nm_pessoa, pd.nm_pessoa");
		
		return sql.toString();
	}
	
	public List<Map<String, Object>> findDadosRelatorioFUP(Long idEnvioCteCliente, Long idClienteCCT) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idEnvioCteCliente", idEnvioCteCliente);
		parametersValues.put("idClienteCCT", idClienteCCT);
		parametersValues.put("tpParametrizacao", "F");
		parametersValues.put("blClienteCCT", "S");
		parametersValues.put("cdEventoEntregaRealizada", ConstantesSim.EVENTO_ENTREGA);
		parametersValues.put("blEventoCancelado", "N");
		parametersValues.put("cdOcorrenciaEntregaRealizada", Short.valueOf("1"));
		parametersValues.put("situacaoNFCCT", "DM_SITUACAO_NF_CCT");
		parametersValues.put("tpSituacaoAgendamento", "A");
		parametersValues.put("tpSituacoesMonitoramento", new String[]{"EN", "IN"});
		parametersValues.put("cdEventoClienteIndenizado", ConstantesSim.EVENTO_CLIENTE_INDENIZADO);
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(getSqlDadosRelatorioFUP(), parametersValues, getConfigureSqlQueryDadosRelatorioFUP());
	}
	
	public List<Map<String, Object>> findDadosRelatorioDevolucao(Long idEnvioCteCliente, Long idClienteCCT) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idEnvioCteCliente", idEnvioCteCliente);
		parametersValues.put("idClienteCCT", idClienteCCT);
		parametersValues.put("tpParametrizacao", "D");
		parametersValues.put("blClienteCCT", "S");
		parametersValues.put("cdEventoEntregaRealizada", ConstantesSim.EVENTO_ENTREGA);
		parametersValues.put("blEventoCancelado", "N");
		parametersValues.put("situacaoNFCCT", "DM_SITUACAO_NF_CCT");
		parametersValues.put("tpSituacoesMonitoramento", new String[]{"EN", "IN"});
		parametersValues.put("cdEventoClienteIndenizado", ConstantesSim.EVENTO_CLIENTE_INDENIZADO);
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(getSqlDadosRelatorioDevolucao(), parametersValues, getConfigureSqlQueryDadosRelatorioDevolucao());
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryDadosRelatorioDevolucao(){
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idMonitoramentoCCT", Hibernate.LONG);
				sqlQuery.addScalar("nr_chave", Hibernate.STRING);		
				sqlQuery.addScalar("nr_nota_fiscal_saida", Hibernate.STRING);
				sqlQuery.addScalar("ds_remetente", Hibernate.STRING);
				sqlQuery.addScalar("nr_cnpj_remet", Hibernate.STRING);
				sqlQuery.addScalar("ds_modal", Hibernate.STRING);
				sqlQuery.addScalar("tp_docto_servico", Hibernate.STRING);
				sqlQuery.addScalar("ds_filial_orig", Hibernate.STRING);
				sqlQuery.addScalar("nr_docto_servico", Hibernate.LONG);
				sqlQuery.addScalar("ds_filial_dest", Hibernate.STRING);
				sqlQuery.addScalar("nr_cnpj_dest", Hibernate.STRING);
				sqlQuery.addScalar("ds_destinatario", Hibernate.STRING);
				sqlQuery.addScalar("ds_cidade_dest", Hibernate.STRING);
				sqlQuery.addScalar("ds_uf_dest", Hibernate.STRING);
				sqlQuery.addScalar("vl_frete", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_peso_cobrado", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_peso_cubado", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dt_emis_docto_servico", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("dh_chegada_filial_dest", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("dt_previsao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dt_entrega", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("ds_situacao", Hibernate.STRING);
				sqlQuery.addScalar("ds_localiz_mercad", Hibernate.STRING);
				sqlQuery.addScalar("nr_awb", Hibernate.STRING);
				sqlQuery.addScalar("ds_cia_aerea", Hibernate.STRING);
				sqlQuery.addScalar("ds_comentarios", Hibernate.STRING);
				sqlQuery.addScalar("nr_dias_armazen", Hibernate.LONG);
				sqlQuery.addScalar("nr_dias_prazo_entrega", Hibernate.LONG);
				sqlQuery.addScalar("ds_regional", Hibernate.STRING);
			}
		};
		
		return csq;
	}
	
	private String getSqlDadosRelatorioDevolucao(){
		StringBuilder sql = new StringBuilder("SELECT DISTINCT ");
		sql.append("mc.id_monitoramento_cct as idMonitoramentoCCT");
		sql.append(",mc.nr_chave as nr_chave");
		sql.append(",SUBSTR (mcs.nr_chave, 26, 9) as nr_nota_fiscal_saida");
		sql.append(",pr.nm_pessoa as ds_remetente");
		sql.append(",pr.nr_identificacao as nr_cnpj_remet");
		sql.append(",VI18N(s.ds_servico_i) as ds_modal");
		sql.append(",ds.tp_documento_servico as tp_docto_servico");
		sql.append(",fo.sg_filial as ds_filial_orig");
		sql.append(",ds.nr_docto_servico as nr_docto_servico");
		sql.append(",fd.sg_filial as ds_filial_dest");
		sql.append(",pd.nr_identificacao as nr_cnpj_dest");
		sql.append(",pd.nm_pessoa as ds_destinatario");
		sql.append(",m.nm_municipio as ds_cidade_dest");
		sql.append(",uf.sg_unidade_federativa as ds_uf_dest");
		sql.append(",ds.vl_frete_liquido as vl_frete");
		sql.append(",ds.ps_referencia_calculo as vl_peso_cobrado");
		sql.append(",ds.ps_cubado_real as vl_peso_cubado");
		sql.append(",ds.dh_emissao as dt_emis_docto_servico");
		sql.append(",(SELECT ct.dh_chegada FROM manifesto_nacional_cto mn, manifesto_viagem_nacional mvn, manifesto man, controle_carga cc, controle_trecho ct WHERE ds.id_docto_servico = mn.id_conhecimento AND mn.id_manifesto_viagem_nacional = mvn.id_manifesto_viagem_nacional AND mvn.id_manifesto_viagem_nacional = man.id_manifesto AND man.id_controle_carga = cc.id_controle_carga(+) AND ct.id_controle_carga = cc.id_controle_carga AND ct.id_filial_origem = man.id_filial_origem AND ct.id_filial_destino = man.id_filial_destino AND ds.id_filial_destino = man.id_filial_destino and rownum =1 ) as dh_chegada_filial_dest");
		sql.append(",ds.dt_prev_entrega as dt_previsao");
		sql.append(",(SELECT eds.dh_evento FROM evento_documento_servico eds, evento e WHERE eds.id_evento = e.id_evento AND mc.id_docto_servico = eds.id_docto_servico AND e.cd_evento = :cdEventoEntregaRealizada AND eds.bl_evento_cancelado = :blEventoCancelado) as dt_entrega");
		sql.append(",VI18N(vd.ds_valor_dominio_i) as ds_situacao");
		sql.append(",VI18N(lm.ds_localizacao_mercadoria_i) as ds_localiz_mercad");
		sql.append(",(SELECT a.ds_serie || '/' || a.nr_awb ||'-'|| a.dv_awb FROM cto_awb ca, awb a WHERE ds.id_docto_servico = ca.id_conhecimento AND ca.id_awb = a.id_awb AND rownum = 1) as nr_awb");
		sql.append(",(SELECT pc.nm_pessoa FROM cto_awb ca, awb a, cia_filial_mercurio cfm, pessoa pc WHERE ds.id_docto_servico = ca.id_conhecimento AND ca.id_awb = a.id_awb AND a.id_cia_filial_mercurio = cfm.id_cia_filial_mercurio AND cfm.id_empresa = pc.id_pessoa AND rownum = 1) as ds_cia_aerea");
		sql.append(",(SELECT LISTAGG(TO_CHAR(evm.dh_inclusao,'DD/MM/YYYY') ||' - '|| evm.ds_comentario, ';') WITHIN GROUP (ORDER BY evm.dh_inclusao DESC) FROM evento_monitoramento_cct evm WHERE evm.id_monitoramento_cct = mc.id_monitoramento_cct AND ds_comentario IS NOT NULL) as ds_comentarios");
		sql.append(",(trunc(sysdate) - trunc(ds.dt_prev_entrega)) as nr_dias_armazen");
		sql.append(",ds.nr_dias_prev_entrega as nr_dias_prazo_entrega");
		sql.append(",r.ds_regional as ds_regional");
		
		sql.append(" FROM ");
		sql.append("monitoramento_cct mc ");
		sql.append(",monitoramento_cct mcs ");
		sql.append(",cliente mc_cr ");
		sql.append(",pessoa pr ");
		sql.append(",envio_cte_cliente ecc ");
		sql.append(",cliente ecc_c ");
		sql.append(",agrupador_cliente ac ");
		sql.append(",docto_servico ds ");
		sql.append(",servico s ");
		sql.append(",filial fo ");
		sql.append(",filial fd ");
		sql.append(",pessoa pd ");
		sql.append(",cliente c ");
		sql.append(",endereco_pessoa ep ");
		sql.append(",municipio m ");
		sql.append(",unidade_federativa uf ");
		sql.append(",dominio d ");
		sql.append(",valor_dominio vd ");
		sql.append(",localizacao_mercadoria lm ");
		sql.append(",regional r ");
		
		sql.append(" WHERE ");
		sql.append("mc.id_cliente_rem = mc_cr.id_cliente ");
		sql.append("AND mc.nr_chave = mcs.nr_chave_devol ");
		sql.append("AND mc_cr.id_cliente = pr.id_pessoa ");
		sql.append("AND ecc.id_cliente = ecc_c.id_cliente " );
		sql.append("AND ecc.id_envio_cte_cliente = ac.id_envio_cte_cliente(+) ");
		sql.append("AND mc.id_docto_servico = ds.id_docto_servico(+) ");
		sql.append("AND ds.id_servico = s.id_servico(+) ");
		sql.append("AND ds.id_filial_origem = fo.id_filial(+) ");
		sql.append("AND ds.id_filial_destino = fd.id_filial(+) ");
		sql.append("AND mc.id_cliente_dest = pd.id_pessoa(+) ");
		sql.append("AND pd.id_endereco_pessoa = ep.id_endereco_pessoa(+) ");
		sql.append("AND ep.id_municipio = m.id_municipio(+) ");
		sql.append("AND m.id_unidade_federativa = uf.id_unidade_federativa(+) ");
		sql.append("AND d.id_dominio = vd.id_dominio ");
		sql.append("AND d.nm_dominio = :situacaoNFCCT ");
		sql.append("AND mc.tp_situacao_nf_cct = vd.vl_valor_dominio ");
		sql.append("AND ds.id_localizacao_mercadoria = lm.id_localizacao_mercadoria(+) ");
		sql.append("AND pd.id_pessoa = c.id_cliente(+) ");
		sql.append("AND c.id_regional_operacional = r.id_regional(+) ");
		
		//Campos do cadastro de parametrização de cliente
		sql.append("AND ecc.tp_parametrizacao = :tpParametrizacao ");
		sql.append("AND ( ");
		sql.append("	(mc.id_cliente_dest = :idClienteCCT AND ecc_c.id_cliente = mc.id_cliente_dest AND c.bl_cliente_cct = :blClienteCCT) ");
		sql.append("	OR ");
		sql.append("	(mc.id_cliente_dest = ac.id_cliente AND ecc.id_envio_cte_cliente = :idEnvioCteCliente) ");
		sql.append(") ");
		
		sql.append("AND ( ");
		sql.append("	(mc.tp_situacao_nf_cct NOT IN (:tpSituacoesMonitoramento) ");
		sql.append("		OR ");
		sql.append(" 		EXISTS (SELECT 1 FROM evento_documento_servico eds, evento e  ");
		sql.append("				WHERE eds.id_evento = e.id_evento AND mc.id_docto_servico = eds.id_docto_servico AND e.cd_evento = :cdEventoEntregaRealizada ");
		sql.append("				AND eds.bl_evento_cancelado = :blEventoCancelado AND eds.dh_evento >= ecc.dt_periodo_inicial AND eds.dh_evento <= ecc.dt_periodo_final) ");
		sql.append("	)");
		sql.append("	OR ");
		sql.append(" 	( EXISTS (SELECT 1 FROM evento_documento_servico eds, evento e  ");
		sql.append("				WHERE eds.id_evento = e.id_evento AND mc.id_docto_servico = eds.id_docto_servico AND e.cd_evento = :cdEventoClienteIndenizado ");
		sql.append("				AND eds.bl_evento_cancelado = :blEventoCancelado AND eds.dh_evento >= ecc.dt_periodo_inicial AND eds.dh_evento <= ecc.dt_periodo_final) ");
		sql.append("	)");
		sql.append(" ) ");
		
		sql.append("ORDER BY pr.nm_pessoa, pd.nm_pessoa");
		
		return sql.toString();
	}
	
	public List<Map> findMonitoramentosEnvioAgendamentoAutomatico() {
		StringBuilder hql = new StringBuilder();
		hql.append("select new Map(mcct.idMonitoramentoCCT as idMonitoramentoCCT, mcct.nrChave as nrChave, cr.idCliente as idClienteRemetente, "
				+ "cd.idCliente as idClienteDestinatario, ds.dtPrevEntrega as dtPrevEntrega, fo.idFilial as idFilialAtendeOperacional, "
				+ "ecc.blConfirmaAgendamento as blConfirmaAgendamento ) ");
		hql.append("from ");
		hql.append(getPersistentClass().getName()).append(" mcct, com.mercurio.lms.vendas.model.EnvioCteCliente ecc ");
		hql.append("inner join mcct.clienteRemetente cr ");
		hql.append("inner join cr.filialByIdFilialAtendeOperacional fo ");
		hql.append("inner join mcct.clienteDestinatario cd ");
		hql.append("inner join mcct.doctoServico ds ");
		hql.append("inner join ecc.cliente c ");
		hql.append("where ");
		hql.append("mcct.tpSituacaoNotaFiscalCCT = ? ");
		hql.append("and cr.blClienteCCT = ? ");
		hql.append("and c.idCliente = cd.idCliente ");
		hql.append("and ecc.tpParametrizacao = ? ");
		hql.append("and ecc.blAgendamentoAutomatico = ? ");
		hql.append("order by cr.idCliente,  cd.idCliente");

		List param = new ArrayList();
		param.add("PA");
		param.add(true);
		param.add("A");
		param.add(true);

		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
	}

	public List<Map> findMonitoramentosEnvioSolicitacaoPagtoICMS() {
		StringBuilder hql = new StringBuilder();
		hql.append("select new Map(mcct.idMonitoramentoCCT as idMonitoramentoCCT, mcct.nrChave as nrChave, cr.idCliente as idClienteRemetente, cd.idCliente as idClienteDestinatario) ");
		hql.append("from ");
		hql.append(getPersistentClass().getName()).append(" mcct ");
		hql.append("inner join mcct.clienteRemetente cr ");
		hql.append("inner join mcct.clienteDestinatario cd ");
		hql.append("where ");
		hql.append("mcct.tpSituacaoNotaFiscalCCT = ? ");
		hql.append("and cr.blClienteCCT = ? ");
		hql.append("and exists (select 1 from com.mercurio.lms.vendas.model.EnvioCteCliente ecc inner join ecc.cliente c where c.idCliente = cd.idCliente and ecc.tpParametrizacao = ?) ");
		hql.append("order by cr.idCliente,  cd.idCliente");

		List param = new ArrayList();
		param.add("IO");
		param.add(true);
		param.add("I");

		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
	}

	/**
	 * Como a quantidade de registros é limitada pela grid da tela, não tem
	 * problema em ser uma busca com IN.
	 * 
	 * @param criteria
	 * @return
	 */
	public List findChavesMonitoramentoCCTByIds(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("nrChave");
		hql.addFrom(MonitoramentoCCT.class.getName(), "m");
		hql.addCustomCriteria("m.idMonitoramentoCCT IN (" + criteria.getString("idsMonitoramentoCCT") + ")");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * 
	 * @param nrChave
	 */
	public void executeNfeAtualizaProcessamento(String nrChave) {
		if (nrChave != null) {
			nrChave = nrChave.trim();

			Session session = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();

			StringBuilder sql = new StringBuilder();
			sql.append("select a.XML_ID ");
			sql.append("from NFE_IDX a ");
			sql.append("where  a.XML_CHAVE_NFE = :nrChave ");
			sql.append("and not exists (select b.id_processo ");
			sql.append("from  nfe_processo_status b ");
			sql.append("where b.id_processo = a.xml_id ");
			sql.append("and rownum = 1  ");
			sql.append(") ");

			final ConfigureSqlQuery csq1 = new ConfigureSqlQuery() {
				public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("XML_ID", Hibernate.LONG);
				}
			};

			SQLQuery queryConsulta = session.createSQLQuery(sql.toString());
			queryConsulta.setParameter("nrChave", nrChave);
			csq1.configQuery(queryConsulta);

			List<Long> xmlIds = queryConsulta.list();

			if (xmlIds != null && !xmlIds.isEmpty()) {
				String sqlInsert = " insert into NFE_PROCESSO_STATUS (id_processo_status, id_processo, id_status, dt_status) values (:idProcessoStatus,:idProcesso,:idStatus,:dtStatus) ";
				SQLQuery query = session.createSQLQuery(sqlInsert);
				query.setLong("idProcessoStatus", 1L);
				query.setString("idStatus", "P");
				query.setTimestamp("dtStatus", JTDateTimeUtils.getDataHoraAtual().toDate());

				for (Long xmlId : xmlIds) {
					query.setLong("idProcesso", xmlId);
					query.executeUpdate();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<MonitoramentoCCT> findByTpSituacao(String[] situacoes) {
		Criteria c = getSession().createCriteria(getPersistentClass());
		c.add(Restrictions.in("tpSituacaoNotaFiscalCCT", situacoes));
		c.add(Restrictions.isNotNull("nrChave"));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	public List findNomeRemetenteByTpSituacao(String situacao) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct p.idPessoa as idPessoa, p.nmPessoa as nmPessoa, p.nrIdentificacao as nrIdentificacao ")
				.append(" FROM ").append(MonitoramentoCCT.class.getName())
				.append(" monitoramentoCCT ")
				.append(" join monitoramentoCCT.clienteRemetente cr ")
				.append(" join cr.pessoa p ")
				.append(" where monitoramentoCCT.tpSituacaoNotaFiscalCCT = ? ")
				.append(" and cr.blClienteCCT = ? ");

		List lista = getHibernateTemplate().find(sql.toString(), new Object[] { situacao, Boolean.TRUE });
		return lista;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDefinition) {
		List param = new ArrayList();
		StringBuilder sql = getSqlPaginated(criteria, false);

		ConfigureSqlQuery scalars = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idMonitoramentoCCT", Hibernate.LONG);
				sqlQuery.addScalar("nmClienteRemetente", Hibernate.STRING);
				sqlQuery.addScalar("nmClienteDestinatario", Hibernate.STRING);

				Properties domain = new Properties();
				domain.put("domainName", "DM_SITUACAO_NF_CCT");
				sqlQuery.addScalar("vlValorDominioSituacaoCCT",Hibernate.STRING);

				sqlQuery.addScalar("nrNotaFiscal", Hibernate.STRING);
				
				sqlQuery.addScalar("sgFilialOrigem", Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServico", Hibernate.STRING);
				sqlQuery.addScalar("sgFilialDestino", Hibernate.STRING);
				sqlQuery.addScalar("ciaAerea", Hibernate.STRING);
				sqlQuery.addScalar("dsSerie", Hibernate.STRING);
				sqlQuery.addScalar("nrAwb", Hibernate.LONG);
				sqlQuery.addScalar("dvAwb", Hibernate.INTEGER);
				sqlQuery.addScalar("dhEmissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("dhIcmsSolic", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dhIcmsPago", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dpe", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dtEntrega", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("blBloqueio",Hibernate.STRING);
				sqlQuery.addScalar("dsLocalizacao",Hibernate.STRING);
				sqlQuery.addScalar("sgEmpresa", Hibernate.STRING);
	
			}
		};
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), new Object[] {}, scalars);

		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		List<Object[]> list = rsp.getList();

		for (Object[] obj : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idMonitoramentoCCT", obj[0]);
			map.put("nmClienteRemetente", obj[1]);
			map.put("nmClienteDestinatario", obj[2]);
			map.put("vlValorDominioSituacaoCCT", obj[3]);
			map.put("nrNotaFiscal", NFEUtils.getNumeroNotaFiscalByChave((String)obj[4]));
			
			if (obj[5] == null) {
				if (obj[6] == null) {
					map.put("doctoServico", " ");
				} else {
					map.put("doctoServico", " " + obj[6]);
				}

			} else {
				if (obj[6] == null) {
					map.put("doctoServico", (obj[5]).toString());
				} else {
					map.put("doctoServico", (obj[5]).toString() + " " + obj[6]);
				}

			}
			
			map.put("sgfilialDestino", obj[7]);
			map.put("ciaAerea", obj[8]);
			
			
			map.put("serieNAwb", (obj[19] != null ? obj[19].toString() + " " : "") + AwbUtils.getNrAwbFormated(obj[9] != null ? obj[9].toString() : "0", 
								  			 Long.valueOf(obj[10] != null ? obj[10].toString() : "0"), 
								  	      Integer.valueOf(obj[11] != null ? obj[11].toString() : "0")));
			
			if(obj[12] != null){
				map.put("dhEmissao", obj[12]);
			}else{
				map.put("dhEmissao", "");
			}
			
			if(obj[13] != null){
				map.put("dhIcmsSolic", obj[13]);
			}else{
				map.put("dhIcmsSolic", "");
			}
			if(obj[14] != null){
				map.put("dhIcmsPago", obj[14]);
			}else{
				map.put("dhIcmsPago", "");
			}
			
			if(!dataAgendamento(Long.parseLong(obj[0].toString())).isEmpty()){
				if(dataAgendamento(Long.parseLong(obj[0].toString())).get(0) != null){
					map.put("dtAgendamento", dataAgendamento(Long.parseLong(obj[0].toString())).get(0));
				}		
			}else{
				map.put("dtAgendamento", "");
			}
			
			if(obj[15] != null){
				map.put("dpe", obj[15]);
			}else{
				map.put("dpe", "");
			}
			if(obj[16] != null){
				map.put("dtEntrega", obj[16]);
			}else{
				map.put("dtEntrega", "");
			}
			if(obj[17] == null){
				map.put("blBloqueio", "N");
			}else{
				map.put("blBloqueio", "S");
			}
			
			map.put("dsLocalizacao", obj[18]);
			
			newList.add(map);
		}

		rsp.setList(newList);

		return rsp;
	}

	public StringBuilder getSqlPaginated(Map<String, Object> criteria, boolean isRowCount) {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT distinct ");
		sql.append("MCCT.ID_MONITORAMENTO_CCT 													AS idMonitoramentoCCT, ");
		sql.append("PCR.NM_PESSOA 																AS nmClienteRemetente, ");
		sql.append("PCD.NM_PESSOA 																AS nmClienteDestinatario, ");
		sql.append("MCCT.TP_SITUACAO_NF_CCT 													AS vlValorDominioSituacaoCCT, ");
		sql.append("MCCT.NR_CHAVE 																AS nrNotaFiscal, ");
		sql.append("FO.SG_FILIAL  																AS sgFilialOrigem, ");
		sql.append("DS.NR_DOCTO_SERVICO  														as nrDoctoServico, ");
		sql.append("FD.SG_FILIAL  																AS sgFilialDestino, ");
		sql.append("PCA.NM_PESSOA																AS ciaAerea, ");
				sql.append("(select AWB.DS_SERIE from CTO_AWB, AWB where  DS.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO(+) AND "
				+ "CTO_AWB.ID_AWB = AWB.ID_AWB(+) AND  AWB.TP_STATUS_AWB = 'E' and rownum = 1)  AS dsSerie, ");
		sql.append("(select AWB.NR_AWB from CTO_AWB, AWB where  DS.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO(+) AND "
				+ "CTO_AWB.ID_AWB = AWB.ID_AWB(+) AND  AWB.TP_STATUS_AWB = 'E' and rownum = 1)  AS nrAwb, ");
		sql.append("(select AWB.DV_AWB from CTO_AWB, AWB where  DS.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO(+) AND"
				+ " CTO_AWB.ID_AWB = AWB.ID_AWB(+) AND  AWB.TP_STATUS_AWB = 'E' and rownum = 1) AS dvAwb, ");
		sql.append("DS.DH_EMISSAO 																AS dhEmissao, ");
		sql.append("MCCT.DT_ICMS_SOLIC 															AS dhIcmsSolic, ");
		sql.append("MCCT.DT_ICMS_PAGO 															AS dhIcmsPago, ");
		
			
		sql.append("DS.DT_PREV_ENTREGA                                                    	  	AS dpe, ");
		sql.append("(select max(EVENTO_DOCUMENTO_SERVICO.DH_EVENTO) from EVENTO_DOCUMENTO_SERVICO, EVENTO"
				+ " where MCCT.ID_DOCTO_SERVICO = EVENTO_DOCUMENTO_SERVICO.ID_DOCTO_SERVICO and "
				+ " EVENTO_DOCUMENTO_SERVICO.ID_EVENTO = EVENTO.ID_EVENTO and EVENTO.CD_EVENTO = 21 and "
				+ " EVENTO_DOCUMENTO_SERVICO.BL_EVENTO_CANCELADO = 'N')                 		AS dtEntrega, ");
		
		sql.append(" ODS.dh_bloqueio  							 								AS blBloqueio,");
		sql.append(PropertyVarcharI18nProjection.createProjection("LM.DS_LOCALIZACAO_MERCADORIA_I"));
		sql.append(" AS dsLocalizacao, ");
		sql.append(" EMP.SG_EMPRESA																AS sgEmpresa ");
		

		// FROM
		sql.append(" FROM ( "
				+ " SELECT   MCCT_AUX.ID_MONITORAMENTO_CCT, "
				+ "			DS_AUX.ID_DOCTO_SERVICO ,  "
				+ "	  		MAX(ODS_AUX.ID_OCORRENCIA_DOCTO_SERVICO) MX_ID_OCORRENCIA_DOCTO_SERVICO, "
				+ "			MAX(EDS_AUX.ID_EVENTO_DOCUMENTO_SERVICO) MX_ID_EVENTO_DOCUMENTO_SERVICO, "
				+ "			MAX(AG_AUX.ID_AGENDAMENTO_ENTREGA) MX_ID_AGENDAMENTO_ENTREGA, "
				+ "			MIN(IM_AUX.ID_EVENTO_MONITORAMENTO_CCT) MN_ID_EVENTO_MONITORAMENTO_CCT   "
				+ " FROM  "
				+ "			MONITORAMENTO_CCT MCCT_AUX "
				+ "			JOIN CLIENTE CR on MCCT_AUX.ID_CLIENTE_REM = CR.ID_CLIENTE "
				+ "			JOIN PESSOA PCR on CR.ID_CLIENTE = PCR.ID_PESSOA  "
				+ "			LEFT JOIN CLIENTE CD on  MCCT_AUX.ID_CLIENTE_DEST = CD.ID_CLIENTE "
				+ "			LEFT JOIN PESSOA PCD on CD.ID_CLIENTE = PCD.ID_PESSOA "
				+ "			LEFT JOIN DOCTO_SERVICO DS_AUX on MCCT_AUX.ID_DOCTO_SERVICO = DS_AUX.ID_DOCTO_SERVICO   "
				+ "			LEFT JOIN OCORRENCIA_DOCTO_SERVICO ODS_AUX on DS_AUX.ID_DOCTO_SERVICO = ODS_AUX.ID_DOCTO_SERVICO "
				+ "			LEFT JOIN EVENTO_DOCUMENTO_SERVICO EDS_AUX on DS_AUX.ID_DOCTO_SERVICO = EDS_AUX.ID_DOCTO_SERVICO and EDS_AUX.BL_EVENTO_CANCELADO = 'N' "
				+ "			LEFT JOIN AGENDAMENTO_MONIT_CCT AG_CCT on AG_CCT.ID_MONITORAMENTO_CCT = MCCT_AUX.ID_MONITORAMENTO_CCT "
				+ "			LEFT JOIN AGENDAMENTO_ENTREGA AG_AUX ON AG_AUX.ID_AGENDAMENTO_ENTREGA = AG_CCT.ID_AGENDAMENTO_ENTREGA "
				+ "			LEFT JOIN EVENTO_MONITORAMENTO_CCT IM_AUX on IM_AUX.ID_MONITORAMENTO_CCT = MCCT_AUX.ID_MONITORAMENTO_CCT and IM_AUX.TP_SITUACAO_NF_CCT = 'IO' ");
		sql.append(" WHERE 1=1 ");
		if (criteria.get("nrChaveNFe") != null) {
			sql.append(" AND MCCT_AUX.NR_CHAVE = '" + criteria.get("nrChaveNFe") + "' ");
		}
		if (criteria.get("nrNotaFiscal") != null) {
			sql.append(" AND SUBSTR(MCCT_AUX.NR_CHAVE,26,9) = '" + criteria.get("nrNotaFiscal") + "' ");
		}
		if (criteria.get("vlValorDominioSituacaoCCT") != null) {
			sql.append(" AND MCCT_AUX.TP_SITUACAO_NF_CCT = '" + criteria.get("vlValorDominioSituacaoCCT") + "' ");
		}

		if (criteria.get("blIcmsSolicitado") != null) {
			if (criteria.get("blIcmsSolicitado").toString().equals("S")) {
				sql.append(" AND MCCT_AUX.DT_ICMS_SOLIC IS NOT NULL ");
			} else {
				sql.append(" AND MCCT_AUX.DT_ICMS_SOLIC IS NULL ");
			}
		}
		if (criteria.get("idClienteRemetente") != null) {
			sql.append(" AND CR.ID_CLIENTE = " + criteria.get("idClienteRemetente") + " ");
		}
		if (criteria.get("idClienteDestinatario") != null) {
			sql.append(" AND CD.ID_CLIENTE = " + criteria.get("idClienteDestinatario") + " ");
		}
		if (criteria.get("idFilialDestino") != null) {
			sql.append(" AND NVL(DS_AUX.ID_FILIAL_DESTINO, CD.ID_FILIAL_ATENDE_OPERACIONAL) = " + criteria.get("idFilialDestino") + " ");
		}
		if (criteria.get("dhImportacaoInicial") != null && criteria.get("dhImportacaoFinal") != null) {
			sql.append(" AND IM_AUX.DH_INCLUSAO  BETWEEN TO_DATE('" + criteria.get("dhImportacaoInicial") + "' , 'yyyy-mm-dd') AND TO_DATE('" 
																	+ criteria.get("dhImportacaoFinal") + "' , 'yyyy-mm-dd') +1 ");
		}

		if (criteria.get("dhPrevisaoEntregaInicial") != null && criteria.get("dhPrevisaoEntregaFinal") != null) {
			sql.append(" AND DS_AUX.DT_PREV_ENTREGA  BETWEEN TO_DATE('" + criteria.get("dhPrevisaoEntregaInicial") + "' , 'yyyy-mm-dd') AND TO_DATE('"
																		+ criteria.get("dhPrevisaoEntregaFinal") + "' , 'yyyy-mm-dd') ");
		}
		if (criteria.get("dhAgendamentoInicial") != null && criteria.get("dhAgendamentoFinal") != null) {
			sql.append(" AND AG_AUX.DT_AGENDAMENTO  BETWEEN TO_DATE('" + criteria.get("dhAgendamentoInicial") + "' , 'yyyy-mm-dd') AND TO_DATE('"
																	   + criteria.get("dhAgendamentoFinal") + "' , 'yyyy-mm-dd') +1 ");
		}
		if (criteria.get("dhEntregaInicial") != null && criteria.get("dhEntregaFinal") != null) {
			sql.append(" AND EDS_AUX.DH_EVENTO  BETWEEN TO_DATE('" + criteria.get("dhEntregaInicial") + "' , 'yyyy-mm-dd') AND TO_DATE('"
																   + criteria.get("dhEntregaFinal") + "' , 'yyyy-mm-dd') +1 ");
		}

		sql.append(" GROUP BY MCCT_AUX.ID_MONITORAMENTO_CCT, DS_AUX.ID_DOCTO_SERVICO ) MCCT_AUX");

		sql.append(" 	JOIN MONITORAMENTO_CCT MCCT on MCCT.ID_MONITORAMENTO_CCT = MCCT_AUX.ID_MONITORAMENTO_CCT "
				+ "	JOIN CLIENTE CR on MCCT.ID_CLIENTE_REM = CR.ID_CLIENTE "
				+ "	JOIN PESSOA PCR on CR.ID_CLIENTE = PCR.ID_PESSOA  "
				+ "	LEFT JOIN CLIENTE CD on  MCCT.ID_CLIENTE_DEST = CD.ID_CLIENTE "
				+ "	LEFT JOIN PESSOA PCD on CD.ID_CLIENTE = PCD.ID_PESSOA "
				+ "	LEFT JOIN PEDIDO_COLETA PC on MCCT.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA "
				+ "	LEFT JOIN DOCTO_SERVICO DS on MCCT_AUX.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO "
				+ "	LEFT JOIN LOCALIZACAO_MERCADORIA LM on DS.ID_LOCALIZACAO_MERCADORIA = LM.ID_LOCALIZACAO_MERCADORIA "
				+ "	LEFT JOIN OCORRENCIA_DOCTO_SERVICO ODS on ODS.ID_OCORRENCIA_DOCTO_SERVICO = MCCT_AUX.MX_ID_OCORRENCIA_DOCTO_SERVICO "
				+ "	LEFT JOIN AGENDAMENTO_ENTREGA AG ON AG.ID_AGENDAMENTO_ENTREGA = MCCT_AUX.MX_ID_AGENDAMENTO_ENTREGA "
				+ "	LEFT JOIN EVENTO_MONITORAMENTO_CCT IM on IM.ID_EVENTO_MONITORAMENTO_CCT = MCCT_AUX.MN_ID_EVENTO_MONITORAMENTO_CCT "
				+ " left join FILIAL FO ON DS.ID_FILIAL_ORIGEM = FO.ID_FILIAL "
				+ " left join FILIAL FD ON DS.ID_FILIAL_DESTINO = FD.ID_FILIAL "
				+ " left join CTO_AWB CTO  ON CTO.ID_CONHECIMENTO = MCCT_AUX.ID_DOCTO_SERVICO "
				+ " left join AWB ON AWB.ID_AWB = CTO.ID_AWB  "
				+ " left join CIA_FILIAL_MERCURIO CIAFILIAL ON CIAFILIAL.ID_CIA_FILIAL_MERCURIO  = AWB.ID_CIA_FILIAL_MERCURIO  "
				+ " left join EMPRESA EMP ON EMP.ID_EMPRESA = CIAFILIAL.ID_EMPRESA "
				+ " left join PESSOA PCA ON  PCA.ID_PESSOA = CIAFILIAL.ID_EMPRESA "
				+ " LEFT JOIN CTO_AWB ON CTO_AWB.ID_AWB = AWB.ID_AWB "
				+ " LEFT JOIN DOCTO_SERVICO ON DOCTO_SERVICO.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO "
				+ " LEFT JOIN DOCTO_SERVICO DOCTSERV ON MCCT.ID_DOCTO_SERVICO = DOCTSERV.ID_DOCTO_SERVICO  "
				+ "	LEFT JOIN LOCALIZACAO_MERCADORIA LM ON DOCTSERV.ID_LOCALIZACAO_MERCADORIA = LM.ID_LOCALIZACAO_MERCADORIA "
				+ "	LEFT JOIN OCORRENCIA_DOCTO_SERVICO ODS ON  MCCT.ID_DOCTO_SERVICO =  ODS.ID_DOCTO_SERVICO and ODS.DH_LIBERACAO is null"
				
				);

		sql.append(" WHERE 1=1 ");

		if (criteria.get("dhColetaInicial") != null
				&& criteria.get("dhColetaFinal") != null) {
			sql.append(" AND PC.DH_PEDIDO_COLETA  BETWEEN TO_DATE('" + criteria.get("dhColetaInicial") + "' , 'yyyy-mm-dd') AND TO_DATE('"
																	 + criteria.get("dhColetaFinal") + "' , 'yyyy-mm-dd') +1 ");
		}

		sql.append("ORDER BY ");
		sql.append("PCR.NM_PESSOA ASC, ");
		sql.append("PCD.NM_PESSOA ASC, ");
		sql.append("MCCT.TP_SITUACAO_NF_CCT ASC, ");
		sql.append("MCCT.NR_CHAVE ASC");

		if (!sql.toString().equals("") && isRowCount) {
			StringBuilder sqlCount = new StringBuilder();
			sqlCount.append(" FROM ( ");
			sqlCount.append(sql);
			sqlCount.append(" ) ");
			return sqlCount;
		}

		return sql;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		StringBuilder sql = getSqlPaginated(criteria, true);
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), criteria);
	}

	public List findManifestoEntregaByMonitoramentoCCT(TypedFlatMap criteria) {
		ProjectionList pl = Projections
				.projectionList()
				.add(Projections.property("me.id"), "idManifestoEntrega")
				.add(Projections.property("f.sgFilial"), "sgFilialOrigem")
				.add(Projections.property("me.nrManifestoEntrega"),"nrManifestoEntrega")
				.add(Projections.property("m.tpManifestoEntrega"),"tpManifestoEntrega")
				.add(Projections.property("me.dhEmissao.value"), "dhEmissao")
				.add(Projections.property("oe.dsOcorrenciaEntrega"),"dsOcorrenciaEntrega")
				.add(Projections.property("med.dhOcorrencia.value"),"dhOcorrencia")
				.add(Projections.property("med.nmRecebedor"), "nmRecebedor");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"mcct");
		dc.createAlias("mcct.doctoServico", "dc");
		dc.createAlias("dc.manifestoEntregaDocumentos", "med");
		dc.createAlias("med.manifestoEntrega", "me");
		dc.createAlias("me.manifesto", "m");
		dc.createAlias("me.filial", "f");
		dc.createAlias("med.ocorrenciaEntrega", "oe");
		dc.setProjection(pl);
		dc.add(Restrictions.eq("mcct.id", criteria.get("idMonitoramentoCCT")));
		dc.addOrder(Order.asc("med.dhOcorrencia.value"));
		List<Object[]> resultList = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		List returnList = new ArrayList();
		for (Object[] obj : resultList) {
			Map row = new HashMap();
			row.put("idManifestoEntrega", obj[0]);
			row.put("nrManifestoEntrega", obj[1] + " " + obj[2]);
			row.put("tpManifestoEntrega",((DomainValue) obj[3]).getDescriptionAsString());
			row.put("dhEmissao", (JTDateTimeUtils.convertDataStringToYearMonthDay(obj[4].toString(), "yyyy-MM-dd HH:mm:ss.S")).toString("dd/MM/yyyy"));
			row.put("dsOcorrenciaEntrega", obj[5]);
			row.put("dhOcorrencia", (JTDateTimeUtils.convertDataStringToYearMonthDay(obj[6].toString(),"yyyy-MM-dd HH:mm:ss.S")).toString("dd/MM/yyyy"));
			row.put("nmRecebedor", obj[7]);
			returnList.add(row);
		}

		return returnList;
	}

	public void updateDataSolicitacaoICMS(List<String> chaves) {
		for (String chave : chaves) {
			StringBuffer sql = new StringBuffer().append("update ")
					.append(getPersistentClass().getName() + " as mcct ")
					.append(" set mcct.dtSolicitacaoICMS = sysdate ")
					.append(" where mcct.nrChave = ? ");

			List param = new ArrayList();
			param.add(chave);

			executeHql(sql.toString(), param);
		}
	}

	 @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> findInformacoesAwbByDoctoServico(Long idDoctoServico) {
		List<Long> param = new ArrayList<Long>();	
		StringBuilder sqlAwb = new StringBuilder();
		sqlAwb.append(" SELECT new Map(awb.nrAwb as nrAwb, awb.dvAwb as dvAwb, awb.dsSerie as dsSerie, pesemp.nmPessoa as ciaAerea, emp.sgEmpresa as sgEmpresa ) ");
		sqlAwb.append(" FROM " + DoctoServico.class.getName() + " do, " );
		sqlAwb.append(CtoAwb.class.getName() + " cto ");
		sqlAwb.append(" inner join cto.awb awb ");
		sqlAwb.append(" inner join awb.ciaFilialMercurio cia ");
		sqlAwb.append(" inner join cia.empresa emp ");
		sqlAwb.append(" inner join emp.pessoa pesemp ");
		sqlAwb.append(" inner join cto.conhecimento co ");
		sqlAwb.append(" WHERE do.idDoctoServico = co.idDoctoServico ");
		sqlAwb.append(" AND do.idDoctoServico = ? ");
		sqlAwb.append(" AND awb.tpStatusAwb = 'E' ");
		sqlAwb.append(" ORDER BY awb.dhEmissao.value DESC " );
		param.add(idDoctoServico);		
		return getAdsmHibernateTemplate().find(sqlAwb.toString(), param.toArray());
	}

	public String findInfoPedido(String nrChave) {
		
		Session session = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select laedc.valor_complemento as nrPedido ")
		.append(" from LOG_ARQUIVO_EDI_DETALHE_COMPL laedc, ")
		.append(" MONITORAMENTO_CCT mcct, ")
		.append(" LOG_ARQUIVO_EDI_DETALHE laed, ")
		.append(" PARAMETRO_GERAL pg ")
		.append(" where  mcct.nr_chave = laed.chave_nfe ")
		.append(" and  laed.id_log_arquivo_edi_detalhe = laedc.loadid_log_arquivo_edi_detalhe ")
		.append(" and  laedc.nome_complemento = pg.ds_conteudo ")
		.append(" and  pg.nm_parametro_geral = 'CAMPO_PEDIDO_CCT' ")
		.append(" and  mcct.nr_chave = :nrChave ");

		final ConfigureSqlQuery csq1 = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("nrPedido", Hibernate.STRING);
			}
		};
		
		SQLQuery queryConsulta = session.createSQLQuery(sql.toString());
		queryConsulta.setParameter("nrChave", nrChave);
		csq1.configQuery(queryConsulta);
				
		return (String) queryConsulta.uniqueResult();		
	}
	
	public List<Map> dataAgendamento(long idMonitoramento){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT   ae.dtAgendamento as dtAgendamento ")
		.append( " FROM " +  AgendamentoMonitCCT.class.getName() + " amcct " )
		.append(" inner join amcct.monitoramentoCCT mcct ")
		.append(" inner join amcct.agendamentoEntrega ae ")
		.append(" WHERE  mcct.idMonitoramentoCCT = ? ")
		.append(" and ae.tpSituacaoAgendamento NOT IN ('C','R') ")
		.append(" and ae.tpAgendamento <> 'TA' ")
		.append(" ORDER BY ae.idAgendamentoEntrega DESC " );
		
		List param = new ArrayList();
		param.add(idMonitoramento);
		

		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		
	}
	
	public String findDsTpSituacaoNfCctByVlSituacao(String valor){
		Session session = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();

		StringBuilder sql = new StringBuilder();
		sql.append(" select ").append(PropertyVarcharI18nProjection.createProjection(" VD.DS_VALOR_DOMINIO_I ")).append(" as situacao ")
		.append(" FROM MONITORAMENTO_CCT MCCT")
		.append(" join VALOR_DOMINIO VD ON MCCT.TP_SITUACAO_NF_CCT  = VD.VL_VALOR_DOMINIO ")
		.append(" inner join DOMINIO D ON D.ID_DOMINIO  = VD.ID_DOMINIO ")
		.append(" where D.NM_DOMINIO = 'DM_SITUACAO_NF_CCT' ")
		.append(" and MCCT.TP_SITUACAO_NF_CCT = :valorSituacao ");
		
		final ConfigureSqlQuery csq1 = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("situacao", Hibernate.STRING);
			}
		};

		SQLQuery queryConsulta = session.createSQLQuery(sql.toString());
		queryConsulta.setParameter("valorSituacao", valor);
		csq1.configQuery(queryConsulta);
				
		List<String> lista = queryConsulta.list();	
		if(lista.isEmpty()){
			return "";
		}else{
			return lista.get(0);
		}
	}
}
