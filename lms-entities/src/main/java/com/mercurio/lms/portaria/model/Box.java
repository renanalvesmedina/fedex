package com.mercurio.lms.portaria.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Box implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idBox;

    /** persistent field */
    private Short nrBox;

    /** persistent field */
    private DomainValue tpSituacaoBox;

    /** persistent field */
    @Temporal(TemporalType.DATE)
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    @Temporal(TemporalType.DATE)
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private String dsBox;

    /** nullable persistent field */
    private String obBox;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.Modulo modulo;

    /** persistent field */
    private com.mercurio.lms.portaria.model.Doca doca;

    /** persistent field */
    private List meioTransporteRodoBoxs;

    /** persistent field */
    private List boxFinalidades;

    /** persistent field */
    private List carregamentoDescargas;

    public Long getIdBox() {
        return this.idBox;
    }

    public void setIdBox(Long idBox) {
        this.idBox = idBox;
    }

    public Short getNrBox() {
        return this.nrBox;
    }

    public void setNrBox(Short nrBox) {
        this.nrBox = nrBox;
    }

    public DomainValue getTpSituacaoBox() {
        return this.tpSituacaoBox;
    }

    public void setTpSituacaoBox(DomainValue tpSituacaoBox) {
        this.tpSituacaoBox = tpSituacaoBox;
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

    public String getDsBox() {
        return this.dsBox;
    }

    public void setDsBox(String dsBox) {
        this.dsBox = dsBox;
    }

    public String getObBox() {
        return this.obBox;
    }

    public void setObBox(String obBox) {
        this.obBox = obBox;
    }

    public com.mercurio.lms.pendencia.model.Modulo getModulo() {
        return this.modulo;
    }

    public void setModulo(com.mercurio.lms.pendencia.model.Modulo modulo) {
        this.modulo = modulo;
    }

    public com.mercurio.lms.portaria.model.Doca getDoca() {
        return this.doca;
    }

    public void setDoca(com.mercurio.lms.portaria.model.Doca doca) {
        this.doca = doca;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.MeioTransporteRodoBox.class)     
    public List getMeioTransporteRodoBoxs() {
        return this.meioTransporteRodoBoxs;
    }

    public void setMeioTransporteRodoBoxs(List meioTransporteRodoBoxs) {
        this.meioTransporteRodoBoxs = meioTransporteRodoBoxs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.BoxFinalidade.class)     
    public List getBoxFinalidades() {
        return this.boxFinalidades;
    }

    public void setBoxFinalidades(List boxFinalidades) {
        this.boxFinalidades = boxFinalidades;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.CarregamentoDescarga.class)     
    public List getCarregamentoDescargas() {
        return this.carregamentoDescargas;
    }

    public void setCarregamentoDescargas(List carregamentoDescargas) {
        this.carregamentoDescargas = carregamentoDescargas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idBox", getIdBox()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_BOX", idBox)
				.append("ID_DOCA", doca != null ? doca.getIdDoca() : null)
				.append("NR_BOX", nrBox)
				.append("TP_SITUACAO_BOX", tpSituacaoBox != null ? tpSituacaoBox.getValue() : null)
				.append("DT_VIGENCIA_INICIAL", dtVigenciaInicial)
				.append("ID_MODULO", modulo != null ? modulo.getIdModulo() : null)
				.append("DT_VIGENCIA_FINAL", dtVigenciaFinal)
				.append("DS_BOX", dsBox)
				.append("OB_BOX", obBox)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Box))
			return false;
        Box castOther = (Box) other;
        return new EqualsBuilder()
				.append(this.getIdBox(), castOther.getIdBox()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdBox()).toHashCode();
    }

}
