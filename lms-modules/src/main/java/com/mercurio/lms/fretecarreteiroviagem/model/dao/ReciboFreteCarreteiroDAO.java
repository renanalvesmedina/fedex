package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.entrega.model.Veiculos;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReciboFreteCarreteiroDAO extends BaseCrudDao<ReciboFreteCarreteiro, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ReciboFreteCarreteiro.class;
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("proprietario",FetchMode.JOIN);
		lazyFindLookup.put("proprietario.pessoa",FetchMode.JOIN);
		lazyFindLookup.put("moedaPais",FetchMode.JOIN);
		lazyFindLookup.put("moedaPais.moeda",FetchMode.JOIN);
		lazyFindLookup.put("controleCarga",FetchMode.JOIN);
		lazyFindLookup.put("controleCarga.filialByIdFilialOrigem",FetchMode.JOIN);
		lazyFindLookup.put("meioTransporteRodoviario",FetchMode.JOIN);
		lazyFindLookup.put("meioTransporteRodoviario.meioTransporte",FetchMode.JOIN);
		lazyFindLookup.put("meioTransporteRodoviario.meioTransporte.modeloMeioTransporte",FetchMode.JOIN);
		lazyFindLookup.put("meioTransporteRodoviario.meioTransporte.modeloMeioTransporte.marcaMeioTransporte",FetchMode.JOIN);
		lazyFindLookup.put("motorista",FetchMode.JOIN);
		lazyFindLookup.put("motorista.pessoa",FetchMode.JOIN);
		lazyFindLookup.put("filial",FetchMode.JOIN);
		lazyFindLookup.put("filial.pessoa",FetchMode.JOIN);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idFilial
	 * @param nrReciboFreteCarreteiro
	 * @return <b>ReciboFreteCarreteiro</b>
	 */
	public ReciboFreteCarreteiro findReciboFreteCarreteiro(Long idFilial, Long nrReciboFreteCarreteiro) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("filial.id", idFilial));
		dc.add(Restrictions.eq("nrReciboFreteCarreteiro", nrReciboFreteCarreteiro));
		return (ReciboFreteCarreteiro)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	* Encontra recibos com mesmo Controle de Carga e Proprietário do recibo recebido por parâmetro.<br>
	* Considera apenas registros com blAdiantamento recebido, e que não estejam cancelados.<br>
	* Se receber idReciboFreteCarreteiro, este será ignorado na consulta.<br>
	* 
	* @author Felipe Ferreita
	* @since 12-01-2006
	* @param idControleCarga
	* @param idProprietario
	* @param idReciboFreteCarreteiro
	* @param blAdiantamento
	* @return
	*/
	public List<ReciboFreteCarreteiro> findRecibosNaoCancelados(
		Long idControleCarga,
		Long idProprietario,
		Long idReciboFreteCarreteiro,
		Long idManifestoViagemNacional,
		Boolean blAdiantamento
	) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "rfc");
		dc.add(Restrictions.eq("rfc.controleCarga.id", idControleCarga));
		dc.add(Restrictions.eq("rfc.proprietario.id", idProprietario));
		dc.add(Restrictions.eq("rfc.blAdiantamento", blAdiantamento));
		dc.add(Restrictions.eq("rfc.manifestoViagemNacional.id", idManifestoViagemNacional));
		dc.add(Restrictions.ne("rfc.tpSituacaoRecibo", "CA"));
		if (idReciboFreteCarreteiro != null)
			dc.add(Restrictions.ne("rfc.id", idReciboFreteCarreteiro));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Find da grid de Atendimentos da tela 'Manter Recibos'
	 * @param criteria
	 * @return
	 */
	public List findGridAdiantamentos(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("RFC");

		hql.addFrom(getPersistentClass().getName() + " RFC inner join fetch RFC.filial");

		hql.addCriteria("RFC.id","!=",criteria.getLong("idReciboFreteCarreteiro"));
		hql.addCriteria("RFC.blAdiantamento","=",Boolean.TRUE);
		hql.addCriteria("RFC.proprietario.id","=",criteria.getLong("proprietario.idProprietario"));
		hql.addCriteria("RFC.controleCarga.id","=",criteria.getLong("controleCarga.idControleCarga"));
		hql.addCriteria("RFC.tpSituacaoRecibo","!=","CA");

		hql.addOrderBy("RFC.nrReciboFreteCarreteiro");
		hql.addOrderBy("RFC.dhEmissao.value");
		hql.addOrderBy("RFC.tpSituacaoRecibo");
		hql.addOrderBy("RFC.vlBruto");
		hql.addOrderBy("RFC.dtSugeridaPagto");
		hql.addOrderBy("RFC.dtPagtoReal");

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public ResultSetPage findPaginatedComplementar(TypedFlatMap criteria,FindDefinition fDef) {
		SqlTemplate sql = this.getSqlTemplate(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),fDef.getCurrentPage(),fDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCountComplementar(TypedFlatMap criteria) {
		SqlTemplate sql = this.getSqlTemplate(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		StringBuffer hqlFrom = new StringBuffer()
		.append(getPersistentClass().getName()).append(" RFC ")
		.append(" inner join fetch RFC.proprietario as P ")
		.append(" inner join fetch P.pessoa as P_P ")
		.append(" inner join fetch RFC.filial as F_RFC ")
		.append(" inner join fetch F_RFC.pessoa as F_RFC_P ")
		.append(" inner join fetch RFC.reciboComplementado as COMP ")
		.append(" inner join fetch COMP.filial as F_COMP ")
		.append("  left join fetch RFC.relacaoPagamento as RP ")
		.append("  left join fetch RFC.moedaPais as MO ")
		.append("  left join fetch MO.moeda as MD ")
		.append("  left join fetch RFC.controleCarga as CC ")
		.append("  left join fetch CC.filialByIdFilialOrigem as FCC ")
		.append(" inner join fetch RFC.meioTransporteRodoviario as MTR ")
		.append(" inner join fetch MTR.meioTransporte as MT ");

		hql.addFrom(hqlFrom.toString());

		hql.addCriteria("RFC.filial.id","=",criteria.getLong("filial.idFilial"));
		hql.addCriteria("RFC.tpReciboFreteCarreteiro","=",criteria.getString("tpReciboFreteCarreteiro"));
		hql.addCriteria("RFC.tpSituacaoRecibo","=",criteria.getString("tpSituacaoRecibo"));
		hql.addCriteria("COMP.id","=",criteria.getLong("reciboComplementado.idReciboFreteCarreteiro"));
		hql.addCriteria("COMP.nrReciboFreteCarreteiro","=",criteria.getLong("reciboComplementado.nrReciboFreteCarreteiro"));
		hql.addCriteria("RFC.nrReciboFreteCarreteiro","=",criteria.getLong("nrReciboFreteCarreteiro"));
		hql.addCriteria("CC.id","=",criteria.getLong("controleCarga.idControleCarga"));
		hql.addCriteria("P.id","=",criteria.getLong("proprietario.idProprietario"));
		hql.addCriteria("MTR.id","=",criteria.getLong("meioTransporteRodoviario.idMeioTransporte"));
		hql.addCriteria("RP.nrRelacaoPagamento","=",criteria.getLong("relacaoPagamento.nrRelacaoPagamento"));

		DateTime dhEmissaoInicial = criteria.getDateTime("dhEmissaoInicial");
		if (dhEmissaoInicial != null) {
			hql.addCriteria("RFC.dhEmissao.value",">=", dhEmissaoInicial);
		}
		
		DateTime dhEmissaoFinal = criteria.getDateTime("dhEmissaoFinal");
		if (dhEmissaoFinal != null) {
			hql.addCriteria("RFC.dhEmissao.value","<", dhEmissaoInicial.plusDays(1));
		}

		hql.addCriteria("RFC.dtPagtoReal",">=",criteria.getYearMonthDay("dtPagtoRealInicial"));
		hql.addCriteria("RFC.dtPagtoReal","<=",criteria.getYearMonthDay("dtPagtoRealFinal"));

		hql.addCriteria("RFC.dtProgramadaPagto",">=",criteria.getYearMonthDay("dtProgramadaPagtoInicial"));
		hql.addCriteria("RFC.dtProgramadaPagto","<=",criteria.getYearMonthDay("dtProgramadaPagtoFinal")); 
		
		hql.addOrderBy("F_COMP.sgFilial");
		hql.addOrderBy("COMP.nrReciboFreteCarreteiro");
		hql.addOrderBy("F_RFC.sgFilial");
		hql.addOrderBy("RFC.nrReciboFreteCarreteiro");

		return hql;
	}

	public List<ReciboFreteCarreteiro> findByProprietarioAndSituacao(Long idProprietario, String tpSituacaoRecibo, YearMonthDay dtBase, Long idReciboExclusivo) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("RFC");
		hql.addFrom(getPersistentClass().getName(),"RFC " +
				"inner join fetch RFC.filial as F " +
				"inner join fetch F.empresa as E");

		hql.addCriteria("RFC.proprietario.id","=",idProprietario);
		hql.addCriteria("RFC.tpSituacaoRecibo","=",tpSituacaoRecibo);
		hql.addCriteria("RFC.id","!=",idReciboExclusivo);

		if (dtBase != null) {
			hql.addCriteria("year(RFC.dhEmissao.value)","=",Integer.valueOf(dtBase.getYear()));
			hql.addCriteria("month(RFC.dhEmissao.value)","=",Integer.valueOf(dtBase.getMonthOfYear()));
		}

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public List<Map<String, Object>> findNotasCreditoToEmissaoColetaEntrega(TypedFlatMap criteria) {
		Boolean isEmpresaParceira = criteria.getBoolean("isEmpresaParceira");
		
		SqlTemplate hql = new SqlTemplate();

		StringBuffer hqlProjection = new StringBuffer()
		.append(" new Map(")
		.append("	CC.proprietario.id as idProprietario,")
		.append("	CC.meioTransporteByIdTransportado.id as idMeioTransporte,")
		.append("	Max(MT.nrFrota) as nrFrota,")
		.append("	Max(MT.nrIdentificador) as nrIdentificador,")
		.append("	Max(CC.motorista.id) as idMotorista")
		.append(" )");

		StringBuffer hqlFrom = new StringBuffer()
		.append(NotaCredito.class.getName()).append(" NC")
		.append("	inner join NC.controleCarga as CC")
		.append("	inner join CC.meioTransporteByIdTransportado as MT")
		.append("   left join NC.reciboFreteCarreteiro as RFC");

		hql.addProjection(hqlProjection.toString());
		hql.addFrom(hqlFrom.toString());

		hql.addCustomCriteria("(RFC.id is null OR RFC.tpSituacaoRecibo = ?)","CA");
		hql.addCriteria("CC.proprietario.id","=",criteria.getLong("proprietario.idProprietario"));
		hql.addCriteria("MT.id","=",criteria.getLong("meioTransporteRodoviario.idMeioTransporte"));
		
		//inclusão da critica para filtrar apenas filial do usuário logado
		hql.addCriteria("NC.filial.idFilial","=", criteria.getLong("filial.idFilial"));

		// Meio de transporte deve ser agregado ou eventual:
		hql.addCustomCriteria("(MT.tpVinculo = ? OR MT.tpVinculo = ?)",new Object[]{"A","E"});
		
		addCriteriosCiot(criteria, isEmpresaParceira, hql);

		hql.addGroupBy("CC.proprietario.id,CC.meioTransporteByIdTransportado.id");

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	private void addCriteriosCiot(TypedFlatMap criteria, Boolean isEmpresaParceira, SqlTemplate hql) {
		if(Boolean.TRUE.equals(isEmpresaParceira)){
			hql.addCustomCriteria("EXISTS (SELECT 1 FROM " + Empresa.class.getName() + " e WHERE e.tpEmpresa = 'P' AND e.idEmpresa = CC.proprietario.id) " );
			
		} else if(Boolean.FALSE.equals(isEmpresaParceira)){
			hql.addCustomCriteria("EXISTS (SELECT 1 FROM " + EventoControleCarga.class.getName()
							+ " evento WHERE TRUNC(evento.dhEvento.value) > ? AND TRUNC(evento.dhEvento.value) <= ? AND evento.controleCarga.idControleCarga = CC.idControleCarga AND evento.tpEventoControleCarga = 'EM') ",
							new Object[] { criteria.getYearMonthDay("dataRetroativaMaxima"), criteria.getYearMonthDay("dataRetroativaMinima") });
			hql.addCustomCriteria("NOT EXISTS (SELECT 1 FROM " + Empresa.class.getName() + " e WHERE e.tpEmpresa = 'P' AND e.idEmpresa = CC.proprietario.id) " );
			hql.addCriteria("CC.tpStatusControleCarga", "!=", "CA");
		}
	}

	public List<NotaCredito> findNotasCreditoToEmissaoColetaEntrega(Long idProprietario, Long idMeioTransporte, Long idFilial, TypedFlatMap criteria) {
		Boolean isEmpresaParceira = criteria.getBoolean("isEmpresaParceira");
		
		SqlTemplate hql = new SqlTemplate();
		hql.setDistinct();
		hql.addProjection("NC");
		
		StringBuilder hqlFrom = new StringBuilder()
		.append(NotaCredito.class.getName()).append(" NC")
		.append("	inner join NC.controleCarga as CC")
		.append("   left join NC.reciboFreteCarreteiro as RFC");

		hql.addFrom(hqlFrom.toString());
		hql.addCriteria("CC.proprietario.id", "=", idProprietario);
		hql.addCriteria("CC.meioTransporteByIdTransportado.id", "=", idMeioTransporte);
		hql.addCriteria("NC.filial.idFilial", "=", idFilial);
		hql.addCustomCriteria("(NC.reciboFreteCarreteiro IS NULL OR RFC.tpSituacaoRecibo = ?)", new Object[]{"A"});
		
		addCriteriosCiot(criteria, isEmpresaParceira, hql);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public BigDecimal findVlPostoPassagemNotasCredito(Long idProprietario, Long idMeioTransporte) {
		StringBuffer hql = new StringBuffer()
		.append(" SELECT")
		.append(" sum(CC.vlPedagio) ")
		.append(" FROM ").append(NotaCredito.class.getName()).append(" NC ")
		.append("   inner join NC.controleCarga as CC")
		.append("   left join NC.reciboFreteCarreteiro as RFC")
		.append(" where (RFC is null OR RFC.tpSituacaoRecibo = ?)")
		.append("   and CC.proprietario.id = ?")
		.append("   and CC.meioTransporteByIdTransportado.id = ?");

		return (BigDecimal)getAdsmHibernateTemplate().find(hql.toString(),new Object[]{"CA",idProprietario,idMeioTransporte}).get(0);
	}

	/**
	 * Consulta ids de recibos a partir de alguns critérios.
	 * 
	 * @param criteria
	 * @return List de Object[] com os ids dos Recibos.
	 */
	public List<Long> findRecibosToReport(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("RFC.idReciboFreteCarreteiro");

		StringBuffer hqlFrom = new StringBuffer();
		hqlFrom.append(ReciboFreteCarreteiro.class.getName()).append(" as RFC ")
		.append(" inner join RFC.meioTransporteRodoviario MTR ")
		.append(" inner join MTR.meioTransporte MT ");
		hql.addFrom(hqlFrom.toString());

		Long idRecibo = criteria.getLong("reciboFreteCarreteiro.idReciboFreteCarreteiro");
		if (idRecibo != null) {
			hql.addCriteria("RFC.idReciboFreteCarreteiro","=",idRecibo);
			hql.addCriteriaIn("RFC.tpSituacaoRecibo",new Object[]{"AJ","EJ","PA"});
		} else {
			hql.addCriteria("RFC.filial.id","=",criteria.getLong("filial.idFilial"));
			hql.addCriteria("RFC.proprietario.id","=",criteria.getLong("proprietario.idProprietario"));
			hql.addCriteria("RFC.meioTransporteRodoviario.id","=",criteria.getLong("meioTransporteRodoviario.idMeioTransporte"));

			hql.addCriteria("RFC.tpReciboFreteCarreteiro","=",criteria.getString("reciboFreteCarreteiro.tpReciboFreteCarreteiro"));
			hql.addCriteria("RFC.tpSituacaoRecibo","=",criteria.getString("reciboFreteCarreteiro.tpSituacaoRecibo"));
		}
		// Meio de transporte deve ser agregado ou eventual:
		hql.addCustomCriteria("(MT.tpVinculo = ? OR MT.tpVinculo = ?)",new Object[]{"A","E"});

		YearMonthDay dhEmissaoInicial = criteria.getYearMonthDay("dtEmissaoInicial");
		if (dhEmissaoInicial != null) {
			hql.addCriteria("RFC.dhEmissao.value",">=",JTDateTimeUtils.yearMonthDayToDateTime(dhEmissaoInicial));
		}
		YearMonthDay dhEmissaoFinal = criteria.getYearMonthDay("dtEmissaoFinal");
		if (dhEmissaoFinal != null) {
			hql.addCriteria("RFC.dhEmissao.value","<",JTDateTimeUtils.yearMonthDayToDateTime(dhEmissaoFinal.plusDays(1)));
		}

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param param
	 * @param isRowCount
	 * @return
	 */
	private StringBuffer addSqlByFindPaginatedByIdControleCarga(Long idControleCarga, List<Long> param, boolean isRowCount) {
		StringBuffer sql = new StringBuffer();
		if (!isRowCount) {
			sql.append("select new map(rfc.idReciboFreteCarreteiro as idReciboFreteCarreteiro, ")
			.append("rfc.vlBruto as vlBruto, ")
			.append("moeda.sgMoeda as moedaPais_moeda_sgMoeda, ")
			.append("moeda.dsSimbolo as moedaPais_moeda_dsSimbolo, ")
			.append("pessoaBeneficiario.nmPessoa as beneficiario_pessoa_nmPessoa, ")
			.append("banco.nrBanco as contaBancaria_agenciaBancaria_banco_nrBanco, ")
			.append("agenciaBancaria.nrAgenciaBancaria as contaBancaria_agenciaBancaria_nrAgenciaBancaria, ")
			.append("contaBancaria.nrContaBancaria as contaBancaria_nrContaBancaria ")
			.append(") ");
		}
		sql.append("from ")
		.append(ReciboFreteCarreteiro.class.getName()).append(" as rfc ")
		.append("left join rfc.beneficiario as beneficiario ")
		.append("left join beneficiario.pessoa as pessoaBeneficiario ")
		.append("left join rfc.contaBancaria as contaBancaria ")
		.append("left join contaBancaria.agenciaBancaria as agenciaBancaria ")
		.append("left join agenciaBancaria.banco as banco ")
		.append("left join rfc.moedaPais as moedaPais ")
		.append("left join moedaPais.moeda as moeda ")
		.append("where ")
		.append("rfc.controleCarga.id = ? ")
		.append("and rfc.blAdiantamento = 'S' ");

		param.add(idControleCarga);
		return sql;
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param findDefinition
	 * @return
	 */
	public ResultSetPage findPaginatedByIdControleCarga(Long idControleCarga, FindDefinition findDefinition) {
		List<Long> param = new ArrayList<Long>();
		StringBuffer sql = addSqlByFindPaginatedByIdControleCarga(idControleCarga, param, false);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
		return rsp;
	}

	/**
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public Integer getRowCountFindPaginatedByIdControleCarga(Long idControleCarga) {
		List<Long> param = new ArrayList<Long>();
		StringBuffer sql = addSqlByFindPaginatedByIdControleCarga(idControleCarga, param, true);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), param.toArray());
	}

	public ReciboFreteCarreteiro findByIdCustom(Long idReciboFreteCarreteiro) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(ReciboFreteCarreteiro.class.getName()).append(" RFC ")
		.append(" inner join fetch RFC.proprietario as PROP ")
		.append(" inner join fetch PROP.pessoa as P_PROP ")
		.append(" inner join fetch RFC.moedaPais as MP ")
		.append(" inner join fetch MP.moeda as M ")
		.append("  left join fetch RFC.relacaoPagamento as RP ")
		.append("  left join fetch RFC.usuario as US ")
		.append("  left join fetch RFC.controleCarga as CC ")
		.append("  left join fetch CC.filialByIdFilialOrigem as FO ")
		.append("  left join fetch FO.pessoa as P_FO ")
		.append("  left join fetch CC.filialByIdFilialDestino as FD ")
		.append("  left join fetch FD.pessoa as P_FD ")
		.append(" inner join fetch RFC.meioTransporteRodoviario as MTR ")
		.append(" inner join fetch MTR.meioTransporte as MT ")
		.append(" inner join fetch MT.modeloMeioTransporte as MODEL ")
		.append(" inner join fetch MODEL.marcaMeioTransporte ")
		.append("  left join fetch RFC.motorista as MOT ")
		.append("  left join fetch MOT.pessoa as P_MOT ")
		.append("  left join fetch RFC.beneficiario as BEN ")
		.append("  left join fetch BEN.pessoa as P_BEN ")
		.append("  left join fetch RFC.contaBancaria as CB ")
		.append("  left join fetch CB.agenciaBancaria as AB ")
		.append("  left join fetch AB.banco as B ")
		.append(" inner join fetch RFC.filial as F")
		.append(" inner join fetch F.pessoa as P_FILIAL")
		.append("  left join fetch RFC.reciboComplementado as RC ")
		.append("  left join fetch RC.filial as FILIAL_COMP ")
		.append("  left join fetch RFC.pendencia as pend ")
		.append("  left join fetch pend.ocorrencia as oco ")
		.append("  left join fetch oco.eventoWorkflow as evw ")
		.append("  left join fetch evw.tipoEvento as tpe ")
		.append("  left join fetch RFC.notaCreditos as ntc ")
		.append("  left join fetch ntc.controleCarga as cc ");
		
		hql.addProjection("RFC");
		hql.addFrom(hqlFrom.toString());
		hql.addCriteria("RFC.idReciboFreteCarreteiro", "=" , idReciboFreteCarreteiro);

		return (ReciboFreteCarreteiro)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}

	public ReciboFreteCarreteiro findByIdCustomColeta(Long idReciboFreteCarreteiro) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(ReciboFreteCarreteiro.class.getName()).append(" RFC ")
		.append(" inner join fetch RFC.proprietario as PROP ")
		.append(" inner join fetch PROP.pessoa as P_PROP ")
		.append(" inner join fetch RFC.moedaPais as MP ")
		.append(" inner join fetch MP.moeda as M ")
		.append(" inner join fetch RFC.meioTransporteRodoviario as MTR ")
		.append(" inner join fetch MTR.meioTransporte as MT ")
		.append(" inner join fetch MT.modeloMeioTransporte as MODEL ")
		.append(" inner join fetch MODEL.marcaMeioTransporte ")
		.append(" inner join fetch RFC.filial as F")
		.append(" inner join fetch F.pessoa as P_FILIAL")
		.append("  left join fetch RFC.notaCreditos as NOTAS ");
		
		hql.addProjection("RFC");
		hql.addFrom(hqlFrom.toString());
		hql.addCriteria("RFC.idReciboFreteCarreteiro","=",idReciboFreteCarreteiro);

		return (ReciboFreteCarreteiro)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}
	
	
	
	/**
	 * 
	 * @param idReciboFreteCarreteiro
	 * @return
	 */
	public Map<String, Object> findByIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		StringBuffer sql = new StringBuffer()
		.append("select new map(rfc.idReciboFreteCarreteiro as idReciboFreteCarreteiro, ")
		.append("rfc.pcAdiantamentoFrete as pcAdiantamentoFrete, ")
		.append("rfc.vlBruto as vlBruto, ")
		.append("rfc.obReciboFreteCarreteiro as obReciboFreteCarreteiro, ")
		.append("moeda.sgMoeda as moedaPais_moeda_sgMoeda, ")
		.append("moeda.dsSimbolo as moedaPais_moeda_dsSimbolo, ")
		.append("pessoaBeneficiario.tpIdentificacao as beneficiario_pessoa_tpIdentificacao, ")
		.append("pessoaBeneficiario.nrIdentificacao as beneficiario_pessoa_nrIdentificacao, ")
		.append("pessoaBeneficiario.nmPessoa as beneficiario_pessoa_nmPessoa, ")
		.append("pessoaProprietario.tpIdentificacao as proprietario_pessoa_tpIdentificacao, ")
		.append("pessoaProprietario.nrIdentificacao as proprietario_pessoa_nrIdentificacao, ")
		.append("pessoaProprietario.nmPessoa as proprietario_pessoa_nmPessoa, ")
		.append("banco.nrBanco as contaBancaria_agenciaBancaria_banco_nrBanco, ")
		.append("banco.nmBanco as contaBancaria_agenciaBancaria_banco_nmBanco, ")
		.append("agenciaBancaria.nrAgenciaBancaria as contaBancaria_agenciaBancaria_nrAgenciaBancaria, ")
		.append("agenciaBancaria.nmAgenciaBancaria as contaBancaria_agenciaBancaria_nmAgenciaBancaria, ")
		.append("contaBancaria.nrContaBancaria as contaBancaria_nrContaBancaria, ")
		.append("contaBancaria.dvContaBancaria as contaBancaria_dvContaBancaria) ")
		.append("from ")
		.append(ReciboFreteCarreteiro.class.getName()).append(" as rfc ")
		.append("left join rfc.beneficiario as beneficiario ")
		.append("left join beneficiario.pessoa as pessoaBeneficiario ")
		.append("left join rfc.proprietario as proprietario ")
		.append("left join proprietario.pessoa as pessoaProprietario ")
		.append("left join rfc.contaBancaria as contaBancaria ")
		.append("left join contaBancaria.agenciaBancaria as agenciaBancaria ")
		.append("left join agenciaBancaria.banco as banco ")
		.append("left join rfc.moedaPais as moedaPais ")
		.append("left join moedaPais.moeda as moeda ")
		.append("where ")
		.append("rfc.id = ? ");

		List<Long> param = new ArrayList<Long>(1);
		param.add(idReciboFreteCarreteiro);

		List<Map<String, Object>> list = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		return list.get(0);
	}

	public List findLookupCustom(TypedFlatMap criteria) {
		SqlTemplate sql = this.getSqlTemplateCustom(criteria);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(),Integer.valueOf(1),Integer.valueOf(2),sql.getCriteria());
		return rsp.getList();
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria,FindDefinition fDef) {
		SqlTemplate sql = this.getSqlTemplateCustom(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),fDef.getCurrentPage(),fDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate sql = this.getSqlTemplateCustom(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	private SqlTemplate getSqlTemplateCustom(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(RFC.idReciboFreteCarreteiro","idReciboFreteCarreteiro");
		hql.addProjection("RFC.nrReciboFreteCarreteiro","nrReciboFreteCarreteiro");
		hql.addProjection("RC.idReciboFreteCarreteiro","idReciboComplementado");
		hql.addProjection("FIL.idFilial","filial_idFilial");
		hql.addProjection("FIL.sgFilial","filial_sgFilial");
		hql.addProjection("P_FIL.nmFantasia","filial_pessoa_nmFantasia");
		hql.addProjection("RFC.dhEmissao","dhEmissao");
		hql.addProjection("RFC.tpSituacaoRecibo","tpSituacaoRecibo");
		hql.addProjection("RFC.blAdiantamento","blAdiantamento");
		hql.addProjection("FO.idFilial","controleCarga_idFilialOrigem");
		hql.addProjection("FO.sgFilial","controleCarga_sgFilialOrigem");
		hql.addProjection("PO.nmFantasia","controleCarga_nmFilialOrigem");
		hql.addProjection("CC.nrControleCarga","controleCarga_nrControleCarga");
		hql.addProjection("P_PROP.tpIdentificacao","proprietario_tpIdentificacao");
		hql.addProjection("P_PROP.nrIdentificacao","proprietario_nrIdentificacao");
		hql.addProjection("P_PROP.nmPessoa","proprietario_nmPessoa");
		hql.addProjection("MT.nrFrota","meioTransporte_nrFrota");
		hql.addProjection("MT.nrIdentificador","meioTransporte_nrIdentificador");
		hql.addProjection("MAR.dsMarcaMeioTransporte","meioTransporte_dsMarca");
		hql.addProjection("MOD.dsModeloMeioTransporte","meioTransporte_dsModelo");
		hql.addProjection("RFC.dtPagtoReal","dtPgtoReal");
		hql.addProjection("M.sgMoeda","sgMoeda");
		hql.addProjection("M.dsSimbolo","dsSimbolo");
		hql.addProjection("RFC.vlLiquido","vlLiquido");

		hql.addProjection("CC.id","controleCarga_idControleCarga");
		hql.addProjection("PROP.id","proprietario_idProprietario");
		hql.addProjection("MT.id","meioTransporteRodoviario_idMeioTransporte");
		hql.addProjection("MOT.id","motorista_idMotorista)");

		StringBuilder sb = new StringBuilder()
		.append(ReciboFreteCarreteiro.class.getName()).append(" RFC ")
		.append(" inner join RFC.proprietario as PROP")
		.append(" inner join PROP.pessoa as P_PROP")
		.append(" inner join RFC.moedaPais as MP")
		.append(" inner join MP.moeda as M")
		.append(" inner join RFC.meioTransporteRodoviario as MTR")
		.append(" inner join MTR.meioTransporte as MT")
		.append(" inner join MT.modeloMeioTransporte as MOD")
		.append(" inner join MOD.marcaMeioTransporte as MAR")
		.append(" left  join RFC.motorista as MOT")
		.append(" left  join MOT.pessoa as P_MOT")
		.append(" inner join RFC.filial as FIL")
		.append(" inner join FIL.pessoa as P_FIL")
		.append("  left join RFC.controleCarga as CC")
		.append("  left join CC.filialByIdFilialOrigem as FO")
		.append("  left join FO.pessoa as PO")
		.append("  left join RFC.relacaoPagamento as RP")
		.append("  left join RFC.reciboComplementado as RC");

		hql.addFrom(sb.toString());

		
		if (criteria.get("tpReciboFreteCarreteiro") instanceof String) {
		hql.addCriteria("RFC.tpReciboFreteCarreteiro","=",criteria.getString("tpReciboFreteCarreteiro"));
		} else {
			hql.addCriteriaIn("RFC.tpReciboFreteCarreteiro", (String[])criteria.get("tpReciboFreteCarreteiro"));
		}

		hql.addCriteria("FIL.id","=",criteria.getLong("filial.idFilial"));
		hql.addCriteria("RFC.nrReciboFreteCarreteiro","=",criteria.getLong("nrReciboFreteCarreteiro"));
		hql.addCriteria("RFC.blAdiantamento", "=", criteria.getBoolean("blAdiantamento"));
		hql.addCriteria("RFC.tpSituacaoRecibo", "=", criteria.getString("tpSituacaoRecibo"));
		hql.addCriteria("CC.id","=",criteria.getLong("controleCarga.idControleCarga"));
		hql.addCriteria("PROP.id","=",criteria.getLong("proprietario.idProprietario"));
		hql.addCriteria("MTR.id","=",criteria.getLong("meioTransporteRodoviario.idMeioTransporte"));

		YearMonthDay dhEmissaoInicial = criteria.getYearMonthDay("dhEmissaoInicial");
		if (dhEmissaoInicial != null) {
			hql.addCriteria("RFC.dhEmissao.value",">=",JTDateTimeUtils.yearMonthDayToDateTime(dhEmissaoInicial));
		}
		YearMonthDay dhEmissaoFinal = criteria.getYearMonthDay("dhEmissaoFinal");
		if (dhEmissaoFinal != null) {
			hql.addCriteria("RFC.dhEmissao.value","<",JTDateTimeUtils.yearMonthDayToDateTime(dhEmissaoFinal.plusDays(1)));
		}

		hql.addCriteria("RFC.dtPagtoReal",">=",criteria.getYearMonthDay("dtPagtoRealInicial"));
		hql.addCriteria("RFC.dtPagtoReal","<=",criteria.getYearMonthDay("dtPagtoRealFinal")); 
		
		hql.addCriteria("RFC.dtProgramadaPagto",">=",criteria.getYearMonthDay("dtProgramadaPagtoInicial"));
		hql.addCriteria("RFC.dtProgramadaPagto","<=",criteria.getYearMonthDay("dtProgramadaPagtoFinal")); 
		
		hql.addCriteria("RP.nrRelacaoPagamento","=",criteria.getLong("relacaoPagamento.nrRelacaoPagamento"));

		Boolean blComplementar = criteria.getBoolean("blComplementar");
		if (blComplementar != null && !blComplementar) {
			hql.addCustomCriteria("RFC.reciboComplementado.id is null");
		}

		hql.addOrderBy("RFC.nrReciboFreteCarreteiro");
		hql.addOrderBy("FO.sgFilial");
		hql.addOrderBy("RFC.dhEmissao.value", "desc");

		return hql;
	}

	/**
	 * Método que retorna uma lista de Recibo Frete Carreteiro a partir do ID do Controle de Carga.
	 * @param idControleCarga
	 * @return
	 */
	public List<ReciboFreteCarreteiro> findReciboFreteCarreteiroByIdControleCarga(Long idControleCarga) {
		return findReciboFreteCarreteiroByIdControleCarga(idControleCarga, null);
	}

	/**
	 * Método que retorna uma lista de Recibo Frete Carreteiro a partir do ID do Controle de Carga.
	 * @param idControleCarga
	 * @param blAdiantamento
	 * @return
	 */
	public List<ReciboFreteCarreteiro> findReciboFreteCarreteiroByIdControleCarga(Long idControleCarga, Boolean blAdiantamento) {
		return findReciboFreteCarreteiroByIdControleCarga(idControleCarga, null, null);
	}

	public List<Map<String, Object>> findDadosReciboPUDParaCIOT(Long idReciboFreteCarreteiro) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idRecibo", idReciboFreteCarreteiro);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT rfc.id_recibo_frete_carreteiro as idReciboFreteCarreteiro, \n");
		sql.append("  rfc.vl_liquido as vlLiquido, \n");
		sql.append("  rfc.vl_inss as vlInss, \n");
		sql.append("  rfc.vl_irrf as vlIrrf, \n");
		sql.append("  rfc.id_proprietario as idProprietario, \n");
		sql.append("  cc.id_controle_carga as idControleCarga, \n");
		sql.append("  cc.ps_total_frota as psTotalFrota, \n"); 
		sql.append("  p.nr_identificacao as nrIdentificacao, \n");
		sql.append("  c.id_ciot as idCiot, \n");
		sql.append("  c.nr_ciot as nrCiot, \n");
		sql.append("  c.nr_cod_verificador  as nrCodVerificador \n");
		sql.append("FROM recibo_frete_carreteiro rfc , \n");
		sql.append("  nota_credito nc , \n");
		sql.append("  controle_carga cc , \n");
		sql.append("  filial f , \n");
		sql.append("  pessoa p , \n");
		sql.append("  ciot c, \n");
		sql.append("  ciot_controle_carga ccc \n");
		sql.append("WHERE \n");
		sql.append("rfc.id_recibo_frete_carreteiro = nc.id_recibo_frete_carreteiro \n");
		sql.append("AND nc.id_controle_carga = cc.id_controle_carga \n");
		sql.append("AND cc.id_filial_origem = f.id_filial \n");
		sql.append("AND f.id_empresa = p.id_pessoa \n");
		sql.append("AND cc.id_controle_carga = ccc.id_controle_carga  \n");
		sql.append("AND c.id_ciot = ccc.id_ciot \n");
		sql.append("AND c.tp_situacao = 'G' \n");
		sql.append("AND rfc.id_recibo_frete_carreteiro = :idRecibo ");
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parametersValues, getConfigureSqlQueryFindDadosReciboPUDParaCIOT());
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryFindDadosReciboPUDParaCIOT(){
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idReciboFreteCarreteiro", Hibernate.LONG);
				sqlQuery.addScalar("vlLiquido", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vlInss", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vlIrrf", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("idProprietario", Hibernate.LONG);
				sqlQuery.addScalar("idControleCarga", Hibernate.LONG);
				sqlQuery.addScalar("psTotalFrota", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("nrIdentificacao", Hibernate.STRING);
				sqlQuery.addScalar("idCiot", Hibernate.LONG);
				sqlQuery.addScalar("nrCiot", Hibernate.LONG);
				sqlQuery.addScalar("nrCodVerificador", Hibernate.STRING);
			}
		};
		return csq;
	}
	
	/**
	 * Método que retorna uma lista de Recibo Frete Carreteiro a partir do ID do Controle de Carga.
	 * @param idControleCarga
	 * @param blAdiantamento
	 * @param tpProprietario - Array de objetos com os TP_PROPRIETARIOS: [A]gregado, [E]ventual ... 
	 * @return
	 */
	public List<ReciboFreteCarreteiro> findReciboFreteCarreteiroByIdControleCarga(Long idControleCarga, Boolean blAdiantamento, String [] tpProprietario) {
		ArrayList<Object> param = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer();

		// Se houver necessidade de joins, colocar no FROM tomando cuidado com os campos nullable.
		hql.append(" from ").append(ReciboFreteCarreteiro.class.getName()).append(" rfc ");
		hql.append(" inner join fetch rfc.proprietario prop ");
		hql.append(" where rfc.controleCarga.id = ? ");
		hql.append(" and rfc.tpSituacaoRecibo != ? ");

		param.add(idControleCarga);
		param.add("CA");

		if (blAdiantamento != null) {
			hql.append(" and rfc.blAdiantamento = ? ");
			param.add(blAdiantamento);
		}

		if (tpProprietario != null && tpProprietario.length > 0) {
			hql.append(" and prop.tpProprietario in (");
			int i = 0;
			for (i = 0; i < tpProprietario.length; i++) {
				String str = (String) tpProprietario[i];
				hql.append("'" + str);
				if (i < tpProprietario.length - 1) {
					hql.append("',");
				}
			}
			hql.append("')");
		}
		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param blAdiantamento
	 * @return
	 */
	public List<ReciboFreteCarreteiro> findReciboFreteCarreteiroByEmissaoControleCarga(Long idControleCarga, Boolean blAdiantamento) {
		StringBuffer sql = new StringBuffer()
		.append("from ").append(ReciboFreteCarreteiro.class.getName()).append(" rfc ")
		.append("where rfc.controleCarga.id = ? ")
		.append("and rfc.blAdiantamento = ? ")
		.append("and rfc.dhEmissao.value is null ");

		return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga, blAdiantamento});
	}

	/**
	 * 
	 * @param idControleCarga
	 */
	public void storeReciboToTrocaVeiculoByIdControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
			.append("update ")
			.append(ReciboFreteCarreteiro.class.getName()).append(" as rfc ")
			.append(" set rfc.tpSituacaoRecibo = 'CA' ")
			.append("where rfc.controleCarga.id = ? and (rfc.tpSituacaoRecibo = 'GE' or rfc.tpSituacaoRecibo = 'EM') ");

		List<Long> param = new ArrayList<Long>(1);
		param.add(idControleCarga);
		super.executeHql(sql.toString(), param);
	}

	/**
	 * 
	 * @param idManifesto
	 * @return
	 */
	public List<ReciboFreteCarreteiro> findReciboFreteCarreteiroByIdManifesto(Long idManifesto) {
		StringBuffer sql = new StringBuffer()
			.append("from ").append(ReciboFreteCarreteiro.class.getName()).append(" rfc ")
			.append("where ")
			.append("rfc.manifestoViagemNacional.id = ? ")
			.append("and rfc.tpSituacaoRecibo <> 'CA' ");

		return getAdsmHibernateTemplate().find(sql.toString(), new Long[]{idManifesto});
	}

	/**
	 * Exclui os recibos de viagem nacional.
	 * @param idManifestoViagemNacional
	 */
	public void removeRecibos(Long idManifestoViagemNacional) {
		StringBuilder sql = new StringBuilder()
		.append("DELETE from ")
		.append(ReciboFreteCarreteiro.class.getName()).append(" as rfc ")
		.append("WHERE rfc.manifestoViagemNacional.id = ? ");

		List<Long> param = new ArrayList<Long>(1);
		param.add(idManifestoViagemNacional);

		executeHql(sql.toString(), param);
	}
	
	
	public Long findPropPessIdByCodPlacaNroPlaca(String codPlaca, Long nroPlaca){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("propPessId");
		
		StringBuffer sb = new StringBuffer();
		sb.append(Veiculos.class.getName()).append(" veic ")
		.append("where ")
		.append("veic.codPlaca = ? ")
		.append("and veic.nroPlaca = ? ");

		
    	sql.addFrom(sb.toString());

		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{codPlaca, nroPlaca});
	}
	
	/**
	 * Retorna o resultado da pesquisa para uma suggest de recibo.
	 * 
	 * @param sgFilial
	 * @param nrReciboFreteCarreteiro
	 * @param limiteRegistros
	 * 
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findReciboSuggest(
			final String sgFilial, final Long nrReciboFreteCarreteiro,
			final Integer limiteRegistros) {	
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>();
				
		final StringBuilder sql = getQueryReciboSuggest(sgFilial, nrReciboFreteCarreteiro, limiteRegistros);
		
		final ConfigureSqlQuery csq = getProjectionReciboSuggest();
		
		final HibernateCallback hcb = getCallbackReciboSuggest(sgFilial, nrReciboFreteCarreteiro, limiteRegistros, sql, csq);
				
		List<Object[]> list = getHibernateTemplate().executeFind(hcb);
	
		for (Object[] o: list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idReciboFreteCarreteiro", o[0]);
			map.put("nrReciboFreteCarreteiro", o[1]);
			map.put("sgFilial", o[2]);
			map.put("tpReciboFreteCarreteiro", ((DomainValue)o[3]).getDescriptionAsString());
			map.put("tpSituacaoRecibo", o[4]);
			map.put("dhEmissao", o[5]);
			toReturn.add(map);			
		}
		
		return toReturn;
	}

	private HibernateCallback getCallbackReciboSuggest(
			final String sgFilial, final Long nrReciboFreteCarreteiro,
			final Integer limiteRegistros, final StringBuilder sql,
			final ConfigureSqlQuery csq) {
		return new HibernateCallback() {
			public Object doInHibernate(Session session){
				SQLQuery query = session.createSQLQuery(sql.toString());
				
				if(StringUtils.isNotBlank(sgFilial)){
					query.setString("sgFilial", sgFilial.trim());
				}
				
				if(nrReciboFreteCarreteiro != null){
					query.setLong("nrReciboFreteCarreteiro", nrReciboFreteCarreteiro);			
				}
				
				if(limiteRegistros != null){
					query.setInteger("limiteRegistros", limiteRegistros);			
				}
					
            	csq.configQuery(query);
				return query.list();
			}
		};
	}

	private ConfigureSqlQuery getProjectionReciboSuggest() {
		return new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_recibo_frete_carreteiro", Hibernate.LONG);
				sqlQuery.addScalar("nr_recibo_frete_carreteiro", Hibernate.STRING);
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);			
				
				Properties propertiesTpReciboFreteCarreteiro = new Properties();
				propertiesTpReciboFreteCarreteiro.put("domainName","DM_TIPO_RECIBO_PAGAMENTO_FRETE_CARRETEIR");
				sqlQuery.addScalar("tp_recibo_frete_carreteiro", Hibernate.custom(DomainCompositeUserType.class,propertiesTpReciboFreteCarreteiro));
				
				sqlQuery.addScalar("tp_situacao_recibo",  Hibernate.STRING);
				
				sqlQuery.addScalar("dh_emissao", Hibernate.TIMESTAMP);
			}
		};
	}

	private StringBuilder getQueryReciboSuggest(
			final String sgFilial, final Long nrReciboFreteCarreteiro,
			final Integer limiteRegistros) {
		final StringBuilder sql = new StringBuilder();
				
		sql.append("SELECT ");
		sql.append(" rfc.id_recibo_frete_carreteiro,");
		sql.append(" rfc.nr_recibo_frete_carreteiro,");
		sql.append(" f.sg_filial,");
		sql.append(" rfc.tp_recibo_frete_carreteiro,");
		sql.append(" rfc.tp_situacao_recibo,");
		sql.append(" rfc.dh_emissao");
		sql.append(" FROM recibo_frete_carreteiro rfc");
		sql.append("  JOIN filial f");
		sql.append("   ON f.id_filial = rfc.id_filial");
		sql.append(" WHERE rfc.id_recibo_complementado IS NULL");		
		
		if(StringUtils.isNotBlank(sgFilial)){
			sql.append(" AND f.sg_filial = :sgFilial");
		}
		
		if(nrReciboFreteCarreteiro != null){
			sql.append(" AND rfc.nr_recibo_frete_carreteiro = :nrReciboFreteCarreteiro");
		}
		
		if (limiteRegistros != null) {
			sql.append(" AND rownum <= :limiteRegistros");
		}
		
		return sql;
	}

	public List findByIntervalo(YearMonthDay dtInicial, YearMonthDay dtFinal, String sgFilial) {
	
		
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		
		final StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ID_RECIBO_FRETE_CARRETEIRO ");
		sql.append(" FROM RECIBO_FRETE_CARRETEIRO RE, ");
		sql.append("   FILIAL FI ");
		sql.append(" WHERE RE.ID_RECIBO_COMPLEMENTADO IS NULL ");
		sql.append(" AND RE.ID_FILIAL                  = FI.ID_FILIAL ");
		sql.append(" AND FI.SG_FILIAL                  = UPPER(:sgFilial) ");
		sql.append(" AND TP_RECIBO_FRETE_CARRETEIRO    = 'C' ");
		sql.append(" AND TP_SITUACAO_RECIBO           IN ('AJ','EJ','PA','EM') ");
		sql.append(" AND TRUNC(DH_EMISSAO)                   >= :dtInicial ");
		sql.append(" AND TRUNC(DH_EMISSAO)                    <= :dtFinal ");
		
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("sgFilial", sgFilial);
		parametersValues.put("dtInicial", dtInicial);
		parametersValues.put("dtFinal", dtFinal);
		
	
		
		ConfigureSqlQuery config = 	 new ConfigureSqlQuery() {
				public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("ID_RECIBO_FRETE_CARRETEIRO", Hibernate.LONG);
				}
			};
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parametersValues, config);
	
	}
}