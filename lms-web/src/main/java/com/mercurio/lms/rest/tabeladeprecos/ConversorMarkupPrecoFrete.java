package com.mercurio.lms.rest.tabeladeprecos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.PrecoFreteService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.ValorMarkupPrecoFrete;

public class ConversorMarkupPrecoFrete {
	
	private enum TipoChave {
		TARIFA {
			@Override
			public MarkupPrecoFreteDTO buscaMarkupPrecoFrete(final Long idTarifa, final YearMonthDay dataVigenciaInicial, final YearMonthDay dataVigenciaFinal, final boolean historico, List<MarkupPrecoFreteDTO> valores) {
				return (MarkupPrecoFreteDTO) CollectionUtils.find(valores, new Predicate() {
					@Override
					public boolean evaluate(Object object) {
						MarkupPrecoFreteDTO markupPrecoFrete = (MarkupPrecoFreteDTO) object;
						
						if(historico){
							return markupPrecoFrete.getIdTarifa().equals(idTarifa) && markupPrecoFrete.getDataVigenciaInicial().equals(dataVigenciaInicial) && markupPrecoFrete.getDataVigenciaFinal().equals(dataVigenciaFinal);
						}
						
						return markupPrecoFrete.getIdTarifa().equals(idTarifa);
					}
				});
			}
		}, 
		ROTA {
			@Override
			public MarkupPrecoFreteDTO buscaMarkupPrecoFrete(final Long idRota, final YearMonthDay dataVigenciaInicial, final YearMonthDay dataVigenciaFinal, final boolean historico, List<MarkupPrecoFreteDTO> valores) {
				return (MarkupPrecoFreteDTO) CollectionUtils.find(valores, new Predicate() {
					@Override
					public boolean evaluate(Object object) {
						MarkupPrecoFreteDTO markupPrecoFrete = (MarkupPrecoFreteDTO) object;
						
						if(historico){
							return markupPrecoFrete.getIdRota().equals(idRota) && markupPrecoFrete.getDataVigenciaInicial().equals(dataVigenciaInicial) && markupPrecoFrete.getDataVigenciaFinal().equals(dataVigenciaFinal);
						}
						
						return markupPrecoFrete.getIdRota().equals(idRota);
					}
				});
			}
		};
		
		abstract MarkupPrecoFreteDTO buscaMarkupPrecoFrete(final Long idRota, YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal, boolean historico, List<MarkupPrecoFreteDTO> valores);
	}
	
	public MarkupPrecoFreteDTO criaValoresVazios(Long idTabelaPreco, List<MarkupPrecoFreteDTO> listMarkupPrecoFreteDtoExistentes,
			ParcelaPrecoService parcelaPrecoService, PrecoFreteService precoFreteService) {
		
		List<ParcelaPreco> listParcelaPrecoDaTabela = parcelaPrecoService.findParcelaPrecoByIdTabelaPrecoParaMarkup(idTabelaPreco, "P");
		if(CollectionUtils.isEmpty(listParcelaPrecoDaTabela)){
			return null;
		}

		//se ja existe markup no banco listMarkupPrecoFreteDtoExistentes.isNotEmpty
		if(CollectionUtils.isNotEmpty(listMarkupPrecoFreteDtoExistentes)){
			for (MarkupPrecoFreteDTO markupPrecoFreteDtoExistente : listMarkupPrecoFreteDtoExistentes) {
				criaParcelaPrecoFreteDto(listParcelaPrecoDaTabela, markupPrecoFreteDtoExistente);
			}
		} else {
			// criaMarkupPrecoFreteDTO
			PrecoFrete precoFrete = precoFreteService.findByIdTabelaPrecoParaMarkup(idTabelaPreco);
			if(precoFrete == null){
				return null;
			}
			TarifaPreco tarifaPreco = precoFrete.getTarifaPreco();
			RotaPreco rotaPreco = precoFrete.getRotaPreco();
			MarkupPrecoFreteDTO markupPrecoFreteDTONovo = new MarkupPrecoFreteDTO(null, null, tarifaPreco != null ? "" : null, null, rotaPreco != null ? "" : null, null, null, new ArrayList<ParcelaPrecoFreteDTO>());
			criaParcelaPrecoFreteDto(listParcelaPrecoDaTabela, markupPrecoFreteDTONovo);
			return markupPrecoFreteDTONovo;
		}
		
		return null;
	}
	
	private void criaParcelaPrecoFreteDto(List<ParcelaPreco> listParcelaPrecoDaTabela, MarkupPrecoFreteDTO markupPrecoFreteDtoExistente) {
		
		List<ParcelaPrecoFreteDTO> listParcelasExistentes = markupPrecoFreteDtoExistente.getListParcelas();
		List<ParcelaPrecoFreteDTO> listParcelasNovas = new ArrayList<ParcelaPrecoFreteDTO>();
		
		for(ParcelaPreco parcelaPreco : listParcelaPrecoDaTabela){
			
			//nao existe parcela na tarifaOuRota?
			Long idParcelaPreco = parcelaPreco.getIdParcelaPreco();
			if(!existeParcela(idParcelaPreco, listParcelasExistentes)){
				//TODO verificar idPrecoFrete
				ParcelaPrecoFreteDTO parcelaPrecoFreteDTO = new ParcelaPrecoFreteDTO(null, null, parcelaPreco.getIdParcelaPreco(), parcelaPreco.getNmParcelaPreco().toString(), null);
				listParcelasNovas.add(parcelaPrecoFreteDTO);
			}
		}
		
		markupPrecoFreteDtoExistente.incluiParcelas(listParcelasNovas);
		
		ordenaListaParcelasPrecoFrete(markupPrecoFreteDtoExistente);
	}
	
	private void ordenaListaParcelasPrecoFrete(MarkupPrecoFreteDTO markupPrecoFreteDTO) {
		Collections.sort(markupPrecoFreteDTO.getListParcelas(), new Comparator<ParcelaPrecoFreteDTO>() {
	
			@Override
			public int compare(ParcelaPrecoFreteDTO thisObject, ParcelaPrecoFreteDTO otherObject) {
				return thisObject.getNomeParcela().toUpperCase().compareTo(otherObject.getNomeParcela().toUpperCase());
			}
			
		});
	}
	
	private boolean existeParcela(Long idParcelaPreco, List<ParcelaPrecoFreteDTO> listParcelaPrecoFreteDTO) {
		for (ParcelaPrecoFreteDTO parcelaPrecoFreteDTO : listParcelaPrecoFreteDTO) {
			if(parcelaPrecoFreteDTO.getIdParcela().equals(idParcelaPreco)){
				return true;
			}
		}
		return false;
	}
	
	public List<MarkupPrecoFreteDTO> converteTodosParaDto(List<ValorMarkupPrecoFrete> entidades, boolean historico) {
		if (CollectionUtils.isEmpty(entidades)) {
			return Collections.emptyList();
		}
		List<MarkupPrecoFreteDTO> resultado = new ArrayList<MarkupPrecoFreteDTO>();
		
		for (ValorMarkupPrecoFrete entidade : entidades) {
			TarifaPreco tarifaPreco = entidade.getTarifaPreco();
			RotaPreco rotaPreco = entidade.getRotaPreco();
			
			YearMonthDay dtInicial = entidade.getDtVigenciaInicial() != null ? entidade.getDtVigenciaInicial() : null;
			YearMonthDay dtFinal = entidade.getDtVigenciaFinal() != null ? entidade.getDtVigenciaFinal() : null;
			
			MarkupPrecoFreteDTO markupPrecoFreteDTO = buscaMarkupExistente(tarifaPreco, rotaPreco, resultado, dtInicial, dtFinal, historico);
			
			// criaParcelaPrecoFreteDTO
			ParcelaPrecoFreteDTO parcelaPrecoFreteDTO = criaParcelaPrecoFreteDTO(entidade);
			
			if(markupPrecoFreteDTO != null){
				markupPrecoFreteDTO.incluiParcela(parcelaPrecoFreteDTO);
				
			} else {
				
				// criaMarkupPrecoFreteDTO
				markupPrecoFreteDTO = new MarkupPrecoFreteDTO(entidade.getIdValorMarkupPrecoFrete(),
																tarifaPreco != null ? tarifaPreco.getIdTarifaPreco() : null, 
																tarifaPreco != null ? tarifaPreco.getCdTarifaPreco() : null, 
																rotaPreco != null ? rotaPreco.getIdRotaPreco() : null,
																rotaPreco != null ? rotaPreco.getOrigemString() + " > " + rotaPreco.getDestinoString() : null,
																dtInicial, dtFinal,
																new ArrayList<ParcelaPrecoFreteDTO>());
				markupPrecoFreteDTO.incluiParcela(parcelaPrecoFreteDTO);
				resultado.add(markupPrecoFreteDTO);
			}
			
		}
		for (MarkupPrecoFreteDTO markupPrecoFreteDTO : resultado) {
			if(markupPrecoFreteDTO.getDataVigenciaFinal().equals(JTDateTimeUtils.MAX_YEARMONTHDAY)){
				markupPrecoFreteDTO.setDataVigenciaFinal(null);
			}
		}
		return resultado;
	}
	
	private MarkupPrecoFreteDTO buscaMarkupExistente(TarifaPreco tarifaPreco, RotaPreco rotaPreco, List<MarkupPrecoFreteDTO> resultado,
													YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal, boolean historico) {
		// se por tarifa
		if(tarifaPreco != null){
			return TipoChave.TARIFA.buscaMarkupPrecoFrete(tarifaPreco.getIdTarifaPreco(), dataVigenciaInicial, dataVigenciaFinal, historico, resultado);
		}
		// se por rota
		if(rotaPreco != null){
			return TipoChave.ROTA.buscaMarkupPrecoFrete(rotaPreco.getIdRotaPreco(), dataVigenciaInicial, dataVigenciaFinal, historico, resultado);
		}
		return null;
	}

	private ParcelaPrecoFreteDTO criaParcelaPrecoFreteDTO(ValorMarkupPrecoFrete entidade) {
		Long idParcelaPreco = entidade.getIdParcelaPreco();
		Long idPrecoFrete = entidade.getIdPrecoFrete();
		BigDecimal valorMarkup = entidade.getVlMarkup();
		return new ParcelaPrecoFreteDTO(entidade.getIdValorMarkupPrecoFrete(), idPrecoFrete, idParcelaPreco, entidade.getNmParcelaPreco(), valorMarkup);
	}
	
	public List<ValorMarkupPrecoFrete> converteTodosParaEntidades(List<MarkupPrecoFreteDTO> linhas, Long idTabelaPreco, PrecoFreteService precoFreteService) {
		if (CollectionUtils.isEmpty(linhas)) {
			return Collections.emptyList();
		}
		List<ValorMarkupPrecoFrete> entidades = new ArrayList<ValorMarkupPrecoFrete>();
		for (MarkupPrecoFreteDTO markupPrecoFrete : linhas) {
			RotaPreco rotaPreco = markupPrecoFrete.getIdRota() != null ? new RotaPreco(markupPrecoFrete.getIdRota()) : null;
			TarifaPreco tarifaPreco = markupPrecoFrete.getIdTarifa() != null ? new TarifaPreco(markupPrecoFrete.getIdTarifa()) : null;
			
			for (ParcelaPrecoFreteDTO parcelaFrete: markupPrecoFrete.getListParcelas()) {
				ValorMarkupPrecoFrete entidade = new ValorMarkupPrecoFrete();
				entidade.setRotaPreco(rotaPreco);
				entidade.setTarifaPreco(tarifaPreco);
				entidade.setIdValorMarkupPrecoFrete(parcelaFrete.getIdValorMarkupPrecoFrete());
				entidade.setDtVigenciaFinal(markupPrecoFrete.getDataVigenciaFinal());
				entidade.setDtVigenciaInicial(markupPrecoFrete.getDataVigenciaInicial());
				entidade.setIdValorMarkupPrecoFrete(parcelaFrete.getIdValorMarkupPrecoFrete());
				entidade.setPrecoFrete(this.obtemPrecoFrete(idTabelaPreco, parcelaFrete, rotaPreco, tarifaPreco, precoFreteService));
				entidade.setVlMarkup(parcelaFrete.getValor());
				entidades.add(entidade);	
			}
			
		}
		return entidades;
	}
	
	private PrecoFrete obtemPrecoFrete(Long idTabelaPreco, ParcelaPrecoFreteDTO parcelaFrete, RotaPreco rotaPreco, TarifaPreco tarifaPreco, PrecoFreteService precoFreteService){

		Long idParcelaPreco = parcelaFrete.getIdParcela();
		Long idRotaPreco = rotaPreco != null ? rotaPreco.getIdRotaPreco() : null;
		Long idTarifaPreco = tarifaPreco != null ? tarifaPreco.getIdTarifaPreco() : null;
		
		return precoFreteService.findPrecoFrete(idTabelaPreco, idParcelaPreco, idRotaPreco, idTarifaPreco);
	}

}
