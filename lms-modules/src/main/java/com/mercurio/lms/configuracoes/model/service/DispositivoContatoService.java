package com.mercurio.lms.configuracoes.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.DispositivoContato;
import com.mercurio.lms.configuracoes.model.dao.DispositivoContatoDAO;
import java.io.Serializable;

public class DispositivoContatoService extends CrudService<DispositivoContato, Long> {

    public void setDispositivoContatoDAO(DispositivoContatoDAO dao) {
        setDao(dao);
    }

    @Override
    protected DispositivoContatoDAO getDao() {
        return (DispositivoContatoDAO) super.getDao();
    }

    @Override
	public DispositivoContato findById(Long id) {
		return (DispositivoContato) super.findById(id);
	}

	@Override
	public Serializable store(DispositivoContato bean) {
		return super.store(bean);
	}

    public DispositivoContato findDispositivo(String token, String plataforma, String ddd, String numero) {
        return getDao().findDispositivo(token, plataforma, ddd, numero);
    }

    public DispositivoContato findDispositivoByToken(String token) {
        return getDao().findByToken(token);
    }
    
}