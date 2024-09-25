package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.NotaFiscalServicoDocumento;
import com.mercurio.lms.expedicao.model.dao.NotaFiscalServicoDocumentoDAO;

public class NotaFiscalServicoDocumentoService extends CrudService<NotaFiscalServicoDocumento, Long> {


	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param Instância do DAO.
	 */
	public void setNotaFiscalServicoDocumentoDAO(NotaFiscalServicoDocumentoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * @return Instância do DAO.
	 */
	@SuppressWarnings("unused")
	private NotaFiscalServicoDocumentoDAO getNotaFiscalServicoDocumentoDAO() {
		return (NotaFiscalServicoDocumentoDAO) getDao();
	}

	public List<NotaFiscalServicoDocumento> findByIdNotaFiscalServico(Long idDoctoServico) {
		return getNotaFiscalServicoDocumentoDAO().findByIdNotaFiscalServico(idDoctoServico);
	}
}