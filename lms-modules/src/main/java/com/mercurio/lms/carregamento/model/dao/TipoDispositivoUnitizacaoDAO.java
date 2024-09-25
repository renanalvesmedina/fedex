package com.mercurio.lms.carregamento.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoDispositivoUnitizacaoDAO extends BaseCrudDao<TipoDispositivoUnitizacao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoDispositivoUnitizacao.class;
    }
    
    /**
     * Retorna um POJO de TipoDispositivoUnitizacao para o ID do TipoDispositivoUnitizacao
     * 
     * @param idTipoDispositivoUnitizacao
     * @return
     */
    public TipoDispositivoUnitizacao findById(Long idTipoDispositivoUnitizacao) {
    	DetachedCriteria dc = DetachedCriteria.forClass(TipoDispositivoUnitizacao.class);
    	dc.add(Restrictions.eq("id", idTipoDispositivoUnitizacao));    	
    	
        Criteria criteria = dc.getExecutableCriteria(getAdsmHibernateTemplate().getSessionFactory().getCurrentSession());
        
        return (TipoDispositivoUnitizacao)criteria.uniqueResult();
    }  

    public List<Map<String,Object>> findCombo() {
    	StringBuilder hql = new StringBuilder();
    	hql.append("select new map( ");
    	hql.append("	tipo.idTipoDispositivoUnitizacao as idTipoDispositivoUnitizacao, ");
    	hql.append("	tipo.dsTipoDispositivoUnitizacao as dsTipoDispositivoUnitizacao ");
    	hql.append(") ");
    	hql.append("from " + this.getPersistentClass().getName() + " tipo ");
    	hql.append("where tipo.tpControleDispositivo = 'I' ");
    	return getAdsmHibernateTemplate().find(hql.toString());
    }
}