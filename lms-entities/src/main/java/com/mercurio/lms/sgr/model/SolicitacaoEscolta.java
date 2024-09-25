package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * LMS-6885 - Entidade para Solicitação de Escolta.
 * 
 * @author romulo.panassolo@cwi.com.br (Rômulo da Silva Panassolo)
 *
 */
@Entity
@Table(name = "SOLICITACAO_ESCOLTA")
@SequenceGenerator(name = "SOLICITACAO_ESCOLTA_SQ", sequenceName = "SOLICITACAO_ESCOLTA_SQ")
public class SolicitacaoEscolta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_SOLICITACAO_ESCOLTA", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOLICITACAO_ESCOLTA_SQ")
	private Long idSolicitacaoEscolta;

	@Columns(columns = {
			@Column(name = "DH_INCLUSAO", nullable = false),
			@Column(name = "DH_INCLUSAO_TZR ", nullable = false)
	})
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Columns(columns = {
			@Column(name = "DH_INICIO_PREVISTO", nullable = false),
			@Column(name = "DH_INICIO_PREVISTO_TZR ", nullable = false)
	})
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInicioPrevisto;

	@Columns(columns = {
			@Column(name = "DH_FIM_PREVISTO", nullable = false),
			@Column(name = "DH_FIM_PREVISTO_TZR ", nullable = false)
	})
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhFimPrevisto;

	@ManyToOne
	@JoinColumn(name = "ID_EXIGENCIA_ESCOLTA", nullable = false)
	private ExigenciaGerRisco exigenciaGerRisco;

	@ManyToOne
	@JoinColumn(name = "ID_CONTROLE_CARGA")
	private ControleCarga controleCarga;

	@ManyToOne
	@JoinColumn(name = "ID_FILIAL_SOLICITANTE", nullable = false)
	private Filial filialSolicitante;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_SOLICITANTE", nullable = false)
	private UsuarioLMS usuarioSolicitante;

	@Column(name = "NR_SOLICITACAO_ESCOLTA", nullable = false)
	private Long nrSolicitacaoEscolta;

	@Column(name = "TP_ORIGEM", nullable = false)
	@Type(
			type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType",
			parameters = { @Parameter(name = "domainName", value = "DM_TIPO_ORIGEM_SOLIC_ESCOLTA") }
	)
	private DomainValue tpOrigem;

	@ManyToOne
	@JoinColumn(name = "ID_FILIAL_ORIGEM")
	private Filial filialOrigem;

	@ManyToOne
	@JoinColumn(name = "ID_AEROPORTO_ORIGEM")
	private Aeroporto aeroportoOrigem;

	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE_ORIGEM")
	private Cliente clienteOrigem;

	@Column(name = "TP_DESTINO", nullable = false)
	@Type(
			type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType",
			parameters = { @Parameter(name = "domainName", value = "DM_TIPO_DESTINO_SOLIC_ESCOLTA") }
	)
	private DomainValue tpDestino;

	@ManyToOne
	@JoinColumn(name = "ID_FILIAL_DESTINO")
	private Filial filialDestino;

	@ManyToOne
	@JoinColumn(name = "ID_AEROPORTO_DESTINO")
	private Aeroporto aeroportoDestino;

	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE_DESTINO")
	private Cliente clienteDestino;

	@ManyToOne
	@JoinColumn(name = "ID_ROTA_COLETA_ENTREGA")
	private RotaColetaEntrega rotaColetaEntrega;

	@Column(name = "NR_KM_SOLICITACAO_ESCOLTA")
	private Long nrKmSolicitacaoEscolta;

	@Column(name = "DS_OBSERVACAO", length = 400)
	private String dsObservacao;

	@Column(name = "TP_SITUACAO_SOLIC_ESCOLTA", nullable = false)
	@Type(
			type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType",
			parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_SOLIC_ESCOLTA") }
	)
	private DomainValue tpSituacao;

	@ManyToOne
	@JoinColumn(name = "ID_TRANSPORTADO")
	private MeioTransporte meioTransporteByIdTransportado;

	@ManyToOne
	@JoinColumn(name = "ID_SEMI_REBOQUE")
	private MeioTransporte meioTransporteBySemiReboque;

	@ManyToOne
	@JoinColumn(name = "ID_MOTORISTA")
	private Motorista motorista;

	@ManyToOne
	@JoinColumn(name = "ID_NATUREZA_PRODUTO", nullable = false)
	private NaturezaProduto naturezaProduto;

	@ManyToOne
	@JoinColumn(name = "ID_CLINTE_ESCOLTA", nullable = false)
	private Cliente clienteEscolta;

	@Column(name = "VL_CARGA_CLIENTE", nullable = false)
	private BigDecimal vlCargaCliente;

	@Column(name = "VL_CARGA_TOTAL")
	private BigDecimal vlCargaTotal;

	@Column(name = "NR_NOTA_ATENDIMENTO")
	private Long nrNotaAtendimento;

	@Column(name = "DS_ORDEM_SERVICO", length = 30)
	private String dsOrdemServico;

	@ManyToOne
	@JoinColumn(name = "ID_FAIXA_VALOR_PGR")
	private FaixaDeValor faixaValorPgr;

	@Column(name = "QT_VIATURAS", nullable = false)
	private Long qtViaturas;

	@OneToMany(mappedBy = "solicitacaoEscolta")
	private List<SolicEscoltaHistorico> solicEscoltaHistorico;

	@OneToMany(mappedBy = "solicitacaoEscolta")
	private List<SolicEscoltaNegociacao> solicEscoltaNegociacao;

	public Long getIdSolicitacaoEscolta() {
		return idSolicitacaoEscolta;
	}

	public void setIdSolicitacaoEscolta(Long idSolicitacaoEscolta) {
		this.idSolicitacaoEscolta = idSolicitacaoEscolta;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhInicioPrevisto() {
		return dhInicioPrevisto;
	}

	public void setDhInicioPrevisto(DateTime dhInicioPrevisto) {
		this.dhInicioPrevisto = dhInicioPrevisto;
	}

	public DateTime getDhFimPrevisto() {
		return dhFimPrevisto;
	}

	public void setDhFimPrevisto(DateTime dhFimPrevisto) {
		this.dhFimPrevisto = dhFimPrevisto;
	}

	public ExigenciaGerRisco getExigenciaGerRisco() {
		return exigenciaGerRisco;
	}

	public void setExigenciaGerRisco(ExigenciaGerRisco exigenciaGerRisco) {
		this.exigenciaGerRisco = exigenciaGerRisco;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public Long getNrSolicitacaoEscolta() {
		return nrSolicitacaoEscolta;
	}

	public void setNrSolicitacaoEscolta(Long nrSolicitacaoEscolta) {
		this.nrSolicitacaoEscolta = nrSolicitacaoEscolta;
	}

	public DomainValue getTpOrigem() {
		return tpOrigem;
	}

	public void setTpOrigem(DomainValue tpOrigem) {
		this.tpOrigem = tpOrigem;
	}

	public Filial getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(Filial filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public Aeroporto getAeroportoOrigem() {
		return aeroportoOrigem;
	}

	public void setAeroportoOrigem(Aeroporto aeroportoOrigem) {
		this.aeroportoOrigem = aeroportoOrigem;
	}

	public Cliente getClienteOrigem() {
		return clienteOrigem;
	}

	public void setClienteOrigem(Cliente clienteOrigem) {
		this.clienteOrigem = clienteOrigem;
	}

	public DomainValue getTpDestino() {
		return tpDestino;
	}

	public void setTpDestino(DomainValue tpDestino) {
		this.tpDestino = tpDestino;
	}

	public Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	public Aeroporto getAeroportoDestino() {
		return aeroportoDestino;
	}

	public void setAeroportoDestino(Aeroporto aeroportoDestino) {
		this.aeroportoDestino = aeroportoDestino;
	}

	public Cliente getClienteDestino() {
		return clienteDestino;
	}

	public void setClienteDestino(Cliente clienteDestino) {
		this.clienteDestino = clienteDestino;
	}

	public RotaColetaEntrega getRotaColetaEntrega() {
		return rotaColetaEntrega;
	}

	public void setRotaColetaEntrega(RotaColetaEntrega rotaColetaEntrega) {
		this.rotaColetaEntrega = rotaColetaEntrega;
	}

	public Long getNrKmSolicitacaoEscolta() {
		return nrKmSolicitacaoEscolta;
	}

	public void setNrKmSolicitacaoEscolta(Long nrKmSolicitacaoEscolta) {
		this.nrKmSolicitacaoEscolta = nrKmSolicitacaoEscolta;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public MeioTransporte getMeioTransporteByIdTransportado() {
		return meioTransporteByIdTransportado;
	}

	public void setMeioTransporteByIdTransportado(MeioTransporte meioTransporteByIdTransportado) {
		this.meioTransporteByIdTransportado = meioTransporteByIdTransportado;
	}

	public MeioTransporte getMeioTransporteBySemiReboque() {
		return meioTransporteBySemiReboque;
	}

	public void setMeioTransporteBySemiReboque(MeioTransporte meioTransporteBySemiReboque) {
		this.meioTransporteBySemiReboque = meioTransporteBySemiReboque;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public NaturezaProduto getNaturezaProduto() {
		return naturezaProduto;
	}

	public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}

	public Cliente getClienteEscolta() {
		return clienteEscolta;
	}

	public void setClienteEscolta(Cliente clienteEscolta) {
		this.clienteEscolta = clienteEscolta;
	}

	public BigDecimal getVlCargaCliente() {
		return vlCargaCliente;
	}

	public void setVlCargaCliente(BigDecimal vlCargaCliente) {
		this.vlCargaCliente = vlCargaCliente;
	}

	public BigDecimal getVlCargaTotal() {
		return vlCargaTotal;
	}

	public void setVlCargaTotal(BigDecimal vlCargaTotal) {
		this.vlCargaTotal = vlCargaTotal;
	}

	public Long getNrNotaAtendimento() {
		return nrNotaAtendimento;
	}

	public void setNrNotaAtendimento(Long nrNotaAtendimento) {
		this.nrNotaAtendimento = nrNotaAtendimento;
	}

	public String getDsOrdemServico() {
		return dsOrdemServico;
	}

	public void setDsOrdemServico(String dsOrdemServico) {
		this.dsOrdemServico = dsOrdemServico;
	}

	public FaixaDeValor getFaixaValorPgr() {
		return faixaValorPgr;
	}

	public void setFaixaValorPgr(FaixaDeValor faixaValorPgr) {
		this.faixaValorPgr = faixaValorPgr;
	}

	public Long getQtViaturas() {
		return qtViaturas;
	}

	public void setQtViaturas(Long qtViaturas) {
		this.qtViaturas = qtViaturas;
	}

	public List<SolicEscoltaHistorico> getSolicEscoltaHistorico() {
		return solicEscoltaHistorico;
	}

	public void setSolicEscoltaHistorico(List<SolicEscoltaHistorico> solicEscoltaHistorico) {
		this.solicEscoltaHistorico = solicEscoltaHistorico;
	}

	public List<SolicEscoltaNegociacao> getSolicEscoltaNegociacao() {
		return solicEscoltaNegociacao;
	}

	public void setSolicEscoltaNegociacao(List<SolicEscoltaNegociacao> solicEscoltaNegociacao) {
		this.solicEscoltaNegociacao = solicEscoltaNegociacao;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idSolicitacaoEscolta)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof SolicitacaoEscolta)) {
			return false;
		}
		SolicitacaoEscolta cast = (SolicitacaoEscolta) other;
		return new EqualsBuilder()
				.append(idSolicitacaoEscolta, cast.idSolicitacaoEscolta)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idSolicitacaoEscolta)
				.toHashCode();
	}

	public Filial getFilialSolicitante() {
		return filialSolicitante;
	}

	public void setFilialSolicitante(Filial filialSolicitante) {
		this.filialSolicitante = filialSolicitante;
	}

	public UsuarioLMS getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(UsuarioLMS usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

}
