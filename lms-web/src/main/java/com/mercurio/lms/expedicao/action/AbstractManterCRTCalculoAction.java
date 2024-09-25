package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.util.CtoInternacionalUtils;

public abstract class AbstractManterCRTCalculoAction extends CrudAction {
	
	public void removeInSession(){
		CtoInternacional ctoInternacional = getCrtInSession();

		ctoInternacional.setParcelaDoctoServicos(null);
		ctoInternacional.setVlTotalDocServico(null);
		ctoInternacional.setVlTotalParcelas(null);
		ctoInternacional.setVlTotalServicos(null);
		ctoInternacional.setPsReferenciaCalculo(null);
		ctoInternacional.setVlDesconto(null);
		ctoInternacional.setTabelaPreco(null);
		ctoInternacional.setTarifaPreco(null);
		ctoInternacional.setMoeda(null);
		ctoInternacional.setDivisaoCliente(null);

		CtoInternacionalUtils.setCtoInternacionalInSession(ctoInternacional);
	}

	public void cancelarTudo(){
		CtoInternacionalUtils.removeCtoInternacionalFromSession();
	}

	protected CtoInternacional getCrtInSession(){
		return CtoInternacionalUtils.getCtoInternacionalInSession();
	}
}
