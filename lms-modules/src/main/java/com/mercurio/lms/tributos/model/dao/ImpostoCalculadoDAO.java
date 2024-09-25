package com.mercurio.lms.tributos.model.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.ImpostoCalculado;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ImpostoCalculadoDAO extends BaseCrudDao<ImpostoCalculado, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ImpostoCalculado.class;
    }

    /**
     * Busca o valor do imposto calculado.<BR>
     * @author Robson Edemar Gehl
     * @param idPessoa
     * @param dtBase
     * @param tpImposto
     * @param tpRecolhimento
     * @return
     */
    public ImpostoCalculado findImpostoCalculoInss(Long idPessoa, YearMonthDay dtBase, String tpImposto, String tpRecolhimento){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.createAlias("pessoa", "p");
    	dc.add(Restrictions.eq("p.id", idPessoa));

    	if (tpImposto != null) {
    		dc.add(Restrictions.eq("tpImposto", tpImposto));
    	}
    	if (tpRecolhimento != null) {
    		dc.add(Restrictions.eq("tpRecolhimento", tpRecolhimento));
    	}
    	dc.add(Restrictions.eq("dtCompetenciaAnoMes", dtBase));
    	List list = findByDetachedCriteria(dc);
    	if (!list.isEmpty()){
    		return (ImpostoCalculado) list.get(0);
    	}
    	return null;
    }
    
    /**
     * Exclui ImpostoCalculado de acordo com o idPessoa e a dtCompetenciaAnoMes.
     *
     * @author Hector Julian Esnaola Junior
     * @since 26/10/2007
     *
     * @param idPessoa
     * @param dtCompetenciaAnoMes
     * @return
     *
     */
    public Boolean removeByPessoaDtCompetencia(
    		final Long idPessoa, 
    		final YearMonthDay dtCompetenciaAnoMes) {
    	
    	return (Boolean) getAdsmHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query q = session.createQuery(
						"   delete " 
						+   getPersistentClass().getName() + " ic "
						+ " where ic.pessoa.id = :idPessoa "
						+ " and ic.dtCompetenciaAnoMes = :dtCompetenciaAnoMes ");
				q.setParameter("idPessoa", idPessoa);
				q.setParameter("dtCompetenciaAnoMes", dtCompetenciaAnoMes);
				
				return q.executeUpdate() > 0;
			}
    		
    	});
    }
    
}