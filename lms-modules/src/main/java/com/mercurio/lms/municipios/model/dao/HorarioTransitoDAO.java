package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.HorarioTransito;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class HorarioTransitoDAO extends BaseCrudDao<HorarioTransito, Long>
{

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("rotaIntervaloCep", FetchMode.JOIN);
	}
	
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("rotaIntervaloCep", FetchMode.JOIN);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return HorarioTransito.class;
    }

    public boolean verificaConvergenciaHorario(Long idHorarioTransito, Long idRotaIntervaloCep, TimeOfDay hrInicial, TimeOfDay hrFinal){
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	if (idHorarioTransito != null)
    		dc.add(Restrictions.ne("idHorarioTransito", idHorarioTransito));
    	
    	dc.add(Restrictions.eq("rotaIntervaloCep.idRotaIntervaloCep", idRotaIntervaloCep));
    	
    	dc.add(
    			Restrictions.and(
    					Restrictions.ge("hrTransitoFinal", hrInicial),
    					Restrictions.le("hrTransitoInicial", hrFinal)
    				)
    	);
    	
    	dc.setProjection(Projections.rowCount());
    	
    	List result = findByDetachedCriteria(dc);
    	
    	return ((Integer) result.get(0)).intValue() > 0;    	
    }


}