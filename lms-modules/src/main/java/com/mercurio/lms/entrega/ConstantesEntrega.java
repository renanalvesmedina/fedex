package com.mercurio.lms.entrega;

import com.mercurio.adsm.framework.model.DomainValue;

public interface ConstantesEntrega {

	String PRE_MANIFESTO_ENTREGA = "PME";
	String MANIFESTO_ENTREGA = "MAE";
	String MANIFESTO_VIAGEM = "MAV";

	String STATUS_MANIFESTO_CANCELADO = "CA";
	String STATUS_MANIFESTO_EMITIDO = "ME";
	String STATUS_MANIFESTO_FECHADO = "FE";
	String STATUS_PRE_MANIFESTO = "PM";
	String STATUS_CARREGAMENTO_CONCLUIDO = "CC";

	String EVENTO_MANIFESTO_EMITIDO = "EM";
	String EVENTO_MANIFESTO_CANCELADO = "CA";
	
	String TP_MANIFESTO_ENTREGA = "E";
	String TP_MANIFESTO_VIAGEM = "V";
	
	String TP_SITUACAO_AGENDAMENTO_ABERTO = "A";
	String TP_SITUACAO_AGENDAMENTO_FECHADO = "F";

    DomainValue DM_MANIFESTO_ENTREGA = new DomainValue("E");
    DomainValue DM_MANIFESTO_ENTREGA_NORMAL = new DomainValue("EN");
    DomainValue DM_MANIFESTO_ENTREGA_DIRETA = new DomainValue("ED");
    DomainValue DM_MANIFESTO_ENTREGA_PARCEIRA = new DomainValue("EP");
    DomainValue DM_MANIFESTO_CANCELADO = new DomainValue("CA");
    
    String TP_OCORRENCIA_ENTREGUE = "E";
    String TP_OCORRENCIA_ENTREGUE_AEROPORTO = "A";
    
    String STATUS_DOCUMENTO_CANCELADO = "CANC";

}