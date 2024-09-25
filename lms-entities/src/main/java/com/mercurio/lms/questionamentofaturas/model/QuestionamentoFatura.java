package com.mercurio.lms.questionamentofaturas.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.MotivoDesconto;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "QUESTIONAMENTOS_FATURAS")
@SequenceGenerator(name = "QUESTIONAMENTOS_FATURAS_SEQ", sequenceName = "SQ_QUESTIONAMENTOS_FATURAS")
public class QuestionamentoFatura implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idQuestionamentoFatura;
	private Filial filialCobradora;
	private Filial filialSolicitante;
	private Cliente cliente;
	private UsuarioLMS usuarioSolicitante;
	private UsuarioLMS usuarioApropriador;
	private MotivoDesconto motivoDesconto;
	private MotivoOcorrencia motivoCancelamento;
	private MotivoOcorrencia motivoSustacaoProtesto;
	private MotivoOcorrencia motivoProrrogacaoVcto;
	private DomainValue tpDocumento;

	private Boolean blConcedeAbatimentoSol;
	private Boolean blProrrogaVencimentoSol;
	private Boolean blSustarProtestoSol;
	private Boolean blBaixaTitCancelSol;
	private Boolean blRecalcularFreteSol;
	private Boolean blConcedeAbatimentoAprov;
	private Boolean blProrrogaVencimentoAprov;
	private Boolean blSustarProtestoAprov;
	private Boolean blBaixaTitCancelAprov;
	private Boolean blRecalcularFreteAprov;
	private YearMonthDay dtEmissaoDocumento;
	private BigDecimal vlDocumento;
	private DomainValue tpSituacaoBoleto;
	private YearMonthDay dtVencimentoDocumento;
	private String nrBoleto;
	private DateTime dhSolicitacao;
	private DateTime dhConclusao;
	private BigDecimal vlAbatimentoSolicitado;
	private YearMonthDay dtVencimentoSolicitado;
	private DomainValue tpSituacao;
	private DomainValue tpSetorCausadorAbatimento;
	private String dsChaveLiberacao;
	private String dsEmailRetorno;
	private String obAcaoCorretiva;
	private String obAbatimento;
	private DoctoServico doctoServico;
	private Fatura fatura;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTIONAMENTOS_FATURAS_SEQ")
	@Column(name = "ID_QUESTIONAMENTOS_FATURAS", nullable = false)
	public Long getIdQuestionamentoFatura() {
		return this.idQuestionamentoFatura;
	}

	public void setIdQuestionamentoFatura(Long idQuestionamentoFatura) {
		this.idQuestionamentoFatura = idQuestionamentoFatura;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_COBRADORA", nullable = false)
	public Filial getFilialCobradora() {
		return filialCobradora;
	}

	public void setFilialCobradora(Filial filialCobradora) {
		this.filialCobradora = filialCobradora;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_SOLICITANTE", nullable = false)
	public Filial getFilialSolicitante() {
		return filialSolicitante;
	}

	public void setFilialSolicitante(Filial filialSolicitante) {
		this.filialSolicitante = filialSolicitante;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE", nullable = false)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_SOLICITANTE", nullable = false)
	public UsuarioLMS getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(UsuarioLMS usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_APROPRIADOR")
	public UsuarioLMS getUsuarioApropriador() {
		return usuarioApropriador;
	}

	public void setUsuarioApropriador(UsuarioLMS usuarioApropriador) {
		this.usuarioApropriador = usuarioApropriador;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MOTIVO_DESCONTO")
	public MotivoDesconto getMotivoDesconto() {
		return motivoDesconto;
	}

	public void setMotivoDesconto(MotivoDesconto motivoDesconto) {
		this.motivoDesconto = motivoDesconto;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MOTIVO_CANCELAMENTO")
	public MotivoOcorrencia getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(MotivoOcorrencia motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MOTIVO_SUST_PROTESTO")
	public MotivoOcorrencia getMotivoSustacaoProtesto() {
		return motivoSustacaoProtesto;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MOTIVO_SUST_PROTESTO")
	public void setMotivoSustacaoProtesto(MotivoOcorrencia motivoSustacaoProtesto) {
		this.motivoSustacaoProtesto = motivoSustacaoProtesto;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DOCTO_SERVICO")
	public DoctoServico getDoctoServico() {
		return doctoServico;
	}
	
	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FATURA")
	public Fatura getFatura() {
		return fatura;
	}
	
	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MOTIVO_PRORROG_VCTO")
	public MotivoOcorrencia getMotivoProrrogacaoVcto() {
		return motivoProrrogacaoVcto;
	}

	public void setMotivoProrrogacaoVcto(MotivoOcorrencia motivoProrrogacaoVcto) {
		this.motivoProrrogacaoVcto = motivoProrrogacaoVcto;
	}

	@Column(name = "TP_DOCUMENTO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_DOC_QUEST_FATURAS") })
	public DomainValue getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(DomainValue tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	@Column(name = "BL_CONCEDE_ABATIMENTO_SOL", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlConcedeAbatimentoSol() {
		return blConcedeAbatimentoSol;
	}

	public void setBlConcedeAbatimentoSol(Boolean blConcedeAbatimentoSol) {
		this.blConcedeAbatimentoSol = blConcedeAbatimentoSol;
	}

	@Column(name = "BL_PRORROGA_VENCIMENTO_SOL", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlProrrogaVencimentoSol() {
		return blProrrogaVencimentoSol;
	}

	public void setBlProrrogaVencimentoSol(Boolean blProrrogaVencimentoSol) {
		this.blProrrogaVencimentoSol = blProrrogaVencimentoSol;
	}

	@Column(name = "BL_SUSTAR_PROTESTO_SOL", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlSustarProtestoSol() {
		return blSustarProtestoSol;
	}

	public void setBlSustarProtestoSol(Boolean blSustarProtestoSol) {
		this.blSustarProtestoSol = blSustarProtestoSol;
	}

	@Column(name = "BL_BAIXA_TIT_CANCEL_SOL", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlBaixaTitCancelSol() {
		return blBaixaTitCancelSol;
	}

	public void setBlBaixaTitCancelSol(Boolean blBaixaTitCancelSol) {
		this.blBaixaTitCancelSol = blBaixaTitCancelSol;
	}

	@Column(name = "BL_CONCEDE_ABATIMENTO_APROV", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlConcedeAbatimentoAprov() {
		return blConcedeAbatimentoAprov;
	}

	public void setBlConcedeAbatimentoAprov(Boolean blConcedeAbatimentoAprov) {
		this.blConcedeAbatimentoAprov = blConcedeAbatimentoAprov;
	}

	@Column(name = "BL_PRORROGA_VENCIMENTO_APROV", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlProrrogaVencimentoAprov() {
		return blProrrogaVencimentoAprov;
	}

	public void setBlProrrogaVencimentoAprov(Boolean blProrrogaVencimentoAprov) {
		this.blProrrogaVencimentoAprov = blProrrogaVencimentoAprov;
	}

	@Column(name = "BL_SUSTAR_PROTESTO_APROV", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlSustarProtestoAprov() {
		return blSustarProtestoAprov;
	}

	public void setBlSustarProtestoAprov(Boolean blSustarProtestoAprov) {
		this.blSustarProtestoAprov = blSustarProtestoAprov;
	}

	@Column(name = "BL_BAIXA_TIT_CANCEL_APROV", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlBaixaTitCancelAprov() {
		return blBaixaTitCancelAprov;
	}

	public void setBlBaixaTitCancelAprov(Boolean blBaixaTitCancelAprov) {
		this.blBaixaTitCancelAprov = blBaixaTitCancelAprov;
	}

	@Column(name = "BL_RECALCULAR_FRETE_SOL", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlRecalcularFreteSol() {
		return blRecalcularFreteSol;
	}

	public void setBlRecalcularFreteSol(Boolean blRecalcularFreteSol) {
		this.blRecalcularFreteSol = blRecalcularFreteSol;
	}
	
	@Column(name = "BL_RECALCULAR_FRETE_APROV", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlRecalcularFreteAprov() {
		return blRecalcularFreteAprov;
	}

	public void setBlRecalcularFreteAprov(Boolean blRecalcularFreteAprov) {
		this.blRecalcularFreteAprov = blRecalcularFreteAprov;
	}

	@Column(name = "DT_EMISSAO_DOCUMENTO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	public YearMonthDay getDtEmissaoDocumento() {
		return dtEmissaoDocumento;
	}

	public void setDtEmissaoDocumento(YearMonthDay dtEmissaoDocumento) {
		this.dtEmissaoDocumento = dtEmissaoDocumento;
	}

	@Column(name = "VL_DOCUMENTO")
	public BigDecimal getVlDocumento() {
		return vlDocumento;
	}

	public void setVlDocumento(BigDecimal vlDocumento) {
		this.vlDocumento = vlDocumento;
	}

	@Column(name = "TP_SITUACAO_BOLETO", length = 2)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS_BLOQUETE") })
	public DomainValue getTpSituacaoBoleto() {
		return tpSituacaoBoleto;
	}

	public void setTpSituacaoBoleto(DomainValue tpSituacaoBoleto) {
		this.tpSituacaoBoleto = tpSituacaoBoleto;
	}

	@Column(name = "DT_VENCIMENTO_DOCUMENTO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	public YearMonthDay getDtVencimentoDocumento() {
		return dtVencimentoDocumento;
	}

	public void setDtVencimentoDocumento(YearMonthDay dtVencimentoDocumento) {
		this.dtVencimentoDocumento = dtVencimentoDocumento;
	}

	@Column(name = "NR_BOLETO", length = 13)
	public String getNrBoleto() {
		return nrBoleto;
	}

	public void setNrBoleto(String nrBoleto) {
		this.nrBoleto = nrBoleto;
	}

	@Columns(columns = { @Column(name = "DH_SOLICITACAO"),
			@Column(name = "DH_SOLICITACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhSolicitacao() {
		return dhSolicitacao;
	}

	public void setDhSolicitacao(DateTime dhSolicitacao) {
		this.dhSolicitacao = dhSolicitacao;
	}

	@Columns(columns = { @Column(name = "DH_CONCLUSAO"),
			@Column(name = "DH_CONCLUSAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhConclusao() {
		return dhConclusao;
	}

	public void setDhConclusao(DateTime dhConclusao) {
		this.dhConclusao = dhConclusao;
	}

	@Column(name = "VL_ABATIMENTO_SOLICITADO")
	public BigDecimal getVlAbatimentoSolicitado() {
		return vlAbatimentoSolicitado;
	}

	public void setVlAbatimentoSolicitado(BigDecimal vlAbatimentoSolicitado) {
		this.vlAbatimentoSolicitado = vlAbatimentoSolicitado;
	}

	@Column(name = "DT_VENCIMENTO_SOLICITADO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	public YearMonthDay getDtVencimentoSolicitado() {
		return dtVencimentoSolicitado;
	}

	public void setDtVencimentoSolicitado(YearMonthDay dtVencimentoSolicitado) {
		this.dtVencimentoSolicitado = dtVencimentoSolicitado;
	}

	@Column(name = "TP_SITUACAO", length = 3, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_QUEST_FATURAS") })
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	@Column(name = "TP_SETOR_CAUSADOR_ABATIMENTO", length = 2)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SETOR_CAUSADOR") })
	public DomainValue getTpSetorCausadorAbatimento() {
		return tpSetorCausadorAbatimento;
	}

	public void setTpSetorCausadorAbatimento(
			DomainValue tpSetorCausadorAbatimento) {
		this.tpSetorCausadorAbatimento = tpSetorCausadorAbatimento;
	}

	@Column(name = "DS_CHAVE_LIBERACAO", length = 50)
	public String getDsChaveLiberacao() {
		return dsChaveLiberacao;
	}

	public void setDsChaveLiberacao(String dsChaveLiberacao) {
		this.dsChaveLiberacao = dsChaveLiberacao;
	}

	@Column(name = "DS_EMAIL_RETORNO", length = 100, nullable = false)
	public String getDsEmailRetorno() {
		return dsEmailRetorno;
	}

	public void setDsEmailRetorno(String dsEmailRetorno) {
		this.dsEmailRetorno = dsEmailRetorno;
	}

	@Column(name = "OB_ACAO_CORRETIVA", length = 500)
	public String getObAcaoCorretiva() {
		return obAcaoCorretiva;
	}

	public void setObAcaoCorretiva(String obAcaoCorretiva) {
		this.obAcaoCorretiva = obAcaoCorretiva;
	}

	@Column(name = "OB_ABATIMENTO", length = 500)
	public String getObAbatimento() {
		return obAbatimento;
	}

	public void setObAbatimento(String obAbatimento) {
		this.obAbatimento = obAbatimento;
	}
}