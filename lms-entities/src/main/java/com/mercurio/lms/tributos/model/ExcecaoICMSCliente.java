package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

/** @author LMS Custom Hibernate CodeGenerator */
public class ExcecaoICMSCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idExcecaoICMSCliente;

    /** persistent field */
    private String nrCNPJParcialDev;

    /** persistent field */
    private DomainValue tpFrete;
    
    /** persistent field */
    private Boolean blSubcontratacao;
    
    /** persistent field */
    private Boolean blObrigaCtrcSubContratante;
    
    private String dsRegimeEspecial;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    /** nullable persistent field */
    private TipoTributacaoIcms tipoTributacaoIcms;
    
    /** nullable persistent field */
    private UnidadeFederativa unidadeFederativa;
    
    /** nullable persistent field */
    private String cdEmbLegalMastersaf;
    
    /** nullable persistent field */
    private List remetentesExcecaoICMSCli;

    /** nullable persistent field */
    private List<ExcecaoICMSNatureza> execoesICMSNatureza;
    
	public Boolean getBlSubcontratacao() {
		return blSubcontratacao;
	}

	public void setBlSubcontratacao(Boolean blSubcontratacao) {
		this.blSubcontratacao = blSubcontratacao;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public Long getIdExcecaoICMSCliente() {
		return idExcecaoICMSCliente;
	}

	public void setIdExcecaoICMSCliente(Long idExcecaoICMSCliente) {
		this.idExcecaoICMSCliente = idExcecaoICMSCliente;
	}

	public String getNrCNPJParcialDev() {
		return nrCNPJParcialDev;
	}

	public void setNrCNPJParcialDev(String nrCNPJParcialDev) {
		this.nrCNPJParcialDev = nrCNPJParcialDev;
	}

	public TipoTributacaoIcms getTipoTributacaoIcms() {
		return tipoTributacaoIcms;
	}

	public void setTipoTributacaoIcms(TipoTributacaoIcms tipoTributacaoIcms) {
		this.tipoTributacaoIcms = tipoTributacaoIcms;
	}

	public DomainValue getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public List getRemetentesExcecaoICMSCli() {
		return remetentesExcecaoICMSCli;
	}

	public void setRemetentesExcecaoICMSCli(List remetentesExcecaoICMSCli) {
		this.remetentesExcecaoICMSCli = remetentesExcecaoICMSCli;
	}

	public Boolean getBlObrigaCtrcSubContratante() {
		return blObrigaCtrcSubContratante;
	}

	public void setBlObrigaCtrcSubContratante(Boolean blObrigaCtrcSubContratante) {
		this.blObrigaCtrcSubContratante = blObrigaCtrcSubContratante;
	}

	public String getDsRegimeEspecial() {
		return dsRegimeEspecial;
	}

	public void setDsRegimeEspecial(String dsRegimeEspecial) {
		this.dsRegimeEspecial = dsRegimeEspecial;
	}

	public String getCdEmbLegalMastersaf() {
		return cdEmbLegalMastersaf;
}

	public void setCdEmbLegalMastersaf(String cdEmbLegalMastersaf) {
		this.cdEmbLegalMastersaf = cdEmbLegalMastersaf;
	}

	public List<ExcecaoICMSNatureza> getExecoesICMSNatureza() {
		return execoesICMSNatureza;
	}

	public void setExecoesICMSNatureza(
			List<ExcecaoICMSNatureza> execoesICMSNatureza) {
		this.execoesICMSNatureza = execoesICMSNatureza;
	}
	
}
