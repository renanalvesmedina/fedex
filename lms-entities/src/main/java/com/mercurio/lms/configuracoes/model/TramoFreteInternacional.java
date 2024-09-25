package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.vendas.model.Cliente;

/** @author LMS Custom Hibernate CodeGenerator */
public class TramoFreteInternacional implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTramoFreteInternacional;

    /** persistent field */
    private Byte nrTramoFreteInternacional;

    /** persistent field */
    private BigDecimal pcFrete;

    /** persistent field */
    private String dsTramoFreteInternacional;

	/** persistent field */
	private Boolean blCruze;

	/** persistent field */
	private Boolean blTramoOrigem;

    private Cliente cliente;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.DistrFreteInternacional distrFreteInternacional;

    private Integer versao;
    
    /** persistent field */
    private List trechoCtoInts;

    public Long getIdTramoFreteInternacional() {
        return this.idTramoFreteInternacional;
    }

    public void setIdTramoFreteInternacional(Long idTramoFreteInternacional) {
        this.idTramoFreteInternacional = idTramoFreteInternacional;
    }

    public Byte getNrTramoFreteInternacional() {
        return this.nrTramoFreteInternacional;
    }

    public void setNrTramoFreteInternacional(Byte nrTramoFreteInternacional) {
        this.nrTramoFreteInternacional = nrTramoFreteInternacional;
    }

    public BigDecimal getPcFrete() {
        return this.pcFrete;
    }

    public void setPcFrete(BigDecimal pcFrete) {
        this.pcFrete = pcFrete;
    }

    public String getDsTramoFreteInternacional() {
        return this.dsTramoFreteInternacional;
    }

    public void setDsTramoFreteInternacional(String dsTramoFreteInternacional) {
        this.dsTramoFreteInternacional = dsTramoFreteInternacional;
    }

	public Boolean getBlCruze() {
		return this.blCruze;
	}

	public void setBlCruze(Boolean blCruze) {
		this.blCruze = blCruze;
	}

	public Boolean getBlTramoOrigem() {
		return this.blTramoOrigem;
	}

	public void setBlTramoOrigem(Boolean blTramoOrigem) {
		this.blTramoOrigem = blTramoOrigem;
	}

    public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public com.mercurio.lms.configuracoes.model.DistrFreteInternacional getDistrFreteInternacional() {
        return this.distrFreteInternacional;
    }

	public void setDistrFreteInternacional(
			com.mercurio.lms.configuracoes.model.DistrFreteInternacional distrFreteInternacional) {
        this.distrFreteInternacional = distrFreteInternacional;
    }
    
    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.TrechoCtoInt.class)     
    public List getTrechoCtoInts() {
        return this.trechoCtoInts;
    }

    public void setTrechoCtoInts(List trechoCtoInts) {
        this.trechoCtoInts = trechoCtoInts;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTramoFreteInternacional",
				getIdTramoFreteInternacional()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TramoFreteInternacional))
			return false;
        TramoFreteInternacional castOther = (TramoFreteInternacional) other;
		return new EqualsBuilder().append(this.getIdTramoFreteInternacional(),
				castOther.getIdTramoFreteInternacional()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTramoFreteInternacional())
            .toHashCode();
    }

}
