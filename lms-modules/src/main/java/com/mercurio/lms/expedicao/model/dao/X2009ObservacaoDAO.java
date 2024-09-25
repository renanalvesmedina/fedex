package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.X2009Observacao;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class X2009ObservacaoDAO extends BaseCrudDao<com.mercurio.lms.expedicao.model.X2009Observacao, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return X2009Observacao.class;
	}
	
	public List findDescricaoByCodObservacao(YearMonthDay dtEmissaoConhecimento,String codObservacao){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("descricao");		
		hql.addFrom(X2009Observacao.class.getName() + " x ");
		hql.addCriteria("x.codObservacao","=",codObservacao);
		hql.addCriteria("x.validObservacao", "<=", dtEmissaoConhecimento);
		return (List) getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
}