package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.DoctoServico;

@Entity
@Table(name = "DOCTO_SERVICO_SEM_COMISSAO")
@SequenceGenerator(name = "DOCTO_SERVICO_SEM_COMISSAO_SEQ", sequenceName = "DOCTO_SERVICO_SEM_COMISSAO_SQ", allocationSize=1)
public class DoctoServicoSemComissao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCTO_SERVICO_SEM_COMISSAO_SEQ")
	@Column(name = "ID_DOCTO_SERVICO_SEM_COMISSAO", nullable = false)
	private Long idDoctoServicoSemComissao;

	@ManyToOne
	@JoinColumn(name = "ID_EXECUTIVO", nullable = false)
	private UsuarioLMS executivo;
	
	@OneToOne
	@JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)
	private DoctoServico doctoServico;

	@Column(name = "DT_COMPETENCIA", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtCompetencia;
	
	@Column(name = "DT_INCLUSAO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtInclusao;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO")
	private UsuarioLMS usuarioInclusao;
	
	public Long getIdDoctoServicoSemComissao() {
		return idDoctoServicoSemComissao;
	}

	public void setIdDoctoServicoSemComissao(Long idDoctoServicoSemComissao) {
		this.idDoctoServicoSemComissao = idDoctoServicoSemComissao;
	}

	public UsuarioLMS getExecutivo() {
		return executivo;
	}

	public void setExecutivo(UsuarioLMS executivo) {
		this.executivo = executivo;
	}

	public YearMonthDay getDtCompetencia() {
		return dtCompetencia;
	}

	public void setDtCompetencia(YearMonthDay dtCompetencia) {
		this.dtCompetencia = dtCompetencia;
	}

	public YearMonthDay getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(YearMonthDay dtInclusao) {
		this.dtInclusao = dtInclusao;
	}
	
	public UsuarioLMS getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(UsuarioLMS usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDoctoServicoSemComissao == null) ? 0 : idDoctoServicoSemComissao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DoctoServicoSemComissao other = (DoctoServicoSemComissao) obj;
		if (idDoctoServicoSemComissao == null) {
			if (other.idDoctoServicoSemComissao != null)
				return false;
		} else if (!idDoctoServicoSemComissao.equals(other.idDoctoServicoSemComissao))
			return false;
		return true;
	}

}
