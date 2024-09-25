package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.AgrupamentoCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class
AgrupamentoClienteDAO extends BaseCrudDao<AgrupamentoCliente, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return AgrupamentoCliente.class;
    }
    
    protected void initFindListLazyProperties(Map lazyFindList) {
    	lazyFindList.put("formaAgrupamento", FetchMode.JOIN);
    }

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("formaAgrupamento", FetchMode.JOIN);
		
	}

   protected void initFindByIdLazyProperties(Map fetchModes) {
	   fetchModes.put("formaAgrupamento", FetchMode.JOIN);
		
   }


	/**
	 * Retorna o numero de agrupamentos dentro da tabela AGRUPAMENTO_CLIENTE a partir da divis�o do cliente.
	 * @param idDivisao
	 * @return Numero de Agrupamentos
	 */
   	public Integer findByDivisaoClienteId(Long idDivisao){
		ProjectionList pl = Projections.projectionList()
			.add(Projections.count("ac.idAgrupamentoCliente"), "countId");	
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ac")
			.setProjection(pl)
			.add(Restrictions.eq("ac.divisaoCliente.idDivisaoCliente", idDivisao));
		
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
   }
}