package com.mercurio.lms.edi.model;

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

import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;

@Entity
@Table(name = "NFF_DN_APPLE_ITENS")
@SequenceGenerator(name = "ITENS_NOTA_FISCAL_APPLE_SEQ", sequenceName = "NFF_DN_APPLE_ITENS_SQ", allocationSize=1)
public class ItemNotaFiscalApple implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long idItemNotaFiscalApple;
	private String dsDnn;
	private String dsValorCampo;
	private NotaFiscalApple notaFiscalApple;
	private InformacaoDoctoCliente informacaoDoctoCliente;	
	
	@Id
	@Column(name = "ID_NFF_DN_APPLE_ITENS", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITENS_NOTA_FISCAL_APPLE_SEQ")	
	public Long getIdItemNotaFiscalApple() {
		return idItemNotaFiscalApple;
	}

	public void setIdItemNotaFiscalApple(Long idItemNotaFiscalApple) {
		this.idItemNotaFiscalApple = idItemNotaFiscalApple;
	}

	@Column(name="DS_DNN", length=20)
	public String getDsDnn() {
		return dsDnn;
	}

	public void setDsDnn(String dsIdDnn) {
		this.dsDnn = dsIdDnn;
	}

	@Column(name="DS_VALOR_CAMPO", length=100)
	public String getDsValorCampo() {
		return dsValorCampo;
	}

	public void setDsValorCampo(String dsValorCampo) {
		this.dsValorCampo = dsValorCampo;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_NFF_DN_APPLE")
	public NotaFiscalApple getNotaFiscalApple() {
		return notaFiscalApple;
	}

	public void setNotaFiscalApple(NotaFiscalApple notaFiscalApple) {
		this.notaFiscalApple = notaFiscalApple;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_INFORMACAO_DOCTO_CLIENTE")
	public InformacaoDoctoCliente getInformacaoDoctoCliente() {
		return informacaoDoctoCliente;
	}

	public void setInformacaoDoctoCliente(
			InformacaoDoctoCliente informacaoDoctoCliente) {
		this.informacaoDoctoCliente = informacaoDoctoCliente;
	}	
}
