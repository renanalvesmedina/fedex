package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class LimiteDesconto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLimiteDesconto;

    /** persistent field */
    private BigDecimal pcLimiteDesconto;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private DomainValue tpIndicadorDesconto;

    /** persistent field */
    private DomainValue tpTipoTabelaPreco;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.ParcelaPreco parcelaPreco;

    /** persistent field */
    private com.mercurio.lms.municipios.model.DivisaoGrupoClassificacao divisaoGrupoClassificacao;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco subtipoTabelaPreco;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.adsm.framework.model.pojo.Perfil perfil;
    
    public Long getIdLimiteDesconto() {
        return this.idLimiteDesconto;
    }

    public void setIdLimiteDesconto(Long idLimiteDesconto) {
        this.idLimiteDesconto = idLimiteDesconto;
    }

    public BigDecimal getPcLimiteDesconto() {
        return this.pcLimiteDesconto;
    }

    public void setPcLimiteDesconto(BigDecimal pcLimiteDesconto) {
        this.pcLimiteDesconto = pcLimiteDesconto;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public DomainValue getTpIndicadorDesconto() {
        return this.tpIndicadorDesconto;
    }

    public void setTpIndicadorDesconto(DomainValue tpIndicadorDesconto) {
        this.tpIndicadorDesconto = tpIndicadorDesconto;
    }

    public DomainValue getTpTipoTabelaPreco() {
        return this.tpTipoTabelaPreco;
    }

    public void setTpTipoTabelaPreco(DomainValue tpTipoTabelaPreco) {
        this.tpTipoTabelaPreco = tpTipoTabelaPreco;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.tabelaprecos.model.ParcelaPreco getParcelaPreco() {
        return this.parcelaPreco;
    }

	public void setParcelaPreco(
			com.mercurio.lms.tabelaprecos.model.ParcelaPreco parcelaPreco) {
        this.parcelaPreco = parcelaPreco;
    }

    public com.mercurio.lms.municipios.model.DivisaoGrupoClassificacao getDivisaoGrupoClassificacao() {
        return this.divisaoGrupoClassificacao;
    }

	public void setDivisaoGrupoClassificacao(
			com.mercurio.lms.municipios.model.DivisaoGrupoClassificacao divisaoGrupoClassificacao) {
        this.divisaoGrupoClassificacao = divisaoGrupoClassificacao;
    }

    public com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco getSubtipoTabelaPreco() {
        return this.subtipoTabelaPreco;
    }

	public void setSubtipoTabelaPreco(
			com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco subtipoTabelaPreco) {
        this.subtipoTabelaPreco = subtipoTabelaPreco;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLimiteDesconto",
				getIdLimiteDesconto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LimiteDesconto))
			return false;
        LimiteDesconto castOther = (LimiteDesconto) other;
		return new EqualsBuilder().append(this.getIdLimiteDesconto(),
				castOther.getIdLimiteDesconto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLimiteDesconto()).toHashCode();
    }
    
	public com.mercurio.adsm.framework.model.pojo.Perfil getPerfil() {
		return perfil;
	}
       	
	public void setPerfil(com.mercurio.adsm.framework.model.pojo.Perfil perfil) {
		this.perfil = perfil;
	}
    
}
