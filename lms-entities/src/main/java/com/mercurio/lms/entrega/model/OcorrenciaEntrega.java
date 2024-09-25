package com.mercurio.lms.entrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.sim.model.DescricaoEvento;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoVolume;

/** @author LMS Custom Hibernate CodeGenerator */
public class OcorrenciaEntrega implements Serializable {

	private static final long serialVersionUID = 1L;

	public OcorrenciaEntrega() {
	}
	
	public OcorrenciaEntrega(Long idOcorrenciaEntrega) {
		this.idOcorrenciaEntrega = idOcorrenciaEntrega;
	}
	
    /** identifier field */
    private Long idOcorrenciaEntrega;

    /** persistent field */
    private Short cdOcorrenciaEntrega;

    /** persistent field */
    private VarcharI18n  dsOcorrenciaEntrega;

    /** persistent field */
    private Boolean blDescontoDpe;

    /** persistent field */
    private DomainValue tpOcorrencia;

    /** persistent field */
    private Boolean blContabilizarEntrega;    
    
    /** persistent field */
    private Boolean blContabilizarTentativaEntrega;

    /** persistent field */
    private Boolean blOcasionadoMercurio;

    /** persistent field */
    private DomainValue tpSituacao;

    private DescricaoEvento descricaoEvento;

    /** persistent field */
    private Evento evento;

    /** persistent field */
    private OcorrenciaPendencia ocorrenciaPendencia;

    /** persistent field */
    private List manifestoEntregaDocumentos;
    
    /** persistent field */
    private List<EventoVolume> eventoVolumes;
    
    /** persistent field */
    private List volRecusas;
    
    public Long getIdOcorrenciaEntrega() {
        return this.idOcorrenciaEntrega;
    }

    public void setIdOcorrenciaEntrega(Long idOcorrenciaEntrega) {
        this.idOcorrenciaEntrega = idOcorrenciaEntrega;
    }

    public Short getCdOcorrenciaEntrega() {
        return this.cdOcorrenciaEntrega;
    }

    public void setCdOcorrenciaEntrega(Short cdOcorrenciaEntrega) {
        this.cdOcorrenciaEntrega = cdOcorrenciaEntrega;
    }

    public VarcharI18n getDsOcorrenciaEntrega() {
		return dsOcorrenciaEntrega;
    }

	public void setDsOcorrenciaEntrega(VarcharI18n dsOcorrenciaEntrega) {
        this.dsOcorrenciaEntrega = dsOcorrenciaEntrega;
    }

    public Boolean getBlDescontoDpe() {
        return this.blDescontoDpe;
    }

    public void setBlDescontoDpe(Boolean blDescontoDpe) {
        this.blDescontoDpe = blDescontoDpe;
    }

    public DomainValue getTpOcorrencia() {
        return this.tpOcorrencia;
    }

    public void setTpOcorrencia(DomainValue tpOcorrencia) {
        this.tpOcorrencia = tpOcorrencia;
    }

    public Boolean getBlOcasionadoMercurio() {
        return this.blOcasionadoMercurio;
    }

    public void setBlOcasionadoMercurio(Boolean blOcasionadoMercurio) {
        this.blOcasionadoMercurio = blOcasionadoMercurio;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.sim.model.Evento getEvento() {
        return this.evento;
    }

    public void setEvento(com.mercurio.lms.sim.model.Evento evento) {
        this.evento = evento;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.ManifestoEntregaDocumento.class)     
    public List getManifestoEntregaDocumentos() {
        return this.manifestoEntregaDocumentos;
    }

    public void setManifestoEntregaDocumentos(List manifestoEntregaDocumentos) {
        this.manifestoEntregaDocumentos = manifestoEntregaDocumentos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOcorrenciaEntrega",
				getIdOcorrenciaEntrega()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaEntrega))
			return false;
        OcorrenciaEntrega castOther = (OcorrenciaEntrega) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaEntrega(),
				castOther.getIdOcorrenciaEntrega()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaEntrega())
            .toHashCode();
    }

	public DescricaoEvento getDescricaoEvento() {
		return descricaoEvento;
	}

	public void setDescricaoEvento(DescricaoEvento descricaoEvento) {
		this.descricaoEvento = descricaoEvento;
	}

	public List getVolRecusas() {
		return volRecusas;
	}

	public void setVolRecusas(List volRecusas) {
		this.volRecusas = volRecusas;
	}

	public Boolean getBlContabilizarEntrega() {
		return blContabilizarEntrega;
}

	public void setBlContabilizarEntrega(Boolean blContabilizarEntrega) {
		this.blContabilizarEntrega = blContabilizarEntrega;
	}
	
	public Boolean getBlContabilizarTentativaEntrega() {
		return this.blContabilizarTentativaEntrega;
	}

	public void setBlContabilizarTentativaEntrega(Boolean blContabilizarTentativaEntrega) {
		this.blContabilizarTentativaEntrega = blContabilizarTentativaEntrega;
	}
	
	public void setEventoVolumes(List<EventoVolume> eventoVolumes) {
		this.eventoVolumes = eventoVolumes;
}

	public List<EventoVolume> getEventoVolumes() {
		return eventoVolumes;
	}

	public void setOcorrenciaPendencia(OcorrenciaPendencia ocorrenciaPendencia) {
		this.ocorrenciaPendencia = ocorrenciaPendencia;
}

	public OcorrenciaPendencia getOcorrenciaPendencia() {
		return ocorrenciaPendencia;
	}

}
