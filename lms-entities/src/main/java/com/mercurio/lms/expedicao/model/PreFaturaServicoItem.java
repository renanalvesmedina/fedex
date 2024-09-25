package com.mercurio.lms.expedicao.model;

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

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name="PRE_FATURA_SERVICO_ITEM")
@SequenceGenerator(name = "PRE_FATURA_SERVICO_ITEM_SQ", sequenceName = "PRE_FATURA_SERVICO_ITEM_SQ", allocationSize=1)
public class PreFaturaServicoItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_PRE_FATURA_SERVICO_ITEM", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRE_FATURA_SERVICO_ITEM_SQ")
	private Long idPreFaturaServicoItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PRE_FATURA_SERVICO", nullable = false)
	private PreFaturaServico preFaturaServico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_ORDEM_SERVICO_ITEM")
	private OrdemServicoItem ordemServicoItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_SERVICO_GERACAO_AUTOMATICA")
	private ServicoGeracaoAutomatica servicoGeracaoAutomatica;
	
	@Column(name = "TP_SITUACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_ITEM_PRE_FATURA") })
	private DomainValue tpSituacao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MOTIVO_PRE_FATURA_SERV")
	private MotivoPreFaturaServico motivoPreFaturaServico;
	
	@Column(name = "VL_SERVICO_COM_DESCONTO", precision=16, scale=2)
	private BigDecimal vlServicoComDesconto;

	public Long getIdPreFaturaServicoItem() {
		return idPreFaturaServicoItem;
	}
	public void setIdPreFaturaServicoItem(Long idPreFaturaServicoItem) {
		this.idPreFaturaServicoItem = idPreFaturaServicoItem;
	}

	public PreFaturaServico getPreFaturaServico() {
		return preFaturaServico;
	}
	public void setPreFaturaServico(PreFaturaServico preFaturaServico) {
		this.preFaturaServico = preFaturaServico;
	}

	public OrdemServicoItem getOrdemServicoItem() {
		return ordemServicoItem;
	}
	public void setOrdemServicoItem(OrdemServicoItem ordemServicoItem) {
		this.ordemServicoItem = ordemServicoItem;
	}

	public ServicoGeracaoAutomatica getServicoGeracaoAutomatica() {
		return servicoGeracaoAutomatica;
	}
	public void setServicoGeracaoAutomatica(
			ServicoGeracaoAutomatica servicoGeracaoAutomatica) {
		this.servicoGeracaoAutomatica = servicoGeracaoAutomatica;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	
	public MotivoPreFaturaServico getMotivoPreFaturaServico() {
		return motivoPreFaturaServico;
	}
	
	public void setMotivoPreFaturaServico(
			MotivoPreFaturaServico motivoPreFaturaServico) {
		this.motivoPreFaturaServico = motivoPreFaturaServico;
	}
	
	public BigDecimal getVlServicoComDesconto() {
		return vlServicoComDesconto;
	}
	
	public void setVlServicoComDesconto(BigDecimal vlServicoComDesconto) {
		this.vlServicoComDesconto = vlServicoComDesconto;
	}
	
}
