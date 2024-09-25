package com.mercurio.lms.coleta.model.util;

public enum TpOperacaoPedidoColeta {
	INCLUSAO(0),
	CANCELAMENTO(1),
	ALTERACAO(4);
	
	private Integer value;
	
	private TpOperacaoPedidoColeta(Integer value) {
        this.value = value;
    }
	
    public Integer getValue(){
        return this.value;
    }

	public static TpOperacaoPedidoColeta getOperacao(Short operacao) {
		switch(operacao) {
		case 0:
			return INCLUSAO;
		case 1:
			return CANCELAMENTO;
		case 4:
			return ALTERACAO;
		default:
			return null;
		}
	}
}
