package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="PARCELA_RECALCULO_SORTER")
@SequenceGenerator(name="PARCELA_RECALCULO_SORTER_SEQ", sequenceName="PARCELA_RECALCULO_SORTER_SQ", allocationSize=1)
public class ParcelaRecalculoSorter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARCELA_RECALCULO_SORTER_SEQ")
	@Column(name = "ID_PARCELA_RECALCULO_SORTER", nullable = false)
	private Long idParcelaRecalculoSorter;
	
	@Column(name = "ID_DOCTO_SERVICO", nullable = false)
	private Long idDoctoServico;
	
	@Column(name = "ID_PARCELA_PRECO", nullable = false)
	private Long idParcelaPreco;
	
	@Column(name = "VL_PARCELA_PRECO", nullable = false, length = 18, scale = 2)
	private BigDecimal vlParcelaPreco;


	public Long getIdParcelaRecalculoSorter() {
		return idParcelaRecalculoSorter;
	}

	public void setIdParcelaRecalculoSorter(Long idParcelaRecalculoSorter) {
		this.idParcelaRecalculoSorter = idParcelaRecalculoSorter;
	}

	public Long getIdDoctoServico() {
		return idDoctoServico;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

	public Long getIdParcelaPreco() {
		return idParcelaPreco;
	}

	public void setIdParcelaPreco(Long idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}

	public BigDecimal getVlParcelaPreco() {
		return vlParcelaPreco;
	}

	public void setVlParcelaPreco(BigDecimal vlParcelaPreco) {
		this.vlParcelaPreco = vlParcelaPreco;
	}
}
