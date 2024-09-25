package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="OBSERVACAO")
@SequenceGenerator(name = "OBSERVACAO_SQ", sequenceName = "OBSERVACAO_SQ")
public class Observacao implements Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name="id_observacao")
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OBSERVACAO_SQ")
		private Long id;

		@Column(name="id_tabela")
		private Long idTabela;

		@Column(name="nm_tabela")
		private String nomeTabela;

		@Column(name="ds_descricao")
		private String descricao;

		public Observacao() {
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getIdTabela() {
			return idTabela;
		}

		public void setIdTabela(Long idTabela) {
			this.idTabela = idTabela;
		}

		public String getNomeTabela() {
			return nomeTabela;
		}

		public void setNomeTabela(String nomeTabela) {
			this.nomeTabela = nomeTabela;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		
		

		
}