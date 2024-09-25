package com.mercurio.lms.portaria.model.dao;


import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.portaria.model.MeioTransporteRodoBox;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MeioTransporteRodoBoxDAO extends BaseCrudDao<MeioTransporteRodoBox, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MeioTransporteRodoBox.class;
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("meioTransporteRodoviario", FetchMode.JOIN);
    	lazyFindPaginated.put("meioTransporteRodoviario.meioTransporte", FetchMode.JOIN);
    }
   
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("meioTransporteRodoviario", FetchMode.JOIN);    	
    	lazyFindById.put("meioTransporteRodoviario.meioTransporte", FetchMode.JOIN);
    }
    
    public boolean verificaVigenciaByBox(Long idMeioTransporteRodoBox, Long idMeioTransporte, Long idBox, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	if (idMeioTransporteRodoBox != null)
    		dc.add(Restrictions.ne("idMeioTransporteRodoBox", idMeioTransporteRodoBox));
    	
    	dc.add(Restrictions.eq("meioTransporteRodoviario.idMeioTransporte", idMeioTransporte));
    	dc.add(Restrictions.eq("box.idBox", idBox));
    	JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);
    	
    	dc.setProjection(Projections.rowCount());
    	
    	return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;    	
    }
    


}