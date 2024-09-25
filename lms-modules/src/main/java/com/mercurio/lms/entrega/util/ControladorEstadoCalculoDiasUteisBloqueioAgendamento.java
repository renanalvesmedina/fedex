package com.mercurio.lms.entrega.util;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * Classe utilizada como retorno e para manter o estado da iteração do negócio de "Calcula os dias úteis bloqueio e agendamento de um documento de serviço"
 * que se encontra na classe CalcularDiasUteisBloqueioAgendamento
 * @author rtavares
 *
 */
public class ControladorEstadoCalculoDiasUteisBloqueioAgendamento implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private DateTime dia;
	private EstadoDia estadoDia;
	private EstadoBloqueio estadoBloqueio;
	private EstadoAgendamento estadoAgendamento;
	private Short quantidadeDiasPrevisto;
	private Short nrDiasRealEntrega;
	
	
	public DateTime getDia() {
		return dia;
	}
	public void setDia(DateTime dia) {
		this.dia = dia;
	}
	public EstadoDia getEstadoDia() {
		return estadoDia;
	}
	public void setEstadoDia(EstadoDia estadoDia) {
		this.estadoDia = estadoDia;
	}
	public EstadoBloqueio getEstadoBloqueio() {
		return estadoBloqueio;
	}
	public void setEstadoBloqueio(EstadoBloqueio estadoBloqueio) {
		this.estadoBloqueio = estadoBloqueio;
	}
	public EstadoAgendamento getEstadoAgendamento() {
		return estadoAgendamento;
	}
	public void setEstadoAgendamento(EstadoAgendamento estadoAgendamento) {
		this.estadoAgendamento = estadoAgendamento;
	}
	public Short getQuantidadeDiasPrevisto() {
		return quantidadeDiasPrevisto;
	}
	public void setQuantidadeDiasPrevisto(Short quantidadeDiasPrevisto) {
		this.quantidadeDiasPrevisto = quantidadeDiasPrevisto;
	}
	public Short getNrDiasRealEntrega() {
		return nrDiasRealEntrega;
	}
	public void setNrDiasRealEntrega(Short nrDiasRealEntrega) {
		this.nrDiasRealEntrega = nrDiasRealEntrega;
	}
}
