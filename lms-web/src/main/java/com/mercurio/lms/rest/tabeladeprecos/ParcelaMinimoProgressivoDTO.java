package com.mercurio.lms.rest.tabeladeprecos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParcelaMinimoProgressivoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idParcela;
	private String nomeParcela;
	private Long idMarkupFaixaProgressiva;
	private List<MarkupValorFaixaProgressivaDTO> listValoresFaixaProgressiva;
	
	public ParcelaMinimoProgressivoDTO(){
	}
	
	public ParcelaMinimoProgressivoDTO(Long idMarkupFaixaProgressiva, Long idParcelaPreco, String nomeParcela) {
		this.idMarkupFaixaProgressiva = idMarkupFaixaProgressiva;
		this.idParcela = idParcelaPreco;
		this.nomeParcela = nomeParcela;
	}

	public Long getIdParcela() {
		return idParcela;
	}
	public void setIdParcela(Long idParcela) {
		this.idParcela = idParcela;
	}
	public Long getIdMarkupFaixaProgressiva() {
		return idMarkupFaixaProgressiva;
	}
	public void setIdMarkupFaixaProgressiva(Long idMarkupFaixaProgressiva) {
		this.idMarkupFaixaProgressiva = idMarkupFaixaProgressiva;
	}
	public List<MarkupValorFaixaProgressivaDTO> getListValoresFaixaProgressiva() {
		return listValoresFaixaProgressiva;
	}
	public void setListValoresFaixaProgressiva(List<MarkupValorFaixaProgressivaDTO> listValoresFaixaProgressiva) {
		this.listValoresFaixaProgressiva = listValoresFaixaProgressiva;
	}
	public void incluiValorFaixaProgressiva(MarkupValorFaixaProgressivaDTO valorFaixa) {
		if (valorFaixa == null) {
			return;
		}
		if (this.listValoresFaixaProgressiva == null) {
			this.listValoresFaixaProgressiva = new ArrayList<MarkupValorFaixaProgressivaDTO>();
		}
		this.listValoresFaixaProgressiva.add(valorFaixa);
	}

	public String getNomeParcela() {
		return nomeParcela;
	}

	public void setNomeParcela(String nomeParcela) {
		this.nomeParcela = nomeParcela;
	}
	
	private Long idValorMarkupFaixaProgressiva;

	public Long getIdValorMarkupFaixaProgressiva() {
		return idValorMarkupFaixaProgressiva;
	}

	public void setIdValorMarkupFaixaProgressiva(Long idValorMarkupFaixaProgressiva) {
		this.idValorMarkupFaixaProgressiva = idValorMarkupFaixaProgressiva;
	}
	
}
