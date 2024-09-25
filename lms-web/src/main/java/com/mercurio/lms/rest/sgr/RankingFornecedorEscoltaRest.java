package com.mercurio.lms.rest.sgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.sgr.dto.RankingFornecedorEscoltaDTO;
import com.mercurio.lms.rest.sgr.dto.RankingFornecedorEscoltaFilterDTO;
import com.mercurio.lms.sgr.model.FornecedorEscoltaRanking;
import com.mercurio.lms.sgr.model.service.FornecedorEscoltaRankingService;
import com.mercurio.lms.sgr.model.service.FornecedorEscoltaService;

@Path("/sgr/rankingFornecedorEscolta")
public class RankingFornecedorEscoltaRest extends
		LmsBaseCrudReportRest<RankingFornecedorEscoltaDTO, RankingFornecedorEscoltaDTO, RankingFornecedorEscoltaFilterDTO> {

	@InjectInJersey
	private FornecedorEscoltaRankingService fornecedorEscoltaRankingService;

	@InjectInJersey
	private FornecedorEscoltaService fornecedorEscoltaService;

	@Override
	protected Integer count(RankingFornecedorEscoltaFilterDTO filter) {
		return fornecedorEscoltaRankingService.getRowCount(convertFilterToTypedFlatMap(filter));
	}

	@Override
	protected List<RankingFornecedorEscoltaDTO> find(RankingFornecedorEscoltaFilterDTO filter) {
		List<FornecedorEscoltaRanking> list = fornecedorEscoltaRankingService.find(convertFilterToTypedFlatMap(filter));
		return FornecedorEscoltaDTOBuilder.convertRankingFornecedorEscoltaDTO(list);
	}

	private TypedFlatMap convertFilterToTypedFlatMap(RankingFornecedorEscoltaFilterDTO filter) {
		TypedFlatMap map = super.getTypedFlatMapWithPaginationInfo(filter);
		if (filter.getCliente() != null) {
			map.put("idCliente", filter.getCliente().getId());
		}
		if (filter.getFilial() != null) {
			map.put("idFilial", filter.getFilial().getId());
		}
		if (filter.getDtVigenciaInicial() != null) {
			map.put("dtVigenciaInicial", filter.getDtVigenciaInicial());
		}
		if (filter.getDtVigenciaFinal() != null) {
			map.put("dtVigenciaFinal", filter.getDtVigenciaFinal().plusDays(1));
		}
		return map;
	}

	@Override
	protected RankingFornecedorEscoltaDTO findById(Long id) {
		FornecedorEscoltaRanking fornecedorEscoltaRanking = fornecedorEscoltaRankingService.findById(id);
		// fornecedorEscoltaRanking.setFornecedoresEscoltaRankingItem(fornecedorEscoltaRankingService.findRankItemByIdRanking(id));
		return FornecedorEscoltaDTOBuilder.buildRankingFornecedorEscoltaDTO(fornecedorEscoltaRanking);
	}

	@Override
	protected Long store(RankingFornecedorEscoltaDTO bean) {
		return fornecedorEscoltaRankingService.store(FornecedorEscoltaDTOBuilder.buildFornecedorEscoltaRanking(bean));
	}

	@Override
	protected void removeById(Long id) {
		fornecedorEscoltaRankingService.removeById(id);
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		fornecedorEscoltaRankingService.removeByIds(ids);
	}

	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(getColumn("cliente", "nmPessoa"));
		list.add(getColumn("filial", "sgFilial"));
		list.add(getColumn("dtVigenciaInicial", "dtVigenciaInicial"));
		list.add(getColumn("dtVigenciaFinal", "dtVigenciaFinal"));
		return list;
	}

	@Override
	protected List<Map<String, Object>> findDataForReport(RankingFornecedorEscoltaFilterDTO filter) {
		return null;
	}

}
