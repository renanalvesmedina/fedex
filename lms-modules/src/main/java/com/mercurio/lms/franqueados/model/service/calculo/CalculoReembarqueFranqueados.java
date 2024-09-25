package com.mercurio.lms.franqueados.model.service.calculo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.ReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

public class CalculoReembarqueFranqueados implements ICalculoFranquiado {

	private Long idFranquia;
	private CalculoFranqueadoParametros parametrosFranqueado;
	private CalculoReembarqueFranqueadoDTO reembarqueFranqueadoDTO;
	private ReembarqueDoctoServicoFranqueado reembarqueDoctoServicoFranqueado;

	public CalculoReembarqueFranqueados() {
		super();
		reembarqueDoctoServicoFranqueado = new ReembarqueDoctoServicoFranqueado();
	}

	@Override
	public void executarCalculo() {

		BigDecimal vlToneladaCalculado = BigDecimal.ZERO;
		BigDecimal pesoAferido = BigDecimal.ZERO;

		if (reembarqueFranqueadoDTO.getPsReal() != null) {
			pesoAferido = reembarqueFranqueadoDTO.getPsReal();
			vlToneladaCalculado = FranqueadoUtils
					.calcularTonelada(parametrosFranqueado.getReembarqueFranqueado().getPcTonelada(), pesoAferido);
		}

		Franquia franquia = new Franquia();
		franquia.setIdFranquia(idFranquia);
		reembarqueDoctoServicoFranqueado.setFranquia(franquia);

		DoctoServico doctoServico = new DoctoServico();
		doctoServico.setIdDoctoServico(reembarqueFranqueadoDTO.getIdDoctoServico());
		reembarqueDoctoServicoFranqueado.setConhecimento(doctoServico);

		Manifesto manifesto = new Manifesto();
		manifesto.setIdManifesto(reembarqueFranqueadoDTO.getIdManifesto());
		reembarqueDoctoServicoFranqueado.setManifesto(manifesto);

		reembarqueDoctoServicoFranqueado.setDtCompetencia(parametrosFranqueado.getCompetencia().getInicio());
		reembarqueDoctoServicoFranqueado.setPsMercadoria(pesoAferido);
		reembarqueDoctoServicoFranqueado.setVlCte(parametrosFranqueado.getReembarqueFranqueado().getVlCte());
		reembarqueDoctoServicoFranqueado.setVlTonelada(vlToneladaCalculado);

	}

	@Override
	public void setIdFranquia(Long idFranquia) {
		this.idFranquia = idFranquia;
	}

	@Override
	public void setParametrosFranqueado(CalculoFranqueadoParametros parametrosFranqueado) {
		this.parametrosFranqueado = parametrosFranqueado;
	}

	@Override
	public void setDocumento(DocumentoFranqueadoDTO documento) {
		this.reembarqueFranqueadoDTO = (CalculoReembarqueFranqueadoDTO) documento;
	}

	@Override
	public Serializable getDocumentoFranqueado() {
		return reembarqueDoctoServicoFranqueado;
	}

	protected Long getIdFranquia() {
		return idFranquia;
	}

	protected CalculoReembarqueFranqueadoDTO getReembarqueFranqueadoDTO() {
		return reembarqueFranqueadoDTO;
	}

	protected CalculoFranqueadoParametros getParametrosFranqueado() {
		return parametrosFranqueado;
	}

}
