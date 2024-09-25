package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class HorarioCorteCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idHorarioCorteCliente;

    /** persistent field */
    private DomainValue tpHorario;

    /** persistent field */
    private TimeOfDay hrInicial;

    /** persistent field */
    private TimeOfDay hrFinal;

    /** nullable persistent field */
    private Short nrHorasAplicadas;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico; 

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipioDestino;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoaDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais paisDestino;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Zona zonaDestino;    
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipioOrigem;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoaOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais paisOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialOrigem;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Zona zonaOrigem;    

    public Long getIdHorarioCorteCliente() {
        return this.idHorarioCorteCliente;
    }

    public void setIdHorarioCorteCliente(Long idHorarioCorteCliente) {
        this.idHorarioCorteCliente = idHorarioCorteCliente;
    }

    public DomainValue getTpHorario() {
        return this.tpHorario;
    }

    public void setTpHorario(DomainValue tpHorario) {
        this.tpHorario = tpHorario;
    }

    public TimeOfDay getHrFinal() {
		return hrFinal;
	}

	public void setHrFinal(TimeOfDay hrFinal) {
		this.hrFinal = hrFinal;
	}

	public TimeOfDay getHrInicial() {
		return hrInicial;
	}

	public void setHrInicial(TimeOfDay hrInicial) {
		this.hrInicial = hrInicial;
	}

	public Short getNrHorasAplicadas() {
        return this.nrHorasAplicadas;
    }

    public void setNrHorasAplicadas(Short nrHorasAplicadas) {
        this.nrHorasAplicadas = nrHorasAplicadas;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

	public com.mercurio.lms.configuracoes.model.Servico getServico() {
		return servico;
    }

	public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
		this.servico = servico;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaDestino() {
		return unidadeFederativaDestino;
    }

	public void setUnidadeFederativaDestino(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaDestino) {
		this.unidadeFederativaDestino = unidadeFederativaDestino;
    }

	public com.mercurio.lms.municipios.model.Municipio getMunicipioDestino() {
		return municipioDestino;
    }

	public void setMunicipioDestino(
			com.mercurio.lms.municipios.model.Municipio municipioDestino) {
		this.municipioDestino = municipioDestino;
    }

	public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoPessoaDestino() {
		return enderecoPessoaDestino;
    }

	public void setEnderecoPessoaDestino(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoaDestino) {
		this.enderecoPessoaDestino = enderecoPessoaDestino;
    }

	public com.mercurio.lms.municipios.model.Pais getPaisDestino() {
		return paisDestino;
	}

	public void setPaisDestino(
			com.mercurio.lms.municipios.model.Pais paisDestino) {
		this.paisDestino = paisDestino;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	public com.mercurio.lms.municipios.model.Zona getZonaDestino() {
		return zonaDestino;
	}

	public void setZonaDestino(
			com.mercurio.lms.municipios.model.Zona zonaDestino) {
		this.zonaDestino = zonaDestino;
	}

	public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaOrigem() {
		return unidadeFederativaOrigem;
	}

	public void setUnidadeFederativaOrigem(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaOrigem) {
		this.unidadeFederativaOrigem = unidadeFederativaOrigem;
	}

	public com.mercurio.lms.municipios.model.Municipio getMunicipioOrigem() {
		return municipioOrigem;
	}

	public void setMunicipioOrigem(
			com.mercurio.lms.municipios.model.Municipio municipioOrigem) {
		this.municipioOrigem = municipioOrigem;
	}

	public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoPessoaOrigem() {
		return enderecoPessoaOrigem;
	}

	public void setEnderecoPessoaOrigem(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoaOrigem) {
		this.enderecoPessoaOrigem = enderecoPessoaOrigem;
	}

	public com.mercurio.lms.municipios.model.Pais getPaisOrigem() {
		return paisOrigem;
	}

	public void setPaisOrigem(com.mercurio.lms.municipios.model.Pais paisOrigem) {
		this.paisOrigem = paisOrigem;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public com.mercurio.lms.municipios.model.Zona getZonaOrigem() {
		return zonaOrigem;
	}

	public void setZonaOrigem(com.mercurio.lms.municipios.model.Zona zonaOrigem) {
		this.zonaOrigem = zonaOrigem;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idHorarioCorteCliente",
				getIdHorarioCorteCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HorarioCorteCliente))
			return false;
        HorarioCorteCliente castOther = (HorarioCorteCliente) other;
		return new EqualsBuilder().append(this.getIdHorarioCorteCliente(),
				castOther.getIdHorarioCorteCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdHorarioCorteCliente())
            .toHashCode();
    }

	}
