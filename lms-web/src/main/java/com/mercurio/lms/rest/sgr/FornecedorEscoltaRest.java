package com.mercurio.lms.rest.sgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.sgr.dto.FornecedorEscoltaDTO;
import com.mercurio.lms.rest.sgr.dto.FornecedorEscoltaFilterDTO;
import com.mercurio.lms.rest.sgr.dto.FornecedorEscoltaImpedidoDTO;
import com.mercurio.lms.rest.sgr.dto.FornecedorEscoltaSuggestDTO;
import com.mercurio.lms.rest.sgr.dto.FranquiaFornecedorEscoltaDTO;
import com.mercurio.lms.rest.utils.SuggestResponseBuilder;
import com.mercurio.lms.sgr.model.FornecedorEscolta;
import com.mercurio.lms.sgr.model.FornecedorEscoltaImpedido;
import com.mercurio.lms.sgr.model.FranquiaFornecedorEscolta;
import com.mercurio.lms.sgr.model.service.FornecedorEscoltaService;

@Path("sgr/fornecedorEscolta")
public class FornecedorEscoltaRest extends LmsBaseCrudReportRest<FornecedorEscoltaDTO, FornecedorEscoltaDTO, FornecedorEscoltaFilterDTO> {

	@InjectInJersey
	private FornecedorEscoltaService fornecedorEscoltaService;

	@InjectInJersey
	private PessoaService pessoaService;

	@InjectInJersey
	private ParametroGeralService parametroGeralService;

	@Override
	protected Integer count(FornecedorEscoltaFilterDTO filter) {
		return fornecedorEscoltaService.getRowCount(convertFilterToTypedFlatMap(filter));
	}

	@Override
	protected List<FornecedorEscoltaDTO> find(FornecedorEscoltaFilterDTO filter) {
		List<FornecedorEscolta> list = fornecedorEscoltaService.find(convertFilterToTypedFlatMap(filter));
		return FornecedorEscoltaDTOBuilder.convertFornecedorEscoltaDTO(list);
	}

	@POST
	@Path("findSuggest")
	public Response findSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		List<FornecedorEscolta> list = fornecedorEscoltaService.findSuggest(value);
		List<FornecedorEscoltaSuggestDTO> dtos = FornecedorEscoltaDTOBuilder.convertFornecedorEscoltaSuggestDTO(list);
		int limite = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS").getDsConteudo());
		return new SuggestResponseBuilder(dtos, limite).build();
	}

	@Override
	protected FornecedorEscoltaDTO findById(Long id) {
		FornecedorEscolta fornecedorEscolta = fornecedorEscoltaService.findById(id);
		return FornecedorEscoltaDTOBuilder.buildFornecedorEscoltaDTO(fornecedorEscolta);
	}

	@Override
	protected Long store(FornecedorEscoltaDTO bean) {
		return fornecedorEscoltaService.store(FornecedorEscoltaDTOBuilder.buildFornecedorEscolta(bean));
	}

	@Override
	protected void removeById(Long id) {
		fornecedorEscoltaService.removeById(id);
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		fornecedorEscoltaService.removeByIds(ids);
	}

	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(getColumn("cnpj", "nrIdentificador"));
		list.add(getColumn("razaoSocial", "nmPessoa"));
		list.add(getColumn("vigenciaInicial", "dtVigenciaInicial"));
		list.add(getColumn("vigenciaFinal", "dtVigenciaFinal"));
		list.add(getColumn("email", "dsEmailFornecedor"));
		list.add(getColumn("telefone", "dsTelefone1"));
		list.add(getColumn("telefone", "dsTelefone2"));
		list.add(getColumn("telefone", "dsTelefone3"));
		return list;
	}

	@Override
	protected List<Map<String, Object>> findDataForReport(FornecedorEscoltaFilterDTO filter) {
		return convertObjectToMap(fornecedorEscoltaService.find(convertFilterToTypedFlatMap(filter)));
	}

	@GET
	@Path("findNmPessoaByNrIdentificacao/{nrIdentificacao}")
	public String findNmPessoaByNrIdentificacao(@PathParam("nrIdentificacao") String nrIdentificacao) {
		Pessoa pessoa = pessoaService.findByNrIdentificacao(nrIdentificacao);
		return pessoa != null ? pessoa.getNmPessoa() : null;
	}

	@POST
	@Path("findFranquia")
	public Response findFranquia(FornecedorEscoltaFilterDTO filter) {
		TypedFlatMap criteria = convertFilterToTypedFlatMap(filter);
		Integer count = fornecedorEscoltaService.getRowCountFranquia(criteria);
		List<FranquiaFornecedorEscolta> list = fornecedorEscoltaService.findFranquia(criteria);
		List<FranquiaFornecedorEscoltaDTO> dtos = FornecedorEscoltaDTOBuilder.convertFranquiaFornecedorEscoltaDTO(list);
		return getReturnFind(dtos, count);
	}

	@GET
	@Path("findFranquia/{id}")
	public FranquiaFornecedorEscoltaDTO findFranquiaById(@PathParam("id") Long id) {
		FranquiaFornecedorEscolta franquia = fornecedorEscoltaService.findFranquiaById(id);
		return FornecedorEscoltaDTOBuilder.buildFranquiaFornecedorEscoltaDTO(franquia);
	}

	@POST
	@Path("storeFranquia")
	public Long storeFranquia(FranquiaFornecedorEscoltaDTO bean) {
		FranquiaFornecedorEscolta franquia = FornecedorEscoltaDTOBuilder.buildFranquiaFornecedorEscolta(bean);
		fornecedorEscoltaService.storeFranquia(franquia);
		return franquia.getIdFranquiaFornecedorEscolta();
	}

	@POST
	@Path("removeFranquiaByIds")
	public void removeFranquiaByIds(List<Long> ids) {
		fornecedorEscoltaService.removeFranquiaByIds(ids);
	}

	@POST
	@Path("findBlacklist")
	public Response findBlacklist(FornecedorEscoltaFilterDTO filter) {
		TypedFlatMap criteria = convertFilterToTypedFlatMap(filter);
		Integer count = fornecedorEscoltaService.getRowCountFranquia(criteria);
		List<FornecedorEscoltaImpedido> list = fornecedorEscoltaService.findBlacklist(criteria);
		List<FornecedorEscoltaImpedidoDTO> dtos = FornecedorEscoltaDTOBuilder.convertFornecedorEscoltaImpedidoDTO(list);
		return getReturnFind(dtos, count);
	}

	@POST
	@Path("storeBlacklist")
	public Long storeBlacklist(FornecedorEscoltaImpedidoDTO bean) {
		FornecedorEscoltaImpedido impedido = FornecedorEscoltaDTOBuilder.buildFornecedorEscoltaImpedido(bean);
		fornecedorEscoltaService.storeBlacklist(impedido);
		return impedido.getIdFornecedorEscoltaImpedido();
	}

	@POST
	@Path("removeBlacklistByIds")
	public void removeBlacklistByIds(List<Long> ids) {
		fornecedorEscoltaService.removeBlacklistByIds(ids);
	}

	private TypedFlatMap convertFilterToTypedFlatMap(FornecedorEscoltaFilterDTO filter) {
		TypedFlatMap map = super.getTypedFlatMapWithPaginationInfo(filter);
		putIfNotNull(filter.getId(), map, "idFornecedorEscolta");
		putIfNotNull(filter.getNrIdentificacao(), map, "nrIdentificacao");
		putIfNotNull(filter.getNmPessoa(), map, "nmPessoa");
		return map;
	}

	private void putIfNotNull(Object object, TypedFlatMap map, String key) {
		if (object != null) {
			map.put(key, object);
		}
	}

	private List<Map<String, Object>> convertObjectToMap(List<FornecedorEscolta> list) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (FornecedorEscolta fornecedor : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			Pessoa pessoa = fornecedor.getPessoa();
			map.put("nrIdentificador", pessoa.getNrIdentificacao());
			map.put("nmPessoa", pessoa.getNmPessoa());
			map.put("dtVigenciaInicial", fornecedor.getDtVigenciaInicial());
			map.put("dtVigenciaFinal", fornecedor.getDtVigenciaFinal());
			map.put("dsEmailFornecedor", fornecedor.getDsEmailFornecedor());
			map.put("dsTelefone1", fornecedor.getDsTelefone1());
			map.put("dsTelefone2", fornecedor.getDsTelefone2());
			map.put("dsTelefone3", fornecedor.getDsTelefone3());
			result.add(map);
		}
		return result;
	}

}
