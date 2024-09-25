package com.mercurio.lms.contasreceber.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.DemonstrativoDesconto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DemonstrativoDescontoDAO extends BaseCrudDao<DemonstrativoDesconto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DemonstrativoDesconto.class;
    }

    public void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("filial",FetchMode.JOIN);
    	lazyFindById.put("filial.pessoa",FetchMode.JOIN);
    }
    
    /**
     * Carrega o DemonstrativoDesconto de acordo com o número e a sigla da filial
     *
     * @author Hector Julian Esnaola Junior
     * @since 10/08/2007
     *
     * @param nrDemonstrativoDesconto
     * @param idFilial
     * @param tpSituacao
     * @return
     *
     */
	public DemonstrativoDesconto findDemonstrativoDescontoByNumeroAndFilial(Long nrDemonstrativoDesconto, Long idFilial, String tpSituacao){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" dd ");
		hql.addInnerJoin(DemonstrativoDesconto.class.getName(), "dd");
		hql.addCriteria("dd.nrDemonstrativoDesconto", "=", nrDemonstrativoDesconto);
		hql.addCriteria("dd.filial.id", "=", idFilial);
		hql.addCriteria("dd.tpSituacaoDemonstrativoDesc", "=", tpSituacao);
		
		return (DemonstrativoDesconto)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}	

}
