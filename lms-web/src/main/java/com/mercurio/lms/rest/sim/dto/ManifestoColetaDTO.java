package com.mercurio.lms.rest.sim.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import org.joda.time.YearMonthDay;
import org.joda.time.DateTime;

public class ManifestoColetaDTO {
	
	private String sgFilialManifesto;
	private Integer nrManifesto;
	private String sgFilialColeta;
	private Long nrColeta;
	private DomainValue tpModoPedidoColeta;
	private DomainValue tpPedidoColeta;
	private YearMonthDay dtPrevisaoColeta;
	private DateTime dhEmissao;
	private DateTime dhColetaDisponivel;
	public String getSgFilialManifesto() {
		return sgFilialManifesto;
	}
	public void setSgFilialManifesto(String sgFilialManifesto) {
		this.sgFilialManifesto = sgFilialManifesto;
	}
	public Integer getNrManifesto() {
		return nrManifesto;
	}
	public void setNrManifesto(Integer nrManifesto) {
		this.nrManifesto = nrManifesto;
	}
	public String getSgFilialColeta() {
		return sgFilialColeta;
	}
	public void setSgFilialColeta(String sgFilialColeta) {
		this.sgFilialColeta = sgFilialColeta;
	}
	public Long getNrColeta() {
		return nrColeta;
	}
	public void setNrColeta(Long nrColeta) {
		this.nrColeta = nrColeta;
	}
	public DomainValue getTpModoPedidoColeta() {
		return tpModoPedidoColeta;
	}
	public void setTpModoPedidoColeta(DomainValue tpModoPedidoColeta) {
		this.tpModoPedidoColeta = tpModoPedidoColeta;
	}
	public DomainValue getTpPedidoColeta() {
		return tpPedidoColeta;
	}
	public void setTpPedidoColeta(DomainValue tpPedidoColeta) {
		this.tpPedidoColeta = tpPedidoColeta;
	}
	public YearMonthDay getDtPrevisaoColeta() {
		return dtPrevisaoColeta;
	}
	public void setDtPrevisaoColeta(YearMonthDay dtPrevisaoColeta) {
		this.dtPrevisaoColeta = dtPrevisaoColeta;
	}
	public DateTime getDhEmissao() {
		return dhEmissao;
	}
	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}
	public DateTime getDhColetaDisponivel() {
		return dhColetaDisponivel;
	}
	public void setDhColetaDisponivel(DateTime dhColetaDisponivel) {
		this.dhColetaDisponivel = dhColetaDisponivel;
	}
	
}
