package com.mercurio.lms.sgr.model;

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

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "VIRUS_CARGA")
@SequenceGenerator(name = "VIRUS_CARGA_SQ", sequenceName = "VIRUS_CARGA_SQ", allocationSize = 1)
public class VirusCarga implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_VIRUS_CARGA", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VIRUS_CARGA_SQ")
	private Long idVirusCarga;
	
	@Columns(columns = { @Column(name = "DH_ATIVACAO", nullable = false), @Column(name = "DH_ATIVACAO_TZR ", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAtivacao;
	
	@Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = false), @Column(name = "DH_INCLUSAO_TZR ", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE", nullable = false)
	private Cliente cliente;
	
	@Column(name = "NR_CHAVE", nullable = true)
	private String nrChave;
	
	@Column(name = "DS_SERIE", nullable = true)
	private String dsSerie;
	
	@Column(name = "NR_ISCA_CARGA", nullable = false)
	private String nrIscaCarga;

	@Column(name = "NR_NOTA_FISCAL", nullable = false)
	private Long nrNotaFiscal;
	
	@Column(name = "NR_VOLUME", nullable = true)
	private String nrVolume;

	public Long getIdVirusCarga() {
		return idVirusCarga;
	}

	public void setIdVirusCarga(Long idVirusCarga) {
		this.idVirusCarga = idVirusCarga;
	}

	public DateTime getDhAtivacao() {
		return dhAtivacao;
	}

	public void setDhAtivacao(DateTime dhAtivacao) {
		this.dhAtivacao = dhAtivacao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

	public String getNrIscaCarga() {
		return nrIscaCarga;
	}

	public void setNrIscaCarga(String nrIscaCarga) {
		this.nrIscaCarga = nrIscaCarga;
	}

	public Long getNrNotaFiscal() {
		return nrNotaFiscal;
	}

	public void setNrNotaFiscal(Long nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	public String getNrVolume() {
		return nrVolume;
	}

	public void setNrVolume(String nrVolume) {
		this.nrVolume = nrVolume;
	}

	public String getDsSerie() {
		return dsSerie;
	}

	public void setDsSerie(String dsSerie) {
		this.dsSerie = dsSerie;
	}
	
}
