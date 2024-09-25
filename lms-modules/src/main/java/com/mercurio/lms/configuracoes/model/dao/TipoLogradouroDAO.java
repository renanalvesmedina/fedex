package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
@SuppressWarnings("deprecation")
public class TipoLogradouroDAO extends BaseCrudDao<TipoLogradouro, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    @SuppressWarnings("rawtypes")
	protected final Class getPersistentClass() {
        return TipoLogradouro.class;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List findListByCriteria(Map criterions, List order) {
   		order = new ArrayList(1);	
   		order.add("dsTipoLogradouro");
    	return super.findListByCriteria(criterions, order);
    }
   
    @SuppressWarnings({ "unchecked" })
	public List<TipoLogradouro> findListByDescricaoTipoLogradouro(String descricao) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" tl "); 
		hql.addFrom(TipoLogradouro.class.getName(), "tl");
		hql.addCustomCriteria("upper(tl.dsTipoLogradouro) like '%»" + descricao.toUpperCase() + "%¦'");

		return getHibernateTemplate().find(hql.getSql());
    }
   


}