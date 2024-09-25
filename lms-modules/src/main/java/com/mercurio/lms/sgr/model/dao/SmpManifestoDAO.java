package com.mercurio.lms.sgr.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.SmpManifesto;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SmpManifestoDAO extends BaseCrudDao<SmpManifesto, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
	protected final Class getPersistentClass() {
		return SmpManifesto.class;
	}

	/**
	 * Remove o SmpManifesto pelo id da SMP
	 * @param idSolicMonitPreventivo
	 */
	public void removeByIdSolicMonitPreventivo(Long idSolicMonitPreventivo) {
		String s = "delete from " + 
			getPersistentClass().getName() + " as sm " +
			" where sm.id in (select smpm.id from " + 
			SmpManifesto.class.getName() + 
			" smpm where smpm.solicMonitPreventivo.id = ?) ";

		getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s).setParameter(0, idSolicMonitPreventivo).executeUpdate();
	}

}