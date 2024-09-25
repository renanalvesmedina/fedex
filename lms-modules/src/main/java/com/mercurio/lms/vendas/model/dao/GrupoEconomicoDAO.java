package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.GrupoEconomico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class GrupoEconomicoDAO extends BaseCrudDao<GrupoEconomico, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return GrupoEconomico.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("clientePrincipal", FetchMode.JOIN);
		lazyFindById.put("clientePrincipal.pessoa", FetchMode.JOIN);
	}
    
	public GrupoEconomico findGrupoEconomicoByIdGrupoIdCliente(Long idGrupoEconomico, Long idCliente) {
		StringBuilder hql = new StringBuilder();
		hql.append("select ge FROM ");
		hql.append(getPersistentClass().getName()).append(" ge ");
		hql.append(" WHERE 1 = 1 ");
		
		if(idGrupoEconomico != null){
			hql.append(" AND ge.idGrupoEconomico = :idGrupoEconomico ");
		}
		if(idCliente != null){
			hql.append(" AND ge.clientePrincipal.id = :idCliente ");
		}
   
		Map<String, Object> parameters = new HashMap<String, Object>();
		if(idGrupoEconomico != null){
			parameters.put("idGrupoEconomico", idGrupoEconomico);
		}
		if(idCliente != null){
			parameters.put("idCliente", idCliente);
		}

		return (GrupoEconomico) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), parameters);
	}

	@SuppressWarnings("unchecked")
	public List<GrupoEconomico> findByCodigoDescricaoAtivoDiferente(String codigo, String descricao, Long idGrupoEconomico) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ge FROM ");
		hql.append(getPersistentClass().getName()).append(" ge ");
		hql.append(" WHERE ge.tpSituacao = 'A' ");
		
		if(StringUtils.isNotBlank(codigo)){
			hql.append(" AND ge.dsCodigo = :codigo ");
		}
		if(StringUtils.isNotBlank(descricao)){
			hql.append(" AND ge.dsGrupoEconomico = :descricao ");
		}
		if(idGrupoEconomico != null){
			hql.append(" AND ge.idGrupoEconomico <> :idGrupoEconomico ");
		}
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(codigo)){
			parameters.put("codigo", codigo);
		}
		if(StringUtils.isNotBlank(descricao)){
			parameters.put("descricao", descricao);
		}
		if(idGrupoEconomico != null){
			parameters.put("idGrupoEconomico", idGrupoEconomico);
		}
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<GrupoEconomico> findGruposEconomicos(TypedFlatMap tfm) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder()
			.append(" SELECT ge ")
			.append(" FROM " + GrupoEconomico.class.getName() + " ge  ")
			.append(" WHERE 1 = 1  ");
			
		String dsCodigo = tfm.getString("dsCodigo");
		String dsGrupoEconomico = tfm.getString("dsGrupoEconomico");
		
		if(StringUtils.isNotBlank(dsCodigo) && StringUtils.isNotBlank(dsGrupoEconomico)){
			query.append(" AND (LOWER(ge.dsCodigo) LIKE LOWER(:dsCodigo) OR LOWER(ge.dsGrupoEconomico) LIKE LOWER(:dsGrupoEconomico)) ");
			params.put("dsCodigo", "%" + dsCodigo + "%");
			params.put("dsGrupoEconomico", "%" + dsGrupoEconomico + "%");
			
		} else if(StringUtils.isNotBlank(dsCodigo)){
			query.append(" AND LOWER(ge.dsCodigo) LIKE LOWER(:dsCodigo) ");
			params.put("dsCodigo", "%" + dsCodigo + "%");
			
		} else if(StringUtils.isNotBlank(dsGrupoEconomico)){
			query.append(" AND LOWER(ge.dsGrupoEconomico) LIKE LOWER(:dsGrupoEconomico) ");
			params.put("dsGrupoEconomico", "%" + dsGrupoEconomico + "%");
		}
		
		
		if(StringUtils.isNotBlank(tfm.getString("tpSituacao"))){
			query.append(" AND ge.tpSituacao = :tpSituacao ");
			params.put("tpSituacao", tfm.getString("tpSituacao"));
		}
		
		if(tfm.getLong("clientePrincipal.idCliente") != null){
			query.append(" AND ge.clientePrincipal.idCliente = :idCliente ");
			params.put("idCliente", tfm.getLong("clientePrincipal.idCliente"));
		}
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}
	
}