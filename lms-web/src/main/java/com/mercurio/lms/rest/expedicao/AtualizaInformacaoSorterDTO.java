package com.mercurio.lms.rest.expedicao;

import java.util.List;

public class AtualizaInformacaoSorterDTO {
	private String loginUsuarioLogado;
	private List<DadosVolumeDTO> dadosVolume;

	public String getLoginUsuarioLogado() {
		return loginUsuarioLogado;
	}

	public void setLoginUsuarioLogado(String loginUsuarioLogado) {
		this.loginUsuarioLogado = loginUsuarioLogado;
	}

	public List<DadosVolumeDTO> getDadosVolume() {
		return dadosVolume;
	}

	public void setDadosVolume(List<DadosVolumeDTO> dadosVolume) {
		this.dadosVolume = dadosVolume;
	}

}
