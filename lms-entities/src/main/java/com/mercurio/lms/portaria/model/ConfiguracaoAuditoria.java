package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ConfiguracaoAuditoria implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idConfiguracaoAuditoria;

    /** persistent field */
    private Short nrPrazoAuditoria;

    /** persistent field */
    private DomainValue tpOperacao;

    /** persistent field */
    private Integer hrTempoAuditoria;
 
    /** persistent field */
    private TimeOfDay hrConfiguracaoFinal;

    /** persistent field */
    private TimeOfDay hrConfiguracaoInicial;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private Short qtVeiculosProprios;

    /** nullable persistent field */
    private Short qtVeiculosTerceiros;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdConfiguracaoAuditoria() {
        return this.idConfiguracaoAuditoria;
    }

    public void setIdConfiguracaoAuditoria(Long idConfiguracaoAuditoria) {
        this.idConfiguracaoAuditoria = idConfiguracaoAuditoria;
    }

    public Short getNrPrazoAuditoria() {
        return this.nrPrazoAuditoria;
    }

    public void setNrPrazoAuditoria(Short nrPrazoAuditoria) {
        this.nrPrazoAuditoria = nrPrazoAuditoria;
    }

    public DomainValue getTpOperacao() {
        return this.tpOperacao;
    }

    public void setTpOperacao(DomainValue tpOperacao) {
        this.tpOperacao = tpOperacao;
    }

    public Integer getHrTempoAuditoria() {
        return this.hrTempoAuditoria;
    }

    public void setHrTempoAuditoria(Integer hrTempoAuditoria) {
        this.hrTempoAuditoria = hrTempoAuditoria;
    }

    public TimeOfDay getHrConfiguracaoFinal() {
        return this.hrConfiguracaoFinal;
    }

    public void setHrConfiguracaoFinal(TimeOfDay hrConfiguracaoFinal) {
        this.hrConfiguracaoFinal = hrConfiguracaoFinal;
    }

    public TimeOfDay getHrConfiguracaoInicial() {
        return this.hrConfiguracaoInicial;
    }

    public void setHrConfiguracaoInicial(TimeOfDay hrConfiguracaoInicial) {
        this.hrConfiguracaoInicial = hrConfiguracaoInicial;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public Short getQtVeiculosProprios() {
        return this.qtVeiculosProprios;
    }

    public void setQtVeiculosProprios(Short qtVeiculosProprios) {
        this.qtVeiculosProprios = qtVeiculosProprios;
    }

    public Short getQtVeiculosTerceiros() {
        return this.qtVeiculosTerceiros;
    }

    public void setQtVeiculosTerceiros(Short qtVeiculosTerceiros) {
        this.qtVeiculosTerceiros = qtVeiculosTerceiros;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idConfiguracaoAuditoria",
				getIdConfiguracaoAuditoria()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ConfiguracaoAuditoria))
			return false;
        ConfiguracaoAuditoria castOther = (ConfiguracaoAuditoria) other;
		return new EqualsBuilder().append(this.getIdConfiguracaoAuditoria(),
				castOther.getIdConfiguracaoAuditoria()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdConfiguracaoAuditoria())
            .toHashCode();
    }

}
