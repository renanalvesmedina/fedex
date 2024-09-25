package com.mercurio.lms.rest.sgr.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;

/**
 * FIXME corrigir número do JIRA
 * 
 * LMS-???? (Tela para simulação do Plano de Gerenciamento de Risco) - DTO para
 * identificação de {@link ControleCarga} e geração do respectivo
 * {@link PlanoGerenciamentoRiscoDTO} para a tela
 * "Simular Plano de Gerenciamento de Risco".
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class SimularPlanoGerenciamentoRiscoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String sgFilial;
	private Long nrControleCarga;
	private DateTime dhGeracao;
	private String tpControleCarga;
	private String dsRota;
	private String tpRota;
	private String meioTransporteNrFrota;
	private String meioTransporteNrIdentificador;
	private String semiReboqueNrFrota;
	private String semiReboqueNrIdentificador;

	private PlanoGerenciamentoRiscoDTO plano;

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public Long getNrControleCarga() {
		return nrControleCarga;
	}

	public void setNrControleCarga(Long nrControleCarga) {
		this.nrControleCarga = nrControleCarga;
	}

	public DateTime getDhGeracao() {
		return dhGeracao;
	}

	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public String getTpControleCarga() {
		return tpControleCarga;
	}

	public void setTpControleCarga(String tpControleCarga) {
		this.tpControleCarga = tpControleCarga;
	}

	public String getDsRota() {
		return dsRota;
	}

	public void setDsRota(String dsRota) {
		this.dsRota = dsRota;
	}

	public String getTpRota() {
		return tpRota;
	}

	public void setTpRota(String tpRota) {
		this.tpRota = tpRota;
	}

	public String getMeioTransporteNrFrota() {
		return meioTransporteNrFrota;
	}

	public void setMeioTransporteNrFrota(String meioTransporteNrFrota) {
		this.meioTransporteNrFrota = meioTransporteNrFrota;
	}

	public String getMeioTransporteNrIdentificador() {
		return meioTransporteNrIdentificador;
	}

	public void setMeioTransporteNrIdentificador(String meioTransporteNrIdentificador) {
		this.meioTransporteNrIdentificador = meioTransporteNrIdentificador;
	}

	public String getSemiReboqueNrFrota() {
		return semiReboqueNrFrota;
	}

	public void setSemiReboqueNrFrota(String semiReboqueNrFrota) {
		this.semiReboqueNrFrota = semiReboqueNrFrota;
	}

	public String getSemiReboqueNrIdentificador() {
		return semiReboqueNrIdentificador;
	}

	public void setSemiReboqueNrIdentificador(String semiReboqueNrIdentificador) {
		this.semiReboqueNrIdentificador = semiReboqueNrIdentificador;
	}

	public PlanoGerenciamentoRiscoDTO getPlano() {
		return plano;
	}

	public void setPlano(PlanoGerenciamentoRiscoDTO plano) {
		this.plano = plano;
	}

}
