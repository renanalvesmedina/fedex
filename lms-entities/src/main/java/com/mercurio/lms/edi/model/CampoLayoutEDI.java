package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;

public class CampoLayoutEDI implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idCampo;
	
	private String nomeCampo;
	
	private String descricaoCampo;

	private String campoTabela;
	
	private String nmComplemento;
	
	private DomainValue dmTipoDePara;
	
	private DomainValue dmObrigatorio;
		
	public Long getIdCampo() {
		return idCampo;
	}

	public void setIdCampo(Long idCampo) {
		this.idCampo = idCampo;
	}

	public String getNomeCampo() {
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	public String getDescricaoCampo() {
		return descricaoCampo;
	}

	public void setDescricaoCampo(String descricaoCampo) {
		this.descricaoCampo = descricaoCampo;
	}
	
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		
		/*Id Campo Layout*/
		bean.put("idCampo", this.getIdCampo());
		/*Campo*/
		bean.put("nomeCampo", this.getNomeCampo());
		/*Descrição*/
		bean.put("descricaoCampo", this.getDescricaoCampo());
		/*Campo Tabela*/
		bean.put("campoTabela", this.getCampoTabela());
		/*Nome complemento*/
		bean.put("nmComplemento", this.getNmComplemento());
		/*Obrigatório*/
		if(this.getDmObrigatorio() != null){
			bean.put("dmObrigatorio", this.getDmObrigatorio().getValue());
		}
		/*Tipo de Para*/
		if(this.getDmTipoDePara() != null){
			bean.put("dmTipoDePara", this.getDmTipoDePara().getValue());
		}
		return bean;
	}

	public String getCampoTabela() {
		return campoTabela;
	}

	public void setCampoTabela(String campoTabela) {
		this.campoTabela = campoTabela;
	}

	public String getNmComplemento() {
		return nmComplemento;
	}

	public void setNmComplemento(String nmComplemento) {
		this.nmComplemento = nmComplemento;
	}

	public DomainValue getDmTipoDePara() {
		return dmTipoDePara;
	}

	public void setDmTipoDePara(DomainValue dmTipoDePara) {
		this.dmTipoDePara = dmTipoDePara;
	}

	public DomainValue getDmObrigatorio() {
		return dmObrigatorio;
	}

	public void setDmObrigatorio(DomainValue dmObrigatorio) {
		this.dmObrigatorio = dmObrigatorio;
	}	
	
}
