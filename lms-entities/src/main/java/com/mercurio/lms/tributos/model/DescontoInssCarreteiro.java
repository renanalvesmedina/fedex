package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class DescontoInssCarreteiro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDescontoInssCarreteiro;

    /** persistent field */
    private BigDecimal vlInss;

    /** persistent field */
    private YearMonthDay dtEmissaoRecibo;

    /** persistent field */
    private String nrRecibo;

    /** persistent field */
    private String dsEmpresa;

    /** nullable persistent field */
    private DateTime dhInclusao;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario;

    private Filial filial;
    
    //LMS-5590
    /** persistent field */
    private DomainValue tpIdentificacao;
        
	/** persistent field */
    private String nrIdentEmpregador;
    
    /** persistent field */
    private Usuario usuario;
    
    /** persistent field */
    private BigDecimal vlRemuneracao;

    public Long getIdDescontoInssCarreteiro() {
        return this.idDescontoInssCarreteiro;
    }

    public void setIdDescontoInssCarreteiro(Long idDescontoInssCarreteiro) {
        this.idDescontoInssCarreteiro = idDescontoInssCarreteiro;
    }

    public BigDecimal getVlInss() {
        return this.vlInss;
    }

    public void setVlInss(BigDecimal vlInss) {
        this.vlInss = vlInss;
    }

    public YearMonthDay getDtEmissaoRecibo() {
        return this.dtEmissaoRecibo;
    }

    public void setDtEmissaoRecibo(YearMonthDay dtEmissaoRecibo) {
        this.dtEmissaoRecibo = dtEmissaoRecibo;
    }

    public String getNrRecibo() {
        return this.nrRecibo;
    }

    public void setNrRecibo(String nrRecibo) {
        this.nrRecibo = nrRecibo;
    }

    public String getDsEmpresa() {
        return this.dsEmpresa;
    }

    public void setDsEmpresa(String dsEmpresa) {
        this.dsEmpresa = dsEmpresa;
    }

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

    public com.mercurio.lms.contratacaoveiculos.model.Proprietario getProprietario() {
        return this.proprietario;
    }

    public void setProprietario(
			com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}
	
	public DomainValue getTpIdentificacao() {
		return tpIdentificacao;
	}

	public void setTpIdentificacao(DomainValue tpIdentificacao) {
		this.tpIdentificacao = tpIdentificacao;
	}

	public String getNrIdentEmpregador() {
		return nrIdentEmpregador;
	}

	public void setNrIdentEmpregador(String nrIdentEmpregador) {
		this.nrIdentEmpregador = nrIdentEmpregador;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getVlRemuneracao() {
		return vlRemuneracao;
	}

	public void setVlRemuneracao(BigDecimal vlRemuneracao) {
		this.vlRemuneracao = vlRemuneracao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idDescontoInssCarreteiro",
				getIdDescontoInssCarreteiro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DescontoInssCarreteiro))
			return false;
        DescontoInssCarreteiro castOther = (DescontoInssCarreteiro) other;
		return new EqualsBuilder().append(this.getIdDescontoInssCarreteiro(),
				castOther.getIdDescontoInssCarreteiro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDescontoInssCarreteiro())
            .toHashCode();
    }

}
