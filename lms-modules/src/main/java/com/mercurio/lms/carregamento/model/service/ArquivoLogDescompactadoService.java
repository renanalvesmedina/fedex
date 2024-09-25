package com.mercurio.lms.carregamento.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ArquivoLogDescompactado;
import com.mercurio.lms.carregamento.model.dao.ArquivoLogDescompactadoDAO;

/**
 * Classe de serviço para o Arquivo Log Descompactado. Atende a demanda LMS-1236
 * 
 * @author mxavier@voiza.com.br
 * @spring.bean id="lms.carregamento.arquivoLogDescompactadoService"
 */
public class ArquivoLogDescompactadoService extends CrudService<ArquivoLogDescompactado, Long> {

	@Override
	public void storeAll(List<ArquivoLogDescompactado> list) {
		super.storeAll(list);
	}

	@Override
	protected Serializable store(ArquivoLogDescompactado bean) {
		return super.store(bean);
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setArquivoLogDescompactadoServicoDAO(ArquivoLogDescompactadoDAO dao) {
		setDao( dao );
	}
	
	public ArquivoLogDescompactadoDAO getArquivoLogDescompactadoServicoDAO() {
		return (ArquivoLogDescompactadoDAO)getDao();
	}
	
	public ResultSetPage findPaginatedConsultarLogCargaArquivosInHouse(TypedFlatMap criteria) {
		return getArquivoLogDescompactadoServicoDAO().findPaginatedConsultarLogCargaArquivosInHouse(criteria);
	}
	
	public Integer getRowCountConsultarLogCargaArquivosInHouse(TypedFlatMap criteria) {
		return getArquivoLogDescompactadoServicoDAO().getRowCountConsultarLogCargaArquivosInHouse(criteria);
	}
}