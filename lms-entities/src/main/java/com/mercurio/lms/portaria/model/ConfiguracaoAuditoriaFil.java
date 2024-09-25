package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ConfiguracaoAuditoriaFil implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idConfiguracaoAuditoriaFil;

    /** persistent field */
    private DomainValue tpOperacao;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private TimeOfDay hrAuditoriaInicial;

    /** nullable persistent field */
    private TimeOfDay hrAuditoriaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private RotaIdaVolta rotaIdaVolta;

    public Long getIdConfiguracaoAuditoriaFil() {
        return this.idConfiguracaoAuditoriaFil;
    }

    public void setIdConfiguracaoAuditoriaFil(Long idConfiguracaoAuditoriaFil) {
        this.idConfiguracaoAuditoriaFil = idConfiguracaoAuditoriaFil;
    }

    public DomainValue getTpOperacao() {
        return this.tpOperacao;
    }

    public void setTpOperacao(DomainValue tpOperacao) {
        this.tpOperacao = tpOperacao;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public TimeOfDay getHrAuditoriaInicial() {
        return this.hrAuditoriaInicial;
    }

    public void setHrAuditoriaInicial(TimeOfDay hrAuditoriaInicial) {
        this.hrAuditoriaInicial = hrAuditoriaInicial;
    }

    public TimeOfDay getHrAuditoriaFinal() {
        return this.hrAuditoriaFinal;
    }

    public void setHrAuditoriaFinal(TimeOfDay hrAuditoriaFinal) {
        this.hrAuditoriaFinal = hrAuditoriaFinal;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviario() {
        return this.meioTransporteRodoviario;
    }

	public void setMeioTransporteRodoviario(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario) {
        this.meioTransporteRodoviario = meioTransporteRodoviario;
    }

    public com.mercurio.lms.municipios.model.RotaColetaEntrega getRotaColetaEntrega() {
        return this.rotaColetaEntrega;
    }

	public void setRotaColetaEntrega(
			com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega) {
        this.rotaColetaEntrega = rotaColetaEntrega;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public RotaIdaVolta getRotaIdaVolta() {
        return this.rotaIdaVolta;
    }

    public void setRotaIdaVolta(RotaIdaVolta rotaIdaVolta) {
        this.rotaIdaVolta = rotaIdaVolta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idConfiguracaoAuditoriaFil",
				getIdConfiguracaoAuditoriaFil()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ConfiguracaoAuditoriaFil))
			return false;
        ConfiguracaoAuditoriaFil castOther = (ConfiguracaoAuditoriaFil) other;
		return new EqualsBuilder().append(this.getIdConfiguracaoAuditoriaFil(),
				castOther.getIdConfiguracaoAuditoriaFil()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdConfiguracaoAuditoriaFil())
            .toHashCode();
    }

}
