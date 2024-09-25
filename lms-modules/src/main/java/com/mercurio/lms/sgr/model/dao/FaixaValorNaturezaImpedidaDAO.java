package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.FaixaValorNaturezaImpedida;

/**
 * @spring.bean
 */
public class FaixaValorNaturezaImpedidaDAO extends BaseCrudDao<FaixaValorNaturezaImpedida, Long> {

	protected final Class<FaixaValorNaturezaImpedida> getPersistentClass() {
		return FaixaValorNaturezaImpedida.class;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("naturezaProduto", FetchMode.JOIN);
	}    

}
