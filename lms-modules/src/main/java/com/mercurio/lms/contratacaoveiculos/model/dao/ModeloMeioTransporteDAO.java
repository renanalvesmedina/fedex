package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ModeloMeioTransporteDAO extends BaseCrudDao<ModeloMeioTransporte, Long>
{

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("tipoMeioTransporte", FetchMode.JOIN);
		fetchModes.put("tipoMeioTransporte.tpMeioTransporte",FetchMode.JOIN);
		fetchModes.put("marcaMeioTransporte", FetchMode.JOIN);
	}
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("tipoMeioTransporte", FetchMode.JOIN);
		fetchModes.put("tipoMeioTransporte.tpMeioTransporte",FetchMode.JOIN);
		fetchModes.put("marcaMeioTransporte", FetchMode.JOIN);
	}

	public List findListByCriteria(Map criterions) {
		List lista = new ArrayList();
		lista.add("dsModeloMeioTransporte");
		return super.findListByCriteria(criterions,lista);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ModeloMeioTransporte.class;
    }

    /**
     * Busca modelo de meio de transporte a partir da descrição.
     * @param dsModeloMeioTransporte
     * @return
     */
    public ModeloMeioTransporte findModeloMeioTransporte(String dsModeloMeioTransporte) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.add(Restrictions.ilike("dsModeloMeioTransporte",dsModeloMeioTransporte,MatchMode.EXACT));
    	return (ModeloMeioTransporte) getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
    /**
     * Busca modelos de meio de transporte a partir de
     * tipo de meio de transporte e marca de meio de transporte.
     * @param idTipoMeioTransporte identificador do tipo
     * @param idMarcaMeioTransporte identificador da marca
     * @return Lista de modelos de meio de transporte
     */
    public List<ModeloMeioTransporte> findModelosMeioTransporte(Long idTipoMeioTransporte, Long idMarcaMeioTransporte) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	// Se o tipo de meio de transporte recebido por parâmetro é diferente de nulo.
    	if (idTipoMeioTransporte != null) {
    		dc.add(Restrictions.eq("tipoMeioTransporte.id",idTipoMeioTransporte));
    	}
    	// Se a marca de meio de transporte recebido por parâmetro é diferente de nulo.
    	if (idMarcaMeioTransporte != null) {
    		dc.add(Restrictions.eq("marcaMeioTransporte.id",idMarcaMeioTransporte));
    	}
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
}