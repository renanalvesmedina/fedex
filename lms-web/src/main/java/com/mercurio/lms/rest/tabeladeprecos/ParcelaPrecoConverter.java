package com.mercurio.lms.rest.tabeladeprecos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;

public class ParcelaPrecoConverter {
	
	public ParcelaDTO converteParaJson(ParcelaPreco entidade) {
		if(entidade != null){
			char tipoPrecificacao = entidade.getTpPrecificacao().getValue().charAt(0);
			return new ParcelaDTO(entidade.getIdParcelaPreco(), entidade.getNmParcelaPreco().toString(), tipoPrecificacao);
		}
		return new ParcelaDTO();
	}
	
	public List<ParcelaDTO> converteTodosParaDto(List<ParcelaPreco> entidades) {
		if (CollectionUtils.isEmpty(entidades)) {
			return Collections.emptyList();
		}
		List<ParcelaDTO> resultado = new ArrayList<ParcelaDTO>();
		for (ParcelaPreco parcela : entidades) {
			resultado.add(this.converteParaJson(parcela));
		}
		return resultado;
	}
}
