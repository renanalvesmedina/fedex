package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.ReciboDesconto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReciboDescontoDAO extends BaseCrudDao<ReciboDesconto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ReciboDesconto.class;
    }

    public void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("filial",FetchMode.JOIN);
    	lazyFindById.put("filial.pessoa",FetchMode.JOIN);
    	lazyFindById.put("pendencia",FetchMode.JOIN);
    }    

	/**
	 * Retorna a lista de recibos de desconto do redeco informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 06/07/2006
	 * 
	 * @param Long idRedeco
	 * @return List
	 */
	public List findByRedeco(Long idRedeco){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("rd");
		
		hql.addInnerJoin(ReciboDesconto.class.getName(), "rd");
		
		hql.addCriteria("rd.redeco.id", "=", idRedeco);
				
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	/**
	 * Retorna o número de recibo de desconto do redeco informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/12/2006
	 * 
	 * @param Long idRedeco
	 * @return Long
	 */
	public Integer getRowCountByIdRedeco(Long idRedeco){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("count(rd.id)");
		hql.addInnerJoin(ReciboDesconto.class.getName(), "rd");
		hql.addCriteria("rd.redeco.id", "=", idRedeco);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		return result.intValue();
	}

	/**
	 * Carrega o ReciboDesconto de acordo com o número e a sigla da filial
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 09/08/2007
	 *
	 * @param idRedeco
	 * @return
	 *
	 */
	public ReciboDesconto findReciboDescontoByNumeroAndFilial(Long nrReciboDesconto, Long idFilial, String tpSituacao){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" rd ");
		hql.addInnerJoin(ReciboDesconto.class.getName(), "rd");
		hql.addCriteria("rd.nrReciboDesconto", "=", nrReciboDesconto);
		hql.addCriteria("rd.filial.id", "=", idFilial);
		hql.addCriteria("rd.tpSituacaoReciboDesconto", "=", tpSituacao);
		
		return (ReciboDesconto)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}	
	
}