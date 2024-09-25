package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.AnaliseCreditoCliente;
import com.mercurio.lms.workflow.model.Substituto;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class AnaliseCreditoClienteDAO extends BaseCrudDao<AnaliseCreditoCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return AnaliseCreditoCliente.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("usuario", FetchMode.JOIN);
	}

	public TypedFlatMap findByIdMapped(Long idAnaliseCreditoCliente) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("acc.id"), "idAnaliseCreditoCliente")
			.add(Projections.property("acc.tpSituacao"), "tpSituacao")
			.add(Projections.property("acc.blCreditoLiberado"), "blCreditoLiberado")
			.add(Projections.property("acc.dhSolicitacao.value"), "dhSolicitacao")
			.add(Projections.property("acc.dhConclusao.value"), "dhConclusao")
			.add(Projections.property("acc.dhUltimaConsultaSerasa.value"), "dhUltimaConsultaSerasa")
			/** Cliente */
			.add(Projections.property("c.id"), "cliente_idCliente")
			.add(Projections.property("c.vlFaturamentoPrevisto"), "vlFaturamentoPrevisto")
			.add(Projections.property("c.vlLimiteCredito"), "vlLimiteCredito")
			.add(Projections.property("c.tpCobrancaSolicitado"), "tpCobrancaSolicitado")
			.add(Projections.property("c.tpCobrancaAprovado"), "tpCobrancaAprovado")
			.add(Projections.property("c.tpSituacao"), "tpSituacaoCliente")
			.add(Projections.property("p.nrIdentificacao"), "cliente_pessoa_nrIdentificacao")
			.add(Projections.property("p.nmPessoa"), "cliente_pessoa_nmPessoa")
			.add(Projections.property("p.tpIdentificacao"), "cliente_pessoa_tpIdentificacao")
			.add(Projections.property("sm.id"), "segmentoMercado_idSegmentoMercado")
			.add(Projections.property("m.id"), "moedaByIdMoedaFatPrev_idMoeda")
			.add(Projections.property("m2.id"), "moedaByIdMoedaLimCred_idMoeda")
			/** Filial Comercial */
			.add(Projections.property("f.id"), "filial_idFilial")
			.add(Projections.property("f.sgFilial"), "filial_sgFilial")
			.add(Projections.property("pf.nmPessoa"), "filial_pessoa_nmFantasia")
			/** Usuario */
			.add(Projections.property("u.id"), "usuario_idUsuario")
			.add(Projections.property("u.nrMatricula"), "usuario_nrMatricula")
			.add(Projections.property("u.nmUsuario"), "usuario_nmUsuario");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "acc")
			.createAlias("acc.usuario", "u")
			.createAlias("acc.cliente", "c")
			.createAlias("c.pessoa", "p")
			.createAlias("c.segmentoMercado", "sm", CriteriaSpecification.LEFT_JOIN)
			.createAlias("c.moedaByIdMoedaFatPrev", "m", CriteriaSpecification.LEFT_JOIN)
			.createAlias("c.moedaByIdMoedaLimCred", "m2", CriteriaSpecification.LEFT_JOIN)
			.createAlias("c.filialByIdFilialAtendeComercial", "f")
			.createAlias("f.pessoa", "pf");
		dc.setProjection(pl);
		dc.add(Restrictions.eq("acc.id", idAnaliseCreditoCliente));
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return (TypedFlatMap)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public AnaliseCreditoCliente findByIdCliente(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq("cliente.id", idCliente));
		return (AnaliseCreditoCliente)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition def) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("acc.idAnaliseCreditoCliente"), "idAnaliseCreditoCliente")
			.add(Projections.property("acc.dhSolicitacao.value"), "dhSolicitacao")
			.add(Projections.property("f.sgFilial"), "filial_sgFilial")
			.add(Projections.property("p.nmPessoa"), "cliente_pessoa_nmPessoa")
			.add(Projections.property("u.nmUsuario"), "usuario_nmUsuario")
			.add(Projections.property("acc.tpSituacao"), "tpSituacao")
			.add(Projections.property("acc.dhConclusao.value"), "dhConclusao");

		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(pl);
		dc.addOrder(Order.asc("acc.dhSolicitacao"));
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	private DetachedCriteria createCriteriaPaginated(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "acc")
			.createAlias("acc.usuario", "u")
			.createAlias("acc.cliente", "c")
			.createAlias("c.pessoa", "p")
			.createAlias("c.filialByIdFilialAtendeComercial", "f");

		DateTime dhSolicitacaoInicial = criteria.getDateTime("dhSolicitacaoInicial");
		if (dhSolicitacaoInicial != null) {
			dc.add(Restrictions.ge("acc.dhSolicitacao", dhSolicitacaoInicial));
		}
		DateTime dhSolicitacaoFinal = criteria.getDateTime("dhSolicitacaoFinal");
		if (dhSolicitacaoFinal != null) {
			dc.add(Restrictions.le("acc.dhSolicitacao", dhSolicitacaoFinal));
		}
		Long idFilial = criteria.getLong("filial.idFilial");
		if (idFilial != null) {
			dc.add(Restrictions.eq("f.id", idFilial));
		}
		Long idUsuario = criteria.getLong("usuario.idUsuario");
		if (idUsuario != null) {
			dc.add(Restrictions.eq("u.id", idUsuario));
		}
		Long idCliente = criteria.getLong("cliente.idCliente");
		if (idCliente != null) {
			dc.add(Restrictions.eq("c.id", idCliente));
		}
		String tpSituacao = criteria.getString("tpSituacao");
		if (StringUtils.isNotBlank(tpSituacao)) {
			dc.add(Restrictions.eq("acc.tpSituacao", tpSituacao));
		}		
		DateTime dhConclusaoInicial = criteria.getDateTime("dhConclusaoInicial");
		if (dhConclusaoInicial != null) {
			dc.add(Restrictions.ge("acc.dhConclusao", dhConclusaoInicial));
		}
		DateTime dhConclusaoFinal = criteria.getDateTime("dhConclusaoFinal");
		if (dhConclusaoFinal != null) {
			dc.add(Restrictions.le("acc.dhConclusao", dhConclusaoFinal));
		}
		return dc;
	}

	/**
	 * Perfil
	 * @param idUsuario
	 * @param idsPerfil
	 * @return
	 */
	public Boolean allowUserAccessByPerfil(Long idUsuario, String...idsPerfil) {
		return !this.findUsersAccessByPerfil(idUsuario, idsPerfil).isEmpty();
	}
	public List<Usuario> findUsersAccessByPerfil(String...idsPerfil) {
		return findUsersAccessByPerfil(null, idsPerfil);
	}
	public List<Usuario> findUsersAccessByPerfil(Long idUsuario, String...idsPerfil) {
		/** Busca IDPerfil do Parametro Geral */
		DetachedCriteria dcParametroGeral = DetachedCriteria.forClass(ParametroGeral.class, "pg");
		dcParametroGeral.setProjection(Projections.property("pg.dsConteudo"));
		dcParametroGeral.add(Restrictions.in("pg.nmParametroGeral",idsPerfil));

		/** Busca Perfil do Usuario */
		DetachedCriteria dc = DetachedCriteria.forClass(PerfilUsuario.class, "pu");
		dc.setProjection(Projections.property("pu.usuario"));
		if(idUsuario != null) {
			dc.add(Restrictions.eq("pu.usuario.id", idUsuario));
		}
		dc.add(Subqueries.propertyEq("pu.perfil.id", dcParametroGeral));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Substituto
	 * @param idUsuario
	 * @param idsPerfil
	 * @return
	 */
	public Boolean allowUserAccessByPerfilSubstituto(Long idUsuario, String...idsPerfil) {
		return !this.findUsersAccessByPerfilSubstituto(idUsuario, idsPerfil).isEmpty();
	}
	public List<Usuario> findUsersAccessByPerfilSubstituto(String...idsPerfil) {
		return findUsersAccessByPerfilSubstituto(null, idsPerfil);
	}
	public List<Usuario> findUsersAccessByPerfilSubstituto(Long idUsuario, String...idsPerfil) {
		/** Busca IDPerfil do Parametro Geral */
		DetachedCriteria dcParametroGeral = DetachedCriteria.forClass(ParametroGeral.class, "pg");
		dcParametroGeral.setProjection(Projections.property("pg.dsConteudo"));
		dcParametroGeral.add(Restrictions.in("pg.nmParametroGeral",idsPerfil));

		/** Busca PerfilUsuario */
		DetachedCriteria dcPerfilUsuario = DetachedCriteria.forClass(PerfilUsuario.class, "pu");
		dcPerfilUsuario.setProjection(Projections.distinct(Projections.property("pu.usuario.id")));
		dcPerfilUsuario.add(Subqueries.propertyEq("pu.perfil.id", dcParametroGeral));

		/** Busca UsuarioSubstituto */
		DetachedCriteria dc = DetachedCriteria.forClass(Substituto.class, "s");
		dc.setProjection(Projections.property("s.usuarioByIdUsuarioSubstituto"));

		YearMonthDay today = JTDateTimeUtils.getDataAtual();
		dc.add(Restrictions.le("s.dtSubstituicaoInicial", today));
		dc.add(Restrictions.ge("s.dtSubstituicaoFinal", today));
		if(idUsuario != null) {
			dc.add(Restrictions.eq("s.usuarioByIdUsuarioSubstituto.id", idUsuario));
		}
		dc.add(Subqueries.propertyIn("s.usuarioByIdUsuarioSubstituido.id", dcPerfilUsuario));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
}