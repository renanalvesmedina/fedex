package com.mercurio.lms.contasreceber.model.dao;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.PreFatura;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PreFaturaDAO extends BaseCrudDao<PreFatura, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    @Override
	protected final Class getPersistentClass() {
        return PreFatura.class;
    }

    
    
	/**
	 * Recuperda os dados da PreFatura, Fatura e Filial da Fatura para o idFatura especificado
	 * @author Diego Umpierre
	 *
	 *
	 * @param id da fatura 
	 * @return Instancia com os dados a partir do id da fatura
	 */
	public Object findByIdFatura(Long idFatura) {
		SqlTemplate sql = new SqlTemplate();
    	
		
//		from
		sql.addInnerJoin(PreFatura.class.getName(), "preFat");
    	sql.addInnerJoin("fetch preFat.fatura", "fat");
    	sql.addInnerJoin("fetch fat.filialByIdFilial", "fil");
    	sql.addInnerJoin("fetch fat.moeda", "moeda");
    	
    	
    	//where
    	sql.addCriteria("fat.idFatura","=",idFatura);
    	
    	
		ArrayList resultado = (ArrayList)getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		if(resultado != null && !resultado.isEmpty()){

			return resultado.get(0);
		}
		
		return null;
	}
	
	

	public Long findDeParaFilialNatura(String cdFilial) {
		StringBuffer sql = new StringBuffer()
		.append("SELECT F_INT_DE_PARA('FILIAL_NATURA', ?, 2) as TIPO from dual");
   
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("TIPO", Hibernate.LONG);
			}
		};

		List<String> param = new ArrayList<String>();
		param.add(cdFilial);

		List<Long> result = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), configureSqlQuery).getList();
		if (result.isEmpty() || result.get(0) == null)
			return null;

		return result.get(0);		
	}
	


}