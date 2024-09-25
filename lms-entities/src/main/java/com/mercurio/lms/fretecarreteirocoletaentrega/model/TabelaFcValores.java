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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "TABELA_FC_VALORES")
@SequenceGenerator(name = "TABELA_FC_VALORES_SQ", sequenceName = "TABELA_FC_VALORES_SQ", allocationSize = 1)
public class TabelaFcValores implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TABELA_FC_VALORES_SQ")
	@Column(name = "ID_TABELA_FC_VALORES", nullable = false)
	private Long idTabelaFcValores;

	@ManyToOne
	@JoinColumn(name = "ID_TABELA_FRETE_CARRETEIRO_CE", nullable = false)
	private TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe;

	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "ID_PROPRIETARIO")
	private Proprietario proprietario;

	@ManyToOne
	@JoinColumn(name = "ID_ROTA_COLETA_ENTREGA")
	private RotaColetaEntrega rotaColetaEntrega;

	@ManyToOne
	@JoinColumn(name = "ID_MUNICIPIO")
	private Municipio municipio;

	@ManyToOne
	@JoinColumn(name = "ID_MEIO_TRANSPORTE")
	private MeioTransporte meioTransporte;

	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	@Column(name = "DT_CRIACAO")
	private YearMonthDay dtCriacao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_CRIACAO")
	private UsuarioLMS usuarioCriacao;

	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	@Column(name = "DT_EXCLUSAO")
	private YearMonthDay dtExclusao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_EXCLUSAO")
	private UsuarioLMS usuarioExclusao;

	@ManyToOne
	@JoinColumn(name = "ID_TIPO_MEIO_TRANSPORTE")
	private TipoMeioTransporte tipoMeioTransporte;

	@Column(name = "VL_CONHECIMENTO")
	private BigDecimal vlConhecimento;

	@Column(name = "VL_EVENTO")
	private BigDecimal vlEvento;

	@Column(name = "VL_VOLUME")
	private BigDecimal vlVolume;

	@Column(name = "VL_PALETE")
	private BigDecimal vlPalete;

	@Column(name = "VL_TRANSFERENCIA")
	private BigDecimal vlTransferencia;

	@Column(name = "VL_AJUDANTE")
	private BigDecimal vlAjudante;

	@Column(name = "VL_HORA")
	private BigDecimal vlHora;

	@Column(name = "VL_DIARIA")
	private BigDecimal vlDiaria;

	@Column(name = "VL_PRE_DIARIA")
	private BigDecimal vlPreDiaria;

	@Column(name = "VL_DEDICADO")
	private BigDecimal vlDedicado;

	@Column(name = "VL_PERNOITE")
	private BigDecimal vlPernoite;

	@Column(name = "VL_CAPATAZIA_CLIENTE")
	private BigDecimal vlCapataziaCliente;

	@Column(name = "VL_LOCACAO_CARRETA")
	private BigDecimal vlLocacaoCarreta;

	@Column(name = "VL_PREMIO")
	private BigDecimal vlPremio;

	@Column(name = "VL_KM_EXCEDENTE")
	private BigDecimal vlKmExcedente;

	@Column(name = "VL_FRETE_MINIMO")
	private BigDecimal vlFreteMinimo;

	@Column(name = "PC_FRETE")
	private BigDecimal pcFrete;

	@Column(name = "VL_MERC_MINIMO")
	private BigDecimal vlmercadoriaMinimo;

	@Column(name = "PC_MERCADORIA")
	private BigDecimal pcMercadoria;

	@Column(name = "QT_AJUDANTE")
	private Integer qtAjudante;

	@Column(name = "PC_FRETE_LIQ")
	private BigDecimal pcFreteLiq;

	@Column(name = "VL_FRETE_MINIMO_LIQ")
	private BigDecimal vlFreteMinimoLiq;

	@Column(name = "BL_GERAL", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blGeral;

	@Column(name = "TP_SITUACAO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_SITUACAO") })
	private DomainValue tpSituacao;

	@Column(name = "BL_TIPO", length = 2)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_TAB_FC_VALORES") })
	private DomainValue blTipo;

	@OrderBy("psInicial")
	@OneToMany(mappedBy = "tabelaFcValores", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<TabelaFcFaixaPeso> listTabelaFcFaixaPeso;

	public Long getIdTabelaFcValores() {
		return this.idTabelaFcValores;
	}

	public void setIdTabelaFcValores(Long idTabelaFcValores) {
		this.idTabelaFcValores = idTabelaFcValores;
	}

	public TabelaFreteCarreteiroCe getTabelaFreteCarreteiroCe() {
		return this.tabelaFreteCarreteiroCe;
	}

	public void setTabelaFreteCarreteiroCe(
			TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		this.tabelaFreteCarreteiroCe = tabelaFreteCarreteiroCe;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Municipio getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public YearMonthDay getDtCriacao() {
		return this.dtCriacao;
	}

	public void setDtCriacao(YearMonthDay dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public UsuarioLMS getUsuarioCriacao() {
		return this.usuarioCriacao;
	}

	public void setUsuarioCriacao(UsuarioLMS usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public BigDecimal getVlConhecimento() {
		return this.vlConhecimento;
	}

	public void setVlConhecimento(BigDecimal vlConhecimento) {
		this.vlConhecimento = vlConhecimento;
	}

	public BigDecimal getVlEvento() {
		return this.vlEvento;
	}

	public void setVlEvento(BigDecimal vlEvento) {
		this.vlEvento = vlEvento;
	}

	public BigDecimal getVlVolume() {
		return this.vlVolume;
	}

	public void setVlVolume(BigDecimal vlVolume) {
		this.vlVolume = vlVolume;
	}

	public BigDecimal getVlPalete() {
		return this.vlPalete;
	}

	public void setVlPalete(BigDecimal vlPalete) {
		this.vlPalete = vlPalete;
	}

	public BigDecimal getVlTransferencia() {
		return this.vlTransferencia;
	}

	public void setVlTransferencia(BigDecimal vlTransferencia) {
		this.vlTransferencia = vlTransferencia;
	}

	public BigDecimal getVlAjudante() {
		return this.vlAjudante;
	}

	public void setVlAjudante(BigDecimal vlAjudante) {
		this.vlAjudante = vlAjudante;
	}

	public BigDecimal getVlHora() {
		return this.vlHora;
	}

	public void setVlHora(BigDecimal vlHora) {
		this.vlHora = vlHora;
	}

	public BigDecimal getVlDiaria() {
		return this.vlDiaria;
	}

	public void setVlDiaria(BigDecimal vlDiaria) {
		this.vlDiaria = vlDiaria;
	}

	public BigDecimal getVlPreDiaria() {
		return this.vlPreDiaria;
	}

	public void setVlPreDiaria(BigDecimal vlPreDiaria) {
		this.vlPreDiaria = vlPreDiaria;
	}

	public BigDecimal getVlDedicado() {
		return this.vlDedicado;
	}

	public void setVlDedicado(BigDecimal vlDedicado) {
		this.vlDedicado = vlDedicado;
	}

	public BigDecimal getVlPernoite() {
		return this.vlPernoite;
	}

	public void setVlPernoite(BigDecimal vlPernoite) {
		this.vlPernoite = vlPernoite;
	}

	public BigDecimal getVlCapataziaCliente() {
		return this.vlCapataziaCliente;
	}

	public void setVlCapataziaCliente(BigDecimal vlCapataziaCliente) {
		this.vlCapataziaCliente = vlCapataziaCliente;
	}

	public BigDecimal getVlLocacaoCarreta() {
		return this.vlLocacaoCarreta;
	}

	public void setVlLocacaoCarreta(BigDecimal vlLocacaoCarreta) {
		this.vlLocacaoCarreta = vlLocacaoCarreta;
	}

	public BigDecimal getVlPremio() {
		return this.vlPremio;
	}

	public void setVlPremio(BigDecimal vlPremio) {
		this.vlPremio = vlPremio;
	}

	public BigDecimal getVlKmExcedente() {
		return this.vlKmExcedente;
	}

	public void setVlKmExcedente(BigDecimal vlKmExcedente) {
		this.vlKmExcedente = vlKmExcedente;
	}

	public BigDecimal getVlFreteMinimo() {
		return this.vlFreteMinimo;
	}

	public void setVlFreteMinimo(BigDecimal vlFreteMinimo) {
		this.vlFreteMinimo = vlFreteMinimo;
	}

	public BigDecimal getPcFrete() {
		return this.pcFrete;
	}

	public void setPcFrete(BigDecimal pcFrete) {
		this.pcFrete = pcFrete;
	}

	public BigDecimal getVlmercadoriaMinimo() {
		return this.vlmercadoriaMinimo;
	}

	public void setVlmercadoriaMinimo(BigDecimal vlmercadoriaMinimo) {
		this.vlmercadoriaMinimo = vlmercadoriaMinimo;
	}

	public BigDecimal getPcMercadoria() {
		return this.pcMercadoria;
	}

	public void setPcMercadoria(BigDecimal pcMercadoria) {
		this.pcMercadoria = pcMercadoria;
	}

	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public RotaColetaEntrega getRotaColetaEntrega() {
		return rotaColetaEntrega;
	}

	public void setRotaColetaEntrega(RotaColetaEntrega rotaColetaEntrega) {
		this.rotaColetaEntrega = rotaColetaEntrega;
	}

	public YearMonthDay getDtExclusao() {
		return dtExclusao;
	}

	public void setDtExclusao(YearMonthDay dtExclusao) {
		this.dtExclusao = dtExclusao;
	}

	public UsuarioLMS getUsuarioExclusao() {
		return usuarioExclusao;
	}

	public void setUsuarioExclusao(UsuarioLMS usuarioExclusao) {
		this.usuarioExclusao = usuarioExclusao;
	}

	public List<TabelaFcFaixaPeso> getListTabelaFcFaixaPeso() {
		return listTabelaFcFaixaPeso;
	}

	public void setListTabelaFcFaixaPeso(
			List<TabelaFcFaixaPeso> listTabelaFcFaixaPeso) {
		this.listTabelaFcFaixaPeso = listTabelaFcFaixaPeso;
	}

	public MeioTransporte getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public TipoMeioTransporte getTipoMeioTransporte() {
		return tipoMeioTransporte;
	}

	public void setTipoMeioTransporte(TipoMeioTransporte tipoMeioTransporte) {
		this.tipoMeioTransporte = tipoMeioTransporte;
	}

	public DomainValue getBlGeral() {
		return blGeral;
	}

	public void setBlGeral(DomainValue blGeral) {
		this.blGeral = blGeral;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Integer getQtAjudante() {
		return qtAjudante;
	}

	public void setQtAjudante(Integer qtAjudante) {
		this.qtAjudante = qtAjudante;
	}

	public DomainValue getBlTipo() {
		return blTipo;
	}
	
	public void setBlTipo(DomainValue blTipo) {
		this.blTipo = blTipo;
	}

	public BigDecimal getPcFreteLiq() {
		return pcFreteLiq;
	}

	public void setPcFreteLiq(BigDecimal pcFreteLiq) {
		this.pcFreteLiq = pcFreteLiq;
	}

	public BigDecimal getVlFreteMinimoLiq() {
		return vlFreteMinimoLiq;
	}

	public void setVlFreteMinimoLiq(BigDecimal vlFreteMinimoLiq) {
		this.vlFreteMinimoLiq = vlFreteMinimoLiq;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idTabelaFcValores == null) ? 0 : idTabelaFcValores
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof TabelaFcValores)) {
			return false;
		}

		TabelaFcValores castOther = (TabelaFcValores) obj;

		return new EqualsBuilder().append(this.getIdTabelaFcValores(),
				castOther.getIdTabelaFcValores()).isEquals();
	}
}