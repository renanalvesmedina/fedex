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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;


@Entity
@Table(name="ORDEM_SERVICO_ITEM")
@SequenceGenerator(name = "ORDEM_SERVICO_ITEM_SEQ", sequenceName = "ORDEM_SERVICO_ITEM_SQ", allocationSize=1)
public class OrdemServicoItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_ORDEM_SERVICO_ITEM", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDEM_SERVICO_ITEM_SEQ")
	private Long idOrdemServicoItem;
	
	@Column(name="DT_EXECUCAO", nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtExecucao;
	
	@Column(name="DS_SERVICO", length=500, nullable = false)
	private String dsServico;
	
	@Column(name = "NR_QUILOMETRO_RODADO", precision=6)
	private Integer nrKmRodado;
	
	@Column(name = "VL_TABELA", precision=16, scale=2)
	private BigDecimal vlTabela;
	
	@Column(name = "VL_NEGOCIADO", precision=16, scale=2)
	private BigDecimal vlNegociado;
	
	@Column(name = "VL_CUSTO", precision=16, scale=2)
	private BigDecimal vlCusto;
	
	@Column(name = "QT_HOMEM", precision=4)
    private Integer qtHomem;
    
	@Column(name = "QT_VOLUME", precision=6)
    private Integer qtVolume;
    
	@Column(name = "QT_PALETE", precision=4)
    private Integer qtPalete;
    
	@Columns(columns = {@Column(name = "DH_PERIODO_INICIAL"), @Column(name = "DH_PERIODO_INICIAL_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime  dhPeriodoInicial;
    
	@Columns(columns = {@Column(name = "DH_PERIODO_FINAL"), @Column(name = "DH_PERIODO_FINAL_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    private DateTime  dhPeriodoFinal;
    
	@Column(name = "TP_MODELO_PALETE", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_MODELO_PALETE") })
    private DomainValue tpModeloPalete;
	
	@Column(name = "BL_RETORNA_PALETE")
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
    private Boolean blRetornaPalete;
    
	@Column(name = "TP_ESCOLTA", length = 1)
   	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
   		  parameters = { @Parameter(name = "domainName", value = "DM_TIPO_ESCOLTA") })
    private DomainValue tpEscolta;
	
	@Column(name="BL_FATURADO", nullable=false)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blFaturado;
	
	@Column(name="BL_SEM_COBRANCA", nullable=false)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blSemCobranca;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_ORDEM_SERVICO", nullable = false)
    private OrdemServico ordemServico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PARCELA_PRECO", nullable = false)
	private ParcelaPreco parcelaPreco;

	public Long getIdOrdemServicoItem() {
		return idOrdemServicoItem;
	}
	public void setIdOrdemServicoItem(Long idOrdemServicoItem) {
		this.idOrdemServicoItem = idOrdemServicoItem;
	}

	public YearMonthDay getDtExecucao() {
		return dtExecucao;
	}
	public void setDtExecucao(YearMonthDay dtExecucao) {
		this.dtExecucao = dtExecucao;
	}

	public String getDsServico() {
		return dsServico;
	}
	public void setDsServico(String dsServico) {
		this.dsServico = dsServico;
	}

	public Integer getNrKmRodado() {
		return nrKmRodado;
	}
	public void setNrKmRodado(Integer nrKmRodado) {
		this.nrKmRodado = nrKmRodado;
	}

	public BigDecimal getVlTabela() {
		return vlTabela;
	}
	public void setVlTabela(BigDecimal vlTabela) {
		this.vlTabela = vlTabela;
	}

	public BigDecimal getVlNegociado() {
		return vlNegociado;
	}
	public void setVlNegociado(BigDecimal vlNegociado) {
		this.vlNegociado = vlNegociado;
	}

	public BigDecimal getVlCusto() {
		return vlCusto;
	}
	public void setVlCusto(BigDecimal vlCusto) {
		this.vlCusto = vlCusto;
	}

	public Integer getQtHomem() {
		return qtHomem;
	}
	public void setQtHomem(Integer qtHomem) {
		this.qtHomem = qtHomem;
	}

	public Integer getQtVolume() {
		return qtVolume;
	}
	public void setQtVolume(Integer qtVolume) {
		this.qtVolume = qtVolume;
	}

	public Integer getQtPalete() {
		return qtPalete;
	}
	public void setQtPalete(Integer qtPalete) {
		this.qtPalete = qtPalete;
	}

	public DateTime getDhPeriodoInicial() {
		return dhPeriodoInicial;
	}
	public void setDhPeriodoInicial(DateTime dhPeriodoInicial) {
		this.dhPeriodoInicial = dhPeriodoInicial;
	}

	public DateTime getDhPeriodoFinal() {
		return dhPeriodoFinal;
	}
	public void setDhPeriodoFinal(DateTime dhPeriodoFinal) {
		this.dhPeriodoFinal = dhPeriodoFinal;
	}

	public DomainValue getTpModeloPalete() {
		return tpModeloPalete;
	}
	public void setTpModeloPalete(DomainValue tpModeloPalete) {
		this.tpModeloPalete = tpModeloPalete;
	}

	public Boolean getBlRetornaPalete() {
		return blRetornaPalete;
	}
	public void setBlRetornaPalete(Boolean blRetornaPalete) {
		this.blRetornaPalete = blRetornaPalete;
	}

	public DomainValue getTpEscolta() {
		return tpEscolta;
	}
	public void setTpEscolta(DomainValue tpEscolta) {
		this.tpEscolta = tpEscolta;
	}
	
	public Boolean getBlFaturado() {
		return blFaturado;
	}
	public void setBlFaturado(Boolean blFaturado) {
		this.blFaturado = blFaturado;
	}
	
	public Boolean getBlSemCobranca() {
		return blSemCobranca;
	}
	public void setBlSemCobranca(Boolean blSemCobranca) {
		this.blSemCobranca = blSemCobranca;
	}
	
	public OrdemServico getOrdemServico() {
		return ordemServico;
	}
	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}
	
	public ParcelaPreco getParcelaPreco() {
		return parcelaPreco;
	}
	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idOrdemServicoItem", idOrdemServicoItem).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OrdemServicoItem))
			return false;
		OrdemServicoItem castOther = (OrdemServicoItem) other;
		return new EqualsBuilder().append(idOrdemServicoItem, castOther.getIdOrdemServicoItem()).isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(idOrdemServicoItem).toHashCode();
	}
	
	/**
	 * Copia os valores dos atributos do objeto passado por parâmetro (exceto idOrdemServicoItem).
	 * @param newBean Objeto de onde os valores serão copiadas.
	 */
	public void copyValuesFrom(OrdemServicoItem newBean) {
		if(newBean != null) {
			this.setParcelaPreco(newBean.getParcelaPreco());
			this.setDtExecucao(newBean.getDtExecucao());
			this.setVlTabela(newBean.getVlTabela());
			this.setVlNegociado(newBean.getVlNegociado());
			this.setVlCusto(newBean.getVlCusto());				
			this.setNrKmRodado(newBean.getNrKmRodado());								
			this.setQtVolume(newBean.getQtVolume());
			this.setQtHomem(newBean.getQtHomem());
			this.setQtPalete(newBean.getQtPalete());
			this.setBlRetornaPalete(newBean.getBlRetornaPalete());							
			this.setDhPeriodoInicial(newBean.getDhPeriodoInicial());
			this.setDhPeriodoFinal(newBean.getDhPeriodoFinal());				
			this.setDsServico(newBean.getDsServico());								
			this.setTpEscolta(newBean.getTpEscolta());
			this.setTpModeloPalete(newBean.getTpModeloPalete());
			this.setBlFaturado(newBean.getBlFaturado());
			this.setBlSemCobranca(newBean.getBlSemCobranca());
		}
	}
}