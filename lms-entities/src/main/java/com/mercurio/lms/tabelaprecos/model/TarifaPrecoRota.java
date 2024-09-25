package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class TarifaPrecoRota implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTarifaPrecoRota;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.TarifaPreco tarifaPreco;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.RotaPreco rotaPreco;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.TabelaPreco tabelaPreco;

    public Long getIdTarifaPrecoRota() {
        return this.idTarifaPrecoRota;
    }

    public void setIdTarifaPrecoRota(Long idTarifaPrecoRota) {
        this.idTarifaPrecoRota = idTarifaPrecoRota;
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

    public com.mercurio.lms.tabelaprecos.model.TabelaPreco getTabelaPreco() {
        return this.tabelaPreco;
    }

	public void setTabelaPreco(
			com.mercurio.lms.tabelaprecos.model.TabelaPreco tabelaPreco) {
        this.tabelaPreco = tabelaPreco;
    }

    @Override
	public String toString() {
		return new ToStringBuilder(this).append("idTarifaPrecoRota",
				getIdTarifaPrecoRota()).toString();
    }

    @Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TarifaPrecoRota))
			return false;
        TarifaPrecoRota castOther = (TarifaPrecoRota) other;
		return new EqualsBuilder().append(this.getIdTarifaPrecoRota(),
				castOther.getIdTarifaPrecoRota()).isEquals();
    }

    @Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdTarifaPrecoRota())
            .toHashCode();
    }
    
    public boolean mesmaRota(RotaPreco rota) {
    	if (this.rotaPreco == null) {
    		return false;
    	}
    	return this.rotaPreco.equals(rota);
    }

}
