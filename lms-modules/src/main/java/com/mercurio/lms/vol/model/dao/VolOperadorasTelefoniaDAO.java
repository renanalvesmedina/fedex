package com.mercurio.lms.vol.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.VolOperadorasTelefonia;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolOperadorasTelefoniaDAO extends BaseCrudDao<VolOperadorasTelefonia, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolOperadorasTelefonia.class;
    }
    /**
     * Verifica se a operadora existe, sem retornar o objeto
     * @param id id da operadora
     * @return
     */
    public boolean findOperadoraExiste(Long id){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(VolOperadorasTelefonia.class.getName() + " as opte");
    	sql.addCriteria("opte.idOperadora","=",id);
    	Integer qtd = getAdsmHibernateTemplate()
    		.getRowCountForQuery(sql.getSql(), sql.getCriteria());
    	if (qtd.intValue()>=1)
    		return true;
    	return false;    	
    }
    public ResultSetPage findPaginatedOperadoras(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = createHqlPaginatedOperadoras(criteria);    	    
		
		StringBuffer pj = new StringBuffer()
			.append(" distinct new map( ")
			.append("   OPTE.idOperadora     as idOperadora, ")
			.append("   PESS.tpIdentificacao as pessoa_tpIdentificacao, ")
			.append("   PESS.nrIdentificacao as pessoa_nrIdentificacao, ")
			.append("   PESS.nmPessoa        as pessoa_nmPessoa, ")
			.append("   OPTE.tpSituacao      as tpSituacao, ")
			.append("   PESS.dsEmail         as pessoa_dsEmail ")
			.append(")");
		sql.addProjection(pj.toString());
		sql.addOrderBy("PESS.nmPessoa");
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),
    			findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
    }	
	public SqlTemplate createHqlPaginatedOperadoras(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer from = new StringBuffer()
			.append(VolOperadorasTelefonia.class.getName()).append(" as OPTE")
			.append(" inner join OPTE.pessoa  as PESS ")
		;
		sql.addFrom(from.toString()); 	   
    	
		sql.addCriteria("PESS.idPessoa","=",criteria.getLong("pessoa.idPessoa"));
		sql.addCriteria("OPTE.tpSituacao","=",criteria.get("tpSituacao"));
		sql.addCriteria("PESS.tpPessoa","=",criteria.get("pessoa.tpPessoa"));
		sql.addCriteria("lower(PESS.nmPessoa)","like",criteria.getString("pessoa.nmPessoa").toLowerCase());		
		sql.addCriteria("PESS.tpIdentificacao","like",criteria.get("pessoa.tpIdentificacao"));
		sql.addCriteria("PESS.nrIdentificacao","like",criteria.get("pessoa.nrIdentificacao"));

		return sql;    	    	   
	}

	public Integer getRowCountOperadoras(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlPaginatedOperadoras(criteria);
		sql.addProjection("COUNT(distinct OPTE.idOperadora) as rowcount");
		Integer i = Integer.valueOf(getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()).get(0).toString());		
		return i;
	} 
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pessoa",FetchMode.JOIN);
	}
}