package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServicoDAO extends BaseCrudDao<Servico, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return Servico.class;
	}
	
	@Override
	public List findListByCriteria(Map criterions) {
		if (criterions == null) criterions = new HashMap(1);
		List order = new ArrayList(1);
		order.add("dsServico");
		return super.findListByCriteria(criterions, order);
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tipoServico", FetchMode.JOIN);
	}
	
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindPaginatedLazyProperties(java.util.Map)
	 */
	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("tipoServico", FetchMode.JOIN);
	}
	
	public List findIdsServicosAtivos() {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setProjection(Projections.property("idServico"));
		dc.add(Restrictions.eq("tpSituacao","A"));
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public List findIdsServicosAtivosByModal(String tpModal) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "s")
			.setProjection(Projections.projectionList()
					.add(Projections.property("s.idServico"), "idServico")
					.add(Projections.property("s.dsServico"), "dsServico"))
			.add(Restrictions.eq("s.tpSituacao","A"))
			.add(Restrictions.eq("s.tpModal",tpModal))
			.addOrder(OrderVarcharI18n.asc("s.dsServico", LocaleContextHolder.getLocale()))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}
	
	public List findByTpAbrangencia(String tpAbrangencia) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "s")
		.setProjection(
				Projections.projectionList()
				.add(Projections.property("s.idServico"), "idServico")
				.add(Projections.property("s.dsServico"), "dsServico")
				.add(Projections.property("s.sgServico"), "sgServico")
				.add(Projections.property("s.tpModal"), "tpModal")
				.add(Projections.property("s.tpAbrangencia"), "tpAbrangencia"))
				.add(Restrictions.eq("s.tpSituacao","A"));
		if(StringUtils.isNotBlank(tpAbrangencia))		
			dc = dc.add(Restrictions.eq("s.tpAbrangencia", tpAbrangencia));
		dc = dc.addOrder(OrderVarcharI18n.asc("s.dsServico", LocaleContextHolder.getLocale()))
			   .setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}
	
	/**
	 * Busca o servico do documento de servico informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 09/05/2006
	 * 
	 * @param Long idDoctoServico
	 * @return Servico
	 * */
	public Servico findByDoctoServico(Long idDoctoServico) {
		SqlTemplate hql = mountHql(idDoctoServico);
		
		hql.addProjection("ser");
		
		List lstServico = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (lstServico.size() == 1){
			return (Servico)lstServico.get(0);
		} else {
			return null;
		}
	}	
	
	private SqlTemplate mountHql(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addInnerJoin(DoctoServico.class.getName(), "doc");
		hql.addInnerJoin("doc.servico", "ser");
		hql.addCriteria("doc.id", "=", idDoctoServico);
		return hql;
	}
	
	/**
	 * Retorna um Servico que corresponde à sigla informada.
	 * @param sigla
	 * @return
	 */
	public Servico findServicoBySigla(String sigla) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "s")
		.add(Restrictions.eq("s.sgServico", sigla));
		return (Servico)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	/**
	 * Busca uma entidade servico de acordo com os filtros
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 18/01/2007
	 *
	 * @param idTipoServico
	 * @param tpAbrangencia
	 * @param tpModal
	 * @return
	 *
	 */
	public Servico findServicoByIdTpServTpAbrangTpModal( String idTipoServico, String tpAbrangencia, String tpModal ){

		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection(" s ");
		
		hql.addInnerJoin(getPersistentClass().getName() + " s");
		hql.addInnerJoin("s.tipoServico ts");
		
		hql.addCriteria("ts.id", "=", idTipoServico);
		hql.addCriteria("s.tpAbrangencia", "=", tpAbrangencia);
		hql.addCriteria("s.tpModal", "=", tpModal);
		
		return (Servico) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	public Servico findServicoByTpAbrangTpModal(String tpAbrangencia, String tpModal ){
	 
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" s ");
		
		hql.addInnerJoin(getPersistentClass().getName() + " s");

		hql.addCriteria("s.tpAbrangencia", "=", tpAbrangencia);
		hql.addCriteria("s.tpModal", "=", tpModal);
		
		return (Servico) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	public List findServicoByTpSituacaoTpModalTpAbrangencia(String tpSituacao, String tpModal, String tpAbrangencia ){
		 
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" new Map (s.idServico as idServico, s.dsServico as dsServico ) ");
		
		hql.addInnerJoin(getPersistentClass().getName() + " s");

		hql.addCriteria("s.tpSituacao", "=", tpSituacao);
		hql.addCriteria("s.tpModal", "=", tpModal);
		hql.addCriteria("s.tpAbrangencia", "=", tpAbrangencia);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	
	}
	
	public Servico findServicoByFluxoFilial() {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "s")
		.add(Restrictions.eq("s.tpSituacao", "A"))
		.add(Restrictions.eq("s.tpModal", "R"))
		.add(Restrictions.eq("s.tpAbrangencia", "N"));
		return (Servico)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public List<Servico> findChosen() {
		String hql = "select s from " + getPersistentClass().getName() + " as s ";
		return getAdsmHibernateTemplate().find(hql);
	}

}
