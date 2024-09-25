package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.MoedaPais;

/** @author LMS Custom Hibernate CodeGenerator */
public class ValorTarifaPostoPassagem implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idValorTarifaPostoPassagem;

    private Integer versao;
  
    /** persistent field */
    private BigDecimal vlTarifa;

    /** nullable persistent field */
    private TimeOfDay hrInicial;

    /** nullable persistent field */
    private TimeOfDay hrFinal;

    private Integer qtEixos; 
    
    /** persistent field */
    private DomainValue diaSemana;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TarifaPostoPassagem tarifaPostoPassagem;

    /** persistent field */
    private MoedaPais moedaPais;
    
    public Long getIdValorTarifaPostoPassagem() {
        return this.idValorTarifaPostoPassagem;
    }

    public void setIdValorTarifaPostoPassagem(Long idValorTarifaPostoPassagem) {
        this.idValorTarifaPostoPassagem = idValorTarifaPostoPassagem;
    }

    public BigDecimal getVlTarifa() {
        return this.vlTarifa;
    }

    public void setVlTarifa(BigDecimal vlTarifa) {
        this.vlTarifa = vlTarifa;
    }

    public TimeOfDay getHrInicial() {
        return this.hrInicial;
    }

    public void setHrInicial(TimeOfDay hrInicial) {
        this.hrInicial = hrInicial;
    }

    public TimeOfDay getHrFinal() {
        return this.hrFinal;
    }

    public void setHrFinal(TimeOfDay hrFinal) {
        this.hrFinal = hrFinal;
    }
    
    public Integer getQtEixos() {
		return qtEixos;
	}

	public void setQtEixos(Integer qtEixos) {
		this.qtEixos = qtEixos;
	}

	public DomainValue getDiaSemana() {
        return this.diaSemana;
    }

    public void setDiaSemana(DomainValue diaSemana) {
        this.diaSemana = diaSemana;
    }

	public com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

	public void setTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    public com.mercurio.lms.municipios.model.TarifaPostoPassagem getTarifaPostoPassagem() {
        return this.tarifaPostoPassagem;
    }

	public void setTarifaPostoPassagem(
			com.mercurio.lms.municipios.model.TarifaPostoPassagem tarifaPostoPassagem) {
        this.tarifaPostoPassagem = tarifaPostoPassagem;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idValorTarifaPostoPassagem",
				getIdValorTarifaPostoPassagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorTarifaPostoPassagem))
			return false;
        ValorTarifaPostoPassagem castOther = (ValorTarifaPostoPassagem) other;
		return new EqualsBuilder().append(this.getIdValorTarifaPostoPassagem(),
				castOther.getIdValorTarifaPostoPassagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorTarifaPostoPassagem())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public MoedaPais getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

}
