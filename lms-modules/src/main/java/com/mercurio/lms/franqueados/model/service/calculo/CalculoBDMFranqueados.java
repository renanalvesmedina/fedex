package com.mercurio.lms.franqueados.model.service.calculo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.LancamentoFranqueado;
import com.mercurio.lms.util.BigDecimalUtils;

public class CalculoBDMFranqueados implements ICalculoFranquiado {

	private Long idFranquia;
	private CalculoFranqueadoParametros parametrosFranqueado;
	private CalculoBDMFranqueadoDTO calculoBDMFranqueadoDTO;
	private LancamentoFranqueado lancamentoFranqueado;

	public CalculoBDMFranqueados() {
		super();
	}

	@Override
	public void executarCalculo() {
		CalculoBDMFranqueadoDTO bdm = calculoBDMFranqueadoDTO;
		if (BigDecimalUtils.hasValue(bdm.getVlParticipacao()) 
				&& BigDecimalUtils.gtZero(bdm.getVlParticipacao())
				&& StringUtils.isNotBlank(bdm.getSgFilial()) 
				&& StringUtils.isNotBlank(bdm.getNrBDM())) {
			lancamentoFranqueado = new LancamentoFranqueado();

			lancamentoFranqueado.setManual(false);
			Franquia franquia = new Franquia();
			franquia.setIdFranquia(idFranquia);
			lancamentoFranqueado.setFranquia(franquia);
			lancamentoFranqueado.setContaContabilFranqueado(parametrosFranqueado.getContaContabilFranqueado());
			lancamentoFranqueado.setDtCompetencia(parametrosFranqueado.getCompetencia().getInicio());
			lancamentoFranqueado.setVlLancamento(bdm.getVlParticipacao());
			lancamentoFranqueado.setTpSituacaoPendencia(new DomainValue(ConstantesFranqueado.TP_APROVADO));
			lancamentoFranqueado.setDsLancamento(gerarDescricao());

		}

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
		this.calculoBDMFranqueadoDTO = (CalculoBDMFranqueadoDTO) documento;
	}

	@Override
	public Serializable getDocumentoFranqueado() {
		return lancamentoFranqueado;
	}

	private String gerarDescricao() {
		StringBuilder obs = new StringBuilder();
		obs.append("BDM: ").append(calculoBDMFranqueadoDTO.getSgFilial()).append(" ")
				.append(calculoBDMFranqueadoDTO.getNrBDM()).append(" DOCUMENTO DE SERVIÇO: ")
				.append(calculoBDMFranqueadoDTO.getTpDocumentoServico().getDescriptionAsString()).append(" ")
				.append(calculoBDMFranqueadoDTO.getSgFilialOrigem()).append(" ")
				.append(calculoBDMFranqueadoDTO.getNrDoctoServico());

		return obs.toString();
	}

}
