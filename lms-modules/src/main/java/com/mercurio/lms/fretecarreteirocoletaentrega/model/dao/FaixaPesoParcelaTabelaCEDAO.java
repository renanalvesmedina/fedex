 package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.FaixaPesoParcelaTabelaCE;
 
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FaixaPesoParcelaTabelaCEDAO extends BaseCrudDao<FaixaPesoParcelaTabelaCE, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
	   return FaixaPesoParcelaTabelaCE.class;
	}
	
	private SqlTemplate getSqlTemplateFilterParcelaTabelaCE(Long idParcelaTabelaCe,boolean orderByPsInicial) {
		
		SqlTemplate hql = new SqlTemplate();		
   		hql.addInnerJoin(getPersistentClass().getName() , "faixaPesoParcelaTabelaCE");
   		
   		if(orderByPsInicial) {
   			hql.addOrderBy("faixaPesoParcelaTabelaCE.psInicial");
   		}
  		hql.addCriteria("faixaPesoParcelaTabelaCE.tabelaColetaEntrega.id", "=", idParcelaTabelaCe);
		return hql;
	}

	public List<FaixaPesoParcelaTabelaCE> findFaixaPesoByIdParcelaTabelaCE(Long idParcelaTabelaCe) {
		if (idParcelaTabelaCe == null) {
			return new ArrayList<FaixaPesoParcelaTabelaCE>();
		}
		SqlTemplate hql = getSqlTemplateFilterParcelaTabelaCE(idParcelaTabelaCe,false);
   		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public BigDecimal findValorFaixaPeso(Long idTabelaColetaEntrega, BigDecimal qtdTotalFretePeso) {
		StringBuilder query = new StringBuilder();
		query.append(" select faixa.vlValor from "+ getPersistentClass().getName() + " as faixa where  " );
		query.append(" faixa.psInicial >= ? and faixa.psInicial <= ? ");
		query.append(" and faixa.tabelaColetaEntrega.id = ? ");
		
		return (BigDecimal) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{qtdTotalFretePeso, qtdTotalFretePeso, idTabelaColetaEntrega});
	}

	public FaixaPesoParcelaTabelaCE findFaixaPeso(Long idTabelaColetaEntrega, BigDecimal qtdTotalFretePeso) {
		StringBuilder query = new StringBuilder();
		query.append(" select faixa from "+ getPersistentClass().getName() + " as faixa where  " );
		query.append(" faixa.psFinal >= ? and faixa.psInicial <= ? ");
		query.append(" and faixa.tabelaColetaEntrega.id = ? ");
	
		return (FaixaPesoParcelaTabelaCE) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{qtdTotalFretePeso, qtdTotalFretePeso, idTabelaColetaEntrega});
	}

	public FaixaPesoParcelaTabelaCE findFaixaInicial(Long idTabelaColetaEntrega) {
		StringBuilder query = new StringBuilder();
		query.append(" select faixa from "+ getPersistentClass().getName() + " as faixa where  " );
		query.append(" faixa.psInicial = 0 ");
		query.append(" and faixa.tabelaColetaEntrega.id = ? ");
	
		return (FaixaPesoParcelaTabelaCE) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{idTabelaColetaEntrega});
	}
	
	public List<FaixaPesoParcelaTabelaCE> findFaixaPesoByIdParcelaTabelaCE(Long idParcelaTabelaCe,boolean orderByPsInicial) {
		SqlTemplate hql = getSqlTemplateFilterParcelaTabelaCE(idParcelaTabelaCe,orderByPsInicial);
   		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	
	
}