package com.mercurio.lms.portaria.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.lms.portaria.model.dao.OperacoesPortuariasDAO;

public class OperacoesPortuariasService {
	private OperacoesPortuariasDAO operacoesPortuariasDAO;
	
	
	public List<Map<String, Object>> findGridEntrega(Long idFilial){
		return operacoesPortuariasDAO.findGridEntrega(idFilial);
	}

	public List<Map<String, Object>> findGridColeta(Long idFilial){
		return operacoesPortuariasDAO.findGridColeta(idFilial);
	}

	public OperacoesPortuariasDAO getOperacoesPortuariasDAO() {
		return operacoesPortuariasDAO;
	}

	public void setOperacoesPortuariasDAO(
			OperacoesPortuariasDAO operacoesPortuariasDAO) {
		this.operacoesPortuariasDAO = operacoesPortuariasDAO;
	}

}
