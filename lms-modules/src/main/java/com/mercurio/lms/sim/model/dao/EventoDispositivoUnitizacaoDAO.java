package com.mercurio.lms.sim.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.DateTime;
import org.springframework.dao.DeadlockLoserDataAccessException;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.sim.model.EventoDispositivoUnitizacao;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoDispositivoUnitizacaoDAO extends BaseCrudDao<EventoDispositivoUnitizacao, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<EventoDispositivoUnitizacao> getPersistentClass() {
		return EventoDispositivoUnitizacao.class;
	}

	public List<EventoDispositivoUnitizacao> findByEventoByDispositivo(Long idEvento, Long idDispositivoUnitizacao) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("evento.idEvento", idEvento));
		dc.add(Restrictions.eq("dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao));
		dc.add(Restrictions.eq("blEventoCancelado", Boolean.FALSE));
		dc.addOrder(Order.desc("dhEvento"));

		return findByDetachedCriteria(dc);
	}

	public List<EventoDispositivoUnitizacao> findEventoDispositivoUnitizacao(Long idDispositivoUnitizacao, Short cdEvento, DomainValue tpEvento, Boolean blEventoCancelado) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev");
		dc.createAlias("ev.evento", "e");
		dc.add(Restrictions.eq("ev.dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao));;
		dc.add(Restrictions.eq("ev.blEventoCancelado", blEventoCancelado));
		dc.add(Restrictions.eq("e.cdEvento", cdEvento));
		dc.add(Restrictions.eq("e.tpEvento", tpEvento));
		return findByDetachedCriteria(dc);
	}
	
	public List<EventoDispositivoUnitizacao> findEventosDispositivoUnitizacao(Long idDispositivoUnitizacao, Short cdEvento, DomainValue tpScan, Boolean blEventoCancelado) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev");
		dc.createAlias("ev.evento", "e");
		dc.add(Restrictions.eq("ev.dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao));;
		dc.add(Restrictions.eq("ev.blEventoCancelado", blEventoCancelado));
		dc.add(Restrictions.eq("e.cdEvento", cdEvento));
		dc.add(Restrictions.eq("ev.tpScan", tpScan));
		return findByDetachedCriteria(dc);
	}

	/**
	 * Procura evento(s) no dispositivoUnitizacao informado que seja igual ao código do evento
	 * passado por parâmetro.
	 * @param idDispositivoUnitizacao
	 * @param cdEvento
	 * @return
	 */
	public List<EventoDispositivoUnitizacao> findEventoDispositivoUnitizacao(Long idDispositivoUnitizacao, Short cdEvento) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev");
		dc.createAlias("ev.evento", "e");
		dc.add(Restrictions.eq("ev.dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao));;
		dc.add(Restrictions.eq("e.cdEvento", cdEvento));
		return findByDetachedCriteria(dc);
	}

	/**
	 * Procura evento(s) no dispositivoUnitizacao informado que seja igual ao código e a filial do evento informado. 
	 * passado por parâmetro.
	 * @param idDispositivoUnitizacao
	 * @param cdEvento
	 * @return
	 */
	public List<EventoDispositivoUnitizacao> findEventoDispositivoUnitizacao(Long idDispositivoUnitizacao, Long idFilialEvento, Short[] cdEvento) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev");
		dc.createAlias("ev.evento", "e");
		dc.add(Restrictions.eq("ev.dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao));
		dc.add(Restrictions.eq("ev.filial.id", idFilialEvento));
		dc.add(Restrictions.in("e.cdEvento", cdEvento));
		return findByDetachedCriteria(dc);
	}

	public List<Long> findIdsByIdDispositivo(Long idDispositivoUnitizacao) {
		String sql = "select pojo.idEventoDispositivoUnitizacao " +
			"from "+ EventoDispositivoUnitizacao.class.getName() + " as pojo " +
			"join pojo.dispositivoUnitizacao as du " +
			"where du.idDispositivoUnitizacao = :idDispositivoUnitizacao ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idDispositivoUnitizacao", idDispositivoUnitizacao);
	}

	/**
	 * Busca Ultimo Evento do Dispositivo
	 * @param idDispositivoUnitizacao
	 */
	public EventoDispositivoUnitizacao findUltimoEventoDispositivoUnitizacao(Long idDispositivoUnitizacao, String tpEvento, Boolean blEventoCancelado) {
		//Busca data do último evento
		DetachedCriteria dcMax = DetachedCriteria.forClass(getPersistentClass(), "evb");
		dcMax.setProjection(Projections.max("evb.dhEvento.value"));
		dcMax.createAlias("evb.evento", "eb");
		dcMax.add(Restrictions.eqProperty("evb.dispositivoUnitizacao.idDispositivoUnitizacao", "eva.dispositivoUnitizacao.idDispositivoUnitizacao"));
		dcMax.add(Restrictions.eqProperty("evb.blEventoCancelado", "eva.blEventoCancelado"));
		dcMax.add(Restrictions.eqProperty("eb.tpEvento", "ea.tpEvento"));

		//Busca EventoDispositivoUnitizacao
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eva");
		dc.createAlias("eva.evento", "ea");
		dc.add(Restrictions.eq("eva.dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao));
		dc.add(Restrictions.eq("eva.blEventoCancelado", blEventoCancelado));
		dc.add(Restrictions.eq("ea.tpEvento", tpEvento));
		dc.add(Subqueries.propertyEq("eva.dhEvento.value", dcMax));

		return (EventoDispositivoUnitizacao)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	/**
	 * Busca Ultimo Evento do Dispositivo
	 * @param idDispositivoUnitizacao
	 */
	public EventoDispositivoUnitizacao findUltimoEventoDispositivoUnitizacaoOnSession(Long idDispositivoUnitizacao, String tpEvento, Boolean blEventoCancelado) {
		//Busca data do último evento
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "evb");
		dc.setProjection(Projections.max("evb.dhEvento.value"));
		dc.createAlias("evb.evento", "eb");
		dc.add(Restrictions.eqProperty("evb.dispositivoUnitizacao.idDispositivoUnitizacao", "eva.dispositivoUnitizacao.idDispositivoUnitizacao"));
		if(blEventoCancelado != null){
			dc.add(Restrictions.eqProperty("evb.blEventoCancelado", "eva.blEventoCancelado"));
		}
		if(tpEvento != null){
			dc.add(Restrictions.eqProperty("eb.tpEvento", "ea.tpEvento"));
		}
		
		//Busca EventoDispositivoUnitizacao
		Criteria c = getSession().createCriteria(getPersistentClass(), "eva");
		c.createAlias("eva.evento", "ea");
		c.setFetchMode("eva.evento", FetchMode.JOIN);
		c.add(Restrictions.eq("eva.dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao));
		if(blEventoCancelado != null){
			c.add(Restrictions.eq("eva.blEventoCancelado", blEventoCancelado));
		}
		if(tpEvento != null){
			c.add(Restrictions.eq("ea.tpEvento", tpEvento));
		}
		c.add(Subqueries.propertyEq("eva.dhEvento.value", dc));

		return (EventoDispositivoUnitizacao) (c.list() != null && !c.list().isEmpty() ? c.list().get(0) : null);
	}

	public void removeByIdDispositivo(Serializable idDispositivoUnitizacao) {
		String sql = "delete from " + EventoDispositivoUnitizacao.class.getName() + " as ev where ev.dispositivoUnitizacao.idDispositivoUnitizacao = :idDispositivoUnitizacao";
		getAdsmHibernateTemplate().removeById(sql, idDispositivoUnitizacao);
	}
	
	/**
	 * Método que retorna a maior dhEvento da tabela de EventoDispositivoUnitizacao com 
	 * o ID do Dispositivo e IDs de LocalizacaoMercadoria
	 * @param idDispositivoUnitizacao
	 * @param idLocalizacaoMercadoria
	 * @return
	 */
	public DateTime findMaiorDhEventoByIdDispositivoByIdsLocalizacaoMercadoria(Long idDispositivoUnitizacao, List<Long> idsLocalizacaoMercadoria) {
		StringBuffer hql = new StringBuffer();

		hql.append(" select max(ev.dhEvento.value) \n");
		hql.append("   from " + EventoDispositivoUnitizacao.class.getName() + " as ev \n");
		hql.append("   join ev.evento as evn \n");
		hql.append("  where ev.dispositivoUnitizacao.idDispositivoUnitizacao = :idDispositivoUnitizacao \n");
		hql.append("    and evn.localizacaoMercadoria.idLocalizacaoMercadoria" +
								" in (:idsLocalizacaoMercadoria) \n");

		Query query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
		query.setParameter("idDispositivoUnitizacao", idDispositivoUnitizacao);
			query.setParameterList("idsLocalizacaoMercadoria", idsLocalizacaoMercadoria);

		return (DateTime) query.uniqueResult();
	}
	
	/**
	 * Método que busca todos os eventos do documento de serviço não cancelados cujo evento 
	 * associado possua localização associada
	 * 
	 * @param Long idDispositivoUnitizacao
	 * @return
	 */
	public List<EventoDispositivoUnitizacao> findEventosDispositivoNaoCancelados(Long idDispositivoUnitizacao){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev")
			.createAlias("ev.dispositivoUnitizacao", "vol")
			.createAlias("ev.evento", "e")
			.add(Restrictions.eq("ev.blEventoCancelado", Boolean.FALSE))
			.add(Restrictions.eq("vol.idDispositivoUnitizacao", idDispositivoUnitizacao))
			.add(Restrictions.isNotNull("e.localizacaoMercadoria.idLocalizacaoMercadoria"));
		dc.addOrder(Order.desc("ev.dhEvento.value"));
		return findByDetachedCriteria(dc);
	}

	/**
	 * find para buscar o Evento do Documento de Serviço conforme os critérios enviados
	 * 
	 * @param nrDocumento
	 * @param cdEvento
	 * @param idFilial
	 * @param dhEvento
	 * 
	 * @return EventoDispositivoUnitizacao
	 */
	public EventoDispositivoUnitizacao findEventoDispositivoUnitizacao(String nrDocumento, Short cdEvento, Long idFilial, DateTime dhEvento) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev")
			.createAlias("ev.dispositivoUnitizacao", "vol")
			.createAlias("ev.evento", "e")
			.createAlias("ev.filial", "f")
			.add(Restrictions.eq("ev.nrDocumento", nrDocumento))
			.add(Restrictions.eq("e.cdEvento", cdEvento))
			.add(Restrictions.eq("f.idFilial", idFilial))
			.add(Restrictions.eq("ev.dhEvento.value", dhEvento));
		return (EventoDispositivoUnitizacao)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Retorna a lista de eventos não cancelados do documento informado filtrando por o código do evento
	 * 
	 * @param idDispositivoUnitizacao
	 * @param cdEvento
	 * 
	 * @return
	 */
	public List<EventoDispositivoUnitizacao> findEventoDispositivoUnitizacao(Long idDispositivoUnitizacao, Short[] cdEvento) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("e");
		hql.addFrom(EventoDispositivoUnitizacao.class.getName(), "e");
		hql.addCriteria("e.dispositivoUnitizacao.idDispositivoUnitizacao", "=", idDispositivoUnitizacao);
		hql.addCriteriaIn("e.evento.cdEvento", cdEvento);
		hql.addCriteria("e.blEventoCancelado", "=", Boolean.FALSE);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

    /**
     * 
     * @param idDispositivoUnitizacao
     * @param blOrigemCancelamentoRIM
     * @return
     */
    public EventoDispositivoUnitizacao findEventoDispositivoUnitizacaoByLastDhEventoByIdDispositivo(Long idDispositivoUnitizacao, Boolean blOrigemCancelamentoRIM) {
		StringBuffer hql = new StringBuffer()
		.append(" select ev ")
		.append("from ").append(EventoDispositivoUnitizacao.class.getName()).append(" as ev ")
		.append("inner join fetch ev.evento as evento ")
		.append("where ev.dispositivoUnitizacao.idDispositivoUnitizacao = ? ");

		if (blOrigemCancelamentoRIM != null && blOrigemCancelamentoRIM) {
			hql.append("and evento.cdEvento in (35, 132) ");
		}

		hql.append("order by ev.dhEvento.value desc ");

		List<EventoDispositivoUnitizacao> lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idDispositivoUnitizacao});
		if (lista.isEmpty())
			return null;

		return lista.get(0);
	}

	public ResultSetPage<EventoDispositivoUnitizacao> findPaginatedByIdDispositivo(Long id, FindDefinition findDefinition) {
		List<Object> param = new ArrayList();
    	
    	StringBuilder sql = new StringBuilder()
    	.append("from ")
    	.append(EventoDispositivoUnitizacao.class.getName()).append(" as evtDisp ")
    	.append("left join fetch evtDisp.evento as evt ")
    	.append("left join fetch evt.descricaoEvento as dsEvt ")
    	.append("left join fetch evtDisp.usuario as usu ")
    	.append("left join fetch evtDisp.filial as fil ")
    	.append("where ")
    	.append("evtDisp.dispositivoUnitizacao.idDispositivoUnitizacao = ? ")
    	.append("order by evtDisp.dhEvento.value desc ");
		
    	param.add(id);
    	    	
    	return getAdsmHibernateTemplate().findPaginated(
    			sql.toString(), sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
	}
	
	public ResultSetPage<EventoDispositivoUnitizacao> findPaginated(PaginatedQuery paginatedQuery) {
		Map<String, Object> criteria = paginatedQuery.getCriteria();				
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, this.getHqlPaginated(criteria));
	}
			
	@Override		
	public Integer getRowCount(Map criteria) {				
		return getAdsmHibernateTemplate().getRowCountForQuery(this.getHqlPaginated(criteria), criteria);
	}
	
	public String getHqlPaginated(Map<String,Object> criteria) {
		StringBuilder hql = new StringBuilder();
		hql.append("from " + EventoDispositivoUnitizacao.class.getName() + " as eventoDispositivo ");
    	hql.append("left join fetch eventoDispositivo.evento as evento ");
    	hql.append("left join fetch evento.descricaoEvento as descricaoEvento ");
    	hql.append("left join fetch eventoDispositivo.usuario as usuario ");
    	hql.append("left join fetch eventoDispositivo.filial as filial ");      	
		hql.append("where 1=1 ");
		
		List param = new ArrayList();
		if(criteria.get("idDispositivoUnitizacao") != null) {
			hql.append("and eventoDispositivo.dispositivoUnitizacao.id = :idDispositivoUnitizacao ");			
		}
    	hql.append("order by eventoDispositivo.dhEvento.value ");						
		return hql.toString();
	}
}