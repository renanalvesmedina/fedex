package com.mercurio.lms.dto;

public class FiltroPaginacaoOrdenacaoDto {
	
	private String coluna;
	
	private Boolean asc;
	
	public String getColuna() {
		return coluna;
	}
	
	public void setColuna(String coluna) {
		this.coluna = coluna;
	}
	
	public Boolean getAsc() {
		return asc;
	}
	
	public void setAsc(boolean asc) {
		this.asc = asc;
	}

}
