package com.mercurio.lms.expedicao.model.service;

import com.mercurio.lms.expedicao.model.dao.CalculoParcelaNFServicoDAO;


/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.calculoParcelaNFServicoService" autowire="no"
 * @spring.property name="calculoParcelaNFServicoDAO" ref="lms.expedicao.calculoParcelaNFServicoDAO"
 * @spring.property name="calculoNFServicoService" ref="lms.expedicao.calculoNFServicoService"
 */
public class CalculoParcelaNFServicoService extends CalculoParcelaServicoService {

	public CalculoParcelaNFServicoDAO getCalculoParcelaNFServicoDAO() {
		return (CalculoParcelaNFServicoDAO) super.getCalculoParcelaServicoDAO();
	}
	public void setCalculoParcelaNFServicoDAO(CalculoParcelaNFServicoDAO calculoParcelaNFServicoDAO) {
		super.setCalculoParcelaServicoDAO(calculoParcelaNFServicoDAO);
	}

	public CalculoNFServicoService getCalculoNFServicoService() {
		return (CalculoNFServicoService) super.getCalculoServicoService();
	}
	public void setCalculoNFServicoService(CalculoNFServicoService calculoNFServicoService) {
		super.setCalculoServicoService(calculoNFServicoService);
	}

}
