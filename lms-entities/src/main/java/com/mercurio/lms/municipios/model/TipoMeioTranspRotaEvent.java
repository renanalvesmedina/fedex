package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoMeioTranspRotaEvent implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoMeioTranspRotaEvent;

    /** persistent field */
    private BigDecimal vlFrete;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaIdaVolta rotaIdaVolta;

    public Long getIdTipoMeioTranspRotaEvent() {
        return this.idTipoMeioTranspRotaEvent;
    }

    public void setIdTipoMeioTranspRotaEvent(Long idTipoMeioTranspRotaEvent) {
        this.idTipoMeioTranspRotaEvent = idTipoMeioTranspRotaEvent;
    }

    public BigDecimal getVlFrete() {
        return this.vlFrete;
    }

    public void setVlFrete(BigDecimal vlFrete) {
        this.vlFrete = vlFrete;
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

    public com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

	public void setTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    public com.mercurio.lms.municipios.model.RotaIdaVolta getRotaIdaVolta() {
        return this.rotaIdaVolta;
    }

	public void setRotaIdaVolta(
			com.mercurio.lms.municipios.model.RotaIdaVolta rotaIdaVolta) {
        this.rotaIdaVolta = rotaIdaVolta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoMeioTranspRotaEvent",
				getIdTipoMeioTranspRotaEvent()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoMeioTranspRotaEvent))
			return false;
        TipoMeioTranspRotaEvent castOther = (TipoMeioTranspRotaEvent) other;
		return new EqualsBuilder().append(this.getIdTipoMeioTranspRotaEvent(),
				castOther.getIdTipoMeioTranspRotaEvent()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoMeioTranspRotaEvent())
            .toHashCode();
    }
    
}
