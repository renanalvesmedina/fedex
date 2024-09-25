package com.mercurio.lms.franqueados.model;

import javax.persistence.CascadeType;
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
import javax.persistence.Version;

import org.hibernate.annotations.Type;


@Entity
@Table(name = "ANEXO_LANCAMENTO_FRQ")
@SequenceGenerator(name = "ANEXO_LANCAMENTO_FRQ_SEQ", sequenceName = "ANEXO_LANCAMENTO_FRQ_SQ", allocationSize = 1)
public class AnexoLancamentoFranqueado implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_ANEXO_LANCAMENTO_FRQ", unique = true, nullable = false, precision = 10, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANEXO_LANCAMENTO_FRQ_SEQ")
	private Long idAnexoLancamentoFrq;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade =  {CascadeType.ALL})
	@JoinColumn(name = "ID_LANCAMENTO_FRQ", nullable = false)
	private LancamentoFranqueado lancamento;
	
	@Column(name = "DS_ANEXO", nullable = false)
	private String dsAnexo;
	
	@Column(name = "DC_ARQUIVO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	private byte[] dcArquivo;

	
	/**
	* atributo necessário quando é utilizado em DF2 
	*/
	@Version
	@Column(name = "NR_VERSAO")
	private Integer versao;
	
	
	public AnexoLancamentoFranqueado() {
	}

	public Long getIdAnexoLancamentoFrq() {
		return idAnexoLancamentoFrq;
	}

	public void setIdAnexoLancamentoFrq(Long idAnexoLancamentoFrq) {
		this.idAnexoLancamentoFrq = idAnexoLancamentoFrq;
	}

	public LancamentoFranqueado getLancamento() {
		return lancamento;
	}

	public void setLancamento(LancamentoFranqueado lancamento) {
		this.lancamento = lancamento;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}
}
