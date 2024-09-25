package com.mercurio.lms.expedicao.util.nfe.converter;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.util.nfe.NfeConverterUtils;
import com.mercurio.lms.layoutNfse.model.cancelamento.Cancelamento;
import com.mercurio.lms.layoutNfse.model.cancelamento.InfPedidoCancelamento;
import com.mercurio.lms.municipios.model.Municipio;

public abstract class CancelamentoConverter {
	
	private final MonitoramentoDocEletronico monitoramentoDocEletronico;
	private final String versaoNfe;
	protected abstract DoctoServico getDoctoServico();
	protected abstract Municipio getMunicipioPrestacaoServico();
	
	public CancelamentoConverter(MonitoramentoDocEletronico monitoramentoDocEletronico, String versaoNfe) {
		this.monitoramentoDocEletronico = monitoramentoDocEletronico;
		this.versaoNfe = versaoNfe;
	}
	
	public Cancelamento convert() {
		return createCancelamento();
	}
	
	private Cancelamento createCancelamento() {
		Cancelamento cancelamento = new Cancelamento();
		cancelamento.setInfPedidoCancelamento(createInfPedidoCancelamento());
		return cancelamento;
	}
	private InfPedidoCancelamento createInfPedidoCancelamento() {
		InfPedidoCancelamento infPedidoCancelamento = new InfPedidoCancelamento();
		if (getDoctoServico().getFilialByIdFilialOrigem() != null) {
			infPedidoCancelamento.setId("Canc_Id");			
			infPedidoCancelamento.setCnpj(StringUtils.leftPad(getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao(), 14, '0'));
			infPedidoCancelamento.setInscricaoMunicipal(getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getNrInscricaoMunicipal());
		}
		infPedidoCancelamento.setNumero(monitoramentoDocEletronico.getNrDocumentoEletronico());
		if (getMunicipioPrestacaoServico() != null) {
			infPedidoCancelamento.setCodigoMunicipio(NfeConverterUtils.formatCMun(getMunicipioPrestacaoServico()));
		}
		infPedidoCancelamento.setCodigoCancelamento("2");
		infPedidoCancelamento.setVersao(versaoNfe);

		return infPedidoCancelamento;
	}

}
