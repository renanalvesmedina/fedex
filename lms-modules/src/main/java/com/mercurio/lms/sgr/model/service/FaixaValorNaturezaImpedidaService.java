package com.mercurio.lms.sgr.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.sgr.model.FaixaValorNaturezaImpedida;
import com.mercurio.lms.sgr.model.dao.FaixaValorNaturezaImpedidaDAO;

/**
 * @spring.bean id="lms.sgr.faixaValorNaturezaImpedidaService"
 */
public class FaixaValorNaturezaImpedidaService extends CrudService<FaixaValorNaturezaImpedida, Long> {

	public void setFaixaValorNaturezaImpedidaDAO(FaixaValorNaturezaImpedidaDAO dao) {
		setDao(dao);
	}

    @Override
	public FaixaValorNaturezaImpedida findById(Long id) {
		return (FaixaValorNaturezaImpedida) super.findById(id);
	}

	@Override
	public Serializable store(FaixaValorNaturezaImpedida bean) {
		return super.store(bean);
	}

	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

}
