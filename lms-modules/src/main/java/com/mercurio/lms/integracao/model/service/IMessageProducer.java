package com.mercurio.lms.integracao.model.service;

import java.io.Serializable;

import br.com.tntbrasil.integracao.domains.endToEnd.EMonitoramentoDMN;

public interface IMessageProducer<T extends Serializable> {
	public EMonitoramentoDMN<T> build();
}
