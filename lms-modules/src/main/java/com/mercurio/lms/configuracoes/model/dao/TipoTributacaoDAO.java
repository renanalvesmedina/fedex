package com.mercurio.lms.configuracoes.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.configuracoes.model.TipoTributacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoTributacaoDAO extends BaseCrudDao<TipoTributacao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoTributacao.class;
    }

	public BigDecimal findDeParaTipoTributacao(String dsTipoTributacaoIcms) {
		StringBuffer sql = new StringBuffer()
		.append("SELECT F_INT_DE_PARA('TIPO_TRIBUTACAO_ICMS', ?, 3) as TIPO from dual");
   
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("TIPO", Hibernate.LONG);
			}
		};

		List<String> param = new ArrayList<String>();
		param.add(dsTipoTributacaoIcms);

		List<Long> result = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), configureSqlQuery).getList();
		if (result.isEmpty() || result.get(0) == null)
			return null;

		return new BigDecimal(result.get(0));		
	}


}