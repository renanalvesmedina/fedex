package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contratacaoveiculos.model.EventoPuxada;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoPuxadaDAO extends BaseCrudDao<EventoPuxada, Long>{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected Class getPersistentClass() {
		return EventoPuxada.class;
	}
	
	public List<EventoPuxada> findBySolicitacaoContratacao(Long idSolicitacaoContratacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ep");
		dc.add(Restrictions.eq("ep.solicitacaoContracao.idSolicitacaoContratacao", idSolicitacaoContratacao));;
		return findByDetachedCriteria(dc);
	}
	
	public EventoPuxada findByLastSolicitacaoContratacao(Long idSolicitacaoContratacao) {	
		
		StringBuffer sql = new StringBuffer()
		.append("select new map(ep.idEventoPuxada,ep.tpStatusEvento,ep.solicitacaoContratacao.nrIdentificacaoMeioTransp) from EventoPuxada as ep ")
		.append("where ")
		.append("ep.solicitacaoContratacao.idSolicitacaoContratacao = ? ")
		.append("order by ep.idEventoPuxada desc");
		
		
		List list = super.getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idSolicitacaoContratacao});
		if(list != null && list.size() > 0){
			EventoPuxada eventoPuxada = new EventoPuxada();
			Map mapEventoPuxada = (Map) list.get(0);
			eventoPuxada.setIdEventoPuxada((Long) mapEventoPuxada.get("0"));
			eventoPuxada.setTpStatusEvento((DomainValue) mapEventoPuxada.get("1"));
			eventoPuxada.setSolicitacaoContratacao(new SolicitacaoContratacao());
			eventoPuxada.getSolicitacaoContratacao().setNrIdentificacaoMeioTransp((String) mapEventoPuxada.get("2"));
			return eventoPuxada;
		}
		
		return null;
		
		
	}

}
