package com.mercurio.lms.carregamento.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.ArquivoLogProcessamento;
import com.mercurio.lms.carregamento.model.dao.ArquivoLogProcessamentoDAO;

/**
 * Classe de servi�o para o Arquivo Log Processamento. Atende a demanda LMS-1236
 * 
 * @author mxavier@voiza.com.br
 * @spring.bean id="lms.carregamento.arquivoLogProcessamentoService"
 */
public class ArquivoLogProcessamentoService extends CrudService<ArquivoLogProcessamento, Long> {

	@Override
	public void storeAll(List<ArquivoLogProcessamento> list) {
		super.storeAll(list);
	}

	@Override
	protected Serializable store(ArquivoLogProcessamento bean) {
		return super.store(bean);
	}
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setArquivoLogProcessamentoServicoDAO(ArquivoLogProcessamentoDAO dao) {
		setDao( dao );
	}
}