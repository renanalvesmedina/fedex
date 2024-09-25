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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name = "TABELA_FRETE_CARRETEIRO_CE")
@SequenceGenerator(name = "TABELA_FRETE_CARRETEIRO_CE_SQ", sequenceName = "TABELA_FRETE_CARRETEIRO_CE_SQ", allocationSize = 1)
public class TabelaFreteCarreteiroCe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TABELA_FRETE_CARRETEIRO_CE_SQ")
	@Column(name = "ID_TABELA_FRETE_CARRETEIRO_CE", nullable = false)
	private Long idTabelaFreteCarreteiroCe;

	@Column(name = "NR_TABELA_FRETE_CARRETEIRO_CE", nullable = false, length = 10)
	private Long nrTabelaFreteCarreteiroCe;

	@Columns(columns = { @Column(name = "DH_VIGENCIA_INICIAL"),
			@Column(name = "DH_VIGENCIA_INICIAL_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhVigenciaInicial;

	@Column(name = "DH_VIGENCIA_INICIAL_TZR", nullable = false, insertable = false, updatable = false)
	private String dhVigenciaInicialTimeZone;

	@Columns(columns = { @Column(name = "DH_VIGENCIA_FINAL"),
			@Column(name = "DH_VIGENCIA_FINAL_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhVigenciaFinal;

	@Column(name = "DH_VIGENCIA_FINAL_TZR", nullable = false, insertable = false, updatable = false)
	private String dhVigenciaFinalTimeZone;

	@Column(name = "DT_CRIACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtCriacao;

	@Column(name = "DT_ATUALIZACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtAtualizacao;

	@Column(name = "BL_DOMINGO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blDomingo;

	@Column(name = "BL_SEGUNDA", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blSegunda;

	@Column(name = "BL_TERCA", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blTerca;

	@Column(name = "BL_QUARTA", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blQuarta;

	@Column(name = "BL_QUINTA", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blQuinta;

	@Column(name = "BL_SEXTA", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blSexta;

	@Column(name = "BL_SABADO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blSabado;

	@Column(name = "BL_DEDICADO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blDedicado;

	@Column(name = "BL_DESCONTA_FRETE", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blDescontaFrete;

	@Column(name = "TP_VINCULO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_VINCULO_TAB_FRETE_CE") })
	private DomainValue tpVinculo;

	@Column(name = "TP_OPERACAO", length = 2, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_OPERACAO_TAB_FRETE_CE") })
	private DomainValue tpOperacao;

	@Column(name = "TP_MODAL", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_MODAL_TAB_FRETE_CE") })
	private DomainValue tpModal;
	
	@Column(name = "TP_PESO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_PESO_CALC_NOTA_CRED") })
	private DomainValue tpPeso;

	@Column(name = "OB_TABELA_FRETE", length = 500)
	private String obTabelaFrete;

	@Column(name = "PC_PREMIO_CTE")
	private BigDecimal pcPremioCte;
	 
	@Column(name = "PC_PREMIO_EVENTO")
	private BigDecimal pcPremioEvento;
	
	@Column(name = "PC_PREMIO_DIARIA")
	private BigDecimal pcPremioDiaria;

	@Column(name = "PC_PREMIO_VOLUME")
	private BigDecimal pcPremioVolume;
	
	@Column(name = "PC_PREMIO_SAIDA")
	private BigDecimal pcPremioSaida;
	
	@Column(name = "PC_PREMIO_FRETE_BRUTO")
	private BigDecimal pcPremioFreteBruto;
	
	@Column(name = "PC_PREMIO_FRETE_LIQ")
	private BigDecimal pcPremioFreteLiq;
	
	@Column(name = "PC_PREMIO_MERCADORIA") 
	private BigDecimal pcPremioMercadoria;
	
	@ManyToOne
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	private Filial filial;

	@ManyToOne
	@JoinColumn(name = "ID_TABELA_CLONADA")
	private TabelaFreteCarreteiroCe tabelaClonada;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_CRIACAO")
	private UsuarioLMS usuarioCriacao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO")
	private UsuarioLMS usuarioAlteracao;

	@OneToMany(mappedBy = "tabelaFreteCarreteiroCe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<TabelaFcValores> listTabelaFcValores;

	public Long getIdTabelaFreteCarreteiroCe() {
		return idTabelaFreteCarreteiroCe;
	}

	public void setIdTabelaFreteCarreteiroCe(Long idTabelaFreteCarreteiroCe) {
		this.idTabelaFreteCarreteiroCe = idTabelaFreteCarreteiroCe;
	}

	public Long getNrTabelaFreteCarreteiroCe() {
		return nrTabelaFreteCarreteiroCe;
	}

	public void setNrTabelaFreteCarreteiroCe(Long nrTabelaFreteCarreteiroCe) {
		this.nrTabelaFreteCarreteiroCe = nrTabelaFreteCarreteiroCe;
	}

	public YearMonthDay getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(YearMonthDay dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public YearMonthDay getDtAtualizacao() {
		return dtAtualizacao;
	}

	public void setDtAtualizacao(YearMonthDay dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}

	public DomainValue getBlDomingo() {
		return blDomingo;
	}

	public void setBlDomingo(DomainValue blDomingo) {
		this.blDomingo = blDomingo;
	}

	public DomainValue getBlSegunda() {
		return blSegunda;
	}

	public void setBlSegunda(DomainValue blSegunda) {
		this.blSegunda = blSegunda;
	}

	public DomainValue getBlTerca() {
		return blTerca;
	}

	public void setBlTerca(DomainValue blTerca) {
		this.blTerca = blTerca;
	}

	public DomainValue getBlQuarta() {
		return blQuarta;
	}

	public void setBlQuarta(DomainValue blQuarta) {
		this.blQuarta = blQuarta;
	}

	public DomainValue getBlQuinta() {
		return blQuinta;
	}

	public void setBlQuinta(DomainValue blQuinta) {
		this.blQuinta = blQuinta;
	}

	public DomainValue getBlSexta() {
		return blSexta;
	}

	public void setBlSexta(DomainValue blSexta) {
		this.blSexta = blSexta;
	}

	public DomainValue getBlSabado() {
		return blSabado;
	}

	public void setBlSabado(DomainValue blSabado) {
		this.blSabado = blSabado;
	}

	public DomainValue getBlDedicado() {
		return blDedicado;
	}

	public void setBlDedicado(DomainValue blDedicado) {
		this.blDedicado = blDedicado;
	}

	public DomainValue getBlDescontaFrete() {
		return blDescontaFrete;
	}

	public void setBlDescontaFrete(DomainValue blDescontaFrete) {
		this.blDescontaFrete = blDescontaFrete;
	}

	public DomainValue getTpVinculo() {
		return tpVinculo;
	}

	public void setTpVinculo(DomainValue tpVinculo) {
		this.tpVinculo = tpVinculo;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}
	
	public DomainValue getTpPeso() {
		return tpPeso;
	}

	public void setTpPeso(DomainValue tpPeso) {
		this.tpPeso = tpPeso;
	}

	public String getObTabelaFrete() {
		return obTabelaFrete;
	}

	public void setObTabelaFrete(String obTabelaFrete) {
		this.obTabelaFrete = obTabelaFrete;
	}

	public BigDecimal getPcPremioCte() {
		return pcPremioCte;
	}

	public void setPcPremioCte(BigDecimal pcPremioCte) {
		this.pcPremioCte = pcPremioCte;
	}

	public BigDecimal getPcPremioEvento() {
		return pcPremioEvento;
	}

	public void setPcPremioEvento(BigDecimal pcPremioEvento) {
		this.pcPremioEvento = pcPremioEvento;
	}

	public BigDecimal getPcPremioDiaria() {
		return pcPremioDiaria;
	}

	public void setPcPremioDiaria(BigDecimal pcPremioDiaria) {
		this.pcPremioDiaria = pcPremioDiaria;
	}

	public BigDecimal getPcPremioVolume() {
		return pcPremioVolume;
	}

	public void setPcPremioVolume(BigDecimal pcPremioVolume) {
		this.pcPremioVolume = pcPremioVolume;
	}

	public BigDecimal getPcPremioSaida() {
		return pcPremioSaida;
	}

	public void setPcPremioSaida(BigDecimal pcPremioSaida) {
		this.pcPremioSaida = pcPremioSaida;
	}

	public BigDecimal getPcPremioFreteBruto() {
		return pcPremioFreteBruto;
	}

	public void setPcPremioFreteBruto(BigDecimal pcPremioFreteBruto) {
		this.pcPremioFreteBruto = pcPremioFreteBruto;
	}

	public BigDecimal getPcPremioFreteLiq() {
		return pcPremioFreteLiq;
	}

	public void setPcPremioFreteLiq(BigDecimal pcPremioFreteLiq) {
		this.pcPremioFreteLiq = pcPremioFreteLiq;
	}

	public BigDecimal getPcPremioMercadoria() {
		return pcPremioMercadoria;
	}

	public void setPcPremioMercadoria(BigDecimal pcPremioMercadoria) {
		this.pcPremioMercadoria = pcPremioMercadoria;
	}
	
	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public TabelaFreteCarreteiroCe getTabelaClonada() {
		return tabelaClonada;
	}

	public void setTabelaClonada(TabelaFreteCarreteiroCe tabelaClonada) {
		this.tabelaClonada = tabelaClonada;
	}

	public UsuarioLMS getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(UsuarioLMS usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public UsuarioLMS getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioLMS usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public List<TabelaFcValores> getListTabelaFcValores() {
		return listTabelaFcValores;
	}

	public void setListTabelaFcValores(List<TabelaFcValores> listTabelaFcValores) {
		this.listTabelaFcValores = listTabelaFcValores;
	}

	public DateTime getDhVigenciaInicial() {
		return dhVigenciaInicial;
	}

	public void setDhVigenciaInicial(DateTime dhVigenciaInicial) {
		this.dhVigenciaInicial = dhVigenciaInicial;
	}

	public DateTime getDhVigenciaFinal() {
		return dhVigenciaFinal;
	}

	public void setDhVigenciaFinal(DateTime dhVigenciaFinal) {
		this.dhVigenciaFinal = dhVigenciaFinal;
	}

	public String getDhVigenciaInicialTimeZone() {
		return dhVigenciaInicialTimeZone;
	}

	public void setDhVigenciaInicialTimeZone(String dhVigenciaInicialTimeZone) {
		this.dhVigenciaInicialTimeZone = dhVigenciaInicialTimeZone;
	}

	public String getDhVigenciaFinalTimeZone() {
		return dhVigenciaFinalTimeZone;
	}

	public void setDhVigenciaFinalTimeZone(String dhVigenciaFinalTimeZone) {
		this.dhVigenciaFinalTimeZone = dhVigenciaFinalTimeZone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idTabelaFreteCarreteiroCe == null) ? 0
						: idTabelaFreteCarreteiroCe.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof TabelaFreteCarreteiroCe)) {
			return false;
		}

		TabelaFreteCarreteiroCe castOther = (TabelaFreteCarreteiroCe) obj;

		return new EqualsBuilder().append(this.getIdTabelaFreteCarreteiroCe(),
				castOther.getIdTabelaFreteCarreteiroCe()).isEquals();
	}

}