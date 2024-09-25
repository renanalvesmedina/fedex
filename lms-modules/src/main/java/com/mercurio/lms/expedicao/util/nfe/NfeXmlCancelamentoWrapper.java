package com.mercurio.lms.expedicao.util.nfe;

import com.mercurio.lms.expedicao.util.AliasLayoutNfse;
import com.mercurio.lms.layoutNfse.model.cancelamento.Cancelamento;
import com.thoughtworks.xstream.XStream;

public class NfeXmlCancelamentoWrapper {
	
	private final Cancelamento cancelamento;
	
	public NfeXmlCancelamentoWrapper(Cancelamento cancelamento) {
		super();
		this.cancelamento = cancelamento;
	}


	public String generate() {

	    XStream xstream = new XStream();
		xstream = AliasLayoutNfse.createAliasCancelamento(xstream);
		

		return xstream.toXML(cancelamento);
		
	}
	
}
