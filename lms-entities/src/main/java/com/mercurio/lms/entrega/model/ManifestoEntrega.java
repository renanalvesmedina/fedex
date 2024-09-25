package com.mercurio.lms.entrega.model;

import java.io.Serializable;
import java.util.List;

import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.configuracoes.model.Setor;
import com.mercurio.lms.municipios.model.Filial;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;

/** @author LMS Custom Hibernate CodeGenerator */
public class ManifestoEntrega implements Serializable{

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idManifestoEntrega;

    /** persistent field */
    private Integer nrManifestoEntrega;

    /** persistent field */
    private DateTime dhEmissao;

     /** nullable persistent field */
    private String obManifestoEntrega;

    /** persistent field */
    private DateTime dhFechamento;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Setor setor;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    private com.mercurio.lms.carregamento.model.Manifesto manifesto;
    
    /** persistent field */
    private Usuario usuarioFechamento;

    /** persistent field */
    private List manifestoEntregaDocumentos;

    /** persistent field */
    private List manifestoEntregaVolumes;
    
    /** persistent field */
    private List faturas;

    private List<NotaCreditoDocto> notaCreditoDoctos;

    public ManifestoEntrega() {
    }

    public ManifestoEntrega(Long idManifestoEntrega, Integer nrManifestoEntrega, DateTime dhEmissao, String obManifestoEntrega, DateTime dhFechamento, Setor setor, Filial filial, Manifesto manifesto, Usuario usuarioFechamento, List manifestoEntregaDocumentos, List manifestoEntregaVolumes, List faturas, List<NotaCreditoDocto> notaCreditoDoctos) {
        this.idManifestoEntrega = idManifestoEntrega;
        this.nrManifestoEntrega = nrManifestoEntrega;
        this.dhEmissao = dhEmissao;
        this.obManifestoEntrega = obManifestoEntrega;
        this.dhFechamento = dhFechamento;
        this.setor = setor;
        this.filial = filial;
        this.manifesto = manifesto;
        this.usuarioFechamento = usuarioFechamento;
        this.manifestoEntregaDocumentos = manifestoEntregaDocumentos;
        this.manifestoEntregaVolumes = manifestoEntregaVolumes;
        this.faturas = faturas;
        this.notaCreditoDoctos = notaCreditoDoctos;
    }


    public Long getIdManifestoEntrega() {
        return this.idManifestoEntrega;
    }

    public void setIdManifestoEntrega(Long idManifestoEntrega) {
        this.idManifestoEntrega = idManifestoEntrega;
    }

    public Integer getNrManifestoEntrega() {
        return this.nrManifestoEntrega;
    }

    public void setNrManifestoEntrega(Integer nrManifestoEntrega) {
        this.nrManifestoEntrega = nrManifestoEntrega;
    }

    public DateTime getDhEmissao() {
        return this.dhEmissao;
    }

    public void setDhEmissao(DateTime dhEmissao) {
        this.dhEmissao = dhEmissao;
    }

    public String getObManifestoEntrega() {
        return this.obManifestoEntrega;
    }

    public void setObManifestoEntrega(String obManifestoEntrega) {
        this.obManifestoEntrega = obManifestoEntrega;
    }

    public com.mercurio.lms.configuracoes.model.Setor getSetor() {
        return this.setor;
    }

    public void setSetor(com.mercurio.lms.configuracoes.model.Setor setor) {
        this.setor = setor;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.ManifestoEntregaDocumento.class)     
    public List<ManifestoEntregaDocumento> getManifestoEntregaDocumentos() {
        return this.manifestoEntregaDocumentos;
    }

    public void setManifestoEntregaDocumentos(List manifestoEntregaDocumentos) {
        this.manifestoEntregaDocumentos = manifestoEntregaDocumentos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.ManifestoEntregaVolume.class)
    public List getManifestoEntregaVolumes() {
		return manifestoEntregaVolumes;
	}

	public void setManifestoEntregaVolumes(List manifestoEntregaVolumes) {
		this.manifestoEntregaVolumes = manifestoEntregaVolumes;
	}

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Fatura.class)     
    public List getFaturas() {
        return this.faturas;
    }

    public void setFaturas(List faturas) {
        this.faturas = faturas;
    }

    public List<NotaCreditoDocto> getNotaCreditoDoctos() {
        return notaCreditoDoctos;
    }

    public void setNotaCreditoDoctos(List<NotaCreditoDocto> notaCreditoDoctos) {
        this.notaCreditoDoctos = notaCreditoDoctos;
    }

    @Override
    public String toString() {
		return new ToStringBuilder(this).append("idManifestoEntrega",
				getIdManifestoEntrega()).toString();
    }

    @Override
    public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof ManifestoEntrega))
			return false;
        ManifestoEntrega castOther = (ManifestoEntrega) other;
		return new EqualsBuilder().append(this.getIdManifestoEntrega(),
				castOther.getIdManifestoEntrega()).isEquals();
    }

    @Override
    public int hashCode() {
		return new HashCodeBuilder().append(getIdManifestoEntrega())
            .toHashCode();
    }

	/**
	 * @return Returns the dhFechamento.
	 */
	public DateTime getDhFechamento() {
		return dhFechamento;
	}

	/**
	 * @param dhFechamento
	 *            The dhFechamento to set.
	 */
	public void setDhFechamento(DateTime dhFechamento) {
		this.dhFechamento = dhFechamento;
	}

	public Usuario getUsuarioFechamento() {
		return usuarioFechamento;
	}

	public void setUsuarioFechamento(Usuario usuarioFechamento) {
		this.usuarioFechamento = usuarioFechamento;
	}

	public com.mercurio.lms.carregamento.model.Manifesto getManifesto() {
		return manifesto;
	}

	public void setManifesto(
			com.mercurio.lms.carregamento.model.Manifesto manifesto) {
		this.manifesto = manifesto;
	}

}
