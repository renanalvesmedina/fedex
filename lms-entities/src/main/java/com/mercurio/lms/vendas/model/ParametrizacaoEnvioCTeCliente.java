package com.mercurio.lms.vendas.model;

import com.mercurio.adsm.framework.model.DomainValue;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PARAM_ENVIO_CTE_CLIENTE")
@SequenceGenerator(name = "PARAM_ENVIO_CTE_CLIENTE_SQ", sequenceName = "PARAM_ENVIO_CTE_CLIENTE_SQ", allocationSize = 1)
public class ParametrizacaoEnvioCTeCliente  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARAM_ENVIO_CTE_CLIENTE_SQ")
    @Column(name = "ID_PARAM_ENVIO_CTE_CLIENTE", nullable = false)
    private Long idParametrizacaoEnvioCTeCliente;

    @Column(name = "DS_DIRETORIO_ARMAZENAGEM")
    private String dsDiretorioArmazenagem;

    @Column(name = "TP_PESQUISA", nullable = false)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType",
        parameters = { @Parameter(name = "domainName", value = "DM_TIPO_PESQUISA_CTE") })
    private DomainValue tpPesquisa;

    @Columns(columns = { @Column(name = "DH_INCLUSAO"),
    @Column(name = "DH_INCLUSAO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    private DateTime dhInclusao;

    @Column(name = "NR_IDENTIFICACAO", nullable = false)
    private String nrIdentificacao;

    @Column(name = "BL_ATIVO", length = 1, nullable = false)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
    private Boolean blAtivo;

    public ParametrizacaoEnvioCTeCliente() {
    }


    public Long getIdParametrizacaoEnvioCTeCliente() {
        return idParametrizacaoEnvioCTeCliente;
    }

    public void setIdParametrizacaoEnvioCTeCliente(Long idParametrizacaoEnvioCTeCliente) {
        this.idParametrizacaoEnvioCTeCliente = idParametrizacaoEnvioCTeCliente;
    }

    public String getDsDiretorioArmazenagem() {
        return dsDiretorioArmazenagem;
    }

    public void setDsDiretorioArmazenagem(String dsDiretorioArmazenagem) {
        this.dsDiretorioArmazenagem = dsDiretorioArmazenagem;
    }

    public DomainValue getTpPesquisa() {
        return tpPesquisa;
    }

    public void setTpPesquisa(DomainValue tpPesquisa) {
        this.tpPesquisa = tpPesquisa;
    }

    public DateTime getDhInclusao() {
        return dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    public String getNrIdentificacao() {
        return nrIdentificacao;
    }

    public void setNrIdentificacao(String nrIdentificacao) {
        this.nrIdentificacao = nrIdentificacao;
    }

    public Boolean getBlAtivo() {
        return blAtivo;
    }

    public void setBlAtivo(Boolean blAtivo) {
        this.blAtivo = blAtivo;
    }
}
