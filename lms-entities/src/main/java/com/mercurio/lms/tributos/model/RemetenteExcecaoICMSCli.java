package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class RemetenteExcecaoICMSCli implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRemetenteExcecaoICMSCli;

    /** persistent field */
    private String nrCnpjParcialRem;    
    
    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    /** nullable persistent field */
    private ExcecaoICMSCliente excecaoICMSCliente;

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public ExcecaoICMSCliente getExcecaoICMSCliente() {
		return excecaoICMSCliente;
	}

	public void setExcecaoICMSCliente(ExcecaoICMSCliente excecaoICMSCliente) {
		this.excecaoICMSCliente = excecaoICMSCliente;
	}

	public Long getIdRemetenteExcecaoICMSCli() {
		return idRemetenteExcecaoICMSCli;
	}

	public void setIdRemetenteExcecaoICMSCli(Long idRemetenteExcecaoICMSCli) {
		this.idRemetenteExcecaoICMSCli = idRemetenteExcecaoICMSCli;
	}

	public String getNrCnpjParcialRem() {
		return nrCnpjParcialRem;
	}

	public void setNrCnpjParcialRem(String nrCnpjParcialRem) {
		this.nrCnpjParcialRem = nrCnpjParcialRem;
	}

}
