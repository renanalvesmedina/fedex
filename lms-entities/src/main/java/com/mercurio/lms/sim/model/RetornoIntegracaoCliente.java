package com.mercurio.lms.sim.model;
import com.mercurio.lms.vendas.model.Cliente;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="RETORNO_INTEGRACAO_CLIENTE")
@SequenceGenerator(name = "RETORNO_INTEGRACAO_CLIENTE_SQ", sequenceName = "RETORNO_INTEGRACAO_CLIENTE_SQ", allocationSize = 1)
public class RetornoIntegracaoCliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idRetornoIntegracaoCliente;
    private Long idDoctoServico;
    private Long idEventoDocumentoServico;
    private Cliente cliente;
    private String protocolo;
    private String codRetorno;
    private String dsRetorno;
    private String dcEnviado;
    private String tpIntegracao;
    private DateTime dhEnvio;
    private Long idFatura;

    public RetornoIntegracaoCliente
            (
                    Long idDoctoServico, Long idEventoDocumentoServico, Cliente cliente, String protocolo,
                    String codRetorno, String dsRetorno, String dcEnviado, String tpIntegracao, DateTime dhEnvio
            ){

        this.idDoctoServico = idDoctoServico;
        this.idEventoDocumentoServico = idEventoDocumentoServico;
        this.cliente = cliente;
        this.protocolo = protocolo;
        this.codRetorno = codRetorno;
        this.dsRetorno = dsRetorno;
        this.dcEnviado = dcEnviado;
        this.tpIntegracao = tpIntegracao;
        this.dhEnvio = dhEnvio;
    }

    public RetornoIntegracaoCliente(Long idDoctoServico, Long idEventoDocumentoServico, Cliente cliente, String protocolo,
                                    String codRetorno, String dsRetorno, String dcEnviado, String tpIntegracao, DateTime dhEnvio, Long idFatura) {
        this(idDoctoServico, idEventoDocumentoServico, cliente, protocolo, codRetorno, dsRetorno, dcEnviado, tpIntegracao, dhEnvio);
        this.idFatura = idFatura;
    }

    public RetornoIntegracaoCliente() {
        super();
        cliente = new Cliente();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RETORNO_INTEGRACAO_CLIENTE_SQ")
    @Column(name="ID_RETORNO_INTEGRACAO_CLIENTE", nullable=false)
    public Long getIdRetornoIntegracaoCliente() {
        return idRetornoIntegracaoCliente;
    }

    public void setIdRetornoIntegracaoCliente(Long idRetornoIntegracaoCliente) {
        this.idRetornoIntegracaoCliente = idRetornoIntegracaoCliente;
    }
    @Column(name = "ID_DOCTO_SERVICO")
    public Long getIdDoctoServico() {
        return idDoctoServico;
    }

    public void setIdDoctoServico(Long idDoctoServico) {
        this.idDoctoServico = idDoctoServico;
    }
    @Column(name = "ID_EVENTO_DOCUMENTO_SERVICO")
    public Long getIdEventoDocumentoServico() {
        return idEventoDocumentoServico;
    }

    public void setIdEventoDocumentoServico(Long idEventoDocumentoServico) {
        this.idEventoDocumentoServico = idEventoDocumentoServico;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE")
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Column(name = "PROTOCOLO")
    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }
    @Column(name = "COD_RETORNO")
    public String getCodRetorno() {
        return codRetorno;
    }

    public void setCodRetorno(String codRetorno) {
        this.codRetorno = codRetorno;
    }
    @Column(name = "DS_RETORNO")
    public String getDsRetorno() {
        return dsRetorno;
    }

    public void setDsRetorno(String dsRetorno) {
        this.dsRetorno = dsRetorno;
    }
    @Column(name = "DC_ENVIADO")
    @Type(type = "com.mercurio.adsm.core.model.hibernate.CLobUserType")
    public String getDcEnviado() {
        return dcEnviado;
    }

    public void setDcEnviado(String dcEnviado) {
        this.dcEnviado = dcEnviado;
    }
    @Column(name = "TP_INTEGRACAO", nullable = false)
    public String getTpIntegracao() {
        return tpIntegracao;
    }

    public void setTpIntegracao(String tpIntegracao) {
        this.tpIntegracao = tpIntegracao;
    }
    @Columns(columns = { @Column(name = "DH_ENVIO"),
            @Column(name = "DH_ENVIO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    public DateTime getDhEnvio() {
        return dhEnvio;
    }

    public void setDhEnvio(DateTime dhEnvio) {
        this.dhEnvio = dhEnvio;
    }

    @Column(name = "ID_FATURA")
    public Long getIdFatura() {
        return idFatura;
    }

    public void setIdFatura(Long idFatura) {
        this.idFatura = idFatura;
    }
}
