package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.sim.model.DescricaoEvento;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.vendas.model.EventoCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoClienteDAO extends BaseCrudDao<EventoCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EventoCliente.class;
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("evento",FetchMode.JOIN);
    	lazyFindPaginated.put("evento.descricaoEvento",FetchMode.JOIN);
    	lazyFindPaginated.put("evento.localizacaoMercadoria",FetchMode.JOIN);
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("evento",FetchMode.JOIN);
    	lazyFindById.put("evento.descricaoEvento",FetchMode.JOIN);  
    	lazyFindById.put("evento.localizacaoMercadoria",FetchMode.JOIN);
    }

    /**
     * Find personalizado para montar a combo de Eventos
     * com campos concatenados
     * @param map
     * @return List
     */
    public List findEventoCombo(Map map) {
    	SqlTemplate hql = createHqlCombo(map);
    	return getHibernateTemplate().find(hql.getSql());
    }

    /**
     * Find personalizado para montar a combo de Eventos
     * com campos concatenados
     * @param map
     * @return List
     */
    public List findEventoComboAtivo(Map map) {
    	SqlTemplate hql = createHqlCombo(map);
    	Long idEvento = MapUtilsPlus.getLong(map, "idEvento");
		if (idEvento != null){
	    	hql.addCustomCriteria("((descricaoEvento.tpSituacao='A' AND evento.tpSituacao='A') OR (evento.idEvento = ? ))",idEvento);
    	} else {
	    	hql.addCustomCriteria("(descricaoEvento.tpSituacao='A' AND evento.tpSituacao='A')");
    	}
    	return getHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }

    private SqlTemplate createHqlCombo(Map map){
    	StringBuilder projection =  new StringBuilder();
    	projection.append("new Map(");
    	projection.append("	 evento.idEvento as idEvento");
    	projection.append("	,evento.cdEvento as cdEvento");
    	projection.append("	,descricaoEvento.dsDescricaoEvento as dsDescricaoEvento");
    	projection.append("	,lm.dsLocalizacaoMercadoria as dsLocalizacaoMercadoria");
    	projection.append("	,evento.tpSituacao as tpSituacaoe");
    	projection.append("	,descricaoEvento.tpSituacao as tpSituacaod");
    	projection.append(")");

        SqlTemplate hql = new SqlTemplate();
        hql.addProjection(projection.toString());

        hql.addFrom(Evento.class.getName(),"evento left outer join evento.localizacaoMercadoria as lm ");
        hql.addFrom(DescricaoEvento.class.getName(), "descricaoEvento");
        hql.addJoin("evento.descricaoEvento", "descricaoEvento");

        hql.addCustomCriteria("evento.blExibeCliente = 'S'");

        hql.addOrderBy("evento.cdEvento");
        hql.addOrderBy(OrderVarcharI18n.hqlOrder("descricaoEvento.dsDescricaoEvento", LocaleContextHolder.getLocale()));
        return hql;
    }
    
    /**
	 * Remove todas os itens relacionados ao cliente informado.
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
    	StringBuilder hql = new StringBuilder()
    	.append(" DELETE ").append(getPersistentClass().getName())
    	.append(" WHERE cliente.id = :id");

    	getAdsmHibernateTemplate().removeById(hql.toString(), idCliente);
    }
}