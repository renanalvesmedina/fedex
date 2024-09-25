package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="TAG_PARCELA_PRECOS")
@SequenceGenerator(name = "TAG_PARCELA_PRECO_SQ", sequenceName = "TAG_PARCELA_PRECO_SQ")
public class TagTabelaPreco implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final TagTabelaPreco VAZIO = new TagTabelaPreco();

	@Id
	@Column(name="ID_TAG_PARCELA_PRECO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TAG_PARCELA_PRECO_SQ")
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PARCELA_PRECO", nullable = true)
	private ParcelaPreco parcelaPreco;

	@Column(name="DS_TAG", nullable=false)
	private String descricao;

	@Column(name="TAG", nullable=false)
	private String tag;

	@Column(name="DS_OBSERVACOES")
	private String observacoes;

	TagTabelaPreco() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ParcelaPreco getParcelaPreco() {
		return parcelaPreco;
	}

	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Long getIdParcelaPreco() {
		if(this.parcelaPreco == null) return null;
		return this.parcelaPreco.getIdParcelaPreco();
	}



}
