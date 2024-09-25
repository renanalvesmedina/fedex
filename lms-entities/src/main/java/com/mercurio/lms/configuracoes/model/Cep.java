/*
 * Created on Aug 24, 2005
 *
 */
package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

/**
 * @author Robson Edemar Gehl
 *
 */
public class Cep implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nrCep;
	private String dsTipoLogradouro;
	private String dsLogComplemento;      
	private String nmLogradouro;
	private String nmBairro;
	
	private UnidadeFederativa unidadeFederativa;
	private Municipio municipio;
	private Pais pais;
	
	public String getDsTipoLogradouro() {
		return dsTipoLogradouro;
	}

	public void setDsTipoLogradouro(String dsTipoLogradouro) {
		this.dsTipoLogradouro = dsTipoLogradouro;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public String getNmBairro() {
		return nmBairro;
	}

	public void setNmBairro(String nmBairro) {
		this.nmBairro = nmBairro;
	}

	public String getNmLogradouro() {
		return nmLogradouro;
	}

	public void setNmLogradouro(String nmLogradouro) {
		this.nmLogradouro = nmLogradouro;
	}

	public String getNrCep() {
		return nrCep;
	}

	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}
	
	public String getDsLogComplemento() {
		return dsLogComplemento;
	}

	public void setDsLogComplemento(String dsLogComplemento) {
		this.dsLogComplemento = dsLogComplemento;
	}
	
	public String getCepCriteria(){
		return this.nrCep;
	}
	
}
