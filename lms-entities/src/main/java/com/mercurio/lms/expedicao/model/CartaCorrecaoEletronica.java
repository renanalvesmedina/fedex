package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "CARTA_CORRECAO_ELETRONICA")
@SequenceGenerator(name = "CARTA_CORRECAO_ELETRONICA_SEQ", sequenceName = "CARTA_CORRECAO_ELETRONICA_SQ", allocationSize = 1)
public class CartaCorrecaoEletronica implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARTA_CORRECAO_ELETRONICA_SEQ")
	@Column(name = "ID_CARTA_CORRECAO_ELETRONICA", nullable = false)
	private Long idCartaCorrecaoEletronica;
	
	@Columns(columns = {
			@Column(name = "DH_EMISSAO", nullable = false),
			@Column(name = "DH_EMISSAO_TZR", nullable = false)
	})
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEmissao;
	
	@OneToOne
	@JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)
	private DoctoServico doctoServico;
	
	@Column(name = "NR_CARTA_CORRECAO_ELETRONICA", length = 10, nullable = false)
	private Long nrCartaCorrecaoEletronica;
	
	@Column(name = "ID_TAG_GRUPO", length = 20, nullable = false)
	private String idTagGrupo;
	
	@Column(name = "ID_TAG_CAMPO", length = 20, nullable = false)
	private String idTagCampo;
	
	@Column(name = "DS_CONTEUDO_DOCTO", length = 2000, nullable = true)
	private String dsConteudoDocto;
	
	@Column(name = "DS_CONTEUDO_CARTA", length = 500, nullable = true)
	private String dsConteudoCarta;
	
	@Column(name = "TP_SITUACAO_CARTA_CORRECAO", length = 1, nullable = false)
	private String tpSituacaoCartaCorrecao;
	
	@Column(name = "NR_PROTOCOLO", length = 15, nullable = true)
	private Long nrProtocolo;
	
	@Column(name = "DS_OBSERVACAO", length = 255, nullable = true)
	private String dsObservacao;

	public Long getIdCartaCorrecaoEletronica() {
		return idCartaCorrecaoEletronica;
	}

	public void setIdCartaCorrecaoEletronica(Long idCartaCorrecaoEletronica) {
		this.idCartaCorrecaoEletronica = idCartaCorrecaoEletronica;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public Long getNrCartaCorrecaoEletronica() {
		return nrCartaCorrecaoEletronica;
	}

	public void setNrCartaCorrecaoEletronica(Long nrCartaCorrecaoEletronica) {
		this.nrCartaCorrecaoEletronica = nrCartaCorrecaoEletronica;
	}

	public String getIdTagGrupo() {
		return idTagGrupo;
	}

	public void setIdTagGrupo(String idTagGrupo) {
		this.idTagGrupo = idTagGrupo;
	}

	public String getIdTagCampo() {
		return idTagCampo;
	}

	public void setIdTagCampo(String idTagCampo) {
		this.idTagCampo = idTagCampo;
	}

	public String getDsConteudoDocto() {
		return dsConteudoDocto;
	}

	public void setDsConteudoDocto(String dsConteudoDocto) {
		this.dsConteudoDocto = dsConteudoDocto;
	}

	public String getDsConteudoCarta() {
		return dsConteudoCarta;
	}

	public void setDsConteudoCarta(String dsConteudoCarta) {
		this.dsConteudoCarta = dsConteudoCarta;
	}

	public String getTpSituacaoCartaCorrecao() {
		return tpSituacaoCartaCorrecao;
	}

	public void setTpSituacaoCartaCorrecao(String tpSituacaoCartaCorrecao) {
		this.tpSituacaoCartaCorrecao = tpSituacaoCartaCorrecao;
	}

	public Long getNrProtocolo() {
		return nrProtocolo;
	}

	public void setNrProtocolo(Long nrProtocolo) {
		this.nrProtocolo = nrProtocolo;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdCartaCorrecaoEletronica()).toHashCode();
	}
	
	@Override
	public boolean equals(Object that) {
		if ((this == that)) {
			return true;
		}
		if (!(that instanceof CartaCorrecaoEletronica)) {
			return false;
		}
		return new EqualsBuilder().append(
				this.getIdCartaCorrecaoEletronica(),
				((CartaCorrecaoEletronica) that).getIdCartaCorrecaoEletronica()).isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(
				"idCartaCorrecaoEletronica",
				getIdCartaCorrecaoEletronica()).toString();
	}
	
}
