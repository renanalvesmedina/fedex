package com.mercurio.lms.coleta.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class MilkRemetente implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String PRIMEIRA_SEMANA = "P";
	public static final String SEGUNDA_SEMANA = "S";
	public static final String TERCEIRA_SEMANA = "T";
	public static final String QUARTA_SEMANA = "Q";

    /** identifier field */
    private Long idMilkRemetente;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;
    
    /** persistent field */
    private com.mercurio.lms.coleta.model.MilkRun milkRun;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa;
    
    /** persistent field */
    private com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;
    
    /** persistent field */
    private List semanaRemetMruns;

    private Integer versao;
    
    public Long getIdMilkRemetente() {
        return this.idMilkRemetente;
    }

    public void setIdMilkRemetente(Long idMilkRemetente) {
        this.idMilkRemetente = idMilkRemetente;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.coleta.model.MilkRun getMilkRun() {
        return this.milkRun;
    }

    public void setMilkRun(com.mercurio.lms.coleta.model.MilkRun milkRun) {
        this.milkRun = milkRun;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.SemanaRemetMrun.class)     
    public List getSemanaRemetMruns() {
        return this.semanaRemetMruns;
    }

    public void setSemanaRemetMruns(List semanaRemetMruns) {
        this.semanaRemetMruns = semanaRemetMruns;
    }

    public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoPessoa() {
        return enderecoPessoa;
    }
    
	public void setEnderecoPessoa(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa) {
        this.enderecoPessoa = enderecoPessoa;
    }

    public com.mercurio.lms.expedicao.model.NaturezaProduto getNaturezaProduto() {
        return naturezaProduto;
    }
    
	public void setNaturezaProduto(
			com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto) {
        this.naturezaProduto = naturezaProduto;
    }
    
    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return servico;
    }
    
    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }
    
    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idMilkRemetente",
				getIdMilkRemetente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MilkRemetente))
			return false;
        MilkRemetente castOther = (MilkRemetente) other;
		return new EqualsBuilder().append(this.getIdMilkRemetente(),
				castOther.getIdMilkRemetente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMilkRemetente()).toHashCode();
    }
    
    public SemanaRemetMrun getSemanaRemetente(String tpSemanaDoMes) {
		if (tpSemanaDoMes == null
				|| (!tpSemanaDoMes.equals(MilkRemetente.PRIMEIRA_SEMANA)
						&& !tpSemanaDoMes.equals(MilkRemetente.SEGUNDA_SEMANA)
						&& !tpSemanaDoMes.equals(MilkRemetente.TERCEIRA_SEMANA) && !tpSemanaDoMes
						.equals(MilkRemetente.QUARTA_SEMANA))) {
    		
    			throw new IllegalArgumentException();
    	}
    	
    	if(this.semanaRemetMruns != null) {    		
	    	for(int i=0; i < this.semanaRemetMruns.size(); i++) {
				SemanaRemetMrun semanaRemetMrun = (SemanaRemetMrun) this.semanaRemetMruns
						.get(i);
				if (semanaRemetMrun.getTpSemanaDoMes().getValue()
						.equals(tpSemanaDoMes)) {
	    			return semanaRemetMrun;
	    		}
				
	    	}    	
    	}
    	return null;
    }

}
