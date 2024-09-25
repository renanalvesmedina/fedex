package com.mercurio.lms.carregamento.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.FotoCarregmtoDescarga;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FotoCarregmtoDescargaDAO extends BaseCrudDao<FotoCarregmtoDescarga, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FotoCarregmtoDescarga.class;
    }

    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("foto", FetchMode.JOIN);
    }
    
    protected void initFindByIdLazyProperties(Map map) {
    	map.put("foto", FetchMode.JOIN);
    }
    
	private DetachedCriteria getDetachedCriteria(Long idCarregamentoDescarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(FotoCarregmtoDescarga.class);
		dc.add(Restrictions.eq("carregamentoDescarga.id", idCarregamentoDescarga));
		return dc;
	}
	
	public List findFotoCarregmtoDescargaByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
		return super.findByDetachedCriteria(getDetachedCriteria(idCarregamentoDescarga));		
	}	
	
	public Integer getRowCountFotoCarregmtoDescargaByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(idCarregamentoDescarga)
				.setProjection(Projections.rowCount()));
	}
	
    /**
     * Salva as fotos de um carregamento descarga
     * 
     * @param listFotosCarregmtoDescarga
     */
    public void storeFotosCarregmtoDescarga(List listFotosCarregmtoDescarga) {
		getAdsmHibernateTemplate().saveOrUpdateAll(listFotosCarregmtoDescarga);
	}		
}