package com.mercurio.lms.sim.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.sim.model.UsuarioResponsavelClienteCCT;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ClienteUsuarioCCTDAO extends BaseCrudDao<UsuarioResponsavelClienteCCT, Long> {

	@Override
	protected final Class<UsuarioResponsavelClienteCCT> getPersistentClass() {
		return UsuarioResponsavelClienteCCT.class;
	}
	
	public UsuarioResponsavelClienteCCT findById(Long id){
		return (UsuarioResponsavelClienteCCT)getAdsmHibernateTemplate().get(UsuarioResponsavelClienteCCT.class, id);
	}
	
	public UsuarioResponsavelClienteCCT findById(Long id, String[] fetches) {
		Criteria c = getSession().createCriteria(getPersistentClass());
		
		if (fetches != null && fetches.length != 0) {
		    for (int i = 0; i < fetches.length; i++) {
			c.setFetchMode(fetches[i], FetchMode.JOIN);
		    }
		}
		c.add(Restrictions.idEq(id));
		
		return (UsuarioResponsavelClienteCCT) c.uniqueResult();
	    }
	
	public UsuarioResponsavelClienteCCT findByNrChave(String chave) {
		Criteria c = getSession().createCriteria(getPersistentClass());
		c.add(Restrictions.eq("nrChave", chave));
		return (UsuarioResponsavelClienteCCT) c.uniqueResult();
	}
	
	public ResultSetPage findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder();
		query.append("select new Map(urc.idUsuarioResponsavelClienteCCT as idUsuarioResponsavelClienteCCT, uadsm.nmUsuario as nmUsuario, uadsm.idUsuario as idUsuario, p.nmPessoa as nmCliente, p.idPessoa as idCliente) ");
		query.append("from " + getPersistentClass().getName() + " as urc ");
		query.append("inner join urc.usuario.usuarioADSM uadsm ");
		query.append("inner join urc.cliente.pessoa p ");
		query.append(" where 1=1 ");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if (MapUtils.getObject(criteria, "idUsuario") != null) {
			query.append(" and uadsm.idUsuario = :idUsuario ");
		}
		if (MapUtils.getObject(criteria, "idCliente") != null) {
			query.append(" and urc.cliente.idCliente = :idCliente ");
		}
		
		query.append(" order by uadsm.nmUsuario, p.nmPessoa ");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
	
	public List findClienteCCTByUsuario(Long idUsuario){
		StringBuilder query = new StringBuilder();
		query.append("select new Map(p.nmPessoa as nmPessoa, p.nrIdentificacao as nrIdentificacao) ");
		query.append("from " + getPersistentClass().getName() + " as urc ");
		query.append("inner join urc.usuario u ");
		query.append("inner join urc.cliente.pessoa p ");
		query.append(" where ");
		query.append(" u.idUsuario = ? ");
		
		return getAdsmHibernateTemplate().find(query.toString(), new Object[]{idUsuario});
	}
}
