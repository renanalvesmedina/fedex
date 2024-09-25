package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;

public class RecalculoComissaoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private ExecutivoTerritorioDTO executivoTerritorio;
	private UsuarioDTO usuario;

	private Long idComissaoGarantida; 
	private BigDecimal vlComissaoGarantida;
	private YearMonthDay dtInicio;
	private YearMonthDay dtFim;
	
	private YearMonthDay dtVigenciaEquipeVendasInicial;
	private YearMonthDay dtVigenciaEquipeVendasFinal;
	
	private Long idDiferencaComissao;
	private BigDecimal vlDiferencaComissao;
	private YearMonthDay dtCompetencia;
	private DomainValue tpTeto;
	private String dsObservacao;

	public ExecutivoTerritorioDTO getExecutivoTerritorio() {
		return executivoTerritorio;
	}

	public void setExecutivoTerritorio(ExecutivoTerritorioDTO executivoTerritorio) {
		this.executivoTerritorio = executivoTerritorio;
	}

	public BigDecimal getVlComissaoGarantida() {
		return vlComissaoGarantida;
	}

	public void setVlComissaoGarantida(BigDecimal vlComissaoGarantida) {
		this.vlComissaoGarantida = vlComissaoGarantida;
	}

	public Long getIdComissaoGarantida() {
		return idComissaoGarantida;
	}

	public void setIdComissaoGarantida(Long idComissaoGarantida) {
		this.idComissaoGarantida = idComissaoGarantida;
	}

	public YearMonthDay getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(YearMonthDay dtInicio) {
		this.dtInicio = dtInicio;
	}

	public YearMonthDay getDtFim() {
		return dtFim;
	}

	public void setDtFim(YearMonthDay dtFim) {
		this.dtFim = dtFim;
	}

	public Long getIdDiferencaComissao() {
		return idDiferencaComissao;
	}

	public void setIdDiferencaComissao(Long idDiferencaComissao) {
		this.idDiferencaComissao = idDiferencaComissao;
	}

	public BigDecimal getVlDiferencaComissao() {
		return vlDiferencaComissao;
	}

	public void setVlDiferencaComissao(BigDecimal vlDiferencaComissao) {
		this.vlDiferencaComissao = vlDiferencaComissao;
	}

	public YearMonthDay getDtCompetencia() {
		return dtCompetencia;
	}

	public void setDtCompetencia(YearMonthDay dtCompetencia) {
		this.dtCompetencia = dtCompetencia;
	}

	public DomainValue getTpTeto() {
		return tpTeto;
	}

	public void setTpTeto(DomainValue tpTeto) {
		this.tpTeto = tpTeto;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public YearMonthDay getDtVigenciaEquipeVendasInicial() {
		return dtVigenciaEquipeVendasInicial;
	}

	public void setDtVigenciaEquipeVendasInicial(YearMonthDay dtVigenciaEquipeVendasInicial) {
		this.dtVigenciaEquipeVendasInicial = dtVigenciaEquipeVendasInicial;
	}

	public YearMonthDay getDtVigenciaEquipeVendasFinal() {
		return dtVigenciaEquipeVendasFinal;
	}

	public void setDtVigenciaEquipeVendasFinal(YearMonthDay dtVigenciaEquipeVendasFinal) {
		this.dtVigenciaEquipeVendasFinal = dtVigenciaEquipeVendasFinal;
	}
	
}
