package com.mercurio.lms.entrega.model;

import java.io.Serializable;

import com.mercurio.lms.configuracoes.model.Usuario;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;

/** @author LMS Custom Hibernate CodeGenerator */
public class ManifestoEntregaVolume implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idManifestoEntregaVolume;

    /** persistent field */
    private DateTime dhOcorrencia;

    /** persistent field */
    private DateTime dhInclusao;
    
    /** persistent field */
    private ManifestoEntrega manifestoEntrega;
    
    /** persistent field */
    private OcorrenciaEntrega ocorrenciaEntrega;

    /** persistent field */
    private VolumeNotaFiscal volumeNotaFiscal;

    /** persistent field */
    private ManifestoEntregaDocumento manifestoEntregaDocumento;
    
    /** persistent field */
    private DoctoServico doctoServico;
    
    /** persistent field */
    private DomainValue tpFormaBaixa;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

	public ManifestoEntregaVolume() {
	}

	public ManifestoEntregaVolume(Long idManifestoEntregaVolume, DateTime dhOcorrencia, DateTime dhInclusao, ManifestoEntrega manifestoEntrega, OcorrenciaEntrega ocorrenciaEntrega, VolumeNotaFiscal volumeNotaFiscal, ManifestoEntregaDocumento manifestoEntregaDocumento, DoctoServico doctoServico, DomainValue tpFormaBaixa, Usuario usuario) {
		this.idManifestoEntregaVolume = idManifestoEntregaVolume;
		this.dhOcorrencia = dhOcorrencia;
		this.dhInclusao = dhInclusao;
		this.manifestoEntrega = manifestoEntrega;
		this.ocorrenciaEntrega = ocorrenciaEntrega;
		this.volumeNotaFiscal = volumeNotaFiscal;
		this.manifestoEntregaDocumento = manifestoEntregaDocumento;
		this.doctoServico = doctoServico;
		this.tpFormaBaixa = tpFormaBaixa;
		this.usuario = usuario;
	}

	public Long getIdManifestoEntregaVolume() {
		return idManifestoEntregaVolume;
	}

	public void setIdManifestoEntregaVolume(Long idManifestoEntregaVolume) {
		this.idManifestoEntregaVolume = idManifestoEntregaVolume;
	}

	public DateTime getDhOcorrencia() {
		return dhOcorrencia;
	}

	public void setDhOcorrencia(DateTime dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}

	public ManifestoEntrega getManifestoEntrega() {
		return manifestoEntrega;
	}

	public void setManifestoEntrega(ManifestoEntrega manifestoEntrega) {
		this.manifestoEntrega = manifestoEntrega;
	}

	public OcorrenciaEntrega getOcorrenciaEntrega() {
		return ocorrenciaEntrega;
	}

	public void setOcorrenciaEntrega(OcorrenciaEntrega ocorrenciaEntrega) {
		this.ocorrenciaEntrega = ocorrenciaEntrega;
	}

	public VolumeNotaFiscal getVolumeNotaFiscal() {
		return volumeNotaFiscal;
	}

	public void setVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal) {
		this.volumeNotaFiscal = volumeNotaFiscal;
	}

	public ManifestoEntregaDocumento getManifestoEntregaDocumento() {
		return manifestoEntregaDocumento;
}

	public void setManifestoEntregaDocumento(
			ManifestoEntregaDocumento manifestoEntregaDocumento) {
		this.manifestoEntregaDocumento = manifestoEntregaDocumento;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setTpFormaBaixa(DomainValue tpFormaBaixa) {
		this.tpFormaBaixa = tpFormaBaixa;
	}

	public DomainValue getTpFormaBaixa() {
		return tpFormaBaixa;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}	
}
