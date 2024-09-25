package com.mercurio.lms.expedicao.util.nfe.converter;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.municipios.model.Municipio;

public class CancelamentoNteConverter extends CancelamentoConverter {

	private final Conhecimento conhecimento;

	public CancelamentoNteConverter(Conhecimento conhecimento, MonitoramentoDocEletronico monitoramentoDocEletronico, String versaoNfe) {
		super(monitoramentoDocEletronico, versaoNfe);
		this.conhecimento = conhecimento;
	}
	
	@Override
	protected DoctoServico getDoctoServico() {
		return conhecimento;
	}
	
	@Override
	protected Municipio getMunicipioPrestacaoServico() {
		return conhecimento.getMunicipioByIdMunicipioColeta();
	}
	
}
