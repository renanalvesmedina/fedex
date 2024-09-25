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

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;

@Entity
@Table(name="ETIQUETA_AFERICAO")
@SequenceGenerator(name="SQ_ETIQUETA_AFERICAO", sequenceName="ETIQUETA_AFERICAO_SQ", allocationSize=1)
public class EtiquetaAfericao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_ETIQUETA_AFERICAO")
	@Column(name="ID_ETIQUETA_AFERICAO", nullable=false)
	private Long idEtiquetaAfericao;
	
	@ManyToOne
	@JoinColumn(name="ID_FILIAL_ORIGEM", nullable=false)
	private Filial filialOrigem;
	
	@ManyToOne
	@JoinColumn(name="ID_FILIAL_DESTINO", nullable=false)
	private Filial filialDestino;
	
	@ManyToOne
	@JoinColumn(name="ID_ROTA_COLETA_ENTREGA", nullable=false)
	private RotaColetaEntrega rotaColetaEntrega;
	
	@Column(name="NR_COD_BARRA")
	private String nrCodigoBarras;
	
	@Column(name="NR_ALTURA")
	private Long nrAltura;
	
	@Column(name="NR_LARGURA")
	private Long nrLargura;
	
	@Column(name="NR_COMPRIMENTO")
	private Long nrComprimento;
	
	@Column(name="PS_INFORMADO")
	private BigDecimal psInformado;

	public Long getIdEtiquetaAfericao() {
		return idEtiquetaAfericao;
	}

	public void setIdEtiquetaAfericao(Long idEtiquetaAfericao) {
		this.idEtiquetaAfericao = idEtiquetaAfericao;
	}

	public Filial getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(Filial filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	public RotaColetaEntrega getRotaColetaEntrega() {
		return rotaColetaEntrega;
	}

	public void setRotaColetaEntrega(RotaColetaEntrega rotaColetaEntrega) {
		this.rotaColetaEntrega = rotaColetaEntrega;
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

	public BigDecimal getPsInformado() {
		return psInformado;
	}

	public void setPsInformado(BigDecimal psInformado) {
		this.psInformado = psInformado;
	}
}
