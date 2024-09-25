package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ProtocoloTransferencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idProtocoloTransferencia;

    /** persistent field */
    private DateTime dhProtocolo;

    /** persistent field */
    private DomainValue tpDestinatario;

    /** nullable persistent field */
    private String nmRecebedorCliente;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Funcionario funcionario;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Setor setor;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List lotePendencias;

    public Long getIdProtocoloTransferencia() {
        return this.idProtocoloTransferencia;
    }

    public void setIdProtocoloTransferencia(Long idProtocoloTransferencia) {
        this.idProtocoloTransferencia = idProtocoloTransferencia;
    }

    public DateTime getDhProtocolo() {
        return this.dhProtocolo;
    }

    public void setDhProtocolo(DateTime dhProtocolo) {
        this.dhProtocolo = dhProtocolo;
    }

    public DomainValue getTpDestinatario() {
        return this.tpDestinatario;
    }

    public void setTpDestinatario(DomainValue tpDestinatario) {
        this.tpDestinatario = tpDestinatario;
    }

    public String getNmRecebedorCliente() {
        return this.nmRecebedorCliente;
    }

    public void setNmRecebedorCliente(String nmRecebedorCliente) {
        this.nmRecebedorCliente = nmRecebedorCliente;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.configuracoes.model.Funcionario getFuncionario() {
        return this.funcionario;
    }

	public void setFuncionario(
			com.mercurio.lms.configuracoes.model.Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public com.mercurio.lms.configuracoes.model.Setor getSetor() {
        return this.setor;
    }

    public void setSetor(com.mercurio.lms.configuracoes.model.Setor setor) {
        this.setor = setor;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.LotePendencia.class)     
    public List getLotePendencias() {
        return this.lotePendencias;
    }

    public void setLotePendencias(List lotePendencias) {
        this.lotePendencias = lotePendencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idProtocoloTransferencia",
				getIdProtocoloTransferencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ProtocoloTransferencia))
			return false;
        ProtocoloTransferencia castOther = (ProtocoloTransferencia) other;
		return new EqualsBuilder().append(this.getIdProtocoloTransferencia(),
				castOther.getIdProtocoloTransferencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdProtocoloTransferencia())
            .toHashCode();
    }

}
