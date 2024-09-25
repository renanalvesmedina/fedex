package com.mercurio.lms.portaria.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.portaria.model.MacroZona;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MacroZonaDAO extends BaseCrudDao<MacroZona, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MacroZona.class;
    }

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("terminal", FetchMode.JOIN);
		lazyFindLookup.put("terminal.pessoa", FetchMode.JOIN);
	}	
   
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("terminal", FetchMode.JOIN);
		lazyFindById.put("terminal.filial", FetchMode.JOIN);
		lazyFindById.put("terminal.filial.pessoa", FetchMode.JOIN);
	}
	
    public List findListByCriteria(Map criterions) {
    	List order = new ArrayList();
    	order.add("dsMacroZona");
    	return super.findListByCriteria(criterions, order);
    }


    public ResultSetPage<MacroZona> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder();
		query.append("from " + getPersistentClass().getName() + " as mz ");
		query.append("inner join fetch mz.terminal t ");
		query.append("inner join fetch t.filial f ");
		query.append("inner join fetch t.pessoa pessoaTerminal ");
		query.append("where 1=1 ");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if (MapUtils.getObject(criteria, "idTerminal") != null) {
			query.append("and mz.terminal.id = :idTerminal ");
		}
		if (MapUtils.getObject(criteria, "idFilial") != null) {
			query.append("and f.id = :idFilial ");
		}
		if (MapUtils.getObject(criteria, "dsMacroZona") != null) {
			query.append("and mz.dsMacroZona like :dsMacroZona ");
		}
		if (MapUtils.getObject(criteria, "nrCodigoBarras") != null) {
			query.append("and mz.nrCodigoBarras like :nrCodigoBarras ");
		}
		if (MapUtils.getObject(criteria, "tpSituacao") != null) {
			query.append("and mz.tpSituacao like :tpSituacao ");
		}

		query.append("order by f.sgFilial, pessoaTerminal.nmPessoa, mz.dsMacroZona");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}

    public Serializable store(MacroZona macroZona) {
		super.store(macroZona);
		return macroZona.getIdMacroZona();
	}

    /**
	 * Recupera uma instância de <code>MacroZona</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public MacroZona findById(Long id) {
    	return super.findById(id);
    }
    
    
    public MacroZona findMacroZonaByBarcode(BigDecimal barcode) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "et")
			.add(Restrictions.eq("et.nrCodigoBarras", barcode));
		return (MacroZona)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
}