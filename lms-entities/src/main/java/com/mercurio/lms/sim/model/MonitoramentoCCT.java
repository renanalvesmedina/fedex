package com.mercurio.lms.sim.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.vendas.model.Cliente;


@Entity
@Table(name="MONITORAMENTO_CCT")
@SequenceGenerator(name="SQ_MONITORAMENTO_CCT", sequenceName="MONITORAMENTO_CCT_SQ", allocationSize=1)
public class MonitoramentoCCT implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_MONITORAMENTO_CCT")
	@Column(name="ID_MONITORAMENTO_CCT", nullable=false)
	private Long idMonitoramentoCCT;

	@Column(name="NR_CHAVE", length=44, nullable=false)
	private String nrChave;

	@OneToOne
	@JoinColumn(name="ID_CLIENTE_REM", nullable=false)
	private Cliente clienteRemetente;

	@OneToOne
	@JoinColumn(name="ID_CLIENTE_DEST")
	private Cliente clienteDestinatario;

	@Column(name = "BL_RECOLHE_ICMS", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blRecolheICMS;

	@Column(name = "BL_CONF_AGENDAMENTO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blConfiguraAgendamento;

	@Column(name = "TP_SITUACAO_NF_CCT", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_NF_CCT") })
	private DomainValue tpSituacaoNotaFiscalCCT;

	@OneToOne
	@JoinColumn(name="ID_DOCTO_SERVICO")
	private DoctoServico doctoServico;

	@OneToOne
	@JoinColumn(name="ID_PEDIDO_COLETA")
	private PedidoColeta pedidoColeta;

	@Column(name="NR_CHAVE_DEVOL", length=44)
	private String nrChaveDevolucao;

	@Column(name = "DT_ICMS_SOLIC")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtSolicitacaoICMS;

	@Column(name = "DT_ICMS_PAGO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtPagamentoICMS;
	
	@Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = true), @Column(name = "DH_INCLUSAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	public Long getIdMonitoramentoCCT() {
		return idMonitoramentoCCT;
	}

	public void setIdMonitoramentoCCT(Long idMonitoramentoCCT) {
		this.idMonitoramentoCCT = idMonitoramentoCCT;
	}

	public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

	public Cliente getClienteRemetente() {
		return clienteRemetente;
	}

	public void setClienteRemetente(Cliente clienteRemetente) {
		this.clienteRemetente = clienteRemetente;
	}

	public Cliente getClienteDestinatario() {
		return clienteDestinatario;
	}

	public void setClienteDestinatario(Cliente clienteDestinatario) {
		this.clienteDestinatario = clienteDestinatario;
	}

	public Boolean getBlRecolheICMS() {
		return blRecolheICMS == null ? false : blRecolheICMS;
	}

	public void setBlRecolheICMS(Boolean blRecolheICMS) {
		this.blRecolheICMS = blRecolheICMS == null ? false : blRecolheICMS;
	}

	public Boolean getBlConfiguraAgendamento() {
		return blConfiguraAgendamento == null ? false : blConfiguraAgendamento;
	}

	public void setBlConfiguraAgendamento(Boolean blConfiguraAgendamento) {
		this.blConfiguraAgendamento = blConfiguraAgendamento == null ? false : blConfiguraAgendamento;
	}

	public DomainValue getTpSituacaoNotaFiscalCCT() {
		return tpSituacaoNotaFiscalCCT;
	}

	public void setTpSituacaoNotaFiscalCCT(DomainValue tpSituacaoNotaFiscalCCT) {
		this.tpSituacaoNotaFiscalCCT = tpSituacaoNotaFiscalCCT;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public PedidoColeta getPedidoColeta() {
		return pedidoColeta;
	}

	public void setPedidoColeta(PedidoColeta pedidoColeta) {
		this.pedidoColeta = pedidoColeta;
	}

	public String getNrChaveDevolucao() {
		return nrChaveDevolucao;
	}

	public void setNrChaveDevolucao(String nrChaveDevolucao) {
		this.nrChaveDevolucao = nrChaveDevolucao;
	}

	public YearMonthDay getDtSolicitacaoICMS() {
		return dtSolicitacaoICMS;
	}

	public void setDtSolicitacaoICMS(YearMonthDay dtSolicitacaoICMS) {
		this.dtSolicitacaoICMS = dtSolicitacaoICMS;
	}

	public YearMonthDay getDtPagamentoICMS() {
		return dtPagamentoICMS;
	}

	public void setDtPagamentoICMS(YearMonthDay dtPagamentoICMS) {
		this.dtPagamentoICMS = dtPagamentoICMS;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}


}
