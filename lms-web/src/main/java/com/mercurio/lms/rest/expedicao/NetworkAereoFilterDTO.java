package com.mercurio.lms.rest.expedicao;

import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.expedicao.dto.AwbSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.AeroportoSuggestDTO;
 
public class NetworkAereoFilterDTO extends BaseFilterDTO { 
	private static final long serialVersionUID = 1L; 
	
	private DomainValue servico;
	private Map<String, Object> ciaAerea;
	private AwbSuggestDTO awb;
	private AeroportoSuggestDTO monitoramentoAeroportoOrigem;
	private AeroportoSuggestDTO monitoramentoAeroportoDestino;
	private DomainValue situacao;
	private YearMonthDay dtAtualizacaoInicial;
	private YearMonthDay dtAtualizacaoFinal;
	private Boolean situacaoFS;
	
	public DomainValue getServico() {
		return servico;
	}
	public void setServico(DomainValue servico) {
		this.servico = servico;
	}
	public Map<String, Object> getCiaAerea() {
		return ciaAerea;
	}
	public void setCiaAerea(Map<String, Object> ciaAerea) {
		this.ciaAerea = ciaAerea;
	}
	public AwbSuggestDTO getAwb() {
		return awb;
	}
	public void setAwb(AwbSuggestDTO awb) {
		this.awb = awb;
	}
	public DomainValue getSituacao() {
		return situacao;
	}
	public void setSituacao(DomainValue situacao) {
		this.situacao = situacao;
	}
	public YearMonthDay getDtAtualizacaoInicial() {
		return dtAtualizacaoInicial;
	}
	public void setDtAtualizacaoInicial(YearMonthDay dtAtualizacaoInicial) {
		this.dtAtualizacaoInicial = dtAtualizacaoInicial;
	}
	public YearMonthDay getDtAtualizacaoFinal() {
		return dtAtualizacaoFinal;
	}
	public void setDtAtualizacaoFinal(YearMonthDay dtAtualizacaoFinal) {
		this.dtAtualizacaoFinal = dtAtualizacaoFinal;
	}
	public AeroportoSuggestDTO getMonitoramentoAeroportoOrigem() {
		return monitoramentoAeroportoOrigem;
	}
	public void setMonitoramentoAeroportoOrigem(
			AeroportoSuggestDTO monitoramentoAeroportoOrigem) {
		this.monitoramentoAeroportoOrigem = monitoramentoAeroportoOrigem;
	}
	public AeroportoSuggestDTO getMonitoramentoAeroportoDestino() {
		return monitoramentoAeroportoDestino;
	}
	public void setMonitoramentoAeroportoDestino(
			AeroportoSuggestDTO monitoramentoAeroportoDestino) {
		this.monitoramentoAeroportoDestino = monitoramentoAeroportoDestino;
	}
	public Boolean getSituacaoFS() {
		return situacaoFS;
	}
	public void setSituacaoFS(Boolean situacaoFS) {
		this.situacaoFS = situacaoFS;
	}
} 
