package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.vendas.model.ComissaoProvisionada;
import com.mercurio.lms.vendas.model.dao.ComissaoProvisionadaDAO;

public class ComissaoProvisionadaService extends CrudService<ComissaoProvisionada, Long> {

	public void findDadosForDemonstrativo(Long idExecutivo, Integer mesVigencia, Integer anoVigencia, DomainValue tpModal, Long idFilial) {
		List<ComissaoProvisionada> comissoes = getComissaoProvisionadaDao().findDadosForDemonstrativo(idExecutivo, mesVigencia, anoVigencia, tpModal, idFilial);
		
	}
	
	private BigDecimal getFob(ComissaoProvisionada c) {
		if ("FOB".equals(c.getTpDocFrete())) {
			return c.getVlComissaoCalculada();
		} 
		return new BigDecimal(0);
	}


	private BigDecimal getCif(ComissaoProvisionada c) {
		if ("CIF".equals(c.getTpDocFrete())) {
			return c.getVlComissaoCalculada();
		} 
		return new BigDecimal(0);
	}


	public void setComissaoProvisionadaDao(ComissaoProvisionadaDAO dao) {
		setDao(dao);
	}

	public ComissaoProvisionadaDAO getComissaoProvisionadaDao() {
		return (ComissaoProvisionadaDAO) getDao();
	}

	
}
