package com.mercurio.lms.vendas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MOTIVO_REPROVACAO")
@SequenceGenerator(name = "MOTIVO_REPROVACAO_SEQ", sequenceName = "MOTIVO_REPROVACAO_SQ", allocationSize = 1)
public class MotivoReprovacao implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_MOTIVO_REPROVACAO", unique = true, nullable = false, precision = 10, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOTIVO_REPROVACAO_SEQ")
	private Long idMotivoReprovacao;

	@Column(name = "DS_MOTIVO_REPROVACAO", nullable = false)
	private String dsMotivoReprovacao;

	public Long getIdMotivoReprovacao() {
		return idMotivoReprovacao;
	}

	public void setIdMotivoReprovacao(Long idMotivoReprovacao) {
		this.idMotivoReprovacao = idMotivoReprovacao;
	}

	public String getDsMotivoReprovacao() {
		return dsMotivoReprovacao;
	}

	public void setDsMotivoReprovacao(String dsMotivoReprovacao) {
		this.dsMotivoReprovacao = dsMotivoReprovacao;
	}

	
}
