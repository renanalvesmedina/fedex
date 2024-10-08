package com.mercurio.lms.rnc.model.service;

import com.mercurio.adsm.framework.model.DomainValue;

public interface ConstantesRnc {
	String TP_STATUS_NAO_CONFORMIDADE_PRE= "PRE";
	String TP_STATUS_NAO_CONFORMIDADE_AGUARDANDO_APROVACAO= "AGP";
	String TP_STATUS_NAO_CONFORMIDADE_RECIBO= "RNC";
	String TP_ANEXO_BO= "B";
	
	DomainValue DM_TP_STATUS_NAO_CONFORMIDADE_PRE = new DomainValue(TP_STATUS_NAO_CONFORMIDADE_PRE);
	DomainValue DM_TP_STATUS_NAO_CONFORMIDADE_AGUARDANDO_APROVACAO = new DomainValue(TP_STATUS_NAO_CONFORMIDADE_AGUARDANDO_APROVACAO);
	DomainValue DM_TP_STATUS_NAO_CONFORMIDADE_RECIBO = new DomainValue(TP_STATUS_NAO_CONFORMIDADE_RECIBO);
	
	String TP_MODAL_CAUSADOR_FILIAL = "F";
	String TP_MODAL_CAUSADOR_AEROPORTO = "A";
}
