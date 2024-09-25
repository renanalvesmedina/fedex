package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.MunicipioGrupoRegiao;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class MunicipioGrupoRegiaoDAO extends BaseCrudDao<MunicipioGrupoRegiao, Long> {

	@Override
	protected Class getPersistentClass() {
		return MunicipioGrupoRegiao.class;
	}

	public List findByIdMunicipioGrupoRegiao(Long idMunicipoGrupoRegiao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),
				"mgr");
		dc.add(Restrictions.eq("mgr.municipiogruporegiao.id", idMunicipoGrupoRegiao));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public ResultSetPage<MunicipioGrupoRegiao> findPaginated(TypedFlatMap criteria, FindDefinition def) {
		return null; 
	}
	
	public MunicipioGrupoRegiao store(MunicipioGrupoRegiao municipioGrupoRegiao){
		super.store(municipioGrupoRegiao);
		return municipioGrupoRegiao;
	}
	
	public void removeById(Long municipioGrupoRegiaoId) {
		super.removeById(municipioGrupoRegiaoId);
	}

	public List<MunicipioGrupoRegiao> findByIdGrupoRegiao(Long idGrupoRegiao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"municipioGrupoRegiao");
		dc.add(Restrictions.eq("municipioGrupoRegiao.grupoRegiao.id", idGrupoRegiao));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}		
	
	public ResultSetPage<MunicipioGrupoRegiao> findPaginated(PaginatedQuery paginatedQuery) {		
		StringBuilder sql = new StringBuilder();			
		sql.append(" from ").append(getPersistentClass().getName()).append(" as municipioGrupoRegiao ");		
		sql.append(" inner join fetch municipioGrupoRegiao.grupoRegiao grupoRegiao ");
		sql.append(" inner join fetch municipioGrupoRegiao.municipio municipio ");
		sql.append(" inner join fetch municipio.unidadeFederativa unidadeFederativa ");
		sql.append(" left join fetch municipio.municipioDistrito municipioDistrito ");		
		sql.append(" where 1=1 ");
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		
		if (MapUtils.getLong(criteria, "idGrupoRegiao")!= null) {
			sql.append(" and grupoRegiao.id = :idGrupoRegiao");			
		}
		
		sql.append(" order by municipio.nmMunicipio ");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, sql.toString());
	}
	
	public List findMunicipioGrupoRegiao(Long idTabelaPreco, Long idMunicipo){
		
		StringBuilder sql = new StringBuilder()
		.append(" select gr.id_grupo_regiao  ")
		.append(" from grupo_regiao gr, municipio m, municipio_grupo_regiao mgr ")
		.append(" where m.id_municipio = ").append(idMunicipo)
		.append(" and   gr.id_tabela_preco = ").append(idTabelaPreco)
		.append(" and   gr.id_unidade_federativa = m.id_unidade_federativa ")
		.append(" and   mgr.id_grupo_regiao = gr.id_grupo_regiao ")
		.append(" and   mgr.id_municipio = m.id_municipio "); 

		return getSession().createSQLQuery(sql.toString()).list();
	}

	public void insertCloneMunicipioGrupoRegiao(Long idNewTabelaPreco) {
		StringBuilder sql = new StringBuilder()
				.append(" insert into municipio_grupo_regiao ( ")
				.append(" id_municipio_grupo_regiao, ")
				.append(" id_municipio, ")
				.append(" id_grupo_regiao) ")
				.append(" SELECT ")
				.append(" municipio_grupo_regiao_sq.NEXTVAL, ")
				.append(" id_municipio, ")
				.append(" (SELECT id_grupo_regiao FROM grupo_regiao gr WHERE id_grupo_regiao_origem = mgr.id_grupo_regiao AND id_tabela_preco = :idNewTabelaPreco ) ")
				.append(" FROM ")
				.append(" municipio_grupo_regiao mgr ")
				.append(" WHERE ")
				.append(" id_grupo_regiao in ( SELECT id_grupo_regiao_origem FROM grupo_regiao WHERE id_tabela_preco = :idNewTabelaPreco ) ");

		Map<String, Object> params = new HashMap<>();
		params.put("idNewTabelaPreco", idNewTabelaPreco);
		getAdsmHibernateTemplate().executeUpdateBySql(String.valueOf(sql), params);
	}
}
