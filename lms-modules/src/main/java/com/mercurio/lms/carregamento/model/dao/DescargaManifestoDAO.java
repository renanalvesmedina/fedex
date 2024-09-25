package com.mercurio.lms.carregamento.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.DescargaManifesto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DescargaManifestoDAO extends BaseCrudDao<DescargaManifesto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DescargaManifesto.class;
    }

    /**
     * Retorna um POJO de Descarga Manifesto para o ID do Manifesto
     * 
     * @param idManifesto
     * @return
     */
    public DescargaManifesto findDescargaManifestoByIdManifesto(Long idManifesto) {
    	DetachedCriteria dc = DetachedCriteria.forClass(DescargaManifesto.class);
    	dc.add(Restrictions.eq("manifesto.id", idManifesto));
    	
        Criteria criteria = dc.getExecutableCriteria(getAdsmHibernateTemplate().getSessionFactory().getCurrentSession());
                
        return (DescargaManifesto)criteria.uniqueResult();
    }
    
    
    /**
     * Retorna um POJO de Descarga Manifesto para o ID do Manifesto Coleta
     * 
     * @param idManifestoColeta
     * @return
     */
    public DescargaManifesto findDescargaManifestoByIdManifestoColeta(Long idManifestoColeta) {
    	DetachedCriteria dc = DetachedCriteria.forClass(DescargaManifesto.class);
    	dc.add(Restrictions.eq("manifestoColeta.id", idManifestoColeta));    	
    	
        Criteria criteria = dc.getExecutableCriteria(getAdsmHibernateTemplate().getSessionFactory().getCurrentSession());
        
        return (DescargaManifesto)criteria.uniqueResult();
    }
    
    public List<DescargaManifesto> findByCarregamentoDescarga(CarregamentoDescarga carregamentoDescarga){
    	String hql = "select dm from DescargaManifesto dm where dm.carregamentoDescarga = :carregamentoDescarga";
    	Map<String, Object> param = new HashMap<String, Object>();
    	param.put("carregamentoDescarga", carregamentoDescarga);
    	
    	return getAdsmHibernateTemplate().findByNamedParam(hql, param);
    }
}