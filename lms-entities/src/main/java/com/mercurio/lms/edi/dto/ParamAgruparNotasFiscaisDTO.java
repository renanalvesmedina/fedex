package com.mercurio.lms.edi.dto;

import com.mercurio.lms.edi.enums.StatusProcessamento;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ParamAgruparNotasFiscaisDTO {

    private Long idInscricaoEstadualRemetente;
    private Map<String, Object> mapDataHoraNotaFiscal;
    private List<Map> listNumeroEtiquetasEdiInformada;
    private List<DadosValidacaoEdiDTO> listaValidacao;
    private String opcaoProcessamento;
    private Map<String, Object> mapMeioTransporte;
    private DateTime dhChegada;
    private Long idPedidoColeta;
    private String tpProcessamento;
    private String processarPor;
    private Long idDivisaoCliente;
    private boolean blOperacaoSpitFire;
    private List<Long> listIdsNotasFiscaisEdiInformada;
    private Long nrProcessamento;
    private Long idPedidoContato;
    private Long idFilial;
    private StatusProcessamento status;
    private Long idUsuario;
    private Long idEmpresa;

    public Long getIdInscricaoEstadualRemetente() {
        return idInscricaoEstadualRemetente;
    }

    public void setIdInscricaoEstadualRemetente(Long idInscricaoEstadualRemetente) {
        this.idInscricaoEstadualRemetente = idInscricaoEstadualRemetente;
    }

    public Map<String, Object> getMapDataHoraNotaFiscal() {
        return mapDataHoraNotaFiscal;
    }

    public void setMapDataHoraNotaFiscal(Map<String, Object> mapDataHoraNotaFiscal) {
        this.mapDataHoraNotaFiscal = mapDataHoraNotaFiscal;
    }

    public List<Map> getListNumeroEtiquetasEdiInformada() {
        return listNumeroEtiquetasEdiInformada;
    }

    public void setListNumeroEtiquetasEdiInformada(List<Map> listNumeroEtiquetasEdiInformada) {
        this.listNumeroEtiquetasEdiInformada = listNumeroEtiquetasEdiInformada;
    }

    public List<DadosValidacaoEdiDTO> getListaValidacao() {
        return listaValidacao;
    }

    public void setListaValidacao(List<DadosValidacaoEdiDTO> listaValidacao) {
        this.listaValidacao = listaValidacao;
    }

    public String getOpcaoProcessamento() {
        return opcaoProcessamento;
    }

    public void setOpcaoProcessamento(String opcaoProcessamento) {
        this.opcaoProcessamento = opcaoProcessamento;
    }

    public Map<String, Object> getMapMeioTransporte() {
        return mapMeioTransporte;
    }

    public void setMapMeioTransporte(Map<String, Object> mapMeioTransporte) {
        this.mapMeioTransporte = mapMeioTransporte;
    }

    public DateTime getDhChegada() {
        return dhChegada;
    }

    public void setDhChegada(DateTime dhChegada) {
        this.dhChegada = dhChegada;
    }

    public Long getIdPedidoColeta() {
        return idPedidoColeta;
    }

    public void setIdPedidoColeta(Long idPedidoColeta) {
        this.idPedidoColeta = idPedidoColeta;
    }

    public String getTpProcessamento() {
        return tpProcessamento;
    }

    public void setTpProcessamento(String tpProcessamento) {
        this.tpProcessamento = tpProcessamento;
    }

    public String getProcessarPor() {
        return processarPor;
    }

    public void setProcessarPor(String processarPor) {
        this.processarPor = processarPor;
    }

    public Long getIdDivisaoCliente() {
        return idDivisaoCliente;
    }

    public void setIdDivisaoCliente(Long idDivisaoCliente) {
        this.idDivisaoCliente = idDivisaoCliente;
    }

    public boolean getBlOperacaoSpitFire() {
        return blOperacaoSpitFire;
    }

    public void setBlOperacaoSpitFire(boolean blOperacaoSpitFire) {
        this.blOperacaoSpitFire = blOperacaoSpitFire;
    }

    public List<Long> getListIdsNotasFiscaisEdiInformada() {
        return listIdsNotasFiscaisEdiInformada;
    }

    public void setListIdsNotasFiscaisEdiInformada(List<Long> listIdsNotasFiscaisEdiInformada) {
        this.listIdsNotasFiscaisEdiInformada = listIdsNotasFiscaisEdiInformada;
    }

    public Long getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    public Long getIdPedidoContato() {
        return idPedidoContato;
    }

    public void setIdPedidoContato(Long idPedidoContato) {
        this.idPedidoContato = idPedidoContato;
    }

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }

    public StatusProcessamento getStatus() {
        return status;
    }

    public void setStatus(StatusProcessamento status) {
        this.status = status;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}
