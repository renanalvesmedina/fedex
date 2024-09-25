package com.mercurio.lms.pendencia.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.ComprovanteEntrega;
import com.mercurio.lms.carregamento.model.dao.ComprovanteEntregaDAO;
import com.mercurio.lms.pendencia.model.dao.OcorrenciaPendenciaDAO;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * servi�o.
 * 
 * @spring.bean id="lms.pendencia.ocorrenciaPendenciaService"
 */
public class ComprovanteEntregaService extends CrudService<ComprovanteEntrega, Long> {

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ComprovanteEntrega bean) {
		if (bean.getIdComprovanteEntrega() == null) {
			throw new BusinessException("LMS-17001");
		}
		return super.store(bean);
	}

	public void storeAssinatura(ComprovanteEntrega comprovanteEntrega) {
		getOcorrenciaPendenciaDAO().store(comprovanteEntrega);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param inst�ncia do DAO.
	 */
	public void setComprovanteEntregaDAO(ComprovanteEntregaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos
	 * dados deste servi�o.
	 *
	 * @return inst�ncia do DAO.
	 */
	private OcorrenciaPendenciaDAO getOcorrenciaPendenciaDAO() {
		return (OcorrenciaPendenciaDAO) getDao();
	}
}