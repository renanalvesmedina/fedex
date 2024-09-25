package com.mercurio.lms.expedicao.model.service;

import com.mercurio.lms.tabelaprecos.model.RestricaoRota;

public class RestricaoRotaBuilder {

	private RestricaoRota restricaoRota;
	public static final long RIBEIRAO_PRETO[] = { 1207L, 374L } ;
	public static final long PORTO_ALEGRE[] = { 2114L, 370L } ;
	
	private RestricaoRotaBuilder() {
		restricaoRota = new RestricaoRota();
	}
	
	public static RestricaoRotaBuilder novaRestricaoRotaCom() {
		return new RestricaoRotaBuilder();
	}
	
	public RestricaoRotaBuilder municipio(long municipio) {
		restricaoRota.setIdMunicipio(municipio);
		return this;
	}
	
	public RestricaoRotaBuilder filial(long filial) {
		restricaoRota.setIdFilial(filial);
		return this;
	}
	
	public RestricaoRotaBuilder municipio(long municipio_filial[]) {
		restricaoRota.setIdMunicipio(municipio_filial[0]);
		restricaoRota.setIdFilial(municipio_filial[1]);
		return this;
	}
	
	public RestricaoRota build() {
		return restricaoRota;
	}
	
}
