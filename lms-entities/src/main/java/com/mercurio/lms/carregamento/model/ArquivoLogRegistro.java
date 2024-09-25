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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * Entidade para atender a demanda LMS-1236
 * 
 * @author mxavier@voiza.com.br
 * 
 */

@Entity
@Table(name = "ARQUIVO_LOG_REGISTRO")
@SequenceGenerator(name = "ARQUIVO_LOG_REGISTRO_SQ", sequenceName = "ARQUIVO_LOG_REGISTRO_SQ")
public class ArquivoLogRegistro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARQUIVO_LOG_REGISTRO_SQ")
	@Column(name = "ID_ARQUIVO_LOG_REGISTRO", nullable = false)
	private Long idArquivoLogRegistro;

	@Column(name = "DS_ARQUIVO_LOG", nullable = true)
	private String dsArquivoLog;

	@Columns(columns = { @Column(name = "DH_INICIO_LEITURA"), @Column(name = "DH_INICIO_LEITURA_TZR ") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInicioLeitura;

	@Columns(columns = { @Column(name = "DH_FIM_LEITURA"), @Column(name = "DH_FIM_LEITURA_TZR ") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhFimLeitura;

	@Column(name = "QT_TOTAL_ARQUIVO_COMPACTADO", nullable = true)
	private Integer qtTotalArquivoCompactado;

	@Column(name = "DS_ERRO_ARQUIVO", nullable = true)
	private String dsErroArquivo;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ARQUIVO_LOG_REGISTRO")
	private List<ArquivoLogCompactado> arquivosLogCompactados; 
	
	public Long getIdArquivoLogRegistro() {
		return idArquivoLogRegistro;
	}

	public void setIdArquivoLogRegistro(Long idArquivoLogRegistro) {
		this.idArquivoLogRegistro = idArquivoLogRegistro;
	}

	public String getDsArquivoLog() {
		return dsArquivoLog;
	}

	public void setDsArquivoLog(String dsArquivoLog) {
		this.dsArquivoLog = dsArquivoLog;
	}

	public DateTime getDhInicioLeitura() {
		return dhInicioLeitura;
	}

	public void setDhInicioLeitura(DateTime dhInicioLeitura) {
		this.dhInicioLeitura = dhInicioLeitura;
	}

	public DateTime getDhFimLeitura() {
		return dhFimLeitura;
	}

	public void setDhFimLeitura(DateTime dhFimLeitura) {
		this.dhFimLeitura = dhFimLeitura;
	}

	public Integer getQtTotalArquivoCompactado() {
		return qtTotalArquivoCompactado;
	}

	public void setQtTotalArquivoCompactado(Integer qtTotalArquivoCompactado) {
		this.qtTotalArquivoCompactado = qtTotalArquivoCompactado;
	}

	public String getDsErroArquivo() {
		return dsErroArquivo;
	}

	public void setDsErroArquivo(String dsErroArquivo) {
		this.dsErroArquivo = dsErroArquivo;
	}

	public List<ArquivoLogCompactado> getArquivosLogCompactados() {
		return arquivosLogCompactados;
	}

	public void setArquivosLogCompactados(
			List<ArquivoLogCompactado> arquivosLogCompactados) {
		this.arquivosLogCompactados = arquivosLogCompactados;
	}
}