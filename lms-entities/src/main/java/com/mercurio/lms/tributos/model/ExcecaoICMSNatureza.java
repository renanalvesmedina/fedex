package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import com.mercurio.lms.expedicao.model.NaturezaProduto;

/** @author LMS Custom Hibernate CodeGenerator */
public class ExcecaoICMSNatureza implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idExcecaoICMSNatureza;

    /** persistent field */
    private NaturezaProduto naturezaProduto;
    
    /** persistent field */
    private ExcecaoICMSCliente excecaoICMSCliente;

	public Long getIdExcecaoICMSNatureza() {
		return idExcecaoICMSNatureza;
	}

	public void setIdExcecaoICMSNatureza(Long idExcecaoICMSNatureza) {
		this.idExcecaoICMSNatureza = idExcecaoICMSNatureza;
	}

	public NaturezaProduto getNaturezaProduto() {
		return naturezaProduto;
	}

	public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}

	public ExcecaoICMSCliente getExcecaoICMSCliente() {
		return excecaoICMSCliente;
	}

	public void setExcecaoICMSCliente(ExcecaoICMSCliente excecaoICMSCliente) {
		this.excecaoICMSCliente = excecaoICMSCliente;
	}

}
