package com.mercurio.lms.workflow.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.workflow.model.UsuarioAprovador;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class UsuarioAprovadorDAO extends BaseCrudDao<UsuarioAprovador, Long> {

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    @Transactional
    protected Class getPersistentClass() {
        return UsuarioAprovador.class;
    }

    public List findUsuarioSuggest(String usuario){
        StringBuilder hql = new StringBuilder()
                .append("SELECT new Map(")
                .append("u.nmUsuario AS nmUsuario, ")
                .append("u.login AS login) ")
                .append("FROM ").append(getPersistentClass().getName()).append(" AS u ")
                .append("WHERE LOWER(u.nmUsuario) LIKE '%").append(usuario.toLowerCase()).append("%'")
                .append("OR LOWER(u.login) = '").append(usuario.toLowerCase()).append("%'");

        List result = getAdsmHibernateTemplate().find(hql.toString());

        return result;

    }


}
