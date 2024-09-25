package com.mercurio.lms.expedicao.dto;

public enum EVersaoXMLCTE {
	
	VERSAO_300a("3.00a"),
	VERSAO_400("4.00");
	
	private static final long serialVersionUID = 1L;
	
	private final String conteudoParametro;
	
	private EVersaoXMLCTE(final String conteudoParametro){
		this.conteudoParametro = conteudoParametro;
	}
	
	public static EVersaoXMLCTE getByConteudoParametro(final String conteudoParametro){
		for (EVersaoXMLCTE eVersaoXMLCTE : values()) {
			if(eVersaoXMLCTE.conteudoParametro.equals(conteudoParametro)){
				return eVersaoXMLCTE;
			}
		}
		return null;
	}

	public String getConteudoParametro() {
		return this.conteudoParametro;
	}

}
