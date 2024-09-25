package com.mercurio.lms.sgr.model;

import java.util.ArrayList;

import org.apache.commons.lang.BooleanUtils;

import com.mercurio.lms.configuracoes.model.MoedaBuilder;
import com.mercurio.lms.municipios.model.PaisBuilder;
import com.mercurio.lms.sgr.dto.DoctoServicoDTO;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;

public class PlanoGerenciamentoRiscoDTOBuilder {

	public static PlanoGerenciamentoRiscoDTOBuilder newPlanoGerenciamentoRiscoDTO() {
		return new PlanoGerenciamentoRiscoDTOBuilder();
	}

	private PlanoGerenciamentoRiscoDTO plano;

	private PlanoGerenciamentoRiscoDTOBuilder() {
		plano = new PlanoGerenciamentoRiscoDTO();
		plano.setIdMoedaDestino(MoedaBuilder.BRL().getIdMoeda());
		plano.setIdPaisDestino(PaisBuilder.BRASIL().getIdPais());
	}

	public PlanoGerenciamentoRiscoDTOBuilder controleCarga(long idControleCarga) {
		plano.setIdControleCarga(idControleCarga);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder moeda(long idMoedaDestino) {
		plano.setIdMoedaDestino(idMoedaDestino);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder pais(long idPaisDestino) {
		plano.setIdPaisDestino(idPaisDestino);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder verificacaoGeral() {
		plano.setBlVerificacaoGeral(true);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder realizaBloqueios() {
		plano.setBlRealizaBloqueios(true);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder permitePGRDuplicado() {
		plano.setBlPermitePGRDuplicado(true);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder validaMaximoCarregamento() {
		plano.setBlValidaMaximoCarregamento(true);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder validaCargaExclusivaCliente() {
		plano.setBlValidaCargaExclusivaCliente(true);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder validaLiberacaoCemop() {
		plano.setBlRequerLiberacaoCemop(true);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder validaNaturezaImpedida() {
		plano.setBlValidaNaturezaImpedida(true);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder documento(DoctoServicoDTO documento) {
		if (plano.getDocumentos() == null) {
			plano.setDocumentos(new ArrayList<DoctoServicoDTO>());
		}
		plano.getDocumentos().add(documento);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder regraCliente(EnquadramentoRegra regra) {
		if (BooleanUtils.isTrue(regra.getBlRegraGeral())) {
			throw new IllegalStateException("Regra de enquadramento geral n�o pode ser utilizada como regra de cliente.");
		}
		if (plano.getRegrasCliente() == null) {
			plano.setRegrasCliente(new ArrayList<EnquadramentoRegra>());
		}
		plano.getRegrasCliente().add(regra);
		return this;
	}

	public PlanoGerenciamentoRiscoDTOBuilder regraGeral(EnquadramentoRegra regra) {
		if (!BooleanUtils.isTrue(regra.getBlRegraGeral())) {
			throw new IllegalStateException("Regra de enquadramento de cliente n�o pode ser utilizada como regra geral.");
		}
		if (plano.getRegrasGeral() == null) {
			plano.setRegrasGeral(new ArrayList<EnquadramentoRegra>());
		}
		plano.getRegrasGeral().add(regra);
		return this;
	}

	public PlanoGerenciamentoRiscoDTO build() {
		return plano;
	}

}
