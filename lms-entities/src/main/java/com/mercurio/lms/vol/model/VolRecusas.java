package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolRecusas implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRecusa;

    /** nullable persistent field */
    private String contato;

    /** nullable persistent field */
    private DateTime dhEnvio;

    /** nullable persistent field */
    private String obResolucao;

    /** nullable persistent field */
    private DateTime dhResolucao;

    /** nullable persistent field */
    private String obTratativa;

    /** nullable persistent field */
    private String obEnvio;

    /** nullable persistent field */
    private DateTime dhTratativa;

    /** persistent field */
    private DateTime dhRecusa;

    /** persistent field */
    private DomainValue tpRecusa;

    /** persistent field */
    private com.mercurio.lms.vol.model.VolEmailsRecusa volEmailsRecusa;

    /** persistent field */
    private Usuario usuarioByIdEnviou;

    /** persistent field */
    private Usuario usuarioByIdUsuario;

    /** persistent field */
    private Usuario usuarioByIdResolucao;
    
    /** persistent field */
    private ManifestoEntregaDocumento manifestoEntregaDocumento;

    /** persistent field */
    private List volEmailsRecusas;
    
    /** persistent field */
    private Filial filial;
    
    /** persistent field */
    private OcorrenciaEntrega ocorrenciaEntrega;
    
    public Long getIdRecusa() {
        return this.idRecusa;
    }

    public void setIdRecusa(Long idRecusa) {
        this.idRecusa = idRecusa;
    }

    public String getContato() {
        return this.contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public DateTime getDhEnvio() {
        return this.dhEnvio;
    }

    public void setDhEnvio(DateTime dhEnvio) {
        this.dhEnvio = dhEnvio;
    }

    public String getObResolucao() {
        return this.obResolucao;
    }

    public void setObResolucao(String obResolucao) {
        this.obResolucao = obResolucao;
    }

    public DateTime getDhResolucao() {
        return this.dhResolucao;
    }

    public void setDhResolucao(DateTime dhResolucao) {
        this.dhResolucao = dhResolucao;
    }

    public String getObTratativa() {
        return this.obTratativa;
    }

    public void setObTratativa(String obTratativa) {
        this.obTratativa = obTratativa;
    }

    public String getObEnvio() {
        return this.obEnvio;
    }

    public void setObEnvio(String obEnvio) {
        this.obEnvio = obEnvio;
    }

    public DateTime getDhTratativa() {
        return this.dhTratativa;
    }

    public void setDhTratativa(DateTime dhTratativa) {
        this.dhTratativa = dhTratativa;
    }

    public DateTime getDhRecusa() {
        return this.dhRecusa;
    }

    public void setDhRecusa(DateTime dhRecusa) {
        this.dhRecusa = dhRecusa;
    }

    public DomainValue getTpRecusa() {
        return this.tpRecusa;
    }

    public void setTpRecusa(DomainValue tpRecusa) {
        this.tpRecusa = tpRecusa;
    }

    public VolEmailsRecusa getVolEmailsRecusa() {
        return this.volEmailsRecusa;
    }

    public void setVolEmailsRecusa(VolEmailsRecusa volEmailsRecusa) {
        this.volEmailsRecusa = volEmailsRecusa;
    }

    public ManifestoEntregaDocumento getManifestoEntregaDocumento() {
        return this.manifestoEntregaDocumento;
    }

	public void setManifestoEntregaDocumento(
			ManifestoEntregaDocumento manifestoEntregaDocumento) {
        this.manifestoEntregaDocumento = manifestoEntregaDocumento;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolEmailsRecusa.class)     
    public List getVolEmailsRecusas() {
        return this.volEmailsRecusas;
    }

    public void setVolEmailsRecusas(List volEmailsRecusas) {
        this.volEmailsRecusas = volEmailsRecusas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRecusa", getIdRecusa())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolRecusas))
			return false;
        VolRecusas castOther = (VolRecusas) other;
		return new EqualsBuilder().append(this.getIdRecusa(),
				castOther.getIdRecusa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRecusa()).toHashCode();
    }

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

	public OcorrenciaEntrega getOcorrenciaEntrega() {
		return ocorrenciaEntrega;
	}

	public void setOcorrenciaEntrega(OcorrenciaEntrega ocorrenciaEntrega) {
		this.ocorrenciaEntrega = ocorrenciaEntrega;
	}

	public Usuario getUsuarioByIdEnviou() {
		return usuarioByIdEnviou;
	}

	public void setUsuarioByIdEnviou(Usuario usuarioByIdEnviou) {
		this.usuarioByIdEnviou = usuarioByIdEnviou;
	}

	public Usuario getUsuarioByIdResolucao() {
		return usuarioByIdResolucao;
	}

	public void setUsuarioByIdResolucao(Usuario usuarioByIdResolucao) {
		this.usuarioByIdResolucao = usuarioByIdResolucao;
	}

	public Usuario getUsuarioByIdUsuario() {
		return usuarioByIdUsuario;
	}

	public void setUsuarioByIdUsuario(Usuario usuarioByIdUsuario) {
		this.usuarioByIdUsuario = usuarioByIdUsuario;
	}

}
