package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "AVERBACAO")
@SequenceGenerator(name = "AVERBACAO_SQ", sequenceName = "AVERBACAO_SQ")
public class Averbacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AVERBACAO_SQ")
	@Column(name = "ID_AVERBACAO", nullable = false)
	private Long idAverbacao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_SEGURO", nullable = false)
	private TipoSeguro tipoSeguro;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_SEGURADORA", nullable = false)
	private Seguradora seguradora;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MUNICIPIO_ORIGEM", nullable = false)
	private Municipio municipioOrigem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MUNICIPIO_DESTINO", nullable = false)
	private Municipio municipioDestino;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MEIO_TRANSPORTE", nullable = true)
	private MeioTransporte meioTransporte;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CORRETORA", nullable = true)
	private ReguladoraSeguro corretora;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE", nullable = false)
	private Cliente cliente;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_ORIGEM", nullable = false)
	private Filial filialOrigem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_DESTINO", nullable = false)
	private Filial filialDestino;
	
	@Column(name = "TP_MODAL", length = 1, nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_MODAL") })
	private DomainValue tpModal;
	
	@Column(name = "TP_ABRANGENCIA", length = 1, nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_ABRANGENCIA") })
	private DomainValue tpAbrangencia;
	
	@Column(name = "TP_FRETE", length = 1, nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_TIPO_FRETE") })
	private DomainValue tpFrete;
	
	@Column(name = "DT_VIAGEM", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtViagem;
	
	@Column(name = "VL_ESTIMADO", nullable = false)
	private BigDecimal vlEstimado;
	
	@Column(name = "PS_TOTAL", nullable = false)
	private BigDecimal psTotal;
	
	@Column(name = "DS_NF", length = 1500, nullable = true)
	private String dsNF;
	
	@Column(name = "DS_CONTINGENCIA", length = 1500, nullable = false)
	private String dsContingencia;
	
	@Column(name = "OB_AVERBACAO", length = 1500, nullable = true)
	private String obAverbacao;
	
	
	public Long getIdAverbacao() {
		return idAverbacao;
	}
	
	public void setIdAverbacao(Long idAverbacao) {
		this.idAverbacao = idAverbacao;
	}
	
	public TipoSeguro getTipoSeguro() {
		return tipoSeguro;
	}
	
	public void setTipoSeguro(TipoSeguro tipoSeguro) {
		this.tipoSeguro = tipoSeguro;
	}
	
	public Seguradora getSeguradora() {
		return seguradora;
	}
	
	public void setSeguradora(Seguradora seguradora) {
		this.seguradora = seguradora;
	}
	
	public Municipio getMunicipioOrigem() {
		return municipioOrigem;
	}
	
	public void setMunicipioOrigem(
			Municipio municipioOrigem) {
		this.municipioOrigem = municipioOrigem;
	}
	
	public Municipio getMunicipioDestino() {
		return municipioDestino;
	}
	
	public void setMunicipioDestino(
			Municipio municipioDestino) {
		this.municipioDestino = municipioDestino;
	}
	
	public MeioTransporte getMeioTransporte() {
		return meioTransporte;
	}
	
	public void setMeioTransporte(MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
	}
	
	public ReguladoraSeguro getCorretora() {
		return corretora;
	}
	
	public void setCorretora(ReguladoraSeguro corretora) {
		this.corretora = corretora;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
	
	public DomainValue getTpModal() {
		return tpModal;
	}
	
	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}
	
	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}
	
	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}
	
	public DomainValue getTpFrete() {
		return tpFrete;
	}
	
	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}
		
	public YearMonthDay getDtViagem() {
		return dtViagem;
	}

	public void setDtViagem(YearMonthDay dtViagem) {
		this.dtViagem = dtViagem;
	}

	public BigDecimal getVlEstimado() {
		return vlEstimado;
	}
	
	public void setVlEstimado(BigDecimal vlEstimado) {
		this.vlEstimado = vlEstimado;
	}
	
	public BigDecimal getPsTotal() {
		return psTotal;
	}
	
	public void setPsTotal(BigDecimal psTotal) {
		this.psTotal = psTotal;
	}
	
	public String getDsNF() {
		return dsNF;
	}
	
	public void setDsNF(String dsNF) {
		this.dsNF = dsNF;
	}
	
	public String getDsContingencia() {
		return dsContingencia;
	}
	
	public void setDsContingencia(String dsContingencia) {
		this.dsContingencia = dsContingencia;
	}
	
	public String getObAverbacao() {
		return obAverbacao;
	}
	
	public void setObAverbacao(String obAverbacao) {
		this.obAverbacao = obAverbacao;
	}
	
}
