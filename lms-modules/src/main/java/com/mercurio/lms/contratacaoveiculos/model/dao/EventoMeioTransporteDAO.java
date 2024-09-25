package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class EventoMeioTransporteDAO extends BaseCrudDao<EventoMeioTransporte, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EventoMeioTransporte.class;
    }

    protected void initFindListLazyProperties(Map arg0) {
        arg0.put("meioTransporte", FetchMode.JOIN);
    }

    public ResultSetPage findEventosPaginated(TypedFlatMap criteria, FindDefinition fdef){
    	SqlTemplate hql = this.getSqlTemplate(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),fdef.getCurrentPage(),fdef.getPageSize(),hql.getCriteria());
    }

    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	SqlTemplate hql = this.getSqlTemplate(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
    }

    private SqlTemplate getSqlTemplate(TypedFlatMap criteria) {
    	SqlTemplate hql = new SqlTemplate();

    	hql.addFrom(new StringBuffer().append(EventoMeioTransporte.class.getName()).append(" as E ")
    			.append("  left join fetch E.filial as F ")
    			.append("  left join fetch F.pessoa ")
    			.append(" inner join fetch E.meioTransporte as MT ")
    			.append("  left join fetch E.pontoParadaTrecho as PPT ")
    			.append("  left join fetch PPT.pontoParada ")
    			.append("  left join fetch PPT.trechoRotaIdaVolta TRIV ")
    			.append("  left join fetch TRIV.filialRotaByIdFilialRotaOrigem as FTO ")
    			.append("  left join fetch TRIV.filialRotaByIdFilialRotaDestino as FTD ")
    			.append("  left join fetch FTO.filial ")
    			.append("  left join fetch FTD.filial ")
    			.append("  left join fetch E.controleTrecho as CT ")
    			.append("  left join fetch CT.controleCarga as CC ")
    			.append("  left join fetch CC.filialByIdFilialOrigem ")
    			.append("  left join fetch CC.filialByIdFilialDestino ")
    			.toString());

    	hql.addCriteria("F.idFilial","=",criteria.getLong("filial.idFilial"));

    	hql.addCriteria("MT.idMeioTransporte","=",criteria.getLong("meioTransporte.idMeioTransporte"));

    	hql.addCriteria("trunc(cast(E.dhInicioEvento.value as date))",">=",criteria.getYearMonthDay("dtInicioEvento"));

    	if (criteria.getYearMonthDay("dtFimEvento")!= null) {
    		hql.addCustomCriteria("(trunc(cast(E.dhFimEvento.value as date)) <= ? or E.dhFimEvento.value is null)",
    				criteria.getYearMonthDay("dtFimEvento"));
    	}

    	hql.addCriteria("E.tpSituacaoMeioTransporte","=",criteria.getString("tpSituacaoMeioTransporte"));

    	hql.addOrderBy("MT.nrFrota,E.dhInicioEvento.value");

    	return hql;
    }

    /**
     * Retorna o último evento de um meio de transporte.
     *
     * @param idMeioTransporte
     * @return instância de EventoMeioTransporte
     */
	public EventoMeioTransporte findUltimoEventoVigenteComFinalNulo(Long idMeioTransporte) {
		StringBuilder query = new StringBuilder()
				.append(" SELECT EMT ")
				.append(" FROM " + getPersistentClass().getName() + " EMT ")
				.append(" WHERE ")
				.append(" EMT.meioTransporte.id = ").append(idMeioTransporte)
				.append(" AND EMT.dhFimEvento IS NULL ")
				.append(" AND trunc(cast(EMT.dhInicioEvento.value as date)) > trunc(sysdate-30) ")
				.append(" ORDER BY EMT.dhInicioEvento DESC ");

		List<EventoMeioTransporte> result = getAdsmHibernateTemplate().find(query.toString());
		return (result.isEmpty() ? null : result.get(0));
    }

	/**
     * Retorna o evento de um meio de transporte para um carragamento especifico.
     * 
     * @param idMeioTransporte
     * @param idFilial
     * @param idControleCarga
     * @param tpSituacaoMeioTransporte
     * @return
     */
    public EventoMeioTransporte findLastEventoMeioTransporteToMeioTransporte(
    		Long idMeioTransporte, Long idFilial, Long idControleCarga, String tpSituacaoMeioTransporte){
    	
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass())
    		.addOrder(Order.desc("dhInicioEvento"));
    		
    	detachedCriteria.add(Restrictions.eq("meioTransporte.id", idMeioTransporte));
    	detachedCriteria.add(Restrictions.eq("filial.idFilial", idFilial));
    	detachedCriteria.add(Restrictions.eq("controleCarga.idControleCarga", idControleCarga));
    	detachedCriteria.add(Restrictions.eq("tpSituacaoMeioTransporte", tpSituacaoMeioTransporte));
    	List eventos = getAdsmHibernateTemplate().findByDetachedCriteria(detachedCriteria);
    	if (eventos.size()>0){
    		return (EventoMeioTransporte)eventos.get(0);
    	}
    	return null;
    	
    }
    

    /**
     * Retorna o último evento registrado.
     * 
     * @param idMeioTransporte
     * @param idControleCarga
     * @return
     */
    public EventoMeioTransporte findLastEventoMeioTransporte(Long idMeioTransporte, Long idControleCarga){
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass())
    		.addOrder(Order.desc("dhInicioEvento"));

    	detachedCriteria.add(Restrictions.eq("meioTransporte.id", idMeioTransporte));
    	detachedCriteria.add(Restrictions.eq("controleCarga.idControleCarga", idControleCarga));
    	List eventos = getAdsmHibernateTemplate().findByDetachedCriteria(detachedCriteria);
    	if (eventos.size()>0){
    		return (EventoMeioTransporte)eventos.get(0);
    	}
    	return null;
    }

    /**
     * Retorna o último evento registrado pelo Meio Tranporte.
     * 
     * @param idMeioTransporte
     * @return
     */
    public EventoMeioTransporte findLastEventoMeioTransporteByMeioTransporte(Long idMeioTransporte){
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass())
    		.addOrder(Order.desc("dhInicioEvento"));
    
    	detachedCriteria.add(Restrictions.eq("meioTransporte.id", idMeioTransporte));
    	List eventos = getAdsmHibernateTemplate().findByDetachedCriteria(detachedCriteria);
    	if (eventos.size()>0){
    		return (EventoMeioTransporte)eventos.get(0);
    	}
    	return null;
    }
    
    
    
    /**
	 * Busca um Evento de Meio de Transporte a partir dos parâmetros informados.
	 * 
	 * @param idMeioTransporte Identificador do Meio de transporte.
	 * @param tpSituacaoMeioTransporte Domínio da situação do meio de transporte.
	 * @param dhInicioEvento Data/Hora de início do evento.
	 * @return uma instância de EventoMeioTransporte caso encontrado. Senão, retora null.
	 */
	public EventoMeioTransporte findEventoMeioTransporte(Long idMeioTransporte,
			String tpSituacaoMeioTransporte, DateTime dhInicioEvento) {
		DateTime data = dhInicioEvento;
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"EMT");
		dc.add(Restrictions.eq("EMT.meioTransporte.id",idMeioTransporte));
		dc.add(Restrictions.eq("EMT.tpSituacaoMeioTransporte",tpSituacaoMeioTransporte));
		dc.add(Restrictions.eq("EMT.dhInicioEvento.value",dhInicioEvento));
		
		return (EventoMeioTransporte) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
    
	public EventoMeioTransporte getEventoMeioTransporte(Long idMeioTransporte,
														String tpSituacaoMeioTransporte, 
														DateTime dhInicioEvento) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(getPersistentClass().getName() + " EMT");
		hql.addCriteria("EMT.meioTransporte.id", "=", idMeioTransporte);
		hql.addCriteria("EMT.tpSituacaoMeioTransporte", "=", tpSituacaoMeioTransporte);
		hql.addCriteria("cast(EMT.dhInicioEvento.value as date)", "=", dhInicioEvento);
		List result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if(result.isEmpty()){
			return null;
		}
		return (EventoMeioTransporte) result.get(0);
	}
    
	public ResultSetPage<EventoMeioTransporte> findPaginatedByIdMeioTransporte(Long id, FindDefinition findDefinition) {
		List<Object> param = new ArrayList();
    	
    	StringBuilder sql = new StringBuilder()
    	.append("from ")
    	.append(EventoMeioTransporte.class.getName()).append(" as evtMeio ")
    	.append("left join fetch evtMeio.box as box ")
    	.append("left join fetch evtMeio.filial as fil ")
    	.append("left join fetch fil.pessoa as pes ")
    	.append("left join fetch evtMeio.controleTrecho as ctoTre ")
    	.append("left join fetch ctoTre.filialByIdFilialOrigem as filOriCto ")
    	.append("left join fetch ctoTre.filialByIdFilialDestino as filDesCto ")
    	.append("where ")
    	.append("evtMeio.meioTransporte.idMeioTransporte = ? ")
    	.append("order by evtMeio.dhInicioEvento.value desc ");
		
    	param.add(id);
    	    	        
    	    	
    	return getAdsmHibernateTemplate().findPaginated(
    			sql.toString(), sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
	}
    
}