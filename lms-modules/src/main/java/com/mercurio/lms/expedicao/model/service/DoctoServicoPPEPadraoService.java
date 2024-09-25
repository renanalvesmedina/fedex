package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.DoctoServicoPPEPadrao;
import com.mercurio.lms.expedicao.model.dao.DoctoServicoPPEPadraoDAO;
import com.mercurio.lms.util.session.SessionUtils;

public class DoctoServicoPPEPadraoService extends CrudService<DoctoServicoPPEPadrao, Long>{
	
	public void setDoctoServicoPPEPadraoDAO(DoctoServicoPPEPadraoDAO dao) {
		setDao( dao );
	}
	
	private DoctoServicoPPEPadraoDAO getDoctoServicoPPEPadraoDAO() {
		return (DoctoServicoPPEPadraoDAO) getDao();
	}
	
	public Serializable store(DoctoServicoPPEPadrao bean) {
		return super.store(bean);
	}
	
	public DoctoServicoPPEPadrao findById(Long id) {
		return (DoctoServicoPPEPadrao) super.findById(id);
	}

	public DoctoServicoPPEPadrao findByIdDoctoServico(Long idDoctoServico) {
		return getDoctoServicoPPEPadraoDAO().findByIdDoctoServico(idDoctoServico);
	}
	
	public void updateDoctoServicoPPEPadrao(Long idDoctoServico, Long nrDiasColeta, Long nrDiasEntrega, Long nrDiasTransferencia) {
		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		nrDiasColeta = (nrDiasColeta != null) ? nrDiasColeta.longValue() : 0;
		nrDiasEntrega = (nrDiasEntrega != null) ? nrDiasEntrega.longValue() : 0;
		nrDiasTransferencia = (nrDiasTransferencia != null) ? nrDiasTransferencia.longValue() : 0;
		
		DoctoServicoPPEPadrao doctoServicoPPEPadrao = this.findByIdDoctoServico(idDoctoServico);
		
		if(doctoServicoPPEPadrao == null){
			doctoServicoPPEPadrao = new DoctoServicoPPEPadrao();
			DoctoServico doctoServico = new DoctoServico();
			doctoServico.setIdDoctoServico(idDoctoServico);
			doctoServicoPPEPadrao.setDoctoServico(doctoServico);
			doctoServicoPPEPadrao.setUsuarioInclusao(usuarioLMS);
		}
		doctoServicoPPEPadrao.setUsuarioAlteracao(usuarioLMS);
		doctoServicoPPEPadrao.setNrDiasColeta(nrDiasColeta.shortValue());		
		doctoServicoPPEPadrao.setNrDiasEntrega(nrDiasEntrega.shortValue());	
		doctoServicoPPEPadrao.setNrDiasTransferencia(nrDiasTransferencia.shortValue());	
		
		super.store(doctoServicoPPEPadrao);
		getDao().flush();
	}
	
}
