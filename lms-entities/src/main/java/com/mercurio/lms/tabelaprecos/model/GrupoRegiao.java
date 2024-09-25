package com.mercurio.lms.tabelaprecos.model;

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

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

@Entity
@Table(name = "GRUPO_REGIAO")
@SequenceGenerator(name = "GRUPO_REGIAO_SEQ", sequenceName = "GRUPO_REGIAO_SQ")
public class GrupoRegiao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRUPO_REGIAO_SEQ")
	@Column(name = "ID_GRUPO_REGIAO", nullable = false)
	private Long idGrupoRegiao;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TABELA_PRECO", nullable = false)
	private TabelaPreco tabelaPreco;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_UNIDADE_FEDERATIVA", nullable = false)
	private UnidadeFederativa unidadeFederativa;

	@Column(name = "DS_GRUPO_REGIAO", length = 60, nullable = false)
	private String dsGrupoRegiao;

	@Column(name = "TP_AJUSTE", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_AJUSTE") })
	private DomainValue tpAjuste;

	@Column(name = "TP_VALOR_AJUSTE", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_VALOR_AJUSTE") })
	private DomainValue tpValorAjuste;

	@Column(name = "VL_AJUSTE_PADRAO", nullable = true)
	private BigDecimal vlAjustePadrao;

	@Column(name = "VL_AJUSTE_MINIMO", nullable = true)
	private BigDecimal vlAjusteMinimo;

	//--------- CQ 28633
	@Column(name = "TP_AJUSTE_ADVALOREM", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_AJUSTE") })
	private DomainValue tpAjusteAdvalorem;

	@Column(name = "TP_VALOR_AJUSTE_ADVALOREM", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_VALOR_AJUSTE_ADVALOREM") })
	private DomainValue tpValorAjusteAdvalorem;

	@Column(name = "VL_AJUSTE_PADRAO_ADVALOREM", nullable = true)
	private BigDecimal vlAjustePadraoAdvalorem;

	@Column(name = "VL_AJUSTE_MINIMO_ADVALOREM", nullable = true)
	private BigDecimal vlAjusteMinimoAdvalorem;

	@Column(name = "ID_GRUPO_REGIAO_ORIGEM", nullable = true)
	private Long idGrupoRegiaoOrigem;

	//--------- CQ 28633

	public GrupoRegiao clone(TabelaPreco newTabelaPreco) throws Throwable{
		GrupoRegiao newGrupoRegiao = (GrupoRegiao) BeanUtils.cloneBean(this);
		newGrupoRegiao.setIdGrupoRegiao(null);
		newGrupoRegiao.setTabelaPreco(newTabelaPreco);

		return newGrupoRegiao;
	}

	public Long getIdGrupoRegiao() {
		return this.idGrupoRegiao;
	}

	public void setIdGrupoRegiao(Long idGrupoRegiao) {
		this.idGrupoRegiao = idGrupoRegiao;
	}

	public TabelaPreco getTabelaPreco() {
		return this.tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return this.unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public String getDsGrupoRegiao() {
		return this.dsGrupoRegiao;
	}

	public void setDsGrupoRegiao(String dsGrupoRegiao) {
		this.dsGrupoRegiao = dsGrupoRegiao;
	}

	public DomainValue getTpAjuste() {
		return this.tpAjuste;
	}

	public void setTpAjuste(DomainValue tpAjuste) {
		this.tpAjuste = tpAjuste;
	}

	public DomainValue getTpValorAjuste() {
		return this.tpValorAjuste;
	}

	public void setTpValorAjuste(DomainValue tpValorAjuste) {
		this.tpValorAjuste = tpValorAjuste;
	}

	public BigDecimal getVlAjustePadrao() {
		return this.vlAjustePadrao;
	}

	public void setVlAjustePadrao(BigDecimal vlAjustePadrao) {
		this.vlAjustePadrao = vlAjustePadrao;
	}

	public BigDecimal getVlAjusteMinimo() {
		return vlAjusteMinimo;
	}

	public void setVlAjusteMinimo(BigDecimal vlAjusteMinimo) {
		this.vlAjusteMinimo = vlAjusteMinimo;
	}

	public DomainValue getTpAjusteAdvalorem() {
		return tpAjusteAdvalorem;
}

	public void setTpAjusteAdvalorem(DomainValue tpAjusteAdvalorem) {
		this.tpAjusteAdvalorem = tpAjusteAdvalorem;
	}

	public DomainValue getTpValorAjusteAdvalorem() {
		return tpValorAjusteAdvalorem;
	}

	public void setTpValorAjusteAdvalorem(DomainValue tpValorAjusteAdvalorem) {
		this.tpValorAjusteAdvalorem = tpValorAjusteAdvalorem;
	}

	public BigDecimal getVlAjustePadraoAdvalorem() {
		return vlAjustePadraoAdvalorem;
	}

	public void setVlAjustePadraoAdvalorem(BigDecimal vlAjustePadraoAdvalorem) {
		this.vlAjustePadraoAdvalorem = vlAjustePadraoAdvalorem;
	}

	public BigDecimal getVlAjusteMinimoAdvalorem() {
		return vlAjusteMinimoAdvalorem;
	}

	public void setVlAjusteMinimoAdvalorem(BigDecimal vlAjusteMinimoAdvalorem) {
		this.vlAjusteMinimoAdvalorem = vlAjusteMinimoAdvalorem;
	}

	public Long getIdGrupoRegiaoOrigem() { return idGrupoRegiaoOrigem;	}

	public void setIdGrupoRegiaoOrigem(Long idGrupoRegiaoOrigem) {
		this.idGrupoRegiaoOrigem = idGrupoRegiaoOrigem;
	}
}
