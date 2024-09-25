package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItChecklistTpMeioTransp implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItChecklistTpMeioTransp;

    /** persistent field */
    private DomainValue tpItChecklistTpMeioTransp;

    /** persistent field */
    private Boolean blObrigatorioAprovacao; 

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.ItemCheckList itemCheckList;

    /** persistent field */
    private List respostaChecklists;

    public Long getIdItChecklistTpMeioTransp() {
        return this.idItChecklistTpMeioTransp;
    }

    public void setIdItChecklistTpMeioTransp(Long idItChecklistTpMeioTransp) {
        this.idItChecklistTpMeioTransp = idItChecklistTpMeioTransp;
    }

    public DomainValue getTpItChecklistTpMeioTransp() {
        return this.tpItChecklistTpMeioTransp;
    }

	public void setTpItChecklistTpMeioTransp(
			DomainValue tpItChecklistTpMeioTransp) {
        this.tpItChecklistTpMeioTransp = tpItChecklistTpMeioTransp;
    }

    public Boolean getBlObrigatorioAprovacao() {
        return this.blObrigatorioAprovacao;
    }

    public void setBlObrigatorioAprovacao(Boolean blObrigatorioAprovacao) {
        this.blObrigatorioAprovacao = blObrigatorioAprovacao;
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

    public com.mercurio.lms.contratacaoveiculos.model.ItemCheckList getItemCheckList() {
        return this.itemCheckList;
    }

	public void setItemCheckList(
			com.mercurio.lms.contratacaoveiculos.model.ItemCheckList itemCheckList) {
        this.itemCheckList = itemCheckList;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.RespostaChecklist.class)     
    public List getRespostaChecklists() {
        return this.respostaChecklists;
    }

    public void setRespostaChecklists(List respostaChecklists) {
        this.respostaChecklists = respostaChecklists;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idItChecklistTpMeioTransp",
				getIdItChecklistTpMeioTransp()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItChecklistTpMeioTransp))
			return false;
        ItChecklistTpMeioTransp castOther = (ItChecklistTpMeioTransp) other;
		return new EqualsBuilder().append(this.getIdItChecklistTpMeioTransp(),
				castOther.getIdItChecklistTpMeioTransp()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItChecklistTpMeioTransp())
            .toHashCode();
    }

}
