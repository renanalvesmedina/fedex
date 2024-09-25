package com.mercurio.lms.tributos.model.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.DescontoInssCarreteiro;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class DescontoInssCarreteiroDAO extends BaseCrudDao<DescontoInssCarreteiro, Long> {

	/**
	 * Busca o valor total do inss, do proprietário informado.<BR>
	 * O valor total é um somatório dos valores do inss para o mes e ano
	 * informado na dtBase.<BR>
	 *
	 * @author Robson Edemar Gehl
	 * @param idPessoa
	 *            proprietário
	 * @param dtBase
	 *            mes e ano
	 * @return total do valor do inss
	 */
	public BigDecimal findTotalValorINSS(Long idPessoa, YearMonthDay dtBase) {

		StringBuffer sql = new StringBuffer().append("select sum(d.vlInss) from ").append(getPersistentClass().getName()).append(" d join d.proprietario p ").append(" where p.id = :propId and year(d.dtEmissaoRecibo) = :ano and month(d.dtEmissaoRecibo) = :mes ");

		Query query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sql.toString());
		query.setParameter("propId", idPessoa);
		query.setParameter("ano", Integer.valueOf(dtBase.getYear()));
		query.setParameter("mes", Integer.valueOf(dtBase.getMonthOfYear()));

		List list = query.list();
		if (!list.isEmpty()) {
			return (BigDecimal) list.get(0);
		}
		return null;
	}

	/**
	 * Busca uma lista de descontoInss a partir de um proprietario.
	 * 
	 * @param idProprietario
	 *            proprietário
	 * @param dtBase
	 *            mes e ano
	 * @return List de DescontoInss
	 */
	public List findByProprietario(Long idProprietario, YearMonthDay dtBase) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("D");
		hql.addFrom(getPersistentClass().getName(), "D left join fetch D.filial");

		hql.addCriteria("D.proprietario.id", "=", idProprietario);
		if (dtBase != null) {
			hql.addCriteria("year(D.dtEmissaoRecibo)", "=", Integer.valueOf(dtBase.getYear()));
			hql.addCriteria("month(D.dtEmissaoRecibo)", "=", Integer.valueOf(dtBase.getMonthOfYear()));
		}

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return DescontoInssCarreteiro.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("proprietario", FetchMode.JOIN);
		lazyFindById.put("proprietario.pessoa", FetchMode.JOIN);
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
		lazyFindById.put("usuario", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("proprietario", FetchMode.JOIN);
		lazyFindPaginated.put("proprietario.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("filial", FetchMode.JOIN);
		lazyFindPaginated.put("filial.pessoa", FetchMode.JOIN);
	}

	@SuppressWarnings("unchecked")
	public List<DescontoInssCarreteiro> findRecibosOutrasEmpresas(final TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(getPersistentClass().getName(), "DescInssC");

		//Proprietario
		hql.addCriteria("DescInssC.proprietario.idProprietario", "=", criteria.getLong("proprietario.idProprietario"));
		//Filial
		hql.addCriteria("DescInssC.filial.idFilial", "=", criteria.getLong("filial.idFilial"));
		//Nome empregador
		if (criteria.getString("dsEmpresa") != null) {
			hql.addCriteria("upper(DescInssC.dsEmpresa)", "like", "%" + criteria.getString("dsEmpresa").toUpperCase().trim() + "%");
		}
		//Identificador empregador - opcional
		if (criteria.getDomainValue("tipoIdentificadorEmpregador.dsIdentificador") != null) {
			hql.addCriteria("DescInssC.tpIdentificacao", "=", criteria.getDomainValue("tipoIdentificadorEmpregador.dsIdentificador").getValue());
		}
		hql.addCriteria("DescInssC.nrIdentEmpregador", "=", criteria.getString("nrEmpregador"));
		//Numero recibo
		hql.addCriteria("DescInssC.nrRecibo", "=", criteria.getString("nrRecibo"));
		//Valor do INSS
		hql.addCriteria("DescInssC.vlInss", "=", criteria.getBigDecimal("vlInss"));
		
		//Data emissao recibo
		if (criteria.getYearMonthDay("dtEmissaoReciboInicial") != null && criteria.getYearMonthDay("dtEmissaoReciboFinal") != null) {
			hql.addCustomCriteria("DescInssC.dtEmissaoRecibo between ? and ? ");
			hql.addCriteriaValue(criteria.getYearMonthDay("dtEmissaoReciboInicial"));
			hql.addCriteriaValue(criteria.getYearMonthDay("dtEmissaoReciboFinal"));
		}
		
		hql.addOrderBy("DescInssC.proprietario.pessoa.nrIdentificacao", "asc");
		hql.addOrderBy("DescInssC.dtEmissaoRecibo", "desc");
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public Integer getRowCountRecibosOutrasEmpresas() {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(getPersistentClass().getName());

		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
	}

}