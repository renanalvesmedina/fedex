package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DificuldadeColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.DificuldadeColetaEntregaDAO;

/**
 * Service responsável Dificuldade de Coleta e Entrega de um cliente
 * @author AlessandroSF
 */
public class DificuldadeColetaEntregaService extends CrudService<DificuldadeColetaEntrega, Long> {

	private DificuldadeColetaEntregaDAO dificuldadeColetaEntregaDAO;

	public DificuldadeColetaEntregaDAO getDificuldadeColetaEntregaDAO() {
		return dificuldadeColetaEntregaDAO;
	}

	public void setDificuldadeColetaEntregaDAO(DificuldadeColetaEntregaDAO dificuldadeColetaEntregaDAO) {
		this.dificuldadeColetaEntregaDAO = dificuldadeColetaEntregaDAO;
	}
	
	
	public DificuldadeColetaEntrega findByCliente(Long idCliente) {
		return dificuldadeColetaEntregaDAO.findByCliente(idCliente);
	}
}