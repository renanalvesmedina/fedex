package com.mercurio.lms.coleta.model.dao;

import org.hibernate.Hibernate;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RelatorioColetasEntregasFrotaDAO extends BaseCrudDao
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return null;
    }

    
    public ResultSetPage findPaginatedCustom(FindDefinition findDef, String query, Object[] criteria) {
    	
        	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
        		public void configQuery(org.hibernate.SQLQuery sqlQuery) {

        			sqlQuery.addScalar("FILIAL_CONTROLE_CARGA",Hibernate.STRING);
        			sqlQuery.addScalar("NR_CONTROLE_CARGA",Hibernate.LONG);
        			sqlQuery.addScalar("PROCESSO",Hibernate.STRING);
        			sqlQuery.addScalar("ORDEM_PROCESSO",Hibernate.LONG);
        			sqlQuery.addScalar("FILIAL_PEDIDO_COLETA",Hibernate.STRING);
        			sqlQuery.addScalar("NR_COLETA",Hibernate.LONG);
        			sqlQuery.addScalar("TP_DOCUMENTO_SERVICO",Hibernate.STRING);
        			sqlQuery.addScalar("NR_DOCUMENTO_SERVICO",Hibernate.LONG);
        			sqlQuery.addScalar("FILIAL_DOCUMENTO_SERVICO",Hibernate.STRING);
        			sqlQuery.addScalar("DH_EMISSAO",Hibernate.custom(JodaTimeDateTimeUserType.class));
        			sqlQuery.addScalar("FILIAL_MANIFESTO",Hibernate.STRING);
        			sqlQuery.addScalar("NR_MANIFESTO",Hibernate.LONG);
        			sqlQuery.addScalar("CLIENTE_DESTINATARIO",Hibernate.STRING);
        			sqlQuery.addScalar("QT_VOLUMES",Hibernate.LONG);
        			sqlQuery.addScalar("PESO",Hibernate.BIG_DECIMAL);
        			sqlQuery.addScalar("MOEDA",Hibernate.STRING);
        			sqlQuery.addScalar("VALOR",Hibernate.BIG_DECIMAL);
        			sqlQuery.addScalar("TP_STATUS",Hibernate.STRING);
        			sqlQuery.addScalar("QT_RETORNOS",Hibernate.INTEGER);
        			
        		}
        	};

       	return getAdsmHibernateTemplate().findPaginatedBySql(query, findDef.getCurrentPage(), findDef.getPageSize(),criteria, csq);
    }
    
    public Integer getRowCountCustom(String query, Object[] criteria) {
    	return getAdsmHibernateTemplate().getRowCountBySql(query, criteria);
	}
    

    
}