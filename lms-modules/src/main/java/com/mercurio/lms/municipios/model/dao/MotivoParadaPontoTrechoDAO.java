package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.lms.municipios.model.MotivoParadaPontoTrecho;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoParadaPontoTrechoDAO extends BaseCrudDao<MotivoParadaPontoTrecho, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoParadaPontoTrecho.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("motivoParada",FetchMode.JOIN);
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("motivoParada",FetchMode.JOIN);
    }
    
    public boolean validateDuplicatedMotivo(Long id, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal,
    		Long idMotivoParada, Long idPontoParadaTrecho) {
    	DetachedCriteria dc = JTVigenciaUtils.getDetachedVigencia(getPersistentClass(),id,dtVigenciaInicial,dtVigenciaFinal);
    	
    	dc.createAlias("pontoParadaTrecho","p");
    	dc.createAlias("motivoParada","m");
    	dc.add(Restrictions.eq("p.idPontoParadaTrecho",idPontoParadaTrecho));
    	dc.add(Restrictions.eq("m.idMotivoParada",idMotivoParada));
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc).size() > 0;
    }
    
    public List findToConsultarRotas(Long idPontoParadaTrecho) {
    	StringBuffer hql = new StringBuffer()
			.append(" select new Map( ")
			.append("   MP.dsMotivoParada as dsMotivo, ")
			.append("   MPPT.dtVigenciaInicial as dtVigenciaInicial, ")
			.append("   MPPT.dtVigenciaFinal as dtVigenciaFinal ")
			.append(" ) ")
			.append(" from " + MotivoParadaPontoTrecho.class.getName() + " MPPT ")
			.append(" left join MPPT.pontoParadaTrecho as PPT ")
			.append(" left join MPPT.motivoParada as MP ")
			
			.append(" where PPT.idPontoParadaTrecho = ? ")
			.append(" order by "+OrderVarcharI18n.hqlOrder("MP.dsMotivoParada",LocaleContextHolder.getLocale()));
	
		return getAdsmHibernateTemplate().find(hql.toString(),idPontoParadaTrecho);
    }
}