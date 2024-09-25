package com.mercurio.lms.carregamento.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.ArquivoLogCompactado;
import com.mercurio.lms.carregamento.model.dao.ArquivoLogCompactadoDAO;

/**
 * Classe de serviço para o Arquivo Log Compactado. Atende a demanda LMS-1236
 * 
 * @author mxavier@voiza.com.br
 * @spring.bean id="lms.carregamento.arquivoLogCompactadoService"
 */
public class ArquivoLogCompactadoService extends CrudService<ArquivoLogCompactado, Long> {

	@Override
	public void storeAll(List<ArquivoLogCompactado> list) {
		super.storeAll(list);
	}

	@Override
	protected Serializable store(ArquivoLogCompactado bean) {
		return super.store(bean);
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setArquivoLogCompactadoServicoDAO(ArquivoLogCompactadoDAO dao) {
		setDao( dao );
	}
}