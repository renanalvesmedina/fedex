package com.mercurio.lms.rnc.model;

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

/**
 * 
 * @author WagnerFC
 *
 */
@Entity
@Table(name = "ITEM_OCORRENCIA_NC")
@SequenceGenerator(name = "ITEM_OCORRENCIA_NC_SEQ", sequenceName = "ITEM_OCORRENCIA_NC_SQ")
public class ItemOcorrenciaNc implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_OCORRENCIA_NC_SEQ")
	@Column(name = "ID_ITEM_OCORRENCIA_NC", nullable = false)
	private Long idItemOcorrenciaNc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_OCORRENCIA_NAO_CONFORMIDADE", nullable = false)
	private OcorrenciaNaoConformidade ocorrenciaNaoConformidade;

	@Column(name = "NR_CHAVE", nullable = false)
	private String nrChave;

	@Column(name = "NR_ITEM", nullable = false)
	private Long nrItem;

	@Column(name = "QT_INFORMADA", nullable = false)
	private Long qtInformada;

	@Column(name = "QT_NAO_CONFORMIDADE", nullable = false)
	private Long qtNaoConformidade;

	@Column(name = "VL_NAO_CONFORMIDADE", nullable = false)
	private Double vlNaoConformidade;

	public Long getIdItemOcorrenciaNc() {
		return idItemOcorrenciaNc;
	}

	public void setIdItemOcorrenciaNc(Long idItemOcorrenciaNc) {
		this.idItemOcorrenciaNc = idItemOcorrenciaNc;
	}

	public OcorrenciaNaoConformidade getOcorrenciaNaoConformidade() {
		return ocorrenciaNaoConformidade;
	}

	public void setOcorrenciaNaoConformidade(
			OcorrenciaNaoConformidade ocorrenciaNaoConformidade) {
		this.ocorrenciaNaoConformidade = ocorrenciaNaoConformidade;
	}

	public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

	public Long getNrItem() {
		return nrItem;
	}

	public void setNrItem(Long nrItem) {
		this.nrItem = nrItem;
	}

	public Long getQtInformada() {
		return qtInformada;
	}

	public void setQtInformada(Long qtInformada) {
		this.qtInformada = qtInformada;
	}

	public Long getQtNaoConformidade() {
		return qtNaoConformidade;
	}

	public void setQtNaoConformidade(Long qtNaoConformidade) {
		this.qtNaoConformidade = qtNaoConformidade;
	}

	public Double getVlNaoConformidade() {
		return vlNaoConformidade;
	}

	public void setVlNaoConformidade(Double vlNaoConformidade) {
		this.vlNaoConformidade = vlNaoConformidade;
	}
	
}
