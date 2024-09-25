package com.mercurio.lms.sim.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;


@Entity
@Table(name="EVENTO_MONITORAMENTO_CCT")
@SequenceGenerator(name="SQ_EVENTO_MONITORAMENTO_CCT", sequenceName="EVENTO_MONITORAMENTO_CCT_SQ", allocationSize=1)
public class EventoMonitoramentoCCT implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_EVENTO_MONITORAMENTO_CCT")
	@Column(name="ID_EVENTO_MONITORAMENTO_CCT", nullable=false)
	private Long idEventoMonitoramentoCCT;

	@ManyToOne(fetch=FetchType.LAZY , cascade={CascadeType.PERSIST , CascadeType.MERGE})
	@JoinColumn(name="ID_MONITORAMENTO_CCT", nullable=false, updatable=true, insertable=true)
	private MonitoramentoCCT monitoramentoCCT;

	@Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = false), @Column(name = "DH_INCLUSAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Column(name = "DT_EVENTO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtEvento;

	@ManyToOne
	@JoinColumn(name="ID_USUARIO", nullable=false)
	private UsuarioLMS usuario;

	@Column(name = "TP_SITUACAO_NF_CCT", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_NF_CCT") })
	private DomainValue tpSituacaoNotaFiscalCCT;

	@Column(name="DS_COMENTARIO", length=4000)
	private String dsComentario;

	public Long getIdEventoMonitoramentoCCT() {
		return idEventoMonitoramentoCCT;
	}

	public void setIdEventoMonitoramentoCCT(Long idEventoMonitoramentoCCT) {
		this.idEventoMonitoramentoCCT = idEventoMonitoramentoCCT;
	}

	public MonitoramentoCCT getMonitoramentoCCT() {
		return monitoramentoCCT;
	}

	public void setMonitoramentoCCT(MonitoramentoCCT monitoramentoCCT) {
		this.monitoramentoCCT = monitoramentoCCT;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public YearMonthDay getDtEvento() {
		return dtEvento;
	}

	public void setDtEvento(YearMonthDay dtEvento) {
		this.dtEvento = dtEvento;
	}

	public DomainValue getTpSituacaoNotaFiscalCCT() {
		return tpSituacaoNotaFiscalCCT;
	}

	public void setTpSituacaoNotaFiscalCCT(DomainValue tpSituacaoNotaFiscalCCT) {
		this.tpSituacaoNotaFiscalCCT = tpSituacaoNotaFiscalCCT;
	}

	public String getDsComentario() {
		return dsComentario;
	}

	public void setDsComentario(String dsComentario) {
		this.dsComentario = dsComentario;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}


}
