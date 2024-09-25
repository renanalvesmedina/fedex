package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.DadoModeloMensagem;
import com.mercurio.lms.contasreceber.model.dao.DadoModeloMensagemDAO;

public class DadoModeloMensagemService extends CrudService<DadoModeloMensagem, Long> {

	private DadoModeloMensagemDAO dadoModeloMensagemDao;

	public Serializable store(DadoModeloMensagem bean){
		return super.store(bean);
	}
	public DadoModeloMensagem findById(Long id){
		return (DadoModeloMensagem) super.findById(id);
	}
	public void setDadoModeloMensagemDAO(DadoModeloMensagemDAO dadoModeloMensagemDAO) {
		this.dadoModeloMensagemDao = dadoModeloMensagemDAO;
		setDao( this.dadoModeloMensagemDao );
	}

}