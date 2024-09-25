package com.mercurio.lms.expedicao.edi.model.service;

import br.com.tntbrasil.integracao.domains.expedicao.EtiquetaDMN;
import br.com.tntbrasil.integracao.domains.expedicao.NotaFiscalDMN;
import br.com.tntbrasil.integracao.domains.expedicao.NotaFiscalWrapperDMN;
import br.com.tntbrasil.integracao.domains.expedicao.VolumeDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import com.mercurio.adsm.core.util.ADSMInitArgs;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.UnitizacaoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.*;
import com.mercurio.lms.configuracoes.model.service.*;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.edi.dto.ProcessarEdiDTO;
import com.mercurio.lms.expedicao.dto.*;
import com.mercurio.lms.edi.dto.ValidarEdiDTO;
import com.mercurio.lms.edi.enums.DsCampoLogErrosEDI;
import com.mercurio.lms.edi.model.*;
import com.mercurio.lms.edi.model.dao.NotaFiscalEdiDAO;
import com.mercurio.lms.edi.model.service.*;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.expedicao.model.service.*;
import com.mercurio.lms.expedicao.util.*;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.*;
import com.mercurio.lms.municipios.model.service.*;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.*;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.*;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.mercurio.lms.util.FormatUtils.fillNumberWithZero;

/**
 * Service para processamento individual das notas EDI, serve para isolar o
 * processamento em uma transação separada para não afetar a transação principal
 * do processamento quando lancadas as BusinessException.
 *
 * @author Vagner Huzalo
 *
 */
public class NotaFiscalEDIService extends CrudService<NotaFiscalEdi, Long> {

	private static final String AJUSTA_VOLUMES_EDI = "AJUSTA_VOLUMES_EDI";
	private static final String VOLTOTALIZ = "VOLTOTALIZ";
	private static final Short ROTA_ZERO = Short.valueOf("0");
	private static final String DS_CAMPO_CONTRIB = "CONTRIB";
	private ConhecimentoNormalService conhecimentoNormalService;
	private DigitarDadosNotaNormalNotasFiscaisService digitarDadosNotaNormalNotasFiscaisService;
	private DigitarDadosNotaNormalCamposAdicionaisService digitarDadosNotaNormalCamposAdicionaisService;
	private DigitarDadosNotaNormalService digitarDadosNotaNormalService;
	private DigitarDadosNotaNormalCalculoCTRCService digitarDadosNotaNormalCalculoCTRCService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private ParametroGeralService parametroGeralService;
	private DivisaoClienteNaturezaProdutoService divisaoClienteNaturezaProdutoService;
	private NaturezaProdutoService naturezaProdutoService;
	private EnderecoPessoaService enderecoPessoaService;
	private LogErrosEDIService logErrosEDIService;
	private ServicoService servicoService;
	private ClienteService clienteService;
	private IntervaloCepService intervaloCepService;
	private UnidadeFederativaService unidadeFederativaService;
	private InscricaoEstadualService inscricaoEstadualService;
	private PpeService ppeService;
	private DivisaoClienteService divisaoClienteService;
	private FilialService filialService;
	private MunicipioService municipioService;
	private NotaFiscalExpedicaoEDIVolumeService notaFiscalExpedicaoEDIVolumeService;
	private NotaFiscalEDIComplementoService notaFiscalEDIComplementoService;
	private NotaFiscalEDIItemService notaFiscalEDIItemService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService;
	private InformacaoDoctoClienteService informacaoDoctoClienteService;
	private DpeService dpeService;
	private TipoTributacaoIEService tipoTributacaoIEService;
	private ConhecimentoService conhecimentoService;
	private ConfiguracoesFacade configuracoesFacade;
	private PessoaService pessoaService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private NotaFiscalEDIVolumeService notaFiscalEDIVolumeService;
	private McdService mcdService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private InformacaoDocServicoService informacaoDocServicoService;
	private LogEDIDetalheService logEDIDetalheService;
	private LogEDIVolumeService logEDIVolumeService;
	private LogEDIComplementoService logEDIComplementoService;
	private UnitizacaoService unitizacaoService;
	private CCEItemService cceItemService;
	private IntegracaoJmsService integracaoJmsService;
	private PaisService paisService;
	private UsuarioService usuarioService;
	private EmpresaService empresaService;
	public void setNotaFiscalEdiDao(NotaFiscalEdiDAO dao){
		this.setDao(dao);
	}

	public NotaFiscalEdiDAO getNotaFiscalEdiDAO(){
		return (NotaFiscalEdiDAO)this.getDao();
	}

	private final Logger log = LogManager.getLogger();

	private static HashMap<String, Long> vlIdParcelasCalculo = new HashMap<String, Long>();

	static {
		vlIdParcelasCalculo.put("vlFretePeso", 8l);
		vlIdParcelasCalculo.put("vlFreteValor", 23l);
		vlIdParcelasCalculo.put("vlCat", 36l);
		vlIdParcelasCalculo.put("vlDespacho", 16l);
		vlIdParcelasCalculo.put("vlItr", 38l);
		vlIdParcelasCalculo.put("vlAdeme", 24l);
		vlIdParcelasCalculo.put("vlPedagio", 12l);
		vlIdParcelasCalculo.put("vlTaxasEOutrosValores", 60l);
	}


	@Override
	public NotaFiscalEdi findById(Long idNotaFiscalEdi){
		return (NotaFiscalEdi) super.findById(idNotaFiscalEdi);
	}

	public NotaFiscalEdi findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
		return (NotaFiscalEdi)super.findByIdInitLazyProperties(id, initializeLazyProperties);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> executeNotasFiscaisEdi(ProcessaNotasEdiItemDto processaNotasEdiItemDto, Cliente cliente) throws Exception {

		Map<String, Object> retorno = new HashMap<>();
		ProcessarEdiUtils processarEdiUtils = new ProcessarEdiUtils();
		ProcessarEdiDTO processarEdiDTO = processarEdiUtils.toProcessarEdiDTO(processaNotasEdiItemDto);
		ParameterDto parameterDto = processaNotasEdiItemDto.getParameters();
		Map<String, Object> parameters = processarEdiDTO.getParameters();
		parameters.put("nrProcessamento", processaNotasEdiItemDto.getNrProcessamento());

		Conhecimento conhecimento = (Conhecimento)parameters.get("conhecimento");
		carregarClientesConhecimento(conhecimento);
		buildObservacaoDoctoServico(conhecimento, parameters);
		Usuario usuario = usuarioService.findById(processaNotasEdiItemDto.getIdUsuario());
		SessionContext.setUser(usuario);
		SessionContext.set(SessionKey.FILIAL_KEY, filialService.findById(processaNotasEdiItemDto.getIdFilial()));
		Empresa empresaPadrao = empresaService.findEmpresaPadraoByUsuario(SessionUtils.getUsuarioLogado());
		SessionContext.set(SessionKey.PAIS_KEY, paisService.findPaisByUsuarioEmpresa(usuario, empresaPadrao));
		// LMSA-7137
		parameters.put("clienteRemetente", cliente);

		try {
			digitarDadosNotaNormalNotasFiscaisService
					.storeNotas
							(
									conhecimento,
									parameterDto.getNotaFiscalConhecimento(), cliente,
									parameterDto.getTpCalculoPreco(), parameterDto.getValidaLimiteValorMercadoria(),
									processaNotasEdiItemDto.getIdFilial(), processaNotasEdiItemDto.getIdProcessamentoEdi()
							);


			//Conhecimento conhecimento = (Conhecimento) mapResult.get("conhecimento");
			// LMSA-6598
			if (BooleanUtils.isTrue(conhecimento.getBlRedespachoIntermediario())) {
				// LMSA-7137
				Cliente clienteRemetente = (Cliente) parameters.get("clienteRemetente");
				for (final NotaFiscalConhecimento notaFiscalConhecimento : conhecimento.getNotaFiscalConhecimentos()) {
					// LMSA-7137
					verificarChavesNotaUtilizadaOutroCte(
							notaFiscalConhecimento.getNrChave(),
							clienteRemetente != null ? clienteRemetente.getIdCliente() : 0
					);
				}
			}

			setDhInclusaoConhecimento(conhecimento, (HashMap) parameters.get("mapDataHoraNotaFiscal"), (String) parameters.get("opcaoProcessamento"));

			final Map mapDadosComplementos = (Map) parameters.get("mapDadosComplementos");
			List<CampoAdicionalConhecimentoDto> campoAdicionalConhecimento =
					(List<CampoAdicionalConhecimentoDto>) mapDadosComplementos.get("campoAdicionalConhecimento");

			digitarDadosNotaNormalCamposAdicionaisService
					.storeInSessionForEdi(conhecimento, campoAdicionalConhecimento);

			conhecimento.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NORMAL));
			conhecimento.setTpDocumentoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NACIONAL));
			conhecimento.setTpDoctoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NACIONAL));

			Boolean obrigaAgendamentoConhecimento = Boolean.FALSE;

			List<DadosComplemento> lstDadosComplementos = conhecimento.getDadosComplementos();
			for (DadosComplemento dadoComplemento : lstDadosComplementos) {
				if ("S".equalsIgnoreCase(dadoComplemento.getDsValorCampo())) {
					InformacaoDoctoCliente infDoctoCliente = dadoComplemento.getInformacaoDoctoCliente();
					InformacaoDoctoCliente infoObgAgend = informacaoDoctoClienteService.findByIdInformacaoDoctoClienteAndDsCampo(infDoctoCliente.getIdInformacaoDoctoCliente(), "OBG_AGE");

					if (infoObgAgend != null) {
						obrigaAgendamentoConhecimento = Boolean.TRUE;
					}
				}
			}

			// setar como dhInclusao a menor data de digitacao
			if (parameters.get("mapDataHoraNotaFiscal") != null) {
				final Map dataHoraMap = (Map) parameters.get("mapDataHoraNotaFiscal");
				if (dataHoraMap.size() > 0) {
					DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
					final TreeSet<String> datas = new TreeSet<String>(dataHoraMap.values());
					conhecimento.setDhInclusao(formatter.parseDateTime(datas.first()));
				}
			}

			parameters.put("conhecimento", conhecimento);
			setIdNaturezaProduto(parameters);

			parameters = digitarDadosNotaNormalService.executeCtrcPrimeiraFase(parameters);

			parameters = digitarDadosNotaNormalCalculoCTRCService.findDivisaoClienteByCTRC(parameters);

			parameters = prepararDivisaoCliente(parameters);
			calculaPesoCubado
					(conhecimento.getNotaFiscalConhecimentos(),
							(List<NotaFiscalConhecimentoDto>) parameters.get("notaFiscalConhecimento"),
							(Long) parameters.get("idDivisaoCliente"), (Long) parameters.get("idServico"));

			parameters.putAll(digitarDadosNotaNormalService.executeCtrcSegundaFase(parameters));

			conhecimento = (Conhecimento) parameters.get("conhecimento");

			if (conhecimento.getDtPrevEntrega() != null) {
				Map dtPrevEntrega = dpeService.executeCalculoDPE(
						conhecimento.getClienteByIdClienteRemetente(),
						conhecimento.getClienteByIdClienteDestinatario(),
						conhecimento.getClienteByIdClienteBaseCalculo(),
						conhecimento.getClienteByIdClienteConsignatario(),
						conhecimento.getClienteByIdClienteRedespacho(),
						null,
						conhecimento.getServico().getIdServico(),
						conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio(),
						conhecimento.getFilialByIdFilialOrigem().getIdFilial(),
						conhecimento.getFilialByIdFilialDestino().getIdFilial(),
						conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio(),
						conhecimento.getNrCepColeta(),
						conhecimento.getNrCepEntrega(),
						conhecimento.getDhEmissao() == null ? null : conhecimento.getDhEmissao()
				);

				if (dtPrevEntrega != null && dtPrevEntrega.get("dtPrazoEntrega") != null) {
					ObservacaoDoctoServico observacaoDoctoServico = new ObservacaoDoctoServico();
					observacaoDoctoServico.setBlPrioridade(Boolean.FALSE);
					observacaoDoctoServico.setDoctoServico(conhecimento);
					observacaoDoctoServico.setDsObservacaoDoctoServico("OTD TNT: " + ((YearMonthDay) dtPrevEntrega.get("dtPrazoEntrega")).toString("dd/MM/yyyy"));
					observacaoDoctoServico.setIdObservacaoDoctoServico(null);
					conhecimento.addObservacaoDoctoServico(observacaoDoctoServico);
				}
			}

			if (ConstantesExpedicao.CALCULO_MANUAL.equals(parameters.get("tpCalculoPreco"))) {
				final List<Map> parcelas = new ArrayList<Map>();

				final List<NotaFiscalConhecimentoDto>
					notaFiscalConhecimento = (List<NotaFiscalConhecimentoDto>) parameters.get("notaFiscalConhecimento");
				
				BigDecimal valorNf = null;
				final Map<String, BigDecimal> mapValoresNota = new HashMap<String, BigDecimal>();
				for (final String chave : vlIdParcelasCalculo.keySet()) {
					for (final NotaFiscalConhecimentoDto notaFiscal : notaFiscalConhecimento) {
						Class<NotaFiscalConhecimentoDto> aClass = (Class<NotaFiscalConhecimentoDto>) notaFiscal.getClass();
						Method method = aClass.getMethod("get" + StringUtils.capitalize(chave));
						valorNf = (BigDecimal) method.invoke(notaFiscal);
						if ((valorNf != null) && (BigDecimalUtils.gtZero(valorNf))) {
							mapValoresNota.put(chave, (mapValoresNota.get(chave) == null) ? valorNf : valorNf.add(mapValoresNota.get(chave)));
						}
					}

				}

				// monta as parcelas do calculo manual
				for (final String key : mapValoresNota.keySet()) {
					final Long idKey = vlIdParcelasCalculo.get(key);
					final Map<String, Object> parcela = new HashMap<String, Object>();
					parcela.put("id", idKey);
					parcela.put("vlParcela", mapValoresNota.get(key));
					parcelas.add(parcela);
				}
				parameters.put("parcela", parcelas);

				CalculoFrete calculoFrete = ExpedicaoUtils.newCalculoFrete(conhecimento.getTpDocumentoServico());

				conhecimentoNormalService.validateCalculoFreteManual(calculoFrete, conhecimento, parameters);
				parameters.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, calculoFrete);
				digitarDadosNotaNormalCalculoCTRCService.storeCtrcPrimeiraFaseCalculoFrete(parameters);
			} else {
				digitarDadosNotaNormalCalculoCTRCService.storeCtrcPrimeiraFasePreCalculoFrete(parameters);
			}

			if (Boolean.TRUE.equals(ConhecimentoUtils.isAtualizaDestinatarioEdiOrConsignatarioEdi(conhecimento))) {
				//Somente atualiza o endereço de entrega do conhecimento se não foi utilizado o endereço do consignatário.
				if (parameters.get("blUsaEnderecoConsignatario") == null || Boolean.FALSE.equals(parameters.get("blUsaEnderecoConsignatario"))) {
					Object dsLocalEntregaReal = parameters.get("dadosEnderecoEntregaReal");
					if (!StringUtils.isEmpty((String) dsLocalEntregaReal)) {
						conhecimento.setDsEnderecoEntregaReal((String) dsLocalEntregaReal);
						conhecimentoService.store(conhecimento);
					}
				}
			}

			final StringBuffer strLog = new StringBuffer("[EDI] CTRC incluído com Sucesso. Filial: " + SessionUtils.getFilialSessao().getSgFilial());

			strLog.append("; Cliente: " + conhecimento.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao());
			strLog.append("; idConhecimento: " + conhecimento.getIdDoctoServico());
			strLog.append("; nrConhecimento: " + conhecimento.getNrConhecimento());
			strLog.append("; nrOrdemEmissaoEDI: " + conhecimento.getNrOrdemEmissaoEDI());
			strLog.append("; NFs (número): ");

			Long idMonitoramentoDescarga = null;
			for (final NotaFiscalConhecimento notaFiscalConhecimento : conhecimento.getNotaFiscalConhecimentos()) {
				strLog.append(notaFiscalConhecimento.getNrNotaFiscal() + "[");
				final List listaVolumes = notaFiscalConhecimento.getVolumeNotaFiscais();


				for (Object listaVolume : listaVolumes) {
					final VolumeNotaFiscal vnf = (VolumeNotaFiscal) listaVolume;
					strLog.append(vnf.getNrVolumeColeta() + ",");

					if (idMonitoramentoDescarga == null && vnf.getMonitoramentoDescarga() != null) {
						idMonitoramentoDescarga = vnf.getMonitoramentoDescarga().getIdMonitoramentoDescarga();
						retorno.put("idMonitoramentoDescarga", idMonitoramentoDescarga);
					}

				}
				strLog.append("], ");

				//LMS-795: por questao de concorrencia valida novamente
				List<Integer> nrNotasFiscais = new ArrayList<>(1);
				nrNotasFiscais.add(notaFiscalConhecimento.getNrNotaFiscal());
				List<YearMonthDay> dtEmissoes = new ArrayList<>(1);
				dtEmissoes.add(notaFiscalConhecimento.getDtEmissao());
				List<String> dsSeries = new ArrayList<>();
				dsSeries.add(notaFiscalConhecimento.getDsSerie());

				// LMSA-6598
				if (!BooleanUtils.isTrue(conhecimento.getBlRedespachoIntermediario())) {
					verifyNrNotasFiscais(conhecimento.getClienteByIdClienteRemetente().getPessoa().getIdPessoa(), nrNotasFiscais, dtEmissoes, conhecimento.getIdDoctoServico(), dsSeries);
					validateChaveNfe(notaFiscalConhecimento.getNrChave(), Long.valueOf(notaFiscalConhecimento.getCliente().getPessoa().getNrIdentificacao()), notaFiscalConhecimento.getNrNotaFiscal(), notaFiscalConhecimento.getDtEmissao(), conhecimento.getIdDoctoServico());
				}
			}
			log.warn(strLog);

			conhecimento.setBlObrigaAgendamento(obrigaAgendamentoConhecimento);

			/** Remove objeto de suas coleções de cache de primeiro nível */
			super.flush();
			getDao().getAdsmHibernateTemplate().evict(conhecimento);


			this.unitizacaoService.storeUnitizarVolumesConhecimento(conhecimento);
		} catch (BusinessException be){
			throw new BusinessException(be.getMessageKey(), be.getMessageArguments(), be);
		} catch (Throwable e){
			throw new Exception(e.getMessage(), e);
		}
		return retorno;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> executeNotasFiscaisEdi(Map parameters) {
		Map<String, Object> retorno = new HashMap<String, Object>();

		final Map mapConhecimento = digitarDadosNotaNormalNotasFiscaisService.storeNotas(parameters);

		Conhecimento conhecimento = (Conhecimento) mapConhecimento.get("conhecimento");

		// LMSA-6598
		if(BooleanUtils.isTrue(conhecimento.getBlRedespachoIntermediario())) {
			// LMSA-7137
			Cliente clienteRemetente = (Cliente) parameters.get("clienteRemetente");
			for (final NotaFiscalConhecimento notaFiscalConhecimento : conhecimento.getNotaFiscalConhecimentos()) {
				// LMSA-7137
				verificarChavesNotaUtilizadaOutroCte(
						notaFiscalConhecimento.getNrChave(),
						clienteRemetente != null ? clienteRemetente.getIdCliente() : 0
				);
			}
		}

		setDhInclusaoConhecimento(conhecimento, (HashMap) parameters.get("mapDataHoraNotaFiscal"), (String) parameters.get("opcaoProcessamento"));

		final Map mapDadosComplementos = (Map) parameters.get("mapDadosComplementos");
		mapDadosComplementos.put("conhecimento", conhecimento);

		final Map mapRetorno = digitarDadosNotaNormalCamposAdicionaisService.storeInSessionForEdi(mapDadosComplementos);
		conhecimento = (Conhecimento) mapRetorno.get("conhecimento");

		conhecimento.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NORMAL));
		conhecimento.setTpDocumentoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NACIONAL));
		conhecimento.setTpDoctoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NACIONAL));

		Boolean obrigaAgendamentoConhecimento = Boolean.FALSE;

		List<DadosComplemento> lstDadosComplementos = conhecimento.getDadosComplementos();
		for (DadosComplemento dadoComplemento : lstDadosComplementos){
			if ("S".equalsIgnoreCase(dadoComplemento.getDsValorCampo())){
				InformacaoDoctoCliente infDoctoCliente = dadoComplemento.getInformacaoDoctoCliente();
				InformacaoDoctoCliente infoObgAgend = informacaoDoctoClienteService.findByIdInformacaoDoctoClienteAndDsCampo(infDoctoCliente.getIdInformacaoDoctoCliente(), "OBG_AGE");

				if (infoObgAgend != null){
					obrigaAgendamentoConhecimento = Boolean.TRUE;
				}
			}
		}

		// setar como dhInclusao a menor data de digitacao
		if(parameters.get("mapDataHoraNotaFiscal") != null) {
			final Map dataHoraMap = (Map) parameters.get("mapDataHoraNotaFiscal");
			if(dataHoraMap.size() > 0) {
				final TreeSet<DateTime> datas = new TreeSet<DateTime>(dataHoraMap.values());
				conhecimento.setDhInclusao(datas.first());
			}
		}

		parameters.put("conhecimento", conhecimento);
		setIdNaturezaProduto(parameters);

		try {
			parameters = digitarDadosNotaNormalService.executeCtrcPrimeiraFase(parameters);
		}catch(BusinessException ex) {
			if("LMS-04387".equals(ex.getMessageKey())) {
				throw new BusinessException("LMS-04386");
			}
			throw ex;
		}

		parameters = digitarDadosNotaNormalCalculoCTRCService.findDivisaoClienteByCTRC(parameters);

		parameters = prepararDivisaoCliente(parameters);
		parameters = calculaPesoCubado(parameters);

		parameters.putAll(digitarDadosNotaNormalService.executeCtrcSegundaFase(parameters));

		conhecimento = (Conhecimento) parameters.get("conhecimento");

		if (conhecimento.getDtPrevEntrega() != null){
			Map dtPrevEntrega = dpeService.executeCalculoDPE(
					conhecimento.getClienteByIdClienteRemetente(),
					conhecimento.getClienteByIdClienteDestinatario(),
					conhecimento.getClienteByIdClienteBaseCalculo(),
					conhecimento.getClienteByIdClienteConsignatario(),
					conhecimento.getClienteByIdClienteRedespacho(),
					null,
					conhecimento.getServico().getIdServico(),
					conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio(),
					conhecimento.getFilialByIdFilialOrigem().getIdFilial(),
					conhecimento.getFilialByIdFilialDestino().getIdFilial(),
					conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio(),
					conhecimento.getNrCepColeta(),
					conhecimento.getNrCepEntrega(),
					conhecimento.getDhEmissao() == null ? null : conhecimento.getDhEmissao()
			);

			if (dtPrevEntrega != null && dtPrevEntrega.get("dtPrazoEntrega") != null){
				ObservacaoDoctoServico observacaoDoctoServico = new ObservacaoDoctoServico();
				observacaoDoctoServico.setBlPrioridade(Boolean.FALSE);
				observacaoDoctoServico.setDoctoServico(conhecimento);
				observacaoDoctoServico.setDsObservacaoDoctoServico("OTD TNT: "+ ((YearMonthDay)dtPrevEntrega.get("dtPrazoEntrega")).toString("dd/MM/yyyy"));
				observacaoDoctoServico.setIdObservacaoDoctoServico(null);
				conhecimento.addObservacaoDoctoServico(observacaoDoctoServico);
			}
		}

		if(ConstantesExpedicao.CALCULO_MANUAL.equals(parameters.get("tpCalculoPreco"))) {
			final List<Map> parcelas = new ArrayList<Map>();
			final List<Map> notaFiscalConhecimento = (List<Map>) parameters.get("notaFiscalConhecimento");

			BigDecimal valorNf = null;
			final Map<String, BigDecimal> mapValoresNota = new HashMap<String, BigDecimal>();
			for (final String chave : vlIdParcelasCalculo.keySet()) {
				for (final Map map : notaFiscalConhecimento) {
					valorNf = (BigDecimal) map.get(chave);
					// se foi recebido o valor da parcela
					if((valorNf != null) && (BigDecimalUtils.gtZero(valorNf))) {
						mapValoresNota.put(chave, (mapValoresNota.get(chave) == null) ? valorNf : valorNf.add(mapValoresNota.get(chave)));
					}
				}

			}

			// monta as parcelas do calculo manual
			for (final String key : mapValoresNota.keySet()) {
				final Long idKey = vlIdParcelasCalculo.get(key);
				final Map<String, Object> parcela = new HashMap<String, Object>();
				parcela.put("id", idKey);
				parcela.put("vlParcela", mapValoresNota.get(key));
				parcelas.add(parcela);
			}
			parameters.put("parcela", parcelas);

			CalculoFrete calculoFrete = ExpedicaoUtils.newCalculoFrete(conhecimento.getTpDocumentoServico());

			conhecimentoNormalService.validateCalculoFreteManual(calculoFrete, conhecimento, parameters);
			parameters.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, calculoFrete);
			digitarDadosNotaNormalCalculoCTRCService.storeCtrcPrimeiraFaseCalculoFrete(parameters);
		} else {
			digitarDadosNotaNormalCalculoCTRCService.storeCtrcPrimeiraFasePreCalculoFrete(parameters);
		}

		if (Boolean.TRUE.equals(ConhecimentoUtils.isAtualizaDestinatarioEdiOrConsignatarioEdi(conhecimento))){
			//Somente atualiza o endereço de entrega do conhecimento se não foi utilizado o endereço do consignatário.
			if(parameters.get("blUsaEnderecoConsignatario")== null ||Boolean.FALSE.equals(parameters.get("blUsaEnderecoConsignatario"))){
				Object dsLocalEntregaReal =  parameters.get("dadosEnderecoEntregaReal");
				if (!StringUtils.isEmpty((String)dsLocalEntregaReal)){
					conhecimento.setDsEnderecoEntregaReal((String)dsLocalEntregaReal);
					conhecimentoService.store(conhecimento);
				}
			}
		}

		final StringBuffer strLog = new StringBuffer("[EDI] CTRC incluído com Sucesso. Filial: " + SessionUtils.getFilialSessao().getSgFilial());

		strLog.append("; Cliente: " + conhecimento.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao());
		strLog.append("; idConhecimento: " + conhecimento.getIdDoctoServico());
		strLog.append("; nrConhecimento: " + conhecimento.getNrConhecimento());
		strLog.append("; nrOrdemEmissaoEDI: " + conhecimento.getNrOrdemEmissaoEDI());
		strLog.append("; NFs (número): ");

		Long idMonitoramentoDescarga = null;
		for (final NotaFiscalConhecimento notaFiscalConhecimento : conhecimento.getNotaFiscalConhecimentos()) {
			strLog.append(notaFiscalConhecimento.getNrNotaFiscal() + "[");
			final List listaVolumes = notaFiscalConhecimento.getVolumeNotaFiscais();


			for (Object listaVolume : listaVolumes) {
				final VolumeNotaFiscal vnf = (VolumeNotaFiscal) listaVolume;
				strLog.append(vnf.getNrVolumeColeta() + ",");

				if (idMonitoramentoDescarga == null && vnf.getMonitoramentoDescarga() != null) {
					idMonitoramentoDescarga = vnf.getMonitoramentoDescarga().getIdMonitoramentoDescarga();
					retorno.put("idMonitoramentoDescarga", idMonitoramentoDescarga);
				}

			}
			strLog.append("], ");

			//LMS-795: por questao de concorrencia valida novamente
			List<Integer> nrNotasFiscais = new ArrayList<>(1);
			nrNotasFiscais.add(notaFiscalConhecimento.getNrNotaFiscal());
			List<YearMonthDay> dtEmissoes = new ArrayList<>(1);
			dtEmissoes.add(notaFiscalConhecimento.getDtEmissao());
			List<String> dsSeries = new ArrayList<>();
			dsSeries.add(notaFiscalConhecimento.getDsSerie());

			// LMSA-6598
			if(!BooleanUtils.isTrue(conhecimento.getBlRedespachoIntermediario())) {
				verifyNrNotasFiscais(conhecimento.getClienteByIdClienteRemetente().getPessoa().getIdPessoa(), nrNotasFiscais, dtEmissoes, conhecimento.getIdDoctoServico(),dsSeries);
				validateChaveNfe(notaFiscalConhecimento.getNrChave(), Long.valueOf(notaFiscalConhecimento.getCliente().getPessoa().getNrIdentificacao()), notaFiscalConhecimento.getNrNotaFiscal(), notaFiscalConhecimento.getDtEmissao(), conhecimento.getIdDoctoServico());
			}
		}
		log.warn(strLog);

		conhecimento.setBlObrigaAgendamento(obrigaAgendamentoConhecimento);

		/** Remove objeto de suas coleções de cache de primeiro nível */
		super.flush();
		getDao().getAdsmHibernateTemplate().evict(conhecimento);



		this.unitizacaoService.storeUnitizarVolumesConhecimento(conhecimento);
		return retorno;
	}

	// LMSA-6598: LMSA-7137
	@SuppressWarnings("rawtypes")
	private void verificarChavesNotaUtilizadaOutroCte(String chave, Long idClienteRemetente) {
		if(chave != null && !chave.isEmpty()) {
			List result = notaFiscalConhecimentoService.findByNrChaveEClienteRemetente(chave, idClienteRemetente);
			if(result != null && !result.isEmpty()) {
				throw new BusinessException("LMS-04202", new Object[]{chave});
			}
		}
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	private boolean executeBuildMapEndereco(final String nome, final String ie, final Long cnpj, final String endereco, final String bairro,
											final Integer cepEnder, final String uf, final HashMap<String, Object> mapStoreCliente, boolean atualizaIETipoTributacao, String tpCliente) {
		Map<String, Object> mapMunicipio = new HashMap<String, Object>();

		// LMS-796: Corrigido por Luis Carlos Poletto, validacao de endereco de cliente
		String nrEndereco = getNrEndereco(new StringBuilder(endereco));
		if (nrEndereco.length() > 5) {
			throw new BusinessException("LMS-28007 ", new Object[] {getLocalizedTpCliente(tpCliente)});
		}

		final Long idMunicipio = findIdMunicipioBRAByCep(cepEnder);
		if (idMunicipio == null) {
			throw new BusinessException("LMS-04299", new Object[] {cepEnder});
		}


		final Municipio municipio = municipioService.findById(idMunicipio);

		final List<Map<String, Object>> municipioList = municipioService.findByNmMunicipioBySgUfByTpSituacao(municipio.getNmMunicipio(),
				municipio.getUnidadeFederativa().getSgUnidadeFederativa(), ConstantesVendas.SITUACAO_ATIVO);
		if((municipioList != null) && (municipioList.size() == 1)) {
			mapMunicipio = (Map<String, Object>) municipioList.get(0);
		} else if((municipioList != null) && (municipioList.size() > 1)) {
			throw new BusinessException("LMS-04272", new Object[] { cepEnder, uf });
		} else if((municipioList == null) || (municipioList.size() <= 0)) {
			throw new BusinessException("LMS-04273", new Object[] { cepEnder, uf });
		}

		mapStoreCliente.put("nmPessoa", nome);
		mapStoreCliente.put("nrInscricaoEstadual", ie);
		mapStoreCliente.put("atualizaIETipoTributacao", atualizaIETipoTributacao);

		// valida se é cpf
		if((cnpj.toString().length() <= 11) && ValidateUtils.validateCpfOrCnpj(fillNumberWithZero(cnpj.toString(), 11))) {
			mapStoreCliente.put("tpIdentificacao", "CPF");
			mapStoreCliente.put("tpPessoa", "F");
			mapStoreCliente.put("nrIdentificacao", FormatUtils.formatCPF(fillNumberWithZero(cnpj.toString(), 11)));
		} else {
			mapStoreCliente.put("nrIdentificacao", FormatUtils.formatCNPJ(fillNumberWithZero(cnpj.toString(), 14)));
			mapStoreCliente.put("tpIdentificacao", "CNPJ");
			mapStoreCliente.put("tpPessoa", "J");
		}

		mapStoreCliente.put("idTipoLogradouro", getIdTipoLogradouro(new StringBuilder(endereco)));
		mapStoreCliente.put("dsTipoLogradouro", null);
		mapStoreCliente.put("nrEndereco", nrEndereco);
		mapStoreCliente.put("dsEndereco", endereco);

		mapStoreCliente.put("dsBairro", bairro);
		mapStoreCliente.put("idMunicipio", mapMunicipio.get("idMunicipio"));
		final Map mapUnidadeFederativa = (Map) mapMunicipio.get("unidadeFederativa");
		mapStoreCliente.put("idUnidadeFederativa", mapUnidadeFederativa.get("idUnidadeFederativa"));
		final Map mapPais = (Map) mapUnidadeFederativa.get("pais");
		mapStoreCliente.put("idPais", mapPais.get("idPais"));
		mapStoreCliente.put("nrCep", String.valueOf(cepEnder));

		mapStoreCliente.put("idEnderecoPessoa", null);
		mapStoreCliente.put("dsComplemento", null);
		if("ISENTO".equals(ie)) {
			mapStoreCliente.put("tpSituacaoTributaria", ConstantesExpedicao.CD_SIT_TRIB_ISENTO);
		} else {
			mapStoreCliente.put("tpSituacaoTributaria", ConstantesExpedicao.CD_COFINS);
		}
		mapStoreCliente.put("blIsento", false);
		mapStoreCliente.put("idRamoAtividade", Long.valueOf(3)); // Estabelecimento
		// Comercial,
		// qual
		// Ramo de
		// atividade
		// deve ser
		// colocado?
		mapStoreCliente.put("blObrigaSerie", false);
		mapStoreCliente.put("idPessoa", null);
		mapStoreCliente.put("nrDdd", null);
		mapStoreCliente.put("blPermiteCte", false);

		mapStoreCliente.put("nmContato", null);
		mapStoreCliente.put("nrTelefone", null);

		return true;
	}

	private Long getIdTipoLogradouro(final StringBuilder dsEndereco) {
		return Long.valueOf(EnderecoPessoaUtils.getTipoLogradouro(dsEndereco));
	}

	public Map<String, Object> executeBuildMapConhecimento(final Cliente clienteRemetente,
														   final Long idInscricaoEstadualRemetente,
														   final NotaFiscalEdi notaFiscalEdi,
														   final String opcaoProcessamento,
														   final Map<String, Object> mapMeioTransporte,
														   final DateTime dhChegada,
														   final List<Map> listNumeroEtiquetasEdiInformada,
														   final List<ValidarEdiDTO> listNotasDtoASeremAgrupadas,
														   final String nrCae,
														   Long nrProcessamento,
														   final String tpProcessamento,
														   String processarPor,
														   final Long idDivisaoCliente,
														   final boolean blOperacaoSpitFire) {
		return executeBuildMapConhecimento(clienteRemetente, idInscricaoEstadualRemetente, notaFiscalEdi,
				opcaoProcessamento, mapMeioTransporte, dhChegada, listNumeroEtiquetasEdiInformada, listNotasDtoASeremAgrupadas,
				nrCae, nrProcessamento, tpProcessamento, processarPor, idDivisaoCliente, blOperacaoSpitFire, null, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Map<String, Object> executeBuildMapConhecimento(final Cliente clienteRemetente,
														   final Long idInscricaoEstadualRemetente,
														   final NotaFiscalEdi notaFiscalEdi,
														   final String opcaoProcessamento,
														   final Map<String, Object> mapMeioTransporte,
														   final DateTime dhChegada,
														   final List<Map> listNumeroEtiquetasEdiInformada,
														   final List<ValidarEdiDTO> listNotasDtoASeremAgrupadas,
														   final String nrCae,
														   Long nrProcessamento,
														   final String tpProcessamento,
														   String processarPor,
														   final Long idDivisaoCliente,
														   final boolean blOperacaoSpitFire, Long idFilial, Long idUsuario){
		final EnderecoPessoa enderecoRemetente = enderecoPessoaService.findByIdPessoa(clienteRemetente.getIdCliente());
		final String nrIdentificacaoDest = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjDest().toString());
		final String nrIdentificacaoRem = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjReme().toString());
		String nrIdentificacaoRedesp = null;

		final Cliente clienteDestinatario = clienteService.findByNrIdentificacao(nrIdentificacaoDest);
		final Long idMunicipioDestinatario = getIdMunicipioByClient(clienteDestinatario);
		Long idServico = calculaSiglaServicoId(notaFiscalEdi);


		final Map<String, Object> parameters = new HashMap<String, Object>();
		Long idPais = SessionUtils.getPaisSessao() != null ? SessionUtils.getPaisSessao().getIdPais() : paisService.findPaisByIdFilialSessao(idFilial).getIdPais();

		parameters.put("nrConhecimentoSubcontratante", notaFiscalEdi.getNrCtrcSubcontratante());
		parameters.put("nrConhecimentoDoctoServicoOriginal", null);
		parameters.put("nrCepDestinatario", String.valueOf(notaFiscalEdi.getCepEnderDest()));
		parameters.put("tpModalServico", notaFiscalEdi.getModalFrete() != null ? notaFiscalEdi.getModalFrete() : "R");
		parameters.put("idUnidadeFederativaRemetente", enderecoRemetente.getMunicipio().getUnidadeFederativa()
				.getIdUnidadeFederativa());
		parameters.put("nrIdentificacaoRemetente", nrIdentificacaoRem);
		parameters.put("idServico", idServico);
		parameters.put("nrCtoCooperada", null);
		parameters.put("tpCalculoPreco", ConstantesExpedicao.CALCULO_NORMAL);
		parameters.put("nrIdentificacaoDestinatario", nrIdentificacaoDest);
		parameters.put("blServicosAdicionais", false);
		parameters.put("tpFrete", "CIF".equals(notaFiscalEdi.getTipoFrete()) ? ConstantesExpedicao.TP_FRETE_CIF : ConstantesExpedicao.TP_FRETE_FOB);
		parameters.put("idMunicipioDestinatario", idMunicipioDestinatario);
		parameters.put("idProdutoEspecifico", null);
		parameters.put("idDoctoServico", null);
		parameters.put("nrCepRemetente", enderecoRemetente.getNrCep());
		parameters.put("idInscricaoEstadualRemetente", idInscricaoEstadualRemetente);
		parameters.put("idCotacao", null);
		parameters.put("tpClienteRemetente", clienteRemetente.getTpCliente().getValue());
		parameters.put("dsCodigoColeta", null);
		parameters.put("idFilialOrigemDoctoServicoOriginal", null);
		parameters.put("idNaturezaProduto", null);
		parameters.put("dsNatureza", notaFiscalEdi.getNatureza());
		parameters.put("idMunicipioRemetente", enderecoRemetente.getMunicipio().getIdMunicipio());
		parameters.put("tpClienteDestinatario", clienteDestinatario == null ? null : clienteDestinatario.getTpCliente().getValue());
		parameters.put("idFilial", null);
		parameters.put("quantidadeNotasFiscais", listNotasDtoASeremAgrupadas.size());
		parameters.put("blEntregaEmergencia", false);
		parameters.put("idUnidadeFederativaDestinatario", findIdUnidadeFederativaBySgUf(notaFiscalEdi.getUfDest(), idPais));
		parameters.put("idClienteDestinatario", clienteDestinatario == null ? null : clienteDestinatario.getIdCliente());
		parameters.put("clientesSession", null);
		parameters.put("blFrete", true);
		parameters.put("meioTransporte", mapMeioTransporte);
		parameters.put("idPedidoColeta", mapMeioTransporte != null ? mapMeioTransporte.get("idPedidoColeta") : null);
		parameters.put("dhChegada", dhChegada);
		parameters.put("tpAbrangenciaServico", ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		parameters.put("idInscricaoEstadualDestinatario", calcIdInscricaoEstadual(clienteDestinatario));

		parameters.put("idEmpresa", null);
		parameters.put("idDoctoServicoOriginal", null);
		parameters.put("idClienteRemetente", clienteRemetente.getIdCliente());
		parameters.put("clienteRemetente",clienteRemetente);
		parameters.put("idFilialDestino", null);

		if("A".equals(notaFiscalEdi.getModalFrete())) {
			Long idFilialDestinatario = findFilialAtendimento(parameters);
			parameters.put("idAeroportoOrigem", findAeroportoOrigemByFilial(idFilial).get("idAeroporto"));
			parameters.put("idAeroportoDestino", findAeroportoDestinoByFilial(idFilialDestinatario).get("idAeroporto"));
		}

		parameters.put("blColetaEmergencia", false);
		parameters.put("blCotacaoRemetente", false);
		parameters.put("psCubado", notaFiscalEdi.getPesoCubado());

		if (idDivisaoCliente != null) {
			parameters.put("idDivisaoCliente", idDivisaoCliente);
		} else {
			Long idCliente = clienteRemetente.getIdCliente();
			if (ConstantesVendas.CLIENTE_FILIAL.equals(clienteRemetente.getTpCliente().getValue())) {
				idCliente = clienteRemetente.getClienteMatriz().getIdCliente();
			}

			DivisaoCliente divisaoCliente = getDivisaoCliente(clienteRemetente, notaFiscalEdi, clienteDestinatario, idServico);

			if (divisaoCliente != null) {
				parameters.put("idDivisaoCliente", divisaoCliente.getIdDivisaoCliente());
			}
		}

		final Conhecimento conhecimento = new Conhecimento();
		conhecimento.setBlIndicadorEdi(Boolean.TRUE);
		conhecimento.setTpCalculoPreco(new DomainValue(ConstantesExpedicao.CALCULO_NORMAL));
		conhecimento.setNrOrdemEmissaoEDI(findNextNrOrdemEmissaoEDI());


		this.setProdutosDiferenciados(conhecimento, listNotasDtoASeremAgrupadas);

		boolean blAtualizaDestinatarioEdi = BooleanUtils.isTrue(clienteRemetente.getBlAtualizaDestinatarioEdi());
		boolean blAtualizaConsignatarioEdi = BooleanUtils.isTrue(clienteRemetente.getBlAtualizaConsignatarioEdi());
		if (processarPor!= null && "T".equals(processarPor)) {
			Cliente clienteTomador = clienteService.findByNrIdentificacao(getValidatedNrIdentificacao(String.valueOf(notaFiscalEdi.getCnpjTomador())));
			blAtualizaDestinatarioEdi = BooleanUtils.isTrue(clienteTomador.getBlAtualizaDestinatarioEdi());
			blAtualizaConsignatarioEdi = BooleanUtils.isTrue(clienteTomador.getBlAtualizaConsignatarioEdi());
			blAtualizaConsignatarioEdi = clienteTomador.getBlAtualizaConsignatarioEdi();
			InscricaoEstadual ie = inscricaoEstadualService.findIeByIdPessoaAtivoPadrao(clienteRemetente.getIdCliente());
			parameters.put("idInscricaoEstadualRemetente", ie.getIdInscricaoEstadual());
			parameters.put("idClienteTomador", clienteTomador.getIdCliente());
			conhecimento.setBlProcessamentoTomador(Boolean.TRUE);
		}

		if (Boolean.TRUE.equals(blAtualizaDestinatarioEdi)){
			parameters.put("dadosEnderecoEntregaReal",atualizaDadosEnderecoDestinatario(notaFiscalEdi, idUsuario));
		}

		if (Boolean.TRUE.equals(blAtualizaConsignatarioEdi)){
			atualizaDadosEnderecoConsignatario(notaFiscalEdi, idUsuario);
		}

		if (StringUtils.isNotBlank(nrCae)) {
			conhecimento.setNrCae(nrCae);
		}

		if((listNumeroEtiquetasEdiInformada != null)
				&& (listNumeroEtiquetasEdiInformada.size() > 0)
				&& (listNotasDtoASeremAgrupadas != null)
				&& (listNotasDtoASeremAgrupadas.size() > 0)) {
			for (final Map mapNumeroEtiqueta : listNumeroEtiquetasEdiInformada) {
				for (final ValidarEdiDTO dto : listNotasDtoASeremAgrupadas) {
					if(mapNumeroEtiqueta.containsKey("palete" + dto.getNotaFiscalEdi().getNrNotaFiscal())) {
						final Boolean isPaletizacao = (Boolean) mapNumeroEtiqueta.get("palete" + dto.getNotaFiscalEdi().getNrNotaFiscal());
						if(BooleanUtils.isTrue(isPaletizacao)) {
							conhecimento.setBlPaletizacao(true);
							break;
						}
					}
				}
			}
		}

		//LMS-2353
		BigDecimal nrCubagemDeclarada = BigDecimal.ZERO;

		if (listNotasDtoASeremAgrupadas != null) {
			List<String> chavesNotas = new ArrayList<String> ();
			for (final ValidarEdiDTO dto : listNotasDtoASeremAgrupadas) {

				if (BigDecimalUtils.hasValue(dto.getNotaFiscalEdi().getPesoCubado())) {
					nrCubagemDeclarada = nrCubagemDeclarada.add(dto.getNotaFiscalEdi().getPesoCubado());
				}
				chavesNotas.add(notaFiscalEdi.getChaveNfe());
			}
			//LMS-8345

			while(chavesNotas.size()>999){
				List<String> sublist = new ArrayList<String>(chavesNotas.subList(0, 999));
				this.adicionarIndicadorPaletizacao(conhecimento, sublist);
				chavesNotas.removeAll(sublist);
			}

			this.adicionarIndicadorPaletizacao(conhecimento, chavesNotas);

		}
		conhecimento.setNrCubagemDeclarada(nrCubagemDeclarada);
		conhecimento.setBlOperacaoSpitFire(blOperacaoSpitFire);

		parameters.put("conhecimento", conhecimento);

		Cliente clienteRedesp = null;
		if (notaFiscalEdi.getCnpjRedesp() != null){
			nrIdentificacaoRedesp = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjRedesp().toString());
			clienteRedesp = clienteService.findByNrIdentificacao(nrIdentificacaoRedesp);
			conhecimento.setClienteByIdClienteRedespacho(clienteRedesp);
			parameters.put("ieRedesp", notaFiscalEdi.getIeRedesp());
		}

		String tpDevedorFrete = null;
		if((notaFiscalEdi.getCnpjTomador() != null) && (notaFiscalEdi.getCnpjTomador().longValue() > 0)) {
			final String nrIdentificacaoTomador = getValidatedNrIdentificacao(String.valueOf(notaFiscalEdi.getCnpjTomador()));

			if(!nrIdentificacaoTomador.equals(parameters.get("nrIdentificacaoRemetente")) &&
					!nrIdentificacaoTomador.equals(parameters.get("nrIdentificacaoDestinatario")) &&
					(nrIdentificacaoRedesp == null || !nrIdentificacaoTomador.equals(nrIdentificacaoRedesp))){
				tpDevedorFrete = ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL;

				final Cliente clienteTomador = clienteService.findByNrIdentificacao(nrIdentificacaoTomador);

				final InscricaoEstadual inscricaoEstadual = inscricaoEstadualService.findByPessoaIndicadorPadrao(clienteTomador.getIdCliente(), Boolean.TRUE);

				final DevedorDocServ devedor = new DevedorDocServ();
				devedor.setCliente(clienteTomador);
				devedor.setInscricaoEstadual(inscricaoEstadual);

				final List<DevedorDocServ> devedores = new ArrayList<DevedorDocServ>(1);
				devedores.add(devedor);
				conhecimento.setDevedorDocServs(devedores);
			}
		}
		if(tpDevedorFrete != null) {
			parameters.put("tpDevedorFrete", tpDevedorFrete);
		} else {
			parameters.put("tpDevedorFrete",
					notaFiscalEdi.getCnpjRedesp() != null && !isTpProcessamentoPorManifestoConsol(tpProcessamento) ?
							ConstantesExpedicao.TP_DEVEDOR_REDESPACHO
							: "CIF".equals(notaFiscalEdi.getTipoFrete()) ? ConstantesExpedicao.TP_DEVEDOR_REMETENTE
							: "FOB".equals(notaFiscalEdi.getTipoFrete()) ? ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO
							: ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL);
		}

		if (isTpProcessamentoPorManifestoConsol(tpProcessamento)) {
			conhecimento.setBlRedespachoIntermediario(true);
		}

		// Se CNPJ do destinatário for diferente do CNPJ do consiganatário,
		// assumir consignatário do EDI como consignatário no conhecimento
		if((notaFiscalEdi.getCnpjDest() != null) && (notaFiscalEdi.getCnpjConsig() != null) &&
				!notaFiscalEdi.getCnpjDest().equals(notaFiscalEdi.getCnpjConsig())) {
			final HashMap<String, Object> consignatarioMap = new HashMap<String, Object>();

			final String nrIdentificacaoConsig = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjConsig().toString());

			final Cliente clienteConsig = clienteService.findByNrIdentificacao(nrIdentificacaoConsig);

			consignatarioMap.put("idCliente", clienteConsig.getIdCliente());
			consignatarioMap.put("dsComplemento", null);
			consignatarioMap.put("dsEndereco", notaFiscalEdi.getEnderecoConsig());
			consignatarioMap.put("nrEndereco", getNrEndereco(new StringBuilder(notaFiscalEdi.getEnderecoConsig())));
			consignatarioMap.put("nrCep", notaFiscalEdi.getCepEnderConsig());

			final Map<String, Object> clientesSession = new HashMap<String, Object>();
			clientesSession.put(ConstantesExpedicao.CLIENTE_CONSIGNATARIO_IN_SESSION, consignatarioMap);
			parameters.put("clientesSession", clientesSession);
			parameters.put("blUsaEnderecoConsignatario", Boolean.TRUE);

			conhecimento.setClienteByIdClienteConsignatario(clienteConsig);

			// Se CNPJ do consiganatário for igual ao CNPJ do destinatário,
			// assumir dados do consigantário do EDI como local de entrega no
			// conhecimento
		} else if((notaFiscalEdi.getCnpjDest() != null) && (notaFiscalEdi.getCnpjConsig() != null) && notaFiscalEdi.getCnpjDest().equals(notaFiscalEdi.getCnpjConsig())) {

			conhecimento.setDsBairroEntrega(notaFiscalEdi.getBairroConsig());
			conhecimento.setDsComplementoEntrega(null);
			conhecimento.setDsEnderecoEntrega(notaFiscalEdi.getEnderecoConsig());
			String nrCep = FormatUtils.fillNumberWithZero(String.valueOf(notaFiscalEdi.getCepEnderConsig()), 8);
			conhecimento.setNrCepEntrega(nrCep);
			parameters.put("blUsaEnderecoConsignatario", Boolean.TRUE);
			final Long idMunicipio = findIdMunicipioBRAByCep(notaFiscalEdi.getCepEnderConsig());
			if(idMunicipio != null) {
				final Municipio municipio = municipioService.findByIdInitLazyProperties(idMunicipio, false);
				conhecimento.setMunicipioByIdMunicipioEntrega(municipio);
			} else {
				conhecimento.setMunicipioByIdMunicipioEntrega(findMunicipioByNrIdentificacao(notaFiscalEdi.getCnpjConsig().toString()));
			}

			if (Boolean.TRUE.equals(conhecimento.getBlRedespachoIntermediario())){
				final String nrIdentificacaoConsig = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjConsig().toString());

				final Cliente clienteConsig = clienteService.findByNrIdentificacao(nrIdentificacaoConsig);

				conhecimento.setClienteByIdClienteConsignatario(clienteConsig);
			}
		}

		final List listNotaFiscalConhecimento = new ArrayList();
		listNotaFiscalConhecimento.addAll(buildMapNotaFiscalConhecimento(parameters, opcaoProcessamento, listNumeroEtiquetasEdiInformada, listNotasDtoASeremAgrupadas, tpProcessamento));
		parameters.put("notaFiscalConhecimento", listNotaFiscalConhecimento);
		parameters.putAll(buildDadosComplementos(conhecimento, listNotasDtoASeremAgrupadas,parameters));

		buildObservacaoDoctoServico(conhecimento, parameters);

		/*
		 * Quest CQPRO00028706 Se o cliente do EDI tiver o campo
		 * CLIENTE.BL_UTILIZA_FRETE_EDI = ‘S’ e o campo
		 * NOTA_FISCAL_EDI.VLR_FRETE_TOTAL estiver preenchido,
		 */
		if((clienteRemetente != null) && (clienteRemetente.getBlUtilizaFreteEDI() != null) && clienteRemetente.getBlUtilizaFreteEDI() && (notaFiscalEdi.getVlrFreteTotal() != null)) {
			parameters.put("tpCalculoPreco", "M");
		}

		parameters.put("tpProcessamento", tpProcessamento);

		return parameters;
	}

	private void adicionarIndicadorPaletizacao(final Conhecimento conhecimento, List<String> chavesNotas) {
		if(notIsPaletizado(conhecimento)  && !chavesNotas.isEmpty()) {
			conhecimento.setBlPaletizacao(cceItemService.findExistsNotasPaletizadas(chavesNotas));
		}
	}

	private boolean notIsPaletizado(final Conhecimento conhecimento) {
		return conhecimento.getBlPaletizacao() == null || !conhecimento.getBlPaletizacao();
	}

	/**
	 * Verifica se a Nota Foi paletizaca no Posto Avancado.
	 * @param notaFiscalEdi
	 * @return
	 */
	private boolean isNotaPaletizadaPA(NotaFiscalEdi notaFiscalEdi) {
		if(notaFiscalEdi == null) {
			return false;
		}
		return cceItemService.findExistsNotasPaletizadas(Arrays.asList(notaFiscalEdi.getChaveNfe()));
	}


	private void setProdutosDiferenciados(Conhecimento conhecimento, List<ValidarEdiDTO> listNotasDtoASeremAgrupadas) {
		for (ValidarEdiDTO dto : listNotasDtoASeremAgrupadas) {
			if(Boolean.TRUE.equals(dto.getIsControladoExercito())
					|| Boolean.TRUE.equals(dto.getIsControladoPoliciaCivil())
					|| Boolean.TRUE.equals(dto.getIsControladoPoliciaFederal())){
				conhecimento.setBlProdutoControlado(Boolean.TRUE);
				if(Boolean.TRUE.equals(conhecimento.getBlProdutoPerigoso())){
					break;
				}
			}else if(conhecimento.getBlProdutoControlado() == null
					&& (Boolean.FALSE.equals(dto.getIsControladoExercito())
					|| Boolean.FALSE.equals(dto.getIsControladoPoliciaCivil())
					|| Boolean.FALSE.equals(dto.getIsControladoPoliciaFederal()))){
				conhecimento.setBlProdutoControlado(Boolean.FALSE);
			}

			if(Boolean.TRUE.equals(dto.getIsProdutoPerigoso())){
				conhecimento.setBlProdutoPerigoso(Boolean.TRUE);
				if(Boolean.TRUE.equals(conhecimento.getBlProdutoControlado())){
					break;
				}
			}else if(conhecimento.getBlProdutoPerigoso() == null
					&& Boolean.FALSE.equals(dto.getIsProdutoPerigoso())){
				conhecimento.setBlProdutoPerigoso(Boolean.FALSE);
			}
		}
	}

	public DivisaoCliente getDivisaoCliente(final Cliente clienteRemetente, final NotaFiscalEdi notaFiscalEdi, final Cliente clienteDestinatario, Long idServico) {
		DivisaoCliente divisaoCliente = null;
		if(notaFiscalEdi.getDsDivisaoCliente()!=null && !"".equals(notaFiscalEdi.getDsDivisaoCliente().trim())) {
			if(notaFiscalEdi.getCnpjTomador()!=null) {
				Cliente tomador = findClienteByNrIdentificacaoEDI(notaFiscalEdi.getCnpjTomador().toString());
				if(tomador!=null) {
					divisaoCliente = this.findDivisaoCliente(tomador,idServico,notaFiscalEdi.getDsDivisaoCliente().toLowerCase());
				}
			} else if("CIF".equals(notaFiscalEdi.getTipoFrete())) {
				divisaoCliente = this.findDivisaoCliente(clienteRemetente,idServico,notaFiscalEdi.getDsDivisaoCliente().toLowerCase());
			} else if("FOB".equals(notaFiscalEdi.getTipoFrete())) {

				boolean condicoesFOBSatisfeitas = false;

				if("E".equals(clienteDestinatario.getTpCliente().getValue()) || "P".equals(clienteDestinatario.getTpCliente().getValue())) {

					if(("S".equals(clienteRemetente.getTpCliente().getValue()) || "F".equals(clienteRemetente.getTpCliente().getValue()))
							&& clienteRemetente.getBlFobDirigido()!=null && clienteRemetente.getBlFobDirigido()) {

						condicoesFOBSatisfeitas = true;
						divisaoCliente = this.findDivisaoCliente(clienteRemetente,idServico,notaFiscalEdi.getDsDivisaoCliente().toLowerCase());

					}

				}

				if(!condicoesFOBSatisfeitas) {
					divisaoCliente = this.findDivisaoCliente(clienteDestinatario,idServico,notaFiscalEdi.getDsDivisaoCliente().toLowerCase());
				}

			}

		} else {

			if(notaFiscalEdi.getCnpjTomador()!=null) {
				Cliente tomador = clienteService.findByNrIdentificacao(FormatUtils.formatLongWithZeros(notaFiscalEdi.getCnpjTomador(), "00000000000000"));
				if(tomador!=null) {
					divisaoCliente = this.obterMenorDivisaoCliente(tomador,idServico);
				}
			} else if("CIF".equals(notaFiscalEdi.getTipoFrete())) {
				divisaoCliente = this.obterMenorDivisaoCliente(clienteRemetente,idServico);
			}else if("FOB".equals(notaFiscalEdi.getTipoFrete())) {
				divisaoCliente = this.obterMenorDivisaoCliente(clienteDestinatario,idServico);
			}

		}
		return divisaoCliente;
	}

	private Long getIdMunicipioByClient(final Cliente clienteDestinatario) {
		if(clienteDestinatario != null) {
			return clienteDestinatario.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
		}
		return null;
	}

	private Long calcIdInscricaoEstadual(final Cliente cliente) {
		final InscricaoEstadual inscDest = cliente == null ? null : inscricaoEstadualService.findByPessoaIndicadorPadrao(
				cliente.getIdCliente(), Boolean.TRUE);

		Long idInscricaoEstadual = inscDest == null ? null : inscDest.getIdInscricaoEstadual();

		return idInscricaoEstadual;
	}

	private Long calculaSiglaServicoId(final NotaFiscalEdi notaFiscalEdi) {
		String siglaServico = notaFiscalEdi.getModalFrete() != null ? notaFiscalEdi.getModalFrete() : "R";
		siglaServico = "A".equals(siglaServico) ? ConstantesExpedicao.AEREO_NACIONAL_CONVENCIONAL : ConstantesExpedicao.RODOVIARIO_NACIONAL_CONVENCIONAL;

		return findIdServicoBySigla(siglaServico);
	}

	/**
	 * Faz a busca do cliente baseando-se no nrIdentificacao do EDI, que sendo do tipo numerico na
	 * base de dados omite os zeros a esquerda.
	 *
	 * @param nrIdentificacaoEDI CPF ou CNPJ em formato numerico sem zeros a esquerda.
	 * @return
	 */
	private Cliente findClienteByNrIdentificacaoEDI(String nrIdentificacaoEDI){
		String nrIdentificacao = FormatUtils.fillNumberWithZero(nrIdentificacaoEDI, 14);
		Cliente cliente = null;
		if(ValidateUtils.validateCpfOrCnpj(nrIdentificacao)) {
			cliente = clienteService.findByNrIdentificacao(nrIdentificacao);
		}
		//Faz a verificação se o cliente é nulo, pois há casos onde mesmo acrescentando zeros,
		//o metodo validateCpfCnpj retorna true. Entao busca novamente formatando o nrIdentificação
		//para 11 digitos.
		if (cliente == null){
			nrIdentificacao = FormatUtils.fillNumberWithZero(nrIdentificacaoEDI, 11);
			cliente = clienteService.findByNrIdentificacao(nrIdentificacao);
		}

		return cliente;

	}

	private Long findNextNrOrdemEmissaoEDI(){
		return notaFiscalExpedicaoEDIService.findNextNrOrdemEmissaoEDI();
	}

	/**
	 * Atualiza os dados do destinatário da nota fiscal e retorna os dados do endereço em um String para
	 * inserir no local de entrega do conhecimento.
	 *
	 * @param notaFiscalEdi
	 * @return
	 */
	private String atualizaDadosEnderecoDestinatario(NotaFiscalEdi notaFiscalEdi, Long idUsuario) {
		String nrIdentificacao = FormatUtils.fillNumberWithZero(notaFiscalEdi.getCnpjDest().toString(), 14);
		Cliente clienteDestinatario = null;
		if(ValidateUtils.validateCpfOrCnpj(nrIdentificacao)) {
			clienteDestinatario = clienteService.findByNrIdentificacao(nrIdentificacao);
		}
		//Faz a verificação se o cliente é nulo, pois há casos onde mesmo acrescentando zeros,
		//o metodo validateCpfCnpj retorna true. Entao busca novamente formatando o nrIdentificação
		//para 11 digitos.
		if (clienteDestinatario == null){
			nrIdentificacao = FormatUtils.fillNumberWithZero(notaFiscalEdi.getCnpjDest().toString(), 11);
			clienteDestinatario = clienteService.findByNrIdentificacao(nrIdentificacao);
		}

		StringBuilder builder = new StringBuilder();
		if(clienteDestinatario != null) {
			// Cliente Potencial ou Eventualç
			if(clienteDestinatario.getTpCliente().getValue().equals("E") || clienteDestinatario.getTpCliente().getValue().equals("P")) {
				Municipio municipioDestino = findMunicipio(notaFiscalEdi.getMunicipioDest(), notaFiscalEdi.getUfDest(), notaFiscalEdi.getCepEnderDest().toString());
				final EnderecoPessoa enderecoPessoa = enderecoPessoaService.findById(clienteDestinatario.getPessoa().getEnderecoPessoa().getIdEnderecoPessoa());
				enderecoPessoa.setDsBairro(notaFiscalEdi.getBairroDest());
				enderecoPessoa.setDsEndereco(notaFiscalEdi.getEnderecoDest());
				builder.append(enderecoPessoa.getDsEndereco());
				//Apenas o endereço é inserido aqui pois o numero já vem concatenado no campo endereço do EDI.
				//Na tabela nota_fiscal_edi não possui campo para numero do logradouro.
				if (enderecoPessoa.getDsBairro() != null && !(enderecoPessoa.getDsBairro().length() == 0)){
					builder.append(" - ").append(enderecoPessoa.getDsBairro());
				}
				if(municipioDestino != null) {
					enderecoPessoa.setMunicipio(municipioDestino);
					builder.append(" - ").append(enderecoPessoa.getMunicipio().getNmMunicipio());
					builder.append("/").append(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
				}

				if (notaFiscalEdi.getCepEnderDest()!= null){
					enderecoPessoa.setNrCep(notaFiscalEdi.getCepEnderDest().toString());
					builder.append(" CEP ").append(enderecoPessoa.getNrCep());
				}
				enderecoPessoaService.store(enderecoPessoa, null, true, buscarUsuarioPorId(idUsuario));
			}

		}
		return builder.toString();
	}

	// LMSA-3544
	private void atualizaDadosEnderecoConsignatario(NotaFiscalEdi notaFiscalEdi, Long idUsuario) {
		if(notaFiscalEdi.getCnpjConsig() != null){

			String nrIdentificacao = FormatUtils.fillNumberWithZero(notaFiscalEdi.getCnpjConsig().toString(), 14);
			Cliente clienteConsignatario = null;
			if(ValidateUtils.validateCpfOrCnpj(nrIdentificacao)) {
				clienteConsignatario = clienteService.findByNrIdentificacao(nrIdentificacao);
			}
			//Faz a verificação se o cliente é nulo, pois há casos onde mesmo acrescentando zeros,
			//o metodo validateCpfCnpj retorna true. Entao busca novamente formatando o nrIdentificação
			//para 11 digitos.
			if (clienteConsignatario == null){
				nrIdentificacao = FormatUtils.fillNumberWithZero(notaFiscalEdi.getCnpjConsig().toString(), 11);
				clienteConsignatario = clienteService.findByNrIdentificacao(nrIdentificacao);
			}

			if(clienteConsignatario != null) {
				// Cliente Potencial ou Eventualç
				if(clienteConsignatario.getTpCliente().getValue().equals("E") || clienteConsignatario.getTpCliente().getValue().equals("P")) {

					Municipio municipioDestino = findMunicipio(notaFiscalEdi.getMunicipioConsig(), notaFiscalEdi.getUfConsig(), notaFiscalEdi.getCepEnderConsig().toString());
					final EnderecoPessoa enderecoPessoa = enderecoPessoaService.findById(clienteConsignatario.getPessoa().getEnderecoPessoa().getIdEnderecoPessoa());
					enderecoPessoa.setDsBairro(notaFiscalEdi.getBairroConsig());
					enderecoPessoa.setDsEndereco(notaFiscalEdi.getEnderecoConsig());

					if(municipioDestino != null) {
						enderecoPessoa.setMunicipio(municipioDestino);
					}

					if (notaFiscalEdi.getCepEnderConsig()!= null){
						enderecoPessoa.setNrCep(notaFiscalEdi.getCepEnderConsig().toString());
					}
					enderecoPessoaService.store(enderecoPessoa, null, true, buscarUsuarioPorId(idUsuario));
				}
			}

		}
	}

	private Municipio findMunicipio(String municipio, String uf, String cep) {
		Municipio municipioDestino = municipioService.findByIntervaloCepByUf(cep, null);
		if(municipioDestino == null) {
			municipioDestino = municipioService.findByNmMunicipioAndUf(municipio, uf);
		}
		return municipioDestino;
	}

	@SuppressWarnings("rawtypes")
	public void buildObservacaoDoctoServico(final Conhecimento conhecimento, Map parameters) {
		final ObservacaoDoctoServico observacaoDoctoServico = new ObservacaoDoctoServico();
		observacaoDoctoServico.setBlPrioridade(Boolean.FALSE);
		observacaoDoctoServico.setDoctoServico(conhecimento);
		observacaoDoctoServico.setDsObservacaoDoctoServico((String) parametroGeralService.findConteudoByNomeParametro("OBSERVACAO EDI", false));
		observacaoDoctoServico.setIdObservacaoDoctoServico(null);
		conhecimento.addObservacaoDoctoServico(observacaoDoctoServico);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map buildDadosComplementos(final Conhecimento conhecimento, final List<ValidarEdiDTO> listNotaFiscalDtoEdis, Map parameters) {
		final Map mapReturn = new HashMap();
		if((listNotaFiscalDtoEdis != null) && (listNotaFiscalDtoEdis.size() > 0)) {
			final Map mapDadosComplementos = new HashMap();
			mapDadosComplementos.put("conhecimento", conhecimento);
			final List listDadosComplementos = new ArrayList();

			final List listIdNotaFiscalEdi = new ArrayList();
			for (final ValidarEdiDTO dto : listNotaFiscalDtoEdis) {
				listIdNotaFiscalEdi.add(dto.getNotaFiscalEdi().getIdNotaFiscalEdi());
			}

			//Quest 31584 - Busca pelo DPE da natura. Esta informacao eh informada nos complementos das notas EDI
			InformacaoDoctoCliente infoDPENatura = informacaoDoctoClienteService.findInformacaoDoctoCliente((Long)parameters.get("idClienteRemetente"), "DPE Natura");

			//LMSA-5973
			InformacaoDoctoCliente infoDPEFedex = null;
			if(parameters.get("idClienteTomador") != null){
				infoDPEFedex = informacaoDoctoClienteService.findInformacaoDoctoCliente((Long)parameters.get("idClienteTomador"), "DPEFDX");
			}List<Map> listNotaFiscalEdiComplemento = new ArrayList<Map>();
			while(listIdNotaFiscalEdi.size() > 999){
				List<String> sublist = new ArrayList<String>(listIdNotaFiscalEdi.subList(0, 999));
				listNotaFiscalEdiComplemento.addAll(notaFiscalEDIComplementoService.findDadosComplementos(sublist));
				listIdNotaFiscalEdi.removeAll(sublist);
			} 	listNotaFiscalEdiComplemento.addAll(notaFiscalEDIComplementoService.findDadosComplementos(listIdNotaFiscalEdi));


			int nrNotas = 0;
			Long idNotaFiscalEdiOld = null;
			for (final Map map : listNotaFiscalEdiComplemento) {
				final String tpCampo = map.get("tpCampo") != null ? ((DomainValue) map.get("tpCampo")).getValue() : null;

				// se foi encontrada a data de previsão de entrega da natura, utiliza esta como dpe do conhecimento.
				if (infoDPEFedex==null && infoDPENatura != null && infoDPENatura.getIdInformacaoDoctoCliente().equals(map.get("idInformacaoDoctoCliente"))){
					YearMonthDay dpeNatura = JTDateTimeUtils.convertDataStringToYearMonthDay((String)map.get("valorComplemento"));
					//antes de atribuir a data, verifica se eh igual ou posterior a data atual.
					if (dpeNatura.isAfter(JTDateTimeUtils.getDataAtual()) || dpeNatura.isEqual(JTDateTimeUtils.getDataAtual())){
						conhecimento.setDtPrevEntrega(dpeNatura);
						parameters.put("dpe",dpeNatura);
					}
				}else if (infoDPEFedex != null && infoDPEFedex.getIdInformacaoDoctoCliente().equals(map.get("idInformacaoDoctoCliente"))){
					YearMonthDay dpeFedex = JTDateTimeUtils.convertDataStringToYearMonthDay((String)map.get("valorComplemento"));
					if (dpeFedex.isAfter(JTDateTimeUtils.getDataAtual()) || dpeFedex.isEqual(JTDateTimeUtils.getDataAtual())){
						conhecimento.setDtPrevEntrega(dpeFedex);
						parameters.put("dpe",dpeFedex);
					}
				}else{
					if(Boolean.TRUE.equals(map.get("blIndicadorNotaFiscal")) && (map.get("idNotaFiscalEdi") != null)) {
						// deve adicionar este campo adicional nrNotas vezes com o
						// nr da nota
						if((idNotaFiscalEdiOld == null) || !idNotaFiscalEdiOld.equals(map.get("idNotaFiscalEdi"))) {
							nrNotas++;
						}
						final String dsCampoNf = map.get("nrNotaFiscal") + "- Nota Fiscal: " + nrNotas + " - " + map.get("dsCampo");
						criaRegistroDadosComplementos(listDadosComplementos, (Long) map.get("idInformacaoDoctoCliente"),
								(String) map.get("valorComplemento"), (String) map.get("dsFormatacao"), tpCampo, dsCampoNf);
					} else {
						//senao, registra o campo como complemento normalmente.
						criaRegistroDadosComplementos(listDadosComplementos, (Long) map.get("idInformacaoDoctoCliente"),
								(String) map.get("valorComplemento"), (String) map.get("dsFormatacao"), tpCampo, (String) map.get("dsCampo"));
					}
					idNotaFiscalEdiOld = map.get("idNotaFiscalEdi") != null ? (Long) map.get("idNotaFiscalEdi") : null;
				}
			}

			buildDadosComplementosSubContratacao(listDadosComplementos,
					(String)parameters.get("nrConhecimentoSubcontratante"), conhecimento);

			buildDadosComplementosRedespacho(listDadosComplementos, parameters);

			final List list = this.ajustarComplementoVolTotalL(new ArrayList(new HashSet(listDadosComplementos)));

			mapDadosComplementos.put("campoAdicionalConhecimento", list);
			mapReturn.put("mapDadosComplementos", mapDadosComplementos);
		}
		return mapReturn;
	}

	private List ajustarComplementoVolTotalL(List<Map> complementos) {
		List retorno =  new ArrayList<Map>();

		Integer qtdeTotal = 0;
		Map complementoVolTol = null;
		for (Map complemento : complementos) {
			if(complemento.get("dsCampo") != null && VOLTOTALIZ.equalsIgnoreCase(complemento.get("dsCampo").toString())) {
				qtdeTotal = qtdeTotal + Integer.valueOf(String.valueOf(complemento.get("dsValorCampo")));
				if(complementoVolTol == null) {
					complementoVolTol = complemento;
				}
			}else {
				retorno.add(complemento);
			}
		}
		if(qtdeTotal > 0) {
			complementoVolTol.put("dsValorCampo", qtdeTotal);
			retorno.add(complementoVolTol);
		}
		return retorno;
	}

	@SuppressWarnings("rawtypes")
	private void buildDadosComplementosRedespacho(final List listDadosComplementos, Map parameters) {
		if(StringUtils.isNotBlank((String)parameters.get("ieRedesp"))){
			InformacaoDocServico informacaoDocServico = informacaoDocServicoService.findByDsCampo("IE_REDESPACHO");
			criaRegistroDadosComplementosSubContratacao(listDadosComplementos,
					corrigeIE((String)parameters.get("ieRedesp")),
					informacaoDocServico.getIdInformacaoDocServico(), informacaoDocServico.getDsCampo());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void criaRegistroDadosComplementos(final List listDadosComplementos, final Long idInformacaoDoctoCliente, final String valorComplemento,
											   final String dsFormatacao, final String tpCampo, final String dsCampo) {
		final Map mapCampoAdicionalConhecimento = new HashMap();

		mapCampoAdicionalConhecimento.put("dsValorCampo", formatValorComplemento(dsFormatacao, tpCampo, valorComplemento));
		mapCampoAdicionalConhecimento.put("dsCampo", dsCampo);

		mapCampoAdicionalConhecimento.put("idCampoAdicionalConhecimento", null);
		mapCampoAdicionalConhecimento.put("id", idInformacaoDoctoCliente);
		listDadosComplementos.add(mapCampoAdicionalConhecimento);
	}

	@SuppressWarnings("rawtypes")
	private void buildDadosComplementosSubContratacao(final List listDadosComplementos, final String nrConhecimentoSubcontratante, Conhecimento conhecimento) {
		if (isValidChaveCte(nrConhecimentoSubcontratante)) {
			InformacaoDocServico informacaoDocServico = informacaoDocServicoService
					.findByDsCampo("NR_SERIE_ANTERIOR");
			criaRegistroDadosComplementosSubContratacao(listDadosComplementos,
					nrConhecimentoSubcontratante.substring(22, 25),
					informacaoDocServico.getIdInformacaoDocServico(), informacaoDocServico.getDsCampo());

			informacaoDocServico = informacaoDocServicoService
					.findByDsCampo("NR_DOCUMENTO_ANTERIOR");
			criaRegistroDadosComplementosSubContratacao(listDadosComplementos,
					nrConhecimentoSubcontratante.substring(25, 34),
					informacaoDocServico.getIdInformacaoDocServico(), informacaoDocServico.getDsCampo());

			informacaoDocServico = informacaoDocServicoService
					.findByDsCampo("TP_DOCUMENTO_ANTERIOR");
			criaRegistroDadosComplementosSubContratacao(listDadosComplementos,
					ConstantesExpedicao.TIPO_DOCUMENTO_ANTERIOR,
					informacaoDocServico.getIdInformacaoDocServico(), informacaoDocServico.getDsCampo());

			informacaoDocServico = informacaoDocServicoService
					.findByDsCampo("NR_CHAVE_DOCUMENTO_ANTERIOR");
			criaRegistroDadosComplementosSubContratacao(listDadosComplementos,
					nrConhecimentoSubcontratante,
					informacaoDocServico.getIdInformacaoDocServico(), informacaoDocServico.getDsCampo());

			if(conhecimento.getClienteByIdClienteRedespacho() == null){
				conhecimento.setNrCtrcSubcontratante(nrConhecimentoSubcontratante.substring(25, 34));
			}
		}
	}

	private boolean isValidChaveCte(String chaveCte) {
		return chaveCte != null && ValidateUtils.validateChaveNfe(chaveCte);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void criaRegistroDadosComplementosSubContratacao(final List listDadosComplementos, final String dsValorCampo, final Long idInformacaoDocServico, final String dsCampo) {
		final Map mapCampoAdicionalConhecimento = new HashMap();
		mapCampoAdicionalConhecimento.put("dsValorCampo", dsValorCampo);
		mapCampoAdicionalConhecimento.put("idInformacaoDocServico", idInformacaoDocServico);
		mapCampoAdicionalConhecimento.put("dsCampo", dsCampo);
		mapCampoAdicionalConhecimento.put("idCampoAdicionalConhecimento", null);
		mapCampoAdicionalConhecimento.put("id", null);
		listDadosComplementos.add(mapCampoAdicionalConhecimento);
	}

	@SuppressWarnings("deprecation")
	private Object formatValorComplemento(final String dsFormatacao, final String tpCampo, final String valorComplemento) {
		DateTimeFormatter jodaParser = null;
		boolean decimal = false;

		final String cdDataType = tpCampo;
		if(StringUtils.isNotBlank(dsFormatacao)) {
			if((dsFormatacao.indexOf('.') > -1) || (dsFormatacao.indexOf(',') > -1)) {
				decimal = true;
			}
		}

		if("A".equals(cdDataType)) {
			return valorComplemento;
		} else if("N".equals(cdDataType)) {
			if(decimal) {
				try {
					return Double.parseDouble(NumberFormat.getNumberInstance(SessionUtils.getUsuarioLogado().getLocale()).parse(valorComplemento).toString());
				} catch (final NumberFormatException e) {
					log.error(e);
				} catch (final ParseException e) {
					log.error(e);
				}
			} else {
				return Integer.parseInt(valorComplemento);
			}
		} else if("D".equals(cdDataType)) {
			// FIXME engessado ddMMyyyy devido edi da GM vir os dados nesse
			// layout, Eri e Elisete irão estudar uma maneira de deixar um
			// layout padrão para todos os cliente no que diz respeito a data
			jodaParser = DateTimeFormat.forPattern("ddMMyyyy").withZone(DateTimeZone.forID(SessionUtils.getFilialSessao().getDsTimezone()));
			return jodaParser.withZone(DateTimeZone.UTC).parseDateTime(valorComplemento).toYearMonthDay();
		} else if("H".equals(cdDataType)) {
			// TODO substituir dsFormatacao para o tipo que esta vindo
			jodaParser = DateTimeFormat.forPattern(dsFormatacao).withZone(DateTimeZone.forID(SessionUtils.getFilialSessao().getDsTimezone()));
			return jodaParser.parseDateTime(valorComplemento).toTimeOfDay();
		} else if("Z".equals(cdDataType)) {
			// FIXME engessado ddMMyyyyHHmm devido edi da GM vir os dados nesse
			// layout, Eri e Elisete irão estudar uma maneira de deixar um
			// layout padrão para todos os cliente no que diz respeito a data
			jodaParser = DateTimeFormat.forPattern("ddMMyyyyHHmm").withZone(DateTimeZone.forID(SessionUtils.getFilialSessao().getDsTimezone()));
			return jodaParser.parseDateTime(valorComplemento);
		}
		return valorComplemento;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private List buildMapNotaFiscalConhecimento(final Map parameters,
												final String opcaoProcessamento,
												final List<Map> listNumeroEtiquetasEdiInformada,
												final List<ValidarEdiDTO> listNotasDtoASeremAgrupadas,
												final String tpProcessamento) {

		final List listNotaFiscalConhecimento = new ArrayList();
		final StringBuilder strLog = new StringBuilder("[EDI] NFs a serem processadas: ");

		for (final ValidarEdiDTO dto : listNotasDtoASeremAgrupadas) {
			final Map mapNotaFiscalConhecimento = new HashMap();
			mapNotaFiscalConhecimento.put("idNotaFiscalEdi", dto.getNotaFiscalEdi().getIdNotaFiscalEdi());
			mapNotaFiscalConhecimento.put("nrVolumeEtiquetaInicial", null);
			mapNotaFiscalConhecimento.put("dtEmissao", new YearMonthDay(dto.getNotaFiscalEdi().getDataEmissaoNf()));
			mapNotaFiscalConhecimento.put("vlIcms", dto.getNotaFiscalEdi().getVlrIcms() == null ? BigDecimal.ZERO : dto.getNotaFiscalEdi().getVlrIcms());
			mapNotaFiscalConhecimento.put("qtVolumes", Short.valueOf(dto.getNotaFiscalEdi().getQtdeVolumes() != null ? dto.getNotaFiscalEdi().getQtdeVolumes().setScale(0).toString() : "0"));
			mapNotaFiscalConhecimento.put("vlIcmsSt", dto.getNotaFiscalEdi().getVlrIcmsStNf() == null ? BigDecimal.ZERO : dto.getNotaFiscalEdi().getVlrIcmsStNf());
			mapNotaFiscalConhecimento.put("vlTotal", dto.getNotaFiscalEdi().getVlrTotalMerc() == null ? BigDecimal.ZERO : dto.getNotaFiscalEdi().getVlrTotalMerc());
			mapNotaFiscalConhecimento.put("vlTotalProdutos", dto.getNotaFiscalEdi().getVlrTotProdutosNf() == null ? BigDecimal.ZERO
					: dto.getNotaFiscalEdi().getVlrTotProdutosNf());
			mapNotaFiscalConhecimento.put("nrPinSuframa", dto.getNotaFiscalEdi().getPinSuframa());
			mapNotaFiscalConhecimento.put("nrNotaFiscal", dto.getNotaFiscalEdi().getNrNotaFiscal());
			mapNotaFiscalConhecimento.put("vlBaseCalculoSt", dto.getNotaFiscalEdi().getVlrBaseCalcStNf() == null ? BigDecimal.ZERO
					: dto.getNotaFiscalEdi().getVlrBaseCalcStNf());
			mapNotaFiscalConhecimento.put("psMercadoria", dto.getNotaFiscalEdi().getPesoReal() == null ? BigDecimal.ZERO : dto.getNotaFiscalEdi().getPesoReal());
			mapNotaFiscalConhecimento.put("nrVolumeEtiquetaFinal", null);
			mapNotaFiscalConhecimento.put("vlBaseCalculo", dto.getNotaFiscalEdi().getVlrBaseCalcNf() == null ? BigDecimal.ZERO
					: dto.getNotaFiscalEdi().getVlrBaseCalcNf());
			mapNotaFiscalConhecimento.put("dsSerie", dto.getNotaFiscalEdi().getSerieNf());

			mapNotaFiscalConhecimento.put("blProdutoPerigoso", dto.getIsProdutoPerigoso());
			mapNotaFiscalConhecimento.put("blControladoPoliciaCivil", dto.getIsControladoPoliciaCivil());
			mapNotaFiscalConhecimento.put("blControladoPoliciaFederal", dto.getIsControladoPoliciaFederal());
			mapNotaFiscalConhecimento.put("blControladoExercito", dto.getIsControladoExercito());

			//LMS-4940
			mapNotaFiscalConhecimento.put("psCubadoNotfis", dto.getNotaFiscalEdi().getPesoCubado());

			// setar nos parametros campos a serem utilizados em caso de calculo
			// manual
			mapNotaFiscalConhecimento.put("vlPedagio", dto.getNotaFiscalEdi().getVlrPedagio());
			mapNotaFiscalConhecimento.put("vlAdeme", dto.getNotaFiscalEdi().getVlrAdeme());
			mapNotaFiscalConhecimento.put("vlFreteValor", dto.getNotaFiscalEdi().getVlrFreteValor());
			mapNotaFiscalConhecimento.put("vlItr", dto.getNotaFiscalEdi().getVlrItr());
			mapNotaFiscalConhecimento.put("vlDespacho", dto.getNotaFiscalEdi().getVlrDespacho());
			mapNotaFiscalConhecimento.put("vlFretePeso", dto.getNotaFiscalEdi().getVlrFretePeso());
			mapNotaFiscalConhecimento.put("vlCat", dto.getNotaFiscalEdi().getVlrCat());

			//correção   LMS-493 O peso cubado mesmo presente do EDI está sendo gravado como zero no conhecimento e não levado em consideração no cálculo do frete
			mapNotaFiscalConhecimento.put("psCubado", dto.getNotaFiscalEdi().getPesoCubado());

			BigDecimal vlTaxasEOutrosValores;
			vlTaxasEOutrosValores = dto.getNotaFiscalEdi().getOutrosValores() != null ? dto.getNotaFiscalEdi().getOutrosValores() : BigDecimal.ZERO;
			vlTaxasEOutrosValores = vlTaxasEOutrosValores.add(dto.getNotaFiscalEdi().getVlrTaxas() != null ? dto.getNotaFiscalEdi().getVlrTaxas() : BigDecimal.ZERO);
			mapNotaFiscalConhecimento.put("vlTaxasEOutrosValores", vlTaxasEOutrosValores);

			if (dto.getNotaFiscalEdi().getChaveNfe() != null && !dto.getNotaFiscalEdi().getChaveNfe().isEmpty()){
				mapNotaFiscalConhecimento.put("nrChave", dto.getNotaFiscalEdi().getChaveNfe());
			}
			else{
				Object indicadorCTE = conteudoParametroFilialService.findConteudoByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "INDICADOR CTE", false);
				if (indicadorCTE != null && ("1".equals(indicadorCTE) || "2".equals(indicadorCTE))){
					Integer nr = dto.getNotaFiscalEdi().getCfopNf() == null || isTypeDocument(dto.getNotaFiscalEdi().getCfopNf()) ?  getCfopNotaEdi() : Integer.valueOf(dto.getNotaFiscalEdi().getCfopNf());
					mapNotaFiscalConhecimento.put("nrCfop", nr);
				}
			}

			mapNotaFiscalConhecimento.put("tpDocumento", (isTypeDocument(dto.getNotaFiscalEdi().getCfopNf()) || isTpProcessamentoPorManifestoConsol(tpProcessamento)) ? "99" : "01");

			final List listVolumeNotaFiscal = new ArrayList();

			if((listNumeroEtiquetasEdiInformada != null) && (listNumeroEtiquetasEdiInformada.size() > 0)) {
				for (final Map mapNumeroEtiqueta : listNumeroEtiquetasEdiInformada) {
					if(mapNumeroEtiqueta.containsKey(dto.getNotaFiscalEdi().getNrNotaFiscal())) {
						final Map mapVolumeNotaFiscal = new HashMap();
						final Map mapNrVolumeColeta = (HashMap) mapNumeroEtiqueta.get(dto.getNotaFiscalEdi().getNrNotaFiscal());
						mapVolumeNotaFiscal.put("nrVolumeColeta", fillNumberWithZero((String) mapNrVolumeColeta.get("numeroEtiqueta"), 12));
						listVolumeNotaFiscal.add(mapVolumeNotaFiscal);
					}
				}
			} else {

				Cliente clienteRemetente = (Cliente)parameters.get("clienteRemetente");
				final List<NotaFiscalEdiVolume> list = notaFiscalExpedicaoEDIVolumeService.findByIdNotaFiscalEdi(dto.getNotaFiscalEdi().getIdNotaFiscalEdi());

				for (final NotaFiscalEdiVolume notaFiscalEdiVolume : list) {
					final Map mapVolumeNotaFiscal = new HashMap();
					mapVolumeNotaFiscal.put("nrVolumeColeta", notaFiscalEdiVolume.getCodigoVolume());
					mapVolumeNotaFiscal.put("cdBarraPostoAvancado", notaFiscalEdiVolume.getCdBarraPostoAvancado());
					listVolumeNotaFiscal.add(mapVolumeNotaFiscal);
				}
			}
			mapNotaFiscalConhecimento.put("volumeNotaFiscal", listVolumeNotaFiscal);
			listNotaFiscalConhecimento.add(mapNotaFiscalConhecimento);

			strLog.append(dto.getNotaFiscalEdi().getNrNotaFiscal() + " (qtdVolumes=" + dto.getNotaFiscalEdi().getQtdeVolumes() + ") [");
			for (int i = 0; i < listVolumeNotaFiscal.size(); i++) {
				final Map mapVnf = (Map) listVolumeNotaFiscal.get(i);
				strLog.append((mapVnf.get("nrVolumeColeta")).toString() + ",");
			}
			strLog.append("], ");
		}
		log.warn(strLog.toString());
		return listNotaFiscalConhecimento;
	}

	private boolean isTypeDocument(Short cfopNF){
		return cfopNF != null && "9999".equals(cfopNF.toString()) ? true : false;
	}

	private boolean isTpProcessamentoPorManifestoConsol(String tpProcessamento){
		return tpProcessamento.equals(ConstantesExpedicao.TP_PROCESSAMENTO_POR_MANIFESTO_CONSOLIDADO);
	}

	private Integer getCfopNotaEdi(){
		Object cfopNotaEdi = parametroGeralService.findConteudoByNomeParametro("CFOP_NOTA_EDI", false);
		return cfopNotaEdi != null ?  Integer.valueOf(cfopNotaEdi.toString()) : null;
	}

	public Municipio findMunicipioByNrIdentificacao(String nrIdentificacao) {
		try {
			nrIdentificacao = nrIdentificacao.replaceAll("[^\\p{Digit}]", "");

			String nrIdentificacaoPessoa = null;
			if(nrIdentificacao.trim().length() <= 11) {
				nrIdentificacao = FormatUtils.fillNumberWithZero(nrIdentificacao, 11);
				if(ValidateUtils.validateCpfOrCnpj(nrIdentificacao)) {
					nrIdentificacaoPessoa = nrIdentificacao;
				}
			}

			if(nrIdentificacaoPessoa == null) {
				nrIdentificacaoPessoa = FormatUtils.fillNumberWithZero(nrIdentificacao, 14);
			}
			final Cliente clienteConsig = clienteService.findByNrIdentificacao(nrIdentificacaoPessoa);
			final EnderecoPessoa enderecoPessoa = enderecoPessoaService.findByIdPessoa(clienteConsig.getPessoa().getIdPessoa());
			return enderecoPessoa.getMunicipio();
		} catch (final NullPointerException npe) {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public Map findAeroportoOrigemByFilial(Long idFilial) {
		if (SessionUtils.getFilialSessao() != null) {
			idFilial = SessionUtils.getFilialSessao().getIdFilial();
		}
		return findAeroportoByFilial(idFilial);
	}

	// TODO: 20/01/2023 rever regar e objeto de filtro
	private Long findFilialAtendimento(final Map<String, Object> criteria) {
		return ppeService.findFilialAtendimentoMunicipio(
				(Long) criteria.get("idMunicipioDestinatario"),
				(Long) criteria.get("idServico"),
				Boolean.FALSE,
				JTDateTimeUtils.getDataAtual(),
				(String) criteria.get("nrCepDestinatario"),
				(Long) criteria.get("idClienteDestinatario"),
				null, null, null, null,null);
	}

	@SuppressWarnings("rawtypes")
	private Map findAeroportoDestinoByFilial(final Long idFilial) {
		return findAeroportoByFilial(idFilial);
	}

	@SuppressWarnings("rawtypes")
	public Map findAeroportoByFilial(final Long idFilial) {
		if(idFilial == null) {
			return null;
		}
		return filialService.findAeroportoFilial(idFilial);
	}

	public String getNrEndereco(final StringBuilder dsEndereco) {
		return EnderecoPessoaUtils.getNumeroEndereco(dsEndereco);
	}

	@SuppressWarnings("rawtypes")
	public Municipio findMunicipioBRAByCep(final Integer nrCep) {
		if(nrCep == null) {
			return null;
		}
		final List list = intervaloCepService.findByCep(fillNumberWithZero(nrCep.toString(), 8), ConstantesAmbiente.SG_PAIS_BRASIL);
		if((list != null) && !list.isEmpty()) {
			final IntervaloCep intervaloCep = (IntervaloCep) list.get(0);
			return intervaloCep.getMunicipio();
		}
		return null;
	}

	public Long findIdMunicipioBRAByCep(final Integer nrCep) {
		Municipio m = findMunicipioBRAByCep(nrCep);
		return m != null ? m.getIdMunicipio() : null;
	}

	@SuppressWarnings("rawtypes")
	public Long findIdUnidadeFederativaBySgUf(final String sgUf, Long idPais) {
		if(StringUtils.isBlank(sgUf)) {
			return null;
		}
		final List listUfs = unidadeFederativaService.findUfBySgAndPais(sgUf, idPais);
		if((listUfs != null) && !listUfs.isEmpty()) {
			final Map map = (Map) listUfs.get(0);
			return (Long) map.get("idUnidadeFederativa");
		}
		return null;
	}

	public Long findIdServicoBySigla(final String sigla) {
		final Servico servico = servicoService.findServicoBySigla(sigla);
		if(servico != null) {
			return servico.getIdServico();
		}
		return null;
	}

	public String getValidatedNrIdentificacao(String nrIdentificacao) {
		String retorno = null;
		String cpf = null;
		if (nrIdentificacao.trim().length() <= 11) {
			cpf = FormatUtils.fillNumberWithZero(nrIdentificacao, 11);
		}
		String cnpj = FormatUtils.fillNumberWithZero(nrIdentificacao, 14);

		Boolean isCpfValido = ValidateUtils.validateCpfOrCnpj(cpf);
		Boolean isCnpjValido = ValidateUtils.validateCpfOrCnpj(cnpj);

		if (isCpfValido && isCnpjValido) {

			Boolean isCnpjCadastrado = pessoaService.validatePessoaByNrIdentificacao(cnpj);
			if (isCnpjCadastrado) {
				retorno = cnpj;
			} else {
				retorno = cpf;
			}
		} else if (isCpfValido) {
			retorno = cpf;
		} else {
			retorno = cnpj;
		}

		return retorno;
	}

	@SuppressWarnings("rawtypes")
	private void setIdNaturezaProduto(final Map parameters) {
		final Long idDivisaoCliente = (Long) parameters.get("idDivisaoCliente");
		if(idDivisaoCliente != null) {
			final String dsNatureza = (String) parameters.get("dsNatureza");
			final Long idNaturezaProduto = findIdNaturezaProdutoByIdDivisaoClienteByNmNatureza(idDivisaoCliente, dsNatureza);

			if(idNaturezaProduto != null) {
				final Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
				final NaturezaProduto naturezaProduto = new NaturezaProduto();
				naturezaProduto.setIdNaturezaProduto(idNaturezaProduto);
				conhecimento.setNaturezaProduto(naturezaProduto);
			}
		}else{
			/*caso não tenha sido informada a divisão ainda, busca a natureza padrão ("Diversos"),
				caso não encontre pega a primeira ativa, pois a natureza do produto não deve ser nula.
			*/
			List list = naturezaProdutoService.findByDsNaturezaProduto("Diversos");
			Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
			if((list != null) && (list.size() > 0)) {
				conhecimento.setNaturezaProduto((NaturezaProduto) list.get(0));
			} else {
				list = naturezaProdutoService.findAllAtivo();
				NaturezaProduto naturezaProduto = new NaturezaProduto();
				naturezaProduto.setIdNaturezaProduto((Long) ((Map) list.get(0)).get("idNaturezaProduto"));
				conhecimento.setNaturezaProduto(naturezaProduto);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Long findIdNaturezaProdutoByIdDivisaoClienteByNmNatureza(final Long idDivisaoCliente, final String dsNatureza) {
		// Após definir a divisão do cliente a ser utilizado no cálculo do
		// frete, verificar se o
		// conteúdo do campo NOTA_FISCAL_EDI.NATUREZA está cadastrado na tabela
		// NAT_PROD_CLIENTE_DIV pelo campo DS_NAT_PROD_CLIENTE_DIV para a
		// divisão definida, se existir o registro, utiliza a natureza de
		// produto associada para
		// preencher o campo a tabela DOCTO_SERVICO.ID_NATUREZA_PRODUTO. Se o
		// registro não existir, verificar se o conteúdo do campo
		// NOTA_FISCAL_EDI.NATUREZA está cadastrado na tabela NATUREZA_PRODUTO,
		// se não estiver cadastrado, utilizar o registro com
		// DS_NATUREZA_PRODUTO =
		// “Diversos”.

		final Map criteria = new HashMap();
		criteria.put("divisaoCliente.idDivisaoCliente", idDivisaoCliente);
		criteria.put("dsNaturezaProdutoCliente", dsNatureza);
		final List listDivisaoClienteNaturezaProduto = divisaoClienteNaturezaProdutoService.find(criteria);
		if((listDivisaoClienteNaturezaProduto != null) && (listDivisaoClienteNaturezaProduto.size() > 0)) {
			final DivisaoClienteNaturezaProduto divisaoClienteNaturezaProduto = (DivisaoClienteNaturezaProduto) listDivisaoClienteNaturezaProduto.get(0);
			return divisaoClienteNaturezaProduto.getNaturezaProduto().getIdNaturezaProduto();
		}

		List list = naturezaProdutoService.findByDsNaturezaProduto(dsNatureza);
		if((list != null) && (list.size() > 0)) {
			final NaturezaProduto naturezaProduto = (NaturezaProduto) list.get(0);
			return naturezaProduto.getIdNaturezaProduto();
		} else {
			// Conforme regra da ET - CJS 04/12/2009
			// Se o conteúdo do campo NOTA_FISCAL_EDI.NATUREZA não estiver
			// cadastrado natabela NATUREZA_PRODUTO
			// utilizar o registro com DS_NATUREZA_PRODUTO = “Diversos”.
			list = naturezaProdutoService.findByDsNaturezaProduto("Diversos");
			if((list != null) && (list.size() > 0)) {
				final NaturezaProduto naturezaProduto = (NaturezaProduto) list.get(0);
				return naturezaProduto.getIdNaturezaProduto();
			} else {
				list = naturezaProdutoService.findAllAtivo();
				return (Long) ((Map) list.get(0)).get("idNaturezaProduto");
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map prepararDivisaoCliente(final Map parameters) {
		final List<Map> listDivisao = (List) parameters.get("listDivisao");
		if((listDivisao != null) && (listDivisao.size() == 1)) {
			final HashMap map = (HashMap) listDivisao.get(0);
			parameters.putAll(map);
			return parameters;
		} else if((listDivisao != null) && (listDivisao.size() > 1)) {

			if(parameters.get("idDivisaoCliente") != null) {
				for (final Map map : listDivisao) {
					if(map.get("idDivisaoCliente").equals(parameters.get("idDivisaoCliente"))) {
						parameters.putAll(map);
						return parameters;
					}
				}
			}

			parameters.putAll(listDivisao.get(0));
			return parameters;

		} else if(listDivisao == null) {
			return parameters;
		}
		return parameters;
	}

	/**
	 * No campo DOCTO_SERVICO.DH_INCLUSAO deverá ser gravado a menor data/hora
	 * de digitação da nota nas abas de digitação de notas para o grupo de notas
	 * do Pré-Conhecimento, ou a data/hora da digitação do intervalo de notas
	 * quando for esta a opção de digitação. Se o processamento for automático
	 * gravar data/hora atual.
	 *
	 * @param conhecimento
	 * @param opcaoProcessamento
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setDhInclusaoConhecimento(final Conhecimento conhecimento, final Map<Object, Object> mapDataHoraNotaFiscal, final String opcaoProcessamento) {
		if(opcaoProcessamento.equals(ConstantesExpedicao.EDI_PROCESSAR_NOTAS_FISCAIS) ||
				opcaoProcessamento.equals(ConstantesExpedicao.EDI_PROCESSAR_NOTAS_FISCAIS_ETIQUETAS)) {

			final Set<Integer> listKeySet = mapDataHoraNotaFiscal.entrySet().stream().map(entry -> {
				Integer value = null;
				if (entry.getKey() instanceof String) {
					value = Integer.valueOf((String) entry.getKey());
				}else{
					value = (Integer)entry.getKey();
				}
				return value;
			}).collect(Collectors.toSet());

			final Collection<NotaFiscalConhecimento> listFiltrado = CollectionUtils.select(conhecimento.getNotaFiscalConhecimentos(),
					new Predicate() {
						public boolean evaluate(final Object object) {
							final NotaFiscalConhecimento notaFiscalConhecimento = (NotaFiscalConhecimento) object;
							for (final Integer nrNotaFiscal : listKeySet) {

								if(notaFiscalConhecimento.getNrNotaFiscal().equals(nrNotaFiscal)) {
									return true;
								}
							}
							return false;
						}
					});

			DateTime dateTimeInclusao = JTDateTimeUtils.getDataHoraAtual(); // deve
			// ser
			// setado
			// a
			// data
			// mais
			// antiga...
			DateTime dateTime = null;
			for (final NotaFiscalConhecimento notaFiscalConhecimento : listFiltrado) {
				dateTime = (DateTime) mapDataHoraNotaFiscal.get(notaFiscalConhecimento.getNrNotaFiscal());
				if(dateTimeInclusao == null) {
					dateTimeInclusao = dateTime;
				} else if(dateTimeInclusao.isAfter(dateTime)) {
					dateTimeInclusao = dateTime;
				}
			}
			conhecimento.setDhInclusao(dateTimeInclusao);
		} else {
			conhecimento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map calculaPesoCubado(final Map parameters) {
		final List<Map> listNotaFiscalConhecimento = (List) parameters.get("notaFiscalConhecimento");
		for (final Map mapNotaFiscalConhecimento : listNotaFiscalConhecimento) {

			BigDecimal psCubadoEDI = (BigDecimal) mapNotaFiscalConhecimento.remove("psCubado");


			BigDecimal pesoCubado = null;
			final TabelaDivisaoCliente tabelaDivisaoCliente = tabelaDivisaoClienteService.findTabelaDivisaoCliente(
					(Long) parameters.get("idDivisaoCliente"), (Long) parameters.get("idServico"));
			if(BigDecimalUtils.hasValue(tabelaDivisaoCliente != null ? tabelaDivisaoCliente.getNrFatorCubagem() : null)) {
				pesoCubado = new BigDecimal((psCubadoEDI == null ? BigDecimal.ZERO : psCubadoEDI).doubleValue()
						* tabelaDivisaoCliente.getNrFatorCubagem().doubleValue());
			} else {
				final BigDecimal pesoMetragemCubicaRodoviario = (BigDecimal) parametroGeralService.findConteudoByNomeParametro(
						"PESO_METRAGEM_CUBICA_RODOVIARIO", false);
				pesoCubado = new BigDecimal(
						((psCubadoEDI == null ? BigDecimal.ZERO : psCubadoEDI).doubleValue() / pesoMetragemCubicaRodoviario.doubleValue()) * 1000000);
			}

			mapNotaFiscalConhecimento.put("psCubado", pesoCubado.setScale(3, BigDecimal.ROUND_HALF_UP));

			final Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
			for (final NotaFiscalConhecimento nfc : conhecimento.getNotaFiscalConhecimentos()) {
				Integer nrNotaFiscal = (Integer)mapNotaFiscalConhecimento.get("nrNotaFiscal");
				if (nfc.getNrNotaFiscal().equals(nrNotaFiscal)){
					nfc.setPsCubado(pesoCubado.setScale(3, BigDecimal.ROUND_HALF_UP));
				}
			}
		}

		return parameters;
	}

	private void calculaPesoCubado
			(List<NotaFiscalConhecimento> notaFiscalConhecimentos,
			 List<NotaFiscalConhecimentoDto> listNotaFiscalConhecimento,
			 Long idDivisaoCliente, Long idServico) {

		for (final NotaFiscalConhecimentoDto notaFiscalConhecimentoDto : listNotaFiscalConhecimento) {

			BigDecimal psCubadoEDI = notaFiscalConhecimentoDto.getPsCubado();


			BigDecimal pesoCubado = null;
			final TabelaDivisaoCliente tabelaDivisaoCliente = tabelaDivisaoClienteService
					.findTabelaDivisaoCliente(idDivisaoCliente, idServico);
			if(BigDecimalUtils.hasValue(tabelaDivisaoCliente != null ? tabelaDivisaoCliente.getNrFatorCubagem() : null)) {
				pesoCubado = new BigDecimal((psCubadoEDI == null ? BigDecimal.ZERO : psCubadoEDI).doubleValue()
						* tabelaDivisaoCliente.getNrFatorCubagem().doubleValue());
			} else {
				final BigDecimal pesoMetragemCubicaRodoviario = (BigDecimal) parametroGeralService.findConteudoByNomeParametro(
						"PESO_METRAGEM_CUBICA_RODOVIARIO", false);
				pesoCubado = new BigDecimal(
						((psCubadoEDI == null ? BigDecimal.ZERO : psCubadoEDI).doubleValue() / pesoMetragemCubicaRodoviario.doubleValue()) * 1000000);
			}

			notaFiscalConhecimentoDto.setPsCubado(pesoCubado.setScale(3, BigDecimal.ROUND_HALF_UP));

			//final Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");

			for (final NotaFiscalConhecimento nfc : notaFiscalConhecimentos) {
				if (nfc.getNrNotaFiscal().equals(notaFiscalConhecimentoDto.getNrNotaFiscal())){
					nfc.setPsCubado(pesoCubado.setScale(3, BigDecimal.ROUND_HALF_UP));
				}
			}
		}

	}

	public void setConhecimentoNormalService(
			final ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}

	public void setDigitarDadosNotaNormalNotasFiscaisService(
			final DigitarDadosNotaNormalNotasFiscaisService digitarDadosNotaNormalNotasFiscaisService) {
		this.digitarDadosNotaNormalNotasFiscaisService = digitarDadosNotaNormalNotasFiscaisService;
	}

	public void setDigitarDadosNotaNormalCamposAdicionaisService(
			final DigitarDadosNotaNormalCamposAdicionaisService digitarDadosNotaNormalCamposAdicionaisService) {
		this.digitarDadosNotaNormalCamposAdicionaisService = digitarDadosNotaNormalCamposAdicionaisService;
	}

	public void setDigitarDadosNotaNormalService(
			final DigitarDadosNotaNormalService digitarDadosNotaNormalService) {
		this.digitarDadosNotaNormalService = digitarDadosNotaNormalService;
	}

	public DigitarDadosNotaNormalCalculoCTRCService getDigitarDadosNotaNormalCalculoCTRCService() {
		return digitarDadosNotaNormalCalculoCTRCService;
	}

	public void setDigitarDadosNotaNormalCalculoCTRCService(
			final DigitarDadosNotaNormalCalculoCTRCService digitarDadosNotaNormalCalculoCTRCService) {
		this.digitarDadosNotaNormalCalculoCTRCService = digitarDadosNotaNormalCalculoCTRCService;
	}

	public void setTabelaDivisaoClienteService(
			final TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}

	public void setParametroGeralService(final ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setDivisaoClienteNaturezaProdutoService(
			final DivisaoClienteNaturezaProdutoService divisaoClienteNaturezaProdutoService) {
		this.divisaoClienteNaturezaProdutoService = divisaoClienteNaturezaProdutoService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(final EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setServicoService(final ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public void setClienteService(final ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public void setIntervaloCepService(final IntervaloCepService intervaloCepService) {
		this.intervaloCepService = intervaloCepService;
	}

	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}

	public void setUnidadeFederativaService(
			final UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setInscricaoEstadualService(
			final InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public void setPpeService(final PpeService ppeService) {
		this.ppeService = ppeService;
	}

	public DivisaoClienteService getDivisaoClienteService() {
		return divisaoClienteService;
	}

	public void setDivisaoClienteService(final DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public void setFilialService(final FilialService filialService) {
		this.filialService = filialService;
	}

	public void setMunicipioService(final MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setNotaFiscalExpedicaoEDIVolumeService(
			final NotaFiscalExpedicaoEDIVolumeService notaFiscalExpedicaoEDIVolumeService) {
		this.notaFiscalExpedicaoEDIVolumeService = notaFiscalExpedicaoEDIVolumeService;
	}

	public void setNotaFiscalEDIComplementoService(
			final NotaFiscalEDIComplementoService notaFiscalEDIComplementoService) {
		this.notaFiscalEDIComplementoService = notaFiscalEDIComplementoService;
	}

	public void setNaturezaProdutoService(
			final NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}


	/**
	 * Realiza uma pré-validação da nota fiscal EDI informada, e caso existam erros
	 * cria um logErrosEDI, para cada erro, para posterior ajuste manual.
	 *
	 * LMS-3831
	 * @param criteria
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> preValidarNotaFiscalEDI(Map<String, Object> criteria){
		Map<String, Object> retorno = new HashMap<>();

		ValidarEdiDTO validarEdiDTO = (ValidarEdiDTO) criteria.get("validarEdiDTO");
		String tpProcessamento = (String) criteria.get("tpProcessamento");

		retorno.put("validarEdiDTO", this.preValidarNotaFiscalEDI(validarEdiDTO, tpProcessamento));
		return retorno;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> preValidarVolumeNotaFiscalEDI(Map<String, Object> criteria) {
		ValidarEdiDTO validarEdiDTO = (ValidarEdiDTO) criteria.get("validarEdiDTO");
		String tpProcessamento = (String) criteria.get("tpProcessamento");

		Map<String, Object> retorno = new HashMap<>();
		retorno.put("validarEdiDTO", preValidarVolumeNotaFiscalEDI(validarEdiDTO, tpProcessamento));

		return retorno;
	}

	public ValidarEdiDTO preValidarVolumeNotaFiscalEDI(ValidarEdiDTO validarEdiDTO, String tpProcessamento){
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(AJUSTA_VOLUMES_EDI);
		NotaFiscalEdi notaFiscalEdi = findById(validarEdiDTO.getNotaFiscalEdi().getIdNotaFiscalEdi());
		String cnpjRemePart = clienteService.findById(validarEdiDTO.getClienteRemetente().getIdCliente()).getPessoa().getNrIdentificacao().substring(0, 8);

		List<LogErrosEDI> logsErrosEDI = new ArrayList<>();

		if (parametroGeral != null && parametroGeral.getDsConteudo().contains(cnpjRemePart)
				&& !clienteService.validateIsDanfeSimplificada(notaFiscalEdi.getCnpjReme())) {

			BigDecimal qtdVolumesFaltantes = calcularVolumesFaltantesEDI(notaFiscalEdi);

			if(BigDecimalUtils.hasValue(notaFiscalEdi.getQtdeVolumes()) && BigDecimalUtils.gtZero(qtdVolumesFaltantes)) {
				logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.ETIQUETAS, qtdVolumesFaltantes.toString(), null, null));
			}
		}

		if(!logsErrosEDI.isEmpty()){
			logErrosEDIService.storeAll(logsErrosEDI);
		}

		return validarEdiDTO;
	}
	/* LMSA-6702
	1	Se a quantidade de volumes for maior que zero (NOTA_FISCAL_EDI.QTDE_VOLUMES)
	e a quantidade de registros para a nota em questão na tabela VOLUME_NOTA_FISCAL_EDI for menor
	do que a quantidade de volumes da tabela NOTA_FISCAL_EDI.QTDE_VOLUMES, então criar um registro na tabela LOG_ERROS_EDI,
	conforme abaixo:
	 */
	private BigDecimal calcularVolumesFaltantesEDI(NotaFiscalEdi notaFiscalEdi) {
		BigDecimal totalRegistroVolumes = BigDecimalUtils.defaultBigDecimal(notaFiscalEDIVolumeService.countNotaFiscalVolumeByNotaFiscalEDI(notaFiscalEdi.getIdNotaFiscalEdi()));
		BigDecimal qtadeVolumesNotaFiscal = BigDecimalUtils.defaultBigDecimal(notaFiscalEdi.getQtdeVolumes());
		return qtadeVolumesNotaFiscal.subtract(totalRegistroVolumes);
	}

	private void executeLogsCubagemEDI(Cliente clienteRemetente, List<LogErrosEDI> logsErrosEDI, ValidarEdiDTO validarEdiDTO, String tpProcessamento, String pesoCubado) {
		if(BooleanUtils.isTrue(clienteRemetente.getBlObrigaPesoCubadoEdi())){
			logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.METRAGEM_CUBICA_M3, pesoCubado, null, null));
			logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.PESO_CUBADO, pesoCubado, null, null));
		}
	}

	//LMSA-3544
	private void preValidarCepConsignatarioNotaFiscalEdi(Cliente clienteRemetente, NotaFiscalEdi notaFiscalEdi, ValidarEdiDTO validarEdiDTO,
														 String tpProcessamento, List<LogErrosEDI> logsErrosEDI) {

		if (Boolean.TRUE.equals(clienteRemetente.getBlAtualizaConsignatarioEdi())) {
			if (notaFiscalEdi.getCnpjConsig() != null) {
				Cliente clienteConsignatario = clienteService
						.findByNrIdentificacao(getValidatedNrIdentificacao(notaFiscalEdi.getCnpjConsig().toString()));

				if (clienteConsignatario == null || "P".equals(clienteConsignatario.getTpCliente().getValue())
						|| "E".equals(clienteConsignatario.getTpCliente().getValue())) {
					Long idMunicipioConsignatario = findIdMunicipioBRAByCep(notaFiscalEdi.getCepEnderConsig());

					if (idMunicipioConsignatario == null && notaFiscalEdi.getMunicipioConsig() != null && notaFiscalEdi.getUfConsig() != null) {
						Municipio municipioDestino = municipioService.findByNmMunicipioAndUf(notaFiscalEdi.getMunicipioConsig(),
								notaFiscalEdi.getUfConsig());

						if (municipioDestino != null && municipioDestino.getNrCep() != null && !"".equals(municipioDestino.getNrCep())) {
							String dsValorErrado = null;
							if (notaFiscalEdi.getCepEnderConsig() != null) {
								dsValorErrado = notaFiscalEdi.getCepEnderConsig().toString();
							}

							logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.CEP_ENDER_CONSIG, dsValorErrado,
									municipioDestino.getNrCep(), JTDateTimeUtils.getDataHoraAtual()));

							String cep = municipioDestino.getNrCep();
							cep = cep.replaceAll("-", "");

							notaFiscalEdi = this.findById(notaFiscalEdi.getIdNotaFiscalEdi());
							notaFiscalEdi.setCepEnderConsig(Integer.valueOf(cep));
							this.store(notaFiscalEdi);
						}
					}
				}
			}
		}
	}

	/**
	 * LMS-3831
	 * @param clienteRemetente
	 * @param notaFiscalEdi
	 * @return
	 */
	private void preValidarCepDestinatarioNotaFiscalEdi(Cliente clienteRemetente, NotaFiscalEdi notaFiscalEdi, ValidarEdiDTO validarEdiDTO,
														String tpProcessamento, List<LogErrosEDI> logsErrosEDI) {

		if (Boolean.TRUE.equals(clienteRemetente.getBlAtualizaDestinatarioEdi())) {
			if (notaFiscalEdi.getCnpjDest() != null) {
				Cliente clienteDestinatario = clienteService
						.findByNrIdentificacao(getValidatedNrIdentificacao(notaFiscalEdi.getCnpjDest().toString()));

				if (clienteDestinatario == null || "P".equals(clienteDestinatario.getTpCliente().getValue())
						|| "E".equals(clienteDestinatario.getTpCliente().getValue())) {
					Long idMunicipioDestinatario = findIdMunicipioBRAByCep(notaFiscalEdi.getCepEnderDest());

					if (idMunicipioDestinatario == null && notaFiscalEdi.getMunicipioDest() != null && notaFiscalEdi.getUfDest() != null) {
						Municipio municipioDestino = municipioService.findByNmMunicipioAndUf(notaFiscalEdi.getMunicipioDest(),
								notaFiscalEdi.getUfDest());

						if (municipioDestino != null && municipioDestino.getNrCep() != null && !"".equals(municipioDestino.getNrCep())) {
							String dsValorErrado = null;
							if (notaFiscalEdi.getCepEnderDest() != null) {
								dsValorErrado = notaFiscalEdi.getCepEnderDest().toString();
							}

							logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.CEP_ENDER_DEST, dsValorErrado,
									municipioDestino.getNrCep(), JTDateTimeUtils.getDataHoraAtual()));

							String cep = municipioDestino.getNrCep();
							cep = cep.replaceAll("-", "");

							notaFiscalEdi = this.findById(notaFiscalEdi.getIdNotaFiscalEdi());
							notaFiscalEdi.setCepEnderDest(Integer.valueOf(cep));
							this.store(notaFiscalEdi);
						}
					}
				}
			}
		}
	}

	/**
	 * LMS-3831
	 * @param notaFiscalEdi
	 * @param eTipoCliente
	 * @return
	 */
	private Boolean preValidarClienteNotaFiscalEdi(NotaFiscalEdi notaFiscalEdi, ETipoCliente eTipoCliente) {
		Long cnpj = null;
		String nrInscricaoEstadual = null;
		String ufCliente = null;

		if (ETipoCliente.DESTINATARIO.equals(eTipoCliente)) {
			cnpj = notaFiscalEdi.getCnpjDest();
			nrInscricaoEstadual = notaFiscalEdi.getIeDest();
			ufCliente = notaFiscalEdi.getUfDest();
		} else if (ETipoCliente.TOMADOR.equals(eTipoCliente)) {
			cnpj = notaFiscalEdi.getCnpjTomador();
			nrInscricaoEstadual = notaFiscalEdi.getIeTomador();
			ufCliente = notaFiscalEdi.getUfTomador();
		} else if (ETipoCliente.CONSIGNATARIO.equals(eTipoCliente)) {
			cnpj = notaFiscalEdi.getCnpjConsig();
			nrInscricaoEstadual = notaFiscalEdi.getIeConsig();
			ufCliente = notaFiscalEdi.getUfConsig();
		} else if (ETipoCliente.REDESPACHO.equals(eTipoCliente)) {
			cnpj = notaFiscalEdi.getCnpjRedesp();
			nrInscricaoEstadual = notaFiscalEdi.getIeRedesp();
			ufCliente = notaFiscalEdi.getUfRedesp();
		}
		nrInscricaoEstadual = corrigeIE(nrInscricaoEstadual);

		Cliente cliente = null;
		if (cnpj != null) {
			cliente = clienteService.findByNrIdentificacao(getValidatedNrIdentificacao(cnpj.toString()));
		}

		if (cliente != null && !ConstantesVendas.SITUACAO_ATIVO.equals(cliente.getTpSituacao().getValue())) {
			return Boolean.TRUE;
		} else if (cliente == null
				|| (ETipoCliente.DESTINATARIO.equals(eTipoCliente) && cliente != null && Boolean.TRUE
				.equals(cliente.getBlAtualizaIEDestinatarioEdi()))) {

			if (StringUtils.isNotBlank(nrInscricaoEstadual)) {
				// Caso venha escrito "isento", transforma em caixa alta.
				nrInscricaoEstadual = nrInscricaoEstadual.toUpperCase();

				// Valida o número da inscrição estadual
				boolean ieValida = false;

				try {
					ieValida = ValidateUtils.validateInscricaoEstadual(ufCliente, nrInscricaoEstadual);
				} catch (NumberFormatException nfe) {
					return Boolean.FALSE;
				}

				if (!ieValida) {
					return Boolean.FALSE;
				}
			}
		}

		return Boolean.TRUE;
	}

	private Set<Long> getIdsGm(){
		String idGM = (String) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_IDS_GM);
		Set<Long> idsGM = new HashSet<Long>();

		try {
			for (String id : idGM.split(";")) {
				idsGM.add(Long.valueOf(id));
			}
		} catch (Exception e) {
			throw new IllegalStateException("PARAMETRO IDS_GM NÃO ESTÁ NO FORMATO ID;ID;ID;ID");
		}

		return idsGM;
	}

	/**
	 * LMS-3831
	 * @param validarEdiDTO
	 * @param tpProcessamento
	 * @param dsCampo
	 * @param dsValorErrado
	 */
	private LogErrosEDI popularLogErrosEdi(ValidarEdiDTO validarEdiDTO, String tpProcessamento, DsCampoLogErrosEDI dsCampoLogErrosEDI,
										   String dsValorErrado, String dsValorCorrigido, DateTime dhCorrecao) {
		LogErrosEDI logErrosEDI = new LogErrosEDI();
		logErrosEDI.setDsCampoLogErrosEDI(dsCampoLogErrosEDI);
		logErrosEDI.setDsTipoProcessamento(tpProcessamento);
		logErrosEDI.setDsValorErrado(dsValorErrado);
		logErrosEDI.setDsValorCorrigido(dsValorCorrigido);
		logErrosEDI.setNotaFiscalEdi(validarEdiDTO.getNotaFiscalEdi());
		logErrosEDI.setNrNotaFiscal(validarEdiDTO.getNotaFiscalEdi().getNrNotaFiscal());
		logErrosEDI.setDataEmissaoNf(validarEdiDTO.getNotaFiscalEdi().getDataEmissaoNf());
		logErrosEDI.setCnpjReme(validarEdiDTO.getNotaFiscalEdi().getCnpjReme());
		logErrosEDI.setNrProcessamento(validarEdiDTO.getNrProcessamento());
		logErrosEDI.setDhCorrecao(dhCorrecao);
		if(tpProcessamento.equals(ConstantesExpedicao.TP_PROCESSAMENTO_POR_NOTA_FISCAL)) {
			logErrosEDI.setNrOrdemDigitacao(validarEdiDTO.getIndex());
		}
		return logErrosEDI;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void validarNotaEdi(final Cliente clienteRemetente, final Long nrProcessamento, NotaFiscalEdi notaFiscalEdi, String tpProcessamento, String processarPor, Long idFilial) throws Exception {
		validateDadosNfe(clienteRemetente, notaFiscalEdi, tpProcessamento, idFilial);

		notaFiscalEdi = executeValidatePendenciaAtualizacaoByNrIdentificacaoByNrNotaFiscal(clienteRemetente.getPessoa().getNrIdentificacao(),
				notaFiscalEdi.getNrNotaFiscal(), processarPor, idFilial);

		if(processarPor != null && "T".equals(processarPor)){
			processarCliente(clienteRemetente, nrProcessamento, notaFiscalEdi, notaFiscalEdi.getCnpjReme(), ETipoCliente.REMETENTE.getDs(), processarPor);
		}

		processarCliente(clienteRemetente, nrProcessamento, notaFiscalEdi, notaFiscalEdi.getCnpjDest(), ETipoCliente.DESTINATARIO.getDs(), processarPor);

		if(notaFiscalEdi.getCnpjTomador() != null) {
			processarCliente(clienteRemetente, nrProcessamento, notaFiscalEdi, notaFiscalEdi.getCnpjTomador(),
					ETipoCliente.TOMADOR.getDs(), processarPor);
		}

		if(notaFiscalEdi.getCnpjConsig() != null) {
			processarCliente(clienteRemetente, nrProcessamento, notaFiscalEdi, notaFiscalEdi.getCnpjConsig(),
					ETipoCliente.CONSIGNATARIO.getDs(), processarPor);
		}

		if(notaFiscalEdi.getCnpjRedesp() != null) {
			processarCliente(clienteRemetente, nrProcessamento, notaFiscalEdi, notaFiscalEdi.getCnpjRedesp(),
					ETipoCliente.REDESPACHO.getDs(), processarPor);
		}

		if(BooleanUtils.isTrue(clienteRemetente.getBlObrigaPesoCubadoEdi()) && !BigDecimalUtils.hasValue(notaFiscalEdi.getPesoCubado())){
			throw new BusinessException("LMS-28012");
		}

		//RECUPERA A LISTA DE NOTA FISCAL EDI VOLUME PELO ID_NOTA_FISCAL_EDI
		final List<NotaFiscalEdiVolume> notaFiscalEdiVolumeList = notaFiscalExpedicaoEDIVolumeService.findByIdNotaFiscalEdi(notaFiscalEdi.getIdNotaFiscalEdi());

		//VERIFICA SE A QUANTIDADE DE VOLUMES INFORMADOS NA NOTA É IGUAL A QUANTIDADE DE VOLUMES RECUPERADOS
		if (clienteRemetente.getBlNumeroVolumeEDI() != null && clienteRemetente.getBlNumeroVolumeEDI() &&
				(notaFiscalEdi.getQtdeVolumes().intValue() != notaFiscalEdiVolumeList.size()) &&
				!clienteService.validateIsDanfeSimplificada(notaFiscalEdi.getCnpjReme())) {
			throw new BusinessException("LMS-28009");
		}

		this.validateTotalMercadoria(notaFiscalEdi);
	}

	/**
	 * LMS-1717
	 * Validar o preenchimento dos dados de NFe para cliente e filial com CTE configurado
	 * @param clienteRemetente
	 * @param notaFiscalEdi
	 * @return
	 */
	private void validateDadosNfe(Cliente clienteRemetente, NotaFiscalEdi notaFiscalEdi, String tpProcessamento, Long idFilial) {
		Object value = conteudoParametroFilialService.findConteudoByNomeParametro(idFilial, "INDICADOR CTE", false);
		/* LMS-3419 */
		if (value != null && ((Boolean.TRUE.equals(clienteRemetente.getBlPermiteCte()) &&  "1".equals(value)) || "2".equals(value))
				&& (clienteRemetente.getBlSemChaveNfeEdi() == null || Boolean.FALSE.equals(clienteRemetente.getBlSemChaveNfeEdi()))){
			if(notaFiscalEdi.getChaveNfe() == null ||
					notaFiscalEdi.getChaveNfe().length() != 44 ||
					!ValidateUtils.validateDigitoVerificadorNfe(notaFiscalEdi.getChaveNfe()) ||
					!validaDigitoChaveByTpProcessamento(notaFiscalEdi.getChaveNfe(), tpProcessamento)){
				throw new BusinessException("LMS-04400",new Object[]{clienteRemetente.getPessoa().getNrIdentificacao()});
			}
		}
	}

	private boolean validaDigitoChaveByTpProcessamento(String nrChave, String tProcessamento){
		boolean isNfe = "55".equals(nrChave.substring(20, 22));
		boolean isCte = "57".equals(nrChave.substring(20, 22));
		boolean isProcessamentoPorManifesto = "M".equalsIgnoreCase(tProcessamento);


		return (isCte && isProcessamentoPorManifesto) || isNfe;
	}

	public void validateTotalMercadoria(NotaFiscalEdi notaFiscalEdi) {
		if (notaFiscalEdi.getVlrTotalMerc() == null || notaFiscalEdi.getVlrTotalMerc().signum() <= 0 ){
			throw new BusinessException("LMS-04383");
		}
	}


	/**
	 * LMS-1685
	 * - Se o remetente NÃO tiver marcado para atualizar os dados do destinatário
	 * e o destinatário existir na base, utilizar o CEP do destinatário.
	 * - Se o remetente estiver marcado para atualizar os dados do destinatário
	 * e o destinatário existir na base e for um cliente especial ou filial de cliente especial,
	 * utilizar o CEP do destinatário.
	 * - Caso contrário utilizar o CEP do EDI.
	 */
	public Municipio findMunicipioDestinatario(NotaFiscalEdi notaFiscalEdi, Cliente clienteDestinatario, String processarPor) {
		String nrIdentificacao = null;
		if(processarPor!= null && "T".equals(processarPor)){
			nrIdentificacao = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjTomador().toString());
		}else{
			nrIdentificacao = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjReme().toString());
		}

		Cliente cliente = clienteService.findByNrIdentificacao(nrIdentificacao);

		Boolean atualizaDest = BooleanUtils.isTrue(cliente.getBlAtualizaDestinatarioEdi());
		if (!atualizaDest && clienteDestinatario != null) {
			return clienteDestinatario.getPessoa().getEnderecoPessoa().getMunicipio();
		}

		if (atualizaDest && clienteDestinatario != null &&
				Arrays.asList(new Object[]{"S", "F"}).contains(clienteDestinatario.getTpCliente().getValue())) {
			return clienteDestinatario.getPessoa().getEnderecoPessoa().getMunicipio();
		}

		return findMunicipioBRAByCep(notaFiscalEdi.getCepEnderDest());
	}

	private Municipio findMunicipioConsignatario(NotaFiscalEdi notaFiscalEdi, Cliente clienteConsignatario, String processarPor) {
		String nrIdentificacao = null;
		if(processarPor!= null && "T".equals(processarPor)){
			nrIdentificacao = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjTomador().toString());
		}else{
			nrIdentificacao = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjReme().toString());
		}

		Cliente cliente = clienteService.findByNrIdentificacao(nrIdentificacao);

		Boolean atualizaCons = BooleanUtils.isTrue(cliente.getBlAtualizaConsignatarioEdi());
		if (!atualizaCons && clienteConsignatario != null) {
			return clienteConsignatario.getPessoa().getEnderecoPessoa().getMunicipio();
		}

		if (atualizaCons && clienteConsignatario != null &&
				Arrays.asList(new Object[]{"S", "F"}).contains(clienteConsignatario.getTpCliente().getValue())) {
			return clienteConsignatario.getPessoa().getEnderecoPessoa().getMunicipio();
		}

		return findMunicipioBRAByCep(notaFiscalEdi.getCepEnderConsig());
	}


	/**
	 * LMS-1685
	 * O CEP informado não possui município relacionado
	 */
	private void validateMunicipioDestinatario(NotaFiscalEdi notaFiscalEdi, String processarPor) {
		String nrIdentificacaoDest = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjDest().toString());
		Cliente clienteDestinatario = clienteService.findByNrIdentificacao(nrIdentificacaoDest);

		Municipio municipioDest = findMunicipioDestinatario(notaFiscalEdi, clienteDestinatario, processarPor);

		if (municipioDest == null) {
			throw new BusinessException("LMS-04299", new Object[]{notaFiscalEdi.getCepEnderDest()});
		}
	}

	public void validateMunicipioConsignatario(NotaFiscalEdi notaFiscalEdi, String processarPor) {
		String nrIdentificacaoConsig = getValidatedNrIdentificacao(notaFiscalEdi.getCnpjConsig().toString());
		Cliente clienteConsignatario = clienteService.findByNrIdentificacao(nrIdentificacaoConsig);

		Municipio municipioConsig = findMunicipioConsignatario(notaFiscalEdi, clienteConsignatario, processarPor);

		if (municipioConsig == null) {
			throw new BusinessException("LMS-04299", new Object[]{notaFiscalEdi.getCepEnderConsig()});
		}
	}

	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public Map validateResponsavelPaleteFechado(final Map criteria) {
		Map result = new HashMap();
		final Long nrNotaFiscalEdi = (Long) criteria.get("idNotaFiscalEdi");
		NotaFiscalEdi notaFiscalEdi = notaFiscalExpedicaoEDIService.findById(nrNotaFiscalEdi);
		String tpDevedorFrete = null;
		Cliente responsavel = null;
		if((notaFiscalEdi.getCnpjTomador() != null) && (notaFiscalEdi.getCnpjTomador().longValue() > 0)) {
			final String nrIdentificacaoTomador = getValidatedNrIdentificacao(String.valueOf(notaFiscalEdi.getCnpjTomador()));
			if(!nrIdentificacaoTomador.equals(criteria.get("nrIdentificacaoRemetente")) && !nrIdentificacaoTomador.equals(criteria.get("nrIdentificacaoDestinatario"))) {
				tpDevedorFrete = ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL;
				responsavel = clienteService.findByNrIdentificacao(nrIdentificacaoTomador);
				result.put("responsavelBlPaleteFechado", responsavel != null ? (responsavel.getBlPaleteFechado() != null ? responsavel.getBlPaleteFechado() : false) : false);
			}
		}
		if (responsavel == null){
			if ("CIF".equals(notaFiscalEdi.getTipoFrete())){
				final String nrIdentificacaoRemetente = getValidatedNrIdentificacao(String.valueOf(notaFiscalEdi.getCnpjReme()));
				responsavel = clienteService.findByNrIdentificacao(nrIdentificacaoRemetente);
				result.put("responsavelBlPaleteFechado", responsavel != null ? (responsavel.getBlPaleteFechado() != null ? responsavel.getBlPaleteFechado() : false) : false);
			}
			else if ("FOB".equals(notaFiscalEdi.getTipoFrete())){
				final String nrIdentificacaoDestinatario = getValidatedNrIdentificacao(String.valueOf(notaFiscalEdi.getCnpjDest()));
				responsavel = clienteService.findByNrIdentificacao(nrIdentificacaoDestinatario);
				result.put("responsavelBlPaleteFechado", responsavel != null ? (responsavel.getBlPaleteFechado() != null ? responsavel.getBlPaleteFechado() : false) : false);
			}
			else{
				result.put("responsavelBlPaleteFechado", false);
			}
		}
		return result;
	}

	public ResponsavelEtiquetaDto validateResponsavelPaleteFechado(ValidateResponsavelEtiquetaDto validateResponsavelEtiquetaDto){
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("idNotaFiscalEdi", validateResponsavelEtiquetaDto.getIdNotaFiscalEdi());
		criteria.put("nrIdentificacaoRemetente", validateResponsavelEtiquetaDto.getNrIdentificacaoRemetente());
		Map<String, Object> result = validateResponsavelPaleteFechado(criteria);
		ResponsavelEtiquetaDto responsavelEtiquetaDto = new ResponsavelEtiquetaDto();
		responsavelEtiquetaDto.setBlPaleteFechado(BooleanUtils.isFalse((Boolean)result.get("responsavelBlPaleteFechado")));
		return responsavelEtiquetaDto;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processarCliente(final Cliente clienteRemetente, final Long nrProcessamento, final NotaFiscalEdi notaFiscalEdi, final Long cnpj, final String tpCliente, final String processarPor) throws Exception, BusinessException {
		if (ETipoCliente.DESTINATARIO.equals(tpCliente)) {
			validateMunicipioDestinatario(notaFiscalEdi, processarPor);
		}

		// LMSA-3544
		if (ETipoCliente.CONSIGNATARIO.equals(tpCliente)) {
			validateMunicipioConsignatario(notaFiscalEdi, processarPor);
		}

		/*
		 * Verificar se é um cliente cadastrado na tabela CLIENTE/PESSOA,
		 * utilizando o campo CNPJ_DEST para a consulta.
		 */

		final Cliente cliente = findClienteByNrIdentificacaoEDI(cnpj.toString());

		/*
		 * Se o cliente está cadastrado verificar se sua situação é igual a
		 * Ativo (CLIENTE.TP_SITUACAO_CLIENTE = “A”), se não for criar um
		 * registro na tabela LOG_ATUALIZACAO_EDI com a mensagem LMS-28006
		 * (parâmetro = “Destinatario”) e finalizar o processo com erro.
		 */
		if(cliente != null) {

			if (ETipoCliente.TOMADOR.equals(tpCliente)) {
				if(!ConstantesVendas.SITUACAO_ATIVO.equals(cliente.getTpSituacao().getValue())) {
					throw new BusinessException("LMS-04539", new Object[]{cnpj.toString()});
				}

			}else{
				if(!validateDadosBasicos(cliente)){
					throw new BusinessException("LMS-28006", new Object[]{cnpj.toString()});
				}
			}

			String nrIdentificacao = null;

			//Se não estiver marcado para atualizar endereço do cliente Destinatario, verifica endereço vigente, se não existe inclui um novo baseado
			//nas informações do EDI
			if (Boolean.FALSE.equals(clienteRemetente.getBlAtualizaDestinatarioEdi()) && ETipoCliente.DESTINATARIO.equals(tpCliente)){
				if((cnpj.toString().length() <= 11) && ValidateUtils.validateCpfOrCnpj(fillNumberWithZero(cnpj.toString(), 11))) {
					nrIdentificacao = fillNumberWithZero(cnpj.toString(), 11);
				} else {
					nrIdentificacao = fillNumberWithZero(cnpj.toString(), 14);

				}

				Pessoa dest = pessoaService.findByNrIdentificacao(nrIdentificacao);
				if(dest != null){
					List vigentes = enderecoPessoaService.findEnderecosVigentesByIdPessoa(dest.getIdPessoa());
					if (vigentes == null || vigentes.size() == 0){
						final HashMap mapStoreCliente = new HashMap();
						String nome = notaFiscalEdi.getNomeDest();
						String uf = notaFiscalEdi.getUfDest();
						String ie = corrigeIE(notaFiscalEdi.getIeDest());
						executeBuildMapEndereco(nome, ie, notaFiscalEdi.getCnpjDest(), notaFiscalEdi.getEnderecoDest(),
								notaFiscalEdi.getBairroDest(), notaFiscalEdi.getCepEnderDest(), uf, mapStoreCliente,Boolean.TRUE.equals(clienteRemetente.getBlAtualizaIEDestinatarioEdi()), tpCliente);

						TypedFlatMap tfm = new TypedFlatMap();
						tfm.putAll(mapStoreCliente);
						clienteService.saveEnderecoCliente(tfm, dest);
					}
				}

			}

			//Se o cliente do EDI tiver o campo CLIENTE.BL_ATUALIZA_CONSIGNATARIO_EDI = ‘N’, verificar se o cliente consignatário do EDI possui endereço do tipo comercial vigente.
			//Se não possuir atualizar o registro do cliente em ENDEREÇO_PESSOA com as informações contidas no EDI, usar mesma  estrutura utilizada pela integração.
			if (Boolean.FALSE.equals(clienteRemetente.getBlAtualizaConsignatarioEdi()) && ETipoCliente.CONSIGNATARIO.equals(tpCliente)){

				if((cnpj.toString().length() <= 11) && ValidateUtils.validateCpfOrCnpj(fillNumberWithZero(cnpj.toString(), 11))) {
					nrIdentificacao = fillNumberWithZero(cnpj.toString(), 11);
				} else {
					nrIdentificacao = fillNumberWithZero(cnpj.toString(), 14);
				}

				Pessoa consig = pessoaService.findByNrIdentificacao(nrIdentificacao);
				if(consig != null){
					List vigentes = enderecoPessoaService.findEnderecosVigentesByIdPessoa(consig.getIdPessoa());
					if (vigentes == null || vigentes.size() == 0){
						final HashMap mapStoreCliente = new HashMap();
						String nome = notaFiscalEdi.getNomeConsig();
						String uf = notaFiscalEdi.getUfConsig();
						String ie = corrigeIE(notaFiscalEdi.getIeConsig());
						executeBuildMapEndereco(nome, ie, notaFiscalEdi.getCnpjConsig(), notaFiscalEdi.getEnderecoConsig(),
								notaFiscalEdi.getBairroConsig(), notaFiscalEdi.getCepEnderConsig(), uf, mapStoreCliente,Boolean.TRUE.equals(clienteRemetente.getBlAtualizaIEDestinatarioEdi()), tpCliente);

						TypedFlatMap tfm = new TypedFlatMap();
						tfm.putAll(mapStoreCliente);
						clienteService.saveEnderecoCliente(tfm, consig);
					}
				}
			}

			//LMSA-2810
			if (Boolean.TRUE.equals(clienteRemetente.getBlAtualizaRazaoSocialDest()) && ETipoCliente.DESTINATARIO.equals(tpCliente)){
				if((cnpj.toString().length() <= 11) && ValidateUtils.validateCpfOrCnpj(fillNumberWithZero(cnpj.toString(), 11))) {
					nrIdentificacao = fillNumberWithZero(cnpj.toString(), 11);
				} else {
					nrIdentificacao = fillNumberWithZero(cnpj.toString(), 14);

				}

				Pessoa dest = pessoaService.findByNrIdentificacao(nrIdentificacao);
				if(dest != null){
					String nomeDestinatario = notaFiscalEdi.getNomeDest();
					dest.setNmPessoa(nomeDestinatario);
					pessoaService.store(dest);
				}
			}
			//LMSA-2810

			if (Boolean.TRUE.equals(clienteRemetente.getBlAtualizaIEDestinatarioEdi()) && ETipoCliente.DESTINATARIO.equals(tpCliente)){
				if (cliente != null){

					//Validação da inscrição estadual.
					String nrInscricaoEstadual = notaFiscalEdi.getIeDest();
					if(StringUtils.isNotBlank(nrInscricaoEstadual)) {

						//Caso venha escrito "isento", transforma em caixa alta.
						nrInscricaoEstadual = nrInscricaoEstadual.toUpperCase();

						//Valida o número da inscrição estadual
						String ufCliente = cliente.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();
						boolean ieValida = false;
						try{
							ieValida = ValidateUtils.validateInscricaoEstadual(ufCliente, nrInscricaoEstadual);
						}catch (NumberFormatException nfe){
							throw new BusinessException("LMS-28007",new Object[]{configuracoesFacade.getMensagem("destinatario")});
						}
						if (!ieValida){
							throw new BusinessException("LMS-28007",new Object[]{configuracoesFacade.getMensagem("destinatario")});
						}
					}

					// Busca as inscrições estaduais do cliente destinatário.
					List<InscricaoEstadual> ies = inscricaoEstadualService.findInscricaoByPessoa(cliente.getIdCliente());

					//Busca se a inscricao estadual informada já está cadastrada.
					InscricaoEstadual insc = null;
					if (ies != null){
						for (InscricaoEstadual ie:ies){
							if (ie.getNrInscricaoEstadual().equalsIgnoreCase(nrInscricaoEstadual)){
								insc = ie;
							}
						}
					}

					//Se não está cadastrada, desmarca a padrão e insere a inscricao estadual informada
					//como a padrão.
					if (insc == null){
						if (ies != null){
							for (InscricaoEstadual ie:ies){
								if (Boolean.TRUE.equals(ie.getBlIndicadorPadrao()) || "A".equals(ie.getTpSituacao().getValue()) ){
									ie.setBlIndicadorPadrao(Boolean.FALSE);
									ie.setTpSituacao(new DomainValue("I"));
									inscricaoEstadualService.storeBypassValidations(ie);
								}
							}
						}

						//Cria a nova inscricao estadual padrão.
						InscricaoEstadual ie = new InscricaoEstadual();
						ie.setPessoa(cliente.getPessoa());
						ie.setUnidadeFederativa(cliente.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa());
						ie.setBlIndicadorPadrao(Boolean.TRUE);
						ie.setTpSituacao(new DomainValue("A"));
						ie.setNrInscricaoEstadual(nrInscricaoEstadual);

						InformacaoDoctoCliente informacaoDoctoCliente = informacaoDoctoClienteService.findByIdClienteAndDsCampo(clienteRemetente.getIdCliente(), DS_CAMPO_CONTRIB);
						if (informacaoDoctoCliente != null) {
							inscricaoEstadualService.storeBasic(ie);
							validateContribuicaoCliente(clienteRemetente, notaFiscalEdi, ie, informacaoDoctoCliente);
						} else {
							inscricaoEstadualService.storeBypassValidations(ie);
						}
					}else{
						//Se encontrou a inscrição informada, ativar ela e marcar como padrão.
						insc.setTpSituacao(new DomainValue("A"));
						insc.setBlIndicadorPadrao(Boolean.TRUE);
						inscricaoEstadualService.storeBypassValidations(insc);

						//inativa as outras e desmarca o flag de padrão
						if (ies != null){
							for (InscricaoEstadual ie:ies){
								if (Boolean.TRUE.equals(ie.getBlIndicadorPadrao()) || "A".equals(ie.getTpSituacao().getValue()) ){
									if (!ie.getNrInscricaoEstadual().equals(nrInscricaoEstadual)){
										ie.setBlIndicadorPadrao(Boolean.FALSE);
										ie.setTpSituacao(new DomainValue("I"));
										inscricaoEstadualService.storeBypassValidations(ie);
									}
								}
							}
						}

						//Verificar vigência dos tipos de tributação da IE informada.

						//Tipo de tributação vigente ou a mais atual
						TipoTributacaoIE tipoTributacaoRecente = null;
						for (TipoTributacaoIE tt : insc.getTiposTributacaoIe()){
							if (tipoTributacaoRecente == null){
								tipoTributacaoRecente = tt;
							}else if (tt.getDtVigenciaInicial().isAfter(tipoTributacaoRecente.getDtVigenciaInicial())){
								tipoTributacaoRecente = tt;
							}
						}

						if (tipoTributacaoRecente.getDtVigenciaFinal() != null && tipoTributacaoRecente.getDtVigenciaFinal().isBefore(JTDateTimeUtils.getDataAtual())){
							tipoTributacaoRecente.setDtVigenciaFinal(null);
							tipoTributacaoIEService.storeSkipValidations(tipoTributacaoRecente);
						}

						InformacaoDoctoCliente informacaoDoctoCliente = informacaoDoctoClienteService.findByIdClienteAndDsCampo(clienteRemetente.getIdCliente(), DS_CAMPO_CONTRIB);
						if (informacaoDoctoCliente != null) {
							validateContribuicaoCliente(clienteRemetente, notaFiscalEdi, insc, informacaoDoctoCliente);
						}
					}
				}
			}

		} else {
			/*
			 * Se o cliente não estiver cadastrado validar os dados do mesmo
			 * conforme abaixo: · NOME_DEST: deverá estar preenchido com no
			 * mínimo 5 posições · IE_DEST: Chamar rotinas padrões de validação
			 * da IE por UF (já utilizadas na inclusão de cliente na digitação
			 * do conhecimento) Se algum dado não estiver ok criar um registro
			 * na tabela LOG_ATUALIZACAO_EDI com a mensagem LMS-28007 (parâmetro
			 * = “Destinatario”) e finalizar o processo com erro.
			 */
			validateDadosAndStoreClienteByTpCliente(clienteRemetente, nrProcessamento, notaFiscalEdi, tpCliente);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean validateDadosBasicos(Cliente clienteAux) {
		List<Map<String, Object>> result = null;
		result = clienteService.findLookupClienteEnderecoValidaDadosBasicos(clienteAux.getIdCliente());
		return CollectionUtils.isNotEmpty(result);
	}

	//	LMS-8108
	private void validateContribuicaoCliente(final Cliente clienteRemetente, final NotaFiscalEdi notaFiscalEdi, InscricaoEstadual insc, InformacaoDoctoCliente informacaoDoctoCliente) {
		TipoTributacaoIE tipoTributacaoIE = tipoTributacaoIEService.findTiposTributacaoIEVigente(insc.getIdInscricaoEstadual(), JTDateTimeUtils.getDataAtual());;

		if (tipoTributacaoIE == null) {
			tipoTributacaoIE = new TipoTributacaoIE();
			tipoTributacaoIE.setInscricaoEstadual(insc);
			tipoTributacaoIE.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
			tipoTributacaoIE.setBlAceitaSubstituicao(true);
			tipoTributacaoIE.setBlIncentivada(false);
			tipoTributacaoIE.setBlIsencaoExportacoes(false);
		}

		NotaFiscalEdiComplemento nfec = notaFiscalEDIComplementoService.findByIdInformacaoDocClienteAndIdNotaFiscalEdi(informacaoDoctoCliente.getIdInformacaoDoctoCliente(), notaFiscalEdi.getIdNotaFiscalEdi());
		if (nfec.getValorComplemento() != null && "S".equals(nfec.getValorComplemento())) {
			executeTipoTributacaoIE(insc, tipoTributacaoIE, new DomainValue("CO"));
		} else if (nfec.getValorComplemento() == null || "N".equals(nfec.getValorComplemento())) {
			executeTipoTributacaoIE(insc, tipoTributacaoIE, new DomainValue("NC"));
		}
	}

	private void executeTipoTributacaoIE(InscricaoEstadual insc, TipoTributacaoIE tipoTributacaoIE, DomainValue tpSituacaoTributariaIE) {
		if (tipoTributacaoIE.getIdTipoTributacaoIE() != null &&
				!tipoTributacaoIE.getTpSituacaoTributaria().getValue().equals(tpSituacaoTributariaIE.getValue())) {
//			Fecha tributação IE anterior
			tipoTributacaoIE.setDtVigenciaFinal(JTDateTimeUtils.getDataAtual().minusDays(IntegerUtils.ONE));
			tipoTributacaoIEService.storeSkipValidations(tipoTributacaoIE);

//			Cria uma nova tributação IE
			TipoTributacaoIE tipoTributacaoIENovo = new TipoTributacaoIE();
			tipoTributacaoIENovo.setInscricaoEstadual(insc);
			tipoTributacaoIENovo.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
			tipoTributacaoIENovo.setBlAceitaSubstituicao(tipoTributacaoIE.getBlAceitaSubstituicao());
			tipoTributacaoIENovo.setBlIncentivada(tipoTributacaoIE.getBlIncentivada());
			tipoTributacaoIENovo.setBlIsencaoExportacoes(tipoTributacaoIE.getBlIsencaoExportacoes());
			tipoTributacaoIENovo.setTpSituacaoTributaria(tpSituacaoTributariaIE);
			tipoTributacaoIEService.storeSkipValidations(tipoTributacaoIENovo);
		} else if (tipoTributacaoIE.getIdTipoTributacaoIE() == null) {
			tipoTributacaoIE.setTpSituacaoTributaria(tpSituacaoTributariaIE);
			tipoTributacaoIEService.storeSkipValidations(tipoTributacaoIE);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void validateDadosAndStoreClienteByTpCliente(final Cliente clienteRemetente, final Long nrProcessamento, final NotaFiscalEdi notaFiscalEdi, final String tpCliente) throws Exception, BusinessException {
		final HashMap mapStoreCliente = new HashMap();

		String nome = null;
		String uf = null;
		String ie = null;

		if(ETipoCliente.CONSIGNATARIO.equals(tpCliente)) {
			nome = notaFiscalEdi.getNomeConsig();
			uf = notaFiscalEdi.getUfConsig();
			ie = corrigeIE(notaFiscalEdi.getIeConsig());
			executeBuildMapEndereco(nome, ie, notaFiscalEdi.getCnpjConsig(), notaFiscalEdi.getEnderecoConsig(),
					notaFiscalEdi.getBairroConsig(), notaFiscalEdi.getCepEnderConsig(), uf, mapStoreCliente,false, tpCliente);
		} else if(ETipoCliente.DESTINATARIO.equals(tpCliente)) {
			if(notaFiscalEdi.getCnpjDest() == null) {
				throw new BusinessException("LMS-28007");
			} else {
				nome = notaFiscalEdi.getNomeDest();
				uf = notaFiscalEdi.getUfDest();
				ie = corrigeIE(notaFiscalEdi.getIeDest());
				executeBuildMapEndereco(nome, ie, notaFiscalEdi.getCnpjDest(), notaFiscalEdi.getEnderecoDest(),
						notaFiscalEdi.getBairroDest(), notaFiscalEdi.getCepEnderDest(), uf, mapStoreCliente,Boolean.TRUE.equals(clienteRemetente.getBlAtualizaIEDestinatarioEdi()), tpCliente);
			}
		} else if(ETipoCliente.REDESPACHO.equals(tpCliente)) {
			nome = notaFiscalEdi.getNomeRedesp();
			uf = notaFiscalEdi.getUfRedesp();
			ie = corrigeIE(notaFiscalEdi.getIeRedesp());
			executeBuildMapEndereco(nome, ie, notaFiscalEdi.getCnpjRedesp(), notaFiscalEdi.getEnderecoRedesp(),
					notaFiscalEdi.getBairroRedesp(), notaFiscalEdi.getCepEnderRedesp(), uf, mapStoreCliente,false, tpCliente);
		} else if(ETipoCliente.REMETENTE.equals(tpCliente)) {
			nome = notaFiscalEdi.getNomeReme();
			uf = notaFiscalEdi.getUfReme();
			ie = corrigeIE(notaFiscalEdi.getIeReme());
			executeBuildMapEndereco(nome, ie, notaFiscalEdi.getCnpjReme(), notaFiscalEdi.getEnderecoReme(),
					notaFiscalEdi.getBairroReme(), notaFiscalEdi.getCepEnderReme(), uf, mapStoreCliente,false, tpCliente);
		} else if(ETipoCliente.TOMADOR.equals(tpCliente)) {
			nome = notaFiscalEdi.getNomeTomador();
			uf = notaFiscalEdi.getUfTomador();
			ie = corrigeIE(notaFiscalEdi.getIeTomador());
			executeBuildMapEndereco(nome, ie, notaFiscalEdi.getCnpjTomador(), notaFiscalEdi.getEnderecoTomador(),
					notaFiscalEdi.getBairroTomador(), notaFiscalEdi.getCepEnderTomador(), uf, mapStoreCliente,false, tpCliente);
		}

		if((nome == null) || (nome.trim().length() < 5) || !ValidateUtils.validateInscricaoEstadual(uf, ie)) {
			throw new BusinessException("LMS-28007");
		}

		/*
		 * Caso os dados estejam ok, incluir o cliente conforme regras de
		 * gravação da tela 04.01.01.02 - Cadastrar clientes. O endereço deverá
		 * ser convertido para o padrão do LMS utilizando-se a rotina de
		 * separação de endereço.
		 */
		try {
			notaFiscalExpedicaoEDIService.storeCliente(clienteRemetente, nrProcessamento, mapStoreCliente, tpCliente);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			if(!e.getMessage().contains("PESS_02_UK") && !e.getMessage().contains("pess_02_uk")
					&& !e.getMessage().contains("PESS_IDX_") && !e.getMessage().contains("pess_idx_")){
				throw e;
			}
		}
	}

	private String corrigeIE(final String ie) {
		if((ie != null) && !ie.equals("")) {
			return ie.replaceAll("\\.", "").replaceAll("-", "");
		}
		return ie;
	}

	public NotaFiscalEdi executeValidatePendenciaAtualizacaoByNrIdentificacaoByNrNotaFiscal(final String nrIdentificacao, final Integer nrNotaFiscal, String processarPor, Long idFilial) {
		NotaFiscalEdi notaFiscalEdi = notaFiscalExpedicaoEDIService.findByNrIdentificacaoByNrNotaFiscal(nrIdentificacao, nrNotaFiscal, processarPor);
		Object habilitaReprocessamento = configuracoesFacade.getValorParametro(idFilial, "REPROCESSA_EDI");
		if(notaFiscalEdi == null){
			if ("S".equals(habilitaReprocessamento)) {
				LogEDIDetalhe logEdiNota = logEDIDetalheService.findByNrNotaFiscalReprocessamento(nrIdentificacao, nrNotaFiscal);
				if (logEdiNota != null){
					notaFiscalEdi = executeRecargaNotaEDI(logEdiNota);
				}
			}
		}

		if (notaFiscalEdi == null){
			throw new BusinessException("LMS-28005", new Object[] { nrNotaFiscal });
		}
		return notaFiscalEdi;

	}

	public List<NotaFiscalEdi> executeValidatePendenciaAtualizacaoByNrIdentificacaoByIntervaloNotaFiscal
	(final String nrIdentificacao, final Integer nrNotaFiscalInicial,
	 final Integer nrNotaFiscalFinal, Long idFilial) {
		List<NotaFiscalEdi> notaFiscalEdi = notaFiscalExpedicaoEDIService
			.findByNrIdentificacaoByIntervaloNotaFiscal(nrIdentificacao, nrNotaFiscalInicial, nrNotaFiscalFinal);

		Object habilitaReprocessamento = configuracoesFacade.getValorParametro(idFilial, "REPROCESSA_EDI");
		if(notaFiscalEdi.isEmpty()){
			if ("S".equals(habilitaReprocessamento)) {
				List<LogEDIDetalhe> logEdiNota = logEDIDetalheService
					.findByNrNotaFiscalReprocessamento(nrIdentificacao, nrNotaFiscalInicial, nrNotaFiscalFinal);

					notaFiscalEdi =  logEdiNota.stream().map(this::executeRecargaNotaEDI).collect(Collectors.toList());

			}
		}

		if (notaFiscalEdi.isEmpty()){
			throw new BusinessException("LMS-28005", new Object[] { nrNotaFiscalFinal });
		}
		return notaFiscalEdi;
	}


	public NotaFiscalEdi executeRecargaNotaEDI(final LogEDIDetalhe logEdiDetalhe){
		return executeRecargaNotaEDI(logEdiDetalhe, null);
	}

	/**
	 * Converte o objeto LogEDIDetalhe para NotaFiscalEdi
	 * @param idCliente
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public NotaFiscalEdi executeRecargaNotaEDI(final LogEDIDetalhe logEdiDetalhe, Long idCliente) {
		NotaFiscalEdi notaFiscalEdi = new NotaFiscalEdi();

		notaFiscalEdi.setNomeReme(logEdiDetalhe.getNomeReme());
		notaFiscalEdi.setCnpjReme(logEdiDetalhe.getCnpjReme());
		notaFiscalEdi.setIeReme(logEdiDetalhe.getIeReme());
		notaFiscalEdi.setEnderecoReme(logEdiDetalhe.getEnderecoReme());
		notaFiscalEdi.setBairroReme(logEdiDetalhe.getBairroReme());
		notaFiscalEdi.setMunicipioReme(logEdiDetalhe.getMunicipioReme());
		notaFiscalEdi.setUfReme(logEdiDetalhe.getUfReme());
		notaFiscalEdi.setCepEnderReme(logEdiDetalhe.getCepEnderReme());
		notaFiscalEdi.setCepMuniReme(logEdiDetalhe.getCepMuniReme());
		notaFiscalEdi.setNomeDest(logEdiDetalhe.getNomeDest());
		notaFiscalEdi.setCnpjDest(logEdiDetalhe.getCnpjDest());
		notaFiscalEdi.setIeDest(logEdiDetalhe.getIeDest());
		notaFiscalEdi.setEnderecoDest(logEdiDetalhe.getEnderecoDest());
		notaFiscalEdi.setBairroDest(logEdiDetalhe.getBairroDest());
		notaFiscalEdi.setMunicipioDest(logEdiDetalhe.getMunicipioDest());
		notaFiscalEdi.setUfDest(logEdiDetalhe.getUfDest());
		notaFiscalEdi.setCepEnderDest(logEdiDetalhe.getCepEnderDest());
		notaFiscalEdi.setCepMunicDest(logEdiDetalhe.getCepMunicDest());
		notaFiscalEdi.setNomeConsig(logEdiDetalhe.getNomeConsig());
		notaFiscalEdi.setCnpjConsig(logEdiDetalhe.getCnpjConsig());
		notaFiscalEdi.setIeConsig(logEdiDetalhe.getIeConsig());
		notaFiscalEdi.setEnderecoConsig(logEdiDetalhe.getEnderecoConsig());
		notaFiscalEdi.setBairroConsig(logEdiDetalhe.getBairroConsig());
		notaFiscalEdi.setMunicipioConsig(logEdiDetalhe.getMunicipioConsig());
		notaFiscalEdi.setUfConsig(logEdiDetalhe.getUfConsig());
		notaFiscalEdi.setCepEnderConsig(logEdiDetalhe.getCepEnderConsig());
		notaFiscalEdi.setCepMunicConsig(logEdiDetalhe.getCepMunicConsig());
		notaFiscalEdi.setNomeRedesp(logEdiDetalhe.getNomeRedesp());
		notaFiscalEdi.setCnpjRedesp(logEdiDetalhe.getCnpjRedesp());
		notaFiscalEdi.setIeRedesp(logEdiDetalhe.getIeRedesp());
		notaFiscalEdi.setEnderecoRedesp(logEdiDetalhe.getEnderecoRedesp());
		notaFiscalEdi.setBairroRedesp(logEdiDetalhe.getBairroRedesp());
		notaFiscalEdi.setMunicipioRedesp(logEdiDetalhe.getMunicipioRedesp());
		notaFiscalEdi.setUfRedesp(logEdiDetalhe.getUfRedesp());
		if (logEdiDetalhe.getCepEnderRedesp() != null)
			notaFiscalEdi.setCepEnderRedesp(Integer.parseInt(logEdiDetalhe.getCepEnderRedesp()));
		if (logEdiDetalhe.getCepMunicRedesp() != null)
			notaFiscalEdi.setCepMunicRedesp(Integer.parseInt(logEdiDetalhe.getCepMunicRedesp()));
		notaFiscalEdi.setNomeTomador(logEdiDetalhe.getNomeTomador());
		notaFiscalEdi.setCnpjTomador(logEdiDetalhe.getCnpjTomador());
		notaFiscalEdi.setIeTomador(logEdiDetalhe.getIeTomador());
		notaFiscalEdi.setEnderecoTomador(logEdiDetalhe.getEnderecoTomador());
		notaFiscalEdi.setBairroTomador(logEdiDetalhe.getBairroTomador());
		notaFiscalEdi.setMunicipioTomador(logEdiDetalhe.getMunicipioTomador());
		notaFiscalEdi.setUfTomador(logEdiDetalhe.getUfTomador());
		notaFiscalEdi.setCepEnderTomador(logEdiDetalhe.getCepEnderTomador());
		notaFiscalEdi.setCepMunicTomador(logEdiDetalhe.getCepMunicTomador());
		notaFiscalEdi.setNatureza(logEdiDetalhe.getNatureza());
		notaFiscalEdi.setEspecie(logEdiDetalhe.getEspecie());
		notaFiscalEdi.setTipoFrete(logEdiDetalhe.getTipoFrete());
		notaFiscalEdi.setModalFrete(logEdiDetalhe.getModalFrete());
		notaFiscalEdi.setTipoTabela(logEdiDetalhe.getTipoTabela());
		notaFiscalEdi.setTarifa(logEdiDetalhe.getTarifa());
		notaFiscalEdi.setSerieNf(logEdiDetalhe.getSerieNf());
		notaFiscalEdi.setNrNotaFiscal(logEdiDetalhe.getNrNotaFiscal());
		notaFiscalEdi.setQtdeVolumes(logEdiDetalhe.getQtdeVolumes());
		notaFiscalEdi.setVlrTotalMerc(logEdiDetalhe.getVlrTotalMerc());
		notaFiscalEdi.setPesoReal(logEdiDetalhe.getPesoReal());
		notaFiscalEdi.setPesoCubado(logEdiDetalhe.getPesoCubado());
		notaFiscalEdi.setChaveNfe(logEdiDetalhe.getChaveNfe());
		notaFiscalEdi.setVlrIcmsNf(logEdiDetalhe.getVlrIcmsNf());
		notaFiscalEdi.setVlrIcmsStNf(logEdiDetalhe.getVlrIcmsStNf());
		notaFiscalEdi.setAliqNf(logEdiDetalhe.getAliqNf());
		notaFiscalEdi.setVlrBaseCalcNf(logEdiDetalhe.getVlrBaseCalcNf());
		notaFiscalEdi.setVlrBaseCalcStNf(logEdiDetalhe.getVlrBaseCalcStNf());
		notaFiscalEdi.setVlrTotProdutosNf(logEdiDetalhe.getVlrTotProdutosNf());
		notaFiscalEdi.setCfopNf(logEdiDetalhe.getCfopNf());
		notaFiscalEdi.setPinSuframa(logEdiDetalhe.getPinSuframa());
		notaFiscalEdi.setVlrFretePeso(logEdiDetalhe.getVlrFretePeso());
		notaFiscalEdi.setVlrFreteValor(logEdiDetalhe.getVlrFreteValor());
		notaFiscalEdi.setVlrCat(logEdiDetalhe.getVlrCat());
		notaFiscalEdi.setVlrDespacho(logEdiDetalhe.getVlrDespacho());
		notaFiscalEdi.setVlrItr(logEdiDetalhe.getVlrItr());
		notaFiscalEdi.setVlrAdeme(logEdiDetalhe.getVlrAdeme());
		notaFiscalEdi.setVlrPedagio(logEdiDetalhe.getVlrPedagio());
		notaFiscalEdi.setVlrTaxas(logEdiDetalhe.getVlrTaxas());
		notaFiscalEdi.setOutrosValores(logEdiDetalhe.getOutrosValores());
		notaFiscalEdi.setVlrIcms(logEdiDetalhe.getVlrIcms());
		notaFiscalEdi.setVlrBaseCalcIcms(logEdiDetalhe.getVlrBaseCalcIcms());
		notaFiscalEdi.setAliqIcms(logEdiDetalhe.getAliqIcms());
		notaFiscalEdi.setVlrFreteLiquido(logEdiDetalhe.getVlrFreteLiquido());
		notaFiscalEdi.setVlrFreteTotal(logEdiDetalhe.getVlrFreteTotal());
		notaFiscalEdi.setPesoRealTotal(logEdiDetalhe.getPesoRealTotal());
		notaFiscalEdi.setPesoCubadoTotal(logEdiDetalhe.getPesoCubadoTotal());
		notaFiscalEdi.setVlrTotalMercTotal(logEdiDetalhe.getVlrTotalMercTotal());
		notaFiscalEdi.setSequenciaAgrupamento(logEdiDetalhe.getSequenciaAgrupamento());
		notaFiscalEdi.setDsDivisaoCliente(logEdiDetalhe.getDsDivisaoCliente());
		notaFiscalEdi.setNrCtrcSubcontratante(logEdiDetalhe.getNrCtrcSubcontratante());
		notaFiscalEdi.setDataEmissaoNf(JTDateTimeUtils.yearMonthDayToDateTime(logEdiDetalhe.getDataEmissaoNf()).toDate());
		notaFiscalEdi.setIdNotaFiscalEdi(getNotaFiscalEdiDAO().findSequence());
		super.store(notaFiscalEdi);

		Cliente cliente = null;
		if (idCliente != null){
			cliente = clienteService.findById(idCliente);
		}else{
			cliente = findClienteByNrIdentificacaoEDI(notaFiscalEdi.getCnpjReme().toString());
		}

		if (cliente != null){

			List<LogEDIComplemento> complementos = logEDIComplementoService.findByLogEDIDetalhe(logEdiDetalhe);
			for (LogEDIComplemento logComplemento : complementos) {
				NotaFiscalEdiComplemento complemento = new NotaFiscalEdiComplemento();
				complemento.setNotaFiscalEdi(notaFiscalEdi);
				complemento.setValorComplemento(logComplemento.getValorComplemento());

				InformacaoDoctoCliente informacaoDoctoCliente = informacaoDoctoClienteService.findByIdClienteAndDsCampo(cliente.getIdCliente(), logComplemento.getNomeComplemento());
				if (informacaoDoctoCliente != null){
					complemento.setIndcIdInformacaoDoctoClien(informacaoDoctoCliente.getIdInformacaoDoctoCliente());
					this.notaFiscalEDIComplementoService.store(complemento);
				}
			}

		}
		List<LogEDIVolume> volumes = logEDIVolumeService.findByLogEDIDetalhe(logEdiDetalhe);
		for (LogEDIVolume logVolume : volumes) {
			NotaFiscalEdiVolume volume = new NotaFiscalEdiVolume();
			volume.setNotaFiscalEdi(notaFiscalEdi);
			volume.setCodigoVolume(logVolume.getCodigoVolume());
			volume.setCdBarraPostoAvancado(logVolume.getCdBarraPostoAvancado());
			this.notaFiscalEDIVolumeService.store(volume);
		}

		return notaFiscalEdi;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map executeValidatePendenciaAtualizacao(final Map criteria) {
		final String nrIdentificacao = (String) criteria.get("nrIdentificacao");
		final Integer nrNotaFiscal = (Integer) criteria.get("nrNotaFiscal");

		final NotaFiscalEdi notaFiscalEdi = executeValidatePendenciaAtualizacaoByNrIdentificacaoByNrNotaFiscal(nrIdentificacao, nrNotaFiscal, null, SessionUtils.getFilialSessao().getIdFilial());

		validateNotasConhecimento(criteria, nrIdentificacao, notaFiscalEdi, notaFiscalEdi.getSerieNf());

		final Map mapReturn = new HashMap();
		mapReturn.put("idNotaFiscalEdi", notaFiscalEdi.getIdNotaFiscalEdi());
		mapReturn.put("nrNotaFiscal", notaFiscalEdi.getNrNotaFiscal());
		mapReturn.put("qtdeVolumes", notaFiscalEdi.getQtdeVolumes());
		mapReturn.put("dataHora", JTDateTimeUtils.getDataHoraAtual());

		mapReturn.put("cnpjDest", FormatUtils.formatCNPJ(notaFiscalEdi.getCnpjDest().toString()));
		mapReturn.put("nomeDest", notaFiscalEdi.getNomeDest());


		return mapReturn;
	}

	@SuppressWarnings("rawtypes")
	private void validateNotasConhecimento(final Map criteria, final String nrIdentificacao, final NotaFiscalEdi notaFiscalEdi, final String dsSerie) {
		final String nrChave = (String) criteria.get("nrChave");

		final List<Integer> nrNotasFiscais = new ArrayList<Integer>(1);
		nrNotasFiscais.add((Integer) criteria.get("nrNotaFiscal"));

		final List<YearMonthDay> datas = new ArrayList<YearMonthDay>(1);
		datas.add(new YearMonthDay(notaFiscalEdi.getDataEmissaoNf()));

		final Cliente cliente = clienteService.findByNrIdentificacao(nrIdentificacao);
		List<String> dsSeries = new ArrayList<String>(1);
		dsSeries.add(dsSerie);
		verifyNotasConhecimento(cliente.getIdCliente(), nrNotasFiscais, datas, dsSeries);
		validateChaveNfe(chooseNrChaveNfe(notaFiscalEdi.getChaveNfe(), nrChave), notaFiscalEdi.getCnpjReme(), notaFiscalEdi.getNrNotaFiscal(), JTDateTimeUtils.convertDateToYearMonthDay(notaFiscalEdi.getDataEmissaoNf()), null);
	}

	public void verifyNotasConhecimento(final Long idClienteRemetente, final List<Integer> nrNotasFiscais, final List<YearMonthDay> datas, final List<String> dsSeries) {
		verifyNrNotasFiscais(idClienteRemetente, nrNotasFiscais, datas, null,dsSeries);
		verifyDataEmissaoNotasFiscais(nrNotasFiscais, datas);
	}

	public void validateChaveNfe(String chaveNfe, Long cnpjReme, Integer nrNotafiscal, YearMonthDay dtEmissao, Long idConhecimentoOriginal) {
		String validaChaveNFE = (String) conteudoParametroFilialService.findConteudoByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "VALIDA_CHAVE_NFE", false);
		if("S".equals(validaChaveNFE)){
			verifyChavesNotasFiscais(chaveNfe, idConhecimentoOriginal);
			validateRemetenteChaveNfe(chaveNfe, cnpjReme.toString());
			validateNrNotaFiscalChaveNfe(chaveNfe, nrNotafiscal);
			validateDataEmissaoChaveNfe(chaveNfe, dtEmissao);
		}
	}

	private String chooseNrChaveNfe(String nrChaveNotaFiscalEdi, String nrChave){
		return nrChaveNotaFiscalEdi != null ? nrChaveNotaFiscalEdi : nrChave;
	}

	private void validateRemetenteChaveNfe(String chaveNfe, String cnpjReme) {
		String nrIdentificacao = getValidatedNrIdentificacao(cnpjReme);
		Cliente clienteRemetente = clienteService.findByNrIdentificacao(nrIdentificacao);
		if(chaveNfe == null || clienteRemetente == null || !clienteRemetente.getPessoa().getNrIdentificacao().equals(chaveNfe.substring(6, 20))){
			throw new BusinessException("LMS-04497");
		}
	}

	private void validateNrNotaFiscalChaveNfe(String chaveNfe, Integer nrNotaFiscal) {
		Integer nrNotaFiscalChaveNfe = Integer.valueOf(chaveNfe.substring(25, 34));
		if(nrNotaFiscal == null || !nrNotaFiscal.equals(nrNotaFiscalChaveNfe)){
			throw new BusinessException("LMS-04562");
		}
	}

	public void validateDataEmissaoChaveNfe(String chaveNfe, YearMonthDay dtEmissao){
		String dtChaveNfe = chaveNfe.substring(2, 6);
		String formattedDtEmissao = JTDateTimeUtils.getFormattedYearMonth(dtEmissao);

		if(dtChaveNfe == null || formattedDtEmissao == null || !dtChaveNfe.equals(formattedDtEmissao)){
			throw new BusinessException("LMS-04563");
		}
	}

	@SuppressWarnings("rawtypes")
	private void verifyChavesNotasFiscais(String chaveNfe, Long idConhecimentoOriginal) {
		if(chaveNfe != null && !chaveNfe.isEmpty()){
			StringBuilder builder = new StringBuilder();
			List returned = notaFiscalConhecimentoService.findByNrChaveIdConhecimentoOriginal(chaveNfe, idConhecimentoOriginal);
			if(returned != null && !returned.isEmpty()){
				Conhecimento conhecimento = (Conhecimento)((Object[])returned.get(0))[1];

				Filial origem = null;
				DomainValue tpDocumento = null;
				Long nrDocumento = null;
				if(conhecimento != null){
					tpDocumento = conhecimento.getTpDoctoServico();
					origem = conhecimento.getFilialOrigem();
					nrDocumento = conhecimento.getNrDoctoServico();
				}

				if(tpDocumento != null){
					builder.append(tpDocumento.getValue());
				}

				if(origem != null){
					builder.append(" ");
					builder.append(origem.getSgFilial());
				}

				if(nrDocumento != null){
					builder.append(" ");
					builder.append(nrDocumento);
				}

				throw new BusinessException("LMS-04498", new Object[]{builder.toString()});
			}
		}

	}

	private void verifyNrNotasFiscais(final Long idRemetente, final List<Integer> nrNotasFiscais, List<YearMonthDay> datas, final Long idConhecimento, List<String> dsSeries) {
		if(idRemetente == null) {
			return;
		}
		final List<Object[]> result = notaFiscalConhecimentoService.findListByRemetenteNrNotasDsSerie(idRemetente, nrNotasFiscais, dsSeries);
		// TODO LMSA-7137
		if((result != null) && !result.isEmpty()) {
			final StringBuilder param = new StringBuilder();
			for (final Iterator<Object[]> iter = result.iterator(); iter.hasNext();) {
				Object[] item = iter.next();
				final Integer nrNota = (Integer) item[0];
				if(idConhecimento == null || !idConhecimento.equals(item[1])) {
					param.append(nrNota);
					if(iter.hasNext()) {
						param.append("/");
					}
				}
			}
			if(param.length() > 0){
				throw new BusinessException("LMS-04202", new Object[] { param.toString() });
			}
		}
	}

	private void verifyDataEmissaoNotasFiscais(final List<Integer> nrNotasFiscais, final List<YearMonthDay> datas) {
		final BigDecimal validade = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.DIAS_VALIDADE_NF);
		int nrDias = 0;
		if(validade != null) {
			nrDias = -(validade.intValue());
		}
		final YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
		final YearMonthDay dtValidade = dtAtual.plusDays(nrDias);
		for (int i = 0; i < datas.size(); i++) {
			final YearMonthDay data = datas.get(i);
			if(data != null) {
				if(JTDateTimeUtils.comparaData(data, dtValidade) < 0) {
					throw new BusinessException("LMS-04020");
				}
			}
		}
	}

	private String getLocalizedTpCliente(String tipoCliente) {
		ETipoCliente[] tipoClientes = ETipoCliente.values();
		for (ETipoCliente eTipoCliente : tipoClientes) {
			if (eTipoCliente.equals(tipoCliente)) {
				return configuracoesFacade.getMensagem(eTipoCliente.getKey());
			}
		}
		return null;
	}


	public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
		this.informacaoDoctoClienteService = informacaoDoctoClienteService;
	}

	public void setDpeService(DpeService dpeService) {
		this.dpeService = dpeService;
	}

	private enum ETipoCliente {
		CONSIGNATARIO("CONS", "consignatario"),
		DESTINATARIO("DEST", "destinatario"),
		REDESPACHO("REDE", "redespacho"),
		REMETENTE("REME", "remetente"),
		TOMADOR("TOMA", "tomador");

		private final String ds;
		private final String key;

		ETipoCliente(final String ds, final String key) {
			this.ds = ds;
			this.key = key;
		}

		public boolean equals(final String valor) {
			return ds.equals(valor);
		}

		public String getDs() {
			return ds;
		}

		public String getKey() {
			return key;
		}
	}

	public void setTipoTributacaoIEService(
			TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
		return notaFiscalConhecimentoService;
	}

	public void setNotaFiscalConhecimentoService(final NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public NotaFiscalExpedicaoEDIService getNotaFiscalExpedicaoEDIService() {
		return notaFiscalExpedicaoEDIService;
	}

	public void setNotaFiscalExpedicaoEDIService(final NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService) {
		this.notaFiscalExpedicaoEDIService = notaFiscalExpedicaoEDIService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(final ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	private DivisaoCliente findDivisaoCliente(Cliente cliente,Long idServico,String dsDivisaoCliente) {

		List<DivisaoCliente> divisoes = this.divisaoClienteService.findDivisaoClienteByIdServico(cliente.getIdCliente(), idServico);

		for(DivisaoCliente divisao : divisoes) {

			if(divisao.getDsDivisaoCliente()!=null && divisao.getDsDivisaoCliente().equalsIgnoreCase(dsDivisaoCliente)) {

				return divisao;

			}

		}

		return null;

	}

	private Comparator<DivisaoCliente> getComparatorDivisaoCliente() {

		return new Comparator<DivisaoCliente>() {

			public int compare(DivisaoCliente o1, DivisaoCliente o2) {
				return o1.getCdDivisaoCliente().compareTo(o2.getCdDivisaoCliente());
			}
		};

	}

	private DivisaoCliente obterMenorDivisaoCliente(Cliente cliente,Long idServico) {
		List<DivisaoCliente> divisoes = this.divisaoClienteService.findDivisaoClienteByIdServico(cliente.getIdCliente(), idServico);
		DivisaoCliente divisao = null;
		if(divisoes!=null && !divisoes.isEmpty()) {
			Collections.sort(divisoes,this.getComparatorDivisaoCliente());
			divisao = divisoes.get(0);
		}
		return divisao;
	}

	public void setLogErrosEDIService(LogErrosEDIService logErrosEDIService) {
		this.logErrosEDIService = logErrosEDIService;
	}

	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public Map validateResponsavelEtiquetaPorVolume(final Map criteria) {
		Map result = new HashMap();
		final Long nrNotaFiscalEdi = (Long) criteria.get("idNotaFiscalEdi");
		NotaFiscalEdi notaFiscalEdi = notaFiscalExpedicaoEDIService.findById(nrNotaFiscalEdi);
		String tpDevedorFrete = null;
		Cliente responsavel = null;
		if((notaFiscalEdi.getCnpjTomador() != null) && (notaFiscalEdi.getCnpjTomador().longValue() > 0)) {
			final String nrIdentificacaoTomador = getValidatedNrIdentificacao(String.valueOf(notaFiscalEdi.getCnpjTomador()));
			if(!nrIdentificacaoTomador.equals(criteria.get("nrIdentificacaoRemetente")) && !nrIdentificacaoTomador.equals(criteria.get("nrIdentificacaoDestinatario"))) {
				tpDevedorFrete = ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL;
				responsavel = clienteService.findByNrIdentificacao(nrIdentificacaoTomador);
				result.put("responsavelBlEtiquetaPorVolume", responsavel != null ? (responsavel.getBlEtiquetaPorVolume() != null ? responsavel.getBlEtiquetaPorVolume() : false) : false);
			}
		}
		if (responsavel == null){
			if ("CIF".equals(notaFiscalEdi.getTipoFrete())){
				final String nrIdentificacaoRemetente = getValidatedNrIdentificacao(String.valueOf(notaFiscalEdi.getCnpjReme()));
				responsavel = clienteService.findByNrIdentificacao(nrIdentificacaoRemetente);
				result.put("responsavelBlEtiquetaPorVolume", responsavel != null ? (responsavel.getBlEtiquetaPorVolume() != null ? responsavel.getBlEtiquetaPorVolume() : false) : false);
			}
			else if ("FOB".equals(notaFiscalEdi.getTipoFrete())){
				final String nrIdentificacaoDestinatario = getValidatedNrIdentificacao(String.valueOf(notaFiscalEdi.getCnpjDest()));
				responsavel = clienteService.findByNrIdentificacao(nrIdentificacaoDestinatario);
				result.put("responsavelBlEtiquetaPorVolume", responsavel != null ? (responsavel.getBlEtiquetaPorVolume() != null ? responsavel.getBlEtiquetaPorVolume() : false) : false);
			}
			else{
				result.put("responsavelBlEtiquetaPorVolume", false);
			}
		}
		return result;
	}

	public ResponsavelEtiquetaDto validateResponsavelEtiquetaPorVolume
		(ValidateResponsavelEtiquetaDto validateResponsavelEtiquetaDto) {
		Map<String, Object> criteria = new HashMap<>();

		criteria.put("idNotaFiscalEdi", validateResponsavelEtiquetaDto.getIdNotaFiscalEdi());
		criteria.put("nrIdentificacaoRemetente", validateResponsavelEtiquetaDto.getNrIdentificacaoRemetente());

		Map<String, Object> result = validateResponsavelEtiquetaPorVolume(criteria);
		ResponsavelEtiquetaDto responsavelEtiquetaDto = new ResponsavelEtiquetaDto();
		responsavelEtiquetaDto.setBlEtiquetaPorVolume(BooleanUtils.isFalse((Boolean) result.get("responsavelBlEtiquetaPorVolume")));
		return responsavelEtiquetaDto;
	}

	/**
	 * Busca os dados para montar as etiquetas de embarque dos postos avançados, com base na tabela NOTA_FISCAL_EDI.
	 *
	 *
	 * @param identificador Quando informado, a busca é filtrada por CHAVE_NFE quando tiver 44 caracteres, caso contrario será filtrado por BOX_NUMBER
	 * @param cnpj
	 * @param serieNotaFiscal
	 * @param numeroNotaFiscal
	 * @param nrVolumes Quantidade de volumes informado na tela
	 *
	 * @return List Lista contendo dados de cada etiqueta a ser impressa
	 *
	 * @author Vagner Huzalo
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public Map<String, Object> generateEtiquetasPostoAvancado(String identificador, String cnpj, String serieNotaFiscal, String nrNotaFiscal, Long nrVolumes, BigDecimal psTotal,Boolean atualizaVolumesPesos) {
		String chaveNfe = null;
		String boxNumber = null;

		List<Map<String, Object>> etiquetas = new ArrayList<Map<String,Object>>();


		if (identificador != null && identificador.trim().length() == 44){
			chaveNfe = identificador;
		}else if (!identificador.trim().isEmpty()){
			throw new BusinessException("LMS-28016");
		}else if (StringUtils.isBlank(cnpj) || StringUtils.isBlank(nrNotaFiscal) || StringUtils.isBlank(serieNotaFiscal)){
			throw new BusinessException("LMS-28017");
		}

		NotaFiscalEdi notaFiscalEdi = getNotaFiscalEdiDAO().findByOutpostData(chaveNfe,cnpj,serieNotaFiscal,nrNotaFiscal);
		if (notaFiscalEdi == null){
			throw new BusinessException("LMS-28015");
		}


		if(!atualizaVolumesPesos){
			if(notaFiscalEdi.getQtdeVolumes() == null && notaFiscalEdi.getPesoReal() == null){
				throw new BusinessException("LMS-04551");
			}
			if(notaFiscalEdi.getPesoReal() == null){
				throw new BusinessException("LMS-04550");
			}
			if(notaFiscalEdi.getQtdeVolumes() == null){
				throw new BusinessException("LMS-04029");
			}



		}else{
			if(notaFiscalEdi.getPesoReal() == null){
				if(psTotal == null){
					throw new BusinessException("LMS-04550");
				}
				notaFiscalEdi.setPesoReal(psTotal);
			}
			if(notaFiscalEdi.getQtdeVolumes() == null){
				if(nrVolumes == null){
					throw new BusinessException("LMS-04029");
				}
				notaFiscalEdi.setQtdeVolumes(new BigDecimal(nrVolumes));
			}
		}

		psTotal = notaFiscalEdi.getPesoReal();
		nrVolumes = notaFiscalEdi.getQtdeVolumes().longValue();


		Cliente remetente = clienteService.findByNrIdentificacao(getValidatedNrIdentificacao(notaFiscalEdi.getCnpjReme().toString()));

		if (remetente == null){
			throw new BusinessException("LMS-25013",new Object[]{getValidatedNrIdentificacao(notaFiscalEdi.getCnpjReme().toString())});
		}
		Filial filialOrigem = remetente.getFilialByIdFilialAtendeOperacional();

		Cliente destinatario = clienteService.findByNrIdentificacao(getValidatedNrIdentificacao(notaFiscalEdi.getCnpjDest().toString()));
		if (destinatario == null){
			throw new BusinessException("LMS-28014",new Object[]{getValidatedNrIdentificacao(notaFiscalEdi.getCnpjDest().toString())});
		}
		Filial filialDestino = destinatario.getFilialByIdFilialAtendeOperacional();

		EnderecoPessoa enderecoDestinatario = destinatario.getPessoa().getEnderecoPessoa();

		Short nrRota = ROTA_ZERO;

		RotaIntervaloCep rotaIntervaloCep = rotaColetaEntregaService.findRotaAtendimentoCep(notaFiscalEdi.getCepEnderDest().toString(),
				destinatario.getIdCliente(), enderecoDestinatario.getIdEnderecoPessoa(), filialDestino.getIdFilial(), JTDateTimeUtils.getDataAtual());
		if (rotaIntervaloCep != null){
			nrRota = rotaIntervaloCep.getRotaColetaEntrega().getNrRota();
		}

		Long idServicoRodoviarioConvencional = ((BigDecimal)parametroGeralService.findConteudoByNomeParametro("ID_SERVICO_RODOVIARIO_NACIONAL", false)).longValue();

		FluxoFilial fluxoFilial = mcdService.findFluxoEntreFiliais(filialOrigem.getIdFilial(), filialDestino.getIdFilial(), idServicoRodoviarioConvencional, JTDateTimeUtils.getDataAtual());
		String dsFluxoFilial = fluxoFilial.getDsFluxoFilial();

		notaFiscalEDIVolumeService.removeByIdNotaFiscalEdi(notaFiscalEdi.getIdNotaFiscalEdi());//


		LogEDIDetalhe logEDIDetalhe = logEDIDetalheService.findByCnpjRemeNrNota(notaFiscalEdi.getCnpjReme(), notaFiscalEdi.getNrNotaFiscal());

		for (Long nrSequenciaVolume = 1L; nrSequenciaVolume <= nrVolumes; nrSequenciaVolume++){
			String barCode = extractBarcode(nrVolumes, notaFiscalEdi.getIdNotaFiscalEdi(),Long.valueOf(filialOrigem.getCodFilial()),
					Long.valueOf(filialDestino.getCodFilial()), nrRota, nrSequenciaVolume);

			Map<String,Object> dadosEtiqueta = new HashMap<String, Object>();
			dadosEtiqueta.put("codigoBarras", barCode);
			dadosEtiqueta.put("remetente", remetente.getPessoa().getNmPessoa());
			dadosEtiqueta.put("destinatario", destinatario.getPessoa().getNmPessoa());
			dadosEtiqueta.put("endereco", extractEndereco(enderecoDestinatario));
			dadosEtiqueta.put("bairro", enderecoDestinatario.getDsBairro());
			Municipio municipio = enderecoDestinatario.getMunicipio();
			dadosEtiqueta.put("municipio", municipio.getNmMunicipio());
			UnidadeFederativa unidadeFederativa = municipio.getUnidadeFederativa();
			dadosEtiqueta.put("estado", unidadeFederativa.getSgUnidadeFederativa());
			dadosEtiqueta.put("pais", unidadeFederativa.getPais().getNmPais().getValue());
			dadosEtiqueta.put("cep", enderecoDestinatario.getNrCep());
			dadosEtiqueta.put("dsFluxoFilial", dsFluxoFilial);
			dadosEtiqueta.put("numeroNotaFiscal", notaFiscalEdi.getNrNotaFiscal());
			dadosEtiqueta.put("volumeContagem", nrSequenciaVolume.toString()+"/"+nrVolumes.toString());
			if (!ROTA_ZERO.equals(nrRota)){
				dadosEtiqueta.put("numeroRota", nrRota);
			}
			dadosEtiqueta.put("filialOrigem", fluxoFilial.getFilialByIdFilialOrigem().getSgFilial());
			dadosEtiqueta.put("filialDestino", fluxoFilial.getFilialByIdFilialDestino().getSgFilial());

			dadosEtiqueta.put("nrVolumes", nrVolumes);
			dadosEtiqueta.put("psTotal", psTotal);


			String[] rota = dsFluxoFilial.split(" - ");
			dadosEtiqueta.putAll(extractRoute(rota));

			etiquetas.add(dadosEtiqueta);

			NotaFiscalEdiVolume volume = new NotaFiscalEdiVolume();
			volume.setNotaFiscalEdi(notaFiscalEdi);
			volume.setCodigoVolume(barCode);
			notaFiscalEDIVolumeService.store(volume);

			LogEDIVolume logEDIVolume = new LogEDIVolume();
			logEDIVolume.setLogEDIDetalhe(logEDIDetalhe);
			logEDIVolume.setCodigoVolume(barCode);
			logEDIVolumeService.store(logEDIVolume);

		}

		notaFiscalEdi.setPesoReal(psTotal);
		notaFiscalEdi.setQtdeVolumes(new BigDecimal(nrVolumes));
		this.store(notaFiscalEdi);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("etiquetas", etiquetas);
		return map;
	}

	@SuppressWarnings("rawtypes")
	private Map extractRoute(String[] routeElements){
		Map<String,String> routes = new HashMap<String, String>();
		if (routeElements != null && routeElements.length > 2){
			Long rota = 1L;
			for (int index = 1; index < routeElements.length-1;index++ ){
				routes.put("rota"+rota,routeElements[index]);
				rota++;
			}
		}
		return routes;
	}

	private String extractEndereco(EnderecoPessoa endereco){
		String dsEndereco = endereco.getDsEndereco();
		if (endereco.getNrEndereco() != null && !".".equals(endereco.getNrEndereco().trim()) ){
			dsEndereco = dsEndereco.concat(", ").concat(endereco.getNrEndereco());
		}

		if (endereco.getDsComplemento() != null){
			dsEndereco = dsEndereco.concat(", ").concat(endereco.getDsComplemento());
		}
		return dsEndereco;
	}


	private String extractBarcode(Long nrVolumes, Long idNotaFiscalEdi, Long cdFilialOrigem, Long cdFilialDestino, Short nrRota, Long nrSequenciaVolume) {
		String barCode = "02";
		barCode = barCode.concat(FormatUtils.fillNumberWithZero(cdFilialOrigem.toString(), 3));
		barCode = barCode.concat("4");

		String idNota = idNotaFiscalEdi.toString();
		if (idNota.length() <= 9){
			idNota = FormatUtils.fillNumberWithZero(idNota,9);
		}else{
			idNota  = idNota.substring(idNota.length()-9);
		}
		barCode = barCode.concat(idNota);
		barCode = barCode.concat(FormatUtils.fillNumberWithZero(cdFilialDestino.toString(), 3));
		barCode = barCode.concat(FormatUtils.fillNumberWithZero(nrSequenciaVolume.toString(), 4));
		barCode = barCode.concat(FormatUtils.fillNumberWithZero(nrVolumes.toString(), 4));
		barCode = barCode.concat(FormatUtils.fillNumberWithZero(nrRota.toString(), 3));
		barCode = barCode.concat(FormatUtils.fillNumberWithZero("0000", 4));
		return barCode;
	}

	public void setNotaFiscalEDIVolumeService(NotaFiscalEDIVolumeService notaFiscalEDIVolumeService) {
		this.notaFiscalEDIVolumeService = notaFiscalEDIVolumeService;
	}

	public void setMcdService(McdService mcdService) {
		this.mcdService = mcdService;
	}

	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	public NotaFiscalWrapperDMN executeFindNotaFiscalEdiByUltimoIdImportado(Long idNotaFiscalEdi, String cnpjCliente) {

		List<Object[]> listNotaFiscalEdi = findNotasEdiPorCNPJ(idNotaFiscalEdi, cnpjCliente);

		List<Long> listaNotasIds = new ArrayList<>();
		for (Object[] notaFiscalEdi : listNotaFiscalEdi) {
			listaNotasIds.add((Long) notaFiscalEdi[0]);
		}

		Map<Long, List<NotaFiscalEdiVolume>> mapVolumes = null;
		if (CollectionUtils.isNotEmpty(listaNotasIds)) {
			mapVolumes = getNotaFiscalEdiDAO().findVolumesByNotasFiscaisEdi(listaNotasIds);
		}

		List<NotaFiscalDMN> listaNotas = new ArrayList<>();
		List<NotaFiscalEdiVolume> notaFiscalEdiVolumeLista = executeProcessamentoNotasEGeracaoEtiquetas(listNotaFiscalEdi, listaNotas, mapVolumes);

		notaFiscalEDIVolumeService.storeAll(notaFiscalEdiVolumeLista);

		NotaFiscalWrapperDMN notaFiscalEdiWrapperDMN = new NotaFiscalWrapperDMN();
		notaFiscalEdiWrapperDMN.setNotas(listaNotas);

		return notaFiscalEdiWrapperDMN;
	}

	private List<NotaFiscalEdiVolume> executeProcessamentoNotasEGeracaoEtiquetas(List<Object[]> listNotaFiscalEdi, List<NotaFiscalDMN> listaNotas, Map<Long, List<NotaFiscalEdiVolume>> mapVolumes) {

		Map<String, Short> rotas = new HashMap<String, Short>();
		List<NotaFiscalEdiVolume> notaFiscalEdiVolumeLista = new ArrayList<NotaFiscalEdiVolume>();
		StringBuilder erros = new StringBuilder();
		for (Object[] notaFiscalEdi : listNotaFiscalEdi) {

			String retorno = executeProcessamentoNota(listaNotas, notaFiscalEdiVolumeLista, rotas, mapVolumes, notaFiscalEdi);

			if (retorno != null)  {
				erros.append("\n\n--->");
				erros.append(retorno);
			}
		}

		if (erros.length() > 0) {
			erros.insert(0, "---------------------------------ERROS CARGA POSTO AVANCADO----------------------------------- \n\nSERVIDOR: " + ADSMInitArgs.LMS_ADDRESS.getValue());
			log.error(erros.toString());
			executeEnvioEmail(erros.toString());
		}
		return notaFiscalEdiVolumeLista;
	}

	private void executeEnvioEmail(String erros) {
		String emailErro = (String) configuracoesFacade.getValorParametro("EMAIL_ERRO_CARGA_NOTAS_POSTO_AVANCADO");
		String remetenteLms = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		Mail email = new Mail();
		email.setFrom(remetenteLms);
		email.setTo(emailErro);
		email.setBody(erros);
		email.setSubject("ERROS NAS NOTAS DURANTE A CARGA DO POSTO AVANCADO");

		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, email);
		integracaoJmsService.storeMessage(msg);
	}

	private List<Object[]> findNotasEdiPorCNPJ(Long idNotaFiscalEdi, String cnpjCliente) {
		List<String> cnpjs = Arrays.asList(cnpjCliente.split(";"));
		List<Long> cnpjLongs = new ArrayList<>();
		for (String cnpj : cnpjs) {
			cnpjLongs.add(Long.parseLong(cnpj));
		}

		Long idServicoRodoviarioConvencional = ((BigDecimal)configuracoesFacade.getValorParametro("ID_SERVICO_RODOVIARIO_NACIONAL")).longValue();

		Integer limite = findLimiteConsultaPostoAvancado();

		return getNotaFiscalEdiDAO().findNotaFiscalEdiByUltimoIdImportado(idNotaFiscalEdi, cnpjLongs, idServicoRodoviarioConvencional, limite);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String executeProcessamentoNota(List<NotaFiscalDMN> listaNotas, List<NotaFiscalEdiVolume> notaFiscalEdiVolumeLista,
										   Map<String, Short> rotas, Map<Long, List<NotaFiscalEdiVolume>> mapVolumes, Object[] notaFiscalEdi) {
		Long idNFEdi = (Long) notaFiscalEdi[0];

		List<NotaFiscalEdiVolume> volumesEDI = mapVolumes.get(idNFEdi);
		LogEDIDetalhe logEDIDetalhe = logEDIDetalheService.findByIdNotaFiscalEDI(idNFEdi);
		if (logEDIDetalhe == null) {
			return "idNFEdi:" + idNFEdi + " - erro: Nenhum LOG Edi Detalhe foi encontrado.";
		}

		if (CollectionUtils.isNotEmpty(volumesEDI)) {

			boolean erroFluxo = false;

			try {

				NotaFiscalDMN notaFiscalDMN = new NotaFiscalDMN();

				notaFiscalDMN.setIdNotaFiscalEdi(idNFEdi);
				notaFiscalDMN.setChaveNFE((String) notaFiscalEdi[1]);
				notaFiscalDMN.setCNPJDestinatario((String) notaFiscalEdi[2]);
				notaFiscalDMN.setFluxo((String) notaFiscalEdi[3]);

				if (notaFiscalDMN.getFluxo() == null) {
					erroFluxo = true;
				}

				String cepEndDestino = FormatUtils.fillNumberWithZero(((String) notaFiscalEdi[4]), 8);
				Long idCliente = (Long) notaFiscalEdi[5];
				Long idEnderecoDestinatario = (Long) notaFiscalEdi[6];
				Long idFilialDestino = (Long) notaFiscalEdi[7];
				String sgFilialOrigem = (String) notaFiscalEdi[8];
				String nmRemetente = (String) notaFiscalEdi[9];
				String nmDestinatario = (String) notaFiscalEdi[10];
				String dsEnderecoDestinatario = (String) notaFiscalEdi[11];
				String dsBairroDestinatario = (String) notaFiscalEdi[12];
				String nmMunicipioDestinatario = (String) notaFiscalEdi[13];
				String sgUfDestinatario = (String) notaFiscalEdi[14];
				String nmPaisDestinatario = (String) notaFiscalEdi[15];
				String nrCepDestinatario = (String) notaFiscalEdi[16];
				String sgFilialDestino = (String) notaFiscalEdi[17];
				Long idFilialOrigem = (Long) notaFiscalEdi[18];
				Long cdFilialDestino = (Long) notaFiscalEdi[19];
				Long cdFilialOrigem = (Long) notaFiscalEdi[20];
				String nrNotaFiscal = (String) notaFiscalEdi[21];

				if (anyValueIsEmpty(nmRemetente, nmDestinatario, dsEnderecoDestinatario, dsBairroDestinatario, nmMunicipioDestinatario)){
					StringBuffer logStr = new StringBuffer();
					logStr.append("Erro: [Dados inválidos] NotaFiscalEDIService.getEtiquetaByVolume ->").append(" \n").
							append("Data da Execução: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append(" \n").
							append("idNotaFiscalEdi: ").append(idNFEdi).append(" \n").
							append("nmRemetente: ").append(nmRemetente).append(" \n").
							append("nmDestinatario: ").append(nmDestinatario).append(" \n").
							append("dsEnderecoDestinatario: ").append(dsEnderecoDestinatario).append(" \n").
							append("dsBairroDestinatario: ").append(dsBairroDestinatario).append(" \n").
							append("nmMunicipioDestinatario: ").append(nmMunicipioDestinatario);
					return logStr.toString();
				}

				String key = cepEndDestino + "-" + idCliente.toString() + "-" +
						idEnderecoDestinatario.toString() + "-" + idFilialDestino.toString();

				Short nrRota = ROTA_ZERO;

				if(rotas.containsKey(key)){
					nrRota = rotas.get(key);
				} else {
					RotaIntervaloCep rotaIntervaloCep = rotaColetaEntregaService.findRotaAtendimentoCep(cepEndDestino, idCliente, idEnderecoDestinatario,
							idFilialDestino, JTDateTimeUtils.getDataAtual());
					if (rotaIntervaloCep != null){
						nrRota = rotaIntervaloCep.getRotaColetaEntrega().getNrRota();
					}
					rotas.put(key, nrRota);
				}

				int nrVolume = 0;

				if (CollectionUtils.isNotEmpty(volumesEDI)) {
					for (NotaFiscalEdiVolume notaFiscalEdiVolume: volumesEDI) {

						VolumeDMN notaFiscalVolumeDMN = new VolumeDMN();

						notaFiscalVolumeDMN.setCodigoVolume(notaFiscalEdiVolume.getCodigoVolume());
						notaFiscalVolumeDMN.setIdNotaFiscalEdiVolume(notaFiscalEdiVolume.getIdNotaFiscalEdiVolume());
						notaFiscalVolumeDMN.setSgFilial(sgFilialOrigem);
						nrVolume++;

						EtiquetaDMN etiqueta = getEtiquetaByVolume(notaFiscalDMN.getIdNotaFiscalEdi(), nrNotaFiscal, nrVolume, volumesEDI.size(), nmRemetente, nmDestinatario, dsEnderecoDestinatario,
								dsBairroDestinatario, nmMunicipioDestinatario, sgUfDestinatario, nmPaisDestinatario, nrCepDestinatario, nrRota, notaFiscalDMN.getFluxo(),
								cdFilialOrigem, sgFilialOrigem, cdFilialDestino, sgFilialDestino, notaFiscalEdiVolume.getCodigoVolume());

						notaFiscalVolumeDMN.setEtiqueta(etiqueta);
						notaFiscalDMN.getVolumes().add(notaFiscalVolumeDMN);

						notaFiscalEdiVolume.setCdBarraPostoAvancado(etiqueta.getCodigoBarras());

						try {
							atualizaLogVolume(notaFiscalEdiVolume, etiqueta.getCodigoBarras(),logEDIDetalhe);
						} catch (Exception e) {
							log.error(e);
							return "idNFEdi:" + idNFEdi + " - codigo de barras: " + etiqueta.getCodigoBarras() + " - possivelmente não existe Log EDI Volume - erro: " + e.getMessage() + " - causa: " + e.getCause();
						}
					}

					notaFiscalEdiVolumeLista.addAll(volumesEDI);
				}

				listaNotas.add(notaFiscalDMN);

			} catch (Exception e) {
				log.error(e);
				return "idNFEdi:" + idNFEdi + " - erro: " + e.getMessage() + " - causa: " + e.getCause();
			}
		}
		return null;
	}

	private boolean anyValueIsEmpty(String nmRemetente, String nmDestinatario,
									String dsEnderecoDestinatario, String dsBairroDestinatario,
									String nmMunicipioDestinatario) {
		if(valueIsEmpty(nmRemetente) || valueIsEmpty(nmDestinatario) || valueIsEmpty(dsEnderecoDestinatario) ||
				valueIsEmpty(dsBairroDestinatario) || valueIsEmpty(nmMunicipioDestinatario)){
			return true;
		}
		return false;
	}

	private boolean valueIsEmpty(String desc){
		return ((desc == null || desc.isEmpty()) ? true : false);
	}

	private Integer findLimiteConsultaPostoAvancado() {
		Integer limite = null;
		try {
			limite = Integer.valueOf(configuracoesFacade.getValorParametro("LIMITE_NOTAS_POSTO_AVANCADO").toString());
		} catch (Exception e) {
			limite = 100;
		}
		return limite;
	}

	private void atualizaLogVolume(NotaFiscalEdiVolume notaFiscalEdiVolume,
								   String codigoBarras,LogEDIDetalhe logEDIDetalhe) {
		LogEDIVolume logEDIVolume = logEDIVolumeService.findByCodigoVolume(logEDIDetalhe,notaFiscalEdiVolume.getCodigoVolume());

		logEDIVolume.setCdBarraPostoAvancado(codigoBarras);
		logEDIVolumeService.store(logEDIVolume);

	}

	@SuppressWarnings("static-access")
	public EtiquetaDMN getEtiquetaByVolume(Long idNotaFiscalEdi, String nrNotaFiscal, int nrVolume, int totalVolumes, String nmRemetente, String nmDestinatario, String dsEnderecoDestinatario,
										   String dsBairroDestinatario, String nmMunicipioDestinatario, String sgUfDestinatario, String nmPaisDestinatario, String nrCepDestinatarioShort,
										   Short nrRota, String dsFluxoFilial, Long cdFilialOrigem, String sgFilialOrigem, Long cdFilialDestino, String sgFilialDestino, String codigoVolume){

		EtiquetaDMN etiqueta = new EtiquetaDMN();

		String barcode = extractBarcode((long)totalVolumes, idNotaFiscalEdi, cdFilialOrigem, cdFilialDestino, nrRota, (long)nrVolume);

		etiqueta.setCodigoBarras(barcode);
		etiqueta.setRemetente(this.unAccent(nmRemetente));
		etiqueta.setDestinatario(this.unAccent(nmDestinatario));
		etiqueta.setEndereco(this.unAccent(dsEnderecoDestinatario));
		etiqueta.setBairro(this.unAccent(dsBairroDestinatario));
		etiqueta.setMunicipio(this.unAccent(nmMunicipioDestinatario));
		etiqueta.setEstado(sgUfDestinatario);
		etiqueta.setPais(nmPaisDestinatario);
		etiqueta.setCep(FormatUtils.formatCep("BRA", nrCepDestinatarioShort));
		etiqueta.setDsFluxoFilial(dsFluxoFilial);
		etiqueta.setNumeroNotaFiscal(FormatUtils.fillNumberWithZero(nrNotaFiscal, 8));
		etiqueta.setVolumeContagem(nrVolume +"/" + totalVolumes);
		etiqueta.setCsi(codigoVolume);
		etiqueta.setInfoEtiqueta("#infoEtiqueta#");
		etiqueta.setDataEmissao("#dataEmissao#");
		etiqueta.setTpDocumento("CAE");

		if (!ROTA_ZERO.equals(nrRota)){
			etiqueta.setNumeroRota(nrRota.toString());
		}
		etiqueta.setFilialOrigem(sgFilialOrigem);
		etiqueta.setFilialDestino(sgFilialDestino);

		String[] rota = dsFluxoFilial.split(" - ");

		if(rota.length > 2){
			etiqueta.setRota1(rota[1]);
		}
		if(rota.length > 3){
			etiqueta.setRota2(rota[2]);
		}
		if(rota.length > 4){
			etiqueta.setRota3(rota[3]);
		}
		if(rota.length > 5){
			etiqueta.setRota4(rota[4]);
		}
		if(rota.length > 6){
			etiqueta.setRota5(rota[5]);
		}

		return etiqueta;
	}

	public static String unAccent(String s) {
		//
		// JDK1.5
		//   use sun.text.Normalizer.normalize(s, Normalizer.DECOMP, 0);
		//
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("");
	}

	public void setInformacaoDocServicoService(
			InformacaoDocServicoService informacaoDocServicoService) {
		this.informacaoDocServicoService = informacaoDocServicoService;
	}

	public void setLogEDIDetalheService(LogEDIDetalheService logEDIDetalheService) {
		this.logEDIDetalheService = logEDIDetalheService;
	}
	public void setUnitizacaoService(UnitizacaoService unitizacaoService) {
		this.unitizacaoService = unitizacaoService;
	}

	public void setCceItemService(CCEItemService cceItemService) {
		this.cceItemService = cceItemService;
	}

	public void setLogEDIVolumeService(LogEDIVolumeService logEDIVolumeService) {
		this.logEDIVolumeService = logEDIVolumeService;
	}


	public void setLogEDIComplementoService(
			LogEDIComplementoService logEDIComplementoService) {
		this.logEDIComplementoService = logEDIComplementoService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ValidarEdiDTO preValidarNotaFiscalEDI(ValidarEdiDTO validarEdiDTO, String tpProcessamento){
		List<LogErrosEDI> logsErrosEDI = new ArrayList<>();

		Cliente clienteRemetente = clienteService.findById(validarEdiDTO.getClienteRemetente().getIdCliente());
		NotaFiscalEdi notaFiscalEdi = validarEdiDTO.getNotaFiscalEdi();

		/* Validação 1 - IE_DEST */
		if(!preValidarClienteNotaFiscalEdi(notaFiscalEdi, ETipoCliente.DESTINATARIO)){
			logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.IE_DEST, notaFiscalEdi.getIeDest(), null, null));
		}

		/* Validação 2 - IE_TOMADOR */
		if(notaFiscalEdi.getCnpjTomador() != null && !preValidarClienteNotaFiscalEdi(notaFiscalEdi, ETipoCliente.TOMADOR)){
			logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.IE_TOMADOR, notaFiscalEdi.getIeTomador(), null, null));
		}

		/* Validação 3 - IE_CONSIG */
		if(notaFiscalEdi.getCnpjConsig() != null && !preValidarClienteNotaFiscalEdi(notaFiscalEdi, ETipoCliente.CONSIGNATARIO)){
			logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.IE_CONSIG, notaFiscalEdi.getIeConsig(), null, null));
		}

		/* Validação 4 - IE_REDESP */
		if(notaFiscalEdi.getCnpjRedesp() != null && !preValidarClienteNotaFiscalEdi(notaFiscalEdi, ETipoCliente.REDESPACHO)){
			logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.IE_REDESP, notaFiscalEdi.getIeRedesp(), null, null));
		}

		/* Validação 5 - CEP_ENDER_DEST */
		preValidarCepDestinatarioNotaFiscalEdi(clienteRemetente, notaFiscalEdi, validarEdiDTO, tpProcessamento, logsErrosEDI);
		/* Validação 5 - CEP_ENDER_CONSIG */ //LMSA-3544
		preValidarCepConsignatarioNotaFiscalEdi(clienteRemetente, notaFiscalEdi, validarEdiDTO, tpProcessamento, logsErrosEDI);

		/* Validação 6 - QTDE_VOLUMES */
		if (notaFiscalEdi.getQtdeVolumes() == null || BigDecimal.ZERO.compareTo(notaFiscalEdi.getQtdeVolumes()) == 0) {
			String qtdVolumes = null;
			if (notaFiscalEdi.getQtdeVolumes() != null) {
				qtdVolumes = notaFiscalEdi.getQtdeVolumes().toString();
			}
			logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.QTDE_VOLUMES, qtdVolumes, null, null));
		}

		/* Validação 7 - PESO_REAL */
		if (notaFiscalEdi.getPesoReal() == null || BigDecimal.ZERO.compareTo(notaFiscalEdi.getPesoReal()) == 0) {
			String pesoReal = null;
			if (notaFiscalEdi.getPesoReal() != null) {
				pesoReal = notaFiscalEdi.getPesoReal().toString();
			}
			logsErrosEDI.add(popularLogErrosEdi(validarEdiDTO, tpProcessamento, DsCampoLogErrosEDI.PESO_REAL, pesoReal, null, null));
		}

		if(notaFiscalEdi.getPesoCubado() == null){
			executeLogsCubagemEDI(clienteRemetente, logsErrosEDI, validarEdiDTO, tpProcessamento,null);
		}else if(notaFiscalEdi.getPesoCubado().intValue() == 0){
			executeLogsCubagemEDI(clienteRemetente, logsErrosEDI, validarEdiDTO, tpProcessamento,notaFiscalEdi.getPesoCubado().toString());
		}

		if(!logsErrosEDI.isEmpty()){
			logErrosEDIService.storeAll(logsErrosEDI);
		}

		return validarEdiDTO;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	private Usuario buscarUsuarioPorId(Long idUsuario) {
		if (idUsuario == null) return null;

		return usuarioService.findById(idUsuario);
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public PaisService getPaisService() {
		return paisService;
	}

	private void carregarClientesConhecimento(Conhecimento conhecimento) {
		Cliente cliente = null;
		if (conhecimento.getClienteByIdClienteRedespacho() != null) {
			cliente = clienteService
				.findByIdInitLazyProperties
					(conhecimento.getClienteByIdClienteRedespacho().getIdCliente(), false);
			conhecimento.setClienteByIdClienteRedespacho(cliente);
		}
	}

	public DocumentoPorNotaFiscalDto executeValidatePendenciaAtualizacao(FiltroProcessoPorNotaFiscalDto requestFiltroProcessoPorNotaFiscalDTO) {
		final String nrIdentificacao = requestFiltroProcessoPorNotaFiscalDTO.getNrIdentificacao();
		final Integer nrNotaFiscal = requestFiltroProcessoPorNotaFiscalDTO.getNrNotaFiscalInicial();
		final Long idFilial = requestFiltroProcessoPorNotaFiscalDTO.getIdFilial();
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("nrChave", requestFiltroProcessoPorNotaFiscalDTO.getNrChave());
		criteria.put("nrNotaFiscal", nrNotaFiscal);
		String processadoPor = null;
		final Filial filial = filialService.findById(idFilial);
		SessionContext.set(SessionKey.FILIAL_KEY, filial);

		final NotaFiscalEdi notaFiscalEdi = executeValidatePendenciaAtualizacaoByNrIdentificacaoByNrNotaFiscal
				(nrIdentificacao, nrNotaFiscal, null,idFilial);

		validateNotasConhecimento(criteria, nrIdentificacao, notaFiscalEdi, notaFiscalEdi.getSerieNf());
		SessionContext.remove(SessionKey.FILIAL_KEY);
		return new DocumentoPorNotaFiscalDto
				(
					notaFiscalEdi.getIdNotaFiscalEdi(),
					notaFiscalEdi.getNrNotaFiscal(),
					notaFiscalEdi.getQtdeVolumes(),
					JTDateTimeUtils.getDataHoraAtual(filial).toString(),
					FormatUtils.formatCNPJ(notaFiscalEdi.getCnpjDest().toString()),
					notaFiscalEdi.getNomeDest()
				);
	}

	public List<DocumentoPorNotaFiscalDto> executeValidatePendenciaAtualizacaoIntervaloNota
	(FiltroProcessoPorNotaFiscalDto requestFiltroProcessoPorNotaFiscalDTO) {

		final String nrIdentificacao = requestFiltroProcessoPorNotaFiscalDTO.getNrIdentificacao();
		final Integer nrNotaFiscalInicial = requestFiltroProcessoPorNotaFiscalDTO.getNrNotaFiscalInicial();
		final Integer nrNotaFiscalFinal = requestFiltroProcessoPorNotaFiscalDTO.getNrNotaFiscalFinal();
		final Long idFilial = requestFiltroProcessoPorNotaFiscalDTO.getIdFilial();
		Map<String, Object> criteria = new HashMap<>();
		List<DocumentoPorNotaFiscalDto> documentoPorNotaFiscal = new ArrayList<>();
		criteria.put("nrChave", requestFiltroProcessoPorNotaFiscalDTO.getNrChave());
		final Filial filial = filialService.findById(idFilial);
		SessionContext.set(SessionKey.FILIAL_KEY,filial);

		final List<NotaFiscalEdi> notaFiscalEdiList = executeValidatePendenciaAtualizacaoByNrIdentificacaoByIntervaloNotaFiscal
				(nrIdentificacao, nrNotaFiscalInicial, nrNotaFiscalFinal, idFilial);

		for(NotaFiscalEdi notaFiscalEdi : notaFiscalEdiList) {
			criteria.put("nrNotaFiscal", notaFiscalEdi.getNrNotaFiscal());
			validateNotasConhecimento(criteria, nrIdentificacao, notaFiscalEdi, notaFiscalEdi.getSerieNf());
			documentoPorNotaFiscal.add(new DocumentoPorNotaFiscalDto
					(
							notaFiscalEdi.getIdNotaFiscalEdi(),
							notaFiscalEdi.getNrNotaFiscal(),
							notaFiscalEdi.getQtdeVolumes(),
							JTDateTimeUtils.getDataHoraAtual(filial).toString(),
							FormatUtils.formatCNPJ(notaFiscalEdi.getCnpjDest().toString()),
							notaFiscalEdi.getNomeDest()
					));
		}
		SessionContext.remove(SessionKey.FILIAL_KEY);
		return documentoPorNotaFiscal;
	}
}
