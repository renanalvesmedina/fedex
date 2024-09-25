package com.mercurio.lms.configuracoes.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.configuracoes.model.BlackList;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class BlackListDAO extends BaseCrudDao<BlackList, Long> {

	@Override
	protected Class getPersistentClass() {
		return BlackList.class;
	}

	/**
	 * Retorna os emails que estão na blackList
	 * @param listEmail
	 * @return
	 */
	public List<Object[]> findBlackListByEmails(String[] listEmail) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("listEmail", listEmail);
		StringBuilder sql = new StringBuilder()
				.append(" SELECT CON.DS_EMAIL FROM BLACK_LIST BL, "
						+ "CONTATO CON WHERE BL.ID_CONTATO = CON.ID_CONTATO "
						+ "AND CON.DS_EMAIL  IN (:listEmail) ");

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("DS_EMAIL", Hibernate.STRING);
			}
		};

		return getAdsmHibernateTemplate().findBySql(sql.toString(), params,
				configureSqlQuery);
	}

}
