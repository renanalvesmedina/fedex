package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class SimulacaoAnexoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idSimulacaoAnexo;
	private Long idSimulacao;
	private DateTime dhInclusao;
	private byte[] dsDocumento;
	private String dsAnexo;
	private String blobLocator;
	
	public SimulacaoAnexoDTO(){
		
	}
	
	public SimulacaoAnexoDTO(Long idSimulacaoAnexo, Long idSimulacao, DateTime dhInclusao, String dsAnexo) {
		super();
		this.idSimulacaoAnexo = idSimulacaoAnexo;
		this.idSimulacao = idSimulacao;
		this.dhInclusao = dhInclusao;
		this.dsAnexo = dsAnexo;
	}

	public SimulacaoAnexoDTO(String blobLocator){
		this.blobLocator = blobLocator;
	}

	public Long getIdSimulacaoAnexo() {
		return idSimulacaoAnexo;
	}

	public void setIdSimulacaoAnexo(Long idSimulacaoAnexo) {
		this.idSimulacaoAnexo = idSimulacaoAnexo;
	}

	public Long getIdSimulacao() {
		return idSimulacao;
	}

	public void setIdSimulacao(Long idSimulacao) {
		this.idSimulacao = idSimulacao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public byte[] getDsDocumento() {
		return dsDocumento;
	}

	public void setDsDocumento(byte[] dsDocumento) {
		this.dsDocumento = dsDocumento;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBlobLocator() {
		return blobLocator;
	}

	public void setBlobLocator(String blobLocator) {
		this.blobLocator = blobLocator;
	}
}
