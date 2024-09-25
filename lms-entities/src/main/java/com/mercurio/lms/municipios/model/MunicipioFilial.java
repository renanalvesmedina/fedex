package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class MunicipioFilial implements Serializable, Vigencia {
	
	private static final long serialVersionUID = 1L;
	public final static String FIND_FILIAL_BY_MUNICIPIO_NR_CEP = "com.mercurio.lms.municipios.model.MunicipioFilial.findFilialByIdMunicipioNrCep";
	public final static String FIND_FILIAL_DADOS_BY_MUNICIPIO_NR_CEP = "com.mercurio.lms.municipios.model.MunicipioFilial.findFilialDadosByIdMunicipioNrCep";
	
    /** identifier field */
    private Long idMunicipioFilial;

    /** persistent field */
    private Integer nrDistanciaAsfalto;

    /** persistent field */
    private Integer nrDistanciaChao;

    /** persistent field */
    private Boolean blRecebeColetaEventual;

    /** persistent field */
    private Boolean blPadraoMcd;
    
    /** persistent field */
    private Boolean blDificuldadeEntrega;

    /** persistent field */
    private Boolean blRestricaoAtendimento;

    /** persistent field */
    private Boolean blRestricaoTransporte;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private Integer nrGrauDificuldade;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List mcdMunicipioFiliaisByIdMunicipioFilialOrigem;

    /** persistent field */
    private List mcdMunicipioFiliaisByIdMunicipioFilialDestino;

    /** persistent field */
    private List historicoTrocaFiliaisByIdMunicipioFilial;

    /** persistent field */
    private List historicoTrocaFiliaisByIdMunicipioFilialTroca;

    /** persistent field */
    private List municipioFilialIntervCeps;

    /** persistent field */
    private List postoPassagemMunicipios;

    /** persistent field */
    private List municipioFilialCliOrigems;

    /** persistent field */
    private List municipioFilialUfOrigems;

    /** persistent field */
    private List municipioFilialSegmentos;

    /** persistent field */
    private List operacaoServicoLocalizas;

    /** persistent field */
    private List municipioFilialFilOrigems;

    /** persistent field */
    private String nmMunicipioAlternativo;

    public Long getIdMunicipioFilial() {
        return this.idMunicipioFilial;
    }

    public void setIdMunicipioFilial(Long idMunicipioFilial) {
        this.idMunicipioFilial = idMunicipioFilial;
    }

    public Integer getNrDistanciaAsfalto() {
        return this.nrDistanciaAsfalto;
    }

    public void setNrDistanciaAsfalto(Integer nrDistanciaAsfalto) {
        this.nrDistanciaAsfalto = nrDistanciaAsfalto;
    }

    public Integer getNrDistanciaChao() {
        return this.nrDistanciaChao;
    }

    public void setNrDistanciaChao(Integer nrDistanciaChao) {
        this.nrDistanciaChao = nrDistanciaChao;
    }

    public Boolean getBlRecebeColetaEventual() {
        return this.blRecebeColetaEventual;
    }

    public void setBlRecebeColetaEventual(Boolean blRecebeColetaEventual) {
        this.blRecebeColetaEventual = blRecebeColetaEventual;
    }

    public Boolean getBlPadraoMcd() {
        return this.blPadraoMcd;
    }

    public void setBlPadraoMcd(Boolean blPadraoMcd) {
        this.blPadraoMcd = blPadraoMcd;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public Integer getNrGrauDificuldade() {
        return this.nrGrauDificuldade;
    }

    public void setNrGrauDificuldade(Integer nrGrauDificuldade) {
        this.nrGrauDificuldade = nrGrauDificuldade;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.McdMunicipioFilial.class)     
    public List getMcdMunicipioFiliaisByIdMunicipioFilialOrigem() {
        return this.mcdMunicipioFiliaisByIdMunicipioFilialOrigem;
    }

	public void setMcdMunicipioFiliaisByIdMunicipioFilialOrigem(
			List mcdMunicipioFiliaisByIdMunicipioFilialOrigem) {
        this.mcdMunicipioFiliaisByIdMunicipioFilialOrigem = mcdMunicipioFiliaisByIdMunicipioFilialOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.McdMunicipioFilial.class)     
    public List getMcdMunicipioFiliaisByIdMunicipioFilialDestino() {
        return this.mcdMunicipioFiliaisByIdMunicipioFilialDestino;
    }

	public void setMcdMunicipioFiliaisByIdMunicipioFilialDestino(
			List mcdMunicipioFiliaisByIdMunicipioFilialDestino) {
        this.mcdMunicipioFiliaisByIdMunicipioFilialDestino = mcdMunicipioFiliaisByIdMunicipioFilialDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.HistoricoTrocaFilial.class)     
    public List getHistoricoTrocaFiliaisByIdMunicipioFilial() {
        return this.historicoTrocaFiliaisByIdMunicipioFilial;
    }

	public void setHistoricoTrocaFiliaisByIdMunicipioFilial(
			List historicoTrocaFiliaisByIdMunicipioFilial) {
        this.historicoTrocaFiliaisByIdMunicipioFilial = historicoTrocaFiliaisByIdMunicipioFilial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.HistoricoTrocaFilial.class)     
    public List getHistoricoTrocaFiliaisByIdMunicipioFilialTroca() {
        return this.historicoTrocaFiliaisByIdMunicipioFilialTroca;
    }

	public void setHistoricoTrocaFiliaisByIdMunicipioFilialTroca(
			List historicoTrocaFiliaisByIdMunicipioFilialTroca) {
        this.historicoTrocaFiliaisByIdMunicipioFilialTroca = historicoTrocaFiliaisByIdMunicipioFilialTroca;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MunicipioFilialIntervCep.class)     
    public List getMunicipioFilialIntervCeps() {
        return this.municipioFilialIntervCeps;
    }

    public void setMunicipioFilialIntervCeps(List municipioFilialIntervCeps) {
        this.municipioFilialIntervCeps = municipioFilialIntervCeps;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PostoPassagemMunicipio.class)     
    public List getPostoPassagemMunicipios() {
        return this.postoPassagemMunicipios;
    }

    public void setPostoPassagemMunicipios(List postoPassagemMunicipios) {
        this.postoPassagemMunicipios = postoPassagemMunicipios;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MunicipioFilialCliOrigem.class)     
    public List getMunicipioFilialCliOrigems() {
        return this.municipioFilialCliOrigems;
    }

    public void setMunicipioFilialCliOrigems(List municipioFilialCliOrigems) {
        this.municipioFilialCliOrigems = municipioFilialCliOrigems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MunicipioFilialUFOrigem.class)     
    public List getMunicipioFilialUfOrigems() {
        return this.municipioFilialUfOrigems;
    }

    public void setMunicipioFilialUfOrigems(List municipioFilialUfOrigems) {
        this.municipioFilialUfOrigems = municipioFilialUfOrigems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MunicipioFilialSegmento.class)     
    public List getMunicipioFilialSegmentos() {
        return this.municipioFilialSegmentos;
    }

    public void setMunicipioFilialSegmentos(List municipioFilialSegmentos) {
        this.municipioFilialSegmentos = municipioFilialSegmentos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.OperacaoServicoLocaliza.class)     
    public List getOperacaoServicoLocalizas() {
        return this.operacaoServicoLocalizas;
    }

    public void setOperacaoServicoLocalizas(List operacaoServicoLocalizas) {
        this.operacaoServicoLocalizas = operacaoServicoLocalizas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MunicipioFilialFilOrigem.class)     
    public List getMunicipioFilialFilOrigems() {
        return this.municipioFilialFilOrigems;
    }

    public void setMunicipioFilialFilOrigems(List municipioFilialFilOrigems) {
        this.municipioFilialFilOrigems = municipioFilialFilOrigems;
    }
    
	public String getNmMunicipioAlternativo() {
		return nmMunicipioAlternativo;
	}

	public void setNmMunicipioAlternativo(String nmMunicipioAlternativo) {
		this.nmMunicipioAlternativo = nmMunicipioAlternativo;
	}

	public Boolean getBlRestricaoAtendimento() {
		return blRestricaoAtendimento;
	}

	public void setBlRestricaoAtendimento(Boolean blRestricaoAtendimento) {
		this.blRestricaoAtendimento = blRestricaoAtendimento;
	}

	public Boolean getBlDificuldadeEntrega() {
		return blDificuldadeEntrega;
	}

	public void setBlDificuldadeEntrega(Boolean blDificuldadeEntrega) {
		this.blDificuldadeEntrega = blDificuldadeEntrega;
	}

	public Boolean getBlRestricaoTransporte() {
		return blRestricaoTransporte;
	}

	public void setBlRestricaoTransporte(Boolean blRestricaoTransporte) {
		this.blRestricaoTransporte = blRestricaoTransporte;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idMunicipioFilial",
				getIdMunicipioFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MunicipioFilial))
			return false;
        MunicipioFilial castOther = (MunicipioFilial) other;
		return new EqualsBuilder().append(this.getIdMunicipioFilial(),
				castOther.getIdMunicipioFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipioFilial())
            .toHashCode();
    }
}
