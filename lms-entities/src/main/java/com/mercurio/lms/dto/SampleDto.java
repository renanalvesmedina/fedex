package com.mercurio.lms.dto;

import java.io.Serializable;

public class SampleDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dsDescricao;
	private byte[] dcArquivo;

	@Override
	public String toString() {
		return "[SampleDto] Descrição: " + getDsDescricao();
	}
	
	public String getDsDescricao() {
		return dsDescricao;
	}

	public void setDsDescricao(String dsDescricao) {
		this.dsDescricao = dsDescricao;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

}
