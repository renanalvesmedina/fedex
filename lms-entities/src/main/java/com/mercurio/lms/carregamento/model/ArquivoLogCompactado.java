package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entidade para atender a demanda LMS-1236
 * 
 * @author mxavier@voiza.com.br
 * 
 */

@Entity
@Table(name = "ARQUIVO_LOG_COMPACTADO")
@SequenceGenerator(name = "ARQUIVO_LOG_COMPACTADO_SQ", sequenceName = "ARQUIVO_LOG_COMPACTADO_SQ")
public class ArquivoLogCompactado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARQUIVO_LOG_COMPACTADO_SQ")
	@Column(name = "ID_ARQUIVO_LOG_COMPACTADO", nullable = false)
	private Long idArquivoLogCompatado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ARQUIVO_LOG_REGISTRO", nullable = false)
	private ArquivoLogRegistro arquivoLogRegistro;

	@Column(name = "DS_ARQUIVO_COMPACTADO", nullable = true)
	private String dsArquivoCompactado;

	@Column(name = "NR_TAMANHO_TOTAL_KB", nullable = true)
	private Long nrTamanhoTotalKb;

	@Column(name = "QT_TOTAL_ARQUIVO_DESCOMPACTADO", nullable = true)
	private Integer qtTotalArquivoDescompactado;

	@Column(name = "DS_ERRO_ARQUIVO", nullable = true)
	private String dsErroArquivo;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ARQUIVO_LOG_COMPACTADO")
	private List<ArquivoLogDescompactado> arquivosLogDescompactados;

	public Long getIdArquivoLogCompatado() {
		return idArquivoLogCompatado;
	}

	public void setIdArquivoLogCompatado(Long idArquivoLogCompatado) {
		this.idArquivoLogCompatado = idArquivoLogCompatado;
	}

	public ArquivoLogRegistro getArquivoLogRegistro() {
		return arquivoLogRegistro;
	}

	public void setArquivoLogRegistro(ArquivoLogRegistro arquivoLogRegistro) {
		this.arquivoLogRegistro = arquivoLogRegistro;
	}

	public String getDsArquivoCompactado() {
		return dsArquivoCompactado;
	}

	public void setDsArquivoCompactado(String dsArquivoCompactado) {
		this.dsArquivoCompactado = dsArquivoCompactado;
	}

	public Long getNrTamanhoTotalKb() {
		return nrTamanhoTotalKb;
	}

	public void setNrTamanhoTotalKb(Long nrTamanhoTotalKb) {
		this.nrTamanhoTotalKb = nrTamanhoTotalKb;
	}

	public Integer getQtTotalArquivoDescompactado() {
		return qtTotalArquivoDescompactado;
	}

	public void setQtTotalArquivoDescompactado(Integer qtTotalArquivoDescompactado) {
		this.qtTotalArquivoDescompactado = qtTotalArquivoDescompactado;
	}

	public String getDsErroArquivo() {
		return dsErroArquivo;
	}

	public void setDsErroArquivo(String dsErroArquivo) {
		this.dsErroArquivo = dsErroArquivo;
	}

	public List<ArquivoLogDescompactado> getArquivosLogDescompactados() {
		return arquivosLogDescompactados;
	}

	public void setArquivosLogDescompactados(
			List<ArquivoLogDescompactado> arquivosLogDescompactados) {
		this.arquivosLogDescompactados = arquivosLogDescompactados;
	}
}