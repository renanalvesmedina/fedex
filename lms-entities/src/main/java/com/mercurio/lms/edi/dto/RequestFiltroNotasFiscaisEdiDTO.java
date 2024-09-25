package com.mercurio.lms.edi.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestFiltroNotasFiscaisEdiDTO implements Serializable {

    private String opcaoProcessamento;
    private Long idPedidoColeta;
    private Map<String, Object> mapDataHoraNotaFiscal;
    private Boolean blProdutoDiferenciado;
    private String dsInformacaoDoctoCliente;
    private Long idInscricaoEstadualRemetente;
    private Boolean blOperacaoSpitFire;
    private Map<String, Object> meioTransporte;
    private String nrIdentificacao;
    private Long idMeioTransporte;
    private Long idCliente;
    private List<NotaFiscalProdutoDiferenciadoEdiDTO> notasFiscaisProdutosDiferenciados;
    private String processarPor;
    private Long idClienteRemetente;
    private String tpProcessamento;
    private DateTime dhChegada;
    private Long idDivisaoCliente;
    private List<?> listNotasFiscaisEdiInformada;
    private List<Map<String, Integer>> listIntervalosNotasFiscaisEdi;
    private List<String> chavesNfe;
    private String nrCce;
    private Long idFilial;
    private String sgFilial;
    private Long idUsuario;


    public String getProcessarPor() {
        return processarPor;
    }

    public void setProcessarPor(String processarPor) {
        this.processarPor = processarPor;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getOpcaoProcessamento() {
        return opcaoProcessamento;
    }

    public void setOpcaoProcessamento(String opcaoProcessamento) {
        this.opcaoProcessamento = opcaoProcessamento;
    }

    public String getTpProcessamento() {
        return tpProcessamento;
    }

    public void setTpProcessamento(String tpProcessamento) {
        this.tpProcessamento = tpProcessamento;
    }

    public List<?> getListNotasFiscaisEdiInformada() {
        return listNotasFiscaisEdiInformada;
    }

    public void setListNotasFiscaisEdiInformada(List<?> listNotasFiscaisEdiInformada) {
        this.listNotasFiscaisEdiInformada = listNotasFiscaisEdiInformada;
    }

    public List<Map<String, Integer>> getListIntervalosNotasFiscaisEdi() {
        return listIntervalosNotasFiscaisEdi;
    }

    public void setListIntervalosNotasFiscaisEdi(List<Map<String, Integer>> listIntervalosNotasFiscaisEdi) {
        this.listIntervalosNotasFiscaisEdi = listIntervalosNotasFiscaisEdi;
    }

    public List<String> getChavesNfe() {
        return chavesNfe;
    }

    public void setChavesNfe(List<String> chavesNfe) {
        this.chavesNfe = chavesNfe;
    }

    public String getNrCce() {
        return nrCce;
    }

    public void setNrCce(String nrCce) {
        this.nrCce = nrCce;
    }

    public Long getIdPedidoColeta() {
        return idPedidoColeta;
    }

    public void setIdPedidoColeta(Long idPedidoColeta) {
        this.idPedidoColeta = idPedidoColeta;
    }

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }

    public Map<String, Object> getMapDataHoraNotaFiscal() {
        return mapDataHoraNotaFiscal;
    }

    public void setMapDataHoraNotaFiscal(Map<String, Object> mapDataHoraNotaFiscal) {
        this.mapDataHoraNotaFiscal = mapDataHoraNotaFiscal;
    }

    public List<NotaFiscalProdutoDiferenciadoEdiDTO> getNotasFiscaisProdutosDiferenciados() {
        return notasFiscaisProdutosDiferenciados;
    }

    public void setNotasFiscaisProdutosDiferenciados(List<NotaFiscalProdutoDiferenciadoEdiDTO> notasFiscaisProdutosDiferenciados) {
        this.notasFiscaisProdutosDiferenciados = notasFiscaisProdutosDiferenciados;
    }

    public Boolean getBlProdutoDiferenciado() {
        return blProdutoDiferenciado;
    }

    public void setBlProdutoDiferenciado(Boolean blProdutoDiferenciado) {
        this.blProdutoDiferenciado = blProdutoDiferenciado;
    }

    public String getDsInformacaoDoctoCliente() {
        return dsInformacaoDoctoCliente;
    }

    public void setDsInformacaoDoctoCliente(String dsInformacaoDoctoCliente) {
        this.dsInformacaoDoctoCliente = dsInformacaoDoctoCliente;
    }

    public Long getIdInscricaoEstadualRemetente() {
        return idInscricaoEstadualRemetente;
    }

    public void setIdInscricaoEstadualRemetente(Long idInscricaoEstadualRemetente) {
        this.idInscricaoEstadualRemetente = idInscricaoEstadualRemetente;
    }

    public Boolean getBlOperacaoSpitFire() {
        return blOperacaoSpitFire;
    }

    public void setBlOperacaoSpitFire(Boolean blOperacaoSpitFire) {
        this.blOperacaoSpitFire = blOperacaoSpitFire;
    }

    public Map<String, Object> getMeioTransporte() {
        return meioTransporte;
    }

    public void setMeioTransporte(Map<String, Object> meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public String getNrIdentificacao() {
        return nrIdentificacao;
    }

    public void setNrIdentificacao(String nrIdentificacao) {
        this.nrIdentificacao = nrIdentificacao;
    }

    public Long getIdMeioTransporte() {
        return idMeioTransporte;
    }

    public void setIdMeioTransporte(Long idMeioTransporte) {
        this.idMeioTransporte = idMeioTransporte;
    }

    public Long getIdClienteRemetente() {
        return idClienteRemetente;
    }

    public void setIdClienteRemetente(Long idClienteRemetente) {
        this.idClienteRemetente = idClienteRemetente;
    }

    public DateTime getDhChegada() {
        return dhChegada;
    }

    public void setDhChegada(DateTime dhChegada) {
        this.dhChegada = dhChegada;
    }

    public Long getIdDivisaoCliente() {
        return idDivisaoCliente;
    }

    public void setIdDivisaoCliente(Long idDivisaoCliente) {
        this.idDivisaoCliente = idDivisaoCliente;
    }

    public String getSgFilial() {
        return sgFilial;
    }

    public void setSgFilial(String sgFilial) {
        this.sgFilial = sgFilial;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
