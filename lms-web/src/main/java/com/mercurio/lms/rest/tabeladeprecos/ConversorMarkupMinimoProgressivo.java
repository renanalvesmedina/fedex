package com.mercurio.lms.rest.tabeladeprecos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.service.FaixaProgressivaService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.ValorFaixaProgressivaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.MarkupFaixaProgressiva;
import com.mercurio.lms.vendas.model.ValorMarkupFaixaProgressiva;

public class ConversorMarkupMinimoProgressivo {
	
	private static final String VALOR = "V";
	private static final String PRODUTO = "P";
	
	private enum TipoChave {
		TARIFA {
			@Override
			public MarkupMinimoProgressivoDTO buscaMarkupMinimoProgressivo(final Long idTarifa, final YearMonthDay dataVigenciaInicial, final YearMonthDay dataVigenciaFinal, final boolean historico, List<MarkupMinimoProgressivoDTO> valores) {
				return (MarkupMinimoProgressivoDTO) CollectionUtils.find(valores, new Predicate() {
					@Override
					public boolean evaluate(Object object) {
						MarkupMinimoProgressivoDTO markup = (MarkupMinimoProgressivoDTO) object;
						
						if(historico){
							return markup.getIdTarifa().equals(idTarifa) && markup.getDataVigenciaInicial().equals(dataVigenciaInicial) && markup.getDataVigenciaFinal().equals(dataVigenciaFinal);
						}
						
						return markup.getIdTarifa().equals(idTarifa);
					}
				});
			}
		},
		ROTA {
			@Override
			public MarkupMinimoProgressivoDTO buscaMarkupMinimoProgressivo(final Long idRota, final YearMonthDay dataVigenciaInicial, final YearMonthDay dataVigenciaFinal, final boolean historico, List<MarkupMinimoProgressivoDTO> valores) {
				return (MarkupMinimoProgressivoDTO) CollectionUtils.find(valores, new Predicate() {
					@Override
					public boolean evaluate(Object object) {
						MarkupMinimoProgressivoDTO markup = (MarkupMinimoProgressivoDTO) object;
						
						if(historico){
							return markup.getIdRota().equals(idRota) && markup.getDataVigenciaInicial().equals(dataVigenciaInicial) && markup.getDataVigenciaFinal().equals(dataVigenciaFinal);
						}
						
						return markup.getIdRota().equals(idRota);
					}
				});
			}
		};
		
		abstract MarkupMinimoProgressivoDTO buscaMarkupMinimoProgressivo(Long idChave, YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal, boolean historico, List<MarkupMinimoProgressivoDTO> valores);
	}
	
	public MarkupMinimoProgressivoDTO criaValoresVazios(Long idTabelaPreco, List<MarkupMinimoProgressivoDTO> listMarkupMinimoProgDtoExistentes, 
				ParcelaPrecoService parcelaPrecoService, FaixaProgressivaService faixaProgressivaService, ValorFaixaProgressivaService valorFaixaProgressivaService){
		
		List<ParcelaPreco> listParcelaPrecoDaTabela = parcelaPrecoService.findParcelaPrecoByIdTabelaPrecoParaMarkup(idTabelaPreco, "M");
		if(CollectionUtils.isEmpty(listParcelaPrecoDaTabela)){
			return null;
		}
		
		
		//se ja existe markup no banco listMarkupMinimoProgDtoExistentes.isNotEmpty
		if(CollectionUtils.isNotEmpty(listMarkupMinimoProgDtoExistentes)){
			for (MarkupMinimoProgressivoDTO markupMinProgDtoExistente : listMarkupMinimoProgDtoExistentes) {
				criaParcelaMinProgDto(listParcelaPrecoDaTabela, markupMinProgDtoExistente, idTabelaPreco, faixaProgressivaService);
			}
		} else {
			// criaMarkupMinimoProgressivoDTO
			ValorFaixaProgressiva valorFaixaProgressiva = valorFaixaProgressivaService.findByIdTabelaPrecoParaMarkup(idTabelaPreco);
			TarifaPreco tarifaPreco = valorFaixaProgressiva.getTarifaPreco();
			RotaPreco rotaPreco = valorFaixaProgressiva.getRotaPreco();
			MarkupMinimoProgressivoDTO markupMinimoProgressivoDTONovo = new MarkupMinimoProgressivoDTO(null, 
																								   tarifaPreco != null ? "" : null, 
																								   null,
																								   rotaPreco != null ? "" : null,
																									null, null,	   
																								   new ArrayList<ParcelaMinimoProgressivoDTO>());
			criaParcelaMinProgDto(listParcelaPrecoDaTabela, markupMinimoProgressivoDTONovo, idTabelaPreco, faixaProgressivaService);
			return markupMinimoProgressivoDTONovo;
		}
		
		return null;
	}
	
	private void criaParcelaMinProgDto(List<ParcelaPreco> listParcelaPrecoDaTabela, MarkupMinimoProgressivoDTO markupMinProgDtoExistente, Long idTabelaPreco, FaixaProgressivaService faixaProgressivaService ) {
		
		List<ParcelaMinimoProgressivoDTO> listParcelasExistentes = markupMinProgDtoExistente.getListParcelas();
		List<ParcelaMinimoProgressivoDTO> listParcelasNovas = new ArrayList<ParcelaMinimoProgressivoDTO>();
		
		for(ParcelaPreco parcelaPreco : listParcelaPrecoDaTabela){
			
			//existe parcela na tarifaOuRota?
			Long idParcelaPreco = parcelaPreco.getIdParcelaPreco();
			if(existeParcela(idParcelaPreco, listParcelasExistentes)){
				
				ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO = buscaParcela(idParcelaPreco, listParcelasExistentes);
				
				criaValorFaixaProgressiva(idTabelaPreco, faixaProgressivaService, parcelaPreco, parcelaMinimoProgressivoDTO);
				
			} else { // NAO existe parcela na tarifaOuRota
				
				// criaParcelaMinimoProgressivoDTO
				ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO = criaParcelaMinimoProgressivoDTOVazio(parcelaPreco);
				criaValorFaixaProgressiva(idTabelaPreco, faixaProgressivaService, parcelaPreco, parcelaMinimoProgressivoDTO);
				listParcelasNovas.add(parcelaMinimoProgressivoDTO);
			}
			
		}
		
		markupMinProgDtoExistente.incluiParcelas(listParcelasNovas);
		
		ordenaListaParcelasMinimoProgressivo(markupMinProgDtoExistente);
		
	}

	private void ordenaListaParcelasMinimoProgressivo(MarkupMinimoProgressivoDTO markupMinProgDto) {
		Collections.sort(markupMinProgDto.getListParcelas(), new Comparator<ParcelaMinimoProgressivoDTO>() {

			@Override
			public int compare(ParcelaMinimoProgressivoDTO thisObject, ParcelaMinimoProgressivoDTO otherObject) {
				return thisObject.getNomeParcela().toUpperCase().compareTo(otherObject.getNomeParcela().toUpperCase());
			}
			
		});
	}

	private void criaValorFaixaProgressiva(Long idTabelaPreco, FaixaProgressivaService faixaProgressivaService, ParcelaPreco parcelaPreco, ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO) {
		//verificar se ja existe faixa na parcela, se nao crai
		List<FaixaProgressiva> listFaixaProgressivaDaParcelaETabela = faixaProgressivaService.findFaixaProgressivaParaMarkup(parcelaPreco.getIdParcelaPreco(), idTabelaPreco);
		
		for (FaixaProgressiva faixaProgressiva : listFaixaProgressivaDaParcelaETabela) {
			if(!existeFaixaNaParcela(faixaProgressiva, parcelaMinimoProgressivoDTO)){
				// criar valorfaixa vazio
				MarkupValorFaixaProgressivaDTO valorFaixaProgressivaDTO = criaValorFaixaProgressivaDTOVazio(faixaProgressiva);
				parcelaMinimoProgressivoDTO.incluiValorFaixaProgressiva(valorFaixaProgressivaDTO);
			}
		}
		
		ordenaListaValoresFaixaProgressiva(parcelaMinimoProgressivoDTO);
		
	}

	private void ordenaListaValoresFaixaProgressiva(ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO) {
		Collections.sort(parcelaMinimoProgressivoDTO.getListValoresFaixaProgressiva(), new Comparator<MarkupValorFaixaProgressivaDTO>() {

			@Override
			public int compare(MarkupValorFaixaProgressivaDTO thisObject, MarkupValorFaixaProgressivaDTO otherObject) {
				
				int result = thisObject.getFaixa().substring(0, 1).compareTo(otherObject.getFaixa().substring(0, 1)) * -1;
				if (result == 0){
					String thisValue = thisObject.getFaixa().substring(1, thisObject.getFaixa().length());
					String otherValue = otherObject.getFaixa().substring(1, otherObject.getFaixa().length());
				
					result = new BigDecimal(thisValue).compareTo(new BigDecimal(otherValue));
				}
				return result;
			}
			
		});
	}
	
	private ParcelaMinimoProgressivoDTO criaParcelaMinimoProgressivoDTOVazio(ParcelaPreco parcelaPreco) {
		return new ParcelaMinimoProgressivoDTO(null, parcelaPreco.getIdParcelaPreco(), parcelaPreco.getNmParcelaPreco().toString());
	}
	
	private boolean existeFaixaNaParcela(FaixaProgressiva faixaProgressiva, ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO) {
		if(parcelaMinimoProgressivoDTO.getListValoresFaixaProgressiva() != null &&CollectionUtils.isNotEmpty(parcelaMinimoProgressivoDTO.getListValoresFaixaProgressiva())){
			for(MarkupValorFaixaProgressivaDTO valorFaixaProgressivaDTO : parcelaMinimoProgressivoDTO.getListValoresFaixaProgressiva()){
				String faixa = VALOR;
				faixa += faixaProgressiva.getVlFaixaProgressiva();
				if(faixaProgressiva.getProdutoEspecifico() != null){
					faixa = PRODUTO;
					faixa += faixaProgressiva.getProdutoEspecifico().getNrTarifaEspecifica();
				}
				if(faixa.equals(valorFaixaProgressivaDTO.getFaixa())){
					return true;
				}
			}
		}
		return false;
	}

	public List<MarkupMinimoProgressivoDTO> converteTodosParaDto(List<ValorMarkupFaixaProgressiva> entidades, boolean historico) {
		if (CollectionUtils.isEmpty(entidades)) {
			return Collections.emptyList();
		}
		List<MarkupMinimoProgressivoDTO> resultado = new ArrayList<MarkupMinimoProgressivoDTO>();
		
		for (ValorMarkupFaixaProgressiva entidade : entidades) {
			
			TarifaPreco tarifaPreco = entidade.getTarifaPreco();
			RotaPreco rotaPreco = entidade.getRotaPreco();
			
			YearMonthDay dtInicial = entidade.getDtVigenciaInicial() != null ? entidade.getDtVigenciaInicial() : null;
			YearMonthDay dtFinal = entidade.getDtVigenciaFinal() != null ? entidade.getDtVigenciaFinal() : null;

			MarkupMinimoProgressivoDTO markupMinimoProgressivoDTO = this.buscaMarkupExistente(resultado, tarifaPreco, rotaPreco, dtInicial, dtFinal, historico);
			
			// criaValorFaixaProgressivaDTO
			MarkupValorFaixaProgressivaDTO valorFaixaProgressivaDTO = criaValorFaixaProgressivaDTO(entidade);
			
			if(markupMinimoProgressivoDTO != null){
				//existe parcela na tarifaOuRota?
				Long idParcelaPreco = entidade.getIdParcelaPreco();
				if(existeParcela(idParcelaPreco, markupMinimoProgressivoDTO.getListParcelas())){
					
					ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO = buscaParcela(idParcelaPreco, markupMinimoProgressivoDTO.getListParcelas());
					parcelaMinimoProgressivoDTO.incluiValorFaixaProgressiva(valorFaixaProgressivaDTO);
				
				} else { // NAO existe parcela na tarifaOuRota
					
					// criaParcelaMinimoProgressivoDTO
					ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO = criaParcelaMinimoProgressivoDTO(entidade);
					parcelaMinimoProgressivoDTO.incluiValorFaixaProgressiva(valorFaixaProgressivaDTO);
					markupMinimoProgressivoDTO.incluiParcela(parcelaMinimoProgressivoDTO);
				}
				
			} else {
				
				// criaParcelaMinimoProgressivoDTO
				ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO = criaParcelaMinimoProgressivoDTO(entidade);
				parcelaMinimoProgressivoDTO.incluiValorFaixaProgressiva(valorFaixaProgressivaDTO);
				
				// criaMarkupMinimoProgressivoDTO
				markupMinimoProgressivoDTO = new MarkupMinimoProgressivoDTO(tarifaPreco != null ? tarifaPreco.getIdTarifaPreco() : null, 
																			tarifaPreco != null ? tarifaPreco.getCdTarifaPreco() : null, 
																			rotaPreco != null ? rotaPreco.getIdRotaPreco() : null,
																			rotaPreco != null ? rotaPreco.getOrigemString() +" > " + rotaPreco.getDestinoString() : null,
																			dtInicial, dtFinal,
																			new ArrayList<ParcelaMinimoProgressivoDTO>());
				markupMinimoProgressivoDTO.getListParcelas().add(parcelaMinimoProgressivoDTO);
				resultado.add(markupMinimoProgressivoDTO);
			}
			
		}
		for (MarkupMinimoProgressivoDTO markupMinimoProgressivoDTO : resultado) {
			if(markupMinimoProgressivoDTO.getDataVigenciaFinal().equals(JTDateTimeUtils.MAX_YEARMONTHDAY)){
				markupMinimoProgressivoDTO.setDataVigenciaFinal(null);
			}
		}
		return resultado;
	}
	
	private MarkupMinimoProgressivoDTO buscaMarkupExistente(List<MarkupMinimoProgressivoDTO> resultado, TarifaPreco tarifaPreco, RotaPreco rotaPreco,
															YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal, boolean historico) {
		
		// se por tarifa
		if(tarifaPreco != null){
			return TipoChave.TARIFA.buscaMarkupMinimoProgressivo(tarifaPreco.getIdTarifaPreco(), dataVigenciaInicial, dataVigenciaFinal, historico, resultado);
		}
		// se por rota
		if(rotaPreco != null) {
			return TipoChave.ROTA.buscaMarkupMinimoProgressivo(rotaPreco.getIdRotaPreco(), dataVigenciaInicial, dataVigenciaFinal, historico, resultado);
		}
		
		return null;
	}
	
	private MarkupValorFaixaProgressivaDTO criaValorFaixaProgressivaDTOVazio(FaixaProgressiva faixaProgressiva) {
		
		String faixa = VALOR;
		faixa += faixaProgressiva.getVlFaixaProgressiva();
		if(faixaProgressiva.getProdutoEspecifico() != null){
			faixa = PRODUTO;
			faixa += faixaProgressiva.getProdutoEspecifico().getNrTarifaEspecifica();
		}
		
		return new MarkupValorFaixaProgressivaDTO(null, faixaProgressiva.getIdFaixaProgressiva(), faixa, null);
	}
	
	private MarkupValorFaixaProgressivaDTO criaValorFaixaProgressivaDTO(ValorMarkupFaixaProgressiva entidade) {
		
		String faixa = VALOR;
		faixa += entidade.getVlFaixaProgressiva();
		if(entidade.getFaixaProgressiva() != null && entidade.getFaixaProgressiva().getProdutoEspecifico() != null){
			faixa = PRODUTO;
			faixa += entidade.getFaixaProgressiva().getProdutoEspecifico().getNrTarifaEspecifica();
		}
		
		BigDecimal valorMarkup = entidade.getVlMarkup();
		
		return new MarkupValorFaixaProgressivaDTO(entidade.getIdValorMarkupFaixaProgressiva(), entidade.getIdFaixaProgressiva(), faixa, valorMarkup);
	}
	
	private ParcelaMinimoProgressivoDTO criaParcelaMinimoProgressivoDTO(ValorMarkupFaixaProgressiva entidade) {
		Long idParcelaPreco = entidade.getIdParcelaPreco();
		return new ParcelaMinimoProgressivoDTO(entidade.getIdMarkupFaixaProgressiva(), idParcelaPreco, entidade.getNmParcelaPreco());
	}
	
	private boolean existeParcela(Long idParcelaPreco, List<ParcelaMinimoProgressivoDTO> listParcelaMinimoProgressivoDTO) {
		for (ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO : listParcelaMinimoProgressivoDTO) {
			if(parcelaMinimoProgressivoDTO.getIdParcela().equals(idParcelaPreco)){
				return true;
			}
		}
		return false;
	}

	private ParcelaMinimoProgressivoDTO buscaParcela(Long idParcelaPreco, List<ParcelaMinimoProgressivoDTO> listParcelaMinimoProgressivoDTO) {
		for (ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO : listParcelaMinimoProgressivoDTO) {
			if(parcelaMinimoProgressivoDTO.getIdParcela().equals(idParcelaPreco)){
				return parcelaMinimoProgressivoDTO;
			}
		}
		return null;
	}

	public List<MarkupFaixaProgressiva> converteTodosParaEntidade(List<MarkupMinimoProgressivoDTO> linhas, FaixaProgressivaService faixaProgressivaService) {
		if (CollectionUtils.isEmpty(linhas)) {
			return Collections.emptyList();
		}
		List<MarkupFaixaProgressiva> entidades = new ArrayList<MarkupFaixaProgressiva>();
		List<FaixaProgressiva> faixas = this.obtemFaixasProgressiva(linhas, faixaProgressivaService);
		
		for (MarkupMinimoProgressivoDTO markupMinimoProgressivoDTO : linhas) {
			RotaPreco rota = markupMinimoProgressivoDTO.getIdRota() != null ? new RotaPreco(markupMinimoProgressivoDTO.getIdRota()) : null;
			TarifaPreco tarifa = markupMinimoProgressivoDTO.getIdTarifa() != null ? new TarifaPreco(markupMinimoProgressivoDTO.getIdTarifa()) : null;
			YearMonthDay dataVigenciaInicial = markupMinimoProgressivoDTO.getDataVigenciaInicial();
			YearMonthDay dataVigenciaFinal = markupMinimoProgressivoDTO.getDataVigenciaFinal();
			
			
			for (ParcelaMinimoProgressivoDTO parcela: markupMinimoProgressivoDTO.getListParcelas()) {

				for(MarkupValorFaixaProgressivaDTO valor: parcela.getListValoresFaixaProgressiva()) {
					//verificar se markupfaixa já existe para a faixa do valorDTO, se não existir, cria
					MarkupFaixaProgressiva entidadeFaixa = buscaMarkupFaixa(entidades, rota, tarifa, dataVigenciaInicial, dataVigenciaFinal, parcela);
					
					//cria a entidade de valor
					ValorMarkupFaixaProgressiva entidadeValor = new ValorMarkupFaixaProgressiva();
					entidadeValor.setIdValorMarkupFaixaProgressiva(valor.getIdValorMarkupFaixaProgressiva());
					entidadeValor.setFaixaProgressiva(buscaFaixaProgressiva(faixas, valor.getIdFaixaProgressiva()));
					entidadeValor.setMarkupFaixaProgressiva(entidadeFaixa);
					entidadeValor.setVlMarkup(valor.getValor());
					entidadeFaixa.incluiValor(entidadeValor);
				}
				
			}
		}
		return entidades;
	}


	private List<FaixaProgressiva> obtemFaixasProgressiva(List<MarkupMinimoProgressivoDTO> linhas, FaixaProgressivaService faixaProgressivaService) {
		List<Long> idsfaixas = new ArrayList<Long>();
		List<FaixaProgressiva> faixas = new ArrayList<FaixaProgressiva>();
		
		for (MarkupMinimoProgressivoDTO markupMinimoProgressivoDTO : linhas) {
			for (ParcelaMinimoProgressivoDTO parcela: markupMinimoProgressivoDTO.getListParcelas()) {
				for(MarkupValorFaixaProgressivaDTO valor: parcela.getListValoresFaixaProgressiva()) {
					idsfaixas.add(valor.getIdFaixaProgressiva());
				}
			}
		}
		if(CollectionUtils.isNotEmpty(idsfaixas)){
			faixas = faixaProgressivaService.findByIds(idsfaixas);
		}
		return faixas;
	}

	private MarkupFaixaProgressiva buscaMarkupFaixa(List<MarkupFaixaProgressiva> entidades, RotaPreco rota, TarifaPreco tarifa,	YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal, ParcelaMinimoProgressivoDTO parcela) {
		
		for (MarkupFaixaProgressiva markup : entidades) {
			if (rota != null && rota.equals(markup.getRotaPreco()) &&
					dataVigenciaInicial != null && dataVigenciaInicial.isEqual(markup.getDtVigenciaInicial()) &&
						(dataVigenciaFinal != null && dataVigenciaFinal.isEqual(markup.getDtVigenciaFinal()) ||
						dataVigenciaFinal == null && markup.getDtVigenciaFinal() == null)
						) {
				return markup;
			}
			if (tarifa != null && tarifa.equals(markup.getTarifaPreco()) &&
					dataVigenciaInicial != null && dataVigenciaInicial.isEqual(markup.getDtVigenciaInicial()) &&
						(dataVigenciaFinal != null && dataVigenciaFinal.isEqual(markup.getDtVigenciaFinal()) ||
						dataVigenciaFinal == null && markup.getDtVigenciaFinal() == null)
						) {
				return markup;
			}
		}
		
		MarkupFaixaProgressiva entidadeFaixa = new MarkupFaixaProgressiva();
		entidadeFaixa.setRotaPreco(rota);
		entidadeFaixa.setTarifaPreco(tarifa);
		entidadeFaixa.setDtVigenciaInicial(dataVigenciaInicial);
		entidadeFaixa.setDtVigenciaFinal(dataVigenciaFinal);
		entidadeFaixa.setIdMarkupFaixaProgressiva(parcela.getIdMarkupFaixaProgressiva());
		entidades.add(entidadeFaixa);
		return entidadeFaixa;
	}
	
	private FaixaProgressiva buscaFaixaProgressiva(List<FaixaProgressiva> faixas, final Long idFaixaProgressiva) {
		if (CollectionUtils.isEmpty(faixas)) {
			FaixaProgressiva faixa = new FaixaProgressiva(idFaixaProgressiva);
			faixas.add(faixa);
			return faixa;
		}
		
		FaixaProgressiva faixa = (FaixaProgressiva) CollectionUtils.find(faixas, new Predicate() {
			@Override public boolean evaluate(Object object) {
				return ((FaixaProgressiva) object).getIdFaixaProgressiva().equals(idFaixaProgressiva);
			}
		});
		
		if (faixa == null) {
			faixa = new FaixaProgressiva(idFaixaProgressiva);
			faixas.add(faixa);
		}
		
		return faixa;
	}

}
