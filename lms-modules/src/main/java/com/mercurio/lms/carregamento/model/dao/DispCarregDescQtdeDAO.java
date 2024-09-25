package com.mercurio.lms.carregamento.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.DispCarregDescQtde;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DispCarregDescQtdeDAO extends BaseCrudDao<DispCarregDescQtde, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DispCarregDescQtde.class;
    }
        
	private DetachedCriteria getDetachedCriteria(Long idCarregamentoDescarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(DispCarregDescQtde.class);
		dc.add(Restrictions.eq("carregamentoDescarga.id", idCarregamentoDescarga));
		
		return dc;
	}
	
	public List findDispCarregDescQtdeByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
		return super.findByDetachedCriteria(getDetachedCriteria(idCarregamentoDescarga));		
	}	
	
	public Integer getRowCountDispCarregDescQtdeByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(idCarregamentoDescarga)
				.setProjection(Projections.rowCount()));
	}	    
	
    /**
     * Salva os DispCarregDescQtde de um CarregamentoDescarga
     * 
     * @param itemsDispCarregDescQtde
     */
    public void storeDispCarregDescQtde(List itemsDispCarregDescQtde) {
		getAdsmHibernateTemplate().saveOrUpdateAll(itemsDispCarregDescQtde);
	}		

    /**
     * Localiza os registros DispCarregDescQtde a partir de um idManifesto.
     * @param idManifesto
     * @return
     */
    public List findDispCarregDescQtdeByIdManifesto(Long idManifesto){
    	DetachedCriteria dc = DetachedCriteria.forClass(DispCarregDescQtde.class, "dc");
    	dc.createAlias("dc.carregamentoPreManifesto", "cpm");
    	dc.createAlias("cpm.manifesto", "man");
    	dc.createAlias("dc.empresa", "emp"); //Utilizado para fazer fetch
    	dc.createAlias("dc.tipoDispositivoUnitizacao", "tdu"); //Utilizado para fazer fetch
    	dc.add(Restrictions.eq("man.id", idManifesto));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
}