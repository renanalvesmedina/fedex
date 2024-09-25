package com.mercurio.lms.rest.sgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.sgr.dto.SolicitacaoEscoltaDTO;
import com.mercurio.lms.rest.sgr.dto.SolicitacaoEscoltaFilterDTO;
import com.mercurio.lms.rest.sgr.dto.SolicitacaoEscoltaTableDTO;
import com.mercurio.lms.sgr.model.ExigenciaGerRisco;
import com.mercurio.lms.sgr.model.SolicEscoltaHistorico;
import com.mercurio.lms.sgr.model.SolicitacaoEscolta;
import com.mercurio.lms.sgr.model.service.ExigenciaGerRiscoService;
import com.mercurio.lms.sgr.model.service.SolicitacaoEscoltaService;
import com.mercurio.lms.vendas.model.Cliente;

@Path("sgr/solicitacaoEscolta")
public class SolicitacaoEscoltaRest extends LmsBaseCrudReportRest<SolicitacaoEscoltaDTO, SolicitacaoEscoltaTableDTO, SolicitacaoEscoltaFilterDTO> {

	@InjectInJersey
	private SolicitacaoEscoltaService solicitacaoEscoltaService;

	@InjectInJersey
	private ExigenciaGerRiscoService exigenciaGerRiscoService;

	@InjectInJersey
	private ParametroGeralService parametroGeralService;

	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("filial", "sgFilial"));
		list.add(getColumn("nrSolicitacaoEscolta", "nrSolicitacaoEscolta"));
		list.add(getColumn("situacao", "tpSituacao"));
		list.add(getColumn("nrIdentificacao", "nrIdentificacao"));
		list.add(getColumn("clienteSolicitante", "clienteEscolta"));
		list.add(getColumn("origem", "dsOrigem"));
		list.add(getColumn("destino", "dsDestino"));
		list.add(getColumn("inicioPrevisto", "dhInicioPrevisto"));
		list.add(getColumn("tipoEscolta", "dsExigenciaGerRisco"));
		list.add(getColumn("viaturas", "qtViaturas"));
		return list;
	}

	@Override
	protected List<Map<String, Object>> findDataForReport(SolicitacaoEscoltaFilterDTO filter) {
		List<SolicitacaoEscolta> list = solicitacaoEscoltaService.find(convertFilterToTypedFlatMap(filter));
		return SolicitacaoEscoltaDTOBuilder.convertObjectToMap(list);
	}

	@Override
	protected SolicitacaoEscoltaDTO findById(Long id) {
		SolicitacaoEscolta solicitacao = solicitacaoEscoltaService.findById(id);
		return SolicitacaoEscoltaDTOBuilder.buildSolicitacaoEscoltaDTO(solicitacao);
	}

	@Override
	protected Long store(SolicitacaoEscoltaDTO dto) {
		SolicitacaoEscolta bean = new SolicitacaoEscolta();
		bean.setIdSolicitacaoEscolta(dto.getId());
		bean.setDhInicioPrevisto(dto.getDhInicioPrevisto());
		bean.setDhFimPrevisto(dto.getDhFimPrevisto());
		ExigenciaGerRisco exigencia = new ExigenciaGerRisco();
		exigencia.setIdExigenciaGerRisco(dto.getIdExigenciaGerRisco());
		bean.setExigenciaGerRisco(exigencia);
		if (dto.getControleCarga() != null) {
			ControleCarga controleCarga = new ControleCarga();
			controleCarga.setIdControleCarga(dto.getControleCarga().getId());
			bean.setControleCarga(controleCarga);
		}
		bean.setFilialSolicitante(new Filial(dto.getFilialSolicitante().getId()));
		bean.setTpOrigem(dto.getTpOrigem());
		if (dto.getFilialOrigem() != null) {
			bean.setFilialOrigem(new Filial(dto.getFilialOrigem().getId()));
		} else if (dto.getAeroportoOrigem() != null) {
			Aeroporto aeroporto = new Aeroporto();
			aeroporto.setIdAeroporto(dto.getAeroportoOrigem().getId());
			bean.setAeroportoOrigem(aeroporto);
		} else if (dto.getClienteOrigem() != null) {
			bean.setClienteOrigem(new Cliente(dto.getClienteOrigem().getId()));
		}
		bean.setTpDestino(dto.getTpDestino());
		if (dto.getFilialDestino() != null) {
			bean.setFilialDestino(new Filial(dto.getFilialDestino().getId()));
		} else if (dto.getAeroportoDestino() != null) {
			Aeroporto aeroporto = new Aeroporto();
			aeroporto.setIdAeroporto(dto.getAeroportoDestino().getId());
			bean.setAeroportoDestino(aeroporto);
		} else if (dto.getClienteDestino() != null) {
			bean.setClienteDestino(new Cliente(dto.getClienteDestino().getId()));
		} else if (dto.getRotaColetaEntrega() != null) {
			RotaColetaEntrega rota = new RotaColetaEntrega();
			rota.setIdRotaColetaEntrega(dto.getRotaColetaEntrega().getId());
			bean.setRotaColetaEntrega(rota);
		}
		bean.setNrKmSolicitacaoEscolta(dto.getNrKmSolicitacaoEscolta());
		bean.setDsObservacao(dto.getDsObservacao());
		if (dto.getMeioTransporteByIdTransportado() != null) {
			MeioTransporte meioTransporte = new MeioTransporte();
			meioTransporte.setIdMeioTransporte(dto.getMeioTransporteByIdTransportado().getId());
			bean.setMeioTransporteByIdTransportado(meioTransporte);
		}
		if (dto.getMeioTransporteBySemiReboque() != null) {
			MeioTransporte semiReboque = new MeioTransporte();
			semiReboque.setIdMeioTransporte(dto.getMeioTransporteBySemiReboque().getId());
			bean.setMeioTransporteBySemiReboque(semiReboque);
		}
		if (dto.getMotorista() != null) {
			Motorista motorista = new Motorista();
			motorista.setIdMotorista(dto.getMotorista().getId());
			bean.setMotorista(motorista);
		}
		NaturezaProduto natureza = new NaturezaProduto();
		natureza.setIdNaturezaProduto(dto.getIdNaturezaProduto());
		bean.setNaturezaProduto(natureza);
		bean.setClienteEscolta(new Cliente(dto.getClienteEscolta().getId()));
		bean.setVlCargaCliente(dto.getVlCargaCliente());
		bean.setVlCargaTotal(dto.getVlCargaTotal());
		bean.setQtViaturas(dto.getQtViaturas());
		return (Long) solicitacaoEscoltaService.store(bean);
	}

	@Override
	protected void removeById(Long id) {
		solicitacaoEscoltaService.removeById(id);
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		solicitacaoEscoltaService.removeByIds(ids);
	}

	@Override
	protected Integer count(SolicitacaoEscoltaFilterDTO filter) {
		return solicitacaoEscoltaService.getRowCount(convertFilterToTypedFlatMap(filter));
	}

	@Override
	protected List<SolicitacaoEscoltaTableDTO> find(SolicitacaoEscoltaFilterDTO filter) {
		List<SolicitacaoEscolta> list = solicitacaoEscoltaService.find(convertFilterToTypedFlatMap(filter));
		return SolicitacaoEscoltaDTOBuilder.convertSolicitacaoEscoltaTableDTO(list);
	}

	private TypedFlatMap convertFilterToTypedFlatMap(SolicitacaoEscoltaFilterDTO filter) {
		TypedFlatMap map = super.getTypedFlatMapWithPaginationInfo(filter);
		putIfNotNull(filter.getFilialSolicitacao() != null ? filter.getFilialSolicitacao().getIdFilial() : null, map, "idFilialSolicitante");
		putIfNotNull(filter.getNrSolicitacaoEscolta(), map, "nrSolicitacaoEscolta");
		putIfNotNull(filter.getTpSituacao() != null ? filter.getTpSituacao().getValue() : null, map, "tpSituacao");
		putIfNotNull(filter.getUsuarioSolicitante() != null ? filter.getUsuarioSolicitante().getIdUsuario() : null, map, "idUsuarioSolicitante");
		putIfNotNull(filter.getDhInclusaoInicial(), map, "dhInclusaoInicial");
		putIfNotNull(filter.getDhInclusaoFinal() != null ? filter.getDhInclusaoFinal().plusDays(1) : null, map, "dhInclusaoFinal");
		putIfNotNull(filter.getDhInicioPrevistoInicial(), map, "dhInicioPrevistoInicial");
		putIfNotNull(filter.getDhInicioPrevistoFinal() != null ? filter.getDhInicioPrevistoFinal().plusDays(1) : null, map, "dhInicioPrevistoFinal");
		putIfNotNull(filter.getIdExigenciaGerRisco(), map, "idExigenciaGerRisco");
		putIfNotNull(filter.getNrKmSolicitacaoEscoltaInicial(), map, "nrKmSolicitacaoEscoltaInicial");
		putIfNotNull(filter.getNrKmSolicitacaoEscoltaFinal(), map, "nrKmSolicitacaoEscoltaFinal");
		putIfNotNull(filter.getClienteEscolta() != null ? filter.getClienteEscolta().getIdCliente() : null, map, "idClienteEscolta");
		putIfNotNull(filter.getControleCarga() != null ? filter.getControleCarga().getIdControleCarga() : null, map, "idControleCarga");
		putIfNotNull(filter.getIdNaturezaProduto(), map, "idNaturezaProduto");
		putIfNotNull(filter.getMeioTransporteByIdTransportado() != null ? filter.getMeioTransporteByIdTransportado().getIdMeioTransporte() : null, map, "idMeioTransporteTransportado");
		putIfNotNull(filter.getMeioTransporteBySemiReboque() != null ? filter.getMeioTransporteBySemiReboque().getIdMeioTransporte() : null, map, "idMeioTransporteSemiReboque");
		putIfNotNull(filter.getTpOrigem() != null ? filter.getTpOrigem().getValue() : null, map, "tpOrigem");
		putIfNotNull(filter.getFilialOrigem() != null ? filter.getFilialOrigem().getIdFilial() : null, map, "idFilialOrigem");
		putIfNotNull(filter.getClienteOrigem() != null ? filter.getClienteOrigem().getIdCliente() : null, map, "idClienteOrigem");
		putIfNotNull(filter.getAeroportoOrigem() != null ? filter.getAeroportoOrigem().getIdAeroporto() : null, map, "idAeroportoOrigem");
		putIfNotNull(filter.getTpDestino() != null ? filter.getTpDestino().getValue() : null, map, "tpDestino");
		putIfNotNull(filter.getFilialDestino() != null ? filter.getFilialDestino().getIdFilial() : null, map, "idFilialDestino");
		putIfNotNull(filter.getClienteDestino() != null ? filter.getClienteDestino().getIdCliente() : null, map, "idClienteDestino");
		putIfNotNull(filter.getAeroportoDestino() != null ? filter.getAeroportoDestino().getIdAeroporto() : null, map, "idAeroportoDestino");
		putIfNotNull(filter.getRotaColetaEntrega() != null ? filter.getRotaColetaEntrega().getId() : null, map, "idRotaColetaEntrega");
		return map;
	}

	private void putIfNotNull(Object object, TypedFlatMap map, String key) {
		if (object != null) {
			map.put(key, object);
		}
	}

	@GET
	@Path("findExigencias")
	public Response findExigencias() {
		List<ExigenciaGerRisco> list = exigenciaGerRiscoService.findByTipoExigenciaGerRisco(getIdTipoExigenciaEscolta());
		return Response.ok(SolicitacaoEscoltaDTOBuilder.convertExigenciaGerRiscoDTO(list)).build();
	}

	private List<Long> getIdTipoExigenciaEscolta() {
		String parametro = (String) parametroGeralService.findConteudoByNomeParametro("SGR_ID_TIPO_EXIGENCIA_ESCOLTA", false);
		List<Long> ids = new ArrayList<Long>();
		for (String string : parametro.split("\\D+")) {
			ids.add(Long.valueOf(string));
		}
		return ids;
	}

	@POST
	@Path("findHistorico")
	public Response findHistorico(Long id) {
		List<SolicEscoltaHistorico> list = solicitacaoEscoltaService.findSolicEscoltaHistorico(id);
		return getReturnFind(SolicitacaoEscoltaDTOBuilder.convertSolicEscoltaHistoricoDTO(list), list.size());
	}

}
