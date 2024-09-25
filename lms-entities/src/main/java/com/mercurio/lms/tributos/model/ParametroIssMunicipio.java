package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParametroIssMunicipio implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idParametroIssMunicipio;

    /** persistent field */
    private Byte dtDiaRecolhimento;

    /** persistent field */
    private DomainValue tpFormaPagamento;

    /** persistent field */
    private Boolean blProcEletronicoLivro;

    /** persistent field */
    private Boolean blEmissaoComCtrc;

    /** nullable persistent field */
    private Long nrCnpj;

    /** nullable persistent field */
    private Date dtAnoDispositivoLegal;

    /** nullable persistent field */
    private String dsSiteInternet;

    /** nullable persistent field */
    private DomainValue tpDispositivoLegal;

    /** nullable persistent field */
    private String nrDispositivoLegal;

    /** nullable persistent field */
    private String dsEndereco;

    /** nullable persistent field */
    private String nrDddTelefone;

    /** nullable persistent field */
    private String nrTelefone;

    /** nullable persistent field */
    private String nrDddFax;

    /** nullable persistent field */
    private String nrFax;

    /** nullable persistent field */
    private String dsContato;

    /** nullable persistent field */
    private String dsEmailContato;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** nullable persistent field */
    private Date dtEmissaoNotaFiscalEletronica;

    private Boolean blArredondamentoIss;

    private BigDecimal vlLimiteRetencaoIss;
    
    public Long getIdParametroIssMunicipio() {
        return this.idParametroIssMunicipio;
    }

    public void setIdParametroIssMunicipio(Long idParametroIssMunicipio) {
        this.idParametroIssMunicipio = idParametroIssMunicipio;
    }

    public Byte getDtDiaRecolhimento() {
        return this.dtDiaRecolhimento;
    }

    public void setDtDiaRecolhimento(Byte dtDiaRecolhimento) {
        this.dtDiaRecolhimento = dtDiaRecolhimento;
    }

    public DomainValue getTpFormaPagamento() {
        return this.tpFormaPagamento;
    }

    public void setTpFormaPagamento(DomainValue tpFormaPagamento) {
        this.tpFormaPagamento = tpFormaPagamento;
    }

    public Boolean getBlProcEletronicoLivro() {
        return this.blProcEletronicoLivro;
    }

    public void setBlProcEletronicoLivro(Boolean blProcEletronicoLivro) {
        this.blProcEletronicoLivro = blProcEletronicoLivro;
    }

    public Boolean getBlEmissaoComCtrc() {
        return this.blEmissaoComCtrc;
    }

    public void setBlEmissaoComCtrc(Boolean blEmissaoComCtrc) {
        this.blEmissaoComCtrc = blEmissaoComCtrc;
    }

    public Long getNrCnpj() {
        return this.nrCnpj;
    }

    public void setNrCnpj(Long nrCnpj) {
        this.nrCnpj = nrCnpj;
    }

    public Date getDtAnoDispositivoLegal() {
        return this.dtAnoDispositivoLegal;
    }

    public void setDtAnoDispositivoLegal(Date dtAnoDispositivoLegal) {
        this.dtAnoDispositivoLegal = dtAnoDispositivoLegal;
    }

    public String getDsSiteInternet() {
        return this.dsSiteInternet;
    }

    public void setDsSiteInternet(String dsSiteInternet) {
        this.dsSiteInternet = dsSiteInternet;
    }

    public DomainValue getTpDispositivoLegal() {
        return this.tpDispositivoLegal;
    }

    public void setTpDispositivoLegal(DomainValue tpDispositivoLegal) {
        this.tpDispositivoLegal = tpDispositivoLegal;
    }

    public String getNrDispositivoLegal() {
        return this.nrDispositivoLegal;
    }

    public void setNrDispositivoLegal(String nrDispositivoLegal) {
        this.nrDispositivoLegal = nrDispositivoLegal;
    }

    public String getDsEndereco() {
        return this.dsEndereco;
    }

    public void setDsEndereco(String dsEndereco) {
        this.dsEndereco = dsEndereco;
    }

    public String getNrDddTelefone() {
        return this.nrDddTelefone;
    }

    public void setNrDddTelefone(String nrDddTelefone) {
        this.nrDddTelefone = nrDddTelefone;
    }

    public String getNrTelefone() {
        return this.nrTelefone;
    }

    public void setNrTelefone(String nrTelefone) {
        this.nrTelefone = nrTelefone;
    }

    public String getNrDddFax() {
        return this.nrDddFax;
    }

    public void setNrDddFax(String nrDddFax) {
        this.nrDddFax = nrDddFax;
    }

    public String getNrFax() {
        return this.nrFax;
    }

    public void setNrFax(String nrFax) {
        this.nrFax = nrFax;
    }

    public String getDsContato() {
        return this.dsContato;
    }

    public void setDsContato(String dsContato) {
        this.dsContato = dsContato;
    }

    public String getDsEmailContato() {
        return this.dsEmailContato;
    }

    public void setDsEmailContato(String dsEmailContato) {
        this.dsEmailContato = dsEmailContato;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public Date getDtEmissaoNotaFiscalEletronica() {
		return dtEmissaoNotaFiscalEletronica;
	}

	public void setDtEmissaoNotaFiscalEletronica(
			Date dtEmissaoNotaFiscalEletronica) {
		this.dtEmissaoNotaFiscalEletronica = dtEmissaoNotaFiscalEletronica;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idParametroIssMunicipio",
				getIdParametroIssMunicipio()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParametroIssMunicipio))
			return false;
        ParametroIssMunicipio castOther = (ParametroIssMunicipio) other;
		return new EqualsBuilder().append(this.getIdParametroIssMunicipio(),
				castOther.getIdParametroIssMunicipio()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParametroIssMunicipio())
            .toHashCode();
    }

    public Boolean getBlArredondamentoIss() {
		return blArredondamentoIss;
}
    
    public void setBlArredondamentoIss(Boolean blArredondamentoIss) {
		this.blArredondamentoIss = blArredondamentoIss;
	}

	public BigDecimal getVlLimiteRetencaoIss() {
		return vlLimiteRetencaoIss;
}

	public void setVlLimiteRetencaoIss(BigDecimal vlLimiteRetencaoIss) {
		this.vlLimiteRetencaoIss = vlLimiteRetencaoIss;
	}

}
