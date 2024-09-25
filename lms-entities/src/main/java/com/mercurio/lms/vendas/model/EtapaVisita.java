package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EtapaVisita implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEtapaVisita;
    
    /** identifier field */
    private Integer versao;

    /** nullable persistent field */
    private String nrTelefone;

    /** nullable persistent field */
    private String nrDDD;
    
    /** nullable persistent field */
    private String nrDDI;
    
    /** nullable persistent field */
    private DomainValue tpModal;

    /** nullable persistent field */
    private DomainValue tpAbrangencia;

    /** nullable persistent field */
    private DomainValue tpPerspectivaFaturamento;

    /** nullable persistent field */
    private String dsContato;

    /** nullable persistent field */
    private String dsAreaAtuacao;

    /** nullable persistent field */
    private String dsEmail;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Visita visita;

    /** persistent field */
    private com.mercurio.lms.vendas.model.TipoVisita tipoVisita;

    /** persistent field */
    private com.mercurio.lms.vendas.model.CampanhaMarketing campanhaMarketing;

    /** persistent field */
    private List servicoOferecidos;

    public Long getIdEtapaVisita() {
        return this.idEtapaVisita;
    }

    public void setIdEtapaVisita(Long idEtapaVisita) {
        this.idEtapaVisita = idEtapaVisita;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public String getNrTelefone() {
        return this.nrTelefone;
    }

    public void setNrTelefone(String nrTelefone) {
        this.nrTelefone = nrTelefone;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }

    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
    }

    public DomainValue getTpPerspectivaFaturamento() {
        return this.tpPerspectivaFaturamento;
    }

    public void setTpPerspectivaFaturamento(DomainValue tpPerspectivaFaturamento) {
        this.tpPerspectivaFaturamento = tpPerspectivaFaturamento;
    }

    public String getDsContato() {
        return this.dsContato;
    }

    public void setDsContato(String dsContato) {
        this.dsContato = dsContato;
    }

    public String getDsAreaAtuacao() {
        return this.dsAreaAtuacao;
    }

    public void setDsAreaAtuacao(String dsAreaAtuacao) {
        this.dsAreaAtuacao = dsAreaAtuacao;
    }

    public String getDsEmail() {
        return this.dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    public com.mercurio.lms.vendas.model.Visita getVisita() {
        return this.visita;
    }

    public void setVisita(com.mercurio.lms.vendas.model.Visita visita) {
        this.visita = visita;
    }

    public com.mercurio.lms.vendas.model.TipoVisita getTipoVisita() {
        return this.tipoVisita;
    }

	public void setTipoVisita(
			com.mercurio.lms.vendas.model.TipoVisita tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    public com.mercurio.lms.vendas.model.CampanhaMarketing getCampanhaMarketing() {
        return this.campanhaMarketing;
    }

	public void setCampanhaMarketing(
			com.mercurio.lms.vendas.model.CampanhaMarketing campanhaMarketing) {
        this.campanhaMarketing = campanhaMarketing;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ServicoOferecido.class)     
    public List getServicoOferecidos() {
        return this.servicoOferecidos;
    }

    public void setServicoOferecidos(List servicoOferecidos) {
        this.servicoOferecidos = servicoOferecidos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEtapaVisita",
				getIdEtapaVisita()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EtapaVisita))
			return false;
        EtapaVisita castOther = (EtapaVisita) other;
		return new EqualsBuilder().append(this.getIdEtapaVisita(),
				castOther.getIdEtapaVisita()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEtapaVisita()).toHashCode();
    }

	public String getNrDDD() {
		return this.nrDDD;
	}

	public void setNrDDD(String nrDDD) {
		this.nrDDD = nrDDD;
	}

	public String getNrDDI() {
		return this.nrDDI;
	}

	public void setNrDDI(String nrDDI) {
		this.nrDDI = nrDDI;
	}

}
