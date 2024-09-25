package com.mercurio.lms.sim.model;

import java.io.Serializable;

import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;

/**
 * @author LMS Custom Hibernate CodeGenerator
 */
public class EventoDocumentoServico implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * identifier field
     */
    private Long idEventoDocumentoServico;

    /**
     * persistent field
     */
    private DateTime dhEvento;

    /**
     * persistent field
     */
    private DateTime dhInclusao;

    /**
     * persistent field
     */
    private Boolean blEventoCancelado;

    /**
     * nullable persistent field
     */
    private String nrDocumento;

    /**
     * nullable persistent field
     */
    private String obComplemento;

    /**
     * nullable persistent field
     */
    private DateTime dhGeracaoParceiro;

    /**
     * nullable persistent field
     */
    private DateTime dhComunicacao;

    private DomainValue tpDocumento;

    /**
     * persistent field
     */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /**
     * persistent field
     */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /**
     * persistent field
     */
    private com.mercurio.lms.sim.model.Evento evento;

    /**
     * persistent field
     */
    private com.mercurio.lms.municipios.model.Filial filial;

    /**
     * persistent field
     */
    private com.mercurio.lms.sim.model.PedidoCompra pedidoCompra;

    /**
     * persistent field
     */
    private OcorrenciaEntrega ocorrenciaEntrega;

    /**
     * persistent field
     */
    private OcorrenciaPendencia ocorrenciaPendencia;

    public EventoDocumentoServico() {
    }

    public EventoDocumentoServico(Long idEventoDocumentoServico, DateTime dhEvento, DateTime dhInclusao, Boolean blEventoCancelado, String nrDocumento, String obComplemento, DateTime dhGeracaoParceiro, DateTime dhComunicacao, DomainValue tpDocumento, Usuario usuario, DoctoServico doctoServico, Evento evento, Filial filial, PedidoCompra pedidoCompra, OcorrenciaEntrega ocorrenciaEntrega, OcorrenciaPendencia ocorrenciaPendencia) {
        this.idEventoDocumentoServico = idEventoDocumentoServico;
        this.dhEvento = dhEvento;
        this.dhInclusao = dhInclusao;
        this.blEventoCancelado = blEventoCancelado;
        this.nrDocumento = nrDocumento;
        this.obComplemento = obComplemento;
        this.dhGeracaoParceiro = dhGeracaoParceiro;
        this.dhComunicacao = dhComunicacao;
        this.tpDocumento = tpDocumento;
        this.usuario = usuario;
        this.doctoServico = doctoServico;
        this.evento = evento;
        this.filial = filial;
        this.pedidoCompra = pedidoCompra;
        this.ocorrenciaEntrega = ocorrenciaEntrega;
        this.ocorrenciaPendencia = ocorrenciaPendencia;
    }

    public Long getIdEventoDocumentoServico() {
        return this.idEventoDocumentoServico;
    }

    public void setIdEventoDocumentoServico(Long idEventoDocumentoServico) {
        this.idEventoDocumentoServico = idEventoDocumentoServico;
    }

    public Boolean getBlEventoCancelado() {
        return this.blEventoCancelado;
    }

    public void setBlEventoCancelado(Boolean blEventoCancelado) {
        this.blEventoCancelado = blEventoCancelado;
    }

    public String getNrDocumento() {
        return this.nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public String getObComplemento() {
        return this.obComplemento;
    }

    public void setObComplemento(String obComplemento) {
        this.obComplemento = obComplemento;
    }

    public DateTime getDhComunicacao() {
        return dhComunicacao;
    }

    public void setDhComunicacao(DateTime dhComunicacao) {
        this.dhComunicacao = dhComunicacao;
    }

    public DateTime getDhEvento() {
        return dhEvento;
    }

    public void setDhEvento(DateTime dhEvento) {
        this.dhEvento = dhEvento;
    }

    public DateTime getDhGeracaoParceiro() {
        return dhGeracaoParceiro;
    }

    public void setDhGeracaoParceiro(DateTime dhGeracaoParceiro) {
        this.dhGeracaoParceiro = dhGeracaoParceiro;
    }

    public DateTime getDhInclusao() {
        return dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    public DomainValue getTpDocumento() {
        return tpDocumento;
    }

    public void setTpDocumento(DomainValue tpDocumento) {
        this.tpDocumento = tpDocumento;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

    public void setDoctoServico(
            com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public com.mercurio.lms.sim.model.Evento getEvento() {
        return this.evento;
    }

    public void setEvento(com.mercurio.lms.sim.model.Evento evento) {
        this.evento = evento;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public com.mercurio.lms.sim.model.PedidoCompra getPedidoCompra() {
        return this.pedidoCompra;
    }

    public void setPedidoCompra(
            com.mercurio.lms.sim.model.PedidoCompra pedidoCompra) {
        this.pedidoCompra = pedidoCompra;
    }

    public OcorrenciaEntrega getOcorrenciaEntrega() {
        return ocorrenciaEntrega;
    }

    public void setOcorrenciaEntrega(OcorrenciaEntrega ocorrenciaEntrega) {
        this.ocorrenciaEntrega = ocorrenciaEntrega;
    }

    public OcorrenciaPendencia getOcorrenciaPendencia() {
        return ocorrenciaPendencia;
    }

    public void setOcorrenciaPendencia(OcorrenciaPendencia ocorrenciaPendencia) {
        this.ocorrenciaPendencia = ocorrenciaPendencia;
    }

    public String toString() {
        return new ToStringBuilder(this).append("idEventoDocumentoServico",
                getIdEventoDocumentoServico()).toString();
    }

    public String toString(ToStringStyle style) {
        return new ToStringBuilder(this, style)
                .append("ID_EVENTO_DOCUMENTO_SERVICO", idEventoDocumentoServico)
                .append("ID_FILIAL", filial != null ? filial.getIdFilial() : null)
                .append("ID_DOCTO_SERVICO", doctoServico != null ? doctoServico.getIdDoctoServico() : null)
                .append("ID_EVENTO", evento != null ? evento.getIdEvento() : null)
                .append("DH_EVENTO", dhEvento)
                .append("DH_INCLUSAO", dhInclusao)
                .append("ID_USUARIO", usuario != null ? usuario.getIdUsuario() : null)
                .append("BL_EVENTO_CANCELADO", blEventoCancelado)
                .append("NR_DOCUMENTO", nrDocumento)
                .append("OB_COMPLEMENTO", obComplemento)
                .append("DH_GERACAO_PARCEIRO", dhGeracaoParceiro)
                .append("DH_COMUNICACAO", dhComunicacao)
                .append("ID_PEDIDO_COMPRA", pedidoCompra != null ? pedidoCompra.getIdPedidoCompra() : null)
                .append("TP_DOCUMENTO", tpDocumento != null ? tpDocumento.getValue() : null)
                .append("ID_OCORRENCIA_PENDENCIA", ocorrenciaPendencia != null ? ocorrenciaPendencia.getIdOcorrenciaPendencia() : null)
                .append("ID_OCORRENCIA_ENTREGA", ocorrenciaEntrega != null ? ocorrenciaEntrega.getIdOcorrenciaEntrega() : null)
                .toString();
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if (!(other instanceof EventoDocumentoServico))
            return false;
        EventoDocumentoServico castOther = (EventoDocumentoServico) other;
        return new EqualsBuilder().append(this.getIdEventoDocumentoServico(),
                castOther.getIdEventoDocumentoServico()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getIdEventoDocumentoServico())
                .toHashCode();
    }

}
