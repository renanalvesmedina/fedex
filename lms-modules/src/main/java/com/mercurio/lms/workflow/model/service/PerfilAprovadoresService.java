package com.mercurio.lms.workflow.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.workflow.model.PerfilAprovadores;
import com.mercurio.lms.workflow.model.dao.PerfilAprovadoresDAO;

import java.util.List;

/**
 * Classe de servi�o para CRUD:
 *
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.workflow.perfilAprovadoresService"
 */
public class PerfilAprovadoresService extends CrudService<PerfilAprovadores, Long> {

    private PerfilAprovadoresDAO getPerfilAprovadoresDAO() {
        return (PerfilAprovadoresDAO) getDao();
    }

    public void setPerfilAprovadoresDAO (PerfilAprovadoresDAO dao) {
        setDao(dao);
    }

    public List findByIdUsuarioPerfilUsuario(Long idUsuario){
        return getPerfilAprovadoresDAO().findByIdUsuarioPerfilUsuario(idUsuario);
    }

    public List findAllPerfis(){
        List retorno = getPerfilAprovadoresDAO().findAllPerfis();
        return retorno;
    }
}
