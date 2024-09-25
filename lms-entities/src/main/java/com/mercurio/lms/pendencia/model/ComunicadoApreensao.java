package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class ComunicadoApreensao implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idComunicadoApreensao;

    /** persistent field */
    private YearMonthDay dtOcorrencia;

    /** persistent field */
    private String nrTermoApreensao;

    /** persistent field */
    private String dsMotivoAlegado;

    /** persistent field */
    private BigDecimal vlMulta;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private List ocorrenciaDoctoServicos;

    public Long getIdComunicadoApreensao() {
        return this.idComunicadoApreensao;
    }

    public void setIdComunicadoApreensao(Long idComunicadoApreensao) {
        this.idComunicadoApreensao = idComunicadoApreensao;
    }

    public YearMonthDay getDtOcorrencia() {
        return this.dtOcorrencia;
    }

    public void setDtOcorrencia(YearMonthDay	 dtOcorrencia) {
        this.dtOcorrencia = dtOcorrencia;
    }

    public String getNrTermoApreensao() {
        return this.nrTermoApreensao;
    }

    public void setNrTermoApreensao(String nrTermoApreensao) {
        this.nrTermoApreensao = nrTermoApreensao;
    }

    public String getDsMotivoAlegado() {
        return this.dsMotivoAlegado;
    }

    public void setDsMotivoAlegado(String dsMotivoAlegado) {
        this.dsMotivoAlegado = dsMotivoAlegado;
    }

    public BigDecimal getVlMulta() {
        return this.vlMulta;
    }

    public void setVlMulta(BigDecimal vlMulta) {
        this.vlMulta = vlMulta;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico.class)     
    public List getOcorrenciaDoctoServicos() {
        return this.ocorrenciaDoctoServicos;
    }

	public void setOcorrenciaDoctoServicos(List ocorrenciaDoctoServicos) {
		this.ocorrenciaDoctoServicos = ocorrenciaDoctoServicos;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idComunicadoApreensao",
				getIdComunicadoApreensao()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ComunicadoApreensao))
			return false;
		ComunicadoApreensao castOther = (ComunicadoApreensao) other;
		return new EqualsBuilder().append(this.getIdComunicadoApreensao(),
				castOther.getIdComunicadoApreensao()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdComunicadoApreensao())
			.toHashCode();
}

	@Override
	public ComunicadoApreensao clone() {  
		ComunicadoApreensao clone = new ComunicadoApreensao();
		clone.setDsMotivoAlegado(dsMotivoAlegado);
		clone.setDtOcorrencia(dtOcorrencia);
		clone.setMoeda(moeda);
		clone.setNrTermoApreensao(nrTermoApreensao);
		clone.setOcorrenciaDoctoServicos(ocorrenciaDoctoServicos);
		clone.setVlMulta(vlMulta);
		return clone;
	}

	public boolean isValid() {
		return !StringUtils.isEmpty(nrTermoApreensao) 
				&& dtOcorrencia != null
				&& moeda != null
				&& vlMulta != null 
				&& !StringUtils.isEmpty(dsMotivoAlegado);
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(nrTermoApreensao) 
				&& dtOcorrencia == null
				&& moeda == null
				&& vlMulta == null 
				&& StringUtils.isEmpty(dsMotivoAlegado);
	}
}
