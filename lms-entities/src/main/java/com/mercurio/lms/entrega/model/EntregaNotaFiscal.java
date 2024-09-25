package com.mercurio.lms.entrega.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;

@Entity
@Table(name = "ENTREGA_NOTA_FISCAL")
@SequenceGenerator(name = "ENTREGA_NOTA_FISCAL_SEQ", sequenceName = "ENTREGA_NOTA_FISCAL_SQ", allocationSize = 1)
public class EntregaNotaFiscal implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_ENTREGA_NOTA_FISCAL", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTREGA_NOTA_FISCAL_SEQ")
	private Long idEntregaNotaFiscal;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MANIFESTO", nullable = false)
	private Manifesto manifesto;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NOTA_FISCAL_CONHECIMENTO", nullable = false)
	private NotaFiscalConhecimento notaFiscalConhecimento;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MANIFESTO_NACIONAL_CTO", nullable = true)
	private ManifestoNacionalCto manifestoNacionalCto;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MANIFESTO_ENTREGA_DOCUMENTO", nullable = true)
	private ManifestoEntregaDocumento manifestoEntregaDocumento;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_OCORRENCIA_ENTREGA", nullable = false)
	private OcorrenciaEntrega ocorrenciaEntrega;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioLMS usuario;
	
	@Columns(columns = { @Column(name = "DH_OCORRENCIA", nullable = true), @Column(name = "DH_OCORRENCIA_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhOcorrencia;
	
	@Column(name = "OB_ALTERACAO", nullable = true)
	private String obAlteracao;
	
	@Column(name = "QT_VOLUMES_ENTREGUES", nullable = true)
	private Integer qtVolumesEntregues;
	
	public String toString() {
		return new ToStringBuilder(this).append("idEntregaNotaFiscal", getIdEntregaNotaFiscal()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EntregaNotaFiscal))
			return false;
		EntregaNotaFiscal castOther = (EntregaNotaFiscal) other;
		return new EqualsBuilder().append(this.getIdEntregaNotaFiscal(), castOther.getIdEntregaNotaFiscal()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdEntregaNotaFiscal()).toHashCode();
	}

	public Long getIdEntregaNotaFiscal() {
		return idEntregaNotaFiscal;
	}

	public void setIdEntregaNotaFiscal(Long idEntregaNotaFiscal) {
		this.idEntregaNotaFiscal = idEntregaNotaFiscal;
	}

	public Manifesto getManifesto() {
		return manifesto;
	}

	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}

	public NotaFiscalConhecimento getNotaFiscalConhecimento() {
		return notaFiscalConhecimento;
	}

	public void setNotaFiscalConhecimento(
			NotaFiscalConhecimento notaFiscalConhecimento) {
		this.notaFiscalConhecimento = notaFiscalConhecimento;
	}

	public ManifestoNacionalCto getManifestoNacionalCto() {
		return manifestoNacionalCto;
	}

	public void setManifestoNacionalCto(ManifestoNacionalCto manifestoNacionalCto) {
		this.manifestoNacionalCto = manifestoNacionalCto;
	}

	public ManifestoEntregaDocumento getManifestoEntregaDocumento() {
		return manifestoEntregaDocumento;
	}

	public void setManifestoEntregaDocumento(
			ManifestoEntregaDocumento manifestoEntregaDocumento) {
		this.manifestoEntregaDocumento = manifestoEntregaDocumento;
	}

	public OcorrenciaEntrega getOcorrenciaEntrega() {
		return ocorrenciaEntrega;
	}

	public void setOcorrenciaEntrega(OcorrenciaEntrega ocorrenciaEntrega) {
		this.ocorrenciaEntrega = ocorrenciaEntrega;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public DateTime getDhOcorrencia() {
		return dhOcorrencia;
	}

	public void setDhOcorrencia(DateTime dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}

	public String getObAlteracao() {
		return obAlteracao;
	}

	public void setObAlteracao(String obAlteracao) {
		this.obAlteracao = obAlteracao;
	}

	public Integer getQtVolumesEntregues() {
		return qtVolumesEntregues;
	}

	public void setQtVolumesEntregues(Integer qtVolumesEntregues) {
		this.qtVolumesEntregues = qtVolumesEntregues;
	}


}
