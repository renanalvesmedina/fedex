package com.mercurio.lms.tributos.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.IssMunicipioServico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class IssMunicipioServicoDAO extends BaseCrudDao<IssMunicipioServico, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return IssMunicipioServico.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {     
        lazyFindById.put("servicoMunicipio",FetchMode.JOIN);
        lazyFindById.put("municipio",FetchMode.JOIN);
        lazyFindById.put("servicoTributo",FetchMode.JOIN);
        lazyFindById.put("servicoAdicional",FetchMode.JOIN);
    }
   
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
        lazyFindPaginated.put("servicoMunicipio",FetchMode.JOIN);
        lazyFindPaginated.put("municipio",FetchMode.JOIN);
        lazyFindPaginated.put("servicoTributo",FetchMode.JOIN);
        lazyFindPaginated.put("servicoAdicional",FetchMode.JOIN);
    }
    
    /**
     * Método sobrescrito para buscar os registros onde alguns Serviços podem ser nulos
     * @param criteria Critérios de pesquisa
     * @param findDef Definições de paginação
     * @return ResultSetPage Resultado da pesquisa
     */
    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection(" iss ");
        
        StringBuffer joins = new StringBuffer()
            .append(" left  join fetch iss.servicoMunicipio sm " )
            .append(" left  join fetch iss.servicoAdicional sa ")
            .append(" left  join fetch iss.servicoTributo st ")            
            .append(" inner join fetch iss.municipio m ");
            
        sql.addFrom( getPersistentClass().getName() + " iss " + joins.toString() );
        
        sql.addCriteria("m.idMunicipio","=", criteria.getLong("municipio.idMunicipio"));
        sql.addCriteria("sm.idServicoMunicipio","=", criteria.getLong("servicoMunicipio.idServicoMunicipio"));
        sql.addCriteria("sa.idServicoAdicional","=", criteria.getLong("servicoAdicional.idServicoAdicional"));
        sql.addCriteria("st.idServicoTributo"  ,"=", criteria.getLong("servicoTributo.idServicoTributo"));        
        
        sql.addOrderBy("m.nmMunicipio");
        sql.addOrderBy("sm.dsServicoMunicipio");
        sql.addOrderBy(OrderVarcharI18n.hqlOrder("sa.dsServicoAdicional", LocaleContextHolder.getLocale()));        
        sql.addOrderBy("st.dsServicoTributo");        
        
        ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), 
                                                                     findDef.getCurrentPage(), 
                                                                     findDef.getPageSize(),
                                                                     sql.getCriteria());
        return rsp;
    }

}