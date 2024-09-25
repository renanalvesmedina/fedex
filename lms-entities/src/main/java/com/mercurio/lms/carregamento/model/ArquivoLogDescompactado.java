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
@Table(name = "ARQUIVO_LOG_DESCOMPACTADO")
@SequenceGenerator(name = "ARQUIVO_LOG_DESCOMPACTADO_SQ", sequenceName = "ARQUIVO_LOG_DESCOMPACTADO_SQ")
public class ArquivoLogDescompactado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARQUIVO_LOG_DESCOMPACTADO_SQ")
	@Column(name = "ID_ARQUIVO_LOG_DESCOMPACTADO", nullable = false)
	private Long idArquivoLogDescompatado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ARQUIVO_LOG_COMPACTADO", nullable = false)
	private ArquivoLogCompactado arquivoLogCompactado;

	@Column(name = "DS_ARQUIVO_DESCOMPACTADO", nullable = true)
	private String dsArquivoDescompactado;

	@Column(name = "NR_TAMANHO_KB", nullable = true)
	private Long nrTamanhoTotalKb;

	@Column(name = "DS_ERRO_ARQUIVO", nullable = true)
	private String dsErroArquivo;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ARQUIVO_LOG_DESCOMPACTADO")
	private List<ArquivoLogProcessamento> arquivosLogProcessamentos;

	public Long getIdArquivoLogDescompatado() {
		return idArquivoLogDescompatado;
	}

	public void setIdArquivoLogDescompatado(Long idArquivoLogDescompatado) {
		this.idArquivoLogDescompatado = idArquivoLogDescompatado;
	}

	public ArquivoLogCompactado getArquivoLogCompactado() {
		return arquivoLogCompactado;
	}

	public void setArquivoLogCompactado(ArquivoLogCompactado arquivoLogCompactado) {
		this.arquivoLogCompactado = arquivoLogCompactado;
	}

	public String getDsArquivoDescompactado() {
		return dsArquivoDescompactado;
	}

	public void setDsArquivoDescompactado(String dsArquivoDescompactado) {
		this.dsArquivoDescompactado = dsArquivoDescompactado;
	}

	public Long getNrTamanhoTotalKb() {
		return nrTamanhoTotalKb;
	}

	public void setNrTamanhoTotalKb(Long nrTamanhoTotalKb) {
		this.nrTamanhoTotalKb = nrTamanhoTotalKb;
	}

	public String getDsErroArquivo() {
		return dsErroArquivo;
	}

	public void setDsErroArquivo(String dsErroArquivo) {
		this.dsErroArquivo = dsErroArquivo;
	}

	public List<ArquivoLogProcessamento> getArquivosLogProcessamentos() {
		return arquivosLogProcessamentos;
	}

	public void setArquivosLogProcessamentos(
			List<ArquivoLogProcessamento> arquivosLogProcessamentos) {
		this.arquivosLogProcessamentos = arquivosLogProcessamentos;
	}
}