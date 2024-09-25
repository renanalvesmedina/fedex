package com.mercurio.lms.vendas.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.TaxaCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TaxaClienteDAO extends BaseCrudDao<TaxaCliente, Long>{
	
	private JdbcTemplate jdbcTemplate;
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("parcelaPreco", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return TaxaCliente.class;
	}
	/**
	 * Método utilizado pela Integração - CQPRO00008642
	 * @author Andre Valadas
	 * 
	 * @param idsTabelaDivisaoCliente
	 */
	public void removeByTabelaDivisaoCliente(List<Long> idsTabelaDivisaoCliente) {
		StringBuilder hql = new StringBuilder();
    	hql.append(" DELETE FROM ").append(getPersistentClass().getName());
    	hql.append(" WHERE parametroCliente.id IN ( ");
    	hql.append("  	SELECT pc.id ");
    	hql.append("   	FROM ").append(ParametroCliente.class.getName()).append(" pc ");
    	hql.append("    WHERE pc.tabelaDivisaoCliente.id IN (:id)) ");
    	getAdsmHibernateTemplate().removeByIds(hql.toString(), idsTabelaDivisaoCliente);
	}

	public ResultSetPage findPaginatedByParametroCliente(Long idParamentoCliente, FindDefinition fd) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("tc.idTaxaCliente"), "idTaxaCliente")
			.add(Projections.property("tc.tpTaxaIndicador"), "tpTaxaIndicador")
			.add(Projections.property("tc.vlTaxa"), "vlTaxa")
			.add(Projections.property("tc.psMinimo"), "psMinimo")
			.add(Projections.property("tc.vlExcedente"), "vlExcedente")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco.nmParcelaPreco")
			.add(Projections.property("m.dsSimbolo"), "dsSimbolo")
			.add(Projections.property("tc.pcReajTaxa"), "pcReajTaxa")
			.add(Projections.property("tc.pcReajVlExcedente"), "pcReajVlExcedente");
		DetachedCriteria dc = getCriteria(idParamentoCliente)
			.setProjection(pl)
			.createAlias("tc.parcelaPreco", "pp")
			.createAlias("tc.parametroCliente", "pc")
			.createAlias("pc.tabelaDivisaoCliente", "tdc")
			.createAlias("tdc.tabelaPreco", "tab")
			.createAlias("tab.moeda", "m")
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TaxaCliente.class))
			.addOrder(OrderVarcharI18n.asc("pp.nmParcelaPreco", LocaleContextHolder.getLocale()));
		return findPaginatedByDetachedCriteria(dc, fd.getCurrentPage(), fd.getPageSize());
	}
	public Integer getRowCountByParametroCliente(Long idParamentoCliente) {
		DetachedCriteria dc = getCriteria(idParamentoCliente).setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	private DetachedCriteria getCriteria(Long idParamentoCliente) {
		return DetachedCriteria.forClass(TaxaCliente.class, "tc")
		.add(Restrictions.eq("tc.parametroCliente.id", idParamentoCliente));
	}
	
	public Map findTaxaClienteById(Long id) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("tc.idTaxaCliente"), "idTaxaCliente")
			.add(Projections.property("tc.tpTaxaIndicador"), "tpTaxaIndicador")
			.add(Projections.property("tc.vlTaxa"), "vlTaxa")
			.add(Projections.property("tc.psMinimo"), "psMinimo")
			.add(Projections.property("tc.vlExcedente"), "vlExcedente")
			.add(Projections.property("tc.pcReajTaxa"), "pcReajTaxa")
			.add(Projections.property("tc.pcReajVlExcedente"), "pcReajVlExcedente")
			.add(Projections.property("pp.idParcelaPreco"), "parcelaPreco_idParcelaPreco")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco_nmParcelaPreco");
		final DetachedCriteria dc = DetachedCriteria.forClass(TaxaCliente.class, "tc")
			.setProjection(pl)
			.createAlias("tc.parcelaPreco", "pp")
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance())
			.add(Restrictions.idEq(id));
		return (Map)getAdsmHibernateTemplate().execute(new HibernateCallback() {
	            @Override
				public Object doInHibernate(Session session) throws HibernateException {
	                Criteria criteria = dc.getExecutableCriteria(session);
			        return criteria.uniqueResult();
	            }
	        }, true);
	}
	
	public List findByIdParametroCliente(Long idParametroCliente) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("tc.tpTaxaIndicador"), "tpTaxaIndicador")
			.add(Projections.property("tc.vlTaxa"), "vlTaxa")
			.add(Projections.property("tc.psMinimo"), "psMinimo")
			.add(Projections.property("tc.vlExcedente"), "vlExcedente")
			.add(Projections.property("tc.pcReajTaxa"), "pcReajTaxa")
			.add(Projections.property("tc.pcReajVlExcedente"), "pcReajVlExcedente")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco_nmParcelaPreco");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tc")
			.add(Restrictions.eq("tc.parametroCliente.id", idParametroCliente))
			.setProjection(pl)
			.createAlias("tc.parcelaPreco", "pp")
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	public List<TaxaCliente> findTaxaClienteByIdParamCliente(Long idParamCliente){
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("parametroCliente.idParametroCliente", idParamCliente));
		return findByCriterion(criterions, null);
	}
	
	public ResultSetPage findPaginatedByParametroClienteProposta(Long idParamentoCliente, FindDefinition fd) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("tc.idTaxaCliente"), "idTaxaCliente")
			.add(Projections.property("tc.tpTaxaIndicador"), "tpTaxaIndicador")
			.add(Projections.property("tc.vlTaxa"), "vlTaxa")
			.add(Projections.property("tc.psMinimo"), "psMinimo")
			.add(Projections.property("tc.vlExcedente"), "vlExcedente")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco.nmParcelaPreco")
			.add(Projections.property("tc.pcReajTaxa"), "pcReajTaxa")
			.add(Projections.property("tc.pcReajVlExcedente"), "pcReajVlExcedente");
		DetachedCriteria dc = getCriteria(idParamentoCliente)
			.setProjection(pl)
			.createAlias("tc.parcelaPreco", "pp")
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TaxaCliente.class))
			.addOrder(OrderVarcharI18n.asc("pp.nmParcelaPreco", LocaleContextHolder.getLocale()));
		return findPaginatedByDetachedCriteria(dc, fd.getCurrentPage(), fd.getPageSize());
	}
	
	public Integer getRowCountByParametroClienteProposta(Long idParamentoCliente) {
		DetachedCriteria dc = getCriteria(idParamentoCliente).setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	public List findIdsByTabelasPrecoIdSimulacao(Long idSimulacao, List idParcelaPrecos) {
		StringBuilder hql = new StringBuilder()
			.append(" select tc.idTaxaCliente \n")
			.append("   from ").append(getPersistentClass().getName()).append(" tc \n")
			.append("  where tc.parametroCliente.id in ( \n")
			.append("        select pc.idParametroCliente \n")
			.append("          from ").append(ParametroCliente.class.getName()).append(" pc \n")
			.append("         where pc.simulacao.id = :idSimulacao) \n");
		
		List<String> paramNames = new ArrayList<String>();
		List<Object> paramValues = new ArrayList<Object>();
		paramNames.add("idSimulacao");
		paramValues.add(idSimulacao);
		if (idParcelaPrecos != null && !idParcelaPrecos.isEmpty()) {
			hql.append("    and tc.parcelaPreco.id in (:idParcelaPrecos) \n");
			paramNames.add("idParcelaPrecos");
			paramValues.add(idParcelaPrecos);
		}
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), paramNames.toArray(new String[] {}), paramValues.toArray());
	}
	
	public void updatePercValorTaxaAndPercValorExcedente(Long id, BigDecimal taxaValor, BigDecimal taxaPercentual, BigDecimal excedenteValor, BigDecimal excedentePercentual){
		StringBuilder sql = new StringBuilder()
									.append(" UPDATE TAXA_CLIENTE SET ")
									.append("   VL_TAXA = ").append(taxaValor)
									.append(" , PC_REAJ_TAXA = ").append(taxaPercentual) 
									.append(" , VL_EXCEDENTE = ").append(excedenteValor)
									.append(" , PC_REAJ_VL_EXCEDENTE = ").append(excedentePercentual)
									.append(" WHERE ID_TAXA_CLIENTE = ").append(id);
		
		jdbcTemplate.update(sql.toString());
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}