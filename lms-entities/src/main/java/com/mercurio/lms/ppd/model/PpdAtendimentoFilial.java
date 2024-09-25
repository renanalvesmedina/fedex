package com.mercurio.lms.ppd.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name = "PPD_ATENDIMENTO_FILIAIS")
@SequenceGenerator(name = "ATENDIMENTO_FILIAL_SEQ", sequenceName = "PPD_ATENDIMENTO_FILIAIS_SQ")
public class PpdAtendimentoFilial implements Serializable  {	
	private static final long serialVersionUID = 1L;
	private Long idAtendimentoFilial;
	private PpdGrupoAtendimento grupoAtendimento;
	private Filial filial;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATENDIMENTO_FILIAL_SEQ")
	@Column(name = "ID_ATENDIMENTO_FILIAL", nullable = false)
	public Long getIdAtendimentoFilial() {
		return idAtendimentoFilial;
	}

	public void setIdAtendimentoFilial(Long idAtendimentoFilial) {
		this.idAtendimentoFilial = idAtendimentoFilial;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPO_ATENDIMENTO", nullable = false)
	public PpdGrupoAtendimento getGrupoAtendimento() {
		return grupoAtendimento;
	}

	public void setGrupoAtendimento(PpdGrupoAtendimento grupoAtendimento) {
		this.grupoAtendimento = grupoAtendimento;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}	
	
	@Transient
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		bean.put("idAtendimentoFilial", this.getIdAtendimentoFilial());
		bean.putAll(this.getGrupoAtendimento().getMapped());
		bean.put("idFilial", this.getFilial().getIdFilial());		
		bean.put("nmFantasia", this.getFilial().getPessoa().getNmFantasia());
		bean.put("sgFilial", this.getFilial().getSgFilial());
		return bean;
	}
}
