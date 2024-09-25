package com.mercurio.lms.expedicao.dto;

import java.math.BigDecimal;
import java.util.List;

public class XmlAwbCiaAereaInsertDto extends XmlAwbCiaAereaDto{
	
	private String nrIdentificacaoExp;
	private String identificacaoRecebedor;
	private String dsSerie;
	private Long nrAwb;
	private String forPag;
	private Integer digito;
	private Long nrSerieDacte;
	private Long nrDacte;
	private String dsModeloDacte; 
	private String tpCte;
	private String tpServico;
	private String dsNaturezaPrestacao;
	private String dsAutorizacaoUso;
	private String dhReceb;
	private String aeroportoOrigem;
	private String aeroportoDestino;
	private BigDecimal vlFrete;
	private BigDecimal vlFretePeso;
	private BigDecimal vlICMS;
	private BigDecimal pcAliquotaICMS;
	private String dhEmissao;
	private List<String> nrChaveInfNfe;
	private String obAwb;
	private BigDecimal vlBaseCalcImposto;
	private Integer qtVolumes;
	private String dsProddominante;
	private BigDecimal vlTotalMercadoria;
	private String nmResponsavelApolice;
	private BigDecimal vlTotalServico;
	private BigDecimal vlReceber;
	private String dsSituacaoTributaria;
	private String dsInfManuseio;
	private String cdCargaEspecial;
	private String dsCaracAdicServ;
	private String dtPrevistaEntrega;
	private String dsClasse;
	private BigDecimal vlTarifa;
	private String dsIdentificacaoTomador;
	private String dsIdentificacaoEmissor;
	private String blRetira;
	private String nrApoliceSeguro;
	private String nrIdentificacaoEmit;
	private String dhVigenciaEmit;
	private List vlParcelasAwb;
	private List dsParcelasAwb;
	
	private List<String> listNrChaveCteAnt;
	private BigDecimal psTotal;
	
	//TAM
	
	private BigDecimal psCubado;
	private String dsOutCaracteristicas;
	private BigDecimal psBaseCalc;
	private String dsUsoEmissor;
	private String nrOperacionalAereo;
		
	//GOL
	private String nrMinuta;
	private String cdTarifa;
	
	
	public String getNrIdentificacaoExp() {
		return nrIdentificacaoExp;
	}
	public void setNrIdentificacaoExp(String nrIdentificacaoExp) {
		this.nrIdentificacaoExp = nrIdentificacaoExp;
	}
	public String getIdentificacaoRecebedor() {
		return identificacaoRecebedor;
	}
	public void setIdentificacaoRecebedor(String identificacaoRecebedor) {
		this.identificacaoRecebedor = identificacaoRecebedor;
	}
	public String getDsSerie() {
		return dsSerie;
	}
	public void setDsSerie(String dsSerie) {
		this.dsSerie = dsSerie;
	}
	public Long getNrAwb() {
		return nrAwb;
	}
	public void setNrAwb(Long nrAwb) {
		this.nrAwb = nrAwb;
	}
	public String getForPag() {
		return forPag;
	}
	public void setForPag(String forPag) {
		this.forPag = forPag;
	}
	public Integer getDigito() {
		return digito;
	}
	public void setDigito(Integer digito) {
		this.digito = digito;
	}
	public Long getNrSerieDacte() {
		return nrSerieDacte;
	}
	public void setNrSerieDacte(Long nrSerieDacte) {
		this.nrSerieDacte = nrSerieDacte;
	}
	public Long getNrDacte() {
		return nrDacte;
	}
	public void setNrDacte(Long nrDacte) {
		this.nrDacte = nrDacte;
	}
	public String getDsModeloDacte() {
		return dsModeloDacte;
	}
	public void setDsModeloDacte(String dsModeloDacte) {
		this.dsModeloDacte = dsModeloDacte;
	}
	public String getTpCte() {
		return tpCte;
	}
	public void setTpCte(String tpCte) {
		this.tpCte = tpCte;
	}
	public String getTpServico() {
		return tpServico;
	}
	public void setTpServico(String tpServico) {
		this.tpServico = tpServico;
	}
	public String getDsNaturezaPrestacao() {
		return dsNaturezaPrestacao;
	}
	public void setDsNaturezaPrestacao(String dsNaturezaPrestacao) {
		this.dsNaturezaPrestacao = dsNaturezaPrestacao;
	}
	public String getDsAutorizacaoUso() {
		return dsAutorizacaoUso;
	}
	public void setDsAutorizacaoUso(String dsAutorizacaoUso) {
		this.dsAutorizacaoUso = dsAutorizacaoUso;
	}
	public String getAeroportoOrigem() {
		return aeroportoOrigem;
	}
	public void setAeroportoOrigem(String aeroportoOrigem) {
		this.aeroportoOrigem = aeroportoOrigem;
	}
	public String getAeroportoDestino() {
		return aeroportoDestino;
	}
	public void setAeroportoDestino(String aeroportoDestino) {
		this.aeroportoDestino = aeroportoDestino;
	}
	public BigDecimal getVlFrete() {
		return vlFrete;
	}
	public void setVlFrete(BigDecimal vlFrete) {
		this.vlFrete = vlFrete;
	}
	public BigDecimal getVlFretePeso() {
		return vlFretePeso;
	}
	public void setVlFretePeso(BigDecimal vlFretePeso) {
		this.vlFretePeso = vlFretePeso;
	}
	public BigDecimal getVlICMS() {
		return vlICMS;
	}
	public void setVlICMS(BigDecimal vlICMS) {
		this.vlICMS = vlICMS;
	}
	public BigDecimal getPcAliquotaICMS() {
		return pcAliquotaICMS;
	}
	public void setPcAliquotaICMS(BigDecimal pcAliquotaICMS) {
		this.pcAliquotaICMS = pcAliquotaICMS;
	}
	public String getDhEmissao() {
		return dhEmissao;
	}
	public void setDhEmissao(String dhEmissao) {
		this.dhEmissao = dhEmissao;
	}
	public List<String> getNrChaveInfNfe() {
		return nrChaveInfNfe;
	}
	public void setNrChaveInfNfe(List<String> nrChaveInfNfe) {
		this.nrChaveInfNfe = nrChaveInfNfe;
	}
	public String getObAwb() {
		return obAwb;
	}
	public void setObAwb(String obAwb) {
		this.obAwb = obAwb;
	}
	public BigDecimal getVlBaseCalcImposto() {
		return vlBaseCalcImposto;
	}
	public void setVlBaseCalcImposto(BigDecimal vlBaseCalcImposto) {
		this.vlBaseCalcImposto = vlBaseCalcImposto;
	}
	public Integer getQtVolumes() {
		return qtVolumes;
	}
	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}
	public String getDsProddominante() {
		return dsProddominante;
	}
	public void setDsProddominante(String dsProddominante) {
		this.dsProddominante = dsProddominante;
	}
	public BigDecimal getVlTotalMercadoria() {
		return vlTotalMercadoria;
	}
	public void setVlTotalMercadoria(BigDecimal vlTotalMercadoria) {
		this.vlTotalMercadoria = vlTotalMercadoria;
	}
	public String getNmResponsavelApolice() {
		return nmResponsavelApolice;
	}
	public void setNmResponsavelApolice(String nmResponsavelApolice) {
		this.nmResponsavelApolice = nmResponsavelApolice;
	}
	public BigDecimal getVlTotalServico() {
		return vlTotalServico;
	}
	public void setVlTotalServico(BigDecimal vlTotalServico) {
		this.vlTotalServico = vlTotalServico;
	}
	public BigDecimal getVlReceber() {
		return vlReceber;
	}
	public void setVlReceber(BigDecimal vlReceber) {
		this.vlReceber = vlReceber;
	}
	public String getDsSituacaoTributaria() {
		return dsSituacaoTributaria;
	}
	public void setDsSituacaoTributaria(String dsSituacaoTributaria) {
		this.dsSituacaoTributaria = dsSituacaoTributaria;
	}
	public String getDsInfManuseio() {
		return dsInfManuseio;
	}
	public void setDsInfManuseio(String dsInfManuseio) {
		this.dsInfManuseio = dsInfManuseio;
	}
	public String getCdCargaEspecial() {
		return cdCargaEspecial;
	}
	public void setCdCargaEspecial(String cdCargaEspecial) {
		this.cdCargaEspecial = cdCargaEspecial;
	}
	public String getDsCaracAdicServ() {
		return dsCaracAdicServ;
	}
	public void setDsCaracAdicServ(String dsCaracAdicServ) {
		this.dsCaracAdicServ = dsCaracAdicServ;
	}
	public String getDtPrevistaEntrega() {
		return dtPrevistaEntrega;
	}
	public void setDtPrevistaEntrega(String dtPrevistaEntrega) {
		this.dtPrevistaEntrega = dtPrevistaEntrega;
	}
	public String getDsClasse() {
		return dsClasse;
	}
	public void setDsClasse(String dsClasse) {
		this.dsClasse = dsClasse;
	}
	public BigDecimal getVlTarifa() {
		return vlTarifa;
	}
	public void setVlTarifa(BigDecimal vlTarifa) {
		this.vlTarifa = vlTarifa;
	}
	public String getDsIdentificacaoTomador() {
		return dsIdentificacaoTomador;
	}
	public void setDsIdentificacaoTomador(String dsIdentificacaoTomador) {
		this.dsIdentificacaoTomador = dsIdentificacaoTomador;
	}
	public String getDsIdentificacaoEmissor() {
		return dsIdentificacaoEmissor;
	}
	public void setDsIdentificacaoEmissor(String dsIdentificacaoEmissor) {
		this.dsIdentificacaoEmissor = dsIdentificacaoEmissor;
	}
	public String getBlRetira() {
		return blRetira;
	}
	public void setBlRetira(String blRetira) {
		this.blRetira = blRetira;
	}
	public String getNrApoliceSeguro() {
		return nrApoliceSeguro;
	}
	public void setNrApoliceSeguro(String nrApoliceSeguro) {
		this.nrApoliceSeguro = nrApoliceSeguro;
	}
	public String getNrIdentificacaoEmit() {
		return nrIdentificacaoEmit;
	}
	public void setNrIdentificacaoEmit(String nrIdentificacaoEmit) {
		this.nrIdentificacaoEmit = nrIdentificacaoEmit;
	}
	public String getDhVigenciaEmit() {
		return dhVigenciaEmit;
	}
	public void setDhVigenciaEmit(String dhVigenciaEmit) {
		this.dhVigenciaEmit = dhVigenciaEmit;
	}
	public List getVlParcelasAwb() {
		return vlParcelasAwb;
	}
	public void setVlParcelasAwb(List vlParcelasAwb) {
		this.vlParcelasAwb = vlParcelasAwb;
	}
	public List getDsParcelasAwb() {
		return dsParcelasAwb;
	}
	public void setDsParcelasAwb(List dsParcelasAwb) {
		this.dsParcelasAwb = dsParcelasAwb;
	}
	public String getDhReceb() {
		return dhReceb;
	}
	public void setDhReceb(String dhReceb) {
		this.dhReceb = dhReceb;
	}
	public List<String> getListNrChaveCteAnt() {
		return listNrChaveCteAnt;
	}
	public void setListNrChaveCteAnt(List<String> listNrChaveCteAnt) {
		this.listNrChaveCteAnt = listNrChaveCteAnt;
	}
	public BigDecimal getPsTotal() {
		return psTotal;
	}
	public void setPsTotal(BigDecimal psTotal) {
		this.psTotal = psTotal;
	}
	public BigDecimal getPsCubado() {
		return psCubado;
	}
	public void setPsCubado(BigDecimal psCubado) {
		this.psCubado = psCubado;
	}
	public String getDsOutCaracteristicas() {
		return dsOutCaracteristicas;
	}
	public void setDsOutCaracteristicas(String dsOutCaracteristicas) {
		this.dsOutCaracteristicas = dsOutCaracteristicas;
	}
	public BigDecimal getPsBaseCalc() {
		return psBaseCalc;
	}
	public void setPsBaseCalc(BigDecimal psBaseCalc) {
		this.psBaseCalc = psBaseCalc;
	}
	public String getDsUsoEmissor() {
		return dsUsoEmissor;
	}
	public void setDsUsoEmissor(String dsUsoEmissor) {
		this.dsUsoEmissor = dsUsoEmissor;
	}
	public String getNrOperacionalAereo() {
		return nrOperacionalAereo;
	}
	public void setNrOperacionalAereo(String nrOperacionalAereo) {
		this.nrOperacionalAereo = nrOperacionalAereo;
	}
	public String getNrMinuta() {
		return nrMinuta;
	}
	public void setNrMinuta(String nrMinuta) {
		this.nrMinuta = nrMinuta;
	}
	public String getCdTarifa() {
		return cdTarifa;
	}
	public void setCdTarifa(String cdTarifa) {
		this.cdTarifa = cdTarifa;
	}
	
	
	
}
