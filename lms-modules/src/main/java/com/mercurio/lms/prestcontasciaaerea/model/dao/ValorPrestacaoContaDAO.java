package com.mercurio.lms.prestcontasciaaerea.model.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.prestcontasciaaerea.model.ValorPrestacaoConta;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ValorPrestacaoContaDAO extends BaseCrudDao<ValorPrestacaoConta, Long>
{
	/**
	 * Busca o somatório do vlTipoPrestacaoConta agrupado pelo Tipo Valor informado.<BR>
	 *@author Robson Edemar Gehl
	 * @param tpValor
	 * @return
	 */
	public BigDecimal findVlTipoPrestacaoConta(String tpValor, Long idPrestacaoConta){
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.sum("vlTipoPrestacaoConta"));
		dc.add(Restrictions.eq("tpValor",tpValor));
		dc.add(Restrictions.eq("prestacaoConta.id", idPrestacaoConta));
		List list = findByDetachedCriteria(dc);
		if (!list.isEmpty()){
			return (BigDecimal) list.get(0);
		}
		return null;
	}
	
	/**
	 * Retorna o total da prestação de contas do tipo
	 * @author Edenilson
	 * @param idPrestacaoConta
	 * @param tpValor
	 * @return Valor
	 */
	public BigDecimal findTotalByTpValor(Long idPrestacaoConta, String tpValor){
		return findTotalByTpValor(idPrestacaoConta, tpValor, null);
	}

	/**
	 * Retorna o total da prestação de contas do tipo
	 * @author Edenilson
	 * @param idPrestacaoConta
	 * @param tpValor
	 * @return Valor
	 */
	public BigDecimal findTotalByTpValor(Long idPrestacaoConta, String tpValor, String tpFormaPagamento){
		DetachedCriteria dc = createDetachedCriteria();
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.sum("vlTipoPrestacaoConta"),"vlTipoPrestacaoConta");
		
		if (tpValor == null){
			projectionList.add(Projections.groupProperty("tpValor"),"tpValor");				
		}

		dc.setProjection(projectionList);

		if (tpValor != null){
			dc.add(Restrictions.eq("tpValor", tpValor));	
		}

		if (tpFormaPagamento != null){
			dc.add(Restrictions.eq("tpFormaPagamento", tpFormaPagamento));	
		}

		dc.add(Restrictions.eq("prestacaoConta.id", idPrestacaoConta));
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		
		BigDecimal vlRet = new BigDecimal(0);
		
		List listTotal = findByDetachedCriteria(dc);
		if (!listTotal.isEmpty()){
			Map map = (Map) listTotal.get(0);
			vlRet = (BigDecimal) map.get("vlTipoPrestacaoConta");
		}

		return vlRet;
	
	}
	
	/**
	 * Busca os totais de vlTipoPrestacaoConta em ValorPrestacaoConta -- para a Prestaca Conta informada, agrupando por tpValor.<BR>
	 *@author Robson Edemar Gehl
	 * @param idPrestacaoConta
	 * @return
	 */
	public List findTotalByTpValor(Long idPrestacaoConta){
		DetachedCriteria dc = createDetachedCriteria();
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.sum("vlTipoPrestacaoConta"),"vlTipoPrestacaoConta");
		
		projectionList.add(Projections.groupProperty("tpValor"),"tpValor");				

		dc.setProjection(projectionList);

		dc.add(Restrictions.eq("prestacaoConta.id", idPrestacaoConta));
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		
		return findByDetachedCriteria(dc);
	}
	
	/**
	 * Busca os totais de vlTipoPrestacaoConta em ValorPrestacaoConta -- para a Prestaca Conta informada, agrupando por tpFormaPagamento.<BR>
	 *@author Robson Edemar Gehl
	 * @param idPrestacaoConta
	 * @return
	 */
	public List findTotaisByTpFormaPagamento(Long idPrestacaoConta, String tpFormaPagamento){
		DetachedCriteria dc = createDetachedCriteria();
		
		ProjectionList pl = Projections.projectionList();
		if (tpFormaPagamento == null){
			pl.add(Projections.groupProperty("tpFormaPagamento"),"tpFormaPagamento");	
		}
		pl.add(Projections.sum("vlTipoPrestacaoConta"),"vlTipoPrestacaoConta");
		dc.setProjection(pl);
		
		if (tpFormaPagamento != null){
			dc.add(Restrictions.eq("tpFormaPagamento", tpFormaPagamento));	
		}
		dc.add(Restrictions.ne("tpValor", "QA"));
		dc.add(Restrictions.ne("tpValor", "PE"));

		dc.add(Restrictions.eq("prestacaoConta.id", idPrestacaoConta));
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		
		return findByDetachedCriteria(dc);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ValorPrestacaoConta.class;
    }

    /**
     * Busca Valores do Tipo de Prestacao pela Prestacao de Conta.<BR>
     *@author Robson Edemar Gehl
     * @param idPrestacaoConta
     * @return
     */
    public List findVlTipoPrestacaoConta(Long idPrestacaoConta){
    	
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("prestacaoConta.id", idPrestacaoConta));
    	
    	return findByDetachedCriteria(dc);
    }
    
    /**
     * Remove os registros de valores da prestação em questão
     * @param idPrestacaoConta
     */
    public void removeDesmarcarPrestacaoConta(final Long idPrestacaoConta){
			
    	getAdsmHibernateTemplate().execute(new HibernateCallback() {
			
    		public Object doInHibernate(Session session) throws HibernateException, SQLException {

    			Query query = null;

    			String hql = "DELETE " + getPersistentClass().getName() + " \n" +
							 "WHERE prestacaoConta.id = :idPrestacaoConta \n"; 
				
				query = session.createQuery(hql);
				query.setParameter("idPrestacaoConta", idPrestacaoConta);
				query.executeUpdate();
				
				return null;
		}
	});
    	
    }
    


}