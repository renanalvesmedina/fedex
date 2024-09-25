package com.mercurio.lms.coleta.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.coleta.model.ServicoAdicionalColeta;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServicoAdicionalColetaDAO extends BaseCrudDao<ServicoAdicionalColeta, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ServicoAdicionalColeta.class;
    }

    protected void initFindListLazyProperties(Map map) {
    	map.put("servicoAdicional", FetchMode.JOIN);
    	map.put("pedidoColeta", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("servicoAdicional", FetchMode.JOIN);
    	map.put("pedidoColeta", FetchMode.JOIN);
    }
    
    public List findServicoAdicionalColetaByIdPedidoColeta(Long idPedidoColeta) {
		DetachedCriteria dc = DetachedCriteria.forClass(ServicoAdicionalColeta.class);
    	dc.add(Restrictions.eq("pedidoColeta.idPedidoColeta", idPedidoColeta));
    	dc.setFetchMode("servicoAdicional", FetchMode.JOIN);
    	
    	return super.findByDetachedCriteria(dc);    	
    }
    
    public void removeByIdPedidoColeta(Serializable idPedidoColeta) {
        String sql = "delete from " + ServicoAdicionalColeta.class.getName() + " as sac " +
		 			 " where " +
		 			 " sac.pedidoColeta.id = :id";

        getAdsmHibernateTemplate().removeById(sql, idPedidoColeta);
    }
    
}