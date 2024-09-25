package com.mercurio.lms.services.statusMercadoria;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.DescargaManifesto;
import com.mercurio.lms.carregamento.model.Equipe;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.DescargaManifestoService;
import com.mercurio.lms.carregamento.model.service.EquipeOperacaoService;
import com.mercurio.lms.carregamento.model.service.EquipeService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.EventoManifestoService;
import com.mercurio.lms.carregamento.model.service.LacreControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.edi.model.ConhecimentoFedex;
import com.mercurio.lms.edi.model.service.ConhecimentoFedexService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.service.CIOTService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.portaria.model.service.NewInformarChegadaService;
import com.mercurio.lms.portaria.model.service.PortariaService;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.rest.portaria.chegadasaida.dto.FrotaPlacaChegadaSaidaSuggestDTO;
import com.mercurio.lms.rest.portaria.chegadasaida.dto.InformarChegadaSaidaDTO;
import com.mercurio.lms.rest.portaria.chegadasaida.dto.LacreDTO;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.vol.model.service.VolDadosSessaoService;

import br.com.tntbrasil.integracao.domains.fedex.RetornoAtualizaStatusMercadoriaFedexDTO;
import br.com.tntbrasil.integracao.domains.fedex.StatusMercadoriaFedexDTO;

@Path("statusMercadoria")
public class AtualizaStatusMercadoriaRest {

	@InjectInJersey
	private NewInformarChegadaService newInformarChegadaService;

	@InjectInJersey
	private PortariaService portariaService;

	@InjectInJersey
	private FilialService filialService;

	@InjectInJersey
	private ControleCargaService controleCargaService;

	@InjectInJersey
	private ParametroGeralService parametroGeralService;

	@InjectInJersey
	private ConteudoParametroFilialService conteudoParametroFilialService;

	@InjectInJersey
	private UsuarioService usuarioService;

	@InjectInJersey
	private PaisService paisService;

	@InjectInJersey
	private VolDadosSessaoService volDadosSessaoService;
	
	@InjectInJersey
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	
	@InjectInJersey
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	
	@InjectInJersey
	private ManifestoService manifestoService;
	
	@InjectInJersey
	private LacreControleCargaService lacreControleCargaService;
	
	@InjectInJersey
	private EventoManifestoService eventoManifestoService;
	
	@InjectInJersey
	private DoctoServicoService doctoServicoService;
	
	@InjectInJersey
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	
	@InjectInJersey
	private CarregamentoDescargaService carregamentoDescargaService; 
	
	@InjectInJersey
	private DescargaManifestoService descargaManifestoService;
	
	@InjectInJersey
	private EventoMeioTransporteService eventoMeioTransporteService;
	
	@InjectInJersey
	private EventoControleCargaService eventoControleCargaService; 
	
	@InjectInJersey
	private CIOTService ciotService;
	
	@InjectInJersey
	private ConhecimentoService conhecimentoService;
	
	@InjectInJersey
	private ConhecimentoFedexService conhecimentoFedexService; 
	
	@InjectInJersey
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	
	@InjectInJersey
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;

	@InjectInJersey
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	
	@InjectInJersey
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	
	@InjectInJersey
	private EquipeOperacaoService equipeOperacaoService;
	@InjectInJersey
	private ServicoService servicoService;
	
	@InjectInJersey
	private EmpresaService empresaService;
	
	@InjectInJersey
	private EquipeService equipeService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade; 
	
	@InjectInJersey
	private NaoConformidadeService naoConformidadeService;
	
	@POST
	@Path("/generateRncFalta")
	public Response generateRNCFedex(StatusMercadoriaFedexDTO statusMercadoriaFedexDTO) {
		RetornoAtualizaStatusMercadoriaFedexDTO retornoDto = new RetornoAtualizaStatusMercadoriaFedexDTO();

		retornoDto.setFileName(statusMercadoriaFedexDTO.getFileName());

		try {

			Filial filialFedex = conteudoParametroFilialService.findFilialByConteudoParametro("CD_FILIAL_FEDEX", statusMercadoriaFedexDTO.getFilialFedex());
			if (filialFedex == null) {
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-09147", new Object[] { statusMercadoriaFedexDTO.getFilialFedex() }));
				return Response.ok(retornoDto).build();
			}

			setFilialSessao(filialFedex);
			setEmpresaSessao(361l);
			retornoDto.setDestinatariosEmail(findDestinatariosEmailRetorno(filialFedex));
			
			DoctoServico doctoServico = (Conhecimento)monitoramentoDocEletronicoService.findDoctoServicoByNrChave(statusMercadoriaFedexDTO.getNrChave());
			if (doctoServico == null){
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-09154", new Object[] { statusMercadoriaFedexDTO.getNrChave()}));
				return Response.ok(retornoDto).build();
			}
			
			if (null == statusMercadoriaFedexDTO.getQtdeVolumesFaltantes() || statusMercadoriaFedexDTO.getQtdeVolumesFaltantes().compareTo(0) <= 0) {
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-12029"));
				return Response.ok(retornoDto).build();
			}
			boolean faltaTotal = statusMercadoriaFedexDTO.getQtdeVolumesFaltantes().compareTo(doctoServico.getQtVolumes()) >= 0;

			Long idDoctoServico = doctoServico.getIdDoctoServico();

			Long idDescricaoPadraoNc = null;
			Long idMotivoAberturaNc = null;
			Long idCausaNaoConformidade = null;
			if (faltaTotal) {
				idDescricaoPadraoNc = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("DESC_PAD_NC_FALTA_TOT", false)).longValue();
				idMotivoAberturaNc = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("MOT_ABERT_NC_TOTAL", false)).longValue();
				idCausaNaoConformidade = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("CAUSA_NC_TOTAL", false)).longValue();
			} else {
				idDescricaoPadraoNc = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("DESC_PAD_NC_FALTA_PARC", false)).longValue();
				idMotivoAberturaNc = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("MOT_ABERT_NC_PARCIAL", false)).longValue();
				idCausaNaoConformidade = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("CAUSA_NC_PARCIAL", false)).longValue();
			}

			Long idFilialByFilialResponsavel = filialFedex.getIdFilial();
			Long idMoeda = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("ID_MOEDA_REAL", false)).longValue();
			String dsOcorrenciaNc = (String) parametroGeralService.findConteudoByNomeParametro("MOT_RNC_FEDEX", false);
			BigDecimal vlOcorrenciaNc = doctoServico.getVlMercadoria().multiply(new BigDecimal(statusMercadoriaFedexDTO.getQtdeVolumesFaltantes())).divide(new BigDecimal(doctoServico.getQtVolumes()), 2, BigDecimal.ROUND_HALF_UP);
			Integer qtVolumes = statusMercadoriaFedexDTO.getQtdeVolumesFaltantes();
			String tpModal =  servicoService.findById(doctoServico.getServico().getIdServico()).getTpModal().getValue();
			Boolean blCaixaReaproveitada = Boolean.FALSE;

			Long idManifesto = null;
			Long idControleCarga = null;
			Long idEmpresa = null;
			String dsCaixaReaproveitada = null;
			String dsCausaNc = null;
			Long idClienteDestinatarioNc = null;
			Long idClienteRemetenteNc = null;
			List listaNotaOcorrenciaNcs = null;
			String dsMotivoAbertura = null;
			List itensNFe = null;
			Long idAwb = null;
			OcorrenciaNaoConformidade onc = null;
			String causadorRnc = null;
			DateTime dhEvento = getDateTime(statusMercadoriaFedexDTO.getDataHoraEvento(), filialFedex, "yyyyMMddHHmm");

			
			NaoConformidade naoConformidade = naoConformidadeService.findByIdDoctoServico(idDoctoServico);
			if (naoConformidade != null){
				throw new BusinessException("LMS-12030");
			}
			
			ocorrenciaNaoConformidadeService.storeRNC(idManifesto, idDoctoServico, idMotivoAberturaNc, idControleCarga, idEmpresa, idDescricaoPadraoNc, idFilialByFilialResponsavel,
					idCausaNaoConformidade, idMoeda, dsOcorrenciaNc, blCaixaReaproveitada, dsCaixaReaproveitada, dsCausaNc, vlOcorrenciaNc, qtVolumes, idClienteDestinatarioNc,
					idClienteRemetenteNc, listaNotaOcorrenciaNcs, dsMotivoAbertura, itensNFe, idAwb, tpModal, onc, causadorRnc, dhEvento);

		} catch (BusinessException be) {
			retornoDto.getMessageErrors().add(parametroGeralService.getMessage(be.getMessageKey(), be.getMessageArguments()));
		} catch (Exception e) {
			retornoDto.setDestinatariosEmail(null);
			retornoDto.getMessageErrors().add(e.getMessage());
		}

		return Response.ok(retornoDto).build();
	}
	
	@POST
	@Path("/generateCteFedex")
	public Response generateCteFedex(StatusMercadoriaFedexDTO statusMercadoriaFedexDTO) {
		RetornoAtualizaStatusMercadoriaFedexDTO retornoDto = new RetornoAtualizaStatusMercadoriaFedexDTO();
		
		retornoDto.setFileName(statusMercadoriaFedexDTO.getFileName());
		
		try {
			
			Conhecimento conhecimento  = (Conhecimento)monitoramentoDocEletronicoService.findDoctoServicoByNrChave(statusMercadoriaFedexDTO.getNrChave());
			if (conhecimento == null){
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-10088", new Object[] { statusMercadoriaFedexDTO.getNrChave()}));
				return Response.ok(retornoDto).build();
			}
			
			
			Filial filialFedex = filialService.findById(conhecimento.getFilialByIdFilialDestino().getIdFilial());
			setFilialSessao(filialFedex);
			retornoDto.setSgFilial(statusMercadoriaFedexDTO.getFilialFedex());
			retornoDto.setDestinatariosEmail(findDestinatariosEmailRetorno(filialFedex));
			retornoDto.setNrDocumento(statusMercadoriaFedexDTO.getNrDocumentoFedex());
			
			ConhecimentoFedex conhecimentoFedex = conhecimentoFedexService.findByFilialNrConhecimento(statusMercadoriaFedexDTO.getFilialFedex(), statusMercadoriaFedexDTO.getNrDocumentoFedex());
			if (conhecimentoFedex == null){
				conhecimentoFedex = new ConhecimentoFedex();
				conhecimentoFedex.setDoctoServico(conhecimento);
				conhecimentoFedex.setSiglaFilialOrigem(statusMercadoriaFedexDTO.getFilialFedex());
				conhecimentoFedex.setNumeroConhecimento(statusMercadoriaFedexDTO.getNrDocumentoFedex());
				conhecimentoFedex.setDataLogEDI(new Date());
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
				conhecimentoFedex.setDataEmissao(dateFormat.parse(statusMercadoriaFedexDTO.getDataHoraEvento()));
				conhecimentoFedexService.store(conhecimentoFedex);
				
				String cdOcorrencia = (String)parametroGeralService.findConteudoByNomeParametro("OCORRENCIA_EMISSAO_CTE_FDX", false);
				OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaPendenciaService.findOcorrenciaBloqueioFromOcorrenciaDoctoServicoEmAberto(conhecimento.getIdDoctoServico());
				if (ocorrenciaPendencia != null && cdOcorrencia.contains(ocorrenciaPendencia.getCdOcorrencia().toString())){
					BigDecimal cdOcorrenciaBloqueio = (BigDecimal)parametroGeralService.findConteudoByNomeParametroWithoutException("OCORRENCIA_LIBERACAO_FEDEX", false);
			        OcorrenciaPendencia ocorrencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf(cdOcorrenciaBloqueio.toString()));
			        ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(conhecimento.getIdDoctoServico(), ocorrencia.getIdOcorrenciaPendencia(), null, JTDateTimeUtils.getDataHoraAtual(), null);
				}
				
			}
			
		} catch (BusinessException be) {
			retornoDto.getMessageErrors().add(parametroGeralService.getMessage(be.getMessageKey(), be.getMessageArguments()));
		} catch (Exception e) {
			retornoDto.setDestinatariosEmail(null);
			retornoDto.getMessageErrors().add(e.getMessage());
		}
		
		return Response.ok(retornoDto).build();
		
	}
	

	@POST
	@Path("/generateStatusChegadaPortaria")
	public Response generateStatusChegadaPortaria(StatusMercadoriaFedexDTO statusMercadoriaFedexDTO) {
		RetornoAtualizaStatusMercadoriaFedexDTO retornoDto = new RetornoAtualizaStatusMercadoriaFedexDTO();
		
		retornoDto.setFileName(statusMercadoriaFedexDTO.getFileName());

		try {

			Filial filialFedex = conteudoParametroFilialService.findFilialByConteudoParametro("CD_FILIAL_FEDEX", statusMercadoriaFedexDTO.getFilialFedex());
			if (filialFedex == null) {
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-09147", new Object[] { statusMercadoriaFedexDTO.getFilialFedex() }));
				return Response.ok(retornoDto).build();
			}

			setFilialSessao(filialFedex);
			retornoDto.setSgFilial(filialFedex.getSgFilial());
			retornoDto.setDestinatariosEmail(findDestinatariosEmailRetorno(filialFedex));
			retornoDto.setNrDocumento(statusMercadoriaFedexDTO.getNrDocumentoTNT());
			
			Filial filialOrigem = filialService.findBySgFilialAndIdEmpresa(statusMercadoriaFedexDTO.getSgFilialOrigem(), findIdEmpresaMercurio());
			if (filialOrigem == null) {
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-06050", new Object[] { statusMercadoriaFedexDTO.getSgFilialOrigem() + " " + statusMercadoriaFedexDTO.getNrDocumentoTNT(), filialFedex.getSgFilial() }));
				return Response.ok(retornoDto).build();
			}
			
			ControleCarga controleCarga = controleCargaService.findControleCargaByNrControleCargaByIdFilial(Long.valueOf(statusMercadoriaFedexDTO.getNrDocumentoTNT()), filialOrigem.getIdFilial());
			if (controleCarga == null) {
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-06050", new Object[] { statusMercadoriaFedexDTO.getSgFilialOrigem() + " " + statusMercadoriaFedexDTO.getNrDocumentoTNT(), filialFedex.getSgFilial() }));
				return Response.ok(retornoDto).build();
			}
			
			liberarManifestosBloqueados(controleCarga);
			
			FrotaPlacaChegadaSaidaSuggestDTO frotaPlacaChegadaSaidaSuggestDTO = findDadosByPlacaOuFrota(filialFedex, controleCarga.getMeioTransporteByIdTransportado().getNrFrota());

			if (frotaPlacaChegadaSaidaSuggestDTO.getIdControleCarga() == null || !ConstantesPortaria.TIPO_CHEGADA.equals(frotaPlacaChegadaSaidaSuggestDTO.getTipo())) {
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-06050", new Object[] { statusMercadoriaFedexDTO.getSgFilialOrigem() + " " + statusMercadoriaFedexDTO.getNrDocumentoTNT(), filialFedex.getSgFilial() }));
				return Response.ok(retornoDto).build();
			}

			TypedFlatMap parametros = frotaPlacaChegadaSaidaSuggestDTO.toMap();
			parametros.put("integracaoFedex", true);
			Map<String, Object> mapInformarChegada = newInformarChegadaService.validateAndFindDados(parametros);

			InformarChegadaSaidaDTO informarChegadaSaidaDTO = toInformarChegadaSaidaDTO(mapInformarChegada, filialFedex);
			TypedFlatMap mapChegada = informarChegadaSaidaDTO.toMapChegada();
			mapChegada.putAll(frotaPlacaChegadaSaidaSuggestDTO.toMap());
			mapChegada.put("dhChegada", getDateTime(statusMercadoriaFedexDTO.getDataHoraEvento(), filialFedex, "yyyyMMddHHmm"));
			newInformarChegadaService.executeSalvar(mapChegada);

		} catch (BusinessException be) {
			retornoDto.getMessageErrors().add(parametroGeralService.getMessage(be.getMessageKey(), be.getMessageArguments()));
		} catch (Exception e) {
			retornoDto.setDestinatariosEmail(null);
			retornoDto.getMessageErrors().add(e.getMessage());
		}
		return Response.ok(retornoDto).build();
	}

	private void liberarManifestosBloqueados(ControleCarga controleCarga) {
		List<String> idsManifesto = new ArrayList<String>();
		List<Manifesto> listManifestos = manifestoService.findManifestosByControleCarga(controleCarga);
		for (Manifesto manifesto : listManifestos) {
			Boolean bloqueado = manifesto.getBlBloqueado();
			if(Boolean.TRUE.equals(bloqueado)){
				idsManifesto.add(manifesto.getIdManifesto().toString());
			}
		}
		
		if(CollectionUtils.isNotEmpty(idsManifesto)){
			OcorrenciaPendencia ocorrenciaPendenciaLiberacao = ocorrenciaPendenciaService.findByCodigoOcorrencia(findCdOcorrenciaLiberacaoViagemFedex());
			
			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("ids", idsManifesto);
			typedFlatMap.put("ocorrenciaPendencia.tpOcorrencia", ocorrenciaPendenciaLiberacao.getTpOcorrencia().getValue());
			typedFlatMap.put("ocorrenciaPendencia.blApreensao", false);
			typedFlatMap.put("ocorrenciaPendencia.idOcorrenciaPendencia", ocorrenciaPendenciaLiberacao.getIdOcorrenciaPendencia());
			typedFlatMap.put("ocorrenciaPendencia.evento.idEvento", ocorrenciaPendenciaLiberacao.getEvento().getIdEvento());
			typedFlatMap.put("blIntegracaoFedex", true);
			
			ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServicoCC(typedFlatMap);
		}
	}

	private FrotaPlacaChegadaSaidaSuggestDTO findDadosByPlacaOuFrota(Filial filialFedex, String placaOuFrota) {
		// igual chamada da suggest placa/frota da tela portaria nova UI
		List<Object[]> listChegadaViagem = portariaService.findChegadaViagemIntegracaoFedex(placaOuFrota, filialFedex.getIdFilial());
		TypedFlatMap parametros = chegadaViagemToMap(listChegadaViagem);
		parametros.put("idFilial", filialFedex.getIdFilial());
		return new FrotaPlacaChegadaSaidaSuggestDTO(new TypedFlatMap(parametros));
	}

	public TypedFlatMap chegadaViagemToMap(List<Object[]> list) {
		TypedFlatMap parametros = new TypedFlatMap();

		for (Object[] obj : list) {
			parametros.put("idControleCarga", obj[0]);
			parametros.put("dsControleCarga", obj[1]);
			parametros.put("idControle", obj[2]);
			parametros.put("idOrdemSaida", obj[3]);
			parametros.put("nrFrota", obj[4]);
			parametros.put("nrIdentificador", obj[5]);
			parametros.put("idMeioTransporte", obj[6]);
			parametros.put("tipo", obj[7]);
			parametros.put("tipoLabel", obj[8]);
			parametros.put("subTipo", obj[9]);
			parametros.put("subTipoLabel", obj[10]);
		}
		return parametros;
	}

	private InformarChegadaSaidaDTO toInformarChegadaSaidaDTO(Map<String, Object> map, Filial filial) {
		InformarChegadaSaidaDTO dto = new InformarChegadaSaidaDTO();

		dto.setBlInformaKmPortaria(map.get("blInformaKmPortaria") != null ? (Boolean) map.get("blInformaKmPortaria") : null);
		dto.setDhChegada(map.get("dhChegada") != null ? (DateTime) map.get("dhChegada") : null);
		dto.setTpChegada(map.get("tpChegada") != null ? (String) map.get("tpChegada") : null);
		dto.setDsRota(map.get("dsRota") != null ? (String) map.get("dsRota") : null);
		dto.setDsTipoMeioTransporte(map.get("dsTipoMeioTransporte") != null ? (String) map.get("dsTipoMeioTransporte") : null);
		dto.setIdControleTrecho(map.get("idControleTrecho") != null ? (Long) map.get("idControleTrecho") : null);
		dto.setIdReboque(map.get("idReboque") != null ? (Long) map.get("idReboque") : null);
		dto.setLacres(toLacresDto(map));
		dto.setNmPessoa(map.get("nmPessoa") != null ? (String) map.get("nmPessoa") : null);
		dto.setNrControleCarga(map.get("nrControleCarga") != null ? (Long) map.get("nrControleCarga") : null);
		dto.setNrFrotaTransportado(map.get("nrFrotaTransportado") != null ? (String) map.get("nrFrotaTransportado") : null);
		dto.setNrIdentificacao(map.get("nrIdentificacao") != null ? (String) map.get("nrIdentificacao") : null);
		dto.setNrIdentificadorReboque(map.get("nrIdentificadorReboque") != null ? (String) map.get("nrIdentificadorReboque") : null);
		dto.setNrIdentificadorTransportado(map.get("nrIdentificadorTransportado") != null ? (String) map.get("nrIdentificadorTransportado") : null);
		dto.setSgFilial(map.get("sgFilial") != null ? (String) map.get("sgFilial") : null);
		dto.setTpControleCarga(map.get("tpControleCarga") != null ? (String) map.get("tpControleCarga") : null);
		dto.setTpIdentificacao(map.get("tpIdentificacao") != null ? (DomainValue) map.get("tpIdentificacao") : null);
		dto.setVersao(map.get("versao") != null ? ((Integer) map.get("versao")).toString() : null);
		return dto;
	}

	@SuppressWarnings("unchecked")
	private List<LacreDTO> toLacresDto(Map<String, Object> map) {
		List<LacreDTO> listLacresDto = new ArrayList<LacreDTO>();
		List<Map<String, Object>> lacres = map.get("lacres") != null ? (List<Map<String, Object>>) map.get("lacres") : null;
		for (Map<String, Object> lacre : lacres) {
			LacreDTO lacreDTO = new LacreDTO();
			lacreDTO.setNrLacres(lacre.get("nrLacres") != null ? (String) lacre.get("nrLacres") : null);
			lacreDTO.setIdLacreControleCarga(lacre.get("idLacreControleCarga") != null ? (Long) lacre.get("idLacreControleCarga") : null);
			listLacresDto.add(lacreDTO);
		}
		return listLacresDto;
	}
	
	
	@POST
	@Path("/generateStatusInicioDescarga")
	public Response generateStatusInicioDescarga(StatusMercadoriaFedexDTO statusMercadoriaFedexDTO){
		RetornoAtualizaStatusMercadoriaFedexDTO retornoDto = new RetornoAtualizaStatusMercadoriaFedexDTO();
		
		retornoDto.setFileName(statusMercadoriaFedexDTO.getFileName());

		try {
			Filial filialOrigem = filialService.findBySgFilialAndIdEmpresa(statusMercadoriaFedexDTO.getSgFilialOrigem(), findIdEmpresaMercurio());
			if (filialOrigem == null) {
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-06050", new Object[] { statusMercadoriaFedexDTO.getSgFilialOrigem() + " " + statusMercadoriaFedexDTO.getNrDocumentoTNT(), filialOrigem.getSgFilial() }));
				return Response.ok(retornoDto).build();
			}

			
			Filial filialFedex = conteudoParametroFilialService.findFilialByConteudoParametro("CD_FILIAL_FEDEX", statusMercadoriaFedexDTO.getFilialFedex());
			if (filialFedex == null) {
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-09147", new Object[] { statusMercadoriaFedexDTO.getFilialFedex() }));
				return Response.ok(retornoDto).build();
			}
			
			setFilialSessao(filialFedex);
			retornoDto.setSgFilial(filialOrigem.getSgFilial());
			retornoDto.setDestinatariosEmail(findDestinatariosEmailRetorno(filialFedex));
			retornoDto.setNrDocumento(statusMercadoriaFedexDTO.getNrDocumentoTNT());
			
			Usuario usuario = usuarioService.findUsuarioByLogin("integracao");
			Pais pais = paisService.findByIdPessoa(filialFedex.getIdFilial());
			volDadosSessaoService.executeDadosSessaoBanco(usuario, filialFedex, pais);

			String nrManifesto = null;
			Manifesto manifesto = null;
			List<ManifestoViagemNacional> manifestoViagemNacionalList = manifestoViagemNacionalService.findByNrManifestoOrigemByFilial(Integer.valueOf(statusMercadoriaFedexDTO.getNrDocumentoTNT()), filialOrigem.getIdFilial());
			if (manifestoViagemNacionalList != null && manifestoViagemNacionalList.size() > 0){
				manifesto = manifestoService.findById(manifestoViagemNacionalList.get(0).getManifesto().getIdManifesto());
				nrManifesto = manifestoViagemNacionalList.get(0).getNrManifestoOrigem().toString();
			}else{
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-03024", new Object[] { statusMercadoriaFedexDTO.getSgFilialOrigem(),statusMercadoriaFedexDTO.getNrDocumentoTNT()}));
				return Response.ok(retornoDto).build();
			}
			
			
			DateTime dhEvento = DateTimeFormat.forPattern("yyyyMMddHHmm").parseDateTime(statusMercadoriaFedexDTO.getDataHoraEvento());
			
			manifestoService.generateStatusInicioDescarga(manifesto, filialFedex, filialOrigem, usuario, dhEvento);
			
		} catch (BusinessException be) {
			retornoDto.getMessageErrors().add(parametroGeralService.getMessage(be.getMessageKey(), be.getMessageArguments()));
		} catch (Exception e) {
			retornoDto.setDestinatariosEmail(null);
			retornoDto.getMessageErrors().add(e.getMessage());
		}
		return Response.ok(retornoDto).build();
	}

	@POST
	@Path("/generateStatusFimDescarga")
	public Response generateStatusFimDescarga(StatusMercadoriaFedexDTO statusMercadoriaFedexDTO){
		RetornoAtualizaStatusMercadoriaFedexDTO retornoDto = new RetornoAtualizaStatusMercadoriaFedexDTO();
		
		retornoDto.setFileName(statusMercadoriaFedexDTO.getFileName());

		try {

			Filial filialFedex = conteudoParametroFilialService.findFilialByConteudoParametro("CD_FILIAL_FEDEX", statusMercadoriaFedexDTO.getFilialFedex());
			if (filialFedex == null) {
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-09147", new Object[] { statusMercadoriaFedexDTO.getFilialFedex() }));
				return Response.ok(retornoDto).build();
			}
			
			setFilialSessao(filialFedex);
			retornoDto.setSgFilial(filialFedex.getSgFilial());
			retornoDto.setDestinatariosEmail(findDestinatariosEmailRetorno(filialFedex));
			retornoDto.setNrDocumento(statusMercadoriaFedexDTO.getNrChave());
			
			Usuario usuario = usuarioService.findUsuarioByLogin("integracao");
			Pais pais = paisService.findByIdPessoa(filialFedex.getIdFilial());
			volDadosSessaoService.executeDadosSessaoBanco(usuario, filialFedex, pais);
			
			Conhecimento conhecimento = conhecimentoService.findByNrChave(statusMercadoriaFedexDTO.getNrChave());
			if (conhecimento == null){
				retornoDto.getMessageErrors().add(parametroGeralService.getMessage("LMS-09154", new Object[] { statusMercadoriaFedexDTO.getNrChave()}));
				return Response.ok(retornoDto).build();
			}
			
			DateTime dhEvento = DateTimeFormat.forPattern("yyyyMMddHHmm").parseDateTime(statusMercadoriaFedexDTO.getDataHoraEvento()); 
			
			controleCargaService.generateStatusFimDescarga(conhecimento, filialFedex, usuario, dhEvento);
			
		} catch (BusinessException be) {
			retornoDto.getMessageErrors().add(parametroGeralService.getMessage(be.getMessageKey(), be.getMessageArguments()));
		} catch (Exception e) {
			retornoDto.setDestinatariosEmail(null);
			retornoDto.getMessageErrors().add(e.getMessage());
		}
		return Response.ok(retornoDto).build();
	}
	
	
	private Long findIdEmpresaMercurio() {
		Long idEmpresaMercurio = Long.valueOf(parametroGeralService.findByNomeParametro("ID_EMPRESA_MERCURIO").getDsConteudo());
		return idEmpresaMercurio;
	}
	
	private short findCdOcorrenciaLiberacaoViagemFedex() {
		short cdOcorrenciaLiberacaoViagem = Short.valueOf(parametroGeralService.findByNomeParametro("OCORRENCIA_LIBERACAO_VIAGEM_FEDEX").getDsConteudo());
		return cdOcorrenciaLiberacaoViagem;
	}

	private String findDestinatariosEmailRetorno(Filial filial) {
		return (String) conteudoParametroFilialService.findConteudoByNomeParametro(filial.getIdFilial(), "DEST_EMAIL_ERROS_FDX", false);
	}

	private DateTime getDateTime(String dataHora, Filial filial, String pattern) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		DateTime dateTime = formatter.parseDateTime(dataHora);
		return dateTime.withZone(filial.getDateTimeZone());
	}

	private void setFilialSessao(Filial filialFedex) {
		Usuario usuario = usuarioService.findUsuarioByLogin("integracao");
		Pais pais = paisService.findByIdPessoa(filialFedex.getIdFilial());
		volDadosSessaoService.executeDadosSessaoBanco(usuario, filialFedex, pais);
	}
	
	private void setEmpresaSessao(Long idEmpresa) {
		Empresa empresa = empresaService.findEmpresaLogadoById(idEmpresa);
		SessionContext.set(SessionKey.EMPRESA_KEY, empresa);
	}

}