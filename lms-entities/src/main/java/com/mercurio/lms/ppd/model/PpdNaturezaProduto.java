package com.mercurio.lms.ppd.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PPD_NATUREZA_PRODUTOS")
@SequenceGenerator(name = "NATUREZA_PRODUTO_SEQ", sequenceName = "PPD_NATUREZA_PRODUTOS_SQ")
public class PpdNaturezaProduto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long idNaturezaProduto;
	private String dsNaturezaProduto;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NATUREZA_PRODUTO_SEQ")
	@Column(name = "ID_NATUREZA_PRODUTO", nullable = false)
	public Long getIdNaturezaProduto() {
		return idNaturezaProduto;
	}

	public void setIdNaturezaProduto(Long idNaturezaProduto) {
		this.idNaturezaProduto = idNaturezaProduto;
	}

	@Column(name = "DS_NATUREZA_PRODUTO", nullable = false, length = 60)
	public String getDsNaturezaProduto() {
		return dsNaturezaProduto;
	}

	public void setDsNaturezaProduto(String dsNaturezaProduto) {
		this.dsNaturezaProduto = dsNaturezaProduto;
	}
	
	@Transient
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		bean.put("idNaturezaProduto", this.getIdNaturezaProduto());
		bean.put("dsNaturezaProduto", this.getDsNaturezaProduto());
		return bean;
	}
}
