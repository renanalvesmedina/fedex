package com.mercurio.lms.rest.portaria.chegadasaida.dto;

import com.mercurio.adsm.framework.util.TypedFlatMap;

import java.io.Serializable;

public class FrotaPlacaChegadaSaidaSuggestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String NR_IDENTIFICADOR = "nrIdentificador";
    public static final String NR_FROTA = "nrFrota";
    public static final String ID_CONTROLE = "idControle";
    public static final String TIPO_C = "tipo";
    public static final String TIPO_LABEL = "tipoLabel";
    public static final String SUB_TIPO = "subTipo";
    public static final String SUB_TIPO_LABEL = "subTipoLabel";
    public static final String ID_FILIAL = "idFilial";
    public static final String ID_CONTROLE_CARGA = "idControleCarga";
    public static final String DS_CONTROLE_CARGA = "dsControleCarga";
    public static final String CONTROLE_CARGA_LABEL = "controleCargaLabel";
    public static final String ID_MEIO_TRANSPORTE = "idMeioTransporte";
    public static final String ID_CONTROLE_TEMP = "idControleTemp";

    protected String nrIdentificador;
    protected String nrFrota;
    protected String idControle;
    protected String tipo;
    private String tipoLabel;
    protected String subTipo;
    private String subTipoLabel;
    protected Long idFilial;
    private Long idControleCarga;
    private String dsControleCarga;
    private String controleCargaLabel;
    private Long idMeioTransporte;

    public FrotaPlacaChegadaSaidaSuggestDTO() {
    }

    public FrotaPlacaChegadaSaidaSuggestDTO(TypedFlatMap parametros) {
        setNrIdentificador(parametros.getString(NR_IDENTIFICADOR));
        setNrFrota(parametros.getString(NR_FROTA));
        setIdControle(parametros.getString(ID_CONTROLE));
        setTipo(parametros.getString(TIPO_C));
        setTipoLabel(parametros.getString(TIPO_LABEL));
        setSubTipo(parametros.getString(SUB_TIPO));
        setSubTipoLabel(parametros.getString(SUB_TIPO_LABEL));
        setIdFilial(parametros.getLong(ID_FILIAL));
        setIdControleCarga(parametros.getLong(ID_CONTROLE_CARGA));
        setDsControleCarga((parametros.getString(DS_CONTROLE_CARGA) == null) ? " " : parametros.getString(DS_CONTROLE_CARGA));
        setControleCargaLabel(parametros.getString(CONTROLE_CARGA_LABEL));
        setIdMeioTransporte(parametros.getLong(ID_MEIO_TRANSPORTE));
    }

    public TypedFlatMap toMap() {
        TypedFlatMap parametros = new TypedFlatMap();
        parametros.put(NR_IDENTIFICADOR, getNrIdentificador());
        parametros.put(NR_FROTA, getNrFrota());
        parametros.put(ID_CONTROLE, getIdControle());
        parametros.put(ID_CONTROLE_TEMP, getIdControle());
        parametros.put(TIPO_C, getTipo());
        parametros.put(TIPO_LABEL, getTipoLabel());
        parametros.put(SUB_TIPO, getSubTipo());
        parametros.put(SUB_TIPO_LABEL, getSubTipoLabel());
        parametros.put(ID_FILIAL, getIdFilial());
        parametros.put(ID_CONTROLE_CARGA, getIdControleCarga());
        parametros.put(DS_CONTROLE_CARGA, getDsControleCarga());
        parametros.put(CONTROLE_CARGA_LABEL, getControleCargaLabel());
        parametros.put(ID_MEIO_TRANSPORTE, getIdMeioTransporte());
        return parametros;
    }

    public String getNrIdentificador() {
        return nrIdentificador;
    }

    public void setNrIdentificador(String nrIdentificador) {
        this.nrIdentificador = nrIdentificador;
    }

    public String getNrFrota() {
        return nrFrota;
    }

    public void setNrFrota(String nrFrota) {
        this.nrFrota = nrFrota;
    }

    public String getIdControle() {
        return idControle;
    }

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSubTipo() {
        return subTipo;
    }

    public void setSubTipo(String subTipo) {
        this.subTipo = subTipo;
    }

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }

    public Long getIdControleCarga() {
        return idControleCarga;
    }

    public void setIdControleCarga(Long idControleCarga) {
        this.idControleCarga = idControleCarga;
    }

    public Long getIdMeioTransporte() {
        return idMeioTransporte;
    }

    public void setIdMeioTransporte(Long idMeioTransporte) {
        this.idMeioTransporte = idMeioTransporte;
    }

    public String getTipoLabel() {
        return tipoLabel;
    }

    public void setTipoLabel(String tipoLabel) {
        this.tipoLabel = tipoLabel;
    }

    public String getSubTipoLabel() {
        return subTipoLabel;
    }

    public void setSubTipoLabel(String subTipoLabel) {
        this.subTipoLabel = subTipoLabel;
    }

    public String getControleCargaLabel() {
        return controleCargaLabel;
    }

    public void setControleCargaLabel(String controleCargaLabel) {
        this.controleCargaLabel = controleCargaLabel;
    }

    public String getDsControleCarga() {
        return dsControleCarga;
    }

    public void setDsControleCarga(String dsControleCarga) {
        this.dsControleCarga = dsControleCarga;
    }

}
