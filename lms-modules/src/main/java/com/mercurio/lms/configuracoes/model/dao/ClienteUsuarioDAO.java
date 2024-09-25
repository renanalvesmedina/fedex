package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.ClienteUsuario;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ClienteUsuarioDAO extends BaseCrudDao<ClienteUsuario, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ClienteUsuario.class;
    }

    public List findByIdUsuario(Long id){
		StringBuffer sb = new StringBuffer();
		sb.append("from " + getPersistentClass().getName() + " as p ");
		sb.append("join fetch p.usuario as user ");
		sb.append("join fetch p.cliente as cliente ");
		sb.append("join fetch cliente.pessoa as pessoa ");
		sb.append("WHERE user.idUsuario = ?");
		
    	return getAdsmHibernateTemplate().find(sb.toString(),id); 
    }

}
