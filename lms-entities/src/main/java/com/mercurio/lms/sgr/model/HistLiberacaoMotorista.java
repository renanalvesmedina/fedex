package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name = "HIST_LIBERACAO_MOTORISTA")
public class HistLiberacaoMotorista implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_HIST_LIBERACAO_MOTORISTA", nullable = false)
	private Long idHistLiberacaoMotorista;

	@Column(name = "CPF_MOTORISTA")
	private String cpfMotorista;

	@Column(name = "NM_MOTORISTA")
	private String nmMotorista;

	@Column(name = "TP_EVENTO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_HIST_MOT_EVENTO") })
	private DomainValue tpEvento;

	@Column(name = "NR_LIBERACAO")
	private String nrLiberacao;

	@Column(name = "DT_CONCLUSAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtConclusao;

	@Column(name = "DT_VALIDADE")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtValidade;

	@Column(name = "TP_PESQUISA")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_HIST_MOT_PESQUISA") })
	private DomainValue tpPesquisa;

	@Column(name = "VL_SCORE_PESQUISA")
	private BigDecimal vlScorePesquisa;

	@Column(name = "VL_SCORE_RECOMENDADO")
	private BigDecimal vlScoreRecomendado;

	@Column(name = "DS_ERRO")
	private String dsErro;

	@Columns(columns = { @Column(name = "DH_PROCESSAMENTO"), @Column(name = "DH_PROCESSAMENTO_TZR ") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhProcessamento;

	private transient String arquivo = "<XML>";

	public Long getIdHistLiberacaoMotorista() {
		return idHistLiberacaoMotorista;
	}

	public void setIdHistLiberacaoMotorista(Long idHistLiberacaoMotorista) {
		this.idHistLiberacaoMotorista = idHistLiberacaoMotorista;
	}

	public String getCpfMotorista() {
		return cpfMotorista;
	}

	public void setCpfMotorista(String cpfMotorista) {
		this.cpfMotorista = cpfMotorista;
	}

	public String getNmMotorista() {
		return nmMotorista;
	}

	public void setNmMotorista(String nmMotorista) {
		this.nmMotorista = nmMotorista;
	}

	public DomainValue getTpEvento() {
		return tpEvento;
	}

	public void setTpEvento(DomainValue tpEvento) {
		this.tpEvento = tpEvento;
	}

	public String getNrLiberacao() {
		return nrLiberacao;
	}

	public void setNrLiberacao(String nrLiberacao) {
		this.nrLiberacao = nrLiberacao;
	}

	public YearMonthDay getDtConclusao() {
		return dtConclusao;
	}

	public void setDtConclusao(YearMonthDay dtConclusao) {
		this.dtConclusao = dtConclusao;
	}

	public YearMonthDay getDtValidade() {
		return dtValidade;
	}

	public void setDtValidade(YearMonthDay dtValidade) {
		this.dtValidade = dtValidade;
	}

	public DomainValue getTpPesquisa() {
		return tpPesquisa;
	}

	public void setTpPesquisa(DomainValue tpPesquisa) {
		this.tpPesquisa = tpPesquisa;
	}

	public BigDecimal getVlScorePesquisa() {
		return vlScorePesquisa;
	}

	public void setVlScorePesquisa(BigDecimal vlScorePesquisa) {
		this.vlScorePesquisa = vlScorePesquisa;
	}

	public BigDecimal getVlScoreRecomendado() {
		return vlScoreRecomendado;
	}

	public void setVlScoreRecomendado(BigDecimal vlScoreRecomendado) {
		this.vlScoreRecomendado = vlScoreRecomendado;
	}

	public String getDsErro() {
		return dsErro;
	}

	public void setDsErro(String dsErro) {
		this.dsErro = dsErro;
	}

	public DateTime getDhProcessamento() {
		return dhProcessamento;
	}

	public void setDhProcessamento(DateTime dhProcessamento) {
		this.dhProcessamento = dhProcessamento;
	}

	public String getArquivo() {
		return arquivo;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idHistLiberacaoMotorista)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof HistLiberacaoMotorista)) {
			return false;
		}
		HistLiberacaoMotorista cast = (HistLiberacaoMotorista) other;
		return new EqualsBuilder()
				.append(idHistLiberacaoMotorista, cast.idHistLiberacaoMotorista)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idHistLiberacaoMotorista)
				.toHashCode();
	}

}
