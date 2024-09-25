package com.mercurio.lms.carregamento.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.PrestadorServico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PrestadorServicoDAO extends BaseCrudDao<PrestadorServico, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PrestadorServico.class;
    }

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("pessoa",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map map) {
		map.put("pessoa",FetchMode.JOIN);
	}
	
	protected void initFindLookupLazyProperties(Map map) {
		map.put("pessoa",FetchMode.JOIN);
	}

    /**
     * Verifica a existencia de Prestador de Serviço com mesmo Numero e Tipo de Identificacao, exceto ela (se existir!).
     * @param map
     * @return se existe algum Prestador de Serviço 
     */
    public boolean verificaExistenciaPrestadorServico(PrestadorServico prestadorServico){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.createAlias("pessoa", "p");
    	dc.add(Restrictions.eq("p.nrIdentificacao", prestadorServico.getPessoa().getNrIdentificacao()));
    	dc.add(Restrictions.eq("p.tpIdentificacao", prestadorServico.getPessoa().getTpIdentificacao()));

    	if (prestadorServico.getIdPrestadorServico() != null){
    		dc.add(Restrictions.ne("idPrestadorServico", null));	
    	}
    	dc.setProjection( Projections.count("idPrestadorServico") );
    	
    	List list = findByDetachedCriteria(dc);
    	int count = ((Integer) list.get(0)).intValue();
    	return (count > 0);
    }
    
    /**
     * Verifica a existencia de Prestador de Serviço.
     * @param Long idPrestadorServico
     * @return se existe alguma prestador de Serviço
     */
    public boolean findPrestadorServicoById(Long idPrestadorServico){
    	DetachedCriteria dc = createDetachedCriteria();
    	if (idPrestadorServico != null){
    		dc.add(Restrictions.eq("idPrestadorServico", idPrestadorServico));	
    	}
    	dc.setProjection( Projections.count("idPrestadorServico") );
   	
    	List list = findByDetachedCriteria(dc);
    	int count = ((Integer) list.get(0)).intValue();
    	return (count > 0);
    }
}