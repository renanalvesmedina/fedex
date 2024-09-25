package com.mercurio.lms.vol.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.vol.model.VolModeloseqps;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolModeloseqpsDAO extends BaseCrudDao<VolModeloseqps, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolModeloseqps.class;
    }
    
    /**
     * Realiza a consulta de Modelos de equipamento utilizando os filtros recebidos da tela e apresenta 
     * informações no componente grid.
     * @param criteria
     * @return ResultSetPage 
     */
    public ResultSetPage findPaginatedModelos(TypedFlatMap criteria,FindDefinition findDef) {
    	
        SqlTemplate sql = mountSql(criteria);
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("new Map(mode.idModeloeqp as idModeloeqp,");
    	sb.append("mode.dsNome as dsNome,");
    	sb.append("mode.blHomologado as blHomologado,");
    	sb.append("tipo.dsNome as volTiposEqpto_dsNome,");
    	sb.append("pes.nmPessoa as volFabricante_pessoa_nmPessoa)");
    	
    	sql.addOrderBy("mode.dsNome");
    	sql.addProjection(sb.toString());
    	
    	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria()); 
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}


    /**
     * Retorna quantidade de modelos encontrados baseado nos filtros informados.
     * @param criteria
     * @return
     */
    public Integer getRowCountModelos(TypedFlatMap criteria) {
    	SqlTemplate sql = mountSql(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}

    private SqlTemplate mountSql(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate() ;

		StringBuffer sb = new StringBuffer();
		sb.append(" VolModeloseqps mode ");
		sb.append("inner join mode.volTiposEqpto tipo ");
		sb.append("inner join mode.volFabricante fab ");
		sb.append("inner join fab.pessoa pes ");

		sql.addFrom(sb.toString());

		sql.addCriteria("lower(mode.dsNome)","like", criteria.getString("dsNome").toLowerCase());
		sql.addCriteria("fab.idFabricante","=",criteria.getLong("volFabricante.idFabricante"));
		sql.addCriteria("tipo.idTipoEqpto","=",criteria.getLong("volTiposEqpto.idTipoEqpto"));
		Boolean blHomologado = criteria.getBoolean("blHomologado");
		if(blHomologado != null) {
			sql.addCriteria("mode.blHomologado", "=", blHomologado);
		}		
		return sql;
	}

    public void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("volFabricante", FetchMode.JOIN);
    	lazyFindById.put("volFabricante.pessoa", FetchMode.JOIN);
    	lazyFindById.put("volTiposEqpto", FetchMode.JOIN);
    }
}