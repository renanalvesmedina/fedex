package com.mercurio.lms.carregamento.model.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ArquivoLogRegistro;
import com.mercurio.lms.carregamento.model.dao.ArquivoLogRegistroDAO;

/**
 * Classe de servi�o para Arquivo Log Registro. Atende a demanda LMS-1236
 * 
 * @author mxavier@voiza.com.br
 * @spring.bean id="lms.carregamento.arquivoLogRegistroService"
 */
public class ArquivoLogRegistroService extends CrudService<ArquivoLogRegistro, Long> {

	@Override
	public void storeAll(List<ArquivoLogRegistro> list) {
		super.storeAll(list);
	}

	@Override
	protected Serializable store(ArquivoLogRegistro bean) {
		return super.store(bean);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Serializable storeWithNewTransaction(ArquivoLogRegistro bean) {
		return super.store(bean);
	}
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setArquivoLogRegistroServicoDAO(ArquivoLogRegistroDAO dao) {
		setDao( dao );
	}
	
	public ArquivoLogRegistroDAO getArquivoLogRegistroDAO() {
		return (ArquivoLogRegistroDAO)getDao();
	}
	
	public ResultSetPage findPaginatedConsultarLogCargaArquivosInHouse(TypedFlatMap criteria) {
		return getArquivoLogRegistroDAO().findPaginatedConsultarLogCargaArquivosInHouse(criteria);		
	}

	public Integer getRowCountConsultarLogCargaArquivosInHouse(TypedFlatMap criteria) {
		return getArquivoLogRegistroDAO().getRowCountConsultarLogCargaArquivosInHouse(criteria);
	}
}