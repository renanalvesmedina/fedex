package com.mercurio.lms.expedicao.model.service;

import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.municipios.model.Filial;

public class DoctoServicoDadosClienteBuilder {
	
	DoctoServicoDadosCliente dadosCliente;
	
	private DoctoServicoDadosClienteBuilder() {
		dadosCliente = new DoctoServicoDadosCliente();
	}
	
	public static DoctoServicoDadosClienteBuilder novosDados() {
		return new DoctoServicoDadosClienteBuilder();
	}
	
	public DoctoServicoDadosClienteBuilder ufRementente(Long uf) {
		dadosCliente.setIdUfRemetente(uf);
		return this;
	}
	
	public DoctoServicoDadosClienteBuilder situacaoTributariaRemetente(String situacaoTributaria) {
		dadosCliente.setTpSituacaoTributariaRemetente(situacaoTributaria);
		return this;
	}
	
	public DoctoServicoDadosClienteBuilder situacaoTributariaDestinatario(String situacaoTributaria) {
		dadosCliente.setTpSituacaoTributariaDestinatario(situacaoTributaria);
		return this;
	}
	
	public DoctoServicoDadosClienteBuilder filialTransacao(Filial filial) {
		dadosCliente.setFilialTransacao(filial);
		return this;
	}
	
	public DoctoServicoDadosCliente build() {
		return dadosCliente;
	}
}
