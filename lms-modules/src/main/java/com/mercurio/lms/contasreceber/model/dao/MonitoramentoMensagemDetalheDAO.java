package com.mercurio.lms.contasreceber.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemDetalhe;

public class MonitoramentoMensagemDetalheDAO  extends BaseCrudDao<MonitoramentoMensagemDetalhe, Long> {

	@Override
	protected Class getPersistentClass() {
		return MonitoramentoMensagemDetalhe.class;
	}

}