package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.SaldoCheque;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SaldoChequeDAO extends BaseCrudDao<SaldoCheque, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return SaldoCheque.class;
    }

   
    /**
     * Retorna o último saldo cheque da filial e moeda pais informado.
     * 
     * @author Mickaël Jalbert
     * 21/03/2006
     * 
     * @param Long idFilial
     * @param Long idMoedaPais
     */    
    public List findLastSaldoCheque(Long idFilial, Long idMoedaPais, YearMonthDay dtSaldo){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("sc");
    	hql.addInnerJoin(SaldoCheque.class.getName(), "sc");
    	hql.addInnerJoin("sc.filial", "fi");
    	hql.addInnerJoin("sc.moedaPais", "mo");
    	hql.addCriteria("fi.id","=",idFilial);
    	hql.addCriteria("mo.id","=",idMoedaPais);
    	hql.addCriteria("sc.dtSaldo", "=", dtSaldo);
    	hql.addOrderBy("sc.dtSaldo","desc");
    	
    	return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

}