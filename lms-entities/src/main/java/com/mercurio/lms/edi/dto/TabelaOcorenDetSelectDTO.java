package com.mercurio.lms.edi.dto;

import java.io.Serializable;

public class TabelaOcorenDetSelectDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idEdiTabelaOcorenDet;

    private Long idEdiTabelaOcoren;

    private String cdOcorrencia;

    private String dsCodicoOcorrencia;

    private String cdOcorrenciaCliente;

    private String tpOcorrencia;

    private String descricaoDomino;

    private String dsOcorrencia;

    private String cdWsDde;

    private String cdWsExcecao;

    private String dsWsExcecao;

    public TabelaOcorenDetSelectDTO(Long idEdiTabelaOcorenDet, Long idEdiTabelaOcoren, String cdOcorrencia, String dsCodicoOcorrencia, String cdOcorrenciaCliente, String tpOcorrencia, String descricaoDomino, String dsOcorrencia, String cdWsDde, String cdWsExcecao, String dsWsExcecao) {
        this.idEdiTabelaOcorenDet = idEdiTabelaOcorenDet;
        this.idEdiTabelaOcoren = idEdiTabelaOcoren;
        this.cdOcorrencia = cdOcorrencia;
        this.dsCodicoOcorrencia = dsCodicoOcorrencia;
        this.cdOcorrenciaCliente = cdOcorrenciaCliente;
        this.tpOcorrencia = tpOcorrencia;
        this.descricaoDomino = descricaoDomino;
        this.dsOcorrencia = dsOcorrencia;
        this.cdWsDde = cdWsDde;
        this.cdWsExcecao = cdWsExcecao;
        this.dsWsExcecao = dsWsExcecao;
    }

    public Long getIdEdiTabelaOcorenDet() {
        return idEdiTabelaOcorenDet;
    }

    public void setIdEdiTabelaOcorenDet(Long idEdiTabelaOcorenDet) {
        this.idEdiTabelaOcorenDet = idEdiTabelaOcorenDet;
    }

    public Long getIdEdiTabelaOcoren() {
        return idEdiTabelaOcoren;
    }

    public void setIdEdiTabelaOcoren(Long idEdiTabelaOcoren) {
        this.idEdiTabelaOcoren = idEdiTabelaOcoren;
    }

    public String getCdOcorrencia() {
        return cdOcorrencia;
    }

    public void setCdOcorrencia(String cdOcorrencia) {
        this.cdOcorrencia = cdOcorrencia;
    }

    public String getDsCodicoOcorrencia() {
        return dsCodicoOcorrencia;
    }

    public void setDsCodicoOcorrencia(String dsCodicoOcorrencia) {
        this.dsCodicoOcorrencia = dsCodicoOcorrencia;
    }

    public String getCdOcorrenciaCliente() {
        return cdOcorrenciaCliente;
    }

    public void setCdOcorrenciaCliente(String cdOcorrenciaCliente) {
        this.cdOcorrenciaCliente = cdOcorrenciaCliente;
    }

    public String getTpOcorrencia() {
        return tpOcorrencia;
    }

    public void setTpOcorrencia(String tpOcorrencia) {
        this.tpOcorrencia = tpOcorrencia;
    }

    public String getDescricaoDomino() {
        return descricaoDomino;
    }

    public void setDescricaoDomino(String descricaoDomino) {
        this.descricaoDomino = descricaoDomino;
    }

    public String getDsOcorrencia() {
        return dsOcorrencia;
    }

    public void setDsOcorrencia(String dsOcorrencia) {
        this.dsOcorrencia = dsOcorrencia;
    }

    public String getCdWsDde() {
        return cdWsDde;
    }

    public void setCdWsDde(String cdWsDde) {
        this.cdWsDde = cdWsDde;
    }

    public String getCdWsExcecao() {
        return cdWsExcecao;
    }

    public void setCdWsExcecao(String cdWsExcecao) {
        this.cdWsExcecao = cdWsExcecao;
    }

    public String getDsWsExcecao() {
        return dsWsExcecao;
    }

    public void setDsWsExcecao(String dsWsExcecao) {
        this.dsWsExcecao = dsWsExcecao;
    }
}
