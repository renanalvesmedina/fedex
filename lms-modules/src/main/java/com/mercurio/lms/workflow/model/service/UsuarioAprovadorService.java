package com.mercurio.lms.workflow.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.dao.UsuarioDAO;

import java.util.List;


/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.workflow.usuarioAprovadorService"
 */
public class UsuarioAprovadorService extends CrudService<Usuario, Long> {

    protected UsuarioDAO getUsuarioDAO() {
        return (UsuarioDAO) getDao();
    }

    public void setUsuarioDAO(UsuarioDAO dao) {
        setDao(dao);
    }

    public List findUsuarioByLogin(String login){
        return getUsuarioDAO().findUsuarioByLogin(login);
    }


}
