package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoTributacaoIE implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoTributacaoIE; 
    
    private DomainValue tpSituacaoTributaria;
    
    private Boolean blIsencaoExportacoes;
    
    private Boolean blAceitaSubstituicao;
    
    private Boolean blIncentivada;    

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    private TipoTributacaoIcms tipoTributacaoIcms;
    
    private InscricaoEstadual inscricaoEstadual;

	public Boolean getBlAceitaSubstituicao() {
		return blAceitaSubstituicao;
	}

	public void setBlAceitaSubstituicao(Boolean blAceitaSubstituicao) {
		this.blAceitaSubstituicao = blAceitaSubstituicao;
	}

	public Boolean getBlIncentivada() {
		return blIncentivada;
	}

	public void setBlIncentivada(Boolean blIncentivada) {
		this.blIncentivada = blIncentivada;
	}

	public Boolean getBlIsencaoExportacoes() {
		return blIsencaoExportacoes;
	}

	public void setBlIsencaoExportacoes(Boolean blIsencaoExportacoes) {
		this.blIsencaoExportacoes = blIsencaoExportacoes;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public Long getIdTipoTributacaoIE() {
		return idTipoTributacaoIE;
	}

	public void setIdTipoTributacaoIE(Long idTipoTributacaoIE) {
		this.idTipoTributacaoIE = idTipoTributacaoIE;
	}

	public InscricaoEstadual getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(InscricaoEstadual inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public TipoTributacaoIcms getTipoTributacaoIcms() {
		return tipoTributacaoIcms;
	}

	public void setTipoTributacaoIcms(TipoTributacaoIcms tipoTributacaoIcms) {
		this.tipoTributacaoIcms = tipoTributacaoIcms;
	}

	public DomainValue getTpSituacaoTributaria() {
		return tpSituacaoTributaria;
	}

	public void setTpSituacaoTributaria(DomainValue tpSituacaoTributaria) {
		this.tpSituacaoTributaria = tpSituacaoTributaria;
	}
}
