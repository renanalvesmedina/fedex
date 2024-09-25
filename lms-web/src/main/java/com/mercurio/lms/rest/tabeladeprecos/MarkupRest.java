package com.mercurio.lms.rest.tabeladeprecos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.service.FaixaProgressivaService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.PrecoFreteService;
import com.mercurio.lms.tabelaprecos.model.service.RotaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TarifaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.ValorFaixaProgressivaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Markup;
import com.mercurio.lms.vendas.model.MarkupFaixaProgressiva;
import com.mercurio.lms.vendas.model.ValorMarkupPrecoFrete;
import com.mercurio.lms.vendas.model.service.MarkupService;

@Path("/tabeladeprecos/markup")
public class MarkupRest {
	
	private static final String ROTA = "Rota ";
	private static final String PARCELA = "Parcela ";
	private static final String TABELA = "Tabela";
	private static final String GENERICA = "Generica";
	private static final String TIPO_MINIMO_PROGRESSIVO = "minimoProgressivo";
	private static final Map<String, List<String>> TIPOS_PRECIFICACAO = new HashMap<String, List<String>>();
	{
		TIPOS_PRECIFICACAO.put(TABELA, Arrays.asList("M","P"));
		TIPOS_PRECIFICACAO.put(GENERICA, Arrays.asList("G","T","S"));
	}
	
	@InjectInJersey TabelaPrecoService tabelaPrecoService;
	@InjectInJersey MarkupService markupService;
	@InjectInJersey ParcelaPrecoService parcelaPrecoService;
	@InjectInJersey FaixaProgressivaService faixaProgressivaService;
	@InjectInJersey ValorFaixaProgressivaService valorFaixaProgressivaService;
	@InjectInJersey PrecoFreteService precoFreteService;
	@InjectInJersey TarifaPrecoService tarifaPrecoService;
	@InjectInJersey RotaPrecoService rotaPrecoService;
	
	private ConversorMarkup conversorMarkup = new ConversorMarkup();
	private ConversorMarkupMinimoProgressivo conversorMarkupMinimoProgressivo = new ConversorMarkupMinimoProgressivo();
	private ConversorMarkupPrecoFrete conversorMarkupPrecoFrete = new ConversorMarkupPrecoFrete();
	private ParcelaPrecoConverter parcelaPrecoConverter = new ParcelaPrecoConverter();
	
	@GET
	@Path("/findtabelapreco")
	public Response findTabelaPrecoByIdTabelaPreco(@QueryParam("idTabela") Long id) {
		TabelaPreco tabelaPreco = tabelaPrecoService.findByIdTabelaPreco(id);
		TabelaPrecoSuggestDTO tabelaPrecoSuggestDTO = new TabelaPrecoSuggestDTO(tabelaPreco);
		return Response.ok(tabelaPrecoSuggestDTO).build();
	}
	
	@GET
	@Path("/tabelapreco")
	public Response findMarkupGeralByIdTabelaPreco(@QueryParam("idTabela") Long id) {
		MarkupDTO markupGeralDTO = this.conversorMarkup.converteParaJson(markupService.findMarkupGeral(id));
		return Response.ok(markupGeralDTO).build();
	}
	
	@GET
	@Path("/generalidade")
	public Response findGeneralidadeByIdTabelaPreco(@QueryParam("idTabela") Long id) {
		List<MarkupDTO> listMarkupGeneralidadeDTO = this.conversorMarkup.converteTodosParaJson(markupService.findMarkupsGeneralidadeVigentes(id));
		return Response.ok(listMarkupGeneralidadeDTO).build();
	}
	
	@GET
	@Path("/generalidadehist")
	public Response findGeneralidadeByIdTabelaPrecoHist(@QueryParam("idTabela") Long id) {
		List<MarkupDTO> listMarkupGeneralidadeDTO = this.conversorMarkup.converteTodosParaJson(markupService.findMarkupsGeneralidadeNotVigentes(id));
		return Response.ok(listMarkupGeneralidadeDTO).build();
	}
	
	@GET
	@Path("/minimoprogressivo")
	public Response findMinimoProgressivoByIdTabelaPreco(@QueryParam("idTabela") Long idTabela) {
		return Response.ok(montaListaMarkupMinimoProgressivoDto(idTabela)).build();
	}
	
	@GET
	@Path("/minimoprogressivohist")
	public Response findMinimoProgressivoByIdTabelaPrecoHist(@QueryParam("idTabela") Long idTabela) {
		return Response.ok(montaListaMarkupMinimoProgressivoDtoHist(idTabela)).build();
	}
	
	@GET
	@Path("/modelominimoprogressivo")
	public Response findModeloMinimoProgressivoByIdTabelaPreco(@QueryParam("idTabela") Long idTabela) {
		MarkupMinimoProgressivoDTO modelo = this.conversorMarkupMinimoProgressivo.criaValoresVazios(idTabela, null, parcelaPrecoService, faixaProgressivaService, valorFaixaProgressivaService);
		return Response.ok(modelo).build();
	}
	
	@GET
	@Path("/precofrete")
	public Response findprecoFreteByIdTabelaPreco(@QueryParam("idTabela") Long idTabela) {
		return Response.ok(montaListaMarkupPrecoFreteDto(idTabela)).build();
	}
	
	@GET
	@Path("/precofretehist")
	public Response findprecoFreteByIdTabelaPrecoHist(@QueryParam("idTabela") Long idTabela) {
		return Response.ok(montaListaMarkupPrecoFreteDtoHist(idTabela)).build();
	}
	
	@GET
	@Path("/modeloprecofrete")
	public Response findModeloprecoFreteByIdTabelaPreco(@QueryParam("idTabela") Long idTabela) {
		MarkupPrecoFreteDTO modelo = this.conversorMarkupPrecoFrete.criaValoresVazios(idTabela, null, parcelaPrecoService, precoFreteService);
		return Response.ok(modelo).build();
	}
	
	private List<MarkupPrecoFreteDTO> montaListaMarkupPrecoFreteDto(Long id) {
		List<MarkupPrecoFreteDTO> listMarkupPrecoFreteDTO = new ArrayList<MarkupPrecoFreteDTO>();
		List<MarkupPrecoFreteDTO> listMarkupPrecoFreteDtoExistentes = this.conversorMarkupPrecoFrete.converteTodosParaDto(markupService.findValorMarkupPrecoFreteVigentes(id), false);
		this.conversorMarkupPrecoFrete.criaValoresVazios(id, listMarkupPrecoFreteDtoExistentes, parcelaPrecoService, precoFreteService);
		
		if(CollectionUtils.isNotEmpty(listMarkupPrecoFreteDtoExistentes)){
			listMarkupPrecoFreteDTO.addAll(listMarkupPrecoFreteDtoExistentes);
		}
		return listMarkupPrecoFreteDTO;
	}
	
	private List<MarkupPrecoFreteDTO> montaListaMarkupPrecoFreteDtoHist(Long id) {
		List<MarkupPrecoFreteDTO> listMarkupPrecoFreteDTO = new ArrayList<MarkupPrecoFreteDTO>();
		List<MarkupPrecoFreteDTO> listMarkupPrecoFreteDtoExistentes = this.conversorMarkupPrecoFrete.converteTodosParaDto(markupService.findValorMarkupPrecoFreteNotVigentes(id), true);
		this.conversorMarkupPrecoFrete.criaValoresVazios(id, listMarkupPrecoFreteDtoExistentes, parcelaPrecoService, precoFreteService);
		
		if(CollectionUtils.isNotEmpty(listMarkupPrecoFreteDtoExistentes)){
			listMarkupPrecoFreteDTO.addAll(listMarkupPrecoFreteDtoExistentes);
		}
		return listMarkupPrecoFreteDTO;
	}
	
	private List<MarkupMinimoProgressivoDTO> montaListaMarkupMinimoProgressivoDto(Long id) {
		List<MarkupMinimoProgressivoDTO> listMarkupMinimoProgressivoDTO = new ArrayList<MarkupMinimoProgressivoDTO>();
		List<MarkupMinimoProgressivoDTO> listMarkupMinimoProgDtoExistentes = this.conversorMarkupMinimoProgressivo.converteTodosParaDto(markupService.findValorMarkupFaixaProgressivaVigentes(id), false);
		this.conversorMarkupMinimoProgressivo.criaValoresVazios(id, listMarkupMinimoProgDtoExistentes, parcelaPrecoService, faixaProgressivaService, valorFaixaProgressivaService);
		
		if(CollectionUtils.isNotEmpty(listMarkupMinimoProgDtoExistentes)){
			listMarkupMinimoProgressivoDTO.addAll(listMarkupMinimoProgDtoExistentes);
		}
		return listMarkupMinimoProgressivoDTO;
	}
	
	private List<MarkupMinimoProgressivoDTO> montaListaMarkupMinimoProgressivoDtoHist(Long id) {
		List<MarkupMinimoProgressivoDTO> listMarkupMinimoProgressivoDTO = new ArrayList<MarkupMinimoProgressivoDTO>();
		List<MarkupMinimoProgressivoDTO> listMarkupMinimoProgDtoExistentes = this.conversorMarkupMinimoProgressivo.converteTodosParaDto(markupService.findValorMarkupFaixaProgressivaNotVigentes(id), true);
		this.conversorMarkupMinimoProgressivo.criaValoresVazios(id, listMarkupMinimoProgDtoExistentes, parcelaPrecoService, faixaProgressivaService, valorFaixaProgressivaService);
		
		if(CollectionUtils.isNotEmpty(listMarkupMinimoProgDtoExistentes)){
			listMarkupMinimoProgressivoDTO.addAll(listMarkupMinimoProgDtoExistentes);
		}
		return listMarkupMinimoProgressivoDTO;
	}
	
	@GET
	@Path("/tabelapreco/{idTabelaPreco}/parcelas")
	public Response findParcelas(@PathParam("idTabelaPreco") Long idTabela, @QueryParam("tipoPrecificacao") String tipoPrecificacao) {
		if(!TIPOS_PRECIFICACAO.containsKey(tipoPrecificacao)){
			throw new ADSMException(String.format("Chave %s não possui tipo de precificação válida", tipoPrecificacao));
		}
		List<ParcelaPreco> listaTipoPrecificacao = tabelaPrecoService.findByIdTabelaPrecoTipoPrecificacao(idTabela, TIPOS_PRECIFICACAO.get(tipoPrecificacao));
		return Response.ok(parcelaPrecoConverter.converteTodosParaDto(listaTipoPrecificacao)).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response removeById(@PathParam("id") Long id) {
		this.markupService.removeById(id);
		return Response.ok().build();
	}
	
	@POST
	@Path("/removeall")
	public Response removeByIds(List<Long> ids) {
		this.markupService.removeByIds(ids);
		return Response.ok().build();
	}
	
	@POST
	@Path("/validavigenciaMinimoProgressivotela")
	public Response validavigenciaMinimoProgressivotela(List<MarkupMinimoProgressivoDTO> markups) {
		Map<String, List<Interval>> markupsPorRotaTarifa = preparaIntervalosMinimoProgressivo(markups);
		this.validaVigencia(markupsPorRotaTarifa);
		return Response.ok("").build();
	}
	
	private Map<String, List<Interval>> preparaIntervalosMinimoProgressivo(List<MarkupMinimoProgressivoDTO> markups) {
		Map<String, List<Interval>> markupsPorRotaTarifa = new HashMap<String, List<Interval>>();
		for (MarkupMinimoProgressivoDTO markup : markups) {
			
			String chave = markup.getIdRota() != null ? markup.getIdRota().toString() : markup.getIdTarifa() != null ? markup.getIdTarifa().toString() : null;
					
			if (!markupsPorRotaTarifa.containsKey(chave)) {
				markupsPorRotaTarifa.put(chave, new ArrayList<Interval>());
			}
			markupsPorRotaTarifa.get(chave).add(
					new Interval(
							markup.getDataVigenciaInicial().toDateTime(new TimeOfDay(0, 0, 0)), 
							markup.getDataVigenciaFinal() == null ? JTDateTimeUtils.MAX_YEARMONTHDAY.toDateTime(new TimeOfDay(23,59,59)) : markup.getDataVigenciaFinal().toDateTime(new TimeOfDay(23,59,59))
							));
		}
		return markupsPorRotaTarifa;
	}
	
	@POST
	@Path("/validavigenciaPrecoFretetela")
	public Response validavigenciaPrecoFretetela(List<MarkupPrecoFreteDTO> markups) {
		Map<String, List<Interval>> markupsPorRotaTarifa = preparaIntervalosPrecoFrete(markups);
		this.validaVigencia(markupsPorRotaTarifa);
		return Response.ok("").build();
	}
	
	private Map<String, List<Interval>> preparaIntervalosPrecoFrete(List<MarkupPrecoFreteDTO> markups) {
		Map<String, List<Interval>> markupsPorRotaTarifa = new HashMap<String, List<Interval>>();
		for (MarkupPrecoFreteDTO markup : markups) {
			
			String chave = markup.getIdRota() != null ? markup.getIdRota().toString() : markup.getIdTarifa() != null ? markup.getIdTarifa().toString() : null;
					
			if (!markupsPorRotaTarifa.containsKey(chave)) {
				markupsPorRotaTarifa.put(chave, new ArrayList<Interval>());
			}
			markupsPorRotaTarifa.get(chave).add(
					new Interval(
							markup.getDataVigenciaInicial().toDateTime(new TimeOfDay(0, 0, 0)), 
							markup.getDataVigenciaFinal() == null ? JTDateTimeUtils.MAX_YEARMONTHDAY.toDateTime(new TimeOfDay(23,59,59)) : markup.getDataVigenciaFinal().toDateTime(new TimeOfDay(23,59,59))
							));
		}
		return markupsPorRotaTarifa;
	}
	
	private void validaVigencia(Map<String, List<Interval>> markupsPorRotaTarifa) {
		for (String chave : markupsPorRotaTarifa.keySet()) {
			if(!markupService.validaVigencia(markupsPorRotaTarifa.get(chave))){
				
				RotaPreco rotaPreco = rotaPrecoService.findById(Long.valueOf(chave));
				String param = ROTA + rotaPreco.getOrigemString() +" > " + rotaPreco.getDestinoString();
				
				throw new BusinessException("LMS-46116", new Object[] {param});
			}
		}
	}
	
	@POST
	@Path("/validavigenciaGeneralidadetela")
	public Response validavigenciaGeneralidadetela(List<MarkupDTO> markups) {
		Map<String, List<Interval>> markupsPorRotaTarifa = preparaIntervalosGeneralidade(markups);
		this.validaVigenciaGeneralidade(markupsPorRotaTarifa);
		return Response.ok("").build();
	}
	
	private Map<String, List<Interval>> preparaIntervalosGeneralidade(List<MarkupDTO> markups) {
		Map<String, List<Interval>> markupsPorParcela = new HashMap<String, List<Interval>>();
		for (MarkupDTO markup : markups) {
			
			String chave = markup.getIdParcela() != null ? markup.getIdParcela().toString() : null;
					
			if (!markupsPorParcela.containsKey(chave)) {
				markupsPorParcela.put(chave, new ArrayList<Interval>());
			}
			markupsPorParcela.get(chave).add(
					new Interval(
							markup.getDataVigenciaInicial().toDateTime(new TimeOfDay(0, 0, 0)), 
							markup.getDataVigenciaFinal() == null ? JTDateTimeUtils.MAX_YEARMONTHDAY.toDateTime(new TimeOfDay(23,59,59)) : markup.getDataVigenciaFinal().toDateTime(new TimeOfDay(23,59,59))
							));
		}
		return markupsPorParcela;
	}
	
	private void validaVigenciaGeneralidade(Map<String, List<Interval>> markupsPorRotaTarifa) {
		for (String chave : markupsPorRotaTarifa.keySet()) {
			if(!markupService.validaVigencia(markupsPorRotaTarifa.get(chave))){
				
				ParcelaPreco parcelaPreco = parcelaPrecoService.findById(Long.valueOf(chave));
				String param = PARCELA + parcelaPreco.getNmParcelaPreco();
				
				throw new BusinessException("LMS-46116", new Object[] {param});
			}
		}
	}
	
	@POST
	@Path("/storeall")
	public Response storeAll(MarkupTabelasDTO markups) {
		Long idTabelaPreco = markups.getIdTabelaPreco();
		
		List<MarkupFaixaProgressiva> entidadesFaixaProgressiva = this.conversorMarkupMinimoProgressivo.converteTodosParaEntidade(markups.getListMarkupMinimoProgressivoDTO(), faixaProgressivaService);
		List<ValorMarkupPrecoFrete> entidadesPrecoFrete = this.conversorMarkupPrecoFrete.converteTodosParaEntidades(markups.getListMarkupPrecoFreteDTO(), idTabelaPreco, precoFreteService);
		List<Markup> entidadesGeneralidade = this.conversorMarkup.converteTodosParaEntidade(markups.getListMarkupDto(), idTabelaPreco);
		Markup markupGeral = this.conversorMarkup.converteParaEntidade(markups.getMarkupGeral(), idTabelaPreco);
		
		this.validaVigenciaFaixaProgressiva(entidadesFaixaProgressiva, idTabelaPreco);
		this.validaVigenciaPrecoFrete(entidadesPrecoFrete, idTabelaPreco);
		this.validaVigenciaGeneralidade(entidadesGeneralidade, idTabelaPreco);
		
		this.markupService.executeSubmitAllFaixaProgressiva(entidadesFaixaProgressiva, idTabelaPreco);
		this.markupService.executeSubmitAllPrecoFrete(entidadesPrecoFrete, idTabelaPreco);
		this.markupService.executeSubmitAllGeneralidade(entidadesGeneralidade, idTabelaPreco);
		this.markupService.executeSubmitMarkupGeral(markupGeral);
		return Response.ok(markups).build();
	}
	
	private void validaVigenciaGeneralidade(List<Markup> entidadesGeneralidade, Long idTabelaPreco) {
		if(CollectionUtils.isEmpty(entidadesGeneralidade)){
			return;
		}
		this.markupService.executeValidaMarkupsGeneralidade(entidadesGeneralidade, idTabelaPreco);
	}

	private void validaVigenciaPrecoFrete(List<ValorMarkupPrecoFrete> entidadesPrecoFrete, Long idTabelaPreco) {
		if(CollectionUtils.isEmpty(entidadesPrecoFrete)){
			return;
		}
		this.markupService.executeValidaMarkupsPrecoFrete(entidadesPrecoFrete, idTabelaPreco);
	}
	
	private void validaVigenciaFaixaProgressiva(List<MarkupFaixaProgressiva> entidadesFaixaProgressiva, Long idTabelaPreco) {
		if(CollectionUtils.isEmpty(entidadesFaixaProgressiva)){
			return;
		}
		this.markupService.executeValidaMarkupsMinimoProgressivo(entidadesFaixaProgressiva, idTabelaPreco);
	}

	@POST
	@Path("/deleteMinimoProgressivo")
	public Response removeAllMarkupFaixaProgressiva(List<Long> idsValorMarkupFaixaProgressiva) {
		markupService.removeAllMarkupFaixaProgressiva(idsValorMarkupFaixaProgressiva);
		return Response.ok().build();
	}
	
	@POST
	@Path("/deletePrecoFrete")
	public Response removePrecosFrete(List<Long> idsValorMarkupPrecoFrete) {
		markupService.removePrecosFrete(idsValorMarkupPrecoFrete);
		return Response.ok().build();
	}
	
	@POST
	@Path("findTarifas")
	public Response findTarifas(TarifaMarkupFiltroDTO filter) {
		List<TarifaPreco> listTarifaPreco = tarifaPrecoService.findTarifaPrecoParaMarkup(filter.getCdTarifaPreco(), filter.getIdTabelaPreco() ,filter.getTipo().equalsIgnoreCase(TIPO_MINIMO_PROGRESSIVO));
		List<TarifaMarkupDTO> tarifasDto = converteTodosParaTarifaDto(listTarifaPreco);
		return Response.ok(tarifasDto).build();
	}

	private List<TarifaMarkupDTO> converteTodosParaTarifaDto(List<TarifaPreco> listTarifaPreco) {
		List<TarifaMarkupDTO> tarifasDto = new ArrayList<TarifaMarkupDTO>();
		for (TarifaPreco entidade : listTarifaPreco) {
			TarifaMarkupDTO tarifaDto = new TarifaMarkupDTO();
			tarifaDto.setIdTarifaPreco(entidade.getIdTarifaPreco());
			tarifaDto.setCdTarifaPreco(entidade.getCdTarifaPreco());
			tarifasDto.add(tarifaDto);
		}
		return tarifasDto;
	}

	@POST
	@Path("findRotas")
	public Response findRotas(RotaMarkupFiltroDTO filter) {
		if(StringUtils.isEmpty(filter.getTextoBusca())){
			return Response.ok().build();
		}
		Pattern pattern = Pattern.compile("([\\w\\s]+)-?([\\w\\s]+)?>?([\\w\\s]+)?-?([\\w\\s]+)?");
		Matcher matcher = pattern.matcher(filter.getTextoBusca().toUpperCase());
		if(!matcher.matches()){
			return Response.ok().build();
		}
		
		String sgUnidadeFederativaOrigem = matcher.group(1).trim();
		String sgAeroportoOrigem = null;
		String sgUnidadeFederativaDestino = null;
		String sgAeroportoDestino = null;
		
		if(StringUtils.isNotEmpty(matcher.group(2))){
			sgAeroportoOrigem = matcher.group(2).trim();
		}
		if(StringUtils.isNotEmpty(matcher.group(3))){
			sgUnidadeFederativaDestino = matcher.group(3).trim();
		}
		if(StringUtils.isNotEmpty(matcher.group(4))){
			sgAeroportoDestino = matcher.group(4).trim();
		}
		
		List<RotaPreco> listRotaPreco = rotaPrecoService.findRotaPrecoParaMarkup(filter.getIdTabelaPreco(), sgUnidadeFederativaOrigem, sgAeroportoOrigem, sgUnidadeFederativaDestino, sgAeroportoDestino, filter.getTipo().equalsIgnoreCase(TIPO_MINIMO_PROGRESSIVO));
		List<RotaMarkupDTO> rotasDto = converteTodosParaRotaDto(listRotaPreco);
		return Response.ok(rotasDto).build();
	}
	
	private List<RotaMarkupDTO> converteTodosParaRotaDto(List<RotaPreco> listRotaPreco) {
		List<RotaMarkupDTO> rotasDto = new ArrayList<RotaMarkupDTO>();
		for (RotaPreco entidade : listRotaPreco) {
			RotaMarkupDTO rotaDto = new RotaMarkupDTO();
			rotaDto.setIdRotaPreco(entidade.getIdRotaPreco());
			rotaDto.setDescricao(entidade.getOrigemString() +" > " + entidade.getDestinoString());
			rotasDto.add(rotaDto);
		}
		return rotasDto;
	}
	
}
