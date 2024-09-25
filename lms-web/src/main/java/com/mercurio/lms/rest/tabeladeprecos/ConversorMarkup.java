package com.mercurio.lms.rest.tabeladeprecos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Markup;

public class ConversorMarkup {
	
	public MarkupDTO converteParaJson(Markup entidade) {
		if(entidade != null){
			char tipoParcela = this.converteTipoParcela(entidade.getTipoPrecificacaoParcela());
			YearMonthDay dataFinal = entidade.getDtVigenciaFinal().equals(JTDateTimeUtils.MAX_YEARMONTHDAY) ? null : entidade.getDtVigenciaFinal();
			return new MarkupDTO(entidade.getIdMarkup(), entidade.getIdParcelaPreco(), entidade.getNomeParcelaPreco(), tipoParcela, entidade.getVlMarkup(), entidade.getDtVigenciaInicial() , dataFinal);
		}
		return new MarkupDTO();
	}
	
	private char converteTipoParcela(String tipoPrecificacaoParcela) {
		if (StringUtils.isBlank(tipoPrecificacaoParcela)) {
			return 0;
		}
		return tipoPrecificacaoParcela.charAt(0);
	}
	
	public Markup converteParaEntidade(MarkupDTO json, Long idTabelaPreco) {
		if(json == null){
			return null;
		}
		Markup result = new Markup(json.getId());

		if(idTabelaPreco != null) {
			TabelaPreco tabelaPreco = new TabelaPreco();
			tabelaPreco.setIdTabelaPreco(idTabelaPreco);
			result.setTabelaPreco(tabelaPreco);
		}
		
		if(json.getIdParcela() != null) {
			ParcelaPreco parcela = new ParcelaPreco();
			parcela.setIdParcelaPreco(json.getIdParcela());
			result.setParcelaPreco(parcela);
		}
		
		if(json.getValorMarkup() != null) {
			result.setVlMarkup(json.getValorMarkup());
		}
		
		if(json.getDataVigenciaInicial() != null) {
			result.setDtVigenciaInicial(json.getDataVigenciaInicial());
		}else{
			result.setDtVigenciaInicial(JTDateTimeUtils.MAX_YEARMONTHDAY);
		}
		
		if(json.getDataVigenciaFinal() != null) {
			result.setDtVigenciaFinal(json.getDataVigenciaFinal());
		}else{
			result.setDtVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY);
		}
		
		return result;
	}
	
	public List<MarkupDTO> converteTodosParaJson(List<Markup> entidades) {
		if (CollectionUtils.isEmpty(entidades)) {
			return Collections.emptyList();
		}
		List<MarkupDTO> resultado = new ArrayList<MarkupDTO>();
		for (Markup markup : entidades) {
			resultado.add(this.converteParaJson(markup));
		}
		return resultado;
	}
	
	public List<Markup> converteTodosParaEntidade(List<MarkupDTO> jsons, Long idTabelaPreco) {
		if (CollectionUtils.isEmpty(jsons)) {
			return Collections.emptyList();
		}
		List<Markup> entidades = new ArrayList<Markup>();
		for (MarkupDTO markupDTO : jsons) {
			entidades.add(this.converteParaEntidade(markupDTO, idTabelaPreco));
		}
		return entidades;
	}
}
