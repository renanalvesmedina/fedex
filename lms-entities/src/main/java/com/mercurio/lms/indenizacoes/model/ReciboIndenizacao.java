package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReciboIndenizacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idReciboIndenizacao;

    /** persistent field */
    private Integer nrReciboIndenizacao;

    /** persistent field */
    private Integer qtVolumesIndenizados;

    /** persistent field */
    private BigDecimal vlIndenizacao;

    /** persistent field */
    private DomainValue tpIndenizacao;

    /** persistent field */
    private DomainValue tpSituacaoWorkflow;

    /** persistent field */
    private Pendencia pendencia;
    
    /** persistent field */
    private LoteJdeRim loteJdeRim;
    
    /** persistent field */
    private DomainValue tpBeneficiarioIndenizacao;

    /** persistent field */
    private DomainValue tpStatusIndenizacao;

    /** persistent field */
    private DomainValue tpFormaPagamento;

    /** persistent field */
    private DomainValue tpFavorecidoIndenizacao;
    
    /** persistent field */
    private Boolean blSalvados;
    
    /** persistent field */
    private Boolean blMaisUmaOcorrencia;
    
    /** persistent field */
    private Boolean blSegurado;
    
    private Boolean blEmailPgto; 
    
    /** persistent field */
    private Integer versao;

    /** nullable persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private YearMonthDay dtGeracao;    
    
    /** nullable persistent field */
    private YearMonthDay dtProgramadaPagamento;

    /** nullable persistent field */
    private YearMonthDay dtLiberacaoPagamento;

    /** nullable persistent field */
    private YearMonthDay dtPagamentoEfetuado;
    
    /** nullable persistent field */
    private YearMonthDay dtDevolucaoBanco;    

    
    /** nullable persistent field */
    private Long nrNotaFiscalDebitoCliente;

    /** nullable persistent field */
    private Long nrContaCorrente;

    /** nullable persistent field */
    private Byte qtParcelasBoletoBancario;

    /** nullable persistent field */
    private BigDecimal vlJuros;

    /** nullable persistent field */
    private String nrDigitoContaCorrente;

    /** nullable persistent field */
    private String obReciboIndenizacao;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.AgenciaBancaria agenciaBancaria;

    /** persistent field */
    private com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Banco banco;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoaByIdFavorecido;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoaByIdBeneficiario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List filialDebitadas;

    /** persistent field */
    private List doctoServicoIndenizacoes;

    /** persistent field */
    private List mdaSalvadoIndenizacoes;

    /** persistent field */
    private List eventoRims;

    /** persistent field */
    private List parcelaReciboIndenizacoes;

    private List avisoPagoRim;

    public Long getIdReciboIndenizacao() {
        return this.idReciboIndenizacao;
    }

    public void setIdReciboIndenizacao(Long idReciboIndenizacao) {
        this.idReciboIndenizacao = idReciboIndenizacao;
    }

    public Integer getNrReciboIndenizacao() {
        return this.nrReciboIndenizacao;
    }

    public void setNrReciboIndenizacao(Integer nrReciboIndenizacao) {
        this.nrReciboIndenizacao = nrReciboIndenizacao;
    }

    public Integer getQtVolumesIndenizados() {
        return this.qtVolumesIndenizados;
    }

    public void setQtVolumesIndenizados(Integer qtVolumesIndenizados) {
        this.qtVolumesIndenizados = qtVolumesIndenizados;
    }

    public BigDecimal getVlIndenizacao() {
        return this.vlIndenizacao;
    }

    public void setVlIndenizacao(BigDecimal vlIndenizacao) {
        this.vlIndenizacao = vlIndenizacao;
    }

    public DomainValue getTpIndenizacao() {
        return this.tpIndenizacao;
    }

    public void setTpIndenizacao(DomainValue tpIndenizacao) {
        this.tpIndenizacao = tpIndenizacao;
    }

    public DomainValue getTpBeneficiarioIndenizacao() {
        return this.tpBeneficiarioIndenizacao;
    }

	public void setTpBeneficiarioIndenizacao(
			DomainValue tpBeneficiarioIndenizacao) {
        this.tpBeneficiarioIndenizacao = tpBeneficiarioIndenizacao;
    }

    public DomainValue getTpStatusIndenizacao() {
        return this.tpStatusIndenizacao;
    }

    public void setTpStatusIndenizacao(DomainValue tpStatusIndenizacao) {
        this.tpStatusIndenizacao = tpStatusIndenizacao;
    }

    public DomainValue getTpFormaPagamento() {
        return this.tpFormaPagamento;
    }

    public void setTpFormaPagamento(DomainValue tpFormaPagamento) {
        this.tpFormaPagamento = tpFormaPagamento;
    }

    public DomainValue getTpFavorecidoIndenizacao() {
		return tpFavorecidoIndenizacao;
	}

	public void setTpFavorecidoIndenizacao(DomainValue tpFavorecidoIndenizacao) {
		this.tpFavorecidoIndenizacao = tpFavorecidoIndenizacao;
	}

	public Boolean getBlSalvados() {
        return this.blSalvados;
    }

    public void setBlSalvados(Boolean blSalvados) {
        this.blSalvados = blSalvados;
    }

    public Boolean getBlMaisUmaOcorrencia() {
		return blMaisUmaOcorrencia;
	}

	public void setBlMaisUmaOcorrencia(Boolean blMaisUmaOcorrencia) {
		this.blMaisUmaOcorrencia = blMaisUmaOcorrencia;
	}

	public Boolean getBlEmailPgto() {
		return blEmailPgto;
	}

	public void setBlEmailPgto(Boolean blEmailPgto) {
		this.blEmailPgto = blEmailPgto;
	}

	public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public YearMonthDay getDtProgramadaPagamento() {
        return this.dtProgramadaPagamento;
    }

    public void setDtProgramadaPagamento(YearMonthDay dtProgramadaPagamento) {
        this.dtProgramadaPagamento = dtProgramadaPagamento;
    }

    public YearMonthDay getDtLiberacaoPagamento() {
        return this.dtLiberacaoPagamento;
    }

    public void setDtLiberacaoPagamento(YearMonthDay dtLiberacaoPagamento) {
        this.dtLiberacaoPagamento = dtLiberacaoPagamento;
    }

    public YearMonthDay getDtPagamentoEfetuado() {
        return this.dtPagamentoEfetuado;
    }

    public void setDtPagamentoEfetuado(YearMonthDay dtPagamentoEfetuado) {
        this.dtPagamentoEfetuado = dtPagamentoEfetuado;
    }
    
    public YearMonthDay getDtDevolucaoBanco() {
		return dtDevolucaoBanco;
	}

	public void setDtDevolucaoBanco(YearMonthDay dtDevolucaoBanco) {
		this.dtDevolucaoBanco = dtDevolucaoBanco;
	}

	public YearMonthDay getDtGeracao() {
		return dtGeracao;
	}

	public void setDtGeracao(YearMonthDay dtGeracao) {
		this.dtGeracao = dtGeracao;
	}

    public Long getNrNotaFiscalDebitoCliente() {
        return this.nrNotaFiscalDebitoCliente;
    }

    public void setNrNotaFiscalDebitoCliente(Long nrNotaFiscalDebitoCliente) {
        this.nrNotaFiscalDebitoCliente = nrNotaFiscalDebitoCliente;
    }

    public Long getNrContaCorrente() {
        return this.nrContaCorrente;
    }

    public void setNrContaCorrente(Long nrContaCorrente) {
        this.nrContaCorrente = nrContaCorrente;
    }

    public Byte getQtParcelasBoletoBancario() {
        return this.qtParcelasBoletoBancario;
    }

    public void setQtParcelasBoletoBancario(Byte qtParcelasBoletoBancario) {
        this.qtParcelasBoletoBancario = qtParcelasBoletoBancario;
    }

    public BigDecimal getVlJuros() {
        return this.vlJuros;
    }

    public void setVlJuros(BigDecimal vlJuros) {
        this.vlJuros = vlJuros;
    }

    public String getNrDigitoContaCorrente() {
        return this.nrDigitoContaCorrente;
    }

    public void setNrDigitoContaCorrente(String nrDigitoContaCorrente) {
        this.nrDigitoContaCorrente = nrDigitoContaCorrente;
    }

    public String getObReciboIndenizacao() {
        return this.obReciboIndenizacao;
    }

    public void setObReciboIndenizacao(String obReciboIndenizacao) {
        this.obReciboIndenizacao = obReciboIndenizacao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.configuracoes.model.AgenciaBancaria getAgenciaBancaria() {
        return this.agenciaBancaria;
    }

	public void setAgenciaBancaria(
			com.mercurio.lms.configuracoes.model.AgenciaBancaria agenciaBancaria) {
        this.agenciaBancaria = agenciaBancaria;
    }

    public com.mercurio.lms.seguros.model.ProcessoSinistro getProcessoSinistro() {
        return this.processoSinistro;
    }

	public void setProcessoSinistro(
			com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro) {
        this.processoSinistro = processoSinistro;
    }

    public com.mercurio.lms.configuracoes.model.Banco getBanco() {
        return this.banco;
    }

    public void setBanco(com.mercurio.lms.configuracoes.model.Banco banco) {
        this.banco = banco;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoaByIdFavorecido() {
        return this.pessoaByIdFavorecido;
    }

	public void setPessoaByIdFavorecido(
			com.mercurio.lms.configuracoes.model.Pessoa pessoaByIdFavorecido) {
        this.pessoaByIdFavorecido = pessoaByIdFavorecido;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoaByIdBeneficiario() {
        return this.pessoaByIdBeneficiario;
    }

	public void setPessoaByIdBeneficiario(
			com.mercurio.lms.configuracoes.model.Pessoa pessoaByIdBeneficiario) {
        this.pessoaByIdBeneficiario = pessoaByIdBeneficiario;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.FilialDebitada.class)     
    public List getFilialDebitadas() {
        return this.filialDebitadas;
    }

    public void setFilialDebitadas(List filialDebitadas) {
        this.filialDebitadas = filialDebitadas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao.class)     
    public List getDoctoServicoIndenizacoes() {
        return this.doctoServicoIndenizacoes;
    }

    public void setDoctoServicoIndenizacoes(List doctoServicoIndenizacoes) {
        this.doctoServicoIndenizacoes = doctoServicoIndenizacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.MdaSalvadoIndenizacao.class)     
    public List getMdaSalvadoIndenizacoes() {
        return this.mdaSalvadoIndenizacoes;
    }

    public void setMdaSalvadoIndenizacoes(List mdaSalvadoIndenizacoes) {
        this.mdaSalvadoIndenizacoes = mdaSalvadoIndenizacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.EventoRim.class)     
    public List getEventoRims() {
        return this.eventoRims;
    }

    public void setEventoRims(List eventoRims) {
        this.eventoRims = eventoRims;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.ParcelaReciboIndenizacao.class)     
    public List getParcelaReciboIndenizacoes() {
        return this.parcelaReciboIndenizacoes;
    }

    public void setParcelaReciboIndenizacoes(List parcelaReciboIndenizacoes) {
        this.parcelaReciboIndenizacoes = parcelaReciboIndenizacoes;
    }

	@ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.AvisoPagoRimInd.class)
	public List getAvisoPagoRim() {
		return avisoPagoRim;
	}

	public void setAvisoPagoRim(List avisoPagoRim) {
		this.avisoPagoRim = avisoPagoRim;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idReciboIndenizacao",
				getIdReciboIndenizacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReciboIndenizacao))
			return false;
        ReciboIndenizacao castOther = (ReciboIndenizacao) other;
		return new EqualsBuilder().append(this.getIdReciboIndenizacao(),
				castOther.getIdReciboIndenizacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdReciboIndenizacao())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public DomainValue getTpSituacaoWorkflow() {
		return tpSituacaoWorkflow;
	}

	public void setTpSituacaoWorkflow(DomainValue tpSituacaoWorkflow) {
		this.tpSituacaoWorkflow = tpSituacaoWorkflow;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public LoteJdeRim getLoteJdeRim() {
		return loteJdeRim;
	}

	public void setLoteJdeRim(LoteJdeRim loteJdeRim) {
		this.loteJdeRim = loteJdeRim;
	}

	public Boolean getBlSegurado() {
		return blSegurado;
}

	public void setBlSegurado(Boolean blSegurado) {
		this.blSegurado = blSegurado;
	}

}
