package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.TimeOfDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class RotasExpressas implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sgUnidOrigem;
	private String sgUnidDestino;
	private Integer nrCodigo;
	private Integer status;
	private Integer nrRotaLms;
	private String tpAgrupador;
	private BigDecimal vlFaixa1;
	private BigDecimal vlFaixa2;
	private BigDecimal vlFaixa3;
	private TimeOfDay hrSaida;
	private String nrHorasPrev;

	public Integer getNrCodigo() {
		return nrCodigo;
	}

	public void setNrCodigo(Integer nrCodigo) {
		this.nrCodigo = nrCodigo;
	}
	
	public String getSgUnidDestino() {
		return sgUnidDestino;
	}
	
	public void setSgUnidDestino(String sgUnidDestino) {
		this.sgUnidDestino = sgUnidDestino;
	}
	
	public String getSgUnidOrigem() {
		return sgUnidOrigem;
	}
	
	public void setSgUnidOrigem(String sgUnidOrigem) {
		this.sgUnidOrigem = sgUnidOrigem;
	}

	public Integer getNrRotaLms() {
		return nrRotaLms;
	}

	public void setNrRotaLms(Integer nrRotaLms) {
		this.nrRotaLms = nrRotaLms;
	}
	
	public TimeOfDay getHrSaida() {
		return hrSaida;
	}

	public void setHrSaida(TimeOfDay hrSaida) {
		this.hrSaida = hrSaida;
	}

	public String getTpAgrupador() {
		return tpAgrupador;
	}

	public void setTpAgrupador(String tpAgrupador) {
		this.tpAgrupador = tpAgrupador;
	}

	public BigDecimal getVlFaixa1() {
		return vlFaixa1;
	}

	public void setVlFaixa1(BigDecimal vlFaixa1) {
		this.vlFaixa1 = vlFaixa1;
	}

	public BigDecimal getVlFaixa2() {
		return vlFaixa2;
	}

	public void setVlFaixa2(BigDecimal vlFaixa2) {
		this.vlFaixa2 = vlFaixa2;
	}

	public BigDecimal getVlFaixa3() {
		return vlFaixa3;
	}

	public void setVlFaixa3(BigDecimal vlFaixa3) {
		this.vlFaixa3 = vlFaixa3;
	}

	public String getNrHorasPrev() {
		return nrHorasPrev;
	}

	public void setNrHorasPrev(String nrHorasPrev) {
		this.nrHorasPrev = nrHorasPrev;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RotasExpressas == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		RotasExpressas rhs = (RotasExpressas) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj))
					.append(this.sgUnidOrigem, rhs.sgUnidOrigem)
					.append(this.sgUnidDestino, rhs.sgUnidDestino)
				.append(this.nrCodigo, rhs.nrCodigo).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(sgUnidOrigem)
				.append(sgUnidDestino).append(nrCodigo).toHashCode();
	}
}