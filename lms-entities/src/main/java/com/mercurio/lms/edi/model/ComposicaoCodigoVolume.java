package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mercurio.adsm.framework.model.DomainValue;

public class ComposicaoCodigoVolume implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long idComposicaoCodigoVolume;
	
	private Integer ordem;
	
	private Integer tamanho;
	
	private String formato;
	
	private String complPreenchimento;
	
	private DomainValue alinhamento;
	
	private DomainValue indicadorCalculo;
	
	@JsonIgnore
	private ComposicaoLayoutEDI composicaoLayoutEDI;
	
	@JsonIgnore
	private ComposicaoLayoutEDI composicaoCampoEDI;

	public Long getIdComposicaoCodigoVolume() {
		return idComposicaoCodigoVolume;
	}

	public void setIdComposicaoCodigoVolume(Long idComposicaoCodigoVolume) {
		this.idComposicaoCodigoVolume = idComposicaoCodigoVolume;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getComplPreenchimento() {
		return complPreenchimento;
	}

	public void setComplPreenchimento(String complPreenchimento) {
		this.complPreenchimento = complPreenchimento;
	}

	public DomainValue getAlinhamento() {
		return alinhamento;
	}

	public void setAlinhamento(DomainValue alinhamento) {
		this.alinhamento = alinhamento;
	}

	public DomainValue getIndicadorCalculo() {
		return indicadorCalculo;
	}

	public void setIndicadorCalculo(DomainValue indicadorCalculo) {
		this.indicadorCalculo = indicadorCalculo;
	}

	public ComposicaoLayoutEDI getComposicaoLayoutEDI() {
		return composicaoLayoutEDI;
	}

	public void setComposicaoLayoutEDI(ComposicaoLayoutEDI composicaoLayoutEDI) {
		this.composicaoLayoutEDI = composicaoLayoutEDI;
	}

	public ComposicaoLayoutEDI getComposicaoCampoEDI() {
		return composicaoCampoEDI;
	}

	public void setComposicaoCampoEDI(ComposicaoLayoutEDI composicaoCampoEDI) {
		this.composicaoCampoEDI = composicaoCampoEDI;
	}
	
	@JsonIgnore
	public Map<String, Object> getMapped(){
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("idComposicaoCodigoVolume", this.idComposicaoCodigoVolume);
		param.put("ordem", this.ordem);
		param.put("tamanho", this.tamanho);
		param.put("formato", this.formato);
		param.put("complPreenchimento", this.complPreenchimento);		
		param.put("alinhamento", this.alinhamento.getDescription());
		
		if(this.indicadorCalculo != null){
			param.put("indicadorCalculo", this.indicadorCalculo.getValue());
		}
		
		if(this.composicaoLayoutEDI != null){
			param.put("idComposicaoLayout",
					this.composicaoLayoutEDI.getIdComposicaoLayout());
			param.put("nmLayoutEdi", this.composicaoLayoutEDI.getLayout()
					.getNmLayoutEdi());
			
		}
		
		if(this.composicaoCampoEDI != null){
			param.put("idComposicaoCampo",
					this.composicaoCampoEDI.getIdComposicaoLayout());
			
			param.put("idRegistroLayoutEdi", this.composicaoCampoEDI
					.getRegistroLayout().getIdRegistroLayoutEdi());
			param.put("identificador", this.composicaoCampoEDI
					.getRegistroLayout().getIdentificador());
			
			param.put("dsTipoLayoutDocumento", this.composicaoCampoEDI
					.getLayout().getTipoLayoutDocumento()
					.getDsTipoLayoutDocumento());
			
			param.put("nomeCampo", this.composicaoCampoEDI.getCampoLayout()
					.getNomeCampo());
		}
		
		return param;
	}
	
}
