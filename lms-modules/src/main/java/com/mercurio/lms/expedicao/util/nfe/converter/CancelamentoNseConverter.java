package com.mercurio.lms.expedicao.util.nfe.converter;

import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.municipios.model.Municipio;

public class CancelamentoNseConverter extends CancelamentoConverter {

	private NotaFiscalServico nfs;

	public CancelamentoNseConverter(NotaFiscalServico nfs, MonitoramentoDocEletronico monitoramentoDocEletronico, String versaoNfe) {
		super(monitoramentoDocEletronico, versaoNfe);
		this.nfs = nfs;
	}
	
	@Override
	protected DoctoServico getDoctoServico() {
		return nfs;
	}
	
	@Override
	protected Municipio getMunicipioPrestacaoServico() {
		return nfs.getMunicipio();
	}
	
}
