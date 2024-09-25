package com.mercurio.lms.edi.model;

import java.io.Serializable;

import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class LogArquivoEDI implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long idLogArquivo;
	
	private String nome;
	
	private String observacao;
	
	private String status;
	
	private YearMonthDay data;
	
	private TimeOfDay horaInicio;
	
	private TimeOfDay horaFim;
	
	private Long idFilial;
	
	private Long idCliente;
	
	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public Long getIdLogArquivo() {
		return idLogArquivo;
	}

	public void setIdLogArquivo(Long idLogArquivo) {
		this.idLogArquivo = idLogArquivo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public YearMonthDay getData() {
		return data;
	}

	public void setData(YearMonthDay data) {
		this.data = data;
	}

	public TimeOfDay getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(TimeOfDay horaInicio) {
		this.horaInicio = horaInicio;
	}

	public TimeOfDay getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(TimeOfDay horaFim) {
		this.horaFim = horaFim;
	}

}
