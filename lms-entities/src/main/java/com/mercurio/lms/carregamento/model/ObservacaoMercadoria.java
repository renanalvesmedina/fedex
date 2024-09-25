package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.DoctoServico;

@Entity
@Table(name = "OBSERVACAO_MERCADORIA")
@SequenceGenerator(name = "OBSERVACAO_MERCADORIA_SEQ", sequenceName = "OBSERVACAO_MERCADORIA_SQ", allocationSize=1)
public class ObservacaoMercadoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OBSERVACAO_MERCADORIA_SEQ")
	@Column(name = "ID_OBSERVACAO_MERCADORIA", nullable = false)
	private Long idObservacaoMercadoria;

	@ManyToOne
	@JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)
	private DoctoServico doctoServico;
	
	@Column(name = "DS_OBSERVACAO", nullable = false)
	private String dsObservacao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO")
	private UsuarioLMS usuarioInclusao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO")
	private UsuarioLMS usuarioAlteracao;

	@Columns(columns = { @Column(name = "DH_INCLUSAO"), @Column(name = "DH_INCLUSAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Columns(columns = { @Column(name = "DH_ALTERACAO"), @Column(name = "DH_ALTERACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;

	public Long getIdObservacaoMercadoria() {
		return idObservacaoMercadoria;
	}

	public void setIdObservacaoMercadoria(Long idObservacaoMercadoria) {
		this.idObservacaoMercadoria = idObservacaoMercadoria;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
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

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idObservacaoMercadoria == null) ? 0 : idObservacaoMercadoria.hashCode());
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
		ObservacaoMercadoria other = (ObservacaoMercadoria) obj;
		if (idObservacaoMercadoria == null) {
			if (other.idObservacaoMercadoria != null)
				return false;
		} else if (!idObservacaoMercadoria.equals(other.idObservacaoMercadoria))
			return false;
		return true;
	}

}
