package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name = "LOG_ARQUIVO_EDI")
@Proxy(lazy=false)  
public class LogEDI implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long idLogEdi;
	
	private String nome;
	
	private String observacao;
	
	private String status;
	
	private YearMonthDay data;
	
	private TimeOfDay horaInicio;
	
	private TimeOfDay horaFim;
	
	private Filial filial;
	
	private ClienteEDIFilialEmbarcadora clienteEDIFilialEmbarcadora;
	
	private List<LogEDIDetalhe> logDetalheList;
	
	private String statusTmp;
	
	private Integer qtdePartes;
	
	@Id
	@Column(name = "ID_LOG_ARQUIVO_EDI", nullable = false)
	public Long getIdLogEdi() {
		return idLogEdi;
	}

	public void setIdLogEdi(Long idLogEdi) {
		this.idLogEdi = idLogEdi;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILI_ID_FILIAL", nullable = true)
	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CEID_CLIENTE_EDI_FILIAL_EMBARC", nullable = true)
	public ClienteEDIFilialEmbarcadora getClienteEDIFilialEmbarcadora() {
		return clienteEDIFilialEmbarcadora;
	}

	public void setClienteEDIFilialEmbarcadora(
			ClienteEDIFilialEmbarcadora clienteEDIFilialEmbarcadora) {
		this.clienteEDIFilialEmbarcadora = clienteEDIFilialEmbarcadora;
	}
	
	@OneToMany(mappedBy="logEDI")
	public List<LogEDIDetalhe> getLogDetalheList() {
		return logDetalheList;
	}

	public void setLogDetalheList(List<LogEDIDetalhe> logDetalheList) {
		this.logDetalheList = logDetalheList;
	}

	@Column(name = "NOME_ARQUIVO", length = 200)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "OBSERVACAO", length = 60)
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "STATUS", length = 30)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "DATA")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType") 
	public YearMonthDay getData() {
		return data;
	}

	public void setData(YearMonthDay data) {
		this.data = data;
	}

	@Column(name = "HORA_INI")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeTimeOfDayUserType") 
	public TimeOfDay getHoraInicio() {		
		return horaInicio;
	}

	public void setHoraInicio(TimeOfDay horaInicio) {
		this.horaInicio = horaInicio;
	}

	@Column(name = "HORA_FIM")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeTimeOfDayUserType") 
	public TimeOfDay getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(TimeOfDay horaFim) {
		this.horaFim = horaFim;
	}

	@Column(name = "STATUS_TMP", length = 30)
	public String getStatusTmp() {
		return statusTmp;
	}

	public void setStatusTmp(String statusTmp) {
		this.statusTmp = statusTmp;
	}

	@Column(name = "QTDE_PARTES")
	public Integer getQtdePartes() {
		return qtdePartes;
	}

	public void setQtdePartes(Integer qtdePartes) {
		this.qtdePartes = qtdePartes;
	}

}
