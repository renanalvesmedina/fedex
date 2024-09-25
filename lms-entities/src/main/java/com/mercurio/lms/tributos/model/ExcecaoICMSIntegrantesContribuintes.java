package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

public class ExcecaoICMSIntegrantesContribuintes implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private UnidadeFederativa unidadeFederativaOrigem;
	
	private UnidadeFederativa unidadeFederativaDestino;
	
	private DomainValue tpFrete;
	
	private DomainValue tpIntegranteFrete;
	
	private EmbasamentoLegalIcms embasamentoLegalIcms;
	
    private YearMonthDay dtVigenciaInicial;

    private YearMonthDay dtVigenciaFinal;

	public Long getId() {
		return id;
	}

	public void setId(Long idEmbasamento) {
		this.id = idEmbasamento;
	}

	public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaOrigem() {
		return unidadeFederativaOrigem;
	}

	public void setUnidadeFederativaOrigem(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaOrigem) {
		this.unidadeFederativaOrigem = unidadeFederativaOrigem;
	}

	public UnidadeFederativa getUnidadeFederativaDestino() {
		return unidadeFederativaDestino;
	}

	public void setUnidadeFederativaDestino(UnidadeFederativa unidadeFederativaDestino) {
		this.unidadeFederativaDestino = unidadeFederativaDestino;
	}

	public DomainValue getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}

	public DomainValue getTpIntegranteFrete() {
		return tpIntegranteFrete;
	}

	public void setTpIntegranteFrete(DomainValue tpIntegranteFrete) {
		this.tpIntegranteFrete = tpIntegranteFrete;
	}

	public EmbasamentoLegalIcms getEmbasamentoLegalIcms() {
		return embasamentoLegalIcms;
	}

	public void setEmbasamentoLegalIcms(EmbasamentoLegalIcms embasamentoLegalIcms) {
		this.embasamentoLegalIcms = embasamentoLegalIcms;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public ExcecaoICMSIntegrantesContribuintes fillByMap(TypedFlatMap data) {
		this.id						  = data.getLong("id");
		this.tpFrete                  = new DomainValue("");
		if (data.getString("idTpFrete") != null){
			this.tpFrete 				  = new DomainValue(data.getString("idTpFrete"));
		}
		this.embasamentoLegalIcms 	  = buildEmbasamento(data.getLong("idEmbasamento"));
		this.dtVigenciaInicial    	  = data.getYearMonthDay("periodoInicial");
		this.dtVigenciaFinal      	  = data.getYearMonthDay("periodoFinal");
		this.tpIntegranteFrete 		  = new DomainValue(data.getString("idIntegranteFrete"));
		this.unidadeFederativaOrigem  = buildUnidadeFederativa( data.getLong("idUnidadeFederativaOrigem"));
		this.unidadeFederativaDestino = buildUnidadeFederativa( data.getLong("idUnidadeFederativaDestino"));
		return this;
	}

	private EmbasamentoLegalIcms buildEmbasamento(Long idEmbasamento) {
		if(idEmbasamento == null) { 
			return null; 
		}
		
		this.embasamentoLegalIcms = new EmbasamentoLegalIcms();
		this.embasamentoLegalIcms.setIdEmbasamento(idEmbasamento);
		return this.embasamentoLegalIcms;
	}
	
	private UnidadeFederativa buildUnidadeFederativa(Long idUnidadefederativa){
		if(idUnidadefederativa == null) { 
			return null; 
		}
		
		UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
		unidadeFederativa.setIdUnidadeFederativa(idUnidadefederativa);
		return unidadeFederativa;
	}

}
