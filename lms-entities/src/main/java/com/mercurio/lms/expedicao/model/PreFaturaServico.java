package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
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

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;

@Entity
@Table(name="PRE_FATURA_SERVICO")
@SequenceGenerator(name = "PRE_FATURA_SERVICO_SQ", sequenceName = "PRE_FATURA_SERVICO_SQ", allocationSize=1)
public class PreFaturaServico implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_PRE_FATURA_SERVICO", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRE_FATURA_SERVICO_SQ")
	private Long idPreFaturaServico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_COBRANCA", nullable = false)
	private Filial filialCobranca;
	
	@Column(name="NR_PRE_FATURA", length=9, nullable = false)
	private Long nrPreFatura;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE_TOMADOR", nullable = false)
	private Cliente clienteTomador;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DIVISAO_CLIENTE")
	private DivisaoCliente divisaoCliente;
	
	@Columns(columns = {@Column(name = "DH_GERACAO", nullable=false), @Column(name = "DH_GERACAO_TZR", nullable=false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhGeracao;
	
	@Column(name = "TP_SITUACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_PRE_FATURA") })
	private DomainValue tpSituacao;
	
	@Column(name = "VL_TOTAL", precision=16, scale=2, nullable=false)
	private BigDecimal vlTotal;

	@OneToMany(mappedBy="preFaturaServico",fetch=FetchType.LAZY)
	private List<PreFaturaServicoItem> preFaturaServicoItens;
	
	// LMS-7220
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_FINALIZACAO", nullable = true)
	private UsuarioLMS usuarioFinalizacao;
	
	public Long getIdPreFaturaServico() {
		return idPreFaturaServico;
	}
	public void setIdPreFaturaServico(Long idPreFaturaServico) {
		this.idPreFaturaServico = idPreFaturaServico;
	}

	public Filial getFilialCobranca() {
		return filialCobranca;
	}
	public void setFilialCobranca(Filial filialCobranca) {
		this.filialCobranca = filialCobranca;
	}

	public Long getNrPreFatura() {
		return nrPreFatura;
	}
	public void setNrPreFatura(Long nrPreFatura) {
		this.nrPreFatura = nrPreFatura;
	}

	public Cliente getClienteTomador() {
		return clienteTomador;
	}
	public void setClienteTomador(Cliente clienteTomador) {
		this.clienteTomador = clienteTomador;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}
	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public DateTime getDhGeracao() {
		return dhGeracao;
	}
	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public BigDecimal getVlTotal() {
		return vlTotal;
	}
	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}
	
	public List<PreFaturaServicoItem> getPreFaturaServicoItens() {
		return preFaturaServicoItens;
	}
	
	public void setPreFaturaServicoItens(List<PreFaturaServicoItem> preFaturaServicoItens) {
		this.preFaturaServicoItens = preFaturaServicoItens;
	}
	
	// LMS-7220
	public UsuarioLMS getUsuarioFinalizacao() {
		return usuarioFinalizacao;
	}
	
	// LMS-7220
	public void setUsuarioFinalizacao(UsuarioLMS usuarioFinalizacao) {
		this.usuarioFinalizacao = usuarioFinalizacao;
	}
	
}
