package com.mercurio.lms.rest.fretecarreteiroviagem.manterrecibosdeoutrasempresas;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class RecibosDeOutrasEmpresasFiltroDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;
	
	private ProprietarioDTO proprietario;
	
	private FilialSuggestDTO filial;
	
	private String dsEmpresa;
	
	private DomainValue tipoIdentificadorEmpregador;

	private String nrEmpregador;
	
	private String nrRecibo;
	
	private BigDecimal vlInss;
	
	private String dtEmissaoReciboInicial;
	
	private String dtEmissaoReciboFinal;

	public ProprietarioDTO getProprietario() {
		return proprietario;
	}

	public void setProprietario(ProprietarioDTO proprietario) {
		this.proprietario = proprietario;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public String getDsEmpresa() {
		return dsEmpresa;
	}

	public void setDsEmpresa(String dsEmpresa) {
		this.dsEmpresa = dsEmpresa;
	}

	public DomainValue getTipoIdentificadorEmpregador() {
		return tipoIdentificadorEmpregador;
	}

	public void setTipoIdentificadorEmpregador(
			DomainValue tipoIdentificadorEmpregador) {
		this.tipoIdentificadorEmpregador = tipoIdentificadorEmpregador;
	}

	public String getNrEmpregador() {
		return nrEmpregador;
	}

	public void setNrEmpregador(String nrEmpregador) {
		this.nrEmpregador = nrEmpregador;
	}

	public String getNrRecibo() {
		return nrRecibo;
	}

	public void setNrRecibo(String nrRecibo) {
		this.nrRecibo = nrRecibo;
	}

	public BigDecimal getVlInss() {
		return vlInss;
	}

	public void setVlInss(BigDecimal vlInss) {
		this.vlInss = vlInss;
	}

	public String getDtEmissaoReciboInicial() {
		return dtEmissaoReciboInicial;
	}

	public void setDtEmissaoReciboInicial(String dtEmissaoReciboInicial) {
		this.dtEmissaoReciboInicial = dtEmissaoReciboInicial;
	}

	public String getDtEmissaoReciboFinal() {
		return dtEmissaoReciboFinal;
	}

	public void setDtEmissaoReciboFinal(String dtEmissaoReciboFinal) {
		this.dtEmissaoReciboFinal = dtEmissaoReciboFinal;
	}
}
