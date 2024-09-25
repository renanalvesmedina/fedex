package com.mercurio.lms.seguros.model;

import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "AVERB_DOCTO_SERVICO_MDFE")
@SequenceGenerator(name = "AVERB_DOCTO_SERVICO_MDFE_SQ", sequenceName = "AVERB_DOCTO_SERVICO_MDFE_SQ")
public class AverbacaoDoctoServicoMdfe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AVERB_DOCTO_SERVICO_MDFE_SQ")
    @Column(name = "ID_AVERB_DOCTO_SERVICO_MDFE", nullable = false)
    private Long idAverbacaoDoctoServicoMdfe;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MANIFESTO_ELETRONICO", nullable = false)
    private ManifestoEletronico manifestoEletronico;

    @Column(name = "TP_WEBSERVICE", length = 1)
    private String tpWebservice;

    @Column(name = "TP_DESTINO", length = 10)
    private String tpDestino;

    // MDF-e Autorizado
    @Column(name = "BL_AUTORIZADO", length = 1)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
    private Boolean blAutorizado;

    @Column(name = "DS_RETORNO_AUTORIZADO", length = 2000)
    private String dsRetornoAutorizado;

    @Column(name = "DC_RETORNO_AUTORIZADO")
    private String dcRetornoAutorizado;

    @Columns(columns = { @Column(name = "DH_ENVIO_AUTORIZADO"),
    @Column(name = "DH_ENVIO_AUTORIZADO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    private DateTime dhEnvioAutorizado;

    // MDF-e Cancelado
    @Column(name = "BL_CANCELADO", length = 1)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
    private Boolean blCancelado;

    @Column(name = "DS_RETORNO_CANCELADO", length = 2000)
    private String dsRetornoCancelado;

    @Column(name = "DC_RETORNO_CANCELADO")
    private String dcRetornoCancelado;

    @Columns(columns = { @Column(name = "DH_ENVIO_CANCELADO"),
    @Column(name = "DH_ENVIO_CANCELADO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    private DateTime dhEnvioCancelado;

    // MDF-e Encerrado
    @Column(name = "BL_ENCERRADO", length = 1)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
    private Boolean blEncerrado;

    @Column(name = "DS_RETORNO_ENCERRADO", length = 2000)
    private String dsRetornoEncerrado;

    @Column(name = "DC_RETORNO_ENCERRADO")
    private String dcRetornoEncerrado;

    @Columns(columns = { @Column(name = "DH_ENVIO_ENCERRADO"),
    @Column(name = "DH_ENVIO_ENCERRADO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    private DateTime dhEnvioEncerrado;
    
    @Columns(columns = { @Column(name = "DH_INCLUSAO"),
    @Column(name = "DH_INCLUSAO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    private DateTime dhInclusao;

    @Column(name = "NR_ENVIO", nullable = false)
    private Long nrEnvio;

    @Column(name = "NR_PROTOCOLO", length = 120)
    private String nrProtocolo;

    public Long getIdAverbacaoDoctoServicoMdfe() {
        return idAverbacaoDoctoServicoMdfe;
    }

    public ManifestoEletronico getAverbacaoDoctoServico() {
        return manifestoEletronico;
    }

    public void setAverbacaoDoctoServico(ManifestoEletronico manifestoEletronico) {
        this.manifestoEletronico = manifestoEletronico;
    }

    public void setIdAverbacaoDoctoServicoMdfe(Long idAverbacaoDoctoServico) {
            this.idAverbacaoDoctoServicoMdfe = idAverbacaoDoctoServico;
    }

    public String getDsRetornoAutorizado() {
        return dsRetornoAutorizado;
    }

    public void setDsRetornoAutorizado(String dsRetornoAutorizado) {
        this.dsRetornoAutorizado = dsRetornoAutorizado;
    }

    public String getDcRetornoAutorizado() {
        return dcRetornoAutorizado;
    }

    public void setDcRetornoAutorizado(String dcRetornoAutorizado) {
        this.dcRetornoAutorizado = dcRetornoAutorizado;
    }

    public DateTime getDhEnvioAutorizado() {
        return dhEnvioAutorizado;
    }

    public void setDhEnvioAutorizado(DateTime dhEnvioAutorizado) {
        this.dhEnvioAutorizado = dhEnvioAutorizado;
    }

    public Boolean getBlCancelado() {
        return blCancelado;
    }

    public void setBlCancelado(Boolean blCancelado) {
        this.blCancelado = blCancelado;
    }

    public String getDsRetornoCancelado() {
        return dsRetornoCancelado;
    }

    public void setDsRetornoCancelado(String dsRetornoCancelado) {
        this.dsRetornoCancelado = dsRetornoCancelado;
    }

    public String getDcRetornoCancelado() {
        return dcRetornoCancelado;
    }

    public void setDcRetornoCancelado(String dcRetornoCancelado) {
        this.dcRetornoCancelado = dcRetornoCancelado;
    }

    public DateTime getDhEnvioCancelado() {
        return dhEnvioCancelado;
    }

    public void setDhEnvioCancelado(DateTime dhEnvioCancelado) {
        this.dhEnvioCancelado = dhEnvioCancelado;
    }

    public Boolean getBlEncerrado() {
        return blEncerrado;
    }

    public void setBlEncerrado(Boolean blEncerrado) {
        this.blEncerrado = blEncerrado;
    }

    public String getDsRetornoEncerrado() {
        return dsRetornoEncerrado;
    }

    public void setDsRetornoEncerrado(String dsRetornoEncerrado) {
        this.dsRetornoEncerrado = dsRetornoEncerrado;
    }

    public String getDcRetornoEncerrado() {
        return dcRetornoEncerrado;
    }

    public void setDcRetornoEncerrado(String dcRetornoEncerrado) {
        this.dcRetornoEncerrado = dcRetornoEncerrado;
    }

    public DateTime getDhEnvioEncerrado() {
        return dhEnvioEncerrado;
    }

    public void setDhEnvioEncerrado(DateTime dhEnvioEncerrado) {
        this.dhEnvioEncerrado = dhEnvioEncerrado;
    }

    public ManifestoEletronico getManifestoEletronico() {
        return manifestoEletronico;
    }

    public void setManifestoEletronico(ManifestoEletronico manifestoEletronico) {
        this.manifestoEletronico = manifestoEletronico;
    }

    public String getTpWebservice() {
        return tpWebservice;
    }

    public void setTpWebservice(String tpWebservice) {
        this.tpWebservice = tpWebservice;
    }

    public String getTpDestino() {
        return tpDestino;
    }

    public void setTpDestino(String tpDestino) {
        this.tpDestino = tpDestino;
    }

    public Boolean getBlAutorizado() {
        return blAutorizado;
    }

    public void setBlAutorizado(Boolean blAutorizado) {
        this.blAutorizado = blAutorizado;
    }

    public Long getNrEnvio() {
        return nrEnvio;
    }

    public void setNrEnvio(Long nrEnvio) {
            this.nrEnvio = nrEnvio;
    }

    public String getNrProtocolo() {
        return nrProtocolo;
    }

    public void setNrProtocolo(String nrProtocolo) {
        this.nrProtocolo = nrProtocolo;
    }

    public DateTime getDhInclusao() {
        return dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }
    
    public String toString() {
        return new ToStringBuilder(this).append("id",
                        getIdAverbacaoDoctoServicoMdfe()).toString();
    }

    public boolean equals(Object other) {
            if ((this == other))
                    return true;
            if (!(other instanceof AverbacaoDoctoServicoMdfe))
                    return false;
            AverbacaoDoctoServicoMdfe castOther = (AverbacaoDoctoServicoMdfe) other;
            return new EqualsBuilder().append(this.getIdAverbacaoDoctoServicoMdfe(),
                            castOther.getIdAverbacaoDoctoServicoMdfe()).isEquals();
    }

    public int hashCode() {
            return new HashCodeBuilder().append(getIdAverbacaoDoctoServicoMdfe())
                            .toHashCode();
    }

}
