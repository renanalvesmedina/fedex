package com.mercurio.lms.coleta.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.coleta.model.PedidoColetaProduto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PedidoColetaProdutoDAO extends BaseCrudDao<PedidoColetaProduto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PedidoColetaProduto.class;
    }

    protected void initFindListLazyProperties(Map map) {
    	map.put("produto", FetchMode.JOIN);
    	map.put("pedidoColeta", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("produto", FetchMode.JOIN);
    	map.put("pedidoColeta", FetchMode.JOIN);
    }
    
    public List findPedidoColetaProdutoByIdPedidoColeta(Long idPedidoColeta) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColetaProduto.class);
    	dc.add(Restrictions.eq("pedidoColeta.idPedidoColeta", idPedidoColeta));
    	dc.setFetchMode("produto", FetchMode.JOIN);
    	
    	return super.findByDetachedCriteria(dc);    	
    }
    
    public void removeByIdPedidoColeta(Serializable idPedidoColeta) {
        String sql = "delete from " + PedidoColetaProduto.class.getName() + " as sac " +
		 			 " where " +
		 			 " sac.pedidoColeta.id = :id";

        getAdsmHibernateTemplate().removeById(sql, idPedidoColeta);
    }
}