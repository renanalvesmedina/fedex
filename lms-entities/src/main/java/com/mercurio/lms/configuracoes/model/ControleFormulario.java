package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class ControleFormulario implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idControleFormulario;

    /** persistent field */
    private Long nrFormularioInicial;

    /** persistent field */
    private Long nrFormularioFinal;

    /** persistent field */
    private YearMonthDay dtRecebimento;

    /** persistent field */
    private DomainValue tpSituacaoFormulario;

    /** persistent field */
    private DomainValue tpFormulario;

    /** nullable persistent field */
    private String nrAidf;

    /** nullable persistent field */
    private Long nrSeloFiscalInicial;

    /** nullable persistent field */
    private Long nrSeloFiscalFinal;

    /** persistent field */
    private Long nrCodigoBarrasInicial;

    /** persistent field */
    private Long nrCodigoBarrasFinal;

    /** nullable persistent field */
    private String cdSerie;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.ControleFormulario controleFormulario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List controleFormularios;

    /** persistent field */
    private List impressoraFormularios;

    public Long getIdControleFormulario() {
        return this.idControleFormulario;
    }

    public void setIdControleFormulario(Long idControleFormulario) {
        this.idControleFormulario = idControleFormulario;
    }

    public Long getNrFormularioInicial() {
        return this.nrFormularioInicial;
    }

    public void setNrFormularioInicial(Long nrFormularioInicial) {
        this.nrFormularioInicial = nrFormularioInicial;
    }

    public Long getNrFormularioFinal() {
        return this.nrFormularioFinal;
    }

    public void setNrFormularioFinal(Long nrFormularioFinal) {
        this.nrFormularioFinal = nrFormularioFinal;
    }

    public YearMonthDay getDtRecebimento() {
        return this.dtRecebimento;
    }

    public void setDtRecebimento(YearMonthDay dtRecebimento) {
        this.dtRecebimento = dtRecebimento;
    }

    public DomainValue getTpSituacaoFormulario() {
        return this.tpSituacaoFormulario;
    }

    public void setTpSituacaoFormulario(DomainValue tpSituacaoFormulario) {
        this.tpSituacaoFormulario = tpSituacaoFormulario;
    }

    public DomainValue getTpFormulario() {
        return this.tpFormulario;
    }

    public void setTpFormulario(DomainValue tpFormulario) {
        this.tpFormulario = tpFormulario;
    }

    public String getNrAidf() {
        return this.nrAidf;
    }

    public void setNrAidf(String nrAidf) {
        this.nrAidf = nrAidf;
    }

    public Long getNrSeloFiscalInicial() {
        return this.nrSeloFiscalInicial;
    }

    public void setNrSeloFiscalInicial(Long nrSeloFiscalInicial) {
        this.nrSeloFiscalInicial = nrSeloFiscalInicial;
    }

    public Long getNrSeloFiscalFinal() {
        return this.nrSeloFiscalFinal;
    }

    public void setNrSeloFiscalFinal(Long nrSeloFiscalFinal) {
        this.nrSeloFiscalFinal = nrSeloFiscalFinal;
    }

    public String getCdSerie() {
        return this.cdSerie;
    }

    public void setCdSerie(String cdSerie) {
        this.cdSerie = cdSerie;
    }

    public com.mercurio.lms.configuracoes.model.ControleFormulario getControleFormulario() {
        return this.controleFormulario;
    }

	public void setControleFormulario(
			com.mercurio.lms.configuracoes.model.ControleFormulario controleFormulario) {
        this.controleFormulario = controleFormulario;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ControleFormulario.class)     
    public List getControleFormularios() {
        return this.controleFormularios;
    }

    public void setControleFormularios(List controleFormularios) {
        this.controleFormularios = controleFormularios;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ImpressoraFormulario.class)     
    public List getImpressoraFormularios() {
        return this.impressoraFormularios;
    }

    public void setImpressoraFormularios(List impressoraFormularios) {
        this.impressoraFormularios = impressoraFormularios;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idControleFormulario",
				getIdControleFormulario()).toString();
    }

    public Long getNrCodigoBarrasInicial() {
		return nrCodigoBarrasInicial;
	}
    
	public void setNrCodigoBarrasInicial(Long nrCodigoBarrasInicial) {
		this.nrCodigoBarrasInicial = nrCodigoBarrasInicial;
	}

	public Long getNrCodigoBarrasFinal() {
		return nrCodigoBarrasFinal;
	}

	public void setNrCodigoBarrasFinal(Long nrCodigoBarrasFinal) {
		this.nrCodigoBarrasFinal = nrCodigoBarrasFinal;
	}

    /**
     * Retorna os valores do Estoque Origem.
	 * 
	 * @return "Sigla da Filial" - "Nr forumário inicial" -
	 *         "Nr formulário final"
     */
    public String getControleFormOrigem() {
    	
    	StringBuffer controleForm = new StringBuffer();
    	ControleFormulario cfEstoqueOrigem = this.getControleFormulario();
    	//ControleFormulario deve possuir um EstoqueOrigem
    	if (cfEstoqueOrigem != null){ 
    		if (!Hibernate.isInitialized(cfEstoqueOrigem)){
    			Hibernate.initialize(cfEstoqueOrigem);
    		}
    		Filial filial = cfEstoqueOrigem.getFilial();
    		if (Hibernate.isInitialized(filial)){
    			controleForm.append(filial.getSgFilial()).append(" - ");
    		}
			controleForm.append(cfEstoqueOrigem.getNrFormularioInicial())
					.append(" - ");
    		controleForm.append(cfEstoqueOrigem.getNrFormularioFinal());
    	}
    	return controleForm.toString();
    }
    
    /**
     * 
	 * @return "Sigla da Filial" - "Nr forumário inicial" -
	 *         "Nr formulário final"
     */
    public String getControleForm() {
    	StringBuffer controleForm = new StringBuffer();
    	Filial f = getFilial();
    	if (Hibernate.isInitialized(f) && f != null ){
			controleForm.append(f.getSgFilial()).append(" - ")
					.append(this.nrFormularioInicial).append(" - ");
    		controleForm.append(this.nrFormularioFinal);
    	}
    	return controleForm.toString();
    }
    
    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ControleFormulario))
			return false;
        ControleFormulario castOther = (ControleFormulario) other;
		return new EqualsBuilder().append(this.getIdControleFormulario(),
				castOther.getIdControleFormulario()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdControleFormulario())
            .toHashCode();
    }

}
