package com.mercurio.lms.expedicao.dto;

import com.mercurio.lms.vendas.dto.ClienteDTO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ParameterDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idMunicipioRemetente;
    private ClienteDTO clienteRemetente;
    private DadosComplemntoDto mapDadosComplementos;
    private Long idInscricaoEstadualRemetente;
    private Integer quantidadeNotasFiscais;
    private Boolean blColetaEmergncia;
    private String tpClienteRemetente;
    private String tpCalculoPreco;
    private List<NotaFiscalConhecimentoDto> notaFiscalConhecimento;
    private Long idUnidadeFederativaDestinatario;
    private Boolean blEntregaEmergencia;
    private Map<String, Object> mapDataHoraNotaFiscal;
    private Map<String, Object> meioTransporte;
    private String nrIdentificacaoDestinatario;
    private String nrCepDestinatario;
    private Long idServico;
    private Long idMunicipioDestinatario;
    private String tpFrete;
    private String tpDevedorFrete;
    private Long idClienteRemetente;
    private ConhecimentoDto conhecimento;
    private Long idInscricaoEstadualDestinatario;
    private Long idClienteDestinatario;
    private Boolean blUsaEnderecoConsignatario;
    private Boolean blCotacaoRemetente;
    private String ieRedesp;
    private Long idUnidadeFederativaRemetente;
    private Boolean blFrete;
    private Long idPedidoColeta;
    private String dsNatureza;
    private String nrConhecimentoSubcontratante;
    private String nrCepRemetente;
    private String tpModalServico;
    private String tpClienteDestinatario;
    private Boolean blServicosAdicionais;
    private String tpProcessamento;
    private String nrIdentificacaoRemetente;
    private String dhChegada;
    private Long idAeroportoOrigem;
    private Long idAeroportoDestino;
    private Long idDivisaoCliente;
    private String dadosEnderecoEntregaReal;
    private Boolean validaLimiteValorMercadoria;
    private String opcaoProcessamento;
    private String tpAbrangenciaServico;
    private Boolean blColetaEmergencia;

    public Long getIdMunicipioRemetente() {
        return idMunicipioRemetente;
    }

    public void setIdMunicipioRemetente(Long idMunicipioRemetente) {
        this.idMunicipioRemetente = idMunicipioRemetente;
    }

    public ClienteDTO getClienteRemetente() {
        return clienteRemetente;
    }

    public void setClienteRemetente(ClienteDTO clienteRemetente) {
        this.clienteRemetente = clienteRemetente;
    }

    public DadosComplemntoDto getMapDadosComplementos() {
        return mapDadosComplementos;
    }

    public void setMapDadosComplementos(DadosComplemntoDto mapDadosComplementos) {
        this.mapDadosComplementos = mapDadosComplementos;
    }

    public Long getIdInscricaoEstadualRemetente() {
        return idInscricaoEstadualRemetente;
    }

    public void setIdInscricaoEstadualRemetente(Long idInscricaoEstadualRemetente) {
        this.idInscricaoEstadualRemetente = idInscricaoEstadualRemetente;
    }

    public Integer getQuantidadeNotasFiscais() {
        return quantidadeNotasFiscais;
    }

    public void setQuantidadeNotasFiscais(Integer quantidadeNotasFiscais) {
        this.quantidadeNotasFiscais = quantidadeNotasFiscais;
    }

    public Boolean getBlColetaEmergncia() {
        return blColetaEmergncia;
    }

    public void setBlColetaEmergncia(Boolean blColetaEmergncia) {
        this.blColetaEmergncia = blColetaEmergncia;
    }

    public String getTpClienteRemetente() {
        return tpClienteRemetente;
    }

    public void setTpClienteRemetente(String tpClienteRemetente) {
        this.tpClienteRemetente = tpClienteRemetente;
    }

    public String getTpCalculoPreco() {
        return tpCalculoPreco;
    }

    public void setTpCalculoPreco(String tpCalculoPreco) {
        this.tpCalculoPreco = tpCalculoPreco;
    }

    public List<NotaFiscalConhecimentoDto> getNotaFiscalConhecimento() {
        return notaFiscalConhecimento;
    }

    public void setNotaFiscalConhecimento(List<NotaFiscalConhecimentoDto> notaFiscalConhecimento) {
        this.notaFiscalConhecimento = notaFiscalConhecimento;
    }

    public Long getIdUnidadeFederativaDestinatario() {
        return idUnidadeFederativaDestinatario;
    }

    public void setIdUnidadeFederativaDestinatario(Long idUnidadeFederativaDestinatario) {
        this.idUnidadeFederativaDestinatario = idUnidadeFederativaDestinatario;
    }

    public Boolean getBlEntregaEmergencia() {
        return blEntregaEmergencia;
    }

    public void setBlEntregaEmergencia(Boolean blEntregaEmergencia) {
        this.blEntregaEmergencia = blEntregaEmergencia;
    }

    public Map<String, Object> getMapDataHoraNotaFiscal() {
        return mapDataHoraNotaFiscal;
    }

    public void setMapDataHoraNotaFiscal(Map<String, Object> mapDataHoraNotaFiscal) {
        this.mapDataHoraNotaFiscal = mapDataHoraNotaFiscal;
    }

    public String getNrIdentificacaoDestinatario() {
        return nrIdentificacaoDestinatario;
    }

    public void setNrIdentificacaoDestinatario(String nrIdentificacaoDestinatario) {
        this.nrIdentificacaoDestinatario = nrIdentificacaoDestinatario;
    }

    public String getNrCepDestinatario() {
        return nrCepDestinatario;
    }

    public void setNrCepDestinatario(String nrCepDestinatario) {
        this.nrCepDestinatario = nrCepDestinatario;
    }

    public Long getIdServico() {
        return idServico;
    }

    public void setIdServico(Long idServico) {
        this.idServico = idServico;
    }

    public Long getIdMunicipioDestinatario() {
        return idMunicipioDestinatario;
    }

    public void setIdMunicipioDestinatario(Long idMunicipioDestinatario) {
        this.idMunicipioDestinatario = idMunicipioDestinatario;
    }

    public String getTpFrete() {
        return tpFrete;
    }

    public void setTpFrete(String tpFrete) {
        this.tpFrete = tpFrete;
    }

    public String getTpDevedorFrete() {
        return tpDevedorFrete;
    }

    public void setTpDevedorFrete(String tpDevedorFrete) {
        this.tpDevedorFrete = tpDevedorFrete;
    }

    public Long getIdClienteRemetente() {
        return idClienteRemetente;
    }

    public void setIdClienteRemetente(Long idClienteRemetente) {
        this.idClienteRemetente = idClienteRemetente;
    }

    public ConhecimentoDto getConhecimento() {
        return conhecimento;
    }

    public void setConhecimento(ConhecimentoDto conhecimento) {
        this.conhecimento = conhecimento;
    }

    public Long getIdInscricaoEstadualDestinatario() {
        return idInscricaoEstadualDestinatario;
    }

    public void setIdInscricaoEstadualDestinatario(Long idInscricaoEstadualDestinatario) {
        this.idInscricaoEstadualDestinatario = idInscricaoEstadualDestinatario;
    }

    public Long getIdClienteDestinatario() {
        return idClienteDestinatario;
    }

    public void setIdClienteDestinatario(Long idClienteDestinatario) {
        this.idClienteDestinatario = idClienteDestinatario;
    }

    public Boolean getBlUsaEnderecoConsignatario() {
        return blUsaEnderecoConsignatario;
    }

    public void setBlUsaEnderecoConsignatario(Boolean blUsaEnderecoConsignatario) {
        this.blUsaEnderecoConsignatario = blUsaEnderecoConsignatario;
    }

    public Boolean getBlCotacaoRemetente() {
        return blCotacaoRemetente;
    }

    public void setBlCotacaoRemetente(Boolean blCotacaoRemetente) {
        this.blCotacaoRemetente = blCotacaoRemetente;
    }

    public String getIeRedesp() {
        return ieRedesp;
    }

    public void setIeRedesp(String ieRedesp) {
        this.ieRedesp = ieRedesp;
    }

    public Long getIdUnidadeFederativaRemetente() {
        return idUnidadeFederativaRemetente;
    }

    public void setIdUnidadeFederativaRemetente(Long idUnidadeFederativaRemetente) {
        this.idUnidadeFederativaRemetente = idUnidadeFederativaRemetente;
    }

    public Boolean getBlFrete() {
        return blFrete;
    }

    public void setBlFrete(Boolean blFrete) {
        this.blFrete = blFrete;
    }

    public Long getIdPedidoColeta() {
        return idPedidoColeta;
    }

    public void setIdPedidoColeta(Long idPedidoColeta) {
        this.idPedidoColeta = idPedidoColeta;
    }

    public String getDsNatureza() {
        return dsNatureza;
    }

    public void setDsNatureza(String dsNatureza) {
        this.dsNatureza = dsNatureza;
    }

    public String getNrConhecimentoSubcontratante() {
        return nrConhecimentoSubcontratante;
    }

    public void setNrConhecimentoSubcontratante(String nrConhecimentoSubcontratante) {
        this.nrConhecimentoSubcontratante = nrConhecimentoSubcontratante;
    }

    public String getNrCepRemetente() {
        return nrCepRemetente;
    }

    public void setNrCepRemetente(String nrCepRemetente) {
        this.nrCepRemetente = nrCepRemetente;
    }

    public String getTpModalServico() {
        return tpModalServico;
    }

    public void setTpModalServico(String tpModalServico) {
        this.tpModalServico = tpModalServico;
    }

    public String getTpClienteDestinatario() {
        return tpClienteDestinatario;
    }

    public void setTpClienteDestinatario(String tpClienteDestinatario) {
        this.tpClienteDestinatario = tpClienteDestinatario;
    }

    public Boolean getBlServicosAdicionais() {
        return blServicosAdicionais;
    }

    public void setBlServicosAdicionais(Boolean blServicosAdicionais) {
        this.blServicosAdicionais = blServicosAdicionais;
    }

    public String getTpProcessamento() {
        return tpProcessamento;
    }

    public void setTpProcessamento(String tpProcessamento) {
        this.tpProcessamento = tpProcessamento;
    }

    public String getNrIdentificacaoRemetente() {
        return nrIdentificacaoRemetente;
    }

    public void setNrIdentificacaoRemetente(String nrIdentificacaoRemetente) {
        this.nrIdentificacaoRemetente = nrIdentificacaoRemetente;
    }

    public String getDhChegada() {
        return dhChegada;
    }

    public void setDhChegada(String dhChegada) {
        this.dhChegada = dhChegada;
    }

    public Long getIdAeroportoOrigem() {
        return idAeroportoOrigem;
    }

    public void setIdAeroportoOrigem(Long idAeroportoOrigem) {
        this.idAeroportoOrigem = idAeroportoOrigem;
    }

    public Long getIdAeroportoDestino() {
        return idAeroportoDestino;
    }

    public void setIdAeroportoDestino(Long idAeroportoDestino) {
        this.idAeroportoDestino = idAeroportoDestino;
    }

    public Long getIdDivisaoCliente() {
        return idDivisaoCliente;
    }

    public void setIdDivisaoCliente(Long idDivisaoCliente) {
        this.idDivisaoCliente = idDivisaoCliente;
    }

    public String getDadosEnderecoEntregaReal() {
        return dadosEnderecoEntregaReal;
    }

    public void setDadosEnderecoEntregaReal(String dadosEnderecoEntregaReal) {
        this.dadosEnderecoEntregaReal = dadosEnderecoEntregaReal;
    }

    public Boolean getValidaLimiteValorMercadoria() {
        return validaLimiteValorMercadoria;
    }

    public void setValidaLimiteValorMercadoria(Boolean validaLimiteValorMercadoria) {
        this.validaLimiteValorMercadoria = validaLimiteValorMercadoria;
    }

    public String getOpcaoProcessamento() {
        return opcaoProcessamento;
    }

    public void setOpcaoProcessamento(String opcaoProcessamento) {
        this.opcaoProcessamento = opcaoProcessamento;
    }

    public String getTpAbrangenciaServico() {
        return tpAbrangenciaServico;
    }

    public void setTpAbrangenciaServico(String tpAbrangenciaServico) {
        this.tpAbrangenciaServico = tpAbrangenciaServico;
    }

    public Map<String, Object> getMeioTransporte() {
        return meioTransporte;
    }

    public void setMeioTransporte(Map<String, Object> meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public Boolean getBlColetaEmergencia() {
        return blColetaEmergencia;
    }

    public void setBlColetaEmergencia(Boolean blColetaEmergencia) {
        this.blColetaEmergencia = blColetaEmergencia;
    }
}
