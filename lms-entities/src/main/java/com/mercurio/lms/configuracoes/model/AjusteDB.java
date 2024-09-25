package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "AJUSTE_DB")
public class AjusteDB implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idAjusteDB;
    private UsuarioLMS usuarioCriador;
    private String nmAjuste;
    private String dsConteudo;
    private Boolean blExecutaTrigger;
    private Boolean blAtivo;
    private Date dhCriacao;
    private List<AjusteDBBind> binds;

    @Id
    @SequenceGenerator(name = "AJUSTE_DB_SQ", sequenceName = "AJUSTE_DB_SQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AJUSTE_DB_SQ")
    @Column(name = "ID_AJUSTE_DB", nullable = false)
    public Long getIdAjusteDB() {
        return idAjusteDB;
    }

    public void setIdAjusteDB(Long idAjusteDB) {
        this.idAjusteDB = idAjusteDB;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO_CRIADOR", nullable = false)
    public UsuarioLMS getUsuarioCriador() {
        return usuarioCriador;
    }

    public void setUsuarioCriador(UsuarioLMS usuarioCriador) {
        this.usuarioCriador = usuarioCriador;
    }

    @Column(name = "NM_AJUSTE", length = 200, nullable = false)
    public String getNmAjuste() {
        return nmAjuste;
    }

    public void setNmAjuste(String nmAjuste) {
        this.nmAjuste = nmAjuste;
    }

    @Column(name = "DS_CONTEUDO", length = 4000, nullable = false)
    public String getDsConteudo() {
        return dsConteudo;
    }

    public void setDsConteudo(String dsConteudo) {
        this.dsConteudo = dsConteudo;
    }

    @Column(name = "BL_EXECUTA_TRIGGER", length = 1, nullable = false)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
    public Boolean getBlExecutaTrigger() {
        return blExecutaTrigger;
    }

    public void setBlExecutaTrigger(Boolean blExecutaTrigger) {
        this.blExecutaTrigger = blExecutaTrigger;
    }

    @Column(name = "BL_ATIVO", length = 1, nullable = false)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
    public Boolean getBlAtivo() {
        return blAtivo;
    }

    public void setBlAtivo(Boolean blAtivo) {
        this.blAtivo = blAtivo;
    }

    @Column(name = "DH_CRIACAO", nullable = false)
    public Date getDhCriacao() {
        return dhCriacao;
    }

    public void setDhCriacao(Date dhCriacao) {
        this.dhCriacao = dhCriacao;
    }

    @Column(name = "DH_CRIACAO_TZR")
    public String getDhCriacaoTzr() {
        return "America/Sao_Paulo";
    }

    public void setDhCriacaoTzr(String s) {}

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AJUSTE_DB")
    public List<AjusteDBBind> getBinds() {
        return binds;
    }

    public void setBinds(List<AjusteDBBind> binds) {
        this.binds = binds;
    }

}
