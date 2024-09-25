package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

/**
 * Pojo da tabela TIPO_TRIBUTACAO_UF
 *  
 * @author Diego Umpierre
 * @since 24/08/2006
 *  
 */
public class TipoTributacaoUf implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
	private Long idTipoTributacaoUf;
	
	/** nullable persistent field */
    private UnidadeFederativa unidadeFederativa;
    
    /** nullable persistent field */
    private TipoTributacaoIcms tipoTributacaoIcms;
    
    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private Boolean blContribuinte;
    
    /** nullable persistent field */
    private DomainValue tpTipoFrete;
    
    /** nullable persistent field */
    private DomainValue tpAbrangenciaUf;
    
	public Boolean getBlContribuinte() {
		return blContribuinte;
	}

	public void setBlContribuinte(Boolean blContribuinte) {
		this.blContribuinte = blContribuinte;
	}

	public DomainValue getTpTipoFrete() {
		return tpTipoFrete;
	}

	public void setTpTipoFrete(DomainValue tpTipoFrete) {
		this.tpTipoFrete = tpTipoFrete;
	}

	public DomainValue getTpAbrangenciaUf() {
		return tpAbrangenciaUf;
	}

	public void setTpAbrangenciaUf(DomainValue tpAbrangenciaUf) {
		this.tpAbrangenciaUf = tpAbrangenciaUf;
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

	public Long getIdTipoTributacaoUf() {
		return idTipoTributacaoUf;
	}

	public void setIdTipoTributacaoUf(Long idTipoTributacaoUf) {
		this.idTipoTributacaoUf = idTipoTributacaoUf;
	}

	public TipoTributacaoIcms getTipoTributacaoIcms() {
		return tipoTributacaoIcms;
	}

	public void setTipoTributacaoIcms(TipoTributacaoIcms tipoTributacaoIcms) {
		this.tipoTributacaoIcms = tipoTributacaoIcms;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}
    
}
