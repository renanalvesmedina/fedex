package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.mercurio.lms.municipios.model.Filial;


@Entity
@Table(name="HISTORICO_AFERICAO")
@SequenceGenerator(name="SQ_HISTORICO_AFERICAO", sequenceName="HISTORICO_AFERICAO_SQ", allocationSize=1)
public class HistoricoAfericao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_HISTORICO_AFERICAO")
	@Column(name="ID_HISTORICO_AFERICAO", nullable=false)
	private Long idHistoricoAfericao;
	
	@ManyToOne
	@JoinColumn(name="ID_FILIAL", nullable=false)
	private Filial filial;
	
	@ManyToOne
	@JoinColumn(name="ID_ETIQUETA_AFERICAO")
	private EtiquetaAfericao etiquetaAfericao;
	
	@Columns(columns = {@Column(name = "DH_AFERICAO", nullable = false),@Column(name = "DH_AFERICAO_TZR", nullable = false)})
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAfericao;
	
	@Column(name="NR_COD_BARRA")
	private String nrCodigoBarras;
	
	@Column(name="NR_ALTURA")
	private Long nrAltura;
	
	@Column(name="NR_LARGURA")
	private Long nrLargura;
	
	@Column(name="NR_COMPRIMENTO")
	private Long nrComprimento;
	
	@Column(name="PS_AFERIDO")
	private BigDecimal psAferido;

	public Long getIdHistoricoAfericao() {
		return idHistoricoAfericao;
	}

	public void setIdHistoricoAfericao(Long idHistoricoAfericao) {
		this.idHistoricoAfericao = idHistoricoAfericao;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public EtiquetaAfericao getEtiquetaAfericao() {
		return etiquetaAfericao;
	}

	public void setEtiquetaAfericao(EtiquetaAfericao etiquetaAfericao) {
		this.etiquetaAfericao = etiquetaAfericao;
	}

	public DateTime getDhAfericao() {
		return dhAfericao;
	}

	public void setDhAfericao(DateTime dhAfericao) {
		this.dhAfericao = dhAfericao;
	}

	public String getNrCodigoBarras() {
		return nrCodigoBarras;
	}

	public void setNrCodigoBarras(String nrCodigoBarras) {
		this.nrCodigoBarras = nrCodigoBarras;
	}

	public Long getNrAltura() {
		return nrAltura;
	}

	public void setNrAltura(Long nrAltura) {
		this.nrAltura = nrAltura;
	}

	public Long getNrLargura() {
		return nrLargura;
	}

	public void setNrLargura(Long nrLargura) {
		this.nrLargura = nrLargura;
	}

	public Long getNrComprimento() {
		return nrComprimento;
	}

	public void setNrComprimento(Long nrComprimento) {
		this.nrComprimento = nrComprimento;
	}

	public BigDecimal getPsAferido() {
		return psAferido;
	}

	public void setPsAferido(BigDecimal psAferido) {
		this.psAferido = psAferido;
	}
}
