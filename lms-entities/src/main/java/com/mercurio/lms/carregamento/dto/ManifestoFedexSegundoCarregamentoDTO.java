package com.mercurio.lms.carregamento.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author ernani.brandao
 * LMSA-6520
 */
public class ManifestoFedexSegundoCarregamentoDTO implements Serializable {

	private static final long serialVersionUID = 7937035064238276237L;
	
	private String numeroChave;
	private List<Integer> notasEDI;
	private List<String> conhecimentosFedex;
	
	public ManifestoFedexSegundoCarregamentoDTO() {
	}
	
	public ManifestoFedexSegundoCarregamentoDTO(String numeroChave) {
		this();
		this.numeroChave = numeroChave;
	}
	
	public ManifestoFedexSegundoCarregamentoDTO(String numeroChave, List<Integer> notasEDI, List<String> conhecimentosFedex) {
		this(numeroChave);
		this.notasEDI = notasEDI;
		this.conhecimentosFedex = conhecimentosFedex;
	}
	
	public String getNumeroChave() {
		return numeroChave;
	}
	public void setNumeroChave(String numeroChave) {
		this.numeroChave = numeroChave;
	}
	public List<Integer> getNotasEDI() {
		return notasEDI;
	}
	public void setNotasEDI(List<Integer> notasEDI) {
		this.notasEDI = notasEDI;
	}
	public List<String> getConhecimentosFedex() {
		return conhecimentosFedex;
	}
	public void setConhecimentosFedex(List<String> conhecimentosFedex) {
		this.conhecimentosFedex = conhecimentosFedex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroChave == null) ? 0 : numeroChave.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManifestoFedexSegundoCarregamentoDTO other = (ManifestoFedexSegundoCarregamentoDTO) obj;
		if (numeroChave == null) {
			if (other.numeroChave != null)
				return false;
		} else if (!numeroChave.equals(other.numeroChave))
			return false;
		return true;
	}
	
}
