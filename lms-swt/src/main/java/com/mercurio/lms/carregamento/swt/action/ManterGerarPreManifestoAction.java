package com.mercurio.lms.carregamento.swt.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoVolumeService;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.edi.model.service.ConhecimentoFedexService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.DocumentoMir;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.entrega.model.service.DocumentoMirService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.SubstAtendimentoFilial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
import com.mercurio.lms.municipios.model.service.SubstAtendimentoFilialService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.sim.model.DocumentoServicoRetirada;
import com.mercurio.lms.sim.model.SolicitacaoRetirada;
import com.mercurio.lms.sim.model.service.SolicitacaoRetiradaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.swt.manterGerarPreManifestoAction"
 */

public class ManterGerarPreManifestoAction extends MasterDetailAction {
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private ClienteService clienteService;
	private FilialService filialService;
	private ControleCargaService controleCargaService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private RotaIdaVoltaService rotaIdaVoltaService;
	private AwbService awbService;
	private DoctoServicoService doctoServicoService;
	private ConversaoMoedaService conversaoMoedaService;
	private PedidoColetaService pedidoColetaService;
	private DetalheColetaService detalheColetaService;
	private SubstAtendimentoFilialService substAtendimentoFilialService;
	private PessoaService pessoaService;
	private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
	private DocumentoMirService documentoMirService;
	private SolicitacaoRetiradaService solicitacaoRetiradaService;
	private ControleTrechoService controleTrechoService;
	private EventoControleCargaService eventoControleCargaService;
	private EmpresaService empresaService;
	private ParametroGeralService parametroGeralService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private PreManifestoVolumeService preManifestoVolumeService;
	private ConhecimentoService conhecimentoService;
	private DadosComplementoService dadosComplementoService;
	private ProprietarioService proprietarioService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	
	// LMSA-6267: LMSA-6630
	private ConhecimentoFedexService conhecimentoFedexService;
	
	private List<Long> conferenciaEmLinha = new ArrayList<Long>();
	
	public ProprietarioService getProprietarioService() {
		return proprietarioService;
	}

	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}


	/**
	 * ResultSetPage de Manifestos
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedManifesto(Map criteria) {
		return this.getManifestoService().findPaginatedManifesto(createCriteria(criteria));
	}

	/**
	 * Quantidade de manifestos do ResultSetPage
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Integer getRowCountManifesto(Map criteria) {
		return this.getManifestoService().getRowCountManifesto(createCriteria(criteria));
	}
	
	/**
	 * Cria critérios de pesquisa.
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private TypedFlatMap createCriteria(Map criteria) {
		TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("tpManifesto", criteria.get("tpManifesto"));
		tfmCriteria.put("tpPreManifesto", criteria.get("tpPreManifesto"));
		tfmCriteria.put("controleCarga.idControleCarga", criteria.get("idControleCarga"));
		tfmCriteria.put("controleCarga.rotaIdaVolta.idRotaIdaVolta", criteria.get("idRotaIdaVolta"));
		tfmCriteria.put("controleCarga.rotaColetaEntrega.idRotaColetaEntrega", criteria.get("idRotaColetaEntrega"));
		tfmCriteria.put("filialByIdFilialDestino.idFilial", criteria.get("idFilialDestino"));
		tfmCriteria.put("cliente.idCliente", criteria.get("idCliente"));
		tfmCriteria.put("solicitacaoRetirada.idSolicitacaoRetirada", criteria.get("idSolicitacaoRetirada"));
		tfmCriteria.put("nrPreManifesto", criteria.get("nrPreManifesto"));
		tfmCriteria.put("_currentPage", criteria.get("_currentPage"));
		tfmCriteria.put("_pageSize", criteria.get("_pageSize"));
		tfmCriteria.put("_order", criteria.get("_order"));
		return tfmCriteria;
	}

	/**
	 * ######################### INICIO DOS MÉTODOS DIVERSOS PARA A TELA ############################
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupBySgFilial(Map criteria) {
		List listResult = new ArrayList();
		List listFilial = filialService.findLookupBySgFilial((String)criteria.get("sgFilial"), null);
		for (Iterator iter = listFilial.iterator(); iter.hasNext();) {
			Map mapFilial = (Map) iter.next();
			Map mapResult = new HashMap();
			mapResult.put("idFilial", mapFilial.get("idFilial"));
			mapResult.put("sgFilial", mapFilial.get("sgFilial"));
			Map mapPessoa = (Map)mapFilial.get("pessoa");
			mapResult.put("nmFantasia", mapPessoa.get("nmFantasia"));
			
			listResult.add(mapResult);
		}
		return listResult;
	}
	
	public void validateInicioProjetoMerger(TypedFlatMap criteria){
    	String parametro = parametroGeralService.findSimpleConteudoByNomeParametro("INICIO_PROJETO_MERGER");
    	if (parametro != null && parametro.length() > 0){
    		YearMonthDay dataInicio = JTDateTimeUtils.convertDataStringToYearMonthDay(parametro,"dd/MM/yyyy");
    		if (JTDateTimeUtils.comparaData(dataInicio, JTDateTimeUtils.getDataAtual()) < 1){
    			throw new BusinessException("LMS-05322");
    		}
    	}
    }
	
	/**
	 * Busca um controle de carga com validações.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findControleCargaByNrControleByFilial(Map criteria) {
		Long nrControleCarga = (Long)criteria.get("nrControleCarga");
		Long idFilial = (Long)criteria.get("idFilialOrigem");
		String tpPreManifesto = (String)criteria.get("tpPreManifesto");
		String tpOperacao = (String)criteria.get("tpOperacao");
		
		List listControleCarga = null;
		ControleCarga controleCarga = controleCargaService.findControleCargaByNrControleCargaByIdFilial(nrControleCarga, idFilial);
		
		if (controleCarga != null) {
			listControleCarga = new ArrayList();
			Map mapControleCarga = new HashMap();
			
			// Verifica se o Controle de Carga está Cancelado.
			if (controleCarga.getTpStatusControleCarga().getValue().equals("CA")) {
				throw new BusinessException("LMS-05123");
			}
			
			// Verifica se o Controle de Carga está com o carregamento finalizado para a filial do usuario logado.
			List listEventoCCFinalizado = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(
				SessionUtils.getFilialSessao().getIdFilial(), 
				controleCarga.getIdControleCarga(),
				"FC"
			);
			if (!listEventoCCFinalizado.isEmpty()) {
				throw new BusinessException("LMS-05124");
			}
			
			// Verifica se o Controle de Carga está emitido para a filial do usuario logado.
			List listEventoCCEmitido = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(
				SessionUtils.getFilialSessao().getIdFilial(), 
				controleCarga.getIdControleCarga(),
				"EM"
			);
			List listEventoCCCancelamentoEmissao = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(
					SessionUtils.getFilialSessao().getIdFilial(), 
					controleCarga.getIdControleCarga(),
					"EC"
			);
			if (listEventoCCEmitido.size() > listEventoCCCancelamentoEmissao.size()) {
				throw new BusinessException("LMS-05172");
			}
						
			if (controleCarga.getTpControleCarga() != null && "C".equals(controleCarga.getTpControleCarga().getValue())) {
				controleCargaService.validateParcelaPud(controleCarga.getIdControleCarga(), tpPreManifesto);
			} 
			if("E".equals(tpOperacao) && StringUtils.isNotBlank(tpPreManifesto)) {
				getManifestoService().validateInclusaoManifestosControleCarga(controleCarga.getIdControleCarga(), tpPreManifesto);
			}
			
						
			mapControleCarga.put("idControleCarga", controleCarga.getIdControleCarga());
			mapControleCarga.put("nrControleCarga", controleCarga.getNrControleCarga());
			mapControleCarga.put("idFilialOrigem", controleCarga.getFilialByIdFilialOrigem().getIdFilial());
			mapControleCarga.put("sgFilialOrigem", controleCarga.getFilialByIdFilialOrigem().getSgFilial());
			mapControleCarga.put("nmFantasiaFilialOrigem", controleCarga.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
			mapControleCarga.put("tpControleCarga", controleCarga.getTpControleCarga().getValue());
			mapControleCarga.put("blEntregaDireta", controleCarga.getBlEntregaDireta());
			mapControleCarga.put("proprietario",controleCarga.getProprietario());
    		
			if (controleCarga.getTpControleCarga().getValue().equals("V")) {
				if (controleCarga.getTpRotaViagem() != null && (
						controleCarga.getTpRotaViagem().getValue().equals("EX") || 
						controleCarga.getTpRotaViagem().getValue().equals("EC") )) {
					if (controleCarga.getRotaIdaVolta() != null) {
						mapControleCarga.put("idRotaIdaVolta", controleCarga.getRotaIdaVolta().getIdRotaIdaVolta());
						mapControleCarga.put("nrRotaIdaVolta", controleCarga.getRotaIdaVolta().getNrRota());
						mapControleCarga.put("dsRotaIdaVolta", controleCarga.getRotaIdaVolta().getRota().getDsRota());
					}
				} else {
					if (controleCarga.getRota() != null) {
						mapControleCarga.put("idRotaIdaVolta", controleCarga.getRota().getIdRota());
						mapControleCarga.put("nrRotaIdaVolta", null);
						mapControleCarga.put("dsRotaIdaVolta", controleCarga.getRota().getDsRota());
					}
				}
			} else if (controleCarga.getTpControleCarga().getValue().equals("C")) {
				if (!controleCarga.getFilialByIdFilialOrigem().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
					throw new BusinessException("LMS-05169");
				}
				
				if (controleCarga.getRotaColetaEntrega() != null) {
					mapControleCarga.put("idRotaColetaEntrega", controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega());
					mapControleCarga.put("nrRotaColetaEntrega", controleCarga.getRotaColetaEntrega().getNrRota());
					mapControleCarga.put("dsRotaColetaEntrega", controleCarga.getRotaColetaEntrega().getDsRota());
				}
				
				// Verifica se existe um Manifesto para o Controle de Carga em questão que seja de ENTREGA.
				List listManifesto = this.getManifestoService().findManifestoByIdControleCarga(controleCarga.getIdControleCarga(), null, null, null);
				for (Iterator iterator = listManifesto.iterator(); iterator.hasNext();) {
					Manifesto manifesto = (Manifesto) iterator.next();
					if (manifesto.getTpManifesto().getValue().equals("E") 
							&& !manifesto.getTpStatusManifesto().getValue().equals("CA")
							&& !manifesto.getTpStatusManifesto().getValue().equals("FE")){
						mapControleCarga.put("possuiManifestoEntrega", "LMS-05076");
					}
				}
			}
			
			listControleCarga.clear();
			listControleCarga.add(mapControleCarga);
		}
		
		return listControleCarga;
	}

	/**
	 * Busca um controle de carga
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findControleCargaByNrControleByFilialPadrao(Map criteria) {
		Map mapControleCarga = new HashMap();
		Long nrControleCarga = (Long)criteria.get("nrControleCarga");
		Long idFilial = (Long)criteria.get("idFilialOrigem");
		
		List listControleCarga = new ArrayList();
		ControleCarga controleCarga = controleCargaService.findControleCargaByNrControleCargaByIdFilial(nrControleCarga, idFilial);
		if (controleCarga == null)
			return Collections.EMPTY_LIST;
		
		mapControleCarga.put("idControleCarga", controleCarga.getIdControleCarga());
		mapControleCarga.put("nrControleCarga", controleCarga.getNrControleCarga());
		mapControleCarga.put("idFilialOrigem", controleCarga.getFilialByIdFilialOrigem().getIdFilial());
		mapControleCarga.put("sgFilialOrigem", controleCarga.getFilialByIdFilialOrigem().getSgFilial());
		mapControleCarga.put("nmFantasiaFilialOrigem", controleCarga.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
		mapControleCarga.put("tpControleCarga", controleCarga.getTpControleCarga().getValue());
		
		listControleCarga.clear();
		listControleCarga.add(mapControleCarga);
		
		return listControleCarga;
	}
	

	/**
	 * Busca Rota de Viagem
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupRotaIdaVolta(Map criteria) {
		TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("nrRota", (Integer)criteria.get("nrRota"));

		List list = rotaIdaVoltaService.findLookupRotaIdaVolta(tfmCriteria);
		List retorno = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			RotaIdaVolta rotaIdaVolta = (RotaIdaVolta)iter.next();
			Map map = new HashMap();
			map.put("idRotaIdaVolta", rotaIdaVolta.getIdRotaIdaVolta());
			map.put("nrRota", rotaIdaVolta.getNrRota());
			map.put("dsRota", rotaIdaVolta.getRota().getDsRota());
			retorno.add(map);
		}
		return retorno;
	}
	
	/**
	 * Busca Rota de Coleta Entrega.
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupRotaColetaEntrega(Map criteria) {
		Map filial = new HashMap();
		filial.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
		criteria.put("filial", filial);

		List list = rotaColetaEntregaService.findLookup(criteria);
		List retorno = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			RotaColetaEntrega rotaColetaEntrega = (RotaColetaEntrega)iter.next();
			Map map = new HashMap();
			map.put("idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega());
			map.put("nrRota", rotaColetaEntrega.getNrRota());
			map.put("dsRota", rotaColetaEntrega.getDsRota());
			retorno.add(map);
		}
		return retorno;
	}
	
	/**
	 * Busca Filial.
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findLookupFilial(Map criteria) {
		String sgFilial = (String)criteria.get("sgFilial");
		String tpAcesso = (String)criteria.get("tpAcesso");
		List listFilial = filialService.findLookupBySgFilial(sgFilial, tpAcesso);
		List listResult = new ArrayList();
		for (Iterator iter = listFilial.iterator(); iter.hasNext();) {
			Map mapFilial = (Map) iter.next();
			Map mapResult = new HashMap();
			mapResult.put("idFilial", mapFilial.get("idFilial"));
			mapResult.put("sgFilial", mapFilial.get("sgFilial"));
			Map mapPessoa = (Map)mapFilial.get("pessoa");
			mapResult.put("nmFantasia", mapPessoa.get("nmFantasia"));
			
			listResult.add(mapResult);
		}
		return listResult;
	}

	/**
	 * Busca Solicitação de Retirada.
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupSolicitacaoRetirada(Map criteria) {
		Map filialSolicitacao = new HashMap();
		Map filialRetirada = new HashMap();
		filialSolicitacao.put("idFilial", criteria.get("idFilialSolicitacao"));
		filialRetirada.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
		criteria.put("filial", filialSolicitacao);
		criteria.put("filialRetirada", filialRetirada);
		criteria.remove("idFilialSolicitacao");

		List listSolicitacaoRetirada = solicitacaoRetiradaService.findLookup(criteria);
		Map mapSolicitacaoRetirada = new HashMap();

		for (Iterator iter = listSolicitacaoRetirada.iterator(); iter.hasNext();) {
			SolicitacaoRetirada solicitacaoRetirada = (SolicitacaoRetirada) iter.next();

			mapSolicitacaoRetirada.put("idFilialRetirada", solicitacaoRetirada.getFilialRetirada().getIdFilial());
			mapSolicitacaoRetirada.put("sgFilialRetirada", solicitacaoRetirada.getFilialRetirada().getSgFilial());
			mapSolicitacaoRetirada.put("nmFantasiaRetirada", solicitacaoRetirada.getFilialRetirada().getPessoa().getNmFantasia());
			mapSolicitacaoRetirada.put("idSolicitacaoRetirada", solicitacaoRetirada.getIdSolicitacaoRetirada());
			mapSolicitacaoRetirada.put("nrSolicitacaoRetirada", solicitacaoRetirada.getNrSolicitacaoRetirada());
			mapSolicitacaoRetirada.put("tpSituacao", solicitacaoRetirada.getTpSituacao().getValue());
			
			listSolicitacaoRetirada.clear();
			listSolicitacaoRetirada.add(mapSolicitacaoRetirada);
		}
		
		return listSolicitacaoRetirada;
	}

	/**
	 * Busca Cliente.
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findLookupCliente(Map criteria) {
		Map mapPessoa = new HashMap();
		mapPessoa.put("nrIdentificacao", criteria.get("nrIdentificacao"));
		criteria.put("pessoa", mapPessoa);
		criteria.remove("nrIdentificacao");

		List clientes = clienteService.findLookup(criteria);
		List retorno = new ArrayList();
		if (clientes != null) {
			for (Iterator iter = clientes.iterator(); iter.hasNext();) {
				Map map = new HashMap();
				Cliente cliente = (Cliente) iter.next();
				map.put("idCliente", cliente.getIdCliente());
				map.put("tpSituacao", cliente.getTpSituacao());
				map.put("nmPessoa", cliente.getPessoa().getNmPessoa());
				map.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
				map.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(cliente.getPessoa()));
				map.put("tpIdentificacao", cliente.getPessoa().getTpIdentificacao());
				map.put("tpCliente", cliente.getTpCliente());
				retorno.add(map);
			}
		}
		return retorno;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupProprietario(Map criteria) {
   		Map mapPessoa = new HashMap();
   		mapPessoa.put("nrIdentificacao", criteria.get("nrIdentificacao"));

   		Map map = new HashMap();
   		map.put("pessoa", mapPessoa);
   		map.put("tpSituacao", criteria.get("tpSituacao"));

    	List list = proprietarioService.findLookup(map);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Proprietario proprietario = (Proprietario)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idProprietario", proprietario.getIdProprietario());
    		typedFlatMap.put("tpIdentificacao", proprietario.getPessoa().getTpIdentificacao());
    		typedFlatMap.put("nrIdentificacao", FormatUtils.formatIdentificacao(proprietario.getPessoa()));
    		typedFlatMap.put("nmPessoa", proprietario.getPessoa().getNmPessoa());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
	
	
	/**
	 * #############################
	 * # Documento Servico Methods #
	 * #############################
	 */
	
	/**
	 * Busca os tipos de documento serviço.
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findTipoDocumentoServico(TypedFlatMap criteria) {
		List dominiosValidos = new ArrayList();
		dominiosValidos.add("CTR");
		dominiosValidos.add("CRT");
		dominiosValidos.add("NFT");
		dominiosValidos.add("MDA");
		dominiosValidos.add("RRE");
		List retorno = this.getDomainValueService().findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
		return retorno;
	}

	/**
	 * Busca a filial baseado no documento de serviço
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findLookupFilialByDocumentoServico(Map criteria) {
		FilterList filter = new FilterList(filialService.findLookup(criteria)) {
			public Map filterItem(Object item) {
				Filial filial = (Filial)item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idFilial", filial.getIdFilial());
				typedFlatMap.put("sgFilial", filial.getSgFilial());
				typedFlatMap.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
				return typedFlatMap;
			}
		};
		return (List)filter.doFilter();
	}


	
	/**
	 * FindLookup para filial do tipo de DoctoServico Escolhido.
	 */ 
	@SuppressWarnings("rawtypes")
	public List findLookupServiceDocumentFilialCTR(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	@SuppressWarnings("rawtypes")
	public List findLookupServiceDocumentFilialCRT(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	@SuppressWarnings("rawtypes")
	public List findLookupServiceDocumentFilialNFT(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}
	
	@SuppressWarnings("rawtypes")
	public List findLookupServiceDocumentFilialMDA(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}
	
	@SuppressWarnings("rawtypes")
	public List findLookupServiceDocumentFilialRRE(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	/**
	 * Método que valida se o destino está dentro da Rota de Ida e Volta do Controle de Carga.
	 * @param parameter
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map validaDestinoParaRotaIdaVolta(Map criteria) {
		Map map = new HashMap();
		String tpPreManifesto = (String)criteria.get("tpPreManifesto");
		String tpControleCarga = (String)criteria.get("tpControleCarga");
		Long idFilialDestino = (Long)criteria.get("idFilialDestino");
		Long idControleCarga = (Long)criteria.get("idControleCarga");
		Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
		if (tpControleCarga.equals("V")) { 
			List listManifesto = this.getManifestoService().findManifestoByIdControleCarga(idControleCarga, null, null, null);
			for (Iterator iter = listManifesto.iterator(); iter.hasNext();) {
				Manifesto manifesto = (Manifesto) iter.next();
				if ( manifesto.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialSessao) && 
						manifesto.getFilialByIdFilialDestino().getIdFilial().equals(idFilialDestino) &&
						manifesto.getTpStatusManifesto().getValue().equals("PM") ) {
					if (manifesto.getTpManifestoViagem().getValue().equals("RV")) {
						if (tpPreManifesto.equals("RV")) {
							throw new BusinessException("LMS-05158");
						} else {
							throw new BusinessException("LMS-05159");
						}
					} else if (tpPreManifesto.equals("RV")) {
						throw new BusinessException("LMS-05160");
					} else {
						map.put("confirmar", "LMS-05035");
					}
				}
			}

			ControleTrecho controleTrecho = controleTrechoService.findControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(idControleCarga, idFilialSessao, idFilialDestino);
			if (controleTrecho == null) {
				throw new BusinessException("LMS-05071");
			}
			map.put("idControleTrecho", controleTrecho.getIdControleTrecho());

		}

		return map;
	}

	/**
	 * Método que valida se a filial da solicitação de retirada pertence a filial do usuario logado.
	 * Caso verdadeiro, busca os documentos referentes a solicitação de retirada informada.
	 * @param criteria
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void validaSolicitacaoRetiradaAndGetDocumentos(Map criteria) {
		List listManifesto = this.getManifestoService().
							findManifestoByIdSolicitacaoRetirada((Long)criteria.get("idSolicitacaoRetirada"));
		if (!listManifesto.isEmpty()) {
			throw new BusinessException("LMS-05151");
		}

		if (!((String)criteria.get("tpSituacao")).equals("A")) {
			throw new BusinessException("LMS-05135");
		}

		Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
		if (!idFilialSessao.equals(
				(Long)criteria.get("idFilialRetirada"))) {
			throw new BusinessException("LMS-05114");
		}

		List listDocumentosServicoRetirada = solicitacaoRetiradaService.findDocsBySolicitacaoRetirada((Long)criteria.get("idSolicitacaoRetirada")); 

		List localizacoesList = new ArrayList();
		localizacoesList.add("24");
		localizacoesList.add("28");
		localizacoesList.add("34");
		localizacoesList.add("35");
		localizacoesList.add("43");


		for (Iterator iter = listDocumentosServicoRetirada.iterator(); iter.hasNext();) {
			DocumentoServicoRetirada documentoServicoRetirada = (DocumentoServicoRetirada) iter.next();
			DoctoServico doctoServico = documentoServicoRetirada.getDoctoServico();

			if ( !doctoServico.getFilialLocalizacao().getIdFilial().equals(idFilialSessao) && 
					!localizacoesList.contains(doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().toString())	) {
				throw new BusinessException("LMS-05115");
			}

			//verifica ser existe algum volume unitizado
			List<VolumeNotaFiscal> volumeNotaFiscalList = volumeNotaFiscalService.findByIdConhecimentoAndIdLocalizacaoFilial(
									doctoServico.getIdDoctoServico(), idFilialSessao );
			
			for(VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscalList ){
				
				if( localizacoesList.contains(volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().toString()) ){
			
					if( volumeNotaFiscal.getDispositivoUnitizacao() != null ){
						throw new BusinessException("LMS-05319");
					}
					
				}
			}
			
			TypedFlatMap mapItem = new TypedFlatMap();
			mapItem.put("idDoctoServico", doctoServico.getIdDoctoServico());
			mapItem.put("masterId", criteria.get("idManifesto"));

			savePreManifestoDocumento(mapItem);
		}
	}

	/**
	 * Método que retorna um map com dados da filial a partir do ID.
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map findFilialById(TypedFlatMap criteria) {
		Filial filial = filialService.findById(criteria.getLong("idFilial"));
		Map mapFilial = new HashMap();
		mapFilial.put("idFilial", filial.getIdFilial().toString());
		mapFilial.put("sgFilial", filial.getSgFilial());
		mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());

		return mapFilial;
	}

	/**
	 * Faz a validacao do PCE.
	 *
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map validatePCE(TypedFlatMap criteria) {
		//Busca o pai a partir da sessao...
		MasterEntry entry = getMasterFromSession(criteria.getLong("idManifesto"), true);
		Manifesto manifesto = (Manifesto) entry.getMaster();

		//Busca o itemList a partir da sessao...
		ItemList items = getItemsFromSession(entry, "preManifestoDocumento");
		ItemListConfig itemsConfig = getMasterConfig().getItemListConfig("preManifestoDocumento"); 

		//Inicia a captura dos doctoServico que estao vinculados ao preManifestoDocto da sessao...
		List doctosServicos = new ArrayList();
		for(Iterator iter = items.iterator(manifesto.getIdManifesto(), itemsConfig); iter.hasNext();) {
			PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
			DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(preManifestoDocumento.getDoctoServico().getIdDoctoServico());
			doctosServicos.add(doctoServico);
		}

		TypedFlatMap mapRetorno = new TypedFlatMap();
		mapRetorno.put("codigos", this.getManifestoService().validatePCE(doctosServicos));
		return mapRetorno;
	}

	/**
	 * Carrega informacoes basicas do pre-manifesto.
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map findManifesto(Map criteria) {
		Manifesto manifesto = getManifestoService().findById((Long)criteria.get("idManifesto"));
		
		Map result = new HashMap();
		result.put("from", criteria.get("from"));
		result.put("idManifesto", manifesto.getIdManifesto().toString());
		result.put("tpManifesto", manifesto.getTpManifesto().getValue());
		result.put("tpAbrangencia", manifesto.getTpAbrangencia().getValue());
		result.put("nrPreManifesto", manifesto.getNrPreManifesto());
		result.put("sgFilialPreManifesto", manifesto.getFilialByIdFilialOrigem().getSgFilial());
		result.put("idFilialDestino", manifesto.getFilialByIdFilialDestino().getIdFilial());
		result.put("tpModal", manifesto.getTpModal().getValue());
		result.put("idControleCarga", manifesto.getControleCarga().getIdControleCarga());
		
		// Consignatario...
		if (manifesto.getCliente() != null) { 
			result.put("idClienteConsignatario", manifesto.getCliente().getIdCliente());
			result.put("nrIdentificacaoConsignatario", FormatUtils.formatIdentificacao(manifesto.getCliente().getPessoa()));
			result.put("nmPessoaConsignatario", manifesto.getCliente().getPessoa().getNmPessoa());
		}
		
		if (manifesto.getTpManifesto().getValue().equals("V")) {
			result.put("tpPreManifesto", manifesto.getTpManifestoViagem().getValue());
		} else {
			result.put("tpPreManifesto", manifesto.getTpManifestoEntrega().getValue());
						
			//Destino...
			Filial filial = SessionUtils.getFilialSessao();
			result.put("idFilialDestino", filial.getIdFilial());
			result.put("sgFilialDestino", filial.getSgFilial());
			result.put("nmFilialDestino", filial.getPessoa().getNmFantasia());
			
			//Rota..
			if (manifesto.getControleCarga().getRotaColetaEntrega() != null) {
				result.put("idRotaColetaEntrega", manifesto.getControleCarga().getRotaColetaEntrega().getIdRotaColetaEntrega());
				result.put("nrRota", manifesto.getControleCarga().getRotaColetaEntrega().getNrRota());
				result.put("dsRota", manifesto.getControleCarga().getRotaColetaEntrega().getDsRota());
			}
		}
		
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findNrCapacidadeKg(Map criteria) {		
		Map result = new HashMap();
		BigDecimal nrCapacidadeKg = new BigDecimal(0);
		if((Long) criteria.get("idControleCarga") != null) {
			ControleCarga c1 = controleCargaService.findById((Long) criteria.get("idControleCarga"));
			ControleCarga controleCarga = controleCargaService.findControleCargaByNrControleCargaByIdFilial( c1.getNrControleCarga(), c1.getFilialByIdFilialOrigem().getIdFilial());	
			if(controleCarga.getMeioTransporteByIdTransportado() != null || controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
				if(controleCarga.getMeioTransporteByIdSemiRebocado() != null)
					nrCapacidadeKg = controleCarga.getMeioTransporteByIdSemiRebocado().getNrCapacidadeKg();
				else
					nrCapacidadeKg = controleCarga.getMeioTransporteByIdTransportado().getNrCapacidadeKg();
			}
		}
		result.put("nrCapacidadeKg", nrCapacidadeKg);
	
		return result;
	}
		
	
		
	/**
	 * ########################### FIM DOS MÉTODOS DIVERSOS PARA A TELA	############################
	 */
	

	
	/**
	 * ############################## INICIO DOS MÉTODOS PARA TELA DE DF2 ###########################
	 */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map store(Map bean) {
		// Validações para Pré-Manifesto
		ControleCarga controleCarga = null;
		if (bean.get("idControleCarga") != null) {
			Long idControleCarga = (Long)bean.get("idControleCarga");
			controleCargaService.validateControleCargaByInclusaoDocumentos(idControleCarga);
			controleCarga = controleCargaService.findById(idControleCarga);
			if (controleCarga.getTpControleCarga().getValue().equals("V")) {
				if (((String)bean.get("tpPreManifesto")).equals("RV")) {

					// LMS-1991
					Filial filial = new Filial();
					if(bean.get("idFilialOrigem") != null) {
						filial = filialService.findById((Long)bean.get("idFilialOrigem"));
					} else {
						filial = filialService.findById((Long)SessionUtils.getFilialSessao().getIdFilial()); 							
					}			
					
					if(filial.getBlGeraContratacaoRetornoVazio() == Boolean.FALSE){
						
					if (controleCarga.getMeioTransporteByIdTransportado() != null) {
						if (controleCarga.getMeioTransporteByIdTransportado().getTpVinculo().getValue().equals("E")) {
							throw new BusinessException("LMS-05036");
						}
					} else {
						throw new BusinessException("LMS-05111");
					}
					
					if (controleCarga.getTpRotaViagem() != null && 
							!controleCarga.getTpRotaViagem().getValue().equals("EX")) {
						throw new BusinessException("LMS-05037");
					}
						
				}
					
				
			}
		}
		}
		
		Long idManifesto = StringUtils.isBlank((String)bean.get("idManifesto")) ? null : 
												Long.valueOf((String)bean.get("idManifesto"));
		MasterEntry entry = getMasterFromSession(idManifesto, true);
		Manifesto manifesto = (Manifesto) entry.getMaster();
		
		if("EP".equals(bean.get("tpPreManifesto"))){
			manifesto.setCliente(null);
		}
		
		manifesto.setIdManifesto(idManifesto);
		Long nrPreManifesto = null;
		if (bean.get("nrPreManifesto") instanceof Long) {
			nrPreManifesto = (Long) bean.get("nrPreManifesto");
		} else { 
			nrPreManifesto = StringUtils.isBlank((String)bean.get("nrPreManifesto")) ? null : Long.valueOf((String)bean.get("nrPreManifesto"));
		}

		manifesto.setNrPreManifesto(nrPreManifesto);
		manifesto.setDhGeracaoPreManifesto(JTDateTimeUtils.getDataHoraAtual());
		manifesto.setTpManifesto(new DomainValue((String)bean.get("tpManifesto")));
		if (((String)bean.get("tpManifesto")).equals("V")) {
			manifesto.setTpManifestoViagem(new DomainValue((String)bean.get("tpPreManifesto")));
		} else if (((String)bean.get("tpManifesto")).equals("E")) {
			manifesto.setTpManifestoEntrega(new DomainValue((String)bean.get("tpPreManifesto")));
		}
		manifesto.setTpModal(new DomainValue((String)bean.get("tpModal")));
		manifesto.setTpAbrangencia(new DomainValue((String)bean.get("tpAbrangencia")));
		if (bean.get("tpStatusManifesto") != null) {
			manifesto.setTpStatusManifesto(new DomainValue((String)bean.get("tpStatusManifesto")));
		} else {
			manifesto.setTpStatusManifesto(new DomainValue("PM"));
		}
		manifesto.setObManifesto((String)bean.get("obManifesto"));
		manifesto.setBlBloqueado(Boolean.FALSE);
		
		if(bean.get("idFilialOrigem") != null) {
			manifesto.setFilialByIdFilialOrigem(filialService.findById((Long)bean.get("idFilialOrigem")));
		} else {
			manifesto.setFilialByIdFilialOrigem(SessionUtils.getFilialSessao());
		}
		manifesto.setFilialByIdFilialDestino(filialService.findById((Long)bean.get("idFilialDestino")));
		manifesto.setControleCarga(controleCarga);

		if (bean.get("idControleTrecho") != null) {
			manifesto.setControleTrecho(controleTrechoService.findById((Long)bean.get("idControleTrecho")));
		}
		
		manifesto.setMoeda(SessionUtils.getMoedaSessao());
				
		if (bean.get("idSolicitacaoRetirada") != null) {
			manifesto.setSolicitacaoRetirada(solicitacaoRetiradaService.findById((Long)bean.get("idSolicitacaoRetirada")));
		}
		
		if (bean.get("idClienteConsignatario") != null) {
			manifesto.setCliente(clienteService.findById((Long)bean.get("idClienteConsignatario")));
		}
			
		//itens do PreManifestoDocumento
		ItemList itemsPremanifestoDocumento = getItemsFromSession(entry, "preManifestoDocumento");
		ItemListConfig itemsConfigPreManifestoDocumento = getMasterConfig().getItemListConfig("preManifestoDocumento");
		
		
		// itens do PreManifestoVolume
		ItemList itemsPremanifestoVolume = getItemsFromSession(entry, "preManifestoVolume");
		ItemListConfig itemsConfigPreManifestoVolume  = getMasterConfig().getItemListConfig("preManifestoVolume");
		
	
		// Faz a conversão de valores do DoctoServico para o valor da moeda do usuário logado.
		BigDecimal vlTotalManifesto = BigDecimalUtils.ZERO;
		BigDecimal psTotalManifesto = BigDecimalUtils.ZERO;
		BigDecimal psTotalAforadoManifesto = BigDecimalUtils.ZERO;
		for(Iterator iter = itemsPremanifestoDocumento.iterator(manifesto.getIdManifesto(), itemsConfigPreManifestoDocumento); iter.hasNext();) {
			PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
			DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(preManifestoDocumento.getDoctoServico().getIdDoctoServico());
			doctoServico.setDsCodigoBarras(preManifestoDocumento.getDoctoServico().getDsCodigoBarras());
			preManifestoDocumento.setDoctoServico(doctoServico);
			
			
			if (doctoServico.getVlMercadoria() != null) {
				BigDecimal vlTotalManifestoConvertido = conversaoMoedaService.findConversaoMoeda(
					 doctoServico.getPaisOrigem().getIdPais(),
					 doctoServico.getMoeda().getIdMoeda(),
					 SessionUtils.getPaisSessao().getIdPais(),
					 SessionUtils.getMoedaSessao().getIdMoeda(),
					 JTDateTimeUtils.getDataAtual(),
					 doctoServico.getVlMercadoria());
			
				vlTotalManifesto = vlTotalManifesto.add(vlTotalManifestoConvertido);
			}
			
			if (doctoServico.getPsReal() != null) {
				psTotalManifesto = psTotalManifesto.add(doctoServico.getPsReal());
			}
			
			if (doctoServico.getPsAforado() != null) {
				psTotalAforadoManifesto = psTotalAforadoManifesto.add(doctoServico.getPsAforado());
			}

			Awb awb = awbService.findPreAwbByIdDoctoServicoAndFilialOrigem(doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial()); 
			preManifestoDocumento.setAwb(awb);
		}
		
		manifesto.setVlTotalManifesto(vlTotalManifesto);
		manifesto.setPsTotalManifesto(psTotalManifesto);
		manifesto.setPsTotalAforadoManifesto(psTotalAforadoManifesto);
		
		TypedFlatMap mapBeanStored = this.getManifestoService().storeAll(manifesto, itemsPremanifestoDocumento, itemsConfigPreManifestoDocumento,
				itemsPremanifestoVolume, itemsConfigPreManifestoVolume, getConferenciaLinhaSession());
		
		Map mapReturn = new HashMap();
		if (mapBeanStored.containsKey("errorMessage")) {
			mapReturn.put("idPreManifestoDocumento",mapBeanStored.get("idPreManifestoDocumento"));
			mapReturn.put("errorMessage",mapBeanStored.get("errorMessage"));
			mapReturn.put("documento",mapBeanStored.get("documento"));
			
			return mapReturn;
		}
		
		itemsPremanifestoDocumento.resetItemsState(); 
		itemsPremanifestoVolume.resetItemsState();
		updateMasterInSession(entry);
		
		mapReturn.put("idManifesto", ((Long)mapBeanStored.get("idManifesto")).toString());
		mapReturn.put("idFilialOrigem", mapBeanStored.get("filialByIdFilialOrigem.idFilial"));
		mapReturn.put("sgFilialOrigem", mapBeanStored.get("filialByIdFilialOrigem.sgFilial"));
		mapReturn.put("nrPreManifesto", mapBeanStored.get("nrPreManifesto"));
		mapReturn.put("dhGeracaoPreManifesto", mapBeanStored.get("dhGeracaoPreManifesto"));
		mapReturn.put("tpManifesto", mapBeanStored.get("tpManifesto"));
		mapReturn.put("tpPreManifesto", mapBeanStored.get("tpPreManifesto"));
		
		return mapReturn;
	}
	
	
	/**
	 * Salva a referencia do objeto Master na sessão.
	 * não devem ser inicializadas as coleções que representam os filhos
	 * já que o usuário pode vir a não utilizar a aba de filhos, evitando assim
	 * a carga desnecessária de objetos na sessão e a partir do banco de dados.
	 * 
	 * @param id
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findById(java.lang.Long id) {
		Object masterObj = this.getManifestoService().findManifestoById(id);
		
		putMasterInSession(masterObj);
		Manifesto manifesto = (Manifesto) masterObj;
		
		Map mapManifesto = new HashMap();
		
		mapManifesto.put("idManifesto", manifesto.getIdManifesto());
		mapManifesto.put("tpStatusManifesto", manifesto.getTpStatusManifesto().getValue());
		mapManifesto.put("idFilialOrigem", manifesto.getFilialByIdFilialOrigem().getIdFilial());
		mapManifesto.put("sgFilialOrigem", manifesto.getFilialByIdFilialOrigem().getSgFilial());
		mapManifesto.put("nrPreManifesto", manifesto.getNrPreManifesto());
		mapManifesto.put("dhGeracaoPreManifesto", manifesto.getDhGeracaoPreManifesto());
		mapManifesto.put("tpManifesto", manifesto.getTpManifesto().getValue());
		mapManifesto.put("sgFilialNrPreManifesto", FormatUtils.formatSgFilialWithLong(
										manifesto.getFilialByIdFilialOrigem().getSgFilial(), manifesto.getNrPreManifesto())); 
				
		if (manifesto.getTpManifestoViagem() != null) {
			mapManifesto.put("tpPreManifesto", manifesto.getTpManifestoViagem().getValue());
						
			if (manifesto.getControleCarga() != null) {
				if (manifesto.getControleCarga().getTpRotaViagem() != null
						&& ( manifesto.getControleCarga().getTpRotaViagem().getValue().equals("EX")
								|| manifesto.getControleCarga().getTpRotaViagem().getValue().equals("EC")
						)
				) {
					if (manifesto.getControleCarga().getRotaIdaVolta() != null){
						mapManifesto.put("idRotaIdaVolta", manifesto.getControleCarga().getRotaIdaVolta().getIdRotaIdaVolta());
						RotaIdaVolta rotaIdaVolta = rotaIdaVoltaService.findById(manifesto.getControleCarga().getRotaIdaVolta().getIdRotaIdaVolta());
						mapManifesto.put("nrRotaIdaVolta", rotaIdaVolta.getNrRota());
						mapManifesto.put("dsRotaIdaVolta", rotaIdaVolta.getRota().getDsRota());
					}
				} else {
					if (manifesto.getControleCarga().getRota() != null) {
						mapManifesto.put("idRotaIdaVolta", manifesto.getControleCarga().getRota().getIdRota());
						mapManifesto.put("nrRotaIdaVolta", null);
						mapManifesto.put("dsRotaIdaVolta", manifesto.getControleCarga().getRota().getDsRota());
					}
				}
			}
			
		} else if (manifesto.getTpManifestoEntrega() != null) {
			mapManifesto.put("tpPreManifesto", manifesto.getTpManifestoEntrega().getValue());
			if (manifesto.getControleCarga() != null && manifesto.getControleCarga().getRotaColetaEntrega() != null){
				mapManifesto.put("idRotaColetaEntrega", manifesto.getControleCarga().getRotaColetaEntrega().getIdRotaColetaEntrega());
				RotaColetaEntrega rotaColetaEntrega = rotaColetaEntregaService.findById(manifesto.getControleCarga().getRotaColetaEntrega().getIdRotaColetaEntrega());
				mapManifesto.put("nrRotaColetaEntrega", rotaColetaEntrega.getNrRota());
				mapManifesto.put("dsRotaColetaEntrega", rotaColetaEntrega.getDsRota());
			}
		}
		
		if (manifesto.getTpModal() != null) {
			mapManifesto.put("tpModal", manifesto.getTpModal().getValue());
		}

		if (manifesto.getTpAbrangencia() != null) {
			mapManifesto.put("tpAbrangencia", manifesto.getTpAbrangencia().getValue());
		}
		
		if (manifesto.getControleCarga() != null) {
			// Verifica se o Controle de Carga está Cancelado.
			if (manifesto.getControleCarga().getTpStatusControleCarga().getValue().equals("CA")) {
				mapManifesto.put("desabilitaControleCarga", Boolean.TRUE);
			} else {
				mapManifesto.put("desabilitaControleCarga", Boolean.FALSE);
			}
			
			// Verifica se o Controle de Carga stá com o carregamento finalizado para a filial do usuario logado.
			List listEventoControleCarga = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(
				SessionUtils.getFilialSessao().getIdFilial(), 
				manifesto.getControleCarga().getIdControleCarga(),
				"FC"
			);
			if (!listEventoControleCarga.isEmpty()) {
				mapManifesto.put("desabilitaControleCarga", Boolean.TRUE);
			} else {
				mapManifesto.put("desabilitaControleCarga", Boolean.FALSE);
			}
			
			mapManifesto.put("tpControleCarga", manifesto.getControleCarga().getTpControleCarga().getValue());
			mapManifesto.put("idFilialOrigemControleCarga", manifesto.getControleCarga().getFilialByIdFilialOrigem().getIdFilial());
			mapManifesto.put("sgFilialOrigemControleCarga", manifesto.getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
			mapManifesto.put("nmFilialOrigemControleCarga", manifesto.getControleCarga().getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
			mapManifesto.put("idControleCarga", manifesto.getControleCarga().getIdControleCarga());
			mapManifesto.put("nrControleCarga", manifesto.getControleCarga().getNrControleCarga());
			mapManifesto.put("blEntregaDireta", manifesto.getControleCarga().getBlEntregaDireta());
		}
		
		mapManifesto.put("tpStatusManifesto", manifesto.getTpStatusManifesto().getValue());
		mapManifesto.put("idFilialDestino", manifesto.getFilialByIdFilialDestino().getIdFilial());
		mapManifesto.put("sgFilialDestino", manifesto.getFilialByIdFilialDestino().getSgFilial());
		Pessoa pessoaFilial = pessoaService.findById(manifesto.getFilialByIdFilialDestino().getIdFilial());
		mapManifesto.put("nmFilialDestino", pessoaFilial.getNmFantasia());
		mapManifesto.put("obManifesto", manifesto.getObManifesto());

		if(manifesto.getSolicitacaoRetirada() != null) {
			mapManifesto.put("idSolicitacaoRetirada", manifesto.getSolicitacaoRetirada().getIdSolicitacaoRetirada());
			mapManifesto.put("idFilialRetirada", manifesto.getSolicitacaoRetirada().getFilial().getIdFilial());
			mapManifesto.put("sgFilialRetirada", manifesto.getSolicitacaoRetirada().getFilial().getSgFilial());
			mapManifesto.put("nmFilialRetirada", manifesto.getSolicitacaoRetirada().getFilial().getPessoa().getNmFantasia());
			mapManifesto.put("nrSolicitacaoRetirada", manifesto.getSolicitacaoRetirada().getNrSolicitacaoRetirada());
			mapManifesto.put("tpSituacao", manifesto.getSolicitacaoRetirada().getTpSituacao().getValue());
		}
		
		if (manifesto.getCliente() != null) {
			mapManifesto.put("idClienteConsignatario", manifesto.getCliente().getIdCliente());
			mapManifesto.put("idPessoa", manifesto.getCliente().getPessoa().getIdPessoa());
			mapManifesto.put("tpIdentificacao", manifesto.getCliente().getPessoa().getTpIdentificacao().getValue());
			mapManifesto.put("nrIdentificacao", manifesto.getCliente().getPessoa().getNrIdentificacao());
			mapManifesto.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(manifesto.getCliente().getPessoa()));
			mapManifesto.put("nmPessoa", manifesto.getCliente().getPessoa().getNmPessoa());
		}
				
		return mapManifesto;
	}

	
	/**
	 * Remoção de um conjunto de registros Master.
	 * 
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		this.getManifestoService().removeByIds(ids);
	}

	/**
	 * Remoção de um registro Master.
	 */	
	public void removeById(java.lang.Long id) {
		this.getManifestoService().removeById(id);
		newMaster();
	} 
	
	/**
	 * Salva um item Descrição Padrão na sessão.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> savePreManifestoDocumento(Map parameters) {
		
		String tpManifesto = (String) parameters.get("tpManifesto");
		Long idFilialDestinoManifesto = (Long) parameters.get("idFilialDestino");
		
		Map<String, Object> resultMapped = new HashMap<String, Object>();
		final List<Long> listIdsDocumentos = new ArrayList();
		/** Lista de IDs */
		if (parameters.get("ids") != null) {
			Long[] ids = (Long[])parameters.get("ids");
			for (int i = 0; i < ids.length; i++) {
				listIdsDocumentos.add(ids[i]);
			}
		/** Único Documento */
		} else if (parameters.get("idDoctoServico") != null) {
			Long masterId = getMasterId(parameters);
			List idsDoctoServico = getIdsDoctoServicoFromSession(masterId);

			for (Iterator iter = idsDoctoServico.iterator(); iter.hasNext();) {
				Long idDoctoServico = (Long) iter.next();
				if (idDoctoServico.equals((Long)parameters.get("idDoctoServico"))) {
					throw new BusinessException("LMS-05097");
				}
			}
			final Long idDoctoServico = MapUtilsPlus.getLong(parameters,"idDoctoServico");
			listIdsDocumentos.add(idDoctoServico);
			
			PreManifestoDocumento preManifestoDocumento = new PreManifestoDocumento();
			preManifestoDocumento.setIdPreManifestoDocumento(-1L);
			resultMapped = convertPreManifestoDocumentoBeanResultToMap(preManifestoDocumento, idDoctoServico, (String) parameters.get("nrCodigoBarras"));
		}

		/** Valida informações de Reentrega */
		for (Long idDoctoServico : listIdsDocumentos) {
			Map mapPreManifestoDocumento = new HashMap();
		
			/*CQPRO00023462 - Valida informações de reentrega*/
			DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
			if(!ConstantesExpedicao.MINUTA_DESPACHO_ACOMPANHAMENTO.equals(doctoServico.getTpDocumentoServico().getValue())) {
				preManifestoDocumentoService.validateReentregaDocumento(idDoctoServico);
			}
			
			// LMS-2337
			if (idFilialDestinoManifesto != null && tpManifesto != null) {
				Boolean validateDocumentoServicoManifesto = preManifestoDocumentoService.validateDocumentoServicoManifesto(idFilialDestinoManifesto, tpManifesto, idDoctoServico);
				if (!validateDocumentoServicoManifesto) {
					throw new BusinessException("LMS-05352");
				}
			}
			
			mapPreManifestoDocumento.put("masterId", parameters.get("masterId"));
			mapPreManifestoDocumento.put("idDoctoServico", idDoctoServico);
			mapPreManifestoDocumento.put("idAwb", parameters.get("idAwb"));
			mapPreManifestoDocumento.put("dsCodigoBarras", resultMapped.get("dsCodigoBarras"));
			mapPreManifestoDocumento.put("preAwb", resultMapped.get("preAwb"));
			addConferenciaLinhaSession(idDoctoServico);
			Object obj = saveItemInstance(mapPreManifestoDocumento, "preManifestoDocumento");
			
			/*
			 * LMS-4807 - Correção do erro da exclusão, o que acontencia é que
			 * retornava como id artificial para o registro da grid sempre o valor -1 (através do resultMapped), 
			 * e era adicionado na lista da sessão o valor de id correto (-1, -2, etc). 
			 * Dessa forma ao clicar no botão para excluir era enviado um valor de id (-1) e 
			 * na lista para exclusão existiam outros valores, fazendo com que o registro não fosse encontrado,
			 * e por consequencia, não fosse excluído.
			 * 
			 * Isso tudo foi feito, a princípio, para otimizar a consulta da tela, conforme descrito na LMS-874.
			 */
			/** Único Documento */
			if (parameters.get("idDoctoServico") != null && obj instanceof Long){
				resultMapped.put("idPreManifestoDocumento", (Long) obj);
		}
			
		}
		
		return resultMapped;
	} 


	public List<Long> getConferenciaLinhaSession() {
		return conferenciaEmLinha;
	}

	public void addConferenciaLinhaSession(Long idDoctoServico) {
		conferenciaEmLinha.add(idDoctoServico);
	}

	/**
	 * salva um PreManifestoVolume na sessão
	 * @param criteria
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void savePreManifestoVolume( Map criteria ){
		
		if (criteria.get("ids") != null) {
			Long[] idVolumeNotaFiscal = (Long[])criteria.get("ids");
			for (int i = 0; i < idVolumeNotaFiscal.length; i++) {
				
				Map mapPreManifestoVolume = new HashMap();
				mapPreManifestoVolume.put("masterId", criteria.get("masterId"));
				mapPreManifestoVolume.put("idVolumeNotaFiscal", idVolumeNotaFiscal[i]);
				saveItemInstance(mapPreManifestoVolume, "preManifestoVolume");
	
			}
		} 
	}
		

	/**
	 * Persiste o Pre-manifesto, para a tela de carregamentoDescargaDocumentos
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Serializable savePreManifestoDocumentoToCarregamento(Map parameters) {
		List listIdsDocumentos = new ArrayList();

		if (parameters.get("ids") != null) {
			Long[] ids = (Long[])parameters.get("ids");
			for (int i = 0; i < ids.length; i++) {
				listIdsDocumentos.add(ids[i]);
			}
		} else if (parameters.get("idDoctoServico") != null) {
			listIdsDocumentos.add((Long)parameters.get("idDoctoServico"));
		}

		Long idManifesto = Long.parseLong((String)parameters.get("idManifesto"));
		preManifestoDocumentoService.storePreManifestoDocumentoToCarregamento(idManifesto, listIdsDocumentos);
		
		return null;
	}

	/**
	 * Remove uma lista de registros items.
	 *  
	 * @param ids ids dos desciçoes item a serem removidos.
	 */
	@SuppressWarnings("rawtypes")
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsPreManifestoDocumento(List ids) {
		super.removeItemByIds(ids, "preManifestoDocumento");
	} 
	
	/**
	 * Remove uma lista de registros items.
	 *  
	 * @param ids ids dos desciçoes item a serem removidos.
	 */
	@SuppressWarnings("rawtypes")
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsPreManifestoVolume(List ids) {
		super.removeItemByIds(ids, "preManifestoVolume");
	} 
	
	/**
	 * Remove uma lista de documentos do pre-manifesto.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void removeDocumentosPreManifesto(Map criteria) {
		Long masterId = getMasterId(criteria);
		MasterEntry masterEntry = getMasterFromSession(masterId, true);
		ItemList itemList = masterEntry.getItems("preManifestoDocumento");
		ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("preManifestoDocumento");
		
		List listIds = new ArrayList();
		for (Iterator iter = itemList.iterator(masterId, itemListConfig); iter.hasNext();) {
			PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
			listIds.add(preManifestoDocumento.getIdPreManifestoDocumento());
		}
		
		if(!listIds.isEmpty()) {
			this.removeByIdsPreManifestoDocumento(listIds);
		}
	}

	
	/**
	 * Remove uma lista de volumes do pre-manifesto.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void removeVolumesPreManifesto(Map criteria) {
		Long masterId = getMasterId(criteria);
		MasterEntry masterEntry = getMasterFromSession(masterId, true);
		ItemList itemList = masterEntry.getItems("preManifestoVolume");
		ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("preManifestoVolume");
		
		List listIds = new ArrayList();
		for (Iterator iter = itemList.iterator(masterId, itemListConfig); iter.hasNext();) {
			PreManifestoVolume preManifestoVolume = (PreManifestoVolume) iter.next();
			listIds.add(preManifestoVolume.getIdPreManifestoVolume());
		}
		
		if(!listIds.isEmpty()) {
			this.removeByIdsPreManifestoVolume(listIds);
		}
	}

	
	/**
	 * Valida remoção de lista de documentos do pre-manifesto.
	 */
	@SuppressWarnings("rawtypes")
	public void validateRemoveDocumentosPreManifesto(Map criteria) {
		Long masterId = getMasterId(criteria);
		if (masterId != null) {
			MasterEntry masterEntry = getMasterFromSession(masterId, true);
			ItemList itemList = masterEntry.getItems("preManifestoDocumento");
			Long[] ids = (Long[])criteria.get("ids");
			
			if (itemList.size() == ids.length) {
				throw new BusinessException("LMS-05052");
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultSetPage findPaginatedPreManifestoVolume(Map criteria) {
		ResultSetPage rspPreManifestoVolume = findPaginatedItemList(criteria, "preManifestoVolume");
	
		List listPreManifestoVolume = new ArrayList();
		for(int i=0; i< rspPreManifestoVolume.getList().size(); i++) {
			PreManifestoVolume preManifestoVolume = (PreManifestoVolume) rspPreManifestoVolume.getList().get(i);
			DoctoServico doctoServico = conhecimentoService.findConhecimentoByIdVolumeNotaFiscal(preManifestoVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal());
	
			String tpDocumentoServico = doctoServico.getTpDocumentoServico().getValue();

			Map mapPreManifestoVolume = new HashMap();
			mapPreManifestoVolume.put("idPreManifestoVolume", preManifestoVolume.getIdPreManifestoVolume());
			mapPreManifestoVolume.put("nrSequencia", preManifestoVolume.getVolumeNotaFiscal().getNrSequencia());
			mapPreManifestoVolume.put("qtdVolumes", doctoServico.getQtVolumes());

			mapPreManifestoVolume.put("idDoctoServico", doctoServico.getIdDoctoServico());
			mapPreManifestoVolume.put("sgFilialOrigem", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
			mapPreManifestoVolume.put("tpDocumentoServico", doctoServico.getTpDocumentoServico());
			mapPreManifestoVolume.put("nrDoctoServico", doctoServico.getNrDoctoServico());
			
			if (tpDocumentoServico.equals(ConstantesExpedicao.CONHECIMENTO_NACIONAL)) {
				mapPreManifestoVolume.put("dvDoctoServico", ((Conhecimento)doctoServico).getDvConhecimento());
			}

			if (preManifestoVolume.getVolumeNotaFiscal().getLocalizacaoMercadoria() != null) {
				mapPreManifestoVolume.put("idLocalizacaoMercadoria", preManifestoVolume.getVolumeNotaFiscal().getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());
				mapPreManifestoVolume.put("dsLocalizacaoMercadoria", preManifestoVolume.getVolumeNotaFiscal().getLocalizacaoMercadoria().getDsLocalizacaoMercadoria());
			}
			
			listPreManifestoVolume.add(mapPreManifestoVolume);
		}
		
		rspPreManifestoVolume.setList(listPreManifestoVolume);

		return rspPreManifestoVolume;
	}
	
	
	/**
	 * FindPaginated para grid de PreManifestoDocumento
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultSetPage findPaginatedPreManifestoDocumento(Map criteria) {
		ResultSetPage rspPreManifestoDocumento = findPaginatedItemList(criteria, "preManifestoDocumento");
		
		List listPreManifestoDocumento = new ArrayList();
		for(int i=0; i< rspPreManifestoDocumento.getList().size(); i++) {
			/*Pré manifesto*/
			final PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) rspPreManifestoDocumento.getList().get(i);
			
			Map mapPreManifestoDocumento = convertPreManifestoDocumentoBeanResultToMap(preManifestoDocumento, preManifestoDocumento.getDoctoServico().getIdDoctoServico(), null);
			listPreManifestoDocumento.add(mapPreManifestoDocumento);
		}
		rspPreManifestoDocumento.setList(listPreManifestoDocumento);
		return rspPreManifestoDocumento;
	}
			
	/**
	 * Método responsável por converter dados do Bean para Map da Table
	 * @author André Valadas
	 * 
	 * @param preManifestoDocumento
	 * @param idDoctoServico
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map convertPreManifestoDocumentoBeanResultToMap(final PreManifestoDocumento preManifestoDocumento, final Long idDoctoServico, String nrCodigoBarras) {
		final DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico);
		
		String sinalizar = "";
		if(dadosComplementoService.executeDadosComplementoSPP(doctoServico,ConstantesExpedicao.CNPJ_NATURA,ConstantesExpedicao.COD_CONF) != null){
			sinalizar = "*";
		}		
			
		final Map mapPreManifestoDocumento = new HashMap();
		mapPreManifestoDocumento.put("nrDoctoServico", doctoServico.getNrDoctoServico());
		mapPreManifestoDocumento.put("idPreManifestoDocumento", preManifestoDocumento.getIdPreManifestoDocumento());
		mapPreManifestoDocumento.put("nrOrdem", preManifestoDocumento.getNrOrdem());
		mapPreManifestoDocumento.put("idDoctoServico", doctoServico.getIdDoctoServico());
		final Map descriptionDocumentoServico = new HashMap();
		descriptionDocumentoServico.put("description", doctoServico.getTpDocumentoServico().getDescription().getValue());
		mapPreManifestoDocumento.put("tpDocumentoServico", descriptionDocumentoServico);
	    mapPreManifestoDocumento.put("dsCodigoBarras", nrCodigoBarras);
		mapPreManifestoDocumento.put("qtVolumes", doctoServico.getQtVolumes());
		mapPreManifestoDocumento.put("psReal", doctoServico.getPsReal());
		mapPreManifestoDocumento.put("vlMercadoria", doctoServico.getVlMercadoria());
		mapPreManifestoDocumento.put("vlTotalDocServico", doctoServico.getVlTotalDocServico());
		mapPreManifestoDocumento.put("dtPrevEntrega", doctoServico.getDtPrevEntrega());
		mapPreManifestoDocumento.put("dhEmissao", doctoServico.getDhEmissao());
		mapPreManifestoDocumento.put("idFilialOrigem", doctoServico.getFilialByIdFilialOrigem().getIdFilial());
		mapPreManifestoDocumento.put("sgFilialOrigem", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
		mapPreManifestoDocumento.put("nmFantasiaFilialOrigem", doctoServico.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());

		final String tpDocumentoServico = doctoServico.getTpDocumentoServico().getValue();
			if (tpDocumentoServico.equals(ConstantesExpedicao.CONHECIMENTO_NACIONAL)) {
				Integer dvCTRC = IntegerUtils.defaultInteger(((Conhecimento)doctoServico).getDvConhecimento());
				mapPreManifestoDocumento.put("dvDoctoServico", dvCTRC.toString().concat(sinalizar));
			}
			
			if (doctoServico.getFilialByIdFilialDestino() != null) {
				mapPreManifestoDocumento.put("idFilialDestino", doctoServico.getFilialByIdFilialDestino().getIdFilial());
				mapPreManifestoDocumento.put("sgFilialDestino", doctoServico.getFilialByIdFilialDestino().getSgFilial());
				mapPreManifestoDocumento.put("nmFantasiaFilialDestino", doctoServico.getFilialByIdFilialDestino().getPessoa().getNmFantasia());
			}
			
			if (doctoServico.getMoeda() != null) {
				mapPreManifestoDocumento.put("idMoeda", doctoServico.getMoeda().getIdMoeda());
				mapPreManifestoDocumento.put("siglaSimbolo", doctoServico.getMoeda().getSiglaSimbolo());
				mapPreManifestoDocumento.put("siglaSimbolo2", doctoServico.getMoeda().getSiglaSimbolo());
			}
			
 			if(preManifestoDocumento.getAwb() != null) {
				Awb awb = awbService.findById(preManifestoDocumento.getAwb().getIdAwb());
				String ciaAerea = awb.getCiaFilialMercurio().getEmpresa().getPessoa().getNmPessoa();
				mapPreManifestoDocumento.put("ciaAerea", ciaAerea);
				mapPreManifestoDocumento.put("preAwb", awb.getIdAwb());
				mapPreManifestoDocumento.put("nrTextoAwb", awb.getIdAwb().toString());
			}else{ 
				Awb awb = awbService.findPreAwbByIdDoctoServicoAndFilialOrigem(doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial());
				if(awb != null){
					String ciaAerea = awb.getCiaFilialMercurio().getEmpresa().getPessoa().getNmPessoa();
					mapPreManifestoDocumento.put("ciaAerea", ciaAerea);
					mapPreManifestoDocumento.put("preAwb", awb.getIdAwb());
					mapPreManifestoDocumento.put("nrTextoAwb", awb.getIdAwb().toString());
				}
			}
			
			if (doctoServico.getServico() != null) {
				mapPreManifestoDocumento.put("idServico", doctoServico.getServico().getIdServico());
				mapPreManifestoDocumento.put("sgServico", doctoServico.getServico().getSgServico());
				mapPreManifestoDocumento.put("dsServico", doctoServico.getServico().getDsServico());

			final Map descriptionModal = new HashMap();
			final DomainValue tpModal = doctoServico.getServico().getTpModal();
			if(tpModal != null) {
				descriptionModal.put("description", tpModal.getDescription().getValue());
				mapPreManifestoDocumento.put("tpModal", descriptionModal);
			}

			final Map descriptionAbrangencia = new HashMap();
			final DomainValue tpAbrangencia = doctoServico.getServico().getTpAbrangencia();
			if(tpAbrangencia != null) {
				descriptionAbrangencia.put("description", tpAbrangencia.getDescription().getValue());
				mapPreManifestoDocumento.put("tpAbrangencia", descriptionAbrangencia);
			}
				if (doctoServico.getServico().getTipoServico() != null) {
					mapPreManifestoDocumento.put("blPriorizar", doctoServico.getServico().getTipoServico().getBlPriorizar());
				}
			}
			
			if (doctoServico.getLocalizacaoMercadoria() != null) {
				mapPreManifestoDocumento.put("idLocalizacaoMercadoria", doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());
				mapPreManifestoDocumento.put("dsLocalizacaoMercadoria", doctoServico.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria());
			}
			
			if (doctoServico.getClienteByIdClienteRemetente() != null) {
				mapPreManifestoDocumento.put("idClienteRemetente", doctoServico.getClienteByIdClienteRemetente().getIdCliente());
				mapPreManifestoDocumento.put("nmPessoaRemetente", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
			}
			
			if (doctoServico.getClienteByIdClienteDestinatario() != null) {
				mapPreManifestoDocumento.put("idClienteDestinatario", doctoServico.getClienteByIdClienteDestinatario().getIdCliente());
				mapPreManifestoDocumento.put("nmPessoaDestinatario", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
			}
			
		return mapPreManifestoDocumento;
	}

		

	/**
	 * GetRowCount para grid de PreManifestoDocumento
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Integer getRowCountPreManifestoDocumento(Map criteria) {
		return getRowCountItemList(criteria, "preManifestoDocumento");
	}
	
	
	/**
	 * GetRowCount para grid de PreManifestoDocumento
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Integer getRowCountPreManifestoVolume(Map criteria) {
		return getRowCountItemList(criteria, "preManifestoVolume");
	}
	
	/**
	 * Busca o registro de Pré-Manifesto Documento pelo ID. 
	 * @param key
	 * @return
	 */
	public Object findByIdPreManifestoDocumento(MasterDetailKey key) {
		PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) findItemById(key, "preManifestoDocumento");
		DoctoServico doctoServico = preManifestoDocumento.getDoctoServico();
		
		TypedFlatMap mapPreManifestoDocumento = new TypedFlatMap();
		mapPreManifestoDocumento.put("idPreManifestoDocumento", preManifestoDocumento.getIdPreManifestoDocumento());
		mapPreManifestoDocumento.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
		mapPreManifestoDocumento.put("doctoServico.tpDocumentoServico.description", doctoServico.getTpDocumentoServico().getDescription());
		mapPreManifestoDocumento.put("doctoServico.tpDocumentoServico.value", doctoServico.getTpDocumentoServico().getValue());
		mapPreManifestoDocumento.put("doctoServico.tpDocumentoServico.status", doctoServico.getTpDocumentoServico().getStatus());
		mapPreManifestoDocumento.put("doctoServico.nrDoctoServico", doctoServico.getNrDoctoServico());
		mapPreManifestoDocumento.put("doctoServico.qtVolumes", doctoServico.getQtVolumes());
		mapPreManifestoDocumento.put("doctoServico.psReal", doctoServico.getPsReal());
		mapPreManifestoDocumento.put("doctoServico.vlMercadoria", doctoServico.getVlMercadoria());
		mapPreManifestoDocumento.put("doctoServico.vlTotalDocServico", doctoServico.getVlTotalDocServico());
		mapPreManifestoDocumento.put("doctoServico.dtPrevEntrega", doctoServico.getDtPrevEntrega());
		mapPreManifestoDocumento.put("doctoServico.dhEmissao", doctoServico.getDhEmissao());
		if (doctoServico.getFilialByIdFilialDestino() != null) {
			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialDestino.idFilial", doctoServico.getFilialByIdFilialDestino().getIdFilial());
			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialDestino.sgFilial", doctoServico.getFilialByIdFilialDestino().getSgFilial());
			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialDestino.pessoa.nmFantasia", doctoServico.getFilialByIdFilialDestino().getPessoa().getNmFantasia());
		}
		if (doctoServico.getMoeda() != null) {
			mapPreManifestoDocumento.put("doctoServico.moeda.idMoeda", doctoServico.getMoeda().getIdMoeda());
			mapPreManifestoDocumento.put("doctoServico.moeda.sgMoeda", doctoServico.getMoeda().getSgMoeda());
			mapPreManifestoDocumento.put("doctoServico.moeda.dsSimbolo", doctoServico.getMoeda().getDsSimbolo());
		}
		if (doctoServico.getServico() != null) {
			mapPreManifestoDocumento.put("doctoServico.servico.idServico", doctoServico.getServico().getIdServico());
			mapPreManifestoDocumento.put("doctoServico.servico.dsServico", doctoServico.getServico().getDsServico());
			mapPreManifestoDocumento.put("doctoServico.servico.tpModal.description", doctoServico.getServico().getTpModal().getDescription());
			mapPreManifestoDocumento.put("doctoServico.servico.tpModal.value", doctoServico.getServico().getTpModal().getValue());
			mapPreManifestoDocumento.put("doctoServico.servico.tpModal.status", doctoServico.getServico().getTpModal().getStatus());
			mapPreManifestoDocumento.put("doctoServico.servico.tpAbrangencia.description", doctoServico.getServico().getTpAbrangencia().getDescription());
			mapPreManifestoDocumento.put("doctoServico.servico.tpAbrangencia.value", doctoServico.getServico().getTpAbrangencia().getValue());
			mapPreManifestoDocumento.put("doctoServico.servico.tpAbrangencia.status", doctoServico.getServico().getTpAbrangencia().getStatus());
			if (doctoServico.getServico().getTipoServico() != null) {
				mapPreManifestoDocumento.put("doctoServico.servico.tipoServico.blPriorizar", doctoServico.getServico().getTipoServico().getBlPriorizar());
			}
		}
		if (doctoServico.getLocalizacaoMercadoria() != null) {
			mapPreManifestoDocumento.put("doctoServico.localizacaoMercadoria.idLocalizacaoMercadoria", doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());
			mapPreManifestoDocumento.put("doctoServico.localizacaoMercadoria.dsLocalizacaoMercadoria", doctoServico.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria());
		}
		if (doctoServico.getClienteByIdClienteRemetente() != null) {
			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteRemetente.idCliente", doctoServico.getClienteByIdClienteRemetente().getIdCliente());
			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
		}
		if (doctoServico.getClienteByIdClienteDestinatario() != null) {
			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteRemetente.idCliente", doctoServico.getClienteByIdClienteRemetente().getIdCliente());
			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
		}
		return mapPreManifestoDocumento;
	}

	@SuppressWarnings({ "serial", "rawtypes" })
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
		/**
		 * Declaracao da classe pai
		 */		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(Manifesto.class);
		
		/**
		 * Esta classe e reponsavel por ordenar a List dos filhos que estao
		 * em memoria de acordo com as regras de negocio
		 */
		Comparator preManifestoDocumentoComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				PreManifestoDocumento preManifestoDocumento1 = (PreManifestoDocumento)obj1;
				PreManifestoDocumento preManifestoDocumento2 = (PreManifestoDocumento)obj2;
					
				if (preManifestoDocumento1.getNrOrdem() != null && preManifestoDocumento2.getNrOrdem() != null)
					return preManifestoDocumento1.getNrOrdem().compareTo(preManifestoDocumento2.getNrOrdem());
				return -1;
			}
		};
		
		
		/**
		 * Esta classe e reponsavel por ordenar a List dos filhos que estao
		 * em memoria de acordo com as regras de negocio
		 */
		Comparator preManifestoVolumeComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				PreManifestoVolume preManifestoVolume1 = (PreManifestoVolume)obj1;
				PreManifestoVolume preManifestoVolume2 = (PreManifestoVolume)obj2;
					
				if ( preManifestoVolume1.getVolumeNotaFiscal().getNrConhecimento() != null 
						&& preManifestoVolume2.getVolumeNotaFiscal().getNrConhecimento() != null )
					return  preManifestoVolume1.getVolumeNotaFiscal().getNrConhecimento().compareTo(
							preManifestoVolume2.getVolumeNotaFiscal().getNrConhecimento());
				return -1;
			}
		};
		
		/**
		 * Esta instancia é responsavel por carregar os 
		 * items filhos na sessão a partir do banco de dados.
		 */
		ItemListConfig itemInitPreManifestoVolume = new ItemListConfig() {
 
			/**
			 * Find paginated do filho
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */			
			public List initialize(Long masterId, Map parameters) {
				return preManifestoVolumeService.findPreManifestoVolumeByIdManifesto(masterId, false);
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */			
			public Integer getRowCount(Long masterId, Map parameters) {
				return Integer.valueOf(preManifestoVolumeService.findPreManifestoVolumeByIdManifesto(masterId, false).size());
			}
			
			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 * 
			 * @param parameters 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object bean) {
				PreManifestoVolume preManifestoVolume = (PreManifestoVolume) bean;
								
				if (!StringUtils.isBlank((String)parameters.get("masterId"))) {
					preManifestoVolume.setManifesto(getManifestoService().findById(Long.valueOf((String)parameters.get("masterId"))));
				}
				if (parameters.get("idVolumeNotaFiscal") != null) {
					preManifestoVolume.setVolumeNotaFiscal( volumeNotaFiscalService.findById((Long)parameters.get("idVolumeNotaFiscal")));
				}
				return preManifestoVolume;
			}
		};
		
		/**
		 * Esta instancia é responsavel por carregar os 
		 * items filhos na sessão a partir do banco de dados.
		 */
		ItemListConfig itemInitPreManifestoDocumento = new ItemListConfig() {
 
			/**
			 * Find paginated do filho
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */			
			public List initialize(Long masterId, Map parameters) {
				return preManifestoDocumentoService.findPreManifestoDocumentoByIdManifesto(masterId);
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */			
			public Integer getRowCount(Long masterId, Map parameters) {
				return preManifestoDocumentoService.getRowCountPreManifestoDocumento(masterId);
			}
			
			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 * 
			 * @param parameters 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object bean) {
				PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) bean;
								
				if (!StringUtils.isBlank((String)parameters.get("masterId"))) {
					preManifestoDocumento.setManifesto(getManifestoService().findById(Long.valueOf((String)parameters.get("masterId"))));
				}
				if (parameters.get("idDoctoServico") != null) {
					DoctoServico docto = doctoServicoService.findDoctoServicoById((Long)parameters.get("idDoctoServico"));
					docto.setDsCodigoBarras((String) parameters.get("dsCodigoBarras"));
					preManifestoDocumento.setDoctoServico(docto);
				}
				if (parameters.get("nrOrdem") != null) {
					preManifestoDocumento.setNrOrdem((Integer)parameters.get("nrOrdem"));
				}
				
				return preManifestoDocumento;
			}
		};

		config.addItemConfig("preManifestoVolume", PreManifestoVolume.class, itemInitPreManifestoVolume, preManifestoVolumeComparator);
		config.addItemConfig("preManifestoDocumento", PreManifestoDocumento.class, itemInitPreManifestoDocumento, preManifestoDocumentoComparator);
		return config;
	}
	
	/**
	 * ############################# FIM DOS MÉTODOS PARA TELA DE DF2 ###############################
	 */	

	
	
	
	/**
	 * ################## INICIO DOS MÉTODOS PARA A POP-UP DE ADICIONAR DOCUMENTOS ##################
	 */

	/**
	 * Utilizado pela combo de cias aereas.
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findCiaAerea(Map criteria) {
		List listCiaAerea = empresaService.findCiaAerea(criteria);
		List retorno = new ArrayList();
		if (!listCiaAerea.isEmpty()) {
			for (Iterator iter = listCiaAerea.iterator(); iter.hasNext();) {
				Map map = new HashMap();
				Empresa empresa = (Empresa) iter.next();
				map.put("idEmpresa", empresa.getIdEmpresa());
				map.put("nmEmpresa", empresa.getPessoa().getNmPessoa());
				retorno.add(map);
			}
		}
		return retorno;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupAwb(Map criteria) {
		Map ciaFilialMercurio = new HashMap();
		Map empresa = new HashMap();
		empresa.put("idEmpresa", criteria.get("idEmpresa"));
		ciaFilialMercurio.put("empresa", empresa);
		criteria.put("ciaFilialMercurio", ciaFilialMercurio);
		criteria.remove("idEmpresa");
		
		List listAwbs = awbService.findLookup(criteria);
		List listResult = new ArrayList();
		for (Iterator iter = listAwbs.iterator(); iter.hasNext();) {
			Awb awb = (Awb) iter.next();
			Map mapAwb = new HashMap();
			mapAwb.put("idAwb", awb.getIdAwb());
			mapAwb.put("nrAwb", awb.getNrAwb());
			mapAwb.put("idEmpresa", awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa());
			listResult.add(mapAwb);
		}
		return listResult;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupPedidoColeta(Map criteria) {
		Map idFilial = new HashMap();
		idFilial.put("idFilial", criteria.get("idFilial"));
		criteria.put("filialByIdFilialResponsavel",idFilial);
		criteria.remove("idFilial");
		
	    List listPedidosColeta = pedidoColetaService.findLookup(criteria);
	    
	    if(!listPedidosColeta.isEmpty() && listPedidosColeta.size()==1){
	    	PedidoColeta pedidocoleta = (PedidoColeta)listPedidosColeta.get(0);
	    	
	    	Map map = new HashMap();
	    	map.put("idPedidoColeta", pedidocoleta.getIdPedidoColeta());
	    	map.put("nrColeta", pedidocoleta.getNrColeta());
	    	listPedidosColeta.add(map);
	    	listPedidosColeta.remove(pedidocoleta);
	    }
	    return listPedidosColeta;
	  
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>>findDoctoServicoByPedidoColeta(Map criteria){
		Long idPedidoColeta = (Long)criteria.get("idPedidoColeta");
		return doctoServicoService.findDoctosServicoByIdPedidoColetaLiberaEtiquetaEdi(idPedidoColeta);
	}
	
	/**
	 * Obtém os ids dos Documentos de Servico da Sessão.
	 * @param masterId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List getIdsDoctoServicoFromSession(Long masterId) {
		MasterEntry master = getMasterFromSession(masterId, true);
		ItemList itens = getItemsFromSession(master, "preManifestoDocumento");
		ItemListConfig itensConfig = getMasterConfig().getItemListConfig("preManifestoDocumento");

		List list = new ArrayList();
		if (itens.isInitialized()) {
			for (Iterator it = itens.iterator(masterId, itensConfig); it.hasNext(); ) {
				PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento)it.next();
				list.add(preManifestoDocumento.getDoctoServico().getIdDoctoServico());
			} 
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	private BigDecimal getPsRealTotal(Long masterId) {
		MasterEntry master = getMasterFromSession(masterId, true);
		ItemList itens = getItemsFromSession(master, "preManifestoDocumento");
		ItemListConfig itensConfig = getMasterConfig().getItemListConfig("preManifestoDocumento");		
	
		BigDecimal psRealTotal = new BigDecimal(0);
		
		if (itens.isInitialized()) {
			for (Iterator it = itens.iterator(masterId, itensConfig); it.hasNext(); ) {
				PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento)it.next();
				psRealTotal = psRealTotal.add(preManifestoDocumento.getDoctoServico().getPsReal());
			} 
		} 
		
		return psRealTotal;
	}
	
	/**
	 * Obtém os ids dos Volumes da Sessão.
	 * @param masterId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List getIdsVolumesFromSession(Long idManifesto) {
		MasterEntry master = getMasterFromSession(idManifesto, true);
		ItemList itensVolumes = getItemsFromSession(master, "preManifestoVolume");
		ItemListConfig itensConfigVolumes = getMasterConfig().getItemListConfig("preManifestoVolume");

		List list = new ArrayList();
		if (itensVolumes.isInitialized()) {
			for (Iterator it = itensVolumes.iterator(idManifesto, itensConfigVolumes); it.hasNext(); ) {
				PreManifestoVolume preManifestoVolume = (PreManifestoVolume)it.next();
				list.add(preManifestoVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal());
			} 
		}
		return list;
	}

	/**
	 * Retorna os Volumes Nota Fiscal com localização no "Terminal", cujo os Documentos de Serviço não estão mais na Filial logada
	 * @param criteria
	 * @return
	 */			
	@SuppressWarnings("rawtypes")
	public List findVolumeNotaFiscalNoTerminalSemDoctoServicoNaFilial(Map criteria){
		final String localizacaoVolume = "terminal";
		return findVolumeNotaFiscal(criteria, localizacaoVolume);
			}
				
	
	/**
	 * Retorna os Volumes Nota Fiscal com localização "Em Descarga", cujo os Documentos de Serviço não estão mais na Filial logada
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findVolumeNotaFiscalEmDescargaSemDoctoServicoNaFilial(Map criteria){
		final String localizacaoVolume = "descarga";
		return findVolumeNotaFiscal(criteria, localizacaoVolume);
	}
	
	/**
	 * Busca os Volumes das Notas Fiscais
	 * @author André Valadas
	 * 
	 * @param criteria
	 * @param localizacaoVolume
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map> findVolumeNotaFiscal(final Map criteria, final String localizacaoVolume) {
		final Long idManifesto = StringUtils.isBlank((String)criteria.get("idManifesto")) ? null : Long.valueOf((String)criteria.get("idManifesto"));
		final Long idFilialLogada = SessionUtils.getFilialSessao().getIdFilial();
		final String tpManifesto = (String)criteria.get("tpManifesto");
		Long idDoctoServico =  (Long)criteria.get("idDoctoServico");
		
		/*CQPRO00027518 - Se o documento desciçoes servico for reentrega  então considerar o docto de servico original*/
		if(LongUtils.hasValue(idDoctoServico)){
			final Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idDoctoServico, false);
			if(conhecimento != null && ConstantesExpedicao.CONHECIMENTO_REENTREGA.equals(conhecimento.getTpConhecimento().getValue())){
				final DoctoServico doctoServicoOriginal = conhecimento.getDoctoServicoOriginal();
				if(doctoServicoOriginal != null) {
					idDoctoServico = doctoServicoOriginal.getIdDoctoServico();
			}
		}
		}
				
		final List<Long> idVolumeNotaFiscalList = getIdsVolumesFromSession(idManifesto);
		final List<Map> lolumeNotaFiscalListMap = volumeNotaFiscalService.findVolumeNotaFiscalSemDoctoServicoNaFilial(idDoctoServico, idFilialLogada, idVolumeNotaFiscalList, tpManifesto, localizacaoVolume);	
		return lolumeNotaFiscalListMap;
	}
	
	/**
	 * Busca Documentos para a grid da aba "Terminal" da pop-up de Adicionar Documentos. 
	 */
	@SuppressWarnings("rawtypes")
	public List findListTerminalNovo(final Map criteria) {
		// Localização do documento.
		final String localizacaoDocumento = "terminal";
		return findDoctoServicoList(criteria, localizacaoDocumento);
			}
			
	/**
	 * Busca Documentos para a grid da aba "Descarga" da pop-up de Adicionar Documentos. 
			 */			
	@SuppressWarnings("rawtypes")
	public List findListDescargaNovo(final Map criteria) {
		// Localização do documento.
		final String localizacaoDocumento = "descarga";
		return findDoctoServicoList(criteria, localizacaoDocumento);
			}
			
	
	/**
	 * Busca os documentos do pre-manifesto relacionado
	 * @author André Valadas
	 * 
	 * @param criteria
	 * @param localizacaoDocumento
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List findDoctoServicoList(final Map criteria, final String localizacaoDocumento) {
		Long idManifesto = StringUtils.isBlank((String)criteria.get("idManifesto")) ? null : Long.valueOf((String)criteria.get("idManifesto")); 
		Long idSolicitacaoRetirada = (Long)criteria.get("idSolicitacaoRetirada");
		Long idRotaColetaEntrega = (Long)criteria.get("idRotaColetaEntrega");
		Long idFilialDestinoManifesto = (Long)criteria.get("idFilialDestino");
		Long idConsignatario = (Long)criteria.get("idClienteConsignatario");
		Long idClienteRemetente = (Long)criteria.get("idClienteRemetente");
		Long idClienteDestinatario = (Long)criteria.get("idClienteDestinatario");
		String tpDoctoServico = (String)criteria.get("tpDocumentoServico");
		Long idFilialOrigemDoctoServico = (Long)criteria.get("idFilialOrigemDoctoServico");
		Long idFilialDestinoDoctoServico = (Long)criteria.get("idFilialDestinoDoctoServico");
		Long idDoctoServico = (Long)criteria.get("idDoctoServico");
		Long idAwb = (Long)criteria.get("idAwb");
		String tpManifesto = (String)criteria.get("tpManifesto");
		String tpAbrangencia = (String)criteria.get("tpAbrangencia");
		String tpPreManifesto = (String)criteria.get("tpPreManifesto");
		Long idPedidoColeta = (Long)criteria.get("idPedidoColeta");
		String dsBox = (String)criteria.get("dsBox") != null ? criteria.get("dsBox").toString().replace("%", "") : null;
		
		List idsDoctoServico = new ArrayList();
		String from = (String)criteria.get("from");
		if (from != null && from.equals("carregamento")) {
			final List preManifestoDocumentos = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifesto(idManifesto);
			for (Iterator iter = preManifestoDocumentos.iterator(); iter.hasNext();) {
				PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
				idDoctoServico = preManifestoDocumento.getDoctoServico().getIdDoctoServico();
				idsDoctoServico.add(idDoctoServico);
			} 
		} else {
			idsDoctoServico = getIdsDoctoServicoFromSession(idManifesto);
		}
		
		/** Busca os documentos do pre-manifesto */
		final List<TypedFlatMap> doctoServicoList = doctoServicoService.findAdicionarDoctoServicoPreManifesto( 
				idManifesto,
				idSolicitacaoRetirada,
				idRotaColetaEntrega,
				idFilialDestinoManifesto,
				idConsignatario,
				idClienteRemetente,
				idClienteDestinatario,
				tpDoctoServico,
				idFilialOrigemDoctoServico,
				idFilialDestinoDoctoServico,
				idDoctoServico,
				idAwb,
				tpManifesto,
				tpAbrangencia,
				tpPreManifesto,
				idsDoctoServico,
				localizacaoDocumento,
				idPedidoColeta,
				dsBox);

		return convertDoctoServicoBeanResultToMapList(doctoServicoList);
			}

	/**
	 * Método responsável por converter dados do Bean para Map
	 * @author André Valadas
	 * 
	 * @param doctoServicoList
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map> convertDoctoServicoBeanResultToMapList(final List<TypedFlatMap> doctoServicoList) {
		final YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		final Long idPaisSessao = SessionUtils.getPaisSessao().getIdPais();
		final Long idMoedaSessao = SessionUtils.getMoedaSessao().getIdMoeda();
				
		final List<Map> toReturn = new ArrayList<Map>(doctoServicoList.size());
		for (final TypedFlatMap doctoServico : doctoServicoList) {
			final Map mapDoctoServico = new HashMap();
			mapDoctoServico.put("idDoctoServico", doctoServico.get("idDoctoServico"));
			mapDoctoServico.put("tpDocumentoServico", doctoServico.get("tpDocumentoServico"));
			mapDoctoServico.put("nrDoctoServico", doctoServico.get("nrDoctoServico"));
			mapDoctoServico.put("dvDoctoServico", (doctoServico.get("dvDoctoServico") != null ? " - " + doctoServico.get("dvDoctoServico") : ""));
			mapDoctoServico.put("qtVolumes", doctoServico.get("qtVolumes"));
			mapDoctoServico.put("psReal", doctoServico.get("psReal"));
			mapDoctoServico.put("vlMercadoria", doctoServico.get("vlMercadoria"));
		
			final Long idMoeda = doctoServico.getLong("moeda.idMoeda");
			final BigDecimal vlMercadoria = doctoServico.getBigDecimal("vlMercadoria");
			if (LongUtils.hasValue(idMoeda) && BigDecimalUtils.hasValue(vlMercadoria)) {
				final BigDecimal vlMercadoriaConvertido = conversaoMoedaService.findConversaoMoeda(
							 idPaisSessao,
						idMoeda,
						idPaisSessao,
							 idMoedaSessao,
							 dataAtual,
						vlMercadoria
					);
				mapDoctoServico.put("vlMercadoriaConvertido", vlMercadoriaConvertido);
				}
	
			mapDoctoServico.put("dtPrevEntrega", doctoServico.get("dtPrevEntrega"));
			mapDoctoServico.put("dhEmissao", doctoServico.get("dhEmissao"));
			mapDoctoServico.put("blBloqueado", doctoServico.get("blBloqueado"));
			if (doctoServico.get("filialByIdFilialOrigem.idFilial") != null) {
				mapDoctoServico.put("idFilialOrigem", doctoServico.get("filialByIdFilialOrigem.idFilial"));
				mapDoctoServico.put("sgFilialOrigem", doctoServico.get("filialByIdFilialOrigem.sgFilial"));
				mapDoctoServico.put("nmFantasiaFilialOrigem", doctoServico.get("filialByIdFilialOrigem.pessoa.nmFantasia"));
				}
			if (doctoServico.get("filialByIdFilialDestino.idFilial") != null) {
				mapDoctoServico.put("idFilialDestino", doctoServico.get("filialByIdFilialDestino.idFilial"));
				mapDoctoServico.put("sgFilialDestino", doctoServico.get("filialByIdFilialDestino.sgFilial"));
				mapDoctoServico.put("nmFantasiaFilialDestino", doctoServico.get("filialByIdFilialDestino.pessoa.nmFantasia"));
				}
			if (LongUtils.hasValue(idMoeda)) {
				mapDoctoServico.put("idMoeda", idMoeda);
				mapDoctoServico.put("siglaSimboloMoeda", (String)doctoServico.get("moeda.sgMoeda") + " " + (String)doctoServico.get("moeda.dsSimbolo"));
			}
			if (doctoServico.get("servico.idServico") != null) {
				mapDoctoServico.put("idServico", doctoServico.get("servico.idServico"));
				mapDoctoServico.put("sgServico", doctoServico.get("servico.sgServico"));
				mapDoctoServico.put("tpModalServico", doctoServico.get("servico.tpModal"));
				mapDoctoServico.put("tpAbrangenciaServico", doctoServico.get("servico.tpAbrangencia"));
				if (doctoServico.get("servico.tipoServico.blPriorizar") != null) {
					mapDoctoServico.put("blPriorizar", doctoServico.get("servico.tipoServico.blPriorizar"));
		}
	}
			if (doctoServico.get("clienteByIdClienteRemetente.idCliente") != null) {
				mapDoctoServico.put("idClienteRemetente", doctoServico.get("clienteByIdClienteRemetente.idCliente"));
				mapDoctoServico.put("nmPessoaRemetente", doctoServico.get("clienteByIdClienteRemetente.pessoa.nmPessoa"));
				mapDoctoServico.put("blAgendamentoPessoaFisicaRemetente", doctoServico.get("clienteByIdClienteRemetente.blAgendamentoPessoaFisica"));
				mapDoctoServico.put("blAgendamentoPessoaJuridicaRemetente", doctoServico.get("clienteByIdClienteRemetente.blAgendamentoPessoaJuridica"));
				}
			if (doctoServico.get("clienteByIdClienteDestinatario.idCliente") != null) {
				mapDoctoServico.put("idClienteDestinatario", doctoServico.get("clienteByIdClienteDestinatario.idCliente"));
				mapDoctoServico.put("nmPessoaDestinatario", doctoServico.get("clienteByIdClienteDestinatario.pessoa.nmPessoa"));
				mapDoctoServico.put("tpPessoaDestinatario", doctoServico.get("clienteByIdClienteDestinatario.tpPessoa"));
				}
			if (doctoServico.get("clienteByIdClienteConsignatario.idCliente") != null) {
				mapDoctoServico.put("idClienteConsignatario", doctoServico.get("clienteByIdClienteConsignatario.idCliente"));
				mapDoctoServico.put("nmPessoaConsignatario", doctoServico.get("clienteByIdClienteConsignatario.pessoa.nmPessoa"));
				mapDoctoServico.put("tpPessoaConsignatario", doctoServico.get("clienteByIdClienteConsignatario.tpPessoa"));
				}
			if (doctoServico.get("rotaColetaEntregaByIdRotaColetaEntregaReal.idRotaColetaEntrega") != null) {
				mapDoctoServico.put("idRotaColetaEntrega", doctoServico.get("rotaColetaEntregaByIdRotaColetaEntregaReal.idRotaColetaEntrega"));
			}
			if (doctoServico.get("paisOrigem.idPais") != null) {
				mapDoctoServico.put("idPais", doctoServico.get("paisOrigem.idPais"));
		}
			mapDoctoServico.put("diasTerminal", doctoServico.get("diasTerminal"));

			/*
			 * Verifica se dtPrevEntrega é menor que a data atual, se o tpModal é igual a 'A'(Áereo)
			 * e se o tipo de serviço possui blPriorizar = 'S'. Caso essas cláusulas sejam verdadeiras,
			 * colocar o registro em destaque na grid.
			 */			
			if (doctoServico.get("emDestaque") != null) {
				mapDoctoServico.put("emDestaque", doctoServico.get("emDestaque"));
	}
			toReturn.add(mapDoctoServico);
		}
		return toReturn;
	}
	
	/**
	 * FindPaginated para a grid de Chegadas
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultSetPage findPaginatedChegadas(Map criteria) {
		final TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("manifesto.filialByIdFilialDestino.idFilial", criteria.get("idFilialDestino"));
		tfmCriteria.put("doctoServico.filialByIdFilialDestino.idFilial", criteria.get("idFilialDestinoDoctoServico"));
		tfmCriteria.put("_pageSize", criteria.get("_pageSize"));
		tfmCriteria.put("_currentPage", criteria.get("_currentPage"));
		tfmCriteria.put("_order", criteria.get("_order"));
		
		final List tpStatusColetaList = new ArrayList();
		tpStatusColetaList.add("AD");
		tpStatusColetaList.add("ED");
		tpStatusColetaList.add("NT");
		
		final List<PedidoColeta> pedidoColetaList = pedidoColetaService.findPedidoColetaByTpStatusColetaByIdFilialResponsavel(
				tpStatusColetaList,
				SessionUtils.getFilialSessao().getIdFilial()
		);
		
		Long idFilialDestino = null;
		if (tfmCriteria.getLong("doctoServico.filialByIdFilialDestino.idFilial") != null) {
			idFilialDestino = tfmCriteria.getLong("doctoServico.filialByIdFilialDestino.idFilial");
		} else if (tfmCriteria.getLong("manifesto.filialByIdFilialDestino.idFilial") != null) {
			idFilialDestino = tfmCriteria.getLong("manifesto.filialByIdFilialDestino.idFilial");
		}
		
		final ResultSetPage rsp = new ResultSetPage(Integer.valueOf(1), false, false, Collections.EMPTY_LIST);
		rsp.setCurrentPage(tfmCriteria.getInteger("_currentPage"));
		rsp.setList(convertPedidoColetaBeanResultToMapList(pedidoColetaList, idFilialDestino));
		return rsp;
	}

	/**
	 * Método responsável por converter dados do Bean para Map
	 * @author André Valadas
	 * 
	 * @param pedidoColetaList
	 * @param idFilialDestino
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<TypedFlatMap> convertPedidoColetaBeanResultToMapList(final List<PedidoColeta> pedidoColetaList, final Long idFilialDestino) {
		final List<TypedFlatMap> toReturn = new ArrayList<TypedFlatMap>();
		for (final PedidoColeta pedidoColeta : pedidoColetaList) {
			final TypedFlatMap mapPedidoColeta = new TypedFlatMap();
				
			final List<DetalheColeta> detalheColetaList = detalheColetaService.findDetalheColeta(pedidoColeta.getIdPedidoColeta());
			for (final DetalheColeta detalheColeta : detalheColetaList) {
				if (detalheColeta.getFilial() != null && detalheColeta.getFilial().getIdFilial().equals(idFilialDestino)) {
					mapPedidoColeta.put("idPedidoColeta", pedidoColeta.getIdPedidoColeta());
					mapPedidoColeta.put("nrColeta", pedidoColeta.getNrColeta());
								
					if (pedidoColeta.getFilialByIdFilialResponsavel() != null) {
						mapPedidoColeta.put("sgFilialResponsavel", pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
					}
					if (pedidoColeta.getCliente() != null) {
						mapPedidoColeta.put("nmPessoaCliente", pedidoColeta.getCliente().getPessoa().getNmPessoa());
					}
					mapPedidoColeta.put("psReal", detalheColeta.getPsMercadoria());
					mapPedidoColeta.put("qtVolumes", detalheColeta.getQtVolumes());
					mapPedidoColeta.put("vlMercadoria", detalheColeta.getVlMercadoria());
					if (detalheColeta.getServico() != null) {
						mapPedidoColeta.put("sgServico", detalheColeta.getServico().getSgServico());
					}
					if (detalheColeta.getNaturezaProduto() != null) {
						mapPedidoColeta.put("dsNaturezaProduto", detalheColeta.getNaturezaProduto().getDsNaturezaProduto());
					}
					if (detalheColeta.getMoeda() != null) {
						mapPedidoColeta.put("idMoeda", detalheColeta.getMoeda().getIdMoeda());
						mapPedidoColeta.put("siglaSimboloMoeda", detalheColeta.getMoeda().getSiglaSimbolo());
					}
					if (detalheColeta.getFilial() != null) {
						mapPedidoColeta.put("sgFilialDetalhe", detalheColeta.getFilial().getSgFilial());
					}
					if (detalheColeta.getMunicipio() != null) {
						mapPedidoColeta.put("idPais", detalheColeta.getMunicipio().getUnidadeFederativa().getPais().getIdPais());
					}
					toReturn.add(mapPedidoColeta);
				}
			}
		}
		return toReturn;
	}
	
	/**
	 * FindPaginated para a grid de Executadas
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultSetPage findPaginatedExecutadas(Map criteria) {
		final TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("manifesto.filialByIdFilialDestino.idFilial", criteria.get("idFilialDestino"));
		tfmCriteria.put("doctoServico.filialByIdFilialDestino.idFilial", criteria.get("idFilialDestinoDoctoServico"));
		tfmCriteria.put("_pageSize", criteria.get("_pageSize"));
		tfmCriteria.put("_currentPage", criteria.get("_currentPage"));
		tfmCriteria.put("_order", criteria.get("_order"));

		final List tpStatusColetaList = new ArrayList();
		tpStatusColetaList.add("EX");

		final List<PedidoColeta> pedidoColetaList = pedidoColetaService.findPedidoColetaByTpStatusColetaByIdFilialResponsavel(
				tpStatusColetaList, 
				SessionUtils.getFilialSessao().getIdFilial()
		);

		Long idFilialDestino = null;
		if (tfmCriteria.getLong("doctoServico.filialByIdFilialDestino.idFilial") != null) {
			idFilialDestino = tfmCriteria.getLong("doctoServico.filialByIdFilialDestino.idFilial");
		} else if (tfmCriteria.getLong("manifesto.filialByIdFilialDestino.idFilial") != null) {
			idFilialDestino = tfmCriteria.getLong("manifesto.filialByIdFilialDestino.idFilial");
		}
		
		final ResultSetPage rsp = new ResultSetPage(Integer.valueOf(1), false, false, Collections.EMPTY_LIST);
		rsp.setCurrentPage(tfmCriteria.getInteger("_currentPage"));
		rsp.setList(convertPedidoColetaBeanResultToMapList(pedidoColetaList, idFilialDestino));
		return rsp;
	}
		
	/**
	 * Método chamado ao selecionar TODOS os registros na grid de DoctoServico para validação de situações.
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map validaDoctoServicosList(Map criteria) {
		final List idsList = new ArrayList();
		final List<Map> list = new ArrayList<Map>();
		final List criteriaList = (List)criteria.get("list");
		list.addAll(criteriaList);
		/** Itera todos documentos selecionados */
		for (final Map data : list) {
			data.put("list", criteriaList);
			final Map result = this.validaDoctoServico(data);
			result.put("rowIndex", data.get("rowIndex"));

			/** Adiciona registros com alertas ou erros */
			if (result.get("mensagem") != null) {
				idsList.add(result);
			}
		}

		/** Retorna lista de conhecimentos */
		final Map toReturn = new HashMap();
		toReturn.put("ids", idsList);
		return toReturn;
	}

	/**
	 * Valida documento de serviço do pré-manifesto
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map validaDoctoServico(Map criteria) {
		final Map toReturn = new HashMap();
		final Long idDoctoServico = (Long)criteria.get("idDoctoServico");
		final String tpManifesto = (String)criteria.get("tpManifesto");
				
		//LMS-4643
		BusinessException businessException = ocorrenciaDoctoServicoService.validateTemBloqueioDoctoServico(idDoctoServico, null);
		if (businessException != null) {
			toReturn.put("mensagem", businessException.getMessageKey());
			toReturn.put("alerta", Boolean.TRUE);
			toReturn.put("params", businessException.getMessageArguments());
			toReturn.put("idDoctoServico", idDoctoServico);
			return toReturn;
		}

		if(ConstantesEntrega.TP_MANIFESTO_VIAGEM.equals(tpManifesto)) {
		//Se for do tipo 'Viagem' verificar se a filial destino é a mesma do usuario logado. Se sim, adicionar parametro novo 
		//e verificar no callback	
			DoctoServico docto = doctoServicoService.findById(idDoctoServico);			
			if(SessionUtils.getFilialSessao().equals(docto.getFilialByIdFilialDestino())) {
				toReturn.put("mensagemManifestoViagem", "LMS-05339");
			}
		
		} else if (ConstantesEntrega.TP_MANIFESTO_ENTREGA.equals(tpManifesto)) {
			if (conhecimentoService.validateAgendamentoObrigatorioEsemAgendamentoDoctoServicoAtivo(idDoctoServico)){
				toReturn.put("mensagem", "LMS-05424");
				return toReturn;
			}
			// Se for to tipo 'Entrega'
			// Verifica se o Documento é do tipo 'RRE' e permite incluir no manifesto somente se
			// for vinculada a uma MIR do tipo 'Administrativo para entrega'.
			if (((String)criteria.get("tpDocumentoServico")).equals(ConstantesExpedicao.RECIBO_REEMBOLSO)) {
				final DocumentoMir documentoMir = documentoMirService.findDocumentoMirByIdReciboReembolso(idDoctoServico, "AE");
				if (documentoMir == null) {
						toReturn.put("mensagem", "LMS-05046");
						toReturn.put("idDoctoServico", criteria.get("idDoctoServico"));
					return toReturn;
				}
			} else {
				// Verifica se o documento possui agendamento Ativo
				final List<AgendamentoDoctoServico> agendamentoDoctoServicoList = agendamentoDoctoServicoService.findAgendamentosAtivos(idDoctoServico);
				if (!agendamentoDoctoServicoList.isEmpty()) {
					final AgendamentoEntrega agendamentoEntrega = agendamentoDoctoServicoList.get(0).getAgendamentoEntrega();
					// Testa se data do Agendamento é maior ou menos que a data atual
					if (agendamentoEntrega.getDtAgendamento().compareTo(JTDateTimeUtils.getDataAtual()) > 0 ) {
						String dataAgendamento = JTFormatUtils.format(agendamentoEntrega.getDtAgendamento(), JTFormatUtils.MEDIUM);
							toReturn.put("mensagem", "LMS-05324");
							toReturn.put("alerta", Boolean.TRUE);
							toReturn.put("params", dataAgendamento);
							toReturn.put("idDoctoServico", idDoctoServico);
						return toReturn;
					} else if (agendamentoEntrega.getDtAgendamento().compareTo(JTDateTimeUtils.getDataAtual()) < 0 ) {
							toReturn.put("mensagem", "LMS-05045");
							toReturn.put("idDoctoServico", idDoctoServico);
						return toReturn;
					}
				} else {
					// Verifica se cliente Remetente do Documento em questão possui necessidade de Agendamento 
					// e se o Documento já foi agendado.			
					Boolean blAgendamentoPessoaFisicaRemetente = ((Boolean)criteria.get("blAgendamentoPessoaFisicaRemetente"));
					Boolean blAgendamentoPessoaJuridicaRemetente = ((Boolean)criteria.get("blAgendamentoPessoaJuridicaRemetente"));
					String tpPessoaDestinatario = (String)criteria.get("tpPessoaDestinatario");
					if ( (Boolean.TRUE.equals(blAgendamentoPessoaFisicaRemetente) && tpPessoaDestinatario.equals("F")) 
							|| (Boolean.TRUE.equals(blAgendamentoPessoaJuridicaRemetente) && tpPessoaDestinatario.equals("J"))) {
							toReturn.put("mensagem", "LMS-05043");
							toReturn.put("idDoctoServico", idDoctoServico);
						return toReturn;
					}
				}
				
				Awb awb = awbService.findByIdDoctoServicoAndFilialOrigem(idDoctoServico, SessionUtils.getFilialSessao().getIdFilial());
				if (awb == null) {
					if (criteria.get("idRotaColetaEntregaManifesto") != null && criteria.get("idRotaColetaEntrega") != null
							&& !((Long) criteria.get("idRotaColetaEntrega")).equals((Long) criteria.get("idRotaColetaEntregaManifesto"))) {
						toReturn.put("mensagem", "LMS-05047");
						toReturn.put("confirmar", Boolean.TRUE);
						toReturn.put("idDoctoServico", idDoctoServico);
						return toReturn;
					}
				}
			}
		}

			// Verifica se o modal do documento é diferente do modal do Manifesto em questão
			if (criteria.get("tpModalServico") != null && !((String)criteria.get("tpModalServico")).equals((String)criteria.get("tpModalManifesto")) ) {
					toReturn.put("mensagem", "LMS-05049");
					toReturn.put("confirmar", Boolean.TRUE);
					toReturn.put("idDoctoServico", idDoctoServico);
				return toReturn;
			}			
			
			// Valida Filial substituta do Documento.
			toReturn.putAll(validaFilialSubstitutaDoctoServico(criteria));		
			
		return toReturn;
	}
		
	/**
	 * Verifica se o destino possui filial substituta. Em seguida busca a filial substituta do documento 
	 * para validar com a filial de destino do manifesto.
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map validaFilialSubstitutaDoctoServico(Map criteria) {
		Map mapResult = new HashMap();
		
		// LMS-7173 
		// Se o tipo do documento de serviço for diferente de MDA, então não valida a filial substituta
		if(!((String)criteria.get("tpDocumentoServico")).equals(ConstantesExpedicao.MINUTA_DESPACHO_ACOMPANHAMENTO)) {		
			if (!((String)criteria.get("tpDocumentoServico")).equals(ConstantesExpedicao.RECIBO_REEMBOLSO) && criteria.get("idFilialDestinoManifesto") != null) {
				Long idDoctoServico = (Long)criteria.get("idDoctoServico");
				Long idFilialDestinoDocto = (Long)criteria.get("idFilialDestino");
				Long idFilialDestinoManifesto = (Long)criteria.get("idFilialDestinoManifesto");
		
				if (idFilialDestinoDocto.equals(idFilialDestinoManifesto)) {
					List listaSubstAtendimentoFilial = substAtendimentoFilialService.findSubstAtendimentoFilialByIdFilialDestino(idFilialDestinoManifesto, null);
					if (!listaSubstAtendimentoFilial.isEmpty()) {
						Filial filialDestinoSubstituta = substAtendimentoFilialService.findFilialDestinoDoctoServico(idDoctoServico, null, null, null);
						if (!idFilialDestinoManifesto.equals(filialDestinoSubstituta.getIdFilial())) {
							Pessoa pessoa = pessoaService.findById(filialDestinoSubstituta.getIdFilial());
							mapResult.put("mensagem", "LMS-05051");
							mapResult.put("params", pessoa.getNmFantasia());
							mapResult.put("idDoctoServico", idDoctoServico);
						}
					}
				} else {
				/*
				 * Corrigido erro de unique result, pois estava buscando mais de 1 resultado na consulta(hotfix)
				 */
				List<SubstAtendimentoFilial> substAtendimentoFilial = substAtendimentoFilialService.findSubstAtendimentoFilialByIdFilialDestinoAndFilialSubstituta(
						idFilialDestinoManifesto,
						idFilialDestinoDocto
				);
				if (substAtendimentoFilial != null && !substAtendimentoFilial.isEmpty()) {
					Filial filialDestinoSubstituta = substAtendimentoFilialService.findFilialDestinoDoctoServico(idDoctoServico, null, null, null);
					if (!idFilialDestinoDocto.equals(filialDestinoSubstituta.getIdFilial()) && !idFilialDestinoManifesto.equals(filialDestinoSubstituta.getIdFilial())) {
							Pessoa pessoa = pessoaService.findById(filialDestinoSubstituta.getIdFilial());
							mapResult.put("mensagem", "LMS-05051");
							mapResult.put("params", pessoa.getNmFantasia());
							mapResult.put("idDoctoServico", idDoctoServico);
						}
					}
				}
			}
		}
		return mapResult;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map validaPsRealTotal(Map criteria) {
		Map mapResult = new HashMap();
		BigDecimal psRealTotal = new BigDecimal(0);

		Long idControleCarga = (Long) criteria.get("idControleCarga");
		Long idDoctoServico = (Long) criteria.get("idDoctoServico");
		DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico); //(Long) criteria.get("idDoctoServico"));
		
		if(doctoServico != null) {
			psRealTotal = getPsRealTotal(getMasterId(criteria)).add(doctoServico.getPsReal());
			
			BigDecimal psRealTotalOlder = preManifestoDocumentoService.findPsRealTotalByIdControleCarga(idControleCarga);
			if(psRealTotalOlder != null) {
				psRealTotal = psRealTotal.add(psRealTotalOlder);
			}
		}
		
		// LMSA-6267: LMSA-6630
		BigDecimal pesoTotalInicialComCarregamentoFedexEfetuadoEmPrimeiroCarregamento =
				conhecimentoFedexService.findTotalPesoRealConhecimentoFedexByIdControleCarga(idControleCarga);
		if(pesoTotalInicialComCarregamentoFedexEfetuadoEmPrimeiroCarregamento != null) {
			psRealTotal = psRealTotal.add(pesoTotalInicialComCarregamentoFedexEfetuadoEmPrimeiroCarregamento);
		}
		
		mapResult.put("psRealTotal", psRealTotal);
		
		return mapResult;
	}

	/**
	 * Retorna o conteúdo do atributo VL_CONTEUDO_PARAMETRO_FILIAL da tabela CONTEUDO_PARAMETRO_FILIAL 
	 * @param Map idFilial
	 * @return Map indicadorCte
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map loadIndicadorCte(Map map){

		Object obj = conteudoParametroFilialService.findConteudoByNomeParametro((Long)map.get("idFilial"), "INDICADOR CTE", false);
		Integer vlConteudoParametroFilial = obj != null ? Integer.valueOf(obj.toString()) : null;
		
		Map retorno = new HashMap();
		retorno.put("indicadorCte", vlConteudoParametroFilial);
		return retorno;
	}

	/**
	 * Método que retorna um Map com o id do DoctoServico pesquisado atraves do Tipo de Documento, 
	 * da Filial de Origem e do Número.
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map findDoctoServico(Map criteria) {
		return preManifestoDocumentoService.findDoctoServico(criteria);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findDoctoServicoManual(Map criteria){
		criteria.put("idFilialOrigem", criteria.get("idFilial"));
		criteria.put("nrDoctoServico", criteria.get("nrDoctoServico"));
		criteria.put("dvConhecimento", criteria.get("dvConhecimento"));
		return doctoServicoService.findDoctoServicoManual(criteria);
	}
	
	/**
	 * Valida se o numero de codigo de barras pertence a um conhecimento e se o
	 * cliente é valido para leitura via código de barras. Definido pela issue
	 * LMS-879.
	 * 
	 * @param criteria
	 *            deve possuir o atributo nrCodigoBarras
	 * @return dados do conhecimento encontrado
	 * @author Luis Carlos Poletto
	 */
	public Map<String, Object> validateCodigoBarras(Map<String, Object> criteria) {
		String nrCodigoBarras = (String)criteria.get("nrCodigoBarras");
		
		// CONHECIMENTO RETORNADO
		Conhecimento conhecimento;
		
		// BUSCA CONHECIMENTO COM CÓDIGO DE BARRAS ATÉ 20 DÍGITOS
		if ( nrCodigoBarras.length() <= 20 ) {
			Long nrCodigoBarrasMenor = Long.valueOf(nrCodigoBarras);
			
			conhecimento = conhecimentoService.findByNrCodigoBarras(nrCodigoBarrasMenor);
		} else {
			// BUSCA CONHECIMENTO COM CÓDIGO DE BARRAS DE 21 ATÉ 44 DÍGITOS
			conhecimento = conhecimentoService.findByNrChave(nrCodigoBarras);
		}
		
		Long nrPreManifesto = criteria.get("nrPreManifesto")!=null?(Long) criteria.get("nrPreManifesto"):null;
		Long idManifesto = criteria.get("idManifesto")!=null?(Long) criteria.get("idManifesto"):null;
		
		conhecimentoService.validateInformarConhecimentoManifesto(conhecimento, idManifesto, nrPreManifesto);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("idDoctoServico", conhecimento.getIdDoctoServico());
		result.put("tpDocumentoServico", conhecimento.getTpDoctoServico().getValue());
		result.put("nrDoctoServico", conhecimento.getNrDoctoServico());
		result.put("dvConhecimento", conhecimento.getDvConhecimento());
		result.put("sgFilial", conhecimento.getFilialByIdFilialOrigem().getSgFilial());
		result.put("idFilial", conhecimento.getFilialByIdFilialOrigem().getIdFilial());
		result.put("nmFantasia", conhecimento.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
		return result;
	}
	
	/**
	 * Valida o barcode LMS-2505
	 * @author DiogoSB
	 */
	@SuppressWarnings("rawtypes")
	public Map validaBarcode(Map<String,Object> criteria){
		
		Map<String,Object> retorno = new HashMap<String, Object>();
		
		String nrCodigoBarras = (String)criteria.get("nrCodigoBarras");
		
		// CONHECIMENTO RETORNADO
		Conhecimento conhecimento = null;
		
		// BUSCA CONHECIMENTO COM CÓDIGO DE BARRAS ATÉ 20 DÍGITOS
		if ( nrCodigoBarras.length() <= 20 ) {
			Long nrCodigoBarrasCTR = Long.valueOf(nrCodigoBarras);
			conhecimento = conhecimentoService.findConhecimentoByCodigoBarras(nrCodigoBarrasCTR);
		} else {
			// BUSCA CONHECIMENTO COM CÓDIGO DE BARRAS DE 21 ATÉ 44 DÍGITOS
			conhecimento = conhecimentoService.findConhecimentoByNrChaveCTE(nrCodigoBarras);
		}
		
		if (conhecimento != null) {
			
			Integer numPreManifestoDocs = preManifestoDocumentoService.validateDoctoServico(Long.parseLong((String) criteria.get("idManifesto")), conhecimento.getIdDoctoServico());
			Integer numPreManifestoVol = preManifestoVolumeService.validateDoctoServico(Long.parseLong((String) criteria.get("idManifesto")) , conhecimento.getIdDoctoServico());
			if(numPreManifestoDocs > 0){
				retorno.put("retorno", true);
				retorno.put("addDocumento", false);
				retorno.put("conhecimento", null);
				return retorno;
			}else{
				if(numPreManifestoVol > 0){
					retorno.put("retorno", true);
					retorno.put("addDocumento", true);
					
					Map<String, Object> conhecimentoMap = new HashMap<String, Object>();
					conhecimentoMap.put("idDoctoServico", conhecimento.getIdDoctoServico());
					conhecimentoMap.put("tpDocumentoServico", conhecimento.getTpDoctoServico().getValue());
					conhecimentoMap.put("nrDoctoServico", conhecimento.getNrDoctoServico());
					conhecimentoMap.put("dvConhecimento", conhecimento.getDvConhecimento());
					Filial origem = filialService.findById(conhecimento.getFilialByIdFilialOrigem().getIdFilial());
					conhecimentoMap.put("sgFilial", origem.getSgFilial());
					conhecimentoMap.put("idFilial", origem.getIdFilial());
					Pessoa pessoa = pessoaService.findById(origem.getPessoa().getIdPessoa());
					conhecimentoMap.put("nmFantasia", pessoa.getNmFantasia()); 
					
					retorno.put("conhecimento", conhecimentoMap);
					
					return retorno;
					// EXECUTAR A REGRA
				}else{
					retorno.put("retorno", false);
					retorno.put("addDocumento", false);
					retorno.put("conhecimento", null);
					return retorno;
				}
			}
		} else {
			retorno.put("retorno", false);
			throw new BusinessException("LMS-10040");
		}
	}
	
	/**
	 * LMS-5261
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findDocsManifestadosMesmoDestino(Map<String, Long> parameters) {
		return getManifestoService().findDocsManifestadosMesmoDestino(parameters.get("idManifesto"), parameters.get("idDocumento"));
	}
	
	@SuppressWarnings("rawtypes")
	public void validaGeracaoByControleCarga(Map parameters) {
		if (parameters != null) {
			String tpManifesto = (String) parameters.get("tpManifesto");
			String tpPreManifesto = (String) parameters.get("tpPreManifesto");
			Long idControleCarga = (Long) parameters.get("idControleCarga");
			if ("E".equals(tpManifesto) && StringUtils.isNotBlank(tpPreManifesto) && idControleCarga != null) {
				controleCargaService.validateParcelaPud(idControleCarga, tpPreManifesto);
				getManifestoService().validateInclusaoManifestosControleCarga(idControleCarga, tpPreManifesto);
			}
		}
	}
	
	public Map<String, Object> findPreAwb (Map<String, Object> criteria){
		Awb awb = awbService.findPreAwb((Long)criteria.get("nrAwb"), ConstantesExpedicao.TP_STATUS_PRE_AWB, SessionUtils.getFilialSessao().getIdFilial());
		Map<String, Object> retorno = new HashMap<String, Object>();
		if(awb != null){
			retorno.put("ciaAerea", awb.getCiaFilialMercurio().getEmpresa().getPessoa().getNmPessoa());
		}
		return retorno;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>>findPreAwbDoctoServicoAtivo(Map criteria){
		Long idAwb = (Long)criteria.get("nrAwb");
		return doctoServicoService.findPreAwbDoctoServicoAtivo(idAwb);
	}
	
	public void setPreManifestoVolumeService(PreManifestoVolumeService preManifestoVolumeService) {
		this.preManifestoVolumeService = preManifestoVolumeService;
	}
	public void setDadosComplementoService(DadosComplementoService dadosComplementoService) {
		this.dadosComplementoService = dadosComplementoService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.setMasterService(manifestoService);
	}
	private ManifestoService getManifestoService() {
		return (ManifestoService) super.getMasterService();
	}

	public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}
	public void setSubstAtendimentoFilialService(SubstAtendimentoFilialService substAtendimentoFilialService) {
		this.substAtendimentoFilialService = substAtendimentoFilialService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setAgendamentoDoctoServicoService(AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
		this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
	}
	public void setDocumentoMirService(DocumentoMirService documentoMirService) {
		this.documentoMirService = documentoMirService;
	}
	public void setSolicitacaoRetiradaService(SolicitacaoRetiradaService solicitacaoRetiradaService) {
		this.solicitacaoRetiradaService = solicitacaoRetiradaService;
	}
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}
	public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	
	// LMSA-6267: LMSA-6630
	public void setConhecimentoFedexService (ConhecimentoFedexService conhecimentoFedexService) {
		this.conhecimentoFedexService = conhecimentoFedexService;
	}

	/**
	 * ################# FIM DOS MÉTODOS PARA A POP-UP DE ADICIONAR DOCUMENTOS ######################
	 */
}
