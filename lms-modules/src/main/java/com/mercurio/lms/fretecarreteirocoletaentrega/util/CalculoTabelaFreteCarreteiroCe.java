package com.mercurio.lms.fretecarreteirocoletaentrega.util;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcValores;

public class CalculoTabelaFreteCarreteiroCe {
	public TabelaFcValores buscaTabela(List<Chave> variacoes,	List<TabelaFcValores> tabelas) {
		TabelaFcValores retorno = null;
		

		for (int i = 0; i < variacoes.size(); i++) {
			Chave parametro = variacoes.get(i);									
			for (TabelaFcValores tabelaFcValores : tabelas) {
				if(tabelaFcValores.getBlTipo()!= null && "MT".equals(tabelaFcValores.getBlTipo().getValue())){
					continue;
				}
				Chave chave = getChave(tabelaFcValores);				
				if(parametro.key().equals(chave.key())){			
					return tabelaFcValores;
				}
			}			
		}
		
		
		return retorno;
	}

	public Chave getChave(TabelaFcValores tabelaFcValores) {
		
		// Rota proprietario cliente municipio veiculo idDocumento
		Long idRota = tabelaFcValores.getRotaColetaEntrega() != null ? tabelaFcValores.getRotaColetaEntrega().getIdRotaColetaEntrega() : null;
		Long idProprietario = tabelaFcValores.getProprietario() != null ? tabelaFcValores.getProprietario().getIdProprietario() : null;
		
		Long idCliente = null;
		Long idClienteDestinatario = null;
		if(tabelaFcValores.getCliente() != null){
			if(tabelaFcValores.getBlTipo() != null && "CD".equals(tabelaFcValores.getBlTipo().getValue())){
				idClienteDestinatario = tabelaFcValores.getCliente() != null ? tabelaFcValores.getCliente().getIdCliente() : null;
			}else{
				idCliente = tabelaFcValores.getCliente() != null ? tabelaFcValores.getCliente().getIdCliente() : null;
			}
		}
		Long idMunicipio = tabelaFcValores.getMunicipio() != null ? tabelaFcValores.getMunicipio().getIdMunicipio() : null;
		Long idVeiculo = tabelaFcValores.getTipoMeioTransporte() != null ? tabelaFcValores.getTipoMeioTransporte().getIdTipoMeioTransporte() : null;

		return new Chave(idRota, idProprietario, idCliente, idClienteDestinatario, idMunicipio, idVeiculo);
	}

	public List<Chave> getEstrategia(Long[] documento) {
		
		if(documento == null){
			throw new IllegalArgumentException("A entrada nao pode ser nula");
		}
		if (documento.length != 6) {
			throw new IllegalArgumentException("A entrada deve ter 5 (para coleta) ou 6 (para entrega) variáveis");
		}
		
		//Variações possiveis de rota,proprietario,cliente,municipio,veiculo
		int variaveis = documento.length;
		Double possibilidades = Math.pow(2,variaveis); 
		
		List<Chave> variacoes = new ArrayList<Chave>();

		for (int i = possibilidades.intValue() -1  ; i >= 0; i--) {
			Long[] doc = new Long[variaveis];

			char[] v = getArrayBinaryByInt(i,variaveis);

			for (int j = 0; j < v.length; j++) {
				if (v[j] == '0') {
					doc[j] = null;
				} else {
					doc[j] = documento[j];
				}
			}
			
			variacoes.add(new Chave(doc[0], doc[1], doc[2], doc[3], doc[4],doc[5]));			
			
		}
		return variacoes;
	}

	
	private char[] getArrayBinaryByInt(int numero, int tamanho) {
		String bin = Integer.toString(numero, 2);
		while (bin.length() < tamanho) {
			bin = "0".concat(bin);
		}

		char[] v = bin.toCharArray();
		return v;
	}
}