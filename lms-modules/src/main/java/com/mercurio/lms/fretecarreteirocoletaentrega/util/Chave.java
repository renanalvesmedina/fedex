package com.mercurio.lms.fretecarreteirocoletaentrega.util;


public class Chave {
	private Long idRota, idProprietario, idCliente, idMunicipio, idVeiculo,idClienteDestinatario;	
	
	
	public Chave(Long idRota, Long idProprietario, Long idCliente, Long idClienteDestinatario, Long idMunicipio, Long idVeiculo) {
		super();
		this.idRota = idRota;
		this.idProprietario = idProprietario;
		this.idCliente = idCliente;
		this.idClienteDestinatario = idClienteDestinatario;
		this.idMunicipio = idMunicipio;
		this.idVeiculo = idVeiculo;
	}



	public String key(){
		return String.format("c-%d-cd-%d-m-%d-p-%d-r-%d-v-%d",getValue(idCliente),getValue(idClienteDestinatario),getValue(idMunicipio),getValue(idProprietario),getValue(idRota),getValue(idVeiculo)); 
	}
	
	@Override
	public String toString() {
		return key();
	}


	/**
	 * @return
	 */
	private long getValue(Long atributo) {
		return atributo == null ? 0 : atributo;
	}
	
	
}