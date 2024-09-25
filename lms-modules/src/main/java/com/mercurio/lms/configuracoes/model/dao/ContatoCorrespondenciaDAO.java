package com.mercurio.lms.configuracoes.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ContatoCorrespondencia;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ContatoCorrespondenciaDAO extends BaseCrudDao<ContatoCorrespondencia, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ContatoCorrespondencia.class;
    }

    public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
    	SqlTemplate sql = new SqlTemplate();
    	TypedFlatMap tfm = (TypedFlatMap)criteria;
	    
		sql.addProjection("cc");
		sql.addFrom(ContatoCorrespondencia.class.getName()+" cc join fetch cc.contato co");		 
		sql.addFrom(DomainValue.class.getName()+" dv join dv.domain do");		
		sql.addJoin("cc.tpCorrespondencia","dv.value");  		
		sql.addCriteria("do.name","=","DM_TIPO_CORRESPONDENCIA"); 
		sql.addCriteria("co.pessoa.idPessoa","=", tfm.getLong("contato.pessoa.idPessoa"));
		sql.addCriteria("co.idContato","=", tfm.getLong("contato.idContato"));
		sql.addCriteria("cc.tpCorrespondencia","=", tfm.getString("tpCorrespondencia")); 
	
		sql.addOrderBy("co.nmContato");		
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("dv.description", LocaleContextHolder.getLocale()));
		
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
    }
    
    protected void initFindByIdLazyProperties(Map fetchModes) {
        fetchModes.put("contato", FetchMode.SELECT);         
    }      


}