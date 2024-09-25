package com.mercurio.lms.rest.tabeladeprecos;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

public class MarkupDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idParcela;
	private String nomeParcela;
	private char tipoParcela; //'M'=faixa progressiva,'P'=preço frete,'G'=generalidade,'T'=taxas ou 'S'=serviço adicional
	private YearMonthDay dataVigenciaInicial;
	private YearMonthDay dataVigenciaFinal;
	private BigDecimal valorMarkup;
	
	public MarkupDTO() {
		
	}
	
	public MarkupDTO(Long id, Long idParcela, String nomeParcela, char tipoParcela, BigDecimal valorMarkup, YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal) {
		super();
		this.id = id;
		this.idParcela = idParcela;
		this.nomeParcela = nomeParcela;
		this.tipoParcela = tipoParcela;
		this.valorMarkup = valorMarkup;
		this.dataVigenciaInicial = dataVigenciaInicial;
		this.dataVigenciaFinal = dataVigenciaFinal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIdParcela() {
		return idParcela;
	}
	
	public void setIdParcela(Long idParcela) {
		this.idParcela = idParcela;
	}
	
	public String getNomeParcela() {
		return nomeParcela;
	}
	
	public void setNomeParcela(String nomeParcela) {
		this.nomeParcela = nomeParcela;
	}
	
	public char getTipoParcela() {
		return tipoParcela;
	}
	
	public void setTipoParcela(char tipoParcela) {
		this.tipoParcela = tipoParcela;
	}
	
	public YearMonthDay getDataVigenciaInicial() {
		return dataVigenciaInicial;
	}
	
	public void setDataVigenciaInicial(YearMonthDay dataVigenciaInicial) {
		this.dataVigenciaInicial = dataVigenciaInicial;
	}
	
	public YearMonthDay getDataVigenciaFinal() {
		return dataVigenciaFinal;
	}
	
	public void setDataVigenciaFinal(YearMonthDay dataVigenciaFinal) {
		this.dataVigenciaFinal = dataVigenciaFinal;
	}
	
	public BigDecimal getValorMarkup() {
		return valorMarkup;
	}
	
	public void setValorMarkup(BigDecimal valorMarkup) {
		this.valorMarkup = valorMarkup;
	}
}
