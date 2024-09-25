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
import com.mercurio.lms.municipios.model.TipoMeioTranspRotaEvent;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoMeioTranspRotaEventDAO extends BaseCrudDao<TipoMeioTranspRotaEvent, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoMeioTranspRotaEvent.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("tipoMeioTransporte",FetchMode.JOIN);
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("tipoMeioTransporte",FetchMode.JOIN);
    	lazyFindPaginated.put("moeda",FetchMode.JOIN);
    	lazyFindPaginated.put("rotaIdaVolta",FetchMode.JOIN);
    	lazyFindPaginated.put("rotaIdaVolta.rota",FetchMode.JOIN);
    }

    public boolean validateDuplicated(Long id, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal,
    		Long idTipoMeioTransporte, Long idRotaIdaVolta) {
    	DetachedCriteria dc = JTVigenciaUtils.getDetachedVigencia(getPersistentClass(),id,dtVigenciaInicial,dtVigenciaFinal);

    	dc.createAlias("tipoMeioTransporte","t");
    	dc.createAlias("rotaIdaVolta","r");
    	dc.add(Restrictions.eq("t.idTipoMeioTransporte",idTipoMeioTransporte));
    	dc.add(Restrictions.eq("r.idRotaIdaVolta",idRotaIdaVolta));
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc).size() > 0;
    } 
    
    public List findToConsultarRotas(Long idRotaIdaVolta) {
    	StringBuffer hql = new StringBuffer()
    		.append(" select new Map( ")
    		.append("  MT.idTipoMeioTranspRotaEvent as idTipoMeioTranspRotaEvent, ")
    		.append("  TIPO.dsTipoMeioTransporte as dsTipoMeioTransporte, ")
    		.append("  TIPO.idTipoMeioTransporte as idTipoMeioTransporte, ")
    		.append("  R.idRota as idRota, ")
    		.append("  M.idMoeda as idMoeda, ")
    		.append("  M.sgMoeda as sgMoeda, ")
    		.append("  M.dsSimbolo as dsSimbolo, ")
    		.append("  MT.vlFrete as vlFrete ")
    		.append(" ) ")
    		.append(" from " + TipoMeioTranspRotaEvent.class.getName() + " MT ")
    		.append(" inner join MT.rotaIdaVolta as RIV ")
    		.append(" inner join RIV.rota as R ")
    		.append(" inner join RIV.moedaPais as MP ")
    		.append(" inner join MP.moeda as M ")
    		.append(" inner join MT.tipoMeioTransporte TIPO")
    		.append(" where RIV.idRotaIdaVolta = ? ")
    		.append(" order by "+OrderVarcharI18n.hqlOrder("TIPO.dsTipoMeioTransporte",LocaleContextHolder.getLocale()));
    	return getAdsmHibernateTemplate().find(hql.toString(),idRotaIdaVolta);
    }	
}