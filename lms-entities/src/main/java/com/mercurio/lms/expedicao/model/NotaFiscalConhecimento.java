package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.vendas.model.Cliente;

/** @author LMS Custom Hibernate CodeGenerator */
public class NotaFiscalConhecimento implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idNotaFiscalConhecimento;

	/** persistent field */
	private Integer nrNotaFiscal;

	/** persistent field */
	private BigDecimal vlTotal;

	/** persistent field */
	private Short qtVolumes;

	/** persistent field */
	private BigDecimal psMercadoria;

	/** persistent field */
	private YearMonthDay dtEmissao;

	/** nullable persistent field */
	private BigInteger nrCfop;

	/** nullable persistent field */
	private BigDecimal vlBaseCalculo;

	/** nullable persistent field */
	private BigDecimal vlIcms;

	/** nullable persistent field */
	private YearMonthDay dtSaida;

	/** nullable persistent field */
	private String dsSerie;

	/** nullable persistent field */
	private String tpDocumento;
	
	/** persistent field */
	private BigDecimal psCubado;
	
    /** persistent field */
	private BigDecimal psCubadoNotfis;
	
    /** persistent field */
	private BigDecimal psAferido;
	
    /** persistent field */
	private BigDecimal vlTotalProdutos;
	
    /** persistent field */
	private BigDecimal vlBaseCalculoSt;
	
    /** persistent field */
	private BigDecimal vlIcmsSt;
	
    /** persistent field */
	private Integer nrPinSuframa;
	
	private String nrChave;
	
	/** persistent field */
	private Cliente cliente;

	/** persistent field */
	private Conhecimento conhecimento;
	
	/** persistent field */
	private List nfItemMdas;

	/** persistent field */
	private List reciboIndenizacaoNfs;

	/** persistent field */
	private List nfCartaMercadorias;

	/** persistent field */
	private List notaOcorrenciaNcs;

	/** persistent field */
	private List manifestoEntregaDocumentos;

	/** persistent field */
	private List nfDadosComps;

	/** persistent field */
	private List itemNfCtos;

	/** persistent field */
	private List volumeNotaFiscais;
	
	private Boolean blProdutoPerigoso;
	private Boolean blControladoPoliciaCivil;
	private Boolean blControladoPoliciaFederal;
	private Boolean blControladoExercito;
	
	public NotaFiscalConhecimento(){
	}
	
	public NotaFiscalConhecimento(Integer nrNotaFiscal){
		this.nrNotaFiscal = nrNotaFiscal;
	}
	
	public Long getIdNotaFiscalConhecimento() {
		return this.idNotaFiscalConhecimento;
	}

	public void setIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
		this.idNotaFiscalConhecimento = idNotaFiscalConhecimento;
	}

	public Integer getNrNotaFiscal() {
		return this.nrNotaFiscal;
	}

	public void setNrNotaFiscal(Integer nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	public BigDecimal getVlTotal() {
		return this.vlTotal;
	}

	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}

	public Short getQtVolumes() {
		return this.qtVolumes;
	}

	public void setQtVolumes(Short qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public BigDecimal getPsMercadoria() {
		return this.psMercadoria;
	}

	public void setPsMercadoria(BigDecimal psMercadoria) {
		this.psMercadoria = psMercadoria;
	}

	public YearMonthDay getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(YearMonthDay dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public BigInteger getNrCfop() {
		return this.nrCfop;
	}

	public void setNrCfop(BigInteger nrCfop) {
		this.nrCfop = nrCfop;
	}

	public BigDecimal getVlBaseCalculo() {
		return this.vlBaseCalculo;
	}

	public void setVlBaseCalculo(BigDecimal vlBaseCalculo) {
		this.vlBaseCalculo = vlBaseCalculo;
	}

	public BigDecimal getVlIcms() {
		return this.vlIcms;
	}

	public void setVlIcms(BigDecimal vlIcms) {
		this.vlIcms = vlIcms;
	}

	public YearMonthDay getDtSaida() {
		return this.dtSaida;
	}

	public void setDtSaida(YearMonthDay dtSaida) {
		this.dtSaida = dtSaida;
	}

	public String getDsSerie() {
		return this.dsSerie;
	}

	public void setDsSerie(String dsSerie) {
		this.dsSerie = dsSerie;
	}

	public BigDecimal getPsCubado() {
		return this.psCubado;
	}

	public void setPsCubado(BigDecimal psCubado) {
		this.psCubado = psCubado;
	}

	public BigDecimal getPsCubadoNotfis() {
		return psCubadoNotfis;
	}

	public void setPsCubadoNotfis(BigDecimal psCubadoNotfis) {
		this.psCubadoNotfis = psCubadoNotfis;
	}

	public BigDecimal getPsAferido() {
		return this.psAferido;
	}

	public void setPsAferido(BigDecimal psAferido) {
		this.psAferido = psAferido;
	}

	public BigDecimal getVlTotalProdutos() {
		return this.vlTotalProdutos;
	}

	public void setVlTotalProdutos(BigDecimal vlTotalProdutos) {
		this.vlTotalProdutos = vlTotalProdutos;
	}

	public BigDecimal getVlBaseCalculoSt() {
		return this.vlBaseCalculoSt;
	}

	public void setVlBaseCalculoSt(BigDecimal vlBaseCalculoSt) {
		this.vlBaseCalculoSt = vlBaseCalculoSt;
	}

	public BigDecimal getVlIcmsSt() {
		return this.vlIcmsSt;
	}

	public void setVlIcmsSt(BigDecimal vlIcmsSt) {
		this.vlIcmsSt = vlIcmsSt;
	}

	public Integer getNrPinSuframa() {
		return nrPinSuframa;
	}

	public void setNrPinSuframa(Integer nrPinSuframa) {
		this.nrPinSuframa = nrPinSuframa;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Conhecimento getConhecimento() {
		return this.conhecimento;
	}

	public void setConhecimento(Conhecimento conhecimento) {
		this.conhecimento = conhecimento;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.NfItemMda.class) 
	public List getNfItemMdas() {
		return this.nfItemMdas;
	}

	public void setNfItemMdas(List nfItemMdas) {
		this.nfItemMdas = nfItemMdas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.ReciboIndenizacaoNf.class) 
	public List getReciboIndenizacaoNfs() {
		return this.reciboIndenizacaoNfs;
	}

	public void setReciboIndenizacaoNfs(List reciboIndenizacaoNfs) {
		this.reciboIndenizacaoNfs = reciboIndenizacaoNfs;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.NfCartaMercadoria.class) 
	public List getNfCartaMercadorias() {
		return this.nfCartaMercadorias;
	}

	public void setNfCartaMercadorias(List nfCartaMercadorias) {
		this.nfCartaMercadorias = nfCartaMercadorias;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.rnc.model.NotaOcorrenciaNc.class) 
	public List getNotaOcorrenciaNcs() {
		return this.notaOcorrenciaNcs;
	}

	public void setNotaOcorrenciaNcs(List notaOcorrenciaNcs) {
		this.notaOcorrenciaNcs = notaOcorrenciaNcs;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.entrega.model.ManifestoEntregaDocumento.class) 
	public List getManifestoEntregaDocumentos() {
		return this.manifestoEntregaDocumentos;
	}

	public void setManifestoEntregaDocumentos(List manifestoEntregaDocumentos) {
		this.manifestoEntregaDocumentos = manifestoEntregaDocumentos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.NfDadosComp.class) 
	public List getNfDadosComps() {
		return this.nfDadosComps;
	}

	public void setNfDadosComps(List nfDadosComps) {
		this.nfDadosComps = nfDadosComps;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ItemNfCto.class) 
	public List getItemNfCtos() {
		return this.itemNfCtos;
	}

	public void setItemNfCtos(List itemNfCtos) {
		this.itemNfCtos = itemNfCtos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.VolumeNotaFiscal.class) 
	public List getVolumeNotaFiscais() {
		return this.volumeNotaFiscais;
	}

	public void setVolumeNotaFiscais(List volumeNotaFiscais) {
		this.volumeNotaFiscais = volumeNotaFiscais;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idNotaFiscalConhecimento",
				getIdNotaFiscalConhecimento()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NotaFiscalConhecimento))
			return false;
		NotaFiscalConhecimento castOther = (NotaFiscalConhecimento) other;
		return new EqualsBuilder().append(this.getIdNotaFiscalConhecimento(),
				castOther.getIdNotaFiscalConhecimento()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdNotaFiscalConhecimento())
			.toHashCode();
	}

	public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

	public String getTpDocumento() {
		return tpDocumento;
}

	public void setTpDocumento(String tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	public Boolean getBlProdutoPerigoso() {
		return blProdutoPerigoso;
	}

	public void setBlProdutoPerigoso(Boolean blProdutoPerigoso) {
		this.blProdutoPerigoso = blProdutoPerigoso;
	}

	public Boolean getBlControladoPoliciaCivil() {
		return blControladoPoliciaCivil;
	}

	public void setBlControladoPoliciaCivil(Boolean blControladoPoliciaCivil) {
		this.blControladoPoliciaCivil = blControladoPoliciaCivil;
	}

	public Boolean getBlControladoPoliciaFederal() {
		return blControladoPoliciaFederal;
	}

	public void setBlControladoPoliciaFederal(Boolean blControladoPoliciaFederal) {
		this.blControladoPoliciaFederal = blControladoPoliciaFederal;
	}

	public Boolean getBlControladoExercito() {
		return blControladoExercito;
	}

	public void setBlControladoExercito(Boolean blControladoExercito) {
		this.blControladoExercito = blControladoExercito;
	}

}
