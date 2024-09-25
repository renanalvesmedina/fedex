package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.municipios.model.RotaIdaVolta;

public class AplicaReajusteRota  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	 /** identifier field */
    private Long idAplicaReajusteRota;

    /** persistent field */
    private Boolean blAplicacao;
    
    /** persistent field */
    private SimulacaoReajusteRota simulacaoReajusteRota;
    
    /** persistent field */
    private RotaIdaVolta rotaIdaVolta;

	public Boolean getBlAplicacao() {
		return blAplicacao;
	}

	public void setBlAplicacao(Boolean blAplicacao) {
		this.blAplicacao = blAplicacao;
	}

	public Long getIdAplicaReajusteRota() {
		return idAplicaReajusteRota;
	}

	public void setIdAplicaReajusteRota(Long idAplicaReajusteRota) {
		this.idAplicaReajusteRota = idAplicaReajusteRota;
	}

	public SimulacaoReajusteRota getSimulacaoReajusteRota() {
		return simulacaoReajusteRota;
	}

	public void setSimulacaoReajusteRota(
			SimulacaoReajusteRota simulacaoReajusteRota) {
		this.simulacaoReajusteRota = simulacaoReajusteRota;
	}   
   
	public String toString() {
		return new ToStringBuilder(this).append("idAplicaReajusteRota",
				getIdAplicaReajusteRota()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AplicaReajusteRota))
			return false;
        AplicaReajusteRota castOther = (AplicaReajusteRota) other;
		return new EqualsBuilder().append(this.getIdAplicaReajusteRota(),
				castOther.getIdAplicaReajusteRota()).isEquals();
	}

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAplicaReajusteRota())
            .toHashCode();
    }

	public RotaIdaVolta getRotaIdaVolta() {
		return rotaIdaVolta;
	}

	public void setRotaIdaVolta(RotaIdaVolta rotaIdaVolta) {
		this.rotaIdaVolta = rotaIdaVolta;
	}

}
