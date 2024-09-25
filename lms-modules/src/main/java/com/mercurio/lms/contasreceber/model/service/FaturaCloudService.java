package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.FaturaCloud;
import com.mercurio.lms.contasreceber.model.dao.FaturaCloudDAO;

public class FaturaCloudService extends CrudService<FaturaCloud, Long> {

	@Override
	public void flush() {
		getFaturaCloudDAO().flush();

	}

	@Override
	public FaturaCloud findById(Long id) {
		return (FaturaCloud) super.findById(id);

	}

	@Override
	public java.io.Serializable store(FaturaCloud faturaCloud) {
		getFaturaCloudDAO().store(faturaCloud, true);
		
		return faturaCloud;
	}

	public FaturaCloud findByFatura(Long idFatura) {
		return getFaturaCloudDAO().findByFatura(idFatura);

	}

	public FaturaCloud storeBasic(FaturaCloud faturaCloud) {
		return getFaturaCloudDAO().storeBasic(faturaCloud);

	}

	private FaturaCloudDAO getFaturaCloudDAO() {
		return (FaturaCloudDAO) getDao();
		
	}

	public void setFaturaCloudDAO(FaturaCloudDAO dao) {
		setDao(dao);
		
	}
}
