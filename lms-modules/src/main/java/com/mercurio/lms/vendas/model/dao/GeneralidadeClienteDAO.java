package com.mercurio.lms.vendas.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
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
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class GeneralidadeClienteDAO extends BaseCrudDao<GeneralidadeCliente, Long> {

	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    @Override
	protected final Class getPersistentClass() {
        return GeneralidadeCliente.class;
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

	public GeneralidadeCliente findGeneralidadeCliente(
		Long idTabelaPreco,
		Long idParcelaPreco,
		Long idDivisaoCliente,
		Long idServico,
		RestricaoRota restricaoRotaOrigem,
		RestricaoRota restricaoRotaDestino
	) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "gc");

		dc.createAlias("gc.parametroCliente", "pc");
		dc.createAlias("pc.tabelaDivisaoCliente", "tdc");

		dc.add(Restrictions.eq("tdc.tabelaPreco.id", idTabelaPreco));
		dc.add(Restrictions.eq("tdc.divisaoCliente.id", idDivisaoCliente));
		dc.add(Restrictions.eq("tdc.servico.id", idServico));
		dc.add(Restrictions.eq("gc.parcelaPreco.id", idParcelaPreco));

		ParametroClienteUtils.addParametroClienteRestricaoRota(dc, restricaoRotaOrigem, restricaoRotaDestino);

		List result = findPaginatedByDetachedCriteria(dc, IntegerUtils.ONE, IntegerUtils.ONE).getList();
		if(result.size() == 1) {
			return (GeneralidadeCliente) result.get(0);
		}
		return null;
	}
	
	public List<GeneralidadeCliente> findGeneralidadeClienteByIdParamCliente(Long idParamCliente){
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("parametroCliente.idParametroCliente", idParamCliente));
		return findByCriterion(criterions, null);
	}

	public ResultSetPage findPaginatedByParametroCliente(Long idParamentoCliente, FindDefinition fd) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("gc.idGeneralidadeCliente"), "idGeneralidadeCliente")
			.add(Projections.property("gc.tpIndicador"), "tpIndicador")
			.add(Projections.property("gc.vlGeneralidade"), "vlGeneralidade")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco.nmParcelaPreco")
			.add(Projections.property("m.dsSimbolo"), "dsSimbolo")
			.add(Projections.property("gc.pcReajGeneralidade"), "pcReajGeneralidade");
		DetachedCriteria dc = getCriteria(idParamentoCliente)
			.setProjection(pl)
			.createAlias("gc.parcelaPreco", "pp")
			.createAlias("gc.parametroCliente", "pc")
			.createAlias("pc.tabelaDivisaoCliente", "tdc")
			.createAlias("tdc.tabelaPreco", "tab")
			.createAlias("tab.moeda", "m")
			.setResultTransformer(new AliasToNestedBeanResultTransformer(GeneralidadeCliente.class))
			.addOrder(OrderVarcharI18n.asc("pp.nmParcelaPreco", LocaleContextHolder.getLocale()));
		return findPaginatedByDetachedCriteria(dc, fd.getCurrentPage(), fd.getPageSize());
	}
	public Integer getRowCountByParametroCliente(Long idParamentoCliente) {
		DetachedCriteria dc = getCriteria(idParamentoCliente).setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	private DetachedCriteria getCriteria(Long idParamentoCliente) {
		return DetachedCriteria.forClass(GeneralidadeCliente.class, "gc")
		.add(Restrictions.eq("gc.parametroCliente.id", idParamentoCliente));
	}
	
	public Map findGeneralidadeClienteById(Long id) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("gc.idGeneralidadeCliente"), "idGeneralidadeCliente")
			.add(Projections.property("gc.tpIndicador"), "tpIndicador")
			.add(Projections.property("gc.tpIndicadorMinimo"), "tpIndicadorMinimo")
			.add(Projections.property("gc.vlMinimo"), "vlMinimo")
			.add(Projections.property("gc.pcReajMinimo"), "pcReajMinimo")
			.add(Projections.property("gc.vlGeneralidade"), "vlGeneralidade")
			.add(Projections.property("gc.pcReajGeneralidade"), "pcReajGeneralidade")
			.add(Projections.property("gc.parametroCliente.id"), "idParametroCliente")
			.add(Projections.property("pp.idParcelaPreco"), "parcelaPreco_idParcelaPreco")
			.add(Projections.property("pp.cdParcelaPreco"), "parcelaPreco_cdParcelaPreco")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco_nmParcelaPreco");
		final DetachedCriteria dc = DetachedCriteria.forClass(GeneralidadeCliente.class, "gc")
			.setProjection(pl)
			.createAlias("gc.parcelaPreco", "pp")
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
			.add(Projections.property("tc.idGeneralidadeCliente"), "idGeneralidadeCliente")
			.add(Projections.property("tc.tpIndicador"), "tpIndicador")
			.add(Projections.property("tc.vlGeneralidade"), "vlGeneralidade")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco_nmParcelaPreco");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tc")
			.add(Restrictions.eq("tc.parametroCliente.id", idParametroCliente))
			.setProjection(pl)
			.createAlias("tc.parcelaPreco", "pp")
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}
	
	public ResultSetPage findPaginatedByParametroClienteProposta(Long idParamentoCliente, FindDefinition fd) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("gc.idGeneralidadeCliente"), "idGeneralidadeCliente")
			.add(Projections.property("gc.tpIndicador"), "tpIndicador")
			.add(Projections.property("gc.vlGeneralidade"), "vlGeneralidade")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco.nmParcelaPreco");
		DetachedCriteria dc = getCriteria(idParamentoCliente)
			.setProjection(pl)
			.createAlias("gc.parcelaPreco", "pp")
			.setResultTransformer(new AliasToNestedBeanResultTransformer(GeneralidadeCliente.class))
			.addOrder(OrderVarcharI18n.asc("pp.nmParcelaPreco", LocaleContextHolder.getLocale()));
		return findPaginatedByDetachedCriteria(dc, fd.getCurrentPage(), fd.getPageSize());
	}
	
	public Integer getRowCountByParametroClienteProposta(Long idParamentoCliente) {
		DetachedCriteria dc = getCriteria(idParamentoCliente).setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	public List findByIdParametroClienteProposta(Long idParametroCliente) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("gc.idGeneralidadeCliente"), "idGeneralidadeCliente")
			.add(Projections.property("gc.tpIndicador"), "tpIndicador")
			.add(Projections.property("gc.vlGeneralidade"), "vlGeneralidade")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco.nmParcelaPreco")
			.add(Projections.property("pp.idParcelaPreco"), "parcelaPreco.idParcelaPreco");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "gc")
			.add(Restrictions.eq("gc.parametroCliente.id", idParametroCliente))
			.setProjection(pl)
			.createAlias("gc.parcelaPreco", "pp")
			.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		return findByDetachedCriteria(dc);
	}
	
	public List findIdsByTabelasPrecoIdSimulacao(Long idSimulacao, List idParcelaPrecos) {
		StringBuilder hql = new StringBuilder()
			.append(" select gc.idGeneralidadeCliente \n")
			.append("   from ").append(getPersistentClass().getName()).append(" gc \n")
			.append("  where gc.parametroCliente.id in ( \n")
			.append("        select pc.idParametroCliente \n")
			.append("          from ").append(ParametroCliente.class.getName()).append(" pc \n")
			.append("         where pc.simulacao.id = :idSimulacao) \n");
		
		List<String> paramNames = new ArrayList<String>();
		List<Object> paramValues = new ArrayList<Object>();
		paramNames.add("idSimulacao");
		paramValues.add(idSimulacao);
		if (idParcelaPrecos != null && !idParcelaPrecos.isEmpty()) {
			hql.append("    and gc.parcelaPreco.id in (:idParcelaPrecos) \n");
			paramNames.add("idParcelaPrecos");
			paramValues.add(idParcelaPrecos);
		}
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), paramNames.toArray(new String[] {}), paramValues.toArray());
	}

	public void updatePercValorAndPercValorMin(Long id, BigDecimal percentual, BigDecimal minimoPercentual, BigDecimal valor, BigDecimal minimoValor){
		StringBuilder sql = new StringBuilder()
									.append(" UPDATE GENERALIDADE_CLIENTE SET ")
									.append("   VL_GENERALIDADE = ").append(valor)
									.append(" , PC_REAJ_GENERALIDADE = ").append(percentual) 
									.append(" , VL_MINIMO = ").append(minimoValor)
									.append(" , PC_REAJUSTE_MINIMO = ").append(minimoPercentual)
									.append(" WHERE ID_GENERALIDADE_CLIENTE = ").append(id);
		
		jdbcTemplate.update(sql.toString());
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}