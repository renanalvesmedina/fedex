package com.mercurio.lms.vendas.util;

public interface ConstantesEventosPCE {

	//Códigos do PCE para registro de baixas
	Long CD_OCORRENCIA_TIPO_REENTREGA = Long.valueOf(29);
	Long CD_OCORRENCIA_TIPO_ENTREGA = Long.valueOf(27);
	Long CD_OCORRENCIA_TIPO_NAO_ENTREGUE = Long.valueOf(28);
	Long CD_OCORRENCIA = Long.valueOf(30);
	
	//Códigos do PCE para o agendamento
	Long CD_PROCESSO_AGENDAMENTO = Long.valueOf(6);
	Long CD_EVENTO_AGENDAMENTO = Long.valueOf(14);
	Long CD_OCORRENCIA_AGENDAMENTO = Long.valueOf(20);
	
	//Códigos do PCE para manifesto de entrega
	Long CD_PROCESSO_ENTREGA = Long.valueOf(8);
	Long CD_EVENTO_MANIFESTO_ENTREGA = Long.valueOf(18);
	Long CD_OCORRENCIA_NA_ENTREGA = Long.valueOf(26);
}
