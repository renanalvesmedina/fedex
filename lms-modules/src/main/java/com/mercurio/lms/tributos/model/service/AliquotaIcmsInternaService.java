package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.AliquotaIcmsInterna;
import com.mercurio.lms.tributos.model.dao.AliquotaIcmsInternaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

public class AliquotaIcmsInternaService extends CrudService<AliquotaIcmsInterna, Long> {

	public AliquotaIcmsInterna findByIdUnidadeFederativa(Long idUfCtoMunicipioEntrega) {
		return getAliquotaIcmsInternaDAO().findByIdUnidadeFederativa(idUfCtoMunicipioEntrega, JTDateTimeUtils.getDataAtual());
	}

	public void setAliquotaIcmsInternaDAO(AliquotaIcmsInternaDAO aliquotaIcmsInternaDAO) {
		super.setDao(aliquotaIcmsInternaDAO);
	}

	public AliquotaIcmsInternaDAO getAliquotaIcmsInternaDAO() {
		return (AliquotaIcmsInternaDAO)getDao();
	}
}
