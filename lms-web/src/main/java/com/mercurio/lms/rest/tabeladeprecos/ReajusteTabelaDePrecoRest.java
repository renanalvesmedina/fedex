package com.mercurio.lms.rest.tabeladeprecos;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.rest.json.YearMonthDayDeserializer;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ReajusteTabelaPrecoFacade;
import com.mercurio.lms.tabelaprecos.model.service.ReajusteTabelaPrecoService;

@Path("/tabeladeprecos/reajustetabeladepreco")
public class ReajusteTabelaDePrecoRest {

	@InjectInJersey ReajusteTabelaPrecoService reajusteTabelaPrecoService;
	@InjectInJersey ReajusteTabelaPrecoFacade reajusteTabelaPrecoFacade;
	
	/**
	 * Buscar os registros da listagem
	 * 
	 * @param filter
	 * @return
	 */
	@POST
	@Path("find")
	public Response findRest(FiltroPaginacaoDto filter) {
		return Response.ok(find(filter)).build();
	}
	
	protected Map<String, Object> find(FiltroPaginacaoDto filtro) {
		return reajusteTabelaPrecoService.findReajuste(filtro);
	}
	
	@POST
	@Path("findParcelas")
	public List<Map<String, Object>> findParcelas(Long idTabelaBase){
		return reajusteTabelaPrecoService.listParcelas(idTabelaBase);
	}
	
	@POST
	@Path("listParcelaFreteQuilo")
	public List<Map<String, Object>> listParcelaFreteQuilo(Long idReajuste){
		return reajusteTabelaPrecoService.listParcelaFreteQuilo(idReajuste);
	}
	
	@POST
	@Path("listParcelaAdvalorem")
	public List<Map<String, Object>> listParcelaAdvalorem(Long idReajuste){
		return reajusteTabelaPrecoService.listParcelaAdvalorem(idReajuste);
	}
	
	@POST
	@Path("listParcelaGeneralidade")
	public List<Map<String, Object>> listParcelaGeneralidade(Long idReajuste){
		return reajusteTabelaPrecoService.listParcelaGeneralidade(idReajuste);
	}
	
	@POST
	@Path("listParcelaServicoAdicional")
	public List<Map<String, Object>> listParcelaServicoAdicional(Long idReajuste){
		return reajusteTabelaPrecoService.listParcelaServicoAdicional(idReajuste);
	}
	
	@POST
	@Path("listParcelaTaxas")
	public List<Map<String, Object>> listParcelaTaxas(Long idReajuste){
		return reajusteTabelaPrecoService.listParcelaTaxas(idReajuste);
	}
	
	@POST
	@Path("listParcelaPesoRodoviario")
	public List<LinkedHashMap<String, Object>> listParcelaPesoRodoviario(Long idReajuste){
		return reajusteTabelaPrecoService.listParcelaFretePesoRodoviario(idReajuste);
	}
	
	@POST
	@Path("listParcelaPesoAereo")
	public List<Map<String, Object>> listParcelaPesoAereo(Map<String,Object> params){
		return reajusteTabelaPrecoService.listParcelaFretePesoAereo((Integer)params.get("idReajuste"),(Integer)params.get("idOrigem"), (Integer)params.get("idDestino"));
	}
	
	@POST
	@Path("listParcelaTaxaCombustivel")
	public List<Map<String, Object>> listParcelaTaxaCombustivel(Map<String,Object> params){
		return reajusteTabelaPrecoService.listParcelaTaxaCombustivel((Integer)params.get("idReajuste"),(Integer)params.get("idOrigem"), (Integer)params.get("idDestino"));
	}
	
	@POST
	@Path("listParcelaTarifaMinima")
	public List<Map<String, Object>> listParcelaTarifaMinima(Map<String,Object> params){
		return reajusteTabelaPrecoService.listParcelaTarifaMinima((Integer)params.get("idReajuste"),(Integer)params.get("idOrigem"), (Integer)params.get("idDestino"));
	}
	
	@POST
	@Path("salvarParcelaTarifaMinima")
	public String salvarParcelaTarifaMinima(List<Map<String,Object>> listParams){
		return reajusteTabelaPrecoService.salvarPercValor(listParams);
	}
	
	@POST
	@Path("listParcelaPesoDiferenciada")
	public List<Map<String, Object>> listParcelaPesoDiferenciada(Long idReajuste){
		return reajusteTabelaPrecoService.listParcelaFretePesoDiferenciada(idReajuste);
	}
	
	@POST
	@Path("listParcelaTaxaTerrestre")
	public List<Map<String, Object>> listParcelaTaxaTerrestre(Long idReajuste){
		return reajusteTabelaPrecoService.listParcelaTaxaTerrestre(idReajuste);
	}
	
	@POST
	@Path("salvarParcelaPesoDiferenciada")
	public String salvarParcelaPesoDiferenciada(List<Map<String,Object>> listParams){
		return reajusteTabelaPrecoService.salvarPercValor(listParams);
	}
	
	@POST
	@Path("salvarParcelaPesoAereo")
	public String salvarParcelaPesoAereo(List<Map<String,Object>> listParams){
		return reajusteTabelaPrecoService.salvarPercValor(listParams);
	}
	
	@POST
	@Path("salvarParcelaServicoAdicional")
	public String salvarParcelaServicoAdicional(List<Map<String,Object>> listParams){
		return reajusteTabelaPrecoService.salvarPercValorAndPercValorMin(listParams);
	}
	
	@POST
	@Path("salvarParcelaTaxas")
	public String salvarParcelaTaxas(List<Map<String,Object>> listParams){
		return reajusteTabelaPrecoService.salvarPercValorAndPercValorMin(listParams);
	}
	
	@POST
	@Path("salvarParcelaGeneralidade")
	public String salvarParcelaGeneralidade(List<Map<String,Object>> listParams){
		return reajusteTabelaPrecoService.salvarPercValorAndPercValorMin(listParams);
	}
	
	@POST
	@Path("salvarParcelaFreteQuilo")
	public String salvarParcelaFreteQuilo(List<Map<String,Object>> listParams){
		return reajusteTabelaPrecoService.salvarPercValor(listParams);
	}
	
	@POST
	@Path("salvarParcelaTaxaCombustivel")
	public String salvarParcelaTaxaCombustivel(List<Map<String,Object>> listParams){
		return reajusteTabelaPrecoService.salvarPercValor(listParams);
	}
	
	@POST
	@Path("salvarParcelaAdvalorem")
	public String salvarParcelaAdvalorem(List<Map<String,Object>> listParams){
		return reajusteTabelaPrecoService.salvarPercValor(listParams);
	}
	
	@POST
	@Path("salvarParcelaTaxaTerrestre")
	public String salvarParcelaTaxaTerrestre(List<Map<String,Object>> listParams){
		return reajusteTabelaPrecoService.salvarPercValor(listParams);
	}

	@POST
	@Path("efetivar")
	public ReajusteTabelaPreco efetivar(Map<String,Object> paramsReajuste)  throws Throwable{
		paramsReajuste = convertDatasStringToYearMonthDayEfetivar(paramsReajuste);
		
		return reajusteTabelaPrecoFacade.reajusteTabelaPreco(paramsReajuste);
	}
	
	
	private Map<String, Object> convertDatasStringToYearMonthDayEfetivar(
			Map<String, Object> paramsReajuste) {
		paramsReajuste.put("dtVigenciaInicial", 
				YearMonthDayDeserializer.parse((String) paramsReajuste.get("dtVigenciaInicial")));
		paramsReajuste.put("dtVigenciaFinal", 
				YearMonthDayDeserializer.parse((String) paramsReajuste.get("dtVigenciaFinal")));
		paramsReajuste.put("dtAgendamento", 
				YearMonthDayDeserializer.parse((String) paramsReajuste.get("dtAgendamento")));
		return paramsReajuste;
	}

	/**
	 * Método get padrão (findById)
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	public Response getRest(@PathParam("id") Long id) {
		return Response.ok(findById(id)).build();
	}
	
	protected Map<String, Object> findById(Long id) {
		return reajusteTabelaPrecoService.findReajusteById(id);
	}

	/**
	 * Metodo delete padrão
	 * 
	 * @param id
	 */
	@DELETE
	@Path("/{id}")
	public void deleteRest(@PathParam("id") Long id) {
		removeById(id);
	}
	
	protected void removeById(Long id) {
		reajusteTabelaPrecoService.removeByIdReajuste(id);
	}

	/**
	 * Método insert padrão
	 * 
	 * @param bean
	 * @return
	 */
	@POST
	@Path("/")
	public Response insertRest(Map<String, Object> bean) {
		return getRest(store(bean));
	}
	
	/**
	 * Método save padrão
	 * 
	 * @param bean
	 * @return
	 */
	@POST
	@Path("/{id}")
	public Response saveRest(@PathParam("id") Long id, Map<String, Object> bean) {
		return getRest(store(bean));
	}
	
	protected Long store(Map<String, Object> map) {

		map = convertDatasStringToYearMonthDayStore(map);
		
		return reajusteTabelaPrecoService.store(map);
	}

	private Map<String, Object> convertDatasStringToYearMonthDayStore(Map<String, Object> map) {
		map.put("dtVigenciaInicial", 
				YearMonthDayDeserializer.parse((String) map.get("dtVigenciaInicial")));
		map.put("dtVigenciaFinal",
				YearMonthDayDeserializer.parse((String) map.get("dtVigenciaFinal")));
		map.put("dtEfetivacao",
				YearMonthDayDeserializer.parse((String) map.get("dtEfetivacao")));
		map.put("dtAgendamento",
				YearMonthDayDeserializer.parse((String) map.get("dtAgendamento")));
		return map;
	}
	
	@POST
	@Path("findTabelaBaseSuggest")
	public Response findSuggest(Map<String, Object> data) {
		return Response.ok(reajusteTabelaPrecoService.findSuggestTabelaBase((String) data.get("value"))).build();
	}
	
	@POST
	@Path("findTabelaNovaSuggest")
	public Response findSuggestTabelaNova(Map<String, Object> data) {
		return Response.ok(reajusteTabelaPrecoService.findSuggestTabelaNova((String) data.get("value"))).build();
	}
	
	@POST
	@Path("findTabelaNovaEfetivadaSuggest")
	public Response findSuggestTabelaNovaEfetivada(Map<String, Object> data) {
		return Response.ok(reajusteTabelaPrecoService.findSuggestTabelaNovaEfetivada((String) data.get("value"))).build();
	}

	@POST
	@Path("findDescricaoTabelaPreco")
	public Map<String, Object> findDescricaoTabelaPreco(Long idTabelaBase){
		return reajusteTabelaPrecoService.findDescricaoTabelaPreco(idTabelaBase);
	}

}
