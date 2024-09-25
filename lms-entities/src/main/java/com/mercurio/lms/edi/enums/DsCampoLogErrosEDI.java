package com.mercurio.lms.edi.enums;

public enum DsCampoLogErrosEDI {
	IE_DEST("ieDest"),
	IE_TOMADOR("ieTomador"),
	IE_CONSIG("ieConsig"),
	IE_REDESP("ieRedesp"),
	CEP_ENDER_DEST("cepEnderDest"),
	CEP_ENDER_CONSIG("cepEnderConsig"),
	QTDE_VOLUMES("qtdeVolumes"),
	PESO_REAL("pesoReal"),
	PESO_CUBADO("pesoCubado"),
	METRAGEM_CUBICA_M3("metragemCubica"),
	ETIQUETAS("etiquetas");

	DsCampoLogErrosEDI(String dsCampoEntidade) {
		this.dsCampoEntidade = dsCampoEntidade;
	}
	
	private String dsCampoEntidade;

	public String getDsCampoEntidade() {
		return dsCampoEntidade;
	}

	public void setDsCampoEntidade(String dsCampoEntidade) {
		this.dsCampoEntidade = dsCampoEntidade;
	}
}
