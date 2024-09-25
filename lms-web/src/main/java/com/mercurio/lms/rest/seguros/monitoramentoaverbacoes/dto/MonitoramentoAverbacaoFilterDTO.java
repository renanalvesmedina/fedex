package com.mercurio.lms.rest.seguros.monitoramentoaverbacoes.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class MonitoramentoAverbacaoFilterDTO extends BaseFilterDTO {

    private static final long serialVersionUID = 7488562180247630948L;
    
    private FilialSuggestDTO filial;
    private ClienteSuggestDTO cliente;
    private YearMonthDay dtAverbacaoInicial;
    private YearMonthDay dtAverbacaoFinal;
    private DomainValue averbado;
    private DomainValue tipoMonitoriamentoAverbacao;
    private String acao;

    private Long nrConhecimento;

    public FilialSuggestDTO getFilial() {
        return filial;
    }

    public void setFilial(FilialSuggestDTO filial) {
        this.filial = filial;
    }

    public ClienteSuggestDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteSuggestDTO cliente) {
        this.cliente = cliente;
    }

    public YearMonthDay getDtAverbacaoInicial() {
        return dtAverbacaoInicial;
    }

    public void setDtAverbacaoInicial(YearMonthDay dtAverbacaoInicial) {
        this.dtAverbacaoInicial = dtAverbacaoInicial;
    }

    public YearMonthDay getDtAverbacaoFinal() {
        return dtAverbacaoFinal;
    }

    public void setDtAverbacaoFinal(YearMonthDay dtAverbacaoFinal) {
        this.dtAverbacaoFinal = dtAverbacaoFinal;
    }

    public DomainValue getAverbado() {
        return averbado;
    }

    public void setAverbado(DomainValue averbado) {
        this.averbado = averbado;
    }

    public Long getNrConhecimento() {
        return nrConhecimento;
    }

    public void setNrConhecimento(Long nrConhecimento) {
        this.nrConhecimento = nrConhecimento;
    }

    public DomainValue getTipoMonitoriamentoAverbacao() {
        return tipoMonitoriamentoAverbacao;
    }

    public void setTipoMonitoriamentoAverbacao(DomainValue tipoMonitoriamentoAverbacao) {
        this.tipoMonitoriamentoAverbacao = tipoMonitoriamentoAverbacao;
    }

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}
}
