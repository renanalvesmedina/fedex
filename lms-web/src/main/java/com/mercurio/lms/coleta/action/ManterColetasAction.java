package com.mercurio.lms.coleta.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
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
import com.mercurio.lms.coleta.model.AwbColeta;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.NotaFiscalColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.PedidoColetaProduto;
import com.mercurio.lms.coleta.model.RestricaoColeta;
import com.mercurio.lms.coleta.model.ServicoAdicionalColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.LocalidadeEspecialService;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.coleta.model.service.RestricaoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoAdicionalService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.EmbarqueValidationService;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.municipios.model.service.RegiaoColetaEntregaFilService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.model.service.RotaIntervaloCepService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.HorarioCorteCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.CotacaoService;
import com.mercurio.lms.vendas.model.service.HorarioCorteClienteService;
import com.mercurio.lms.vendas.model.service.LiberacaoEmbarqueService;

/**
 * Generated by: ADSM ActionGenerator
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.coleta.manterColetasAction"
 */

public class ManterColetasAction extends MasterDetailAction {
	private DetalheColetaService detalheColetaService;
	private ClienteService clienteService;
	private EnderecoPessoaService enderecoPessoaService;	
	private ServicoAdicionalService servicoAdicionalService;
	private RotaIntervaloCepService rotaIntervaloCepService;
	private MoedaService moedaService;
	private PpeService ppeService;
	private ParametroGeralService parametroGeralService;
	private FilialService filialService;
	private ServicoService servicoService;
	private NaturezaProdutoService naturezaProdutoService;
	private LocalidadeEspecialService localidadeEspecialService;
	private MunicipioService municipioService;
	private UnidadeFederativaService unidadeFederativaService;
	private AwbService awbService;
	private UsuarioService usuarioService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private MunicipioFilialService municipioFilialService;
	private RestricaoColetaService restricaoColetaService;
	private RegiaoColetaEntregaFilService regiaoColetaEntregaFilService;
	private OcorrenciaColetaService ocorrenciaColetaService;
	private HorarioCorteClienteService horarioCorteClienteService;
	private ConversaoMoedaService conversaoMoedaService;
	private CotacaoService cotacaoService;
	private CtoInternacionalService ctoInternacionalService;
	private ConfiguracoesFacade configuracoesFacade;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private LiberacaoEmbarqueService liberacaoEmbarqueService;
	private ConhecimentoService conhecimentoService;
	private EmbarqueValidationService embarqueValidationService;
	
	public ResultSetPage findPaginated(Map criteria) {		
		return this.getPedidoColetaService().findPaginated(criteria);
	}
	
	public Integer getRowCount(Map criteria) {
		return this.getPedidoColetaService().getRowCount(criteria);
	}		
	
	public List findLookupRotaColetaEntrega(Map criteria) {
		return rotaColetaEntregaService.findLookup(criteria);
	}
	
    public List findLookupUsuarioFuncionario(TypedFlatMap tfm){
    	String nrMatricula = tfm.getString("nrMatricula");
    	if (!StringUtils.isBlank(nrMatricula)){
    		nrMatricula = StringUtils.leftPad(nrMatricula, 9, '0');
	}
    	return usuarioService.findLookupUsuarioFuncionario(tfm.getLong("idUsuario"), nrMatricula, null, null, null, null, true);
    }


	public List findLookupRegiaoColetaEntregaFil(Map criteria) {
		return regiaoColetaEntregaFilService.findLookup(criteria);
	}
	
	/**
	 * Recebe o endereco em forma de TypedFlatMap e
	 * formata para mostrar no TextArea
	 * @param tfm
	 * @return
	 */
	public TypedFlatMap formataEndereco(TypedFlatMap tfm) {

		String complemento = configuracoesFacade.getMensagem("complemento");
		String bairro = configuracoesFacade.getMensagem("bairro");
		String cep = configuracoesFacade.getMensagem("cep");

		String dsTipoLogradouro = tfm.getString("dsTipoLogradouro");
		String dsComplemento = tfm.getString("dsComplemento");
		String dsEndereco = tfm.getString("dsEndereco");
		String nrEndereco = tfm.getString("nrEndereco");
		String dsBairro = tfm.getString("dsBairro");
		String nrCep = tfm.getString("nrCep");

		StringBuffer endereco = new StringBuffer()
		.append(dsTipoLogradouro==null?"":dsTipoLogradouro+" ")
		.append(dsEndereco)
		.append(", n�: ")
		.append(nrEndereco)
		.append(dsComplemento==null?"":" / "+complemento+": "+dsComplemento)
		.append(dsBairro==null?"":"\n"+bairro+": "+dsBairro)
		.append("\n"+cep+": "+nrCep);

		TypedFlatMap map = new TypedFlatMap();
		map.put("endereco", endereco.toString());
		return map;
	}
	
	
	public ResultSetPage findPaginatedRestricaoColeta(TypedFlatMap criteria) {
		ResultSetPage rsp = restricaoColetaService.findPaginated(criteria);
		List result = rsp.getList();
		List retorno = new ArrayList();
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			TypedFlatMap tfm = new TypedFlatMap();
			RestricaoColeta restricaoColeta = (RestricaoColeta) iter.next();
			tfm.put("idRestricaoColeta", restricaoColeta.getIdRestricaoColeta());
			if (restricaoColeta.getServico()!=null){
				tfm.put("servico.idServico", restricaoColeta.getServico().getIdServico());
				tfm.put("servico.dsServico", restricaoColeta.getServico().getDsServico());
			}
			if (restricaoColeta.getPais()!=null){
				tfm.put("pais.nmPais", restricaoColeta.getPais().getNmPais());
			}
			if (restricaoColeta.getPsMaximoVolume()!=null){
				tfm.put("psMaximoVolume", restricaoColeta.getPsMaximoVolume());
			}
			if (restricaoColeta.getProdutoProibido()!=null){
				tfm.put("produtoProibido.dsProduto", restricaoColeta.getProdutoProibido().getDsProduto());
			}
			retorno.add(tfm);
		}
		rsp.setList(retorno);
		return rsp;
	}

	public Integer getRowCountRestricaoColeta(TypedFlatMap criteria) {
		return restricaoColetaService.getRowCount(criteria);
	}
	
	public List findLookupCliente(Map criteria) {		
		return clienteService.findLookup(criteria);
	}
	
	public Cliente findClienteByIdCliente(Map criteria) {
		String strIdCliente = criteria.get("idCliente").toString();
		return clienteService.findById(Long.valueOf(strIdCliente));
	}		
	
	public List findLookupEnderecoPessoa(Map criteria) {		
		return enderecoPessoaService.findLookup(criteria);
	}	
	
	public List findModoPedidoColeta(TypedFlatMap criteria) {
		List dominiosValidos = new ArrayList();
		dominiosValidos.add("TE");
		dominiosValidos.add("BA");
		List retorno = this.getDomainValueService().findByDomainNameAndValues("DM_MODO_PEDIDO_COLETA", dominiosValidos);
		return retorno;
	}
	
	public List findServicoAdicional(Map criteria) {
		List retorno = new ArrayList();
		List listServicosAdicionais = servicoAdicionalService.findServicosAdicionaisAtivos(criteria);
		for (Iterator iter = listServicosAdicionais.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			ServicoAdicional servicoAdicional = (ServicoAdicional) iter.next();
			map.put("idServicoAdicional", servicoAdicional.getIdServicoAdicional());
			map.put("dsServicoAdicional", servicoAdicional.getDsServicoAdicional());
			retorno.add(map);
		}
		return retorno;
	}
		
	public List findMoeda(Map criteria) {
		List retorno = new ArrayList();
		List listMoedas = moedaService.find(criteria);
		for (Iterator iter = listMoedas.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			Moeda moeda = (Moeda) iter.next();
			map.put("idMoeda", moeda.getIdMoeda());
			map.put("siglaSimbolo", moeda.getSiglaSimbolo());
			retorno.add(map);
		}
		return retorno;
	}
	
	public List findServico(Map criteria) {
		List retorno = new ArrayList();
		List listServicos = servicoService.find(criteria);
		for (Iterator iter = listServicos.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			Servico servico = (Servico) iter.next();
			map.put("idServico", servico.getIdServico());
			map.put("dsServico", servico.getDsServico());
			retorno.add(map);
		}
		return retorno;
	} 
	
	public List findNaturezaProduto(Map criteria) {		
		List retorno = new ArrayList();
		List listNaturezasProduto = naturezaProdutoService.find(criteria);
		for (Iterator iter = listNaturezasProduto.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			NaturezaProduto naturezaProduto = (NaturezaProduto) iter.next();
			map.put("idNaturezaProduto", naturezaProduto.getIdNaturezaProduto());
			map.put("dsNaturezaProduto", naturezaProduto.getDsNaturezaProduto());
			retorno.add(map);
		}
		return retorno;
	}
	
	public List findLookupLocalidadeEspecial(Map criteria) {
		return localidadeEspecialService.findLookup(criteria);
	}
	
	public List findLookupMunicipio(Map criteria) {
		FilterList filter = new FilterList(municipioFilialService.findLookup(criteria)) {
			public Map filterItem(Object item) {
				MunicipioFilial mf = (MunicipioFilial)item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idMunicipioFilial",mf.getIdMunicipioFilial());
				typedFlatMap.put("municipio.idMunicipio",mf.getMunicipio().getIdMunicipio());
				typedFlatMap.put("municipio.nmMunicipio",mf.getMunicipio().getNmMunicipio());
				typedFlatMap.put("municipio.unidadeFederativa.idUnidadeFederativa",mf.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
				typedFlatMap.put("municipio.unidadeFederativa.sgUnidadeFederativa",mf.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
				typedFlatMap.put("filial.idFilial",mf.getFilial().getIdFilial());
				typedFlatMap.put("filial.sgFilial",mf.getFilial().getSgFilial());
				typedFlatMap.put("filial.pessoa.nmFantasia",mf.getFilial().getPessoa().getNmFantasia());
				return typedFlatMap;
			}
		};
		return (List)filter.doFilter();
	}
	
	/**
	 * M�todo que valida a vigencia do municipio selecionado.
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap validaVigenciaAtendimento(TypedFlatMap criteria) {
		try {
			municipioFilialService.validateVigenciaAtendimento(criteria.getLong("idMunicipioFilial"), 
																		 JTDateTimeUtils.getDataAtual(),
																		 JTDateTimeUtils.getDataAtual());
		} catch (BusinessException e) {			
			throw new BusinessException("LMS-29022");
		}
		
		return criteria;
	}
	
	public List findLookupUnidadeFederativa(Map criteria) {
		return unidadeFederativaService.findLookup(criteria);
	}
		
	public List findLookupAwb(Map criteria) {
		TypedFlatMap mapResult = new TypedFlatMap();
		List listResult = new ArrayList();
		List awbList = awbService.findLookup(criteria);
		if (awbList != null && !awbList.isEmpty()) {
			for (int i = 0; i < awbList.size(); i++) {
				Awb awb = (Awb) awbList.get(i);
				String nrAwbFormatado = AwbUtils.formatNrAwb(awb.getNrAwb(), awb.getDvAwb());
				mapResult.put("idAwb", awb.getIdAwb());
				mapResult.put("nrAwb", awb.getNrAwb());
				mapResult.put("nrAwbFormatado", nrAwbFormatado);
				
				listResult.add(mapResult);
			}
		}		
		
		return listResult;
	}

	
	/**
	 * Retorna o EnderecoPessoa referente ao ID do Cliente
	 */
	public Map getEnderecoPessoa(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();		
		Long idCliente = criteria.getLong("idCliente");

		List listEnderecoPessoa = enderecoPessoaService.findEnderecoPessoaByIdPessoaByTipoEnderecoPessoa(idCliente, "COL", JTDateTimeUtils.getDataAtual());		

		if(listEnderecoPessoa.size() == 1) {			
			EnderecoPessoa enderecoPessoa = (EnderecoPessoa) listEnderecoPessoa.get(0);

			tfm.put("idEnderecoPessoa", enderecoPessoa.getIdEnderecoPessoa());
			tfm.put("dsEndereco", enderecoPessoa.getDsEndereco());
			tfm.put("dsTipoLogradouro", enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro());
			tfm.put("nrEndereco", enderecoPessoa.getNrEndereco());
			tfm.put("dsComplemento", enderecoPessoa.getDsComplemento());
			tfm.put("dsBairro", enderecoPessoa.getDsBairro());
			tfm.put("nrCep", enderecoPessoa.getNrCep());
			tfm.put("municipio.idMunicipio", enderecoPessoa.getMunicipio().getIdMunicipio());
			tfm.put("municipio.nmMunicipio", enderecoPessoa.getMunicipio().getNmMunicipio());
			tfm.put("municipio.unidadeFederativa.sgUnidadeFederativa", enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			
			return tfm;			
		} else if(listEnderecoPessoa.size() == 0) {			
			EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idCliente);
			
			tfm.put("idEnderecoPessoa", enderecoPessoa.getIdEnderecoPessoa());
			tfm.put("dsEndereco", enderecoPessoa.getDsEndereco());
			tfm.put("dsTipoLogradouro", enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro());
			tfm.put("nrEndereco", enderecoPessoa.getNrEndereco());
			tfm.put("dsComplemento", enderecoPessoa.getDsComplemento());
			tfm.put("dsBairro", enderecoPessoa.getDsBairro());
			tfm.put("nrCep", enderecoPessoa.getNrCep());
			tfm.put("municipio.idMunicipio", enderecoPessoa.getMunicipio().getIdMunicipio());
			tfm.put("municipio.nmMunicipio", enderecoPessoa.getMunicipio().getNmMunicipio());
			tfm.put("municipio.unidadeFederativa.sgUnidadeFederativa", enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			
			return tfm;
		} else {	
			return null;
		}
	}
	
	/**
	 * M�todo que retorna dados complementares para coleta.
	 */
	public TypedFlatMap getDadosComplementares(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		Long idEnderecoPessoa = criteria.getLong("idEnderecoPessoa");
		Long idCliente = criteria.getLong("idCliente");
		String nrCep = criteria.getString("nrCep");
		DateTime dhColetaDisponivel = criteria.getDateTime("dhColetaDisponivel");
		
		// Busca o horario de corte do cliente
		HorarioCorteCliente horarioCorteCliente = horarioCorteClienteService.findHorarioCorteClienteByIdEnderecoPessoaByHoraAtual(idEnderecoPessoa, JTDateTimeUtils.getHorarioAtual());
		if (horarioCorteCliente != null) {
			map.put("hrCorteCliente", horarioCorteCliente.getHrFinal());			
		}

		// Busca dados da filial respons�vel.
		Cliente cliente = clienteService.findById(idCliente);
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findById(idEnderecoPessoa);		
		Filial filialAtendeOperacional = cliente.getFilialByIdFilialAtendeOperacional();

		if (filialAtendeOperacional.getIdFilial() != null){
			map.put("idFilial", filialAtendeOperacional.getIdFilial());
			map.put("sgFilial", filialAtendeOperacional.getSgFilial());
			map.put("pessoa.nmFantasia", filialAtendeOperacional.getPessoa().getNmFantasia());	
		} else {
			String strServicoDefaultColeta = (String)parametroGeralService.findConteudoByNomeParametro("ID_SERVICO_DEFAULT_COLETA", false);
			Long servicoDefaultColeta = Long.valueOf(strServicoDefaultColeta);
			Long idFilial = ppeService.findFilialColetaMunicipio(enderecoPessoa.getMunicipio().getIdMunicipio(), servicoDefaultColeta, enderecoPessoa.getNrCep());
			Filial filial = filialService.findById(idFilial);
			map.put("idFilial", filial.getIdFilial());
			map.put("sgFilial", filial.getSgFilial());
			map.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		}		

		// Busca dados da rota pelo intervalo de cep.		
		RotaIntervaloCep rotaIntervaloCep = rotaColetaEntregaService.findRotaAtendimentoCep(
				nrCep, 
				idCliente, 
				idEnderecoPessoa,
				map.getLong("idFilial"), //LMS-1321
				JTDateTimeUtils.getDataAtual());
		if(rotaIntervaloCep != null) {
			RotaColetaEntrega rotaColetaEntrega = rotaIntervaloCep.getRotaColetaEntrega();		
			map.put("idRotaIntervaloCep", rotaIntervaloCep.getIdRotaIntervaloCep());
			map.put("horaCorteSolicitacao", rotaIntervaloCep.getHrCorteSolicitacao());
			map.put("idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega());
			map.put("numeroRota", rotaColetaEntrega.getNrRota());
			map.put("descricaoRota", rotaColetaEntrega.getDsRota());

			if(dhColetaDisponivel.getHourOfDay() > rotaIntervaloCep.getHrCorteSolicitacao().getHourOfDay()) {			
				dhColetaDisponivel = dhColetaDisponivel.plusDays(1);
			} else if(dhColetaDisponivel.getHourOfDay() == rotaIntervaloCep.getHrCorteSolicitacao().getHourOfDay()) {
				if(dhColetaDisponivel.getMinuteOfHour() > rotaIntervaloCep.getHrCorteSolicitacao().getMinuteOfHour()) {
					dhColetaDisponivel = dhColetaDisponivel.plusDays(1);
				}
			}		
			map.put("dhColetaDisponivel", dhColetaDisponivel);		
		}

		return map;
	}
	
	/**
	 * Pega os dados da sess�o. 
	 */
	public TypedFlatMap getDadosSessao() {
		TypedFlatMap map = new TypedFlatMap();
		map.put("siglaSimboloMoedaSessao", SessionUtils.getMoedaSessao().getSiglaSimbolo());
		map.put("dataAtual", JTDateTimeUtils.getDataAtual());
		
		return map;
	}
	
	/**
	 * Verifica se a hora de dhColetaDisponivel � menor que horario de corte.
	 * caso verdadeiro, somar mais um a data de dhColetaDisponivel e retornar
	 * para ser atribuido a data de previs�o de coleta.
	 */
	public DateTime getDataPrevisaoColeta(TypedFlatMap criteria) {		
		DateTime dhColetaDisponivel = criteria.getDateTime("dhColetaDisponivel");
		TimeOfDay horarioCorte = criteria.getTimeOfDay("horarioCorte");
		
		if(dhColetaDisponivel.getHourOfDay() > horarioCorte.getHourOfDay()) {			
			dhColetaDisponivel = dhColetaDisponivel.plusDays(1);
		} else if(dhColetaDisponivel.getHourOfDay() == horarioCorte.getHourOfDay()
			 && dhColetaDisponivel.getMinuteOfHour() > horarioCorte.getMinuteOfHour()) {
				dhColetaDisponivel = dhColetaDisponivel.plusDays(1);
		}
		
		return dhColetaDisponivel;
	}	
	
	
	/**
	 * Retorna o Contato do ultimo Pedido Coleta para o cliente em quest�o
	 */		
	public String getContatoPedidoColeta(Map criteria) {
		String strIdCliente = criteria.get("idCliente").toString();
		
		List listPedidoColeta = this.getPedidoColetaService().findPedidoColetaByIdCliente(Long.valueOf(strIdCliente));
		for (int i=0; i<listPedidoColeta.size(); i++) {
			PedidoColeta pedidoColeta = (PedidoColeta) listPedidoColeta.get(i);			
			if (pedidoColeta.getIdPedidoColeta() != null) {
				return pedidoColeta.getNmContatoCliente();
			}			
		}		
		return "";		
	}

	/**
	 * Retorna a coluna BL_RECEBE_COLETAS_EVENTUAIS da tabela MUNICIPIO_FILIAL
	 */
	public Map getMunicipioDestinoBloqueado(TypedFlatMap criteria) {
		Long idMunicipio = criteria.getLong("idMunicipio");	
		Long idServico = criteria.getLong("idServico");
		Long idCliente = criteria.getLong("idCliente");
		
		Servico servico = servicoService.findById(idServico);
		Cliente clienteResponsavel = clienteService.findById(idCliente);
		Map map = new HashMap();
		
		boolean hasToValidateEmbarqueProibido = embarqueValidationService.hasToValidateEmbarqueProibido(
								servico, 
								clienteResponsavel, 
								new DomainValue(criteria.getString("tpFrete")));
						
		map.put("hasToValidateEmbarqueProibido", hasToValidateEmbarqueProibido);				
		map.putAll(buildColetaEventualMap(servico, idMunicipio, idCliente));
		
		validateBloqCredEmbProib(map);
		return map;
	}
	private void validateBloqCredEmbProib(Map map) {
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "bloqCredEmbProib", false, true);

		 if (conteudoParametroFilial != null && "S".equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			 map.put("bloqCredEmbProib", true);
		 }
	}
	private Map buildColetaEventualMap(Servico servico, Long idMunicipio, Long idCliente) {
		Map<String, Object> map = new HashMap<String, Object>();
		List atendimentos = municipioFilialService.findAtendimentosVigentesByMunicipioServicoOperacao(idMunicipio, servico.getIdServico(), "E");
	
		if(!atendimentos.isEmpty()) {
			Object[] atendimento = (Object[]) atendimentos.get(0);
			
			Long idMunicipioFilial = (Long) atendimento[0];
			MunicipioFilial municipioFilial = municipioFilialService.findById(idMunicipioFilial);

			Long idFilial = (Long) atendimento[1];
			map.putAll(buildFilialMap(idFilial));			
			
			Municipio municipio = municipioService.findById(idMunicipio);
			map.put("sgUnidadeFederativa", municipio.getUnidadeFederativa().getSgUnidadeFederativa());
			
			boolean blRecebeColetaEventual = getBlRecebeColetaEventual(servico, idMunicipio, servico.getIdServico(), idCliente, municipioFilial);
			map.put("recebeColetaEventual", blRecebeColetaEventual);
		}
		return map;
	}
	private boolean getBlRecebeColetaEventual(Servico servico, Long idMunicipio,
			Long idServico, Long idCliente, MunicipioFilial municipioFilial) {
		boolean blRecebeColetaEventual = municipioFilial.getBlRecebeColetaEventual();
		
		if(!blRecebeColetaEventual && idServico != null && idCliente != null){
			List le = liberacaoEmbarqueService.findLiberacaoCliente(idCliente, idMunicipio, servico.getTpModal());
			blRecebeColetaEventual = !le.isEmpty();
		}
		return blRecebeColetaEventual;
	}
	
	private Map<String, Object> buildFilialMap(Long idFilial) {
		Map<String, Object> map = new HashMap<String, Object>();
		Filial filial = filialService.findById(idFilial);
		
		map.put("filialId", idFilial);
		map.put("filialSigla", filial.getSgFilial());
		map.put("filialNome", filial.getPessoa().getNmFantasia());
		
		return map;
	}

	/**
	 * #######################################
	 * # Inicio dos m�todos para tela de DF2 #
	 * #######################################
	 */

	public TypedFlatMap store(TypedFlatMap tfmBean) {
		// Salva Pedido de Coleta, os itens de Detalhe Coleta e os Servi�os Adicionais de Coleta em DF2
		MasterEntry entry = getMasterFromSession(tfmBean.getLong("idPedidoColeta"), true);		
		PedidoColeta pedidoColeta = (PedidoColeta) entry.getMaster();
		
		if (tfmBean.getDomainValue("tpPedidoColeta").getValue().equals("DE")) {
			// Pega os filhos da sess�o
			ItemList items = getItemsFromSession(entry, "detalheColeta");		
			if (items.isInitialized() == false) {
				items.initialize(detalheColetaService.findDetalheColeta(pedidoColeta.getIdPedidoColeta()));			
			}
			ItemListConfig config = getMasterConfig().getItemListConfig("detalheColeta");

			for(Iterator iter = items.iterator(pedidoColeta.getIdPedidoColeta(), config); iter.hasNext();) {					
				DetalheColeta detalheColeta = (DetalheColeta) iter.next();				
				if(detalheColeta.getCliente() == null && detalheColeta.getNmDestinatario() == null) {
					throw new BusinessException("requiredField", new Object[]{configuracoesFacade.getMensagem("destinatario")});
				}
			}
		}

		pedidoColeta.setIdPedidoColeta(tfmBean.getLong("idPedidoColeta"));
		pedidoColeta.setNrColeta(tfmBean.getLong("nrColeta"));		
		pedidoColeta.setTpStatusColeta(tfmBean.getDomainValue("tpStatusColeta"));
		pedidoColeta.setBlClienteLiberadoManual(tfmBean.getBoolean("blClienteLiberadoManual"));
		pedidoColeta.setBlProdutoDiferenciado(tfmBean.getBoolean("blProdutoDiferenciado"));
		
		String statusColeta = tfmBean.getDomainValue("tpStatusColeta").getValue();
		if(statusColeta.equals("MA") || statusColeta.equals("TR")) {
			pedidoColeta.setBlAlteradoPosProgramacao(Boolean.TRUE);
		} else {
			pedidoColeta.setBlAlteradoPosProgramacao(tfmBean.getBoolean("blAlteradoPosProgramacao"));
		}		

		YearMonthDay dataPrevisaoColeta = tfmBean.getYearMonthDay("dtPrevisaoColeta");
		
		if(JTDateTimeUtils.comparaData(dataPrevisaoColeta, JTDateTimeUtils.getDataAtual()) < 0){
			throw new BusinessException("LMS-02111");
		}
		pedidoColeta.setDtPrevisaoColeta(dataPrevisaoColeta);
		
		pedidoColeta.setTpModoPedidoColeta(tfmBean.getDomainValue("tpModoPedidoColeta"));
		pedidoColeta.setNmContatoCliente(tfmBean.getString("nmContatoCliente"));
		pedidoColeta.setNmSolicitante(tfmBean.getString("nmSolicitante"));
		pedidoColeta.setNrDddCliente(tfmBean.getString("nrDddCliente"));		
		pedidoColeta.setNrTelefoneCliente(tfmBean.getString("nrTelefoneCliente"));
		pedidoColeta.setTpPedidoColeta(tfmBean.getDomainValue("tpPedidoColeta"));
		pedidoColeta.setDhColetaDisponivel(tfmBean.getDateTime("dhColetaDisponivel"));
		pedidoColeta.setHrLimiteColeta(tfmBean.getTimeOfDay("hrLimiteColeta"));
		
		pedidoColeta.setEdColeta(tfmBean.getString("edColeta"));
		
		if(tfmBean.getString("nrEndereco") != null){
			pedidoColeta.setNrEndereco(tfmBean.getString("nrEndereco").trim());
		}
		
		pedidoColeta.setDsComplementoEndereco(tfmBean.getString("dsComplementoEndereco"));
		pedidoColeta.setDsBairro(tfmBean.getString("dsBairro"));
		pedidoColeta.setNrCep(tfmBean.getString("nrCep"));		
		pedidoColeta.setVlTotalInformado(tfmBean.getBigDecimal("vlTotalInformado"));
		pedidoColeta.setVlTotalVerificado(tfmBean.getBigDecimal("vlTotalVerificado"));
		pedidoColeta.setQtTotalVolumesInformado(tfmBean.getInteger("qtTotalVolumesInformado"));
		pedidoColeta.setQtTotalVolumesVerificado(tfmBean.getInteger("qtTotalVolumesVerificado"));
		pedidoColeta.setPsTotalInformado(tfmBean.getBigDecimal("psTotalInformado"));
		pedidoColeta.setPsTotalVerificado(tfmBean.getBigDecimal("psTotalVerificado"));
		pedidoColeta.setPsTotalAforadoInformado(tfmBean.getBigDecimal("psTotalAforadoInformado"));
		pedidoColeta.setPsTotalAforadoVerificado(tfmBean.getBigDecimal("psTotalAforadoVerificado"));
		pedidoColeta.setObPedidoColeta(tfmBean.getString("obPedidoColeta"));
		pedidoColeta.setDsInfColeta(tfmBean.getString("dsInfColeta"));

		pedidoColeta.setCliente(clienteService.findById(tfmBean.getLong("cliente.idCliente")));
		pedidoColeta.setEnderecoPessoa(enderecoPessoaService.findById(tfmBean.getLong("enderecoPessoa.idEnderecoPessoa")));
		pedidoColeta.setMunicipio(municipioService.findById(tfmBean.getLong("municipio.idMunicipio")));
		pedidoColeta.setUsuario(usuarioService.findById(tfmBean.getLong("usuario.idUsuario")));
		pedidoColeta.setFilialByIdFilialSolicitante(filialService.findById(tfmBean.getLong("filialByIdFilialSolicitante.idFilial")));
		pedidoColeta.setFilialByIdFilialResponsavel(filialService.findById(tfmBean.getLong("filialByIdFilialResponsavel.idFilial")));
		pedidoColeta.setMoeda(moedaService.findById(tfmBean.getLong("moeda.idMoeda")));

		if (tfmBean.getLong("cotacao.idCotacao") != null) {
			pedidoColeta.setCotacao(cotacaoService.findById(tfmBean.getLong("cotacao.idCotacao")));
		}

		if (tfmBean.getLong("rotaIntervaloCep.idRotaIntervaloCep") != null) {
			pedidoColeta.setRotaIntervaloCep(rotaIntervaloCepService.findById(tfmBean.getLong("rotaIntervaloCep.idRotaIntervaloCep")));
		} else {
	    if (!"BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())){
			throw new BusinessException("LMS-02011");
		}
	}

		if (tfmBean.getLong("rotaColetaEntrega.idRotaColetaEntrega") != null) {
				pedidoColeta.setRotaColetaEntrega(rotaColetaEntregaService.findById(tfmBean.getLong("rotaColetaEntrega.idRotaColetaEntrega")));
		} else {
	    if (!"BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())){
			throw new BusinessException("LMS-02011");
		}			
	}			

		// Pega os filhos da sess�o
		ItemList items = getItemsFromSession(entry, "detalheColeta");		
		if (items.isInitialized() == false) {
			items.initialize(detalheColetaService.findDetalheColeta(pedidoColeta.getIdPedidoColeta()));			
		}		
		ItemListConfig config = getMasterConfig().getItemListConfig("detalheColeta");

		// Salva o registro pai e seus filhos.
		TypedFlatMap registrosSalvos = this.getPedidoColetaService().storeAll(pedidoColeta, tfmBean, items, config);
		items.resetItemsState();
		updateMasterInSession(entry);

		return registrosSalvos;
	}	

	/**
	 * Salva a referencia do objeto Master na sess�o.
	 * N�o devem ser inicializadas as cole��es que representam os filhos
	 * j� que o usu�rio pode vir a n�o utilizar a aba de filhos, evitando assim
	 * a carga desnecess�ria de objetos na sess�o e a partir do banco de dados.
	 */
	public Object findById(java.lang.Long id) {
		Long idCliente = null;
		Long idEnderecoPessoa = null;
		Object masterObj = getPedidoColetaService().findDetalhamentoByIdPedidoColeta(id);
		putMasterInSession(masterObj);		
		PedidoColeta pedidoColeta = (PedidoColeta) masterObj;
		
		TypedFlatMap mapPedidoColeta = new TypedFlatMap();
		
		mapPedidoColeta.put("idPedidoColeta", pedidoColeta.getIdPedidoColeta());
		mapPedidoColeta.put("nrColeta", pedidoColeta.getNrColeta());
		mapPedidoColeta.put("nrDddCliente", pedidoColeta.getNrDddCliente());
		mapPedidoColeta.put("nrTelefoneCliente", pedidoColeta.getNrTelefoneCliente());
		mapPedidoColeta.put("nrCep", pedidoColeta.getNrCep());
		mapPedidoColeta.put("dhPedidoColeta", pedidoColeta.getDhPedidoColeta());
		mapPedidoColeta.put("dhColetaDisponivel", pedidoColeta.getDhColetaDisponivel());
		mapPedidoColeta.put("dtPrevisaoColeta", pedidoColeta.getDtPrevisaoColeta());
		mapPedidoColeta.put("hrLimiteColeta", pedidoColeta.getHrLimiteColeta());
		mapPedidoColeta.put("tpModoPedidoColeta.description", pedidoColeta.getTpModoPedidoColeta().getDescription());
		mapPedidoColeta.put("tpModoPedidoColeta.value", pedidoColeta.getTpModoPedidoColeta().getValue());
		mapPedidoColeta.put("tpModoPedidoColeta.status", pedidoColeta.getTpModoPedidoColeta().getStatus());
		mapPedidoColeta.put("tpPedidoColeta.description", pedidoColeta.getTpPedidoColeta().getDescription());
		mapPedidoColeta.put("tpPedidoColeta.value", pedidoColeta.getTpPedidoColeta().getValue());
		mapPedidoColeta.put("tpPedidoColeta.status", pedidoColeta.getTpPedidoColeta().getStatus());
		mapPedidoColeta.put("tpStatusColeta.description", pedidoColeta.getTpStatusColeta().getDescription());
		mapPedidoColeta.put("tpStatusColeta.value", pedidoColeta.getTpStatusColeta().getValue());
		mapPedidoColeta.put("tpStatusColeta.status", pedidoColeta.getTpStatusColeta().getStatus());
		mapPedidoColeta.put("edColeta", /*pedidoColeta.getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro() + " " +*/ pedidoColeta.getEdColeta());
		mapPedidoColeta.put("dsBairro", pedidoColeta.getDsBairro());
		mapPedidoColeta.put("nmSolicitante", pedidoColeta.getNmSolicitante());
		mapPedidoColeta.put("nmContatoCliente", pedidoColeta.getNmContatoCliente());
		mapPedidoColeta.put("vlTotalInformado", pedidoColeta.getVlTotalInformado());		
		mapPedidoColeta.put("qtTotalVolumesInformado", pedidoColeta.getQtTotalVolumesInformado());		
		mapPedidoColeta.put("psTotalInformado", pedidoColeta.getPsTotalInformado());		
		mapPedidoColeta.put("psTotalAforadoInformado", pedidoColeta.getPsTotalAforadoInformado());		
		mapPedidoColeta.put("blClienteLiberadoManual", pedidoColeta.getBlClienteLiberadoManual());
		mapPedidoColeta.put("blAlteradoPosProgramacao", pedidoColeta.getBlAlteradoPosProgramacao());
		mapPedidoColeta.put("nrEndereco", pedidoColeta.getNrEndereco());
		mapPedidoColeta.put("dsComplementoEndereco", pedidoColeta.getDsComplementoEndereco());	
		mapPedidoColeta.put("obPedidoColeta", pedidoColeta.getObPedidoColeta());
		mapPedidoColeta.put("obPedidoColeta", pedidoColeta.getObPedidoColeta());
		mapPedidoColeta.put("blProdutoDiferenciado", pedidoColeta.getBlProdutoDiferenciado());
		mapPedidoColeta.put("dsInfColeta", pedidoColeta.getDsInfColeta());
		if (pedidoColeta.getSituacaoAprovacao() != null) {
			mapPedidoColeta.put("situacaoAprovacao.value", pedidoColeta.getSituacaoAprovacao().getValue());
			mapPedidoColeta.put("situacaoAprovacao.status", pedidoColeta.getSituacaoAprovacao().getStatus());
		}
		
		List listProdutoDiferenciado = new ArrayList();
		if (pedidoColeta.getBlProdutoDiferenciado()) {
			for (int i=0; i<pedidoColeta.getPedidoColetaProdutos().size(); i++) {
				PedidoColetaProduto pedidoColetaProduto = (PedidoColetaProduto) pedidoColeta.getPedidoColetaProdutos().get(i);
				TypedFlatMap mapPedidoColetaProduto = new TypedFlatMap();
				
				mapPedidoColetaProduto.put("idPedidoColetaProduto", pedidoColetaProduto.getIdPedidoColetaProduto());
				mapPedidoColetaProduto.put("idProduto", pedidoColetaProduto.getProduto().getIdProduto());
				mapPedidoColetaProduto.put("dsProduto", pedidoColetaProduto.getProduto().getDsProduto());
	
				listProdutoDiferenciado.add(mapPedidoColetaProduto);
			}
		}
		mapPedidoColeta.put("produtoDiferenciado", listProdutoDiferenciado);
		
		
		
		BigDecimal vlTotalVerificado = BigDecimalUtils.ZERO;
		Integer qtTotalVolumesVerificado = Integer.valueOf(0);
		BigDecimal psTotalVerificado = BigDecimalUtils.ZERO;
		BigDecimal psTotalAforadoVerificado = BigDecimalUtils.ZERO;		
		for (Iterator iter = pedidoColeta.getDetalheColetas().iterator(); iter.hasNext();) {
			DetalheColeta detalheColeta = (DetalheColeta) iter.next();
			BigDecimal valorMercadoriaConvertido = conversaoMoedaService.findConversaoMoeda(
													 SessionUtils.getPaisSessao().getIdPais(),
													 detalheColeta.getMoeda().getIdMoeda(),
													 SessionUtils.getPaisSessao().getIdPais(),
													 SessionUtils.getMoedaSessao().getIdMoeda(),
													 JTDateTimeUtils.getDataAtual(),
													 detalheColeta.getVlMercadoria());

			vlTotalVerificado = vlTotalVerificado.add(valorMercadoriaConvertido);
			psTotalVerificado = psTotalVerificado.add(detalheColeta.getPsMercadoria());
			psTotalAforadoVerificado = psTotalAforadoVerificado.add(detalheColeta.getPsAforado());
			qtTotalVolumesVerificado = Integer.valueOf(qtTotalVolumesVerificado.intValue() + detalheColeta.getQtVolumes().intValue());			
		}
		mapPedidoColeta.put("vlTotalVerificado", vlTotalVerificado);
		mapPedidoColeta.put("qtTotalVolumesVerificado", qtTotalVolumesVerificado);
		mapPedidoColeta.put("psTotalVerificado", psTotalVerificado);
		mapPedidoColeta.put("psTotalAforadoVerificado", psTotalAforadoVerificado);
		
		
		if (pedidoColeta.getUsuario() != null) {
			mapPedidoColeta.put("usuario.idUsuario", pedidoColeta.getUsuario().getIdUsuario());
		}
		if (pedidoColeta.getFilialByIdFilialSolicitante() != null) {
			mapPedidoColeta.put("filialByIdFilialSolicitante.idFilial", pedidoColeta.getFilialByIdFilialSolicitante().getIdFilial());	
		}
		if (pedidoColeta.getCliente() != null) {
			idCliente = pedidoColeta.getCliente().getIdCliente();
			mapPedidoColeta.put("cliente.idCliente", pedidoColeta.getCliente().getIdCliente());
			mapPedidoColeta.put("cliente.pessoa.idPessoa", pedidoColeta.getCliente().getPessoa().getIdPessoa());
			mapPedidoColeta.put("cliente.pessoa.tpIdentificacao", pedidoColeta.getCliente().getPessoa().getTpIdentificacao().getValue());
			mapPedidoColeta.put("cliente.pessoa.nrIdentificacao", pedidoColeta.getCliente().getPessoa().getNrIdentificacao());			
			mapPedidoColeta.put("cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(pedidoColeta.getCliente().getPessoa().getTpIdentificacao(), pedidoColeta.getCliente().getPessoa().getNrIdentificacao()));
			mapPedidoColeta.put("cliente.pessoa.nmPessoa", pedidoColeta.getCliente().getPessoa().getNmPessoa());
			mapPedidoColeta.put("cliente.tpCliente", pedidoColeta.getCliente().getTpCliente().getValue());	
		}
		if (pedidoColeta.getEnderecoPessoa() != null) {
			idEnderecoPessoa = pedidoColeta.getEnderecoPessoa().getIdEnderecoPessoa();
			mapPedidoColeta.put("enderecoPessoa.idEnderecoPessoa", pedidoColeta.getEnderecoPessoa().getIdEnderecoPessoa());
		}		
		if (pedidoColeta.getMoeda() != null) {
			mapPedidoColeta.put("moeda.idMoeda", pedidoColeta.getMoeda().getIdMoeda());
			mapPedidoColeta.put("moeda.siglaSimbolo", pedidoColeta.getMoeda().getSiglaSimbolo());	
		}
		if (pedidoColeta.getMunicipio() != null) {
			mapPedidoColeta.put("municipio.idMunicipio", pedidoColeta.getMunicipio().getIdMunicipio());
			mapPedidoColeta.put("municipio.nmMunicipio", pedidoColeta.getMunicipio().getNmMunicipio());
			mapPedidoColeta.put("municipio.unidadeFederativa.sgUnidadeFederativa", pedidoColeta.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());			
		}
		if (pedidoColeta.getFilialByIdFilialResponsavel() != null) {
			mapPedidoColeta.put("sgFilialColeta", pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
			mapPedidoColeta.put("filialByIdFilialResponsavel.idFilial", pedidoColeta.getFilialByIdFilialResponsavel().getIdFilial());
			mapPedidoColeta.put("filialByIdFilialResponsavel.sgFilial", pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
			mapPedidoColeta.put("filialByIdFilialResponsavel.pessoa.nmFantasia", pedidoColeta.getFilialByIdFilialResponsavel().getPessoa().getNmFantasia());			
		}
		if (pedidoColeta.getRotaIntervaloCep() != null) {
			mapPedidoColeta.put("rotaIntervaloCep.idRotaIntervaloCep", pedidoColeta.getRotaIntervaloCep().getIdRotaIntervaloCep());
		}
		if (pedidoColeta.getRotaColetaEntrega() != null) {
			mapPedidoColeta.put("rotaColetaEntrega.idRotaColetaEntrega", pedidoColeta.getRotaColetaEntrega().getIdRotaColetaEntrega());
			mapPedidoColeta.put("rotaColetaEntrega.nrRota", pedidoColeta.getRotaColetaEntrega().getNrRota());
			mapPedidoColeta.put("rotaColetaEntrega.dsRota", pedidoColeta.getRotaColetaEntrega().getDsRota());			
		}
		if (pedidoColeta.getCotacao() != null) {
			mapPedidoColeta.put("cotacao.idCotacao", pedidoColeta.getCotacao().getIdCotacao());
			mapPedidoColeta.put("cotacao.filialByIdFilialOrigem.sgFilial", pedidoColeta.getCotacao().getFilialByIdFilialOrigem().getSgFilial());
			mapPedidoColeta.put("cotacao.nrCotacao", pedidoColeta.getCotacao().getNrCotacao());	
		}
		
		// Busca hor�rio de corte pela Rota de Intervalo de Cep.
		if (pedidoColeta.getNrCep() != null) {
			Long idFilialRotaAtendimento = pedidoColeta.getFilialByIdFilialSolicitante().getIdFilial();
			if(pedidoColeta.getRotaColetaEntrega() != null) {
				idFilialRotaAtendimento = pedidoColeta.getRotaColetaEntrega().getFilial().getIdFilial();
			}
			RotaIntervaloCep rotaIntervaloCep = rotaColetaEntregaService.findRotaAtendimentoCep(
					pedidoColeta.getNrCep(), 
					idCliente, 
					idEnderecoPessoa,
					idFilialRotaAtendimento, //LMS-1321
					JTDateTimeUtils.getDataAtual());
			if(rotaIntervaloCep != null) {
				mapPedidoColeta.put("horarioCorte", rotaIntervaloCep.getHrCorteSolicitacao());								
			}
		}

		List listServicoAdicionalColeta = new ArrayList();		
		for (int i=0; i<pedidoColeta.getServicoAdicionalColetas().size(); i++) {
			ServicoAdicionalColeta servicoAdicionalColeta = (ServicoAdicionalColeta) pedidoColeta.getServicoAdicionalColetas().get(i);
			TypedFlatMap mapServicoAdicionalColeta = new TypedFlatMap();
			
			mapServicoAdicionalColeta.put("idServicoAdicionalColeta", servicoAdicionalColeta.getIdServicoAdicionalColeta());
			mapServicoAdicionalColeta.put("idServicoAdicional", servicoAdicionalColeta.getServicoAdicional().getIdServicoAdicional());
			mapServicoAdicionalColeta.put("dsServicoAdicional", servicoAdicionalColeta.getServicoAdicional().getDsServicoAdicional());

			listServicoAdicionalColeta.add(mapServicoAdicionalColeta);
		}	
		mapPedidoColeta.put("servicoAdicionalColetas", listServicoAdicionalColeta);
				
		return mapPedidoColeta;
	}
	
	/***
	 * Remo��o de um conjunto de registros Master.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getPedidoColetaService().removeByIds(ids);
	}

	/**
	 * Remo��o de um registro Master.
	 */	
	public void removeById(java.lang.Long id) {
		getPedidoColetaService().removeById(id);
		newMaster();
	}	 
	
	public Serializable saveDetalheColeta(TypedFlatMap parameters) {
		
		List notasFiscais = parameters.getList("notaFiscalColetas");
		List chavesNFe = parameters.getList("nrChaveNfe");
	
		// Remove o map para que ao salvar detalhe ele seja refeito de acordo
		// com a tela, e suas modifica��es
		parameters.remove("notaFiscalColetas");
	
		if (notasFiscais != null) {
		    List notasFiscalColetas = new ArrayList();
	
		    for (int i = 0; i < notasFiscais.size(); i++) {
				TypedFlatMap notaFiscal = (TypedFlatMap) notasFiscais.get(i);
				TypedFlatMap mapNotaFiscalColeta = new TypedFlatMap();
				String nrNotaFiscal = notaFiscal.getString("nrNotaFiscal");
				boolean hasChave = false;
		
				if (chavesNFe != null) {
					for (int j = 0; j < chavesNFe.size(); j++) {
						TypedFlatMap chaveNFe = (TypedFlatMap) chavesNFe.get(j);
						if(chaveNFe.getString("nrChave").length() == 44){
							Long nrNotaFromChaveNFe = Long.valueOf(chaveNFe.getString("nrChave").substring(25, 34));
							
							if (Long.valueOf(nrNotaFiscal).equals(nrNotaFromChaveNFe)) {
								if (chaveNFe.getLong("idNotaFiscalColeta") != null && chaveNFe.getLong("idNotaFiscalColeta") > 0) {
									mapNotaFiscalColeta.put("idNotaFiscalColeta", chaveNFe.getLong("idNotaFiscalColeta"));
								}else {
									mapNotaFiscalColeta.put("idNotaFiscalColeta", null);
								}
								
								hasChave = true;
								mapNotaFiscalColeta.put("nrNotaFiscal", nrNotaFiscal);
								mapNotaFiscalColeta.put("nrChave", chaveNFe.getString("nrChave"));
								notasFiscalColetas.add(mapNotaFiscalColeta);
								break;
							}
						}
					}
				}
		
				if (!hasChave) {
				    mapNotaFiscalColeta.put("nrNotaFiscal", nrNotaFiscal);
				    notasFiscalColetas.add(mapNotaFiscalColeta);
				}
		    }
	
		    parameters.put("notaFiscalColetas", notasFiscalColetas);
		}
		
		Long idDoctoServico = parameters.getLong("doctoServico.idDoctoServico");
		if (LongUtils.hasValue(idDoctoServico)) {
			Boolean blEntregaDireta = parameters.getBoolean("blEntregaDireta");
			this.validateEntregaDiretaClienteByNrDoctoServicoAndSgFilialOrigem(idDoctoServico, blEntregaDireta);
		}
		return saveItemInstance(parameters, "detalheColeta");
	}
	
	private void validateEntregaDiretaClienteByNrDoctoServicoAndSgFilialOrigem(Long idConhecimento, Boolean blEntregaDireta) {	
		Conhecimento c = conhecimentoService.findById(idConhecimento);
		detalheColetaService.validateEntregaDiretaCliente(c, blEntregaDireta);
	}

	public ResultSetPage findPaginatedDetalheColeta(Map parameters) {
		ResultSetPage rspDetalheColeta = findPaginatedItemList(parameters, "detalheColeta");
		
		List listDetalheColeta = new ArrayList();
		for(int i=0; i< rspDetalheColeta.getList().size(); i++) {
			DetalheColeta detalheColeta = (DetalheColeta) rspDetalheColeta.getList().get(i);
			TypedFlatMap mapDetalheColeta = new TypedFlatMap();			
			
			mapDetalheColeta.put("idDetalheColeta", detalheColeta.getIdDetalheColeta());
			mapDetalheColeta.put("tpFrete.description", detalheColeta.getTpFrete().getDescription());
			mapDetalheColeta.put("tpFrete.value", detalheColeta.getTpFrete().getValue());
			mapDetalheColeta.put("tpFrete.status", detalheColeta.getTpFrete().getStatus());
			mapDetalheColeta.put("qtVolumes", detalheColeta.getQtVolumes());
			mapDetalheColeta.put("vlMercadoria", detalheColeta.getVlMercadoria());
			mapDetalheColeta.put("psMercadoria", detalheColeta.getPsMercadoria());
			mapDetalheColeta.put("psAforado", detalheColeta.getPsAforado());
			mapDetalheColeta.put("obDetalheColeta", detalheColeta.getObDetalheColeta());
			
			if (detalheColeta.getServico() != null) {
				mapDetalheColeta.put("servico.idServico", detalheColeta.getServico().getIdServico());
				mapDetalheColeta.put("servico.dsServico", detalheColeta.getServico().getDsServico());				
			}
			if (detalheColeta.getNaturezaProduto() != null) {
				mapDetalheColeta.put("naturezaProduto.idNaturezaProduto", detalheColeta.getNaturezaProduto().getIdNaturezaProduto());
				mapDetalheColeta.put("naturezaProduto.dsNaturezaProduto", detalheColeta.getNaturezaProduto().getDsNaturezaProduto());			
			}
			if (detalheColeta.getLocalidadeEspecial() != null) {
				mapDetalheColeta.put("localidadeEspecial.idLocalidadeEspecial", detalheColeta.getLocalidadeEspecial().getIdLocalidadeEspecial());
				mapDetalheColeta.put("localidadeEspecial.dsLocalidade", detalheColeta.getLocalidadeEspecial().getDsLocalidade());
			}
			if (detalheColeta.getMunicipio() != null) {
				mapDetalheColeta.put("municipio.idMunicipio", detalheColeta.getMunicipio().getIdMunicipio());
				mapDetalheColeta.put("municipio.nmMunicipio", detalheColeta.getMunicipio().getNmMunicipio());
				mapDetalheColeta.put("municipio.unidadeFederativa.sgUnidadeFederativa", detalheColeta.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			}
			if (detalheColeta.getFilial() != null) {
				mapDetalheColeta.put("filial.idFilial", detalheColeta.getFilial().getIdFilial());
				mapDetalheColeta.put("filial.sgFilial", detalheColeta.getFilial().getSgFilial());
				mapDetalheColeta.put("filial.pessoa.nmFantasia", detalheColeta.getFilial().getPessoa().getNmFantasia());
			}
			if (detalheColeta.getCliente() != null) {
				mapDetalheColeta.put("cliente.idCliente", detalheColeta.getCliente().getIdCliente());
				mapDetalheColeta.put("cliente.pessoa.nrIdentificacao", detalheColeta.getCliente().getPessoa().getNrIdentificacao());
				mapDetalheColeta.put("nmDestinatario", detalheColeta.getCliente().getPessoa().getNmPessoa());
			} else if (detalheColeta.getNmDestinatario() != null) {
				mapDetalheColeta.put("nmDestinatario", detalheColeta.getNmDestinatario());
			}
			if (detalheColeta.getMoeda() != null) {
				mapDetalheColeta.put("moeda.idMoeda", detalheColeta.getMoeda().getIdMoeda());
				mapDetalheColeta.put("moeda.dsSimbolo", detalheColeta.getMoeda().getDsSimbolo());
				mapDetalheColeta.put("moeda.sgMoeda", detalheColeta.getMoeda().getSgMoeda());
			}
			if (detalheColeta.getCotacao() != null) {
				mapDetalheColeta.put("cotacao.idCotacao", detalheColeta.getCotacao().getIdCotacao());
			}
			
			getDoctoServicoByDetalheColeta(detalheColeta, mapDetalheColeta);
			
			if (detalheColeta.getCtoInternacional() != null) {
				mapDetalheColeta.put("ctoInternacional.idDoctoServico", detalheColeta.getCtoInternacional().getIdDoctoServico());				
				mapDetalheColeta.put("ctoInternacional.sgPais", detalheColeta.getCtoInternacional().getSgPais());
				mapDetalheColeta.put("ctoInternacional.nrCrt", detalheColeta.getCtoInternacional().getNrCrt());
			}
			
			List listNotaFiscalColetas = new ArrayList();
			for(int j=0; j<detalheColeta.getNotaFiscalColetas().size(); j++) {
				NotaFiscalColeta notaFiscalColeta = (NotaFiscalColeta) detalheColeta.getNotaFiscalColetas().get(j);
				TypedFlatMap mapNotaFiscalColeta = new TypedFlatMap();
				
				mapNotaFiscalColeta.put("idNotaFiscalColeta", notaFiscalColeta.getIdNotaFiscalColeta());
				mapNotaFiscalColeta.put("nrNotaFiscal", notaFiscalColeta.getNrNotaFiscal());				
				
				listNotaFiscalColetas.add(mapNotaFiscalColeta);
			}
			
			mapDetalheColeta.put("notaFiscalColetas", listNotaFiscalColetas);
			mapDetalheColeta.put("blEntregaDireta", detalheColeta.getBlEntregaDireta());
			listDetalheColeta.add(mapDetalheColeta);
		}
		
		rspDetalheColeta.setList(listDetalheColeta);
		
		return rspDetalheColeta;		
	}

	private void getDoctoServicoByDetalheColeta(DetalheColeta detalheColeta, TypedFlatMap mapDetalheColeta) {
		if(detalheColeta.getDoctoServico()!=null){
			mapDetalheColeta.put("doctoServico.idDoctoServico", detalheColeta.getDoctoServico().getIdDoctoServico());    			
			mapDetalheColeta.put("doctoServico.tpDoctoSgFilial", detalheColeta.getDoctoServico().getTpDocumentoServico().getValue() 
					 + " " + detalheColeta.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
			mapDetalheColeta.put("doctoServico.nrDoctoServico", detalheColeta.getDoctoServico().getNrDoctoServico());
			
			mapDetalheColeta.put("awb", awbService.findPreAwbAwbByIdDoctoServico(detalheColeta.getDoctoServico().getIdDoctoServico()));
			
		}
	}

	public Integer getRowCountDetalheColeta(Map parameters){
		return getRowCountItemList(parameters, "detalheColeta");
	}	
	
	public Object findByIdDetalheColeta(MasterDetailKey key) {
		DetalheColeta detalheColeta = (DetalheColeta) findItemById(key, "detalheColeta");
		TypedFlatMap mapDetalheColeta = new TypedFlatMap();			
		
		mapDetalheColeta.put("idDetalheColeta", detalheColeta.getIdDetalheColeta());
		mapDetalheColeta.put("tpFrete.description", detalheColeta.getTpFrete().getDescription());
		mapDetalheColeta.put("tpFrete.value", detalheColeta.getTpFrete().getValue());
		mapDetalheColeta.put("tpFrete.status", detalheColeta.getTpFrete().getStatus());
		mapDetalheColeta.put("qtVolumes", detalheColeta.getQtVolumes());
		mapDetalheColeta.put("vlMercadoria", detalheColeta.getVlMercadoria());
		mapDetalheColeta.put("psMercadoria", detalheColeta.getPsMercadoria());
		mapDetalheColeta.put("psAforado", detalheColeta.getPsAforado());
		mapDetalheColeta.put("obDetalheColeta", detalheColeta.getObDetalheColeta());
		
		if (detalheColeta.getServico() != null) {
			mapDetalheColeta.put("servico.idServico", detalheColeta.getServico().getIdServico());
			mapDetalheColeta.put("servico.dsServico", detalheColeta.getServico().getDsServico());				
		}
		if (detalheColeta.getNaturezaProduto() != null) {
			mapDetalheColeta.put("naturezaProduto.idNaturezaProduto", detalheColeta.getNaturezaProduto().getIdNaturezaProduto());
			mapDetalheColeta.put("naturezaProduto.dsNaturezaProduto", detalheColeta.getNaturezaProduto().getDsNaturezaProduto());			
		}
		if (detalheColeta.getLocalidadeEspecial() != null) {
			mapDetalheColeta.put("localidadeEspecial.idLocalidadeEspecial", detalheColeta.getLocalidadeEspecial().getIdLocalidadeEspecial());
			mapDetalheColeta.put("localidadeEspecial.dsLocalidade", detalheColeta.getLocalidadeEspecial().getDsLocalidade());
		}
		if (detalheColeta.getMunicipio() != null) {
			mapDetalheColeta.put("municipio.idMunicipio", detalheColeta.getMunicipio().getIdMunicipio());
			mapDetalheColeta.put("municipioFilial.municipio.idMunicipio", detalheColeta.getMunicipio().getIdMunicipio());
			mapDetalheColeta.put("municipioFilial.municipio.nmMunicipio", detalheColeta.getMunicipio().getNmMunicipio());
			mapDetalheColeta.put("municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa", detalheColeta.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		}
		if (detalheColeta.getFilial() != null) {
			mapDetalheColeta.put("filial.idFilial", detalheColeta.getFilial().getIdFilial());
			mapDetalheColeta.put("filial.sgFilial", detalheColeta.getFilial().getSgFilial());
			mapDetalheColeta.put("filial.pessoa.nmFantasia", detalheColeta.getFilial().getPessoa().getNmFantasia());
		}
		if (detalheColeta.getCliente() != null) {
			mapDetalheColeta.put("cliente.idCliente", detalheColeta.getCliente().getIdCliente());
			mapDetalheColeta.put("cliente.pessoa.nrIdentificacao", detalheColeta.getCliente().getPessoa().getNrIdentificacao());
			mapDetalheColeta.put("cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(detalheColeta.getCliente().getPessoa().getTpIdentificacao(),
																											detalheColeta.getCliente().getPessoa().getNrIdentificacao()));
			mapDetalheColeta.put("nmDestinatario", detalheColeta.getCliente().getPessoa().getNmPessoa());
		} else if (detalheColeta.getNmDestinatario() != null) {
			mapDetalheColeta.put("nmDestinatario", detalheColeta.getNmDestinatario());
		}
		if (detalheColeta.getMoeda() != null) {
			mapDetalheColeta.put("moeda.idMoeda", detalheColeta.getMoeda().getIdMoeda());
			mapDetalheColeta.put("moeda.dsSimbolo", detalheColeta.getMoeda().getDsSimbolo());
			mapDetalheColeta.put("moeda.sgMoeda", detalheColeta.getMoeda().getSgMoeda());
		}
		if (detalheColeta.getCotacao() != null) {
			mapDetalheColeta.put("cotacao.idCotacao", detalheColeta.getCotacao().getIdCotacao());
		}
		if(detalheColeta.getDoctoServico() != null ) {
			DoctoServico ds = detalheColeta.getDoctoServico();
    		mapDetalheColeta.put("doctoServico.idDoctoServico", ds.getIdDoctoServico());
    		mapDetalheColeta.put("doctoServico.nrDoctoServico", ds.getNrDoctoServico());
    		mapDetalheColeta.put("doctoServico.tpDocumentoServico", ds.getTpDocumentoServico().getValue());
    		mapDetalheColeta.put("doctoServico.filialByIdFilialOrigem.idFilial", ds.getFilialByIdFilialOrigem().getIdFilial());    		
    		mapDetalheColeta.put("doctoServico.filialByIdFilialOrigem.sgFilial", ds.getFilialByIdFilialOrigem().getSgFilial());
		}
		if (detalheColeta.getCtoInternacional() != null) {
			mapDetalheColeta.put("ctoInternacional.idDoctoServico", detalheColeta.getCtoInternacional().getIdDoctoServico());
			mapDetalheColeta.put("ctoInternacional.sgPais", detalheColeta.getCtoInternacional().getSgPais());
			mapDetalheColeta.put("ctoInternacional.nrCrt", detalheColeta.getCtoInternacional().getNrCrt());
		}		
		
		if(detalheColeta.getAwbColetas() != null && !detalheColeta.getAwbColetas().isEmpty()) {
			AwbColeta awbColeta = (AwbColeta) detalheColeta.getAwbColetas().get(IntegerUtils.ZERO);
			Awb awb = awbService.findById(awbColeta.getAwb().getIdAwb());
			
				mapDetalheColeta.put("ciaFilialMercurio.empresa.idEmpresa", awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa());
			if(awb.getTpStatusAwb().getValue().equals(ConstantesExpedicao.TP_STATUS_PRE_AWB)) {
				mapDetalheColeta.put("idPreAwb", awb.getIdAwb());
				mapDetalheColeta.put("nmAeroporto", awb.getCiaFilialMercurio().getEmpresa().getPessoa().getNmPessoa());
			} else {
				mapDetalheColeta.put("ciaFilialMercurio.empresa.idEmpresa", awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa());
				mapDetalheColeta.put("awb.idAwb", awb.getIdAwb());
				mapDetalheColeta.put("awb.nrAwb", awb.getNrAwb());
			}
		}
		
		List listNotaFiscalColetas = new ArrayList();
		List listNFe = new ArrayList();
	
		for(int j=0; j<detalheColeta.getNotaFiscalColetas().size(); j++) {
			NotaFiscalColeta notaFiscalColeta = (NotaFiscalColeta) detalheColeta.getNotaFiscalColetas().get(j);
			TypedFlatMap mapNotaFiscalColeta = new TypedFlatMap();
			
	    if(notaFiscalColeta.getNrNotaFiscal() != null || notaFiscalColeta.getNrChave() != null){
			mapNotaFiscalColeta.put("idNotaFiscalColeta", notaFiscalColeta.getIdNotaFiscalColeta());
	    }
	    
	    if(notaFiscalColeta.getNrNotaFiscal() != null){
			mapNotaFiscalColeta.put("nrNotaFiscal", notaFiscalColeta.getNrNotaFiscal());				
			listNotaFiscalColetas.add(mapNotaFiscalColeta);
		}
		
	    if(notaFiscalColeta.getNrChave() != null){
	    	mapNotaFiscalColeta.put("nrChave", notaFiscalColeta.getNrChave());
	    	listNFe.add(mapNotaFiscalColeta);
	    }
	}

	mapDetalheColeta.put("notaFiscalColetas", listNotaFiscalColetas);
	mapDetalheColeta.put("nrChaveNfe", listNFe);
	mapDetalheColeta.put("blEntregaDireta", detalheColeta.getBlEntregaDireta());
		
		return mapDetalheColeta;
		
	}	
	
	/***
	 * Remove uma lista de registros items.
	 * @param ids ids dos desci�oes item a serem removidos.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsDetalheColeta(List ids) {
		super.removeItemByIds(ids, "detalheColeta");
	}
	
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) { 
		
		/**
		 * Declaracao da classe pai
		 */	
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(PedidoColeta.class);

		/**
		 * Esta classe e reponsavel por ordenar a List dos filhos que estao
		 * em memoria de acordo com as regras de negocio
		 */
		Comparator descComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				DetalheColeta detalheColeta1 = (DetalheColeta) obj1;
				DetalheColeta detalheColeta2 = (DetalheColeta) obj2;
				
				int retorno = 0;
				
				retorno =  detalheColeta1.getServico().getDsServico().toString().compareTo(detalheColeta2.getServico().getDsServico().toString());
				
				if(detalheColeta1.getDoctoServico() != null && detalheColeta2.getDoctoServico() != null){
					detalheColeta1.setAwb(awbService.findUltimoAwbByDoctoServico(detalheColeta1.getDoctoServico().getIdDoctoServico()));
					detalheColeta2.setAwb(awbService.findUltimoAwbByDoctoServico(detalheColeta2.getDoctoServico().getIdDoctoServico()));
					if(retorno == 0 && detalheColeta1.getAwb() != null && detalheColeta2.getAwb()!= null){
						retorno  = detalheColeta1.getAwb().getNrAwb().compareTo(detalheColeta2.getAwb().getNrAwb());
					}
					if(retorno ==0){
						retorno = detalheColeta1.getDoctoServico().getNrDoctoServico().compareTo(detalheColeta2.getDoctoServico().getNrDoctoServico());
					}
				}
								
				return retorno;
			}
		};
		
		/**
		 * Esta instancia � responsavel por carregar os 
		 * items filhos na sess�o a partir do banco de dados.
		 */
		ItemListConfig itemDetalheColeta = new ItemListConfig() {
 
			/**
			 * Find paginated do filho
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */
			public List initialize(Long masterId, Map parameters) { 
				return detalheColetaService.findDetalheColeta(masterId);				
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
				return detalheColetaService.getRowCountDetalheColeta(masterId);				
			}

			/**
			 * Chama esta funcao depois de editar um item da grid filho
			 * E retira atributos desnecessarios para o filho
			 * 
			 * @param newBean 
			 * @param oldBean 
			 */
			public void modifyItemValues(Object newBean, Object bean) {
				DetalheColeta detalheColetaNew = (DetalheColeta) newBean;
				DetalheColeta detalheColetaOld = (DetalheColeta) bean;
				
				detalheColetaOld.setIdDetalheColeta(detalheColetaNew.getIdDetalheColeta());				
				detalheColetaOld.setTpFrete(detalheColetaNew.getTpFrete());
				detalheColetaOld.setVlMercadoria(detalheColetaNew.getVlMercadoria());
				detalheColetaOld.setQtVolumes(detalheColetaNew.getQtVolumes());
				detalheColetaOld.setPsMercadoria(detalheColetaNew.getPsMercadoria());
				detalheColetaOld.setPsAforado(detalheColetaNew.getPsAforado());
				detalheColetaOld.setObDetalheColeta(detalheColetaNew.getObDetalheColeta());
				detalheColetaOld.setNmDestinatario(detalheColetaNew.getNmDestinatario());
				detalheColetaOld.setPedidoColeta(detalheColetaNew.getPedidoColeta());
				detalheColetaOld.setServico(detalheColetaNew.getServico());
				detalheColetaOld.setNaturezaProduto(detalheColetaNew.getNaturezaProduto());
				detalheColetaOld.setMoeda(detalheColetaNew.getMoeda());
				detalheColetaOld.setLocalidadeEspecial(detalheColetaNew.getLocalidadeEspecial());
				detalheColetaOld.setMunicipio(detalheColetaNew.getMunicipio());
				detalheColetaOld.setFilial(detalheColetaNew.getFilial());
				detalheColetaOld.setCliente(detalheColetaNew.getCliente());
				detalheColetaOld.setEventoColeta(detalheColetaNew.getEventoColeta());
				detalheColetaOld.setCotacao(detalheColetaNew.getCotacao());
				detalheColetaOld.setCtoInternacional(detalheColetaNew.getCtoInternacional());
				detalheColetaOld.setBlEntregaDireta(detalheColetaNew.getBlEntregaDireta());
				
				List listNotaFiscalColeta = new ArrayList();
				List listAwbColeta = new ArrayList();
				
				for(int i=0; i<detalheColetaNew.getNotaFiscalColetas().size(); i++) {
					NotaFiscalColeta notaFiscalColeta = (NotaFiscalColeta) detalheColetaNew.getNotaFiscalColetas().get(i);
					notaFiscalColeta.setIdNotaFiscalColeta(null);
					notaFiscalColeta.setDetalheColeta(detalheColetaOld);
					listNotaFiscalColeta.add(notaFiscalColeta);
				}
				detalheColetaOld.getNotaFiscalColetas().clear();
				
				for(int i=0; i<detalheColetaNew.getAwbColetas().size(); i++) {
					AwbColeta awbColeta = (AwbColeta) detalheColetaNew.getAwbColetas().get(i);
					awbColeta.setIdAwbColeta(null);
					awbColeta.setDetalheColeta(detalheColetaOld);
					listAwbColeta.add(awbColeta);
				}				
				detalheColetaOld.getAwbColetas().clear();
				
				detalheColetaOld.setNotaFiscalColetas(listNotaFiscalColeta);
				detalheColetaOld.setAwbColetas(listAwbColeta);
			}	
			
			/**
			 * Mapeia atributos de dominio do pojo filho
			 */			
			public Map configItemDomainProperties() {
				Map props = new HashMap(1);
				props.put("tpFrete", "DM_TIPO_FRETE");				
				return props;
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
				DetalheColeta detalheColeta = (DetalheColeta)bean;
				TypedFlatMap param = (TypedFlatMap) parameters;
				
				detalheColeta.setIdDetalheColeta(param.getLong("idDetalheColeta"));
				DomainValue tpFrete = getDomainValueService().findDomainValueByValue("DM_TIPO_FRETE", param.getString("tpFrete"));
				detalheColeta.setTpFrete(tpFrete);
				detalheColeta.setVlMercadoria(param.getBigDecimal("vlMercadoria"));
				detalheColeta.setQtVolumes(param.getInteger("qtVolumes"));
				detalheColeta.setPsMercadoria(param.getBigDecimal("psMercadoria"));
				detalheColeta.setPsAforado(param.getBigDecimal("psAforado"));
				detalheColeta.setObDetalheColeta(param.getString("obDetalheColeta"));
				detalheColeta.setBlEntregaDireta(param.getBoolean("blEntregaDireta"));
				if(!param.getString("nmDestinatario").equals("")) {
					detalheColeta.setNmDestinatario(param.getString("nmDestinatario"));
				}
				if(param.getLong("masterId") != null) {
					detalheColeta.setPedidoColeta(getPedidoColetaService().findById(param.getLong("masterId")));
				}
				detalheColeta.setServico(servicoService.findById(param.getLong("servico.idServico")));
				detalheColeta.setNaturezaProduto(naturezaProdutoService.findById(param.getLong("naturezaProduto.idNaturezaProduto")));
				detalheColeta.setMoeda(moedaService.findById(param.getLong("moeda.idMoeda")));
				if(param.getLong("localidadeEspecial.idLocalidadeEspecial") != null) {
					detalheColeta.setLocalidadeEspecial(localidadeEspecialService.findById(param.getLong("localidadeEspecial.idLocalidadeEspecial")));
				}
				if(param.getLong("municipio.idMunicipio") != null) {
					detalheColeta.setMunicipio(municipioService.findById(param.getLong("municipio.idMunicipio")));
				}
				if(param.getLong("filial.idFilial") != null) {
					detalheColeta.setFilial(filialService.findById(param.getLong("filial.idFilial")));
				}
				if(param.getLong("cliente.idCliente") != null) {
					detalheColeta.setCliente(clienteService.findById(param.getLong("cliente.idCliente")));
				}
				if(param.getLong("cotacao.idCotacao") != null) {
					detalheColeta.setCotacao(cotacaoService.findById(param.getLong("cotacao.idCotacao")));
				}
				if(param.getLong("ctoInternacional.idDoctoServico") != null) {
					detalheColeta.setCtoInternacional(ctoInternacionalService.findById(param.getLong("ctoInternacional.idDoctoServico")));					
				}				

				List listMapNotaFiscalColetas = param.getList("notaFiscalColetas");		
				List listNotaFiscalColetas = new ArrayList();
				if(listMapNotaFiscalColetas != null) {
					for (int i=0; i<listMapNotaFiscalColetas.size(); i++) {
						TypedFlatMap typedFlatMap = (TypedFlatMap) listMapNotaFiscalColetas.get(i);
						NotaFiscalColeta notaFiscalColeta = new NotaFiscalColeta();
						Long idNotaFiscalColeta = typedFlatMap.getLong("idNotaFiscalColeta");
						if(idNotaFiscalColeta != null && !(idNotaFiscalColeta.longValue() <= 0)) {
							notaFiscalColeta.setIdNotaFiscalColeta(typedFlatMap.getLong("idNotaFiscalColeta"));	
						} else {
							notaFiscalColeta.setIdNotaFiscalColeta(null);
						}						
						notaFiscalColeta.setNrNotaFiscal(typedFlatMap.getInteger("nrNotaFiscal"));
			notaFiscalColeta.setNrChave(typedFlatMap.getString("nrChave"));
						notaFiscalColeta.setDetalheColeta(detalheColeta);
						
						listNotaFiscalColetas.add(notaFiscalColeta);
					}
				}
				
				List listMapAwbColetas = param.getList("awbColetas");
				List listAwbColetas = new ArrayList();
				if(listMapAwbColetas != null) {
					for (int i=0; i<listMapAwbColetas.size(); i++) {
						TypedFlatMap typedFlatMap = (TypedFlatMap) listMapAwbColetas.get(i);
						AwbColeta awbColeta = new AwbColeta();
						Long idAwbColeta = typedFlatMap.getLong("idAwbColeta");
						if(idAwbColeta != null && !(idAwbColeta.longValue() <= 0)) {
							awbColeta.setIdAwbColeta(typedFlatMap.getLong("idAwbColeta"));	
						} else {
							awbColeta.setIdAwbColeta(null);
						}
						Awb awb = new Awb();
						awb.setIdAwb(typedFlatMap.getLong("idAwb"));
						awbColeta.setAwb(awb);
						awbColeta.setDetalheColeta(detalheColeta);
						
						listAwbColetas.add(awbColeta);
					}
				}
				
				detalheColeta.setNotaFiscalColetas(listNotaFiscalColetas);
				detalheColeta.setAwbColetas(listAwbColetas);
				
				// Insere um Evento de Coleta para o Detalhe de Coleta.
				if(param.getLong("eventoColeta.usuario.idUsuario") != null && 
							param.getLong("eventoColeta.ocorrenciaColeta.idOcorrenciaColeta") != null) {
					EventoColeta eventoColeta = new EventoColeta();
					eventoColeta.setPedidoColeta(detalheColeta.getPedidoColeta());
					
					Usuario usuario = usuarioService.findById(param.getLong("eventoColeta.usuario.idUsuario"));				
					eventoColeta.setUsuario(usuario);

					OcorrenciaColeta ocorrenciaColeta = ocorrenciaColetaService.findById(param.getLong("eventoColeta.ocorrenciaColeta.idOcorrenciaColeta"));
					eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);

					eventoColeta.setDsDescricao(param.getString("eventoColeta.dsDescricao"));
					eventoColeta.setDhEvento(JTDateTimeUtils.getDataHoraAtual());
					eventoColeta.setTpEventoColeta(new DomainValue("LI"));					
										
					detalheColeta.setEventoColeta(eventoColeta);
				}				

				return detalheColeta;
			}			

		};
		
		config.addItemConfig("detalheColeta", DetalheColeta.class, itemDetalheColeta, descComparator);
		return config;
	}


    public void validateChaveNfe(){
	throw new BusinessException("LMS-04400");
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Map findSomatorios(TypedFlatMap criteria){
		Map map = new HashMap();
		
		Long idColeta = criteria.getLong("idColeta");

		MasterEntry entry = getMasterFromSession(idColeta, true);
		
		ItemList items = getItemsFromSession(entry, "detalheColeta");
		ItemListConfig config = getMasterConfig().getItemListConfig("detalheColeta");
		
		BigDecimal vlDocumentoTotal = new BigDecimal("0.00");
		vlDocumentoTotal = vlDocumentoTotal.setScale(2, RoundingMode.HALF_UP);
		BigDecimal vlPesoTotal = new BigDecimal("0.000");
		vlPesoTotal = vlPesoTotal.setScale(3, RoundingMode.HALF_UP);
		Integer totalCtes = 0;
		
		for (Iterator iter = items.iterator(idColeta, config); iter.hasNext();) {
			DetalheColeta detalheColeta = (DetalheColeta)iter.next();
			if(detalheColeta.getDoctoServico() != null){
				totalCtes = totalCtes + 1;
				vlPesoTotal = vlPesoTotal.add(detalheColeta.getPsMercadoria());
				vlDocumentoTotal = vlDocumentoTotal.add(detalheColeta.getVlMercadoria());
			}
		}
		
		map.put("qtdeTotalDocumentos", totalCtes);
		map.put("vlPesoTotal", vlPesoTotal);
		map.put("valorTotalDocumentos",vlDocumentoTotal);

		return map;
	}
    

	/**
	 * ####################################
	 * # Fim dos m�todos para tela de DF2 #
	 * ####################################
	 */	
	
	/**
	 * Declara��o servi�o principal da Action.
	 * 
	 * @param pedidoColetaService
	 */
	private final PedidoColetaService getPedidoColetaService() {
		return (PedidoColetaService) super.getMasterService();
	}	
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.setMasterService(pedidoColetaService);
	}
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setLocalidadeEspecialService(LocalidadeEspecialService localidadeEspecialService) {
		this.localidadeEspecialService = localidadeEspecialService;
	}
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setNaturezaProdutoService(NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}
	public void setRestricaoColetaService(RestricaoColetaService restricaoColetaService) {
		this.restricaoColetaService = restricaoColetaService;
	}
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	public void setRotaIntervaloCepService(RotaIntervaloCepService rotaIntervaloCepService) {
		this.rotaIntervaloCepService = rotaIntervaloCepService;
	}
	public void setServicoAdicionalService(ServicoAdicionalService servicoAdicionalService) {
		this.servicoAdicionalService = servicoAdicionalService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setRegiaoColetaEntregaFilService(RegiaoColetaEntregaFilService regiaoColetaEntregaFilService) {
		this.regiaoColetaEntregaFilService = regiaoColetaEntregaFilService;
	}
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	public void setOcorrenciaColetaService(OcorrenciaColetaService ocorrenciaColetaService) {
		this.ocorrenciaColetaService = ocorrenciaColetaService;
	}
	public void setHorarioCorteClienteService(HorarioCorteClienteService horarioCorteClienteService) {
		this.horarioCorteClienteService = horarioCorteClienteService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}
	public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	public void setLiberacaoEmbarqueService(LiberacaoEmbarqueService liberacaoEmbarqueService) {
		this.liberacaoEmbarqueService = liberacaoEmbarqueService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	
	public void setEmbarqueValidationService(EmbarqueValidationService embarqueValidationService) {
		this.embarqueValidationService = embarqueValidationService;
	}
}