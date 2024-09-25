package com.mercurio.lms.expedicao.edi.model.service;

import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.AEREO_NACIONAL_CONVENCIONAL;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.RODOVIARIO_NACIONAL_CONVENCIONAL;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.TP_PROCESSAMENTO_POR_MANIFESTO_CONSOLIDADO;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import com.mercurio.adsm.core.cache.RecursoMensagemCache;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.service.*;
import com.mercurio.lms.edi.dto.*;
import com.mercurio.lms.expedicao.dto.*;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.expedicao.model.service.*;
import com.mercurio.lms.expedicao.util.NotaFiscalEdiUtils;

import com.mercurio.lms.expedicao.util.nfe.LogErroEdiUtil;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;

import com.mercurio.lms.util.*;
import com.mercurio.lms.util.dto.ErroProcessamentoEdiDto;
import com.mercurio.lms.util.session.SessionKey;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.joda.time.DateTime;
import org.joda.time.base.AbstractDateTime;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.edi.enums.DsCampoLogErrosEDI;
import com.mercurio.lms.edi.model.LogErrosEDI;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiComplemento;
import com.mercurio.lms.edi.model.NotaFiscalEdiVolume;
import com.mercurio.lms.edi.model.service.LogAtualizacaoEDIService;
import com.mercurio.lms.edi.model.service.LogErrosEDIService;
import com.mercurio.lms.edi.model.service.NotaFiscalEDIVolumeService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.expedicao.edi.processarNotaFiscalEDIService"
 */
public class ProcessarNotaFiscalEDIService {

	private static final AbstractDateTime logErrosEDIPorNotaFiscalEdi = null;
	private NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService;
	private NotaFiscalExpedicaoEDIVolumeService notaFiscalExpedicaoEDIVolumeService;
	private NotaFiscalExpedicaoEDIComplementoService notaFiscalExpedicaoEDIComplementoService;
	private NotaFiscalExpedicaoEDIItemService notaFiscalExpedicaoEDIItemService;
	private LogAtualizacaoEDIService logAtualizacaoEDIService;
	private ParametroGeralService parametroGeralService;
	private ConhecimentoNormalService conhecimentoNormalService;
	private LogErrosEDIService logErrosEDIService;
	private UsuarioLMSService usuarioLMSService;
	private ClienteService clienteService;
	private NotaFiscalEDIService notaFiscalEDIService;
	private DoctoServicoService doctoServicoService;
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;
	private PpeService ppeService;
	private PedidoColetaService pedidoColetaService;
	private ServicoService servicoService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private CCEItemService cceItemService;
	private InscricaoEstadualService inscricaoEstadualService;
	private NotaFiscalEDIVolumeService notaFiscalEDIVolumeService;
	private IntegracaoJmsService integracaoJmsService;
	private ProcessarNotasEDICommonService processarNotasEDICommonService;
	private ContingenciaService contingenciaService;
	private ConhecimentoService conhecimentoService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	private ProcessamentoEdiService processamentoEdiService;
	private UsuarioAdsmService usuarioAdsmService;
	private ProcessamentoNotaEdiService processamentoNotaEdiService;
	private ProcessamentoIbEdiService processamentoIbEdiService;

	private final Log log = LogFactory.getLog(ProcessarNotaFiscalEDIService.class);

	public Map<String, Object> executeFiltroNotasFiscaisEdi(final Map<String, Object> criteria) {
		final Long nrProcessamento = notaFiscalExpedicaoEDIService.findNextNrProcessamento();

		final String processarPor = (String) criteria.get("processarPor");
		final Long idCliente = (Long) criteria.get("idCliente");
		final String opcaoProcessamento = (String) criteria.get("opcaoProcessamento");
		final String tpProcessamento = (String) criteria.get("tpProcessamento");
		final Map notasFiscaisProdutosDiferenciados = (Map) criteria.get("notasFiscaisProdutosDiferenciados");

		final Cliente cliente = (Cliente) clienteService.findByIdInitLazyProperties(idCliente, false);
		final List<Integer> nrNotasFiscais = findNumerosNotasEdi(criteria);

		final String tpAgrupamentoEdi = cliente.getTpAgrupamentoEDI() == null ? "N" : cliente.getTpAgrupamentoEDI().getValue();
		final String tpOrdemEmissaoEdi = cliente.getTpOrdemEmissaoEDI() == null ? "I" : cliente.getTpOrdemEmissaoEDI().getValue();

		List<NotaFiscalEdi> listNotaFiscalExpedicaoEdis = null;

		List<NotaFiscalEdi> notasFiscaisEncontradas = findNotasEdi(cliente, nrNotasFiscais, tpAgrupamentoEdi, processarPor);

		for(NotaFiscalEdi nfe : notasFiscaisEncontradas){
			String nfeInformado = this.processarNotasEDICommonService.getChaveNfeFromMap(nfe.getNrNotaFiscal(), (List<String>) criteria.get("chavesNfe"));
			if( nfeInformado != null ){
				nfe.setChaveNfe(nfeInformado);
			}
		}

		if (validateIntervaloEtiqueta(criteria)) {
			listNotaFiscalExpedicaoEdis = executeAgrupamentoClienteSemIntervaloEtiqueta(criteria);
		} else {
			listNotaFiscalExpedicaoEdis = notaFiscalExpedicaoEDIService.findByNrIdentificacaoByTpAgrupamentoEdiByTpOrdemEmissaoEdi(cliente.getPessoa().getNrIdentificacao(), tpAgrupamentoEdi, tpOrdemEmissaoEdi, processarPor, tpProcessamento);
		}

		List<ValidarEdiDTO> listaValidacao = new ArrayList<ValidarEdiDTO>();

		int i = 0;

		// "I" = Digitação
		if ("I".equals(tpOrdemEmissaoEdi) && !opcaoProcessamento.equals(ConstantesExpedicao.EDI_PROCESSAR_AUTOMATICO)) {
			Map<String, String> mapChaveNFEXCae = null;
			final String nrCce = (String) criteria.get("nrCCE");
			if (nrCce != null) {
				List<CCEItem> cces = cceItemService.findByCCE(nrCce);
				mapChaveNFEXCae = this.processarNotasEDICommonService.createMapChaveNFEXCae(cces);
			} else {
				List<String> chavesNotasFiscaisEncontradas = new ArrayList<String>();
				for (final NotaFiscalEdi idNotaFiscalEDI : notasFiscaisEncontradas) {
					chavesNotasFiscaisEncontradas.add(idNotaFiscalEDI.getChaveNfe());
				}
			    List<CCEItem> cces = new ArrayList<>();
				while(chavesNotasFiscaisEncontradas.size() > 999){
					List<String> sublist = new ArrayList<String>(chavesNotasFiscaisEncontradas.subList(0, 999));
					cces.addAll(cceItemService.findByChavesNfe(sublist));
					chavesNotasFiscaisEncontradas.removeAll(sublist);
				}
				cces.addAll(cceItemService.findByChavesNfe(chavesNotasFiscaisEncontradas));
				mapChaveNFEXCae = this.processarNotasEDICommonService.createMapChaveNFEXCae(cces);
			}
			for (final NotaFiscalEdi idNotaFiscalEDI : notasFiscaisEncontradas) {
				NotaFiscalEdi notaFiscalEdi = new NotaFiscalEdi();
				notaFiscalEdi.setIdNotaFiscalEdi(idNotaFiscalEDI.getIdNotaFiscalEdi());
				final int index = listNotaFiscalExpedicaoEdis.indexOf(notaFiscalEdi);
				if (index >= 0) {
					notaFiscalEdi = listNotaFiscalExpedicaoEdis.get(index);
					ValidarEdiDTO dto = new ValidarEdiDTO();
					dto.setClienteRemetente(cliente);
					dto.setNotaFiscalEdi(notaFiscalEdi);
					dto.setNrProcessamento(nrProcessamento);
					dto.setTpProcessamento(tpProcessamento);
					if (mapChaveNFEXCae != null && mapChaveNFEXCae.containsKey(notaFiscalEdi.getChaveNfe())) {
						dto.setCae(mapChaveNFEXCae.get(notaFiscalEdi.getChaveNfe()));
					}
					dto.setIndex(i++);
					listaValidacao.add(dto);
				}
			}
		} else {
			for (final NotaFiscalEdi notaFiscalEdi : listNotaFiscalExpedicaoEdis) {
				if (opcaoProcessamento.equals(ConstantesExpedicao.EDI_PROCESSAR_AUTOMATICO)) {
					ValidarEdiDTO dto = new ValidarEdiDTO();
					dto.setClienteRemetente(cliente);
					dto.setNotaFiscalEdi(notaFiscalEdi);
					dto.setNrProcessamento(nrProcessamento);
					dto.setTpProcessamento(tpProcessamento);
					dto.setIndex(i++);
					listaValidacao.add(dto);
				} else if (opcaoProcessamento.equals(ConstantesExpedicao.EDI_PROCESSAR_NOTAS_FISCAIS)) {
					if (notasFiscaisEncontradas.contains(notaFiscalEdi)) {
						ValidarEdiDTO dto = new ValidarEdiDTO();
						dto.setClienteRemetente(cliente);
						dto.setNotaFiscalEdi(notaFiscalEdi);
						dto.setNrProcessamento(nrProcessamento);
						dto.setTpProcessamento(tpProcessamento);
						dto.setIndex(i++);
						listaValidacao.add(dto);
					}
				}
			}
		}

		//Verifica se o cliente que está sendo processado é um cliente GM,
		//para tanto verificar se o ID do cliente informado para processamento consta no campo
		//PARAMETRO_GERAL.DS_CONTEÚDO para PARAMETRO_GERAL.NM_PARAMETRO = "IDS_GM"
		String dsIdsGM = (String) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_IDS_GM);

		Set<Long> idsGM = new HashSet<Long>();
		try {
			for (String id : dsIdsGM.split(";")) {
				idsGM.add(Long.valueOf(id));
			}
		} catch (Exception e) {
			throw new IllegalStateException("PARAMETRO IDS_GM NAO ESTA NO FORMATO ID;ID;ID;ID");
		}

		List<Long> idsNotaFiscalEdiGM = null;
		if(idsGM.contains(idCliente)){
			idsNotaFiscalEdiGM = new ArrayList<Long>(notasFiscaisEncontradas.size());
			for(NotaFiscalEdi nfEDI : notasFiscaisEncontradas){
				if(notaFiscalExpedicaoEDIVolumeService.existsVolumePendenteGM(nfEDI.getIdNotaFiscalEdi())){
					throw new BusinessException("LMS-04364", new Object[]{nfEDI.getNrNotaFiscal().toString()});
				}
				idsNotaFiscalEdiGM.add(nfEDI.getIdNotaFiscalEdi());
			}
			//LMS-2784
			List<Object[]> carregamentosDiferentes = notaFiscalExpedicaoEDIVolumeService.findIdCarregamentoPlacaERota(idsNotaFiscalEdiGM);
			if(carregamentosDiferentes.size() > 1) {
				// Montando os dados da mensagem
				String veiculo_1 = (carregamentosDiferentes.get(0)[1]).toString();
				String destino_1 = (carregamentosDiferentes.get(0)[2]).toString();
				String veiculo_2 = (carregamentosDiferentes.get(1)[1]).toString();
				String destino_2 = (carregamentosDiferentes.get(1)[2]).toString();

				throw new BusinessException("LMS-04456", new Object[]{veiculo_1, destino_1, veiculo_2, destino_2});
			}
		}

		Integer parametroThreads = notaFiscalExpedicaoEDIService.findParametroGeralThreads();
		if (parametroThreads == null) {
			parametroThreads = 20;
		}

		final List<Long> idsNotasFiscais = new ArrayList<Long>(notasFiscaisEncontradas.size());
		for (NotaFiscalEdi notaFiscalEdi : notasFiscaisEncontradas) {
			idsNotasFiscais.add(notaFiscalEdi.getIdNotaFiscalEdi());
		}

		if(notasFiscaisProdutosDiferenciados != null){
			for (ValidarEdiDTO dto : listaValidacao) {
				Map mapProdutoDiferenciado = (Map) notasFiscaisProdutosDiferenciados.get(dto.getNotaFiscalEdi().getNrNotaFiscal());

				if(mapProdutoDiferenciado != null){
					dto.setIsControladoExercito((Boolean)mapProdutoDiferenciado.get("isControladoExercito"));
					dto.setIsControladoPoliciaCivil((Boolean)mapProdutoDiferenciado.get("isControladoPoliciaCivil"));
					dto.setIsControladoPoliciaFederal((Boolean)mapProdutoDiferenciado.get("isControladoPoliciaFederal"));
					dto.setIsProdutoPerigoso((Boolean)mapProdutoDiferenciado.get("isProdutoPerigoso"));
				}
			}
		}

		final Map<String, Object> mapReturn = new HashMap<String, Object>();
		mapReturn.put("listaValidacao", listaValidacao);
		mapReturn.put("parametroThreads", parametroThreads);
		mapReturn.put("nrProcessamento", nrProcessamento);
		mapReturn.put("listIdsNotasFiscaisEdiInformada", idsNotasFiscais);
		mapReturn.put("listIdNotaFiscalEdi", idsNotasFiscais);
		mapReturn.put("idPedidoColeta", criteria.get("idPedidoColeta"));

		return mapReturn;
	}

	/**
	 * Recarrega os dados da nota fiscal que sofreram alterações na pré-validação do processamento do Edi.
	 * LMS-3831
	 *
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> executeRecarregarListaValicadaoEdi(Map criteria) {
		List<ValidarEdiDTO> listaValidacao = (List<ValidarEdiDTO>) criteria.get("listaValidacao");
		executeRecarregarListaValicadaoEdi(listaValidacao);
		criteria.put("listaValidacao", listaValidacao);
		return criteria;
	}

	public void executeRecarregarListaValicadaoEdi(List<ValidarEdiDTO> listaValidacao) {
		for (ValidarEdiDTO validarEdiDTO : listaValidacao) {
			validarEdiDTO.setNotaFiscalEdi(notaFiscalEDIService.findById(validarEdiDTO.getNotaFiscalEdi().getIdNotaFiscalEdi()));
		}
	}

	public void executeRecarregarListaDadosValicadaoEdi(List<DadosValidacaoEdiDTO> listaValidacao) {
		NotaFiscalEdi notaFiscalEdi = null;
		NotaFiscalEdiUtils notaFiscalEdiUtils = new NotaFiscalEdiUtils();
		for (DadosValidacaoEdiDTO validarEdiDTO : listaValidacao) {
			notaFiscalEdi = notaFiscalEDIService
											.findById(validarEdiDTO.getNotaFiscalEdi().getIdNotaFiscalEdi());
			validarEdiDTO.setNotaFiscalEdi(notaFiscalEdiUtils.toDTO(notaFiscalEdi));
		}
	}

	/**
	 * LMS-3831
	 * @param criteria
	 * @return
	 */
	public Map<String, Object> executePreValidacaoNotaEDI(Map<String, Object> criteria){
		return notaFiscalEDIService.preValidarNotaFiscalEDI(criteria);
	}

	/**
	 * LMSA-6702
	 * @param criteria
	 * @return
	 */
	public Map<String, Object> executePreValidacaoVolumeEDI(Map criteria) {
		return notaFiscalEDIService.preValidarVolumeNotaFiscalEDI(criteria);
	}

	/**
	 * Retorna uma lista de logErrosEDI, de acordo com o nrProcessamento informado, para realizar ajuste manual.
	 *
	 * LMS-3831
	 * @param criteria
	 * @return
	 */
	public List findNotasEDIParaAjuste(TypedFlatMap criteria) {
		return logErrosEDIService.findNotasEDIParaAjuste(criteria);
	}

	/**
	 * LMS-3831
	 * @param criteria
	 * @return
	 */
	public Map<String, Object> executeAjustarNotasEDI(Map<String, Object> criteria) {
		Map<Long, List<LogErrosEDI>> logErrosEDIPorNotaFiscalEdi = storeLogErrosEDI((List<LogErrosEDI>) criteria.get("listaLogErrosEDI"));
		storeNotasFiscaisEdiCorrigidas(logErrosEDIPorNotaFiscalEdi);
		Map<String, Object> retorno = new HashMap<String, Object>();
		return retorno;
	}

	public Map<String, Object> executeAjustarNotasEDI(LogErroDTO logErroDTO) {
		Map<Long, List<LogErrosEDI>> logErrosEDIPorNotaFiscalEdi = storeLogErrosEDI(logErroDTO);
		storeNotasFiscaisEdiCorrigidas(logErrosEDIPorNotaFiscalEdi);
		Map<String, Object> retorno = new HashMap<String, Object>();
		return retorno;
	}

	/**
	 * LMSA-6702
	 * @param criteria
	 * @return
	 */
	public Map<String, Object> executeAjustarVolumesEDI(Map criteria) {
		Map<Long, List<LogErrosEDI>> logErrosEDIPorNotaFiscalEdi = storeLogErrosEDI((List<LogErrosEDI>) criteria.get("listaLogErrosEDI"));
		storeVolumesNotasFiscaisEdiCorrigido(logErrosEDIPorNotaFiscalEdi);

		return new HashMap<String, Object>();
	}

	public Map<String, Object> executeAjustarVolumesEDI(LogErroDTO logErroDTO) {
		Map<Long, List<LogErrosEDI>> logErrosEDIPorNotaFiscalEdi = storeLogErrosEDI(logErroDTO);
		storeVolumesNotasFiscaisEdiCorrigido(logErrosEDIPorNotaFiscalEdi);

		return new HashMap<String, Object>();
	}

	private void storeVolumesNotasFiscaisEdiCorrigido(Map<Long, List<LogErrosEDI>> logErrosEDIPorNotaFiscalEdi) {

		for (Map.Entry<Long, List<LogErrosEDI>> entry : logErrosEDIPorNotaFiscalEdi.entrySet()) {
			List<NotaFiscalEdiVolume> listNfev = new ArrayList<NotaFiscalEdiVolume>();

			for (LogErrosEDI logErrosEDI : entry.getValue()) {
				Long valorCorrigido = LongUtils.getLong(logErrosEDI.getDsValorCorrigido());
				if (LongUtils.hasValue(valorCorrigido)) {
					NotaFiscalEdi notaFiscalEdi = notaFiscalEDIService.findById(entry.getKey());

					int total = IntegerUtils.getInteger(logErrosEDI.getDsValorErrado());
					for (int i = 1; i <= total; i++) {
						listNfev.add(prepareVolumeNotaFiscaEdi(notaFiscalEdi, logErrosEDI, i));
					}
				}
			}

			if (!CollectionUtils.isEmpty(listNfev)) {
				notaFiscalEDIVolumeService.storeAll(listNfev);
			}
		}
	}

	private NotaFiscalEdiVolume prepareVolumeNotaFiscaEdi(NotaFiscalEdi notaFiscalEdi, LogErrosEDI logErrosEDI, Integer sequence) {
		NotaFiscalEdiVolume nfev = new NotaFiscalEdiVolume();

		nfev.setNotaFiscalEdi(logErrosEDI.getNotaFiscalEdi());
		nfev.setNotaFiscalEdi(notaFiscalEdi);
		nfev.setCodigoVolume(prepareCodigoVolume(logErrosEDI.getEtiquetaInicial(), sequence));

		return nfev;
	}

	private String prepareCodigoVolume(Long etiquetaInicial, Integer sequence) {
		Long r = LongUtils.add(etiquetaInicial, sequence.longValue());

		return StringUtils.leftPad(r.toString(), 12, "0");
	}

	/**
	 * Grava os logs com as correções efetuadas pelo usuário, e agrupa os mesmos por notaFiscalEdi.
	 *
	 * LMS-3831
	 * @param listaLogErrosEDI
	 * @return
	 */
	private Map<Long, List<LogErrosEDI>> storeLogErrosEDI(List<LogErrosEDI> listaLogErrosEDI) {
		UsuarioLMS usuario = usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
		return storeLogErrosEDI(listaLogErrosEDI, usuario);
	}

	private Map<Long, List<LogErrosEDI>> storeLogErrosEDI(LogErroDTO logErroDTO) {
		LogErroEdiUtil logErroEdiUtil = new LogErroEdiUtil();
		UsuarioLMS usuario = usuarioLMSService.findById(logErroDTO.getIdUsuario());
		List<LogErrosEDI> listaLogErrosEDI = logErroEdiUtil.toListLogErrosEDI(logErroDTO.getLogErrosEdiDTO());
		return storeLogErrosEDI(listaLogErrosEDI, usuario);
	}

	private Map<Long, List<LogErrosEDI>> storeLogErrosEDI(List<LogErrosEDI> listaLogErrosEDI, UsuarioLMS usuario) {

		Map<Long, List<LogErrosEDI>> logErrosEDIPorNotaFiscalEdi = new HashMap<Long, List<LogErrosEDI>>();
		DateTime dataAtual = JTDateTimeUtils.getDataHoraAtual();

		for (LogErrosEDI logErrosEDI : listaLogErrosEDI) {
			if (logErrosEDI.getDsValorCorrigido() != null && !"".equals(logErrosEDI.getDsValorCorrigido())) {
				logErrosEDI.setUsuario(usuario);
				logErrosEDI.setDhCorrecao(dataAtual);

				Long idNotaFiscalEdi = logErrosEDI.getNotaFiscalEdi().getIdNotaFiscalEdi();
				if (logErrosEDIPorNotaFiscalEdi.containsKey(idNotaFiscalEdi)) {
					logErrosEDIPorNotaFiscalEdi.get(idNotaFiscalEdi).add(logErrosEDI);
				} else {
					List<LogErrosEDI> logs = new ArrayList<LogErrosEDI>();
					logs.add(logErrosEDI);
					logErrosEDIPorNotaFiscalEdi.put(idNotaFiscalEdi, logs);
				}
			}

			/* Seta "null" para evitar futuros erros nas buscas da entity, pois o registro de notaFiscalEdi será excluído no fim do processamento. */
			logErrosEDI.setNotaFiscalEdi(null);
		}

		//Se encontrar para o mesmo ID_NOTA_FISCAL_EDI, registro com o campo DS_CAMPO = ‘METRAGEM_CUBICA’ e DS_CAMPO = ‘PESO_CUBADO’ e DS_VLR_CORRIGIDO diferente de null,
		//apresentar a mensagem LMS-04526, substituindo {0} por LOG_ERROS_EDI. NR_NOTA_FISCAL_EDI, retornando à popup “Ajuste de Informações”.

		for (Map.Entry<Long, List<LogErrosEDI>> entry : logErrosEDIPorNotaFiscalEdi.entrySet()){
			boolean foundErrorMetragemOrPesoCubado = false;
			for(LogErrosEDI logErrosEDI : entry.getValue()){
				if(DsCampoLogErrosEDI.METRAGEM_CUBICA_M3.getDsCampoEntidade().equalsIgnoreCase(logErrosEDI.getDsCampoLogErrosEDI().getDsCampoEntidade())
						|| DsCampoLogErrosEDI.PESO_CUBADO.getDsCampoEntidade().equalsIgnoreCase(logErrosEDI.getDsCampoLogErrosEDI().getDsCampoEntidade())){
					if(foundErrorMetragemOrPesoCubado){
						// TODO: 03/02/2023 verificar se de ve colocar na tabela de processamento para monitoramento
						throw new BusinessException("LMS-04526", new Object[]{logErrosEDI.getNrNotaFiscal().toString()});
					}
					foundErrorMetragemOrPesoCubado = true;
				}
			}
		}

		logErrosEDIService.storeAll(listaLogErrosEDI);
		return logErrosEDIPorNotaFiscalEdi;
	}

	/**
	 * Realiza todas as alterações da mesma nota, de acordo com os possíveis logErrosEdi, para realizar um único update por nota.
	 *
	 * LMS-3831
	 * @param logErrosEDIPorNotaFiscalEdi
	 */
	private void storeNotasFiscaisEdiCorrigidas(Map<Long, List<LogErrosEDI>> logErrosEDIPorNotaFiscalEdi) {
		List<NotaFiscalEdi> notasFiscaisEdiCorrigidas = new ArrayList<NotaFiscalEdi>();

		for (Map.Entry<Long, List<LogErrosEDI>> entry : logErrosEDIPorNotaFiscalEdi.entrySet()) {
			boolean atualizarNota = false;
			NotaFiscalEdi notaFiscalEdi = notaFiscalEDIService.findById(entry.getKey());

			for (LogErrosEDI logErrosEDI : entry.getValue()) {

				if (logErrosEDI.getDsValorCorrigido() != null && !"".equals(logErrosEDI.getDsValorCorrigido())) {
					Object dsValorCorrigido = logErrosEDI.getDsValorCorrigido();
					DsCampoLogErrosEDI dsCampoLogErrosEDI = logErrosEDI.getDsCampoLogErrosEDI();

					if (DsCampoLogErrosEDI.PESO_REAL.equals(dsCampoLogErrosEDI) || DsCampoLogErrosEDI.QTDE_VOLUMES.equals(dsCampoLogErrosEDI)
							|| DsCampoLogErrosEDI.METRAGEM_CUBICA_M3.equals(dsCampoLogErrosEDI) || DsCampoLogErrosEDI.PESO_CUBADO.equals(dsCampoLogErrosEDI)) {
						dsValorCorrigido = ((String) dsValorCorrigido).replace(",", ".");
					}

					ReflectionUtils.setBeanPropertyValue(notaFiscalEdi, dsCampoLogErrosEDI.getDsCampoEntidade(), dsValorCorrigido);

					//Se LOG_ERROS_EDI.DS_CAMPO = ‘METRAGEM_CUBICA
					if(DsCampoLogErrosEDI.METRAGEM_CUBICA_M3.getDsCampoEntidade().equalsIgnoreCase(logErrosEDI.getDsCampoLogErrosEDI().getDsCampoEntidade())){
						//atualizar o campo NOTA_FISCAL_EDI.PESO_CUBADO com nPesoCubado  (valor obtido na conversão de metragem cúbica em peso cubado).
						notaFiscalEdi.setPesoCubado(new BigDecimal(dsValorCorrigido.toString()));
					}

					if(DsCampoLogErrosEDI.PESO_CUBADO.getDsCampoEntidade().equalsIgnoreCase(logErrosEDI.getDsCampoLogErrosEDI().getDsCampoEntidade())){
						//converter a metragem cúbica em peso cubado:
						BigDecimal nPesoCubado = convertPesoCubadoEmMetragemCubica(new BigDecimal(dsValorCorrigido.toString()), notaFiscalEdi);
						notaFiscalEdi.setPesoCubado(nPesoCubado);
					}

					atualizarNota = true;
				}
			}

			/* Somente atualiza as notas que sofreram correções. */
			if (atualizarNota) {
				notasFiscaisEdiCorrigidas.add(notaFiscalEdi);
			}
		}

		if(!notasFiscaisEdiCorrigidas.isEmpty()){
			notaFiscalEDIService.storeAll(notasFiscaisEdiCorrigidas);
		}
	}

	private BigDecimal convertPesoCubadoEmMetragemCubica(BigDecimal vlCorrigido, NotaFiscalEdi notaFiscalEdi) {
		BigDecimal nPesoCubado = null;

		Long cnpjRemetente = notaFiscalEdi.getCnpjReme();
		Long cnpjDestinatario = notaFiscalEdi.getCnpjDest();
		String siglaServico = notaFiscalEdi.getModalFrete() != null ? notaFiscalEdi.getModalFrete() : "R";
		Long idServico = getIdServico(siglaServico, notaFiscalEdi);

		Cliente clienteRemetente = clienteService.findByNumeroIdentificacao(cnpjRemetente.toString());
		Cliente clienteDestinatario = clienteService.findByNumeroIdentificacao(cnpjDestinatario.toString());

		DivisaoCliente divisaoCliente = notaFiscalEDIService.getDivisaoCliente(clienteRemetente, notaFiscalEdi, clienteDestinatario, idServico);

		// Se NR_FATOR_CUBAGEM da divisão do cliente (conforme a regra já existente para este definição no item 10 da Rotina: Processa Notas Fiscais EDI) está preenchido com valor diferente de zero, então:
		if(divisaoCliente != null){
			TabelaDivisaoCliente tabelaDivisaoCliente = tabelaDivisaoClienteService.findTabelaDivisaoCliente(divisaoCliente.getIdDivisaoCliente(), idServico);
			if(BigDecimalUtils.hasValue(tabelaDivisaoCliente != null ? tabelaDivisaoCliente.getNrFatorCubagem() : null)) {
				//nPesoCubado = LOG_ERROS.DS_VLR_CORRIGIDO * NR_FATOR_CUBAGEM
				nPesoCubado = vlCorrigido.divide(tabelaDivisaoCliente.getNrFatorCubagem());
			}else{
				//Senão, utilizar o parâmetro geral conforme o modal da divisão do cliente (conforme a regra já existente para este definição no item 10 da Rotina: Processa Notas Fiscais EDI):
				//nFatorCubagem = “FATOR_CUBAGEM_PADRAO_AEREO” ou “FATOR_CUBAGEM_PADRAO_RODO”
				BigDecimal nFatorCubagem = BigDecimalUtils.getBigDecimal(configuracoesFacade.getValorParametro(("A".equals(siglaServico) ? "FATOR_CUBAGEM_PADRAO_AEREO" : "FATOR_CUBAGEM_PADRAO_RODO")));
				if (BigDecimalUtils.hasValue(nFatorCubagem)) {
					String nFatorCubagemFormated = (nFatorCubagem).toString().replace(",", ".");
					nPesoCubado = vlCorrigido.divide(new BigDecimal(nFatorCubagemFormated));
				}else{
					nPesoCubado = BigDecimal.ZERO;
				}

			}
		}
		return nPesoCubado;
	}

	private Long getIdServico(String siglaServico, NotaFiscalEdi notaFiscalEdi) {
		Servico servico = servicoService.findServicoBySigla("A".equals(siglaServico) ? ConstantesExpedicao.AEREO_NACIONAL_CONVENCIONAL : ConstantesExpedicao.RODOVIARIO_NACIONAL_CONVENCIONAL);
		if(servico != null) {
			return servico.getIdServico();
		}
		return null;
	}

	public Map<String, Object> executeValidacaoEdiItem(final Map<String, Object> criteria){
		Map<String, Object> retorno = new HashMap<>();
		ValidarEdiDTO dto = (ValidarEdiDTO) criteria.get("validarEdiDTO");
		dto.setProcessarPor((String) criteria.get("processarPor"));
		dto.setIdFilial(SessionUtils.getFilialSessao().getIdFilial());
		retorno.put("validado", this.executeValidacaoEdiItem(dto));
		return retorno;
	}

	private List<NotaFiscalEdi> executeAgrupamentoClienteSemIntervaloEtiqueta(final Map<String, Object> criteria) {

		/* Obtem o cliente */
		final Long idClienteRemetente = (Long) criteria.get("idCliente");
		final String processarPor = (String) criteria.get("processarPor");

		final Cliente clienteRemetente = clienteService.findByIdInitLazyProperties(idClienteRemetente, false);

		final List<NotaFiscalEdi> list = (List<NotaFiscalEdi>) criteria.get("listNotasFiscaisEdiInformada");

		final List<NotaFiscalEdi> listNFEDI = new ArrayList<NotaFiscalEdi>();

		NotaFiscalEdi notaFiscalEdi = null;
		for (final NotaFiscalEdi nf : list) {
			notaFiscalEdi = notaFiscalExpedicaoEDIService.findByNrIdentificacaoByNrNotaFiscal(clienteRemetente.getPessoa().getNrIdentificacao(), nf.getNrNotaFiscal(), processarPor);
			notaFiscalEdi.setQtVolumeInformado(nf.getQtVolumeInformado());
			notaFiscalEdi.setNrEtiquetaInicial(nf.getNrEtiquetaInicial());
			notaFiscalEdi.setNrEtiquetaFinal(nf.getNrEtiquetaFinal());
			listNFEDI.add(notaFiscalEdi);
		}

		/* Ordena as notas através do numero do cnpj do destinatario */
		Collections.sort(listNFEDI, new Comparator<NotaFiscalEdi>() {

			public int compare(final NotaFiscalEdi nf1, final NotaFiscalEdi nf2) {
				return nf1.getCnpjDest().compareTo(nf2.getCnpjDest());
			}

		});

		return this.processarNotasEDICommonService.executeFiltroNotas(listNFEDI);
	}

	public Map<String, Object> executePrepararAgruparNotasFiscaisEdi(final Map<String, Object> criteria){
		final Map<String, Object> retorno = new HashMap<String, Object>();
		final Map<String, Object> mapDataHoraNotaFiscal = (HashMap) criteria.get("mapDataHoraNotaFiscal");
		final Long idInscricaoEstadualRemetente = (Long) criteria.get("idInscricaoEstadualRemetente");
		final List<Map> listNumeroEtiquetasEdiInformada = (List) criteria.get("listNotaFiscalNumeroEtiquetasEdiInformada");
		final DateTime dhChegada = (DateTime) criteria.get("dhChegada");
		final Map<String, Object> mapMeioTransporte = (Map) criteria.get("meioTransporte");
		final String opcaoProcessamento = (String) criteria.get("opcaoProcessamento");
		final String tpProcessamento = (String) criteria.get("tpProcessamento");
		final List<ValidarEdiDTO> listaValidacao = (List<ValidarEdiDTO>) criteria.get("listaValidacao");
		final Long idPedidoColeta = (Long) criteria.get("idPedidoColeta");
		final String processarPor = (String) criteria.get("processarPor");
		final Long idDivisaoCliente = (Long) criteria.get("idDivisaoCliente");
		final boolean blOperacaoSpitFire = criteria.get("blOperacaoSpitFire") != null ? (Boolean) criteria.get("blOperacaoSpitFire") : false;
		final Long idFilial = criteria.get("idFilial") != null ? (Long) criteria.get("idFilial") : SessionUtils.getFilialSessao().getIdFilial();

		List<ProcessarEdiDTO> listaDtos = prepararNotasFiscaisEdi(
				idInscricaoEstadualRemetente,
				mapDataHoraNotaFiscal,
				listNumeroEtiquetasEdiInformada,
				listaValidacao,
				opcaoProcessamento,
				mapMeioTransporte,
				dhChegada,idPedidoColeta,
				tpProcessamento,
				processarPor,
				idDivisaoCliente,
				blOperacaoSpitFire,
				idFilial, null);

		retorno.put("listaDtos", listaDtos);

		return retorno;
	}

	private String getCae(List<ValidarEdiDTO> listaValidacao, List<ValidarEdiDTO> agrupamento) {
		for (ValidarEdiDTO val : listaValidacao) {
			for (ValidarEdiDTO notaEdi : agrupamento) {
				if (notaEdi.getNotaFiscalEdi().getIdNotaFiscalEdi().equals(val.getNotaFiscalEdi().getIdNotaFiscalEdi()) && val.getCae() != null) {
					return val.getCae();
				}
			}
		}
		return null;
	}

	public List<ProcessarEdiDTO>prepararNotasFiscaisEdi(ParamAgruparNotasFiscaisDTO panf){
		NotaFiscalEdiUtils notaFiscalEdiUtils = new NotaFiscalEdiUtils();
		if(panf.getListaValidacao() == null || panf.getListaValidacao().isEmpty()) {
			removeRegistrosProcessados(panf.getListIdsNotasFiscaisEdiInformada());
			return new ArrayList<>();
		} else {
			List<ValidarEdiDTO> listaValidacao = notaFiscalEdiUtils.toListValidarEdiDTO(panf.getListaValidacao());
			return prepararNotasFiscaisEdi(panf.getIdInscricaoEstadualRemetente(), panf.getMapDataHoraNotaFiscal(),
						panf.getListNumeroEtiquetasEdiInformada(), listaValidacao,
						panf.getOpcaoProcessamento(), panf.getMapMeioTransporte(), panf.getDhChegada(), panf.getIdPedidoColeta(),
						panf.getTpProcessamento(), panf.getProcessarPor(), panf.getIdDivisaoCliente(), panf.getBlOperacaoSpitFire(), panf.getIdFilial(), panf.getIdUsuario());

		}
	}

	private List<ProcessarEdiDTO> prepararNotasFiscaisEdi(
			final Long idInscricaoEstadualRemetente,
			final Map<String, Object> mapDataHoraNotaFiscal,
			final List<Map> listNumeroEtiquetasEdiInformada,
			final List<ValidarEdiDTO> listaValidacao,
			final String opcaoProcessamento,
			final Map<String, Object> mapMeioTransporte,
			final DateTime dhChegada, final Long idPedidoColeta,
			final String tpProcessamento,
			final String processarPor,
			final Long idDivisaoCliente,
			final boolean blOperacaoSpitFire,
			final Long idFilial,
			final Long idUsuario) {

		if (listaValidacao == null || listaValidacao.isEmpty()) {
			throw new IllegalArgumentException("Deveria ser passada uma lista preenchida");
		}

		ValidarEdiDTO firstEdiDTO = listaValidacao.get(0);
		Long nrProcessamento = firstEdiDTO.getNrProcessamento();
		Cliente clienteRemetente = clienteService.findById(firstEdiDTO.getClienteRemetente().getIdCliente());
		Boolean blClientePostoAvancado = clienteRemetente.getBlClientePostoAvancado();

		if (blClientePostoAvancado == null){
		    blClientePostoAvancado = Boolean.FALSE;
		}

		List<ProcessarEdiDTO> listProcessar = new ArrayList<>();

		final List<ValidarEdiDTO> listNotasDtoASeremAgrupadas = new ArrayList<>();

		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("opcaoProcessamento", opcaoProcessamento);
		parameters.put("mapDataHoraNotaFiscal", mapDataHoraNotaFiscal);
		parameters.put("idPedidoColeta", idPedidoColeta);

		// ordena pela ordem definida antes da validação
		listaValidacao.sort(Comparator.comparingInt(ValidarEdiDTO::getIndex));

		List<ValidarEdiDTO> validacoesAux = new ArrayList<>();
		validacoesAux.addAll(listaValidacao);

		final String tpAgrupamentoEdi = clienteRemetente.getTpAgrupamentoEDI() == null ? "N" : clienteRemetente.getTpAgrupamentoEDI().getValue();

		Boolean buildMapConhecimento = null;
		if (!tpProcessamento.equals(TP_PROCESSAMENTO_POR_MANIFESTO_CONSOLIDADO) &&
				("N".equals(tpAgrupamentoEdi) || (tpAgrupamentoEdi == null)
				|| ("E".equals(tpAgrupamentoEdi) && clienteRemetente.getInformacaoDoctoCliente() == null))) {

			for (final ValidarEdiDTO dtoValidacao : listaValidacao) {
				buildMapConhecimento = Boolean.TRUE;
				NotaFiscalEdi notaFiscalEdiAProcessar = dtoValidacao.getNotaFiscalEdi();

				listNotasDtoASeremAgrupadas.clear();
				listNotasDtoASeremAgrupadas.add(dtoValidacao);

				if (!validateQuantidadePeso(listNotasDtoASeremAgrupadas) && !blClientePostoAvancado) {
					buildMapConhecimento = Boolean.FALSE;
					storeLogNotas(listNotasDtoASeremAgrupadas, nrProcessamento, clienteRemetente, "LMS-04369");
				}

				if(!executeValidacoesParaBloqueioValores(listNotasDtoASeremAgrupadas, nrProcessamento, clienteRemetente, idFilial)){
					buildMapConhecimento = Boolean.FALSE;
				}

				if(buildMapConhecimento){
					Map<String, Object> parametersItens = new HashMap<>(parameters);

					Cliente clienteRemetenteDocto = clienteService.findByNrIdentificacao(notaFiscalEDIService.getValidatedNrIdentificacao(listNotasDtoASeremAgrupadas.get(0).getNotaFiscalEdi().getCnpjReme().toString()));
					parametersItens.putAll(notaFiscalEDIService.executeBuildMapConhecimento(clienteRemetenteDocto, idInscricaoEstadualRemetente,
							notaFiscalEdiAProcessar, opcaoProcessamento, mapMeioTransporte, dhChegada, listNumeroEtiquetasEdiInformada,
							listNotasDtoASeremAgrupadas, dtoValidacao.getCae(), nrProcessamento, tpProcessamento, processarPor, idDivisaoCliente,
							blOperacaoSpitFire));

					ProcessarEdiDTO dto = new ProcessarEdiDTO();
					dto.setClienteRemetente(clienteRemetente);
					dto.setNrProcessamento(nrProcessamento);
					dto.setParameters(parametersItens);
					listProcessar.add(dto);
				}
			}
		} else if ("P".equals(tpAgrupamentoEdi)) {

			while (!listaValidacao.isEmpty()) {
				listNotasDtoASeremAgrupadas.clear();

				final NotaFiscalEdi notaFiscalEdi = listaValidacao.get(0).getNotaFiscalEdi();
				final Long cnpjDest = notaFiscalEdi.getCnpjDest();
				final String tipoFrete = notaFiscalEdi.getTipoFrete();
				final String modalFrete = notaFiscalEdi.getModalFrete() != null ? notaFiscalEdi.getModalFrete() : "R";

				List<ValidarEdiDTO> listaARemover = new ArrayList<>();

				for (final ValidarEdiDTO validacaoDto : listaValidacao) {
					String modalFreteDto = validacaoDto.getNotaFiscalEdi().getModalFrete() != null ? validacaoDto.getNotaFiscalEdi().getModalFrete() : "R";

					if (validacaoDto.getNotaFiscalEdi().getCnpjDest().equals(cnpjDest) &&
							validacaoDto.getNotaFiscalEdi().getTipoFrete().equals(tipoFrete) &&
							modalFreteDto.equals(modalFrete)){
						listNotasDtoASeremAgrupadas.add(validacaoDto);
						listaARemover.add(validacaoDto);
					}
				}
				listaValidacao.removeAll(listaARemover);

				List<List<ValidarEdiDTO>> agrupamentos = splitAgrupamentos(notaFiscalEdi, listNotasDtoASeremAgrupadas, processarPor, idFilial, clienteRemetente);
				for (List<ValidarEdiDTO> agrupamento : agrupamentos) {
					buildMapConhecimento = Boolean.TRUE;

					String nrCae = getCae(validacoesAux, agrupamento);
					if (!validateQuantidadePeso(agrupamento) && !blClientePostoAvancado) {
						buildMapConhecimento = Boolean.FALSE;
						storeLogNotas(agrupamento, nrProcessamento, clienteRemetente, "LMS-04369");
					}

					if(!executeValidacoesParaBloqueioValores(listNotasDtoASeremAgrupadas, nrProcessamento, clienteRemetente, idFilial)){
						buildMapConhecimento = Boolean.FALSE;
					}

					if(buildMapConhecimento){
						Map<String, Object> parametersItens = new HashMap<>(parameters);

						Cliente clienteRemetenteDocto = clienteService.findByNrIdentificacao(
								notaFiscalEDIService.getValidatedNrIdentificacao(agrupamento.get(0).getNotaFiscalEdi().getCnpjReme().toString()));

						Map<String, Object> parametrosConhecimento = notaFiscalEDIService.executeBuildMapConhecimento(clienteRemetenteDocto,
								idInscricaoEstadualRemetente, notaFiscalEdi, opcaoProcessamento, mapMeioTransporte, dhChegada, listNumeroEtiquetasEdiInformada,
								agrupamento, nrCae, nrProcessamento, tpProcessamento, processarPor, idDivisaoCliente,
								blOperacaoSpitFire);

						parametersItens.putAll(parametrosConhecimento);

						ProcessarEdiDTO dto = new ProcessarEdiDTO();
						dto.setClienteRemetente(clienteRemetente);
						dto.setNrProcessamento(nrProcessamento);
						dto.setParameters(parametersItens);
						listProcessar.add(dto);
					}

				}
			}

		} else if ("C".equals(tpAgrupamentoEdi)) { // Agrupando destinatarios e
													// sequenciaAgrupamento

			while (!listaValidacao.isEmpty()) {
				listNotasDtoASeremAgrupadas.clear();

				final NotaFiscalEdi notaFiscalEdi = listaValidacao.get(0).getNotaFiscalEdi();
				final Long cnpjDest = notaFiscalEdi.getCnpjDest();
				Long agrupador = null;

				List<ValidarEdiDTO> listaARemover = new ArrayList<ValidarEdiDTO>();

				for (final ValidarEdiDTO validacaoDTO : listaValidacao) {
					if ((validacaoDTO.getNotaFiscalEdi().getSequenciaAgrupamento() == null) && listNotasDtoASeremAgrupadas.isEmpty()) {
						listNotasDtoASeremAgrupadas.add(validacaoDTO);
						listaARemover.add(validacaoDTO);
						break;
					} else if (validacaoDTO.getNotaFiscalEdi().getCnpjDest().equals(cnpjDest)) {
						if ((agrupador == null) || agrupador.equals(validacaoDTO.getNotaFiscalEdi().getSequenciaAgrupamento())) {
							listNotasDtoASeremAgrupadas.add(validacaoDTO);
							listaARemover.add(validacaoDTO);
						} else {
							continue;
						}
					} else {
						continue;
					}

					agrupador = validacaoDTO.getNotaFiscalEdi().getSequenciaAgrupamento();
				}
				listaValidacao.removeAll(listaARemover);

				List<List<ValidarEdiDTO>> agrupamentos = splitAgrupamentos(notaFiscalEdi, listNotasDtoASeremAgrupadas, processarPor, idFilial, clienteRemetente);

				for (List<ValidarEdiDTO> agrupamento : agrupamentos) {
					buildMapConhecimento = Boolean.TRUE;
					boolean isStore = true;

					String nrCae = getCae(validacoesAux, agrupamento);

					if (!validateQuantidadePeso(agrupamento)  && !blClientePostoAvancado) {
						buildMapConhecimento = Boolean.FALSE;
						storeLogNotas(agrupamento, nrProcessamento, clienteRemetente, "LMS-04369");
					}

					if(!executeValidacoesParaBloqueioValores(listNotasDtoASeremAgrupadas, nrProcessamento, clienteRemetente, idFilial)){
						buildMapConhecimento = Boolean.FALSE;
					}

					if(buildMapConhecimento){
						Map<String, Object> parametersItens = new HashMap<>(parameters);

						Cliente clienteRemetenteDocto = clienteService.findByNrIdentificacao(
								notaFiscalEDIService.getValidatedNrIdentificacao(agrupamento.get(0).getNotaFiscalEdi().getCnpjReme().toString()));

						parametersItens.putAll(notaFiscalEDIService.executeBuildMapConhecimento(clienteRemetenteDocto, idInscricaoEstadualRemetente, notaFiscalEdi,
								opcaoProcessamento, mapMeioTransporte, dhChegada, listNumeroEtiquetasEdiInformada, agrupamento, nrCae, nrProcessamento, tpProcessamento, processarPor, idDivisaoCliente,
								blOperacaoSpitFire));

						ProcessarEdiDTO dto = new ProcessarEdiDTO();
						dto.setClienteRemetente(clienteRemetente);
						dto.setNrProcessamento(nrProcessamento);
						dto.setParameters(parametersItens);

						if (isStore) {
							listProcessar.add(dto);
						}
					}
				}
			}
		} else if ("E".equals(tpAgrupamentoEdi)) {
			while (!listaValidacao.isEmpty()) {
				listNotasDtoASeremAgrupadas.clear();

				final NotaFiscalEdi notaFiscalEdi = listaValidacao.get(0).getNotaFiscalEdi();
				final Long cnpjDest = notaFiscalEdi.getCnpjDest();
				final String modalFrete = notaFiscalEdi.getModalFrete() != null ? notaFiscalEdi.getModalFrete() : "R";
				String agrupador = null;

				boolean isFirstAgrupado = true;

				List<ValidarEdiDTO> listaARemover = new ArrayList<>();

				for (final ValidarEdiDTO validacaoDTO : listaValidacao) {

					String modalFreteDto = validacaoDTO.getNotaFiscalEdi().getModalFrete() != null ? validacaoDTO.getNotaFiscalEdi().getModalFrete() : "R";

					Long indcIdInformacaoDoctoClien = null;
					if (tpProcessamento.equals(TP_PROCESSAMENTO_POR_MANIFESTO_CONSOLIDADO)) {
						indcIdInformacaoDoctoClien = clienteRemetente.getInformacaoDoctoClienteConsolidado().getIdInformacaoDoctoCliente();
					} else {
						indcIdInformacaoDoctoClien = clienteRemetente.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente();
					}
					List<NotaFiscalEdiComplemento> nfecList = notaFiscalExpedicaoEDIComplementoService.findByIdNotaFiscalEdiIndcIdInformacaoDoctoClien(
							validacaoDTO.getNotaFiscalEdi().getIdNotaFiscalEdi(), indcIdInformacaoDoctoClien);
					String agrupamentoItem;
					if (nfecList == null || nfecList.isEmpty() || (isLongNumber(nfecList.get(0).getValorComplemento()) && Long.parseLong(nfecList.get(0).getValorComplemento()) == 0)) {
						agrupamentoItem = null;
					} else {
						agrupamentoItem = nfecList.get(0).getValorComplemento();
					}

					if (validacaoDTO.getNotaFiscalEdi().getCnpjDest().equals(cnpjDest) && modalFreteDto.equals(modalFrete)) {
						if(agrupamentoItem == null){
							prepararNota(idInscricaoEstadualRemetente, listNumeroEtiquetasEdiInformada, opcaoProcessamento,
									mapMeioTransporte, dhChegada, nrProcessamento, clienteRemetente, listProcessar,
									Arrays.asList(validacaoDTO), parameters, validacaoDTO.getNotaFiscalEdi(), validacaoDTO.getCae(),
									tpProcessamento, processarPor, idDivisaoCliente, blOperacaoSpitFire, idFilial, idUsuario);
							listaARemover.add(validacaoDTO);
							continue;
						}

						if (isFirstAgrupado) {
							isFirstAgrupado = false;
							agrupador = agrupamentoItem;
							listNotasDtoASeremAgrupadas.add(validacaoDTO);
							listaARemover.add(validacaoDTO);
							continue;
						}
						if(agrupador != null && agrupamentoItem != null && agrupador.equals(agrupamentoItem)){
							listNotasDtoASeremAgrupadas.add(validacaoDTO);
							listaARemover.add(validacaoDTO);
						}
					} else {
						continue;
					}

				}

				listaValidacao.removeAll(listaARemover);

				List<List<ValidarEdiDTO>> agrupamentos = splitAgrupamentos(notaFiscalEdi, listNotasDtoASeremAgrupadas, processarPor, idFilial, clienteRemetente);

				for (List<ValidarEdiDTO> agrupamento : agrupamentos) {
					buildMapConhecimento = Boolean.TRUE;

					String nrCae = getCae(validacoesAux, agrupamento);

					if(listNotasDtoASeremAgrupadas.isEmpty()){
						continue;
					}

					if (!validateQuantidadePeso(listNotasDtoASeremAgrupadas)  && !blClientePostoAvancado) {
						buildMapConhecimento = Boolean.FALSE;
						storeLogNotas(agrupamento, nrProcessamento, clienteRemetente, "LMS-04369");
					}

					if(!executeValidacoesParaBloqueioValores(listNotasDtoASeremAgrupadas, nrProcessamento, clienteRemetente, idFilial)){
						buildMapConhecimento = Boolean.FALSE;
					}

					if(buildMapConhecimento){
						Map<String, Object> parametersItens = new HashMap<String, Object>(parameters);
						Cliente clienteRemetenteDocto = clienteService.findByNrIdentificacao(
								notaFiscalEDIService.getValidatedNrIdentificacao(agrupamento.get(0).getNotaFiscalEdi().getCnpjReme().toString()));

						parametersItens.putAll(notaFiscalEDIService.executeBuildMapConhecimento(clienteRemetenteDocto, idInscricaoEstadualRemetente, notaFiscalEdi,
								opcaoProcessamento, mapMeioTransporte, dhChegada, listNumeroEtiquetasEdiInformada, agrupamento, nrCae, nrProcessamento, tpProcessamento, processarPor, idDivisaoCliente,
								blOperacaoSpitFire, idFilial, idUsuario));

						ProcessarEdiDTO dto = new ProcessarEdiDTO();
						dto.setClienteRemetente(clienteRemetente);
						dto.setNrProcessamento(nrProcessamento);
						dto.setParameters(parametersItens);
						listProcessar.add(dto);
					}
				}

			}
		}

		return listProcessar;
	}

	private void prepararNota(Long idInscricaoEstadualRemetente, List<Map> listNumeroEtiquetasEdiInformada, String opcaoProcessamento,
			Map<String, Object> mapMeioTransporte, DateTime dhChegada, Long nrProcessamento, Cliente clienteRemetente, List<ProcessarEdiDTO> listProcessar,
			List<ValidarEdiDTO> listNotasDtoASeremAgrupadas, Map<String, Object> parameters, NotaFiscalEdi notaFiscalEdiAProcessar, String nrCae, String tpProcessamento,
			String processarPor, final Long idDivisaoCliente, final boolean blOperacaoSpitFire, Long idFilial, Long idUsuario) {

		Boolean buildMapConhecimento = Boolean.TRUE;

		Boolean blClientePostoAvancado = clienteRemetente.getBlClientePostoAvancado();
        if (blClientePostoAvancado == null){
            blClientePostoAvancado = Boolean.FALSE;
        }

		if (!validateQuantidadePeso(listNotasDtoASeremAgrupadas)  && !blClientePostoAvancado) {
			buildMapConhecimento = Boolean.FALSE;
			storeLogNotas(listNotasDtoASeremAgrupadas, nrProcessamento, clienteRemetente, "LMS-04369");
		}

		if(!executeValidacoesParaBloqueioValores(listNotasDtoASeremAgrupadas, nrProcessamento, clienteRemetente, idFilial)){
			buildMapConhecimento = Boolean.FALSE;
		}

		if(buildMapConhecimento){
			Map<String, Object> parametersItens = new HashMap<String, Object>(parameters);
			Cliente clienteRemetenteDocto = clienteService.findByNrIdentificacao(
					notaFiscalEDIService.getValidatedNrIdentificacao(listNotasDtoASeremAgrupadas.get(0).getNotaFiscalEdi().getCnpjReme().toString()));

			parametersItens.putAll(notaFiscalEDIService.executeBuildMapConhecimento(clienteRemetenteDocto, idInscricaoEstadualRemetente,
					notaFiscalEdiAProcessar, opcaoProcessamento, mapMeioTransporte, dhChegada, listNumeroEtiquetasEdiInformada,
					listNotasDtoASeremAgrupadas, nrCae, nrProcessamento, tpProcessamento, processarPor, idDivisaoCliente,
					blOperacaoSpitFire, idFilial, idUsuario));

			ProcessarEdiDTO dto = new ProcessarEdiDTO();
			dto.setClienteRemetente(clienteRemetente);
			dto.setNrProcessamento(nrProcessamento);
			dto.setParameters(parametersItens);
			listProcessar.add(dto);
		}
	}

	private boolean isLongNumber(final String num) {
		try {
			Long.parseLong(num);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private List<List<ValidarEdiDTO>> splitAgrupamentos(NotaFiscalEdi notaFiscalEdi, List<ValidarEdiDTO> dtos, String processarPor, Long idFilial, Cliente clienteRemetente) {
		final String nrIdentificacaoDest = notaFiscalEDIService.getValidatedNrIdentificacao(notaFiscalEdi.getCnpjDest().toString());

		final Cliente clienteDestinatario = clienteService.findByNrIdentificacao(nrIdentificacaoDest);

		String siglaServico = notaFiscalEdi.getModalFrete() != null ? notaFiscalEdi.getModalFrete() : "R";
		boolean isAereo = "A".equals(siglaServico);
		siglaServico = isAereo ? AEREO_NACIONAL_CONVENCIONAL : RODOVIARIO_NACIONAL_CONVENCIONAL;
		final Long idServico = notaFiscalEDIService.findIdServicoBySigla(siglaServico);

		final Municipio municipioDest = notaFiscalEDIService.findMunicipioDestinatario(notaFiscalEdi, clienteDestinatario, processarPor);

		final Long idFilialDestinatario = ppeService.findFilialAtendimentoMunicipio(municipioDest.getIdMunicipio(), idServico,
				Boolean.FALSE, JTDateTimeUtils.getDataAtual(), String.valueOf(municipioDest.getNrCep()),
				clienteDestinatario != null ? clienteDestinatario.getIdCliente() : null, null, null, null, null, null);

		int maxNotas = filialService.findMaxNotasCtrcOrigemDestino(idFilialDestinatario, idFilial);
		List<List<ValidarEdiDTO>> result = new ArrayList<List<ValidarEdiDTO>>();

		BigDecimal valorLimite = isAereo ? clienteRemetente.getVlLimiteValorMercadoriaCteAereo() : clienteRemetente.getVlLimiteValorMercadoriaCteRodo();
		boolean agrupamentoPadrao = clienteRemetente.getTpAgrupamentoEDI() != null && clienteRemetente.getTpAgrupamentoEDI().getValue().equals("P");
		if (maxNotas > 0 && maxNotas < dtos.size()) {
			if(valorLimite != null && agrupamentoPadrao){
				result = agruparValorLimiteMercadoria(dtos, valorLimite, clienteRemetente, maxNotas);
			} else {
				final List<ValidarEdiDTO> notasRecebidas = new ArrayList<ValidarEdiDTO>(dtos);
				while (!notasRecebidas.isEmpty()) {
					int length = Math.min(maxNotas, notasRecebidas.size());
					List<ValidarEdiDTO> grupo = new ArrayList<ValidarEdiDTO>(length);
					for (int i = 0; i < length; i++) {
						grupo.add(notasRecebidas.remove(0));
					}
					result.add(grupo);
				}
			}
		}
		if (result.isEmpty()) {
			if(valorLimite != null && agrupamentoPadrao ){
				result = agruparValorLimiteMercadoria(dtos, valorLimite, clienteRemetente, maxNotas);
			} else {
				result.add(dtos);
			}
		}
		return result;
	}

	private List<List<ValidarEdiDTO>> agruparValorLimiteMercadoria(List<ValidarEdiDTO> dtos, BigDecimal valorLimite, Cliente clienteRemetente,  int maxNotas){
		final List<List<ValidarEdiDTO>> result = new ArrayList<>();
		if(valorLimite == null){
			return null;
		}
		Collections.sort(dtos, Comparator.comparing(v -> v.getNotaFiscalEdi().getVlrTotalMerc()));
		List<ValidarEdiDTO> grupo = new ArrayList<>();
		result.add(grupo);
		BigDecimal valorLista = BigDecimal.ZERO;

		for(ValidarEdiDTO edi : dtos){
			BigDecimal valorMercadoria = edi.getNotaFiscalEdi().getVlrTotalMerc();
			if(valorMercadoria.doubleValue() > valorLimite.doubleValue()){
				BusinessException e = new BusinessException("LMS-28023");
				logAtualizacaoEDIService.storeLog(edi.getNotaFiscalEdi().getNrNotaFiscal(), edi.getNrProcessamento(), clienteRemetente, null, e);
				continue;
			}

			if(valorLista.add(valorMercadoria).doubleValue() >= valorLimite.doubleValue() || grupo.size() == maxNotas){
				grupo = new ArrayList<>();
				result.add(grupo);
			}
			grupo.add(edi);
			valorLista = valorLista.add(valorMercadoria);
		}

		return result;
	}

	/**
	 * LMS-3281: Verifica se o valor da mercadoria, do conhecimento que será gerado, está
	 * dentro do limite permitido. Caso não esteja sinaliza que o conhecimento
	 * não deve ser criado e grava erros na tabela de log.
	 *
	 * @param dtos
	 * @param nrProcessamento
	 * @param clienteRemetente
	 * @return
	 */
	private Boolean executeValidacoesParaBloqueioValores(List<ValidarEdiDTO> dtos, Long nrProcessamento, Cliente clienteRemetente, Long idFilialParametro) {
		BigDecimal nTotVlMercadoria = BigDecimal.ZERO;

		for (ValidarEdiDTO dto : dtos) {
			if(dto.getNotaFiscalEdi().getVlrTotalMerc() != null){
				nTotVlMercadoria = nTotVlMercadoria.add(dto.getNotaFiscalEdi().getVlrTotalMerc());
			}
		}

		TypedFlatMap map = new TypedFlatMap();
		map.put("lancarExcecao", false);
		map.put("buscarMsgValidacao", true);
		map.put("vlMercadoria", nTotVlMercadoria);
		map = doctoServicoService.executeValidacaoLimiteValorMercadoria(map, idFilialParametro);

		if(!map.getBoolean("vlValido")){
			for (ValidarEdiDTO validarDto : dtos) {
				logAtualizacaoEDIService.storeLog(validarDto.getNotaFiscalEdi().getNrNotaFiscal(), nrProcessamento, clienteRemetente, null, map.getString("chaveMsgValidacao")
						+ " - " + map.getString("msgValidacao"));
			}
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	private boolean validateQuantidadePeso(List<ValidarEdiDTO> dtos) {
		BigDecimal pesoTotal = BigDecimal.ZERO;
		BigDecimal volumesTotal = BigDecimal.ZERO;
		for (ValidarEdiDTO dto : dtos) {
			if (dto.getNotaFiscalEdi().getPesoReal() != null) {
				pesoTotal = pesoTotal.add(dto.getNotaFiscalEdi().getPesoReal());
			}
			if (dto.getNotaFiscalEdi().getQtdeVolumes() != null) {
				volumesTotal = volumesTotal.add(dto.getNotaFiscalEdi().getQtdeVolumes());
			}

			if (pesoTotal.compareTo(BigDecimal.ZERO) > 0 && volumesTotal.compareTo(BigDecimal.ZERO) > 0) {
				return true;
			}
		}
		return false;
	}

	private void storeLogNotas(List<ValidarEdiDTO> validarDtos, Long nrProcessamento, Cliente cliente, String error) {
		BusinessException e = new BusinessException(error);
		for (ValidarEdiDTO validarDto : validarDtos) {
			logAtualizacaoEDIService.storeLog(validarDto.getNotaFiscalEdi().getNrNotaFiscal(), nrProcessamento, cliente, null, e);
		}
	}

	public Map<String, Object> executeProcessarNotasFiscaisEdiItem(final Map<String, Object> mapEntrada) {
		final ProcessarEdiDTO item = (ProcessarEdiDTO) mapEntrada.get("item");
		return executeProcessarNotasFiscaisEdiItem(item);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void tratarErroProcessarNotasFiscaisEdiItem
	(ErroProcessarNotasFiscaisEdiItemException exception) {

		ProcessaNotasEdiItemDto item = exception.getProcessaNotasEdiItemDto();
		Throwable throwable = null;
		String mensagem = null;
		if (exception.getThrowable() instanceof BusinessException) {
			BusinessException be = (BusinessException)exception.getThrowable();
			mensagem = MessageFormat.format(montarMessagemErroExcessao(be), be.getMessageArguments());
			throwable = lancarBusinessException(item.getNrProcessamento(), item.getIdProcessamentoEdi(), be);
		} else {
			throwable = new Exception("erro não tratado...");

		}
		sendMessageQueueErro
			(
				exception.getNotaFiscalConhecimentoDtos(), mensagem, throwable, exception.getCliente(),
				item.getIdProcessamentoEdi(), item.getNrProcessamento()
			);
		enviarListaPacialParaRemover(item);
		processamentoIbEdiService.reenviarProcessamentoNotaEdiItem(item);

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeProcessarNotasFiscaisEdiItem(ProcessaNotasEdiItemDto item) throws ErroProcessarNotasFiscaisEdiItemException {
		List<NotaFiscalConhecimentoDto> notaFiscalConhecimentoDtos = item.getParameters().getNotaFiscalConhecimento();
		Cliente cliente = this.clienteService.findById(item.getClienteRemetente().getIdCliente());

		try {
			this.notaFiscalEDIService.executeNotasFiscaisEdi(item, cliente);
		} catch (BusinessException be) {
			throw new ErroProcessarNotasFiscaisEdiItemException(notaFiscalConhecimentoDtos, cliente, item, be);

		} catch (Throwable e) {
			throw new ErroProcessarNotasFiscaisEdiItemException(notaFiscalConhecimentoDtos, cliente, item, e);
		} finally {
			SessionContext.remove("adsm.session.authenticatedUser");
			SessionContext.remove(SessionKey.FILIAL_KEY);
			SessionContext.remove(SessionKey.PAIS_KEY);
		}

		this.atualizarItemProcessado(item);
		this.processamentoIbEdiService.findRemainingNotes(item.getNrProcessamento());
	}

	private void sendMessageQueueErro
	(List<NotaFiscalConhecimentoDto> notaFiscalConhecimentoDtos, String mensagem, Throwable throwable,
	 Cliente cliente, Long idProcessamentoEdi, Long nrProcessamento){


		IntegracaoJmsService.JmsMessageSender messageSender = integracaoJmsService.createMessage(Queues.STORE_LOG_EDI);
		notaFiscalConhecimentoDtos.stream().forEach(notaFiscal-> {
			carregarMessageSenderStoreLogEdiDto
					(idProcessamentoEdi, notaFiscal.getNrNotaFiscal().longValue(),mensagem, messageSender);
			if (throwable instanceof BusinessException) {
				logAtualizacaoEDIService
					.storeLog
						(
							notaFiscal.getNrNotaFiscal(), nrProcessamento,
							cliente, null, (BusinessException) throwable
						);
			} else {
				logAtualizacaoEDIService
					.storeLog
						(
							notaFiscal.getNrNotaFiscal(), nrProcessamento,
							cliente, null, mensagem, false
						);
			}
		});
		integracaoJmsService.storeMessage(messageSender);
	}

	public String montarMessagemErroExcessao(Throwable throwable) {
		Optional<StackTraceElement> stackTraceElement =  Arrays.stream(throwable.getStackTrace()).findFirst();
		StringBuilder message = new StringBuilder();
		if (!(throwable instanceof BusinessException)) {
			if (throwable.getMessage() != null) {
				message.append(throwable.getMessage()).append(System.lineSeparator());
			} else {
				message.append("Error interno não tratado.").append(System.lineSeparator());
			}
		} else {
			String msgKey = ((BusinessException)throwable).getMessageKey();
			String messageBusiness = RecursoMensagemCache.getMessage(msgKey, LocaleContextHolder.getLocale().toString());
			message.append(messageBusiness).append(System.lineSeparator());
		}

		if (stackTraceElement.isPresent()) {
			StackTraceElement stackElement = stackTraceElement.get();
			message.append(stackElement.getFileName()).append("/");
			message.append(stackElement.getClassName()).append("/");
			message.append(stackElement.getMethodName()).append("/");
			message.append(stackElement.getLineNumber());
		}
		return message.toString();
	}

	public void carregarMessageSenderStoreLogEdiDto
			(Long idProcessamentoEdi, Long nrNotaFiscal,
			 IntegracaoJmsService.JmsMessageSender jmsMessageSender){
		this.carregarMessageSenderStoreLogEdiDto(idProcessamentoEdi, nrNotaFiscal, null, jmsMessageSender);
	}
	public void carregarMessageSenderStoreLogEdiDto
		(Long idProcessamentoEdi, Long nrNotaFiscal, String mensagem,
		 IntegracaoJmsService.JmsMessageSender jmsMessageSender){

		StoreLogEdiDto storeLogEdiDto = new StoreLogEdiDto();
		storeLogEdiDto.setIdProcessamentoEdi(idProcessamentoEdi);
		storeLogEdiDto.setNrNotaFiscal(nrNotaFiscal);
		storeLogEdiDto.setMensagem(mensagem);
		jmsMessageSender.addMsg(storeLogEdiDto);
	}

	public BusinessException lancarBusinessException(Long nrProcessamento, Long idProcessamentoEdi, BusinessException be) {
		ErroProcessamentoEdiDto erroProcessamentoEdiDto = new ErroProcessamentoEdiDto();
		erroProcessamentoEdiDto.setNrProcessamento(nrProcessamento);
		erroProcessamentoEdiDto.setIdProcessamentoEdi(idProcessamentoEdi);
		Object[] messageArguments =  new Object[]{erroProcessamentoEdiDto};
		return new BusinessException(be.getMessageKey(), ArrayUtils.addAll(messageArguments, be.getMessageArguments()));
	}

	public Map<String, Object> executeProcessarNotasFiscaisEdiItem(ProcessarEdiDTO item) {
		//aqui
		final Map<String, Object> parameters = item.getParameters();
		parameters.put("nrProcessamento", item.getNrProcessamento()); //LMS-3331

		// LMSA-7137
		parameters.put("clienteRemetente", item.getClienteRemetente());

		final Map<String, Object> retorno = new HashMap<String, Object>();
		try {
			retorno.putAll(notaFiscalEDIService.executeNotasFiscaisEdi(parameters));
			retorno.put("validado", Boolean.TRUE);
		} catch (final BusinessException e) {
			final List list = (List) parameters.get("notaFiscalConhecimento");
			if(list != null) {
				for (Object o : list) {
					final Map map = (HashMap) o;
					if (map != null) {
						logAtualizacaoEDIService.storeLog((Integer) map.get("nrNotaFiscal"), item.getNrProcessamento(), item.getClienteRemetente(), null, e);
					}
				}
			}
			retorno.put("validado", Boolean.FALSE);
		} catch (final Throwable e) {
			log.error("Erro no processamento do EDI nrProcessamento:" + item.getNrProcessamento() + " erro: " + e.getMessage(), e);

			final List list = (List) parameters.get("notaFiscalConhecimento");
			if(list != null) {
				for (Object o : list) {
					final Map map = (HashMap) o;
					if (map != null) {
						String msg = e.getMessage();
						if ((msg == null) && (e.fillInStackTrace() != null)) {
							msg = e.fillInStackTrace().toString();
						}
						logAtualizacaoEDIService.storeLog((Integer) map.get("nrNotaFiscal"),
								item.getNrProcessamento(), item.getClienteRemetente(), null, msg,
								false);
					}
				}
			}
			retorno.put("validado", Boolean.FALSE);
		}
		return retorno;
	}

	/**
	 * Abre o Monitoramento Descarga
	 * @param criteria
	 */
	public void executeOpenMonitoramentoDescarga(final Map<String, Object> criteria) {
		final ProcessarEdiDTO item = (ProcessarEdiDTO) criteria.get("item");
		final Map<String, Object> parameters = item.getParameters();

		//LMSA-3331
		final String nrProcessamento = item.getNrProcessamento().toString();
		final Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
		final Map mapMeioTransporte = (HashMap) parameters.get("meioTransporte");

		openMonitoramentoDescarga(conhecimento, mapMeioTransporte, nrProcessamento);
	}

	public void openMonitoramentoDescarga(Conhecimento conhecimento, Map mapMeioTransporte, String nrProcessamento) {

		/** Busca Meio de Transporte */
		final Map<String, String> mapSituacaoDescarga = new HashMap<>();
		final MeioTransporte meioTransporte = conhecimentoNormalService.createMeioTransporte(conhecimento, mapMeioTransporte, mapSituacaoDescarga);

		/** Grava Monitoramento Descarga */
		final MonitoramentoDescarga monitoramentoDescarga = conhecimentoNormalService.storeMonitoramentoDescarga(meioTransporte, conhecimento.getDtColeta(), mapSituacaoDescarga.get("tpSituacaoDescarga"), LongUtils.ZERO, conhecimento.getBlIndicadorEdi(), conhecimento.getPedidoColeta().getDsInfColeta(), nrProcessamento);

		/** Valida se Monitoramento foi criado */
		if (monitoramentoDescarga == null) {
			throw new BusinessException("LMS-04227");
		}
	}

	/**
	 * Verifica atraves do parametro_geral ClientesSemValidaçãoIntervaloEtiqueta
	 * se o cliente passado por parametro não possue validação de intervalo de
	 * etiqueta
	 *
	 * @param criteria
	 * @return Boolean
	 */
	public Boolean validateIntervaloEtiqueta(final Map criteria) {
		/* Id cliente remetente */
		final Long idClienteRemetente = (Long) criteria.get("idCliente");
		final String clientes = (String) parametroGeralService.findConteudoByNomeParametro("ClientesSemValidaçãoIntervaloEtiqueta", false);

		final String[] idClientes = clientes.split(";");
		for (final String id : idClientes) {
			if(id.equals(String.valueOf(idClienteRemetente))) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public void removeRegistrosProcessados(final Map parameters) {
		final List<Long> list = (List) parameters.get("listIdsNotasFiscaisEdiInformada");
		removeRegistrosProcessados(list);
	}

	public void removeRegistrosProcessados(List<Long> list){
		if((list != null) && !list.isEmpty()) {
			notaFiscalExpedicaoEDIVolumeService.removeByIdNotaFiscalEdi(list);
			notaFiscalExpedicaoEDIComplementoService.removeByIdNotaFiscalEdi(list);
			notaFiscalExpedicaoEDIItemService.removeByIdNotaFiscalEdi(list);
			notaFiscalExpedicaoEDIService.removeByIdNotaFiscalEdi(list);
		}
	}

	public void removeLogEdiProcessado(final Map parameters) {
		final Long nrProcessamento = (Long) parameters.get("nrProcessamento");
		if(nrProcessamento != null) {
			logAtualizacaoEDIService.removeByNrProcessamento(nrProcessamento);
		}
	}

	public void finalizaProcessamentoEDI(Long idPedidoColeta, Long idMonitoramentoDescarga, String tpProcessamento) {
		if (idPedidoColeta != null) {
			PedidoColeta pedidoColeta = this.pedidoColetaService.findByIdBasic(idPedidoColeta);
			pedidoColeta.setCliente(this.clienteService.findByIdPedidoColeta(pedidoColeta.getIdPedidoColeta()));

			if (pedidoColeta != null && "BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue()) &&
					validaClienteLiberaEtiquetaContingencia(pedidoColeta, tpProcessamento, SessionUtils.getFilialSessao().getIdFilial())) {
				this.monitoramentoDescargaService.updateDescarcaFinalizadaMonitoramentoDescargaEDI(idMonitoramentoDescarga);
			} else {
				this.monitoramentoDescargaService.updateMonitoramentoDescargaEDI(pedidoColeta, idMonitoramentoDescarga);
			}

		} else {
			this.monitoramentoDescargaService.updateDigitacaoNotasIniciadaMonitoramentoDescargaEDI(idMonitoramentoDescarga);
		}
	}

	private void atualizarItemProcessado(ProcessaNotasEdiItemDto item) {
		if (item.getFinalizou()){
			processamentoEdiService.updateTpStatus(String.valueOf(item.getNrProcessamento()), new DomainValue("PI"));
			return;
		}

		IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.STORE_LOG_EDI);
		item.getParameters().getNotaFiscalConhecimento().stream().forEach(notaFiscalConhecimentoDto ->
			carregarMessageSenderStoreLogEdiDto
				(
					item.getIdProcessamentoEdi(),
					Long.valueOf(notaFiscalConhecimentoDto.getNrNotaFiscal()),
					jmsMessageSender
				));
		integracaoJmsService.storeMessage(jmsMessageSender);
	}

	public void setNotaFiscalExpedicaoEDIService(final NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService) {
		this.notaFiscalExpedicaoEDIService = notaFiscalExpedicaoEDIService;
	}

	public void setNotaFiscalExpedicaoEDIVolumeService(final NotaFiscalExpedicaoEDIVolumeService notaFiscalExpedicaoEDIVolumeService) {
		this.notaFiscalExpedicaoEDIVolumeService = notaFiscalExpedicaoEDIVolumeService;
	}

	public void setNotaFiscalExpedicaoEDIComplementoService(final NotaFiscalExpedicaoEDIComplementoService notaFiscalExpedicaoEDIComplementoService) {
		this.notaFiscalExpedicaoEDIComplementoService = notaFiscalExpedicaoEDIComplementoService;
	}

	public void setNotaFiscalExpedicaoEDIItemService(final NotaFiscalExpedicaoEDIItemService notaFiscalExpedicaoEDIItemService) {
		this.notaFiscalExpedicaoEDIItemService = notaFiscalExpedicaoEDIItemService;
	}

	public void setLogAtualizacaoEDIService(final LogAtualizacaoEDIService logAtualizacaoEDIService) {
		this.logAtualizacaoEDIService = logAtualizacaoEDIService;
	}

	public void setParametroGeralService(final ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	private List<Integer> findNumerosNotasEdi(final Map criteria) {
		List<Integer> nrNotasFiscais = (List<Integer>) criteria.get("listNotasFiscaisEdiInformada");
		final List<Map<String, Integer>> intervalosNotasFiscaisEdi = (List<Map<String, Integer>>) criteria.get("listIntervalosNotasFiscaisEdi");
		if (nrNotasFiscais == null && intervalosNotasFiscaisEdi != null) {
			nrNotasFiscais = new ArrayList<Integer>();
			for (final Map<String, Integer> intervalo : intervalosNotasFiscaisEdi) {
				Integer nrNotaFiscalInicial = intervalo.get("nrNotaFiscalInicial");
				final Integer nrNotaFiscalFinal = intervalo.get("nrNotaFiscalFinal");
				while (nrNotaFiscalInicial <= nrNotaFiscalFinal) {
					nrNotasFiscais.add(nrNotaFiscalInicial++);
				}
			}
		}
		return nrNotasFiscais;
	}

	private List<NotaFiscalEdi> findNotasEdi(final Cliente cliente, final List<Integer> nrNotasFiscais, final String tpAgrupamentoEdi, final String processarPor) {
		final String cnpj = cliente.getPessoa().getNrIdentificacao();
		Long idInformacaoDoctoCliente;
		if("E".equals(tpAgrupamentoEdi) && cliente.getInformacaoDoctoCliente() != null){
			idInformacaoDoctoCliente = cliente.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente();
		} else {
			idInformacaoDoctoCliente = null;
		}
		if (nrNotasFiscais == null) {
			return notaFiscalExpedicaoEDIService.find(cnpj, idInformacaoDoctoCliente, processarPor);
		}
			final StringBuilder message = new StringBuilder("[EDI] Filial: ");
			message.append(SessionUtils.getFilialSessao().getSgFilial());
			message.append(" - Cliente: ");
			message.append(cnpj);
			message.append(" - NFs informadas a serem processadas: ");

		for (final Integer nrNotaFiscalEdi : nrNotasFiscais) {
			message.append(nrNotaFiscalEdi);
				message.append(",");
			}
		final List<NotaFiscalEdi> notasEncontradas = notaFiscalExpedicaoEDIService.findNotas(cnpj, nrNotasFiscais, idInformacaoDoctoCliente, processarPor);
		final List<NotaFiscalEdi> result = new ArrayList<>();
		// garante a ordenação digitada pelo usuário
		for (final Integer recebida : nrNotasFiscais) {
			for (final NotaFiscalEdi encontrada : notasEncontradas) {
				if (recebida.equals(encontrada.getNrNotaFiscal())) {
					result.add(encontrada);
					break;
				}
			}
		}
			log.warn(message);
		return result;
	}

	public void validateEDI(final Map parameters) {
		DateTime sysDate = JTDateTimeUtils.getDataHoraAtual();
		final DateTime dhChegada = (DateTime) parameters.get("dhChegada");

		/*
		 * Validar para que a data/hora informada esteja menor ou igual à
		 * data/hora atual. Se não estiver visualizar mensagem LMS-00074, não
		 * aceitando a informação.
		 */
		if(sysDate.compareTo(dhChegada) < 0) {
			throw new BusinessException("LMS-00074");
		}

		// Chama a procedure P_ACERTO_NOTA_FISCAL_EDI para acertar os dados dos
		// clientes no LMS
		final String cnpjRemetente = (String) parameters.get("nrIdentificacaoNaoFormatado");
		notaFiscalExpedicaoEDIService.updateAcertoNotaFiscal(Long.valueOf(cnpjRemetente));

		final BigDecimal tmpMinimoDataColeta = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("TMP_MINIMO_DATA_COLETA", false);

		/*
		 * Validar para que a data/hora informada não esteja menor que a
		 * data/hora atuais menos a quantidade de horas informada no parâmetro
		 * geral TMP_MINIMO_DATA_COLETA, se estiver visualizar a mensagem
		 * LMS-04250, não aceitando a informação.
		 */
		if(tmpMinimoDataColeta != null) {
			sysDate = sysDate.minusHours(tmpMinimoDataColeta.intValue());
			if(sysDate.compareTo(dhChegada) > 0) {
				throw new BusinessException("LMS-04250");
			}
		}
	}

	/**
	 * Valida se existe dados para processamento "Especial por documento do Cliente", retornando sua descrição
	 * @param idCliente
	 */
	public String findInformacaoDoctoClienteDescription(final Long idCliente) {
		final Cliente cliente = clienteService.findByIdInitLazyProperties(idCliente, false);
		if (cliente.getInformacaoDoctoClienteEDI() == null) {
			throw new BusinessException("LMS-28003");
		}
		return cliente.getInformacaoDoctoClienteEDI().getDsCampo();
	}

	/**
	 * Valida se existe dados para processamento "Por Manifesto (Consolidado)", retornando sua descrição
	 * @param idCliente
	 */
	public String findInformacaoDoctoClienteConsolidadoDescription(final Long idCliente) {
		final Cliente cliente = clienteService.findByIdInitLazyProperties(idCliente, false);
		if (cliente.getInformacaoDoctoClienteConsolidado() == null) {
			throw new BusinessException("LMS-28003");
		}
		return cliente.getInformacaoDoctoClienteConsolidado().getDsCampo();
	}

	public List<Map<String,Object>> findConhecimentosSemPesagem(final Map criteria) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Long idPedidoColeta = (Long) criteria.get("idPedidoColeta");
		Long idMonitoramentoDescarga = (Long) criteria.get("idMonitoramentoDescarga");
		String tpProcessamento = (String) criteria.get("tpProcessamento");
		Long idFilial = SessionUtils.getFilialSessao() != null
				? SessionUtils.getFilialSessao().getIdFilial()
				: (Long)criteria.get("idFilial");
		Boolean blContingencia = contingenciaService.findByFilial(idFilial, "A", "E") != null;

		List<Conhecimento> listaConhecimento = findConhecimentosSemPesagem(idPedidoColeta, idMonitoramentoDescarga, tpProcessamento, idFilial);

		for (Conhecimento conhecimento : listaConhecimento) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("blContingencia", blContingencia);
			map.put("idConhecimento", conhecimento.getIdDoctoServico());
			list.add(map);
		}
		return list;
	}

	public List<Conhecimento> findConhecimentosSemPesagem(Long idPedidoColeta, Long idMonitoramentoDescarga, String tpProcessamento, Long idFilial){
		//aqui
		List<Conhecimento> listaConhecimento = new ArrayList<>();
		if (idPedidoColeta != null) {
			PedidoColeta pedidoColeta = this.pedidoColetaService.findByIdBasic(idPedidoColeta);
			pedidoColeta.setCliente(clienteService.findByIdPedidoColeta(pedidoColeta.getIdPedidoColeta()));

			if (pedidoColeta != null && "BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())
					&& validaClienteLiberaEtiquetaContingencia(pedidoColeta, tpProcessamento, idFilial)) {
				listaConhecimento = conhecimentoService.findConhecimentosEmProcessamentoEDIByIdMonitoramentoDescarga(idMonitoramentoDescarga);
			}
		}
		return listaConhecimento;
	}

	public boolean validaClienteLiberaEtiquetaContingencia(PedidoColeta pedidoColeta, String tpProcessamento, Long idFilial){
		Boolean blContingencia = contingenciaService.findByFilial(idFilial, "A", "E") != null;
		Boolean isLiberaEtiquetaEdi = Boolean.TRUE.equals(pedidoColeta.getCliente().getBlLiberaEtiquetaEdi());
		Boolean isLiberaEtiquetaEdiLinehaul = Boolean.TRUE.equals(pedidoColeta.getCliente().getBlLiberaEtiquetaEdiLinehaul());

		if("M".equals(tpProcessamento) && isLiberaEtiquetaEdiLinehaul){
			return true;
		}else if(!"M".equals(tpProcessamento) && isLiberaEtiquetaEdi){
			return true;
		} else {
			return blContingencia;
		}
	}

	public void setClienteService(final ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setNotaFiscalEDIService(final NotaFiscalEDIService notaFiscalEDIService) {
		this.notaFiscalEDIService = notaFiscalEDIService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	/**
	 * @param ppeService
	 *            the ppeService to set
	 */
	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}

	public void setLogErrosEDIService(LogErrosEDIService logErrosEDIService) {
		this.logErrosEDIService = logErrosEDIService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
	
	/**
	 * @param filialService
	 *            the filialService to set
	 */
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setConhecimentoNormalService(ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	
	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
	
	public void setCceItemService(CCEItemService cceItemService) {
		this.cceItemService = cceItemService;
	}
	
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public void setNotaFiscalEDIVolumeService(
			NotaFiscalEDIVolumeService notaFiscalEDIVolumeService) {
		this.notaFiscalEDIVolumeService = notaFiscalEDIVolumeService;
	}

	public Boolean executeValidacaoEdiItem(ValidarEdiDTO dto) {
		Cliente cliente = clienteService.findById(dto.getClienteRemetente().getIdCliente());
		try {
			notaFiscalEDIService.validarNotaEdi(cliente, dto.getNrProcessamento(), dto.getNotaFiscalEdi(),
						dto.getTpProcessamento(), dto.getProcessarPor(), dto.getIdFilial());
			return Boolean.TRUE;
		} catch (final BusinessException e) {
			log.error("Erro na validacao do EDI nrProcessamento:" + dto.getNrProcessamento() + " nrNotaFiscal: " + dto.getNotaFiscalEdi().getNrNotaFiscal() + " erro: " + e.getMessage(), e);
			logAtualizacaoEDIService.storeLog(dto.getNotaFiscalEdi().getNrNotaFiscal(), dto.getNrProcessamento(), cliente, null, e);
			erroMonitoramentoNota(dto.getNotaFiscalEdi().getNrNotaFiscal(), cliente, dto.getIdProcessamentoEdi(), e);
			return Boolean.FALSE;
		} catch (final Exception e) {
			log.error("Erro na validacao do EDI nrProcessamento:" + dto.getNrProcessamento() + " nrNotaFiscal: " + dto.getNotaFiscalEdi().getNrNotaFiscal() + " erro: " + e.getMessage(), e);
			logAtualizacaoEDIService.storeLog(dto.getNotaFiscalEdi().getNrNotaFiscal(), dto.getNrProcessamento(), cliente, null, e.getMessage(), false);
			erroMonitoramentoNota(dto.getNotaFiscalEdi().getNrNotaFiscal(), cliente, dto.getIdProcessamentoEdi(), e);
			return Boolean.FALSE;
		}
	}

	public void erroMonitoramentoNota(Integer nrNotaFiscal, Cliente clienteRemetente, Long idProcessamentoEdi, Throwable throwable) {

		if (idProcessamentoEdi == null) {
			return;
		}

		String mensagem = montarMessagemErroExcessao(throwable);

		if (throwable instanceof BusinessException) {
			BusinessException businessException = (BusinessException)throwable;
			mensagem = MessageFormat.format(mensagem, businessException.getMessageArguments());
		}

		ProcessamentoEdi processamentoEdi = processamentoEdiService.findById(idProcessamentoEdi);
		gerarProcessamentoNotaEdi(nrNotaFiscal, clienteRemetente, processamentoEdi, 0L, null);
		processamentoNotaEdiService.storeAll(processamentoEdi.getListProcessamentoNotaEdi());
		IntegracaoJmsService.JmsMessageSender messageSender = integracaoJmsService.createMessage(Queues.STORE_LOG_EDI);

		carregarMessageSenderStoreLogEdiDto(idProcessamentoEdi, nrNotaFiscal.longValue(), mensagem, messageSender);
		integracaoJmsService.storeMessage(messageSender);
	}

	public Long storeProcessamentoNotaEdi(List<ProcessaNotasEdiItemDto> processaNotasEdiItemDtos, ParamAgruparNotasFiscaisDTO panfDTO) {
		Cliente	clienteRemetente = this.clienteService.findById(processaNotasEdiItemDtos.get(0).getClienteRemetente().getIdCliente());
		DadosValidacaoEdiDTO dadosValidacaoEdiDTO = panfDTO.getListaValidacao().get(0);
		Long idProcessamentoEdi = dadosValidacaoEdiDTO.getIdProcessamentoEdi();
		ProcessamentoEdi processamentoEdi = this.processamentoEdiService.findById(idProcessamentoEdi);
		Long index = 0L;
		List<NotaFiscalConhecimentoDto> notaFiscalConhecimentoDtos = null;

		for(ProcessaNotasEdiItemDto processaNotasEdiItemDto : processaNotasEdiItemDtos){
			processaNotasEdiItemDto.setListIdsNotasFiscaisEdiParcial(new ArrayList<>());
			processaNotasEdiItemDto.setInicioIndex(++index);
			notaFiscalConhecimentoDtos = processaNotasEdiItemDto.getParameters().getNotaFiscalConhecimento();
			for (NotaFiscalConhecimentoDto notaFiscalConhecimento : notaFiscalConhecimentoDtos) {
				gerarProcessamentoNotaEdi(notaFiscalConhecimento.getNrNotaFiscal(), clienteRemetente, processamentoEdi, index, processaNotasEdiItemDto.getNrProcessamento().toString());
				processaNotasEdiItemDto.getListIdsNotasFiscaisEdiParcial().add(notaFiscalConhecimento.getIdNotaFiscalEdi());
				++index;
			}
			processaNotasEdiItemDto.setFinalIndex(--index);
		}

		processamentoNotaEdiService.storeAll(processamentoEdi.getListProcessamentoNotaEdi());

		return idProcessamentoEdi;
	}

	public ProcessamentoNotaEdi gerarProcessamentoNotaEdi(Integer nrNotaFiscal, Cliente clienteRemetente, ProcessamentoEdi processamentoEdi, Long index, String nrProcessamento) {

		ProcessamentoNotaEdi processamentoNotaEdi =  new ProcessamentoNotaEdi();
		processamentoNotaEdi.setNrNotaFiscal(nrNotaFiscal.longValue());
		processamentoNotaEdi.setBlProcessada(false);
		processamentoNotaEdi.setClienteRemetente(clienteRemetente);
		processamentoNotaEdi.setNrIndex(index);
		processamentoNotaEdi.setNrProcessamento(nrProcessamento);
		processamentoEdi.addProcessamentoNotaEdi(processamentoNotaEdi);

		return processamentoNotaEdi;
	}

	public Long storeProcessamentoEdi(Cliente clienteRemetente, Long idFilial, Long idUsuario, Long qtTotalNotas) {
		ProcessamentoEdi processamentoEdi = new ProcessamentoEdi();
		processamentoEdi.setFilial(filialService.findById(idFilial));
		processamentoEdi.setUsuario(usuarioAdsmService.findById(idUsuario));
		processamentoEdi.setClienteProcessamento(clienteRemetente);
		processamentoEdi.setQtTotalNotas(qtTotalNotas);
		processamentoEdi.setDhProcessamento(new DateTime());
		processamentoEdi.setQtNotasProcessadas(0L);
		processamentoEdi.setBlVisivel(false);
		return (Long)processamentoEdiService.store(processamentoEdi);
	}

	public ProcessarNotasEDICommonService getProcessarNotasEDICommonService() {
		return processarNotasEDICommonService;
	}

	public void setProcessarNotasEDICommonService(ProcessarNotasEDICommonService processarNotasEDICommonService) {
		this.processarNotasEDICommonService = processarNotasEDICommonService;
	}

	public ContingenciaService getContingenciaService() {
		return contingenciaService;
	}

	public void setContingenciaService(ContingenciaService contingenciaService) {
		this.contingenciaService = contingenciaService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public MonitoramentoDescargaService getMonitoramentoDescargaService() {
		return monitoramentoDescargaService;
	}

	public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}

	public ProcessamentoEdiService getProcessamentoEdiService() {
		return processamentoEdiService;
	}

	public void setProcessamentoEdiService(ProcessamentoEdiService processamentoEdiService) {
		this.processamentoEdiService = processamentoEdiService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public UsuarioAdsmService getUsuarioAdsmService() {
		return usuarioAdsmService;
	}

	public void setUsuarioAdsmService(UsuarioAdsmService usuarioAdsmService) {
		this.usuarioAdsmService = usuarioAdsmService;
	}

	public ProcessamentoNotaEdiService getProcessamentoNotaEdiService() {
		return processamentoNotaEdiService;
	}

	public void setProcessamentoNotaEdiService(ProcessamentoNotaEdiService processamentoNotaEdiService) {
		this.processamentoNotaEdiService = processamentoNotaEdiService;
	}

	public void enviarFilaRemoveRegistrosProcessados(ProcessaNotasEdiItemDto processaNotasEdiItemDto) {
		IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService
				.createMessage(Queues.REMOVE_REGISTROS_PROCESSADOS, processaNotasEdiItemDto);
		integracaoJmsService.storeMessage(jmsMessageSender);
	}

	public void enviarListaPacialParaRemover(ProcessaNotasEdiItemDto item){
		List<Long> newListId = item.getListIdsNotasFiscaisEdiParcial().stream().collect(Collectors.toList());
		item.setListIdsNotasFiscaisEdiInformada(newListId);
		item.setListIdsNotasFiscaisEdiParcial(null);
		enviarFilaRemoveRegistrosProcessados(item);
	}

	public ProcessamentoIbEdiService getProcessamentoIbEdiService() {
		return processamentoIbEdiService;
	}

	public void setProcessamentoIbEdiService(ProcessamentoIbEdiService processamentoIbEdiService) {
		this.processamentoIbEdiService = processamentoIbEdiService;
	}
}
