package com.mercurio.lms.rest.coleta.programacaocoletasveiculos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.EquipeOperacaoService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.IntegranteEqOperacService;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.EventoColetaService;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.coleta.report.RelatorioEventosColetaService;
import com.mercurio.lms.coleta.report.RelatorioPedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt;
import com.mercurio.lms.municipios.model.service.RegiaoFilialRotaColEntService;
import com.mercurio.lms.rest.coleta.dto.OcorrenciaColetaDTO;
import com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto.ColetasEntregasDTO;
import com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto.DadosColetaDTO;
import com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto.DetalhesColetaDTO;
import com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto.EventosColetaDTO;
import com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto.ProgramacaoColetasVeiculosDTO;
import com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto.ProgramacaoColetasVeiculosFilterDTO;
import com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto.ProgramacaoColetasVeiculosRetornoExecutarDTO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/coleta/programacaoColetasVeiculos")
public class ProgramacaoColetasVeiculosRest extends BaseCrudRest<ProgramacaoColetasVeiculosDTO, ProgramacaoColetasVeiculosDTO, ProgramacaoColetasVeiculosFilterDTO> {

	@InjectInJersey
	private ControleCargaService controleCargaService;

	@InjectInJersey
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;

	@InjectInJersey
	private EquipeOperacaoService equipeOperacaoService;

	@InjectInJersey
	private IntegranteEqOperacService integranteEqOperaService;

	@InjectInJersey
	private PedidoColetaService pedidoColetaService;

	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;

	@InjectInJersey
	private RegiaoFilialRotaColEntService regiaoFilialRotaColEntService;

	@InjectInJersey
	private EventoColetaService eventoColetaService;

	@InjectInJersey
	private DetalheColetaService detalheColetaService;

	@InjectInJersey
	private AwbService awbService;

	@InjectInJersey
	private RelatorioPedidoColetaService relatorioPedidoColetaService;

	@InjectInJersey
	private ReportExecutionManager reportExecutionManager;

	@InjectInJersey
	private RelatorioEventosColetaService relatorioEventosColetaService;

	@InjectInJersey
	private OcorrenciaColetaService ocorrenciaColetaService;

	@InjectInJersey
	private EventoControleCargaService eventoControleCargaService;

	@Override
	protected ProgramacaoColetasVeiculosDTO findById(Long id) {
		ControleCarga cc = controleCargaService.findById(id);
		ProgramacaoColetasVeiculosDTO programacaoColetasVeiculosDTO = getProgramacaoColetasVeiculosDTO(cc);
		return programacaoColetasVeiculosDTO;
	}

	private ProgramacaoColetasVeiculosDTO getProgramacaoColetasVeiculosDTO(ControleCarga cc) {
		ProgramacaoColetasVeiculosDTO programacaoColetasVeiculosDTO = new ProgramacaoColetasVeiculosDTO();
		MeioTransporte meioTransporteTransportado = cc.getMeioTransporteByIdTransportado();
		programacaoColetasVeiculosDTO.setId(cc.getIdControleCarga());
		programacaoColetasVeiculosDTO.setNrControleCarga(cc.getNrControleCarga());
		programacaoColetasVeiculosDTO.setPcOcupacaoInformado(cc.getPcOcupacaoInformado());
		programacaoColetasVeiculosDTO.setVlTotalFrota(cc.getVlTotalFrota());
		programacaoColetasVeiculosDTO.setVlColetado(cc.getVlColetado());
		programacaoColetasVeiculosDTO.setVlAColetar(cc.getVlAColetar());
		programacaoColetasVeiculosDTO.setVlAEntregar(cc.getVlAEntregar());
		programacaoColetasVeiculosDTO.setVlEntregue(cc.getVlEntregue());
		programacaoColetasVeiculosDTO.setPsTotalFrota(cc.getPsTotalFrota());
		programacaoColetasVeiculosDTO.setPsColetado(cc.getPsColetado());
		programacaoColetasVeiculosDTO.setPsAColetar(cc.getPsAColetar());
		programacaoColetasVeiculosDTO.setPsAEntregar(cc.getPsAEntregar());
		programacaoColetasVeiculosDTO.setPsEntregue(cc.getPsEntregue());

		programacaoColetasVeiculosDTO.setSgFilial(cc.getFilialByIdFilialOrigem().getSgFilial());

		if (meioTransporteTransportado != null) {
			programacaoColetasVeiculosDTO.setNrFrota(meioTransporteTransportado.getNrFrota());
			programacaoColetasVeiculosDTO.setNrIdentificador(meioTransporteTransportado.getNrIdentificador());
			programacaoColetasVeiculosDTO.setNrCapacidadeKg(meioTransporteTransportado.getNrCapacidadeKg());
			programacaoColetasVeiculosDTO.setDsTipoMeioTransporte(meioTransporteTransportado.getModeloMeioTransporte().getTipoMeioTransporte().getDsTipoMeioTransporte());
		}
		return programacaoColetasVeiculosDTO;
	}

	@POST
	@Path("transmitirColetas")
	public Response transmitirColetas(ProgramacaoColetasVeiculosFilterDTO filter){
		List<Long> listIdsPedido = filter.getIdsPedido();
		Map mapRetorno = new TypedFlatMap();

		Long idControleCarga = filter.getId();
		Long idMeioTransporte = filter.getIdMeioTransporte();

		for (Long idPedidoColeta : listIdsPedido) {
			try {
				pedidoColetaService.generateTransmissaoColetaByLiberacaoGerRisco(idControleCarga, idMeioTransporte, idPedidoColeta, false);
			} catch (BusinessException be) {
				tratarBusinessExcpColeta(mapRetorno, idPedidoColeta, be);
			}
		}
		return Response.ok(mapRetorno).build();
	}

	private void tratarBusinessExcpColeta(Map mapRetorno, Long idPedidoColeta, BusinessException be) {
		PedidoColeta pedidoColeta = pedidoColetaService.findById(idPedidoColeta);

		TypedFlatMap mapPedidoCol = new TypedFlatMap();
		mapPedidoCol.put("message", configuracoesFacade.getMensagem(be.getMessageKey()));
		mapPedidoCol.put("nrColeta",pedidoColeta.getNrColeta());
		mapPedidoCol.put("sgFilial",pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());		

		mapRetorno.put(idPedidoColeta, mapPedidoCol);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map getCriteriaProgamacaoColetasNaoProgramadas(ProgramacaoColetasVeiculosFilterDTO filter) {
		Map criteria = new TypedFlatMap();
		Usuario usuario = SessionUtils.getUsuarioLogado();
		
		Filial filialUsuario = SessionUtils.getFilialSessao();
		criteria.put("tpStatusColeta", "AB");
		criteria.put("dhPedidoColeta", JTDateTimeUtils.getDataHoraAtual());
		criteria.put("tpModoPedidoColeta", "AU");
		criteria.put("filialByIdFilialResponsavel.idFilial", filialUsuario.getIdFilial());
		
		criteria.put("idUsuario", usuario.getIdUsuario());
		if (filter.getRotaColetaEntregaFiltroColetas() != null){
			criteria.put("idRotaColetaEntrega", filter.getRotaColetaEntregaFiltroColetas().getIdRotaColetaEntrega());	
		}
		
		return criteria;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@POST
	@Path("findProgamacaoColetasNaoProgramadas")
	public Response findProgamacaoColetasNaoProgramadas(ProgramacaoColetasVeiculosFilterDTO filter) {
		Map criteria =  getCriteriaProgamacaoColetasNaoProgramadas(filter);
		List listColetasNaoProgramadas = pedidoColetaService.findPaginatedByColetasNaoProgramadas(criteria);
		
		for (Iterator iter = listColetasNaoProgramadas.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			map.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao((String) map.get("tpIdentificacao"), (String) map.get("nrIdentificacao")));
			map.put("coleta", map.get("sgFilial").toString() + " " +map.get("nrColeta").toString());
			
			map.put("blProdutoPerigoso", Boolean.TRUE.equals(map.get("blProdutoPerigoso")) ? configuracoesFacade.getMensagem("sim") : configuracoesFacade.getMensagem("nao"));
			
			map.remove("tpIdentificacao");
		}

		return getReturnFind(listColetasNaoProgramadas, listColetasNaoProgramadas.size());
	}
	
	
	@SuppressWarnings("rawtypes")
	@POST
	@Path("findProgamacaoColetasVeiculos")
	public Response findProgamacaoColetasVeiculos(ProgramacaoColetasVeiculosFilterDTO filter) {
		
		ResultSetPage rsp = controleCargaService.findPaginatedByProgramacaoColetasVeiculos(filter.getIdRotaColetaEntregaSuggest(), filter.getIdMeioTransporteSuggest(), FindDefinition.createFindDefinition(getTypedFlatMapWithPaginationInfo(filter)));
		
		List<ProgramacaoColetasVeiculosDTO> listProgramacaoVeiculos = new ArrayList<ProgramacaoColetasVeiculosDTO>();

		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			TypedFlatMap tfm = (TypedFlatMap) iter.next();
			ProgramacaoColetasVeiculosDTO programacaoColetasVeiculosDTO = parseToProgramacaoColetasVeiculosDTO(tfm);			
			listProgramacaoVeiculos.add(programacaoColetasVeiculosDTO);
		}
		return getReturnFind(listProgramacaoVeiculos, countProgramacaoColetasVeiculos(filter));
	}

private ProgramacaoColetasVeiculosDTO parseToProgramacaoColetasVeiculosDTO(TypedFlatMap tfm) {
		
		ProgramacaoColetasVeiculosDTO programacaoColetasVeiculosDTO = new ProgramacaoColetasVeiculosDTO();
		
		programacaoColetasVeiculosDTO.setNrRota(tfm.getShort("rotaColetaEntrega.nrRota"));
		programacaoColetasVeiculosDTO.setDsRota(tfm.getShort("rotaColetaEntrega.nrRota") + " " + tfm.getString("rotaColetaEntrega.dsRota"));		
		programacaoColetasVeiculosDTO.setFrotaPlaca(tfm.getString("meioTransporteByIdTransportado.nrFrota") + "  /  " + tfm.getString("meioTransporteByIdTransportado.nrIdentificador"));
		programacaoColetasVeiculosDTO.setId(tfm.getLong("idControleCarga"));
		programacaoColetasVeiculosDTO.setIdMeioTransporte(tfm.getLong("meioTransporteByIdTransportado.idMeioTransporte"));
		programacaoColetasVeiculosDTO.setNrFrota(tfm.getString("meioTransporteByIdTransportado.nrFrota"));
		programacaoColetasVeiculosDTO.setNrIdentificador(tfm.getString("meioTransporteByIdTransportado.nrIdentificador"));
		programacaoColetasVeiculosDTO.setSgFilial(tfm.getString("filialByIdFilialOrigem.sgFilial"));
		programacaoColetasVeiculosDTO.setNrControleCarga(tfm.getLong("nrControleCarga"));
		programacaoColetasVeiculosDTO.setSgFilial(tfm.getString("filialByIdFilialOrigem.sgFilial"));
		programacaoColetasVeiculosDTO.setSgFilialControleCarga(tfm.getString("filialByIdFilialOrigem.sgFilial") + " " + tfm.getLong("nrControleCarga").toString());
		programacaoColetasVeiculosDTO.setNrCapacidadeKg(tfm.getBigDecimal("meioTransporteByIdTransportado.nrCapacidadeKg"));
		programacaoColetasVeiculosDTO.setSiglaSimbolo1(tfm.getString("siglaSimbolo1"));
		programacaoColetasVeiculosDTO.setVlTotalFrota(tfm.getBigDecimal("vlTotalFrota"));
		programacaoColetasVeiculosDTO.setSiglaSimbolo2(tfm.getString("siglaSimbolo2"));
		programacaoColetasVeiculosDTO.setVlAColetar(tfm.getBigDecimal("vlAColetar"));
		programacaoColetasVeiculosDTO.setPsTotalFrota(tfm.getBigDecimal("psTotalFrota"));
		programacaoColetasVeiculosDTO.setPsAColetar(tfm.getBigDecimal("psAColetar"));
		programacaoColetasVeiculosDTO.setPcOcupacaoInformado(tfm.getBigDecimal("pcOcupacaoInformado"));
		programacaoColetasVeiculosDTO.setDsTipoMeioTransporte(tfm.getString("meioTransporteByIdTransportado.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte"));
		
		return programacaoColetasVeiculosDTO;
	}

	private Integer countProgramacaoColetasVeiculos(ProgramacaoColetasVeiculosFilterDTO filter) {
		return controleCargaService.getRowCountByProgramacaoColetasVeiculos(filter.getIdMeioTransporteSuggest(), filter.getIdRotaColetaEntregaSuggest());
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Path("findColetasPendentes")
	public Response findColetasPendentes(ProgramacaoColetasVeiculosFilterDTO filter){
		
		ResultSetPage rsp = pedidoColetaService.findPaginatedColetasPendentes(filter.getId(), null);
		
		List<ColetasEntregasDTO> listColetasPendentes = new ArrayList<ColetasEntregasDTO>();

		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			PedidoColeta pedidoColeta = (PedidoColeta)iter.next();
			ColetasEntregasDTO coletasEntregasDTO = parseToColetasEntregasDTOPendentes(pedidoColeta);
			listColetasPendentes.add(coletasEntregasDTO);
		}

		return getReturnFind(listColetasPendentes, getRowCountColetasPendentes(filter));
	}

	private ColetasEntregasDTO parseToColetasEntregasDTOPendentes(PedidoColeta pedidoColeta) {
		ColetasEntregasDTO coletasEntregasDTO = new ColetasEntregasDTO();
		coletasEntregasDTO.setIdPedidoColeta(pedidoColeta.getIdPedidoColeta());
		coletasEntregasDTO.setSgFilial(pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
		coletasEntregasDTO.setNrColeta(pedidoColeta.getNrColeta());

		if (pedidoColeta.getCliente() != null) {
			coletasEntregasDTO.setNrIdentificacaoFormatado(FormatUtils.formatIdentificacao(pedidoColeta.getCliente().getPessoa())); 
			coletasEntregasDTO.setCliente(pedidoColeta.getCliente().getPessoa().getNmPessoa());
		}
		coletasEntregasDTO.setEnderecoComComplemento(pedidoColeta.getEnderecoComComplemento());
		coletasEntregasDTO.setQtTotalVolumesVerificado(pedidoColeta.getQtTotalVolumesVerificado());
		coletasEntregasDTO.setPsTotalVerificado(pedidoColeta.getPsTotalVerificado());    	
		coletasEntregasDTO.setVlTotalVerificado(pedidoColeta.getVlTotalVerificado());

		String strHorarioColeta = 
			JTFormatUtils.format(pedidoColeta.getDhColetaDisponivel(), JTFormatUtils.SHORT, JTFormatUtils.TIMEOFDAY) 
			+ " " + configuracoesFacade.getMensagem("ate") + " " + JTFormatUtils.format(pedidoColeta.getHrLimiteColeta(), JTFormatUtils.SHORT);

		coletasEntregasDTO.setStrHorarioColeta(strHorarioColeta);
		coletasEntregasDTO.setTpModoPedidoColeta(pedidoColeta.getTpModoPedidoColeta().getDescriptionAsString());
		
		return coletasEntregasDTO;
	}
	
	private Integer getRowCountColetasPendentes(ProgramacaoColetasVeiculosFilterDTO filter) {
		Long idControleCarga = filter.getId();
		return pedidoColetaService.getRowCountColetasPendentes(idControleCarga);
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Path("findEntregasRealizar")
	public Response findEntregasRealizar(ProgramacaoColetasVeiculosFilterDTO filter){
		ResultSetPage rsp = manifestoEntregaDocumentoService.findPaginatedEntregasRealizar(filter.getId(), SessionUtils.getFilialSessao().getIdFilial(), FindDefinition.createFindDefinition(getTypedFlatMapWithPaginationInfo(filter)));
		List<ColetasEntregasDTO> listaColetasEntregas = new ArrayList<ColetasEntregasDTO>();

		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			TypedFlatMap tfm = (TypedFlatMap)iter.next();
			ColetasEntregasDTO coletasEntregasDTO = parseColetasEntregasDTORealizar(filter, tfm); 			
			listaColetasEntregas.add(coletasEntregasDTO);
		}

		return getReturnFind(listaColetasEntregas, getRowCountEntregasRealizar(filter));
	}

	private ColetasEntregasDTO parseColetasEntregasDTORealizar(ProgramacaoColetasVeiculosFilterDTO filter, TypedFlatMap tfm) {
		ColetasEntregasDTO coletasEntregasDTO = new ColetasEntregasDTO();
			
		String tpIdentificacaoValue = tfm.getString("doctoServico.clienteByIdClienteDestinatario.pessoa.tpIdentificacao.value");
		if (tpIdentificacaoValue != null) { 
			coletasEntregasDTO.setNrIdentificacaoFormatado(FormatUtils.formatIdentificacao(tpIdentificacaoValue, tfm.getString("doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao")));
		}

		coletasEntregasDTO.setIdControleCarga(filter.getId());
		coletasEntregasDTO.setTpDocumentoServico(tfm.getVarcharI18n("doctoServico.tpDocumentoServico.description").toString());
		coletasEntregasDTO.setSgFilialOrigem(tfm.getString("doctoServico.filialByIdFilialOrigem.sgFilial"));
		coletasEntregasDTO.setNrDoctoServico(coletasEntregasDTO.getTpDocumentoServico()+ "  " + coletasEntregasDTO.getSgFilialOrigem()+ " " +tfm.getLong("nrDoctoServico").toString());
		coletasEntregasDTO.setDtPrevEntrega(tfm.getYearMonthDay("doctoServico.dtPrevEntrega")); //verificar
		coletasEntregasDTO.setNmPessoaDestinatario(tfm.getString("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa"));
		coletasEntregasDTO.setDsEnderecoEntregaReal(tfm.getString("doctoServico.dsEnderecoEntregaReal"));
		coletasEntregasDTO.setQtVolumes(tfm.getInteger("qtVolumes"));
		coletasEntregasDTO.setPsDoctoServico(tfm.getBigDecimal("psDoctoServico"));
		coletasEntregasDTO.setVlTotalDocServico(tfm.getBigDecimal("vlTotalDocServico"));
		coletasEntregasDTO.setSituacaoDoctoServico(tfm.getString("situacaoDoctoServico"));
		
		return coletasEntregasDTO;
	}
	
	private Integer getRowCountEntregasRealizar(ProgramacaoColetasVeiculosFilterDTO filter) {
		Long idControleCarga = filter.getId();
		return manifestoEntregaDocumentoService.getRowCountEntregasRealizar(idControleCarga,SessionUtils.getFilialSessao().getIdFilial());
	}


	@SuppressWarnings("rawtypes")
	@POST
	@Path("findColetasRealizadas")
	public Response findColetasRealizadas(ProgramacaoColetasVeiculosFilterDTO filter){

		List list = pedidoColetaService.findPaginatedByColetasVeiculosRealizadas(filter.getId());
		List<ColetasEntregasDTO> listColetasVeiculosEntregasRealizadas = new ArrayList<ColetasEntregasDTO>();

		for (Iterator iter = list.iterator(); iter.hasNext();) {
			PedidoColeta pedidoColeta = (PedidoColeta)iter.next();
			ColetasEntregasDTO coletasEntregasDTO = parseToColetasEntregasDTORealizadas(filter, pedidoColeta);
			listColetasVeiculosEntregasRealizadas.add(coletasEntregasDTO);
		}

		return getReturnFind(listColetasVeiculosEntregasRealizadas, getRowCountColetasVeiculosRealizadas(filter));    	
	}

	private ColetasEntregasDTO parseToColetasEntregasDTORealizadas(ProgramacaoColetasVeiculosFilterDTO filter, PedidoColeta pedidoColeta) {
		ColetasEntregasDTO coletasEntregasDTO = new ColetasEntregasDTO();
		coletasEntregasDTO.setIdPedidoColeta(pedidoColeta.getIdPedidoColeta());
		coletasEntregasDTO.setSgFilial(pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
		coletasEntregasDTO.setNrColeta(pedidoColeta.getNrColeta());
		coletasEntregasDTO.setNrIdentificacaoFormatado(FormatUtils.formatIdentificacao(pedidoColeta.getCliente().getPessoa()));
		coletasEntregasDTO.setCliente(pedidoColeta.getCliente().getPessoa().getNmPessoa());
		coletasEntregasDTO.setEnderecoComComplemento( pedidoColeta.getEnderecoComComplemento());
		coletasEntregasDTO.setPsTotalVerificado(pedidoColeta.getPsTotalVerificado());
		coletasEntregasDTO.setVlTotalVerificado(pedidoColeta.getVlTotalVerificado());
		coletasEntregasDTO.setQtTotalVolumesVerificado(pedidoColeta.getQtTotalVolumesVerificado());
		coletasEntregasDTO.setTpModoPedidoColeta(pedidoColeta.getTpModoPedidoColeta().getDescriptionAsString());
		coletasEntregasDTO.setIdControleCarga(filter.getId());
		return coletasEntregasDTO;
	} 
	
	private Integer getRowCountColetasVeiculosRealizadas(ProgramacaoColetasVeiculosFilterDTO filter) {
		Long idControleCarga = filter.getId();
		return pedidoColetaService.getRowCountByColetasVeiculosRealizadas(idControleCarga);
	}


	@SuppressWarnings("rawtypes")
	@POST
	@Path("findEntregasRealizadas")
	public Response findEntregasRealizadas(ProgramacaoColetasVeiculosFilterDTO filter) {

		ResultSetPage rsp = manifestoEntregaDocumentoService.findPaginatedEntregasRealizadasByProgramacaoColetas(filter.getId(), FindDefinition.createFindDefinition(getTypedFlatMapWithPaginationInfo(new ProgramacaoColetasVeiculosFilterDTO())));
		
		List<ColetasEntregasDTO> listColetasVeiculosEntregasRealizadas = new ArrayList<ColetasEntregasDTO>();

		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			TypedFlatMap tfm = (TypedFlatMap) iter.next();
			
			ColetasEntregasDTO coletasEntregasDTO = parseToColetasEntregasDTOEntregasRealizadas(tfm, filter);
			listColetasVeiculosEntregasRealizadas.add(coletasEntregasDTO);
		}

		return getReturnFind(listColetasVeiculosEntregasRealizadas, getRowCountEntregasRealizadasByProgramacaoColetas(filter.getId()));
	}

	private ColetasEntregasDTO parseToColetasEntregasDTOEntregasRealizadas(TypedFlatMap tfm, ProgramacaoColetasVeiculosFilterDTO filter) {
		ColetasEntregasDTO coletasEntregasDTO = new ColetasEntregasDTO();
		String tpIdentificacaoValue = tfm.getString("doctoServico.clienteByIdClienteDestinatario.pessoa.tpIdentificacao.value");
		if (tpIdentificacaoValue != null) {
			coletasEntregasDTO.setNrIdentificacaoFormatado(FormatUtils.formatIdentificacao(tpIdentificacaoValue,
				tfm.getString("doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao")));
		}
		coletasEntregasDTO.setIdControleCarga(filter.getId());
		coletasEntregasDTO.setTpDocumentoServico(tfm.getVarcharI18n("doctoServico.tpDocumentoServico.description").toString());
		coletasEntregasDTO.setSgFilialOrigem(tfm.getString("doctoServico.filialByIdFilialOrigem.sgFilial"));
		coletasEntregasDTO.setNrDoctoServico(coletasEntregasDTO.getTpDocumentoServico()+ "  " + coletasEntregasDTO.getSgFilialOrigem()+ " " +tfm.getLong("nrDoctoServico").toString());
		coletasEntregasDTO.setDtPrevEntrega(tfm.getYearMonthDay("doctoServico.dtPrevEntrega")); // verificar
		coletasEntregasDTO.setNmPessoaDestinatario(tfm.getString("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa"));
		coletasEntregasDTO.setDsEnderecoEntregaReal(tfm.getString("doctoServico.dsEnderecoEntregaReal"));
		coletasEntregasDTO.setQtVolumes(tfm.getInteger("qtVolumes"));
		coletasEntregasDTO.setPsDoctoServico(tfm.getBigDecimal("psDoctoServico"));
		coletasEntregasDTO.setVlTotalDocServico(tfm.getBigDecimal("vlTotalDocServico"));
		coletasEntregasDTO.setSituacaoDoctoServico(tfm.getString("situacaoDoctoServico"));
		coletasEntregasDTO.setDhEvento(tfm.getDateTime("doctoServico.eventoDocumentoServico.dhEvento"));
		
		return coletasEntregasDTO;
	}
	public Integer getRowCountEntregasRealizadasByProgramacaoColetas(Long idControleCarga) {
		return manifestoEntregaDocumentoService.getRowCountEntregasRealizadasByProgramacaoColetas(idControleCarga);
	}



	@POST
	@Path("consultarColetasDadosColeta")
	public DadosColetaDTO consultarColetasDadosColeta(Long idPedidoColeta) {
		PedidoColeta pedidoColeta = pedidoColetaService.findById(idPedidoColeta);
		DadosColetaDTO dadosColetaDTO = parsePedidoColetaToDadosColetaDTO(pedidoColeta);
		return dadosColetaDTO;
	}

	private DadosColetaDTO parsePedidoColetaToDadosColetaDTO(PedidoColeta pedidoColeta) {
		DadosColetaDTO dadosColetaDTO = new DadosColetaDTO();
		
		dadosColetaDTO.setId(pedidoColeta.getIdPedidoColeta());
		dadosColetaDTO.setNrColeta(pedidoColeta.getNrColeta());

		setFilialDadosColeta(pedidoColeta, dadosColetaDTO);
		
		dadosColetaDTO.setDhPedidoColeta(pedidoColeta.getDhPedidoColeta());
		dadosColetaDTO.setNmSolicitante(pedidoColeta.getUsuario().getNmUsuario());

		if (pedidoColeta.getCliente() != null) {
			dadosColetaDTO.setCliente_nrIdentificacaoFormatado(FormatUtils.formatIdentificacao(pedidoColeta.getCliente().getPessoa()));
			dadosColetaDTO.setCliente_nmPessoa(pedidoColeta.getCliente().getPessoa().getNmPessoa());
		}
		dadosColetaDTO.setEnderecoComComplemento(pedidoColeta.getEnderecoComComplemento());

		if (pedidoColeta.getMunicipio() != null) {
			dadosColetaDTO.setNmMunicipio(pedidoColeta.getMunicipio().getNmMunicipio());
		}
		dadosColetaDTO.setBlProdutoDiferenciado(pedidoColeta.getBlProdutoDiferenciado());
		dadosColetaDTO.setNrCep(pedidoColeta.getNrCep());
		dadosColetaDTO.setTpStatusColeta_description(pedidoColeta.getTpStatusColeta().getDescription().toString());
		dadosColetaDTO.setTpPedidoColeta_description(pedidoColeta.getTpPedidoColeta().getDescription().toString());
		dadosColetaDTO.setTpModoPedidoColeta_description(pedidoColeta.getTpModoPedidoColeta().getDescription().toString());
		dadosColetaDTO.setDhColetaDisponivel(pedidoColeta.getDhColetaDisponivel());
		dadosColetaDTO.setDtPrevisaoColeta(pedidoColeta.getDtPrevisaoColeta());
		dadosColetaDTO.setHrLimiteColeta(pedidoColeta.getHrLimiteColeta().toDateTimeToday());
		dadosColetaDTO.setNrTelefoneCliente(pedidoColeta.getNrTelefoneCliente());
		dadosColetaDTO.setNmContatoCliente(pedidoColeta.getNmContatoCliente());

		if (pedidoColeta.getRotaColetaEntrega() != null) {
			dadosColetaDTO.setDsRota(pedidoColeta.getRotaColetaEntrega().getDsRota());
		}
		
		dadosColetaDTO.setQtTotalVolumesVerificado(pedidoColeta.getQtTotalVolumesVerificado());
		dadosColetaDTO.setPsTotalInformado(pedidoColeta.getPsTotalInformado());
		dadosColetaDTO.setPsTotalVerificado(pedidoColeta.getPsTotalVerificado());
		dadosColetaDTO.setVlTotalVerificado(pedidoColeta.getVlTotalVerificado());

		if (pedidoColeta.getManifestoColeta() != null) {
			setManifestoColetaDadosColeta(pedidoColeta, dadosColetaDTO);
		}
		dadosColetaDTO.setObPedidoColeta(pedidoColeta.getObPedidoColeta());
		dadosColetaDTO.setSgFilialPedidoColeta(dadosColetaDTO.getFilialByIdFilialResponsavel_sgFilial());

		setRegiaoDadosColeta(pedidoColeta, dadosColetaDTO);
		setObservacaoCancelamentoDadosColeta(pedidoColeta, dadosColetaDTO);
		
		return dadosColetaDTO;
	}

	private void setManifestoColetaDadosColeta(PedidoColeta pedidoColeta, DadosColetaDTO dadosColetaDTO) {
		dadosColetaDTO.setManifestoColeta_nrManifesto(pedidoColeta.getManifestoColeta().getNrManifesto());
		dadosColetaDTO.setManifestoColeta_sgFilial(pedidoColeta.getManifestoColeta().getFilial().getSgFilial());

		if (pedidoColeta.getManifestoColeta().getControleCarga() != null) {
			dadosColetaDTO.setIdControleCarga(pedidoColeta.getManifestoColeta().getControleCarga().getIdControleCarga());
			dadosColetaDTO.setNrControleCarga(pedidoColeta.getManifestoColeta().getControleCarga().getNrControleCarga());
			dadosColetaDTO.setFilialByIdFilialOrigem_sgFilial(pedidoColeta.getManifestoColeta().getControleCarga().getFilialByIdFilialOrigem().getSgFilial());

			if (pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado() != null) {
				dadosColetaDTO.setNrIdentificador(pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado().getNrIdentificador());
				dadosColetaDTO.setNrFrota(pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado().getNrFrota());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void setObservacaoCancelamentoDadosColeta(PedidoColeta pedidoColeta, DadosColetaDTO dadosColetaDTO) {
		StringBuilder obsCancelamento = new StringBuilder();
		if (pedidoColeta.getTpStatusColeta().getValue().equals("CA")) {
			List<EventoColeta> eventosColeta = eventoColetaService.findEventoColetaByIdPedidoColeta(pedidoColeta.getIdPedidoColeta(), "CA");
			for (EventoColeta evento : eventosColeta) {
				obsCancelamento.append(evento.getDsDescricao()).append(" ");
			}
		}
		dadosColetaDTO.setObsCancelamento(obsCancelamento.toString());
	}

	private void setRegiaoDadosColeta(PedidoColeta pedidoColeta, DadosColetaDTO dadosColetaDTO) {
		Map criteriosRegiao = new HashMap();
		criteriosRegiao.put("idRotaColetaEntrega", pedidoColeta.getRotaColetaEntrega().getIdRotaColetaEntrega());
		criteriosRegiao.put("dtVigente", JTDateTimeUtils.getDataAtual());
		List listaRegiaoColetaEntregaFil = regiaoFilialRotaColEntService.findByVigencia(criteriosRegiao);
		if (!listaRegiaoColetaEntregaFil.isEmpty()) {
			RegiaoFilialRotaColEnt regiaoFilialRotaColEnt = (RegiaoFilialRotaColEnt) listaRegiaoColetaEntregaFil.get(0);
			dadosColetaDTO.setDsRegiaoColetaEntregaFil(regiaoFilialRotaColEnt.getRegiaoColetaEntregaFil().getDsRegiaoColetaEntregaFil());
		}
	}

	private void setFilialDadosColeta(PedidoColeta pedidoColeta, DadosColetaDTO dadosColetaDTO) {
		if (pedidoColeta.getFilialByIdFilialResponsavel() != null) {
			dadosColetaDTO.setFilialByIdFilialResponsavel_sgFilial(pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
			dadosColetaDTO.setFilialByIdFilialResponsavel_nmFantasia(pedidoColeta.getFilialByIdFilialResponsavel().getPessoa().getNmFantasia());
		}
		if (pedidoColeta.getFilialByIdFilialSolicitante() != null) {
			dadosColetaDTO.setFilialByIdFilialSolicitante_sgFilial(pedidoColeta.getFilialByIdFilialSolicitante().getSgFilial());
			dadosColetaDTO.setFilialByIdFilialSolicitante_nmFantasia(pedidoColeta.getFilialByIdFilialSolicitante().getPessoa().getNmFantasia());
		}
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Path("findDetalhesColeta")
	public Response findDetalhesColeta(ProgramacaoColetasVeiculosFilterDTO filter) {
		ResultSetPage rsp = detalheColetaService.findPaginatedByDetalhesColeta(filter.getIdPedidoColeta(), FindDefinition.createFindDefinition(getTypedFlatMapWithPaginationInfo(filter)));
		
		List<DetalhesColetaDTO> listDetalheColeta = new ArrayList<DetalhesColetaDTO>();

		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			TypedFlatMap map = (TypedFlatMap) iter.next();
			DetalhesColetaDTO detalhesColetaDTO = parseToDetalhesColetaDTO(map);
			listDetalheColeta.add(detalhesColetaDTO);
		}

		return getReturnFind(listDetalheColeta, getRowCountDetalhesColeta(filter));
	}

	private DetalhesColetaDTO parseToDetalhesColetaDTO(TypedFlatMap map) {
		DetalhesColetaDTO detalhesColetaDTO = new DetalhesColetaDTO();
		detalhesColetaDTO.setNmMunicipio(map.getString("municipio.nmMunicipio"));
		detalhesColetaDTO.setSgFilial(map.getString("filial.sgFilial"));
		detalhesColetaDTO.setDsNaturezaProduto(map.get("naturezaProduto.dsNaturezaProduto").toString());
		detalhesColetaDTO.setTpIdentificacao(map.getString("cliente.pessoa.tpIdentificacao"));
		detalhesColetaDTO.setNmPessoa(map.getString("cliente.pessoa.nmPessoa"));
		detalhesColetaDTO.setDsServico(map.getVarcharI18n("servico.dsServico").toString());
		detalhesColetaDTO.setTpFrete(map.getString("tpFrete"));
		detalhesColetaDTO.setPsAforado(map.getBigDecimal("psAforado"));
		detalhesColetaDTO.setQtVolumes(map.getInteger("qtVolumes"));
		detalhesColetaDTO.setVlMercadoria(map.getBigDecimal("vlMercadoria"));
		detalhesColetaDTO.setSgFilialOrigemDocto(map.getString("sgFilialOrigemDocto"));
		detalhesColetaDTO.setNrDoctoServico(map.getInteger("nrDoctoServico"));
		detalhesColetaDTO.setCotacao_sgFilial(map.getString("cotacao.filial.sgFilial"));
		detalhesColetaDTO.setNrCotacao(map.getInteger("cotacao.nrCotacao"));
		detalhesColetaDTO.setCtoInternacional_sgPais(map.getString("ctoInternacional.sgPais"));
		detalhesColetaDTO.setCtoInternacional_nrCrt(map.getInteger("ctoInternacional.nrCrt"));
		detalhesColetaDTO.setObPedidoColeta(map.getString("pedidoColeta.obPedidoColeta"));

		String nrIdentificacao = map.getString("cliente.pessoa.nrIdentificacao");
		if (nrIdentificacao != null && !nrIdentificacao.equals("")) {
			detalhesColetaDTO.setNrIdentificacaoFormatado(FormatUtils.formatIdentificacao(map.getString("cliente.pessoa.tpIdentificacao.value"), nrIdentificacao));
		}

		Long idDoctoServico = map.getLong("idDoctoServico");
		if (idDoctoServico != null) {
			detalhesColetaDTO.setAwb(awbService.findPreAwbAwbByIdDoctoServico(idDoctoServico));
		}
		return detalhesColetaDTO;
	}

	private Integer getRowCountDetalhesColeta(ProgramacaoColetasVeiculosFilterDTO filter) {
		return detalheColetaService.getRowCountByDetalhesColeta(filter.getIdPedidoColeta());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@POST
	@Path("consultaEventosColeta")
	public Response consultaEventosColeta(ProgramacaoColetasVeiculosFilterDTO filter) {
		Map criteria = getTypedFlatMapWithPaginationInfo(filter);
		criteria.put("pedidoColeta.idPedidoColeta", filter.getIdPedidoColeta());
		ResultSetPage rsp = eventoColetaService.findPaginated(criteria);

		List<EventosColetaDTO> eventosColetaDTOList = new ArrayList<EventosColetaDTO>();
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			EventoColeta eventoColeta = (EventoColeta) iter.next();			
			EventosColetaDTO eventoColetaDTO = parseToEventosColetaDTO(eventoColeta);
			eventosColetaDTOList.add(eventoColetaDTO);
		}
		return getReturnFind(eventosColetaDTOList, getRowCountConsultaEventosColeta(filter));
	}

	private EventosColetaDTO parseToEventosColetaDTO(EventoColeta eventoColeta) {
		EventosColetaDTO eventoColetaDTO = new EventosColetaDTO();
		eventoColetaDTO.setId(eventoColeta.getIdEventoColeta());
		eventoColetaDTO.setDhEvento(eventoColeta.getDhEvento());
		eventoColetaDTO.setTpEventoColeta(eventoColeta.getTpEventoColeta());


		if (eventoColeta.getMeioTransporteRodoviario() != null) {
			eventoColetaDTO.setNrFrota(eventoColeta.getMeioTransporteRodoviario().getMeioTransporte().getNrFrota());
		}
		eventoColetaDTO.setDsDescricaoCompleta(eventoColeta.getOcorrenciaColeta().getDsDescricaoCompleta().toString());
		eventoColetaDTO.setNmUsuario(eventoColeta.getUsuario().getNmUsuario());
		return eventoColetaDTO;
	}

	private Integer getRowCountConsultaEventosColeta(ProgramacaoColetasVeiculosFilterDTO filter) {
		return eventoColetaService.getRowCount(filter.getIdPedidoColeta());

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@POST
	@Path("visualizaRelatorioPedidoColeta")
	public Response visualizaRelatorioPedidoColeta(Long idPedidoColeta) throws Exception {

		Map criteria = new TypedFlatMap();
		criteria.put("idPedidoColeta", idPedidoColeta.toString());

		File report = reportExecutionManager.executeReport(relatorioPedidoColetaService, criteria);
		String reportLocator = reportExecutionManager.generateReportLocator(report);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("reportLocator", reportLocator);

		return Response.ok(retorno).build();

	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@POST
	@Path("visualizaRelatorioEventosColeta")
	public Response visualizaRelatorioEventosColeta(Long idPedidoColeta) throws Exception {

		PedidoColeta pedidoColeta = pedidoColetaService.findById(idPedidoColeta);
		Map parameters = new TypedFlatMap();
		Map mapPedidoColeta = new TypedFlatMap();
		Map mapFilial = new TypedFlatMap();

		mapPedidoColeta.put("idPedidoColeta",idPedidoColeta.toString());
		mapFilial.put("idFilial", pedidoColeta.getFilialByIdFilialResponsavel().getIdFilial().toString());
		mapPedidoColeta.put("filialByIdFilialResponsavel", mapFilial);
		parameters.put("tpFormatoRelatorio", "pdf");
		parameters.put("pedidoColeta", mapPedidoColeta);
		File report = reportExecutionManager.executeReport(relatorioEventosColetaService, parameters);
		String reportLocator = reportExecutionManager.generateReportLocator(report);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("reportLocator", reportLocator);

		return Response.ok(retorno).build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@POST
	@Path("exportExcelEventosColeta")
	public Response exportExcelEventosColeta(Long idPedidoColeta) throws Exception{
		PedidoColeta pedidoColeta = pedidoColetaService.findById(idPedidoColeta);
		Map parameters = new TypedFlatMap();
		Map mapPedidoColeta = new TypedFlatMap();
		Map mapFilial = new TypedFlatMap();

		mapPedidoColeta.put("idPedidoColeta",idPedidoColeta.toString());
		mapFilial.put("idFilial", pedidoColeta.getFilialByIdFilialResponsavel().getIdFilial().toString());
		mapPedidoColeta.put("filialByIdFilialResponsavel", mapFilial);
		parameters.put("tpFormatoRelatorio", "csv");
		parameters.put("pedidoColeta", mapPedidoColeta);

		File report = reportExecutionManager.executeReport(relatorioEventosColetaService, parameters);
		String reportLocator = reportExecutionManager.generateReportLocator(report);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("reportLocator", reportLocator);

		return Response.ok(retorno).build();
	}

	


	@SuppressWarnings("rawtypes")
	@POST
	@Path("retornarColeta")
	public Response retornarColeta(ProgramacaoColetasVeiculosRetornoExecutarDTO retornoExecutarDTO) {	
		try {
			confirmarDataHoraEvento(retornoExecutarDTO);
		} catch (BusinessException be) {
			Map mapException = tratarBusinessExceptionRetornoColeta(be);
			return Response.ok(mapException).build();
		}
		return generateRetornarColeta(retornoExecutarDTO);
	}

	private void confirmarDataHoraEvento(ProgramacaoColetasVeiculosRetornoExecutarDTO retornoExecutarDTO) {
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("idPedidoColeta", Long.valueOf(retornoExecutarDTO.getIdsPedidoColeta().get(0)));
		parameters.put("dhLiberacao", retornoExecutarDTO.getDtHoraOcorrencia());
		parameters.put("idControleCarga", retornoExecutarDTO.getIdControleCarga());
		
		pedidoColetaService.confirmarDataHoraEvento(parameters);
	}
	@SuppressWarnings("rawtypes")
	@POST
	@Path("executarColeta")
	public Response executarColetaConfirmarDataHoraEvento(ProgramacaoColetasVeiculosRetornoExecutarDTO retornoExecutarDTO) {
		try {
			confirmarDataHoraEvento(retornoExecutarDTO);
		} catch (BusinessException be) {
			Map mapException = tratarBusinessExceptionRetornoColeta(be);
			return Response.ok(mapException).build();
		}
		return Response.ok().build();
	}
	
	@SuppressWarnings("rawtypes")
	@POST
	@Path("generateExecutarColetasPendentes")
	public Response generateExecutarColetasPendentes(ProgramacaoColetasVeiculosRetornoExecutarDTO retornoExecutarDTO) {
		List<Long> ids = new ArrayList<Long>();
		
		for (String idStr : retornoExecutarDTO.getIdsPedidoColeta()) {
			ids.add(Long.valueOf(idStr));
		}
		
		String retorno = pedidoColetaService.generateExecutarColetasPendenteNovaUI(ids, retornoExecutarDTO.getDtHoraOcorrencia());
		if (retorno.length() > 0){
			Map mapExcp = tratarBusinessExceptionRetornoColeta(new BusinessException("LMS-02022", new Object[] {retorno}));
			return Response.ok(mapExcp).build();
		}
		return Response.ok().build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map tratarBusinessExceptionRetornoColeta(BusinessException be) {
		Map mapRetorno = new TypedFlatMap();
		String mensagem = null;
		if(be.getMessageArguments()!=null){
			mensagem = configuracoesFacade.getMensagem(be.getMessageKey(),be.getMessageArguments());
		}else{
			mensagem = configuracoesFacade.getMensagem(be.getMessageKey());
		}
		
		mapRetorno.put("businessMsg", mensagem);
		return mapRetorno;
	}

	private Response generateRetornarColeta(ProgramacaoColetasVeiculosRetornoExecutarDTO retornoDTO) {
		TypedFlatMap criteria = getCriteriaRetornarColeta(retornoDTO);
		pedidoColetaService.generateRetornarColeta(criteria);
		return Response.ok().build();
	}

	private TypedFlatMap getCriteriaRetornarColeta(ProgramacaoColetasVeiculosRetornoExecutarDTO retornoDTO) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idsPedidoColeta.ids", retornoDTO.getIdsPedidoColeta());
		criteria.put("dtHoraOcorrencia", JTDateTimeUtils.formatDateTimeToString(retornoDTO.getDtHoraOcorrencia(), "yyyy-MM-dd HH:mm:ss"));
		criteria.put("ocorrenciaColeta.blIneficienciaFrota", retornoDTO.getBlIneficienciaFrota().getValue());
		criteria.put("ocorrenciaColeta.idOcorrenciaColeta", retornoDTO.getIdOcorrenciaColeta().toString());
		criteria.put("dsDescricao", retornoDTO.getDsDescricao());
		criteria.put("meioTransporte.idMeioTransporte", retornoDTO.getIdMeioTransporte());
		return criteria;
	}

	
	@POST
	@Path("findPedidoColetaById")
	public Response findPedidoColetaById(Long idPedidoColeta){
		PedidoColeta pedidoColeta = pedidoColetaService.findById(idPedidoColeta);

		ColetasEntregasDTO coletaEntregaDTO = new ColetasEntregasDTO();
		coletaEntregaDTO.setNrColeta(pedidoColeta.getNrColeta());
		coletaEntregaDTO.setSgFilial(pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());

		return Response.ok(coletaEntregaDTO).build();
	}
	
	
	
	@POST
	@Path("getDadosModalRetornarColeta")
	public Response dadosModalRetornarColeta(){
		Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
    	TypedFlatMap criteria = new TypedFlatMap(); 	
    	criteria.put("nmUsuario", usuarioLogado.getNmUsuario());
		criteria.put("ocorrencias", getListOcorrenciasDTO());
		criteria.put("dtHoraOcorrencia", JTDateTimeUtils.getDataHoraAtual());
   
    	return Response.ok(criteria).build();
	}

	private List<OcorrenciaColetaDTO> getListOcorrenciasDTO() {
		List<OcorrenciaColeta> listOcorrenciaColeta = ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColetaRetornoColeta();
		List<OcorrenciaColetaDTO> listOcorrenciaColetaDTO = new ArrayList<OcorrenciaColetaDTO>();
		for (OcorrenciaColeta ocorrencia : listOcorrenciaColeta) {
			listOcorrenciaColetaDTO.add(new OcorrenciaColetaDTO(ocorrencia.getIdOcorrenciaColeta(), ocorrencia.getCodigo(), ocorrencia.getDsDescricaoCompleta().getValue()));
		}
		return listOcorrenciaColetaDTO;
	}
	
	@POST
	@Path("getDadosModalExecutarColeta")
	public Response dadosModalExecutarColeta(){
		Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
    	TypedFlatMap criteria = new TypedFlatMap();
    	criteria.put("nmUsuario", usuarioLogado.getNmUsuario());
		criteria.put("dtHoraOcorrencia", JTDateTimeUtils.getDataHoraAtual());
   
    	return Response.ok(criteria).build();
	}

	@Override
	protected Long store(ProgramacaoColetasVeiculosDTO bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void removeById(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		// TODO Auto-generated method stub
	}
	

	@Override
	protected List<ProgramacaoColetasVeiculosDTO> find(ProgramacaoColetasVeiculosFilterDTO filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Integer count(ProgramacaoColetasVeiculosFilterDTO filter) {
		// TODO Auto-generated method stub
		return null;
	}
}