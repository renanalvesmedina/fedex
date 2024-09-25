package com.mercurio.lms.edi.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.LayoutEDI;
import com.mercurio.lms.edi.model.TipoArquivoEDI;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LayoutEDIDAO extends BaseCrudDao<LayoutEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return LayoutEDI.class;
	}
	
	protected void initFindLookupLazyProperties(Map map) {
		map.put("tipoArquivoEDI", FetchMode.JOIN);
		map.put("tipoArquivoEDI.extTipoArquivoEDI", FetchMode.JOIN);
	}

	
	public LayoutEDI findById(Long id) {
		
		DetachedCriteria dc = createDetachedCriteria()
		.setFetchMode("tipoLayoutDocumento", FetchMode.JOIN)
		.add(Restrictions.eq("idLayoutEdi", id));
				
		return (LayoutEDI)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<LayoutEDI> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from " + getPersistentClass().getName() + " as layoutedi ")
			.append(" join fetch layoutedi.tipoLayoutDocumento as tld ")
			.append("where 1=1 ");
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "nmLayoutEdi") != null) {
			query.append("  and layoutedi.nmLayoutEdi like :nmLayoutEdi ");
		}
		if(MapUtils.getObject(criteria, "tpLayoutEdi") != null) {
			query.append("  and layoutedi.tpLayoutEdi = :tpLayoutEdi ");
		}
		if(MapUtils.getObject(criteria, "idTipoLayoutDocumento") != null) {
			query.append("  and layoutedi.tipoLayoutDocumento.id = :idTipoLayoutDocumento ");
		}
		
		query.append("order by layoutedi.nmLayoutEdi,layoutedi.tpLayoutEdi, layoutedi.tipoLayoutDocumento.dsTipoLayoutDocumento ");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
	
	public List<LayoutEDI> find(LayoutEDI bean){
		
		DetachedCriteria dc = createDetachedCriteria()
		.add(Restrictions.eq("nmLayoutEdi", bean.getNmLayoutEdi()))
		.add(Restrictions.eq("tpLayoutEdi", bean.getTpLayoutEdi().getValue()))
		.add(Restrictions.eq("tipoLayoutDocumento", bean.getTipoLayoutDocumento()));
		
		return getAdsmHibernateTemplate().findByCriteria(dc);
	} 
	
	public String findTipoArquivo(Long idLayoutEdi){
		StringBuilder query = new StringBuilder()
		.append(" select tpe.extTipoArquivoEDI ")
		.append(" from ").append(LayoutEDI.class.getName()).append(" la, ")
		                 .append(TipoArquivoEDI.class.getName()).append(" tpe ")
		.append(" where la.tipoArquivoEDI.idTipoArquivoEDI = tpe.idTipoArquivoEDI ")
		.append(" and  la.idLayoutEdi = :idLayoutEdi ");
		
		List<String> tpList = getAdsmHibernateTemplate().findByNamedParam(query.toString(), "idLayoutEdi", idLayoutEdi);
		
		if (tpList != null && tpList.size() != 0)
			return tpList.get(0);
		else
			return null;
}
	
}
