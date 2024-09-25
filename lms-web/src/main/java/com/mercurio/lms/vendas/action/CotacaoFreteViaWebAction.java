package com.mercurio.lms.vendas.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConfiguracoesFacadeImpl;
import com.mercurio.lms.configuracoes.model.Cep;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.param.PesquisarCepParam;
import com.mercurio.lms.configuracoes.model.service.CepService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CalculoFreteWebServiceRetorno;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Densidade;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.model.Frete;
import com.mercurio.lms.expedicao.model.ParcelasFreteWebService;
import com.mercurio.lms.expedicao.model.ServicoAdicionalPrecificado;
import com.mercurio.lms.expedicao.model.ServicoAdicionalWebService;
import com.mercurio.lms.expedicao.model.service.CalcularFreteService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DensidadeService;
import com.mercurio.lms.expedicao.model.service.TabelaServicoAdicionalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.service.MunicipioGrupoRegiaoService;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.DiaUtils;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.CotacaoWebService;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.CotacaoFreteViaWebService;
import com.mercurio.lms.vendas.model.service.CotacaoService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.util.ClienteUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.VendasUtils;

/**
 * Generated by: ADSM ActionGenerator
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.cotacaoFreteViaWebAction"
 */
@ServiceSecurity
public class CotacaoFreteViaWebAction extends CrudAction {
	private static final long ID_PAIS_BRASIL = 30L;
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private ClienteService clienteService;
	private ServicoService servicoService;
	private CepService cepService;
	private EnderecoPessoaService enderecoPessoaService;
	private MoedaPaisService moedaPaisService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private DivisaoClienteService divisaoClienteService;
	private PpeService ppeService;
	private ConhecimentoService conhecimentoService;
	private CotacaoFreteViaWebService cotacaoFreteViaWebService;
	private TipoTributacaoIEService tipoTributacaoIEService;
	private DomainValueService domainValueService;
	private ConfiguracoesFacadeImpl configuracoesFacadeImpl;
	private DomainService domainService;
	private MunicipioService municipioService;
	private MoedaService moedaService;
	private FilialService filialService;
	private UnidadeFederativaService unidadeFederativaService;
	private PaisService paisService;
	private CotacaoFreteViaWebCalculoFreteAction cotacaoFreteViaWebCalculoFreteAction;
	private CalcularFreteService calcularFreteService;
	private DensidadeService densidadeService;
	private CotacaoService cotacaoService;
	private TabelaServicoAdicionalService tabelaServicoAdicionalService;
	private MunicipioGrupoRegiaoService municipioGrupoRegiaoService;
	private ConteudoParametroFilialService conteudoParamFilialService;
	private DiaUtils diaUtils;
	private ReportExecutionManager reportExecutionManager;
	private UsuarioService usuarioService;
	private ConfiguracoesFacade configuracoesFacade;
	private IntegracaoJmsService integracaoJmsService;
	
	final String REMETENTE = "remetente";
	final String DESTINATARIO = "destinatario";
	private static final Locale DEFAULT_LOCALE = new Locale("pt", "BR");

	private final Logger log = LogManager.getLogger(CotacaoFreteViaWebAction.class);


	/**
	 * Realiza o cálculo do frete via web service 
	 * @param cotacaoWebService
	 * @author lucianos
	 * @return CalculoFreteWebServiceRetorno
	 */
	@MethodSecurity(processGroup = "vendas.calculoFreteWebService", processName = "calculaFrete", authenticationRequired=false)
	public CalculoFreteWebServiceRetorno calculoFrete( CotacaoWebService cotacaoWebService ){
		Pais pais = getPaisService().findPaisBySgPais("BRA");
		SessionContext.set(SessionKey.PAIS_KEY, pais);

		List<String> errorList = new ArrayList<String>();
		CalculoFreteWebServiceRetorno calculoFreteWebServiceRetorno = new CalculoFreteWebServiceRetorno();
		calculoFreteWebServiceRetorno.setErrorList(errorList);
		
		try{
			Frete frete = new Frete();
			calculoFreteWebServiceRetorno = populaPojoFrete(frete, cotacaoWebService, calculoFreteWebServiceRetorno);
			
			if (CollectionUtils.isNotEmpty(calculoFreteWebServiceRetorno.getErrorList())) {
				return calculoFreteWebServiceRetorno;
			}
			
			SessionContext.set(SessionKey.FILIAL_KEY,frete.getConhecimento().getFilialByIdFilialOrigem());
	
			/** Executa a rotina de calculo de frete */
			Map<String, Object> mapRetorno = calcularFreteService.executeCalcularFrete(frete);
			
			/** Erros retornados pelo calcularFreteService.calcularFrete(frete) */
			String erro = (String)mapRetorno.get("erro"); 
			
			if( erro == null ){
			/**prazo de entrega em horas */
			 	Long prazoEntrega = calculaPpe(frete);
				calculoFreteWebServiceRetorno.setPrazoEntrega(prazoEntrega);
						
				Long idServico = null;
				Long idDivisaoCliente = null;
				
				if(frete.getConhecimento() != null && frete.getConhecimento().getServico() != null) {
					idServico = frete.getConhecimento().getServico().getIdServico();
				}
				
				if(frete.getConhecimento() != null && frete.getConhecimento().getDivisaoCliente() != null) {
					idDivisaoCliente = frete.getConhecimento().getDivisaoCliente().getIdDivisaoCliente();
				}
				
				List<ServicoAdicionalPrecificado> servicosAdicionais =
						tabelaServicoAdicionalService.findByTabelaCliente(idServico, idDivisaoCliente);
								
				populaPojoCalculoFreteWebServiceRetorno( calculoFreteWebServiceRetorno, mapRetorno, frete );
				populateServicosAdicionais(calculoFreteWebServiceRetorno, servicosAdicionais);						
				
			}else{
				findErroByMessage(erro, calculoFreteWebServiceRetorno);
			}
			
		}catch (Exception e) {
				log.error(e);
			log.error("Ocorreu um erro na cotação web: ", e);
			findErroByMessage( e.toString(), calculoFreteWebServiceRetorno);
			return calculoFreteWebServiceRetorno;
		}
		return calculoFreteWebServiceRetorno;
	}
	
	private Long calculaPpe(Frete frete){
		Long idServico = frete.getCalculoFrete().getIdServico();
		Long idMunicipioOrigem = frete.getConhecimento().getMunicipioByIdMunicipioColeta().getIdMunicipio();
		Long idMunicipioDestino = frete.getConhecimento().getMunicipioByIdMunicipioEntrega().getIdMunicipio();
		String nrIdentificacaoClienteRemetente = getNrIdentificacaoCliente(frete.getConhecimento().getClienteByIdClienteRemetente());
		String nrIdentificacaoClienteDestinatario = getNrIdentificacaoCliente(frete.getConhecimento().getClienteByIdClienteDestinatario());
		String nrIdentificacaoClienteDevedorFrete = getNrIdentificacaoCliente(frete.getConhecimento().getDevedorDocServs().get(0).getCliente());
		Long idFilialOrigem = frete.getConhecimento().getFilialByIdFilialOrigem().getIdFilial();
		Long idFilialDestino = frete.getConhecimento().getFilialByIdFilialDestino().getIdFilial();

		Short nrPpe = 0;

		Cliente clienteRemetente = clienteService.findByNrIdentificacao(nrIdentificacaoClienteRemetente);
		Cliente clienteDestinatario = clienteService.findByNrIdentificacao(nrIdentificacaoClienteDestinatario);
		
		Boolean contemDadosBasicos = idServico != null && idMunicipioOrigem != null && idMunicipioDestino != null;
		if (contemDadosBasicos 
				&& clienteRemetente != null
				&& clienteDestinatario != null) {
			nrPpe = cotacaoService.executeCalcularPpeClienteEspecial(nrIdentificacaoClienteRemetente, nrIdentificacaoClienteDestinatario, nrIdentificacaoClienteDevedorFrete,
					idServico, idMunicipioOrigem, idMunicipioDestino, idFilialOrigem, idFilialDestino);
		} else if( contemDadosBasicos ){
			nrPpe = cotacaoService.validateCalculoPpe(idServico,idMunicipioOrigem,idMunicipioDestino);
		}
		
		return nrPpe.longValue(); 
	}	
	
	private String getNrIdentificacaoCliente(Cliente cliente){
		if( cliente != null && cliente.getPessoa() != null){
			return cliente.getPessoa().getNrIdentificacao();	
		}
		
		return null;
	}
	
	private void findErroByMessage(String erro, CalculoFreteWebServiceRetorno calculoFreteWebServiceRetorno){
		Scanner sc = new Scanner(erro).useDelimiter("=");
	
		while ( sc.hasNext() ) {
			String msg = sc.next();
			if( msg.startsWith("LMS") ){
				calculoFreteWebServiceRetorno.getErrorList().add(getConfiguracoesFacadeImpl().getMensagem(msg));
			}else{
				calculoFreteWebServiceRetorno.getErrorList().add(erro);
			}
		}
		
	}

	/**
	 * Cria Cotacao na Sessao
	 */
	public void createCotacao() {
		Cotacao cotacao = new Cotacao();
		cotacao.setTpDocumentoCotacao(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NACIONAL));
		VendasUtils.createCotacaoInSession(cotacao);
	}

	/**
	 * Remove Cotacao da Sessao
	 */
	public void cancelarCotacao() {
		createCotacao();
	}

	/**
	 * Tipo Serviço
	 * @param criteria
	 * @return
	 */
	@MethodSecurity(processGroup="vendasCalculoFreteWebService", processName="findServicos", authenticationRequired=false)
	public List findServicos() {
		return servicoService.findByTpAbrangencia(ConstantesExpedicao.ABRANGENCIA_NACIONAL);
	}

	/**
	 * Divisões Cliente
	 * @param criteria
	 * @return
	 */
	@MethodSecurity(processGroup="vendasCalculoFreteWebService", processName="findDivisaoCliente", authenticationRequired=false)
	public List findDivisaoCliente(TypedFlatMap criteria) {
		Long idCliente = criteria.getLong("idCliente");
		Long idServico = criteria.getLong("idServico");
		String tpCliente = criteria.getString("tpCliente");
		if (idCliente != null && idServico != null 
			&& ClienteUtils.isParametroClienteEspecial(tpCliente)
		) {
			List<DivisaoCliente> divisoesCliente = divisaoClienteService.findDivisaoClienteByIdServico(idCliente, idServico);
			for (DivisaoCliente divisaoCliente : divisoesCliente) {
				//Seta cliente como null para nao trafegar esse dados ateh a tela, pois nao serao utilizados 
				divisaoCliente.setCliente(null);
			}
			return divisoesCliente;
		}
		return null;
	}
	

	@MethodSecurity(processGroup="vendasCalculoFreteWebService", processName="findDomainByDsDomain", authenticationRequired=false)
	public List findDomainByDsDomain( String dsDomain ){
		List<Map<String, Object>> listRetorno = new ArrayList<Map<String, Object>>();

		List listDomain = new ArrayList();
		listDomain =  domainValueService.findDomainValues( dsDomain );
		for (Iterator iterator = listDomain.iterator(); iterator.hasNext();) {
			Map map = new HashMap<String, Object>();
			DomainValue domainValue = (DomainValue) iterator.next();
			map.put("description", domainValue.getDescriptionAsString() );
			map.put("value", domainValue.getValue() );
			listRetorno.add(map);
		}
				
		return listRetorno;
	}
	

	/**
	 * Carrega Dados necessários do <b>Cliente Remetente e Destinatario</b>
	 * @param criteria
	 * @return
	 */
	@MethodSecurity(processGroup="vendas.calculoFreteWebService", processName="findDadosCliente", authenticationRequired=false )
	public List findDadosCliente(TypedFlatMap criteria) {
		List result = clienteService.findClienteByNrIdentificacao(criteria.getString("pessoa.nrIdentificacao"));
		if(!result.isEmpty()) {
			Map cliente = (Map) result.get(0);
			Long idCliente = Long.valueOf(cliente.get("idCliente").toString());
			cliente.put("inscricaoEstadual", tipoTributacaoIEService.findVigentesByIdPessoa(idCliente));
		}
		return result;
	}
	

	/**
	 * Lookup de CEP 
	 * @param criteria
	 * @return
	 */
	@MethodSecurity(processGroup="vendas.calculoFreteWebService", processName="findCepLookup", authenticationRequired=false )
	public List findCepLookup(TypedFlatMap criteria) {
		PesquisarCepParam param = new PesquisarCepParam();
		param.setNrCep(criteria.getString("cepCriteria"));
		param.setIdPais(criteria.getLong("municipio.unidadeFederativa.pais.idPais"));
		param.setSgPais(criteria.getString("municipio.unidadeFederativa.pais.sgPais"));
		return cepService.findCepLookup(param);
	}

	/**
	 * Retorna a Filial e o telefone respectivo
	 * @param criteria
	 * @return
	 */
	@MethodSecurity(processGroup="vendas.calculoFreteWebService", processName="findFilialDadosByMunicipioNrCepServico", authenticationRequired=false )
	public Map findFilialDadosByMunicipioNrCepServico(TypedFlatMap criteria) {
		Long idMunicipio = criteria.getLong("idMunicipio");
		String nrCep = criteria.getString("nrCep");
		Long idServico = criteria.getLong("idServico");
		Boolean blIndicadorColeta = criteria.getBoolean("blIndicadorColeta");
		Map retorno = new HashMap();
		if (idMunicipio != null && nrCep != null) {
			try {
				retorno = ppeService.findAtendimentoMunicipio(
					idMunicipio,
					idServico,
					blIndicadorColeta,
					null,
					nrCep,
					null, null, null, null, null,
					"N", null,null);
			} catch (RuntimeException e) {
				throw new BusinessException("LMS-01100");
			}
		}
		if (retorno.get("idFilial") != null) {
			Long idFilial = (Long) retorno.get("idFilial");
			TypedFlatMap telefone = telefoneEnderecoService.findTelefoneEnderecoByIdPessoaTpTelefone(idFilial, "C");
			if(telefone != null) {
				String ddd = telefone.getString("nrDdd");
				String nrTelefone = telefone.getString("nrTelefone");
				retorno.put("nrTelefone", FormatUtils.formatTelefone(ddd + nrTelefone));
			}
		}
		return retorno;
	}

	/**
	 * Moeda, Pais e Endereco Padrão 
	 * @param criteria
	 * @return
	 */
	public List findMoedasPaisEnderecoPadrao(TypedFlatMap criteria) {
		Long idCliente = criteria.getLong("idCliente");
		if (idCliente != null) {
			/** Busca Moeda do Cliente */
			EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idCliente);
			if (enderecoPessoa != null) {
				return moedaPaisService.findMoedaByPaisSituacao(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getIdPais(), "A");
			}
		} else {
			/** Caso nao seja informado cliente, busca moeda da Sessão */
			Moeda moeda = SessionUtils.getMoedaSessao();
			if(moeda != null) {
				Map moedaMap = new HashMap(1);
				moedaMap.put("idMoeda", moeda.getIdMoeda());
				moedaMap.put("dsSimbolo", moeda.getSgMoeda()+" "+moeda.getDsSimbolo());
				List result = new ArrayList(1);
				result.add(moedaMap);
				return result;
			}
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * Calcula Peso Cubado
	 * @param criteria
	 * @return
	 */
	@MethodSecurity(processGroup = "vendas.calculoFreteWebService", processName = "calculaPesoCubado", authenticationRequired=false)
	public Map calculaPesoCubado(TypedFlatMap criteria) {
		BigDecimal pesoCubado = BigDecimal.ZERO;
		String tpModal = criteria.getString("tpModal");
		if (StringUtils.isNotBlank(tpModal)) {
			Cotacao cotacao = VendasUtils.getCotacaoInSession();
			pesoCubado = conhecimentoService.calculaPsAforado(tpModal, cotacao.getDimensoes());
		}
		Map retorno = new HashMap();
		retorno.put("pesoCubado",pesoCubado);
		return retorno;
	}

	
	/**
	 * popula o pojo Cotação 
	 * @param cotacaoWebService
	 * @param calculoFreteWebServiceRetorno
	 */
	private CalculoFreteWebServiceRetorno populaPojoFrete(Frete frete, CotacaoWebService cotacaoWebService, CalculoFreteWebServiceRetorno calculoFreteWebServiceRetorno)
					throws Exception{
		String PESSOA_JURIDICA = "J";
		
		List<String> errorList = new ArrayList<String>();
		
		DoctoServicoDadosCliente doctoServicoDadosCliente = new DoctoServicoDadosCliente();
		Conhecimento conhecimento = new Conhecimento();
			
		/** Tipo conhecimento */
		conhecimento.setTpConhecimento(domainValueService.findDomainValueByValue("DM_TIPO_CONHECIMENTO", "NO") );
			
		/** C - Cotação */
		frete.setTpFrete("C");
		
		/** Tipo de frete */
		conhecimento.setTpFrete(domainValueService.findDomainValueByValue("DM_TIPO_FRETE", cotacaoWebService.getTpFrete()));
		
		/** Tipo calculo preço */
		conhecimento.setTpCalculoPreco(domainValueService.findDomainValueByValue("DM_TIPO_CALCULO_FRETE", "N"));
				
		/** Tipo documento Servico */
		conhecimento.setTpDocumentoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NACIONAL));
			
			Servico servico = servicoService.findServicoBySigla(cotacaoWebService.getTpServico());
		conhecimento.setServico(servico);
			
		/** dados do remente */
		TypedFlatMap typedFlatMapAux = new TypedFlatMap();
			typedFlatMapAux.put("pessoa.nrIdentificacao", cotacaoWebService.getNrIdentifClienteRem());
			List dadosClienteRemetente = findDadosCliente( typedFlatMapAux );	
		Map dadosClienteRemetenteMap = new HashMap();
		if(dadosClienteRemetente != null && !dadosClienteRemetente.isEmpty() ){
			dadosClienteRemetenteMap = (Map)dadosClienteRemetente.get(0);
	
			Cliente cliente = getCliente(cotacaoWebService, dadosClienteRemetenteMap, REMETENTE);
			conhecimento.setClienteByIdClienteRemetente(cliente);
			conhecimento.getClienteByIdClienteRemetente().getPessoa().setNmPessoa(cliente.getPessoa().getNmPessoa());
			
			doctoServicoDadosCliente.setIdClienteRemetente(cliente.getIdCliente());
			
			TipoTributacaoIE tipoTributacaoIE = null;
			if( cliente.getPessoa().getTpPessoa().getValue().equalsIgnoreCase("J") ){
				List<TipoTributacaoIE> listTpTributacao = (List)dadosClienteRemetenteMap.get("inscricaoEstadual");
				
				boolean iEValido = validaInscricaoEstadual(listTpTributacao, cotacaoWebService.getNrInscricaoEstadualRemetente(),
						calculoFreteWebServiceRetorno, REMETENTE);
				
				if( iEValido ){
					tipoTributacaoIE = getTipoTributacao(cliente.getPessoa(), listTpTributacao, 
							cotacaoWebService.getNrInscricaoEstadualRemetente());
					
					validaSituacaoTributaria(cotacaoWebService.getTpSituacaoTributariaRemetente(),
							tipoTributacaoIE.getTpSituacaoTributaria().getValue(),
							calculoFreteWebServiceRetorno, REMETENTE);
			}
			
			}

			InscricaoEstadual inscricaoEstadualRemetente = new InscricaoEstadual();
			inscricaoEstadualRemetente.setIdInscricaoEstadual(tipoTributacaoIE.getInscricaoEstadual().getIdInscricaoEstadual());
			conhecimento.setInscricaoEstadualRemetente(inscricaoEstadualRemetente);
			doctoServicoDadosCliente.setIdInscricaoEstadualRemetente(conhecimento.getInscricaoEstadualRemetente().getIdInscricaoEstadual());
			doctoServicoDadosCliente.setTpSituacaoTributariaRemetente(cotacaoWebService.getTpSituacaoTributariaRemetente());
						
			if (ConstantesExpedicao.TP_FRETE_CIF.equals( cotacaoWebService.getTpFrete() )) {
				
				conhecimento.setTpDevedorFrete(domainValueService.findDomainValueByValue("DM_TIPO_DEVEDOR_FRETE", "R"));
				
				List<DevedorDocServ> list = new ArrayList<DevedorDocServ>();
				DevedorDocServ devedorDocServ = new DevedorDocServ();
				devedorDocServ.setCliente(cliente);
				list.add(devedorDocServ);
				conhecimento.setDevedorDocServs(list);
				
				doctoServicoDadosCliente.setTpSituacaoTributariaResponsavel(cotacaoWebService.getTpSituacaoTributariaRemetente());
				
				DivisaoCliente divisaoCliente = getDivisaoCliente( dadosClienteRemetenteMap, cotacaoWebService,
												calculoFreteWebServiceRetorno, servico, REMETENTE );
				
				conhecimento.setDivisaoCliente(divisaoCliente);
			}
			
		} else if( dadosClienteRemetente.isEmpty() && cotacaoWebService.getTpFrete().equalsIgnoreCase(ConstantesExpedicao.TP_FRETE_CIF) ){
			calculoFreteWebServiceRetorno.getErrorList().add( getConfiguracoesFacadeImpl().getMensagem("LMS-01180") );
		} else {
			//se não for cliente e não for CIF então assume a situação tributária enviada pelo Web Service
			doctoServicoDadosCliente.setTpSituacaoTributariaRemetente( cotacaoWebService.getTpSituacaoTributariaRemetente() );
		}
		
		/** dados do destinatário */
		typedFlatMapAux.put("pessoa.nrIdentificacao", cotacaoWebService.getNrIdentifClienteDest() );		
		List dadosClienteDestino = new ArrayList<Map>();
		
		if( PESSOA_JURIDICA.equalsIgnoreCase(cotacaoWebService.getTpPessoaDestinatario())){
			dadosClienteDestino = findDadosCliente( typedFlatMapAux );
		}

		Map dadosClienteDestinatarioMap = new HashMap();
		if(dadosClienteRemetente != null && !dadosClienteDestino.isEmpty() ){
			dadosClienteDestinatarioMap = (Map)dadosClienteDestino.get(0);
			
			Cliente cliente = getCliente(cotacaoWebService, dadosClienteDestinatarioMap, DESTINATARIO);
			conhecimento.setClienteByIdClienteDestinatario(cliente);
			conhecimento.getClienteByIdClienteDestinatario().getPessoa().setNmPessoa(cliente.getPessoa().getNmPessoa());
			doctoServicoDadosCliente.setIdClienteDestinatario(cliente.getIdCliente());
			doctoServicoDadosCliente.setBlDificuldadeEntregaDestinatario(cliente.getBlDificuldadeEntrega());
			
			// Taxas de dificuldade de entrega
			doctoServicoDadosCliente.setBlAgendamentoEntregaDestinatario(cliente.getBlAgendamentoEntrega());
			doctoServicoDadosCliente.setBlCustoDescargaDestinatario(cliente.getBlCustoDescarga());
			doctoServicoDadosCliente.setBlPaletizacaoDestinatario(cliente.getBlPaletizacao());
			doctoServicoDadosCliente.setBlVeiculoDedicadoDestinatario(cliente.getBlVeiculoDedicado());
			
			TipoTributacaoIE tipoTributacaoIE = null;
			if( cliente.getPessoa().getTpPessoa().getValue().equalsIgnoreCase("J") ){
				List<TipoTributacaoIE> listTpTributacao = (List)dadosClienteDestinatarioMap.get("inscricaoEstadual");
				boolean iEValido = validaInscricaoEstadual(listTpTributacao, cotacaoWebService.getNrInscricaoEstadualDestinatario(),
						calculoFreteWebServiceRetorno, DESTINATARIO);
				
				if( iEValido ){
					tipoTributacaoIE = getTipoTributacao(cliente.getPessoa(), listTpTributacao,
							cotacaoWebService.getNrInscricaoEstadualDestinatario());
					
					validaSituacaoTributaria(cotacaoWebService.getTpSituacaoTributariaDestinatario(),
							tipoTributacaoIE.getTpSituacaoTributaria().getValue(),
							calculoFreteWebServiceRetorno, DESTINATARIO);
			}
				
			}else if( cliente.getPessoa().getTpPessoa().getValue().equalsIgnoreCase("F") ){
				List<TipoTributacaoIE> listTpTributacao = (List)dadosClienteDestinatarioMap.get("inscricaoEstadual");
				tipoTributacaoIE = listTpTributacao.get(0);
			}
			
			
			InscricaoEstadual inscricaoEstadualDestinatario = new InscricaoEstadual();
			inscricaoEstadualDestinatario.setIdInscricaoEstadual(tipoTributacaoIE.getInscricaoEstadual().getIdInscricaoEstadual());
			conhecimento.setInscricaoEstadualDestinatario(inscricaoEstadualDestinatario);
			doctoServicoDadosCliente.setIdInscricaoEstadualDestinatario(inscricaoEstadualDestinatario.getIdInscricaoEstadual());
			
			doctoServicoDadosCliente.setTpSituacaoTributariaDestinatario(cotacaoWebService.getTpSituacaoTributariaDestinatario());
			
			if (ConstantesExpedicao.TP_FRETE_FOB.equals( cotacaoWebService.getTpFrete() )) {
				
				conhecimento.setTpDevedorFrete(domainValueService.findDomainValueByValue("DM_TIPO_DEVEDOR_FRETE", "D"));
				
				List<DevedorDocServ> list = new ArrayList<DevedorDocServ>();
				DevedorDocServ devedorDocServ = new DevedorDocServ();
				devedorDocServ.setCliente(cliente);
				list.add(devedorDocServ);
				conhecimento.setDevedorDocServs(list);
				
				doctoServicoDadosCliente.setTpSituacaoTributariaResponsavel( cotacaoWebService.getTpSituacaoTributariaDestinatario() );
				
				DivisaoCliente divisaoCliente = getDivisaoCliente( dadosClienteDestinatarioMap,
						cotacaoWebService, calculoFreteWebServiceRetorno, servico, DESTINATARIO );
				
				conhecimento.setDivisaoCliente(divisaoCliente);
			}
			
		} else if( dadosClienteDestino.isEmpty() && cotacaoWebService.getTpFrete().equalsIgnoreCase(ConstantesExpedicao.TP_FRETE_FOB) ){
			calculoFreteWebServiceRetorno.getErrorList().add( getConfiguracoesFacadeImpl().getMensagem("LMS-01184") );
			} else {
			//se não for cliente e não for FOB então assume a situação tributária enviada pelo Web Service
			doctoServicoDadosCliente.setTpSituacaoTributariaDestinatario(cotacaoWebService.getTpSituacaoTributariaDestinatario());
			}

		/** Municipio Origem */
		Municipio municipioOrigem = findMunicipio(cotacaoWebService.getCepOrigem()); 
			
			if( municipioOrigem == null ){
			calculoFreteWebServiceRetorno.getErrorList().add( getConfiguracoesFacadeImpl().getMensagem("LMS-01315") );
			return calculoFreteWebServiceRetorno;
		}else{
			conhecimento.setMunicipioByIdMunicipioColeta(municipioOrigem);
			}
		doctoServicoDadosCliente.setIdMunicipioRemetente(municipioOrigem.getIdMunicipio());
		doctoServicoDadosCliente.setIdUfRemetente(municipioOrigem.getUnidadeFederativa().getIdUnidadeFederativa());
		doctoServicoDadosCliente.setNrCepRemetente(cotacaoWebService.getCepOrigem());
			
		/** Municipio Destino */
		Municipio municipioDestino = findMunicipio(cotacaoWebService.getCepDestino());
			if( municipioDestino == null ){
			calculoFreteWebServiceRetorno.getErrorList().add( getConfiguracoesFacadeImpl().getMensagem("LMS-01316") );
			return calculoFreteWebServiceRetorno;
		}else{
			conhecimento.setMunicipioByIdMunicipioEntrega(municipioDestino);
			}
		doctoServicoDadosCliente.setIdMunicipioDestinatario(municipioDestino.getIdMunicipio());
		doctoServicoDadosCliente.setIdUfDestinatario(municipioDestino.getUnidadeFederativa().getIdUnidadeFederativa());
			
						
		/** Filial origem */
		Filial filialOrigem = getIdFilialAtende(cotacaoWebService.getCepOrigem(), municipioOrigem, servico, Boolean.TRUE); 
		filialOrigem = filialService.findById(filialOrigem.getIdFilial());
	
		UnidadeFederativa unidadeFederativa = unidadeFederativaService.findUFByIdMunicipio(municipioOrigem.getIdMunicipio());
		unidadeFederativa.setPais(paisService.findPaisByIdMunicipio(filialOrigem.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio()));
		
		filialOrigem.getPessoa().getEnderecoPessoa().getMunicipio().setUnidadeFederativa(unidadeFederativa);
		conhecimento.setFilialByIdFilialOrigem(filialOrigem);
		doctoServicoDadosCliente.setFilialTransacao(filialOrigem);
		
		doctoServicoDadosCliente.setIdUfFilialUsuario(unidadeFederativa.getIdUnidadeFederativa());
				
		/** Filial destino */
		Filial filialDestino = getIdFilialAtende(cotacaoWebService.getCepDestino(), municipioDestino, servico, Boolean.FALSE);
		conhecimento.setFilialByIdFilialDestino(filialDestino);
		
		//Modal Aereo
		if(ConstantesExpedicao.AEREO_NACIONAL_CONVENCIONAL.equals(cotacaoWebService.getTpServico())) {
			//Aeroportos
			Long idAeroportoOrigem = MapUtils.getLong(filialService.findAeroportoFilial(conhecimento.getFilialByIdFilialOrigem().getIdFilial()), "idAeroporto");
			Aeroporto aeroportoOrigem = new Aeroporto();
			aeroportoOrigem.setIdAeroporto(idAeroportoOrigem);
			conhecimento.setAeroportoByIdAeroportoOrigem(aeroportoOrigem);
	
			Long idAeroportoDestino = MapUtils.getLong(filialService.findAeroportoFilial(conhecimento.getFilialByIdFilialDestino().getIdFilial()), "idAeroporto");
			Aeroporto aeroportoDestino = new Aeroporto();
			aeroportoDestino.setIdAeroporto(idAeroportoDestino);
			conhecimento.setAeroportoByIdAeroportoDestino(aeroportoDestino);
		}
		
		
		
		doctoServicoDadosCliente.setBlCotacaoRemetente(true);
		conhecimento.setDadosCliente(doctoServicoDadosCliente);

		conhecimento.setVlMercadoria( new BigDecimal(cotacaoWebService.getVlMercadoria()) );
		conhecimento.setPsReal( new BigDecimal(cotacaoWebService.getPsReal()) );
		
		if(cotacaoWebService.getQtVolumes() != null){
			conhecimento.setQtVolumes(cotacaoWebService.getQtVolumes().intValue());
		}
		
		if (cotacaoWebService.getPsCubado() != null)
			conhecimento.setPsAforado( new BigDecimal(cotacaoWebService.getPsCubado()) );
		
		Densidade densidade = densidadeService.findByTpDensidade("A");
		conhecimento.setDensidade(densidade);
				
		CalculoFrete calculoFrete = new CalculoFrete();
		
		/** Calcula as parcelas */
		calculoFrete.setBlCalculaParcelas(true);
		calculoFrete.setIdDivisaoCliente(conhecimento.getDivisaoCliente().getIdDivisaoCliente());		
		
		frete.setCalculoFrete(calculoFrete);
		frete.setConhecimento(conhecimento);
				
		return calculoFreteWebServiceRetorno;
	}
	
	
	/**
	 * Configura os atributos da filial 
	 * @param cotacaoWebService
	 * @param municipio
	 * @param servico
	 * @param tipo
	 * @return
	 */
	private Filial getIdFilialAtende(String nroCEP, Municipio municipio, Servico servico, Boolean blIndicadorColeta){
		TypedFlatMap map = new TypedFlatMap();
		
		map.put("nrCep", nroCEP );
		map.put("idMunicipio", municipio.getIdMunicipio() );
		map.put("idServico", servico.getIdServico() );
		map.put("blIndicadorColeta", blIndicadorColeta );
		Map mapDadosFilial = findFilialDadosByMunicipioNrCepServico( map ); 
		Filial filial = new Filial();
		filial.setIdFilial( (Long)mapDadosFilial.get("idFilial") );

		return filial;
	}
	
	
	/**
	 * Configura os atributos do município
	 * @param cotacaoWebService
	 * @param tipo
	 * @return
	 */
	private Municipio findMunicipio( String nrCep ){
		List municipioList = new ArrayList();
		Municipio municipio = null;
		
		TypedFlatMap map = new TypedFlatMap();
		
		map.put("municipio.unidadeFederativa.pais.idPais", ID_PAIS_BRASIL);
		map.put("cepCriteria", nrCep );
		
			municipioList = findCepLookup(map);
		
		if( !municipioList.isEmpty() ){
			Cep cep = (Cep)municipioList.get(0);
			municipio = new Municipio();
			municipio.setIdMunicipio( cep.getMunicipio().getIdMunicipio() );
			municipio.setNmMunicipio( cep.getMunicipio().getNmMunicipio() );
			municipio.setNrCep( cep.getNrCep() );
			municipio.setUnidadeFederativa(cep.getMunicipio().getUnidadeFederativa());
		}
		
		return municipio;
	}
	
	private void populateServicosAdicionais(CalculoFreteWebServiceRetorno calculoFreteWebServiceRetorno, List<ServicoAdicionalPrecificado> servicosAdicionais) {
		List<ServicoAdicionalWebService> servicos = new ArrayList<ServicoAdicionalWebService>();
				
		for(ServicoAdicionalPrecificado servicoAdicional : servicosAdicionais) {
			ServicoAdicionalWebService servico = new ServicoAdicionalWebService();
			servico.setNmServico(servicoAdicional.getDsParcela());
			
			if(servicoAdicional.getVlServico() != null) {
				servico.setVlServico(servicoAdicional.getVlServico().toString());
			}
			
			servico.setDsComplemento(tabelaServicoAdicionalService.findDsFormatada(servicoAdicional, Boolean.FALSE, DEFAULT_LOCALE));			
			servico.setSgMoeda(servicoAdicional.getMoeda().getDsSimbolo());
			servicos.add(servico);	
		}
		calculoFreteWebServiceRetorno.setServicosAdicionais(servicos);
	}
	
	/**
	 * Popula do pojo de retorno com as informações do cálculo do frete
	 * @param calculoFreteWebServiceRetorno
	 * @param map
	 * @return
	 */
	private void populaPojoCalculoFreteWebServiceRetorno( CalculoFreteWebServiceRetorno calculoFreteWebServiceRetorno, 
			Map<String, Object> mapRetorno, Frete frete ){
		
		calculoFreteWebServiceRetorno.setVlDesconto( mapRetorno.get("vlDesconto").toString() ); 
		calculoFreteWebServiceRetorno.setVlImposto( mapRetorno.get("vlImposto").toString() );
		calculoFreteWebServiceRetorno.setVlTotalTributos(mapRetorno.get("vlTotalTributos").toString() );
		calculoFreteWebServiceRetorno.setVlICMSubstituicaoTributaria( mapRetorno.get("vlICMSubstituicaoTributaria").toString() );
		calculoFreteWebServiceRetorno.setVlDevido(mapRetorno.get("vlDevido").toString() );
		calculoFreteWebServiceRetorno.setVlTotalFrete( mapRetorno.get("vlTotalFrete").toString() );
		calculoFreteWebServiceRetorno.setVlTotalServico( mapRetorno.get("vlTotalServico").toString() );
		calculoFreteWebServiceRetorno.setVlTotalCtrc( mapRetorno.get("vlTotalCtrc").toString() );
		calculoFreteWebServiceRetorno.setBlIncidenciaIcmsPedagio((Boolean) mapRetorno.get("blIncidenciaIcmsPedagio"));
		
		if( frete.getConhecimento().getClienteByIdClienteRemetente() != null ){
			calculoFreteWebServiceRetorno.setNmRemetente( frete.getConhecimento().getClienteByIdClienteRemetente().getPessoa().getNmPessoa() ); 
		}
		calculoFreteWebServiceRetorno.setNmMunicipioOrigem( frete.getConhecimento().getMunicipioByIdMunicipioColeta().getNmMunicipio() ); 
		TypedFlatMap telefoneOrigem = telefoneEnderecoService.findTelefoneEnderecoByIdPessoaTpTelefone( 
								frete.getConhecimento().getFilialByIdFilialOrigem().getIdFilial(), "C");
		if(telefoneOrigem != null) {
			calculoFreteWebServiceRetorno.setNrDDDFilialOrigem( telefoneOrigem.getString("nrDdd") );
			calculoFreteWebServiceRetorno.setNrTelefoneFilialOrigem( telefoneOrigem.getString("nrTelefone") );
	}
	
		if(  frete.getConhecimento().getClienteByIdClienteDestinatario() != null ){
			calculoFreteWebServiceRetorno.setNmDestinatario( frete.getConhecimento().getClienteByIdClienteDestinatario().getPessoa().getNmPessoa() ); 
		}
		calculoFreteWebServiceRetorno.setNmMunicipioDestino( frete.getConhecimento().getMunicipioByIdMunicipioEntrega().getNmMunicipio() );
		TypedFlatMap telefoneDestino = telefoneEnderecoService.findTelefoneEnderecoByIdPessoaTpTelefone(
				frete.getConhecimento().getFilialByIdFilialDestino().getIdFilial(), "C");
		if(telefoneDestino != null) {
			calculoFreteWebServiceRetorno.setNrDDDFilialDestino( telefoneDestino.getString("nrDdd") );
			calculoFreteWebServiceRetorno.setNrTelefoneFilialDestino( telefoneDestino.getString("nrTelefone") );
	}
	
		List<ParcelasFreteWebService> parcelasFreteList = new ArrayList<ParcelasFreteWebService>();
		List<Map> parcelasRetorno = (List)mapRetorno.get("parcelasFrete");
		for ( Map parcela : parcelasRetorno ) {
			ParcelasFreteWebService parcelasFreteWebService = new ParcelasFreteWebService();
			parcelasFreteWebService.setDsParcela( parcela.get("nmParcelaPreco").toString() );
			parcelasFreteWebService.setVlParcela( ((BigDecimal)parcela.get("vlParcela")).setScale(2, RoundingMode.HALF_EVEN).toString() );
			parcelasFreteWebService.setVlParcelaBruto(parcela.get("vlParcelaBruto") != null ? ((BigDecimal) parcela.get("vlParcelaBruto")).toString() : null);
			parcelasFreteWebService.setIdParcelaPreco(parcela.get("idParcelaPreco") != null ? ((Long) parcela.get("idParcelaPreco")).toString() : null);
			parcelasFreteList.add( parcelasFreteWebService );
		}
		
		calculoFreteWebServiceRetorno.setParcelas( parcelasFreteList );
	}


	/**
	 * Divisões Cliente utilizado pelo web service
	 * @param List dadosCliente
	 * @return divisões do cliente
	 */
	public List<DivisaoCliente> findDivCliente(Map dadosCliente, Servico servico) {
				
		Long idCliente = (Long)dadosCliente.get("idCliente");
		Long idServico = servico.getIdServico();
		
		Map tpClienteMap = new HashMap();
		tpClienteMap = (Map)(dadosCliente.get("tpCliente"));
		String tpCliente = (String)tpClienteMap.get("value");
		
		if (idCliente != null && idServico != null && ClienteUtils.isParametroClienteEspecial(tpCliente) ) {
			List<DivisaoCliente> divisoesCliente = divisaoClienteService.findDivisaoClienteByIdServico(idCliente, idServico);
			return divisoesCliente;
			}
		return null;
	}
	
	/**
	 * Seta o cliente no conhecimento 
	 * @param conhecimento
	 * @param dadosClienteMap
	 * @param tipo
	 */
	private Cliente getCliente( CotacaoWebService cotacaoWebService, Map  dadosClienteMap, String tipo ){
		Pessoa pessoa = new Pessoa();
		Map mapPessoa = new HashMap();
		mapPessoa = (Map)dadosClienteMap.get("pessoa");
		pessoa.setNmPessoa(mapPessoa.get("nmPessoa").toString());	
		pessoa.setNrIdentificacao(mapPessoa.get("nrIdentificacao").toString());
		
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		if( tipo.equalsIgnoreCase(REMETENTE)){
			enderecoPessoa.setNrCep(cotacaoWebService.getCepOrigem());
		}else if( tipo.equalsIgnoreCase(DESTINATARIO) ){
			enderecoPessoa.setNrCep(cotacaoWebService.getCepDestino());
		}
		pessoa.setEnderecoPessoa(enderecoPessoa);
		
		Map mapTpPessoa = new HashMap();
		mapTpPessoa = (Map)mapPessoa.get("tpPessoa");			
		pessoa.setTpPessoa(domainValueService.findDomainValueByValue("DM_TIPO_PESSOA", mapTpPessoa.get("value").toString() ));

		Cliente cliente = new Cliente( Long.parseLong(dadosClienteMap.get("idCliente").toString()) );
		cliente.setPessoa(pessoa);
		cliente.setBlDificuldadeEntrega((Boolean)dadosClienteMap.get("blDificuldadeEntrega"));
		
		return cliente;
	}
		
	

	/**
	 * Retorna o Tipo de Tributação de acordo com a IE enviada pelo Web Service  
	 * @param pessoa
	 * @param listTpTributacao
	 * @param nrInscricaoEstadual
	 */
	private TipoTributacaoIE getTipoTributacao( Pessoa pessoa, List<TipoTributacaoIE> listTpTributacao, String nrInscricaoEstadual ){
		TipoTributacaoIE tipoTributacaoIE = null;
		
		if( pessoa.getTpPessoa().getValue().equalsIgnoreCase("J") ){
			for (TipoTributacaoIE tipoTributacaoIETemp : listTpTributacao) {
				if( tipoTributacaoIETemp.getInscricaoEstadual().getNrInscricaoEstadual().equalsIgnoreCase(nrInscricaoEstadual)){
					tipoTributacaoIE = tipoTributacaoIETemp; 
			}
			}
		} else if( pessoa.getTpPessoa().getValue().equalsIgnoreCase("F") ){
			tipoTributacaoIE = (TipoTributacaoIE)listTpTributacao.get(0);
		}
		
		return tipoTributacaoIE;
	}
			
	

	private DivisaoCliente getDivisaoCliente( Map dadosCliente, CotacaoWebService cotacaoWebService,  
			CalculoFreteWebServiceRetorno calculoFreteWebServiceRetorno, Servico servico, String tipo ) throws Exception{
		
		List<DivisaoCliente> divisaoClienteList = findDivCliente( dadosCliente, servico );
		DivisaoCliente divisaoCliente = validaDivisaoCliente( divisaoClienteList, cotacaoWebService.getCdDivisaoCliente(),
				calculoFreteWebServiceRetorno, tipo );
		
		return divisaoCliente;
		
			}
	
	/**
	 * Verifica se a divisão do cliente enviada pelo Web Service está cadastrada para o cliente. Caso esteja cadastrada retorna
	 * a divisão senão retorna null 
	 * @param divisaoClienteList
	 * @param cdDivisaoCliente
	 * @param calculoFreteWebServiceRetorno
	 * @param tipo
	 * @return
	 */
	private DivisaoCliente validaDivisaoCliente( List<DivisaoCliente> divisaoClienteList, Long cdDivisaoCliente,
			CalculoFreteWebServiceRetorno calculoFreteWebServiceRetorno, String tipo ) throws Exception{
		
		DivisaoCliente divisaoCliente = new DivisaoCliente();;
		
		if( divisaoClienteList != null ){
			for (DivisaoCliente divisaoClienteTemp : divisaoClienteList) {
				if( divisaoClienteTemp.getCdDivisaoCliente().equals(cdDivisaoCliente) ){
					divisaoCliente = divisaoClienteTemp;
					
		}		
			}
		}
		
		if ( divisaoClienteList == null || divisaoCliente.getIdDivisaoCliente() == null ){
			String erro = configuracoesFacadeImpl.getMensagem("LMS-01183", new Object[]{tipo});
			
			calculoFreteWebServiceRetorno.getErrorList().add( erro );
			throw new BusinessException( erro );			
	}
		return divisaoCliente;
	}
	
	/**
	 * Verifica se o tipo de tributação enviada pelo Web Service está cadastrada para o cliente.
	 * @param listTpTributacao
	 * @param nrInscricaoEstadual
	 * @param calculoFreteWebServiceRetorno 
	 * @param tipo
	 */
	private boolean validaInscricaoEstadual( List<TipoTributacaoIE> listTpTributacao, String nrInscricaoEstadual, 
			CalculoFreteWebServiceRetorno calculoFreteWebServiceRetorno, String tipo) throws Exception{
		
		boolean ieValido = false;
		for (TipoTributacaoIE tipoTributacaoIE : listTpTributacao) {
			if( tipoTributacaoIE.getInscricaoEstadual().getNrInscricaoEstadual().equalsIgnoreCase(nrInscricaoEstadual) ){
				ieValido = true;
				return ieValido;
			} 
		}
		
		if( !ieValido ){
			String erro = configuracoesFacadeImpl.getMensagem("LMS-01182", new Object[]{tipo});
			
			calculoFreteWebServiceRetorno.getErrorList().add( erro );
			throw new BusinessException( erro );
		}
		return false;
	}
	
	/**
	 * Verifica se a situação tributária enviada pelo Web Service é igual a que está cadastrada para o cliente. 
	 * @param tpSituacaoTributariaWebService - tipo de situação tributária enviada pelo Web Service
	 * @param tpSituacaoTributariaCadastrada - tipo de situação tributária que está cadastrada para o cliente
	 * @throws Exception
	 */
	private void validaSituacaoTributaria( String tpSituacaoTributariaWebService, String tpSituacaoTributariaCadastrada,
			CalculoFreteWebServiceRetorno calculoFreteWebServiceRetorno, String tipo ) throws Exception{
		
		if( !tpSituacaoTributariaCadastrada.equalsIgnoreCase(tpSituacaoTributariaWebService) ){
			String erro = configuracoesFacadeImpl.getMensagem("LMS-01181", new Object[]{tipo});
			
			calculoFreteWebServiceRetorno.getErrorList().add( erro );
			throw new BusinessException( erro );
		}
		
	}
	

	/**
	 * Grava Cotacao na Sessao
	 * @param criteria
	 */
	public void storeInSession(TypedFlatMap criteria) {
		
		Cotacao cotacao = getCotacaoPersistentInSession();

		/** Remetente */
		Pessoa pessoa = new Pessoa();
		pessoa.setNmPessoa(criteria.getString("clienteByIdClienteRemetente.pessoa.nmPessoa"));
		pessoa.setNrIdentificacao(criteria.getString("clienteByIdClienteRemetente.pessoa.nrIdentificacao"));
		pessoa.setTpPessoa(domainValueService.findDomainValueByValue("DM_TIPO_PESSOA", criteria.getString("clienteByIdClienteRemetente.pessoa.tpPessoa")));

		Cliente clienteRemetente = new Cliente(criteria.getLong("clienteByIdClienteRemetente.idCliente"));
		String tpCliente = criteria.getString("clienteByIdClienteRemetente.tpCliente");
		if(StringUtils.isBlank(tpCliente)) {
			tpCliente = ConstantesVendas.CLIENTE_EVENTUAL;
		}
		clienteRemetente.setTpCliente(new DomainValue(tpCliente));
		clienteRemetente.setPessoa(pessoa);
		cotacao.setClienteByIdClienteSolicitou(clienteRemetente);
		cotacao.setTpSitTributariaRemetente(this.getTpSituacaoTributaria(criteria.getBoolean("blRemetenteContribuinte")));
		Long idIERemetente = criteria.getLong("clienteByIdClienteRemetente.idInscricaoEstadual");
		if(idIERemetente != null) {
			InscricaoEstadual inscricaoEstadualRemetente = new InscricaoEstadual();
			inscricaoEstadualRemetente.setIdInscricaoEstadual(idIERemetente);
			Pessoa pessoaIe = new Pessoa();
			pessoaIe.setNmPessoa(criteria.getString("clienteByIdClienteRemetente.dsInscricaoEstadual"));
			inscricaoEstadualRemetente.setPessoa(pessoaIe);
			cotacao.setInscricaoEstadualRemetente(inscricaoEstadualRemetente);
		}

		/** Destinatário */
		pessoa = new Pessoa();
		pessoa.setNmPessoa(criteria.getString("clienteByIdClienteDestinatario.pessoa.nmPessoa"));
		pessoa.setNrIdentificacao(criteria.getString("clienteByIdClienteDestinatario.pessoa.nrIdentificacao"));
		pessoa.setTpPessoa(domainValueService.findDomainValueByValue("DM_TIPO_PESSOA", criteria.getString("clienteByIdClienteDestinatario.pessoa.tpPessoa")));

		Cliente clienteDestinatario = new Cliente(criteria.getLong("clienteByIdClienteDestinatario.idCliente"));
		clienteDestinatario.setPessoa(pessoa);
		tpCliente = criteria.getString("clienteByIdClienteDestinatario.tpCliente");
		if(StringUtils.isBlank(tpCliente)) {
			tpCliente = ConstantesVendas.CLIENTE_EVENTUAL;
		}
		clienteRemetente.setTpCliente(new DomainValue(tpCliente));
		cotacao.setClienteByIdClienteDestino(clienteDestinatario);
		cotacao.setTpSitTributariaDestinatario(this.getTpSituacaoTributaria(criteria.getBoolean("blDestinatarioContribuinte")));
		Long idIEDestinatario = criteria.getLong("clienteByIdClienteDestinatario.idInscricaoEstadual");
		if(idIEDestinatario != null) {
			InscricaoEstadual inscricaoEstadualDestinatario = new InscricaoEstadual();
			inscricaoEstadualDestinatario.setIdInscricaoEstadual(idIEDestinatario);
			Pessoa pessoaIe = new Pessoa();
			pessoaIe.setNmPessoa(criteria.getString("clienteByIdClienteDestinatario.dsInscricaoEstadual"));
			inscricaoEstadualDestinatario.setPessoa(pessoaIe);
			cotacao.setInscricaoEstadualDestinatario(inscricaoEstadualDestinatario);
		}

		DomainValue tpFrete = criteria.getDomainValue("tpFrete");
		tpFrete.setDescription(criteria.getVarcharI18n("dsTpFrete"));
		cotacao.setTpFrete(tpFrete);

		//Cliente RESPONSAVEL
		if (ConstantesExpedicao.TP_FRETE_CIF.equals(tpFrete.getValue())) {
			cotacao.setClienteByIdCliente(clienteRemetente);
			cotacao.setTpSitTributariaResponsavel(cotacao.getTpSitTributariaRemetente());
		} else if (ConstantesExpedicao.TP_FRETE_FOB.equals(tpFrete.getValue())) {
			cotacao.setClienteByIdCliente(clienteDestinatario);
			cotacao.setTpSitTributariaResponsavel(cotacao.getTpSitTributariaDestinatario());
		}

		//Divisão Cliente
		DivisaoCliente divisaoCliente = new DivisaoCliente();
		divisaoCliente.setIdDivisaoCliente(criteria.getLong("divisaoCliente.idDivisaoCliente"));
		divisaoCliente.setDsDivisaoCliente(criteria.getString("divisaoCliente.dsDivisaoCliente"));
		cotacao.setDivisaoCliente(divisaoCliente);

		//Servico
		Servico servico = new Servico();
		servico.setIdServico(criteria.getLong("servico.idServico"));
		servico.setDsServico(criteria.getVarcharI18n("servico.dsServico"));

		DomainValue tpModal = criteria.getDomainValue("tpModal");
		tpModal.setDescription(criteria.getVarcharI18n("dsTpModal"));
		servico.setTpModal(tpModal);
		cotacao.setServico(servico);

		//Municipio Origem
		Municipio municipioOrigem = new Municipio();
		municipioOrigem.setIdMunicipio(criteria.getLong("idMunicipioOrigem"));
		municipioOrigem.setNmMunicipio(criteria.getString("nmMunicipioOrigem"));
		municipioOrigem.setNrCep(criteria.getString("nrCepOrigem"));

		Pais paisOrigem =  new Pais();
		paisOrigem.setSgPais(criteria.getString("sgPaisOrigem"));
		UnidadeFederativa ufOrigem = new UnidadeFederativa();
		ufOrigem.setPais(paisOrigem);
		municipioOrigem.setUnidadeFederativa(ufOrigem);
		cotacao.setMunicipioByIdMunicipioOrigem(municipioOrigem);

		//Filial Origem
		Long idFilialOrigem = criteria.getLong("idFilialOrigem");
		Filial filialOrigem = new Filial();
		filialOrigem.setIdFilial(idFilialOrigem);
		filialOrigem.setSgFilial(criteria.getString("sgFilialOrigem"));

		Pessoa pessoaFilialOrigem = new Pessoa();
		pessoaFilialOrigem.setNmPessoa(criteria.getString("nmFilialOrigem"));
		List telefoneEnderecos = new ArrayList();
		telefoneEnderecos.add(telefoneEnderecoService.findTelefoneEnderecoPadrao(idFilialOrigem));
		pessoaFilialOrigem.setTelefoneEnderecos(telefoneEnderecos);
		filialOrigem.setPessoa(pessoaFilialOrigem);
		cotacao.setFilialByIdFilialOrigem(filialOrigem);

		//Municipio Destino
		Municipio municipioDestino = new Municipio();
		municipioDestino.setIdMunicipio(criteria.getLong("idMunicipioDestino"));
		municipioDestino.setNmMunicipio(criteria.getString("nmMunicipioDestino"));
		municipioDestino.setNrCep(criteria.getString("nrCepDestino"));

		Pais paisDestino =  new Pais();
		paisDestino.setSgPais(criteria.getString("sgPaisDestino"));
		UnidadeFederativa ufDestino = new UnidadeFederativa();
		ufDestino.setPais(paisDestino);
		municipioDestino.setUnidadeFederativa(ufDestino);
		cotacao.setMunicipioByIdMunicipioDestino(municipioDestino);
		cotacao.setMunicipioByIdMunicipioEntrega(municipioDestino);

		//Filial Destino
		Long idFilialDestino = criteria.getLong("idFilialDestino");
		Filial filialDestino = new Filial();
		filialDestino.setIdFilial(idFilialDestino);
		filialDestino.setSgFilial(criteria.getString("sgFilialDestino"));

		Pessoa pessoaFilialDestino = new Pessoa();
		pessoaFilialDestino.setNmPessoa(criteria.getString("nmFilialDestino"));
		telefoneEnderecos = new ArrayList();
		telefoneEnderecos.add(telefoneEnderecoService.findTelefoneEnderecoPadrao(idFilialDestino));
		pessoaFilialDestino.setTelefoneEnderecos(telefoneEnderecos);
		filialDestino.setPessoa(pessoaFilialDestino);
		cotacao.setFilialByIdFilialDestino(filialDestino);

		Moeda moeda = new Moeda();
		moeda.setIdMoeda(criteria.getLong("moeda.idMoeda"));
		moeda.setDsMoeda(criteria.getVarcharI18n("dsMoeda"));
		cotacao.setMoeda(moeda);

		cotacao.setVlMercadoria(criteria.getBigDecimal("vlMercadoria"));

		cotacao.setPsCubado(criteria.getBigDecimal("psCubado"));
		cotacao.setPsReal(criteria.getBigDecimal("psReal"));

		cotacao.setBlFrete(Boolean.TRUE);
		cotacao.setBlServicosAdicionais(Boolean.FALSE);

		/** Valida Dados */
		cotacaoFreteViaWebService.validateCotacao(cotacao);

		VendasUtils.setCotacaoInSession(cotacao);
	}

	/**
	 * Busca Cotacao na Sessão, mantendo as dimensoes.
	 * @return
	 */
	private Cotacao getCotacaoPersistentInSession() {
		Cotacao cotacao = VendasUtils.getCotacaoInSession();
		Cotacao cotacaoNova = new Cotacao();
		cotacaoNova.setTpDocumentoCotacao(cotacao.getTpDocumentoCotacao());
		cotacaoNova.setDimensoes(cotacao.getDimensoes());
		return cotacaoNova;
	}

	/**
	 * Busca se Cliente eh ou nao Contribuinte
	 * @param isContribuinte
	 * @return
	 */
	private DomainValue getTpSituacaoTributaria(Boolean isContribuinte) {
		if (Boolean.TRUE.equals(isContribuinte)) {
			return new DomainValue("CO");
		}
		return new DomainValue("NC");
	}

	
	/**
	 * Retorna os dados da filial responsavel por efetuar o atendimento do municipio
	 * @param criteria
	 * @return
	 */
	@MethodSecurity(processGroup="vendas.calculoFreteWebService", processName="findAtendimentoMunicipio", authenticationRequired=false )
	public Map<String, Object> findAtendimentoMunicipio(TypedFlatMap criteria) {
		Long idMunicipio = criteria.getLong("idMunicipio");
		Long idServico = criteria.getLong("idServico");
		Boolean blIndicadorColeta = criteria.getBoolean("blIndicadorColeta");
		YearMonthDay dtConsulta = criteria.getYearMonthDay("dtConsulta");
		String nrCep = criteria.getString("nrCep");
		Long idCliente = criteria.getLong("idCliente");
		Long idClienteRemetente = criteria.getLong("idClienteRemetente");
		Long idSegmento = criteria.getLong("idSegmento");
		Long idUfOrigem = criteria.getLong("idUfOrigem");
		Long idFilialOrigem = criteria.getLong("idFilialOrigem");
		String blGeracaoMCD = criteria.getString("blGeracaoMCD");
		Long idNaturezaProduto = criteria.getLong("idNaturezaProduto");
		String error = "";
		
		Map<String, Object> retorno = new HashMap<String, Object>();
		if (idMunicipio != null && idServico != null && blIndicadorColeta != null) {
			try {
				retorno = ppeService.findAtendimentoMunicipio(
					idMunicipio,
					idServico,
					blIndicadorColeta,
					dtConsulta,
					nrCep,
					idCliente,
					idClienteRemetente,
					idSegmento,
					idUfOrigem,
					idFilialOrigem,
					blGeracaoMCD,
					idNaturezaProduto,
					null);
			} catch (RuntimeException e) {
				error = "O CEP ou município informado não é atendido.";
				retorno.put("error", error);
			}
		} else {
			error = "Os campos idMunicipio, idServico e blIndicadorColeta são obrigatórios.";
			retorno.put("error", error);
		}
		
		return retorno;
	}
	
	/**
	 * Obtem o grupo região para o municipio
	 *
	 * @param criteria contendo o idMunicipio e o idTabelaPreco
	 * @return GrupoRegiao
	 */
	@MethodSecurity(processGroup="vendas.calculoFreteWebService", processName="findGrupoRegiao", authenticationRequired=false)
	public GrupoRegiao findGrupoRegiao(TypedFlatMap criteria){
		GrupoRegiao retorno = new GrupoRegiao();
		Long idMunicipio = criteria.getLong("idMunicipio");
		Long idTabelaPreco = criteria.getLong("idTabelaPreco");
		
		if (idMunicipio != null && idTabelaPreco != null) {
			retorno = municipioGrupoRegiaoService.findGrupoRegiaoAtendimento(idTabelaPreco, idMunicipio);
		}
		
		return retorno;
	}
	
	/**
	 * Verifica se o cep é válido, se valido retorna o Municipio e a UF.
	 *
	 * @param criteria contendo o numero do cep
	 * @return Cep
	 */
	@MethodSecurity(processGroup="vendas.calculoFreteWebService", processName="validateCep", authenticationRequired=false)
	public List<Cep> validateCep(TypedFlatMap criteria){
		List<Cep> retorno = null;
		String nrCep = criteria.getString("cep");
		if (nrCep != null) {
			retorno = cepService.findCepByNrCep(nrCep);
		}
		
		return retorno;
	}
	
	
	
	/**
	 * Calcular o prazo previsto de entrega em dias
	 *
	 * @param criteria
	 * @return Long prazo em dias
	 */
	@MethodSecurity(processGroup="vendas.calculoFreteWebService", processName="calcularPpe", authenticationRequired=false)
	public Map<String, Long> calcularPpe(TypedFlatMap criteria){
		Short ppe = -1;
		Long idMunicipioOrigem = criteria.getLong("idMunicipioOrigem");
		Long idMunicipioDestino = criteria.getLong("idMunicipioDestino");
		Long idServico = criteria.getLong("idServico");
		
		if (idMunicipioOrigem != null && idMunicipioDestino != null && idServico != null) {
			ppe = cotacaoService.validateCalculoPpe(idServico, idMunicipioOrigem, idMunicipioDestino);
		}
		
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("nrPpe", ppe.longValue());
		
		return map;
	}
	
	/**
	 * Gera o número da cotação acessando a ultima cotacao da filial e somando 1
	 * @param idFilial
	 * @return Map com o numero da cotacao
	 */
	@MethodSecurity(processGroup="vendas.calculoFreteWebService", processName="geraNroCotacao", authenticationRequired=false)
	public Map<String, Integer> geraNroCotacao(TypedFlatMap criteria) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Long idFilial = criteria.getLong("idFilialOrigem");
		
		if (idFilial != null) {
			int nroCotacao = conteudoParamFilialService.generateProximoValorParametroSequencial(idFilial, ConstantesVendas.NR_ULTIMA_COTACAO, true).intValue();
			map.put("nroCotacao", nroCotacao);
		}
		
		return map;
	}
	
	/**
	 * Soma N dias uteis do municipio e retorna a data do resultado
	 * @param criteria(
	 * date Data base,
	 * numeroDias Numero de dias uteis a serem adicionados,
	 * idMunicipio Id do municipio)
	 * @return DateTime Data do dia util após a soma de N dias para a determinada filial
	 */
	@MethodSecurity(processGroup="vendas.calculoFreteWebService", processName="somaNDiasUteis", authenticationRequired=false)
	public Map<String, Object> somaNDiasUteis(TypedFlatMap criteria) {
		Map<String, Object> map = new HashMap<String, Object>();
		DateTime dateInicial = criteria.getDateTime("dateInicial");
		int numeroDias = criteria.getInteger("numeroDias");
		Long idMunicipio = criteria.getLong("idMunicipio");
		String error = "";
		
		try {
			DateTime time = diaUtils.somaNDiasUteis(dateInicial, numeroDias, idMunicipio);
			map.put("dateTime", time);
		} catch (Exception e) {
			error = "Nao foi possivel realizar a soma de dias uteis.";
			map.put("error", error);
		}
		
		return map;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@MethodSecurity(processGroup="vendas.calculoFreteWebService", processName="imprimirCotacoes", authenticationRequired=false)
	public Map imprimirCotacoes(TypedFlatMap criteria) throws Exception{
		Usuario usuario = usuarioService.findUsuarioByLogin((String) configuracoesFacade.getValorParametro("USUARIO_RADAR_WEB"));
		SessionContext.setUser(usuario);
		SessionContext.set("PAIS_KEY", paisService.findPaisBySgPais("BRA"));
		
		MultiReportCommand mrc = new MultiReportCommand("arquivo"); 
		Long[] idCotacoes = (Long[]) criteria.get("idCotacoes");
		for (Long idCotacao : idCotacoes) {
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idCotacao", idCotacao);
			mrc.addCommand("lms.vendas.emitirCotacaoPrecoService", tfm);
		}

		File reportFile = this.reportExecutionManager.executeMultiReport(mrc);

		Map retorno = new HashMap();
		
		if(criteria.getBoolean("enviarPorEmail") != null){
			retorno.put("reportFile", reportFile);
		} else {
			FileInputStream  fi = new FileInputStream(reportFile);
			final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] temp = new byte[1024];
			int read;
			while((read = fi.read(temp)) >= 0){
			   buffer.write(temp, 0, read);
			}
			fi.close();
			retorno.put("relatorio", buffer.toByteArray());
		}
		return retorno;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@MethodSecurity(processGroup="vendas.calculoFreteOnlineWebService", processName="enviarEmailCotacoes", authenticationRequired=false)
	public Map enviarEmailCotacoes(TypedFlatMap criteria) throws Exception {
		final String strTo = criteria.getString("dsEmail");
		String mensagem = configuracoesFacade.getMensagem("radarSucessoEmailCotacaoOnline", new String[]{strTo});
		
		Long[] idCotacoes = (Long[]) criteria.get("idCotacoes");
		StringBuilder dsCotacaoHtml = new StringBuilder("");
		for (Long idCotacao : idCotacoes) {
			Cotacao cotacao = cotacaoService.findCotacaoByIdCotacao(idCotacao);
			
			dsCotacaoHtml.append("Cotação: " + cotacao.getFilialByIdFilialOrigem().getSgFilial()+ "- "  + cotacao.getNrCotacao()); 
			dsCotacaoHtml.append(" &nbsp;&nbsp; Validade: " +cotacao.getDtValidade().toString("dd/MM/yyyy"));
			
			if (cotacao.getFilialByIdFilialOrigem().getIdFilial() != null) {
				Long idFilial = cotacao.getFilialByIdFilialOrigem().getIdFilial();
				TypedFlatMap telefone = telefoneEnderecoService.findTelefoneEnderecoByIdPessoaTpTelefone(idFilial, "C");
				if(telefone != null) {
					String ddd = "("+telefone.getString("nrDdd") + ")";
					String nrTelefone = telefone.getString("nrTelefone");
					dsCotacaoHtml.append(" &nbsp;&nbsp; Tel. Filial Origem: "+FormatUtils.formatTelefone(ddd + nrTelefone)+" <br>");
				}else{
					dsCotacaoHtml.append("<br>");
				}
			}
			
			
		}
		
		final String strFrom = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		final String strSubject = configuracoesFacade.getMensagem("radarAssuntoEmailCotacaoOnline");
		final String conteudo = configuracoesFacade.getMensagem("radarConteudoEmailCotacaoOnline", new String[]{dsCotacaoHtml.toString()});
		final String reportName = criteria.getString("reportName");
		criteria.put("enviarPorEmail", true);
		Map mapRelatorio = this.imprimirCotacoes(criteria);
		final File relatorioAnexo = (File) mapRelatorio.get("reportFile");

		this.sendEmail(strTo, strSubject, strFrom, conteudo, relatorioAnexo, reportName);
		
		Map retorno = new HashMap();
		retorno.put("mensagem", mensagem);
		return retorno;
	}

	private void sendEmail(String strEmails, String strSubject, String strFrom, 
	String strText, File anexo, String nomeAnexo){
		Mail mail = createMail(strEmails, strFrom, strSubject, strText, anexo, nomeAnexo);
		
			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}
	
	private Mail createMail(String strTo, String strFrom, String strSubject, 
	String body, File anexo, String nomeAnexo) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		
		MailAttachment mailAttachment = new MailAttachment();
		mailAttachment.setName(nomeAnexo);
		mailAttachment.setData(FileUtils.readFile(anexo));
		MailAttachment[] mailAttachmentArr = {mailAttachment};
		mail.setAttachements(mailAttachmentArr);
		
		return mail;
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public void setCepService(CepService cepService) {
		this.cepService = cepService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}
	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}
	public void setCotacaoFreteViaWebService(CotacaoFreteViaWebService cotacaoFreteViaWebService) {
		this.cotacaoFreteViaWebService = cotacaoFreteViaWebService;
	}
	public void setTipoTributacaoIEService(TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public ConfiguracoesFacadeImpl getConfiguracoesFacadeImpl() {
		return configuracoesFacadeImpl;
	}

	public void setConfiguracoesFacadeImpl(
			ConfiguracoesFacadeImpl configuracoesFacadeImpl) {
		this.configuracoesFacadeImpl = configuracoesFacadeImpl;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public DomainService getDomainService() {
		return domainService;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public MoedaService getMoedaService() {
		return moedaService;
	}

	public CotacaoFreteViaWebCalculoFreteAction getCotacaoFreteViaWebCalculoFreteAction() {
		return cotacaoFreteViaWebCalculoFreteAction;
	}

	public void setCalcularFreteService(CalcularFreteService calcularFreteService) {
		this.calcularFreteService = calcularFreteService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


	public FilialService getFilialService() {
		return filialService;
	}


	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}


	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}


	public PaisService getPaisService() {
		return paisService;
	}


	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}


	public void setDensidadeService(DensidadeService densidadeService) {
		this.densidadeService = densidadeService;
	}
	
	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}

	public void setCotacaoFreteViaWebCalculoFreteAction(
			CotacaoFreteViaWebCalculoFreteAction cotacaoFreteViaWebCalculoFreteAction) {
		this.cotacaoFreteViaWebCalculoFreteAction = cotacaoFreteViaWebCalculoFreteAction;
	}

	public void setMunicipioGrupoRegiaoService(
			MunicipioGrupoRegiaoService municipioGrupoRegiaoService) {
		this.municipioGrupoRegiaoService = municipioGrupoRegiaoService;
	}

	public void setConteudoParamFilialService(
			ConteudoParametroFilialService conteudoParamFilialService) {
		this.conteudoParamFilialService = conteudoParamFilialService;
	}

	public void setDiaUtils(DiaUtils diaUtils) {
		this.diaUtils = diaUtils;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setTabelaServicoAdicionalService(TabelaServicoAdicionalService tabelaServicoAdicionalService) {
		this.tabelaServicoAdicionalService = tabelaServicoAdicionalService;
	}
}
