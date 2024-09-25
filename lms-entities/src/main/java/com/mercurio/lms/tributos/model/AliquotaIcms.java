package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class AliquotaIcms implements Serializable {

	private static final long serialVersionUID = 2L;

    /** identifier field */
    private Long idAliquotaIcms;

    /** persistent field */
    private BigDecimal pcAliquota;

    /** persistent field */
    private BigDecimal pcEmbute;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private DomainValue tpSituacaoTribRemetente;

    /** persistent field */
    private DomainValue tpSituacaoTribDestinatario;

    /** persistent field */
    private DomainValue tpTipoFrete;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    private String obAliquota;

    /** persistent field */
    private com.mercurio.lms.tributos.model.TipoTributacaoIcms tipoTributacaoIcms;

    /** persistent field */
    private com.mercurio.lms.tributos.model.EmbasamentoLegalIcms embasamento;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RegiaoGeografica regiaoGeografica;

    public Long getIdAliquotaIcms() {
        return this.idAliquotaIcms;
    }

    public void setIdAliquotaIcms(Long idAliquotaIcms) {
        this.idAliquotaIcms = idAliquotaIcms;
    }

    public BigDecimal getPcEmbute() {
    	return pcEmbute;
    }
    
    public void setPcEmbute(BigDecimal pcEmbute) {
    	this.pcEmbute = pcEmbute;
    }

    public BigDecimal getPcAliquota() {
        return this.pcAliquota;
    }

    public void setPcAliquota(BigDecimal pcAliquota) {
        this.pcAliquota = pcAliquota;
    }

    /* Alterado por causa da demanda LMS-1048*/
    public BigDecimal pcEmbuteCalculado() {    	
    	final BigDecimal HUNDRED = new BigDecimal(100);
    	final BigDecimal ONE = BigDecimal.ONE;
		return HUNDRED.divide(ONE.subtract(pcAliquota.divide(HUNDRED)), 10,
				RoundingMode.HALF_UP).subtract(HUNDRED);
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtInicioVigencia) {
        this.dtVigenciaInicial = dtInicioVigencia;
    }

    public DomainValue getTpSituacaoTribRemetente() {
        return this.tpSituacaoTribRemetente;
    }

    public void setTpSituacaoTribRemetente(DomainValue tpSituacaoTribRemetente) {
        this.tpSituacaoTribRemetente = tpSituacaoTribRemetente;
    }

    public DomainValue getTpSituacaoTribDestinatario() {
        return this.tpSituacaoTribDestinatario;
    }

	public void setTpSituacaoTribDestinatario(
			DomainValue tpSituacaoTribDestinatario) {
        this.tpSituacaoTribDestinatario = tpSituacaoTribDestinatario;
    }

    public DomainValue getTpTipoFrete() {
        return this.tpTipoFrete;
    }

    public void setTpTipoFrete(DomainValue tpTipoFrete) {
        this.tpTipoFrete = tpTipoFrete;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtFimVigencia) {
        this.dtVigenciaFinal = dtFimVigencia;
    }

    public com.mercurio.lms.tributos.model.TipoTributacaoIcms getTipoTributacaoIcms() {
        return this.tipoTributacaoIcms;
    }

	public void setTipoTributacaoIcms(
			com.mercurio.lms.tributos.model.TipoTributacaoIcms tipoTributacaoIcms) {
        this.tipoTributacaoIcms = tipoTributacaoIcms;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaOrigem() {
        return this.unidadeFederativaOrigem;
    }

	public void setUnidadeFederativaOrigem(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaOrigem) {
        this.unidadeFederativaOrigem = unidadeFederativaOrigem;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaDestino() {
        return this.unidadeFederativaDestino;
    }

	public void setUnidadeFederativaDestino(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaDestino) {
        this.unidadeFederativaDestino = unidadeFederativaDestino;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAliquotaIcms",
				getIdAliquotaIcms()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaIcms))
			return false;
        AliquotaIcms castOther = (AliquotaIcms) other;
		return new EqualsBuilder().append(this.getIdAliquotaIcms(),
				castOther.getIdAliquotaIcms()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaIcms()).toHashCode();
    }

	public com.mercurio.lms.tributos.model.EmbasamentoLegalIcms getEmbasamento() {
		return embasamento;
}

	public void setEmbasamento(
			com.mercurio.lms.tributos.model.EmbasamentoLegalIcms embasamento) {
		this.embasamento = embasamento;
	}

	public com.mercurio.lms.municipios.model.RegiaoGeografica getRegiaoGeografica() {
		return regiaoGeografica;
	}

	public void setRegiaoGeografica(
			com.mercurio.lms.municipios.model.RegiaoGeografica regiaoGeografica) {
		this.regiaoGeografica = regiaoGeografica;
	}

	public String getObAliquota() {
		return obAliquota;
	}

	public void setObAliquota(String obAliquota) {
		this.obAliquota = obAliquota;
	}

}
