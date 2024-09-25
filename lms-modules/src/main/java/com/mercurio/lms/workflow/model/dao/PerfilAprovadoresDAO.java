package com.mercurio.lms.workflow.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.workflow.model.PerfilAprovadores;

import java.util.List;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class PerfilAprovadoresDAO extends BaseCrudDao<PerfilAprovadores, Long> {

    protected Class getPersistentClass() {
        return PerfilAprovadores.class;
    }


    public List findByIdUsuarioPerfilUsuario(Long idUsuario) {
        StringBuilder hql = new StringBuilder()
                .append("select pu from ")
                .append(getPersistentClass().getName()).append(" as pu ")
                .append("join fetch pu.perfil as p where pu.usuario.idUsuario=?");
        return this.getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idUsuario});
    }

    public List findAllPerfis() {
        String tpSituacao = "A";
        StringBuilder hql = new StringBuilder()
                .append("select pu from ")
                .append(getPersistentClass().getName()).append(" as pu ")
                .append("where pu.tpSituacao=?");
        List retorno = this.getAdsmHibernateTemplate().find(hql.toString(), new Object[]{tpSituacao});
        return retorno;
    }

}







