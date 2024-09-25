package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.AnexoSolicContratacao;
import com.mercurio.lms.contratacaoveiculos.model.FluxoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.municipios.model.TipoMeioTranspRotaEvent;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;

/**
 * DAO pattern.
 * <p>
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 *
 * @spring.bean 
 */
public class SolicitacaoContratacaoDAO extends BaseCrudDao<SolicitacaoContratacao, Long> {

    private static final int MAX_UPDATE_SIZE = 300;

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return SolicitacaoContratacao.class;
	}

	protected void initFindLookupLazyProperties(Map map) {
		map.put("tipoMeioTransporte", FetchMode.JOIN);
		map.put("tipoMeioTransporte.tipoMeioTransporte",FetchMode.JOIN);
		map.put("rota", FetchMode.JOIN);
		map.put("filial", FetchMode.JOIN);
		map.put("filial.pessoa", FetchMode.JOIN);
		map.put("moedaPais", FetchMode.JOIN);
		map.put("moedaPais.moeda", FetchMode.JOIN);
	}
	
	protected void initFindByIdLazyProperties(Map map) {
		map.put("rota", FetchMode.JOIN);
		map.put("moedaPais", FetchMode.JOIN);
		map.put("moedaPais.moeda", FetchMode.JOIN);
		map.put("filial", FetchMode.JOIN);
		map.put("rotaIdaVolta", FetchMode.JOIN);
		map.put("rotaIdaVolta.rota", FetchMode.JOIN);
		map.put("rotaIdaVolta.rotaViagem", FetchMode.JOIN);
	}

	@Override
	public void removeById(Long id) {
		SolicitacaoContratacao solicitacaoContratacao = (SolicitacaoContratacao)getAdsmHibernateTemplate().load(SolicitacaoContratacao.class,id);

		List<TabelaColetaEntrega> tabelasColetaEntrega = solicitacaoContratacao.getTabelaColetaEntregas();
		if(tabelasColetaEntrega != null) {
			tabelasColetaEntrega.clear();
		}
		List<FluxoContratacao> fluxosConstratacao = solicitacaoContratacao.getFluxosContratacao();
		if(fluxosConstratacao != null) {
			fluxosConstratacao.clear();
		}
		getAdsmHibernateTemplate().delete(solicitacaoContratacao);
	}

	public List findObAcaoByIdSolicitacao(Long idSolicitacao) {
		DetachedCriteria dc = createDetachedCriteria()
		.setProjection(Projections.property("A.obAcao"))
		.createAlias("pendencia","P")
		.createAlias("P.acoes","A")
		.add(Restrictions.eq("idSolicitacaoContratacao",idSolicitacao))
		.add(Restrictions.isNotNull("A.obAcao"))
		.add(Restrictions.eq("A.tpSituacaoAcao","R"))
		.addOrder(Order.desc("A.dhAcao.value"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List findVlMaxAndDtViagemById(Long idSolicitacao) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.alias(Projections.property("PTC.vlMaximoAprovado"),"vlMaximoAprovado"));

		DetachedCriteria dc = DetachedCriteria.forClass(ParcelaTabelaCe.class,"PTC")
		.createAlias("PTC.tabelaColetaEntrega","TCE")
		.createAlias("TCE.solicitacaoContratacao","SC")
		.add(Restrictions.eq("SC.idSolicitacaoContratacao",idSolicitacao))
		.add(Restrictions.isNotNull("PTC.vlMaximoAprovado"))
		.setProjection(pl)
		.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List findRotaViagemVigente(Long idTipoMeioTransporte,String dsRota,Long idMoedaPais) {
		DetachedCriteria dc = DetachedCriteria.forClass(TipoMeioTranspRotaEvent.class,"TMTRE")
		.createAlias("TMTRE.rotaIdaVolta","RIV")
		.createAlias("RIV.moedaPais","MP")
		.createAlias("RIV.rota","R")
		.createAlias("RIV.rotaViagem","RV")
		.createAlias("TMTRE.tipoMeioTransporte","TMT")
		.add(Restrictions.le("RV.dtVigenciaInicial",JTDateTimeUtils.getDataAtual()))
		.add(Restrictions.or(Restrictions.ge("RV.dtVigenciaFinal",JTDateTimeUtils.getDataAtual()),Restrictions.isNull("RV.dtVigenciaFinal")))
		.add(Restrictions.le("TMTRE.dtVigenciaInicial",JTDateTimeUtils.getDataAtual()))
		.add(Restrictions.or(Restrictions.ge("TMTRE.dtVigenciaFinal",JTDateTimeUtils.getDataAtual()),Restrictions.isNull("TMTRE.dtVigenciaFinal")))
		.add(Restrictions.eq("TMT.idTipoMeioTransporte",idTipoMeioTransporte))
		.add(Restrictions.eq("R.dsRota",dsRota))
		.add(Restrictions.eq("MP.idMoedaPais",idMoedaPais));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		ProjectionList projections = Projections.projectionList()
		.add(Projections.alias(Projections.property("SC.tpSolicitacaoContratacao"),"tpSolicitacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.idSolicitacaoContratacao"),"idSolicitacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.nrSolicitacaoContratacao"),"nrSolicitacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.dtCriacao"),"dtCriacao"))
		.add(Projections.alias(Projections.property("SC.tpSituacaoContratacao"),"tpSituacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.tpAbrangencia"),"tpAbrangencia"))
		.add(Projections.alias(Projections.property("SC.tpRotaSolicitacao"),"tpRotaSolicitacao"))
		.add(Projections.alias(Projections.property("SC.vlFreteSugerido"),"vlFreteSugerido"))
		.add(Projections.alias(Projections.property("SC.vlFreteMaximoAutorizado"),"vlFreteMaximoAutorizado"))
		.add(Projections.alias(Projections.property("SC.vlFreteNegociado"),"vlFreteNegociado"))
		// LMSA-6520: LMSA-6534
		.add(Projections.alias(Projections.property("SC.tpCargaCompartilhada"),"tpCargaCompartilhada"))

		.add(Projections.alias(Projections.property("SC.nrIdentificacaoMeioTransp"),"nrIdentificacaoMeioTransp"))
		.add(Projections.alias(Projections.property("SC.nrIdentificacaoSemiReboque"),"nrIdentificacaoSemiReboque"))

		.add(Projections.alias(Projections.property("TMT.dsTipoMeioTransporte"),"tipoMeioTransporte_dsTipoMeioTransporte"))
		.add(Projections.alias(Projections.property("TMT.idTipoMeioTransporte"),"tipoMeioTransporte_idTipoMeioTransporte"))
		.add(Projections.alias(Projections.property("TMT.tpMeioTransporte"),"tipoMeioTransporte_tpMeioTransporte"))
		.add(Projections.alias(Projections.property("F.sgFilial"),"filial_sgFilial"))
		.add(Projections.alias(Projections.property("F.idFilial"),"filial_idFilial"))
		.add(Projections.alias(Projections.property("P.nmFantasia"),"filial_pessoa_nmFantasia"))
		.add(Projections.alias(Projections.property("US.nmUsuario"),"usuarioSolicitador_nmUsuario"))

		.add(Projections.alias(Projections.property("SC.vlFreteNegociado"),"vlFreteNegociado"))
		.add(Projections.alias(Projections.property("moeda.idMoeda"),"moedaPais_moeda_idMoeda"))
		.add(Projections.alias(Projections.property("moeda.sgMoeda"),"moedaPais_moeda_sgMoeda"))
		.add(Projections.alias(Projections.property("moeda.dsSimbolo"),"moedaPais_moeda_dsSimbolo"))
		.add(Projections.alias(Projections.property("rota.idRota"),"rota_idRota"))
		.add(Projections.alias(Projections.property("rota.dsRota"),"rota_dsRota"));

		DetachedCriteria dc = createFindToPaginatedAndRowCount(criteria)
		.setProjection(projections)
		.setResultTransformer(AliasToNestedMapResultTransformer.getInstance())
		.addOrder(Order.asc("F.sgFilial"))
		.addOrder(Order.asc("SC.tpSolicitacaoContratacao"))
		.addOrder(Order.asc("SC.nrSolicitacaoContratacao"));

		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc,findDef.getCurrentPage(),findDef.getPageSize());						
	}

	public List findLookup(TypedFlatMap criteria) {
		ProjectionList projections = Projections.projectionList()
		.add(Projections.alias(Projections.property("SC.tpSolicitacaoContratacao"),"tpSolicitacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.idSolicitacaoContratacao"),"idSolicitacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.nrSolicitacaoContratacao"),"nrSolicitacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.dtCriacao"),"dtCriacao"))
		.add(Projections.alias(Projections.property("SC.tpSituacaoContratacao"),"tpSituacaoContratacao"))

		.add(Projections.alias(Projections.property("SC.nrIdentificacaoMeioTransp"),"nrIdentificacaoMeioTransp"))
		.add(Projections.alias(Projections.property("SC.nrIdentificacaoSemiReboque"),"nrIdentificacaoSemiReboque"))

		.add(Projections.alias(Projections.property("TMT.dsTipoMeioTransporte"),"tipoMeioTransporte_dsTipoMeioTransporte"))
		.add(Projections.alias(Projections.property("TMT.idTipoMeioTransporte"),"tipoMeioTransporte_idTipoMeioTransporte"))
		.add(Projections.alias(Projections.property("TMT.tpMeioTransporte"),"tipoMeioTransporte_tpMeioTransporte"))
		.add(Projections.alias(Projections.property("F.sgFilial"),"filial_sgFilial"))
		.add(Projections.alias(Projections.property("F.idFilial"),"filial_idFilial"))
		.add(Projections.alias(Projections.property("P.nmFantasia"),"filial_pessoa_nmFantasia"))
		.add(Projections.alias(Projections.property("US.nmUsuario"),"usuarioSolicitador_nmUsuario"));

		DetachedCriteria dc = createFindToPaginatedAndRowCount(criteria)
		.setProjection(projections)
		.setResultTransformer(AliasToNestedMapResultTransformer.getInstance())
		.addOrder(Order.asc("F.sgFilial"))
		.addOrder(Order.asc("SC.tpSolicitacaoContratacao"))
		.addOrder(Order.asc("SC.nrSolicitacaoContratacao"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);						
	}

	public Map findByIdView(Long id) {
		ProjectionList projections = Projections.projectionList()
		.add(Projections.alias(Projections.property("SC.tpSituacaoContratacao"),"tpSituacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.dtViagem"),"dtViagem"))
		.add(Projections.alias(Projections.property("SC.acao.id"),"idAcao"))
		.add(Projections.alias(Projections.property("SC.nrDddSolicitante"),"nrDddSolicitante"))
		.add(Projections.alias(Projections.property("SC.nrTelefoneSolicitante"),"nrTelefoneSolicitante"))
		.add(Projections.alias(Projections.property("pendencia.idPendencia"),"idPendencia"))
		.add(Projections.alias(Projections.property("SC.dtCriacao"),"dtCriacao"))
		.add(Projections.alias(Projections.property("SC.obMotivoReprovacao"),"obMotivoReprovacao"))
		.add(Projections.alias(Projections.property("SC.vlFreteMaximoAutorizado"),"vlFreteMaximoAutorizado"))
		.add(Projections.alias(Projections.property("SC.vlFreteNegociado"),"vlFreteNegociado"))
		.add(Projections.alias(Projections.property("SC.vlFreteSugerido"),"vlFreteSugerido"))
		.add(Projections.alias(Projections.property("SC.idSolicitacaoContratacao"),"idSolicitacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.tpSolicitacaoContratacao"),"tpSolicitacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.tpRotaSolicitacao"),"tpRotaSolicitacao"))
		.add(Projections.alias(Projections.property("SC.tpVinculoContratacao"),"tpVinculoContratacao"))
		.add(Projections.alias(Projections.property("SC.nrSolicitacaoContratacao"),"nrSolicitacaoContratacao"))
		.add(Projections.alias(Projections.property("SC.blIndicadorRastreamento"),"blIndicadorRastreamento"))
		.add(Projections.alias(Projections.property("SC.obObservacao"),"obObservacao"))
		.add(Projections.alias(Projections.property("SC.nrIdentificacaoMeioTransp"),"nrIdentificacaoMeioTransp_nrPlaca"))
		.add(Projections.alias(Projections.property("SC.nrIdentificacaoSemiReboque"),"nrIdentificacaoSemiReboque_nrPlaca"))
		.add(Projections.alias(Projections.property("SC.nrAnoFabricacaoMeioTransporteSemiReboque"),"nrAnoFabricacaoMeioTransporteSemiReboque"))
		.add(Projections.alias(Projections.property("SC.nrAnoFabricacaoMeioTransporte"),"nrAnoFabricacaoMeioTransporte"))
		.add(Projections.alias(Projections.property("SC.tpFluxoContratacao"),"tpFluxoContratacao"))
		.add(Projections.alias(Projections.property("SC.tpAbrangencia"),"tpAbrangencia"))
		.add(Projections.alias(Projections.property("SC.qtEixos"),"eixosTipoMeioTransporte_qtEixos"))
		.add(Projections.alias(Projections.property("SC.vlPostoPassagem"),"vlPostoPassagem"))
		.add(Projections.alias(Projections.property("TMT.tpMeioTransporte"),"tipoMeioTransporte_tpMeioTransporte"))
		.add(Projections.alias(Projections.property("TMT.idTipoMeioTransporte"),"tipoMeioTransporte_idTipoMeioTransporte"))
		.add(Projections.alias(Projections.property("TMT.dsTipoMeioTransporte"),"tipoMeioTransporte_dsTipoMeioTransporte"))
		.add(Projections.alias(Projections.property("F.sgFilial"),"filial_sgFilial"))
		.add(Projections.alias(Projections.property("F.idFilial"),"filial_idFilial"))
		.add(Projections.alias(Projections.property("P.nmFantasia"),"filial_pessoa_nmFantasia"))
		.add(Projections.alias(Projections.property("moedaPais.idMoedaPais"),"moedaPais_idMoedaPais"))
		.add(Projections.alias(Projections.property("US.nmUsuario"),"usuarioSolicitador_nmUsuario"))
		.add(Projections.alias(Projections.property("US.idUsuario"),"usuarioSolicitador_idUsuario"))
		.add(Projections.alias(Projections.property("rota.idRota"),"rota_idRota"))
		.add(Projections.alias(Projections.property("US.nrMatricula"),"usuarioSolicitador_nrMatricula"))
		.add(Projections.alias(Projections.property("rotaIdaVolta.idRotaIdaVolta"),"idRotaIdaVolta"));
								

		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idSolicitacaoContratacao",id);
		return (Map)createFindToPaginatedAndRowCount(criteria, Boolean.TRUE)
							.setProjection(projections)
							.setResultTransformer(AliasToNestedMapResultTransformer.getInstance())
							.getExecutableCriteria(getSession())
							.uniqueResult();
	}

	public SolicitacaoContratacao findSCbyId(Long idSolicitacaoContratacao) { 
		return (SolicitacaoContratacao)createDetachedCriteria()
			.setFetchMode("controleCargas", FetchMode.SELECT)
			.setFetchMode("filial", FetchMode.JOIN)
			.add(Restrictions.eq("idSolicitacaoContratacao", idSolicitacaoContratacao))
			.getExecutableCriteria(getSession())
			.uniqueResult();
	}

	public TabelaColetaEntrega findTabelaCEByIdSolicitacao(Long idSolicitacao) {
		return (TabelaColetaEntrega)DetachedCriteria.forClass(TabelaColetaEntrega.class,"TCE")
			.createAlias("solicitacaoContratacao", "SC")
			.add(Restrictions.eq("SC.idSolicitacaoContratacao",idSolicitacao))
			.getExecutableCriteria(getSession())
			.uniqueResult();
	}

	public List findParcelaTabelaCEByIdSolicitacao(Long idSolicitacao) {
		return DetachedCriteria.forClass(ParcelaTabelaCe.class,"PTCE")
		.createAlias("tabelaColetaEntrega","TCE")
		.createAlias("TCE.solicitacaoContratacao","SC")
		.add(Restrictions.eq("SC.idSolicitacaoContratacao",idSolicitacao))
		.getExecutableCriteria(getSession()).list();
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		DetachedCriteria dc = createFindToPaginatedAndRowCount(criteria)
						.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	} 

	private DetachedCriteria createFindToPaginatedAndRowCount(TypedFlatMap criteria) {
		return createFindToPaginatedAndRowCount(criteria,Boolean.FALSE);
	}

	private DetachedCriteria createFindToPaginatedAndRowCount(TypedFlatMap criteria,Boolean isFindById) {
		DetachedCriteria dc = DetachedCriteria.forClass(SolicitacaoContratacao.class,"SC")
		.createAlias("SC.tipoMeioTransporte", "TMT")
		.createAlias("SC.filial", "F")
		.createAlias("F.pessoa", "P")
		.createAlias("SC.moedaPais", "moedaPais")
		.createAlias("moedaPais.moeda", "moeda")
		.createAlias("SC.usuarioSolicitador", "US")
		.createAlias("SC.rota", "rota", JoinFragment.LEFT_OUTER_JOIN);

		if (isFindById.equals(Boolean.TRUE)) {
			dc.setFetchMode("pendencia",FetchMode.JOIN)
			.add(Restrictions.eq("SC.idSolicitacaoContratacao",criteria.getLong("idSolicitacaoContratacao")));
			return dc;
		}

        if (criteria.getLong("filial.idFilial") != null) {
			dc.add(Restrictions.eq("F.idFilial",criteria.getLong("filial.idFilial")));
        }

        if (!StringUtils.isBlank(criteria.getString("tpSolicitacaoContratacao"))) {
			dc.add(Restrictions.eq("SC.tpSolicitacaoContratacao",criteria.getString("tpSolicitacaoContratacao")));
        }

        if (!StringUtils.isBlank(criteria.getString("tpVinculoContratacao"))) {
			dc.add(Restrictions.eq("SC.tpVinculoContratacao",criteria.getString("tpVinculoContratacao")));
        }

        if (criteria.getLong("nrSolicitacaoContratacao") != null) {
			dc.add(Restrictions.eq("SC.nrSolicitacaoContratacao",criteria.getLong("nrSolicitacaoContratacao")));
        }

        if (!StringUtils.isBlank(criteria.getString("tipoMeioTransporte.tpMeioTransporte"))) {
			dc.add(Restrictions.eq("TMT.tpMeioTransporte",criteria.getString("tipoMeioTransporte.tpMeioTransporte")));
        }

        if (criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte") != null) {
			dc.add(Restrictions.eq("TMT.idTipoMeioTransporte",criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte")));
        }

		Boolean blIndicadorRastreamento = criteria.getBoolean("blIndicadorRastreamento");
        if (blIndicadorRastreamento != null) {
			dc.add(Restrictions.eq("SC.blIndicadorRastreamento", blIndicadorRastreamento));
        }

        if (criteria.getYearMonthDay("dtInicioContratacao") != null) {
			dc.add(Restrictions.ge("SC.dtCriacao",criteria.getYearMonthDay("dtInicioContratacao")));
        }

        if (criteria.getYearMonthDay("dtFimContratacao") != null) {
			dc.add(Restrictions.le("SC.dtCriacao",criteria.getYearMonthDay("dtFimContratacao")));
        }

        if (!StringUtils.isBlank(criteria.getString("tpSituacaoContratacao"))) {
			dc.add(Restrictions.eq("SC.tpSituacaoContratacao",criteria.getString("tpSituacaoContratacao")));
        }

        if (!StringUtils.isBlank(criteria.getString("nrIdentificacaoSemiReboque.nrPlaca"))) {
			dc.add(Restrictions.ilike("SC.nrIdentificacaoSemiReboque",criteria.getString("nrIdentificacaoSemiReboque.nrPlaca"),MatchMode.START));
        }

        if (!StringUtils.isBlank(criteria.getString("nrIdentificacaoMeioTransp.nrPlaca"))) {
			dc.add(Restrictions.ilike("SC.nrIdentificacaoMeioTransp",criteria.getString("nrIdentificacaoMeioTransp.nrPlaca"),MatchMode.START));
        }
		
		if (StringUtils.isNotBlank(criteria.getString("tpAbrangencia"))) {
			dc.add(Restrictions.eq("SC.tpAbrangencia", criteria.getString("tpAbrangencia")));
		}

		if (criteria.getLong("usuario.idUsuario") != null) {
			dc.add(Restrictions.eq("US.idUsuario",criteria.getLong("usuario.idUsuario")));
		}
		
		if (StringUtils.isNotBlank(criteria.getString("tpModal"))) {
			dc.add(Restrictions.eq("SC.tpModal", criteria.getString("tpModal")));
		}
		
		String blQuebraMeioTransporte = criteria.getString("blQuebraMeioTransporte");
		if (StringUtils.isNotBlank(blQuebraMeioTransporte)) {
			if ("S".equals(blQuebraMeioTransporte)) {
				dc.add(Restrictions.eq("SC.blQuebraMeioTransporte", Boolean.TRUE));
			} else if ("N".equals(blQuebraMeioTransporte)){
				dc.add(Restrictions.eq("SC.blQuebraMeioTransporte", Boolean.FALSE));
			}
		}

		return dc;
	}

	public MeioTransporte validateMeioTranpIsToAgFilial(Long idMeioTransporte, Long idFilial) {
		return (MeioTransporte)DetachedCriteria.forClass(MeioTransporte.class)
		.createAlias("filialAgregadoCe","FA")
		.add(Restrictions.eq("idMeioTransporte",idMeioTransporte))
		.add(Restrictions.eq("FA.idFilial",idFilial))
		.getExecutableCriteria(getSession()).uniqueResult();
					
	}

	public Integer getRowCountTabelaColetaEntregaByFilialMeioTransporte(TabelaColetaEntrega bean) {
		DetachedCriteria dc = DetachedCriteria.forClass(TabelaColetaEntrega.class,"TCE")
		.setProjection(Projections.rowCount())
		.createAlias("TCE.filial","F")
		.createAlias("TCE.solicitacaoContratacao","SC")
		.add(Restrictions.eq("F.idFilial",bean.getFilial().getIdFilial()))
		.add(Restrictions.ne("TCE.tpSituacaoAprovacao","R"));

		JTVigenciaUtils.getDetachedVigencia(dc,bean.getDtVigenciaInicial(),bean.getDtVigenciaFinal());

        if (bean.getIdTabelaColetaEntrega() != null) {
			dc.add(Restrictions.ne("TCE.idTabelaColetaEntrega",bean.getIdTabelaColetaEntrega()));
        }

		dc.add(Restrictions.ilike("SC.nrIdentificacaoMeioTransp",bean.getSolicitacaoContratacao().getNrIdentificacaoMeioTransp()));

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
	 * Retorna as solicitações de contratação aprovadas para o veículo informado
     *
	 * @param nrMeioTransporte
	 * @param idTipoMeioTransporte
	 * @param tpVinculo
	 * @param idFilialAgregado
	 * @return
	 */
	public List<SolicitacaoContratacao> validadeMeioTransporteContratacaoAprovada(String nrMeioTransporte, Long idTipoMeioTransporte, String tpVinculo, Long idFilialAgregado) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("SC");
		hql.addFrom(getPersistentClass().getName() + " SC" +
				" left join SC.tipoMeioTransporte as TMT" + 
				" left join fetch SC.rota as rota");

		hql.addCustomCriteria("((SC.nrIdentificacaoMeioTransp = ? and SC.tipoMeioTransporte.id = ?)" +
				" or (SC.nrIdentificacaoSemiReboque = ? and TMT.tipoMeioTransporte.id = ?))");
		hql.addCriteriaValue(nrMeioTransporte);
		hql.addCriteriaValue(idTipoMeioTransporte);
		hql.addCriteriaValue(nrMeioTransporte);
		hql.addCriteriaValue(idTipoMeioTransporte);

		hql.addCustomCriteria("SC.tpSituacaoContratacao = ?","AP");
        if (tpVinculo != null) {
		hql.addCustomCriteria("SC.tpVinculoContratacao = ?", tpVinculo);
        }
		if (idFilialAgregado != null && ! Long.valueOf(0).equals(idFilialAgregado)) {
			hql.addCustomCriteria("SC.filial.idFilial = ?", idFilialAgregado);
		}
		
		hql.addOrderBy("SC.dtCriacao", "desc");

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	/**
	 * Atualiza a Tabela de Coleta/entrega de acordo com pendências em soliciitação de contratação.
	 * @param idMeioTransporte
     * @param ids
	 */
	public void updateMeioTransporteContratacaoAprovada(final Long idMeioTransporte, final List<SolicitacaoContratacao> ids) {

        int maxSize = MAX_UPDATE_SIZE;

        for (int posSolicitacao = 0; posSolicitacao <= ids.size(); posSolicitacao += maxSize) {

            int valorOffset = posSolicitacao + maxSize;
            final List<SolicitacaoContratacao> subListUpdateIds = ids.subList(posSolicitacao, valorOffset > ids.size() ? ids.size() : valorOffset);

		HibernateCallback updateSituacao = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String update = "update " + TabelaColetaEntrega.class.getName() + 
						" set id_meio_transporte = ? " +
						" where id_solicitacao_contratacao in (";

				String interrogacoes = "";
                    for (int i = 0; i < subListUpdateIds.size(); i++) {
                        if (!"".equals(interrogacoes)) {
						interrogacoes = interrogacoes.concat(",");
                        }
					interrogacoes = interrogacoes.concat("?");
				}
				update = update.concat(interrogacoes).concat(")");

				Query q = session.createQuery(update);
                    q.setLong(0, idMeioTransporte);
                    for (int i = 0; i < subListUpdateIds.size(); i++) {
                        q.setLong(i + 1, subListUpdateIds.get(i).getIdSolicitacaoContratacao());
				}
				q.executeUpdate();
				
				return null;
			}
		};
		getAdsmHibernateTemplate().execute(updateSituacao);
	}

    }

	public String findRotaByIdSolicitacaoContratacao(Long idSolicitacaoContratacao){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("nvl2(rotaIdaRota.dsRota,rotaIda.nrRota||'-'||rotaIdaRota.dsRota,rotaIda.nrRota)","dsRota");

		hql.addFrom(SolicitacaoContratacao.class.getName()+" sc " +
				"left outer join sc.rotaIdaVolta rotaIda " +
				"left outer join rotaIda.rota rotaIdaRota ");

		hql.addCriteria("sc.idSolicitacaoContratacao","=",idSolicitacaoContratacao);

		return(String)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),new Object[]{idSolicitacaoContratacao});
	}

	public Map findRotasByIdSolicitacaoContratacao(Long idSolicitacaoContratacao){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(rotaIda.nrRota as nrRota, rotaIdaRota.dsRota as dsRota)");

		hql.addFrom(SolicitacaoContratacao.class.getName()+" sc " +
				"left outer join sc.rotaIdaVolta rotaIda " +
				"left outer join rotaIda.rota rotaIdaRota ");

		hql.addCriteria("sc.idSolicitacaoContratacao","=",idSolicitacaoContratacao);

		return(Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),new Object[]{idSolicitacaoContratacao});
	}

	public List findPendenciaBySolicitacao(Long idSolicitacao) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(Pendencia.class.getName());
		hql.addCriteria("idProcesso","=",idSolicitacao);
		hql.addCriteria("tpSituacaoPendencia","=","E");

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public SolicitacaoContratacao findByNumeroSolicitacaoContratacao(Long nrSolicitacaoContratacao) {
		return (SolicitacaoContratacao)DetachedCriteria.forClass(SolicitacaoContratacao.class)
		.add(Restrictions.eq("nrSolicitacaoContratacao",nrSolicitacaoContratacao))
		.getExecutableCriteria(getSession()).uniqueResult();

		
	}

	public List<SolicitacaoContratacao> findSolicitacaoContratacao(String nrMeioTransporte, String tpSolicitacaoContratacao, DateTime dhInicioContratacao, Long idFilial) { 

		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("SC");
		hql.addFrom(getPersistentClass().getName() + " SC" +
				" left join SC.tipoMeioTransporte as TMT");

		hql.addCustomCriteria("SC.nrIdentificacaoMeioTransp = ? " +
				" and SC.tpSolicitacaoContratacao = ?" +
				" and SC.filial.idFilial = ?");
		
		if (dhInicioContratacao != null){
			hql.addCustomCriteria(" and SC.dtInicioContratacao = ?");			
		}
				
		hql.addCriteriaValue(nrMeioTransporte);
		hql.addCriteriaValue(tpSolicitacaoContratacao);

		if (dhInicioContratacao != null){
			hql.addCriteriaValue(dhInicioContratacao);
		}
		
		hql.addCriteriaValue(idFilial);
		
		hql.addOrderBy("SC.dtInicioContratacao","desc");
		hql.addOrderBy("SC.dtCriacao","desc");
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	
	}

	public List<SolicitacaoContratacao> findSolicitacoesByMeioTransporte( String nrIdentificador, Long idTipoMeioTransporte) {

		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("SC");
		hql.addFrom(getPersistentClass().getName() + " SC");
		
		hql.addCriteria("SC.nrIdentificacaoMeioTransp", "=", nrIdentificador);
		hql.addCriteria("SC.tipoMeioTransporte.id", "=", idTipoMeioTransporte);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public ResultSetPage findSolicitacoesContratacaoControleCarga(Map filtros, FindDefinition definition) {
		
		Long idControleCarga = (Long)filtros.get("idControleCarga");
		
		StringBuilder sql = new StringBuilder()
		
		.append("select * from (select f.sg_filial || '-' || sc.nr_solicitacao_contratacao solcitacao ")
		.append(", VI18N(vd.ds_valor_dominio_i,'")
		.append(SessionUtils.getUsuarioLogado().getLocale())
		.append("') status ")
		.append(", sc.nr_identificacao_meio_transp veiculo ")
		.append(", to_char(ep.dh_evento,'dd/mm/yyyy hh24:mi') data_hora ")
		.append(", u.nm_usuario usuario ")
		.append(", vd.vl_valor_dominio tipo_status ")
		.append(", sc.id_solicitacao_contratacao id_solicitacao_contratacao ")
		
		.append(" ,case ") 
		.append(" 	when vd.vl_valor_dominio = 'GE' then 0 ")
    	.append(" 	when vd.vl_valor_dominio = 'EP' then 1 ")
    	.append(" 	when vd.vl_valor_dominio = 'PF' then 2 ") 
    	.append(" 	else 3 ")
    	.append(" end ordenacao  ") 
		
		.append("from solicitacao_contratacao sc ")
		.append(", filial f ")
		.append(", controle_carga cc ")
		.append(", evento_puxada ep ")
		.append(", dominio d ")
		.append(", valor_dominio vd ")
		.append(", Usuario_adsm u ")
		.append("where sc.id_controle_carga = cc.id_controle_carga ")
		.append("and sc.id_controle_carga = ? ")
		.append("and sc.id_filial = f.id_filial ")
		.append("and sc.id_solicitacao_contratacao = ep.id_solicitacao_contratacao ")
		.append("and ep.id_usuario = u.id_usuario ")
		.append("and d.id_dominio = vd.id_dominio ")
		.append("and d.nm_dominio = 'DM_STATUS_CONTROLE_PUXADA' ")
		.append("and vd.vl_valor_dominio = ep.tp_status_evento ")
		
		.append("and ep.tp_status_evento = 'GE' ")
		.append("and sc.id_solicitacao_contratacao in ( ")
		.append(" select e.id_solicitacao_contratacao ")
		.append("from evento_puxada e ")
		.append("where e.id_solicitacao_contratacao = sc.id_solicitacao_contratacao ")
		.append("group by e.id_solicitacao_contratacao ")
		.append(" having count(*) = 1) ")
		.append(" order by solcitacao,ordenacao) ")
		
		.append("UNION ALL ")
		.append("select * from (select f.sg_filial || '-' || sc.nr_solicitacao_contratacao solcitacao ")
		.append(", VI18N(vd.ds_valor_dominio_i,'")
		.append(SessionUtils.getUsuarioLogado().getLocale())
		.append("') status ")
		.append(", sc.nr_identificacao_meio_transp veiculo ")
		.append(", to_char(ep.dh_evento,'dd/mm/yyyy hh24:mi') data_hora ")
		.append(", u.nm_usuario usuario ")
		.append(", vd.vl_valor_dominio tipo_status ")
		.append(", sc.id_solicitacao_contratacao id_solicitacao_contratacao ")
		.append(" ,case ") 
		.append(" 	when vd.vl_valor_dominio = 'GE' then 0 ")
    	.append(" 	when vd.vl_valor_dominio = 'EP' then 1 ")
    	.append(" 	when vd.vl_valor_dominio = 'PF' then 2 ") 
    	.append(" 	else 3 ")
    	.append(" end ordenacao  ") 
		.append("from solicitacao_contratacao sc ")
		.append(", filial f ")
		.append(", controle_carga cc ")
		.append(", evento_puxada ep ")
		.append(", dominio d ")
		.append(", valor_dominio vd ")
		.append(", Usuario_adsm u ")
		.append("where sc.id_controle_carga = cc.id_controle_carga ")
		.append("and sc.id_controle_carga = ? ")
		.append("and sc.id_filial = f.id_filial ")
		.append("and sc.id_solicitacao_contratacao = ep.id_solicitacao_contratacao ")
		.append("and ep.id_usuario = u.id_usuario ")
		.append("and d.id_dominio = vd.id_dominio ")
		.append("and d.nm_dominio = 'DM_STATUS_CONTROLE_PUXADA' ")
		.append("and vd.vl_valor_dominio = ep.tp_status_evento ")
		
		
		.append("and ep.tp_status_evento in ( 'GE' , 'EP') ")
		.append("and sc.id_solicitacao_contratacao in ( ")
		.append("select e.id_solicitacao_contratacao ")
		.append("from evento_puxada e ")
		.append("where e.id_solicitacao_contratacao = sc.id_solicitacao_contratacao ")
		.append("group by e.id_solicitacao_contratacao ")
		.append("having count(*) = 2) ")
		.append(" order by solcitacao,ordenacao) ")
		
		.append("UNION ALL ")
		.append("select * from (select f.sg_filial || '-' || sc.nr_solicitacao_contratacao solcitacao ")
		.append(", VI18N(vd.ds_valor_dominio_i,'")
		.append(SessionUtils.getUsuarioLogado().getLocale())
		.append("') status ")
		.append(", sc.nr_identificacao_meio_transp veiculo ")
		.append(", to_char(ep.dh_evento,'dd/mm/yyyy hh24:mi') data_hora ")
		.append(", u.nm_usuario usuario ")
		.append(", vd.vl_valor_dominio tipo_status ")
		.append(", sc.id_solicitacao_contratacao id_solicitacao_contratacao ")
		.append(" ,case ") 
		.append(" 	when vd.vl_valor_dominio = 'GE' then 0 ")
    	.append(" 	when vd.vl_valor_dominio = 'EP' then 1 ")
    	.append(" 	when vd.vl_valor_dominio = 'PF' then 2 ") 
    	.append(" else 3  ")
    	.append(" end ordenacao ") 
		.append("from solicitacao_contratacao sc ")
		.append(", filial f ")
		.append(", controle_carga cc ")
		.append(", evento_puxada ep ")
		.append(", dominio d ")
		.append(", valor_dominio vd ")
		.append(", Usuario_adsm u ")
		.append("where sc.id_controle_carga = cc.id_controle_carga ")
		.append("and sc.id_controle_carga = ? ")
		.append("and sc.id_filial = f.id_filial ")
		.append("and sc.id_solicitacao_contratacao = ep.id_solicitacao_contratacao ")
		.append("and ep.id_usuario = u.id_usuario ")
		.append("and d.id_dominio = vd.id_dominio ")
		.append("and d.nm_dominio = 'DM_STATUS_CONTROLE_PUXADA' ")
		.append("and vd.vl_valor_dominio = ep.tp_status_evento ")
		
		.append("and ep.tp_status_evento in ( 'GE' , 'EP', 'PF') ")
		.append("and sc.id_solicitacao_contratacao in ( ")
		.append("select e.id_solicitacao_contratacao ")
		.append("from evento_puxada e ")
	    .append("where e.id_solicitacao_contratacao = sc.id_solicitacao_contratacao ")
	    .append("group by e.id_solicitacao_contratacao ")
	    .append("having count(*) = 3) ")
	    .append(" order by solcitacao,ordenacao) ");
		
		
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {	
				
				sqlQuery.addScalar("solcitacao");
				sqlQuery.addScalar("status");
				sqlQuery.addScalar("veiculo");
				sqlQuery.addScalar("data_hora");
				sqlQuery.addScalar("usuario");
				sqlQuery.addScalar("tipo_status");
				sqlQuery.addScalar("id_solicitacao_contratacao",Hibernate.LONG);
				
			}
		};

		
		List param = new ArrayList();
		param.add(idControleCarga);
		param.add(idControleCarga);
		param.add(idControleCarga);
		 
		
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), definition.getCurrentPage(), definition.getPageSize(), param.toArray(), configureSqlQuery);	
	}
	
	
	public Integer getRowCount(Long idControleCarga) {
		StringBuilder sql = new StringBuilder()
		
		.append("select count(*) as rowCount from ( ")
		.append("select f.sg_filial || '-' || sc.nr_solicitacao_contratacao solcitacao ")
		.append(", VI18N(vd.ds_valor_dominio_i,'")
		.append(SessionUtils.getUsuarioLogado().getLocale())
		.append("') status ")
		.append(", sc.nr_identificacao_meio_transp veiculo ")
		.append(", to_char(ep.dh_evento,'dd/mm/yyyy hh24:mi') data_hora ")
		.append(", u.nm_usuario usuario ")
		.append(", vd.vl_valor_dominio tipo_status ")
		.append(", sc.id_solicitacao_contratacao id_solicitacao_contratacao ")
		.append("from solicitacao_contratacao sc ")
		.append(", filial f ")
		.append(", controle_carga cc ")
		.append(", evento_puxada ep ")
		.append(", dominio d ")
		.append(", valor_dominio vd ")
		.append(", Usuario_adsm u ")
		.append("where sc.id_controle_carga = cc.id_controle_carga ")
		.append("and sc.id_controle_carga = ? ")
		.append("and sc.id_filial = f.id_filial ")
		.append("and sc.id_solicitacao_contratacao = ep.id_solicitacao_contratacao ")
		.append("and ep.id_usuario = u.id_usuario ")
		.append("and d.id_dominio = vd.id_dominio ")
		.append("and d.nm_dominio = 'DM_STATUS_CONTROLE_PUXADA' ")
		.append("and vd.vl_valor_dominio = ep.tp_status_evento ")
		
		.append("and ep.tp_status_evento = 'GE' ")
		.append("and sc.id_solicitacao_contratacao in ( ")
		.append(" select e.id_solicitacao_contratacao ")
		.append("from evento_puxada e ")
		.append("where e.id_solicitacao_contratacao = sc.id_solicitacao_contratacao ")
		.append("group by e.id_solicitacao_contratacao ")
		.append(" having count(*) = 1) ")
		
		
		.append("UNION ALL ")
		.append("select f.sg_filial || '-' || sc.nr_solicitacao_contratacao solcitacao ")
		.append(", VI18N(vd.ds_valor_dominio_i,'")
		.append(SessionUtils.getUsuarioLogado().getLocale())
		.append("') status ")
		.append(", sc.nr_identificacao_meio_transp veiculo ")
		.append(", to_char(ep.dh_evento,'dd/mm/yyyy hh24:mi') data_hora ")
		.append(", u.nm_usuario usuario ")
		.append(", vd.vl_valor_dominio tipo_status ")
		.append(", sc.id_solicitacao_contratacao id_solicitacao_contratacao ")
		.append("from solicitacao_contratacao sc ")
		.append(", filial f ")
		.append(", controle_carga cc ")
		.append(", evento_puxada ep ")
		.append(", dominio d ")
		.append(", valor_dominio vd ")
		.append(", Usuario_adsm u ")
		.append("where sc.id_controle_carga = cc.id_controle_carga ")
		.append("and sc.id_controle_carga = ? ")
		.append("and sc.id_filial = f.id_filial ")
		.append("and sc.id_solicitacao_contratacao = ep.id_solicitacao_contratacao ")
		.append("and ep.id_usuario = u.id_usuario ")
		.append("and d.id_dominio = vd.id_dominio ")
		.append("and d.nm_dominio = 'DM_STATUS_CONTROLE_PUXADA' ")
		.append("and vd.vl_valor_dominio = ep.tp_status_evento ")
		
		
		.append("and ep.tp_status_evento in ( 'GE' , 'EP') ")
		.append("and sc.id_solicitacao_contratacao in ( ")
		.append("select e.id_solicitacao_contratacao ")
		.append("from evento_puxada e ")
		.append("where e.id_solicitacao_contratacao = sc.id_solicitacao_contratacao ")
		.append("group by e.id_solicitacao_contratacao ")
		.append("having count(*) = 2) ")
		
		.append("UNION ALL ")
		.append("select f.sg_filial || '-' || sc.nr_solicitacao_contratacao solcitacao ")
		.append(", VI18N(vd.ds_valor_dominio_i,'")
		.append(SessionUtils.getUsuarioLogado().getLocale())
		.append("') status ")
		.append(", sc.nr_identificacao_meio_transp veiculo ")
		.append(", to_char(ep.dh_evento,'dd/mm/yyyy hh24:mi') data_hora ")
		.append(", u.nm_usuario usuario ")
		.append(", vd.vl_valor_dominio tipo_status ")
		.append(", sc.id_solicitacao_contratacao id_solicitacao_contratacao ")
		.append("from solicitacao_contratacao sc ")
		.append(", filial f ")
		.append(", controle_carga cc ")
		.append(", evento_puxada ep ")
		.append(", dominio d ")
		.append(", valor_dominio vd ")
		.append(", Usuario_adsm u ")
		.append("where sc.id_controle_carga = cc.id_controle_carga ")
		.append("and sc.id_controle_carga = ? ")
		.append("and sc.id_filial = f.id_filial ")
		.append("and sc.id_solicitacao_contratacao = ep.id_solicitacao_contratacao ")
		.append("and ep.id_usuario = u.id_usuario ")
		.append("and d.id_dominio = vd.id_dominio ")
		.append("and d.nm_dominio = 'DM_STATUS_CONTROLE_PUXADA' ")
		.append("and vd.vl_valor_dominio = ep.tp_status_evento ")
		
		.append("and ep.tp_status_evento in ( 'GE' , 'EP', 'PF') ")
		.append("and sc.id_solicitacao_contratacao in ( ")
		.append("select e.id_solicitacao_contratacao ")
		.append("from evento_puxada e ")
	    .append("where e.id_solicitacao_contratacao = sc.id_solicitacao_contratacao ")
	    .append("group by e.id_solicitacao_contratacao ")
	    .append("having count(*) = 3) ")
		.append(")");
		

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {	
				
				
				sqlQuery.addScalar("rowCount",Hibernate.INTEGER);
			}
		};

		
		List param = new ArrayList();
		param.add(idControleCarga);
		param.add(idControleCarga);
		param.add(idControleCarga);
		
		
        return (Integer) getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), 1, 12, param.toArray(), configureSqlQuery).getList().get(0);

		
	}
	
	public void removeByIdsAnexoSolicContratacao(List ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + AnexoSolicContratacao.class.getName() + " WHERE idAnexoSolicContratacao IN (:id)", ids);
	}

	public AnexoSolicContratacao findAnexoSolicContratacaoById(Long idAnexoSolicContratacao) {
		return (AnexoSolicContratacao) getAdsmHibernateTemplate().load(AnexoSolicContratacao.class, idAnexoSolicContratacao);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<AnexoSolicContratacao> findPaginatedAnexoSolicContratacao(PaginatedQuery paginatedQuery) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" anexo.idAnexoSolicContratacao AS idAnexoSolicContratacao,");
		hql.append(" anexo.nmArquivo AS nmArquivo,");
		hql.append(" anexo.dsAnexo AS dsAnexo,");
		hql.append(" anexo.dhCriacao AS dhInclusao,");
		hql.append(" usuario.usuarioADSM.nmUsuario as nmUsuario)");
		hql.append(" FROM AnexoSolicContratacao AS anexo");
		hql.append("  INNER JOIN anexo.solicitacaoContratacao solicitacaoContratacao");
		hql.append("  INNER JOIN anexo.usuario usuario");
		hql.append(" WHERE solicitacaoContratacao.idSolicitacaoContratacao = :idSolicitacaoContratacao");
		hql.append(" ORDER BY anexo.dhCriacao.value DESC ");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, hql.toString());
	}
	
	public Integer getRowCountAnexoSolicContratacao(TypedFlatMap criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM anexo_solic_contratacao WHERE id_solicitacao_contratacao = ?");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{criteria.get("idSolicitacaoContratacao")});
	}

	public Integer findSolicitacaoValida(Long idFilial,	String nrIdentificador) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT 1 ");
		sql.append(" FROM SOLICITACAO_CONTRATACAO SO, ");
		sql.append("   TABELA_COLETA_ENTREGA CE ");
		sql.append(" WHERE SO.ID_SOLICITACAO_CONTRATACAO = CE.ID_SOLICITACAO_CONTRATACAO(+) ");
		sql.append(" AND SO.NR_IDENTIFICACAO_MEIO_TRANSP = ? ");
		sql.append(" AND SO.TP_SITUACAO_CONTRATACAO      = 'AP' ");
		sql.append(" AND SO.TP_SOLICITACAO_CONTRATACAO   = 'C' ");
		sql.append(" AND SO.ID_FILIAL                    = ? ");
		sql.append(" AND (SO.TP_VINCULO_CONTRATACAO      = 'A' ");
		sql.append(" OR ( SO.TP_VINCULO_CONTRATACAO      = 'E' ");
		sql.append(" AND ((SO.DT_INICIO_CONTRATACAO     <= SYSDATE ");
		sql.append(" AND SO.DT_FIM_CONTRATACAO          >= SYSDATE) ");
		sql.append(" OR (CE.DT_VIGENCIA_INICIAL         <= SYSDATE ");
		sql.append(" AND CE.DT_VIGENCIA_FINAL           >= SYSDATE))))");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{nrIdentificador,idFilial});
	}	
	
	public List<Object[]> findUltimaSolicitacaoValida(Long idFilial,	String nrIdentificador, DateTime dateTime) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TP_VINCULO_CONTRATACAO ");
		sql.append(" FROM ");
		sql.append("   (SELECT ");
		sql.append("		SO.TP_VINCULO_CONTRATACAO, ");
		sql.append("     	CASE TP_VINCULO_CONTRATACAO ");
		sql.append("       		WHEN 'E' ");
		sql.append("     		THEN 1 ");
		sql.append("       		ELSE 2 ");
		sql.append("     	END AS ordem ");
		sql.append("   	FROM SOLICITACAO_CONTRATACAO SO, ");
		sql.append("   		TABELA_COLETA_ENTREGA CE ");
		sql.append("   	WHERE SO.ID_SOLICITACAO_CONTRATACAO = CE.ID_SOLICITACAO_CONTRATACAO(+) ");
		sql.append("   		AND SO.NR_IDENTIFICACAO_MEIO_TRANSP = :nrIdentificador ");
		sql.append("   		AND SO.TP_SITUACAO_CONTRATACAO      = 'AP' ");
		sql.append("   		AND SO.TP_SOLICITACAO_CONTRATACAO   = 'C' ");
		sql.append("   		AND SO.ID_FILIAL                    = :idFilial ");
		sql.append("   		AND (SO.TP_VINCULO_CONTRATACAO      = 'A' ");
		sql.append("   		OR ( SO.TP_VINCULO_CONTRATACAO      = 'E' ");
		String data = JTDateTimeUtils.formatDateTimeToString(dateTime, JTDateTimeUtils.DATETIME_WITH_WITHOUT_TIME_PATTERN);
		sql.append("        AND ((SO.DT_INICIO_CONTRATACAO <= TO_DATE(:dataInicioContratacao, 'DD/MM/YYYY') ");
		sql.append("        AND SO.DT_FIM_CONTRATACAO >= TO_DATE(:dataFimContratacao, 'DD/MM/YYYY')) ");
		sql.append("        OR (CE.DT_VIGENCIA_INICIAL <= TO_DATE(:dataVigenciaInicial, 'DD/MM/YYYY') ");
		sql.append("        AND CE.DT_VIGENCIA_FINAL >= TO_DATE(:dataVigenciaFinal, 'DD/MM/YYYY'))))) ");
		sql.append("   		ORDER BY ordem ");
		sql.append(" ) ");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("TP_VINCULO_CONTRATACAO", Hibernate.STRING);      			
      		}
    	};
		
    	Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dataInicioContratacao", data);
		parameters.put("dataFimContratacao", data);
		parameters.put("dataVigenciaInicial", data);
		parameters.put("dataVigenciaFinal", data);
    	parameters.put("idFilial", idFilial);
    	parameters.put("nrIdentificador", nrIdentificador);
		
		return  getAdsmHibernateTemplate().findBySql(sql.toString(), parameters,csq);
	}

    public List<SolicitacaoContratacao> findSolicitacoesAprovadasControleCarga(String nrIdentificacaoMeioTransp, Long idFilial) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("sc");
        hql.addFrom(getPersistentClass().getName() + " sc" );
        
        hql.addCustomCriteria("((sc.tpVinculoContratacao = 'E' and sc.dtInicioContratacao <= :dtVigencia and sc.dtFimContratacao >= :dtVigencia) or sc.tpVinculoContratacao ='A')");
        hql.addCustomCriteria("(sc.filial.sgFilial = 'MTZ' or sc.filial.id = :idFilial)");
        hql.addCustomCriteria("UPPER(sc.nrIdentificacaoMeioTransp) = UPPER(:nrIdentificacaoMeioTransp)");
        hql.addCustomCriteria("sc.tpSituacaoContratacao = 'AP'");
        hql.addCustomCriteria("sc.tpSolicitacaoContratacao = 'C'");
        
        Map<String,Object> criteria = new HashMap<String, Object>();
        criteria.put("dtVigencia", JTDateTimeUtils.getDataAtual());
        criteria.put("idFilial", idFilial);
        criteria.put("nrIdentificacaoMeioTransp", nrIdentificacaoMeioTransp);
        
        return getAdsmHibernateTemplate().findByNamedParam(hql.getSql(),criteria);
    
    }	
	
	
}