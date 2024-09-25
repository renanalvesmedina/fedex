package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class PrecoFrete implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPrecoFrete;

    /** persistent field */
    private BigDecimal vlPrecoFrete;

    /** nullable persistent field */
    private com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela tabelaPrecoParcela;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.TarifaPreco tarifaPreco;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.RotaPreco rotaPreco;

    private BigDecimal pesoMinimo;
    
    public PrecoFrete() {}
    
    public PrecoFrete(Long id) {
    	this.idPrecoFrete = id;
    }

    public Long getIdPrecoFrete() {
        return this.idPrecoFrete;
    }

    public void setIdPrecoFrete(Long idPrecoFrete) {
        this.idPrecoFrete = idPrecoFrete;
    }

    public BigDecimal getVlPrecoFrete() {
        return this.vlPrecoFrete;
    }

    public void setVlPrecoFrete(BigDecimal vlPrecoFrete) {
        this.vlPrecoFrete = vlPrecoFrete;
    }
    
    public com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela getTabelaPrecoParcela() {
        return this.tabelaPrecoParcela;
    }

	public void setTabelaPrecoParcela(
			com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela tabelaPrecoParcela) {
        this.tabelaPrecoParcela = tabelaPrecoParcela;
    }

    public com.mercurio.lms.tabelaprecos.model.TarifaPreco getTarifaPreco() {
        return this.tarifaPreco;
    }

	public void setTarifaPreco(
			com.mercurio.lms.tabelaprecos.model.TarifaPreco tarifaPreco) {
        this.tarifaPreco = tarifaPreco;
    }

    public com.mercurio.lms.tabelaprecos.model.RotaPreco getRotaPreco() {
        return this.rotaPreco;
    }

	public void setRotaPreco(
			com.mercurio.lms.tabelaprecos.model.RotaPreco rotaPreco) {
        this.rotaPreco = rotaPreco;
    }

    public BigDecimal getPesoMinimo() {
        return pesoMinimo;
    }
    
    public void setPesoMinimo(BigDecimal pesoMinimo) {
        this.pesoMinimo = pesoMinimo;
    }

    @Override
	public String toString() {
		return new ToStringBuilder(this).append("idPrecoFrete",
				getIdPrecoFrete()).toString();
    }

    @Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PrecoFrete))
			return false;
        PrecoFrete castOther = (PrecoFrete) other;
		return new EqualsBuilder().append(this.getIdPrecoFrete(),
				castOther.getIdPrecoFrete()).isEquals();
    }

    @Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdPrecoFrete()).toHashCode();
    }

	public Long getIdParcelaPreco() {
		if (tabelaPrecoParcela == null) {
			return null;
		}
		return tabelaPrecoParcela.getIdParcelaPreco();
	}


	public String getNmParcelaPreco() {
		if (tabelaPrecoParcela == null) {
			return null;
		}
		return tabelaPrecoParcela.getNmarcelaPreco();
	}
	
}
