package com.mercurio.lms.sim.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class LocalizacaoMercadoria implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLocalizacaoMercadoria;

    /** persistent field */
    private VarcharI18n  dsLocalizacaoMercadoria;

    /** persistent field */
    private Short cdLocalizacaoMercadoria;

    /** persistent field */
    private Boolean blArmazenagem;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List eventos;

    private FaseProcesso faseProcesso;

    public LocalizacaoMercadoria() {
    }

    public LocalizacaoMercadoria(Long idLocalizacaoMercadoria, VarcharI18n dsLocalizacaoMercadoria, Short cdLocalizacaoMercadoria, Boolean blArmazenagem, DomainValue tpSituacao, List eventos, FaseProcesso faseProcesso) {
        this.idLocalizacaoMercadoria = idLocalizacaoMercadoria;
        this.dsLocalizacaoMercadoria = dsLocalizacaoMercadoria;
        this.cdLocalizacaoMercadoria = cdLocalizacaoMercadoria;
        this.blArmazenagem = blArmazenagem;
        this.tpSituacao = tpSituacao;
        this.eventos = eventos;
        this.faseProcesso = faseProcesso;
    }

    public Long getIdLocalizacaoMercadoria() {
        return this.idLocalizacaoMercadoria;
    }

    public void setIdLocalizacaoMercadoria(Long idLocalizacaoMercadoria) {
        this.idLocalizacaoMercadoria = idLocalizacaoMercadoria;
    }

    public VarcharI18n getDsLocalizacaoMercadoria() {
		return dsLocalizacaoMercadoria;
    }

	public void setDsLocalizacaoMercadoria(VarcharI18n dsLocalizacaoMercadoria) {
        this.dsLocalizacaoMercadoria = dsLocalizacaoMercadoria;
    }

    public Short getCdLocalizacaoMercadoria() {
        return this.cdLocalizacaoMercadoria;
    }

    public void setCdLocalizacaoMercadoria(Short cdLocalizacaoMercadoria) {
        this.cdLocalizacaoMercadoria = cdLocalizacaoMercadoria;
    }

    public Boolean getBlArmazenagem() {
        return this.blArmazenagem;
    }

    public void setBlArmazenagem(Boolean blArmazenagem) {
        this.blArmazenagem = blArmazenagem;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sim.model.Evento.class)     
    public List getEventos() {
        return this.eventos;
    }

    public void setEventos(List eventos) {
        this.eventos = eventos;
    }

    public FaseProcesso getFaseProcesso() {
		return faseProcesso;
	}

	public void setFaseProcesso(FaseProcesso faseProcesso) {
		this.faseProcesso = faseProcesso;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idLocalizacaoMercadoria",
				getIdLocalizacaoMercadoria()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LocalizacaoMercadoria))
			return false;
        LocalizacaoMercadoria castOther = (LocalizacaoMercadoria) other;
		return new EqualsBuilder().append(this.getIdLocalizacaoMercadoria(),
				castOther.getIdLocalizacaoMercadoria()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLocalizacaoMercadoria())
            .toHashCode();
    }

    public String getLocalizacaoMercadoriaConcatenado() {
		if (this.cdLocalizacaoMercadoria != null
				&& this.dsLocalizacaoMercadoria != null)
			return this.cdLocalizacaoMercadoria.toString()
					+ " - "
					+ this.dsLocalizacaoMercadoria.getValue(LocaleContextHolder
							.getLocale());
    	else
    		return "";
    }
}
