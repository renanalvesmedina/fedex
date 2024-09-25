package com.mercurio.lms.expedicao.model;

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
@Table(name="ORDEM_SERVICO_DOCUMENTO")
@SequenceGenerator(name = "ORDEM_SERVICO_DOCUMENTO_SQ", sequenceName = "ORDEM_SERVICO_DOCUMENTO_SQ", allocationSize=1)
public class OrdemServicoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_ORDEM_SERVICO_DOCUMENTO", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDEM_SERVICO_DOCUMENTO_SQ")
	private Long idOrdemServicoDocumento;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_ORDEM_SERVICO", nullable = false)
	private OrdemServico ordemServico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DOCTO_SERVICO", nullable = true)
	private DoctoServico doctoServico;

	public Long getIdOrdemServicoDocumento() {
		return idOrdemServicoDocumento;
	}
	public void setIdOrdemServicoDocumento(Long idOrdemServicoDocumento) {
		this.idOrdemServicoDocumento = idOrdemServicoDocumento;
	}

	public OrdemServico getOrdemServico() {
		return ordemServico;
	}
	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}
	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}
}