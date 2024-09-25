package com.mercurio.lms.vendas.model.dao;

import static org.hibernate.sql.JoinFragment.LEFT_OUTER_JOIN;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.ReajusteCliente;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class ReajusteClienteDAO extends BaseCrudDao<ReajusteCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return ReajusteCliente.class;
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaDivisaoCliente", FetchMode.JOIN);
		lazyFindById.put("tabelaDivisaoCliente.tabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("divisaoCliente", FetchMode.JOIN);
		lazyFindById.put("usuarioByIdUsuarioAprovou", FetchMode.JOIN);
		lazyFindById.put("pendenciaAprovacao", FetchMode.JOIN);
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("filial", FetchMode.JOIN);
	}

	public TypedFlatMap findDadosById(Long idReajusteCliente) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT New Map(rc.idReajusteCliente as idReajusteCliente,");
		hql.append("        rc.nrReajuste as nrReajuste,");
		hql.append("        rc.dtInicioVigencia as dtInicioVigencia,");
		hql.append("        rc.blEfetivado as blEfetivado,");
		hql.append("        rc.pcReajusteSugerido as pcReajusteSugerido,");
		hql.append("        rc.pcReajusteAcordado as pcReajusteAcordado,");
		hql.append("        rc.dsJustificativa as dsJustificativa,");
		hql.append("        rc.tpSituacaoAprovacao as tpSituacaoAprovacao,");
		hql.append("        rc.blReajustaPercTde as blReajustaPercTde,");
		hql.append("        rc.blReajustaPercTrt as blReajustaPercTrt,");
		hql.append("        rc.blReajustaPercGris as blReajustaPercGris,");
		hql.append("        rc.blReajustaAdValorEm as blReajustaAdValorEm,");
		hql.append("        rc.blReajustaAdValorEm2 as blReajustaAdValorEm2,");
		hql.append("        rc.blReajustaFretePercentual as blReajustaFretePercentual,");
		hql.append("        rc.blGerarSomenteMarcados as blGerarSomenteMarcados,");
		hql.append("        f.idFilial as filial_idFilial,");
		hql.append("        f.sgFilial as filial_sgFilial,");
		hql.append("        c.idCliente as cliente_idCliente,");
		hql.append("        p.nrIdentificacao as cliente_pessoa_nrIdentificacao,");
		hql.append("        p.tpIdentificacao as cliente_pessoa_tpIdentificacao,");
		hql.append("        p.nmPessoa as cliente_pessoa_nmPessoa,");
		hql.append("        d.idDivisaoCliente as divisaoCliente_idDivisaoCliente,");
		hql.append("        ttp.tpTipoTabelaPreco as tabelaPreco_tipoTabelaPreco_tpTipoTabelaPreco,");
		hql.append("        ttp.nrVersao as tabelaPreco_tipoTabelaPreco_nrVersao,");
		hql.append("        stp.tpSubtipoTabelaPreco as tabelaPreco_subtipoTabelaPreco_tpSubtipoTabelaPreco,");
		hql.append("        tp.dsDescricao as tabelaPreco_dsDescricao,");
		hql.append("        tp.idTabelaPreco as tabelaPreco_idTabelaPreco,");
		hql.append("        tdc.idTabelaDivisaoCliente as tabelaDivisaoCliente_idTabelaDivisaoCliente,");
		hql.append("        pa.idPendencia as pendenciaAprovacao_idPendencia)");
		hql.append(" FROM " + getPersistentClass().getName() + " rc");
		hql.append(" JOIN rc.cliente c");
		hql.append(" JOIN c.pessoa p");
		hql.append(" JOIN rc.filial f");
		hql.append(" JOIN rc.divisaoCliente d");
		hql.append(" JOIN rc.tabelaPreco tp");
		hql.append(" JOIN tp.tipoTabelaPreco ttp");
		hql.append(" JOIN tp.subtipoTabelaPreco stp");
		hql.append(" JOIN rc.tabelaDivisaoCliente tdc");
		hql.append(" LEFT JOIN rc.pendenciaAprovacao pa");
		hql.append(" WHERE rc.id = ?");

		Map result = (Map) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idReajusteCliente});
		return AliasToTypedFlatMapResultTransformer.getInstance().transformeTupleMap(result);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition def) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("rc.idReajusteCliente"), "idReajusteCliente")
			.add(Projections.property("rc.dtInicioVigencia"), "dtInicioVigencia")
			.add(Projections.property("rc.nrReajuste"), "nrReajuste")
			.add(Projections.property("rc.blEfetivado"), "blEfetivado")
			.add(Projections.property("rc.pcReajusteAcordado"), "pcReajusteAcordado")
			.add(Projections.property("rc.tpSituacaoAprovacao"), "tpSituacaoAprovacao")
			.add(Projections.property("p.nmPessoa"), "cliente_pessoa_nmPessoa")
			.add(Projections.property("ttp.tpTipoTabelaPreco"), "tipoTabelaPreco_tpTipoTabelaPreco")
			.add(Projections.property("ttp.nrVersao"), "tipoTabelaPreco_nrVersao")
			.add(Projections.property("stp.tpSubtipoTabelaPreco"), "subtipoTabelaPreco_tpSubtipoTabelaPreco")
			.add(Projections.property("tpSituacaoAprovacao"), "tpSituacaoAprovacao")
			.add(Projections.property("f.sgFilial"), "filial_sgFilial")
			.add(Projections.property("rc.blReajustaPercTde"), "blReajustaPercTde")
			.add(Projections.property("rc.blReajustaPercTrt"), "blReajustaPercTrt")
			.add(Projections.property("rc.blReajustaPercGris"), "blReajustaPercGris")
			.add(Projections.property("rc.blReajustaAdValorEm"), "blReajustaAdValorEm")
			.add(Projections.property("rc.blReajustaAdValorEm2"), "blReajustaAdValorEm2")
			.add(Projections.property("rc.blReajustaFretePercentual"), "blReajustaFretePercentual")
			.add(Projections.property("rc.blGerarSomenteMarcados"), "blGerarSomenteMarcados")
			;

		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(pl);
		dc.addOrder(Order.asc("nrReajuste"));
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	private DetachedCriteria createCriteriaPaginated(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "rc")
			.createAlias("rc.cliente", "c")
			.createAlias("c.pessoa", "p")
			.createAlias("rc.tabelaPreco", "tp", LEFT_OUTER_JOIN)
			.createAlias("tp.tipoTabelaPreco", "ttp", LEFT_OUTER_JOIN)
			.createAlias("tp.subtipoTabelaPreco", "stp", LEFT_OUTER_JOIN)
			.createAlias("rc.filial", "f");

		Long idFilial = criteria.getLong("filial.idFilial");
		if (idFilial != null) {
			dc.add(Restrictions.eq("f.idFilial", idFilial));
		}
		Long nrReajuste = criteria.getLong("nrReajuste");
		if (nrReajuste != null) {
			dc.add(Restrictions.eq("rc.nrReajuste", nrReajuste));
		}
		Long idCliente = criteria.getLong("cliente.idCliente");
		if (idCliente != null) {
			dc.add(Restrictions.eq("c.idCliente", idCliente));
		}
		Long idDivisaoCliente = criteria.getLong("divisaoCliente.idDivisaoCliente");
		if (idDivisaoCliente != null) {
			dc.add(Restrictions.eq("rc.divisaoCliente.id", idDivisaoCliente));
		}
		Long idTabelaDivisaoCliente = criteria.getLong("tabelaDivisaoCliente.idTabelaDivisaoCliente");
		if (idTabelaDivisaoCliente != null) {
			dc.add(Restrictions.eq("rc.tabelaDivisaoCliente.id", idTabelaDivisaoCliente));
		}
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaPreco != null) {
			dc.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco));
		}
		return dc;
	}
	
	
	public List<Map<String,Object>> findReajusteCliente(Long id, Long nrIdentificacao,  Long idFilial, YearMonthDay dataReajuste){
		String query = queryFindReajusteCliente(id, nrIdentificacao, idFilial, dataReajuste); 

		Map<String, Object> namedParams = new HashMap<String, Object>();
		namedParams.put("id", id);
		namedParams.put("idFilial", idFilial);
		namedParams.put("dataReajuste", dataReajuste);
		namedParams.put("nrIdentificacao", nrIdentificacao);
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(query, namedParams, getConfigureSqlFindReajusteCliente());
	}
	
	private ConfigureSqlQuery getConfigureSqlFindReajusteCliente(){
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("numeroReajusteFilial", Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacao", Hibernate.STRING);
				sqlQuery.addScalar("nome", Hibernate.STRING);
				sqlQuery.addScalar("divisaoCliente", Hibernate.STRING);
				sqlQuery.addScalar("tabelaAtual", Hibernate.STRING);
				sqlQuery.addScalar("tabelaNova", Hibernate.STRING);
				sqlQuery.addScalar("sgFilial", Hibernate.STRING);
				sqlQuery.addScalar("percSugerido", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("percAcordado", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dataReajuste", Hibernate.DATE);
				sqlQuery.addScalar("id", Hibernate.LONG);
				sqlQuery.addScalar("idDivisaoCliente", Hibernate.LONG);
				sqlQuery.addScalar("idTabDivisaoCliente", Hibernate.LONG);
				sqlQuery.addScalar("nrReajuste", Hibernate.LONG);
				sqlQuery.addScalar("idFilial", Hibernate.LONG);
				sqlQuery.addScalar("situacaoWorkflow", Hibernate.STRING);
				sqlQuery.addScalar("idUsuario", Hibernate.LONG);
				sqlQuery.addScalar("dataEfetivacao", Hibernate.DATE);
				sqlQuery.addScalar("efetivado", Hibernate.STRING);
				sqlQuery.addScalar("idCliente", Hibernate.LONG);
				sqlQuery.addScalar("idTabelaPreco", Hibernate.LONG);
				sqlQuery.addScalar("nomeUsuario", Hibernate.STRING);
				sqlQuery.addScalar("justificativa", Hibernate.STRING);
				sqlQuery.addScalar("situacaoWorkflowDesc", Hibernate.STRING);
				sqlQuery.addScalar("idPendenciaAprovacao" , Hibernate.LONG);
			}
		};
		return csq;
	}
	
	private String queryFindReajusteCliente(Long id, Long nrIdentificacao, Long idFilial, YearMonthDay dataReajuste){
		return new StringBuilder()
			.append(" select f.sg_filial || ' - ' || rc.nr_reajuste numeroReajusteFilial, p.nr_identificacao nrIdentificacao, p.nm_fantasia nome, dc.ds_divisao_cliente divisaoCliente,  ")
			.append("		 ttp.tp_tipo_tabela_preco || ttp.nr_versao || '-' || stp.tp_subtipo_tabela_preco tabelaAtual,  ")
			.append("		 ttp1.tp_tipo_tabela_preco || ttp1.nr_versao || '-' || stp1.tp_subtipo_tabela_preco tabelaNova, f.sg_filial sgFilial, ")
			.append("		 rc.PC_REAJUSTE_SUGERIDO percSugerido, rc.pc_reajuste_acordado percAcordado, rc.DT_INICIO_VIGENCIA dataReajuste,")
			.append("        rc.id_reajuste_cliente id, rc.id_divisao_cliente idDivisaoCliente, tdc.id_tabela_divisao_cliente idTabDivisaoCliente,  ")
			.append("        rc.nr_reajuste nrReajuste, f.id_filial idFilial, rc.tp_situacao_aprovacao situacaoWorkflow, rc.id_usuario_efetivacao idUsuario, ") 
			.append("        rc.dt_efetivacao dataEfetivacao, rc.bl_efetivado efetivado, rc.id_cliente idCliente, rc.id_tabela_preco idTabelaPreco, ")
			.append("        u.nm_usuario nomeUsuario, rc.ds_justificativa justificativa, ")
			.append("       ( select SUBSTR(REGEXP_SUBSTR(vd.ds_valor_dominio_i, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR(vd.ds_valor_dominio_i, 'pt_BR»[^¦]+'), 'pt_BR»')+LENGTH('pt_BR»'))  ")
			.append("           from dominio d, valor_dominio vd where d.nm_dominio = 'DM_STATUS_WORKFLOW' and d.id_dominio = vd.id_dominio and rc.tp_situacao_aprovacao = vd.vl_valor_dominio ") 
			.append("       ) as situacaoWorkflowDesc , rc.id_pendencia_aprovacao idPendenciaAprovacao") 
			.append(" from   reajuste_cliente rc, filial f, pessoa p, divisao_cliente dc, tabela_divisao_cliente tdc,  ")
			.append("	     tabela_preco tp, tipo_tabela_preco ttp, subtipo_tabela_preco stp, usuario u,  ")
			.append("        tabela_preco tp1, tipo_tabela_preco ttp1, subtipo_tabela_preco stp1 ")
			.append(" where  f.id_filial = rc.id_filial ")
			.append("	and  p.id_pessoa = rc.id_cliente ")
			.append("	and  rc.id_usuario_efetivacao = u.id_usuario(+) ") 
			.append("	and  tdc.id_tabela_divisao_cliente = rc.id_tabela_divisao_cliente	 ")
			.append("	and  dc.ID_DIVISAO_CLIENTE = tdc.ID_DIVISAO_CLIENTE	 ")
			.append("	and  tp.ID_TABELA_PRECO = tdc.ID_TABELA_PRECO ")
			.append("	and  ttp.ID_TIPO_TABELA_PRECO = tp.ID_TIPO_TABELA_PRECO	 ")
			.append("	and  stp.ID_SUBTIPO_TABELA_PRECO = tp.ID_SUBTIPO_TABELA_PRECO ")
			.append("	and  tp1.ID_TABELA_PRECO = rc.ID_TABELA_PRECO	 ")
			.append("	and  ttp1.ID_TIPO_TABELA_PRECO = tp1.ID_TIPO_TABELA_PRECO ")
			.append("	and  stp1.ID_SUBTIPO_TABELA_PRECO = tp1.ID_SUBTIPO_TABELA_PRECO ")
			.append( 	id == null 				? "" : " and rc.id_reajuste_cliente = :id ")
			.append( 	idFilial == null 		? "" : " and rc.id_filial = :idFilial ")
			.append( 	dataReajuste == null    ? "" : " and rc.dt_inicio_vigencia >= :dataReajuste ")
			.append( 	nrIdentificacao == null ? "" : " and p.nr_identificacao = :nrIdentificacao ")
			.append(" order by p.nr_identificacao,  rc.DT_INICIO_VIGENCIA, f.sg_filial, rc.nr_reajuste ") 
			.toString();
	}
	
	
	public void updateReajustecliente(Long idReajusteCliente, BigDecimal percAcordado, YearMonthDay dataVigenciaInicial, String justificativa) {
		String sql = new StringBuilder()
				.append("UPDATE reajuste_cliente ")
				.append("SET PC_REAJUSTE_ACORDADO = :percAcordado, ")
				.append("    DT_INICIO_VIGENCIA = :dataVigenciaInicial,  ")
				.append("    DS_JUSTIFICATIVA = :justificativa ")
				.append("WHERE ID_REAJUSTE_CLIENTE = :idReajusteCliente ")
				.toString();
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("percAcordado", percAcordado);
		parametersValues.put("dataVigenciaInicial", dataVigenciaInicial);
		parametersValues.put("justificativa", justificativa);
		parametersValues.put("idReajusteCliente", idReajusteCliente);
		getAdsmHibernateTemplate().executeUpdateBySql(sql, parametersValues);
	}
	
	public void updateEfetivaReajusteCliente(Long idReajusteCliente, Long idUsuarioEfetivacao) {
		String sql = new StringBuilder()
				.append("UPDATE reajuste_cliente ")
				.append("SET BL_EFETIVADO = 'S' , ")
				.append("    ID_USUARIO_EFETIVACAO = :idUsuarioEfetivacao , ")
				.append("    DT_EFETIVACAO = trunc(sysdate) ")
				.append("WHERE ID_REAJUSTE_CLIENTE = :idReajusteCliente ")
				.toString();
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idUsuarioEfetivacao", idUsuarioEfetivacao);
		parametersValues.put("idReajusteCliente", idReajusteCliente);
		getAdsmHibernateTemplate().executeUpdateBySql(sql, parametersValues);
	}
	
	public Integer existsReajusteClienteByDataVigenciaInicial(Long idReajusteCliente, Long idTabDivisaoCliente, YearMonthDay dataVigenciaInicial){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder()
			.append(" select 1 from REAJUSTE_CLIENTE ")
			.append(" where ID_TABELA_DIVISAO_CLIENTE = :idTabDivisaoCliente ")
			.append("   and DT_INICIO_VIGENCIA = :dataVigenciaInicial ");
			
		if(idReajusteCliente != null){
			sql.append(" and ID_REAJUSTE_CLIENTE <> :idReajusteCliente ");
			params.put("idReajusteCliente", idReajusteCliente);
		}
		
		params.put("dataVigenciaInicial", dataVigenciaInicial);
		params.put("idTabDivisaoCliente", idTabDivisaoCliente);
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), params);
	}

	public List<Map<String,Object>> findDadosReajustesEfetivados(){
		String query = new StringBuilder()
						.append(" select ")
						.append("   rc.id_reajuste_cliente id,  ")
						.append("   rc.id_tabela_divisao_cliente idTabDivisaoCliente, ")
						.append("   rc.ID_TABELA_PRECO tabNova, ")
						.append("   rc.PC_REAJUSTE_ACORDADO percentual, ")
						.append("   tdc.id_tabela_preco tabBase ")
						.append(" from  tabela_divisao_cliente tdc,   ")
						.append("       reajuste_cliente rc   ")
						.append(" where rc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente  ")
						.append("   and rc.BL_EFETIVADO = 'S' ")
						.append("   and rc.DT_INICIO_VIGENCIA = :vigenciaInicial ")
						.toString();
		
		Map<String, Object> namedParams = new HashMap<String, Object>();
		namedParams.put("vigenciaInicial", JTDateTimeUtils.getDataAtual());
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id", Hibernate.LONG);
				sqlQuery.addScalar("idTabDivisaoCliente", Hibernate.LONG);
				sqlQuery.addScalar("tabNova", Hibernate.LONG);
				sqlQuery.addScalar("percentual", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("tabBase", Hibernate.LONG);
			}
		};
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(query, namedParams, csq);
	}
	
	public Long getMaxNrReajuste(Long idFilial){
		String sql = "select MAX(nrReajuste) from ReajusteCliente where filial.idFilial = :idFilial";
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("idFilial", idFilial);
		
		return (Long)getAdsmHibernateTemplate().findUniqueResult(sql, param);
	}

}