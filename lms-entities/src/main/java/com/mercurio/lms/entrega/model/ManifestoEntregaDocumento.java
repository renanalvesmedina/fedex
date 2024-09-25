package com.mercurio.lms.entrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ManifestoEntregaDocumento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idManifestoEntregaDocumento;

    /** persistent field */
    private DateTime dhOcorrencia;
    
    /** persistent field */
    private DateTime dhBaixa;

    /** persistent field */
    private DomainValue tpFormaBaixa;

    /** persistent field */
    private DomainValue tpSituacaoDocumento;
    
    /** nullable persistent field */
    private String nmRecebedor;
    
    /** nullable persistent field */
    private String nmRgRecebedor;

    /** nullable persistent field */
    private DomainValue tpDocumentoCobranca;

    /** nullable persistent field */
    private DomainValue tpEntregaParcial;
    
    /** nullable persistent field */
    private String obAlteracao;

    /** nullable persistent field */
    private String obMotivoAlteracao;

    /** nullable persistent field */
    private String obManifestoEntregaDocumento;
    
    /** persistent field */
    private DateTime dhInclusao;
    
    private Boolean blRetencaoComprovanteEnt;
        
    /** persistent field */
    private com.mercurio.lms.entrega.model.OcorrenciaEntrega ocorrenciaEntrega;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** persistent field */
    private com.mercurio.lms.entrega.model.ManifestoEntrega manifestoEntrega;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento;

    /** persistent field */
    private List volRecusas;
    
    private com.mercurio.lms.expedicao.model.Awb awb;
    
    private DomainValue tpGrauRecebedor;
    
    public Long getIdManifestoEntregaDocumento() {
        return this.idManifestoEntregaDocumento;
    }

    public void setIdManifestoEntregaDocumento(Long idManifestoEntregaDocumento) {
        this.idManifestoEntregaDocumento = idManifestoEntregaDocumento;
    }

    public DateTime getDhOcorrencia() {
        return this.dhOcorrencia;
    }

    public void setDhOcorrencia(DateTime dhOcorrencia) {
        this.dhOcorrencia = dhOcorrencia;
    }

    public DomainValue getTpFormaBaixa() {
        return this.tpFormaBaixa;
    }

    public void setTpFormaBaixa(DomainValue tpFormaBaixa) {
        this.tpFormaBaixa = tpFormaBaixa;
    }

    public String getNmRecebedor() {
        return this.nmRecebedor;
    }

    public void setNmRecebedor(String nmRecebedor) {
        this.nmRecebedor = nmRecebedor;
    }

    public DomainValue getTpDocumentoCobranca() {
        return this.tpDocumentoCobranca;
    }

    public void setTpDocumentoCobranca(DomainValue tpDocumentoCobranca) {
        this.tpDocumentoCobranca = tpDocumentoCobranca;
    }

    public DomainValue getTpEntregaParcial() {
		return tpEntregaParcial;
	}

	public void setTpEntregaParcial(DomainValue tpEntregaParcial) {
		this.tpEntregaParcial = tpEntregaParcial;
	}

    public String getObAlteracao() {
        return this.obAlteracao;
    }

    public void setObAlteracao(String obAlteracao) {
        this.obAlteracao = obAlteracao;
    }

    public String getObMotivoAlteracao() {
        return this.obMotivoAlteracao;
    }

    public void setObMotivoAlteracao(String obMotivoAlteracao) {
        this.obMotivoAlteracao = obMotivoAlteracao;
    }

    public String getObManifestoEntregaDocumento() {
        return this.obManifestoEntregaDocumento;
    }

	public void setObManifestoEntregaDocumento(
			String obManifestoEntregaDocumento) {
        this.obManifestoEntregaDocumento = obManifestoEntregaDocumento;
    }

    public DateTime getDhInclusao() {
        return this.dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }
    
    public com.mercurio.lms.entrega.model.OcorrenciaEntrega getOcorrenciaEntrega() {
        return this.ocorrenciaEntrega;
    }

	public void setOcorrenciaEntrega(
			com.mercurio.lms.entrega.model.OcorrenciaEntrega ocorrenciaEntrega) {
        this.ocorrenciaEntrega = ocorrenciaEntrega;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public com.mercurio.lms.entrega.model.ManifestoEntrega getManifestoEntrega() {
        return this.manifestoEntrega;
    }

	public void setManifestoEntrega(
			com.mercurio.lms.entrega.model.ManifestoEntrega manifestoEntrega) {
        this.manifestoEntrega = manifestoEntrega;
    }

    public com.mercurio.lms.expedicao.model.NotaFiscalConhecimento getNotaFiscalConhecimento() {
        return this.notaFiscalConhecimento;
    }

	public void setNotaFiscalConhecimento(
			com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento) {
        this.notaFiscalConhecimento = notaFiscalConhecimento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idManifestoEntregaDocumento",
				getIdManifestoEntregaDocumento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ManifestoEntregaDocumento))
			return false;
        ManifestoEntregaDocumento castOther = (ManifestoEntregaDocumento) other;
		return new EqualsBuilder().append(
				this.getIdManifestoEntregaDocumento(),
				castOther.getIdManifestoEntregaDocumento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdManifestoEntregaDocumento())
            .toHashCode();
    }

	/**
	 * @return Returns the tpSituacaoDocumento.
	 */
	public DomainValue getTpSituacaoDocumento() {
		return tpSituacaoDocumento;
	}

	/**
	 * @param tpSituacaoDocumento
	 *            The tpSituacaoDocumento to set.
	 */
	public void setTpSituacaoDocumento(DomainValue tpSituacaoDocumento) {
		this.tpSituacaoDocumento = tpSituacaoDocumento;
	}

	public List getVolRecusas() {
		return volRecusas;
	}

	public void setVolRecusas(List volRecusas) {
		this.volRecusas = volRecusas;
	}

	public DateTime getDhBaixa() {
		return dhBaixa;
	}

	public void setDhBaixa(DateTime dhBaixa) {
		this.dhBaixa = dhBaixa;
	}

	public Boolean getBlRetencaoComprovanteEnt() {
		return blRetencaoComprovanteEnt;
	}

	public void setBlRetencaoComprovanteEnt(Boolean blRetencaoComprovanteEnt) {
		this.blRetencaoComprovanteEnt = blRetencaoComprovanteEnt;
	}

	public com.mercurio.lms.expedicao.model.Awb getAwb() {
		return awb;
	}

	public void setAwb(com.mercurio.lms.expedicao.model.Awb awb) {
		this.awb = awb;
	}

	public String getNmRgRecebedor() {
		return nmRgRecebedor;
	}

	public void setNmRgRecebedor(String nmRgRecebedor) {
		this.nmRgRecebedor = nmRgRecebedor;
	}	

    public DomainValue getTpGrauRecebedor() {
        return tpGrauRecebedor;
    }

    public void setTpGrauRecebedor(DomainValue tpGrauRecebedor) {
        this.tpGrauRecebedor = tpGrauRecebedor;
    }
	
}
