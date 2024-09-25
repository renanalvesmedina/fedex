package com.mercurio.lms.portaria.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.portaria.model.BoxFinalidade;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class BoxFinalidadeDAO extends BaseCrudDao<BoxFinalidade, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return BoxFinalidade.class;
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("finalidade", FetchMode.JOIN);
    	lazyFindPaginated.put("servico", FetchMode.JOIN);
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("finalidade", FetchMode.JOIN);
    	lazyFindById.put("servico", FetchMode.JOIN);
    }
       
    public boolean verificaVigenciaByFaixaHorario(Long idBoxFinalidade,  Long idBox, Long idServico, TimeOfDay hrInicial, TimeOfDay hrFinal, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	dc.setProjection(Projections.rowCount());
    	
    	dc.add(Restrictions.eq("box.idBox", idBox));
    	
    	if (idBoxFinalidade != null)
    		dc.add(Restrictions.ne("idBoxFinalidade", idBoxFinalidade));
    		
    	if (idServico != null){
	    	dc.add(
		    	Restrictions.or(
		    		Restrictions.isNull("servico.idServico"),
		    		Restrictions.eq("servico.idServico", idServico)
		    	));
    	}
    	
    	dc.add(Restrictions.not(
    				Restrictions.or(
    						Restrictions.lt("hrFinal", hrInicial),
    						Restrictions.gt("hrInicial", hrFinal)
    				)
    			));
    	
    	JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);
    	
    	return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
    }


}