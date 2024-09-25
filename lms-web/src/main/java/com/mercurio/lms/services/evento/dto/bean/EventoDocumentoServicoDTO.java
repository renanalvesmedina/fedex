package com.mercurio.lms.services.evento.dto.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EventoDocumentoServicoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("idEventoDocumentoServico")
    private Long idEventoDocumentoServico;

    @JsonProperty("idNotaFiscalConhecimento")
    private Long idNotaFiscalConhecimento;

    @JsonProperty("idDoctoServico")
    private Long idDoctoServico;

    @JsonProperty("idFilialEvento")
    private Long idFilialEvento;

    @JsonProperty("idFilialOrigem")
    private Long idFilialOrigem;

    @JsonProperty("idFilialDestino")
    private Long idFilialDestino;

    @JsonProperty("nrDoctoServico")
    private Long nrDoctoServico;

    @JsonProperty("idWhiteList")
    private Long idWhiteList;

    @JsonProperty("cdEvento")
    private Short cdEvento;

    @JsonProperty("cdLocalizacaoMercadoria")
    private Short cdLocalizacaoMercadoria;

    @JsonProperty("cdOcorrenciaEntrega")
    private Short cdOcorrenciaEntrega;

    @JsonProperty("cdOcorrenciaPendencia")
    private Short cdOcorrenciaPendencia;

    @JsonProperty("nrIdentClienteDevedor")
    private String nrIdentClienteDevedor;

    @JsonProperty("sgFilialEvento")
    private String sgFilialEvento;

    @JsonProperty("sgFilialOrigem")
    private String sgFilialOrigem;

    @JsonProperty("sgFilialDestino")
    private String sgFilialDestino;

    @JsonProperty("tpEvento")
    private String tpEvento;

    @JsonProperty("tpSituacaoEvento")
    private String tpSituacaoEvento;

    @JsonProperty("dsEvento")
    private String dsEvento;

    @JsonProperty("dsLocalizacaoMercadoria")
    private String dsLocalizacaoMercadoria;

    @JsonProperty("dsOcorrenciaEntrega")
    private String dsOcorrenciaEntrega;

    @JsonProperty("dsOcorrenciaPendencia")
    private String dsOcorrenciaPendencia;

    @JsonProperty("tpModal")
    private String tpModal;

    @JsonProperty("tpFrete")
    private String tpFrete;

    @JsonProperty("tpDoctoServico")
    private String tpDoctoServico;

    @JsonProperty("dhEvento")
    private DateTime dhEvento;

    @JsonProperty("blExibeCliente")
    private Boolean blExibeCliente = false;

    @JsonProperty("blArmazenagem")
    private Boolean blArmazenagem = false;

    @JsonProperty("blEventoCancelado")
    private Boolean blEventoCancelado = false;

    @JsonProperty("nomeDestinatario")
    private String nomeDestinatario;

    @JsonProperty("nomeRecebedor")
    private String nomeRecebedor;

    @JsonProperty("dtEntrega")
    private Date dtEntrega;

    @JsonProperty("assinatura")
    private byte[] assinatura;

    @JsonProperty("pedido")
    private String pedido;

    @JsonProperty("cte")
    private String cte;

    @JsonProperty("volumes")
    private Integer volumes;

    @JsonProperty("origemDestino")
    private String origemDestino;

    @JsonProperty("blEnviado")
    private Boolean blEnviado;

    @JsonProperty("numRgRecebedor")
    private String numRgRecebedor;

    @JsonProperty("grauParentesco")
    private String grauParentesco;

    @JsonProperty("grauRisco")
    private Integer grauRisco;

    @JsonProperty("caixaLacrada")
    private String caixaLacrada;

    @JsonProperty("conferido")
    private String conferido;

    @JsonProperty("blOcorrenciaDoctoServicoManual")
    private Boolean blOcorrenciaDoctoServicoManual = false;

    @JsonProperty("complementos")
    private Map<String, String> complementos;

    public EventoDocumentoServicoDTO() {
    }

    public Long getIdEventoDocumentoServico() {
        return idEventoDocumentoServico;
    }

    public void setIdEventoDocumentoServico(Long idEventoDocumentoServico) {
        this.idEventoDocumentoServico = idEventoDocumentoServico;
    }

    public Long getIdNotaFiscalConhecimento() {
        return idNotaFiscalConhecimento;
    }

    public void setIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
        this.idNotaFiscalConhecimento = idNotaFiscalConhecimento;
    }

    public Long getIdDoctoServico() {
        return idDoctoServico;
    }

    public void setIdDoctoServico(Long idDoctoServico) {
        this.idDoctoServico = idDoctoServico;
    }

    public Long getIdFilialEvento() {
        return idFilialEvento;
    }

    public void setIdFilialEvento(Long idFilialEvento) {
        this.idFilialEvento = idFilialEvento;
    }

    public Long getIdFilialOrigem() {
        return idFilialOrigem;
    }

    public void setIdFilialOrigem(Long idFilialOrigem) {
        this.idFilialOrigem = idFilialOrigem;
    }

    public Long getIdFilialDestino() {
        return idFilialDestino;
    }

    public void setIdFilialDestino(Long idFilialDestino) {
        this.idFilialDestino = idFilialDestino;
    }

    public Long getNrDoctoServico() {
        return nrDoctoServico;
    }

    public void setNrDoctoServico(Long nrDoctoServico) {
        this.nrDoctoServico = nrDoctoServico;
    }

    public Long getIdWhiteList() {
        return idWhiteList;
    }

    public void setIdWhiteList(Long idWhiteList) {
        this.idWhiteList = idWhiteList;
    }

    public Short getCdEvento() {
        return cdEvento;
    }

    public void setCdEvento(Short cdEvento) {
        this.cdEvento = cdEvento;
    }

    public Short getCdLocalizacaoMercadoria() {
        return cdLocalizacaoMercadoria;
    }

    public void setCdLocalizacaoMercadoria(Short cdLocalizacaoMercadoria) {
        this.cdLocalizacaoMercadoria = cdLocalizacaoMercadoria;
    }

    public Short getCdOcorrenciaEntrega() {
        return cdOcorrenciaEntrega;
    }

    public void setCdOcorrenciaEntrega(Short cdOcorrenciaEntrega) {
        this.cdOcorrenciaEntrega = cdOcorrenciaEntrega;
    }

    public Short getCdOcorrenciaPendencia() {
        return cdOcorrenciaPendencia;
    }

    public void setCdOcorrenciaPendencia(Short cdOcorrenciaPendencia) {
        this.cdOcorrenciaPendencia = cdOcorrenciaPendencia;
    }

    public String getNrIdentClienteDevedor() {
        return nrIdentClienteDevedor;
    }

    public void setNrIdentClienteDevedor(String nrIdentClienteDevedor) {
        this.nrIdentClienteDevedor = nrIdentClienteDevedor;
    }

    public String getSgFilialEvento() {
        return sgFilialEvento;
    }

    public void setSgFilialEvento(String sgFilialEvento) {
        this.sgFilialEvento = sgFilialEvento;
    }

    public String getSgFilialOrigem() {
        return sgFilialOrigem;
    }

    public void setSgFilialOrigem(String sgFilialOrigem) {
        this.sgFilialOrigem = sgFilialOrigem;
    }

    public String getSgFilialDestino() {
        return sgFilialDestino;
    }

    public void setSgFilialDestino(String sgFilialDestino) {
        this.sgFilialDestino = sgFilialDestino;
    }

    public String getTpEvento() {
        return tpEvento;
    }

    public void setTpEvento(String tpEvento) {
        this.tpEvento = tpEvento;
    }

    public String getTpSituacaoEvento() {
        return tpSituacaoEvento;
    }

    public void setTpSituacaoEvento(String tpSituacaoEvento) {
        this.tpSituacaoEvento = tpSituacaoEvento;
    }

    public String getDsEvento() {
        return dsEvento;
    }

    public void setDsEvento(String dsEvento) {
        this.dsEvento = dsEvento;
    }

    public String getDsLocalizacaoMercadoria() {
        return dsLocalizacaoMercadoria;
    }

    public void setDsLocalizacaoMercadoria(String dsLocalizacaoMercadoria) {
        this.dsLocalizacaoMercadoria = dsLocalizacaoMercadoria;
    }

    public String getDsOcorrenciaEntrega() {
        return dsOcorrenciaEntrega;
    }

    public void setDsOcorrenciaEntrega(String dsOcorrenciaEntrega) {
        this.dsOcorrenciaEntrega = dsOcorrenciaEntrega;
    }

    public String getDsOcorrenciaPendencia() {
        return dsOcorrenciaPendencia;
    }

    public void setDsOcorrenciaPendencia(String dsOcorrenciaPendencia) {
        this.dsOcorrenciaPendencia = dsOcorrenciaPendencia;
    }

    public String getTpModal() {
        return tpModal;
    }

    public void setTpModal(String tpModal) {
        this.tpModal = tpModal;
    }

    public String getTpFrete() {
        return tpFrete;
    }

    public void setTpFrete(String tpFrete) {
        this.tpFrete = tpFrete;
    }

    public String getTpDoctoServico() {
        return tpDoctoServico;
    }

    public void setTpDoctoServico(String tpDoctoServico) {
        this.tpDoctoServico = tpDoctoServico;
    }

    public DateTime getDhEvento() {
        return dhEvento;
    }

    public void setDhEvento(DateTime dhEvento) {
        this.dhEvento = dhEvento;
    }

    public Boolean getBlExibeCliente() {
        return blExibeCliente;
    }

    public void setBlExibeCliente(Boolean blExibeCliente) {
        this.blExibeCliente = blExibeCliente;
    }

    public Boolean getBlArmazenagem() {
        return blArmazenagem;
    }

    public void setBlArmazenagem(Boolean blArmazenagem) {
        this.blArmazenagem = blArmazenagem;
    }

    public Boolean getBlEventoCancelado() {
        return blEventoCancelado;
    }

    public void setBlEventoCancelado(Boolean blEventoCancelado) {
        this.blEventoCancelado = blEventoCancelado;
    }

    public String getNomeDestinatario() {
        return nomeDestinatario;
    }

    public void setNomeDestinatario(String nomeDestinatario) {
        this.nomeDestinatario = nomeDestinatario;
    }

    public String getNomeRecebedor() {
        return nomeRecebedor;
    }

    public void setNomeRecebedor(String nomeRecebedor) {
        this.nomeRecebedor = nomeRecebedor;
    }

    public Date getDtEntrega() {
        return dtEntrega;
    }

    public void setDtEntrega(Date dtEntrega) {
        this.dtEntrega = dtEntrega;
    }

    public byte[] getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(byte[] assinatura) {
        this.assinatura = assinatura;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getCte() {
        return cte;
    }

    public void setCte(String cte) {
        this.cte = cte;
    }

    public Integer getVolumes() {
        return volumes;
    }

    public void setVolumes(Integer volumes) {
        this.volumes = volumes;
    }

    public String getOrigemDestino() {
        return origemDestino;
    }

    public void setOrigemDestino(String origemDestino) {
        this.origemDestino = origemDestino;
    }

    public Boolean getBlEnviado() {
        return blEnviado;
    }

    public void setBlEnviado(Boolean blEnviado) {
        this.blEnviado = blEnviado;
    }

    public String getNumRgRecebedor() {
        return numRgRecebedor;
    }

    public void setNumRgRecebedor(String numRgRecebedor) {
        this.numRgRecebedor = numRgRecebedor;
    }

    public String getGrauParentesco() {
        return grauParentesco;
    }

    public void setGrauParentesco(String grauParentesco) {
        this.grauParentesco = grauParentesco;
    }

    public Integer getGrauRisco() {
        return grauRisco;
    }

    public void setGrauRisco(Integer grauRisco) {
        this.grauRisco = grauRisco;
    }

    public String getCaixaLacrada() {
        return caixaLacrada;
    }

    public void setCaixaLacrada(String caixaLacrada) {
        this.caixaLacrada = caixaLacrada;
    }

    public String getConferido() {
        return conferido;
    }

    public void setConferido(String conferido) {
        this.conferido = conferido;
    }

    public Boolean getBlOcorrenciaDoctoServicoManual() {
        return blOcorrenciaDoctoServicoManual;
    }

    public void setBlOcorrenciaDoctoServicoManual(Boolean blOcorrenciaDoctoServicoManual) {
        this.blOcorrenciaDoctoServicoManual = blOcorrenciaDoctoServicoManual;
    }

    public Map<String, String> getComplementos() {
        return complementos;
    }

    public void setComplementos(Map<String, String> complementos) {
        this.complementos = complementos;
    }

    public EventoDocumentoServicoDTO(Long idDoctoServico, Long idFilialEvento, Long idFilialOrigem,
                                     Long idFilialDestino, Long nrDoctoServico, Long idWhiteList, Short cdEvento,
                                     Short cdLocalizacaoMercadoria, Short cdOcorrenciaEntrega,
                                     Short cdOcorrenciaPendencia, String nrIdentClienteDevedor,
                                     String sgFilialEvento, String sgFilialOrigem, String sgFilialDestino,
                                     String tpEvento, String tpSituacaoEvento, String dsEvento,
                                     String dsLocalizacaoMercadoria, String dsOcorrenciaEntrega,
                                     String dsOcorrenciaPendencia, String tpModal, String tpFrete,
                                     String tpDoctoServico, DateTime dhEvento, Boolean blExibeCliente,
                                     Boolean blArmazenagem, Boolean blEventoCancelado, String nomeDestinatario,
                                     String nomeRecebedor, Date dtEntrega, byte[] assinatura, String pedido,
                                     String cte, Integer volumes, String origemDestino, Boolean blEnviado,
                                     String numRgRecebedor, String grauParentesco, Integer grauRisco,
                                     String caixaLacrada, String conferido, Boolean blOcorrenciaDoctoServicoManual,
                                     Map<String, String> complementos) {
        this.idDoctoServico = idDoctoServico;
        this.idFilialEvento = idFilialEvento;
        this.idFilialOrigem = idFilialOrigem;
        this.idFilialDestino = idFilialDestino;
        this.nrDoctoServico = nrDoctoServico;
        this.idWhiteList = idWhiteList;
        this.cdEvento = cdEvento;
        this.cdLocalizacaoMercadoria = cdLocalizacaoMercadoria;
        this.cdOcorrenciaEntrega = cdOcorrenciaEntrega;
        this.cdOcorrenciaPendencia = cdOcorrenciaPendencia;
        this.nrIdentClienteDevedor = nrIdentClienteDevedor;
        this.sgFilialEvento = sgFilialEvento;
        this.sgFilialOrigem = sgFilialOrigem;
        this.sgFilialDestino = sgFilialDestino;
        this.tpEvento = tpEvento;
        this.tpSituacaoEvento = tpSituacaoEvento;
        this.dsEvento = dsEvento;
        this.dsLocalizacaoMercadoria = dsLocalizacaoMercadoria;
        this.dsOcorrenciaEntrega = dsOcorrenciaEntrega;
        this.dsOcorrenciaPendencia = dsOcorrenciaPendencia;
        this.tpModal = tpModal;
        this.tpFrete = tpFrete;
        this.tpDoctoServico = tpDoctoServico;
        this.dhEvento = dhEvento;
        this.blExibeCliente = blExibeCliente;
        this.blArmazenagem = blArmazenagem;
        this.blEventoCancelado = blEventoCancelado;
        this.nomeDestinatario = nomeDestinatario;
        this.nomeRecebedor = nomeRecebedor;
        this.dtEntrega = dtEntrega;
        this.assinatura = assinatura;
        this.pedido = pedido;
        this.cte = cte;
        this.volumes = volumes;
        this.origemDestino = origemDestino;
        this.blEnviado = blEnviado;
        this.numRgRecebedor = numRgRecebedor;
        this.grauParentesco = grauParentesco;
        this.grauRisco = grauRisco;
        this.caixaLacrada = caixaLacrada;
        this.conferido = conferido;
        this.blOcorrenciaDoctoServicoManual = blOcorrenciaDoctoServicoManual;
        this.complementos = complementos;
    }
}
