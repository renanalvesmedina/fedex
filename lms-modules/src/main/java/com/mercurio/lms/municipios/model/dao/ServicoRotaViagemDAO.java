package com.mercurio.lms.municipios.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.ServicoRotaViagem;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServicoRotaViagemDAO extends BaseCrudDao<ServicoRotaViagem, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ServicoRotaViagem.class;
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("servico",FetchMode.JOIN);
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("servico",FetchMode.JOIN);
    }

    public boolean validateDuplicatedMotivo(Long id, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal,
    		Long idServico, Long idRotaViagem) {
    	DetachedCriteria dc = JTVigenciaUtils.getDetachedVigencia(getPersistentClass(),id,dtVigenciaInicial,dtVigenciaFinal);
    	
    	dc.createAlias("servico","s");
    	dc.createAlias("rotaViagem","r");
    	dc.add(Restrictions.eq("s.idServico",idServico));
    	dc.add(Restrictions.eq("r.idRotaViagem",idRotaViagem));
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc).size() > 0;
    }    

}