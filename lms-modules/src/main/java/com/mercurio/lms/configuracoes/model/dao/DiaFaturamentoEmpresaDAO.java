package com.mercurio.lms.configuracoes.model.dao;
        
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.DiaFaturamentoEmpresa;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DiaFaturamentoEmpresaDAO extends BaseCrudDao<DiaFaturamentoEmpresa, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DiaFaturamentoEmpresa.class;
    }
   
    public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		 
        SqlTemplate sql = new SqlTemplate();
  	    String dominio = "DM_PERIODICIDADE_FATURAMENTO";
  	    
       sql.addProjection("dfe");
 
       sql.addFrom(Domain.class.getName() + " as d");
       sql.addFrom(DomainValue.class.getName() + " as vd");
       sql.addFrom(DiaFaturamentoEmpresa.class.getName() + " as dfe");
   
     	   sql.addJoin("dfe.tpPeriodicidade","vd.value");
 	   sql.addJoin("d.id","vd.domain.id");     	   
 	   sql.addCriteria("d.name","=",dominio); 
       sql.addCriteria("dfe.tpPeriodicidade","=",(String)(criteria.get("tpPeriodicidade")));     	   
       sql.addCriteria("dfe.tpSituacao","=",(String)(criteria.get("tpSituacao")));
  	   
 	   sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd.description", LocaleContextHolder.getLocale()));
     	   
       return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
 		
 	}
  	
}   

