package com.mercurio.lms.tabelaprecos.model;

public class CloneGrupoRegiaoDTO {

	Long idGrupoRegiaoOrigem;
	Long idGrupoRegiaoDestino;
	Long idRotaPreco;

	public CloneGrupoRegiaoDTO() {
	}
	public CloneGrupoRegiaoDTO(Long idGrupoRegiaoOrigem, Long idGrupoRegiaoDestino, Long idRotaPreco) {
		this.idGrupoRegiaoOrigem = idGrupoRegiaoOrigem;
		this.idGrupoRegiaoDestino = idGrupoRegiaoDestino;
		this.idRotaPreco = idRotaPreco;
	}
	public Long getIdGrupoRegiaoOrigem() {
		return idGrupoRegiaoOrigem;
	}
	public void setIdGrupoRegiaoOrigem(Long idGrupoRegiaoOrigem) {
		this.idGrupoRegiaoOrigem = idGrupoRegiaoOrigem;
	}
	public Long getIdGrupoRegiaoDestino() {
		return idGrupoRegiaoDestino;
	}
	public void setIdGrupoRegiaoDestino(Long idGrupoRegiaoDestino) {
		this.idGrupoRegiaoDestino = idGrupoRegiaoDestino;
	}
	public Long getIdRotaPreco() {
		return idRotaPreco;
	}
	public void setIdRotaPreco(Long idRotaPreco) {
		this.idRotaPreco = idRotaPreco;
	}

}
