package com.mercurio.lms.rest.contasareceber.lotecobranca.dto;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
 
public class HistoricoMensagemDetalheDTO extends BaseDTO { 
	
	private static final long serialVersionUID = 1L;
	private DomainValue tpEvento;
	private String descricao;
	private String dhEvento;
	private String dcMensagem;
	
	public DomainValue getTpEvento() {
		return tpEvento;
	}
	public void setTpEvento(DomainValue tpEvento) {
		this.tpEvento = tpEvento;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getDhEvento() {
		return dhEvento;
	}
	public void setDhEvento(String dhEvento) {
		this.dhEvento = dhEvento;
	}
	public String getDcMensagem() {
		return dcMensagem;
	}
	public void setDcMensagem(String dcMensagem) {
		this.dcMensagem = dcMensagem;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
} 
