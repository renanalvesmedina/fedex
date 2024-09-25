package com.mercurio.lms.rest.sgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.sgr.dto.SimularPlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.rest.sgr.dto.SimularPlanoGerenciamentoRiscoFilterDTO;
import com.mercurio.lms.sgr.model.service.PlanoGerenciamentoRiscoService;

/**
 * FIXME corrigir número do JIRA
 * 
 * LMS-???? (Tela para simulação do Plano de Gerenciamento de Risco) - REST para
 * operações da tela "Simular Plano de Gerenciamento de Risco".
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
@Path("/sgr/planoGerenciamentoRisco")
public class SimularPlanoGerenciamentoRiscoRest extends LmsBaseCrudReportRest<SimularPlanoGerenciamentoRiscoDTO, SimularPlanoGerenciamentoRiscoDTO, SimularPlanoGerenciamentoRiscoFilterDTO> {

	@InjectInJersey
	private ControleCargaService controleCargaService;

	@InjectInJersey
	private PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService;

	@Override
	protected SimularPlanoGerenciamentoRiscoDTO findById(Long id) {
		return buildSimularPGRListItemDTO(controleCargaService.findById(id));
	}

	@GET
	@Path("generateEnquadramentoRegra")
	public Response generateEnquadramentoRegra(@QueryParam("id") Long id) {
		return Response.ok(planoGerenciamentoRiscoService.generateEnquadramentoRegraJson(id)).build();
	}

	@Override
	protected Integer count(SimularPlanoGerenciamentoRiscoFilterDTO filter) {
		return controleCargaService.getRowCountByControleCargaByDhGeracao(convertFilterToTypedFlatMap(filter));
	}

	@Override
	protected List<SimularPlanoGerenciamentoRiscoDTO> find(SimularPlanoGerenciamentoRiscoFilterDTO filter) {
		List<ControleCarga> list = controleCargaService.findByControleCargaByDhGeracao(convertFilterToTypedFlatMap(filter));
		return convertSimularPGRListItemDTO(list);
	}

	private TypedFlatMap convertFilterToTypedFlatMap(SimularPlanoGerenciamentoRiscoFilterDTO filter) {
		TypedFlatMap map = super.getTypedFlatMapWithPaginationInfo(filter);
		if (filter.getControleCarga() != null) {
			map.put("idControleCarga", filter.getControleCarga().getId());
		}
		if (filter.getFilial() != null) {
			map.put("idFilial", filter.getFilial().getId());
		}
		if (filter.getDhGeracaoInicial() != null) {
			map.put("dhGeracaoInicial", filter.getDhGeracaoInicial());
		}
		if (filter.getDhGeracaoFinal() != null) {
			map.put("dhGeracaoFinal", filter.getDhGeracaoFinal().plusDays(1));
		}
		return map;
	}

	private List<SimularPlanoGerenciamentoRiscoDTO> convertSimularPGRListItemDTO(List<ControleCarga> list) {
		List<SimularPlanoGerenciamentoRiscoDTO> dtos = new ArrayList<SimularPlanoGerenciamentoRiscoDTO>();
		for (ControleCarga controleCarga : list) {
			dtos.add(buildSimularPGRListItemDTO(controleCarga));
		}
		return dtos;
	}

	private SimularPlanoGerenciamentoRiscoDTO buildSimularPGRListItemDTO(ControleCarga controleCarga) {
		SimularPlanoGerenciamentoRiscoDTO dto = new SimularPlanoGerenciamentoRiscoDTO();
		dto.setId(controleCarga.getIdControleCarga());
		dto.setSgFilial(controleCarga.getFilialByIdFilialOrigem().getSgFilial());
		dto.setNrControleCarga(controleCarga.getNrControleCarga());
		dto.setDhGeracao(controleCarga.getDhGeracao());
		DomainValue tpControleCarga = controleCarga.getTpControleCarga();
		dto.setTpControleCarga(tpControleCarga.getDescriptionAsString());
		if (controleCarga.getRotaColetaEntrega() != null) {
			dto.setDsRota(controleCarga.getRotaColetaEntrega().getDsRota());
			dto.setTpRota(tpControleCarga.getDescriptionAsString());
		} else if (controleCarga.getRota() != null) {
			dto.setDsRota(controleCarga.getRota().getDsRota());
			dto.setTpRota(controleCarga.getTpRotaViagem().getDescriptionAsString());
		}
		MeioTransporte meioTransporte = controleCarga.getMeioTransporteByIdTransportado();
		if (meioTransporte != null) {
			dto.setMeioTransporteNrFrota(meioTransporte.getNrFrota());
			dto.setMeioTransporteNrIdentificador(meioTransporte.getNrIdentificador());
		}
		MeioTransporte semiRebocado = controleCarga.getMeioTransporteByIdSemiRebocado();
		if (semiRebocado != null) {
			dto.setSemiReboqueNrFrota(semiRebocado.getNrFrota());
			dto.setSemiReboqueNrIdentificador(semiRebocado.getNrIdentificador());
		}
		return dto;
	}

	@Override
	protected Long store(SimularPlanoGerenciamentoRiscoDTO bean) {
		return null;
	}

	@Override
	protected void removeById(Long id) {
	}

	@Override
	protected void removeByIds(List<Long> ids) {
	}

	@Override
	protected List<Map<String, String>> getColumns() {
		return null;
	}

	@Override
	protected List<Map<String, Object>> findDataForReport(SimularPlanoGerenciamentoRiscoFilterDTO filter) {
		return null;
	}

}
