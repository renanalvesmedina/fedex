package com.mercurio.lms.expedicao.model;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioAdsm;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "PROCESSAMENTO_EDI")
@SequenceGenerator(name = "PROCESSAMENTO_EDI_SEQ", sequenceName = "PROCESSAMENTO_EDI_SQ", allocationSize=1)
public class ProcessamentoEdi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSAMENTO_EDI_SEQ")
    @Column(name = "ID_PROCESSAMENTO_EDI", nullable = false)
    private Long idProcessamentoEdi;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "ID_FILIAL", nullable = false)
    private Filial filial;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "ID_CLIENTE_PROCESSAMENTO", nullable = false)
    private Cliente clienteProcessamento;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private UsuarioAdsm usuario;

    @Columns(columns = { @Column(name = "DH_PROCESSAMENTO"), @Column(name = "DH_PROCESSAMENTO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    private DateTime dhProcessamento;

    @Column(name = "QT_NOTAS_PROCESSADAS", nullable = false, columnDefinition = "number default 0")
    private Long qtNotasProcessadas;

    @Column(name = "QT_TOTAL_NOTAS", nullable = false)
    private Long qtTotalNotas;

    @OneToMany(mappedBy = "processamentoEdi")
    @JoinColumn(name = "ID_PROCESSAMENTO_EDI")
    private List<ProcessamentoNotaEdi> listProcessamentoNotaEdi;

    @Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
    @Column(name = "BL_VISIVEL")
    private Boolean blVisivel;

    @Column(name = "TP_STATUS", length = 2)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType",
            parameters = { @Parameter(name = "domainName", value = "DM_STATUS") })
    private DomainValue tpStatus;

    public ProcessamentoEdi() {
    }

    public Long getIdProcessamentoEdi() {
        return idProcessamentoEdi;
    }

    public void setIdProcessamentoEdi(Long idProcessamentoEdi) {
        this.idProcessamentoEdi = idProcessamentoEdi;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Cliente getClienteProcessamento() {
        return clienteProcessamento;
    }

    public void setClienteProcessamento(Cliente clienteProcessamento) {
        this.clienteProcessamento = clienteProcessamento;
    }

    public UsuarioAdsm getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioAdsm usuario) {
        this.usuario = usuario;
    }

    public DateTime getDhProcessamento() {
        return dhProcessamento;
    }

    public void setDhProcessamento(DateTime dhProcessamento) {
        this.dhProcessamento = dhProcessamento;
    }

    public Long getQtNotasProcessadas() {
        return qtNotasProcessadas;
    }

    public void setQtNotasProcessadas(Long qtNotasProcessadas) {
        this.qtNotasProcessadas = qtNotasProcessadas;
    }

    public Long getQtTotalNotas() {
        return qtTotalNotas;
    }

    public void setQtTotalNotas(Long qtTotalNotas) {
        this.qtTotalNotas = qtTotalNotas;
    }

    public List<ProcessamentoNotaEdi> getListProcessamentoNotaEdi() {
        return listProcessamentoNotaEdi;
    }

    public void setListProcessamentoNotaEdi(List<ProcessamentoNotaEdi> listProcessamentoNotaEdi) {
        this.listProcessamentoNotaEdi = listProcessamentoNotaEdi;
    }

    public Boolean getBlVisivel() {
        return blVisivel;
    }

    public void setBlVisivel(Boolean blVisivel) {
        this.blVisivel = blVisivel;
    }

    public DomainValue getTpStatus() {
        return tpStatus;
    }

    public void setTpStatus(DomainValue tpStatus) {
        this.tpStatus = tpStatus;
    }

    public void addProcessamentoNotaEdi(final ProcessamentoNotaEdi processamentoNotaEdi) {

        if (processamentoNotaEdi == null) {
            throw new IllegalArgumentException("Processamento nota edi está nulo");
        }

        if (this.listProcessamentoNotaEdi == null) {
            this.listProcessamentoNotaEdi = new ArrayList<>();
        }

        processamentoNotaEdi.setProcessamentoEdi(this);
        List<ProcessamentoNotaEdi> existPne = this.listProcessamentoNotaEdi.stream().filter(pne -> pne.getNrNotaFiscal().equals(processamentoNotaEdi.getNrNotaFiscal())).collect(Collectors.toList());

        if (existPne.isEmpty()) {
            this.listProcessamentoNotaEdi.add(processamentoNotaEdi);
        }
    }
}
