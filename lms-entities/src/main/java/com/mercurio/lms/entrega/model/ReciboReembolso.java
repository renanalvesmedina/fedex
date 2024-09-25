package com.mercurio.lms.entrega.model;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReciboReembolso extends DoctoServico {

	private static final long serialVersionUID = 1L;

    /** persistent field */
    private Integer nrReciboReembolso;

    /** nullable persistent field */
    private BigDecimal vlAplicado;

    /** nullable persistent field */
    private String obMotivoValorAplicado;

    /** nullable persistent field */
    private String obReciboReembolso;
    
    private String obRecolhimento;
    
    /** nullable persistent field */
    private String obCancelamento;

    private DomainValue tpSituacaoRecibo;
    
    private DomainValue tpValorAtribuidoRecibo;
    
    private BigDecimal vlReembolso;
        
    private DateTime dhCancelamento;
    
    private DateTime dhDigitacaoCheque;
    
    /** nullable persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServicoByIdReciboReembolso;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServicoByIdDoctoServReembolsado;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    private ManifestoEntrega manifestoEntrega;  
    
    private ManifestoViagemNacional manifestoViagemNacional;
        
    /** persistent field */
    private List chequeReembolsos;

    /** persistent field */
    private List documentoMirs;

    public Integer getNrReciboReembolso() {
        return this.nrReciboReembolso;
    }

    public void setNrReciboReembolso(Integer nrReciboReembolso) {
        this.nrReciboReembolso = nrReciboReembolso;
    }

    public BigDecimal getVlAplicado() {
        return this.vlAplicado;
    }

    public void setVlAplicado(BigDecimal vlAplicado) {
        this.vlAplicado = vlAplicado;
    }

    public String getObMotivoValorAplicado() {
        return this.obMotivoValorAplicado;
    }

    public void setObMotivoValorAplicado(String obMotivoValorAplicado) {
        this.obMotivoValorAplicado = obMotivoValorAplicado;
    }

    public String getObReciboReembolso() {
        return this.obReciboReembolso;
    }

    public void setObReciboReembolso(String obReciboReembolso) {
        this.obReciboReembolso = obReciboReembolso;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServicoByIdReciboReembolso() {
        return this.doctoServicoByIdReciboReembolso;
    }

	public void setDoctoServicoByIdReciboReembolso(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServicoByIdReciboReembolso) {
        this.doctoServicoByIdReciboReembolso = doctoServicoByIdReciboReembolso;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServicoByIdDoctoServReembolsado() {
        return this.doctoServicoByIdDoctoServReembolsado;
    }

	public void setDoctoServicoByIdDoctoServReembolsado(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServicoByIdDoctoServReembolsado) {
        this.doctoServicoByIdDoctoServReembolsado = doctoServicoByIdDoctoServReembolsado;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.ChequeReembolso.class)     
    public List getChequeReembolsos() {
        return this.chequeReembolsos;
    }

    public void setChequeReembolsos(List chequeReembolsos) {
        this.chequeReembolsos = chequeReembolsos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.DocumentoMir.class)     
    public List getDocumentoMirs() {
        return this.documentoMirs;
    }

    public void setDocumentoMirs(List documentoMirs) {
        this.documentoMirs = documentoMirs;
    }

	/**
	 * @return Returns the manifestoEntrega.
	 */
	public ManifestoEntrega getManifestoEntrega() {
		return manifestoEntrega;
	}

	/**
	 * @param manifestoEntrega
	 *            The manifestoEntrega to set.
	 */
	public void setManifestoEntrega(ManifestoEntrega manifestoEntrega) {
		this.manifestoEntrega = manifestoEntrega;
	}

	/**
	 * @return Returns the manifestoViagemNacional.
	 */
	public ManifestoViagemNacional getManifestoViagemNacional() {
		return manifestoViagemNacional;
	}

	/**
	 * @param manifestoViagemNacional
	 *            The manifestoViagemNacional to set.
	 */
	public void setManifestoViagemNacional(
			ManifestoViagemNacional manifestoViagemNacional) {
		this.manifestoViagemNacional = manifestoViagemNacional;
	}

	/**
	 * @return Returns the tpSituacaoRecibo.
	 */
	public DomainValue getTpSituacaoRecibo() {
		return tpSituacaoRecibo;
	}

	/**
	 * @param tpSituacaoRecibo
	 *            The tpSituacaoRecibo to set.
	 */
	public void setTpSituacaoRecibo(DomainValue tpSituacaoRecibo) {
		this.tpSituacaoRecibo = tpSituacaoRecibo;
	}

	/**
	 * @return Returns the vlReembolso.
	 */
	public BigDecimal getVlReembolso() {
		return vlReembolso;
	}

	/**
	 * @param vlReembolso
	 *            The vlReembolso to set.
	 */
	public void setVlReembolso(BigDecimal vlReembolso) {
		this.vlReembolso = vlReembolso;
	}

	public String getObCancelamento() {
		return obCancelamento;
	}

	public void setObCancelamento(String obCancelamento) {
		this.obCancelamento = obCancelamento;
	}

	public DomainValue getTpValorAtribuidoRecibo() {
		return tpValorAtribuidoRecibo;
	}

	public void setTpValorAtribuidoRecibo(DomainValue tpValorAtribuidoRecibo) {
		this.tpValorAtribuidoRecibo = tpValorAtribuidoRecibo;
	}

	public String getObRecolhimento() {
		return obRecolhimento;
	}

	public void setObRecolhimento(String obRecolhimento) {
		this.obRecolhimento = obRecolhimento;
	}

	public DateTime getDhCancelamento() {
		return dhCancelamento;
	}

	public void setDhCancelamento(DateTime dhCancelamento) {
		this.dhCancelamento = dhCancelamento;
	}

	public DateTime getDhDigitacaoCheque() {
		return dhDigitacaoCheque;
	}

	public void setDhDigitacaoCheque(DateTime dhDigitacaoCheque) {
		this.dhDigitacaoCheque = dhDigitacaoCheque;
	}
	
}
