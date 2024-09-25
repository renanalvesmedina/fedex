package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;

public class ExecutivoTerritorioFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private TerritorioSuggestDTO territorio;
	private YearMonthDay periodoInicial;
	private YearMonthDay periodoFinal;
	private DomainValue tpExecutivo;
	private UsuarioDTO usuario;

	public TerritorioSuggestDTO getTerritorio() {
		return territorio;
	}

	public void setTerritorio(TerritorioSuggestDTO territorio) {
		this.territorio = territorio;
	}

	public YearMonthDay getPeriodoInicial() {
		return periodoInicial;
	}

	public void setPeriodoInicial(YearMonthDay periodoInicial) {
		this.periodoInicial = periodoInicial;
	}

	public YearMonthDay getPeriodoFinal() {
		return periodoFinal;
	}

	public void setPeriodoFinal(YearMonthDay periodoFinal) {
		this.periodoFinal = periodoFinal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public DomainValue getTpExecutivo() {
		return tpExecutivo;
	}

	public void setTpExecutivo(DomainValue tpExecutivo) {
		this.tpExecutivo = tpExecutivo;
	}

}
