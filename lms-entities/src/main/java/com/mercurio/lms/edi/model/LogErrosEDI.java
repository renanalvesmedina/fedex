package com.mercurio.lms.edi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.edi.enums.DsCampoLogErrosEDI;

@Entity
@Table(name = "LOG_ERROS_EDI")
@SequenceGenerator(name = "LOG_ERROS_EDI_SEQ", sequenceName = "LOG_ERROS_EDI_SQ", allocationSize = 1)
public class LogErrosEDI implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_LOG_ERROS_EDI", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_ERROS_EDI_SEQ")
	private Long idLogErrosEDI;

	@Column(name = "DS_TIPO_PROCESSAMENTO", length = 1, nullable = false)
	private String dsTipoProcessamento;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DS_CAMPO", length = 20, nullable = false)
	private DsCampoLogErrosEDI dsCampoLogErrosEDI;

	@Column(name = "DS_VLR_ERRADO", length = 20)
	private String dsValorErrado;

	@Column(name = "DS_VLR_CORRIGIDO", length = 20)
	private String dsValorCorrigido;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NOTA_FISCAL_EDI")
	private NotaFiscalEdi notaFiscalEdi;

	@Column(name = "NR_NOTA_FISCAL")
	private Integer nrNotaFiscal;
	
	@Column(name = "DATA_EMISSAO_NF")
	private Date dataEmissaoNf;
	
	@Column(name = "CNPJ_REME")
	private Long cnpjReme;
	
	@Column(name = "DH_CORRECAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType")
	private DateTime dhCorrecao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioLMS usuario;

	@Column(name = "NR_PROCESSAMENTO", nullable = false)
	private Long nrProcessamento;

	@Column(name = "NR_ORDEM_DIGITACAO")
	private Integer nrOrdemDigitacao;

	@Transient
	private Long etiquetaInicial;
	
	public Long getIdLogErrosEDI() {
		return idLogErrosEDI;
	}

	public void setIdLogErrosEDI(Long idLogErrosEDI) {
		this.idLogErrosEDI = idLogErrosEDI;
	}

	public String getDsTipoProcessamento() {
		return dsTipoProcessamento;
	}

	public void setDsTipoProcessamento(String dsTipoProcessamento) {
		this.dsTipoProcessamento = dsTipoProcessamento;
	}

	public String getDsValorErrado() {
		return dsValorErrado;
	}

	public void setDsValorErrado(String dsValorErrado) {
		this.dsValorErrado = dsValorErrado;
	}

	public String getDsValorCorrigido() {
		return dsValorCorrigido;
	}

	public void setDsValorCorrigido(String dsValorCorrigido) {
		this.dsValorCorrigido = dsValorCorrigido;
	}

	public Long getNrProcessamento() {
		return nrProcessamento;
	}

	public void setNrProcessamento(Long nrProcessamento) {
		this.nrProcessamento = nrProcessamento;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public DsCampoLogErrosEDI getDsCampoLogErrosEDI() {
		return dsCampoLogErrosEDI;
	}

	public void setDsCampoLogErrosEDI(DsCampoLogErrosEDI dsCampoLogErrosEDI) {
		this.dsCampoLogErrosEDI = dsCampoLogErrosEDI;
	}

	public Integer getNrNotaFiscal() {
		return nrNotaFiscal;
	}

	public void setNrNotaFiscal(Integer nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	public Date getDataEmissaoNf() {
		return dataEmissaoNf;
	}

	public void setDataEmissaoNf(Date dataEmissaoNf) {
		this.dataEmissaoNf = dataEmissaoNf;
	}

	public Long getCnpjReme() {
		return cnpjReme;
	}

	public void setCnpjReme(Long cnpjReme) {
		this.cnpjReme = cnpjReme;
	}

	public DateTime getDhCorrecao() {
		return dhCorrecao;
	}

	public void setDhCorrecao(DateTime dhCorrecao) {
		this.dhCorrecao = dhCorrecao;
	}

	public NotaFiscalEdi getNotaFiscalEdi() {
		return notaFiscalEdi;
	}

	public void setNotaFiscalEdi(NotaFiscalEdi notaFiscalEdi) {
		this.notaFiscalEdi = notaFiscalEdi;
	}

	public Long getEtiquetaInicial() {
		return etiquetaInicial;
	}

	public void setEtiquetaInicial(Long etiquetaInicial) {
		this.etiquetaInicial = etiquetaInicial;
	}

	public Integer getNrOrdemDigitacao() {
		return nrOrdemDigitacao;
	}

	public void setNrOrdemDigitacao(Integer nrOrdemDigitacao) {
		this.nrOrdemDigitacao = nrOrdemDigitacao;
	}

}
