package com.mercurio.lms.edi.model.dao;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.DeParaDetalheEDI;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DeParaDetalheEDIDAO extends BaseCrudDao<DeParaDetalheEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return DeParaDetalheEDI.class;
	}

	public void initFindByIdLazyProperties(Map map) {
		map.put("deParaEDI", FetchMode.JOIN);
	}
	
	public ResultSetPage<DeParaDetalheEDI> findPaginated(PaginatedQuery paginatedQuery) {
		
		StringBuilder query = new StringBuilder()
		.append(" from " + getPersistentClass().getName() + " as det ")
		.append(" join fetch det.deParaEDI as dep ")		
		.append(" where 1=1 ");
	
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idDeParaEDI") != null) {
			query.append("  and dep.id = :idDeParaEDI ");
		}
						
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}	
	public DeParaDetalheEDI findByIdDeParaAndDe(Long idDePara, String de){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq("deParaEDI.idDeParaEDI", idDePara));
		dc.add(Restrictions.eq("de", de));
		return (DeParaDetalheEDI)getAdsmHibernateTemplate().findUniqueResult(dc);
		
	} 
}
