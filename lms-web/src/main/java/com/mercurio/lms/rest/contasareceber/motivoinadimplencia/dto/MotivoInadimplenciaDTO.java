package com.mercurio.lms.rest.contasareceber.motivoinadimplencia.dto;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO; 
import com.mercurio.lms.contasreceber.model.ModeloMensagem;
import com.mercurio.lms.contasreceber.model.MotivoInadimplencia;
 
public class MotivoInadimplenciaDTO extends BaseDTO { 

	private static final long serialVersionUID = 1L;
	private DomainValue tpSituacao;
	private String descricao;
	
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public MotivoInadimplencia build(MotivoInadimplencia current) {
		MotivoInadimplencia motivoInadimplencia = current;
		motivoInadimplencia.setDescricao(getDescricao());
		motivoInadimplencia.setTpSituacao(getTpSituacao());
		return motivoInadimplencia;
	}
	
} 
