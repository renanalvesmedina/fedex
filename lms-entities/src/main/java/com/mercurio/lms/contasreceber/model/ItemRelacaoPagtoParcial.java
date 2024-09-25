package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name="ITEM_REL_PAGTO_PARCIAL")
@SequenceGenerator(name="SQ_ITEM_REL_PAGTO_PARCIAL", sequenceName="ITEM_REL_PAGTO_PARCIAL_SQ")
public class ItemRelacaoPagtoParcial implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ITEM_REL_PAGTO_PARCIAL")
	@Column(name = "ID_ITEM_REL_PAGTO_PARCIAL", nullable = false)
	private Long idItemRelacaoPagtoParcial;
	
	@Column(name="VL_PAGAMENTO", nullable = false)
	private BigDecimal vlPagamento;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DEVEDOR_DOC_SERV_FAT")
	private DevedorDocServFat devedorDocServFat;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_RELACAO_PAGTO_PARCIAL", nullable = false)
	private RelacaoPagtoParcial relacaoPagtoParcial;

	
	public Long getIdItemRelacaoPagtoParcial() {
		return idItemRelacaoPagtoParcial;
	}

	public void setIdItemRelacaoPagtoParcial(Long idItemRelacaoPagtoParcial) {
		this.idItemRelacaoPagtoParcial = idItemRelacaoPagtoParcial;
	}

	public BigDecimal getVlPagamento() {
		return vlPagamento;
	}

	public void setVlPagamento(BigDecimal vlPagamento) {
		this.vlPagamento = vlPagamento;
	}

	public DevedorDocServFat getDevedorDocServFat() {
		return devedorDocServFat;
	}

	public void setDevedorDocServFat(DevedorDocServFat devedorDocServFat) {
		this.devedorDocServFat = devedorDocServFat;
	}

	public RelacaoPagtoParcial getRelacaoPagtoParcial() {
		return relacaoPagtoParcial;
	}

	public void setRelacaoPagtoParcial(RelacaoPagtoParcial relacaoPagtoParcial) {
		this.relacaoPagtoParcial = relacaoPagtoParcial;
	}
}
