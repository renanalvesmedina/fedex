package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.FilialMercurioFilialCia;
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
public class FilialMercurioFilialCiaDAO extends BaseCrudDao<FilialMercurioFilialCia, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FilialMercurioFilialCia.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("ciaFilialMercurio",FetchMode.JOIN);
    	lazyFindById.put("ciaFilialMercurio.empresa",FetchMode.JOIN);
    	lazyFindById.put("ciaFilialMercurio.empresa.pessoa",FetchMode.JOIN);
    	lazyFindById.put("ciaFilialMercurio.filial",FetchMode.JOIN);
    	lazyFindById.put("ciaFilialMercurio.filial.pessoa",FetchMode.JOIN);
    	lazyFindById.put("filialCiaAerea",FetchMode.JOIN);
    	lazyFindById.put("filialCiaAerea.pessoa",FetchMode.JOIN);
    	lazyFindById.put("filialCiaAerea.aeroporto",FetchMode.JOIN);
    	lazyFindById.put("filialCiaAerea.aeroporto.pessoa",FetchMode.JOIN);
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("ciaFilialMercurio",FetchMode.JOIN);
    	lazyFindPaginated.put("ciaFilialMercurio.empresa",FetchMode.JOIN);
    	lazyFindPaginated.put("ciaFilialMercurio.empresa.pessoa",FetchMode.JOIN);
    	lazyFindPaginated.put("ciaFilialMercurio.filial",FetchMode.JOIN);
    	lazyFindPaginated.put("ciaFilialMercurio.filial.pessoa",FetchMode.JOIN);
    	lazyFindPaginated.put("filialCiaAerea",FetchMode.JOIN);
    	lazyFindPaginated.put("filialCiaAerea.pessoa",FetchMode.JOIN);
    	lazyFindPaginated.put("filialCiaAerea.aeroporto",FetchMode.JOIN);
    	lazyFindPaginated.put("filialCiaAerea.aeroporto.pessoa",FetchMode.JOIN);
    }
        
    /**
     * Não pode haver mais de um registro vigente para mesma Filial Cia Aérea, Cia aérea X Filial Mercúrio.
     * @author Felipe Ferreira
     * @param bean
     * @return
     */
    public boolean verificaAssocCiaAereaMercurio(FilialMercurioFilialCia bean) {
    	DetachedCriteria dc = DetachedCriteria.forClass(FilialMercurioFilialCia.class);
    	dc.createAlias("filialCiaAerea","fca");
    	dc.createAlias("ciaFilialMercurio","cfm");
    	dc.add(Restrictions.eq("fca.idFilialCiaAerea",bean.getFilialCiaAerea().getIdFilialCiaAerea()));
    	dc.add(Restrictions.eq("cfm.idCiaFilialMercurio",bean.getCiaFilialMercurio().getIdCiaFilialMercurio()));
    	if (bean.getIdFilialMercurioFilialCia() != null)
    		dc.add(Restrictions.ne("idFilialMercurioFilialCia",bean.getIdFilialMercurioFilialCia()));
    	dc = JTVigenciaUtils.getDetachedVigencia(dc,bean.getDtVigenciaInicial(),bean.getDtVigenciaFinal());
    	List l = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	return l.size() > 0;
    }
    
    public boolean findFilialCiaAreaById(YearMonthDay dataInicio, YearMonthDay dataFim, Long idFilialCiaAerea){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("filialCiaAerea.idFilialCiaAerea",idFilialCiaAerea));
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


}