package com.mercurio.lms.contasreceber.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemEvento;

public class MonitoramentoMensagemEventoDAO  extends BaseCrudDao<MonitoramentoMensagemEvento, Long> {

	@Override
	protected Class getPersistentClass() {
		return MonitoramentoMensagemEvento.class;
	}
	
}