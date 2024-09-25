package com.mercurio.lms.expedicao.dto;

import com.mercurio.lms.vendas.dto.ClienteDTO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProcessaNotasEdiItemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String uuid;
    private Long idFilial;
    private Long idUsuario;
    private Long idProcessamentoEdi;
    private Long idMonitoramentoDescarga;
    private List<Long> listIdsNotasFiscaisEdiInformada;
    private Long nrProcessamento;
    private ClienteDTO clienteRemetente;
    private ParameterDto parameters;
    private String tpProcessamento;
    private Long idPedidoColeta;
    private Boolean finalizou = Boolean.FALSE;
    private Long inicioIndex;
    private Long finalIndex;
    private List<Long> listIdsNotasFiscaisEdiParcial;
    private Boolean primeiraExecucao = Boolean.FALSE;
    private Boolean processadoComErro = Boolean.FALSE;

    public ProcessaNotasEdiItemDto() {
    }

    public ProcessaNotasEdiItemDto(Long idFilial, Long idUsuario, Long idProcessamentoEdi, Long idMonitoramentoDescarga, List<Long> listIdsNotasFiscaisEdiInformada, Long nrProcessamento, String tpProcessamento, Long idPedidoColeta, Long inicioIndex, Long finalIndex, ParameterDto parameters) {
        this.idFilial = idFilial;
        this.idUsuario = idUsuario;
        this.idProcessamentoEdi = idProcessamentoEdi;
        this.idMonitoramentoDescarga = idMonitoramentoDescarga;
        this.listIdsNotasFiscaisEdiInformada = listIdsNotasFiscaisEdiInformada;
        this.nrProcessamento = nrProcessamento;
        this.tpProcessamento = tpProcessamento;
        this.idPedidoColeta = idPedidoColeta;
        this.inicioIndex = inicioIndex;
        this.finalIndex = finalIndex;
        this.parameters = parameters;
    }

    public Long getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    public ClienteDTO getClienteRemetente() {
        return clienteRemetente;
    }

    public void setClienteRemetente(ClienteDTO clienteRemetente) {
        this.clienteRemetente = clienteRemetente;
    }

    public ParameterDto getParameters() {
        return parameters;
    }

    public void setParameters(ParameterDto parameters) {
        this.parameters = parameters;
    }

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }

    public List<Long> getListIdsNotasFiscaisEdiInformada() {
        return listIdsNotasFiscaisEdiInformada;
    }

    public void setListIdsNotasFiscaisEdiInformada(List<Long> listIdsNotasFiscaisEdiInformada) {
        this.listIdsNotasFiscaisEdiInformada = listIdsNotasFiscaisEdiInformada;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdProcessamentoEdi() {
        return idProcessamentoEdi;
    }

    public void setIdProcessamentoEdi(Long idProcessamentoEdi) {
        this.idProcessamentoEdi = idProcessamentoEdi;
    }

    public Long getIdMonitoramentoDescarga() {
        return idMonitoramentoDescarga;
    }

    public void setIdMonitoramentoDescarga(Long idMonitoramentoDescarga) {
        this.idMonitoramentoDescarga = idMonitoramentoDescarga;
    }

    public String getTpProcessamento() {
        return tpProcessamento;
    }

    public void setTpProcessamento(String tpProcessamento) {
        this.tpProcessamento = tpProcessamento;
    }

    public Long getIdPedidoColeta() {
        return idPedidoColeta;
    }

    public void setIdPedidoColeta(Long idPedidoColeta) {
        this.idPedidoColeta = idPedidoColeta;
    }

    public Boolean getFinalizou() {
        return finalizou;
    }

    public void setFinalizou(Boolean finalizou) {
        this.finalizou = finalizou;
    }

    public Long getInicioIndex() {
        return inicioIndex;
    }

    public void setInicioIndex(Long inicioIndex) {
        this.inicioIndex = inicioIndex;
    }

    public Long getFinalIndex() {
        return finalIndex;
    }

    public void setFinalIndex(Long finalIndex) {
        this.finalIndex = finalIndex;
    }

    public List<Long> getListIdsNotasFiscaisEdiParcial() {
        return listIdsNotasFiscaisEdiParcial;
    }

    public void setListIdsNotasFiscaisEdiParcial(List<Long> listIdsNotasFiscaisEdiParcial) {
        this.listIdsNotasFiscaisEdiParcial = listIdsNotasFiscaisEdiParcial;
    }

    public Boolean getPrimeiraExecucao() {
        return primeiraExecucao;
    }

    public void setPrimeiraExecucao(Boolean primeiraExecucao) {
        this.primeiraExecucao = primeiraExecucao;
    }

    public Boolean getProcessadoComErro() {
        return processadoComErro;
    }

    public void setProcessadoComErro(Boolean processadoComErro) {
        this.processadoComErro = processadoComErro;
    }

    public String getUuid() {
        return uuid;
    }

    public void gerarUuid() {
        this.uuid = UUID.randomUUID().toString();
    }
}
