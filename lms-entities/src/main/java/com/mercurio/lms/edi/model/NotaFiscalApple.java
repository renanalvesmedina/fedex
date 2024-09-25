package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

@Entity
@Table(name = "NFF_DN_APPLE")
@SequenceGenerator(name = "NOTA_FISCAL_APPLE_SEQ", sequenceName = "NFF_DN_APPLE_SQ", allocationSize=1)
public class NotaFiscalApple implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long idNotaFiscalApple;
	private Long nrNotaFiscal;
	private String nrSerie;
	private YearMonthDay dtEmissao;
	private List<ItemNotaFiscalApple> itens;
	
	@Id
	@Column(name = "ID_NFF_DN_APPLE", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTA_FISCAL_APPLE_SEQ")
	public Long getIdNotaFiscalApple() {
		return idNotaFiscalApple;
	}

	public void setIdNotaFiscalApple(Long idNotaFiscalApple) {
		this.idNotaFiscalApple = idNotaFiscalApple;
	}
	
	@Column(name = "NR_NOTA_FISCAL", nullable = false)
	public Long getNrNotaFiscal() {
		return nrNotaFiscal;
	}

	public void setNrNotaFiscal(Long nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}
		
	@Column(name = "DS_SERIE_NF", nullable = false, length=3)
	public String getNrSerie() {
		return nrSerie;
	}

	public void setNrSerie(String nrSerie) {
		this.nrSerie = nrSerie;
	}
	
	@Column(name = "DT_EMISSAO_NF", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType") 
	public YearMonthDay getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(YearMonthDay dtEmissao) {
		this.dtEmissao = dtEmissao;
	}
	
	@OneToMany(mappedBy="informacaoDoctoCliente")
	public List<ItemNotaFiscalApple> getItens() {
		return itens;
	}

	public void setItens(List<ItemNotaFiscalApple> itens) {
		this.itens = itens;
	}		
}
