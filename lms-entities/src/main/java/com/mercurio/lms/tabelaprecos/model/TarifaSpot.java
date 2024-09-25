package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TarifaSpot implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTarifaSpot;

    /** persistent field */
    private BigDecimal vlTarifaSpot;

    /** persistent field */
    private Byte nrPossibilidades;

    /** persistent field */
    private YearMonthDay dtLiberacao;

    /** persistent field */
    private String dsSenha;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private Byte nrUtilizacoes;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Aeroporto aeroportoByIdAeroportoDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Aeroporto aeroportoByIdAeroportoOrigem;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioSolicitante;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioLiberador;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private List awbs;

	public Long getIdTarifaSpot() {
        return this.idTarifaSpot;
    }

    public void setIdTarifaSpot(Long idTarifaSpot) {
        this.idTarifaSpot = idTarifaSpot;
    }

    public BigDecimal getVlTarifaSpot() {
        return this.vlTarifaSpot;
    }

    public void setVlTarifaSpot(BigDecimal vlTarifaSpot) {
        this.vlTarifaSpot = vlTarifaSpot;
    }

    public Byte getNrPossibilidades() {
        return this.nrPossibilidades;
    }

    public void setNrPossibilidades(Byte nrPossibilidades) {
        this.nrPossibilidades = nrPossibilidades;
    }

    public YearMonthDay getDtLiberacao() {
        return this.dtLiberacao;
    }

    public void setDtLiberacao(YearMonthDay dtLiberacao) {
        this.dtLiberacao = dtLiberacao;
    }

    public String getDsSenha() {
        return this.dsSenha;
    }

    public void setDsSenha(String dsSenha) {
        this.dsSenha = dsSenha;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Byte getNrUtilizacoes() {
        return this.nrUtilizacoes;
    }

    public void setNrUtilizacoes(Byte nrUtilizacoes) {
        this.nrUtilizacoes = nrUtilizacoes;
    }

    public com.mercurio.lms.municipios.model.Aeroporto getAeroportoByIdAeroportoDestino() {
        return this.aeroportoByIdAeroportoDestino;
    }

	public void setAeroportoByIdAeroportoDestino(
			com.mercurio.lms.municipios.model.Aeroporto aeroportoByIdAeroportoDestino) {
        this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
    }

    public com.mercurio.lms.municipios.model.Aeroporto getAeroportoByIdAeroportoOrigem() {
        return this.aeroportoByIdAeroportoOrigem;
    }

	public void setAeroportoByIdAeroportoOrigem(
			com.mercurio.lms.municipios.model.Aeroporto aeroportoByIdAeroportoOrigem) {
        this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioSolicitante() {
        return this.usuarioByIdUsuarioSolicitante;
    }

	public void setUsuarioByIdUsuarioSolicitante(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioSolicitante) {
        this.usuarioByIdUsuarioSolicitante = usuarioByIdUsuarioSolicitante;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioLiberador() {
        return this.usuarioByIdUsuarioLiberador;
    }

	public void setUsuarioByIdUsuarioLiberador(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioLiberador) {
        this.usuarioByIdUsuarioLiberador = usuarioByIdUsuarioLiberador;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Awb.class)     
    public List getAwbs() {
        return this.awbs;
    }

    public void setAwbs(List awbs) {
        this.awbs = awbs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTarifaSpot",
				getIdTarifaSpot()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TarifaSpot))
			return false;
        TarifaSpot castOther = (TarifaSpot) other;
		return new EqualsBuilder().append(this.getIdTarifaSpot(),
				castOther.getIdTarifaSpot()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTarifaSpot()).toHashCode();
    }

}
