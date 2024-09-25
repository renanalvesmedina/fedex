package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.carregamento.model.ControleCarga;

public class TabelaColetaEntregaCC implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long idTabelaColetaEntregaCC;
	private ControleCarga controleCarga;
	private TabelaColetaEntrega tabelaColetaEntrega;
	
	public Long getIdTabelaColetaEntregaCC() {
		return idTabelaColetaEntregaCC;
	}

	public void setIdTabelaColetaEntregaCC(Long idTabelaColetaEntregaCC) {
		this.idTabelaColetaEntregaCC = idTabelaColetaEntregaCC;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public TabelaColetaEntrega getTabelaColetaEntrega() {
		return tabelaColetaEntrega;
	}

	public void setTabelaColetaEntrega(TabelaColetaEntrega tabelaColetaEntrega) {
		this.tabelaColetaEntrega = tabelaColetaEntrega;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idTabelaColetaEntregaCC()",
				getIdTabelaColetaEntregaCC()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TabelaColetaEntregaCC))
			return false;
        TabelaColetaEntregaCC castOther = (TabelaColetaEntregaCC) other;
		return new EqualsBuilder().append(this.getIdTabelaColetaEntregaCC(),
				castOther.getIdTabelaColetaEntregaCC()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTabelaColetaEntregaCC())
            .toHashCode();
    }
}
