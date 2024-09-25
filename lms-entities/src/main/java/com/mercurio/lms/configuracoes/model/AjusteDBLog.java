package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "AJUSTE_DB_LOG")
public class AjusteDBLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idAjusteDBLog;
    private AjusteDB ajusteDB;
    private UsuarioLMS usuarioExecutor;
    private String dsBind;
    private DateTime dhExecucao;

    @Id
    @SequenceGenerator(name = "AJUSTE_DB_LOG_SQ", sequenceName = "AJUSTE_DB_LOG_SQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AJUSTE_DB_LOG_SQ")
    @Column(name = "ID_AJUSTE_DB_LOG", nullable = false)
    public Long getIdAjusteDBLog() {
        return idAjusteDBLog;
    }

    public void setIdAjusteDBLog(Long idAjusteDBLog) {
        this.idAjusteDBLog = idAjusteDBLog;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "ID_AJUSTE_DB", nullable = false)
    public AjusteDB getAjusteDB() {
        return ajusteDB;
    }

    public void setAjusteDB(AjusteDB ajusteDB) {
        this.ajusteDB = ajusteDB;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO_EXECUTOR", nullable = false)
    public UsuarioLMS getUsuarioExecutor() {
        return usuarioExecutor;
    }

    public void setUsuarioExecutor(UsuarioLMS usuarioExecutor) {
        this.usuarioExecutor = usuarioExecutor;
    }

    @Column(name = "DS_BIND", length = 4000, nullable = false)
    public String getDsBind() {
        return dsBind;
    }

    public void setDsBind(String dsBind) {
        this.dsBind = dsBind;
    }

    @Columns(columns = {
            @Column(name = "DH_EXECUCAO", nullable = false),
            @Column(name = "DH_EXECUCAO_TZR", nullable = false) })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    public DateTime getDhExecucao() {
        return dhExecucao;
    }

    public void setDhExecucao(DateTime dhExecucao) {
        this.dhExecucao = dhExecucao;
    }

}
