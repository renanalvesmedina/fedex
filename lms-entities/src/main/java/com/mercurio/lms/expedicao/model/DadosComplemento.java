package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;

/** @author LMS Custom Hibernate CodeGenerator */
public class DadosComplemento implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idDadosComplemento;

    /** nullable persistent field */
    private String dsValorCampo;

    /** persistent field */
    private Conhecimento conhecimento;

    /** persistent field */
    private InformacaoDocServico informacaoDocServico;

    /** persistent field */
    private InformacaoDoctoCliente informacaoDoctoCliente;

    /** persistent field */
    private List<NfDadosComp> nfDadosComps;

    public Long getIdDadosComplemento() {
        return this.idDadosComplemento;
    }

    public void setIdDadosComplemento(Long idDadosComplemento) {
        this.idDadosComplemento = idDadosComplemento;
    }

    public String getDsValorCampo() {
        return this.dsValorCampo;
    }

    public void setDsValorCampo(String dsValorCampo) {
        this.dsValorCampo = dsValorCampo;
    }

    public Conhecimento getConhecimento() {
        return this.conhecimento;
    }

    public void setConhecimento(Conhecimento conhecimento) {
        this.conhecimento = conhecimento;
    }

    public InformacaoDocServico getInformacaoDocServico() {
        return this.informacaoDocServico;
    }

	public void setInformacaoDocServico(
			InformacaoDocServico informacaoDocServico) {
        this.informacaoDocServico = informacaoDocServico;
    }

    public InformacaoDoctoCliente getInformacaoDoctoCliente() {
        return this.informacaoDoctoCliente;
    }

	public void setInformacaoDoctoCliente(
			InformacaoDoctoCliente informacaoDoctoCliente) {
        this.informacaoDoctoCliente = informacaoDoctoCliente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.NfDadosComp.class)     
    public List<NfDadosComp> getNfDadosComps() {
        return this.nfDadosComps;
    }

    public void setNfDadosComps(List<NfDadosComp> nfDadosComps) {
        this.nfDadosComps = nfDadosComps;
    }

    public void addNfDadosComps(NfDadosComp nfDadosComp) {
    	if (this.nfDadosComps == null) {
    		this.nfDadosComps = new ArrayList<NfDadosComp>();
    	}
    	this.nfDadosComps.add(nfDadosComp);
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDadosComplemento",
				getIdDadosComplemento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DadosComplemento))
			return false;
        DadosComplemento castOther = (DadosComplemento) other;
		return new EqualsBuilder().append(this.getIdDadosComplemento(),
				castOther.getIdDadosComplemento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDadosComplemento())
            .toHashCode();
    }

}
