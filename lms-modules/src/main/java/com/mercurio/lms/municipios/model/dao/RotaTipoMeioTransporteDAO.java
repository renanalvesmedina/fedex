package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.RotaTipoMeioTransporte;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RotaTipoMeioTransporteDAO extends BaseCrudDao<RotaTipoMeioTransporte, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RotaTipoMeioTransporte.class;
    }
    
    public boolean verificaRotaTipoMeioTransporteVigente(RotaTipoMeioTransporte rotaTipoMeioTransporte){
    	DetachedCriteria dc = createDetachedCriteria();
    	if (rotaTipoMeioTransporte.getIdRotaTipoMeioTransporte() != null){
    		dc.add(Restrictions.ne("id",rotaTipoMeioTransporte.getIdRotaTipoMeioTransporte()));
    	}
    	if (rotaTipoMeioTransporte.getRotaColetaEntrega() != null) 
    	dc.add(Restrictions.eq("rotaColetaEntrega.id",rotaTipoMeioTransporte.getRotaColetaEntrega().getIdRotaColetaEntrega()));
    	if (rotaTipoMeioTransporte.getTipoMeioTransporte() != null)
    	dc.add(Restrictions.eq("tipoMeioTransporte.id",rotaTipoMeioTransporte.getTipoMeioTransporte().getIdTipoMeioTransporte()));
    	dc=JTVigenciaUtils.getDetachedVigencia(dc,rotaTipoMeioTransporte.getDtVigenciaInicial(), rotaTipoMeioTransporte.getDtVigenciaFinal());  	
    	return findByDetachedCriteria(dc).size()>0;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("rotaColetaEntrega", FetchMode.JOIN);
    	lazyFindById.put("rotaColetaEntrega.filial", FetchMode.JOIN);
    	lazyFindById.put("rotaColetaEntrega.filial.pessoa", FetchMode.JOIN);
    	lazyFindById.put("tipoMeioTransporte", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	super.initFindPaginatedLazyProperties(lazyFindPaginated);
    	lazyFindPaginated.put("rotaColetaEntrega", FetchMode.JOIN);
    	lazyFindPaginated.put("tipoMeioTransporte", FetchMode.JOIN);
    	lazyFindPaginated.put("rotaColetaEntrega.filial", FetchMode.JOIN);
    	lazyFindPaginated.put("rotaColetaEntrega.filial.pessoa", FetchMode.JOIN);
    }

    
    public List findRotaTipoMeioTransporteById(Long idRotaTipoMeioTransporte, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idRotaTipoMeioTransporte",idRotaTipoMeioTransporte));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
   


}