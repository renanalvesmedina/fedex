package com.mercurio.lms.configuracoes.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="IMAGEM")
@SequenceGenerator(name = "IMAGEM_SQ", sequenceName = "IMAGEM_SQ")
public class Imagem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_IMAGEM")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMAGEM_SQ")
	private Long idImagem;
	
	@Column(name = "CHAVE", length = 60)
	private String chave;
	
	@Column(name = "PICTURE")
	private String picture;

	public Long getIdImagem() {
		return idImagem;
	}

	public void setIdImagem(Long idImagem) {
		this.idImagem = idImagem;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}
