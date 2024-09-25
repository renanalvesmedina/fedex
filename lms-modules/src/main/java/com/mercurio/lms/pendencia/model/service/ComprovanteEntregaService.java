package com.mercurio.lms.pendencia.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.ComprovanteEntrega;
import com.mercurio.lms.carregamento.model.dao.ComprovanteEntregaDAO;
import com.mercurio.lms.pendencia.model.dao.OcorrenciaPendenciaDAO;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.pendencia.ocorrenciaPendenciaService"
 */
public class ComprovanteEntregaService extends CrudService<ComprovanteEntrega, Long> {

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
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
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param instância do DAO.
	 */
	public void setComprovanteEntregaDAO(ComprovanteEntregaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos
	 * dados deste serviço.
	 *
	 * @return instância do DAO.
	 */
	private OcorrenciaPendenciaDAO getOcorrenciaPendenciaDAO() {
		return (OcorrenciaPendenciaDAO) getDao();
	}
}