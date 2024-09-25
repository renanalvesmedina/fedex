package com.mercurio.lms.rest.sim.dto;

import br.com.tntbrasil.integracao.domains.edw.LocalizacaoEmListaResultadoDto;
import com.mercurio.adsm.rest.BaseDTO;
import org.joda.time.DateTime;

public class LocalizacaoEmListaResultadoLmsWebDto extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private Long idDoctoServico;
    private Integer nrNotaFiscal;
    private String tpDoctoServico;
    private String sgFilialOrigem;
    private Long nrDoctoServico;
    private DateTime dhEmissao;
    private String sgFilialDestino;
    private String nrPedidoCliente;
    private Long nrColeta;
    private String nrIdentificacaoRemetente;
    private String nmPessoaRemetente;
    private String nmFantasiaRemetente;
    private String nrIdentificacaoDestinatario;
    private String nmPessoaDestinatario;
    private String nmFantasiaDestinatario;

    public static LocalizacaoEmListaResultadoLmsWebDto of(LocalizacaoEmListaResultadoDto dto) {
        LocalizacaoEmListaResultadoLmsWebDto lmsDto = new LocalizacaoEmListaResultadoLmsWebDto();

        lmsDto.setIdDoctoServico(dto.getIdDoctoServico());
        lmsDto.setNrNotaFiscal(dto.getNrNotaFiscal());
        lmsDto.setTpDoctoServico(dto.getTpDoctoServico());
        lmsDto.setSgFilialOrigem(dto.getSgFilialOrigem());
        lmsDto.setNrDoctoServico(dto.getNrDoctoServico());
        lmsDto.setDhEmissao(extraiDhEmissao(dto));
        lmsDto.setSgFilialDestino(dto.getSgFilialDestino());
        lmsDto.setNrPedidoCliente(dto.getNrPedidoCliente());
        lmsDto.setNrColeta(dto.getNrColeta());
        lmsDto.setNrIdentificacaoRemetente(dto.getNrIdentificacaoRemetente());
        lmsDto.setNmPessoaRemetente(dto.getNmPessoaRemetente());
        lmsDto.setNmFantasiaRemetente(dto.getNmFantasiaRemetente());
        lmsDto.setNrIdentificacaoDestinatario(dto.getNrIdentificacaoDestinatario());
        lmsDto.setNmPessoaDestinatario(dto.getNmPessoaDestinatario());
        lmsDto.setNmFantasiaDestinatario(dto.getNmFantasiaDestinatario());

        /*
         * Necessário informar o ID para o framework.
         */
        lmsDto.setId(dto.getIdDoctoServico());

        return lmsDto;
    }

    private static DateTime extraiDhEmissao(LocalizacaoEmListaResultadoDto dto) {
        Long dhEmissaoInstant = dto.getDhEmissaoInstant();

        if (dhEmissaoInstant == null) {
            return null;
        }

        return new DateTime(dhEmissaoInstant);
    }

    public Long getIdDoctoServico() {
        return idDoctoServico;
    }

    public void setIdDoctoServico(Long idDoctoServico) {
        this.idDoctoServico = idDoctoServico;
    }

    public Integer getNrNotaFiscal() {
        return nrNotaFiscal;
    }

    public void setNrNotaFiscal(Integer nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public String getTpDoctoServico() {
        return tpDoctoServico;
    }

    public void setTpDoctoServico(String tpDoctoServico) {
        this.tpDoctoServico = tpDoctoServico;
    }

    public DateTime getDhEmissao() {
        return dhEmissao;
    }

    public void setDhEmissao(DateTime dhEmissao) {
        this.dhEmissao = dhEmissao;
    }

    public String getSgFilialOrigem() {
        return sgFilialOrigem;
    }

    public void setSgFilialOrigem(String sgFilialOrigem) {
        this.sgFilialOrigem = sgFilialOrigem;
    }

    public Long getNrDoctoServico() {
        return nrDoctoServico;
    }

    public void setNrDoctoServico(Long nrDoctoServico) {
        this.nrDoctoServico = nrDoctoServico;
    }

    public String getSgFilialDestino() {
        return sgFilialDestino;
    }

    public void setSgFilialDestino(String sgFilialDestino) {
        this.sgFilialDestino = sgFilialDestino;
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

    public String getNrIdentificacaoRemetente() {
        return nrIdentificacaoRemetente;
    }

    public void setNrIdentificacaoRemetente(String nrIdentificacaoRemetente) {
        this.nrIdentificacaoRemetente = nrIdentificacaoRemetente;
    }

    public String getNmPessoaRemetente() {
        return nmPessoaRemetente;
    }

    public void setNmPessoaRemetente(String nmPessoaRemetente) {
        this.nmPessoaRemetente = nmPessoaRemetente;
    }

    public String getNmFantasiaRemetente() {
        return nmFantasiaRemetente;
    }

    public void setNmFantasiaRemetente(String nmFantasiaRemetente) {
        this.nmFantasiaRemetente = nmFantasiaRemetente;
    }

    public String getNrIdentificacaoDestinatario() {
        return nrIdentificacaoDestinatario;
    }

    public void setNrIdentificacaoDestinatario(String nrIdentificacaoDestinatario) {
        this.nrIdentificacaoDestinatario = nrIdentificacaoDestinatario;
    }

    public String getNmPessoaDestinatario() {
        return nmPessoaDestinatario;
    }

    public void setNmPessoaDestinatario(String nmPessoaDestinatario) {
        this.nmPessoaDestinatario = nmPessoaDestinatario;
    }

    public String getNmFantasiaDestinatario() {
        return nmFantasiaDestinatario;
    }

    public void setNmFantasiaDestinatario(String nmFantasiaDestinatario) {
        this.nmFantasiaDestinatario = nmFantasiaDestinatario;
    }
}
