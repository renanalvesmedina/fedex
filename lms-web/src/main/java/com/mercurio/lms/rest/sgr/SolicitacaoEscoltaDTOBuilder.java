package com.mercurio.lms.rest.sgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.rest.sgr.dto.ExigenciaGerRiscoDTO;
import com.mercurio.lms.rest.sgr.dto.SolicEscoltaHistoricoDTO;
import com.mercurio.lms.rest.sgr.dto.SolicitacaoEscoltaDTO;
import com.mercurio.lms.rest.sgr.dto.SolicitacaoEscoltaTableDTO;
import com.mercurio.lms.sgr.model.ExigenciaGerRisco;
import com.mercurio.lms.sgr.model.SolicEscoltaHistorico;
import com.mercurio.lms.sgr.model.SolicitacaoEscolta;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;

public final class SolicitacaoEscoltaDTOBuilder extends BaseDTOBuilder {

	public static List<SolicitacaoEscoltaTableDTO> convertSolicitacaoEscoltaTableDTO(List<SolicitacaoEscolta> list) {
		List<SolicitacaoEscoltaTableDTO> dtos = new ArrayList<SolicitacaoEscoltaTableDTO>();
		for (SolicitacaoEscolta bean : list) {
			dtos.add(buildSolicitacaoEscoltaTableDTO(bean));
		}
		return dtos;
	}

	public static SolicitacaoEscoltaTableDTO buildSolicitacaoEscoltaTableDTO(SolicitacaoEscolta bean) {
		SolicitacaoEscoltaTableDTO dto = new SolicitacaoEscoltaTableDTO();
		dto.setId(bean.getIdSolicitacaoEscolta());
		dto.setDhInclusao(bean.getDhInclusao());
		dto.setDhInicioPrevisto(bean.getDhInicioPrevisto());
		dto.setDsExigenciaGerRisco(bean.getExigenciaGerRisco().getDsResumida().getValue());
		dto.setFilialSolicitante(buildFilialSuggestDTO(bean.getFilialSolicitante()));
		dto.setNrSolicitacaoEscolta(bean.getNrSolicitacaoEscolta());
		dto.setDsOrigem(buildDsOrigemDestino(
				bean.getAeroportoOrigem(), bean.getFilialOrigem(), bean.getClienteOrigem(), null));
		dto.setDsDestino(buildDsOrigemDestino(
				bean.getAeroportoDestino(), bean.getFilialDestino(), bean.getClienteDestino(), bean.getRotaColetaEntrega()));
		dto.setTpSituacao(bean.getTpSituacao());
		dto.setClienteEscolta(buildClienteSuggestDTO(bean.getClienteEscolta()));
		dto.setQtViaturas(bean.getQtViaturas());
		return dto;
	}

	public static SolicitacaoEscoltaDTO buildSolicitacaoEscoltaDTO(SolicitacaoEscolta bean) {
		SolicitacaoEscoltaDTO dto = new SolicitacaoEscoltaDTO();
		dto.setId(bean.getIdSolicitacaoEscolta());
		dto.setDhInclusao(bean.getDhInclusao());
		dto.setDhInicioPrevisto(bean.getDhInicioPrevisto());
		dto.setDhFimPrevisto(bean.getDhFimPrevisto());
		dto.setIdExigenciaGerRisco(bean.getExigenciaGerRisco().getIdExigenciaGerRisco());
		if (bean.getControleCarga() != null) {
			dto.setControleCarga(buildControleCargaSuggestDTO(bean.getControleCarga()));
		}
		dto.setFilialSolicitante(buildFilialSuggestDTO(bean.getFilialSolicitante()));
		dto.setNmUsuarioSolicitacao(bean.getUsuarioSolicitante().getUsuarioADSM().getNmUsuario());
		dto.setNrSolicitacaoEscolta(bean.getNrSolicitacaoEscolta());
		dto.setTpOrigem(bean.getTpOrigem());
		if (bean.getFilialOrigem() != null) {
			dto.setFilialOrigem(buildFilialSuggestDTO(bean.getFilialOrigem()));
		}
		if (bean.getAeroportoOrigem() != null) {
			dto.setAeroportoOrigem(buildAeroportoSuggestDTO(bean.getAeroportoOrigem()));
		}
		if (bean.getClienteOrigem() != null) {
			dto.setClienteOrigem(buildClienteSuggestDTO(bean.getClienteOrigem()));
		}
		dto.setTpDestino(bean.getTpDestino());
		if (bean.getFilialDestino() != null) {
			dto.setFilialDestino(buildFilialSuggestDTO(bean.getFilialDestino()));
		}
		if (bean.getAeroportoDestino() != null) {
			dto.setAeroportoDestino(buildAeroportoSuggestDTO(bean.getAeroportoDestino()));
		}
		if (bean.getClienteDestino() != null) {
			dto.setClienteDestino(buildClienteSuggestDTO(bean.getClienteDestino()));
		}
		if (bean.getRotaColetaEntrega() != null) {
			dto.setRotaColetaEntrega(buildRotaColetaEntregaSuggestDTO(bean.getRotaColetaEntrega()));
		}
		dto.setNrKmSolicitacaoEscolta(bean.getNrKmSolicitacaoEscolta());
		dto.setDsObservacao(bean.getDsObservacao());
		dto.setTpSituacao(bean.getTpSituacao());
		if (bean.getMeioTransporteByIdTransportado() != null) {
			dto.setMeioTransporteByIdTransportado(buildMeioTransporteSuggestDTO(bean.getMeioTransporteByIdTransportado()));
		}
		if (bean.getMeioTransporteBySemiReboque() != null) {
			dto.setMeioTransporteBySemiReboque(buildMeioTransporteSuggestDTO(bean.getMeioTransporteBySemiReboque()));
		}
		if (bean.getMotorista() != null) {
			dto.setMotorista(buildMotoristaSuggestDTO(bean.getMotorista()));
		}
		dto.setIdNaturezaProduto(bean.getNaturezaProduto().getIdNaturezaProduto());
		dto.setClienteEscolta(buildClienteSuggestDTO(bean.getClienteEscolta()));
		dto.setVlCargaCliente(bean.getVlCargaCliente());
		dto.setVlCargaTotal(bean.getVlCargaTotal());
		dto.setQtViaturas(bean.getQtViaturas());
		return dto;
	}

	public static List<Map<String, Object>> convertObjectToMap(List<SolicitacaoEscolta> list) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for (SolicitacaoEscolta bean : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sgFilial", bean.getFilialSolicitante().getSgFilial());
			map.put("nrSolicitacaoEscolta", bean.getNrSolicitacaoEscolta());
			map.put("tpSituacao", bean.getTpSituacao().getDescriptionAsString());
			map.put("nrIdentificacao", FormatUtils.formatIdentificacao(bean.getClienteEscolta().getPessoa()));
			map.put("clienteEscolta", bean.getClienteEscolta().getPessoa().getNmPessoa());
			map.put("dsOrigem", buildDsOrigemDestino(
					bean.getAeroportoOrigem(), bean.getFilialOrigem(), bean.getClienteOrigem(), null));
			map.put("dsDestino", buildDsOrigemDestino(
					bean.getAeroportoDestino(), bean.getFilialDestino(), bean.getClienteDestino(), bean.getRotaColetaEntrega()));
			map.put("dhInicioPrevisto", bean.getDhInicioPrevisto());
			map.put("dsExigenciaGerRisco", bean.getExigenciaGerRisco().getDsResumida());
			map.put("qtViaturas", bean.getQtViaturas());
			result.add(map);
		}
		return result;
	}

	private static String buildDsOrigemDestino(Aeroporto aeroporto, Filial filial, Cliente cliente, RotaColetaEntrega rota) {
		String dsOrigemDestino = null;
		if (aeroporto != null) {
			dsOrigemDestino = aeroporto.getSiglaDescricao();
		} else if (filial != null) {
			dsOrigemDestino = filial.getSgFilial();
		} else if (cliente != null) {
			Pessoa pessoa = cliente.getPessoa();
			dsOrigemDestino = FormatUtils.formatIdentificacao(pessoa) + " - " + pessoa.getNmPessoa();
		} else if (rota != null) {
			dsOrigemDestino = rota.getDsRota();
		}
		return dsOrigemDestino;
	}

	public static List<ExigenciaGerRiscoDTO> convertExigenciaGerRiscoDTO(List<ExigenciaGerRisco> list) {
		List<ExigenciaGerRiscoDTO> dtos = new ArrayList<ExigenciaGerRiscoDTO>();
		for (ExigenciaGerRisco bean : list) {
			dtos.add(buildExigenciaGerRiscoDTO(bean));
		}
		return dtos;
	}

	public static ExigenciaGerRiscoDTO buildExigenciaGerRiscoDTO(ExigenciaGerRisco bean) {
		ExigenciaGerRiscoDTO dto = new ExigenciaGerRiscoDTO();
		dto.setId(bean.getIdExigenciaGerRisco());
		dto.setDsResumida(bean.getDsResumida().getValue());
		return dto;
	}

	public static List<?> convertSolicEscoltaHistoricoDTO(List<SolicEscoltaHistorico> list) {
		List<SolicEscoltaHistoricoDTO> dtos = new ArrayList<SolicEscoltaHistoricoDTO>();
		for (SolicEscoltaHistorico bean : list) {
			dtos.add(buildSolicEscoltaHistoricoDTO(bean));
		}
		return dtos;
	}
	
	public static SolicEscoltaHistoricoDTO buildSolicEscoltaHistoricoDTO(SolicEscoltaHistorico bean) {
		SolicEscoltaHistoricoDTO dto = new SolicEscoltaHistoricoDTO();
		dto.setId(bean.getIdHistoricoSolicEscolta());
		dto.setTpSituacao(bean.getTpSituacao());
		dto.setDsJustificativa(bean.getDsJustificativa());
		dto.setDhInclusao(bean.getDhInclusao());
		dto.setNmUsuario(bean.getUsuario().getUsuarioADSM().getNmUsuario());
		return dto;
	}

	private SolicitacaoEscoltaDTOBuilder() {
		throw new AssertionError();
	}

}
