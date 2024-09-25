package com.mercurio.lms.layoutNfse.model.cancelamento;

public class Cancelamento {
	
	private final String xsi = "http://www.w3.org/2001/XMLSchema-instance";
	private final String xsd = "http://www.w3.org/2001/XMLSchema";
	
	private InfPedidoCancelamento InfPedidoCancelamento;

	public InfPedidoCancelamento getInfPedidoCancelamento() {
		return InfPedidoCancelamento;
	}

	public void setInfPedidoCancelamento(InfPedidoCancelamento infPedidoCancelamento) {
		this.InfPedidoCancelamento = infPedidoCancelamento;
	}

	public String getXsd() {
		return xsd;
	}
	
	public String getXsi() {
		return xsi;
	}
	
}
