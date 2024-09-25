package com.mercurio.lms.vol.model.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.vol.model.VolGruposFrotas;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolGruposFrotasDAO extends BaseCrudDao<VolGruposFrotas, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolGruposFrotas.class;
    }

    /**
     * retorna o nome do grupo frota pelo id
     * @param id
     * @return nome do grupo frota
     */
    public List findDsNomeById(Long id){
    	SqlTemplate sql = sqlDsNome(id);
    	
    	StringBuffer sb = new StringBuffer();
		sb.append("new Map(gf.dsNome as dsNome)");
				
		sql.addProjection(sb.toString());
		
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	
    }
    
    /**
     * monta o HQL para o findDsNomeById
     * @param id
     * @return SqlTemplate
     */
    private SqlTemplate sqlDsNome(Long id){
    	SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append("VolGruposFrotas gf ");
		sb.append("left outer join gf.volGrfrFuncionarios f ");
		sb.append("left outer join gf.volGrfsVeiculos v ");
		sb.append("inner join gf.filial fi ");
		sb.append("inner join fi.pessoa pes ");
		
		sql.addFrom(sb.toString());
		
		sql.addCriteria("gf.idGrupoFrota","=", id);
	
    	return sql;
    }
    
    public List findGruposFrotaByUsuario(TypedFlatMap criteria){
    	SqlTemplate sql = mountSql(criteria);
    	StringBuffer projecao = new StringBuffer();
    	projecao.append("new Map( ")
    			 .append("fi.sgFilial as sgFilial,")
    			 .append("fi.idFilial as idFilial,")
    			 .append("gf.idGrupoFrota as idGrupoFrota,")
    			 .append("pes.nmFantasia as nmFantasia,")
    			 .append("gf.dsNome as dsNome )");
    	sql.addProjection(projecao.toString());
    	return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
	/**
	 * Realiza uma consulta HQL e retorna a lista com os resultados para grid de ManterGruposFrota 
     * e as informações de paginação.
	 * @param criteria
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedGruposFrota(TypedFlatMap criteria,FindDefinition findDef) {
		SqlTemplate sql = mountSql(criteria);
		
		StringBuffer sb = new StringBuffer();
		sb.append("new Map(fi.sgFilial as sgFilial,")
		  .append("gf.idGrupoFrota as idGrupoFrota,")
		  .append("gf.dsNome as dsNome,")
		  .append("pes.nmFantasia as nmFantasia,")
		  .append("count(distinct v.idGruVeic) as qtdFrotasGF,")
		  .append("count(distinct f.idGruFunc) as qtdUsuariosGF)");


		sql.addOrderBy("gf.dsNome");
		sql.addProjection(sb.toString());
		
		sql.addGroupBy("fi.sgFilial");
		sql.addGroupBy("gf.idGrupoFrota");
		sql.addGroupBy("gf.dsNome");
	    sql.addGroupBy("pes.nmFantasia");
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	/**
	 * Monta hql para findPaginatedGruposFrota
	 * @param criteria
	 * @return
	 */
	private SqlTemplate mountSql(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append("VolGruposFrotas gf ");
		sb.append("inner join gf.volGrfrFuncionarios f ");
		sb.append("left outer join gf.volGrfsVeiculos v ");
		sb.append("inner join gf.filial fi ");
		sb.append("inner join fi.pessoa pes ");
			
		sql.addFrom(sb.toString());
		
		sql.addCriteria("fi.idFilial","=", criteria.getLong("filial.idFilial"));
		
		if (criteria.getString("dsNome") != null) {
			sql.addCriteria("UPPER(gf.dsNome)","like", criteria.getString("dsNome").toUpperCase());
		}
		
		return sql;
	}

	
    /**
     * Retorna quantidade de grupos frotas baseado nos filtros informados.
     * @param criteria
     * @return
     */
    public Integer getRowCountGruposFrota(TypedFlatMap criteria) {
    	SqlTemplate sql = mountSql(criteria);
    	sql.addProjection("count(distinct gf.idGrupoFrota)");
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria()); 
    	
    	return result.intValue();
	}
	
    /**
	 * Realiza uma consulta HQL e retorna a lista com os resultados para grid de ManterGruposFrotaFrotas 
     * e as informações de paginação.
     * @param criteria
     * @param findDef
     * @return
     */
    public ResultSetPage findPaginatedGruposFrotaMeiosTransporte(TypedFlatMap criteria,FindDefinition findDef) {
    	SqlTemplate sql = mountSqlGruposFrotaMeiosTransporte(criteria);
    	
    	StringBuffer sb = new StringBuffer();
		sb.append("new Map(mt.nrIdentificador as nrIdentificador,");
		sb.append("mt.nrFrota as nrFrota,");
		sb.append("ve.dsNumero as dsNumero,");
		sb.append("ve.dsNumero as dsNumero,");
		sb.append("v.idGruVeic as idGruVeic,");
		sb.append("gf.dsNome as dsNome)");
		
		sql.addOrderBy("gf.dsNome");
		sql.addProjection(sb.toString());
	  
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}
	               
	/**
	 * Retorna quantidade de linhas e as informações de paginação.
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountGruposFrotaMeiosTransporte(TypedFlatMap criteria) {
		SqlTemplate sql = mountSqlGruposFrotaMeiosTransporte(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
	
	/**
	 * Monta hql para findPaginatedGruposFrotaMeiosTransporte 
	 * @param criteria
	 * @return
	 */
	private SqlTemplate mountSqlGruposFrotaMeiosTransporte(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append("VolGrfsVeiculos v ");
		sb.append("inner join v.volGruposFrota gf ");
		sb.append("left outer join v.meioTransporte mt ");
		sb.append("left outer join mt.volEquipamentos ve ");
		
		sql.addFrom(sb.toString());
		
		sql.addCriteria("gf.idGrupoFrota","=", criteria.getLong("idGrupoFrota"));
		sql.addCriteria("mt.idMeioTransporte","=", criteria.getLong("meioTransporte.idMeioTransporte"));
		
		return sql;
	}

	
	
	
	/**
	 * Realiza uma consulta HQL e retorna a lista com os resultados para grid de ManterGruposFrotaUsuario 
     * e as informações de paginação.
     * @param criteria
     * @param findDef
     * @return
     */
    public ResultSetPage findPaginatedGruposFrotaUsuario(TypedFlatMap criteria,FindDefinition findDef) {
    	SqlTemplate sql = mountSqlGruposFrotaUsuario(criteria);
    	
    	StringBuffer sb = new StringBuffer();
		sb.append("new Map(u.login as login,");
		sb.append("u.nmUsuario as nmUsuario,");
		sb.append("f.idGruFunc as idGruFunc,");
		sb.append("fun.dsFuncao as dsFuncao)");
		
		sql.addOrderBy("u.nmUsuario");
		sql.addProjection(sb.toString());
	  
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}
	               
	/**
	 * Retorna quantidade de linhas e as informações de paginação.
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountGruposFrotaUsuario(TypedFlatMap criteria) {
		SqlTemplate sql = mountSqlGruposFrotaUsuario(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
	
	/**
	 * Monta hql para findPaginatedGruposFrotaUsuario
	 * @param criteria
	 * @return
	 */
	private SqlTemplate mountSqlGruposFrotaUsuario(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append("VolGrfrFuncionarios f ");
		sb.append("inner join f.volGruposFrota gf ");
		sb.append("inner join f.usuario u ");
		sb.append("left outer join u.vfuncionario fun ");
		
		sql.addFrom(sb.toString());
		
		sql.addCriteria("gf.idGrupoFrota","=", criteria.getLong("idGrupoFrota"));
		sql.addCriteria("u.idUsuario","=", criteria.getLong("usuario.idUsuario"));
		
		return sql;
	}

	public void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("filial",FetchMode.JOIN);
    	lazyFindById.put("filial.pessoa",FetchMode.JOIN);
    }
	public void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("filial",FetchMode.JOIN);
		lazyFindLookup.put("filial.pessoa",FetchMode.JOIN);
	}
	
	
	public List findMeioTransporteByIdGrupoFrota(Long idGrupoFrota) {
   	 
		if (idGrupoFrota == null) {
			   return Collections.EMPTY_LIST;
		}
		
        SqlTemplate sql = mountSqlMeioTransporteByIdGrupoFrota(idGrupoFrota);
         
    	StringBuffer sb = new StringBuffer();
		sb.append("new Map(mt.nrIdentificador as meioTransporte_nrIdentificador,");
		sb.append("mt.nrFrota as meioTransporte_nrFrota,");
		sb.append("mt.idMeioTransporte as meioTransporte_idMeioTransporte,");
		sb.append("ve.idEquipamento as volEquipamentos_idEquipamento,");
		sb.append("ve.dsNumero as volEquipamentos_dsNumero,");
		sb.append("v.idGruVeic as idGruVeic,");
		sb.append("gf.idGrupoFrota as volGruposFrota_idGrupoFrota,");
		sb.append("gf.dsNome as volGruposFrota_dsNome)");
		
		sql.addOrderBy("gf.dsNome");
		sql.addProjection(sb.toString());
	  
		List result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		
      return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
	}
	
	public Integer getRowCountMeioTransporteByIdGrupoFrota(Long idGrupoFrota) {
		SqlTemplate sql = mountSqlMeioTransporteByIdGrupoFrota(idGrupoFrota);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
	
	private SqlTemplate mountSqlMeioTransporteByIdGrupoFrota(Long idGrupoFrota) {
		SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append("VolGrfsVeiculos v ");
		sb.append("inner join v.volGruposFrota gf ");
		sb.append("inner join v.meioTransporte mt ");
		sb.append("left outer join mt.volEquipamentos ve ");
		
		sql.addFrom(sb.toString());
		sql.addOrderBy("mt.nrFrota");
		sql.addCriteria("gf.idGrupoFrota","=", idGrupoFrota);
		
		return sql;
	}
	
	public List findUsuarioByIdGrupoFrota(Long idGrupoFrota) {
	   	 
		if (idGrupoFrota == null) {
			   return Collections.EMPTY_LIST;
		}
		
        SqlTemplate sql = mountSqlUsuarioByIdGrupoFrota(idGrupoFrota);
    	
    	StringBuffer sb = new StringBuffer();
		sb.append("new Map(u.login as usuario_login,");
		sb.append("u.idUsuario as usuario_idUsuario,");		
		sb.append("u.nmUsuario as usuario_nmUsuario,");
		sb.append("u.nrMatricula as usuario_nrMatricula,");
		sb.append("f.idGruFunc as idGruFunc,");
		sb.append("gf.idGrupoFrota as volGruposFrota_idGrupoFrota,");
		sb.append("gf.dsNome as volGruposFrota_dsNome)");
		/* 
		 * retirado pois o join com a v_funcionario estava ocasionando full scan
		   sb.append("fun.dsFuncao as funcionario_dsFuncao)");
		*/
		sql.addOrderBy("u.nmUsuario");
		sql.addProjection(sb.toString());
	
		List result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		
	    return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
	}
	
	public Integer getRowCountUsuarioByIdGrupoFrota(Long idGrupoFrota) {
		SqlTemplate sql = mountSqlMeioTransporteByIdGrupoFrota(idGrupoFrota);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
	
	private SqlTemplate mountSqlUsuarioByIdGrupoFrota(Long idGrupoFrota) {
		SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append("VolGrfrFuncionarios f ");
		sb.append("inner join f.volGruposFrota gf ");
		sb.append("inner join f.usuario u ");
		/*
		sb.append("left outer join u.vfuncionario fun ");
		*/
		sql.addFrom(sb.toString());
		
		sql.addCriteria("gf.idGrupoFrota","=", idGrupoFrota);
		
		return sql;
	}
}