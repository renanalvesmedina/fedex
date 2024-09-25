package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.HistoricoCheque;
import com.mercurio.lms.contasreceber.model.param.MovimentoChequeParam;
import com.mercurio.lms.util.SQLUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class HistoricoChequeDAO extends BaseCrudDao<HistoricoCheque, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return HistoricoCheque.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("cheque", FetchMode.JOIN);
    	lazyFindById.put("cheque.moedaPais", FetchMode.JOIN);
    	lazyFindById.put("cheque.filial", FetchMode.JOIN);
    	lazyFindById.put("cheque.loteCheque", FetchMode.JOIN);
    	lazyFindById.put("cheque.loteCheque.redeco", FetchMode.JOIN);
    	lazyFindById.put("cheque.pendencia", FetchMode.JOIN);
    	lazyFindById.put("filial", FetchMode.JOIN);
    }

    public List find(MovimentoChequeParam param) {
    	SqlTemplate hql = mountHql(param, Boolean.TRUE, null);
    	hql.addProjection("hc");
    	return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    public Integer getRowCount(MovimentoChequeParam param) {
    	SqlTemplate hql = mountHql(param, Boolean.TRUE, null);    	
    	return this.getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
    } 
    
    private SqlTemplate mountHql(Long idLoteCheque){  
    	return mountHql(null, null, idLoteCheque);    
    }
    
    private SqlTemplate mountHql(MovimentoChequeParam param, Boolean blFechado, Long idLoteCheque){
    	
    	SqlTemplate hql = new SqlTemplate();
    	SqlTemplate hqlSub = new SqlTemplate();
    	SqlTemplate hqlSubSub = new SqlTemplate();

    	hqlSubSub.addProjection("MAX(hcSubSub.dhHistoricoCheque.value)");
    	hqlSubSub.addInnerJoin(HistoricoCheque.class.getName(), "hcSubSub");
    	hqlSubSub.addCustomCriteria("hcSubSub.cheque.id = hc.cheque.id");
    	
    	hqlSub.addProjection("MAX(hcSub.idHistoricoCheque)");
    	hqlSub.addInnerJoin(HistoricoCheque.class.getName(), "hcSub");
    	hqlSub.addCustomCriteria("hcSub.cheque.id = hc.cheque.id");
    	hqlSub.addCustomCriteria("hcSub.dhHistoricoCheque.value = (" + hqlSubSub.getSql()+")");
    	
    	hql.addInnerJoin(HistoricoCheque.class.getName(), "hc");
    	hql.addInnerJoin("fetch hc.cheque", "ch");
    	hql.addInnerJoin("fetch ch.moedaPais", "mp");
    	hql.addInnerJoin("fetch mp.moeda", "mo");
    	hql.addInnerJoin("fetch ch.loteCheque", "lc");
    	
    	hql.addCriteria("lc.nrLoteCheque",">=",param.getNrLoteChequeInicial());
    	hql.addCriteria("lc.nrLoteCheque","<=",param.getNrLoteChequeFinal());
    	hql.addCriteria("lc.idLoteCheque","=",idLoteCheque);
    	hql.addCriteria("ch.nrBanco","=",param.getNrBanco());
    	hql.addCriteria("ch.nrAgencia","=",param.getNrAgencia());
    	hql.addCriteria("ch.nrCheque","=",param.getNrCheque());
    	hql.addCriteria("ch.nrContaCorrente","=",param.getNrContaBancaria());
    	hql.addCriteria("ch.tpSituacaoCheque","=",param.getTpSituacaoCheque());
    	hql.addCriteria("hc.tpHistoricoCheque","=",param.getTpHistoricoCheque());
    	hql.addCriteria("lc.dtEmissao",">=",param.getDtEmissaoInicial());
    	hql.addCriteria("lc.dtEmissao","<=",param.getDtEmissaoFinal());
    	hql.addCriteria("ch.dtVencimento",">=",param.getDtVencimentoInicial());
    	hql.addCriteria("ch.dtVencimento","<=",param.getDtVencimentoFinal());
    	hql.addCriteria("ch.dtReapresentacao",">=",param.getDtReapresentacaoInicial());
    	hql.addCriteria("ch.dtReapresentacao","<=",param.getDtReapresentacaoFinal());
    	hql.addCriteria("ch.filial.id","=",param.getIdFilial());
    	
    	hql.addCustomCriteria("hc.idHistoricoCheque = (" + hqlSub.getSql()+")");
    	hql.addCriteria("lc.blFechado","=",blFechado);
    	
    	hql.addOrderBy("ch.nrBanco");
    	hql.addOrderBy("ch.nrAgencia");
    	hql.addOrderBy("ch.nrContaCorrente");
    	hql.addOrderBy("ch.nrCheque");
    	return hql;
    }
    
    
    
    /**
	 * Retorna os últimos historicos de cada cheque do lote cheque informado.
	 * 
	 * Mickaël Jalbert
	 * 21/03/2006
	 * 
	 * @param Long idLoteCheque
	 * @return List
	 **/
    public List findByLoteCheque(Long idLoteCheque){
    	SqlTemplate hql = mountHql(idLoteCheque);
    	hql.addProjection("hc");
    	return this.getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }

    /**
	 * Retorna o último historico do cheque informado.
	 * 
	 * Edenilson
	 * 18/07/2006
	 * 
	 * @param Long idCheque
	 * @return Long
	 **/
    public Long findByCheque(Long idCheque){

    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection(" max(hc.id) as id ");
    	hql.addFrom(HistoricoCheque.class.getName() + " hc ");
    	hql.addCriteria("hc.cheque.id", "=", idCheque);
    	
    	List ret = this.getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    	
		return ((Long)ret.get(0));

    }

    /**
     * autor Edenilson
     * @param idCheque
     * @param lstSituacoes
     * Verifica se existe algumm histórico nas situações passadas por parâmetro
     */
	public boolean validadeExisteHistorico(Long idCheque, List lstSituacoes) {

		SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection(" hc.id ");
    	hql.addFrom(HistoricoCheque.class.getName() + " hc ");
    	hql.addCriteria("hc.cheque.id", "=", idCheque);
    	SQLUtils.joinExpressionsWithComma(lstSituacoes, hql, "hc.tpHistoricoCheque");
    	
    	List ret = this.getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    	
		return (ret.size()>0);

	}    
}