package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class McdMunicipioFilial implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMcdMunicipioFilial;

    /** persistent field */
    private Long nrPpe;

    /** nullable persistent field */
    private Byte qtPedagio;

    /** persistent field */
    private Boolean blDomingoOrigem;
    
    /** persistent field */
    private Boolean blSegundaOrigem;
    
    /** persistent field */
    private Boolean blTercaOrigem;
    
    /** persistent field */
    private Boolean blQuartaOrigem;
    
    /** persistent field */
    private Boolean blQuintaOrigem;
    
    /** persistent field */
    private Boolean blSextaOrigem;
    
    /** persistent field */
    private Boolean blSabadoOrigem;
    
    /** persistent field */
    private Boolean blDomingoDestino;
    
    /** persistent field */
    private Boolean blSegundaDestino;
    
    /** persistent field */
    private Boolean blTercaDestino;
    
    /** persistent field */
    private Boolean blQuartaDestino;
    
    /** persistent field */
    private Boolean blQuintaDestino;
    
    /** persistent field */
    private Boolean blSextaDestino;
    
    /** persistent field */
    private Boolean blSabadoDestino;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Mcd mcd;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.TarifaPreco tarifaPreco;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MunicipioFilial municipioFilialByIdMunicipioFilialOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MunicipioFilial municipioFilialByIdMunicipioFilialDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.FluxoFilial fluxoFilial;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioDestino;

    public Long getIdMcdMunicipioFilial() {
        return this.idMcdMunicipioFilial;
    }

    public void setIdMcdMunicipioFilial(Long idMcdMunicipioFilial) {
        this.idMcdMunicipioFilial = idMcdMunicipioFilial;
    }

    public Long getNrPpe() {
        return this.nrPpe;
    }

    public void setNrPpe(Long nrPpe) {
        this.nrPpe = nrPpe;
    }

    public Byte getQtPedagio() {
        return this.qtPedagio;
    }

    public void setQtPedagio(Byte qtPedagio) {
        this.qtPedagio = qtPedagio;
    }

    public com.mercurio.lms.municipios.model.Mcd getMcd() {
        return this.mcd;
    }

    public void setMcd(com.mercurio.lms.municipios.model.Mcd mcd) {
        this.mcd = mcd;
    }

    public com.mercurio.lms.tabelaprecos.model.TarifaPreco getTarifaPreco() {
        return this.tarifaPreco;
    }

	public void setTarifaPreco(
			com.mercurio.lms.tabelaprecos.model.TarifaPreco tarifaPreco) {
        this.tarifaPreco = tarifaPreco;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public com.mercurio.lms.municipios.model.MunicipioFilial getMunicipioFilialByIdMunicipioFilialOrigem() {
        return this.municipioFilialByIdMunicipioFilialOrigem;
    }

	public void setMunicipioFilialByIdMunicipioFilialOrigem(
			com.mercurio.lms.municipios.model.MunicipioFilial municipioFilialByIdMunicipioFilialOrigem) {
        this.municipioFilialByIdMunicipioFilialOrigem = municipioFilialByIdMunicipioFilialOrigem;
    }

    public com.mercurio.lms.municipios.model.MunicipioFilial getMunicipioFilialByIdMunicipioFilialDestino() {
        return this.municipioFilialByIdMunicipioFilialDestino;
    }

	public void setMunicipioFilialByIdMunicipioFilialDestino(
			com.mercurio.lms.municipios.model.MunicipioFilial municipioFilialByIdMunicipioFilialDestino) {
        this.municipioFilialByIdMunicipioFilialDestino = municipioFilialByIdMunicipioFilialDestino;
    }

    public com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioOrigem() {
        return this.tipoLocalizacaoMunicipioOrigem;
    }

	public void setTipoLocalizacaoMunicipioOrigem(
			com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioOrigem) {
        this.tipoLocalizacaoMunicipioOrigem = tipoLocalizacaoMunicipioOrigem;
    }

    public com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioDestino() {
        return this.tipoLocalizacaoMunicipioDestino;
    }

	public void setTipoLocalizacaoMunicipioDestino(
			com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioDestino) {
        this.tipoLocalizacaoMunicipioDestino = tipoLocalizacaoMunicipioDestino;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMcdMunicipioFilial",
				getIdMcdMunicipioFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof McdMunicipioFilial))
			return false;
        McdMunicipioFilial castOther = (McdMunicipioFilial) other;
		return new EqualsBuilder().append(this.getIdMcdMunicipioFilial(),
				castOther.getIdMcdMunicipioFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMcdMunicipioFilial())
            .toHashCode();
    }

	/**
	 * @return Returns the fluxoFilial.
	 */
	public com.mercurio.lms.municipios.model.FluxoFilial getFluxoFilial() {
		return fluxoFilial;
	}

	/**
	 * @param fluxoFilial
	 *            The fluxoFilial to set.
	 */
	public void setFluxoFilial(
			com.mercurio.lms.municipios.model.FluxoFilial fluxoFilial) {
		this.fluxoFilial = fluxoFilial;
	}

	/**
	 * @return Returns the blDomingoDestino.
	 */
	public Boolean getBlDomingoDestino() {
		return blDomingoDestino;
	}

	/**
	 * @param blDomingoDestino
	 *            The blDomingoDestino to set.
	 */
	public void setBlDomingoDestino(Boolean blDomingoDestino) {
		this.blDomingoDestino = blDomingoDestino;
	}

	/**
	 * @return Returns the blDomingoOrigem.
	 */
	public Boolean getBlDomingoOrigem() {
		return blDomingoOrigem;
	}

	/**
	 * @param blDomingoOrigem
	 *            The blDomingoOrigem to set.
	 */
	public void setBlDomingoOrigem(Boolean blDomingoOrigem) {
		this.blDomingoOrigem = blDomingoOrigem;
	}

	/**
	 * @return Returns the blQuartaDestino.
	 */
	public Boolean getBlQuartaDestino() {
		return blQuartaDestino;
	}

	/**
	 * @param blQuartaDestino
	 *            The blQuartaDestino to set.
	 */
	public void setBlQuartaDestino(Boolean blQuartaDestino) {
		this.blQuartaDestino = blQuartaDestino;
	}

	/**
	 * @return Returns the blQuartaOrigem.
	 */
	public Boolean getBlQuartaOrigem() {
		return blQuartaOrigem;
	}

	/**
	 * @param blQuartaOrigem
	 *            The blQuartaOrigem to set.
	 */
	public void setBlQuartaOrigem(Boolean blQuartaOrigem) {
		this.blQuartaOrigem = blQuartaOrigem;
	}

	/**
	 * @return Returns the blQuintaDestino.
	 */
	public Boolean getBlQuintaDestino() {
		return blQuintaDestino;
	}

	/**
	 * @param blQuintaDestino
	 *            The blQuintaDestino to set.
	 */
	public void setBlQuintaDestino(Boolean blQuintaDestino) {
		this.blQuintaDestino = blQuintaDestino;
	}

	/**
	 * @return Returns the blQuintaOrigem.
	 */
	public Boolean getBlQuintaOrigem() {
		return blQuintaOrigem;
	}

	/**
	 * @param blQuintaOrigem
	 *            The blQuintaOrigem to set.
	 */
	public void setBlQuintaOrigem(Boolean blQuintaOrigem) {
		this.blQuintaOrigem = blQuintaOrigem;
	}

	/**
	 * @return Returns the blSabadoDestino.
	 */
	public Boolean getBlSabadoDestino() {
		return blSabadoDestino;
	}

	/**
	 * @param blSabadoDestino
	 *            The blSabadoDestino to set.
	 */
	public void setBlSabadoDestino(Boolean blSabadoDestino) {
		this.blSabadoDestino = blSabadoDestino;
	}

	/**
	 * @return Returns the blSabadoOrigem.
	 */
	public Boolean getBlSabadoOrigem() {
		return blSabadoOrigem;
	}

	/**
	 * @param blSabadoOrigem
	 *            The blSabadoOrigem to set.
	 */
	public void setBlSabadoOrigem(Boolean blSabadoOrigem) {
		this.blSabadoOrigem = blSabadoOrigem;
	}

	/**
	 * @return Returns the blSegundaDestino.
	 */
	public Boolean getBlSegundaDestino() {
		return blSegundaDestino;
	}

	/**
	 * @param blSegundaDestino
	 *            The blSegundaDestino to set.
	 */
	public void setBlSegundaDestino(Boolean blSegundaDestino) {
		this.blSegundaDestino = blSegundaDestino;
	}

	/**
	 * @return Returns the blSegundaOrigem.
	 */
	public Boolean getBlSegundaOrigem() {
		return blSegundaOrigem;
	}

	/**
	 * @param blSegundaOrigem
	 *            The blSegundaOrigem to set.
	 */
	public void setBlSegundaOrigem(Boolean blSegundaOrigem) {
		this.blSegundaOrigem = blSegundaOrigem;
	}

	/**
	 * @return Returns the blSextaDestino.
	 */
	public Boolean getBlSextaDestino() {
		return blSextaDestino;
	}

	/**
	 * @param blSextaDestino
	 *            The blSextaDestino to set.
	 */
	public void setBlSextaDestino(Boolean blSextaDestino) {
		this.blSextaDestino = blSextaDestino;
	}

	/**
	 * @return Returns the blSextaOrigem.
	 */
	public Boolean getBlSextaOrigem() {
		return blSextaOrigem;
	}

	/**
	 * @param blSextaOrigem
	 *            The blSextaOrigem to set.
	 */
	public void setBlSextaOrigem(Boolean blSextaOrigem) {
		this.blSextaOrigem = blSextaOrigem;
	}

	/**
	 * @return Returns the blTercaDestino.
	 */
	public Boolean getBlTercaDestino() {
		return blTercaDestino;
	}

	/**
	 * @param blTercaDestino
	 *            The blTercaDestino to set.
	 */
	public void setBlTercaDestino(Boolean blTercaDestino) {
		this.blTercaDestino = blTercaDestino;
	}

	/**
	 * @return Returns the blTercaOrigem.
	 */
	public Boolean getBlTercaOrigem() {
		return blTercaOrigem;
	}

	/**
	 * @param blTercaOrigem
	 *            The blTercaOrigem to set.
	 */
	public void setBlTercaOrigem(Boolean blTercaOrigem) {
		this.blTercaOrigem = blTercaOrigem;
	}

}
