package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class AliquotaIssMunicipioServ implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAliquotaIssMunicipioServ;

    /** persistent field */
    private BigDecimal pcAliquota;

    /** persistent field */
    private BigDecimal pcEmbute;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private Boolean blEmiteNfServico;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    /** nullable persistent field */
    private Boolean blRetencaoTomadorServico;

    /** persistent field */
    private com.mercurio.lms.tributos.model.IssMunicipioServico issMunicipioServico;

    /** persistent field */
    private String obAliIssMunServ;

    public Long getIdAliquotaIssMunicipioServ() {
        return this.idAliquotaIssMunicipioServ;
    }

    public void setIdAliquotaIssMunicipioServ(Long idAliquotaIssMunicipioServ) {
        this.idAliquotaIssMunicipioServ = idAliquotaIssMunicipioServ;
    }

    public BigDecimal getPcAliquota() {
        return this.pcAliquota;
    }

    public void setPcAliquota(BigDecimal pcAliquota) {
        this.pcAliquota = pcAliquota;
    }

    public BigDecimal getPcEmbute() {
        return this.pcEmbute;
    }

    public void setPcEmbute(BigDecimal pcEmbute) {
        this.pcEmbute = pcEmbute;
    }

    public Boolean getBlEmiteNfServico() {
        return this.blEmiteNfServico;
    }

    public void setBlEmiteNfServico(Boolean blEmiteNfServico) {
        this.blEmiteNfServico = blEmiteNfServico;
    }
    
    public com.mercurio.lms.tributos.model.IssMunicipioServico getIssMunicipioServico() {
        return this.issMunicipioServico;
    }

	public void setIssMunicipioServico(
			com.mercurio.lms.tributos.model.IssMunicipioServico issMunicipioServico) {
        this.issMunicipioServico = issMunicipioServico;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAliquotaIssMunicipioServ",
				getIdAliquotaIssMunicipioServ()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaIssMunicipioServ))
			return false;
        AliquotaIssMunicipioServ castOther = (AliquotaIssMunicipioServ) other;
		return new EqualsBuilder().append(this.getIdAliquotaIssMunicipioServ(),
				castOther.getIdAliquotaIssMunicipioServ()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaIssMunicipioServ())
            .toHashCode();
    }

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

    public Boolean getBlRetencaoTomadorServico() {
        return blRetencaoTomadorServico;
    }

    public void setBlRetencaoTomadorServico(Boolean blRetencaoTomadorServico) {
        this.blRetencaoTomadorServico = blRetencaoTomadorServico;
    }

	public String getObAliIssMunServ() {
		return obAliIssMunServ;
	}

	public void setObAliIssMunServ(String obAliIssMunServ) {
		this.obAliIssMunServ = obAliIssMunServ;
	}

}