package com.mercurio.lms.facade.radar.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.ParametroFilial;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.TipoEnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DimensaoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.facade.radar.CotacaoOnlineFacade;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.ProdutoEspecificoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.LiberacaoEmbarque;
import com.mercurio.lms.vendas.model.ParcelaCotacao;
import com.mercurio.lms.vendas.model.ProibidoEmbarque;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.CotacaoService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.model.service.LiberacaoEmbarqueService;
import com.mercurio.lms.vendas.model.service.ParcelaCotacaoService;
import com.mercurio.lms.vendas.model.service.ProibidoEmbarqueService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.PendenciaService;

/**
 * @author Gabriel Rubens
 * @spring.bean id="lms.cotacaoOnlineFacade"
 */
@ServiceSecurity
public class CotacaoOnlineFacadeImpl implements CotacaoOnlineFacade {
	
	private static final String SITUACAO_ATIVA = "A";
	private static final String NAO = "N";
	private static final String SIM = "S";
	private static final Logger LOGGER = LogManager.getLogger(CotacaoOnlineFacadeImpl.class);
	private static final long DM_STATUS_COTACAO = 51L;
	private static final long DM_TIPO_FRETE = 190L;
	private static final long DM_SIT_TRIBUTARIA = 128L;
	private static final long DM_TIPO_PESSOA_COTACAO = 296L;
	private static final long DM_TIPO_CALCULO_COTACAO = 300L;
	private static final long DM_TIPO_DOCUMENTO = 35L;
	private static final long DM_SITUACAO_APROVACAO_COTACAO = 2362;
	private static final String SIGLA_BRASIL = "BRA";
	private DimensaoService dimensaoService;
	private DomainValueService domainValueService;
	private ServicoService servicoService;
	private ParametroGeralService parametroGeralService;
	private UsuarioADSMService usuarioADSMService;
	private com.mercurio.lms.configuracoes.model.service.UsuarioService usuarioService;
	private ClienteService clienteService;
	private InscricaoEstadualService inscricaoEstadualService;
	private EnderecoPessoaService enderecoPessoaService;
	private TipoEnderecoPessoaService tipoEnderecoPessoaService;
	private DivisaoClienteService divisaoClienteService;
	private PessoaService pessoaService;
	private NaturezaProdutoService naturezaProdutoService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private UnidadeFederativaService unidadeFederativaService;
	private MunicipioService municipioService;
	private TipoTributacaoIEService tipoTributacaoIEService;
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ProibidoEmbarqueService proibidoEmbarqueService;
	private MunicipioFilialService municipioFilialService;
	private LiberacaoEmbarqueService liberacaoEmbarqueService;
	private AeroportoService aeroportoService;
	private ProdutoEspecificoService produtoEspecificoService;
	private CotacaoService cotacaoService;
	private FilialService filialService;
	private MoedaService moedaService;
	private TabelaPrecoService tabelaPrecoService;
	private DoctoServicoService doctoServicoService;
	private PendenciaService pendenciaService;
	private ParcelaPrecoService parcelaPrecoService;
	private ParcelaCotacaoService parcelaCotacaoService;
	private ConhecimentoService conhecimentoService;
	private AwbService awbService;
	private CtoInternacionalService ctoInternacionalService;
	private DomainService domainService;
	private PaisService paisService;

	@SuppressWarnings("all")
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "searchDimensao", authenticationRequired=false)
	public TypedFlatMap searchDimensao(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		Long idCotacao = (Long) criteria.get("idCotacao");
		List<Dimensao> searchDimensao = dimensaoService.findByIdCotacao(idCotacao);
    	tfm.put("dimensoes", searchDimensao);
		return tfm;
	}

	@SuppressWarnings("all")
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findServicosByTpAbrangencia", authenticationRequired=false)
	public TypedFlatMap findServicosByTpAbrangencia(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		List listServicoByTpAbrangencia = servicoService.findByTpAbrangencia(criteria.getString("tipoAbrangencia"));
		List<Map<String, Object>> listServicos = parseServicoToListMap(listServicoByTpAbrangencia);
		tfm.put("servicosModal", listServicos);
		return tfm;
	}


	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findServicoById", authenticationRequired=false)
	public TypedFlatMap findServicoById(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		Servico servico = servicoService.findById(criteria.getLong("idServico"));
		tfm.put("idServico", servico.getIdServico());
		tfm.put("dsServico", servico.getDsServico());
		tfm.put("tpModal", servico.getTpModal().getValue());
		tfm.put("sgServico", servico.getSgServico());
		return tfm;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findIdServicoDefaultModal", authenticationRequired=false)
	public TypedFlatMap findIdServicoDefaultModal(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(criteria.getString("nomeParametoGeral"));
		
		if(NumberUtils.isNumber(parametroGeral.getDsConteudo())){
			Servico servico = servicoService.findById(Long.parseLong(parametroGeral.getDsConteudo()));
			tfm.put("idServicoDefaultModal", servico.getIdServico());
		}
		return tfm;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findUserById", authenticationRequired=false)
	public TypedFlatMap findUserById(TypedFlatMap criteria) {
		
		TypedFlatMap map = new TypedFlatMap();
		String login = criteria.getString("login");
		UsuarioADSM usuarioADSM = usuarioADSMService.findUsuarioADSMByLogin(login);

		map.put("idUsuarioCliente", usuarioADSM.getUsuarioCadastro().getIdUsuario());
		map.put("idUsuario", usuarioADSM.getIdUsuario());
		map.put("login", usuarioADSM.getLogin());
		map.put("nmUsuario", usuarioADSM.getNmUsuario());
		map.put("tpCategoriaUsuario", usuarioADSM.getTpCategoriaUsuario().getValue());
		map.put("email", usuarioADSM.getDsEmail());
		map.put("ddd", usuarioADSM.getNrDdd());
		map.put("fone", usuarioADSM.getNrFone());
		
		return map;
		
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getClienteByIdentificador", authenticationRequired=false)
	public TypedFlatMap getClienteByIdentificador(TypedFlatMap criteria) {
		String nrIdentificacao = criteria.getString("nrIdentificacao");
		Cliente c = this.clienteService.findByNumeroIdentificacao(nrIdentificacao);
		return parseClienteToMap(c);
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findFilialAtendeOperacional", authenticationRequired=false)
	public TypedFlatMap findFilialAtendeOperacional(TypedFlatMap criteria) {
		TypedFlatMap map = null;
		Long idCliente = criteria.getLong("idCliente");
		Cliente c = this.clienteService.findById(idCliente);
		
		if(c!=null && c.getFilialByIdFilialAtendeOperacional()!=null){
			map = new TypedFlatMap();
			map.put("idFilial", c.getFilialByIdFilialAtendeOperacional().getIdFilial());
			map.put("sgFilial", c.getFilialByIdFilialAtendeOperacional().getSgFilial());
			map.put("nmFantasia", c.getFilialByIdFilialAtendeOperacional().getPessoa().getNmFantasia());
		}
		
		
		return map;
	}


	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getIeByCliente", authenticationRequired=false)
	public List<TypedFlatMap> getIeByCliente(TypedFlatMap criteria) {
		Long idCliente = criteria.getLong("idCliente");
		List<TypedFlatMap> listMap = new ArrayList<TypedFlatMap>();
		List<InscricaoEstadual> list = inscricaoEstadualService
				.findInscricaoEstadualAtivaByPessoa(idCliente);
		TypedFlatMap map = null;
		if (list != null && !list.isEmpty()) {
			for (InscricaoEstadual ie : list) {
				map = new TypedFlatMap();
				map.put("idInscricaoEstadual", ie.getIdInscricaoEstadual());
				map.put("nrInscricaoEstadual", ie.getNrInscricaoEstadual());
				map.put("tpSituacao", ie.getTpSituacao().getValue());
				listMap.add(map);
			}
		}

		return listMap;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findEnderecoByCliente", authenticationRequired=false)
	public TypedFlatMap findEnderecoByCliente(TypedFlatMap criteria) {
		Long idCliente = criteria.getLong("idCliente");
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findByIdPessoa(idCliente);
		TipoEnderecoPessoa tipoEnderecoPessoa = tipoEnderecoPessoaService.findTipoEnderecoPessoaByEnderecoPessoa(enderecoPessoa
						.getIdEnderecoPessoa());
		TypedFlatMap map = parseEnderecoPessoaToMap(enderecoPessoa);
		if (tipoEnderecoPessoa != null) {
			map.put("tpEndereco", tipoEnderecoPessoa.getTpEndereco().getValue());
		}
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getDivisaoByIdClienteCalculo", authenticationRequired=false)
	public List<TypedFlatMap> getDivisaoByIdClienteCalculo(TypedFlatMap criteria) {
		Long idCliente = criteria.getLong("idCliente");
		List l = divisaoClienteService.findByIdCliente(idCliente);

		List<TypedFlatMap> listMap = new ArrayList<TypedFlatMap>();
		for (Object object : l) {
			Map<String, Object> map = (Map<String, Object>) object;

			String keyValorDominio = map.get("idDivisaoCliente").toString();
			String dsValorDominio = map.get("dsDivisaoCliente").toString();
			listMap.add(createMapToDominio(keyValorDominio, dsValorDominio));
		}

		return listMap;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getClienteById", authenticationRequired=false)
	public TypedFlatMap getClienteById(TypedFlatMap criteria) {
		Long idCliente = criteria.getLong("idCliente");
		Cliente c = clienteService.findByIdComPessoa(idCliente);
		return parseClienteToMap(c);
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getDivisaoClienteById", authenticationRequired=false)
	public TypedFlatMap getDivisaoClienteById(TypedFlatMap criteria) {
		Long idDivisaoCliente = criteria.getLong("idDivisaoCliente");
		TypedFlatMap map = new TypedFlatMap();
		DivisaoCliente dc = divisaoClienteService.findById(idDivisaoCliente);

		map.put("idDivisaoCliente", dc.getIdDivisaoCliente());
		map.put("cdDivisaoCliente", dc.getCdDivisaoCliente());
		map.put("dsDivisaoCliente", dc.getDsDivisaoCliente());
		map.put("nrQtdeDocsRomaneio", dc.getNrQtdeDocsRomaneio());
		map.put("tpSituacao", dc.getTpSituacao().getValue());
		return map;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getResponsavelByIdCliente", authenticationRequired=false)
	public List<TypedFlatMap> getResponsavelByIdCliente(TypedFlatMap criteria) {
		Long idCliente = criteria.getLong("idCliente");
		Cliente responsavel = clienteService
				.findClienteResponsavelByIdCliente(idCliente);
		Cliente c = clienteService
				.findClienteByIdInitialazeResponsavelFrete(responsavel
						.getIdCliente());
		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();
		list.add((TypedFlatMap) parseClienteToMap(c));

		return list;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getPessoaById", authenticationRequired=false)
	public TypedFlatMap getPessoaById(TypedFlatMap criteria) {
		Long idPessoa = criteria.getLong("idPessoa");
		Pessoa pessoa = pessoaService.findById(idPessoa);
		pessoa.getIdPessoa();
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.putAll(parsePessoaToMap(pessoa));
		tfm.putAll(parseEnderecoPessoaToMap(pessoa.getEnderecoPessoa()));

		return tfm;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getTabDivClienteByIdDivCliente", authenticationRequired=false)
	public List<TypedFlatMap> getTabDivClienteByIdDivCliente(TypedFlatMap criteria) {
		Long idDivisaoCliente = criteria.getLong("idDivisaoCliente");
		List<TabelaDivisaoCliente> listTabelaDivisaoCliente = tabelaDivisaoClienteService
				.findByDivisaoCliente(idDivisaoCliente);
		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();

		for (TabelaDivisaoCliente tabelaDivisaoCliente : listTabelaDivisaoCliente) {
			list.add(parseTabelaDivisaClienteToMap(tabelaDivisaoCliente));
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findAllEstadosByPaisAndSituacao", authenticationRequired=false)
	public List<TypedFlatMap> findAllEstadosByPaisAndSituacao() {
		Pais pais = paisService.findPaisBySgPais(SIGLA_BRASIL);
		List<UnidadeFederativa> listUf = unidadeFederativaService.findUfsByPais(pais.getIdPais(), SITUACAO_ATIVA);
		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();
		for (UnidadeFederativa uf : listUf) {
			list.add(parseUfToMap(uf));
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findMunicipiosByEstado", authenticationRequired=false)
	public List<TypedFlatMap> findMunicipiosByEstado(TypedFlatMap cri) {
		Long idUnidadeFederativa = cri.getLong("idUnidadeFederativa");
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("unidadeFederativa.idUnidadeFederativa",
				idUnidadeFederativa);
		List<Municipio> listMunicipio = municipioService.find(criteria);
		UnidadeFederativa uf = unidadeFederativaService
				.findById(idUnidadeFederativa);
		Map<String, Object> mapUf = parseUfToMap(uf);

		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();
		for (Municipio municipio : listMunicipio) {
			TypedFlatMap map = parseMunicipioToMap(municipio);
			map.putAll(mapUf);
			list.add(map);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findNaturezaProduto", authenticationRequired=false)
	public TypedFlatMap findNaturezaProduto(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		List<NaturezaProduto> naturezaProduto = naturezaProdutoService.findAllAtivo();
		tfm.put("naturezaProduto", naturezaProduto);
		return tfm;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findTabelaByDivisao", authenticationRequired=false)
	public TypedFlatMap findTabelaByDivisao(TypedFlatMap criteria) {
		
		TypedFlatMap tfm = new TypedFlatMap();
		Long idDivisaoCliente = criteria.getLong("idDivisaoCliente");
		List<TabelaDivisaoCliente> tabelaByDivisao = tabelaDivisaoClienteService.findByDivisaoCliente(idDivisaoCliente);
		
		TabelaDivisaoCliente tabelaDivisaoCliente = null;
		if(tabelaByDivisao != null && !tabelaByDivisao.isEmpty()) {
			for (int i = 0; i < tabelaByDivisao.size(); i++) {
				tabelaDivisaoCliente = tabelaByDivisao.get(i);
				
				String value = tabelaDivisaoCliente.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue()+
								tabelaDivisaoCliente.getTabelaPreco().getTipoTabelaPreco().getNrVersao()+
								tabelaDivisaoCliente.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco();
				
				tfm.put(String.valueOf(tabelaDivisaoCliente.getTabelaPreco().getIdTabelaPreco()), value);
			}
		}
		return tfm;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findNomesMunicipiosCotacaoPendente", authenticationRequired=false)
	public TypedFlatMap findNomesMunicipiosCotacaoPendente(TypedFlatMap criteria) {
		Long[][] idsRadCotacoes = (Long[][])criteria.get("idsRadCotacoes");
		int tamanho = idsRadCotacoes.length;
		String[][] retornoIdsRadCotacoes = new String[tamanho][3];
		TypedFlatMap tfm = new TypedFlatMap();
		for (int i = 0; i < tamanho; i++) {
			Long idRadarCotacao = idsRadCotacoes[i][0];
			Long idMunicipioOrigem = idsRadCotacoes[i][1];
			Long idMunicipioDestino =idsRadCotacoes[i][2];
			Municipio municipioOrigem = municipioService.findById(idMunicipioOrigem);
	        Municipio municipioDestino = municipioService.findById(idMunicipioDestino);
	        retornoIdsRadCotacoes[i][0] = String.valueOf(idRadarCotacao);
	        retornoIdsRadCotacoes[i][1] = municipioOrigem.getNmMunicipioAndSgUnidadeFederativa();
	        retornoIdsRadCotacoes[i][2] = municipioDestino.getNmMunicipioAndSgUnidadeFederativa();
		}
		tfm.put("municipiosByIdRadCotacao", retornoIdsRadCotacoes);
		return tfm;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findAllEstados", authenticationRequired=false)
	public List<TypedFlatMap> findAllEstados(TypedFlatMap criteria) {
		return unidadeFederativaService.findUnidadeFederativa("BR", SITUACAO_ATIVA);
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getSitTributariaByIE", authenticationRequired=false)
	public List<TypedFlatMap> getSitTributariaByIE(TypedFlatMap criteria) {
		Long idInscricaoEstadual = criteria.getLong("idInscricaoEstadual");
		List<TipoTributacaoIE> list = tipoTributacaoIEService
				.findTiposTributacaoIEVigente(idInscricaoEstadual,
						JTDateTimeUtils.getDataAtual(), Boolean.FALSE);

		List<TypedFlatMap> listMap = new ArrayList<TypedFlatMap>();
		for (TipoTributacaoIE tipoTributacaoIE : list) {
			listMap.add(parseTipoTributacaoIEToMap(tipoTributacaoIE));
		}

		return listMap;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getTipoDestinoByIdLocalizacaoMunicipio", authenticationRequired=false)
	public List<TypedFlatMap> getTipoDestinoByIdLocalizacaoMunicipio(TypedFlatMap criteria) {
		Long idTipoLocalizacaoMunicipio = criteria.getLong("idTipoLocalizacaoMunicipio");
		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio = tipoLocalizacaoMunicipioService.findById(idTipoLocalizacaoMunicipio);
		String keyValorDominio = tipoLocalizacaoMunicipio.getIdTipoLocalizacaoMunicipio().toString();
		String dsValorDominio = tipoLocalizacaoMunicipio.getDsTipoLocalizacaoMunicipio().getValue().replaceAll(".*?»(.*?)¦", "\1");
		TypedFlatMap map = createMapToDominio(keyValorDominio, dsValorDominio);
		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();
		list.add(map);

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "hasParametroGeralDimensoes", authenticationRequired=false)
	public TypedFlatMap hasParametroGeralDimensoes(TypedFlatMap cri) {
		String param = cri.getString("nmParametroGeral");
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("dsConteudo", SIM);
		criteria.put("nmParametroGeral", param);

		List<ParametroGeral> l = parametroGeralService.find(criteria);
		if (!l.isEmpty()){
			return parseParametroGeralToMap(l.get(0));
		}
		return null;
	}

	private TypedFlatMap parseParametroGeralToMap(ParametroGeral parametroGeral) {
		String keyValorDominio = parametroGeral.getIdParametroGeral()
				.toString();
		String dsValorDominio = parametroGeral.getDsConteudo();
		return createMapToDominio(keyValorDominio, dsValorDominio);
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "hasParametroFilialOrigemDimensoes", authenticationRequired=false)
	public TypedFlatMap hasParametroFilialOrigemDimensoes(TypedFlatMap criteria) {
		Long idFilial = criteria.getLong("idFilial");
		ConteudoParametroFilial cpf = conteudoParametroFilialService
				.findConteudoParametroFilial(idFilial, "DIMENSAO_OBRIGATORIA",
						SIM);

		TypedFlatMap map = null;
		if (cpf != null && cpf.getParametroFilial() != null) {
			ParametroFilial p = cpf.getParametroFilial();
			String keyValorDominio = p.getIdParametroFilial().toString();
			String dsValorDominio = p.getNmParametroFilial();
			map = createMapToDominio(keyValorDominio, dsValorDominio);
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "hasParametroClienteCalculoDimensoes", authenticationRequired=false)
	public TypedFlatMap hasParametroClienteCalculoDimensoes(TypedFlatMap cri) {
		Long idDivisaoCliente = cri.getLong("idDivisaoCliente");
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("blObrigaDimensoes", Boolean.TRUE);
		criteria.put("divisaoCliente.idDivisaoCliente", idDivisaoCliente);

		List<TabelaDivisaoCliente> l = tabelaDivisaoClienteService
				.find(criteria);

		TypedFlatMap map = null;
		if (l != null && !l.isEmpty()) {
			String keyValorDominio = l.get(0).getIdTabelaDivisaoCliente()
					.toString();
			String dsValorDominio = BooleanUtils.toString(l.get(0)
					.getBlObrigaDimensoes(), TRUE, FALSE);
			map = createMapToDominio(keyValorDominio, dsValorDominio);
		}

		return map;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getProibidoEmbarqueByIdCliente", authenticationRequired=false)
	public TypedFlatMap getProibidoEmbarqueByIdCliente(TypedFlatMap criteria) {
		Long idCliente = criteria.getLong("idCliente");
		ProibidoEmbarque proibidoEmbarque = proibidoEmbarqueService.findProibidoEmbarque(idCliente, null, null);
		return parseProibidoEmbarqueToMap(proibidoEmbarque);
	}

	private TypedFlatMap parseProibidoEmbarqueToMap(
			ProibidoEmbarque proibidoEmbarque) {
		if (proibidoEmbarque != null) {
			TypedFlatMap map = new TypedFlatMap();

			map.put("idProibidoEmbarque",
					proibidoEmbarque.getIdProibidoEmbarque());
			map.put("idMotivoProibidoEmbarque", proibidoEmbarque
					.getMotivoProibidoEmbarque().getIdMotivoProibidoEmbarque());
			map.put("idCliente", proibidoEmbarque.getCliente().getIdCliente());
			map.put("idUsuarioBloqueio", proibidoEmbarque
					.getUsuarioByIdUsuarioBloqueio().getIdUsuario());
			map.put("dtBloqueio", proibidoEmbarque.getDtBloqueio());
			map.put("idUsuarioDesbloqueio", proibidoEmbarque
					.getUsuarioByIdUsuarioDesbloqueio().getIdUsuario());
			map.put("dtDesbloqueio", proibidoEmbarque.getDtDesbloqueio());
			map.put("dsDesbloqueio", proibidoEmbarque.getDsDesbloqueio());
			map.put("dsBloqueio", proibidoEmbarque.getDsBloqueio());

			return map;
		}

		return null;
	}

	@SuppressWarnings({"unchecked" })
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getMunicipioFilialByMunicipioFilial", authenticationRequired=false)
	public List<TypedFlatMap> getMunicipioFilialByMunicipioFilial(TypedFlatMap criteria) {
		Long idFilial = criteria.getLong("idFilial");
		Long idMunicipio = criteria.getLong("idMunicipio");
		List<MunicipioFilial> list = municipioFilialService.findMunicipioFilialVigente(idMunicipio, idFilial);
		List<TypedFlatMap> listMap = new ArrayList<TypedFlatMap>();
		for (MunicipioFilial municipioFilial : list) {
			listMap.add(parseMunicipioFilialToMap(municipioFilial));
		}

		return listMap;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getLiberacaoEmbarqueByMunicipioClienteTpModal", authenticationRequired=false)
	public List<TypedFlatMap> getLiberacaoEmbarqueByMunicipioClienteTpModal(TypedFlatMap criteria) {
		String tpModal = criteria.getString("tpModal");
		Long idMunicipio = criteria.getLong("idMunicipio");
		Long idCliente = criteria.getLong("idCliente");
		DomainValue domainTpModal = new DomainValue(tpModal);
		List<LiberacaoEmbarque> l = liberacaoEmbarqueService
				.findLiberacaoCliente(idCliente, idMunicipio, domainTpModal);

		List<TypedFlatMap> listMap = new ArrayList<TypedFlatMap>();
		for (LiberacaoEmbarque liberacaoEmbarque : l) {
			listMap.add(parseLiberacaoEmbarqueToMap(liberacaoEmbarque));
		}

		return listMap;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findCotacoesByUserAndTipoSituacaoPaginated", authenticationRequired=false)
	public List<TypedFlatMap> findCotacoesByUserAndTipoSituacaoPaginated(TypedFlatMap criteria){
		List<Object[]> listFromResult = cotacaoService.findCotacoesByUserAndTipoSituacaoPaginated(criteria);
		return parseResultCotacoesByUserToMap(listFromResult);
	}
	

	@SuppressWarnings({ "unchecked" })
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getNrCubagemByDivisao", authenticationRequired=false)
	public TypedFlatMap getNrCubagemByDivisao(TypedFlatMap cri) {
		Long idDivisao = cri.getLong("idDivisao");
		Long idTabelaPreco = cri.getLong("idTabelaPreco");
		
		Map<String, Object> criteria = new HashMap<String, Object>();

		criteria.put("divisaoCliente.idDivisaoCliente", idDivisao);
		criteria.put("tabelaPreco.idTabelaPreco", idTabelaPreco);
		List<TabelaDivisaoCliente> list = tabelaDivisaoClienteService
				.find(criteria);

		TypedFlatMap map = null;
		for (TabelaDivisaoCliente tabelaDivisaoCliente : list) {
			map = new TypedFlatMap();
			String keyValorDominio = tabelaDivisaoCliente
					.getIdTabelaDivisaoCliente().toString();
			
			String dsValorDominio = "";
			if(tabelaDivisaoCliente.getNrFatorCubagem() != null){
				dsValorDominio = tabelaDivisaoCliente.getNrFatorCubagem().toString();
			} else{
				dsValorDominio = buscarValoresDefaultCubagem(tabelaDivisaoCliente.getServico().getTpModal()); 
			}
			
			map = createMapToDominio(keyValorDominio, dsValorDominio);
		}

		return map;
	}
	
	private String buscarValoresDefaultCubagem(DomainValue tpModal) {
		String dsValorDominio = "";
		
		if(ConstantesExpedicao.MODAL_AEREO.equals(tpModal)){
			ParametroGeral valorDefaultCubagemAereo = parametroGeralService.findByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_AEREO);
			dsValorDominio = valorDefaultCubagemAereo.getDsConteudo();
		} else if(ConstantesExpedicao.MODAL_RODOVIARIO.equals(tpModal)){
			ParametroGeral valorDefaultCubagemRodoviario = parametroGeralService.findByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_RODO);
			dsValorDominio = valorDefaultCubagemRodoviario.getDsConteudo();
		}
		return dsValorDominio;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getAeroportoByFilial", authenticationRequired=false)
	public List<TypedFlatMap> getAeroportoByFilial(TypedFlatMap criteria) {
		Long idFilial = criteria.getLong("idFilial");
		criteria = new TypedFlatMap();
		criteria.put("filial.idFilial", idFilial);

		List<Filial> l = aeroportoService.findByIdFilial(idFilial);

		List<TypedFlatMap> listMap = new ArrayList<TypedFlatMap>();
		for (Filial filialAeroporto : l) {
			listMap.add(parseAeroportoToMap(filialAeroporto));
		}

		return listMap;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "findNroCepByIdMunicipio", authenticationRequired=false)
	public TypedFlatMap findNroCepByIdMunicipio(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		Municipio municipio = municipioService.findById(criteria.getLong("idMunicipio"));
		if(municipio != null){
			tfm.put("nroCep", municipio.getNrCep());
		}
		return tfm;
	}

	@SuppressWarnings("unchecked")
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "getProdutoEspecifico", authenticationRequired=false)
	public List<TypedFlatMap> getProdutoEspecifico() {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("tpSituacao", SITUACAO_ATIVA);
		List<ProdutoEspecifico> l = produtoEspecificoService.find(criteria);

		Collections.sort(l, new Comparator<ProdutoEspecifico>() {
			@Override
			public int compare(ProdutoEspecifico o1, ProdutoEspecifico o2) {
				ProdutoEspecifico produtoEspecifico1 = (ProdutoEspecifico) o1;
				ProdutoEspecifico produtoEspecifico2 = (ProdutoEspecifico) o2;
				return produtoEspecifico1
						.getDsProdutoEspecifico()
						.getValue()
						.compareTo(
								produtoEspecifico2.getDsProdutoEspecifico()
										.getValue());
			}
		});

		List<TypedFlatMap> listMap = new ArrayList<TypedFlatMap>();
		for (ProdutoEspecifico produtoEspecifico : l) {
			String keyValorDominio = produtoEspecifico.getIdProdutoEspecifico()
					.toString();
			String dsValorDominio = produtoEspecifico.getDsProdutoEspecifico()
					.getValue();
			listMap.add(createMapToDominio(keyValorDominio, dsValorDominio));
		}

		return listMap;
	}

	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "saveCotacao", authenticationRequired=false)
	public TypedFlatMap saveCotacao(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		Long idCotacao = null;
		try {
			Cotacao cotacao = parseToCotacao(criteria);
			cotacaoService.store(cotacao);
			idCotacao = cotacao.getIdCotacao();
        } catch (Exception ex) {
			LOGGER.error("Ocorreu problemas a salvar a cotação: ", ex);
		}
		tfm.put("idCotacao", idCotacao);
		return tfm;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "saveParcelasCotacao", authenticationRequired=false)
	public TypedFlatMap saveParcelasCotacao(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		List<ParcelaCotacao> parcelasCotacao = parseMapToListParcelaCotacoes(criteria);
		if (parcelasCotacao != null && !parcelasCotacao.isEmpty()) {
			for (ParcelaCotacao parcelaCotacao : parcelasCotacao) {
				parcelaCotacaoService.store(parcelaCotacao);
			}
		}
		return tfm;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.cotacaoOnline", processName = "saveDimensoesCotacao", authenticationRequired=false)
	public TypedFlatMap saveDimensoesCotacao(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		List<Dimensao> dimensoesCotacao = parseMapToListDimensoes(criteria);
		if (dimensoesCotacao != null && !dimensoesCotacao.isEmpty()) {
			for (Dimensao dimensao : dimensoesCotacao) {
				dimensaoService.store(dimensao);
			}
		}
		return tfm;
	}

	private Cotacao parseToCotacao(TypedFlatMap criteria) {
		Cotacao cotacao = new Cotacao();
		parseToCotacaoEnderecos(criteria, cotacao);
		parseToCotacaoIdentificacao(criteria, cotacao);
		parseToCotacaoValores(criteria, cotacao);
		parseToCotacaoDatas(criteria, cotacao);
		parseToCotacaoTipos(criteria, cotacao);
		parseToCotacaoCondicional(criteria, cotacao);
		parseToCotacaoOutros(criteria, cotacao);
		
		return cotacao;
	}

	private void parseToCotacaoOutros(TypedFlatMap criteria, Cotacao cotacao) {
		if (getValorCampoNumericoLong(criteria.getLong("ID_SERVICO")) != null) {
			Servico servico = servicoService.findById(criteria.getLong("ID_SERVICO"));
			cotacao.setServico(servico);
		}
		
		if (getValorCampoNumericoLong(criteria.getLong("ID_PRODUTO_ESPECIFICO")) != null) {
			ProdutoEspecifico produtoEspecifico = produtoEspecificoService.findById(criteria.getLong("ID_PRODUTO_ESPECIFICO"));
			cotacao.setProdutoEspecifico(produtoEspecifico);
		}
		if (getValorCampoNumericoLong(criteria.getLong("NR_NOTA_FISCAL")) != null) {
			String nroNota = String.valueOf(criteria.getLong("NR_NOTA_FISCAL"));
			cotacao.setNrNotaFiscal(Integer.parseInt(nroNota));
		}
		
		if (getValorCampoNumericoLong(criteria.getLong("ID_DOCTO_SERVICO")) != null) {
			DoctoServico doctoServico = doctoServicoService.findById(criteria.getLong("ID_DOCTO_SERVICO"));
			cotacao.setDoctoServico(doctoServico);
		}
		if (criteria.getString("DS_MOTIVO") != null){
			cotacao.setDsMotivo(criteria.getString("DS_MOTIVO"));
		}
		
		if (criteria.getString("NR_DOCUMENTO_COTACAO") != null){
			cotacao.setNrDocumentoCotacao(criteria.getString("NR_DOCUMENTO_COTACAO"));
		}
		if (criteria.getString("OB_COTACAO") != null){
			cotacao.setObCotacao(criteria.getString("OB_COTACAO"));
		}
		
		Long idPendenciaAprovacao = getValorCampoNumericoLong(criteria.getLong("ID_PENDENCIA_APROVACAO"));
		if (idPendenciaAprovacao != null) {
			Pendencia pendencia = pendenciaService.findById(idPendenciaAprovacao);
			cotacao.setPendencia(pendencia);
		}
	}

	private void parseToCotacaoCondicional(TypedFlatMap criteria,
			Cotacao cotacao) {
		String blColetaEmergencia = criteria.getString("BL_COLETA_EMERGENCIA");
		if (blColetaEmergencia != null){
			cotacao.setBlColetaEmergencia(convertStringToBoolean(blColetaEmergencia));
		}
		String blEntregaEmergencia = criteria.getString("BL_ENTREGA_EMERGENCIA");
		if (blEntregaEmergencia != null){
			cotacao.setBlEntregaEmergencia(convertStringToBoolean(blEntregaEmergencia));
		}
		String blMercadoriaExport = criteria.getString("BL_MERCADORIA_EXPORTACAO");
		if (blMercadoriaExport != null){
			cotacao.setBlMercadoriaExportacao(convertStringToBoolean(blMercadoriaExport));
		}
		if (criteria.getBigDecimal("VL_ICMS_ST") != null){
			cotacao.setVlIcmsSubstituicaoTributaria(criteria.getBigDecimal("VL_ICMS_ST"));
		}
		String blIncidenciaIcmsPedagio = criteria.getString("BL_INCIDENCIA_ICMS_PEDAGIO");
		if (blIncidenciaIcmsPedagio != null){
			cotacao.setBlIncideIcmsPedagio(convertStringToBoolean(blIncidenciaIcmsPedagio));
		}
		String blIndicadorSeguro = criteria.getString("BL_INDICADOR_SEGURO");
		if (blIndicadorSeguro != null) {
			cotacao.setBlIndicadorSeguro(convertStringToBoolean(blIndicadorSeguro));
		}
	}

	private void parseToCotacaoTipos(TypedFlatMap criteria, Cotacao cotacao) {
		String tpSituacao = criteria.getString("TP_SITUACAO");
		if (tpSituacao != null) {
			Domain domain = domainService.findById(DM_STATUS_COTACAO);
			DomainValue tpSituacaoResult = domainValueService.findDomainValueByValue(domain.getName(), tpSituacao);
			cotacao.setTpSituacao(tpSituacaoResult);
		}
		String tpFrete = criteria.getString("TP_FRETE");
		if (tpFrete != null) {
			Domain domain = domainService.findById(DM_TIPO_FRETE);
			DomainValue tpFreteResult = domainValueService.findDomainValueByValue(domain.getName(), tpFrete);
			cotacao.setTpFrete(tpFreteResult);
		}
		String tpSitTribRemetente = criteria.getString("TP_SIT_TRIBUTARIA_REMETENTE");
		if (tpSitTribRemetente != null) {
			Domain domain = domainService.findById(DM_SIT_TRIBUTARIA);
			DomainValue tpSitTribRemetenteResult = domainValueService.findDomainValueByValue(domain.getName(), tpSitTribRemetente);
			cotacao.setTpSitTributariaRemetente(tpSitTribRemetenteResult);
		}
		String tpSitTribDestinatario = criteria.getString("TP_SIT_TRIBUTARIA_DESTINATARIO");
		if (tpSitTribDestinatario != null) {
			Domain domain = domainService.findById(DM_SIT_TRIBUTARIA);
			DomainValue tpSitTribDestinatarioResult = domainValueService.findDomainValueByValue(domain.getName(), tpSitTribDestinatario);
			cotacao.setTpSitTributariaDestinatario(tpSitTribDestinatarioResult);
		}
		String tpSitTribResponsavel = criteria.getString("TP_SIT_TRIBUTARIA_RESPONSAVEL");
		if (tpSitTribResponsavel != null) {
			Domain domain = domainService.findById(DM_SIT_TRIBUTARIA);
			DomainValue tpSitTribResponsavelResult = domainValueService.findDomainValueByValue(domain.getName(), tpSitTribResponsavel);
			cotacao.setTpSitTributariaResponsavel(tpSitTribResponsavelResult);
		}
		String tpDevedorFrete = criteria.getString("TP_DEVEDOR_FRETE");
		if (tpDevedorFrete != null) {
			Domain domain = domainService.findById(DM_TIPO_PESSOA_COTACAO);
			DomainValue tpDevedorFreteResult = domainValueService.findDomainValueByValue(domain.getName(), tpDevedorFrete);
			cotacao.setTpDevedorFrete(tpDevedorFreteResult);
		}
		
		String tpDoctoCotacao = criteria.getString("TP_DOCTO_COTACAO");
		if (tpDoctoCotacao != null) {
			Domain domain = domainService.findById(DM_TIPO_DOCUMENTO);
			DomainValue tpDoctoCotacaoResult = domainValueService.findDomainValueByValue(domain.getName(), tpDoctoCotacao);
			cotacao.setTpDocumentoCotacao(tpDoctoCotacaoResult);
		}
		String tpSitAprovacao = criteria.getString("TP_SITUACAO_APROVACAO");
		if (tpSitAprovacao != null) {
			Domain domain = domainService.findById(DM_SITUACAO_APROVACAO_COTACAO);
			DomainValue tpSitAprovacaoResult = domainValueService.findDomainValueByValue(domain.getName(), tpSitAprovacao);
			cotacao.setTpSituacaoAprovacao(tpSitAprovacaoResult);
		}
		String tpModoCotacao = criteria.getString("TP_MODO_COTACAO");
		if(tpModoCotacao != null){
			cotacao.setTpModoCotacao(tpModoCotacao);
		}
		if (getValorCampoNumericoLong(criteria.getLong("ID_NATUREZA_PRODUTO")) != null) {
			NaturezaProduto naturezaProduto = naturezaProdutoService.findById(criteria.getLong("ID_NATUREZA_PRODUTO"));
			cotacao.setNaturezaProduto(naturezaProduto);
		}
	}

	@SuppressWarnings("all")
	private void parseToCotacaoDatas(TypedFlatMap criteria, Cotacao cotacao) {
		if (criteria.getDateTime("DT_VALIDADE") != null){
			cotacao.setDtValidade(criteria.getDateTime("DT_VALIDADE").toYearMonthDay());
		}
		if (criteria.getDateTime("DT_GERACAO_COTACAO") != null){
			cotacao.setDtGeracaoCotacao(criteria.getDateTime("DT_GERACAO_COTACAO").toYearMonthDay());
		}
		if (criteria.getDateTime("DT_APROVACAO") != null){
			cotacao.setDtAprovacao(criteria.getDateTime("DT_APROVACAO").toYearMonthDay());
		}
	}

	private void parseToCotacaoValores(TypedFlatMap criteria, Cotacao cotacao) {
		if (getValorCampoNumericoLong(criteria.getLong("ID_TABELA_PRECO")) != null) {
			TabelaPreco tabelaPreco = tabelaPrecoService.findByIdTabelaPreco(criteria.getLong("ID_TABELA_PRECO"));
			cotacao.setTabelaPreco(tabelaPreco);
		}
		if (getValorCampoNumericoLong(criteria.getLong("ID_MOEDA")) != null) {
			Moeda moeda = moedaService.findById(criteria.getLong("ID_MOEDA"));
			cotacao.setMoeda(moeda);
		}
		Double psReal = criteria.getDouble("PS_REAL");
		if (psReal != null){
			cotacao.setPsReal(new BigDecimal(psReal));
		}
		BigDecimal vlTotalCotacao = criteria.getBigDecimal("VL_TOTAL_COTACAO");
		if (vlTotalCotacao != null){
			cotacao.setVlTotalCotacao(vlTotalCotacao);
		}
		BigDecimal vlImposto = criteria.getBigDecimal("VL_IMPOSTO");
		if (vlImposto != null) {
			cotacao.setVlImposto(vlImposto);
		}
		if (criteria.getBigDecimal("VL_TOTAL_PARCELAS") != null){
			cotacao.setVlTotalParcelas(criteria.getBigDecimal("VL_TOTAL_PARCELAS"));
		}
		if (criteria.getBigDecimal("VL_TOTAL_SERVICOS") != null){
			cotacao.setVlTotalServicos(criteria.getBigDecimal("VL_TOTAL_SERVICOS"));
		}
		if (criteria.getBigDecimal("VL_DESCONTO") != null){
			cotacao.setVlDesconto(criteria.getBigDecimal("VL_DESCONTO"));
		}
		Double psCubado = criteria.getDouble("PS_CUBADO");
		if (psCubado != null) {
			cotacao.setPsCubado(new BigDecimal(psCubado));
		}
		Double vlMercadoria = criteria.getDouble("VL_MERCADORIA");
		if (vlMercadoria != null){
			cotacao.setVlMercadoria(new BigDecimal(vlMercadoria));
		}
		String tpCalculo = criteria.getString("TP_CALCULO");
		if (tpCalculo != null) {
			Domain domain = domainService.findById(DM_TIPO_CALCULO_COTACAO);
			DomainValue tpCalculoResult = domainValueService.findDomainValueByValue(domain.getName(), tpCalculo);
			cotacao.setTpCalculo(tpCalculoResult);
		}
		if (getValorCampoNumericoLong(criteria.getLong("QT_VOLUMES")) != null){
			cotacao.setQtVolumes(criteria.getLong("QT_VOLUMES"));
		}
		if (criteria.getBigDecimal("VL_FRETE_LIQUIDO") != null){
			cotacao.setVlLiquido(criteria.getBigDecimal("VL_FRETE_LIQUIDO"));
		}
	}

	private void parseToCotacaoIdentificacao(TypedFlatMap criteria, Cotacao cotacao) {
		String nmClienteDestino = criteria.getString("NM_CLIENTE_DESTINO");
		if (nmClienteDestino != null) {
			cotacao.setNmClienteDestino(nmClienteDestino);
		}
		if (getValorCampoNumericoLong(criteria.getLong("ID_USUARIO_APROVOU")) != null) {
			Usuario usuario = usuarioService.findById(criteria.getLong("ID_USUARIO_APROVOU"));
			cotacao.setUsuarioByIdUsuarioAprovou(usuario);
		}
		if (getValorCampoNumericoLong(criteria.getLong("ID_USUARIO_REALIZOU")) != null) {
			Usuario usuarioRealizou = usuarioService.findById(criteria.getLong("ID_USUARIO_REALIZOU"));
			cotacao.setUsuarioByIdUsuarioRealizou(usuarioRealizou);
		}
		if (getValorCampoNumericoLong(criteria.getLong("ID_CLIENTE_DESTINO")) != null) {
			Cliente clienteDestino = clienteService.findById(criteria.getLong("ID_CLIENTE_DESTINO"));
			cotacao.setClienteByIdClienteDestino(clienteDestino);
		}
		
		if (getValorCampoNumericoLong(criteria.getLong("ID_DIVISAO_CLIENTE")) != null) {
			DivisaoCliente divisaoCliente = divisaoClienteService.findById(criteria.getLong("ID_DIVISAO_CLIENTE"));
			cotacao.setDivisaoCliente(divisaoCliente);
		}
		if (getValorCampoNumericoLong(criteria.getLong("ID_CLIENTE")) != null) {
			Cliente cliente = clienteService.findById(criteria.getLong("ID_CLIENTE"));
			cotacao.setClienteByIdCliente(cliente);
		}
			
			
		if (getValorCampoNumericoLong(criteria.getLong("NR_TELEFONE")) != null){
			cotacao.setNrTelefone(criteria.getLong("NR_TELEFONE"));
		}
		if (getValorCampoNumericoLong(criteria.getLong("NR_FAX")) != null){
			cotacao.setNrFax(criteria.getLong("NR_FAX"));
		}
		if (criteria.getString("NR_IDENTIF_CLIENTE_REM") != null){
			cotacao.setNrIdentifClienteRem(criteria.getString("NR_IDENTIF_CLIENTE_REM"));
		}
		if (criteria.getString("NR_IDENTIF_CLIENTE_DEST") != null){
			cotacao.setNrIdentifClienteDest(criteria.getString("NR_IDENTIF_CLIENTE_DEST"));
		}
		if (criteria.getString("NR_IDENTIF_RESPONS_FRETE") != null){
			cotacao.setNrIdentifResponsFrete(criteria.getString("NR_IDENTIF_RESPONS_FRETE"));
		}
		if (getValorCampoNumericoLong(criteria.getLong("NR_PPE")) != null) {
			String nrPpe = String.valueOf(criteria.getLong("NR_PPE"));
			if (nrPpe != null){
				cotacao.setNrPpe(Short.parseShort(nrPpe));
			}
		}
		if (criteria.getString("DS_CONTATO") != null){
			cotacao.setDsContato(criteria.getString("DS_CONTATO"));
		}
		if (criteria.getString("DS_EMAIL") != null){
			cotacao.setDsEmail(criteria.getString("DS_EMAIL"));
		}
		if (getValorCampoNumericoLong(criteria.getLong("ID_CLIENTE_SOLICITOU")) != null) {
			Cliente clienteSolicitou = clienteService.findById(criteria.getLong("ID_CLIENTE_SOLICITOU"));
			cotacao.setClienteByIdClienteSolicitou(clienteSolicitou);
		}
		if (criteria.getString("NM_CLIENTE_REMETENTE") != null){
			cotacao.setNmClienteRemetente(criteria.getString("NM_CLIENTE_REMETENTE"));
		}
		if (criteria.getString("NM_RESPONSAVEL_FRETE") != null){
			cotacao.setNmResponsavelFrete(criteria.getString("NM_RESPONSAVEL_FRETE"));
		}
		Long idIeDestinatario = getValorCampoNumericoLong(criteria.getLong("ID_IE_DESTINATARIO"));
		if (idIeDestinatario != null) {
			InscricaoEstadual ieDestinatario = inscricaoEstadualService.findById(idIeDestinatario);
			cotacao.setInscricaoEstadualDestinatario(ieDestinatario);
		}
		Long idIeRemetente = getValorCampoNumericoLong(criteria.getLong("ID_IE_REMETENTE"));
		if (idIeRemetente != null) {
			InscricaoEstadual ieRemetente = inscricaoEstadualService.findById(idIeRemetente);
			cotacao.setInscricaoEstadualRemetente(ieRemetente);
		}
		Long idIeResponsavel = getValorCampoNumericoLong(criteria.getLong("ID_IE_RESPONSAVEL"));
		if (idIeResponsavel != null) {
			InscricaoEstadual ieResponsavel = inscricaoEstadualService.findById(idIeResponsavel);
			cotacao.setInscricaoEstadualResponsavel(ieResponsavel);
		}
	}

	private void parseToCotacaoEnderecos(TypedFlatMap criteria, Cotacao cotacao) {
		Long idFilial = getValorCampoNumericoLong(criteria.getLong("ID_FILIAL"));
		if (idFilial != null) {
			Filial filial = filialService.findById(idFilial);
			cotacao.setFilial(filial);
			int nroCotacao = conteudoParametroFilialService.generateProximoValorParametroSequencial(idFilial, ConstantesVendas.NR_ULTIMA_COTACAO, true).intValue();
			cotacao.setNrCotacao(nroCotacao);
		}
		if (getValorCampoNumericoLong(criteria.getLong("ID_FILIAL_ORIGEM")) != null) {
			Filial filialOrigem = filialService.findById(criteria.getLong("ID_FILIAL_ORIGEM"));
			cotacao.setFilialByIdFilialOrigem(filialOrigem);
		}

		if (getValorCampoNumericoLong(criteria.getLong("ID_FILIAL_DESTINO")) != null) {
			Filial filialDestino = filialService.findById(criteria.getLong("ID_FILIAL_DESTINO"));
			cotacao.setFilialByIdFilialDestino(filialDestino);
		}
		
		if (getValorCampoNumericoLong(criteria.getLong("ID_MUNICIPIO_ORIGEM")) != null) {
			Municipio municipioOrigem = municipioService.findById(criteria.getLong("ID_MUNICIPIO_ORIGEM"));
			cotacao.setMunicipioByIdMunicipioOrigem(municipioOrigem);
		}
		if (getValorCampoNumericoLong(criteria.getLong("ID_MUNICIPIO_DESTINO")) != null) {
			Municipio municipioDestino = municipioService.findById(criteria.getLong("ID_MUNICIPIO_DESTINO"));
			cotacao.setMunicipioByIdMunicipioDestino(municipioDestino);
		}
		
		if (getValorCampoNumericoLong(criteria.getLong("ID_AEROPORTO_ORIGEM")) != null) {
			Aeroporto aeroportoOrigem = aeroportoService.findById(criteria.getLong("ID_AEROPORTO_ORIGEM"));
			cotacao.setAeroportoByIdAeroportoOrigem(aeroportoOrigem);
		}
		if (getValorCampoNumericoLong(criteria.getLong("ID_AEROPORTO_DESTINO")) != null) {
			Aeroporto aeroportoDestino = aeroportoService.findById(criteria.getLong("ID_AEROPORTO_DESTINO"));
			cotacao.setAeroportoByIdAeroportoDestino(aeroportoDestino);
		}
		
		Long idFilialResponsavel = getValorCampoNumericoLong(criteria.getLong("ID_FILIAL_RESPONSAVEL"));
		if (idFilialResponsavel != null) {
			Filial filialOrigemResponsavel = filialService.findById(idFilialResponsavel);
			cotacao.setFilialByIdFilialResponsavel(filialOrigemResponsavel);
		}
		Long idMunicipioResp = getValorCampoNumericoLong(criteria.getLong("ID_MUNICIPIO_RESPONSAVEL"));
		if (idMunicipioResp != null) {
			Municipio municipioResponsavel = municipioService.findById(idMunicipioResp);
			cotacao.setMunicipioByIdMunicipioResponsavel(municipioResponsavel);
		}
	}

	/**
	 * Reponsável por gravar corretamente os campos do tipo Long no banco de
	 * dados.
	 * 
	 * @param campo
	 * @return Valor do campo ou nulo caso o campo = 0L
	 */
	private Long getValorCampoNumericoLong(Long campo) {
		if (campo != null) {
			if (campo.longValue() == 0) {
				return null; // correção para erros de conversão no resulset
			}

			return campo;
		}
		return null;
	}

	@SuppressWarnings("all")
	private List<Map<String, Object>> parseServicoToListMap(List listServicoByTpAbrangencia) {
		List<Map<String, Object>> listServicosMap = new ArrayList<Map<String, Object>>();
		for (Iterator iter = listServicoByTpAbrangencia.iterator(); iter
				.hasNext();) {
			Map s = (Map) iter.next();
			listServicosMap.add(s);
		}
		return listServicosMap;
	}

	@SuppressWarnings("unchecked")
	private List<ParcelaCotacao> parseMapToListParcelaCotacoes(TypedFlatMap tfm) {
		List<ParcelaCotacao> parcelasCotacao = null;
		List<Map<String, Object>> list = (List<Map<String, Object>>) tfm.get("parcelasCotacao");
		if (list != null && !list.isEmpty()) {
			parcelasCotacao = new ArrayList<ParcelaCotacao>();
			ParcelaCotacao pc = null;
			for (Map<String, Object> map : list) {
				pc = new ParcelaCotacao();
				Double vlParcelaCotacao = (Double) map.get("vlParcelaCotacao");
				Long idParcelaPreco = (Long) map.get("idParcelaPreco");
				Long idCotacao = (Long) map.get("idCotacao");
				Double vlParcelaBruto = (Double) map.get("vlParcelaBruto");
				if (vlParcelaCotacao != null){
					pc.setVlParcelaCotacao(new BigDecimal(vlParcelaCotacao));
				}
				if (getValorCampoNumericoLong(idParcelaPreco) != null) {
					ParcelaPreco parcelaPreco = parcelaPrecoService.findById(idParcelaPreco);
					pc.setParcelaPreco(parcelaPreco);
				}
				if (getValorCampoNumericoLong(idCotacao) != null) {
					Cotacao cotacao = cotacaoService.findById(idCotacao);
					pc.setCotacao(cotacao);
				}
				if (vlParcelaBruto != null){
					pc.setVlBrutoParcela(new BigDecimal(vlParcelaBruto));
				}

				parcelasCotacao.add(pc);
			}
			return parcelasCotacao;
		}

		return parcelasCotacao;
	}
	
	private List<TypedFlatMap> parseResultCotacoesByUserToMap(
			List<Object[]> listFromResult) {
		List<TypedFlatMap> listRetorno = new ArrayList<TypedFlatMap>(listFromResult.size());
		TypedFlatMap map = null;
		for (final Object[] objResult : listFromResult) {
			map = new TypedFlatMap();
			map.put("idCotacao", objResult[0]);
			map.put("dsCotacao", objResult[1]);
			map.put("vlTotalCotacao", objResult[2]);
			map.put("dtGeracao", objResult[3]);
			map.put("dataValidade", objResult[4]);
			map.put("nrIdentifRemetente", objResult[5]);
			map.put("nmRemetente", objResult[6]);
			map.put("nrIdentifRemetenteCotacao", objResult[7]);
			
			map.put("nmRemetenteCotacao", objResult[8]);
			map.put("origem", objResult[9]);
			map.put("nrIdentifDestinatario", objResult[10]);
			map.put("nmDestinatario", objResult[11]);
			map.put("nrIdentifDestinatarioCotacao", objResult[12]);
			map.put("nmDestinatarioCotacao", objResult[13]);
			map.put("destino", objResult[14]);
			map.put("nrNotaFiscal", objResult[15]);
			map.put("psReal", objResult[16]);
			map.put("psCubado", objResult[17]);
			map.put("vlMercadoria", objResult[18]);
			
			listRetorno.add(map);
		}

		return listRetorno;
	}
	

	private TypedFlatMap parseLiberacaoEmbarqueToMap(
			LiberacaoEmbarque liberacaoEmbarque) {
		TypedFlatMap map = new TypedFlatMap();

		map.put("idLiberacaoEmbarque",
				liberacaoEmbarque.getIdLiberacaoEmbarque());
		map.put("idMunicipio", liberacaoEmbarque.getMunicipio()
				.getIdMunicipio());
		map.put("idCliente", liberacaoEmbarque.getCliente().getIdCliente());
		map.put("tpModal", liberacaoEmbarque.getTpModal().getValue());

		return map;
	}

	@SuppressWarnings("unchecked")
	private List<Dimensao> parseMapToListDimensoes(TypedFlatMap tfm) {
		List<Dimensao> dimensoesCotacao = null;
		List<Map<String, Object>> list = (List<Map<String, Object>>) tfm.get("dimensoesCotacao");
		if (list != null && !list.isEmpty()) {
			dimensoesCotacao = new ArrayList<Dimensao>();
			Dimensao dimensao = null;
			Long idConhecimento = null, idAwb = null, idCtoInternacional = null, idCotacao = null;
			for (Map<String, Object> map : list) {
				dimensao = new Dimensao();
				if (map.get("idConhecimento") != null){
					idConhecimento = getValorCampoNumericoLong((Long) map.get("idConhecimento"));
				}
				if (map.get("idAwb") != null){
					idAwb = getValorCampoNumericoLong((Long) map.get("idAwb"));
				}
				if (map.get("idCtoInternacional") != null){
					idCtoInternacional = getValorCampoNumericoLong((Long) map.get("idCtoInternacional"));
				}
				if (map.get("idCotacao") != null){
					idCotacao = getValorCampoNumericoLong((Long) map.get("idCotacao"));
				}
				Long nrComprimento = getValorCampoNumericoLong((Long) map.get("nrComprimento"));
				Long nrAltura = getValorCampoNumericoLong((Long) map.get("nrAltura"));
				Long nrQuantidade = getValorCampoNumericoLong((Long) map.get("nrQuantidade"));
				Long nrLargura = getValorCampoNumericoLong((Long) map.get("nrLargura"));

				if (nrComprimento != null) {
					dimensao.setNrComprimento(Integer.parseInt(String.valueOf(nrComprimento)));
				}
				if (idConhecimento != null) {
					Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);
					dimensao.setConhecimento(conhecimento);
				}
				if (idAwb != null) {
					Awb awb = awbService.findById(idAwb);
					dimensao.setAwb(awb);
				}
				if (idCtoInternacional != null) {
					CtoInternacional ctoInternacional = ctoInternacionalService.findById(idCtoInternacional);
					dimensao.setCtoInternacional(ctoInternacional);
				}
				if (idCotacao != null) {
					Cotacao cotacao = cotacaoService.findById(idCotacao);
					dimensao.setCotacao(cotacao);
				}
				if (nrLargura != null){
					dimensao.setNrLargura(Integer.parseInt(String.valueOf(nrLargura)));
				}
				if (nrAltura != null){
					dimensao.setNrAltura(Integer.parseInt(String.valueOf(nrAltura)));
				}
				if (nrQuantidade != null){
					dimensao.setNrQuantidade(Integer.parseInt(String.valueOf(nrQuantidade)));
				}
				dimensoesCotacao.add(dimensao);
			}
			return dimensoesCotacao;
		}

		return dimensoesCotacao;
	}
	
	private TypedFlatMap parseAeroportoToMap(Filial filialAeroporto) {
		TypedFlatMap map = new TypedFlatMap();

		map.put("idAeroporto", filialAeroporto.getAeroporto().getIdAeroporto());
		map.put("nmAeroporto", filialAeroporto.getAeroporto().getSiglaDescricao());
		return map;
	}

	private TypedFlatMap parseMunicipioToMap(Municipio municipio) {
		TypedFlatMap map = new TypedFlatMap();

		map.put("idMunicipio", municipio.getIdMunicipio());
		map.put("nmMunicipio", municipio.getNmMunicipio());
		map.put("nrCep", municipio.getNrCep());

		return map;
	}

	private TypedFlatMap parseUfToMap(UnidadeFederativa uf) {
		TypedFlatMap tfm = new TypedFlatMap();

		tfm.put("idEstado", uf.getIdUnidadeFederativa());
		tfm.put("uf", uf.getSgUnidadeFederativa());
		tfm.put("nome", uf.getNmUnidadeFederativa());
		tfm.put("blIncideIss",
				BooleanUtils.toString(uf.getBlIncideIss(), TRUE, FALSE));

		return tfm;
	}

	private TypedFlatMap parseTabelaDivisaClienteToMap(
			TabelaDivisaoCliente tabelaDivisaoCliente) {
		TypedFlatMap map = new TypedFlatMap();

		map.put("idTabelaDivisaoCliente", tabelaDivisaoCliente.getIdTabelaDivisaoCliente());
		map.put("idDivisaoCliente", tabelaDivisaoCliente.getDivisaoCliente().getIdDivisaoCliente());
		map.put("nrFatorCubagem", tabelaDivisaoCliente.getNrFatorCubagem());
		map.put("idTabelaPreco", tabelaDivisaoCliente.getTabelaPreco().getIdTabelaPreco());
		return map;
	}

	private TypedFlatMap parseEnderecoPessoaToMap(EnderecoPessoa enderecoPessoa) {
		TypedFlatMap map = new TypedFlatMap();
		if(enderecoPessoa != null){
			Municipio municipio = enderecoPessoa.getMunicipio();
			UnidadeFederativa unidadeFederativa = null;
			map.put("idEndereco", enderecoPessoa.getIdEnderecoPessoa());
			map.put("cep", enderecoPessoa.getNrCep());
			if(municipio != null){
				unidadeFederativa = municipio.getUnidadeFederativa();
				map.put("idMunicipio", municipio.getIdMunicipio());
				map.put("nmMunicipio", municipio.getNmMunicipio());
			}
			if(unidadeFederativa != null){
				map.put("idEstado", unidadeFederativa.getIdUnidadeFederativa());
				map.put("uf", unidadeFederativa.getSgUnidadeFederativa());
			}
		}
		return map;
	}
	
	private TypedFlatMap parseMunicipioFilialToMap(MunicipioFilial mF) {
		TypedFlatMap map = new TypedFlatMap();

		map.put("idMunicipioFilial", mF.getIdMunicipioFilial());
		map.put("idMunicipio", mF.getMunicipio().getIdMunicipio());
		map.put("idFilial", mF.getFilial().getIdFilial());
		map.put("blPadraoMcd",BooleanUtils.toString(mF.getBlPadraoMcd(), TRUE, FALSE));
		map.put("blRecebeColetaEventual", BooleanUtils.toString(mF.getBlRecebeColetaEventual(), TRUE, FALSE));
		map.put("dtVigenciaInicial", mF.getDtVigenciaInicial().toDateTimeAtMidnight());
		if (mF.getDtVigenciaFinal() != null) {
			map.put("dtVigenciaFinal", mF.getDtVigenciaFinal().toDateTimeAtMidnight());
		} else {
			map.put("dtVigenciaFinal", null);
		}

		return map;
	}

	private TypedFlatMap parsePessoaToMap(Pessoa pessoa) {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("id", pessoa.getIdPessoa());
		tfm.put("nome", pessoa.getNmPessoa());
		tfm.put("nomeFantasia", pessoa.getNmFantasia());
		tfm.put("tpIdentificacao", pessoa.getTpIdentificacao().getValue());
		tfm.put("nrIdentificador", pessoa.getNrIdentificacao());
		tfm.put("tpPessoa", pessoa.getTpPessoa().getValue());

		return tfm;

	}

	private TypedFlatMap parseClienteToMap(Cliente c) {
		TypedFlatMap map = new TypedFlatMap();
		if (c != null) {
			map.put("id", c.getPessoa().getIdPessoa());
			if (c.getPessoa() != null && c.getPessoa().getNmPessoa() != null){
				map.put("nome", c.getPessoa().getNmPessoa());
			}
			if (c.getPessoa() != null && c.getPessoa().getNrIdentificacao() != null){
				map.put("nrIdentificacao", c.getPessoa().getNrIdentificacao());
			}
			if (c.getPessoa() != null && c.getPessoa().getTpPessoa() != null && c.getPessoa().getTpPessoa().getValue() != null){
				map.put("tpPessoa", c.getPessoa().getTpPessoa().getValue());
			}
			if (c.getTpCliente() != null){
				map.put("tpCliente", c.getTpCliente().getValue());
			}
			if (c.getBlFobDirigido() != null){
				map.put("blFobDirigido", BooleanUtils.toString(c.getBlFobDirigido(), TRUE, FALSE));
			}
			if (c.getBlAceitaFobGeral() != null){
				map.put("blAceitaFobGeral", BooleanUtils.toString(c.getBlAceitaFobGeral(), TRUE, FALSE));
			}
		}
		return map;
	}

	private TypedFlatMap parseTipoTributacaoIEToMap(
			TipoTributacaoIE tipoTributacaoIE) {
		String keyValorDominio = tipoTributacaoIE.getTpSituacaoTributaria()
				.getValue();
		String dsValorDominio = tipoTributacaoIE.getTpSituacaoTributaria()
				.getValue();
		return createMapToDominio(keyValorDominio, dsValorDominio);
	}

	private TypedFlatMap createMapToDominio(String keyValorDominio,
			String dsValorDominio) {
		TypedFlatMap map = new TypedFlatMap();
		map.put(keyValorDominio, dsValorDominio);
		return map;
	}

	/**
	 * Converte string value "S" para true ou "N" para false. Obs:
	 * IgnoreCaseSensitive.
	 * 
	 * @param value
	 *            - "S" or "N"
	 * @return true, false or null.
	 */
	public static Boolean convertStringToBoolean(String value) {
		Boolean isConvertido = null;
		if (value.trim().equalsIgnoreCase(SIM)) {
			isConvertido = true;
		} else if (value.trim().equalsIgnoreCase(NAO)) {
			isConvertido = false;
		}
		return isConvertido;
	}

	public void setDimensaoService(DimensaoService dimensaoService) {
		this.dimensaoService = dimensaoService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setUsuarioADSMService(UsuarioADSMService usuarioADSMService) {
		this.usuarioADSMService = usuarioADSMService;
	}

	public void setUsuarioService(
			com.mercurio.lms.configuracoes.model.service.UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public void setInscricaoEstadualService(
			InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public void setEnderecoPessoaService(
			EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setTipoEnderecoPessoaService(
			TipoEnderecoPessoaService tipoEnderecoPessoaService) {
		this.tipoEnderecoPessoaService = tipoEnderecoPessoaService;
	}

	public void setDivisaoClienteService(
			DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setNaturezaProdutoService(
			NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}

	public void setTabelaDivisaoClienteService(
			TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setTipoTributacaoIEService(
			TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}

	public void setTipoLocalizacaoMunicipioService(
			TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setParcelaCotacaoService(
			ParcelaCotacaoService parcelaCotacaoService) {
		this.parcelaCotacaoService = parcelaCotacaoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public void setCtoInternacionalService(
			CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	public void setProibidoEmbarqueService(
			ProibidoEmbarqueService proibidoEmbarqueService) {
		this.proibidoEmbarqueService = proibidoEmbarqueService;
	}

	public void setMunicipioFilialService(
			MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public void setLiberacaoEmbarqueService(
			LiberacaoEmbarqueService liberacaoEmbarqueService) {
		this.liberacaoEmbarqueService = liberacaoEmbarqueService;
	}

	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}

	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}

	public void setProdutoEspecificoService(
			ProdutoEspecificoService produtoEspecificoService) {
		this.produtoEspecificoService = produtoEspecificoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
}
