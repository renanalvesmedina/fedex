package com.mercurio.lms.municipios.model;

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
public class TarifaPostoPassagem implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTarifaPostoPassagem;
    
    private Integer versao;

    /** persistent field */
    private DomainValue tpFormaCobranca;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.PostoPassagem postoPassagem;

    /** persistent field */
    private List valorTarifaPostoPassagems;

    public Long getIdTarifaPostoPassagem() {
        return this.idTarifaPostoPassagem;
    }

    public void setIdTarifaPostoPassagem(Long idTarifaPostoPassagem) {
        this.idTarifaPostoPassagem = idTarifaPostoPassagem;
    }

    public DomainValue getTpFormaCobranca() {
        return this.tpFormaCobranca;
    }

    public void setTpFormaCobranca(DomainValue tpFormaCobranca) {
        this.tpFormaCobranca = tpFormaCobranca;
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

    public com.mercurio.lms.municipios.model.PostoPassagem getPostoPassagem() {
        return this.postoPassagem;
    }

	public void setPostoPassagem(
			com.mercurio.lms.municipios.model.PostoPassagem postoPassagem) {
        this.postoPassagem = postoPassagem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.ValorTarifaPostoPassagem.class)     
    public List getValorTarifaPostoPassagems() {
        return this.valorTarifaPostoPassagems;
    }

    public void setValorTarifaPostoPassagems(List valorTarifaPostoPassagems) {
        this.valorTarifaPostoPassagems = valorTarifaPostoPassagems;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTarifaPostoPassagem",
				getIdTarifaPostoPassagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TarifaPostoPassagem))
			return false;
        TarifaPostoPassagem castOther = (TarifaPostoPassagem) other;
		return new EqualsBuilder().append(this.getIdTarifaPostoPassagem(),
				castOther.getIdTarifaPostoPassagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTarifaPostoPassagem())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
