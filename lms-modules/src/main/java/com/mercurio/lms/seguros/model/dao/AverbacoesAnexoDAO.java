package com.mercurio.lms.seguros.model.dao;

import java.util.List;

import org.hibernate.Query;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.seguros.model.AverbacaoAnexo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean com.mercurio.lms.seguros.model.dao.AverbacoesAnexoDAO
 */
public class AverbacoesAnexoDAO extends BaseCrudDao<AverbacaoAnexo, Long>{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected Class getPersistentClass() {
		return AverbacaoAnexo.class;
	}
	
	public List<AverbacaoAnexo> findAnexosByIdAverbacao(Long idAverbacao) {
		SqlTemplate hql = new SqlTemplate();		
   		hql.addInnerJoin(getPersistentClass().getName() , "averbacaoAnexo");
   		hql.addInnerJoin("fetch averbacaoAnexo.usuario","usuario");
   		hql.addInnerJoin("fetch usuario.usuarioADSM","usuarioADSM");
   		hql.addInnerJoin("fetch averbacaoAnexo.averbacao","averbacao");
   		
   		hql.addCriteria("averbacaoAnexo.averbacao.idAverbacao", "=", idAverbacao);
   		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public Integer getRowCountAnexosByIdAverbacao(Long idAverbacao){
		SqlTemplate hql = new SqlTemplate();		
   		hql.addInnerJoin(getPersistentClass().getName() , "averbacaoAnexo");
   		hql.addInnerJoin("averbacaoAnexo.usuario","usuario");
   		hql.addInnerJoin("averbacaoAnexo.averbacao","averbacao");
   		
   		hql.addCustomCriteria("averbacaoAnexo.averbacao.idAverbacao = ? ");
		
		Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("select count(*) as rowCount " + 
				hql.toString());
		q.setParameter(0, idAverbacao);
		return Integer.valueOf( ((Long)q.uniqueResult()).intValue() );
	}
	
}
