package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.CodigoMunicipioFilial;
import com.mercurio.lms.expedicao.model.dao.CodigoMunicipioFilialDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;

public class CodigoMunicipioFilialService extends CrudService<CodigoMunicipioFilial, Long> {
    
	public CodigoMunicipioFilial findByFilialMunicipio(Filial filial, Municipio municipio) {
		return getCodigoMunicipioFilialDAO().findByFilialMunicipio(filial, municipio);
	}

	private CodigoMunicipioFilialDAO getCodigoMunicipioFilialDAO() {
		return (CodigoMunicipioFilialDAO) getDao();
	}
	
	public void setCodigoMunicipioFilialDAO(CodigoMunicipioFilialDAO dao) {
		setDao( dao );
	}
	
}
