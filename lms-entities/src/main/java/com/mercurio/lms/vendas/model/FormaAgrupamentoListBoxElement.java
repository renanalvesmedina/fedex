package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.Map;
import java.util.StringTokenizer;

import com.mercurio.adsm.framework.util.TypedFlatMap;

public class FormaAgrupamentoListBoxElement implements Serializable {

	private static final long serialVersionUID = 1L;

    private Long id;

    private String valor;

    private String descricao;

    private String tipo;
    
    private Byte nrOrdemPrioridade;
    
    private Long idDominioAgrupamento;
    
    private Long idCliente;   
    
	public FormaAgrupamentoListBoxElement(Long id, String valor,
			String descricao, String tipo) {
    	this.id = id;
    	this.valor = valor;
    	this.descricao = descricao;
    	this.tipo = tipo;
    }
 
	public FormaAgrupamentoListBoxElement(String id, String valor,
			String descricao, String tipo) {
    	this(Long.valueOf(id),valor,descricao,tipo);
    }

    public FormaAgrupamentoListBoxElement(Map map) {
    	
    	TypedFlatMap tfm = (TypedFlatMap) map;
    	
    	setNrOrdemPrioridade(Byte.valueOf(tfm.getString("nrOrdemPrioridade")));
    	
    	if( tfm.getLong("formaAgrupamentoListBoxElement.idDominioAgrupamento") != null){
			setIdDominioAgrupamento(tfm
					.getLong("formaAgrupamentoListBoxElement.idDominioAgrupamento"));
    	}
    	
		setIdComposto(tfm
				.getString("formaAgrupamentoListBoxElement.idComposto"));
    }

    public FormaAgrupamentoListBoxElement() {
    }

    /**
	 * @param id
	 *            The id to set.
	 */
    public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the id.
	 */
    public Long getId() {
		return id;
	}
   
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String toString(){
		return new StringBuffer().append("[id: ").append(id).append(";")
				.append("valor: ").append(valor).append(";")
				.append("descricao: ").append(descricao).append(";")
				.append("tipo: ").append(tipo).append("]").toString();
		
	}

	/**
	 * @param idComposto
	 *            The idComposto to set.
	 */
	public void setIdComposto(String idComposto) {
		StringTokenizer st = new StringTokenizer(idComposto,";");
		setId(Long.valueOf(st.nextToken()));
		setValor(st.nextToken());
		setDescricao(st.nextToken());
		setTipo(st.nextToken());
	}
	
	public Byte getNrOrdemPrioridade() {
		return nrOrdemPrioridade;
	}

	public void setNrOrdemPrioridade(Byte nrOrdemPrioridade) {
		this.nrOrdemPrioridade = nrOrdemPrioridade;
	}
	
	public Long getIdDominioAgrupamento() {
		return idDominioAgrupamento;
	}

	public void setIdDominioAgrupamento(Long idDominioAgrupamento) {
		this.idDominioAgrupamento = idDominioAgrupamento;
	}
	
	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * @return Returns the idComposto.
	 */
	public String getIdComposto() {
		return new StringBuffer().append(getId()).append(";")
				.append(getValor()).append(";").append(getDescricao())
				.append(";").append(getTipo()).toString();
	}	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getIdComposto().hashCode();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof FormaAgrupamentoListBoxElement){
			FormaAgrupamentoListBoxElement falb = (FormaAgrupamentoListBoxElement)obj;
			if (getIdComposto() != null
					&& getIdComposto().equals(falb.getIdComposto())) {
				return true;
			}
		}
		return false;
	}	
	
}
