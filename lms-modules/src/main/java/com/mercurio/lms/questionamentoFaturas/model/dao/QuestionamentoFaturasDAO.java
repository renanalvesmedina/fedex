package com.mercurio.lms.questionamentoFaturas.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;

public class QuestionamentoFaturasDAO extends BaseCrudDao<QuestionamentoFatura, Long> {

	@Override
	protected Class getPersistentClass() {
		return QuestionamentoFatura.class;
	}

	public Map<String, Object> findQuestionamentoEmAnalise(String tpDocumento, Long idDoctoServico, Long idFatura) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("q.id"), "idQuestionamentoFatura")
			.add(Projections.property("q.tpSituacao"), "tpSituacao")
			.add(Projections.property("q.tpSetorCausadorAbatimento"), "tpSetorCausadorAbatimento")
			.add(Projections.property("md.id"), "idMotivoDesconto")
			.add(Projections.property("mc.id"), "idMotivoCancelamento")
			.add(Projections.property("mpv.id"), "idMotivoProrrogacaoVcto")
			.add(Projections.property("msp.id"), "idMotivoSustacaoProtesto")
			.add(Projections.property("q.dtVencimentoSolicitado"), "dtVencimentoSolicitado")
			.add(Projections.property("q.blConcedeAbatimentoSol"), "blConcedeAbatimentoSol")
			.add(Projections.property("q.blSustarProtestoSol"), "blSustarProtestoSol")
			.add(Projections.property("q.blBaixaTitCancelSol"), "blBaixaTitCancelSol")
			.add(Projections.property("q.blProrrogaVencimentoSol"), "blProrrogaVencimentoSol")
			.add(Projections.property("q.blRecalcularFreteSol"), "blRecalcularFreteSol")
			.add(Projections.property("q.vlAbatimentoSolicitado"), "vlAbatimentoSolicitado")
			.add(Projections.property("q.obAcaoCorretiva"), "obAcaoCorretiva")
			.add(Projections.property("q.obAbatimento"), "obAbatimento");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "q")
			.createAlias("q.motivoDesconto", "md", CriteriaSpecification.LEFT_JOIN)
			.createAlias("q.motivoCancelamento", "mc", CriteriaSpecification.LEFT_JOIN)
			.createAlias("q.motivoSustacaoProtesto", "msp", CriteriaSpecification.LEFT_JOIN)
			.createAlias("q.motivoProrrogacaoVcto", "mpv", CriteriaSpecification.LEFT_JOIN)
			.createAlias("q.doctoServico", "doctoServico", CriteriaSpecification.LEFT_JOIN)
			.createAlias("q.fatura", "fatura", CriteriaSpecification.LEFT_JOIN);
		
		dc.setProjection(pl);

		/** Verifica o Tipo de Documento */
		if("C".equals(tpDocumento)) {
			dc.add(Restrictions.eq("q.doctoServico.idDoctoServico", idDoctoServico));
		} else {
			dc.add(Restrictions.eq("q.fatura.idFatura", idFatura));
		}
		dc.add(Restrictions.ne("q.tpSituacao", "ACO"));
		dc.add(Restrictions.ne("q.tpSituacao", "CAN"));

		dc.add(Restrictions.ne("q.blConcedeAbatimentoSol", true));

		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return (Map<String, Object>)getAdsmHibernateTemplate().findUniqueResult(dc);
	}	

	public Boolean findQuestionamentoEmAnalise(Long idQuestionamentoFatura) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "q");
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("q.tpSituacao", "AA1"));
		dc.add(Restrictions.eq("q.id", idQuestionamentoFatura));
		return CompareUtils.gt((Integer)getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc), IntegerUtils.ZERO);
	}

	public ResultSetPage<QuestionamentoFatura> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = this.createBaseQuery(paginatedQuery.getCriteria());
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}

	private StringBuilder createBaseQuery(Map<String, Object> criteria) {
		StringBuilder query = new StringBuilder()
			.append("from QuestionamentoFatura as q ")
			.append("	inner join fetch q.filialCobradora as fc ")
			.append("	inner join fetch fc.pessoa as pfc ")
			.append("	inner join fetch q.filialSolicitante as fs ")
			.append("	inner join fetch fs.pessoa as pfs ")
			.append("	inner join fetch q.cliente as c ")
			.append("	inner join fetch c.pessoa as p ")
			.append("	inner join fetch q.usuarioSolicitante as us ")
			.append("	left join fetch us.usuarioADSM as usADSM ")
			.append("	left join fetch q.usuarioApropriador as ua ")
			.append("	left join fetch ua.usuarioADSM as uaADSM ")
			.append("	left join fetch q.motivoDesconto as md ")
			.append("	left join fetch q.motivoCancelamento as mc ")
			.append("	left join fetch q.motivoSustacaoProtesto as msp ")
			.append("	left join fetch q.motivoProrrogacaoVcto as mpv ")
			
			.append("	left join fetch q.fatura as fatura ")
			.append("	left join fetch fatura.filialByIdFilial as faturaFilial ")
			
			.append("	left join fetch q.doctoServico as doctoServico ")
			.append("	left join fetch doctoServico.filialByIdFilialOrigem as filialDoctoServico ")
			
			.append("where 1=1 ");

		if(MapUtils.getLong(criteria, "idQuestionamentoFatura") != null) {
			query.append("  and q.id = :idQuestionamentoFatura ");
		}
		/** Tipo de documento */
		String tpDocumento = MapUtils.getString(criteria, "tpDocumento");
		if(StringUtils.isNotBlank(tpDocumento)) {
			query.append("  and q.tpDocumento = :tpDocumento ");

			if("R".equals(tpDocumento)) {
				
				if(MapUtils.getObject(criteria, "sgFilialOrigem") != null) {
					query.append("  and q.fatura.filialByIdFilial.sgFilial = :sgFilialOrigem ");
				}
				if(MapUtils.getObject(criteria, "nrDocumento") != null) {
					query.append("  and q.fatura.nrFatura = :nrDocumento ");
				}
			} else {
				if(MapUtils.getObject(criteria, "sgFilialOrigem") != null) {
					query.append("  and q.doctoServico.filialByIdFilialOrigem.sgFilial = :sgFilialOrigem ");
				}
				if(MapUtils.getObject(criteria, "nrDocumento") != null) {
					query.append("  and q.doctoServico.nrDoctoServico = :nrDocumento ");
				}
			}
		}

		if(MapUtils.getObject(criteria, "dhSolicitacaoInicial") != null) {
			query.append("  and q.dhSolicitacao.value >= :dhSolicitacaoInicial ");
		}
		if(MapUtils.getObject(criteria, "dhSolicitacaoFinal") != null) {
			query.append("  and q.dhSolicitacao.value <= :dhSolicitacaoFinal ");
		}
		if(MapUtils.getObject(criteria, "idFilialCobradora") != null) {
			query.append("  and fc.id = :idFilialCobradora ");
		}
		if(MapUtils.getObject(criteria, "idUsuarioSolicitante") != null) {
			query.append("  and us.id = :idUsuarioSolicitante ");
		}
		if(MapUtils.getObject(criteria, "tpSituacao") != null) {
			query.append("  and q.tpSituacao = :tpSituacao ");
		}
		if(MapUtils.getObject(criteria, "idCliente") != null) {
			query.append("  and c.id = :idCliente ");
		} else if(MapUtils.getObject(criteria, "nrIdentificacaoCustom") != null) {
			query.append("  and p.tpPessoa = 'J' ");
			query.append("  and substring(p.nrIdentificacao, 1, 8) = :nrIdentificacaoCustom ");
		}
		if(MapUtils.getObject(criteria, "dhConclusaoInicial") != null) {
			query.append("  and q.dhConclusao.value >= :dhConclusaoInicial ");
		}
		if(MapUtils.getObject(criteria, "dhConclusaoFinal") != null) {
			query.append("  and q.dhConclusao.value <= :dhConclusaoFinal ");
		}
		if(MapUtils.getObject(criteria, "idUsuarioApropriador") != null) {
			query.append("  and ua.id = :idUsuarioApropriador ");
		}
		Object blSemApropriador = MapUtils.getObject(criteria, "blSemApropriador");
		if(blSemApropriador != null && BooleanUtils.toBoolean(blSemApropriador.toString())) {
			query.append("  and ua.id is null ");
		}
		query.append("order by q.dhSolicitacao.value ");
		return query;
	}

	public QuestionamentoFatura findByIdBasic(Long id) {
		StringBuilder query = new StringBuilder()
				.append("from QuestionamentoFatura as q ")
				
				.append("	left join fetch q.fatura as fatura ")
				.append("	left join fetch fatura.filialByIdFilial as faturaFilial ")
				
				.append("	left join fetch q.doctoServico as doctoServico ")
				.append("	left join fetch doctoServico.filialByIdFilialOrigem as filialDoctoServico ")

				.append("where q.idQuestionamentoFatura = :idQuestionamentoFatura");
		
		
		Map<String, Object> criteria = new HashMap<String, Object>(1);
		criteria.put("idQuestionamentoFatura", id);
		
		return (QuestionamentoFatura) getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}

	public QuestionamentoFatura findById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Problemas na execução da consulta!");
		}
		Map<String, Object> criteria = new HashMap<String, Object>(1);
		criteria.put("idQuestionamentoFatura", id);
		return (QuestionamentoFatura) getAdsmHibernateTemplate().findUniqueResult(this.createBaseQuery(criteria).toString(), criteria);
	}

	public QuestionamentoFatura findByIdFilialCobradoraRomaNumeroSituacao(Fatura fatura) {
		StringBuilder query = new StringBuilder();

		query.append("from QuestionamentoFatura as q ");
		query.append(" WHERE q.fatura.idFatura = :idFatura ");
		query.append(" AND q.tpSituacao NOT IN('ACO','CAN') ");
		
		Map<String, Object> criteria = new HashMap<String, Object>(1);
		criteria.put("idFatura", fatura.getIdFatura());
		
		return (QuestionamentoFatura)getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}

	public Serializable store(QuestionamentoFatura questionamentoFatura) {
		super.store(questionamentoFatura);
		return questionamentoFatura.getIdQuestionamentoFatura();
	}

	public UsuarioLMS findLastUserApropriador(Cliente cliente) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT max(q.usuarioApropriador.id) ");
		query.append(" FROM ").append(getPersistentClass().getName()).append(" as q ");
		query.append("	INNER JOIN q.cliente as c ");
		query.append("	INNER JOIN c.pessoa as p ");
		query.append(" WHERE q.tpSituacao NOT IN('ACO','CAN') ");

		Pessoa pessoa = cliente.getPessoa();
		if("J".equals(pessoa.getTpPessoa().getValue())) {
			query.append("   AND p.tpPessoa = 'J' ");
			query.append("	 AND substring(p.nrIdentificacao, 1, 8) = :nrIdentificacao ");
		} else {
			query.append("   AND c.id = :idCliente ");
		}

		Map<String, Object> criteria = new HashMap<String, Object>(1);
		criteria.put("nrIdentificacao", StringUtils.left(pessoa.getNrIdentificacao(), 8));
		criteria.put("idCliente", cliente.getIdCliente());
		Long idUsuarioApropriador = (Long)getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
		if(idUsuarioApropriador != null) {
			UsuarioLMS usuarioLMS = new UsuarioLMS();
			usuarioLMS.setIdUsuario(idUsuarioApropriador);
			return usuarioLMS;
		}
		return null;
	}

	/**
	 * Agrupa TODOS questionamento de mesma SITUACAO e de clientes de mesmo NR_IDENTIFICACAO_INICIAL, para o Usuario passado.
	 * @param idUsuarioApropriador
	 * @param tpSituacao
	 * @param idCliente
	 * @return
	 */
	public Integer updateApropriadorGrupoByPessoaJuridica(final Long idUsuarioApropriador, final String tpSituacao, final String nrIdentificacao) {
		return (Integer)getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				StringBuilder hql = new StringBuilder();
				hql.append(" UPDATE ").append(getPersistentClass().getName()).append(" as q ");
				hql.append(" SET q.usuarioApropriador.id = :idUsuarioApropriador ");
				hql.append(" WHERE q.tpSituacao = :tpSituacao ");
				hql.append("   AND q.cliente.id IN (");
				hql.append("   		SELECT c.id ");
				hql.append("   		FROM ").append(Cliente.class.getName()).append(" as c ");
				hql.append("   			INNER JOIN c.pessoa as p ");
				hql.append("   		WHERE substring(p.nrIdentificacao, 1, 8) = :nrIdentificacao ");
				hql.append("   )");

				Query query = session.createQuery(hql.toString());
				query.setParameter("idUsuarioApropriador", idUsuarioApropriador);
				query.setParameter("tpSituacao", tpSituacao);
                query.setParameter("nrIdentificacao", StringUtils.left(nrIdentificacao, 8));

   				int updateEntities = query.executeUpdate();
				return Integer.valueOf(updateEntities);
			}
		}, true);
	}

	/**
	 * Agrupa TODOS questionamento de mesma SITUACAO e CLIENTE, para o Usuario passado.
	 * @param idUsuarioApropriador
	 * @param tpSituacao
	 * @param idCliente
	 * @return
	 */
	public Integer updateApropriadorGrupoByPessoaFisica(final Long idUsuarioApropriador, final String tpSituacao, final Long idCliente) {
		return (Integer)getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				StringBuilder hql = new StringBuilder();
				hql.append(" UPDATE ").append(getPersistentClass().getName()).append(" as q ");
				hql.append(" SET q.usuarioApropriador.id = :idUsuarioApropriador ");
				hql.append(" WHERE q.tpSituacao = :tpSituacao ");
				hql.append("   AND q.cliente.id = :idCliente ");

				Query query = session.createQuery(hql.toString());
				query.setParameter("idUsuarioApropriador", idUsuarioApropriador);
				query.setParameter("tpSituacao", tpSituacao);
                query.setParameter("idCliente", idCliente);

   				int updateEntities = query.executeUpdate();
				return Integer.valueOf(updateEntities);
			}
		}, true);
	}

	/**
	 * LMS-6109 - Busca <tt>Filial</tt> para filial debitada de uma
	 * <tt>Fatura</tt>. Encontra a fatura a partir de um
	 * <tt>QuestionamentoFatura</tt> utilizando o id da filial de cobrança e
	 * número de romaneio do questionamento.
	 * 
	 * @param questionamentoFatura
	 *            questionamento para a busca da fatura e filial debitada
	 * @return filial debitada da fatura relacionada ao questionamento
	 */
	public Filial findFilialDebitadaByQuestionamentoFatura(QuestionamentoFatura questionamentoFatura) {
		return questionamentoFatura.getFatura().getFilialByIdFilialDebitada();
	}

	/**
	 * LMS-6109 - Busca id da filial de origem da <tt>Fatura</tt>. Encontra a
	 * fatura a partir de um <tt>QuestionamentoFatura</tt> utilizando o id da
	 * filial de cobrança e número de romaneio do questionamento.
	 * 
	 * @param questionamentoFatura
	 *            questionamento para a busca da fatura e filial de origem
	 * @return id da filial de origem da fatura relacionada ao questionamento
	 */
	public Long findIdFilialOrigemFatura(QuestionamentoFatura questionamentoFatura) {
		return questionamentoFatura.getFatura().getFilialByIdFilial().getIdFilial();
	}

	/**
	 * LMS-6109 - Busca id's das filiais de origem dos <tt>DoctoServico</tt>
	 * relacionados a <tt>Fatura</tt>. Encontra a fatura a partir de um
	 * <tt>QuestionamentoFatura</tt> utilizando o id da filial de cobrança e
	 * número de romaneio do questionamento.
	 * 
	 * @param questionamentoFatura
	 *            questionamento para a busca da fatura e documentos de serviço
	 * @return id's das filiais de origem dos documentos de serviço
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findListIdFilialOrigemDoctoServico(QuestionamentoFatura questionamentoFatura) {
		
		StringBuilder hql = new StringBuilder()
				.append("SELECT DISTINCT if.devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial ")
				.append("FROM QuestionamentoFatura qf ")
				.append("INNER JOIN qf.fatura f ")
				.append("INNER JOIN f.itemFaturas if ")
				.append("WHERE qf = ? ");
		
		return getAdsmHibernateTemplate().find(hql.toString(), questionamentoFatura);
	}

}