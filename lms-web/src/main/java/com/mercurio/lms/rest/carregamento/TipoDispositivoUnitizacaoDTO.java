package com.mercurio.lms.rest.carregamento;

import com.mercurio.adsm.rest.BaseDTO;

public class TipoDispositivoUnitizacaoDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;

	private Long idTipoDispositivoUnitizacao;
	
	private Long tpNrIdentificacao;
	
	private String dsTipoDispositivoUnitizacao;

	public Long getIdTipoDispositivoUnitizacao() {
		return idTipoDispositivoUnitizacao;
	}

	public void setIdTipoDispositivoUnitizacao(Long idTipoDispositivoUnitizacao) {
		this.idTipoDispositivoUnitizacao = idTipoDispositivoUnitizacao;
	}

	public Long getTpNrIdentificacao() {
		return tpNrIdentificacao;
	}

	public void setTpNrIdentificacao(Long tpNrIdentificacao) {
		this.tpNrIdentificacao = tpNrIdentificacao;
	}

	public String getDsTipoDispositivoUnitizacao() {
		return dsTipoDispositivoUnitizacao;
	}

	public void setDsTipoDispositivoUnitizacao(String dsTipoDispositivoUnitizacao) {
		this.dsTipoDispositivoUnitizacao = dsTipoDispositivoUnitizacao;
	}
	
	
}
