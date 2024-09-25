package com.mercurio.lms.edi.model;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.DomainValue;

public class RegistroLayoutEDI implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long idRegistroLayoutEdi;
	
	private String identificador;
	
	private DomainValue preenchimento;
	
	private Integer tamanhoRegistro;
	
	private String descricao;
	
	private Integer ordem;
	
	private Integer ocorrencias;
	
	private Long idRegistroPai;
	
	private String nomeIdentificador;
	
	public Long getIdRegistroLayoutEdi() {
		return idRegistroLayoutEdi;
	}

	public void setIdRegistroLayoutEdi(Long idRegistroLayoutEdi) {
		this.idRegistroLayoutEdi = idRegistroLayoutEdi;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public DomainValue getPreenchimento() {
		return preenchimento;
	}

	public void setPreenchimento(DomainValue preenchimento) {
		this.preenchimento = preenchimento;
	}

	public Integer getTamanhoRegistro() {
		return tamanhoRegistro;
	}

	public void setTamanhoRegistro(Integer tamanhoRegistro) {
		this.tamanhoRegistro = tamanhoRegistro;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Integer getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(Integer ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	public Long getIdRegistroPai() {
		return idRegistroPai;
	}

	public void setIdRegistroPai(Long idRegistroPai) {
		this.idRegistroPai = idRegistroPai;
	}

	public String getNomeIdentificador() {
		return nomeIdentificador;
	}

	public void setNomeIdentificador(String nomeIdentificador) {
		this.nomeIdentificador = nomeIdentificador;
	}
}
