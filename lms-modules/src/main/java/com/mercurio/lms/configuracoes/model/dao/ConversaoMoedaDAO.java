package com.mercurio.lms.configuracoes.model.dao;

import java.math.BigDecimal;

import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.util.SqlTemplate;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
@SuppressWarnings("deprecation")
public class ConversaoMoedaDAO extends AdsmDao {

    
	public BigDecimal executeConversaoMoeda(Long idPaisOrigem, Long idMoedaOrigem, Long idPaisDestino, Long idMoedaDestino, YearMonthDay dtCotacao, BigDecimal vlMoeda){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("F_CONV_MOEDA("+
    			idPaisOrigem + ", " +
    			idMoedaOrigem + ", " +
    			idPaisDestino + ", " +
    			idMoedaDestino + ", " +
    			"to_date('" + dtCotacao.toString("ddMMyyyy") + "', 'ddMMyyyy'), " +
    			vlMoeda + ")", "VL_CONVERTIDO");
    	sql.addFrom("DUAL");

    	return (BigDecimal)getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.getSql()).addScalar("VL_CONVERTIDO", Hibernate.BIG_DECIMAL).list().get(0);
    }

}