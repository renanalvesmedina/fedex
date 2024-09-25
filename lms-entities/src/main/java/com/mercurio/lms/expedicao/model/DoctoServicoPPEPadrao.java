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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "DOCTO_SERVICO_PPE_PADRAO")
@SequenceGenerator(name = "DOCTO_SERVICO_PPE_PADRAO_SEQ", sequenceName = "DOCTO_SERVICO_PPE_PADRAO_SQ", allocationSize = 1)
public class DoctoServicoPPEPadrao implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCTO_SERVICO_PPE_PADRAO_SEQ")
	@Column(name = "ID_DOCTO_SERVICO_PPE_PADRAO", nullable = false)
	private Long idDoctoServicoPPEPadrao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)	
	private DoctoServico doctoServico;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO", nullable = false)
	private UsuarioLMS usuarioInclusao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO", nullable = false)
	private UsuarioLMS usuarioAlteracao;
	
	@Column(name = "NR_DIAS_COLETA", length = 3, nullable = true)
	private Short nrDiasColeta;
	
	@Column(name = "NR_DIAS_ENTREGA", length = 3, nullable = true)
	private Short nrDiasEntrega;
	
	@Column(name = "NR_DIAS_TRANSFERENCIA", length = 3, nullable = true)
	private Short nrDiasTransferencia;

	public Long getIdDoctoServicoPPEPadrao() {
		return idDoctoServicoPPEPadrao;
	}

	public void setIdDoctoServicoPPEPadrao(Long idDoctoServicoPPEPadrao) {
		this.idDoctoServicoPPEPadrao = idDoctoServicoPPEPadrao;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public UsuarioLMS getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(UsuarioLMS usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public UsuarioLMS getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioLMS usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Short getNrDiasColeta() {
		return nrDiasColeta;
	}

	public void setNrDiasColeta(Short nrDiasColeta) {
		this.nrDiasColeta = nrDiasColeta;
	}

	public Short getNrDiasEntrega() {
		return nrDiasEntrega;
	}

	public void setNrDiasEntrega(Short nrDiasEntrega) {
		this.nrDiasEntrega = nrDiasEntrega;
	}

	public Short getNrDiasTransferencia() {
		return nrDiasTransferencia;
	}

	public void setNrDiasTransferencia(Short nrDiasTransferencia) {
		this.nrDiasTransferencia = nrDiasTransferencia;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DoctoServicoPPEPadrao))
			return false;
		DoctoServicoPPEPadrao castOther = (DoctoServicoPPEPadrao) other;
		return new EqualsBuilder().append(this.getIdDoctoServicoPPEPadrao(), castOther.getIdDoctoServicoPPEPadrao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDoctoServicoPPEPadrao()).toHashCode();
    }
	
}
