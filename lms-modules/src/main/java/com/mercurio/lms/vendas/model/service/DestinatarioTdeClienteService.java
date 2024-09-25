package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.DestinatarioTdeCliente;
import com.mercurio.lms.vendas.model.dao.DestinatarioTdeClienteDAO;

public class DestinatarioTdeClienteService extends CrudService<DestinatarioTdeCliente, Long> {

	@Override
	protected Serializable store(DestinatarioTdeCliente bean) {
		return super.store(bean);
	}

	public DestinatarioTdeClienteDAO getDestinatarioTdeClienteDAO() {
		return (DestinatarioTdeClienteDAO)getDao();
	}

	public void setDestinatarioTdeClienteDAO(DestinatarioTdeClienteDAO destinatarioTDEClienteDAO) {
		this.setDao(destinatarioTDEClienteDAO);
	}

	public DestinatarioTdeCliente findByIdTdeClienteAndIdClienteDestinatario(Long idTdeCliente, Long idClienteDestinatario) {
		return getDestinatarioTdeClienteDAO().findByIdTdeClienteAndIdClienteDestinatario(idTdeCliente, idClienteDestinatario);
	}
	
}
