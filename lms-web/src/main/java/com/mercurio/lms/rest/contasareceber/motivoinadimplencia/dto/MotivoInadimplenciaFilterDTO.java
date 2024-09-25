package com.mercurio.lms.rest.contasareceber.motivoinadimplencia.dto;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO; 
 
public class MotivoInadimplenciaFilterDTO extends BaseFilterDTO { 
	
	private static final long serialVersionUID = 1L; 
	private DomainValue tpSituacao;
	
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

} 
