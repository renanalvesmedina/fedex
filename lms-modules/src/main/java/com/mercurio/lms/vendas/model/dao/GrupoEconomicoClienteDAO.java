package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.GrupoEconomico;
import com.mercurio.lms.vendas.model.GrupoEconomicoCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class GrupoEconomicoClienteDAO extends BaseCrudDao<GrupoEconomicoCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return GrupoEconomicoCliente.class;
    }

    /**
	 * Busca lista de <tt>GrupoEconomicoCliente</tt> pelo id do
	 * <tt>GrupoEconomico</tt> a que estão relacionadas, correspondente atributo
	 * <tt>grupoEconomico.idGrupoEconomico</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>grupoEconomico.idGrupoEconomico</tt>
	 * @return lista de <tt>GrupoEconomicoCliente</tt>
	 */
    @SuppressWarnings("unchecked")
	public ResultSetPage<GrupoEconomicoCliente> findGrupoEconomicoClienteList(TypedFlatMap criteria) {
		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);
		return findPaginated(criteria, findDefinition);
	}

    /**
	 * Busca quantidade de <tt>GrupoEconomicoCliente</tt> relacionados a um
	 * <tt>GrupoEconomico</tt> com id correspondente atributo
	 * <tt>grupoEconomico.idGrupoEconomico</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>grupoEconomico.idGrupoEconomico</tt>
	 * @return quantidade de <tt>GrupoEconomicoCliente</tt>
	 */
	public Integer findGrupoEconomicoClienteRowCount(Long idGrupoEconomico) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("grupoEconomico.idGrupoEconomico", idGrupoEconomico);
		return getRowCount(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> findIdsByIdsGrupoEconomico(List<Long> idsGrupoEconomico){
		StringBuilder query = new StringBuilder()
		.append("select gec.idGrupoEconomicoCliente ")
		.append("from ").append(GrupoEconomicoCliente.class.getName()).append(" as gec ")
		.append("where gec.grupoEconomico.idGrupoEconomico in (:idsGrupoEconomico)");

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(),"idsGrupoEconomico", idsGrupoEconomico);
	}
	
	@SuppressWarnings("unchecked")
	public List<GrupoEconomicoCliente> findByIdGrupoIdCliente(Long idGrupoEconomico, Long idCliente) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT gec FROM ");
		hql.append(getPersistentClass().getName()).append(" gec ");
		hql.append(" WHERE gec.grupoEconomico.idGrupoEconomico = :idGrupoEconomico ");
		hql.append(" AND gec.cliente.idCliente = :idCliente ");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idGrupoEconomico", idGrupoEconomico);
		parameters.put("idCliente", idCliente);
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<GrupoEconomico> findGruposEconomicos(Long idCliente) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder()
			.append(" SELECT gec.grupoEconomico ")
			.append(" FROM " + GrupoEconomicoCliente.class.getName() + " gec  ");
			
		query.append(" WHERE gec.cliente.idCliente = :idCliente ");
		params.put("idCliente", idCliente);
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}

}