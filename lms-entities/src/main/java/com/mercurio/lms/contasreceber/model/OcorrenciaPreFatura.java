package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.vendas.model.Cliente;

/**
 * @author José Rodrigo Moraes
 * @since  28/04/2006
 */
public class OcorrenciaPreFatura implements Serializable {
    
	private static final long serialVersionUID = 1L;

    /** persistent field */
    private Long idOcorrenciaPreFatura;
    
    /** persistent field */
    private String nrPreFatura;
    
    /** persistent field */
    private YearMonthDay dtEmissao;
    
    /** persistent field */
    private YearMonthDay dtVencimento;
    
    /** persistent field */
    private DateTime dhImportacao;
    
    /** persistent field */
    private String nmArquivo;
    
    /** persistent field */
    private Cliente cliente;
    
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public DateTime getDhImportacao() {
        return dhImportacao;
    }

    public void setDhImportacao(DateTime dhImportacao) {
        this.dhImportacao = dhImportacao;
    }

    public YearMonthDay getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public YearMonthDay getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(YearMonthDay dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public Long getIdOcorrenciaPreFatura() {
        return idOcorrenciaPreFatura;
    }

    public void setIdOcorrenciaPreFatura(Long idOcorrenciaPreFatura) {
        this.idOcorrenciaPreFatura = idOcorrenciaPreFatura;
    }

    public String getNmArquivo() {
        return nmArquivo;
    }

    public void setNmArquivo(String nmArquivo) {
        this.nmArquivo = nmArquivo;
    }

    public String getNrPreFatura() {
        return nrPreFatura;
    }

    public void setNrPreFatura(String nrPreFatura) {
        this.nrPreFatura = nrPreFatura;
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaPreFatura))
			return false;
        OcorrenciaPreFatura castOther = (OcorrenciaPreFatura) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaPreFatura(),
				castOther.getIdOcorrenciaPreFatura()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaPreFatura())
            .toHashCode();
    }

}
