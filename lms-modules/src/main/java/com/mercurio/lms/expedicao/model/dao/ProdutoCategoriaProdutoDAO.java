package com.mercurio.lms.expedicao.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.expedicao.model.ProdutoCategoriaProduto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ProdutoCategoriaProdutoDAO extends BaseCrudDao<ProdutoCategoriaProduto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ProdutoCategoriaProduto.class;
    }

    protected void initFindListLazyProperties(Map map) {
    	map.put("categoriaProduto", FetchMode.JOIN);
    	map.put("produto", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map map) {
        map.put("categoriaProduto", FetchMode.JOIN);
        map.put("produto", FetchMode.JOIN);
    }
    
    public List findProdutoCategoriaProdutoByIdProduto(Long idProduto) {
		DetachedCriteria dc = DetachedCriteria.forClass(ProdutoCategoriaProduto.class);
    	dc.add(Restrictions.eq("produto.idProduto", idProduto));
    	dc.setFetchMode("categoriaProduto", FetchMode.JOIN);
    	
    	return super.findByDetachedCriteria(dc);    	
    }
    
    public boolean findProdutoCategoriaProdutoByIdProdutoCdCategoria(Long idProduto, List listCdCategoriaProduto) {
        StringBuilder sql = new StringBuilder()
        .append(" select  pcp ")
        .append(" from ")
        .append(ProdutoCategoriaProduto.class.getName()).append(" as pcp ")
        .append(" where pcp.produto.idProduto = :idProduto ")
        .append(" and pcp.categoriaProduto.cdCategoriaProduto in (:listCdCategoriaProduto) "); 
        
        
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("idProduto", idProduto);
        param.put("listCdCategoriaProduto", listCdCategoriaProduto);
        
        List result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), param);
        return CollectionUtils.isNotEmpty(result) && result.size()>0;  
    }
    
    public void removeByIdProduto(Serializable idProduto) {
        String sql = "delete from " + ProdutoCategoriaProduto.class.getName() + " as sac " +
		 			 " where " +
		 			 " sac.produto.id = :id";

        getAdsmHibernateTemplate().removeById(sql, idProduto);
        getAdsmHibernateTemplate().flush();
    }
    
}