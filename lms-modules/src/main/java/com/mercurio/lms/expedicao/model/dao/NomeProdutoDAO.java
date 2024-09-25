package com.mercurio.lms.expedicao.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.expedicao.model.NomeProduto;
import com.mercurio.lms.expedicao.model.Produto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NomeProdutoDAO extends BaseCrudDao<NomeProduto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NomeProduto.class;
    }

    protected void initFindPaginatedLazyProperties(Map map) {
        map.put("produto",FetchMode.JOIN);
        map.put("produto.classeRisco",FetchMode.JOIN);
        map.put("produto.subClasseRisco",FetchMode.JOIN);
        map.put("produto.naturezaProduto",FetchMode.JOIN);
        map.put("produto.tipoProduto",FetchMode.JOIN);
        
    }
    
    
    protected void initFindByIdLazyProperties(Map map) {
        map.put("produto",FetchMode.JOIN);
        map.put("produto.classeRisco",FetchMode.JOIN);
        map.put("produto.subClasseRisco",FetchMode.JOIN);
        map.put("produto.naturezaProduto",FetchMode.JOIN);
        map.put("produto.tipoProduto",FetchMode.JOIN);
    }
    
	public Integer getRowCountNomeProduto(Long idProduto) {
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(getDetachedCriteria(idProduto).setProjection(Projections.rowCount()));
	}

	private DetachedCriteria getDetachedCriteria(Long idProduto) {
		DetachedCriteria dc = DetachedCriteria.forClass(NomeProduto.class);
		dc.add(Restrictions.eq("produto.id", idProduto));
		return dc;
	}
	
	public Boolean findBydsNomeProduto(Long idProduto, String dsNomeProduto) {
        StringBuilder sb = new StringBuilder()
        .append("select np ")
        .append(" from " + NomeProduto.class.getName() + " np ")
        .append(" where lower(np.dsNomeProduto) = '" + dsNomeProduto.toLowerCase() + "' "); 
        
        if(idProduto!= null){
            sb.append(" and np.produto.id <> " + idProduto);
        }
        return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sb.toString()).list().size() > 0;
    }
	
	public List findNomeProdutoByIdProduto(Long idProduto) {
		return super.findByDetachedCriteria(getDetachedCriteria(idProduto));		
	}
	
	public List<NomeProduto> findByIds(List<Long> ids) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.in("idNomeProduto", ids));
        return criteria.list();
    }
}