package com.mercurio.lms.tabelaprecos.model;

public class CloneTabelaPrecoDTO {
	Long idTabelaBase;
	Long idTabelaNova;
	
	public CloneTabelaPrecoDTO(){}
	
	public CloneTabelaPrecoDTO(Long idTabelaBase, Long idTabelaNova) {
		this.idTabelaBase = idTabelaBase;
		this.idTabelaNova = idTabelaNova;
	}
	public Long getIdTabelaBase() {
		return idTabelaBase;
	}
	public void setIdTabelaBase(Long idTabelaBase) {
		this.idTabelaBase = idTabelaBase;
	}
	public Long getIdTabelaNova() {
		return idTabelaNova;
	}
	public void setIdTabelaNova(Long idTabelaNova) {
		this.idTabelaNova = idTabelaNova;
	}
	
	
	
}
