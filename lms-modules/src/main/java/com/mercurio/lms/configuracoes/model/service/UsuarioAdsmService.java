package com.mercurio.lms.configuracoes.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.UsuarioAdsm;
import com.mercurio.lms.configuracoes.model.dao.UsuarioAdsmDAO;
import java.util.List;

public class UsuarioAdsmService extends CrudService<UsuarioAdsm, Long> {

    protected UsuarioAdsmDAO getUsuarioAdsmDAO() {
        return (UsuarioAdsmDAO) getDao();
    }

    public void setUsuarioAdsmDAO(UsuarioAdsmDAO dao) {
        setDao(dao);
    }

    public List findSuggestUsuario(String value, Integer limiteRegistro){
        return getUsuarioAdsmDAO().findSuggestUsuario(value, limiteRegistro);
    }

    public UsuarioAdsm findById(Long id){
        return getUsuarioAdsmDAO().findById(id);
    }

}
