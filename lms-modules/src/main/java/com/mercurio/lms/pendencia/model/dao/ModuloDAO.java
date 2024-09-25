package com.mercurio.lms.pendencia.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.pendencia.model.Modulo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ModuloDAO extends BaseCrudDao<Modulo, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Modulo.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("terminal", FetchMode.JOIN);
    	lazyFindById.put("terminal.pessoa", FetchMode.JOIN);
    	lazyFindById.put("terminal.filial", FetchMode.JOIN);
    	lazyFindById.put("terminal.filial.pessoa", FetchMode.JOIN);
    }
    
    public ResultSetPage findPaginatedCustom(TypedFlatMap tfm, FindDefinition findDef) {
    	SqlTemplate s = compileFindPaginatedQuery(tfm);
    	return getAdsmHibernateTemplate().findPaginated(s.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(), s.getCriteria());
    }
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	SqlTemplate s = compileFindPaginatedQuery(tfm);
    	return getAdsmHibernateTemplate().getRowCountForQuery(s.getSql(false), s.getCriteria());
    }
    
    private SqlTemplate compileFindPaginatedQuery(TypedFlatMap tfm) {
    	SqlTemplate s = new SqlTemplate();
    	
    	s.addProjection("new Map(m.idModulo as idModulo, t_filial.sgFilial as sgFilial, t_filial_pessoa.nmFantasia as nmFilial, " +
    					"t_pessoa.nmPessoa as terminal, m.nrModulo as modulo, m.tpSituacao as situacao)");
    	
    	s.addFrom(getPersistentClass().getName() + " as m "
    											 + " left join m.terminal as t "
    											 + " left join t.pessoa as t_pessoa "
    											 + " left join t.filial as t_filial "
    											 + " left join t_filial.pessoa as t_filial_pessoa ");
    	
    	s.addCriteria("m.nrModulo", "=", tfm.getShort("numeroModulo"));
    	s.addCriteria("m.tpSituacao", "=", tfm.getDomainValue("situacao").getValue());
    	
    	s.addOrderBy("t_filial.sgFilial");
    	s.addOrderBy("t_filial_pessoa.nmPessoa");
    	s.addOrderBy("t_pessoa.nmPessoa");
    	s.addOrderBy("m.nrModulo");
    	s.addOrderBy("m.tpSituacao");
    	
    	
    	Long idFilial = tfm.getLong("terminal.filial.idFilial");
    	if (idFilial != null) {
	    	s.addCustomCriteria("( (t_filial is null) or (t_filial.idFilial = ?) )");
	    	s.addCriteriaValue(idFilial);
    	}
    	
    	Long idTerminal = tfm.getLong("terminal.idTerminal");
    	if (idTerminal != null) {
	    	s.addCustomCriteria("( (t is null) or (t.idTerminal = ?) )");
	    	s.addCriteriaValue(tfm.getLong("terminal.idTerminal"));
    	}
    	
    	return s;
    }

   


}