package com.mercurio.lms.rest.expedicao;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO; 
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
 
public class RelatorioReceitaPotencialFilterDTO extends BaseFilterDTO { 
	private static final long serialVersionUID = -7148948363440961529L;
	
	private Long idRegional;
	private List<Map<String,Object>> regionais;
	
	private FilialSuggestDTO filial;
	private FilialSuggestDTO idFilialCobranca;
	
	private Long idParcelaPreco;
	private List<Map<String,Object>> servicosAdicionais;
	
	private YearMonthDay periodoInicial;
	private YearMonthDay periodoFinal;
	private DomainValue abrangencia;
	private DomainValue modal;
	private ClienteSuggestDTO cliente;
	private ClienteSuggestDTO executivo;
	
	public Long getIdRegional() {
		return idRegional;
	}
	public void setIdRegional(Long idRegional) {
		this.idRegional = idRegional;
	}
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	public FilialSuggestDTO getIdFilialCobranca() {
		return idFilialCobranca;
	}
	public void setIdFilialCobranca(FilialSuggestDTO idFilialCobranca) {
		this.idFilialCobranca = idFilialCobranca;
	}
	public List<Map<String, Object>> getRegionais() {
		return regionais;
	}
	public void setRegionais(List<Map<String, Object>> regionais) {
		this.regionais = regionais;
	}
	public YearMonthDay getPeriodoInicial() {
		return periodoInicial;
	}
	public void setPeriodoInicial(YearMonthDay periodoInicial) {
		this.periodoInicial = periodoInicial;
	}
	public YearMonthDay getPeriodoFinal() {
		return periodoFinal;
	}
	public void setPeriodoFinal(YearMonthDay periodoFinal) {
		this.periodoFinal = periodoFinal;
	}
	public DomainValue getAbrangencia() {
		return abrangencia;
	}
	public void setAbrangencia(DomainValue abrangencia) {
		this.abrangencia = abrangencia;
	}
	public DomainValue getModal() {
		return modal;
	}
	public void setModal(DomainValue modal) {
		this.modal = modal;
	}
	public ClienteSuggestDTO getCliente() {
		return cliente;
	}
	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}
	public ClienteSuggestDTO getExecutivo() {
		return executivo;
	}
	public void setExecutivo(ClienteSuggestDTO executivo) {
		this.executivo = executivo;
	}
	public List<Map<String, Object>> getServicosAdicionais() {
		return servicosAdicionais;
	}
	public void setServicosAdicionais(List<Map<String, Object>> servicosAdicionais) {
		this.servicosAdicionais = servicosAdicionais;
	}
	public Long getIdParcelaPreco() {
		return idParcelaPreco;
	}
	public void setIdParcelaPreco(Long idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}
} 
