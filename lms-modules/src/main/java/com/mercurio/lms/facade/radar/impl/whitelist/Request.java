package com.mercurio.lms.facade.radar.impl.whitelist;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tracking.util.TrackingContantsUtil;

public class Request {

    private String token;
    private String plataforma;
    private String versao;
    private String ddd;
    private String numero;
    private String arn;

    private Request() {
    }

    public static Request from(TypedFlatMap args) {
        Request request = new Request();
        request.token = args.getString(TrackingContantsUtil.WHITE_LIST_PRM_TOKEN);
        request.plataforma = args.getString(TrackingContantsUtil.WHITE_LIST_PRM_PLATAFORMA);
        request.ddd = args.getString(TrackingContantsUtil.WHITE_LIST_PRM_DDD);
        request.numero = args.getString(TrackingContantsUtil.WHITE_LIST_PRM_TELEFONE);
        request.versao = args.getString(TrackingContantsUtil.WHITE_LIST_PRM_VERSAO);
        request.arn = args.getString("dsArn");
        return request;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }
}
