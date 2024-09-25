package com.mercurio.lms.prestcontasciaaerea.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.util.Vigencia;

public class FaturamentoCiaAerea implements Serializable, Vigencia{
	private static final long serialVersionUID = 1L;
	private Long idFaturamentoCiaAerea;
	private CiaFilialMercurio ciaFilialMercurio;
	private BigDecimal pcComissao;
	private DomainValue tpPeriodicidade;
	private Byte ddFaturamento;
	private Byte nrPrazoPagamento;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	
	public void setIdFaturamentoCiaAerea(Long idFaturamentoCiaAerea) {
		this.idFaturamentoCiaAerea = idFaturamentoCiaAerea;
	}

	public Long getIdFaturamentoCiaAerea() {
		return idFaturamentoCiaAerea;
	}

	public void setCiaFilialMercurio(CiaFilialMercurio ciaFilialMercurio) {
		this.ciaFilialMercurio = ciaFilialMercurio;
	}

	public CiaFilialMercurio getCiaFilialMercurio() {
		return ciaFilialMercurio;
	}

	public void setPcComissao(BigDecimal pcComissao) {
		this.pcComissao = pcComissao;
	}

	public BigDecimal getPcComissao() {
		return pcComissao;
	}

	public void setTpPeriodicidade(DomainValue tpPeriodicidade) {
		this.tpPeriodicidade = tpPeriodicidade;
	}

	public DomainValue getTpPeriodicidade() {
		return tpPeriodicidade;
	}	

	public void setDdFaturamento(Byte ddFaturamento) {
		this.ddFaturamento = ddFaturamento;
	}

	public Byte getDdFaturamento() {
		return ddFaturamento;
	}

	public Byte getNrPrazoPagamento() {
		return nrPrazoPagamento;
	}

	public void setNrPrazoPagamento(Byte nrPrazoPagamento) {
		this.nrPrazoPagamento = nrPrazoPagamento;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	@Override
    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FaturamentoCiaAerea))
			return false;
        FaturamentoCiaAerea castOther = (FaturamentoCiaAerea) other;
		return new EqualsBuilder().append(this.getIdFaturamentoCiaAerea(),
				castOther.getIdFaturamentoCiaAerea()).isEquals();
    }
    
    @Override
 	public int hashCode() {
 		final int prime = 31;
 		int result = 1;
 		result = prime * result + ((idFaturamentoCiaAerea == null) ? 0 : idFaturamentoCiaAerea.hashCode());
 		return result;
 	}
    
}