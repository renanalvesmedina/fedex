package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;

public class PreManifestoVolume implements Serializable {

	private static final long serialVersionUID = 1L;
   private Long idPreManifestoVolume;
   private VolumeNotaFiscal volumeNotaFiscal;
   private DoctoServico doctoServico;
   private PreManifestoDocumento preManifestoDocumento;
   private Manifesto manifesto;
   /* DM_TIPO_SCAN */
   private DomainValue tpScan;
   
   /**
    * atributo necessário quando é utilizado em DF2 
    */
   private Integer versao;

	public PreManifestoVolume() {
	}

	public PreManifestoVolume(Long idPreManifestoVolume, VolumeNotaFiscal volumeNotaFiscal, DoctoServico doctoServico, PreManifestoDocumento preManifestoDocumento, Manifesto manifesto, DomainValue tpScan, Integer versao) {
		this.idPreManifestoVolume = idPreManifestoVolume;
		this.volumeNotaFiscal = volumeNotaFiscal;
		this.doctoServico = doctoServico;
		this.preManifestoDocumento = preManifestoDocumento;
		this.manifesto = manifesto;
		this.tpScan = tpScan;
		this.versao = versao;
	}

	public Long getIdPreManifestoVolume() {
		return idPreManifestoVolume;
	}
	
	public void setIdPreManifestoVolume(Long idPreManifestoVolume) {
		this.idPreManifestoVolume = idPreManifestoVolume;
	}
	
	public VolumeNotaFiscal getVolumeNotaFiscal() {
		return volumeNotaFiscal;
	}
	
	public void setVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal) {
		this.volumeNotaFiscal = volumeNotaFiscal;
	}
	
	public DoctoServico getDoctoServico() {
		return doctoServico;
	}
	
	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}
	
	public PreManifestoDocumento getPreManifestoDocumento() {
		return preManifestoDocumento;
	}
	
	public void setPreManifestoDocumento(
			PreManifestoDocumento preManifestoDocumento) {
		this.preManifestoDocumento = preManifestoDocumento;
	}
	
	public Manifesto getManifesto() {
		return manifesto;
	}
	
	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}	

	public DomainValue getTpScan() {
		return tpScan;
	}

	public void setTpScan(DomainValue tpScan) {
		this.tpScan = tpScan;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idPreManifestoVolume",
				getIdPreManifestoVolume()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PreManifestoVolume))
			return false;
        PreManifestoVolume castOther = (PreManifestoVolume) other;
		return new EqualsBuilder().append(this.getIdPreManifestoVolume(),
				castOther.getIdPreManifestoVolume()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPreManifestoVolume())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
