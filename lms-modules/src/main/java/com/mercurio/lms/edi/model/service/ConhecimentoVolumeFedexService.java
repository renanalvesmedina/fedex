package com.mercurio.lms.edi.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.ConhecimentoVolumeFedex;
import com.mercurio.lms.edi.model.dao.ConhecimentoVolumeFedexDAO;

public class ConhecimentoVolumeFedexService extends CrudService<ConhecimentoVolumeFedex, Long> {

	@Override
	protected ConhecimentoVolumeFedexDAO getDao() {
		return (ConhecimentoVolumeFedexDAO) super.getDao();
	}
	
	public void setConhecimentoVolumeFedexDAO(ConhecimentoVolumeFedexDAO dao) {
		this.setDao(dao);
	}

	@Override
    public Serializable store(ConhecimentoVolumeFedex bean) {
		if(bean.getIdVolumeConhecimentoFedex() == null){
			bean.setIdVolumeConhecimentoFedex(getDao().findSequence());
		}
		return super.store(bean);
	}
	

}
