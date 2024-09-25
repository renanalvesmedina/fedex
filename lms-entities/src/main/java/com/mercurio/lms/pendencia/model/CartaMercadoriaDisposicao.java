package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class CartaMercadoriaDisposicao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMercadoriaDisposicao;

    /** persistent field */
    private String nmContato;

    /** persistent field */
    private String nrTelefoneEmpresa;

    /** persistent field */
    private String nrFaxEmpresa;

    /** nullable persistent field */
    private Short nrRamal;

    /** nullable persistent field */
    private String dsEmailCliente;

    /** nullable persistent field */
    private String dsEmailFuncionario;

    /** nullable persistent field */
    private String obCarta;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** persistent field */
    private List nfCartaMercadorias;

    public Long getIdMercadoriaDisposicao() {
        return this.idMercadoriaDisposicao;
    }

    public void setIdMercadoriaDisposicao(Long idMercadoriaDisposicao) {
        this.idMercadoriaDisposicao = idMercadoriaDisposicao;
    }

    public String getNmContato() {
        return this.nmContato;
    }

    public void setNmContato(String nmContato) {
        this.nmContato = nmContato;
    }

    public String getNrTelefoneEmpresa() {
        return this.nrTelefoneEmpresa;
    }

    public void setNrTelefoneEmpresa(String nrTelefoneEmpresa) {
        this.nrTelefoneEmpresa = nrTelefoneEmpresa;
    }

    public String getNrFaxEmpresa() {
        return this.nrFaxEmpresa;
    }

    public void setNrFaxEmpresa(String nrFaxEmpresa) {
        this.nrFaxEmpresa = nrFaxEmpresa;
    }

    public Short getNrRamal() {
        return this.nrRamal;
    }

    public void setNrRamal(Short nrRamal) {
        this.nrRamal = nrRamal;
    }

    public String getDsEmailCliente() {
        return this.dsEmailCliente;
    }

    public void setDsEmailCliente(String dsEmailCliente) {
        this.dsEmailCliente = dsEmailCliente;
    }

    public String getDsEmailFuncionario() {
        return this.dsEmailFuncionario;
    }

    public void setDsEmailFuncionario(String dsEmailFuncionario) {
        this.dsEmailFuncionario = dsEmailFuncionario;
    }

    public String getObCarta() {
        return this.obCarta;
    }

    public void setObCarta(String obCarta) {
        this.obCarta = obCarta;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.NfCartaMercadoria.class)     
    public List getNfCartaMercadorias() {
        return this.nfCartaMercadorias;
    }

    public void setNfCartaMercadorias(List nfCartaMercadorias) {
        this.nfCartaMercadorias = nfCartaMercadorias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMercadoriaDisposicao",
				getIdMercadoriaDisposicao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CartaMercadoriaDisposicao))
			return false;
        CartaMercadoriaDisposicao castOther = (CartaMercadoriaDisposicao) other;
		return new EqualsBuilder().append(this.getIdMercadoriaDisposicao(),
				castOther.getIdMercadoriaDisposicao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMercadoriaDisposicao())
            .toHashCode();
    }

}
