package com.mercurio.lms.ppd.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.ppd.model.PpdAtendimentoUsuario;

public class PpdAtendimentoUsuarioDAO extends BaseCrudDao<PpdAtendimentoUsuario, Long>  {
		
	public PpdAtendimentoUsuario findById(Long id) {	
		return (PpdAtendimentoUsuario)super.findById(id);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<PpdAtendimentoUsuario> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from " + getPersistentClass().getName() + " as atendimentoUsuario ")	
			.append("	inner join fetch atendimentoUsuario.grupoAtendimento as grupoAtendimento ")
			.append("	inner join fetch atendimentoUsuario.usuario as usuario ")
			.append("	left join fetch usuario.usuarioADSM as usuarioADSM ")			
			.append("where 1=1 ");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idGrupoAtendimento") != null) {
			query.append("  and grupoAtendimento.idGrupoAtendimento = :idGrupoAtendimento ");
		}
		
		query.append("order by usuarioADSM.nmUsuario ");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
	
	public List<PpdAtendimentoUsuario> findByIdGrupoAtendimento(Long idGrupoAtendimento) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("atendimentoUsuario");
		
		hql.addLeftOuterJoin(getPersistentClass().getName(),"atendimentoUsuario");
		hql.addLeftOuterJoin("atendimentoUsuario.usuario","usuario");
		hql.addLeftOuterJoin("usuario.usuarioADSM","usuarioADSM");
		hql.addCriteria("atendimentoUsuario.grupoAtendimento.idGrupoAtendimento","=",idGrupoAtendimento);
				
		return (List)getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	protected Class getPersistentClass() {		
		return PpdAtendimentoUsuario.class;
	}
	
	public void store(PpdAtendimentoUsuario atendimentoUsuario) {
		super.store(atendimentoUsuario);
	}
}	
