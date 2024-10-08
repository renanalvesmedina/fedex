package com.mercurio.lms.expedicao.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.AwbEmbalagem;
import com.mercurio.lms.expedicao.model.CalculoFreteCiaAerea;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.Embalagem;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.AwbOcorrenciaService;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.EmbalagemService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.expedicao.model.service.PreAwbService;
import com.mercurio.lms.expedicao.model.service.TrackingAwbService;
import com.mercurio.lms.expedicao.report.EmitirAWBService;
import com.mercurio.lms.expedicao.report.EmitirMinutaEletronicaService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesAwb;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.CiaFilialMercurioService;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.service.ApoliceSeguroService;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.TarifaSpot;
import com.mercurio.lms.tabelaprecos.model.service.ProdutoEspecificoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TarifaSpotService;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.SeguroClienteService;


/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.expedicao.digitarPreAWBAction"
 */
public class DigitarPreAWBAction extends CrudAction {
	
	private AeroportoService aeroportoService;
	private CiaFilialMercurioService ciaFilialMercurioService;
	private FilialService filialService;
	private MunicipioService municipioService;
	private ConhecimentoService conhecimentoService;
	private ClienteService clienteService;
	private TipoTributacaoIEService tipoTributacaoIEService;
	private EmbalagemService embalagemService;
	private NaturezaProdutoService naturezaProdutoService;
	private ProdutoEspecificoService produtoEspecificoService;
	private TarifaSpotService tarifaSpotService;
	private PpeService ppeService;
	private EmitirAWBService emitirAWBService;
	private ConfiguracoesFacade configuracoesFacade;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private PessoaService pessoaService;
	private SeguroClienteService seguroClienteService;
	private ApoliceSeguroService apoliceSeguroService;
	private ParametroGeralService parametroGeralService;
	private EmitirMinutaEletronicaService emitirMinutaEletronicaService;
	private TabelaPrecoService tabelaPrecoService;
	private ReportExecutionManager reportExecutionManager;
	private AwbService awbService;
	private ControleCargaService controleCargaService;
	private ConversaoMoedaService conversaoMoedaService;
	private ManifestoService manifestoService;
	private PedidoColetaService pedidoColetaService;
	private AwbOcorrenciaService awbOcorrenciaService;
	private TrackingAwbService trackingAwbService;
	private EmpresaService empresaService;

	public String execute(TypedFlatMap reportParams) throws Exception {
		return this.reportExecutionManager.generateReportLocator(this.emitirMinutaEletronicaService, reportParams);
	}	
	
	private Empresa buscarEmpresaByCiaAerea(Long idCiaAerea) {
		Empresa empresa = null;
		if (idCiaAerea != null) {
			empresa = empresaService.findById(idCiaAerea);
		}
		return empresa;
	}

	private String buscarNrIdentificacaoByFilialEmpresa(Empresa empresa) {
		if ((empresa != null) && (empresa.getFilialTomadorFrete() != null)) {
			Pessoa pessoa = pessoaService.findById(empresa.getFilialTomadorFrete().getIdFilial());
			if (pessoa != null) {
				return pessoa.getNrIdentificacao();
			}
		}
		return null;
	}
	
	private Cliente buildClienteByFilialEmpresa(Empresa empresa) {
		Cliente cliente = null;
		if ((empresa != null) && (empresa.getFilialTomadorFrete() != null)) {
			cliente = new Cliente();
			cliente.setIdCliente(empresa.getFilialTomadorFrete().getIdFilial());
		}
		return cliente;
	}
	
	public TypedFlatMap store(TypedFlatMap parameters) {
		Awb awb = AwbUtils.getAwbInSession();

		/** CALCULO FRETE */
		Long idCalculoFrete = parameters.getLong("idCalculoFrete");
		CalculoFreteCiaAerea calculoFreteCiaAerea = getCalculoFrete(idCalculoFrete);
		CalculoFreteUtils.copyResult(awb, calculoFreteCiaAerea);

		Filial filialOrigem = SessionUtils.getFilialSessao();
		awb.setFilialByIdFilialOrigem(filialOrigem);

		Long idAeroportoDestino = awb.getAeroportoByIdAeroportoDestino().getIdAeroporto();
		Aeroporto aeroportoDestino = aeroportoService.findById(idAeroportoDestino);
		awb.setFilialByIdFilialDestino(aeroportoDestino.getFilial());

		Long idCiaAerea = calculoFreteCiaAerea.getIdCiaAerea();
		CiaFilialMercurio ciaFilialMercurio = ciaFilialMercurioService.findByIdCiaAereaIdFilial(idCiaAerea, filialOrigem.getIdFilial());
		awb.setCiaFilialMercurio(ciaFilialMercurio);

		Moeda moeda = configuracoesFacade.getMoeda(ConstantesConfiguracoes.NBR_ISO_BR_REAL);
		awb.setMoeda(moeda);

		awb.setTpStatusAwb(new DomainValue("P"));
		awb.setTpLocalEmissao(new DomainValue(ConstantesExpedicao.TP_EMPRESA_MERCURIO));

		awb.setDhPrevistaChegada(parameters.getDateTime("dhChegada"));
		awb.setDhPrevistaSaida(parameters.getDateTime("dhSaida"));
		awb.setDsVooPrevisto(parameters.getString("nrVooPrevisto"));

		String nrIdentificacaoTomador = "";
		Empresa empresa = buscarEmpresaByCiaAerea(idCiaAerea);
		if (empresa != null) {
			awb.setClienteByIdClienteTomador(buildClienteByFilialEmpresa(empresa));
			awb.setNrCcTomadorServico(empresa.getNrContaCorrente());
			nrIdentificacaoTomador = buscarNrIdentificacaoByFilialEmpresa(empresa);
		}
		if ("".equals(nrIdentificacaoTomador) || (awb.getNrCcTomadorServico() == null)) {
			throw new BusinessException("LMS-04557");
		}

		Long idTabelaPreco = parameters.getLong("idTabelaPreco");
		awb.setTabelaPreco(tabelaPrecoService.findByIdTabelaPreco(idTabelaPreco));
		
		List<CtoAwb> listCtoAwb = awb.getCtoAwbs();
		String dsJustificativaPrejuizoAwb = parameters.getString("dsJustificativaPrejuizo");
		if(StringUtils.isNotBlank(dsJustificativaPrejuizoAwb)){
			awb.setUsuarioJustificativa(SessionUtils.getUsuarioLogado());
			awb.setDsJustificativaPrejuizo(dsJustificativaPrejuizoAwb);
		}
		
		double vlFrete = Double.parseDouble(calculoFreteCiaAerea.getVlTotal().toString());
		if(!listCtoAwb.isEmpty()){
			String tipoAwb = configuracoesFacade.getMensagem("preAwb");
			getAwbService().validateRentabilidadeAwb(listCtoAwb, vlFrete, dsJustificativaPrejuizoAwb, tipoAwb);
		}
		
		//LMS-3381: Ao cadastrar o Pr�-awb a localiza��o deve ser Aguardando Entrega Aeroporto
		awb.setTpLocalizacao(new DomainValue(ConstantesAwb.TP_LOCALIZACAO_AWB_AGUARDANDO_ENTREGA_AEROPORTO));
		
		/** STORE */
		getPreAwbService().storePreAwb(awb);
		
		//LMS-3381: Registrar ocorrencia na tabela de ocorrencias com o mesmo tpLocalizacao do Awb
		getAwbOcorrenciaService().store(awb, new DomainValue(ConstantesAwb.TP_LOCALIZACAO_AWB_AGUARDANDO_ENTREGA_AEROPORTO));
		
		getTrackingAwbService().storeTrackingAwb(awb, idCiaAerea, ConstantesAwb.TP_LOCALIZACAO_AWB_AGUARDANDO_ENTREGA_AEROPORTO);

		TypedFlatMap result = new TypedFlatMap();
		result.put("idAwb", awb.getIdAwb());
		result.put("dhDigitacao", awb.getDhDigitacao());
		result.put("idClienteTomador", awb.getClienteByIdClienteTomador().getIdCliente());
		result.put("nrIdentificacaoTomador", nrIdentificacaoTomador);
		result.put("nrCcTomadorServico", awb.getNrCcTomadorServico());
		return result;
	}

	private CalculoFreteCiaAerea getCalculoFrete(Long id) {
		List<CalculoFreteCiaAerea> calculos = (List) SessionContext.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
		for (CalculoFreteCiaAerea calculoFreteCiaAerea : calculos) {
			if(CompareUtils.eq(calculoFreteCiaAerea.getIdCalculoFrete(), id)) {
				return calculoFreteCiaAerea;
			}
		}
		return null;
	}

	/**
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		awbOcorrenciaService.removeAwbOcorrenciaByIdAwb(ids);
		super.removeByIds(ids);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		this.preparePaginatedCriteria(criteria);
		return getPreAwbService().findPaginated(criteria);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		this.preparePaginatedCriteria(criteria);
		return getPreAwbService().getRowCount(criteria);
	}

	public List findAeroporto(TypedFlatMap criteria) {
		return aeroportoService.findLookupAeroporto(criteria);
	}

	private void preparePaginatedCriteria(TypedFlatMap criteria) {
		Filial filial = SessionUtils.getFilialSessao();
		criteria.put("filialOrigem.idFilial", filial.getIdFilial());
	}

	public List findCiaAerea(TypedFlatMap criteria) {
		return ciaFilialMercurioService.findCiaAerea(criteria);
	}

	public List findDadosRemetente(TypedFlatMap criteria) {
		return findDadosCliente(criteria);
	}

	public List findDadosDestinatario(TypedFlatMap criteria) {
		return findDadosCliente(criteria);
	}

	public List findDadosTomador(TypedFlatMap criteria) {
		return findDadosCliente(criteria);
	}

	public List findDadosCliente(TypedFlatMap criteria) {
		List returnList = new ArrayList();
		Long idCliente = criteria.getLong("idCliente");
		if(LongUtils.hasValue(idCliente)) {
			returnList = clienteService.findLookupClienteEndereco(idCliente);
		} else {
			String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
			if(StringUtils.isNotBlank(nrIdentificacao)) {
				returnList = clienteService.findClienteByNrIdentificacao(nrIdentificacao);
			}
		}

		if(!returnList.isEmpty()) {
			Map cliente = (Map) returnList.get(0);
			cliente.put("ie", tipoTributacaoIEService.findVigentesByIdPessoa(MapUtils.getLong(cliente, "idCliente")));
		}
		return returnList;
	}

	public List findClienteLogado() {
		Pessoa pessoa = SessionUtils.getFilialSessao().getPessoa();

		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("pessoa.nrIdentificacao", pessoa.getNrIdentificacao());

		return findDadosCliente(criteria);
	}

	public Map findAeroportoFilialSessao() {
		return findAeroportoByIdFilial(SessionUtils.getFilialSessao().getIdFilial());
	}

	public Map findAeroportoByIdFilial(TypedFlatMap criteria) {
		Long idFilial = criteria.getLong("idFilial");
		return (idFilial == null) ? null : filialService.findAeroportoFilial(idFilial);
	}

	private Map findAeroportoByIdFilial(Long idFilial) {
		return (idFilial == null) ? null : filialService.findAeroportoFilial(idFilial);
	}

	public List findEmbalagem(TypedFlatMap criteria) {
		return embalagemService.find(criteria);
	}

	public List findNaturezaProduto(TypedFlatMap criteria) {
		return naturezaProdutoService.findAllAtivo();
	}

	public List findProdutoEspecifico(TypedFlatMap criteria) {
		return produtoEspecificoService.findAllAtivo();
	}

	public TypedFlatMap findById(Long id) {
		TypedFlatMap result = new TypedFlatMap();
		Awb awb = getPreAwbService().findToUpdate(id, result);

		AwbUtils.setAwbInSession(awb);
		return result;
	}

	public List findLookupFilial(TypedFlatMap criteria) {
		return filialService.findLookupBySgFilial(criteria.getString("sgFilial"), criteria.getString("tpAcesso"));
	}

	public List findLookupMunicipio(TypedFlatMap criteria) {
		return municipioService.findLookup(criteria);
	}

	public List findLookupManifestoViagem(TypedFlatMap criteria){
		List<ManifestoViagemNacional> manifestos = manifestoViagemNacionalService.findByNrManifestoOrigemByFilial(criteria.getInteger("nrManifesto"), criteria.getLong("filialOrigem.idFilial"));
		if (manifestos.isEmpty()){
			return null;
		}
		else{
			TypedFlatMap map = new TypedFlatMap();
			map.put("idManifestoViagemNacional", manifestos.get(0).getIdManifestoViagemNacional());
			map.put("nrManifesto", manifestos.get(0).getNrManifestoOrigem());
			Filial filial = filialService.findById(manifestos.get(0).getFilial().getIdFilial());
			map.put("filial.sgFilial", filial.getSgFilial());
			map.put("filial.idFilial", filial.getIdFilial());
			List retorno = new ArrayList();
			retorno.add(map);
			return retorno;
		}
	}

	public List findCtrc(TypedFlatMap criteria) {
		if (criteria.getString("conhecimentos") == null){
		this.verifyCtrcParameters(criteria);
		}

		Filial filialSessao = SessionUtils.getFilialSessao();
		criteria.put("filialLocalizacao.idFilial", filialSessao.getIdFilial());
		List result = conhecimentoService.findByTerminalNotInAwb(criteria);

		for(Iterator it = result.iterator();it.hasNext();) {
			TypedFlatMap map = (TypedFlatMap) it.next();

			String tpDocumento = map.getDomainValue("tpDocumentoServico.value").getValue();
			String nrCtrc;

			if(tpDocumento.equals(ConstantesExpedicao.CONHECIMENTO_NACIONAL)){
				nrCtrc = ConhecimentoUtils.formatConhecimento(
				 map.getString("filialOrigem.sgFilial")
				,map.getLong("nrConhecimento")
				,map.getInteger("dvConhecimento"));
			} else {
				nrCtrc = ConhecimentoUtils.formatConhecimento(
					map.getString("filialOrigem.sgFilial")
					,map.getLong("nrConhecimento")
					,null);
			}
			map.put("nrCtrc", nrCtrc);

			Integer nrAltura = map.getInteger("dimensao.nrAltura");
			Integer nrLargura = map.getInteger("dimensao.nrLargura");
			Integer nrComprimento = map.getInteger("dimensao.nrComprimento");

			String dimensaoString = null;
			if (IntegerUtils.hasValue(nrAltura) &&
				IntegerUtils.hasValue(nrLargura) &&
				IntegerUtils.hasValue(nrComprimento)
			) {
				dimensaoString = nrAltura + " x " + nrLargura + " x " + nrComprimento;
			}

			map.put("dimensaoString", dimensaoString);
		}
		return result;
	}

	private void verifyCtrcParameters(TypedFlatMap criteria) {
		Long idAeroporto = criteria.getLong("aeroporto.idAeroporto");
		Long idFilial = criteria.getLong("filial.idFilial");
		Long idMunicipio = criteria.getLong("municipio.idMunicipio");
		Long idManifestoViagem = criteria.getLong("manifestoViagem.idManifestoViagemNacional");

		// indicam se os campos acima estao vazios
		// true = vazio, false = preenchido
		boolean blAeroporto = (idAeroporto == null);
		boolean blFilial = (idFilial == null);
		boolean blMunicipio = (idMunicipio == null);
		boolean blManifestoViagem = (idManifestoViagem == null);

		if(blAeroporto && blFilial && blMunicipio && blManifestoViagem) {
			throw new BusinessException("LMS-00055");
		}

		if(!blAeroporto) {
			if(!blFilial || !blMunicipio || !blManifestoViagem) {
				throw new BusinessException("LMS-04168");
			}
		}

		if(!blFilial) {
			if(!blAeroporto || !blMunicipio || !blManifestoViagem) {
				throw new BusinessException("LMS-04168");
			}
		}

		if(!blMunicipio) {
			if(!blFilial || !blAeroporto || !blManifestoViagem) {
				throw new BusinessException("LMS-04168");
			}
		}
		
		if(!blManifestoViagem) {
			if(!blFilial || !blAeroporto || !blMunicipio) {
				throw new BusinessException("LMS-04168");
	}
		}
	}

	public Map findAeroportoDestino(TypedFlatMap criteria) {
		Long idFilial = ppeService.findFilialAtendimentoMunicipio(
			criteria.getLong("idMunicipio"), 
			criteria.getLong("idServico"),
			Boolean.FALSE,
			JTDateTimeUtils.getDataAtual(),
			criteria.getString("nrCep"), 
			criteria.getLong("idCliente"),
			null, null, null, null, null
		);
		return findAeroportoByIdFilial(idFilial);
	}

	public void reconfiguraSessao() {
		AwbUtils.setAwbInSession(new Awb());
		SessionContext.remove(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
	}

	public TypedFlatMap verificaTarifaSpot(TypedFlatMap data) {
		String dsSenha = data.getString("dsSenha");
		if (StringUtils.isBlank(dsSenha)) {
			return data;
		}

		TarifaSpot tarifaSpot = tarifaSpotService.findByDsSenha(dsSenha);
		if (tarifaSpot == null) {
			data.put("error", "true");
			return data;
		}
		if (!"A".equals(tarifaSpot.getTpSituacao().getValue())) {
			data.put("error", "true");
			return data;
		}

		long nrUtilizacoes = 0; 
		if(tarifaSpot.getNrUtilizacoes() != null) {
			nrUtilizacoes = tarifaSpot.getNrUtilizacoes().longValue();
		}
		long nrPossibilidaddes = tarifaSpot.getNrPossibilidades().longValue();

		if(!(nrUtilizacoes < nrPossibilidaddes)) {
			data.put("error", "true");
			return data;
		}

		Long idAeroportoOrigem = data.getLong("aeroportoOrigem.idAeroporto");
		if (!tarifaSpot.getAeroportoByIdAeroportoOrigem().getIdAeroporto().equals(idAeroportoOrigem)) {
			data.put("error", "true");
			return data;
		}

		Long idAeroportoDestino = data.getLong("aeroportoDestino.idAeroporto");
		if (!tarifaSpot.getAeroportoByIdAeroportoDestino().getIdAeroporto().equals(idAeroportoDestino)) {
			data.put("error", "true");
			return data;
		}

		Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
		if (!tarifaSpot.getFilial().getIdFilial().equals(idFilialSessao)) {
			data.put("error", "true");
			return data;
		}
		data.put("idTarifaSpot", tarifaSpot.getIdTarifaSpot());
		return data;
	}

	private Cliente getCliente(Long idUnidadeFederativa, Long idMunicipio, Long idCliente) {
		UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
		unidadeFederativa.setIdUnidadeFederativa(idUnidadeFederativa);
		
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(idMunicipio);
		municipio.setUnidadeFederativa(unidadeFederativa);
		
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		enderecoPessoa.setMunicipio(municipio);

		Pessoa pessoa = new Pessoa();
		pessoa.setEnderecoPessoa(enderecoPessoa);
		
		Cliente cliente = new Cliente();
		cliente.setPessoa(pessoa);
		cliente.setIdCliente(idCliente);
		
		return cliente;
	}

	private InscricaoEstadual getInscricaoEstadual(Long idInscricaoEstadual) {
		InscricaoEstadual inscricaoEstadual = new InscricaoEstadual();
		inscricaoEstadual.setIdInscricaoEstadual(idInscricaoEstadual);
		return inscricaoEstadual;
	}
	
	public void calcularAwb(TypedFlatMap parameters) {
		Awb awb = AwbUtils.getAwbInSession();

		//Dados do Expedidor
		awb.setClienteByIdClienteExpedidor(
			getCliente(parameters.getLong("clienteByIdClienteRemetente.endereco.idUnidadeFederativa")
			, parameters.getLong("clienteByIdClienteRemetente.endereco.idMunicipio")
			, parameters.getLong("clienteByIdClienteRemetente.idCliente")));
		
		Long idInscricaoEstadualRemetente = parameters.getLong("clienteByIdClienteRemetente.idInscricaoEstadual");
		if (idInscricaoEstadualRemetente != null) {
			awb.setInscricaoEstadualExpedidor(getInscricaoEstadual(idInscricaoEstadualRemetente));
		}
		
		//Dados do Tomador
		Long idClienteTomador = parameters.getLong("clienteByIdClienteTomador.idCliente");
		if (idClienteTomador != null) {
			awb.setClienteByIdClienteTomador(
				getCliente(parameters.getLong("clienteByIdClienteTomador.endereco.idUnidadeFederativa")
				, parameters.getLong("clienteByIdClienteTomador.endereco.idMunicipio")
				, idClienteTomador));
		}

		Long idInscricaoEstadualTomador = parameters.getLong("clienteByIdClienteTomador.idInscricaoEstadual");
		if (idInscricaoEstadualTomador != null) {
			awb.setInscricaoEstadualTomador(getInscricaoEstadual(idInscricaoEstadualTomador));
		}

		//Dados do Destinatario
		awb.setClienteByIdClienteDestinatario(
			getCliente(parameters.getLong("clienteByIdClienteDestinatario.endereco.idUnidadeFederativa")
			, parameters.getLong("clienteByIdClienteDestinatario.endereco.idMunicipio")
			, parameters.getLong("clienteByIdClienteDestinatario.idCliente")));

		Long idInscricaoEstadualDest = parameters.getLong("clienteByIdClienteDestinatario.idInscricaoEstadual");
		if (idInscricaoEstadualDest != null) {
			awb.setInscricaoEstadualDestinatario(getInscricaoEstadual(idInscricaoEstadualDest));
		}

		Aeroporto aeroporto = new Aeroporto();
		aeroporto.setIdAeroporto(parameters.getLong("aeroportoByIdAeroportoOrigem.idAeroporto"));
		awb.setAeroportoByIdAeroportoOrigem(aeroporto);

		aeroporto = new Aeroporto();
		aeroporto.setIdAeroporto(parameters.getLong("aeroportoByIdAeroportoDestino.idAeroporto"));
		awb.setAeroportoByIdAeroportoDestino(aeroporto);

		Long idAeroportoEscala = parameters.getLong("idAeroportoEscala");
		if (idAeroportoEscala != null) {
			aeroporto = new Aeroporto();
			aeroporto.setIdAeroporto(idAeroportoEscala);
			awb.setAeroportoByIdAeroportoEscala(aeroporto);
		}

		awb.setTpFrete(parameters.getDomainValue("tpFrete"));
		awb.setQtVolumes(parameters.getInteger("qtVolumes"));
		awb.setPsTotal(parameters.getBigDecimal("psReal"));
		awb.setPsCubado(parameters.getBigDecimal("psCubado"));

		Long idTarifaSpot = parameters.getLong("tarifaSpot.idTarifaSpot");
		if (idTarifaSpot != null) {
			TarifaSpot tarifaSpot = new TarifaSpot();
			tarifaSpot.setIdTarifaSpot(idTarifaSpot);
			awb.setTarifaSpot(tarifaSpot);
		}

		NaturezaProduto naturezaProduto = new NaturezaProduto();
		naturezaProduto.setIdNaturezaProduto(parameters.getLong("naturezaProduto.idNaturezaProduto"));
		awb.setNaturezaProduto(naturezaProduto);

		setAwbEmbalagem(awb, parameters.getLong("embalagem.idEmbalagem"));

		if (parameters.getLong("produtoEspecifico.idProdutoEspecifico") != null) {
			ProdutoEspecifico produtoEspecifico = new ProdutoEspecifico();
			produtoEspecifico.setIdProdutoEspecifico(parameters.getLong("produtoEspecifico.idProdutoEspecifico"));
		
			if (produtoEspecifico.getIdProdutoEspecifico() != null) {
				awb.setProdutoEspecifico(produtoEspecifico);
			}
		}

		if (parameters.getLong("idApoliceSeguro") != null) {
			ApoliceSeguro apoliceSeguro = new ApoliceSeguro();	
			apoliceSeguro.setIdApoliceSeguro(parameters.getLong("idApoliceSeguro"));
			awb.setApoliceSeguro(apoliceSeguro);
		}
		
		awb.setNrLvSeguro(parameters.getString("nrLvSeguro"));
		awb.setObAwb(parameters.getString("obAwb"));
		
		getPreAwbService().validatePreAwb(awb);
	}

	private void setAwbEmbalagem(Awb awb, Long idEmbalagem) {
		List<AwbEmbalagem> awbEmbalagens = awb.getAwbEmbalagems();
		boolean update = false;
		if(awbEmbalagens != null) {
			for (AwbEmbalagem awbEmbalagem : awbEmbalagens) {
				if(idEmbalagem.equals(awbEmbalagem.getEmbalagem().getIdEmbalagem())) {
					update = true;
				}
			}
		}
		if(!update) {
			Embalagem embalagem = new Embalagem();
			embalagem.setIdEmbalagem(idEmbalagem);

			AwbEmbalagem awbEmbalagem = new AwbEmbalagem();
			awbEmbalagem.setEmbalagem(embalagem);
			awbEmbalagem.setAwb(awb);

			awbEmbalagens = new ArrayList<AwbEmbalagem>();
			awbEmbalagens.add(awbEmbalagem);
			awb.setAwbEmbalagems(awbEmbalagens);
		}
	}

	public TypedFlatMap findSeguroPorTipo(){
		return apoliceSeguroService.findSeguroPorTipo();
	}
	
	public TypedFlatMap findSeguroPorAwb(TypedFlatMap criteria){
		return apoliceSeguroService.findSeguroPorAwb(criteria.getString("dsApolice"));
	}
	
	public ParametroGeral findByNomeParametro(){
		return parametroGeralService.findByNomeParametro("RESP_SEGURO_AEREO", false);
	}
	
	

	/**
	 * Emissao/Reemissao do AWB.
	 * @author Andre Valadas.
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap emitirAWB(TypedFlatMap criteria) {
		Long idAwb = criteria.getLong("idAwb");
		return emitirAWBService.executeEmitirAWB(idAwb);
	}

	public PreAwbService getPreAwbService() {
		return (PreAwbService)super.defaultService;
	}
	public void setPreAwbService(PreAwbService serviceService) {
		super.defaultService = serviceService;
	}
	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}
	public void setCiaFilialMercurioService(CiaFilialMercurioService ciaFilialMercurioService) {
		this.ciaFilialMercurioService = ciaFilialMercurioService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setTipoTributacaoIEService(TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}
	public void setEmbalagemService(EmbalagemService embalagemService) {
		this.embalagemService = embalagemService;
	}
	public void setNaturezaProdutoService(NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}
	public void setProdutoEspecificoService(ProdutoEspecificoService produtoEspecificoService) {
		this.produtoEspecificoService = produtoEspecificoService;
	}
	public void setTarifaSpotService(TarifaSpotService tarifaSpotService) {
		this.tarifaSpotService = tarifaSpotService;
	}
	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}
	public void setEmitirAWBService(EmitirAWBService emitirAWBService) {
		this.emitirAWBService = emitirAWBService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public ManifestoViagemNacionalService getManifestoViagemNacionalService() {
		return manifestoViagemNacionalService;
	}

	public void setManifestoViagemNacionalService(
			ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}
	
	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
	public SeguroClienteService getSeguroClienteService() {
		return seguroClienteService;
	}

	public void setSeguroClienteService(SeguroClienteService seguroClienteService) {
		this.seguroClienteService = seguroClienteService;
	}
	
	public void setApoliceSeguroService(ApoliceSeguroService apoliceSeguroService) {
		this.apoliceSeguroService = apoliceSeguroService;
	}
	
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public EmitirMinutaEletronicaService getEmitirMinutaEletronicaService() {
		return emitirMinutaEletronicaService;
	}

	public void setEmitirMinutaEletronicaService(
			EmitirMinutaEletronicaService emitirMinutaEletronicaService) {
		this.emitirMinutaEletronicaService = emitirMinutaEletronicaService;
	}

	public TabelaPrecoService getTabelaPrecoService() {
		return tabelaPrecoService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public AwbService getAwbService() {
		return awbService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
	
	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public ManifestoService getManifestoService() {
		return manifestoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	
	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}
	
	public AwbOcorrenciaService getAwbOcorrenciaService() {
		return awbOcorrenciaService;
	}

	public void setAwbOcorrenciaService(AwbOcorrenciaService awbOcorrenciaService) {
		this.awbOcorrenciaService = awbOcorrenciaService;
	}

	public TrackingAwbService getTrackingAwbService() {
		return trackingAwbService;
	}

	public void setTrackingAwbService(TrackingAwbService trackingAwbService) {
		this.trackingAwbService = trackingAwbService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
}