package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.NotaFiscalServicoDocumento;
import com.mercurio.lms.expedicao.model.dao.NotaFiscalServicoDocumentoDAO;

public class NotaFiscalServicoDocumentoService extends CrudService<NotaFiscalServicoDocumento, Long> {


	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @param Inst�ncia do DAO.
	 */
	public void setNotaFiscalServicoDocumentoDAO(NotaFiscalServicoDocumentoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @return Inst�ncia do DAO.
	 */
	@SuppressWarnings("unused")
	private NotaFiscalServicoDocumentoDAO getNotaFiscalServicoDocumentoDAO() {
		return (NotaFiscalServicoDocumentoDAO) getDao();
	}

	public List<NotaFiscalServicoDocumento> findByIdNotaFiscalServico(Long idDoctoServico) {
		return getNotaFiscalServicoDocumentoDAO().findByIdNotaFiscalServico(idDoctoServico);
	}
}