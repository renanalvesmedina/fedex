package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="REAJUSTE_TABELA_PRECO_PARCELA")
@SequenceGenerator(name = "REAJUSTE_TAB_PRECO_PARCELA_SQ", sequenceName = "REAJUSTE_TAB_PRECO_PARCELA_SQ")
public class ReajusteTabelaPrecoParcela implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_REAJUSTE_TAB_PRECO_PARCELA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REAJUSTE_TAB_PRECO_PARCELA_SQ")
	private Long id;

	@Column(name="ID_REAJUSTE_TABELA_PRECO")
	private Long idReajusteTabelaPreco;
	
	@Column(name="ID_TABELA_PRECO_PARCELA")
	private Long idTabelaPrecoParcela;
	
	
	
	public ReajusteTabelaPrecoParcela() { 
	}
	
	public ReajusteTabelaPrecoParcela(Long idReajuste, Long idTabelaPrecoParcela){
		this.idReajusteTabelaPreco = idReajuste;
		this.idTabelaPrecoParcela = idTabelaPrecoParcela;
	}

	public Long getIdReajusteTabelaPreco() {
		return idReajusteTabelaPreco;
	}

	public void setIdReajusteTabelaPreco(Long idReajusteTabelaPreco) {
		this.idReajusteTabelaPreco = idReajusteTabelaPreco;
	}

	public Long getIdTabelaPrecoParcela() {
		return idTabelaPrecoParcela;
	}

	public void setIdTabelaPrecoParcela(Long idTabelaPrecoParcela) {
		this.idTabelaPrecoParcela = idTabelaPrecoParcela;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
