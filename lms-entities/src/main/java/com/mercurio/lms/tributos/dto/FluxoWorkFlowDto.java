package com.mercurio.lms.tributos.dto;

public class FluxoWorkFlowDto {

    private Long idAcao;
    private String usuario_nrMatricula;
    private String aprovador;
    private String dhLiberacao;
    private String dhAcao;
    private String tpSituacaoAcao;
    private String obAcao;
    private Long idPendencia;

    public FluxoWorkFlowDto() {
    }

    public FluxoWorkFlowDto(Long idAcao, String usuario_nrMatricula, String aprovador, String dhLiberacao, String dhAcao, String tpSituacaoAcao, String obAcao, Long idPendencia) {
        this.idAcao = idAcao;
        this.usuario_nrMatricula = usuario_nrMatricula;
        this.aprovador = aprovador;
        this.dhLiberacao = dhLiberacao;
        this.dhAcao = dhAcao;
        this.tpSituacaoAcao = tpSituacaoAcao;
        this.obAcao = obAcao;
        this.idPendencia = idPendencia;
    }

    public Long getIdAcao() {
        return idAcao;
    }

    public void setIdAcao(Long idAcao) {
        this.idAcao = idAcao;
    }

    public String getUsuario_nrMatricula() {
        return usuario_nrMatricula;
    }

    public void setUsuario_nrMatricula(String usuario_nrMatricula) {
        this.usuario_nrMatricula = usuario_nrMatricula;
    }

    public String getAprovador() {
        return aprovador;
    }

    public void setAprovador(String aprovador) {
        this.aprovador = aprovador;
    }

    public String getDhLiberacao() {
        return dhLiberacao;
    }

    public void setDhLiberacao(String dhLiberacao) {
        this.dhLiberacao = dhLiberacao;
    }

    public String getDhAcao() {
        return dhAcao;
    }

    public void setDhAcao(String dhAcao) {
        this.dhAcao = dhAcao;
    }

    public String getTpSituacaoAcao() {
        return tpSituacaoAcao;
    }

    public void setTpSituacaoAcao(String tpSituacaoAcao) {
        this.tpSituacaoAcao = tpSituacaoAcao;
    }

    public String getObAcao() {
        return obAcao;
    }

    public void setObAcao(String obAcao) {
        this.obAcao = obAcao;
    }

    public Long getIdPendencia() {
        return idPendencia;
    }

    public void setIdPendencia(Long idPendencia) {
        this.idPendencia = idPendencia;
    }

}
