package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class IssMunicipioServico implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idIssMunicipioServico;

    /** nullable persistent field */
    private Byte ddRecolhimento;

    /** nullable persistent field */
    private DomainValue tpFormaPagamento;

    /** persistent field */
    private com.mercurio.lms.tributos.model.ServicoMunicipio servicoMunicipio;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.tributos.model.ServicoTributo servicoTributo;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional;

    /** persistent field */
    private List aliquotaIssMunicipioServs;

    public Long getIdIssMunicipioServico() {
        return this.idIssMunicipioServico;
    }

    public void setIdIssMunicipioServico(Long idIssMunicipioServico) {
        this.idIssMunicipioServico = idIssMunicipioServico;
    }

    public Byte getDdRecolhimento() {
        return this.ddRecolhimento;
    }

    public void setDdRecolhimento(Byte ddRecolhimento) {
        this.ddRecolhimento = ddRecolhimento;
    }

    public DomainValue getTpFormaPagamento() {
        return this.tpFormaPagamento;
    }

    public void setTpFormaPagamento(DomainValue tpFormaPagamento) {
        this.tpFormaPagamento = tpFormaPagamento;
    }

    public com.mercurio.lms.tributos.model.ServicoMunicipio getServicoMunicipio() {
        return this.servicoMunicipio;
    }

	public void setServicoMunicipio(
			com.mercurio.lms.tributos.model.ServicoMunicipio servicoMunicipio) {
        this.servicoMunicipio = servicoMunicipio;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.tributos.model.ServicoTributo getServicoTributo() {
        return this.servicoTributo;
    }

	public void setServicoTributo(
			com.mercurio.lms.tributos.model.ServicoTributo servicoTributo) {
        this.servicoTributo = servicoTributo;
    }

    public com.mercurio.lms.configuracoes.model.ServicoAdicional getServicoAdicional() {
        return this.servicoAdicional;
    }

	public void setServicoAdicional(
			com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional) {
        this.servicoAdicional = servicoAdicional;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.AliquotaIssMunicipioServ.class)     
    public List getAliquotaIssMunicipioServs() {
        return this.aliquotaIssMunicipioServs;
    }

    public void setAliquotaIssMunicipioServs(List aliquotaIssMunicipioServs) {
        this.aliquotaIssMunicipioServs = aliquotaIssMunicipioServs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idIssMunicipioServico",
				getIdIssMunicipioServico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof IssMunicipioServico))
			return false;
        IssMunicipioServico castOther = (IssMunicipioServico) other;
		return new EqualsBuilder().append(this.getIdIssMunicipioServico(),
				castOther.getIdIssMunicipioServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdIssMunicipioServico())
            .toHashCode();
    }

}
