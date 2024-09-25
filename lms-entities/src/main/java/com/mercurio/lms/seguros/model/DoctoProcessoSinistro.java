package com.mercurio.lms.seguros.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class DoctoProcessoSinistro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDoctoProcessoSinistro;

    /** persistent field */
    private String nrDocumento;

    /** persistent field */
    private DateTime dhCadastroDocumento;

    /** persistent field */
    private DomainValue tpEntregaRecebimento;

    /** nullable persistent field */
    private DateTime dhEmissaoProtocolo;

    /** nullable persistent field */
    private String nmRecebedor;

    /** nullable persistent field */
    private Long nrProtocolo;

    /** nullable persistent field */
    private String obDocumentoProcesso;

    /** persistent field */
    private com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro;

    /** persistent field */
    private com.mercurio.lms.seguros.model.TipoDocumentoSeguro tipoDocumentoSeguro;

    public Long getIdDoctoProcessoSinistro() {
        return this.idDoctoProcessoSinistro;
    }

    public void setIdDoctoProcessoSinistro(Long idDoctoProcessoSinistro) {
        this.idDoctoProcessoSinistro = idDoctoProcessoSinistro;
    }

    public String getNrDocumento() {
        return this.nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public DateTime getDhCadastroDocumento() {
        return this.dhCadastroDocumento;
    }

    public void setDhCadastroDocumento(DateTime dhCadastroDocumento) {
        this.dhCadastroDocumento = dhCadastroDocumento;
    }

    public DomainValue getTpEntregaRecebimento() {
        return this.tpEntregaRecebimento;
    }

    public void setTpEntregaRecebimento(DomainValue tpEntregaRecebimento) {
        this.tpEntregaRecebimento = tpEntregaRecebimento;
    }

    public DateTime getDhEmissaoProtocolo() {
        return this.dhEmissaoProtocolo;
    }

    public void setDhEmissaoProtocolo(DateTime dhEmissaoProtocolo) {
        this.dhEmissaoProtocolo = dhEmissaoProtocolo;
    }

    public String getNmRecebedor() {
        return this.nmRecebedor;
    }

    public void setNmRecebedor(String nmRecebedor) {
        this.nmRecebedor = nmRecebedor;
    }

    public Long getNrProtocolo() {
        return this.nrProtocolo;
    }

    public void setNrProtocolo(Long nrProtocolo) {
        this.nrProtocolo = nrProtocolo;
    }

    public String getObDocumentoProcesso() {
        return this.obDocumentoProcesso;
    }

    public void setObDocumentoProcesso(String obDocumentoProcesso) {
        this.obDocumentoProcesso = obDocumentoProcesso;
    }

    public com.mercurio.lms.seguros.model.ProcessoSinistro getProcessoSinistro() {
        return this.processoSinistro;
    }

	public void setProcessoSinistro(
			com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro) {
        this.processoSinistro = processoSinistro;
    }

    public com.mercurio.lms.seguros.model.TipoDocumentoSeguro getTipoDocumentoSeguro() {
        return this.tipoDocumentoSeguro;
    }

	public void setTipoDocumentoSeguro(
			com.mercurio.lms.seguros.model.TipoDocumentoSeguro tipoDocumentoSeguro) {
        this.tipoDocumentoSeguro = tipoDocumentoSeguro;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDoctoProcessoSinistro",
				getIdDoctoProcessoSinistro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DoctoProcessoSinistro))
			return false;
        DoctoProcessoSinistro castOther = (DoctoProcessoSinistro) other;
		return new EqualsBuilder().append(this.getIdDoctoProcessoSinistro(),
				castOther.getIdDoctoProcessoSinistro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDoctoProcessoSinistro())
            .toHashCode();
    }

}
