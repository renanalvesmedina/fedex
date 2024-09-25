package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.MotivoReprovacao;
import com.mercurio.lms.vendas.model.dao.MotivoReprovacaoDAO;

public class MotivoReprovacaoService extends CrudService<MotivoReprovacao, Long>{


	
	public List<MotivoReprovacao> findMotivosReprovacao(){
		return ((MotivoReprovacaoDAO) getDao()).findMotivosReprovacao();
	}
	
	public MotivoReprovacao findById(Long id) {
		return ((MotivoReprovacaoDAO) getDao()).findById(id);
	}
	
	public void setMotivoReprovacaoDAO(MotivoReprovacaoDAO motivoReprovacaoDAO) {
		this.setDao(motivoReprovacaoDAO);
	}
	public MotivoReprovacaoDAO getMotivoReprovacaoDAO() {
		return (MotivoReprovacaoDAO)getDao();
	}
}