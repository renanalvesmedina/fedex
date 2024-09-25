package com.mercurio.lms.util.enums.edw;

public enum ComissionamentoEDWServiceEnum {

	FIND_DEMONSTRATIVO_PAGAMENTO("comissionamento/findDemonstrativoPagamento"),
	FIND_TOTAL_DEMONSTRATIVO_PAGAMENTO("comissionamento/findTotalDemonstrativoPagamento"),
	FIND_DEMONSTRATIVO_FECHAMENTO("comissionamento/findDemonstrativoFechamento"),
	FIND_TOTAL_DEMONSTRATIVO_FECHAMENTO("comissionamento/findTotalDemonstrativoFechamento");
	
	private String path;

	private ComissionamentoEDWServiceEnum(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
}
