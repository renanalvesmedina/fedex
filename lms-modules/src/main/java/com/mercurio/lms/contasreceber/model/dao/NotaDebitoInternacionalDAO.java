package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.Fatura;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaDebitoInternacionalDAO extends BaseCrudDao<Fatura, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return Fatura.class;
    }
    
    public Fatura findById(Long id) {
    	return super.findById(id);
    }
    
    public void store(Fatura obj) {
    	super.store(obj);
    }
    
    /**
     * Retorna a nota de debito internacional com o n�mero da nota e a filial informado.
     * 
     * @author Micka�l Jalbert
     * @since 07/07/2006
     * 
     * @param nrNotaDebito
     * @param idFilial
     * 
     * @return List
     */    
    public List findByNrFaturaByFilial(Long nrNotaDebito, Long idFilial){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("fat");
    	
    	hql.addInnerJoin(Fatura.class.getName(), "fat");
    	hql.addInnerJoin("fetch fat.filialByIdFilial", "fil");
    	hql.addInnerJoin("fetch fil.pessoa", "pes");
    	
    	hql.addCriteria("fat.tpFatura", "=", "D");
    	hql.addCriteria("fat.nrFatura", "=", nrNotaDebito);
    	hql.addCriteria("fil.id", "=", idFilial);
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }    
    
}