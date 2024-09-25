package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.ZonaServico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ZonaServicoDAO extends BaseCrudDao<ZonaServico, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ZonaServico.class;
    }

    public boolean consultarVigenciasServicoAvancado(ZonaServico bean){
    	DetachedCriteria dc = DetachedCriteria.forClass(ZonaServico.class);
    	dc.add(Restrictions.eq("zona.id", bean.getZona().getIdZona()));	
    	dc.add(Restrictions.eq("servico.id", bean.getServico().getIdServico()));
    	if (bean.getIdZonaServico() != null)
        	dc.add(Restrictions.ne("id", bean.getIdZonaServico()));
    	if (bean.getDtVigenciaFinal() != null) {
    		
    		dc.add(
    				Restrictions.not(
    						Restrictions.or(
								 Restrictions.lt("dtVigenciaFinal",bean.getDtVigenciaInicial())
								,Restrictions.gt("dtVigenciaInicial",bean.getDtVigenciaFinal()))));
    	} else {
    		dc.add(Restrictions.not(Restrictions.lt("dtVigenciaFinal",bean.getDtVigenciaInicial())));
    	}
		List lista = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		return (lista.size()>0);
    } 

    protected void initFindByIdLazyProperties(Map fetchModes) {
        fetchModes.put("zona", FetchMode.JOIN);
        fetchModes.put("servico", FetchMode.JOIN);
    } 

    protected void initFindPaginatedLazyProperties(Map fetchModes) {
        fetchModes.put("zona", FetchMode.JOIN);
        fetchModes.put("servico", FetchMode.JOIN);
    }
    
    
}