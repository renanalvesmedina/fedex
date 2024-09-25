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
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.NotaFiscalColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.RestricaoColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.LocalidadeEspecialService;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.coleta.model.service.RestricaoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoAdicionalService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.EmbarqueValidationService;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.model.service.RotaIntervaloCepService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.DiaUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.HorarioCorteCliente;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.ProibidoEmbarque;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.CotacaoService;
import com.mercurio.lms.vendas.model.service.HorarioCorteClienteService;
import com.mercurio.lms.vendas.model.service.LiberacaoEmbarqueService;
import com.mercurio.lms.vendas.model.service.ProibidoEmbarqueService;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.coleta.cadastrarPedidoColetaAction" 
 */

public class CadastrarPedidoColetaAction extends MasterDetailAction {

	private static final String TP_PEDIDO_COLETA_AEREO = "AE";
	private DetalheColetaService detalheColetaService;
	private ClienteService clienteService;
	private EnderecoPessoaService enderecoPessoaService;	
	private TelefoneEnderecoService telefoneEnderecoService;
	private ServicoAdicionalService servicoAdicionalService;
	private RotaIntervaloCepService rotaIntervaloCepService;
	private MoedaService moedaService;
	private PpeService ppeService;
	private ParametroGeralService parametroGeralService;
	private FilialService filialService;
	private ProibidoEmbarqueService proibidoEmbarqueService;
	private ServicoService servicoService; 
	private NaturezaProdutoService naturezaProdutoService;
	private LocalidadeEspecialService localidadeEspecialService;
	private MunicipioService municipioService;
	private AwbService awbService;
	private EmpresaService empresaService;	
	private UsuarioService usuarioService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private MunicipioFilialService municipioFilialService;
	private RestricaoColetaService restricaoColetaService;
	private OcorrenciaColetaService ocorrenciaColetaService;
	private CotacaoService cotacaoService;
	private HorarioCorteClienteService horarioCorteClienteService;
	private CtoInternacionalService ctoInternacionalService;
	private ConfiguracoesFacade configuracoesFacade;
	private VersaoDescritivoPceService versaoDescritivoPceService;
	private ContatoService contatoService;
	private LiberacaoEmbarqueService liberacaoEmbarqueService;
	private ConhecimentoService conhecimentoService;
	private MdaService mdaService;
	private ReciboReembolsoService reciboReembolsoService;
	private DoctoServicoService doctoServicoService;
	private InscricaoEstadualService inscricaoEstadualService;
	private TipoTributacaoIEService tipoTributacaoIEService;
	private DiaUtils diaUtils;
	private EmbarqueValidationService embarqueValidationService;

	private ConteudoParametroFilialService conteudoParametroFilialService;
	
	public void setTipoTributacaoIEService(
			TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}

	public void setInscricaoEstadualService(
			InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setReciboReembolsoService(
			ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}
	
	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	
	/**
	 * Declaração serviço principal da Action.
	 * 
	 * @param pedidoColetaService
	 */
	private PedidoColetaService getPedidoColetaService() {
		return (PedidoColetaService) super.getMasterService();
	}	

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.setMasterService(pedidoColetaService);
	}

	public void setDetalheColetaService(
			DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}	

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setEnderecoPessoaService(
			EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public void setServicoAdicionalService(
			ServicoAdicionalService servicoAdicionalService) {
		this.servicoAdicionalService = servicoAdicionalService;
	}

	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}

	public void setRotaIntervaloCepService(
			RotaIntervaloCepService rotaIntervaloCepService) {
		this.rotaIntervaloCepService = rotaIntervaloCepService;
	}

	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}

	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setProibidoEmbarqueService(
			ProibidoEmbarqueService proibidoEmbarqueService) {
		this.proibidoEmbarqueService = proibidoEmbarqueService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public void setNaturezaProdutoService(
			NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}

	public void setLocalidadeEspecialService(
			LocalidadeEspecialService localidadeEspecialService) {
		this.localidadeEspecialService = localidadeEspecialService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setRotaColetaEntregaService(
			RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	public void setMunicipioFilialService(
			MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public void setRestricaoColetaService(
			RestricaoColetaService restricaoColetaService) {
		this.restricaoColetaService = restricaoColetaService;
	}

	public void setOcorrenciaColetaService(
			OcorrenciaColetaService ocorrenciaColetaService) {
		this.ocorrenciaColetaService = ocorrenciaColetaService;
	}

	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}

	public void setHorarioCorteClienteService(
			HorarioCorteClienteService horarioCorteClienteService) {
		this.horarioCorteClienteService = horarioCorteClienteService;
	}

	public void setCtoInternacionalService(
			CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setVersaoDescritivoPceService(
			VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}

	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	public List findLookupCliente(Map criteria) {		
		List clientes = clienteService.findLookup(criteria);
		List retorno = new ArrayList();
		if (clientes != null) {
			for (Iterator iter = clientes.iterator(); iter.hasNext();) {
				TypedFlatMap map = new TypedFlatMap();
				Cliente cliente = (Cliente) iter.next();
				map.put("idCliente", cliente.getIdCliente());
				map.put("tpSituacao", cliente.getTpSituacao());
				map.put("pessoa.nmPessoa", cliente.getPessoa().getNmPessoa());
				map.put("pessoa.nrIdentificacao", cliente.getPessoa()
						.getNrIdentificacao());
				map.put("pessoa.nrIdentificacaoFormatado",
						FormatUtils.formatIdentificacao(cliente.getPessoa()));
				map.put("pessoa.tpIdentificacao", cliente.getPessoa()
						.getTpIdentificacao());
				map.put("pessoa.tpCliente", cliente.getTpCliente());
				EnderecoPessoa endereco = enderecoPessoaService
						.findByIdPessoa(cliente.getPessoa().getIdPessoa());
				map.put("pessoa.endereco.municipio.idMunicipio", endereco
						.getMunicipio().getIdMunicipio());
				//nrDdd ???
				//nrTelefone ???
				retorno.add(map);

			}			
		}
		return retorno;
	}
	
	public TypedFlatMap findDadosColetaAwb(TypedFlatMap criteria){
		Long idAwb = criteria.getLong("idAwb");
		Long idConhecimento = criteria.getLong("idConhecimento");
		TypedFlatMap tfm = awbService.findDadosColetaAwb(idAwb, idConhecimento);
		
		if (LongUtils.hasValue(idConhecimento)) {
			NaturezaProduto natureza = naturezaProdutoService
					.findByIdConhecimento(criteria.getLong("idConhecimento"));
		tfm.put("dsNaturezaProduto", natureza.getIdNaturezaProduto());
		}
		return tfm;
	}	

	/**
	 * Pesquisa de Filial utilizada na busca de uma cotação.
	 * 
	 * @param criteria
	 * @return
	 */
	public List findLookupFilial(Map criteria) {
		List listFiliais = filialService.findLookup(criteria);
		List retorno = new ArrayList();
		for (Iterator iter = listFiliais.iterator(); iter.hasNext();) {
			Filial filial = (Filial) iter.next();
			TypedFlatMap map = new TypedFlatMap();
			map.put("idFilial", filial.getIdFilial());
			map.put("sgFilial", filial.getSgFilial());
			retorno.add(map);
		}
		return retorno;
	}
	
	public List findLookupEnderecoPessoa(Map criteria) {		
		return enderecoPessoaService.findLookup(criteria);
	}	
	
	public List findLookupTelefoneEndereco(Map criteria) {
		criteria.put("nrTelefone", ((String)criteria.get("nrTelefone")) + "%");
		List retorno = telefoneEnderecoService.findLookup(criteria); 
		if (retorno.size()==0) {
			// MENSAGEM: Telefone não encontrado. Deseja incluir como um novo
			// telefone do cliente ao efetivar o pedido de coleta?
			throw new BusinessException("LMS-02081");
		}
		return retorno;
		
	}
	
    public List findModoPedidoColeta(TypedFlatMap criteria) {
    	List dominiosValidos = new ArrayList();
    	dominiosValidos.add("TE");
    	dominiosValidos.add("BA");
		List retorno = this.getDomainValueService().findByDomainNameAndValues(
				"DM_MODO_PEDIDO_COLETA", dominiosValidos);
    	return retorno;
    }
	
	public List findServicoAdicional(Map criteria) {
		List retorno = new ArrayList();
		List listServicosAdicionais = servicoAdicionalService
				.findServicosAdicionaisAtivos(criteria);
		for (Iterator iter = listServicosAdicionais.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			ServicoAdicional servicoAdicional = (ServicoAdicional) iter.next();
			map.put("idServicoAdicional",
					servicoAdicional.getIdServicoAdicional());
			map.put("dsServicoAdicional",
					servicoAdicional.getDsServicoAdicional());
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
	
	public List findSiglaFilial(Map criteria) {
		List retorno = new ArrayList();
		retorno.add(new TypedFlatMap());
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
	
	public List findLookupMunicipio(TypedFlatMap criteria) {
		List retornoPesquisa = null;
		String nmMunicipio = criteria.getString("municipio.nmMunicipio");
		Long idMunicipio = criteria.getLong("municipio.idMunicipio");
		if (idMunicipio!=null){
			retornoPesquisa = municipioFilialService
					.findMunicipioFilialVigenteByIdMunicipio(idMunicipio);
		} else {
			// Faz uma pesquisa exata. Caso nao retorne registros, faz outra
			// pesquisa com like.
			retornoPesquisa = municipioFilialService
					.findMunicipioFilialVigenteByNmMunicipio(nmMunicipio, true);
			if (retornoPesquisa.size()==0){
				retornoPesquisa = municipioFilialService
						.findMunicipioFilialVigenteByNmMunicipio(nmMunicipio,
								false);
			}
		}
		FilterList filter = new FilterList(retornoPesquisa) {
			public Map filterItem(Object item) {
				MunicipioFilial mf = (MunicipioFilial)item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap
						.put("idMunicipioFilial", mf.getIdMunicipioFilial());
				typedFlatMap.put("municipio.idMunicipio", mf.getMunicipio()
						.getIdMunicipio());
				typedFlatMap.put("municipio.nmMunicipio", mf.getMunicipio()
						.getNmMunicipio());
				typedFlatMap.put(
						"municipio.unidadeFederativa.idUnidadeFederativa", mf
								.getMunicipio().getUnidadeFederativa()
								.getIdUnidadeFederativa());
				typedFlatMap.put(
						"municipio.unidadeFederativa.sgUnidadeFederativa", mf
								.getMunicipio().getUnidadeFederativa()
								.getSgUnidadeFederativa());
				
				typedFlatMap.put("filial.sgFilial", mf.getFilial()
						.getSgFilial());
				typedFlatMap.put("filial.pessoa.nmFantasia", mf.getFilial()
						.getPessoa().getNmFantasia());
				return typedFlatMap;
			}
		};
		return (List)filter.doFilter();
	}
	
	public TypedFlatMap validaBloqueioDestinatario(TypedFlatMap criteria){
		criteria.put("clienteBloqueado", getBloqueioCliente(criteria));
		return criteria;
	}
	
	/**
	 * Método que valida a vigencia do municipio selecionado.
	 * 
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap validaVigenciaAtendimento(TypedFlatMap criteria) {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		municipioFilialService.validateVigenciaAtendimento(
				criteria.getLong("idMunicipioFilial"), dataAtual, dataAtual);
		
		return criteria;
	}
	
	public List findLookupAwb(Map criteria) {
		TypedFlatMap mapResult = new TypedFlatMap();
		List listResult = new ArrayList();
		
		if (ConstantesExpedicao.TP_STATUS_AWB_EMITIDO.equals(criteria.get(
				"tpStatusAwb").toString())) {
			Awb awbAux = AwbUtils.splitNrAwb(criteria.get("nrAwb").toString());
			
			if(awbAux.getNrAwb() != null){
				criteria.put("nrAwb", awbAux.getNrAwb());
				criteria.put("dsSerie", awbAux.getDsSerie());
				criteria.put("dvAwb", awbAux.getDvAwb());
			}
		}else{
			criteria.put("idAwb", criteria.get("nrAwb"));
			criteria.remove("nrAwb");
		}
			
		List awbList = awbService.findLookup(criteria);
		if (awbList != null && !awbList.isEmpty()) {
			for (int i = 0; i < awbList.size(); i++) {
				Awb awb = (Awb) awbList.get(i);
				
				if (ConstantesExpedicao.TP_STATUS_AWB_EMITIDO.equals(awb
						.getTpStatusAwb().getValue())) {
					mapResult.put("idAwb", awb.getIdAwb());
					mapResult.put("nrAwb", AwbUtils.getNrAwb(awb));
					mapResult.put("nrAwbFormatado",
							AwbUtils.getNrAwbFormated(awb));
				}else{
					mapResult.put("idAwb", awb.getIdAwb());
					mapResult.put("nrAwb", awb.getIdAwb());
					mapResult.put("nrAwbFormatado",  awb.getIdAwb());
				}
				
				listResult.add(mapResult);
			}
		}		
		
		return listResult;
	}
	
	public List findLookupSgCiaAerea(Map criteria) {
		TypedFlatMap mapResult = new TypedFlatMap();
		List listResult = new ArrayList();
		
		criteria.put("tpEmpresa", ConstantesExpedicao.TP_EMPRESA_CIA_AEREA);
		criteria.put("sgEmpresa", criteria.get("sgEmpresa").toString()
				.toUpperCase());
		
		List ciaList = empresaService.findLookupEmpresaAwb(criteria);
		if (ciaList != null && !ciaList.isEmpty()) {
			for (int i = 0; i < ciaList.size(); i++) {
				Empresa ciaAerea = (Empresa) ciaList.get(i);
				mapResult.put("idEmpresa", ciaAerea.getIdEmpresa());
				mapResult.put("sgEmpresa", ciaAerea.getSgEmpresa());
				
				listResult.add(mapResult);
			}
		}		
		
		return listResult;
	}
	
	public List findTpDoctoServico() {
		List<String> dominiosValidos = new ArrayList<String>();
		dominiosValidos.add("CTR");
		dominiosValidos.add("NFT");
		dominiosValidos.add("CRT");
		dominiosValidos.add("MDA");
		dominiosValidos.add("RRE");
		dominiosValidos.add("NTE");
		dominiosValidos.add("CTE");
		return getDomainValueService().findByDomainNameAndValues(
				"DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
	}	
	
    /**
     * FindLookup para filial do tipo de DoctoServico Escolhido.
     */ 
    public List findLookupFilialByDocumentoServico(Map criteria) {
    	List list = this.filialService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFilial", filial.getIdFilial());
    		typedFlatMap.put("sgFilial", filial.getSgFilial());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }	
	
    public List findLookupServiceDocumentFilialCTR(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }

    public List findLookupServiceDocumentFilialCRT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }

    public List findLookupServiceDocumentFilialMDA(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }

    public List findLookupServiceDocumentFilialRRE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  

    public List findLookupServiceDocumentFilialNFT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  

    public List findLookupServiceDocumentFilialNFS(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  

    public List findLookupServiceDocumentFilialNTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }

    public List findLookupServiceDocumentFilialNSE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }

    public List findLookupServiceDocumentFilialCTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
	
    public List findLookupServiceDocumentNumberCTE(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }

    public List findLookupServiceDocumentNumberNSE(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }

    public List findLookupServiceDocumentNumberNTE(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }

    public List findLookupServiceDocumentNumberNFS(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }

    public List findLookupServiceDocumentNumberCTR(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria); 
    	return findLookupDoctoServico(retorno);
    }

    public List findLookupServiceDocumentNumberCRT(Map criteria) {    	
    	List retorno = ctoInternacionalService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }    

    public List findLookupServiceDocumentNumberMDA(Map criteria) {
    	List retorno = mdaService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }

    public List findLookupServiceDocumentNumberRRE(Map criteria) {
    	List retorno = reciboReembolsoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }

    public List findLookupServiceDocumentNumberNFT(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
   
    /**
     * FindLookup para a tag DoctoServico.
     */  
    public List findLookupDoctoServico(List list) {
    	
    	if (list!=null && list.size()==1) {
    		DoctoServico doctoServico = (DoctoServico)list.remove(0);
			Filial filial = filialService.findById(doctoServico
					.getFilialByIdFilialOrigem().getIdFilial());
    		TypedFlatMap result = new TypedFlatMap();
			result.put("doctoServico.idDoctoServico",
					doctoServico.getIdDoctoServico());
			result.put("doctoServico.nrDoctoServico",
					doctoServico.getNrDoctoServico());
			result.put("doctoServico.filialByIdFilialOrigem.idFilial",
					filial.getIdFilial());
			result.put("doctoServico.filialByIdFilialOrigem.sgFilial",
					filial.getSgFilial());
    		list.add(result);
    	}    	
    	return list;    	
    }    
    
	/**
	 * Utilizado pela combo de cias aereas.
	 * 
	 * @param criteria
	 * @return
	 */
	public List findCiaAerea(TypedFlatMap criteria) {
		List retorno = new ArrayList();
		List cias = empresaService.findCiaAerea(criteria);
		for (Iterator iter = cias.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			Empresa empresa = (Empresa) iter.next();
			map.put("idEmpresa", empresa.getIdEmpresa());
			map.put("pessoa.nmPessoa", empresa.getPessoa().getNmPessoa());
			map.put("tpSituacao.value", empresa.getTpSituacao().getValue());
			retorno.add(map);
		}
		return retorno;
	}	
	
    public ResultSetPage findPaginatedRestricaoColeta(TypedFlatMap criteria) {
		ResultSetPage resultSetPage = restricaoColetaService
				.findPaginated(criteria);
    	List restricoesColeta = resultSetPage.getList();
    	List retorno = new ArrayList();
    	for (Iterator iter = restricoesColeta.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
    		RestricaoColeta restricaoColeta = (RestricaoColeta) iter.next();
			map.put("idRestricaoColeta", restricaoColeta.getIdRestricaoColeta());
			if (restricaoColeta.getServico()!=null){
				map.put("servico.idServico", restricaoColeta.getServico()
						.getIdServico());
				map.put("servico.sgServico", restricaoColeta.getServico()
						.getSgServico());
			}
			if (restricaoColeta.getPais()!=null){
				map.put("pais.nmPais", restricaoColeta.getPais().getNmPais());
			}
			if (restricaoColeta.getPsMaximoVolume()!=null){
				map.put("psMaximoVolume", restricaoColeta.getPsMaximoVolume());
			}
			if (restricaoColeta.getProdutoProibido()!=null){
				map.put("produtoProibido.dsProduto", restricaoColeta
						.getProdutoProibido().getDsProduto());
			}
			retorno.add(map);
		}
    	
    	resultSetPage.setList(retorno);
    	return resultSetPage; 
    }
    
    public Integer getRowCountRestricaoColeta(TypedFlatMap criteria) {
    	return restricaoColetaService.getRowCount(criteria);
    }
    
	/**
	 * Busca CTRs p/ Lookup
	 * 
	 * @param criteria
	 * @return
	 */
	public List findLookupCRT(TypedFlatMap criteria) {
		Long nrCrt = criteria.getLong("nrCrt");
		String sgPais = criteria.getString("sgPais");
		Long idFilialOrigem = criteria
				.getLong("filialByIdFilialOrigem.idFilial");
		
		List listCRT = ctoInternacionalService.findLookup(sgPais, nrCrt,
				idFilialOrigem);
		for (Iterator iter = listCRT.iterator(); iter.hasNext();) {
			TypedFlatMap mapCtoInternacional = (TypedFlatMap) iter.next();
			if (mapCtoInternacional.getString("tpSituacaoCrt.value")
					.equals("C")) {
				throw new BusinessException("LMS-02078");				
			}
		}
		
		return listCRT;
	}    
    
	/**
	 * Retorna o EnderecoPessoa referente ao ID do Cliente
	 */
	public Map getEnderecoPessoa(TypedFlatMap criteria) {
		TypedFlatMap tfm = null;		
		Long idCliente = criteria.getLong("idCliente");

		List listEnderecoPessoa = enderecoPessoaService
				.findEnderecoPessoaByIdPessoaByTipoEnderecoPessoa(idCliente,
						"COL", JTDateTimeUtils.getDataAtual());
		
		EnderecoPessoa enderecoPessoa = null;
		if(listEnderecoPessoa.size() == 1) {			
			enderecoPessoa = (EnderecoPessoa) listEnderecoPessoa.get(0);
		} else if(listEnderecoPessoa.size() == 0) {			
			enderecoPessoa = enderecoPessoaService
					.findEnderecoPessoaPadrao(idCliente);
		}
		
		if (enderecoPessoa!=null){
			tfm = new TypedFlatMap();
			
			TelefoneEndereco telefoneEndereco = telefoneEnderecoService
					.findTelefoneEnderecoPedidoColeta(enderecoPessoa
							.getIdEnderecoPessoa());
			if (telefoneEndereco != null){
				tfm.put("nrDdd", telefoneEndereco.getNrDdd());
				tfm.put("idTelefoneEndereco",
						telefoneEndereco.getIdTelefoneEndereco());
				tfm.put("nrTelefone", telefoneEndereco.getNrTelefone());
			}
						
			tfm.put("idEnderecoPessoa", enderecoPessoa.getIdEnderecoPessoa());
			tfm.put("dsTipoLogradouro", enderecoPessoa.getTipoLogradouro()
					.getDsTipoLogradouro());
			tfm.put("dsEndereco", enderecoPessoa.getDsEndereco());			
			tfm.put("nrEndereco", enderecoPessoa.getNrEndereco());
			tfm.put("dsComplemento", enderecoPessoa.getDsComplemento());
			tfm.put("dsBairro", enderecoPessoa.getDsBairro());
			tfm.put("nrCep", enderecoPessoa.getNrCep());
			tfm.put("municipio.idMunicipio", enderecoPessoa.getMunicipio()
					.getIdMunicipio());
			tfm.put("municipio.nmMunicipio", enderecoPessoa.getMunicipio()
					.getNmMunicipio());
			tfm.put("municipio.unidadeFederativa.sgUnidadeFederativa",
					enderecoPessoa.getMunicipio().getUnidadeFederativa()
							.getSgUnidadeFederativa());
		}
		return tfm;
	}
	
	/**
	 * Método que retorna dados complementares para coleta.
	 */
	public TypedFlatMap getDadosComplementares(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		Long idEnderecoPessoa = criteria.getLong("idEnderecoPessoa");
		Long idCliente = criteria.getLong("idCliente");
		String nrCep = criteria.getString("nrCep");
		DateTime dhColetaDisponivel = criteria
				.getDateTime("dhColetaDisponivel");
		String tipoColeta = criteria.getString("tpModoPedidoColeta");

		// Busca o horario de corte do cliente
		HorarioCorteCliente horarioCorteCliente = horarioCorteClienteService
				.findHorarioCorteClienteByIdEnderecoPessoaByHoraAtual(
						idEnderecoPessoa, JTDateTimeUtils.getHorarioAtual());
		if (horarioCorteCliente != null) {
			map.put("hrCorteCliente", horarioCorteCliente.getHrFinal());			
		}

		/*
		 * Busca dados da filial responsável.
		 * 
		 * 
		 * **** resolução do bug nº 30 ****** se o modoPedidoColeta for
		 * balcao(BA) a filial responsável será a filial do usuário logado
		 ***********************************
		 */	
		Long idFilialCliente = null;
		Filial filialSessao = SessionUtils.getFilialSessao();
		List<Map<String, Object>> filiais = new ArrayList<Map<String,Object>>();

		if (tipoColeta.equalsIgnoreCase("BA") && filialSessao != null){
			Map<String,Object> m = new HashMap<String,Object>();
			idFilialCliente = filialSessao.getIdFilial();
			m.put("idFilial", filialSessao.getIdFilial());
			m.put("sgFilial", filialSessao.getSgFilial());
			m.put("nmFantasia", filialSessao.getPessoa().getNmFantasia());
			filiais.add(m);
		} else {
			Cliente cliente = clienteService.findById(idCliente);
			EnderecoPessoa enderecoPessoa = enderecoPessoaService
					.findById(idEnderecoPessoa);
			Filial filialAtendeOperacional = cliente
					.getFilialByIdFilialAtendeOperacional();
			Long idFilial = null;
			String sgFilial = null;
			String nmFantasia = null;
	
			if (filialAtendeOperacional.getIdFilial() != null){
				idFilial = filialAtendeOperacional.getIdFilial();
				sgFilial = filialAtendeOperacional.getSgFilial();
				nmFantasia = filialAtendeOperacional.getPessoa()
						.getNmFantasia();
			} else {
				String strServicoDefaultColeta = (String) parametroGeralService
						.findConteudoByNomeParametro(
								"ID_SERVICO_DEFAULT_COLETA", false);
				Long servicoDefaultColeta = Long.valueOf(strServicoDefaultColeta);
				idFilial = ppeService.findFilialColetaMunicipio(enderecoPessoa
						.getMunicipio().getIdMunicipio(), servicoDefaultColeta,
						enderecoPessoa.getNrCep());
				Filial filial = filialService.findById(idFilial);			
				idFilial = filial.getIdFilial();
				sgFilial = filial.getSgFilial();
				nmFantasia = filial.getPessoa().getNmFantasia();
			}		

			idFilialCliente = idFilial;

			if (idFilial == filialSessao.getIdFilial()) {
				Map<String,Object> m = new HashMap<String,Object>();
				m.put("idFilial", idFilial);
				m.put("sgFilial", sgFilial);
				m.put("nmFantasia", nmFantasia);
				filiais.add(m);
			} else {
				Map<String,Object> m1 = new HashMap<String,Object>();
				Map<String,Object> m2 = new HashMap<String,Object>();
				m1.put("idFilial", idFilial);
				m1.put("sgFilial", sgFilial);
				m1.put("nmFantasia", nmFantasia);
				m2.put("idFilial", filialSessao.getIdFilial());
				m2.put("sgFilial", filialSessao.getSgFilial());
				m2.put("nmFantasia", filialSessao.getPessoa().getNmFantasia());
				filiais.add(m1);
				filiais.add(m2);
			}
		}

		map.put("filiais", filiais);

		// Busca dados da rota pelo intervalo de cep.		
		// LMS-5316 - busca a rota somente se for modo balcão, já que este não é
		// validado e a o sistema busca a rota pelo id da filial logada
		if (tipoColeta.equalsIgnoreCase("BA")) {
			buscaRotaIntervaloCep(map, idEnderecoPessoa, idCliente, nrCep,
					dhColetaDisponivel, idFilialCliente);
		}
		
		return map;
	}
	
	// LMS-5316
	public TypedFlatMap findRotaColetaEntrega(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		Long idEnderecoPessoa = criteria.getLong("idEnderecoPessoa");
		Long idCliente = criteria.getLong("idCliente");
		String nrCep = criteria.getString("nrCep");
		Long idFilial = criteria.getLong("idFilial");
		DateTime dhColetaDisponivel = criteria
				.getDateTime("dhColetaDisponivel");
		
		buscaRotaIntervaloCep(map, idEnderecoPessoa, idCliente, nrCep,
				dhColetaDisponivel, idFilial);
		
		return map;
	}
	
	// LMS-5316
	private void buscaRotaIntervaloCep(TypedFlatMap map, Long idEnderecoPessoa,
			Long idCliente, String nrCep, DateTime dhColetaDisponivel,
			Long idFilialCliente) {
		RotaIntervaloCep rotaIntervaloCep = rotaColetaEntregaService
				.findRotaAtendimentoCep(nrCep, idCliente, idEnderecoPessoa,
						idFilialCliente, // Utilizar sempre a filial do cliente
											// para buscar o intervalo de CEP
				JTDateTimeUtils.getDataAtual());
		if(rotaIntervaloCep != null) {
			RotaColetaEntrega rotaColetaEntrega = rotaIntervaloCep
					.getRotaColetaEntrega();
			map.put("idRotaIntervaloCep",
					rotaIntervaloCep.getIdRotaIntervaloCep());
			map.put("horaCorteSolicitacao",
					rotaIntervaloCep.getHrCorteSolicitacao());
			map.put("idRotaColetaEntrega",
					rotaColetaEntrega.getIdRotaColetaEntrega());
			map.put("numeroRota", rotaColetaEntrega.getNrRota());
			map.put("descricaoRota", rotaColetaEntrega.getDsRota());

			if (dhColetaDisponivel!=null){
				if (dhColetaDisponivel.getHourOfDay() > rotaIntervaloCep
						.getHrCorteSolicitacao().getHourOfDay()) {
					dhColetaDisponivel = diaUtils.getNextDiaUtil(
							dhColetaDisponivel, rotaIntervaloCep.getMunicipio()
									.getIdMunicipio());
				} else if (dhColetaDisponivel.getHourOfDay() == rotaIntervaloCep
						.getHrCorteSolicitacao().getHourOfDay()) {
					if (dhColetaDisponivel.getMinuteOfHour() > rotaIntervaloCep
							.getHrCorteSolicitacao().getMinuteOfHour()) {
						dhColetaDisponivel = diaUtils.getNextDiaUtil(
								dhColetaDisponivel, rotaIntervaloCep
										.getMunicipio().getIdMunicipio());
					}
				}
			}
			map.put("dhColetaDisponivel", dhColetaDisponivel);
		}
	}

	/**
	 * Pega os dados da sessão. 
	 */
	public TypedFlatMap getDadosSessao() {
		TypedFlatMap map = new TypedFlatMap();
		map.put("idUsuarioSessao", SessionUtils.getUsuarioLogado()
				.getIdUsuario());
		map.put("idFilialSessao", SessionUtils.getFilialSessao().getIdFilial());
		map.put("siglaSimboloMoedaSessao", SessionUtils.getMoedaSessao()
				.getSiglaSimbolo());
		map.put("dataHoraAtual", JTDateTimeUtils.getDataHoraAtual());
		map.put("dataAtual", JTDateTimeUtils.getDataAtual());
		
		String idServicoDefault = ((BigDecimal) parametroGeralService
				.findConteudoByNomeParametro("ID_SERVICO_DEFAULT_COLETA", false))
				.toString();
		map.put("idServicoDefault", idServicoDefault);
		
		Servico servicoAereo = servicoService
				.findServicoBySigla(ConstantesExpedicao.AEREO_NACIONAL_CONVENCIONAL);
		map.put("idServicoAereo", servicoAereo.getIdServico());
		
		return map;
	}
	
	/**
	 * Verifica se a hora de dhColetaDisponivel é menor que horario de corte.
	 * caso verdadeiro, somar mais um a data de dhColetaDisponivel e retornar
	 * para ser atribuido a data de previsão de coleta.
	 */
	public DateTime getDataPrevisaoColeta(TypedFlatMap criteria) {		
		DateTime dhColetaDisponivel = criteria
				.getDateTime("dhColetaDisponivel");
		TimeOfDay horarioCorte = criteria.getTimeOfDay("horarioCorte");
		
		if(dhColetaDisponivel.getHourOfDay() > horarioCorte.getHourOfDay()) {			
			dhColetaDisponivel = dhColetaDisponivel.plusDays(1);
		} else if (dhColetaDisponivel.getHourOfDay() == horarioCorte
				.getHourOfDay()) {
			if (dhColetaDisponivel.getMinuteOfHour() > horarioCorte
					.getMinuteOfHour()) {
				dhColetaDisponivel = dhColetaDisponivel.plusDays(1);
				
			}
		}
		
		return dhColetaDisponivel;
	}
	
	/**
	 * Método que pega dados de Contato e Telefone do Cliente.
	 */
	//Método poderia ir para a service.
	public TypedFlatMap getDadosCliente(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		Long idCliente = criteria.getLong("idCliente");
		
		PedidoColeta ultimoPedidoColeta = getPedidoColetaService()
				.findUltimoPedidoColetaByIdCliente(idCliente);
		if (ultimoPedidoColeta!=null){
			map.put("nomeContato", ultimoPedidoColeta.getNmContatoCliente());
		} else {
			List contatos = contatoService.findContatosByIdPessoaTpContato(
					idCliente, "CN");
			if (!contatos.isEmpty()){
				map.put("nomeContato",
						((Contato) contatos.get(0)).getNmContato());
			}
		}
		
		return map;
	}
	
	/**
	 * Busca um TelefoneEndereco para o idEnderecoPessoa enviado por parâmetro.
	 * 
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap getTelefoneEnderecoPedidoColeta(TypedFlatMap criteria){
		TypedFlatMap map = new TypedFlatMap();
		Long idEnderecoPessoa = criteria.getLong("idEnderecoPessoa");
		TelefoneEndereco telefoneEndereco = telefoneEnderecoService
				.findTelefoneEnderecoPedidoColeta(idEnderecoPessoa);
		if (telefoneEndereco!=null){
			map.put("nrDdd", telefoneEndereco.getNrDdd());
			map.put("idTelefoneEndereco",
					telefoneEndereco.getIdTelefoneEndereco());
			map.put("nrTelefone", telefoneEndereco.getNrTelefone());
		}
		return map;
	}
	
	/**
	 * Recebe o endereco em forma de TypedFlatMap e formata para mostrar no
	 * TextArea
	 * 
	 * @param tfm
	 * @return
	 */
	public TypedFlatMap formataEndereco(TypedFlatMap tfm) {
		
		String complemento = configuracoesFacade.getMensagem("complemento");
		String bairro = configuracoesFacade.getMensagem("bairro");
		String cep = configuracoesFacade.getMensagem("cep");

		String dsTipoLogradouro = tfm.getString("dsTipoLogradouro");
		String dsComplemento = tfm.getString("dsComplemento");
		String dsEndereco    = tfm.getString("dsEndereco");
		String nrEndereco    = tfm.getString("nrEndereco");
		String dsBairro      = tfm.getString("dsBairro");
		String nrCep         = tfm.getString("nrCep");
		
		StringBuffer endereco = new StringBuffer()
				.append(StringUtils.isBlank(dsTipoLogradouro) ? ""
						: dsTipoLogradouro + " ")
		.append(dsEndereco)
		.append(", nº: ")
		.append(nrEndereco)
				.append(StringUtils.isBlank(dsComplemento) ? "" : " / "
						+ complemento + ": " + dsComplemento)
				.append(StringUtils.isBlank(dsBairro) ? "" : "\n" + bairro
						+ ": " + dsBairro).append("\n" + cep + ": " + nrCep);

		TypedFlatMap map = new TypedFlatMap();
		map.put("endereco", endereco.toString());
		return map;
	}

	/**
	 * Retorna a TRUE caso não exista data de desbloqueio na tabela
	 * PROIBIDO_EMBARQUE e FALSE caso possua.
	 */
	// Método poderia ir para a service.
	public boolean getBloqueioCliente(TypedFlatMap criteria) {		
		List listProibidoEmbarques = proibidoEmbarqueService
				.findProibidoEmbarqueByIdCliente(criteria.getLong("idCliente"));
		for (Iterator iter = listProibidoEmbarques.iterator(); iter.hasNext();) {
			ProibidoEmbarque proibidoEmbarque = (ProibidoEmbarque) iter.next();
			// Se em algum registro a dtDesbloqueio é vazia é porque o cliente
			// está bloqueado por algum motivo.
			if (proibidoEmbarque.getDtDesbloqueio() == null) {
				return true;
			}	
		}
		return false;		
	}

	public Map getMunicipioDestinoBloqueado(TypedFlatMap criteria) {
		Long idMunicipio = criteria.getLong("idMunicipio");	
		Long idServico = criteria.getLong("idServico");
		Long idCliente = criteria.getLong("idCliente");
		String tpFrete = criteria.getString("tpFrete");
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

	private Map buildColetaEventualMap(Servico servico, Long idMunicipio,
			Long idCliente) {
		Map<String, Object> map = new HashMap<String, Object>();
		List atendimentos = municipioFilialService
				.findAtendimentosVigentesByMunicipioServicoOperacao(
						idMunicipio, Boolean.FALSE, servico.getIdServico(),
						JTDateTimeUtils.getDataAtual());
	
		if(!atendimentos.isEmpty()) {
			Object[] atendimento = (Object[]) atendimentos.get(0);
			
			Long idMunicipioFilial = (Long) atendimento[0];
			MunicipioFilial municipioFilial = municipioFilialService
					.findById(idMunicipioFilial);

            Long idFilial = (Long) atendimento[4];
			map.putAll(buildFilialMap(idFilial));			
			
			Municipio municipio = municipioService.findById(idMunicipio);
			map.put("sgUnidadeFederativa", municipio.getUnidadeFederativa()
					.getSgUnidadeFederativa());
			
			boolean blRecebeColetaEventual = getBlRecebeColetaEventual(servico,
					idMunicipio, servico.getIdServico(), idCliente,
					municipioFilial);
			map.put("recebeColetaEventual", blRecebeColetaEventual);
		}
		return map;
	}
	
	private Map<String, Object> buildFilialMap(Long idFilial) {
		Map<String, Object> map = new HashMap<String, Object>();
		Filial filial = filialService.findById(idFilial);
		
		map.put("filialId", idFilial);
		map.put("filialSigla", filial.getSgFilial());
		map.put("filialNome", filial.getPessoa().getNmFantasia());
		
		return map;
	}

	private void validateBloqCredEmbProib(Map map) {
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService
				.findByNomeParametro(SessionUtils.getFilialSessao()
						.getIdFilial(), "bloqCredEmbProib", false, true);

		if (conteudoParametroFilial != null
				&& "S".equalsIgnoreCase(conteudoParametroFilial
						.getVlConteudoParametroFilial())) {
			map.put("bloqCredEmbProib", true);
		 }
	}
		
	private boolean getBlRecebeColetaEventual(Servico servico,
			Long idMunicipio, Long idServico, Long idCliente,
			MunicipioFilial municipioFilial) {
		boolean blRecebeColetaEventual = municipioFilial
				.getBlRecebeColetaEventual();

		if(!blRecebeColetaEventual && idServico != null && idCliente != null){
			List le = liberacaoEmbarqueService.findLiberacaoCliente(idCliente,
					idMunicipio, servico.getTpModal());
			blRecebeColetaEventual = !le.isEmpty();
		}
		return blRecebeColetaEventual;
	}
	
	/**
	 * Método que valida a cotação. 
	 * 
	 * @param parameters
	 * @return
	 */
	public TypedFlatMap validaCotacao(TypedFlatMap parameters) {    	
		TypedFlatMap mapCotacao = new TypedFlatMap();
		Cotacao cotacao = cotacaoService
				.findCotacaoByIdFilialOrigemByNrCotacao(
						parameters.getLong("idFilialOrigem"),
						parameters.getInteger("nrCotacao"));
		getPedidoColetaService().executeValidaCotacao(cotacao,
				parameters.getLong("idCliente"),
				parameters.getLong("idMunicipio"));
		if (cotacao!=null){
			newMaster(); // Foi Comentado uma linha no
							// pedidoColetaPedidoColeta.jsp que fazia um xmit
							// para removeDetalheCotação(...)
			mapCotacao.put("idCotacao", cotacao.getIdCotacao());
			if (cotacao.getClienteByIdClienteSolicitou()!=null){
				mapCotacao.put(
						"clienteByIdClienteSolicitou.pessoa.nrIdentificacao",
						cotacao.getClienteByIdClienteSolicitou().getPessoa()
								.getNrIdentificacao());
			}
			mapCotacao.put("servico.idServico", cotacao.getServico()
					.getIdServico());
			if (cotacao.getNaturezaProduto()!=null){
				mapCotacao.put("naturezaProduto.idNaturezaProduto", cotacao
						.getNaturezaProduto().getIdNaturezaProduto());
			}
			if (cotacao.getMunicipioByIdMunicipioDestino()!=null){
				mapCotacao.put("municipioByIdMunicipioDestino.nmMunicipio",
						cotacao.getMunicipioByIdMunicipioDestino()
								.getNmMunicipio());
				mapCotacao.put("municipioByIdMunicipioDestino.idMunicipio",
						cotacao.getMunicipioByIdMunicipioDestino()
								.getIdMunicipio());
			}
			if (cotacao.getClienteByIdClienteDestino()!=null){
				mapCotacao.put(
						"clienteByIdClienteDestino.pessoa.nrIdentificacao",
						cotacao.getClienteByIdClienteDestino().getPessoa()
								.getNrIdentificacao());
				mapCotacao.put("clienteByIdClienteDestino.pessoa.nmPessoa",
						cotacao.getClienteByIdClienteDestino().getPessoa()
								.getNmPessoa());
			}
			mapCotacao.put("nmClienteDestino", cotacao.getNmClienteDestino());
			mapCotacao.put("dsContato", cotacao.getDsContato());
			mapCotacao.put("moeda.idMoeda", cotacao.getMoeda().getIdMoeda());
			mapCotacao.put("vlMercadoria", cotacao.getVlMercadoria());
			mapCotacao.put("psReal", cotacao.getPsReal());
			mapCotacao.put("psAforado", cotacao.getPsCubado());
			mapCotacao.put("tpFrete.value", cotacao.getTpFrete().getValue());
			
		}
		return mapCotacao;
	}
	
	/**
	 * Método que busca os dados da Cotação a partir do ID.
	 */
	public TypedFlatMap getDadosCotacao(TypedFlatMap criteria) {
		Cotacao cotacao = cotacaoService.findCotacaoByIdCotacao(criteria
				.getLong("idCotacao"));
		TypedFlatMap mapCotacao = new TypedFlatMap();
		
		mapCotacao.put("filialByIdFilialOrigem.idFilial", cotacao
				.getFilialByIdFilialOrigem().getIdFilial());
		mapCotacao.put("filialByIdFilialOrigem.sgFilial", cotacao
				.getFilialByIdFilialOrigem().getSgFilial());
		mapCotacao.put("nrCotacao", cotacao.getNrCotacao());
		
		return mapCotacao;
	}	
	
    /**
     * Método que verifica se o Serviço é Internacional.
	 * 
     * @param criteria
     * @return
     */
    public TypedFlatMap verificaServicoInternacional(TypedFlatMap criteria) {
    	TypedFlatMap mapResult = new TypedFlatMap();
    	
		Servico servico = servicoService
				.findById(criteria.getLong("idServico"));
    	if (servico.getTpAbrangencia().getValue().equals("I")) {
    		mapResult.put("blServicoInternacional", Boolean.TRUE);
		}
    	
    	return mapResult;
    }
    
	/**
	 * ####################################### # Inicio dos métodos para tela de
	 * DF2 # #######################################
	 */
	
	public TypedFlatMap store(TypedFlatMap tfmBean) {
		// Salva Pedido de Coleta, os itens de Detalhe Coleta e os Serviços
		// Adicionais de Coleta em DF2
		MasterEntry entry = getMasterFromSession(
				tfmBean.getLong("idPedidoColeta"), true);
		PedidoColeta pedidoColeta = (PedidoColeta) entry.getMaster();
		
		// Pega os filhos da sessão
		ItemList items = getItemsFromSession(entry, "detalheColeta");
		if (items.size() == 0){
			throw new BusinessException("LMS-02008");
		}
		ItemListConfig config = getMasterConfig().getItemListConfig(
				"detalheColeta");
		
		Cliente cliente = clienteService.findById(tfmBean
				.getLong("cliente.idCliente"));
		
		pedidoColeta.setIdPedidoColeta(tfmBean.getLong("idPedidoColeta"));
		pedidoColeta.setNrColeta(tfmBean.getLong("nrColeta"));
		pedidoColeta.setDhPedidoColeta(JTDateTimeUtils.getDataHoraAtual());
		
		pedidoColeta
				.setTpStatusColeta(tfmBean.getDomainValue("tpStatusColeta"));
		pedidoColeta.setTpModoPedidoColeta(tfmBean
				.getDomainValue("tpModoPedidoColeta"));
		if (pedidoColeta.getTpModoPedidoColeta().getValue().equals("BA")) {			
			pedidoColeta.setTpStatusColeta(new DomainValue("EX"));
		}
		
		pedidoColeta.setBlClienteLiberadoManual(tfmBean
				.getBoolean("blClienteLiberadoManual"));
		pedidoColeta.setBlAlteradoPosProgramacao(tfmBean
				.getBoolean("blAlteradoPosProgramacao"));
		pedidoColeta.setNmContatoCliente(tfmBean.getString("nmContatoCliente"));
		pedidoColeta.setNmSolicitante(tfmBean.getString("nmSolicitante"));
		
		if (tfmBean.getLong("telefoneEndereco.idTelefoneEndereco") == null){
			//Telefone novo
			pedidoColeta
					.setNrDddCliente(tfmBean.getString("nrDddTelefoneNovo"));
			pedidoColeta.setNrTelefoneCliente(tfmBean
					.getString("nrTelefoneNovo"));
			
			DomainValue domainValueTpTelefone;
			if (cliente.getPessoa().getTpPessoa().getValue().equals("J")) {
				domainValueTpTelefone = new DomainValue("C");
		} else {
				domainValueTpTelefone = new DomainValue("R");			
			}
			TelefoneEndereco telefoneEndereco = new TelefoneEndereco();
			telefoneEndereco.setEnderecoPessoa(enderecoPessoaService
					.findByIdFetchTipoEnderecoPessoas(tfmBean
							.getLong("idEnderecoPessoa")));
			telefoneEndereco.setNrDdd(tfmBean.getString("nrDddTelefoneNovo"));
			telefoneEndereco.setNrTelefone(tfmBean.getString("nrTelefoneNovo"));
			telefoneEndereco.setPessoa(cliente.getPessoa());
			telefoneEndereco.setTpUso(new DomainValue("FO"));
			telefoneEndereco.setTpTelefone(domainValueTpTelefone);
			telefoneEnderecoService.store(telefoneEndereco);
		} else {
			pedidoColeta.setNrDddCliente(tfmBean.getString("nrDddCliente"));
			pedidoColeta.setNrTelefoneCliente(tfmBean
					.getString("telefoneEndereco.nrTelefone"));
		}
		
		YearMonthDay dataPrevisaoColeta = tfmBean
				.getYearMonthDay("dtPrevisaoColeta");
		
		if (JTDateTimeUtils.comparaData(dataPrevisaoColeta,
				JTDateTimeUtils.getDataAtual()) < 0) {
			throw new BusinessException("LMS-02111");
		}
		pedidoColeta.setDtPrevisaoColeta(dataPrevisaoColeta);
		
		pedidoColeta.setObPedidoColeta(tfmBean.getString("obPedidoColeta"));
		pedidoColeta
				.setTpPedidoColeta(tfmBean.getDomainValue("tpPedidoColeta"));
		pedidoColeta.setDhColetaDisponivel(tfmBean
				.getDateTime("dhColetaDisponivel"));
		pedidoColeta.setHrLimiteColeta(tfmBean.getTimeOfDay("hrLimiteColeta"));
		pedidoColeta.setEdColeta(tfmBean.getString("dsTipoLogradouro") + " "
				+ tfmBean.getString("edColeta"));
		
		if(tfmBean.getString("nrEndereco") != null) {		
			pedidoColeta.setNrEndereco(tfmBean.getString("nrEndereco").trim());
		}
		
		pedidoColeta.setDsComplementoEndereco(tfmBean
				.getString("dsComplementoEndereco"));
		pedidoColeta.setDsBairro(tfmBean.getString("dsBairro"));
		pedidoColeta.setNrCep(tfmBean.getString("nrCep"));
		
		pedidoColeta.setVlTotalInformado(tfmBean
				.getBigDecimal("vlTotalInformado"));
		if(tfmBean.getBigDecimal("vlTotalInformado") == null) {		
			pedidoColeta.setVlTotalInformado(BigDecimalUtils.ZERO);
		}
		pedidoColeta.setVlTotalVerificado(tfmBean
				.getBigDecimal("vlTotalVerificado"));
		if(tfmBean.getBigDecimal("vlTotalVerificado") == null) {
			pedidoColeta.setVlTotalVerificado(BigDecimalUtils.ZERO);
		}
		pedidoColeta.setQtTotalVolumesInformado(tfmBean
				.getInteger("qtTotalVolumesInformado"));
		pedidoColeta.setQtTotalVolumesVerificado(tfmBean
				.getInteger("qtTotalVolumesVerificado"));
		pedidoColeta.setPsTotalInformado(tfmBean
				.getBigDecimal("psTotalInformado"));
		if(tfmBean.getBigDecimal("psTotalInformado") == null) {
			pedidoColeta.setPsTotalInformado(BigDecimalUtils.ZERO);
		}
		pedidoColeta.setPsTotalVerificado(tfmBean
				.getBigDecimal("psTotalVerificado"));
		if(tfmBean.getBigDecimal("psTotalVerificado") == null) {
			pedidoColeta.setPsTotalVerificado(BigDecimalUtils.ZERO);
		}
		pedidoColeta.setPsTotalAforadoInformado(tfmBean
				.getBigDecimal("psTotalAforadoInformado"));
		if(tfmBean.getBigDecimal("psTotalAforadoInformado") == null) {
			pedidoColeta.setPsTotalAforadoInformado(BigDecimalUtils.ZERO);
		}	
		pedidoColeta.setPsTotalAforadoVerificado(tfmBean
				.getBigDecimal("psTotalAforadoVerificado"));
		if(tfmBean.getBigDecimal("psTotalAforadoVerificado") == null) {
			pedidoColeta.setPsTotalAforadoVerificado(BigDecimalUtils.ZERO);
		}			
		
		pedidoColeta.setCliente(cliente);
		pedidoColeta.setEnderecoPessoa(enderecoPessoaService.findById(tfmBean
				.getLong("enderecoPessoa.idEnderecoPessoa")));
		pedidoColeta.setMunicipio(municipioService.findById(tfmBean
				.getLong("municipio.idMunicipio")));
		pedidoColeta.setUsuario(usuarioService.findById(tfmBean
				.getLong("usuario.idUsuario")));
		pedidoColeta.setFilialByIdFilialSolicitante(filialService
				.findById(tfmBean
						.getLong("filialByIdFilialSolicitante.idFilial")));
		pedidoColeta.setFilialByIdFilialResponsavel(filialService
				.findById(tfmBean
						.getLong("filialByIdFilialResponsavel.idFilial")));
		pedidoColeta.setMoeda(moedaService.findById(tfmBean
				.getLong("moeda.idMoeda")));
		
		pedidoColeta.setBlProdutoDiferenciado(tfmBean
				.getBoolean("blProdutoDiferenciado"));
		pedidoColeta.setDsInfColeta(tfmBean.getString("dsInfColeta"));
		
		if (tfmBean.getLong("cotacao.idCotacao") != null) {
			pedidoColeta.setCotacao(cotacaoService.findById(tfmBean
					.getLong("cotacao.idCotacao")));
		}

		if (tfmBean.getLong("rotaIntervaloCep.idRotaIntervaloCep") != null) {
			pedidoColeta.setRotaIntervaloCep(rotaIntervaloCepService
					.findById(tfmBean
							.getLong("rotaIntervaloCep.idRotaIntervaloCep")));
		} else {
			if (!"BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())){
			throw new BusinessException("LMS-02011");
		}
		
		}
		
		if (tfmBean.getLong("rotaColetaEntrega.idRotaColetaEntrega") != null) {
			pedidoColeta.setRotaColetaEntrega(rotaColetaEntregaService
					.findById(tfmBean
							.getLong("rotaColetaEntrega.idRotaColetaEntrega")));
		} else {
			if (!"BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())){
			throw new BusinessException("LMS-02011");
		}		
		}
		
		// Salva o registro pai e seus filhos.
		TypedFlatMap registrosSalvos = this.getPedidoColetaService().storeAll(
				pedidoColeta, tfmBean, items, config);
		if(registrosSalvos == null){
			tfmBean.put("AlertaProdutoDiferenciado", false);
			return tfmBean;
		}else{
			registrosSalvos.put("AlertaProdutoDiferenciado", true);
			items.resetItemsState(); 
    		updateMasterInSession(entry);
		}
		return registrosSalvos;
	}	

	/**
	 * Salva a referencia do objeto Master na sessão. Não devem ser
	 * inicializadas as coleções que representam os filhos já que o usuário pode
	 * vir a não utilizar a aba de filhos, evitando assim a carga desnecessária
	 * de objetos na sessão e a partir do banco de dados.
	 */
    public Object findById(java.lang.Long id) {
		Object masterObj = getPedidoColetaService().findById(id);
		putMasterInSession(masterObj); 
		return masterObj;
    }
    
    /**
     * Remoção de um conjunto de registros Master.
     */
    public void removeByIds(List ids) {
    	getPedidoColetaService().removeByIds(ids);
    }

    /**
     * Remoção de um registro Master.
     */    
    public void removeById(java.lang.Long id) {
        getPedidoColetaService().removeById(id);
		newMaster();
    }     
    
    /**
     * Salva um item Descrição Padrão na sessão.
     */
    public Serializable saveDetalheColeta(TypedFlatMap parameters) {
    	
    	this.validateTpPedidoColeta(parameters);
    	Serializable s = null;
    	
		if (TP_PEDIDO_COLETA_AEREO.equals(parameters
				.getString("tpPedidoColeta"))) {
    		Long idAwb = parameters.getLong("awb.idAwb");
        	Boolean hasAwb = idAwb != null ? Boolean.TRUE : Boolean.FALSE;
        	Long idDoctoServico = this.getParametersDoctoServico(parameters);
    	
			List<Long> idsConh = getPedidoColetaService()
					.findIdsConhecimentoByAwb(idAwb, idDoctoServico);
    	    	
        	if(parameters.getBoolean("inclusaoParcial")){
        		idsConh = findDocumentosSemColeta(parameters, idsConh);
        	}
        	
        	for (Long idConhecimento : idsConh) {
				this.validateAwb(hasAwb, idConhecimento,
						parameters.getLong("idPedidoColeta"),
						parameters.getLong("idDetalheColeta"));
            	
            	Conhecimento c = conhecimentoService.findById(idConhecimento);
            	if(hasAwb) {
					parameters.put("doctoServico.idDoctoServico",
							idConhecimento);
					parameters.put(
							"naturezaProduto.idNaturezaProduto",
							naturezaProdutoService.findByIdConhecimento(
									idConhecimento).getIdNaturezaProduto());
    	        	parameters.put("psMercadoria", c.getPsReal());
    	        	parameters.put("psAforado", c.getPsAforado());
    	        	parameters.put("vlMercadoria", c.getVlMercadoria());
    	        	parameters.put("qtVolumes", c.getQtVolumes());
            	}
            	
				detalheColetaService.validateEntregaDiretaCliente(c,
						parameters.getBoolean("blEntregaDireta"));
            	s =  saveItemInstance(parameters, "detalheColeta");
    		}
		} else {
    		s = prepareNotaFiscal(parameters);
    	}
    	
		return s; 
    }

	private Serializable prepareNotaFiscal(TypedFlatMap parameters) {
		List notasFiscais = parameters.getList("notaFiscalColetas");
		List chavesNFe = parameters.getList("nrChaveNfe");

		/*
		 * Remove o map para que ao salvar detalhe ele seja refeito de acordo
		 * com a tela, e suas modificações
		 */
		parameters.remove("notaFiscalColetas");

		if (notasFiscais != null) {
			List notasFiscalColetas = new ArrayList();

			for (int i = 0; i < notasFiscais.size(); i++) {
				TypedFlatMap notaFiscal = (TypedFlatMap) notasFiscais.get(i);
				TypedFlatMap mapNotaFiscalColeta = new TypedFlatMap();
				String nrNotaFiscal = notaFiscal.getString("nrNotaFiscal");
				boolean hasChave = false;

				if (chavesNFe != null) {
					hasChave = prepareChavesNFe(chavesNFe, notasFiscalColetas,
							mapNotaFiscalColeta, nrNotaFiscal);
				}
				
				if (!hasChave) {
					mapNotaFiscalColeta.put("nrNotaFiscal", nrNotaFiscal);
					notasFiscalColetas.add(mapNotaFiscalColeta);
				}
			}
			parameters.put("notaFiscalColetas", notasFiscalColetas);
		}
		
		return saveItemInstance(parameters, "detalheColeta");
	}

	private Boolean prepareChavesNFe(List chavesNFe, List notasFiscalColetas,
			TypedFlatMap mapNotaFiscalColeta, String nrNotaFiscal) {
		Boolean hasChave = Boolean.FALSE; 
					for (int j = 0; j < chavesNFe.size(); j++) {
						TypedFlatMap chaveNFe = (TypedFlatMap) chavesNFe.get(j);
						if (chaveNFe.getString("nrChave").length() == 44) {
				Long nrNotaFromChaveNFe = Long.valueOf(chaveNFe.getString(
						"nrChave").substring(25, 34));

							if (Long.valueOf(nrNotaFiscal).equals(nrNotaFromChaveNFe)) {
					getChaveNfe(mapNotaFiscalColeta, chaveNFe);

								hasChave = true;
					mapNotaFiscalColeta.put("nrNotaFiscal", nrNotaFiscal);
					mapNotaFiscalColeta.put("nrChave",
							chaveNFe.getString("nrChave"));
								notasFiscalColetas.add(mapNotaFiscalColeta);
								break;
							}
						}
					}
		return hasChave;
				}

	private void getChaveNfe(TypedFlatMap mapNotaFiscalColeta,
			TypedFlatMap chaveNFe) {
		if (chaveNFe.getLong("idNotaFiscalColeta") != null
				&& chaveNFe.getLong("idNotaFiscalColeta") > 0) {
			mapNotaFiscalColeta.put("idNotaFiscalColeta",
					chaveNFe.getLong("idNotaFiscalColeta"));
		} else {
			mapNotaFiscalColeta.put("idNotaFiscalColeta", null);
				}
			}

	private void validateAwb(Boolean hasAwb, Long idDoctoServico,
			Long idPedidoColeta, Long idDetalheColeta) {
    	MasterEntry entry = getMasterFromSession(idPedidoColeta, true);
		PedidoColeta pedidoColeta = (PedidoColeta) entry.getMaster();
		ItemList items = getItemsFromSession(entry, "detalheColeta");    	
		this.getPedidoColetaService().validateAwb(pedidoColeta, items,
				getMasterConfig().getItemListConfig("detalheColeta"), hasAwb,
				idDoctoServico, idDetalheColeta);
	}

	private Long getParametersDoctoServico(TypedFlatMap parameters) {
		Long idDoctoServico = null;
		if (parameters.getLong("doctoServico.idDoctoServico") != null) {
			idDoctoServico = parameters.getLong("doctoServico.idDoctoServico");
			TypedFlatMap doctoServico = new TypedFlatMap();
			doctoServico.put("idDoctoServico", idDoctoServico);			
			parameters.put("doctoServico", doctoServico);
		}
		return idDoctoServico;
	}

	private void validateTpPedidoColeta(TypedFlatMap parameters) {
		if (TP_PEDIDO_COLETA_AEREO.equals(parameters
				.getString("tpPedidoColeta"))) {
    		int qtValues = (parameters.getLong("awb.idAwb") == null ? 0 : 1)
					+ (parameters.getLong("doctoServico.idDoctoServico") == null ? 0
							: 1);
    		
			if (qtValues == 0) {
				throw new BusinessException("LMS-02105");
			}else if(qtValues > 1){
				throw new BusinessException("LMS-02106");
			}
		}		
	}

	public ResultSetPage findPaginatedDetalheColeta(Map parameters) { 
		ResultSetPage rspDetalheColeta = findPaginatedItemList(parameters,
				"detalheColeta");
    	
    	List listDetalheColeta = new ArrayList();
    	for(int i=0; i< rspDetalheColeta.getList().size(); i++) {
			DetalheColeta detalheColeta = (DetalheColeta) rspDetalheColeta
					.getList().get(i);
    		TypedFlatMap mapDetalheColeta = new TypedFlatMap();
    		
			mapDetalheColeta.put("idDetalheColeta",
					detalheColeta.getIdDetalheColeta());
			mapDetalheColeta.put("tpFrete.description", detalheColeta
					.getTpFrete().getDescription());
			mapDetalheColeta.put("tpFrete.value", detalheColeta.getTpFrete()
					.getValue());
			mapDetalheColeta.put("tpFrete.status", detalheColeta.getTpFrete()
					.getStatus());
    		mapDetalheColeta.put("qtVolumes", detalheColeta.getQtVolumes());
			mapDetalheColeta.put("vlMercadoria",
					detalheColeta.getVlMercadoria());
			mapDetalheColeta.put("psMercadoria",
					detalheColeta.getPsMercadoria());
    		mapDetalheColeta.put("psAforado", detalheColeta.getPsAforado());
			mapDetalheColeta.put("obDetalheColeta",
					detalheColeta.getObDetalheColeta());
    		mapDetalheColeta.put("origem", detalheColeta.getOrigem());    	
			mapDetalheColeta.put("blEntregaDireta",
					detalheColeta.getBlEntregaDireta());
    		if (detalheColeta.getServico() != null) {
				mapDetalheColeta.put("servico.idServico", detalheColeta
						.getServico().getIdServico());
				mapDetalheColeta.put("servico.dsServico", detalheColeta
						.getServico().getDsServico());
			}
    		if (detalheColeta.getNaturezaProduto() != null) {
				mapDetalheColeta.put("naturezaProduto.idNaturezaProduto",
						detalheColeta.getNaturezaProduto()
								.getIdNaturezaProduto());
				mapDetalheColeta.put("naturezaProduto.dsNaturezaProduto",
						detalheColeta.getNaturezaProduto()
								.getDsNaturezaProduto());
			}
    		if (detalheColeta.getLocalidadeEspecial() != null) {
				mapDetalheColeta.put("localidadeEspecial.idLocalidadeEspecial",
						detalheColeta.getLocalidadeEspecial()
								.getIdLocalidadeEspecial());
				mapDetalheColeta
						.put("localidadeEspecial.dsLocalidade", detalheColeta
								.getLocalidadeEspecial().getDsLocalidade());
			}
    		if (detalheColeta.getMunicipio() != null) {
				mapDetalheColeta.put("municipio.idMunicipio", detalheColeta
						.getMunicipio().getIdMunicipio());
				mapDetalheColeta.put("municipio.nmMunicipio", detalheColeta
						.getMunicipio().getNmMunicipio());
				mapDetalheColeta.put(
						"municipio.unidadeFederativa.sgUnidadeFederativa",
						detalheColeta.getMunicipio().getUnidadeFederativa()
								.getSgUnidadeFederativa());
			}
    		if (detalheColeta.getFilial() != null) {
				mapDetalheColeta.put("filial.idFilial", detalheColeta
						.getFilial().getIdFilial());
				mapDetalheColeta.put("filial.sgFilial", detalheColeta
						.getFilial().getSgFilial());
				mapDetalheColeta.put("filial.pessoa.nmFantasia", detalheColeta
						.getFilial().getPessoa().getNmFantasia());
			}
    		if (detalheColeta.getCliente() != null) {
				mapDetalheColeta.put("cliente.idCliente", detalheColeta
						.getCliente().getIdCliente());
				mapDetalheColeta.put("cliente.pessoa.nrIdentificacao",
						detalheColeta.getCliente().getPessoa()
								.getNrIdentificacao());
				mapDetalheColeta.put("nmDestinatario", detalheColeta
						.getCliente().getPessoa().getNmPessoa());
			} else if (detalheColeta.getNmDestinatario() != null) {
				mapDetalheColeta.put("nmDestinatario",
						detalheColeta.getNmDestinatario());
			}    		
    		if (detalheColeta.getMoeda() != null) {
				mapDetalheColeta.put("moeda.idMoeda", detalheColeta.getMoeda()
						.getIdMoeda());
				mapDetalheColeta.put("moeda.dsSimbolo", detalheColeta
						.getMoeda().getDsSimbolo());
				mapDetalheColeta.put("moeda.sgMoeda", detalheColeta.getMoeda()
						.getSgMoeda());
    		}
    		if (detalheColeta.getCotacao() != null) {
				mapDetalheColeta.put("cotacao.idCotacao", detalheColeta
						.getCotacao().getIdCotacao());
    		}
    		
    		getDoctoServicoByDetalheColeta(detalheColeta, mapDetalheColeta);
    		
    		if (detalheColeta.getCtoInternacional() != null) {
				mapDetalheColeta
						.put("ctoInternacional.idDoctoServico", detalheColeta
								.getCtoInternacional().getIdDoctoServico());
				mapDetalheColeta.put("ctoInternacional.sgPais", detalheColeta
						.getCtoInternacional().getSgPais());
				mapDetalheColeta.put("ctoInternacional.nrCrt", detalheColeta
						.getCtoInternacional().getNrCrt());
    		}
    		
    		if(detalheColeta.getDoctoServico() != null) {
				mapDetalheColeta.put("awb", awbService
						.findPreAwbAwbByIdDoctoServico(detalheColeta
								.getDoctoServico().getIdDoctoServico()));
    		}
    		
    		List listNotaFiscalColetas = new ArrayList();
    		for(int j=0; j< detalheColeta.getNotaFiscalColetas().size(); j++) {
				NotaFiscalColeta notaFiscalColeta = (NotaFiscalColeta) detalheColeta
						.getNotaFiscalColetas().get(j);
    			TypedFlatMap mapNotaFiscalColeta = new TypedFlatMap();
    			
				mapNotaFiscalColeta.put("idNotaFiscalColeta",
						notaFiscalColeta.getIdNotaFiscalColeta());
				mapNotaFiscalColeta.put("nrNotaFiscal",
						notaFiscalColeta.getNrNotaFiscal());
    			
    			listNotaFiscalColetas.add(mapNotaFiscalColeta);
    		}
    		
    		mapDetalheColeta.put("notaFiscalColetas", listNotaFiscalColetas);
    		
    		listDetalheColeta.add(mapDetalheColeta);
    	}
    	
    	rspDetalheColeta.setList(listDetalheColeta);
    	
    	return rspDetalheColeta;    	
    }

	private void getDoctoServicoByDetalheColeta(DetalheColeta detalheColeta,
			TypedFlatMap mapDetalheColeta) {
		if(detalheColeta.getDoctoServico()!=null){
			mapDetalheColeta.put("doctoServico.idDoctoServico", detalheColeta
					.getDoctoServico().getIdDoctoServico());
			mapDetalheColeta.put("doctoServico.tpDoctoSgFilial", detalheColeta
					.getDoctoServico().getTpDocumentoServico().getValue()
					+ " "
					+ detalheColeta.getDoctoServico()
							.getFilialByIdFilialOrigem().getSgFilial());
			mapDetalheColeta.put("doctoServico.nrDoctoServico", detalheColeta
					.getDoctoServico().getNrDoctoServico());
		}
	}

    public Integer getRowCountDetalheColeta(Map parameters){
    	return getRowCountItemList(parameters, "detalheColeta");
    }    
    
    public Object findByIdDetalheColeta(MasterDetailKey key) {
		DetalheColeta detalheColeta = (DetalheColeta) findItemById(key,
				"detalheColeta");
		TypedFlatMap mapDetalheColeta = new TypedFlatMap();    		
		
		mapDetalheColeta.put("idDetalheColeta",
				detalheColeta.getIdDetalheColeta());
		mapDetalheColeta.put("tpFrete.description", detalheColeta.getTpFrete()
				.getDescription());
		mapDetalheColeta.put("tpFrete.value", detalheColeta.getTpFrete()
				.getValue());
		mapDetalheColeta.put("tpFrete.status", detalheColeta.getTpFrete()
				.getStatus());
		mapDetalheColeta.put("qtVolumes", detalheColeta.getQtVolumes());
		mapDetalheColeta.put("vlMercadoria", detalheColeta.getVlMercadoria());
		mapDetalheColeta.put("psMercadoria", detalheColeta.getPsMercadoria());
		mapDetalheColeta.put("psAforado", detalheColeta.getPsAforado());
		mapDetalheColeta.put("obDetalheColeta",
				detalheColeta.getObDetalheColeta());
		mapDetalheColeta.put("origem", detalheColeta.getOrigem());
		mapDetalheColeta.put("blEntregaDireta",
				detalheColeta.getBlEntregaDireta());
		if (detalheColeta.getServico() != null) {
			mapDetalheColeta.put("servico.idServico", detalheColeta
					.getServico().getIdServico());
			mapDetalheColeta.put("servico.dsServico", detalheColeta
					.getServico().getDsServico());
		}
		if (detalheColeta.getNaturezaProduto() != null) {
			mapDetalheColeta.put("naturezaProduto.idNaturezaProduto",
					detalheColeta.getNaturezaProduto().getIdNaturezaProduto());
			mapDetalheColeta.put("naturezaProduto.dsNaturezaProduto",
					detalheColeta.getNaturezaProduto().getDsNaturezaProduto());
		}
		if (detalheColeta.getLocalidadeEspecial() != null) {
			mapDetalheColeta.put("localidadeEspecial.idLocalidadeEspecial",
					detalheColeta.getLocalidadeEspecial()
							.getIdLocalidadeEspecial());
			mapDetalheColeta.put("localidadeEspecial.dsLocalidade",
					detalheColeta.getLocalidadeEspecial().getDsLocalidade());
		}
		if (detalheColeta.getMunicipio() != null) {
			mapDetalheColeta.put("municipio.idMunicipio", detalheColeta
					.getMunicipio().getIdMunicipio());
			mapDetalheColeta.put("municipioFilial.municipio.idMunicipio",
					detalheColeta.getMunicipio().getIdMunicipio());
			mapDetalheColeta.put("municipioFilial.municipio.nmMunicipio",
					detalheColeta.getMunicipio().getNmMunicipio());
			mapDetalheColeta
					.put("municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa",
							detalheColeta.getMunicipio().getUnidadeFederativa()
									.getSgUnidadeFederativa());
		}
		if (detalheColeta.getFilial() != null) {
			mapDetalheColeta.put("filial.idFilial", detalheColeta.getFilial()
					.getIdFilial());
			mapDetalheColeta.put("filial.sgFilial", detalheColeta.getFilial()
					.getSgFilial());
			mapDetalheColeta.put("filial.pessoa.nmFantasia", detalheColeta
					.getFilial().getPessoa().getNmFantasia());
		}
		if (detalheColeta.getCliente() != null) {
			mapDetalheColeta.put("cliente.idCliente", detalheColeta
					.getCliente().getIdCliente());
			mapDetalheColeta
					.put("cliente.pessoa.nrIdentificacao", detalheColeta
							.getCliente().getPessoa().getNrIdentificacao());
			mapDetalheColeta.put(
					"cliente.pessoa.nrIdentificacaoFormatado",
					FormatUtils.formatIdentificacao(detalheColeta.getCliente()
							.getPessoa().getTpIdentificacao(), detalheColeta
							.getCliente().getPessoa().getNrIdentificacao()));
			mapDetalheColeta.put("nmDestinatario", detalheColeta.getCliente()
					.getPessoa().getNmPessoa());
		} else if (detalheColeta.getNmDestinatario() != null) {
			mapDetalheColeta.put("nmDestinatario",
					detalheColeta.getNmDestinatario());
		}
		if (detalheColeta.getMoeda() != null) {
			mapDetalheColeta.put("moeda.idMoeda", detalheColeta.getMoeda()
					.getIdMoeda());
			mapDetalheColeta.put("moeda.dsSimbolo", detalheColeta.getMoeda()
					.getDsSimbolo());
			mapDetalheColeta.put("moeda.sgMoeda", detalheColeta.getMoeda()
					.getSgMoeda());
		}
		if (detalheColeta.getCotacao() != null) {
			mapDetalheColeta.put("cotacao.idCotacao", detalheColeta
					.getCotacao().getIdCotacao());
		}
		if (detalheColeta.getCtoInternacional() != null) {
			mapDetalheColeta.put("ctoInternacional.idDoctoServico",
					detalheColeta.getCtoInternacional().getIdDoctoServico());
			mapDetalheColeta.put("ctoInternacional.sgPais", detalheColeta
					.getCtoInternacional().getSgPais());
			mapDetalheColeta.put("ctoInternacional.nrCrt", detalheColeta
					.getCtoInternacional().getNrCrt());
		} 		
		
		List listNFe = new ArrayList();
		List listNotaFiscalColetas = new ArrayList();
		for(int j=0; j<detalheColeta.getNotaFiscalColetas().size(); j++) {
			NotaFiscalColeta notaFiscalColeta = (NotaFiscalColeta) detalheColeta
					.getNotaFiscalColetas().get(j);
			TypedFlatMap mapNotaFiscalColeta = new TypedFlatMap();
			
			if (notaFiscalColeta.getNrNotaFiscal() != null
					|| notaFiscalColeta.getNrChave() != null) {
				mapNotaFiscalColeta.put("idNotaFiscalColeta",
						notaFiscalColeta.getIdNotaFiscalColeta());
			}

			if (notaFiscalColeta.getNrNotaFiscal() != null) {
				mapNotaFiscalColeta.put("nrNotaFiscal",
						notaFiscalColeta.getNrNotaFiscal());
			listNotaFiscalColetas.add(mapNotaFiscalColeta);
		}
		
			if (notaFiscalColeta.getNrChave() != null) {
				mapNotaFiscalColeta.put("nrChave",
						notaFiscalColeta.getNrChave());
				listNFe.add(mapNotaFiscalColeta);
			}
			
		}
		
		if(detalheColeta.getDoctoServico() != null ) {
			DoctoServico ds = detalheColeta.getDoctoServico();
			mapDetalheColeta.put("doctoServico.idDoctoServico",
					ds.getIdDoctoServico());
			mapDetalheColeta.put("doctoServico.nrDoctoServico",
					ds.getNrDoctoServico());
			mapDetalheColeta.put("doctoServico.tpDocumentoServico", ds
					.getTpDocumentoServico().getValue());
			mapDetalheColeta.put(
					"doctoServico.filialByIdFilialOrigem.idFilial", ds
							.getFilialByIdFilialOrigem().getIdFilial());
			mapDetalheColeta.put(
					"doctoServico.filialByIdFilialOrigem.sgFilial", ds
							.getFilialByIdFilialOrigem().getSgFilial());
    		
		}
		
		mapDetalheColeta.put("notaFiscalColetas", listNotaFiscalColetas);
		mapDetalheColeta.put("nrChaveNfe", listNFe);
		
		return mapDetalheColeta;
    }

    /***
     * Remove uma lista de registros items.
	 *  
	 * @param ids
	 *            ids dos desciçoes item a serem removidos.
	 * 
	 * 
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsDetalheColeta(List ids) {
    	super.removeItemByIds(ids, "detalheColeta");    	
    }   
	
	protected MasterEntryConfig createMasterConfig(
			MasterDetailFactory masterFactory) {
		
		/**
		 * Declaracao da classe pai
		 */	
		MasterEntryConfig config = masterFactory
				.createMasterEntryConfig(PedidoColeta.class);

		/**
		 * Esta classe e reponsavel por ordenar a List dos filhos que estao em
		 * memoria de acordo com as regras de negocio
		 */
		Comparator descComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				DetalheColeta detalheColeta1 = (DetalheColeta) obj1;
				DetalheColeta detalheColeta2 = (DetalheColeta) obj2;
				
				int retorno = 0;
				
				retorno = detalheColeta1
						.getServico()
						.getDsServico()
						.toString()
						.compareTo(
								detalheColeta2.getServico().getDsServico()
										.toString());
				
				if (detalheColeta1.getDoctoServico() != null
						&& detalheColeta2.getDoctoServico() != null) {
					detalheColeta1.setAwb(awbService
							.findUltimoAwbByDoctoServico(detalheColeta1
									.getDoctoServico().getNrDoctoServico()));
					detalheColeta2.setAwb(awbService
							.findUltimoAwbByDoctoServico(detalheColeta2
									.getDoctoServico().getNrDoctoServico()));
					if (retorno == 0 && detalheColeta1.getAwb() != null
							&& detalheColeta2.getAwb() != null) {
						retorno = detalheColeta1.getAwb().getNrAwb()
								.compareTo(detalheColeta2.getAwb().getNrAwb());
					}
					if(retorno ==0){
						retorno = detalheColeta1
								.getDoctoServico()
								.getNrDoctoServico()
								.compareTo(
										detalheColeta2.getDoctoServico()
												.getNrDoctoServico());
					}
				}
								
				return retorno;
			}
		}; 	
    	
    	/**
		 * Esta instancia é responsavel por carregar os items filhos na sessão a
		 * partir do banco de dados.
    	 */
    	ItemListConfig itemDetalheColeta = new ItemListConfig() {
 
    		/**
			 * Find paginated do filho Passa por este ponto apenas na primeira
			 * vez em que a list filha e chamada. Apos a primeira vez ela e
			 * carregada da memoria
    		 * 
			 * @param masterId
			 *            id do pai
			 * @param parameters
			 *            todos os parametros vindo da tela pai
    		 */ 
			public List initialize(Long masterId, Map parameters) { 
				return detalheColetaService.findDetalheColeta(masterId);				
			}

			/**
			 * Busca rowCount da grid da tela filha Passa por este ponto apenas
			 * na primeira vez em que a list filha e chamada. Apos a primeira
			 * vez ela e carregada da memoria
			 * 
			 * @param masterId
			 *            id do pai
			 * @param parameters
			 *            todos os parametros vindo da tela pai
			 */	
			public Integer getRowCount(Long masterId, Map parameters) {
				return detalheColetaService.getRowCountDetalheColeta(masterId);				
			}
			
			/**
			 * Chama esta funcao depois de editar um item da grid filho E retira
			 * atributos desnecessarios para o filho
			 * 
			 * @param newBean 
			 * @param oldBean 
			 */
			public void modifyItemValues(Object newBean, Object bean) {
		        DetalheColeta detalheColetaNew = (DetalheColeta) newBean;
		        DetalheColeta detalheColetaOld = (DetalheColeta) bean;
		        		        
				detalheColetaOld.setIdDetalheColeta(detalheColetaNew
						.getIdDetalheColeta());
				detalheColetaOld.setTpFrete(detalheColetaNew.getTpFrete());
				detalheColetaOld.setVlMercadoria(detalheColetaNew
						.getVlMercadoria());
				detalheColetaOld.setQtVolumes(detalheColetaNew.getQtVolumes());
				detalheColetaOld.setPsMercadoria(detalheColetaNew
						.getPsMercadoria());
				detalheColetaOld.setPsAforado(detalheColetaNew.getPsAforado());
				detalheColetaOld.setObDetalheColeta(detalheColetaNew
						.getObDetalheColeta());
				detalheColetaOld.setNmDestinatario(detalheColetaNew
						.getNmDestinatario());
				detalheColetaOld.setOrigem(detalheColetaNew.getOrigem());
				detalheColetaOld.setPedidoColeta(detalheColetaNew
						.getPedidoColeta());
				detalheColetaOld.setServico(detalheColetaNew.getServico());
				detalheColetaOld.setNaturezaProduto(detalheColetaNew
						.getNaturezaProduto());
				detalheColetaOld.setMoeda(detalheColetaNew.getMoeda());
				detalheColetaOld.setLocalidadeEspecial(detalheColetaNew
						.getLocalidadeEspecial());
				detalheColetaOld.setMunicipio(detalheColetaNew.getMunicipio());
				detalheColetaOld.setFilial(detalheColetaNew.getFilial());
				detalheColetaOld.setCliente(detalheColetaNew.getCliente());
				detalheColetaOld.setEventoColeta(detalheColetaNew
						.getEventoColeta());
				detalheColetaOld.setCotacao(detalheColetaNew.getCotacao());
				detalheColetaOld.setCtoInternacional(detalheColetaNew
						.getCtoInternacional());
				detalheColetaOld.setBlEntregaDireta(detalheColetaNew
						.getBlEntregaDireta());
		        
		        List listNotaFiscalColeta = new ArrayList();
		        
				for (int i = 0; i < detalheColetaNew.getNotaFiscalColetas()
						.size(); i++) {
					NotaFiscalColeta notaFiscalColeta = (NotaFiscalColeta) detalheColetaNew
							.getNotaFiscalColetas().get(i);
					notaFiscalColeta.setIdNotaFiscalColeta(null);
					notaFiscalColeta.setDetalheColeta(detalheColetaOld);
					listNotaFiscalColeta.add(notaFiscalColeta);
				}
				detalheColetaOld.getNotaFiscalColetas().clear();
				
				detalheColetaOld.setNotaFiscalColetas(listNotaFiscalColeta);
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
			 * Todos os dados a serem carregados na grid pelo form passam antes
			 * por este metodo. Para se fazer uma validacao... Recomenda-se que
			 * o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 * 
			 * @param parameters 
			 * @param bean
			 *            a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object bean) {
				DetalheColeta detalheColeta = (DetalheColeta)bean;
				TypedFlatMap param = (TypedFlatMap) parameters;
				
				detalheColeta.setIdDetalheColeta(param
						.getLong("idDetalheColeta"));
				DomainValue tpFrete = getDomainValueService()
						.findDomainValueByValue("DM_TIPO_FRETE",
								param.getString("tpFrete"));
				detalheColeta.setTpFrete(tpFrete);
				detalheColeta.setVlMercadoria(param
						.getBigDecimal("vlMercadoria"));
				detalheColeta.setQtVolumes(param.getInteger("qtVolumes"));
				detalheColeta.setPsMercadoria(param
						.getBigDecimal("psMercadoria"));
				detalheColeta.setPsAforado(param.getBigDecimal("psAforado"));
				detalheColeta.setBlEntregaDireta(param
						.getBoolean("blEntregaDireta"));
				detalheColeta.setObDetalheColeta(param
						.getString("obDetalheColeta"));
				if(!param.getString("nmDestinatario").equals("")) {
					detalheColeta.setNmDestinatario(param
							.getString("nmDestinatario"));
				}				
				detalheColeta.setOrigem(param.getString("origem"));
				if(param.getLong("masterId") != null) {
					detalheColeta.setPedidoColeta(getPedidoColetaService()
							.findById(param.getLong("masterId")));
				}
				detalheColeta.setServico(servicoService.findById(param
						.getLong("servico.idServico")));
				detalheColeta.setNaturezaProduto(naturezaProdutoService
						.findById(param
								.getLong("naturezaProduto.idNaturezaProduto")));
				detalheColeta.setMoeda(moedaService.findById(param
						.getLong("moeda.idMoeda")));
				if(param.getLong("localidadeEspecial.idLocalidadeEspecial") != null) {
					detalheColeta
							.setLocalidadeEspecial(localidadeEspecialService.findById(param
									.getLong("localidadeEspecial.idLocalidadeEspecial")));
				}
				if(param.getLong("municipio.idMunicipio") != null) {
					detalheColeta.setMunicipio(municipioService.findById(param
							.getLong("municipio.idMunicipio")));
				}
				if(param.getLong("filial.idFilial") != null) {
					detalheColeta.setFilial(filialService.findById(param
							.getLong("filial.idFilial")));
				}
				if(param.getLong("cliente.idCliente") != null) {					
					detalheColeta.setCliente(clienteService.findById(param
							.getLong("cliente.idCliente")));
				}
				if(param.getLong("cotacao.idCotacao") != null) {
					detalheColeta.setCotacao(cotacaoService.findById(param
							.getLong("cotacao.idCotacao")));
				}
				if(param.getLong("ctoInternacional.idDoctoServico") != null) {
					detalheColeta
							.setCtoInternacional(ctoInternacionalService.findById(param
									.getLong("ctoInternacional.idDoctoServico")));
				}

				List listMapNotaFiscalColetas = param
						.getList("notaFiscalColetas");
				List listNotaFiscalColetas = new ArrayList();
				if(listMapNotaFiscalColetas != null) {
					for (int i=0; i<listMapNotaFiscalColetas.size(); i++) {
						TypedFlatMap typedFlatMap = (TypedFlatMap) listMapNotaFiscalColetas
								.get(i);
						NotaFiscalColeta notaFiscalColeta = new NotaFiscalColeta();
						Long idNotaFiscalColeta = typedFlatMap
								.getLong("idNotaFiscalColeta");
						if (idNotaFiscalColeta != null
								&& !(idNotaFiscalColeta.longValue() <= 0)) {
							notaFiscalColeta.setIdNotaFiscalColeta(typedFlatMap
									.getLong("idNotaFiscalColeta"));
						} else {
							notaFiscalColeta.setIdNotaFiscalColeta(null);
						}						
						notaFiscalColeta.setNrNotaFiscal(typedFlatMap
								.getInteger("nrNotaFiscal"));
						notaFiscalColeta.setNrChave(typedFlatMap
								.getString("nrChave"));
						notaFiscalColeta.setDetalheColeta(detalheColeta);
						
						listNotaFiscalColetas.add(notaFiscalColeta);
					}
				}
				
				detalheColeta.setNotaFiscalColetas(listNotaFiscalColetas);
				
		    	// Insere um Evento de Coleta para o Detalhe de Coleta.
				detalheColeta.setEventoColeta(this.getEventoColeta(param,
						detalheColeta.getPedidoColeta()));
		    	
		    	if(param.getLong("doctoServico.idDoctoServico") != null) {
					detalheColeta.setDoctoServico(doctoServicoService
							.findByIdJoinFilial(param
									.getLong("doctoServico.idDoctoServico")));
		    	}
		    	
				return detalheColeta;
			}

			private EventoColeta getEventoColeta(TypedFlatMap param,
					PedidoColeta pedidoColeta) {
				if (param.getLong("eventoColeta.usuario.idUsuario") != null
						&& param.getLong("eventoColeta.ocorrenciaColeta.idOcorrenciaColeta") != null) {
					EventoColeta eventoColeta = new EventoColeta();
					eventoColeta.setPedidoColeta(pedidoColeta);
					
					Usuario usuario = usuarioService.findById(param
							.getLong("eventoColeta.usuario.idUsuario"));
					eventoColeta.setUsuario(usuario);
	
					OcorrenciaColeta ocorrenciaColeta = ocorrenciaColetaService
							.findById(param
									.getLong("eventoColeta.ocorrenciaColeta.idOcorrenciaColeta"));
					eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
	
					eventoColeta.setDsDescricao(param
							.getString("eventoColeta.dsDescricao"));
					eventoColeta
							.setDhEvento(JTDateTimeUtils.getDataHoraAtual());
					eventoColeta.setTpEventoColeta(new DomainValue("LI"));		    		
					return eventoColeta;
				}
				return null;
			}			

    	};

		config.addItemConfig("detalheColeta", DetalheColeta.class,
				itemDetalheColeta, descComparator);
		return config;
	}	
	
	/**
	 * #################################### # Fim dos métodos para tela de DF2 #
	 * ####################################
	 */

	/**
	 * Faz a validacao do PCE.
	 *  
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap validatePCE(TypedFlatMap criteria) {
		TypedFlatMap result = new TypedFlatMap();

		Long cdProcesso;
    	Long cdEvento;
    	Long cdOcorrencia;
		Long idCliente = criteria.getLong("idCliente");
			
		//PCE = NORMAL
		if (criteria.getString("tpPedidoColeta").equals("NO")
				|| criteria.getString("tpPedidoColeta").equals("DA")) {
			
    		cdProcesso = Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_COLETA);
        	cdEvento = Long.valueOf(EventoPce.ID_EVENTO_PCE_REG_PED_COLETA_NORMAL);
			cdOcorrencia = Long.valueOf(
					OcorrenciaPce.ID_OCORR_PCE_COL_ABERT0_REG_PED_COL_NORMAL);
        
        //PCE = DIRETA
    	} else if (criteria.getString("tpPedidoColeta").equals("DI")) {
    		
    		cdProcesso = Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_COLETA);
        	cdEvento = Long.valueOf(EventoPce.ID_EVENTO_PCE_REG_PED_COLETA_DIRETA);
			cdOcorrencia = Long.valueOf(
					OcorrenciaPce.ID_OCORR_PCE_COL_ABERT0_REG_PED_COL_DIRETA);
        	
        //PCE = AEROPORTO
		} else if (criteria.getString("tpPedidoColeta").equals(
				TP_PEDIDO_COLETA_AEREO)) {
    		
    		cdProcesso = Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_COLETA);
			cdEvento = Long.valueOf(
					EventoPce.ID_EVENTO_PCE_REG_PED_COLETA_AEROPORTO);
			cdOcorrencia = Long.valueOf(
					OcorrenciaPce.ID_OCORR_PCE_COL_ABERT0_REG_PED_COL_AEROPORTO);
        	
        //PCE = DEVOLUCAO
    	} else {
    		cdProcesso = Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_COLETA);
			cdEvento = Long.valueOf(
					EventoPce.ID_EVENTO_PCE_REG_PED_COLETA_DEVOLUCAO);
			cdOcorrencia = Long.valueOf(
					OcorrenciaPce.ID_OCORR_PCE_COL_ABERT0_REG_PED_COL_DEVOLUCAO);
    	}
		
		result = versaoDescritivoPceService.validatePCE(idCliente, cdProcesso,
				cdEvento, cdOcorrencia);
		
		return result;
	}
	
	/**
	 * Faz a validacao do PCE.
	 *  
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap validatePCEDetalheColeta(TypedFlatMap criteria) {
		
		TypedFlatMap result = new TypedFlatMap();
		Long idCliente = criteria.getLong("idCliente");
		
		if (criteria.getString("tpPedidoColeta").equals("DE")) {	
			result = versaoDescritivoPceService
					.validatePCE(
							idCliente,
        			Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_COLETA), 
							Long.valueOf(
									EventoPce.ID_EVENTO_PCE_REG_PED_COLETA_DEVOLUCAO),
							Long.valueOf(
									OcorrenciaPce.ID_OCORR_PCE_COL_ABERT0_REG_PED_COL_DEVOLUCAO));
		}
		
		return result;
	}

	/**
	 * @param liberacaoEmbarqueService
	 *            the liberacaoEmbarqueService to set
	 */
	public void setLiberacaoEmbarqueService(
			LiberacaoEmbarqueService liberacaoEmbarqueService) {
		this.liberacaoEmbarqueService = liberacaoEmbarqueService;
	}
	
	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map findSomatorios(TypedFlatMap criteria){
		Map map = new HashMap();
		
		Long idColeta = criteria.getLong("idColeta");
		String tpColeta = criteria.getString("tpColeta");

		MasterEntry entry = getMasterFromSession(idColeta, true);
		
		ItemList items = getItemsFromSession(entry, "detalheColeta");
		ItemListConfig config = getMasterConfig().getItemListConfig(
				"detalheColeta");
		
		BigDecimal vlDocumentoTotal = new BigDecimal("0.00");
		vlDocumentoTotal = vlDocumentoTotal.setScale(2, RoundingMode.HALF_UP);
		BigDecimal vlPesoTotal = new BigDecimal("0.000");
		vlPesoTotal = vlPesoTotal.setScale(3, RoundingMode.HALF_UP);
		Integer totalCtes = 0;
		
		for (Iterator iter = items.iterator(idColeta, config); iter.hasNext();) {
			DetalheColeta detalheColeta = (DetalheColeta)iter.next();
			if (TP_PEDIDO_COLETA_AEREO.equals(tpColeta)
					&& detalheColeta.getDoctoServico() != null) {
				totalCtes = totalCtes + 1;
				vlPesoTotal = vlPesoTotal.add(detalheColeta.getPsMercadoria());
				vlDocumentoTotal = vlDocumentoTotal.add(detalheColeta
						.getVlMercadoria());
			}
		}
		
		map.put("qtdeTotalDocumentos", totalCtes);
		map.put("vlPesoTotal", vlPesoTotal);
		map.put("valorTotalDocumentos",vlDocumentoTotal);

		return map;
	}
	
	public Map findDocumentosAwbComColeta(TypedFlatMap criteria){
		Map map = new HashMap();
		
		Long coleta = criteria.getLong("idColeta");
		MasterEntry master = getMasterFromSession(coleta, true);
		PedidoColeta pedidoColeta = (PedidoColeta) master.getMaster();
		ItemList listaItems = getItemsFromSession(master, "detalheColeta");
		ItemListConfig config = getMasterConfig().getItemListConfig(
				"detalheColeta");

		if(criteria.getLong("idAwb") != null){
			map.put("valor",
					getPedidoColetaService().findDocumentosAwbComColeta(
							criteria.getLong("idAwb"), listaItems, config,
							pedidoColeta));
		}
		
		return map;
	}
	
	public List<Long> findDocumentosSemColeta(TypedFlatMap criteria,
			List<Long> idsConhecimento) {
		Long coleta = criteria.getLong("idColeta");
		MasterEntry master = getMasterFromSession(coleta, true);
		PedidoColeta pedidoColeta = (PedidoColeta) master.getMaster();
		ItemList listaItems = getItemsFromSession(master, "detalheColeta");
		ItemListConfig config = getMasterConfig().getItemListConfig(
				"detalheColeta");
		List<Long> idsConhecimentoSemColeta = new ArrayList<Long>();
		for(Long id: idsConhecimento){
			int registroNoBanco = getPedidoColetaService()
					.findRowCountByIdDoctoServicoNotCanceled(id, null);
			int registroMemoria = getPedidoColetaService().validateAwbIntersec(
					id, pedidoColeta, config, listaItems, null);
			
			if(registroNoBanco == 0 && registroMemoria == 0 ){
				idsConhecimentoSemColeta.add(id);
			}
		}
		return idsConhecimentoSemColeta;
	}
	
	public void setDiaUtils(DiaUtils diaUtils) {
		this.diaUtils = diaUtils;
	}

	/**
	 * Busca a filial do usuario logado.
	 * 
	 * @return Retorna apenas o id da filial, a sigla e o nome
	 */
	public Filial getFilialUsuarioLogado(Map map) {
		Filial filial = new Filial();
		filial.setIdFilial(SessionUtils.getFilialSessao().getIdFilial());
		filial.setSgFilial(SessionUtils.getFilialSessao().getSgFilial());
		Pessoa pessoa = new Pessoa();
		pessoa.setNmFantasia(SessionUtils.getFilialSessao().getPessoa()
				.getNmFantasia());
		filial.setPessoa(pessoa);
		return filial;
	}

	/***
	 * Valida se é necessário validar o FOB, se for necessário é feita a
	 * validação.
	 * 
	 * @author Gabriel.Scossi
	 * @since 15/02/2016
	 * @param parameters
	 * @return Map
	 */
	public Map validaColetaFOB(TypedFlatMap parameters) {
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService
				.findByNomeParametro(parameters.getLong("idFilial"),
						"BLQ_FOB_TP_SIT_TRIBU", false, true);
		if (conteudoParametroFilial == null
				|| conteudoParametroFilial.getVlConteudoParametroFilial()
						.equals("S")) {

			InscricaoEstadual inscEstadual = inscricaoEstadualService
					.findIeByIdPessoaAtivoPadrao(parameters.getLong("idPessoa"));
			if (inscEstadual != null) {
				parameters.put("validaColetaFOB", tipoTributacaoIEService
						.validateTipoTributacaoFOB(inscEstadual
								.getIdInscricaoEstadual()));
			}

			return parameters;
		}

		return null;

	}
	
	public void setEmbarqueValidationService(EmbarqueValidationService embarqueValidationService) {
		this.embarqueValidationService = embarqueValidationService;
	}
}