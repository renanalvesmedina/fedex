package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoTabelaPreco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoTabelaPreco;

    /** persistent field */
    private String dsIdentificacao;

    /** persistent field */
    private DomainValue tpTipoTabelaPreco;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private Integer nrVersao;

    private Integer nrVersaoFinal;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresaByIdEmpresaCadastrada;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresaByIdEmpresaLogada;

    /** persistent field */
    private List tabelaPrecos;

    public Long getIdTipoTabelaPreco() {
        return this.idTipoTabelaPreco;
    }

    public void setIdTipoTabelaPreco(Long idTipoTabelaPreco) {
        this.idTipoTabelaPreco = idTipoTabelaPreco;
    }

    public String getDsIdentificacao() {
        return this.dsIdentificacao;
    }

    public void setDsIdentificacao(String dsIdentificacao) {
        this.dsIdentificacao = dsIdentificacao;
    }

    public DomainValue getTpTipoTabelaPreco() {
        return this.tpTipoTabelaPreco;
    }

    public void setTpTipoTabelaPreco(DomainValue tpTipoTabelaPreco) {
        this.tpTipoTabelaPreco = tpTipoTabelaPreco;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Integer getNrVersao() {
        return this.nrVersao;
    }

    public void setNrVersao(Integer nrVersao) {
        this.nrVersao = nrVersao;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresaByIdEmpresaCadastrada() {
        return this.empresaByIdEmpresaCadastrada;
    }

	public void setEmpresaByIdEmpresaCadastrada(
			com.mercurio.lms.municipios.model.Empresa empresaByIdEmpresaCadastrada) {
        this.empresaByIdEmpresaCadastrada = empresaByIdEmpresaCadastrada;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresaByIdEmpresaLogada() {
        return this.empresaByIdEmpresaLogada;
    }

	public void setEmpresaByIdEmpresaLogada(
			com.mercurio.lms.municipios.model.Empresa empresaByIdEmpresaLogada) {
        this.empresaByIdEmpresaLogada = empresaByIdEmpresaLogada;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.TabelaPreco.class)     
    public List getTabelaPrecos() {
        return this.tabelaPrecos;
    }

    public void setTabelaPrecos(List tabelaPrecos) {
        this.tabelaPrecos = tabelaPrecos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoTabelaPreco",
				getIdTipoTabelaPreco()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoTabelaPreco))
			return false;
        TipoTabelaPreco castOther = (TipoTabelaPreco) other;
		return new EqualsBuilder().append(this.getIdTipoTabelaPreco(),
				castOther.getIdTipoTabelaPreco()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoTabelaPreco())
            .toHashCode();
    }

    public String getTpTipoTabelaPrecoNrVersao(){
    	String retorno = null;
    	if(tpTipoTabelaPreco != null) {
			retorno = (nrVersao != null ? tpTipoTabelaPreco.getValue()
					+ nrVersao : tpTipoTabelaPreco.getValue());
    	}
    	return retorno;
    }

	public String getNomeServico() {
		if(Hibernate.isInitialized(getServico()) && getServico() != null){
			return getServico().getNomeServico();
		}
		return StringUtils.EMPTY;
	}

    public Integer getNrVersaoFinal() {
        return nrVersaoFinal;
    }

    public void setNrVersaoFinal(Integer nrVersaoFinal) {
        this.nrVersaoFinal = nrVersaoFinal;
    }
}
