package com.mercurio.lms.entrega.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.mercurio.lms.sim.model.MonitoramentoCCT;


@Entity
@Table(name="AGENDAMENTO_MONIT_CCT")
@SequenceGenerator(name="SQ_AGENDAMENTO_MONIT_CCT", sequenceName="AGENDAMENTO_MONIT_CCT_SQ", allocationSize=1)
public class AgendamentoMonitCCT implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_AGENDAMENTO_MONIT_CCT")
	@Column(name="ID_AGENDAMENTO_MONIT_CCT", nullable=false)
	private Long idAgendamentoMonitCCT;
	
	@OneToOne
	@JoinColumn(name="ID_AGENDAMENTO_ENTREGA", nullable=false)
	private AgendamentoEntrega agendamentoEntrega;
	
	@OneToOne
	@JoinColumn(name="ID_MONITORAMENTO_CCT", nullable=false)
	private MonitoramentoCCT monitoramentoCCT;

	public Long getIdAgendamentoMonitCCT() {
		return idAgendamentoMonitCCT;
	}

	public void setIdAgendamentoMonitCCT(Long idAgendamentoMonitCCT) {
		this.idAgendamentoMonitCCT = idAgendamentoMonitCCT;
	}

	public AgendamentoEntrega getAgendamentoEntrega() {
		return agendamentoEntrega;
	}

	public void setAgendamentoEntrega(AgendamentoEntrega agendamentoEntrega) {
		this.agendamentoEntrega = agendamentoEntrega;
	}

	public MonitoramentoCCT getMonitoramentoCCT() {
		return monitoramentoCCT;
	}

	public void setMonitoramentoCCT(MonitoramentoCCT monitoramentoCCT) {
		this.monitoramentoCCT = monitoramentoCCT;
	}

}
