package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.sim.model.TemplateRelatorio;

@Entity
@Table(name="ENVIO_CTE_CLIENTE")
@SequenceGenerator(name="SQ_ENVIO_CTE_CLIENTE", sequenceName="ENVIO_CTE_CLIENTE_SQ", allocationSize=1)
public class EnvioCteCliente implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_ENVIO_CTE_CLIENTE")
	@Column(name="ID_ENVIO_CTE_CLIENTE", nullable=false)
	private Long idEnvioCteCliente;
	
	@Column(name = "TP_ENVIO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_ENVIO_CTE") })
	private DomainValue tpEnvio;
	
	@Column(name = "TP_PARAMETRIZACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_EMAIL") })
	private DomainValue tpParametrizacao;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_CLIENTE", nullable=false)
	private Cliente cliente;
	
	@Column(name="BL_ENVIA_XML",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blEnviaXml; 
	
	@Column(name="BL_ENVIA_PDF",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blEnviaPdf; 
	
	@Column(name="BL_ENVIA_NFE",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blEnviaNfe; 
	
	@Column(name="DS_ASSUNTO", nullable=true, length=50)
	private String dsAssunto;
	
	@Column(name="BL_ESPACO_ASSUNTO",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blEspacoAssunto; 
	
	@Column(name="BL_CHAVE_ASSUNTO",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blChaveAssunto; 
	
	@Column(name = "NM_XML", length = 50, nullable = true)
	private String nmXml;
	
	@Column(name="BL_ESPACO_XML",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blEspacoXml; 
	
	@Column(name="BL_CHAVE_XML",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blChaveXml;
	
	@Column(name = "NM_PDF", length = 50, nullable = true)
	private String nmPdf;
	
	@Column(name="BL_ESPACO_PDF",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blEspacoPdf; 
	
	@Column(name="BL_CHAVE_PDF",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blChavePdf;
	
	@Column(name="DS_TEXTO_EMAIL", nullable=true, length=200)
	private String dsTextoEmail;
	
	@Column(name="BL_AGEND_AUTOMATICO",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blAgendamentoAutomatico;
	
	@Column(name="BL_CONFIRMA_AGEND",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blConfirmaAgendamento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TEMPLATE_RELATORIO")
	private TemplateRelatorio templateRelatorio;
	
	@Column(name = "DT_PERIODO_INICIAL", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType") 
	private YearMonthDay dtPeriodoInicial;
	
	@Column(name = "DT_PERIODO_FINAL", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType") 
	private YearMonthDay dtPeriodoFinal;
	
	@Column(name = "QT_DIAS_ESTOQUE", nullable = true)
	private Integer qtDiasEstoque; 
	
	
	public Long getIdEnvioCteCliente() {
		return idEnvioCteCliente;
	}
	public void setIdEnvioCteCliente(Long idEnvioCteCliente) {
		this.idEnvioCteCliente = idEnvioCteCliente;
	}
	public DomainValue getTpSituacao() {
		return tpEnvio;
	}
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpEnvio = tpSituacao;
	}
	public DomainValue getTpContingencia() {
		return tpParametrizacao;
	}
	public void setTpContingencia(DomainValue tpContingencia) {
		this.tpParametrizacao = tpContingencia;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Boolean getBlEnviaXml() {
		return blEnviaXml;
	}
	public void setBlEnviaXml(Boolean blEnviaXml) {
		this.blEnviaXml = blEnviaXml;
	}
	public Boolean getBlEnviaPdf() {
		return blEnviaPdf;
	}
	public void setBlEnviaPdf(Boolean blEnviaPdf) {
		this.blEnviaPdf = blEnviaPdf;
	}
	public Boolean getBlEnviaNfe() {
		return blEnviaNfe;
	}
	public void setBlEnviaNfe(Boolean blEnviaNfe) {
		this.blEnviaNfe = blEnviaNfe;
	}
	public String getDsAssunto() {
		return dsAssunto;
	}
	public void setDsAssunto(String dsAssunto) {
		this.dsAssunto = dsAssunto;
	}
	public DomainValue getTpEnvio() {
		return tpEnvio;
	}
	public void setTpEnvio(DomainValue tpEnvio) {
		this.tpEnvio = tpEnvio;
	}
	public DomainValue getTpParametrizacao() {
		return tpParametrizacao;
	}
	public void setTpParametrizacao(DomainValue tpParametrizacao) {
		this.tpParametrizacao = tpParametrizacao;
	}
	public Boolean getBlEspacoAssunto() {
		return blEspacoAssunto;
	}
	public void setBlEspacoAssunto(Boolean blEspacoAssunto) {
		this.blEspacoAssunto = blEspacoAssunto;
	}
	public Boolean getBlChaveAssunto() {
		return blChaveAssunto;
	}
	public void setBlChaveAssunto(Boolean blChaveAssunto) {
		this.blChaveAssunto = blChaveAssunto;
	}
	public String getNmXml() {
		return nmXml;
	}
	public void setNmXml(String nmXml) {
		this.nmXml = nmXml;
	}
	public Boolean getBlEspacoXml() {
		return blEspacoXml;
	}
	public void setBlEspacoXml(Boolean blEspacoXml) {
		this.blEspacoXml = blEspacoXml;
	}
	public Boolean getBlChaveXml() {
		return blChaveXml;
	}
	public void setBlChaveXml(Boolean blChaveXml) {
		this.blChaveXml = blChaveXml;
	}
	public String getNmPdf() {
		return nmPdf;
	}
	public void setNmPdf(String nmPdf) {
		this.nmPdf = nmPdf;
	}
	public Boolean getBlEspacoPdf() {
		return blEspacoPdf;
	}
	public void setBlEspacoPdf(Boolean blEspacoPdf) {
		this.blEspacoPdf = blEspacoPdf;
	}
	public Boolean getBlChavePdf() {
		return blChavePdf;
	}
	public void setBlChavePdf(Boolean blChavePdf) {
		this.blChavePdf = blChavePdf;
	}
	public String getDsTextoEmail() {
		return dsTextoEmail;
	}
	public void setDsTextoEmail(String dsTextoEmail) {
		this.dsTextoEmail = dsTextoEmail;
	}
	public Boolean getBlAgendamentoAutomatico() {
		return blAgendamentoAutomatico;
	}
	public void setBlAgendamentoAutomatico(Boolean blAgendamentoAutomatico) {
		this.blAgendamentoAutomatico = blAgendamentoAutomatico;
	}
	public Boolean getBlConfirmaAgendamento() {
		return blConfirmaAgendamento;
	}
	public void setBlConfirmaAgendamento(Boolean blConfirmaAgendamento) {
		this.blConfirmaAgendamento = blConfirmaAgendamento;
	}
	public TemplateRelatorio getTemplateRelatorio() {
		return templateRelatorio;
	}
	public void setTemplateRelatorio(TemplateRelatorio templateRelatorio) {
		this.templateRelatorio = templateRelatorio;
	}
	public Integer getQtDiasEstoque() {
		return qtDiasEstoque;
	}
	public void setQtDiasEstoque(Integer qtDiasEstoque) {
		this.qtDiasEstoque = qtDiasEstoque;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public YearMonthDay getDtPeriodoInicial() {
		return dtPeriodoInicial;
	}
	public void setDtPeriodoInicial(YearMonthDay dtPeriodoInicial) {
		this.dtPeriodoInicial = dtPeriodoInicial;
	}
	public YearMonthDay getDtPeriodoFinal() {
		return dtPeriodoFinal;
	}
	public void setDtPeriodoFinal(YearMonthDay dtPeriodoFinal) {
		this.dtPeriodoFinal = dtPeriodoFinal;
	}

}
