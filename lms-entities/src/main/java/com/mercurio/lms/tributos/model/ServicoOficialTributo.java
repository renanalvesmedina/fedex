package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ServicoOficialTributo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idServicoOficialTributo;

    /** persistent field */
    private Long nrServicoOficialTributo;

    /** persistent field */
    private String dsServicoOficialTributo;

    /** persistent field */
    private DomainValue tpLocalDevido;

    /** persistent field */
    private Boolean blRetencaoTomadorServico;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private String obServicoOficialTributo;

    /** persistent field */
    private List servicoAdicionais;

    /** persistent field */
    private List servicoTributos;

    public Long getIdServicoOficialTributo() {
        return this.idServicoOficialTributo;
    }

    public void setIdServicoOficialTributo(Long idServicoOficialTributo) {
        this.idServicoOficialTributo = idServicoOficialTributo;
    }

    public Long getNrServicoOficialTributo() {
        return this.nrServicoOficialTributo;
    }

    public void setNrServicoOficialTributo(Long nrServicoOficialTributo) {
        this.nrServicoOficialTributo = nrServicoOficialTributo;
    }

    public String getDsServicoOficialTributo() {
        return this.dsServicoOficialTributo;
    }

    public void setDsServicoOficialTributo(String dsServicoOficialTributo) {
        this.dsServicoOficialTributo = dsServicoOficialTributo;
    }

    public DomainValue getTpLocalDevido() {
        return this.tpLocalDevido;
    }

    public void setTpLocalDevido(DomainValue tpLocalDevido) {
        this.tpLocalDevido = tpLocalDevido;
    }

    public Boolean getBlRetencaoTomadorServico() {
        return this.blRetencaoTomadorServico;
    }

    public void setBlRetencaoTomadorServico(Boolean blRetencaoTomadorServico) {
        this.blRetencaoTomadorServico = blRetencaoTomadorServico;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getObServicoOficialTributo() {
        return this.obServicoOficialTributo;
    }

    public void setObServicoOficialTributo(String obServicoOficialTributo) {
        this.obServicoOficialTributo = obServicoOficialTributo;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ServicoAdicional.class)     
    public List getServicoAdicionais() {
        return this.servicoAdicionais;
    }

    public void setServicoAdicionais(List servicoAdicionais) {
        this.servicoAdicionais = servicoAdicionais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.ServicoTributo.class)     
    public List getServicoTributos() {
        return this.servicoTributos;
    }

    public void setServicoTributos(List servicoTributos) {
        this.servicoTributos = servicoTributos;
    }
    
    /**
	 * Retorna o Número do Serviço Oficial Tributos concatenado com a descrição
	 * do serviço oficial. Exemplo: Número - Descrição
	 * 
     * @return String Contendo Número - Descrição
     */
    public String getNrServicoTributoDsServicoTributo(){
		return this.getNrServicoOficialTributo() + " - "
				+ this.getDsServicoOficialTributo();
    }

    public String toString() {
		return new ToStringBuilder(this).append("idServicoOficialTributo",
				getIdServicoOficialTributo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ServicoOficialTributo))
			return false;
        ServicoOficialTributo castOther = (ServicoOficialTributo) other;
		return new EqualsBuilder().append(this.getIdServicoOficialTributo(),
				castOther.getIdServicoOficialTributo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdServicoOficialTributo())
            .toHashCode();
    }

}
