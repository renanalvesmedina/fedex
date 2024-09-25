package com.mercurio.lms.vendas.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.PromotorCliente;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PromotorClienteDAO extends BaseCrudDao<PromotorCliente, Long> {

	private final int MODAL = 0;
	private final int MODAL_ABRANGENCIA = 1;
	private final int ABRANGENCIA = 2;
	private final int TODOS = 3;

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PromotorCliente.class;
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("usuario",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("usuario",FetchMode.JOIN);
	}

	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("usuario",FetchMode.JOIN);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idCliente
	 * @param idUsuario
	 * @return <PromotorCliente>
	 */
	public PromotorCliente findPromotorCliente(Long idCliente, Long idUsuario) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("cliente", "c");
		dc.createAlias("usuario", "u");
		dc.add(Restrictions.eq("c.idCliente", idCliente));
		dc.add(Restrictions.eq("u.idUsuario", idUsuario));
		return (PromotorCliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	/**
	 * Método utilizado para validar o promotor.
	 * @author Leandro Renato Beneduzi
	 * 
	 * @param idPromotorCliente
	 * @param idCliente
	 * @param idUsuario
	 * @param tpModal
	 * @param tpAbrangencia
	 * @return <PromotorCliente>
	 */
	public List<PromotorCliente> findPromotorCliente(Long idPromotorCliente, Long idCliente, Long idUsuario, DomainValue tpModal, DomainValue tpAbrangencia) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("cliente", "c");
		dc.createAlias("usuario", "u");
		dc.add(Restrictions.eq("c.idCliente", idCliente));
		dc.add(Restrictions.eq("u.idUsuario", idUsuario));
		dc.add(Restrictions.eq("tpModal", tpModal));
		dc.add(Restrictions.eq("tpAbrangencia", tpAbrangencia));
		if (idPromotorCliente != null){
			dc.add(Restrictions.ne("idPromotorCliente", idPromotorCliente));
		}
		return (List<PromotorCliente>) getAdsmHibernateTemplate().findByCriteria(dc);
	}
	
	/**
	 * Método utilizado pela Integração
	 * @author Anibal Maffioletti de Deus
	 * 
	 * @param idCliente
	 * @return List<PromotorCliente>
	 */
	public List<PromotorCliente> findPromotorCliente(Long idCliente) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("cliente", "c");
		dc.add(Restrictions.eq("c.idCliente", idCliente));
		return (List<PromotorCliente>) getAdsmHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Foi necessário replicar o método para não afetar as rotinas de integração
	 * @param idCliente
	 * @return
	 */
	public List<PromotorCliente> findPromotorClienteByIdCliente(Long idCliente) {
		StringBuilder hql = new StringBuilder();
		hql.append("select pc from ")
			.append(getPersistentClass().getName()).append("  pc ")
			.append(" join fetch pc.usuario ")
			.append(" where pc.cliente.id = ?");
		return (List<PromotorCliente>) getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idCliente});
	}
	
	public List<PromotorCliente> findByIdClienteAndTpModalAndDtAtual(Long idCliente, String tpModal, DateTime dtAtual) {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("SELECT pc ")
		.append("FROM ").append(PromotorCliente.class.getSimpleName()).append(" pc ")
		.append("JOIN pc.cliente cl ")
		.append("WHERE ")
		.append("	 cl.idCliente = :idCliente ")
		.append("AND pc.tpModal = :tpModal ")
		.append("AND pc.dtInicioPromotor <= :dtAtual ")
		.append("AND (pc.dtFimPromotor >= :dtAtual OR pc.dtFimPromotor IS NULL) ")
		;
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idCliente", idCliente);
		parameters.put("tpModal", tpModal);
		parameters.put("dtAtual", dtAtual);
		
		return (List<PromotorCliente>)getAdsmHibernateTemplate().findByNamedParam(sql.toString(), parameters);
	}

	/**
	 * Consulta Otimizada para Manter Promotores List 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedOtimizado(TypedFlatMap criteria) {
		SqlTemplate query = createCriteriaPaginated(criteria);
		StringBuilder projections = new StringBuilder()
			.append(" new Map( ")
			.append("  pc.id as idPromotorCliente")
			.append(" ,pc.usuario.nmUsuario as nomePromotor")
			.append(" ,pc.dtInicioPromotor as dtInicioPromotor")
			.append(" ,pc.tpModal as tpModal")
			.append(" ,pc.tpAbrangencia as tpAbrangencia")
			.append(" ,pc.pcRateioComissao as pcRateioComissao")
			.append(" )");
		query.addProjection(projections.toString());
		query.addOrderBy("pc.usuario.nmUsuario");
		query.addOrderBy("pc.dtInicioPromotor");
		query.addOrderBy("pc.tpModal");
		query.addOrderBy("pc.tpAbrangencia");
		query.addOrderBy("pc.pcRateioComissao");

		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(query.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), query.getCriteria());
		return rsp;
	}

	public Integer getRowCountOtimizado(TypedFlatMap criteria) {
		SqlTemplate query = createCriteriaPaginated(criteria);
		query.addProjection("count(pc.id)");
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(query.getSql(), query.getCriteria());
		return result.intValue();
	}

	/**
	 * Determina filtros da consulta
	 * @param criteria
	 * @return
	 */
	private SqlTemplate createCriteriaPaginated(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
			.append(getPersistentClass().getName()).append(" as pc")
			.append(" join pc.cliente c");

		SqlTemplate query = new SqlTemplate();
		query.addFrom(hql.toString());

		/** Where */
		Long idCliente = criteria.getLong("cliente.idCliente");
		if (idCliente != null) {
			query.addCriteria("c.id", "=", idCliente);
		}
		Long idUsuario = criteria.getLong("usuario.idUsuario");
		if (idUsuario != null) {
			query.addCriteria("pc.usuario.id", "=", idUsuario);
		}
		DomainValue tpModal = criteria.getDomainValue("tpModal");
		if (StringUtils.isNotBlank(tpModal.getValue())) {
			query.addCriteria("pc.tpModal", "=", tpModal.getValue());
		}
		DomainValue tpAbrangencia = criteria.getDomainValue("tpAbrangencia");
		if (StringUtils.isNotBlank(tpAbrangencia.getValue())) {
			query.addCriteria("pc.tpAbrangencia", "=", tpAbrangencia.getValue());
		}
		YearMonthDay dtInicioPromotor = criteria.getYearMonthDay("dtInicioPromotor");
		if (dtInicioPromotor != null) {
			query.addCriteria("pc.dtInicioPromotor", "=", dtInicioPromotor);
		}

		return query;
	}

	/**
	 * Retorna a dtGeracao do Cliente 
	 * @param idCliente
	 * @return dtGeracao
	 */
	public YearMonthDay findClienteDtGeracao(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(Cliente.class);
		dc.add(Restrictions.eq("idCliente",idCliente));
		dc.setProjection(Projections.property("dtGeracao"));

		return (YearMonthDay) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Busca Usuario vigente por sua filial regional
	 * @param idUsuario
	 * @param dtInicioPromotor
	 * @return
	 */
	public Usuario findUsuarioRegionalFilialVigente(Long idUsuario, YearMonthDay dtInicioPromotor) {
		DetachedCriteria dc = DetachedCriteria.forClass(Usuario.class, "usu");
		dc.createAlias("usu.vfuncionario", "func");

		dc.createAlias("func.filial","filial");
		dc.createAlias("filial.regionalFiliais","regionalFilial");

		dc.add(Restrictions.eq("usu.id", idUsuario));
		dc.add(Restrictions.le("regionalFilial.dtVigenciaInicial", dtInicioPromotor));
		dc.add(Restrictions.ge("regionalFilial.dtVigenciaFinal", dtInicioPromotor));

		return (Usuario) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Retorna a dtPrimeiroPromotor
	 * @param idCliente
	 * @return dtPrimeiroPromotor
	 */
	public YearMonthDay findDtPrimeiroPromotor(Long idCliente) {

		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.property("dtPrimeiroPromotor"));
		dc.createAlias("cliente", "cliente");
		dc.add(Restrictions.eq("cliente.idCliente", idCliente));

		YearMonthDay dtPrimeiroPromotor = null;
		List<YearMonthDay> list = findByDetachedCriteria(dc);
		if (!list.isEmpty()) {
			dtPrimeiroPromotor = list.get(0);
		}
		return dtPrimeiroPromotor;	
	}

	public Integer verifyModalAbrangencia(Long idCliente, DomainValue tpModal, DomainValue tpAbrangencia) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.rowCount());
		dc.createAlias("cliente", "cliente");
		dc.add(Restrictions.eq("cliente.id", idCliente));

		/** Valida Modal e Abragencia */
		if(tpModal != null || tpAbrangencia != null){
			if(tpModal != null && tpAbrangencia != null){
				dc.add(Restrictions.or(Restrictions.isNull("tpModal"), Restrictions.isNull("tpAbrangencia")));
			} else if(tpModal != null){
				dc.add(Restrictions.or(Restrictions.isNull("tpModal"), Restrictions.isNotNull("tpAbrangencia")));
			} else if(tpAbrangencia!= null){
				dc.add(Restrictions.or(Restrictions.isNull("tpAbrangencia"), Restrictions.isNotNull("tpModal")));
			}
		} else {
			dc.add(Restrictions.or(Restrictions.isNotNull("tpModal"), Restrictions.isNotNull("tpAbrangencia")));
		}

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);	
	}

	/**
	 * Verifica se o percentual do rateio da Comissão a ser inserido não ultrapassa os 100% com os registros já existentes.
	 * @param idCliente
	 * @param tpModal
	 * @param tpAbrangencia
	 * @param idPromotorCliente
	 * @param pcRateioComissao
	 * @return booleano que indica se estourou o percentual de rateio acima de 100%
	 */
	public boolean findSumPcRateioComissao(Long idCliente, DomainValue tpModal,DomainValue tpAbrangencia,Long idPromotorCliente, BigDecimal pcRateioComissao) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("cliente", "cliente");
		dc.add(Restrictions.eq("cliente.idCliente",idCliente));
		if (idPromotorCliente != null){
			dc.add(Restrictions.ne("idPromotorCliente",idPromotorCliente));
		}

		List<PromotorCliente> result = findByDetachedCriteria(dc);
		int caminho = TODOS;

		if( tpModal != null && StringUtils.isNotBlank(tpModal.getValue()) ){
			if( tpAbrangencia == null ){
				caminho = MODAL;
			} else {
				caminho = MODAL_ABRANGENCIA;
			}
		} else if( tpAbrangencia != null && StringUtils.isNotBlank(tpAbrangencia.getValue() )){
			caminho = ABRANGENCIA;
		} else {
			caminho = TODOS;
		}

		BigDecimal somatorio = BigDecimalUtils.ZERO; 
		BigDecimal somaModal = BigDecimalUtils.ZERO;
		BigDecimal somaAbrangencia = BigDecimalUtils.ZERO;
		BigDecimal somaTodos = BigDecimalUtils.ZERO;
		for (PromotorCliente pc : result) {
			switch (caminho) {
				case MODAL:
					if( pc.getTpModal() == null || pc.getTpModal().equals(tpModal) )
						somatorio = somatorio.add(pc.getPcRateioComissao());
					break;
				case ABRANGENCIA:
					if( pc.getTpAbrangencia() == null || pc.getTpAbrangencia().equals(tpAbrangencia) )
						somatorio = somatorio.add(pc.getPcRateioComissao());
					break;
				case MODAL_ABRANGENCIA:
					if( ( pc.getTpModal() == null || pc.getTpModal().equals(tpModal) ) &&
						( pc.getTpAbrangencia() == null || pc.getTpAbrangencia().equals(tpAbrangencia) ))
						somatorio = somatorio.add(pc.getPcRateioComissao());
					break;
				case TODOS:
					if( pc.getTpModal() != null )
						somaModal = somaModal.add(pc.getPcRateioComissao());
					if( pc.getTpAbrangencia() != null )
						somaAbrangencia = somaAbrangencia.add(pc.getPcRateioComissao());
					if( pc.getTpModal() == null && pc.getTpAbrangencia() == null )
						somaTodos = somaTodos.add(pc.getPcRateioComissao());
					break;
				default:
					break;
			}
			if(caminho == TODOS) {
				if(CompareUtils.gt(somaModal.add(pcRateioComissao), BigDecimalUtils.HUNDRED)
					|| CompareUtils.gt(somaAbrangencia.add(pcRateioComissao), BigDecimalUtils.HUNDRED)
					|| CompareUtils.gt(somaTodos.add(pcRateioComissao), BigDecimalUtils.HUNDRED))
					return true;
			} else 
				return CompareUtils.gt(somatorio.add(pcRateioComissao), BigDecimalUtils.HUNDRED);
		}
		return false;
	}
}