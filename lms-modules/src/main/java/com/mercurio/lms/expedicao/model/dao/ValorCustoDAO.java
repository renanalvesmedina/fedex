package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ValorCusto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ValorCustoDAO extends BaseCrudDao<ValorCusto, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<ValorCusto> getPersistentClass() {
		return ValorCusto.class;
	}

	public void removeByIdDoctoServico(Long id, Boolean isFlushSession){
		StringBuilder hql = new StringBuilder()
		.append("delete	").append(getPersistentClass().getName()).append("\n")
		.append("where	doctoServico = :id");

		DoctoServico doctoServico = new CtoInternacional();
		doctoServico.setIdDoctoServico(id);

		getAdsmHibernateTemplate().removeById(hql.toString(), doctoServico);

		if(isFlushSession.booleanValue()) 
			getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}

}