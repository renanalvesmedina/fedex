package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;

/** @author LMS Custom Hibernate CodeGenerator */
public class ManifestoNacionalVolume implements Serializable {

	private static final long serialVersionUID = 1L;

	/** persistent field */
	private Long idManifestoNacionalVolume;

	/** identifier field */
	private VolumeNotaFiscal volumeNotaFiscal;

	/** persistent field */
	private ManifestoViagemNacional manifestoViagemNacional;

	private Conhecimento conhecimento;
	
	private ManifestoNacionalCto manifestoNacionalCto;

	public ManifestoNacionalVolume() {
	}

	public ManifestoNacionalVolume(Long idManifestoNacionalVolume, VolumeNotaFiscal volumeNotaFiscal, ManifestoViagemNacional manifestoViagemNacional, Conhecimento conhecimento, ManifestoNacionalCto manifestoNacionalCto) {
		this.idManifestoNacionalVolume = idManifestoNacionalVolume;
		this.volumeNotaFiscal = volumeNotaFiscal;
		this.manifestoViagemNacional = manifestoViagemNacional;
		this.conhecimento = conhecimento;
		this.manifestoNacionalCto = manifestoNacionalCto;
	}

	public Long getIdManifestoNacionalVolume() {
		return idManifestoNacionalVolume;
	}

	public void setIdManifestoNacionalVolume(Long idManifestoNacionalVolume) {
		this.idManifestoNacionalVolume = idManifestoNacionalVolume;
	}

	public VolumeNotaFiscal getVolumeNotaFiscal() {
		return volumeNotaFiscal;
	}

	public void setVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal) {
		this.volumeNotaFiscal = volumeNotaFiscal;
	}

	public ManifestoViagemNacional getManifestoViagemNacional() {
		return manifestoViagemNacional;
	}

	public void setManifestoViagemNacional(
			ManifestoViagemNacional manifestoViagemNacional) {
		this.manifestoViagemNacional = manifestoViagemNacional;
	}

	public Conhecimento getConhecimento() {
		return conhecimento;
	}
	
	public void setConhecimento(Conhecimento conhecimento) {
		this.conhecimento = conhecimento;
}

	public ManifestoNacionalCto getManifestoNacionalCto() {
		return manifestoNacionalCto;
	}

	public void setManifestoNacionalCto(
			ManifestoNacionalCto manifestoNacionalCto) {
		this.manifestoNacionalCto = manifestoNacionalCto;
	}
}
