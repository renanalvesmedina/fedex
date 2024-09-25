package com.mercurio.lms.expedicao.dto;

import java.util.List;

public class XmlAwbCiaAereaDto {

	protected String nrChaveAwb;
	
	protected List<byte[]> listaAnexos;

	public String getNrChaveAwb() {
		return nrChaveAwb;
	}

	public void setNrChaveAwb(String nrChaveAwb) {
		this.nrChaveAwb = nrChaveAwb;
	}

	public List<byte[]> getListaAnexos() {
		return listaAnexos;
	}

	public void setListaAnexos(List<byte[]> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}
	
}
