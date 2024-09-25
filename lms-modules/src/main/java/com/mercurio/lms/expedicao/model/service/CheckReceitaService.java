package com.mercurio.lms.expedicao.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.lms.expedicao.model.dao.CheckReceitaDAO;

/**
 * Servico responsável pela Geração e Envio do relatorio de receitas
 * 
 * @author André Valadas
 * @since 15/04/2011
 * @spring.bean id="lms.expedicao.checkReceitaService"
 */
public class CheckReceitaService {

	private CheckReceitaDAO checkReceitaDAO;

	/**
	 * Busca dados da Receita
	 */
	public List<Map> findCheckReceitaMapped() {
		return checkReceitaDAO.findCheckReceitaMapped();
	}

	/**
	 * @param checkReceitaDAO
	 *            the checkReceitaDAO to set
	 */
	public void setCheckReceitaDAO(CheckReceitaDAO checkReceitaDAO) {
		this.checkReceitaDAO = checkReceitaDAO;
	}
}