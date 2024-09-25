package com.mercurio.lms.workflow.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.workflow.model.HistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class HistoricoWorkflowDAO extends BaseCrudDao<HistoricoWorkflow, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@SuppressWarnings("rawtypes")
	protected final Class getPersistentClass() {
		return HistoricoWorkflow.class;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pendencia", FetchMode.JOIN);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		String sql = getSqlPaginated(criteria, Boolean.TRUE);
		return getAdsmHibernateTemplate().getRowCountBySql(sql, criteria);
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		String sql = getSqlPaginated(criteria, Boolean.FALSE);
		return getAdsmHibernateTemplate().findPaginatedBySqlToMappedResult(sql, criteria, getConfigureSqlQueryPaginated());
	}
	
	private String getSqlPaginated(TypedFlatMap criteria, Boolean isRowCount) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("hw.id_historico_workflow as idHistoricoWorkflow");
		
		if(!isRowCount){
			sql.append(",us.nm_usuario as solicitante");
			sql.append(",TO_CHAR(hw.dh_solicitacao,'DD/MM/YYYY HH24:MI') as dataSolicitacao");
			sql.append(",a.nr_ordem_aprovacao as nrOrdemAprovacao");
			sql.append(",NVL(ua.nm_usuario, NVL(ui.nm_usuario, p.ds_perfil)) as aprovadorAcao");
			sql.append(",replace(replace(p.ds_pendencia,CHR(13),''),CHR(10),' ') as dsPendencia");
			sql.append(",(SELECT VI18N(vc.ds_valor_dominio_i) FROM dominio dc, valor_dominio vc WHERE dc.id_dominio = vc.id_dominio AND dc.nm_dominio = 'DM_TIPO_CAMPO_WORKFLOW' AND hw.tp_campo_workflow = vc.vl_valor_dominio ) as campoAlterado");
			sql.append(",hw.ds_vl_antigo as conteudoOriginal");
			sql.append(",hw.ds_vl_novo as conteudoSolicitado");
			sql.append(",TO_CHAR(a.dh_liberacao,'DD/MM/YYYY HH24:MI') as dataLiberacao");
			sql.append(",TO_CHAR(a.dh_acao,'DD/MM/YYYY HH24:MI') as dataAcao");
			sql.append(",VI18N(va.ds_valor_dominio_i) as acao");
			sql.append(",replace(replace(a.ob_acao,CHR(13),''),CHR(10),' ') as observacaoAcao");
			sql.append(",(SELECT u_ap.nm_usuario FROM acao a_ap, usuario u_ap WHERE a_ap.id_pendencia = p.id_pendencia AND u_ap.id_usuario = a_ap.id_usuario AND p.tp_situacao_pendencia <> 'E' AND a_ap.dh_acao = (SELECT MAX(dh_acao) FROM acao WHERE id_pendencia = p.id_pendencia)) as aprovadorFinal");
			sql.append(",(SELECT TO_CHAR(max(dh_acao),'DD/MM/YYYY HH24:MI') FROM acao WHERE id_pendencia = p.id_pendencia AND p.tp_situacao_pendencia <> 'E') as dataEncerramento");
			sql.append(",VI18N(vp.ds_valor_dominio_i) as situacao");
		}
		
		sql.append(" FROM ");
		sql.append("historico_workflow hw ");
		sql.append(",pendencia p ");
		sql.append(",acao a ");
		sql.append(",usuario us ");
		sql.append(",usuario ua ");
		sql.append(",dominio da ");
		sql.append(",valor_dominio va ");
		sql.append(",dominio dp ");
		sql.append(",valor_dominio vp ");
		sql.append(",integrante i ");
		sql.append(",perfil p ");
		sql.append(",usuario ui ");
		
		sql.append("WHERE ");
		sql.append("hw.id_pendencia = p.id_pendencia ");
		sql.append("AND p.id_pendencia = a.id_pendencia ");
		sql.append("AND hw.id_usuario = us.id_usuario ");
		sql.append("AND a.id_usuario = ua.id_usuario(+) ");
		sql.append("AND da.nm_dominio = 'DM_STATUS_ACAO_WORKFLOW' ");
		sql.append("AND da.id_dominio = va.id_dominio ");
		sql.append("AND a.tp_situacao_acao = va.vl_valor_dominio ");
		sql.append("AND dp.nm_dominio = 'DM_STATUS_WORKFLOW' ");
		sql.append("AND dp.id_dominio = vp.id_dominio ");
		sql.append("AND p.tp_situacao_pendencia = vp.vl_valor_dominio ");
		sql.append("AND a.id_integrante = i.id_integrante ");
		sql.append("AND i.id_perfil = p.id_perfil(+) ");
		sql.append("AND i.id_usuario = ui.id_usuario(+) ");
		sql.append("AND hw.id_processo = :idProcesso ");
		sql.append("AND hw.nm_tabela = :nmTabela ");
		
		aplicarFiltrosSqlPaginated(criteria, sql);
		
		sql.append("ORDER BY hw.tp_campo_workflow, hw.dh_solicitacao desc");
		return sql.toString();
	}

	private void aplicarFiltrosSqlPaginated(TypedFlatMap criteria, StringBuilder sql) {
		if(criteria.getLong("aprovadorAcao.idUsuario") != null ){
			sql.append("AND ua.id_usuario = :aprovadorAcao.idUsuario ");
		}
		
		if(criteria.getLong("solicitante.idUsuario") != null ){
			sql.append("AND hw.id_usuario = :solicitante.idUsuario ");
		}
		
		if(criteria.getYearMonthDay("dtAcaoInicial") != null){
			sql.append("AND TRUNC(a.dh_acao) >= TO_DATE(:dtAcaoInicial, 'yyyy-MM-dd') ");
		}
		
		if(criteria.getYearMonthDay("dtAcaoFinal") != null){
			sql.append("AND TRUNC(a.dh_acao) <= TO_DATE(:dtAcaoFinal, 'yyyy-MM-dd') ");
		}
		
		if(criteria.getYearMonthDay("dtSolicitacaoInicial") != null){
			sql.append("AND TRUNC(hw.dh_solicitacao) >= TO_DATE(:dtSolicitacaoInicial, 'yyyy-MM-dd') ");
		}
		
		if(criteria.getYearMonthDay("dtSolicitacaoFinal") != null){
			sql.append("AND TRUNC(hw.dh_solicitacao) <= TO_DATE(:dtSolicitacaoFinal, 'yyyy-MM-dd') ");
		}
		
		if(StringUtils.isNotBlank(criteria.getString("tpSituacaoAcao"))){
			sql.append("AND a.tp_situacao_acao = :tpSituacaoAcao ");
		}
		
		if(StringUtils.isNotBlank(criteria.getString("tpCampoWorkflow"))){
			sql.append("AND hw.tp_campo_workflow = :tpCampoWorkflow ");
		}
		
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryPaginated(){
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("idHistoricoWorkflow", Hibernate.LONG);
				sqlQuery.addScalar("solicitante", Hibernate.STRING);
				sqlQuery.addScalar("dataSolicitacao", Hibernate.STRING);
				sqlQuery.addScalar("nrOrdemAprovacao", Hibernate.STRING);
				sqlQuery.addScalar("aprovadorAcao", Hibernate.STRING);
				sqlQuery.addScalar("dsPendencia", Hibernate.STRING);
				sqlQuery.addScalar("campoAlterado", Hibernate.STRING);
				sqlQuery.addScalar("conteudoOriginal", Hibernate.STRING);
				sqlQuery.addScalar("conteudoSolicitado", Hibernate.STRING);
				sqlQuery.addScalar("dataLiberacao", Hibernate.STRING);
				sqlQuery.addScalar("dataAcao", Hibernate.STRING);
				sqlQuery.addScalar("acao", Hibernate.STRING);
				sqlQuery.addScalar("observacaoAcao", Hibernate.STRING);
				sqlQuery.addScalar("aprovadorFinal", Hibernate.STRING);
				sqlQuery.addScalar("dataEncerramento", Hibernate.STRING);
				sqlQuery.addScalar("situacao", Hibernate.STRING);
			}
		};
		return configSql;
	}
	
	public List<Map<String, Object>> findRelatorioHistoricoWorkflow(TypedFlatMap criteria) {
		Map<String, Object> parametersValues = new HashMap<String, Object>(); 
		StringBuilder sql = getSqlRelatorioHistoricoWorkflow(criteria, parametersValues);
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parametersValues, getConfigureSqlQueryRelatorioHistoricoWorkflow());
	}
	
	private StringBuilder getSqlRelatorioHistoricoWorkflow(TypedFlatMap criteria, Map<String, Object> parametersValues){
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append("us.nm_usuario as solicitante");
		sql.append(",TO_CHAR(hw.dh_solicitacao,'DD/MM/YYYY HH24:MI') as dataSolicitacao");
		sql.append(",f.sg_filial as filial");
		sql.append(",VI18N(vp.ds_valor_dominio_i) as situacao");
		sql.append(",a.nr_ordem_aprovacao as ordemAprovacao");
		sql.append(",NVL(ua.nm_usuario, NVL(ui.nm_usuario, p.ds_perfil)) as aprovadorAcao");
		sql.append(",VI18N(va.ds_valor_dominio_i) as acao");
		sql.append(",TO_CHAR(a.dh_acao,'DD/MM/YYYY HH24:MI') as dataAcao");
		sql.append(",replace(replace(p.ds_pendencia,CHR(13),''),CHR(10),' ') as dsPendencia");
		sql.append(getSqlCliente());
		sql.append(getSqlRazaoSocial());
		sql.append(getSqlDivisao());
		sql.append(getSqlTabelaPreco());
		sql.append(getSqlSubTipoTabelaPreco());
		sql.append(getSqlMunicipio());
		sql.append(getSqlLiberaGrandeCapital());
		sql.append(",(SELECT VI18N(vc.ds_valor_dominio_i) FROM dominio dc, valor_dominio vc WHERE dc.id_dominio = vc.id_dominio AND dc.nm_dominio = :dmTpCampoWk AND hw.tp_campo_workflow = vc.vl_valor_dominio ) as campoAlterado");
		sql.append(",hw.ds_vl_antigo as conteudoOriginal");
		sql.append(",hw.ds_vl_novo as conteudoSolicitado");
		sql.append(",replace(replace(hw.ds_observacao,CHR(13),''),CHR(10),' ') as motivoAlteracao");
		sql.append(",TO_CHAR(a.dh_liberacao,'DD/MM/YYYY HH24:MI') as dataLiberacao");
		sql.append(",replace(replace(a.ob_acao,CHR(13),''),CHR(10),' ') as observacaoAcao");
		sql.append(",(SELECT u_ap.nm_usuario FROM acao a_ap, usuario u_ap WHERE a_ap.id_pendencia = p.id_pendencia AND u_ap.id_usuario = a_ap.id_usuario AND p.tp_situacao_pendencia <> :tpStPendenciaEncerrada AND a_ap.dh_acao = (SELECT MAX(dh_acao) FROM acao WHERE id_pendencia = p.id_pendencia)) as aprovadorFinal");
		sql.append(",(SELECT TO_CHAR(max(dh_acao),'DD/MM/YYYY HH24:MI') FROM acao WHERE id_pendencia = p.id_pendencia AND p.tp_situacao_pendencia <> :tpStPendenciaEncerrada) as dataEncerramento");
		sql.append(" FROM ");
		sql.append("historico_workflow hw ");
		sql.append(",pendencia p ");
		sql.append(",ocorrencia o ");
		sql.append(",acao a ");
		sql.append(",usuario us ");
		sql.append(",usuario ua ");
		sql.append(",filial f ");
		sql.append(",regional_filial rf ");
		sql.append(",regional r ");
		sql.append(",dominio da ");
		sql.append(",valor_dominio va ");
		sql.append(",dominio dp ");
		sql.append(",valor_dominio vp ");
		sql.append(",integrante i ");
		sql.append(",perfil p");
		sql.append(",usuario ui ");
		sql.append("WHERE ");
		sql.append("hw.id_pendencia = p.id_pendencia ");
		sql.append("AND p.id_ocorrencia = o.id_ocorrencia ");
		sql.append("AND p.id_pendencia = a.id_pendencia ");
		sql.append("AND hw.id_usuario = us.id_usuario ");
		sql.append("AND a.id_usuario = ua.id_usuario(+) ");
		sql.append("AND o.id_filial = f.id_filial ");
		sql.append("AND rf.id_filial = f.id_filial ");
		sql.append("AND rf.id_regional = r.id_regional ");
		sql.append("AND rf.dt_vigencia_inicial <= sysdate "); 
		sql.append("AND rf.dt_vigencia_final >= sysdate ");
		sql.append("AND r.dt_vigencia_inicial <= sysdate "); 
		sql.append("AND r.dt_vigencia_final >= sysdate "); 
		sql.append("AND da.nm_dominio = :dmStAcaoWk ");
		sql.append("AND da.id_dominio = va.id_dominio ");
		sql.append("AND a.tp_situacao_acao = va.vl_valor_dominio ");
		sql.append("AND dp.nm_dominio = :dmStPendencia ");
		sql.append("AND dp.id_dominio = vp.id_dominio ");
		sql.append("AND p.tp_situacao_pendencia = vp.vl_valor_dominio ");		
		sql.append("AND a.id_integrante = i.id_integrante ");
		sql.append("AND i.id_perfil = p.id_perfil(+) ");
		sql.append("AND i.id_usuario = ui.id_usuario(+) ");
		
		aplicarFiltros(criteria, parametersValues, sql);
		
		sql.append("ORDER BY f.sg_filial, hw.dh_solicitacao, p.id_pendencia, a.nr_ordem_aprovacao");
		return sql;
	}
	
	@SuppressWarnings("unchecked")
	private void aplicarFiltros(TypedFlatMap criteria, Map<String, Object> parametersValues, StringBuilder sql) {
		parametersValues.put("tpStPendenciaEncerrada", "E");
		parametersValues.put("dmTpCampoWk", "DM_TIPO_CAMPO_WORKFLOW");
		parametersValues.put("dmStAcaoWk", "DM_STATUS_ACAO_WORKFLOW");
		parametersValues.put("dmStPendencia", "DM_STATUS_WORKFLOW");
		parametersValues.put("nmTbDivisao", PendenciaHistoricoDTO.TabelaHistoricoWorkflow.DIVISAO_CLIENTE.name());
		parametersValues.put("nmTbTabelaDivisaoCliente", PendenciaHistoricoDTO.TabelaHistoricoWorkflow.TABELA_DIVISAO_CLIENTE.name());
		parametersValues.put("nmTbTabelaPreco", PendenciaHistoricoDTO.TabelaHistoricoWorkflow.TABELA_PRECO.name());
		parametersValues.put("nmTbCliente", PendenciaHistoricoDTO.TabelaHistoricoWorkflow.CLIENTE.name());
		parametersValues.put("nmTbLiberacaoEmbarque", PendenciaHistoricoDTO.TabelaHistoricoWorkflow.LIBERACAO_EMBARQUE.name());
		parametersValues.put("nmTbTrtCliente", PendenciaHistoricoDTO.TabelaHistoricoWorkflow.TRT_CLIENTE.name());
		parametersValues.put("nmTbSimulacao", PendenciaHistoricoDTO.TabelaHistoricoWorkflow.SIMULACAO.name());
		
		if(isFiltroValido(criteria, "cliente")){
			sql.append("AND (");
			sql.append(" 	EXISTS (SELECT 1 FROM divisao_cliente dc WHERE dc.id_cliente = :idCliente AND dc.id_divisao_cliente = hw.id_processo AND hw.tp_campo_workflow = :stDivisaoCliente) ");
			sql.append("OR ");
			sql.append("	EXISTS (SELECT 1 FROM tabela_divisao_cliente tdc, divisao_cliente dc WHERE tdc.id_divisao_cliente = dc.id_divisao_cliente AND dc.id_cliente = :idCliente AND tdc.id_tabela_divisao_cliente = hw.id_processo AND hw.tp_campo_workflow = :psBaseCalculo) ");
			sql.append("OR ");
			sql.append("	EXISTS (SELECT 1 FROM cliente c WHERE c.id_cliente = :idCliente AND c.id_cliente = hw.id_processo AND hw.nm_tabela = :nmTbCliente) ");
			sql.append(") ");
			
			parametersValues.put("idCliente", ((Map<String, Long>)criteria.get("cliente")).get("idCliente"));
			parametersValues.put("stDivisaoCliente", PendenciaHistoricoDTO.CampoHistoricoWorkflow.STDV.name());
			parametersValues.put("psBaseCalculo", PendenciaHistoricoDTO.CampoHistoricoWorkflow.PBCL.name());
		}
		
		if(isFiltroValido(criteria, "idRegional")){
			sql.append("AND r.id_regional = :idRegional ");
			parametersValues.put("idRegional", criteria.get("idRegional"));
	}
	
		if(isFiltroValido(criteria, "filial")){
			sql.append("AND f.id_filial = :idFilial ");
			parametersValues.put("idFilial", ((Map<String, Long>)criteria.get("filial")).get("idFilial"));
		}
		
		if(isFiltroValido(criteria, "solicitante")){
			sql.append("AND hw.id_usuario = :idUsuario ");
			parametersValues.put("idUsuario", ((Map<String, Long>)criteria.get("solicitante")).get("idUsuario"));
		}
		
		if(isFiltroValido(criteria, "tpCampoWorkflow")){
			sql.append("AND hw.tp_campo_workflow  = :tpCampoWorkflow ");
			parametersValues.put("tpCampoWorkflow", ((Map<String, String>)criteria.get("tpCampoWorkflow")).get("value"));
		}
		
		if(isFiltroValido(criteria, "periodoInicial")){
			sql.append("AND TRUNC(hw.dh_solicitacao) >= :periodoInicial ");
			parametersValues.put("periodoInicial", new YearMonthDay(criteria.get("periodoInicial")));
		}
		
		if(isFiltroValido(criteria, "periodoFinal")){
			sql.append("AND TRUNC(hw.dh_solicitacao) <= :periodoFinal ");
			parametersValues.put("periodoFinal", new YearMonthDay(criteria.get("periodoFinal")));
		}
	}

	private Boolean isFiltroValido(TypedFlatMap criteria, String nmFiltro){
		return criteria.get(nmFiltro) != null && !"".equals(criteria.get(nmFiltro).toString());
	}
	
	private String getSqlCliente() {
		StringBuilder sqlCliente = new StringBuilder();
		sqlCliente.append(",CASE ");
		sqlCliente.append("	WHEN hw.nm_tabela = :nmTbDivisao ");
		sqlCliente.append("		THEN (SELECT CASE WHEN LENGTH(p.nr_identificacao) < 12 THEN REGEXP_REPLACE(LPAD(p.nr_identificacao, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ELSE REGEXP_REPLACE(LPAD(p.nr_identificacao, 14,'0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') END FROM pessoa p, divisao_cliente dc WHERE dc.id_cliente = p.id_pessoa AND dc.id_divisao_cliente = hw.id_processo) ");
		sqlCliente.append("	WHEN hw.nm_tabela = :nmTbTabelaDivisaoCliente ");
		sqlCliente.append("		THEN (SELECT CASE WHEN LENGTH(p.nr_identificacao) < 12 THEN REGEXP_REPLACE(LPAD(p.nr_identificacao, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ELSE REGEXP_REPLACE(LPAD(p.nr_identificacao, 14,'0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') END FROM pessoa p, divisao_cliente dc, tabela_divisao_cliente tdc WHERE dc.id_cliente = p.id_pessoa AND dc.id_divisao_cliente = tdc.id_divisao_cliente AND tdc.id_tabela_divisao_cliente = hw.id_processo) ");
		sqlCliente.append("	WHEN hw.nm_tabela = :nmTbCliente ");
		sqlCliente.append("		THEN (SELECT CASE WHEN LENGTH(p.nr_identificacao) < 12 THEN REGEXP_REPLACE(LPAD(p.nr_identificacao, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ELSE REGEXP_REPLACE(LPAD(p.nr_identificacao, 14,'0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') END FROM pessoa p WHERE p.id_pessoa = hw.id_processo) ");
		sqlCliente.append("	WHEN hw.nm_tabela = :nmTbLiberacaoEmbarque ");
		sqlCliente.append("		THEN (SELECT CASE WHEN LENGTH(p.nr_identificacao) < 12 THEN REGEXP_REPLACE(LPAD(p.nr_identificacao, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ELSE REGEXP_REPLACE(LPAD(p.nr_identificacao, 14,'0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') END FROM pessoa p, liberacao_embarque le WHERE p.id_pessoa = le.id_cliente AND le.id_liberacao_embarque = hw.id_processo) ");
		sqlCliente.append("	WHEN hw.nm_tabela = :nmTbTrtCliente ");
		sqlCliente.append("		THEN (SELECT CASE WHEN LENGTH(p.nr_identificacao) < 12 THEN REGEXP_REPLACE(LPAD(p.nr_identificacao, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ELSE REGEXP_REPLACE(LPAD(p.nr_identificacao, 14,'0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') END FROM pessoa p, trt_cliente tc WHERE p.id_pessoa = tc.id_cliente AND tc.id_trt_cliente = hw.id_processo) ");
		sqlCliente.append("	WHEN hw.nm_tabela = :nmTbSimulacao ");
		sqlCliente.append("		THEN (SELECT CASE WHEN LENGTH(p.nr_identificacao) < 12 THEN REGEXP_REPLACE(LPAD(p.nr_identificacao, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ELSE REGEXP_REPLACE(LPAD(p.nr_identificacao, 14,'0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') END FROM pessoa p, simulacao s WHERE p.id_pessoa = s.id_cliente AND s.id_simulacao = hw.id_processo) ");
		sqlCliente.append(" ELSE null ");
		sqlCliente.append("END as cliente "); 
		return sqlCliente.toString();
	}

	private String getSqlRazaoSocial() {
		StringBuilder sqlRazaoSocial = new StringBuilder();
		sqlRazaoSocial.append(",CASE ");
		sqlRazaoSocial.append("	WHEN hw.nm_tabela = :nmTbDivisao ");
		sqlRazaoSocial.append("		THEN (SELECT p.nm_pessoa FROM pessoa p, divisao_cliente dc WHERE dc.id_cliente = p.id_pessoa AND dc.id_divisao_cliente = hw.id_processo) ");
		sqlRazaoSocial.append("	WHEN hw.nm_tabela = :nmTbTabelaDivisaoCliente ");
		sqlRazaoSocial.append("		THEN (SELECT p.nm_pessoa FROM pessoa p, divisao_cliente dc, tabela_divisao_cliente tdc WHERE dc.id_cliente = p.id_pessoa AND dc.id_divisao_cliente = tdc.id_divisao_cliente AND tdc.id_tabela_divisao_cliente = hw.id_processo) ");
		sqlRazaoSocial.append("	WHEN hw.nm_tabela = :nmTbCliente ");
		sqlRazaoSocial.append("		THEN (SELECT p.nm_pessoa FROM pessoa p WHERE p.id_pessoa = hw.id_processo) ");
		sqlRazaoSocial.append("	WHEN hw.nm_tabela = :nmTbLiberacaoEmbarque ");
		sqlRazaoSocial.append("		THEN (SELECT p.nm_pessoa FROM pessoa p, liberacao_embarque le WHERE p.id_pessoa = le.id_cliente AND le.id_liberacao_embarque = hw.id_processo) ");
		sqlRazaoSocial.append("	WHEN hw.nm_tabela = :nmTbTrtCliente ");
		sqlRazaoSocial.append("		THEN (SELECT p.nm_pessoa FROM pessoa p, trt_cliente tc WHERE p.id_pessoa = tc.id_cliente AND tc.id_trt_cliente = hw.id_processo) ");
		sqlRazaoSocial.append("	WHEN hw.nm_tabela = :nmTbSimulacao ");
		sqlRazaoSocial.append("		THEN (SELECT p.nm_pessoa FROM pessoa p, simulacao s WHERE p.id_pessoa = s.id_cliente AND s.id_simulacao = hw.id_processo) ");
		sqlRazaoSocial.append(" ELSE null ");
		sqlRazaoSocial.append("END as razaoSocial ");
		return sqlRazaoSocial.toString();
	}
	
	private String getSqlDivisao() {
		StringBuilder sqlDivisao = new StringBuilder();
		sqlDivisao.append(",CASE ");
		sqlDivisao.append("	WHEN hw.nm_tabela = :nmTbDivisao ");
		sqlDivisao.append("		THEN (SELECT dc.ds_divisao_cliente FROM divisao_cliente dc WHERE dc.id_divisao_cliente = hw.id_processo) ");
		sqlDivisao.append("	WHEN hw.nm_tabela = :nmTbTabelaDivisaoCliente ");
		sqlDivisao.append("		THEN (SELECT dc.ds_divisao_cliente FROM divisao_cliente dc, tabela_divisao_cliente tdc WHERE dc.id_divisao_cliente = tdc.id_divisao_cliente AND tdc.id_tabela_divisao_cliente = hw.id_processo) ");
		sqlDivisao.append("	WHEN hw.nm_tabela = :nmTbSimulacao ");
		sqlDivisao.append("		THEN (SELECT dc.ds_divisao_cliente FROM divisao_cliente dc, simulacao s WHERE dc.id_divisao_cliente = s.id_divisao_cliente AND s.id_simulacao = hw.id_processo) ");
		sqlDivisao.append(" ELSE null ");
		sqlDivisao.append("END as divisao ");
		return sqlDivisao.toString();
	}
	
	private String getSqlTabelaPreco() {
		StringBuilder sqlRazaoSocial = new StringBuilder();
		sqlRazaoSocial.append(",CASE ");
		sqlRazaoSocial.append("	WHEN hw.nm_tabela = :nmTbDivisao ");
		sqlRazaoSocial.append("		THEN null ");
		sqlRazaoSocial.append("	WHEN hw.nm_tabela = :nmTbTabelaDivisaoCliente ");
		sqlRazaoSocial.append("		THEN (SELECT ttp.tp_tipo_tabela_preco || ttp.nr_versao FROM tipo_tabela_preco ttp, tabela_preco tp, tabela_divisao_cliente tdc WHERE ttp.id_tipo_tabela_preco = tp.id_tipo_tabela_preco AND tp.id_tabela_preco = tdc.id_tabela_preco AND tdc.id_tabela_divisao_cliente = hw.id_processo) ");
		sqlRazaoSocial.append("	WHEN hw.nm_tabela = :nmTbTabelaPreco ");
		sqlRazaoSocial.append("		THEN (SELECT ttp.tp_tipo_tabela_preco || ttp.nr_versao FROM tipo_tabela_preco ttp, tabela_preco tp WHERE ttp.id_tipo_tabela_preco = tp.id_tipo_tabela_preco AND tp.id_tabela_preco = hw.id_processo) ");
		sqlRazaoSocial.append("END as tabelaPreco ");
		return sqlRazaoSocial.toString();
	}
	
	private String getSqlSubTipoTabelaPreco() {
		StringBuilder sqlSubTipoTabelaPreco = new StringBuilder();
		sqlSubTipoTabelaPreco.append(",CASE ");
		sqlSubTipoTabelaPreco.append("	WHEN hw.nm_tabela = :nmTbDivisao ");
		sqlSubTipoTabelaPreco.append("		THEN null ");
		sqlSubTipoTabelaPreco.append("	WHEN hw.nm_tabela = :nmTbTabelaDivisaoCliente ");
		sqlSubTipoTabelaPreco.append("		THEN (SELECT stp.tp_subtipo_tabela_preco FROM subtipo_tabela_preco stp, tabela_preco tp, tabela_divisao_cliente tdc WHERE stp.id_subtipo_tabela_preco = tp.id_subtipo_tabela_preco AND tp.id_tabela_preco = tdc.id_tabela_preco AND tdc.id_tabela_divisao_cliente = hw.id_processo) ");
		sqlSubTipoTabelaPreco.append("	WHEN hw.nm_tabela = :nmTbTabelaPreco ");
		sqlSubTipoTabelaPreco.append("		THEN (SELECT stp.tp_subtipo_tabela_preco FROM subtipo_tabela_preco stp, tabela_preco tp WHERE stp.id_subtipo_tabela_preco = tp.id_subtipo_tabela_preco AND tp.id_tabela_preco = hw.id_processo) ");
		sqlSubTipoTabelaPreco.append("END as subtipoTabela ");
		return sqlSubTipoTabelaPreco.toString();
	}
	
	private String getSqlMunicipio() {
		StringBuilder sqlMunicipio = new StringBuilder();
		sqlMunicipio.append(",CASE ");
		sqlMunicipio.append("	WHEN hw.nm_tabela = :nmTbLiberacaoEmbarque ");
		sqlMunicipio.append("		THEN (SELECT m.nm_municipio FROM municipio m, liberacao_embarque le WHERE m.id_municipio = le.id_municipio AND le.id_liberacao_embarque = hw.id_processo) ");
		sqlMunicipio.append(" ELSE null ");
		sqlMunicipio.append("END as municipio ");
		return sqlMunicipio.toString();
	}
	
	private String getSqlLiberaGrandeCapital() {
		StringBuilder sqlLiberaGrandeCapital = new StringBuilder();
		sqlLiberaGrandeCapital.append(",CASE ");
		sqlLiberaGrandeCapital.append("  WHEN hw.nm_tabela = :nmTbLiberacaoEmbarque ");
		sqlLiberaGrandeCapital.append("		THEN (SELECT CASE WHEN le.bl_libera_grande_capital = 'S' THEN 'Sim' ELSE 'Não' END FROM liberacao_embarque le WHERE le.id_liberacao_embarque = hw.id_processo) ");
		sqlLiberaGrandeCapital.append(" ELSE null ");
		sqlLiberaGrandeCapital.append("END as liberaGrandeCapital ");
		return sqlLiberaGrandeCapital.toString();
	}
	
	
	private ConfigureSqlQuery getConfigureSqlQueryRelatorioHistoricoWorkflow(){
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("solicitante", Hibernate.STRING);
				sqlQuery.addScalar("dataSolicitacao", Hibernate.STRING);
				sqlQuery.addScalar("filial", Hibernate.STRING);
				sqlQuery.addScalar("situacao", Hibernate.STRING);
				sqlQuery.addScalar("ordemAprovacao", Hibernate.STRING);
				sqlQuery.addScalar("aprovadorAcao", Hibernate.STRING);
				sqlQuery.addScalar("acao", Hibernate.STRING);
				sqlQuery.addScalar("dataAcao", Hibernate.STRING);
				sqlQuery.addScalar("dsPendencia", Hibernate.STRING);
				sqlQuery.addScalar("cliente", Hibernate.STRING);
				sqlQuery.addScalar("razaoSocial", Hibernate.STRING);
				sqlQuery.addScalar("divisao", Hibernate.STRING);
				sqlQuery.addScalar("tabelaPreco", Hibernate.STRING);
				sqlQuery.addScalar("subtipoTabela", Hibernate.STRING);
				sqlQuery.addScalar("municipio", Hibernate.STRING);
				sqlQuery.addScalar("liberaGrandeCapital", Hibernate.STRING);
				sqlQuery.addScalar("campoAlterado", Hibernate.STRING);
				sqlQuery.addScalar("conteudoOriginal", Hibernate.STRING);
				sqlQuery.addScalar("conteudoSolicitado", Hibernate.STRING);
				sqlQuery.addScalar("motivoAlteracao", Hibernate.STRING);
				sqlQuery.addScalar("dataLiberacao", Hibernate.STRING);
				sqlQuery.addScalar("observacaoAcao", Hibernate.STRING);
				sqlQuery.addScalar("aprovadorFinal", Hibernate.STRING);
				sqlQuery.addScalar("dataEncerramento", Hibernate.STRING);
			}
		};
		return csq;
	}
	
	public List findPendenciaAprovacaoByTabelaCampos(Long idProcesso, TabelaHistoricoWorkflow tabelaHistoricoWorkflow, List<String> camposWK) {
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("tpCampoWK", Hibernate.STRING);
			}
		};

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT hw.tp_campo_workflow as tpCampoWK  ");
		sql.append("FROM HISTORICO_WORKFLOW hw, PENDENCIA p ");
		sql.append("WHERE ");
		sql.append("hw.id_pendencia = p.id_pendencia ");
		sql.append("AND p.tp_situacao_pendencia = :tpStPendencia ");
		sql.append("AND hw.id_processo = :idProcesso ");
		sql.append("AND hw.nm_tabela = :nmTabela ");
		sql.append("AND hw.tp_campo_workflow IN (:camposWK) ");
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tpStPendencia", "E");
		param.put("idProcesso", idProcesso);
		param.put("nmTabela", tabelaHistoricoWorkflow.name());
		param.put("camposWK", camposWK);

		return getAdsmHibernateTemplate().findBySql(sql.toString(), param, csq);
	}
	
	public Boolean validateWorkflowPendenciaAprovacao(Long idProcesso, TabelaHistoricoWorkflow tabelaHistoricoWorkflow,
			CampoHistoricoWorkflow campoHistoricoWorkflow) {
		List<Object> param = new ArrayList<Object>();

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT 1 FROM ");
		hql.append(getPersistentClass().getName()).append(" hw ");
		hql.append("inner join hw.pendencia p ");
		hql.append("WHERE ");
		hql.append("p.tpSituacaoPendencia = ? ");
		param.add("E");
		
		hql.append("AND hw.idProcesso = ? ");
		param.add(idProcesso);

		hql.append("AND hw.nmTabela = ? ");
		param.add(tabelaHistoricoWorkflow.name());

		if(campoHistoricoWorkflow != null){
			hql.append("AND hw.tpCampoWorkflow = ? ");
			param.add(campoHistoricoWorkflow.name());
		}

		List<?> registros = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());

		return !registros.isEmpty();
	}

	public Boolean validateHistoricoWorkflow(Long idProcesso, TabelaHistoricoWorkflow tabelaHistoricoWorkflow, CampoHistoricoWorkflow campoHistoricoWorkflow) {
		List<Object> param = new ArrayList<Object>();

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT 1 FROM ");
		hql.append(getPersistentClass().getName()).append(" hw ");
		hql.append("WHERE ");
		
		hql.append("hw.idProcesso = ? ");
		param.add(idProcesso);

		hql.append("AND hw.nmTabela = ? ");
		param.add(tabelaHistoricoWorkflow.name());

		if(campoHistoricoWorkflow != null){
			hql.append("AND hw.tpCampoWorkflow = ? ");
			param.add(campoHistoricoWorkflow.name());
		}
		
		List<?> registros = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
		return !registros.isEmpty();
	}
	
	public Long findIdPendencia(Long idProcesso, TabelaHistoricoWorkflow tabelaHistoricoWorkflow) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT p.idPendencia FROM ");
		hql.append(getPersistentClass().getName()).append(" hw ");
		hql.append("inner join hw.pendencia p ");
		hql.append("WHERE ");
		hql.append("p.tpSituacaoPendencia = ? ");
		hql.append("AND hw.idProcesso = ? ");
		hql.append("AND hw.nmTabela = ? ");

		List<Object> param = new ArrayList<Object>();
		param.add("E");
		param.add(idProcesso);
		param.add(tabelaHistoricoWorkflow.name());

		List<?> registros = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());

		if (registros != null && !registros.isEmpty()){
			return (Long) registros.get(0);			
		}
		return LongUtils.ZERO;
	}
	
	public List<HistoricoWorkflow> findByIdPendencia(Long idPendencia) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("hw");

		sql.addInnerJoin(getPersistentClass().getName(),"hw");

		sql.addInnerJoin("fetch hw.pendencia","pendencia");

		sql.addCriteria("pendencia.idPendencia", "=", idPendencia);
		
		List <HistoricoWorkflow> retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		return retorno;
	}

}