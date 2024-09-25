package com.mercurio.lms.expedicao.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.coleta.model.ServicoAdicionalColeta;
import com.mercurio.lms.expedicao.model.CategoriaProduto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CategoriaProdutoDAO extends BaseCrudDao<CategoriaProduto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CategoriaProduto.class;
    }

    protected void initFindListLazyProperties(Map map) {
    	map.put("categoriaProduto", FetchMode.JOIN);
    	map.put("produto", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("categoriaProduto", FetchMode.JOIN);
    	map.put("produto", FetchMode.JOIN);
    }
    
    public List findCategoriaProdutoByIdProduto(Long idProduto) {
		DetachedCriteria dc = DetachedCriteria.forClass(CategoriaProduto.class);
    	dc.add(Restrictions.eq("produto.idProduto", idProduto));
    	dc.setFetchMode("categoriaProduto", FetchMode.JOIN);
    	
    	return super.findByDetachedCriteria(dc);    	
    }
    
    public void removeByIdProduto(Serializable idProduto) {
        String sql = "delete from " + CategoriaProduto.class.getName() + " as cap " +
		 			 " where " +
		 			 " cap.produto.id = :id";

        getAdsmHibernateTemplate().removeById(sql, idProduto);
    }
    
}