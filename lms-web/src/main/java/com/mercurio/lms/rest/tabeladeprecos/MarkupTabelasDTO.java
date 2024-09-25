package com.mercurio.lms.rest.tabeladeprecos;

import java.io.Serializable;
import java.util.List;

public class MarkupTabelasDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idTabelaPreco;
	private List<MarkupMinimoProgressivoDTO> listMarkupMinimoProgressivoDTO;
	private List<MarkupPrecoFreteDTO> listMarkupPrecoFreteDTO;
	private List<MarkupDTO> listMarkupDto;
	private MarkupDTO markupGeral;
	
	public MarkupTabelasDTO() {
	}

	public List<MarkupMinimoProgressivoDTO> getListMarkupMinimoProgressivoDTO() {
		return listMarkupMinimoProgressivoDTO;
	}

	public void setListMarkupMinimoProgressivoDTO(List<MarkupMinimoProgressivoDTO> listMarkupMinimoProgressivoDTO) {
		this.listMarkupMinimoProgressivoDTO = listMarkupMinimoProgressivoDTO;
	}

	public List<MarkupPrecoFreteDTO> getListMarkupPrecoFreteDTO() {
		return listMarkupPrecoFreteDTO;
	}

	public void setListMarkupPrecoFreteDTO(
			List<MarkupPrecoFreteDTO> listMarkupPrecoFreteDTO) {
		this.listMarkupPrecoFreteDTO = listMarkupPrecoFreteDTO;
	}

	public List<MarkupDTO> getListMarkupDto() {
		return listMarkupDto;
	}

	public void setListMarkupDto(List<MarkupDTO> listMarkupDto) {
		this.listMarkupDto = listMarkupDto;
	}

	public Long getIdTabelaPreco() {
		return idTabelaPreco;
	}

	public void setIdTabelaPreco(Long idTabelaPreco) {
		this.idTabelaPreco = idTabelaPreco;
	}

	public MarkupDTO getMarkupGeral() {
		return markupGeral;
	}

	public void setMarkupGeral(MarkupDTO markupGeral) {
		this.markupGeral = markupGeral;
	}
	
}