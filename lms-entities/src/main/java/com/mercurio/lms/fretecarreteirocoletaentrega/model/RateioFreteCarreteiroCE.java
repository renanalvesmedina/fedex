package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;

@Entity
@Table(name = "RATEIO_FRETE_CARRETEIRO_CE")
@SequenceGenerator(name = "RATEIO_FRETE_CARRETEIRO_CE_SQ", sequenceName = "RATEIO_FRETE_CARRETEIRO_CE_SQ", allocationSize = 1)
public class RateioFreteCarreteiroCE implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RATEIO_FRETE_CARRETEIRO_CE_SQ")
	@Column(name = "ID_RATEIO_FRETE_CARRETEIRO_CE", nullable = false)
	private Long idRateioFreteCarreteiroCE;

	@OneToOne
	@JoinColumn(name = "ID_NOTA_CREDITO")
	private NotaCredito notaCredito;

	@OneToOne
	@JoinColumn(name = "ID_DOCTO_SERVICO")
	private DoctoServico doctoServico;

	@OneToOne
	@JoinColumn(name = "ID_PEDIDO_COLETA")
	private PedidoColeta pedidoColeta;

	@OneToOne
	@JoinColumn(name = "ID_RECIBO_FRETE_CARRETEIRO")
	private ReciboFreteCarreteiro reciboFreteCarreteiro;
	
	
	@Column(name = "VL_TOTAL")
	private BigDecimal vlTotal;
	
	@Column(name = "VL_RATEIO_DE")
	private BigDecimal vlRateioDE;
	
	@Columns(columns = { @Column(name = "DH_RATEIO", nullable = false), @Column(name = "DH_RATEIO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhRateio;

	@OneToMany(mappedBy = "rateioFreteCarreteiroCE", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ParcelaRateioFreteCarreteiroCE> parcelas;

	public Long getIdRateioFreteCarreteiroCE() {
		return idRateioFreteCarreteiroCE;
	}

	public void setIdRateioFreteCarreteiroCE(Long idRateioFreteCarreteiroCE) {
		this.idRateioFreteCarreteiroCE = idRateioFreteCarreteiroCE;
	}

	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

	public List<ParcelaRateioFreteCarreteiroCE> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<ParcelaRateioFreteCarreteiroCE> parcelas) {
		this.parcelas = parcelas;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public BigDecimal getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}

	@Override
	public String toString() {
		return "RateioFreteCarreteiroCE [idRateioFreteCarreteiroCE=" + idRateioFreteCarreteiroCE + ", notaCredito=" + notaCredito + ", doctoServico="
				+ doctoServico + ", total=" + vlTotal + ", parcelas=" + parcelas + "]";
	}
	public PedidoColeta getPedidoColeta() {
		return pedidoColeta;
	}
	public void setPedidoColeta(PedidoColeta pedidoColeta) {
		this.pedidoColeta = pedidoColeta;
	}

	public BigDecimal getVlRateioDE() {
		return vlRateioDE;
	}

	public void setVlRateioDE(BigDecimal vlRateioDE) {
		this.vlRateioDE = vlRateioDE;
	}

	public ReciboFreteCarreteiro getReciboFreteCarreteiro() {
		return reciboFreteCarreteiro;
	}

	public void setReciboFreteCarreteiro(ReciboFreteCarreteiro reciboFreteCarreteiro) {
		this.reciboFreteCarreteiro = reciboFreteCarreteiro;
	}

	public DateTime getDhRateio() {
		return dhRateio;
	}

	public void setDhRateio(DateTime dhRateio) {
		this.dhRateio = dhRateio;
	}

}