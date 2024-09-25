package com.mercurio.lms.sim.model.service;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.ObservacaoMercadoria;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.sim.model.dao.ObservacaoMercadoriaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class ObservacaoMercadoriaService extends CrudService<ObservacaoMercadoria, Long> {
	
	private UsuarioLMSService usuarioService;

	public UsuarioLMSService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioLMSService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public Serializable saveObservacao(Long idDoctoServico, String dsObservacao) {
		DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
		
		UsuarioLMS usuario = usuarioService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());

		DoctoServico doctoServico = new DoctoServico();
		doctoServico.setIdDoctoServico(idDoctoServico);

		ObservacaoMercadoria om = new ObservacaoMercadoria();
		om.setDoctoServico(doctoServico);
		om.setDsObservacao(dsObservacao);
		
		om.setUsuarioInclusao(usuario);
		om.setDhInclusao(dhAtual);
		om.setUsuarioAlteracao(usuario);
		om.setDhAlteracao(dhAtual);
		
		store(om);
		return om.getIdObservacaoMercadoria();
	}
	
	public String findSgFilialFromIdUsuario(Long idUsuario) {
		return getObservacaoMercadoriaDao().findSgFilialFromIdUsuario(idUsuario);
	}

	public List<ObservacaoMercadoria> findByDoctoServico(Long idDoctoServico) {
		return getObservacaoMercadoriaDao().findByDoctoServico(idDoctoServico);
	}
	
	public ObservacaoMercadoriaDAO getObservacaoMercadoriaDao() {
		return (ObservacaoMercadoriaDAO) getDao();
	}

	public void setObservacaoMercadoriaDao(ObservacaoMercadoriaDAO observacaoMercadoriaDao) {
		setDao(observacaoMercadoriaDao);
	}
	
}
