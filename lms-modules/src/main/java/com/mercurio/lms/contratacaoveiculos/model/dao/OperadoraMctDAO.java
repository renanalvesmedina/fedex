package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contratacaoveiculos.model.OperadoraMct;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class OperadoraMctDAO extends BaseCrudDao<OperadoraMct, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return OperadoraMct.class;
	}

	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("pessoa",FetchMode.JOIN);
	}
	
	protected void initFindLookupLazyProperties(Map map) {
		map.put("pessoa",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pessoa",FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pessoa",FetchMode.JOIN);
	}
	
	public List findListByCriteria(Map criterions) {
		List lista = new ArrayList();
		lista.add("pessoa_.nmPessoa");
		return super.findListByCriteria(criterions,lista);
	}

	/**
     * Verifica a existencia da especialização com mesmo Numero e Tipo de Identificacao, exceto a mesma.
     * @param map
     * @return a existência de uma especialização
     */
    public boolean verificaExistenciaEspecializacao(OperadoraMct operadoraMct){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.createAlias("pessoa", "pessoa_");
    	dc.add(Restrictions.eq("pessoa_.nrIdentificacao", operadoraMct.getPessoa().getNrIdentificacao()));
    	dc.add(Restrictions.eq("pessoa_.tpIdentificacao", operadoraMct.getPessoa().getTpIdentificacao().getValue()));
    	dc.setProjection(Projections.rowCount());
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(dc);
    	return (result.intValue() > 0);	
    }
    
	/**
	 * Retorna 'true' se a pessoa informada é uma operadora ativa senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isOperadoraMct(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("count(op.id)");
		
		hql.addInnerJoin(OperadoraMct.class.getName(), "op");
		
		hql.addCriteria("op.id", "=", idPessoa);
		hql.addCriteria("op.tpSituacao", "=", "A");
		
		List lstOperadoraMct = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (((Long)lstOperadoraMct.get(0)) > 0){
			return true;
		} else {
			return false;
		}
	}    
	
}