package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParametroGeral implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String FIND_SERVICOS_ADICIONAIS = "com.mercurio.lms.configuracoes.model.ParametroGeral.findServicosAdicionais";

    /** identifier field */
    private Long idParametroGeral;

    /** persistent field */
    private Short nrTamanho;

    /** persistent field */
    private String nmParametroGeral;

    /** persistent field */
    private VarcharI18n dsParametro;

    /** persistent field */
    private DomainValue tpFormato;

    /** persistent field */
    private String dsConteudo;

    public Long getIdParametroGeral() {
        return this.idParametroGeral;
    }

    public void setIdParametroGeral(Long idParametroGeral) {
        this.idParametroGeral = idParametroGeral;
    }

    public Short getNrTamanho() {
        return this.nrTamanho;
    }

    public void setNrTamanho(Short nrTamanho) {
        this.nrTamanho = nrTamanho;
    }

    public String getNmParametroGeral() {
        return this.nmParametroGeral;
    }

    public void setNmParametroGeral(String nmParametroGeral) {
        this.nmParametroGeral = nmParametroGeral;
    }

    public VarcharI18n getDsParametro() {
        return this.dsParametro;
    }

    public void setDsParametro(VarcharI18n dsParametro) {
        this.dsParametro = dsParametro;
    }

    public DomainValue getTpFormato() {
        return this.tpFormato;
    }

    public void setTpFormato(DomainValue tpFormato) {
        this.tpFormato = tpFormato;
    }

    public String getDsConteudo() {
        return this.dsConteudo;
    }

    public void setDsConteudo(String dsConteudo) {
        this.dsConteudo = dsConteudo;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idParametroGeral",
				getIdParametroGeral()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParametroGeral))
			return false;
        ParametroGeral castOther = (ParametroGeral) other;
		return new EqualsBuilder().append(this.getIdParametroGeral(),
				castOther.getIdParametroGeral()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParametroGeral()).toHashCode();
    }

}
