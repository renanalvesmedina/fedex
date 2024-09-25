package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt;
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
public class RegiaoFilialRotaColEntDAO extends BaseCrudDao<RegiaoFilialRotaColEnt, Long>
{

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("rotaColetaEntrega", FetchMode.JOIN);
		lazyFindById.put("rotaColetaEntrega.filial", FetchMode.JOIN);		
		lazyFindById.put("rotaColetaEntrega.filial.pessoa", FetchMode.JOIN);
		lazyFindById.put("regiaoColetaEntregaFil", FetchMode.JOIN);
	}
	
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("rotaColetaEntrega", FetchMode.JOIN);
		lazyFindPaginated.put("rotaColetaEntrega.filial", FetchMode.JOIN);		
		lazyFindPaginated.put("rotaColetaEntrega.filial.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("regiaoColetaEntregaFil", FetchMode.JOIN);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RegiaoFilialRotaColEnt.class;
    }
    
    //verifica se a regiao filial rota tem a vigencia final maior que que vigencia final da regiao de coleta, se tem a vigencia final da regiao de coleta nao poderá ser alterada
    public boolean verificaVigenciaVigenteRegiaoFilialRota(Long idRegiaoColetaEntregaFilial, YearMonthDay dataInicio ,YearMonthDay dataFim){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil",idRegiaoColetaEntregaFilial));
    	dc.add(Restrictions.or(
    			Restrictions.or(
	    			Restrictions.and(
	    					Restrictions.lt("dtVigenciaInicial",dataInicio),
	    					Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataFim))),
	    			Restrictions.and(
	    	    					Restrictions.gt("dtVigenciaInicial",dataInicio),
	    	    					Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataFim)))),
	    	   Restrictions.or(
	    			   Restrictions.lt("dtVigenciaInicial",dataInicio),
	    			   Restrictions.gt("dtVigenciaFinal",dataFim))));
		return findByDetachedCriteria(dc).size()>0;
		
    	
    }

    /**
     * Verifica se a rota ja esta vigente no perido informado
     * @param idRegiaoFilialRotaColEnt
     * @param idRotaColetaEntrea
     * @param dtVigenciaInicial
     * @param dtVigenciaFinal
     * @return TRUE se existe um registro vigente, FALSE caso contrario
     */
    public boolean verificaVigenciaRota(Long idRegiaoFilialRotaColEnt, Long idRotaColetaEntrega, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	if (idRegiaoFilialRotaColEnt != null)
    		dc.add(Restrictions.ne("idRegiaoFilialRotaColEnt", idRegiaoFilialRotaColEnt));
    	
    	dc.add(Restrictions.eq("rotaColetaEntrega.idRotaColetaEntrega", idRotaColetaEntrega));
    	JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);
    	
    	dc.setProjection(Projections.rowCount());
    	
    	List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	
    	return ((Integer)result.get(0)).intValue() > 0 ;
    }
    
   

    /**
	 * Recupera instâncias de RegiaoFilialRotaColEnt que estejam vigentes.
	 * @param criteria 
	 * @return
	 */
    public List findByVigencia(Map criteria){
    	DetachedCriteria dc = DetachedCriteria.forClass(RegiaoFilialRotaColEnt.class, "regiaoFilial")
        .add(Restrictions.le("dtVigenciaInicial", new YearMonthDay()))
    	.add(Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.maxYmd((YearMonthDay)criteria.get("dtVigente"))))
    	
    	.setFetchMode("regiaoColetaEntregaFil",FetchMode.JOIN)
    	.setFetchMode("rotaColetaEntrega",FetchMode.JOIN)

    	.createAlias("regiaoFilial.rotaColetaEntrega", "rotaColetaEntrega")
    	.add(Restrictions.eq("rotaColetaEntrega.id", criteria.get("idRotaColetaEntrega")));

    	return super.findByDetachedCriteria(dc);
    }
}