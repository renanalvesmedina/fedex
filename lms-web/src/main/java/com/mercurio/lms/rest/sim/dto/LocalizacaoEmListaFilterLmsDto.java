package com.mercurio.lms.rest.sim.dto;

import br.com.tntbrasil.integracao.domains.edw.LocalizacaoEmListaFilterDto;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LocalizacaoEmListaFilterLmsDto extends BaseFilterDTO {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter ISO_DATE_FORMAT = DateTimeFormat
            .forPattern(DateFormatUtils.ISO_DATE_FORMAT.getPattern());

    // Filtros básicos
    private Integer nrNotaFiscal;
    private Long nrDoctoServico;
    private String nrPedidoCliente;
    private Long nrColeta;
    private FilialSuggestDTO filialOrigem;
    private FilialSuggestDTO filialDestino;

    // Período de emissão
    private YearMonthDay dtEmissaoInicial;
    private YearMonthDay dtEmissaoFinal;

    // Remetente
    private ClienteSuggestDTO remetente;

    // Destinatario
    private ClienteSuggestDTO destinatario;

    public LocalizacaoEmListaFilterDto toIntegrationDomainFilter() {
        LocalizacaoEmListaFilterDto filterDto = new LocalizacaoEmListaFilterDto();

        // Filtros básicos
        filterDto.setNrNotaFiscal(nrNotaFiscal);
        filterDto.setNrDoctoServico(nrDoctoServico);
        filterDto.setNrPedidoCliente(nrPedidoCliente);
        filterDto.setNrColeta(nrColeta);
        filterDto.setSgFilialOrigem(extraiSgFilial(filialOrigem));
        filterDto.setSgFilialDestino(extraiSgFilial(filialDestino));

        // Período de emissão
        filterDto.setDtEmissaoInicialString(extraiDataString(dtEmissaoInicial));
        filterDto.setDtEmissaoFinalString(extraiDataString(dtEmissaoFinal));

        // Remetente
        filterDto.setNrIdentificacaoRemetente(extraiNrIdentificacao(remetente));

        // Destinatario
        filterDto.setNrIdentificacaoDestinatario(extraiNrIdentificacao(destinatario));

        // Paginacao
        filterDto.setPageNumber(getPagina() == null ? null : getPagina() - 1);
        filterDto.setPageSize(getQtRegistrosPagina());

        return filterDto;
    }

    private String extraiNrIdentificacao(ClienteSuggestDTO cliente) {
        return cliente == null ? null : cliente.getNrIdentificacao();
    }

    private String extraiSgFilial(FilialSuggestDTO filial) {
        return filial == null ? null : filial.getSgFilial();
    }

    private String extraiDataString(YearMonthDay yearMonthDay) {
        if (yearMonthDay == null) {
            return null;
        }

        return yearMonthDay.toString(ISO_DATE_FORMAT);
    }

    public Integer getNrNotaFiscal() {
        return nrNotaFiscal;
    }

    public void setNrNotaFiscal(Integer nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public Long getNrDoctoServico() {
        return nrDoctoServico;
    }

    public void setNrDoctoServico(Long nrDoctoServico) {
        this.nrDoctoServico = nrDoctoServico;
    }

    public String getNrPedidoCliente() {
        return nrPedidoCliente;
    }

    public void setNrPedidoCliente(String nrPedidoCliente) {
        this.nrPedidoCliente = nrPedidoCliente;
    }

    public Long getNrColeta() {
        return nrColeta;
    }

    public void setNrColeta(Long nrColeta) {
        this.nrColeta = nrColeta;
    }

    public FilialSuggestDTO getFilialOrigem() {
        return filialOrigem;
    }

    public void setFilialOrigem(FilialSuggestDTO filialOrigem) {
        this.filialOrigem = filialOrigem;
    }

    public FilialSuggestDTO getFilialDestino() {
        return filialDestino;
    }

    public void setFilialDestino(FilialSuggestDTO filialDestino) {
        this.filialDestino = filialDestino;
    }

    public YearMonthDay getDtEmissaoInicial() {
        return dtEmissaoInicial;
    }

    public void setDtEmissaoInicial(YearMonthDay dtEmissaoInicial) {
        this.dtEmissaoInicial = dtEmissaoInicial;
    }

    public YearMonthDay getDtEmissaoFinal() {
        return dtEmissaoFinal;
    }

    public void setDtEmissaoFinal(YearMonthDay dtEmissaoFinal) {
        this.dtEmissaoFinal = dtEmissaoFinal;
    }

    public ClienteSuggestDTO getRemetente() {
        return remetente;
    }

    public void setRemetente(ClienteSuggestDTO remetente) {
        this.remetente = remetente;
    }

    public ClienteSuggestDTO getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(ClienteSuggestDTO destinatario) {
        this.destinatario = destinatario;
    }
}
