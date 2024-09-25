package com.mercurio.lms.rest.coleta.pedidocoleta;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.coleta.model.AwbColeta;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.LocalidadeEspecial;
import com.mercurio.lms.coleta.model.NotaFiscalColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.dto.ConsultarColetaDTO;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.coleta.model.util.TpStatusColetaConstants;
import com.mercurio.lms.coleta.report.RelatorioPedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.model.service.RotaIntervaloCepService;
import com.mercurio.lms.rest.coleta.dto.OcorrenciaColetaDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.AwbDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.ColetaAutomaticaDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.DadosClientesColetaDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.EnderecosColetaDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.NotaFiscalColetaDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.PedidoColetaDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.PedidoColetaDetalheColetaDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.PedidoColetaFilterDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.PedidoColetaListDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.ProibidoEmbarqueDTO;
import com.mercurio.lms.rest.configuracoes.ServicoChosenDTO;
import com.mercurio.lms.rest.expedicao.DoctoServicoSuggestDTO;
import com.mercurio.lms.rest.expedicao.dto.NaturezaProdutoChosenDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.LocalidadeEspecialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.MunicipioFilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.ColetaAutomaticaCliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.ProibidoEmbarque;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.ColetaAutomaticaClienteService;
import com.mercurio.lms.vendas.model.service.ProibidoEmbarqueService;

@Path("/coleta/pedidocoleta/pedidoColeta")
public class PedidoColetaRest extends BaseCrudRest<PedidoColetaDTO, PedidoColetaListDTO, PedidoColetaFilterDTO> {

	private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");

	@InjectInJersey
	PedidoColetaService pedidoColetaService;

	@InjectInJersey
	EnderecoPessoaService enderecoPessoaService;

	@InjectInJersey
	RotaIntervaloCepService rotaIntervaloCepService;

	@InjectInJersey
	ColetaAutomaticaClienteService coletaAutomaticaClienteService;

	@InjectInJersey
	DetalheColetaService detalheColetaService;

	@InjectInJersey
	AwbService awbService;

	@InjectInJersey
	ProibidoEmbarqueService proibidoEmbarqueService;

	@InjectInJersey
	OcorrenciaColetaService ocorrenciaColetaService;

	@InjectInJersey
	private ConversaoMoedaService conversaoMoedaService;

	@InjectInJersey
	private RotaColetaEntregaService rotaColetaEntregaService;

	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;

	@InjectInJersey
	private ClienteService clienteService;

	@InjectInJersey
	private TelefoneEnderecoService telefoneEnderecoService;

	@InjectInJersey
	private MoedaService moedaService;

	@InjectInJersey
	private ReportExecutionManager reportExecutionManager;

	@InjectInJersey
	private RelatorioPedidoColetaService relatorioPedidoColetaService;

	@InjectInJersey
	private MunicipioFilialService municipioFilialService;

	@InjectInJersey
	private ConhecimentoService conhecimentoService;

	private static final String TP_ENDERECO_COLETA = "COL";

	private ConsultarColetaDTO populateConsultarColetaDTO(PedidoColetaFilterDTO filter) {
		ConsultarColetaDTO consultarColetaDTO = new ConsultarColetaDTO();

		if (filter.getFilial() == null) {
			return null;
		}

		consultarColetaDTO.setIdFilial(filter.getFilial().getIdFilial());
		consultarColetaDTO.setIdRotaColetaEntrega(filter.getRotaColetaEntrega() == null ? null : filter.getRotaColetaEntrega().getIdRotaColetaEntrega());
		consultarColetaDTO.setIdRegiaoColetaEntregaFil(filter.getRegiaoColetaEntrega() == null ? null : filter.getRegiaoColetaEntrega().getIdRegiaoColetaEntregaFil());
		consultarColetaDTO.setIdServico(filter.getServico() == null ? null : filter.getServico().getIdServico());
		consultarColetaDTO.setIdCliente(filter.getCliente() == null ? null : filter.getCliente().getIdCliente());
		consultarColetaDTO.setIdFilialDestino(filter.getDestino() == null ? null : filter.getDestino().getIdFilial());
		consultarColetaDTO.setIdDestino(filter.getDestino() == null ? null : filter.getDestino().getIdFilial());

		consultarColetaDTO.setIdUsuario(filter.getUsuario() == null ? null : filter.getUsuario().getIdUsuario());
		consultarColetaDTO.setNrColeta(filter.getNrColeta());

		consultarColetaDTO.setDhPedidoColetaInicial(filter.getDtPedidoColetaInicial());
		consultarColetaDTO.setDhPedidoColetaFinal(filter.getDtPedidoColetaFinal());

		consultarColetaDTO.setBlSemVinculoDoctoServico(filter.isSemVinculo());
		consultarColetaDTO.setTpPedidoColeta(filter.getTipoColeta() == null ? null : filter.getTipoColeta().getValue());

		/*
		 * Define tipos de status da coleta.
		 */
		List<String> listStatus = new ArrayList<String>();
		addTpColeta(listStatus, filter.isAberto(), TpStatusColetaConstants.EM_ABERTO);
		addTpColeta(listStatus, filter.isTransmitida(), TpStatusColetaConstants.TRANSMITIDA);
		addTpColeta(listStatus, filter.isManifestada(), TpStatusColetaConstants.MANIFESTADA);
		addTpColeta(listStatus, filter.isExecutada(), TpStatusColetaConstants.EXECUTADA);
		addTpColeta(listStatus, filter.isCancelada(), TpStatusColetaConstants.CANCELADA);
		addTpColeta(listStatus, filter.isAguardandoDescarga(), TpStatusColetaConstants.AGUARDANDO_DESCARGA);
		addTpColeta(listStatus, filter.isEmDescarga(), TpStatusColetaConstants.EM_DESCARGA);
		addTpColeta(listStatus, filter.isColetaTerminal(), TpStatusColetaConstants.NO_TERMINAL);
		addTpColeta(listStatus, filter.isFinalizada(), TpStatusColetaConstants.FINALIZADA);
		addTpColeta(listStatus, filter.isNoManifesto(), TpStatusColetaConstants.NO_MANIFESTO);

		consultarColetaDTO.setTpsStatusColeta(listStatus);

		return consultarColetaDTO;
	}

	private void addTpColeta(List<String> listStatus, boolean selecionado, TpStatusColetaConstants tpStatusColeta) {
		if (selecionado) {
			listStatus.add(tpStatusColeta.getValue());
		}
	}

	@GET
	@Path("testeteste")
	public void teste() {
		System.out.println("funciona");
	}
	@POST
	@Path("findDadosClientesColeta")
	public Response findDadosClientesColeta(PedidoColetaFilterDTO filter) {
		TypedFlatMap criteria = getTypedFlatMapWithPaginationInfo(filter);
		criteria.put("cliente.idCliente", filter.getIdCliente());

		ResultSetPage rsp = pedidoColetaService.findPaginatedClientesColeta(null, filter.getIdCliente(), null, null, null, null, criteria);
		Iterator iterator = rsp.getList().iterator();
		Map map = (Map) iterator.next();

		DadosClientesColetaDTO dadosClientesColetaDTO = new DadosClientesColetaDTO();
		dadosClientesColetaDTO.setId(filter.getIdCliente());

		DateTime dhPedidoColeta = new DateTime(map.get("dhPedidoColeta"));
		dadosClientesColetaDTO.setDhPedidoColeta(dhPedidoColeta);
		dadosClientesColetaDTO.setNmPessoa(map.get("nmPessoaCliente").toString());
		dadosClientesColetaDTO.setNrColeta((Long) map.get("nrColeta"));
		DomainValue tpIdentificacao = (DomainValue) map.get("tpIdentificacaoCliente");
		dadosClientesColetaDTO.setNrIdentificacao(FormatUtils.formatIdentificacao(tpIdentificacao.getValue(), (String) map.get("nrIdentificacaoCliente")));
		dadosClientesColetaDTO.setSgFilial(map.get("sgFilial").toString());

		return Response.ok(dadosClientesColetaDTO).build();
	}

	@POST
	@Path("findEnderecosColeta")
	public Response findEnderecosColeta(PedidoColetaFilterDTO filter) {
		TypedFlatMap criteria = getTypedFlatMapWithPaginationInfo(filter);
		criteria.put("pessoa.idPessoa", filter.getIdCliente().toString());
		criteria.put("tpEndereco", TP_ENDERECO_COLETA);
		ResultSetPage rsp = enderecoPessoaService.findPaginated(criteria);

		List<EnderecosColetaDTO> listEnderecosColetaDTOs = new ArrayList<EnderecosColetaDTO>();
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			EnderecoPessoa enderecoPessoa = (EnderecoPessoa) iter.next();
			EnderecosColetaDTO enderecos = new EnderecosColetaDTO();
			enderecos.setId(enderecoPessoa.getIdEnderecoPessoa());
			enderecos.setDsEndereco(enderecoPessoa.getDsEndereco());
			enderecos.setNrEndereco(enderecoPessoa.getNrEndereco());
			enderecos.setDsComplemento(enderecoPessoa.getDsComplemento());
			enderecos.setDsBairro(enderecoPessoa.getDsBairro());
			enderecos.setNrCep(enderecoPessoa.getNrCep());
			enderecos.setNmMunicipio(enderecoPessoa.getMunicipio().getNmMunicipio());
			enderecos.setSgUnidadeFederativa(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());

			List rotaIntervaloList = rotaIntervaloCepService.findRotaIntervaloCepByCep(enderecoPessoa.getMunicipio().getIdMunicipio(), enderecoPessoa.getNrCep());
			if (rotaIntervaloList.size() > 0) {
				RotaIntervaloCep ric = (RotaIntervaloCep) rotaIntervaloList.get(0);
				enderecos.setDsRota(ric.getRotaColetaEntrega().getDsRota());
			}

			listEnderecosColetaDTOs.add(enderecos);
		}

		return getReturnFind(listEnderecosColetaDTOs, getRowCountEnderecosColeta(filter));

	}

	private Integer getRowCountEnderecosColeta(PedidoColetaFilterDTO filter) {
		TypedFlatMap criteria = getTypedFlatMapWithPaginationInfo(filter);
		criteria.put("pessoa.idPessoa", filter.getIdCliente().toString());
		criteria.put("tpEndereco", TP_ENDERECO_COLETA);

		return enderecoPessoaService.getRowCount(criteria);
	}

	@POST
	@Path("findColetasAutomatica")
	public Response findColetasAutomatica(PedidoColetaFilterDTO filter) {

		TypedFlatMap criteria = getTypedFlatMapWithPaginationInfo(filter);
		criteria.put("cliente.idCliente", filter.getIdCliente().toString());
		criteria.put("enderecoPessoa.idEnderecoPessoa", filter.getIdEnderecoPessoa());

		ResultSetPage rsp = coletaAutomaticaClienteService.findPaginated(criteria);
		List<ColetaAutomaticaDTO> listColetaAutomaticaDTOs = new ArrayList<ColetaAutomaticaDTO>();
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			ColetaAutomaticaDTO coletaAutomaticaDTO = new ColetaAutomaticaDTO();
			ColetaAutomaticaCliente coletaAutomaticaCliente = (ColetaAutomaticaCliente) iter.next();

			DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
			coletaAutomaticaDTO.setTpDiaSemana(coletaAutomaticaCliente.getTpDiaSemana().getDescriptionAsString());
			coletaAutomaticaDTO.setHrChegada(coletaAutomaticaCliente.getHrChegada().toString(fmt));
			coletaAutomaticaDTO.setHrSaida(coletaAutomaticaCliente.getHrSaida().toString(fmt));

			listColetaAutomaticaDTOs.add(coletaAutomaticaDTO);
		}

		return getReturnFind(listColetaAutomaticaDTOs, getRowCountColetasAutomatica(filter));

	}

	private Integer getRowCountColetasAutomatica(PedidoColetaFilterDTO filter) {
		TypedFlatMap criteria = getTypedFlatMapWithPaginationInfo(filter);
		return coletaAutomaticaClienteService.getRowCount(criteria);
	}

	@POST
	@Path("findAwbs")
	public Response findAwbs(PedidoColetaFilterDTO filter) {
		List<DetalheColeta> detalhes = detalheColetaService.findDetalheColeta(filter.getId());
		List<AwbDTO> listAwbs = new ArrayList<AwbDTO>();
		for (DetalheColeta detalheColeta : detalhes) {
			if (detalheColeta.getDoctoServico() != null) {
				final Awb awb = awbService.findUltimoAwbByDoctoServico(detalheColeta.getDoctoServico().getIdDoctoServico());

				Boolean exists = 0 < CollectionUtils.countMatches(listAwbs, new Predicate() {
					@Override
					public boolean evaluate(Object obj) {
						Map<String, Object> map = (Map<String, Object>) obj;
						return ((Long) map.get("idAwb")).equals(awb.getIdAwb());
					}
				});

				if (!exists) {
					AwbDTO awbDto = new AwbDTO();
					awbDto.setId(awb.getIdAwb());
					awbDto.setNrAWB(AwbUtils.getSgEmpresaAndNrAwbFormated(awb));
					listAwbs.add(awbDto);
				}
			}
		}

		return getReturnFind(listAwbs, listAwbs.size());

	}

	@POST
	@Path("findProibidoEmbarque")
	public Response findProibidoEmbarque(PedidoColetaFilterDTO filter) {
		List<ProibidoEmbarqueDTO> listProibidoEmbarqueDTO = new ArrayList<ProibidoEmbarqueDTO>();
		ResultSetPage rspProibidoEmbarque = proibidoEmbarqueService.findPaginatedByIdCliente(filter.getIdCliente(), FindDefinition.createFindDefinition(getTypedFlatMapWithPaginationInfo(filter)));
		for (int i = 0; i < rspProibidoEmbarque.getList().size(); i++) {
			ProibidoEmbarqueDTO proibidoEmbarqueDTO = new ProibidoEmbarqueDTO();
			ProibidoEmbarque proibidoEmbarque = (ProibidoEmbarque) rspProibidoEmbarque.getList().get(i);
			if (proibidoEmbarque.getDtDesbloqueio() == null) {
				proibidoEmbarqueDTO.setDsBloqueio(proibidoEmbarque.getDsBloqueio());
				proibidoEmbarqueDTO.setDsMotivoProibidoEmbarque(proibidoEmbarque.getMotivoProibidoEmbarque().getDsMotivoProibidoEmbarque().getValue().toString());
				listProibidoEmbarqueDTO.add(proibidoEmbarqueDTO);
			}
		}

		return getReturnFind(listProibidoEmbarqueDTO, listProibidoEmbarqueDTO.size());
	}

	@POST
	@Path("findOcorrenciaColetaLiberacaoCredito")
	public Response findOcorrenciaColetaLiberacaoCredito() {
		List<OcorrenciaColeta> listOcorrenciaColeta = ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColeta("LI");
		List<OcorrenciaColetaDTO> listOcorrenciaColetaDTO = new ArrayList<OcorrenciaColetaDTO>();
		for (OcorrenciaColeta ocorrencia : listOcorrenciaColeta) {
			listOcorrenciaColetaDTO.add(new OcorrenciaColetaDTO(ocorrencia.getIdOcorrenciaColeta(), ocorrencia.getCodigo(), ocorrencia.getDsDescricaoCompleta().getValue()));
		}

		return Response.ok(listOcorrenciaColetaDTO).build();
	}

	private PedidoColetaListDTO constroiPedidoColeta(Map map) {
		PedidoColetaListDTO dto = new PedidoColetaListDTO();
		dto.setId((Long) map.get("idPedidoColeta"));
		dto.setColeta(((String) map.get("sgFilial")) + " " + ((Long) map.get("nrColeta")));
		ClienteSuggestDTO cliente = new ClienteSuggestDTO();
		cliente.setId((Long) map.get("idCliente"));
		cliente.setNmPessoa((String) map.get("nmPessoa"));
		dto.setCliente(cliente);
		dto.setTipoColeta((DomainValue) map.get("tpPedidoColeta"));
		dto.setSolicitacao((DateTime) map.get("dhPedidoColeta"));
		dto.setPeso((BigDecimal) map.get("psTotalVerificado"));
		dto.setVolumes((Integer) map.get("qtTotalVolumesVerificado"));
		dto.setMoeda(((String) map.get("sgMoeda")) + " " + ((String) map.get("dsSimbolo")));
		dto.setValor((BigDecimal) map.get("vlTotalVerificado"));
		dto.setStatus((DomainValue) map.get("tpStatusColeta"));
		dto.setModoColeta((DomainValue) map.get("tpModoPedidoColeta"));
		dto.setFuncionario((String) map.get("nmUsuario"));
		return dto;
	}

	@POST
	@Path("inicializarCamposPedidoColetaList")
	public Response inicializarCamposPedidoColetaList() {
		PedidoColetaFilterDTO filter = new PedidoColetaFilterDTO();

		Filial filial = SessionUtils.getFilialSessao();
		filter.setFilial(new FilialSuggestDTO());
		filter.getFilial().setIdFilial(filial.getIdFilial());
		filter.getFilial().setSgFilial(filial.getSgFilial());
		filter.getFilial().setNmFilial(filial.getPessoa().getNmFantasia());

		DateTime dataHoraAtualMenos30Dias = JTDateTimeUtils.getDataHoraAtual();
		dataHoraAtualMenos30Dias = dataHoraAtualMenos30Dias.minusDays(30);
		filter.setDtPedidoColetaInicial(dataHoraAtualMenos30Dias);
		filter.setDtPedidoColetaFinal(JTDateTimeUtils.getDataHoraAtual());

		return Response.ok(filter).build();
	}

	@POST
	@Path("carregarGridDetalheColeta")
	public Response carregarGridDetalheColeta(PedidoColetaDTO pedidoColetaDTO) {
		if (pedidoColetaDTO.getDetalheColetasDTO() != null) {
			List<PedidoColetaDetalheColetaDTO> listPedidoColetaDetalheColetaDTO = pedidoColetaDTO.getDetalheColetasDTO();
			return getReturnFind(listPedidoColetaDetalheColetaDTO, listPedidoColetaDetalheColetaDTO.size());
		} else {
			return Response.ok().build();
		}
	}

	@Override
	protected PedidoColetaDTO findById(Long id) {
		PedidoColeta pedidoColeta = pedidoColetaService.findById(id);
		PedidoColetaParaDTOTransfomer transformer = PedidoColetaParaDTOTransfomer.getInstance();
		PedidoColetaDTO pedidoColetaDTO = transformer.transformaParaDTO(pedidoColeta, conversaoMoedaService, rotaColetaEntregaService, configuracoesFacade);
		pedidoColetaDTO.setDetalheColetasDTO(detalheToDetalheColetaDTO(pedidoColeta.getIdPedidoColeta()));
		return pedidoColetaDTO;
	}
	
	private DetalheColeta detalheColetaDTOToDetalheColeta(PedidoColetaDetalheColetaDTO dto, PedidoColeta pedidoColeta) {
		
		DetalheColeta detalheColeta = null;
		
		if (dto.getId() != null) {
			detalheColeta = detalheColetaService.findById(dto.getId());
		} else {
			detalheColeta = new DetalheColeta();
		}
		
		detalheColeta.setTpFrete(dto.getTpFrete());
		detalheColeta.setVlMercadoria(dto.getVlMercadoria());
		detalheColeta.setQtVolumes(dto.getQtVolumes());
		detalheColeta.setPsMercadoria(dto.getPsMercadoria());
		detalheColeta.setPsAforado(dto.getPsAforado());
		detalheColeta.setObDetalheColeta(dto.getObDetalheColeta());
		detalheColeta.setBlEntregaDireta(dto.isBlEntregaDireta());
		if (StringUtils.isNotBlank(dto.getNmDestinatario())) {
			detalheColeta.setNmDestinatario(dto.getNmDestinatario());
		}
		detalheColeta.setPedidoColeta(pedidoColeta);
		Servico servico = new Servico();
		servico.setIdServico(dto.getServico().getIdServico());
		detalheColeta.setServico(servico);
		NaturezaProduto naturezaProduto = new NaturezaProduto();
		naturezaProduto.setIdNaturezaProduto(dto.getNaturezaProduto().getId());
		detalheColeta.setNaturezaProduto(naturezaProduto);
		Moeda moeda = new Moeda(dto.getIdMoeda());
		detalheColeta.setMoeda(moeda);
		if (dto.getIdLocalidadeEspecial() != null) {
			LocalidadeEspecial localidadeEspecial = new LocalidadeEspecial();
			localidadeEspecial.setIdLocalidadeEspecial(dto.getIdLocalidadeEspecial());
			detalheColeta.setLocalidadeEspecial(localidadeEspecial);
		}
		if (dto.getIdMunicipio() != null) {
			Municipio municipio = new Municipio();
			municipio.setIdMunicipio(dto.getIdMunicipio());
			detalheColeta.setMunicipio(municipio);
		}
		if (dto.getIdFilial() != null) {
			Filial filial = new Filial(dto.getIdFilial());
			detalheColeta.setFilial(filial);
		}
		if (dto.getIdCliente() != null) {
			Cliente cliente = new Cliente(dto.getIdCliente());
			detalheColeta.setCliente(cliente);
		}
		if (dto.getIdCotacao() != null) {
			Cotacao cotacao = new Cotacao();
			cotacao.setIdCotacao(dto.getIdCotacao());
			detalheColeta.setCotacao(cotacao);
		}
		if (dto.getIdDoctoServicoCtoInternacional() != null) {
			CtoInternacional ctoInternacional = new CtoInternacional();
			ctoInternacional.setIdDoctoServico(dto.getIdDoctoServicoCtoInternacional());
			detalheColeta.setCtoInternacional(ctoInternacional);					
		}				
		
		detalheColeta.setNotaFiscalColetas(transformDtoParaNotaFiscalColeta(dto, detalheColeta));
		
		// Insere um Evento de Coleta para o Detalhe de Coleta.
		if(dto.getEventoColeta() != null && dto.getEventoColeta().getUsuario() != null && dto.getEventoColeta().getUsuario().getIdUsuario() != null
				&& dto.getEventoColeta().getOcorrenciaColeta() != null && dto.getEventoColeta().getOcorrenciaColeta().getIdOcorrenciaColeta() != null) {
			EventoColeta eventoColeta = new EventoColeta();
			eventoColeta.setPedidoColeta(detalheColeta.getPedidoColeta());
			
			
			Usuario usuario = new Usuario();
			usuario.setIdUsuario(dto.getEventoColeta().getUsuario().getIdUsuario());
			eventoColeta.setUsuario(usuario);

			
			OcorrenciaColeta ocorrenciaColeta = new OcorrenciaColeta();
			ocorrenciaColeta.setIdOcorrenciaColeta(dto.getEventoColeta().getOcorrenciaColeta().getIdOcorrenciaColeta());
			eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);

			eventoColeta.setDsDescricao(dto.getEventoColeta().getDsDescricao());
			eventoColeta.setDhEvento(JTDateTimeUtils.getDataHoraAtual());
			eventoColeta.setTpEventoColeta(new DomainValue("LI"));					
								
			detalheColeta.setEventoColeta(eventoColeta);
		}				

		return detalheColeta;
	}

	private List<NotaFiscalColeta> transformDtoParaNotaFiscalColeta(PedidoColetaDetalheColetaDTO dto, DetalheColeta detalheColeta) {
		List<NotaFiscalColetaDTO> listMapNotaFiscalColetas = dto.getNotaFiscalColetas();		
		List<NotaFiscalColeta> listNotaFiscalColetas = new ArrayList<NotaFiscalColeta>();
		if(listMapNotaFiscalColetas != null) {
			for (NotaFiscalColetaDTO notaFiscalColetaDTO : listMapNotaFiscalColetas) {
				NotaFiscalColeta notaFiscalColeta = new NotaFiscalColeta();
				Long idNotaFiscalColeta = notaFiscalColetaDTO.getIdNotaFiscalColeta();
				if(idNotaFiscalColeta != null && !(idNotaFiscalColeta.longValue() <= 0)) {
					notaFiscalColeta.setIdNotaFiscalColeta(idNotaFiscalColeta);	
				} else {
					notaFiscalColeta.setIdNotaFiscalColeta(null);
				}						
				notaFiscalColeta.setNrNotaFiscal(notaFiscalColetaDTO.getNrNotaFiscal());
				notaFiscalColeta.setNrChave(notaFiscalColetaDTO.getNrChave());
				notaFiscalColeta.setDetalheColeta(detalheColeta);
				
				listNotaFiscalColetas.add(notaFiscalColeta);
			}
		}
		return listNotaFiscalColetas;
	}

	private List<PedidoColetaDetalheColetaDTO> detalheToDetalheColetaDTO(Long idPedidoColeta) {
		List<DetalheColeta> detalhes = detalheColetaService.findDetalheColeta(idPedidoColeta);
		List<PedidoColetaDetalheColetaDTO> listDetalheColetaDTO = new ArrayList<PedidoColetaDetalheColetaDTO>();
		for (DetalheColeta detalheColeta : detalhes) {
			PedidoColetaDetalheColetaDTO detalheColetaDTO = new PedidoColetaDetalheColetaDTO();
			
			detalheColetaDTO.setId(detalheColeta.getIdDetalheColeta());
			detalheColetaDTO.setTpFrete(detalheColeta.getTpFrete());
			detalheColetaDTO.setTpFreteDescription(detalheColeta.getTpFrete().getDescription().getValue());
			detalheColetaDTO.setQtVolumes(detalheColeta.getQtVolumes());
			detalheColetaDTO.setVlMercadoria(detalheColeta.getVlMercadoria());
			detalheColetaDTO.setPsMercadoria(detalheColeta.getPsMercadoria());
			detalheColetaDTO.setPsAforado(detalheColeta.getPsAforado());
			detalheColetaDTO.setObDetalheColeta(detalheColeta.getObDetalheColeta());

			if (detalheColeta.getServico() != null) {
				
				ServicoChosenDTO servico = new ServicoChosenDTO();
				servico.setDsServico(detalheColeta.getServico().getDsServico().getValue());
				servico.setIdServico(detalheColeta.getServico().getIdServico());
				detalheColetaDTO.setServico(servico);
				
			}
			if (detalheColeta.getNaturezaProduto() != null) {
				NaturezaProdutoChosenDTO naturezaProduto = new NaturezaProdutoChosenDTO();
				naturezaProduto.setId(detalheColeta.getNaturezaProduto().getIdNaturezaProduto());
				naturezaProduto.setDsNaturezaProduto(detalheColeta.getNaturezaProduto().getDsNaturezaProduto().getValue());
				detalheColetaDTO.setNaturezaProduto(naturezaProduto);
			}
			if (detalheColeta.getLocalidadeEspecial() != null) {
				LocalidadeEspecialSuggestDTO localidadeEspecial = new LocalidadeEspecialSuggestDTO();
				localidadeEspecial.setId(detalheColeta.getLocalidadeEspecial().getIdLocalidadeEspecial());
				localidadeEspecial.setDsLocalidade(detalheColeta.getLocalidadeEspecial().getDsLocalidade().getValue());
				detalheColetaDTO.setLocalidadeEspecial(localidadeEspecial);
			}
			
			if (detalheColeta.getMunicipio() != null && detalheColeta.getFilial() != null) {
				MunicipioFilialSuggestDTO municipioFilial = new MunicipioFilialSuggestDTO();
				municipioFilial.setIdMunicipio(detalheColeta.getMunicipio().getIdMunicipio());
				municipioFilial.setNmMunicipio(detalheColeta.getMunicipio().getNmMunicipio());
				municipioFilial.setSgUnidadeFederativa(detalheColeta.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
				municipioFilial.setIdFilial(detalheColeta.getFilial().getIdFilial());
				municipioFilial.setSgFilial(detalheColeta.getFilial().getSgFilial());
				municipioFilial.setNmFantasia(detalheColeta.getFilial().getPessoa().getNmFantasia());
				detalheColetaDTO.setMunicipioFilial(municipioFilial);

			}
			
			
			
			if (detalheColeta.getCliente() != null) {
				ClienteSuggestDTO cliente = new ClienteSuggestDTO();
				cliente.setIdCliente(detalheColeta.getCliente().getIdCliente());
				cliente.setNrIdentificacao(detalheColeta.getCliente().getPessoa().getNrIdentificacao());
				cliente.setNmPessoa(detalheColeta.getCliente().getPessoa().getNmPessoa());
				detalheColetaDTO.setDestinatario(cliente);
			} else if (detalheColeta.getNmDestinatario() != null) {
				ClienteSuggestDTO cliente = new ClienteSuggestDTO();
				cliente.setNmPessoa(detalheColeta.getNmDestinatario());
				detalheColetaDTO.setDestinatario(cliente);
			}
			if (detalheColeta.getMoeda() != null) {
				detalheColetaDTO.setIdMoeda(detalheColeta.getMoeda().getIdMoeda());
				detalheColetaDTO.setDsSimboloMoeda(detalheColeta.getMoeda().getDsSimbolo());
				detalheColetaDTO.setSgMoeda(detalheColeta.getMoeda().getSgMoeda());
			}
			if (detalheColeta.getCotacao() != null) {
				detalheColetaDTO.setIdCotacao(detalheColeta.getCotacao().getIdCotacao());
			}

			if (detalheColeta.getDoctoServico() != null) {
				DoctoServicoSuggestDTO doctoServico = new DoctoServicoSuggestDTO();
				doctoServico.setIdDoctoServico(detalheColeta.getDoctoServico().getIdDoctoServico());
				doctoServico.setTpDoctoServico(detalheColeta.getDoctoServico().getTpDocumentoServico());
				Filial filialOrigem = detalheColeta.getDoctoServico().getFilialByIdFilialOrigem();
				FilialSuggestDTO filialOrigemDTO = new FilialSuggestDTO();
				filialOrigemDTO.setIdFilial(filialOrigem.getIdFilial());
				filialOrigemDTO.setSgFilial( filialOrigem.getSgFilial());
				doctoServico.setFilialOrigem(filialOrigemDTO);
				doctoServico.setNrDoctoServico(detalheColeta.getDoctoServico().getNrDoctoServico());
				doctoServico.setDsDoctoServico(ConhecimentoUtils.formatConhecimento(filialOrigemDTO.getSgFilial(), doctoServico.getNrDoctoServico()));
				
				detalheColetaDTO.setTpDoctoSgFilial(detalheColeta.getDoctoServico().getTpDocumentoServico().getValue() + " "
					+ doctoServico.getDsDoctoServico() );
				detalheColetaDTO.setAwbDs(awbService.findPreAwbAwbByIdDoctoServico(detalheColeta.getDoctoServico().getIdDoctoServico()));
				detalheColetaDTO.setDoctoServico(doctoServico);
			}

			if (detalheColeta.getCtoInternacional() != null) {
				detalheColetaDTO.setIdDoctoServicoCtoInternacional(detalheColeta.getCtoInternacional().getIdDoctoServico());
				detalheColetaDTO.setSgPaisCtoInternacional(detalheColeta.getCtoInternacional().getSgPais());
				detalheColetaDTO.setNrCrtCtoInternacional(detalheColeta.getCtoInternacional().getNrCrt());
			}

			if (detalheColeta.getAwbColetas() != null && !detalheColeta.getAwbColetas().isEmpty()) {
			
				AwbColeta awbColeta = (AwbColeta) detalheColeta.getAwbColetas().get(IntegerUtils.ZERO);
				Awb awb = awbService.findById(awbColeta.getAwb().getIdAwb());
			
				detalheColetaDTO.setIdEmpresa(awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa());
				if (awb.getTpStatusAwb().getValue().equals(ConstantesExpedicao.TP_STATUS_PRE_AWB)) {
				
					detalheColetaDTO.setIdPreAwb(awb.getIdAwb());
					detalheColetaDTO.setNmAeroporto(awb.getCiaFilialMercurio().getEmpresa().getPessoa().getNmPessoa());
				} else {
					detalheColetaDTO.setIdEmpresa(awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa());
					detalheColetaDTO.setIdAwb(awb.getIdAwb());
					detalheColetaDTO.setNrAwb(awb.getNrAwb());
				}
			}

			List<NotaFiscalColetaDTO> listNotaFiscalColetas = new ArrayList<NotaFiscalColetaDTO>();
			List<NotaFiscalColetaDTO> listNFe = new ArrayList<NotaFiscalColetaDTO>();

			for (int j = 0; j < detalheColeta.getNotaFiscalColetas().size(); j++) {
				NotaFiscalColeta notaFiscalColeta = (NotaFiscalColeta) detalheColeta.getNotaFiscalColetas().get(j);
				NotaFiscalColetaDTO mapNotaFiscalColeta = new NotaFiscalColetaDTO();

				if (notaFiscalColeta.getNrNotaFiscal() != null || notaFiscalColeta.getNrChave() != null) {
					mapNotaFiscalColeta.setIdNotaFiscalColeta(notaFiscalColeta.getIdNotaFiscalColeta());
				}

				if (notaFiscalColeta.getNrNotaFiscal() != null) {
					mapNotaFiscalColeta.setNrNotaFiscal(notaFiscalColeta.getNrNotaFiscal());
					listNotaFiscalColetas.add(mapNotaFiscalColeta);
				}

				if (notaFiscalColeta.getNrChave() != null) {
					mapNotaFiscalColeta.setNrChave(notaFiscalColeta.getNrChave());
					listNFe.add(mapNotaFiscalColeta);
				}
			}
			detalheColetaDTO.setNrChaveNfe(listNFe);
			detalheColetaDTO.setNotaFiscalColetas(listNotaFiscalColetas);
			if(detalheColeta.getBlEntregaDireta()!=null){
				detalheColetaDTO.setBlEntregaDireta(detalheColeta.getBlEntregaDireta());
			}

			listDetalheColetaDTO.add(detalheColetaDTO);
		}

		return listDetalheColetaDTO;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Long store(PedidoColetaDTO bean) {
		PedidoColetaParaDTOTransfomer transformer = PedidoColetaParaDTOTransfomer.getInstance();
		PedidoColeta pedidoColeta = transformer.transformaParaEntity(bean, pedidoColetaService, clienteService, telefoneEnderecoService);
		List<Long> detalhesParaRemover = new ArrayList<Long>();
		if (CollectionUtils.isEmpty(bean.getDetalheColetasDTO())) {
			throw new BusinessException("LMS-02008");
		}
		if (bean.getIdPedidoColeta() != null) {
			List<DetalheColeta> detalhesOriginais = detalheColetaService.findDetalheColeta(bean.getIdPedidoColeta());
			for (DetalheColeta detalhe : detalhesOriginais) {
				boolean achou = false;
				for (PedidoColetaDetalheColetaDTO detalheDTO : bean.getDetalheColetasDTO()) {
					if (detalheDTO.getId() != null && detalheDTO.getId().equals(detalhe.getIdDetalheColeta())) {
						achou = true;
					}
				}
				if (!achou) {
					detalhesParaRemover.add(detalhe.getIdDetalheColeta());
				}
			}
		}
		for (Long id : detalhesParaRemover) {
			detalheColetaService.removeById(id);
		}
		pedidoColeta.setDetalheColetas(new ArrayList<DetalheColeta>(bean.getDetalheColetasDTO().size()));
		Long idPedidoColeta = (Long) pedidoColetaService.store(pedidoColeta);
		for (PedidoColetaDetalheColetaDTO detalheDTO : bean.getDetalheColetasDTO()) {
			DetalheColeta detalheColeta = detalheColetaDTOToDetalheColeta(detalheDTO, pedidoColeta);
			detalheColetaService.store(detalheColeta);
			pedidoColeta.getDetalheColetas().add(detalheColeta);
		}
		return idPedidoColeta;
	}

	@Override
	protected void removeById(Long id) {
		pedidoColetaService.removeById(id);

	}

	@Override
	protected void removeByIds(List<Long> ids) {
		pedidoColetaService.removeByIds(ids);
	}

	@POST
	@Path("bloqueioCliente")
	public Response getBloqueioCliente(Long idCliente) {
		List listProibidoEmbarques = proibidoEmbarqueService.findProibidoEmbarqueByIdCliente(idCliente);
		for (Iterator iter = listProibidoEmbarques.iterator(); iter.hasNext();) {
			ProibidoEmbarque proibidoEmbarque = (ProibidoEmbarque) iter.next();
			// Se em algum registro a dtDesbloqueio é vazia é porque o cliente
			// está bloqueado por algum motivo.
			if (proibidoEmbarque.getDtDesbloqueio() == null) {
				return Response.ok(Boolean.TRUE).build();
			}
		}
		return Response.ok(Boolean.FALSE).build();
	}

	@Override
	protected List<PedidoColetaListDTO> find(PedidoColetaFilterDTO filter) {

		ResultSetPage resultSetPage = pedidoColetaService.findPaginatedConsultarColeta(populateConsultarColetaDTO(filter),
			FindDefinition.createFindDefinition(getTypedFlatMapWithPaginationInfo(filter)));

		List<PedidoColetaListDTO> list = new ArrayList<PedidoColetaListDTO>();
		for (Map map : (List<Map>) resultSetPage.getList()) {
			list.add(constroiPedidoColeta(map));
		}

		return list;
	}

	@POST
	@Path("verificaDataDisponibilidade")
	public Response getDataPrevisaoColeta(PedidoColetaDTO pedidoColeta) {

		DateTime dhColetaDisponivel = pedidoColeta.getDhColetaDisponivel();
		TimeOfDay horarioCorte = TimeOfDay.fromDateFields(dateTimeFormatter.parseDateTime(pedidoColeta.getHorarioCorte()).toDate());

		if (dhColetaDisponivel.getHourOfDay() > horarioCorte.getHourOfDay()) {
			dhColetaDisponivel = dhColetaDisponivel.plusDays(1);
		} else if (dhColetaDisponivel.getHourOfDay() == horarioCorte.getHourOfDay() && dhColetaDisponivel.getMinuteOfHour() > horarioCorte.getMinuteOfHour()) {
			dhColetaDisponivel = dhColetaDisponivel.plusDays(1);
		}

		return Response.ok(dhColetaDisponivel.toString()).build();
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

	@POST
	@Path("findMoeda")
	public Response findMoeda() {
		List retorno = new ArrayList();
		List listMoedas = moedaService.find(new TypedFlatMap());
		for (Iterator iter = listMoedas.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			Moeda moeda = (Moeda) iter.next();
			map.put("idMoeda", moeda.getIdMoeda());
			map.put("siglaSimbolo", moeda.getSiglaSimbolo());
			retorno.add(map);
		}
		return Response.ok(retorno).build();
	}

	/**
	 * Método que valida a vigencia do municipio selecionado.
	 * 
	 * @param criteria
	 * @return
	 */
	@POST
	@Path("validaVigenciaAtendimento")
	public Response validaVigenciaAtendimento(TypedFlatMap criteria) {
		try {
			municipioFilialService.validateVigenciaAtendimento(criteria.getLong("idMunicipioFilial"), JTDateTimeUtils.getDataAtual(), JTDateTimeUtils.getDataAtual());
		} catch (BusinessException e) {
			throw new BusinessException("LMS-29022");
		}

		return Response.ok(criteria).build();
	}

	@POST
	@Path("saveDetalheColetaValidateNF")
	public Response saveDetalheColetaValidateNF(PedidoColetaDetalheColetaDTO detalheColeta) {

		List<NotaFiscalColetaDTO> notasFiscais = detalheColeta.getNotaFiscalColetas();
		List<NotaFiscalColetaDTO> chavesNFe = detalheColeta.getNrChaveNfe();

		// Remove o map para que ao salvar detalhe ele seja refeito de acordo
		// com a tela, e suas modificações
		detalheColeta.setNotaFiscalColetas(null);

		if (notasFiscais != null) {
			List<NotaFiscalColetaDTO> notasFiscalColetas = new ArrayList<NotaFiscalColetaDTO>();

			for (NotaFiscalColetaDTO notaFiscal : notasFiscais) {
				NotaFiscalColetaDTO mapNotaFiscalColeta = new NotaFiscalColetaDTO();
				Integer nrNotaFiscal = notaFiscal.getNrNotaFiscal();
				boolean hasChave = false;

				if (chavesNFe != null) {
					for (NotaFiscalColetaDTO chaveNFe : chavesNFe) {
						if (chaveNFe.getNrChave().length() == 44) {
							Long nrNotaFromChaveNFe = Long.valueOf(chaveNFe.getNrChave().substring(25, 34));

							if (Long.valueOf(nrNotaFiscal).equals(nrNotaFromChaveNFe)) {
								if (chaveNFe.getIdNotaFiscalColeta() != null && chaveNFe.getIdNotaFiscalColeta() > 0) {
									mapNotaFiscalColeta.setIdNotaFiscalColeta(chaveNFe.getIdNotaFiscalColeta());
								} else {
									mapNotaFiscalColeta.setIdNotaFiscalColeta(null);
								}

								hasChave = true;
								mapNotaFiscalColeta.setNrNotaFiscal(nrNotaFiscal);
								mapNotaFiscalColeta.setNrChave(chaveNFe.getNrChave());
								notasFiscalColetas.add(mapNotaFiscalColeta);
								break;
							}
						}
					}
				}

				if (!hasChave) {
					mapNotaFiscalColeta.setNrNotaFiscal(nrNotaFiscal);
					notasFiscalColetas.add(mapNotaFiscalColeta);
				}
			}

			detalheColeta.setNotaFiscalColetas(notasFiscalColetas);
		}
		Long idDoctoServico = detalheColeta.getIdDoctoServico();
		if (LongUtils.hasValue(idDoctoServico)) {
			Boolean blEntregaDireta = detalheColeta.isBlEntregaDireta();
			validateEntregaDiretaClienteByNrDoctoServicoAndSgFilialOrigem(idDoctoServico, blEntregaDireta);
		}
		return Response.ok(detalheColeta).build();
	}

	private void validateEntregaDiretaClienteByNrDoctoServicoAndSgFilialOrigem(Long idConhecimento, Boolean blEntregaDireta) {
		Conhecimento c = conhecimentoService.findById(idConhecimento);
		detalheColetaService.validateEntregaDiretaCliente(c, blEntregaDireta);
	}

	@Override
	protected Integer count(PedidoColetaFilterDTO filter) {
		return pedidoColetaService.getRowCountConsultarColeta(populateConsultarColetaDTO(filter));
	}

}
