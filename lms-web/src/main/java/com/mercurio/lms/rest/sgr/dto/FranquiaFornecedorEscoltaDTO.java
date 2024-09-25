package com.mercurio.lms.rest.sgr.dto;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class FranquiaFornecedorEscoltaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idFornecedorEscolta;
	private DomainValue tpEscolta;
	private Long nrQuilometragem;
	private Long nrTempoViagemMinutos;
	private BigDecimal vlFranquia;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private BigDecimal vlHoraExcedente;
	private BigDecimal vlQuilometroExcedente;
	private List<FilialSuggestDTO> filiaisAtendimento;
	private FilialSuggestDTO filialOrigem;
	private FilialSuggestDTO filialDestino;

	public Long getIdFornecedorEscolta() {
		return idFornecedorEscolta;
	}

	public void setIdFornecedorEscolta(Long idFornecedorEscolta) {
		this.idFornecedorEscolta = idFornecedorEscolta;
	}

	public DomainValue getTpEscolta() {
		return tpEscolta;
	}

	public void setTpEscolta(DomainValue tpEscolta) {
		this.tpEscolta = tpEscolta;
	}

	public Long getNrQuilometragem() {
		return nrQuilometragem;
	}

	public void setNrQuilometragem(Long nrQuilometragem) {
		this.nrQuilometragem = nrQuilometragem;
	}

	public Long getNrTempoViagemMinutos() {
		return nrTempoViagemMinutos;
	}

	public void setNrTempoViagemMinutos(Long nrTempoViagemMinutos) {
		this.nrTempoViagemMinutos = nrTempoViagemMinutos;
	}

	public BigDecimal getVlFranquia() {
		return vlFranquia;
	}

	public void setVlFranquia(BigDecimal vlFranquia) {
		this.vlFranquia = vlFranquia;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public BigDecimal getVlHoraExcedente() {
		return vlHoraExcedente;
	}

	public void setVlHoraExcedente(BigDecimal vlHoraExcedente) {
		this.vlHoraExcedente = vlHoraExcedente;
	}

	public BigDecimal getVlQuilometroExcedente() {
		return vlQuilometroExcedente;
	}

	public void setVlQuilometroExcedente(BigDecimal vlQuilometroExcedente) {
		this.vlQuilometroExcedente = vlQuilometroExcedente;
	}

	public List<FilialSuggestDTO> getFiliaisAtendimento() {
		return filiaisAtendimento;
	}

	public void setFiliaisAtendimento(List<FilialSuggestDTO> filiaisAtendimento) {
		this.filiaisAtendimento = filiaisAtendimento;
	}

	public FilialSuggestDTO getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(FilialSuggestDTO filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public FilialSuggestDTO getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(FilialSuggestDTO filialDestino) {
		this.filialDestino = filialDestino;
	}

}
