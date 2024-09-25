package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.MotivoPreFaturaServico;
import com.mercurio.lms.expedicao.model.dao.MotivoPreFaturaServicoDAO;

public class MotivoPreFaturaServicoService extends CrudService<MotivoPreFaturaServico, Long> {
	
	public void setPreFaturaServicoDAO(MotivoPreFaturaServicoDAO motivoPreFaturaServicoDAO) {
		setDao( motivoPreFaturaServicoDAO );
	}

	private MotivoPreFaturaServicoDAO getMotivoPreFaturaServicoDAO() {
		return (MotivoPreFaturaServicoDAO) getDao();
	}
	
	public MotivoPreFaturaServico findByDsMotivoPreFaturaServico() {
		return getMotivoPreFaturaServicoDAO().findByDsMotivoPreFaturaServico();
	}
}
