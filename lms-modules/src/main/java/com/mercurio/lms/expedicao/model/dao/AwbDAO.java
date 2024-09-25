package com.mercurio.lms.expedicao.model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.LiberaAWBComplementar;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesAwb;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AwbDAO extends BaseCrudDao<Awb, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return Awb.class;
	}
	
	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("ciaFilialMercurio", FetchMode.JOIN);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idFilialOrigem
	 * @param idCiaFilialMercurio
	 * @param nrAwb
	 * @param dsSerie
	 * @return <Awb>
	 */
	public Awb findAwb(Long idFilialOrigem, Long idCiaFilialMercurio, Long nrAwb, String dsSerie) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("filialByIdFilialOrigem", "fo");
		dc.createAlias("ciaFilialMercurio", "fm");

		dc.add(Restrictions.eq("nrAwb", nrAwb));
		dc.add(Restrictions.eq("dsSerie", dsSerie));
		dc.add(Restrictions.eq("fo.id", idFilialOrigem));
		dc.add(Restrictions.eq("fm.id", idCiaFilialMercurio));

		return (Awb) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	/**
	 * Busca o somatório das taxas de combustiveis e terrestre, valor do ICMS e valor do Frete.<BR>
	 * @author Robson Edemar Gehl
	 * @param idFilialPrestadora
	 * @param idCiaAerea
	 * @param dtDecendioInicial
	 * @param dtDecendioFinal
	 * @return Coleção de pojos Awb, com o somatório dos campos.
	 */
	public List findPrestacaoContasICMS(Long idFilialPrestadora, Long idCiaAerea, YearMonthDay dtDecendioInicial, YearMonthDay dtDecendioFinal) {

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" new Map (awb.pcAliquotaICMS as pcAliquotaICMS, sum(awb.vlICMS) as vlICMS, " +
									"sum(awb.vlFrete) as vlFrete, sum(awb.vlTaxaTerrestre) as vlTaxaTerrestre, " +
									"sum(awb.vlTaxaCombustivel) as vlTaxaCombustivel) ");

		sql.addFrom(new StringBuffer(CiaFilialMercurio.class.getName()).append(" cfm ")
				.append(" join cfm.empresa emp ")
				.append(" join cfm.filial fil ")
				.append(" join cfm.awbs awb ")
				.toString()
			);

		sql.addCriteria("emp.id","=",idCiaAerea);
		sql.addCustomCriteria(" ( (fil.id = ? and fil.filialByIdFilialResponsavalAwb.id is null) or fil.filialByIdFilialResponsavalAwb.id = ? )");
		sql.addCriteriaValue(idFilialPrestadora);
		sql.addCriteriaValue(idFilialPrestadora);

		sql.addCriteria("trunc(awb.dhEmissao.value)",">=",JTDateTimeUtils.getFirstHourOfDay(JTDateTimeUtils.yearMonthDayToDateTime(dtDecendioInicial)));
		sql.addCriteria("trunc(awb.dhEmissao.value)","<=",JTDateTimeUtils.getLastHourOfDay(JTDateTimeUtils.yearMonthDayToDateTime(dtDecendioFinal)));

		sql.addCustomCriteria("awb.tpStatusAwb = 'E'");
		sql.addCustomCriteria("awb.tpLocalEmissao = 'M'");
		sql.addCustomCriteria("not exists (select 1 from "+PrestacaoConta.class.getName()+
				" prest " +
				" join prest.intervaloAwbs inter"+
				" where inter.id = awb.id)");

		sql.addGroupBy("awb.pcAliquotaICMS");
		sql.addOrderBy("awb.pcAliquotaICMS");

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Busca as CiaFilialMercurio e Awbs para Prestação de Contas.<BR>
	 *@author Robson Edemar Gehl
	 * @param idFilialPrestadora
	 * @param idCiaAerea
	 * @param dtDecendioInicial
	 * @param dtDecendioFinal
	 * @return um Map com o ID da CiaFilialMercurio e Awb (pojo), com as chaves: {idCiaFilialMercurio, awb}
	 */
	public List findPrestacaoContas(Long idFilialPrestadora, Long idCiaAerea, YearMonthDay dtDecendioInicial, YearMonthDay dtDecendioFinal) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" new Map (cfm.id as idCiaFilialMercurio, awb as awb ) ");

		sql.addFrom(new StringBuffer(CiaFilialMercurio.class.getName()).append(" cfm ")
				.append(" join cfm.empresa emp ")
				.append(" join cfm.filial fil ")
				.append(" join cfm.awbs awb ")
				.toString()
		);

		sql.addCriteria("emp.id","=",idCiaAerea);
		sql.addCustomCriteria(" ( (fil.id = ? and fil.filialByIdFilialResponsavalAwb.id is null) or fil.filialByIdFilialResponsavalAwb.id = ? )");
		sql.addCriteriaValue(idFilialPrestadora);
		sql.addCriteriaValue(idFilialPrestadora);
		
		sql.addCriteria("awb.dhEmissao.value",">=",JTDateTimeUtils.getFirstHourOfDay(JTDateTimeUtils.yearMonthDayToDateTime(dtDecendioInicial)));
		sql.addCriteria("awb.dhEmissao.value","<=",JTDateTimeUtils.getLastHourOfDay(JTDateTimeUtils.yearMonthDayToDateTime(dtDecendioFinal)));
			
		sql.addCustomCriteria("awb.tpStatusAwb in ('E','C')");
		sql.addCustomCriteria("awb.tpLocalEmissao = 'M'");
		sql.addCustomCriteria("awb.prestacaoConta.id is null");
		
		sql.addOrderBy("awb.nrAwb");

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public List findByNrAwb(String nrAwb) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("nrAwb", Long.valueOf(nrAwb)));
		return findByDetachedCriteria(dc);
	}
	
	public List<Awb> findByNrChave(String nrChave) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("nrChave", nrChave));
		return findByDetachedCriteria(dc);
	}

	public List findDocumentosServico(Long idAwb) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("ctoAwbs", "ca");
		dc.createAlias("ca.conhecimento", "c");	
		dc.add(Restrictions.eq("idAwb", idAwb));
		dc.setProjection(Projections.property("c.id"));

		return findByDetachedCriteria(dc);
	}

	/**
	 * Atualiza a tabela awb com null no campo ID_PRESTACAO_CONTA
	 * @param idPrestacaoConta
	 */
	public void updateAwbDesmarcarPrestacaoConta(final Long idPrestacaoConta) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				Query query = null;
				String hql = "UPDATE " + getPersistentClass().getName() + " \n" +
							 "SET prestacaoConta = null \n" +
							 "WHERE prestacaoConta.id = :idPrestacaoConta \n"; 

				query = session.createQuery(hql);
				query.setParameter("idPrestacaoConta", idPrestacaoConta);
				query.executeUpdate();

				return null;
		}
		});

		this.getHibernateTemplate().flush();
	}

	public List findByNrAwbByCiaAerea(String dsSerie, Long nrAwb, Integer dvAwb, Long idCiaAerea, Long idFilialOrigem, String tpStatusAwb) {
		ProjectionList projectionList = Projections.projectionList()
		.add(Projections.property("idAwb"), "idAwb")
		.add(Projections.property("nrAwb"), "nrAwb")
		.add(Projections.property("dsSerie"), "dsSerie")
		.add(Projections.property("dvAwb"), "dvAwb")
		.add(Projections.property("dhEmissao.value"), "dhEmissao")
		.add(Projections.property("dsVooPrevisto"), "dsVooPrevisto")
		.add(Projections.property("m.sgMoeda"), "moeda.sgMoeda")
		.add(Projections.property("a.tpStatusAwb"), "tpStatusAwb")
		.add(Projections.property("m.dsSimbolo"), "moeda.dsSimbolo")
		.add(Projections.property("vlFrete"), "vlFrete");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "a")
		.setProjection(projectionList)
		.add(Restrictions.eq("a.dsSerie", dsSerie))
		.add(Restrictions.eq("a.nrAwb", nrAwb))
		.add(Restrictions.eq("a.dvAwb", dvAwb))
		.add(Restrictions.eq("a.ciaFilialMercurio.id", idCiaAerea));
		if(idFilialOrigem!=null){
			dc.add(Restrictions.eq("a.filialByIdFilialOrigem.id", idFilialOrigem));
		}
		if(tpStatusAwb != null) {
			dc.add(Restrictions.eq("a.tpStatusAwb", tpStatusAwb));
		}
		dc.createAlias("a.moeda", "m")
		.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
	}

	public List findByNrAwbByEmpresa(String dsSerie, Long nrAwb, Long idEmpresa, Integer digito) {
		ProjectionList projectionList = Projections.projectionList()
		.add(Projections.property("idAwb"), "idAwb")
		.add(Projections.property("nrAwb"), "nrAwb")
		.add(Projections.property("dvAwb"), "dvAwb");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "a")
		.createAlias("a.ciaFilialMercurio", "c")
		.setProjection(projectionList)
		.add(Restrictions.eq("a.dsSerie", dsSerie))
		.add(Restrictions.eq("a.nrAwb", nrAwb))
		.add(Restrictions.eq("a.dvAwb", digito))
		.add(Restrictions.eq("c.empresa.id", idEmpresa));

		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
	}

	/**
	 * Pesquisa apenas pré-awbs conforme os criterios informados.
	 * 
	 * @param criteria criterios de pesquisa
	 * @return ResultSetPage contendo uma lista de TypedFlatMap
	 */
	public ResultSetPage findPaginatedPreAwb(TypedFlatMap criteria) {
		DetachedCriteria dc = this.createCriteriaForPreAwb(criteria);
		dc.setProjection(this.createProjectionsPreAwb());
		
		dc.addOrder(Order.asc("idAwb"));
		
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}

	/**
	 * Conta o numero de registros para a pesquisa de pré-awbs.
	 * 
	 * @param criteria criterios de pesquisa
	 * @return numero de registros retornados pela consulta
	 */
	public Integer getRowCountPreAwb(TypedFlatMap criteria) {
		DetachedCriteria dc = this.createCriteriaForPreAwb(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	/**
	 * Foi sobrescrito apenas para adicionar visibilidade ao metodo pai.
	 * 
	 * @param idAwb
	 * @return
	 */
	@Override
	public Awb findById(Long idAwb) {
		return super.findById(idAwb);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		StringBuilder hql = new StringBuilder()
		.append(" new map( ")
		.append(" a.idAwb as idAwb, ")		
		.append(" e.idEmpresa as ciaFilialMercurio_empresa_idEmpresa, ")
		.append(" e.sgEmpresa as ciaFilialMercurio_empresa_sgEmpresa, ")
		.append(" p.nmPessoa as ciaFilialMercurio_empresa_pessoa_nmPessoa, ")
		.append(" a.dsSerie as dsSerieAwb, ")
		.append(" a.nrAwb as nrAwb, ")
		.append(" a.dvAwb as dvAwb, ")
		.append(" a.tpStatusAwb as tpStatusAwb, ")
		.append(" ao.sgAeroporto as aeroportoByIdAeroportoOrigem_sgAeroporto, ")
		.append(" ad.sgAeroporto as aeroportoByIdAeroportoDestino_sgAeroporto, ")
		.append(" a.dhEmissao as dhEmissao, ")
		.append(" a.vlFrete as vlFrete, ")
		.append(" a.vlFreteCalculado as vlFreteCalculado, ")
		.append(" m.sgMoeda as moeda_sgMoeda, ")
		.append(" m.dsSimbolo as moeda_dsSimbolo, ")
		.append(" a.dsVooPrevisto as dsVooPrevisto,")
		.append(" fca.nrFaturaCiaAerea as nrFaturaAwb, ")
		.append(" a.tpAwb as tpAwb ") 
		.append(" ) ");
		
		SqlTemplate sql = createCriteriaPaginated(criteria);
		sql.addProjection(hql.toString());
		sql.addOrderBy("p.nmPessoa");
		sql.addOrderBy("a.dsSerie");
		sql.addOrderBy("a.nrAwb");
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
		List result = rsp.getList();
		result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
		rsp.setList(result);
		
		return rsp;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = createCriteriaPaginated(criteria);
		sql.addProjection("count(a.id)");
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("ciaFilialMercurio", FetchMode.JOIN);
		lazyFindById.put("ciaFilialMercurio.empresa", FetchMode.JOIN);
		lazyFindById.put("ciaFilialMercurio.empresa.pessoa", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoOrigem", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoOrigem.pessoa", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoEscala", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoEscala.pessoa", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoDestino", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoDestino.pessoa", FetchMode.JOIN);
		lazyFindById.put("clienteByIdClienteExpedidor", FetchMode.JOIN);
		lazyFindById.put("clienteByIdClienteExpedidor.pessoa", FetchMode.JOIN);
		lazyFindById.put("clienteByIdClienteDestinatario", FetchMode.JOIN);
		lazyFindById.put("clienteByIdClienteDestinatario.pessoa", FetchMode.JOIN);
		lazyFindById.put("clienteByIdClienteTomador", FetchMode.JOIN);
		lazyFindById.put("clienteByIdClienteTomador.pessoa", FetchMode.JOIN);
		lazyFindById.put("naturezaProduto", FetchMode.JOIN);
		lazyFindById.put("tarifaSpot", FetchMode.JOIN);
		lazyFindById.put("produtoEspecifico", FetchMode.JOIN);
		lazyFindById.put("awbEmbalagems", FetchMode.JOIN);
		lazyFindById.put("awbEmbalagems.embalagem", FetchMode.JOIN);
		lazyFindById.put("dimensoes", FetchMode.SELECT);
		lazyFindById.put("prestacaoConta", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialDestino", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialDestino.pessoa", FetchMode.JOIN);
		lazyFindById.put("moeda", FetchMode.JOIN);
		lazyFindById.put("inscricaoEstadualExpedidor", FetchMode.JOIN);
		lazyFindById.put("inscricaoEstadualDestinatario", FetchMode.JOIN);
		lazyFindById.put("inscricaoEstadualTomador", FetchMode.JOIN);
		lazyFindById.put("usuarioInclusao", FetchMode.JOIN);
		lazyFindById.put("usuarioCancelamento", FetchMode.JOIN);
	}
	
	private DetachedCriteria createCriteriaForPreAwb(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "a");
		dc.add(Restrictions.eq("a.tpStatusAwb", "P"));
		
		Long idAeroportoOrigem = criteria.getLong("aeroportoOrigem.idAeroporto");
		if(idAeroportoOrigem != null) {
			dc.add(Restrictions.eq("ao.idAeroporto", idAeroportoOrigem));
		}

		Long idAeroportoDestino = criteria.getLong("aeroportoDestino.idAeroporto");
		if(idAeroportoDestino != null) {
			dc.add(Restrictions.eq("ad.idAeroporto", idAeroportoDestino));
		}

		Long idFilialOrigem = criteria.getLong("filialOrigem.idFilial");
		if(idFilialOrigem != null) {
			dc.add(Restrictions.eq("fo.idFilial", idFilialOrigem));
		}

		Long idCiaFilialMercurio = criteria.getLong("ciaFilialMercurio.idCiaFilialMercurio");
		if(idCiaFilialMercurio != null) {
			dc.add(Restrictions.eq("cfm.idCiaFilialMercurio", idCiaFilialMercurio));
		}

		Long idAwb = criteria.getLong("idAwb");
		if(idAwb != null) {
			dc.add(Restrictions.eq("a.idAwb", idAwb));
		}

		dc.createAlias("aeroportoByIdAeroportoOrigem", "ao");
		dc.createAlias("aeroportoByIdAeroportoDestino", "ad");
		dc.createAlias("filialByIdFilialOrigem", "fo");
		dc.createAlias("ciaFilialMercurio", "cfm");
		dc.createAlias("cfm.empresa", "e");
		dc.createAlias("e.pessoa", "p");

		return dc;
	}

	private ProjectionList createProjectionsPreAwb() {
		ProjectionList projections = Projections.projectionList()
		.add(Projections.property("a.idAwb"), "idAwb")
		.add(Projections.property("a.idAwb"), "awb_idAwb")
		.add(Projections.property("a.dhDigitacao.value"), "awb_dhDigitacao")
		.add(Projections.property("a.vlFrete"), "awb_vlFrete")
		.add(Projections.property("ao.sgAeroporto"), "aeroportoOrigem_sgAeroporto")
		.add(Projections.property("ad.sgAeroporto"), "aeroportoDestino_sgAeroporto")
		.add(Projections.property("p.nmFantasia"), "ciaAerea_nmFantasia")
		.add(Projections.property("p.nmPessoa"), "ciaAerea_nmPessoa");
		return projections;
	}

	private SqlTemplate createCriteriaPaginated(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
			.append(getPersistentClass().getName()).append(" as a ")
			.append(" join a.ciaFilialMercurio cfm ")
			.append(" join cfm.empresa e ")
			.append(" join e.pessoa p ")
			.append(" join a.aeroportoByIdAeroportoOrigem ao ")
			.append(" join a.aeroportoByIdAeroportoDestino ad ")
			.append(" left outer join a.inscricaoEstadualExpedidor iee ")
			.append(" left outer join a.inscricaoEstadualDestinatario ied ")
			.append(" left outer join a.inscricaoEstadualTomador iet ")
			.append(" left outer join a.itensFaturaCiaAerea  ifca ")
			.append(" left outer join ifca.faturaCiaAerea fca ")
			.append(" join a.moeda m ");

		SqlTemplate hqlTemplate = new SqlTemplate();
		hqlTemplate.addFrom(hql.toString());
		
		Long idEmpresa = criteria.getLong("ciaFilialMercurio.empresa.idEmpresa");
		if (idEmpresa != null) {
			hqlTemplate.addCriteria("e.idEmpresa", "=", idEmpresa);
		}		
		
		String tpAwb = criteria.getString("tpAwb");
		if (StringUtils.isNotBlank(tpAwb)) {
			Long nrAwb = criteria.getLong("nrAwb");
			if (nrAwb != null) {
				setCriteriaAwb(tpAwb, nrAwb,  hqlTemplate);
			}
		}

		String tpSituacaoAwb = criteria.getString("tpSituacaoAwb");
		if (StringUtils.isNotEmpty(tpSituacaoAwb)) {
			hqlTemplate.addCriteria("a.tpStatusAwb", "=", tpSituacaoAwb);
		}
		
		Long idAeroportoOrigem = criteria.getLong("aeroportoByIdAeroportoOrigem.idAeroporto");
		if (idAeroportoOrigem != null) {
			hqlTemplate.addCriteria("ao.idAeroporto", "=", idAeroportoOrigem);
		}
		Long idAeroportoDestino = criteria.getLong("aeroportoByIdAeroportoDestino.idAeroporto");
		if (idAeroportoDestino != null) {
			hqlTemplate.addCriteria("ad.idAeroporto", "=", idAeroportoDestino);
		}

		YearMonthDay dataInicial = criteria.getYearMonthDay("dataInicial");
		if (dataInicial != null) {
			hqlTemplate.addCriteria("trunc(a.dhEmissao.value)", ">=", dataInicial);
		}
		YearMonthDay dataFinal = criteria.getYearMonthDay("dataFinal");
		if (dataFinal != null) {
			hqlTemplate.addCriteria("trunc(a.dhEmissao.value)", "<=", dataFinal);
		}
		
		Boolean blConferido = criteria.getBoolean("blConferido");
		if(blConferido != null){
			hqlTemplate = setBlConferido(hqlTemplate, blConferido);
		}
		
		String cdLiberacaoAWBCompl = criteria.getString("cdLiberacaoAWBCompl");
		if (StringUtils.isNotBlank(cdLiberacaoAWBCompl)) {
			hqlTemplate.addCustomCriteria(subQueryCodigoLiberacaoAwbCompl(), cdLiberacaoAWBCompl);
		}
		
		return hqlTemplate;
	}
	
	public String subQueryCodigoLiberacaoAwbCompl() {
		StringBuilder hql = new StringBuilder();
		hql.append("a.idAwb in (");
		hql.append("SELECT awb.idAwb ");
		hql.append("	FROM " + LiberaAWBComplementar.class.getSimpleName() + " lac, " + Awb.class.getSimpleName() + " awb");
		hql.append("	WHERE ");
		hql.append(" 		awb.idAwb in (lac.awbOriginal.idAwb, lac.awbComplementar.idAwb) ");
		hql.append("	AND lac.dsSenha = ?)");
		
		return hql.toString();
	}
	
	private void setCriteriaAwb(String tpStatusAwb, Long nrAwb, SqlTemplate hqlTemplate) {
		if(ConstantesAwb.TP_STATUS_AWB.equalsIgnoreCase(tpStatusAwb) 
				|| ConstantesAwb.TP_STATUS_CANCELADO.equalsIgnoreCase(tpStatusAwb) && nrAwb != null){
			Awb awb = AwbUtils.splitNrAwb(nrAwb.toString());
			hqlTemplate.addCriteria("a.nrAwb", "=", awb.getNrAwb());
			hqlTemplate.addCriteria("a.dsSerie", "=", awb.getDsSerie());
			hqlTemplate.addCriteria("a.dvAwb", "=", awb.getDvAwb());
		}
		if(ConstantesAwb.TP_STATUS_PRE_AWB.equalsIgnoreCase(tpStatusAwb) && nrAwb != null){
			hqlTemplate.addCriteria("a.idAwb", "=", nrAwb);
		}
	}
	
	private SqlTemplate setBlConferido(SqlTemplate hqlTemplate,
			Boolean blConferido) {
		if(blConferido){
			hqlTemplate.addCriteria("a.blConferido", "=", blConferido);
		}else{
			hqlTemplate.addCustomCriteria(" ( a.blConferido = 'N' OR a.blConferido is null) ");
		}
		return hqlTemplate;
	}

	public List<Awb> findBySerieByNrAwbByDvAwbCiaAerea(String dsSerie, Long nrAwb, Integer dvAwb, Long idCiaAerea) {
		StringBuilder projection = new StringBuilder()
		.append(" a ");
		
		StringBuilder hql = new StringBuilder()
		.append(getPersistentClass().getName()).append(" as a ")
		.append(" join a.ciaFilialMercurio cfm ")
		.append(" join cfm.empresa emp ");

		SqlTemplate hqlTemplate = new SqlTemplate();
		hqlTemplate.addFrom(hql.toString());
		hqlTemplate.addRequiredCriteria("emp.idEmpresa", "=", idCiaAerea);
		hqlTemplate.addCriteria("a.dsSerie", "=", dsSerie);
		hqlTemplate.addCriteria("a.nrAwb", "=", nrAwb);
		hqlTemplate.addCriteria("a.dvAwb", "=", dvAwb);
	
		hqlTemplate.addProjection(projection.toString());

		return getAdsmHibernateTemplate().find(hqlTemplate.toString(), hqlTemplate.getCriteria());
	}
	
	public boolean findCtosAwbSemModalAereoEComClienteTomadorNaoLiberado(Awb awb){
		boolean hasCtos = false;
		String tpModal = "A";
		
		List<CtoAwb> ctoAwbs = awb.getCtoAwbs();
		if (ctoAwbs != null && !ctoAwbs.isEmpty()) {
			for (Iterator iter = ctoAwbs.iterator(); iter.hasNext();) {
				CtoAwb ctoAwb = (CtoAwb) iter.next();
				StringBuilder hql = new StringBuilder();
				
				hql.append("  FROM ");
				hql.append(DevedorDocServ.class.getName());
				hql.append(" as dev ");		
				hql.append("  JOIN dev.cliente cli ");
				hql.append("  JOIN dev.doctoServico doc ");
				hql.append("  JOIN doc.servico serv ");
				hql.append(" WHERE NVL(cli.blPermiteEmbarqueRodoNoAereo, 'N') = 'N' ");
				hql.append("   AND doc.idDoctoServico = ? ");
				hql.append("   AND serv.tpModal <> ? ");
				
				Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(
						hql.toString(), new Object[]{ctoAwb.getConhecimento().getIdDoctoServico(), tpModal});
				
				if(IntegerUtils.gtZero(rows)){
					hasCtos = true;
				}
			}
		}
		return hasCtos;
		
	}

	public Awb findPreAwb(Long nrAwb, String tpStatusAwb, Long idFilialOrigem) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select ");
		hql.append(" 	awb  ");

		hql.append(" from ");
		hql.append(		Awb.class.getName()).append(" awb ");
		hql.append(" 	join awb.ciaFilialMercurio cfm ");
		hql.append(" 	join cfm.empresa empresa ");
		hql.append(" 	join empresa.pessoa ");
		
		hql.append(" where ");
		hql.append(" 	awb.idAwb = ? ");
		hql.append(" 	and awb.tpStatusAwb = ? ");
		hql.append(" 	and awb.filialByIdFilialOrigem.idFilial = ? ");
		
		return (Awb)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[] {nrAwb, tpStatusAwb, idFilialOrigem});
	}

	public Awb findByIdDoctoServicoAndFilialOrigem(Long idDoctoServico, Long idFilialSessao) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select ");
		hql.append(" 	awb  ");
		
		hql.append(" from ");
		hql.append(		Awb.class.getName()).append(" awb ");
		hql.append(" 	join awb.ctoAwbs ca ");
		hql.append(" 	join ca.conhecimento c ");
		hql.append(" 	join fetch awb.ciaFilialMercurio cfm ");
		hql.append(" 	join fetch cfm.empresa empresa ");
		hql.append(" 	join fetch empresa.pessoa ");
		
		hql.append(" where ");
		hql.append(" 	c.idDoctoServico = ? ");
		hql.append(" 	and awb.filialByIdFilialOrigem.id = ? ");
		hql.append(" 	and awb.tpStatusAwb != 'C' ");
		
		List<Awb> awbs = getAdsmHibernateTemplate().find(hql.toString(), new Object[] {idDoctoServico, idFilialSessao});
		
		if(CollectionUtils.isNotEmpty(awbs)){
			return awbs.get(0);
		}
		return null;
	}

	public Awb findPreAwbByIdDoctoServicoAndFilialOrigem(Long idDoctoServico, Long idFilialSessao) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select awb ")
		
		.append(" from ")
		.append(		Awb.class.getName()).append(" awb ")
		.append(" 	join awb.ctoAwbs ca ")
		.append(" 	join ca.conhecimento c ")
		.append(" 	join fetch awb.ciaFilialMercurio cfm ")
		.append(" 	join fetch cfm.empresa empresa ")
		.append(" 	join fetch empresa.pessoa ")
		
		.append(" where ")
		.append(" 	c.idDoctoServico = ? ")
		.append(" 	and awb.filialByIdFilialOrigem.id = ? ")
		.append(" 	and awb.tpStatusAwb = 'P' ");
		
		List<Awb> awbs = getAdsmHibernateTemplate().find(hql.toString(), new Object[] {idDoctoServico, idFilialSessao});
		
		if(CollectionUtils.isNotEmpty(awbs)){
			return awbs.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public TypedFlatMap findDadosColetaAwb(Long idAwb, Long idConhecimento) {

		StringBuilder hql = new StringBuilder();
		Map<String, Object> namedParams = new HashMap<String, Object>();
		
		hql.append(" select " );
		hql.append(" 	new map(  " ); 
		hql.append(" 	  	sum(con.vlMercadoria) as vlMercadoria," );
		hql.append(" 	  	sum(con.psReal) as psReal," );
		hql.append(" 	  	sum(con.psAforado) as psAforado," );
		hql.append(" 	  	max(pessoa.nmPessoa) as nmAeroporto," );
		hql.append(" 	  	sum(con.qtVolumes) as qtdVolumes" );
		hql.append(" 	)" );
		
		hql.append(" from ");
		hql.append(		Awb.class.getName()).append(" awb ");
		hql.append(" 	join awb.ctoAwbs ca ");
		hql.append(" 	join ca.conhecimento con ");
		hql.append(" 	join awb.ciaFilialMercurio cfm ");
		hql.append(" 	join cfm.empresa empresa ");
		hql.append(" 	join empresa.pessoa pessoa ");
		
		hql.append(" where ");
		
		if(idAwb != null){
			hql.append(" 	((awb.idAwb = :idAwb ");
			hql.append(" 		and awb.tpStatusAwb = :tpPreAwb ) ");
			hql.append(" 	or (awb.idAwb = :idAwb ");
			hql.append(" 		and awb.tpStatusAwb not in (:tpCancelado, :tpPreAwb)))  ");
			namedParams.put("idAwb", idAwb);
			namedParams.put("tpPreAwb", ConstantesExpedicao.TP_STATUS_PRE_AWB);
		}else if(idConhecimento != null){
			hql.append(" 	con.idDoctoServico = :idConhecimento  ");
			hql.append(" and awb.idAwb = (");
			hql.append("					select max(a.idAwb)");
			hql.append("						from " + CtoAwb.class.getSimpleName() + " cto ");
			hql.append("							inner join cto.awb a ");
			hql.append("							inner join cto.conhecimento c ");
			hql.append("						where c.id = con.id)");
			hql.append("	or con is null");
			
			namedParams.put("idConhecimento", idConhecimento);
		}
		
		hql.append(" 	and con.tpSituacaoConhecimento != :tpCancelado ");
		
		namedParams.put("tpCancelado", ConstantesExpedicao.TP_STATUS_CANCELADO);
		
		List<Map<String, Object>> result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), namedParams);
		
		return new TypedFlatMap(result.get(0));
	}
	
//	Busca AWB do documento de serviço do manifesto
	public Awb findAwbByDoctoServicoAndManifesto(Long idDoctoServico, Long idManifesto) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT awb ");
		sql.append(" FROM ");
		sql.append(Awb.class.getSimpleName() + " awb ");
		sql.append(" inner join fetch awb.manifestoEntregaDocumentos med ");
		sql.append(" inner join fetch awb.aeroportoByIdAeroportoOrigem ao ");
		sql.append(" inner join fetch ao.pessoa p ");
		sql.append(" inner join fetch p.enderecoPessoa ep ");
		sql.append(" inner join fetch med.manifestoEntrega me ");
		sql.append(" inner join fetch med.doctoServico ds ");
		sql.append(" inner join fetch me.manifesto m, ");
		sql.append(Conhecimento.class.getSimpleName() + " c ");
		sql.append("WHERE ");
		sql.append(" 	m.tpStatusManifesto != :tpStatusManifesto ");
		sql.append("AND	med.tpSituacaoDocumento != :tpSituacaoDocumento ");
		sql.append("AND	ds.id = c.id ");
		sql.append("AND	awb.tpStatusAwb != :tpStatusAwb ");
		sql.append("AND c.tpSituacaoConhecimento != :tpSituacaoConhecimento ");
		sql.append("AND c.id = :idConhecimento ");
		sql.append("AND m.idManifesto = :idManifesto ");
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("tpStatusManifesto" ,ConstantesEntrega.DM_MANIFESTO_CANCELADO);
		param.put("tpSituacaoDocumento", ConstantesEntrega.STATUS_DOCUMENTO_CANCELADO);
		param.put("tpStatusAwb", ConstantesExpedicao.TP_STATUS_CANCELADO);
		param.put("tpSituacaoConhecimento", ConstantesExpedicao.TP_STATUS_CANCELADO);
		param.put("idConhecimento", idDoctoServico);
		param.put("idManifesto", idManifesto);
		
		return (Awb)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), param); 
	}

	public Awb findByCiaFilialMercurioAndDsSerieAndNrAwb(Long idCiaFilialMercurio, String dsSerie, Long nrAwb, Integer digito) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT a ");
		sql.append(" FROM " + getPersistentClass().getSimpleName() + " a ");
		sql.append(" JOIN a.ciaFilialMercurio cfm ");
		sql.append(" WHERE ");
		sql.append(" 	 a.dsSerie = :dsSerie");
		sql.append(" AND a.nrAwb = :nrAwb");
		sql.append(" AND a.dvAwb = :digito ");
		sql.append(" AND cfm.idCiaFilialMercurio = :idCiaFilialMercurio ");
		
		TypedFlatMap param = new TypedFlatMap();
		param.put("idCiaFilialMercurio", idCiaFilialMercurio);
		param.put("dsSerie", dsSerie);
		param.put("nrAwb", nrAwb);
		param.put("digito", digito);
		
		return (Awb)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), param);
	}

	public Awb findByDoctoServicoAndTpStatusAwb(Long idDoctoServico, String tpStatusAwb) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT a ");
		sql.append(" FROM " + getPersistentClass().getSimpleName() + " a ");
		sql.append("JOIN a.ctoAwbs ca ");
		sql.append("JOIN a.ciaFilialMercurio cfm ");
		sql.append("JOIN cfm.empresa e ");
		sql.append("JOIN e.pessoa p ");
		sql.append("JOIN ca.conhecimento c ");
		sql.append(" WHERE ");
		sql.append(" 	 c.id = :idConhecimento ");
		sql.append(" and a.tpStatusAwb = :tpStatusAwb");
		
		TypedFlatMap paramValues = new TypedFlatMap();
		paramValues.put("idConhecimento", idDoctoServico);
		paramValues.put("tpStatusAwb", tpStatusAwb);
		
		return (Awb)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), paramValues);
	}
	
	public List<Map<String, Object>> findDadosByChaveAwb(String chave) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("chaveAwb", chave);
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(getSqlDadosAwb(), parametersValues, getConfigureSqlQueryDadosAwb());
	}

	private ConfigureSqlQuery getConfigureSqlQueryDadosAwb() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("serieAwb", Hibernate.STRING);
				sqlQuery.addScalar("numeroAwb", Hibernate.LONG);				
				sqlQuery.addScalar("digitoAwb", Hibernate.INTEGER);
				sqlQuery.addScalar("qtdVolumes", Hibernate.LONG);
				sqlQuery.addScalar("pesoReal", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("pesoCubado", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("ciaAerea", Hibernate.STRING);
				sqlQuery.addScalar("sgAeroportoOrigem", Hibernate.STRING);
				sqlQuery.addScalar("nmAeroportoOrigem", Hibernate.STRING);
				sqlQuery.addScalar("sgAeroportoDestino", Hibernate.STRING);
				sqlQuery.addScalar("nmAeroportoDestino", Hibernate.STRING);
				sqlQuery.addScalar("sgFilialOrigem", Hibernate.STRING);
				sqlQuery.addScalar("nmFilialOrigem", Hibernate.STRING);
				sqlQuery.addScalar("sgFilialDestino", Hibernate.STRING);
				sqlQuery.addScalar("nmFilialDestino", Hibernate.STRING);
				sqlQuery.addScalar("freteAWB", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("chaveAwb", Hibernate.STRING);
				sqlQuery.addScalar("idAwb", Hibernate.LONG);
				sqlQuery.addScalar("blConferido", Hibernate.STRING);
				sqlQuery.addScalar("freteCtos", Hibernate.BIG_DECIMAL);
			}
		};
		
		return csq;
	}

	private String getSqlDadosAwb() {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT ");
		sql.append(" AWB.DS_SERIE 				as serieAwb, ")
		.append(" AWB.NR_AWB 					as numeroAwb, ")
		.append(" AWB.DV_AWB 					as digitoAwb, ")
		.append(" AWB.QT_VOLUMES 				as qtdVolumes, ")
		.append(" AWB.PS_TOTAL 					as pesoReal, ")
		.append(" AWB.PS_CUBADO 				as pesoCubado, ")
		.append(" EMP.SG_EMPRESA 				as ciaAerea, ")
		.append(" AOR.SG_AEROPORTO 				as sgAeroportoOrigem, ")
		.append(" PAOR.NM_FANTASIA 				as nmAeroportoOrigem, ")
		.append(" AD.SG_AEROPORTO 				as sgAeroportoDestino, ")
		.append(" PAD.NM_FANTASIA 				as nmAeroportoDestino, ")
		.append(" FO.SG_FILIAL 					as sgFilialOrigem, ")
		.append(" PFO.NM_FANTASIA 				as nmFilialOrigem, ")
		.append(" FD.SG_FILIAL 					as sgFilialDestino, ")
		.append(" PFD.NM_FANTASIA 				as nmFilialDestino, ")
		.append(" AWB.VL_FRETE 					as freteAWB, ")
		.append(" AWB.NR_CHAVE      			as chaveAwb, ")
		.append(" AWB.ID_AWB      				as idAwb, ")
		.append(" NVL(AWB.BL_CONFERIDO, 'N')  	as blConferido, ")
		.append(" (SELECT SUM(VL_FRETE_LIQUIDO) ")
			.append(" FROM DOCTO_SERVICO, ")
			.append(" CTO_AWB ")
			.append(" WHERE ")
			.append(" DOCTO_SERVICO.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO ")
			.append(" AND CTO_AWB.ID_AWB = AWB.ID_AWB) AS freteCtos ")
		
		.append(" FROM ")
		.append(" AWB, ")
		.append(" CIA_FILIAL_MERCURIO CFM, ")
		.append(" EMPRESA EMP, ")
		.append(" AEROPORTO AOR, ")
		.append(" PESSOA PAOR, ")
		.append(" AEROPORTO AD, ")
		.append(" PESSOA PAD, ")
		.append(" FILIAL FO, ")
		.append(" PESSOA PFO, ")
		.append(" FILIAL FD, ")
		.append(" PESSOA PFD ")
		
		.append(" WHERE ")
		.append(" AWB.ID_CIA_FILIAL_MERCURIO = CFM.ID_CIA_FILIAL_MERCURIO ")
		.append(" AND CFM.ID_EMPRESA = EMP.ID_EMPRESA ")
		.append(" AND AWB.ID_AEROPORTO_ORIGEM = AOR.ID_AEROPORTO ")
		.append(" AND AOR.ID_AEROPORTO = PAOR.ID_PESSOA ")
		.append(" AND AWB.ID_AEROPORTO_DESTINO = AD.ID_AEROPORTO ")
		.append(" AND AD.ID_AEROPORTO = PAD.ID_PESSOA ")
		.append(" AND AWB.ID_FILIAL_ORIGEM = FO.ID_FILIAL ")
		.append(" AND FO.ID_FILIAL = PFO.ID_PESSOA ")
		.append(" AND AWB.ID_FILIAL_DESTINO = FD.ID_FILIAL ")
		.append(" AND FD.ID_FILIAL = PFD.ID_PESSOA ")
		.append(" AND AWB.TP_STATUS_AWB = 'E' ")
		.append(" AND AWB.NR_CHAVE = :chaveAwb ");

		return sql.toString();
	}

	public String findAwbLogCargaAwb(String chaveAwb) {
		Session session = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select ds_mensagem ");
		sql.append("from log_carga_awb lca ");
		sql.append("where  lca.nr_chave = :chaveAwb ");

		final ConfigureSqlQuery csq1 = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ds_mensagem", Hibernate.STRING);
			}
		};
		
		SQLQuery queryConsulta = session.createSQLQuery(sql.toString());
		queryConsulta.setParameter("chaveAwb", chaveAwb);
		csq1.configQuery(queryConsulta);
				
		List<String>  lista = queryConsulta.list();
		if (lista.isEmpty()){
			return "";
		}else{
			return lista.get(0);
		}
	}

	public void updateConferenciaAwb(final Long idAwb) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws SQLException {

				Query query = null;
				String hql = "UPDATE " + getPersistentClass().getName() + " awb \n" +
							 "SET awb.blConferido = 'S' \n" +
							 "WHERE awb.idAwb = :id \n"; 

				query = session.createQuery(hql);
				query.setParameter("id", idAwb);
				query.executeUpdate();

				return null;
		}
		});
	}

	public Map<String, Object> findChaveJaUsadaEmAWB(String nrChave) {
		
		StringBuilder hql = new StringBuilder();
		   
		hql.append("SELECT new map ( awb.nrAwb as numero, ")
		.append(" p.nmFantasia  as nmCiaAerea, e.sgEmpresa as sgEmpresa, awb.dsSerie as dsSerie, awb.dvAwb as dvAwb ) ")
		.append("  FROM ")
		.append(getPersistentClass().getName())
		.append(" as awb ")
		.append(" join awb.ciaFilialMercurio cfm ")
		.append(" join cfm.empresa e ")
		.append(" join e.pessoa p ")
		.append(" WHERE awb.nrChave = ? ")
		.append(" and awb.tpStatusAwb <> 'C' ");

		List<Map<String, Object>> lista = getAdsmHibernateTemplate().find(hql.toString(), nrChave);
		
		if(lista.isEmpty()){
			return new HashMap<String, Object>();
		}
		
		return lista.get(0);
	}	
	
	public Awb findUltimoAwbByDoctoServico(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder();
		hql.append("select awbs")
		.append(" from " + Awb.class.getSimpleName() + " awbs")
		.append(" inner join fetch awbs.ciaFilialMercurio cfm ")
		.append(" inner join fetch cfm.empresa e ")
		.append(" inner join fetch e.pessoa p ")
		.append(" where awbs.idAwb in (")
		.append("					select max(a.idAwb)")
		.append("						from " + CtoAwb.class.getSimpleName() + " cto ")
		.append("							inner join cto.awb a ")
		.append("							inner join cto.conhecimento c ")
		.append("						where c.id = :idConhecimento)");
		
		TypedFlatMap paramValues = new TypedFlatMap();
		paramValues.put("idConhecimento", idDoctoServico);
		
		return (Awb)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), paramValues);		
	}

	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id_awb    AS idAwb, ");
		sql.append("  tp_status_awb  AS tpStatusAwb, ");
		sql.append("  nr_awb         AS nrAwb, ");
		sql.append("  ds_serie       AS dsSerie, ");
		sql.append("  dv_awb         AS dvAwb, ");
		sql.append("  dh_emissao     AS dhEmissao, ");
		sql.append("  emp.sg_empresa AS sgEmpresa ");
		sql.append("FROM awb ");
		sql.append("INNER JOIN CIA_FILIAL_MERCURIO cfm ON cfm.ID_CIA_FILIAL_MERCURIO = awb.ID_CIA_FILIAL_MERCURIO ");
		sql.append("INNER JOIN empresa emp ON emp.id_empresa = cfm.id_empresa ");
		sql.append("WHERE 1 = 1 ");

		if(filter.get("tpStatusAwb") != null) {
			sql.append(" and tp_status_awb = :tpStatusAwb ");
		}
		if(filter.get("idAwb") != null) {
			sql.append(" and id_awb = :idAwb ");
		}
		if(filter.get("sgEmpresa") != null) {
			sql.append(" and emp.sg_empresa = :sgEmpresa ");
		}
		if(filter.get("dsSerie") != null) {
			sql.append(" and ds_serie = :dsSerie ");
		}
		if(filter.get("nrAwb") != null) {
			sql.append(" and nr_awb = :nrAwb ");
		}
		if(filter.get("dvAwb") != null) {
			sql.append(" and dv_awb = :dvAwb ");
		}
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idAwb", Hibernate.LONG);
				sqlQuery.addScalar("tpStatusAwb", Hibernate.STRING);
				sqlQuery.addScalar("nrAwb", Hibernate.STRING);
				sqlQuery.addScalar("dsSerie", Hibernate.STRING);
				sqlQuery.addScalar("dvAwb", Hibernate.SHORT);
				sqlQuery.addScalar("sgEmpresa", Hibernate.STRING);
	  			sqlQuery.addScalar("dhEmissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
			}
		};
		return new ResponseSuggest(sql.toString(), filter, csq);
	}

	public ResultSetPage findPaginatedAwbsByIdConhecimento(Long idConhecimento) {
		StringBuilder hql = new StringBuilder()
		.append(" new map( ")
		.append(" a.idAwb as idAwb, ")
		.append(" e.idEmpresa as ciaFilialMercurio_empresa_idEmpresa, ")
		.append(" e.sgEmpresa as ciaFilialMercurio_empresa_sgEmpresa, ")
		.append(" p.nmPessoa as ciaFilialMercurio_empresa_pessoa_nmPessoa, ")
		.append(" a.dsSerie as dsSerieAwb, ")
		.append(" a.nrAwb as nrAwb, ")
		.append(" a.dvAwb as dvAwb, ")
		.append(" a.tpStatusAwb as tpStatusAwb, ")
		.append(" a.tpMotivoCancelamento as tpMotivoCancelamento, ")
		.append(" a.dsMotivoCancelamento as dsMotivoCancelamento, ")
		
		.append(" aws.idAwb as idAwbSubs, ")
		.append(" es.idEmpresa as ciaFilialMercurioSubs_empresa_idEmpresa, ")
		.append(" es.sgEmpresa as ciaFilialMercurioSubs_empresa_sgEmpresa, ")
		.append(" ps.nmPessoa as ciaFilialMercurioSubs_empresa_pessoa_nmPessoa, ")
		.append(" aws.dsSerie as dsSerieAwbSubs, ")
		.append(" aws.nrAwb as nrAwbSubs, ")
		.append(" aws.dvAwb as dvAwbSubs, ")
		.append(" aws.tpStatusAwb as tpStatusAwbSubs, ")
		
		.append(" ao.sgAeroporto as sgAeroportoOrigem, ")
		.append(" ad.sgAeroporto as sgAeroportoDestino, ")
		.append(" pao.nmPessoa as nmAeroportoOrigem, ")
		.append(" pad.nmPessoa as nmAeroportoDestino, ")
		.append(" a.dhEmissao as dhEmissao, ")
		.append(" a.dhDigitacao as dhDigitacao, ")
		.append(" a.vlFrete as vlFrete, ")
		.append(" m.sgMoeda as moeda_sgMoeda, ")
		.append(" m.dsSimbolo as moeda_dsSimbolo, ")
		.append(" a.dsVooPrevisto as dsVooPrevisto,")
		.append(" fca.nrFaturaCiaAerea as nrFaturaAwb, ")
		.append(" a.tpAwb as tpAwb ")
		.append(" ) ");
		
		StringBuilder hqlfrom = new StringBuilder()
		.append(getPersistentClass().getName()).append(" as a ")
		.append(" join a.ctoAwbs ca ")
		.append(" left join a.awbSubstituido aws ")
		.append(" left join aws.ciaFilialMercurio cfms ")
		.append(" left join cfms.empresa es ")
		.append(" left join es.pessoa ps ")
		
		.append(" join ca.conhecimento con ")
		.append(" join a.ciaFilialMercurio cfm ")
		.append(" join cfm.empresa e ")
		.append(" join e.pessoa p ")
		.append(" join a.aeroportoByIdAeroportoOrigem ao ")
		.append(" join a.aeroportoByIdAeroportoDestino ad ")
		.append(" join ao.pessoa pao ")
		.append(" join ad.pessoa pad ")
		.append(" left outer join a.inscricaoEstadualExpedidor iee ")
		.append(" left outer join a.inscricaoEstadualDestinatario ied ")
		.append(" left outer join a.inscricaoEstadualTomador iet ")
		.append(" left outer join a.itensFaturaCiaAerea ifca ")
		.append(" left outer join ifca.faturaCiaAerea fca ")
		.append(" join a.moeda m ");

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(hqlfrom.toString());
	
		sql.addCriteria("con.idDoctoServico", "=", idConhecimento);
		
		sql.addProjection(hql.toString());

		sql.addOrderBy("a.idAwb", "asc");
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(), 1, 1000, sql.getCriteria());
		List result = rsp.getList();
		result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
		rsp.setList(result);
		
		return rsp;
	}
	
	public Awb findAWBComplementadoByIdAwbComplementar(Long idAwbComplementar) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> parameterValues = new HashMap<String, Object>();
		parameterValues.put("idAwbComplementar", idAwbComplementar);
		
		hql.append("SELECT awbOrig ");
		hql.append(" FROM " + Awb.class.getSimpleName() + " awbOrig ");
		hql.append(" INNER JOIN FETCH awbOrig.liberaAWBComplementars lac");
		hql.append(" INNER JOIN FETCH lac.awbComplementar awbCompl ");
		hql.append("WHERE ");
		hql.append(" awbCompl.idAwb = :idAwbComplementar ");
		
		return (Awb)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), parameterValues);
	}

	public List<Map<String, Object>> findParcelasForReport(Long idAwb, int mod) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * ");
		sql.append("FROM ( ");
		sql.append("SELECT MOD(ROW_NUMBER() OVER (ORDER BY PA.ID_PARCELA_AWB), 4) AS LINHA, ");
		sql.append("  PA.DS_PARCELA_AWB                                           AS DS_PARCELA_AWB, ");
		sql.append("  PA.VL_PARCELA_AWB                                           AS VL_PARCELA_AWB ");
		sql.append("FROM PARCELA_AWB PA ");
		sql.append("WHERE PA.ID_AWB = :idAwb ");
		sql.append("ORDER BY PA.ID_PARCELA_AWB");
		sql.append("  ) ");
		sql.append("WHERE LINHA = :linha");

		@SuppressWarnings("unchecked")
		Map<String, Object> params = new TypedFlatMap();
        
       	params.put("idAwb", idAwb);
       	params.put("linha", mod);
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), params, getConfigureSqlQueryDadosParcela());
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryDadosParcela() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("LINHA", Hibernate.STRING);
				sqlQuery.addScalar("DS_PARCELA_AWB", Hibernate.STRING);				
				sqlQuery.addScalar("VL_PARCELA_AWB", Hibernate.BIG_DECIMAL);
			}
		};
		
		return csq;
	}
	
	public void updateCancelarAwb(final String chaveAwb, final String justificativa, final Long idUsuario) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws SQLException {

				Query query = null;
				String hql = "UPDATE " + getPersistentClass().getName() + " awb " +
							 "SET awb.tpStatusAwb = 'C' , awb.dsMotivoCancelamento = :justificativa, awb.usuarioCancelamento.idUsuario = :idUsuario " +
							 "WHERE awb.nrChave = :nrChave \n"; 

				query = session.createQuery(hql);
				query.setParameter("justificativa", justificativa);
				query.setParameter("nrChave", chaveAwb);
				query.setParameter("idUsuario", idUsuario);
				query.executeUpdate();

				return null;
			}
		});
	}

	public Empresa findResponsavelAwbEmpresaParceira(Long idAwb) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> parameterValues = new HashMap<String, Object>();
		parameterValues.put("idAwb", idAwb);
		
		hql.append("SELECT emp ");
		hql.append(" FROM " + Awb.class.getSimpleName() + " awb, ");
		hql.append( Empresa.class.getSimpleName() + " emp ");
		hql.append(" INNER JOIN  awb.clienteByIdClienteDestinatario cli ");
		hql.append(" WHERE ");
		hql.append(" cli.idCliente = emp.idEmpresa ");
		hql.append(" AND emp.tpEmpresa = 'P' ");
		hql.append(" AND awb.idAwb = :idAwb ");
		
		return (Empresa)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), parameterValues);
		
	}
	
	private String getSqlMonitoramentoNetworkAereoByCiaAerea(TypedFlatMap criteria){
		StringBuilder sql = new StringBuilder();
		sql
		.append(" SELECT ")
		.append(" 	/*+ opt_param('_OPTIMIZER_USE_FEEDBACK','FALSE') */  ")
		.append("   id_cia_aerea, ")
		.append("	nm_cia_aerea,")
		.append("   sg_empresa, ")
		.append("   COUNT(DISTINCT id_pre_awb)              AS qt_pre_awb, ")
		.append("   COUNT(DISTINCT id_awb)                  AS qt_awb, ")
		.append("   MAX(eea)                                AS eea, ")
		.append("   SUM(DECODE(tp_localizacao, 'AN', 1, 0)) AS qt_aguardando_entrega, ")
		.append("   SUM(DECODE(tp_localizacao, 'AE', 1, 0)) AS qt_embarque_aereo, ")
		.append("   MAX(eva)                                AS eva, ")
		.append("   SUM(DECODE(tp_localizacao, 'EV', 1, 0)) AS qt_viagem_aerea, ")
		.append("   SUM(DECODE(tp_localizacao, 'FS', 1, 0)) AS qt_liberacao_fiscal, ")
		.append("   SUM(DECODE(tp_localizacao, 'DR', 1, DECODE(tp_localizacao, 'RP', 1, 0))) AS qt_disponivel, ")
		.append("   MAX(tt_total)                           AS tt, ")
		.append("   MAX(dpr)                                AS dpr, ")
		.append("   SUM(DECODE(tp_localizacao, 'RE', 1, 0)) AS qt_retirada ")		
		.append("from ( ")
		.append(this.getSqlMonitoriamentoAwb(criteria))
		.append(") awb_pre_ocorrencia_tempos ")
		.append("group by nm_cia_aerea, sg_empresa, id_cia_aerea ")
		.append("order by nm_cia_aerea ");

		
		return sql.toString();
	}	

	public String getSqlMonitoriamentoAwb(TypedFlatMap criteria) {
		StringBuilder sql  = new StringBuilder();
		StringBuilder sql1 = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		
		sql1
		.append(" SELECT ")
		.append("     id_cia_aerea, ")
		.append("     nm_cia_aerea, ")
		.append("     sg_empresa, ")
		.append("     tp_localizacao, ")
		.append("     dh_ocorrencia_situacao, ")
		.append("     tp_status_awb, ")
		.append("     id_pre_awb, ")
		.append("     id_awb, ")
		.append("     nr_awb, ")
		.append("     nr_pre_awb, ")
		.append("     sg_aeroporto_origem, ")
		.append("     sg_aeroporto_destino, ")
		.append("     ps_total, ")
		.append("     vl_awb, ")
		.append("     dh_AN, ")
		.append("     dh_AE, ")
		.append("     CASE WHEN dh_AE > dh_EV THEN NULL ELSE dh_EV END as dh_EV, ")
		.append("     dh_FS, ")
		.append("     dh_LF, ")
		.append("     dh_DR, ")
		.append("     dh_RP, ")
		.append("     dh_RE, ")
		.append("     dh_FV, ")
		.append("     ( ")
		.append("     CASE ")
		.append("       WHEN dh_AN - NVL(dh_AE, sysdate) <= hr_tempo_entrega ")
		.append("       THEN 1 ")
		.append("       WHEN dh_AN - NVL(dh_AE, sysdate) <= (hr_tempo_entrega * 1.5) ")
		.append("       THEN 2 ")
		.append("       ELSE 3 ")
		.append("     END) AS eea, ")
		.append("     ( ")
		.append("     CASE ")
		.append("       WHEN dh_AE - NVL(dh_FS, NVL(dh_LF, NVL(dh_DR, sysdate))) <= hr_tempo_transferencia ")
		.append("       THEN 1 ")
		.append("       WHEN dh_AE - NVL(dh_FS, NVL(dh_LF, NVL(dh_DR, sysdate))) <= (hr_tempo_transferencia * 1.5) ")
		.append("       THEN 2 ")
		.append("       ELSE 3 ")
		.append("     END) AS eva, ")
		.append("     ( ")
		.append("     CASE ")
		.append("       WHEN NVL(dh_DR, sysdate) - sysdate <= hr_tempo_coleta ")
		.append("       THEN 1 ")
		.append("       WHEN NVL(dh_DR, sysdate) - sysdate <= (hr_tempo_coleta * 1.5) ")
		.append("       THEN 2 ")
		.append("       ELSE 3 ")
		.append("     END) AS dpr, ")
		.append("     (NVL(dh_AE, sysdate) - dh_AN) * 24                         	AS tt_eea, ")
		.append("     (NVL(dh_FV, NVL(dh_FS, NVL(dh_LF, NVL(dh_DR, sysdate)))) - dh_AE) * 24 AS tt_eva, ")
		.append("	  (NVL(dh_LF, sysdate) - dh_FS) * 24 							AS tt_sefaz, ")
		.append("     ((sysdate - NVL(dh_AN, dh_digitacao))) * 24                   AS tt_total ")
		.append("   FROM ")
		.append("     (SELECT ")
		.append("       id_cia_aerea, ")
		.append("       nm_cia_aerea, ")
		.append("       sg_empresa, ")
		.append("       tp_localizacao, ")
		.append("       dh_ocorrencia_situacao, ")
		.append("       tp_status_awb, ")
		.append("       id_pre_awb, ")
		.append("       id_awb, ")
		.append("       dh_digitacao, ")
		.append("       nr_awb, ")
		.append("       nr_pre_awb, ")
		.append("       vl_awb, ")
		.append("       ps_total, ")
		.append("       (SELECT MAX(cast(dh_ocorrencia as date)) ")
		.append("       FROM awb_ocorrencia ao ")
		.append("       WHERE ao.tp_localizacao = 'AN' ")
		.append("       AND ao.id_awb           = awb_pre.id_pre_awb ")
		.append("       ) AS dh_AN, ")
		.append("       (SELECT MAX(cast(dh_ocorrencia as date)) ")
		.append("       FROM awb_ocorrencia ao ")
		.append("       WHERE ao.tp_localizacao = 'AE' ")
		.append("       AND ao.id_awb           in(awb_pre.id_pre_awb, awb_pre.id_awb) ")
		.append("       ) AS dh_AE, ")
		.append("       (SELECT MAX(cast(dh_ocorrencia as date)) ")
		.append("       FROM awb_ocorrencia ao ")
		.append("       WHERE ao.tp_localizacao = 'EV' ")
		.append("       AND ao.id_awb           = awb_pre.id_awb ")
		.append("       ) AS dh_EV, ")
		.append("       (SELECT MAX(cast(dh_ocorrencia as date)) ")
		.append("       FROM awb_ocorrencia ao ")
		.append("       WHERE ao.tp_localizacao = 'FS' ")
		.append("       AND ao.id_awb           = awb_pre.id_awb ")
		.append("       ) AS dh_FS, ")
		.append("       (SELECT MAX(cast(dh_ocorrencia as date)) ")
		.append("       FROM awb_ocorrencia ao ")
		.append("       WHERE ao.tp_localizacao = 'LF' ")
		.append("       AND ao.id_awb           = awb_pre.id_awb ")
		.append("       ) AS dh_LF, ")
		.append("       (SELECT MAX(cast(dh_ocorrencia as date)) ")
		.append("       FROM awb_ocorrencia ao ")
		.append("       WHERE ao.tp_localizacao = 'DR' ")
		.append("       AND ao.id_awb           = awb_pre.id_awb ")
		.append("       ) AS dh_DR, ")
		.append("       (SELECT MAX(cast(dh_ocorrencia as date)) ")
		.append("       FROM awb_ocorrencia ao ")
		.append("       WHERE ao.tp_localizacao = 'RP' ")
		.append("       AND ao.id_awb           = awb_pre.id_awb ")
		.append("       ) AS dh_RP, ")
		.append("       (SELECT MAX(cast(dh_ocorrencia as date)) ")
		.append("       FROM awb_ocorrencia ao ")
		.append("       WHERE ao.tp_localizacao = 'RE' ")
		.append("       AND ao.id_awb           = awb_pre.id_awb ")
		.append("       ) AS dh_RE, ")
		.append("       (SELECT MAX(cast(dh_ocorrencia as date)) ")
		.append("       FROM awb_ocorrencia ao ")
		.append("       WHERE ao.tp_localizacao = 'FV' ")
		.append("       AND ao.id_awb           = awb_pre.id_awb ")
		.append("       ) AS dh_FV, ")
		.append("       hr_tempo_entrega, ")
		.append("       hr_tempo_coleta, ")
		.append("       hr_tempo_transferencia, ")
		.append("       sg_aeroporto_origem, ")
		.append("       sg_aeroporto_destino ")
		.append("     FROM ")
		.append("       (SELECT e.id_empresa AS id_cia_aerea, ")
		.append("         p.nm_fantasia AS nm_cia_aerea, ")
		.append("         e.sg_empresa AS sg_empresa, ")
		.append("         a.tp_localizacao, ")
		.append("         a.dh_ocorrencia_situacao, ")
		.append("         a.tp_status_awb, ")
		.append("         pre.id_awb AS id_pre_awb, ")
		.append("         a.id_awb, ")
		.append("         a.ds_serie ")
		.append("         || '.' ")
		.append("         || a.nr_awb ")
		.append("         || '-' ")
		.append("         || a.dv_awb AS nr_awb, ")
		.append("         pre.id_awb  AS nr_pre_awb, ")
		.append("         pre.dh_digitacao, ")
		.append("         NVL(a.vl_frete, pre.vl_frete)                               AS vl_awb, ")
		.append("         NVL(a.ps_total, pre.ps_total)                               AS ps_total, ")
		.append("         NVL(mo_pre.hr_tempo_entrega, mo_awb.hr_tempo_entrega)             AS hr_tempo_entrega, ")
		.append("         NVL(mo_pre.hr_tempo_coleta, mo_awb.hr_tempo_coleta)               AS hr_tempo_coleta, ")
		.append("         NVL(mo_pre.hr_tempo_transferencia, mo_awb.hr_tempo_transferencia) AS hr_tempo_transferencia, ")
		.append("         ao.sg_aeroporto                                             AS sg_aeroporto_origem, ")
		.append("         ad.sg_aeroporto                                             AS sg_aeroporto_destino ")
		.append("       FROM awb a, ")
		.append("         cia_filial_mercurio cfm, ")
		.append("         empresa e, ")
		.append("         pessoa p, ")
		.append("         awb pre, ")
		.append("         monitoramento_network_aereo mo_awb, ")
		.append("         monitoramento_network_aereo mo_pre, ")
		.append("         aeroporto ao, ")
		.append("         aeroporto ad  ")
		.append("       WHERE a.id_cia_filial_mercurio                            = cfm.id_cia_filial_mercurio ")
		.append("       AND cfm.id_empresa                                        = e.id_empresa ")
		.append("       AND e.id_empresa                                          = p.id_pessoa ")
		.append("       AND a.tp_status_awb                                       = 'E' ")
		.append("       AND NVL(a.id_aeroporto_origem, pre.id_aeroporto_origem)   = ao.id_aeroporto ")
		.append("       AND NVL(a.id_aeroporto_destino, pre.id_aeroporto_destino) = ad.id_aeroporto ")
		.append("       AND pre.id_awb = ")
		.append("         (SELECT MIN(pre_awb.id_awb) ")
		.append("         FROM awb pre_awb, ")
		.append("           cto_awb pre_cto, ")
		.append("           cto_awb ")
		.append("         WHERE pre_awb.id_awb        = pre_cto.id_awb ")
		.append("         AND pre_awb.tp_status_awb   = 'P' ")
		.append("         AND pre_cto.id_conhecimento = cto_awb.id_conhecimento ")
		.append("         AND cto_awb.id_awb          = a.id_awb ")
		.append("         ) ")
		.append("       AND mo_awb.id_aeroporto_origem  = a.id_aeroporto_origem ")
		.append("       AND mo_awb.id_aeroporto_destino = a.id_aeroporto_destino ")
		.append("       AND mo_pre.id_aeroporto_origem  = pre.id_aeroporto_origem ")
		.append("       AND mo_pre.id_aeroporto_destino = pre.id_aeroporto_destino ")
		.append("       AND NOT EXISTS ")
		.append("         (SELECT 1 ")
		.append("         FROM awb_ocorrencia ao ")
		.append("         WHERE ao.id_awb                        = a.id_awb ")
		.append("         AND ao.tp_localizacao                  = 'RE' ")
		.append("         AND TRUNC(CAST(dh_ocorrencia AS DATE)) < (sysdate - 1) ")
		.append("         ) ");
		
		sql2
		.append("      SELECT ")
		.append("        e.id_empresa AS id_cia_aerea, ")
		.append("        p.nm_fantasia AS nm_cia_aerea, ")
		.append("        e.sg_empresa AS sg_empresa, ")
		.append("        NVL(a.tp_localizacao,'AN'), ")
		.append("        a.dh_ocorrencia_situacao, ")
		.append("        a.tp_status_awb, ")
		.append("        a.id_awb AS id_pre_awb, ")
		.append("        NULL AS id_awb, ")
		.append("        NULL, ")
		.append("        a.id_awb AS nr_pre_awb, ")
		.append("        a.dh_digitacao, ")
		.append("        a.vl_frete                 AS vl_awb, ")
		.append("        a.ps_total                 AS ps_total, ")
		.append("        mo_pre.hr_tempo_entrega       AS hr_tempo_entrega, ")
		.append("        mo_pre.hr_tempo_coleta        AS hr_tempo_coleta, ")
		.append("        mo_pre.hr_tempo_transferencia AS hr_tempo_transferencia, ")
		.append("        ao.sg_aeroporto, ")
		.append("        ad.sg_aeroporto ")
		.append("      FROM awb a, ")
		.append("        cia_filial_mercurio cfm, ")
		.append("        empresa e, ")
		.append("        pessoa p, ")
		.append("        monitoramento_network_aereo mo_pre, ")
		.append("        aeroporto ao, ")
		.append("        aeroporto ad ")
		.append("      WHERE a.id_cia_filial_mercurio = cfm.id_cia_filial_mercurio ")
		.append("      AND cfm.id_empresa             = e.id_empresa ")
		.append("      AND e.id_empresa               = p.id_pessoa  ")
		.append("      AND a.id_aeroporto_origem      = ao.id_aeroporto ")
		.append("      AND a.id_aeroporto_destino     = ad.id_aeroporto ")
		.append("      AND a.tp_status_awb            = 'P' ")
		.append("      AND NOT EXISTS ")
		.append("        (SELECT 1 ")
		.append("        FROM awb, ")
		.append("          cto_awb, ")
		.append("          cto_awb pre_cto ")
		.append("        WHERE awb.id_awb            = cto_awb.id_awb ")
		.append("        AND awb.tp_status_awb       = 'E' ")
		.append("        AND pre_cto.id_conhecimento = cto_awb.id_conhecimento ")
		.append("        AND pre_cto.id_awb          = a.id_awb ")
		.append("        ) ")
		.append("      AND mo_pre.id_aeroporto_origem  = a.id_aeroporto_origem ")
		.append("      AND mo_pre.id_aeroporto_destino = a.id_aeroporto_destino ")
		;
		
		YearMonthDay dtPeriodoInicial = criteria.getYearMonthDay("dtPeriodoInicial");
		YearMonthDay dtPeriodoFinal = criteria.getYearMonthDay("dtPeriodoFinal");
		if(dtPeriodoInicial != null && dtPeriodoFinal != null){
			sql1.append("	and trunc(cast(a.dh_emissao as date)) between :dtPeriodoInicial and :dtPeriodoFinal ");
			sql2.append("	and trunc(cast(a.dh_digitacao as date)) between :dtPeriodoInicial and :dtPeriodoFinal ");
		}
		
		Long idAwb = criteria.getLong("idAwb");
		if(idAwb != null){
			sql1.append("	and a.id_awb = :idAwb ");
			sql2.append("	and a.id_awb = :idAwb ");
		}
		
		Long aeroportoOrigem = criteria.getLong("aeroportoOrigem");
		if(aeroportoOrigem != null){
			sql1.append("	and a.id_aeroporto_origem = :aeroportoOrigem ");
			sql2.append("	and a.id_aeroporto_origem = :aeroportoOrigem ");
		}
		
		Long aeroportoDestino = criteria.getLong("aeroportoDestino");
		if(aeroportoDestino != null){
			sql1.append("	and a.id_aeroporto_destino = :aeroportoDestino ");
			sql2.append("	and a.id_aeroporto_destino = :aeroportoDestino ");
		}
		
		Long idCiaAerea = criteria.getLong("idCiaAerea");
		if(idCiaAerea != null){
			sql1.append("	and e.id_empresa = :idCiaAerea ");
			sql2.append("	and e.id_empresa = :idCiaAerea ");
		}
		
		String servico = criteria.getString("servico");
		if (StringUtils.isNotEmpty(servico)) {
			sql1.append("	and a.tp_carac_servico = :servico");
			sql2.append("	and a.tp_carac_servico = :servico");
		}
		
		String situacao = criteria.getString("situacao");
		if(situacao != null){
			if(situacao.equals("AN")){
				sql1.append("	and a.id_awb is null ");
			}else if(situacao.equals("AWB")){
				sql1.append("	and a.tp_status_awb = 'E' ");
				sql2.append("	and a.tp_status_awb = 'E' ");
			}else if(situacao.equals("DR")) {
				sql1.append("	and a.tp_localizacao in (:situacao , 'RP') ");
				sql2.append("	and a.tp_localizacao in (:situacao , 'RP') ");
			}else {
				sql1.append("	and a.tp_localizacao = :situacao ");
				sql2.append("	and a.tp_localizacao = :situacao ");
			}
		}	
		
		sql2
		.append("      ) awb_pre ")
		.append("    ) awb_pre_ocorrencia ");
		
		sql.append("select * from (").append(sql1.toString()).append(" union ").append(sql2.toString()).append(") order by nm_cia_aerea, nr_awb, nr_pre_awb");
		
		return sql.toString();
	}
	
	public List findMonitoramentoNetworkAereoCiaAerea(TypedFlatMap tfm) {
		String sql = getSqlMonitoramentoNetworkAereoByCiaAerea(tfm);
		ConfigureSqlQuery confSql = configureSqlQueryMonitoramentoNetworkAereoCiaAerea();
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql, tfm, confSql);
	}

	private ConfigureSqlQuery configureSqlQueryMonitoramentoNetworkAereoCiaAerea() {
		return new ConfigureSqlQuery() {
			
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("nm_cia_aerea", Hibernate.STRING);
				sqlQuery.addScalar("id_cia_aerea", Hibernate.LONG);
				sqlQuery.addScalar("qt_pre_awb", Hibernate.INTEGER);
				sqlQuery.addScalar("qt_awb", Hibernate.INTEGER);
				sqlQuery.addScalar("eea", Hibernate.INTEGER);
				sqlQuery.addScalar("qt_aguardando_entrega", Hibernate.INTEGER);
				sqlQuery.addScalar("qt_embarque_aereo", Hibernate.INTEGER);
				sqlQuery.addScalar("eva", Hibernate.INTEGER);
				sqlQuery.addScalar("qt_viagem_aerea", Hibernate.INTEGER);				
				sqlQuery.addScalar("qt_liberacao_fiscal", Hibernate.INTEGER);				
				sqlQuery.addScalar("qt_disponivel", Hibernate.INTEGER);				
				sqlQuery.addScalar("tt", Hibernate.DOUBLE);				
				sqlQuery.addScalar("dpr", Hibernate.INTEGER);				
				sqlQuery.addScalar("qt_retirada", Hibernate.INTEGER);				
			}
		};
	}

	public List findMonitoramentoNetworkAereoAwb(TypedFlatMap criteria) {
		String sql = getSqlMonitoriamentoAwb(criteria);
		
		ConfigureSqlQuery confSql =new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("id_cia_aerea", Hibernate.LONG);
				sqlQuery.addScalar("nm_cia_aerea", Hibernate.STRING);
				sqlQuery.addScalar("sg_empresa", Hibernate.STRING);
				sqlQuery.addScalar("dh_ocorrencia_situacao", Hibernate.STRING);
				sqlQuery.addScalar("tp_status_awb", Hibernate.STRING);
				sqlQuery.addScalar("id_pre_awb", Hibernate.LONG);
				sqlQuery.addScalar("id_awb", Hibernate.LONG);
				sqlQuery.addScalar("nr_awb", Hibernate.STRING);
				sqlQuery.addScalar("nr_pre_awb", Hibernate.STRING);
				sqlQuery.addScalar("sg_aeroporto_origem", Hibernate.STRING);
				sqlQuery.addScalar("sg_aeroporto_destino", Hibernate.STRING);
				sqlQuery.addScalar("ps_total", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_awb", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dh_AN", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dh_AE", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dh_EV", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dh_FS", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dh_LF", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dh_DR", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dh_RP", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dh_RE", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dh_FV", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("eea", Hibernate.INTEGER);
				sqlQuery.addScalar("eva", Hibernate.INTEGER);
				sqlQuery.addScalar("dpr", Hibernate.INTEGER);				
				sqlQuery.addScalar("tt_eea", Hibernate.BIG_DECIMAL);				
				sqlQuery.addScalar("tt_eva", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("tt_sefaz", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("tt_total", Hibernate.BIG_DECIMAL);	
				
				Properties propertiesTpStatusAwb = new Properties();
				propertiesTpStatusAwb.put("domainName","DM_STATUS_AWB");
				sqlQuery.addScalar("tp_status_awb", Hibernate.custom(DomainCompositeUserType.class, propertiesTpStatusAwb));
				
				Properties propertiesTpLocalizacao = new Properties();
				propertiesTpLocalizacao.put("domainName","DM_TIPO_LOCALIZACAO_AWB");
				sqlQuery.addScalar("tp_localizacao", Hibernate.custom(DomainCompositeUserType.class, propertiesTpLocalizacao));
			}
		};		
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql, criteria, confSql);
	}


	
}