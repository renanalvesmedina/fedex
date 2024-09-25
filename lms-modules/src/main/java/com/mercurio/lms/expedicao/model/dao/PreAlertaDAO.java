package com.mercurio.lms.expedicao.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.ContatoCorrespondencia;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.PreAlerta;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PreAlertaDAO extends BaseCrudDao<PreAlerta, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PreAlerta.class;
	}

	public PreAlerta findById(Long id) {
		return super.findById(id);
	}

	public TypedFlatMap findMapById(Long id) {

		PreAlerta preAlerta = (PreAlerta)this.findById(id);
		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("idPreAlerta", preAlerta.getIdPreAlerta());
		toReturn.put("nrPreAlerta", preAlerta.getNrPreAlerta());
		toReturn.put("dsVoo", preAlerta.getDsVoo());
		toReturn.put("blVooConfirmado", preAlerta.getBlVooConfirmado());
		toReturn.put("dhSaida", preAlerta.getDhSaida());
		toReturn.put("dhChegada", preAlerta.getDhChegada());
		toReturn.put("dhRecebimentoMens", preAlerta.getDhRecebimentoMens());
		/** Awb */
		if (preAlerta.getAwb() != null) {
			Awb awb = preAlerta.getAwb();
			toReturn.put("awb.idAwb", awb.getIdAwb());
			toReturn.put("awb.nrAwb", AwbUtils.getNrAwb(awb.getDsSerie(), awb.getNrAwb(), awb.getDvAwb()));
			
			if(awb.getCiaFilialMercurio() != null && awb.getCiaFilialMercurio().getEmpresa() != null){
				toReturn.put("ciaFilialMercurio.empresa.idEmpresa", awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa());
				toReturn.put("ciaFilialMercurio.empresa.sgEmpresa", awb.getCiaFilialMercurio().getEmpresa().getSgEmpresa());
			}
			toReturn.put("awb.filialByIdFilialOrigem.idFilial", awb.getFilialByIdFilialOrigem().getIdFilial());
		}
		/** Usuario nao eh obrigatorio */
		if (preAlerta.getUsuario() != null) {
			toReturn.put("usuario.idUsuario", preAlerta.getUsuario().getIdUsuario());
			toReturn.put("usuario.nrMatricula", preAlerta.getUsuario().getNrMatricula());
			toReturn.put("usuario.nmUsuario", preAlerta.getUsuario().getNmUsuario());
		}
		return toReturn;
	}
	
	/**
	 * @author Andre Valadas
	 * 
	 * @param ids
	 * @return
	 */
	public List findByIds(List ids) {
		if (ids == null || ids.size() < 1){
			return null;
		}
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("pa");
		sql.addFrom(getPersistentClass().getName(), "pa");
		sql.addCustomCriteria("pa.id in ( :ids )", ids);

		Query query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sql.getSql());
		query.setParameterList("ids", ids);
		return query.list();
	}

	/**
	 * Apaga uma entidade através do Id do Pedido de Coleta.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */	
	public void removeByIdPedidoColeta(Serializable idPedidoColeta) {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from ").append(PreAlerta.class.getName()).append(" as pa ");
		sql.append(" where pa.pedidoColeta.id = :id");
		getAdsmHibernateTemplate().removeById(sql.toString(), idPedidoColeta);
	}

	public List findByNrAwb(Long nrAwb) {
		ProjectionList projectionList = Projections.projectionList()
			.add(Projections.property("a.dvAwb"), "dvAwb")
			.add(Projections.property("pa.idPreAlerta"), "idPreAlerta")
			.add(Projections.property("blVooConfirmado"), "blVooConfirmado");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pa")
			.setProjection(projectionList)
			.createAlias("pa.awb", "a")
			.add(Restrictions.eq("a.nrAwb", nrAwb))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
	}

	/**
	 * getRowCount Visualizar Prealerta Implementado
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCount(Map criteria) {
		/** Query */
		DetachedCriteria dc = this.getQueryPaginated(criteria)
			.setProjection(Projections.count("pa.id"));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
	 * findPaginated Visualizar Prealerta Implementado
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @param findDef
	 * @return TypedFlatMap
	 */
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		/** Map de Retorno */
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("pa.id"), "idPreAlerta");
		projectionList.add(Projections.property("pa.nrPreAlerta"), "nrPreAlerta");
		projectionList.add(Projections.property("pa.dsVoo"), "dsVoo");
		projectionList.add(Projections.property("pa.dhSaida.value"), "dhSaida");
		projectionList.add(Projections.property("pa.dhChegada.value"), "dhChegada");
		projectionList.add(Projections.property("pa.blVooConfirmado"), "blVooConfirmado");
		projectionList.add(Projections.property("emp.sgEmpresa"), "sgEmpresa");
		projectionList.add(Projections.property("emp.idEmpresa"), "idEmpresa");
		projectionList.add(Projections.property("fo.sgFilial"), "awb_filialByIdFilialOrigem_sgFilial");
		projectionList.add(Projections.property("ao.sgAeroporto"), "awb_aeroportoByIdAeroportoOrigem_sgAeroporto");
		projectionList.add(Projections.property("awb.nrAwb"), "nrAwb");
		projectionList.add(Projections.property("awb.dvAwb"), "dvAwb");
		projectionList.add(Projections.property("awb.dsSerie"), "dsSerie");
		projectionList.add(Projections.property("awb.qtVolumes"), "awb_qtVolumes");
		projectionList.add(Projections.property("awb.psTotal"), "awb_psTotal");
		projectionList.add(Projections.property("awb.psCubado"), "awb_psCubado");

		DetachedCriteria dc = this.getQueryPaginated(criteria)
			.setProjection(projectionList);

		/** TypedFlatMapResultTransformer */
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}

	/**
	 * getQueryPaginated Visualizar Prealerta 
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @return
	 */
	public DetachedCriteria getQueryPaginated(Map map) {

		TypedFlatMap criteria = new TypedFlatMap();
		criteria.putAll(map);

		/** Filiais Usuario */
		List filiaisUsuario = SessionUtils.getFiliaisUsuarioLogado();
		List idsFiliais = new ArrayList();
		for (Iterator iter = filiaisUsuario.iterator(); iter.hasNext();) {
			Filial filial = (Filial) iter.next();
			idsFiliais.add(filial.getIdFilial());
		}
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pa")
			/** FROM */
			.createAlias("pa.awb", "awb")
			.createAlias("awb.filialByIdFilialOrigem", "fo")
			.createAlias("awb.filialByIdFilialDestino", "fd")
			.createAlias("awb.aeroportoByIdAeroportoOrigem", "ao")
			.createAlias("awb.aeroportoByIdAeroportoDestino", "ad")
			.createAlias("awb.ciaFilialMercurio", "fm")
			.createAlias("fm.empresa", "emp")
			.createAlias("emp.pessoa", "pes")
			/** WHERE */
			.add(Restrictions.not(Restrictions.eq("awb.tpStatusAwb", "C")))
			.add(Restrictions.isNull("pa.dhRecebimentoMens.value"))
			.add(Restrictions.or(
				 Restrictions.in("fo.id", idsFiliais)
				,Restrictions.in("fd.id", idsFiliais)))
			/** ODER BY */
			.addOrder(Order.asc("pa.nrPreAlerta"));

		Long idAeroportoDestino = criteria.getLong("idAeroporto");
		if (idAeroportoDestino != null) {
			dc.add(Restrictions.eq("ad.id", idAeroportoDestino));
		}

		return dc;
	}

	/**
	 * getRowCountPrealerta Implementado
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountPreAlerta(TypedFlatMap criteria) {
		/** Query */
		SqlTemplate hql = this.getQueryPaginatedPreAlerta(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
	}

	/**
	 * findPaginatedPrealerta Implementado
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @param findDef
	 * @return TypedFlatMap
	 */
	public ResultSetPage findPaginatedPreAlerta(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.getQueryPaginatedPreAlerta(criteria);
		/** Map de Retorno */
		StringBuffer projection = new StringBuffer();
		projection.append(" new Map(");
		projection.append(" pa.id as idPreAlerta");
		projection.append("	,pa.nrPreAlerta as nrPreAlerta");
		projection.append("	,pa.dsVoo as dsVoo");
		projection.append("	,pa.dhSaida.value as dhSaida");
		projection.append("	,pa.dhChegada.value as dhChegada");
		projection.append("	,pa.dhRecebimentoMens as dhRecebimentoMens");
		projection.append("	,pa.blVooConfirmado as blVooConfirmado");
		projection.append("	,awb.nrAwb as nrAwb");
		projection.append("	,awb.dvAwb as dvAwb");
		projection.append("	,awb.dsSerie as dsSerie");
		projection.append("	,pes.nmPessoa as pessoa_nmPessoa");
		projection.append("	,emp.idEmpresa as idEmpresa");
		projection.append("	,emp.sgEmpresa as sgEmpresa");
		projection.append("	)");

		hql.addProjection(projection.toString());

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}
	
	/**
	 * getQueryPaginatedPreAlerta Manter Prealerta 
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @return
	 */
	public SqlTemplate getQueryPaginatedPreAlerta(TypedFlatMap criteria) {

		SqlTemplate hql = new SqlTemplate();
		/** FROM */
		StringBuffer query = new StringBuffer();
		query.append(getPersistentClass().getName()+" pa");
		query.append(" inner join pa.awb awb");
		query.append(" left join pa.usuario usu");
		query.append(" inner join awb.filialByIdFilialOrigem fo");
		query.append(" inner join awb.ciaFilialMercurio fm");
		query.append(" inner join fm.empresa emp");
		query.append(" inner join emp.pessoa pes");
		hql.addFrom(query.toString());

		/** WHERE */
		Integer nrPreAlerta = criteria.getInteger("nrPreAlerta");
		if (nrPreAlerta != null) {
			hql.addCriteria("pa.nrPreAlerta", "=", nrPreAlerta);
		}
		Long idAwb = criteria.getLong("awb.idAwb");
		if (idAwb != null) {
			hql.addCriteria("awb.id", "=", idAwb);
		}
		String dsVoo = criteria.getString("dsVoo");
		if (StringUtils.isNotBlank(dsVoo)) {
			hql.addCriteria("pa.dsVoo", "LIKE", dsVoo);
		}
		Boolean blVooConfirmado = criteria.getBoolean("blVooConfirmado");
		if (blVooConfirmado != null) {
			hql.addCriteria("pa.blVooConfirmado", "=", blVooConfirmado);
		}
		DateTime dhSaidaInicial = criteria.getDateTime("dhSaidaInicial");
		if (dhSaidaInicial != null) {
			hql.addCriteria("pa.dhSaida.value", ">=", dhSaidaInicial);
		}
		DateTime dhSaidaFinal = criteria.getDateTime("dhSaidaFinal");
		if (dhSaidaFinal != null) {
			hql.addCriteria("pa.dhSaida.value", "<=", dhSaidaFinal);
		}
		DateTime dhChegadaInicial = criteria.getDateTime("dhChegadaInicial");
		if (dhChegadaInicial != null) {
			hql.addCriteria("pa.dhChegada.value", ">=", dhChegadaInicial);
		}
		DateTime dhChegadaFinal = criteria.getDateTime("dhChegadaFinal");
		if (dhChegadaFinal != null) {
			hql.addCriteria("pa.dhChegada.value", "<=", dhChegadaFinal);
		}
		/** ODER BY */
		hql.addOrderBy("pa.nrPreAlerta");

		return hql;
	}
	
	/**
	 * findAwbByPreAlerta
	 * @author Andre Valadas
	 * 
	 * @param idPreAlerta
	 * @return List<TypedFlatMap>
	 */
	public List findAwbByPreAlerta(Long idPreAlerta) {

		SqlTemplate hql = new SqlTemplate();
		/** Map de Retorno */
		StringBuffer projection = new StringBuffer();
		projection.append(" new Map(");
		projection.append("  awb.idAwb as awb_idAwb");
		projection.append("	,awb.psTotal as awb_psTotal");
		projection.append("	,awb.qtVolumes as awb_qtVolumes");
		projection.append("	,awb.tpFrete as awb_tpFrete");
		projection.append("	,pfo.nrIdentificacao as cliente_nrIdentificacao");
		projection.append("	,fd.id as awb_filialByIdFilialDestino_idFilial");
		projection.append("	,fd.sgFilial as awb_filialByIdFilialDestino_sgFilial");
		projection.append("	,pfd.nmFantasia as awb_filialByIdFilialDestino_nmFantasia");
		projection.append("	,mu.nmMunicipio as awb_aeroportoByIdAeroportoDestino_nmMunicipio");
		projection.append("	,ce.idEmpresa as awb_ciaFilialMercurio_empresa_idEmpresa");
		projection.append("	,mo.idMoeda as awb_idMoeda");
		projection.append("	,mo.dsSimbolo as awb_moeda_dsSimbolo");
		projection.append("	,np.idNaturezaProduto as awb_naturezaProduto_idNaturezaProduto");
		projection.append("	)");
		hql.addProjection(projection.toString());

		/** FROM */
		StringBuffer query = new StringBuffer();
		query.append(Awb.class.getName()+" awb");
		query.append(" inner join awb.preAlertas pa");
		query.append(" inner join awb.filialByIdFilialOrigem.pessoa pfo");
		query.append(" inner join awb.filialByIdFilialDestino fd");
		query.append(" inner join fd.pessoa pfd");
		query.append(" inner join awb.moeda mo");
		query.append(" inner join awb.naturezaProduto np");
		query.append(" inner join awb.ciaFilialMercurio cfm");
		query.append(" inner join cfm.empresa ce");
		query.append(" inner join awb.aeroportoByIdAeroportoDestino.pessoa.enderecoPessoa.municipio mu");
		query.append(" inner join mu.unidadeFederativa uf");
		hql.addFrom(query.toString());

		/** WHERE */
		if (idPreAlerta != null) {
			hql.addCriteria("pa.idPreAlerta", "=", idPreAlerta);
		}

		return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()));
	}

	/**
	 * findEmailsContadosByFiliaisDestinoAWB 
	 * @author Andre Valadas
	 * 
	 * @param Long idPreAlerta
	 * @return List<Contato>
	 */
	public List findEmailsContadosByFiliaisDestinoAWB(Long idPreAlerta) {

		/** SubSelect para Buscar Filial */
		DetachedCriteria dcFilialAwb = DetachedCriteria.forClass(getPersistentClass(), "pa")
			.setProjection(Projections.property("p.id"))
			.createAlias("pa.awb", "awb")
			.createAlias("awb.aeroportoByIdAeroportoDestino", "ad")
			.createAlias("ad.filial", "f")
			.createAlias("f.pessoa", "p")
			.add(Restrictions.eq("pa.id", idPreAlerta));
		List subSelect = getAdsmHibernateTemplate().findByDetachedCriteria(dcFilialAwb);

		DetachedCriteria dc = DetachedCriteria.forClass(ContatoCorrespondencia.class, "cc")
			.setProjection(Projections.property("c.dsEmail"))
			.createAlias("cc.contato", "c")
			.createAlias("c.pessoa", "pes")
			.add(Restrictions.eq("cc.tpCorrespondencia", "P"))
			.add(Restrictions.isNotNull("c.dsEmail"))
			.add(Property.forName("pes.id").in(subSelect));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List findEmailsContadosByParceira(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(Contato.class, "c")
				.setProjection(Projections.property("c.dsEmail"))
				.createAlias("c.pessoa", "pes")
				.add(Restrictions.isNotNull("c.dsEmail"))
				.add(Property.forName("pes.id").eq(idCliente));
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
}