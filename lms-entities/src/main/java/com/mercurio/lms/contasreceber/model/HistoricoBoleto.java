package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class HistoricoBoleto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idHistoricoBoleto;

    /** persistent field */
    private DateTime dhOcorrencia;

    /** persistent field */
    private DomainValue tpSituacaoHistoricoBoleto;

    /** nullable persistent field */
    private String dsHistoricoBoleto;
    
    /** persistent field */
    private DomainValue tpSituacaoAprovacao;
    
    /** persistent field */
    private Long idPendencia;     

    /** persistent field */
    private Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.OcorrenciaBanco ocorrenciaBanco;

    /** persistent field */
    private Boleto boleto;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.MotivoOcorrencia motivoOcorrencia;

    /** persistent field */
    private List historicoMotivoOcorrencias;

    public Long getIdHistoricoBoleto() {
        return this.idHistoricoBoleto;
    }

    public void setIdHistoricoBoleto(Long idHistoricoBoleto) {
        this.idHistoricoBoleto = idHistoricoBoleto;
    }

    public DateTime getDhOcorrencia() {
        return this.dhOcorrencia;
    }

    public void setDhOcorrencia(DateTime dhOcorrencia) {
        this.dhOcorrencia = dhOcorrencia;
    }

    public DomainValue getTpSituacaoHistoricoBoleto() {
        return this.tpSituacaoHistoricoBoleto;
    }

	public void setTpSituacaoHistoricoBoleto(
			DomainValue tpSituacaoHistoricoBoleto) {
        this.tpSituacaoHistoricoBoleto = tpSituacaoHistoricoBoleto;
    }

    public String getDsHistoricoBoleto() {
        return this.dsHistoricoBoleto;
    }

    public void setDsHistoricoBoleto(String dsHistoricoBoleto) {
        this.dsHistoricoBoleto = dsHistoricoBoleto;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.contasreceber.model.OcorrenciaBanco getOcorrenciaBanco() {
        return this.ocorrenciaBanco;
    }

	public void setOcorrenciaBanco(
			com.mercurio.lms.contasreceber.model.OcorrenciaBanco ocorrenciaBanco) {
        this.ocorrenciaBanco = ocorrenciaBanco;
    }

    public Boleto getBoleto() {
        return this.boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }

    public com.mercurio.lms.contasreceber.model.MotivoOcorrencia getMotivoOcorrencia() {
        return this.motivoOcorrencia;
    }

	public void setMotivoOcorrencia(
			com.mercurio.lms.contasreceber.model.MotivoOcorrencia motivoOcorrencia) {
        this.motivoOcorrencia = motivoOcorrencia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.HistoricoMotivoOcorrencia.class)     
    public List getHistoricoMotivoOcorrencias() {
        return this.historicoMotivoOcorrencias;
    }

    public void setHistoricoMotivoOcorrencias(List historicoMotivoOcorrencias) {
        this.historicoMotivoOcorrencias = historicoMotivoOcorrencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idHistoricoBoleto",
				getIdHistoricoBoleto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HistoricoBoleto))
			return false;
        HistoricoBoleto castOther = (HistoricoBoleto) other;
		return new EqualsBuilder().append(this.getIdHistoricoBoleto(),
				castOther.getIdHistoricoBoleto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdHistoricoBoleto())
            .toHashCode();
    }

	public Long getIdPendencia() {
		return idPendencia;
	}

	public void setIdPendencia(Long idPendencia) {
		this.idPendencia = idPendencia;
	}

	public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

}
