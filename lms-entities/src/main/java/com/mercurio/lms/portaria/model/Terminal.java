package com.mercurio.lms.portaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Terminal implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTerminal;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private BigDecimal nrAreaTotal;

    /** nullable persistent field */
    private BigDecimal nrAreaArmazenagem;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private String obTerminal;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List docas;

    /** persistent field */
    private List modulos;

    /** persistent field */
    private List portarias;

    public Long getIdTerminal() {
        return this.idTerminal;
    }

    public void setIdTerminal(Long idTerminal) {
        this.idTerminal = idTerminal;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public BigDecimal getNrAreaTotal() {
        return this.nrAreaTotal;
    }

    public void setNrAreaTotal(BigDecimal nrAreaTotal) {
        this.nrAreaTotal = nrAreaTotal;
    }

    public BigDecimal getNrAreaArmazenagem() {
        return this.nrAreaArmazenagem;
    }

    public void setNrAreaArmazenagem(BigDecimal nrAreaArmazenagem) {
        this.nrAreaArmazenagem = nrAreaArmazenagem;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public String getObTerminal() {
        return this.obTerminal;
    }

    public void setObTerminal(String obTerminal) {
        this.obTerminal = obTerminal;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.Doca.class)     
    public List getDocas() {
        return this.docas;
    }

    public void setDocas(List docas) {
        this.docas = docas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.Modulo.class)     
    public List getModulos() {
        return this.modulos;
    }

    public void setModulos(List modulos) {
        this.modulos = modulos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.Portaria.class)     
    public List getPortarias() {
        return this.portarias;
    }

    public void setPortarias(List portarias) {
        this.portarias = portarias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTerminal", getIdTerminal())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Terminal))
			return false;
        Terminal castOther = (Terminal) other;
		return new EqualsBuilder().append(this.getIdTerminal(),
				castOther.getIdTerminal()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTerminal()).toHashCode();
    }

}
